///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDASPreparedStatement.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////
//
//  File Name: JDASPreparedStatement.java
//  Classes:   JDASPeparedStatement
//
////////////////////////////////////////////////////////////////////////////
// 
////////////////////////////////////////////////////////////////////////////
package test.JD.AS;


import java.io.FileOutputStream;
import java.util.Hashtable; import java.util.Vector;
import java.sql.*;

import com.ibm.as400.access.AS400;

/**
 * Testcase JDASPreparedStatement
 **/
public class JDASPreparedStatement extends JDASTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDASPreparedStatement";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDASTest.main(newArgs); 
   }


  private Statement statement_;


  /**
   * Constructor.
   **/
  public JDASPreparedStatement(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password, String pwrSysUserID, String pwrSysPassword) {
    super(systemObject, "JDASPreparedStatement", namesAndVars, runMode,
        fileOutputStream, password, pwrSysUserID, pwrSysPassword);
    // originalPrintWriter_ = Trace.getPrintWriter();
  }

  public JDASPreparedStatement(AS400 systemObject, String testname, Hashtable<String,Vector<String>> namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password, String pwrSysUserID, String pwrSysPassword) {
    super(systemObject, testname, namesAndVars, runMode,
        fileOutputStream, password, pwrSysUserID, pwrSysPassword);
    // originalPrintWriter_ = Trace.getPrintWriter();
  }

  
  
  public void setup() {
    String sql = "";
    try {

      if (isToolboxFixDate(TOOLBOX_FIX_DATE)) {

        String url = baseURL_;

        url += ";user=" + systemObject_.getUserId() 
            + ";enableClientAffinitiesList=1;date format=iso";
        connection_ = testDriver_.getConnection(url, systemObject_.getUserId(), encryptedPassword_);

        statement_ = connection_.createStatement(); 
      }

    } catch (Exception e) {
      output_.println("Setup error.");
      output_.println("Last sql statement was the following");
      output_.println(sql);
      e.printStackTrace(output_);
    }
  }

  /**
   * @exception Exception
   *              If an exception occurs.
   **/
  public void cleanup() throws Exception {
    try {
      if (isToolboxFixDate(TOOLBOX_FIX_DATE)) {
        
      }
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  /**
   * 
   **/

  /* Test an integer parameter where the connection is re-established before the execute */ 
  
  public void testParameterForSwitchBeforeExecute(String testname, String sql, String stringValue) {
    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      boolean passed = true;
      StringBuffer sb = new StringBuffer();
      sb.append(" testParameterForSwitchBeforeExecute "+testname); 
      sb.append("\n"); 
      
      try {
          
        PreparedStatement ps = connection_.prepareStatement(sql); 
        ps.setString(1, stringValue);
        try { 
          statement_.execute("CALL QSYS2.QCMDEXC('ENDJOB JOB(*) OPTION(*IMMED)')");
          passed = false; 
          sb.append("Did not get exception when ending job\n"); 
        } catch (SQLException e) { 
          sb.append("Expected exception received when ending job "+e+"\n"); 
        }
        ResultSet rs = ps.executeQuery();
        rs.next(); 
        String outValue = rs.getString(1); 
        if (!stringValue.equals(outValue)) { 
          passed = false; 
          sb.append("Error: got "+outValue+" should be "+stringValue); 
        }
        ps.close(); 
        
        
        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {

    }
  }

  public void Var001() { testParameterForSwitchBeforeExecute("BIGINT", "SELECT CAST(? AS BIGINT) FROM SYSIBM.SYSDUMMY1", "6782847"); }
  public void Var002() { testParameterForSwitchBeforeExecute("BINARY", "SELECT CAST(? AS BINARY(2)) FROM SYSIBM.SYSDUMMY1", "EBE7"); }
  public void Var003() { testParameterForSwitchBeforeExecute("BLOB",   "SELECT CAST(? AS BLOB(100)) FROM SYSIBM.SYSDUMMY1", "EBE7"); }
  public void Var004() { testParameterForSwitchBeforeExecute("CHAR",   "SELECT CAST(? AS CHAR(2)) FROM SYSIBM.SYSDUMMY1", "eb"); }
  public void Var005() { testParameterForSwitchBeforeExecute("CLOB",   "SELECT CAST(? AS CLOB(100)) FROM SYSIBM.SYSDUMMY1", "eb"); }
  public void Var006() { testParameterForSwitchBeforeExecute("DATE",   "SELECT CAST(? AS DATE) FROM SYSIBM.SYSDUMMY1", "2019-12-11"); }
  public void Var007() { testParameterForSwitchBeforeExecute("DBCLOB",   "SELECT CAST(? AS DBCLOB(100) CCSID 13488) FROM SYSIBM.SYSDUMMY1", "eb"); }
  public void Var008() { testParameterForSwitchBeforeExecute("DECFLOAT16",   "SELECT CAST(? AS DECFLOAT(16)) FROM SYSIBM.SYSDUMMY1", "1.78"); }
  public void Var009() { testParameterForSwitchBeforeExecute("DECFLOAT34",   "SELECT CAST(? AS DECFLOAT(34)) FROM SYSIBM.SYSDUMMY1", "1.78"); }
  public void Var010() { testParameterForSwitchBeforeExecute("DECIMAL",   "SELECT CAST(? AS DECIMAL(10,2)) FROM SYSIBM.SYSDUMMY1", "1.78"); }
  public void Var011() { testParameterForSwitchBeforeExecute("DOUBLE",   "SELECT CAST(? AS DOUBLE) FROM SYSIBM.SYSDUMMY1", "1.78"); }
  public void Var012() { testParameterForSwitchBeforeExecute("FLOAT",   "SELECT CAST(? AS FLOAT) FROM SYSIBM.SYSDUMMY1", "1.78"); }
  public void Var013() { testParameterForSwitchBeforeExecute("GRAPHIC",   "SELECT CAST(? AS GRAPHIC(2) CCSID 13488) FROM SYSIBM.SYSDUMMY1", "eb"); }
  public void Var014() { testParameterForSwitchBeforeExecute("Integer", "SELECT CAST(? AS INTEGER) FROM SYSIBM.SYSDUMMY1", "6782847"); }
  public void Var015() { testParameterForSwitchBeforeExecute("Long NVARCHAR",   "SELECT CAST(? AS NVARCHAR(16000) ) FROM SYSIBM.SYSDUMMY1", "eb"); }
  public void Var016() { testParameterForSwitchBeforeExecute("Long VARCHAR",   "SELECT CAST(? AS VARCHAR(30000) ) FROM SYSIBM.SYSDUMMY1", "eb"); }
  public void Var017() { testParameterForSwitchBeforeExecute("Long VARCHAR FBD",   "SELECT CAST(? AS VARCHAR(30000) CCSID 65535 ) FROM SYSIBM.SYSDUMMY1", "EB"); }
  public void Var018() { testParameterForSwitchBeforeExecute("Long VARGRAPHIC",   "SELECT CAST(? AS VARGRAPHIC(16000) CCSID 1200 ) FROM SYSIBM.SYSDUMMY1", "eb"); }
  public void Var019() { testParameterForSwitchBeforeExecute("NCHAR",   "SELECT CAST(? AS NCHAR(2) ) FROM SYSIBM.SYSDUMMY1", "eb"); }
  public void Var020() { testParameterForSwitchBeforeExecute("NCLOB",   "SELECT CAST(? AS NCLOB(2) ) FROM SYSIBM.SYSDUMMY1", "eb"); }
  public void Var021() { testParameterForSwitchBeforeExecute("NUMERIC",   "SELECT CAST(? AS NUMERIC(10,2)) FROM SYSIBM.SYSDUMMY1", "1.78"); }
  public void Var022() { testParameterForSwitchBeforeExecute("NVARCHAR",   "SELECT CAST(? AS NVARCHAR(30) ) FROM SYSIBM.SYSDUMMY1", "eb"); }
  public void Var023() { testParameterForSwitchBeforeExecute("REAL",   "SELECT CAST(? AS REAL) FROM SYSIBM.SYSDUMMY1", "1.78"); }
  public void Var024() { testParameterForSwitchBeforeExecute("SMALLINT", "SELECT CAST(? AS SMALLINT) FROM SYSIBM.SYSDUMMY1", "2847"); }
  public void Var025() { testParameterForSwitchBeforeExecute("TIME", "SELECT CAST(? AS TIME) FROM SYSIBM.SYSDUMMY1", "11:28:47"); }
  public void Var026() { testParameterForSwitchBeforeExecute("TIMESTAMP", "SELECT CAST(? AS TIMESTAMP) FROM SYSIBM.SYSDUMMY1", "2019-11-27 11:28:47.023336"); }
  public void Var027() { testParameterForSwitchBeforeExecute("VARBINARY", "SELECT CAST(? AS VARBINARY(20)) FROM SYSIBM.SYSDUMMY1", "EBE7"); }
  public void Var028() { testParameterForSwitchBeforeExecute("VARCHAR",   "SELECT CAST(? AS VARCHAR(30) ) FROM SYSIBM.SYSDUMMY1", "eb"); }
  public void Var029() { testParameterForSwitchBeforeExecute("VARCHAR FOR BIT DATA", "SELECT CAST(? AS VARCHAR(20) CCSID 65535) FROM SYSIBM.SYSDUMMY1", "EBE7"); }
  public void Var030() { testParameterForSwitchBeforeExecute("VARGRAPHIC",   "SELECT CAST(? AS VARGRAPHIC(20) CCSID 13488) FROM SYSIBM.SYSDUMMY1", "eb"); }
  public void Var031() { if (checkBooleanSupport()) testParameterForSwitchBeforeExecute("BOOLEAN",   "SELECT CAST(? AS BOOLEAN) FROM SYSIBM.SYSDUMMY1", "1"); }
  public void Var032() { if (checkBooleanSupport()) testParameterForSwitchBeforeExecute("BOOLEAN",   "SELECT CAST(? AS BOOLEAN) FROM SYSIBM.SYSDUMMY1", "0"); }
  
  
  


  
  

  
  
  
  
}
