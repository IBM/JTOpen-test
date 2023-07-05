///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDLobBlobLocator.java
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
import java.io.BufferedInputStream;
import java.io.OutputStream;                          //@C2A
import java.sql.Blob;
import java.sql.CallableStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException; 
import java.util.Hashtable;


/**
Testcase JDLobBlobLocator.  This tests the following method
of the JDBC Blob class:

<ul>
<li>getBinaryStream()
<li>getBytes()
<li>length()
<li>position()
<li>setBytes()    // @C2A
<li>truncate()    // @C2A
</ul>
**/
public class JDLobBlobLocator
extends JDTestcase
{

  // The native JDBC driver will free the locators in blocks of 32
  public final static int FREE_LOCATOR_BLOCK_SIZE=32; 



    // Private data.
    private Connection          connection_;
    private Statement           statement_;
    private Statement           statement2_;
    private Statement           statement6_;
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
    private ResultSet           rs6_;               //@pda

    public static String TABLE_  = JDLobTest.COLLECTION + ".BLOBLOC";
    public static String TABLE2_ = JDLobTest.COLLECTION + ".BLOBLOC2";
    public static String TABLE6_ = JDLobTest.COLLECTION + ".BLOBLOC6";      //@pda
    public static       byte[]  MEDIUM_         = null; // final
    public static       byte[]  LARGE_          = null; // final
    public static final int     WIDTH_          = 30000;
    public static       byte[]  SMALL_          = null; // @C2A

    private byte[]  biggerBlob  = null;
    private byte[]  biggerBlob2 = null;
    private byte[]  biggerBlob3 = null;


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

        SMALL_ = new byte[] { (byte) 22, (byte) 4, (byte) -2};            // @C2A
    }



/**
Constructor.
**/
    public JDLobBlobLocator (AS400 systemObject,
                             Hashtable namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             String password)
    {
        super (systemObject, "JDLobBlobLocator",
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
        TABLE_  = JDLobTest.COLLECTION + ".BLOBLOC";
        TABLE2_ = JDLobTest.COLLECTION + ".BLOBLOC2";
        TABLE6_ = JDLobTest.COLLECTION + ".BLOBLOC6";   

        if (isJdbc20 ()) {
            if (areLobsSupported ()) {
                String url = baseURL_
                             + ";lob threshold=1";
                            connection_ = testDriver_.getConnection (url, systemObject_.getUserId(), encryptedPassword_);

                connection_.setAutoCommit(false);
                connection_.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
		statement_  = connection_.createStatement(); 

		try { 
		    statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                          ResultSet.CONCUR_UPDATABLE); //@C2C
		} catch (Exception e) {
		    System.out.println("Warning:  Exception creating statement2");
		    e.printStackTrace();
		    statement2_ = connection_.createStatement(); 
		} 

		try { 
		    statement6_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                          ResultSet.CONCUR_UPDATABLE); //@C2C
		} catch (Exception e) {
		    System.out.println("Warning:  Exception creating statement6");
		    e.printStackTrace();
		    statement6_ = connection_.createStatement(); 
		} 

		try{
		    statement_.executeUpdate(" DROP TABLE "+TABLE_);
		}catch(Exception e){
		}

		try{
		    statement_.executeUpdate(" DROP TABLE "+TABLE2_);
		}catch(Exception e){
		}

                statement_.executeUpdate ("CREATE TABLE " + TABLE_ 
                                          + "(C_BLOB BLOB(" + WIDTH_ + "), ID INT )");

                statement_.executeUpdate ("CREATE TABLE " + TABLE2_ 
                                          + "(C_BLOB BLOB(204800))");

                try{
                    statement_.executeUpdate(" DROP TABLE "+TABLE6_);
                }catch(Exception e){
                }
                statement_.executeUpdate ("CREATE TABLE " + TABLE6_ + "(C_BLOB BLOB(" + WIDTH_ + "))");  //@pda

                PreparedStatement ps = connection_.prepareStatement ("INSERT INTO " 
                                                                     + TABLE_ + " (C_BLOB, ID) VALUES (?,?)");
                ps.setBytes (1, new byte[0]);
		ps.setInt(2, 1); 
                ps.executeUpdate ();

                ps.setBytes (1, MEDIUM_);
		ps.setInt(2, 2); 
                ps.executeUpdate ();

                ps.setBytes (1, LARGE_);
		ps.setInt(2, 3); 
                ps.executeUpdate ();

                ps.setBytes (1, SMALL_);   // @C2A
		ps.setInt(2, 4);
                ps.executeUpdate ();       // @C2A
                ps.close ();

                rs_ = statement_.executeQuery ("SELECT * FROM " + TABLE_);

                biggerBlob  = new byte[49 * 1024];
                biggerBlob2 = new byte[100 * 1024];
                biggerBlob3 = new byte[150 * 1024];

                for (int i=0; i<biggerBlob.length; i++)
                    biggerBlob[i] = (byte) (i % 10);

                for (int i=0; i<biggerBlob2.length; i++)
                    biggerBlob2[i] = (byte) ((i % 10) + 10);

                for (int i=0; i<biggerBlob3.length; i++)
                    biggerBlob3[i] = (byte) ((i % 10) + 20);

                ps = connection_.prepareStatement ("INSERT INTO " + TABLE2_ + " (C_BLOB) VALUES (?)");
                ps.setBytes (1, biggerBlob);
                ps.executeUpdate ();
                ps.setBytes (1, biggerBlob2);
                ps.executeUpdate ();
                ps.setBytes (1, biggerBlob3);
                ps.executeUpdate ();

                ps.close();          
                
                PreparedStatement ps6 = connection_.prepareStatement("INSERT INTO " + TABLE6_ + " (C_BLOB) VALUES (?)");  //@pda
                for (int i = 0; i < FREE_LOCATOR_BLOCK_SIZE ; i++) {//@pda
                    ps6.setBytes(1, SMALL_);//@pda
                    ps6.executeUpdate();//@pda
                }//@pda
                ps6.close(); //@pda
                
                
                // Create the UDF to test the locator
                String cProgram[] = {
                    "   /* File bloblocinf.c created on Thu Feb 22 2007. */",
                    "",
                    "  /*",
                    "  * UDF to get info about a blob from a locator",
                    "",
                    "",
                    "    compile using the following ",
                    "   CRTCMOD MODULE(BLOBLOCINF) DBGVIEW(*ALL)   ",
                    "  CRTSRVPGM BLOBLOCINF export(*all)",
                    "",
                    "   CREATE FUNCTION BLOBLOCINF (int) RETURNS VARCHAR(200) ",
                    "   LANGUAGE C EXTERNAL NAME ",
                    "   'QGPL/BLOBLOCINF(BLOBLOCINF)', PARAMETER STYLE SQL",
                    "",
                    "   To test",
                    "",
                    "    select BLOBLOCINF(256) from qsys2.qsqptabl",
                    "","",
                    "",
                    "",
                    "  */",
                    "",
                    "#include <stdlib.h>",
                    "#include <stdio.h>",
                    "#include <string.h>",
                    "#include <sqludf.h>",
                    "",
                    "void  BLOBLOCINF (int * locator,",
                    "                  char * output,",
                    "                  int * ind0,",
                    "                  int * ind1,",
                    "                  char * sqlstate,",
                    "                  char * functionName,",
                    "                  char * specificName,",
                    "                  char * messageText",
                    "                 ) {",
                    "     int rc; ",
                    "     long length;",
                    "     udf_locator loc;",
                    "",
                    "     loc = *locator; ",
                    "     rc = sqludf_length(&loc, &length);",
                    "     if (rc == 0) {",
                    "         sprintf(output, \"length for locator %d is %d\", loc, length); ",
                    "     } else {",
                    "         sprintf(output, \"ERROR sqludf_length for %d  returned %d\", loc, rc); ",
                    "     } ",
                    "",
                    "                    }",
                };


                try {

                    //
                    // Make sure the procedure exists to call CL commands.
                    // 
		    Statement s1 = connection_.createStatement(); 
		    try {
			s1.executeUpdate("create procedure "+
					   "QGPL.JDCMDEXEC(IN CMDSTR VARCHAR(1024),IN CMDLEN DECIMAL(15,5)) "+
					   "External name QSYS.QCMDEXC LANGUAGE C GENERAL"); 
		    } catch (Exception e) {
                         	  // Just ignore error 
		    }
		    s1.close(); 

                  stringArrayToSourceFile(connection_,cProgram, JDLobTest.COLLECTION, "BLOBLOCINF");

                  CallableStatement cmd = connection_.prepareCall("call QGPL.JDCMDEXEC(?,?)");
                  String command = "CRTCMOD MODULE("+JDLobTest.COLLECTION+"/BLOBLOCINF) "+
                                          " SRCFILE("+JDLobTest.COLLECTION+"/BLOBLOCINF)   ";
                  
                  cmd.setString(1, command );
                  cmd.setInt(2, command.length());
                  try { 
                    cmd.execute();
                  } catch (Exception e) {
                    System.out.println("Error calling "+command); 
                    e.printStackTrace(); 
                  }
                  
                  command = "CRTSRVPGM SRVPGM("+JDLobTest.COLLECTION+"/BLOBLOCINF) MODULE("+JDLobTest.COLLECTION+"/BLOBLOCINF) EXPORT(*ALL)  "; 
                  cmd.setString(1, command );
                  cmd.setInt(2, command.length());
                  try {
                    cmd.execute();
                  } catch (Exception e) {
                    System.out.println("Error calling "+command); 
                    e.printStackTrace(); 
                  }
                  
                  String sql = " CREATE FUNCTION "+JDLobTest.COLLECTION +".BLOBLOCINF (int) RETURNS VARCHAR(200) "+
                      "LANGUAGE C EXTERNAL NAME " +
                      "'"+JDLobTest.COLLECTION+"/BLOBLOCINF(BLOBLOCINF)' PARAMETER STYLE SQL"; 

                  Statement stmt = connection_.createStatement(); 
                  try {
                      stmt.executeUpdate("drop FUNCTION "+JDLobTest.COLLECTION +".BLOBLOCINF"); 
                  } catch (Exception e) {
                  } 
                  try {
                    stmt.executeUpdate(sql); 
                  } catch (Exception e) {
                    System.out.println("Error running "+sql); 
                    e.printStackTrace(); 
                  }
                  stmt.close(); 
                } catch (Exception e) {
                  e.printStackTrace(); 
                }
                
            }
        }

	v5r2nativeFunctions_ = (getRelease() == JDTestDriver.RELEASE_V5R2M0) && (getDriver() == JDTestDriver.DRIVER_NATIVE);

    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
	statement2_.close();
	statement6_.close(); 
        if (isJdbc20 ()) {
            if (areLobsSupported ()) {
		try { 
		    statement_.executeUpdate ("DROP TABLE " + TABLE_);
		} catch (Exception e) {}
		try { 
                statement_.executeUpdate ("DROP TABLE " + TABLE2_);
		} catch (Exception e) {}
                try {
                  statement_.executeUpdate("drop FUNCTION "+JDLobTest.COLLECTION +".BLOBLOCINF"); 
              } catch (Exception e) {
              } 

            } 
        }
        try { 
            statement_.executeUpdate ("DROP TABLE " + TABLE6_);
        } catch (Exception e) {}

	statement_.close ();
	connection_.close ();

    }



    public void positionToRow(int x) throws SQLException {
	rs_ = statement_.executeQuery ("SELECT * FROM " + TABLE_);
	for (int i = 0; i < x; i++) {
	    rs_.next(); 
	} 
    }    

/**
getBinaryStream() - When the lob is empty.
**/
    public void Var001()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    positionToRow (1);
                    Blob blob = rs_.getBlob ("C_BLOB");
		    System.out.println("****** blobLocator = "+blob);
		    sb.setLength(0);
		    InputStream v = blob.getBinaryStream ();
		    if(getDriver() == JDTestDriver.DRIVER_NATIVE &&	// @D3
		       getRelease() >= JDTestDriver.RELEASE_V5R3M0)	// @D3
			assertCondition( compareBeginsWithBytes( v, new byte[0],sb),sb);	// @D3
		    else						// @D3
			assertCondition (compare (v, new byte[0],sb),sb);
		}
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }


/**
getBinaryStream() - When the lob is not empty.
**/
    public void Var002()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
		    int id = rs_.getInt(2); 
		    InputStream v = blob.getBinaryStream ();
		    sb.setLength(0);
		    boolean condition;					// @D3
		    if(getDriver() == JDTestDriver.DRIVER_NATIVE &&	// @D3
		       getRelease() >= JDTestDriver.RELEASE_V5R3M0)	// @D3
			condition = id==2 && compareBeginsWithBytes( v, MEDIUM_,sb );	// @D3
		    else						// @D3
			condition = ( id == 2 && compare (v, MEDIUM_,sb)); 
		    assertCondition (condition, "id should be 2 but was "+id+" or compare mismatch "+sb);
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



/**
getBinaryStream() - When the lob is full.
**/
    public void Var003()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    positionToRow (3);
		    Blob blob = rs_.getBlob ("C_BLOB");
		    sb.setLength(0); 
		    InputStream v = blob.getBinaryStream ();
		    if(getDriver() == JDTestDriver.DRIVER_NATIVE &&		// @D3
		       getRelease() >= JDTestDriver.RELEASE_V5R3M0)		// @D3
			assertCondition( compareBeginsWithBytes( v, LARGE_ ,sb),sb);	// @D3
		    else							// @D3
			assertCondition (compare (v, LARGE_,sb),sb);
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
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
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    blob.getBytes (-1, 5);
                    failed ("Didn't throw SQLException");
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }



/**
getBytes() - Should throw an exception when start is 0.
**/
    public void Var005()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    blob.getBytes (0, 5);
                    failed ("Didn't throw SQLException");
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }



/**
getBytes() - Should return an empty byte array
than the length of the lob.

SQL400 - Given what this test is doing, this should be an exception condition.
**/
    public void Var006()
    {        
        //@C3D if (getDriver () == JDTestDriver.DRIVER_NATIVE) {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] v = blob.getBytes (MEDIUM_.length+1, 5);
                    failed ("Didn't throw SQLException "+v);
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
        //@C3D }
        //@C3D else {
        //@C3D     if (checkJdbc20 ()) {
        //@C3D         if (checkLobSupport ()) {
        //@C3D             try {
        //@C3D                 positionToRow (2);
        //@C3D                 Blob blob = rs_.getBlob ("C_BLOB");
        //@C3D                 byte[] v = blob.getBytes (MEDIUM_.length+1, 5);
        //@C3D                 assertCondition (v.length == 0);
        //@C3D             }
        //@C3D             catch (Exception e) {
        //@C3D                 failed (e, "Unexpected Exception");
        //@C3D             }
        //@C3D         }
        //@C3D     }
        //@C3D }
    }



/**
getBytes() - Should throw an exception when length is less
than 0.
**/
    public void Var007()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    blob.getBytes (2, -1);
                    failed ("Didn't throw SQLException");
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }


/**
getBytes() - Should throw an exception when length extends
past the end of the lob.

SQL400 - The test is saying it will test the right thing but then
         testing the wrong thing.

@D1 - Native does padding and returns the Byte Array.
**/
    public void Var008()
    {
            if (checkJdbc20 ()) {
                if (checkLobSupport ()) {
                    try {
                        positionToRow (2);
                        Blob blob = rs_.getBlob ("C_BLOB");
                        byte[] v = blob.getBytes (47, 8);
			if(getDriver() == JDTestDriver.DRIVER_NATIVE &&		// @D1
			   getRelease() >= JDTestDriver.RELEASE_V5R3M0)		// @D1
			    succeeded();					// @D1
			else
			    failed ("Didn't throw SQLException"+v);
                    }
                    catch (Exception e) {
                        assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                    }
                }
            }
        }



/**
getBytes() - Should work on an empty lob.         
**/
    public void Var009()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                byte[]v = null; 
                try {
                    positionToRow (1);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    v = blob.getBytes (1, 0);
//                    failed ("Didn't throw SQLException");
                    succeeded();
                }
                catch (Exception e) {
//                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                  failed(e, "Unexpected exception"+v);
                }
            }
        }
    }



/**
getBytes() - Should work to retrieve all of a non-empty lob.
**/
    public void Var010()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);                    
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] v = blob.getBytes (1, MEDIUM_.length);
                    assertCondition (isEqual (v, MEDIUM_));
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



/**
getBytes() - Should work to retrieve all of a full lob.
**/
    public void Var011()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    positionToRow (3);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] v = blob.getBytes (1, WIDTH_);
                    assertCondition (isEqual (v, LARGE_));
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



/**
getBytes() - Should work to retrieve the first part of a non-empty lob.
**/
    public void Var012()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] v = blob.getBytes (1, 3);
                    byte[] expected = new byte[] { 0, -2, -4};
                    assertCondition (isEqual (v, expected));
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



/**
getBytes() - Should work to retrieve the middle part of a non-empty lob.
**/
    public void Var013()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] v = blob.getBytes (9, 6);
                    byte[] expected = new byte[] { -16, -18, -20, -22, -24, -26};
                    assertCondition (isEqual (v, expected));
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



/**
getBytes() - Should work to retrieve the last part of a non-empty lob.
**/
    public void Var014()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] v = blob.getBytes (48, 3);
                    byte[] expected = new byte[] { -94, -96, -98};
                    assertCondition (isEqual (v, expected));
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



/**
length() - When the lob is empty.

SQL400 - Native returns length of lob, not length of column
**/
    public void Var015()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    positionToRow (1);
                    Blob blob = rs_.getBlob ("C_BLOB");

                    // @C1D if (getDriver () == JDTestDriver.DRIVER_NATIVE)
                    assertCondition (blob.length() == 0);
                    // @C1D else
                    // @C1D    assertCondition (blob.length() == WIDTH_);

                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }


/**
length() - When the lob is not empty.

SQL400 - Native returns length of lob, not length of column
**/
    public void Var016()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    // @C1D if (getDriver () == JDTestDriver.DRIVER_NATIVE)
                    assertCondition (blob.length() == 50);
                    // @C1D else
                    // @C1D    assertCondition (blob.length() == WIDTH_);

                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



/**
length() - When the lob is full.
**/
    public void Var017()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    positionToRow (3);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    assertCondition (blob.length () == WIDTH_);
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



/**
position(byte[],long) - Should throw an exception when the pattern is null.
**/
    public void Var018()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    blob.position ((byte[]) null, (long) 1);
                    failed ("Didn't throw SQLException");
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }



/**
position(byte[],long) - Should throw an exception when start is less than 0.
**/
    public void Var019()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] test = new byte[] { -45, -23, 32};
                    blob.position (test, (long) -1);
                    failed ("Didn't throw SQLException");
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }



/**
position(byte[],long) - Should throw an exception when start is 0.
**/
    public void Var020()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] test = new byte[] { -45, -23, 32};
                    blob.position (test, (long) 0);
                    failed ("Didn't throw SQLException");
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
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
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] test = new byte[] { -45, -23, 32};
                    blob.position (test, 40001);
                    failed ("Didn't throw SQLException");
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }



/**
position(byte[],long) - Should return -1 when the pattern is not found at all.
**/
    public void Var022()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] test = new byte[] {  32, 0, -1, 32, 44, 34};
                    long v = blob.position (test, (long) 1);
                    assertCondition (v == -1, "POSITION should have returned -1 instead it returned "+v);
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
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
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] test = new byte[] { -6, -8, -10, -12};
                    long v = blob.position (test, (long) 33);
                    assertCondition (v == -1, "POSITION should have returned -1 instead it returned "+v);
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
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
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] test = new byte[] { 0, -2, -4, -6, -8, -10};
                    long v = blob.position (test, (long) 1);

//                    if (getDriver () == JDTestDriver.DRIVER_NATIVE)
                        assertCondition (v == 1, "POSITION should have returned 1 instead it returned "+v);
//                    else
//                        assertCondition (v == -1); // position() not supported for LOB locators.
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
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
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] test = new byte[] { -28, -30};
                    long v = blob.position (test, (long) 1);

//                    if (getDriver () == JDTestDriver.DRIVER_NATIVE)
                        assertCondition (v == 15, "POSITION should have returned 15 instead it returned "+v);
//                    else
//                        assertCondition (v == -1); // position() not supported for LOB locators.
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
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
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] test = new byte[] { -32, -34, -36, -38, -40};
                    long v = blob.position (test, (long) 7);

//                    if (getDriver () == JDTestDriver.DRIVER_NATIVE)
                        assertCondition (v == 17, "POSITION should have returned 17 instead it returned "+v);
//                    else
//                        assertCondition (v == -1); // position() not supported for LOB locators.
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
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
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] test = new byte[] { -20};
                    long v = blob.position (test, (long) 11);

//                    if (getDriver () == JDTestDriver.DRIVER_NATIVE)
                        assertCondition (v == 11, "POSITION should have returned 11 instead it returned "+v);
//                    else
//                        assertCondition (v == -1); // position() not supported for LOB locators.
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
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
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] test = new byte[] { -92, -94, -96, -98};
                    long v = blob.position (test, (long) 1);

//                    if (getDriver () == JDTestDriver.DRIVER_NATIVE)
                        assertCondition (v == 47, "POSITION should have returned 47 instead it returned "+v);
//                    else
//                        assertCondition (v == -1); // position() not supported for LOB locators.
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
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
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] test = new byte[] { -98};
                    long v = blob.position (test, (long) 50);

//                    if (getDriver () == JDTestDriver.DRIVER_NATIVE)
                        assertCondition (v == 50, "POSITION should have returned 50 instead it returned "+v);
//                    else
//                        assertCondition (v == -1); // position() not supported for LOB locators.
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
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
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] test = new byte[0];
                    long v = blob.position (test, (long) 1);

//                    if (getDriver () == JDTestDriver.DRIVER_NATIVE)
                        assertCondition (v == 1);
//                    else
//                        assertCondition (v == -1); // position() not supported for LOB locators.
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
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
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] test = new byte[0];
                    long v = blob.position (test, (long) 39);

//                    if (getDriver () == JDTestDriver.DRIVER_NATIVE)
                        assertCondition (v == 39);
//                    else
//                        assertCondition (v == -1); // position() not supported for LOB locators.
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



/**
position(byte[],long) - Should return length-1 when the pattern is the empty
string and start is the length-1.
**/
    public void Var032()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] test = new byte[0];
                    long v = blob.position (test, (long) MEDIUM_.length);

//                    if (getDriver () == JDTestDriver.DRIVER_NATIVE)
                        assertCondition (v == 50);
//                    else
//                        assertCondition (v == -1); // position() not supported for LOB locators.
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



/**
position(Blob,long) - Should throw an exception when the pattern is null.
**/
    public void Var033()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    blob.position ((Blob) null, (long) 1);
                    failed ("Didn't throw SQLException");
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }



/**
position(Blob,long) - Should throw an exception when start is less than 0.
**/
    public void Var034()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] test = new byte[] { -34, 23, 5, 6};
                    blob.position (new JDLobTest.JDTestBlob (test), (long) -1);
                    failed ("Didn't throw SQLException");
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }



/**
position(Blob,long) - Should throw an exception when start is 0.
**/
    public void Var035()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] test = new byte[] { -34, 23, 5, 6};
                    blob.position (new JDLobTest.JDTestBlob (test), (long) 0);
                    failed ("Didn't throw SQLException");
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
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
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] test = new byte[] { 3, 56, -3, -1, 111};
                    blob.position (new JDLobTest.JDTestBlob (test), 45001);
                    failed ("Didn't throw SQLException");
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }



/**
position(Blob,long) - Should return -1 when the pattern is not found at all.
**/
    public void Var037()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] test = new byte[] { 23, 0};
                    long v = blob.position (new JDLobTest.JDTestBlob (test), (long) 1);
                    assertCondition (v == -1, "POSITION should have returned -1 instead it returned "+v);
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
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
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] test = new byte[] { -6, -8};
                    long v = blob.position (new JDLobTest.JDTestBlob (test), (long) 14);
                    assertCondition (v == -1, "POSITION should have returned -1 instead it returned "+v);
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
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
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] test = new byte[] { 0, -2, -4, -6, -8, -10, -12, -14};
                    long v = blob.position (new JDLobTest.JDTestBlob (test), (long) 1);

//                    if (getDriver () == JDTestDriver.DRIVER_NATIVE)
                        assertCondition (v == 1, "POSITION should have returned 1 instead it returned "+v);
//                    else
//                        assertCondition (v == -1); // position() not supported for LOB locators.
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
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
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] test = new byte[] { -16, -18, -20, -22, -24, -26, -28};
                    long v = blob.position (new JDLobTest.JDTestBlob (test), (long) 1);

//                    if (getDriver () == JDTestDriver.DRIVER_NATIVE)
                        assertCondition (v == 9, "POSITION should have returned 9 instead it returned "+v);
//                    else
//                        assertCondition (v == -1); // position() not supported for LOB locators.
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
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
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] test = new byte[] { -30, -32, -34, -36, -38, -40};
                    long v = blob.position (new JDLobTest.JDTestBlob (test), (long) 5);

//                    if (getDriver () == JDTestDriver.DRIVER_NATIVE)
                        assertCondition (v == 16, "POSITION should have returned 16 instead it returned "+v);
//                    else
//                        assertCondition (v == -1); // position() not supported for LOB locators.
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
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
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] test = new byte[] { -42, -44, -46, -48, -50, -52, -54, -56};
                    long v = blob.position (new JDLobTest.JDTestBlob (test), (long) 22);

//                    if (getDriver () == JDTestDriver.DRIVER_NATIVE)
                        assertCondition (v == 22, "POSITION should have returned 22 instead it returned "+v);
//                    else
//                        assertCondition (v == -1); // position() not supported for LOB locators.
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
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
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] test = new byte[] { -90, -92, -94, -96, -98};
                    long v = blob.position (new JDLobTest.JDTestBlob (test), (long) 1);

//                    if (getDriver () == JDTestDriver.DRIVER_NATIVE)
                        assertCondition (v == 46, "POSITION should have returned 46 instead it returned "+v);
//                    else
//                        assertCondition (v == -1); // position() not supported for LOB locators.
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
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
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] test = new byte[] { -96, -98};
                    long v = blob.position (new JDLobTest.JDTestBlob (test), (long) 49);

//                    if (getDriver () == JDTestDriver.DRIVER_NATIVE)
                        assertCondition (v == 49, "POSITION should have returned 49 instead it returned "+v);
//                    else
//                        assertCondition (v == -1); // position() not supported for LOB locators.
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
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
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] test = new byte[0];
                    long v = blob.position (new JDLobTest.JDTestBlob (test), (long) 1);

//                    if (getDriver () == JDTestDriver.DRIVER_NATIVE)
                        assertCondition (v == 1, "POSITION should have returned 1 instead it returned "+v);
//                    else
//                        assertCondition (v == -1); // position() not supported for LOB locators.
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
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
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");              
                    byte[] test = new byte[0];
                    long v = blob.position (new JDLobTest.JDTestBlob (test), (long) 45);

//                    if (getDriver () == JDTestDriver.DRIVER_NATIVE)
                        assertCondition (v == 45, "POSITION should have returned 45 instead it returned "+v);
//                    else
//                        assertCondition (v == -1); // position() not supported for LOB locators.
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



/**
position(Blob,long) - Should return length-1 when the pattern is the empty
lob and start is the length-1.
**/
    public void Var047()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    positionToRow (2);
                    Blob blob = rs_.getBlob ("C_BLOB");
                    byte[] test = new byte[0];
                    long v = blob.position (new JDLobTest.JDTestBlob (test), (long) MEDIUM_.length);

//                    if (getDriver () == JDTestDriver.DRIVER_NATIVE)
                        assertCondition (v == 50, "POSITION should have returned 50 instead it returned "+v);
//                    else
//                        assertCondition (v == -1); // position() not supported for LOB locators.
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }


/**
Verify Clobs live beyond the life of the row.
**/
    public void Var048()
    {
        if (checkJdbc20 ())
        {
            if (checkLobSupport ())
            {
                Statement s = null;
                try
                {
                    s = connection_.createStatement();
		    String sql="SELECT * FROM " + TABLE2_+" A ORDER BY RRN(A)"; 
                    ResultSet rs = statement2_.executeQuery (sql);

                    rs.next();
                    Blob blob1 = rs.getBlob ("C_BLOB");
                    rs.next();
                    Blob blob2 = rs.getBlob ("C_BLOB");
                    rs.next();
                    Blob blob3 = rs.getBlob ("C_BLOB");

                    if (   (JDLobGraphicData.checkResultsBytes(biggerBlob,  blob1.getBytes(1, (int) blob1.length())))
                           && (JDLobGraphicData.checkResultsBytes(biggerBlob2, blob2.getBytes(1, (int) blob2.length())))
                           && (JDLobGraphicData.checkResultsBytes(biggerBlob3, blob3.getBytes(1, (int) blob3.length()))))
                        assertCondition(true);
                    else
                        failed("data mismatch sql was "+sql );


		    //
		    // As of 10/12/2009 on V6R1, the locator seems to be gone after the
		    // result set is closed.  Move the result set close after test
		    // 
		    try {
			rs.close();
		    } catch (Exception e) {

		    } 
                }
                catch (Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
                if (s != null) try { 
                        s.close();
                    }
                    catch (Exception e) {
                    }
            }
        }
    }


/**
Verify blobs live beyond the life of the row.
**/
    public void Var049()
    {
        if (checkJdbc20 ())
        {
            if (checkLobSupport ())
            {
                Statement s = null;
                try
                {
                    s = connection_.createStatement();
		    String sql = "SELECT * FROM " + TABLE2_+" A ORDER BY RRN(A) "; 
                    ResultSet rs = statement2_.executeQuery (sql);

                    rs.next();
                    Blob blob1 = rs.getBlob ("C_BLOB");
                    rs.next();
                    Blob blob2 = rs.getBlob ("C_BLOB");
                    rs.next();
                    Blob blob3 = rs.getBlob ("C_BLOB");

                    InputStream in1 = blob1.getBinaryStream();      
                    InputStream in2 = blob2.getBinaryStream();      
                    InputStream in3 = blob3.getBinaryStream();      

                    BufferedInputStream bin1 = new BufferedInputStream(in1);
                    BufferedInputStream bin2 = new BufferedInputStream(in2);
                    BufferedInputStream bin3 = new BufferedInputStream(in3);     

                    byte[] b1 = new byte[(int)blob1.length()];
                    byte[] b2 = new byte[(int)blob2.length()];
                    byte[] b3 = new byte[(int)blob3.length()];

                    int c;
                    int i = 0;
                    while (0 <= (c = bin1.read()))
                    {
                        b1[i] = (byte) c;
                        i++;
                    }

                    i = 0;
                    while (0 <= (c = bin2.read()))
                    {
                        b2[i] = (byte) c;
                        i++;
                    }

                    i = 0;
                    while (0 <= (c = bin3.read()))
                    {
                        b3[i] = (byte) c;
                        i++;
                    }

                    if (   (JDLobGraphicData.checkResultsBytes(biggerBlob,  b1))
                           && (JDLobGraphicData.checkResultsBytes(biggerBlob2, b2))
                           && (JDLobGraphicData.checkResultsBytes(biggerBlob3, b3)))
                        assertCondition(true);
                    else
                        failed("data mismatch sql was "+sql);

		    //
		    // As of 10/12/2009 on V6R1, the locator seems to be gone after the
		    // result set is closed.  Move the result set close after test
		    // 
		    try {
			rs.close();
		    } catch (Exception e) {

		    } 

                }
                catch (Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
                if (s != null) try { 
                        s.close();
                    }
                    catch (Exception e) {
                    }
            }
        }
    }

    /**
     setBytes(long, byte[]) - Should throw an exception when start is less
     than 0.
     **/
    public void Var050()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {                                                
            try {                                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute (4);                                                          
                Blob blob = rs2_.getBlob ("C_BLOB");                                   
		try { 
		    blob.setBytes (-1, new byte[] { (byte) -4});
		    rs2_.close();                                                               
		    failed ("Didn't throw SQLException");
		} catch (Exception e) {
		    rs2_.close();                                                               
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");               
		}
            }
            catch (Exception e) {                                                           
		failed (e, "Unexpected Exception");                                     
            }
        }
    }                                                                                       

    /**
    setBytes(long, byte[]) - Should throw an exception when start is 0.
    **/
    public void Var051()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {                                                
            try {                                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute (4);                                                          
                Blob blob = rs2_.getBlob ("C_BLOB");
		try { 
		    blob.setBytes (0, new byte[] { (byte) -9, (byte) 3});                                                                            
		    failed ("Didn't throw SQLException");
		} catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");               
		} 
                rs2_.close();                                                               
            }
	    catch (Exception e) {                                                           
		failed (e, "Unexpected Exception");                                     

            }
        }
    }



    /**
    setBytes(long, byte[]) - Should throw an exception when start is greater
    than the length of the lob.
    @CRS - This variation should succeed. Only when you try to write past the
    max column size will it be an exception.
    **/
    public void Var052()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) { 
//            if (getDriver () == JDTestDriver.DRIVER_NATIVE)
//            {
                try {                                                                           
                    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                    rs2_.absolute (4);                                                          
                    Blob blob = rs2_.getBlob ("C_BLOB");                                   
                    blob.setBytes (SMALL_.length+1, new byte[] { (byte) 44});                                                                          
//                    failed ("Didn't throw SQLException");                                       
                    rs2_.close();                                                               
                    succeeded();
                }
                catch (Exception e) {                                                           
//                    try {                                                                       
//                        assertExceptionIsInstanceOf (e, "java.sql.SQLException");               
//                        rs2_.close();                                                           
//                    }
//                    catch (Exception s) {                                                       
                        failed (e, "Unexpected Exception");                                     
//                    }
                }
/*            }
            else if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
            {
                //@C4C Because Toolbox only knows the max length of a lob, we will
                //@C4C not throw an exception if the user asks for a situation where the start
                //@C4C is greater than the length of the lob until updateRow() is called.
                try { 
                    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                    rs2_.absolute (4);                                                          
                    Blob blob = rs2_.getBlob ("C_BLOB");                                   
                    blob.setBytes (SMALL_.length+1, new byte[] { (byte) 44});
                    rs2_.updateBlob("C_BLOB", blob);
                    rs2_.updateRow();
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
*/            
        }
    }


    /**
    setBytes(long, byte[]) - Should throw an exception when length is less 
    than 0 for the number of bytes to write.
    **/
    public void Var053()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {                                                
            try {                                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute (4);                                                          
                Blob blob = rs2_.getBlob ("C_BLOB");                                   
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
    public void Var054()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {                                                
            try {                                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute (4);                                                          
                Blob blob = rs2_.getBlob ("C_BLOB");
                byte[] expected = new byte[] { (byte) 56, (byte) 65}; 
                int written = blob.setBytes (3, expected);
                rs2_.updateBlob("C_BLOB", blob);            //@C2A
                rs2_.updateRow();                                //@C2A
                rs2_.close();

		// Call commit to free the locator 
		connection_.commit(); 

                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute(4);                                                           
                Blob blob1 = rs2_.getBlob ("C_BLOB");                                  
                byte[] b = blob1.getBytes (3, 2);                                           
                assertCondition(areEqual (b, expected) && written == 2,
				"written("+written+")!= 2 OR "+
				"b("+dumpBytes(b)+")!="+dumpBytes(expected));                    
                rs2_.close();
		connection_.commit(); 

            }
            catch (Exception e) {                                                       
                failed (e, "Unexpected Exception");                                     
            }
        }
    }

    /**
    setBytes(long, byte[]) - Should work to set all of a non-empty lob.
    **/
    public void Var055()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {                                                
            try {                                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute (4);                                                          
                Blob blob = rs2_.getBlob ("C_BLOB");                                   
                byte[] expected = new byte[] { (byte) 57, (byte) 58, (byte) 98};           
                int written = blob.setBytes (1, expected);
                rs2_.updateBlob("C_BLOB", blob);            //@C2A
                rs2_.updateRow();                                //@C2A
                rs2_.close ();
		// Call commit to free the locator 
		connection_.commit(); 

                                                            
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute(4);                                                           
                Blob blob1 = rs2_.getBlob ("C_BLOB");                                  
                byte[] b = blob1.getBytes (1, 3);                                           
                assertCondition(areEqual (b, expected) && written == 3,
				"written("+written+")!= 3 OR "+
				"b("+dumpBytes(b)+")!="+dumpBytes(expected));  //@C2C                  
                rs2_.close();
		connection_.commit(); 
            }
            catch (Exception e) {                                                           
                failed (e, "Unexpected Exception");                                         
            }
        }
    }


    /**
    setBytes(long, byte[]) - Should work to set the last part of a non-empty lob.
    **/
    public void Var056()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {                                                
            try {                                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute (4);                                                          
                Blob blob = rs2_.getBlob ("C_BLOB");                                   
                byte[] expected = new byte[] { (byte) 578};                                
                int written = blob.setBytes (3, expected); 
                rs2_.updateBlob("C_BLOB", blob);            //@C2A
                rs2_.updateRow();                                //@C2A
                rs2_.close ();
		// Call commit to free the locator 
		connection_.commit(); 
                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute(4);                                                           
                Blob blob1 = rs2_.getBlob("C_BLOB");                                   
                byte[] b = blob1.getBytes (3, 1);                                           
                assertCondition( areEqual (b, expected) && written == 1,
				 "written("+written+")!= 1 OR "+
				 "b("+dumpBytes(b)+")!="+dumpBytes(expected));    //@C2C                 
                rs2_.close();
		connection_.commit(); 
            }
            catch (Exception e) {                                                           
                failed (e, "Unexpected Exception");                                         
            }
        }
    }



    /**
    setBytes(long, byte[]) - Should work to set the middle part of a non-empty lob.
    **/
    public void Var057()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {                                                
            try {                                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute (4);                                                          
                Blob blob = rs2_.getBlob ("C_BLOB");                                   
                byte[] expected = new byte[] { (byte) 578};                                
                int written = blob.setBytes (2, expected); 
                rs2_.updateBlob("C_BLOB", blob);            //@C2A
                rs2_.updateRow();                                //@C2A
                rs2_.close ();

		// Call commit to free the locator 
		connection_.commit(); 
                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute(4);                                                           
                Blob blob1 = rs2_.getBlob("C_BLOB");                                   
                byte[] b = blob1.getBytes (2, 1);                                           
                assertCondition( areEqual (b, expected) && written == 1,
				 "written("+written+")!= 1 OR "+
				 "b("+dumpBytes(b)+")!="+dumpBytes(expected));  //@C2C                   
                rs2_.close();
		connection_.commit(); 
            }
            catch (Exception e) {                                                           
                failed (e, "Unexpected Exception");                                         
            }
        }
    }  



    /**
    setBytes(long, byte[]) - Should work to set the first part of a non-empty lob.
    **/
    public void Var058()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {                                                
            try {                                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute (4);                                                          
                Blob blob = rs2_.getBlob ("C_BLOB");                                   
                byte[] expected = new byte[] { (byte) 578};                                
                int written = blob.setBytes (1, expected);
                rs2_.updateBlob("C_BLOB", blob);            //@C2A
                rs2_.updateRow();                                //@C2A
                rs2_.close ();

		// Call commit to free the locator 
		connection_.commit(); 
                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute(4);                                                           
                Blob blob1 = rs2_.getBlob("C_BLOB");                                   
                byte[] b = blob1.getBytes (1, 1);                                           
                assertCondition( areEqual (b, expected) && written == 1,
				 "written("+written+")!= 1 OR "+
				 "b("+dumpBytes(b)+")!="+dumpBytes(expected));   //@C2C                  
                rs2_.close();
		connection_.commit(); 
            }
            catch (Exception e) {                                                           
                failed (e, "Unexpected Exception");                                         
            }
        }
    }


    /**
    setBytes(long, byte[]) - Should throw exception because lob was truncated in var. 58.
    @CRS - This variation should succeed. Only when you try to write past the
    max column size will it be an exception.
    **/
    public void Var059()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {                                                
            try {                                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute (4);                                                          
                Blob blob = rs2_.getBlob ("C_BLOB");                                   
                byte[] expected = new byte[] { (byte) 578};                                
                int written = blob.setBytes (2, expected);
                rs2_.updateBlob("C_BLOB", blob);            
//                failed ("Didn't throw SQLException");
                assertCondition(written==1, "written="+written+" sb 1 Updated 06/01/06");
                /* succeeded(); */
            }
            catch (Exception e) {                                                           
//                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
              failed(e, "Unexpected exception");
            }
        }
    }



    /**
    setBytes(long, byte[], int, int) - Should throw an exception when start is less
    than 0.
    **/
    public void Var060()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {                                                
            try {                                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute (4);                                                          
                Blob blob = rs2_.getBlob ("C_BLOB");                                   
                blob.setBytes (-1, new byte[] { (byte) -4}, 1, 1);  
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
    public void Var061()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {                                                
            try {                                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute (4);                                                          
                Blob blob = rs2_.getBlob ("C_BLOB");                                   
                blob.setBytes (0, new byte[] { (byte) -9, (byte) 3}, 1, 2);                                                                     
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
    **/
    public void Var062()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {    
            if (getDriver() == JDTestDriver.DRIVER_NATIVE)
            {
                try {                                                                           
                    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                    rs2_.absolute (4);                                                          
                    Blob blob = rs2_.getBlob ("C_BLOB");
                    blob.setBytes (blob.length()+1, new byte[] { (byte) 44}, 1, 1); //@C2C length changed
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

            else if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
            {
                //@C4C Because Toolbox only knows the max length of a lob, we will
                //@C4C not throw an exception if the user asks for a situation where the start
                //@C4C is greater than the length of the lob until updateRow() is called.
                try { 
                    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                    rs2_.absolute (4);                                                          
                    Blob blob = rs2_.getBlob ("C_BLOB");                                   
                    blob.setBytes (blob.length()+1, new byte[] { (byte) 44}, 1, 1); //@C2C length changed
                    rs2_.updateBlob("C_BLOB", blob);
                    rs2_.updateRow();
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
    }


    //@C2C Changed testcase to not expect an exception if the writing of the bytes will extend
    //@C2C past the end of the lob.
    /**
    setBytes(long, byte[], int, int) - Should not throw an exception when length of bytes to write 
    extends past the length lob as long as start is not passed the end of the lob.
    **/
    public void Var063()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {                                                
            try {                                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute (4);                        
                byte[] expected = new byte[] { (byte) 56, (byte) 65, (byte) 76, (byte) 89};
                Blob blob = rs2_.getBlob ("C_BLOB");                                   
                int written = blob.setBytes (1, expected, 0, 4); //@C5 changed third parameter from 1 to 0
                rs2_.updateBlob("C_BLOB", blob);            //@C2A
                rs2_.updateRow();                                //@C2A
                rs2_.close();

		// Call commit to free the locator 
		connection_.commit(); 
                                                             
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute(4);                                                           
                Blob blob1 = rs2_.getBlob("C_BLOB");                                   
                byte[] b = blob1.getBytes (1, 4);                                           
                assertCondition( areEqual (b, expected) && written == 4,
				 "written("+written+")!= 4 OR "+
				 "b("+dumpBytes(b)+")!="+dumpBytes(expected));   //@C2C                  
                rs2_.close();
		connection_.commit(); 
            }
            catch (Exception e) {                                                           
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    setBytes(long, byte[], int, int) - Should work to set all of a non-empty lob.
    **/
    public void Var064()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {                                                
            try {                                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute (4);                                                          
                Blob blob = rs2_.getBlob ("C_BLOB");                                   
                byte[] expected = new byte[] { (byte) 57, (byte) 58, (byte) 98};           
                int written = blob.setBytes (1, expected, 0, 3); //@C5 changed third parameter from 1 to 0 
                rs2_.updateBlob("C_BLOB", blob);            //@C2A
                rs2_.updateRow();                                //@C2A
                rs2_.close ();

		// Call commit to free the locator 
		connection_.commit(); 
                                                             
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute(4);                                                           
                Blob blob1 = rs2_.getBlob("C_BLOB");                                   
                byte[] b = blob1.getBytes (1, 3);                                           
                assertCondition( areEqual (b, expected) && written == 3,
				 "written("+written+")!= 3 OR "+
				 "b("+dumpBytes(b)+")!="+dumpBytes(expected));    //@C2C                
                rs2_.close();
		connection_.commit(); 
            }
            catch (Exception e) {                                                           
                failed (e, "Unexpected Exception");                                         
            }
        }
    }


    /**
    setBytes(long, byte[], int, int) - Should work to set the middle part of a non-empty lob.
    **/
    public void Var065()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {                                                
            try {                                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute (4);                                                          
                Blob blob = rs2_.getBlob ("C_BLOB");                                   

                byte[] expected = new byte[] { (byte) 578, (byte) 654};                            
                int written = blob.setBytes (2, expected, 0, 2); //@C5 changed third parameter from 1 to 0 
                rs2_.updateBlob("C_BLOB", blob);            //@C2A
                rs2_.updateRow();                                //@C2A
                rs2_.close ();

		// Call commit to free the locator 
		connection_.commit(); 
                                                            
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute(4);                                                           
                Blob blob1 = rs2_.getBlob("C_BLOB");                                   
                byte[] b = blob1.getBytes (2, 2);                                           
                assertCondition( areEqual (b, expected) && written == 2,
				 "written("+written+")!= 2 OR "+
				 "b("+dumpBytes(b)+")!="+dumpBytes(expected));  //@C2C                   
                rs2_.close();
		connection_.commit(); 
            }
            catch (Exception e) {                                                           
                failed (e, "Unexpected Exception");                                         
            }
        }
    } 


    /**
setBytes(long, byte[], int, int) - Should work to set the last part of a non-empty lob.
**/
    public void Var066()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {                                                
            try {                                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute (4);                                                          
                Blob blob = rs2_.getBlob ("C_BLOB");   
                byte[] expected = new byte[] { (byte) 578};                                
                int written = blob.setBytes (3, expected, 0, 1); //@C5 changed third parameter from 1 to 0 
                rs2_.updateBlob("C_BLOB", blob);            //@C2A
                rs2_.updateRow();                                //@C2A
                rs2_.close ();

		// Call commit to free the locator 
		connection_.commit(); 
                                                             
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute(4);                                                           
                Blob blob1 = rs2_.getBlob("C_BLOB");                                   
                byte[] b = blob1.getBytes (3, 1);                                           
                assertCondition( areEqual (b, expected) && written == 1,
				 "written("+written+")!= 1 OR "+
				 "b("+dumpBytes(b)+")!="+dumpBytes(expected));   //@C2C                  
                rs2_.close();
		connection_.commit(); 
            }
            catch (Exception e) {                                                           
                failed (e, "Unexpected Exception");                                         
            }
        }
    }



    /**
    setBytes(long, byte[], int, int) - Should work to set the first part of a non-empty lob.
    **/
    public void Var067()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {                                                
            try {                                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute (4);                                                          
                Blob blob = rs2_.getBlob ("C_BLOB");     
                byte[] expected = new byte[] { (byte) 578};                                
                int written = blob.setBytes (1, expected, 0, 1);  //@C5 changed third parameter from 1 to 0
                rs2_.updateBlob("C_BLOB", blob);            //@C2A
                rs2_.updateRow();                                //@C2A
                rs2_.close ();

		// Call commit to free the locator 
		connection_.commit(); 
                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute(4);                                                           
                Blob blob1 = rs2_.getBlob("C_BLOB");                                   
                byte[] b = blob1.getBytes (1, 1);                                           
                assertCondition( areEqual (b, expected) && written == 1,
				 "written("+written+")!= 1 OR "+
				 "b("+dumpBytes(b)+")!="+dumpBytes(expected));    //@C2C                
                rs2_.close();
		connection_.commit(); 
            }
            catch (Exception e) {                                                           
                failed (e, "Unexpected Exception");                                         
            }
        }
    }


    /**
    setBytes(long, byte[], int, int) - Should throw exception because the lob was truncated in var. 67.
     @D1: should not expect an exception because expansion is allowed till maxLength of BLOB
    **/
    public void Var068()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {                                                
            try {                                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute (4);                                                          
                Blob blob = rs2_.getBlob ("C_BLOB");               
                byte[] expected = new byte[] { (byte) 578};                                
                int written = blob.setBytes (2, expected, 0, 1);  // @D1: changed offset from 1 to 0 .... setBytes(2, expected, 1, 1);
                rs2_.updateBlob("C_BLOB", blob);

		if(getRelease() >=  JDTestDriver.RELEASE_V5R3M0 || v5r2nativeFunctions_) //@K2D && getDriver () == JDTestDriver.DRIVER_NATIVE)	// @D1
		    assertCondition(written == 1);								// @D1
		else												// @D1
		    failed ("Didn't throw SQLException");                                                               
            }
            catch (Exception e) {                                                           
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }




    /**
    setBinaryStream() - When the lob is not empty.
    **/
    public void Var069()                                                        
    {          
        if (checkUpdateableLobsSupport ()) {                                    
            try {                                                               
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_); 
/*                rs2_.absolute (2);                                              
                Blob blob2 = rs2_.getBlob ("C_BLOB");
                rs2_.absolute (3);                                              
                Blob blob3 = rs2_.getBlob ("C_BLOB");
  */              rs2_.absolute (4);                                              
                Blob blob = rs2_.getBlob ("C_BLOB");   
                OutputStream v = blob.setBinaryStream (1);                      
                byte [] expected = new byte[] {(byte) 6, (byte) 7, (byte) 8};
                v.write(expected);                                           
                v.close();                                                      
                rs2_.updateBlob("C_BLOB", blob);                           
                rs2_.updateRow();                                               
                rs2_.close();

		// Call commit to free the locator 
		connection_.commit(); 
                                            
                rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);      
                rs2_.absolute(4);                                               
                Blob blob1 = rs2_.getBlob("C_BLOB");                       
                InputStream inputStream = blob1.getBinaryStream();
		byte[] actualExp = new byte[] {(byte) 6, (byte) 7, (byte) 8, (byte)89}; // 4th byte from var063   @D1
		sb.setLength(0); 
		if(getRelease() >=  JDTestDriver.RELEASE_V5R3M0					// @D3
		   && getDriver () == JDTestDriver.DRIVER_NATIVE)			// @D1	// @D3
		    assertCondition(compareBeginsWithBytes( inputStream, actualExp,sb),sb);	// @D1	// @D3
		else									// @D1	// @D3
		    assertCondition (compare (inputStream, expected,sb),sb); 
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
    public void Var070()                                                        
    {                                                                           
        if (checkUpdateableLobsSupport ())
        {                                                                       
            try
            {                                                                                                                                    
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);        
                rs2_.absolute(4);                                               
                Blob blob = rs2_.getBlob ("C_BLOB");                       
                blob.setBinaryStream (blob.length()+1);    
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

    /**
    setBinaryStream() - Should throw an exception if you try to set a stream
    that starts at 0.
    **/
    public void Var071()                                                        
    {                                                                           
        if (checkUpdateableLobsSupport ())
        {                                                                       
            try
            {                                                                                                                                    
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);        
                rs2_.absolute(4);                                               
                Blob blob = rs2_.getBlob ("C_BLOB");                       
                OutputStream v = blob.setBinaryStream (0);                      
                failed("Didn't throw SQLException "+v);                            
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
    public void Var072()                                                        
    {                                                                           
        if (checkUpdateableLobsSupport ())
        {                                                                       
            try
            {                                                                                                                                    
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);        
                rs2_.absolute(4);                                               
                Blob blob = rs2_.getBlob ("C_BLOB");                       
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
    public void Var073()                                                        
    {                                                                           
        if (checkUpdateableLobsSupport ())
        {                                                                       
            try
            {                                                                                                                                    
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);        
                rs2_.absolute(4);                                               
                Blob blob = rs2_.getBlob ("C_BLOB");                       
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
    public void Var074()                                                        
    {                                                                           
        if (checkUpdateableLobsSupport ())
        {   
//            if (getDriver () == JDTestDriver.DRIVER_NATIVE)
//            {
                try
                {                                                                                                                                    
                    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);        
                    rs2_.absolute(4);                                               
                    Blob blob = rs2_.getBlob ("C_BLOB");                       
                    blob.truncate (5);
		    if (v5r2nativeFunctions_) { 
		    failed("Didn't throw SQLException");
		    } else { 
			rs2_.close();                                                   
			succeeded();
		    }
                }
                catch (Exception e) {
		    if (v5r2nativeFunctions_) { 
                     try {                                                           
                         assertExceptionIsInstanceOf (e, "java.sql.SQLException");   
                         rs2_.close();                                               
                     }
                     catch (Exception s) {                                           
                        failed (e, "Unexpected Exception");                         
                     }
		    } else {
                        failed (e, "Unexpected Exception");                         
		    } 
                }
/*            }
            else if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
            {
                //@C4C Because Toolbox only knows the max length of a lob, we will
                //@C4C not throw an exception if the user truncates to a length 
                //@C4C greater than the end of the lob until updateRow() is called.
                try { 
                    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                    rs2_.absolute (4);                                                          
                    Blob blob = rs2_.getBlob ("C_BLOB");                                   
                    blob.truncate (5);
                    rs2_.updateBlob("C_BLOB", blob);
                    rs2_.updateRow();
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
*/
        }
    }                                                                           


    /**
    truncate() - Should work on a non-empty lob.
    **/
    public void Var075()                                                        
    {                                                                           
        if (checkUpdateableLobsSupport ())
        {                                                                       
            try
            {                                                                                                                                    
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);        
                rs2_.absolute(4);                                               
                Blob blob = rs2_.getBlob ("C_BLOB");                       
                blob.truncate (2); 
                rs2_.updateBlob("C_BLOB", blob);            //@C2A
                rs2_.updateRow();                                //@C2A
                rs2_.close ();                                                  

		// Call commit to free the locator 
		connection_.commit(); 
                                            
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);        
                rs2_.absolute(4);                                               
                Blob blob1 = rs2_.getBlob("C_BLOB");
                assertCondition (blob1.length() == 2, "blob1.length is "+blob1.length()+" sb 2");                          
                rs2_.close();
		connection_.commit(); 
            }
            catch (Exception e)
            {                                                                   
                failed(e, "Unexpected Exception");                              
            }
        }
    }    


    /**
setBytes(long, byte[]) - Should update blob with an unchanged blob.
**/
    public void Var076()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {
            try {
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);
                rs2_.absolute (2);  
                Blob blob = rs2_.getBlob ("C_BLOB");                                   
                rs2_.updateBlob("C_BLOB", blob); 
                rs2_.updateRow();
                rs2_.close ();                                                              

		// Call commit to free the locator 
		connection_.commit(); 
                                            
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute(2);                                       
                Blob blob1 = rs2_.getBlob ("C_BLOB");
                byte[] v = blob1.getBytes (1, MEDIUM_.length);
                assertCondition (isEqual (v, MEDIUM_));                                                          
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");                                         
            }
        }
    }  

    /**
    setBytes(long, byte[]) - Should not change lob if updateRow() is not called.
    **/
    public void Var077()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {
            try {
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);
                rs2_.absolute (2);  
                Blob blob = rs2_.getBlob ("C_BLOB");                                   
                rs2_.updateBlob("C_BLOB", blob);            
                rs2_.close ();                                                              

		// Call commit to free the locator 
		connection_.commit(); 
                                            
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute(2);                                       
                Blob blob1 = rs2_.getBlob ("C_BLOB");
                byte[] v = blob1.getBytes (1, MEDIUM_.length);
                assertCondition (isEqual (v, MEDIUM_));

		rs2_.close();
		connection_.commit(); 

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");                                         
            }
        }
    }


    /**
    setBytes(long, byte[]) - Make sure lob in database is not changed if updateRow() was not called.
    **/
    public void Var078()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {
            try {
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);
                rs2_.absolute (2);  
                Blob blob = rs2_.getBlob ("C_BLOB");                                   
                byte[] expected = new byte[] { (byte) 35};                                
                blob.setBytes (2, expected); 
                rs2_.updateBlob("C_BLOB", blob); 
                rs2_.close ();                                                              

		// Call commit to free the locator 
		connection_.commit(); 
                                            
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute(2);                                       
                Blob blob1 = rs2_.getBlob ("C_BLOB");
                byte[] v = blob1.getBytes (1, MEDIUM_.length);
                assertCondition (isEqual (v, MEDIUM_));
                rs2_.close();
		connection_.commit(); 
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");                                         
            }
        }
    }  


    /**
    setBytes(long, byte[], int, int) - Should work to set bytes to a non-empty lob with multiple
    updates to the same position.
    **/
    public void Var079()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {                                                
            try {                                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute (4);                                                          
                Blob blob = rs2_.getBlob ("C_BLOB");                                   
                byte[] expected = new byte[] { (byte) 57, (byte) 58, (byte) 98};           
                int written = blob.setBytes (1, expected, 0, 3);//@C5 changed third parameter from 1 to 0 
                byte[] expected2 = new byte[] { (byte) 64, (byte) 65, (byte) 75};           
                int written2 = blob.setBytes (1, expected2, 0, 3);//@C5 changed third parameter from 1 to 0 
                rs2_.updateBlob("C_BLOB", blob);            
                rs2_.updateRow();                                
                rs2_.close ();                                                              

		// Call commit to free the locator 
		connection_.commit(); 
                                            
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute(4);                                                           
                Blob blob1 = rs2_.getBlob("C_BLOB");                                   
                byte[] b = blob1.getBytes (1, 3);
                assertCondition( areEqual (b, expected2) && written == 3 && written2 == 3,
				 "written("+written+")!= 3 OR "+
				 "written2("+written2+")!= 3 OR "+
				 "b("+dumpBytes(b)+")!="+dumpBytes(expected2));       
                rs2_.close();
		connection_.commit(); 
            }
            catch (Exception e) {                                                           
                failed (e, "Unexpected Exception");                                         
            }
        }
    }


    /**
    setBytes(long, byte[], int, int) - Should work to set bytes to a non-empty lob with multiple
    updates to the different positions.
    **/
    public void Var080()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {                                                
            try {                                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute (4);                                                          
                Blob blob = rs2_.getBlob ("C_BLOB");                                   
                byte[] bytes = new byte[] { (byte) 57, (byte) 58, (byte) 98};           
                int written = blob.setBytes (1, bytes, 0, 3);//@C5 changed third parameter from 1 to 0 
                byte[] bytes2 = new byte[] { (byte) 64, (byte) 65, (byte) 75};           
                int written2 = blob.setBytes (3, bytes2, 0, 3);//@C5 changed third parameter from 1 to 0 
                byte[] expected = new byte[] {(byte) 57, (byte) 58, (byte) 64, (byte) 65, (byte) 75};
                rs2_.updateBlob("C_BLOB", blob);            
                rs2_.updateRow();                                
                rs2_.close ();                                                              
 
		// Call commit to free the locator 
		connection_.commit(); 
                                            
               rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute(4);                                                           
                Blob blob1 = rs2_.getBlob("C_BLOB");                                   
                byte[] b = blob1.getBytes (1, 5);                                           
		assertCondition( areEqual (b, expected) && written == 3 && written2 == 3,
				 "written("+written+")!= 3 OR "+
				 "written2("+written2+")!= 3 OR "+
				 "b("+dumpBytes(b)+")!="+dumpBytes(expected));       
                rs2_.close();
		connection_.commit(); 
            }
            catch (Exception e) {                                                           
                failed (e, "Unexpected Exception");                                         
            }
        }
    }



    /**
   setBytes(long, byte[], int, int) - Should work to set bytes to a non-empty lob with multiple
   updates to the same position.
   **/
    public void Var081()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {                                                
            try {                                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute (4);                                                          
                Blob blob = rs2_.getBlob ("C_BLOB");                                   
                byte[] expected = new byte[] { (byte) 57, (byte) 58, (byte) 98};           
                int written = blob.setBytes (1, expected, 0, 3);//@C5 changed third parameter from 1 to 0 
                rs2_.updateBlob("C_BLOB", blob);
                byte[] expected2 = new byte[] { (byte) 64, (byte) 65, (byte) 75};           
                int written2 = blob.setBytes (1, expected2, 0, 3);//@C5 changed third parameter from 1 to 0 
                rs2_.updateBlob("C_BLOB", blob);            
                rs2_.updateRow();                                
                rs2_.close ();                                                              

		// Call commit to free the locator 
		connection_.commit(); 
                                            
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute(4);                                                           
                Blob blob1 = rs2_.getBlob("C_BLOB");                                   
                byte[] b = blob1.getBytes (1, 3);
                assertCondition( areEqual (b, expected2) && written == 3 && written2 == 3,
				 "written("+written+")!= 3 OR "+
				 "written2("+written2+")!= 3 OR "+
				 "b("+dumpBytes(b)+")!="+dumpBytes(expected2));       
                rs2_.close();
		connection_.commit(); 
            }
            catch (Exception e) {                                                           
                failed (e, "Unexpected Exception");                                         
            }
        }
    }

    /**
    setBytes(long, byte[], int, int) - Should work to set bytes to a non-empty lob with multiple
    updates to the different positions with updateBlob calls between.
    **/
    public void Var082()                                                                    
    {                                                                                       
        if (checkUpdateableLobsSupport ()) {                                                
            try {                                                                           
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute (4);                                                          
                Blob blob = rs2_.getBlob ("C_BLOB");                                   
                byte[] bytes = new byte[] { (byte) 57, (byte) 58, (byte) 98};           
                int written = blob.setBytes (1, bytes, 0, 3);//@C5 changed third parameter from 1 to 0
                byte[] bytes2 = new byte[] { (byte) 64, (byte) 65, (byte) 75};
                rs2_.updateBlob("C_BLOB", blob);
                int written2 = blob.setBytes (3, bytes2, 0, 3);//@C5 changed third parameter from 1 to 0
                byte[] expected = new byte[] {(byte) 57, (byte) 58, (byte) 64, (byte) 65, (byte) 75};
                rs2_.updateBlob("C_BLOB", blob);            
                rs2_.updateRow();                                
                rs2_.close ();                                                              

		// Call commit to free the locator 
		connection_.commit(); 
                                            
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
                rs2_.absolute(4);                                                           
                Blob blob1 = rs2_.getBlob("C_BLOB");                                   
                byte[] b = blob1.getBytes (1, 5);                                           
                assertCondition( areEqual (b, expected) && written == 3 && written2 == 3,
				 "written("+written+")!= 3 OR "+
				 "written2("+written2+")!= 3 OR "+
				 "b("+dumpBytes(b)+")!="+dumpBytes(expected));       
                rs2_.close();
		connection_.commit(); 
            }
            catch (Exception e) {                                                           
                failed (e, "Unexpected Exception");                                         
            }
        }
    }


        /**
    setBinaryStream() - When the lob is not empty to position 2.
    **/
    public void Var083()                                                        
    {                                                                           
        if (checkUpdateableLobsSupport ()) {                                    
            try {                                                               
                rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);        
                rs2_.absolute (4);                                              
                Blob blob = rs2_.getBlob ("C_BLOB");                       
                OutputStream v = blob.setBinaryStream (1);  
                byte [] expected = new byte[] {(byte) 10, (byte) 11, (byte) 12};
                v.write(expected);                                           
                v.close();                                                      
                rs2_.updateBlob("C_BLOB", blob);                           
                rs2_.updateRow();                                               
                rs2_.close();                                                   

		// Call commit to free the locator 
		connection_.commit(); 
                                            
                rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE_);      
                rs2_.absolute(4);                                               
                Blob blob1 = rs2_.getBlob("C_BLOB");                       
                InputStream inputStream = blob1.getBinaryStream();

                //We are starting from position 2 and variation 69 should
                //have left a (byte)6 in position 1.

		byte [] newBytes = new byte[] {(byte) 10, (byte) 11, (byte) 12, (byte)65, (byte)75}; // from Var082: last 2 bytes @D1
    sb.setLength(0); 
		if(getRelease() >=  JDTestDriver.RELEASE_V5R3M0					// @D3
		   && getDriver () == JDTestDriver.DRIVER_NATIVE)			// @D1	// @D3
		    assertCondition(compareBeginsWithBytes (inputStream, newBytes,sb),sb);	// @D1	// @D3
		else										// @D3
		    assertCondition (compare (inputStream, expected,sb),sb); 
            }
            catch (Exception e) {                                               
                failed (e, "Unexpected Exception");                             
            }
        }
    } 

    //@K1A
    /**
    setBytes(long, byte[]) - Should now throw an exception when length of bytes to write extends
    past the length of the lob as long as start is not passed the end of the lob.
    **/
    public void Var084()                                                                    
    {
	if (v5r2nativeFunctions_) {
	    notApplicable("Testcase added for V5R3"); 
	} else { 
	    if (checkUpdateableLobsSupport ()) {                                                
		try {                                                                           
		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2_.absolute (3);                                                          
		    Blob blob = rs2_.getBlob ("C_BLOB");
		    long length1 = blob.length();
		    byte[] expected = new byte[] { (byte) 56, (byte) 65}; 
		    blob.setBytes (3, expected);
		    rs2_.updateBlob("C_BLOB", blob);            
		    rs2_.updateRow();                                
		    rs2_.close();

 		    // Call commit to free the locator 
		    connection_.commit(); 

		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2_.absolute(3);                                                           
		    Blob blob1 = rs2_.getBlob ("C_BLOB");                                  
		    long length2 = blob1.length();
		    assertCondition( length1 == length2,""+length1+" == "+length2 );                    
		    rs2_.close();
		    connection_.commit(); 
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
    public void Var085()                                                                    
    {                                                                                       
	if (v5r2nativeFunctions_) {
	    notApplicable("Testcase added for V5R3"); 
	} else { 
	    if (checkUpdateableLobsSupport ()) {                                                
		try {                                                                           
		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2_.absolute (3);                                                          
		    Blob blob = rs2_.getBlob ("C_BLOB");                                   
		    long length1 = blob.length();
		    byte[] expected = new byte[] { (byte) 57, (byte) 58, (byte) 98};           
		    blob.setBytes (1, expected);
		    rs2_.updateBlob("C_BLOB", blob);            
		    rs2_.updateRow();                                
		    rs2_.close ();                                                              

 		    // Call commit to free the locator 
		    connection_.commit(); 

		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2_.absolute(3);                                                           
		    Blob blob1 = rs2_.getBlob ("C_BLOB");                                  
		    long length2 = blob1.length();
		    assertCondition( length1 == length2,""+length1+" == "+length2 );                 
		    rs2_.close();
		    connection_.commit(); 
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
    public void Var086()                                                                    
    {
	if (v5r2nativeFunctions_) {
	    notApplicable("Testcase added for V5R3"); 
	} else { 

	    if (checkUpdateableLobsSupport ()) {                                                
		try {                                                                           
		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2_.absolute (3);                                                          
		    Blob blob = rs2_.getBlob ("C_BLOB");                                   
		    long length1 = blob.length();
		    byte[] expected = new byte[] { (byte) 578};                                
		    blob.setBytes (3, expected); 
		    rs2_.updateBlob("C_BLOB", blob);           
		    rs2_.updateRow();                                
		    rs2_.close ();                                                              

 		    // Call commit to free the locator 
		    connection_.commit(); 

		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2_.absolute(3);                                                           
		    Blob blob1 = rs2_.getBlob("C_BLOB");                                   
		    long length2 = blob1.length();
		    assertCondition( length1 == length2 );   
		    rs2_.close();
		    connection_.commit(); 
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
    public void Var087()                                                                    
    {                                                                                       
	if (v5r2nativeFunctions_) {
	    notApplicable("Testcase added for V5R3"); 
	} else { 
	    if (checkUpdateableLobsSupport ()) {                                                
		try {                                                                           
		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2_.absolute (2);                                                          
		    Blob blob = rs2_.getBlob ("C_BLOB");                                   
		    long length1 = blob.length();
		    byte[] expected = new byte[] { (byte) 578};                                
		    blob.setBytes (2, expected); 
		    rs2_.updateBlob("C_BLOB", blob);          
		    rs2_.updateRow();                              
		    rs2_.close ();                                                              

 		    // Call commit to free the locator 
		    connection_.commit(); 

		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2_.absolute(2);                                                           
		    Blob blob1 = rs2_.getBlob("C_BLOB");      
		    long length2 = blob1.length();
		    assertCondition( length1 == length2,""+length1+" == "+length2);                   
		    rs2_.close();
		    connection_.commit(); 
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
    public void Var088()                                                                    
    {
	if (v5r2nativeFunctions_) {
	    notApplicable("Testcase added for V5R3"); 
	} else { 

	    if (checkUpdateableLobsSupport ()) {                                                
		try {                                                                           
		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2_.absolute (2);                                                          
		    Blob blob = rs2_.getBlob ("C_BLOB");                                   
		    long length1 = blob.length();
		    byte[] expected = new byte[] { (byte) 578};                                
		    blob.setBytes (1, expected);
		    rs2_.updateBlob("C_BLOB", blob);            
		    rs2_.updateRow();                                
		    rs2_.close ();                                                              

 		    // Call commit to free the locator 
		    connection_.commit(); 

		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2_.absolute(2);                                                           
		    Blob blob1 = rs2_.getBlob("C_BLOB");                                   
		    long length2 = blob1.length();
		    assertCondition( length1 == length2,""+length1+" == "+length2 );    
		    rs2_.close();
		    connection_.commit(); 
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
    public void Var089()                                                                    
    {
	if (v5r2nativeFunctions_) {
	    notApplicable("Testcase added for V5R3"); 
	} else { 

	    if (checkUpdateableLobsSupport ()) {                                                
		try {                                                                           
		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2_.absolute (3);                                                          
		    Blob blob = rs2_.getBlob ("C_BLOB");                                   
		    long length1 = blob.length();
		    byte[] expected = new byte[] { (byte) 57, (byte) 58, (byte) 98};           
		    blob.setBytes (1, expected, 0, 3); 
		    rs2_.updateBlob("C_BLOB", blob);            
		    rs2_.updateRow();                                
		    rs2_.close ();                                                              

 		    // Call commit to free the locator 
		    connection_.commit(); 

		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2_.absolute(3);                                                           
		    Blob blob1 = rs2_.getBlob("C_BLOB");                                   
		    long length2 = blob1.length();
		    assertCondition( length1 == length2 );
		    rs2_.close();
		    connection_.commit(); 
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
    public void Var090()                                                                    
    {
	if (v5r2nativeFunctions_) {
	    notApplicable("Testcase added for V5R3"); 
	} else { 

	    if (checkUpdateableLobsSupport ()) {                                                
		try {                                                                           
		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2_.absolute (2);                                                          
		    Blob blob = rs2_.getBlob ("C_BLOB");                                   
		    long length1 = blob.length();
		    byte[] expected = new byte[] { (byte) 578};                                
		    blob.setBytes (3, expected, 0, 1); 
		    rs2_.updateBlob("C_BLOB", blob);            
		    rs2_.updateRow();                                
		    rs2_.close ();                                                              

 		    // Call commit to free the locator 
		    connection_.commit(); 

		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2_.absolute(2);                                                           
		    Blob blob1 = rs2_.getBlob("C_BLOB");        
		    long length2 = blob1.length();
		    assertCondition( length1 == length2,""+length1+" == "+length2 );    
		    rs2_.close();
		    connection_.commit(); 
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
    public void Var091()                                                                    
    {                                                                                       
	if (v5r2nativeFunctions_) {
	    notApplicable("Testcase added for V5R3"); 
	} else { 
	    if (checkUpdateableLobsSupport ()) {                                                
		try {                                                                           
		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2_.absolute (2);                                                          
		    Blob blob = rs2_.getBlob ("C_BLOB");                                   
		    long length1 = blob.length();
		    byte[] expected = new byte[] { (byte) 578};                                
		    blob.setBytes (2, expected, 0, 1); 
		    rs2_.updateBlob("C_BLOB", blob);            
		    rs2_.updateRow();                                
		    rs2_.close ();                                                              

 		    // Call commit to free the locator 
		    connection_.commit(); 

		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2_.absolute(2);                                                           
		    Blob blob1 = rs2_.getBlob("C_BLOB");       
		    long length2 = blob1.length();
		    assertCondition( length1 == length2,""+length1+" == "+length2 );                 
		    rs2_.close();
		    connection_.commit(); 
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
    public void Var092()                                                                    
    {                                                                                       
	if (v5r2nativeFunctions_) {
	    notApplicable("Testcase added for V5R3"); 
	} else { 
	    if (checkUpdateableLobsSupport ()) {                                                
		try {                                                                           
		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2_.absolute (3);                                                          
		    Blob blob = rs2_.getBlob ("C_BLOB");
		    long length1 = blob.length();                                   
		    byte[] expected = new byte[] { (byte) 578};                                
		    blob.setBytes (1, expected, 0, 1); 
		    rs2_.updateBlob("C_BLOB", blob);            
		    rs2_.updateRow();                               
		    rs2_.close ();                                                              

 		    // Call commit to free the locator 
		    connection_.commit(); 

		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);                    
		    rs2_.absolute(3);                                                           
		    Blob blob1 = rs2_.getBlob("C_BLOB");                                   
		    long length2 = blob1.length();                                           
		    assertCondition( length1 == length2,""+length1+" == "+length2 );                  
		    rs2_.close();
		    connection_.commit(); 
		}
		catch (Exception e) {                                                           
		    failed (e, "Unexpected Exception");                                         
		}
	    }
	}
    }

    //@K1A
    /**
    truncate() - Should work on a non-empty lob.
    **/
    public void Var093()                                                        
    {                                                                           
	if (v5r2nativeFunctions_) {
	    notApplicable("Testcase added for V5R3"); 
	} else { 
	    if (checkUpdateableLobsSupport ())
	    {                                                                       
		try
		{                                                                                                                                    
		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);        
		    rs2_.absolute(2);                                               
		    Blob blob = rs2_.getBlob ("C_BLOB");       
		    long length1 = blob.length();
		    blob.truncate (2); 
		    rs2_.updateBlob("C_BLOB", blob);           
		    rs2_.updateRow();                          
		    rs2_.close ();                                                  

 		    // Call commit to free the locator 
		    connection_.commit(); 

		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);        
		    rs2_.absolute(2);                                               
		    Blob blob1 = rs2_.getBlob("C_BLOB");
		    assertCondition (length1 == 50 && blob1.length() == 2,"length1 = "+length1+" SB 50, blob1.length() =  "+blob1.length()+" SB 2");                          
		    rs2_.close();
		    connection_.commit(); 
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
    setString() - Incoming Data Truncation case works
    **/
    public void Var094()                                                        
    {                                                                           
	if (v5r2nativeFunctions_) {
	    notApplicable("Testcase added for V5R3"); 
	} else { 
	    if (checkUpdateableLobsSupport ())
	    {                                                                       
		try
		{                                                                                                                                    
		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);        
		    rs2_.absolute(2);                                               
		    Blob blob = rs2_.getBlob ("C_BLOB");
		    long length1 = blob.length(); // should be 2 from previous testcase
		    byte[] newByte = new byte[30005];
		    for(int i=0; i<newByte.length; i++)
			newByte[i] = (byte)i;
		    int written = blob.setBytes(1, newByte);
		    rs2_.updateBlob("C_BLOB", blob);           
		    rs2_.updateRow();                          
		    if(getDriver() == JDTestDriver.DRIVER_TOOLBOX)
			failed("Didn't throw SQLException");
		    rs2_.close ();                                                  

 		    // Call commit to free the locator 
		    connection_.commit(); 

		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);        
		    rs2_.absolute(2);                                               
		    Blob blob1 = rs2_.getBlob("C_BLOB");
		    assertCondition (length1 == 2 && blob1.length() == 30000 && written == 30000,
				     "length1 = "+length1+" SB 2, blob1.length() =  "+blob1.length()
				     +" SB 30000, written = "+written+" SB 30000"); 
		    rs2_.close();
		    connection_.commit(); 
		}
		catch (Exception e)
		{                                                                   
		    if(getDriver() == JDTestDriver.DRIVER_TOOLBOX)
			assertExceptionIsInstanceOf(e, "java.sql.SQLException");
		    else
			failed(e, "Unexpected Exception");                              
		}
	    }
	}
    }

   //@D1
    /**
    setString() - Prepadding of incoming data if pos > length_
    **/
    public void Var095() 
    {                                                                           
	if (v5r2nativeFunctions_) {
	    notApplicable("Testcase added for V5R3"); 
	} else { 
	    if (checkUpdateableLobsSupport ())
	    {                                                                       
		try
		{
		// Clearing up a few things of previous testcases
		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);        
		    rs2_.absolute(2);                                              
		    Blob blob = rs2_.getBlob ("C_BLOB");
		    blob.truncate(2);
		    rs2_.updateBlob("C_BLOB", blob);           
		    rs2_.updateRow();                          
		    rs2_.close ();

		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);        
		    rs2_.absolute(2);                                              
		    blob = rs2_.getBlob ("C_BLOB");
		    long length1 = blob.length(); // should be 2 from above cleaning

		    byte[] newByte = new byte[10];
		    for(int i=0; i<newByte.length; i++)
			newByte[i] = (byte)i;
		    blob.setBytes(5, newByte); 
		    rs2_.updateBlob("C_BLOB", blob);           
		    rs2_.updateRow();                          
		    rs2_.close ();                                                  

 		    // Call commit to free the locator 
		    connection_.commit(); 

		    rs2_=statement2_.executeQuery("SELECT * FROM " + TABLE_);        
		    rs2_.absolute(2);                                               
		    Blob blob1 = rs2_.getBlob("C_BLOB");
		    long length2 = blob1.length();
		    byte[] returned = blob1.getBytes(1, (int)length2);

		    boolean success = (newByte.length-1+4 < returned.length);
		    for(int i=0; success && i<newByte.length; i++)
			success = success && (newByte[i] == returned[i+4]);

		    assertCondition (success && length1 == 2 && length2 == 14,
				     "success = "+success+" SB true. "+
				     "length1 = "+length1+" SB 2, length2 =  "+
				     blob1.length()+" SB 14"); 
		    rs2_.close();
		    connection_.commit(); 
		}
		catch (Exception e)
		{                                                                   
		    failed(e, "Unexpected Exception");                              
		}
	    }
	}
    }

    
    /**
    free()  
    Test select then free make sure locator not available
    **/
    public void Var096()
    {
        if(getRelease() < JDTestDriver.RELEASE_V5R5M0){
            notApplicable("v5r5 variation");
            return;
        }

	if ( (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) {
	    if (checkJdbc40 ()) {
		try {

		    connection_.setAutoCommit(false);
		    rs6_ = statement6_.executeQuery ("SELECT * FROM " + TABLE6_);
		    rs6_.next();
                    Blob[] blob = new Blob[FREE_LOCATOR_BLOCK_SIZE ];
                    int[] locator = new int[FREE_LOCATOR_BLOCK_SIZE ];
		    for (int i = 0; i < FREE_LOCATOR_BLOCK_SIZE; i++) { 
		     blob[i] = rs6_.getBlob ("C_BLOB");
		     locator[i] = JDReflectionUtil.callMethod_I(blob[i], "getLocator"); 
                     rs6_.next(); 
                    }
                    
                    for (int i = 0; i < FREE_LOCATOR_BLOCK_SIZE; i++) { 
                        JDReflectionUtil.callMethod_V(blob[i], "free");
                    }
	      // Check the locator using DB locator value (not using the BLOB object)

                    String[] answer = new String[FREE_LOCATOR_BLOCK_SIZE];
                    for (int i = 0; i < FREE_LOCATOR_BLOCK_SIZE; i++) { 
                      
                      ResultSet rs = statement2_.executeQuery( "select "+JDLobTest.COLLECTION +".BLOBLOCINF("+locator[i]+")"+
                      " from qsys2.qsqptabl");
                      rs.next();
                      answer[i] = rs.getString(1); 
                      rs.close();
                    }
		    assertCondition(answer[0].indexOf("ERROR") >= 0 && 
                        answer[FREE_LOCATOR_BLOCK_SIZE-1].indexOf("ERROR") >= 0, 
                        "Check said "+answer[0]); 

		}
		catch (Exception e) {
		    System.out.println(e);
		    failed (e, "Unexpected Exception");
		}
	    }
        }else{
        	notApplicable();
            return;
        }
    }



    /**
    free()  
    Test insert and close then free, then make sure lob not available 
    **/
    public void Var097()
    {
        if(getRelease() < JDTestDriver.RELEASE_V5R5M0){
            notApplicable("v5r5 variation");
            return;
        }

        if ( (getDriver() != JDTestDriver.DRIVER_TOOLBOX) ) {
            if (checkLobSupport () && checkJdbc40 ()) {
                try {
                  
                    rs6_ = statement6_.executeQuery ("SELECT * FROM " + TABLE6_);      
                    Blob[] blob = new Blob[FREE_LOCATOR_BLOCK_SIZE];
                    int[] locator = new int[FREE_LOCATOR_BLOCK_SIZE]; 
                    rs6_.next();
                    for (int i = 0; i < FREE_LOCATOR_BLOCK_SIZE; i++) { 
                      blob[i] = rs6_.getBlob ("C_BLOB");
                      locator[i] = JDReflectionUtil.callMethod_I(blob[i], "getLocator");
                      rs6_.next();
                     
                    }
                    
                    connection_.setAutoCommit(false);
                    PreparedStatement ps6 = connection_.prepareStatement("INSERT INTO " + TABLE6_ + " (C_BLOB) VALUES (?)");  //@pda
                    for (int i = 0; i < FREE_LOCATOR_BLOCK_SIZE; i++) { 
                    ps6.setBlob(1, blob[i]);
                    ps6.executeUpdate();
                    }
                    ps6.close();
                    for (int i = 0; i < FREE_LOCATOR_BLOCK_SIZE; i++) { 
                    JDReflectionUtil.callMethod_V(blob[i], "free");
                    }

                      // Check the locator using DB locator value (not using the BLOB object)
                    String[] answer = new String[FREE_LOCATOR_BLOCK_SIZE];
                    for (int i = 0; i < FREE_LOCATOR_BLOCK_SIZE; i++) { 
                   
                      ResultSet rs = statement2_.executeQuery( "select "+JDLobTest.COLLECTION +".BLOBLOCINF("+locator[i]+")"+
                          " from qsys2.qsqptabl");
                      rs.next();
                      answer[i] = rs.getString(1); 
                      rs.close(); 
                    }
                      assertCondition((answer[0].indexOf("ERROR") >= 0) &&
                          answer[FREE_LOCATOR_BLOCK_SIZE-1].indexOf("ERROR") >= 0, 
                          "Check said "+answer[0]); 
                }
                
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
        else{
        	notApplicable();
            return;
        }
    }

    /**
    free()  
    Test insert without close
    **/
    public void Var098()
    {
        if(getRelease() < JDTestDriver.RELEASE_V5R5M0){
            notApplicable("v5r5 variation");
            return;
        }

        if ( (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) {
            if (checkLobSupport ()   && checkJdbc40 ()) {
                try {
     
                    rs6_ = statement6_.executeQuery ("SELECT * FROM " + TABLE6_);      
                    Blob[] blob = new Blob[FREE_LOCATOR_BLOCK_SIZE];
                    int[]  locator=new int[FREE_LOCATOR_BLOCK_SIZE]; 
                    rs6_.next();
                    for (int i = 0; i < FREE_LOCATOR_BLOCK_SIZE; i++) { 
                      blob[i] = rs6_.getBlob ("C_BLOB");
                      rs6_.next();
                    }

                    connection_.setAutoCommit(false);
                    PreparedStatement ps6 = connection_.prepareStatement("INSERT INTO " + TABLE6_ + " (C_BLOB) VALUES (?)");  //@pda
                    for (int i = 0; i < FREE_LOCATOR_BLOCK_SIZE; i++) { 
                      
                      blob[i].setBytes(1, new byte[]{1,2,3,4,5});
                      ps6.setBlob(1, blob[i]);
                      locator[i] = JDReflectionUtil.callMethod_I(blob[i], "getLocator"); 
                      ps6.executeUpdate();
                      JDReflectionUtil.callMethod_V(blob[i], "free");
                    }
                    // Check the locator using DB locator value (not using the BLOB object)
                    String[] answer = new String[FREE_LOCATOR_BLOCK_SIZE];
                    for (int i = 0; i < FREE_LOCATOR_BLOCK_SIZE; i++) { 
                      
                      ResultSet rs = statement2_.executeQuery( "select "+JDLobTest.COLLECTION +".BLOBLOCINF("+locator[i]+")"+
                      " from qsys2.qsqptabl");
                      rs.next();
                      answer[i] = rs.getString(1); 
                      rs.close();
                    }
                    assertCondition(answer[0].indexOf("ERROR") >= 0 &&
                        answer[FREE_LOCATOR_BLOCK_SIZE-1].indexOf("ERROR") >= 0 , "Check said "+answer); 
                }

                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
        else{
        	notApplicable();
            return;
        }
    }

    /**
    free()  
    Test close then free with autocommit on.
    This is a case where lob is already freed on host (toolbox ignores returncode back from host in this case)
    **/
    public void Var099()
    {
        if(getRelease() < JDTestDriver.RELEASE_V5R5M0){
            notApplicable("v5r5 variation");
            return;
        }

        if ( (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) {
            if (checkLobSupport ()   && checkJdbc40 ()) {
                try {
                  rs6_ = statement6_.executeQuery ("SELECT * FROM " + TABLE6_);      
                  rs6_.next();
                  Blob[] blob = new Blob[FREE_LOCATOR_BLOCK_SIZE];
                  int[] locator=new int [FREE_LOCATOR_BLOCK_SIZE];
                  for (int i = 0; i < FREE_LOCATOR_BLOCK_SIZE ; i++) { 
                    blob[i] = rs6_.getBlob ("C_BLOB");
                    rs6_.next(); 
                  }
                  rs6_.close();
                  statement6_.close();
                    
                    // Reopen statement
                  statement6_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_UPDATABLE); //@C2C

                    
                    PreparedStatement ps6 = connection_.prepareStatement("INSERT INTO " + TABLE6_ + " (C_BLOB) VALUES (?)");  //@pda
                    for (int i = 0; i < FREE_LOCATOR_BLOCK_SIZE ; i++) { 
                      
                      blob[i].setBytes(1, new byte[]{1,2,3,4,5});
                      ps6.setBlob(1, blob[i]);
                      locator[i] = JDReflectionUtil.callMethod_I(blob[i], "getLocator"); 
                      ps6.executeUpdate();
                    }
                    ps6.close(); 
                    for (int i = 0; i < FREE_LOCATOR_BLOCK_SIZE ; i++) { 
                       JDReflectionUtil.callMethod_V(blob[i], "free");
                    }
                    // Check the locator using DB locator value (not using the BLOB object)
                    String[] answer = new String[FREE_LOCATOR_BLOCK_SIZE];
                    for (int i = 0; i < FREE_LOCATOR_BLOCK_SIZE ; i++) { 
                      
                      ResultSet rs = statement2_.executeQuery( "select "+JDLobTest.COLLECTION +".BLOBLOCINF("+locator[i]+")"+
                      " from qsys2.qsqptabl");
                      rs.next();
                      answer [i]= rs.getString(1); 
                      rs.close();
                    }
                    assertCondition(answer[0].indexOf("ERROR") >= 0 && answer[FREE_LOCATOR_BLOCK_SIZE-1].indexOf("ERROR") >= 0, "Check said "+answer); 
                }

                catch (Exception e) {
                    failed (e, "Unexpected Exception-This is a case where lob is already freed on host (toolbox ignores returncode back from host in this case)");
                }
            }
        }
        else{
        	notApplicable();
            return;
        }
    }



    /**
     free() length() -- Make sure length throws an exception after free
     **/
    public void Var100()
    {
      
      if ((getDriver() == JDTestDriver.DRIVER_TOOLBOX) ||
	    ((getDriver() == JDTestDriver.DRIVER_NATIVE) && checkJdbc40 ())) {
        try {
          rs_ = statement_.executeQuery ("SELECT * FROM " + TABLE_);//var58 commited and TB does not hold locataors
          rs_.next(); rs_.next();;
          Blob blob = rs_.getBlob ("C_BLOB");
	  System.out.println("Classname is "+blob.getClass().getName()); 
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
        	if((getDriver() == JDTestDriver.DRIVER_TOOLBOX) && (getRelease()< JDTestDriver.RELEASE_V5R5M0)) 
        	{
        		assertCondition(e.toString().indexOf("not support this function") != -1 || e.toString().indexOf("NoSuchMethod") != -1 ,
					"message is "+e.toString()+" sb ..not support");
        		return;
        	}
          failed(e, "Unexpected Exception");                              
        }
      }
    }
    
    /**
     free() getbytes() -- Make sure method throws an exception after free
     **/
    public void Var101()
    {
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX || checkJdbc40 ()) {
        try {
            rs_ = statement_.executeQuery ("SELECT * FROM " + TABLE_);//prev var frees locator so reselect
          rs_.next(); rs_.next();;
          Blob blob = rs_.getBlob ("C_BLOB");
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
        	if((getDriver() == JDTestDriver.DRIVER_TOOLBOX) && (getRelease()< JDTestDriver.RELEASE_V5R5M0)) 
        	{
        		assertCondition(e.getMessage().indexOf("not support this function") != -1 || e.toString().indexOf("NoSuchMethod") != -1 ,
					"message is "+e.getMessage()+" sb ..not support");
        		return;
        	}
          failed(e, "Unexpected Exception");                              
        }
      }
    }
    
    /**
     free() getBinaryStream() -- Make sure method throws an exception after free
     **/
    public void Var102()
    {
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX || checkJdbc40 ()) {
        try {
          rs_ = statement_.executeQuery ("SELECT * FROM " + TABLE_);//prev var frees locator so reselect
          rs_.next(); rs_.next();;
          Blob blob = rs_.getBlob ("C_BLOB");
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
        	if((getDriver() == JDTestDriver.DRIVER_TOOLBOX) && (getRelease()< JDTestDriver.RELEASE_V5R5M0)) 
        	{
        		assertCondition(e.getMessage().indexOf("not support this function") != -1 || e.toString().indexOf("NoSuchMethod") != -1 ,
					"message is "+e.getMessage()+" sb ..not support");
        		return;
        	}
          failed(e, "Unexpected Exception");                              
        }
      }
    }
    
    
    /**
     free() position() -- Make sure method throws an exception after free
     **/
    public void Var103()
    {
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX || checkJdbc40 ()) {
        try {
          rs_ = statement_.executeQuery ("SELECT * FROM " + TABLE_);//prev var frees locator so reselect
          rs_.next(); rs_.next();;
          Blob blob = rs_.getBlob ("C_BLOB");
          Blob blob2 = rs_.getBlob ("C_BLOB");
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
        	if((getDriver() == JDTestDriver.DRIVER_TOOLBOX) && (getRelease()< JDTestDriver.RELEASE_V5R5M0)) 
        	{
        		assertCondition(e.getMessage().indexOf("not support this function") != -1 || e.toString().indexOf("NoSuchMethod") != -1 ,
					"message is "+e.getMessage()+" sb ..not support");
        		return;
        	}
          failed(e, "Unexpected Exception");                              
        }
      }
    }
    
    /**
     free() position() -- Make sure method throws an exception after free
     **/
    public void Var104()
    {
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX  || checkJdbc40 ()) {
        try {
          rs_ = statement_.executeQuery ("SELECT * FROM " + TABLE_);//prev var frees locator so reselect
          rs_.next(); rs_.next();;
          Blob blob = rs_.getBlob ("C_BLOB");
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
        	if((getDriver() == JDTestDriver.DRIVER_TOOLBOX) && (getRelease()< JDTestDriver.RELEASE_V5R5M0)) 
        	{
        		assertCondition(e.getMessage().indexOf("not support this function") != -1 || e.toString().indexOf("NoSuchMethod") != -1 ,
					"message is "+e.getMessage()+" sb ..not support");
        		return;
        	}
          failed(e, "Unexpected Exception");                              
        }
      }
    }
    
    /**
     free() setBytes() -- Make sure method throws an exception after free
     **/
    public void Var105()
    {
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX  || checkJdbc40 ()) {
        try {
          rs_ = statement_.executeQuery ("SELECT * FROM " + TABLE_);//prev var frees locator so reselect
          rs_.next(); rs_.next();;
          Blob blob = rs_.getBlob ("C_BLOB");
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
        	if((getDriver() == JDTestDriver.DRIVER_TOOLBOX) && (getRelease()< JDTestDriver.RELEASE_V5R5M0)) 
        	{
        		assertCondition(e.getMessage().indexOf("not support this function") != -1 || e.toString().indexOf("NoSuchMethod") != -1 ,
					"message is "+e.getMessage()+" sb ..not support");
        		return;
        	}
          failed(e, "Unexpected Exception");                              
        }
      }
    }
    
    
    /**
     free() setBytes() -- Make sure method throws an exception after free
     **/
    public void Var106()
    {
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX  || checkJdbc40 ()) {
        try {
          rs_ = statement_.executeQuery ("SELECT * FROM " + TABLE_);//prev var frees locator so reselect
          rs_.next(); rs_.next();;
          Blob blob = rs_.getBlob ("C_BLOB");
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
        	if((getDriver() == JDTestDriver.DRIVER_TOOLBOX) && (getRelease()< JDTestDriver.RELEASE_V5R5M0)) 
        	{
        		assertCondition(e.getMessage().indexOf("not support this function") != -1 || e.toString().indexOf("NoSuchMethod") != -1 ,
					"message is "+e.getMessage()+" sb ..not support");
        		return;
        	}
          failed(e, "Unexpected Exception");                              
        }
      }
    }
    
    /**
     free() setBinaryStream() -- Make sure method throws an exception after free
     **/
    public void Var107()
    {
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX  || checkJdbc40 ()) {
        try {
          rs_ = statement_.executeQuery ("SELECT * FROM " + TABLE_);//prev var frees locator so reselect
          rs_.next(); rs_.next();;
          Blob blob = rs_.getBlob ("C_BLOB");
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
        	if((getDriver() == JDTestDriver.DRIVER_TOOLBOX) && (getRelease()< JDTestDriver.RELEASE_V5R5M0)) 
        	{
        		assertCondition(e.getMessage().indexOf("not support this function") != -1 || e.toString().indexOf("NoSuchMethod") != -1 ,
					"message is "+e.getMessage()+" sb ..not support");
        		return;
        	}
          failed(e, "Unexpected Exception");                              
        }
      }
    }
    
    /**
     free() truncate() -- Make sure method throws an exception after free
     **/
    public void Var108()
    {
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX  || checkJdbc40 ()) {
        try {
          rs_ = statement_.executeQuery ("SELECT * FROM " + TABLE_);//prev var frees locator so reselect
          rs_.next(); rs_.next();;
          Blob blob = rs_.getBlob ("C_BLOB");
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
        	if((getDriver() == JDTestDriver.DRIVER_TOOLBOX) && (getRelease()< JDTestDriver.RELEASE_V5R5M0)) 
        	{
        		assertCondition(e.getMessage().indexOf("not support this function") != -1 || e.toString().indexOf("NoSuchMethod") != -1 ,
					"message is "+e.getMessage()+" sb ..not support");
        		return;
        	}
          failed(e, "Unexpected Exception");                              
        }
      }
    }
    
    
    /**
     free() getBinaryStream() -- Make sure method throws an exception after free
     **/
    public void Var109()
    {
      if (checkJdbc40 ()) {
        try {
          rs_ = statement_.executeQuery ("SELECT * FROM " + TABLE_);//prev var frees locator so reselect
          rs_.next(); rs_.next();;
          Blob blob = rs_.getBlob ("C_BLOB");
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
        	if((getDriver() == JDTestDriver.DRIVER_TOOLBOX) && (getRelease()< JDTestDriver.RELEASE_V5R5M0)) 
        	{
        		assertCondition(e.getMessage().indexOf("not support this function") != -1 || e.toString().indexOf("NoSuchMethod") != -1 ,
					"message is "+e.getMessage()+" sb ..not support");
        		return;
        	}
          failed(e, "Unexpected Exception");                              
        }
      }
    }
    
    /**
     free() free() -- Shouldn't throw exception
     **/
    public void Var110()                                                        
    {                                                                           
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX  || checkJdbc40()) { 
        
        try
        {
          rs_ = statement_.executeQuery ("SELECT * FROM " + TABLE_);//prev var frees locator so reselect
          rs_.next(); rs_.next();;
          Blob blob = rs_.getBlob ("C_BLOB");
          JDReflectionUtil.callMethod_V(blob, "free");
          JDReflectionUtil.callMethod_V(blob, "free");
          assertCondition (true); 
        }
        catch (Exception e)
        {            
        	if((getDriver() == JDTestDriver.DRIVER_TOOLBOX) && (getRelease()< JDTestDriver.RELEASE_V5R5M0)) 
        	{
        		assertCondition(e.getMessage().indexOf("not support this function") != -1 || e.toString().indexOf("NoSuchMethod") != -1 ,
					"message is "+e.getMessage()+" sb ..not support");
        		return;
        	}
          failed(e, "Unexpected Exception");                              
        }
      }
    }
    
    

    /**
     Make sure lob locator not accessible after rs.close()
    **/
    public void Var111()
    {
	if(getRelease() <= JDTestDriver.RELEASE_V7R1M0){
	    notApplicable("v7r2 variation");
	    return;
	}

	if ( (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) {
	    if ( checkJdbc40 ()) {
		try {

		    connection_.setAutoCommit(false);
		    rs6_ = statement6_.executeQuery ("SELECT * FROM " + TABLE6_);
		    rs6_.next();

		    Blob blob = rs6_.getBlob ("C_BLOB");
		    int locator = JDReflectionUtil.callMethod_I(blob, "getLocator"); 

		    rs6_.close(); 

	      // Check the locator using DB locator value (not using the BLOB object)

		    ResultSet rs = statement2_.executeQuery( "select "+JDLobTest.COLLECTION +".BLOBLOCINF("+locator+")"+
							     " from qsys2.qsqptabl");
		    rs.next();
		    String answer = rs.getString(1); 
		    rs.close(); 
		    assertCondition(answer.indexOf("ERROR") >= 0, "Check said '"+answer+"' should have said error because the result set is closed -- new variation for V7R1" ); 

		}
		catch (Exception e) {
		    System.out.println(e);
		    failed (e, "Unexpected Exception");
		}
	    }
	}else{
	    notApplicable("NON TOOLBox VAR");
	    return;
	}
    }



   /**
     Make sure lob locator not accessible after connection.commit()
    **/

    public void Var112()
    {
	if(getRelease() < JDTestDriver.RELEASE_V5R5M0){
	    notApplicable("v5r5 variation");
	    return;
	}
	if ( getDriver() == JDTestDriver.DRIVER_TOOLBOX)  
	{
	    notApplicable();
	    return;
	}

          try {
            
            connection_.setAutoCommit(false);
            rs6_ = statement6_.executeQuery ("SELECT * FROM " + TABLE6_);
            rs6_.next();
            
            Blob blob = rs6_.getBlob ("C_BLOB");
            int locator = JDReflectionUtil.callMethod_I(blob, "getLocator"); 

	    connection_.commit(); 
	    rs6_.close();

              // Check the locator using DB locator value (not using the BLOB object)
           
              ResultSet rs = statement2_.executeQuery( "select "+JDLobTest.COLLECTION +".BLOBLOCINF("+locator+")"+
                  " from qsys2.qsqptabl");
              rs.next();
              String answer = rs.getString(1); 
              rs.close(); 
              assertCondition(answer.indexOf("ERROR") >= 0, "Check said "+answer); 
            
          }
          catch (Exception e) {
            System.out.println(e);
            failed (e, "Unexpected Exception");
          }

    }




}
