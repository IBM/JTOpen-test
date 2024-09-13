///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDLobRemoteClob.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.JD.Lob;

import com.ibm.as400.access.AS400;

import test.JDJSTPOutputThread;
import test.JDJSTPTestcase;
import test.JDJobName;
import test.JDLobTest;
import test.JDTestDriver;
import test.JDTestcase;
import test.PasswordVault;
import test.JD.JDSetupCollection;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;


/**
Testcase JDLobRemoteClob.  This tests the use of a remote clob.
This is used only for the native driver.  

**/
public class JDLobRemoteClob
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDLobRemoteClob";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDLobTest.main(newArgs); 
   }


    // Private data.
    private Connection          connection_;
    private Statement           statement_;


    public static String TABLE_  = JDLobTest.COLLECTION + ".REMOTECLOB";
    private String added = " -- added 3/14/2006 by native driver";
    private String password5035 = "";
    private String userid5035="jjapan";
    String otherSystem="";
    String collection=""; 
/**
Constructor.
**/
    public JDLobRemoteClob    (AS400 systemObject,
                             Hashtable namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             String password)
    {
        super (systemObject, "JDLobRemoteClob",
               namesAndVars, runMode, fileOutputStream,
               password);
    }


    void executeCommand(String command) throws Exception { 
	System.out.flush();
	System.out.println("Attempting:"+command); 
	Process p = JDJSTPTestcase.exec(command);
	JDJSTPTestcase.showProcessOutput(p,"stdout", JDJSTPOutputThread.ENCODING_UNKNOWN); 
	System.out.flush();
	p.waitFor();
    }




    protected void executeSetupCommand(String command) {
	try {
	    System.out.println("SetupCommand:  "+command); 
	    String sql = "CALL QSYS.QCMDEXC('"+command+
	      "                                                                          ',"+
	      " 0000000080.00000)"; 
	    statement_.executeUpdate(sql);

	} catch (Exception e) {
	    e.printStackTrace(System.out);
	}

    } 
/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
    throws Exception
    {

       System.out.println("setup");

       collection = JDLobTest.COLLECTION;
       if (collection.length() < 10) {
	   collection = collection+"O";
       }
       TABLE_  = collection + ".REMOTECLOB";

       String letter =   JDLobTest.COLLECTION.substring(JDLobTest.COLLECTION.length() - 1);  

       userid5035="JJAPAN"+letter;

       if (isJdbc20 ()) {
       System.out.println("jdbc20"); 
            if (areLobsSupported ()) {
		System.out.println("lobs supported"); 
		if (getDriver () == JDTestDriver.DRIVER_NATIVE) {

		    System.out.println("nativeDriver"); 
		    password5035="PASS"+ ( System.currentTimeMillis() % 10000);


		    try {
			System.out.println("Current user is "+System.getProperty("user.name")); 
			System.out.println("Attempting setIGC(5035)");
			System.out.flush(); 
			JDJobName.setIGC(5035);

			String command = "system CRTUSRPRF "+userid5035+" 'CCSID(5035)' 'PASSWORD(GARBAGE)'"; 
			executeCommand(command);

			command = "system CHGUSRPRF "+userid5035+" 'CCSID(5035)' 'STATUS(*ENABLED)' 'PASSWORD("+password5035+")'";
			executeCommand(command);

			command = "system CHGUSRPRF 'USRPRF("+userid5035+")' 'CCSID(5035)'";
			executeCommand(command);

			command = "system CHGUSRPRF 'USRPRF("+userid5035+")'  'STATUS(*ENABLED)'";
			executeCommand(command);

			command = "system CHGUSRPRF 'USRPRF("+userid5035+")' 'PASSWORD("+password5035+")'";
			executeCommand(command); 

			command = "system GRTOBJAUT 'OBJ("+userid5035+")' 'OBJTYPE(*USRPRF)' 'USER("+userid5035+")' 'AUT(*ALL)' "; 
			executeCommand(command); 

			command = "system GRTOBJAUT 'OBJ("+collection+") OBJTYPE(*LIB) USER("+userid5035+") AUT(*ALL)' "; 
			executeCommand(command);

			command = "system GRTOBJAUT 'OBJ("+userid5035+")' 'OBJTYPE(*USRPRF)' 'USER("+System.getProperty("user.name")+")' 'AUT(*ALL)' "; 
			executeCommand(command); 


		    } catch (Exception e) {
			e.printStackTrace(); 
		    } 


		    TABLE_  = collection + ".REMOTECLOB";

		    otherSystem = System.getProperty("jta.secondary.system");
		    if (otherSystem == null ||
			otherSystem.equalsIgnoreCase(system_)) {
			failed("This variation requires the System property 'jta.secondary.system' " +
			       "be set to a system (DB) other than the one named for the main test");
			return;
		    }




		    //
		    // Get the connection to the local system
		    //
		    Connection localConnection; 

		    String localUrl = "jdbc:db2:*LOCAL"
		      + ";lob threshold=1";
		    localConnection = testDriver_.getConnection(localUrl,systemObject_.getUserId(), encryptedPassword_);

		    localConnection.setAutoCommit(false);
		    localConnection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
		    statement_ = localConnection.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
							      ResultSet.CONCUR_UPDATABLE);







		    localConnection.close(); 

	//
	// Get the connection to the remote system.
	//

		    String url = "jdbc:db2:"+otherSystem
		      + ";user=" + systemObject_.getUserId() 
		      + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
		      + ";lob threshold=1";
		    connection_ = testDriver_.getConnection(url,systemObject_.getUserId(),encryptedPassword_);
		    connection_.setAutoCommit(false);
		    connection_.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
		    statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
							      ResultSet.CONCUR_UPDATABLE);


		    String command = "CRTUSRPRF "+userid5035+" CCSID(5035) PASSWORD(GARBAGE)"; 
		    executeSetupCommand(command);
		    command = "CHGUSRPRF "+userid5035+" CCSID(5035)  STATUS(*ENABLED) PASSWORD("+password5035+")";
		    executeSetupCommand(command);
		    command = "GRTOBJAUT OBJ("+collection+") OBJTYPE(*LIB) USER("+userid5035+") AUT(*ALL)"; 
		    executeSetupCommand(command);
		    command = "GRTOBJAUT OBJ("+userid5035+") OBJTYPE(*USRPRF) USER("+userid5035+") AUT(*ALL) ";
		    executeSetupCommand(command); 
		    command = "system GRTOBJAUT OBJ("+collection+") OBJTYPE(*LIB) USER("+userid5035+") AUT(*ALL) "; 
		    executeSetupCommand(command); 

		    command = "system GRTOBJAUT OBJ("+userid5035+") OBJTYPE(*USRPRF) USER("+System.getProperty("user.name")+") AUT(*ALL) "; 
		      executeSetupCommand(command); 




		    JDJobName.setJobLogOption();



		}
	    }
	}
    }

/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
        if (isJdbc20 ()) {
            if (areLobsSupported ()) {
		if (statement_ != null) { 
		    statement_.executeUpdate ("DROP TABLE " + TABLE_);

		    statement_.close ();
		}
		if (connection_ != null) { 
		    try {
			connection_.commit(); 
		    } catch (Exception e) {
			e.printStackTrace(); 
		    } 
		    connection_.close ();
		}
            }
        }
    }



/**
get a remote clob with 5035 data 
**/
    public void Var001()
    {
	String sql=""; 
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
		if (checkNative()) { 

		    try {
			JDJobName.swapTo(userid5035, password5035); 
			JDJobName.setIGC(5035);


			String url = "jdbc:db2:"+otherSystem
			  + ";user=" + userid5035
			  + ";password=" + password5035 
			  + ";transaction isolation=read uncommitted"; 
			Connection connection = DriverManager.getConnection (url);
			connection.setAutoCommit(false);
			connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

			Statement stmt = connection.createStatement();
			try {

			    JDSetupCollection.create(connection, collection, false);
			    connection.commit(); 
			} catch (Exception e) {
			    // assume it exists 
			} 

			try {
			    stmt.executeUpdate("Drop table "+TABLE_); 
			} catch (Exception e) {
			} 
			sql = "CREATE TABLE "+TABLE_+" (C1 CLOB(102400) CCSID 5035)";
			stmt.executeUpdate(sql);
			sql="INSERT INTO  "+TABLE_+" VALUES(UX'3042304430463048304a')";
			stmt.executeUpdate(sql);
			connection.commit();
			sql = "Select * from "+TABLE_;
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			String answer = rs.getString(1);
			
			connection.close();

			assertCondition("\u3042\u3044\u3046\u3048\u304a".equals(answer), "Wrong Answer expected UX'3042304430463048304a' got "+formatString(answer)+" job CCSID is "+JDJobName.getJobCCSID()); 
		    }
		    catch (Exception e) {
			failed (e, "Unexpected Exception "+added+" job CCSID is "+JDJobName.getJobCCSID()+ " sql="+sql); 
		    }
		    try {
			JDJobName.swapBack(); 
		    } catch (Exception e) {
			e.printStackTrace(); 
		    } 
		}
            }
        }
    }


    public static String formatString(String s) {
	char chars[]         = s.toCharArray(); 
	boolean isAsciiMode=true;
	StringBuffer buf = new StringBuffer(); 
	for(int i = 0; i < chars.length; i++){
	    if ((chars[i] <= 0x7E) && (chars[i] >= 0x20)){
		if (!isAsciiMode) { 
		    buf.append(">>UX>>");
		    isAsciiMode = true;
		}
	    } else {
		if (isAsciiMode) { 
		    buf.append("<<UX<<");	
		    isAsciiMode = false;
		}
	    }
	    if(isAsciiMode){
		buf.append(chars[i]);
	    } else{
		int showInt = chars[i] & 0xFFFF;
		String showString = Integer.toHexString(showInt); 
		buf.append(" ");
		buf.append(showString); 
	    }  
	}
	if (!isAsciiMode) {
	    buf.append(">>UX>>");
	} 
	return buf.toString(); 
    } 


}


