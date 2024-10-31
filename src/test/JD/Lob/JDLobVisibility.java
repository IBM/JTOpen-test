///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDLobVisibility.java
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
import java.sql.Blob;
import java.sql.Clob; 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDLobVisibility.

This testcase tests the visibility of lobs in a variation of scenarios
under different configurations.

This is written so that the scenario code can be reused for different configurations.
**/
public class JDLobVisibility
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDLobVisibility";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDLobTest.main(newArgs); 
   }

    // Private data.
    private Connection          connection_;
    private Statement           statement_;
    // 
    // private ResultSet           rs_;
    //
      public static String TABLE_          = JDLobTest.COLLECTION + ".VISIBILITY";
    public static int BLOBSIZE_=100000;
    public static int CLOBSIZE_=100000;
    public static int THRESHOLD_ = 2000000; 
    public static byte[]  BLOB_         = null;  
    public static String CLOB_ = null;

    public final static int USE_BLOB = 1;
    public final static int USE_CLOB = 2;
    public final static int SHOULD_BE_VISIBLE = 1;
    public final static int SHOULD_NOT_BE_VISIBLE = 2; 

    /*
     * Connection string for autocommit, hold cursor, and use lobs 
     */
    public final static String AUTOCOMMIT_ON  = "auto commit=true";
    public final static String AUTOCOMMIT_OFF = "auto commit=false";
    public final static String LOCATORS_ON    = "lob threshold=0";
    public final static String LOCATORS_OFF   = "lob threshold="+THRESHOLD_;
    public final static String HOLD_ON        = "cursor hold=true";
    public final static String HOLD_OFF       = "cursor hold=false";
    public final static String A0H0L0  = AUTOCOMMIT_OFF+";"+HOLD_OFF+ ";"+LOCATORS_OFF;
    public final static String A0H0L1  = AUTOCOMMIT_OFF+";"+HOLD_OFF+ ";"+LOCATORS_ON; 
    public final static String A0H1L0  = AUTOCOMMIT_OFF+";"+HOLD_ON+ ";" +LOCATORS_OFF;
    public final static String A0H1L1  = AUTOCOMMIT_OFF+";"+HOLD_ON+ ";" +LOCATORS_ON; 
    public final static String A1H0L0  = AUTOCOMMIT_ON+ ";"+HOLD_OFF+ ";"+LOCATORS_OFF;
    public final static String A1H0L1  = AUTOCOMMIT_ON+ ";"+HOLD_OFF+ ";"+LOCATORS_ON; 
    public final static String A1H1L0  = AUTOCOMMIT_ON+ ";"+HOLD_ON+ ";" +LOCATORS_OFF;
    public final static String A1H1L1  = AUTOCOMMIT_ON+ ";"+HOLD_ON+ ";" +LOCATORS_ON; 

    byte[] stuff; 
    
    /**
    Static initializer.
    **/
    static 
    {

	BLOB_ = new byte[BLOBSIZE_];
        for (int i = 0; i < BLOB_.length; ++i)
            BLOB_[i] = (byte) (-2 * i);

        StringBuffer buffer = new StringBuffer (CLOBSIZE_);
	for (int i = 1; i <= CLOBSIZE_; ++i) { 
            buffer.append ((char)('A'+(i%26)));
	}
	
        CLOB_ = buffer.toString ();

    }



    /**
    Constructor.
    **/
    public JDLobVisibility (AS400 systemObject,
                      Hashtable namesAndVars,
                      int runMode,
                      FileOutputStream fileOutputStream,
                      String password)
    {
        super (systemObject, "JDLobVisibility",
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
        TABLE_          = JDLobTest.COLLECTION + ".VISIBILITY";
        if (isJdbc20 ()) {
            String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) ;
            connection_ = testDriver_.getConnection(url,systemObject_.getUserId(),encryptedPassword_);
            statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                      ResultSet.CONCUR_UPDATABLE);
	    try {
		statement_.executeUpdate ("DROP TABLE " + TABLE_);
	    } catch (Exception e) {
	    } 

            statement_.executeUpdate ("CREATE TABLE " + TABLE_ 
                                      + "(C_BLOB BLOB(" + BLOBSIZE_ + "),"
				      + " C_CLOB CLOB(" + CLOBSIZE_ + "))");

            PreparedStatement ps = connection_.prepareStatement ("INSERT INTO " 
                                                                 + TABLE_ + " (C_BLOB, C_CLOB) VALUES (?,?)");
            ps.setBytes (1, BLOB_);
	    ps.setString(2, CLOB_);
            ps.executeUpdate ();
            ps.close ();
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
            statement_.executeUpdate ("DROP TABLE " + TABLE_);
            statement_.close ();
            connection_.close ();
        }
    }

    /*
     *  These are the different scenios.  Each scenario obtains it's own connection
     */

    /**
     * accessBlob -- can the blob be accessed
     * throws an exception if the blob is not valid
     */
    public void accessBlob(Blob blob) throws Exception  {
	stuff = blob.getBytes(1,BLOBSIZE_);
	
    } 

    /**
     * accessClob -- can the Clob be accessed
     * throws an exception if the Clob is not valid
     */
    public void accessClob(Clob clob) throws Exception  {
	int length = (int) clob.length();
	if (length < 40) length=40; 
	String string = clob.getSubString(1,length);
	if ( !CLOB_.equals(string)) {
	    throw new Exception("Clob string(40/"+string.length()+")="+string.substring(0,40) +
				" does not match CLOB_(40/"+CLOB_.length()+")= "+CLOB_.substring(0,40) +
				" "+showDifference( string, CLOB_) ); 
	} 
    } 

    public String showDifference(String a, String b) {
	String output=""; 
	if (a.length() != b.length()) {
	    if (a.length() > b.length()) {
		output += "Second string missing characters "+a.substring(b.length());
	    } else {
		output += "First string missing characters "+b.substring(a.length());
	    } 
	} else {
	    boolean done=false;
	    for (int i = 0; !done && i < a.length(); i++) {
		if (a.charAt(i) != b.charAt(i)) {
		    output += "different at "+i+" first:"+truncate(a.substring(i),40)+" second:"+truncate(b.substring(i),40);
		    done=true; 
		} 
	    } 
	}
	return output; 
	
    }

    public String truncate(String s, int len) {
	if (s.length() < len) return s;
	return s.substring(0,len); 
    } 
    public void adjustConnection(Connection connection, String url) throws Exception {
	//
	// Turn off autocommit if needed
	//
	if (url.indexOf(AUTOCOMMIT_OFF) >= 0) {
	    connection.setAutoCommit(false); 
	} 
    } 

    /**
     * testAccessAfterQueryResultSetClose
     *
     * Tests if the lob is accessible after result set close
     *
     * @parm connectionProperties -- what properties to pass to the created connection
     * @parm blobType   -- either USE_BLOB or USE_CLOB
     * @parm visibility -- either SHOULD_BE_VISIBLE or SHOULD_NOT_BE_VISIBLE  
     */
    public void testAccessAfterQueryResultSetClose(String connectionProperties,
					      int blobType,
					      int visibility) {
	Blob blob = null;
	Clob clob = null ; 

	try { 
	    String url = baseURL_
	      +";"+connectionProperties;
	    Connection connection = testDriver_.getConnection(url,systemObject_.getUserId(), encryptedPassword_);

	    adjustConnection(connection, url);
	    //
	    // Note .. need more testing with different cursor types
	    // 
	    Statement  statement =  connection.createStatement (); 

	    ResultSet rs = statement.executeQuery("SELECT * FROM "+TABLE_);
	    rs.next();

	    switch (blobType) { 
		case USE_BLOB:
		    blob = rs.getBlob(1); 
		      break;
		case USE_CLOB:
		    clob = rs.getClob(2); 
		    break;
		default:
		    failed(new Exception( "testAccessAfterQueryResultSetClose blobTypeNotValue"));
		    return;
	    }
	    boolean isVisible;

	    Exception savedException = null; 
	    rs.close();
	    try {
		switch (blobType) { 
		    case USE_BLOB:
			accessBlob(blob); 
			break;
		    case USE_CLOB:
			accessClob(clob); 
			break;
		}
		isVisible = true;
	    } catch (Exception e) {
		isVisible = false;
		savedException = e; 
	    }

	    statement.close();
	    connection.close();


	    if (visibility == SHOULD_BE_VISIBLE) {
		if (isVisible) {
		    assertCondition(true); 
		} else {
		    failed (savedException, "testAccessAfterQueryResultSetClose: LOB should be visible for properties "+connectionProperties ); 
		}
	    } else {
		if (isVisible) {
		    failed(new Exception("testAccessAfterQueryResultSetClose: Lob should not be visible for properties "+connectionProperties)); 
		} else {
		    assertCondition(true); 
		} 
	    }

	} catch (Exception e) {
                failed (e, "testAccessAfterQueryResultSetClose: Unexpected Exception using properties "+connectionProperties );
	}

    } 


    /**
     * testAccessAfterStatementClose
     *
     * Tests if the lob is accessible after statement close
     *
     * @parm connectionProperties -- what properties to pass to the created connection
     * @parm blobType   -- either USE_BLOB or USE_CLOB
     * @parm visibility -- either SHOULD_BE_VISIBLE or SHOULD_NOT_BE_VISIBLE  
     */
    public void testAccessAfterStatementClose(String connectionProperties,
					      int blobType,
					      int visibility) {
	Blob blob = null;
	Clob clob = null ; 

	try { 
	    String url = baseURL_
	      + ";"+connectionProperties;
            if(getDriver() == JDTestDriver.DRIVER_TOOLBOX)  //@B1A
                url+= ";hold statements=true";
	    Connection connection = testDriver_.getConnection(url,systemObject_.getUserId(), encryptedPassword_);

	    adjustConnection(connection, url); 
	    Statement  statement =  connection.createStatement (); 

	    ResultSet rs = statement.executeQuery("SELECT * FROM "+TABLE_);
	    rs.next();

	    switch (blobType) { 
		case USE_BLOB:
		    blob = rs.getBlob(1); 
		      break;
		case USE_CLOB:
		    clob = rs.getClob(2); 
		    break;
		default:
		    failed(new Exception("testAccessAfterStatementClose: blobTypeNotValue"));
		    return;
	    }
	    boolean isVisible;
            
	    statement.close();       //@B1C changed to statement.close since that is what the method implies
	    Exception savedException = null; 
	    try {
		switch (blobType) { 
		    case USE_BLOB:
			accessBlob(blob); 
			break;
		    case USE_CLOB:
			accessClob(clob); 
			break;
		}
		isVisible = true;
	    } catch (Exception e) {
		isVisible = false;
		savedException = e; 
	    }

	    //@B1D statement.close();
	    connection.close();


	    if (visibility == SHOULD_BE_VISIBLE) {
		if (isVisible) {
		    assertCondition(true); 
		} else {
		    failed (savedException, "testAccessAfterStatementClose: LOB should be visible for properties "+connectionProperties ); 
		}
	    } else {
		if (isVisible) {
		    failed(new Exception("testAccessAfterStatementClose: Lob should not be visible for properties "+connectionProperties)); 
		} else {
		    assertCondition(true); 
		} 
	    }

	} catch (Exception e) {
                failed (e, "testAccessAfterStatementClose: Unexpected Exception using properties "+connectionProperties );
	}

    } 



    /**
     * testAccessAfterConnectionCommit
     *
     * Tests if the lob is accessible after statement close
     *
     * @parm connectionProperties -- what properties to pass to the created connection
     * @parm blobType   -- either USE_BLOB or USE_CLOB
     * @parm visibility -- either SHOULD_BE_VISIBLE or SHOULD_NOT_BE_VISIBLE  
     */
    public void testAccessAfterConnectionCommit(String connectionProperties,
					      int blobType,
					      int visibility) {
	Blob blob = null;
	Clob clob = null ; 

	try { 
	    String url = baseURL_
	      + ";"+connectionProperties;
	    Connection  connection = testDriver_.getConnection(url,systemObject_.getUserId(), encryptedPassword_);

	    adjustConnection(connection, url); 
	    Statement  statement =  connection.createStatement (); 

	    ResultSet rs = statement.executeQuery("SELECT * FROM "+TABLE_);
	    rs.next();

	    switch (blobType) { 
		case USE_BLOB:
		    blob = rs.getBlob(1); 
		      break;
		case USE_CLOB:
		    clob = rs.getClob(2); 
		    break;
		default:
		    failed(new Exception("testAccessAfterConnectionCommit: blobTypeNotValue"));
		    return;
	    }
	    boolean isVisible;

	    Exception savedException = null; 
	    connection.commit();         //@B1C changed to connection.commit since that is what the method name implies
	    rs.close(); //@PDA per db issue 25928

	    try {
		switch (blobType) { 
		    case USE_BLOB:
			accessBlob(blob); 
			break;
		    case USE_CLOB:
			accessClob(clob); 
			break;
		}
		isVisible = true;
	    } catch (Exception e) {
		isVisible = false;
		savedException = e; 
	    }

	    connection.close();


	    if (visibility == SHOULD_BE_VISIBLE) {
		if (isVisible) {
		    assertCondition(true); 
		} else {
		    failed (savedException, "testAccessAfterConnectionCommit: LOB should be visible for properties "+connectionProperties ); 
		}
	    } else {
		if (isVisible) {
		    failed(new Exception("testAccessAfterConnectionCommit: Lob should not be visible for properties "+connectionProperties)); 
		} else {
		    assertCondition(true); 
		} 
	    }

	} catch (Exception e) {
                failed (e, "testAccessAfterConnectionCommit: Unexpected Exception using properties "+connectionProperties );
	}

    } 






    /**
     Variations 1 - 16  test the visibility after result set close
    **/
    public void Var001() {
        if (checkJdbc20 ()) testAccessAfterQueryResultSetClose(A0H0L0, USE_BLOB, SHOULD_BE_VISIBLE); 
    }
    public void Var002() {
        if (checkJdbc20 ()) testAccessAfterQueryResultSetClose(A0H0L1, USE_BLOB, SHOULD_BE_VISIBLE); 
    }
    public void Var003() {
        if (checkJdbc20 ()) testAccessAfterQueryResultSetClose(A0H1L0, USE_BLOB, SHOULD_BE_VISIBLE); 
    }
    public void Var004() {
        if (checkJdbc20 ()) testAccessAfterQueryResultSetClose(A0H1L1, USE_BLOB, SHOULD_BE_VISIBLE); 
    }
    public void Var005() {
	if (checkJdbc20 ()) {
		// In this case the native driver doesn't comply with the spec.  Since a
		// locator is not used, the data is read into the lob and the driver doesn't
		// invalidate existing lob objects on a commit.
		testAccessAfterQueryResultSetClose(A1H0L0, USE_BLOB, SHOULD_BE_VISIBLE);
	}
    }
    public void Var006() {
        if (checkJdbc20 ()) testAccessAfterQueryResultSetClose(A1H0L1, USE_BLOB, SHOULD_NOT_BE_VISIBLE); 
    }
    public void Var007() {
	if (checkJdbc20 ()) { 

		// In this case the native driver doesn't comply with the spec.  Since a
		// locator is not used, the data is read into the lob and the driver doesn't
		// invalidate existing lob objects on a commit.
		testAccessAfterQueryResultSetClose(A1H1L0, USE_BLOB, SHOULD_BE_VISIBLE);
	}
    }
    public void Var008() {
        if (checkJdbc20 ()) testAccessAfterQueryResultSetClose(A1H1L1, USE_BLOB, SHOULD_NOT_BE_VISIBLE); 
    }


    public void Var009() {
        if (checkJdbc20 ()) testAccessAfterQueryResultSetClose(A0H0L0, USE_CLOB, SHOULD_BE_VISIBLE); 
    }
    public void Var010() {
        if (checkJdbc20 ()) testAccessAfterQueryResultSetClose(A0H0L1, USE_CLOB, SHOULD_BE_VISIBLE); 
    }
    public void Var011() {
        if (checkJdbc20 ()) testAccessAfterQueryResultSetClose(A0H1L0, USE_CLOB, SHOULD_BE_VISIBLE); 
    }
    public void Var012() {
        if (checkJdbc20 ()) testAccessAfterQueryResultSetClose(A0H1L1, USE_CLOB, SHOULD_BE_VISIBLE); 
    }
    public void Var013() {
	if (checkJdbc20 ()) {
		// In this case the native driver doesn't comply with the spec.  Since a
		// locator is not used, the data is read into the lob and the driver doesn't
		// invalidate existing lob objects on a commit.
		testAccessAfterQueryResultSetClose(A1H0L0, USE_CLOB, SHOULD_BE_VISIBLE);
	}
    }
    public void Var014() {
        if (checkJdbc20 ()) testAccessAfterQueryResultSetClose(A1H0L1, USE_CLOB, SHOULD_NOT_BE_VISIBLE); 
    }
    public void Var015() {
	if (checkJdbc20 ()) { 

		// In this case the native driver doesn't comply with the spec.  Since a
		// locator is not used, the data is read into the lob and the driver doesn't
		// invalidate existing lob objects on a commit.
		testAccessAfterQueryResultSetClose(A1H1L0, USE_CLOB, SHOULD_BE_VISIBLE);
	}
    }
    public void Var016() {
        if (checkJdbc20 ()) testAccessAfterQueryResultSetClose(A1H1L1, USE_CLOB, SHOULD_NOT_BE_VISIBLE); 
    }


    /**
     Variations 17 - 32  test the visibility after statement close 
    **/
    public void Var017() {
        if (checkJdbc20 ()) testAccessAfterStatementClose(A0H0L0, USE_BLOB, SHOULD_BE_VISIBLE); 
    }
    public void Var018() {
        if (checkJdbc20 ()) testAccessAfterStatementClose(A0H0L1, USE_BLOB, SHOULD_BE_VISIBLE); 
    }
    public void Var019() {
        if (checkJdbc20 ()) testAccessAfterStatementClose(A0H1L0, USE_BLOB, SHOULD_BE_VISIBLE); 
    }
    public void Var020() {
        if (checkJdbc20 ()) testAccessAfterStatementClose(A0H1L1, USE_BLOB, SHOULD_BE_VISIBLE); 
    }
    public void Var021() {
	if (checkJdbc20 ()) {
		// In this case the native driver doesn't comply with the spec.  Since a
		// locator is not used, the data is read into the lob and the driver doesn't
		// invalidate existing lob objects on a commit.
		testAccessAfterStatementClose(A1H0L0, USE_BLOB, SHOULD_BE_VISIBLE);
	}
    }
    public void Var022() {
        if (checkJdbc20 ()) testAccessAfterStatementClose(A1H0L1, USE_BLOB, SHOULD_NOT_BE_VISIBLE); 
    }
    public void Var023() {
	if (checkJdbc20 ()) { 

		// In this case the native driver doesn't comply with the spec.  Since a
		// locator is not used, the data is read into the lob and the driver doesn't
		// invalidate existing lob objects on a commit.
		testAccessAfterStatementClose(A1H1L0, USE_BLOB, SHOULD_BE_VISIBLE);
	}
    }
    public void Var024() {
        if (checkJdbc20 ()) testAccessAfterStatementClose(A1H1L1, USE_BLOB, SHOULD_NOT_BE_VISIBLE); 
    }


    public void Var025() {
        if (checkJdbc20 ()) testAccessAfterStatementClose(A0H0L0, USE_CLOB, SHOULD_BE_VISIBLE); 
    }
    public void Var026() {
        if (checkJdbc20 ()) testAccessAfterStatementClose(A0H0L1, USE_CLOB, SHOULD_BE_VISIBLE); 
    }
    public void Var027() {
        if (checkJdbc20 ()) testAccessAfterStatementClose(A0H1L0, USE_CLOB, SHOULD_BE_VISIBLE); 
    }
    public void Var028() {
        if (checkJdbc20 ()) testAccessAfterStatementClose(A0H1L1, USE_CLOB, SHOULD_BE_VISIBLE); 
    }
    public void Var029() {
	if (checkJdbc20 ()) {
		// In this case the native driver doesn't comply with the spec.  Since a
		// locator is not used, the data is read into the lob and the driver doesn't
		// invalidate existing lob objects on a commit.
		testAccessAfterStatementClose(A1H0L0, USE_CLOB, SHOULD_BE_VISIBLE);
	}
    }
    public void Var030() {
        if (checkJdbc20 ()) testAccessAfterStatementClose(A1H0L1, USE_CLOB, SHOULD_NOT_BE_VISIBLE); 
    }
    public void Var031() {
	if (checkJdbc20 ()) { 

		// In this case the native driver doesn't comply with the spec.  Since a
		// locator is not used, the data is read into the lob and the driver doesn't
		// invalidate existing lob objects on a commit.
		testAccessAfterStatementClose(A1H1L0, USE_CLOB, SHOULD_BE_VISIBLE);
	}
    }
    public void Var032() {
        if (checkJdbc20 ()) testAccessAfterStatementClose(A1H1L1, USE_CLOB, SHOULD_NOT_BE_VISIBLE); 
    }



    /**
     Variations 33 - 48  test the visibility after coonection commit
     **/
    public void Var033() {
	if (checkJdbc20 ()) {
		// In this case the native driver doesn't comply with the spec.  Since a
		// locator is not used, the data is read into the lob and the driver doesn't
		// invalidate existing lob objects on a commit.
		testAccessAfterConnectionCommit(A0H0L0, USE_BLOB, SHOULD_BE_VISIBLE);
	}
    }
    public void Var034() {
        if (checkJdbc20 ()) testAccessAfterConnectionCommit(A0H0L1, USE_BLOB, SHOULD_NOT_BE_VISIBLE); 
    }
    public void Var035() {
	if (checkJdbc20 ()) {
		// In this case the native driver doesn't comply with the spec.  Since a
		// locator is not used, the data is read into the lob and the driver doesn't
		// invalidate existing lob objects on a commit.
		testAccessAfterConnectionCommit(A0H1L0, USE_BLOB, SHOULD_BE_VISIBLE);
	}

    }
    public void Var036() {
        if (checkJdbc20 ()) testAccessAfterConnectionCommit(A0H1L1, USE_BLOB, SHOULD_NOT_BE_VISIBLE); 
    }
    public void Var037() {
	if (checkJdbc20 ()) {
		// In this case the native driver doesn't comply with the spec.  Since a
		// locator is not used, the data is read into the lob and the driver doesn't
		// invalidate existing lob objects on a commit.
		testAccessAfterConnectionCommit(A1H0L0, USE_BLOB, SHOULD_BE_VISIBLE);
	}
    }
    public void Var038() {
        if (checkJdbc20 ()) testAccessAfterConnectionCommit(A1H0L1, USE_BLOB, SHOULD_NOT_BE_VISIBLE); 
    }
    public void Var039() {
	if (checkJdbc20 ()) { 

		// In this case the native driver doesn't comply with the spec.  Since a
		// locator is not used, the data is read into the lob and the driver doesn't
		// invalidate existing lob objects on a commit.
		testAccessAfterConnectionCommit(A1H1L0, USE_BLOB, SHOULD_BE_VISIBLE);
	}
    }
    public void Var040() {
        if (checkJdbc20 ()) testAccessAfterConnectionCommit(A1H1L1, USE_BLOB, SHOULD_NOT_BE_VISIBLE);
    }


    public void Var041() {
	if (checkJdbc20 ()) {
		// In this case the native driver doesn't comply with the spec.  Since a
		// locator is not used, the data is read into the lob and the driver doesn't
		// invalidate existing lob objects on a commit.
		testAccessAfterConnectionCommit(A0H0L0, USE_CLOB, SHOULD_BE_VISIBLE);
	}
    }

    public void Var042() {
        if (checkJdbc20 ()) testAccessAfterConnectionCommit(A0H0L1, USE_CLOB, SHOULD_NOT_BE_VISIBLE);
    }
    public void Var043() {
	if (checkJdbc20 ()) {
		// In this case the native driver doesn't comply with the spec.  Since a
		// locator is not used, the data is read into the lob and the driver doesn't
		// invalidate existing lob objects on a commit.
		testAccessAfterConnectionCommit(A0H1L0, USE_CLOB, SHOULD_BE_VISIBLE);
	}
    }
    public void Var044() {
        if (checkJdbc20 ()) testAccessAfterConnectionCommit(A0H1L1, USE_CLOB, SHOULD_NOT_BE_VISIBLE); 
    }
    public void Var045() {
	if (checkJdbc20 ()) {
		// In this case the native driver doesn't comply with the spec.  Since a
		// locator is not used, the data is read into the lob and the driver doesn't
		// invalidate existing lob objects on a commit.
		testAccessAfterConnectionCommit(A1H0L0, USE_CLOB, SHOULD_BE_VISIBLE);
	}
    }
    public void Var046() {
        if (checkJdbc20 ()) testAccessAfterConnectionCommit(A1H0L1, USE_CLOB, SHOULD_NOT_BE_VISIBLE); 
    }
    public void Var047() {
	if (checkJdbc20 ()) { 

		// In this case the native driver doesn't comply with the spec.  Since a
		// locator is not used, the data is read into the lob and the driver doesn't
		// invalidate existing lob objects on a commit.
		testAccessAfterConnectionCommit(A1H1L0, USE_CLOB, SHOULD_BE_VISIBLE);
	}
    }
    public void Var048() {
        if (checkJdbc20 ()) testAccessAfterConnectionCommit(A1H1L1, USE_CLOB, SHOULD_NOT_BE_VISIBLE); 
    }



}
