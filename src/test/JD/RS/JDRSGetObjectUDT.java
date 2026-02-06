///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetObjectUDT.java
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

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.DataTruncation;
import java.sql.Date;

import java.sql.ResultSet;

import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable; import java.util.Vector;
import java.util.Map;



/**
Testcase JDRSGetObject.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>getObject()
</ul>
**/
public class JDRSGetObjectUDT
extends JDTestcase {


    private static final byte[] eleven = { (byte) 'E', (byte) 'l', (byte) 'e', (byte) 'v', (byte) 'e',
        (byte) 'n', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ',
        (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ',
        (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' '};

    private static final byte[] twelve = { (byte) 'T', (byte) 'w', (byte) 'e', (byte) 'l', (byte) 'v',
        (byte) 'e'};



    // Private data.
    private Statement           statement_;
    private Statement           statement0_;
    private ResultSet           rs_;
    java.util.Map<String, Class<?>>               map_;
    private boolean canRun = false; 

/**
Constructor.
**/
    public JDRSGetObjectUDT (AS400 systemObject,
                          Hashtable<String,Vector<String>> namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDRSGetObject",
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
                     
                     ;
        connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
	setAutoCommit(connection_, false); // @E1A

	//
	// Create and add the mapping function for UDTs
	//
	if ( (getDriver()  == JDTestDriver.DRIVER_NATIVE) &&
	     (true)) {
	    canRun=true; 
	    map_ = connection_.getTypeMap();
	    // SSN is a CHAR(9) 
	    map_.put(collection_ + ".SSN", Class.forName("test.JDSSN")); 
	    connection_.setTypeMap(map_); 
	}
	
        statement0_ = connection_.createStatement ();

        if (isJdbc20 ()) {
            statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                      ResultSet.CONCUR_UPDATABLE);
            statement_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_GET
                                      + " (C_KEY) VALUES ('DUMMYROW_GETOBJU')");
            rs_ = statement_.executeQuery ("SELECT * FROM "
                                           + JDRSTest.RSTEST_GET + " FOR UPDATE");        
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
	if (canRun) { 
	    try {
		Statement s = connection_.createStatement ();
		ResultSet rs = s.executeQuery ("SELECT C_DISTINCT FROM "
					       + JDRSTest.RSTEST_GET);
		rs.next ();
		rs.close ();
		rs.getObject (1, map_ );
		failed ("Didn't throw SQLException");
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}
    }



/**
getObject() - Should throw exception when cursor is not pointing
to a row.
**/
    public void Var002()
    {
	if (canRun) { 
	    try {
		ResultSet rs = statement0_.executeQuery ("SELECT C_DISTINCT FROM "
							 + JDRSTest.RSTEST_GET);
		Object s = rs.getObject (1, map_);
		failed ("Didn't throw SQLException s="+s);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}

    }



/**
getObject() - Should throw an exception when the column
is an invalid index.
**/
    public void Var003()
    {
	if (canRun) { 
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT C_DISTINCT FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            Object s = rs.getObject (100, map_);
            failed ("Didn't throw SQLException s="+s);
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}

    }



/**
getObject() - Should throw an exception when the column
is 0.
**/
    public void Var004()
    {
	if (canRun) { 
	    try {
		ResultSet rs = statement0_.executeQuery ("SELECT C_DISTINCT FROM "
							 + JDRSTest.RSTEST_GET);
		JDRSTest.position0 (rs, "DATE_1998");
		Object s = rs.getObject (0, map_);
		failed ("Didn't throw SQLException s="+s);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}

    }



/**
getObject() - Should throw an exception when the column
is -1.
**/
    public void Var005()
    {
	if (canRun) { 
	    try {
		ResultSet rs = statement0_.executeQuery ("SELECT C_DISTINCT FROM "
							 + JDRSTest.RSTEST_GET);
		JDRSTest.position0 (rs, "DATE_1998");
		Object s = rs.getObject (-1, map_);
		failed ("Didn't throw SQLException s="+s);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}

    }



/**
getObject() - Should work when the column index is valid.
**/
    public void Var006()
    {
	if (canRun) { 
	    try {
		ResultSet rs = statement0_.executeQuery ("SELECT C_DISTINCT FROM "
							 + JDRSTest.RSTEST_GET);
		JDRSTest.position0 (rs, "DATE_1998");
		Object s = rs.getObject (1, map_);
		assertCondition (s.equals ("DATE_1998"), "s("+s+") != ??? ");
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}

    }



/**
getObject() with a column index and a type map - Should throw an exception
if the type map is null.
**/
    public void Var007()
    {
	if (canRun) { 
	    if (checkJdbc20 ()) {
		try {
		    ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							     + JDRSTest.RSTEST_GET);
		    JDRSTest.position0 (rs, "DATE_2000");
		    Object s = rs.getObject (1, (Map<String, Class<?>>) null);
		    failed ("Didn't throw SQLException s="+s);
		} catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}

    }



/**
getObject() with a column index and a type map - Should work when the column 
index and type map are valid.
**/
    public void Var008()
    {
	if (canRun) { 
	    if (checkJdbc20 ()) {
		try {
   		   JDRSTest.position (rs_, "DATE_2000");
		   Object s = rs_.getObject (1, new HashMap<String, Class<?>> ());
		   assertCondition (s.equals ("DATE_2000"));
		} catch (Exception e) {
		    failed (e, "Unexpected Exception");
		}
	    }
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}

    }

/**
getObject() - Should throw an exception when the column
name is null.
**/
    public void Var009()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC throws null pointer exception when column name is null "); 
      } else { 
        
        if (canRun) { 
          try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            Object s = rs.getObject (null, map_);
            failed ("Didn't throw SQLException s="+s);
          } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
          }
        } else {
          notApplicable("V5R4 native driver type mapping test"); 
        }
        
      }
    }

/**
getObject() - Should throw an exception when the column
name is an empty string.
**/
    public void Var010()
    {
     if (canRun) { 
	 try {
	     ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
						      + JDRSTest.RSTEST_GET); 
	     JDRSTest.position0 (rs, "DATE_1998");
	     Object s = rs.getObject ("", map_);
	     failed ("Didn't throw SQLException s="+s);
	 } catch (Exception e) {
	     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	 }
     } else {
	 notApplicable("V5R4 native driver type mapping test"); 
     }
 }



/**
getObject() - Should throw an exception when the column
name is invalid.
**/
    public void Var011()
    {
     if (canRun) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET); 
            JDRSTest.position0 (rs, "DATE_1998");
            Object s = rs.getObject ("INVALID", map_);
            failed ("Didn't throw SQLException s="+s);
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
     } else {
	 notApplicable("V5R4 native driver type mapping test"); 
     }
    }



/**
getObject() - Should work when the column name is valid.
**/
    public void Var012()
    {
	if (canRun) { 
	    try {
		ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							 + JDRSTest.RSTEST_GET); 
		JDRSTest.position0 (rs, "DATE_1998");
		Object s = rs.getObject ("C_KEY");
		assertCondition (s.equals ("DATE_1998"));
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}
    }


/**
getObject() with a column name and a type map - Should throw an exception
if the type map is null.
**/
    public void Var013()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, "DATE_2000");
                Object s = rs_.getObject ("C_KEY", (Map<String, Class<?>>) null);
                failed ("Didn't throw SQLException s="+s);
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getObject() with a column name and a type map - Should work when the column 
index and type map are valid.
**/
    public void Var014()
    {
	if (canRun) { 
	    if (checkJdbc20 ()) {
		try {

		JDRSTest.position (rs_, "DATE_2000");
		Object s = rs_.getObject ("C_KEY", new Hashtable<String, Class<?>> ());
		assertCondition (s.equals ("DATE_2000"));


		} catch (Exception e) {
		    failed (e, "Unexpected Exception");
		}
	    }
     } else {
	 notApplicable("V5R4 native driver type mapping test"); 
     }

    }



/**
getObject() - Should work when an update is pending.
**/
    public void Var015()
    {
	if (canRun) { 
	    if (checkJdbc20 ()) {
		try {
		    JDRSTest.position (rs_, "UPDATE_SANDBOX");
		    rs_.updateObject ("C_VARCHAR_50", "Cam");
		    Object v = rs_.getObject ("C_VARCHAR_50", map_);
		    assertCondition (v.equals ("Cam"));
		} catch (Exception e) {
		    failed (e, "Unexpected Exception");
		}
	    }
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}

    }



/**
getObject() - Should work when an update has been done.
**/
    public void Var016()
    {
	if (canRun) { 
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, "UPDATE_SANDBOX");
                rs_.updateObject ("C_VARCHAR_50", "Jeru");
                rs_.updateRow ();
                Object v = rs_.getObject ("C_VARCHAR_50",map_);
                assertCondition (v.equals ("Jeru"));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}

    }



/**
getObject() - Should work when the current row is the insert
row, when an insert is pending.
**/
    public void Var017()
    {
	if (canRun) { 
        if (checkJdbc20 ()) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            notApplicable("JCC does not support moveToInserRow"); 
            return; 
          }
            try {
                rs_.moveToInsertRow ();
                rs_.updateObject ("C_VARCHAR_50", "Visitors");
                Object v = rs_.getObject ("C_VARCHAR_50", map_);
                assertCondition (v.equals ("Visitors"));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}

    }



/**
getObject() - Should work when the current row is the insert
row, when an insert has been done.
**/
    public void Var018()
    {
	if (canRun) { 
	    if (checkJdbc20 ()) {
                  if (getDriver() == JDTestDriver.DRIVER_JCC) {
                    notApplicable("JCC does not support moveToInserRow"); 
                    return; 
                  }
		try {
		    rs_.moveToInsertRow ();
		    rs_.updateObject ("C_VARCHAR_50", "Twins");
		    rs_.insertRow ();
		    Object v = rs_.getObject ("C_VARCHAR_50", map_);
		    assertCondition (v.equals ("Twins"));
		} catch (Exception e) {
		    failed (e, "Unexpected Exception");
		}
	    }
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}

    }



/**
getObject() - Should throw an exception on a deleted row.
**/
    public void Var019()
    {
	if (canRun) { 
        if (checkJdbc20 ()) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) { 
            notApplicable("JCC doesn't throw exception for get on deleted row");
            return; 
          }
            try {
                JDRSTest.position (rs_, "DUMMYROW_GETOBJU");
		rs_.deleteRow ();
		Object v = rs_.getObject ("C_VARCHAR_50", map_);
		failed ("Didn't throw SQLException v="+v);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}

    }


/**
getObject() - Should return null when the column is NULL.
**/
    public void Var020 ()
    {
	if (canRun) { 
	    try {
		ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							 + JDRSTest.RSTEST_GET);
		JDRSTest.position0 (rs, "CHAR_NULL");
		Object s = rs.getObject ("C_CHAR_50",map_);
		assertCondition (s == null);
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}

    }



/**
getObject() - Get from a SMALLINT.
<P><B>Note:</B> The JDBC specification states that getting a value from a SMALLINT
database column with getObject() will return an java.lang.Integer object, not a 
java.lang.Short object as we have used in the past. This change could potentially 
break customers but we are making it for spec compliance in the V5R2 timeframe and
customers will have to change applications that break.
**/
    public void Var021 ()
    {

	if (canRun) { 
	    try {
		ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							 + JDRSTest.RSTEST_GET);
		JDRSTest.position0 (rs, "NUMBER_POS");
		Object s = rs.getObject ("C_SMALLINT", map_);
	    // assertCondition ((s instanceof Short) && (s.equals (Short.valueOf((short) 198))));
		assertCondition ((s instanceof Integer) && (s.equals (Integer.valueOf(198))));
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}

    }



/**
getObject() - Get from a INTEGER.
**/
    public void Var022 ()
    {
	if (canRun) { 
	    try {
		ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							 + JDRSTest.RSTEST_GET);
		JDRSTest.position0 (rs, "NUMBER_NEG");
		Object s = rs.getObject ("C_INTEGER", map_);
		assertCondition ((s instanceof Integer) && (s.equals (Integer.valueOf(-98765))));
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}

    }



/**
getObject() - Get from a REAL.
**/
    public void Var023 ()
    {
	if (canRun) { 
	    try {
		ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							 + JDRSTest.RSTEST_GET);
		JDRSTest.position0 (rs, "NUMBER_POS");
		Object s = rs.getObject ("C_REAL", map_);
		assertCondition ((s instanceof Float) && (s.equals (Float.valueOf(4.4f))));
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}

    }



/**
getObject() - Get from a FLOAT.
**/
    public void Var024 ()
    {
	if (canRun) { 
	    try {
		ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							 + JDRSTest.RSTEST_GET);
		JDRSTest.position0 (rs, "NUMBER_NEG");
		Object s = rs.getObject ("C_FLOAT", map_);
		assertCondition ((s instanceof Double) && (s.equals (Double.valueOf(-5.55))));
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}

    }



/**
getObject() - Get from a DOUBLE.
**/
    public void Var025 ()
    {
	if (canRun) { 
	    try {
		ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							 + JDRSTest.RSTEST_GET);
		JDRSTest.position0 (rs, "NUMBER_POS");
		Object s = rs.getObject ("C_DOUBLE", map_);
		assertCondition ((s instanceof Double) && (s.equals (Double.valueOf(6.666))));
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}

    }



/**
getObject() - Get from a DECIMAL.
**/
    public void Var026 ()
    {
	if (canRun) { 
	    try {
		ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							 + JDRSTest.RSTEST_GET);
		JDRSTest.position0 (rs, "NUMBER_NEG");
		Object s = rs.getObject ("C_DECIMAL_50",map_);
		assertCondition ((s instanceof BigDecimal) && (s.equals (new BigDecimal (-7))));
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}

    }



/**
getObject() - Get from a NUMERIC.
**/
    public void Var027 ()
    {
	if (canRun) { 
	    try {
		ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							 + JDRSTest.RSTEST_GET);
		JDRSTest.position0 (rs, "NUMBER_POS");
		Object s = rs.getObject ("C_NUMERIC_105",map_);
		assertCondition ((s instanceof BigDecimal) && (s.equals (new BigDecimal ("10.10105"))));
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}

    }



/**
getObject() - Get from a CHAR.
**/
    public void Var028 ()
    {
	if (canRun) { 
	    try {
		ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							 + JDRSTest.RSTEST_GET);
		JDRSTest.position0 (rs, "CHAR_FULL");
		Object s = rs.getObject ("C_CHAR_50",map_);
		assertCondition ((s instanceof String) && (s.equals ("Toolbox for Java                                  ")));
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}

    }



/**
getObject() - Get from a VARCHAR.
**/
    public void Var029 ()
    {
	if (canRun) { 
	    try {
		ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							 + JDRSTest.RSTEST_GET);
		JDRSTest.position0 (rs, "CHAR_FULL");
		Object s = rs.getObject ("C_VARCHAR_50",map_);
		assertCondition ((s instanceof String) && (s.equals ("Java Toolbox")));
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}

    }



/**
getObject() - Get from a CLOB.
**/
    public void Var030 ()
    {
	if (canRun) { 
	    if (checkLobSupport ()) {
		try {
		    ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							     + JDRSTest.RSTEST_GET);
		    JDRSTest.position0 (rs, "LOB_FULL");
		    Object s = rs.getObject ("C_CLOB",map_);
		    if (isJdbc20 ()) {
			Clob c = (Clob) s;
			String answer=c.getSubString (1, (int) c.length ());
			assertCondition (answer.equals (JDRSTest.CLOB_FULL), "\""+answer+"\" <> expected \""+JDRSTest.CLOB_FULL+"\""); // @D1C
		    } else {
			assertCondition (((String) s).equals (JDRSTest.CLOB_FULL));
		    }
		} catch (Exception e) {
		    failed (e, "Unexpected Exception");
		}
	    }
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}

    }



/**
getObject() - Get from a DBCLOB.
**/
    public void Var031 ()
    {
	if (canRun) { 
	    if (checkLobSupport ()) {
		try {
		    ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							     + JDRSTest.RSTEST_GET);
		    JDRSTest.position0 (rs, "LOB_FULL");
		    Object s = rs.getObject ("C_DBCLOB",map_);
		    if (isJdbc20 ()) {
			Clob c = (Clob) s;
			String answer = c.getSubString (1, (int) c.length ());
			assertCondition (answer.equals (JDRSTest.DBCLOB_FULL),"\""+answer+"\" <> expected \""+JDRSTest.DBCLOB_FULL+"\""); // @D1C
		    } else {
			assertCondition (((String) s).equals (JDRSTest.DBCLOB_FULL));
		    }
		} catch (Exception e) {
		    failed (e, "Unexpected Exception");
		}
	    }
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}

    }



/**
getObject() - Get from a BINARY.
**/
    public void Var032 ()
    {
	if (canRun) { 
	    try {
		ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							 + JDRSTest.RSTEST_GET);
		JDRSTest.position0 (rs, "BINARY_NOTRANS");
		Object s = rs.getObject ("C_BINARY_20",map_);
		assertCondition ((s instanceof byte[]) && (areEqual ((byte[]) s, eleven)));
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}

    }



/**
getObject() - Get from a VARBINARY.
**/
    public void Var033 ()
    {
	if (canRun) { 
	    try {
		ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							 + JDRSTest.RSTEST_GET);
		JDRSTest.position0 (rs, "BINARY_NOTRANS");
		Object s = rs.getObject ("C_VARBINARY_20",map_);
		assertCondition ((s instanceof byte[]) && (areEqual ((byte[]) s, twelve)));
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}

    }



/**
getObject() - Get from a BLOB.
**/
    public void Var034 ()
    {
	if (canRun) { 
	    if (checkLobSupport ()) {
		try {
		    ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							     + JDRSTest.RSTEST_GET);
		    JDRSTest.position0 (rs, "LOB_FULL");
		    Object s = rs.getObject ("C_BLOB",map_);
		    if (isJdbc20 ()) {
			Blob b = (Blob) s;
			assertCondition (areEqual (b.getBytes (1, (int) b.length ()), JDRSTest.BLOB_FULL)); // @D1C
		    } else {
			assertCondition (areEqual ((byte[]) s, JDRSTest.BLOB_FULL));
		    }
		} catch (Exception e) {
		    failed (e, "Unexpected Exception");
		}
	    }
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}
    }



/**
getObject() - Get from a DATE.
**/
    public void Var035 ()
    {
	if (canRun) { 
	    try {
		ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							 + JDRSTest.RSTEST_GET);
		JDRSTest.position0 (rs, "DATE_1998");
		Object s = rs.getObject ("C_DATE",map_);
		assertCondition ((s instanceof Date) && (s.toString ().equals ("1998-04-08")));
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}
    }



/**
getObject() - Get from a TIME.
**/
    public void Var036 ()
    {
	if (canRun) { 
	    try {
		ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							 + JDRSTest.RSTEST_GET);
		JDRSTest.position0 (rs, "DATE_1998");
		Object s = rs.getObject ("C_TIME",map_);
		assertCondition ((s instanceof Time) && (s.toString ().equals ("08:14:03")));
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}

    }



/**
getObject() - Get from a TIMESTAMP.
**/
    public void Var037 ()
    {
	if (canRun) { 
	    try {
		ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							 + JDRSTest.RSTEST_GET);
		JDRSTest.position0 (rs, "DATE_1998");
		Object s = rs.getObject ("C_TIMESTAMP",map_);
		assertCondition ((s instanceof Timestamp) && (s.equals (Timestamp.valueOf ("1998-11-18 03:13:42.987654000"))));
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}
    }



/**
getObject() - Get from a DATALINK.

With JDBC 3.0 and beyond, DATALINK columns should be returned as java.net.URL objects, not Strings.
Because the ResultSet.getObject method can't be updated to throw MalformedURLExceptions, there needs
to be some determination as to how to handle the possibility that this type of exception may be thrown.
I have decided to catch the exception and, instead of thrown a comparable SQLException, return a String
object to the user as would be done for JDBC releases prior to 3.0.
NOTE: This really shouldn't be an issue as all datalinks on the AS/400 are http-based URLs today.
**/
    public void Var038 ()
    {
	if (canRun) { 
	    if (checkDatalinkSupport ()) {
		try {
		    Statement s = connection_.createStatement ();
		    ResultSet rs = s.executeQuery ("SELECT * FROM "
						   + JDRSTest.RSTEST_GETDL);
		    JDRSTest.position0 (rs, "LOB_FULL");
		    Object o = rs.getObject ("C_DATALINK",map_);
		//Native driver will return datalinks as strings until v5r3.
		    if ( (getJdbcLevel() < 3) || ( getDriver() == JDTestDriver.DRIVER_NATIVE && false))
			assertCondition ((o instanceof String) && (o.toString().equalsIgnoreCase("https://github.com/IBM/JTOpen-test/blob/main/README.testing.txt")));
		    else
			assertCondition ((o instanceof java.net.URL) && (o.toString().equalsIgnoreCase("https://github.com/IBM/JTOpen-test/blob/main/README.testing.txt")));

		} catch (Exception e) {
		    failed (e, "Unexpected Exception");
		}
	    }
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}

    }



/**
getObject() - Get from a DISTINCT.
**/
    public void Var039 ()
    {
	if (canRun) { 
	    if (checkLobSupport ()) {
		try {
		    ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							     + JDRSTest.RSTEST_GET);
		    JDRSTest.position0 (rs, "LOB_FULL");
		    Object s = rs.getObject ("C_DISTINCT",map_);
		    assertCondition ((s instanceof String) && (s.equals ("123456789")));
		} catch (Exception e) {
		    failed (e, "Unexpected Exception");
		}
	    }
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}
    }



/**
getObject() - Get from a BIGINT.
**/
    public void Var040()
    {
	if (canRun) { 
	    if (checkBigintSupport()) {
		try {
		    ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							     + JDRSTest.RSTEST_GET);
		    JDRSTest.position0 (rs, "NUMBER_NEG");
		    Object s = rs.getObject ("C_BIGINT",map_);
		    assertCondition ((s instanceof Long) && (s.equals (Long.valueOf(-44332123))));
		} catch (Exception e) {
		    failed (e, "Unexpected Exception");
		}
	    }
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}
    }



/**
getObject() - Verify that no DataTruncation is posted
when the max field size is set to 0.
**/
    public void Var041()
    {
	if (canRun) { 
	    try {
		Statement s = connection_.createStatement ();
		s.setMaxFieldSize (0);
		ResultSet rs = s.executeQuery ("SELECT * FROM "
					       + JDRSTest.RSTEST_GET);
		JDRSTest.position0 (rs, "CHAR_FULL");
		Object s1 = rs.getObject ("C_VARCHAR_50",map_);
		DataTruncation dt = (DataTruncation) rs.getWarnings ();
		rs.close ();
		s.close ();
		assertCondition ((s1.equals ("Java Toolbox"))
				 && (dt == null));
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}
    }



/**
getObject() - Verify that data is truncated without
a DataTruncation posted when the max field size is set 
to a value shorter than the string.
**/
    public void Var042()
    {
	if (canRun) { 
	    try {
		Statement s = connection_.createStatement ();
		s.setMaxFieldSize (11);
		ResultSet rs = s.executeQuery ("SELECT * FROM "
					       + JDRSTest.RSTEST_GET);
		JDRSTest.position0 (rs, "CHAR_FULL");
		int columnIndex = rs.findColumn ("C_VARCHAR_50");
		Object s1 = rs.getObject ("C_VARCHAR_50",map_);
		SQLWarning w = rs.getWarnings ();
		rs.close ();
		s.close ();
		assertCondition ((s1.equals ("Java Toolbo")) && (w == null), "s1="+s1+" sb 'JavaToolbo' w="+w+" sb null columnIndex"+columnIndex);
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	} else {
	    notApplicable("V5R4 native driver type mapping test"); 
	}
    }



}



