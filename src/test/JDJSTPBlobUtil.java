///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDJSTPBlobUtil.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.sql.*;

import test.JD.JDSetupCollection;

import java.io.*;

/**
 * This class contains utilities for copying files to and from a blob
 */ 

public class JDJSTPBlobUtil implements java.sql.Blob {

    static Statement stmt = null ;
    static PreparedStatement pstmt = null;
    static Connection lastConn = null;
    static boolean debug = false;
    static {
	String propertyString;
	try {
	    propertyString = System.getProperty("debug");
	    if (propertyString != null) {
		propertyString = propertyString.toUpperCase().trim();
		if (propertyString.equals("TRUE")) {
		    debug = true;
		}
	    }
	} catch (Exception dontCare) {
	}
    }



    /**
     * execute a statement, ignoring errors if needed
     */ 
    static int execStatement(Statement stmt1, String sql, boolean ignoreErrors) throws Exception {
	int count = 0; 
	try {
	    if (debug) System.out.println("execStatement: "+sql); 
	    count = stmt1.executeUpdate(sql); 
	} catch (Exception ex) {
	    if (debug) ex.printStackTrace(); 
	    if (!ignoreErrors) {
		System.out.println("Error executing -- "+sql); 
		throw ex; 
	    } else {
		if (debug) System.out.println("Error on "+sql+" ignored"); 
	    } 
	}
	return count; 
    }


    /**
     * Make sure everything is set up
     */

    public static void setup(Connection conn) throws Exception  {
        //
        // If we're still set up, then just return
        // 
	if (lastConn == conn) return;

	lastConn = conn;
	stmt = conn.createStatement();

        //
        // See if the lob tables exists 
	DatabaseMetaData dmd = conn.getMetaData();
	ResultSet rs = dmd.getTables(null, "JDJSTPENV", "BLOBS", null);
	if ( ! rs.next()) {
	    if (debug) System.out.println("BLOB table does not exist...creating");
	    JDSetupCollection.create(conn, "JDJSTPENV", false);
	    execStatement(stmt, "create table JDJSTPENV.BLOBS(blobName varchar(200), b blob(15M))",true);
	}
	rs.close(); 
    } 

    public static void copyToBlob(Connection conn, String filename, String blobname) throws Exception  {
	synchronized(conn) { 
	    setup(conn);

	    JDJSTPBlobUtil blob = new JDJSTPBlobUtil(filename);
	    execStatement(stmt, "delete from JDJSTPENV.BLOBS where blobName='"+blobname+"'", true); 
	    pstmt = conn.prepareStatement("insert into JDJSTPENV.BLOBS values(?, ?)");
	    pstmt.setString(1, blobname);
	    pstmt.setBlob(2, blob);
	    pstmt.executeUpdate(); 


	}
    } 

    public static void copyFromBlob(Connection conn, String filename, String blobname) throws Exception {
	synchronized(conn) {
	    setup(conn); 
	    ResultSet rs = stmt.executeQuery("select b from JDJSTPENV.BLOBS where blobName='"+blobname+"'");
	    if (rs.next()) {
		Blob blob = rs.getBlob(1); 
		InputStream blobStream = blob.getBinaryStream();

		OutputStream out = new FileOutputStream(filename);
		byte[] buffer = new byte [8192];
		int len;

		len = blobStream.read(buffer, 0, buffer.length); 
		while (len > 0) {
		    out.write(buffer, 0, len);
		    len = blobStream.read(buffer, 0, buffer.length); 
		} 
		out.close();
		blobStream.close();

	    } else {
		throw new Exception("Blob with name "+blobname+" not found"); 
	    } 


	}
    } 


    public static void usage() {
	System.out.println("Usage:");
	System.out.println("   java test.JDJSTPBlobUtil CopyToBlob    <System> <Filename> <Blobname>");
	System.out.println("   java test.JDJSTPBlobUtil CopyFromBlob  <System> <Filename> <Blobname>");
	System.out.println("       if <System> is localhost then current system used");
	System.exit(1); 
    } 

    /**
     * The main program to manipulate. 
     * For now, we use the native driver if on the iseries otherwise we use the toolbox driver
     *
     */
    public static void main(String[] args) {
      try {
	String url = null; 
	if (args.length < 3) {
	    usage(); 
	} else {
	  String system=args[1]; 
          //
          // Determine which driver to use
          //
	  if (JTOpenTestEnvironment.isOS400) {
	      Class.forName("com.ibm.db2.jdbc.app.DB2Driver");
	      url = "jdbc:db2:"+system;
	  } else {
	      Class.forName("com.ibm.as400.access.AS400JDBCDriver");
	      url = "jdbc:as400://"+system;
	  } 
  
	  Connection conn = DriverManager.getConnection (url);
	  if ( args[0].equals("CopyToBlob")) {
	      copyToBlob(conn, args[2], args[3]); 
	  } else if ( args[0].equals("CopyFromBlob")) {
	      copyFromBlob(conn, args[2], args[3]); 
	  } else {
	      System.out.println("Unrecognized option:  \""+args[0]+"\"");
	      usage(); 
	  } 


	}
      } catch (Exception e) {
	  e.printStackTrace();
	  System.exit(1); 
      } 
    } 



    /**
     * The instance method holds an actual blob that will be used to copy into the database
     */
    String filename;
    long blobLength = -1; 
    byte[] stuff = null; 
    public JDJSTPBlobUtil (String filename) throws Exception {
	this.filename=filename; 
    } 
    public long length() throws java.sql.SQLException {
	try {
	    if (blobLength == -1) { 
		File file = new File(filename);
	        blobLength = file.length();
	    }
	    return blobLength; 
	} catch (Exception e) {
	    throw new SQLException("Unable to determine length:  "+e); 
	}
    }

    public byte[] getBytes(long pos, int length) throws java.sql.SQLException {
        pos = pos-1; 
	try { 
	    if (stuff ==null) {
		length(); 
		stuff = new byte[(int) blobLength];

		InputStream inputStream = getBinaryStream();
		inputStream.read(stuff, 0, stuff.length);
		inputStream.close(); 
	    }
	    if (pos + length > blobLength) {
		length = (int) (blobLength - pos); 
	    } 

	    byte[] returnByte = new byte[length];
	    for (int i = 0; i < length; i++) {
		returnByte[i] = stuff[((int)pos)+i]; 
	    }
	    return returnByte; 
	} catch (Exception e) {
	    throw new SQLException("problem in getBytes: "+e); 
	} 

    }

    public void free() {
    }
    public java.io.InputStream getBinaryStream() throws java.sql.SQLException {
	try { 
	    return new FileInputStream(filename); 
	} catch (Exception e) { 
   	   throw new SQLException("Unable to open filename: "+e);
	}
    }

    public java.io.InputStream getBinaryStream(long a, long b)  throws java.sql.SQLException {
	throw new SQLException("not implemented"); 
    }

    public long position(byte[] patten , long start ) throws java.sql.SQLException {
	throw new SQLException("not implemented"); 
    }
    public long position(java.sql.Blob blob, long start ) throws java.sql.SQLException {
	throw new SQLException("not implemented"); 
    }
    public int setBytes(long pos, byte[] stuff ) throws java.sql.SQLException {
	throw new SQLException("not implemented"); 
    }
    public int setBytes(long a, byte[] b, int c, int d) throws java.sql.SQLException {
	throw new SQLException("not implemented"); 
    }
    public java.io.OutputStream setBinaryStream(long l) throws java.sql.SQLException {
	throw new SQLException("not implemented"); 
    }
    public void truncate(long l) throws java.sql.SQLException {
	throw new SQLException("not implemented"); 
    }



} 
