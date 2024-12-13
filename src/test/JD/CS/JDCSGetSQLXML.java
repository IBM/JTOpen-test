///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetSQLXML.java
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
// File Name:    JDCSGetSQLXML.java
//
// Classes:      JDCSGetSQLXML
//
////////////////////////////////////////////////////////////////////////
//
//
// 
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JD.JDTestUtilities;

import java.awt.TextArea;
import java.io.FileOutputStream;
import java.io.InputStream;

import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector; 



/**
Testcase JDCSGetSQLXML.  This tests the getSQLXML method
of the JDBC CallableStatement class using the following
data types

<ul>
<li>BLOB
<li>CLOB
<li>DBCLOB
<li>BLOB LOCATOR
<li>CLOB LOCATOR
<li>DBCLOB LOCATOR 
</ul>
**/
public class JDCSGetSQLXML
extends JDCSGetTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSGetSQLXML";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCSTest.main(newArgs); 
   }


   // Note.. LARGECLOB_SIZE must be a multiple of 200
   private static int  LARGE_CLOB_SIZE = 20000000;
   private static int LARGE_BLOB_SIZE=40000000; 

// Private data.
   private Connection      connectionLob_;

   private CallableStatement   csTypesLob_;

   private Connection          connectionLobLocator_;
   private CallableStatement   csTypesLobLocator_;
   boolean RUNTOOLONG = false;

   static final byte[] expected = { (byte) 0x4c,(byte) 0x84,(byte) 0x6e,
     (byte) 0xC4, (byte) 0x81, (byte) 0xA5, (byte) 0x85, (byte) 0x40,
      (byte) 0xC5, (byte) 0x87, (byte) 0x93, (byte) 0x85,
      (byte) 0x4c, (byte) 0x61, (byte) 0x84,(byte) 0x6e,
      };



/**
Constructor.
**/
   public JDCSGetSQLXML (AS400 systemObject,
                       Hashtable namesAndVars,
                       int runMode,
                       FileOutputStream fileOutputStream,
                       
                       String password)
   {
      super (systemObject, "JDCSGetSQLXML",
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
     
     super.setup(); 
      connectionLob_ = testDriver_.getConnection (baseURL_
                                               + ";errors=full;lob threshold=30000", userId_, encryptedPassword_);
      connectionLobLocator_ = testDriver_.getConnection (baseURL_
                                                + ";errors=full;lob threshold=0", userId_, encryptedPassword_);
      connectionLobLocator_.setAutoCommit(false);
      connectionLobLocator_.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

      csTypesLob_ = JDSetupProcedure.prepare (connectionLob_,
                                           JDSetupProcedure.STP_CSTYPESOUTX, supportedFeatures_);
      JDSetupProcedure.register (csTypesLob_, JDSetupProcedure.STP_CSTYPESOUTX, supportedFeatures_, getDriver());
      csTypesLob_.execute ();

      csTypesLobLocator_ = JDSetupProcedure.prepare (connectionLobLocator_,
                                            JDSetupProcedure.STP_CSTYPESOUTX, supportedFeatures_);
      JDSetupProcedure.register (csTypesLobLocator_, JDSetupProcedure.STP_CSTYPESOUTX, supportedFeatures_, getDriver());
      csTypesLobLocator_.execute ();
   }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
   protected void cleanup ()
   throws Exception
   {
      connectionLobLocator_.commit();
      connectionLobLocator_.close ();                                     
      connectionLob_.close (); 
      super.cleanup(); 
      
   }


/**
dumpBytes() - Utility function used to see the bytes
**/
    public  static String dumpBytes(byte[] b){
        StringBuffer sb = new StringBuffer(); 
	int len = b.length;
	if (len > 1000) len = 1000; 
	for(int i = 0; i < len; i++){
	    String ns = Integer.toHexString(((int) b[i]) & 0xFF);
	    if(ns.length() == 1){
		sb.append("0");
		sb.append(ns);
	    }
	    else{
		sb.append(ns);
	    }
	}
	if (len == 1000) { sb.append("......"); } 
	return sb.toString();
    }


    static String dumpBytes(Object o) throws Exception{

	InputStream is = (InputStream) JDReflectionUtil.callMethod_O(o, "getBinaryStream");
	byte[] buffer = new byte[65536];
	byte[] answer = new byte[0]; 
	int bytesRead = 0;
	bytesRead = is.read(buffer); 
	while (bytesRead > 0) {
	    answer=concatArray(answer, buffer, bytesRead);
	    bytesRead = is.read(buffer); 
	} 

	return dumpBytes(answer);
    }





/**
Compares a SQLXML with a String.
**/
   private boolean compare(Object i, String b, StringBuffer sb)
      throws SQLException {

    try {
      String s = (String) JDReflectionUtil.callMethod_O(i, "getString");
      boolean equals = s.equals(b);
      if (!equals) {
        int differentIndex = 0; 
        int sLength = s.length(); 
        int bLength = b.length(); 
        for (differentIndex = 0; 
             differentIndex < sLength  
             && differentIndex < bLength 
             && s.charAt(differentIndex) == b.charAt(differentIndex);
             differentIndex++);
        if (differentIndex > 5) differentIndex-=5; 
        sb.append("\n");
        if (s.length() < 256) {
          sb.append("xml.getString       =" + s + "\n");
        } else {
          sb.append("xml.getString(0,256) of " + s.length() + "='"
              + s.substring(0, 256) + "'\n");
          int differentLength = sLength - differentIndex;
          if (differentLength > 256) differentLength = 256; 
          sb.append("xml.getString("+differentIndex+"  = "+s.substring(differentIndex,differentIndex+differentLength)+"\n") ; 
        }
        if (b.length() < 256) {
          sb.append("expected            =" + b + "\n");
        } else {
          sb.append("expected(0,256)      of " + b.length() + "='"
              + b.substring(0, 256) + "'\n");
          int differentLength = bLength - differentIndex;
          if (differentLength > 256) differentLength = 256; 
          sb.append("expected     ("+differentIndex+"  = "+b.substring(differentIndex,differentIndex+differentLength)+"\n") ; 
          
          
        }
        
      }
      return equals; // @B1C
    } catch (Exception e) {
      SQLException sqlex = new SQLException("Comparison failure");
      sqlex.initCause(e);
      throw sqlex;
    }
  }

   static byte[]  concatArray(byte[] first, byte[] second, int secondLength) {
       byte[] answer = new byte[first.length + secondLength];

       for (int i = 0; i < first.length; i++) {
	   answer[i] = first[i]; 
       }
       for (int i = 0; i < secondLength; i++) {
	   answer[i+first.length] = second[i]; 
       } 
       return answer; 
   } 

/**
Compares a SQLXML with a byte[].
**/
   static boolean compare (Object i, byte[] b, StringBuffer sb)
   throws SQLException
   {
       try { 

	   Vector stuff = new Vector(); 
	   InputStream is = (InputStream) JDReflectionUtil.callMethod_O(i, "getBinaryStream");
	   byte[] buffer = new byte[65536];
	   byte[] answer = new byte[0]; 
	   int bytesRead = 0;
	   int totalBytes = 0; 
	   bytesRead = is.read(buffer); 
	   while (bytesRead > 0) {
	       if (bytesRead == buffer.length) {
		   stuff.addElement(buffer);
		   buffer = new byte[65536];
	       } else {
		   answer= new byte[bytesRead];
		   System.arraycopy(buffer,0,answer,0,bytesRead); 
		   stuff.addElement(answer); 
	       }
	       totalBytes += bytesRead; 
               bytesRead = is.read(buffer);
	   } 
	   answer = new byte[totalBytes];
	   Enumeration enumeration = stuff.elements();
	   int position = 0; 
	   while (enumeration.hasMoreElements()) {
	       byte block[] = (byte[]) enumeration.nextElement();
	       System.arraycopy(block, 0, answer, position, block.length);
	       position+=block.length; 

	   } 
	   



	   boolean result =  areEqual (answer, b);
	   if (!result) {
	       sb.append("\n"); 
	       sb.append("XML  ("+dumpBytes(answer)+")\n");
	       sb.append("Bytes("+dumpBytes(b)+")\n"); 
         sb.append("XML   Ascii("+JDTestUtilities.dumpBytesAsAscii(answer)+")\n");
         sb.append("Bytes Ascii("+JDTestUtilities.dumpBytesAsAscii(b)+")\n"); 
         sb.append("XML   Ebcdic("+JDTestUtilities.dumpBytesAsEbcdic(answer)+")\n");
         sb.append("Bytes Ebcdic("+JDTestUtilities.dumpBytesAsEbcdic(b)+")\n"); 
	   } 
	   return result; 


       } catch (Exception e) {
	   SQLException sqlex = new SQLException("Comparison failure"); 
	   sqlex.initCause(e); 
	   throw sqlex; 
       }
   }

/**
getSQLXML() - Get parameter -1.
**/
   public void Var001()
   {
      if (checkJdbc40 ()) {
         try {
            csTypesLob_.execute ();
            Object p = JDReflectionUtil.callMethod_O(csTypesLob_, "getSQLXML", -1);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getSQLXML() - Get parameter 0 
**/
   public void Var002()
   {
      if (checkJdbc40 ()) {
         try {
            Clob p = (Clob) JDReflectionUtil.callMethod_O(csTypesLob_, "getSQLXML", 0);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getSQLXML() - Use a parameter that is too big.
**/
   public void Var003()
   {
      if (checkJdbc40 ()) {
         try {
            Object p = JDReflectionUtil.callMethod_O(csTypesLob_, "getSQLXML", 35);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getSQLXML() - Get a parameter when there are no parameters.
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
            Object p =  JDReflectionUtil.callMethod_O(cs, "getSQLXML", 1);
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
getSQLXML() - Get a parameter that was not registered.
**/
   public void Var005()
   {
      if (checkJdbc40 ()) {
         try {
            CallableStatement cs = JDSetupProcedure.prepare (connectionLob_,
                                                             JDSetupProcedure.STP_CSTYPESOUTX, supportedFeatures_);
            cs.execute ();
            Object p =  JDReflectionUtil.callMethod_O(cs, "getSQLXML", 12);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getSQLXML() - Get a parameter when the statement has not been
executed.
**/
   public void Var006()
   {
      if (checkJdbc40 ()) {
         try {
            CallableStatement cs = JDSetupProcedure.prepare (connectionLob_,
                                                             JDSetupProcedure.STP_CSTYPESOUTX, supportedFeatures_);
            cs.registerOutParameter (20, Types.CLOB);
            Object p =  JDReflectionUtil.callMethod_O(cs, "getSQLXML", 20);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getSQLXML() - Get a parameter when the statement is closed.
**/
   public void Var007()
   {
      if (checkJdbc40 ()) {
         try {
            CallableStatement cs = JDSetupProcedure.prepare (connectionLob_,
                                                             JDSetupProcedure.STP_CSTYPESOUTX, supportedFeatures_);
            cs.registerOutParameter (20, Types.CLOB);
            cs.execute ();
            cs.close ();
            Object p =  JDReflectionUtil.callMethod_O(cs, "getSQLXML", 20);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getSQLXML() - Get an IN parameter that was correctly registered.
**/
   public void Var008()
   {
      if (checkJdbc40 ()) {
         try {
            CallableStatement cs = JDSetupProcedure.prepare (connectionLob_,
                                                             JDSetupProcedure.STP_CSTYPESIN, supportedFeatures_);
            JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESIN,
                                                 supportedFeatures_);
            cs.registerOutParameter (20, Types.CLOB);
            cs.execute ();
            Object p =  JDReflectionUtil.callMethod_O(cs, "getSQLXML", 20);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }


/**
getSQLXML() - Get an INOUT parameter, where the OUT parameter is
longer than the IN parameter.
**/
   public void Var009()
   {
     StringBuffer sb = new StringBuffer(); 
      if (checkJdbc40 ()) {
         if (checkLobSupport ()) {
            try {
               CallableStatement cs = JDSetupProcedure.prepare (connectionLob_,
                                                                JDSetupProcedure.STP_CSTYPESINOUTX, supportedFeatures_);
               JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESINOUTX,
                                                    supportedFeatures_);
               JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSTYPESINOUTX,
                                          supportedFeatures_, getDriver());
               cs.registerOutParameter (20, Types.CLOB);
               cs.execute ();
               Object p =  JDReflectionUtil.callMethod_O(cs, "getSQLXML", 20);
               assertCondition (compare(p, JDSetupProcedure.STP_CSTYPESINOUTX_CLOB_OUTPUT,sb),sb);
            } catch (Exception e)
            {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
getSQLXML() - Get a type that was registered as a SMALLINT.
**/
   public void Var010()
   {
      if (checkJdbc40 ()) {
         try {
            Object p =  JDReflectionUtil.callMethod_O(csTypesLob_, "getSQLXML", 1);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getSQLXML() - Get a type that was registered as an INTEGER.
**/
   public void Var011()
   {
      if (checkJdbc40 ()) {
         try {
            Object p =  JDReflectionUtil.callMethod_O(csTypesLob_, "getSQLXML", 2);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getSQLXML() - Get a type that was registered as an REAL.
**/
   public void Var012()
   {
      if (checkJdbc40 ()) {
         try {
            Object p =  JDReflectionUtil.callMethod_O(csTypesLob_, "getSQLXML", 3);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getSQLXML() - Get a type that was registered as an FLOAT.
**/
   public void Var013()
   {
      if (checkJdbc40 ()) {
         try {
            Object p =  JDReflectionUtil.callMethod_O(csTypesLob_, "getSQLXML", 4);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getSQLXML() - Get a type that was registered as an DOUBLE.
**/
   public void Var014()
   {
      if (checkJdbc40 ()) {
         try {
            Object p =  JDReflectionUtil.callMethod_O(csTypesLob_, "getSQLXML", 5);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getSQLXML() - Get a type that was registered as an DECIMAL.
**/
   public void Var015()
   {
      if (checkJdbc40 ()) {
         try {
            Object p =  JDReflectionUtil.callMethod_O(csTypesLob_, "getSQLXML", 6);
            failed ("Didn't throw SQLException  p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getSQLXML() - Get a type that was registered as an NUMERIC.
**/
   public void Var016()
   {
      if (checkJdbc40 ()) {
         try {
            Object p =  JDReflectionUtil.callMethod_O(csTypesLob_, "getSQLXML", 8);
            failed ("Didn't throw SQLException  p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getSQLXML() - Get a type that was registered as a CHAR, we allow this to work. 
**/
   public void Var017()
   {
       StringBuffer sb = new StringBuffer("Get XML registered as char"); 
      if (checkJdbc40 ()) {
         try {
            Object p =  JDReflectionUtil.callMethod_O(csTypesLob_, "getSQLXML", 11);
	    assertCondition (compare (p, "<d>Jim</d>",sb), sb);
         } catch (Exception e) {
	     failed (e, "Unexpected Exception");
         }
      }
   }



/**
getSQLXML() - Get a type that was registered as a VARCHAR.
**/
   public void Var018()
   {
     StringBuffer sb = new StringBuffer(); 

      if (checkJdbc40 ()) {
         try {
            Object p =  JDReflectionUtil.callMethod_O(csTypesLob_, "getSQLXML", 12);
	    assertCondition (compare (p, "<d>Charlie</d>",sb),sb); 
         } catch (Exception e) {
	     failed (e, "Unexpected Exception");
         }
      }
   }



/**
getSQLXML() - Get a type that was registered as a BINARY.
**/
   public void Var019()
   {
      if (checkJdbc40 ()) {
         try {
            Object p =  JDReflectionUtil.callMethod_O(csTypesLob_, "getSQLXML", 13);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getSQLXML() - Get a type that was registered as a VARBINARY.
**/
   public void Var020()
   {
      if (checkJdbc40 ()) {
         try {
            Object p =  JDReflectionUtil.callMethod_O(csTypesLob_, "getSQLXML", 14);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getSQLXML() - Get a type that was registered as a DATE.
**/
   public void Var021()
   {
      if (checkJdbc40 ()) {
         try {
            Object p =  JDReflectionUtil.callMethod_O(csTypesLob_, "getSQLXML", 15);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getSQLXML() - Get a type that was registered as a TIME.
**/
   public void Var022()
   {
      if (checkJdbc40 ()) {
         try {
            Object p =  JDReflectionUtil.callMethod_O(csTypesLob_, "getSQLXML", 16);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getSQLXML() - Get a type that was registered as a TIMESTAMP.
**/
   public void Var023()
   {
      if (checkJdbc40 ()) {
         try {
            Object p =  JDReflectionUtil.callMethod_O(csTypesLob_, "getSQLXML", 17);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getSQLXML() - Get a type that was registered as an OTHER.
The XML is not valid for this condition
**/
   public void Var024()
   {
       StringBuffer sb = new StringBuffer("The output http://w3... is not valid XML.  Updated 12/15/2011"); 
         if (checkJdbc40 ()) {
            try {
               Object p =  JDReflectionUtil.callMethod_O(csTypesLob_, "getSQLXML", 18);

	       assertCondition (false, "should throw exception but got "+p+"\n"+sb); 

            } catch (Exception e) {
              assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
         }
   }



/**
getSQLXML() - Get a type that was registered as a BLOB.
**/
   public void Var025()
   {
       notApplicable("See variation 58");
   }



/**
getSQLXML() - Get a type that was registered as a CLOB when the data
was returned in the result set.
**/
   public void Var026()
   {
     StringBuffer sb = new StringBuffer(); 

      if (checkLobSupport ()) {
         if (checkJdbc40 ()) {
            if (checkLobSupport ()) {
               try {
                  Object p =  JDReflectionUtil.callMethod_O(csTypesLob_, "getSQLXML", 20);
                  assertCondition (compare (p, "<d>Chris Smyth</d>",sb),sb);
               } catch (Exception e) {
                  failed (e, "Unexpected Exception");
               }
            }
         }
      }
   }



/**
getSQLXML() - Get a type that was registered as a CLOB when the locator
was returned in the result set.

SQL400 - let this work.
**/
   public void Var027()
   {     StringBuffer sb = new StringBuffer(); 

      if (checkLobSupport ()) {
         if (checkJdbc40 ()) {
            if (checkLobSupport ()) {
              try {
                 Object p =  JDReflectionUtil.callMethod_O(csTypesLobLocator_,"getSQLXML", 20);
                 assertCondition (compare (p, "<d>Chris Smyth</d>",sb),sb);
              } catch (Exception e) {
                 failed (e, "Unexpected Exception");
              }
            }
         }
      }
   }



/**
getSQLXML() - Get a type that was registered as a CLOB when the data
was returned in the result set and is a DBCLOB.
**/
   public void Var028()
   {     StringBuffer sb = new StringBuffer(); 

      if (checkLobSupport ()) {
         if (checkJdbc40 ()) {
            if (checkLobSupport ()) {
               try {
                  Object p =  JDReflectionUtil.callMethod_O(csTypesLob_, "getSQLXML", 21);
                  assertCondition (compare (p, "<d>Jeff Lex</d>",sb),sb);
               } catch (Exception e) {
                  failed (e, "Unexpected Exception");
               }
            }
         }
      }
   }



/**
getSQLXML() - Get a type that was registered as a CLOB when the locator
was returned in the result set and is a DBCLOB.

SQL400 - let this work.
**/
   public void Var029()
   {
     StringBuffer sb = new StringBuffer(); 

      if (checkLobSupport ()) {
         if (checkJdbc40 ()) {
            if (checkLobSupport ()) {
              try {
                 Object p =  JDReflectionUtil.callMethod_O(csTypesLobLocator_,"getSQLXML", 21);
                 assertCondition (compare (p, "<d>Jeff Lex</d>",sb),sb);
              } catch (Exception e) {
                 failed (e, "Unexpected Exception");
              }
            }
         }
      }
   }



/**
getSQLXML() - Get a type that was registered as a BIGINT.
**/
   public void Var030()
   {
      if (checkJdbc40 ()) {
         if (checkBigintSupport()) {
            try {
               Object p =  JDReflectionUtil.callMethod_O(csTypesLob_, "getSQLXML", 22);
               failed ("Didn't throw SQLException p="+p);
            } catch (Exception e) {
               assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
         }
      }
   }



/**
getSQLXML() - Get an INOUT parameter, where the OUT parameter is
longer than the IN parameter, when the output parameter is registered first.

SQL400 - We added this testcase because of a customer bug.
**/
   public void Var031()
   {
     StringBuffer sb = new StringBuffer(); 

      if (checkJdbc40 ()) {
         if (checkLobSupport ()) {
            try {
               CallableStatement cs = JDSetupProcedure.prepare (connectionLob_,
                                                                JDSetupProcedure.STP_CSTYPESINOUTX, supportedFeatures_);
               JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSTYPESINOUTX,
                                          supportedFeatures_, getDriver());
               cs.registerOutParameter (20, Types.CLOB);
               JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESINOUTX,
                                                    supportedFeatures_);
               cs.execute ();
               Object p =  JDReflectionUtil.callMethod_O(cs, "getSQLXML", 20);
               assertCondition (compare(p, JDSetupProcedure.STP_CSTYPESINOUTX_CLOB_OUTPUT,sb),sb);
            } catch (Exception e)
            {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }
// NERYKAL - named parameteR
/**
getSQLXML() - Get a type that was registered as a SMALLINT.
**/
   public void Var032()
   {
      if (checkJdbc40 ()) {
         try {
            Object p =  JDReflectionUtil.callMethod_OS(csTypesLob_, "getSQLXML", "P_SMALLINT");
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**
getSQLXML() - Get a type that was registered as an INTEGER.
**/
   public void Var033()
   {

      if (checkJdbc40 ()) {
         try {
            Object p =  JDReflectionUtil.callMethod_OS(csTypesLob_, "getSQLXML", "P_INTERGER");
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**
getSQLXML() - Get a type that was registered as an REAL.
**/
   public void Var034()
   {
      if (checkJdbc40 ()) {
         try {
            Object p =  JDReflectionUtil.callMethod_OS(csTypesLob_, "getSQLXML", "P_REAL");
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**
getSQLXML() - Get a type that was registered as an FLOAT.
**/
   public void Var035()
   {
      if (checkJdbc40 ()) {
         try {
            Object p =  JDReflectionUtil.callMethod_OS(csTypesLob_, "getSQLXML", "P_FLOAT");
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**F
getSQLXML() - Get a type that was registered as an DOUBLE.
**/
   public void Var036()
   {
      if(checkJdbc40()) {
         try {
            Object p =  JDReflectionUtil.callMethod_OS(csTypesLob_, "getSQLXML", "P_DOUBLE");
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**
getSQLXML() - Get a type that was registered as an DECIMAL.
**/
   public void Var037()
   {
      if(checkJdbc40()) {
         try {
            Object p =  JDReflectionUtil.callMethod_OS(csTypesLob_, "getSQLXML", "P_DECIMAL_50");
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**
getSQLXML() - Get a type that was registered as an DECIMAL.
**/
   public void Var038()
   {
      if(checkJdbc40()) {
         try {
            Object p =  JDReflectionUtil.callMethod_OS(csTypesLob_, "getSQLXML", "P_DECIMAL_105");
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**
getSQLXML() - Get a type that was registered as an NUMERIC.
**/
   public void Var039()
   {
      if(checkJdbc40()) {
         try {
            Object p =  JDReflectionUtil.callMethod_OS(csTypesLob_, "getSQLXML", "P_NUMERIC_50");
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**
getSQLXML() - Get a type that was registered as an NUMERIC.
**/
   public void Var040()
   {
      if(checkJdbc40()){
         try {
            Object p =  JDReflectionUtil.callMethod_OS(csTypesLob_, "getSQLXML", "P_NUMERIC_105");
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**
getSQLXML() - Get a type that was registered as a CHAR.
**/
   public void Var041()
   {
      StringBuffer sb = new StringBuffer("A 1 character char cannot be valid xml -- updated 12/14/2011"); 
      if(checkJdbc40()) {
         try {
            Object p =  JDReflectionUtil.callMethod_OS(csTypesLob_, "getSQLXML", "P_CHAR_1");
            assertCondition(false, "got "+p+" "+sb); 
         } catch (Exception e) {
           assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**
getSQLXML() - Get a type that was registered as a CHAR.
**/
   public void Var042()
   {
      if(checkJdbc40()) {
	  StringBuffer sb = new StringBuffer("Get XML registered as char"); 
         try {
            Object p =  JDReflectionUtil.callMethod_OS(csTypesLob_, "getSQLXML", "P_CHAR_50");
	    assertCondition (compare (p, "<d>Jim</d>",sb),sb);
         } catch (Exception e) {
	     failed (e, "Unexpected Exception");
         }
      }
   }

/**
getSQLXML() - Get a type that was registered as a VARCHAR.
**/
   public void Var043()
   {
     StringBuffer sb = new StringBuffer(); 

      if(checkJdbc40()) {
         try {
            Object p =  JDReflectionUtil.callMethod_OS(csTypesLob_, "getSQLXML", "P_VARCHAR_50");
	    assertCondition (compare (p, "<d>Charlie</d>",sb),sb); 
	 } catch (Exception e) {
	     failed (e, "Unexpected Exception");
	 }
      }
   }

/**
getSQLXML() - Get a type that was registered as a BINARY.
**/
   public void Var044()
   {
      if(checkJdbc40()) {
         try {
            Object p =  JDReflectionUtil.callMethod_OS(csTypesLob_, "getSQLXML", "P_BINARY_20");
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**
getSQLXML() - Get a type that was registered as a VARBINARY.
**/
   public void Var045()
   {
      if(checkJdbc40()) {
         try {
            Object p =  JDReflectionUtil.callMethod_OS(csTypesLob_, "getSQLXML", "P_VARBINARY_20");
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**
getSQLXML() - Get a type that was registered as a DATE.
**/
   public void Var046()
   {
      if(checkJdbc40()) {
         try {
            Object p =  JDReflectionUtil.callMethod_OS(csTypesLob_, "getSQLXML", "P_DATE");
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**
getSQLXML() - Get a type that was registered as a TIME.
**/
   public void Var047()
   {
      if(checkJdbc40()) {
         try {
            Object p =  JDReflectionUtil.callMethod_OS(csTypesLob_, "getSQLXML", "P_TIME");
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**
getSQLXML() - Get a type that was registered as a TIMESTAMP.
**/
   public void Var048()
   {
      if(checkJdbc40()) {
         try {
            Object p =  JDReflectionUtil.callMethod_OS(csTypesLob_, "getSQLXML", "P_TIMESTAMP");
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**
getSQLXML() - Get a type that was registered as an OTHER.
**/
   public void Var049()
   {
     StringBuffer sb = new StringBuffer("The output http://w3... is not valid XML.  Updated 12/15/2011"); 

      if(checkJdbc40()) {
         if (checkJdbc40 ()) {
            try {
               Object p =  JDReflectionUtil.callMethod_OS(csTypesLob_, "getSQLXML", "P_DATALINK");
               assertCondition (false, "should throw exception but got "+p+"\n"+sb); 


            } catch (Exception e) {
              assertExceptionIsInstanceOf (e, "java.sql.SQLException");

            }
         }
      }
   }

/**
getSQLXML() - Get a type that was registered as a BLOB.
**/
   public void Var050()
   {
       notApplicable("BLOB variation");
   }

/**
getSQLXML() - Get a type that was registered as a CLOB when the data
was returned in the result set.
**/
   public void Var051()
   {     StringBuffer sb = new StringBuffer(); 

      if(checkJdbc40()) {
         if (checkJdbc40 ()) {
            if (checkLobSupport ()) {
               try {
                  Object p =  JDReflectionUtil.callMethod_OS(csTypesLob_, "getSQLXML", "P_CLOB");
                  assertCondition (compare (p, "<d>Chris Smyth</d>",sb),sb);
               } catch (Exception e) {
                  failed (e, "Unexpected Exception");
               }
            }
         }
      }
   }

/**
getSQLXML() - Get a type that was registered as a CLOB when the locator
was returned in the result set.

SQL400 - let this work.
**/
   public void Var052()
   {
     StringBuffer sb = new StringBuffer(); 

      if(checkJdbc40()) {
         if (checkJdbc40 ()) {
            if (checkLobSupport ()) {
              try {
                 Object p =  JDReflectionUtil.callMethod_OS(csTypesLobLocator_,"getSQLXML", "P_CLOB");
                 assertCondition (compare (p, "<d>Chris Smyth</d>",sb),sb);
              } catch (Exception e) {
                 failed (e, "Unexpected Exception");
              }
            }
         }
      }
   }

/**
getSQLXML() - Get a type that was registered as a CLOB when the data
was returned in the result set and is a DBCLOB.
**/
   public void Var053()
   {
     StringBuffer sb = new StringBuffer(); 

     if(checkJdbc40()) {
         if (checkJdbc40 ()) {
            if (checkLobSupport ()) {
               try {
                  Object p =  JDReflectionUtil.callMethod_OS(csTypesLob_, "getSQLXML", "P_DBCLOB");
                  assertCondition (compare (p, "<d>Jeff Lex</d>",sb),sb);
               } catch (Exception e) {
                  failed (e, "Unexpected Exception");
               }
            }
         }
      }
   }

/**
getSQLXML() - Get a type that was registered as a CLOB when the locator
was returned in the result set and is a DBCLOB.

SQL400 - let this work.
**/
   public void Var054()
   {
     StringBuffer sb = new StringBuffer(); 

      if(checkJdbc40()) {
         if (checkJdbc40 ()) {
            if (checkLobSupport ()) {
              try {
                 Object p =  JDReflectionUtil.callMethod_OS(csTypesLobLocator_,"getSQLXML", "P_DBCLOB");
                 assertCondition (compare (p, "<d>Jeff Lex</d>",sb),sb);
              } catch (Exception e) {
                 failed (e, "Unexpected Exception");
              }
            }
         }
      }
   }

/**
getSQLXML() - Get a type that was registered as a BIGINT.
**/
   public void Var055()
   {
      if(checkJdbc40()) {
         if (checkBigintSupport()) {
            try {
               Object p =  JDReflectionUtil.callMethod_OS(csTypesLob_, "getSQLXML", "P_BIGINT");
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
        StringBuffer info = new StringBuffer(); 

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

		      Statement stmt = connectionLob_.createStatement ();
		      try {
			  stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".BIGCLOBOUT");
		      } catch (Exception e) {}    	    
		      stmt.executeUpdate(sql);
		      sql = "{call "+JDCSTest.COLLECTION+".BIGCLOBOUT (?)}"; 
		      CallableStatement cstmt = connectionLob_.prepareCall(sql);
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
		      String expected2 = sb.toString(); 

		      Clob check = (Clob) JDReflectionUtil.callMethod_O(cstmt, "getSQLXML", 1);
		      assertCondition (compare(check, expected2,info), "Testcase added 12/14/2005 by native driver");
		  } catch (Exception e) {
		      failed (e, "Unexpected Exception Last SQL was "+sql+" Testcase added 12/14/2005 by native driver -- "+
			      " Currrently fails -- see issue 29243 ");
		  } 
	      }
	  }
      }

      /**
       * getSQLXML() - Get CLOB on a large CLOB
       */
  public void Var057() {
    StringBuffer info = new StringBuffer(); 

    if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
      notApplicable("V5R3 or later test");
    } else {
      if (checkJdbc40()) {
	  if (getDriver()  == JDTestDriver.DRIVER_NATIVE) { /* native only large lob test */ 
	      String sql = "";
	      try {
		  StringBuffer sb = new StringBuffer();
		  sb.append("<d>"); 
		  for (int i = 0; i < LARGE_CLOB_SIZE-7; i++) {
		      sb.append('A');
		  }
		  sb.append("</d>"); 
		  String expected1; /*  = sb.toString(); */ 
		  String quarter = sb.substring(0, LARGE_CLOB_SIZE / 4 - 4)+"</d>";
		  expected1 = quarter + quarter + quarter + quarter; 
		  Statement stmt = connectionLob_.createStatement();

		  sql = "DROP TABLE " + JDCSTest.COLLECTION + ".BIGCLOBQ";
		  try {
		      stmt.executeUpdate(sql);
		  } catch (Exception e) {
		  }

		  sql = "CREATE TABLE " + JDCSTest.COLLECTION + ".BIGCLOBQ (C1 CLOB("
		    + LARGE_CLOB_SIZE / 4 + "))";
		  stmt.executeUpdate(sql);
		  sql = "INSERT INTO " + JDCSTest.COLLECTION + ".BIGCLOBQ VALUES(? )";
		  PreparedStatement pstmt = connectionLob_.prepareStatement(sql);
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
		  sql = "{call " + JDCSTest.COLLECTION + ".BIGCLOBO2 (?)}";
		  CallableStatement cstmt = connectionLob_.prepareCall(sql);
		  cstmt.registerOutParameter(1, java.sql.Types.CLOB);
		  cstmt.execute();

		  Object check =  JDReflectionUtil.callMethod_O(cstmt,"getSQLXML",1);
		  assertCondition(compare(check, expected1, info),
				  "Testcase added 12/14/2005 by native driver"+info);
	      } catch (Exception e) {
		  failed(e, "Unexpected Exception Last SQL was " + sql
			 + " Testcase added 12/14/2005 by native driver -- ");
	      } finally {
		  sql = "DROP TABLE " + JDCSTest.COLLECTION + ".BIGCLOBT";
		  try {
		      Statement stmt = connectionLob_.createStatement();
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
getSQLXML() - Get an INOUT parameter, where the OUT parameter is
longer than the IN parameter.
**/
  public void Var058()
  {
      StringBuffer sb = new StringBuffer("Get inout where out longer than in"); 
      if (checkJdbc40 ()) {
	  try {
	      CallableStatement cs = JDSetupProcedure.prepare (connectionLob_,
							       JDSetupProcedure.STP_CSTYPESINOUTX, supportedFeatures_);
	      JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESINOUTX,
						   supportedFeatures_);
	      JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSTYPESINOUTX,
					 supportedFeatures_, getDriver());
	      cs.registerOutParameter (19, Types.BLOB);
	      cs.execute ();
	      Object p =  JDReflectionUtil.callMethod_O(cs,"getSQLXML",19);
	      byte[] check = new byte[] {
		  (byte)0x4C, (byte)0x84, (byte) 0x6e,
		  (byte)0xC8, (byte)0x85, (byte)0x93, (byte)0x93,
	      (byte)0x96, (byte)0x40, (byte)0xA3, (byte)0x88,
	      (byte)0x85, (byte)0x99, (byte)0x85,
		  (byte)0x4C, (byte) 0x61, (byte)0x84, (byte) 0x6e}; // "<d>Hello there</d>"
	      if (compare(p, check,sb)) {
		  assertCondition(true); 
	      } else { 
		  assertCondition (false, "\nGot: "+dumpBytes(p)+"\nExp: "+dumpBytes(check)+sb);    // @B1C
	      }
	  } catch (Exception e)
	  {
	      failed (e, "Unexpected Exception");
	  }
      }
  }



/**
getSQLXML() - Get a type that was registered as a BLOB, when 
the data was returned in the result set.
**/
   public void Var059()
   {
       StringBuffer sb = new StringBuffer("Get type registered as BLOB"); 
      if (checkJdbc40 ()) {
         if (checkLobSupport ()) {
            try {
		Object p =  JDReflectionUtil.callMethod_O(csTypesLob_,"getSQLXML",19);
               assertCondition (compare(p, expected,sb),sb);
            } catch (Exception e)
            {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }



/**
getSQLXML() - Get a type that was registered as a BLOB, when 
the locator was returned in the result set.
**/
   public void Var060()
   {
       StringBuffer sb = new StringBuffer("Get type registered as BLOB locator"); 
      if (checkJdbc40 ()) {
         if (checkLobSupport ()) {
            try {
		Object p =  JDReflectionUtil.callMethod_O(csTypesLobLocator_,"getSQLXML",19);
		if (compare(p, expected,sb)) {
		    assertCondition(true); 
		} else { 
		    assertCondition (false, "\nGot: "+dumpBytes(p)+"\nExp: "+dumpBytes(expected)+sb);
		}

            } catch (Exception e)
            {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


   /**
   getSQLXML() - Get blob on a large blob 
   **/
      public void Var061()
      {
        StringBuffer testInfo = new StringBuffer(); 
	  if (checkJdbc40 ()) {
	      if (getDriver()  == JDTestDriver.DRIVER_NATIVE) { /* native only large lob test */ 
		  String sql ="";
		  Statement stmt = null; 
		  try {
		      byte[] info = new byte[LARGE_BLOB_SIZE];
		      for (int i = 0; i < LARGE_BLOB_SIZE; i++) {
			       info[i] = 0x01;
		      }

		      stmt = connectionLob_.createStatement ();

		      sql = "DROP TABLE "+JDCSTest.COLLECTION+".BIGBLOBT";
		      try {
			  stmt.executeUpdate(sql); 
		      } catch (Exception e) {} 

		      sql = "CREATE TABLE "+JDCSTest.COLLECTION+".BIGBLOBT (C1 BLOB("+LARGE_BLOB_SIZE+"))";
		      stmt.executeUpdate(sql); 
		      sql = "INSERT INTO "+JDCSTest.COLLECTION+".BIGBLOBT VALUES(?)";
		      PreparedStatement pstmt = connectionLob_.prepareStatement(sql);
		      pstmt.setBytes(1, info);
		      pstmt.executeUpdate(); 

		      sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+".BIGBLOBO2(OUT B BLOB("+LARGE_BLOB_SIZE+")) LANGUAGE SQL " +
			"SPECIFIC BIGLOBO2 JDCSBBO2: BEGIN "+
			"DECLARE C1 CURSOR FOR SELECT C1 FROM "+JDCSTest.COLLECTION+".BIGBLOBT;"+
			"OPEN C1;"+
			"FETCH C1 INTO B;"+
			"CLOSE C1;"+
			"END JDCSBBO2" ;

		      try {
			  stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".BIGBLOBO2");
		      } catch (Exception e) {}    	    
		      stmt.executeUpdate(sql);
		      sql = "{call "+JDCSTest.COLLECTION+".BIGBLOBO2 (?)}"; 
		      CallableStatement cstmt = connectionLob_.prepareCall(sql);
		      cstmt.registerOutParameter(1, java.sql.Types.BLOB);
		      cstmt.execute();

		      Object check =  JDReflectionUtil.callMethod_O(cstmt,"getSQLXML",1);

		      if (compare(check, info, testInfo)) {
			  assertCondition(true);
		      } else { 
			  assertCondition (false, "Testcase added 09/15/2008 by native driver\n"+testInfo);
		      }
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
getSQLXML() - Get parameter 0 on a return value function.
**/
   public void Var062()
   {
      if (checkJdbc40 ()) {
         try {

            CallableStatement cs = connection_.prepareCall("?=CALL " + JDSetupProcedure.STP_CSPARMS + "(?,?,?)");
            cs.setInt(2, 332);
            cs.setInt(4, 126);
            cs.registerOutParameter(1, Types.INTEGER);
            cs.registerOutParameter(3, Types.INTEGER);
            cs.registerOutParameter(4, Types.INTEGER);
            cs.executeUpdate ();

            Object p = JDReflectionUtil.callMethod_O(cs, "getSQLXML", 1);
            failed ("Didn't throw SQLException p="+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



   /**
   getSQLXML() - Get SQLXML on a null column  
   **/
      public void Var063()
      {
	  if (checkJdbc40 ()) {
	      String sql ="";
	      Statement stmt = null; 
	      try {
		  stmt = connectionLob_.createStatement ();

		  
		  sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+".XMLNULLCLB(OUT B CLOB(10000)) LANGUAGE SQL " +
			"SPECIFIC XMLNULLCLB XMLO2: BEGIN "+
			"SET B=NULL; " + 
			"END XMLO2" ;
		  
		  try {
		      stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".XMLNULLCLB");
		  } catch (Exception e) {}    	    
		  stmt.executeUpdate(sql);
		  sql = "{call "+JDCSTest.COLLECTION+".XMLNULLCLB (?)}"; 
		  CallableStatement cstmt = connectionLob_.prepareCall(sql);
		  cstmt.registerOutParameter(1, java.sql.Types.BLOB);
		  cstmt.execute();
		  
		  Object check =  JDReflectionUtil.callMethod_O(cstmt,"getSQLXML",1);

		  assertCondition (check == null, "Expected null but got "+check+" -- Testcase added 08/07/2008 by native driver");
	      } catch (Exception e) {
		  failed (e, "Unexpected Exception Last SQL was "+sql+" Testcase added 08/07/2008 by native driver -- "
			      );
	      }

	  }
      }


      /**
       * getSQLXML() - Get a type that was registered as a BOOLEAN.
       **/

      public void Var064() {
        if (checkJdbc40()) {

          if (checkBooleanSupport()) {
            getTestFailed(csTypes_, "getSQLXML", 25, "Data type mismatch", "");

          }
        }
      }

      /**
       * getSQLXML() - Get a type that was registered as a BOOLEAN.
       **/

      public void Var065() {
        if (checkJdbc40()) {

          if (checkBooleanSupport()) {
            getTestFailed(csTypes_, "getSQLXML", "P_BOOLEAN", "Data type mismatch",
                "");

          }
        }
      }




}
