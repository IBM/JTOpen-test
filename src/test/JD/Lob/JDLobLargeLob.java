///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDLobLargeLob.java
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
import test.Testcase;
import test.JDLobTest.JDTestBlob;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;


/**
Testcase JDLobLargeLob.  This tests the following method
of the JDBC Blob class:

<ul>
<li>getBinaryStream()
<li>getBytes()
<li>length()
<li>position()
<li>setBytes()
<li>truncate()
</ul>
**/
public class JDLobLargeLob
extends JDTestcase
{



    // Private data.
    private Connection          connection_;
    private Statement           statement_;
    private ResultSet           rs_;

    private ResultSet           rs2_;

    
    public static String TABLE_  = JDLobTest.COLLECTION + ".JDLLLBLOB";
    public        byte[]  MEDIUM_         = null; 
    public        byte[]  LARGE_          = null; 
    public        byte[]  VLARGE_         = null;  
    public final static int      mega     = 1024*1024;
    public final int      WIDTH_          = 200*mega;  // 200 MB, this value can be increased to 2*1024*mega-1 (test will take 45-60mins).
    
    private StringBuffer message = new StringBuffer(); 
    private	boolean extraBytesOK = false;		// @K3


/**
Constructor.
**/
    public JDLobLargeLob    (AS400 systemObject,
                             Hashtable namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             String password)
    {
        super (systemObject, "JDLobLargeLob",
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
        String vmName = System.getProperty("java.vm.name");
       
        // This byte array will just contain a list of negative
        // even numbers.

        MEDIUM_ = new byte[50];  // [50000];
        for (int i = 0; i < MEDIUM_.length; ++i)
            MEDIUM_[i] = (byte) (-2 * i);

        initializeLargeArray();

	extraBytesOK = getDriver() == JDTestDriver.DRIVER_NATIVE &&		// @K3
	  getRelease() >= JDTestDriver.RELEASE_V7R1M0 ;				// @K3

        TABLE_  = JDLobTest.COLLECTION + ".JDLLLBLOB";
        if (isJdbc20 ()) {
            if (areLobsSupported ()) {
                String url = baseURL_
                             + ";user=" + systemObject_.getUserId()
                             + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                             + ";lob threshold=1";
                connection_ = testDriver_.getConnection(url,systemObject_.getUserId(),encryptedPassword_);
                connection_.setAutoCommit(false);
                connection_.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
                statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                          ResultSet.CONCUR_UPDATABLE);


                initTable(statement_, TABLE_, "(ID INTEGER, C_BLOB BLOB(" + (2*1024*mega-1) + "))");

                PreparedStatement ps = connection_.prepareStatement ("INSERT INTO "
                                                                     + TABLE_ + " (ID, C_BLOB) VALUES (?,?)");
                //long time = System.currentTimeMillis();
                ps.setInt(1, 1); 
                ps.setBytes (2, new byte[0]);
                ps.executeUpdate ();
                //System.out.println("Inserting small Took: " + ((System.currentTimeMillis()-time)/1000) + " seconds");
                //time = System.currentTimeMillis();
                
                ps.setInt(1, 2); 
                ps.setBytes (2, MEDIUM_);
                ps.executeUpdate ();
                //System.out.println("Inserting medium Took: " + ((System.currentTimeMillis()-time)/1000) + " seconds");
                //time = System.currentTimeMillis();
              
                ps.setInt(1, 3); 
                    ps.setBytes (2, LARGE_);
                    ps.executeUpdate ();
              
/* -- CHANGE IT BACK !! @kkashir
                //System.out.println("Inserting large Took: " + ((System.currentTimeMillis()-time)/1000) + " seconds");
                //time = System.currentTimeMillis();
                ps.setBytes (1, VLARGE_);
                //System.out.println("Setting Very Large bytes Took: " + ((System.currentTimeMillis()-time)/1000) + " seconds");
                //time = System.currentTimeMillis();
                ps.executeUpdate ();
*/
                //System.out.println("Inserting Very Large Took: " + ((System.currentTimeMillis()-time)/1000) + " seconds");
                //time = System.currentTimeMillis();
                rs_ = statement_.executeQuery ("SELECT * FROM " + TABLE_ +" ORDER BY ID");
                //System.out.println("Selecting Took: " + ((System.currentTimeMillis()-time)/1000) + " seconds");
                //time = System.currentTimeMillis();
                ps.close ();
	    }
	}
    }

/**
initializes the Large Array to be size WIDTH_
**/
    protected void initializeLargeArray()
    {
	boolean useTmpFile = false;
	if ( File.separatorChar == '/') {
	    useTmpFile = true;
	}



        //long time = System.currentTimeMillis();
        //System.out.println("Large array of size 16megs has been declared & initialized");
        //System.out.println("Took: " + ((System.currentTimeMillis()-time)/1000) + " seconds");
        //time = System.currentTimeMillis();
	File file = null; 
	if (useTmpFile) {   
	    file = new File("/tmp/JDLobLargeLob16"); 
	    if ( file.exists()) {
	        int numMeg = 16;
	        // String vmName = System.getProperty("java.vm.name");
	        
	        LARGE_ = new byte[numMeg*mega];
	        try {
	            System.out.println("Reading from input stream"); 
	            FileInputStream fis = new FileInputStream(file); 
	            fis.read(LARGE_);
	            fis.close();
	            System.out.println("Read from input stream"); 

	        } catch (Exception e) { 
	            e.printStackTrace(); 
	            LARGE_ = null; 
	        }
	    }
	}
	if (LARGE_ == null) { 
	    
	    int numMeg = 16;
	    // String vmName = System.getProperty("java.vm.name");
	    LARGE_ = new byte[numMeg*mega];
	    for (int i =0; i<numMeg*mega;i++)
	    {
	        LARGE_[i] = (byte) (-2 *i);
	    }
	    try {
	        if (useTmpFile) { 
	            FileOutputStream fos = new FileOutputStream(file); 
	            fos.write(LARGE_); 
	            fos.close();
	        }
	    } catch (Exception e) {
	        e.printStackTrace(); 
	    }

	}
/* --- CHANGE IT BACK !!!
        //System.out.println(" & filled with test data.");
        //System.out.println("Took: " + ((System.currentTimeMillis()-time)/1000) + " seconds");
        //time = System.currentTimeMillis();
        VLARGE_ = new byte[WIDTH_];
        //System.out.println("VLarge array of size " + WIDTH_ + " bytes has been declared & initialized");
        //System.out.println("Took: " + ((System.currentTimeMillis()-time)/1000) + " seconds");
        //time = System.currentTimeMillis();

        for (int i = 0; i < mega; i++)
        {
            VLARGE_[i] = LARGE_[i];
            }
        for (int i = 0; i < mega; i++)
        {
            VLARGE_[(WIDTH_-mega)/2+i] = LARGE_[7*mega+i];
        }
        for (int i = 0; i < mega; i++)
        {
            VLARGE_[WIDTH_-mega+i] = LARGE_[15*mega+i];
	}
        //System.out.println(" & filled with test data.");
        //System.out.println("Took: " + ((System.currentTimeMillis()-time)/1000) + " seconds");
        //time = System.currentTimeMillis();
*/
    }

/**
Compares the megabytes specified by offsets
and returns true if the arrays are equal

@exception none.
**/
    protected boolean compareInputStreamToByteArray (InputStream i, byte[] b, boolean extraBytesOK1) {	// @K3
        try {
            //we will check the first, the last and the middle megabyte
            if (b.length > 3*mega)
            {
                if (i.available() != b.length) {
                    message.append("i.available = "+i.available()+" b.length = "+b.length); 
                    return false;
                }
                boolean check = true;
                byte[] m = new byte[mega];
                byte[] m2 = new byte[mega];

                //first meg
                System.arraycopy(b,0,m,0,mega);
                i.read (m2, 0,mega);
                if (!Testcase.isEqual (m, m2)) {
                  message.append("First meg is not equal"); 
                  check = false; 
                }
                

                //middle meg
                i.skip((b.length-3*mega)/2);
                System.arraycopy(b,(b.length-mega)/2,m,0,mega);
                i.read(m2,0,mega);
                if (!Testcase.isEqual (m, m2)) {
                  message.append("Middle meg is not equal"); 
                  check = false; 
                }
                

                //last meg
                i.skip((b.length-3*mega)/2);
                System.arraycopy(b,(b.length-mega),m,0,mega);
                i.read(m2,0,mega);
                if (!Testcase.isEqual (m, m2)) {
                  message.append("Last meg is not equal"); 
                  check = false; 
                }


                return check;
            }
            //too small to bother trying to save time, just check it all.
            else
            {
                byte[] b2 = new byte[b.length];
                int l = i.read (b2, 0, b.length);
                if (l == -1) {
                    message.append("b.length="+b.length+" sb 0"); 
                    return(b.length == 0);
                } else{							// @K3
		    if( extraBytesOK1) {					// @K3
                        if (l != b.length) {
                          message.append("l = "+l+" b.length = "+b.length); 
                          return false; 
                        }
                        if ( ! Testcase.isEqual(b,b2)) {	// @K3
                          message.append("Not equal "); 
                          return false; 
                        }
                        return true; 
                    } else {
                      if (l != b.length) {
                        message.append("l = "+l+" b.length = "+b.length); 
                        return false; 
                      }
                      if (i.available() != 0) {
                        message.append("i.available = "+i.available()); 
                        return false; 
                      }
                      if ( ! Testcase.isEqual(b,b2)) {  // @K3
                        message.append("Not equal "); 
                        return false; 
                      }
                      return true; 
                    }
		}							// @K3
            }
        }							
        catch (Exception e) {
            e.printStackTrace(); 
            return false;
        }
    }

/**
     Indicates whether the contents of two arrays match.
     @param  array1  The first array.
     @param  array2  The second array.
     @return  true if the arrays have the same length and their contents match; false otherwise.
     **/
    public static boolean areEqual(byte[] array1, byte[] array2)
    {
        boolean answer = array1.length == array2.length;
        if (answer)
        {
            //to save time we will only check the first, middle & third megs.
            if (array1.length > 3*mega)
            {
                int offset = (array1.length-mega)/2;
                for (int i =0;i<3;i++)
                {
                    int UO = i*offset;
                    for (int j = 0; j < mega; j++)
                    {
                        if (array1[UO+j] != array2[UO+j])
                        {
                            answer = false;
                            break;
                        }
                    }
                }
            }
            else
            {
                for (int i = 0; i < array1.length; i++)
                {
                    if (array1[i] != array2[i])
                    {
                        answer = false;
                        break;
                    }
                }

            }
        }
        return answer;
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
   * getBinaryStream() - When the lob is not empty.
   **/
  public void Var001() {
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          message.setLength(0);
          rs_.absolute(3);
          Blob blob = rs_.getBlob("C_BLOB");
          InputStream v = blob.getBinaryStream();
          assertCondition(
              compareInputStreamToByteArray(v, LARGE_, extraBytesOK),
              message.toString() + " Large lob Testcases added 01/02/2003"); // @K3
        } catch (Exception e) {
          failed(e,
              "Unexpected Exception -- Large lob Testcases added 01/02/2003");
        }
      }
    }
  }

  /**
   * getBinaryStream() - When the lob is full.
   **/
  public void Var002() {
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          rs_.absolute(3);
          Blob blob = rs_.getBlob("C_BLOB");
          InputStream v = blob.getBinaryStream();
          assertCondition(
              compareInputStreamToByteArray(v, LARGE_, extraBytesOK),
              "Large lob Testcases added 01/02/2003"); // @K3
        } catch (Exception e) {
          failed(e,
              "Unexpected Exception -- Large lob Testcases added 01/02/2003");
        }
      }
    }
  }

  /**
   * getBytes() - Should throw an exception when length extends past the end of
   * the lob.
   * 
   * @K2: Native driver pads extra characters to satisfy the mentioned length as
   *      long as it is within the limits of maxLength_
   **/
  public void Var003() {
    if (checkJdbc20()) {
      try {
        rs_.absolute(3);
        Blob blob = rs_.getBlob("C_BLOB");
        blob.getBytes(3, LARGE_.length + 1);
        if (getRelease() >= JDTestDriver.RELEASE_V7R1M0 && // @K2
            getDriver() == JDTestDriver.DRIVER_NATIVE) // @K2
          succeeded(); // @K2
        else
          // @K2
          failed("Didn't throw SQLException -- Large lob Testcases added 01/02/2003");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException",
            "Large lob Testcases added 01/02/2003");
      }
    }
  }

/**
getBytes() - Should work to retrieve all of a non-empty lob.
**/
    public void Var004()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    rs_.absolute (3);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] v = blob.getBytes (1, LARGE_.length);
                    assertCondition (areEqual (v, LARGE_), "Large lob Testcases added 01/02/2003");
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception -- Large lob Testcases added 01/02/2003");
                }
            }
        }
    }



/**
getBytes() - Should work to retrieve all of a full lob.
**/
    public void Var005()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    rs_.absolute (3);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] v = null;
                    v = blob.getBytes (1, 16*mega);
                    assertCondition (areEqual (v, LARGE_), "Large lob Testcases added 01/02/2003");
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception Large lob Testcases added 01/02/2003");
                }
            }
        }
    }



/**
getBytes() - Should work to retrieve the first part of a non-empty lob.
**/
    public void Var006()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    rs_.absolute (3);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] v = blob.getBytes (1, 3);
                    byte[] expected = new byte[] { LARGE_[0], LARGE_[1], LARGE_[2]};
                    assertCondition (areEqual (v, expected), "Large lob Testcases added 01/02/2003");
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception -- Large lob Testcases added 01/02/2003");
                }
            }
        }
    }



/**
getBytes() - Should work to retrieve the middle part of a non-empty lob.
**/
    public void Var007()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    rs_.absolute (3);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] v = blob.getBytes (9, 6);
                    byte[] expected = new byte[] { LARGE_[8], LARGE_[9], LARGE_[10], LARGE_[11], LARGE_[12], LARGE_[13]};
                    assertCondition (areEqual (v, expected), "Large lob Testcases added 01/02/2003");
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception -- Large lob Testcases added 01/02/2003");
                }
            }
        }
    }



/**
getBytes() - Should work to retrieve the last part of a non-empty lob.
**/
    public void Var008()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    rs_.absolute (3);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] v = blob.getBytes (48, 3);
                    byte[] expected = new byte[] { LARGE_[47], LARGE_[48], LARGE_[49]};
                    assertCondition (areEqual (v, expected), "Large lob Testcases added 01/02/2003");
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception -- Large lob Testcases added 01/02/2003");
                }
            }
        }
    }


/**
length() - When the lob is not empty.
**/
    public void Var009()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    rs_.absolute (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
		    boolean condition = (blob.length() == MEDIUM_.length);
		    if (!condition) {
			System.out.println("blob.length = "+blob.length());
                }
                    assertCondition (condition, "Large lob Testcases added 01/02/2003");
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception Large lob Testcases added 01/02/2003");
                }
            }
        }
    }



/**
length() - When the lob is full.
**/
    public void Var010()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    rs_.absolute (3);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    int numMeg = 16;
                    assertCondition (blob.length () == numMeg*mega, "Large lob Testcases added 01/02/2003");
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception -- Large lob Testcases added 01/02/2003");
                }
            }
        }
    }



/**
position(byte[],long) - Should throw an exception when the pattern is null.
**/
    public void Var011()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    rs_.absolute (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    blob.position ((byte[]) null, (long) 1);
                    failed ("Didn't throw SQLException -- Large lob Testcases added 01/02/2003");
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException", "Large lob Testcases added 01/02/2003");
                }
            }
        }
    }



/**
position(byte[],long) - Should throw an exception when start is less than 0.
**/
    public void Var012()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    rs_.absolute (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] test = new byte[] { -45, -23, 32};
                    blob.position (test, (long) -1);
                    failed ("Didn't throw SQLException Large lob Testcases added 01/02/2003");
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException", "Large lob Testcases added 01/02/2003");
                }
            }
        }
    }



/**
position(byte[],long) - Should throw an exception when start is 0.
**/
    public void Var013()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    rs_.absolute (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] test = new byte[] { -45, -23, 32};
                    blob.position (test, (long) 0);
                    failed ("Didn't throw SQLException -- Large lob Testcases added 01/02/2003");
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException", "Large lob Testcases added 01/02/2003");
                }
            }
        }
    }



/**
position(byte[],long) - Should throw an exception when start is
greater than the length of the lob.
**/
    public void Var014()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    rs_.absolute (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] test = new byte[] { -45, -23, 32};
                    blob.position (test, 40001);
                    failed ("Didn't throw SQLException -- Large lob Testcases added 01/02/2003");
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException", "Large lob Testcases added 01/02/2003");
                }
            }
        }
    }



/**
position(Blob,long) - Should throw an exception when start is
greater than the length of the lob.
**/
    public void Var015()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    rs_.absolute (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] test = new byte[] { 3, 56, -3, -1, 111};
                    blob.position (new JDLobTest.JDTestBlob (test), 45001);
                    failed ("Didn't throw SQLException -- Large lob Testcases added 01/02/2003");
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException", "Large lob Testcases added 01/02/2003");
                }
            }
        }
    }



/**
position(Blob,long) - Should return -1 when the pattern is not found at all.
**/
    public void Var016()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    rs_.absolute (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] test = new byte[] { 23, 0};
                    long v = blob.position (new JDLobTest.JDTestBlob (test), (long) 1);
                    assertCondition (v == -1, "Large lob Testcases added 01/02/2003");
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception -- Large lob Testcases added 01/02/2003");
                }
            }
        }
    }



/**
position(Blob,long) - Should return the position when the pattern is
found at the end of the lob, and start is 1.
**/
    public void Var017()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    rs_.absolute (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] test = new byte[] { -90, -92, -94, -96, -98};
                    long v = blob.position (new JDLobTest.JDTestBlob (test), (long) 1);

                    //@K1D if (getDriver () == JDTestDriver.DRIVER_NATIVE)
                        assertCondition (v == 46, "Large lob Testcases added 01/02/2003");
                    //@K1D else
                    //@K1D     assertCondition (v == -1, "Large lob Testcases added 01/02/2003"); // position() not supported for LOB locators.
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception -- Large lob Testcases added 01/02/2003");
                }
            }
        }
    }



/**
position(Blob,long) - Should return the position when the pattern is
found at the end of the lob, and start is right where the pattern occurs.
**/
    public void Var018()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    rs_.absolute (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] test = new byte[] { -96, -98};
                    long v = blob.position (new JDLobTest.JDTestBlob (test), (long) 49);

                    //@K1D if (getDriver () == JDTestDriver.DRIVER_NATIVE)                           
                        assertCondition (v == 49, "Large lob Testcases added 01/02/2003");
                    //@K1D else
                    //@K1D    assertCondition (v == -1, "Large lob Testcases added 01/02/2003"); // position() not supported for LOB locators.
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception -- Large lob Testcases added 01/02/2003");
                }
            }
        }
    }



    /**
    setBytes(long, byte[]) - Should now throw an exception when length of bytes to write extends
    past the length of the lob as long as start is not passed the end of the lob.
    **/
    public void Var019()
    {
	// Use the sb to record the time.
	// Tool box takes a long time on the updateRow method
	// 
	StringBuffer sb = new StringBuffer();
	long start;
	long end; 
        if (checkUpdateableLobsSupport ()) {
            try {
		start=System.currentTimeMillis(); 
                rs2_=statement_.executeQuery("SELECT * FROM " + TABLE_ +" ORDER BY ID");
		end  =System.currentTimeMillis(); 
		sb.append("executeQuery: "+(end-start)+" ms \n");
		start=System.currentTimeMillis();

                rs2_.absolute(3);
		end  =System.currentTimeMillis(); 
		sb.append("rs2.absolute: "+(end-start)+" ms \n");
		start=System.currentTimeMillis();



                Blob blob = rs2_.getBlob ("C_BLOB");
		end  =System.currentTimeMillis(); 
		sb.append("rs2.getBlob: "+(end-start)+" ms \n");
		start=System.currentTimeMillis();


                byte[] expected = new byte[] { (byte) 56, (byte) 65};
                int written = blob.setBytes (3, expected);
		end  =System.currentTimeMillis(); 
		sb.append("rs2.setBytes: "+(end-start)+" ms \n");
		start=System.currentTimeMillis();

                rs2_.updateBlob("C_BLOB", blob);            //@C2A
		end  =System.currentTimeMillis(); 
		sb.append("rs2.updateBlob: "+(end-start)+" ms \n");
		start=System.currentTimeMillis();

                rs2_.updateRow();                                //@C2A
		end  =System.currentTimeMillis(); 
		sb.append("rs2.updateRow: "+(end-start)+" ms \n");
		start=System.currentTimeMillis();

                rs2_.close();
		end  =System.currentTimeMillis(); 
		sb.append("rs2.close: "+(end-start)+" ms \n");
		start=System.currentTimeMillis();

                rs2_=statement_.executeQuery("SELECT * FROM " + TABLE_ +" ORDER BY ID");
		end  =System.currentTimeMillis(); 
		sb.append("executeQuery: "+(end-start)+" ms \n");
		start=System.currentTimeMillis();

                rs2_.absolute(3);
		end  =System.currentTimeMillis(); 
		sb.append("rs2.absolute: "+(end-start)+" ms \n");
		start=System.currentTimeMillis();

                Blob blob1 = rs2_.getBlob ("C_BLOB");
		end  =System.currentTimeMillis(); 
		sb.append("rs2.getBlob: "+(end-start)+" ms \n");
		start=System.currentTimeMillis();

                byte[] b = blob1.getBytes (3, 2);
		end  =System.currentTimeMillis(); 
		sb.append("blob1.getBytes: "+(end-start)+" ms \n");
		start=System.currentTimeMillis();

		// Print out the time everything took. 
		// System.out.println(sb.toString());
		// 
                assertCondition(areEqual (b, expected) && written == 2, "Large lob Testcases added 01/02/2003");
                rs2_.close();
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception -- Large lob Testcases added 01/02/2003");
            }
        }
    }

    /**
    setBytes(long, byte[]) - Should work to set all of a non-empty lob.
    **/
    public void Var020()
    {
        if (checkUpdateableLobsSupport ()) {
            try {
                rs2_=statement_.executeQuery("SELECT * FROM " + TABLE_+" ORDER BY ID");
                rs2_.absolute (3);
                Blob blob = rs2_.getBlob ("C_BLOB");
                byte[] expected = new byte[] { (byte) 57, (byte) 58, (byte) 98};
                int written = blob.setBytes (1, expected);
                rs2_.updateBlob("C_BLOB", blob);
                rs2_.updateRow();
                rs2_.close ();
                rs2_=statement_.executeQuery("SELECT * FROM " + TABLE_+" ORDER BY ID");
                rs2_.absolute(3);
                Blob blob1 = rs2_.getBlob ("C_BLOB");
                byte[] b = blob1.getBytes (1, 3);
                assertCondition(areEqual (b, expected) && written == 3, "Large lob Testcases added 01/02/2003");
                rs2_.close();
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception -- Large lob Testcases added 01/02/2003");
            }
        }
    }


    /**
    setBytes(long, byte[]) - Should work to set the last part of a non-empty lob.
    **/
    public void Var021()
    {
        if (checkUpdateableLobsSupport ()) {
            try {
                rs2_=statement_.executeQuery("SELECT * FROM " + TABLE_+" ORDER BY ID");
                rs2_.absolute (3);
                Blob blob = rs2_.getBlob ("C_BLOB");
                byte[] expected = new byte[] { (byte) 578};
                int written = blob.setBytes (3, expected);
                rs2_.updateBlob("C_BLOB", blob);
                rs2_.updateRow();
                rs2_.close ();
                rs2_=statement_.executeQuery("SELECT * FROM " + TABLE_+" ORDER BY ID");
                rs2_.absolute(3);
                Blob blob1 = rs2_.getBlob("C_BLOB");
                byte[] b = blob1.getBytes (3, 1);
                assertCondition( areEqual (b, expected) && written == 1, "Large lob Testcases added 01/02/2003");
                rs2_.close();
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception -- Large lob Testcases added 01/02/2003");
            }
        }
    }



    /**
    setBytes(long, byte[]) - Should work to set the middle part of a non-empty lob.
    **/
    public void Var022()
    {
        if (checkUpdateableLobsSupport ()) {
            try {
                rs2_=statement_.executeQuery("SELECT * FROM " + TABLE_+" ORDER BY ID");
                rs2_.absolute (3);
                Blob blob = rs2_.getBlob ("C_BLOB");
                byte[] expected = new byte[] { (byte) 578};
                int written = blob.setBytes (2, expected);
                rs2_.updateBlob("C_BLOB", blob);            //@C2A
                rs2_.updateRow();                                //@C2A
                rs2_.close ();
                rs2_=statement_.executeQuery("SELECT * FROM " + TABLE_+" ORDER BY ID");
                rs2_.absolute(3);
                Blob blob1 = rs2_.getBlob("C_BLOB");
                byte[] b = blob1.getBytes (2, 1);
                assertCondition( areEqual (b, expected) && written == 1, "Large lob Testcases added 01/02/2003");  //@C2C
                rs2_.close();
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception -- Large lob Testcases added 01/02/2003");
            }
        }
    }



    /**
    setBytes(long, byte[], int, int) - Should throw an exception when start is greater
    than the length of the lob.

    @K2 - Native driver permits setting bytes beyond length_ as long as they are within limits of maxLength_
    **/
    public void Var023()
    {
        if (checkUpdateableLobsSupport ()) {
            if (getDriver() == JDTestDriver.DRIVER_NATIVE)
            {
                try {
                    rs2_=statement_.executeQuery("SELECT * FROM " + TABLE_+" ORDER BY ID");
                    rs2_.absolute (3);
                    Blob blob = rs2_.getBlob ("C_BLOB");

		    long pos = blob.length() + 1;
                    blob.setBytes ( pos , new byte[] { (byte) 44}, 0 /*@K2 1*/, 1); //@C2C length changed
		    // in the above stmt. error was generated because offset of 1 generates ArrayIndexOutOfBound exception which is thrown as a java.sql.SQLException
		    // And so changed offset from 1 to 0

		    byte[] result = blob.getBytes( pos, 1);
		    assertCondition( (byte)result[0] == (byte)44, "result[0] = "+(byte)result[0]+
				     "(byte)44 = "+(byte)44);
		    /* @K2 failed ("Didn't throw SQLException -- Large lob Testcases added 01/02/2003"); */
                    rs2_.close();
                }
                catch (Exception e) {
		    /* @K2
                    try {
                        assertExceptionIsInstanceOf (e, "java.sql.SQLException", "Large lob Testcases added 01/02/2003");
                        rs2_.close();
                    }
                    catch (Exception s) {
		    */
		    failed (/*@K2 s */e, "Unexpected Exception -- Large lob Testcases added 01/02/2003");
                    // @K2 }
                }
            }

            else if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
            {
                //@C4C Because Toolbox only knows the max length of a lob, we will
                //@C4C not throw an exception if the user asks for a situation where the start
                //@C4C is greater than the length of the lob until updateRow() is called.
                try {
                    rs2_=statement_.executeQuery("SELECT * FROM " + TABLE_+" ORDER BY ID");
                    rs2_.absolute (3);
                    Blob blob = rs2_.getBlob ("C_BLOB");
                    blob.setBytes (blob.length()+1, new byte[] { (byte) 44}, 1, 1); //@C2C length changed
                    rs2_.updateBlob("C_BLOB", blob);
                    rs2_.updateRow();
                    failed ("Didn't throw SQLException -- Large lob Testcases added 01/02/2003");
                    rs2_.close();
                }
                catch (Exception e) {
                    try {
                        assertExceptionIsInstanceOf (e, "java.sql.SQLException", "Large lob Testcases added 01/02/2003");
                        rs2_.close();
                    }
                    catch (Exception s) {
                        failed (s, "Unexpected Exception -- Large lob Testcases added 01/02/2003");
                    }
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
    public void Var024()
    {
        if (checkUpdateableLobsSupport ()) {
            try {
                rs2_=statement_.executeQuery("SELECT * FROM " + TABLE_ +" ORDER BY ID");
                rs2_.absolute (3);
                byte[] expected = new byte[] { (byte) 56, (byte) 65, (byte) 76, (byte) 89};
                Blob blob = rs2_.getBlob ("C_BLOB");
                int written = blob.setBytes (1, expected, 0, 4); //@A1
                rs2_.updateBlob("C_BLOB", blob);            //@C2A
                rs2_.updateRow();                                //@C2A
                rs2_.close();
                rs2_=statement_.executeQuery("SELECT * FROM " + TABLE_  +" ORDER BY ID");
                rs2_.absolute(3);
                Blob blob1 = rs2_.getBlob("C_BLOB");
                byte[] b = blob1.getBytes (1, 4);
                assertCondition( areEqual (b, expected) && written == 4, "Large lob Testcases added 01/02/2003");   //@C2C
                rs2_.close();
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception -- Large lob Testcases added 01/02/2003");
            }
        }
    }

    /**
    setBytes(long, byte[], int, int) - Should work to set all of a non-empty lob.
    **/
    public void Var025()
    {
        if (checkUpdateableLobsSupport ()) {
            try {
                rs2_=statement_.executeQuery("SELECT * FROM " + TABLE_ + " ORDER BY ID");
                rs2_.absolute (3);
                Blob blob = rs2_.getBlob ("C_BLOB");
                byte[] expected = new byte[] { (byte) 57, (byte) 58, (byte) 98};
                int written = blob.setBytes (1, expected, 0, 3);//@A1
                rs2_.updateBlob("C_BLOB", blob);            //@C2A
                rs2_.updateRow();                                //@C2A
                rs2_.close ();
                rs2_=statement_.executeQuery("SELECT * FROM " + TABLE_ + " ORDER BY ID");
	                    rs2_.absolute(3);
                Blob blob1 = rs2_.getBlob("C_BLOB");
                byte[] b = blob1.getBytes (1, 3);
                assertCondition( areEqual (b, expected) && written == 3, "Large lob Testcases added 01/02/2003");    //@C2C
                rs2_.close();
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception -- Large lob Testcases added 01/02/2003");
            }
        }
    }


    /**
setBytes(long, byte[], int, int) - Should work to set the last part of a non-empty lob.
**/
    public void Var026()
    {
        if (checkUpdateableLobsSupport ()) {
            try {
                rs2_=statement_.executeQuery("SELECT * FROM " + TABLE_ + " ORDER BY ID");
                rs2_.absolute (3);
                Blob blob = rs2_.getBlob ("C_BLOB");
                byte[] expected = new byte[] { (byte) 578};
                int written = blob.setBytes (3, expected, 0, 1);//@A1
                rs2_.updateBlob("C_BLOB", blob);            //@C2A
                rs2_.updateRow();                                //@C2A
                rs2_.close ();
                rs2_=statement_.executeQuery("SELECT * FROM " + TABLE_ + " ORDER BY ID");
                rs2_.absolute(3);
                Blob blob1 = rs2_.getBlob("C_BLOB");
                byte[] b = blob1.getBytes (3, 1);
                assertCondition( areEqual (b, expected) && written == 1, "Large lob Testcases added 01/02/2003");   //@C2C
                rs2_.close();
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception -- Large lob Testcases added 01/02/2003");
            }
        }
    }



    /**
    truncate() - Should throw an exception if you try to truncate to
    a length greater than the length of the lob.
    
    K1C- This variation should succeed.  Only when you try to write past the max column size should it be an exception.

    @K2- Native Driver expects same as Toolbox from v5r3 onwards.
    **/
    public void Var027()
    {
        if (checkUpdateableLobsSupport ())
        {
            if (getDriver () == JDTestDriver.DRIVER_NATIVE &&	// @K2
		getRelease() < JDTestDriver.RELEASE_V7R1M0 )	// @K2
            {
                try
                {
                    rs2_=statement_.executeQuery("SELECT * FROM " + TABLE_ + " ORDER BY ID");
                    rs2_.absolute(3);
                    Blob blob = rs2_.getBlob ("C_BLOB");
                    blob.truncate (5);
                    failed("Didn't throw SQLException -- Large lob Testcases added 01/02/2003");
                    rs2_.close();
                }
                catch (Exception e) {
                    try {
                        assertExceptionIsInstanceOf (e, "java.sql.SQLException", "Large lob Testcases added 01/02/2003");
                        rs2_.close();
                    }
                    catch (Exception s) {
                        failed (s, "Unexpected Exception -- Large lob Testcases added 01/02/2003");
                    }
                }
            }
            else  /* if (getDriver () == JDTestDriver.DRIVER_TOOLBOX  ) */ /* DON'T NEED THIS CHECK */ 
            {
                //@C4C Because Toolbox only knows the max length of a lob, we will
                //@C4C not throw an exception if the user truncates to a length
                //@C4C greater than the end of the lob until updateRow() is called.
                try {
                    rs2_=statement_.executeQuery("SELECT * FROM " + TABLE_ + " ORDER BY ID");
                    rs2_.absolute (3);
                    Blob blob = rs2_.getBlob ("C_BLOB");
                    blob.truncate (5);
                    rs2_.updateBlob("C_BLOB", blob);
                    rs2_.updateRow();
                    //@K1D failed ("Didn't throw SQLException -- Large lob Testcases added 01/02/2003");
                    rs2_.close();
                    succeeded();
                }
                catch (Exception e) {
                    //@K1D try {
                    //@K1D     assertExceptionIsInstanceOf (e, "java.sql.SQLException", "Large lob Testcases added 01/02/2003");
                    //@K1D     rs2_.close();
                    //@K1D }
                    //@K1D catch (Exception s) {
                        failed (e, "Unexpected Exception -- Large lob Testcases added 01/02/2003"); //@K1C changed s to e
                    //@K1D }
                }
	    }

	}
    }


    /**
    truncate() - Should work on a non-empty lob.
    **/
    public void Var028()
    {
        if (checkUpdateableLobsSupport ())
        {
            try
            {
                rs2_=statement_.executeQuery("SELECT * FROM " + TABLE_ + " ORDER BY ID");
                rs2_.absolute(3);
                Blob blob = rs2_.getBlob ("C_BLOB");
                blob.truncate (2);
                rs2_.updateBlob("C_BLOB", blob);            //@C2A
                rs2_.updateRow();                                //@C2A
                rs2_.close ();
                rs2_=statement_.executeQuery("SELECT * FROM " + TABLE_ + " ORDER BY ID");
                rs2_.absolute(3);
                Blob blob1 = rs2_.getBlob("C_BLOB");
                assertCondition (blob1.length() == 2, "Large lob Testcases added 01/02/2003");
                rs2_.close();
            }
            catch (Exception e)
            {
                failed(e, "Unexpected Exception -- Large lob Testcases added 01/02/2003");
            }
        }
    }


    /**
    setBytes(long, byte[]) - Should not change lob if updateRow() is not called.
    **/
    public void Var029()
    {
        if (checkUpdateableLobsSupport ()) {
            try {
                rs2_=statement_.executeQuery("SELECT * FROM " + TABLE_ + " ORDER BY ID");
                rs2_.absolute (2);
                Blob blob = rs2_.getBlob ("C_BLOB");
                rs2_.updateBlob("C_BLOB", blob);
                rs2_.close ();
                rs2_=statement_.executeQuery("SELECT * FROM " + TABLE_ + " ORDER BY ID");
                rs2_.absolute(2);
                Blob blob1 = rs2_.getBlob ("C_BLOB");
                byte[] v = blob1.getBytes (1, MEDIUM_.length);
                assertCondition (areEqual (v, MEDIUM_), "Large lob Testcases added 01/02/2003");
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception -- Large lob Testcases added 01/02/2003");
            }
        }
    }


    /**
    setBytes(long, byte[]) - Make sure lob in database is not changed if updateRow() was not called.
    **/
    public void Var030()
    {
        if (checkUpdateableLobsSupport ()) {
            try {
                rs2_=statement_.executeQuery("SELECT * FROM " + TABLE_ + " ORDER BY ID");
                rs2_.absolute (2);
                Blob blob = rs2_.getBlob ("C_BLOB");
                byte[] expected = new byte[] { (byte) 35};
                int written = blob.setBytes (2, expected);
                rs2_.updateBlob("C_BLOB", blob);
                rs2_.close ();
                rs2_=statement_.executeQuery("SELECT * FROM " + TABLE_ + " ORDER BY ID");
                rs2_.absolute(2);
                Blob blob1 = rs2_.getBlob ("C_BLOB");
                byte[] v = blob1.getBytes (1, MEDIUM_.length);
                assertCondition (areEqual (v, MEDIUM_), "Large lob Testcases added 01/02/2003 written="+written);
                rs2_.close();
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception -- Large lob Testcases added 01/02/2003");
            }
        }
    }


    /**
    setBytes(long, byte[], int, int) - Should work to set bytes to a non-empty lob with multiple
    updates to the same position.
    **/
    public void Var031()
    {
        if (checkUpdateableLobsSupport ()) {
            try {
                rs2_=statement_.executeQuery("SELECT * FROM " + TABLE_ + " ORDER BY ID");
                rs2_.absolute (3);
                Blob blob = rs2_.getBlob ("C_BLOB");
                byte[] expected = new byte[] { (byte) 57, (byte) 58, (byte) 98};
                int written = blob.setBytes (1, expected, 0, 3);  //@A1
                byte[] expected2 = new byte[] { (byte) 64, (byte) 65, (byte) 75};
                int written2 = blob.setBytes (1, expected2, 0, 3); //@A1
                rs2_.updateBlob("C_BLOB", blob);
                rs2_.updateRow();
                rs2_.close ();
                rs2_=statement_.executeQuery("SELECT * FROM " + TABLE_ + " ORDER BY ID");
                rs2_.absolute(3);
                Blob blob1 = rs2_.getBlob("C_BLOB");
                byte[] b = blob1.getBytes (1, 3);
                assertCondition( areEqual (b, expected2) && written == 3 && written2 == 3, "Large lob Testcases added 01/02/2003");
                rs2_.close();
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception -- Large lob Testcases added 01/02/2003");
            }
        }
    }


    /**
    setBytes(long, byte[], int, int) - Should work to set bytes to a non-empty lob with multiple
    updates to the different positions.
    **/
    public void Var032()
    {
        if (checkUpdateableLobsSupport ()) {
            try {
                rs2_=statement_.executeQuery("SELECT * FROM " + TABLE_ + " ORDER BY ID");
                rs2_.absolute (3);
                Blob blob = rs2_.getBlob ("C_BLOB");
                byte[] bytes = new byte[] { (byte) 57, (byte) 58, (byte) 98};
                int written = blob.setBytes (1, bytes, 0, 3); //@A1
                byte[] bytes2 = new byte[] { (byte) 64, (byte) 65, (byte) 75};
                int written2 = blob.setBytes (3, bytes2, 0, 3); //@A1
                byte[] expected = new byte[] {(byte) 57, (byte) 58, (byte) 64, (byte) 65, (byte) 75};
                rs2_.updateBlob("C_BLOB", blob);
                rs2_.updateRow();
                rs2_.close ();
                rs2_=statement_.executeQuery("SELECT * FROM " + TABLE_ + " ORDER BY ID");
                rs2_.absolute(3);
                Blob blob1 = rs2_.getBlob("C_BLOB");
                byte[] b = blob1.getBytes (1, 5);
                assertCondition( areEqual (b, expected) && written == 3 && written2 == 3, "Large lob Testcases added 01/02/2003");
                rs2_.close();
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception -- Large lob Testcases added 01/02/2003");
            }
        }
    }



    /**
   setBytes(long, byte[], int, int) - Should work to set bytes to a non-empty lob with multiple
   updates to the same position.
   **/
    public void Var033()
    {
        if (checkUpdateableLobsSupport ()) {
            try {
                rs2_=statement_.executeQuery("SELECT * FROM " + TABLE_ + " ORDER BY ID");
                rs2_.absolute (3);
                Blob blob = rs2_.getBlob ("C_BLOB");
                byte[] expected = new byte[] { (byte) 57, (byte) 58, (byte) 98};
                int written = blob.setBytes (1, expected, 0, 3); //@A1
                rs2_.updateBlob("C_BLOB", blob);
                byte[] expected2 = new byte[] { (byte) 64, (byte) 65, (byte) 75};
                int written2 = blob.setBytes (1, expected2, 0, 3); //@A1
                rs2_.updateBlob("C_BLOB", blob);
                rs2_.updateRow();
                rs2_.close ();
                rs2_=statement_.executeQuery("SELECT * FROM " + TABLE_ + " ORDER BY ID");
                rs2_.absolute(3);
                Blob blob1 = rs2_.getBlob("C_BLOB");
                byte[] b = blob1.getBytes (1, 3);
                assertCondition( areEqual (b, expected2) && written == 3 && written2 == 3, "Large lob Testcases added 01/02/2003");
                rs2_.close();
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception -- Large lob Testcases added 01/02/2003");
            }
        }
    }

    /**
    setBytes(long, byte[], int, int) - Should work to set bytes to a non-empty lob with multiple
    updates to the different positions with updateBlob calls between.
    **/
    public void Var034()
    {
        if (checkUpdateableLobsSupport ()) {
            try {
                rs2_=statement_.executeQuery("SELECT * FROM " + TABLE_ + " ORDER BY ID");
                rs2_.absolute (3);
                Blob blob = rs2_.getBlob ("C_BLOB");
                byte[] bytes = new byte[] { (byte) 57, (byte) 58, (byte) 98};
                int written = blob.setBytes (1, bytes, 0, 3); //@A1
                byte[] bytes2 = new byte[] { (byte) 64, (byte) 65, (byte) 75};
                rs2_.updateBlob("C_BLOB", blob);
                int written2 = blob.setBytes (3, bytes2, 0, 3); //@A1
                byte[] expected = new byte[] {(byte) 57, (byte) 58, (byte) 64, (byte) 65, (byte) 75};
                rs2_.updateBlob("C_BLOB", blob);
                rs2_.updateRow();
                rs2_.close ();
                rs2_=statement_.executeQuery("SELECT * FROM " + TABLE_ + " ORDER BY ID");
                rs2_.absolute(3);
                Blob blob1 = rs2_.getBlob("C_BLOB");
                byte[] b = blob1.getBytes (1, 5);
                assertCondition( areEqual (b, expected) && written == 3 && written2 == 3, "Large lob Testcases added 01/02/2003");
                rs2_.close();
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception -- Large lob Testcases added 01/02/2003");
            }
        }
    }

/*
    public void Var035()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    rs2_=statement_.executeQuery("SELECT * FROM " + TABLE_ + " ORDER BY ID");
                    rs2_.absolute (4);
                    Blob blob = rs2_.getBlob ("C_BLOB");                        //@K1C changed to rs2_ instead of rs_
                    byte[] b1 = blob.getBytes (1,mega);
                    byte[] b2 = blob.getBytes ((WIDTH_-mega)/2 + 1,mega);       //@K1C this is one based
                    byte[] b3 = blob.getBytes ((WIDTH_-mega) + 1,mega);         //@K1C this is one based
                    byte[] c1 = new byte[mega];
                    byte[] c2 = new byte[mega];
                    byte[] c3 = new byte[mega];
                    System.arraycopy(LARGE_,0,c1,0,mega);
                    System.arraycopy(LARGE_,7*mega,c2,0,mega);
                    System.arraycopy(LARGE_,15*mega,c3,0,mega);
                    assertCondition (areEqual (b1, c1) && areEqual (b2, c2) && areEqual (b3, c3), "Large lob Testcases added 01/02/2003");  //@K1C changed to compare b2 with c2 and b3 with c3 instead of b1 and c1 three times
                    rs2_.close();
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception -- Large lob Testcases added 01/02/2003");
                }
            }
        }                           
    }
*/
}


