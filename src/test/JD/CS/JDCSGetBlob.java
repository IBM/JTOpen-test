///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetBlob.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetBlob.java
//
// Classes:      JDCSGetBlob
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDTestDriver;

import java.awt.TextArea;
import java.io.FileOutputStream;
import java.io.ByteArrayInputStream; 
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable;



/**
Testcase JDCSGetBlob.  This tests the following
method of the JDBC CallableStatement class:

<ul>
<li>getBlob()
</ul>
**/
public class JDCSGetBlob
extends JDCSGetTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSGetBlob";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCSTest.main(newArgs); 
   }

   private static int LARGE_BLOB_SIZE=40000000;


   private Connection          connection2_;
   private CallableStatement   csTypes2_;


   static final byte[] expected = { (byte) 'D', (byte) 'a', (byte) 'v', (byte) 'e', (byte) ' ',
      (byte) 'E', (byte) 'g', (byte) 'l', (byte) 'e'};


/**
Constructor.
**/
   public JDCSGetBlob (AS400 systemObject,
                       Hashtable namesAndVars,
                       int runMode,
                       FileOutputStream fileOutputStream,
                       
                       String password)
   {
      super (systemObject, "JDCSGetBlob",
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
     setup("lob threshold=30000");
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
      connection2_.close ();
      connection_.close ();
   }

/**
dumpBytes() - Utility function used to see the bytes
**/
    public static String dumpBytes(byte[] b){
	String s = "";
	for(int i = 0; i < b.length; i++){
	    String ns = Integer.toHexString(((int) b[i]) & 0xFF);
	    if(ns.length() == 1){
		s += "0" + ns;
	    }
	    else{
		s += ns;
	    }
	}
	return s;
    }



/**
Compares a Blob with a byte[].
**/
   static boolean compare (Blob i, byte[] b)
   throws SQLException
   {
      byte[] iBytes = i.getBytes (1, (int) i.length ());  // @B1C
      boolean result =  areEqual (iBytes, b);
      if (!result) {
	  System.out.println("     BLOB("+dumpBytes(iBytes)+") != Bytes("+dumpBytes(b)+")");
      }
      return result;
   }






/**
getBlob() - Get parameter -1.
**/
   public void Var001()
   {
      if (checkJdbc20 ()) {
         try {
            csTypes_.execute ();
            Blob p = csTypes_.getBlob (-1);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getBlob() - Get parameter 0.
**/
   public void Var002()
   {
      if (checkJdbc20 ()) {
         try {
            Blob p = csTypes_.getBlob (0);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getBlob() - Use a parameter that is too big.
**/
   public void Var003()
   {
      if (checkJdbc20 ()) {
         try {
            Blob p = csTypes_.getBlob (35);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getBlob() - Get a parameter when there are no parameters.
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
            Blob p = cs.getBlob (1);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }

         if (c != null) try { c.close(); } catch (Exception e) {}
      }
   }



/**
getBlob() - Get a parameter that was not registered.
**/
   public void Var005()
   {
      if (checkJdbc20 ()) {
         try {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
            cs.execute ();
            Blob p = cs.getBlob (19);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getBlob() - Get a parameter when the statement has not been
executed.
**/
   public void Var006()
   {
      if (checkJdbc20 ()) {
         try {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
            cs.registerOutParameter (19, Types.BLOB);
            Blob p = cs.getBlob (19);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getBlob() - Get a parameter when the statement is closed.
**/
   public void Var007()
   {
      if (checkJdbc20 ()) {
         try {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
            cs.registerOutParameter (19, Types.BLOB);
            cs.execute ();
            cs.close ();
            Blob p = cs.getBlob (19);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getBlob() - Get an IN parameter that was correctly registered.
**/
   public void Var008()
   {
      if (checkJdbc20 ()) {
         try {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESIN, supportedFeatures_);
            JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESIN,
                                                 supportedFeatures_);
            cs.registerOutParameter (19, Types.BLOB);
            cs.execute ();
            Blob p = cs.getBlob (19);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }


/**
getBlob() - Get an INOUT parameter, where the OUT parameter is
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
               cs.registerOutParameter (19, Types.BLOB);
               cs.execute ();
               Blob p = cs.getBlob (19);
               byte[] check = new byte[] { (byte)0xC8, (byte)0x85, (byte)0x93, (byte)0x93,
                  (byte)0x96, (byte)0x40, (byte)0xA3, (byte)0x88,
                  (byte)0x85, (byte)0x99, (byte)0x85}; // "Hello there"
               assertCondition (areEqual (p.getBytes (1, (int) p.length ()), check));    // @B1C
            } catch (Exception e)
            {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
getBlob() - Get a type that was registered as a SMALLINT.
**/
   public void Var010()
   {
      if (checkJdbc20 ()) {
         try {
            Blob p = csTypes_.getBlob (1);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getBlob() - Get a type that was registered as an INTEGER.
**/
   public void Var011()
   {
      if (checkJdbc20 ()) {
         try {
            Blob p = csTypes_.getBlob (2);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getBlob() - Get a type that was registered as an REAL.
**/
   public void Var012()
   {
      if (checkJdbc20 ()) {
         try {
            Blob p = csTypes_.getBlob (3);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getBlob() - Get a type that was registered as an FLOAT.
**/
   public void Var013()
   {
      if (checkJdbc20 ()) {
         try {
            Blob p = csTypes_.getBlob (4);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getBlob() - Get a type that was registered as an DOUBLE.
**/
   public void Var014()
   {
      if (checkJdbc20 ()) {
         try {
            Blob p = csTypes_.getBlob (5);
            failed ("Didn't throw SQLException  p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getBlob() - Get a type that was registered as an DECIMAL.
**/
   public void Var015()
   {
      if (checkJdbc20 ()) {
         try {
            Blob p = csTypes_.getBlob (6);
            failed ("Didn't throw SQLException  p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getBlob() - Get a type that was registered as an NUMERIC.
**/
   public void Var016()
   {
      if (checkJdbc20 ()) {
         try {
            Blob p = csTypes_.getBlob (8);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getBlob() - Get a type that was registered as a CHAR.
**/
   public void Var017()
   {
      if (checkJdbc20 ()) {
         try {
            Blob p = csTypes_.getBlob (11);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getBlob() - Get a type that was registered as a VARCHAR.
**/
   public void Var018()
   {
      if (checkJdbc20 ()) {
         try {
            Blob p = csTypes_.getBlob (12);
            failed ("Didn't throw SQLException  p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getBlob() - Get a type that was registered as a BINARY.
**/
   public void Var019()
   {
      if (checkJdbc20 ()) {
         try {
            Blob p = csTypes_.getBlob (13);
            failed ("Didn't throw SQLException  p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getBlob() - Get a type that was registered as a VARBINARY.
**/
   public void Var020()
   {
      if (checkJdbc20 ()) {
         try {
            Blob p = csTypes_.getBlob (14);
            failed ("Didn't throw SQLException   p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }


/**
getBlob() - Get a type that was registered as a DATE.
**/
   public void Var021()
   {
      if (checkJdbc20 ()) {
         try {
            Blob p = csTypes_.getBlob (15);
            failed ("Didn't throw SQLException  p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getBlob() - Get a type that was registered as a TIME.
**/
   public void Var022()
   {
      if (checkJdbc20 ()) {
         try {
            Blob p = csTypes_.getBlob (16);
            failed ("Didn't throw SQLException  p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getBlob() - Get a type that was registered as a TIMESTAMP.
**/
   public void Var023()
   {
      if (checkJdbc20 ()) {
         try {
            Blob p = csTypes_.getBlob (17);
            failed ("Didn't throw SQLException  p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getBlob() - Get a type that was registered as an OTHER.
**/
   public void Var024()
   {
      if (checkJdbc20 ()) {
         try {
            Blob p = csTypes_.getBlob (18);
            failed ("Didn't throw SQLException  p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getBlob() - Get a type that was registered as a BLOB, when
the data was returned in the result set.
**/
   public void Var025()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               Blob p = csTypes_.getBlob (19);
               assertCondition (compare(p, expected));
            } catch (Exception e)
            {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }



/**
getBlob() - Get a type that was registered as a BLOB, when
the locator was returned in the result set.
**/
   public void Var026()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               Blob p = csTypes2_.getBlob (19);
               assertCondition (compare(p, expected));
            } catch (Exception e)
            {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }



/**
getBlob() - Get a type that was registered as a CLOB.
**/
   public void Var027()
   {
      if (checkJdbc20 ()) {
         try {
            Blob p = csTypes_.getBlob (20);
            failed ("Didn't throw SQLException  p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getBlob() - Get a type that was registered as a BIGINT.
**/
   public void Var028()
   {
      if (checkJdbc20 ()) {
         if (checkBigintSupport()) {
            try {
               Blob p = csTypes_.getBlob (22);
               failed ("Didn't throw SQLException p="+p);
            } catch (Exception e) {
               assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
         }
      }
   }



/**
getBlob() - Get an INOUT parameter, where the OUT parameter is
longer than the IN parameter, when the output parameter is registered first.

SQL400 - We added this testcase because of a customer bug.
**/
   public void Var029()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                                JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_);
               JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                          supportedFeatures_,getDriver());
               cs.registerOutParameter (19,  Types.BLOB);
               JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                                    supportedFeatures_);
               cs.execute ();
               Blob p = cs.getBlob (19);
               byte[] check = new byte[] { (byte)0xC8, (byte)0x85, (byte)0x93, (byte)0x93,
                  (byte)0x96, (byte)0x40, (byte)0xA3, (byte)0x88,
                  (byte)0x85, (byte)0x99, (byte)0x85}; // "Hello there"
               assertCondition (areEqual (p.getBytes (1, (int) p.length ()), check));
            } catch (Exception e)
            {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }

// Named Parameter
/**
getBlob() - Get a NAMED PARAMETER THAT DOES NOT EXIST
**/
   public void Var030()
   {
       if (checkNamedParametersSupport()) {
	   if (checkJdbc20 ()) {
	       try {
		   Blob p = csTypes_.getBlob ("P_FAKE");
		   failed ("Didn't throw SQLException p="+p);
	       } catch (Exception e) {
		   assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	       }
	   }
       }
   }
/**
getBlob() - Get a type that was registered as a SMALLINT.
**/
   public void Var031()
   {
       if (checkNamedParametersSupport()) {
	   if (checkJdbc20 ()) {
	       try {
		   Blob p = csTypes_.getBlob ("P_SMALLINT");
		   failed ("Didn't throw SQLException p="+p);
	       } catch (Exception e) {
		   assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	       }
	   }
       }
   }


/**
getBlob() - Get a type that was registered as an INTEGER.
**/
   public void Var032()
   {
       if (checkNamedParametersSupport()) {
	   if (checkJdbc20 ()) {
	       try {
		   Blob p = csTypes_.getBlob ("P_INTEGER");
		   failed ("Didn't throw SQLException p="+p);
	       } catch (Exception e) {
		   assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	       }
	   }
       }
   }


/**
getBlob() - Get a type that was registered as an REAL.
**/
   public void Var033()
   {
       if (checkNamedParametersSupport()) {
	   if (checkJdbc20 ()) {
	       try {
		   Blob p = csTypes_.getBlob ("P_REAL");
		   failed ("Didn't throw SQLException p="+p);
	       } catch (Exception e) {
		   assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	       }
	   }
       }
   }

/**
getBlob() - Get a type that was registered as an FLOAT.
**/
   public void Var034()
   {
       if (checkNamedParametersSupport()) {
	   if (checkJdbc20 ()) {
	       try {
		   Blob p = csTypes_.getBlob ("P_FLOAT");
		   failed ("Didn't throw SQLException p="+p);
	       } catch (Exception e) {
		   assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	       }
	   }
       }
   }


/**
getBlob() - Get a type that was registered as an DOUBLE.
**/
   public void Var035()
   {
       if (checkNamedParametersSupport()) {
	   if (checkJdbc20 ()) {
	       try {
		   Blob p = csTypes_.getBlob ("P_DOUBLE");
		   failed ("Didn't throw SQLException p="+p);
	       } catch (Exception e) {
		   assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	       }
	   }
       }
   }


/**
getBlob() - Get a type that was registered as an DECIMAL.
**/
   public void Var036()
   {
       if (checkNamedParametersSupport()) {
	   if (checkJdbc20 ()) {
	       try {
		   Blob p = csTypes_.getBlob ("P_DECIMAL_50");
		   failed ("Didn't throw SQLException p="+p);
	       } catch (Exception e) {
		   assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	       }
	   }
       }
   }


/**
getBlob() - Get a type that was registered as an NUMERIC.
**/
   public void Var037()
   {
       if (checkNamedParametersSupport()) {
	   if (checkJdbc20 ()) {
	       try {
		   Blob p = csTypes_.getBlob ("P_NUMERIC_50");
		   failed ("Didn't throw SQLException p="+p);
	       } catch (Exception e) {
		   assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	       }
	   }
       }
   }


/**
getBlob() - Get a type that was registered as a CHAR.
**/
   public void Var038()
   {
       if (checkNamedParametersSupport()) {
	   if (checkJdbc20 ()) {
	       try {
		   Blob p = csTypes_.getBlob ("P_CHAR_50");
		   failed ("Didn't throw SQLException p="+p);
	       } catch (Exception e) {
		   assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	       }
	   }
       }
   }


/**
getBlob() - Get a type that was registered as a VARCHAR.
**/
   public void Var039()
   {
       if (checkNamedParametersSupport()) {
	   if (checkJdbc20 ()) {
	       try {
		   Blob p = csTypes_.getBlob ("P_VARCHAR_50");
		   failed ("Didn't throw SQLException p="+p);
	       } catch (Exception e) {
		   assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	       }
	   }
       }
   }


/**
getBlob() - Get a type that was registered as a BINARY.
**/
   public void Var040()
   {
       if (checkNamedParametersSupport()) {
	   if (checkJdbc20 ()) {
	       try {
		   Blob p = csTypes_.getBlob ("P_BINARY_20");
		   failed ("Didn't throw SQLException p="+p);
	       } catch (Exception e) {
		   assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	       }
	   }
       }
   }


/**
getBlob() - Get a type that was registered as a VARBINARY.
**/
   public void Var041()
   {
       if (checkNamedParametersSupport()) {
	   if (checkJdbc20 ()) {
	       try {
		   Blob p = csTypes_.getBlob ("P_VARBINARY");
		   failed ("Didn't throw SQLException p="+p);
	       } catch (Exception e) {
		   assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	       }
	   }
       }
   }

/**
getBlob() - Get a type that was registered as a DATE.
**/
   public void Var042()
   {
       if (checkNamedParametersSupport()) {
	   if (checkJdbc20 ()) {
	       try {
		   Blob p = csTypes_.getBlob ("P_DATE");
		   failed ("Didn't throw SQLException p="+p);
	       } catch (Exception e) {
		   assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	       }
	   }
       }
   }


/**
getBlob() - Get a type that was registered as a TIME.
**/
   public void Var043()
   {
       if (checkNamedParametersSupport()) {
	   if (checkJdbc20 ()) {
	       try {
		   Blob p = csTypes_.getBlob ("P_TIME");
		   failed ("Didn't throw SQLException p="+p);
	       } catch (Exception e) {
		   assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	       }
	   }
       }
   }


/**
getBlob() - Get a type that was registered as a TIMESTAMP.
**/
   public void Var044()
   {
       if (checkNamedParametersSupport()) {
	   if (checkJdbc20 ()) {
	       try {
		   Blob p = csTypes_.getBlob ("P_TIMESTAMP");
		   failed ("Didn't throw SQLException p="+p);
	       } catch (Exception e) {
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	       }
	   }
       }
   }


/**
getBlob() - Get a type that was registered as an OTHER.
**/
   public void Var045()
   {
       if (checkNamedParametersSupport()) {
	   if (checkJdbc20 ()) {
	       try {
		   Blob p = csTypes_.getBlob ("P_DATALINK");
		   failed ("Didn't throw SQLException p="+p);
	       } catch (Exception e) {
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	       }
	   }
       }
   }


/**
getBlob() - Get a type that was registered as a BLOB, when
the data was returned in the result set.
**/
   public void Var046()
   {
       if (checkNamedParametersSupport()) {
	   if (checkJdbc20 ()) {
	       if (checkLobSupport ()) {
		   try {
		       Blob p = csTypes_.getBlob ("P_BLOB");
		       assertCondition (compare(p, expected));
		   } catch (Exception e)
		   {
		       failed (e, "Unexpected Exception");
		   }
	       }
	   }
       }
   }


/**
getBlob() - Get a type that was registered as a BLOB, when
the locator was returned in the result set.
**/
   public void Var047()
   {
       if (checkNamedParametersSupport()) {
	   if (checkJdbc20 ()) {
	       if (checkLobSupport ()) {
		   try {
		       Blob p = csTypes2_.getBlob ("P_BLOB");
		       assertCondition (compare(p, expected));
		   } catch (Exception e)
		   {
		       failed (e, "Unexpected Exception");
		   }
	       }
	   }
       }
   }


/**
getBlob() - Get a type that was registered as a CLOB.
**/
   public void Var048()
   {
       if (checkNamedParametersSupport()) {
	   if (checkJdbc20 ()) {
	       try {
		   Blob p = csTypes_.getBlob ("P_CLOB");
		   failed ("Didn't throw SQLException p="+p);
	       } catch (Exception e) {
		   assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	       }
	   }
       }
   }


/**
getBlob() - Get a type that was registered as a BIGINT.
**/
   public void Var049()
   {
       if (checkNamedParametersSupport()) {
	   if (checkJdbc20 ()) {
	       if (checkBigintSupport()) {
		   try {
		       Blob p = csTypes_.getBlob ("P_BIGINT");
		       failed ("Didn't throw SQLException p="+p);
		   } catch (Exception e) {
		       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		   }
	       }
	   }
       }
   }



/**
getBlob() - Get blob on a large blob
**/
   public void Var050()
   {
       if (checkJdbc20 ()) {
	   if (getDriver()  == JDTestDriver.DRIVER_NATIVE) { /* native only large blob tc */

	       if ( getRelease() >=  JDTestDriver.RELEASE_V7R1M0 ) {
		   String sql ="";
		   try {
		       sql = "CREATE OR REPLACE  PROCEDURE "+JDCSTest.COLLECTION+".BIGBLOBOUT(OUT B BLOB(40000000)) LANGUAGE SQL " +
			 "SPECIFIC BIGLOBOUT JDCSBBOUT: BEGIN "+
			 "DECLARE DUMMY BLOB(40000000); "+
			 "DECLARE C INT DEFAULT 0; "+
			 " SET DUMMY=BLOB(X'01');"+
			 "BUILD_LOOP: "+
			 "LOOP "+
			 " SET C = C + 1; "+
			 " SET DUMMY = DUMMY CONCAT DUMMY;"+
			 " IF C = 25 THEN "+
			 "   LEAVE BUILD_LOOP;  "+
			 " END IF; "  +
			 "END LOOP ; "+
			 "SET B=DUMMY; "+
			 "END JDCSBBOUT" ;

		       Statement stmt = connection_.createStatement ();
		      
		       stmt.executeUpdate(sql);
		       sql = "{call "+JDCSTest.COLLECTION+".BIGBLOBOUT (?)}";
		       CallableStatement cstmt = connection_.prepareCall(sql);
		       cstmt.registerOutParameter(1, java.sql.Types.BLOB);
		       cstmt.execute();

		       // 2 ^^ 25 = 33554432
		       byte [] expected1 = new byte[33554432];
		       for (int i = 0; i < 33554432; i++) {
			   expected1[i] = 0x01;
		       }

		       Blob check = cstmt.getBlob(1);
		       assertCondition (compare(check, expected1), "Testcase added 12/14/2005 by native driver");
		   } catch (Exception e) {
		       failed (e, "Unexpected Exception Last SQL was "+sql+" Testcase added 12/14/2005 by native driver -- "+
			       " Currrently fails -- see issue 29243 ");
		   }

	       } else {
		   notApplicable("New V5R4 testcase.. fails for native in V5R3 with The maximum number of concurrent LOB locators has been reached");
	       }
	   } else {
	       notApplicable("Toolbox can't handle very large blob -- java.lang.OutOfMemory error occurs");
	   }

       }
   }

   /**
   getBlob() - Get blob on a large blob
   **/
      public void Var051()
      {
	  if (checkJdbc20 ()) {
	      if (getDriver()  == JDTestDriver.DRIVER_NATIVE) { /* native only large bloc tc */
		  String sql ="";
		  Statement stmt = null;
		  try {
		      byte[] expected1 = new byte[LARGE_BLOB_SIZE];
		      for (int i = 0; i < LARGE_BLOB_SIZE; i++) {
			  expected1[i] = 0x01;
		      }

		      stmt = connection_.createStatement ();

		      sql = "DROP TABLE "+JDCSTest.COLLECTION+".BIGBLOBT";
		      try {
			  stmt.executeUpdate(sql);
		      } catch (Exception e) {}

		      sql = "CREATE TABLE "+JDCSTest.COLLECTION+".BIGBLOBT (C1 BLOB("+LARGE_BLOB_SIZE+"))";
		      stmt.executeUpdate(sql);
		      sql = "INSERT INTO "+JDCSTest.COLLECTION+".BIGBLOBT VALUES(?)";
		      PreparedStatement pstmt = connection_.prepareStatement(sql);
		      pstmt.setBytes(1, expected1);
		      pstmt.executeUpdate();

		      sql = "CREATE OR REPLACE PROCEDURE "+JDCSTest.COLLECTION+".BIGBLOBO2(OUT B BLOB("+LARGE_BLOB_SIZE+")) LANGUAGE SQL " +
			"SPECIFIC BIGLOBO2 JDCSBBO2: BEGIN "+
			"DECLARE C1 CURSOR FOR SELECT C1 FROM "+JDCSTest.COLLECTION+".BIGBLOBT;"+
			"OPEN C1;"+
			"FETCH C1 INTO B;"+
			"CLOSE C1;"+
			"END JDCSBBO2" ;

		     
		      stmt.executeUpdate(sql);
		      sql = "{call "+JDCSTest.COLLECTION+".BIGBLOBO2 (?)}";
		      CallableStatement cstmt = connection_.prepareCall(sql);
		      cstmt.registerOutParameter(1, java.sql.Types.BLOB);
		      cstmt.execute();


		      Blob check = cstmt.getBlob(1);
		      assertCondition (compare(check, expected1), "Testcase added 12/14/2005 by native driver");
		  } catch (Exception e) {
		      failed (e, "Unexpected Exception Last SQL was "+sql+" Testcase added 12/14/2005 by native driver -- "
			      );
		  }


		  try {
		      sql = "DROP TABLE "+JDCSTest.COLLECTION+".BIGBLOBT";
		      if (stmt != null) stmt.executeUpdate(sql);
		  } catch (Exception e) {
		      // Just ignore
		  }



	      } else {
		  notApplicable("Toolbox can't handle very large blob -- java.lang.OutOfMemory error occurs");
	      }
	  }
      }


      /**
      getBlob() - Get a type that was registered as an DECFLOAT(16)
      **/
         public void Var052()
         {
            if (checkDecFloatSupport ()) {
               try {
                  Blob p = csTypes_.getBlob ("P_DECFLOAT16");
                  failed ("Didn't throw SQLException p="+p);
               } catch (Exception e) {
		   String expectedMessage = "Data type mismatch";
                   String m = e.getMessage();
                   assertCondition(m.indexOf(expectedMessage)>=0,
                       "expected '"+expectedMessage+"' got "+m+" Update 11/8/2011 for native");
               }
            }
         }


         /**
         getBlob() - Get a type that was registered as an DECFLOAT(34)
         **/
            public void Var053()
            {
               if (checkDecFloatSupport ()) {
                  try {
                     Blob p = csTypes_.getBlob ("P_DECFLOAT34");
                     failed ("Didn't throw SQLException p="+p);
                  } catch (Exception e) {
		      String expectedMessage = "Data type mismatch";
		      String m = e.getMessage();
		      assertCondition(m.indexOf(expectedMessage)>=0,
				      "expected '"+expectedMessage+"' got "+m+" Update 11/8/2011 for native");

                  }
               }
            }


	    /**
             * Call getBlob on different type of procedure declarations.
             * Used to detect problem encountered by CPS 8RHVUH
	     *
             * The procedure must have one blob output parameter
             *
	     * errors ignored on setupStatements
             * errors not ignored on cleanupStatements
             */


	    public void testGetBlob(String info,
				    String procedureName,
				    String procedureDefinition,
				    byte[] expectedValue,
				    int runSeconds,
				    String[] setupStatements,
				    String[] cleanupStatements) {

		boolean passed = true;
		String sql;
		StringBuffer sb = new StringBuffer();
		int callCount=0;
		try {
		//
		// The setup statements ignore any errors
		//
		    Statement stmt = connection_.createStatement();

		    if (setupStatements != null) {
			for (int i = 0; i < setupStatements.length; i++) {
			    sql = setupStatements[i];
			    sb.append("Executing: "+sql+"\n");
			    try {
				stmt.executeUpdate(sql);
			    } catch (Exception e) {
				sb.append("Ignored exception caught "+e.toString());
			    }
			}
		    }

		    try {
			sql = "drop procedure "+procedureName;
			sb.append("Executing: "+sql+"\n");
			stmt.executeUpdate(sql);
		    } catch (Exception e) {
			sb.append("Ignored exception caught "+e.toString());
		    }

		    sql = "create procedure "+procedureName+" " +procedureDefinition;
		    sb.append("Executing: "+sql+"\n");
		    stmt.executeUpdate(sql);

		    sql = "CALL "+procedureName+"(?)";
		    sb.append("Preparing: "+sql+"\n");
		    CallableStatement cstmt = connection_.prepareCall(sql);

		    cstmt.registerOutParameter(1, java.sql.Types.BLOB);
		    long currentTime=System.currentTimeMillis();
		    long endTime=currentTime+1+runSeconds*1000;
		    while (passed && currentTime < endTime) {
			sb.append("Calling prepared: "+sql+"\n");
			cstmt.execute();
			sb.append("Getting blob\n");
			Blob blob = cstmt.getBlob(1);

			passed = compare(blob, expectedValue, sb);
			currentTime=System.currentTimeMillis();
			callCount++;
		    }

		    if (cleanupStatements != null) {
			for (int i = 0; i < cleanupStatements.length; i++) {
			    sql = cleanupStatements[i];
			    sb.append("Executing: "+sql+"\n");
			    stmt.executeUpdate(sql);
			}
		    }

		    stmt.close();

		    if (callCount > 1) { System.out.println("Test made "+callCount+" calls in  "+runSeconds+" seconds."); }
		    assertCondition(passed, info+"\n"+sb.toString());

		} catch (Exception e) {
		    failed(e, "Unexpected exception "+info+"\n"+sb.toString());
		}

	    }



	    public void Var054() {
		byte[] expectedValue ={ (byte) 0xab, (byte) 0xcd, (byte) 0xef};
		testGetBlob("Call procedure that returns BLOB",
			    JDCSTest.COLLECTION+".JDCSGB54",
			       "(OUT OUTBLOB BLOB(4186000)) LANGUAGE SQL BEGIN  SET OUTBLOB=BLOB(X'abcdef');  END",
			    expectedValue,
			    0,
			    null,
			    null);
	    }



	    public void Var055() {
		byte[] expectedValue ={ (byte) 0xab, (byte) 0xcd, (byte) 0xef};
		testGetBlob("Call commit on return procedure that returns BLOB",
			    JDCSTest.COLLECTION+".JDCSGB55",
			       "(OUT OUTBLOB BLOB(4186000)) LANGUAGE SQL COMMIT ON RETURN YES BEGIN  SET OUTBLOB=BLOB(X'abcdef');  END ",
			    expectedValue,
			    0,
			    null,
			    null);
	    }


	    public void Var056() {
		byte[] expectedValue ={ (byte) 0xab, (byte) 0xcd, (byte) 0xef};
		String[] setupStatements={
		    "DROP TABLE "+JDCSTest.COLLECTION+".BLOBLOG56",
		    "CREATE TABLE "+JDCSTest.COLLECTION+".BLOBLOG56 (TS TIMESTAMP)",
		};
		String[] cleanupStatements = {
		    "DROP TABLE "+JDCSTest.COLLECTION+".BLOBLOG56",
		};
		testGetBlob("Call procedure that returns BLOB and does insert",
			    JDCSTest.COLLECTION+".JDCSGB54",
			    "(OUT OUTBLOB BLOB(4186000)) LANGUAGE SQL  "+
			    "BEGIN  INSERT INTO "+JDCSTest.COLLECTION+".BLOBLOG56 VALUES(CURRENT TIMESTAMP); SET OUTBLOB=BLOB(X'abcdef');  END",
			    expectedValue,
			    0,
			    setupStatements,
			    cleanupStatements);
	    }



	    public void Var057() {
		byte[] expectedValue ={ (byte) 0xab, (byte) 0xcd, (byte) 0xef};
		String[] setupStatements={
		    "DROP TABLE "+JDCSTest.COLLECTION+".BLOBLOG57",
		    "CREATE TABLE "+JDCSTest.COLLECTION+".BLOBLOG57 (TS TIMESTAMP)",
		};
		String[] cleanupStatements = {
		    "DROP TABLE "+JDCSTest.COLLECTION+".BLOBLOG57",
		};
		testGetBlob("Call procedure that returns BLOB, does insert, and runs with commit=*all",
			    JDCSTest.COLLECTION+".JDCSGB54",
			    "(OUT OUTBLOB BLOB(4186000)) LANGUAGE SQL  "+
			    " SET OPTION COMMIT=*ALL "+
			    "BEGIN  INSERT INTO "+JDCSTest.COLLECTION+".BLOBLOG57 VALUES(CURRENT TIMESTAMP); SET OUTBLOB=BLOB(X'abcdef');  END",
			    expectedValue,
			    0,
			    setupStatements,
			    cleanupStatements);
	    }



	    // Repeat tests, running for 20 seconds  (looking for locator leak)


	    public void Var058() {
		byte[] expectedValue ={ (byte) 0xab, (byte) 0xcd, (byte) 0xef};
		testGetBlob("Call procedure that returns BLOB",
			    JDCSTest.COLLECTION+".JDCSGB58",
			       "(OUT OUTBLOB BLOB(4186000)) LANGUAGE SQL BEGIN  SET OUTBLOB=BLOB(X'abcdef');  END",
			    expectedValue,
			    20,
			    null,
			    null);
	    }



	    public void Var059() {
		byte[] expectedValue ={ (byte) 0xab, (byte) 0xcd, (byte) 0xef};
		testGetBlob("Call commit on return procedure that returns BLOB",
			    JDCSTest.COLLECTION+".JDCSGB59",
			       "(OUT OUTBLOB BLOB(4186000)) LANGUAGE SQL COMMIT ON RETURN YES BEGIN  SET OUTBLOB=BLOB(X'abcdef');  END",
			    expectedValue,
			    20,
			    null,
			    null);
	    }


	    public void Var060() {
		byte[] expectedValue ={ (byte) 0xab, (byte) 0xcd, (byte) 0xef};
		String[] setupStatements={
		    "DROP TABLE "+JDCSTest.COLLECTION+".BLOBLOG60",
		    "CREATE TABLE "+JDCSTest.COLLECTION+".BLOBLOG60 (TS TIMESTAMP)",
		};
		String[] cleanupStatements = {
		    "DROP TABLE "+JDCSTest.COLLECTION+".BLOBLOG60",
		};
		testGetBlob("Call procedure that returns BLOB and does insert",
			    JDCSTest.COLLECTION+".JDCSGB58",
			    "(OUT OUTBLOB BLOB(4186000)) LANGUAGE SQL  "+
			    "BEGIN  INSERT INTO "+JDCSTest.COLLECTION+".BLOBLOG60 VALUES(CURRENT TIMESTAMP); SET OUTBLOB=BLOB(X'abcdef');  END",
			    expectedValue,
			    20,
			    setupStatements,
			    cleanupStatements);
	    }



	    public void Var061() {
		byte[] expectedValue ={ (byte) 0xab, (byte) 0xcd, (byte) 0xef};
		String[] setupStatements={
		    "DROP TABLE "+JDCSTest.COLLECTION+".BLOBLOG61",
		    "CREATE TABLE "+JDCSTest.COLLECTION+".BLOBLOG61 (TS TIMESTAMP)",
		};
		String[] cleanupStatements = {
		    "DROP TABLE "+JDCSTest.COLLECTION+".BLOBLOG61",
		};
		testGetBlob("Call procedure that returns BLOB, does insert, and runs with commit=*all",
			    JDCSTest.COLLECTION+".JDCSGB58",
			    "(OUT OUTBLOB BLOB(4186000)) LANGUAGE SQL  "+
			    " SET OPTION COMMIT=*ALL "+
			    "BEGIN  INSERT INTO "+JDCSTest.COLLECTION+".BLOBLOG61 VALUES(CURRENT TIMESTAMP); SET OUTBLOB=BLOB(X'abcdef');  END",
			    expectedValue,
			    20,
			    setupStatements,
			    cleanupStatements);
	    }


  public void Var062() {
    // NOTE: Do not need to check for JDBC40 because the drivers
    // provide the method in the JDBC30 version also

    StringBuffer sb = new StringBuffer();
    sb.append(" -- Added 03/09/2017 for jt400 bug 385\n");
    String sql;
    try {
      byte[] bytes = { 0x01, 0x02, 0x03, 0x04, 0x05 };
      byte[] expectedBytes = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x01, 0x02, 0x03,
          0x04, 0x05, };

      boolean passed = true;
      Statement stmt = connection_.createStatement();
      String procedureName = JDCSTest.COLLECTION + ".JDBLOBIO";
      try {
        sql = "DROP PROCEDURE " + procedureName;
        sb.append("Executing " + sql + "\n");
        stmt.executeUpdate(sql);
      } catch (Exception e) {
        printStackTraceToStringBuffer(e, sb);
      }
      sql = "CREATE  PROCEDURE " + procedureName
          + "(INOUT P1 BLOB(1M)) LANGUAGE SQL BEGIN SET P1 = P1 || P1; END";
      sb.append("Executing " + sql + "\n");
      stmt.executeUpdate(sql);

      sql = "CALL " + procedureName + "(?)";
      sb.append("Preparing " + sql + "\n");
      CallableStatement cstmt = connection_.prepareCall(sql);
      cstmt.registerOutParameter(1, Types.BLOB);
      ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
      // Method only available on Java 1.6
      JDReflectionUtil.callMethod_V(cstmt, "setBlob", 1, bais, (long) 5);

      cstmt.execute();
      Blob blob = cstmt.getBlob(1);
      passed = compare(blob, expectedBytes, sb);

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
  getBlob() - Get a type that was registered as a BOOLEAN.
  **/
     
     public void Var063()
     {
        if (checkBooleanSupport()) {
          getTestFailed(csTypes_,"getBlob",25,"Data type mismatch",""); 
         
        }
     }

     
     /**
     getBlob() - Get a type that was registered as a BOOLEAN.
     **/
        
        public void Var064()
        {
           if (checkBooleanSupport()) {
             getTestFailed(csTypes_,"getBlob","P_BOOLEAN","Data type mismatch",""); 
            
           }
        }

}
