///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDLobBlob.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;                          //@C1A
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDLobBlob.  This tests the following method
of the JDBC Blob class:

<ul>
<li>getBinaryStream()
<li>getBytes()
<li>length()
<li>position()
<li>setBytes()    // @C1A
<li>truncate()    // @C1A
</ul>
**/
public class JDLobBlob
extends JDTestcase
{



    // Private data.
    private Statement           statement1_;
    private Statement           statement2_;
    private ResultSet           rs_;
    StringBuffer sb = new StringBuffer(); 
    //
    // The v5r2nativeFunctions is so that the native driver will pass the
    // regression bucket in V5R2.  There are many known problems here, but
    // if the problems are fixed in the native driver for V5R2, then this
    // code can be removed.
    // 
    private              boolean        v5r2nativeFunctions_ = false;
    private ResultSet           rs2_;                                     //@C1A
    public static String TABLE_          = JDLobTest.COLLECTION + ".BLOB";
    public static       byte[]  MEDIUM_         = null; // final
    public static       byte[]  LARGE_          = null; // final
    public static       byte[]  SMALL_          = null; // @C1A
    public static final int     WIDTH_          = 30000;



    private boolean var060Called_ = false;

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

        SMALL_ = new byte[] { (byte) 22, (byte) 4, (byte) -2};            // @C1A
    }



    /**
    Constructor.
    **/
    public JDLobBlob (AS400 systemObject,
                      Hashtable namesAndVars,
                      int runMode,
                      FileOutputStream fileOutputStream,
                      String password)
    {
        super (systemObject, "JDLobBlob",
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
        TABLE_          = JDLobTest.COLLECTION + ".BLOB";
        if (isJdbc20 ()) {
            String url = baseURL_;
            connection_ = testDriver_.getConnection (url, systemObject_.getUserId(), encryptedPassword_);
            statement1_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                      ResultSet.CONCUR_UPDATABLE);

            statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE);

	    JDTestDriver.dropTable(statement1_, TABLE_); 
            statement1_.executeUpdate ("CREATE TABLE " + TABLE_ 
                                      + "(C_VARBINARY VARCHAR(" + WIDTH_ + ") FOR BIT DATA)");

            PreparedStatement ps = connection_.prepareStatement ("INSERT INTO " 
                                                                 + TABLE_ + " (C_VARBINARY) VALUES (?)");
            ps.setString (1, "");
            ps.executeUpdate ();
            ps.setBytes (1, MEDIUM_);
            ps.executeUpdate ();
            ps.setBytes (1, LARGE_);
            ps.executeUpdate ();            
            ps.setBytes (1, SMALL_);   // @C1A
            ps.executeUpdate ();       // @C1A
	    ps.setBytes (1, MEDIUM_);  // @D1
	    ps.executeUpdate ();       // @D1

            ps.close ();

            rs_ = statement1_.executeQuery ("SELECT * FROM " + TABLE_);
        }
	v5r2nativeFunctions_ = (getRelease() == JDTestDriver.RELEASE_V5R2M0 || getRelease() == JDTestDriver.RELEASE_V5R1M0) && (getDriver() == JDTestDriver.DRIVER_NATIVE);
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
	    JDTestDriver.dropTable(statement1_, TABLE_); 
            statement1_.close ();
            connection_.close ();
        }
    }



    /**
    getBinaryStream() - When the lob is empty.
    **/
    public void Var001()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (1);
                Blob blob = rs_.getBlob ("C_VARBINARY");
		InputStream v = blob.getBinaryStream ();
		sb.setLength(0); 
		if(getDriver() == JDTestDriver.DRIVER_NATIVE &&		// @D4
		   getRelease() >= JDTestDriver.RELEASE_V5R3M0)		// @D4
		    assertCondition( compareBeginsWithBytes( v, new byte[0],sb),sb);	// @D4
		else							// @D4
		    assertCondition (compare (v, new byte[0],sb),sb);
	    }
	    catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


    /**
    getBinaryStream() - When the lob is not empty.
    **/
    public void Var002()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                InputStream v = blob.getBinaryStream ();
                sb.setLength(0); 
		if(getDriver() == JDTestDriver.DRIVER_NATIVE &&		// @D4
		   getRelease() >= JDTestDriver.RELEASE_V5R3M0)		// @D4
		    assertCondition( compareBeginsWithBytes( v, MEDIUM_,sb),sb);	// @D4
		else							// @D4
                assertCondition (compare (v, MEDIUM_,sb),sb);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getBinaryStream() - When the lob is full.
    **/
    public void Var003()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (3);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                InputStream v = blob.getBinaryStream ();
                sb.setLength(0); 
if(getDriver() == JDTestDriver.DRIVER_NATIVE &&			// @D4
		   getRelease() >= JDTestDriver.RELEASE_V5R3M0)			// @D4
		    assertCondition( compareBeginsWithBytes( v, LARGE_,sb));	// @D4
		else								// @D4
                assertCondition (compare (v, LARGE_,sb));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getBytes() - Should throw an exception when start is less
    than 0.
    **/
    public void Var004()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                blob.getBytes (-1, 5);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    getBytes() - Should throw an exception when start is 0.
    **/
    public void Var005()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                blob.getBytes (0, 5);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    getBytes() - Should throw an exception when start is greater
    than the length of the lob.

     @D2: Native driver extracts with padding for specified length exceeding actual length but within limits of maxLength
    **/
    public void Var006()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                blob.getBytes (MEDIUM_.length, 5);
		if(getRelease() >= JDTestDriver.RELEASE_V5R3M0 &&	// @D2
		   getDriver() == JDTestDriver.DRIVER_NATIVE )		// @D2
		    succeeded();					// @D2
		else							// @D2
		    failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    getBytes() - Should throw an exception when length is less
    than 0.
    **/
    public void Var007()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                blob.getBytes (1, -1);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


    /**
    getBytes() - Should throw an exception when length extends
    past the end of the lob.

    @D2: Native driver extracts with padding for specified length exceeding actual length but within limits of maxLength
    **/
    public void Var008()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                blob.getBytes (3, MEDIUM_.length - 1);
		if(getRelease() >= JDTestDriver.RELEASE_V5R3M0 &&	// @D2
		   getDriver() == JDTestDriver.DRIVER_NATIVE )		// @D2
		    succeeded();					// @D2
		else							// @D2
		    failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    getBytes() - Should throw an exception on an empty lob.
    @CRS - This variation should succeed.
    **/
    public void Var009()
    {
        if (checkJdbc20 ()) {
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE &&  getRelease() ==  JDTestDriver.RELEASE_V5R1M0 ) {
		try {
		    rs_.absolute (1);
		    Blob blob = rs_.getBlob ("C_VARBINARY");
		    byte[] v = blob.getBytes (1, 0);
		    failed ("Didn't throw SQLException "+v);                        // @B1C
		}                                                                   // @B1C
		catch (Exception e) {                                               // @B1C
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");       // @B1C
		}                                                                   // @B1C

	    } else { 

		try {
		    rs_.absolute (1);
		    Blob blob = rs_.getBlob ("C_VARBINARY");
		    byte[] v = blob.getBytes (1, 0);
//                failed ("Didn't throw SQLException");                           // @B1C
		    assertCondition(v != null) ;
		}                                                                   // @B1C
		catch (Exception e) {                                               // @B1C
//                assertExceptionIsInstanceOf (e, "java.sql.SQLException");       // @B1C
		    failed(e, "Unexpected exception");
		}                                                           // @B1C
	    }
        }
    }



    /**
    getBytes() - Should work to retrieve all of a non-empty lob.
    **/
    public void Var010()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                byte[] v = blob.getBytes (1, MEDIUM_.length);
                assertCondition (areEqual (v, MEDIUM_));    //@C2C
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getBytes() - Should work to retrieve all of a full lob.
    **/
    public void Var011()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (3);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                byte[] v = blob.getBytes (1, WIDTH_);
                assertCondition (areEqual (v, LARGE_));   //@C2C
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getBytes() - Should work to retrieve the first part of a non-empty lob.
    **/
    public void Var012()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                byte[] v = blob.getBytes (1, 3);
                byte[] expected = new byte[] { 0, -2, -4};
                assertCondition (areEqual (v, expected));    //@C2C
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getBytes() - Should work to retrieve the middle part of a non-empty lob.
    **/
    public void Var013()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                byte[] v = blob.getBytes (9, 6);
                byte[] expected = new byte[] { -16, -18, -20, -22, -24, -26};
                assertCondition (areEqual (v, expected));  //@C2C
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getBytes() - Should work to retrieve the last part of a non-empty lob.
    **/
    public void Var014()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                byte[] v = blob.getBytes (48, 3);
                byte[] expected = new byte[] { -94, -96, -98};
                assertCondition (areEqual (v, expected));     //@C2C
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    length() - When the lob is empty.
    **/
    public void Var015()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (1);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                assertCondition (blob.length() == 0);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


    /**
    length() - When the lob is not empty.
    **/
    public void Var016()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                assertCondition (blob.length () == MEDIUM_.length);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    length() - When the lob is full.
    **/
    public void Var017()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (3);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                assertCondition (blob.length () == LARGE_.length);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    position(byte[],long) - Should throw an exception when the pattern is null.
    **/
    public void Var018()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                blob.position ((byte[]) null, (long) 1);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    position(byte[],long) - Should throw an exception when start is less than 0.
    **/
    public void Var019()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                byte[] test = new byte[] { -45, -23, 32};
                blob.position (test, (long) -1);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    position(byte[],long) - Should throw an exception when start is0.
    **/
    public void Var020()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                byte[] test = new byte[] { -45, -23, 32};
                blob.position (test, (long) 0);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    position(byte[],long) - Should throw an exception when start is 
    greater than the length of the lob.
    **/
    public void Var021()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                byte[] test = new byte[] { -45, -23, 32};
                blob.position (test, (long) MEDIUM_.length + 1);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    position(byte[],long) - Should return -1 when the pattern is not found at all.
    **/
    public void Var022()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                byte[] test = new byte[] {  32, 0, -1, 32, 44, 34};
                long v = blob.position (test, (long) 1);
                assertCondition (v == -1);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    position(byte[],long) - Should return -1 when the pattern is not found after
    the starting position, although it does appear before the starting position.
    **/
    public void Var023()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                byte[] test = new byte[] { -6, -8, -10, -12};
                long v = blob.position (test, (long) 33);
                assertCondition (v == -1);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    position(byte[],long) - Should return 0 when the pattern is found at
    the beginning of the lob.
    **/
    public void Var024()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                byte[] test = new byte[] { 0, -2, -4, -6, -8, -10};
                long v = blob.position (test, (long) 1);
		if (v5r2nativeFunctions_) {
		    assertCondition (v == 0);
		} else { 
		    assertCondition (v == 1,"v = "+v+" SB 1 ");
		}
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    position(byte[],long) - Should return the position when the pattern is 
    found in the middle of the lob, and start is 1.
    **/
    public void Var025()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                byte[] test = new byte[] { -28, -30};
                long v = blob.position (test, (long) 1);
		if (v5r2nativeFunctions_) {
		    assertCondition (v == 14);
		} else { 
		    assertCondition (v == 15,"v = "+v+" SB 15 ");
		}
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    position(byte[],long) - Should return the position when the pattern is 
    found in the middle of the lob, and start is before where the pattern appears.
    **/
    public void Var026()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                byte[] test = new byte[] { -32, -34, -36, -38, -40};
                long v = blob.position (test, (long) 7);
		if (v5r2nativeFunctions_) {
		    assertCondition (v == 16);
		} else {
		    assertCondition (v == 17,"v = "+v+" SB 17 ");
		}
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    position(byte[],long) - Should return the position when the pattern is 
    found in the middle of the lob, and start is right where the pattern appears.
    **/
    public void Var027()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                byte[] test = new byte[] { -20};
                long v = blob.position (test, (long) 11);
		if (v5r2nativeFunctions_) {
		    assertCondition (v == 10);
		} else { 
		    assertCondition (v == 11,"v = "+v+" SB 11 ");
		}
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    position(byte[],long) - Should return the position when the pattern is 
    found at the end of the lob, and start is 1.
    **/
    public void Var028()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                byte[] test = new byte[] { -92, -94, -96, -98};
                long v = blob.position (test, (long) 1);
		if (v5r2nativeFunctions_) {
		    assertCondition (v == 46);
		} else { 
		    assertCondition (v == 47,"v = "+v+" SB 47 ");
		}
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    position(byte[],long) - Should return the position when the pattern is 
    found at the end of the lob, and start is right where the pattern occurs.
    **/
    public void Var029()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                byte[] test = new byte[] { -98};
                long v = blob.position (test, (long) 50);
		if (v5r2nativeFunctions_) {
		    assertCondition (v == 49);
		} else { 
		    assertCondition (v == 50,"v = "+v+" SB 50 ");
		}

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    position(byte[],long) - Should return 0 when the pattern is the empty
    string and start is 1.
    **/
    public void Var030()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                byte[] test = new byte[0];
                long v = blob.position (test, (long) 1);
		if (v5r2nativeFunctions_) {
		    assertCondition (v == 0);
		} else {
		    assertCondition (v == 1,"v = "+v+" SB 1 ");
		}
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    position(byte[],long) - Should return start when the pattern is the empty
    string and start is in the middle.
    **/
    public void Var031()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                byte[] test = new byte[0];
                long v = blob.position (test, (long) 39);
		if (v5r2nativeFunctions_) {
		    assertCondition (v == 38);
		} else {
		    assertCondition (v == 39,"v = "+v+" SB 39");
		}
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    position(byte[],long) - Should return length-1 when the pattern is the empty
    string and start is the length.
    **/
    public void Var032()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                byte[] test = new byte[0];
                long v = blob.position (test, (long) MEDIUM_.length);
		if (v5r2nativeFunctions_) {
		    assertCondition (v == (long) MEDIUM_.length-1);
		} else { 
		    assertCondition (v == (long) MEDIUM_.length,"v = "+v+" SB "+MEDIUM_.length);
		}
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    position(Blob,long) - Should throw an exception when the pattern is null.
    **/
    public void Var033()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                blob.position ((Blob) null, (long) 1);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    position(Blob,long) - Should throw an exception when start is less than 0.
    **/
    public void Var034()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                byte[] test = new byte[] { -34, 23, 5, 6};
                blob.position (new JDLobTest.JDTestBlob (test), (long) -1);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    position(Blob,long) - Should throw an exception when start is 0.
    **/
    public void Var035()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                byte[] test = new byte[] { -34, 23, 5, 6};
                blob.position (new JDLobTest.JDTestBlob (test), (long) 0);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    position(Blob,long) - Should throw an exception when start is 
    greater than the length of the lob.
    **/
    public void Var036()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                byte[] test = new byte[] { 3, 56, -3, -1, 111};
                blob.position (new JDLobTest.JDTestBlob (test), (long) MEDIUM_.length + 1);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    position(Blob,long) - Should return -1 when the pattern is not found at all.
    **/
    public void Var037()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                byte[] test = new byte[] { 23, 0};
                long v = blob.position (new JDLobTest.JDTestBlob (test), (long) 1);
                assertCondition (v == -1);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    position(Blob,long) - Should return -1 when the pattern is not found after
    the starting position, although it does appear before the starting position.
    **/
    public void Var038()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                byte[] test = new byte[] { -6, -8};
                long v = blob.position (new JDLobTest.JDTestBlob (test), (long) 14);
                assertCondition (v == -1);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    position(Blob,long) - Should return 0 when the pattern is found at
    the beginning of the lob.
    **/
    public void Var039()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                byte[] test = new byte[] { 0, -2, -4, -6, -8, -10, -12, -14};
                long v = blob.position (new JDLobTest.JDTestBlob (test), (long) 1);
		if (v5r2nativeFunctions_) {
		    assertCondition (v == 0);
		} else { 
		    assertCondition (v == 1,"v = "+v+" SB 1 ");
		}
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    position(Blob,long) - Should return the position when the pattern is 
    found in the middle of the lob, and start is 1.
    **/
    public void Var040()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                byte[] test = new byte[] { -16, -18, -20, -22, -24, -26, -28};
                long v = blob.position (new JDLobTest.JDTestBlob (test), (long) 1);
		if (v5r2nativeFunctions_) {
		    assertCondition (v == 8);
		} else { 
		    assertCondition (v == 9,"v = "+v+" SB 9 ");
		}
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    position(Blob,long) - Should return the position when the pattern is 
    found in the middle of the lob, and start is before where the pattern appears.
    **/
    public void Var041()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                byte[] test = new byte[] { -30, -32, -34, -36, -38, -40};
                long v = blob.position (new JDLobTest.JDTestBlob (test), (long) 5);
		if (v5r2nativeFunctions_) {
		    assertCondition (v == 15);
		} else { 
		    assertCondition (v == 16,"v = "+v+" SB 16");
		}
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    position(Blob,long) - Should return the position when the pattern is 
    found in the middle of the lob, and start is right where the pattern appears.
    **/
    public void Var042()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                byte[] test = new byte[] { -42, -44, -46, -48, -50, -52, -54, -56};
                long v = blob.position (new JDLobTest.JDTestBlob (test), (long) 22);
		if (v5r2nativeFunctions_) {		
		    assertCondition (v == 21);
		} else { 
		    assertCondition (v == 22,"v = "+v+" SB 22 ");
		}
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    position(Blob,long) - Should return the position when the pattern is 
    found at the end of the lob, and start is 1.
    **/
    public void Var043()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                byte[] test = new byte[] { -90, -92, -94, -96, -98};
                long v = blob.position (new JDLobTest.JDTestBlob (test), (long) 1);
		if (v5r2nativeFunctions_) {
		    assertCondition (v == 45);
		} else {
		    assertCondition (v == 46,"v = "+v+" SB 46 ");
		} 
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    position(Blob,long) - Should return the position when the pattern is 
    found at the end of the lob, and start is right where the pattern occurs.
    **/
    public void Var044()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                byte[] test = new byte[] { -96, -98};
                long v = blob.position (new JDLobTest.JDTestBlob (test), (long) 49);
		if (v5r2nativeFunctions_) {
		    assertCondition (v == 48);
		} else { 
		    assertCondition (v == 49,"v = "+v+" SB 49 ");
		}
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    position(Blob,long) - Should return 0 when the pattern is the empty
    lob and start is 1.
    **/
    public void Var045()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                byte[] test = new byte[0];
                long v = blob.position (new JDLobTest.JDTestBlob (test), (long) 1);
		if (v5r2nativeFunctions_) {
		    assertCondition (v == 0);
		} else { 
		    assertCondition (v == 1,"v = "+v+" SB 1 ");
		}
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    position(Blob,long) - Should return start when the pattern is the empty
    lob and start is in the middle.
    **/
    public void Var046()
    {
        if (checkJdbc20 ()) {
	    try {
		rs_.absolute (2);
		Blob blob = rs_.getBlob ("C_VARBINARY");              
		byte[] test = new byte[0];
		long v = blob.position (new JDLobTest.JDTestBlob (test), (long) 45);
		if (v5r2nativeFunctions_) {
		    assertCondition (v == 44);
		} else { 
		    assertCondition (v == 45,"v = "+v+" SB 45");
		}
	    }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    position(Blob,long) - Should return length-1 when the pattern is the empty
    lob and start is the length.
    **/
    public void Var047()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (2);
                Blob blob = rs_.getBlob ("C_VARBINARY");
                byte[] test = new byte[0];
                long v = blob.position (new JDLobTest.JDTestBlob (test), (long) MEDIUM_.length);
		if (v5r2nativeFunctions_) {
		    assertCondition (v == (long) MEDIUM_.length -1 );
		} else {
		    assertCondition (v == (long) MEDIUM_.length,"v = "+v+" SB "+MEDIUM_.length);
		}
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    setBytes(long, byte[]) - Should throw an exception when start is less
    than 0.
    **/
    public void Var048()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {                                                
            try {                                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute (4);                                                          
                Blob blob = rs2_.getBlob ("C_VARBINARY");                                   
                blob.setBytes (-1, new byte[] { (byte) -4});                                  
                failed ("Didn't throw SQLException");                                       
                rs2_.close();                                                               
            }
            catch (Exception e) {                                                           
                try {                                                                       
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");               
                    rs2_.close();                                                           
                }
                catch (Exception s) {                                                       
                    failed (s, "Unexpected Exception");                                     
                }
            }
        }
    }                                                                                       

    /**
    setBytes(long, byte[]) - Should throw an exception when start is 0.
    **/
    public void Var049()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {                                                
            try {                                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute (4);                                                          
                Blob blob = rs2_.getBlob ("C_VARBINARY");                                   
                blob.setBytes (0, new byte[] { (byte) -9, (byte) 3});                                                                            
                failed ("Didn't throw SQLException");                                       
                rs2_.close();                                                               
            }
            catch (Exception e) {                                                           
                try {                                                                       
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");               
                    rs2_.close();                                                           
                }
                catch (Exception s) {                                                       
                    failed (s, "Unexpected Exception");                                     
                }
            }
        }
    }



    /**
    setBytes(long, byte[]) - Should throw an exception when start is greater
    than the length of the lob.
    @CRS - This variation should succeed. Only when you try to write past the
    max column size will it be an exception.
    **/
  public void Var050() {
    if (checkUpdateableLobsSupport()) {
      {

        try {
          rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);
          rs2_.absolute(4);
          Blob blob = rs2_.getBlob("C_VARBINARY");
          blob.setBytes(SMALL_.length + 1, new byte[] { (byte) 44 });
   
          rs2_.close();
          succeeded();
        } catch (Exception e) {
          failed(e, "Unexpected Exception def ");
        }
      }
    }
  }

    /**
    setBytes(long, byte[]) - Should throw an exception when length is less 
    than 0 for the number of bytes to write.
    **/
    public void Var051()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {                                                
            try {                                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute (4);                                                          
                Blob blob = rs2_.getBlob ("C_VARBINARY");                                   
                blob.setBytes (1, new byte[] { (byte) 45}, 1, -1);                                                                              
                failed ("Didn't throw SQLException");                                       
                rs2_.close();                                                               
            }
            catch (Exception e) {                                                           
                try {                                                                       
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");               
                    rs2_.close();                                                           
                }
                catch (Exception s) {                                                       
                    failed (s, "Unexpected Exception");                                     
                }
            }
        }
    }


    //@C2C Changed testcase to not expect an exception if the writing of the bytes will extend
    //@C2C past the end of the lob.
    /**
    setBytes(long, byte[]) - Should now throw an exception when length of bytes to write extends
    past the length of the lob as long as start is not passed the end of the lob.
    **/
    public void Var052()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {                                                
            try {                                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute (4);                                                          
                Blob blob = rs2_.getBlob ("C_VARBINARY");
                byte[] expected = new byte[] { (byte) 56, (byte) 65}; 
                int written = blob.setBytes (3, expected);
                rs2_.updateBlob("C_VARBINARY", blob);            //@C2A
                rs2_.updateRow();                                //@C2A
                rs2_.close();
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute(4);                                                           
                Blob blob1 = rs2_.getBlob ("C_VARBINARY");                                  
                byte[] b = blob1.getBytes (3, 2);                                           
                assertCondition(areEqual (b, expected) && written == 2);                    
                rs2_.close(); 
            }
            catch (Exception e) {                                                       
                failed (e, "Unexpected Exception");                                     
            }
        }
    }

    /**
    setBytes(long, byte[]) - Should work to set all of a non-empty lob.
    **/
    public void Var053()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {                                                
            try {                                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute (4);                                                          
                Blob blob = rs2_.getBlob ("C_VARBINARY");                                   
                byte[] expected = new byte[] { (byte) 57, (byte) 58, (byte) 98};           
                int written = blob.setBytes (1, expected);
                rs2_.updateBlob("C_VARBINARY", blob);            //@C2A
                rs2_.updateRow();                                //@C2A
                rs2_.close ();                                                              
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute(4);                                                           
                Blob blob1 = rs2_.getBlob ("C_VARBINARY");                                  
                byte[] b = blob1.getBytes (1, 3);                                           
                assertCondition(areEqual (b, expected) && written == 3);  //@C2C                  
                rs2_.close();                                                               
            }
            catch (Exception e) {                                                           
                failed (e, "Unexpected Exception");                                         
            }
        }
    }



    /**
    setBytes(long, byte[]) - Should work to set the last part of a non-empty lob.
    **/
    public void Var054()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {                                                
            try {                                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute (4);                                                          
                Blob blob = rs2_.getBlob ("C_VARBINARY");                                   
                byte[] expected = new byte[] { (byte) 578};                                
                int written = blob.setBytes (3, expected); 
                rs2_.updateBlob("C_VARBINARY", blob);            //@C2A
                rs2_.updateRow();                                //@C2A
                rs2_.close ();                                                              
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute(4);                                                           
                Blob blob1 = rs2_.getBlob("C_VARBINARY");                                   
                byte[] b = blob1.getBytes (3, 1);                                           
                assertCondition( areEqual (b, expected) && written == 1);   //@C2C                  
                rs2_.close();                                                               
            }
            catch (Exception e) {                                                           
                failed (e, "Unexpected Exception");                                         
            }
        }
    }




    /**
    setBytes(long, byte[]) - Should work to set the middle part of a non-empty lob.
    **/
    public void Var055()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {                                                
            try {                                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute (4);                                                          
                Blob blob = rs2_.getBlob ("C_VARBINARY");                                   
                byte[] expected = new byte[] { (byte) 578};                                
                int written = blob.setBytes (2, expected); 
                rs2_.updateBlob("C_VARBINARY", blob);            //@C2A
                rs2_.updateRow();                                //@C2A
                rs2_.close ();                                                              
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute(4);                                                           
                Blob blob1 = rs2_.getBlob("C_VARBINARY");                                   
                byte[] b = blob1.getBytes (2, 1);                                           
                assertCondition( areEqual (b, expected) && written == 1);  //@C2C                   
                rs2_.close();                                                               
            }
            catch (Exception e) {                                                           
                failed (e, "Unexpected Exception");                                         
            }
        }
    }  


    /**
    setBytes(long, byte[]) - Should work to set the first part of a non-empty lob.
    **/
    public void Var056()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {                                                
            try {                                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute (4);                                                          
                Blob blob = rs2_.getBlob ("C_VARBINARY");                                   
                byte[] expected = new byte[] { (byte) 578};                                
                int written = blob.setBytes (1, expected);
                rs2_.updateBlob("C_VARBINARY", blob);            //@C2A
                rs2_.updateRow();                                //@C2A
                rs2_.close ();                                                              
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute(4);                                                           
                Blob blob1 = rs2_.getBlob("C_VARBINARY");                                   
                byte[] b = blob1.getBytes (1, 1);                                           
                assertCondition( areEqual (b, expected) && written == 1);    //@C2C                 
                rs2_.close();                                                               
            }
            catch (Exception e) {                                                           
                failed (e, "Unexpected Exception");                                         
            }
        }
    }





    /**
    setBytes(long, byte[], int, int) - Should throw an exception when start is less
    than 0.
    **/
    public void Var057()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {                                                
            try {                                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute (4);                                                          
                Blob blob = rs2_.getBlob ("C_VARBINARY");                                   
                //blob.setBytes (-1, new byte[] { (byte) -4}, 1, 1); 
                blob.setBytes (-1, new byte[] { (byte) -4}, 0, 1);//@C3  Arrays are indexed from 0.		
                failed ("Didn't throw SQLException");                                       
                rs2_.close();                                                               
            }
            catch (Exception e) {                                                           
                try {                                                                       
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");               
                    rs2_.close();                                                           
                }
                catch (Exception s) {                                                       
                    failed (s, "Unexpected Exception");                                     
                }
            }
        }
    }                                                                                       

    /**
    setBytes(long, byte[], int, int) - Should throw an exception when start is 0.
    **/
    public void Var058()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {                                                
            try {                                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute (4);                                                          
                Blob blob = rs2_.getBlob ("C_VARBINARY");                               
                blob.setBytes (0, new byte[] { (byte) -9, (byte) 3}, 0, 2); //@C3 offset changed                                                                   
                failed ("Didn't throw SQLException");                                       
                rs2_.close();                                                               
            }
            catch (Exception e) {                                                           
                try {                                                                       
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");               
                    rs2_.close();                                                           
                }
                catch (Exception s) {                                                       
                    failed (s, "Unexpected Exception");                                     
                }
            }
        }
    }



    /**
    setBytes(long, byte[], int, int) - Should throw an exception when start is greater
    than the length of the lob.
    @CRS - This variation should succeed. Only when you try to write past the
    max column size will it be an exception.
    **/
    public void Var059()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {
	    { 
		try {                                                                           
		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2_.absolute (4);                                                          
		    Blob blob = rs2_.getBlob ("C_VARBINARY");
		    blob.setBytes (blob.length()+1, new byte[] { (byte) 44}, 0, 1); //@C3 offset changed //@C2C length changed
//                failed ("Didn't throw SQLException");                                       
		    rs2_.close();                                                               
		    succeeded();
		}
		catch (Exception e) {                                                           
//                try {                                                                       
//                   assertExceptionIsInstanceOf (e, "java.sql.SQLException");               
//                    rs2_.close();                                                           
//                }
//                catch (Exception s) {                                                       
		    failed (e, "Unexpected Exception");                                     
//                }
		}
	    }
        }
    }


    //@C2C Changed testcase to not expect an exception if the writing of the bytes will extend
    //@C2C past the end of the lob.
    /**
    setBytes(long, byte[], int, int) - Should not throw an exception when length of bytes to write 
    extends past the length lob as long as start is not passed the end of the lob.
    **/
    public void Var060()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {                                                
            try {                                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute (4);                        
                byte[] expected = new byte[] { (byte) 56, (byte) 65, (byte) 76, (byte) 89};
                Blob blob = rs2_.getBlob ("C_VARBINARY");                                   
                int written = blob.setBytes (1, expected, 0, 4); //@C3 offset changed
                rs2_.updateBlob("C_VARBINARY", blob);            //@C2A
                rs2_.updateRow();                                //@C2A
                rs2_.close();                                                               
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute(4);                                                           
                Blob blob1 = rs2_.getBlob("C_VARBINARY");                                   
                byte[] b = blob1.getBytes (1, 4); 
                                                          
                assertCondition( areEqual (b, expected) && written == 4);   //@C2C
                var060Called_ = true;
                rs2_.close(); 
            }
            catch (Exception e) {                                                           
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    setBytes(long, byte[], int, int) - Should work to set all of a non-empty lob.
    **/
    public void Var061()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {                                                
            try {                                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute (4);                                                          
                Blob blob = rs2_.getBlob ("C_VARBINARY");                                   
                byte[] expected = new byte[] { (byte) 57, (byte) 58, (byte) 98};           
                int written = blob.setBytes (1, expected, 0, 3); //@C3 offset changed
                rs2_.updateBlob("C_VARBINARY", blob);            //@C2A
                rs2_.updateRow();                                //@C2A
                rs2_.close ();                                                              
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute(4);                                                           
                Blob blob1 = rs2_.getBlob("C_VARBINARY");                                   
                byte[] b = blob1.getBytes (1, 3);                                           
                assertCondition( areEqual (b, expected) && written == 3);    //@C2C                
                rs2_.close();                                                               
            }
            catch (Exception e) {                                                           
                failed (e, "Unexpected Exception");                                         
            }
        }
    }


    /**
    setBytes(long, byte[], int, int) - Should work to set the last part of a non-empty lob.
    **/
    public void Var062()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {                                                
            try {                                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute (4);                                                          
                Blob blob = rs2_.getBlob ("C_VARBINARY");                                   
                byte[] expected = new byte[] { (byte) 578};                                
                int written = blob.setBytes (3, expected, 0, 1); //@C3 offset changed
                rs2_.updateBlob("C_VARBINARY", blob);            //@C2A
                rs2_.updateRow();                                //@C2A
                rs2_.close ();                                                              
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute(4);                                                           
                Blob blob1 = rs2_.getBlob("C_VARBINARY");                                   
                byte[] b = blob1.getBytes (3, 1);                                           
                assertCondition( areEqual (b, expected) && written == 1);    //@C2C                
                rs2_.close();                                                               
            }
            catch (Exception e) {                                                           
                failed (e, "Unexpected Exception");                                         
            }
        }
    }



    /**
    setBytes(long, byte[], int, int) - Should work to set the middle part of a non-empty lob.
    **/
    public void Var063()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {                                                
            try {                                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute (4);                                                          
                Blob blob = rs2_.getBlob ("C_VARBINARY");                                   
                byte[] expected = new byte[] { (byte) 578};                                
                int written = blob.setBytes (2, expected, 0, 1); //@C3 offset changed
                rs2_.updateBlob("C_VARBINARY", blob);            //@C2A
                rs2_.updateRow();                                //@C2A
                rs2_.close ();                                                              
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute(4);                                                           
                Blob blob1 = rs2_.getBlob("C_VARBINARY");                                   
                byte[] b = blob1.getBytes (2, 1);                                           
                assertCondition( areEqual (b, expected) && written == 1);  //@C2C                   
                rs2_.close();                                                               
            }
            catch (Exception e) {                                                           
                failed (e, "Unexpected Exception");                                         
            }
        }
    }  


    /**
    setBytes(long, byte[], int, int) - Should work to set the first part of a non-empty lob.
    **/
    public void Var064()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {                                                
            try {                                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute (4);                                                          
                Blob blob = rs2_.getBlob ("C_VARBINARY");
                                                   
                byte[] expected = new byte[] { (byte) 578};                                
                int written = blob.setBytes (1, expected, 0, 1); //@C3 offset changed
                rs2_.updateBlob("C_VARBINARY", blob);            //@C2A
                rs2_.updateRow();                                //@C2A
                rs2_.close ();                                                              
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute(4);                                                           
                Blob blob1 = rs2_.getBlob("C_VARBINARY");                                   
                byte[] b = blob1.getBytes (1, 1);
                                                           
                assertCondition( areEqual (b, expected) && written == 1);   //@C2C                  
                rs2_.close();                                                               
            }
            catch (Exception e) {                                                           
                failed (e, "Unexpected Exception");                                         
            }
        }
    }



    /**
    setBinaryStream() - When the lob is not empty.
    **/
    public void Var065()                                                        
    {                                                                           
        if (checkUpdateableLobsSupport ()) {                                    
            try {                                                               
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);        
                rs2_.absolute (4);                                              
                Blob blob = rs2_.getBlob ("C_VARBINARY");                       
                OutputStream v = blob.setBinaryStream (1);                      
                byte [] expected = null;
                if (var060Called_)
                {
                  // Var060 changes our LOB data.
                  expected = new byte[] {(byte) 6, (byte) 7, (byte) 8, (byte) 9};
                }
                else
                {
                  expected = new byte[] {(byte) 6, (byte) 7, (byte) 8};
                }
                v.write(expected);                                           
                v.close();                                                      
                rs2_.updateBlob("C_VARBINARY", blob);                           
                rs2_.updateRow();                                               
                rs2_.close();                                                   
                rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);      
                rs2_.absolute(4);                                               
                Blob blob1 = rs2_.getBlob("C_VARBINARY");                       
                InputStream inputStream = blob1.getBinaryStream();
                sb.setLength(0); 
		if(getDriver() == JDTestDriver.DRIVER_NATIVE &&				// @D4
		   getRelease() >= JDTestDriver.RELEASE_V5R3M0)				// @D4
		    assertCondition( compareBeginsWithBytes( inputStream, expected,sb));	// @D4
		else									// @D4
		    assertCondition (compare (inputStream, expected,sb)); 
            }
            catch (Exception e) {                                               
                failed (e, "Unexpected Exception");                             
            }
        }
    }                                                                           

    /**
    setBinaryStream() - Should throw an exception if you try to set a stream
    that starts after the lob.
    @CRS - This variation should succeed. Only when you try to write past the
    max column size will it be an exception.
    **/
    public void Var066()                                                        
    {                                                                           
        if (checkUpdateableLobsSupport ())
        {
	    { 
		try
		{                                                                                                                                    
		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);        
		    rs2_.absolute(4);                                               
		    Blob blob = rs2_.getBlob ("C_VARBINARY");                       
		    OutputStream v = blob.setBinaryStream (blob.length()+1);                      
//                failed("Didn't throw SQLException");                            
		    rs2_.close();  
                    assertCondition(v != null); 
		}
		catch (Exception e) {                                               
//                try {                                                           
//                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");   
//                    rs2_.close();                                               
//                }
//                catch (Exception s) {                                           
		    failed (e, "Unexpected Exception");                         
//                }
		}
	    }
        }
    }                                                                           

    /**
    setBinaryStream() - Should throw an exception if you try to set a stream
    that starts at 0.
    **/
    public void Var067()                                                        
    {                                                                           
        if (checkUpdateableLobsSupport ())
        {                                                                       
            try
            {                                                                                                                                    
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);        
                rs2_.absolute(4);                                               
                Blob blob = rs2_.getBlob ("C_VARBINARY");                       
                OutputStream v = blob.setBinaryStream (0);                      
                failed("Didn't throw SQLException"+v);                            
                rs2_.close();                                                   
            }
            catch (Exception e) {                                               
                try {                                                           
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");   
                    rs2_.close();                                               
                }
                catch (Exception s) {                                           
                    failed (s, "Unexpected Exception");                         
                }
            }
        }
    }                                                                           

    /**
    setBinaryStream() - Should throw an exception if you try to set a stream
    that starts before 0.
    **/
    public void Var068()                                                        
    {                                                                           
        if (checkUpdateableLobsSupport ())
        {                                                                       
            try
            {                                                                                                                                    
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);        
                rs2_.absolute(4);                                               
                Blob blob = rs2_.getBlob ("C_VARBINARY");                       
                OutputStream v = blob.setBinaryStream (-1);                     
                failed("Didn't throw SQLException"+v);                            
                rs2_.close();                                                   
            }
            catch (Exception e) {                                               
                try {                                                           
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");   
                    rs2_.close();                                               
                }
                catch (Exception s) {                                           
                    failed (s, "Unexpected Exception");                         
                }
            }
        }
    }                                                                           

    /**
    truncate() - Should throw an exception if you try to truncate to
    a length less than 0.
    **/
    public void Var069()                                                        
    {                                                                           
        if (checkUpdateableLobsSupport ())
        {                                                                       
            try
            {                                                                                                                                    
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);        
                rs2_.absolute(4);                                               
                Blob blob = rs2_.getBlob ("C_VARBINARY");                       
                blob.truncate (-1);                                             
                failed("Didn't throw SQLException");                            
                rs2_.close();                                                   
            }
            catch (Exception e) {                                               
                try {                                                           
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");   
                    rs2_.close();                                               
                }
                catch (Exception s) {                                           
                    failed (s, "Unexpected Exception");                         
                }
            }
        }
    }                                                                           

    /**
    truncate() - Should throw an exception if you try to truncate to
    a length greater than the length of the lob.
    @CRS - This variation should succeed. Only when you try to write past the
    max column size will it be an exception.
    **/
    public void Var070()                                                        
    {                                                                           
        if (checkUpdateableLobsSupport ())
        {
	    if (v5r2nativeFunctions_) {
		try
		{                                                                                                                                    
		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);        
		    rs2_.absolute(4);
		    Blob blob = rs2_.getBlob ("C_VARBINARY");                       
		    blob.truncate (5);                                              
		    failed("Didn't throw SQLException");                            
		    rs2_.close();                                                   
		}
		catch (Exception e) {                                               
		    try {                                                           
			assertExceptionIsInstanceOf (e, "java.sql.SQLException");   
			rs2_.close();                                               
		    }
		    catch (Exception s) {                                           
			failed (s, "Unexpected Exception");                         
		    }
		}

	    } else { 
		try
		{                                                                                                                                    
		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);        
		    rs2_.absolute(4);                                               
		    Blob blob = rs2_.getBlob ("C_VARBINARY");                       
		    blob.truncate (5);                                              
//                failed("Didn't throw SQLException");                            
		    rs2_.close();                                                   
		    succeeded();
		}
		catch (Exception e) {                                               
//                try {                                                           
//                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");   
//                    rs2_.close();                                               
//                }
//                catch (Exception s) {                                           
		    failed (e, "Unexpected Exception");                         
//                }
		}
	    }
        }
    }                                                                           

    /**
    truncate() - Should work on a non-empty lob.
    **/
    public void Var071()                                                        
    {                                                                           
        if (checkUpdateableLobsSupport ())
        {                                                                       
            try
            {                                                                                                                                    
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);        
                rs2_.absolute(4);                                               
                Blob blob = rs2_.getBlob ("C_VARBINARY");                       
                blob.truncate (2); 
                rs2_.updateBlob("C_VARBINARY", blob);            //@C2A
                rs2_.updateRow();                                //@C2A
                rs2_.close ();                                                  
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);        
                rs2_.absolute(4);                                               
                Blob blob1 = rs2_.getBlob("C_VARBINARY");                       
                assertCondition (blob1.length() == 2);                          
                rs2_.close();                                                   
            }
            catch (Exception e)
            {                                                                   
                failed(e, "Unexpected Exception");                              
            }
        }
    }    


    /**
    setBinaryStream() - When the lob is not empty to position 2.
    **/
    public void Var072()                                                        
    {                                                                           
        if (checkUpdateableLobsSupport ()) {                                    
            try {                                                               
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);        
                rs2_.absolute (4);                                              
                Blob blob = rs2_.getBlob ("C_VARBINARY");                       
                OutputStream v = blob.setBinaryStream (2);                      
                byte [] insert = new byte[] {(byte) 10, (byte) 11, (byte) 12};
                v.write(insert);                                           
                v.close();                                                      
                rs2_.updateBlob("C_VARBINARY", blob);                           
                rs2_.updateRow();                                               
                rs2_.close();                                                   
                rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);      
                rs2_.absolute(4);                                               
                Blob blob1 = rs2_.getBlob("C_VARBINARY"); 
                // Var 65 should have left a (byte) 6 in the first position.
                byte[] expected = new byte[] {(byte) 6, (byte) 10, (byte) 11, (byte) 12};                InputStream inputStream = blob1.getBinaryStream();	
                sb.setLength(0); 
		if(getDriver() == JDTestDriver.DRIVER_NATIVE &&				// @D4
		   getRelease() >= JDTestDriver.RELEASE_V5R3M0)				// @D4
		    assertCondition( compareBeginsWithBytes( inputStream, expected,sb),sb);	// @D4
		else									// @D4
		    assertCondition (compare (inputStream, expected,sb),sb); 
	    }
            catch (Exception e) {                                               
                failed(e, "Unexpected Exception");                              
            }
        }
    }                                                                           

    //@K1A
    /**
    setBytes(long, byte[]) - Should now throw an exception when length of bytes to write extends
    past the length of the lob as long as start is not passed the end of the lob.
    **/
    public void Var073()                                                                    
    {
	if (v5r2nativeFunctions_) {
	    notApplicable(); 
	} else { 
	    if (checkUpdateableLobsSupport ()) {                                                
		try {                                                                           
		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2_.absolute (3);                                                          
		    Blob blob = rs2_.getBlob ("C_VARBINARY");
		    long length1 = blob.length();
		    byte[] expected = new byte[] { (byte) 56, (byte) 65}; 
		    int written = blob.setBytes (3, expected);
		    rs2_.updateBlob("C_VARBINARY", blob);            
		    rs2_.updateRow();                                
		    rs2_.close();
		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2_.absolute(3);                                                           
		    Blob blob1 = rs2_.getBlob ("C_VARBINARY");                                  
		    long length2 = blob1.length();
		    assertCondition( written > 0 && length1 == length2, "length1 = "+length1+ " length2 = "+length2);
		    rs2_.close(); 
		}
		catch (Exception e) {                                                       
		    failed (e, "Unexpected Exception");                                     
		}
	    }
	}
    }

    //@K1A
    /**
    setBytes(long, byte[]) - Should work to set all of a non-empty lob.
    **/
    public void Var074()                                                                    
    {
	if (v5r2nativeFunctions_) {
	    notApplicable(); 
	} else { 

	    if (checkUpdateableLobsSupport ()) {                                                
		try {                                                                           
		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2_.absolute (3);                                                          
		    Blob blob = rs2_.getBlob ("C_VARBINARY");                                   
		    long length1 = blob.length();
		    byte[] expected = new byte[] { (byte) 57, (byte) 58, (byte) 98};           
		    int written = blob.setBytes (1, expected);
		    rs2_.updateBlob("C_VARBINARY", blob);            
		    rs2_.updateRow();                                
		    rs2_.close ();                                                              
		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2_.absolute(3);                                                           
		    Blob blob1 = rs2_.getBlob ("C_VARBINARY");                                  
		    long length2 = blob1.length();
		    assertCondition( written > 0 && length1 == length2, "length1 = "+length1+ " length2 = "+length2);
		    rs2_.close();                                                               
		}
		catch (Exception e) {                                                           
		    failed (e, "Unexpected Exception");                                         
		}
	    }
	}
    }


    //@K1A
    /**
    setBytes(long, byte[]) - Should work to set the last part of a non-empty lob.
    **/
    public void Var075()                                                                    
    {
	if (v5r2nativeFunctions_) {
	    notApplicable(); 
	} else { 

	    if (checkUpdateableLobsSupport ()) {                                                
		try {                                                                           
		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2_.absolute (3);                                                          
		    Blob blob = rs2_.getBlob ("C_VARBINARY");                                   
		    long length1 = blob.length();
		    byte[] expected = new byte[] { (byte) 578};                                
		    int written = blob.setBytes (3, expected); 
		    rs2_.updateBlob("C_VARBINARY", blob);           
		    rs2_.updateRow();                                
		    rs2_.close ();                                                              
		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2_.absolute(3);                                                           
		    Blob blob1 = rs2_.getBlob("C_VARBINARY");                                   
		    long length2 = blob1.length();
		    assertCondition( written > 0 && length1 == length2 );   
		    rs2_.close();                                                               
		}
		catch (Exception e) {                                                           
		    failed (e, "Unexpected Exception");                                         
		}
	    }
	}
    }


    //@K1A
    /**
    setBytes(long, byte[]) - Should work to set the middle part of a non-empty lob.
    **/
    public void Var076()                                                                    
    {
	if (v5r2nativeFunctions_) {
	    notApplicable(); 
	} else { 

	    if (checkUpdateableLobsSupport ()) {                                                
		try {                                                                           
		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2_.absolute (2);                                                          
		    Blob blob = rs2_.getBlob ("C_VARBINARY");                                   
		    long length1 = blob.length();
		    byte[] expected = new byte[] { (byte) 578};                                
		    int written = blob.setBytes (2, expected); 
		    rs2_.updateBlob("C_VARBINARY", blob);          
		    rs2_.updateRow();                              
		    rs2_.close ();                                                              
		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2_.absolute(2);                                                           
		    Blob blob1 = rs2_.getBlob("C_VARBINARY");      
		    long length2 = blob1.length();
		    assertCondition( written > 0 && length1 == length2, "length1 = "+length1+ " length2 = "+length2);     
		    rs2_.close();                                                               
		}
		catch (Exception e) {                                                           
		    failed (e, "Unexpected Exception");                                         
		}
	    }
	}
    }  

    //@k1A
    /**
    setBytes(long, byte[]) - Should work to set the first part of a non-empty lob.
    **/
    public void Var077()                                                                    
    {
	if (v5r2nativeFunctions_) {
	    notApplicable(); 
	} else { 

	    if (checkUpdateableLobsSupport ()) {                                                
		try {                                                                           
		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2_.absolute (2);                                                          
		    Blob blob = rs2_.getBlob ("C_VARBINARY");                                   
		    long length1 = blob.length();
		    byte[] expected = new byte[] { (byte) 578};                                
		    int written = blob.setBytes (1, expected);
		    rs2_.updateBlob("C_VARBINARY", blob);            
		    rs2_.updateRow();                                
		    rs2_.close ();                                                              
		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2_.absolute(2);                                                           
		    Blob blob1 = rs2_.getBlob("C_VARBINARY");                                   
		    long length2 = blob1.length();
		    assertCondition( (written > 0) && length1 == length2, "length1 = "+length1+ " length2 = "+length2);    
		    rs2_.close();                                                               
		}
		catch (Exception e) {                                                           
		    failed (e, "Unexpected Exception");                                         
		}
	    }
	}
    }
    //@K1A
    /**
    setBytes(long, byte[], int, int) - Should work to set all of a non-empty lob.
    **/
    public void Var078()                                                                    
    {
	if (v5r2nativeFunctions_) {
	    notApplicable("Known native problem in V5R2"); 
	} else { 

	    if (checkUpdateableLobsSupport ()) {                                                
		try {                                                                           
		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2_.absolute (3);                                                          
		    Blob blob = rs2_.getBlob ("C_VARBINARY");                                   
		    long length1 = blob.length();
		    byte[] expected = new byte[] { (byte) 57, (byte) 58, (byte) 98};           
		    int written = blob.setBytes (1, expected, 0, 3); 
		    rs2_.updateBlob("C_VARBINARY", blob);            
		    rs2_.updateRow();                                
		    rs2_.close ();                                                              
		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2_.absolute(3);                                                           
		    Blob blob1 = rs2_.getBlob("C_VARBINARY");                                   
		    long length2 = blob1.length();
		    assertCondition( (written > 0) && length1 == length2 );
		    rs2_.close();                                                               
		}
		catch (Exception e) {                                                           
		    failed (e, "Unexpected Exception");                                         
		}
	    }
	}
    }

    //@k1A
    /**
    setBytes(long, byte[], int, int) - Should work to set the last part of a non-empty lob.
    **/
    public void Var079()                                                                    
    {
	if (v5r2nativeFunctions_) {
	    notApplicable(); 
	} else { 

	    if (checkUpdateableLobsSupport ()) {                                                
		try {                                                                           
		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2_.absolute (2);                                                          
		    Blob blob = rs2_.getBlob ("C_VARBINARY");                                   
		    long length1 = blob.length();
		    byte[] expected = new byte[] { (byte) 578};                                
		    int written = blob.setBytes (3, expected, 0, 1); 
		    rs2_.updateBlob("C_VARBINARY", blob);            
		    rs2_.updateRow();                                
		    rs2_.close ();                                                              
		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2_.absolute(2);                                                           
		    Blob blob1 = rs2_.getBlob("C_VARBINARY");        
		    long length2 = blob1.length();
		    assertCondition( (written > 0) && length1 == length2 );    
		    rs2_.close();                                                               
		}
		catch (Exception e) {                                                           
		    failed (e, "Unexpected Exception");                                         
		}
	    }
	}
    }


    //@K1A
    /**
    setBytes(long, byte[], int, int) - Should work to set the middle part of a non-empty lob.
    **/
    public void Var080()                                                                    
    {                                                                                       
	if (v5r2nativeFunctions_) {
	    notApplicable(); 
	} else { 
	    if (checkUpdateableLobsSupport ()) {                                                
		try {                                                                           
		    ResultSet rs2=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2.absolute (2);                                                          
		    Blob blob = rs2.getBlob ("C_VARBINARY");                                   
		    long length1 = blob.length();
		    byte[] expected = new byte[] { (byte) 578};                                
		    int written = blob.setBytes (2, expected, 0, 1); 
		    rs2.updateBlob("C_VARBINARY", blob);            
		    rs2.updateRow();                                
		    rs2.close ();                                                              
		    rs2=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2.absolute(2);                                                           
		    Blob blob1 = rs2.getBlob("C_VARBINARY");       
		    long length2 = blob1.length();
		    assertCondition( (written > 0) && length1 == length2, "length1 = "+length1+ " length2 = "+length2);           
		    rs2.close();                                                               
		}
		catch (Exception e) {                                                           
		    failed (e, "Unexpected Exception");                                         
		}
	    }
	}  
    }

    //@k1A
    /**
    setBytes(long, byte[], int, int) - Should work to set the first part of a non-empty lob.
    **/
    public void Var081()                                                                    
    {                                                                                       
	if (v5r2nativeFunctions_) {
	    notApplicable(); 
	} else { 
	    if (checkUpdateableLobsSupport ()) {                                                
		try {                                                                           
		    ResultSet rs2=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2.absolute (3);                                                          
		    Blob blob = rs2.getBlob ("C_VARBINARY");
		    long length1 = blob.length();                                   
		    byte[] expected = new byte[] { (byte) 578};                                
		    int written = blob.setBytes (1, expected, 0, 1); 
		    rs2.updateBlob("C_VARBINARY", blob);            
		    rs2.updateRow();                               
		    rs2.close ();                                                              
		    rs2=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2.absolute(3);                                                           
		    Blob blob1 = rs2.getBlob("C_VARBINARY");                                   
		    long length2 = blob1.length();                                           
		    assertCondition( (written > 0) && length1 == length2, "length1 = "+length1+ " length2 = "+length2);           
		    rs2.close();                                                               
		}
		catch (Exception e) {                                                           
		    failed (e, "Unexpected Exception");                                         
		}
	    }
	}
    }
    //@k1A
    /**
    truncate() - Should work on a non-empty lob.
    **/
    public void Var082()                                                        
    {                                                                           
	if (v5r2nativeFunctions_) {
	    notApplicable(); 
	} else { 
	    if (checkUpdateableLobsSupport ())
	    {                                                                       
		try
		{                                                                                                                                    
		    ResultSet rs2=statement2_.executeQuery("SELECT * FROM " + TABLE_);        
		    rs2.absolute(2);                                               
		    Blob blob = rs2.getBlob ("C_VARBINARY");                       
		    long length1 = blob.length();
		    blob.truncate (2); 
		    rs2.updateBlob("C_VARBINARY", blob);           
		    rs2.updateRow();                                
		    rs2.close ();                                                  
		    rs2=statement2_.executeQuery("SELECT * FROM " + TABLE_);        
		    rs2.absolute(2);                                               
		    Blob blob1 = rs2.getBlob("C_VARBINARY");                       
		    assertCondition (length1 == 50 && blob1.length() == 2, "length1 = "+length1+ " SB 50 AND blob1.length() = "+blob1.length()+" SB 2");                          
		    rs2.close();                                                   
		}
		catch (Exception e)
		{                                                                   
		    failed(e, "Unexpected Exception");                              
		}
	    }
	}
    }
 //@D1
    /**
    setBytes(long, byte[], int, int) - Using offset other that 0,
    **/
    public void Var083()                                                                    
    {
	byte[] expected1 = {(byte) -8, (byte) -10, (byte) -12, (byte) -14, (byte) -16};
	byte[] expected2 = {(byte) 15, (byte)  17, (byte) -12, (byte) -14, (byte) -16};

	if (v5r2nativeFunctions_) {
	    notApplicable(); 
	} else { 
	    if (checkUpdateableLobsSupport ()) {                                                
		try {                                                                           
		    ResultSet rs2=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2.absolute (5);                                                          
		    Blob blob = rs2.getBlob ("C_VARBINARY");
		    byte[] b1 = blob.getBytes(5, 5);                                   
		    byte[] expected = new byte[] { (byte) 11, (byte) 13, (byte) 15, (byte) 17, (byte) 19};
		    int written = blob.setBytes (5, expected, 2, 2); 
		    rs2.updateBlob("C_VARBINARY", blob);            
		    rs2.updateRow();                               
		    rs2.close ();                                                              
		    rs2=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2.absolute(5);                                                           
		    Blob blob1 = rs2.getBlob("C_VARBINARY");                                   
		    byte[] b2 = blob1.getBytes(5, 5);                                           
		    assertCondition(areEqual(b1, expected1) && areEqual(b2, expected2) && written == 2);
		    rs2.close();                                                               
		}
		catch (Exception e) {                                                           
		    failed (e, "Unexpected Exception");                                         
		}
	    }
	}
    }
//@D1
    /**
    truncate() - Should work on a non-empty lob.
    **/
    public void Var084()                                                        
    {                                                                           
	if (v5r2nativeFunctions_) {
	    notApplicable(); 
	} else { 
	    if (checkUpdateableLobsSupport ())
	    {                                                                       
		try
		{
		    ResultSet rs2=statement2_.executeQuery("SELECT * FROM " + TABLE_);        
		    rs2.absolute(5);                                               
		    Blob blob = rs2.getBlob ("C_VARBINARY");                       
		    long length1 = blob.length();
		    blob.truncate (0); 
		    rs2.updateBlob("C_VARBINARY", blob);           
		    rs2.updateRow();                                
		    rs2.close ();                                                  
		    rs2=statement2_.executeQuery("SELECT * FROM " + TABLE_);        
		    rs2.absolute(5);                                               
		    Blob blob1 = rs2.getBlob("C_VARBINARY");                       
		    assertCondition (length1 == 50 && blob1.length() == 0, "length1 = "+length1+ " SB 50 AND blob1.length() = "+blob1.length()+" SB 2");                          
		    rs2.close();                                                   
		}
		catch (Exception e)
		{                                                                   
		    failed(e, "Unexpected Exception");                              
		}
	    }
	}
    }
    /**
    setBytes(long, byte[]) - Prepadding works ??
    **/
    public void Var085()                                                                    
    {
	if (v5r2nativeFunctions_) {
	    notApplicable(); 
	} else { 

	    if (checkUpdateableLobsSupport ()) {                                                
		try {
		// Pre-cleaning
		    ResultSet rs2=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2.absolute (4);                                                          
		    Blob blob = rs2.getBlob ("C_VARBINARY");
		    blob.truncate(2);
		    rs2.updateBlob("C_VARBINARY", blob);            
		    rs2.updateRow();                                
		    rs2.close ();   

		    rs2=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2.absolute (4);                                                          
		    blob = rs2.getBlob ("C_VARBINARY");
		    byte[] expected = new byte[] { (byte) 57, (byte) 58, (byte) 98};           
		    int written = blob.setBytes (5, expected); 
		    rs2.updateBlob("C_VARBINARY", blob);            
		    rs2.updateRow();                                
		    rs2.close ();                                                              
		    rs2=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2.absolute(4);                                                           
		    Blob blob1 = rs2.getBlob("C_VARBINARY");                                   
		    byte[] b = blob1.getBytes (5, 3);                                           
		    assertCondition( areEqual (b, expected) && written == 3
				     && blob1.length() == 7);  
		    rs2.close();                                                               
		}
		catch (Exception e) {                                                           
		    failed (e, "Unexpected Exception");                                         
		}
	    }
	}
    }
/**
    setBytes(long, byte[]) - Incoming Data Truncation
    **/
    public void Var086()                                                                    
    {
	if (v5r2nativeFunctions_) {
	    notApplicable(); 
	} else { 

	    if (checkUpdateableLobsSupport ()) {                                                
		try {

		    ResultSet rs2=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2.absolute (4);                                                          
		    Blob blob = rs2.getBlob ("C_VARBINARY");

		    byte[] expected = new byte[30005];
		    for(int i=0; i<expected.length; i++)
			expected[i] = (byte)i;

		    int written = blob.setBytes (1, expected); 
		    rs2.updateBlob("C_VARBINARY", blob);            
		    if(getDriver() == JDTestDriver.DRIVER_TOOLBOX)
			failed("Didn't throw Exception.");
		    rs2.updateRow();                                
		    rs2.close ();                                                              
		    rs2=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2.absolute(4);                                                           
		    Blob blob1 = rs2.getBlob("C_VARBINARY");                                   
		    assertCondition( written == 30000 && blob1.length() == 30000);  
		    rs2.close();                                                               
		}
		catch (Exception e) {                                                           
		    if(getDriver() == JDTestDriver.DRIVER_TOOLBOX)
			assertExceptionIsInstanceOf(e, "java.sql.DataTruncation");
		    else
			failed (e, "Unexpected Exception");                                         
		}
	    }
	}
    }


    
    /**
     free() length() -- Make sure length throws an exception after free
     **/
    public void Var087()
    {
      if (checkJdbc40 ()) {
        try {
          rs_.absolute (2);
          Blob blob = rs_.getBlob ("C_VARBINARY");
          JDReflectionUtil.callMethod_V(blob, "free");
          try {
            blob.length(); 
            failed ("Didn't throw SQLException");
          }
          catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
          }
        }
        catch (Exception e)
        {                                                                   
          failed(e, "Unexpected Exception");                              
        }
      }
    }
    
    /**
     free() getbytes() -- Make sure method throws an exception after free
     **/
    public void Var088()
    {
      if (checkJdbc40 ()) {
        try {
          rs_.absolute (2);
          Blob blob = rs_.getBlob ("C_VARBINARY");
          JDReflectionUtil.callMethod_V(blob, "free");
          try {
            blob.getBytes(1,1); 
            failed ("Didn't throw SQLException");
          }
          catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
          }
        }
        catch (Exception e)
        {                                                                   
          failed(e, "Unexpected Exception");                              
        }
      }
    }
    
    /**
     free() getBinaryStream() -- Make sure method throws an exception after free
     **/
    public void Var089()
    {
      if (checkJdbc40 ()) {
        try {
          rs_.absolute (2);
          Blob blob = rs_.getBlob ("C_VARBINARY");
          JDReflectionUtil.callMethod_V(blob, "free");
          try {
            blob.getBinaryStream(); 
            failed ("Didn't throw SQLException");
          }
          catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
          }
        }
        catch (Exception e)
        {                                                                   
          failed(e, "Unexpected Exception");                              
        }
      }
    }
    
    
    /**
     free() position() -- Make sure method throws an exception after free
     **/
    public void Var090()
    {
      if (checkJdbc40 ()) {
        try {
          rs_.absolute (2);
          Blob blob = rs_.getBlob ("C_VARBINARY");
          Blob blob2 = rs_.getBlob ("C_VARBINARY");
          JDReflectionUtil.callMethod_V(blob, "free");
          try {
            blob.position(blob2, 1);  
            failed ("Didn't throw SQLException");
          }
          catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
          }
        }
        catch (Exception e)
        {                                                                   
          failed(e, "Unexpected Exception");                              
        }
      }
    }
    
    /**
     free() position() -- Make sure method throws an exception after free
     **/
    public void Var091()
    {
      if (checkJdbc40 ()) {
        try {
          rs_.absolute (2);
          Blob blob = rs_.getBlob ("C_VARBINARY");
          byte[] stuff = { 0x00 }; 
          JDReflectionUtil.callMethod_V(blob, "free");
          try {
            blob.position(stuff, 1);  
            failed ("Didn't throw SQLException");
          }
          catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
          }
        }
        catch (Exception e)
        {                                                                   
          failed(e, "Unexpected Exception");                              
        }
      }
    }
    
    /**
     free() setBytes() -- Make sure method throws an exception after free
     **/
    public void Var092()
    {
      if (checkJdbc40 ()) {
        try {
          rs_.absolute (2);
          Blob blob = rs_.getBlob ("C_VARBINARY");
          byte[] stuff = { 0x00 }; 
          JDReflectionUtil.callMethod_V(blob, "free");
          try {
            blob.setBytes(1, stuff); 
            failed ("Didn't throw SQLException");
          }
          catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
          }
        }
        catch (Exception e)
        {                                                                   
          failed(e, "Unexpected Exception");                              
        }
      }
    }
    
    
    /**
     free() setBytes() -- Make sure method throws an exception after free
     **/
    public void Var093()
    {
      if (checkJdbc40 ()) {
        try {
          rs_.absolute (2);
          Blob blob = rs_.getBlob ("C_VARBINARY");
          byte[] stuff = { 0x00 }; 
          JDReflectionUtil.callMethod_V(blob, "free");
          try {
            blob.setBytes(1, stuff, 1, 1); 
            failed ("Didn't throw SQLException");
          }
          catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
          }
        }
        catch (Exception e)
        {                                                                   
          failed(e, "Unexpected Exception");                              
        }
      }
    }
    
    /**
     free() setBinaryStream() -- Make sure method throws an exception after free
     **/
    public void Var094()
    {
      if (checkJdbc40 ()) {
        try {
          rs_.absolute (2);
          Blob blob = rs_.getBlob ("C_VARBINARY");
          JDReflectionUtil.callMethod_V(blob, "free");
          try {
            blob.setBinaryStream(1); 
            failed ("Didn't throw SQLException");
          }
          catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
          }
        }
        catch (Exception e)
        {                                                                   
          failed(e, "Unexpected Exception");                              
        }
      }
    }
    
    /**
     free() truncate() -- Make sure method throws an exception after free
     **/
    public void Var095()
    {
      if (checkJdbc40 ()) {
        try {
          rs_.absolute (2);
          Blob blob = rs_.getBlob ("C_VARBINARY");
          JDReflectionUtil.callMethod_V(blob, "free");
          try {
            blob.truncate(1); 
            failed ("Didn't throw SQLException");
          }
          catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
          }
        }
        catch (Exception e)
        {                                                                   
          failed(e, "Unexpected Exception");                              
        }
      }
    }
    
    
    /**
     free() getBinaryStream() -- Make sure method throws an exception after free
     **/
    public void Var096()
    {
      if (checkJdbc40 ()) {
        try {
          rs_.absolute (2);
          Blob blob = rs_.getBlob ("C_VARBINARY");
          JDReflectionUtil.callMethod_V(blob, "free");
          try {
            JDReflectionUtil.callMethod_O(blob, "getBinaryStream", (long) 1, (long) 1 ); 
            failed ("Didn't throw SQLException");
          }
          catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
          }
        }
        catch (Exception e)
        {                                                                   
          failed(e, "Unexpected Exception");                              
        }
      }
    }
    
    /**
     free() free() -- Shouldn't throw exception
     **/
    public void Var097()                                                        
    {                                                                           
      if (checkJdbc40()) { 
        
        try
        {
          rs_.absolute (2);
          Blob blob = rs_.getBlob ("C_VARBINARY");
          JDReflectionUtil.callMethod_V(blob, "free");
          JDReflectionUtil.callMethod_V(blob, "free");
          assertCondition (true); 
        }
        catch (Exception e)
        {                                                                   
          failed(e, "Unexpected Exception");                              
        }
      }
    }
    
    
    
}
