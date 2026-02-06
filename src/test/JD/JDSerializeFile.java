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

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Class to serialize access to a file using data area. 
  The pattern for using the file looks like this
    JDSerializeFile serializeFile = null;
    try {
     serializeFile = new JDSerializeFile(connection_, filename);
     .....
    } finally {
      if (serializeFile != null) {
        try {
          serializeFile.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
 */
public class JDSerializeFile implements AutoCloseable{
  String name_; 
  Statement statement_; 
  boolean closeStatement_ = false; 
  JDDataAreaLock dataAreaLock_ ; 
  
  public JDSerializeFile(Connection c, String name) throws Exception {
    String library = null; 
    int dotIndex = name.indexOf("."); 
    if (dotIndex > 0) { 
      library = name.substring(0,dotIndex); 
      name=name.substring(dotIndex+1); 
    }
    name_ = name; 
    statement_ = c.createStatement();
    closeStatement_ = true; 
    name = name.replace("/","X").replace(".","X").replace("\"","X");
    int len = name.length(); 
    if (len > 10) {
      name = name.substring(len-10); 
    }
    if (Character.isDigit(name.charAt(0))) { 
      name='X'+name.substring(1); 
    }
    if (library != null) { 
      dataAreaLock_ = new JDDataAreaLock(statement_, library, name);
    } else {
      dataAreaLock_ = new JDDataAreaLock(statement_, name);
    }
    dataAreaLock_.lock("JDSerializeFile", 3600);
  }

  public JDSerializeFile(Statement s, String name) throws SQLException {
    String library = null; 
    int dotIndex = name.indexOf("."); 
    if (dotIndex > 0) { 
      library = name.substring(0,dotIndex); 
      name=name.substring(dotIndex+1); 
    }
    name_ = name; 
    statement_ = s;
    name = name.replace("/","X").replace(".","X").replace("\"","X");
    int len = name.length(); 
    if (len > 10) {
      name = name.substring(len-10); 
    }
    if (Character.isDigit(name.charAt(0))) { 
      name='X'+name.substring(1); 
    }
    if (library != null) { 
      dataAreaLock_ = new JDDataAreaLock(statement_, library, name);
    } else {
      dataAreaLock_ = new JDDataAreaLock(statement_, name);
    }
    dataAreaLock_.lock("JDSerializeFile", 3600);
  }


  public String getName() { 
    return name_; 
  }
  
  /*
   * Must be called before the connection is closed  
   */
  public void close() throws SQLException {
    dataAreaLock_.unlock("JDSerializeFile", null);
    if (closeStatement_)
      statement_.close(); 
  }
  
  

}
