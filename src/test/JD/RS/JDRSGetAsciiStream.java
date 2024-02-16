///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetAsciiStream.java
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
import test.Testcase;
import test.JD.JDTestUtilities;

import java.io.FileOutputStream;
import java.io.InputStream;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDRSGetAsciiStream.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>getAsciiStream()
</ul>
**/
public class JDRSGetAsciiStream
extends JDTestcase
{



    // Private data.
    private Statement           statement_;
    private Statement           statement0_;
    private Connection          connection2_;
    private Connection          connectionLocators_; 
    private Statement           statementLocators_;
    private boolean             isJDK14 = false; 
    StringBuffer sb = new StringBuffer(); 


/**
Constructor.
**/
    public JDRSGetAsciiStream (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSGetAsciiStream",
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


	// 
	// look for jdk1.4
	//
		isJDK14 = true; 

	if (connection_ != null) connection_.close();


        String url = baseURL_;
        connection_ = testDriver_.getConnection (url + ";lob threshold=30000",
            systemObject_.getUserId(),
            encryptedPassword_,"JDRSGetAsciiStream1");
	
	setAutoCommit(connection_, false); // @E1A
        statement0_ = connection_.createStatement ();

        connectionLocators_ = testDriver_.getConnection (url + ";lob threshold=0",systemObject_.getUserId(),encryptedPassword_,"JDRSGetAsciiStream2");
	setAutoCommit(connectionLocators_,false); // @E1A

        statementLocators_ = connectionLocators_.createStatement();

        if (isJdbc20 ()) {
            statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
            statement_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_GET
                + " (C_KEY) VALUES ('DUMMY_ROW')");
    
            // Force LOB locators.
            connection2_ = testDriver_.getConnection (url + ";lob threshold=0",systemObject_.getUserId(),encryptedPassword_,"JDRSGetAsciiStream3");

	    setAutoCommit(connection2_, false); // @E1A

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
            connection2_.commit(); // @E1A
            connection2_.close ();
            if (statement_ != null) statement_.close ();
        }

        statement0_.close ();
        connection_.commit(); // @E1A
        connection_.close ();

        statementLocators_.close();
        connectionLocators_.commit(); // @E1A
        connectionLocators_.close(); 
    }

    public  void cleanupConnections ()
        throws Exception
    {
        if (isJdbc20 ()) {
            connection2_.commit(); // @E1A
            connection2_.close ();
        }

        connection_.commit(); // @E1A
        connection_.close ();

        connectionLocators_.commit(); // @E1A
        connectionLocators_.close(); 
    }

    
    

    protected static boolean compareBeginsWithBytes (InputStream i, byte[] b, StringBuffer sb)                   // @K2
    // this compare doesn't check if inputStream has more bytes or not beyond containing b
    // unlike JDTestcase.compare which see thats inputStream should contain only b and nothing
    // beyond it.
    // this exclusion is esp. needed for native driver from v5r3m0 onwards
  {
      try
      {
          byte[] buf = new byte[b.length];
          int num = i.read(buf, 0, buf.length);
          if(num == -1)
                return b.length == 0;
          int total = num;
          while(num > -1 && total < buf.length)
          {
              num = i.read(buf, total, buf.length-total);
              total += num;
          }
          if(num == -1)
                --total;
          boolean passed = total == b.length && Testcase.areEqual(b, buf);
          if (!passed) {
	      JDTestUtilities.outputComparison(b, buf, sb);
          }
          return passed; 
      }
      catch(java.io.IOException e)
      {
          return false;
      }
  }



/**
getAsciiStream() - Should throw exception when the result set is
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
            rs.getAsciiStream (1);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getAsciiStream() - Should throw exception when cursor is not pointing
to a row.
**/
    public void Var002()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            InputStream v = rs.getAsciiStream (1);
            failed ("Didn't throw SQLException "+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getAsciiStream() - Should throw an exception when the column
is an invalid index.
**/
    public void Var003()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            InputStream v = rs.getAsciiStream (100);
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getAsciiStream() - Should throw an exception when the column
is 0.
**/
    public void Var004()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            InputStream v = rs.getAsciiStream (0);
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getAsciiStream() - Should throw an exception when the column
is -1.
**/
    public void Var005()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            InputStream v = rs.getAsciiStream (-1);
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getAsciiStream() - Should work when the column index is valid.
**/
    public void Var006()
    {
      sb.setLength(0); 
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            InputStream v = rs.getAsciiStream (12);
	    if( getDriver() == JDTestDriver.DRIVER_NATIVE &&		// @K2
		getRelease() >= JDTestDriver.RELEASE_V7R1M0 )		// @K2
		assertCondition ( compareBeginsWithBytes(		// @K2
                    v, "Toolbox for Java                                  ".getBytes("8859_1"),sb),sb); // @K2
	    else							// @K2
		assertCondition (compare (				// @K2
                    v, "Toolbox for Java                                  ", "8859_1",sb),sb);	  // @K2
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAsciiStream() - Should throw an exception when the column
name is null.
**/
    public void Var007()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            rs.getAsciiStream (null);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getAsciiStream() - Should throw an exception when the column
name is an empty string.
**/
    public void Var008()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            rs.getAsciiStream ("");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getAsciiStream() - Should throw an exception when the column
name is invalid.
**/
    public void Var009()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            rs.getAsciiStream ("INVALID");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getAsciiStream() - Should work when the column name is valid.
**/
    public void Var010()
    {
      sb.setLength(0); 
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            InputStream v = rs.getAsciiStream ("C_CHAR_50");
	    if( getDriver() == JDTestDriver.DRIVER_NATIVE &&			// @K2
		getRelease() >= JDTestDriver.RELEASE_V7R1M0 )			// @K2
		assertCondition ( compareBeginsWithBytes(			// @K2
                    v, "Toolbox for Java                                  ".getBytes("8859_1"),sb),sb);  // @K2
	    else								// @K2
		assertCondition (compare (					// @K2
                    v, "Toolbox for Java                                  ", "8859_1",sb),sb);	   // @K2
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAsciiStream() - Should work when an update is pending.
**/
    public void Var011()
    {
      sb.setLength(0); 

        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET + " FOR UPDATE");
            JDRSTest.position (rs, "UPDATE_SANDBOX");
            rs.updateString ("C_VARCHAR_50", "World Peace");
            InputStream v = rs.getAsciiStream ("C_VARCHAR_50");
	    boolean check;								// @K2
	    if( getDriver() == JDTestDriver.DRIVER_NATIVE &&				// @K2
		getRelease() >= JDTestDriver.RELEASE_V7R1M0 )				// @K2
		check = compareBeginsWithBytes( v, "World Peace".getBytes("8859_1"),sb);	// @K2
	    else									// @K2
		check = compare (v, "World Peace", "8859_1",sb);				// @K2
	    rs.close();
            assertCondition(check,sb);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getAsciiStream() - Should work when an update has been done.
**/
    public void Var012()
    {
      sb.setLength(0); 

        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET + " FOR UPDATE");
            JDRSTest.position (rs, "UPDATE_SANDBOX");
            rs.updateString ("C_CHAR_50", "New Planet");
            rs.updateRow ();
            InputStream v = rs.getAsciiStream ("C_CHAR_50");
            boolean check;					// @K2
	    if( getDriver() == JDTestDriver.DRIVER_NATIVE &&	// @K2
		getRelease() >= JDTestDriver.RELEASE_V7R1M0 )	// @K2
		check = compareBeginsWithBytes(			// @K2
                                 v, "New Planet                                        ".getBytes("8859_1"),sb);     // @K2
	    else if (getDriver() == JDTestDriver.DRIVER_JCC)	 {
                check = compare (v, "New Planet", "8859_1",sb); // @K2
            } else       // @K2            
		check = compare (v, "New Planet                                        ", "8859_1",sb); // @K2
              String sVal = ""; 
            if (!check) { 
              sVal = rs.getString("C_CHAR_50"); 
            }
            rs.close();
            assertCondition(check, "\nExpected 'New Planet                                        ' "+
                                   "\nGot      '"+sVal+"'"+sb);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }       
        }
    }



/**
getAsciiStream() - Should work when the current row is the insert
row, when an insert is pending.
**/
    public void Var013()
    {
      sb.setLength(0); 

        if (checkJdbc20 ()) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            notApplicable("JCC does not support moveToInserRow"); 
            return; 
          }
        try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET + " FOR UPDATE");
            rs.moveToInsertRow ();
            rs.updateString ("C_VARCHAR_50", "El Nino");
            InputStream v = rs.getAsciiStream ("C_VARCHAR_50");
            boolean check;								// @K2
	    if( getDriver() == JDTestDriver.DRIVER_NATIVE &&				// @K2
		getRelease() >= JDTestDriver.RELEASE_V7R1M0 )				// @K2
		check = compareBeginsWithBytes( v, "El Nino".getBytes("8859_1"),sb);	// @K2
	    else									// @K2
		check = compare (v, "El Nino", "8859_1",sb);				// @K2
            rs.close();
            assertCondition(check,sb);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }

            

/**
getAsciiStream() - Should work when the current row is the insert
row, when an insert has been done.
**/
    public void Var014()
    {
      sb.setLength(0); 

        if (checkJdbc20 ()) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            notApplicable("JCC does not support moveToInserRow"); 
            return; 
          }
        try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET + " FOR UPDATE");
            rs.moveToInsertRow ();
            rs.updateString ("C_VARCHAR_50", "Year 2000 Problem");
            rs.insertRow ();
            InputStream v = rs.getAsciiStream ("C_VARCHAR_50");
            boolean check;						// @K2
	    if( getDriver() == JDTestDriver.DRIVER_NATIVE &&		// @K2
		getRelease() >= JDTestDriver.RELEASE_V7R1M0 )		// @K2
		check = compareBeginsWithBytes( v, "Year 2000 Problem".getBytes("8859_1"),sb);	// @K2
	    else							// @K2
		check = compare (v, "Year 2000 Problem", "8859_1",sb);	// @K2
            rs.close();
            assertCondition(check,sb);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getAsciiStream() - Should throw an exception on a deleted row.
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
                InputStream v = rs.getAsciiStream ("C_VARCHAR_50");
                failed ("Didn't throw SQLException but got v"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
            finally {
                try {
                    if (rs != null) rs.close();
                }
                catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


/**
getAsciiStream() - Should return null when the column is NULL.
**/
    public void Var016 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_NULL");
            InputStream v = rs.getAsciiStream ("C_VARCHAR_50");
            assertCondition (v == null);        
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAsciiStream() - Get from a SMALLINT.
**/
    public void Var017 ()
    {
      sb.setLength(0); 
sb.append(" -- Updated 12/14/2011 Since a smallint can be converted to string, it can be converted to ASCII stream"); 
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            InputStream v = rs.getAsciiStream ("C_SMALLINT");

	    assertCondition(compare(v, "198", "8859_1", sb),sb);     

        }
        catch (Exception e) {

	    failed (e, "Unexpected Exception"+sb);         

        }
    }



/**
getAsciiStream() - Get from a INTEGER.
**/
    public void Var018 ()
    {
      sb.setLength(0); 
	sb.append(" -- Updated 12/14/2011 Since a integer can be converted to string, it can be converted to ASCII stream"); 

        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            InputStream v = rs.getAsciiStream ("C_INTEGER");

	    assertCondition(compare(v, "-98765", "8859_1", sb),sb);     
        }
        catch (Exception e) {
	    failed (e, "Unexpected Exception"+sb);         

        }
    }



/**
getAsciiStream() - Get from a REAL.
**/
    public void Var019 ()
    {
      sb.setLength(0); 
	sb.append(" -- Updated 12/14/2011 Since a real can be converted to string, it can be converted to ASCII stream"); 

        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            InputStream v = rs.getAsciiStream ("C_REAL");

	    assertCondition(compare(v, "4.4", "8859_1", sb),sb);     
        }
        catch (Exception e) {
	    failed (e, "Unexpected Exception"+sb);         
        }
    }



/**
getAsciiStream() - Get from a FLOAT.
**/
    public void Var020 ()
    {
      sb.setLength(0); 
	sb.append(" -- Updated 12/14/2011 Since a float can be converted to string, it can be converted to ASCII stream"); 

        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            InputStream v = rs.getAsciiStream ("C_FLOAT");
	    assertCondition(compare(v, "-5.55", "8859_1", sb),sb);     

        }
        catch (Exception e) {

	    failed (e, "Unexpected Exception"+sb);         
        }
    }



/**
getAsciiStream() - Get from a DOUBLE.
**/
    public void Var021 ()
    {
      sb.setLength(0); 
	sb.append(" -- Updated 12/14/2011 Since a double can be converted to string, it can be converted to ASCII stream"); 

        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            InputStream v = rs.getAsciiStream ("C_DOUBLE");
	    assertCondition(compare(v, "6.666", "8859_1", sb),sb);     

        }
        catch (Exception e) {
	    failed (e, "Unexpected Exception"+sb);         



        }
    }



/**
getAsciiStream() - Get from a DECIMAL.
**/
    public void Var022 ()
    {
      sb.setLength(0); 

        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
                InputStream v = rs.getAsciiStream("C_DECIMAL_50");                                  //@F1A
		assertCondition(compare(v, "7", "8859_1", sb), "update 01/04/2012 for native driver "+sb.toString());                                     //@F1A
        }
        catch (Exception e) {
                failed(e, "Unexpected Exception update 01/04/2012 for native driver");                                                  //@F1A
        }
    }



/**
getAsciiStream() - Get from a NUMERIC.
**/
    public void Var023 ()
    {
      sb.setLength(0); 
      sb.append(" updated 01/04/2012 for native driver"); 

        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
		InputStream v = rs.getAsciiStream("C_NUMERIC_105");                         

		assertCondition(compare(v, "-10.10105", "8859_1",sb));                     //@F1A
        }
        catch (Exception e) {
	    failed(e, "Unexpected Exception updated 01/04/2012 for native driver ");                                          //@F1A
        }
    }


/**
getAsciiStream() - Get from an empty CHAR.
**/
    public void Var024 ()
    {
      sb.setLength(0); 

        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_EMPTY");
            InputStream v = rs.getAsciiStream ("C_CHAR_50");
	    if( getDriver() == JDTestDriver.DRIVER_NATIVE &&		// @K2
		getRelease() >= JDTestDriver.RELEASE_V7R1M0 )		// @K2
		assertCondition( compareBeginsWithBytes( v,		// @K2
		"                                                  ".getBytes("8859_1"),sb),sb);	  // @K2
	    else							// @K2
		assertCondition (compare (v, "                                                  ", "8859_1",sb),sb);					  // @K2
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }              
    }



/**
getAsciiStream() - Get from a full CHAR.
**/
    public void Var025 ()
    {
      sb.setLength(0); 

        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            InputStream v = rs.getAsciiStream ("C_CHAR_50");
	    if( getDriver() == JDTestDriver.DRIVER_NATIVE &&	// @K2
		getRelease() >= JDTestDriver.RELEASE_V7R1M0 )	// @K2
		assertCondition( compareBeginsWithBytes( v,	// @K2
                   "Toolbox for Java                                  ".getBytes("8859_1"),sb),sb);	    // @K2
	    else						// @K2
		assertCondition (compare (v, "Toolbox for Java                                  ",  "8859_1",sb),sb);				  // @K2
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }              
    }



/**
getAsciiStream() - Get from an empty VARCHAR.
**/
    public void Var026 ()
    {
      sb.setLength(0); 

        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_EMPTY");
            InputStream v = rs.getAsciiStream ("C_VARCHAR_50");
	    if( getDriver() == JDTestDriver.DRIVER_NATIVE &&				// @K2
		getRelease() >= JDTestDriver.RELEASE_V7R1M0 )				// @K2
		assertCondition( compareBeginsWithBytes( v, "".getBytes("8859_1"),sb),sb);	// @K2
	    else									// @K2
		assertCondition (compare (v, "", "8859_1",sb),sb);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAsciiStream() - Get from a full VARCHAR.
**/
    public void Var027 ()
    {
      sb.setLength(0); 

        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            InputStream v = rs.getAsciiStream ("C_VARCHAR_50");
	    if( getDriver() == JDTestDriver.DRIVER_NATIVE &&		// @K2
		getRelease() >= JDTestDriver.RELEASE_V7R1M0 )		// @K2
		assertCondition( compareBeginsWithBytes( v, "Java Toolbox".getBytes("8859_1"),sb),sb); // @K2
	    else							// @K2
		assertCondition (compare (v, "Java Toolbox", "8859_1",sb),sb);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAsciiStream() - Get from a BINARY.
**/
    public void Var028 ()
    {
      sb.setLength(0); 

        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_NOTRANS");
            InputStream v = rs.getAsciiStream ("C_BINARY_20");
            sb.setLength(0); 
            if(isToolboxDriver())                          //@F1A
                assertCondition(compare(v, "456C6576656E2020202020202020202020202020", "8859_1",sb),sb);    //@F1A
            else  if(getRelease() < JDTestDriver.RELEASE_V7R1M0)		// @K2
                assertCondition (compare (v, "Eleven              ", "8859_1",sb),sb);
	    else								// @K2
		assertCondition( compareBeginsWithBytes( v, "Eleven              ".getBytes("8859_1"),sb),sb); // @K2
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAsciiStream() - Get from a VARBINARY.
**/
    public void Var029 ()
    {
      sb.setLength(0); 

        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_NOTRANS");
            InputStream v = rs.getAsciiStream ("C_VARBINARY_20");
            if(isToolboxDriver())                          //@F1A
                assertCondition(compare(v, "5477656C7665", "8859_1",sb),sb);    //@F1A
            else if(getRelease() < JDTestDriver.RELEASE_V7R1M0)           //@F1A	   // @K2
                assertCondition (compare (v, "Twelve", "8859_1",sb),sb);
	    else									   // @K2
		assertCondition( compareBeginsWithBytes( v, "Twelve".getBytes("8859_1"),sb),sb); // @K2
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAsciiStream() - Get from a CLOB, when the CLOB data is returned
in the result set.
**/
    public void Var030 ()
    {
      sb.setLength(0); 

        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                InputStream v = rs.getAsciiStream ("C_CLOB");
		if(getDriver() == JDTestDriver.DRIVER_NATIVE &&		// @K2
		   getRelease() >= JDTestDriver.RELEASE_V7R1M0)		// @K2
		    assertCondition( compareBeginsWithBytes( v, JDRSTest.CLOB_FULL.getBytes("8859_1"),sb),sb); // @K2
		else							// @K2
		    assertCondition (compare (v, JDRSTest.CLOB_FULL, "8859_1",sb),""+sb);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getAsciiStream() - Get from a CLOB, when the CLOB locator is returned
in the result set.
<BR>
SQL400 - Changed to recognize the fact that you can have lob support 
outside of a JDBC 2.0 driver.
**/
    public void Var031 ()
    {
      sb.setLength(0); 
      if (getDriver() == JDTestDriver.DRIVER_JCC  ) {
        notApplicable("JCC driver hangs"); 
        return; 
      }
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statementLocators_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                InputStream v = rs.getAsciiStream ("C_CLOB");
		boolean check;						// @K2
		if(getDriver() == JDTestDriver.DRIVER_NATIVE &&		// @K2
		   getRelease() >= JDTestDriver.RELEASE_V7R1M0)		// @K2
		    check = compareBeginsWithBytes( v, JDRSTest.CLOB_FULL.getBytes("8859_1"),sb);	// @K2
		else							// @K2
		    check = compare (v, JDRSTest.CLOB_FULL, "8859_1",sb);	// @K2
                rs.close();
                assertCondition(check,sb);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getAsciiStream() - Get from a DBCLOB, when the DBCLOB data is
returned in the result set.
**/
    public void Var032 ()
    {
      sb.setLength(0); 
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                InputStream v = rs.getAsciiStream ("C_DBCLOB");
		if(getDriver() == JDTestDriver.DRIVER_NATIVE &&		// @K2
		   getRelease() >= JDTestDriver.RELEASE_V7R1M0)		// @K2
		    assertCondition( compareBeginsWithBytes( v, JDRSTest.DBCLOB_FULL.getBytes("8859_1"),sb),sb); // @K2
		else							// @K2
		    assertCondition (compare (v, JDRSTest.DBCLOB_FULL, "8859_1",sb),sb);	// @K2
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getAsciiStream() - Get from a DBCLOB, when the DBCLOB locator is
returned in the result set.
<BR>
SQL400 - Changed to recognize the fact that you can have lob support 
outside of a JDBC 2.0 driver.
**/
    public void Var033 ()
    {
      sb.setLength(0); 
      if (getDriver() == JDTestDriver.DRIVER_JCC  ) {
        notApplicable("JCC driver hangs"); 
        return; 
      }

        if (checkLobSupport ()) {
            try {
                ResultSet rs = statementLocators_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                InputStream v = rs.getAsciiStream ("C_DBCLOB");
		boolean check;						// @K2
		if(getDriver() == JDTestDriver.DRIVER_NATIVE &&		// @K2
		   getRelease() >= JDTestDriver.RELEASE_V7R1M0)		// @K2
		    check = compareBeginsWithBytes( v, JDRSTest.DBCLOB_FULL.getBytes("8859_1"),sb); // @K2
		else							// @K2
		    check = compare (v, JDRSTest.DBCLOB_FULL, "8859_1",sb);// @K2
                rs.close();
                assertCondition(check,sb);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getAsciiStream() - Get from a BLOB, when the BLOB data is returned
in the result set.
**/
    public void Var034 ()
    {
      sb.setLength(0); 
      sb.append("getAsciiStream from BLOB"); 
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                InputStream v = rs.getAsciiStream ("C_BLOB");
                if(isToolboxDriver())                          //@K1A
                    assertCondition(compare(v, JDRSTest.BLOB_FULL, true, sb),"PATH 1 "+sb);               //@K1A
                else  if(getRelease() < JDTestDriver.RELEASE_V7R1M0 )                    //@K1A	// @K2
                    assertCondition (compare (v, JDRSTest.BLOB_FULL,sb),"PATH 2 "+sb);	
		else										// @K2
		    assertCondition( compareBeginsWithBytes( v, JDRSTest.BLOB_FULL,sb),"PATH 3 "+sb);		// @K2

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getAsciiStream() - Get from a BLOB, when the BLOB locator is returned
in the result set.
<BR>
SQL400 - Changed to recognize the fact that you can have lob support 
outside of a JDBC 2.0 driver.
**/
    public void Var035 ()
    {
      sb.setLength(0); 
      sb.append("getAsciiStream from blob"); 
        if (checkLobSupport ()) {
            try {
               // This is a shameless hack to get around another shameless hack.
               // At the time that we get to this testcase, we have accumulated
               // locks that will interfere with our running the test.  The locks
               // exist because we turned off autocommit in the setup code.  Just
               // commit here so that we have things cleaned up.  Sometime, lets 
               // get both the change to not use autocommit and these commits out
               // of the code for good.
               connection_.commit();

               // Connection2_ will only be set for jdbc20 
               if (connection2_ != null) connection2_.commit();

                ResultSet rs = statementLocators_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                InputStream v = rs.getAsciiStream ("C_BLOB");
                boolean check = false;                                                                  //@K1A
		if(isToolboxDriver()) {
		    sb.append("Path 1:"); 
                    check = compare(v, JDRSTest.BLOB_FULL, true,sb);                                       //@K1A
		} else if(getRelease() < JDTestDriver.RELEASE_V7R1M0) {		// @K2
		    sb.append("Path 2:");
                    check = compare (v, JDRSTest.BLOB_FULL, sb);
		} else	{
		    sb.append("Path 3:");
		    check = compareBeginsWithBytes( v, JDRSTest.BLOB_FULL, sb);	// @K2
		}
                rs.close();
                assertCondition(check,sb );
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getAsciiStream() - Get from a DATE.
**/
    public void Var036 ()
    {
      sb.setLength(0); 
	sb.append(" -- Updated 12/14/2011 Since a date can be converted to string, it can be converted to ASCII stream"); 
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            InputStream v = rs.getAsciiStream ("C_DATE");
            if(isToolboxDriver())                                  //@K1A
                assertCondition(compare(v, "04/08/98", "8859_1",sb),sb);                      //@K1A
            else                                                                        //@K1A
                assertCondition(compare(v, "1998-04-08", "8859_1", sb),sb);                      //@K1A
        }
        catch (Exception e) {
	    failed(e, "Unexpected Exception");                                      //@K1A
        }
    }



/**
getAsciiStream() - Get from a TIME.
**/
    public void Var037 ()
    {
      sb.setLength(0); 
	sb.append(" -- Updated 12/14/2011 Since a time can be converted to string, it can be converted to ASCII stream"); 
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            InputStream v = rs.getAsciiStream ("C_TIME");
	    String expected = "08:14:03";
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		expected = "08.14.03";
	    }
	    assertCondition (compare (v, expected, "8859_1", sb),sb);        

        }
        catch (Exception e) {
	    failed (e, "Unexpected exception "+sb.toString()); 
        }
    }



/**
getAsciiStream() - Get from a TIMESTAMP.
**/
    public void Var038 ()
    {
      sb.setLength(0); 
	sb.append(" -- Updated 12/14/2011 Since a time can be converted to string, it can be converted to ASCII stream"); 
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            InputStream v = rs.getAsciiStream ("C_TIMESTAMP");
	    String expected = "1998-11-18 03:13:42.987654";
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		expected = "1998-11-18-03.13.42.987654";
	    } 
	    assertCondition (compare (v, expected, "8859_1", sb),sb);        

        }
        catch (Exception e) {
	    failed (e, "Unexpected exception "+sb.toString()); 
        }
    }



/**
getAsciiStream() - Get from a DATALINK.

SQL400 - From the native driver's perspective, a datalink column is treated 
the same was that it is for standard SQL.  When you make an unqualified select
of a datalink, the SQL statement is implicitly cast to look at the full URL
value for the datalink.  This is, in essence, a String and can be retrieved with
getAsciiStream as other Strings can.
**/
    public void Var039 ()
    {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("Datalink test case not applicable for JCC"); 
          return; 
        }
        if (checkDatalinkSupport ()) {
            try {
                Statement s = connection_.createStatement ();
                ResultSet rs = s.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GETDL);
                JDRSTest.position0 (rs, "LOB_FULL");
                InputStream v = rs.getAsciiStream ("C_DATALINK");

                if (getDriver () == JDTestDriver.DRIVER_NATIVE &&
                    getRelease() >=  JDTestDriver.RELEASE_V7R1M0 &&
                    isJDK14  )      //@K1C
                   failed ("Didn't throw SQLException");                                                              
                else                                                                                                 
                   // Note the case... AS/400 DB does its own thing here...                                             
                   assertCondition (compare (v, JDRSTest.LOB_FULL_DATALINK_UPPER_DOMAIN, "8859_1",sb),sb);        

            }
            catch (Exception e) {
               if (getDriver () == JDTestDriver.DRIVER_NATIVE && getRelease() >= JDTestDriver.RELEASE_V7R1M0 && isJDK14 )        //@K1C
                  assertExceptionIsInstanceOf (e, "java.sql.SQLException");                                           
               else                                                                                                   
                  failed (e, "Unexpected Exception");
            }
        }
    }



/**
getAsciiStream() - Get from an empty DATALINK.
**/
    public void Var040 ()
    {
       if (checkDatalinkSupport ()) {
           try {
               Statement s = connection_.createStatement ();
               ResultSet rs = s.executeQuery ("SELECT * FROM "
                   + JDRSTest.RSTEST_GETDL);
               JDRSTest.position0 (rs, "LOB_EMPTY");
               InputStream v = rs.getAsciiStream ("C_DATALINK");

                if (getDriver () == JDTestDriver.DRIVER_NATIVE && getRelease() >=  JDTestDriver.RELEASE_V7R1M0 && isJDK14 )    //@K1C
                  failed ("Didn't throw SQLException");                                                             
                else                                                                                                 
                  assertCondition (compare (v, "", "8859_1",sb),sb);        

           }
           catch (Exception e) {
	     if (getDriver () == JDTestDriver.DRIVER_NATIVE && getRelease() >=  JDTestDriver.RELEASE_V7R1M0 && isJDK14  )      //@K1C
                 assertExceptionIsInstanceOf (e, "java.sql.SQLException");                                         
              else                                                                                                 
                 failed (e, "Unexpected Exception");
           }
       }
    }



/**
getAsciiStream() - Get from a DISTINCT.
**/
    public void Var041 ()
    {
      sb.setLength(0); 

        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("NA LUW getAsciiStream");
          return; 
        }
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_EMPTY");
                InputStream v = rs.getAsciiStream ("C_DISTINCT");
		if( getDriver() == JDTestDriver.DRIVER_NATIVE &&				  // @K2
		    getRelease() >= JDTestDriver.RELEASE_V7R1M0 )				  // @K2
		    assertCondition( compareBeginsWithBytes( v, "         ".getBytes("8859_1"),sb),sb); // @K2
                else if (getDriver() == JDTestDriver.DRIVER_JCC)	{
                  boolean passed = compareBeginsWithBytes( v, "         ".getBytes("8859_1"),sb); 
                  assertCondition(passed,sb); 
                } else 
               
		    assertCondition (compare (v, "         ", "8859_1",sb),sb);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getAsciiStream() - Get from a BIGINT.
**/
    public void Var042 ()
    {
        if (checkBigintSupport()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            InputStream v = rs.getAsciiStream ("C_BIGINT");
	    assertCondition(compare(v, "12374321", "8859_1",sb),sb);          //@K1A
        }
        catch (Exception e) {
                failed(e, "Unexpected Exception updated 01/04/2012 for native driver");                          //@K1A
        }
        }
    }

    /**
     getInputStream() - Get from DFP16:
     **/
    public void Var043 ()
    {
      sb.setLength(0); 
	sb.append(" -- Updated 12/14/2011 Since a DFP16 can be converted to string, it can be converted to ASCII stream"); 

      if (checkDecFloatSupport()) {
        try {
          Statement s = connection_.createStatement ();
          ResultSet rs = s.executeQuery ("SELECT * FROM "
              + JDRSTest.RSTEST_DFP16);
          rs.next(); 
          InputStream v = rs.getAsciiStream (1);

	  String expected="1.1"; 
	    assertCondition (compare (v, expected, "8859_1", sb),sb);        

        }
        catch (Exception e) {
	    failed (e, "Unexpected exception "+sb); 

        }
      }
    }
    
    
    
    /**
     getInputStream() - Get from DFP34:
     **/
    public void Var044 ()
    {
      sb.setLength(0); 
	sb.append(" -- Updated 12/14/2011 Since a DFP34 can be converted to string, it can be converted to ASCII stream"); 

      if (checkDecFloatSupport()) {
        try {
          Statement s = connection_.createStatement ();
          ResultSet rs = s.executeQuery ("SELECT * FROM "
              + JDRSTest.RSTEST_DFP34);
          rs.next(); 
          InputStream v = rs.getAsciiStream (1);
	  String expected="1.1"; 
	  assertCondition (compare (v, expected, "8859_1", sb),sb);        

        }
        catch (Exception e) {
	    failed (e, "Unexpected exception "+sb); 

        }
      }
    }
    
    /**
    getAsciiStream() - Get from an SQLXML. 
    **/
        public void Var045 ()
        {
            if (checkXmlSupport ()) {
                try {
                sb.setLength(0);
		   String sql = "SELECT * FROM " + JDRSTest.RSTEST_GETXML +" A ORDER BY RRN(A)";
		   sb.append("SQL="+sql+"\n"); 
                    ResultSet rs = statement0_.executeQuery (sql);
                    boolean passed = true;
                    int i = 0; 
                    while (rs.next()) { 
                        String expected = JDRSTest.VALUES_XML_EXPECTED[i]; 
                        InputStream v  = rs.getAsciiStream ("C_XML");
                        if (expected == null ) {
                          if (v != null) { 
                            sb.append("For row "+i+" Expected null but got '"+v+"'\n"); 
                          }
                        } else {
                          
                          if (! compareBeginsWithBytes( v, expected.getBytes("8859_1"),sb)) {
                            passed = false; 
                           
                          }

                        }
                        i++; 
                    }
                        rs.close(); 
                    assertCondition (passed,sb); 
                    
                } catch (Exception e) {
                    failed (e, "Unexpected Exception - V7R1 needs XML host support to run");
                }

                
                
            }
        }


  
        /**
         * getAsciiStream() - Get from a TIMESTAMP(12).
         **/
        public void Var046() {
            if (checkTimestamp12Support()) {
		if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		    testGet(statement0_,
			    "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(12)) from SYSIBM.SYSDUMMY1",
			    "getAsciiStream", 
			    "1998-11-18-03.13.42.987654000000",
			    " -- added 11/19/2012");
		} else { 
		    testGet(statement0_,
			    "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(12)) from SYSIBM.SYSDUMMY1",
			    "getAsciiStream", 
			    "1998-11-18 03:13:42.987654000000",
			    " -- added 11/19/2012");
		}

            }
        }


        /**
         * getAsciiStream() - Get from a TIMESTAMP(0).
         **/
        public void Var047() {
            if (checkTimestamp12Support()) {
		if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		    testGet(statement0_,
			    "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(0)) from SYSIBM.SYSDUMMY1",
			    "getAsciiStream",
			    "1998-11-18-03.13.42",
			    " -- added 11/19/2012"); 

		} else { 

		    testGet(statement0_,
			    "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(0)) from SYSIBM.SYSDUMMY1",
			    "getAsciiStream",
			    "1998-11-18 03:13:42",
			    " -- added 11/19/2012"); 
		}
            
            }
        }


/**
getAsciiStream() - Get from a BOOLEAN true .
**/
    public void Var048 ()
    {
	if (checkBooleanSupport()) { 
	    sb.setLength(0); 

	    try {
		ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							 + JDRSTest.RSTEST_BOOLEAN);
		JDRSTest.position0 (rs, "BOOLEAN_TRUE");
		InputStream v = rs.getAsciiStream ("C_BOOLEAN");
		sb.setLength(0); 
		assertCondition(compare(v, JDRSTest.BOOLEAN_TRUE_STRING, "8859_1",sb),sb);    //@F1A
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}


    }


/**
getAsciiStream() - Get from a BOOLEAN false .
**/
    public void Var049 ()
    {
	if (checkBooleanSupport()) { 
	    sb.setLength(0); 

	    try {
		ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							 + JDRSTest.RSTEST_BOOLEAN);
		JDRSTest.position0 (rs, "BOOLEAN_FALSE");
		InputStream v = rs.getAsciiStream ("C_BOOLEAN");
		sb.setLength(0); 
		assertCondition(compare(v, JDRSTest.BOOLEAN_FALSE_STRING, "8859_1",sb),sb);    //@F1A
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getAsciiStream() - Get from a BOOLEAN null .
**/
    public void Var050 ()
    {
	if (checkBooleanSupport()) { 
	    sb.setLength(0); 

	    try {
		ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							 + JDRSTest.RSTEST_BOOLEAN);
		JDRSTest.position0 (rs, "BOOLEAN_NULL");
		InputStream v = rs.getAsciiStream ("C_BOOLEAN");
		if (v != null) { 
		  sb.append("Got "+v+" sb null"); 
		}
		
		assertCondition(v == null,sb);    //@F1A
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}


    }



}



