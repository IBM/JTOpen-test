///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDASPTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDASPTestcase.java
//
// Classes:      JDASPTestcase
//
////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.*;

import java.io.FileOutputStream;
import java.sql.*;         
import java.util.*;



public class JDASPTestcase
extends JDTestcase 
{
                                                           
   String iasp_ = null;                                
   String URLWithIASP_ = null;
   String URLWithSYSBAS_ = null;

   String userid_ = null;
   String password_1 = null;   
   String puserid_ = null;
   String ppassword_ = null;   

   AS400  system_1 = null;

   boolean setupWorked_ = true;

   String collection_1 = "JDTESTASP";
   String file_       = "ASPTEST";
   String name_1       = collection_1 + "." + file_;
   boolean iaspAvailable = false; 
        
/**
Constructor.
**/
    public JDASPTestcase (AS400 systemObject,
                          Hashtable namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password,
                          String iasp,
                          String powerUID,
                          String powerPWD)
    {
        super (systemObject, "JDASPTestcase",
               namesAndVars, runMode, fileOutputStream, password);
               
        iasp_ = iasp;
        userid_ = systemObject.getUserId();  
        char[] encryptedPassword = PasswordVault.getEncryptedPassword(password);
        password = PasswordVault.decryptPasswordLeak(encryptedPassword); 
        password_1 = password;
        system_1 = systemObject;
        
        if (powerUID != null)
        {
           puserid_   = powerUID;
           ppassword_ = powerPWD;
        }
        else
        {
           puserid_   = userid_;
           ppassword_ = password_1;
        }   
    }



/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
    throws Exception
    {            
        URLWithIASP_   = baseURL_ + ";database name=" + iasp_;
        URLWithSYSBAS_ = baseURL_ + ";database name=*SYSBAS";

       if (getDriver() == JDTestDriver.DRIVER_NATIVE)
       {
	   String replaceString = testDriver_.getRdbName();
	   if (replaceString == null) {
	       replaceString = testDriver_.getSystemName();
	   }
	   System.out.println("baseURL_ is "+baseURL_);
	   System.out.println("replaceString is "+replaceString); 
	   URLWithIASP_   = baseURL_.replace(replaceString,  iasp_);
	   System.out.println("URLWithIASP_ is "+URLWithIASP_); 
	   URLWithSYSBAS_ = baseURL_;
       }

	// Verify that IASP's are available.
	try { 
	    Connection c  = DriverManager.getConnection(baseURL_, userid_, password_1);
	    Statement s = c.createStatement();
	    iaspAvailable=false;
	    System.out.println("The following IASPs are available on the system"); 
	    ResultSet rs = s.executeQuery("select * from qsys2.syscatalogs where CATALOG_STATUS='VARYOFF' OR (RDBTYPE='LOCAL' and CATALOG_ASPNUM > 0 )");
	    ResultSetMetaData rsmd = rs.getMetaData(); 
	    int columns = rsmd.getColumnCount(); 
	    while (rs.next()) {
		iaspAvailable = true;
		for (int i = 1; i <= columns; i++) {
		    System.out.print(rs.getString(i)+","); 
		}
		String iaspName = rs.getString(1);
		if (iaspName.equalsIgnoreCase(iasp_)) {
		    String status = (""+rs.getString(2)).trim();
		    if (status.equals("ACTIVE")) {
		      /* Need to vary off first */ 
                      System.out.println("Warning:  IASP "+iasp_+" is only active.. Attempting to vary off");
                      boolean check = false; 
                      try { 
                          CommandCall cmd = new CommandCall(systemObject_, "VRYCFG CFGOBJ("+iasp_+") CFGTYPE(*DEV) STATUS(*OFF) ");
                          cmd.setThreadSafe(true);
                          check = cmd.run();
                          System.out.println("Varyoff="+check); 
                          status="VARYOFF"; 
                      } catch (Exception e) {
                          e.printStackTrace(); 
                      } 
		      
		    }
		    if (status.equals("VARYOFF")) {
			System.out.println("Warning:  IASP "+iasp_+" is varied off.. Attempting to vary on");
			boolean check = false; 
			try { 
			    CommandCall cmd = new CommandCall(systemObject_, "VRYCFG CFGOBJ("+iasp_+") CFGTYPE(*DEV) STATUS(*ON)                                    ");
			    cmd.setThreadSafe(true);
			    check = cmd.run();
			} catch (Exception e) {
			    e.printStackTrace(); 
			} 
			if (!check) {
			    System.out.println("Varyon failed. Sleeping for 300 seconds"); 
			    try {
				Thread.sleep(300000); 
			    } catch (Exception e2) {
			    }
			} 
			/*
			 *  NOTE :  VARY ON is not allowed in a stored procedure call.  Try CMD CALL 
			 */
			/* 
			Statement s2 = c.createStatement();
			String sqlText = "CALL QSYS.QCMDEXC('VRYCFG CFGOBJ("+iasp_+") CFGTYPE(*DEV) STATUS(*ON)                                    ' , 0000000080.00000)";
			System.out.println("Running: "+sqlText);
			try { 
			  s2.executeUpdate(sqlText);
			} catch (Exception e) { 
			  System.out.println("Varyon failed. Sleeping for 300 seconds"); 
			  try {
			    Thread.sleep(300000); 
			  } catch (Exception e2) {
			    
			  }
			}
			*/ 
		    } 
		} 
		System.out.println(); 
	    } 
	    
	    if (!iaspAvailable) System.out.println("NO IASPS are available"); 
	    rs.close();

	    String sql = "DROP COLLECTION " + collection_;
	 try {
	     s.executeUpdate(sql);
	 } catch (Exception e) {
	     String message = e.toString();
	     if (message.indexOf("not found") >= 0) {
		 System.out.println("  Warning, setup error on "+sql);
		 System.out.println("  " + e.getMessage());
	     }
	 }

	    s.close();

	    c.close();


	} catch (Exception e) {
	    System.out.println("Error in setup ");
	    System.out.flush();
	    e.printStackTrace();
	    System.err.flush(); 
	} 

    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
    }



    //  Specify asp but get at file in the base asp.  Should work.  Should also
    //  work when running to a pre-v5r2 system (asp will be ignored).
    //  URL based naming.
    public void Var001()
    {             
       System.out.println();

             
       Connection c  = null;
       Statement  s  = null;
       ResultSet  rs = null;
       try
       {
          c  = DriverManager.getConnection(URLWithIASP_, userid_, password_1);
          s  = c.createStatement();
          rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
          if (rs.next())
             succeeded();
          else
             failed("no result set returned");
       }              
       catch (Exception e)   
       {
          failed(e, "unexpected exception URLWithIASP_ = "+URLWithIASP_);
       }                                      
       
       if (rs != null) try { rs.close(); } catch (Exception e) { e.printStackTrace(); }
       if (s  != null) try {  s.close(); } catch (Exception e) { e.printStackTrace();}
       if (c  != null) try {  c.close(); } catch (Exception e) { e.printStackTrace();}
    }



    //  Specify asp but get at file in the base asp.  Should work.  Should also
    //  work when running to a pre-v5r2 system (asp will be ignored).
    //  DataSource based naming.
    public void Var002()
    {                                                                                
      
       Connection c  = null;
       Statement  s  = null;
       ResultSet  rs = null;
       try
       {
          AS400JDBCDataSource ds = new AS400JDBCDataSource();
          ds.setUser(userid_);
          ds.setPassword(password_1);
          ds.setServerName(system_1.getSystemName());
          ds.setDatabaseName(iasp_);
          
          c  = ds.getConnection();
          s  = c.createStatement();
          rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
          if (rs.next())
             succeeded();
          else
             failed("no result set returned");
       }              
       catch (Exception e)   
       {
          failed(e, "unexpected exception");
       }                                      
       
       if (rs != null) try { rs.close(); } catch (Exception e) { e.printStackTrace();}
       if (s  != null) try {  s.close(); } catch (Exception e) { e.printStackTrace();}
       if (c  != null) try {  c.close(); } catch (Exception e) { e.printStackTrace();}
    }



    //  Specify *SYSBAS but get at file in the base asp.  Should work.  Should also
    //  work when running to a pre-v5r2 system (asp will be ignored).
    //  URL based naming.
    public void Var003()
    {             
             
       Connection c  = null;
       Statement  s  = null;
       ResultSet  rs = null;
       try
       {
          c  = DriverManager.getConnection(URLWithSYSBAS_, userid_, password_1);
          s  = c.createStatement();
          rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
          if (rs.next())
             succeeded();
          else
             failed("no result set returned");
       }              
       catch (Exception e)   
       {
          failed(e, "unexpected exception");
       }                                      
       
       if (rs != null) try { rs.close(); } catch (Exception e) { e.printStackTrace();}
       if (s  != null) try {  s.close(); } catch (Exception e) { e.printStackTrace();}
       if (c  != null) try {  c.close(); } catch (Exception e) { e.printStackTrace();}
    }



    //  Specify *SYSBAS but get at file in the base asp.  Should work.  Should also
    //  work when running to a pre-v5r2 system (asp will be ignored).
    //  DataSource based naming.
    public void Var004()
    {                                                                                
      
       Connection c  = null;
       Statement  s  = null;
       ResultSet  rs = null;
       try
       {
          AS400JDBCDataSource ds = new AS400JDBCDataSource();
          ds.setUser(userid_);
          ds.setPassword(password_1);
          ds.setServerName(system_1.getSystemName());
          ds.setDatabaseName("*SYSBAS");
          
          c  = ds.getConnection();
          s  = c.createStatement();
          rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
          if (rs.next())
             succeeded();
          else
             failed("no result set returned");
       }              
       catch (Exception e)   
       {
          failed(e, "unexpected exception");
       }                                      
       
       if (rs != null) try { rs.close(); } catch (Exception e) { e.printStackTrace();}
       if (s  != null) try {  s.close(); } catch (Exception e) { e.printStackTrace();}
       if (c  != null) try {  c.close(); } catch (Exception e) { e.printStackTrace();}
    }



    //  Setup testcase -- should create a file on an ASP
    public void Var005()
    {             
     
             
       Connection c  = null;
       Statement  s  = null;
       try
       {
          try
          {                  
             AS400 sys = new AS400(systemObject_.getSystemName(), puserid_, ppassword_);                                                                            
             CommandCall cmd = new CommandCall(sys, "ADDRPYLE SEQNBR(7025) MSGID(CPA7025) RPY(I)");
             cmd.run();       
             //AS400Message[] messageList = cmd.getMessageList();
             //for(int i = 0; i < messageList.length; ++i)
             //  System.out.println(messageList[i]);
          }
          catch (Exception e) { e.printStackTrace(); }

          c  = DriverManager.getConnection(URLWithIASP_, userid_, password_1);
          s  = c.createStatement();
                                                                                                          
          String sql = "DROP TABLE " + name_1;
          try { s.executeUpdate(sql); } catch (Exception e) { System.out.println("  Warning, setup error"); System.out.println("  " + e.getMessage()); System.out.println("  Don't worry if message is file not found, will create file next"); }

 
	  JDSetupCollection.create(c, collection_1, false); 
 
          sql = "CREATE TABLE " + name_1 + " (parm_int INTEGER, parm_string VARCHAR(20))";
          try { s.executeUpdate(sql); } catch (Exception e) { System.out.println("  Warning, setup error"); System.out.println("  " + e.getMessage()); setupWorked_ = false; }
                                                
          sql = "GRANT ALL PRIVILEGES ON TABLE " + name_1 + " TO PUBLIC";
          try { s.executeUpdate(sql); } catch (Exception e) { System.out.println("  Warning, setup error"); System.out.println("  " + e.getMessage()); setupWorked_ = false; }
                                                
          sql = "INSERT INTO " + name_1 + " VALUES (1, 'ONE')";
          try { s.executeUpdate(sql); } catch (Exception e) { System.out.println("  Warning, setup error"); System.out.println("  " + e.getMessage()); }

          sql = "INSERT INTO " + name_1 + " VALUES (2, 'TWO')";
          try { s.executeUpdate(sql); } catch (Exception e) { System.out.println("  Warning, setup error"); System.out.println("  " + e.getMessage()); }

          succeeded();
       }              
       catch (Exception e)   
       {
          failed(e, "unexpected exception  URLWithIASP_ = "+URLWithIASP_);
       }                                      
       
       if (s  != null) try {  s.close(); } catch (Exception e) { e.printStackTrace(); }
       if (c  != null) try {  c.close(); } catch (Exception e) { e.printStackTrace(); }

    }
                                                                              
                                                                              
                      
    // Read file stored on an ASP (put there in variation 5) -- URL
    public void Var006()
    {             
             
       Connection c  = null;
       Statement  s  = null;
       ResultSet  rs = null;
       try
       {
          c  = DriverManager.getConnection(URLWithIASP_, userid_, password_1);
          s  = c.createStatement();
          rs = s.executeQuery("SELECT * FROM " + name_1);
          if (rs.next())
             succeeded();
          else 
             failed("could not access data on ASP");
                                                                                                          
       }              
       catch (Exception e)   
       {
          failed(e, "unexpected exception  URLWithIASP_ = "+URLWithIASP_);
       }                                      
       
       if (rs != null) try { rs.close(); } catch (Exception e) { e.printStackTrace(); }
       if (s  != null) try {  s.close(); } catch (Exception e) { e.printStackTrace(); }
       if (c  != null) try {  c.close(); } catch (Exception e) { e.printStackTrace(); }

    }


                      
    // Read file stored on an ASP (put there in variation 5) -- data source
    public void Var007()
    {             
            
       Connection c  = null;
       Statement  s  = null;
       ResultSet  rs = null;
       try
       {
          AS400JDBCDataSource ds = new AS400JDBCDataSource();
          ds.setUser(userid_);
          ds.setPassword(password_1);
          ds.setServerName(system_1.getSystemName());
          ds.setDatabaseName(iasp_);
          
          c  = ds.getConnection();
          s  = c.createStatement();
          rs = s.executeQuery("SELECT * FROM " + name_1);
          if (rs.next())
             succeeded();
          else 
             failed("could not access data on ASP");
                                                                                                          
       }              
       catch (Exception e)   
       {
          failed(e, "unexpected exception");
       }                                      
       
       if (rs != null) try { rs.close(); } catch (Exception e) { e.printStackTrace(); }
       if (s  != null) try {  s.close(); } catch (Exception e) { e.printStackTrace(); }
       if (c  != null) try {  c.close(); } catch (Exception e) { e.printStackTrace(); }

    }


    // Try to use file on an ASP (put there in var 5) but do not specify the
    // asp on the connection.  Expect file not found. -- URL
    public void Var008()
    {             
       if (!iaspAvailable) {
         notApplicable("No IASPs are defined on this system.");       
         return; 
       }
      
       if (testDriver_.getRelease() < JDTestDriver.RELEASE_V5R2M0)
       {
          notApplicable("Requires OS/400 V5R2 or later.");       
          return;
       }
             
       Connection c  = null;
       Statement  s  = null;
       ResultSet  rs = null;
       try
       {
          c  = DriverManager.getConnection(baseURL_, userid_, password_1);
          s  = c.createStatement();
	  String sql = "SELECT * FROM " + name_1; 
          rs = s.executeQuery(sql);
          
          failed("Should not be able to execute '"+sql+"' when not connected to IASP baseURL_ was "+baseURL_);
       }              
       catch (SQLException sqee)
       {
          String text = sqee.getMessage().toUpperCase();
          if (text.indexOf("NOT FOUND") >= 0)
             succeeded();
          else
             failed(sqee, "wrong exception, expected file not found");
       }
       catch (Exception e)   
       {
          failed(e, "unexpected exception");
       }                                      
       
       if (rs != null) try { rs.close(); } catch (Exception e) { e.printStackTrace(); }
       if (s  != null) try {  s.close(); } catch (Exception e) { e.printStackTrace(); }
       if (c  != null) try {  c.close(); } catch (Exception e) { e.printStackTrace(); }

    }


                      
    // Try to use file on an ASP (put there in var 5) but do not specify the
    // asp on the connection.  Expect file not found. -- data source
    public void Var009()
    {             
      if (!iaspAvailable) {
        notApplicable("No IASPs are defined on this system.");       
        return; 
      }
      
       if (testDriver_.getRelease() < JDTestDriver.RELEASE_V5R2M0)
       {
          notApplicable("Requires OS/400 V5R2 or later.");       
          return;
       }
             
       Connection c  = null;
       Statement  s  = null;
       ResultSet  rs = null;
       try
       {
          AS400JDBCDataSource ds = new AS400JDBCDataSource();
          ds.setUser(userid_);
          ds.setPassword(password_1);
          ds.setServerName(system_1.getSystemName());
          
          c  = ds.getConnection();
          s  = c.createStatement();
          rs = s.executeQuery("SELECT * FROM " + name_1);
          failed("Should not find database.  Likely because asp " + iasp_ + " is not setup");
                                                                                                          
       }              
       catch (SQLException sqee)
       {
          String text = sqee.getMessage().toUpperCase();
          if (text.indexOf("NOT FOUND") >= 0)
             succeeded();
          else
             failed(sqee, "wrong exception, expected file not found");
       }
       catch (Exception e)   
       {
          failed(e, "unexpected exception");
       }                                      
       
       if (rs != null) try { rs.close(); } catch (Exception e) { e.printStackTrace(); }
       if (s  != null) try {  s.close(); } catch (Exception e) { e.printStackTrace(); }
       if (c  != null) try {  c.close(); } catch (Exception e) { e.printStackTrace(); }

    }


    // Try to use file on an ASP (put there in var 5) but specify sysbas as the 
    // asp for the connection.  Expect file not found. -- URL
    public void Var010()
    {             
      if (!iaspAvailable) {
        notApplicable("No IASPs are defined on this system.");       
        return; 
      }
      
       if (testDriver_.getRelease() < JDTestDriver.RELEASE_V5R2M0)
       {
          notApplicable("Requires OS/400 V5R2 or later.");       
          return;
       }
             
       Connection c  = null;
       Statement  s  = null;
       ResultSet  rs = null;
       try
       {
          c  = DriverManager.getConnection(URLWithSYSBAS_, userid_, password_1);
          s  = c.createStatement();
          rs = s.executeQuery("SELECT * FROM " + name_1);
          failed("Should not find database.  Likely because asp " + iasp_ + " is not setup");
       }              
       catch (SQLException sqee)
       {
          String text = sqee.getMessage().toUpperCase();
          if (text.indexOf("NOT FOUND") >= 0)
             succeeded();
          else
             failed(sqee, "wrong exception, expected file not found");
       }
       catch (Exception e)   
       {
          failed(e, "unexpected exception");
       }                                      
       
       if (rs != null) try { rs.close(); } catch (Exception e) { e.printStackTrace(); }
       if (s  != null) try {  s.close(); } catch (Exception e) { e.printStackTrace(); }
       if (c  != null) try {  c.close(); } catch (Exception e) { e.printStackTrace(); }

    }


                      
    // Try to use file on an ASP (put there in var 5) but specify sysbas as the
    // asp for the connection.  Expect file not found. -- data source
    public void Var011()
    {             
      if (!iaspAvailable) {
        notApplicable("No IASPs are defined on this system.");       
        return; 
      }
      
       if (testDriver_.getRelease() < JDTestDriver.RELEASE_V5R2M0)
       {
          notApplicable("Requires OS/400 V5R2 or later.");       
          return;
       }
             
       Connection c  = null;
       Statement  s  = null;
       ResultSet  rs = null;
       try
       {
          AS400JDBCDataSource ds = new AS400JDBCDataSource();
          ds.setUser(userid_);
          ds.setPassword(password_1);
          ds.setServerName(system_1.getSystemName());
          ds.setDatabaseName("*SYSBAS");
          
          c  = ds.getConnection();
          s  = c.createStatement();
          rs = s.executeQuery("SELECT * FROM " + name_1);
          failed("Should not find database.  Likely because asp " + iasp_ + " is not setup");
                                                                                                          
       }              
       catch (SQLException sqee)
       {
          String text = sqee.getMessage().toUpperCase();
          if (text.indexOf("NOT FOUND") >= 0)
             succeeded();
          else
             failed(sqee, "wrong exception, expected file not found");
       }
       catch (Exception e)   
       {
          failed(e, "unexpected exception");
       }                                      
       
       if (rs != null) try { rs.close(); } catch (Exception e) { e.printStackTrace(); }
       if (s  != null) try {  s.close(); } catch (Exception e) { e.printStackTrace(); }
       if (c  != null) try {  c.close(); } catch (Exception e) { e.printStackTrace(); }

    }


    // Connect to a bogus ASP - URL
    public void Var012()
    {             
      if (!iaspAvailable) {
        notApplicable("No IASPs are defined on this system.");       
        return; 
      }
      
       if (testDriver_.getRelease() < JDTestDriver.RELEASE_V5R2M0)
       {
          notApplicable("Requires OS/400 V5R2 or later.");       
          return;
       }
             
       Connection c  = null;
       Statement  s  = null;
       ResultSet  rs = null;
       String badURL; 
       try
       {
          badURL = baseURL_ + ";database name=JDBC_ASP_XYZ";

	  if (getDriver() == JDTestDriver.DRIVER_NATIVE)
	  {
	      String replaceString = testDriver_.getRdbName();
	      if (replaceString == null) {
		  replaceString = testDriver_.getSystemName();
	      }
	      badURL  = baseURL_.replace(replaceString, "JDBC_ASP_XYZ" );
	  }

          c  = DriverManager.getConnection(badURL, userid_, password_1);
          s  = c.createStatement();
          rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
          failed("Should not find database.  Likely because asp " + iasp_ + " is not setup. badUrl="+badURL);
       }              
       catch (SQLException sqee)
       {
          String text = sqee.getMessage().toUpperCase();
	  String expected = "NOT IN RELATIONAL";
	  String expected2= "TBD"; 
	  // In V6R1 we get a different error.  Since it is an error condition,
          // this testcase will tolerate the difference.
	  if (getRelease() == JDTestDriver.RELEASE_V6R1M0) {
	      expected2 = "NOT IN A CONNECTED STATE"; 
	  } 
	  
          if ((text.indexOf(expected) >= 0) ||(text.indexOf(expected2) >= 0) )
             succeeded();
          else
             failed(sqee, "wrong exception, expected '"+expected+"' or '"+expected2+"' -- database name=JDBC_ASP_XYZ");
       }
       catch (Exception e)   
       {
          failed(e, "unexpected exception");
       }                                      
       
       if (rs != null) try { rs.close(); } catch (Exception e) { e.printStackTrace(); }
       if (s  != null) try {  s.close(); } catch (Exception e) { e.printStackTrace(); }
       if (c  != null) try {  c.close(); } catch (Exception e) { e.printStackTrace(); }

    }

    // Connect to a bogus ASP - data source
    public void Var013()
    {             
      if (!iaspAvailable) {
        notApplicable("No IASPs are defined on this system.");       
        return; 
      }
      
       if (testDriver_.getRelease() < JDTestDriver.RELEASE_V5R2M0)
       {
          notApplicable("Requires OS/400 V5R2 or later.");       
          return;
       }
             
       Connection c  = null;
       Statement  s  = null;
       ResultSet  rs = null;
       try
       {
          AS400JDBCDataSource ds = new AS400JDBCDataSource();
          ds.setUser(userid_);
          ds.setPassword(password_1);
          ds.setServerName(system_1.getSystemName());
          ds.setDatabaseName("Nov_1_testcase");
          
          c  = ds.getConnection();
          s  = c.createStatement();
          rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
          failed("Should not find database.  Likely because asp " + iasp_ + " is not setup");
       }              
       catch (SQLException sqee)
       {
          String text = sqee.getMessage().toUpperCase();
	  String expected = "NOT IN RELATIONAL"; 
	  String expected2= "TBD"; 
	  // In V6R1 we get a different error.  Since it is an error condition,
          // this testcase will tolerate the difference.
	  if (getRelease() == JDTestDriver.RELEASE_V6R1M0) {
	      expected2 = "NOT IN A CONNECTED STATE"; 
	  } 
	  
          if ((text.indexOf(expected) >= 0) ||(text.indexOf(expected2) >= 0) )
             succeeded();
          else
             failed(sqee, "wrong exception, expected NOT IN RELATIONAL for databaseName=Nov_1_testcase");
       }
       catch (Exception e)   
       {
          failed(e, "unexpected exception");
       }                                      
       
       if (rs != null) try { rs.close(); } catch (Exception e) { e.printStackTrace(); }
       if (s  != null) try {  s.close(); } catch (Exception e) { e.printStackTrace(); }
       if (c  != null) try {  c.close(); } catch (Exception e) { e.printStackTrace(); }

    }


    // Use an ASP name that is too long - URL
    public void Var014()
    {             
      if (!iaspAvailable) {
        notApplicable("No IASPs are defined on this system.");       
        return; 
      }
      
       if (testDriver_.getRelease() < JDTestDriver.RELEASE_V5R2M0)
       {
          notApplicable("Requires OS/400 V5R2 or later.");       
          return;
       }
             
       Connection c  = null;
       Statement  s  = null;
       ResultSet  rs = null;
       try
       {
          String badURL = baseURL_ + ";database name=Thisssss_ASPPPPPPP_Nameeeeee_Issss_tooooo_Longggggg";
	  if (getDriver() == JDTestDriver.DRIVER_NATIVE)
	  {
	      String replaceString = testDriver_.getRdbName();
	      if (replaceString == null) {
		  replaceString = testDriver_.getSystemName();
	      }
	      badURL  = baseURL_.replace(replaceString, "JDBC_ASP_XYZ" );
	  }

          c  = DriverManager.getConnection(badURL, userid_, password_1);
          s  = c.createStatement();
          rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
          failed("Should not find database.  Likely because asp " + iasp_ + " is not setup badURL="+badURL);
       }              
       catch (SQLException sqee)
       {
          String text = sqee.getMessage().toUpperCase();
	  String expected = "NOT IN RELATIONAL"; 
	  String expected2= "TBD"; 
	  // In V6R1 we get a different error.  Since it is an error condition,
          // this testcase will tolerate the difference.
	  if (getRelease() == JDTestDriver.RELEASE_V6R1M0) {
	      expected2 = "NOT IN A CONNECTED STATE"; 
	  } 
	  
          if ((text.indexOf(expected) >= 0) ||(text.indexOf(expected2) >= 0) )


             succeeded();
          else
             failed(sqee, "wrong exception, expected NOT IN RELATIONAL");
       }
       catch (Exception e)   
       {
          failed(e, "unexpected exception");
       }                                      
       
       if (rs != null) try { rs.close(); } catch (Exception e) { e.printStackTrace(); }
       if (s  != null) try {  s.close(); } catch (Exception e) { e.printStackTrace(); }
       if (c  != null) try {  c.close(); } catch (Exception e) { e.printStackTrace(); }

    }



    // Use an ASP name that is too long - data source
    public void Var015()
    {             
      if (!iaspAvailable) {
        notApplicable("No IASPs are defined on this system.");       
        return; 
      }
      
       if (testDriver_.getRelease() < JDTestDriver.RELEASE_V5R2M0)
       {
          notApplicable("Requires OS/400 V5R2 or later.");       
          return;
       }
             
       Connection c  = null;
       Statement  s  = null;
       ResultSet  rs = null;
       try
       {
          AS400JDBCDataSource ds = new AS400JDBCDataSource();
          ds.setUser(userid_);
          ds.setPassword(password_1);
          ds.setServerName(system_1.getSystemName());
          ds.setDatabaseName("ASP01234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
          
          c  = ds.getConnection();
          s  = c.createStatement();
          rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
          failed("Should not find database.  Likely because asp " + iasp_ + " is not setup");
       }              
       catch (SQLException sqee)
       {
          String text = sqee.getMessage().toUpperCase();
	  String expected = "NOT IN RELATIONAL"; 
	  String expected2= "TBD"; 
	  // In V6R1 we get a different error.  Since it is an error condition,
          // this testcase will tolerate the difference.
	  if (getRelease() == JDTestDriver.RELEASE_V6R1M0) {
	      expected2 = "NOT IN A CONNECTED STATE"; 
	  } 
	  
          if ((text.indexOf(expected) >= 0) ||(text.indexOf(expected2) >= 0) )

             succeeded();
          else
             failed(sqee, "wrong exception, expected NOT IN RELATIONAL");
       }
       catch (Exception e)   
       {
          failed(e, "unexpected exception");
       }                                      
       
       if (rs != null) try { rs.close(); } catch (Exception e) { e.printStackTrace(); }
       if (s  != null) try {  s.close(); } catch (Exception e) { e.printStackTrace(); }
       if (c  != null) try {  c.close(); } catch (Exception e) { e.printStackTrace(); }

    }


                      
    // Get ASP names back on a list request
    public void Var016()
    {                                                                                
      if (!iaspAvailable) {
        notApplicable("No IASPs are defined on this system.");       
        return; 
      }

       if (testDriver_.getRelease() < JDTestDriver.RELEASE_V5R2M0)
       {
          notApplicable("Requires OS/400 V5R2 or later.");       
          return;
       }
      
       Connection         c  = null;
       ResultSet          rs = null;      
       DatabaseMetaData dbmd = null;
       try
       {
          AS400JDBCDataSource ds = new AS400JDBCDataSource();
          ds.setUser(userid_);
          ds.setPassword(password_1);
          ds.setServerName(system_1.getSystemName());
          ds.setDatabaseName(iasp_);
          
          c    = ds.getConnection();    
          dbmd = c.getMetaData();
          rs   = dbmd.getCatalogs();
          
          boolean ordinalSuccess = false;
          boolean nameSuccess = false;
          
          while (rs.next())
          {
             if (rs.getString(1).equalsIgnoreCase(iasp_) || rs.getString(1).equalsIgnoreCase(system_1.getSystemName()))
                ordinalSuccess = true;

             if (rs.getString("TABLE_CAT").equalsIgnoreCase(iasp_) || rs.getString("TABLE_CAT").equalsIgnoreCase(system_1.getSystemName()))
                nameSuccess = true;
          }         
          
          if (ordinalSuccess && nameSuccess)
             succeeded();
          else
          {
             failed(iasp_ + " did not come back in the list.  The list is:");
             rs.close();
             rs = dbmd.getCatalogs();
             while (rs.next())
                System.out.println(rs.getString(1) + "<--");
          }
       }              
       catch (Exception e)   
       {
          failed(e, "unexpected exception");
       }                                      
       
       if (rs != null) try { rs.close(); } catch (Exception e) { e.printStackTrace(); }
       if (c  != null) try {  c.close(); } catch (Exception e) { e.printStackTrace(); }
    }





}



