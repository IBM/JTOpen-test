///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDStatementStressSelect.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.JD.Statement;

import java.sql.*;

import test.JDTestDriver;
import test.JDTestcase;

public class JDStatementStressSelect extends Thread {
    // Private variables
    private Connection connection;
    private int instNum;
    private String collection;

    private int runType = 0; 
    private boolean successful = true;
    StringBuffer errorInfo = new StringBuffer(); 
    private PreparedStatement selectStmt = null; 
   
    long runMillis; 
    String selectStatement; 
    int lowKey; 
    int highKey;

    /**
     Constructor.
     **/
    public JDStatementStressSelect(Connection connection, int instNum, String collection) {
        this.connection = connection;
        this.instNum = instNum;
        this.collection = collection;
        
        this.runType = 1; 

    }

    /* 
     * New flavor to run using its own connection for a period of type
     * 
     * The update statement must has two parameters.
     * The first parameter is a string value that is the value to be updated.
     * The second parameter is an integer which ranges between lowKey and highKey
     */

    public JDStatementStressSelect(JDTestDriver testDriver, String url, String userid, char[] encryptedPassword,
        int instNum, long runMillis, String selectStatement, int lowKey, int highKey) throws Exception {

      this.connection = testDriver.getConnection (url, userid, encryptedPassword ); 
      this.instNum = instNum; 
      this.runMillis = runMillis; 
      this.selectStatement=selectStatement; 
      this.lowKey = lowKey; 
      this.highKey = highKey; 
      this.selectStmt = connection.prepareStatement(selectStatement); 
         
      this.runType = 2; 

    
    
    }

    public void run() {
      switch(runType) {
      case 1:  run1(); break; 
      case 2:  run2(); break; 
      }
    }

    /**
     What this thread does when it runs is here...
     **/
    public void run1() {
        System.out.println("JDStatementStressSelect instance " + instNum + " is starting with name " + getName());
        boolean crashed = false;
        for (int i = 1; !crashed && i<= 200; ++i) {
            try {
                PreparedStatement stmnt = connection.prepareStatement("SELECT * FROM " + collection 
                                                                      + ".STRESS WHERE BID=?");
                stmnt.setInt(1, 1);
                ResultSet rs = stmnt.executeQuery();

                if ((i % 10) == 1) {                   
                    rs.next();
                    rs.getString(1);
                }  

                stmnt.close();                 
            } catch (Exception e) {
                System.out.println("JDStatementStressSelect: Unexpected Exception." + "(" + getName() + ")");
                System.out.println("   Loop #" + i + ": FAILED" + "(" + getName() + ")");
                e.printStackTrace();
                crashed = true;
                successful = false;

                errorInfo.append("JDStatementStressSelect: Unexpected Exception." + "(" + getName() + ")\n");
                errorInfo.append("   Loop #" + i + ": FAILED" + "(" + getName() + ")\n");
                JDTestcase.printStackTraceToStringBuffer(e, errorInfo); 
               
                
                
            }
            if ((i == 199) || crashed)                
            {
                return;
            }
        }
    }
    
    
    public void run2() {
      long endTime = System.currentTimeMillis() + runMillis; 
      System.out.println("JDStatementStressSelect instance " + instNum + " is starting with name " + getName());
      
      try {
          int i = 0;
          int key = lowKey; 
          while (System.currentTimeMillis() < endTime) { 
              try { 
                selectStmt.setInt(1, key); 
                selectStmt.setInt(2, highKey); 
                ResultSet rs = selectStmt.executeQuery();
                while (rs.next()) {
                  rs.getString(1); 
                }
                rs.close(); 
              } catch (Exception e) {
                  System.out.println("JDStatementStressSelect: Unexpected Exception." + "(" + getName() + ")");
                  System.out.println("   Loop #" + i + ": FAILED" + "(" + getName() + ")");
                  e.printStackTrace();
                  successful = false;
                  
                  errorInfo.append("JDStatementStressSelect: Unexpected Exception." + "(" + getName() + ")\n");
                  errorInfo.append("   Loop #" + i + ": FAILED" + "(" + getName() + ")\n");
                  JDTestcase.printStackTraceToStringBuffer(e, errorInfo); 

                  return;
              }
              key++; 
              if (key > highKey) key = lowKey; 
              i++;
          }
          selectStmt.close(); 
          connection.close(); 
      } catch (Exception e) {
          successful = false;
          System.out.println("Prepared Statement failed (" + getName() + ")");
          e.printStackTrace();

          errorInfo.append("Prepared Statement failed (" + getName() + ")\n");
          JDTestcase.printStackTraceToStringBuffer(e,errorInfo); 

      
      }
      System.out.println("JDStatementStressSelect instance " + instNum + " is ending with name " + getName());
  }


    public boolean getSuccessful() {
        return successful;
    }

    public void getErrorInfo(StringBuffer sb) {
      sb.append(errorInfo); 
    }
}
