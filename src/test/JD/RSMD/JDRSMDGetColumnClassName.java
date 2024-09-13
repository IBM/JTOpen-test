///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSMDGetColumnClassName.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////
//
//
//
//
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDRSMDGetColumnClassName.java
//
// Classes:      JDRSMDGetColumnClassName
//
////////////////////////////////////////////////////////////////////////

package test.JD.RSMD;

import com.ibm.as400.access.AS400;

import test.JDRSMDTest;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDRSMDGetColumnClassName.  This tests the following method
of the JDBC ResultSetMetaData class:

<ul>
<li>getColumnClassName()
</ul>
**/
//
// Implementation note:
//
// * We don't bother to test with package caching here, that will
//   be sufficiently tested (for types) in JDRSMDGetColumnType.
//
public class JDRSMDGetColumnClassName
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSMDGetColumnClassName";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSMDTest.main(newArgs); 
   }



   // Private data.
   private ResultSet           rs_             = null;
   private ResultSetMetaData   rsmd_           = null;
   private Statement           statement_      = null;
   private ResultSet           rs2_            = null;
   private String              rsmd2Info_      = null; 
   private ResultSetMetaData   rsmd2_          = null;
   private Statement           statement2_     = null;

   private boolean isJDK14; 

/**
Constructor.
**/
   public JDRSMDGetColumnClassName (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
   {
      super (systemObject, "JDRSMDGetColumnClassName",
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


	// 
	// look for jdk1.4
	//
		isJDK14 = true; 





      // SQL400 - driver neutral...
      String url = baseURL_
                   // String url = "jdbc:as400://" + systemObject_.getSystemName()
                   
                   ;
      connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
      statement_ = connection_.createStatement ();
      rs_ = statement_.executeQuery ("SELECT * FROM "
                                     + JDRSMDTest.RSMDTEST_GET);
      rsmd_ = rs_.getMetaData ();


      statement2_ = connection_.createStatement ();

      rs2_ = statement2_.executeQuery ("SELECT * FROM "
                                     + JDRSMDTest.RSMDTEST_GET2);

      rsmd2_ = rs2_.getMetaData ();

      rsmd2Info_ = "SELECT * FROM " + JDRSMDTest.RSMDTEST_GET2; 


   }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
   protected void cleanup ()
   throws Exception
   {
      rs_.close ();
      rs2_.close (); 
      statement_.close ();
      statement2_.close (); 
      connection_.close ();
   }



/**
getColumnClassName() - Check column -1.  Should throw an exception.
**/
   public void Var001()
   {
      if (checkJdbc20 ()) {
         try {
            rsmd_.getColumnClassName (-1);
            failed ("Didn't throw SQLException");
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getColumnClassName() - Check column 0.  Should throw an exception.
**/
   public void Var002()
   {
      if (checkJdbc20 ()) {
         try {
            rsmd_.getColumnClassName (0);
            failed ("Didn't throw SQLException");
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getColumnClassName() - Check a column greater than the max.
Should throw an exception.
**/
   public void Var003()
   {
      if (checkJdbc20 ()) {
         try {
            rsmd_.getColumnClassName (35);
            failed ("Didn't throw SQLException");
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getColumnClassName() - Check when the result set is closed.
**/
   public void Var004()
   {
      if (checkJdbc20 ()) {
         try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                                           + JDRSMDTest.RSMDTEST_GET);
            ResultSetMetaData rsmd = rs.getMetaData ();
            rs.close ();
            String v = rsmd.getColumnClassName (rs_.findColumn ("C_SMALLINT"));
            s.close ();
            assertCondition (v.equals ("java.lang.Integer"));  // @D1c
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
getColumnClassName() - Check when the meta data is from a prepared
statement.
**/
   public void Var005()
   {
      if (checkJdbc20 ()) {
         try {
            PreparedStatement ps = connection_.prepareStatement ("SELECT * FROM "
                                                                 + JDRSMDTest.RSMDTEST_GET);
            ResultSetMetaData rsmd = ps.getMetaData ();
            String v = rsmd.getColumnClassName (rs_.findColumn ("C_SMALLINT"));
            ps.close ();
            assertCondition (v.equals ("java.lang.Integer"));  // @D1c
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
getColumnClassName() - Check a SMALLINT column.
**/
   public void Var006()
   {
      if (checkJdbc20 ()) {
         try {
            String s = rsmd_.getColumnClassName (rs_.findColumn ("C_SMALLINT"));
            assertCondition (s.equals ("java.lang.Integer"));   // @D1c
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
getColumnClassName() - Check an INTEGER column.
**/
   public void Var007()
   {
      if (checkJdbc20 ()) {
         try {
            String s = rsmd_.getColumnClassName (rs_.findColumn ("C_INTEGER"));
            assertCondition (s.equals ("java.lang.Integer"));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
getColumnClassName() - Check a REAL column.
**/
   public void Var008()
   {
      if (checkJdbc20 ()) {
         try {
            String s = rsmd_.getColumnClassName (rs_.findColumn ("C_REAL"));
            assertCondition (s.equals ("java.lang.Float"), "got "+s+" expected java.lang.Float for REAL");
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
getColumnClassName() - Check a FLOAT column.
**/
   public void Var009()
   {
      if (checkJdbc20 ()) {
         try {
            String s = rsmd_.getColumnClassName (rs_.findColumn ("C_FLOAT"));
            assertCondition (s.equals ("java.lang.Double"));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
getColumnClassName() - Check a FLOAT(3) column.
**/
   public void Var010()
   {
      if (checkJdbc20 ()) {
         try {
            String s = rsmd_.getColumnClassName (rs_.findColumn ("C_FLOAT_3"));
            assertCondition (s.equals ("java.lang.Float"), "got "+s+" expected java.lang.Float for FLOAT(3)");
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
getColumnClassName() - Check a DOUBLE column.
**/
   public void Var011()
   {
      if (checkJdbc20 ()) {
         try {
            String s = rsmd_.getColumnClassName (rs_.findColumn ("C_DOUBLE"));
            assertCondition (s.equals ("java.lang.Double"));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
getColumnClassName() - Check a DECIMAL(10,5) column.
**/
   public void Var012()
   {
      if (checkJdbc20 ()) {
         try {
            String s = rsmd_.getColumnClassName (rs_.findColumn ("C_DECIMAL_105"));
            assertCondition (s.equals ("java.math.BigDecimal"));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
getColumnClassName() - Check a NUMERIC(10,5) column.
**/
   public void Var013()
   {
      if (checkJdbc20 ()) {
         try {
            String s = rsmd_.getColumnClassName (rs_.findColumn ("C_NUMERIC_105"));
            assertCondition (s.equals ("java.math.BigDecimal"));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
getColumnClassName() - Check a CHAR(50) column.
**/
   public void Var014()
   {
      if (checkJdbc20 ()) {
         try {
            String s = rsmd_.getColumnClassName (rs_.findColumn ("C_CHAR_50"));
            assertCondition (s.equals ("java.lang.String"));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
getColumnClassName() - Check a VARCHAR(50) column.
**/
   public void Var015()
   {
      if (checkJdbc20 ()) {
         try {
            String s = rsmd_.getColumnClassName (rs_.findColumn ("C_VARCHAR_50"));
            assertCondition (s.equals ("java.lang.String"));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
getColumnClassName() - Check a BINARY(20) column.
**/
   public void Var016()
   {
      if (checkJdbc20 ()) {
         try {
            String s = rsmd_.getColumnClassName (rs_.findColumn ("C_BINARY_20"));
            assertCondition (s.equals ("[B"));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
getColumnClassName() - Check a VARBINARY(20) column.
**/
   public void Var017()
   {
      if (checkJdbc20 ()) {
         try {
            String s = rsmd_.getColumnClassName (rs_.findColumn ("C_VARBINARY_20"));
            assertCondition (s.equals ("[B"));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
getColumnClassName() - Check a DATE column.
**/
   public void Var018()
   {
      if (checkJdbc20 ()) {
         try {
            String s = rsmd_.getColumnClassName (rs_.findColumn ("C_DATE"));
            assertCondition (s.equals ("java.sql.Date"));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
getColumnClassName() - Check a TIME column.
**/
   public void Var019()
   {
      if (checkJdbc20 ()) {
         try {
            String s = rsmd_.getColumnClassName (rs_.findColumn ("C_TIME"));
            assertCondition (s.equals ("java.sql.Time"));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
getColumnClassName() - Check a TIMESTAMP column.
**/
   public void Var020()
   {
      if (checkJdbc20 ()) {
         try {
            String s = rsmd_.getColumnClassName (rs_.findColumn ("C_TIMESTAMP"));
            assertCondition (s.equals ("java.sql.Timestamp"));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
getColumnClassName() - Check a BLOB column.
**/
   public void Var021()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               String s = rsmd_.getColumnClassName (rs_.findColumn ("C_BLOB"));
               if (isToolboxDriver())
                  assertCondition (s.equals ("com.ibm.as400.access.AS400JDBCBlob")
                          || s.equals("com.ibm.as400.access.AS400JDBCBlobLocator"));
               else
                  assertCondition (s.equals ("com.ibm.db2.jdbc.app.DB2Blob"));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }



/**
getColumnClassName() - Check a CLOB column.
**/
   public void Var022()        // @B1c
   {
      if (checkJdbc20 ()) {   
         if (checkLobSupport ()) {
            try {
               String s = rsmd_.getColumnClassName (rs_.findColumn ("C_CLOB"));
               if (isToolboxDriver())
                  assertCondition (s.equals ("com.ibm.as400.access.AS400JDBCClob")
                          || s.equals("com.ibm.as400.access.AS400JDBCClobLocator"));
               else
                  assertCondition (s.equals ("com.ibm.db2.jdbc.app.DB2Clob"));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }



/**
getColumnClassName() - Check a DBCLOB column.
**/
   public void Var023()        // @B1C
   {
      if (checkJdbc20 ()) {   
         if (checkLobSupport ()) {
            try {
               String s = rsmd_.getColumnClassName (rs_.findColumn ("C_DBCLOB"));
               if (isToolboxDriver())
                  assertCondition (s.equals ("com.ibm.as400.access.AS400JDBCClob")
                          || s.equals("com.ibm.as400.access.AS400JDBCClobLocator"));
               else
                  assertCondition (s.equals ("com.ibm.db2.jdbc.app.DB2Clob"));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }



/**
getColumnClassName() - Check a DATALINK column.

Datalinks come back as java.net.URL objects in JDBC 3.0 and higher, but java.lang.String
objects in prior releases.
**/
   public void Var024()
   {

 
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               String s = rsmd_.getColumnClassName (rs_.findColumn ("C_DATALINK"));
               if(isToolboxDriver())
                   failed("Didn't throw exception");
               else if ((getRelease() < JDTestDriver.RELEASE_V7R1M0) || !isJDK14 ) // @E1a
                   assertCondition (s.equals ("java.lang.String"));
               else                                             // @E1a
                   assertCondition (s.equals ("java.net.URL"));          // @E1a
            } catch (Exception e) {
                if(isToolboxDriver())          //An exception should be thrown since no protocol is specified on the url
                    assertExceptionIsInstanceOf(e, "java.sql.SQLException");
                else
                    failed (e, "Unexpected Exception");
            }
         }
      }
   }



/**
getColumnClassName() - Check a DISTINCT column.
**/
   public void Var025()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               String s = rsmd_.getColumnClassName (rs_.findColumn ("C_DISTINCT"));
               assertCondition (s.equals ("java.math.BigDecimal"));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }



// @B0A
/**
getColumnClassName() - Check a BIGINT column.
**/
   public void Var026()
   {
      if (checkJdbc20 ()) {
         if (checkBigintSupport ()) {
            try {
               String s = rsmd_.getColumnClassName (rs_.findColumn ("C_BIGINT"));
               assertCondition (s.equals ("java.lang.Long"));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }

/**
getColumnClassName() - Check an XML column.
**/
   public void Var027()
   {
      if (checkJdbc20 ()) {
         if (checkXmlSupport ()) {
            try {
		String expected; 
               String s = rsmd2_.getColumnClassName (rs2_.findColumn ("C_XML"));
	       if (isJdbc40()) { 
		   expected = "SQLXML"; 
	       } else {
		   expected = "Clob"; 
	       }
	       assertCondition (s.indexOf(expected)> 0,
				"Expected "+expected+" but got "+s);
            } catch (Exception e) {
               failed (e, "Unexpected Exception -- for find column C_XML from "+rsmd2Info_);
            }
         }
      }
   }

/**
getColumnClassName() - Check a NCHAR(50) column.
**/
   public void Var028()
   {
      if (checkJdbc20 ()) {
         try {
            String s = rsmd_.getColumnClassName (rs_.findColumn ("C_NCHAR_50"));
            assertCondition (s.equals ("java.lang.String"));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
getColumnClassName() - Check a NVARCHAR(50) column.
**/
   public void Var029()
   {
      if (checkJdbc20 ()) {
         try {
            String s = rsmd_.getColumnClassName (rs_.findColumn ("C_NVARCHAR_50"));
            assertCondition (s.equals ("java.lang.String"));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
getColumnClassName() - Check a NCLOB column.
**/
   public void Var030()        // @B1c
   {
      if (checkJdbc20 ()) {   
         if (checkLobSupport ()) {
            try {
               String s = rsmd_.getColumnClassName (rs_.findColumn ("C_NCLOB"));
               if (isToolboxDriver())
                  assertCondition (s.equals ("com.ibm.as400.access.AS400JDBCClob")
                          || s.equals("com.ibm.as400.access.AS400JDBCClobLocator")
				   || s.equals("com.ibm.as400.access.AS400JDBCNClobLocator"), "got incorrect "+s);
               else
                  assertCondition (s.equals ("com.ibm.db2.jdbc.app.DB2Clob"), "got incorrect "+s);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
getColumnClassName() - Check a BOOLEAN column.
**/
   public void Var031()        // @B1c
   {
  
         if (checkBooleanSupport ()) {
            try {
               String s = rsmd_.getColumnClassName (rs_.findColumn ("C_BOOLEAN"));

                  assertCondition (s.equals ("java.lang.Boolean"), "got incorrect "+s+" for boolean");
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
  
   }



}


