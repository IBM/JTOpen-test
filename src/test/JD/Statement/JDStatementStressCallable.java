///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDStatementStressCallable.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.JD.Statement;

import java.sql.*;

import test.JDTestcase;

public class JDStatementStressCallable extends Thread {
    // Private variables
    private Connection connection;
    private int instNum;
    private String collection;

    private boolean successful = true;
    StringBuffer errorInfo = new StringBuffer(); 
    static Object lockObject = new Object();
    

    /**
     Constructor.
     **/
    public JDStatementStressCallable(Connection connection, int instNum, String collection) {
        this.connection = connection;
        this.instNum = instNum;
        this.collection = collection;
    }

    /**
     What this thread does when it runs is here...
     **/
    public void run()
    {
        System.out.println("JDStatementStressCallable instance " + instNum + " is starting with name " + getName());
        try {
            PreparedStatement stmnt = connection.prepareStatement("CALL " + collection + ".STRPRC(?) ");
            ResultSet rs = null;

            for (int i = 1; i <= 200; ++i) {        
                boolean crashed = false;
                try {                            
                    synchronized (lockObject) {
                        stmnt.setInt(1, 1);
                        rs = stmnt.executeQuery();
                        rs.close();
                    }     
                } catch (Exception e) {
                    System.out.println("JDStatementStressCallable: Unexpected Exception." + "(" + getName() + ")");
                    System.out.println("   Loop #" + i + ": FAILED" + "(" + getName() + ")");
                    e.printStackTrace();
                    rs.close();
                    System.out.println();
                    
                    errorInfo.append("JDStatementStressCallable: Unexpected Exception." + "(" + getName() + ")\n");
                    errorInfo.append("   Loop #" + i + ": FAILED" + "(" + getName() + ")\n");
                    JDTestcase.printStackTraceToStringBuffer(e, errorInfo); 
                    
                    crashed = true;
                    successful = false;
                }
                if ((i == 199) || crashed) {                        
                    stmnt.close();
                    return;
                }
            }
        }
        catch (Exception e) {
            successful = false;
            System.out.println("Prepared Statement failed (" + getName() + ")");
            e.printStackTrace();
            
            errorInfo.append("Prepared Statement failed (" + getName() + ")\n");
            JDTestcase.printStackTraceToStringBuffer(e, errorInfo); 
            
            
        }
    }

    public boolean getSuccessful() {
        return successful;
    }

    public void getErrorInfo(StringBuffer sb) {
      sb.append(errorInfo); 
    }
}
