///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetObject41.java
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
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable; import java.util.Vector;



/**
Testcase JDRSGetObject41.  This tests the following method
of the JDBC ResultSet class in JDBC 4.1.
Both the native and toolbox JDBC drivers support this method in all JDKs.
This is possible because Generics is really a compiler, not runtime, implementation. 

<ul>
<li>getObject()
</ul>
**/
public class JDRSGetObject41
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSGetObject41";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



    // Private data.
    private Statement           statement0_;
    private Statement           statement_;
    private ResultSet           rs_;

    private Hashtable<String,String> foundExceptions_; 


/**
Constructor.
**/
    public JDRSGetObject41 (AS400 systemObject,
                          Hashtable<String,Vector<String>> namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDRSGetObject41",
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
        String url = baseURL_
                     // String url = "jdbc:as400://" + systemObject_.getSystemName()
                     
                     
                     + ";translate hex=binary"
	             + ";time format=jis"
                     + ";date format=iso"; 
              
          System.out.println("Connecting to url "+baseURL_); 
        connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
	setAutoCommit(connection_, false); // @E1A

        statement0_ = connection_.createStatement ();

    if (isJdbc20()) {
      collection_ = JDRSTest.COLLECTION; 
      statement_ = connection_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
          ResultSet.CONCUR_UPDATABLE);
      statement_.executeUpdate("INSERT INTO " + JDRSTest.RSTEST_GET
          + " (C_KEY) VALUES ('DUMMY_ROW')");
      rs_ = statement_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET + " FOR UPDATE");

      if (areBooleansSupported()) {
        String sql = "";
        try {
          sql = "DROP TABLE " + collection_ + ".JDRSGOBOOL";
          try {
            statement_.execute(sql);
          } catch (Exception e) {
          }

          sql = "CREATE TABLE " + collection_
              + ".JDRSGOBOOL (C1 BOOLEAN, C2 BOOLEAN, C3 BOOLEAN)";
          statement_.execute(sql);

          sql = "INSERT INTO  " + collection_
              + ".JDRSGOBOOL VALUES(?, ?, NULL)";
          PreparedStatement ps = connection_.prepareStatement(sql);
          try {
            ps.setBoolean(1, true);
          } catch (SQLException e) {
            byte[] stuff = { (byte) 0xF1 };
            ps.setBytes(1, stuff);
          }

          try {
            ps.setBoolean(2, false);
          } catch (SQLException e) {
            byte[] stuff = { (byte) 0xF0 };
            ps.setBytes(2, stuff);
          }
          ps.execute(); 
          ps.close(); 
        } catch (SQLException sqlex) {
          System.out.println("Warning: Boolean setup failure on " + sql);
          sqlex.printStackTrace(System.out);
          sql = "VALUES CURRENT SERVER"; 
          ResultSet rs = statement_.executeQuery(sql); 
          rs.next(); 
          System.out.println("Current server is "+rs.getString(1));
        }

      }
      connection_.commit(); 

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
            rs_.close ();
            statement_.close ();
        }
        statement0_.close ();
        connection_.commit(); // @E1A
        connection_.close ();
    }



/**
getObject() - Should throw exception when the result set is
closed.
**/
    public void Var001()
    {
      if (checkJdbc41())
        try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                                           + JDRSTest.RSTEST_GET);
            rs.next ();
            rs.close ();
	    JDReflectionUtil.callMethod_O(rs, "getObject", 1, String.class);
            failed ("Didn't throw SQLException");
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getObject() - Should throw exception when cursor is not pointing
to a row.
**/
    public void Var002()
    {
      if (checkJdbc41())
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            Object s = 	    JDReflectionUtil.callMethod_O(rs, "getObject", 1, String.class);
            failed ("Didn't throw SQLException"+s);
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getObject() - Should throw an exception when the column
is an invalid index.
**/
    public void Var003()
    {
      if (checkJdbc41())
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
	    rs.next(); 
            Object s =   JDReflectionUtil.callMethod_O(rs, "getObject", 100, String.class);
            failed ("Didn't throw SQLException"+s);
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getObject() - Should throw an exception when the column
is 0.
**/
    public void Var004()
    {
      if (checkJdbc41())
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            Object s =  JDReflectionUtil.callMethod_O(rs, "getObject", 0, String.class);
            failed ("Didn't throw SQLException"+s);
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getObject() - Should throw an exception when the column
is -1.
**/
    public void Var005()
    {
      if (checkJdbc41())
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            Object s =  JDReflectionUtil.callMethod_O(rs, "getObject", -1, String.class);
            failed ("Didn't throw SQLException"+s);
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getObject() - Should work when the column index is valid.
**/
    public void Var006()
    {
      if (checkJdbc41())
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s =  (String) JDReflectionUtil.callMethod_O(rs, "getObject", 1, String.class);
            assertCondition (s.equals ("DATE_1998"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getObject() - Should throw an exception when the column
name is null.
**/
    public void Var007()
    {
      if (checkJdbc41())
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            Object s =  JDReflectionUtil.callMethod_O(rs, "getObject", (String) null, String.class);
            failed ("Didn't throw SQLException"+s);
        } catch (Exception e) {
            if (getDriver() == JDTestDriver.DRIVER_JCC) {
              assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
            } else { 
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getObject() - Should throw an exception when the column
name is an empty string.
**/
    public void Var008()
    {
      if (checkJdbc41())
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            Object s =  JDReflectionUtil.callMethod_O(rs, "getObject", "", String.class);
            failed ("Didn't throw SQLException"+s);
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getObject() - Should throw an exception when the column
name is invalid.
**/
    public void Var009()
    {if (checkJdbc41())
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            Object s =  JDReflectionUtil.callMethod_O(rs, "getObject", "INVALID", String.class);
            failed ("Didn't throw SQLException"+s);
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }





    /*
     * get getObject using the following testSpec, where the elements of the array are as follows
     * testSpec[0]  className
     * testSpec[1]  queryToRun
     * testSpec[2]  expected value for column 1
     * testSpec[3]  expected value for column 2
     * ....
     */ 
    public void testGetObject(String[] testSpec) {
      if (checkJdbc41()) {
	StringBuffer sb = new StringBuffer();
	boolean passed = true; 
	try {
	    String className = testSpec[0];
	    String query     = testSpec[1];
	    int columnCount = testSpec.length - 2;
	    sb.append("Testing class "+className+"\n");
	    Class<?> lookupClass = Class.forName(className); 
	    
	    Class<?> xmlClass = null;
      try {
        xmlClass = Class.forName("java.sql.SQLXML");
      } catch (Exception e) {
      }

      Class<?> rowidClass = null;
      try {
        rowidClass = Class.forName("java.sql.RowId");
      } catch (Exception e) {
      }

	    sb.append("Running query "+query+"\n");

	    ResultSet rs = statement0_.executeQuery(query); 
	    rs.next();
	    for (int i = 1; i <= columnCount; i++) {
		String expected = testSpec[i+1]; 
		try { 
		    Object o = JDReflectionUtil.callMethod_O(rs, "getObject", i, lookupClass);
                    String objectString;
                    String objectClassName; 
                    Class<?> objectClass = null; 
                    if (o == null) {
                      objectString="null";
                      objectClass = lookupClass; 
                      objectClassName = lookupClass.getName(); 
                    } else { 
                      objectClass = o.getClass(); 
                      objectClassName = objectClass.getName();
                      
                      if (objectClassName.equals("[B")) {
                        
                        objectString = "BYTEARRAY="+bytesToString((byte[]) o); 
                      } else if (o instanceof java.io.InputStream) {
                          objectString = "INPUTSTREAM="+inputStreamToString((java.io.InputStream) o); 
                      } else if (o instanceof java.io.Reader) {
                        objectString = "READER="+readerToString((java.io.Reader) o); 
                      } else if (o instanceof java.sql.Blob) {
                        InputStream in = ((java.sql.Blob) o).getBinaryStream();  
                        objectString = "BLOB="+inputStreamToString(in); 
                      } else if (o instanceof java.sql.Clob) {
                        Reader in = ((java.sql.Clob) o).getCharacterStream();  
                        objectString = "CLOB="+readerToString(in); 
                      } else if ( rowidClass != null &&  rowidClass.isAssignableFrom(objectClass)) {
                        byte[] stuff = (byte[]) JDReflectionUtil.callMethod_O(o, "getBytes"); 
                        objectString = "ROWID="+bytesToString(stuff); 
                      } else if ( xmlClass != null  && xmlClass.isAssignableFrom(objectClass)) {
                        Reader in = (Reader) JDReflectionUtil.callMethod_O(o, "getCharacterStream"); 
                        objectString = "SQLXML="+readerToString(in); 
                      } else { 
		         objectString = o.toString();
                      }
                    }
		    if (expected.equals(objectString)) {
			// Object is good 
		    } else {
			sb.append("For column "+i+" got "+objectString+" sb "+expected+"\n");
			passed = false; 
		    } 

		    if (objectClassName.equals(className)) {
		      // Class is good 
		    } else {
		      if (lookupClass.isAssignableFrom(objectClass)) {
		        // Still good 
		      } else { 
		        sb.append("For column "+i+" got class="+objectClassName+" sb class="+className+"\n");
                        passed = false;
		      }
		    }
		    
		} catch (Exception e) {
		    String exceptionString = e.toString();
		    if (exceptionString == null) {
		      exceptionString = e.getClass().getName()+":null"; 
		    }
		    if (exceptionString.indexOf(expected) >= 0) {
			// Exception is good 
		    } else {
			sb.append("For column "+i+" got exception "+exceptionString+" sb "+expected+"\n");
			StringWriter stringWriter = new StringWriter(); 
			PrintWriter printWriter = new PrintWriter(stringWriter);
			// Only dump the exception once 
			e.printStackTrace(printWriter);
			String exception = stringWriter.toString();
			String checkException = exception; 
			int getObjectIndex = checkException.indexOf("JDRSGetObject41");
			if (getObjectIndex >= 0) {
			    checkException = checkException.substring(0,getObjectIndex); 
			} 

			if (foundExceptions_ == null) {
			    foundExceptions_ = new Hashtable<String,String>(); 
			}
			if (foundExceptions_.get(checkException) == null) {
			    foundExceptions_.put(checkException, checkException);
			    sb.append(exception); 
			} else {
			    // Skip 
			} 
			    
			passed = false; 
		    } 
		} 
	    } 
	    rs.close();
	    assertCondition(passed,sb);
	} catch (Exception e) {
            failed (e, "Unexpected Exception "+sb.toString());
	} 
    } 
    }

    /* Test LocalDate with SQLXML X0 */ 
    public void Var010()
    {
      if (checkJdbc41()) {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.lang.String",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ), CAST(null as XML) from sysibm.sysdummy1", 
              "<h>hello</h>",
              "null"};
           testGetObject(testArray);
        }
      }
    }



    /* Test LocalDate with primitive numbers X1 */ 
    public void Var011()
    {
	String[] testArray = {
	    "java.lang.String",
	    "select cast(1 as SMALLINT), cast(2 as INTEGER), cast(3 as BIGINT), cast(null as SMALLINT), cast(null as INTEGER), cast(null as BIGINT) from sysibm.sysdummy1",
	    "1",
	    "2",
	    "3", 
	    "null",
	    "null",
	    "null"};
        testGetObject(testArray); 
    }

    /* Test LocalDate with primitive doubles X2 */ 
    public void Var012()
    {
        String[] testArray = {
            "java.lang.String",
            "select cast(1.0 as REAL), cast(2.0 as FLOAT), cast(3.0 as DOUBLE), cast(null as REAL), cast(null as FLOAT), cast(null as DOUBLE)  from sysibm.sysdummy1",
            "1.0",
            "2.0",
            "3.0",
            "null",
            "null",
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test LocalDate with decimal X3 */ 
    public void Var013()
    {
        String[] testArray = {
            "java.lang.String",
            "select cast(1.0 as DECIMAL(10,1)), cast(2.0 as NUMERIC(10,2)), cast(3.0 as DECIMAL(10,4)), cast(null as DECIMAL(10,1)), cast(null as NUMERIC(10,2)), cast(null as DECIMAL(10,4)) from sysibm.sysdummy1",
            "1.0",
            "2.00",
            "3.0000",
            "null",
            "null",
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test LocalDate with char, graphic, varchar, vargraphic   X4 */ 
    public void Var014()
    {
        String[] testArray = {
            "java.lang.String",
            "select cast('ABC' as CHAR(10)), cast('ABC' as GRAPHIC(10) CCSID 1200), cast(null as GRAPHIC(10)), cast('ABC' as VARCHAR(10)), cast('ABC' as VARGRAPHIC(10) CCSID 1200), cast(null as VARGRAPHIC(10)) from sysibm.sysdummy1",
            "ABC       ",
            "ABC       ",
            "null",
            "ABC",
            "ABC",
            "null"};
        testGetObject(testArray); 
    }
    


    /* Test LocalDate with BINARY, VARBINARY  X5 */   
    public void Var015()
    {
        String[] testArray = {
            "java.lang.String",
            "select cast(X'313233' AS BINARY(10)), cast (X'313233' AS VARBINARY(20)),cast(null AS BINARY(10)), cast (null AS VARBINARY(20)) from sysibm.sysdummy1",
            "31323300000000000000",
            "313233", 
            "null", 
            "null"};


        String[] testArrayNative = {
            "java.lang.String",
            "select cast(X'313233' AS BINARY(10)), cast (X'313233' AS VARBINARY(20)),cast(null AS BINARY(10)), cast (null AS VARBINARY(20)) from sysibm.sysdummy1",
            "\u0031\u0032\u0033\u0000\u0000\u0000\u0000\u0000\u0000\u0000",
            "\u0031\u0032\u0033", 
            "null", 
            "null"};

	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

        testGetObject(testArray); 
    }

    /* Test LocalDate with date, time, timestamp  X6 */ 
    public void Var016()
    {
        String[] testArray = {
            "java.lang.String",
            "select cast('1:02:03' as TIME), cast ('2011-09-11' as DATE), cast('2011-09-11-01.02.03.123456' AS TIMESTAMP), cast(null as TIME), cast (null as DATE), cast(null AS TIMESTAMP) from sysibm.sysdummy1",
            "01:02:03",
            "2011-09-11",
            "2011-09-11 01:02:03.123456", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test LocalDate with clob, dbclob, blob  X7 */ 
    public void Var017()
    {
        String[] testArray = {
            "java.lang.String",
            "select cast('ABC' as CLOB), cast ('ABC' as DBCLOB(1M) CCSID 1200), cast(X'313233' AS BLOB), cast(null as CLOB), cast (null as DBCLOB(1M) CCSID 1200), cast(null AS BLOB) from sysibm.sysdummy1",
            "ABC",
            "ABC",
            "313233", 
            "null", 
            "null", 
            "null"};

        String[] testArrayNative = {
            "java.lang.String",
            "select cast('ABC' as CLOB), cast ('ABC' as DBCLOB(1M) CCSID 1200), cast(X'313233' AS BLOB), cast(null as CLOB), cast (null as DBCLOB(1M) CCSID 1200), cast(null AS BLOB) from sysibm.sysdummy1",
            "ABC",
            "ABC",
            "\u0031\u0032\u0033", 
            "null", 
            "null", 
            "null"};



	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 


        testGetObject(testArray); 
    }
    

    /* Test LocalDate with datalink  X8 */ 
    public void Var018()
    {
        String[] testArray = {
            "java.lang.String",
            "select DLVALUE('file://tmp/x'), CAST(NULL AS DATALINK) from sysibm.sysdummy1",
            "FILE://TMP/x", 
            "null"};
        testGetObject(testArray); 
    }

    /* Test LocalDate with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var019()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.lang.String",
            "select CAST(CAST(X'1122' AS CHAR(2) FOR BIT DATA) AS ROWID), CAST('ABC' as NCHAR(10)), CAST('ABC' AS NVARCHAR(100)), CAST('ABC' AS NCLOB(100K)), CAST(null AS ROWID), CAST(null as NCHAR(10)), CAST(null AS NVARCHAR(100)), CAST(null AS NCLOB(100K)) from sysibm.sysdummy1",
            "1122",
            "ABC       ",
            "ABC",
            "ABC", 
            "null", 
            "null", 
            "null",
            "null"};
        testGetObject(testArray);
      }
    }


    /* Test java.lang.Byte 2X with SQLXML X0 */ 
    public void Var020()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.lang.Byte",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ), CAST(NULL AS XML) from sysibm.sysdummy1", 
              "Data type mismatch",
              "null",};
           testGetObject(testArray);
        }
    }



    /* Test java.lang.Byte 2X with primitive numbers X1 */ 
    public void Var021()
    {
        String[] testArray = {
            "java.lang.Byte",
            "select cast(1 as SMALLINT), cast(2 as INTEGER), cast(3 as BIGINT), cast(null as SMALLINT), cast(null as INTEGER), cast(null as BIGINT)  from sysibm.sysdummy1",
            "1",
            "2",
            "3", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }

    /* Test java.lang.Byte 2X with primitive doubles X2 */ 
    public void Var022()
    {
        String[] testArray = {
            "java.lang.Byte",
            "select cast(1.0 as REAL), cast(2.0 as FLOAT), cast(3.0 as DOUBLE), cast(null as REAL), cast(null as FLOAT), cast(null as DOUBLE) from sysibm.sysdummy1",
            "1",
            "2",
            "3", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test java.lang.Byte 2X with decimal X3 */ 
    public void Var023()
    {
        String[] testArray = {
            "java.lang.Byte",
            "select cast(1.0 as DECIMAL(10,1)), cast(2.0 as NUMERIC(10,2)), cast(3.0 as DECIMAL(10,4)), cast(null as DECIMAL(10,1)), cast(null as NUMERIC(10,2)), cast(null as DECIMAL(10,4)) from sysibm.sysdummy1",
            "1",
            "2",
            "3", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test java.lang.Byte 2X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var024()
    {
        String[] testArray = {
            "java.lang.Byte",
            "select cast('1' as CHAR(10)), cast('2' as GRAPHIC(10) CCSID 1200), cast(null as GRAPHIC(10)), cast('1' as VARCHAR(10)), cast('2' as VARGRAPHIC(10) CCSID 1200), cast(null as VARGRAPHIC(10)) from sysibm.sysdummy1",
            "1",
            "2",
            "null",
            "1",
            "2",
            "null"};
        testGetObject(testArray); 
    }
    


    /* Test java.lang.Byte 2X with BINARY, VARBINARY  X5 */ 
    public void Var025()
    {
        String[] testArray = {
            "java.lang.Byte",
            "select cast(X'01' AS BINARY(1)), cast (X'02' AS VARBINARY(20)),cast(null AS BINARY(1)), cast (null AS VARBINARY(20)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null"};
        testGetObject(testArray); 
    }

    /* Test java.lang.Byte 2X with date, time, timestamp  X6 */ 
    public void Var026()
    {
        String[] testArray = {
            "java.lang.Byte",
            "select cast('1:02:03' as TIME), cast ('2011-09-11' as DATE), cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObject(testArray); 
    }
    
    /* Test java.lang.Byte 2X with clob, dbclob, blob  X7 */ 
    public void Var027()
    {
        String[] testArray = {
            "java.lang.Byte",
            "select cast('1' as CLOB), cast ('2' as DBCLOB(1M) CCSID 1200), cast(X'03' AS BLOB) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObject(testArray); 
    }
    

    /* Test java.lang.Byte 2X with datalink  X8 */ 
    public void Var028()
    {
        String[] testArray = {
            "java.lang.Byte",
            "select DLVALUE('file://tmp/x') from sysibm.sysdummy1",
            "Data type mismatch"};
        testGetObject(testArray); 
    }

    /* Test java.lang.Byte 2X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var029()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.lang.Byte",
            "select CAST(CAST(X'01' AS CHAR(2) FOR BIT DATA) AS ROWID), CAST('1' as NCHAR(10)), CAST('1' AS NVARCHAR(100)), CAST('1' AS NCLOB(100K)), CAST(null AS ROWID), CAST(null as NCHAR(10)), CAST(null AS NVARCHAR(100)), CAST(null AS NCLOB(100K)) from sysibm.sysdummy1",
            "Data type mismatch",
            "1",
            "1",
            "Data type mismatch",
            "null",
            "null",
            "null",
            "null"};
        testGetObject(testArray);
      }
    }


    /* Test java.lang.Integer 3X with SQLXML X0 */ 
    public void Var030()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.lang.Integer",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ), CAST(NULL AS XML) from sysibm.sysdummy1", 
              "Data type mismatch",
              "null",};
           testGetObject(testArray);
        }
    }



    /* Test java.lang.Integer 3X with primitive numbers X1 */ 
    public void Var031()
    {
        String[] testArray = {
            "java.lang.Integer",
            "select cast(1 as SMALLINT), cast(2 as INTEGER), cast(3 as BIGINT), cast(null as SMALLINT), cast(null as INTEGER), cast(null as BIGINT)  from sysibm.sysdummy1",
            "1",
            "2",
            "3", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }

    /* Test java.lang.Integer 3X with primitive doubles X2 */ 
    public void Var032()
    {
        String[] testArray = {
            "java.lang.Integer",
            "select cast(1.0 as REAL), cast(2.0 as FLOAT), cast(3.0 as DOUBLE), cast(null as REAL), cast(null as FLOAT), cast(null as DOUBLE) from sysibm.sysdummy1",
            "1",
            "2",
            "3", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test java.lang.Integer 3X with decimal X3 */ 
    public void Var033()
    {
        String[] testArray = {
            "java.lang.Integer",
            "select cast(1.0 as DECIMAL(10,1)), cast(2.0 as NUMERIC(10,2)), cast(3.0 as DECIMAL(10,4)), cast(null as DECIMAL(10,1)), cast(null as NUMERIC(10,2)), cast(null as DECIMAL(10,4)) from sysibm.sysdummy1",
            "1",
            "2",
            "3", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test java.lang.Integer 3X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var034()
    {
        String[] testArray = {
            "java.lang.Integer",
            "select cast('1' as CHAR(10)), cast('2' as GRAPHIC(10) CCSID 1200), cast(null as GRAPHIC(10)), cast('1' as VARCHAR(10)), cast('2' as VARGRAPHIC(10) CCSID 1200), cast(null as VARGRAPHIC(10)) from sysibm.sysdummy1",
            "1",
            "2",
            "null",
            "1",
            "2",
            "null"};
        testGetObject(testArray); 
    }
    


    /* Test java.lang.Integer 3X with BINARY, VARBINARY  X5 */ 
    public void Var035()
    {
        String[] testArray = {
            "java.lang.Integer",
            "select cast(X'01' AS BINARY(1)), cast (X'02' AS VARBINARY(20)),cast(null AS BINARY(1)), cast (null AS VARBINARY(20)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null"};
        testGetObject(testArray); 
    }

    /* Test java.lang.Integer 3X with date, time, timestamp  X6 */ 
    public void Var036()
    {
        String[] testArray = {
            "java.lang.Integer",
            "select cast('1:02:03' as TIME), cast ('2011-09-11' as DATE), cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObject(testArray); 
    }
    
    /* Test java.lang.Integer 3X with clob, dbclob, blob  X7 */ 
    public void Var037()
    {
        String[] testArray = {
            "java.lang.Integer",
            "select cast('1' as CLOB), cast ('2' as DBCLOB(1M) CCSID 1200), cast(X'03' AS BLOB) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObject(testArray); 
    }
    

    /* Test java.lang.Integer 3X with datalink  X8 */ 
    public void Var038()
    {
        String[] testArray = {
            "java.lang.Integer",
            "select DLVALUE('file://tmp/x') from sysibm.sysdummy1",
            "Data type mismatch"};
        testGetObject(testArray); 
    }

    /* Test java.lang.Integer 3X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var039()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.lang.Integer",
            "select CAST(CAST(X'01' AS CHAR(2) FOR BIT DATA) AS ROWID), CAST('1' as NCHAR(10)), CAST('1' AS NVARCHAR(100)), CAST('1' AS NCLOB(100K)), CAST(null AS ROWID), CAST(null as NCHAR(10)), CAST(null AS NVARCHAR(100)), CAST(null AS NCLOB(100K)) from sysibm.sysdummy1",
            "Data type mismatch",
            "1",
            "1",
            "Data type mismatch",
            "null",
            "null",
            "null",
            "null"};
        testGetObject(testArray);
      }
    }

    /* Test java.lang.Short 4X with SQLXML X0 */ 
    public void Var040()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.lang.Short",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ), CAST(NULL AS XML) from sysibm.sysdummy1", 
              "Data type mismatch",
              "null",};
           testGetObject(testArray);
        }
    }



    /* Test java.lang.Short 4X with primitive numbers X1 */ 
    public void Var041()
    {
        String[] testArray = {
            "java.lang.Short",
            "select cast(1 as SMALLINT), cast(2 as INTEGER), cast(3 as BIGINT), cast(null as SMALLINT), cast(null as INTEGER), cast(null as BIGINT)  from sysibm.sysdummy1",
            "1",
            "2",
            "3", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }

    /* Test java.lang.Short 4X with primitive doubles X2 */ 
    public void Var042()
    {
        String[] testArray = {
            "java.lang.Short",
            "select cast(1.0 as REAL), cast(2.0 as FLOAT), cast(3.0 as DOUBLE), cast(null as REAL), cast(null as FLOAT), cast(null as DOUBLE) from sysibm.sysdummy1",
            "1",
            "2",
            "3", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test java.lang.Short 4X with decimal X3 */ 
    public void Var043()
    {
        String[] testArray = {
            "java.lang.Short",
            "select cast(1.0 as DECIMAL(10,1)), cast(2.0 as NUMERIC(10,2)), cast(3.0 as DECIMAL(10,4)), cast(null as DECIMAL(10,1)), cast(null as NUMERIC(10,2)), cast(null as DECIMAL(10,4)) from sysibm.sysdummy1",
            "1",
            "2",
            "3", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test java.lang.Short 4X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var044()
    {
        String[] testArray = {
            "java.lang.Short",
            "select cast('1' as CHAR(10)), cast('2' as GRAPHIC(10) CCSID 1200), cast(null as GRAPHIC(10)), cast('1' as VARCHAR(10)), cast('2' as VARGRAPHIC(10) CCSID 1200), cast(null as VARGRAPHIC(10)) from sysibm.sysdummy1",
            "1",
            "2",
            "null",
            "1",
            "2",
            "null"};
        testGetObject(testArray); 
    }
    


    /* Test java.lang.Short 4X with BINARY, VARBINARY  X5 */ 
    public void Var045()
    {
        String[] testArray = {
            "java.lang.Short",
            "select cast(X'01' AS BINARY(1)), cast (X'02' AS VARBINARY(20)),cast(null AS BINARY(1)), cast (null AS VARBINARY(20)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null"};
        testGetObject(testArray); 
    }

    /* Test java.lang.Short 4X with date, time, timestamp  X6 */ 
    public void Var046()
    {
        String[] testArray = {
            "java.lang.Short",
            "select cast('1:02:03' as TIME), cast ('2011-09-11' as DATE), cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObject(testArray); 
    }
    
    /* Test java.lang.Short 4X with clob, dbclob, blob  X7 */ 
    public void Var047()
    {
        String[] testArray = {
            "java.lang.Short",
            "select cast('1' as CLOB), cast ('2' as DBCLOB(1M) CCSID 1200), cast(X'03' AS BLOB) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObject(testArray); 
    }
    

    /* Test java.lang.Short 4X with datalink  X8 */ 
    public void Var048()
    {
        String[] testArray = {
            "java.lang.Short",
            "select DLVALUE('file://tmp/x') from sysibm.sysdummy1",
            "Data type mismatch"};
        testGetObject(testArray); 
    }

    /* Test java.lang.Short 4X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var049()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.lang.Short",
            "select CAST(CAST(X'01' AS CHAR(2) FOR BIT DATA) AS ROWID), CAST('1' as NCHAR(10)), CAST('1' AS NVARCHAR(100)), CAST('1' AS NCLOB(100K)), CAST(null AS ROWID), CAST(null as NCHAR(10)), CAST(null AS NVARCHAR(100)), CAST(null AS NCLOB(100K)) from sysibm.sysdummy1",
            "Data type mismatch",
            "1",
            "1",
            "Data type mismatch",
            "null",
            "null",
            "null",
            "null"};
        testGetObject(testArray);
      }
    }


    /* Test java.lang.Long 5X with SQLXML X0 */ 
    public void Var050()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.lang.Long",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ), CAST(NULL AS XML) from sysibm.sysdummy1", 
              "Data type mismatch",
              "null",};
           testGetObject(testArray);
        }
    }



    /* Test java.lang.Long 5X with primitive numbers X1 */ 
    public void Var051()
    {
        String[] testArray = {
            "java.lang.Long",
            "select cast(1 as SMALLINT), cast(2 as INTEGER), cast(3 as BIGINT), cast(null as SMALLINT), cast(null as INTEGER), cast(null as BIGINT)  from sysibm.sysdummy1",
            "1",
            "2",
            "3", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }

    /* Test java.lang.Long 5X with primitive doubles X2 */ 
    public void Var052()
    {
        String[] testArray = {
            "java.lang.Long",
            "select cast(1.0 as REAL), cast(2.0 as FLOAT), cast(3.0 as DOUBLE), cast(null as REAL), cast(null as FLOAT), cast(null as DOUBLE) from sysibm.sysdummy1",
            "1",
            "2",
            "3", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test java.lang.Long 5X with decimal X3 */ 
    public void Var053()
    {
        String[] testArray = {
            "java.lang.Long",
            "select cast(1.0 as DECIMAL(10,1)), cast(2.0 as NUMERIC(10,2)), cast(3.0 as DECIMAL(10,4)), cast(null as DECIMAL(10,1)), cast(null as NUMERIC(10,2)), cast(null as DECIMAL(10,4)) from sysibm.sysdummy1",
            "1",
            "2",
            "3", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test java.lang.Long 5X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var054()
    {
        String[] testArray = {
            "java.lang.Long",
            "select cast('1' as CHAR(10)), cast('2' as GRAPHIC(10) CCSID 1200), cast(null as GRAPHIC(10)), cast('1' as VARCHAR(10)), cast('2' as VARGRAPHIC(10) CCSID 1200), cast(null as VARGRAPHIC(10)) from sysibm.sysdummy1",
            "1",
            "2",
            "null",
            "1",
            "2",
            "null"};
        testGetObject(testArray); 
    }
    


    /* Test java.lang.Long 5X with BINARY, VARBINARY  X5 */ 
    public void Var055()
    {
        String[] testArray = {
            "java.lang.Long",
            "select cast(X'01' AS BINARY(1)), cast (X'02' AS VARBINARY(20)),cast(null AS BINARY(1)), cast (null AS VARBINARY(20)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null"};
        testGetObject(testArray); 
    }

    /* Test java.lang.Long 5X with date, time, timestamp  X6 */ 
    public void Var056()
    {
        String[] testArray = {
            "java.lang.Long",
            "select cast('1:02:03' as TIME), cast ('2011-09-11' as DATE), cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObject(testArray); 
    }
    
    /* Test java.lang.Long 5X with clob, dbclob, blob  X7 */ 
    public void Var057()
    {
        String[] testArray = {
            "java.lang.Long",
            "select cast('1' as CLOB), cast ('2' as DBCLOB(1M) CCSID 1200), cast(X'03' AS BLOB) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObject(testArray); 
    }
    

    /* Test java.lang.Long 5X with datalink  X8 */ 
    public void Var058()
    {
        String[] testArray = {
            "java.lang.Long",
            "select DLVALUE('file://tmp/x') from sysibm.sysdummy1",
            "Data type mismatch"};
        testGetObject(testArray); 
    }

    /* Test java.lang.Long 5X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var059()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.lang.Long",
            "select CAST(CAST(X'01' AS CHAR(2) FOR BIT DATA) AS ROWID), CAST('1' as NCHAR(10)), CAST('1' AS NVARCHAR(100)), CAST('1' AS NCLOB(100K)), CAST(null AS ROWID), CAST(null as NCHAR(10)), CAST(null AS NVARCHAR(100)), CAST(null AS NCLOB(100K)) from sysibm.sysdummy1",
            "Data type mismatch",
            "1",
            "1",
            "Data type mismatch",
            "null",
            "null",
            "null",
            "null"};
        testGetObject(testArray);
      }
    }



    /* Test java.lang.Float 6X with SQLXML X0 */ 
    public void Var060()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.lang.Float",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ), CAST(NULL AS XML) from sysibm.sysdummy1", 
              "Data type mismatch",
              "null",};
           testGetObject(testArray);
        }
    }



    /* Test java.lang.Float 6X with primitive numbers X1 */ 
    public void Var061()
    {
        String[] testArray = {
            "java.lang.Float",
            "select cast(1 as SMALLINT), cast(2 as INTEGER), cast(3 as BIGINT), cast(null as SMALLINT), cast(null as INTEGER), cast(null as BIGINT)  from sysibm.sysdummy1",
            "1.0",
            "2.0",
            "3.0", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }

    /* Test java.lang.Float 6X with primitive doubles X2 */ 
    public void Var062()
    {
        String[] testArray = {
            "java.lang.Float",
            "select cast(1.0 as REAL), cast(2.0 as FLOAT), cast(3.0 as DOUBLE), cast(null as REAL), cast(null as FLOAT), cast(null as DOUBLE) from sysibm.sysdummy1",
            "1.0",
            "2.0",
            "3.0", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test java.lang.Float 6X with decimal X3 */ 
    public void Var063()
    {
        String[] testArray = {
            "java.lang.Float",
            "select cast(1.0 as DECIMAL(10,1)), cast(2.0 as NUMERIC(10,2)), cast(3.0 as DECIMAL(10,4)), cast(null as DECIMAL(10,1)), cast(null as NUMERIC(10,2)), cast(null as DECIMAL(10,4)) from sysibm.sysdummy1",
            "1.0",
            "2.0",
            "3.0", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test java.lang.Float 6X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var064()
    {
        String[] testArray = {
            "java.lang.Float",
            "select cast('1' as CHAR(10)), cast('2' as GRAPHIC(10) CCSID 1200), cast(null as GRAPHIC(10)), cast('1' as VARCHAR(10)), cast('2' as VARGRAPHIC(10) CCSID 1200), cast(null as VARGRAPHIC(10)) from sysibm.sysdummy1",
            "1.0",
            "2.0",
            "null",
            "1.0",
            "2.0",
            "null"};
        testGetObject(testArray); 
    }
    


    /* Test java.lang.Float 6X with BINARY, VARBINARY  X5 */ 
    public void Var065()
    {
        String[] testArray = {
            "java.lang.Float",
            "select cast(X'01' AS BINARY(1)), cast (X'02' AS VARBINARY(20)),cast(null AS BINARY(1)), cast (null AS VARBINARY(20)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null"};
        testGetObject(testArray); 
    }

    /* Test java.lang.Float 6X with date, time, timestamp  X6 */ 
    public void Var066()
    {
        String[] testArray = {
            "java.lang.Float",
            "select cast('1:02:03' as TIME), cast ('2011-09-11' as DATE), cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObject(testArray); 
    }
    
    /* Test java.lang.Float 6X with clob, dbclob, blob  X7 */ 
    public void Var067()
    {
        String[] testArray = {
            "java.lang.Float",
            "select cast('1' as CLOB), cast ('2' as DBCLOB(1M) CCSID 1200), cast(X'03' AS BLOB) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObject(testArray); 
    }
    

    /* Test java.lang.Float 6X with datalink  X8 */ 
    public void Var068()
    {
        String[] testArray = {
            "java.lang.Float",
            "select DLVALUE('file://tmp/x') from sysibm.sysdummy1",
            "Data type mismatch"};
        testGetObject(testArray); 
    }

    /* Test java.lang.Float 6X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var069()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.lang.Float",
            "select CAST(CAST(X'01' AS CHAR(2) FOR BIT DATA) AS ROWID), CAST('1' as NCHAR(10)), CAST('1' AS NVARCHAR(100)), CAST('1' AS NCLOB(100K)), CAST(null AS ROWID), CAST(null as NCHAR(10)), CAST(null AS NVARCHAR(100)), CAST(null AS NCLOB(100K)) from sysibm.sysdummy1",
            "Data type mismatch",
            "1.0",
            "1.0",
            "Data type mismatch",
            "null",
            "null",
            "null",
            "null"};
        testGetObject(testArray);
      }
    }



    /* Test java.lang.Double 7X with SQLXML X0 */ 
    public void Var070()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.lang.Double",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ), CAST(NULL AS XML) from sysibm.sysdummy1", 
              "Data type mismatch",
              "null",};
           testGetObject(testArray);
        }
    }



    /* Test java.lang.Double 7X with primitive numbers X1 */ 
    public void Var071()
    {
        String[] testArray = {
            "java.lang.Double",
            "select cast(1 as SMALLINT), cast(2 as INTEGER), cast(3 as BIGINT), cast(null as SMALLINT), cast(null as INTEGER), cast(null as BIGINT)  from sysibm.sysdummy1",
            "1.0",
            "2.0",
            "3.0", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }

    /* Test java.lang.Double 7X with primitive doubles X2 */ 
    public void Var072()
    {
        String[] testArray = {
            "java.lang.Double",
            "select cast(1.0 as REAL), cast(2.0 as FLOAT), cast(3.0 as DOUBLE), cast(null as REAL), cast(null as FLOAT), cast(null as DOUBLE) from sysibm.sysdummy1",
            "1.0",
            "2.0",
            "3.0", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test java.lang.Double 7X with decimal X3 */ 
    public void Var073()
    {
        String[] testArray = {
            "java.lang.Double",
            "select cast(1.0 as DECIMAL(10,1)), cast(2.0 as NUMERIC(10,2)), cast(3.0 as DECIMAL(10,4)), cast(null as DECIMAL(10,1)), cast(null as NUMERIC(10,2)), cast(null as DECIMAL(10,4)) from sysibm.sysdummy1",
            "1.0",
            "2.0",
            "3.0", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test java.lang.Double 7X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var074()
    {
        String[] testArray = {
            "java.lang.Double",
            "select cast('1' as CHAR(10)), cast('2' as GRAPHIC(10) CCSID 1200), cast(null as GRAPHIC(10)), cast('1' as VARCHAR(10)), cast('2' as VARGRAPHIC(10) CCSID 1200), cast(null as VARGRAPHIC(10)) from sysibm.sysdummy1",
            "1.0",
            "2.0",
            "null",
            "1.0",
            "2.0",
            "null"};
        testGetObject(testArray); 
    }
    


    /* Test java.lang.Double 7X with BINARY, VARBINARY  X5 */ 
    public void Var075()
    {
        String[] testArray = {
            "java.lang.Double",
            "select cast(X'01' AS BINARY(1)), cast (X'02' AS VARBINARY(20)),cast(null AS BINARY(1)), cast (null AS VARBINARY(20)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null"};
        testGetObject(testArray); 
    }

    /* Test java.lang.Double 7X with date, time, timestamp  X6 */ 
    public void Var076()
    {
        String[] testArray = {
            "java.lang.Double",
            "select cast('1:02:03' as TIME), cast ('2011-09-11' as DATE), cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObject(testArray); 
    }
    
    /* Test java.lang.Double 7X with clob, dbclob, blob  X7 */ 
    public void Var077()
    {
        String[] testArray = {
            "java.lang.Double",
            "select cast('1' as CLOB), cast ('2' as DBCLOB(1M) CCSID 1200), cast(X'03' AS BLOB) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObject(testArray); 
    }
    

    /* Test java.lang.Double 7X with datalink  X8 */ 
    public void Var078()
    {
        String[] testArray = {
            "java.lang.Double",
            "select DLVALUE('file://tmp/x') from sysibm.sysdummy1",
            "Data type mismatch"};
        testGetObject(testArray); 
    }

    /* Test java.lang.Double 7X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var079()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.lang.Double",
            "select CAST(CAST(X'01' AS CHAR(2) FOR BIT DATA) AS ROWID), CAST('1' as NCHAR(10)), CAST('1' AS NVARCHAR(100)), CAST('1' AS NCLOB(100K)), CAST(null AS ROWID), CAST(null as NCHAR(10)), CAST(null AS NVARCHAR(100)), CAST(null AS NCLOB(100K)) from sysibm.sysdummy1",
            "Data type mismatch",
            "1.0",
            "1.0",
            "Data type mismatch",
            "null",
            "null",
            "null",
            "null"};
        testGetObject(testArray);
      }
    }



    /* Test java.math.BigDecimal 8X with SQLXML X0 */ 
    public void Var080()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.math.BigDecimal",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ), CAST(NULL AS XML) from sysibm.sysdummy1", 
              "Data type mismatch",
              "null",};
           testGetObject(testArray);
        }
    }



    /* Test java.math.BigDecimal 8X with primitive numbers X1 */ 
    public void Var081()
    {
        String[] testArray = {
            "java.math.BigDecimal",
            "select cast(1 as SMALLINT), cast(2 as INTEGER), cast(3 as BIGINT), cast(null as SMALLINT), cast(null as INTEGER), cast(null as BIGINT)  from sysibm.sysdummy1",
            "1",
            "2",
            "3", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }

    /* Test java.math.BigDecimal 8X with primitive doubles X2 */ 
    public void Var082()
    {
        String[] testArray = {
            "java.math.BigDecimal",
            "select cast(1.0 as REAL), cast(2.0 as FLOAT), cast(3.0 as DOUBLE), cast(null as REAL), cast(null as FLOAT), cast(null as DOUBLE) from sysibm.sysdummy1",
            "1.0",
            "2.0",
            "3.0", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test java.math.BigDecimal 8X with decimal X3 */ 
    public void Var083()
    {
        String[] testArray = {
            "java.math.BigDecimal",
            "select cast(1.0 as DECIMAL(10,1)), cast(2.0 as NUMERIC(10,2)), cast(3.0 as DECIMAL(10,4)), cast(null as DECIMAL(10,1)), cast(null as NUMERIC(10,2)), cast(null as DECIMAL(10,4)) from sysibm.sysdummy1",
            "1.0",
            "2.00",
            "3.0000", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test java.math.BigDecimal 8X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var084()
    {
        String[] testArray = {
            "java.math.BigDecimal",
            "select cast('1' as CHAR(10)), cast('2' as GRAPHIC(10) CCSID 1200), cast(null as GRAPHIC(10)), cast('1' as VARCHAR(10)), cast('2' as VARGRAPHIC(10) CCSID 1200), cast(null as VARGRAPHIC(10)) from sysibm.sysdummy1",
            "1",
            "2",
            "null",
            "1",
            "2",
            "null"};
        testGetObject(testArray); 
    }
    


    /* Test java.math.BigDecimal 8X with BINARY, VARBINARY  X5 */ 
    public void Var085()
    {
        String[] testArray = {
            "java.math.BigDecimal",
            "select cast(X'01' AS BINARY(1)), cast (X'02' AS VARBINARY(20)),cast(null AS BINARY(1)), cast (null AS VARBINARY(20)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null"};
        testGetObject(testArray); 
    }

    /* Test java.math.BigDecimal 8X with date, time, timestamp  X6 */ 
    public void Var086()
    {
        String[] testArray = {
            "java.math.BigDecimal",
            "select cast('1:02:03' as TIME), cast ('2011-09-11' as DATE), cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObject(testArray); 
    }
    
    /* Test java.math.BigDecimal 8X with clob, dbclob, blob  X7 */ 
    public void Var087()
    {
        String[] testArray = {
            "java.math.BigDecimal",
            "select cast('1' as CLOB), cast ('2' as DBCLOB(1M) CCSID 1200), cast(X'03' AS BLOB) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObject(testArray); 
    }
    

    /* Test java.math.BigDecimal 8X with datalink  X8 */ 
    public void Var088()
    {
        String[] testArray = {
            "java.math.BigDecimal",
            "select DLVALUE('file://tmp/x') from sysibm.sysdummy1",
            "Data type mismatch"};
        testGetObject(testArray); 
    }

    /* Test java.math.BigDecimal 8X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var089()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.math.BigDecimal",
            "select CAST(CAST(X'01' AS CHAR(2) FOR BIT DATA) AS ROWID), CAST('1' as NCHAR(10)), CAST('1' AS NVARCHAR(100)), CAST('1' AS NCLOB(100K)), CAST(null AS ROWID), CAST(null as NCHAR(10)), CAST(null AS NVARCHAR(100)), CAST(null AS NCLOB(100K)) from sysibm.sysdummy1",
            "Data type mismatch",
            "1",
            "1",
            "Data type mismatch",
            "null",
            "null",
            "null",
            "null"};
        testGetObject(testArray);
      }
    }




    /* Test java.lang.Boolean 9X with SQLXML X0 */ 
    public void Var090()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.lang.Boolean",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ), CAST(NULL AS XML) from sysibm.sysdummy1", 
              "Data type mismatch",
              "null",};
           testGetObject(testArray);
        }
    }



    /* Test java.lang.Boolean 9X with primitive numbers X1 */ 
    public void Var091()
    {
        String[] testArray = {
            "java.lang.Boolean",
            "select cast(1 as SMALLINT), cast(2 as INTEGER), cast(3 as BIGINT), cast(0 as SMALLINT), cast(0 as INTEGER), cast(0 as BIGINT), cast(null as SMALLINT), cast(null as INTEGER), cast(null as BIGINT)  from sysibm.sysdummy1",
            "true",
            "true",
            "true",
            "false",
            "false",
            "false",
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }

    /* Test java.lang.Boolean 9X with primitive doubles X2 */ 
    public void Var092()
    {
        String[] testArray = {
            "java.lang.Boolean",
            "select cast(1.0 as REAL), cast(2.0 as FLOAT), cast(3.0 as DOUBLE), cast(0.0 as REAL), cast(0.0 as FLOAT), cast(0.0 as DOUBLE), cast(null as REAL), cast(null as FLOAT), cast(null as DOUBLE) from sysibm.sysdummy1",
            "true",
            "true",
            "true", 
            "false",
            "false",
            "false",
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test java.lang.Boolean 9X with decimal X3 */ 
    public void Var093()
    {
        String[] testArray = {
            "java.lang.Boolean",
            "select cast(1.0 as DECIMAL(10,1)), cast(2.0 as NUMERIC(10,2)), cast(3.0 as DECIMAL(10,4)), cast(0.0 as DECIMAL(10,1)), cast(0.0 as NUMERIC(10,2)), cast(0.0 as DECIMAL(10,4)), cast(null as DECIMAL(10,1)), cast(null as NUMERIC(10,2)), cast(null as DECIMAL(10,4)) from sysibm.sysdummy1",
            "true",
            "true",
            "true", 
            "false",
            "false",
            "false",
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test java.lang.Boolean 9X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var094()
    {
        String[] testArray = {
            "java.lang.Boolean",
            "select cast('1' as CHAR(10)), cast('0' as CHAR(10)), cast('2' as GRAPHIC(10) CCSID 1200), cast('0' as GRAPHIC(10) CCSID 1200), cast(null as GRAPHIC(10)), cast('1' as VARCHAR(10)), cast('2' as VARGRAPHIC(10) CCSID 1200), cast('0' as VARCHAR(10)), cast('0' as VARGRAPHIC(10) CCSID 1200), cast(null as VARGRAPHIC(10)) from sysibm.sysdummy1",
            "true",
            "false",
            "true",
            "false",
            "null",
            "true",
            "true",
            "false",
            "false",
            "null"};
        testGetObject(testArray); 
    }
    


    /* Test java.lang.Boolean 9X with BINARY, VARBINARY  X5 */ 
    public void Var095()
    {
        String[] testArray = {
            "java.lang.Boolean",
            "select cast(X'01' AS BINARY(1)), cast (X'02' AS VARBINARY(20)),cast(null AS BINARY(1)), cast (null AS VARBINARY(20)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null"};
        testGetObject(testArray); 
    }

    /* Test java.lang.Boolean 9X with date, time, timestamp  X6 */ 
    public void Var096()
    {
        String[] testArray = {
            "java.lang.Boolean",
            "select cast('1:02:03' as TIME), cast ('2011-09-11' as DATE), cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObject(testArray); 
    }
    
    /* Test java.lang.Boolean 9X with clob, dbclob, blob  X7 */ 
    public void Var097()
    {
        String[] testArray = {
            "java.lang.Boolean",
            "select cast('1' as CLOB), cast ('2' as DBCLOB(1M) CCSID 1200), cast(X'03' AS BLOB) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObject(testArray); 
    }
    

    /* Test java.lang.Boolean 9X with datalink  X8 */ 
    public void Var098()
    {
        String[] testArray = {
            "java.lang.Boolean",
            "select DLVALUE('file://tmp/x') from sysibm.sysdummy1",
            "Data type mismatch"};
        testGetObject(testArray); 
    }

    /* Test java.lang.Boolean 9X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var099()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.lang.Boolean",
            "select CAST(CAST(X'01' AS CHAR(2) FOR BIT DATA) AS ROWID), CAST('1' as NCHAR(10)), CAST('1' AS NVARCHAR(100)), CAST('0' as NCHAR(10)), CAST('0' AS NVARCHAR(100)), CAST('1' AS NCLOB(100K)), CAST(null AS ROWID), CAST(null as NCHAR(10)), CAST(null AS NVARCHAR(100)), CAST(null AS NCLOB(100K)) from sysibm.sysdummy1",
            "Data type mismatch",
            "true",
            "true",
            "false",
            "false",
            "Data type mismatch",
            "null",
            "null",
            "null",
            "null"};
        testGetObject(testArray);
      }
    }




    /* Test [B 10X with SQLXML X0 */ 
    public void Var100()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "[B",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ), CAST(NULL AS XML) from sysibm.sysdummy1", 
              "BYTEARRAY=4C6FA7949340A58599A28996957E7FF14BF07F4085958396848995877E7FC9C2D4F0F3F77F6F6E4C886E88859393964C61886E",
              "null",};
           testGetObject(testArray);
        }
    }



    /* Test [B 10X with primitive numbers X1 */ 
    public void Var101()
    {
        String[] testArray = {
            "[B",
            "select cast(1 as SMALLINT), cast(2 as INTEGER), cast(3 as BIGINT), cast(null as SMALLINT), cast(null as INTEGER), cast(null as BIGINT)  from sysibm.sysdummy1",
            "BYTEARRAY=0001",
            "BYTEARRAY=00000002",
            "BYTEARRAY=0000000000000003",
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }

    /* Test [B 10X with primitive doubles X2 */ 
    public void Var102()
    {
        String[] testArray = {
            "[B",
            "select cast(1.0 as REAL), cast(2.0 as FLOAT), cast(3.0 as DOUBLE), cast(null as REAL), cast(null as FLOAT), cast(null as DOUBLE) from sysibm.sysdummy1",
            "BYTEARRAY=3F800000",
            "BYTEARRAY=4000000000000000", 
            "BYTEARRAY=4008000000000000",
            "null",
            "null", 
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test [B 10X with decimal X3 */ 
    public void Var103()
    {
        String[] testArray = {
            "[B",
            "select cast(1.0 as DECIMAL(10,1)), cast(2.0 as NUMERIC(10,2)), cast(3.0 as DECIMAL(10,4)), cast(null as DECIMAL(10,1)), cast(null as NUMERIC(10,2)), cast(null as DECIMAL(10,4)) from sysibm.sysdummy1",
            "BYTEARRAY=00000000010F",
            "BYTEARRAY=F0F0F0F0F0F0F0F2F0F0",
            "BYTEARRAY=00000030000F", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test [B 10X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var104()
    {
        String[] testArray = {
            "[B",
            "select cast('1' as CHAR(10)), cast('2' as GRAPHIC(10) CCSID 1200), cast(null as GRAPHIC(10)), cast('1' as VARCHAR(10)), cast('2' as VARGRAPHIC(10) CCSID 1200), cast(null as VARGRAPHIC(10)) from sysibm.sysdummy1",
            "BYTEARRAY=F1404040404040404040",
            "BYTEARRAY=0032002000200020002000200020002000200020",
            "null",
            "BYTEARRAY=F1",
            "BYTEARRAY=0032",
            "null"};

	    
        testGetObject(testArray); 
    }
    


    /* Test [B 10X with BINARY, VARBINARY  X5 */ 
    public void Var105()
    {
        String[] testArray = {
            "[B",
            "select cast(X'01' AS BINARY(1)), cast (X'02' AS VARBINARY(20)),cast(null AS BINARY(1)), cast (null AS VARBINARY(20)) from sysibm.sysdummy1",
            "BYTEARRAY=01",
            "BYTEARRAY=02",
            "null",
            "null"};
        testGetObject(testArray); 
    }

    /* Test [B 10X with date, time, timestamp  X6 */ 
    public void Var106()
    {
        String[] testArray = {
            "[B",
            "select cast('1:02:03' as TIME), cast ('2011-09-11' as DATE), cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) from sysibm.sysdummy1",
            "BYTEARRAY=F0F17AF0F27AF0F3",
            "BYTEARRAY=F2F0F1F160F0F960F1F1",
            "BYTEARRAY=F2F0F1F160F0F960F1F160F0F14BF0F24BF0F34BF1F2F3F4F5F6"};
        testGetObject(testArray); 
    }
    
    /* Test [B 10X with clob, dbclob, blob  X7 */ 
    public void Var107()
    {
        String[] testArray = {
            "[B",
            "select cast('1' as CLOB), cast ('2' as DBCLOB(1M) CCSID 1200), cast(X'03' AS BLOB) from sysibm.sysdummy1",
            "BYTEARRAY=F1",
            "BYTEARRAY=0032",
            "BYTEARRAY=03"};

	    // The values for CLOB and DBCLOB do not make sense for toolbox, so skip them
	    String[] testArrayTB  = {
            "[B",
            "select cast(X'03' AS BLOB) from sysibm.sysdummy1",
            "BYTEARRAY=03"
	    }; 


	    if (isToolboxDriver()) {
		testArray = testArrayTB; 
	    } 
        testGetObject(testArray); 
    }
    

    /* Test [B 10X with datalink  X8 */ 
    public void Var108()
    {
        String[] testArray = {
            "[B",
            "select DLVALUE('file://tmp/x') from sysibm.sysdummy1",
            "BYTEARRAY=C6C9D3C57A6161E3D4D761A7"};


        testGetObject(testArray); 
    }

    /* Test [B 10X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var109()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "[B",
            "select CAST(CAST(X'01' AS CHAR(2) FOR BIT DATA) AS ROWID), CAST('1' as NCHAR(10)), CAST('1' AS NVARCHAR(100)), CAST('1' AS NCLOB(100K)), CAST(null AS ROWID), CAST(null as NCHAR(10)), CAST(null AS NVARCHAR(100)), CAST(null AS NCLOB(100K)) from sysibm.sysdummy1",
            "BYTEARRAY=0140",
            "BYTEARRAY=0031002000200020002000200020002000200020",
            "BYTEARRAY=0031", 
            "BYTEARRAY=0031",
            "null",
            "null",
            "null",
            "null"};


    testGetObject(testArray);
      }
    }




    /* Test java.sql.Date 11X with SQLXML X0 */ 
    public void Var110()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.sql.Date",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ), CAST(NULL AS XML) from sysibm.sysdummy1", 
              "Data type mismatch",
              "null",};
           testGetObject(testArray);
        }
    }



    /* Test java.sql.Date 11X with primitive numbers X1 */ 
    public void Var111()
    {
        String[] testArray = {
            "java.sql.Date",
            "select cast(1 as SMALLINT), cast(2 as INTEGER), cast(3 as BIGINT), cast(null as SMALLINT), cast(null as INTEGER), cast(null as BIGINT)  from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }

    /* Test java.sql.Date 11X with primitive doubles X2 */ 
    public void Var112()
    {
        String[] testArray = {
            "java.sql.Date",
            "select cast(1.0 as REAL), cast(2.0 as FLOAT), cast(3.0 as DOUBLE), cast(null as REAL), cast(null as FLOAT), cast(null as DOUBLE) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test java.sql.Date 11X with decimal X3 */ 
    public void Var113()
    {
        String[] testArray = {
            "java.sql.Date",
            "select cast(1.0 as DECIMAL(10,1)), cast(2.0 as NUMERIC(10,2)), cast(3.0 as DECIMAL(10,4)), cast(null as DECIMAL(10,1)), cast(null as NUMERIC(10,2)), cast(null as DECIMAL(10,4)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test java.sql.Date 11X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var114()
    {
        String[] testArray = {
            "java.sql.Date",
            "select cast('2011-09-01' as CHAR(10)), cast('2011-09-02' as GRAPHIC(10) CCSID 1200), cast(null as GRAPHIC(10)), cast('2011-09-03' as VARCHAR(10)), cast('2011-09-04' as VARGRAPHIC(10) CCSID 1200), cast(null as VARGRAPHIC(10)) from sysibm.sysdummy1",
            "2011-09-01",
            "2011-09-02",
            "null",
            "2011-09-03",
            "2011-09-04",
            "null"};
        testGetObject(testArray); 
    }
    


    /* Test java.sql.Date 11X with BINARY, VARBINARY  X5 */ 
    public void Var115()
    {
        String[] testArray = {
            "java.sql.Date",
            "select cast(X'01' AS BINARY(1)), cast (X'02' AS VARBINARY(20)),cast(null AS BINARY(1)), cast (null AS VARBINARY(20)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null"};
        testGetObject(testArray); 
    }

    /* Test java.sql.Date 11X with date, time, timestamp  X6 */ 
    public void Var116()
    {
        String[] testArray = {
            "java.sql.Date",
            "select cast('1:02:03' as TIME), cast ('2011-09-11' as DATE), cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) from sysibm.sysdummy1",
            "Data type mismatch",
            "2011-09-11",
            "2011-09-11"};
        testGetObject(testArray); 
    }
    
    /* Test java.sql.Date 11X with clob, dbclob, blob  X7 */ 
    public void Var117()
    {
        String[] testArray = {
            "java.sql.Date",
            "select cast('2011-09-11' as CLOB), cast ('2011-09-12' as DBCLOB(1M) CCSID 1200), cast(X'03' AS BLOB) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObject(testArray); 
    }
    

    /* Test java.sql.Date 11X with datalink  X8 */ 
    public void Var118()
    {
        String[] testArray = {
            "java.sql.Date",
            "select DLVALUE('file://tmp/x') from sysibm.sysdummy1",
            "Data type mismatch"};
        testGetObject(testArray); 
    }

    /* Test java.sql.Date 11X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var119()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.sql.Date",
            "select CAST(CAST(X'01' AS CHAR(2) FOR BIT DATA) AS ROWID), CAST('2011-09-11' as NCHAR(10)), CAST('2011-09-12' AS NVARCHAR(100)), CAST('2001-09-12' AS NCLOB(100K)), CAST(null AS ROWID), CAST(null as NCHAR(10)), CAST(null AS NVARCHAR(100)), CAST(null AS NCLOB(100K)) from sysibm.sysdummy1",
            "Data type mismatch",
            "2011-09-11",
            "2011-09-12",
            "Data type mismatch",
            "null",
            "null",
            "null",
            "null"};
        testGetObject(testArray);
      }
    }

    



    /* Test java.sql.Time 12X with SQLXML X0 */ 
    public void Var120()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.sql.Time",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ), CAST(NULL AS XML) from sysibm.sysdummy1", 
              "Data type mismatch",
              "null",};
           testGetObject(testArray);
        }
    }



    /* Test java.sql.Time 12X with primitive numbers X1 */ 
    public void Var121()
    {
        String[] testArray = {
            "java.sql.Time",
            "select cast(1 as SMALLINT), cast(2 as INTEGER), cast(3 as BIGINT), cast(null as SMALLINT), cast(null as INTEGER), cast(null as BIGINT)  from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }

    /* Test java.sql.Time 12X with primitive doubles X2 */ 
    public void Var122()
    {
        String[] testArray = {
            "java.sql.Time",
            "select cast(1.0 as REAL), cast(2.0 as FLOAT), cast(3.0 as DOUBLE), cast(null as REAL), cast(null as FLOAT), cast(null as DOUBLE) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test java.sql.Time 12X with decimal X3 */ 
    public void Var123()
    {
        String[] testArray = {
            "java.sql.Time",
            "select cast(1.0 as DECIMAL(10,1)), cast(2.0 as NUMERIC(10,2)), cast(3.0 as DECIMAL(10,4)), cast(null as DECIMAL(10,1)), cast(null as NUMERIC(10,2)), cast(null as DECIMAL(10,4)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test java.sql.Time 12X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var124()
    {
        String[] testArray = {
            "java.sql.Time",
            "select cast('11:22:01' as CHAR(10)), cast('11:22:02' as GRAPHIC(10) CCSID 1200), cast(null as GRAPHIC(10)), cast('11:22:03' as VARCHAR(10)), cast('11:22:04' as VARGRAPHIC(10) CCSID 1200), cast(null as VARGRAPHIC(10)) from sysibm.sysdummy1",
            "11:22:01",
            "11:22:02",
            "null",
            "11:22:03",
            "11:22:04",
            "null"};
        testGetObject(testArray); 
    }
    


    /* Test java.sql.Time 12X with BINARY, VARBINARY  X5 */ 
    public void Var125()
    {
        String[] testArray = {
            "java.sql.Time",
            "select cast(X'01' AS BINARY(1)), cast (X'02' AS VARBINARY(20)),cast(null AS BINARY(1)), cast (null AS VARBINARY(20)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null"};
        testGetObject(testArray); 
    }

    /* Test java.sql.Time 12X with date, time, timestamp  X6 */ 
    public void Var126()
    {
        String[] testArray = {
            "java.sql.Time",
            "select cast('1:02:03' as TIME), cast ('2011-09-11' as DATE), cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) from sysibm.sysdummy1",
            "01:02:03",
            "Data type mismatch",
            "01:02:03"};
        testGetObject(testArray); 
    }
    
    /* Test java.sql.Time 12X with clob, dbclob, blob  X7 */ 
    public void Var127()
    {
        String[] testArray = {
            "java.sql.Time",
            "select cast('11:22:11' as CLOB), cast ('11:22:12' as DBCLOB(1M) CCSID 1200), cast(X'03' AS BLOB) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObject(testArray); 
    }
    

    /* Test java.sql.Time 12X with datalink  X8 */ 
    public void Var128()
    {
        String[] testArray = {
            "java.sql.Time",
            "select DLVALUE('file://tmp/x') from sysibm.sysdummy1",
            "Data type mismatch"};
        testGetObject(testArray); 
    }

    /* Test java.sql.Time 12X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var129()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.sql.Time",
            "select CAST(CAST(X'01' AS CHAR(2) FOR BIT DATA) AS ROWID), CAST('11:22:11' as NCHAR(10)), CAST('11:22:12' AS NVARCHAR(100)), CAST('1' AS NCLOB(100K)), CAST(null AS ROWID), CAST(null as NCHAR(10)), CAST(null AS NVARCHAR(100)), CAST(null AS NCLOB(100K)) from sysibm.sysdummy1",
            "Data type mismatch",
            "11:22:11",
            "11:22:12",
            "Data type mismatch",
            "null",
            "null",
            "null",
            "null"};
        testGetObject(testArray);
      }
    }




    /* Test java.sql.Timestamp 13X with SQLXML X0 */ 
    public void Var130()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.sql.Timestamp",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ), CAST(NULL AS XML) from sysibm.sysdummy1", 
              "Data type mismatch",
              "null",};
           testGetObject(testArray);
        }
    }



    /* Test java.sql.Timestamp 13X with primitive numbers X1 */ 
    public void Var131()
    {
        String[] testArray = {
            "java.sql.Timestamp",
            "select cast(1 as SMALLINT), cast(2 as INTEGER), cast(3 as BIGINT), cast(null as SMALLINT), cast(null as INTEGER), cast(null as BIGINT)  from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }

    /* Test java.sql.Timestamp 13X with primitive doubles X2 */ 
    public void Var132()
    {
        String[] testArray = {
            "java.sql.Timestamp",
            "select cast(1.0 as REAL), cast(2.0 as FLOAT), cast(3.0 as DOUBLE), cast(null as REAL), cast(null as FLOAT), cast(null as DOUBLE) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test java.sql.Timestamp 13X with decimal X3 */ 
    public void Var133()
    {
        String[] testArray = {
            "java.sql.Timestamp",
            "select cast(1.0 as DECIMAL(10,1)), cast(2.0 as NUMERIC(10,2)), cast(3.0 as DECIMAL(10,4)), cast(null as DECIMAL(10,1)), cast(null as NUMERIC(10,2)), cast(null as DECIMAL(10,4)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test java.sql.Timestamp 13X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var134()
    {
        String[] testArray = {
            "java.sql.Timestamp",
            "select cast('2011-09-01 11:22:01.012345' as CHAR(26)), cast('2011-09-01 11:22:02.222222' as GRAPHIC(26) CCSID 1200), cast(null as GRAPHIC(26)), cast('2011-09-01 11:22:03.333333' as VARCHAR(26)), cast('2011-09-01 11:22:04.444444' as VARGRAPHIC(26) CCSID 1200), cast(null as VARGRAPHIC(10)) from sysibm.sysdummy1",
            "2011-09-01 11:22:01.012345",
            "2011-09-01 11:22:02.222222",
            "null",
            "2011-09-01 11:22:03.333333",
            "2011-09-01 11:22:04.444444",
            "null"};
        testGetObject(testArray); 
    }
    


    /* Test java.sql.Timestamp 13X with BINARY, VARBINARY  X5 */ 
    public void Var135()
    {
        String[] testArray = {
            "java.sql.Timestamp",
            "select cast(X'01' AS BINARY(1)), cast (X'02' AS VARBINARY(20)),cast(null AS BINARY(1)), cast (null AS VARBINARY(20)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null"};
        testGetObject(testArray); 
    }

    /* Test java.sql.Timestamp 13X with date, Timestamp, Timestampstamp  X6 */ 
    public void Var136()
    {
        String[] testArray = {
            "java.sql.Timestamp",
            "select cast('1:02:03' as TIME), cast ('2011-09-11' as DATE), cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) from sysibm.sysdummy1",
            "1970-01-01 01:02:03.0",
            "2011-09-11 00:00:00.0",
            "2011-09-11 01:02:03.123456"};
        testGetObject(testArray); 
    }
    
    /* Test java.sql.Timestamp 13X with clob, dbclob, blob  X7 */ 
    public void Var137()
    {
        String[] testArray = {
            "java.sql.Timestamp",
            "select cast('2011-09-01 11:22:11' as CLOB), cast ('2011-09-01 11:22:12' as DBCLOB(1M) CCSID 1200), cast(X'03' AS BLOB) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObject(testArray); 
    }
    

    /* Test java.sql.Timestamp 13X with datalink  X8 */ 
    public void Var138()
    {
        String[] testArray = {
            "java.sql.Timestamp",
            "select DLVALUE('file://tmp/x') from sysibm.sysdummy1",
            "Data type mismatch"};
        testGetObject(testArray); 
    }

    /* Test java.sql.Timestamp 13X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var139()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.sql.Timestamp",
            "select CAST(CAST(X'01' AS CHAR(2) FOR BIT DATA) AS ROWID), CAST('2011-09-01 11:22:11.111111' as NCHAR(26)), CAST('2011-09-01 11:22:12.222222' AS NVARCHAR(100)), CAST('1' AS NCLOB(100K)), CAST(null AS ROWID), CAST(null as NCHAR(26)), CAST(null AS NVARCHAR(100)), CAST(null AS NCLOB(100K)) from sysibm.sysdummy1",
            "Data type mismatch",
            "2011-09-01 11:22:11.111111",
            "2011-09-01 11:22:12.222222",
            "Data type mismatch",
            "null",
            "null",
            "null",
            "null"};
        testGetObject(testArray);
      }
    }


    /* Test java.io.InputStream 14X with SQLXML X0 */ 
    public void Var140()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.io.InputStream",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ), CAST(null as XML) from sysibm.sysdummy1", 
              "INPUTSTREAM=4C6FA7949340A58599A28996957E7FF14BF07F4085958396848995877E7FC9C2D4F0F3F77F6F6E4C886E88859393964C61886E",
              "null"};

           String[] testArrayNative = {
              "java.io.InputStream",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ), CAST(null as XML) from sysibm.sysdummy1", 
              "INPUTSTREAM=3C683E68656C6C6F3C2F683E", 
              "null"};


	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

           testGetObject(testArray);
        }
    }



    /* Test java.io.InputStream 14X with primitive numbers X1 */ 
    public void Var141()
    {

        String[] testArray = {
            "java.io.InputStream",
            "select cast(1 as SMALLINT), cast(2 as INTEGER), cast(3 as BIGINT), cast(null as SMALLINT), cast(null as INTEGER), cast(null as BIGINT) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null",
            "null"};



        testGetObject(testArray); 
    }

    /* Test java.io.InputStream 14X with primitive doubles X2 */ 
    public void Var142()
    {
        String[] testArray = {
            "java.io.InputStream",
            "select cast(1.0 as REAL), cast(2.0 as FLOAT), cast(3.0 as DOUBLE), cast(null as REAL), cast(null as FLOAT), cast(null as DOUBLE)  from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null",
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test java.io.InputStream 14X with decimal X3 */ 
    public void Var143()
    {
        String[] testArray = {
            "java.io.InputStream",
            "select cast(1.0 as DECIMAL(10,1)), cast(2.0 as NUMERIC(10,2)), cast(3.0 as DECIMAL(10,4)), cast(null as DECIMAL(10,1)), cast(null as NUMERIC(10,2)), cast(null as DECIMAL(10,4)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null",
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test java.io.InputStream 14X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var144()
    {
        String[] testArray = {
            "java.io.InputStream",
            "select cast('A2a4a6a8a0' as CHAR(10)), cast('A2a4a6a8a0' as GRAPHIC(10) CCSID 1200), " +
            "cast(null as GRAPHIC(10)), " +
            "cast('AABBCC' as VARCHAR(10)), cast('ABCD' as VARGRAPHIC(10) CCSID 1200), cast(null as VARGRAPHIC(10)) from sysibm.sysdummy1",
            "INPUTSTREAM=A2A4A6A8A0",
            "INPUTSTREAM=A2A4A6A8A0",
            "null",
            "INPUTSTREAM=AABBCC",
            "INPUTSTREAM=ABCD",
            "null"};


        String[] testArrayNative = {
            "java.io.InputStream",
            "select cast('A2a4a6a8a0' as CHAR(10)), cast('A2a4a6a8a0' as GRAPHIC(10) CCSID 1200), " +
            "cast(null as GRAPHIC(10)), " +
            "cast('AABBCC' as VARCHAR(10)), cast('ABCD' as VARGRAPHIC(10) CCSID 1200), cast(null as VARGRAPHIC(10)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "Data type mismatch",
            "Data type mismatch",
            "null"};



	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

        testGetObject(testArray); 
    }
    


    /* Test java.io.InputStream 14X with BINARY, VARBINARY  X5 */ 
    public void Var145()
    {
        String[] testArray = {
            "java.io.InputStream",
            "select cast(X'313233' AS BINARY(10)), cast (X'313233' AS VARBINARY(20)),cast(null AS BINARY(10)), cast (null AS VARBINARY(20)) from sysibm.sysdummy1",
            "INPUTSTREAM=31323300000000000000",
            "INPUTSTREAM=313233", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }

    /* Test java.io.InputStream 14X with date, time, timestamp  X6 */ 
    public void Var146()
    {
        String[] testArray = {
            "java.io.InputStream",
            "select cast('1:02:03' as TIME), cast ('2011-09-11' as DATE), cast('2011-09-11-01.02.03.123456' AS TIMESTAMP), cast(null as TIME), cast (null as DATE), cast(null AS TIMESTAMP) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test java.io.InputStream 14X with clob, dbclob, blob  X7 */ 
    public void Var147()
    {
        String[] testArray = {
            "java.io.InputStream",
            "select cast('ABCD' as CLOB), cast ('ABCD' as DBCLOB(1M) CCSID 1200), cast(X'313233' AS BLOB), cast(null as CLOB), cast (null as DBCLOB(1M) CCSID 1200), cast(null AS BLOB) from sysibm.sysdummy1",
            "INPUTSTREAM=ABCD",
            "INPUTSTREAM=ABCD",
            "INPUTSTREAM=313233", 
            "null", 
            "null", 
            "null"};

        String[] testArrayNative = {
            "java.io.InputStream",
            "select cast('ABCD' as CLOB), cast ('ABCD' as DBCLOB(1M) CCSID 1200), cast(X'313233' AS BLOB), cast(null as CLOB), cast (null as DBCLOB(1M) CCSID 1200), cast(null AS BLOB) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "INPUTSTREAM=313233", 
            "null", 
            "null", 
            "null"};


	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

        testGetObject(testArray); 
    }
    

    /* Test java.io.InputStream 14X with datalink  X8 */ 
    public void Var148()
    {
        String[] testArray = {
            "java.io.InputStream",
            "select DLVALUE('file://tmp/x'), CAST(NULL AS DATALINK) from sysibm.sysdummy1",
            "Data type mismatch", 
            "null"};
        testGetObject(testArray); 
    }

    /* Test java.io.InputStream 14X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var149()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.io.InputStream",
            "select CAST(CAST(X'1122' AS CHAR(2) FOR BIT DATA) AS ROWID), " +
            "CAST('1122334455' as NCHAR(10)), CAST('ABCE' AS NVARCHAR(100)), CAST('ABCF' AS NCLOB(100K)), " +
            "CAST(null AS ROWID), CAST(null as NCHAR(10)), CAST(null AS NVARCHAR(100)), CAST(null AS NCLOB(100K)) from sysibm.sysdummy1",
            "INPUTSTREAM=1122",
            "INPUTSTREAM=1122334455",
            "INPUTSTREAM=ABCE",
            "INPUTSTREAM=ABCF", 
            "null", 
            "null", 
            "null",
            "null"};


    String[] testArrayNative = {
            "java.io.InputStream",
            "select CAST(CAST(X'1122' AS CHAR(2) FOR BIT DATA) AS ROWID), " +
            "CAST('1122334455' as NCHAR(10)), CAST('ABCE' AS NVARCHAR(100)), CAST('ABCF' AS NCLOB(100K)), " +
            "CAST(null AS ROWID), CAST(null as NCHAR(10)), CAST(null AS NVARCHAR(100)), CAST(null AS NCLOB(100K)) from sysibm.sysdummy1",
            "INPUTSTREAM=1122",
            "Data type mismatch", 
            "Data type mismatch", 
            "Data type mismatch", 
            "null", 
            "null", 
            "null",
            "null"};

	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

        testGetObject(testArray);
      }
    }


    /* Test java.io.Reader 15X with SQLXML X0 */ 
    public void Var150()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.io.Reader",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ), CAST(null as XML) from sysibm.sysdummy1", 
              "READER=<h>hello</h>",
              "null"};
           testGetObject(testArray);
        }
    }



    /* Test java.io.Reader 15X with primitive numbers X1 */ 
    public void Var151()
    {

        String[] testArray = {
            "java.io.Reader",
            "select cast(1 as SMALLINT), cast(2 as INTEGER), cast(3 as BIGINT), cast(null as SMALLINT), cast(null as INTEGER), cast(null as BIGINT) from sysibm.sysdummy1",
            "READER=1", 
            "READER=2", 
            "READER=3", 
            "null",
            "null",
            "null"};
        testGetObject(testArray); 
    }

    /* Test java.io.Reader 15X with primitive doubles X2 */ 
    public void Var152()
    {
        String[] testArray = {
            "java.io.Reader",
            "select cast(1.0 as REAL), cast(2.0 as FLOAT), cast(3.0 as DOUBLE), cast(null as REAL), cast(null as FLOAT), cast(null as DOUBLE)  from sysibm.sysdummy1",
            "READER=1.0", 
            "READER=2.0", 
            "READER=3.0", 
            "null",
            "null",
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test java.io.Reader 15X with decimal X3 */ 
    public void Var153()
    {
        String[] testArray = {
            "java.io.Reader",
            "select cast(1.0 as DECIMAL(10,1)), cast(2.0 as NUMERIC(10,2)), cast(3.0 as DECIMAL(10,4)), cast(null as DECIMAL(10,1)), cast(null as NUMERIC(10,2)), cast(null as DECIMAL(10,4)) from sysibm.sysdummy1",
            "READER=1.0",
            "READER=2.00",
            "READER=3.0000",
            "null",
            "null",
            "null"};


        testGetObject(testArray); 
    }
    
    /* Test java.io.Reader 15X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var154()
    {
        String[] testArray = {
            "java.io.Reader",
            "select cast('ABC' as CHAR(10)), cast('ABC' as GRAPHIC(10) CCSID 1200), cast(null as GRAPHIC(10)), cast('ABC' as VARCHAR(10)), cast('ABC' as VARGRAPHIC(10) CCSID 1200), cast(null as VARGRAPHIC(10)) from sysibm.sysdummy1",
            "READER=ABC       ",
            "READER=ABC       ",
            "null",
            "READER=ABC",
            "READER=ABC",
            "null"};
        testGetObject(testArray); 
    }
    


    /* Test java.io.Reader 15X with BINARY, VARBINARY  X5 */ 
    public void Var155()
    {
        String[] testArray = {
            "java.io.Reader",
            "select cast(X'313233' AS BINARY(10)), cast (X'313233' AS VARBINARY(20)),cast(null AS BINARY(10)), cast (null AS VARBINARY(20)) from sysibm.sysdummy1",
            "READER=31323300000000000000",
            "READER=313233", 
            "null", 
            "null"};


        String[] testArrayNative = {
            "java.io.Reader",
            "select cast(X'313233' AS BINARY(10)), cast (X'313233' AS VARBINARY(20)),cast(null AS BINARY(10)), cast (null AS VARBINARY(20)) from sysibm.sysdummy1",
            "READER=123\u0000\u0000\u0000\u0000\u0000\u0000\u0000",
            "READER=123", 
            "null", 
            "null"};

	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

        testGetObject(testArray); 
    }

    /* Test java.io.Reader 15X with date, time, timestamp  X6 */ 
    public void Var156()
    {
        String[] testArray = {
            "java.io.Reader",
            "select cast('1:02:03' as TIME), cast ('2011-09-11' as DATE), cast('2011-09-11-01.02.03.123456' AS TIMESTAMP), cast(null as TIME), cast (null as DATE), cast(null AS TIMESTAMP) from sysibm.sysdummy1",
            "READER=01:02:03",
            "READER=2011-09-11",
            "READER=2011-09-11 01:02:03.123456",
            "null", 
            "null", 
            "null"};
        String[] testArrayNative = {
            "java.io.Reader",
            "select cast('1:02:03' as TIME), cast ('2011-09-11' as DATE), cast('2011-09-11-01.02.03.123456' AS TIMESTAMP), cast(null as TIME), cast (null as DATE), cast(null AS TIMESTAMP) from sysibm.sysdummy1",
            "READER=01:02:03",
            "READER=2011-09-11",
            "READER=2011-09-11-01.02.03.123456",
            "null", 
            "null", 
            "null"};

	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

        testGetObject(testArray); 
    }
    
    /* Test java.io.Reader 15X with clob, dbclob, blob  X7 */ 
    public void Var157()
    {
        String[] testArray = {
            "java.io.Reader",
            "select cast('ABC' as CLOB), cast ('ABC' as DBCLOB(1M) CCSID 1200), cast(X'313233' AS BLOB), cast(null as CLOB), cast (null as DBCLOB(1M) CCSID 1200), cast(null AS BLOB) from sysibm.sysdummy1",
            "READER=ABC",
            "READER=ABC",
            "READER=313233", 
            "null", 
            "null", 
            "null"};

        String[] testArrayNative = {
            "java.io.Reader",
            "select cast('ABC' as CLOB), cast ('ABC' as DBCLOB(1M) CCSID 1200), cast(X'313233' AS BLOB), cast(null as CLOB), cast (null as DBCLOB(1M) CCSID 1200), cast(null AS BLOB) from sysibm.sysdummy1",
            "READER=ABC",
            "READER=ABC",
            "READER=123",
            "null", 
            "null", 
            "null"};

	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

        testGetObject(testArray); 
    }
    

    /* Test java.io.Reader 15X with datalink  X8 */ 
    public void Var158()
    {
        String[] testArray = {
            "java.io.Reader",
            "select DLVALUE('file://tmp/x'), CAST(NULL AS DATALINK) from sysibm.sysdummy1",
            "READER=FILE://TMP/x", 
            "null"};


        testGetObject(testArray); 
    }

    /* Test java.io.Reader 15X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var159()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.io.Reader",
            "select CAST(CAST(X'1122' AS CHAR(2) FOR BIT DATA) AS ROWID), CAST('ABC' as NCHAR(10)), CAST('ABC' AS NVARCHAR(100)), CAST('ABC' AS NCLOB(100K)), CAST(null AS ROWID), CAST(null as NCHAR(10)), CAST(null AS NVARCHAR(100)), CAST(null AS NCLOB(100K)) from sysibm.sysdummy1",
            "READER=1122",
            "READER=ABC       ",
            "READER=ABC",
            "READER=ABC", 
            "null", 
            "null", 
            "null",
            "null"};
        testGetObject(testArray);
      }
    }


    /* Test java.sql.Clob 16X with SQLXML X0 */ 
    public void Var160()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.sql.Clob",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ), CAST(null as XML) from sysibm.sysdummy1", 
              "CLOB=<h>hello</h>",
              "null"};
           testGetObject(testArray);
        }
    }



    /* Test java.sql.Clob 16X with primitive numbers X1 */ 
    public void Var161()
    {
        String[] testArray = {
            "java.sql.Clob",
            "select cast(1 as SMALLINT), cast(2 as INTEGER), cast(3 as BIGINT), cast(null as SMALLINT), cast(null as INTEGER), cast(null as BIGINT) from sysibm.sysdummy1",
            "CLOB=1",
            "CLOB=2",
            "CLOB=3", 
            "null",
            "null",
            "null"};
        testGetObject(testArray); 
    }

    /* Test java.sql.Clob 16X with primitive doubles X2 */ 
    public void Var162()
    {
        String[] testArray = {
            "java.sql.Clob",
            "select cast(1.0 as REAL), cast(2.0 as FLOAT), cast(3.0 as DOUBLE), cast(null as REAL), cast(null as FLOAT), cast(null as DOUBLE)  from sysibm.sysdummy1",
            "CLOB=1.0", 
            "CLOB=2.0", 
            "CLOB=3.0", 
            "null",
            "null",
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test java.sql.Clob 16X with decimal X3 */ 
    public void Var163()
    {
        String[] testArray = {
            "java.sql.Clob",
            "select cast(1.0 as DECIMAL(10,1)), cast(2.0 as NUMERIC(10,2)), cast(3.0 as DECIMAL(10,4)), cast(null as DECIMAL(10,1)), cast(null as NUMERIC(10,2)), cast(null as DECIMAL(10,4)) from sysibm.sysdummy1",
            "CLOB=1.0",
            "CLOB=2.00",
            "CLOB=3.0000",
            "null",
            "null",
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test java.sql.Clob 16X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var164()
    {
        String[] testArray = {
            "java.sql.Clob",
            "select cast('ABC' as CHAR(10)), cast('ABC' as GRAPHIC(10) CCSID 1200), cast(null as GRAPHIC(10)), cast('ABC' as VARCHAR(10)), cast('ABC' as VARGRAPHIC(10) CCSID 1200), cast(null as VARGRAPHIC(10)) from sysibm.sysdummy1",
            "CLOB=ABC       ",
            "CLOB=ABC       ",
            "null",
            "CLOB=ABC",
            "CLOB=ABC",
            "null"};
        testGetObject(testArray); 
    }
    


    /* Test java.sql.Clob 16X with BINARY, VARBINARY  X5 */ 
    public void Var165()
    {
        String[] testArray = {
            "java.sql.Clob",
            "select cast(X'313233' AS BINARY(10)), cast (X'313233' AS VARBINARY(20)),cast(null AS BINARY(10)), cast (null AS VARBINARY(20)) from sysibm.sysdummy1",
            "CLOB=31323300000000000000",
            "CLOB=313233", 
            "null", 
            "null"};

        String[] testArrayNative = {
            "java.sql.Clob",
            "select cast(X'313233' AS BINARY(10)), cast (X'313233' AS VARBINARY(20)),cast(null AS BINARY(10)), cast (null AS VARBINARY(20)) from sysibm.sysdummy1",
            "CLOB=123\u0000\u0000\u0000\u0000\u0000\u0000\u0000",
            "CLOB=123", 
            "null", 
            "null"};

	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
	      testArray = testArrayNative; 
	    } 

        testGetObject(testArray); 
    }

    /* Test java.sql.Clob 16X with date, time, timestamp  X6 */ 
    public void Var166()
    {
        String[] testArray = {
            "java.sql.Clob",
            "select cast('1:02:03' as TIME), cast ('2011-09-11' as DATE), cast('2011-09-11-01.02.03.123456' AS TIMESTAMP), cast(null as TIME), cast (null as DATE), cast(null AS TIMESTAMP) from sysibm.sysdummy1",
            "CLOB=01:02:03",
            "CLOB=2011-09-11",
            "CLOB=2011-09-11 01:02:03.123456",
            "null", 
            "null", 
            "null"};


        String[] testArrayNative = {
            "java.sql.Clob",
            "select cast('1:02:03' as TIME), cast ('2011-09-11' as DATE), cast('2011-09-11-01.02.03.123456' AS TIMESTAMP), cast(null as TIME), cast (null as DATE), cast(null AS TIMESTAMP) from sysibm.sysdummy1",
            "CLOB=01:02:03",
            "CLOB=2011-09-11",
            "CLOB=2011-09-11-01.02.03.123456",
            "null", 
            "null", 
            "null"};


	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

        testGetObject(testArray); 
    }
    
    /* Test java.sql.Clob 16X with clob, dbclob, blob  X7 */ 
    public void Var167()
    {
        String[] testArray = {
            "java.sql.Clob",
            "select cast('ABC' as CLOB), cast ('ABC' as DBCLOB(1M) CCSID 1200), cast(X'313233' AS BLOB), cast(null as CLOB), cast (null as DBCLOB(1M) CCSID 1200), cast(null AS BLOB) from sysibm.sysdummy1",
            "CLOB=ABC",
            "CLOB=ABC",
            "CLOB=313233", 
            "null", 
            "null", 
            "null"};

        String[] testArrayNative = {
            "java.sql.Clob",
            "select cast('ABC' as CLOB), cast ('ABC' as DBCLOB(1M) CCSID 1200), cast(X'505C5D' AS BLOB), cast(null as CLOB), cast (null as DBCLOB(1M) CCSID 1200), cast(null AS BLOB) from sysibm.sysdummy1",
            "CLOB=ABC",
            "CLOB=ABC",
            "CLOB=&*)", 
            "null", 
            "null", 
            "null"};

	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

        testGetObject(testArray); 
    }
    

    /* Test java.sql.Clob 16X with datalink  X8 */ 
    public void Var168()
    {
        String[] testArray = {
            "java.sql.Clob",
            "select DLVALUE('file://tmp/x'), CAST(NULL AS DATALINK) from sysibm.sysdummy1",
            "CLOB=FILE://TMP/x", 
            "null"};
        testGetObject(testArray); 
    }

    /* Test java.sql.Clob 16X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var169()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.sql.Clob",
            "select CAST(CAST(X'1122' AS CHAR(2) FOR BIT DATA) AS ROWID), CAST('ABC' as NCHAR(10)), CAST('ABC' AS NVARCHAR(100)), CAST('ABC' AS NCLOB(100K)), CAST(null AS ROWID), CAST(null as NCHAR(10)), CAST(null AS NVARCHAR(100)), CAST(null AS NCLOB(100K)) from sysibm.sysdummy1",
            "CLOB=1122",
            "CLOB=ABC       ",
            "CLOB=ABC",
            "CLOB=ABC", 
            "null", 
            "null", 
            "null",
            "null"};
        testGetObject(testArray);
      }
    }

    /* Test java.sql.Blob 17X with SQLXML X0 */ 
    public void Var170()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.sql.Blob",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ), CAST(NULL AS XML) from sysibm.sysdummy1", 
              "BLOB=4C6FA7949340A58599A28996957E7FF14BF07F4085958396848995877E7FC9C2D4F0F3F77F6F6E4C886E88859393964C61886E",
              "null",};

           String[] testArrayNative = {
              "java.sql.Blob",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ), CAST(NULL AS XML) from sysibm.sysdummy1", 
              "BLOB=3C683E68656C6C6F3C2F683E",
              "null",};

	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

           testGetObject(testArray);
        }
    }


    /* Test java.sql.Blob 17X with primitive numbers X1 */ 
    public void Var171()
    {

        
        String[] testArray = {
            "java.sql.Blob",
            "select cast(1 as SMALLINT), cast(2 as INTEGER), cast(3 as BIGINT), cast(null as SMALLINT), cast(null as INTEGER), cast(null as BIGINT)  from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null", 
            "null", 
            "null"};


        testGetObject(testArray); 
    }

    /* Test java.sql.Blob 17X with primitive doubles X2 */ 
    public void Var172()
    {
        String[] testArray = {
            "java.sql.Blob",
            "select cast(1.0 as REAL), cast(2.0 as FLOAT), cast(3.0 as DOUBLE), cast(null as REAL), cast(null as FLOAT), cast(null as DOUBLE) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null", 
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test java.sql.Blob 17X with decimal X3 */ 
    public void Var173()
    {
        String[] testArray = {
            "java.sql.Blob",
            "select cast(1.0 as DECIMAL(10,1)), cast(2.0 as NUMERIC(10,2)), cast(3.0 as DECIMAL(10,4)), cast(null as DECIMAL(10,1)), cast(null as NUMERIC(10,2)), cast(null as DECIMAL(10,4)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test java.sql.Blob 17X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var174()
    {
        String[] testArray = {
            "java.sql.Blob",
            "select cast('1' as CHAR(10)), cast('2' as GRAPHIC(10) CCSID 1200), cast(null as GRAPHIC(10)), cast('1' as VARCHAR(10)), cast('2' as VARGRAPHIC(10) CCSID 1200), cast(null as VARGRAPHIC(10)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "BLOB=01",
            "BLOB=02",
            "null"};
        
        String[] testArrayNative = {
            "java.sql.Blob",
            "select cast('1' as CHAR(10)), cast('2' as GRAPHIC(10) CCSID 1200), cast(null as GRAPHIC(10)), cast('1' as VARCHAR(10)), cast('2' as VARGRAPHIC(10) CCSID 1200), cast(null as VARGRAPHIC(10)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "Data type mismatch",
            "Data type mismatch",
            "null"};
        
        
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 


        testGetObject(testArray); 
    }
    


    /* Test java.sql.Blob 17X with BINARY, VARBINARY  X5 */ 
    public void Var175()
    {
        String[] testArray = {
            "java.sql.Blob",
            "select cast(X'01' AS BINARY(1)), cast (X'02' AS VARBINARY(20)),cast(null AS BINARY(1)), cast (null AS VARBINARY(20)) from sysibm.sysdummy1",
            "BLOB=01",
            "BLOB=02",
            "null",
            "null"};
        testGetObject(testArray); 
    }

    /* Test java.sql.Blob 17X with date, time, timestamp  X6 */ 
    public void Var176()
    {
        String[] testArray = {
            "java.sql.Blob",
            "select cast('1:02:03' as TIME), cast ('2011-09-11' as DATE), cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObject(testArray); 
    }
    
    /* Test java.sql.Blob 17X with clob, dbclob, blob  X7 */ 
    public void Var177()
    {
        String[] testArray = {
            "java.sql.Blob",
            "select cast('1' as CLOB), cast ('2' as DBCLOB(1M) CCSID 1200), cast(X'03' AS BLOB) from sysibm.sysdummy1",
            "BLOB=01",
            "BLOB=02",
            "BLOB=03"};


        String[] testArrayNative = {
            "java.sql.Blob",
            "select cast('1' as CLOB), cast ('2' as DBCLOB(1M) CCSID 1200), cast(X'03' AS BLOB) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "BLOB=03"};

	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

        testGetObject(testArray); 
    }
    

    /* Test java.sql.Blob 17X with datalink  X8 */ 
    public void Var178()
    {
        String[] testArray = {
            "java.sql.Blob",
            "select DLVALUE('file://tmp/x') from sysibm.sysdummy1",
            "Data type mismatch"};
        testGetObject(testArray); 
    }

    /* Test java.sql.Blob 17X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var179()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.sql.Blob",
            "select CAST(CAST(X'01' AS CHAR(2) FOR BIT DATA) AS ROWID), CAST('1' as NCHAR(10)), CAST('1' AS NVARCHAR(100))," +
            " CAST('1' AS NCLOB(100K)), CAST(null AS ROWID), CAST(null as NCHAR(10)), CAST(null AS NVARCHAR(100)), CAST(null AS NCLOB(100K)) from sysibm.sysdummy1",
            "BLOB=0140",
            "Data type mismatch",
            "BLOB=01", 
            "BLOB=01",
            "null",
            "null",
            "null",
            "null"};

    String[] testArrayNative = {
        "java.sql.Blob",
        "select CAST(CAST(X'01' AS CHAR(2) FOR BIT DATA) AS ROWID), CAST('1' as NCHAR(10)), CAST('1' AS NVARCHAR(100))," +
        " CAST('1' AS NCLOB(100K)), CAST(null AS ROWID), CAST(null as NCHAR(10)), CAST(null AS NVARCHAR(100)), CAST(null AS NCLOB(100K)) from sysibm.sysdummy1",
        "BLOB=0140",
        "Data type mismatch",
        "Data type mismatch", 
        "Data type mismatch",
        "null",
        "null",
        "null",
        "null"};

    
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

        testGetObject(testArray);
      }
    }



    /* Test java.sql.NClob 18X with SQLXML X0 */ 
    public void Var180()
    {
      if (checkJdbc40()) {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.sql.NClob",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ), CAST(null as XML) from sysibm.sysdummy1", 
              "CLOB=<h>hello</h>",
              "null"};


           testGetObject(testArray);
        }
      }
    }



    /* Test java.sql.NClob 18X with primitive numbers X1 */ 
    public void Var181()
    {
        String[] testArray = {
            "java.sql.NClob",
            "select cast(1 as SMALLINT), cast(2 as INTEGER), cast(3 as BIGINT), cast(null as SMALLINT), cast(null as INTEGER), cast(null as BIGINT) from sysibm.sysdummy1",
            "CLOB=1",
            "CLOB=2",
            "CLOB=3", 
            "null",
            "null",
            "null"};
        if (checkJdbc40()) 
        testGetObject(testArray); 
    }

    /* Test java.sql.NClob 18X with primitive doubles X2 */ 
    public void Var182()
    {
        String[] testArray = {
            "java.sql.NClob",
            "select cast(1.0 as REAL), cast(2.0 as FLOAT), cast(3.0 as DOUBLE), cast(null as REAL), cast(null as FLOAT), cast(null as DOUBLE)  from sysibm.sysdummy1",
            "CLOB=1.0", 
            "CLOB=2.0", 
            "CLOB=3.0", 
            "null",
            "null",
            "null"};
        if (checkJdbc40()) 
        testGetObject(testArray); 
    }
    
    /* Test java.sql.NClob 18X with decimal X3 */ 
    public void Var183()
    {
        String[] testArray = {
            "java.sql.NClob",
            "select cast(1.0 as DECIMAL(10,1)), cast(2.0 as NUMERIC(10,2)), cast(3.0 as DECIMAL(10,4)), cast(null as DECIMAL(10,1)), cast(null as NUMERIC(10,2)), cast(null as DECIMAL(10,4)) from sysibm.sysdummy1",
            "CLOB=1.0",
            "CLOB=2.00",
            "CLOB=3.0000",
            "null",
            "null",
            "null"};
        if (checkJdbc40()) 
        testGetObject(testArray); 
    }
    
    /* Test java.sql.NClob 18X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var184()
    {
        String[] testArray = {
            "java.sql.NClob",
            "select cast('ABC' as CHAR(10)), cast('ABC' as GRAPHIC(10) CCSID 1200), cast(null as GRAPHIC(10)), cast('ABC' as VARCHAR(10)), cast('ABC' as VARGRAPHIC(10) CCSID 1200), cast(null as VARGRAPHIC(10)) from sysibm.sysdummy1",
            "CLOB=ABC       ",
            "CLOB=ABC       ",
            "null",
            "CLOB=ABC",
            "CLOB=ABC",
            "null"};
        if (checkJdbc40()) 
        testGetObject(testArray); 
    }
    


    /* Test java.sql.NClob 18X with BINARY, VARBINARY  X5 */ 
    public void Var185()
    {
        String[] testArray = {
            "java.sql.NClob",
            "select cast(X'313233' AS BINARY(10)), cast (X'313233' AS VARBINARY(20)),cast(null AS BINARY(10)), cast (null AS VARBINARY(20)) from sysibm.sysdummy1",
            "CLOB=31323300000000000000",
            "CLOB=313233", 
            "null", 
            "null"};


        String[] testArrayNative = {
            "java.sql.NClob",
            "select cast(X'313233' AS BINARY(10)), cast (X'313233' AS VARBINARY(20)),cast(null AS BINARY(10)), cast (null AS VARBINARY(20)) from sysibm.sysdummy1",
            "CLOB=123\u0000\u0000\u0000\u0000\u0000\u0000\u0000",
            "CLOB=123", 
            "null", 
            "null"};

	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 



        if (checkJdbc40()) 
        testGetObject(testArray); 
    }

    /* Test java.sql.NClob 18X with date, time, timestamp  X6 */ 
    public void Var186()
    {
        String[] testArray = {
            "java.sql.NClob",
            "select cast('1:02:03' as TIME), cast ('2011-09-11' as DATE), cast('2011-09-11-01.02.03.123456' AS TIMESTAMP), cast(null as TIME), cast (null as DATE), cast(null AS TIMESTAMP) from sysibm.sysdummy1",
            "CLOB=01:02:03",
            "CLOB=2011-09-11",
            "CLOB=2011-09-11 01:02:03.123456",
            "null", 
            "null", 
            "null"};

        String[] testArrayNative = {
            "java.sql.NClob",
            "select cast('1:02:03' as TIME), cast ('2011-09-11' as DATE), cast('2011-09-11-01.02.03.123456' AS TIMESTAMP), cast(null as TIME), cast (null as DATE), cast(null AS TIMESTAMP) from sysibm.sysdummy1",
            "CLOB=01:02:03",
            "CLOB=2011-09-11",
            "CLOB=2011-09-11-01.02.03.123456",
            "null", 
            "null", 
            "null"};


	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

        if (checkJdbc40()) 
        testGetObject(testArray); 
    }
    
    /* Test java.sql.NClob 18X with NClob, dbNClob, blob  X7 */ 
    public void Var187()
    {
        String[] testArray = {
            "java.sql.NClob",
            "select cast('ABC' as CLOB), cast ('ABC' as DBClob(1M) CCSID 1200), cast(X'313233' AS BLOB), cast(null as Clob), cast (null as DBClob(1M) CCSID 1200), cast(null AS BLOB) from sysibm.sysdummy1",
            "CLOB=ABC",
            "CLOB=ABC",
            "CLOB=313233", 
            "null", 
            "null", 
            "null"};


        String[] testArrayNative = {
            "java.sql.NClob",
            "select cast('ABC' as CLOB), cast ('ABC' as DBClob(1M) CCSID 1200), cast(X'313233' AS BLOB), cast(null as Clob), cast (null as DBClob(1M) CCSID 1200), cast(null AS BLOB) from sysibm.sysdummy1",
            "CLOB=ABC",
            "CLOB=ABC",
            "CLOB=123", 
            "null", 
            "null", 
            "null"};


	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

        if (checkJdbc40()) 
        testGetObject(testArray); 
    }
    

    /* Test java.sql.NClob 18X with datalink  X8 */ 
    public void Var188()
    {
        String[] testArray = {
            "java.sql.NClob",
            "select DLVALUE('file://tmp/x'), CAST(NULL AS DATALINK) from sysibm.sysdummy1",
            "CLOB=FILE://TMP/x", 
            "null"};
        if (checkJdbc40()) 
	    testGetObject(testArray); 
    }

    /* Test java.sql.NClob 18X with rowid, nchar, nvarchar, NClob X9 */ 
    public void Var189()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.sql.NClob",
            "select CAST(CAST(X'1122' AS CHAR(2) FOR BIT DATA) AS ROWID), CAST('ABC' as NCHAR(10)), CAST('ABC' AS NVARCHAR(100)), CAST('ABC' AS NClob(100K)), CAST(null AS ROWID), CAST(null as NCHAR(10)), CAST(null AS NVARCHAR(100)), CAST(null AS NClob(100K)) from sysibm.sysdummy1",
            "CLOB=1122",
            "CLOB=ABC       ",
            "CLOB=ABC",
            "CLOB=ABC", 
            "null", 
            "null", 
            "null",
            "null"};

    String[] testArrayNative = {
            "java.sql.NClob",
            "select CAST(CAST(X'1122' AS CHAR(2) FOR BIT DATA) AS ROWID), CAST('ABC' as NCHAR(10)), CAST('ABC' AS NVARCHAR(100)), CAST('ABC' AS NClob(100K)), CAST(null AS ROWID), CAST(null as NCHAR(10)), CAST(null AS NVARCHAR(100)), CAST(null AS NClob(100K)) from sysibm.sysdummy1",
            "CLOB=1122",
            "CLOB=ABC       ",
            "CLOB=ABC",
            "CLOB=ABC", 
            "null", 
            "null", 
            "null",
            "null"};

	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

    if (checkJdbc40()) 
        testGetObject(testArray);
      }
    }


    /* Test java.sql.Array 19X with SQLXML X0 */ 
    public void Var190()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.sql.Array",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ), CAST(null as XML) from sysibm.sysdummy1", 
              "Data type mismatch",
              "Data type mismatch",
	   };
           testGetObject(testArray);
        }
    }



    /* Test java.sql.Array 19X with primitive numbers X1 */ 
    public void Var191()
    {
        String[] testArray = {
            "java.sql.Array",
            "select cast(1 as SMALLINT), cast(2 as INTEGER), cast(3 as BIGINT), cast(null as SMALLINT), cast(null as INTEGER), cast(null as BIGINT) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
	};
        testGetObject(testArray); 
    }

    /* Test java.sql.Array 19X with primitive doubles X2 */ 
    public void Var192()
    {
        String[] testArray = {
            "java.sql.Array",
            "select cast(1.0 as REAL), cast(2.0 as FLOAT), cast(3.0 as DOUBLE), cast(null as REAL), cast(null as FLOAT), cast(null as DOUBLE)  from sysibm.sysdummy1",
            "Data type mismatch", 
            "Data type mismatch", 
            "Data type mismatch", 
            "Data type mismatch", 
            "Data type mismatch", 
            "Data type mismatch", 
	};
        testGetObject(testArray); 
    }
    
    /* Test java.sql.Array 19X with decimal X3 */ 
    public void Var193()
    {
        String[] testArray = {
            "java.sql.Array",
            "select cast(1.0 as DECIMAL(10,1)), cast(2.0 as NUMERIC(10,2)), cast(3.0 as DECIMAL(10,4)), cast(null as DECIMAL(10,1)), cast(null as NUMERIC(10,2)), cast(null as DECIMAL(10,4)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
	};
        testGetObject(testArray); 
    }
    
    /* Test java.sql.Array 19X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var194()
    {
        String[] testArray = {
            "java.sql.Array",
            "select cast('ABC' as CHAR(10)), cast('ABC' as GRAPHIC(10) CCSID 1200), cast(null as GRAPHIC(10)), cast('ABC' as VARCHAR(10)), cast('ABC' as VARGRAPHIC(10) CCSID 1200), cast(null as VARGRAPHIC(10)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
	};
        testGetObject(testArray); 
    }
    


    /* Test java.sql.Array 19X with BINARY, VARBINARY  X5 */ 
    public void Var195()
    {
        String[] testArray = {
            "java.sql.Array",
            "select cast(X'313233' AS BINARY(10)), cast (X'313233' AS VARBINARY(20)),cast(null AS BINARY(10)), cast (null AS VARBINARY(20)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch", 
            "Data type mismatch",
            "Data type mismatch", 
	};
        testGetObject(testArray); 
    }

    /* Test java.sql.Array 19X with date, time, timestamp  X6 */ 
    public void Var196()
    {
        String[] testArray = {
            "java.sql.Array",
            "select cast('1:02:03' as TIME), cast ('2011-09-11' as DATE), cast('2011-09-11-01.02.03.123456' AS TIMESTAMP), cast(null as TIME), cast (null as DATE), cast(null AS TIMESTAMP) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
	};
        testGetObject(testArray); 
    }
    
    /* Test java.sql.Array 19X with CLOB, dbclob, blob  X7 */ 
    public void Var197()
    {
        String[] testArray = {
            "java.sql.Array",
            "select cast('ABC' as clob), cast ('ABC' as DBclob(1M) CCSID 1200), cast(X'313233' AS BLOB), " +
            "cast(null as clob), cast (null as DBclob(1M) CCSID 1200), cast(null AS BLOB) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
	    "Data type mismatch",
	    "Data type mismatch",
	    "Data type mismatch", 
	};
	testGetObject(testArray); 
    }
    

    /* Test java.sql.Array 19X with datalink  X8 */ 
    public void Var198()
    {
        String[] testArray = {
            "java.sql.Array",
            "select DLVALUE('file://tmp/x'), CAST(NULL AS DATALINK) from sysibm.sysdummy1",
            "Data type mismatch", 
            "Data type mismatch", 
	};
        testGetObject(testArray); 
    }

    /* Test java.sql.Array 19X with rowid, nchar, nvarchar, nArray X9 */ 
    public void Var199()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.sql.Array",
            "select CAST(CAST(X'1122' AS CHAR(2) FOR BIT DATA) AS ROWID), " +
            "CAST('ABC' as NCHAR(10)), CAST('ABC' AS NVARCHAR(100)), CAST('ABC' AS NCLOB(100K)), " +
            "CAST(null AS ROWID), CAST(null as NCHAR(10)), CAST(null AS NVARCHAR(100)), " +
            "CAST(null AS NCLOB(100K)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
    };
        testGetObject(testArray);
      }
    }

    
    /* Test java.sql.Ref 20X with SQLXML X0 */ 
    public void Var200()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.sql.Ref",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ), CAST(null as XML) from sysibm.sysdummy1", 
              "Data type mismatch",
              "Data type mismatch",
	   };
           testGetObject(testArray);
        }
    }



    /* Test java.sql.Ref 20X with primitive numbers X1 */ 
    public void Var201()
    {
        String[] testArray = {
            "java.sql.Ref",
            "select cast(1 as SMALLINT), cast(2 as INTEGER), cast(3 as BIGINT), cast(null as SMALLINT), cast(null as INTEGER), cast(null as BIGINT) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 

	};
        testGetObject(testArray); 
    }

    /* Test java.sql.Ref 20X with primitive doubles X2 */ 
    public void Var202()
    {
        String[] testArray = {
            "java.sql.Ref",
            "select cast(1.0 as REAL), cast(2.0 as FLOAT), cast(3.0 as DOUBLE), cast(null as REAL), cast(null as FLOAT), cast(null as DOUBLE)  from sysibm.sysdummy1",
            "Data type mismatch", 
            "Data type mismatch", 
            "Data type mismatch", 
            "Data type mismatch", 
            "Data type mismatch", 
            "Data type mismatch", 
	};
        testGetObject(testArray); 
    }
    
    /* Test java.sql.Ref 20X with decimal X3 */ 
    public void Var203()
    {
        String[] testArray = {
            "java.sql.Ref",
            "select cast(1.0 as DECIMAL(10,1)), cast(2.0 as NUMERIC(10,2)), cast(3.0 as DECIMAL(10,4)), cast(null as DECIMAL(10,1)), cast(null as NUMERIC(10,2)), cast(null as DECIMAL(10,4)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
	};
        testGetObject(testArray); 
    }
    
    /* Test java.sql.Ref 20X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var204()
    {
        String[] testArray = {
            "java.sql.Ref",
            "select cast('ABC' as CHAR(10)), cast('ABC' as GRAPHIC(10) CCSID 1200), cast(null as GRAPHIC(10)), cast('ABC' as VARCHAR(10)), cast('ABC' as VARGRAPHIC(10) CCSID 1200), cast(null as VARGRAPHIC(10)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
	};
        testGetObject(testArray); 
    }
    


    /* Test java.sql.Ref 20X with BINARY, VARBINARY  X5 */ 
    public void Var205()
    {
        String[] testArray = {
            "java.sql.Ref",
            "select cast(X'313233' AS BINARY(10)), cast (X'313233' AS VARBINARY(20)),cast(null AS BINARY(10)), cast (null AS VARBINARY(20)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch", 
            "Data type mismatch",
            "Data type mismatch", 
	};
        testGetObject(testArray); 
    }

    /* Test java.sql.Ref 20X with date, time, timestamp  X6 */ 
    public void Var206()
    {
        String[] testArray = {
            "java.sql.Ref",
            "select cast('1:02:03' as TIME), cast ('2011-09-11' as DATE), cast('2011-09-11-01.02.03.123456' AS TIMESTAMP), cast(null as TIME), cast (null as DATE), cast(null AS TIMESTAMP) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",

	};
        testGetObject(testArray); 
    }
    
    /* Test java.sql.Ref 20X with clob, dbclob, blob  X7 */ 
    public void Var207()
    {
        String[] testArray = {
            "java.sql.Ref",
            "select cast('ABC' as CLOB), cast ('ABC' as DBCLOB(1M) CCSID 1200), cast(X'313233' AS BLOB), cast(null as CLOB), cast (null as DBCLOB(1M) CCSID 1200), cast(null AS BLOB) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
	};
        testGetObject(testArray); 
    }
    

    /* Test java.sql.Ref 20X with datalink  X8 */ 
    public void Var208()
    {
        String[] testArray = {
            "java.sql.Ref",
            "select DLVALUE('file://tmp/x'), CAST(NULL AS DATALINK) from sysibm.sysdummy1",
            "Data type mismatch", 
            "Data type mismatch"};
        testGetObject(testArray); 
    }

    /* Test java.sql.Ref 20X with rowid, nchar, nvarchar, nArray X9 */ 
    public void Var209()
    {
      if (checkRelease710()) {  
	  String[] testArray = {
	      "java.sql.Ref",
	      "select CAST(CAST(X'1122' AS CHAR(2) FOR BIT DATA) AS ROWID), " +
	      "CAST('ABC' as NCHAR(10)), CAST('ABC' AS NVARCHAR(100)), CAST('ABC' AS NCLOB(100K))," +
	      " CAST(null AS ROWID), CAST(null as NCHAR(10)), CAST(null AS NVARCHAR(100)), CAST(null AS NCLOB(100K)) from sysibm.sysdummy1",
	      "Data type mismatch",
	      "Data type mismatch",
	      "Data type mismatch",
	      "Data type mismatch",
	      "Data type mismatch",
	      "Data type mismatch",
	      "Data type mismatch",
	      "Data type mismatch",
	  }; 

        testGetObject(testArray);
      }
    }


    /* Test java.net.URL 21X with SQLXML X0 */ 

    public void Var210()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.net.URL",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ), CAST(null as XML) from sysibm.sysdummy1", 
              "Data type mismatch",
              "null"};
           testGetObject(testArray);
        }
    }



    /* Test java.net.URL 21X with primitive numbers X1 */ 
    public void Var211()
    {
        String[] testArray = {
            "java.net.URL",
            "select cast(1 as SMALLINT), cast(2 as INTEGER), cast(3 as BIGINT), cast(null as SMALLINT), cast(null as INTEGER), cast(null as BIGINT) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "null",
            "null",
            "null"};
        testGetObject(testArray); 
    }

    /* Test java.net.URL 21X with primitive doubles X2 */ 
    public void Var212()
    {
        String[] testArray = {
            "java.net.URL",
            "select cast(1.0 as REAL), cast(2.0 as FLOAT), cast(3.0 as DOUBLE), cast(null as REAL), cast(null as FLOAT), cast(null as DOUBLE)  from sysibm.sysdummy1",
            "Data type mismatch", 
            "Data type mismatch", 
            "Data type mismatch", 
            "null",
            "null",
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test java.net.URL 21X with decimal X3 */ 
    public void Var213()
    {
        String[] testArray = {
            "java.net.URL",
            "select cast(1.0 as DECIMAL(10,1)), cast(2.0 as NUMERIC(10,2)), cast(3.0 as DECIMAL(10,4)), cast(null as DECIMAL(10,1)), cast(null as NUMERIC(10,2)), cast(null as DECIMAL(10,4)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null",
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test java.net.URL 21X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var214()
    {
        String[] testArray = {
            "java.net.URL",
            "select cast('http://ibm.com' as CHAR(20)), cast('http://ibm.com' as GRAPHIC(20) CCSID 1200), " +
            "cast(null as GRAPHIC(10)), " +
            "cast('http://ibm.com' as VARCHAR(20)), cast('http://ibm.com' as VARGRAPHIC(20) CCSID 1200), " +
            "cast(null as VARGRAPHIC(10)) from sysibm.sysdummy1",
            "http://ibm.com",
            "http://ibm.com",
            "null",
            "http://ibm.com",
            "http://ibm.com",
            "null"};
        testGetObject(testArray); 
    }
    


    /* Test java.net.URL 21X with BINARY, VARBINARY  X5 */ 
    public void Var215()
    {
        String[] testArray = {
            "java.net.URL",
            "select cast(X'313233' AS BINARY(10)), cast (X'313233' AS VARBINARY(20)),cast(null AS BINARY(10)), cast (null AS VARBINARY(20)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }

    /* Test java.net.URL 21X with date, time, timestamp  X6 */ 
    public void Var216()
    {
        String[] testArray = {
            "java.net.URL",
            "select cast('1:02:03' as TIME), cast ('2011-09-11' as DATE), cast('2011-09-11-01.02.03.123456' AS TIMESTAMP), cast(null as TIME), cast (null as DATE), cast(null AS TIMESTAMP) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test java.net.URL 21X with clob, dbclob, blob  X7 */ 
    public void Var217()
    {
        String[] testArray = {
            "java.net.URL",
            "select cast('http://ibm.com' as CLOB), cast ('http://ibm.com' as DBCLOB(1M) CCSID 1200), cast(X'313233' AS BLOB), cast(null as CLOB), cast (null as DBCLOB(1M) CCSID 1200), cast(null AS BLOB) from sysibm.sysdummy1",
            "http://ibm.com",
            "http://ibm.com",
            "Data type mismatch", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }
    

    /* Test java.net.URL 21X with datalink  X8 */ 
    public void Var218()
    {
        String[] testArray = {
            "java.net.URL",
            "select DLVALUE('file://tmp/x'), CAST(NULL AS DATALINK) from sysibm.sysdummy1",
            "file://TMP/x", 
            "null"};
        testGetObject(testArray); 
    }

    /* Test java.net.URL 21X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var219()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.net.URL",
            "select CAST(CAST(X'1122' AS CHAR(2) FOR BIT DATA) AS ROWID), CAST('ABC' as NCHAR(10)), CAST('ABC' AS NVARCHAR(100)), CAST('ABC' AS NCLOB(100K)), CAST(null AS ROWID), CAST(null as NCHAR(10)), CAST(null AS NVARCHAR(100)), CAST(null AS NCLOB(100K)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null", 
            "null", 
            "null",
            "null"};
        testGetObject(testArray);
      }
    }


    /* Test object 22X with SQLXML X0 */ 
    public void Var220()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.lang.Object",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ), CAST(null as XML) from sysibm.sysdummy1", 
              "SQLXML=<h>hello</h>",
              "null"};


  

           testGetObject(testArray);
        }
    }



    /* Test object 22X with primitive numbers X1 */ 
    public void Var221()
    {
        String[] testArray = {
            "java.lang.Object",
            "select cast(1 as SMALLINT), cast(2 as INTEGER), cast(3 as BIGINT), cast(null as SMALLINT), cast(null as INTEGER), cast(null as BIGINT) from sysibm.sysdummy1",
            "1",
            "2",
            "3", 
            "null",
            "null",
            "null"};
        testGetObject(testArray); 
    }

    /* Test object 22X with primitive doubles X2 */ 
    public void Var222()
    {
        String[] testArray = {
            "java.lang.Object",
            "select cast(1.0 as REAL), cast(2.0 as FLOAT), cast(3.0 as DOUBLE), cast(null as REAL), cast(null as FLOAT), cast(null as DOUBLE)  from sysibm.sysdummy1",
            "1.0",
            "2.0",
            "3.0",
            "null",
            "null",
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test object 22X with decimal X3 */ 
    public void Var223()
    {
        String[] testArray = {
            "java.lang.Object",
            "select cast(1.0 as DECIMAL(10,1)), cast(2.0 as NUMERIC(10,2)), cast(3.0 as DECIMAL(10,4)), cast(null as DECIMAL(10,1)), cast(null as NUMERIC(10,2)), cast(null as DECIMAL(10,4)) from sysibm.sysdummy1",
            "1.0",
            "2.00",
            "3.0000",
            "null",
            "null",
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test object 22X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var224()
    {
        String[] testArray = {
            "java.lang.Object",
            "select cast('ABC' as CHAR(10)), cast('ABC' as GRAPHIC(10) CCSID 1200), cast(null as GRAPHIC(10)), cast('ABC' as VARCHAR(10)), cast('ABC' as VARGRAPHIC(10) CCSID 1200), cast(null as VARGRAPHIC(10)) from sysibm.sysdummy1",
            "ABC       ",
            "ABC       ",
            "null",
            "ABC",
            "ABC",
            "null"};
        testGetObject(testArray); 
    }
    


    /* Test object 22X with BINARY, VARBINARY  X5 */ 
    public void Var225()
    {
        String[] testArray = {
            "java.lang.Object",
            "select cast(X'313233' AS BINARY(10)), cast (X'313233' AS VARBINARY(20)),cast(null AS BINARY(10)), cast (null AS VARBINARY(20)) from sysibm.sysdummy1",
            "BYTEARRAY=31323300000000000000",
            "BYTEARRAY=313233", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }

    /* Test object 22X with date, time, timestamp  X6 */ 
    public void Var226()
    {
        String[] testArray = {
            "java.lang.Object",
            "select cast('1:02:03' as TIME), cast ('2011-09-11' as DATE), cast('2011-09-11-01.02.03.123456' AS TIMESTAMP), cast(null as TIME), cast (null as DATE), cast(null AS TIMESTAMP) from sysibm.sysdummy1",
            "01:02:03",
            "2011-09-11",
            "2011-09-11 01:02:03.123456", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }
    
    /* Test object 22X with clob, dbclob, blob  X7 */ 
    public void Var227()
    {
        String[] testArray = {
            "java.lang.Object",
            "select cast('ABC' as CLOB), cast ('ABC' as DBCLOB(1M) CCSID 1200), cast(X'313233' AS BLOB), cast(null as CLOB), cast (null as DBCLOB(1M) CCSID 1200), cast(null AS BLOB) from sysibm.sysdummy1",
            "CLOB=ABC",
            "CLOB=ABC",
            "BLOB=313233", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
    }
    

    /* Test object 22X with datalink  X8 */ 
    public void Var228()
    {
        String[] testArray = {
            "java.lang.Object",
            "select DLVALUE('file://tmp/x'), CAST(NULL AS DATALINK) from sysibm.sysdummy1",
            "file://TMP/x", 
            "null"};


        testGetObject(testArray); 
    }

    /* Test object 22X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var229()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.lang.Object",
            "select CAST(CAST(X'1122' AS CHAR(2) FOR BIT DATA) AS ROWID), CAST('ABC' as NCHAR(10)), CAST('ABC' AS NVARCHAR(100)), CAST('ABC' AS NCLOB(100K)), CAST(null AS ROWID), CAST(null as NCHAR(10)), CAST(null AS NVARCHAR(100)), CAST(null AS NCLOB(100K)) from sysibm.sysdummy1",
            "ROWID=1122",
            "ABC       ",
            "ABC",
            "CLOB=ABC", 
            "null", 
            "null", 
            "null",
            "null"};
	    
        testGetObject(testArray);
      }
    }




    /* Test java.sql.RowId 23X with SQLXML X0 */ 
    public void Var230()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.sql.RowId",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ), CAST(NULL AS XML) from sysibm.sysdummy1", 
              "Data type mismatch", 
              "null",};
           if (checkJdbc40()) 
             testGetObject(testArray);
        }
    }


    /* Test java.sql.RowId 23X with primitive numbers X1 */ 
    public void Var231()
    {
        String[] testArray = {
            "java.sql.RowId",
            "select cast(1 as SMALLINT), cast(2 as INTEGER), cast(3 as BIGINT), cast(null as SMALLINT), cast(null as INTEGER), cast(null as BIGINT)  from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null", 
            "null", 
            "null"};
        if (checkJdbc40()) 
        testGetObject(testArray); 
    }

    /* Test java.sql.RowId 23X with primitive doubles X2 */ 
    public void Var232()
    {
        String[] testArray = {
            "java.sql.RowId",
            "select cast(1.0 as REAL), cast(2.0 as FLOAT), cast(3.0 as DOUBLE), cast(null as REAL), cast(null as FLOAT), cast(null as DOUBLE) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null", 
            "null"};
        if (checkJdbc40()) 
        testGetObject(testArray); 
    }
    
    /* Test java.sql.RowId 23X with decimal X3 */ 
    public void Var233()
    {
        String[] testArray = {
            "java.sql.RowId",
            "select cast(1.0 as DECIMAL(10,1)), cast(2.0 as NUMERIC(10,2)), cast(3.0 as DECIMAL(10,4)), cast(null as DECIMAL(10,1)), cast(null as NUMERIC(10,2)), cast(null as DECIMAL(10,4)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null", 
            "null", 
            "null"};
        if (checkJdbc40()) 
        testGetObject(testArray); 
    }
    
    /* Test java.sql.RowId 23X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var234()
    {
        String[] testArray = {
            "java.sql.RowId",
            "select cast('1' as CHAR(10)), cast('2' as GRAPHIC(10) CCSID 1200), cast(null as GRAPHIC(10)), cast('1' as VARCHAR(10)), cast('2' as VARGRAPHIC(10) CCSID 1200), cast(null as VARGRAPHIC(10)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "Data type mismatch",
            "Data type mismatch",
            "null"};
        if (checkJdbc40()) 
        testGetObject(testArray); 
    }
    


    /* Test java.sql.RowId 23X with BINARY, VARBINARY  X5 */ 
    public void Var235()
    {
        String[] testArray = {
            "java.sql.RowId",
            "select cast(X'01' AS BINARY(1)), cast (X'02' AS VARBINARY(20)),cast(null AS BINARY(1)), cast (null AS VARBINARY(20)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null"};

        String[] testArrayNative = {
            "java.sql.RowId",
            "select cast(X'01' AS BINARY(1)), cast (X'02' AS VARBINARY(20)),cast(null AS BINARY(1)), cast (null AS VARBINARY(20)) from sysibm.sysdummy1",
	    "ROWID=01",
	    "ROWID=02",
	    "null",
            "null"};


	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

        if (checkJdbc40()) 
        testGetObject(testArray); 
    }

    /* Test java.sql.RowId 23X with date, time, timestamp  X6 */ 
    public void Var236()
    {
        String[] testArray = {
            "java.sql.RowId",
            "select cast('1:02:03' as TIME), cast ('2011-09-11' as DATE), cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        if (checkJdbc40()) 
        testGetObject(testArray); 
    }
    
    /* Test java.sql.RowId 23X with clob, dbclob, blob  X7 */ 
    public void Var237()
    {
        String[] testArray = {
            "java.sql.RowId",
            "select cast('1' as CLOB), cast ('2' as DBCLOB(1M) CCSID 1200), cast(X'03' AS BLOB) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            };


        String[] testArrayNative = {
            "java.sql.RowId",
            "select cast('1' as CLOB), cast ('2' as DBCLOB(1M) CCSID 1200), cast(X'03' AS BLOB) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "ROWID=03",
            };

	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 



        if (checkJdbc40()) 
        testGetObject(testArray); 
    }
    

    /* Test java.sql.RowId 23X with datalink  X8 */ 
    public void Var238()
    {
        String[] testArray = {
            "java.sql.RowId",
            "select DLVALUE('file://tmp/x') from sysibm.sysdummy1",
            "Data type mismatch"};
        if (checkJdbc40()) 
        testGetObject(testArray); 
    }

    /* Test java.sql.RowId 23X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var239()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.sql.RowId",
            "select CAST(CAST(X'01' AS CHAR(2) FOR BIT DATA) AS ROWID), CAST('1' as NCHAR(10)), CAST('1' AS NVARCHAR(100))," +
            " CAST('1' AS NCLOB(100K)), CAST(null AS ROWID), CAST(null as NCHAR(10)), CAST(null AS NVARCHAR(100)), CAST(null AS NCLOB(100K)) from sysibm.sysdummy1",
            "ROWID=0140",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null",
            "null",
            "null"};
    if (checkJdbc40()) 
        testGetObject(testArray);
      }
    }



    /* Test java.sql.SQLXML 24X with SQLXML X0 */ 
    public void Var240()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.sql.SQLXML",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ), CAST(NULL AS XML) from sysibm.sysdummy1", 
              "SQLXML=<h>hello</h>", 
              "null",};
           if (checkJdbc40()) 
             testGetObject(testArray);
        }
    }


    /* Test java.sql.SQLXML 24X with primitive numbers X1 */ 
    public void Var241()
    {
        String[] testArray = {
            "java.sql.SQLXML",
            "select cast(1 as SMALLINT), cast(2 as INTEGER), cast(3 as BIGINT), cast(null as SMALLINT), cast(null as INTEGER), cast(null as BIGINT)  from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null", 
            "null", 
            "null"};
        if (checkJdbc40()) 
        testGetObject(testArray); 
    }

    /* Test java.sql.SQLXML 24X with primitive doubles X2 */ 
    public void Var242()
    {
        String[] testArray = {
            "java.sql.SQLXML",
            "select cast(1.0 as REAL), cast(2.0 as FLOAT), cast(3.0 as DOUBLE), cast(null as REAL), cast(null as FLOAT), cast(null as DOUBLE) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null", 
            "null"};
        if (checkJdbc40()) 
        testGetObject(testArray); 
    }
    
    /* Test java.sql.SQLXML 24X with decimal X3 */ 
    public void Var243()
    {
        String[] testArray = {
            "java.sql.SQLXML",
            "select cast(1.0 as DECIMAL(10,1)), cast(2.0 as NUMERIC(10,2)), cast(3.0 as DECIMAL(10,4)), cast(null as DECIMAL(10,1)), cast(null as NUMERIC(10,2)), cast(null as DECIMAL(10,4)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null", 
            "null", 
            "null"};
        if (checkJdbc40()) 
        testGetObject(testArray); 
    }
    
    /* Test java.sql.SQLXML 24X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var244()
    {
	// Update 2/21/2014.  Toolbox requires SQLXML data to be valid XML
        String[] testArray = {
            "java.sql.SQLXML",
            "select cast('<h>stf</h>' as CHAR(10)), "+
	      "cast('<h>stf</h>' as GRAPHIC(10) CCSID 1200), "+
	      "cast('<h>nothing</h>' as CHAR(14)), "+
              "cast('<h>nothing</h>' as GRAPHIC(14) CCSID 1200), "+
              "cast(null as CHAR(10)), "+
              "cast(null as GRAPHIC(10)), "+
              "cast('<h>h</h>' as VARCHAR(10)), "+
              "cast('<h>h</h>' as VARGRAPHIC(10) CCSID 1200), "+
              "cast('<h>1</h>' as VARCHAR(10)), "+
              "cast('<h>2</h>' as VARGRAPHIC(10) CCSID 1200), "+
	      "cast(null as VARCHAR(10)), "+
              "cast(null as VARGRAPHIC(10)) from sysibm.sysdummy1",
            "SQLXML=<h>stf</h>",
            "SQLXML=<h>stf</h>",
	    "SQLXML=<h>nothing</h>",
	    "SQLXML=<h>nothing</h>",
	    "null",
            "null",
            "SQLXML=<h>h</h>",
            "SQLXML=<h>h</h>",
            "SQLXML=<h>1</h>",
            "SQLXML=<h>2</h>",
            "null",
	    "null"};
        if (checkJdbc40()) 
        testGetObject(testArray); 
    }
    


    /* Test java.sql.SQLXML 24X with BINARY, VARBINARY  X5 */ 
    public void Var245()
    {
        String[] testArray = {
            "java.sql.SQLXML",
            "select cast(X'01' AS BINARY(1)), cast (X'02' AS VARBINARY(20)),cast(null AS BINARY(1)), cast (null AS VARBINARY(20)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null"};
        if (checkJdbc40()) 
        testGetObject(testArray); 
    }

    /* Test java.sql.SQLXML 24X with date, time, timestamp  X6 */ 
    public void Var246()
    {
        String[] testArray = {
            "java.sql.SQLXML",
            "select cast('1:02:03' as TIME), cast ('2011-09-11' as DATE), cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        if (checkJdbc40()) 
        testGetObject(testArray); 
    }
    
    /* Test java.sql.SQLXML 24X with clob, dbclob, blob  X7 */ 
    public void Var247()
    {
	/* Note.  The current code does not validate XML for locator types. */
        String[] testArray = {
            "java.sql.SQLXML",
            "select cast('<h>h</h>' as CLOB), "+
                   "cast ('<h>h</h>' as DBCLOB(1M) CCSID 1200), "+
                   "cast ('badstuff' as CLOB), "+
                   "cast ('badstuff' as DBCLOB(1M) CCSID 1200), "+
                   "cast(X'03' AS BLOB), "+
                   "cast (null as CLOB), "+
                   "cast (null as DBCLOB(1M) CCSID 1200), "+
                   "cast(null AS BLOB) from sysibm.sysdummy1",
            "SQLXML=<h>h</h>",
            "SQLXML=<h>h</h>",
            "SQLXML=badstuff",
            "Data type mismatch",
            "Data type mismatch",
	    "null",
	    "null",
	    "null",
            };


        String[] testArrayNative = {
            "java.sql.SQLXML",
            "select cast('<h>h</h>' as CLOB), "+
                   "cast ('<h>h</h>' as DBCLOB(1M) CCSID 1200), "+
                   "cast ('badstuff' as CLOB), "+
                   "cast ('badstuff' as DBCLOB(1M) CCSID 1200), "+
                   "cast(X'03' AS BLOB), "+
                   "cast (null as CLOB), "+
                   "cast (null as DBCLOB(1M) CCSID 1200), "+
                   "cast(null AS BLOB) from sysibm.sysdummy1",
            "SQLXML=<h>h</h>",
            "SQLXML=<h>h</h>",
            "SQLXML=badstuff",
            "SQLXML=badstuff",
            "Data type mismatch",
	    "null",
	    "null",
	    "null",
            };

	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

        if (checkJdbc40()) 
        testGetObject(testArray); 
    }
    

    /* Test java.sql.SQLXML 24X with datalink  X8 */ 
    public void Var248()
    {
        String[] testArray = {
            "java.sql.SQLXML",
            "select DLVALUE('file://tmp/x') from sysibm.sysdummy1",
            "Data type mismatch"};
        if (checkJdbc40()) 
        testGetObject(testArray); 
    }

    /* Test java.sql.SQLXML 24X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var249()
    {
	// Update 2/21/2014  Toolbox SQLXML expects valid SQL
      if (checkRelease710()) {  
    String[] testArray = {
            "java.sql.SQLXML",
            "select CAST(CAST(X'01' AS CHAR(2) FOR BIT DATA) AS ROWID), "+
                   "CAST('<h>h</h>' as NCHAR(10)), "+
                   "CAST('<h>h</h>' AS NVARCHAR(100))," +
                   "CAST('<h>h</h>' AS NCLOB(100K)), "+
                   "CAST('<h>1</h>' as NCHAR(10)), "+
                   "CAST('<h>1</h>' AS NVARCHAR(100))," +
                   "CAST('<h>1</h>' AS NCLOB(100K)), "+
                   "CAST(null AS ROWID), CAST(null as NCHAR(10)), CAST(null AS NVARCHAR(100)), CAST(null AS NCLOB(100K)) from sysibm.sysdummy1",
            "Data type mismatch",
            "SQLXML=<h>h</h>",
            "SQLXML=<h>h</h>",
            "SQLXML=<h>h</h>",
            "SQLXML=<h>1</h>",
            "SQLXML=<h>1</h>",
            "SQLXML=<h>1</h>",   /* toolbox does not validate for lob types */ 
            "null",
            "null",
            "null",
            "null"};

    String[] testArrayNative  = {
            "java.sql.SQLXML",
            "select CAST(CAST(X'01' AS CHAR(2) FOR BIT DATA) AS ROWID), "+
                   "CAST('<h>h</h>' as NCHAR(10)), "+
                   "CAST('<h>h</h>' AS NVARCHAR(100))," +
                   "CAST('<h>h</h>' AS NCLOB(100K)), "+
                   "CAST('1' as NCHAR(10)), "+
                   "CAST('1' AS NVARCHAR(100))," +
                   "CAST('1' AS NCLOB(100K)), "+
                   "CAST(null AS ROWID), CAST(null as NCHAR(10)), CAST(null AS NVARCHAR(100)), CAST(null AS NCLOB(100K)) from sysibm.sysdummy1",
            "Data type mismatch",
            "SQLXML=<h>h</h>",
            "SQLXML=<h>h</h>",
            "SQLXML=<h>h</h>",
            "SQLXML=1",
            "SQLXML=1",
            "SQLXML=1",   /* native no longer validates 8/7/2013 */ 
            "null",
            "null",
            "null",
            "null"};



	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

    if (checkJdbc40()) 
        testGetObject(testArray);
      }
    }


    /* Filler */

    public void Var250() { notApplicable(); }
    public void Var251() { notApplicable(); }
    public void Var252() { notApplicable(); }
    public void Var253() { notApplicable(); }
    public void Var254() { notApplicable(); }
    public void Var255() { notApplicable(); }
    public void Var256() { notApplicable(); }
    public void Var257() { notApplicable(); }
    public void Var258() { notApplicable(); }
    public void Var259() { notApplicable(); } 
    public void Var260() { notApplicable(); }
    public void Var261() { notApplicable(); }
    public void Var262() { notApplicable(); }
    public void Var263() { notApplicable(); }
    public void Var264() { notApplicable(); }
    public void Var265() { notApplicable(); }
    public void Var266() { notApplicable(); }
    public void Var267() { notApplicable(); }
    public void Var268() { notApplicable(); }
    public void Var269() { notApplicable(); } 
    public void Var270() { notApplicable(); }
    public void Var271() { notApplicable(); }
    public void Var272() { notApplicable(); }
    public void Var273() { notApplicable(); }
    public void Var274() { notApplicable(); }
    public void Var275() { notApplicable(); }
    public void Var276() { notApplicable(); }
    public void Var277() { notApplicable(); }
    public void Var278() { notApplicable(); }
    public void Var279() { notApplicable(); } 
    public void Var280() { notApplicable(); }
    public void Var281() { notApplicable(); }
    public void Var282() { notApplicable(); }
    public void Var283() { notApplicable(); }
    public void Var284() { notApplicable(); }
    public void Var285() { notApplicable(); }
    public void Var286() { notApplicable(); }
    public void Var287() { notApplicable(); }
    public void Var288() { notApplicable(); }
    public void Var289() { notApplicable(); } 
    public void Var290() { notApplicable(); }
    public void Var291() { notApplicable(); }
    public void Var292() { notApplicable(); }
    public void Var293() { notApplicable(); }
    public void Var294() { notApplicable(); }
    public void Var295() { notApplicable(); }
    public void Var296() { notApplicable(); }
    public void Var297() { notApplicable(); }
    public void Var298() { notApplicable(); }
    public void Var299() { notApplicable(); }
    public void Var300() { notApplicable(); } 

    /* Repeat tests, this time using the column name, where columns are named C1, C2, C3 */


/**
getObject() - Should throw exception when the result set is
closed.
**/
    public void Var301()
    {
	if (checkJdbc40()) {
	    try {
		Statement s = connection_.createStatement ();
		ResultSet rs = s.executeQuery ("SELECT * FROM "
					       + JDRSTest.RSTEST_GET);
		rs.next ();
		rs.close ();
		JDReflectionUtil.callMethod_O(rs, "getObject", "C_KEY", String.class);
		failed ("Didn't throw SQLException");
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



/**
getObject() - Should throw exception when cursor is not pointing
to a row.
**/
    public void Var302()
    {
	if (checkJdbc40()) {
	    try {
		ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							 + JDRSTest.RSTEST_GET);
		Object s = 	    JDReflectionUtil.callMethod_O(rs, "getObject", "C_KEY", String.class);
		failed ("Didn't throw SQLException"+s);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



/**
getObject() - Should throw an exception when the column name does not exist
**/
    public void Var303()
    {
	if (checkJdbc40()) {
	    try {
		ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							 + JDRSTest.RSTEST_GET);
		rs.next(); 
		Object s =   JDReflectionUtil.callMethod_O(rs, "getObject", "COLUMNDOESNOTEXIT", String.class);
		failed ("Didn't throw SQLException"+s);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
getObject() - Should throw an exception when the column names does not exist
**/
    public void Var304()
    {
	if (checkJdbc40()) {
	    try {
		ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							 + JDRSTest.RSTEST_GET);
		JDRSTest.position0 (rs, "DATE_1998");
		Object s =  JDReflectionUtil.callMethod_O(rs, "getObject", "COLUMNDOESNOTEXIST", String.class);
		failed ("Didn't throw SQLException"+s);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



/**
getObject() - Should throw an exception when the column name does not exist
**/
    public void Var305()
    {
	if (checkJdbc40()) {
	    try {
		ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							 + JDRSTest.RSTEST_GET);
		JDRSTest.position0 (rs, "DATE_1998");
		Object s =  JDReflectionUtil.callMethod_O(rs, "getObject", "-1", String.class);
		failed ("Didn't throw SQLException"+s);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}

    }

/**
getObject() - Should work when the column index is valid.
**/
    public void Var306()
    {
      if (checkJdbc41())
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s =  (String) JDReflectionUtil.callMethod_O(rs, "getObject", "C_KEY", String.class);
            assertCondition (s.equals ("DATE_1998"), "Got "+s+" expected DATE_1998");
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getObject() - Should throw an exception when the column
name is null.
**/
    public void Var307()
    {
      if (checkJdbc41())
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            Object s =  JDReflectionUtil.callMethod_O(rs, "getObject", (String) null, String.class);
            failed ("Didn't throw SQLException"+s);
        } catch (Exception e) {
            if (getDriver() == JDTestDriver.DRIVER_JCC) {
              assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
            } else { 
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getObject() - Should throw an exception when the column
name is an empty string.
**/
    public void Var308()
    {
      if (checkJdbc41())
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            Object s =  JDReflectionUtil.callMethod_O(rs, "getObject", "", String.class);
            failed ("Didn't throw SQLException"+s);
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getObject() - Should throw an exception when the column
name is invalid.
**/
    public void Var309()
    {
      if (checkJdbc41())
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            Object s =  JDReflectionUtil.callMethod_O(rs, "getObject", "INVALID", String.class);
            failed ("Didn't throw SQLException"+s);
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }





    /*
     * get getObject using the following testSpec, where the elements of the array are as follows
     * Columns in the result set must be named C1, C2, C3, etc... 
     * testSpec[0]  className
     * testSpec[1]  queryToRun
     * testSpec[2]  expected value for column 1
     * testSpec[3]  expected value for column 2
     * ....
     */ 
    public void testGetObjectNamed(String[] testSpec) {
      if (checkJdbc41()) {
	StringBuffer sb = new StringBuffer();
	boolean passed = true; 
	try {
	    String className = testSpec[0];
	    String query     = testSpec[1];
	    int columnCount = testSpec.length - 2;
	    sb.append("Testing class "+className+"\n");
	    Class<?> lookupClass = Class.forName(className); 
	    
	    Class<?> xmlClass = null;
      try {
        xmlClass = Class.forName("java.sql.SQLXML");
      } catch (Exception e) {
      }

      Class<?> rowidClass = null;
      try {
        rowidClass = Class.forName("java.sql.RowId");
      } catch (Exception e) {
      }

	    sb.append("Running query "+query+"\n");

	    ResultSet rs = statement0_.executeQuery(query); 
	    rs.next();
	    for (int i = 1; i <= columnCount; i++) {
		String expected = testSpec[i+1]; 
		try {
		    String columnName = "C"+i;
		    sb.append("Looking up column "+columnName+"\n"); 
		    Object o = JDReflectionUtil.callMethod_O(rs, "getObject", columnName, lookupClass);
                    String objectString;
                    String objectClassName; 
                    Class<?> objectClass = null; 
                    if (o == null) {
                      objectString="null";
                      objectClass = lookupClass; 
                      objectClassName = lookupClass.getName(); 
                    } else { 
                      objectClass = o.getClass(); 
                      objectClassName = objectClass.getName();
                      
                      if (objectClassName.equals("[B")) {
                        
                        objectString = "BYTEARRAY="+bytesToString((byte[]) o); 
                      } else if (o instanceof java.io.InputStream) {
                          objectString = "INPUTSTREAM="+inputStreamToString((java.io.InputStream) o); 
                      } else if (o instanceof java.io.Reader) {
                        objectString = "READER="+readerToString((java.io.Reader) o); 
                      } else if (o instanceof java.sql.Blob) {
                        InputStream in = ((java.sql.Blob) o).getBinaryStream();  
                        objectString = "BLOB="+inputStreamToString(in); 
                      } else if (o instanceof java.sql.Clob) {
                        Reader in = ((java.sql.Clob) o).getCharacterStream();  
                        objectString = "CLOB="+readerToString(in); 
                      } else if ( rowidClass != null &&  rowidClass.isAssignableFrom(objectClass)) {
                        byte[] stuff = (byte[]) JDReflectionUtil.callMethod_O(o, "getBytes"); 
                        objectString = "ROWID="+bytesToString(stuff); 
                      } else if ( xmlClass != null  && xmlClass.isAssignableFrom(objectClass)) {
                        Reader in = (Reader) JDReflectionUtil.callMethod_O(o, "getCharacterStream"); 
                        objectString = "SQLXML="+readerToString(in); 
                      } else { 
		         objectString = o.toString();
                      }
                    }
		    if (expected.equals(objectString)) {
			// Object is good 
		    } else {
			sb.append("For column "+i+" got "+objectString+" sb "+expected+"\n");
			passed = false; 
		    } 

		    if (objectClassName.equals(className)) {
		      // Class is good 
		    } else {
		      if (lookupClass.isAssignableFrom(objectClass)) {
		        // Still good 
		      } else { 
		        sb.append("For column "+i+" got class="+objectClassName+" sb class="+className+"\n");
                        passed = false;
		      }
		    }
		    
		} catch (Exception e) {
		    String exceptionString = e.toString();
		    if (exceptionString == null) {
		      exceptionString = e.getClass().getName()+":null"; 
		    }
		    if (exceptionString.indexOf(expected) >= 0) {
			// Exception is good 
		    } else {
			sb.append("For column "+i+" got exception "+exceptionString+" sb "+expected+"\n");
			StringWriter stringWriter = new StringWriter(); 
			PrintWriter printWriter = new PrintWriter(stringWriter);
			// Only dump the exception once 
			e.printStackTrace(printWriter);
			String exception = stringWriter.toString();
			String checkException = exception; 
			int getObjectIndex = checkException.indexOf("JDRSGetObject41");
			if (getObjectIndex >= 0) {
			    checkException = checkException.substring(0,getObjectIndex); 
			} 

			if (foundExceptions_ == null) {
			    foundExceptions_ = new Hashtable<String,String>(); 
			}
			if (foundExceptions_.get(checkException) == null) {
			    foundExceptions_.put(checkException, checkException);
			    sb.append(exception); 
			} else {
			    // Skip 
			} 

			passed = false; 
		    } 
		} 
	    } 
	    rs.close();
	    assertCondition(passed,sb);
	} catch (Exception e) {
            failed (e, "Unexpected Exception "+sb.toString());
	} 
    } 
    }

    /* Test LocalDate with SQLXML X0 */ 
    public void Var310()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.lang.String",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ) as C1, CAST(null as XML) as C2 from sysibm.sysdummy1", 
              "<h>hello</h>",
              "null"};
           testGetObjectNamed(testArray);
        }
    }



    /* Test LocalDate with primitive numbers X1 */ 
    public void Var311()
    {
	String[] testArray = {
	    "java.lang.String",
	    "select cast(1 as SMALLINT) AS C1, cast(2 as INTEGER) AS C2, cast(3 as BIGINT) AS C3, cast(null as SMALLINT) AS C4, cast(null as INTEGER) AS C5, cast(null as BIGINT)  AS C6 from sysibm.sysdummy1",
	    "1",
	    "2",
	    "3", 
	    "null",
	    "null",
	    "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test LocalDate with primitive doubles X2 */ 
    public void Var312()
    {
        String[] testArray = {
            "java.lang.String",
            "select cast(1.0 as REAL) AS C1, cast(2.0 as FLOAT) AS C2, cast(3.0 as DOUBLE) AS C3, cast(null as REAL) as C4, CAST( null as FLOAT) AS C5, cast(null as DOUBLE) AS C6  from sysibm.sysdummy1",
            "1.0",
            "2.0",
            "3.0",
            "null",
            "null",
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test LocalDate with decimal X3 */ 
    public void Var313()
    {
        String[] testArray = {
            "java.lang.String",
            "select cast(1.0 as DECIMAL(10,1)) AS C1, cast(2.0 as NUMERIC(10,2)) AS C2, cast(3.0 as DECIMAL(10,4)) AS C3, cast(null as DECIMAL(10,1)) AS C4, cast(null as NUMERIC(10,2)) AS C5, cast(null as DECIMAL(10,4)) AS C6 from sysibm.sysdummy1",
            "1.0",
            "2.00",
            "3.0000",
            "null",
            "null",
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test LocalDate with char, graphic, varchar, vargraphic   X4 */ 
    public void Var314()
    {
        String[] testArray = {
            "java.lang.String",
            "select cast('ABC' as CHAR(10)) AS C1, cast('ABC' as GRAPHIC(10) CCSID 1200) AS C2, cast(null as GRAPHIC(10)) AS C3, cast('ABC' as VARCHAR(10)) AS C4, cast('ABC' as VARGRAPHIC(10) CCSID 1200) AS C5, cast(null as VARGRAPHIC(10)) AS C6 from sysibm.sysdummy1",
            "ABC       ",
            "ABC       ",
            "null",
            "ABC",
            "ABC",
            "null"};
        testGetObjectNamed(testArray); 
    }
    


    /* Test LocalDate with BINARY, VARBINARY  X5 */ 
    public void Var315()
    {
        String[] testArray = {
            "java.lang.String",
            "select cast(X'313233' AS BINARY(10)) AS C1, cast (X'313233' AS VARBINARY(20)) AS C2,cast(null AS BINARY(10)) AS C3, cast (null AS VARBINARY(20)) AS C4 from sysibm.sysdummy1",
            "31323300000000000000",
            "313233", 
            "null", 
            "null"};


        String[] testArrayNative = {
            "java.lang.String",
            "select cast(X'313233' AS BINARY(10)) AS C1, cast (X'313233' AS VARBINARY(20)) AS C2,cast(null AS BINARY(10)) AS C3, cast (null AS VARBINARY(20)) AS C4 from sysibm.sysdummy1",
            "123\u0000\u0000\u0000\u0000\u0000\u0000\u0000",
            "123", 
            "null", 
            "null"};

	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

        testGetObjectNamed(testArray); 
    }

    /* Test LocalDate with date, time, timestamp  X6 */ 
    public void Var316()
    {
        String[] testArray = {
            "java.lang.String",
            "select cast('1:02:03' as TIME) AS C1, cast ('2011-09-11' as DATE) AS C2, cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) AS C3, cast(null as TIME) AS C4, cast (null as DATE) AS C5, cast(null AS TIMESTAMP) AS C6 from sysibm.sysdummy1",
            "01:02:03",
            "2011-09-11",
            "2011-09-11 01:02:03.123456", 
            "null", 
            "null", 
            "null"};


        testGetObjectNamed(testArray); 
    }
    
    /* Test LocalDate with clob, dbclob, blob  X7 */ 
    public void Var317()
    {
        String[] testArray = {
            "java.lang.String",
            "select cast('ABC' as CLOB) AS C1, cast ('ABC' as DBCLOB(1M) CCSID 1200) AS C2, cast(X'313233' AS BLOB) AS C3, cast(null as CLOB) AS C4, cast (null as DBCLOB(1M) CCSID 1200) AS C5, cast(null AS BLOB) AS C6 from sysibm.sysdummy1",
            "ABC",
            "ABC",
            "313233", 
            "null", 
            "null", 
            "null"};

        String[] testArrayNative = {
            "java.lang.String",
            "select cast('ABC' as CLOB) AS C1, cast ('ABC' as DBCLOB(1M) CCSID 1200) AS C2, cast(X'313233' AS BLOB) AS C3, cast(null as CLOB) AS C4, cast (null as DBCLOB(1M) CCSID 1200) AS C5, cast(null AS BLOB) AS C6 from sysibm.sysdummy1",
            "ABC",
            "ABC",
            "123", 
            "null", 
            "null", 
            "null"};

	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

        testGetObjectNamed(testArray); 
    }
    

    /* Test LocalDate with datalink  X8 */ 
    public void Var318()
    {
        String[] testArray = {
            "java.lang.String",
            "select DLVALUE('file://tmp/x') AS C1, CAST(NULL AS DATALINK) AS C2 from sysibm.sysdummy1",
            "FILE://TMP/x", 
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test LocalDate with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var319()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.lang.String",
            "select CAST(CAST(X'1122' AS CHAR(2) FOR BIT DATA) AS ROWID) AS C1, CAST('ABC' as NCHAR(10)) AS C2, CAST('ABC' AS NVARCHAR(100)) AS C3, CAST('ABC' AS NCLOB(100K)) AS C4, CAST(null AS ROWID) AS C5, CAST(null as NCHAR(10)) AS C6, CAST(null AS NVARCHAR(100)) AS C7, CAST(null AS NCLOB(100K)) AS C8 from sysibm.sysdummy1",
            "1122",
            "ABC       ",
            "ABC",
            "ABC", 
            "null", 
            "null", 
            "null",
            "null"};
        testGetObjectNamed(testArray);
      }
    }


    /* Test java.lang.Byte 2X with SQLXML X0 */ 
    public void Var320()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.lang.Byte",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ) as C1, CAST(null as XML) as C2 from sysibm.sysdummy1", 
              "Data type mismatch",
              "null",};
           testGetObjectNamed(testArray);
        }
    }



    /* Test java.lang.Byte 2X with primitive numbers X1 */ 
    public void Var321()
    {
        String[] testArray = {
            "java.lang.Byte",
            "select cast(1 as SMALLINT) AS C1, cast(2 as INTEGER) AS C2, cast(3 as BIGINT) AS C3, cast(null as SMALLINT) AS C4, cast(null as INTEGER) AS C5, cast(null as BIGINT) AS C6 from sysibm.sysdummy1",
            "1",
            "2",
            "3", 
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.lang.Byte 2X with primitive doubles X2 */ 
    public void Var322()
    {
        String[] testArray = {
            "java.lang.Byte",
            "select cast(1.0 as REAL) AS C1, cast(2.0 as FLOAT) AS C2, cast(3.0 as DOUBLE) AS C3, cast(null as REAL) as C4, cast(null AS FLOAT) AS C5, cast(null as DOUBLE) AS C6 from sysibm.sysdummy1",
            "1",
            "2",
            "3", 
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.lang.Byte 2X with decimal X3 */ 
    public void Var323()
    {
        String[] testArray = {
            "java.lang.Byte",
            "select cast(1.0 as DECIMAL(10,1)) AS C1, cast(2.0 as NUMERIC(10,2)) AS C2, cast(3.0 as DECIMAL(10,4)) AS C3, cast(null as DECIMAL(10,1)) AS C4, cast(null as NUMERIC(10,2)) AS C5, cast(null as DECIMAL(10,4)) AS C6 from sysibm.sysdummy1",
            "1",
            "2",
            "3", 
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.lang.Byte 2X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var324()
    {
        String[] testArray = {
            "java.lang.Byte",
            "select cast('1' as CHAR(10)) AS C1, cast('2' as GRAPHIC(10) CCSID 1200) AS C2, cast(null as GRAPHIC(10)) AS C3, cast('1' as VARCHAR(10)) AS C4, cast('2' as VARGRAPHIC(10) CCSID 1200) AS C5, cast(null as VARGRAPHIC(10)) AS C6 from sysibm.sysdummy1",
            "1",
            "2",
            "null",
            "1",
            "2",
            "null"};
        testGetObjectNamed(testArray); 
    }
    


    /* Test java.lang.Byte 2X with BINARY, VARBINARY  X5 */ 
    public void Var325()
    {
        String[] testArray = {
            "java.lang.Byte",
            "select cast(X'01' AS BINARY(1)) AS C1, cast (X'02' AS VARBINARY(20)) AS C2,cast(null AS BINARY(1)) AS C3, cast (null AS VARBINARY(20)) AS C4 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.lang.Byte 2X with date, time, timestamp  X6 */ 
    public void Var326()
    {
        String[] testArray = {
            "java.lang.Byte",
            "select cast('1:02:03' as TIME) AS C1, cast ('2011-09-11' as DATE) AS C2, cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) AS C3 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.lang.Byte 2X with clob, dbclob, blob  X7 */ 
    public void Var327()
    {
        String[] testArray = {
            "java.lang.Byte",
            "select cast('1' as CLOB) AS C1, cast ('2' as DBCLOB(1M) CCSID 1200) AS C2, cast(X'03' AS BLOB) AS C3 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObjectNamed(testArray); 
    }
    

    /* Test java.lang.Byte 2X with datalink  X8 */ 
    public void Var328()
    {
        String[] testArray = {
            "java.lang.Byte",
            "select DLVALUE('file://tmp/x') AS C1 from sysibm.sysdummy1",
            "Data type mismatch"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.lang.Byte 2X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var329()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.lang.Byte",
            "select CAST(CAST(X'01' AS CHAR(2) FOR BIT DATA) AS ROWID) AS C1, CAST('1' as NCHAR(10)) AS C2, CAST('1' AS NVARCHAR(100)) AS C3, CAST('1' AS NCLOB(100K)) AS C4, CAST(null AS ROWID) AS C5, CAST(null as NCHAR(10)) AS C6, CAST(null AS NVARCHAR(100)) AS C7, CAST(null AS NCLOB(100K)) AS C8 from sysibm.sysdummy1",
            "Data type mismatch",
            "1",
            "1",
            "Data type mismatch",
            "null",
            "null",
            "null",
            "null"};
        testGetObjectNamed(testArray);
      }
    }


    /* Test java.lang.Integer 3X with SQLXML X0 */ 
    public void Var330()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.lang.Integer",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ) as C1, CAST(null as XML) as C2 from sysibm.sysdummy1", 
              "Data type mismatch",
              "null",};
           testGetObjectNamed(testArray);
        }
    }



    /* Test java.lang.Integer 3X with primitive numbers X1 */ 
    public void Var331()
    {
        String[] testArray = {
            "java.lang.Integer",
            "select cast(1 as SMALLINT) AS C1, cast(2 as INTEGER) AS C2, cast(3 as BIGINT) AS C3, cast(null as SMALLINT) AS C4, cast(null as INTEGER) AS C5, cast(null as BIGINT) AS C6 from sysibm.sysdummy1",
            "1",
            "2",
            "3", 
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.lang.Integer 3X with primitive doubles X2 */ 
    public void Var332()
    {
        String[] testArray = {
            "java.lang.Integer",
            "select cast(1.0 as REAL) AS C1, cast(2.0 as FLOAT) AS C2, cast(3.0 as DOUBLE) AS C3, cast(null as REAL) as C4, cast(null AS FLOAT) AS C5, cast(null as DOUBLE) AS C6 from sysibm.sysdummy1",
            "1",
            "2",
            "3", 
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.lang.Integer 3X with decimal X3 */ 
    public void Var333()
    {
        String[] testArray = {
            "java.lang.Integer",
            "select cast(1.0 as DECIMAL(10,1)) AS C1, cast(2.0 as NUMERIC(10,2)) AS C2, cast(3.0 as DECIMAL(10,4)) AS C3, cast(null as DECIMAL(10,1)) AS C4, cast(null as NUMERIC(10,2)) AS C5, cast(null as DECIMAL(10,4)) AS C6 from sysibm.sysdummy1",
            "1",
            "2",
            "3", 
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.lang.Integer 3X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var334()
    {
        String[] testArray = {
            "java.lang.Integer",
            "select cast('1' as CHAR(10)) AS C1, cast('2' as GRAPHIC(10) CCSID 1200) AS C2, cast(null as GRAPHIC(10)) AS C3, cast('1' as VARCHAR(10)) AS C4, cast('2' as VARGRAPHIC(10) CCSID 1200) AS C5, cast(null as VARGRAPHIC(10)) AS C6 from sysibm.sysdummy1",
            "1",
            "2",
            "null",
            "1",
            "2",
            "null"};
        testGetObjectNamed(testArray); 
    }
    


    /* Test java.lang.Integer 3X with BINARY, VARBINARY  X5 */ 
    public void Var335()
    {
        String[] testArray = {
            "java.lang.Integer",
            "select cast(X'01' AS BINARY(1)) AS C1, cast (X'02' AS VARBINARY(20)) AS C2,cast(null AS BINARY(1)) AS C3, cast (null AS VARBINARY(20)) AS C4 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.lang.Integer 3X with date, time, timestamp  X6 */ 
    public void Var336()
    {
        String[] testArray = {
            "java.lang.Integer",
            "select cast('1:02:03' as TIME) AS C1, cast ('2011-09-11' as DATE) AS C2, cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) AS C3 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.lang.Integer 3X with clob, dbclob, blob  X7 */ 
    public void Var337()
    {
        String[] testArray = {
            "java.lang.Integer",
            "select cast('1' as CLOB) AS C1, cast ('2' as DBCLOB(1M) CCSID 1200) AS C2, cast(X'03' AS BLOB) AS C3 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObjectNamed(testArray); 
    }
    

    /* Test java.lang.Integer 3X with datalink  X8 */ 
    public void Var338()
    {
        String[] testArray = {
            "java.lang.Integer",
            "select DLVALUE('file://tmp/x') AS C1 from sysibm.sysdummy1",
            "Data type mismatch"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.lang.Integer 3X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var339()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.lang.Integer",
            "select CAST(CAST(X'01' AS CHAR(2) FOR BIT DATA) AS ROWID) AS C1, CAST('1' as NCHAR(10)) AS C2, CAST('1' AS NVARCHAR(100)) AS C3, CAST('1' AS NCLOB(100K)) AS C4, CAST(null AS ROWID) AS C5, CAST(null as NCHAR(10)) AS C6, CAST(null AS NVARCHAR(100)) AS C7, CAST(null AS NCLOB(100K)) AS C8 from sysibm.sysdummy1",
            "Data type mismatch",
            "1",
            "1",
            "Data type mismatch",
            "null",
            "null",
            "null",
            "null"};
        testGetObjectNamed(testArray);
      }
    }

    /* Test java.lang.Short 4X with SQLXML X0 */ 
    public void Var340()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.lang.Short",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ) as C1, CAST(null as XML) as C2 from sysibm.sysdummy1", 
              "Data type mismatch",
              "null",};
           testGetObjectNamed(testArray);
        }
    }



    /* Test java.lang.Short 4X with primitive numbers X1 */ 
    public void Var341()
    {
        String[] testArray = {
            "java.lang.Short",
            "select cast(1 as SMALLINT) AS C1, cast(2 as INTEGER) AS C2, cast(3 as BIGINT) AS C3, cast(null as SMALLINT) AS C4, cast(null as INTEGER) AS C5, cast(null as BIGINT) AS C6 from sysibm.sysdummy1",
            "1",
            "2",
            "3", 
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.lang.Short 4X with primitive doubles X2 */ 
    public void Var342()
    {
        String[] testArray = {
            "java.lang.Short",
            "select cast(1.0 as REAL) AS C1, cast(2.0 as FLOAT) AS C2, cast(3.0 as DOUBLE) AS C3, cast(null as REAL) as C4, cast(null AS FLOAT) AS C5, cast(null as DOUBLE) AS C6 from sysibm.sysdummy1",
            "1",
            "2",
            "3", 
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.lang.Short 4X with decimal X3 */ 
    public void Var343()
    {
        String[] testArray = {
            "java.lang.Short",
            "select cast(1.0 as DECIMAL(10,1)) AS C1, cast(2.0 as NUMERIC(10,2)) AS C2, cast(3.0 as DECIMAL(10,4)) AS C3, cast(null as DECIMAL(10,1)) AS C4, cast(null as NUMERIC(10,2)) AS C5, cast(null as DECIMAL(10,4)) AS C6 from sysibm.sysdummy1",
            "1",
            "2",
            "3", 
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.lang.Short 4X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var344()
    {
        String[] testArray = {
            "java.lang.Short",
            "select cast('1' as CHAR(10)) AS C1, cast('2' as GRAPHIC(10) CCSID 1200) AS C2, cast(null as GRAPHIC(10)) AS C3, cast('1' as VARCHAR(10)) AS C4, cast('2' as VARGRAPHIC(10) CCSID 1200) AS C5, cast(null as VARGRAPHIC(10)) AS C6 from sysibm.sysdummy1",
            "1",
            "2",
            "null",
            "1",
            "2",
            "null"};
        testGetObjectNamed(testArray); 
    }
    


    /* Test java.lang.Short 4X with BINARY, VARBINARY  X5 */ 
    public void Var345()
    {
        String[] testArray = {
            "java.lang.Short",
            "select cast(X'01' AS BINARY(1)) AS C1, cast (X'02' AS VARBINARY(20)) AS C2,cast(null AS BINARY(1)) AS C3, cast (null AS VARBINARY(20)) AS C4 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.lang.Short 4X with date, time, timestamp  X6 */ 
    public void Var346()
    {
        String[] testArray = {
            "java.lang.Short",
            "select cast('1:02:03' as TIME) AS C1, cast ('2011-09-11' as DATE) AS C2, cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) AS C3 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.lang.Short 4X with clob, dbclob, blob  X7 */ 
    public void Var347()
    {
        String[] testArray = {
            "java.lang.Short",
            "select cast('1' as CLOB) AS C1, cast ('2' as DBCLOB(1M) CCSID 1200) AS C2, cast(X'03' AS BLOB) AS C3 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObjectNamed(testArray); 
    }
    

    /* Test java.lang.Short 4X with datalink  X8 */ 
    public void Var348()
    {
        String[] testArray = {
            "java.lang.Short",
            "select DLVALUE('file://tmp/x') AS C1 from sysibm.sysdummy1",
            "Data type mismatch"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.lang.Short 4X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var349()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.lang.Short",
            "select CAST(CAST(X'01' AS CHAR(2) FOR BIT DATA) AS ROWID) AS C1, CAST('1' as NCHAR(10)) AS C2, CAST('1' AS NVARCHAR(100)) AS C3, CAST('1' AS NCLOB(100K)) AS C4, CAST(null AS ROWID) AS C5, CAST(null as NCHAR(10)) AS C6, CAST(null AS NVARCHAR(100)) AS C7, CAST(null AS NCLOB(100K)) AS C8 from sysibm.sysdummy1",
            "Data type mismatch",
            "1",
            "1",
            "Data type mismatch",
            "null",
            "null",
            "null",
            "null"};
        testGetObjectNamed(testArray);
      }
    }


    /* Test java.lang.Long 5X with SQLXML X0 */ 
    public void Var350()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.lang.Long",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ) as C1, CAST(null as XML) as C2 from sysibm.sysdummy1", 
              "Data type mismatch",
              "null",};
           testGetObjectNamed(testArray);
        }
    }



    /* Test java.lang.Long 5X with primitive numbers X1 */ 
    public void Var351()
    {
        String[] testArray = {
            "java.lang.Long",
            "select cast(1 as SMALLINT) AS C1, cast(2 as INTEGER) AS C2, cast(3 as BIGINT) AS C3, cast(null as SMALLINT) AS C4, cast(null as INTEGER) AS C5, cast(null as BIGINT) AS C6 from sysibm.sysdummy1",
            "1",
            "2",
            "3", 
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.lang.Long 5X with primitive doubles X2 */ 
    public void Var352()
    {
        String[] testArray = {
            "java.lang.Long",
            "select cast(1.0 as REAL) AS C1, cast(2.0 as FLOAT) AS C2, cast(3.0 as DOUBLE) AS C3, cast(null as REAL) as C4, cast(null AS FLOAT) AS C5, cast(null as DOUBLE) AS C6 from sysibm.sysdummy1",
            "1",
            "2",
            "3", 
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.lang.Long 5X with decimal X3 */ 
    public void Var353()
    {
        String[] testArray = {
            "java.lang.Long",
            "select cast(1.0 as DECIMAL(10,1)) AS C1, cast(2.0 as NUMERIC(10,2)) AS C2, cast(3.0 as DECIMAL(10,4)) AS C3, cast(null as DECIMAL(10,1)) AS C4, cast(null as NUMERIC(10,2)) AS C5, cast(null as DECIMAL(10,4)) AS C6 from sysibm.sysdummy1",
            "1",
            "2",
            "3", 
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.lang.Long 5X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var354()
    {
        String[] testArray = {
            "java.lang.Long",
            "select cast('1' as CHAR(10)) AS C1, cast('2' as GRAPHIC(10) CCSID 1200) AS C2, cast(null as GRAPHIC(10)) AS C3, cast('1' as VARCHAR(10)) AS C4, cast('2' as VARGRAPHIC(10) CCSID 1200) AS C5, cast(null as VARGRAPHIC(10)) AS C6 from sysibm.sysdummy1",
            "1",
            "2",
            "null",
            "1",
            "2",
            "null"};
        testGetObjectNamed(testArray); 
    }
    


    /* Test java.lang.Long 5X with BINARY, VARBINARY  X5 */ 
    public void Var355()
    {
        String[] testArray = {
            "java.lang.Long",
            "select cast(X'01' AS BINARY(1)) AS C1, cast (X'02' AS VARBINARY(20)) AS C2,cast(null AS BINARY(1)) AS C3, cast (null AS VARBINARY(20)) AS C4 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.lang.Long 5X with date, time, timestamp  X6 */ 
    public void Var356()
    {
        String[] testArray = {
            "java.lang.Long",
            "select cast('1:02:03' as TIME) AS C1, cast ('2011-09-11' as DATE) AS C2, cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) AS C3 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.lang.Long 5X with clob, dbclob, blob  X7 */ 
    public void Var357()
    {
        String[] testArray = {
            "java.lang.Long",
            "select cast('1' as CLOB) AS C1, cast ('2' as DBCLOB(1M) CCSID 1200) AS C2, cast(X'03' AS BLOB) AS C3 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObjectNamed(testArray); 
    }
    

    /* Test java.lang.Long 5X with datalink  X8 */ 
    public void Var358()
    {
        String[] testArray = {
            "java.lang.Long",
            "select DLVALUE('file://tmp/x') AS C1 from sysibm.sysdummy1",
            "Data type mismatch"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.lang.Long 5X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var359()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.lang.Long",
            "select CAST(CAST(X'01' AS CHAR(2) FOR BIT DATA) AS ROWID) AS C1, CAST('1' as NCHAR(10)) AS C2, CAST('1' AS NVARCHAR(100)) AS C3, CAST('1' AS NCLOB(100K)) AS C4, CAST(null AS ROWID) AS C5, CAST(null as NCHAR(10)) AS C6, CAST(null AS NVARCHAR(100)) AS C7, CAST(null AS NCLOB(100K)) AS C8 from sysibm.sysdummy1",
            "Data type mismatch",
            "1",
            "1",
            "Data type mismatch",
            "null",
            "null",
            "null",
            "null"};
        testGetObjectNamed(testArray);
      }
    }



    /* Test java.lang.Float 6X with SQLXML X0 */ 
    public void Var360()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.lang.Float",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ) as C1, CAST(null as XML) as C2 from sysibm.sysdummy1", 
              "Data type mismatch",
              "null",};
           testGetObjectNamed(testArray);
        }
    }



    /* Test java.lang.Float 6X with primitive numbers X1 */ 
    public void Var361()
    {
        String[] testArray = {
            "java.lang.Float",
            "select cast(1 as SMALLINT) AS C1, cast(2 as INTEGER) AS C2, cast(3 as BIGINT) AS C3, cast(null as SMALLINT) AS C4, cast(null as INTEGER) AS C5, cast(null as BIGINT) AS C6 from sysibm.sysdummy1",
            "1.0",
            "2.0",
            "3.0", 
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.lang.Float 6X with primitive doubles X2 */ 
    public void Var362()
    {
        String[] testArray = {
            "java.lang.Float",
            "select cast(1.0 as REAL) AS C1, cast(2.0 as FLOAT) AS C2, cast(3.0 as DOUBLE) AS C3, cast(null as REAL) as C4, cast(null AS FLOAT) AS C5, cast(null as DOUBLE) AS C6 from sysibm.sysdummy1",
            "1.0",
            "2.0",
            "3.0", 
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.lang.Float 6X with decimal X3 */ 
    public void Var363()
    {
        String[] testArray = {
            "java.lang.Float",
            "select cast(1.0 as DECIMAL(10,1)) AS C1, cast(2.0 as NUMERIC(10,2)) AS C2, cast(3.0 as DECIMAL(10,4)) AS C3, cast(null as DECIMAL(10,1)) AS C4, cast(null as NUMERIC(10,2)) AS C5, cast(null as DECIMAL(10,4)) AS C6 from sysibm.sysdummy1",
            "1.0",
            "2.0",
            "3.0", 
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.lang.Float 6X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var364()
    {
        String[] testArray = {
            "java.lang.Float",
            "select cast('1' as CHAR(10)) AS C1, cast('2' as GRAPHIC(10) CCSID 1200) AS C2, cast(null as GRAPHIC(10)) AS C3, cast('1' as VARCHAR(10)) AS C4, cast('2' as VARGRAPHIC(10) CCSID 1200) AS C5, cast(null as VARGRAPHIC(10)) AS C6 from sysibm.sysdummy1",
            "1.0",
            "2.0",
            "null",
            "1.0",
            "2.0",
            "null"};
        testGetObjectNamed(testArray); 
    }
    


    /* Test java.lang.Float 6X with BINARY, VARBINARY  X5 */ 
    public void Var365()
    {
        String[] testArray = {
            "java.lang.Float",
            "select cast(X'01' AS BINARY(1)) AS C1, cast (X'02' AS VARBINARY(20)) AS C2,cast(null AS BINARY(1)) AS C3, cast (null AS VARBINARY(20)) AS C4 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.lang.Float 6X with date, time, timestamp  X6 */ 
    public void Var366()
    {
        String[] testArray = {
            "java.lang.Float",
            "select cast('1:02:03' as TIME) AS C1, cast ('2011-09-11' as DATE) AS C2, cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) AS C3 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.lang.Float 6X with clob, dbclob, blob  X7 */ 
    public void Var367()
    {
        String[] testArray = {
            "java.lang.Float",
            "select cast('1' as CLOB) AS C1, cast ('2' as DBCLOB(1M) CCSID 1200) AS C2, cast(X'03' AS BLOB) AS C3 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObjectNamed(testArray); 
    }
    

    /* Test java.lang.Float 6X with datalink  X8 */ 
    public void Var368()
    {
        String[] testArray = {
            "java.lang.Float",
            "select DLVALUE('file://tmp/x') AS C1 from sysibm.sysdummy1",
            "Data type mismatch"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.lang.Float 6X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var369()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.lang.Float",
            "select CAST(CAST(X'01' AS CHAR(2) FOR BIT DATA) AS ROWID) AS C1, CAST('1' as NCHAR(10)) AS C2, CAST('1' AS NVARCHAR(100)) AS C3, CAST('1' AS NCLOB(100K)) AS C4, CAST(null AS ROWID) AS C5, CAST(null as NCHAR(10)) AS C6, CAST(null AS NVARCHAR(100)) AS C7, CAST(null AS NCLOB(100K)) AS C8 from sysibm.sysdummy1",
            "Data type mismatch",
            "1.0",
            "1.0",
            "Data type mismatch",
            "null",
            "null",
            "null",
            "null"};
        testGetObjectNamed(testArray);
      }
    }



    /* Test java.lang.Double 7X with SQLXML X0 */ 
    public void Var370()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.lang.Double",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ) as C1, CAST(null as XML) as C2 from sysibm.sysdummy1", 
              "Data type mismatch",
              "null",};
           testGetObjectNamed(testArray);
        }
    }



    /* Test java.lang.Double 7X with primitive numbers X1 */ 
    public void Var371()
    {
        String[] testArray = {
            "java.lang.Double",
            "select cast(1 as SMALLINT) AS C1, cast(2 as INTEGER) AS C2, cast(3 as BIGINT) AS C3, cast(null as SMALLINT) AS C4, cast(null as INTEGER) AS C5, cast(null as BIGINT) AS C6 from sysibm.sysdummy1",
            "1.0",
            "2.0",
            "3.0", 
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.lang.Double 7X with primitive doubles X2 */ 
    public void Var372()
    {
        String[] testArray = {
            "java.lang.Double",
            "select cast(1.0 as REAL) AS C1, cast(2.0 as FLOAT) AS C2, cast(3.0 as DOUBLE) AS C3, cast(null as REAL) as C4, cast(null AS FLOAT) AS C5, cast(null as DOUBLE) AS C6 from sysibm.sysdummy1",
            "1.0",
            "2.0",
            "3.0", 
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.lang.Double 7X with decimal X3 */ 
    public void Var373()
    {
        String[] testArray = {
            "java.lang.Double",
            "select cast(1.0 as DECIMAL(10,1)) AS C1, cast(2.0 as NUMERIC(10,2)) AS C2, cast(3.0 as DECIMAL(10,4)) AS C3, cast(null as DECIMAL(10,1)) AS C4, cast(null as NUMERIC(10,2)) AS C5, cast(null as DECIMAL(10,4)) AS C6 from sysibm.sysdummy1",
            "1.0",
            "2.0",
            "3.0", 
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.lang.Double 7X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var374()
    {
        String[] testArray = {
            "java.lang.Double",
            "select cast('1' as CHAR(10)) AS C1, cast('2' as GRAPHIC(10) CCSID 1200) AS C2, cast(null as GRAPHIC(10)) AS C3, cast('1' as VARCHAR(10)) AS C4, cast('2' as VARGRAPHIC(10) CCSID 1200) AS C5, cast(null as VARGRAPHIC(10)) AS C6 from sysibm.sysdummy1",
            "1.0",
            "2.0",
            "null",
            "1.0",
            "2.0",
            "null"};
        testGetObjectNamed(testArray); 
    }
    


    /* Test java.lang.Double 7X with BINARY, VARBINARY  X5 */ 
    public void Var375()
    {
        String[] testArray = {
            "java.lang.Double",
            "select cast(X'01' AS BINARY(1)) AS C1, cast (X'02' AS VARBINARY(20)) AS C2,cast(null AS BINARY(1)) AS C3, cast (null AS VARBINARY(20)) AS C4 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.lang.Double 7X with date, time, timestamp  X6 */ 
    public void Var376()
    {
        String[] testArray = {
            "java.lang.Double",
            "select cast('1:02:03' as TIME) AS C1, cast ('2011-09-11' as DATE) AS C2, cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) AS C3 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.lang.Double 7X with clob, dbclob, blob  X7 */ 
    public void Var377()
    {
        String[] testArray = {
            "java.lang.Double",
            "select cast('1' as CLOB) AS C1, cast ('2' as DBCLOB(1M) CCSID 1200) AS C2, cast(X'03' AS BLOB) AS C3 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObjectNamed(testArray); 
    }
    

    /* Test java.lang.Double 7X with datalink  X8 */ 
    public void Var378()
    {
        String[] testArray = {
            "java.lang.Double",
            "select DLVALUE('file://tmp/x') AS C1 from sysibm.sysdummy1",
            "Data type mismatch"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.lang.Double 7X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var379()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.lang.Double",
            "select CAST(CAST(X'01' AS CHAR(2) FOR BIT DATA) AS ROWID) AS C1, CAST('1' as NCHAR(10)) AS C2, CAST('1' AS NVARCHAR(100)) AS C3, CAST('1' AS NCLOB(100K)) AS C4, CAST(null AS ROWID) AS C5, CAST(null as NCHAR(10)) AS C6, CAST(null AS NVARCHAR(100)) AS C7, CAST(null AS NCLOB(100K)) AS C8 from sysibm.sysdummy1",
            "Data type mismatch",
            "1.0",
            "1.0",
            "Data type mismatch",
            "null",
            "null",
            "null",
            "null"};
        testGetObjectNamed(testArray);
      }
    }



    /* Test java.math.BigDecimal 8X with SQLXML X0 */ 
    public void Var380()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.math.BigDecimal",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ) as C1, CAST(null as XML) as C2 from sysibm.sysdummy1", 
              "Data type mismatch",
              "null",};
           testGetObjectNamed(testArray);
        }
    }



    /* Test java.math.BigDecimal 8X with primitive numbers X1 */ 
    public void Var381()
    {
        String[] testArray = {
            "java.math.BigDecimal",
            "select cast(1 as SMALLINT) AS C1, cast(2 as INTEGER) AS C2, cast(3 as BIGINT) AS C3, cast(null as SMALLINT) AS C4, cast(null as INTEGER) AS C5, cast(null as BIGINT) AS C6 from sysibm.sysdummy1",
            "1",
            "2",
            "3", 
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.math.BigDecimal 8X with primitive doubles X2 */ 
    public void Var382()
    {
        String[] testArray = {
            "java.math.BigDecimal",
            "select cast(1.0 as REAL) AS C1, cast(2.0 as FLOAT) AS C2, cast(3.0 as DOUBLE) AS C3, cast(null as REAL) as C4, cast(null AS FLOAT) AS C5, cast(null as DOUBLE) AS C6 from sysibm.sysdummy1",
            "1.0",
            "2.0",
            "3.0", 
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.math.BigDecimal 8X with decimal X3 */ 
    public void Var383()
    {
        String[] testArray = {
            "java.math.BigDecimal",
            "select cast(1.0 as DECIMAL(10,1)) AS C1, cast(2.0 as NUMERIC(10,2)) AS C2, cast(3.0 as DECIMAL(10,4)) AS C3, cast(null as DECIMAL(10,1)) AS C4, cast(null as NUMERIC(10,2)) AS C5, cast(null as DECIMAL(10,4)) AS C6 from sysibm.sysdummy1",
            "1.0",
            "2.00",
            "3.0000", 
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.math.BigDecimal 8X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var384()
    {
        String[] testArray = {
            "java.math.BigDecimal",
            "select cast('1' as CHAR(10)) AS C1, cast('2' as GRAPHIC(10) CCSID 1200) AS C2, cast(null as GRAPHIC(10)) AS C3, cast('1' as VARCHAR(10)) AS C4, cast('2' as VARGRAPHIC(10) CCSID 1200) AS C5, cast(null as VARGRAPHIC(10)) AS C6 from sysibm.sysdummy1",
            "1",
            "2",
            "null",
            "1",
            "2",
            "null"};
        testGetObjectNamed(testArray); 
    }
    


    /* Test java.math.BigDecimal 8X with BINARY, VARBINARY  X5 */ 
    public void Var385()
    {
        String[] testArray = {
            "java.math.BigDecimal",
            "select cast(X'01' AS BINARY(1)) AS C1, cast (X'02' AS VARBINARY(20)) AS C2,cast(null AS BINARY(1)) AS C3, cast (null AS VARBINARY(20)) AS C4 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.math.BigDecimal 8X with date, time, timestamp  X6 */ 
    public void Var386()
    {
        String[] testArray = {
            "java.math.BigDecimal",
            "select cast('1:02:03' as TIME) AS C1, cast ('2011-09-11' as DATE) AS C2, cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) AS C3 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.math.BigDecimal 8X with clob, dbclob, blob  X7 */ 
    public void Var387()
    {
        String[] testArray = {
            "java.math.BigDecimal",
            "select cast('1' as CLOB) AS C1, cast ('2' as DBCLOB(1M) CCSID 1200) AS C2, cast(X'03' AS BLOB) AS C3 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObjectNamed(testArray); 
    }
    

    /* Test java.math.BigDecimal 8X with datalink  X8 */ 
    public void Var388()
    {
        String[] testArray = {
            "java.math.BigDecimal",
            "select DLVALUE('file://tmp/x') AS C1 from sysibm.sysdummy1",
            "Data type mismatch"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.math.BigDecimal 8X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var389()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.math.BigDecimal",
            "select CAST(CAST(X'01' AS CHAR(2) FOR BIT DATA) AS ROWID) AS C1, CAST('1' as NCHAR(10)) AS C2, CAST('1' AS NVARCHAR(100)) AS C3, CAST('1' AS NCLOB(100K)) AS C4, CAST(null AS ROWID) AS C5, CAST(null as NCHAR(10)) AS C6, CAST(null AS NVARCHAR(100)) AS C7, CAST(null AS NCLOB(100K)) AS C8 from sysibm.sysdummy1",
            "Data type mismatch",
            "1",
            "1",
            "Data type mismatch",
            "null",
            "null",
            "null",
            "null"};
        testGetObjectNamed(testArray);
      }
    }




    /* Test java.lang.Boolean 9X with SQLXML X0 */ 
    public void Var390()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.lang.Boolean",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ) as C1, CAST(null as XML) as C2 from sysibm.sysdummy1", 
              "Data type mismatch",
              "null",};
           testGetObjectNamed(testArray);
        }
    }



    /* Test java.lang.Boolean 9X with primitive numbers X1 */ 
    public void Var391()
    {
        String[] testArray = {
            "java.lang.Boolean",
            "select cast(1 as SMALLINT) AS C1, cast(2 as INTEGER) AS C2, cast(3 as BIGINT) AS C3, cast(0 as SMALLINT) AS C4, cast(0 as INTEGER) AS C5, cast(0 as BIGINT)AS C6, cast(null as SMALLINT) AS C7, cast(null as INTEGER) AS C8, cast(null as BIGINT) AS C9 from sysibm.sysdummy1",
            "true",
            "true",
            "true",
            "false",
            "false",
            "false",
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.lang.Boolean 9X with primitive doubles X2 */ 
    public void Var392()
    {
        String[] testArray = {
            "java.lang.Boolean",
            "select cast(1.0 as REAL) AS C1, cast(2.0 as FLOAT) AS C2, cast(3.0 as DOUBLE) AS C3, cast(0.0 as REAL) AS C4, cast(0.0 as FLOAT) AS C5, cast(0.0 as DOUBLE) AS C6, cast(null as REAL) AS C7, cast(null as FLOAT) AS C8, cast(null as DOUBLE) AS C9 from sysibm.sysdummy1",
            "true",
            "true",
            "true", 
            "false",
            "false",
            "false",
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.lang.Boolean 9X with decimal X3 */ 
    public void Var393()
    {
        String[] testArray = {
            "java.lang.Boolean",
            "select cast(1.0 as DECIMAL(10,1)) AS C1, cast(2.0 as NUMERIC(10,2)) AS C2, cast(3.0 as DECIMAL(10,4)) AS C3, cast(0.0 as DECIMAL(10,1)) AS C4, cast(0.0 as NUMERIC(10,2)) AS C5, cast(0.0 as DECIMAL(10,4)) AS C6, cast(null as DECIMAL(10,1)) AS C7, cast(null as NUMERIC(10,2)) AS C8, cast(null as DECIMAL(10,4)) AS C9 from sysibm.sysdummy1",
            "true",
            "true",
            "true", 
            "false",
            "false",
            "false",
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.lang.Boolean 9X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var394()
    {
        String[] testArray = {
            "java.lang.Boolean",
            "select cast('1' as CHAR(10)) AS C1, cast('0' as CHAR(10)) AS C2, cast('2' as GRAPHIC(10) CCSID 1200) AS C3, cast('0' as GRAPHIC(10) CCSID 1200) AS C4, cast(null as GRAPHIC(10)) AS C5, cast('1' as VARCHAR(10)) AS C6, cast('2' as VARGRAPHIC(10) CCSID 1200)  AS C7, cast('0' as VARCHAR(10))  AS C8, cast('0' as VARGRAPHIC(10) CCSID 1200) AS C9, cast(null as VARGRAPHIC(10))  AS C10 from sysibm.sysdummy1",
            "true",
            "false",
            "true",
            "false",
            "null",
            "true",
            "true",
            "false",
            "false",
            "null"};
        testGetObjectNamed(testArray); 
    }
    


    /* Test java.lang.Boolean 9X with BINARY, VARBINARY  X5 */ 
    public void Var395()
    {
        String[] testArray = {
            "java.lang.Boolean",
            "select cast(X'01' AS BINARY(1)) AS C1, cast (X'02' AS VARBINARY(20)) AS C2,cast(null AS BINARY(1)) AS C3, cast (null AS VARBINARY(20)) AS C4 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.lang.Boolean 9X with date, time, timestamp  X6 */ 
    public void Var396()
    {
        String[] testArray = {
            "java.lang.Boolean",
            "select cast('1:02:03' as TIME) AS C1, cast ('2011-09-11' as DATE) AS C2, cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) AS C3 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.lang.Boolean 9X with clob, dbclob, blob  X7 */ 
    public void Var397()
    {
        String[] testArray = {
            "java.lang.Boolean",
            "select cast('1' as CLOB) AS C1, cast ('2' as DBCLOB(1M) CCSID 1200) AS C2, cast(X'03' AS BLOB) AS C3 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObjectNamed(testArray); 
    }
    

    /* Test java.lang.Boolean 9X with datalink  X8 */ 
    public void Var398()
    {
        String[] testArray = {
            "java.lang.Boolean",
            "select DLVALUE('file://tmp/x') AS C1 from sysibm.sysdummy1",
            "Data type mismatch"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.lang.Boolean 9X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var399()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.lang.Boolean",
            "select CAST(CAST(X'01' AS CHAR(2) FOR BIT DATA) AS ROWID) AS C1, CAST('1' as NCHAR(10)) AS C2, CAST('1' AS NVARCHAR(100)) AS C3, CAST('0' as NCHAR(10)) AS C4, CAST('0' AS NVARCHAR(100)) AS C5, CAST('1' AS NCLOB(100K)) AS C6, CAST(null AS ROWID) AS C7, CAST(null as NCHAR(10)) AS C8, CAST(null AS NVARCHAR(100)) AS C9, CAST(null AS NCLOB(100K)) AS C10 from sysibm.sysdummy1",
            "Data type mismatch",
            "true",
            "true",
            "false",
            "false",
            "Data type mismatch",
            "null",
            "null",
            "null",
            "null"};
        testGetObjectNamed(testArray);
      }
    }




    /* Test [B 10X with SQLXML X0 */ 
    public void Var400()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "[B",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ) as C1, CAST(null as XML) as C2 from sysibm.sysdummy1", 
              "BYTEARRAY=4C6FA7949340A58599A28996957E7FF14BF07F4085958396848995877E7FC9C2D4F0F3F77F6F6E4C886E88859393964C61886E",
              "null",};
           testGetObjectNamed(testArray);
        }
    }



    /* Test [B 10X with primitive numbers X1 */ 
    public void Var401()
    {
        String[] testArray = {
            "[B",
            "select cast(1 as SMALLINT) AS C1, cast(2 as INTEGER) AS C2, cast(3 as BIGINT) AS C3, cast(null as SMALLINT) AS C4, cast(null as INTEGER) AS C5, cast(null as BIGINT) AS C6 from sysibm.sysdummy1",
            "BYTEARRAY=0001",
            "BYTEARRAY=00000002",
            "BYTEARRAY=0000000000000003",
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test [B 10X with primitive doubles X2 */ 
    public void Var402()
    {
        String[] testArray = {
            "[B",
            "select cast(1.0 as REAL) AS C1, cast(2.0 as FLOAT) AS C2, cast(3.0 as DOUBLE) AS C3, cast(null as REAL) as C4, cast(null AS FLOAT) AS C5, cast(null as DOUBLE) AS C6 from sysibm.sysdummy1",
            "BYTEARRAY=3F800000",
            "BYTEARRAY=4000000000000000", 
            "BYTEARRAY=4008000000000000",
            "null",
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test [B 10X with decimal X3 */ 
    public void Var403()
    {
        String[] testArray = {
            "[B",
            "select cast(1.0 as DECIMAL(10,1)) AS C1, cast(2.0 as NUMERIC(10,2)) AS C2, cast(3.0 as DECIMAL(10,4)) AS C3, cast(null as DECIMAL(10,1)) AS C4, cast(null as NUMERIC(10,2)) AS C5, cast(null as DECIMAL(10,4)) AS C6 from sysibm.sysdummy1",
            "BYTEARRAY=00000000010F",
            "BYTEARRAY=F0F0F0F0F0F0F0F2F0F0",
            "BYTEARRAY=00000030000F", 
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test [B 10X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var404()
    {
        String[] testArray = {
            "[B",
            "select cast('1' as CHAR(10)) AS C1, cast('2' as GRAPHIC(10) CCSID 1200) AS C2, cast(null as GRAPHIC(10)) AS C3, cast('1' as VARCHAR(10)) AS C4, cast('2' as VARGRAPHIC(10) CCSID 1200) AS C5, cast(null as VARGRAPHIC(10)) AS C6 from sysibm.sysdummy1",
            "BYTEARRAY=F1404040404040404040",
            "BYTEARRAY=0032002000200020002000200020002000200020",
            "null",
            "BYTEARRAY=F1",
            "BYTEARRAY=0032",
            "null"};




        testGetObjectNamed(testArray); 
    }
    


    /* Test [B 10X with BINARY, VARBINARY  X5 */ 
    public void Var405()
    {
        String[] testArray = {
            "[B",
            "select cast(X'01' AS BINARY(1)) AS C1, cast (X'02' AS VARBINARY(20)) AS C2,cast(null AS BINARY(1)) AS C3, cast (null AS VARBINARY(20)) AS C4 from sysibm.sysdummy1",
            "BYTEARRAY=01",
            "BYTEARRAY=02",
            "null",
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test [B 10X with date, time, timestamp  X6 */ 
    public void Var406()
    {
        String[] testArray = {
            "[B",
            "select cast('1:02:03' as TIME) AS C1, cast ('2011-09-11' as DATE) AS C2, cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) AS C3 from sysibm.sysdummy1",
            "BYTEARRAY=F0F17AF0F27AF0F3",
            "BYTEARRAY=F2F0F1F160F0F960F1F1",
            "BYTEARRAY=F2F0F1F160F0F960F1F160F0F14BF0F24BF0F34BF1F2F3F4F5F6"};


        testGetObjectNamed(testArray); 
    }
    
    /* Test [B 10X with clob, dbclob, blob  X7 */ 
    public void Var407()
    {
        String[] testArray = {
            "[B",
            "select cast('1' as CLOB) AS C1, cast ('2' as DBCLOB(1M) CCSID 1200) AS C2, cast(X'03' AS BLOB) AS C3 from sysibm.sysdummy1",
            "BYTEARRAY=F1",
            "BYTEARRAY=0032",
            "BYTEARRAY=03"};


	    // The values for CLOB and DBCLOB do not make sense for toolbox, so skip them
	    String[] testArrayTB  = {
            "[B",
            "select cast(X'03' AS BLOB) AS C1 from sysibm.sysdummy1",
            "BYTEARRAY=03"
	    };

	    if (isToolboxDriver()) {
		testArray = testArrayTB; 
	    } 

        testGetObjectNamed(testArray); 
    }
    

    /* Test [B 10X with datalink  X8 */ 
    public void Var408()
    {
        String[] testArray = {
            "[B",
            "select DLVALUE('file://tmp/x') AS C1 from sysibm.sysdummy1",
            "BYTEARRAY=C6C9D3C57A6161E3D4D761A7"};
        testGetObjectNamed(testArray); 
    }

    /* Test [B 10X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var409()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "[B",
            "select CAST(CAST(X'01' AS CHAR(2) FOR BIT DATA) AS ROWID) AS C1, CAST('1' as NCHAR(10)) AS C2, CAST('1' AS NVARCHAR(100)) AS C3, CAST('1' AS NCLOB(100K)) AS C4, CAST(null AS ROWID) AS C5, CAST(null as NCHAR(10)) AS C6, CAST(null AS NVARCHAR(100)) AS C7, CAST(null AS NCLOB(100K)) AS C8 from sysibm.sysdummy1",
            "BYTEARRAY=0140",
            "BYTEARRAY=0031002000200020002000200020002000200020",
            "BYTEARRAY=0031",
            "BYTEARRAY=0031",
            "null",
            "null",
            "null",
            "null"};

    String[] testArrayTB = {
            "[B",
            "select CAST(CAST(X'01' AS CHAR(2) FOR BIT DATA) AS ROWID) AS C1, "+
                  " CAST('1' as NCHAR(10)) AS C2, "+
                  " CAST('1' AS NVARCHAR(100)) AS C3, "+
                  " CAST(null AS ROWID) AS C4, "+
                  " CAST(null as NCHAR(10)) AS C5, "+
                  " CAST(null AS NVARCHAR(100)) AS C6, "+
                  " CAST(null AS NCLOB(100K)) AS C7 from sysibm.sysdummy1",
            "BYTEARRAY=0140",
            "BYTEARRAY=0031002000200020002000200020002000200020",
            "BYTEARRAY=0031",
            "null",
            "null",
            "null",
            "null"};

	    // Getting bytes from CLOB does not make sense for toolbox 

	    if (isToolboxDriver()) {
		testArray = testArrayTB; 
	    } 

        testGetObjectNamed(testArray);
      }
    }




    /* Test java.sql.Date 11X with SQLXML X0 */ 
    public void Var410()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.sql.Date",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ) as C1, CAST(null as XML) as C2 from sysibm.sysdummy1", 
              "Data type mismatch",
              "null",};
           testGetObjectNamed(testArray);
        }
    }



    /* Test java.sql.Date 11X with primitive numbers X1 */ 
    public void Var411()
    {
        String[] testArray = {
            "java.sql.Date",
            "select cast(1 as SMALLINT) AS C1, cast(2 as INTEGER) AS C2, cast(3 as BIGINT) AS C3, cast(null as SMALLINT) AS C4, cast(null as INTEGER) AS C5, cast(null as BIGINT) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.sql.Date 11X with primitive doubles X2 */ 
    public void Var412()
    {
        String[] testArray = {
            "java.sql.Date",
            "select cast(1.0 as REAL) AS C1, cast(2.0 as FLOAT) AS C2, cast(3.0 as DOUBLE) AS C3, cast(null as REAL) as C4, cast(null AS FLOAT) AS C5, cast(null as DOUBLE) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.sql.Date 11X with decimal X3 */ 
    public void Var413()
    {
        String[] testArray = {
            "java.sql.Date",
            "select cast(1.0 as DECIMAL(10,1)) AS C1, cast(2.0 as NUMERIC(10,2)) AS C2, cast(3.0 as DECIMAL(10,4)) AS C3, cast(null as DECIMAL(10,1)) AS C4, cast(null as NUMERIC(10,2)) AS C5, cast(null as DECIMAL(10,4)) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.sql.Date 11X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var414()
    {
        String[] testArray = {
            "java.sql.Date",
            "select cast('2011-09-01' as CHAR(10)) AS C1, cast('2011-09-02' as GRAPHIC(10) CCSID 1200) AS C2, cast(null as GRAPHIC(10))  AS C3, cast('2011-09-03' as VARCHAR(10)) AS C4, cast('2011-09-04' as VARGRAPHIC(10) CCSID 1200) AS C5, cast(null as VARGRAPHIC(10))  AS C6 from sysibm.sysdummy1",
            "2011-09-01",
            "2011-09-02",
            "null",
            "2011-09-03",
            "2011-09-04",
            "null"};
        testGetObjectNamed(testArray); 
    }
    


    /* Test java.sql.Date 11X with BINARY, VARBINARY  X5 */ 
    public void Var415()
    {
        String[] testArray = {
            "java.sql.Date",
            "select cast(X'01' AS BINARY(1)) AS C1, cast (X'02' AS VARBINARY(20)) AS C2,cast(null AS BINARY(1)) AS C3, cast (null AS VARBINARY(20)) AS C4 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.sql.Date 11X with date, time, timestamp  X6 */ 
    public void Var416()
    {
        String[] testArray = {
            "java.sql.Date",
            "select cast('1:02:03' as TIME) AS C1, cast ('2011-09-11' as DATE) AS C2, cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) AS C3 from sysibm.sysdummy1",
            "Data type mismatch",
            "2011-09-11",
            "2011-09-11"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.sql.Date 11X with clob, dbclob, blob  X7 */ 
    public void Var417()
    {
        String[] testArray = {
            "java.sql.Date",
            "select cast('2011-09-11' as CLOB) AS C1, cast ('2011-09-12' as DBCLOB(1M) CCSID 1200) AS C2, cast(X'03' AS BLOB)  AS C3 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObjectNamed(testArray); 
    }
    

    /* Test java.sql.Date 11X with datalink  X8 */ 
    public void Var418()
    {
        String[] testArray = {
            "java.sql.Date",
            "select DLVALUE('file://tmp/x') AS C1 from sysibm.sysdummy1",
            "Data type mismatch"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.sql.Date 11X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var419()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.sql.Date",
            "select CAST(CAST(X'01' AS CHAR(2) FOR BIT DATA) AS ROWID) AS C1, CAST('2011-09-11' as NCHAR(10)) AS C2, CAST('2011-09-12' AS NVARCHAR(100)) AS C3, CAST('1' AS NCLOB(100K)) AS C4, CAST(null AS ROWID) AS C5, CAST(null as NCHAR(10)) AS C6, CAST(null AS NVARCHAR(100)) AS C7, CAST(null AS NCLOB(100K)) AS C8 from sysibm.sysdummy1",
            "Data type mismatch",
            "2011-09-11",
            "2011-09-12",
            "Data type mismatch",
            "null",
            "null",
            "null",
            "null"};
        testGetObjectNamed(testArray);
      }
    }

    



    /* Test java.sql.Time 12X with SQLXML X0 */ 
    public void Var420()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.sql.Time",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ) as C1, CAST(null as XML) as C2 from sysibm.sysdummy1", 
              "Data type mismatch",
              "null",};
           testGetObjectNamed(testArray);
        }
    }



    /* Test java.sql.Time 12X with primitive numbers X1 */ 
    public void Var421()
    {
        String[] testArray = {
            "java.sql.Time",
            "select cast(1 as SMALLINT) AS C1, cast(2 as INTEGER) AS C2, cast(3 as BIGINT) AS C3, cast(null as SMALLINT) AS C4, cast(null as INTEGER) AS C5, cast(null as BIGINT) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.sql.Time 12X with primitive doubles X2 */ 
    public void Var422()
    {
        String[] testArray = {
            "java.sql.Time",
            "select cast(1.0 as REAL) AS C1, cast(2.0 as FLOAT) AS C2, cast(3.0 as DOUBLE) AS C3, cast(null as REAL) as C4, cast(null AS FLOAT) AS C5, cast(null as DOUBLE) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.sql.Time 12X with decimal X3 */ 
    public void Var423()
    {
        String[] testArray = {
            "java.sql.Time",
            "select cast(1.0 as DECIMAL(10,1)) AS C1, cast(2.0 as NUMERIC(10,2)) AS C2, cast(3.0 as DECIMAL(10,4)) AS C3, cast(null as DECIMAL(10,1)) AS C4, cast(null as NUMERIC(10,2)) AS C5, cast(null as DECIMAL(10,4)) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.sql.Time 12X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var424()
    {
        String[] testArray = {
            "java.sql.Time",
            "select cast('11:22:01' as CHAR(10)) AS C1, cast('11:22:02' as GRAPHIC(10) CCSID 1200) AS C2, cast(null as GRAPHIC(10)) AS C3, cast('11:22:03' as VARCHAR(10)) AS C4, cast('11:22:04' as VARGRAPHIC(10) CCSID 1200) AS C5, cast(null as VARGRAPHIC(10))  AS C6 from sysibm.sysdummy1",
            "11:22:01",
            "11:22:02",
            "null",
            "11:22:03",
            "11:22:04",
            "null"};
        testGetObjectNamed(testArray); 
    }
    


    /* Test java.sql.Time 12X with BINARY, VARBINARY  X5 */ 
    public void Var425()
    {
        String[] testArray = {
            "java.sql.Time",
            "select cast(X'01' AS BINARY(1)) AS C1, cast (X'02' AS VARBINARY(20)) AS C2,cast(null AS BINARY(1)) AS C3, cast (null AS VARBINARY(20)) AS C4 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.sql.Time 12X with date, time, timestamp  X6 */ 
    public void Var426()
    {
        String[] testArray = {
            "java.sql.Time",
            "select cast('1:02:03' as TIME) AS C1, cast ('2011-09-11' as DATE) AS C2, cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) AS C3 from sysibm.sysdummy1",
            "01:02:03",
            "Data type mismatch",
            "01:02:03"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.sql.Time 12X with clob, dbclob, blob  X7 */ 
    public void Var427()
    {
        String[] testArray = {
            "java.sql.Time",
            "select cast('11:22:11' as CLOB)  AS C1, cast ('11:22:12' as DBCLOB(1M) CCSID 1200) AS C2, cast(X'03' AS BLOB)  AS C3 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObjectNamed(testArray); 
    }
    

    /* Test java.sql.Time 12X with datalink  X8 */ 
    public void Var428()
    {
        String[] testArray = {
            "java.sql.Time",
            "select DLVALUE('file://tmp/x') AS C1 from sysibm.sysdummy1",
            "Data type mismatch"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.sql.Time 12X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var429()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.sql.Time",
            "select CAST(CAST(X'01' AS CHAR(2) FOR BIT DATA) AS ROWID)  AS C1, CAST('11:22:11' as NCHAR(10)) AS C2, CAST('11:22:12' AS NVARCHAR(100)) AS C3, CAST('1' AS NCLOB(100K)) AS C4, CAST(null AS ROWID) AS C5, CAST(null as NCHAR(10)) AS C6, CAST(null AS NVARCHAR(100)) AS C7, CAST(null AS NCLOB(100K)) AS C8 from sysibm.sysdummy1",
            "Data type mismatch",
            "11:22:11",
            "11:22:12",
            "Data type mismatch",
            "null",
            "null",
            "null",
            "null"};
        testGetObjectNamed(testArray);
      }
    }




    /* Test java.sql.Timestamp 13X with SQLXML X0 */ 
    public void Var430()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.sql.Timestamp",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ) as C1, CAST(null as XML) as C2 from sysibm.sysdummy1", 
              "Data type mismatch",
              "null",};
           testGetObjectNamed(testArray);
        }
    }



    /* Test java.sql.Timestamp 13X with primitive numbers X1 */ 
    public void Var431()
    {
        String[] testArray = {
            "java.sql.Timestamp",
            "select cast(1 as SMALLINT) AS C1, cast(2 as INTEGER) AS C2, cast(3 as BIGINT) AS C3, cast(null as SMALLINT) AS C4, cast(null as INTEGER) AS C5, cast(null as BIGINT) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.sql.Timestamp 13X with primitive doubles X2 */ 
    public void Var432()
    {
        String[] testArray = {
            "java.sql.Timestamp",
            "select cast(1.0 as REAL) AS C1, cast(2.0 as FLOAT) AS C2, cast(3.0 as DOUBLE) AS C3, cast(null as REAL) as C4, cast(null AS FLOAT) AS C5, cast(null as DOUBLE) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.sql.Timestamp 13X with decimal X3 */ 
    public void Var433()
    {
        String[] testArray = {
            "java.sql.Timestamp",
            "select cast(1.0 as DECIMAL(10,1)) AS C1, cast(2.0 as NUMERIC(10,2)) AS C2, cast(3.0 as DECIMAL(10,4)) AS C3, cast(null as DECIMAL(10,1)) AS C4, cast(null as NUMERIC(10,2)) AS C5, cast(null as DECIMAL(10,4)) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.sql.Timestamp 13X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var434()
    {
        String[] testArray = {
            "java.sql.Timestamp",
            "select cast('2011-09-01 11:22:01.012345' as CHAR(26)) AS C1, cast('2011-09-01 11:22:02.222222' as GRAPHIC(26) CCSID 1200) AS C2, cast(null as GRAPHIC(26)) AS C3, cast('2011-09-01 11:22:03.333333' as VARCHAR(26)) AS C4, cast('2011-09-01 11:22:04.444444' as VARGRAPHIC(26) CCSID 1200) AS C5, cast(null as VARGRAPHIC(10)) AS C6 from sysibm.sysdummy1",
            "2011-09-01 11:22:01.012345",
            "2011-09-01 11:22:02.222222",
            "null",
            "2011-09-01 11:22:03.333333",
            "2011-09-01 11:22:04.444444",
            "null"};
        testGetObjectNamed(testArray); 
    }
    


    /* Test java.sql.Timestamp 13X with BINARY, VARBINARY  X5 */ 
    public void Var435()
    {
        String[] testArray = {
            "java.sql.Timestamp",
            "select cast(X'01' AS BINARY(1)) AS C1, cast (X'02' AS VARBINARY(20)) AS C2,cast(null AS BINARY(1)) AS C3, cast (null AS VARBINARY(20)) AS C4 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.sql.Timestamp 13X with date, Timestamp, Timestampstamp  X6 */ 
    public void Var436()
    {
        String[] testArray = {
            "java.sql.Timestamp",
            "select cast('1:02:03' as TIME) AS C1, cast ('2011-09-11' as DATE) AS C2, cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) AS C3 from sysibm.sysdummy1",
            "1970-01-01 01:02:03.0",
            "2011-09-11 00:00:00.0",
            "2011-09-11 01:02:03.123456"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.sql.Timestamp 13X with clob, dbclob, blob  X7 */ 
    public void Var437()
    {
        String[] testArray = {
            "java.sql.Timestamp",
            "select cast('2011-09-01 11:22:11' as CLOB) AS C1, cast ('2011-09-01 11:22:12' as DBCLOB(1M) CCSID 1200) AS C2, cast(X'03' AS BLOB)  AS C3 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObjectNamed(testArray); 
    }
    

    /* Test java.sql.Timestamp 13X with datalink  X8 */ 
    public void Var438()
    {
        String[] testArray = {
            "java.sql.Timestamp",
            "select DLVALUE('file://tmp/x') AS C1 from sysibm.sysdummy1",
            "Data type mismatch"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.sql.Timestamp 13X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var439()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.sql.Timestamp",
            "select CAST(CAST(X'01' AS CHAR(2) FOR BIT DATA) AS ROWID)  AS C1, CAST('2011-09-01 11:22:11.111111' as NCHAR(26)) AS C2, CAST('2011-09-01 11:22:12.222222' AS NVARCHAR(100)) AS C3, CAST('1' AS NCLOB(100K)) AS C4, CAST(null AS ROWID) AS C5, CAST(null as NCHAR(26)) AS C6, CAST(null AS NVARCHAR(100)) AS C7, CAST(null AS NCLOB(100K)) AS C8 from sysibm.sysdummy1",
            "Data type mismatch",
            "2011-09-01 11:22:11.111111",
            "2011-09-01 11:22:12.222222",
            "Data type mismatch",
            "null",
            "null",
            "null",
            "null"};
        testGetObjectNamed(testArray);
      }
    }


    /* Test java.io.InputStream 14X with SQLXML X0 */ 
    public void Var440()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.io.InputStream",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ) as C1, CAST(null as XML) as C2 from sysibm.sysdummy1", 
              "INPUTSTREAM=4C6FA7949340A58599A28996957E7FF14BF07F4085958396848995877E7FC9C2D4F0F3F77F6F6E4C886E88859393964C61886E",
              "null"};


           String[] testArrayNative = {
              "java.io.InputStream",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ) as C1, CAST(null as XML) as C2 from sysibm.sysdummy1", 
              "INPUTSTREAM=3C683E68656C6C6F3C2F683E",
              "null"};

	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

           testGetObjectNamed(testArray);
        }
    }



    /* Test java.io.InputStream 14X with primitive numbers X1 */ 
    public void Var441()
    {
        String[] testArray = {
            "java.io.InputStream",
            "select cast(1 as SMALLINT) AS C1, cast(2 as INTEGER) AS C2, cast(3 as BIGINT) AS C3, cast(null as SMALLINT) AS C4, cast(null as INTEGER) AS C5, cast(null as BIGINT) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "null",
            "null",
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.io.InputStream 14X with primitive doubles X2 */ 
    public void Var442()
    {
        String[] testArray = {
            "java.io.InputStream",
            "select cast(1.0 as REAL) AS C1, cast(2.0 as FLOAT) AS C2, cast(3.0 as DOUBLE) AS C3, cast(null as REAL) as C4, cast(null AS FLOAT) AS C5, cast(null as DOUBLE) AS C6  from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null",
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.io.InputStream 14X with decimal X3 */ 
    public void Var443()
    {
        String[] testArray = {
            "java.io.InputStream",
            "select cast(1.0 as DECIMAL(10,1)) AS C1, cast(2.0 as NUMERIC(10,2)) AS C2, cast(3.0 as DECIMAL(10,4)) AS C3, cast(null as DECIMAL(10,1)) AS C4, cast(null as NUMERIC(10,2)) AS C5, cast(null as DECIMAL(10,4)) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null",
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.io.InputStream 14X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var444()
    {
        String[] testArray = {
            "java.io.InputStream",
            "select cast('A2a4a6a8a0' as CHAR(10))  AS C1, cast('A2a4a6a8a0' as GRAPHIC(10) CCSID 1200) AS C2, " +
            "cast(null as GRAPHIC(10)) AS C3, " +
            "cast('AABBCC' as VARCHAR(10)) AS C4, cast('ABCD' as VARGRAPHIC(10) CCSID 1200) AS C5, cast(null as VARGRAPHIC(10))  AS C6 from sysibm.sysdummy1",
            "INPUTSTREAM=A2A4A6A8A0",
            "INPUTSTREAM=A2A4A6A8A0",
            "null",
            "INPUTSTREAM=AABBCC",
            "INPUTSTREAM=ABCD",
            "null"};


        String[] testArrayNative = {
            "java.io.InputStream",
            "select cast('A2a4a6a8a0' as CHAR(10))  AS C1, cast('A2a4a6a8a0' as GRAPHIC(10) CCSID 1200) AS C2, " +
            "cast(null as GRAPHIC(10)) AS C3, " +
            "cast('AABBCC' as VARCHAR(10)) AS C4, cast('ABCD' as VARGRAPHIC(10) CCSID 1200) AS C5, cast(null as VARGRAPHIC(10))  AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "Data type mismatch",
            "Data type mismatch",
            "null"};


	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

        testGetObjectNamed(testArray); 
    }
    


    /* Test java.io.InputStream 14X with BINARY, VARBINARY  X5 */ 
    public void Var445()
    {
        String[] testArray = {
            "java.io.InputStream",
            "select cast(X'313233' AS BINARY(10)) AS C1, cast (X'313233' AS VARBINARY(20)) AS C2,cast(null AS BINARY(10)) AS C3, cast (null AS VARBINARY(20)) AS C4 from sysibm.sysdummy1",
            "INPUTSTREAM=31323300000000000000",
            "INPUTSTREAM=313233", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.io.InputStream 14X with date, time, timestamp  X6 */ 
    public void Var446()
    {
        String[] testArray = {
            "java.io.InputStream",
            "select cast('1:02:03' as TIME) AS C1, cast ('2011-09-11' as DATE) AS C2, cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) AS C3, cast(null as TIME) AS C4, cast (null as DATE) AS C5, cast(null AS TIMESTAMP) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.io.InputStream 14X with clob, dbclob, blob  X7 */ 
    public void Var447()
    {
        String[] testArray = {
            "java.io.InputStream",
            "select cast('ABCD' as CLOB) AS C1, cast ('ABCD' as DBCLOB(1M) CCSID 1200) AS C2, cast(X'313233' AS BLOB) AS C3, cast(null as CLOB) AS C4, cast (null as DBCLOB(1M) CCSID 1200) AS C5, cast(null AS BLOB) AS C6 from sysibm.sysdummy1",
            "INPUTSTREAM=ABCD",
            "INPUTSTREAM=ABCD",
            "INPUTSTREAM=313233", 
            "null", 
            "null", 
            "null"};

        String[] testArrayNative = {
            "java.io.InputStream",
            "select cast('ABCD' as CLOB) AS C1, cast ('ABCD' as DBCLOB(1M) CCSID 1200) AS C2, cast(X'313233' AS BLOB) AS C3, cast(null as CLOB) AS C4, cast (null as DBCLOB(1M) CCSID 1200) AS C5, cast(null AS BLOB) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "INPUTSTREAM=313233", 
            "null", 
            "null", 
            "null"};


	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

        testGetObjectNamed(testArray); 
    }
    

    /* Test java.io.InputStream 14X with datalink  X8 */ 
    public void Var448()
    {
        String[] testArray = {
            "java.io.InputStream",
            "select DLVALUE('file://tmp/x') AS C1, CAST(NULL AS DATALINK) AS C2 from sysibm.sysdummy1",
            "Data type mismatch", 
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.io.InputStream 14X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var449()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.io.InputStream",
            "select CAST(CAST(X'1122' AS CHAR(2) FOR BIT DATA) AS ROWID) AS C1, " +
            "CAST('1122334455' as NCHAR(10)) AS C2, CAST('ABCE' AS NVARCHAR(100)) AS C3, CAST('ABCF' AS NCLOB(100K)) AS C4, " +
            "CAST(null AS ROWID) AS C5, CAST(null as NCHAR(10)) AS C6, CAST(null AS NVARCHAR(100)) AS C7, CAST(null AS NCLOB(100K)) AS C8 from sysibm.sysdummy1",
            "INPUTSTREAM=1122",
            "INPUTSTREAM=1122334455",
            "INPUTSTREAM=ABCE",
            "INPUTSTREAM=ABCF", 
            "null", 
            "null", 
            "null",
            "null"};

	    String[] testArrayNative = {
		"java.io.InputStream",
		"select CAST(CAST(X'1122' AS CHAR(2) FOR BIT DATA) AS ROWID) AS C1, " +
		"CAST('1122334455' as NCHAR(10)) AS C2, CAST('ABCE' AS NVARCHAR(100)) AS C3, CAST('ABCF' AS NCLOB(100K)) AS C4, " +
		"CAST(null AS ROWID) AS C5, CAST(null as NCHAR(10)) AS C6, CAST(null AS NVARCHAR(100)) AS C7, CAST(null AS NCLOB(100K)) AS C8 from sysibm.sysdummy1",
		"INPUTSTREAM=1122",
		"Data type mismatch",
		"Data type mismatch",
		"Data type mismatch", 
		"null", 
		"null", 
		"null",
		"null"};


	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

        testGetObjectNamed(testArray);
      }
    }


    /* Test java.io.Reader 15X with SQLXML X0 */ 
    public void Var450()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.io.Reader",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ) as C1, CAST(null as XML) as C2 from sysibm.sysdummy1", 
              "READER=<h>hello</h>",
              "null"};
           testGetObjectNamed(testArray);
        }
    }



    /* Test java.io.Reader 15X with primitive numbers X1 */ 
    public void Var451()
    {
        String[] testArray = {
            "java.io.Reader",
            "select cast(1 as SMALLINT) AS C1, cast(2 as INTEGER) AS C2, cast(3 as BIGINT) AS C3, cast(null as SMALLINT) AS C4, cast(null as INTEGER) AS C5, cast(null as BIGINT) AS C6 from sysibm.sysdummy1",
            "READER=1",
            "READER=2",
            "READER=3", 
            "null",
            "null",
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.io.Reader 15X with primitive doubles X2 */ 
    public void Var452()
    {
        String[] testArray = {
            "java.io.Reader",
            "select cast(1.0 as REAL) AS C1, cast(2.0 as FLOAT) AS C2, cast(3.0 as DOUBLE) AS C3, cast(null as REAL) as C4, cast(null AS FLOAT) AS C5, cast(null as DOUBLE) AS C6  from sysibm.sysdummy1",
            "READER=1.0",
            "READER=2.0",
            "READER=3.0",
            "null",
            "null",
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.io.Reader 15X with decimal X3 */ 
    public void Var453()
    {
        String[] testArray = {
            "java.io.Reader",
            "select cast(1.0 as DECIMAL(10,1)) AS C1, cast(2.0 as NUMERIC(10,2)) AS C2, cast(3.0 as DECIMAL(10,4)) AS C3, cast(null as DECIMAL(10,1)) AS C4, cast(null as NUMERIC(10,2)) AS C5, cast(null as DECIMAL(10,4)) AS C6 from sysibm.sysdummy1",
            "READER=1.0",
            "READER=2.00",
            "READER=3.0000",
            "null",
            "null",
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.io.Reader 15X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var454()
    {
        String[] testArray = {
            "java.io.Reader",
            "select cast('ABC' as CHAR(10)) AS C1, cast('ABC' as GRAPHIC(10) CCSID 1200) AS C2, cast(null as GRAPHIC(10)) AS C3, cast('ABC' as VARCHAR(10)) AS C4, cast('ABC' as VARGRAPHIC(10) CCSID 1200) AS C5, cast(null as VARGRAPHIC(10)) AS C6 from sysibm.sysdummy1",
            "READER=ABC       ",
            "READER=ABC       ",
            "null",
            "READER=ABC",
            "READER=ABC",
            "null"};
        testGetObjectNamed(testArray); 
    }
    


    /* Test java.io.Reader 15X with BINARY, VARBINARY  X5 */ 
    public void Var455()
    {
        String[] testArray = {
            "java.io.Reader",
            "select cast(X'313233' AS BINARY(10)) AS C1, cast (X'313233' AS VARBINARY(20)) AS C2,cast(null AS BINARY(10)) AS C3, cast (null AS VARBINARY(20)) AS C4 from sysibm.sysdummy1",
            "READER=31323300000000000000",
            "READER=313233", 
            "null", 
            "null"};

        String[] testArrayNative = {
            "java.io.Reader",
            "select cast(X'313233' AS BINARY(10)) AS C1, cast (X'313233' AS VARBINARY(20)) AS C2,cast(null AS BINARY(10)) AS C3, cast (null AS VARBINARY(20)) AS C4 from sysibm.sysdummy1",
            "READER=123\u0000\u0000\u0000\u0000\u0000\u0000\u0000",
            "READER=123", 
            "null", 
            "null"};

	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

        testGetObjectNamed(testArray); 
    }

    /* Test java.io.Reader 15X with date, time, timestamp  X6 */ 
    public void Var456()
    {
        String[] testArray = {
            "java.io.Reader",
            "select cast('1:02:03' as TIME) AS C1, cast ('2011-09-11' as DATE) AS C2, cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) AS C3, cast(null as TIME) AS C4, cast (null as DATE) AS C5, cast(null AS TIMESTAMP) AS C6 from sysibm.sysdummy1",
            "READER=01:02:03",
            "READER=2011-09-11",
            "READER=2011-09-11 01:02:03.123456",
            "null", 
            "null", 
            "null"};


        String[] testArrayNative = {
            "java.io.Reader",
            "select cast('1:02:03' as TIME) AS C1, cast ('2011-09-11' as DATE) AS C2, cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) AS C3, cast(null as TIME) AS C4, cast (null as DATE) AS C5, cast(null AS TIMESTAMP) AS C6 from sysibm.sysdummy1",
            "READER=01:02:03",
            "READER=2011-09-11",
            "READER=2011-09-11-01.02.03.123456",
            "null", 
            "null", 
            "null"};

	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

        testGetObjectNamed(testArray); 
    }
    
    /* Test java.io.Reader 15X with clob, dbclob, blob  X7 */ 
    public void Var457()
    {
        String[] testArray = {
            "java.io.Reader",
            "select cast('ABC' as CLOB) AS C1, cast ('ABC' as DBCLOB(1M) CCSID 1200) AS C2, cast(X'313233' AS BLOB) AS C3, cast(null as CLOB) AS C4, cast (null as DBCLOB(1M) CCSID 1200) AS C5, cast(null AS BLOB) AS C6 from sysibm.sysdummy1",
            "READER=ABC",
            "READER=ABC",
            "READER=313233", 
            "null", 
            "null", 
            "null"};


        String[] testArrayNative = {
            "java.io.Reader",
            "select cast('ABC' as CLOB) AS C1, cast ('ABC' as DBCLOB(1M) CCSID 1200) AS C2, cast(X'313233' AS BLOB) AS C3, cast(null as CLOB) AS C4, cast (null as DBCLOB(1M) CCSID 1200) AS C5, cast(null AS BLOB) AS C6 from sysibm.sysdummy1",
            "READER=ABC",
            "READER=ABC",
            "READER=123", 
            "null", 
            "null", 
            "null"};


	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

        testGetObjectNamed(testArray); 
    }
    

    /* Test java.io.Reader 15X with datalink  X8 */ 
    public void Var458()
    {
        String[] testArray = {
            "java.io.Reader",
            "select DLVALUE('file://tmp/x') AS C1, CAST(NULL AS DATALINK) AS C2 from sysibm.sysdummy1",
            "READER=FILE://TMP/x", 
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.io.Reader 15X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var459()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.io.Reader",
            "select CAST(CAST(X'1122' AS CHAR(2) FOR BIT DATA) AS ROWID) AS C1, CAST('ABC' as NCHAR(10)) AS C2, CAST('ABC' AS NVARCHAR(100)) AS C3, CAST('ABC' AS NCLOB(100K)) AS C4, CAST(null AS ROWID) AS C5, CAST(null as NCHAR(10)) AS C6, CAST(null AS NVARCHAR(100)) AS C7, CAST(null AS NCLOB(100K)) AS C8 from sysibm.sysdummy1",
            "READER=1122",
            "READER=ABC       ",
            "READER=ABC",
            "READER=ABC", 
            "null", 
            "null", 
            "null",
            "null"};
        testGetObjectNamed(testArray);
      }
    }


    /* Test java.sql.Clob 16X with SQLXML X0 */ 
    public void Var460()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.sql.Clob",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ) as C1, CAST(null as XML) as C2 from sysibm.sysdummy1", 
              "CLOB=<h>hello</h>",
              "null"};
           testGetObjectNamed(testArray);
        }
    }



    /* Test java.sql.Clob 16X with primitive numbers X1 */ 
    public void Var461()
    {
        String[] testArray = {
            "java.sql.Clob",
            "select cast(1 as SMALLINT) AS C1, cast(2 as INTEGER) AS C2, cast(3 as BIGINT) AS C3, cast(null as SMALLINT) AS C4, cast(null as INTEGER) AS C5, cast(null as BIGINT) AS C6 from sysibm.sysdummy1",
            "CLOB=1",
            "CLOB=2",
            "CLOB=3", 
            "null",
            "null",
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.sql.Clob 16X with primitive doubles X2 */ 
    public void Var462()
    {
        String[] testArray = {
            "java.sql.Clob",
            "select cast(1.0 as REAL) AS C1, cast(2.0 as FLOAT) AS C2, cast(3.0 as DOUBLE) AS C3, cast(null as REAL) as C4, cast(null AS FLOAT) AS C5, cast(null as DOUBLE) AS C6  from sysibm.sysdummy1",
            "CLOB=1.0", 
            "CLOB=2.0", 
            "CLOB=3.0", 
            "null",
            "null",
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.sql.Clob 16X with decimal X3 */ 
    public void Var463()
    {
        String[] testArray = {
            "java.sql.Clob",
            "select cast(1.0 as DECIMAL(10,1)) AS C1, cast(2.0 as NUMERIC(10,2)) AS C2, cast(3.0 as DECIMAL(10,4)) AS C3, cast(null as DECIMAL(10,1)) AS C4, cast(null as NUMERIC(10,2)) AS C5, cast(null as DECIMAL(10,4)) AS C6 from sysibm.sysdummy1",
            "CLOB=1.0",
            "CLOB=2.00",
            "CLOB=3.0000",
            "null",
            "null",
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.sql.Clob 16X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var464()
    {
        String[] testArray = {
            "java.sql.Clob",
            "select cast('ABC' as CHAR(10)) AS C1, cast('ABC' as GRAPHIC(10) CCSID 1200) AS C2, cast(null as GRAPHIC(10)) AS C3, cast('ABC' as VARCHAR(10)) AS C4, cast('ABC' as VARGRAPHIC(10) CCSID 1200) AS C5, cast(null as VARGRAPHIC(10)) AS C6 from sysibm.sysdummy1",
            "CLOB=ABC       ",
            "CLOB=ABC       ",
            "null",
            "CLOB=ABC",
            "CLOB=ABC",
            "null"};
        testGetObjectNamed(testArray); 
    }
    


    /* Test java.sql.Clob 16X with BINARY, VARBINARY  X5 */ 
    public void Var465()
    {
        String[] testArray = {
            "java.sql.Clob",
            "select cast(X'313233' AS BINARY(10)) AS C1, cast (X'313233' AS VARBINARY(20)) AS C2,cast(null AS BINARY(10)) AS C3, cast (null AS VARBINARY(20)) AS C4 from sysibm.sysdummy1",
            "CLOB=31323300000000000000",
            "CLOB=313233", 
            "null", 
            "null"};


        String[] testArrayNative = {
            "java.sql.Clob",
            "select cast(X'313233' AS BINARY(10)) AS C1, cast (X'313233' AS VARBINARY(20)) AS C2,cast(null AS BINARY(10)) AS C3, cast (null AS VARBINARY(20)) AS C4 from sysibm.sysdummy1",
            "CLOB=123\u0000\u0000\u0000\u0000\u0000\u0000\u0000",
            "CLOB=123", 
            "null", 
            "null"};


	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

        testGetObjectNamed(testArray); 
    }

    /* Test java.sql.Clob 16X with date, time, timestamp  X6 */ 
    public void Var466()
    {
        String[] testArray = {
            "java.sql.Clob",
            "select cast('1:02:03' as TIME) AS C1, cast ('2011-09-11' as DATE) AS C2, cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) AS C3, cast(null as TIME) AS C4, cast (null as DATE) AS C5, cast(null AS TIMESTAMP) AS C6 from sysibm.sysdummy1",
            "CLOB=01:02:03",
            "CLOB=2011-09-11",
            "CLOB=2011-09-11 01:02:03.123456",
            "null", 
            "null", 
            "null"};

        String[] testArrayNative = {
            "java.sql.Clob",
            "select cast('1:02:03' as TIME) AS C1, cast ('2011-09-11' as DATE) AS C2, cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) AS C3, cast(null as TIME) AS C4, cast (null as DATE) AS C5, cast(null AS TIMESTAMP) AS C6 from sysibm.sysdummy1",
            "CLOB=01:02:03",
            "CLOB=2011-09-11",
            "CLOB=2011-09-11-01.02.03.123456",
            "null", 
            "null", 
            "null"};


	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 


        testGetObjectNamed(testArray); 
    }
    
    /* Test java.sql.Clob 16X with clob, dbclob, blob  X7 */ 
    public void Var467()  {
	String[] testArray = {
	    "java.sql.Clob",
	    "select cast('ABC' as CLOB) AS C1, cast ('ABC' as DBCLOB(1M) CCSID 1200) AS C2, cast(X'F1F2F3' AS BLOB) AS C3, cast(null as CLOB) AS C4, cast (null as DBCLOB(1M) CCSID 1200) AS C5, cast(null AS BLOB) AS C6 from sysibm.sysdummy1",
	    "CLOB=ABC",
	    "CLOB=ABC",
	    "CLOB=F1F2F3", 
	    "null", 
	    "null", 
	    "null"};
	    String[] testArrayNative = {
		"java.sql.Clob",
		"select cast('ABC' as CLOB) AS C1, cast ('ABC' as DBCLOB(1M) CCSID 1200) AS C2, cast(X'F1F2F3' AS BLOB) AS C3, cast(null as CLOB) AS C4, cast (null as DBCLOB(1M) CCSID 1200) AS C5, cast(null AS BLOB) AS C6 from sysibm.sysdummy1",
		"CLOB=ABC",
		"CLOB=ABC",
		"CLOB=123", 
		"null", 
		"null", 
		"null"};

		if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		    testArray = testArrayNative; 
		} 


        testGetObjectNamed(testArray); 
    }
    

    /* Test java.sql.Clob 16X with datalink  X8 */ 
    public void Var468()
    {
        String[] testArray = {
            "java.sql.Clob",
            "select DLVALUE('file://tmp/x') AS C1, CAST(NULL AS DATALINK) AS C2 from sysibm.sysdummy1",
            "CLOB=FILE://TMP/x", 
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.sql.Clob 16X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var469()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.sql.Clob",
            "select CAST(CAST(X'1122' AS CHAR(2) FOR BIT DATA) AS ROWID) AS C1, CAST('ABC' as NCHAR(10)) AS C2, CAST('ABC' AS NVARCHAR(100)) AS C3, CAST('ABC' AS NCLOB(100K)) AS C4, CAST(null AS ROWID) AS C5, CAST(null as NCHAR(10)) AS C6, CAST(null AS NVARCHAR(100)) AS C7, CAST(null AS NCLOB(100K)) AS C8 from sysibm.sysdummy1",
            "CLOB=1122",
            "CLOB=ABC       ",
            "CLOB=ABC",
            "CLOB=ABC", 
            "null", 
            "null", 
            "null",
            "null"};
        testGetObjectNamed(testArray);
      }
    }

    /* Test java.sql.Blob 17X with SQLXML X0 */ 
    public void Var470()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.sql.Blob",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ) as C1, CAST(null as XML) as C2 from sysibm.sysdummy1", 
              "BLOB=4C6FA7949340A58599A28996957E7FF14BF07F4085958396848995877E7FC9C2D4F0F3F77F6F6E4C886E88859393964C61886E",
              "null",};

           String[] testArrayNative = {
              "java.sql.Blob",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ) as C1, CAST(null as XML) as C2 from sysibm.sysdummy1", 
              "BLOB=3C683E68656C6C6F3C2F683E",
              "null",};

	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

           testGetObjectNamed(testArray);
        }
    }


    /* Test java.sql.Blob 17X with primitive numbers X1 */ 
    public void Var471()
    {
        String[] testArray = {
            "java.sql.Blob",
            "select cast(1 as SMALLINT) AS C1, cast(2 as INTEGER) AS C2, cast(3 as BIGINT) AS C3, cast(null as SMALLINT) AS C4, cast(null as INTEGER) AS C5, cast(null as BIGINT) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.sql.Blob 17X with primitive doubles X2 */ 
    public void Var472()
    {
        String[] testArray = {
            "java.sql.Blob",
            "select cast(1.0 as REAL) AS C1, cast(2.0 as FLOAT) AS C2, cast(3.0 as DOUBLE) AS C3, cast(null as REAL) as C4, cast(null AS FLOAT) AS C5, cast(null as DOUBLE) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.sql.Blob 17X with decimal X3 */ 
    public void Var473()
    {
        String[] testArray = {
            "java.sql.Blob",
            "select cast(1.0 as DECIMAL(10,1)) AS C1, cast(2.0 as NUMERIC(10,2)) AS C2, cast(3.0 as DECIMAL(10,4)) AS C3, cast(null as DECIMAL(10,1)) AS C4, cast(null as NUMERIC(10,2)) AS C5, cast(null as DECIMAL(10,4)) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.sql.Blob 17X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var474()
    {
        String[] testArray = {
            "java.sql.Blob",
            "select cast('1' as CHAR(10)) AS C1, cast('2' as GRAPHIC(10) CCSID 1200) AS C2, cast(null as GRAPHIC(10)) AS C3, cast('1' as VARCHAR(10)) AS C4, cast('2' as VARGRAPHIC(10) CCSID 1200) AS C5, cast(null as VARGRAPHIC(10)) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "BLOB=01",
            "BLOB=02",
            "null"};

        String[] testArrayNative = {
            "java.sql.Blob",
            "select cast('1' as CHAR(10)) AS C1, cast('2' as GRAPHIC(10) CCSID 1200) AS C2, cast(null as GRAPHIC(10)) AS C3, cast('1' as VARCHAR(10)) AS C4, cast('2' as VARGRAPHIC(10) CCSID 1200) AS C5, cast(null as VARGRAPHIC(10)) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "Data type mismatch",
            "Data type mismatch",
            "null"};

	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

        testGetObjectNamed(testArray); 
    }
    


    /* Test java.sql.Blob 17X with BINARY, VARBINARY  X5 */ 
    public void Var475()
    {
        String[] testArray = {
            "java.sql.Blob",
            "select cast(X'01' AS BINARY(1)) AS C1, cast (X'02' AS VARBINARY(20)) AS C2,cast(null AS BINARY(1)) AS C3, cast (null AS VARBINARY(20)) AS C4 from sysibm.sysdummy1",
            "BLOB=01",
            "BLOB=02",
            "null",
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.sql.Blob 17X with date, time, timestamp  X6 */ 
    public void Var476()
    {
        String[] testArray = {
            "java.sql.Blob",
            "select cast('1:02:03' as TIME) AS C1, cast ('2011-09-11' as DATE) AS C2, cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) AS C3 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.sql.Blob 17X with clob, dbclob, blob  X7 */ 
    public void Var477()
    {
        String[] testArray = {
            "java.sql.Blob",
            "select cast('1' as CLOB) AS C1, cast ('2' as DBCLOB(1M) CCSID 1200) AS C2, cast(X'03' AS BLOB) AS C3 from sysibm.sysdummy1",
            "BLOB=01",
            "BLOB=02",
            "BLOB=03"};
        String[] testArrayNative = {
            "java.sql.Blob",
            "select cast('1' as CLOB) AS C1, cast ('2' as DBCLOB(1M) CCSID 1200) AS C2, cast(X'03' AS BLOB) AS C3 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "BLOB=03"};

	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

        testGetObjectNamed(testArray); 
    }
    

    /* Test java.sql.Blob 17X with datalink  X8 */ 
    public void Var478()
    {
        String[] testArray = {
            "java.sql.Blob",
            "select DLVALUE('file://tmp/x') AS C1 from sysibm.sysdummy1",
            "Data type mismatch"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.sql.Blob 17X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var479()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.sql.Blob",
            "select CAST(CAST(X'01' AS CHAR(2) FOR BIT DATA) AS ROWID) AS C1, CAST('1' as NCHAR(10)) AS C2, CAST('1' AS NVARCHAR(100)) AS C3," +
            " CAST('1' AS NCLOB(100K)) AS C4, CAST(null AS ROWID) AS C5, CAST(null as NCHAR(10)) AS C6, CAST(null AS NVARCHAR(100)) AS C7, CAST(null AS NCLOB(100K)) AS C8 from sysibm.sysdummy1",
            "BLOB=0140",
            "Data type mismatch",
            "BLOB=01", 
            "BLOB=01",
            "null",
            "null",
            "null",
            "null"};



    String[] testArrayNative = {
        "java.sql.Blob",
	"select CAST(CAST(X'01' AS CHAR(2) FOR BIT DATA) AS ROWID) AS C1, CAST('1' as NCHAR(10)) AS C2, CAST('1' AS NVARCHAR(100)) AS C3," +
	" CAST('1' AS NCLOB(100K)) AS C4, CAST(null AS ROWID) AS C5, CAST(null as NCHAR(10)) AS C6, CAST(null AS NVARCHAR(100)) AS C7, CAST(null AS NCLOB(100K)) AS C8 from sysibm.sysdummy1",
        "BLOB=0140",
        "Data type mismatch",
        "Data type mismatch", 
        "Data type mismatch",
        "null",
        "null",
        "null",
        "null"};

    
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 


        testGetObjectNamed(testArray);
      }
    }



    /* Test java.sql.NClob 18X with SQLXML X0 */ 
    public void Var480()
    {
      if (checkJdbc40()) {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.sql.NClob",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ) as C1, CAST(null as XML) as C2 from sysibm.sysdummy1", 
              "CLOB=<h>hello</h>",
              "null"};
           testGetObjectNamed(testArray);
        }
      }
    }



    /* Test java.sql.NClob 18X with primitive numbers X1 */ 
    public void Var481()
    {
        String[] testArray = {
            "java.sql.NClob",
            "select cast(1 as SMALLINT) AS C1, cast(2 as INTEGER) AS C2, cast(3 as BIGINT) AS C3, cast(null as SMALLINT) AS C4, cast(null as INTEGER) AS C5, cast(null as BIGINT) AS C6 from sysibm.sysdummy1",
            "CLOB=1",
            "CLOB=2", 
            "CLOB=3", 
            "null",
            "null",
            "null"};
        if (checkJdbc40()) 
        testGetObjectNamed(testArray); 
    }

    /* Test java.sql.NClob 18X with primitive doubles X2 */ 
    public void Var482()
    {
        String[] testArray = {
            "java.sql.NClob",
            "select cast(1.0 as REAL) AS C1, cast(2.0 as FLOAT) AS C2, cast(3.0 as DOUBLE) AS C3, cast(null as REAL) as C4, cast(null AS FLOAT) AS C5, cast(null as DOUBLE) AS C6  from sysibm.sysdummy1",
            "CLOB=1.0", 
            "CLOB=2.0", 
            "CLOB=3.0", 
            "null",
            "null",
            "null"};
        if (checkJdbc40()) 
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.sql.NClob 18X with decimal X3 */ 
    public void Var483()
    {
        String[] testArray = {
            "java.sql.NClob",
            "select cast(1.0 as DECIMAL(10,1)) AS C1, cast(2.0 as NUMERIC(10,2)) AS C2, cast(3.0 as DECIMAL(10,4)) AS C3, cast(null as DECIMAL(10,1)) AS C4, cast(null as NUMERIC(10,2)) AS C5, cast(null as DECIMAL(10,4)) AS C6 from sysibm.sysdummy1",
            "CLOB=1.0",
            "CLOB=2.00",
            "CLOB=3.0000",
            "null",
            "null",
            "null"};
        if (checkJdbc40()) 
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.sql.NClob 18X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var484()
    {
        String[] testArray = {
            "java.sql.NClob",
            "select cast('ABC' as CHAR(10)) AS C1, cast('ABC' as GRAPHIC(10) CCSID 1200) AS C2, cast(null as GRAPHIC(10)) AS C3, cast('ABC' as VARCHAR(10)) AS C4, cast('ABC' as VARGRAPHIC(10) CCSID 1200) AS C5, cast(null as VARGRAPHIC(10)) AS C6 from sysibm.sysdummy1",
            "CLOB=ABC       ",
            "CLOB=ABC       ",
            "null",
            "CLOB=ABC",
            "CLOB=ABC",
            "null"};
        if (checkJdbc40()) 
        testGetObjectNamed(testArray); 
    }
    


    /* Test java.sql.NClob 18X with BINARY, VARBINARY  X5 */ 
    public void Var485()
    {
        String[] testArray = {
            "java.sql.NClob",
            "select cast(X'313233' AS BINARY(10)) AS C1, cast (X'313233' AS VARBINARY(20)) AS C2,cast(null AS BINARY(10)) AS C3, cast (null AS VARBINARY(20)) AS C4 from sysibm.sysdummy1",
            "CLOB=31323300000000000000",
            "CLOB=313233", 
            "null", 
            "null"};




        String[] testArrayNative = {
            "java.sql.NClob",
            "select cast(X'313233' AS BINARY(10)) AS C1, cast (X'313233' AS VARBINARY(20)) AS C2,cast(null AS BINARY(10)) AS C3, cast (null AS VARBINARY(20)) AS C4 from sysibm.sysdummy1",
            "CLOB=123\u0000\u0000\u0000\u0000\u0000\u0000\u0000",
            "CLOB=123", 
            "null", 
            "null"};

	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 



        if (checkJdbc40()) 
        testGetObjectNamed(testArray); 
    }

    /* Test java.sql.NClob 18X with date, time, timestamp  X6 */ 
    public void Var486()
    {
        String[] testArray = {
            "java.sql.NClob",
            "select cast('1:02:03' as TIME) AS C1, cast ('2011-09-11' as DATE) AS C2, cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) AS C3, cast(null as TIME) AS C4, cast (null as DATE) AS C5, cast(null AS TIMESTAMP) AS C6 from sysibm.sysdummy1",
            "CLOB=01:02:03",
            "CLOB=2011-09-11",
            "CLOB=2011-09-11 01:02:03.123456",
            "null", 
            "null", 
            "null"};


        String[] testArrayNative = {
            "java.sql.NClob",
            "select cast('1:02:03' as TIME) AS C1, cast ('2011-09-11' as DATE) AS C2, cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) AS C3, cast(null as TIME) AS C4, cast (null as DATE) AS C5, cast(null AS TIMESTAMP) AS C6 from sysibm.sysdummy1",
            "CLOB=01:02:03",
            "CLOB=2011-09-11",
            "CLOB=2011-09-11-01.02.03.123456",
            "null", 
            "null", 
            "null"};


	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

        if (checkJdbc40()) 
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.sql.NClob 18X with NClob, dbNClob, blob  X7 */ 
    public void Var487()
    {
        String[] testArray = {
            "java.sql.NClob",
            "select cast('ABC' as CLOB) AS C1, cast ('ABC' as DBCLOB(1M) CCSID 1200) AS C2, cast(X'313233' AS BLOB) AS C3, cast(null as CLOB) AS C4, cast (null as DBCLOB(1M) CCSID 1200) AS C5, cast(null AS BLOB) AS C6 from sysibm.sysdummy1",
            "CLOB=ABC",
            "CLOB=ABC",
            "CLOB=313233", 
            "null", 
            "null", 
            "null"};

        String[] testArrayNative = {
            "java.sql.NClob",
            "select cast('ABC' as CLOB) AS C1, cast ('ABC' as DBCLOB(1M) CCSID 1200) AS C2, cast(X'313233' AS BLOB) AS C3, cast(null as CLOB) AS C4, cast (null as DBCLOB(1M) CCSID 1200) AS C5, cast(null AS BLOB) AS C6 from sysibm.sysdummy1",
            "CLOB=ABC",
            "CLOB=ABC",
            "CLOB=123", 
            "null", 
            "null", 
            "null"};

	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

        if (checkJdbc40()) 
        testGetObjectNamed(testArray); 
    }
    

    /* Test java.sql.NClob 18X with datalink  X8 */ 
    public void Var488()
    {
        String[] testArray = {
            "java.sql.NClob",
            "select DLVALUE('file://tmp/x') AS C1, CAST(NULL AS DATALINK) AS C2 from sysibm.sysdummy1",
            "CLOB=FILE://TMP/x", 
            "null"};
        if (checkJdbc40()) 
	    testGetObjectNamed(testArray); 
    }

    /* Test java.sql.NClob 18X with rowid, nchar, nvarchar, NClob X9 */ 
    public void Var489()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.sql.NClob",
            "select CAST(CAST(X'1122' AS CHAR(2) FOR BIT DATA) AS ROWID) AS C1, CAST('ABC' as NCHAR(10)) AS C2, CAST('ABC' AS NVARCHAR(100)) AS C3, CAST('ABC' AS NCLOB(100K)) AS C4, CAST(null AS ROWID) AS C5, CAST(null as NCHAR(10)) AS C6, CAST(null AS NVARCHAR(100)) AS C7, CAST(null AS NCLOB(100K)) AS C8 from sysibm.sysdummy1",
            "CLOB=1122",
            "CLOB=ABC       ",
            "CLOB=ABC",
            "CLOB=ABC", 
            "null", 
            "null", 
            "null",
            "null"};


    String[] testArrayNative = {
            "java.sql.NClob",
            "select CAST(CAST(X'1122' AS CHAR(2) FOR BIT DATA) AS ROWID) AS C1, CAST('ABC' as NCHAR(10)) AS C2, CAST('ABC' AS NVARCHAR(100)) AS C3, CAST('ABC' AS NCLOB(100K)) AS C4, CAST(null AS ROWID) AS C5, CAST(null as NCHAR(10)) AS C6, CAST(null AS NVARCHAR(100)) AS C7, CAST(null AS NCLOB(100K)) AS C8 from sysibm.sysdummy1",
            "CLOB=1122",
            "CLOB=ABC       ",
            "CLOB=ABC",
            "CLOB=ABC", 
            "null", 
            "null", 
            "null",
            "null"};

	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

    if (checkJdbc40()) 
        testGetObjectNamed(testArray);
      }
    }


    /* Test java.sql.Array 19X with SQLXML X0 */ 
    public void Var490()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.sql.Array",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ) as C1, CAST(null as XML) as C2 from sysibm.sysdummy1", 
              "Data type mismatch",
              "Data type mismatch",
	   };
           testGetObjectNamed(testArray);
        }
    }



    /* Test java.sql.Array 19X with primitive numbers X1 */ 
    public void Var491()
    {
        String[] testArray = {
            "java.sql.Array",
            "select cast(1 as SMALLINT) AS C1, cast(2 as INTEGER) AS C2, cast(3 as BIGINT) AS C3, cast(null as SMALLINT) AS C4, cast(null as INTEGER) AS C5, cast(null as BIGINT) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
	};
        testGetObjectNamed(testArray); 
    }

    /* Test java.sql.Array 19X with primitive doubles X2 */ 
    public void Var492()
    {
        String[] testArray = {
            "java.sql.Array",
            "select cast(1.0 as REAL) AS C1, cast(2.0 as FLOAT) AS C2, cast(3.0 as DOUBLE) AS C3, cast(null as REAL) as C4, cast(null AS FLOAT) AS C5, cast(null as DOUBLE) AS C6  from sysibm.sysdummy1",
            "Data type mismatch", 
            "Data type mismatch", 
            "Data type mismatch", 
            "Data type mismatch", 
            "Data type mismatch", 
            "Data type mismatch", 
	};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.sql.Array 19X with decimal X3 */ 
    public void Var493()
    {
        String[] testArray = {
            "java.sql.Array",
            "select cast(1.0 as DECIMAL(10,1)) AS C1, cast(2.0 as NUMERIC(10,2)) AS C2, cast(3.0 as DECIMAL(10,4)) AS C3, cast(null as DECIMAL(10,1)) AS C4, cast(null as NUMERIC(10,2)) AS C5, cast(null as DECIMAL(10,4)) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
	};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.sql.Array 19X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var494()
    {
        String[] testArray = {
            "java.sql.Array",
            "select cast('ABC' as CHAR(10)) AS C1, cast('ABC' as GRAPHIC(10) CCSID 1200) AS C2, cast(null as GRAPHIC(10)) AS C3, cast('ABC' as VARCHAR(10)) AS C4, cast('ABC' as VARGRAPHIC(10) CCSID 1200) AS C5, cast(null as VARGRAPHIC(10)) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
	};
        testGetObjectNamed(testArray); 
    }
    


    /* Test java.sql.Array 19X with BINARY, VARBINARY  X5 */ 
    public void Var495()
    {
        String[] testArray = {
            "java.sql.Array",
            "select cast(X'313233' AS BINARY(10)) AS C1, cast (X'313233' AS VARBINARY(20)) AS C2,cast(null AS BINARY(10)) AS C3, cast (null AS VARBINARY(20)) AS C4 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch", 
            "Data type mismatch",
            "Data type mismatch", 
	};
        testGetObjectNamed(testArray); 
    }

    /* Test java.sql.Array 19X with date, time, timestamp  X6 */ 
    public void Var496()
    {
        String[] testArray = {
            "java.sql.Array",
            "select cast('1:02:03' as TIME) AS C1, cast ('2011-09-11' as DATE) AS C2, cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) AS C3, cast(null as TIME) AS C4, cast (null as DATE) AS C5, cast(null AS TIMESTAMP) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
	};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.sql.Array 19X with CLOB, dbclob, blob  X7 */ 
    public void Var497()
    {
        String[] testArray = {
            "java.sql.Array",
            "select cast('ABC' as CLOB) AS C1, cast ('ABC' as DBCLOB(1M) CCSID 1200) AS C2, cast(X'313233' AS BLOB) AS C3, " +
            "cast(null as clob) AS C4, cast (null as DBCLOB(1M) CCSID 1200) AS C5, cast(null AS BLOB) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
	    "Data type mismatch",
	    "Data type mismatch",
	    "Data type mismatch", 
	};
	testGetObjectNamed(testArray); 
    }
    

    /* Test java.sql.Array 19X with datalink  X8 */ 
    public void Var498()
    {
        String[] testArray = {
            "java.sql.Array",
            "select DLVALUE('file://tmp/x') AS C1, CAST(NULL AS DATALINK) AS C2 from sysibm.sysdummy1",
            "Data type mismatch", 
            "Data type mismatch", 
	};
        testGetObjectNamed(testArray); 
    }

    /* Test java.sql.Array 19X with rowid, nchar, nvarchar, nArray X9 */ 
    public void Var499()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.sql.Array",
            "select CAST(CAST(X'1122' AS CHAR(2) FOR BIT DATA) AS ROWID) AS C1, " +
            "CAST('ABC' as NCHAR(10)) AS C2, CAST('ABC' AS NVARCHAR(100)) AS C3, CAST('ABC' AS NCLOB(100K)) AS C4, " +
            "CAST(null AS ROWID) AS C5, CAST(null as NCHAR(10)) AS C6, CAST(null AS NVARCHAR(100))  AS C7, " +
            "CAST(null AS NCLOB(100K))  AS C8 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
    };
        testGetObjectNamed(testArray);
      }
    }

    
    /* Test java.sql.Ref 20X with SQLXML X0 */ 
    public void Var500()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.sql.Ref",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ) as C1, CAST(null as XML) as C2 from sysibm.sysdummy1", 
              "Data type mismatch",
              "Data type mismatch",
	   };
           testGetObjectNamed(testArray);
        }
    }



    /* Test java.sql.Ref 20X with primitive numbers X1 */ 
    public void Var501()
    {
        String[] testArray = {
            "java.sql.Ref",
            "select cast(1 as SMALLINT) AS C1, cast(2 as INTEGER) AS C2, cast(3 as BIGINT) AS C3, cast(null as SMALLINT) AS C4, cast(null as INTEGER) AS C5, cast(null as BIGINT) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 

	};
        testGetObjectNamed(testArray); 
    }

    /* Test java.sql.Ref 20X with primitive doubles X2 */ 
    public void Var502()
    {
        String[] testArray = {
            "java.sql.Ref",
            "select cast(1.0 as REAL) AS C1, cast(2.0 as FLOAT) AS C2, cast(3.0 as DOUBLE) AS C3, cast(null as REAL) as C4, cast(null AS FLOAT) AS C5, cast(null as DOUBLE) AS C6  from sysibm.sysdummy1",
            "Data type mismatch", 
            "Data type mismatch", 
            "Data type mismatch", 
            "Data type mismatch", 
            "Data type mismatch", 
            "Data type mismatch", 
	};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.sql.Ref 20X with decimal X3 */ 
    public void Var503()
    {
        String[] testArray = {
            "java.sql.Ref",
            "select cast(1.0 as DECIMAL(10,1)) AS C1, cast(2.0 as NUMERIC(10,2)) AS C2, cast(3.0 as DECIMAL(10,4)) AS C3, cast(null as DECIMAL(10,1)) AS C4, cast(null as NUMERIC(10,2)) AS C5, cast(null as DECIMAL(10,4)) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
	};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.sql.Ref 20X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var504()
    {
        String[] testArray = {
            "java.sql.Ref",
            "select cast('ABC' as CHAR(10)) AS C1, cast('ABC' as GRAPHIC(10) CCSID 1200) AS C2, cast(null as GRAPHIC(10)) AS C3, cast('ABC' as VARCHAR(10)) AS C4, cast('ABC' as VARGRAPHIC(10) CCSID 1200) AS C5, cast(null as VARGRAPHIC(10)) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
	};
        testGetObjectNamed(testArray); 
    }
    


    /* Test java.sql.Ref 20X with BINARY, VARBINARY  X5 */ 
    public void Var505()
    {
        String[] testArray = {
            "java.sql.Ref",
            "select cast(X'313233' AS BINARY(10)) AS C1, cast (X'313233' AS VARBINARY(20)) AS C2,cast(null AS BINARY(10)) AS C3, cast (null AS VARBINARY(20)) AS C4 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch", 
            "Data type mismatch",
            "Data type mismatch", 
	};
        testGetObjectNamed(testArray); 
    }

    /* Test java.sql.Ref 20X with date, time, timestamp  X6 */ 
    public void Var506()
    {
        String[] testArray = {
            "java.sql.Ref",
            "select cast('1:02:03' as TIME) AS C1, cast ('2011-09-11' as DATE) AS C2, cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) AS C3, cast(null as TIME) AS C4, cast (null as DATE) AS C5, cast(null AS TIMESTAMP) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",

	};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.sql.Ref 20X with clob, dbclob, blob  X7 */ 
    public void Var507()
    {
        String[] testArray = {
            "java.sql.Ref",
            "select cast('ABC' as CLOB) AS C1, cast ('ABC' as DBCLOB(1M) CCSID 1200) AS C2, cast(X'313233' AS BLOB) AS C3, cast(null as CLOB) AS C4, cast (null as DBCLOB(1M) CCSID 1200) AS C5, cast(null AS BLOB) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
	};
        testGetObjectNamed(testArray); 
    }
    

    /* Test java.sql.Ref 20X with datalink  X8 */ 
    public void Var508()
    {
        String[] testArray = {
            "java.sql.Ref",
            "select DLVALUE('file://tmp/x') AS C1, CAST(NULL AS DATALINK) AS C2 from sysibm.sysdummy1",
            "Data type mismatch", 
            "Data type mismatch"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.sql.Ref 20X with rowid, nchar, nvarchar, nArray X9 */ 
    public void Var509()
    {
      if (checkRelease710()) {  
	  String[] testArray = {
	      "java.sql.Ref",
	      "select CAST(CAST(X'1122' AS CHAR(2) FOR BIT DATA) AS ROWID) AS C1, " +
	      "CAST('ABC' as NCHAR(10)) AS C2, CAST('ABC' AS NVARCHAR(100)) AS C3, CAST('ABC' AS NCLOB(100K)) AS C4," +
	      " CAST(null AS ROWID) AS C5, CAST(null as NCHAR(10)) AS C6, CAST(null AS NVARCHAR(100)) AS C7, CAST(null AS NCLOB(100K)) AS C8 from sysibm.sysdummy1",
	      "Data type mismatch",
	      "Data type mismatch",
	      "Data type mismatch",
	      "Data type mismatch",
	      "Data type mismatch",
	      "Data type mismatch",
	      "Data type mismatch",
	      "Data type mismatch",
	  }; 

        testGetObjectNamed(testArray);
      }
    }


    /* Test java.net.URL 21X with SQLXML X0 */ 

    public void Var510()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.net.URL",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ) as C1, CAST(null as XML) as C2 from sysibm.sysdummy1", 
              "Data type mismatch",
              "null"};
           testGetObjectNamed(testArray);
        }
    }



    /* Test java.net.URL 21X with primitive numbers X1 */ 
    public void Var511()
    {
        String[] testArray = {
            "java.net.URL",
            "select cast(1 as SMALLINT) AS C1, cast(2 as INTEGER) AS C2, cast(3 as BIGINT) AS C3, cast(null as SMALLINT) AS C4, cast(null as INTEGER) AS C5, cast(null as BIGINT) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "null",
            "null",
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.net.URL 21X with primitive doubles X2 */ 
    public void Var512()
    {
        String[] testArray = {
            "java.net.URL",
            "select cast(1.0 as REAL) AS C1, cast(2.0 as FLOAT) AS C2, cast(3.0 as DOUBLE) AS C3, cast(null as REAL) as C4, cast(null AS FLOAT) AS C5, cast(null as DOUBLE) AS C6  from sysibm.sysdummy1",
            "Data type mismatch", 
            "Data type mismatch", 
            "Data type mismatch", 
            "null",
            "null",
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.net.URL 21X with decimal X3 */ 
    public void Var513()
    {
        String[] testArray = {
            "java.net.URL",
            "select cast(1.0 as DECIMAL(10,1)) AS C1, cast(2.0 as NUMERIC(10,2)) AS C2, cast(3.0 as DECIMAL(10,4)) AS C3, cast(null as DECIMAL(10,1)) AS C4, cast(null as NUMERIC(10,2)) AS C5, cast(null as DECIMAL(10,4)) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null",
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.net.URL 21X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var514()
    {
        String[] testArray = {
            "java.net.URL",
            "select cast('http://ibm.com' as CHAR(20)) AS C1, cast('http://ibm.com' as GRAPHIC(20) CCSID 1200) AS C2, " +
            "cast(null as GRAPHIC(10)) AS C3, " +
            "cast('http://ibm.com' as VARCHAR(20)) AS C4, cast('http://ibm.com' as VARGRAPHIC(20) CCSID 1200) AS C5, " +
            "cast(null as VARGRAPHIC(10)) AS C6 from sysibm.sysdummy1 ",
            "http://ibm.com",
            "http://ibm.com",
            "null",
            "http://ibm.com",
            "http://ibm.com",
            "null"};
        testGetObjectNamed(testArray); 
    }
    


    /* Test java.net.URL 21X with BINARY, VARBINARY  X5 */ 
    public void Var515()
    {
        String[] testArray = {
            "java.net.URL",
            "select cast(X'313233' AS BINARY(10)) AS C1, cast (X'313233' AS VARBINARY(20)) AS C2,cast(null AS BINARY(10)) AS C3, cast (null AS VARBINARY(20)) AS C4 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.net.URL 21X with date, time, timestamp  X6 */ 
    public void Var516()
    {
        String[] testArray = {
            "java.net.URL",
            "select cast('1:02:03' as TIME) AS C1, cast ('2011-09-11' as DATE) AS C2, cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) AS C3, cast(null as TIME) AS C4, cast (null as DATE) AS C5, cast(null AS TIMESTAMP) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.net.URL 21X with clob, dbclob, blob  X7 */ 
    public void Var517()
    {
        String[] testArray = {
            "java.net.URL",
            "select cast('http://ibm.com' as CLOB) AS C1, cast ('http://ibm.com' as DBCLOB(1M) CCSID 1200) AS C2, cast(X'313233' AS BLOB) AS C3, cast(null as CLOB) AS C4, cast (null as DBCLOB(1M) CCSID 1200) AS C5, cast(null AS BLOB) AS C6 from sysibm.sysdummy1",
            "http://ibm.com",
            "http://ibm.com",
            "Data type mismatch", 
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }
    

    /* Test java.net.URL 21X with datalink  X8 */ 
    public void Var518()
    {
        String[] testArray = {
            "java.net.URL",
            "select DLVALUE('file://tmp/x') AS C1, CAST(NULL AS DATALINK) AS C2 from sysibm.sysdummy1",
            "file://TMP/x", 
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test java.net.URL 21X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var519()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.net.URL",
            "select CAST(CAST(X'1122' AS CHAR(2) FOR BIT DATA) AS ROWID) AS C1, CAST('ABC' as NCHAR(10)) AS C2, CAST('ABC' AS NVARCHAR(100)) AS C3, CAST('ABC' AS NCLOB(100K)) AS C4, CAST(null AS ROWID) AS C5, CAST(null as NCHAR(10)) AS C6, CAST(null AS NVARCHAR(100)) AS C7, CAST(null AS NCLOB(100K)) AS C8 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null", 
            "null", 
            "null",
            "null"};
        testGetObjectNamed(testArray);
      }
    }


    /* Test object 22X with SQLXML X0 */ 
    public void Var520()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.lang.Object",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ) as C1, CAST(null as XML) as C2 from sysibm.sysdummy1", 
              "SQLXML=<h>hello</h>",
              "null"};


           testGetObjectNamed(testArray);
        }
    }



    /* Test object 22X with primitive numbers X1 */ 
    public void Var521()
    {
        String[] testArray = {
            "java.lang.Object",
            "select cast(1 as SMALLINT) AS C1, cast(2 as INTEGER) AS C2, cast(3 as BIGINT) AS C3, cast(null as SMALLINT) AS C4, cast(null as INTEGER) AS C5, cast(null as BIGINT) AS C6 from sysibm.sysdummy1",
            "1",
            "2",
            "3", 
            "null",
            "null",
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test object 22X with primitive doubles X2 */ 
    public void Var522()
    {
        String[] testArray = {
            "java.lang.Object",
            "select cast(1.0 as REAL) AS C1, cast(2.0 as FLOAT) AS C2, cast(3.0 as DOUBLE) AS C3, cast(null as REAL) as C4, cast(null AS FLOAT) AS C5, cast(null as DOUBLE) AS C6  from sysibm.sysdummy1",
            "1.0",
            "2.0",
            "3.0",
            "null",
            "null",
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test object 22X with decimal X3 */ 
    public void Var523()
    {
        String[] testArray = {
            "java.lang.Object",
            "select cast(1.0 as DECIMAL(10,1)) AS C1, cast(2.0 as NUMERIC(10,2)) AS C2, cast(3.0 as DECIMAL(10,4)) AS C3, cast(null as DECIMAL(10,1)) AS C4, cast(null as NUMERIC(10,2)) AS C5, cast(null as DECIMAL(10,4)) AS C6 from sysibm.sysdummy1",
            "1.0",
            "2.00",
            "3.0000",
            "null",
            "null",
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test object 22X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var524()
    {
        String[] testArray = {
            "java.lang.Object",
            "select cast('ABC' as CHAR(10)) AS C1, cast('ABC' as GRAPHIC(10) CCSID 1200) AS C2, cast(null as GRAPHIC(10)) AS C3, cast('ABC' as VARCHAR(10)) AS C4, cast('ABC' as VARGRAPHIC(10) CCSID 1200) AS C5, cast(null as VARGRAPHIC(10)) AS C6 from sysibm.sysdummy1",
            "ABC       ",
            "ABC       ",
            "null",
            "ABC",
            "ABC",
            "null"};
        testGetObjectNamed(testArray); 
    }
    


    /* Test object 22X with BINARY, VARBINARY  X5 */ 
    public void Var525()
    {
        String[] testArray = {
            "java.lang.Object",
            "select cast(X'313233' AS BINARY(10)) AS C1, cast (X'313233' AS VARBINARY(20)) AS C2,cast(null AS BINARY(10)) AS C3, cast (null AS VARBINARY(20)) AS C4 from sysibm.sysdummy1",
            "BYTEARRAY=31323300000000000000",
            "BYTEARRAY=313233", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }

    /* Test object 22X with date, time, timestamp  X6 */ 
    public void Var526()
    {
        String[] testArray = {
            "java.lang.Object",
            "select cast('1:02:03' as TIME) AS C1, cast ('2011-09-11' as DATE) AS C2, cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) AS C3, cast(null as TIME) AS C4, cast (null as DATE) AS C5, cast(null AS TIMESTAMP) AS C6 from sysibm.sysdummy1",
            "01:02:03",
            "2011-09-11",
            "2011-09-11 01:02:03.123456", 
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }
    
    /* Test object 22X with clob, dbclob, blob  X7 */ 
    public void Var527()
    {
        String[] testArray = {
            "java.lang.Object",
            "select cast('ABC' as CLOB) AS C1, cast ('ABC' as DBCLOB(1M) CCSID 1200) AS C2, cast(X'313233' AS BLOB) AS C3, cast(null as CLOB) AS C4, cast (null as DBCLOB(1M) CCSID 1200) AS C5, cast(null AS BLOB) AS C6 from sysibm.sysdummy1",
            "CLOB=ABC",
            "CLOB=ABC",
            "BLOB=313233", 
            "null", 
            "null", 
            "null"};
        testGetObjectNamed(testArray); 
    }
    

    /* Test object 22X with datalink  X8 */ 
    public void Var528()
    {
        String[] testArray = {
            "java.lang.Object",
            "select DLVALUE('file://tmp/x') AS C1, CAST(NULL AS DATALINK) AS C2 from sysibm.sysdummy1",
            "file://TMP/x", 
            "null"};


        testGetObjectNamed(testArray); 
    }

    /* Test object 22X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var529()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.lang.Object",
            "select CAST(CAST(X'1122' AS CHAR(2) FOR BIT DATA) AS ROWID) AS C1, CAST('ABC' as NCHAR(10)) AS C2, CAST('ABC' AS NVARCHAR(100)) AS C3, CAST('ABC' AS NCLOB(100K)) AS C4, CAST(null AS ROWID) AS C5, CAST(null as NCHAR(10)) AS C6, CAST(null AS NVARCHAR(100)) AS C7, CAST(null AS NCLOB(100K)) AS C8 from sysibm.sysdummy1",
            "ROWID=1122",
            "ABC       ",
            "ABC",
            "CLOB=ABC", 
            "null", 
            "null", 
            "null",
            "null"};

        testGetObjectNamed(testArray);
      }
    }




    /* Test java.sql.RowId 23X with SQLXML X0 */ 
    public void Var530()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.sql.RowId",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ) as C1, CAST(null as XML) as C2 from sysibm.sysdummy1", 
              "Data type mismatch", 
              "null",};
           if (checkJdbc40()) 
             testGetObjectNamed(testArray);
        }
    }


    /* Test java.sql.RowId 23X with primitive numbers X1 */ 
    public void Var531()
    {
        String[] testArray = {
            "java.sql.RowId",
            "select cast(1 as SMALLINT) AS C1, cast(2 as INTEGER) AS C2, cast(3 as BIGINT) AS C3, cast(null as SMALLINT) AS C4, cast(null as INTEGER) AS C5, cast(null as BIGINT) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null", 
            "null", 
            "null"};
        if (checkJdbc40()) 
        testGetObjectNamed(testArray); 
    }

    /* Test java.sql.RowId 23X with primitive doubles X2 */ 
    public void Var532()
    {
        String[] testArray = {
            "java.sql.RowId",
            "select cast(1.0 as REAL) AS C1, cast(2.0 as FLOAT) AS C2, cast(3.0 as DOUBLE) AS C3, cast(null as REAL) as C4, cast(null AS FLOAT) AS C5, cast(null as DOUBLE) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null", 
            "null"};
        if (checkJdbc40()) 
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.sql.RowId 23X with decimal X3 */ 
    public void Var533()
    {
        String[] testArray = {
            "java.sql.RowId",
            "select cast(1.0 as DECIMAL(10,1)) AS C1, cast(2.0 as NUMERIC(10,2)) AS C2, cast(3.0 as DECIMAL(10,4)) AS C3, cast(null as DECIMAL(10,1)) AS C4, cast(null as NUMERIC(10,2)) AS C5, cast(null as DECIMAL(10,4)) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null", 
            "null", 
            "null"};
        if (checkJdbc40()) 
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.sql.RowId 23X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var534()
    {
        String[] testArray = {
            "java.sql.RowId",
            "select cast('1' as CHAR(10)) AS C1, cast('2' as GRAPHIC(10) CCSID 1200) AS C2, cast(null as GRAPHIC(10)) AS C3, cast('1' as VARCHAR(10)) AS C4, cast('2' as VARGRAPHIC(10) CCSID 1200) AS C5, cast(null as VARGRAPHIC(10)) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "Data type mismatch",
            "Data type mismatch",
            "null"};
        if (checkJdbc40()) 
        testGetObjectNamed(testArray); 
    }
    


    /* Test java.sql.RowId 23X with BINARY, VARBINARY  X5 */ 
    public void Var535()
    {
        String[] testArray = {
            "java.sql.RowId",
            "select cast(X'01' AS BINARY(1)) AS C1, cast (X'02' AS VARBINARY(20)) AS C2,cast(null AS BINARY(1)) AS C3, cast (null AS VARBINARY(20)) AS C4 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null"};

        String[] testArrayNative = {
            "java.sql.RowId",
            "select cast(X'01' AS BINARY(1)) AS C1, cast (X'02' AS VARBINARY(20)) AS C2,cast(null AS BINARY(1)) AS C3, cast (null AS VARBINARY(20)) AS C4 from sysibm.sysdummy1",
            "ROWID=01",
            "ROWID=02",
            "null",
            "null"};

	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

		    
        if (checkJdbc40()) 
        testGetObjectNamed(testArray); 
    }

    /* Test java.sql.RowId 23X with date, time, timestamp  X6 */ 
    public void Var536()
    {
        String[] testArray = {
            "java.sql.RowId",
            "select cast('1:02:03' as TIME) AS C1, cast ('2011-09-11' as DATE) AS C2, cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) AS C3 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        if (checkJdbc40()) 
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.sql.RowId 23X with clob, dbclob, blob  X7 */ 
    public void Var537()
    {
        String[] testArray = {
            "java.sql.RowId",
            "select cast('1' as CLOB) AS C1, cast ('2' as DBCLOB(1M) CCSID 1200) AS C2, cast(X'03' AS BLOB) AS C3 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            };


        String[] testArrayNative = {
            "java.sql.RowId",
            "select cast('1' as CLOB) AS C1, cast ('2' as DBCLOB(1M) CCSID 1200) AS C2, cast(X'03' AS BLOB) AS C3 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "ROWID=03",
            };

	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

        if (checkJdbc40()) 
        testGetObjectNamed(testArray); 
    }
    

    /* Test java.sql.RowId 23X with datalink  X8 */ 
    public void Var538()
    {
        String[] testArray = {
            "java.sql.RowId",
            "select DLVALUE('file://tmp/x') AS C1 from sysibm.sysdummy1",
            "Data type mismatch"};
        if (checkJdbc40()) 
        testGetObjectNamed(testArray); 
    }

    /* Test java.sql.RowId 23X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var539()
    {
      if (checkRelease710()) {  
    String[] testArray = {
            "java.sql.RowId",
            "select CAST(CAST(X'01' AS CHAR(2) FOR BIT DATA) AS ROWID) AS C1, CAST('1' as NCHAR(10)) AS C2, CAST('1' AS NVARCHAR(100)) AS C3," +
            " CAST('1' AS NCLOB(100K)) AS C4, CAST(null AS ROWID) AS C5, CAST(null as NCHAR(10)) AS C6, CAST(null AS NVARCHAR(100)) AS C7, CAST(null AS NCLOB(100K)) AS C8 from sysibm.sysdummy1",
            "ROWID=0140",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null",
            "null",
            "null"};
    if (checkJdbc40()) 
        testGetObjectNamed(testArray);
      }
    }



    /* Test java.sql.SQLXML 24X with SQLXML X0 */ 
    public void Var540()
    {
      if (checkRelease710()) {  
           String[] testArray = {
              "java.sql.SQLXML",
              "select XMLPARSE( DOCUMENT '<h>hello</h>' ) as C1, CAST(null as XML) as C2 from sysibm.sysdummy1", 
              "SQLXML=<h>hello</h>", 
              "null",};
           if (checkJdbc40()) 
             testGetObjectNamed(testArray);
        }
    }


    /* Test java.sql.SQLXML 24X with primitive numbers X1 */ 
    public void Var541()
    {
        String[] testArray = {
            "java.sql.SQLXML",
            "select cast(1 as SMALLINT) AS C1, cast(2 as INTEGER) AS C2, cast(3 as BIGINT) AS C3, cast(null as SMALLINT) AS C4, cast(null as INTEGER) AS C5, cast(null as BIGINT) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null", 
            "null", 
            "null"};
        if (checkJdbc40()) 
        testGetObjectNamed(testArray); 
    }

    /* Test java.sql.SQLXML 24X with primitive doubles X2 */ 
    public void Var542()
    {
        String[] testArray = {
            "java.sql.SQLXML",
            "select cast(1.0 as REAL) AS C1, cast(2.0 as FLOAT) AS C2, cast(3.0 as DOUBLE) AS C3, cast(null as REAL) as C4, cast(null AS FLOAT) AS C5, cast(null as DOUBLE) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null", 
            "null"};
        if (checkJdbc40()) 
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.sql.SQLXML 24X with decimal X3 */ 
    public void Var543()
    {
        String[] testArray = {
            "java.sql.SQLXML",
            "select cast(1.0 as DECIMAL(10,1)) AS C1, cast(2.0 as NUMERIC(10,2)) AS C2, cast(3.0 as DECIMAL(10,4)) AS C3, cast(null as DECIMAL(10,1)) AS C4, cast(null as NUMERIC(10,2)) AS C5, cast(null as DECIMAL(10,4)) AS C6 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null", 
            "null", 
            "null"};
        if (checkJdbc40()) 
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.sql.SQLXML 24X with char, graphic, varchar, vargraphic   X4 */ 
    public void Var544()
    {
        String[] testArray = {
            "java.sql.SQLXML",
            "select cast('<h>stf</h>' as CHAR(10)) AS C1, "+
	      "cast('<h>stf</h>' as GRAPHIC(10) CCSID 1200) AS C2, "+
	      "cast('<h>nothing</h>' as CHAR(14)) AS C3, "+
              "cast('<h>nothing</h>' as GRAPHIC(14) CCSID 1200) AS C4, "+
              "cast(null as CHAR(10)) AS C5, "+
              "cast(null as GRAPHIC(10)) AS C6, "+
              "cast('<h>h</h>' as VARCHAR(10)) AS C7, "+
              "cast('<h>h</h>' as VARGRAPHIC(10) CCSID 1200)  AS C8, "+
              "cast('<h>1</h>' as VARCHAR(10)) AS C9, "+
              "cast('<h>2</h>' as VARGRAPHIC(10) CCSID 1200) AS C10, "+
	      "cast(null as VARCHAR(10)) AS C11, "+
              "cast(null as VARGRAPHIC(10)) AS C12 from sysibm.sysdummy1",
            "SQLXML=<h>stf</h>",
            "SQLXML=<h>stf</h>",
	    "SQLXML=<h>nothing</h>",
	    "SQLXML=<h>nothing</h>",
	    "null",
            "null",
            "SQLXML=<h>h</h>",
            "SQLXML=<h>h</h>",
            "SQLXML=<h>1</h>",
            "SQLXML=<h>2</h>",
            "null",
	    "null"};

        if (checkJdbc40()) 
        testGetObjectNamed(testArray); 
    }
    


    /* Test java.sql.SQLXML 24X with BINARY, VARBINARY  X5 */ 
    public void Var545()
    {
        String[] testArray = {
            "java.sql.SQLXML",
            "select cast(X'01' AS BINARY(1)) AS C1, cast (X'02' AS VARBINARY(20)) AS C2,cast(null AS BINARY(1)) AS C3, cast (null AS VARBINARY(20)) AS C4 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null"};
        if (checkJdbc40()) 
        testGetObjectNamed(testArray); 
    }

    /* Test java.sql.SQLXML 24X with date, time, timestamp  X6 */ 
    public void Var546()
    {
        String[] testArray = {
            "java.sql.SQLXML",
            "select cast('1:02:03' as TIME) AS C1, cast ('2011-09-11' as DATE) AS C2, cast('2011-09-11-01.02.03.123456' AS TIMESTAMP) AS C3 from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch"};
        if (checkJdbc40()) 
        testGetObjectNamed(testArray); 
    }
    
    /* Test java.sql.SQLXML 24X with clob, dbclob, blob  X7 */ 
    public void Var547()
    {
        String[] testArray = {
            "java.sql.SQLXML",
            "select cast('<h>h</h>' as CLOB) AS C1, "+
                   "cast ('<h>h</h>' as DBCLOB(1M) CCSID 1200) AS C2, "+
                   "cast ('badstuff' as CLOB) AS C3, "+
                   "cast ('badstuff' as DBCLOB(1M) CCSID 1200) AS C4, "+
                   "cast(X'03' AS BLOB) AS C5, "+
                   "cast (null as CLOB) AS C6, "+
                   "cast (null as DBCLOB(1M) CCSID 1200) AS C7, "+
                   "cast(null AS BLOB) AS C8 from sysibm.sysdummy1",
            "SQLXML=<h>h</h>",
            "SQLXML=<h>h</h>",
            "SQLXML=badstuff",
            "Data type mismatch",
            "Data type mismatch",
	    "null",
	    "null",
	    "null",
            };


        String[] testArrayNative = {
            "java.sql.SQLXML",
            "select cast('<h>h</h>' as CLOB) AS C1, "+
                   "cast ('<h>h</h>' as DBCLOB(1M) CCSID 1200) AS C2, "+
                   "cast ('badstuff' as CLOB) AS C3, "+
                   "cast ('badstuff' as DBCLOB(1M) CCSID 1200) AS C4, "+
                   "cast(X'03' AS BLOB) AS C5, "+
                   "cast (null as CLOB) AS C6, "+
                   "cast (null as DBCLOB(1M) CCSID 1200) AS C7, "+
                   "cast(null AS BLOB) AS C8 from sysibm.sysdummy1",
            "SQLXML=<h>h</h>",
            "SQLXML=<h>h</h>",
            "SQLXML=badstuff",
            "SQLXML=badstuff",
            "Data type mismatch",
	    "null",
	    "null",
	    "null",
            };


	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

        if (checkJdbc40()) 
        testGetObjectNamed(testArray); 
    }
    

    /* Test java.sql.SQLXML 24X with datalink  X8 */ 
    public void Var548()
    {
        String[] testArray = {
            "java.sql.SQLXML",
            "select DLVALUE('file://tmp/x') AS C1 from sysibm.sysdummy1",
            "Data type mismatch"};
        if (checkJdbc40()) 
        testGetObjectNamed(testArray); 
    }

    /* Test java.sql.SQLXML 24X with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var549()
    {
      if (checkRelease710()) {
    String[] testArray = {
            "java.sql.SQLXML",
            "select CAST(CAST(X'01' AS CHAR(2) FOR BIT DATA) AS ROWID) AS C1, "+
                   "CAST('<h>h</h>' as NCHAR(10)) AS C2, "+
                   "CAST('<h>h</h>' AS NVARCHAR(100)) AS C3," +
                   "CAST('<h>h</h>' AS NCLOB(100K)) AS C4, "+
                   "CAST('<h>1</h>' as NCHAR(10)) AS C5, "+
                   "CAST('<h>1</h>' AS NVARCHAR(100)) AS C6," +
                   "CAST('<h>1</h>' AS NCLOB(100K)) AS C7, "+
                   "CAST(null AS ROWID) AS C8, CAST(null as NCHAR(10)) AS C9, CAST(null AS NVARCHAR(100)) AS C10, CAST(null AS NCLOB(100K)) AS C11 from sysibm.sysdummy1",
            "Data type mismatch",
            "SQLXML=<h>h</h>",
            "SQLXML=<h>h</h>",
            "SQLXML=<h>h</h>",
            "SQLXML=<h>1</h>",
            "SQLXML=<h>1</h>",
            "SQLXML=<h>1</h>",
            "null",
            "null",
            "null",
            "null"};

    String[] testArrayNative = {
            "java.sql.SQLXML",
            "select CAST(CAST(X'01' AS CHAR(2) FOR BIT DATA) AS ROWID) AS C1, "+
                   "CAST('<h>h</h>' as NCHAR(10)) AS C2, "+
                   "CAST('<h>h</h>' AS NVARCHAR(100)) AS C3," +
                   "CAST('<h>h</h>' AS NCLOB(100K)) AS C4, "+
                   "CAST('<h>1</h>' as NCHAR(10)) AS C5, "+
                   "CAST('<h>1</h>' AS NVARCHAR(100)) AS C6," +
                   "CAST('<h>1</h>' AS NCLOB(100K)) AS C7, "+
                   "CAST(null AS ROWID) AS C8, CAST(null as NCHAR(10)) AS C9, CAST(null AS NVARCHAR(100)) AS C10, CAST(null AS NCLOB(100K)) AS C11 from sysibm.sysdummy1",
            "Data type mismatch",
            "SQLXML=<h>h</h>",
            "SQLXML=<h>h</h>",
            "SQLXML=<h>h</h>",
            "SQLXML=<h>1</h>",
            "SQLXML=<h>1</h>",
            "SQLXML=<h>1</h>",
            "null",
            "null",
            "null",
            "null"};



	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		testArray = testArrayNative; 
	    } 

    if (checkJdbc40()) 
        testGetObjectNamed(testArray);
      }
    }


    /* Test boolean with boolean  X8 */ 
  public void Var550() {
    if (checkBooleanSupport()) {

      String[] testArray = { "java.lang.Boolean",
          "select c1,c2,c3 from " + collection_ + ".JDRSGOBOOL", 
          "true",
          "false", 
          "null" };

      testGetObjectNamed(testArray);
    }

  }
 
  public void Var551() {
    if (checkBooleanSupport()) {

      String[] testArray = { "java.lang.String",
          "select c1,c2,c3 from " + collection_ + ".JDRSGOBOOL", 
          JDRSTest.BOOLEAN_TRUE_STRING ,
          JDRSTest.BOOLEAN_FALSE_STRING , 
          "null" };

      testGetObjectNamed(testArray);
    }

  }
  
  public void Var552() {
    if (checkBooleanSupport()) {

      String[] testArray = { "java.lang.Short",
          "select c1,c2,c3 from " + collection_ + ".JDRSGOBOOL", 
          "1",
          "0", 
          "null" };

      testGetObjectNamed(testArray);
    }

  }

  public void Var553() {
    if (checkBooleanSupport()) {

      String[] testArray = { "java.math.BigDecimal",
          "select c1,c2,c3 from " + collection_ + ".JDRSGOBOOL", 
          "1",
          "0", 
          "null" };

      testGetObjectNamed(testArray);
    }

  }    
    public void Var554() {
      if (checkBooleanSupport()) {

        String[] testArray = { "java.lang.Byte",
            "select c1,c2,c3 from " + collection_ + ".JDRSGOBOOL", 
            "1",
            "0", 
            "null" };

        testGetObjectNamed(testArray);
      }

    }

    
    public void Var555() {
      if (checkBooleanSupport()) {

        String[] testArray = { "java.lang.Short",
            "select c1,c2,c3 from " + collection_ + ".JDRSGOBOOL", 
            "1",
            "0", 
            "null" };

        testGetObjectNamed(testArray);
      }

    }


    public void Var556() {
      if (checkBooleanSupport()) {

        String[] testArray = { "java.lang.Integer",
            "select c1,c2,c3 from " + collection_ + ".JDRSGOBOOL", 
            "1",
            "0", 
            "null" };

        testGetObjectNamed(testArray);
      }

    }

    public void Var557() {
      if (checkBooleanSupport()) {

        String[] testArray = { "java.lang.Long",
            "select c1,c2,c3 from " + collection_ + ".JDRSGOBOOL", 
            "1",
            "0", 
            "null" };

        testGetObjectNamed(testArray);
      }

    }

    public void Var558() {
      if (checkBooleanSupport()) {

        String[] testArray = { "java.lang.Float",
            "select c1,c2,c3 from " + collection_ + ".JDRSGOBOOL", 
            "1.0",
            "0.0", 
            "null" };

        testGetObjectNamed(testArray);
      }

    }

    
    public void Var559() {
      if (checkBooleanSupport()) {

        String[] testArray = { "java.lang.Double",
            "select c1,c2,c3 from " + collection_ + ".JDRSGOBOOL", 
            "1.0",
            "0.0", 
            "null" };

        testGetObjectNamed(testArray);
      }

    }

    public void Var560() {
        notApplicable();
    }

       /* Test LocalDate with primitive doubles X2 */ 
       public void Var561()
    {
         if (checkJdbc42()) {
  String[] testArray = {
      "java.time.LocalDate",
      "select cast(1 as SMALLINT), cast(2 as INTEGER), cast(3 as BIGINT), cast(null as SMALLINT), cast(null as INTEGER), cast(null as BIGINT) from sysibm.sysdummy1",
      "Data type mismatch",
      "Data type mismatch",
      "Data type mismatch", 
      "null",
      "null",
      "null"};
        testGetObject(testArray); 
         }
    }

    /* Test LocalDate with primitive doubles X2 */ 
    public void Var562()
    {
         if (checkJdbc42()) {
        String[] testArray = {
            "java.time.LocalDate",
            "select cast(1.0 as REAL), cast(2.0 as FLOAT), cast(3.0 as DOUBLE), cast(null as REAL), cast(null as FLOAT), cast(null as DOUBLE)  from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null",
            "null"};
        testGetObject(testArray); 
    }
    }    
    /* Test LocalDate with decimal X3 */ 
    public void Var563()
    {
              if (checkJdbc42()) {
   String[] testArray = {
            "java.time.LocalDate",
            "select cast(1.0 as DECIMAL(10,1)), cast(2.0 as NUMERIC(10,2)), cast(3.0 as DECIMAL(10,4)), cast(null as DECIMAL(10,1)), cast(null as NUMERIC(10,2)), cast(null as DECIMAL(10,4)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null",
            "null"};
        testGetObject(testArray); 
    }
    }    
    
    /* Test LocalDate with char, graphic, varchar, vargraphic   X4 */ 
    public void Var564()
    {
              if (checkJdbc42()) {
   String[] testArray = {
            "java.time.LocalDate",
            "select cast('ABC' as CHAR(10)), cast('ABC' as GRAPHIC(10) CCSID 1200), cast(null as GRAPHIC(10)), cast('ABC' as VARCHAR(10)), cast('ABC' as VARGRAPHIC(10) CCSID 1200), cast(null as VARGRAPHIC(10)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "Data type mismatch",
            "Data type mismatch",
            "null"};
        testGetObject(testArray); 
    }
    }    

    /* Test LocalDate with char, graphic, varchar, vargraphic   X4 */ 
    public void Var565()
    {
              if (checkJdbc42()) {
   String[] testArray = {
            "java.time.LocalDate",
            "select cast('2021-02-02' as CHAR(10)), cast('2021-02-02' as GRAPHIC(10) CCSID 1200), cast(null as GRAPHIC(10)), cast('2021-02-02' as VARCHAR(10)), cast('2021-02-02' as VARGRAPHIC(10) CCSID 1200), cast(null as VARGRAPHIC(10)) from sysibm.sysdummy1",
            "2021-02-02",
            "2021-02-02",
            "null",
            "2021-02-02",
            "2021-02-02",
            "null"};
        testGetObject(testArray); 
    }
    }    

    
    
    /* Test LocalDate with BINARY, VARBINARY  X5 */   
    public void Var566()
    {
              if (checkJdbc42()) {
   String[] testArray = {
            "java.time.LocalDate",
            "select cast(X'313233' AS BINARY(10)), cast (X'313233' AS VARBINARY(20)),cast(null AS BINARY(10)), cast (null AS VARBINARY(20)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch", 
            "null", 
            "null"};


        testGetObject(testArray); 
    }
    }
    /* Test LocalDate with date, time, timestamp  X6 */ 
    public void Var567()
    {
         if (checkJdbc42()) {
        String[] testArray = {
            "java.time.LocalDate",
            "select cast('1:02:03' as TIME), cast ('2011-09-11' as DATE), cast('2011-09-11-01.02.03.123456' AS TIMESTAMP), cast(null as TIME), cast (null as DATE), cast(null AS TIMESTAMP) from sysibm.sysdummy1",
            "Data type mismatch",
            "2011-09-11",
            "2011-09-11", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
         }
         }

            /* Test LocalDate with clob, dbclob, blob  X7 */ 
    public void Var568()
    {
         if (checkJdbc42()) {
        String[] testArray = {
            "java.time.LocalDate",
            "select cast('ABC' as CLOB), cast ('ABC' as DBCLOB(1M) CCSID 1200), cast(X'313233' AS BLOB), cast(null as CLOB), cast (null as DBCLOB(1M) CCSID 1200), cast(null AS BLOB) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "null", 
            "null", 
            "null"};

       

        testGetObject(testArray); 
    }
    }    


    /* Test LocalDate with clob, dbclob, blob  X7 */ 
    public void Var569()
    {
         if (checkJdbc42()) {
        String[] testArray = {
            "java.time.LocalDate",
            "select cast('2022-02-02' as CLOB), cast ('2022-02-02' as DBCLOB(1M) CCSID 1200), cast(X'20220202' AS BLOB), cast(null as CLOB), cast (null as DBCLOB(1M) CCSID 1200), cast(null AS BLOB) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "null", 
            "null", 
            "null"};

       

        testGetObject(testArray); 
    }
    }    

    /* Test LocalDate with datalink  X8 */ 
    public void Var570()
    {
         if (checkJdbc42()) {
        String[] testArray = {
            "java.time.LocalDate",
            "select DLVALUE('file://tmp/x'), CAST(NULL AS DATALINK) from sysibm.sysdummy1",
            "Data type mismatch", 
            "null"};
        testGetObject(testArray); 
    }
    }
    /* Test LocalDate with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var571()
    {
         if (checkJdbc42()) {
    String[] testArray = {
            "java.time.LocalDate",
            "select CAST(CAST(X'1122' AS CHAR(2) FOR BIT DATA) AS ROWID), CAST('ABC' as NCHAR(10)), CAST('ABC' AS NVARCHAR(100)), CAST('ABC' AS NCLOB(100K)), CAST(null AS ROWID), CAST(null as NCHAR(10)), CAST(null AS NVARCHAR(100)), CAST(null AS NCLOB(100K)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "null", 
            "null", 
            "null",
            "null"};
        testGetObject(testArray);
      }
    }

 public void Var572()
    {
         if (checkJdbc42()) {
    String[] testArray = {
            "java.time.LocalDate",
            "select CAST(CAST('20220222' AS CHAR(2) FOR BIT DATA) AS ROWID), "
            + "CAST('2022-02-22' as NCHAR(10)), "
            + "CAST('2022-02-22' AS NVARCHAR(100)), "
            + "CAST('2022-02-22' AS NCLOB(100K)), "
            + "CAST(null AS ROWID), CAST(null as NCHAR(10)), CAST(null AS NVARCHAR(100)), CAST(null AS NCLOB(100K)) from sysibm.sysdummy1",
            "Data type mismatch",
            "2022-02-22",
            "2022-02-22",
            "Data type mismatch", 
            "null", 
            "null",
            "null",
            "null"};
        testGetObject(testArray);
      }
    }




       /* Test LocalTime with primitive doubles X2 */ 
       public void Var573()
    {
         if (checkJdbc42()) {
  String[] testArray = {
      "java.time.LocalTime",
      "select cast(1 as SMALLINT), cast(2 as INTEGER), cast(3 as BIGINT), cast(null as SMALLINT), cast(null as INTEGER), cast(null as BIGINT) from sysibm.sysdummy1",
      "Data type mismatch",
      "Data type mismatch",
      "Data type mismatch", 
      "null",
      "null",
      "null"};
        testGetObject(testArray); 
         }
    }

    /* Test LocalTime with primitive doubles X2 */ 
    public void Var574()
    {
         if (checkJdbc42()) {
        String[] testArray = {
            "java.time.LocalTime",
            "select cast(1.0 as REAL), cast(2.0 as FLOAT), cast(3.0 as DOUBLE), cast(null as REAL), cast(null as FLOAT), cast(null as DOUBLE)  from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null",
            "null"};
        testGetObject(testArray); 
    }
    }    
    /* Test LocalTime with decimal X3 */ 
    public void Var575()
    {
              if (checkJdbc42()) {
   String[] testArray = {
            "java.time.LocalTime",
            "select cast(1.0 as DECIMAL(10,1)), cast(2.0 as NUMERIC(10,2)), cast(3.0 as DECIMAL(10,4)), cast(null as DECIMAL(10,1)), cast(null as NUMERIC(10,2)), cast(null as DECIMAL(10,4)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null",
            "null"};
        testGetObject(testArray); 
    }
    }    
    
    /* Test LocalTime with char, graphic, varchar, vargraphic   X4 */ 
    public void Var576()
    {
              if (checkJdbc42()) {
   String[] testArray = {
            "java.time.LocalTime",
            "select cast('ABC' as CHAR(10)), cast('ABC' as GRAPHIC(10) CCSID 1200), cast(null as GRAPHIC(10)), cast('ABC' as VARCHAR(10)), cast('ABC' as VARGRAPHIC(10) CCSID 1200), cast(null as VARGRAPHIC(10)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "Data type mismatch",
            "Data type mismatch",
            "null"};
        testGetObject(testArray); 
    }
    }    

    /* Test LocalTime with char, graphic, varchar, vargraphic   X4 */ 
    public void Var577()
    {
              if (checkJdbc42()) {
   String[] testArray = {
            "java.time.LocalTime",
            "select cast('10:11:12' as CHAR(10)), cast('10:11:12' as GRAPHIC(10) CCSID 1200), cast(null as GRAPHIC(10)), cast('10:11:12' as VARCHAR(10)), cast('10:11:12' as VARGRAPHIC(10) CCSID 1200), cast(null as VARGRAPHIC(10)) from sysibm.sysdummy1",
            "10:11:12",
            "10:11:12",
            "null",
            "10:11:12",
            "10:11:12",
            "null"};
        testGetObject(testArray); 
    }
    }    

    
    
    /* Test LocalTime with BINARY, VARBINARY  X5 */   
    public void Var578()
    {
              if (checkJdbc42()) {
   String[] testArray = {
            "java.time.LocalTime",
            "select cast(X'313233' AS BINARY(10)), cast (X'313233' AS VARBINARY(20)),cast(null AS BINARY(10)), cast (null AS VARBINARY(20)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch", 
            "null", 
            "null"};


        testGetObject(testArray); 
    }
    }
    /* Test LocalTime with date, time, timestamp  X6 */ 
    public void Var579()
    {
         if (checkJdbc42()) {
        String[] testArray = {
            "java.time.LocalTime",
            "select cast('1:02:03' as TIME), cast ('2011-09-11' as DATE), cast('2011-09-11-01.02.03.123456' AS TIMESTAMP), cast(null as TIME), cast (null as DATE), cast(null AS TIMESTAMP) from sysibm.sysdummy1",
            "01:02:03",
            "Data type mismatch",
            "01:02:03", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
         }
         }

            /* Test LocalTime with clob, dbclob, blob  X7 */ 
    public void Var580()
    {
         if (checkJdbc42()) {
        String[] testArray = {
            "java.time.LocalTime",
            "select cast('ABC' as CLOB), cast ('ABC' as DBCLOB(1M) CCSID 1200), cast(X'313233' AS BLOB), cast(null as CLOB), cast (null as DBCLOB(1M) CCSID 1200), cast(null AS BLOB) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "null", 
            "null", 
            "null"};

       

        testGetObject(testArray); 
    }
    }    


    /* Test LocalTime with clob, dbclob, blob  X7 */ 
    public void Var581()
    {
         if (checkJdbc42()) {
        String[] testArray = {
            "java.time.LocalTime",
            "select cast('10:11:12' as CLOB), cast ('10:11:12' as DBCLOB(1M) CCSID 1200), cast(X'20220202' AS BLOB), cast(null as CLOB), cast (null as DBCLOB(1M) CCSID 1200), cast(null AS BLOB) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "null", 
            "null", 
            "null"};

       

        testGetObject(testArray); 
    }
    }    

    /* Test LocalTime with datalink  X8 */ 
    public void Var582()
    {
         if (checkJdbc42()) {
        String[] testArray = {
            "java.time.LocalTime",
            "select DLVALUE('file://tmp/x'), CAST(NULL AS DATALINK) from sysibm.sysdummy1",
            "Data type mismatch", 
            "null"};
        testGetObject(testArray); 
    }
    }
    /* Test LocalTime with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var583()
    {
         if (checkJdbc42()) {
    String[] testArray = {
            "java.time.LocalTime",
            "select CAST(CAST(X'1122' AS CHAR(2) FOR BIT DATA) AS ROWID), CAST('ABC' as NCHAR(10)), CAST('ABC' AS NVARCHAR(100)), CAST('ABC' AS NCLOB(100K)), CAST(null AS ROWID), CAST(null as NCHAR(10)), CAST(null AS NVARCHAR(100)), CAST(null AS NCLOB(100K)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "null", 
            "null", 
            "null",
            "null"};
        testGetObject(testArray);
      }
    }

 public void Var584()
    {
         if (checkJdbc42()) {
    String[] testArray = {
            "java.time.LocalTime",
            "select CAST(CAST('10:11:12' AS CHAR(2) FOR BIT DATA) AS ROWID), "
            + "CAST('10:11:12' as NCHAR(10)), "
            + "CAST('10:11:12' AS NVARCHAR(100)), "
            + "CAST('10:11:12' AS NCLOB(100K)), "
            + "CAST(null AS ROWID), CAST(null as NCHAR(10)), CAST(null AS NVARCHAR(100)), CAST(null AS NCLOB(100K)) from sysibm.sysdummy1",
            "Data type mismatch",
            "10:11:12",
            "10:11:12",
            "Data type mismatch", 
            "null", 
            "null",
            "null",
            "null"};
        testGetObject(testArray);
      }
    }



        /* Test LocalDateTime with primitive doubles X2 */ 
       public void Var585()
    {
         if (checkJdbc42()) {
  String[] testArray = {
      "java.time.LocalDateTime",
      "select cast(1 as SMALLINT), cast(2 as INTEGER), cast(3 as BIGINT), cast(null as SMALLINT), cast(null as INTEGER), cast(null as BIGINT) from sysibm.sysdummy1",
      "Data type mismatch",
      "Data type mismatch",
      "Data type mismatch", 
      "null",
      "null",
      "null"};
        testGetObject(testArray); 
         }
    }

    /* Test LocalDateTime with primitive doubles X2 */ 
    public void Var586()
    {
         if (checkJdbc42()) {
        String[] testArray = {
            "java.time.LocalDateTime",
            "select cast(1.0 as REAL), cast(2.0 as FLOAT), cast(3.0 as DOUBLE), cast(null as REAL), cast(null as FLOAT), cast(null as DOUBLE)  from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null",
            "null"};
        testGetObject(testArray); 
    }
    }    
    /* Test LocalDateTime with decimal X3 */ 
    public void Var587()
    {
              if (checkJdbc42()) {
   String[] testArray = {
            "java.time.LocalDateTime",
            "select cast(1.0 as DECIMAL(10,1)), cast(2.0 as NUMERIC(10,2)), cast(3.0 as DECIMAL(10,4)), cast(null as DECIMAL(10,1)), cast(null as NUMERIC(10,2)), cast(null as DECIMAL(10,4)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "null",
            "null"};
        testGetObject(testArray); 
    }
    }    
    
    /* Test LocalDateTime with char, graphic, varchar, vargraphic   X4 */ 
    public void Var588()
    {
              if (checkJdbc42()) {
   String[] testArray = {
            "java.time.LocalDateTime",
            "select cast('ABC' as CHAR(10)), cast('ABC' as GRAPHIC(10) CCSID 1200), cast(null as GRAPHIC(10)), cast('ABC' as VARCHAR(10)), cast('ABC' as VARGRAPHIC(10) CCSID 1200), cast(null as VARGRAPHIC(10)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "null",
            "Data type mismatch",
            "Data type mismatch",
            "null"};
        testGetObject(testArray); 
    }
    }    

    /* Test LocalDateTime with char, graphic, varchar, vargraphic   X4 */ 
    public void Var589()
    {
              if (checkJdbc42()) {
   String[] testArray = {
            "java.time.LocalDateTime",
            "select cast('2021-02-02 10:11:12.123' as CHAR(26)), cast('2021-02-02 10:11:12.123' as GRAPHIC(26) CCSID 1200), cast(null as GRAPHIC(26)), "
            + "cast('2021-02-02 10:11:12.123' as VARCHAR(26)), cast('2021-02-02 10:11:12.123' as VARGRAPHIC(26) CCSID 1200), cast(null as VARGRAPHIC(26)) from sysibm.sysdummy1",
            "2021-02-02T10:11:12.123",
            "2021-02-02T10:11:12.123",
            "null",
            "2021-02-02T10:11:12.123",
            "2021-02-02T10:11:12.123",
            "null"};
        testGetObject(testArray); 
    }
    }    

    
    
    /* Test LocalDateTime with BINARY, VARBINARY  X5 */   
    public void Var590()
    {
              if (checkJdbc42()) {
   String[] testArray = {
            "java.time.LocalDateTime",
            "select cast(X'313233' AS BINARY(10)), cast (X'313233' AS VARBINARY(20)),cast(null AS BINARY(10)), cast (null AS VARBINARY(20)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch", 
            "null", 
            "null"};


        testGetObject(testArray); 
    }
    }
    /* Test LocalDateTime with date, time, timestamp  X6 */ 
    public void Var591()
    {
         if (checkJdbc42()) {
        String[] testArray = {
            "java.time.LocalDateTime",
            "select cast('1:02:03' as TIME), cast ('2011-09-11' as DATE), cast('2011-09-11-01.02.03.123456' AS TIMESTAMP), cast(null as TIME), cast (null as DATE), cast(null AS TIMESTAMP) from sysibm.sysdummy1",
            "1970-01-01T01:02:03",
            "2011-09-11T00:00",
            "2011-09-11T01:02:03.123456", 
            "null", 
            "null", 
            "null"};
        testGetObject(testArray); 
         }
         }

            /* Test LocalDateTime with clob, dbclob, blob  X7 */ 
    public void Var592()
    {
         if (checkJdbc42()) {
        String[] testArray = {
            "java.time.LocalDateTime",
            "select cast('ABC' as CLOB), cast ('ABC' as DBCLOB(1M) CCSID 1200), cast(X'313233' AS BLOB), cast(null as CLOB), cast (null as DBCLOB(1M) CCSID 1200), cast(null AS BLOB) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "null", 
            "null", 
            "null"};

       

        testGetObject(testArray); 
    }
    }    


    /* Test LocalDateTime with clob, dbclob, blob  X7 */ 
    public void Var593()
    {
         if (checkJdbc42()) {
        String[] testArray = {
            "java.time.LocalDateTime",
            "select cast('2022-02-02' as CLOB), cast ('2022-02-02' as DBCLOB(1M) CCSID 1200), cast(X'20220202' AS BLOB), cast(null as CLOB), cast (null as DBCLOB(1M) CCSID 1200), cast(null AS BLOB) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "null", 
            "null", 
            "null"};

       

        testGetObject(testArray); 
    }
    }    

    /* Test LocalDateTime with datalink  X8 */ 
    public void Var594()
    {
         if (checkJdbc42()) {
        String[] testArray = {
            "java.time.LocalDateTime",
            "select DLVALUE('file://tmp/x'), CAST(NULL AS DATALINK) from sysibm.sysdummy1",
            "Data type mismatch", 
            "null"};
        testGetObject(testArray); 
    }
    }
    /* Test LocalDateTime with rowid, nchar, nvarchar, nclob X9 */ 
    public void Var595()
    {
         if (checkJdbc42()) {
    String[] testArray = {
            "java.time.LocalDateTime",
            "select CAST(CAST(X'1122' AS CHAR(2) FOR BIT DATA) AS ROWID), CAST('ABC' as NCHAR(10)), CAST('ABC' AS NVARCHAR(100)), CAST('ABC' AS NCLOB(100K)), CAST(null AS ROWID), CAST(null as NCHAR(10)), CAST(null AS NVARCHAR(100)), CAST(null AS NCLOB(100K)) from sysibm.sysdummy1",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch",
            "Data type mismatch", 
            "null", 
            "null", 
            "null",
            "null"};
        testGetObject(testArray);
      }
    }

 public void Var596()
    {
         if (checkJdbc42()) {
    String[] testArray = {
            "java.time.LocalDateTime",
            "select CAST(CAST('20220222' AS CHAR(2) FOR BIT DATA) AS ROWID), "
            + "CAST('2011-09-11 01.02.03.123456' as NCHAR(26)), "
            + "CAST('2011-09-11 01.02.03.123456' AS NVARCHAR(100)), "
            + "CAST('2011-09-11 01.02.03.123456' AS NCLOB(100K)), "
            + "CAST(null AS ROWID), CAST(null as NCHAR(10)), CAST(null AS NVARCHAR(100)), CAST(null AS NCLOB(100K)) from sysibm.sysdummy1",
            "Data type mismatch",
            "2011-09-11T01:02:03.123456",
            "2011-09-11T01:02:03.123456",
            "Data type mismatch", 
            "null", 
            "null",
            "null",
            "null"};
        testGetObject(testArray);
      }
    }







  


}



