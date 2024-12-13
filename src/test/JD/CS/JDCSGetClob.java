///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetClob.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetClob.java
//
// Classes:      JDCSGetClob
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDTestDriver;

import java.io.FileOutputStream;
import java.io.Reader;
import java.io.StringReader;

import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable;



/**
Testcase JDCSGetClob.  This tests the following
method of the JDBC CallableStatement class:

<ul>
<li>getClob()
</ul>
**/
public class JDCSGetClob
extends JDCSGetTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSGetClob";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCSTest.main(newArgs); 
   }


   // Note.. LARGECLOB_SIZE must be a multiple of 200
   private static int  LARGE_CLOB_SIZE = 20000000;
// Private data.

   private Connection          connection2_;
   private CallableStatement   csTypes2_;
  private boolean RUNTOOLONG = false;



/**
Constructor.
**/
   public JDCSGetClob (AS400 systemObject,
                       Hashtable namesAndVars,
                       int runMode,
                       FileOutputStream fileOutputStream,
                       
                       String password)
   {
      super (systemObject, "JDCSGetClob",
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
     super.setup("lob threshold=30000"); 
      connection2_ = testDriver_.getConnection (baseURL_
                                                + ";errors=full;lob threshold=0", userId_, encryptedPassword_);
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
getClob() - Get parameter -1.
**/
   public void Var001()
   {
      if (checkJdbc20 ()) {
         try {
            csTypes_.execute ();
            Clob p = csTypes_.getClob (-1);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getClob() - Get parameter 0.
**/
   public void Var002()
   {
      if (checkJdbc20 ()) {
         try {
            Clob p = csTypes_.getClob (0);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getClob() - Use a parameter that is too big.
**/
   public void Var003()
   {
      if (checkJdbc20 ()) {
         try {
            Clob p = csTypes_.getClob (35);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getClob() - Get a parameter when there are no parameters.
**/
   public void Var004()
   {        
      if (checkJdbc20 ()) {
         // I created a whole new Connection object here to work
         // around a server bug.
         Connection c = null;
         try {
            c = testDriver_.getConnection (baseURL_
                                           + ";errors=full", userId_, encryptedPassword_);
            CallableStatement cs = c.prepareCall ("CALL "
                                                  + JDSetupProcedure.STP_CS0);
            cs.execute ();
            Clob p = cs.getClob (1);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         } finally {
            try {
               if (c != null) c.close ();
            } catch (SQLException e) {
               // Ignore.
            }
         }
      }
   }



/**
getClob() - Get a parameter that was not registered.
**/
   public void Var005()
   {
      if (checkJdbc20 ()) {
         try {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
            cs.execute ();
            Clob p = cs.getClob (12);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getClob() - Get a parameter when the statement has not been
executed.
**/
   public void Var006()
   {
      if (checkJdbc20 ()) {
         try {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
            cs.registerOutParameter (20, Types.CLOB);
            Clob p = cs.getClob (20);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getClob() - Get a parameter when the statement is closed.
**/
   public void Var007()
   {
      if (checkJdbc20 ()) {
         try {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
            cs.registerOutParameter (20, Types.CLOB);
            cs.execute ();
            cs.close ();
            Clob p = cs.getClob (20);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getClob() - Get an IN parameter that was correctly registered.
**/
   public void Var008()
   {
      if (checkJdbc20 ()) {
         try {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESIN, supportedFeatures_);
            JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESIN,
                                                 supportedFeatures_);
            cs.registerOutParameter (20, Types.CLOB);
            cs.execute ();
            Clob p = cs.getClob (20);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }


/**
getClob() - Get an INOUT parameter, where the OUT parameter is
longer than the IN parameter.
**/
   public void Var009()
   {
      if (checkJdbc20 ()) {
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
               Clob p = cs.getClob (20);
               assertCondition (compare(p, "Welcome"));
            } catch (Exception e)
            {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
getClob() - Get a type that was registered as a SMALLINT.
**/
   public void Var010()
   {
      if (checkJdbc20 ()) {
         try {
            Clob p = csTypes_.getClob (1);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getClob() - Get a type that was registered as an INTEGER.
**/
   public void Var011()
   {
      if (checkJdbc20 ()) {
         try {
            Clob p = csTypes_.getClob (2);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getClob() - Get a type that was registered as an REAL.
**/
   public void Var012()
   {
      if (checkJdbc20 ()) {
         try {
            Clob p = csTypes_.getClob (3);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getClob() - Get a type that was registered as an FLOAT.
**/
   public void Var013()
   {
      if (checkJdbc20 ()) {
         try {
            Clob p = csTypes_.getClob (4);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getClob() - Get a type that was registered as an DOUBLE.
**/
   public void Var014()
   {
      if (checkJdbc20 ()) {
         try {
            Clob p = csTypes_.getClob (5);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getClob() - Get a type that was registered as an DECIMAL.
**/
   public void Var015()
   {
      if (checkJdbc20 ()) {
         try {
            Clob p = csTypes_.getClob (6);
            failed ("Didn't throw SQLException  p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getClob() - Get a type that was registered as an NUMERIC.
**/
   public void Var016()
   {
      if (checkJdbc20 ()) {
         try {
            Clob p = csTypes_.getClob (8);
            failed ("Didn't throw SQLException  p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getClob() - Get a type that was registered as a CHAR.
**/
   public void Var017()
   {
      if (checkJdbc20 ()) {
         try {
            Clob p = csTypes_.getClob (11);
            failed ("Didn't throw SQLException  p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getClob() - Get a type that was registered as a VARCHAR.
**/
   public void Var018()
   {
      if (checkJdbc20 ()) {
         try {
            Clob p = csTypes_.getClob (12);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getClob() - Get a type that was registered as a BINARY.
**/
   public void Var019()
   {
      if (checkJdbc20 ()) {
         try {
            Clob p = csTypes_.getClob (13);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getClob() - Get a type that was registered as a VARBINARY.
**/
   public void Var020()
   {
      if (checkJdbc20 ()) {
         try {
            Clob p = csTypes_.getClob (14);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getClob() - Get a type that was registered as a DATE.
**/
   public void Var021()
   {
      if (checkJdbc20 ()) {
         try {
            Clob p = csTypes_.getClob (15);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getClob() - Get a type that was registered as a TIME.
**/
   public void Var022()
   {
      if (checkJdbc20 ()) {
         try {
            Clob p = csTypes_.getClob (16);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getClob() - Get a type that was registered as a TIMESTAMP.
**/
   public void Var023()
   {
      if (checkJdbc20 ()) {
         try {
            Clob p = csTypes_.getClob (17);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getClob() - Get a type that was registered as an OTHER.
**/
   public void Var024()
   {
      if (checkLobSupport ()) {
         if (checkJdbc20 ()) {
            try {
               Clob p = csTypes_.getClob (18);
               failed ("Didn't throw SQLException p="+p);
            } catch (Exception e) {
               assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
         }
      }
   }



/**
getClob() - Get a type that was registered as a BLOB.
**/
   public void Var025()
   {
      if (checkLobSupport ()) {
         if (checkJdbc20 ()) {
            try {
               Clob p = csTypes_.getClob (19);
               failed ("Didn't throw SQLException p="+p);
            } catch (Exception e) {
               assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
         }
      }
   }



/**
getClob() - Get a type that was registered as a CLOB when the data
was returned in the result set.
**/
   public void Var026()
   {
      if (checkLobSupport ()) {
         if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
               try {
                  Clob p = csTypes_.getClob (20);
                  assertCondition (compare (p, "Chris Smyth"));
               } catch (Exception e) {
                  failed (e, "Unexpected Exception");
               }
            }
         }
      }
   }



/**
getClob() - Get a type that was registered as a CLOB when the locator
was returned in the result set.

SQL400 - let this work.
**/
   public void Var027()
   {
      if (checkLobSupport ()) {
         if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
              try {
                csTypes2_.execute(); 
                 Clob p = csTypes2_.getClob (20);
                 assertCondition (compare (p, "Chris Smyth"));
              } catch (Exception e) {
                 failed (e, "Unexpected Exception");
              }
            }
         }
      }
   }



/**
getClob() - Get a type that was registered as a CLOB when the data
was returned in the result set and is a DBCLOB.
**/
   public void Var028()
   {
      if (checkLobSupport ()) {
         if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
               try {
                  Clob p = csTypes_.getClob (21);
                  assertCondition (compare (p, "Jeff Lex"));
               } catch (Exception e) {
                  failed (e, "Unexpected Exception");
               }
            }
         }
      }
   }



/**
getClob() - Get a type that was registered as a CLOB when the locator
was returned in the result set and is a DBCLOB.

SQL400 - let this work.
**/
   public void Var029()
   {
      if (checkLobSupport ()) {
         if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
              try {
                 Clob p = csTypes2_.getClob (21);
                 assertCondition (compare (p, "Jeff Lex"));
              } catch (Exception e) {
                 failed (e, "Unexpected Exception");
              }
            }
         }
      }
   }



/**
getClob() - Get a type that was registered as a BIGINT.
**/
   public void Var030()
   {
      if (checkJdbc20 ()) {
         if (checkBigintSupport()) {
            try {
               Clob p = csTypes_.getClob (22);
               failed ("Didn't throw SQLException p="+p);
            } catch (Exception e) {
               assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
         }
      }
   }



/**
getClob() - Get an INOUT parameter, where the OUT parameter is
longer than the IN parameter, when the output parameter is registered first.

SQL400 - We added this testcase because of a customer bug.
**/
   public void Var031()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                                JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_);
               JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                          supportedFeatures_, getDriver());
               cs.registerOutParameter (20, Types.CLOB);
               JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                                    supportedFeatures_);
               cs.execute ();
               Clob p = cs.getClob (20);
               assertCondition (compare(p, "Welcome"));
            } catch (Exception e)
            {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }
// NERYKAL - named parameteR
/**
getClob() - Get a type that was registered as a SMALLINT.
**/
   public void Var032()
   {
      if(checkNamedParametersSupport()) {
         try {
            Clob p = csTypes_.getClob ("P_SMALLINT");
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**
getClob() - Get a type that was registered as an INTEGER.
**/
   public void Var033()
   {
      if(checkNamedParametersSupport()) {
         try {
            Clob p = csTypes_.getClob ("P_INTERGER");
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**
getClob() - Get a type that was registered as an REAL.
**/
   public void Var034()
   {
      if(checkNamedParametersSupport()) {
         try {
            Clob p = csTypes_.getClob ("P_REAL");
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**
getClob() - Get a type that was registered as an FLOAT.
**/
   public void Var035()
   {
      if(checkNamedParametersSupport()) {
         try {
            Clob p = csTypes_.getClob ("P_FLOAT");
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**
getClob() - Get a type that was registered as an DOUBLE.
**/
   public void Var036()
   {
      if(checkNamedParametersSupport()) {
         try {
            Clob p = csTypes_.getClob ("P_DOUBLE");
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**
getClob() - Get a type that was registered as an DECIMAL.
**/
   public void Var037()
   {
      if(checkNamedParametersSupport()) {
         try {
            Clob p = csTypes_.getClob ("P_DECIMAL_50");
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**
getClob() - Get a type that was registered as an DECIMAL.
**/
   public void Var038()
   {
      if(checkNamedParametersSupport()) {
         try {
            Clob p = csTypes_.getClob ("P_DECIMAL_105");
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**
getClob() - Get a type that was registered as an NUMERIC.
**/
   public void Var039()
   {
      if(checkNamedParametersSupport()) {
         try {
            Clob p = csTypes_.getClob ("P_NUMERIC_50");
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**
getClob() - Get a type that was registered as an NUMERIC.
**/
   public void Var040()
   {
      if(checkNamedParametersSupport()){
         try {
            Clob p = csTypes_.getClob ("P_NUMERIC_105");
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**
getClob() - Get a type that was registered as a CHAR.
**/
   public void Var041()
   {
      if(checkNamedParametersSupport()) {
         try {
            Clob p = csTypes_.getClob ("P_CHAR_1");
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**
getClob() - Get a type that was registered as a CHAR.
**/
   public void Var042()
   {
      if(checkNamedParametersSupport()) {
         try {
            Clob p = csTypes_.getClob ("P_CHAR_50");
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**
getClob() - Get a type that was registered as a VARCHAR.
**/
   public void Var043()
   {
      if(checkNamedParametersSupport()) {
         try {
            Clob p = csTypes_.getClob ("P_VARCHAR_50");
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**
getClob() - Get a type that was registered as a BINARY.
**/
   public void Var044()
   {
      if(checkNamedParametersSupport()) {
         try {
            Clob p = csTypes_.getClob ("P_BINARY_20");
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**
getClob() - Get a type that was registered as a VARBINARY.
**/
   public void Var045()
   {
      if(checkNamedParametersSupport()) {
         try {
            Clob p = csTypes_.getClob ("P_VARBINARY_20");
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**
getClob() - Get a type that was registered as a DATE.
**/
   public void Var046()
   {
      if(checkNamedParametersSupport()) {
         try {
            Clob p = csTypes_.getClob ("P_DATE");
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**
getClob() - Get a type that was registered as a TIME.
**/
   public void Var047()
   {
      if(checkNamedParametersSupport()) {
         try {
            Clob p = csTypes_.getClob ("P_TIME");
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**
getClob() - Get a type that was registered as a TIMESTAMP.
**/
   public void Var048()
   {
      if(checkNamedParametersSupport()) {
         try {
            Clob p = csTypes_.getClob ("P_TIMESTAMP");
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**
getClob() - Get a type that was registered as an OTHER.
**/
   public void Var049()
   {
      if(checkNamedParametersSupport()) {
         if (checkJdbc20 ()) {
            try {
               Clob p = csTypes_.getClob ("P_DATALINK");
               failed ("Didn't throw SQLException p="+p);
            } catch (Exception e) {
               assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
         }
      }
   }

/**
getClob() - Get a type that was registered as a BLOB.
**/
   public void Var050()
   {
      if(checkNamedParametersSupport()) {
         if (checkJdbc20 ()) {
            try {
               Clob p = csTypes_.getClob ("P_BLOB");
               failed ("Didn't throw SQLException p="+p);
            } catch (Exception e) {
               assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
         }
      }
   }

/**
getClob() - Get a type that was registered as a CLOB when the data
was returned in the result set.
**/
   public void Var051()
   {
      if(checkNamedParametersSupport()) {
         if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
               try {
                  Clob p = csTypes_.getClob ("P_CLOB");
                  assertCondition (compare (p, "Chris Smyth"));
               } catch (Exception e) {
                  failed (e, "Unexpected Exception");
               }
            }
         }
      }
   }

/**
getClob() - Get a type that was registered as a CLOB when the locator
was returned in the result set.

SQL400 - let this work.
**/
   public void Var052()
   {
      if(checkNamedParametersSupport()) {
         if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
              try {
                 Clob p = csTypes2_.getClob ("P_CLOB");
                 assertCondition (compare (p, "Chris Smyth"));
              } catch (Exception e) {
                 failed (e, "Unexpected Exception");
              }
            }
         }
      }
   }

/**
getClob() - Get a type that was registered as a CLOB when the data
was returned in the result set and is a DBCLOB.
**/
   public void Var053()
   {
     if(checkNamedParametersSupport()) {
         if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
               try {
                  Clob p = csTypes_.getClob ("P_DBCLOB");
                  assertCondition (compare (p, "Jeff Lex"));
               } catch (Exception e) {
                  failed (e, "Unexpected Exception");
               }
            }
         }
      }
   }

/**
getClob() - Get a type that was registered as a CLOB when the locator
was returned in the result set and is a DBCLOB.

SQL400 - let this work.
**/
   public void Var054()
   {
      if(checkNamedParametersSupport()) {
         if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
              try {
                 Clob p = csTypes2_.getClob ("P_DBCLOB");
                 assertCondition (compare (p, "Jeff Lex"));
              } catch (Exception e) {
                 failed (e, "Unexpected Exception");
              }
            }
         }
      }
   }

/**
getClob() - Get a type that was registered as a BIGINT.
**/
   public void Var055()
   {
      if(checkNamedParametersSupport()) {
         if (checkBigintSupport()) {
            try {
               Clob p = csTypes_.getClob ("P_BIGINT");
               failed ("Didn't throw SQLException p="+p);
            } catch (Exception e) {
               assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
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
	  if (! RUNTOOLONG ) {
	      notApplicable("Takes too long to run NEED TO FIX!!!!.  This exhausts system storage.  See issue 49443 ");
	  } else { 
	      if (checkJdbc20 ()) {
		  String sql ="";
		  try { 
		      sql = "CREATE OR REPLACE PROCEDURE "+JDCSTest.COLLECTION+".BIGCLOBOUT(OUT B CLOB("+LARGE_CLOB_SIZE+")) LANGUAGE SQL " +
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
		        	    
		      stmt.executeUpdate(sql);
		      sql = "{call "+JDCSTest.COLLECTION+".BIGCLOBOUT (?)}"; 
		      CallableStatement cstmt = connection_.prepareCall(sql);
		      cstmt.registerOutParameter(1, java.sql.Types.CLOB);

		 //
		 // Set the timeout for building the clob
		 // 
		      cstmt.setQueryTimeout(60);

		      cstmt.execute();

		      StringBuffer sb = new StringBuffer(); 

		      for (int i = 0; i < LARGE_CLOB_SIZE; i++) {
			  sb.append('A'); 
		      }
		      String expected = sb.toString(); 

		      Clob check = cstmt.getClob(1);
		      assertCondition (compare(check, expected), "Testcase added 12/14/2005 by native driver");
		  } catch (Exception e) {
		      failed (e, "Unexpected Exception Last SQL was "+sql+" Testcase added 12/14/2005 by native driver -- "+
			      " Currrently fails -- see issue 29243 ");
		  } 
	      }
	  }
      }

      /**
       * getCLOB() - Get CLOB on a large CLOB
       */
  public void Var057() {
    if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
      notApplicable("V5R3 or later test");
    } else {
      if (checkJdbc20()) {
	  if (getDriver()  == JDTestDriver.DRIVER_NATIVE) { /* native only large clob test */ 
	      String sql = "";
	      try {
		  StringBuffer sb = new StringBuffer();

		  for (int i = 0; i < LARGE_CLOB_SIZE; i++) {
		      sb.append('A');
		  }
		  String expected = sb.toString();
		  String quarter = sb.substring(0, LARGE_CLOB_SIZE / 4);
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

		  Clob check = cstmt.getClob(1);
		  cstmt.close(); 
		  assertCondition(compare(check, expected),
				  "Testcase added 12/14/2005 by native driver");
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

 public void Var058() {
    // NOTE: Do not need to check for JDBC40 because the drivers
    // provide the method in the JDBC30 version also

    StringBuffer sb = new StringBuffer();
    sb.append(" -- Added 03/09/2017 for jt400 bug#385\n");
    String sql;
    try {
      String string = "ABCDE"; 
      String expectedString = string+string;


      boolean passed = true;
      Statement stmt = connection_.createStatement();
      String procedureName = JDCSTest.COLLECTION + ".JDCLOBIO";
      try {
        sql = "DROP PROCEDURE " + procedureName;
        sb.append("Executing " + sql + "\n");
        stmt.executeUpdate(sql);
      } catch (Exception e) {
        printStackTraceToStringBuffer(e, sb);
      }
      sql = "CREATE  PROCEDURE " + procedureName
          + "(INOUT P1 CLOB(1M)) LANGUAGE SQL BEGIN SET P1 = P1 || P1; END";
      sb.append("Executing " + sql + "\n");
      stmt.executeUpdate(sql);

      sql = "CALL " + procedureName + "(?)";
      sb.append("Preparing " + sql + "\n");
      CallableStatement cstmt = connection_.prepareCall(sql);
      cstmt.registerOutParameter(1, Types.CLOB);
      Reader reader = new StringReader(string);
      // Method only available on Java 1.6
      JDReflectionUtil.callMethod_V(cstmt, "setClob", 1, reader, (long) string.length());

      cstmt.execute();
      Clob clob = cstmt.getClob(1);
      passed = compare(clob.getCharacterStream(), expectedString, sb);

      sql = "DROP PROCEDURE " + procedureName;
      sb.append("Executing " + sql + "\n");
      stmt.executeUpdate(sql);

      stmt.close();
      cstmt.close();
      assertCondition(passed,sb);
    } catch (Exception e) {
      failed(e, "Unexpected Exception " + sb.toString());
    }
  }


 public void Var059() {
   // NOTE: Do not need to check for JDBC40 because the drivers
   // provide the method in the JDBC30 version also

   StringBuffer sb = new StringBuffer();
   sb.append(" -- Added 03/09/2017 for jt400 bug#385\n");
   String sql;
   try {
     String string = "ABCDE"; 
     String expectedString = string+string;


     boolean passed = true;
     Statement stmt = connection_.createStatement();
     String procedureName = JDCSTest.COLLECTION + ".JDDBCLOBIO";
     try {
       sql = "DROP PROCEDURE " + procedureName;
       sb.append("Executing " + sql + "\n");
       stmt.executeUpdate(sql);
     } catch (Exception e) {
       printStackTraceToStringBuffer(e, sb);
     }
     sql = "CREATE  PROCEDURE " + procedureName
         + "(INOUT P1 DBCLOB(1M) CCSID 13488) LANGUAGE SQL BEGIN SET P1 = P1 || P1; END";
     sb.append("Executing " + sql + "\n");
     stmt.executeUpdate(sql);

     sql = "CALL " + procedureName + "(?)";
     sb.append("Preparing " + sql + "\n");
     CallableStatement cstmt = connection_.prepareCall(sql);
     cstmt.registerOutParameter(1, Types.CLOB);
     Reader reader = new StringReader(string);
     // Method only available on Java 1.6
     JDReflectionUtil.callMethod_V(cstmt, "setClob", 1, reader, (long) string.length());

     cstmt.execute();
     Clob clob = cstmt.getClob(1);
     passed = compare(clob.getCharacterStream(), expectedString, sb);

     sql = "DROP PROCEDURE " + procedureName;
     sb.append("Executing " + sql + "\n");
     stmt.executeUpdate(sql);

     stmt.close();
     cstmt.close();
     assertCondition(passed,sb);
   } catch (Exception e) {
     failed(e, "Unexpected Exception " + sb.toString());
   }
 }


 public void Var060() {
   // NOTE: Do not need to check for JDBC40 because the drivers
   // provide the method in the JDBC30 version also

   StringBuffer sb = new StringBuffer();
   sb.append(" -- Added 03/09/2017 for jt400 bug#385\n");
   String sql;
   try {
     String string = "ABCDE"; 
     String expectedString = string+string;


     boolean passed = true;
     Statement stmt = connection_.createStatement();
     String procedureName = JDCSTest.COLLECTION + ".JDNCLOBIO";
     try {
       sql = "DROP PROCEDURE " + procedureName;
       sb.append("Executing " + sql + "\n");
       stmt.executeUpdate(sql);
     } catch (Exception e) {
       printStackTraceToStringBuffer(e, sb);
     }
     sql = "CREATE  PROCEDURE " + procedureName
         + "(INOUT P1 NCLOB(1M) ) LANGUAGE SQL BEGIN SET P1 = P1 || P1; END";
     sb.append("Executing " + sql + "\n");
     stmt.executeUpdate(sql);

     sql = "CALL " + procedureName + "(?)";
     sb.append("Preparing " + sql + "\n");
     CallableStatement cstmt = connection_.prepareCall(sql);
     cstmt.registerOutParameter(1, Types.CLOB);
     Reader reader = new StringReader(string);
     // Method only available on Java 1.6
     JDReflectionUtil.callMethod_V(cstmt, "setClob", 1, reader, (long) string.length());

     cstmt.execute();
     Clob clob = cstmt.getClob(1);
     sb.append("Doing comparision\n"); 
     passed = compare(clob.getCharacterStream(), expectedString, sb);
     sb.append("Comparision complete\n"); 
     
     sql = "DROP PROCEDURE " + procedureName;
     sb.append("Executing " + sql + "\n");
     stmt.executeUpdate(sql);

     stmt.close();
     cstmt.close();
     assertCondition(passed,sb);
   } catch (Exception e) {
     failed(e, "Unexpected Exception " + sb.toString());
   }
 }

 /**
 getClob() - Get a type that was registered as a BOOLEAN.
 **/
    
    public void Var061()
    {
       if (checkBooleanSupport()) {
         getTestFailed(csTypes_,"getClob",25,"Data type mismatch",""); 
        
       }
    }

    
    /**
    getClob() - Get a type that was registered as a BOOLEAN.
    **/
       
       public void Var062()
       {
          if (checkBooleanSupport()) {
            getTestFailed(csTypes_,"getClob","P_BOOLEAN","Data type mismatch",""); 
           
          }
       }




}
