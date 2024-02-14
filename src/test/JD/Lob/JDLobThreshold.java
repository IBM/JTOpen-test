///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDLobThreshold.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.JD.Lob;

import com.ibm.as400.access.AS400;

import test.JDLobTest;
import test.JDTestDriver;
import test.JDTestcase;
import test.PasswordVault;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDLobThreshold.  This tests the following properties
of the JDBC driver:

<ul>
<li>"lob threshold" property
</ul>
**/
public class JDLobThreshold
extends JDTestcase
{



   // Private data.
   public static String TABLE_          = JDLobTest.COLLECTION + ".THRESHOLD";
   public static       byte[]  MEDIUM_         = null; // final
   public static       byte[]  LARGE_          = null; // final
   public static final int     WIDTH_          = 30000;


   StringBuffer sb = new StringBuffer(); 

/**
Static initializer.
**/
   static 
   {
      // This byte array will just contain a list of negative
      // even numbers.
      MEDIUM_ = new byte[50];
      for (int i = 0; i < MEDIUM_.length; ++i)
         MEDIUM_[i] = (byte) (-2 * i);

      LARGE_ = new byte[WIDTH_];
      for (int i = 0; i < WIDTH_; ++i)
         LARGE_[i] = (byte) '^';
   }



/**
Constructor.
**/
   public JDLobThreshold (AS400 systemObject,
                          Hashtable namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          String password)
   {
      super (systemObject, "JDLobThreshold",
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
       TABLE_          = JDLobTest.COLLECTION + ".THRESHOLD";

      if (isJdbc20 ()) {
         if (areLobsSupported ()) {
            String url = baseURL_
                         + ";lob threshold=1";
            Connection c = testDriver_.getConnection(url,systemObject_.getUserId(),encryptedPassword_);
            
            c.setAutoCommit(false);
            c.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);

            s.executeUpdate ("CREATE TABLE " + TABLE_ 
                             + "(C_BLOB BLOB)");

            PreparedStatement ps = c.prepareStatement ("INSERT INTO " 
                                                       + TABLE_ + " (C_BLOB) VALUES (?)");
            ps.setBytes (1, new byte[0]);   // Row 1
            ps.executeUpdate ();
            ps.setBytes (1, MEDIUM_);       // Row 2
            ps.executeUpdate ();
            ps.setBytes (1, LARGE_);        // Row 3
            ps.executeUpdate ();
            ps.close ();
            s.close ();
            c.commit();
            c.close ();
         }
      }
   }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
   protected void cleanup ()
   throws Exception
   {
      if (isJdbc20 ()) {
         if (areLobsSupported ()) {
            String url = baseURL_
                         + ";lob threshold=1";
            Connection c = testDriver_.getConnection(url,systemObject_.getUserId(),encryptedPassword_);
            c.setAutoCommit(false);
            c.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            s.executeUpdate ("DROP TABLE " + TABLE_);   
            s.close ();
            c.commit();
            c.close ();
         }
      }
   }



/**
"lob threshold" property - Setting to an invalid value.
**/
   public void Var001()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               String url = baseURL_
                            + ";user=" + systemObject_.getUserId()
                            + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                            + ";lob threshold=INVALID";
               Connection c = testDriver_.getConnection(url,systemObject_.getUserId(),encryptedPassword_);
               c.setAutoCommit(false);
               c.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
               Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                ResultSet.CONCUR_READ_ONLY); 
               ResultSet rs = s.executeQuery ("SELECT * FROM " + TABLE_);
               rs.absolute (3);
               Blob blob = rs.getBlob ("C_BLOB");
               InputStream v = blob.getBinaryStream ();
	       boolean check;
	       sb.setLength(0); 
	       if(getDriver() == JDTestDriver.DRIVER_NATIVE &&
		  getRelease() >= JDTestDriver.RELEASE_V7R1M0)
		   check = compareBeginsWithBytes( v, LARGE_,sb);
	       else
		   check = compare (v, LARGE_,sb);
               rs.close ();
               s.close ();
               c.close ();
               assertCondition (check == true);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
"lob threshold" property - Setting to -1.
**/
   public void Var002()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               String url = baseURL_
                            + ";user=" + systemObject_.getUserId()
                            + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                            + ";lob threshold=-1";
               Connection c = testDriver_.getConnection(url,systemObject_.getUserId(),encryptedPassword_);
               c.setAutoCommit(false);
               c.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
               Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);                
               ResultSet rs = s.executeQuery ("SELECT * FROM " + TABLE_);
               rs.absolute (3);
               Blob blob = rs.getBlob ("C_BLOB");
               InputStream v = blob.getBinaryStream ();
	       boolean check;
	       sb.setLength(0); 
	       if(getDriver() == JDTestDriver.DRIVER_NATIVE &&
		  getRelease() >= JDTestDriver.RELEASE_V7R1M0)
		   check = compareBeginsWithBytes( v, LARGE_,sb);
	       else
		   check = compare (v, LARGE_,sb);
               rs.close ();
               s.close ();
               c.close ();
               assertCondition (check,sb);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
"lob threshold" property - Setting to 0.
**/
   public void Var003()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               String url = baseURL_
                            + ";user=" + systemObject_.getUserId()
                            + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                            + ";lob threshold=0";
               Connection c = testDriver_.getConnection(url,systemObject_.getUserId(),encryptedPassword_);
               c.setAutoCommit(false);
               c.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
               Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);                
               ResultSet rs = s.executeQuery ("SELECT * FROM " + TABLE_);
               rs.absolute (3);
               Blob blob = rs.getBlob ("C_BLOB");
               InputStream v = blob.getBinaryStream ();
               sb.setLength(0); 
               boolean check = 	(getDriver() == JDTestDriver.DRIVER_NATIVE &&
				 getRelease() >= JDTestDriver.RELEASE_V7R1M0) ?
		 compareBeginsWithBytes( v, LARGE_,sb):
		 compare (v, LARGE_,sb);
               rs.close ();
               s.close ();
               c.close ();
               assertCondition (check ,sb);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
"lob threshold" property - Setting to 1.
**/
   public void Var004()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               String url = baseURL_
                            + ";user=" + systemObject_.getUserId()
                            + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                            + ";lob threshold=1";
               Connection c = testDriver_.getConnection(url,systemObject_.getUserId(),encryptedPassword_);
               c.setAutoCommit(false);
               c.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
               Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);                
               ResultSet rs = s.executeQuery ("SELECT * FROM " + TABLE_);
               rs.absolute (3);
               Blob blob = rs.getBlob ("C_BLOB");
               InputStream v = blob.getBinaryStream ();
//               boolean check = compare (v, LARGE_);
               sb.setLength(0); 
               boolean check = 	(getDriver() == JDTestDriver.DRIVER_NATIVE &&
				 getRelease() >= JDTestDriver.RELEASE_V7R1M0) ?
		 compareBeginsWithBytes( v, LARGE_,sb):
		 compare (v, LARGE_,sb);

               rs.close ();
               s.close ();
               c.close ();
               assertCondition (check == true,sb);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
"lob threshold" property - Setting to 2.
**/
   public void Var005()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               String url = baseURL_
                            + ";user=" + systemObject_.getUserId()
                            + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                            + ";lob threshold=2";
               Connection c = testDriver_.getConnection(url,systemObject_.getUserId(),encryptedPassword_);
               c.setAutoCommit(false);
               c.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
               Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);                
               ResultSet rs = s.executeQuery ("SELECT * FROM " + TABLE_);
               rs.absolute (3);
               sb.setLength(0); 
               Blob blob = rs.getBlob ("C_BLOB");
               InputStream v = blob.getBinaryStream ();
//               boolean check = compare (v, LARGE_);
               boolean check = 	(getDriver() == JDTestDriver.DRIVER_NATIVE &&
				 getRelease() >= JDTestDriver.RELEASE_V7R1M0) ?
		 compareBeginsWithBytes( v, LARGE_,sb):
		 compare (v, LARGE_,sb);

               rs.close ();
               s.close ();
               c.close ();
               assertCondition (check == true,sb);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
"lob threshold" property - Setting to 10000.
**/
   public void Var006()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               String url = baseURL_
                            + ";user=" + systemObject_.getUserId()
                            + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                            + ";lob threshold=10000";
               Connection c = testDriver_.getConnection(url,systemObject_.getUserId(),encryptedPassword_);
               c.setAutoCommit(false);
               c.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
               Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);                
               ResultSet rs = s.executeQuery ("SELECT * FROM " + TABLE_);
               rs.absolute (3);
               Blob blob = rs.getBlob ("C_BLOB");
               InputStream v = blob.getBinaryStream ();
//               boolean check = compare (v, LARGE_);
               sb.setLength(0); 
               boolean check = 	(getDriver() == JDTestDriver.DRIVER_NATIVE &&
				 getRelease() >= JDTestDriver.RELEASE_V7R1M0) ?
		 compareBeginsWithBytes( v, LARGE_,sb):
		 compare (v, LARGE_,sb);

               rs.close ();
               s.close ();
               c.close ();
               assertCondition (check == true,sb);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
"lob threshold" property - Setting to 30000.
**/
   public void Var007()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               String url = baseURL_
                            + ";user=" + systemObject_.getUserId()
                            + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                            + ";lob threshold=30000";
               Connection c = testDriver_.getConnection(url,systemObject_.getUserId(),encryptedPassword_);
               c.setAutoCommit(false);
               c.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
               Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);                
               ResultSet rs = s.executeQuery ("SELECT * FROM " + TABLE_);
               rs.absolute (3);
               Blob blob = rs.getBlob ("C_BLOB");
               InputStream v = blob.getBinaryStream ();
//               boolean check = compare (v, LARGE_);
               sb.setLength(0); 
               boolean check = 	(getDriver() == JDTestDriver.DRIVER_NATIVE &&
				 getRelease() >= JDTestDriver.RELEASE_V7R1M0) ?
		 compareBeginsWithBytes( v, LARGE_,sb):
		 compare (v, LARGE_,sb);

               rs.close ();
               s.close ();
               c.close ();
               assertCondition (check == true,sb);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
"lob threshold" property - Setting to the maximum.

SQL400 - The native JDBC driver has a considerably smaller LOB threshold of 500,000.  
This is because by default the Native JDBC driver uses blocking and must allocate 
its memory block based on this number for LOBs.  The AS/400 allows memory allocations
to be up to 16M in size and the driver has a default block size of 32.  Anything larger
than 500,000 and we can't create a memory block for a single column result set.  
**/
   public void Var008()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               String url = baseURL_
                            + ";user=" + systemObject_.getUserId()
                            + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) ;

               if (getDriver () == JDTestDriver.DRIVER_NATIVE)
                               url += ";lob threshold=500000";
               else
                               url += ";lob threshold=4194304";

               Connection c = testDriver_.getConnection(url,systemObject_.getUserId(),encryptedPassword_);
               c.setAutoCommit(false);
               c.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
               Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);                
               ResultSet rs = s.executeQuery ("SELECT * FROM " + TABLE_);
               rs.absolute (3);
               Blob blob = rs.getBlob ("C_BLOB");
               InputStream v = blob.getBinaryStream ();
//               boolean check = compare (v, LARGE_);
               sb.setLength(0); 
               boolean check = 	(getDriver() == JDTestDriver.DRIVER_NATIVE &&
				 getRelease() >= JDTestDriver.RELEASE_V7R1M0) ?
		 compareBeginsWithBytes( v, LARGE_,sb):
		 compare (v, LARGE_,sb);

               rs.close ();
               s.close ();
               c.close ();
               assertCondition (check == true,sb);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
"lob threshold" property - Setting to greater than the maximum.

SQL400 - See note in variation 8 for details on the driver different here.
**/
   public void Var009()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               String url = baseURL_
                            + ";user=" + systemObject_.getUserId()
                            + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) ;

               if (getDriver () == JDTestDriver.DRIVER_NATIVE)
                               url += ";lob threshold=500001";
               else
                               url += ";lob threshold=4194305";

               Connection c = testDriver_.getConnection(url,systemObject_.getUserId(),encryptedPassword_);
               c.setAutoCommit(false);
               c.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
               Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);                
               ResultSet rs = s.executeQuery ("SELECT * FROM " + TABLE_);
               rs.absolute (3);
               Blob blob = rs.getBlob ("C_BLOB");
               InputStream v = blob.getBinaryStream ();
//               boolean check = compare (v, LARGE_);
               sb.setLength(0); 
               boolean check = 	(getDriver() == JDTestDriver.DRIVER_NATIVE &&
				 getRelease() >= JDTestDriver.RELEASE_V7R1M0) ?
		 compareBeginsWithBytes( v, LARGE_,sb):
		 compare (v, LARGE_,sb);

               rs.close ();
               s.close ();
               c.close ();
               assertCondition (check == true);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
"lob threshold" property - Setting to WAY greater than the maximum.
**/
   public void Var010()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               String url = baseURL_
                            + ";user=" + systemObject_.getUserId()
                            + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                            + ";lob threshold=41943054454565645645";
               Connection c = testDriver_.getConnection(url,systemObject_.getUserId(),encryptedPassword_);
               c.setAutoCommit(false);
               c.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
               Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);                
               ResultSet rs = s.executeQuery ("SELECT * FROM " + TABLE_);
               rs.absolute (3);
               Blob blob = rs.getBlob ("C_BLOB");
               InputStream v = blob.getBinaryStream ();
//               boolean check = compare (v, LARGE_);
               sb.setLength(0); 
               boolean check = 	(getDriver() == JDTestDriver.DRIVER_NATIVE &&
				 getRelease() >= JDTestDriver.RELEASE_V7R1M0) ?
		 compareBeginsWithBytes( v, LARGE_,sb):
		 compare (v, LARGE_,sb);

               rs.close ();
               s.close ();
               c.close ();
               assertCondition (check == true);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


}



