///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDMDCCSID65535.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
//
//
//
//
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDDMDCCSID65535.java
//
// Classes:      JDDMDCCSID65535
//
////////////////////////////////////////////////////////////////////////
//
//
// 
//
////////////////////////////////////////////////////////////////////////

package test;

import java.sql.*;
import com.ibm.as400.access.AS400;

import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Testcase JDDMDCCSID65535. This tests the methods of the JDBC DatabaseMetaData
 * class that have problems with CCSID 65535 when calling the SYSIBM procedures.
 * 
 * <ul>
 * <li>
 * <li>
 * </ul>
 **/
public class JDDMDCCSID65535 extends JDTestcase {
  public final static String added = " -- added 03/01/2022";
  public static boolean useDbmon = true; 
  // Private data.
  private Connection connection_;
  private DatabaseMetaData dmd_;
  private Statement stmt_;
  private StringBuffer sb = new StringBuffer();

  /**
   * Constructor.
   **/
  public JDDMDCCSID65535(AS400 systemObject, Hashtable namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDDMDCCSID65535", namesAndVars, runMode,
        fileOutputStream, password);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {

    connection_ = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
    dmd_ = connection_.getMetaData();
    stmt_ = connection_.createStatement();

    JDDMDGetColumnPrivileges.setupPrivileges(stmt_, getDriver(), getRelease());
    
    stmt_.execute("CALL QSYS2.QCMDEXC('CHGJOB CCSID(65535)')");

  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   */
  protected void cleanup() throws Exception {


    stmt_.execute("CALL QSYS2.QCMDEXC('DSPJOBLOG JOB(*) OUTPUT(*PRINT)')");
    String sql="select 'DUMPED JOBLOG FOR ' || job_name || ',' || current user from sysibm.sysdummy1"; 
    ResultSet rs = stmt_.executeQuery(sql);
    rs.next();
    System.out.println(rs.getString(1));
    rs.close(); 
    JDDMDGetColumnPrivileges.cleanupPrivileges(stmt_, getDriver(), getRelease());
    connection_.close();

  }

  
    private boolean checkExpected(ResultSet rs, String[] expected,
      StringBuffer sb2) throws SQLException {
    boolean passed = true;
    for (int i = 0; i < expected.length; i++) {
      String s = rs.getString(i+1); 
      if (expected[i] == null) { 
        if (s != null) { 
        sb.append("For index=" + i + " got '" + s + "' sb null\n");
        passed = false;
        }
      } else if (expected[i].equals("NO_CHECK")) { 
        // do not check this column 
      } else { 
      if (!expected[i].equals(s)) {
        sb.append("For index=" + i + " got '" + s + "' sb '" + expected[i]
            + "'\n");
        passed = false;
      }
      } 
    }
    return passed;
  }

    private boolean checkColumnTypes(ResultSetMetaData rsmd,
        String[] expectedColumnTypes, StringBuffer sb ) throws  SQLException {
        boolean passed = true ; 
        
        for (int i = 0; i < expectedColumnTypes.length; i++) { 
          String typeName=rsmd.getColumnTypeName(i+1); 
          if (!expectedColumnTypes[i].equals(typeName)) {
            sb.append("For column "+(i+1)+":("+rsmd.getColumnName((i+1))+") expected '"+expectedColumnTypes[i]+"' but got '"+typeName+"' precision="+
             rsmd.getPrecision((i+1))+"\n");
            passed = false; 
          }
        }
        
        return passed; 
    }

  /**
   *
   * 
   */
  public void Var001() {
    try {
      String[] expected = { "NO_CHECK", "SYSIBM", "COLUMNS", "VIEW" };
      String[] expectedColumnTypes = { "VARCHAR", "VARCHAR", "VARCHAR", "VARCHAR"}; 
      boolean passed = true;
      boolean dynh_found = false;
      boolean emptyhj_found = false;


      sb.setLength(0);
      sb.append("STARTING DBMON TO QGPL/JDDMDCC1\n"); 
      if (useDbmon) stmt_.executeUpdate("CALL QSYS2.QCMDEXC('STRDBMON OUTFILE(QGPL/JDDMDCC1) INCSYSSQL(*YES)    ')"); 

      sb.append("Calling getTables(null,'SYSIBM','COLUMNS',null)\n");
      ResultSet rs = dmd_.getTables(null, "SYSIBM", "COLUMNS", null);
      ResultSetMetaData rsmd = rs.getMetaData(); 
      passed = checkColumnTypes(rsmd,expectedColumnTypes, sb); 
      while (rs.next()) {

        passed = checkExpected(rs, expected, sb) && passed ;

      }
      rs.close();

      // Run all other paths to check types

      /* Path EMPTYHJ */
      sb.append("Testing path EMPTYHJ\n"); 
      rs = dmd_.getTables("XXX", null, null, null);
      rsmd = rs.getMetaData(); 
      passed = checkColumnTypes(rsmd,expectedColumnTypes, sb) && passed; 
      rs.next();
      rs.close(); 

     if (useDbmon) stmt_.executeUpdate("CALL QSYS2.QCMDEXC('ENDDBMON   ')"); 

      rs = stmt_.executeQuery("select distinct(QQC181) from qgpl.jddmdCc1 WHERE QQC181 LIKE 'TABLES_%'");
      while(rs.next()) {
	  String value = rs.getString(1);
	  /* System.out.println("DBMON CURSOR: "+ value); */ 
	  if ( "TABLES_EMPTYHJ".equals(value)) {
	      emptyhj_found  = true; 
	  }
	  if ( "TABLES_DYNH".equals(value)) {
	      dynh_found = true; 
	  }
      } 
      rs.close(); 

      if (!dynh_found) {
	  passed = false;
	  sb.append("Did not found TABLES_DYNH\n"); 
      }
      if (!emptyhj_found) {
	  passed = false;
	  sb.append("Did not found TABLES_EMPTYHJ\n"); 
      } 

 
      assertCondition(passed, sb);

    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

    public void Var002() {
    try {
      String[] expected = { "NO_CHECK", "", "", "" };
      String[] expectedColumnTypes = { "VARCHAR", "VARCHAR", "VARCHAR", "VARCHAR"}; 
      boolean passed = true;
      sb.setLength(0);
      sb.append("Calling getTables(null,'','',null)\n");
      ResultSet rs = dmd_.getTables(null, "", "", null);
      ResultSetMetaData rsmd = rs.getMetaData(); 
      passed = checkColumnTypes(rsmd,expectedColumnTypes, sb); 
      while (rs.next()) {
        passed = checkExpected(rs, expected, sb) && passed ;

      }
      rs.close();

      assertCondition(passed, sb);

    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

    public void Var003() {
    try {
      String[] expected = { "NO_CHECK", "", "", "" };
      String[] expectedColumnTypes = { "VARCHAR", "VARCHAR", "VARCHAR", "VARCHAR"}; 
      boolean passed = true;
      sb.setLength(0);
      sb.append("Calling getTables('','','',null)\n");
      ResultSet rs = dmd_.getTables("", "", "", null);
      ResultSetMetaData rsmd = rs.getMetaData(); 
      passed = checkColumnTypes(rsmd,expectedColumnTypes, sb); 
      while (rs.next()) {
        passed = checkExpected(rs, expected, sb) && passed ;

      }
      rs.close();

      assertCondition(passed, sb);

    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }



   

    String[] expectedGetColumnsColumnTypes = { "VARCHAR", "VARCHAR", "VARCHAR", "VARCHAR","INTEGER","VARCHAR",
          "INTEGER","INTEGER","SMALLINT","SMALLINT","SMALLINT","NVARCHAR","NVARCHAR","SMALLINT",
          "SMALLINT","INTEGER","INTEGER","VARCHAR","VARCHAR","VARCHAR","VARCHAR","SMALLINT","VARCHAR"}; 

    
  public void Var004() {
    try {
      String[] expected = { "NO_CHECK","SYSIBM","COLUMNS","TABLE_NAME","12","VARCHAR",
          "128","128",null,null,"0",null,null,"12",null,"128","3","NO",null,null,null,null,"NO"};
	  boolean dynh_found = false;
	  boolean emptyhj_found = false;
      
      boolean passed = true;
      sb.setLength(0);

      if (useDbmon) { 
     
      String sql = "CALL QSYS2.QCMDEXC('STRDBMON OUTFILE(QGPL/JDDMDVAR04) INCSYSSQL(*YES)')";
      sb.append("Executing "+sql+"\n"); 
      stmt_.execute(sql);
   
      }
      
      sb.append("Calling getColumns(null,'SYSIBM','COLUMNS','TABLE_NAME')\n");
      ResultSet rs = dmd_.getColumns(null, "SYSIBM", "COLUMNS", "TABLE_NAME");
      ResultSetMetaData rsmd = rs.getMetaData(); 
      passed = checkColumnTypes(rsmd,expectedGetColumnsColumnTypes, sb); 
      while (rs.next()) {

        passed = checkExpected(rs, expected, sb) && passed ;

      }
      rs.close();


      /* col_emptyhj */ 
      rs = dmd_.getColumns("XXX", "SYSIBM", "COLUMNS", "TABLE_NAME");
      rs.next();
      rs.close();

      if (useDbmon) stmt_.executeUpdate("CALL QSYS2.QCMDEXC('ENDDBMON   ')"); 


      rs = stmt_.executeQuery("select distinct(QQC181) from qgpl.jddmdvar04 WHERE QQC181 IS NOT NULL");
      while(rs.next()) {
	  String value = rs.getString(1);
	  System.out.println("DBMON CURSOR: "+ value);

	  if ( "COL_EMPTYHJ".equals(value)) {
	      emptyhj_found  = true; 
	  }

	  if ( "COL_DYNH".equals(value))  {
	      dynh_found = true; 
	  }

      }
      rs.close(); 


      if (!dynh_found) {
	  passed = false;
	  sb.append("Did not found COL_DYNH\n"); 
      }
      if (!emptyhj_found) {
	  passed = false;
	  sb.append("Did not found COL_EMPTYHJ\n"); 
      } 


      assertCondition(passed, sb);

    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }


    public void Var005() {
    try {
      String[] expected = { "NO_CHECK","SYSIBM","COLUMNS","TABLE_NAME","12","VARCHAR",
          "128","128",null,null,"0",null,null,"12",null,"128","3","NO",null,null,null,null,"NO"};
          
      boolean passed = true;
      sb.setLength(0);
         
      sb.append("Calling getColumns('','SYSIBM','COLUMNS','TABLE_NAME')\n");
      ResultSet rs = dmd_.getColumns("", "SYSIBM", "COLUMNS", "TABLE_NAME");
      ResultSetMetaData rsmd = rs.getMetaData(); 
      passed = checkColumnTypes(rsmd,expectedGetColumnsColumnTypes, sb); 
      while (rs.next()) {

        passed = checkExpected(rs, expected, sb) && passed ;

      }
      rs.close();

      assertCondition(passed, sb);

    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

    public void Var006() {
    try {
      String[] expected = { "NO_CHECK","SYSIBM","COLUMNS","TABLE_NAME","12","VARCHAR",
          "128","128",null,null,"0",null,null,"12",null,"128","3","NO",null,null,null,null,"NO"};
          
      boolean passed = true;
      sb.setLength(0);

      sb.append("Calling getColumns(null,'','COLUMNS','TABLE_NAME')\n");
      ResultSet rs = dmd_.getColumns(null, "", "COLUMNS", "TABLE_NAME");
      ResultSetMetaData rsmd = rs.getMetaData(); 
      passed = checkColumnTypes(rsmd,expectedGetColumnsColumnTypes, sb); 
      while (rs.next()) {

        passed = checkExpected(rs, expected, sb) && passed ;

      }
      rs.close();

      assertCondition(passed, sb);

    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }


    String[] expectedGetColumnPrivilegesColumnTypes = { "VARCHAR", "VARCHAR", "VARCHAR", "VARCHAR",
        "VARCHAR","VARCHAR","VARCHAR","VARCHAR"}; 

 

    
  public void Var007() {
    try {
      String[] expected = { "NO_CHECK","NO_CHECK","CPRIVS","COLUMN",null,"NO_CHECK",
          "INSERT","YES"};
          
      boolean passed = true;
      sb.setLength(0);
      
      sb.append("Calling getColumnsPrivileges(null, "+JDDMDTest.COLLECTION+", 'CPRIVS','%')\n");
      ResultSet rs = dmd_.getColumnPrivileges(null, JDDMDTest.COLLECTION, "CPRIVS", "%");
      ResultSetMetaData rsmd = rs.getMetaData(); 
      passed = checkColumnTypes(rsmd,expectedGetColumnPrivilegesColumnTypes, sb); 
      rs.next();

        passed = checkExpected(rs, expected, sb) && passed ;

  
      rs.close();

      assertCondition(passed, sb);

    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }


   /* Use blank schema name to return 0 rows */ 
   public void Var008() {
    try {
           
      boolean passed = true;
      sb.setLength(0);
      
      if (useDbmon) stmt_.executeUpdate("CALL QSYS2.QCMDEXC('STRDBMON OUTFILE(QGPL/JDDMDCC7) INCSYSSQL(*YES)    ')"); 
      sb.append("Calling getColumns(null,'',null,'')\n");
      ResultSet rs = dmd_.getColumnPrivileges(null, "", null, "");
      ResultSetMetaData rsmd = rs.getMetaData(); 
      
      passed = checkColumnTypes(rsmd,expectedGetColumnPrivilegesColumnTypes, sb); 
      
      if (rs.next()) {
        passed = false; 
          sb.append("Row returned from query"); 
      }
      rs.close();

      if (useDbmon) stmt_.executeUpdate("CALL QSYS2.QCMDEXC('ENDDBMON   ')"); 

      assertCondition(passed, sb);

    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }



}
