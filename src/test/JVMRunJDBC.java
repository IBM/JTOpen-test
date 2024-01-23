///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JVMRunJDBC.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class JVMRunJDBC {

  /**
   * @param args
   */
  public static void main(String[] args) {
    PreparedStatement[] ps = null ; 
     try {
     String[] drivers = {
         "com.ibm.db2.jdbc.app.DB2Driver",
         "com.ibm.as400.access.AS400JDBCDriver",
     };
     
     for (int i = 0 ; i < drivers.length; i++) {
       try {
        Class.forName(drivers[i]);
      } catch (ClassNotFoundException e) {
        System.out.println("Warning:  Did not find class "+drivers[i]); 
      } 
     }
     
     System.out.println("Usage: java test.JVMRunJDBC <URL> <userid> <password> <preparedStatementCount>"); 
    
     Connection connection = null; 
     String URL=args[0];
     String userid = args[1];
     String password = args[2]; 
     int preparedStatementCount = Integer.parseInt(args[3]); 
        System.out.println("Connecting using "+URL); 
        connection = DriverManager.getConnection(URL, userid, password );
        System.out.println("Connected using "+URL);
          ps = new PreparedStatement[preparedStatementCount]; 
         for (int i = 0; i < ps.length; i++) { 
           if (i%10 == 0) {
              System.out.println("loop i="+i);
           }
           ps[i] = connection.prepareStatement("select * from sysibm.sysdummy1"); 
         }
       } catch (Exception e) { 
         e.printStackTrace(); 
       }
     System.gc();
     for (int i = 0; i < ps.length; i++) {
	 System.out.println("ps["+i+"] = "+ps[i]); 
     } 
     System.exit(0); 
  }

}
