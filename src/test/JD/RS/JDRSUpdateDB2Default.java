///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSUpdateDB2Default.java
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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDRSUpdateDB2Default.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>updateDB2Default()
</ul>
**/
public class JDRSUpdateDB2Default
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSUpdateDB2Default";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



    // Private data.
    private String              RSTEST_UPDTDB2DEF = JDRSTest.COLLECTION + ".RS_UPDEFA";
    private static final String key_            = "JDRSUpdateDB2Default";


    private  String select_         = "SELECT * FROM "
                                                    + RSTEST_UPDTDB2DEF;
 

    private Statement           statement_;
    private Statement           statement2_;
    private ResultSet           rs_;
    private String              url;
    String methodName = "updateDB2Default"; 


/**
Constructor.
**/
    public JDRSUpdateDB2Default (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSUpdateDB2Default",
            namesAndVars, runMode, fileOutputStream,
            password);
    }

    public JDRSUpdateDB2Default (AS400 systemObject,
				 String testname, 
                                    Hashtable namesAndVars,
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
	if (connection_ != null) connection_.close();
        RSTEST_UPDTDB2DEF = JDRSTest.COLLECTION + ".RS_UPDEFA";
        select_         = "SELECT * FROM " + RSTEST_UPDTDB2DEF;
     
        if (isJdbc20 ()) {
            // SQL400 - driver neutral...
            url = baseURL_
                + ";data truncation=true" 
                + ";errors=full" ;
//                + ";cursor sensitivity=insensitive";
            if(isToolboxDriver())	//@A1A
            	url += ";date format=iso;time format=iso";	//@A1A
            connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
            connection_.setAutoCommit(false); // @C1A
            statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
    

	    String booleanInsertValue = "";
            StringBuffer buffer = new StringBuffer();
            buffer.append(" (C_KEY VARCHAR(20)");
            buffer.append(",C_DUMMY           INTEGER"); 
            buffer.append(",C_SMALLINT        SMALLINT      default 99");
            buffer.append(",C_INTEGER         INTEGER       default 99");
            buffer.append(",C_REAL            REAL          default 99");
            buffer.append(",C_FLOAT           FLOAT         default 99");
            buffer.append(",C_DOUBLE          DOUBLE        default 99");
            buffer.append(",C_DECIMAL_50      DECIMAL(5,0)  default 99");
            buffer.append(",C_DECIMAL_105     DECIMAL(10,5) default 99");
            buffer.append(",C_NUMERIC_50      NUMERIC(5,0)  default 99");
            buffer.append(",C_NUMERIC_105     NUMERIC(10,5) default 99");
            buffer.append(",C_CHAR_1          CHAR          default '9'");
            buffer.append(",C_CHAR_50         CHAR(50)       default '99'");
            buffer.append(",C_VARCHAR_50      VARCHAR(50)    default '99' ");
            buffer.append(",C_CFBD_1          CHAR FOR BIT DATA        default '9'");
            buffer.append(",C_CFBD_20         CHAR(20)  FOR BIT DATA   default '9'");
            buffer.append(",C_VCFBD_20        VARCHAR(20) FOR BIT DATA  default '9'");
            buffer.append(",C_BINARY_20       BINARY(20)   default BINARY('9')");
            buffer.append(",C_VARBINARY_20    VARBINARY(20)  default VARBINARY('9')");
            buffer.append(",C_DATE            DATE         default '01/01/2000' ");
            buffer.append(",C_TIME            TIME         default '01:01:01' ");
            buffer.append(",C_TIMESTAMP       TIMESTAMP    default '1990-02-22-13.00.00'  ");
            buffer.append(",C_BLOB            BLOB(1)    default BLOB(X'F9')");
            buffer.append(",C_CLOB            CLOB(1)     default 'a'") ;
            buffer.append(",C_DBCLOB          DBCLOB(200) CCSID 13488 default '9'");
            buffer.append(",C_BIGINT          BIGINT default 9"); 
            if (areBooleansSupported()) { 
              buffer.append(",C_BOOLEAN         BOOLEAN default true");
	      booleanInsertValue=",false"; 
            }
            buffer.append(")");

            initTable(statement_, RSTEST_UPDTDB2DEF, buffer.toString());
                
            statement_.executeUpdate ("INSERT INTO " + RSTEST_UPDTDB2DEF
                + " (C_KEY) VALUES ('DUMMY_ROW')");

            statement_.executeUpdate ("INSERT INTO " + RSTEST_UPDTDB2DEF
                + " (C_KEY) VALUES ('DUMMY_ROW1')");

            String sql = "INSERT INTO " + RSTEST_UPDTDB2DEF 
            + "   VALUES ('JDRSUpdateDB2Default', "  /* KEY */
            + " 11, 11, 11, 11, 11, 11, "            /* DUMMY, SMALLINT.. DOUBLE */ 
            + " 11, 11, 11, 11,  "                   /* DECIMAL..NUMERIC */ 
            + " '1', '11', '11', "                    /* CHAR / VARCHAR */  
            + " '1', '11', '11', "                    /* FBD */
            + " BINARY('11'), VARBINARY('11'), "       /* binary, varbinary */ 
            + " '12/12/1999', '12:12:12', '1990-12-12-12.12.12', "  /* Date.. TS */ 
            + " BLOB(X'00'), CLOB('Z'), DBCLOB('Z'), 11 "
	    + booleanInsertValue 
            +")"; 
            try { 
              statement_.executeUpdate (sql);
            } catch (Exception e) {
              System.out.println("Error on "+sql); 
              e.printStackTrace(); 
              
            }
  
            connection_.commit();
            
            rs_ = statement_.executeQuery (select_ + " FOR UPDATE");
            
        }
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    
    {
      connection_.commit(); 
      connection_.close(); 
      connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
      statement_ = connection_.createStatement();	//@A1A
      
      cleanupTable( statement_,  RSTEST_UPDTDB2DEF);
      if (isJdbc20 ()) {
        rs_.close ();
        statement_.close ();
        connection_.commit(); // @C1A
        connection_.close ();
      }
      
      
      
    }
    

    private boolean check71() {
	if (getRelease() < JDTestDriver.RELEASE_V7R1M0 &&
	    methodName.equals("updateDBDefault")) {
	    notApplicable("updateDBDefault is V7R1 variation");
	    return false; 
	}
	return true; 
    } 



/**
updateDB2Default() - Should throw exception when the result set is
closed.
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
        if (checkJdbc20 ()) {
        try {
            Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + RSTEST_UPDTDB2DEF + " FOR UPDATE");
            rs.next ();
            rs.close ();
            JDReflectionUtil.callMethod_V(rs, methodName, "C_VARCHAR_50");
            //rs.updateNull(13);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateDB2Default() - Should throw exception when the result set is
not updatable.
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
        if (checkJdbc20 ()) {
        try {
            Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + RSTEST_UPDTDB2DEF);
            rs.next ();
            JDReflectionUtil.callMethod_V(rs, methodName, "C_VARCHAR_50");
             
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateDB2Default() - Should throw exception when cursor is not pointing
to a row.
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
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, null);
            JDReflectionUtil.callMethod_V(rs_, methodName, "C_VARCHAR_50");
            
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateDB2Default() - Should throw an exception when the column
is an invalid index.
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
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            JDReflectionUtil.callMethod_V(rs_, methodName, 100);
            
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateDB2Default() - Should throw an exception when the column
is 0.
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
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            JDReflectionUtil.callMethod_V(rs_, methodName, 0);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateDB2Default() - Should throw an exception when the column
is -1.
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
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            JDReflectionUtil.callMethod_V(rs_, methodName, -1);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateDB2Default() - Should work when the column index is valid.
**/
    public void Var007()
    {
        if(getRelease() < JDTestDriver.RELEASE_V7R1M0){
            notApplicable("v5r5 variation");
            return;
        }
	if (!check71()) {
	    return; 
	}
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateInt("C_DUMMY", 1); 
            JDReflectionUtil.callMethod_V(rs_, methodName, "C_VARCHAR_50");
            rs_.updateRow ();
             
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            String v = rs2.getString ("C_VARCHAR_50");
            
            rs2.close ();
            assertCondition (v != null && v.equals("99"), "Got "+v+" sb 99 for VARCHAR");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateDB2Default() - Should throw an exception when the column
name is null.
**/
    public void Var008()
    {
      
      
      if(getRelease() < JDTestDriver.RELEASE_V7R1M0){
        notApplicable("v5r5 variation");
        return;
      }
	if (!check71()) {
	    return; 
	}
      if (checkJdbc20 ()) {
        try {
          JDRSTest.position (rs_, key_);
          Class[] argTypes = new Class[1]; 
          argTypes[0] = Class.forName("java.lang.String"); 
          Object args[] = new Object[1]; 
          args[0] = null; 
          JDReflectionUtil.callMethod_V(rs_, methodName, argTypes, args); 
          failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
          assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
      }
    }
    


/**
updateDB2Default() - Should throw an exception when the column
name is an empty string.
**/
    public void Var009()
    {
        if(getRelease() < JDTestDriver.RELEASE_V7R1M0){
            notApplicable("v5r5 variation");
            return;
        }
	if (!check71()) {
	    return; 
	}
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            JDReflectionUtil.callMethod_V(rs_, methodName, "");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateDB2Default() - Should throw an exception when the column
name is invalid.
**/
    public void Var010()
    {
        if(getRelease() < JDTestDriver.RELEASE_V7R1M0){
            notApplicable("v5r5 variation");
            return;
        }
	if (!check71()) {
	    return; 
	}
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            JDReflectionUtil.callMethod_V(rs_, methodName, "INVALID");
   //         rs_.updateDB2Default ("INVALID");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateDB2Default() - Should work when the column name is valid.
**/
    public void Var011()
    {
        if(getRelease() < JDTestDriver.RELEASE_V7R1M0){
            notApplicable("v5r5 variation");
            return;
        }
 	if (!check71()) {
	    return; 
	}
       if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            JDReflectionUtil.callMethod_V(rs_, methodName, "C_VARCHAR_50");
            rs_.updateInt("C_DUMMY", 1); 
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            String v = rs2.getString ("C_VARCHAR_50");
             
            rs2.close ();
            assertCondition (v!= null && v.equals("99"), "v="+v+" sb 99 for VARCHAR");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }

 

 



/**
updateDB2Default() - Should be reflected by get, after update has
been issued and cursor has been repositioned.
**/
    public void Var012()
    {
        if(getRelease() < JDTestDriver.RELEASE_V7R1M0){
            notApplicable("v5r5 variation");
            return;
        }
	if (!check71()) {
	    return; 
	}
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            JDReflectionUtil.callMethod_V(rs_, methodName, "C_VARCHAR_50");
            rs_.updateRow ();
            rs_.beforeFirst ();
            JDRSTest.position (rs_, key_);
            String v = rs_.getString ("C_VARCHAR_50");
             
            assertCondition (v != null && v.equals("99"), "v = "+v+" sb 99 for VARCHAR");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
updateDB2Default() - Should work when the current row is the insert
row.
**/
    public void Var013()
    {
        if(getRelease() < JDTestDriver.RELEASE_V7R1M0){
            notApplicable("v5r5 variation");
            return;
        }
	if (!check71()) {
	    return; 
	}
        if (checkJdbc20 ()) {
        try {
            rs_.moveToInsertRow ();
            rs_.updateString ("C_KEY", "JDRSUpdateDB2Def 1");
            JDReflectionUtil.callMethod_V(rs_, methodName, "C_VARCHAR_50");
 //           rs_.updateDB2Default ("C_VARCHAR_50");
            rs_.insertRow ();
            JDRSTest.position (rs_, "JDRSUpdateDB2Def 1");
            String v = rs_.getString ("C_VARCHAR_50");
            connection_.commit();
            assertCondition (v != null && v.equals("99"), "v="+v+" sb 99" );
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }


 
           

/**
updateDB2Default() - Should throw an exception on a deleted row.
**/
    public void Var014()
    {
        if(getRelease() < JDTestDriver.RELEASE_V7R1M0){
            notApplicable("v5r5 variation");
            return;
        }
 	if (!check71()) {
	    return; 
	}
       if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "DUMMY_ROW");
            rs_.deleteRow ();
            JDReflectionUtil.callMethod_V(rs_, methodName, "C_VARCHAR_50");
            //rs_.updateDB2Default ("C_VARCHAR_50");
            failed ("Didn't throw SQLException on deleted row");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
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
    

    public void updateTest(String columnName, String expected ) {
      if(getRelease() < JDTestDriver.RELEASE_V7R1M0){
        notApplicable("v5r5 variation");
        return;
      }
	if (!check71()) {
	    return; 
	}
      if (checkJdbc20 ()) {
        try {
          JDRSTest.position (rs_, key_);
          JDReflectionUtil.callMethod_V(rs_, methodName, columnName);
          rs_.updateRow ();
          ResultSet rs2 = statement2_.executeQuery (select_);
          JDRSTest.position (rs2, key_);
          
          String v = getColumnData(rs2, columnName);
          
          rs2.close ();
          connection_.commit();
          assertCondition (expected.equals(v), "v = "+v+" sb "+expected+" for "+columnName );
        }
        catch (Exception e) {
          failed (e, "Unexpected Exception");
        }
      }
      
    }
/**
updateDB2Default() - Update a various types (readable as string) 
**/
    public void Var015 () { updateTest("C_SMALLINT", "99");  }
    public void Var016 () { updateTest("C_INTEGER", "99"); } 
    public void Var017 () { updateTest("C_REAL", "99.0"); }
    public void Var018 () { updateTest("C_FLOAT", "99.0"); } 
    public void Var019 () { updateTest("C_DOUBLE", "99.0"); } 
    public void Var020 () { updateTest("C_DECIMAL_105", "99.00000"); } 
    public void Var021 () { updateTest("C_NUMERIC_105", "99.00000"); } 
    public void Var022 () { updateTest("C_CHAR_50", "99                                                "); } 
    public void Var023 () { updateTest("C_VARCHAR_50", "99"); } 
    public void Var024 () { updateTest("C_CFBD_20", "f940404040404040404040404040404040404040"); }
    public void Var025 () { updateTest("C_VCFBD_20", "f9"); }
    public void Var026 () { updateTest("C_BINARY_20", "f940404040404040404040404040404040404040"); }
    public void Var027 () { updateTest("C_VARBINARY_20", "f9");} 
    public void Var028 () { updateTest("C_DATE", "2000-01-01");}
    public void Var029 () { updateTest("C_TIME", "01.01.01"); }
    public void Var030 () { updateTest("C_TIMESTAMP", "1990-02-22 13:00:00.000000");}
    public void Var031 () { updateTest("C_BLOB", "f9");}
    public void Var032 () { updateTest("C_CLOB","a") ; }
    public void Var033 () { updateTest("C_DBCLOB", "9"); }
    public void Var034 () { updateTest("C_BIGINT", "9"); }
    public void Var035 () { if (checkBooleanSupport()) updateTest("C_BOOLEAN", "1"); }
    


}


