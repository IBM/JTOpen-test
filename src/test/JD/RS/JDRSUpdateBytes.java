///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSUpdateBytes.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.RS;

import java.io.FileOutputStream;
import java.sql.DataTruncation;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDRSTest;
import test.JDTestDriver;
import test.JDTestcase;



/**
Testcase JDRSUpdateBytes.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>updateBytes()
</ul>
**/
public class JDRSUpdateBytes
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSUpdateBytes";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



    // Private data.
    private static final String key_            = "JDRSUpdateBytes";
    private static String select_         = "SELECT * FROM "
                                                    + JDRSTest.RSTEST_UPDATE;

    private Statement           statement_;
    private Statement           statement2_;
    private ResultSet           rs_;



/**
Constructor.
**/
    public JDRSUpdateBytes (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSUpdateBytes",
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
        select_         = "SELECT * FROM " + JDRSTest.RSTEST_UPDATE;

        if (isJdbc20 ()) {
            // SQL400 - driver neutral...
            String url = baseURL_
            // String url = "jdbc:as400://" + systemObject_.getSystemName()
                
                
                + ";data truncation=true";
            connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
            connection_.setAutoCommit(false); // @C1A
            statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
            statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
    
            statement_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_UPDATE
                + " (C_KEY) VALUES ('DUMMY_ROW')");
            statement_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_UPDATE
                + " (C_KEY) VALUES ('" + key_ + "')");
    
            rs_ = statement_.executeQuery (select_ + " FOR UPDATE");
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
            connection_.commit(); // @C1A
            connection_.close ();
        }
    }



/**
updateBytes() - Should throw exception when the result set is
closed.
**/
    public void Var001()
    {
        if (checkJdbc20 ()) {
        try {
            Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_UPDATE + " FOR UPDATE");
            rs.next ();
            rs.close ();
            rs.updateBytes ("C_VARBINARY_20", new byte[] { (byte) 12, (byte) 34 });
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBytes() - Should throw exception when the result set is
not updatable.
**/
    public void Var002()
    {
        if (checkJdbc20 ()) {
        try {
            Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_UPDATE);
            rs.next ();
            rs.updateBytes ("C_VARBINARY_20", new byte[] { (byte) -12, (byte) -34 });
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBytes() - Should throw exception when cursor is not pointing
to a row.
**/
    public void Var003()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, null);
            rs_.updateBytes ("C_VARBINARY_20", new byte[] { (byte) 12, (byte) 34, (byte) 98 });
            rs_.updateRow(); 
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBytes() - Should throw an exception when the column
is an invalid index.
**/
    public void Var004()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBytes (100, new byte[] { (byte) 12, (byte) 34, (byte) 98 });
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBytes() - Should throw an exception when the column
is 0.
**/
    public void Var005()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBytes (0, new byte[] { (byte) 10, (byte) 0, (byte) 98 });
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBytes() - Should throw an exception when the column
is -1.
**/
    public void Var006()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBytes (-1, new byte[] { (byte) 0 });
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBytes() - Should work when the column index is valid.
**/
    public void Var007()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            byte[] ba = new byte[] { (byte) 22, (byte) 4, (byte) 98, (byte) -2 };
            rs_.updateBytes (18, ba);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            byte[] v = rs2.getBytes ("C_VARBINARY_20");
            rs2.close ();
            assertCondition (areEqual (v, ba));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBytes() - Should throw an exception when the column
name is null.
**/
    public void Var008()
    {
      if (checkJdbc20 ()) {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC throws null pointer exception when column name is null "); 
        } else { 
          
          try {
            JDRSTest.position (rs_, key_);
            rs_.updateBytes (null, new byte[] { (byte) 12, (byte) 34, (byte) 98 });
            failed ("Didn't throw SQLException");
          }
          catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
          }
        }
      }
    }


/**
updateBytes() - Should throw an exception when the column
name is an empty string.
**/
    public void Var009()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBytes ("", new byte[] { (byte) 12, (byte) 34, (byte) 98 });
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBytes() - Should throw an exception when the column
name is invalid.
**/
    public void Var010()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBytes ("INVALID", new byte[] { (byte) 12, (byte) 34, (byte) 98 });
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBytes() - Should work when the column name is valid.
**/
    public void Var011()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            byte[] ba = new byte[] { (byte) -4, (byte) 98, (byte) 99 };
            rs_.updateBytes ("C_VARBINARY_20", ba);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            byte[] v = rs2.getBytes ("C_VARBINARY_20");
            rs2.close ();
            assertCondition (areEqual (v, ba));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBytes() - Should update to SQL NULL when the column
value is null.
**/
    public void Var012()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBytes ("C_VARBINARY_20", null);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            byte[] v = rs2.getBytes ("C_VARBINARY_20");
            boolean wn = rs2.wasNull ();
            rs2.close ();
            assertCondition ((v == null) && (wn == true));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBytes() - Should be reflected by get, even if update has
not yet been issued (i.e. update is still pending).
**/
    public void Var013()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            byte[] ba = new byte[] { (byte) 0, (byte) 1, (byte) -1, (byte) 2, (byte) -2 };
            rs_.updateBytes ("C_VARBINARY_20", ba);
            assertCondition (areEqual (rs_.getBytes ("C_VARBINARY_20"), ba));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
updateBytes() - Should be reflected by get, after update has
been issued, but cursor has not been repositioned.
**/
    public void Var014()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            byte[] ba = new byte[] { (byte) 0, (byte) 56, (byte) 1, (byte) -1, (byte) 2, (byte) -2 };
            rs_.updateBytes ("C_VARBINARY_20", ba);
            rs_.updateRow ();
            assertCondition (areEqual (rs_.getBytes ("C_VARBINARY_20"), ba));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
updateBytes() - Should be reflected by get, after update has
been issued and cursor has been repositioned.
**/
    public void Var015()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            byte[] ba = new byte[] { (byte) 0, (byte) 56, (byte) 1, (byte) -1, (byte) 2, (byte) -2 };
            rs_.updateBytes ("C_VARBINARY_20", ba);
            rs_.updateRow ();
            rs_.beforeFirst ();
            JDRSTest.position (rs_, key_);
            byte[] v = rs_.getBytes ("C_VARBINARY_20");
            assertCondition (areEqual (ba, v));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

        }
    }




/**
updateBytes() - Should work when the current row is the insert
row.
**/
    public void Var016()
    {
        if (checkJdbc20 ()) {
        try {
            rs_.moveToInsertRow ();
            rs_.updateString ("C_KEY", "JDRSUpdateBytes 1");
            byte[] ba = new byte[] { (byte) 121, (byte) 0, (byte) 56, (byte) 1, (byte) -1, (byte) 2, (byte) -2 };
            rs_.updateBytes ("C_VARBINARY_20", ba);
            rs_.insertRow ();
            JDRSTest.position (rs_, "JDRSUpdateBytes 1");
            assertCondition (areEqual (rs_.getBytes ("C_VARBINARY_20"), ba));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBytes() - Should be reflected by get on insert row, even if
insert has not yet been issued (i.e. insert is still pending).
**/
    public void Var017()
    {
        if (checkJdbc20 ()) {
        try {
            rs_.moveToInsertRow ();
            byte[] ba = new byte[] { (byte) 121, (byte) 0, (byte) 56, (byte) -1, (byte) 2, (byte) -2 };
            rs_.updateBytes ("C_VARBINARY_20", ba);
            assertCondition (areEqual (rs_.getBytes ("C_VARBINARY_20"), ba));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }

           

/**
updateBytes() - Should throw an exception on a deleted row.
**/
    public void Var018()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "DUMMY_ROW");
            rs_.deleteRow ();
            byte[] ba = new byte[] { (byte) 121, (byte) 0, (byte) 2, (byte) -2 };
            rs_.updateBytes ("C_VARBINARY_20", ba);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }


/**
updateBytes() - Update a SMALLINT.
**/
    public void Var019 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBytes ("C_SMALLINT", new byte[] { (byte) 5 });
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBytes() - Update an INTEGER.
**/
    public void Var020 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBytes ("C_INTEGER", new byte[] { (byte) 50 });
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBytes() - Update a REAL.
**/
    public void Var021 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBytes ("C_REAL", new byte[] { (byte) 12 });
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }


/**
updateBytes() - Update a FLOAT.
**/
    public void Var022 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBytes ("C_FLOAT", new byte[] { (byte) 47 });
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBytes() - Update a DOUBLE.
**/
    public void Var023 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBytes ("C_DOUBLE", new byte[] { (byte) 65 });
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBytes() - Update a DECIMAL.
**/
    public void Var024 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBytes ("C_DECIMAL_105", new byte[] { (byte) 7 });
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBytes() - Update a NUMERIC.
**/
    public void Var025 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBytes ("C_NUMERIC_105", new byte[] { (byte) 88 });
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBytes() - Update a CHAR.
**/
    public void Var026 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBytes ("C_CHAR_50", new byte[] { (byte) 65 });
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBytes() - Update a VARCHAR.
**/
    public void Var027 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBytes ("C_VARCHAR_50", new byte[] { (byte) 65 });
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBytes() - Update a BINARY.
**/
    public void Var028 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            byte[] ba = new byte[] { (byte) 44, (byte) 86, (byte) -1, (byte) 10, (byte) 20,
                                     (byte) 23, (byte) 77, (byte) 10, (byte) -1, (byte) 46,
                                     (byte) 11, (byte) 22, (byte) 43, (byte) 98, (byte) -6,
                                     (byte) 11, (byte) 11, (byte)  0, (byte)  0, (byte) 100 };
            rs_.updateBytes ("C_BINARY_20", ba);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            byte[] v = rs2.getBytes ("C_BINARY_20");
            rs2.close ();
            assertCondition (areEqual (v, ba));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBytes() - Update a BINARY, when the value is too big.
**/
    public void Var029 ()
    {
        if (checkJdbc20 ()) {
        int expectedColumn = -1;
        try {
            JDRSTest.position (rs_, key_);
            expectedColumn = rs_.findColumn ("C_BINARY_1");
            rs_.updateBytes ("C_BINARY_1", new byte[] { (byte) 54, (byte) 64, (byte) 0 });
            rs_.updateRow ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            DataTruncation dt = (DataTruncation)e;
            assertCondition ((dt.getIndex() == expectedColumn)
                && (dt.getParameter() == false)
                && (dt.getRead() == false)
                && (dt.getDataSize() == 3)
                && (dt.getTransferSize() == 1));

        }
        }

    }



/**
updateBytes() - Update a VARBINARY with a "normal" byte array.
**/
    public void Var030 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            byte[] ba = new byte[] { (byte) -1, (byte) 86,  (byte) 11, (byte)  0, (byte)  0, (byte) 100 }; 
            rs_.updateBytes ("C_VARBINARY_20", ba);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            byte[] v = rs2.getBytes ("C_VARBINARY_20");
            rs2.close ();
            assertCondition (areEqual (v, ba));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBytes() - Update a VARBINARY, with an empty array.
**/
    public void Var031 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            byte[] ba = new byte[0];
            rs_.updateBytes ("C_VARBINARY_20", ba);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            byte[] v = rs2.getBytes ("C_VARBINARY_20");
            rs2.close ();
            assertCondition (areEqual (v, ba));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBytes() - Update a VARBINARY, with single element array.
**/
    public void Var032 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            byte[] ba = new byte[] { (byte) 0 };
            rs_.updateBytes ("C_VARBINARY_20", ba);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            byte[] v = rs2.getBytes ("C_VARBINARY_20");
            rs2.close ();
            assertCondition (areEqual (v, ba));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBytes() - Update a VARBINARY, when the value is too big.
**/
    public void Var033 ()
    {
        if (checkJdbc20 ()) {
        int expectedColumn = -1;
        try {
            JDRSTest.position (rs_, key_);
            expectedColumn = rs_.findColumn ("C_VARBINARY_20");
            byte[] ba = new byte[] { (byte) 44, (byte) 86, (byte) -1, (byte) 10, (byte) 20,
                                     (byte) 23, (byte) 77, (byte) 10, (byte) -1, (byte) 46,
                                     (byte) 11, (byte) 22, (byte) 43, (byte) 98, (byte) -6,
                                     (byte) -23, (byte) 77, (byte) 10, (byte) -1, (byte) 46,
                                     (byte) 23, (byte) 77, (byte) 10, (byte) -1, (byte) 46,
                                     (byte) 11, (byte) 11, (byte)  0, (byte)  0, (byte) 100 };
            rs_.updateBytes ("C_VARBINARY_20", ba);
            rs_.updateRow ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            DataTruncation dt = (DataTruncation)e;
            assertCondition ((dt.getIndex() == expectedColumn)
                && (dt.getParameter() == false)
                && (dt.getRead() == false)
                && (dt.getDataSize() == 30)
                && (dt.getTransferSize() == 20));

        }
        }
    }



/**
updateBytes() - Update a CLOB.
**/
    public void Var034 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateBytes ("C_CLOB", new byte[] { (byte) 56, (byte) 98 });
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }



/**
updateBytes() - Update a DBCLOB.
**/
    public void Var035 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateBytes ("C_DBCLOB", new byte[] { (byte) -47, (byte) -98 });
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }



/**
updateBytes() - Update a BLOB.

SQL400 - the native driver expects this update to work correctly.
**/
    public void Var036 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, key_);
                byte[] ba = new byte[] { (byte) 98, (byte) 1 };
                rs_.updateBytes ("C_BLOB", ba);

                //if (getDriver() == JDTestDriver.DRIVER_NATIVE) {                  //@D1D
                   rs_.updateRow ();
                   ResultSet rs2 = statement2_.executeQuery (select_);
                   JDRSTest.position (rs2, key_);
                   byte[] v = rs2.getBytes ("C_BLOB");
                   rs2.close ();
                   assertCondition (areEqual (v, ba));
                //} else {                                                          //@D1D
                   //failed ("Didn't throw SQLException");                          //@D1D
                //}                                                                 //@D1D
            }
            catch (Exception e) {
               //if (getDriver() == JDTestDriver.DRIVER_NATIVE) {                   //@D1D
                  failed (e, "Unexpected Exception");
               //} else {                                                           //@D1D
                  //assertExceptionIsInstanceOf (e, "java.sql.SQLException");       //@DiD
               //}                                                                  //@D1D
            }
        }
        }
    }



/**
updateBytes() - Update a DATE.
**/
    public void Var037 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBytes ("C_DATE", new byte[] { (byte) 19 });
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBytes() - Update a TIME.
**/
    public void Var038 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBytes ("C_TIME", new byte[] { (byte) 1 });
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBytes() - Update a TIMESTAMP.
**/
    public void Var039 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBytes ("C_TIMESTAMP", new byte[] { (byte) 2 });
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBytes() - Update a DATALINK.
**/
    public void Var040 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            // We do not test updating datalinks, since it is not 
            // possible to open a updatable cursor/result set with
            // a datalink column.
            notApplicable("DATALINK update not supported.");
            /*
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateBytes ("C_DATALINK", new byte[] { (byte) 113 });
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
            */
        }
        }
    }



/**
updateBytes() - Update a DISTINCT.
**/
    public void Var041 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateBytes ("C_DISTINCT", new byte[] { (byte) 44 });
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }



/**
updateBytes() - Update a BIGINT.
**/
    public void Var042()
    {
        if (checkJdbc20 ()) {
        if (checkBigintSupport()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBytes ("C_BIGINT", new byte[] { (byte) 50 });
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
        }
    }


    
    /**
     updateBytes() - Update a DFP16. 
     **/
    public void Var043()
    {
      if (checkDecFloatSupport ()) {
        try {
          Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
              ResultSet.CONCUR_UPDATABLE);
          ResultSet rs = s.executeQuery ("SELECT * FROM "
              + JDRSTest.RSTEST_DFP16+" FOR UPDATE ");
          rs.next(); 
          rs.updateBytes (1, new byte[] { (byte) 50 });
          rs.updateRow ();
          failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
          assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
      }
    }


    /**
    updateBytes() - Update a DFP34. 
    **/
   public void Var044()
   {
     if (checkDecFloatSupport ()) {
       try {
         Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
             ResultSet.CONCUR_UPDATABLE);
         ResultSet rs = s.executeQuery ("SELECT * FROM "
             + JDRSTest.RSTEST_DFP34+" FOR UPDATE ");
         rs.next(); 
         rs.updateBytes (1, new byte[] { (byte) 50 });
         rs.updateRow ();
         failed ("Didn't throw SQLException");
       }
       catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
       }
     }
   }

        
   /**
updateBytes() - Update a BOOLEAN.
**/
    public void Var045()
    {
        if (checkJdbc20 ()) {
        if (checkBooleanSupport()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBytes ("C_BOOLEAN", new byte[] { (byte) 50 });
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
        }
    }

        
}



