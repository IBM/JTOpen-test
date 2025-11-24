///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionCreateXXX.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDConnectionCreateXXX.java
//
// Classes:      JDConnectionCreateXXX
//
////////////////////////////////////////////////////////////////////////

package test.JD.Connection;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDConnectionTest;
import test.JDReflectionUtil;
import test.JDTestcase;

/**
Testcase JDConnectionCreateXXX.  This tests the following methods
of the JDBC Connection class:

<ul>
<li>createBlob()
<li>createClob()
<li>createNClob()
<li>createSQLXML()
</ul>
**/
public class JDConnectionCreateXXX
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDConnectionCreateXXX";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDConnectionTest.main(newArgs); 
   }


    // Private data.
    private              Statement      stmt_;
    private              String         collection = "JDCONTST";
    private              String         blobTable = null;
    private              String         clobTable = null;
    private              String         dbclobTable = null;
    private              String         nclobTable = null;
    private              String         SQLXMLTable = null;
    private              byte[]         oneMegBytes = null;
    private              byte[]         seventeenMegBytes = null;
    private              String         oneMegString = null;
    private              String         seventeenMegString = null;


/**
Constructor.
**/
    public JDConnectionCreateXXX (AS400 systemObject,
                             Hashtable<String,Vector<String>> namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             
                             String password) {
        super (systemObject, "JDConnectionCreateXXX",
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

	Runtime runtime = Runtime.getRuntime();
	long freeMemory = runtime.freeMemory();
	System.out.println("setup:  entry freeMemory is "+freeMemory );

        connection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);

	collection = JDConnectionTest.COLLECTION;

	stmt_ = connection_.createStatement();


	if (  getJdbcLevel() >= 4) {

	    blobTable   = collection+".JDCONBLOB";
	    clobTable   = collection+".JDCONCLOB";
	    dbclobTable = collection+".JDCONDLOB";
	    nclobTable  = collection+".JDCONNLOB";
	    SQLXMLTable = collection+".JDCONXML";

	    try { stmt_.executeUpdate("DROP TABLE "+blobTable);   } catch (Exception e) {}
	    try { stmt_.executeUpdate("DROP TABLE "+clobTable);   } catch (Exception e) {}
	    try { stmt_.executeUpdate("DROP TABLE "+dbclobTable);   } catch (Exception e) {}
	    try { stmt_.executeUpdate("DROP TABLE "+nclobTable);   } catch (Exception e) {}
	    try { stmt_.executeUpdate("DROP TABLE "+SQLXMLTable);   } catch (Exception e) {}

	    stmt_.executeUpdate("CREATE TABLE "+blobTable+" ( C1 BLOB(18M))");
	    stmt_.executeUpdate("CREATE TABLE "+clobTable+" ( C1 CLOB(18M) CCSID 37 )");
	    stmt_.executeUpdate("CREATE TABLE "+dbclobTable+"( C1 DBCLOB(18M))");
	    stmt_.executeUpdate("CREATE TABLE "+nclobTable+" ( C1 CLOB(18M) CCSID 1208)");
	    stmt_.executeUpdate("CREATE TABLE "+SQLXMLTable+" ( C1 CLOB(18M) CCSID 37 )");



	    oneMegBytes = new byte [1024 * 1024];
	    for(int i = 1023*1204 ; i < 1024*1024; i++) {
		oneMegBytes[i] = (byte) (i % 0x7F);
	    }

            StringBuffer sb = new StringBuffer();
	    sb.append("<d>");
            for (int i = 7; i < ( 1024 / 4 ) ; i++) {
	      int d0 = (i / 1000) % 10;
              int d1 = (i / 100) % 10;
              int d2 = (i / 10) % 10;
              int d3 = i % 10;
              sb.append(""+d0+d1+d2+d3);
            }
	    sb.append("</d>");
            String oneKString = sb.toString();
            for (int i = 1; i < 1024; i++) {
              sb.append(oneKString);
            }
            oneMegString = sb.toString();
            sb.setLength(0);
	    //
	    // Check to see if there is enough memory to create a 17 M string
	    //
	    freeMemory = runtime.freeMemory();
	    System.out.println("freeMemory is "+freeMemory );
	    System.gc(); 
	    freeMemory = runtime.freeMemory();
	    System.out.println("freeMemory after gc is "+freeMemory );
	    if (freeMemory < 20000000) {
		seventeenMegString = null ;
		System.out.println("Warning:  Not enough memory to create seventeenMegString"); 
	    } else {
		for (int i = 0; i < 17; i++) {
		    sb.append(oneMegString);
		}
		seventeenMegString = sb.toString();
	    }
	    freeMemory = runtime.freeMemory();
	    System.out.println("freeMemory is " + freeMemory);
	    if (freeMemory < 20000000) {
		System.out.println("Warning:  Not enough memory to create seventeenMegBytes"); 
		seventeenMegBytes = null;
	    } else {
		seventeenMegBytes = new byte[17 * 1024 * 1024];
		for (int i = 17 * 1023 * 1204; i < 17 * 1024 * 1024; i++) {
		    seventeenMegBytes[i] = (byte) (i % 0x7F);
		}
	    }
	}


    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {


	if (  getJdbcLevel() >= 4) {

	    stmt_.executeUpdate("DROP TABLE "+blobTable);
	    stmt_.executeUpdate("DROP TABLE "+clobTable);
	    stmt_.executeUpdate("DROP TABLE "+dbclobTable);
	    stmt_.executeUpdate("DROP TABLE "+nclobTable);
	    stmt_.executeUpdate("DROP TABLE "+SQLXMLTable);
	    stmt_.close();
	}

        connection_.close ();
        connection_ = null; 

    }





/**
createBlob() -- Create empty blob and make sure it is usable with a prepared statement
**/
    public void Var001() {
	if (checkJdbc40()) {

	    try {
		byte[] outBytes = null;
		Blob blob  = (Blob) JDReflectionUtil.callMethod_O(connection_, "createBlob");

		stmt_.executeUpdate("DELETE FROM "+blobTable);
		PreparedStatement pstmt = connection_.prepareStatement("INSERT INTO "+blobTable+" VALUES(?)");
		pstmt.setBlob(1, blob);
		pstmt.executeUpdate();
		pstmt.close();
		ResultSet rs = stmt_.executeQuery("SELECT * FROM "+blobTable);
		Blob outBlob = null;
		if (rs.next()) {
		    outBlob = rs.getBlob(1);
		    if (outBlob != null) {
			outBytes = outBlob.getBytes(1,(int) outBlob.length());
		    }
		}
		rs.close();
		assertCondition((blob != null) && (outBytes != null) && areEqual(blob.getBytes(1, (int) blob.length()), outBytes), "Not equal - blob="+blob+" outBlob="+outBlob    );
	    } catch (Exception e) {
		failed(e, "Unexpected Exception");
	    }
	}
    }


/**
createBlob() -- Create 1 meg blob and make sure it is usable with a prepared statement
**/
    public void Var002() {
	if (checkJdbc40()) {

	    try {
		byte[] outBytes = null;
		Blob blob  = (Blob) JDReflectionUtil.callMethod_O(connection_, "createBlob");
		blob.setBytes(1, oneMegBytes);
		stmt_.executeUpdate("DELETE FROM "+blobTable);
		PreparedStatement pstmt = connection_.prepareStatement("INSERT INTO "+blobTable+" VALUES(?)");
		pstmt.setBlob(1, blob);
		pstmt.executeUpdate();
		pstmt.close();
		ResultSet rs = stmt_.executeQuery("SELECT * FROM "+blobTable);
		Blob outBlob = null;
		if (rs.next()) {
		    outBlob = rs.getBlob(1);
		    if (outBlob != null) {
			outBytes=outBlob.getBytes(1,(int) outBlob.length());
		    }
		}
		rs.close();
		assertCondition((blob != null) && (outBlob != null) && areEqual(blob.getBytes(1, (int) blob.length()), outBytes), "Not equal - blob="+blob+" outBlob="+outBlob    );
	    } catch (Exception e) {
		failed(e, "Unexpected Exception");
	    }
	}
    }


/**
createBlob() -- Create 17 meg blob and make sure it is usable with a prepared statement
**/
    public void Var003() {
      if (seventeenMegBytes == null) {
        notApplicable("seventeenMegBytes was not allocated");
        return;
      }

	if (checkJdbc40()) {

	    try {
		byte[] outBytes = null;
		Blob blob  = (Blob) JDReflectionUtil.callMethod_O(connection_, "createBlob");
		blob.setBytes(1, seventeenMegBytes);
		stmt_.executeUpdate("DELETE FROM "+blobTable);
		PreparedStatement pstmt = connection_.prepareStatement("INSERT INTO "+blobTable+" VALUES(?)");
		pstmt.setBlob(1, blob);
		pstmt.executeUpdate();
		pstmt.close();
		ResultSet rs = stmt_.executeQuery("SELECT * FROM "+blobTable);
		Blob outBlob = null;
		if (rs.next()) {
		    outBlob = rs.getBlob(1);
		    if (outBlob != null) {
			outBytes=outBlob.getBytes(1,(int) outBlob.length());
		    }
		}
		rs.close();
		assertCondition((blob != null) && (outBlob != null) && areEqual(blob.getBytes(1, (int) blob.length()), outBytes) &&
				(outBytes.length == seventeenMegBytes.length)
				, "Not equal - blob="+blob+" outBlob="+outBlob +  " outBytes.length="+outBytes.length+" sb " + seventeenMegBytes.length  );
	    } catch (Exception e) {
		failed(e, "Unexpected Exception");
	    }
	}
    }


/**
createClob() -- Create empty Clob and make sure it is usable with a prepared statement
**/
    public void Var004() {
	if (checkJdbc40()) {

	    try {
		String outString=null;
		Clob clob  = (Clob) JDReflectionUtil.callMethod_O(connection_, "createClob");

		stmt_.executeUpdate("DELETE FROM "+clobTable);
		PreparedStatement pstmt = connection_.prepareStatement("INSERT INTO "+clobTable+" VALUES(?)");
		pstmt.setClob(1, clob);
		pstmt.executeUpdate();
		pstmt.close();
		ResultSet rs = stmt_.executeQuery("SELECT * FROM "+clobTable);
		Clob outClob = null;
		if (rs.next()) {
		    outClob = rs.getClob(1);
		    if (outClob != null) {
			outString = outClob.getSubString(1,(int) outClob.length());
		    }
		}
		rs.close();
		assertCondition((clob != null) && (outString != null) &&
                    clob.getSubString(1, (int) clob.length()).equals(outString), "Not equal - Clob="+clob+" outClob="+outClob    );
	    } catch (Exception e) {
		failed(e, "Unexpected Exception");
	    }
	}
    }


/**
createClob() -- Create 1 meg Clob and make sure it is usable with a prepared statement
**/
    public void Var005() {
	if (checkJdbc40()) {

	    try {
		String outString = null;
		Clob clob  = (Clob) JDReflectionUtil.callMethod_O(connection_, "createClob");
		clob.setString(1, oneMegString);
		stmt_.executeUpdate("DELETE FROM "+clobTable);
		PreparedStatement pstmt = connection_.prepareStatement("INSERT INTO "+clobTable+" VALUES(?)");
		pstmt.setClob(1, clob);
		pstmt.executeUpdate();
		pstmt.close();
		ResultSet rs = stmt_.executeQuery("SELECT * FROM "+clobTable);
		Clob outClob = null;
		if (rs.next()) {
		    outClob = rs.getClob(1);
		    if (outClob != null) {
			outString=outClob.getSubString(1,(int) outClob.length());
		    }
		}
		rs.close();
		assertCondition((clob != null) && (outClob != null) &&
                    clob.getSubString(1, (int) clob.length()).equals( outString), "Not equal - Clob="+clob+" outClob="+outClob    );
	    } catch (Exception e) {
		failed(e, "Unexpected Exception");
	    }
	}
    }


/**
createClob() -- Create 17 meg Clob and make sure it is usable with a prepared statement
**/
    public void Var006() {
      if (seventeenMegString == null) {
        notApplicable("seventeenMegString was not allocated");
        return;
      }
	if (checkJdbc40()) {

	    //
	    // Note:  This does not work with the Classic JVM
            //        and jt400native.jar.
            //
	    String jvmName = System.getProperty("java.vm.name");
	    if (jvmName.indexOf("Classic") >=  0) { 
		String classpath = System.getProperty("java.class.path");
		if (classpath.indexOf("jt400native.jar") >= 0) {
		    notApplicable("Fails with OOM error for Classic JVM and jt400native.jar");
		    return; 
		} 
	    }

	    try {
		String outString = null;
		Clob clob  = (Clob) JDReflectionUtil.callMethod_O(connection_, "createClob");
		clob.setString(1, seventeenMegString);
		stmt_.executeUpdate("DELETE FROM "+clobTable);
		PreparedStatement pstmt = connection_.prepareStatement("INSERT INTO "+clobTable+" VALUES(?)");
		pstmt.setClob(1, clob);
		pstmt.executeUpdate();
		pstmt.close();
		ResultSet rs = stmt_.executeQuery("SELECT * FROM "+clobTable);
		Clob outClob = null;
		if (rs.next()) {
		    outClob = rs.getClob(1);
		    if (outClob != null) {
			outString=outClob.getSubString(1,(int) outClob.length());
		    }
		}
		rs.close();
                rs = stmt_.executeQuery("SELECT LENGTH(C1) FROM "+clobTable);
                rs.next();
                int clobLength = rs.getInt(1);
		assertCondition((clob != null) && (outClob != null) &&
                    seventeenMegString.equals(outString),
                    "Not equal - Expected length = "+seventeenMegString.length()+
                    " outLength ="+outString.length()+" clobLength="+clobLength    );
	    } catch (Throwable e) {
		failed(e, "Unexpected Exception");
	    }
	}
    }


    /**
    createClob() -- Create empty NClob and make sure it is usable with a prepared statement
    **/
        public void Var007() {
        if (checkJdbc40()) {

            try {
                String outString=null;
                Object nclob  = JDReflectionUtil.callMethod_O(connection_, "createNClob");

                stmt_.executeUpdate("DELETE FROM "+clobTable);
                PreparedStatement pstmt = connection_.prepareStatement("INSERT INTO "+clobTable+" VALUES(?)");
                JDReflectionUtil.callMethod_V(pstmt, "setNClob", 1, nclob);
                pstmt.executeUpdate();
                pstmt.close();
                ResultSet rs = stmt_.executeQuery("SELECT * FROM "+clobTable);
                Object outNClob = null;
                if (rs.next()) {

                    outNClob = JDReflectionUtil.callMethod_O(rs, "getClob", 1);
                    if (outNClob != null) {
                      long outLength = JDReflectionUtil.callMethod_L(outNClob, "length");
                      outString = JDReflectionUtil.callMethod_S(outNClob, "getSubString", (long) 1, (int) outLength);
                    }
                }
                rs.close();
                long length = JDReflectionUtil.callMethod_L(nclob, "length");
                String value = JDReflectionUtil.callMethod_S(nclob, "getSubString", (long) 1, (int) length);

                assertCondition((nclob != null) && (outString != null) &&
                        value.equals(outString), "Not equal - Clob="+nclob+" outClob="+outNClob    );
            } catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
        }


    /**
    createClob() -- Create 1 meg NClob and make sure it is usable with a prepared statement
    **/
        public void Var008() {
        if (checkJdbc40()) {

            try {
                String outString = null;
                Object clob  = (Clob) JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(clob, "setString", (long) 1, oneMegString);
                stmt_.executeUpdate("DELETE FROM "+clobTable);
                PreparedStatement pstmt = connection_.prepareStatement("INSERT INTO "+clobTable+" VALUES(?)");
                JDReflectionUtil.callMethod_V(pstmt, "setNClob", 1, clob);

                pstmt.executeUpdate();
                pstmt.close();
                ResultSet rs = stmt_.executeQuery("SELECT * FROM "+clobTable);
                Clob outClob = null;
                if (rs.next()) {
                    outClob = rs.getClob(1);
                    if (outClob != null) {
                        outString=outClob.getSubString(1,(int) outClob.length());
                    }
                }
                rs.close();
                assertCondition((clob != null) && (outClob != null) &&
                        oneMegString.equals( outString), "Not equal - Clob="+oneMegString+" outClob="+outClob    );
            } catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
        }


    /**
    createClob() -- Create 17 meg NClob and make sure it is usable with a prepared statement
    **/
        public void Var009() {
          if (seventeenMegString == null) {
            notApplicable("seventeenMegString was not allocated");
            return;
          }
	    //
	    // Note:  This does not work with the Classic JVM
	    //        and jt400native.jar.
	    //
	  String jvmName = System.getProperty("java.vm.name");
	  if (jvmName.indexOf("Classic") >=  0) { 
	      String classpath = System.getProperty("java.class.path");
	      if (classpath.indexOf("jt400native.jar") >= 0) {
		  notApplicable("Fails with OOM error for Classic JVM and jt400native.jar");
		  return; 
	      } 
	  }


          if (checkJdbc40()) {

            try {
                String outString = null;
                Object clob  = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(clob, "setString", (long) 1, seventeenMegString);
                stmt_.executeUpdate("DELETE FROM "+clobTable);
                PreparedStatement pstmt = connection_.prepareStatement("INSERT INTO "+clobTable+" VALUES(?)");
                JDReflectionUtil.callMethod_V(pstmt, "setNClob", 1, clob);

                pstmt.executeUpdate();
                pstmt.close();
                ResultSet rs = stmt_.executeQuery("SELECT * FROM "+clobTable);
                Clob outClob = null;
                if (rs.next()) {
                    outClob = rs.getClob(1);
                    if (outClob != null) {
                        outString=outClob.getSubString(1,(int) outClob.length());
                    }
                }
                rs.close();
                rs = stmt_.executeQuery("SELECT LENGTH(C1) FROM "+clobTable);
                rs.next();
                int clobLength = rs.getInt(1);
                assertCondition((clob != null) && (outClob != null) &&
                        seventeenMegString.equals( outString),
                        "Not equal - Clob=length="+seventeenMegString.length()+
                        " outClob=length="+outString.length()+" clob length="+clobLength    );
            } catch (Throwable e) {
                failed(e, "Unexpected Exception");
            }
        }

        }


/**
createSQLXML() -- Create small SQLXML and make sure it is usable with a prepared statement
**/
    public void Var010() {
	if (checkJdbc40()) {

	    try {
		String outString=null;
		String stringValue = "<T/>";

		Object sqlxml  = JDReflectionUtil.callMethod_O(connection_, "createSQLXML");
		JDReflectionUtil.callMethod_V(sqlxml,"setString",stringValue);
		stmt_.executeUpdate("DELETE FROM "+SQLXMLTable);
		PreparedStatement pstmt = connection_.prepareStatement("INSERT INTO "+SQLXMLTable+" VALUES(?)");
		JDReflectionUtil.callMethod_V(pstmt, "setSQLXML", 1, sqlxml);
		pstmt.executeUpdate();
		pstmt.close();
		ResultSet rs = stmt_.executeQuery("SELECT * FROM "+SQLXMLTable);
		Object outSQLXML = null;
		if (rs.next()) {
                    outSQLXML = JDReflectionUtil.callMethod_O(rs,"getSQLXML", 1);
		    if (outSQLXML != null) {

			outString =  JDReflectionUtil.callMethod_S(outSQLXML, "getString");
		    }
		}
		rs.close();
		assertCondition((sqlxml != null) && (outString != null) &&
                    JDReflectionUtil.callMethod_S(sqlxml, "getString").equals(outString), "Not equal - SQLXML="+sqlxml+" outSQLXML="+outSQLXML    );
	    } catch (Exception e) {
		failed(e, "Unexpected Exception");
	    }
	}
    }


/**
createSQLXML() -- Create 1 meg SQLXML and make sure it is usable with a prepared statement
**/
    public void Var011() {
	if (checkJdbc40()) {

	    try {
		String outString = null;
		Object sqlxml  =  JDReflectionUtil.callMethod_O(connection_, "createSQLXML");
		JDReflectionUtil.callMethod_V(sqlxml, "setString", oneMegString);
		stmt_.executeUpdate("DELETE FROM "+SQLXMLTable);
		PreparedStatement pstmt = connection_.prepareStatement("INSERT INTO "+SQLXMLTable+" VALUES(?)");
		JDReflectionUtil.callMethod_V(pstmt, "setSQLXML", 1, sqlxml);
                pstmt.executeUpdate();
		pstmt.close();
		ResultSet rs = stmt_.executeQuery("SELECT * FROM "+SQLXMLTable);
		Object outSQLXML = null;
		if (rs.next()) {
                    outSQLXML = JDReflectionUtil.callMethod_O(rs,"getSQLXML", 1);
		    if (outSQLXML != null) {
			outString=JDReflectionUtil.callMethod_S(outSQLXML, "getString");

		    }
		}
		rs.close();
		assertCondition((sqlxml != null) && (outSQLXML != null) &&
                    JDReflectionUtil.callMethod_S(sqlxml, "getString").equals( outString),
                    "Not equal - SQLXML="+sqlxml+" outSQLXML="+outSQLXML    );
	    } catch (Exception e) {
		failed(e, "Unexpected Exception");
	    }
	}
    }


/**
createSQLXML() -- Create 17 meg SQLXML and make sure it is usable with a prepared statement
**/
    public void Var012() {
	if (checkJdbc40()) {

    if (seventeenMegString == null) {
      notApplicable("seventeenMegString was not allocated");
      return;
    }

	    try {
                String outString = null;
                Object sqlxml  =  JDReflectionUtil.callMethod_O(connection_, "createSQLXML");
                JDReflectionUtil.callMethod_V(sqlxml, "setString", seventeenMegString);
                stmt_.executeUpdate("DELETE FROM "+SQLXMLTable);
                PreparedStatement pstmt = connection_.prepareStatement("INSERT INTO "+SQLXMLTable+" VALUES(?)");
                JDReflectionUtil.callMethod_V(pstmt, "setSQLXML", 1, sqlxml);
                pstmt.executeUpdate();
                pstmt.close();
                ResultSet rs = stmt_.executeQuery("SELECT * FROM "+SQLXMLTable);
                Object outSQLXML = null;
                if (rs.next()) {
                    outSQLXML = JDReflectionUtil.callMethod_O(rs,"getSQLXML", 1);
                    if (outSQLXML != null) {
                        outString=JDReflectionUtil.callMethod_S(outSQLXML, "getString");

                    }
                }
                rs.close();
                assertCondition((sqlxml != null) && (outSQLXML != null) &&
                    JDReflectionUtil.callMethod_S(sqlxml, "getString").equals( outString),
                    "Not equal - SQLXML="+sqlxml+" outSQLXML="+outSQLXML    );

                    } catch (Exception e) {
		failed(e, "Unexpected Exception");
	    }
	}
    }


/**
createSQLXML() -- Create small SQLXML and make sure it is usable with a prepared statement
**/
    public void Var013() {
	if (checkJdbc40()) {

	    try {
		String outString=null;
		Object sqlxml  = JDReflectionUtil.callMethod_O(connection_, "createSQLXML");

		OutputStream os = (OutputStream) JDReflectionUtil.callMethod_O(sqlxml, "setBinaryStream");
		byte[] stuff = { (byte)'<',(byte)'?',(byte)'x',(byte)'m',(byte)'l',(byte)' ',(byte)'v',(byte)'e',(byte)'r',(byte)'s',(byte)'i',(byte)'o',(byte)'n',(byte)'=',(byte)'"',(byte)'1',(byte)'.',(byte)'0',(byte)'"',(byte)'?',(byte)'>'
		};
		os.write(stuff);
		os.close();

		stmt_.executeUpdate("DELETE FROM "+blobTable);
		PreparedStatement pstmt = connection_.prepareStatement("INSERT INTO "+blobTable+" VALUES(?)");
		JDReflectionUtil.callMethod_V(pstmt, "setSQLXML", 1, sqlxml);
		pstmt.executeUpdate();
		pstmt.close();
		ResultSet rs = stmt_.executeQuery("SELECT * FROM "+blobTable);
		Object outSQLXML = null;
		if (rs.next()) {
                    outSQLXML = JDReflectionUtil.callMethod_O(rs,"getSQLXML", 1);
		    if (outSQLXML != null) {

			outString =  JDReflectionUtil.callMethod_S(outSQLXML, "getString");
		    }
		}
		rs.close();
		assertCondition((sqlxml != null) && (outString != null) &&
                    JDReflectionUtil.callMethod_S(sqlxml, "getString").equals(outString), "Not equal - SQLXML="+sqlxml+" outSQLXML="+outSQLXML+" outString="+outString    );
	    } catch (Exception e) {
	        if(isToolboxDriver())
	            assertCondition(e.getMessage().indexOf("mismatch") != -1);
	        else
                failed(e, "Unexpected Exception");
	    }
	}
    }



/**
createSQLXML() -- Create small SQLXML (declaration only) and make sure it is usable with a prepared statement
**/
    public void Var014() {
	if (checkJdbc40()) {

	    try {
		String outString=null;
		String outString2=null;
		Object sqlxml  = JDReflectionUtil.callMethod_O(connection_, "createSQLXML");

		OutputStream os = (OutputStream) JDReflectionUtil.callMethod_O(sqlxml, "setBinaryStream");
		byte[] stuff = { (byte)'<',(byte)'?',(byte)'x',(byte)'m',(byte)'l',(byte)' ',(byte)'v',(byte)'e',(byte)'r',(byte)'s',(byte)'i',(byte)'o',(byte)'n',(byte)'=',(byte)'"',(byte)'1',(byte)'.',(byte)'0',(byte)'"',(byte)'?',(byte)'>',(byte)'<', (byte)'T', (byte)'/', (byte)'>',
		};
		os.write(stuff);
		os.close();

		stmt_.executeUpdate("DELETE FROM "+SQLXMLTable);
		PreparedStatement pstmt = connection_.prepareStatement("INSERT INTO "+SQLXMLTable+" VALUES(?)");
		JDReflectionUtil.callMethod_V(pstmt, "setSQLXML", 1, sqlxml);
		pstmt.executeUpdate();
		pstmt.close();
		ResultSet rs = stmt_.executeQuery("SELECT * FROM "+SQLXMLTable);
		Object outSQLXML = null;
		if (rs.next()) {
                    outSQLXML = JDReflectionUtil.callMethod_O(rs,"getSQLXML", 1);
		    if (outSQLXML != null) {

			outString =  JDReflectionUtil.callMethod_S(outSQLXML, "getString");
		    }
		    outString2 = rs.getString(1);
		}
		rs.close();
		String dummy = "";
        if(isToolboxDriver())
             dummy = "Toolbox fix: XML code will be checked in with v7r1";

		assertCondition((sqlxml != null) && (outString != null) &&
                    JDReflectionUtil.callMethod_S(sqlxml, "getString").equals(outString), "Not equal - SQLXML="+sqlxml+" outSQLXML="+outSQLXML+" outString="+outString +" outString2="+outString2  + dummy );
	    } catch (Exception e) {
	        failed(e, "Unexpected Exception");
	    }
	}
    }


}



