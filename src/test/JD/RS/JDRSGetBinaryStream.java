///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetBinaryStream.java
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
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DataTruncation;

import java.sql.ResultSet;

import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;



/**
Testcase JDRSGetBinaryStream.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>getBinaryStream()
</ul>
**/
public class JDRSGetBinaryStream
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSGetBinaryStream";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



    // Private data.
    private Statement           statement_;
    private Statement           statement0_;

    private Connection          connection2_;
    private Statement           statement2_;

    // For forcing locators with JDBC 1.0
    private Connection          connection3_;
    private Statement           statement3_;
    StringBuffer sb = new StringBuffer(); 

    private static final byte[] eleven = { (byte) 'E', (byte) 'l', (byte) 'e', (byte) 'v', (byte) 'e', (byte) 'n',
                                           (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ',
                                           (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ',
                                           (byte) ' ', (byte) ' ' };

    private static final byte[] twelve = { (byte) 'T', (byte) 'w', (byte) 'e', (byte) 'l', (byte) 'v',
                                           (byte) 'e' };



/**
Constructor.
**/
    public JDRSGetBinaryStream (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSGetBinaryStream",
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
        String url = baseURL_;
        connection_ = testDriver_.getConnection (url + ";lob threshold=30000",systemObject_.getUserId(),encryptedPassword_,"JDRSGetBinaryStream1");
	setAutoCommit(connection_, false); // @E1A
        statement0_ = connection_.createStatement ();


        // Force LOB locators - JDBC 1.0
        connection3_ = testDriver_.getConnection (url + ";lob threshold=0","JDRSGetBinaryStream1");
	setAutoCommit(connection3_, false); // @E1A

        statement3_ = connection3_.createStatement ();


        if (isJdbc20 ()) {
            statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
            statement_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_GET
                + " (C_KEY) VALUES ('DUMMY_ROW')");
    
            // Force LOB locators.
            connection2_ = testDriver_.getConnection (url + ";lob threshold=0","JDRSGetBinaryStream1");
	    setAutoCommit(connection2_, false); // @E1A


            statement2_ = connection2_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
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
            statement2_.close ();
            connection2_.commit(); // @E1A
            connection2_.close ();
            statement_.close ();
        }

        statement0_.close ();
        connection_.commit(); // @E1A
        connection_.close ();

        statement3_.close();
        connection3_.commit(); // @E1A
        connection3_.close();
    }


    public void cleanupConnections ()
        throws Exception
    {
        if (isJdbc20 ()) {
            connection2_.commit(); // @E1A
            connection2_.close ();
        }

        connection_.commit(); // @E1A
        connection_.close ();

        connection3_.commit(); // @E1A
        connection3_.close();
    }



/**
getBinaryStream() - Should throw exception when the result set is
closed.
**/
    public void Var001()
    {
        try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            rs.close ();
            rs.getBinaryStream (1);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBinaryStream() - Should throw exception when cursor is not pointing
to a row.
**/
    public void Var002()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            InputStream v = rs.getBinaryStream (1);
            failed ("Didn't throw SQLException"+v+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBinaryStream() - Should throw an exception when the column
is an invalid index.
**/
    public void Var003()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            InputStream v = rs.getBinaryStream (100);
            failed ("Didn't throw SQLException"+v+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBinaryStream() - Should throw an exception when the column
is 0.
**/
    public void Var004()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            InputStream v = rs.getBinaryStream (0);
            failed ("Didn't throw SQLException"+v+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBinaryStream() - Should throw an exception when the column
is -1.
**/
    public void Var005()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            InputStream v = rs.getBinaryStream (-1);
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBinaryStream() - Should work when the column index is valid.
**/
    public void Var006()
    {
        try {
          sb.setLength(0);
ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_NOTRANS");
            InputStream v = rs.getBinaryStream (18);
	    if(getDriver() == JDTestDriver.DRIVER_NATIVE &&		// @K2
	       true)		// @K2
		assertCondition( compareBeginsWithBytes( v, twelve,sb),sb);	// @K2
	    else							// @K2
		assertCondition (compare (v, twelve,sb),sb);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBinaryStream() - Should throw an exception when the column
name is null.
**/
    public void Var007()
    {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("jcc throws NPE when column name is null");
          return; 
        }
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_NOTRANS");
            rs.getBinaryStream (null);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBinaryStream() - Should throw an exception when the column
name is an empty string.
**/
    public void Var008()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_NOTRANS");
            rs.getBinaryStream ("");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBinaryStream() - Should throw an exception when the column
name is invalid.
**/
    public void Var009()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_NOTRANS");
            rs.getBinaryStream ("INVALID");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBinaryStream() - Should throw an exception when the column name is valid.
**/
    public void Var010()
    {
        try {
          sb.setLength(0);
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_NOTRANS");
            InputStream v = rs.getBinaryStream ("C_BINARY_20");
	    if(getDriver() == JDTestDriver.DRIVER_NATIVE &&		// @K2
	       true)		// @K2
		assertCondition( compareBeginsWithBytes( v, eleven,sb),sb);	// @K2
	    else							// @K2
		assertCondition (compare (v, eleven,sb),sb);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBinaryStream() - Should work when an update is pending.
**/
    public void Var011()
    {
        if (checkJdbc20 ()) {
        try {                              
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET + " FOR UPDATE");
            JDRSTest.position (rs, "UPDATE_SANDBOX");
            byte[] test = new byte[] { (byte) 0x34, (byte) 0x45, (byte) 0x50,
                           (byte) 0x56, (byte) 0x67, (byte) 0x78, (byte) 0x89, (byte) 0x9A,
                           (byte) 0xAB, (byte) 0xBC };
            rs.updateBytes ("C_VARBINARY_20", test);
            InputStream v = rs.getBinaryStream ("C_VARBINARY_20");
	    boolean check;					// @K2
      sb.setLength(0);

	    if(getDriver() == JDTestDriver.DRIVER_NATIVE &&	// @K2
	       true)	// @K2
		check = compareBeginsWithBytes( v, test,sb);	// @K2
	    else						// @K2
		check = compare (v, test,sb);
            rs.close();
            assertCondition(check,sb);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getBinaryStream() - Should work when an update has been done.
**/
    public void Var012()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET + " FOR UPDATE");
            JDRSTest.position (rs, "UPDATE_SANDBOX");
            byte[] test = new byte[] { (byte) 0x01, (byte) 0x12, (byte) 0x34, (byte) 0x45, (byte) 0x50,
                                       (byte) 0x56, (byte) 0x67, (byte) 0x78, (byte) 0x89, (byte) 0x9A,
                                       (byte) 0xAB, (byte) 0xBC, (byte) 0xCD, (byte) 0xDE, (byte) 0xEF,
                                       (byte) 0xFF, (byte) 0x00, (byte) 0xFF, (byte) 0x00, (byte) 0xFF };
            rs.updateBytes ("C_BINARY_20", test);
            rs.updateRow ();
            InputStream v = rs.getBinaryStream ("C_BINARY_20");	
	    boolean check;					// @K2                sb.setLength(0);
      sb.setLength(0);

	    if(getDriver() == JDTestDriver.DRIVER_NATIVE &&	// @K2
	       true)	// @K2
		check = compareBeginsWithBytes( v, test,sb);	// @K2
	    else						// @K2
		check = compare (v, test,sb);			// @K2
            rs.close();
            assertCondition(check,sb);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getBinaryStream() - Should work when the current row is the insert
row, when an insert is pending.
**/
    public void Var013()
    {
        if (checkJdbc20 ()) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            notApplicable("JCC does not support moveToInserRow"); 
            return; 
          }
        try {
          sb.setLength(0);
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET + " FOR UPDATE");
            rs.moveToInsertRow ();            
            byte[] test = new byte[] { (byte) 0xBC, (byte) 0xCD };
            rs.updateBytes ("C_VARBINARY_20", test);
            InputStream v = rs.getBinaryStream ("C_VARBINARY_20");
            boolean check;					// @K2
	    if(getDriver() == JDTestDriver.DRIVER_NATIVE &&	// @K2
	       true)	// @K2
		check = compareBeginsWithBytes( v, test,sb);	// @K2
	    else						// @K2
		check = compare (v, test,sb);			// @K2
            rs.close();
            assertCondition(check,sb);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }

            

/**
getBinaryStream() - Should work when the current row is the insert
row, when an insert has been done.
**/
    public void Var014()
    {
        if (checkJdbc20 ()) {
        try {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            notApplicable("JCC does not support moveToInserRow"); 
            return; 
          }
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET + " FOR UPDATE");
            rs.moveToInsertRow ();
            byte[] test = new byte[] { (byte) 0xBC, (byte) 0xCD, (byte) 0xDE, (byte) 0xEF,
                                       (byte) 0xFF, (byte) 0x00, (byte) 0xFF, (byte) 0x00, (byte) 0xFF };
            rs.updateBytes ("C_VARBINARY_20", test);
            rs.insertRow ();
            InputStream v = rs.getBinaryStream ("C_VARBINARY_20");
            sb.setLength(0);
	    boolean check;						// @K2
	    if(getDriver() == JDTestDriver.DRIVER_NATIVE &&		// @K2
	       true)		// @K2
		check = compareBeginsWithBytes( v, test,sb);		// @K2
	    else							// @K2
		check = (compare (v, test,sb));				// @K2
            rs.close();
            assertCondition(check,sb);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getBinaryStream() - Should throw an exception on a deleted row.
**/
    public void Var015()
    {
        if (checkJdbc20 ()) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) { 
            notApplicable("JCC doesn't throw exception for get on deleted row");
            return; 
          }
            ResultSet rs = null;
            try {
                rs = statement_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET + " FOR UPDATE");
                JDRSTest.position (rs, "DUMMY_ROW");
                rs.deleteRow ();
                InputStream v = rs.getBinaryStream ("C_VARBINARY_20");
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
            finally {
                rs = null;
            }
        }
    }


/**
getBinaryStream() - Should return null when the column is NULL.
**/
    public void Var016 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_NULL");
            InputStream v = rs.getBinaryStream ("C_VARBINARY_20");
            assertCondition (v == null);        
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

           

/**
getBinaryStream() - Get from a SMALLINT.
**/
    public void Var017 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            InputStream v = rs.getBinaryStream ("C_SMALLINT");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBinaryStream() - Get from a INTEGER.
**/
    public void Var018 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            InputStream v = rs.getBinaryStream ("C_INTEGER");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBinaryStream() - Get from a REAL.
**/
    public void Var019 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            InputStream v = rs.getBinaryStream ("C_REAL");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBinaryStream() - Get from a FLOAT.
**/
    public void Var020 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            InputStream v = rs.getBinaryStream ("C_FLOAT");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBinaryStream() - Get from a DOUBLE.
**/
    public void Var021 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            InputStream v = rs.getBinaryStream ("C_DOUBLE");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBinaryStream() - Get from a DECIMAL.
**/
    public void Var022 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            InputStream v = rs.getBinaryStream ("C_DECIMAL_50");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBinaryStream() - Get from a NUMERIC.
**/
    public void Var023 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            InputStream v = rs.getBinaryStream ("C_NUMERIC_105");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }


/**
getBinaryStream() - Get from a CHAR.
**/
    public void Var024 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            InputStream v = rs.getBinaryStream ("C_CHAR_50");
            if( isToolboxDriver() )            //@F1A
                succeeded();                                            //@F1A
            else                                                        //@F1A
                failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            if(isToolboxDriver())              //@F1A
                failed(e, "Unexpected Exception");                      //@F1A
            else                                                        //@F1A
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBinaryStream() - Get from a VARCHAR.
**/
    public void Var025 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            InputStream v = rs.getBinaryStream ("C_VARCHAR_50");
            if( isToolboxDriver() )            //@F1A
                succeeded();                                            //@F1A
            else                                                        //@F1A
                failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            if(isToolboxDriver())              //@F1A
                failed(e, "Unexpected Exception");                      //@F1A
            else                                                        //@F1A
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBinaryStream() - Get from a BINARY.
**/
    public void Var026 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_NOTRANS");
            InputStream v = rs.getBinaryStream ("C_BINARY_20");
	    if(getDriver() == JDTestDriver.DRIVER_NATIVE &&		// @K2
	       true)		// @K2
		assertCondition( compareBeginsWithBytes( v, eleven,sb),sb);	// @K2
	    else							// @K2
		assertCondition (compare (v, eleven,sb),sb);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBinaryStream() - Get from a VARBINARY.
**/
    public void Var027 ()
    {
        try {
          sb.setLength(0);
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_NOTRANS");
            InputStream v = rs.getBinaryStream ("C_VARBINARY_20");
	    if(getDriver() == JDTestDriver.DRIVER_NATIVE &&		// @K2
	       true)		// @K2
		assertCondition( compareBeginsWithBytes( v, twelve,sb),sb);	// @K2
	    else							// @K2
		assertCondition (compare (v, twelve,sb),sb);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBinaryStream() - Get from a CLOB.
**/
    public void Var028 ()
    {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                InputStream v = rs.getBinaryStream ("C_CLOB");
                if( isToolboxDriver() )            //@F1A
                succeeded();                                            //@F1A
            else                                                        //@F1A
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
            if(isToolboxDriver())              //@F1A
                failed(e, "Unexpected Exception");                      //@F1A
            else                                                        //@F1A
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getBinaryStream() - Get from a DBCLOB.
**/
    public void Var029 ()
    {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                InputStream v = rs.getBinaryStream ("C_DBCLOB");
                if( isToolboxDriver() )            //@F1A
                succeeded();                                            //@F1A
            else                                                        //@F1A
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
            if(isToolboxDriver())              //@F1A
                failed(e, "Unexpected Exception");                      //@F1A
            else                                                        //@F1A
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getBinaryStream() - Get from a BLOB, when the BLOB data is
returned in the result set.
**/
    public void Var030 ()
    {       
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                InputStream v = rs.getBinaryStream ("C_BLOB");
                sb.setLength(0);
		if(getDriver() == JDTestDriver.DRIVER_NATIVE &&				// @K2
		   true)				// @K2
		    assertCondition( compareBeginsWithBytes( v, JDRSTest.BLOB_FULL,sb),sb);	// @K2
		else									// @K2
		    assertCondition (compare (v, JDRSTest.BLOB_FULL,sb),sb);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getBinaryStream() - Get from a BLOB, when the BLOB locator is
returned in the result set.
**/
    public void Var031 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("jcc hangs");
        return; 
      }

      if (checkLobSupport ()) {
            try {
                ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                InputStream v = rs.getBinaryStream ("C_BLOB");
		boolean check;							// @K2
    sb.setLength(0);
		if(getDriver() == JDTestDriver.DRIVER_NATIVE &&			// @K2
		   true)			// @K2
		    check = compareBeginsWithBytes( v, JDRSTest.BLOB_FULL,sb);	// @K2
		else								// @K2
		    check = (compare (v, JDRSTest.BLOB_FULL,sb));			// @K2
                rs.close();
                assertCondition(check,sb);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getBinaryStream() - Get from a DATE.
**/
    public void Var032 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            InputStream v = rs.getBinaryStream ("C_DATE");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBinaryStream() - Get from a TIME.
**/
    public void Var033 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            InputStream v = rs.getBinaryStream ("C_TIME");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBinaryStream() - Get from a TIMESTAMP.
**/
    public void Var034 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            InputStream v = rs.getBinaryStream ("C_TIMESTAMP");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBinaryStream() - Get from a DATALINK.
**/
    public void Var035 ()
    {
        if (checkDatalinkSupport ()) {
            try {
                Statement s = connection_.createStatement ();
                ResultSet rs = s.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GETDL);
                JDRSTest.position0 (rs, "LOB_FULL");
                InputStream v = rs.getBinaryStream ("C_DATALINK");
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getBinaryStream() - Get from a DISTINCT.
**/
    public void Var036 ()
    {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_EMPTY");
                InputStream v = rs.getBinaryStream ("C_DISTINCT");
                if( isToolboxDriver() )            //@F1A
                succeeded();                                            //@F1A
            else                                                        //@F1A
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
            if(isToolboxDriver())              //@F1A
                failed(e, "Unexpected Exception");                      //@F1A
            else                                                        //@F1A
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getBinaryStream() - Get from a BIGINT.
**/
    public void Var037 ()
    {
        if (checkBigintSupport()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            InputStream v = rs.getBinaryStream ("C_BIGINT");
            byte[] bigint = longToByteArray(12374321);
	    failed ("Didn't throw SQLException but got "+v+" "+bigint);
        }
        catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getBinaryStream() - Verify that no DataTruncation is posted
when the max field size is set to 0.
**/
    public void Var038()
    {
        try {
            Statement s = connection_.createStatement ();
            s.setMaxFieldSize (0);
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_NOTRANS");
            InputStream v = rs.getBinaryStream ("C_VARBINARY_20");
            DataTruncation dt = (DataTruncation) rs.getWarnings ();
            rs.close ();
            s.close ();
            sb.setLength(0);
	    if(getDriver() == JDTestDriver.DRIVER_NATIVE &&				// @K2
	       true)				// @K2
		assertCondition( compareBeginsWithBytes( v, twelve,sb) && dt == null,sb);	// @K2
	    else									// @K2
		assertCondition ((compare (v, twelve,sb)) && (dt == null),sb);		// @K2
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBinaryStream() - Verify that data is truncated without
a DataTruncation posted when the max field size is set 
to a value shorter than the byte array.
**/
    public void Var039()
    {
        try {
            Statement s = connection_.createStatement ();
            s.setMaxFieldSize (18);
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_NOTRANS");
            InputStream v = rs.getBinaryStream ("C_BINARY_20");
            SQLWarning w = rs.getWarnings ();
            rs.close ();
            s.close ();
            byte[] expected =  { (byte) 'E', (byte) 'l', (byte) 'e', (byte) 'v', (byte) 'e', (byte) 'n',
                                 (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ',
                                 (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ' };

            sb.setLength(0);
	    if(getDriver() == JDTestDriver.DRIVER_NATIVE &&				// @K2
	       true)				// @K2
		assertCondition( compareBeginsWithBytes( v, expected,sb) && w == null,sb);	// @K2
	    else									// @K2
		assertCondition ((compare (v, expected,sb)) && (w == null),sb);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    
    /**
    getBinaryStream() - Get from DFP16:
    **/
   public void Var040 ()
   {
     if (checkDecFloatSupport()) {
       try {
         Statement s = connection_.createStatement ();
         ResultSet rs = s.executeQuery ("SELECT * FROM "
             + JDRSTest.RSTEST_DFP16);
         rs.next(); 
         InputStream v = rs.getBinaryStream (1);
         failed ("Didn't throw SQLException "+v);
       }
       catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
       }
     }
   }
   
   
   
   /**
    getBinaryStream() - Get from DFP34:
    **/
   public void Var041 ()
   {
     if (checkDecFloatSupport()) {
       try {
         Statement s = connection_.createStatement ();
         ResultSet rs = s.executeQuery ("SELECT * FROM "
             + JDRSTest.RSTEST_DFP34);
         rs.next(); 
         InputStream v = rs.getBinaryStream (1);
         failed ("Didn't throw SQLException "+v);
       }
       catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
       }
     }
   }
   
   
   /**
   getBinaryStream() - Get from an SQLXML. 
   **/
  public void Var042() {
    if (checkXmlSupport()) {
      try {
        sb.setLength(0);
        ResultSet rs = statement0_.executeQuery("SELECT * FROM "
            + JDRSTest.RSTEST_GETXML +" A ORDER BY RRN(A)");
        rs.next();
        InputStream v = rs.getBinaryStream("C_XML");
       
       	byte[] expected = { 0x3c,0x67,0x72,0x65,0x65,0x74,0x69,0x6e,0x67,0x3e,0x48,
            0x65,0x6c,0x6c,0x6f,0x2c,0x20,0x77,0x6f,0x72,0x6c,0x64,0x21,0x3c,
            0x2f,0x67,0x72,0x65,0x65,0x74,0x69,0x6e,0x67,0x3e};
        //toolbox current behavior is that host returns xml with declaration and with whitespace trimmed.
        if(isToolboxDriver()) 
            expected = JDRSTest.SAMPLE_XML1_OUTXML_WITHDECL_UTF8; 
        
        assertCondition ((compare (v, expected, sb)),sb);

      
      } catch (Exception e) {
        failed (e, "Unexpected Exception");
      }

    }
  }


        /**
         * getBinaryStream() - Get from a TIMESTAMP(12).
         **/
        public void Var043() {
            if (checkTimestamp12Support()) {
              testGet(statement0_,
                  "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(12)) from SYSIBM.SYSDUMMY1",
                  "getBinaryStream", 
                  "EXCEPTION:Data type mismatch.", 
                  " -- added 11/19/2012"); 

            }
        }


        /**
         * getBinaryStream() - Get from a TIMESTAMP(0).
         **/
        public void Var044() {
            if (checkTimestamp12Support()) {
          testGet(statement0_,
            "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(0)) from SYSIBM.SYSDUMMY1",
            "getBinaryStream",
		  "EXCEPTION:Data type mismatch.", 
            " -- added 11/19/2012"); 
            
            
            }
        }

/**
getBinaryStream() - Get from a BOOLEAN.
**/
	public void Var045 ()
	{
	    if (checkBooleanSupport()) {
		try {
		    ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							     + JDRSTest.RSTEST_BOOLEAN);
		    JDRSTest.position0 (rs, "BOOLEAN_TRUE");
		    InputStream v = rs.getBinaryStream ("C_BOOLEAN");
		    failed ("Didn't throw SQLException"+v);
		}
		catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}


/**
getBinaryStream() - Get from a BOOLEAN.
**/
	public void Var046 ()
	{
	    if (checkBooleanSupport()) {
		try {
		    ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							     + JDRSTest.RSTEST_BOOLEAN);
		    JDRSTest.position0 (rs, "BOOLEAN_FALSE");
		    InputStream v = rs.getBinaryStream ("C_BOOLEAN");
		    failed ("Didn't throw SQLException"+v);
		}
		catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}

/**
getBinaryStream() - Get from a BOOLEAN.
**/
    public void Var047()
    {
        if (checkBooleanSupport()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_BOOLEAN);            
            JDRSTest.position0 (rs, "BOOLEAN_NULL");
            InputStream v = rs.getBinaryStream ("C_BOOLEAN");
            assertCondition (v == null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




    
    
    //@K1A
    /**
      Convert the specified long into server format in the specified byte array.
      @param  longValue  The value to be converted to server format.
      @param  serverValue  The array to receive the data type in server format.
      @param  offset  The offset into the byte array for the start of the server value.
     **/
    public static void longToByteArray(long longValue, byte[] serverValue, int offset)
    {
	// Do in two parts to avoid long temps.
	int high = (int)(longValue >>> 32);
	int low = (int)longValue;

	serverValue[offset]   = (byte)(high >>> 24);
	serverValue[offset+1] = (byte)(high >>> 16);
	serverValue[offset+2] = (byte)(high >>>  8);
	serverValue[offset+3] = (byte) high;

	serverValue[offset+4] = (byte)(low >>> 24);
	serverValue[offset+5] = (byte)(low >>> 16);
	serverValue[offset+6] = (byte)(low >>>  8);
	serverValue[offset+7] = (byte) low;
    }

    //@K1A
    /**
      Convert the specified long into server format in the specified byte array.
      @param  longValue  The value to be converted to server format.
      @return  The array with the data type in server format.
     **/
    public static byte[] longToByteArray(long longValue)
    {
        byte[] serverValue = new byte[8];
        longToByteArray(longValue, serverValue, 0);
        return serverValue;
    }
}



