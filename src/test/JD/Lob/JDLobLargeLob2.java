///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDLobLargeLob2.java
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
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;
import test.PasswordVault;
import test.JDLobTest.JDTestClob;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;


/**
Testcase JDLobLargeLob.  This tests the following method
of the JDBC Clob class:

<ul>
<li>getAsciiStream()
<li>getCharacterStream()
<li>getSubString()
<li>length()
<li>position()
<li>setAsciiStream()
<li>setCharacterStream()
<li>setString()
<li>truncate()
</ul>
**/

public class JDLobLargeLob2
extends JDTestcase
{

    // Private data.
    private Connection          connection_;
    private Statement           statement_;
    private ResultSet           rs_;

    private ResultSet           rs2_;

    public static String TABLE_  = JDLobTest.COLLECTION + ".JDLLL2CLOB";

    public String MEDIUM_;
    public String LARGE_;
    public String VLARGE_;

    public long mediumLen, largeLen, vLargeLen;

    public final static int mega = 1024*1024;
    StringBuffer sb = new StringBuffer();
    private String TABLE_QUERY; 

/**
Constructor.
**/
    public JDLobLargeLob2    (AS400 systemObject,
			      Hashtable namesAndVars,
			      int runMode,
			      FileOutputStream fileOutputStream,
			      String password)
    {
	super (systemObject, "JDLobLargeLob2",
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

	MEDIUM_ = "";

	String s = "ABCDE";
	do{
	    MEDIUM_ += s;
	}while(MEDIUM_.length() < 45);

	MEDIUM_ += " IBM ";

	String MEGA_ = "a";
	for(int i=1; i<mega; i*=2)
	    MEGA_ += MEGA_;

	// LARGE_.length() SB 4*mega
	VLARGE_ = "";
	for(int i=0; i<4; i++)
	    VLARGE_ += MEGA_;

	LARGE_ = VLARGE_;

        TABLE_  = JDLobTest.COLLECTION + ".JDLLL2CLOB";
        if (isJdbc20 ()) {
            if (areLobsSupported ()) {
//		long threshold = (long)Math.pow(2,31) - 1;
		long threshold = 1;
		// System.out.println("threshold = "+threshold);
                String url = baseURL_
                             + ";user=" + systemObject_.getUserId()
                             + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                             + ";lob threshold=" + threshold;
                connection_ = testDriver_.getConnection(url,systemObject_.getUserId(),encryptedPassword_);
                connection_.setAutoCommit(false);
                connection_.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
                statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                          ResultSet.CONCUR_UPDATABLE);


                initTable(statement_,  TABLE_,  "(ID INTEGER, C_CLOB CLOB(" + (2*1024*mega-1) + "))");

		// System.out.println("Table Name is "+TABLE_);

                PreparedStatement ps = connection_.prepareStatement ("INSERT INTO "
                                                                     + TABLE_ + " (ID, C_CLOB) VALUES (?,?)");
                ps.setInt (1, 1);
                ps.setString (2, "");
                ps.executeUpdate ();

                ps.setInt (1, 2);
                ps.setString (2, MEDIUM_);
                ps.executeUpdate ();

                ps.setInt (1, 3);
                ps.setString (2, LARGE_);
                ps.executeUpdate ();

                ps.setInt (1, 4);
            		ps.setString (2, VLARGE_);
                ps.executeUpdate ();

                TABLE_QUERY = "SELECT * FROM " + TABLE_+" ORDER BY ID "; 
                rs_ = statement_.executeQuery (TABLE_QUERY);

                ps.close ();

		mediumLen = MEDIUM_.length();
		largeLen = LARGE_.length();
		vLargeLen = VLARGE_.length();
            }
        }
    }

/**
Compares the string with incoming data from the inputstream

@exception none.
**/
    protected static boolean compare (InputStream i, String s, boolean extraBytesOK) {		// @KK
        try {

	    int strLen = s.length();
	    if(extraBytesOK) {			// @KK
		if(i.available() < strLen)	// @KK
		    return false;		// @KK
	    }					// @KK
	    else				// @KK
		if (i.available() != strLen)
		    return false;

/** If plenty of time is available, comment the if block and execute the else-block by default.
**/
	    if(strLen > 4096){
		// Then reading the entire thing is very time consuming.
		// Reading first, middle, and last 1k bytes
		byte[] b = new byte[1024];
		i.read(b, 0, 1024);

		if( !s.substring(0, 1024).equals(new String(b)) )
		    return false;
		
		int middle = strLen - 3*1024;
		if(middle%2 == 1){
		    i.skip( (middle+1)/2 );
		    s = s.substring(1024 + (middle+1)/2);
		    middle--;
		}
		else{
		    i.skip( middle/2);
		    s = s.substring(1024 + middle/2);
		}
		middle/=2;
		i.read(b, 0, 1024);

		if(!s.substring(0, 1024).equals(new String(b)))
		    return false;

		i.skip(middle);
		s = s.substring(1024+middle);
		i.read(b, 0, 1024);

		if( s.length() != 1024)	// calculation mistake !!
		    return false;

		return s.equals(new String(b));

	    }
	    else{
		byte[] b = new byte[strLen];
		i.read (b, 0, strLen);
		return s.equals(new String(b));
	    }

	}
        catch (Exception e) {
            return false;
        }
    }

/**
Compares the string with incoming data from the inputstream

@exception none.
**/
    protected static boolean compare (Reader i, String s) {
        try {

	    int strLen = s.length();

/** If plenty of time is available, comment the if block and execute the else-block by default.
**/
	    if(strLen > 4096){
		// Then reading the entire thing is very time consuming.
		// Reading first, middle, and last 1k chars
		char[] b = new char[1024];
		i.read(b, 0, 1024);

		if( !s.substring(0, 1024).equals(new String(b)) )
		    return false;
		
		int middle = strLen - 3*1024;
		if(middle%2 == 1){
		    i.skip( (middle+1)/2 );
		    s = s.substring(1024 + (middle+1)/2);
		    middle--;
		}
		else{
		    i.skip( middle/2);
		    s = s.substring(1024 + middle/2);
		}
		middle/=2;
		i.read(b, 0, 1024);

		if(!s.substring(0, 1024).equals(new String(b)))
		    return false;

		i.skip(middle);
		s = s.substring(1024+middle);
		i.read(b, 0, 1024);

		if( s.length() != 1024)	// calculation mistake !!
		    return false;

		return s.equals(new String(b));

	    }
	    else{
		char[] b = new char[strLen];
		i.read (b, 0, strLen);
		return s.equals(new String(b));
	    }

	}
        catch (Exception e) {
            return false;
        }
    }

/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
  protected void cleanup() throws Exception {
    cleanupTable(statement_, TABLE_);

    statement_.close();
    try {
      connection_.commit();
    } catch (Exception e) {
      e.printStackTrace();
    }
    connection_.close();
  }

  /**
getAsciiStream() - When the lob is not empty. This testcase also checks for - When the lob is full.
IF MODIFICATIONS MADE MAKES THE FOLLOWING VAR. TEST ONLY ONE OF THE ABOVE 2 CONDITIONS THEN
A DIFFERENT TESTCASE SHOULD BE WRITTEN TO COVER THE LEFT OVER CONDITION
**/
    public void Var001()
    {
	if( getRelease() < JDTestDriver.RELEASE_V7R1M0 &&
	    getDriver() == JDTestDriver.DRIVER_NATIVE){
	    notApplicable("NOT APPLICABLE FOR <= V5R3");
	    return;
	}
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {

		    boolean success = true;
		    Clob clob;
		    String expected = null;

		    for(int i=	0; i<4; i++){
			switch(i){
			    case 0:
				expected = "";
				break;
			    case 1:
				expected = MEDIUM_;
				break;
			    case 2:
				expected = LARGE_;
				break;
			    case 3:
				expected = VLARGE_;
				break;
			}

			rs_.absolute(i+1);                  //@KBC this is one based not zero based - rs_.absolute(0) positions you before the first row
			clob = rs_.getClob("C_CLOB");

			InputStream v = clob.getAsciiStream();
	    sb.setLength(0); 
			if( getDriver() == JDTestDriver.DRIVER_TOOLBOX)
			    success = success && compare(v, expected, "8859_1",sb);
			else
			    success = success && compare(v, expected,
							 getRelease()>=JDTestDriver.RELEASE_V7R1M0);	// @KK
		    }

		    assertCondition ( success, "Large lob Testcases added 07/17/2003"+sb);
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception -- Large lob Testcases added 07/17/2003");
                }
            }
        }
    }

/**
getCharacterStream() - When the lob is not empty. This testcase also checks for - When the lob is full.
IF MODIFICATIONS MADE MAKES THE FOLLOWING VAR. TEST ONLY ONE OF THE ABOVE 2 CONDITIONS THEN
A DIFFERENT TESTCASE SHOULD BE WRITTEN TO COVER THE LEFT OVER CONDITION
**/
    public void Var002()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    rs_.absolute (3);
                    Clob clob = rs_.getClob ("C_CLOB");
                    Reader v = clob.getCharacterStream ();
                    assertCondition (compare (v, LARGE_), "Large lob Testcases added 07/17/2003");    
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception -- Large lob Testcases added 07/17/2003");
                }
            }
        }
    }

/**
getSubString() - Native driver pads extra characters if mentioned length exceeds length_ but is within the limits of maxLength_

   06/19/2013
   Upon rexamination, the native JDBC driver should not pad, but just return what is here. 
**/
    public void Var003()
    {
	if( getRelease() < JDTestDriver.RELEASE_V7R1M0 &&
	    getDriver() == JDTestDriver.DRIVER_NATIVE){
	    notApplicable("NOT APPLICABLE FOR <= V5R3");
	    return;
	}
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (3);
                Clob clob = rs_.getClob ("C_CLOB");
                String res = clob.getSubString (3, (int)(largeLen + 1));
		// Updated 10/1/2013 -- toolbox should not throw exception. 
                // if(getDriver() == JDTestDriver.DRIVER_TOOLBOX)          //@A1A
                //    failed("Didn't throw Exception");                   //@A1A
		// else                                                    //@A1A
                    assertCondition(res.length() == largeLen -2, "recvd = "+res.length()+" SB "+(largeLen-2));				
	    }
            catch (Exception e) {
                //if(getDriver() == JDTestDriver.DRIVER_TOOLBOX)          //@A1A Toolbox doesn not pad extra characters
                //    assertExceptionIsInstanceOf(e, "java.sql.SQLException");    //@A1A
                //else                                                    //@A1A
                    failed(e, "Large lob Testcases added 07/17/2003");
            }
        }
    }

/**
getSubString() - Should work to retrieve all of a non-empty lob. This testcase also checks for - When the lob is full.
IF MODIFICATIONS MADE MAKES THE FOLLOWING VAR. TEST ONLY ONE OF THE ABOVE 2 CONDITIONS THEN
A DIFFERENT TESTCASE SHOULD BE WRITTEN TO COVER THE LEFT OVER CONDITION
**/
    public void Var004()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    rs_.absolute (3);
                    Clob clob = rs_.getClob ("C_CLOB");
                    String v = clob.getSubString (1, (int)largeLen);
                    assertCondition ( v.equals(LARGE_), "Large lob Testcases added 07/17/2003");
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception -- Large lob Testcases added 07/17/2003");
                }
            }
        }
    }

/**
getSubString() - Should work to retrieve the first part of a non-empty lob.
**/
    public void Var005()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    rs_.absolute (3);
                    Clob clob = rs_.getClob ("C_CLOB");
                    String v = clob.getSubString (1, 3);
                    String expected = LARGE_.substring(0,3);
                    assertCondition ( v.equals(expected), "Large lob Testcases added 07/17/2003");
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception -- Large lob Testcases added 07/17/2003");
                }
            }
        }
    }

/**
getSubString() - Should work to retrieve the middle part of a non-empty lob.
**/
    public void Var006()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    rs_.absolute (3);
                    Clob clob = rs_.getClob ("C_CLOB");
                    String v = clob.getSubString (9, 6);
                    String expected = LARGE_.substring(8, 14);
                    assertCondition ( v.equals(expected), "Large lob Testcases added 07/17/2003");
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception -- Large lob Testcases added 07/17/2003");
                }
            }
        }
    }

/**
getSubString() - Should work to retrieve the last part of a non-empty lob.
**/
    public void Var007()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    rs_.absolute (3);
                    Clob clob = rs_.getClob ("C_CLOB");
                    String v = clob.getSubString ((int)largeLen-3, 4);	// this is 1-based extraction
                    String expected = LARGE_.substring((int)largeLen-4);// this is 0-based extraction
		    String message = "Recvd: \""+v+"\" expected = \""+expected+"\"";
                    assertCondition ( v.equals(expected), "Large lob Testcases added 07/17/2003 "+message );
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception -- Large lob Testcases added 07/17/2003");
                }
            }
        }
    }


/**
length() - When the lob is not empty.
**/
    public void Var008()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    rs_.absolute (2);
                    Clob clob = rs_.getClob ("C_CLOB");
		    boolean condition = (clob.length() == mediumLen);
                    assertCondition (condition, "Large lob Testcases added 07/17/2003");
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception Large lob Testcases added 07/17/2003");
                }
            }
        }
    }

/**
length() - When the lob is full.
**/
    public void Var009()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    rs_.absolute (3);
                    Clob clob = rs_.getClob ("C_CLOB");
                    assertCondition (clob.length () == largeLen, "Large lob Testcases added 07/17/2003");
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception -- Large lob Testcases added 07/17/2003");
                }
            }
        }
    }

/**
position(String,long) - Should throw an exception when the pattern is null.
**/
    public void Var010()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    rs_.absolute (2);
                    Clob clob = rs_.getClob ("C_CLOB");
                    clob.position ( (String)null, 1);
                    failed ("Didn't throw SQLException -- Large lob Testcases added 07/17/2003");
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException", "Large lob Testcases added 07/17/2003");
                }
            }
        }
    }

/**
position(String,long) - Should throw an exception when start is less than 0.
**/
    public void Var011()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    rs_.absolute (2);
                    Clob clob = rs_.getClob ("C_CLOB");
                    String test = "IBM";
                    clob.position (test, (long) -1);
                    failed ("Didn't throw SQLException Large lob Testcases added 07/17/2003");
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException", "Large lob Testcases added 07/17/2003");
                }
            }
        }
    }

/**
position(String,long) - Should throw an exception when start is 0.
**/
    public void Var012()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    rs_.absolute (2);
                    Clob clob = rs_.getClob ("C_CLOB");
                    String test = "IBM";
                    clob.position (test, (long) 0);
                    failed ("Didn't throw SQLException -- Large lob Testcases added 07/17/2003");
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException", "Large lob Testcases added 07/17/2003");
                }
            }
        }
    }



/**
position(String,long) - Should throw an exception when start is greater than the length of the lob.
**/
    public void Var013()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
		boolean result = true;
		for(int i=0; result && i<3; i++){
		    try {
			rs_.absolute (2);
			Clob clob = rs_.getClob ("C_CLOB");
			String test = "IBM";
			clob.position (test, mediumLen+i+1);
			failed ("Didn't throw SQLException -- Large lob Testcases added 07/17/2003");
		    }
		    catch (Exception e) {
			result = (e instanceof java.sql.SQLException);
			continue;
		    }
		    result = false;
		}
		assertCondition(result, "Large lob Testcases added 07/17/2003");
	    }
	}
    }

/**
position(Clob,long) - Should return -1 when the pattern is not found at all.
**/
    public void Var014()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    rs_.absolute (2);
                    Clob clob = rs_.getClob ("C_CLOB");
		    String addr = clob.toString();
		    String message = "clob = "+clob+" "+addr;
                    String test = "Dept. 45H";
                    long v = clob.position (new JDLobTest.JDTestClob (test), (long) 1);
		    message += "\nv = "+v+" SB -1.";
                    assertCondition (v == -1, "Large lob Testcases added 07/17/2003 "+message);
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception -- Large lob Testcases added 07/17/2003");
                }
            }
        }
    }

/**
position(Clob,long) - Should return the position when the pattern is found at the end of the lob, and start is 1.
**/
    public void Var015()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    rs_.absolute (2);
                    Clob clob = rs_.getClob ("C_CLOB");
                    String test = " IBM ";
                    long v = clob.position (new JDLobTest.JDTestClob (test), (long) 1);
		    String message="v = "+v+" SB 46.";
		    assertCondition (v == 46, "Large lob Testcases added 07/17/2003"+message);
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception -- Large lob Testcases added 07/17/2003");
                }
            }
        }
    }

/**
position(Clob,long) - Should return the position when the pattern is found at the end of the lob, and start is right where the pattern occurs.
**/
    public void Var016()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    rs_.absolute (2);
                    Clob clob = rs_.getClob ("C_CLOB");
                    String test = " IBM ";
                    long v = clob.position (new JDLobTest.JDTestClob (test), (long) 46);
		    assertCondition (v == 46, "Large lob Testcases added 07/17/2003");
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception -- Large lob Testcases added 07/17/2003");
                }
            }
        }
    }

/**
setString(long, String) - Should not throw an exception when length of str to write extends past the length of the lob as long as start is not passed the maxLength_ of the lob.
**/
    public void Var017()
    {
        if (checkUpdateableLobsSupport ()) {
            try {
                rs2_=statement_.executeQuery(TABLE_QUERY);
                rs2_.absolute(3);
                Clob clob = rs2_.getClob ("C_CLOB");
                String expected = "IBM";
                int written = clob.setString (3, expected);
                rs2_.updateClob("C_CLOB", clob);           
                rs2_.updateRow();                          
                rs2_.close();
                rs2_=statement_.executeQuery(TABLE_QUERY);
                rs2_.absolute(3);
                Clob clob1 = rs2_.getClob ("C_CLOB");
                String b = clob1.getSubString (3, 3);
                assertCondition( b.equals(expected) && written == 3,
				 "Large lob Testcases added 07/17/2003");
                rs2_.close();
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception -- Large lob Testcases added 07/17/2003");
            }
        }
    }

/**
setString(long, String) - Should work to set all of a non-empty lob.
**/
    public void Var018()
    {
        if (checkUpdateableLobsSupport ()) {
            try {
                rs2_=statement_.executeQuery(TABLE_QUERY);
                rs2_.absolute (2);
                Clob clob = rs2_.getClob ("C_CLOB");
                String expected = " These are hopefully as many as fifty characters .";
                int written = clob.setString (1, expected);
                rs2_.updateClob("C_CLOB", clob);
                rs2_.updateRow();
                rs2_.close ();
                rs2_=statement_.executeQuery(TABLE_QUERY);
                rs2_.absolute(2);                                 //@A1C Changed to absolute(2) instead of absolute(3)
                Clob clob1 = rs2_.getClob ("C_CLOB");
                String b = clob1.getSubString (1, (int)mediumLen);
		String message = "Exp.len = "+expected.length()+" SB "+mediumLen;
		message +="\nb = "+b+" SB "+expected;
		message +="\nwritten = "+written+" SB "+mediumLen;
                assertCondition( expected.length() == mediumLen &&
				 b.equals(expected) && written == mediumLen,
				 "Large lob Testcases added 07/17/2003 "+message);
                rs2_.close();
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception -- Large lob Testcases added 07/17/2003");
            }
        }
    }

/**
setString(long, String) - Should work to set the last part of a non-empty lob.
**/
    public void Var019()
    {
        if (checkUpdateableLobsSupport ()) {
            try {
                rs2_=statement_.executeQuery(TABLE_QUERY);
                rs2_.absolute (2);
                Clob clob = rs2_.getClob ("C_CLOB");
                String expected = "IBM";
                int written = clob.setString (mediumLen -3, expected);
                rs2_.updateClob("C_CLOB", clob);
                rs2_.updateRow();
                rs2_.close ();
                rs2_=statement_.executeQuery(TABLE_QUERY);
                rs2_.absolute(2);                       //@A1C Changed to absolute(2) instead of absolute(3)
                Clob clob1 = rs2_.getClob("C_CLOB");
                String b = clob1.getSubString (mediumLen-3, 3);
		String message = "b = "+b+" SB "+expected;
		message += "\nwritten = "+written+" SB 3";
                assertCondition( b.equals(expected) && written == 3,
				 "Large lob Testcases added 07/17/2003 "+message);
                rs2_.close();
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception -- Large lob Testcases added 07/17/2003");
            }
        }
    }

/**
setString(long, String) - Should work to set the middle part of a non-empty lob.
**/
    public void Var020()
    {
        if (checkUpdateableLobsSupport ()) {
            try {
                rs2_=statement_.executeQuery(TABLE_QUERY);
                rs2_.absolute (3);
                Clob clob = rs2_.getClob ("C_CLOB");
                String expected = "Rochester";
                int written = clob.setString (2, expected);
                rs2_.updateClob("C_CLOB", clob);            //@C2A
                rs2_.updateRow();                                //@C2A
                rs2_.close ();
                rs2_=statement_.executeQuery(TABLE_QUERY);
                rs2_.absolute(3);
                Clob clob1 = rs2_.getClob("C_CLOB");
                String b = clob1.getSubString (2, 9);
                assertCondition( b.equals(expected) && written == 9,
				 "Large lob Testcases added 07/17/2003");  //@C2C
                rs2_.close();
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception -- Large lob Testcases added 07/17/2003");
            }
        }
    }

/**
setString(long, String, int, int) - Should set value beyond length_ as long as they are within limits of maxLength_
**/
    public void Var021()
    {
	if( getRelease() < JDTestDriver.RELEASE_V7R1M0 &&
	    getDriver() == JDTestDriver.DRIVER_NATIVE){
	    notApplicable("NOT APPLICABLE FOR <= V5R3");
	    return;
	}
        if (checkUpdateableLobsSupport ()) {
	    try {
		rs2_=statement_.executeQuery(TABLE_QUERY);
		rs2_.absolute (2);
		Clob clob = rs2_.getClob ("C_CLOB");
		long pos = clob.length() + 2;
		String append = "Rochester.";
		clob.setString ( pos , append, 0, 1); 
		rs2_.updateClob("C_CLOB", clob);
		rs2_.updateRow();
		rs2_.close();
		rs2_=statement_.executeQuery(TABLE_QUERY);
		rs2_.absolute (2);
		Clob clob1 = rs2_.getClob("C_CLOB");
		String result = clob1.getSubString( 1, (int)clob1.length()).substring( (int)pos-2, (int)pos);
		String expected = " R";
                if(getDriver() == JDTestDriver.DRIVER_TOOLBOX)      //@A1A
                    assertCondition(result.trim().equals(expected.trim()));
                else                                                //@A1A
                    assertCondition(result.equals(expected), result+" SB "+expected);
		rs2_.close();
	    }
	    catch (Exception e) {
		    failed (e, "Unexpected Exception -- Large lob Testcases added 07/17/2003");
	    }
	}
    }

/**
setString(long, String, int, int) - Should not throw an exception when length of str to write extends past the length lob as long as start is within limits of maxLength_ of clob
**/
    public void Var022()
    {
	if (checkUpdateableLobsSupport ()) {
            try {
                rs2_=statement_.executeQuery(TABLE_QUERY);
                rs2_.absolute (3);
                String expected = "Summer";
                Clob clob = rs2_.getClob ("C_CLOB");
                int written = clob.setString (1, expected, 0, 6); 
                rs2_.updateClob("C_CLOB", clob);          
                rs2_.updateRow();                         
                rs2_.close();
                rs2_=statement_.executeQuery(TABLE_QUERY);
                rs2_.absolute(3);
                Clob clob1 = rs2_.getClob("C_CLOB");
                String b = clob1.getSubString (1, 6);
                assertCondition( b.equals(expected) && written == 6,
				 "Large lob Testcases added 07/17/2003");   //@C2C
                rs2_.close();
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception -- Large lob Testcases added 07/17/2003");
            }
        }
    }

/**
setString(long, String, int, int) - Should work to set all of a non-empty lob.
**/
    public void Var023()
    {
        if (checkUpdateableLobsSupport ()) {
            try {
                rs2_=statement_.executeQuery(TABLE_QUERY);
                rs2_.absolute (2);
                Clob clob = rs2_.getClob ("C_CLOB");
                String expected = " These are hopefully as many as fifty characters .";
                int written = clob.setString (1, expected, 0, 50);
                rs2_.updateClob("C_CLOB", clob);        
                rs2_.updateRow();                       
                rs2_.close ();
                rs2_=statement_.executeQuery(TABLE_QUERY);
		rs2_.absolute(2);
                Clob clob1 = rs2_.getClob("C_CLOB");	
                String b = clob1.getSubString (1, 50);
                assertCondition( b.equals(expected) && written == 50,
				 "Large lob Testcases added 07/17/2003");  
		// Cleaning changes made by the variation
		clob1.setString(1, MEDIUM_);
		rs2_.updateClob("C_CLOB", clob1);
		rs2_.updateRow();
                rs2_.close();
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception -- Large lob Testcases added 07/17/2003");
            }
        }
    }

/**
setString(long, String, int, int) - Should work to set the last part of a non-empty lob.
**/
    public void Var024()
    {
        if (checkUpdateableLobsSupport ()) {
            try {
                rs2_=statement_.executeQuery(TABLE_QUERY);
                rs2_.absolute (2);
                Clob clob = rs2_.getClob ("C_CLOB");
                String expected = "UMN";
                int written = clob.setString (48, expected, 0, 3);
                rs2_.updateClob("C_CLOB", clob);    
                rs2_.updateRow();
                rs2_.close ();
                rs2_=statement_.executeQuery(TABLE_QUERY);
                rs2_.absolute(2);               //@A1C Changed to absolute(2) instead of absolute(3)
                Clob clob1 = rs2_.getClob("C_CLOB");
                String b = clob1.getSubString (48, 3);
                assertCondition( b.equals(expected) && written == 3,
				 "Large lob Testcases added 07/17/2003");   //@C2C

                // Cleaning changes made by the variation
		clob1.setString(1, MEDIUM_);
		rs2_.updateClob("C_CLOB", clob1);
		rs2_.updateRow();
                rs2_.close();
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception -- Large lob Testcases added 07/17/2003");
            }
        }
    }

/**
truncate() - Should throw an exception if you try to truncate to a length greater than the maxLength_ of the lob.
**/
    public void Var025()
    {
        if (checkUpdateableLobsSupport ())
        {
	    try{
		rs2_=statement_.executeQuery(TABLE_QUERY);
		rs2_.absolute (3);
		Clob clob = rs2_.getClob ("C_CLOB");
		String message = ""+clob;
		clob.truncate (mega*1024*3);	// truncating @ 3GB
		rs2_.updateClob("C_CLOB", clob);
		rs2_.updateRow();
		rs2_.close();
		failed("Didn't throw SQLException.for "+message );
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException",
					     "Large lob Testcases added 07/17/2003");
	    }
	}
    }	

/**
truncate() - Should work on a non-empty lob.
**/
    public void Var026()
    {
	if( getRelease() < JDTestDriver.RELEASE_V7R1M0 &&
	    getDriver() == JDTestDriver.DRIVER_NATIVE){
	    notApplicable("NOT APPLICABLE FOR <= V5R3");
	    return;
	}
        if (checkUpdateableLobsSupport ())
        {
            try
            {
                rs2_=statement_.executeQuery(TABLE_QUERY);
                rs2_.absolute(3);
                Clob clob = rs2_.getClob ("C_CLOB");
                clob.truncate (10);
                rs2_.updateClob("C_CLOB", clob);       
                rs2_.updateRow();                      
                rs2_.close ();
                rs2_=statement_.executeQuery(TABLE_QUERY);
                rs2_.absolute(3);
                Clob clob1 = rs2_.getClob("C_CLOB");
		long len = clob1.length();
		String message = ""+len+" SB 10";
                assertCondition (len == 10, "Large lob Testcases added 07/17/2003 "+message);
                rs2_.close();
            }
            catch (Exception e)
            {
                failed(e, "Unexpected Exception -- Large lob Testcases added 07/17/2003");
            }
        }
    }

/**
setString(long, String) - Should not change lob if updateRow() is not called.
**/
    public void Var027()
    {
        if (checkUpdateableLobsSupport ()) {
            try {
                rs2_=statement_.executeQuery(TABLE_QUERY);
                rs2_.absolute (2);
                Clob clob = rs2_.getClob ("C_CLOB");
                rs2_.updateClob("C_CLOB", clob);
                rs2_.close ();
                rs2_=statement_.executeQuery(TABLE_QUERY);
                rs2_.absolute(2);
                Clob clob1 = rs2_.getClob ("C_CLOB");
                String v = clob1.getSubString (1, (int)mediumLen);
                assertCondition ( v.equals(MEDIUM_), "Large lob Testcases added 07/17/2003");
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception -- Large lob Testcases added 07/17/2003");
            }
        }
    }

/**
setString(long, String) - Make sure lob in database is not changed if updateRow() was not called.
**/
    public void Var028()
    {
        if (checkUpdateableLobsSupport ()) {
            try {
                rs2_=statement_.executeQuery(TABLE_QUERY);
                rs2_.absolute (2);
                Clob clob = rs2_.getClob ("C_CLOB");
                String expected = "IBM, Rochester";
                int written = clob.setString (2, expected);
                rs2_.updateClob("C_CLOB", clob);
                rs2_.close ();
                rs2_=statement_.executeQuery(TABLE_QUERY);
                rs2_.absolute(2);
                Clob clob1 = rs2_.getClob ("C_CLOB");
                String v = clob1.getSubString (1, (int)mediumLen);
                assertCondition ( v.equals(MEDIUM_), "Large lob Testcases added 07/17/2003 written="+written);
                rs2_.close();
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception -- Large lob Testcases added 07/17/2003");
            }
        }
    }

/**
setString(long, String, int, int) - Should work to set str to a non-empty lob with multiple updates to the same position.
**/
    public void Var029()
    {
	if( getRelease() < JDTestDriver.RELEASE_V7R1M0 &&
	    getDriver() == JDTestDriver.DRIVER_NATIVE){
	    notApplicable("NOT APPLICABLE FOR <= V5R3");
	    return;
	}
        if (checkUpdateableLobsSupport ()) {
            try {
                rs2_=statement_.executeQuery(TABLE_QUERY);
                rs2_.absolute (3);
                Clob clob = rs2_.getClob ("C_CLOB");
                String expected = "HELLO";
                int written = clob.setString (1, expected, 0, 5);  
                String expected2 = "HI";
                int written2 = clob.setString (1, expected2, 0, 2); 
                rs2_.updateClob("C_CLOB", clob);
                rs2_.updateRow();
                rs2_.close ();
                rs2_=statement_.executeQuery(TABLE_QUERY);
                rs2_.absolute(3);
                Clob clob1 = rs2_.getClob("C_CLOB");
                String b = clob1.getSubString (1, 5);
                assertCondition( b.equals("HILLO") && written == 5 && written2 == 2, "Large lob Testcases added 07/17/2003");
                rs2_.close();
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception -- Large lob Testcases added 07/17/2003");
            }
        }
    }

/**
setString(long, String, int, int) - Should work to set str to a non-empty lob with multiple updates to the different positions.
**/
    public void Var030()
    {
	if (checkUpdateableLobsSupport ()) {
	    try{
		rs2_=statement_.executeQuery(TABLE_QUERY);
                rs2_.absolute (3);
                Clob clob = rs2_.getClob ("C_CLOB");
                int written = clob.setString (1, "YOU ARE GOOD", 0, 12); 
                int written2 = clob.setString (9, "GREAT", 0, 5); //@A1
                rs2_.updateClob("C_CLOB", clob);
                rs2_.updateRow();
                rs2_.close ();
                rs2_=statement_.executeQuery(TABLE_QUERY);
                rs2_.absolute(3);
                Clob clob1 = rs2_.getClob("C_CLOB");
                String b = clob1.getSubString (1, 13);
                assertCondition( b.equals("YOU ARE GREAT") && written == 12 && written2 == 5,
				 "Large lob Testcases added 07/17/2003");
                rs2_.close();
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception -- Large lob Testcases added 07/17/2003");
            }
        }
    }

/**
setString(long, String, int, int) - Should work to set str to a non-empty lob with multiple updates to the different positions with updateClob calls between.
**/
    public void Var031()
    {
        if (checkUpdateableLobsSupport ()) {
            try {
                rs2_=statement_.executeQuery(TABLE_QUERY);
                rs2_.absolute (3);
                Clob clob = rs2_.getClob ("C_CLOB");

                String str = "WOW";
                int written = clob.setString (1, str, 0, 3); //@A1
		rs2_.updateClob("C_CLOB", clob);

		String str2 = "FUN";
                int written2 = clob.setString (3, str2, 0, 3); //@A1
                rs2_.updateClob("C_CLOB", clob);

                rs2_.updateRow();
                rs2_.close ();

                rs2_=statement_.executeQuery(TABLE_QUERY);
                rs2_.absolute(3);
                Clob clob1 = rs2_.getClob("C_CLOB");
                String b = clob1.getSubString (1, 5);
                assertCondition( b.equals("WOFUN") && written == 3 && written2 == 3, "Large lob Testcases added 07/17/2003");
                rs2_.close();
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception -- Large lob Testcases added 07/17/2003");
            }
        }
    }

/**
Playing around with VLARGE_ field by checking getSubString, length methods
**/
    public void Var032()
    {
	if( getRelease() < JDTestDriver.RELEASE_V7R1M0 &&
	    getDriver() == JDTestDriver.DRIVER_NATIVE){
	    notApplicable("NOT APPLICABLE FOR <= V5R3");
	    return;
	}
        if (checkJdbc20 ()) {
	    if (checkLobSupport ()) {
		try {
		    rs2_=statement_.executeQuery(TABLE_QUERY);
		    rs2_.absolute (4);
                    Clob clob = rs2_.getClob ("C_CLOB");
		    long startIndex = 1;

		    int length = 4 * mega;

		    boolean success = true;
		    while( startIndex+length < vLargeLen){
			String actual = clob.getSubString( startIndex, length);
			String expected = VLARGE_.substring( 0, length);
			VLARGE_ = VLARGE_.substring( length);
			if( !actual.equals(expected)){
			    System.out.println(""+startIndex);
			    System.out.println("   Actual: '"+actual+"'");
			    System.out.println(" Expected: '"+expected+"'");
			    success = false;
			}
			startIndex += length;
			if( startIndex+length > vLargeLen)
			    length = (int)(vLargeLen - startIndex);
		    }

		    assertCondition(success);
		    rs2_.close();

                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception -- Large lob Testcases added 07/17/2003");
                }
            }
        }

    }

/**
Check for memory leak for toolbox driver (Also run for native).
See Issue 44488 for more information
**/
    public void Var033()
    {

	if (getDriver() == JDTestDriver.DRIVER_NATIVE &&
	    getRelease() <= JDTestDriver.RELEASE_V7R1M0) {
	    System.out.println("Memory leak with locators.  Fix for native in V7R2");
	    assertCondition(true);
	    return; 
	}
        if (proxy_ != null && (!proxy_.equals(""))) {
            notApplicable("Use of com.ibm.as400.access.AS400JDBCConnection does not work for proxy driver");
            return; 
        }

	String sql = "";
	sb.setLength(0); 
        if (checkJdbc20 ()) {
	    if (checkLobSupport ()) {
	        System.out.println("Running testcase 33"); 
		String lobTable =  JDLobTest.COLLECTION  + ". JDLBLRG233";
		String statsTable =  JDLobTest.COLLECTION  + ". JDJOBMEM"; 
		try { 

		    Statement stmt = connection_.createStatement(); 
		// Create the table with 2 megabytes lobs
		    try {
			sql = "DROP TABLE "+lobTable;
			stmt.executeUpdate(sql); 
		    } catch (Exception e) {
		    } 

		    try {
			sql = "DROP TABLE "+statsTable;
			stmt.executeUpdate(sql); 
		    } catch (Exception e) {
		    } 

		    sql = "CREATE TABLE "+lobTable+" (C1 CLOB(10M))"; 
		    stmt.executeUpdate(sql); 


		    sql = "CREATE TABLE "+statsTable+" (MEGS INT)"; 
		    stmt.executeUpdate(sql); 

		    sql = "INSERT INTO "+lobTable+" VALUES(?)"; 
		    PreparedStatement ps = connection_.prepareStatement(sql); 

		    ps.setString(1, VLARGE_); 
		    System.out.println("Inserting"); 
		    for (int i = 0; i < 10; i++) {
			ps.executeUpdate(); 
		    }
		    ps.close();
		    stmt.close();
		    connection_.commit();
                    System.out.println("Done Inserting"); 


		// Create the testConnection

		    String url = baseURL_
		      + ";auto commit=true;transaction isolation=none"; 
		    sql = "Getting connection with "+url;
		    Connection testConnection =  testDriver_.getConnection(url,systemObject_.getUserId(), encryptedPassword_);


		    testConnection.setAutoCommit(false);

		    String jobName="";
          String classname = testConnection.getClass().getName();

          if (testConnection instanceof com.ibm.as400.access.AS400JDBCConnection
              || classname.equals("com.ibm.as400.access.JDConnectionProxy")) {
            jobName = JDReflectionUtil.callMethod_S(testConnection,
                "getServerJobIdentifier");
            if (jobName.length() >= 26) {
              jobName = jobName.substring(20).trim() + "/"
                  + jobName.substring(10, 20).trim() + "/"
                  + jobName.substring(0, 10).trim();
            }

          } else if (classname.indexOf("com.ibm.db2.jdbc.app") >= 0 ) {
            jobName = JDReflectionUtil.callMethod_S(testConnection,  "getServerJobName"); 
          } else {
            throw new Exception(
                "Unable to handle connection class " + classname);

          }

		    stmt = testConnection.createStatement();
		    int[] memAfter = new int[20]; 
		    for (int i = 0; i < memAfter.length; i++) {
			sql = "Select * from "+lobTable; 
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
			    rs.getString(1); 
			}
			rs.close();

			testConnection.commit();

			/* NOTE:  In V7R2, the output also has */
                        /* Peak temporary storage used */
                        /* Using head -1 to get rid of it */ 
			sql = "CALL QSYS.QCMDEXC('QSH CMD("+
			      "''system \"DSPJOB JOB("+jobName+") OUTPUT(*PRINT) OPTION(*RUNA) \""+
			  " | grep -i \"Temporary storage used\" | head -1 "+
			  " | sed \"s/^.*: *\\(.*\\)/db2 ''''insert into "+statsTable+" values(\\1)''''/\""+
			  " | sh '')                                                  ',0000000240.00000 )"; 
			sb.append("Getting stats using "+sql+"\n"); 
			stmt.executeUpdate(sql);

		    } 

		    int largest = -1;
		    int count = 0; 
		    sql = "Select * from "+statsTable;
		    ResultSet rs = stmt.executeQuery(sql);
		    while (rs.next()) {
			int value = rs.getInt(1);
			count++; 
			sb.append("Value="+value+"\n");
			if (value > largest) {
			    largest = value; 
			} 
		    } 

		    stmt.close();
		    testConnection.close(); 


		    if (largest == 0 && count < 10) {
			System.out.println("Largest = 0, count("+count+") < 10 sleeping for 10 minutes");
			Thread.sleep(600000); 
		    } 
		    int maxSize = 85;   /* in v7r2 changed to 85 from 70 */ 
		    assertCondition(largest >= 0  && largest < maxSize, " Memory used is "+largest+" should be >= 0 and < "+maxSize+" savedOutput is "+sb.toString());

		}
                catch (Exception e) {
                    failed (e, "Unexpected Exception -- Large lob Testcases added 11/08/2010 sql="+sql);
		} finally {
		    try {

		    Statement stmt = connection_.createStatement(); 
			sql = "DROP TABLE "+lobTable;
			stmt.executeUpdate(sql); 
			sql = "DROP TABLE "+statsTable;
			stmt.executeUpdate(sql);
			stmt.close(); 
		    } catch (Exception e) {
			System.out.println("Error during test cleanup");
			e.printStackTrace(System.out); 
		    } 

                }
            }
        }

    }


}
