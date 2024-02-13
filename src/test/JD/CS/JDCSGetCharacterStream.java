///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetCharacterStream.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetCharacterStream.java
//
// Classes:      JDCSGetCharacterStream
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDTestDriver;

import java.io.FileOutputStream;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable;
import java.io.Reader; 



/**
Testcase JDCSGetCharacterStream.  This tests the following
method of the JDBC CallableStatement class:

<ul>
<li>getCharacterStream()
</ul>
**/
public class JDCSGetCharacterStream
extends JDCSGetTestcase
{
    static boolean RUNTOOLONG = false; 

   // Note.. LARGECLOB_SIZE must be a multiple of 200
   private static int  LARGE_CLOB_SIZE = 20000000;
// Private data.

   private Connection          connection2_;
   private CallableStatement   csTypes2_;
   private StringBuffer        sb = new StringBuffer(); 
   String getMethodName  = "getCharacterStream"; 

/**
Constructor.
**/
   public JDCSGetCharacterStream (AS400 systemObject,
                       Hashtable namesAndVars,
                       int runMode,
                       FileOutputStream fileOutputStream,
                       
                       String password)
   {
      super (systemObject, "JDCSGetCharacterStream",
             namesAndVars, runMode, fileOutputStream,
             password);
   }


   public JDCSGetCharacterStream (AS400 systemObject,
		       String testcaseName, 
                       Hashtable namesAndVars,
                       int runMode,
                       FileOutputStream fileOutputStream,
                       
                       String password)
   {
      super (systemObject, testcaseName,
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
     super.setup("time format=jis;date format=iso"); 
      connection2_ = testDriver_.getConnection (baseURL_
                                                + ";errors=full;lob threshold=0;time format=jis;date format=iso", userId_, encryptedPassword_);
      connection2_.setAutoCommit(false);
      connection2_.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

      csTypes2_ = JDSetupProcedure.prepare (connection2_,
                                            JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
      JDSetupProcedure.register (csTypes2_, JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_, getDriver());
      csTypes2_.execute ();
   }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
   protected void cleanup ()
   throws Exception
   {
      connection2_.commit();
      connection2_.close ();        
      super.cleanup(); 
                                      
   }






/**
getCharacterStream() - Get parameter -1.
**/
   public void Var001()
   {
      if (checkJdbc40 ()) {
         try {
            csTypes_.execute ();
            Reader p = (Reader) JDReflectionUtil.callMethod_O(csTypes_,getMethodName, -1);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getCharacterStream() - Get parameter 0.
**/
   public void Var002()
   {
      if (checkJdbc40 ()) {
         try {
            Reader p = (Reader) JDReflectionUtil.callMethod_O(csTypes_,getMethodName, 0);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getCharacterStream() - Use a parameter that is too big.
**/
   public void Var003()
   {
      if (checkJdbc40 ()) {
         try {
            Reader p = (Reader) JDReflectionUtil.callMethod_O(csTypes_,getMethodName, 35);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getCharacterStream() - Get a parameter when there are no parameters.
**/
   public void Var004()
   {        
      if (checkJdbc40 ()) {
         // I created a whole new Connection object here to work
         // around a server bug.
         Connection c = null;
         try {
            c = testDriver_.getConnection (baseURL_
                                           + ";errors=full", userId_, encryptedPassword_);
            CallableStatement cs = c.prepareCall ("CALL "
                                                  + JDSetupProcedure.STP_CS0);
            cs.execute ();
            Reader p = (Reader) JDReflectionUtil.callMethod_O(cs, getMethodName, 1);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         } finally {
            try {
               if (c != null) { c.close (); c = null; } 
            } catch (SQLException e) {
               // Ignore.
            }
         }
      }
   }



/**
getCharacterStream() - Get a parameter that was not registered.
**/
   public void Var005()
   {
      if (checkJdbc40 ()) {
         try {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
            cs.execute ();
            Reader p = (Reader) JDReflectionUtil.callMethod_O(cs, getMethodName, 12);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getCharacterStream() - Get a parameter when the statement has not been
executed.
**/
   public void Var006()
   {
      if (checkJdbc40 ()) {
         try {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
            cs.registerOutParameter (20, Types.CLOB);
            Reader p = (Reader) JDReflectionUtil.callMethod_O(cs, getMethodName, 20);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getCharacterStream() - Get a parameter when the statement is closed.
**/
   public void Var007()
   {
      if (checkJdbc40 ()) {
         try {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
            cs.registerOutParameter (20, Types.CLOB);
            cs.execute ();
            cs.close ();
            Reader p = (Reader) JDReflectionUtil.callMethod_O(cs, getMethodName, 20);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getCharacterStream() - Get an IN parameter that was correctly registered.
**/
   public void Var008()
   {
      if (checkJdbc40 ()) {
         try {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESIN, supportedFeatures_);
            JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESIN,
                                                 supportedFeatures_);
            cs.registerOutParameter (20, Types.CLOB);
            cs.execute ();
            Reader p = (Reader) JDReflectionUtil.callMethod_O(cs, getMethodName, 20);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }


/**
getCharacterStream() - Get an INOUT parameter, where the OUT parameter is
longer than the IN parameter.
**/
   public void Var009()
   {
      sb.setLength(0); 
      if (checkJdbc40 ()) {
         if (checkLobSupport ()) {
            try {
               CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                                JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_);
               JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                                    supportedFeatures_);
               JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                          supportedFeatures_, getDriver());
               cs.registerOutParameter (20, Types.CLOB);
               cs.execute ();
               Reader p = (Reader) JDReflectionUtil.callMethod_O(cs, getMethodName, 20);
               assertCondition (compare(p, "Welcome",sb),sb);
            } catch (Exception e)
            {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
getCharacterStream() - Get a type that was registered as a SMALLINT.
**/
   public void Var010()
   {
     sb.setLength(0); 
      if (checkJdbc40 ()) {
         try {
            Reader p = (Reader) JDReflectionUtil.callMethod_O(csTypes_,getMethodName, 1);
            String expected = "123"; 
            assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");

         } catch (Exception e) {
           failed (e, "Unexpected Exception -- updated 11/17/2011");
         }
      }
   }



/**
getCharacterStream() - Get a type that was registered as an INTEGER.
**/
   public void Var011()
   {
     sb.setLength(0); 
      if (checkJdbc40 ()) {
         try {
            Reader p = (Reader) JDReflectionUtil.callMethod_O(csTypes_,getMethodName, 2);
            String expected = "-456"; 
            assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");
         } catch (Exception e) {
           failed (e, "Unexpected Exception -- updated 11/17/2011");
         }
      }
   }



/**
getCharacterStream() - Get a type that was registered as an REAL.
**/
   public void Var012()
   {
     sb.setLength(0); 
      if (checkJdbc40 ()) {
         try {
            Reader p = (Reader) JDReflectionUtil.callMethod_O(csTypes_,getMethodName, 3);
            String expected = "789.54"; 
            assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");
         } catch (Exception e) {
           failed (e, "Unexpected Exception -- updated 11/17/2011");
         }
      }
   }



/**
getCharacterStream() - Get a type that was registered as an FLOAT.
**/
   public void Var013()
   {
     sb.setLength(0); 
      if (checkJdbc40 ()) {
         try {
            Reader p = (Reader) JDReflectionUtil.callMethod_O(csTypes_,getMethodName, 4);
            String expected = "253.1027"; 
            assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");
         } catch (Exception e) {
           failed (e, "Unexpected Exception -- updated 11/17/2011");
         }
      }
   }



/**
getCharacterStream() - Get a type that was registered as an DOUBLE.
**/
   public void Var014()
   {
     sb.setLength(0); 
      if (checkJdbc40 ()) {
         try {
            Reader p = (Reader) JDReflectionUtil.callMethod_O(csTypes_,getMethodName, 5);
            String expected = "-987.3434"; 
            assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");
         } catch (Exception e) {
           failed (e, "Unexpected Exception -- updated 11/17/2011");
         }
      }
   }



/**
getCharacterStream() - Get a type that was registered as an DECIMAL.
**/
   public void Var015()
   {
     sb.setLength(0); 
      if (checkJdbc40 ()) {
         try {
            Reader p = (Reader) JDReflectionUtil.callMethod_O(csTypes_,getMethodName, 6);
            String expected = "54362"; 
            assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");

         } catch (Exception e) {
           failed (e, "Unexpected Exception -- updated 11/17/2011");
         }
      }
   }



/**
getCharacterStream() - Get a type that was registered as an NUMERIC.
**/
   public void Var016()
   {
     sb.setLength(0); 
      if (checkJdbc40 ()) {
         try {
            Reader p = (Reader) JDReflectionUtil.callMethod_O(csTypes_,getMethodName, 8);
            String expected = "-1112"; 
            assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");
         } catch (Exception e) {
           failed (e, "Unexpected Exception -- updated 11/17/2011");
         }
      }
   }



/**
getCharacterStream() - Get a type that was registered as a CHAR.
**/
   public void Var017()
   {
     sb.setLength(0); 
      if (checkJdbc40 ()) {
         try {
            Reader p = (Reader) JDReflectionUtil.callMethod_O(csTypes_,getMethodName, 11);
            String expected = "Jim                                               "; 
            assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");
         } catch (Exception e) {
           failed (e, "Unexpected Exception -- updated 11/17/2011");
         }
      }
   }



/**
getCharacterStream() - Get a type that was registered as a VARCHAR.
**/
   public void Var018()
   {
     sb.setLength(0); 
      if (checkJdbc40 ()) {
         try {
            Reader p = (Reader) JDReflectionUtil.callMethod_O(csTypes_,getMethodName, 12);
            String expected = "Charlie"; 
            assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");
         } catch (Exception e) {
           failed (e, "Unexpected Exception -- updated 11/17/2011");
         }
      }
   }



/**
getCharacterStream() - Get a type that was registered as a BINARY.
**/
   public void Var019()
   {
     sb.setLength(0); 
      if (checkJdbc40 ()) {
         try {
            Reader p = (Reader) JDReflectionUtil.callMethod_O(csTypes_,getMethodName, 13);
            String expected = "Murch               "; 
            if(isToolboxDriver()){
              expected = "4D75726368202020202020202020202020202020"; 
            }
            assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");
         } catch (Exception e) {
           failed (e, "Unexpected Exception -- updated 11/17/2011");
         }
      }
   }



/**
getCharacterStream() - Get a type that was registered as a VARBINARY.
**/
   public void Var020()
   {
     sb.setLength(0); 
      if (checkJdbc40 ()) {
         try {
            Reader p = (Reader) JDReflectionUtil.callMethod_O(csTypes_,getMethodName, 14);
            String expected = "446176652057616C6C";
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		expected = "Dave Wall"; 
	    }
            assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");
         } catch (Exception e) {
           failed (e, "Unexpected Exception -- updated 11/17/2011");
         }
      }
   }



/**
getCharacterStream() - Get a type that was registered as a DATE.
**/
   public void Var021()
   {
     sb.setLength(0); 
      if (checkJdbc40 ()) {
         try {
            Reader p = (Reader) JDReflectionUtil.callMethod_O(csTypes_,getMethodName, 15);
            String expected = "1998-04-15"; 
            assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");
         } catch (Exception e) {
           failed (e, "Unexpected Exception -- updated 11/17/2011");
         }
      }
   }



/**
getCharacterStream() - Get a type that was registered as a TIME.
**/
   public void Var022()
   {
     sb.setLength(0); 
      if (checkJdbc40 ()) {
         try {
            Reader p = (Reader) JDReflectionUtil.callMethod_O(csTypes_,getMethodName, 16);
            String expected = "08:42:30"; 
            assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");
         } catch (Exception e) {
           failed (e, "Unexpected Exception -- updated 11/17/2011");
         }
      }
   }



/**
getCharacterStream() - Get a type that was registered as a TIMESTAMP.
**/
   public void Var023()
   {
     sb.setLength(0); 
      if (checkJdbc40 ()) {
         try {
            Reader p = (Reader) JDReflectionUtil.callMethod_O(csTypes_,getMethodName, 17);
            String expected = "2001-11-18 13:42:22.123456";
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		expected = "2001-11-18-13.42.22.123456";
	    } 
            assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");
         } catch (Exception e) {
           failed (e, "Unexpected Exception -- updated 11/17/2011");
         }
      }
   }



/**
getCharacterStream() - Get a type that was registered as an OTHER.
**/
   public void Var024()
   {
     sb.setLength(0); 
         if (checkJdbc40 ()) {
            try {
               Reader p = (Reader) JDReflectionUtil.callMethod_O(csTypes_,getMethodName, 18);
               String expected = "https://github.com/IBM/JTOpen-test/blob/main/README.md"; 
               assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");
            } catch (Exception e) {
              failed (e, "Unexpected Exception -- updated 11/17/2011");
            }
         }
   }



/**
getCharacterStream() - Get a type that was registered as a BLOB.
**/
   public void Var025()
   {
         if (checkJdbc40 ()) {
            try {
               Reader p = (Reader) JDReflectionUtil.callMethod_O(csTypes_,getMethodName, 19);
               String expected = "446176652045676C65";
	       if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		   expected = "Dave Egle";
	       }
               assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");
            } catch (Exception e) {
              failed (e, "Unexpected Exception -- updated 11/17/2011");
            }
         }
   }



/**
getCharacterStream() - Get a type that was registered as a CLOB when the data
was returned in the result set.
**/
   public void Var026()
   {
     sb.setLength(0); 
         if (checkJdbc40 ()) {
               try {
                  Reader p = (Reader) JDReflectionUtil.callMethod_O(csTypes_,getMethodName, 20);
                  assertCondition (compare (p, "Chris Smith",sb),sb);
               } catch (Exception e) {
                  failed (e, "Unexpected Exception");
               }
         }
   }



/**
getCharacterStream() - Get a type that was registered as a CLOB when the locator
was returned in the result set.

SQL400 - let this work.
**/
   public void Var027()
   {
     sb.setLength(0); 
         if (checkJdbc40 ()) {
              try {
                 Reader p = (Reader) JDReflectionUtil.callMethod_O(csTypes2_,getMethodName,20);
                 assertCondition (compare (p, "Chris Smith",sb),sb);
              } catch (Exception e) {
                 failed (e, "Unexpected Exception");
              }
         }
   }



/**
getCharacterStream() - Get a type that was registered as a CLOB when the data
was returned in the result set and is a DBCLOB.
**/
   public void Var028()
   {
     sb.setLength(0); 
         if (checkJdbc40 ()) {
               try {
                  Reader p = (Reader) JDReflectionUtil.callMethod_O(csTypes_,getMethodName, 21);
                  assertCondition (compare (p, "Jeff Lee",sb),sb);
               } catch (Exception e) {
                  failed (e, "Unexpected Exception");
               }
         }
   }



/**
getCharacterStream() - Get a type that was registered as a CLOB when the locator
was returned in the result set and is a DBCLOB.

SQL400 - let this work.
**/
   public void Var029()
   {
     sb.setLength(0); 
         if (checkJdbc40 ()) {
              try {
                 Reader p = (Reader) JDReflectionUtil.callMethod_O(csTypes2_,getMethodName,21);
                 assertCondition (compare (p, "Jeff Lee",sb),sb);
              } catch (Exception e) {
                 failed (e, "Unexpected Exception");
              }
         }
   }



/**
getCharacterStream() - Get a type that was registered as a BIGINT.
**/
   public void Var030()
   {
     sb.setLength(0); 
      if (checkJdbc40 ()) {
            try {
               Reader p = (Reader) JDReflectionUtil.callMethod_O(csTypes_,getMethodName, 22);
               String expected = "987662234567"; 
               assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");
            } catch (Exception e) {
              failed (e, "Unexpected Exception -- updated 11/17/2011");
            }
      }
   }



/**
getCharacterStream() - Get an INOUT parameter, where the OUT parameter is
longer than the IN parameter, when the output parameter is registered first.

SQL400 - We added this testcase because of a customer bug.
**/
   public void Var031()
   {
     sb.setLength(0); 
      if (checkJdbc40 ()) {
            try {
               CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                                JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_);
               JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                          supportedFeatures_, getDriver());
               cs.registerOutParameter (20, Types.CLOB);
               JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                                    supportedFeatures_);
               cs.execute ();
               Reader p = (Reader) JDReflectionUtil.callMethod_O(cs, getMethodName, 20);
               assertCondition (compare(p, "Welcome",sb),sb);
            } catch (Exception e)
            {
               failed (e, "Unexpected Exception");
            }
         }
   }
// NERYKAL - named parameteR
/**
getCharacterStream() - Get a type that was registered as a SMALLINT.
**/
   public void Var032()
   {
     sb.setLength(0); 
      if(checkJdbc40()) {
         try {
           Reader p = (Reader) JDReflectionUtil.callMethod_OS(csTypes_,getMethodName, "P_SMALLINT");
           String expected = "123"; 
           assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");
         } catch (Exception e) {
           failed (e, "Unexpected Exception -- updated 11/17/2011");
         }
      }
   }

/**
getCharacterStream() - Get a type that was registered as an INTEGER.
**/
   public void Var033()
   {
     sb.setLength(0); 
      if(checkJdbc40()) {
         try {
            Reader p = (Reader) JDReflectionUtil.callMethod_OS(csTypes_,getMethodName, "P_INTEGER");
            String expected = "-456"; 
            assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");
         } catch (Exception e) {
           failed (e, "Unexpected Exception -- updated 11/17/2011");
         }
      }
   }

/**
getCharacterStream() - Get a type that was registered as an REAL.
**/
   public void Var034()
   {
     sb.setLength(0); 
      if(checkJdbc40()) {
         try {
            Reader p = (Reader) JDReflectionUtil.callMethod_OS(csTypes_,getMethodName, "P_REAL");
            String expected = "789.54"; 
            assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");
         } catch (Exception e) {
           failed (e, "Unexpected Exception -- updated 11/17/2011");
         }
      }
   }

/**
getCharacterStream() - Get a type that was registered as an FLOAT.
**/
   public void Var035()
   {
     sb.setLength(0); 
      if(checkJdbc40()) {
         try {
            Reader p = (Reader) JDReflectionUtil.callMethod_OS(csTypes_,getMethodName, "P_FLOAT");
            String expected = "253.1027"; 
            assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");
         } catch (Exception e) {
           failed (e, "Unexpected Exception -- updated 11/17/2011");
         }
      }
   }

/**
getCharacterStream() - Get a type that was registered as an DOUBLE.
**/
   public void Var036()
   {
     sb.setLength(0); 
      if(checkJdbc40()) {
         try {
            Reader p = (Reader) JDReflectionUtil.callMethod_OS(csTypes_,getMethodName, "P_DOUBLE");
            String expected = "-987.3434"; 
            assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");
         } catch (Exception e) {
           failed (e, "Unexpected Exception -- updated 11/17/2011");
         }
      }
   }

/**
getCharacterStream() - Get a type that was registered as an DECIMAL.
**/
   public void Var037()
   {
     sb.setLength(0); 
      if(checkJdbc40()) {
         try {
            Reader p = (Reader) JDReflectionUtil.callMethod_OS(csTypes_,getMethodName, "P_DECIMAL_50");
            String expected = "54362"; 
            assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");
         } catch (Exception e) {
           failed (e, "Unexpected Exception -- updated 11/17/2011");
         }
      }
   }

/**
getCharacterStream() - Get a type that was registered as an DECIMAL.
**/
   public void Var038()
   {
     sb.setLength(0); 
      if(checkJdbc40()) {
         try {
            Reader p = (Reader) JDReflectionUtil.callMethod_OS(csTypes_,getMethodName, "P_DECIMAL_105");
            String expected = "-94732.12345"; 
            assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");
         } catch (Exception e) {
           failed (e, "Unexpected Exception -- updated 11/17/2011");
         }
      }
   }

/**
getCharacterStream() - Get a type that was registered as an NUMERIC.
**/
   public void Var039()
   {
     sb.setLength(0); 
      if(checkJdbc40()) {
         try {
            Reader p = (Reader) JDReflectionUtil.callMethod_OS(csTypes_,getMethodName, "P_NUMERIC_50");
            String expected = "-1112"; 
            assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");
         } catch (Exception e) {
           failed (e, "Unexpected Exception -- updated 11/17/2011");
         }
      }
   }

/**
getCharacterStream() - Get a type that was registered as an NUMERIC.
**/
   public void Var040()
   {
     sb.setLength(0); 
      if(checkJdbc40()){
         try {
            Reader p = (Reader) JDReflectionUtil.callMethod_OS(csTypes_,getMethodName, "P_NUMERIC_105");
            String expected = "19.98765"; 
            assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");
         } catch (Exception e) {
           failed (e, "Unexpected Exception -- updated 11/17/2011");
         }
      }
   }

/**
getCharacterStream() - Get a type that was registered as a CHAR.
**/
   public void Var041()
   {
     sb.setLength(0); 
     if(checkJdbc40()) {
         try {
            Reader p = (Reader) JDReflectionUtil.callMethod_OS(csTypes_,getMethodName, "P_CHAR_1");
            String expected = "C"; 
            assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");
         } catch (Exception e) {
           failed (e, "Unexpected Exception -- updated 11/17/2011");
         }
      }
   }

/**
getCharacterStream() - Get a type that was registered as a CHAR.
**/
   public void Var042()
   {
     sb.setLength(0); 
    if(checkJdbc40()) {
         try {
            Reader p = (Reader) JDReflectionUtil.callMethod_OS(csTypes_,getMethodName, "P_CHAR_50");
            String expected = "Jim                                               "; 
            assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");
         } catch (Exception e) {
           failed (e, "Unexpected Exception -- updated 11/17/2011");
         }
      }
   }

/**
getCharacterStream() - Get a type that was registered as a VARCHAR.
**/
   public void Var043()
   {
     sb.setLength(0); 
      if(checkJdbc40()) {
         try {
            Reader p = (Reader) JDReflectionUtil.callMethod_OS(csTypes_,getMethodName, "P_VARCHAR_50");
            String expected = "Charlie"; 
            assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");
         } catch (Exception e) {
           failed (e, "Unexpected Exception -- updated 11/17/2011");
         }
      }
   }

/**
getCharacterStream() - Get a type that was registered as a BINARY.
**/
   public void Var044()
   {
     sb.setLength(0); 
     if(checkJdbc40()) {
         try {
            Reader p = (Reader) JDReflectionUtil.callMethod_OS(csTypes_,getMethodName, "P_BINARY_20");
            String expected = "4D75726368202020202020202020202020202020";
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		expected = "Murch               "; 
	    }
            assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");
         } catch (Exception e) {
           failed (e, "Unexpected Exception -- updated 11/17/2011");
         }
      }
   }

/**
getCharacterStream() - Get a type that was registered as a VARBINARY.
**/
   public void Var045()
   {
     sb.setLength(0); 
      if(checkJdbc40()) {
         try {
            Reader p = (Reader) JDReflectionUtil.callMethod_OS(csTypes_,getMethodName, "P_VARBINARY_20");
            String expected = "446176652057616C6C";
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		expected = "Dave Wall"; 
	    }
            assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");
         } catch (Exception e) {
           failed (e, "Unexpected Exception -- updated 11/17/2011");
         }
      }
   }

/**
getCharacterStream() - Get a type that was registered as a DATE.
**/
   public void Var046()
   {
     sb.setLength(0); 
    if(checkJdbc40()) {
         try {
            Reader p = (Reader) JDReflectionUtil.callMethod_OS(csTypes_,getMethodName, "P_DATE");
            String expected = "1998-04-15"; 
            assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");
         } catch (Exception e) {
           failed (e, "Unexpected Exception -- updated 11/17/2011");
         }
      }
   }

/**
getCharacterStream() - Get a type that was registered as a TIME.
**/
   public void Var047()
   {
     sb.setLength(0); 
     if(checkJdbc40()) {
         try {
            Reader p = (Reader) JDReflectionUtil.callMethod_OS(csTypes_,getMethodName, "P_TIME");
            String expected = "08:42:30"; 
            assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");
         } catch (Exception e) {
           failed (e, "Unexpected Exception -- updated 11/17/2011");
         }
      }
   }

/**
getCharacterStream() - Get a type that was registered as a TIMESTAMP.
**/
   public void Var048()
   {
     sb.setLength(0); 
     if(checkJdbc40()) {
         try {
            Reader p = (Reader) JDReflectionUtil.callMethod_OS(csTypes_,getMethodName, "P_TIMESTAMP");
            String expected = "2001-11-18 13:42:22.123456";
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		expected = "2001-11-18-13.42.22.123456";
	    }
            assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");
         } catch (Exception e) {
           failed (e, "Unexpected Exception -- updated 11/17/2011");
         }
      }
   }

/**
getCharacterStream() - Get a type that was registered as an OTHER.
**/
   public void Var049()
   {
     sb.setLength(0); 
    if(checkJdbc40()) {
         if (checkJdbc40 ()) {
            try {
               Reader p = (Reader) JDReflectionUtil.callMethod_OS(csTypes_,getMethodName, "P_DATALINK");
               String expected = "https://github.com/IBM/JTOpen-test/blob/main/README.md"; 
               assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");
            } catch (Exception e) {
              failed (e, "Unexpected Exception -- updated 11/17/2011");
            }
         }
      }
   }

/**
getCharacterStream() - Get a type that was registered as a BLOB.
**/
   public void Var050()
   {
     sb.setLength(0); 
      if (checkJdbc40 ()) {
            try {
               Reader p = (Reader) JDReflectionUtil.callMethod_OS(csTypes_,getMethodName, "P_BLOB");
               String expected = "446176652045676C65";  /* primed via toolbox */ 
	       if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		   expected = "Dave Egle"; 
	       }

               assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");
            } catch (Exception e) {
              failed (e, "Unexpected Exception -- updated 11/17/2011");
            }
         }
   }

/**
getCharacterStream() - Get a type that was registered as a CLOB when the data
was returned in the result set.
**/
   public void Var051()
   {
     sb.setLength(0); 
        if (checkJdbc40 ()) {
               try {
                  Reader p = (Reader) JDReflectionUtil.callMethod_OS(csTypes_,getMethodName, "P_CLOB");
                  assertCondition (compare (p, "Chris Smith",sb),sb);
               } catch (Exception e) {
                  failed (e, "Unexpected Exception");
               }
            }
   }

/**
getCharacterStream() - Get a type that was registered as a CLOB when the locator
was returned in the result set.

SQL400 - let this work.
**/
   public void Var052()
   {
     sb.setLength(0); 
    if(checkJdbc40()) {
              try {
                 Reader p = (Reader) JDReflectionUtil.callMethod_OS(csTypes2_,getMethodName,"P_CLOB");
                 assertCondition (compare (p, "Chris Smith",sb),sb);
              } catch (Exception e) {
                 failed (e, "Unexpected Exception");
              }
      }
   }

/**
getCharacterStream() - Get a type that was registered as a CLOB when the data
was returned in the result set and is a DBCLOB.
**/
   public void Var053()
   {
     sb.setLength(0); 
   if(checkJdbc40()) {
               try {
                  Reader p = (Reader) JDReflectionUtil.callMethod_OS(csTypes_,getMethodName, "P_DBCLOB");
                  assertCondition (compare (p, "Jeff Lee",sb),sb);
               } catch (Exception e) {
                  failed (e, "Unexpected Exception");
               }
            }
   }

/**
getCharacterStream() - Get a type that was registered as a CLOB when the locator
was returned in the result set and is a DBCLOB.

SQL400 - let this work.
**/
   public void Var054()
   {
     sb.setLength(0); 
     if(checkJdbc40()) {
              try {
                 Reader p = (Reader) JDReflectionUtil.callMethod_OS(csTypes2_,getMethodName,"P_DBCLOB");
                 assertCondition (compare (p, "Jeff Lee",sb),sb);
              } catch (Exception e) {
                 failed (e, "Unexpected Exception");
              }
      }
   }

/**
getCharacterStream() - Get a type that was registered as a BIGINT.
**/
   public void Var055()
   {
     sb.setLength(0); 
    if(checkJdbc40()) {
            try {
               Reader p = (Reader) JDReflectionUtil.callMethod_OS(csTypes_,getMethodName, "P_BIGINT");
               String expected = "987662234567"; 
               assertCondition (compare(p, expected,sb),sb.toString()+" -- updated 11/17/2011 ");
            } catch (Exception e) {
              failed (e, "Unexpected Exception -- updated 11/17/2011");
            }
      }
   }



/**
 * get Clob on a very large clob
 */ 
   /**
   getBlob() - Get blob on a large blob 
   **/
      public void Var056()
      {
        sb.setLength(0); 
	  if (!RUNTOOLONG) {
	      notApplicable("Takes too long to run NEED TO FIX!!!! ");
	  } else { 
	      if (checkJdbc40 ()) {
		  String sql ="";
		  try { 
		      sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+".BIGCLOBOUT(OUT B CLOB("+LARGE_CLOB_SIZE+")) LANGUAGE SQL " +
			"SPECIFIC BIGCLOBOUT JDCSBBOUT: BEGIN "+
			"DECLARE DUMMY CLOB("+LARGE_CLOB_SIZE+"); "+
			"DECLARE C INT DEFAULT 0; "+ 
			" SET DUMMY=CLOB(X'');"+
			"BUILD_LOOP: "+
			"LOOP "+
			" SET C = C + 200; "+
			" SET DUMMY = DUMMY CONCAT CLOB('ABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWXY');"+
			" IF C = "+LARGE_CLOB_SIZE+" THEN "+
			"   LEAVE BUILD_LOOP;  "+
			" END IF; "  +
			"END LOOP ; "+
			"SET B=DUMMY; "+
			"END JDCSBBOUT" ;

		      Statement stmt = connection_.createStatement ();
		      try {
			  stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".BIGCLOBOUT");
		      } catch (Exception e) {}    	    
		      stmt.executeUpdate(sql);
		      sql = "{call "+JDCSTest.COLLECTION+".BIGCLOBOUT (?)}"; 
		      CallableStatement cstmt = connection_.prepareCall(sql);
		      cstmt.registerOutParameter(1, java.sql.Types.CLOB);

		 //
		 // Set the timeout for building the clob
		 // 
		      cstmt.setQueryTimeout(60);

		      cstmt.execute();

		      StringBuffer sb1 = new StringBuffer(); 

		      for (int i = 0; i < LARGE_CLOB_SIZE; i++) {
			  sb1.append('A'); 
		      }
		      String expected = sb1.toString(); 

		      Reader check = (Reader) JDReflectionUtil.callMethod_O(cstmt,getMethodName,1);
		      assertCondition (compare(check, expected,sb), "Testcase added 12/14/2005 by native driver "+sb);
		  } catch (Exception e) {
		      failed (e, "Unexpected Exception Last SQL was "+sql+" Testcase added 12/14/2005 by native driver -- "+
			      " Currrently fails -- see issue 29243 ");
		  } 
	      }
	  }
      }

      /**
       * getCharacterStream() - Get CLOB on a large CLOB
       */
  public void Var057() {
    sb.setLength(0); 
    if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
      notApplicable("V5R3 or later test");
    } else {
      if (checkJdbc40()) {
	  if (getDriver()  == JDTestDriver.DRIVER_NATIVE) { /*native only large clob test */ 
	      String sql = "";
	      try {
		  StringBuffer sb1 = new StringBuffer();

		  for (int i = 0; i < LARGE_CLOB_SIZE; i++) {
		      sb1.append('A');
		  }
		  String expected = sb1.toString();
		  String quarter = sb1.substring(0, LARGE_CLOB_SIZE / 4);
		  Statement stmt = connection_.createStatement();

		  sql = "DROP TABLE " + JDCSTest.COLLECTION + ".BIGCLOBQ";
		  try {
		      stmt.executeUpdate(sql);
		  } catch (Exception e) {
		  }

		  sql = "CREATE TABLE " + JDCSTest.COLLECTION + ".BIGCLOBQ (C1 CLOB("
		    + LARGE_CLOB_SIZE / 4 + "))";
		  stmt.executeUpdate(sql);
		  sql = "INSERT INTO " + JDCSTest.COLLECTION + ".BIGCLOBQ VALUES(? )";
		  PreparedStatement pstmt = connection_.prepareStatement(sql);
		  pstmt.setString(1, quarter);
		  pstmt.executeUpdate();

		  sql = "DROP TABLE " + JDCSTest.COLLECTION + ".BIGCLOBT";
		  try {
		      stmt.executeUpdate(sql);
		  } catch (Exception e) {
		  }

		  sql = "CREATE TABLE " + JDCSTest.COLLECTION + ".BIGCLOBT (C1 CLOB("
		    + LARGE_CLOB_SIZE + "))";
		  stmt.executeUpdate(sql);
		  sql = "INSERT INTO " + JDCSTest.COLLECTION
		    + ".BIGCLOBT (SELECT C1 CONCAT C1 CONCAT C1 CONCAT C1 FROM "
		    + JDCSTest.COLLECTION + ".BIGCLOBQ)";
		  stmt.executeUpdate(sql);

		  try {
		      stmt.executeUpdate("drop procedure " + JDCSTest.COLLECTION
					 + ".BIGCLOBO2");
		  } catch (Exception e) {
		      String exString = e.toString();
		      if (exString.toUpperCase().indexOf("NOT FOUND") == -1) {
			  System.out.println("Warning on drop procedure");
			  e.printStackTrace();
		      }
		  }

		  sql = "CREATE PROCEDURE " + JDCSTest.COLLECTION
		    + ".BIGCLOBO2(OUT B CLOB(" + LARGE_CLOB_SIZE + ")) LANGUAGE SQL "
		    + "SPECIFIC BIGCLOBO2 JDCSBBO2: BEGIN "
		    + "DECLARE C1 CURSOR FOR SELECT C1 FROM " + JDCSTest.COLLECTION
		    + ".BIGCLOBT;" + "OPEN C1;" + "FETCH C1 INTO B;" + "CLOSE C1;"
		    + "END JDCSBBO2";

		  stmt.executeUpdate(sql);
		  stmt.close(); 
		  sql = "{call " + JDCSTest.COLLECTION + ".BIGCLOBO2 (?)}";
		  CallableStatement cstmt = connection_.prepareCall(sql);
		  cstmt.registerOutParameter(1, java.sql.Types.CLOB);
		  cstmt.execute();

		  Reader check = (Reader) JDReflectionUtil.callMethod_O(cstmt,getMethodName,1);
		  cstmt.close(); 
		  assertCondition(compare(check, expected,sb),
				  "Testcase added 12/14/2005 by native driver "+sb);
	      } catch (Exception e) {
		  failed(e, "Unexpected Exception Last SQL was " + sql
			 + " Testcase added 12/14/2005 by native driver -- ");
	      } finally {
		  sql = "DROP TABLE " + JDCSTest.COLLECTION + ".BIGCLOBT";
		  try {
		      Statement stmt = connection_.createStatement();
		      stmt.executeUpdate(sql);
		      stmt.close(); 
		  } catch (Exception e) {
		      System.out.println("Warning.  Did not cleanup lob");
		      e.printStackTrace(System.out); 
		  }
	      }
          } else {
	      notApplicable("Toolbox can't handle very large clob -- java.lang.OutOfMemory error occurs"); 
	  }
      }

    }
  }

  /**
   * getCharacterStream () - Get a type that was registered as a BOOLEAN.
   **/
  public void Var058() {
    if (checkJdbc40 ()) {
    if (checkBooleanSupport()) {
      String info = " -- Test added 2021/01/06 for boolean";
      getTestSuccessful(csTypes_, getMethodName, 25, "1",info);
    }
    }
  }


  /**
   * getCharacterStream() - Get a type that was registered as a BOOLEAN that
   * returns false.
   **/
  public void Var059() {
    if (checkJdbc40()) {
      if (checkBooleanSupport()) {

        String info = " -- Test added 2021/01/06 for boolean";
        getTestSuccessful(csTypesB_, getMethodName, 25, "0", info);
      }
    }
  }
  
  /**
   * getLong() - Get a type that was registered as a BOOLEAN that returns
   * null.
   **/
  public void Var060() {
    if (checkJdbc40()) {
      if (checkBooleanSupport()) {
        String info = " -- Test added 2021/01/06 for boolean";
        getTestSuccessful(csTypesNull_, getMethodName, 25, "null", info);
      }
    }
  }



}
