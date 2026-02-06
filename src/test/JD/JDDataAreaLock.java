///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRunit.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 2025-2025 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.JD;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import test.JTOpenTestEnvironment;

public class JDDataAreaLock {

  private Statement s_;
  private String dataArea_;
  private String library_; 

  public JDDataAreaLock(Statement s, String dataArea) throws SQLException {
    if (dataArea.length() > 10) {
      throw new SQLException("Too long :"+dataArea); 
    }
    s_ = s; 
    dataArea_ = dataArea; 
    library_ = JTOpenTestEnvironment.testInfoSchema; 
  }

  
  public JDDataAreaLock(Statement s, String library, String dataArea) throws SQLException {
    if (dataArea.length() > 10) {
      throw new SQLException("Too long :"+dataArea); 
    }
    s_ = s; 
    dataArea_ = dataArea; 
    library_ = library ;
  }

  
  public void lock(String comment, int waitTime) throws SQLException {
    
    
    
    String sql="CALL QSYS2.QCMDEXC(' CRTDTAARA DTAARA("+library_+"/"+dataArea_+") TYPE(*CHAR) LEN(2000)')";
    try { 
      s_.execute(sql); 
      sql = "CALL QSYS2.QCMDEXC('GRTOBJAUT OBJ("+library_+"/"+dataArea_+") OBJTYPE(*DTAARA) USER(*PUBLIC) AUT(*ALL)')";
      s_.execute(sql); 
      

    } catch (Exception e) { 
       String message = e.getMessage(); 
       String expected = "exists";
       if (message.indexOf(expected) < 0) { 
         System.out.println("JDDataAreaLock: command failed:"+sql);
         throw e; 
       }
    }
    // Lock exclusive on the system and delay one second 
   try {  
    sql="CALL QSYS2.QCMDEXC('  ALCOBJ OBJ(("+library_+"/"+dataArea_+" *DTAARA *EXCLRD)) WAIT("+waitTime+")    ')";
    s_.execute(sql); 
    // Look at the data area to make sure it isn't already locked
    sql="select JOB_NAME, TRIM(INTERPRET(REPLACE(BINARY(DATA_AREA_VALUE),BX'00',BX'40') AS CHAR(2000) CCSID 37)) "
        + "from qsys2.data_area_info where DATA_AREA_LIBRARY='" + library_
        + "' AND DATA_AREA_NAME='" + dataArea_ + "'";
    try (ResultSet rs = s_.executeQuery(sql)) {
       if (rs.next()) {
        String data = rs.getString(2);
        if (data != null) {
          if (data.indexOf("LOCK") == 0) {
            String jobname = rs.getString(1);
            if (data.indexOf(jobname) > 0) {
              sql = "CALL QSYS2.QCMDEXC('  DLCOBJ OBJ((" + library_ + "/" + dataArea_
                  + " *DTAARA *EXCLRD))   ')";
              try {
                s_.execute(sql);
              } catch (SQLException sqlex) {
              }
              throw new SQLException("Data area " + library_ + "/" + dataArea_
                  + " already locked by this job " + jobname);
            }
          }
        }
      }
    }
  
    
    Timestamp ts = new Timestamp(System.currentTimeMillis());
    String callerString = getCallString(); 
    sql="CALL QSYS2.QCMDEXC(' CHGDTAARA DTAARA("+library_+"/"+dataArea_+" *ALL) VALUE(''LOCK ' || JOB_NAME || ' "+
    "                    "+
    pad50(comment+" "+ts.toString())+" "+callerString+"'')')";
   
    s_.execute(sql);
   } catch (Exception e) {
     System.out.println("JDDataAreaLock: Error on data area error with sql="+sql); 
     e.printStackTrace(System.out) ;
     throw e; 
   }
   
   
    
  }
  
  private String pad50(String inString) {
    StringBuffer sb = new StringBuffer(inString);
    int len = inString.length(); 
    int maxLen = 50 + (len / 50) * 50; 
    for (int i = len; i < maxLen; i++) { 
      sb.append(' '); 
    }
    return sb.toString();   
    
  }

  
  
  private String getCallString() {
    String stackString = "";
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace(); 
    int stackTraceLength = stackTrace.length;
    int i = 3;
    while( (i < 10) &&  ( i < stackTraceLength)) {
      StackTraceElement element = stackTrace[i]; 
      stackString+= pad50(element.getClassName()+"."+element.getMethodName()+":"+element.getLineNumber()); 
      i++; 
    }
    return stackString;
  }
  
  

  public void unlock(String comment, Timestamp ts) throws SQLException {
    
    String sql="NOTSET";
    try {
      if (ts == null)
        ts = new Timestamp(System.currentTimeMillis());
      String callerString = getCallString();
      sql = "CALL QSYS2.QCMDEXC(' CHGDTAARA DTAARA(" + library_ + "/" + dataArea_
          + " *ALL) VALUE(''UNLOCK ' || JOB_NAME || ' " + "                  " + pad50(comment + " " + ts.toString())
          + " " + callerString + "'')')";
      s_.execute(sql);
      sql = "CALL QSYS2.QCMDEXC('  DLCOBJ OBJ((" + library_ + "/" + dataArea_
          + " *DTAARA *EXCLRD))   ')";
      s_.execute(sql);

    } catch (SQLException e) {
      System.out.println("Error on " + sql);
      e.printStackTrace(System.out);
      throw e;
    }
  }
  
  
  
  

}
