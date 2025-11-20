///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDLobInputStream.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.JD.Lob;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDLobTest;
import test.JDTestDriver;
import test.JDTestcase;
import test.PasswordVault;



/**
Testcase JDLobInputStream.  This tests the following method
of the JDBC InputStream class:

<ul>
<li>available()
<li>close()
<li>mark()
<li>markSupported()
<li>read()
<li>read(byte[],int,int)
<li>read(byte[])
<li>reset()
<li>skip()
</ul>
**/
public class JDLobInputStream
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDLobInputStream";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDLobTest.main(newArgs); 
   }



   // Private data.
   private Statement           statement_;

   public static String TABLE_          = JDLobTest.COLLECTION + ".IS";
   public static String  TABLE2_         = JDLobTest.COLLECTION + ".CIS";

   public static       byte[]  MEDIUM_         = null; // final
   public static       byte[]  LARGE_          = null; // final
   public static final int     WIDTH_          = 30000;
   public static       String  CHAR_MEDIUM_    = null;
   public static       String  CHAR_LARGE_     = null;



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


      //Change a few positions in large so we can make sure we read the correct bytes
      LARGE_[0] = (byte) '_';           //@K1A
      LARGE_[4500] = (byte) '`';        //@K1A
      LARGE_[WIDTH_ - 1] = (byte) 'a';  //@K1A

      // CHAR_MEDIUM_ is most of the alphabet twice.
      CHAR_MEDIUM_ = new String("abcdefghigklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWX");

      // CHAR_LARGE_ is all the numbers repeated 3000 times.
      CHAR_LARGE_ = "";
      for (int i = 1; i <= 3000; i++)
         CHAR_LARGE_ = CHAR_LARGE_ + "0123456789";

   }



/**
Constructor.
**/
   public JDLobInputStream (AS400 systemObject,
                            Hashtable<String,Vector<String>> namesAndVars,
                            int runMode,
                            FileOutputStream fileOutputStream,
                            String password)
   {
      super (systemObject, "JDLobInputStream",
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
       TABLE_          = JDLobTest.COLLECTION + ".IS";
       TABLE2_         = JDLobTest.COLLECTION + ".CIS";
       
      if (isJdbc20 ()) {
         if (areLobsSupported ()) {
            String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
            connection_ = testDriver_.getConnection(url,systemObject_.getUserId(),encryptedPassword_);
            connection_.setAutoCommit(false);
            connection_.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            statement_ = connection_.createStatement ();

            statement_.executeUpdate ("CREATE TABLE " + TABLE_ 
                                      + "(C_BLOB BLOB(" + WIDTH_ + "))");

            PreparedStatement ps = connection_.prepareStatement ("INSERT INTO " 
                                                                 + TABLE_ + " (C_BLOB) VALUES (?)");
            ps.setBytes (1, new byte[0]);
            ps.executeUpdate ();
            ps.setBytes (1, MEDIUM_);
            ps.executeUpdate ();
            ps.setBytes (1, LARGE_);
            ps.executeUpdate ();
            ps.close ();            


            statement_.executeUpdate ("CREATE TABLE " + TABLE2_ 
                                      + "(C_CLOB CLOB(" + WIDTH_ + "))");

            ps = connection_.prepareStatement ("INSERT INTO " + TABLE2_ + " (C_CLOB) VALUES (?)");
            ps.setString (1, "");
            ps.executeUpdate ();
            ps.setString (1, CHAR_MEDIUM_);
            ps.executeUpdate ();
            ps.setString (1, CHAR_LARGE_);
            ps.executeUpdate ();
            ps.close ();

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
            statement_.executeUpdate ("DROP TABLE " + TABLE_);
            statement_.executeUpdate ("DROP TABLE " + TABLE2_);

            statement_.close ();
            connection_.close ();
            connection_ = null; 

         }
      }
   }


/**
available() - When the lob is empty and nothing read.
**/
   public void Var001()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               assertCondition (v.available() == 0);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
available() - When the lob is not empty and nothing read.
**/
   public void Var002()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               assertCondition (v.available() == MEDIUM_.length);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
available() - When the lob is not empty and 1 byte
has been read.
**/
   public void Var003()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               v.read ();
               assertCondition (v.available() == (MEDIUM_.length - 1));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
available() - When the lob is not empty and all but 1 byte
have been read.
**/
   public void Var004()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[MEDIUM_.length - 1];
               v.read (p);
               assertCondition (v.available() == 1);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
available() - When the lob is not empty and all bytes
have been read.
**/
   public void Var005()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[MEDIUM_.length];
               v.read (p);
               assertCondition (v.available() == 0);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
available() - When the input stream is closed.
**/
   public void Var006()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               v.close ();
               assertCondition (v.available() == 0);
            } 
            catch (Exception e) 
            {
                String exception = e.getClass().getName();
                
                if  (exception.indexOf("IOException") > 0)
                   succeeded();
                else
                   failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
close() - When the input stream is empty.
**/
   public void Var007()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               v.close ();
               succeeded ();
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
close() - When the input stream is not empty.
**/
   public void Var008()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               v.close ();
               succeeded ();
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
close() - When the input stream is already closed.
**/
   public void Var009()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               v.close ();
               v.close ();
               succeeded ();
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
mark() - Should have no effect.
**/
   public void Var010()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               v.mark (1);
               succeeded ();
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
markSupported() - Should return true.

SQL400 - Native JDBC driver supports the mark function.
**/
   public void Var011()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");

                  assertCondition (v.markSupported () == true);

            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read() - When the lob is empty and nothing read.
**/
   public void Var012()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               assertCondition (v.read() == -1);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read() - When the lob is not empty and nothing read.
**/
   public void Var013()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               assertCondition (v.read() == 0);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read() - When the lob is not empty and 1 byte
has been read.

SQL400 - The native JDBC driver will mask sign extension here (you need to for
serialization to work).  We are following the pattern of java.io.ByteArrayInputStream.java
in doing this. 
**/
   public void Var014()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               v.read ();

          //     if (getDriver () == JDTestDriver.DRIVER_NATIVE) 
          //        assertCondition (v.read() == 254);
          //     else 
          //        assertCondition (v.read() == -2);
               assertCondition (v.read() == 254);

            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read() - When the lob is not empty and all but 1 byte
have been read.

SQL400 - The native JDBC driver will mask sign extension here (you need to for
serialization to work).  We are following the pattern of java.io.ByteArrayInputStream.java
in doing this. 
**/
   public void Var015()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[MEDIUM_.length - 1];
               v.read (p);

               //if (getDriver () == JDTestDriver.DRIVER_NATIVE)
               //   assertCondition (v.read() == 158);
               //else 
               //   assertCondition (v.read() == -98);

               assertCondition (v.read() == 158);
               
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read() - When the lob is not empty and all bytes
have been read.
**/
   public void Var016()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[MEDIUM_.length];
               v.read (p);
               assertCondition (v.read() == -1);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read() - When the input stream is closed.
**/
   public void Var017()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               v.close ();
               v.read ();
               failed ("Didn't throw SQLException");
            } catch (Exception e) {
               assertExceptionIsInstanceOf (e, "java.io.IOException");
            }
         }
      }
   }


/**
read(byte[],int,int) - When the lob is empty and nothing read,
reading 0 bytes.
**/
   public void Var018()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[10];                
               assertCondition (v.read(p, 0, 0) == 0);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[],int,int) - When the lob is empty and nothing read,
reading more than 0 bytes.
**/
   public void Var019()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[10];                
               assertCondition (v.read(p, 0, 5) == -1);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[],int,int) - When the lob is not empty and nothing read, reading
0 bytes.
**/
   public void Var020()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[0];
               assertCondition (v.read(p, 0, 0) == 0);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[],int,int) - When the lob is not empty and nothing read, reading
1 byte into the start of the array.
**/
   public void Var021()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[10];
               int count = v.read(p, 0, 1);
               assertCondition ((count == 1) && (p[0] == 0));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[],int,int) - When the lob is not empty and nothing read, reading
1 byte into the middle of the array.
**/
   public void Var022()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[10];
               int count = v.read(p, 5, 1);
               assertCondition ((count == 1) && (p[5] == 0));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[],int,int) - When the lob is not empty and nothing read, reading
1 byte into the end of the array.
**/
   public void Var023()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[10];
               int count = v.read(p, 9, 1);
               assertCondition ((count == 1) && (p[9] == 0));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[],int,int) - When the lob is not empty and some have been read, reading
1 byte into the start of the array.
**/
   public void Var024()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[10];
               v.read (p);
               int count = v.read(p, 0, 1);
               assertCondition ((count == 1) && (p[0] == -20));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[],int,int) - When the lob is not empty and some have been read, reading
1 byte into the middle of the array.
**/
   public void Var025()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[10];
               v.read (p);
               int count = v.read(p, 5, 1);
               assertCondition ((count == 1) && (p[5] == -20));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[],int,int) - When the lob is not empty and some have been read, reading
several bytes into the start of the array.
**/
   public void Var026()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[10];
               v.read (p);
               int count = v.read(p, 0, 5);
               assertCondition ((count == 5) && (p[0] == -20) && (p[4] == -28));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[],int,int) - When the lob is not empty and some have been read, reading
several bytes into the middle of the array.
**/
   public void Var027()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[10];
               v.read (p);
               int count = v.read(p, 3, 5);
               assertCondition ((count == 5) && (p[3] == -20) && (p[7] == -28));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[],int,int) - When the lob is not empty and some have been read, reading
several bytes into the end of the array.
**/
   public void Var028()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[10];
               v.read (p);
               int count = v.read(p, 5, 5);
               assertCondition ((count == 5) && (p[5] == -20) && (p[9] == -28));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[],int,int) - When the lob is not empty and reading the entire
lob.
**/
   public void Var029()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[MEDIUM_.length];
               int count = v.read(p, 0, p.length);
               assertCondition ((count == MEDIUM_.length) && (p[0] == 0) && (p[MEDIUM_.length-1] == -98));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[],int,int) - When the lob is not empty and reading after the
the entire lob has been read.
**/
   public void Var030()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[MEDIUM_.length];
               v.read(p, 0, p.length);
               int count = v.read (p, 0, p.length);
               assertCondition (count == -1);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[],int,int) - Passing null for the byte array.
**/
   public void Var031()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[10];
               v.read (p);
               int count = v.read(null, 3, 5);
               failed ("Didn't throw Exception"+count);
            } catch (Exception e) {
               assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
            }
         }
      }
   }


/**
read(byte[],int,int) - Passing -1 for offset.
**/
   public void Var032()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[10];
               v.read (p);
               int count = v.read(p, -1, 5);
               failed ("Didn't throw Exception"+count);
            } 
            catch (Exception e) 
            {
                String exception = e.getClass().getName();
                
                if  ((exception.indexOf("ExtendedIllegalArgumentException") > 0) ||
                     (exception.indexOf("IndexOutOfBoundsException") > 0))
                   succeeded();
                else
                   failed (e, "Unexpected Exception");                                         
            }      
         }
      }
   }


/**
read(byte[],int,int) - Passing offset greater than length of the array.
**/
   public void Var033()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[10];
               v.read (p);
               int count = v.read(p, 12, 5);
               failed ("Didn't throw Exception"+count);
            } 
            catch (Exception e) 
            {
                String exception = e.getClass().getName();
                
                if  ((exception.indexOf("ExtendedIllegalArgumentException") > 0) ||
                     (exception.indexOf("IndexOutOfBoundsException") > 0))
                   succeeded();
                else
                   failed (e, "Unexpected Exception");                                         
            }      
         }
      }
   }


/**
read(byte[],int,int) - Passing -1 for length.
**/
   public void Var034()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[10];
               v.read (p);
               int count = v.read(p, 3, -1);
               failed ("Didn't throw Exception"+count);
            } 
            catch (Exception e) 
            {
                String exception = e.getClass().getName();
                
                if  ((exception.indexOf("ExtendedIllegalArgumentException") > 0) ||
                     (exception.indexOf("IndexOutOfBoundsException") > 0))
                   succeeded();
                else
                   failed (e, "Unexpected Exception");                                         
            }      
         }
      }
   }


/**
read(byte[],int,int) - Passing length that extends past the end of the array.
**/
   public void Var035()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[10];
               v.read (p);
               int count = v.read(p, 4, 7);
               failed ("Didn't throw Exception"+count);
            } 
            catch (Exception e) 
            {
                String exception = e.getClass().getName();
                
                if  ((exception.indexOf("ExtendedIllegalArgumentException") > 0) ||
                     (exception.indexOf("IndexOutOfBoundsException") > 0))
                   succeeded();
                else
                   failed (e, "Unexpected Exception");                                         
            }      
         }
      }
   }


/**
read(byte[],int,int) - When the input stream is closed.
**/
   public void Var036()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               v.close ();
               byte[] p = new byte[10];
               int count = v.read(p, 5, 5);
               failed ("Didn't throw Exception"+count);
            } catch (Exception e) {
               assertExceptionIsInstanceOf (e, "java.io.IOException");
            }
         }
      }
   }


/**
read(byte[]) - When the lob is empty and nothing read,
reading 0 bytes.
**/
   public void Var037()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[10];                
               assertCondition (v.read(p) == -1);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[]) - When the lob is not empty and nothing read, reading
0 bytes.
**/
   public void Var038()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[0];
               assertCondition (v.read(p) == 0);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[]) - When the lob is not empty and nothing read, reading
1 byte.
**/
   public void Var039()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[1];
               int count = v.read(p);
               assertCondition ((count == 1) && (p[0] == 0));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[]) - When the lob is not empty and some have been read, reading
1 byte.
**/
   public void Var040()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[1];
               v.read (p);
               int count = v.read(p);
               assertCondition ((count == 1) && (p[0] == -2));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[]) - When the lob is not empty and some have been read, reading
several bytes.
**/
   public void Var041()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[10];
               v.read (p);
               int count = v.read(p);
               assertCondition ((count == 10) && (p[0] == -20) && (p[9] == -38));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[]) - When the lob is not empty and some have been read, reading
several bytes when not all are available.
**/
   public void Var042()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[30];
               v.read (p);
               int count = v.read(p);
               assertCondition ((count == 20) && (p[0] == -60) && (p[19] == -98));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[]) - When the lob is not empty and reading the entire
lob.
**/
   public void Var043()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[MEDIUM_.length];
               int count = v.read(p);
               assertCondition ((count == MEDIUM_.length) && (p[0] == 0) && (p[MEDIUM_.length-1] == -98));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[]) - When the lob is not empty and reading after the
the entire lob has been read.
**/
   public void Var044()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[MEDIUM_.length];
               v.read(p);
               int count = v.read (p);
               assertCondition (count == -1);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[]) - Passing null for the byte array.
**/
   public void Var045()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[10];
               v.read (p);
               int count = v.read(null);
               failed ("Didn't throw SQLException"+count);
            } catch (Exception e) {
               assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
            }
         }
      }
   }


/**
read(byte[]) - When the input stream is closed.
**/
   public void Var046()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               v.close ();
               byte[] p = new byte[10];
               int count = v.read(p);
               failed ("Didn't throw SQLException"+count);
            } catch (Exception e) {
               assertExceptionIsInstanceOf (e, "java.io.IOException");
            }
         }
      }
   }


/**
reset() - Should have no effect.
**/
   public void Var047()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               v.reset ();
               succeeded ();
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }



/**
skip() - When the lob is empty and nothing read,
skipping 0 bytes.
**/
   public void Var048()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               long count = v.skip (0);                
               assertCondition ((v.read() == -1) && (count == 0));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
skip() - When the lob is not empty and nothing read, skipping
0 bytes.
**/
   public void Var049()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               long count = v.skip (0);
               assertCondition ((v.read() == 0) && (count == 0));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
skip() - When the lob is not empty and nothing read, skipping
1 byte.

SQL400 - The native JDBC driver will mask sign extension here (you need to for
serialization to work).  We are following the pattern of java.io.ByteArrayInputStream.java
in doing this. 
**/
   public void Var050()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               long count = v.skip(1);

               // if (getDriver () == JDTestDriver.DRIVER_NATIVE) 
               //    assertCondition ((v.read () == 254) && (count == 1));
               // else 
               //    assertCondition ((v.read () == -2) && (count == 1));

               assertCondition ((v.read () == 254) && (count == 1));

               
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
skip() - When the lob is not empty and some have been read, reading
1 byte.

SQL400 - The native JDBC driver will mask sign extension here (you need to for
serialization to work).  We are following the pattern of java.io.ByteArrayInputStream.java
in doing this. 
**/
   public void Var051()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[10];
               v.read (p);
               long count = v.skip (1);
               
               // if (getDriver () == JDTestDriver.DRIVER_NATIVE) 
               //    assertCondition ((v.read () == 234) && (count == 1));
               // else 
               //    assertCondition ((v.read () == 234) && (count == 1));

               assertCondition ((v.read () == 234) && (count == 1));
   
             } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
skip() - When the lob is not empty and some have been read, skipping
several bytes.

SQL400 - The native JDBC driver will mask sign extension here (you need to for
serialization to work).  We are following the pattern of java.io.ByteArrayInputStream.java
in doing this. 
**/
   public void Var052()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[10];
               v.read (p);
               long count = v.skip(5);

               // if (getDriver () == JDTestDriver.DRIVER_NATIVE) 
               //    assertCondition ((count == 5) && (v.read () == 226));
               // else 
               //    assertCondition ((count == 5) && (v.read () == -30));

               assertCondition ((count == 5) && (v.read () == 226));
               
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
skip() - When the lob is not empty and some have been read, skipping
more bytes than are available.

SQL400 - The native driver expects count to be 20 as 30 bytes of a 50
         byte stream have been read.  That means there are only 20 
         bytes left to skip.
**/
   public void Var053()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[30];
               v.read (p);
               long count = v.skip (30);

               assertCondition (count == 20);
                  
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
skip() - When the lob is not empty and skipping the entire
lob.
**/
   public void Var054()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[MEDIUM_.length];
               long count = v.skip (MEDIUM_.length);
               assertCondition ((count == MEDIUM_.length) && (v.read() == -1), "p.length="+p.length);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
skip() - When the lob is not empty and skipping after the
the entire lob has been read.

SQL400 - The native driver expects this count to be 0.  The
         entire stream has already been read so there is 
         nothing to skip.
**/
   public void Var055()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[MEDIUM_.length];
               v.read(p);
               long count = v.skip (15);

               assertCondition (count == 0);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
skip() - Passing -1 for the argument.
**/
   public void Var056()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               byte[] p = new byte[10];
               v.read (p);
               long count = v.skip (-1);
               failed ("Didn't throw Exception"+count);
            } catch (Exception e) {
               assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
            }
         }
      }
   }


/**
skip() - When the input stream is closed.
**/
   public void Var057()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               v.close ();
               long count = v.skip (4);
               failed ("Didn't throw Exception"+count);
            } catch (Exception e) {
               assertExceptionIsInstanceOf (e, "java.io.IOException");
            }
         }
      }
   }



/**
available() - When the lob is empty and nothing read.

SQL400 - Clob variations.
**/
   public void Var058()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               assertCondition (v.available() == 0);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }



/**
available() - When the lob is not empty and nothing read.

SQL400 - Clob variations.
**/
   public void Var059()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");

               // This method is not currently supported.
	       int a = v.available();

	       if (getDriver () == JDTestDriver.DRIVER_NATIVE)
	       {
		   if (a == 50) succeeded();
		   else failed("Bytes available is "+a+" instead of 50.");
	       }
	       else
	       {
	            //@CRS - For a locator, this returns 0, since we have to go
                    // to the server to read bytes, there are none available yet.
		   if (a == 0) succeeded();
		   else failed("Bytes available is "+a+" instead of 0.");
	       }

            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
available() - When the lob is not empty and 1 byte
has been read.

SQL400 - Clob variations.
**/
   public void Var060()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               v.read ();

               assertCondition (v.available() == 49);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
available() - When the lob is not empty and all but 1 byte
have been read.

SQL400 - Clob variations.
**/
   public void Var061()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               byte[] p = new byte[MEDIUM_.length - 1];
               v.read (p);

               // This method is not currently supported.
               assertCondition (v.available() == 1);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
available() - When the lob is not empty and all bytes
have been read.

SQL400 - Clob variations.
**/
   public void Var062()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               byte[] p = new byte[MEDIUM_.length];
               v.read (p);
               assertCondition (v.available() == 0);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
available() - When the input stream is closed.

SQL400 - Clob variations.
**/
   public void Var063()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               v.close ();

               assertCondition (v.available() == 0);
            }   
            catch (Exception e) 
            {
                String exception = e.getClass().getName();
                
                if  (exception.indexOf("IOException") > 0)
                   succeeded();
                else
                   failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
close() - When the input stream is empty.

SQL400 - Clob variations.
**/
   public void Var064()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               v.close ();
               succeeded ();
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
close() - When the input stream is not empty.

SQL400 - Clob variations.
**/
   public void Var065()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               v.close ();
               succeeded ();
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
close() - When the input stream is already closed.

SQL400 - Clob variations.
**/
   public void Var066()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               v.close ();
               v.close ();
               succeeded ();
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
mark() - Should have no effect.

SQL400 - Clob variations.
**/
   public void Var067()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               v.mark (1);
               succeeded ();
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
markSupported() - Should return false.

SQL400 - Clob variations.

SQL400 - Native driver supports the mark function.
**/
   public void Var068()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");

               if (getDriver () == JDTestDriver.DRIVER_NATIVE)
                  assertCondition (v.markSupported () == true);
               else
                  assertCondition (v.markSupported () == false);

            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }



/**
read() - When the lob is empty and nothing read.

SQL400 - Clob variations.
**/
   public void Var069()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               assertCondition (v.read() == -1);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }



/**
read() - When the lob is not empty and nothing read.

SQL400 - Clob variations.
**/
   public void Var070()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");

               assertCondition (v.read() == 97);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }



/**
read() - When the lob is not empty and 1 byte
has been read.

SQL400 - Clob variations.
**/
   public void Var071()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               v.read ();

               assertCondition (v.read() == 98);

            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }



   /**
read() - When the lob is not empty and all but 1 byte
have been read.

SQL400 - Clob variations.
**/
   public void Var072()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               byte[] p = new byte[MEDIUM_.length - 1];
               v.read (p);

               assertCondition (v.read() == 88);


            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read() - When the lob is not empty and all bytes
have been read.

SQL400 - Clob variations.
**/
   public void Var073()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               byte[] p = new byte[MEDIUM_.length];
               v.read (p);

               assertCondition (v.read() == -1);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read() - When the input stream is closed.

SQL400 - Clob variations.
**/
   public void Var074()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               v.close ();
               v.read ();
               failed ("Didn't throw SQLException");
            } catch (Exception e) {
               assertExceptionIsInstanceOf (e, "java.io.IOException");
            }
         }
      }
   }


/**
read(byte[],int,int) - When the lob is empty and nothing read,
reading 0 bytes.

SQL400 - Clob variations.
**/
   public void Var075()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               byte[] p = new byte[10];                
               assertCondition (v.read(p, 0, 0) == 0);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[],int,int) - When the lob is empty and nothing read,
reading more than 0 bytes.

SQL400 - Clob variations.
**/
   public void Var076()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               byte[] p = new byte[10];                
               assertCondition (v.read(p, 0, 5) == -1);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[],int,int) - When the lob is not empty and nothing read, reading
0 bytes.

SQL400 - Clob variations.
**/
   public void Var077()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               byte[] p = new byte[0];
               assertCondition (v.read(p, 0, 0) == 0);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[],int,int) - When the lob is not empty and nothing read, reading
1 byte into the start of the array.

SQL400 - Clob variations.
**/
   public void Var078()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               byte[] p = new byte[10];
               int count = v.read(p, 0, 1);
               assertCondition ((count == 1) && (p[0] == 97));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[],int,int) - When the lob is not empty and nothing read, reading
1 byte into the middle of the array.

SQL400 - Clob variations.
**/
   public void Var079()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               byte[] p = new byte[10];
               int count = v.read(p, 5, 1);
               assertCondition ((count == 1) && (p[5] == 97));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[],int,int) - When the lob is not empty and nothing read, reading
1 byte into the end of the array.

SQL400 - Clob variations.
**/
   public void Var080()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               byte[] p = new byte[10];
               int count = v.read(p, 9, 1);
               assertCondition ((count == 1) && (p[9] == 97));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[],int,int) - When the lob is not empty and some have been read, reading
1 byte into the start of the array.

SQL400 - Clob variations.
**/
   public void Var081()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               byte[] p = new byte[10];
               v.read (p);
               int count = v.read(p, 0, 1);
               assertCondition ((count == 1) && (p[0] == 107));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[],int,int) - When the lob is not empty and some have been read, reading
1 byte into the middle of the array.

SQL400 - Clob variations.
**/
   public void Var082()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               byte[] p = new byte[10];
               v.read (p);
               int count = v.read(p, 5, 1);
               assertCondition ((count == 1) && (p[5] == 107));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[],int,int) - When the lob is not empty and some have been read, reading
several bytes into the start of the array.

SQL400 - Clob variations.
**/
   public void Var083()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               byte[] p = new byte[10];
               v.read (p);
               int count = v.read(p, 0, 5);
               assertCondition ((count == 5) && (p[0] == 107) && (p[4] == 111));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[],int,int) - When the lob is not empty and some have been read, reading
several bytes into the middle of the array.

SQL400 - Clob variations.
**/
   public void Var084()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               byte[] p = new byte[10];
               v.read (p);
               int count = v.read(p, 3, 5);
               assertCondition ((count == 5) && (p[3] == 107) && (p[7] == 111));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[],int,int) - When the lob is not empty and some have been read, reading
several bytes into the end of the array.

SQL400 - Clob variations.
**/
   public void Var085()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               byte[] p = new byte[10];
               v.read (p);
               int count = v.read(p, 5, 5);
               assertCondition ((count == 5) && (p[5] == 107) && (p[9] == 111));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[],int,int) - When the lob is not empty and reading the entire
lob.

SQL400 - Clob variations.
**/
   public void Var086()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               byte[] p = new byte[MEDIUM_.length];
               int count = v.read(p, 0, p.length);
               assertCondition ((count == MEDIUM_.length) && (p[0] == 97) && (p[MEDIUM_.length-1] == 88));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[],int,int) - When the lob is not empty and reading after the
the entire lob has been read.

SQL400 - Clob variations.
**/
   public void Var087()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               byte[] p = new byte[MEDIUM_.length];
               v.read(p, 0, p.length);
               int count = v.read (p, 0, p.length);
               assertCondition (count == -1);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[],int,int) - Passing null for the byte array.

SQL400 - Clob variations.
**/
   public void Var088()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               byte[] p = new byte[10];
               v.read (p);
               int count = v.read(null, 3, 5);
               failed ("Didn't throw Exception"+count);
            } catch (Exception e) {
               assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
            }
         }
      }
   }


/**
read(byte[],int,int) - Passing -1 for offset.

SQL400 - Clob variations.
**/
   public void Var089()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               byte[] p = new byte[10];
               v.read (p);
               int count = v.read(p, -1, 5);
               failed ("Didn't throw Exception"+count);
            } 
            catch (Exception e) 
            {
                String exception = e.getClass().getName();
                
                if  ((exception.indexOf("ExtendedIllegalArgumentException") > 0) ||
                     (exception.indexOf("IndexOutOfBoundsException") > 0))
                   succeeded();
                else
                   failed (e, "Unexpected Exception");                                         
            }      
         }
      }
   }


/**
read(byte[],int,int) - Passing offset greater than length of the array.

SQL400 - Clob variations.
**/
   public void Var090()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
           int count = 0; 
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               byte[] p = new byte[10];
               v.read (p);
               count = v.read(p, 12, 5);
             } 
             catch (Exception e) 
             {
                 String exception = e.getClass().getName();
                 
                 if  ((exception.indexOf("ExtendedIllegalArgumentException") > 0) ||
                      (exception.indexOf("IndexOutOfBoundsException") > 0))
                    succeeded();
                 else
                    failed (e, "Unexpected Exception"+count);                                         
              }      
          }
      }
   }


/**
read(byte[],int,int) - Passing -1 for length.

SQL400 - Clob variations.
**/
   public void Var091()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               byte[] p = new byte[10];
               v.read (p);
               int count = v.read(p, 3, -1);
               failed ("Didn't throw Exception"+count);
            } 
            catch (Exception e) 
            {
                String exception = e.getClass().getName();
                
                if  ((exception.indexOf("ExtendedIllegalArgumentException") > 0) ||
                     (exception.indexOf("IndexOutOfBoundsException") > 0))
                   succeeded();
                else
                   failed (e, "Unexpected Exception");                                         
            }      
         }
      }
   }


/**
read(byte[],int,int) - Passing length that extends past the end of the array.

SQL400 - Clob variations.
**/
   public void Var092()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               byte[] p = new byte[10];
               v.read (p);
               int count = v.read(p, 4, 7);
               failed ("Didn't throw Exception"+count);
            } 
            catch (Exception e) 
            {
                String exception = e.getClass().getName();
                
                if  ((exception.indexOf("ExtendedIllegalArgumentException") > 0) ||
                     (exception.indexOf("IndexOutOfBoundsException") > 0))
                   succeeded();
                else
                   failed (e, "Unexpected Exception");                                         
            }      
         }
      }
   }


/**
read(byte[],int,int) - When the input stream is closed.

SQL400 - Clob variations.
**/
   public void Var093()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               v.close ();
               byte[] p = new byte[10];
               int count = v.read(p, 5, 5);
               failed ("Didn't throw Exception"+count);
            } catch (Exception e) {
               assertExceptionIsInstanceOf (e, "java.io.IOException");
            }
         }
      }
   }


/**
read(byte[]) - When the lob is empty and nothing read,
reading 0 bytes.

SQL400 - Clob variations.
**/
   public void Var094()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               byte[] p = new byte[10];                
               assertCondition (v.read(p) == -1);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[]) - When the lob is not empty and nothing read, reading
0 bytes.

SQL400 - Clob variations.
**/
   public void Var095()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               byte[] p = new byte[0];
               assertCondition (v.read(p) == 0);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[]) - When the lob is not empty and nothing read, reading
1 byte.

SQL400 - Clob variations.
**/
   public void Var096()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               byte[] p = new byte[1];
               int count = v.read(p);
               assertCondition ((count == 1) && (p[0] == 97));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[]) - When the lob is not empty and some have been read, reading
1 byte.

SQL400 - Clob variations.
**/
   public void Var097()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               byte[] p = new byte[1];
               v.read (p);
               int count = v.read(p);
               assertCondition ((count == 1) && (p[0] == 98));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[]) - When the lob is not empty and some have been read, reading
several bytes.

SQL400 - Clob variations.
**/
   public void Var098()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               byte[] p = new byte[10];
               v.read (p);
               int count = v.read(p);
               assertCondition ((count == 10) && (p[0] == 107) && (p[9] == 116));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[]) - When the lob is not empty and some have been read, reading
several bytes when not all are available.

SQL400 - Clob variations.
**/
   public void Var099()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               byte[] p = new byte[30];
               v.read (p);
               int count = v.read(p);
               assertCondition ((count == 20) && (p[0] == 69) && (p[19] == 88));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[]) - When the lob is not empty and reading the entire
lob.

SQL400 - Clob variations.
**/
   public void Var100()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               byte[] p = new byte[MEDIUM_.length];
               int count = v.read(p);
               assertCondition ((count == MEDIUM_.length) && (p[0] == 97) && (p[MEDIUM_.length-1] == 88));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[]) - When the lob is not empty and reading after the
the entire lob has been read.

SQL400 - Clob variations.
**/
   public void Var101()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               byte[] p = new byte[MEDIUM_.length];
               v.read(p);
               int count = v.read (p);
               assertCondition (count == -1);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
read(byte[]) - Passing null for the byte array.

SQL400 - Clob variations.
**/
   public void Var102()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               byte[] p = new byte[10];
               v.read (p);
               int count = v.read(null);
               failed ("Didn't throw SQLException"+count);
            } catch (Exception e) {
               assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
            }
         }
      }
   }


/**
read(byte[]) - When the input stream is closed.

SQL400 - Clob variations.
**/
   public void Var103()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               v.close ();
               byte[] p = new byte[10];
               int count = v.read(p);
               failed ("Didn't throw SQLException"+count);
            } catch (Exception e) {
               assertExceptionIsInstanceOf (e, "java.io.IOException");
            }
         }
      }
   }


/**
reset() - Should have no effect.

SQL400 - Clob variations.
**/
   public void Var104()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               if (v.markSupported()) v.reset(); //@CRS - Only do this if mark is supported.
               succeeded ();
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }



/**
skip() - When the lob is empty and nothing read,
skipping 0 bytes.

SQL400 - Clob variations.
**/
   public void Var105()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               long count = v.skip (0);                
               assertCondition ((v.read() == -1) && (count == 0));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
skip() - When the lob is not empty and nothing read, skipping
0 bytes.

SQL400 - Clob variations.
**/
   public void Var106()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               long count = v.skip (0);
               assertCondition ((v.read() == 97) && (count == 0));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
skip() - When the lob is not empty and nothing read, skipping
1 byte.

SQL400 - Clob variations.
**/
   public void Var107()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               long count = v.skip(1);
               assertCondition ((v.read () == 98) && (count == 1));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
skip() - When the lob is not empty and some have been read, reading
1 byte.

SQL400 - Clob variations.
**/
   public void Var108()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               byte[] p = new byte[10];
               v.read (p);
               long count = v.skip (1);
               assertCondition ((v.read () == 108) && (count == 1));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
skip() - When the lob is not empty and some have been read, skipping
several bytes.

SQL400 - Clob variations.
**/
   public void Var109()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               byte[] p = new byte[10];
               v.read (p);
               long count = v.skip(5);
               assertCondition ((count == 5) && (v.read () == 112));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
skip() - When the lob is not empty and some have been read, skipping
more bytes than are available.

SQL400 - The native driver expects count to be 20 as 30 bytes of a 50
         byte stream have been read.  That means there are only 20 
         bytes left to skip.

SQL400 - Clob variations.
**/
   public void Var110()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               byte[] p = new byte[30];
               v.read (p);
               long count = v.skip (30);

               assertCondition (count == 20);

            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
skip() - When the lob is not empty and skipping the entire
lob.

SQL400 - Clob variations.
**/
   public void Var111()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               long count = v.skip (MEDIUM_.length);
               assertCondition ((count == MEDIUM_.length) && (v.read() == -1));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
skip() - When the lob is not empty and skipping after the
the entire lob has been read.

SQL400 - The native driver expects this count to be 0.  The
         entire stream has already been read so there is 
         nothing to skip.

SQL400 - Clob variations.
**/
   public void Var112()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               byte[] p = new byte[MEDIUM_.length];
               v.read(p);
               long count = v.skip (15);

               assertCondition (count == 0);

            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }


/**
skip() - Passing -1 for the argument.

SQL400 - Clob variations.
**/
   public void Var113()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               byte[] p = new byte[10];
               v.read (p);
               long count = v.skip (-1);
               failed ("Didn't throw Exception"+count);
            } catch (Exception e) {
               assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
            }
         }
      }
   }


/**
skip() - When the input stream is closed.

SQL400 - Clob variations.
**/
   public void Var114()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE2_);
               rs.next ();
               rs.next ();
               InputStream v = rs.getAsciiStream ("C_CLOB");
               v.close ();
               long count = v.skip (4);
               failed ("Didn't throw Exception"+count);
            } catch (Exception e) {
               assertExceptionIsInstanceOf (e, "java.io.IOException");
            }
         }
      }
   }

//@K1A
/**
read(byte[]) - When the lob is not empty and some have been read, reading
1 byte.
**/
   public void Var115()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next();
               rs.next();
               rs.next();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               int length = LARGE_.length;
               byte[] p = new byte[length];
               int count = v.read(p);
               boolean successful = true;
               int failedAt = -1;
               for(int i=0; i<length; i++)
                {
                    if(LARGE_[i] != p[i])
                    {
                        successful = false;
                        failedAt = i;
                        break;  //Exit after first wrong character
                    }
                }
               assertCondition ((count == length) && (successful), "wrong value found at position " + failedAt +", added by toolbox 2/25/2004");
            } catch (Exception e) {
               failed (e, "Unexpected Exception, Added by Toolbox 2/25/2004");
            }
         }
      }
   }

//@K1A
/**
read(byte[], int, int) - When the lob is not empty and some have been read, reading
1 byte.
**/
   public void Var116()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next();
               rs.next();
               rs.next();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               int length = LARGE_.length;
               byte[] p = new byte[length];
               int count = v.read(p, 0, length);
               boolean successful = true;
               int failedAt = -1;
               for(int i=0; i<length; i++)
                {
                    if(LARGE_[i] != p[i])
                    {
                        successful = false;
                        failedAt = i;
                        break;  //Exit after first wrong character
                    }
                }
               assertCondition ((count == length) && (successful), "wrong value found at position " + failedAt+ ", Added by toolbox 2/25/2004");
            } catch (Exception e) {
               failed (e, "Unexpected Exception, Added by Toolbox 2/25/2004");
            }
         }
      }
   }

//@K1A
/**
read() - When the lob is not empty and some have been read, reading
1 byte.
**/
   public void Var117()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               ResultSet rs = statement_.executeQuery ("SELECT * FROM " + TABLE_);
               rs.next();
               rs.next();
               rs.next();
               InputStream v = rs.getBinaryStream ("C_BLOB");
               int length = LARGE_.length;
               boolean successful = true;
               int failedAt = -1;
               for(int i=0; i<length; i++)
                {
                    if(LARGE_[i] != v.read())
                    {
                        successful = false;
                        failedAt = i;
                        break;  //Exit after first wrong character
                    }
                }
               assertCondition (successful, "wrong value found at position " + failedAt + ", Added by toolbox 2/24/2004");
            } catch (Exception e) {
               failed (e, "Unexpected Exception, Added by Toolbox 2/25/2004");
            }
         }
      }
   }
}



