///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionTranslateHex.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDConnectionTranslateHex.java
//
// Classes:      JDConnectionTranslateHex
//
////////////////////////////////////////////////////////////////////////

package test.JD.Connection;

import com.ibm.as400.access.AS400;

import test.JDConnectionTest;
import test.JDTestDriver;
import test.JDTestcase;
import test.JD.JDTestUtilities;

import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

/**
Testcase JDConnectionTranslateHex. this tests the following 
of the JDBC Connection class:

  Property "Translate Hex"

**/
public class JDConnectionTranslateHex
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDConnectionTranslateHex";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDConnectionTest.main(newArgs); 
   }


    // Private data.
    private static  String table      = JDConnectionTest.COLLECTION;
    private static  String proc	      = JDConnectionTest.COLLECTION;
    private Connection          conn_Char;
    private Connection		conn_Bin;

/**
Constructor.
**/
    public JDConnectionTranslateHex (AS400 systemObject,
                              Hashtable namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password)
    {
        super (systemObject, "JDConnectionTranslateHex",
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
	// reset collection related information after the superclass
	// has had a change to process the arguments
	//
	table   = JDConnectionTest.COLLECTION;
	proc	= JDConnectionTest.COLLECTION;

        String urlChar = baseURL_
            
	    + ";translate hex=character";
	String urlBin = baseURL_
            +  ";translate hex=binary";
	
        conn_Char = testDriver_.getConnection (urlChar,systemObject_.getUserId(), encryptedPassword_);
	conn_Bin = testDriver_.getConnection (urlBin,systemObject_.getUserId(), encryptedPassword_);
	
    }

/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
        conn_Char.close ();
	conn_Bin.close ();
    }

/**
Compares a Blob with a byte[].
**/
    private boolean compare (Blob i, byte[] b)
    throws SQLException
    {
        byte[] iBytes = i.getBytes (1, (int) i.length());              
        return isEqual (iBytes, b);
    }


/**
Utility Function

returns the hex representation of a String
**/
    static String dumpBytes(String temp)
    {
	char cArray [] = temp.toCharArray(); 
	int length = cArray.length;
	String s = "";
	for(int i = 0 ; i < length ; i++)
	{
	    String ns = Integer.toHexString(((int) cArray[i])& 0xFF);
	    if(ns.length() == 1)
	    {
		s += "0"+ ns;
	    }
	    else
	    {
		s += ns;
	    }
	}
	return s;
    }	




/**
Property translate hex = character, expected to fail when we try to insert a hex value into a binary column
**/
    public void Var001 ()
    {
        if(getRelease() >= JDTestDriver.RELEASE_V7R1M0)
        {
        try {
	    Statement stmt = conn_Char.createStatement ();
	    try{
		stmt.execute("DROP TABLE "+table+ ".Var1");
	    } catch (Exception s) {}
	    stmt.execute("CREATE TABLE "+table+ ".Var1" + " (HEX BINARY(20))");
	    stmt.execute("INSERT INTO "+table+ ".Var1" + " VALUES(X'005D3F')");
	    ResultSet rs = stmt.executeQuery("SELECT * FROM "+table+".Var1");
	    rs.close();
	    stmt.execute("DROP TABLE "+table+".Var1");
	    stmt.close();
	    failed("Didn't throw SQLException");
	}
	catch(SQLException e)
	{
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
        }
        else
            notApplicable("V5R3 or greater variation");
    }

/**
Property translate hex = binary, should work when we try to insert a hex value into a binary column
**/
    public void Var002 ()
    {
	
        if(getRelease() >= JDTestDriver.RELEASE_V7R1M0){
	try {
	    Statement stmt = conn_Bin.createStatement ();
	    try{
		stmt.execute("DROP TABLE "+table+ ".Var2");
	    } catch (Exception s) {}
	    stmt.execute("CREATE TABLE "+table+ ".Var2" + " (HEX BINARY(20))");
	    byte[] expected = {0x11,0x12,0x13,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
	    0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	    stmt.execute("INSERT INTO "+table+ ".Var2" + " VALUES(X'111213')");
	    ResultSet rs = stmt.executeQuery("SELECT * FROM "+table+".Var2");
	    rs.next ();
	    byte[] b = rs.getBytes ("HEX");
	    rs.close();
	    stmt.execute("DROP TABLE "+table+ ".Var2");
	    stmt.close();
	    assertCondition (isEqual(b,expected));
	}
	catch(SQLException e)
	{
	     failed(e, "Unexpected Exception");
	}
        }
        else
            notApplicable("V5R3 or greater variation");
    }

/**
Property translate hex = character, should pass when we try to insert a hex value into a char column
**/
    public void Var003 ()
    {
        if(getRelease() >= JDTestDriver.RELEASE_V7R1M0){
	try {
	    Statement stmt = conn_Char.createStatement ();
	    try{
		stmt.execute("DROP TABLE "+table+".Var3" );
	    } catch (Exception s) {}
	    stmt.execute("CREATE TABLE "+table+".Var3" + " (HEX CHAR(20) FOR BIT DATA)");
	    byte[] expected ={0x10, 0x5D, 0x3F, 0x40, 0x40, 0x40, 0x40, 0x40, 0x40 ,0x40,
	    0x40, 0x40, 0x40, 0x40, 0x40, 0x40, 0x40, 0x40, 0x40, 0x40};
	    stmt.execute("INSERT INTO " +table+ ".Var3" + " VALUES(X'105D3F')");
	    ResultSet rs = stmt.executeQuery("SELECT * FROM "+table+ ".Var3");
	    rs.next();
	    byte[] b = rs.getBytes("HEX");
	    rs.close();
	    stmt.execute("DROP TABLE "+ table+ ".Var3");
	    stmt.close();
	    assertCondition(isEqual(b,expected), "b = " +JDTestUtilities.dumpBytes(b)+ " exp = " +JDTestUtilities.dumpBytes(expected));
	}
	catch(SQLException e)
	{
	     failed(e, "Unexpected Exception");
	}
        }
        else
            notApplicable("V5R3 or greater variation");
    }

/**
Property translate hex = binary, should fail when we try to insert a hex value into a char column
@B2 - Now allowed for " FOR BIT DATA " TYPES they really are binary types
**/
    public void Var004 ()
    {
        if(getRelease() >= JDTestDriver.RELEASE_V7R1M0){
	try {
	    Statement stmt = conn_Bin.createStatement ();
	    try{
		stmt.execute("DROP TABLE "+table+ ".Var4");
	    } catch (Exception s) {}
	    byte[] expected = {0x11,0x12,0x13,0x40,0x40,0x40,0x40,0x40,0x40,0x40,           //@B2
	    0x40,0x40,0x40,0x40,0x40,0x40,0x40,0x40,0x40,0x40};
	    stmt.execute("CREATE TABLE "+table+".Var4" + " (HEX CHAR(20) FOR BIT DATA)");
	    stmt.execute("INSERT INTO "+table+".Var4" + " VALUES(X'111213')");
	    ResultSet rs = stmt.executeQuery("SELECT * FROM "+table+".Var4");
	    rs.next();
	    byte[] b = rs.getBytes("HEX");                                                    //@B2
	    rs.close();
	    stmt.execute("DROP TABLE "+table+ ".Var4");
	    stmt.close();
	    if (getDriver () == JDTestDriver.DRIVER_NATIVE)                                 //@B2
		assertCondition(isEqual(b,expected), "b = " +JDTestUtilities.dumpBytes(b)+ " exp = " +JDTestUtilities.dumpBytes(expected));
	    else if (isToolboxDriver())      {
		if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
		    assertCondition(isEqual(b,expected), "b = " +JDTestUtilities.dumpBytes(b)+ " exp = " +JDTestUtilities.dumpBytes(expected));
		} else {
		    failed("Toolbox pre v5r5 didn't throw SQLException");
		}
	    } else {
		failed("Unknown driver "+getDriver());
	    } 
	}
	catch(SQLException e)
	{
	    if (getDriver () == JDTestDriver.DRIVER_NATIVE)                                 //@B2
		failed(e, "Unexpected Exception");
	    else
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
        }
        else
            notApplicable("V5R3 or greater variation");
    }

/**
Property translate hex = character,should pass when we try to insert a hex value into a varchar column
**/
    public void Var005 ()
    {
        if(getRelease() >= JDTestDriver.RELEASE_V7R1M0){
	try {
	    Statement stmt = conn_Char.createStatement ();
	    try{
		stmt.execute("DROP TABLE "+table+ ".Var5" );
	    } catch (Exception s) {}
	    stmt.execute("CREATE TABLE " +table+".Var5" + " (HEX VARCHAR(20) FOR BIT DATA)");
	    byte[] expected = {0x00, 0x5D, 0x3F};
	    stmt.execute("INSERT INTO " +table+ ".Var5" + " VALUES(X'005D3F')");
	    ResultSet rs = stmt.executeQuery("SELECT * FROM "+table+ ".Var5");
	    rs.next();
	    byte[] b = rs.getBytes("HEX");
	    rs.close();
	    stmt.execute("DROP TABLE "+table+ ".Var5");
	    stmt.close();
	    assertCondition(isEqual(b,expected));
	}
	catch(SQLException e)
	{
	     failed(e, "Unexpected Exception");
	}
        }
        else
            notApplicable("V5R3 or greater variation");
    }

/**
Property translate hex = binary, should fail when we try to insert a hex value into a varchar column

@B2 - Now allowed for " FOR BIT DATA " TYPES they really are binary types
**/
    public void Var006 ()
    {
        if(getRelease() >= JDTestDriver.RELEASE_V7R1M0){
	try {
	    Statement stmt = conn_Bin.createStatement ();
	    try{
		stmt.execute("DROP TABLE "+table+ ".Var6");
	    } catch (Exception s) {}
	    byte[] expected = {0x11, 0x12, 0x13};                                                //@B2
	    stmt.execute("CREATE TABLE "+table+ ".Var6" + " (HEX VARCHAR(20) FOR BIT DATA)"); 
	    stmt.execute("INSERT INTO "+table+ ".Var6" + " VALUES(X'111213')");
	    ResultSet rs = stmt.executeQuery("SELECT * FROM "+table+ ".Var6");
	    rs.next();
	    byte [] b = rs.getBytes("HEX");                                                      //@B2
	    rs.close();
	    stmt.execute("DROP TABLE "+table+ ".Var6");
	    stmt.close();
	    if (getDriver () == JDTestDriver.DRIVER_NATIVE)                                      //@B2
		assertCondition(isEqual(b,expected));
	    else {
		if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
		    assertCondition(isEqual(b,expected), "retrieved bytes != expected bytes");
		} else { 
		    failed("Toolbox pre V5R5 didn't throw SQLException");
		}
	    }
	}
	catch(SQLException e)
	{
	    if (getDriver () == JDTestDriver.DRIVER_NATIVE)                                      //@B2
		failed(e, "Unexpected Exception");
	    else
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
        }
        else
            notApplicable("V5R3 or greater variation");
    }

/**
Property translate hex = character,should pass when we try to insert a hex value into a varbinary column
**/
    public void Var007 ()
    {
        if(getRelease() >= JDTestDriver.RELEASE_V7R1M0){
	try {
	    Statement stmt = conn_Char.createStatement ();
	    try{
		stmt.execute("DROP TABLE "+table+ ".Var7" );
	    } catch (Exception s) {}
	    stmt.execute("CREATE TABLE " +table+ ".Var7" + " (HEX VARBINARY(20))");
	    stmt.execute("INSERT INTO " +table+ ".Var7" + " VALUES(X'005D3F')");
	    ResultSet rs = stmt.executeQuery("SELECT * FROM "+table+ ".Var7");
	    rs.close();
	    stmt.execute("DROP TABLE "+ ".Var7");
	    stmt.close();
	    failed("Didn't throw SQL Exception");
	   
	}
	catch(SQLException e)
	{
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
        }
        else
            notApplicable("V5R3 or greater variation");
    }

/**
Property translate hex = binary, should fail when we try to insert a hex value into a varbinary column
**/
    public void Var008 ()
    {
        if(getRelease() >= JDTestDriver.RELEASE_V7R1M0){
	try {
	    Statement stmt = conn_Bin.createStatement ();
	    try{
		stmt.execute("DROP TABLE "+table+ ".Var8");
	    } catch (Exception s) {}
	    stmt.execute("CREATE TABLE "+table+ ".Var8" + " (HEX VARBINARY(20))");
	    byte[] expected = {0x11, 0x12, 0x13};
	    stmt.execute("INSERT INTO "+table+ ".Var8" + " VALUES(X'111213')");
	    ResultSet rs = stmt.executeQuery("SELECT * FROM "+table+ ".Var8");
	    rs.next();
	    byte[] b = rs.getBytes("HEX");
	    rs.close();
	    stmt.execute("DROP TABLE "+table+ ".Var8");
	    stmt.close();
	    assertCondition(isEqual(b, expected));
	}
	catch(SQLException e)
	{
	     failed(e, "Unexpected Exception");
	}
        }
        else
            notApplicable("V5R3 or greater variation");
    }

/**
Property translate hex = character,should fail when we try to insert a hex value into a blob column
**/
    public void Var009 ()
    {
        if(getRelease() >= JDTestDriver.RELEASE_V7R1M0){
	try {
	    Statement stmt = conn_Char.createStatement ();
	    try{
		stmt.execute("DROP TABLE "+table+ ".Var9" );
	    } catch (Exception s) {}
	    stmt.execute("CREATE TABLE " +table+ ".Var9" + " (HEX BLOB(20))");
	    stmt.execute("INSERT INTO " +table+ ".Var9" + " VALUES(X'005D3F')");
	    ResultSet rs = stmt.executeQuery("SELECT * FROM "+table+ ".Var9");
	    rs.next();
	    rs.close();
	    stmt.execute("DROP TABLE "+table+ ".Var9");
	    stmt.close();
	    failed("Didn't throw SQLException");
	}
	catch(SQLException e)
	{
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
        }
        else
            notApplicable("V5R3 or greater variation");
    }

/**
Property translate hex = binary, should pass when we try to insert a hex value into a binary column
**/
    public void Var010 ()
    {
        if(getRelease() >= JDTestDriver.RELEASE_V7R1M0){
	try {
	    Statement stmt = conn_Bin.createStatement ();
	    try{
		stmt.execute("DROP TABLE "+table+ ".Var10");
	    } catch (Exception s) {}
	    stmt.execute("CREATE TABLE "+table+ ".Var10" + " (HEX BLOB(200))");
	    byte [] expected = {0x11, 0x12, 0x13};
	    stmt.execute("INSERT INTO "+table+".Var10" + " VALUES(X'111213')");
	    ResultSet rs = stmt.executeQuery("SELECT * FROM "+table+".Var10");
	    rs.next();
	    Blob b = rs.getBlob(1);
	    assertCondition(compare(b, expected));
	    rs.close();
	    stmt.execute("DROP TABLE "+table+".Var10");
	    stmt.close();
	}
	catch(SQLException e)
	{
	     failed(e, "Unexpected Exception");
	}
        }
        else
            notApplicable("V5R3 or greater variation");
    }

/**
Property translate hex = character, should fail when we try to insert a hex value into a binary parameter
**/
    public void Var011()
    {
        
        if(getRelease() >= JDTestDriver.RELEASE_V7R1M0){
	try{
String sql = "CREATE PROCEDURE "+proc+".TRANSLATE1 (IN B_IN BINARY(20), OUT B_OUT BINARY(20)) LANGUAGE SQL "+
             "SPECIFIC TRANSLATE1 JDCTRANSLATE1: BEGIN DECLARE DUMMY BINARY(20); "+
             "SET B_OUT=B_IN; END JDCTRANSLATE1" ;

            Statement stmt = conn_Char.createStatement ();
            try {
               stmt.executeUpdate("drop procedure "+proc+".TRANSLATE1");
             } catch (Exception e) {}                
             stmt.executeUpdate(sql);
             CallableStatement cstmt = conn_Char.prepareCall("{call "+proc+".TRANSLATE1 (X'2621453A', ?)}");
	     cstmt.registerOutParameter(1, java.sql.Types.BINARY);
	     cstmt.execute();
             cstmt.close();
	     failed("Didn't throw SQLException");
	}
         catch (SQLException e){
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	 }
         }
        else
            notApplicable("V5R3 or greater variation");
    }

/**
Property translate hex = binary, should pass when we try to insert a hex value into a binary parameter
**/
     public void Var012()
     {
        
         if(getRelease() >= JDTestDriver.RELEASE_V7R1M0){
         try{
	     String  sql = "CREATE PROCEDURE "+proc+".TRANSLATE2 (IN B_IN BINARY(20),OUT B_OUT BINARY(20)) LANGUAGE SQL "+
              "SPECIFIC TRANSLATE2 JDCTRANSLATE2: BEGIN DECLARE DUMMY BINARY(20); "+
              "SET B_OUT=B_IN; END JDCTRANSLATE2" ;

             Statement stmt = conn_Bin.createStatement ();
             try {
               stmt.executeUpdate("drop procedure "+proc+".TRANSLATE2");
             } catch (Exception e) {}                
             stmt.executeUpdate(sql);
             CallableStatement cstmt = conn_Bin.prepareCall("{call "+proc+".TRANSLATE2 (X'2301453D6F',?)}");
             cstmt.registerOutParameter(1, java.sql.Types.BINARY);
	     byte [] expected = {0x23, 0x01, 0x45, 0x3D, 0x6F, 0x00, 0x00, 0x00, 0x00, 0x00,
	     0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,};
	     cstmt.execute();

             byte [] check = cstmt.getBytes(1);
             assertCondition(isEqual(expected, check), "expected = " + JDTestUtilities.dumpBytes(expected)+ " check = " + JDTestUtilities.dumpBytes(check));
         }
         catch (SQLException e){
             failed (e, "Unexpected Exception");
         }
         }
        else
            notApplicable("V5R3 or greater variation");
     }

/**
Property translate hex = character, should fail when we try to insert a hex value into a varbinary parameter
**/
    public void Var013()
    {
        
        if(getRelease() >= JDTestDriver.RELEASE_V7R1M0){
	try{
String sql = "CREATE PROCEDURE "+proc+".TRANSLATE3 (IN B_IN VARBINARY(20), OUT B_OUT VARBINARY(20)) LANGUAGE SQL " + "SPECIFIC TRANSLATE3 JDCTRANSLATE3: BEGIN DECLARE DUMMY VARBINARY(20); "+
             "SET B_OUT=B_IN; END JDCTRANSLATE3" ;

            Statement stmt = conn_Char.createStatement ();
            try {
               stmt.executeUpdate("drop procedure "+proc+".TRANSLATE3");
             } catch (Exception e) {}                
             stmt.executeUpdate(sql);
             CallableStatement cstmt = conn_Char.prepareCall("{call "+proc+".TRANSLATE3 (X'2621453A', ?)}");
	     cstmt.registerOutParameter(1, java.sql.Types.VARBINARY);
	     cstmt.execute();
             cstmt.close();
	     failed("Didn't throw SQLException");
	}
         catch (SQLException e){
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	 }
         }
        else
            notApplicable("V5R3 or greater variation");
    }

/**
Property translate hex = binary, should pass when we try to insert a hex value into a varbinary parameter
**/
     public void Var014()
     {
        
         if(getRelease() >= JDTestDriver.RELEASE_V7R1M0){
         try{
String  sql = "CREATE PROCEDURE "+proc+".TRANSLATE4 (IN B_IN VARBINARY(20),OUT B_OUT VARBINARY(20)) LANGUAGE SQL " + "SPECIFIC TRANSLATE4 JDCTRANSLATE4: BEGIN DECLARE DUMMY VARBINARY(20); "+
              "SET B_OUT=B_IN; END JDCTRANSLATE4" ;

             Statement stmt = conn_Bin.createStatement ();
             try {
               stmt.executeUpdate("drop procedure "+proc+".TRANSLATE4");
             } catch (Exception e) {}                
             stmt.executeUpdate(sql);
             CallableStatement cstmt = conn_Bin.prepareCall("{call "+proc+".TRANSLATE4 (X'2301453D6F',?)}");
             cstmt.registerOutParameter(1, java.sql.Types.VARBINARY);
	     byte [] expected = {0x23, 0x01, 0x45, 0x3D, 0x6F};
	     cstmt.execute();
             byte [] check = cstmt.getBytes(1);
             assertCondition(isEqual(expected, check), "expected = " + JDTestUtilities.dumpBytes(expected)+ " check = " + JDTestUtilities.dumpBytes(check));
         }
         catch (SQLException e){
             failed (e, "Unexpected Exception");
         }
         }
        else
            notApplicable("V5R3 or greater variation");
     }


/**
Property translate hex = character, should pass when we try to insert a hex value into a char parameter
**/
    public void Var015()
    {
        if(getRelease() >= JDTestDriver.RELEASE_V7R1M0){
	try{
String sql = "CREATE PROCEDURE "+proc+".TRANSLATE5 (IN B_IN CHAR(20) FOR BIT DATA, OUT B_OUT CHAR(20) FOR BIT DATA) LANGUAGE SQL " + "SPECIFIC TRANSLATE5 JDCTRANSLATE5: BEGIN DECLARE DUMMY CHAR(20); "+
             "SET B_OUT=B_IN; END JDCTRANSLATE5   ";

            Statement stmt = conn_Char.createStatement ();
            try {
               stmt.executeUpdate("drop procedure "+proc+".TRANSLATE5");
             } catch (Exception e) {}                
             stmt.executeUpdate(sql);
             CallableStatement cstmt = conn_Char.prepareCall("{call "+proc+".TRANSLATE5 (X'2621453A', ?)}");
	     byte [] expected = {0x26, 0x21, 0x45, 0x3A, 0x40, 0x40, 0x40, 0x40, 0x40, 0x40,
	     0x40, 0x40, 0x40, 0x40, 0x40, 0x40, 0x40, 0x40, 0x40, 0x40};

	     cstmt.registerOutParameter(1, java.sql.Types.CHAR);
	     cstmt.execute();
	     byte[] b = cstmt.getBytes(1);
	     cstmt.close();
	     assertCondition(isEqual(b,expected));
	}
         catch (SQLException e){
	     failed(e, "UnexpectedException");
	 }
         }
        else
            notApplicable("V5R3 or greater variation");
    }

/**
Property translate hex = binary, should fail when we try to insert a hex value into a char parameter
**/
     public void Var016()
     {
        
         if(getRelease() >= JDTestDriver.RELEASE_V7R1M0){
         try{
String  sql = "CREATE PROCEDURE "+proc+".TRANSLATE6 (IN B_IN CHAR(20) FOR BIT DATA,OUT B_OUT CHAR(20) FOR BIT DATA) LANGUAGE SQL " + "SPECIFIC TRANSLATE6 JDCTRANSLATE6: BEGIN DECLARE DUMMY CHAR(20) FOR BIT DATA; " + "SET B_OUT=B_IN; END JDCTRANSLATE6" ;

             Statement stmt = conn_Bin.createStatement ();
             try {
               stmt.executeUpdate("drop procedure "+proc+".TRANSLATE6");
             } catch (Exception e) {}                
             stmt.executeUpdate(sql);
	     CallableStatement cstmt = conn_Bin.prepareCall("{call "+proc+".TRANSLATE6 (X'2301453D6F',?)}");
             cstmt.registerOutParameter(1, java.sql.Types.CHAR);
	     cstmt.execute();
	     cstmt.close();
	     if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
		 /* In V6R1 the DB eased this restriction */ 
		 assertCondition(true); 
	     } else { 
		 failed("Didn't Throw SQLException");
	     }
         }
         catch (SQLException e){
	     if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
		 failed(e, "Should not throw exception in V6R1 due to DB changes in May 2008"); 
	     } else { 
		 assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	     }
         }
         }
        else
            notApplicable("V5R3 or greater variation");
     }

/**
Property translate hex = character, should pass when we try to insert a hex value into a varchar parameter
**/
    public void Var017()
    {
        if(getRelease() >= JDTestDriver.RELEASE_V7R1M0){
	try{
String sql = "CREATE PROCEDURE "+proc+".TRANSLATE7 (IN B_IN VARCHAR(20) FOR BIT DATA, OUT B_OUT VARCHAR(20) FOR BIT DATA) LANGUAGE SQL " + "SPECIFIC TRANSLATE7 JDCTRANSLATE7: BEGIN DECLARE DUMMY VARCHAR(20) FOR BIT DATA; " + "SET B_OUT=B_IN; END JDCTRANSLATE7" ;

            Statement stmt = conn_Char.createStatement ();
            try {
               stmt.executeUpdate("drop procedure "+proc+".TRANSLATE7");
             } catch (Exception e) {}                
             stmt.executeUpdate(sql);
             CallableStatement cstmt = conn_Char.prepareCall("{call "+proc+".TRANSLATE7 (X'2621453A', ?)}");
	     byte[] expected = {0x26, 0x21, 0x45, 0x3a};
	     cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
	     cstmt.execute();
	     byte[] b = cstmt.getBytes(1);
	     cstmt.close();
	     assertCondition(isEqual(b, expected));
	}
         catch (SQLException e){
	     failed(e, "UnexpectedException");
	 }
         }
        else
            notApplicable("V5R3 or greater variation");
    }

/**
Property translate hex = binary, should fail when we try to insert a hex value into a varchar parameter
**/
     public void Var018()
     {
         if(getRelease() >= JDTestDriver.RELEASE_V7R1M0){
	 try{
String  sql = "CREATE PROCEDURE "+proc+".TRANSLATE8 (IN B_IN VARCHAR(20) FOR BIT DATA ,OUT B_OUT VARCHAR(20) FOR BIT DATA) LANGUAGE SQL " + "SPECIFIC TRANSLATE8 JDCTRANSLATE8: BEGIN DECLARE DUMMY VARCHAR(20) FOR BIT DATA; " + "SET B_OUT=B_IN; END JDCTRANSLATE8" ;

             Statement stmt = conn_Bin.createStatement ();
             try {
               stmt.executeUpdate("drop procedure "+proc+".TRANSLATE8");
             } catch (Exception e) {}                
             stmt.executeUpdate(sql);
             CallableStatement cstmt = conn_Bin.prepareCall("{call "+proc+".TRANSLATE8 (X'2301453D6F',?)}");
             cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
	     cstmt.execute();
	     cstmt.close();
	     if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
		 /* In V6R1 the DB eased this restriction */ 
		 assertCondition(true); 
	     } else { 
		 failed("Didn't Throw SQLException");
	     }
         }
         catch (SQLException e){
	     if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
		 failed(e, "Should not throw exception in V6R1 due to DB changes in May 2008"); 
	     } else { 
		 assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	     }
         }
         }
        else
            notApplicable("V5R3 or greater variation");
     }


/**
Property translate hex = character, should fail when we try to insert a hex value into a BLOB parameter
**/
    public void Var019()
    {
        if(getRelease() >= JDTestDriver.RELEASE_V7R1M0){
	try{
String sql = "CREATE PROCEDURE "+proc+".TRANSLATE9 (IN B_IN BLOB(20), OUT B_OUT BLOB(20)) LANGUAGE SQL " + "SPECIFIC TRANSLATE9 JDCTRANSLATE9: BEGIN DECLARE DUMMY BLOB(20); "+
             "SET B_OUT=B_IN; END JDCTRANSLATE9" ;

            Statement stmt = conn_Char.createStatement ();
            try {
               stmt.executeUpdate("drop procedure "+proc+".TRANSLATE9");
             } catch (Exception e) {}                
             stmt.executeUpdate(sql);
             CallableStatement cstmt = conn_Char.prepareCall("{call "+proc+".TRANSLATE9 (X'2621453A', ?)}");
	     cstmt.registerOutParameter(1, java.sql.Types.BLOB);
	     cstmt.execute();
	     cstmt.close();
	     stmt.close();
	     failed("Didn't throw SQLException");
	}
         catch (SQLException e){
	      assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	 }
         }
        else
            notApplicable("V5R3 or greater variation");
    }

/**
Property translate hex = binary, should pass when we try to insert a hex value into a blob parameter
**/
     public void Var020()
     {
	 /* 
	 if (getRelease() == JDTestDriver.RELEASE_V5R4M0 &&  getDriver() == JDTestDriver.DRIVER_NATIVE) {
	     notApplicable("Not working on V5R4 for native");
	     return; 
	 }
	 */
	 String sql="not set "; 
         if(getRelease() >= JDTestDriver.RELEASE_V7R1M0){
	 try{
 sql = "CREATE PROCEDURE "+proc+".TRANSLATE10 (IN B_IN BLOB(200), OUT B_OUT BLOB(200)) LANGUAGE SQL " + "SPECIFIC TRANSLATE10 JDCTRANSLATE10: BEGIN DECLARE DUMMY BLOB(200); "+
              "SET B_OUT=B_IN; END JDCTRANSLATE10" ;

             Statement stmt = conn_Bin.createStatement ();
             try {
               stmt.executeUpdate("drop procedure "+proc+".TRANSLATE10");
             } catch (Exception e) {}
	     byte [] expected = {0x23, 0x01, 0x45, 0x3D, 0x6F};
             stmt.executeUpdate(sql);
	     sql = "{call "+proc+".TRANSLATE10(BLOB(X'2301453D6F'),?)}";
             CallableStatement cstmt = conn_Bin.prepareCall(sql);
             cstmt.registerOutParameter(1, java.sql.Types.BLOB);
	     cstmt.execute();
	     Blob b = cstmt.getBlob(1);
	     byte[] bytes = b.getBytes (1, (int) b.length());              

	     assertCondition(compare(b, expected), "b = " +JDTestUtilities.dumpBytes(bytes)+ " exp = " +JDTestUtilities.dumpBytes(expected)+ "\n\nNote:  For native driver on V5R4 this will be fixed as a result of issue 35479");
	     cstmt.close();
	     stmt.close();
	 }
         catch (SQLException e){
             failed(e, "Unexpected Exception latest sql was "+sql);
       
	 }
         }
        else
            notApplicable("V5R3 or greater variation");
     }
}
