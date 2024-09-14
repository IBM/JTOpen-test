///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDMDGetCatalogs.java
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
 // File Name:    JDDMDGetCatalogs.java
 //
 // Classes:      JDDMDGetCatalogs
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
Testcase JDDMDGetCatalogs.  This tests the following methods
of the JDBC DatabaseMetaData class:

<ul>
<li>getCatalogs()
</ul>
**/
public class JDDMDGetCatalogs
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDDMDGetCatalogs";
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
    StringBuffer message = new StringBuffer();



/**
Constructor.
**/
    public JDDMDGetCatalogs (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDDMDGetCatalogs",
            namesAndVars, runMode, fileOutputStream,
            password);
    }




/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup() throws Exception {
    // SQL400 - changed to use a generic URL so that it would
    // work with both the toolbox and the native driver.
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
        System.out.println("baseURL="+baseURL_);
        String url = baseURL_;
        connection_ = testDriver_.getConnection(url, systemObject_.getUserId(),
          encryptedPassword_);
        dmd_ = connection_.getMetaData();

        closedConnection_ = testDriver_.getConnection(url, systemObject_
          .getUserId(), encryptedPassword_);
        dmd2_ = closedConnection_.getMetaData();
        closedConnection_.close();
    } else {
      String url = baseURL_;
      connection_ = testDriver_.getConnection (url + ";naming=system",systemObject_.getUserId(), encryptedPassword_);
      dmd_ = connection_.getMetaData();

      closedConnection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
      dmd2_ = closedConnection_.getMetaData();
      closedConnection_.close();
    }
  }



/**
 * Performs cleanup needed after running variations.
 *
 * @exception Exception
 *              If an exception occurs.
 */
    protected void cleanup ()
        throws Exception
    {
        connection_.close ();
    }



/**
getCatalogs() - Check the result set format.
**/
    public void Var001()
    {
        try {
          message.setLength(0);
            ResultSet rs = dmd_.getCatalogs ();
            ResultSetMetaData rsmd = rs.getMetaData ();

            String[] expectedNames = { "TABLE_CAT" };
            int[] expectedTypes = { Types.VARCHAR };

            int count = rsmd.getColumnCount ();
            boolean namesCheck = JDDMDTest.checkColumnNames (rsmd, expectedNames, message);
            boolean typesCheck = JDDMDTest.checkColumnTypes (rsmd, expectedTypes, message);

            rs.close ();
            assertCondition ((count == 1) && (namesCheck) && (typesCheck), message);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getCatalogs() - Should contain at least the connection's catalog.
For the native driver, there may be more if the system has more
than one RDB entry.  The toolbox driver will always contain exactly
one entry.
**/
    public void Var002()
    {
      message.setLength(0);
        try {
            ResultSet rs = dmd_.getCatalogs ();
            boolean found = false;
            int count = 0;
            String expected=connection_.getCatalog ();
            if (expected == null ) {
               expected = system_.toUpperCase();
            }
            while (rs.next ()) {
                ++count;
                String tableCat =rs.getString ("TABLE_CAT");
                message.append("FOUND: "+tableCat+"\n");
                if (tableCat.equals (expected))
                    found = true;
            }

            //
            // Note:  Toolbox will found more than one if running to a System with IASPs.
            //
            if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
                assertCondition ((found) && (count >= 1),"found="+found+" count="+count+" looking for "+expected+"\n message="+message);
            else
                assertCondition ((found) && (count >= 1), "found="+found+" count="+count+" looking for "+expected+"\n message="+message);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getCatalogs() - Should throw an exception when the connection
is closed.
**/
    public void Var003()
    {
        try {
            ResultSet resultSet = dmd2_.getCatalogs ();
            failed ("Didn't throw SQLException for "+resultSet);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }


/**
getCatalogs() - Run getCatalogs multiple times.  Make sure there is not a handle leak.
Created 1/31/2011 for CPS 8DHTTE.

**/
    public void Var004()   // @B1A
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

		for (int i = 0; i < 1000; i++) {
		    // System.out.println("Calling getCatalogs");
		    ResultSet rs = dmd_.getCatalogs ();
		    rs.close();
		}

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

  public void Var005() {
	  checkRSMD(false);
  }
  public void Var006() {
	  checkRSMD(true);
  }

    public void checkRSMD(boolean extendedMetadata)
    {

	Connection connection = connection_;
	DatabaseMetaData dmd = dmd_;
	int TESTSIZE=40;
	StringBuffer prime = new StringBuffer();
	int j=0;
	int col=0;
	String added=" -- Reworked 03/23/2012";
	boolean passed=true;
	message.setLength(0);

	String [][] methodTests = {
	    /* Primed using 546CN */
      {"isAutoIncrement","1","false"},
      {"isCaseSensitive","1","true"},
      {"isSearchable","1","true"},
      {"isCurrency","1","false"},
      {"isNullable","1","0"},
      {"isSigned","1","false"},
      {"getColumnDisplaySize","1","128"},
      {"getColumnLabel","1","TABLE_CAT"},
      {"getColumnName","1","TABLE_CAT"},
      {"getPrecision","1","128"},
      {"getScale","1","0"},
      {"getCatalogName","1","LOCALHOST"},
      {"getColumnType","1","12"},
      {"getColumnTypeName","1","VARCHAR"},
      {"isReadOnly","1","true"},
      {"isWritable","1","false"},
      {"isDefinitelyWritable","1","false"},
      {"getColumnClassName","1","java.lang.String"},

	};

	String [][] fixup614NS = {
	    {"getColumnDisplaySize","1","18"},
	    {"getPrecision","1","18"},
	};

	String [][] fixupExtended715T = {
	    {"getColumnDisplaySize","1","18"},
	    {"getPrecision","1","18"},
	};

	String [][] fixupExtended614N = {
	    {"isSearchable","1","false"},
	    {"getColumnDisplaySize","1","18"},
	    {"getPrecision","1","18"}
	};


	String[][] fixupExtended = {
	    {"isSearchable","1","false"},
	} ;


	String[][] fixupExtended726N = {
	    {"isSearchable","1","false"},
	    {"getColumnDisplaySize","1","18"},
	    {"getPrecision","1","18"},
	} ;


	String [][] fixup = {};

	Object[][] fixupArrayExtended = {
	    {"714T", fixupExtended715T},
	    {"715T", fixupExtended715T},
	    {"716T", fixupExtended715T},
	    {"717T", fixupExtended715T},
	    {"718T", fixupExtended715T},
	    {"719T", fixupExtended715T},

	    {"544N", fixupExtended},
	    {"545N", fixupExtended},
	    {"546N", fixupExtended},
	    {"614N", fixupExtended614N},
	    {"615N", fixupExtended614N},
	    {"616N", fixupExtended614N},
	    {"714N", fixupExtended614N},
/* 
	    {"715N", fixupExtended614N, "08/09/2012 guess from 717N"},
	    {"716N", fixupExtended614N, "08/09/2012 guess from 717N"},
*/
	    {"717N", fixupExtended614N, "08/09/2012 PRIMED"},
	    {"718N", fixupExtended614N, "10/31/2014 Guess from 717N"},
	    {"719N", fixupExtended614N, "10/31/2014 Guess from 717N"}, 

	    {"726N", fixupExtended726N},

	    { "716L", fixup614NS},

	};




	Object[][] fixupArray = {

	    { "714TS", fixup614NS},
	    { "715TS", fixup614NS},
	    { "716TS", fixup614NS},
	    { "717TS", fixup614NS},
	    { "718TS", fixup614NS},
	    { "719TS", fixup614NS},

	    { "614NS", fixup614NS},
	    { "615NS", fixup614NS},
	    { "616NS", fixup614NS},
/* 
	    { "714NS", fixup614NS},
	    { "715NS", fixup614NS, "08/09/2012 -- guess from 717NS "},
	    { "716NS", fixup614NS, "08/09/2012 -- guess from 717NS "},
*/ 
	    { "717NS", fixup614NS, "08/09/2012 -- primed " },
	    { "718NS", fixup614NS, "10/31/2014 -- guess from 717NS " },
	    { "719NS", fixup614NS, "10/31/2014 -- guess from 717NS " },


	    { "716LS", fixup614NS},
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
		    rsA[j] = dmd.getCatalogs();


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
getCatalogs() - Test using readonly connection.
**/
    public void Var007()
    {
      message.setLength(0);
        try {
	    Connection c = testDriver_.getConnection (baseURL_
					       + ";access=read only", userId_, encryptedPassword_);
	    DatabaseMetaData dmd = c.getMetaData(); 

            ResultSet rs = dmd.getCatalogs ();
            boolean found = false;
            int count = 0;
            String expected=connection_.getCatalog ();
            if (expected == null ) {
               expected = system_.toUpperCase();
            }
            while (rs.next ()) {
                ++count;
                String tableCat =rs.getString ("TABLE_CAT");
                message.append("FOUND: "+tableCat+"\n");
                if (tableCat.equals (expected))
                    found = true;
            }

	    c.close(); 
                assertCondition ((found) && (count >= 1),"found="+found+" count="+count+" looking for "+expected+"\n message="+message);
         }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }


}



