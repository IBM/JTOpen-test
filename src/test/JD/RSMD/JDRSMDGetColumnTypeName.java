///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSMDGetColumnTypeName.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////
//
//
//
//
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDRSMDGetColumnTypeName.java
//
// Classes:      JDRSMDGetColumnTypeName
//
////////////////////////////////////////////////////////////////////////

package test.JD.RSMD;

import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDRSMDTest;
import test.JDTestcase;



/**
Testcase JDRSMDGetColumnTypeName.  This tests the following method
of the JDBC ResultSetMetaData class:

<ul>
<li>getColumnTypeName()
</ul>
**/
//
// Implementation note:
//
// * We don't bother to test with package caching here, that will
//   be sufficiently tested (for types) in JDRSMDGetColumnType.
//
public class JDRSMDGetColumnTypeName
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSMDGetColumnTypeName";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSMDTest.main(newArgs); 
   }



    // Private data.
    private ResultSet           rs_             = null;
    private ResultSetMetaData   rsmd_           = null;
    private Statement           statement_      = null;
    private ResultSet           rs2_             = null;
    private ResultSetMetaData   rsmd2_           = null;
    private Statement           statement2_      = null;
    private boolean isJDK14; 


/**
Constructor.
**/
    public JDRSMDGetColumnTypeName (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSMDGetColumnTypeName",
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
	// look for jdk1.4
	//
		isJDK14 = true; 



        // SQL400 - driver neutral...
        String url = baseURL_
        // String url = "jdbc:as400://" + systemObject_.getSystemName()
            
            ;
        connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
        statement_ = connection_.createStatement ();
        rs_ = statement_.executeQuery ("SELECT * FROM "
            + JDRSMDTest.RSMDTEST_GET);
        rsmd_ = rs_.getMetaData ();
        statement2_ = connection_.createStatement ();
        rs2_ = statement2_.executeQuery ("SELECT * FROM "
            + JDRSMDTest.RSMDTEST_GET2);
        rsmd2_ = rs2_.getMetaData ();
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
        rs_.close ();
        statement_.close ();
        rs2_.close ();
        statement2_.close ();
        connection_.close ();
    }



/**
getColumnTypeName() - Check column -1.  Should throw an exception.
**/
    public void Var001()
    {
        try {
            rsmd_.getColumnTypeName (-1);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getColumnTypeName() - Check column 0.  Should throw an exception.
**/
    public void Var002()
    {
        try {
            rsmd_.getColumnTypeName (0);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getColumnTypeName() - Check a column greater than the max.
Should throw an exception.
**/
    public void Var003()
    {
        try {
            rsmd_.getColumnTypeName (35);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getColumnTypeName() - Check when the result set is closed.
**/
    public void Var004()
    {
        try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSMDTest.RSMDTEST_GET);
            ResultSetMetaData rsmd = rs.getMetaData ();
            rs.close ();
            String v = rsmd.getColumnTypeName (rs_.findColumn ("C_SMALLINT"));
            assertCondition (v.equals ("SMALLINT"));
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnTypeName() - Check when the meta data comes from a prepared
statement.
**/
    public void Var005()
    {
        if (checkJdbc20 ()) {
            try {
                PreparedStatement ps;
		try { 
		ps = connection_.prepareStatement ("SELECT C_SMALLINT FROM "
                    + JDRSMDTest.RSMDTEST_GET, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
		} catch (Exception e) {
		ps = connection_.prepareStatement ("SELECT C_SMALLINT FROM "
                    + JDRSMDTest.RSMDTEST_GET); 

		} 
                ResultSetMetaData rsmd = ps.getMetaData ();
                String v = rsmd.getColumnTypeName (1);
                ps.close ();
                assertCondition (v.equals ("SMALLINT"));
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getColumnTypeName() - Check a SMALLINT column.
**/
    public void Var006()
    {
        try {
            String s = rsmd_.getColumnTypeName (rs_.findColumn ("C_SMALLINT"));
            assertCondition (s.equals ("SMALLINT"));
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnTypeName() - Check an INTEGER column.
**/
    public void Var007()
    {
        try {
            String s = rsmd_.getColumnTypeName (rs_.findColumn ("C_INTEGER"));
            assertCondition (s.equals ("INTEGER"));
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnTypeName() - Check a REAL column.
**/
    public void Var008()
    {
        try {
            String s = rsmd_.getColumnTypeName (rs_.findColumn ("C_REAL"));
            assertCondition (s.equals ("REAL"), "Got "+s+" expected REAL");
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnTypeName() - Check a FLOAT column.
**/
    public void Var009()
    {
        try {
            String s = rsmd_.getColumnTypeName (rs_.findColumn ("C_FLOAT"));
            assertCondition (s.equals ("DOUBLE"));
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnTypeName() - Check a FLOAT(3) column.
**/
    public void Var010()
    {
        try {
            String s = rsmd_.getColumnTypeName (rs_.findColumn ("C_FLOAT_3"));
            assertCondition (s.equals ("REAL"), "Got "+s+" expected REAL");
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnTypeName() - Check a DOUBLE column.
**/
    public void Var011()
    {
        try {
            String s = rsmd_.getColumnTypeName (rs_.findColumn ("C_DOUBLE"));
            assertCondition (s.equals ("DOUBLE"));
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnTypeName() - Check a DECIMAL(10,5) column.
**/
    public void Var012()
    {
        try {
            String s = rsmd_.getColumnTypeName (rs_.findColumn ("C_DECIMAL_105"));
            assertCondition (s.equals ("DECIMAL"));
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnTypeName() - Check a NUMERIC(10,5) column.
**/
    public void Var013()
    {
        try {
            String s = rsmd_.getColumnTypeName (rs_.findColumn ("C_NUMERIC_105"));
            assertCondition (s.equals ("NUMERIC"));
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnTypeName() - Check a CHAR(50) column.
**/
    public void Var014()
    {
        try {
            String s = rsmd_.getColumnTypeName (rs_.findColumn ("C_CHAR_50"));
            assertCondition (s.equals ("CHAR"));
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnTypeName() - Check a VARCHAR(50) column.
**/
    public void Var015()
    {
        try {
            String s = rsmd_.getColumnTypeName (rs_.findColumn ("C_VARCHAR_50"));
            assertCondition (s.equals ("VARCHAR"));
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnTypeName() - Check a BINARY(20) column.
**/
    public void Var016()
    {
        try {
            String s = rsmd_.getColumnTypeName (rs_.findColumn ("C_BINARY_20"));
            assertCondition (s.equals ("CHAR () FOR BIT DATA"), "Got "+s+" expected CHAR () FOR BIT DATA");
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnTypeName() - Check a VARBINARY(20) column.
**/
    public void Var017()
    {
        try {
            String s = rsmd_.getColumnTypeName (rs_.findColumn ("C_VARBINARY_20"));
            assertCondition (s.equals ("VARCHAR () FOR BIT DATA"), "Got "+s+" expected VARCHAR () FOR BIT DATA");
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnTypeName() - Check a DATE column.
**/
    public void Var018()
    {
        try {
            String s = rsmd_.getColumnTypeName (rs_.findColumn ("C_DATE"));
            assertCondition (s.equals ("DATE"));
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnTypeName() - Check a TIME column.
**/
    public void Var019()
    {
        try {
            String s = rsmd_.getColumnTypeName (rs_.findColumn ("C_TIME"));
            assertCondition (s.equals ("TIME"));
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnTypeName() - Check a TIMESTAMP column.
**/
    public void Var020()
    {
        try {
            String s = rsmd_.getColumnTypeName (rs_.findColumn ("C_TIMESTAMP"));
            assertCondition (s.equals ("TIMESTAMP"));
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnTypeName() - Check a BLOB column.
**/
    public void Var021()
    {
        if (checkLobSupport ()) {
            try {
                String s = rsmd_.getColumnTypeName (rs_.findColumn ("C_BLOB"));
                assertCondition (s.equals ("BLOB"));
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getColumnTypeName() - Check a CLOB column.
**/
//
    public void Var022()
    {
        if (checkLobSupport ()) {
            try {
                String s = rsmd_.getColumnTypeName (rs_.findColumn ("C_CLOB"));
		assertCondition (s.equals ("CLOB"), "s = "+s+" SB CLOB");
	    }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
	}
    }



/**
getColumnTypeName() - Check a DBCLOB column.
**/
    public void Var023()
    {
        if (checkLobSupport ()) {
            try {
                String s = rsmd_.getColumnTypeName (rs_.findColumn ("C_DBCLOB"));
                assertCondition (s.equals ("DBCLOB"), "Got "+s+" expected DBCLOB");
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getColumnTypeName() - Check a DATALINK column.

SQL400 - The native driver is going to see the type of the column
as VARCHAR, just like the type that comes back on JDRSMDGetColumnType.
**/
    public void Var024()
    {
        if (checkLobSupport ()) {
            try {
                String s = rsmd_.getColumnTypeName (rs_.findColumn ("C_DATALINK"));

                if (isToolboxDriver() || (true && isJDK14))
                {
                   assertCondition (s.equals ("DATALINK"));
                } else {
                   assertCondition (s.equals ("VARCHAR"));
                }
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getColumnTypeName() - Check a DISTINCT column.
**/
    public void Var025()
    {
        if (checkLobSupport ()) {
            try {
                String s = rsmd_.getColumnTypeName (rs_.findColumn ("C_DISTINCT"));
		// Updated 4/7/2013 -- all drivers should return user defined type name

		String expected =JDRSMDTest.COLLECTION+".MONEY";
		assertCondition (s.equals (expected), "returned "+s+" sb "+expected); 
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



// @B0A
/**
getColumnTypeName() - Check a BIGINT column.
**/
    public void Var026()
    {
        if (checkBigintSupport ()) {
            try {
                String s = rsmd_.getColumnTypeName (rs_.findColumn ("C_BIGINT"));
                assertCondition (s.equals ("BIGINT"));
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
getColumnTypeName() - Check a DECFLOAT16 column.
**/
    public void Var027()
    {
        if (checkDecFloatSupport ()) {
            try {
                String s = rsmd_.getColumnTypeName (rs_.findColumn ("C_DECFLOAT16"));
                assertCondition (s.equals ("DECFLOAT"), "Got "+s+" SB DECFLOAT");
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
getColumnTypeName() - Check a DECFLOAT34 column.
**/
    public void Var028()
    {
        if (checkDecFloatSupport ()) {
            try {
                String s = rsmd_.getColumnTypeName (rs_.findColumn ("C_DECFLOAT34"));
                assertCondition (s.equals ("DECFLOAT"), "Got "+s+" SB DECFLOAT");
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getColumnTypeName() - Check an XML column.
**/
    public void Var029()
    {
        if (checkXmlSupport ()) {
            try {
                String s = rsmd2_.getColumnTypeName (rs2_.findColumn ("C_XML"));
		// This should always be XML because that is the DB's name for
		// for the type. 
		assertCondition (s.equals ("XML"), "Got "+s+" SB XML");
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
getColumnTypeName() - Check a NVARCHAR(50) column.
**/
    public void Var030()
    {
        try {
            String s = rsmd_.getColumnTypeName (rs_.findColumn ("C_NVARCHAR_50"));
            assertCondition (s.equals ("NVARCHAR"));
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
getColumnTypeName() - Check a NCHAR(50) column.
**/
    public void Var031()
    {
        try {
            String s = rsmd_.getColumnTypeName (rs_.findColumn ("C_NCHAR_50"));
            assertCondition (s.equals ("NCHAR"));
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

/**
getColumnTypeName() - Check a CLOB column.
**/
//
    public void Var032()
    {
        if (checkLobSupport ()) {
            try {
                String s = rsmd_.getColumnTypeName (rs_.findColumn ("C_NCLOB"));
		assertCondition (s.equals ("NCLOB"), "s = "+s+" SB NCLOB");
	    }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
	}
    }


// @B0A
/**
getColumnTypeName() - Check a BOOLEAN column.
**/
    public void Var033()
    {
        if (checkBooleanSupport ()) {
            try {
                String s = rsmd_.getColumnTypeName (rs_.findColumn ("C_BOOLEAN"));
                assertCondition (s.equals ("BOOLEAN"), "expected BOOLEAN got "+s);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
getColumnTypeName() - Check a UDT Types -- issue 67185
**/
    public void Var034()
    {
	StringBuffer sb = new StringBuffer(" -- added 8/31/2012 for issue 67185 various UDT types"); 
	String sql=""; 
	try {
	    boolean passed = true; 

	    Statement stmt = connection_.createStatement();

	    sql = "DROP TABLE "+collection_+".JDRSMDGT34";
	    try { stmt.execute(sql); } catch (SQLException sqlex) {}
	    sql="DROP DISTINCT TYPE "+collection_+".UDTINT";
	    try { stmt.execute(sql); } catch (SQLException sqlex) {}
	    sql="DROP DISTINCT TYPE "+collection_+".UDTSMINT";
	    try { stmt.execute(sql); } catch (SQLException sqlex) {}
	    sql="DROP DISTINCT TYPE "+collection_+".UDTLVCHAR";
	    try { stmt.execute(sql); } catch (SQLException sqlex) {}
	    sql="DROP DISTINCT TYPE "+collection_+".UDTGRAPHIC";
	    try { stmt.execute(sql); } catch (SQLException sqlex) {}
	    sql="DROP DISTINCT TYPE "+collection_+".UDTCLOB";
	    try { stmt.execute(sql); } catch (SQLException sqlex) {}
	    sql="DROP DISTINCT TYPE "+collection_+".UDTDBCLOB";
	    try { stmt.execute(sql); } catch (SQLException sqlex) {}
	    sql="DROP DISTINCT TYPE "+collection_+".UDTBLOB";
	    try { stmt.execute(sql); } catch (SQLException sqlex) {}
	    sql="DROP DISTINCT TYPE "+collection_+".UDTDATE";
	    try { stmt.execute(sql); } catch (SQLException sqlex) {}


	    sql="CREATE DISTINCT TYPE "+collection_+".UDTINT AS INT";
	    stmt.execute(sql);
	    sql="CREATE DISTINCT TYPE "+collection_+".UDTSMINT AS SMALLINT";
	    stmt.execute(sql);
	    sql="CREATE DISTINCT TYPE "+collection_+".UDTLVCHAR AS VARCHAR(20)";
	    stmt.execute(sql);
	    sql="CREATE DISTINCT TYPE "+collection_+".UDTGRAPHIC AS GRAPHIC(20) CCSID 835";
	    stmt.execute(sql);
	    sql="CREATE DISTINCT TYPE "+collection_+".UDTCLOB AS CLOB(5K)";
	    stmt.execute(sql);
	    sql="CREATE DISTINCT TYPE "+collection_+".UDTDBCLOB AS DBCLOB(5K)";
	    stmt.execute(sql);
	    sql="CREATE DISTINCT TYPE "+collection_+".UDTBLOB AS BLOB(5K)";
	    stmt.execute(sql);
	    sql="CREATE DISTINCT TYPE "+collection_+".UDTDATE AS DATE";
	    stmt.execute(sql);



	    sql = "CREATE TABLE  "+collection_+".JDRSMDGT34 ( " +
	      " A "+collection_+".UDTINT WITH DEFAULT NULL, "+
	      " B "+collection_+".UDTBLOB WITH DEFAULT NULL, "+
	      " S "+collection_+".UDTSMINT WITH DEFAULT NULL, "+
	      " D "+collection_+".UDTDATE, "+
	      " DB "+collection_+".UDTDBCLOB, "+
	      " C "+collection_+".UDTGRAPHIC, "+
	      " E "+collection_+".UDTLVCHAR )";


	    stmt.execute(sql);
	    sql = "SELECT * FROM  "+collection_+".JDRSMDGT34 ";
	    ResultSet rs = stmt.executeQuery(sql);
	    ResultSetMetaData rsmd = rs.getMetaData();

	    String [] expectedTypeNames = { "",
	     collection_+".UDTINT",
	     collection_+".UDTBLOB",
       collection_+".UDTSMINT",
       collection_+".UDTDATE",
       collection_+".UDTDBCLOB",
       collection_+".UDTGRAPHIC", 
       collection_+".UDTLVCHAR" 
	    };
	    for (int i = 1; i < expectedTypeNames.length; i++) {
	      String typeName = rsmd.getColumnTypeName(i);
	      if (! expectedTypeNames[i].equals(typeName)) { 
	        passed = false; 
	        sb.append("\nFor column "+i+" expected "+expectedTypeNames[i]+" got "+typeName); 
	      }
	    }

	    rs.close(); 
	    sql = "DROP TABLE "+collection_+".JDRSMDGT34";
      stmt.execute(sql); 
      sql="DROP DISTINCT TYPE "+collection_+".UDTINT";
     stmt.execute(sql); 
      sql="DROP DISTINCT TYPE "+collection_+".UDTSMINT";
      stmt.execute(sql); 
      sql="DROP DISTINCT TYPE "+collection_+".UDTLVCHAR";
      stmt.execute(sql); 
      sql="DROP DISTINCT TYPE "+collection_+".UDTGRAPHIC";
       stmt.execute(sql); 
      sql="DROP DISTINCT TYPE "+collection_+".UDTCLOB";
       stmt.execute(sql); 
      sql="DROP DISTINCT TYPE "+collection_+".UDTDBCLOB";
       stmt.execute(sql);
      sql="DROP DISTINCT TYPE "+collection_+".UDTBLOB";
       stmt.execute(sql); 
      sql="DROP DISTINCT TYPE "+collection_+".UDTDATE";
       stmt.execute(sql); 

	    assertCondition (passed, sb); 
	}
            catch(Exception e) {
                failed (e, "Unexpected Exception sql="+sql+ " " + sb);
            }
 
    }




}


