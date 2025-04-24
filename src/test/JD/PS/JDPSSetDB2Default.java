///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSSetDB2Default.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////



package test.JD.PS;

import com.ibm.as400.access.AS400;

import test.JDPSTest;
import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable; import java.util.Vector;



/**
Testcase JDPSetDB2Default.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>SetDB2Default()
</ul>
 **/
public class JDPSSetDB2Default
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDPSSetDB2Default";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDPSTest.main(newArgs); 
   }


public static String C_SMALLINT_DEFAULT = "99"; 
public static String C_INTEGER_DEFAULT =          "199";
public static String C_REAL_DEFAULT =             "299.0";
public static String C_FLOAT_DEFAULT =            "399.0";
public static String C_DOUBLE_DEFAULT =           "499.0";
public static String C_DECIMAL_50_DEFAULT =       "599";
public static String C_DECIMAL_105_DEFAULT =      "699.00000";
public static String C_NUMERIC_50_DEFAULT =       "799";
public static String C_NUMERIC_105_DEFAULT =      "899.00000";
public static String C_CHAR_1_DEFAULT =           "8";
public static String C_CHAR_50_DEFAULT =          "789                                               ";
public static String C_VARCHAR_50_DEFAULT =       "109";
public static String C_BINARY_1_DEFAULT =         "f1";
public static String C_BINARY_20_DEFAULT =        "f200000000000000000000000000000000000000";
public static String C_VARBINARY_20_DEFAULT =     "f3";
public static String C_CFBD_1_DEFAULT =           "f4";
public static String C_CFBD_20_DEFAULT =          "f500000000000000000000000000000000000000";
public static String C_VCFBD_20_DEFAULT =         "f6";

public static String C_DATE_DEFAULT_SET =         "01/01/2000";
public static String C_DATE_DEFAULT =             "2000-01-01";
public static String C_TIME_DEFAULT_SET =         "01:01:01";
public static String C_TIME_DEFAULT =             "01.01.01";
public static String C_TIMESTAMP_DEFAULT_SET =    "1990-02-22-13.00.00";	// @A1C Changed from 24.00.00 
public static String C_TIMESTAMP_DEFAULT =        "1990-02-22 13:00:00.000000"; 	// @A1C Changed from 24.00.00


public static String C_BLOB_DEFAULT =          "f7"; 
public static String C_CLOB_DEFAULT =          "a"; 
public static String C_DBCLOB_DEFAULT =        "9";
public static String C_DATALINK_DEFAULT =      "null";
public static String C_DISTINCT_DEFAULT =      "6";        
 
public static String C_BIGINT_DEFAULT =        "5"; 

public static String C_DECFLOAT16_DEFAULT =    "3.14";
public static String C_DECFLOAT34_DEFAULT =    "3.14";
public static String C_XML_DEFAULT_UNUSED   =         "<default>xml default</default>"; 

public static String C_BOOLEAN_DEFAULT =        "false"; 
public static String C_BOOLEAN_DEFAULT_STRING =        "0"; 


    // Private data.
    private Statement           statement_;

    String              methodName = "setDB2Default"; 

    /**
Constructor.
     **/
    public JDPSSetDB2Default (AS400 systemObject,
            Hashtable<String,Vector<String>> namesAndVars,
            int runMode,
            FileOutputStream fileOutputStream,
            
            String password)
    {
        super (systemObject, "JDPSSetDB2Default",
                namesAndVars, runMode, fileOutputStream,
                password);
    }


    public JDPSSetDB2Default (AS400 systemObject,
        String testname,
        Hashtable<String,Vector<String>> namesAndVars,
        int runMode,
        FileOutputStream fileOutputStream,
        
        String password)
{
    super (systemObject, testname,
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
      
        String url = baseURL_
        
         
        + ";errors=full";
        if(isToolboxDriver())	//@A1A
        	url += ";date format=iso;time format=iso";	// @A1A
        connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
        statement_ = connection_.createStatement ();

        // make sure the old one has been cleaned up
        try {
            statement_.executeUpdate("DROP TABLE " + JDPSTest.PSTEST_SETDB2DEF);
        } catch (Exception e) {
            // ignore error
        }

       
     
        StringBuffer buffer = new StringBuffer();
        buffer.append("CREATE TABLE ");
        buffer.append(JDPSTest.PSTEST_SETDB2DEF);
        buffer.append(" (C_KEY VARCHAR(20)");
        buffer.append(",C_SMALLINT        SMALLINT      default "+C_SMALLINT_DEFAULT);
        buffer.append(",C_INTEGER         INTEGER       default "+C_INTEGER_DEFAULT);
        buffer.append(",C_REAL            REAL          default "+C_REAL_DEFAULT);
        buffer.append(",C_FLOAT           FLOAT         default "+C_FLOAT_DEFAULT);
        buffer.append(",C_DOUBLE          DOUBLE        default "+C_DOUBLE_DEFAULT);
        buffer.append(",C_DECIMAL_50      DECIMAL(5,0)  default "+C_DECIMAL_50_DEFAULT);
        buffer.append(",C_DECIMAL_105     DECIMAL(10,5) default "+C_DECIMAL_105_DEFAULT);
        buffer.append(",C_NUMERIC_50      NUMERIC(5,0)  default "+C_NUMERIC_50_DEFAULT);
        buffer.append(",C_NUMERIC_105     NUMERIC(10,5) default "+C_NUMERIC_105_DEFAULT);
        buffer.append(",C_CHAR_1          CHAR          default '"+C_CHAR_1_DEFAULT+"'");
        buffer.append(",C_CHAR_50         CHAR(50)       default '"+C_CHAR_50_DEFAULT+"'");
        buffer.append(",C_VARCHAR_50      VARCHAR(50)    default '"+C_VARCHAR_50_DEFAULT+"' ");
        buffer.append(",C_BINARY_1        BINARY(1)      default BINARY(X'"+C_BINARY_1_DEFAULT+"')");
        buffer.append(",C_BINARY_20       BINARY(20)     default BINARY(X'"+C_BINARY_20_DEFAULT+"')");
        buffer.append(",C_VARBINARY_20    VARBINARY(20)  default VARBINARY(X'"+C_VARBINARY_20_DEFAULT+"')");
        buffer.append(",C_CFBD_1          CHAR FOR BIT DATA        default X'"+C_CFBD_1_DEFAULT+"'");
        buffer.append(",C_CFBD_20         CHAR(20)  FOR BIT DATA   default X'"+C_CFBD_20_DEFAULT+"'");
        buffer.append(",C_VCFBD_20        VARCHAR(20) FOR BIT DATA  default X'"+C_VCFBD_20_DEFAULT+"'");
        buffer.append(",C_DATE            DATE         default '"+C_DATE_DEFAULT_SET+"' ");
        buffer.append(",C_TIME            TIME         default '"+C_TIME_DEFAULT_SET+"' ");
        buffer.append(",C_TIMESTAMP       TIMESTAMP    default '"+C_TIMESTAMP_DEFAULT_SET+"'  ");
       
        buffer.append(",C_BLOB         BLOB(1)    default BLOB(X'"+C_BLOB_DEFAULT+"') ");
        buffer.append(",C_CLOB         CLOB(1)     default '"+C_CLOB_DEFAULT+"'") ;
        buffer.append(",C_DBCLOB       DBCLOB(200) CCSID 13488 default '"+C_DBCLOB_DEFAULT+"'");
        buffer.append(",C_DATALINK     DATALINK    default "+C_DATALINK_DEFAULT+"  ");
        buffer.append(",C_DISTINCT " + JDPSTest.COLLECTION + ".AGE  default "+C_DISTINCT_DEFAULT+" ");        
      
        buffer.append(",C_BIGINT       BIGINT default "+C_BIGINT_DEFAULT); 


	if (areDecfloatsSupported()) {
	    buffer.append(", C_DECFLOAT16 DECFLOAT(16) default "+ C_DECFLOAT16_DEFAULT );
	    buffer.append(", C_DECFLOAT34 DECFLOAT(34) default "+ C_DECFLOAT34_DEFAULT ); 
	}
	if (areBooleansSupported()) { 
      buffer.append(", C_BOOLEAN BOOLEAN default "+ C_BOOLEAN_DEFAULT ); 
	}
	/*
	 * Current XML columns do not support default value 
	if (isXmlSupported()) {
	    buffer.append(", C_XML XML default '"+C_XML_DEFAULT+"'");
	}
         */

        buffer.append(")");

        try {
            statement_.executeUpdate(buffer.toString());
        } catch (Exception e) {
            System.out.println("Error in setup for "+buffer.toString()); 
            // ignore error
            System.out.println(e);
        }
    }



    /**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
     **/
    protected void cleanup ()
    throws Exception
    {
        try
        {
            statement_.executeUpdate("DROP TABLE " + JDPSTest.PSTEST_SETDB2DEF);
        } catch (Exception e)
        {
            System.out.println("Error on DROP TABLE " + JDPSTest.PSTEST_SETDB2DEF);
            e.printStackTrace();

        } 
        statement_.close ();
        connection_.close ();
    }



    private boolean check71() {
	if (getRelease() < JDTestDriver.RELEASE_V7R1M0 &&
	    methodName.equals("setDBDefault")) {
	    notApplicable("setDBDefault is V7R1 variation");
	    return false; 
	}
	return true; 
    } 




    /**
SetDB2Default() - Should throw exception when the prepared
statement is closed.
     **/
    public void Var001()
    {
        if(getRelease() < JDTestDriver.RELEASE_V7R1M0){
            notApplicable("v5r5 variation");
            return;
        }
	if (!check71()) {
	    return; 
	}
        try {
            PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SETDB2DEF
                    + " (C_NUMERIC_105) VALUES (?)");
            ps.close ();
            JDReflectionUtil.callMethod_V(ps, methodName, 1);
            failed ("Didn't throw SQLException");
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
SetDB2Default() - Should throw exception when an invalid index is
specified.
     **/
    public void Var002()
    {
        if(getRelease() < JDTestDriver.RELEASE_V7R1M0){
            notApplicable("v5r5 variation");
            return;
        }
	if (!check71()) {
	    return; 
	}
        try {
            PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SETDB2DEF
                    + " (C_INTEGER, C_SMALLINT, C_VARCHAR) VALUES (?, ?, ?)");
            JDReflectionUtil.callMethod_V(ps, methodName, 100);
            ps.close ();
            failed ("Didn't throw SQLException");
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
SetDB2Default() - Should throw exception when index is 0.
     **/
    public void Var003()
    {
        if(getRelease() < JDTestDriver.RELEASE_V7R1M0){
            notApplicable("v5r5 variation");
            return;
        }
	if (!check71()) {
	    return; 
	}
        try {
            PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SETDB2DEF
                    + " (C_INTEGER, C_SMALLINT, C_VARCHAR) VALUES (?, ?, ?)");
            JDReflectionUtil.callMethod_V(ps, methodName, 0);
            ps.close ();
            failed ("Didn't throw SQLException");
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
SetDB2Default() - Should throw exception when index is -1.
     **/
    public void Var004()
    {
        if(getRelease() < JDTestDriver.RELEASE_V7R1M0){
            notApplicable("v5r5 variation");
            return;
        }
	if (!check71()) {
	    return; 
	}
        try {
            PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SETDB2DEF
                    + " (C_INTEGER, C_SMALLINT, C_VARCHAR) VALUES (?, ?, ?)");
            JDReflectionUtil.callMethod_V(ps, methodName, -1);
            ps.close ();
            failed ("Didn't throw SQLException");
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
SetDB2Default() - Should work with a valid parameter index
greater than 1.
     **/
    public void Var005()
    {
        if(getRelease() < JDTestDriver.RELEASE_V7R1M0){
            notApplicable("v5r5 variation");
            return;
        }
	if (!check71()) {
	    return; 
	}
        try {
            String expected=C_NUMERIC_105_DEFAULT; 
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDB2DEF);
   
            
            PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SETDB2DEF
                    + " (C_KEY, C_NUMERIC_105) VALUES (?, ?)");
            ps.setString (1, "Test");
            JDReflectionUtil.callMethod_V(ps, methodName, 2);
            //ps.setNull (2, Types.NUMERIC);

            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_105 FROM " + JDPSTest.PSTEST_SETDB2DEF);
            rs.next ();
            String retVal = rs.getString(1);
             
            rs.close ();

            assertCondition (expected.equals(retVal)," got "+retVal+" expected "+expected+" for NUMERIC");
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



    /**
SetDB2Default() - Should throw exception when the parameter is
not an input parameter.
     **/
    public void Var006()
    {
        if(getRelease() < JDTestDriver.RELEASE_V7R1M0){
            notApplicable("v5r5 variation");
            return;
        }
	if (!check71()) {
	    return; 
	}
        try {
            PreparedStatement ps = connection_.prepareStatement (
                    "CALL " + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");

            JDReflectionUtil.callMethod_V(ps, methodName, 2);
            ps.close ();
            failed ("Didn't throw SQLException");
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }


    public String getColumnData(ResultSet rs, String columnName) throws SQLException {
      String retVal;
      
      if (columnName.indexOf("BINARY") >= 0   || 
          columnName.indexOf("FBD") >= 0 || 
          columnName.indexOf("BLOB") >= 0 ) {
        byte[] retBytes = rs.getBytes(columnName);
        if (retBytes == null) {
          retVal = "null"; 
        } else {
          retVal=""; 
          for (int i = 0; i < retBytes.length; i++) {
            int b = retBytes[i] & 0xff; 
            if (b < 0x10) {
              retVal+="0"+Integer.toHexString(retBytes[i]); 
            } else {
              retVal+=Integer.toHexString(b); 
            }

          }
        }

      } else { 
        retVal = rs.getString (columnName); 
        if (retVal == null) retVal = "null"; 
      }
      return retVal; 
    }
    
    public void testOk(String columnName, String expectedString )
    {
	if(getRelease() < JDTestDriver.RELEASE_V7R1M0){
	    notApplicable("v5r5 variation");
	    return;
	}
	if (!check71()) {
	    return; 
	}
	try {
	    statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDB2DEF);

	    PreparedStatement ps = connection_.prepareStatement (
								 "INSERT INTO " + JDPSTest.PSTEST_SETDB2DEF
								 + " ("+columnName+") VALUES (?)");
	    JDReflectionUtil.callMethod_V(ps, methodName, 1);

	    ps.executeUpdate ();
	    ps.close ();

	    ResultSet rs = statement_.executeQuery ("SELECT "+columnName+" FROM " + JDPSTest.PSTEST_SETDB2DEF);
	    rs.next ();
	    String retVal= getColumnData(rs, columnName); 
	    rs.close ();

	    assertCondition (expectedString.equals(retVal), 
                "Got '"+retVal+"' expected '"+expectedString+"' for "+columnName);
	} catch (Exception e) {
	    failed (e, "Unexpected Exception");
	}

    } 


    public void testOk(String columnName1, String expectedString1, String columnName2, String expectedString2 )
    {
        if(getRelease() < JDTestDriver.RELEASE_V7R1M0){
            notApplicable("v5r5 variation");
            return;
        }
	if (!check71()) {
	    return; 
	}
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDB2DEF);

            PreparedStatement ps = connection_.prepareStatement (
                                                                 "INSERT INTO " + JDPSTest.PSTEST_SETDB2DEF
                                                                 + " ("+columnName1+","+columnName2+")"+
                                                                 " VALUES (?, ?)");
            JDReflectionUtil.callMethod_V(ps, methodName, 1);
            JDReflectionUtil.callMethod_V(ps, methodName, 2);

            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT "+columnName1+","+columnName2+" FROM " + JDPSTest.PSTEST_SETDB2DEF);
            rs.next ();
            String retVal1= getColumnData(rs, columnName1); 
            String retVal2= getColumnData(rs, columnName2); 
            
            
            
            rs.close ();

            assertCondition (expectedString1.equals(retVal1) && expectedString2.equals(retVal2), 
                "Got '"+retVal1+"' expected '"+expectedString1+"' for "+columnName1 +"..."+
                "Got '"+retVal2+"' expected '"+expectedString2+"' for "+columnName2);
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    } 

    
    public void testOk(String columnName1, String expectedString1, String columnName2, String expectedString2, String columnName3, String expectedString3 )
    {
        if(getRelease() < JDTestDriver.RELEASE_V7R1M0){
            notApplicable("v5r5 variation");
            return;
        }
	if (!check71()) {
	    return; 
	}
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDB2DEF);

            PreparedStatement ps = connection_.prepareStatement (
                                                                 "INSERT INTO " + JDPSTest.PSTEST_SETDB2DEF
                                                                 + " ("+columnName1+","+columnName2+","+columnName3+") VALUES (?, ?, ?)");
            JDReflectionUtil.callMethod_V(ps, methodName, 1);
            JDReflectionUtil.callMethod_V(ps, methodName, 2);
            JDReflectionUtil.callMethod_V(ps, methodName, 3);

            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT "+columnName1+","+columnName2+","+columnName3+" FROM " + JDPSTest.PSTEST_SETDB2DEF);
            rs.next ();
            String retVal1= getColumnData(rs, columnName1); 
            String retVal2= getColumnData(rs, columnName2); 
            String retVal3= getColumnData(rs, columnName3); 
            rs.close ();

            assertCondition (expectedString1.equals(retVal1) && 
                             expectedString2.equals(retVal2) &&
                             expectedString3.equals(retVal3), 
                "Got '"+retVal1+"' expected '"+expectedString1+"' for "+columnName1 +"..."+
                "Got '"+retVal2+"' expected '"+expectedString2+"' for "+columnName2+"..."+
                "Got '"+retVal3+"' expected '"+expectedString3+"' for "+columnName3);
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    } 

    
    
    
    public void testMultiple(String columnName, String expectedString )
    {
        if(getRelease() < JDTestDriver.RELEASE_V7R1M0){
            notApplicable("v5r5 variation");
            return;
        }
 	if (!check71()) {
	    return; 
	}
       try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDB2DEF);

            PreparedStatement ps = connection_.prepareStatement (
                                                                 "INSERT INTO " + JDPSTest.PSTEST_SETDB2DEF
                                                                 + " ("+columnName+") VALUES (?)");
            JDReflectionUtil.callMethod_V(ps, methodName, 1);
            ps.executeUpdate ();
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT "+columnName+" FROM " + JDPSTest.PSTEST_SETDB2DEF);
            rs.next ();
            String retVal1= getColumnData(rs, columnName);
            rs.next(); 
            String retVal2= getColumnData(rs, columnName);
            rs.close ();

            assertCondition (expectedString.equals(retVal1) && expectedString.equals(retVal2), 
                "Got '"+retVal1+"' and '"+retVal2+"' expected '"+expectedString+"' for "+columnName);
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    } 


    public void testMultiple(String columnName1, String expectedString1, String columnName2, String expectedString2, String columnName3, String expectedString3 )
    {
        if(getRelease() < JDTestDriver.RELEASE_V7R1M0){
            notApplicable("v5r5 variation");
            return;
        }
	if (!check71()) {
	    return; 
	}
        try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDB2DEF);

            PreparedStatement ps = connection_.prepareStatement (
                                                                 "INSERT INTO " + JDPSTest.PSTEST_SETDB2DEF
                                                                 + " ("+columnName1+","+columnName2+","+columnName3+") VALUES (?, ?, ?)");
            JDReflectionUtil.callMethod_V(ps, methodName, 1);
            JDReflectionUtil.callMethod_V(ps, methodName, 2);
            JDReflectionUtil.callMethod_V(ps, methodName, 3);

            ps.executeUpdate ();
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT "+columnName1+","+columnName2+","+columnName3+" FROM " + JDPSTest.PSTEST_SETDB2DEF);
            rs.next ();
            String retVal11= getColumnData(rs, columnName1); 
            String retVal12= getColumnData(rs, columnName2); 
            String retVal13= getColumnData(rs, columnName3); 
            rs.next ();
            String retVal21= getColumnData(rs, columnName1); 
            String retVal22= getColumnData(rs, columnName2); 
            String retVal23= getColumnData(rs, columnName3); 
            rs.close ();

            assertCondition (expectedString1.equals(retVal11) && 
                             expectedString2.equals(retVal12) &&
                             expectedString3.equals(retVal13) && 
                             expectedString1.equals(retVal21) && 
                             expectedString2.equals(retVal22) &&
                             expectedString3.equals(retVal23), 
                "Got '"+retVal11+"' and '"+retVal21+"' expected '"+expectedString1+"' for "+columnName1 +"..."+
                "Got '"+retVal12+"' and '"+retVal22+"' expected '"+expectedString2+"' for "+columnName2+"..."+
                "Got '"+retVal13+"' and '"+retVal23+"'expected '"+expectedString3+"' for "+columnName3);
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

    } 


    
    
    
    /**
SetDB2Default() - Set a various type parameters.
     **/
    public void Var007() { testOk("C_SMALLINT", C_SMALLINT_DEFAULT); }
    public void Var008() { testOk("C_INTEGER", C_INTEGER_DEFAULT); } 
    public void Var009() { testOk("C_REAL", C_REAL_DEFAULT);} 
    public void Var010() { testOk("C_FLOAT", C_FLOAT_DEFAULT);} 
    public void Var011() { testOk("C_DOUBLE", C_DOUBLE_DEFAULT);}
    public void Var012() { testOk("C_DECIMAL_105", C_DECIMAL_105_DEFAULT);} 
    public void Var013() { testOk("C_NUMERIC_105", C_NUMERIC_105_DEFAULT);} 
    public void Var014() { testOk("C_CHAR_50", C_CHAR_50_DEFAULT); } 
    public void Var015() { testOk("C_VARCHAR_50", C_VARCHAR_50_DEFAULT);} 
    public void Var016() { testOk("C_CLOB", C_CLOB_DEFAULT);} 
    public void Var017() { testOk("C_BINARY_20", C_BINARY_20_DEFAULT);} 
    public void Var018() { testOk("C_VARBINARY_20", C_VARBINARY_20_DEFAULT);} 
    public void Var019() { testOk("C_BLOB", C_BLOB_DEFAULT);} 
    public void Var020() { testOk("C_BIGINT", C_BIGINT_DEFAULT);} 
    public void Var021() { testOk("C_CFBD_20", C_CFBD_20_DEFAULT);} 
    public void Var022() { testOk("C_VCFBD_20", C_VCFBD_20_DEFAULT);} 
    public void Var023() { testOk("C_DBCLOB", C_DBCLOB_DEFAULT);} 
    public void Var024() { testOk("C_DATE", C_DATE_DEFAULT);} 
    public void Var025() { testOk("C_TIME", C_TIME_DEFAULT);} 
    public void Var026() { testOk("C_TIMESTAMP", C_TIMESTAMP_DEFAULT);} 
    public void Var027() { testOk("C_DISTINCT", C_DISTINCT_DEFAULT);} 
    public void Var028() { if (checkDecFloatSupport()) testOk("C_DECFLOAT16", C_DECFLOAT16_DEFAULT); } 
    public void Var029() { if (checkDecFloatSupport()) testOk("C_DECFLOAT34", C_DECFLOAT34_DEFAULT); } 
    public void Var030() { notApplicable("default values not supported for xml"); 
    /* if (checkXmlSupport()) testOk("C_XML", C_XML_DEFAULT); */ } 
    
    //
    // Test multiple columns
    // 
    public void Var031() { testOk("C_SMALLINT", C_SMALLINT_DEFAULT, "C_INTEGER", C_INTEGER_DEFAULT); } 
    public void Var032() { testOk("C_REAL", C_REAL_DEFAULT,"C_FLOAT", C_FLOAT_DEFAULT);} 
    public void Var033() { testOk("C_DOUBLE", C_DOUBLE_DEFAULT, "C_DECIMAL_105", C_DECIMAL_105_DEFAULT);} 
    public void Var034() { testOk("C_NUMERIC_105", C_NUMERIC_105_DEFAULT, "C_CHAR_50", C_CHAR_50_DEFAULT); } 
    public void Var035() { testOk("C_VARCHAR_50", C_VARCHAR_50_DEFAULT, 
        "C_CLOB", C_CLOB_DEFAULT, 
        "C_BINARY_20", C_BINARY_20_DEFAULT);} 
    public void Var036() { testOk("C_DATE", C_DATE_DEFAULT, "C_TIME", C_TIME_DEFAULT,"C_TIMESTAMP", C_TIMESTAMP_DEFAULT);} 
    public void Var037() { if (checkDecFloatSupport()) testOk("C_SMALLINT", C_SMALLINT_DEFAULT, 
                                                              "C_DECFLOAT16" , C_DECFLOAT16_DEFAULT); }
    public void Var038() { if (checkDecFloatSupport()) testOk("C_SMALLINT", C_SMALLINT_DEFAULT, 
        "C_DECFLOAT34" , C_DECFLOAT34_DEFAULT); }
    
    public void Var039() { notApplicable("default values not supported for xml");
      /* if (checkXmlSupport()) testOk("C_SMALLINT", C_SMALLINT_DEFAULT, 
        "C_XML" , C_XML_DEFAULT); */ }

    // 
    // Test multiple executes
    // 
    public void Var040() { testMultiple("C_SMALLINT", C_SMALLINT_DEFAULT); }
    public void Var041() { testMultiple("C_INTEGER", C_INTEGER_DEFAULT); } 
    public void Var042() { testMultiple("C_REAL", C_REAL_DEFAULT);} 
    public void Var043() { testMultiple("C_FLOAT", C_FLOAT_DEFAULT);} 
    public void Var044() { testMultiple("C_DOUBLE", C_DOUBLE_DEFAULT);}
    public void Var045() { testMultiple("C_DECIMAL_105", C_DECIMAL_105_DEFAULT);} 
    public void Var046() { testMultiple("C_NUMERIC_105", C_NUMERIC_105_DEFAULT);} 
    public void Var047() { testMultiple("C_CHAR_50", C_CHAR_50_DEFAULT); } 
    public void Var048() { testMultiple("C_VARCHAR_50", C_VARCHAR_50_DEFAULT);} 
    public void Var049() { testMultiple("C_CLOB", C_CLOB_DEFAULT);} 
    public void Var050() { testMultiple("C_BINARY_20", C_BINARY_20_DEFAULT);} 
    public void Var051() { testMultiple("C_VARBINARY_20", C_VARBINARY_20_DEFAULT);} 
    public void Var052() { testMultiple("C_BLOB", C_BLOB_DEFAULT);} 
    public void Var053() { testMultiple("C_BIGINT", C_BIGINT_DEFAULT);} 
    public void Var054() { testMultiple("C_CFBD_20", C_CFBD_20_DEFAULT);} 
    public void Var055() { testMultiple("C_VCFBD_20", C_VCFBD_20_DEFAULT);} 
    public void Var056() { testMultiple("C_DBCLOB", C_DBCLOB_DEFAULT);} 
    public void Var057() { testMultiple("C_DATE", C_DATE_DEFAULT);} 
    public void Var058() { testMultiple("C_TIME", C_TIME_DEFAULT);} 
    public void Var059() { testMultiple("C_TIMESTAMP", C_TIMESTAMP_DEFAULT);} 
    public void Var060() { testMultiple("C_DISTINCT", C_DISTINCT_DEFAULT);}
    public void Var061() { if (checkDecFloatSupport()) testMultiple("C_DECFLOAT16", C_DECFLOAT16_DEFAULT);}
    public void Var062() { if (checkDecFloatSupport()) testMultiple("C_DECFLOAT34", C_DECFLOAT34_DEFAULT);}
    public void Var063() { notApplicable("Default value cannot be specified for XML"); 
    /* if (checkXmlSupport()) testMultiple("C_XML", C_XML_DEFAULT);*/ 
    }
    
    
    
    
    public void Var064() { testMultiple("C_VARCHAR_50", C_VARCHAR_50_DEFAULT, 
        "C_CLOB", C_CLOB_DEFAULT, 
        "C_BINARY_20", C_BINARY_20_DEFAULT);} 
    public void Var065() { testMultiple("C_DATE", C_DATE_DEFAULT, "C_TIME", C_TIME_DEFAULT,"C_TIMESTAMP", C_TIMESTAMP_DEFAULT);} 
      
    
    public void Var066() { if (checkBooleanSupport()) testOk("C_BOOLEAN", C_BOOLEAN_DEFAULT_STRING);} 

    public void Var067() { if (checkBooleanSupport())  testMultiple("C_DATE", C_DATE_DEFAULT, "C_TIME", C_TIME_DEFAULT,"C_BOOLEAN", C_BOOLEAN_DEFAULT_STRING);} 
    
    
    // 
    // Test Block insert 
    //
    
    
}
