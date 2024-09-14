///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDMDGetSchemas.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

 //////////////////////////////////////////////////////////////////////
 //
 //
 //
 //
 //
 ////////////////////////////////////////////////////////////////////////
 //
 // File Name:    JDDMDGetSchemas.java
 //
 // Classes:      JDDMDGetSchemas
 //
 ////////////////////////////////////////////////////////////////////////
 //
 //
 //
 //
 //
 //
 //
 ////////////////////////////////////////////////////////////////////////

package test.JD.DMD;

import com.ibm.as400.access.AS400;

import test.JDDMDTest;
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Statement;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import java.sql.Types;
import java.util.Hashtable;



/**
Testcase JDDMDGetSchemas.  This tests the following methods
of the JDBC DatabaseMetaData class:

<ul>
<li>getSchemas()
</ul>
**/
public class JDDMDGetSchemas
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDDMDGetSchemas";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDDMDTest.main(newArgs); 
   }



    // Private data.
    private Connection          connection_;
    private Connection          closedConnection_;
    private DatabaseMetaData    dmd_;
    private DatabaseMetaData    dmd2_;
    StringBuffer message =new StringBuffer();

    private String addedGetSchemasFix = " -- added 9/24/2009 by native driver -- requires SYSIBM fix from issue 42006";


/**
Constructor.
**/
    public JDDMDGetSchemas (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDDMDGetSchemas",
            namesAndVars, runMode, fileOutputStream,
            password);
    }




/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
        throws Exception
    {
        // SQL400 - changed to use a generic URL so that it would
        //          work with both the toolbox and the native driver.
        String url = baseURL_;

	if (getDriver()== JDTestDriver.DRIVER_JCC) {
	    connection_ = testDriver_.getConnection (url , systemObject_.getUserId(), encryptedPassword_);
	} else {
	    
	    connection_ = testDriver_.getConnection (url + ";naming=system",systemObject_.getUserId(), encryptedPassword_);
	}
        dmd_ = connection_.getMetaData ();

        closedConnection_ = testDriver_.getConnection (url, systemObject_.getUserId(), encryptedPassword_);
        dmd2_ = closedConnection_.getMetaData ();
        closedConnection_.close ();
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
        connection_.close ();
    }



/**
getSchemas() - Check the result set format.
**/
    public void Var001()
    {
      message.setLength(0);
        try {
            ResultSet rs = dmd_.getSchemas ();
            ResultSetMetaData rsmd = rs.getMetaData ();

            String[] expectedNames = { "TABLE_SCHEM" };
            int[] expectedTypes = { Types.VARCHAR };

            String[] expectedNames30 = { "TABLE_SCHEM", "TABLE_CATALOG" };
            int[] expectedTypes30 = { Types.VARCHAR, Types.VARCHAR };

            int count = rsmd.getColumnCount ();

            if (getJdbcLevel() <= 2) {
                boolean namesCheck = JDDMDTest.checkColumnNames (rsmd, expectedNames, message);
                boolean typesCheck = JDDMDTest.checkColumnTypes (rsmd, expectedTypes, message);
                rs.close ();
                assertCondition ((count == expectedNames.length) && (namesCheck) && (typesCheck), message);
            } else {
                boolean namesCheck = JDDMDTest.checkColumnNames (rsmd, expectedNames30, message);

                boolean typesCheck = JDDMDTest.checkColumnTypes (rsmd, expectedTypes30, message);

                rs.close ();
                assertCondition ((count == expectedNames30.length) && (namesCheck) && (typesCheck),
				 "\ncount="+count+" sb "+expectedNames30.length+
				 "\nnamesCheck="+namesCheck+
                                 "\ntypesCheck="+typesCheck+
                                 "\nshould have returned 2 columns for JDBC 3.0 and beyond"+
                                 "\n"+message);
            }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception "+message);
        }
    }



/**
getSchemas() - Should contain the expected contents and not
those NOT in the library list.
**/
    public void Var002()
    {
        StringBuffer sb = new StringBuffer();
        try {
            ResultSet rs = dmd_.getSchemas ();

            // It is too hard to determine a list of everything
            // that is expected to be returned, so we just make
            // sure a few that we know are returned.
            boolean foundQGPL = false;
            boolean foundQIWS = false;
            boolean foundJDDMD = false;
            int count =0;
            String QGPL="QGPL";
            String QIWS="QIWS";
            if (JDTestDriver.isLUW()) {
              QGPL="SYSCAT";
              QIWS="SYSFUN";
            }
            while (rs.next ()) {
                String schema = rs.getString ("TABLE_SCHEM");
                count++;
                if (schema.equals (QGPL))
                    foundQGPL = true;
                else if (schema.equals (QIWS))
                    foundQIWS = true;
                else if (schema.equals (JDDMDTest.COLLECTION))
                    foundJDDMD = true;
                else {
                  sb.append("-'");
                  sb.append(schema);
                  sb.append("'-");
			if ((count % 10) == 0) sb.append("\n");
                }
            }

            rs.close ();
            assertCondition (foundQGPL && foundQIWS && foundJDDMD,
                "found"+QGPL+"="+foundQGPL+" found"+QIWS+"="+foundQIWS+" foundJDDMD="+foundJDDMD+
                " count="+count+ "schemas are "+sb.toString());

        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getSchemas() - Should throw an exception when the connection
is closed.
**/
    public void Var003()
    {
        try {
            ResultSet resultSet = dmd2_.getSchemas();
            failed ("Didn't throw SQLException but got "+resultSet);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
     * getSchemas(null, null) - Should contain the expected contents
     */
    public void Var004()
    {

        StringBuffer sb = new StringBuffer();
        if ((getDriver() == JDTestDriver.DRIVER_TOOLBOX  && getRelease() >= JDTestDriver.RELEASE_V7R1M0) || checkJdbc40())
        {
            try
            {
                Class[] ca = {String.class, String.class};
                ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_O(dmd_, "getSchemas", ca, null, null);

                boolean foundQGPL = false;
                boolean foundQIWS = false;
                boolean foundJDDMD = false;
                int count = 0;
                String QGPL = "QGPL";
                String QIWS = "QIWS";
                if (JDTestDriver.isLUW())
                {
                    QGPL = "SYSCAT";
                    QIWS = "SYSFUN";
                }
                while (rs.next())
                {
                    String schema = rs.getString("TABLE_SCHEM");
                    count++;
                    if (schema.equals(QGPL))
                        foundQGPL = true;
                    else if (schema.equals(QIWS))
                        foundQIWS = true;
                    else if (schema.equals(JDDMDTest.COLLECTION))
                        foundJDDMD = true;
                    else
                    {
                        sb.append("-");
                        sb.append(schema);
                        sb.append("-");
			if ((count % 10) == 0) sb.append("\n");
                    }
                }

                rs.close();
                assertCondition(foundQGPL && foundQIWS && foundJDDMD, "found" + QGPL + "=" + foundQGPL + " found" + QIWS + "=" + foundQIWS + " foundJDDMD=" + foundJDDMD + " count=" + count + "schemas are " + sb.toString());

            } catch (Exception e)
            {
                failed(e, "Unexpected Exception");
            }
        }
    }

    /**
     * getSchemas("", null) - Should contain the expected contents
     */
    public void Var005()
    {
        if ((getDriver() == JDTestDriver.DRIVER_TOOLBOX && getRelease() >= JDTestDriver.RELEASE_V7R1M0) || checkJdbc40())
        {
            Class[] ca = {String.class, String.class};
            StringBuffer sb = new StringBuffer();
            try
            {
                ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_O(dmd_, "getSchemas", ca, "", null);

                boolean foundQGPL = false;
                boolean foundQIWS = false;
                boolean foundJDDMD = false;
                int count = 0;
                String QGPL = "QGPL";
                String QIWS = "QIWS";
                if (JDTestDriver.isLUW())
                {
                    QGPL = "SYSCAT";
                    QIWS = "SYSFUN";
                }
                while (rs.next())
                {
                    String schema = rs.getString("TABLE_SCHEM");
                    count++;
                    if (schema.equals(QGPL))
                        foundQGPL = true;
                    else if (schema.equals(QIWS))
                        foundQIWS = true;
                    else if (schema.equals(JDDMDTest.COLLECTION))
                        foundJDDMD = true;
                    else
                    {
                        sb.append("-");
                        sb.append(schema);
                        sb.append("-");
			if ((count % 10) == 0) sb.append("\n");
                    }
                }

                rs.close();
                assertCondition(foundQGPL && foundQIWS && foundJDDMD, "found" + QGPL + "=" + foundQGPL + " found" + QIWS + "=" + foundQIWS + " foundJDDMD=" + foundJDDMD + " count=" + count + "schemas are " + sb.toString());

            } catch (Exception e)
            {
                failed(e, "Unexpected Exception");
            }
        }
    }

    /**
     * getSchemas("", "Q%") - Should contain the expected contents
     */
    public void Var006()
    {
        if ((getDriver() == JDTestDriver.DRIVER_TOOLBOX && getRelease() >= JDTestDriver.RELEASE_V7R1M0 ) || checkJdbc40())
        {
            Class[] ca = {String.class, String.class};
            StringBuffer sb = new StringBuffer();
            try
            {
                ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_O(dmd_, "getSchemas", ca, "", "Q%");

                boolean foundQGPL = false;
                boolean foundQIWS = false;

                int count = 0;
                String QGPL = "QGPL";
                String QIWS = "QIWS";
                if (JDTestDriver.isLUW())
                {
                    QGPL = "SYSCAT";
                    QIWS = "SYSFUN";
                }
                while (rs.next())
                {
                    String schema = rs.getString("TABLE_SCHEM");

                    count++;

                    if (schema.equals(QGPL))
                        foundQGPL = true;
                    else if (schema.equals(QIWS))
                        foundQIWS = true;
                    else
                    {
                        sb.append("-");
                        sb.append(schema);
                        sb.append("-");
			if ((count % 10) == 0) sb.append("\n");
                    }
                }

                rs.close();
                assertCondition(foundQGPL && foundQIWS , "found" + QGPL + "=" + foundQGPL + " found" + QIWS + "=" + foundQIWS + " count=" + count + "schemas are " + sb.toString());

            } catch (Exception e)
            {
                failed(e, "Unexpected Exception");
            }
        }
    }


/**
getSchemas() - Check the result set format for the JDBC 4.0 version
**/
    public void Var007()
    {

	if (getRelease() <= JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("V5R4 needs sysibm changes");
	    return;
	}

        if (checkJdbc40()) {
          message.setLength(0);
            try {
                Class[] ca = {String.class, String.class};
                ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_O(dmd_, "getSchemas", ca, "", "Q%");

                ResultSetMetaData rsmd = rs.getMetaData ();


                String[] expectedNames = { "TABLE_SCHEM", "TABLE_CATALOG" };
                int[] expectedTypes = { Types.VARCHAR, Types.VARCHAR };

                int count = rsmd.getColumnCount ();

                boolean namesCheck = JDDMDTest.checkColumnNames (rsmd, expectedNames, message);

                boolean typesCheck = JDDMDTest.checkColumnTypes (rsmd, expectedTypes, message);

                rs.close ();
                assertCondition ((count == expectedNames.length) && (namesCheck) && (typesCheck),
                        "\ncount="+count+" sb "+expectedNames.length+
                        "\nnamesCheck="+namesCheck+
                        "\ntypesCheck="+typesCheck+
                        "\nshould have returned 2 columns for JDBC 4.0 and beyond"+
                        "\n"+message+"\n"+addedGetSchemasFix);

            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception"+addedGetSchemasFix);
            }
        }
    }


   /**
     * getSchemas(null, null) - Should contain the expected contents
     */
    public void Var008()
    {
	if (getRelease() <= JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("V5R4 needs sysibm changes");
	    return;
	}


        StringBuffer sb = new StringBuffer();
        if ((getDriver() == JDTestDriver.DRIVER_TOOLBOX  && getRelease() >= JDTestDriver.RELEASE_V7R1M0) || checkJdbc40())
        {
            try
            {
                Class[] ca = {String.class, String.class};
                ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_O(dmd_, "getSchemas", ca, null, null);

                boolean foundQGPL = false;
                boolean foundQIWS = false;
                boolean foundJDDMD = false;
                int count = 0;
                String QGPL = "QGPL";
                String QIWS = "QIWS";
                if (JDTestDriver.isLUW())
                {
                    QGPL = "SYSCAT";
                    QIWS = "SYSFUN";
                }
                while (rs.next())
                {
                    String schema = rs.getString(1);
                    count++;
                    if (schema.equals(QGPL))
                        foundQGPL = true;
                    else if (schema.equals(QIWS))
                        foundQIWS = true;
                    else if (schema.equals(JDDMDTest.COLLECTION))
                        foundJDDMD = true;
                    else
                    {
                        sb.append("-");
                        sb.append(schema);
                        sb.append("-");
			if ((count % 10) == 0) sb.append("\n");
                    }
                }

                rs.close();
                assertCondition(foundQGPL && foundQIWS && foundJDDMD, "found" + QGPL + "=" + foundQGPL + " found" + QIWS + "=" + foundQIWS + " foundJDDMD=" + foundJDDMD + " count=" + count + "schemas are " + sb.toString()+"\n"+addedGetSchemasFix);

            } catch (Exception e)
            {
                failed(e, "Unexpected Exception"+addedGetSchemasFix);
            }
        }
    }

    /**
     * getSchemas("", null) - Should contain the expected contents
     */
    public void Var009()
    {
	if (getRelease() <= JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("V5R4 needs sysibm changes");
	    return;
	}

        if ((getDriver() == JDTestDriver.DRIVER_TOOLBOX && getRelease() >= JDTestDriver.RELEASE_V7R1M0) || checkJdbc40())
        {
            Class[] ca = {String.class, String.class};
            StringBuffer sb = new StringBuffer();
            try
            {
                ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_O(dmd_, "getSchemas", ca, "", null);

                boolean foundQGPL = false;
                boolean foundQIWS = false;
                boolean foundJDDMD = false;
                int count = 0;
                String QGPL = "QGPL";
                String QIWS = "QIWS";
                if (JDTestDriver.isLUW())
                {
                    QGPL = "SYSCAT";
                    QIWS = "SYSFUN";
                }
                while (rs.next())
                {
                    String schema = rs.getString(1);
                    count++;
                    if (schema.equals(QGPL))
                        foundQGPL = true;
                    else if (schema.equals(QIWS))
                        foundQIWS = true;
                    else if (schema.equals(JDDMDTest.COLLECTION))
                        foundJDDMD = true;
                    else
                    {
                        sb.append("-");
                        sb.append(schema);
                        sb.append("-");
			if ((count % 10) == 0) sb.append("\n");
                    }
                }

                rs.close();
                assertCondition(foundQGPL && foundQIWS && foundJDDMD, "found" + QGPL + "=" + foundQGPL + " found" + QIWS + "=" + foundQIWS + " foundJDDMD=" + foundJDDMD + " count=" + count + "schemas are " + sb.toString()+"\n"+addedGetSchemasFix);

            } catch (Exception e)
            {
                failed(e, "Unexpected Exception"+addedGetSchemasFix);
            }
        }
    }

    /**
     * getSchemas("", "Q%") - Should contain the expected contents
     */
    public void Var010()
    {

	if (getRelease() <= JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("V5R4 needs sysibm changes");
	    return;
	}


        if ((getDriver() == JDTestDriver.DRIVER_TOOLBOX && getRelease() >= JDTestDriver.RELEASE_V7R1M0 ) || checkJdbc40())
        {
            Class[] ca = {String.class, String.class};
            StringBuffer sb = new StringBuffer();
            try
            {
                ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_O(dmd_, "getSchemas", ca, "", "Q%");

                boolean foundQGPL = false;
                boolean foundQIWS = false;

                int count = 0;
                String QGPL = "QGPL";
                String QIWS = "QIWS";
                if (JDTestDriver.isLUW())
                {
                    QGPL = "SYSCAT";
                    QIWS = "SYSFUN";
                }
                while (rs.next())
                {
                    String schema = rs.getString(1);

                    count++;

                    if (schema.equals(QGPL))
                        foundQGPL = true;
                    else if (schema.equals(QIWS))
                        foundQIWS = true;
                    else
                    {
                        sb.append("-");
                        sb.append(schema);
                        sb.append("-");
			if ((count % 10) == 0) sb.append("\n");
                    }
                }

                rs.close();
                assertCondition(foundQGPL && foundQIWS , "found" + QGPL + "=" + foundQGPL + " found" + QIWS + "=" + foundQIWS + " count=" + count + "schemas are " + sb.toString()+"\n"+addedGetSchemasFix);

            } catch (Exception e)
            {
                failed(e, "Unexpected Exception"+addedGetSchemasFix);
            }
        }
    }


/**
getSchemas() - Run getSchemas multiple times.  Make sure there is not a handle leak.
Created 1/31/2011 for CPS 8DHTTE.

**/
    public void Var011()
    {
	String added = " -- added by 1/31/2011 to test for native statement leak in metadata";
        if (checkNative()) {
            try {

		System.gc();
		Statement fill[] = new Statement[20];
		for (int i = 0; i < fill.length; i++) {
		    fill[i] = connection_.createStatement();
		}

		Statement stmt = connection_.createStatement();
		//
		// Change the default wait on the connection to allow getSchemas to run faster
		//
		// Run for a maximum of 10 minutes
		//
		long startTime = System.currentTimeMillis();
		long endTime = System.currentTimeMillis() + 600000;
		try {
		    // The connection is usually using system naming
		    stmt.execute("CALL QSYS/QCMDEXC('Chgjob  DFTWAIT(1)       ',000000020.00000)");
		} catch (Exception e) {
		    try {
			// Try user naming
			stmt.execute("CALL QSYS.QCMDEXC('Chgjob  DFTWAIT(1)       ',000000020.00000)");
		    } catch (Exception e2) {
			System.out.println("Warning, could not change job ");
			e2.printStackTrace();
		    }
		}
		for (int i = 0; i < 1000 && (System.currentTimeMillis() < endTime); i++) {
		    // System.out.println("Calling getSchemas");
		    ResultSet rs = dmd_.getSchemas ();

		    rs.close();
		}
		System.out.println("Loop of 1000 took "+(System.currentTimeMillis() - startTime)+" ms");
		Statement stmt2 = connection_.createStatement();
		int beginningHandle = JDReflectionUtil.callMethod_I(stmt,"getStatementHandle");
		int endingHandle = JDReflectionUtil.callMethod_I(stmt2,"getStatementHandle");


		for (int i = 0; i < fill.length; i++) {
		    fill[i].close();
		}

                assertCondition((endingHandle - beginningHandle) < 10 ,
				" endingHandle = "+endingHandle+
				" beginningHandle = "+beginningHandle+
				" endingHandle = "+endingHandle+
				added);
            } catch (Exception e) {
                failed (e, "Unexpected Exception"+added);
            }
        }
    }


  public void Var012() {
	  checkRSMD(false);
  }
  public void Var013() {
      if (checkNotGroupTest()) { 
	  checkRSMD(true);
      }
  }

    public void checkRSMD(boolean extendedMetadata)
    {

	Connection connection = connection_;
	DatabaseMetaData dmd = dmd_;
	int TESTSIZE=10;
	StringBuffer prime = new StringBuffer();
	int j=0;
	int col=0;
	String added=" -- Reworked 03/23/2012";
	boolean passed=true;
	message.setLength(0);

	/* primed from 546CN */
	String [][] methodTests = {
	    {"isAutoIncrement","1","false"},
	    {"isCaseSensitive","1","true"},
	    {"isSearchable","1","true"},
	    {"isCurrency","1","false"},
	    {"isNullable","1","0"},
	    {"isSigned","1","false"},
	    {"getColumnDisplaySize","1","128"},
	    {"getColumnLabel","1","TABLE_SCHEM"},
	    {"getColumnName","1","TABLE_SCHEM"},
	    {"getPrecision","1","128"},
	    {"getScale","1","0"},
	    {"getCatalogName","1","LOCALHOST"},
	    {"getColumnType","1","12"},
	    {"getColumnTypeName","1","VARCHAR"},
	    {"isReadOnly","1","true"},
	    {"isWritable","1","false"},
	    {"isDefinitelyWritable","1","false"},
	    {"getColumnClassName","1","java.lang.String"},
	    {"isAutoIncrement","2","false"},
	    {"isCaseSensitive","2","true"},
	    {"isSearchable","2","true"},
	    {"isCurrency","2","false"},
	    {"isNullable","2","0"},
	    {"isSigned","2","false"},
	    {"getColumnDisplaySize","2","18"},
	    {"getColumnLabel","2","TABLE_CATALOG"},
	    {"getColumnName","2","TABLE_CATALOG"},
	    {"getPrecision","2","18"},
	    {"getScale","2","0"},
	    {"getCatalogName","2","LOCALHOST"},
	    {"getColumnType","2","12"},
	    {"getColumnTypeName","2","VARCHAR"},
	    {"isReadOnly","2","true"},
	    {"isWritable","2","false"},
	    {"isDefinitelyWritable","2","false"},
	    {"getColumnClassName","2","java.lang.String"},

	};




	String[][] fixup544T = {
	    {"isNullable","2","1"},
	    {"getColumnDisplaySize","2","128"},
	    {"getPrecision","2","128"},
	};

	String[][] fixup545NX = {
	    {"getColumnDisplaySize","2","128"},
	    {"getPrecision","2","128"},
	};

	String [][] fixup = {};

	String [][] fixupExtended715T = {
	    {"isNullable","2","0"},
	    {"getColumnDisplaySize","2","18"},
	    {"getPrecision","2","18"},
	};


	String [][] fixupExtended737T = {
	    {"isNullable","2","0"},
	    {"getColumnDisplaySize","2","18"},
	    {"getPrecision","2","18"},
	    {"getColumnLabel","1","Table Schem"},
	};

	String [][] fixupExtended545N = {
	    {"isSearchable","2","false"},
	    {"getColumnDisplaySize","2","128"},
	    {"getPrecision","2","128"},
	};
	String [][] fixupExtended546N = {
	    {"isSearchable","1","false"},
	    {"isSearchable","2","false"},
	};



	String [][] fixupExtended726N = {
	    {"isSearchable","1","false"},
	    {"isSearchable","2","false"},
/* April 2019 */
	    { "getColumnLabel","1","Table               Schem"},
	};

	Object[][] fixupArrayExtended = {
	    {"543T", fixup544T},
	    {"544T", fixup544T},
	    {"545T", fixup544T},
	    {"546T", fixup544T},

	    { "614T", fixup544T},
	    { "615T", fixup544T},
	    { "616T", fixup544T},
	    { "617T", fixup544T},
	    { "618T", fixup544T},

	    { "714T", fixupExtended715T},
	    { "715T", fixupExtended715T},
	    { "716T", fixupExtended715T},
	    { "717T", fixupExtended715T},
	    { "718T", fixupExtended715T},
	    { "719T", fixupExtended715T},

	    { "726T", fixupExtended715T},
	    { "727T", fixupExtended715T},
	    { "728T", fixupExtended715T},
	    { "729T", fixupExtended715T},


	   
	    { "737T", fixupExtended737T}, 
	    { "738T", fixupExtended737T}, 
	    { "739T", fixupExtended737T},
 
	    {"544N", fixupExtended545N},
	    {"545N", fixupExtended545N},
	    {"546N", fixupExtended546N},
	    {"614N", fixupExtended546N},
	    {"615N", fixupExtended546N},
	    {"616N", fixupExtended546N},

	    {"714N", fixupExtended546N},
	    {"715N", fixupExtended546N},
	    {"716N", fixupExtended546N},
	    {"717N", fixupExtended546N},
	    {"718N", fixupExtended546N},
	    {"719N", fixupExtended546N},

	    {"725N", fixupExtended546N},
	    {"726N", fixupExtended546N},
	    {"727N", fixupExtended726N},
	    {"728N", fixupExtended726N},
	    {"729N", fixupExtended726N},

	};




	Object[][] fixupArray = {
	    {"543TX", fixup544T},
	    {"544TX", fixup544T},
	    {"545TX", fixup544T},
	    {"546TX", fixup544T},

	    { "614TX", fixup544T},
	    { "615TX", fixup544T},
	    { "616TX", fixup544T},
	    { "617TX", fixup544T},
	    { "618TX", fixup544T},
	    { "619TX", fixup544T},

	    { "714TX", fixup544T},
	    { "715TX", fixup544T},
	    { "716TX", fixup544T},
	    { "717TX", fixup544T},
	    { "718TX", fixup544T},
	    { "719TX", fixup544T},

	    { "724TX", fixup544T},
	    { "725TX", fixup544T},
	    { "726TX", fixup544T},
	    { "727TX", fixup544T},
	    { "728TX", fixup544T},
	    { "729TX", fixup544T},

	    { "544NX", fixup545NX},
	    { "545NX", fixup545NX},

	};



	if (checkJdbc30()) {
	    try {

		if (extendedMetadata) {
		    String url = baseURL_
		      + ";extended metadata=true";
		    if (getDriver() == JDTestDriver.DRIVER_JCC) {
			url = baseURL_;
		    }
		    connection = testDriver_.getConnection (url,
							     userId_, encryptedPassword_);
		    dmd= connection.getMetaData ();

		    fixup = getFixup(fixupArrayExtended, null, "fixupArrayExtended", message);



		} else {
		    String extra="X";
		    if (isSysibmMetadata()) {
		      extra = "S";
		    }

		    fixup = getFixup(fixupArray, extra, "fixupArray", message);


		}

		if (fixup != null) {

		    for (j = 0; j < fixup.length; j++) {
			boolean found = false;
			for (int k = 0; !found &&  k < methodTests.length; k++) {
			    if (fixup[j][0].equals(methodTests[k][0]) &&
				fixup[j][1].equals(methodTests[k][1])) {
				methodTests[k] = fixup[j];
				found = true;
			    }
			}
		    }
		}



		String catalog1 = connection.getCatalog();
		if (catalog1 == null) {
		    catalog1 = system_.toUpperCase();
		}

		ResultSet[] rsA =  new ResultSet[TESTSIZE];
		ResultSetMetaData[] rsmdA = new ResultSetMetaData[TESTSIZE];

		for (j = 0; j < TESTSIZE; j++) {
		    rsA[j] = dmd.getSchemas ();

		    rsmdA[j] = rsA[j].getMetaData ();

		    ResultSetMetaData rsmd = rsmdA[j];
		    passed = verifyRsmd(rsmd, methodTests, catalog1, j, message, prime) && passed;

		} /* for j */
		assertCondition(passed, message.toString()+added+"\nPRIME=\n"+prime.toString());

	    } catch (Exception e) {
		failed (e, "Unexpected Error on loop "+j+" col = "+col+" "+added);
	    } catch (Error e ) {
		failed (e, "Unexpected Error on loop "+j+" col = "+col+" "+added);
	    }
	}
    }



    /**
     * getSchemas(null, null) - Readonly connection 
     */
    public void Var014()
    {

        StringBuffer sb = new StringBuffer();
        if ((getDriver() == JDTestDriver.DRIVER_TOOLBOX  && getRelease() >= JDTestDriver.RELEASE_V7R1M0) || checkJdbc40())
        {
            try
            {

		Connection c = testDriver_.getConnection (baseURL_
							  + ";access=read only", userId_, encryptedPassword_);
		DatabaseMetaData dmd = c.getMetaData(); 

                Class[] ca = {String.class, String.class};
                ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_O(dmd, "getSchemas", ca, null, null);

                boolean foundQGPL = false;
                boolean foundQIWS = false;
                boolean foundJDDMD = false;
                int count = 0;
                String QGPL = "QGPL";
                String QIWS = "QIWS";
                if (JDTestDriver.isLUW())
                {
                    QGPL = "SYSCAT";
                    QIWS = "SYSFUN";
                }
                while (rs.next())
                {
                    String schema = rs.getString("TABLE_SCHEM");
                    count++;
                    if (schema.equals(QGPL))
                        foundQGPL = true;
                    else if (schema.equals(QIWS))
                        foundQIWS = true;
                    else if (schema.equals(JDDMDTest.COLLECTION))
                        foundJDDMD = true;
                    else
                    {
                        sb.append("-");
                        sb.append(schema);
                        sb.append("-");
			if ((count % 10) == 0) sb.append("\n");
                    }
                }

                rs.close();
                assertCondition(foundQGPL && foundQIWS && foundJDDMD, "found" + QGPL + "=" + foundQGPL + " found" + QIWS + "=" + foundQIWS + " foundJDDMD=" + foundJDDMD + " count=" + count + "schemas are " + sb.toString());

            } catch (Exception e)
            {
                failed(e, "Unexpected Exception");
            }
        }
    }


}








