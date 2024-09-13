///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSUpdateRowId.java
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
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDRSUpdateRowId.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>updateRowId()
</ul>
**/
public class JDRSUpdateRowId
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSUpdateRowId";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



    // Private data.
    private static final String key_            = "JDRSUpdateRowId";
    private static String select_         = "SELECT * FROM "
                                                    + JDRSTest.RSTEST_UPDATE;

    private Statement           statement_;
    private Statement           statement2_;
    private ResultSet           rs_;


    private String baseAdded = " -- Added 02/07/2007 by native driver to test JDBC40 RowId support -- based on JDRSUpdateBytes "; 

/**
Constructor.
**/
    public JDRSUpdateRowId (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSUpdateRowId",
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
        collection_ = JDRSTest.COLLECTION;
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
 * Create a ROWID object for the current driver
 */

    Object createRowId(byte[] arg) throws Exception {
	Object testRowId = null; 
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
	   testRowId = JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2RowId", arg); 
        } else {
           //System.out.println("DRIVER NEEDS TO ADD CODE TO CREATE ROWID OBJECT"); 
            testRowId = JDReflectionUtil.createObject("com.ibm.as400.access.AS400JDBCRowId", arg);
        }
	return testRowId; 
    } 



/**
updateRowId() - Should throw exception when the result set is
closed.
**/
    public void Var001()
    {
        if (checkJdbc40 ()) {
        try {
            Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_UPDATE + " FOR UPDATE");
            rs.next ();
            rs.close ();

            JDReflectionUtil.callMethod_V(rs, "updateRowId", "C_VARBINARY_20", createRowId(new byte[] { (byte) 12, (byte) 34 }));
            failed ("Didn't throw SQLException"+baseAdded);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException", baseAdded);
        }
        }
    }



/**
updateRowId() - Should throw exception when the result set is
not updatable.
**/
    public void Var002()
    {
        if (checkJdbc40 ()) {
        try {
            Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_UPDATE);
            rs.next ();
            JDReflectionUtil.callMethod_V(rs, "updateRowId", "C_VARBINARY_20", createRowId(new byte[] { (byte) -12, (byte) -34 }));
            failed ("Didn't throw SQLException"+baseAdded);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateRowId() - Should throw exception when cursor is not pointing
to a row.
**/
    public void Var003()
    {
        if (checkJdbc40 ()) {
        try {
            JDRSTest.position (rs_, null);
            JDReflectionUtil.callMethod_V(rs_, "updateRowId", "C_VARBINARY_20", createRowId(new byte[] { (byte) 12, (byte) 34, (byte) 98 }));
            failed ("Didn't throw SQLException"+baseAdded);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException", baseAdded);
        }
        }
    }



/**
updateRowId() - Should throw an exception when the column
is an invalid index.
**/
    public void Var004()
    {
        if (checkJdbc40 ()) {
        try {
            JDRSTest.position (rs_, key_);
            JDReflectionUtil.callMethod_V(rs_, "updateRowId", 100, createRowId(new byte[] { (byte) 12, (byte) 34, (byte) 98 }));
            failed ("Didn't throw SQLException"+baseAdded);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException", baseAdded);
        }
        }
    }



/**
updateRowId() - Should throw an exception when the column
is 0.
**/
    public void Var005()
    {
        if (checkJdbc40 ()) {
        try {
            JDRSTest.position (rs_, key_);
            JDReflectionUtil.callMethod_V(rs_, "updateRowId", 0, createRowId(new byte[] { (byte) 10, (byte) 0, (byte) 98 }));
            failed ("Didn't throw SQLException"+baseAdded);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException", baseAdded);
        }
        }
    }



/**
updateRowId() - Should throw an exception when the column
is -1.
**/
    public void Var006()
    {
        if (checkJdbc40 ()) {
        try {
            JDRSTest.position (rs_, key_);
            JDReflectionUtil.callMethod_V(rs_, "updateRowId", -1, createRowId(new byte[] { (byte) 0 }));
            failed ("Didn't throw SQLException"+baseAdded);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException", baseAdded);
        }
        }
    }



/**
updateRowId() - Should work when the column index is valid.
**/
    public void Var007()
    {
        if (checkJdbc40 ()) {
        try {
            JDRSTest.position (rs_, key_);
            byte[] ba = new byte[] { (byte) 22, (byte) 4, (byte) 98, (byte) -2 };
	    Object rid = createRowId(ba); 
            JDReflectionUtil.callMethod_V(rs_, "updateRowId", 18, rid);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            byte[] v = rs2.getBytes ("C_VARBINARY_20");
            rs2.close ();
            assertCondition (isEqual (v, ba), baseAdded);
        }
        catch (Exception e) {
            //toolbox does not support rowid on this type
            if(isToolboxDriver())
            {
                assertCondition(e.getMessage().indexOf("Data type mismatch") != -1);
                return;
            }
            failed (e, "Unexpected Exception "+ baseAdded);
        }
        }
    }



/**
updateRowId() - Should throw an exception when the column
name is null.
**/
    public void Var008()
    {
        if (checkJdbc40 ()) {
        try {
            JDRSTest.position (rs_, key_);
            JDReflectionUtil.callMethod_V(rs_, "updateRowId", null, createRowId(new byte[] { (byte) 12, (byte) 34, (byte) 98 }));
            failed ("Didn't throw SQLException"+baseAdded);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException", baseAdded);
        }
        }
    }



/**
updateRowId() - Should throw an exception when the column
name is an empty string.
**/
    public void Var009()
    {
        if (checkJdbc40 ()) {
        try {
            JDRSTest.position (rs_, key_);
            JDReflectionUtil.callMethod_V(rs_, "updateRowId", "", createRowId(new byte[] { (byte) 12, (byte) 34, (byte) 98 }));
            failed ("Didn't throw SQLException"+baseAdded);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException", baseAdded);
        }
        }
    }



/**
updateRowId() - Should throw an exception when the column
name is invalid.
**/
    public void Var010()
    {
        if (checkJdbc40 ()) {
        try {
            JDRSTest.position (rs_, key_);
            JDReflectionUtil.callMethod_V(rs_, "updateRowId", "INVALID", createRowId(new byte[] { (byte) 12, (byte) 34, (byte) 98 }));
            failed ("Didn't throw SQLException"+baseAdded);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException", baseAdded);
        }
        }
    }



/**
updateRowId() - Should work when the column name is valid.
**/
    public void Var011()
    {
        if (checkJdbc40 ()) {
        try {
            JDRSTest.position (rs_, key_);
            byte[] ba = new byte[] { (byte) -4, (byte) 98, (byte) 99 };
	    
            JDReflectionUtil.callMethod_V(rs_, "updateRowId", "C_VARBINARY_20", createRowId(ba));
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            byte[] v = rs2.getBytes ("C_VARBINARY_20");
            rs2.close ();
            assertCondition (isEqual (v, ba), baseAdded);
        }
        catch (Exception e) {
            //toolbox does not support rowid on this type
            if(isToolboxDriver())
            {
                assertCondition(e.getMessage().indexOf("Data type mismatch") != -1);
                return;
            }
            failed (e, "Unexpected Exception"+ baseAdded);
        }
        }
    }



/**
updateRowId() - Should update to SQL NULL when the column
value is null.
**/
    public void Var012()
    {
        if (checkJdbc40 ()) {
        try {
            JDRSTest.position (rs_, key_);
            
            Class[] argTypes = new Class[2];
            argTypes[0] = Class.forName("java.lang.String");
            try {
                argTypes[1] = Class.forName("java.sql.RowId"); 
            }  catch (Exception e) {
                argTypes[1] = Class.forName("com.ibm.db2.jdbc.app.RowId"); 
            }
            Object[] args = new Object[2];
            args[0] = "C_VARBINARY_20";
            args[1] = null;

            JDReflectionUtil.callMethod_V(rs_, "updateRowId", argTypes, args);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            byte[] v = rs2.getBytes ("C_VARBINARY_20");
            boolean wn = rs2.wasNull ();
            rs2.close ();
            assertCondition ((v == null) && (wn == true), baseAdded);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception"+ baseAdded);
        }
        }
    }



/**
updateRowId() - Should be reflected by get, even if update has
not yet been issued (i.e. update is still pending).
**/
    public void Var013()
    {
        if (checkJdbc40 ()) {
        try {
            JDRSTest.position (rs_, key_);
            byte[] ba = new byte[] { (byte) 0, (byte) 1, (byte) -1, (byte) 2, (byte) -2 };
            JDReflectionUtil.callMethod_V(rs_, "updateRowId", "C_VARBINARY_20", createRowId(ba));
            assertCondition (isEqual (rs_.getBytes ("C_VARBINARY_20"), ba), baseAdded);
        }
        catch (Exception e) {
            //toolbox does not support rowid on this type
            if(isToolboxDriver())
            {
                assertCondition(e.getMessage().indexOf("Data type mismatch") != -1);
                return;
            }
            failed (e, "Unexpected Exception"+ baseAdded);
        }
        }
    }




/**
updateRowId() - Should be reflected by get, after update has
been issued, but cursor has not been repositioned.
**/
    public void Var014()
    {
        if (checkJdbc40 ()) {
        try {
            JDRSTest.position (rs_, key_);
            byte[] ba = new byte[] { (byte) 0, (byte) 56, (byte) 1, (byte) -1, (byte) 2, (byte) -2 };
            JDReflectionUtil.callMethod_V(rs_, "updateRowId", "C_VARBINARY_20", createRowId(ba));
            rs_.updateRow ();
            assertCondition (isEqual (rs_.getBytes ("C_VARBINARY_20"), ba), baseAdded);
        }
        catch (Exception e) {
            //toolbox does not support rowid on this type
            if(isToolboxDriver())
            {
                assertCondition(e.getMessage().indexOf("Data type mismatch") != -1);
                return;
            }
            failed (e, "Unexpected Exception"+ baseAdded);
        }
        }
    }




/**
updateRowId() - Should be reflected by get, after update has
been issued and cursor has been repositioned.
**/
    public void Var015()
    {
        if (checkJdbc40 ()) {
        try {
            JDRSTest.position (rs_, key_);
            byte[] ba = new byte[] { (byte) 0, (byte) 56, (byte) 1, (byte) -1, (byte) 2, (byte) -2 };
            JDReflectionUtil.callMethod_V(rs_, "updateRowId", "C_VARBINARY_20", createRowId(ba));
            rs_.updateRow ();
            rs_.beforeFirst ();
            JDRSTest.position (rs_, key_);
            byte[] v = rs_.getBytes ("C_VARBINARY_20");
            assertCondition (isEqual (ba, v), baseAdded);
        }
        catch (Exception e) {
            //toolbox does not support rowid on this type
            if(isToolboxDriver())
            {
                assertCondition(e.getMessage().indexOf("Data type mismatch") != -1);
                return;
            }
            failed (e, "Unexpected Exception"+ baseAdded);
        }

        }
    }




/**
updateRowId() - Should work when the current row is the insert
row.
**/
    public void Var016()
    {
        if (checkJdbc40 ()) {
        try {
            rs_.moveToInsertRow ();
            rs_.updateString ("C_KEY", "JDRSUpdateRowId 1");
            byte[] ba = new byte[] { (byte) 121, (byte) 0, (byte) 56, (byte) 1, (byte) -1, (byte) 2, (byte) -2 };
            JDReflectionUtil.callMethod_V(rs_, "updateRowId", "C_VARBINARY_20", createRowId(ba));
            rs_.insertRow ();
            JDRSTest.position (rs_, "JDRSUpdateRowId 1");
            assertCondition (isEqual (rs_.getBytes ("C_VARBINARY_20"), ba), baseAdded);
        }
        catch (Exception e) {
            //toolbox does not support rowid on this type
            if(isToolboxDriver())
            {
                assertCondition(e.getMessage().indexOf("Data type mismatch") != -1);
                return;
            }
            failed (e, "Unexpected Exception"+ baseAdded);
        }
        }
    }



/**
updateRowId() - Should be reflected by get on insert row, even if
insert has not yet been issued (i.e. insert is still pending).
**/
    public void Var017()
    {
        if (checkJdbc40 ()) {
        try {
            rs_.moveToInsertRow ();
            byte[] ba = new byte[] { (byte) 121, (byte) 0, (byte) 56, (byte) -1, (byte) 2, (byte) -2 };
            JDReflectionUtil.callMethod_V(rs_, "updateRowId", "C_VARBINARY_20", createRowId(ba));
            assertCondition (isEqual (rs_.getBytes ("C_VARBINARY_20"), ba), baseAdded);
        }
        catch (Exception e) {
            //toolbox does not support rowid on this type
            if(isToolboxDriver())
            {
                assertCondition(e.getMessage().indexOf("Data type mismatch") != -1);
                return;
            }
            failed (e, "Unexpected Exception"+ baseAdded);
        }
        }
    }

           

/**
updateRowId() - Should throw an exception on a deleted row.
**/
    public void Var018()
    {
        if (checkJdbc40 ()) {
        try {
            JDRSTest.position (rs_, "DUMMY_ROW");
            rs_.deleteRow ();
            byte[] ba = new byte[] { (byte) 121, (byte) 0, (byte) 2, (byte) -2 };
            JDReflectionUtil.callMethod_V(rs_, "updateRowId", "C_VARBINARY_20", createRowId(ba));
            failed ("Didn't throw SQLException"+baseAdded);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException", baseAdded);
        }
        }
    }


/**
updateRowId() - Update a SMALLINT.
**/
    public void Var019 ()
    {
        if (checkJdbc40 ()) {
        try {
            JDRSTest.position (rs_, key_);
            JDReflectionUtil.callMethod_V(rs_, "updateRowId", "C_SMALLINT", createRowId(new byte[] { (byte) 5 }));
            failed ("Didn't throw SQLException"+baseAdded);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException", baseAdded);
        }
        }
    }



/**
updateRowId() - Update an INTEGER.
**/
    public void Var020 ()
    {
        if (checkJdbc40 ()) {
        try {
            JDRSTest.position (rs_, key_);
            JDReflectionUtil.callMethod_V(rs_, "updateRowId", "C_INTEGER", createRowId(new byte[] { (byte) 50 }));
            failed ("Didn't throw SQLException"+baseAdded);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException", baseAdded);
        }
        }
    }



/**
updateRowId() - Update a REAL.
**/
    public void Var021 ()
    {
        if (checkJdbc40 ()) {
        try {
            JDRSTest.position (rs_, key_);
            JDReflectionUtil.callMethod_V(rs_, "updateRowId", "C_REAL", createRowId(new byte[] { (byte) 12 }));
            failed ("Didn't throw SQLException"+baseAdded);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException", baseAdded);
        }
        }
    }


/**
updateRowId() - Update a FLOAT.
**/
    public void Var022 ()
    {
        if (checkJdbc40 ()) {
        try {
            JDRSTest.position (rs_, key_);
            JDReflectionUtil.callMethod_V(rs_, "updateRowId", "C_FLOAT", createRowId(new byte[] { (byte) 47 }));
            failed ("Didn't throw SQLException"+baseAdded);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException", baseAdded);
        }
        }
    }



/**
updateRowId() - Update a DOUBLE.
**/
    public void Var023 ()
    {
        if (checkJdbc40 ()) {
        try {
            JDRSTest.position (rs_, key_);
            JDReflectionUtil.callMethod_V(rs_, "updateRowId", "C_DOUBLE", createRowId(new byte[] { (byte) 65 }));
            failed ("Didn't throw SQLException"+baseAdded);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException", baseAdded);
        }
        }
    }



/**
updateRowId() - Update a DECIMAL.
**/
    public void Var024 ()
    {
        if (checkJdbc40 ()) {
        try {
            JDRSTest.position (rs_, key_);
            JDReflectionUtil.callMethod_V(rs_, "updateRowId", "C_DECIMAL_105", createRowId(new byte[] { (byte) 7 }));
            failed ("Didn't throw SQLException"+baseAdded);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException", baseAdded);
        }
        }
    }



/**
updateRowId() - Update a NUMERIC.
**/
    public void Var025 ()
    {
        if (checkJdbc40 ()) {
        try {
            JDRSTest.position (rs_, key_);
            JDReflectionUtil.callMethod_V(rs_, "updateRowId", "C_NUMERIC_105", createRowId(new byte[] { (byte) 88 }));
            failed ("Didn't throw SQLException"+baseAdded);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException", baseAdded);
        }
        }
    }



/**
updateRowId() - Update a CHAR.
**/
    public void Var026 ()
    {
        if (checkJdbc40 ()) {
        try {
            JDRSTest.position (rs_, key_);
            JDReflectionUtil.callMethod_V(rs_, "updateRowId", "C_CHAR_50", createRowId(new byte[] { (byte) 65 }));
            failed ("Didn't throw SQLException"+baseAdded);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException", baseAdded);
        }
        }
    }



/**
updateRowId() - Update a VARCHAR.
**/
    public void Var027 ()
    {
        if (checkJdbc40 ()) {
        try {
            JDRSTest.position (rs_, key_);
            JDReflectionUtil.callMethod_V(rs_, "updateRowId", "C_VARCHAR_50", createRowId(new byte[] { (byte) 65 }));
            failed ("Didn't throw SQLException"+baseAdded);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException", baseAdded);
        }
        }
    }



/**
updateRowId() - Update a BINARY.
**/
    public void Var028 ()
    {
        if (checkJdbc40 ()) {
        try {
            JDRSTest.position (rs_, key_);
            byte[] ba = new byte[] { (byte) 44, (byte) 86, (byte) -1, (byte) 10, (byte) 20,
                                     (byte) 23, (byte) 77, (byte) 10, (byte) -1, (byte) 46,
                                     (byte) 11, (byte) 22, (byte) 43, (byte) 98, (byte) -6,
                                     (byte) 11, (byte) 11, (byte)  0, (byte)  0, (byte) 100 };
            JDReflectionUtil.callMethod_V(rs_, "updateRowId", "C_BINARY_20", createRowId(ba));
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            byte[] v = rs2.getBytes ("C_BINARY_20");
            rs2.close ();
            assertCondition (isEqual (v, ba), baseAdded);
        }
        catch (Exception e) {
            //toolbox does not support rowid on this type
            if(isToolboxDriver())
            {
                assertCondition(e.getMessage().indexOf("Data type mismatch") != -1);
                return;
            }
            failed (e, "Unexpected Exception"+ baseAdded);
        }
        }
    }



/**
updateRowId() - Update a BINARY, when the value is too big.
**/
    public void Var029 ()
    {
        if (checkJdbc40 ()) {
        int expectedColumn = -1;
        try {
            JDRSTest.position (rs_, key_);
            expectedColumn = rs_.findColumn ("C_BINARY_1");
            JDReflectionUtil.callMethod_V(rs_, "updateRowId", "C_BINARY_1", createRowId(new byte[] { (byte) 54, (byte) 64, (byte) 0 }));
            rs_.updateRow ();
            failed ("Didn't throw SQLException"+baseAdded);
        }
        catch (Exception e) {
            //toolbox does not support rowid on this type
            if(isToolboxDriver())
            {
                assertCondition(e.getMessage().indexOf("Data type mismatch") != -1);
                return;
            }
            DataTruncation dt = (DataTruncation)e;
            assertCondition ((dt.getIndex() == expectedColumn)
                && (dt.getParameter() == false)
                && (dt.getRead() == false)
                && (dt.getDataSize() == 3)
                && (dt.getTransferSize() == 1), baseAdded);

        }
        }

    }



/**
updateRowId() - Update a VARBINARY with a "normal" byte array.
**/
    public void Var030 ()
    {
        if (checkJdbc40 ()) {
        try {
            JDRSTest.position (rs_, key_);
            byte[] ba = new byte[] { (byte) -1, (byte) 86,  (byte) 11, (byte)  0, (byte)  0, (byte) 100 }; 
            JDReflectionUtil.callMethod_V(rs_, "updateRowId", "C_VARBINARY_20", createRowId(ba));
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            byte[] v = rs2.getBytes ("C_VARBINARY_20");
            rs2.close ();
            assertCondition (isEqual (v, ba), baseAdded);
        }
        catch (Exception e) {
            //toolbox does not support rowid on this type
            if(isToolboxDriver())
            {
                assertCondition(e.getMessage().indexOf("Data type mismatch") != -1);
                return;
            }
            failed (e, "Unexpected Exception"+ baseAdded);
        }
        }
    }



/**
updateRowId() - Update a VARBINARY, with an empty array.
**/
    public void Var031 ()
    {
        if (checkJdbc40 ()) {
        try {
            JDRSTest.position (rs_, key_);
            byte[] ba = new byte[0];
            JDReflectionUtil.callMethod_V(rs_, "updateRowId", "C_VARBINARY_20", createRowId(ba));
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            byte[] v = rs2.getBytes ("C_VARBINARY_20");
            rs2.close ();
            assertCondition (isEqual (v, ba), baseAdded);
        }
        catch (Exception e) {
            //toolbox does not support rowid on this type
            if(isToolboxDriver())
            {
                assertCondition(e.getMessage().indexOf("Data type mismatch") != -1);
                return;
            }
            failed (e, "Unexpected Exception"+ baseAdded);
        }
        }
    }



/**
updateRowId() - Update a VARBINARY, with single element array.
**/
    public void Var032 ()
    {
        if (checkJdbc40 ()) {
        try {
            JDRSTest.position (rs_, key_);
            byte[] ba = new byte[] { (byte) 0 };
            JDReflectionUtil.callMethod_V(rs_, "updateRowId", "C_VARBINARY_20", createRowId(ba));
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            byte[] v = rs2.getBytes ("C_VARBINARY_20");
            rs2.close ();
            assertCondition (isEqual (v, ba), baseAdded);
        }
        catch (Exception e) {
            //toolbox does not support rowid on this type
            if(isToolboxDriver())
            {
                assertCondition(e.getMessage().indexOf("Data type mismatch") != -1);
                return;
            }
            failed (e, "Unexpected Exception"+ baseAdded);
        }
        }
    }



/**
updateRowId() - Update a VARBINARY, when the value is too big.
**/
    public void Var033 ()
    {
        if (checkJdbc40 ()) {
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
            JDReflectionUtil.callMethod_V(rs_, "updateRowId", "C_VARBINARY_20", createRowId(ba));
            rs_.updateRow ();
            failed ("Didn't throw SQLException"+baseAdded);
        }
        catch (Exception e) {
            //toolbox does not support rowid on this type
            if(isToolboxDriver())
            {
                assertCondition(e.getMessage().indexOf("Data type mismatch") != -1);
                return;
            }
            DataTruncation dt = (DataTruncation)e;
            assertCondition ((dt.getIndex() == expectedColumn)
                && (dt.getParameter() == false)
                && (dt.getRead() == false)
                && (dt.getDataSize() == 30)
                && (dt.getTransferSize() == 20), baseAdded);

        }
        }
    }



/**
updateRowId() - Update a CLOB.
**/
    public void Var034 ()
    {
        if (checkJdbc40 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateRowId", "C_CLOB", createRowId(new byte[] { (byte) 56, (byte) 98 }));
                failed ("Didn't throw SQLException"+baseAdded);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException", baseAdded);
            }
        }
        }
    }



/**
updateRowId() - Update a DBCLOB.
**/
    public void Var035 ()
    {
        if (checkJdbc40 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateRowId", "C_DBCLOB", createRowId(new byte[] { (byte) -47, (byte) -98 }));
                failed ("Didn't throw SQLException"+baseAdded);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException", baseAdded);
            }
        }
        }
    }



/**
updateRowId() - Update a BLOB.

SQL400 - the native driver expects this update to work correctly.
**/
    public void Var036 ()
    {
        if (checkJdbc40 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, key_);
                byte[] ba = new byte[] { (byte) 98, (byte) 1 };
                JDReflectionUtil.callMethod_V(rs_, "updateRowId", "C_BLOB", createRowId(ba));

                //if (getDriver() == JDTestDriver.DRIVER_NATIVE) {                  //@D1D
                   rs_.updateRow ();
                   ResultSet rs2 = statement2_.executeQuery (select_);
                   JDRSTest.position (rs2, key_);
                   byte[] v = rs2.getBytes ("C_BLOB");
                   rs2.close ();
                   assertCondition (isEqual (v, ba), baseAdded);
                //} else {                                                          //@D1D
                   //failed ("Didn't throw SQLException"+baseAdded);                          //@D1D
                //}                                                                 //@D1D
            }
            catch (Exception e) {
                //toolbox does not support rowid on this type
                if(isToolboxDriver())
                {
                    assertCondition(e.getMessage().indexOf("Data type mismatch") != -1);
                    return;
                }
               //if (getDriver() == JDTestDriver.DRIVER_NATIVE) {                   //@D1D
                  failed (e, "Unexpected Exception"+ baseAdded);
               //} else {                                                           //@D1D
                  //assertExceptionIsInstanceOf (e, "java.sql.SQLException", baseAdded);       //@DiD
               //}                                                                  //@D1D
            }
        }
        }
    }



/**
updateRowId() - Update a DATE.
**/
    public void Var037 ()
    {
        if (checkJdbc40 ()) {
        try {
            JDRSTest.position (rs_, key_);
            JDReflectionUtil.callMethod_V(rs_, "updateRowId", "C_DATE", createRowId(new byte[] { (byte) 19 }));
            failed ("Didn't throw SQLException"+baseAdded);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException", baseAdded);
        }
        }
    }



/**
updateRowId() - Update a TIME.
**/
    public void Var038 ()
    {
        if (checkJdbc40 ()) {
        try {
            JDRSTest.position (rs_, key_);
            JDReflectionUtil.callMethod_V(rs_, "updateRowId", "C_TIME", createRowId(new byte[] { (byte) 1 }));
            failed ("Didn't throw SQLException"+baseAdded);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException", baseAdded);
        }
        }
    }



/**
updateRowId() - Update a TIMESTAMP.
**/
    public void Var039 ()
    {
        if (checkJdbc40 ()) {
        try {
            JDRSTest.position (rs_, key_);
            JDReflectionUtil.callMethod_V(rs_, "updateRowId", "C_TIMESTAMP", createRowId(new byte[] { (byte) 2 }));
            failed ("Didn't throw SQLException"+baseAdded);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException", baseAdded);
        }
        }
    }



/**
updateRowId() - Update a DATALINK.
**/
    public void Var040 ()
    {
        if (checkJdbc40 ()) {
        if (checkLobSupport ()) {
            // We do not test updating datalinks, since it is not 
            // possible to open a updatable cursor/result set with
            // a datalink column.
            notApplicable("DATALINK update not supported.");
            /*
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs, "updateRowId", "C_DATALINK", createRowId(new byte[] { (byte) 113 }));
                failed ("Didn't throw SQLException"+baseAdded);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException", baseAdded);
            }
            */
        }
        }
    }



/**
updateRowId() - Update a DISTINCT.
**/
    public void Var041 ()
    {
        if (checkJdbc40 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateRowId", "C_DISTINCT", createRowId(new byte[] { (byte) 44 }));
                failed ("Didn't throw SQLException"+baseAdded);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException", baseAdded);
            }
        }
        }
    }



/**
updateRowId() - Update a BIGINT.
**/
    public void Var042()
    {
        if (checkJdbc40 ()) {
        if (checkBigintSupport()) {
        try {
            JDRSTest.position (rs_, key_);
            JDReflectionUtil.callMethod_V(rs_, "updateRowId", "C_BIGINT", createRowId(new byte[] { (byte) 50 }));
            failed ("Didn't throw SQLException"+baseAdded);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException", baseAdded);
        }
        }
        }
    }


    /**
     * updateRowId() -- Update a Real RowId
     */
    /**
    updateRowId() - Should work when the column index is valid.
    **/
        public void Var043()
        {
            boolean passed = false; 
            String message; 
            String sql = "" ;
            String expected="VALUE CANNOT BE SPECIFIED FOR GENERATED ALWAYS"; 
            if (checkJdbc40 ()) {
            try {
                Statement stmt = connection_.createStatement(); 
                String tableName = collection_+".JDRSURID43"; 
                
                initTable(stmt, tableName," (C1 ROWID, C2 INT)");
               
                sql = "insert into "+tableName+" values(DEFAULT, 1)"; 
                stmt.executeUpdate(sql);
                
                sql = "select * from "+tableName;
                
                Statement stmtUpdatable = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
        
                ResultSet rs =  stmtUpdatable.executeQuery(sql);
                sql="rs.next"; 
                rs.next();
                sql="rs.getBytes"; 
                byte[] bytes = rs.getBytes(1); 
                bytes[0xf]+=1;
                sql="createRowId"; 
                Object rid = createRowId(bytes);
                sql="updateRowId"; 
                try { 
                   JDReflectionUtil.callMethod_V(rs, "updateRowId",1, rid);
                   rs.updateRow ();
                   passed=false; 
                   message="Did not throw Exception"; 
                }  catch (Exception e) {
                   message= "Exception is "+e; 
                   String exMessage = e.toString().toUpperCase();
                   if (exMessage.indexOf(expected) >= 0 ) {
                      passed=true; 
                   } else { 
                      passed=false; 
                   }
                }
                rs.close(); 
                stmtUpdatable.close();
                cleanupTable(stmt, tableName); 
                stmt.close();
                assertCondition(passed, message+baseAdded); 
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception sql="+sql+baseAdded);
            }
            }
        }
        
        
        /**
updateRowId() - Update a BOOLEAN.
**/
    public void Var044()
    {
        if (checkJdbc40 ()) {
        if (checkBooleanSupport()) {
        try {
            JDRSTest.position (rs_, key_);
            JDReflectionUtil.callMethod_V(rs_, "updateRowId", "C_BOOLEAN", createRowId(new byte[] { (byte) 50 }));
            failed ("Didn't throw SQLException"+baseAdded);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException", baseAdded);
        }
        }
        }
    }


}



