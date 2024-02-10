///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetBytesBinary.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.RS;

import com.ibm.as400.access.AS400;

import test.JDRSTest;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import java.util.Hashtable;



/**
Testcase JDRSGetBytes.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>getBytes()
</ul>
**/
public class JDRSGetBytesBinary
extends JDTestcase
{



   // Private data.
   private Statement           statement_;
   String statementQuery_;
   private Statement           statement0_;
   private ResultSet           rs_;



   private static final byte[] eleven = { (byte) 'E', (byte) 'l', (byte) 'e', (byte) 'v', (byte) 'e', (byte) 'n',
      (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ',
      (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ',
      (byte) ' ', (byte) ' '};

   private static final byte[] twelve = { (byte) 'T', (byte) 'w', (byte) 'e', (byte) 'l', (byte) 'v',
      (byte) 'e'};



/**
Constructor.
**/
   public JDRSGetBytesBinary (AS400 systemObject,
                        Hashtable namesAndVars,
                        int runMode,
                        FileOutputStream fileOutputStream,
                        
                        String password)
   {
      super (systemObject, "JDRSGetBytesBinary",
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
	if (connection_ != null) connection_.close();

      // SQL400 - driver neutral...
      String url = baseURL_
                   // String url = "jdbc:as400://" + systemObject_.getSystemName()
                   ;
      connection_ = testDriver_.getConnection (url + ";lob threshold=30000",systemObject_.getUserId(),encryptedPassword_,"JDRSGetBytesBinary");
	  setAutoCommit(connection_, false); // @E1A

      statement0_ = connection_.createStatement ();

      if (isJdbc20 ()) {
	  try {
	      statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_UPDATABLE);
	  } catch (SQLException ex) {
	      statement_ = connection_.createStatement ();
	  }
	  statementQuery_ = "SELECT * FROM "
                                        + JDRSTest.RSTEST_BINARY + " FOR UPDATE";
         rs_ = statement_.executeQuery (statementQuery_);
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
         rs_.close ();
         statement_.close ();
      }

      statement0_.close ();
      connection_.commit(); // @E1A
      connection_.close ();
   }

/**
getBytes() - Should work when the column index is valid.
**/
   public void Var001 ()
   {
      if (getRelease() >= JDTestDriver.RELEASE_V5R3M0)
      {
         try {
            statement_.close();
	    try {
		statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
							  ResultSet.CONCUR_UPDATABLE);
	    } catch (SQLException ex) {
		statement_ = connection_.createStatement ();
	    }

            rs_ = statement_.executeQuery (statementQuery_);


            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                  + JDRSTest.RSTEST_BINARY);
            JDRSTest.position0 (rs, "BINARY_NOTRANS");
            byte[] v = rs.getBytes (4);
            assertCondition (isEqual (v, twelve), "New testcase added by Native");
         } catch (Exception e) {
            failed (e, "Unexpected Exception - New testcase added by Native");
         }
      } else {
        notApplicable();
      }
   }

/**
getBytes() - Should work when the column name is valid.
**/
   public void Var002 ()
   {
      if (getRelease() >= JDTestDriver.RELEASE_V5R3M0)
      {
         try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                  + JDRSTest.RSTEST_BINARY);
            JDRSTest.position0 (rs, "BINARY_NOTRANS");
            byte[] v = rs.getBytes ("C_BINARY_20");
            assertCondition (isEqual (v, eleven), "New testcase added by Native");
         } catch (Exception e) {
            failed (e, "Unexpected Exception - New testcase added by Native");
         }
      } else {
        notApplicable();
      }
   }

/**
getBytes() - Should work when an update is pending.
**/
   public void Var003 ()
   {
      if (getRelease() >= JDTestDriver.RELEASE_V5R3M0)
      {
         if (checkJdbc20 ()) {
            try {
               rs_ = JDRSTest.position (getDriver(), statement_, statementQuery_, rs_, "UPDATE_SANDBOX");
               byte[] test = new byte[] { (byte) 0x34, (byte) 0x45, (byte) 0x50,
                  (byte) 0x56, (byte) 0x67, (byte) 0x78, (byte) 0x89, (byte) 0x9A,
                  (byte) 0xAB, (byte) 0xBC};
               rs_.updateBytes ("C_VARBINARY_20", test);
               byte[] v = rs_.getBytes ("C_VARBINARY_20");
               assertCondition (isEqual (v, test), "New testcase added by Native");
            } catch (Exception e) {
               failed (e, "Unexpected Exception - New testcase added by Native");
            }
         }
      } else {
        notApplicable();
      }
   }



/**
getBytes() - Should work when an update has been done.
**/
   public void Var004 ()
   {
      if (getRelease() >= JDTestDriver.RELEASE_V5R3M0)
      {
         if (checkJdbc20 ()) {
            try {
               JDRSTest.position (rs_, "UPDATE_SANDBOX");
               byte[] test = new byte[] { (byte) 0x01, (byte) 0x12, (byte) 0x34, (byte) 0x45, (byte) 0x50,
                  (byte) 0x56, (byte) 0x67, (byte) 0x78, (byte) 0x89, (byte) 0x9A,
                  (byte) 0xAB, (byte) 0xBC, (byte) 0xCD, (byte) 0xDE, (byte) 0xEF,
                  (byte) 0xFF, (byte) 0x00, (byte) 0xFF, (byte) 0x00, (byte) 0xFF};
               rs_.updateBytes ("C_BINARY_20", test);
               rs_.updateRow ();
               byte[] v = rs_.getBytes ("C_BINARY_20");
               assertCondition (isEqual (v, test), "New testcase added by Native");
            } catch (Exception e) {
               failed (e, "Unexpected Exception - New testcase added by Native");
            }
         }
      } else {
        notApplicable();
      }
   }


/**
getBytes() - Should work when the current row is the insert
row, when an insert is pending.
**/
   public void Var005 ()
   {
      if (getRelease() >= JDTestDriver.RELEASE_V5R3M0)
      {
         if (checkJdbc20 ()) {
           if (getDriver() == JDTestDriver.DRIVER_JCC) {
             notApplicable("JCC does not support moveToInserRow");
             return;
           }
            try {
               rs_.moveToInsertRow ();
               byte[] test = new byte[] { (byte) 0xBC, (byte) 0xCD};
               rs_.updateBytes ("C_VARBINARY_20", test);
               byte[] v = rs_.getBytes ("C_VARBINARY_20");
               assertCondition (isEqual (v, test), "New testcase added by Native");
            } catch (Exception e) {
               failed (e, "Unexpected Exception - New testcase added by Native");
            }
         }
      } else {
        notApplicable();
      }
   }



/**
getBytes() - Should work when the current row is the insert
row, when an insert has been done.
**/
   public void Var006 ()
   {
      if (getRelease() >= JDTestDriver.RELEASE_V5R3M0)
      {
         if (checkJdbc20 ()) {
           if (getDriver() == JDTestDriver.DRIVER_JCC) {
             notApplicable("JCC does not support moveToInserRow");
             return;
           }
            try {
               rs_.moveToInsertRow ();
               byte[] test = new byte[] { (byte) 0xBC, (byte) 0xCD, (byte) 0xDE, (byte) 0xEF,
                  (byte) 0xFF, (byte) 0x00, (byte) 0xFF, (byte) 0x00, (byte) 0xFF};
               rs_.updateBytes ("C_VARBINARY_20", test);
               rs_.insertRow ();
               byte[] v = rs_.getBytes ("C_VARBINARY_20");
               assertCondition (isEqual (v, test), "New testcase added by Native");
            } catch (Exception e) {
               failed (e, "Unexpected Exception - New testcase added by Native");
            }
         }
      } else {
        notApplicable();
      }
   }


/**
getBytes() - Should return null when the column is NULL.
**/
   public void Var007 ()
   {
      if (getRelease() >= JDTestDriver.RELEASE_V5R3M0)
      {
         try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                  + JDRSTest.RSTEST_BINARY);
            JDRSTest.position0 (rs, "BINARY_NULL");
            byte[] v = rs.getBytes ("C_VARBINARY_20");
            assertCondition (v == null, "New testcase added by Native");
         } catch (Exception e) {
            failed (e, "Unexpected Exception - New testcase added by Native");
         }
      } else {
        notApplicable();
      }
   }



/**
getBytes() - Get from a BINARY.
**/
   public void Var008 ()
   {
      if (getRelease() >= JDTestDriver.RELEASE_V5R3M0)
      {
         try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_BINARY);
            JDRSTest.position0 (rs, "BINARY_NOTRANS");
            byte[] v = rs.getBytes ("C_BINARY_20");
            assertCondition (isEqual (v, eleven), "New testcase added by Native");
         } catch (Exception e) {
            failed (e, "Unexpected Exception - New testcase added by Native");
         }
      } else {
        notApplicable();
      }
   }



/**
getBytes() - Get from a VARBINARY.
**/
   public void Var009 ()
   {
      if (getRelease() >= JDTestDriver.RELEASE_V5R3M0)
      {
         try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_BINARY);
            JDRSTest.position0 (rs, "BINARY_NOTRANS");
            byte[] v = rs.getBytes ("C_VARBINARY_20");
            assertCondition (isEqual (v, twelve), "New testcase added by Native");
         } catch (Exception e) {
            failed (e, "Unexpected Exception - New testcase added by Native");
         }
      } else {
        notApplicable();
      }
   }


/**
getBytes() - Verify that data is truncated without
a DataTruncation posted when the max field size is set
to a value shorter than the byte array.
**/
   public void Var010 ()
   {
      if (getRelease() >= JDTestDriver.RELEASE_V5R3M0)
      {
         try {
            Statement s = connection_.createStatement ();
            s.setMaxFieldSize (18);
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                                           + JDRSTest.RSTEST_BINARY);
            JDRSTest.position0 (rs, "BINARY_NOTRANS");

            byte[] v = rs.getBytes ("C_BINARY_20");
            SQLWarning w = rs.getWarnings ();
            rs.close ();
            s.close ();
            byte[] expected =  { (byte) 'E', (byte) 'l', (byte) 'e', (byte) 'v', (byte) 'e', (byte) 'n',
               (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ',
               (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' '};
            assertCondition ((isEqual (v, expected)) && (w == null), "New testcase added by Native");
         } catch (Exception e) {
            failed (e, "Unexpected Exception - New testcase added by Native");
         }
      } else {
        notApplicable();
      }
   }
}

