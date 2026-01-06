///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDStatementStressUpdate.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.JD.Statement;

import java.sql.*;
import java.io.PrintWriter; 
import test.JDTestDriver;
import test.JDTestcase;

public class JDStatementStressUpdate extends Thread {
    // Private variables
    private Connection connection;
    private int instNum;
    private String collection;
    private int runType = 0; 
    private boolean successful = true;
    private PreparedStatement updateStmt = null; 

    long runMillis; 
    String updateStatement; 
    int lowKey; 
    int highKey;
    StringBuffer errorInfo = new StringBuffer();
    private JDTestDriver testDriver_;
    private PrintWriter output_;   

    /**
     Constructor.  This is the old testcase 
     **/
    public JDStatementStressUpdate(JDTestDriver testDriver, Connection connection, int instNum, String collection, PrintWriter output) {
        this.connection = connection;
        this.instNum = instNum;
        this.collection = collection;
        this.runType = 1; 
        this.testDriver_ = testDriver; 
        this.output_ = output; 
    }

    /* 
     * New flavor to run using its own connection for a period of type
     * 
     * The update statement must has two parameters.
     * The first parameter is a string value that is the value to be updated.
     * The second parameter is an integer which ranges between lowKey and highKey
     */
    public JDStatementStressUpdate(JDTestDriver testDriver, String URL, String userId, char[] encryptedPassword, int instNum, long runMillis, String updateStatement, int lowKey, int highKey) throws Exception {
	this.testDriver_ = testDriver; 
      this.connection = testDriver_.getConnection (URL, userId, encryptedPassword ); 
      this.instNum = instNum; 
      this.runMillis = runMillis; 
      this.updateStatement=updateStatement; 
      this.lowKey = lowKey; 
      this.highKey = highKey; 
      this.updateStmt = connection.prepareStatement(updateStatement); 
         
      this.runType = 2; 
    }
    
    /**
     What this thread does when it runs is here...
     **/
    public void run() {
      setName("JDSSU_"+instNum);
      try {
	  Statement  s = connection.createStatement();
	  s.executeUpdate("THIS_IS_THREAD_JDSSU_"+instNum); 
	  s.close(); 
      } catch (Exception e) {
      } 
      switch(runType) {
	  case 1:  run1(); break; 
	  case 2:  run2(); break; 
      }
    }
    
    public void run1() {
        output_.println("JDStatementStressUpdate instance " + instNum + " is starting with name " + getName());
        try {
            PreparedStatement stmnt = connection.prepareStatement("UPDATE " + collection + ".STRESS SET BDATA = ? WHERE BID = ?");

            for (int i = 1; i <= 200; ++i) {        
                boolean crashed = false;
                try { 
                     stmnt.setInt(2, 1);
                     byte b[];
                     b = new byte[320];
                     for (int j=0; j<320;j++)
                         b[j] = (byte) (j % 0xff);
                     stmnt.setBytes(1, b);
                     stmnt.executeUpdate();
                } catch (Exception e) {
                    output_.println("JDStatementStressUpdate: Unexpected Exception." + "(" + getName() + ")");
                    output_.println("   Loop #" + i + ": FAILED" + "(" + getName() + ")");
                    e.printStackTrace();
                    errorInfo.append("JDStatementStressUpdate: Unexpected Exception." + "(" + getName() + ")\n");
                    errorInfo.append("   Loop #" + i + ": FAILED" + "(" + getName() + ")\n");
                    JDTestcase.printStackTraceToStringBuffer(e, errorInfo); 

                    crashed = true;
                    successful = false;
                }

                if ((i == 199) || crashed)
                {
                    stmnt.close();
                    return;
                }
            }
        } catch (Exception e) {
            successful = false;
            output_.println("Prepared Statement failed (" + getName() + ")");
            e.printStackTrace();
            errorInfo.append("Prepared Statement failed (" + getName() + ")\n");
            JDTestcase.printStackTraceToStringBuffer(e, errorInfo); 
            
        }
        output_.println("JDStatementStressUpdate instance " + instNum + " is ending with name " + getName());
    }

    public String[] strings = new String[100]; 
    public String generateString(int x) throws SQLException {
        String returnString = null; 
       if ( x >= strings.length) throw new SQLException("x not valid in generateString"); 
       returnString = strings[x];
       if (returnString == null) {
         StringBuffer sb = new StringBuffer(); 
         for (int i = 0 ; i < x; i++) { 
           sb.append( '0' + i % 50); 
         }
         returnString = sb.toString(); 
           strings[x] = returnString; 
       }
       return returnString; 
    }
    
    public void run2() {
      long endTime = System.currentTimeMillis() + runMillis; 
      output_.println("JDStatementStressUpdate instance " + instNum + " is starting with name " + getName());
      
      try {
          int i = 0;
          int key = lowKey; 
          boolean crashed = false;
          while (!crashed && System.currentTimeMillis() < endTime) { 
              try { 
                   updateStmt.setString(1, generateString(1+i%20));
                   updateStmt.setInt(2, key); 
                   updateStmt.executeUpdate();
              } catch (Exception e) {
                  output_.println("JDStatementStressUpdate: Unexpected Exception." + "(" + getName() + ")");
                  output_.println("   Loop #" + i + ": FAILED" + "(" + getName() + ")");
                  e.printStackTrace();
                  crashed = true;
                  successful = false;
                  errorInfo.append("JDStatementStressUpdate: Unexpected Exception." + "(" + getName() + ")\n");
                  errorInfo.append("   Loop #" + i + ": FAILED" + "(" + getName() + ")\n");
                  JDTestcase.printStackTraceToStringBuffer(e, errorInfo); 
                  
                  
                  return;
              }
              key++; 
              if (key > highKey) key = lowKey; 
              i++;
          }
          updateStmt.close(); 
          connection.close(); 
      } catch (Exception e) {
          successful = false;
          output_.println("Prepared Statement failed (" + getName() + ")");
          e.printStackTrace();
          errorInfo.append("Prepared Statement failed (" + getName() + ")\n");
          JDTestcase.printStackTraceToStringBuffer(e, errorInfo); 
          
      }
      output_.println("JDStatementStressUpdate instance " + instNum + " is ending with name " + getName());
  }

    
    
    public boolean getSuccessful() {
        return successful;
    }

    public void getErrorInfo(StringBuffer sb) {
      sb.append(errorInfo); 
      
    }
}
