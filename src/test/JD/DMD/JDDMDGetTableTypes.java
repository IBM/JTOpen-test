///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDMDGetTableTypes.java
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
 // File Name:    JDDMDGetTableTypes.java
 //
 // Classes:      JDDMDGetTableTypes
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

import java.sql.Statement;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import java.sql.Types;
import java.util.Hashtable;



/**
Testcase JDDMDGetTableTypes.  This tests the following methods
of the JDBC DatabaseMetaData class:

<ul>
<li>getTableTypes()
</ul>
**/
public class JDDMDGetTableTypes extends JDTestcase {


    // Private data.
    private Connection          connection_;
    private Connection          closedConnection_;
    private DatabaseMetaData    dmd_;
    private DatabaseMetaData    dmd2_;
    private StringBuffer message= new StringBuffer();



/**
Constructor.
**/
    public JDDMDGetTableTypes (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDDMDGetTableTypes",
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

	    String url = baseURL_
	      
	      ;
	    connection_ = testDriver_.getConnection (url + ";naming=system",systemObject_.getUserId(), encryptedPassword_);
	    dmd_ = connection_.getMetaData ();

	    closedConnection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
	    dmd2_ = closedConnection_.getMetaData ();
	    closedConnection_.close ();
	}
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
     * check that the expected value was found.  If not, add the
     * failing case to the message
     */
    boolean checkExpected(String actual, String expected) {
	if (actual.equals(expected)) {
	    return true;
	} else {
	    message.append("actual("+actual+") != expected("+expected+")\n");
	    return false;
	}
    }


/**
getTableTypes() - Check the result set format.
**/
    public void Var001()
    {
        try {
          message.setLength(0);

            ResultSet rs = dmd_.getTableTypes ();
            ResultSetMetaData rsmd = rs.getMetaData ();

            String[] expectedNames = { "TABLE_TYPE" };
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
getTableTypes() - Should contain the expected contents.

SQL400 - The order of the values between the 2 drivers is different here.
**/
    public void Var002()
    {
      message.setLength(0);
        try {
            ResultSet rs = dmd_.getTableTypes ();
            boolean success = true;

            if (getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata())
            {
                rs.next ();
                success = success && rs.getString ("TABLE_TYPE").equals ("TABLE");

                rs.next ();
                success = success && rs.getString ("TABLE_TYPE").equals ("VIEW");

                rs.next ();
                success = success && rs.getString ("TABLE_TYPE").equals ("SYSTEM TABLE");

                if(getRelease() >= JDTestDriver.RELEASE_V5R3M0)         //@C1A
                {
                    rs.next ();
                    success = success && rs.getString ("TABLE_TYPE").equals ("ALIAS");  // added in JTOpen 4.9
                    rs.next ();
                    success = success && rs.getString ("TABLE_TYPE").equals ("MATERIALIZED QUERY TABLE");
                }
            }
            else
            {
               rs.next ();
               if(getRelease() >= JDTestDriver.RELEASE_V5R2M0)
               {
                   success = checkExpected(rs.getString ("TABLE_TYPE"),"ALIAS") && success ;
               }
               if (JDTestDriver.isLUW()) {
                   rs.next ();
                   success = checkExpected(rs.getString ("TABLE_TYPE"),"HIERARCHY TABLE") && success ;
                   rs.next ();
                   success = checkExpected(rs.getString ("TABLE_TYPE"),"INOPERATIVE VIEW") && success ;
               }

               if(getRelease() >= JDTestDriver.RELEASE_V5R3M0)
               {
                   rs.next ();
                   success = checkExpected(rs.getString ("TABLE_TYPE"),"MATERIALIZED QUERY TABLE") && success ;
               }

               if (JDTestDriver.isLUW()) {
                   rs.next ();
                   success = checkExpected(rs.getString ("TABLE_TYPE"),"NICKNAME") && success ;
               }


               rs.next ();
               success = checkExpected(rs.getString ("TABLE_TYPE"),"SYSTEM TABLE") && success ;

               rs.next ();
               success = checkExpected(rs.getString ("TABLE_TYPE"),"TABLE") && success ;

               if (JDTestDriver.isLUW()) {
                   rs.next ();
                   success = checkExpected(rs.getString ("TABLE_TYPE"),"TYPED TABLE") && success ;
                   rs.next ();
                   success = checkExpected(rs.getString ("TABLE_TYPE"),"TYPED VIEW") && success ;
               }


               rs.next ();
               success = checkExpected(rs.getString ("TABLE_TYPE"),"VIEW") && success;
            }

            if ( rs.next() == false) {
                /* success = success ; */
            } else {
                String anotherRow = rs.getString ("TABLE_TYPE")+" ";
                while (rs.next()) {
                    anotherRow+=rs.getString ("TABLE_TYPE")+" ";
                }
                message.append("Additional row found with "+anotherRow);
                success = false;
            }
            assertCondition (success, message);

        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTableTypes() - Should throw an exception when the connection
is closed.
**/
    public void Var003()
    {
        try {
            ResultSet resultSet = dmd2_.getTableTypes();
            failed ("Didn't throw SQLException but got "+resultSet);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }

/**
getTableTypes() - Run getTableTypes multiple times.  Make sure there is not a handle leak.
Created 1/31/2011 for CPS 8DHTTE.

**/
    public void Var004()   // @B1A
    {
	String added = " -- added by 1/31/2011 to test for native statement leak in metadata";
        if (checkNative()) {
            try {

		Statement stmt = connection_.createStatement();

		for (int i = 0; i < 1000; i++) {
		    // System.out.println("Calling getTableTypes");
		    ResultSet rs = dmd_.getTableTypes ();
		    rs.close();
		}

		Statement stmt2 = connection_.createStatement();
		int beginningHandle = JDReflectionUtil.callMethod_I(stmt,"getStatementHandle");
		int endingHandle = JDReflectionUtil.callMethod_I(stmt2,"getStatementHandle");


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

	/* primed using 546N */ 
	String [][] methodTests = {

	    {"isAutoIncrement","1","false"},
	    {"isCaseSensitive","1","true"},
	    {"isSearchable","1","true"},
	    {"isCurrency","1","false"},
	    {"isNullable","1","0"},
	    {"isSigned","1","false"},
	    {"getColumnDisplaySize","1","128"},
	    {"getColumnLabel","1","TABLE_TYPE"},
	    {"getColumnName","1","TABLE_TYPE"},
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







	String [][] fixup = {};

	String[][] fixup545NX = {
	    {"isNullable","1","1"},
	};
	String [][] fixupExtended545N = {
	    {"isSearchable","1","false"},
	    {"isNullable","1","1"},
	};

	String [][] fixupExtended5440 = {
	    {"isSearchable","1","false"},
	};


	String [][] fixupExtended7340 = {
	    {"isSearchable","1","false"},
	    {"getColumnLabel","1","Table               Type"},
	};


	String [][] fixupExtended7363T = {
	    {"getColumnLabel","1","Table Type"},
	};


	Object[][] fixupArrayExtended = {
	    { "544N", fixupExtended545N},
	    { "545N", fixupExtended545N},
	    { "546N", fixupExtended5440},
	    { "614N", fixupExtended5440},
	    { "615N", fixupExtended5440},
	    { "616N", fixupExtended5440},

	    { "714N", fixupExtended5440},
	    { "715N", fixupExtended5440},
	    { "716N", fixupExtended5440},
	    { "717N", fixupExtended5440},
	    { "718N", fixupExtended5440},
	    { "719N", fixupExtended5440},

	    { "725N", fixupExtended5440},
	    { "726N", fixupExtended5440},
	    { "727N", fixupExtended5440},


	    { "736N", fixupExtended7340},
	    { "737N", fixupExtended7340},
	    { "738N", fixupExtended7340},
	    { "739N", fixupExtended7340},

	    { "736T", fixupExtended7363T },
	    { "737T", fixupExtended7363T },
	    { "738T", fixupExtended7363T },
	    { "739T", fixupExtended7363T }, 

	};




	Object[][] fixupArray = {
	    {"544NX", fixup545NX},
	    {"545NX", fixup545NX}, 
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
		    rsA[j] = dmd.getTableTypes ();

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
getTableTypes() - check readonly connection 

SQL400 - The order of the values between the 2 drivers is different here.
**/
    public void Var007()
    {
      message.setLength(0);
        try {
	    Connection c = testDriver_.getConnection (baseURL_
					       + ";access=read only", userId_, encryptedPassword_);
	    DatabaseMetaData dmd = c.getMetaData(); 

            ResultSet rs = dmd.getTableTypes ();
            boolean success = true;

            if (getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata())
            {
                rs.next ();
                success = success && rs.getString ("TABLE_TYPE").equals ("TABLE");

                rs.next ();
                success = success && rs.getString ("TABLE_TYPE").equals ("VIEW");

                rs.next ();
                success = success && rs.getString ("TABLE_TYPE").equals ("SYSTEM TABLE");

                if(getRelease() >= JDTestDriver.RELEASE_V5R3M0)         //@C1A
                {
                    rs.next ();
                    success = success && rs.getString ("TABLE_TYPE").equals ("ALIAS");  // added in JTOpen 4.9
                    rs.next ();
                    success = success && rs.getString ("TABLE_TYPE").equals ("MATERIALIZED QUERY TABLE");
                }
            }
            else
            {
               rs.next ();
               if(getRelease() >= JDTestDriver.RELEASE_V5R2M0)
               {
                   success = checkExpected(rs.getString ("TABLE_TYPE"),"ALIAS") && success ;
               }
               if (JDTestDriver.isLUW()) {
                   rs.next ();
                   success = checkExpected(rs.getString ("TABLE_TYPE"),"HIERARCHY TABLE") && success ;
                   rs.next ();
                   success = checkExpected(rs.getString ("TABLE_TYPE"),"INOPERATIVE VIEW") && success ;
               }

               if(getRelease() >= JDTestDriver.RELEASE_V5R3M0)
               {
                   rs.next ();
                   success = checkExpected(rs.getString ("TABLE_TYPE"),"MATERIALIZED QUERY TABLE") && success ;
               }

               if (JDTestDriver.isLUW()) {
                   rs.next ();
                   success = checkExpected(rs.getString ("TABLE_TYPE"),"NICKNAME") && success ;
               }


               rs.next ();
               success = checkExpected(rs.getString ("TABLE_TYPE"),"SYSTEM TABLE") && success ;

               rs.next ();
               success = checkExpected(rs.getString ("TABLE_TYPE"),"TABLE") && success ;

               if (JDTestDriver.isLUW()) {
                   rs.next ();
                   success = checkExpected(rs.getString ("TABLE_TYPE"),"TYPED TABLE") && success ;
                   rs.next ();
                   success = checkExpected(rs.getString ("TABLE_TYPE"),"TYPED VIEW") && success ;
               }


               rs.next ();
               success = checkExpected(rs.getString ("TABLE_TYPE"),"VIEW") && success;
            }

            if ( rs.next() == false) {
                /* success = success ; */
            } else {
                String anotherRow = rs.getString ("TABLE_TYPE")+" ";
                while (rs.next()) {
                    anotherRow+=rs.getString ("TABLE_TYPE")+" ";
                }
                message.append("Additional row found with "+anotherRow);
                success = false;
            }
            assertCondition (success, message);

        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



}









