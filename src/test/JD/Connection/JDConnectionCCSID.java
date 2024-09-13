///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionCCSID.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDConnectionCCSID.java
//
// Classes:      JDConnectionCCSID
//
////////////////////////////////////////////////////////////////////////

package test.JD.Connection;

import com.ibm.as400.access.AS400;

import test.JDConnectionTest;
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;
import test.PasswordVault;

import java.awt.TextArea;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;
import java.io.*; 

/**
Testcase JDConnectionCCSID.
Test that the CCSID of the backend job is the expected CCSID.
**/
public class JDConnectionCCSID
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDConnectionCCSID";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDConnectionTest.main(newArgs); 
   }



    // Private data.
    private              Connection     connection_;
    String pwrUID_; 
    String pwrPwd_;
    char[] encryptedPwrPwd_; 
    
    Hashtable userProfiles = new Hashtable();  

/**
Constructor.
**/
    public JDConnectionCCSID (AS400 systemObject,
                             Hashtable namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             
                             String password,
                             String pwrUID,     //@H2A
                             String pwrPwd) {   //@H2A
        super (systemObject, "JDConnectionCCSID",
               namesAndVars, runMode, fileOutputStream,
               password);
        pwrUID_ = pwrUID;   //@H2A
        pwrPwd_ = pwrPwd;   //@H2A
        encryptedPassword_ = PasswordVault.getEncryptedPassword(pwrPwd); 

    }



/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
    throws Exception
    {
	{
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		    String currentUser = System.getProperty("user.name"); 	
		try {
	    // Grant current user access to the QP0ZMAINT program

		    Connection c = testDriver_.getConnection (baseURL_, pwrUID_, encryptedPwrPwd_);
		    Statement s = c.createStatement();
		    s.execute("call QSYS.QCMDEXC('GRTOBJAUT OBJ(QSYS/QP0ZMAINT) OBJTYPE(*PGM) USER(" +currentUser+") AUT(*USE)                                                                  ',000000080.00000)");
		    c.close();
		} catch (Exception e) {
		    System.out.println("Warning:  unable to grant currentUser access to QP0ZMAINT"); 
		} 
	    }
	}

	connection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);


	//
	// Setup the UDF to get the CCSID for the server job
	//

	String cProgram[] = {
	    " ",
	      "",
	    "  /*",
	    "  * UDF to get the current job CCSID",
	    "",
	    "",
	    "    compile using the following ",
	    "   CRTCMOD MODULE(SRVJBCCSID) DBGVIEW(*ALL)   ",
	    "  CRTSRVPGM SRVJBCCSID export(*all)",
	    "",
	    "   CREATE FUNCTION SRVJBCCSID () RETURNS INT ",
	    "   LANGUAGE C EXTERNAL NAME ",
	    "   'EBERHARD/SRVJBCCSID(SRVJBCCSID)', PARAMETER STYLE SQL",
	    "",
	    "   To test",
	    "",
	    "    select SRVJBCCSID() from qsys2.qsqptabl",
	    "","",
	    "",
	    "",
	    "  */",
	    "",
	    "#include <stdlib.h>",
	    "#include <stdio.h>",
	    "#include <string.h>",
	    "#include <qusrjobi.h>",
	    "",
	    "void  SRVJBCCSID (int * output,",
	    "                  int * ind0,",
	    "                  int * ind1,",
	    "                  char * sqlstate,",
	    "                  char * functionName,",
	    "                  char * specificName,",
	    "                  char * messageText",
	    "                 ) {",
	    "    Qwc_JOBI0400_t jobi; ",
	    "    QUSRJOBI(&jobi,        ",
	    "             sizeof(jobi),",
	    "             \"JOBI0400\",",
	    "             \"*                         \",",
	    "             \"                \");",
	    "    *output = jobi.Coded_Char_Set_ID; ",

	    "",
	    "                    }",
	    "",
	};


	try {

		    //
		    // Make sure the procedure exists to call CL commands.
		    // 
	    Statement s1 = connection_.createStatement(); 
	    try {
		s1.executeUpdate("create procedure "+
				 "QGPL.JDCMDEXEC(IN CMDSTR VARCHAR(1024),IN CMDLEN DECIMAL(15,5)) "+
				 "External name QSYS.QCMDEXC LANGUAGE C GENERAL"); 
	    } catch (Exception e) {
				  // Just ignore error 
	    }
	    s1.close(); 

	    stringArrayToSourceFile(connection_,cProgram, JDConnectionTest.COLLECTION, "SRVJBCCSID");

	    CallableStatement cmd = connection_.prepareCall("call QGPL.JDCMDEXEC(?,?)");
	    String command = "CRTCMOD MODULE("+JDConnectionTest.COLLECTION+"/SRVJBCCSID) "+
	      " SRCFILE("+JDConnectionTest.COLLECTION+"/SRVJBCCSID)   ";

	    cmd.setString(1, command );
	    cmd.setInt(2, command.length());
	    try { 
		cmd.execute();
	    } catch (Exception e) {
		System.out.println("Error calling "+command); 
		e.printStackTrace(); 
	    }

	    command = "CRTSRVPGM SRVPGM("+JDConnectionTest.COLLECTION+"/SRVJBCCSID) MODULE("+JDConnectionTest.COLLECTION+"/SRVJBCCSID) EXPORT(*ALL)  "; 
	    cmd.setString(1, command );
	    cmd.setInt(2, command.length());
	    try {
		cmd.execute();
	    } catch (Exception e) {
		System.out.println("Error calling "+command); 
		e.printStackTrace(); 
	    }

	    String sql = " CREATE FUNCTION "+JDConnectionTest.COLLECTION +".SRVJBCCSID () RETURNS INT "+
	      "LANGUAGE C EXTERNAL NAME " +
	      "'"+JDConnectionTest.COLLECTION+"/SRVJBCCSID(SRVJBCCSID)' PARAMETER STYLE SQL"; 

	    Statement stmt = connection_.createStatement(); 
	    try {
		stmt.executeUpdate("drop FUNCTION "+JDConnectionTest.COLLECTION +".SRVJBCCSID"); 
	    } catch (Exception e) {
	    } 
	    try {
		stmt.executeUpdate(sql); 
	    } catch (Exception e) {
		System.out.println("Error running "+sql); 
		e.printStackTrace(); 
	    }
	    stmt.close(); 
	} catch (Exception e) {
	    e.printStackTrace(); 
	}

    } /* setup */ 




/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
       cleanupProfiles(); 
        connection_.close ();
    }

/**
 * Get a valid profile with the specified CCSID
 * This creates a profile if it doesn't exist
 * @throws SQLException 
 */
    public String  setupProfile(int ccsid) throws SQLException {
      String profile = "JDCON"+ccsid; 
      if (userProfiles.get(profile) == null) {
          // The profile has not been created.   Create it
          Statement s = connection_.createStatement(); 
          try { 
            s.executeUpdate("CALL QSYS.QCMDEXC('" + "CRTUSRPRF USRPRF(" + profile
                + ") PASSWORD(DUMMY)                                                            ',"
                + "0000000070.00000 ) ");
          } catch (Exception e) {
            
          }
          s.executeUpdate("CALL QSYS.QCMDEXC('" + "CHGUSRPRF USRPRF(" + profile
              + ") PASSWORD("+PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDConnectionCCSID.1")+") CCSID("+ccsid+")"
              + "                                                                      ',"
              + "0000000080.00000 ) ");
          userProfiles.put(profile,  profile); 
        
          s.executeUpdate("GRANT EXECUTE ON FUNCTION "+JDConnectionTest.COLLECTION +".SRVJBCCSID TO "+profile);
          
          s.close(); 
      }
      return profile; 
    }
    
    
    public void cleanupProfiles() {
      String sql="";
      try {
        Enumeration keys = userProfiles.keys(); 
        Statement s = connection_.createStatement(); 
        while (keys.hasMoreElements()) {
          String key = (String) keys.nextElement(); 
          sql = "CALL QSYS.QCMDEXC('" + "DLTUSRPRF USRPRF(" + key
              + ") OWNOBJOPT(*CHGOWN QUSER)                                                         ',"
              + "0000000070.00000 ) ";
          s.executeUpdate(sql);
        }
        s.close(); 
      } catch (Exception e) {
        System.out.println("Exception in cleanupProfiles:  sql="+sql); 
        e.printStackTrace(); 
      }
    }
/**
 Get the CCSID in the WCB of the current connection
 * @throws Exception 
**/

    public int getConnectionWcbCCSID(Connection inputConnection) throws Exception  {

	String jobname = (String) JDReflectionUtil.callMethod_S(inputConnection, "getServerJobName");
	int slashIndex = jobname.indexOf('/');
	String jobNumber = jobname.substring(0,slashIndex); 

	Runtime rt = Runtime.getRuntime();

	String[] cmd = new String[3];
	cmd[0] = "/usr/bin/qsh";
	cmd[1] = "-c";
	cmd[2] = "/QSYS.LIB/QP0ZMAINT.PGM '30' "+jobNumber+" '0' '1' | grep ':001250' | sed 's/.*:...... ........ ........ \\(....\\).*/\\1/'";

	int ccsid = 0; 
	// System.out.println("Debug:  Running "+cmd[2]);
	try {
	    Process p = rt.exec(cmd);
	    InputStream iStream = p.getInputStream();
	    int readByte = iStream.read();
	    while (readByte != -1) {
		int value; 
		if ( ((readByte >= 0xf0) && (readByte <=0xf9)) ||
		     ((readByte >= 0x30) && (readByte <=0x39))) {
		    value = readByte & 0xf;
		    ccsid=ccsid*16 +value; 
		} else if ( ((readByte >= 0x41) && (readByte <=0x46) ||
			     ((readByte >= 0xc1) && (readByte <=0xc6)))) {
		    value = 9 + readByte & 0xf;
		    ccsid=ccsid*16 +value; 
		}

		readByte=iStream.read();
	    }
	    iStream.close();

	    return ccsid; 
	} catch (Throwable e) {
	    e.printStackTrace();

	    return ccsid = 0; 
	}



    } 
    
    public void changeCcsidAndGetFromQusrjobi(int ccsid) {
      try {

        Statement s = connection_.createStatement();

        CallableStatement cmd = connection_
            .prepareCall("call QGPL.JDCMDEXEC(?,?)");
        String command = "CHGJOB CCSID(" + ccsid + ")";

        cmd.setString(1, command);
        cmd.setInt(2, command.length());
        cmd.execute();

        ResultSet rs = s.executeQuery("SELECT " + JDConnectionTest.COLLECTION
            + ".SRVJBCCSID() from sysibm.sysdummy1");
        rs.next();
        int outCcsid = rs.getInt(1);

        assertCondition(outCcsid == ccsid, "changeCcsidAndGetFromQusrjobi(): input ccsid=" + ccsid
            + " output ccsid=" + outCcsid);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

    public void changeCcsidAndGetFromWcb(int ccsid) { 
      try {
        if (checkNative()) {

          CallableStatement cmd = connection_
              .prepareCall("call QGPL.JDCMDEXEC(?,?)");
          String command = "CHGJOB CCSID(" + ccsid + ")";

          cmd.setString(1, command);
          cmd.setInt(2, command.length());
          cmd.execute();

          // Running a statement changes the CCSID back in V7R1 and earlier
          Statement s = connection_.createStatement(); 
          
          ResultSet rs = s.executeQuery("select * from sysibm.sysdummy1"); 
          while(rs.next()) {
            
          }
          s.close(); 
          
          int outCcsid = getConnectionWcbCCSID(connection_);

          assertCondition(outCcsid == ccsid, "changeCcsidAndGetFromWcb(): input ccsid=" + ccsid
              + " output ccsid=" + outCcsid);
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
      
    }
    
    
    public void changeCcsidAndGetFromColumn(int ccsid) { 
      try {

          CallableStatement cmd = connection_
              .prepareCall("call QGPL.JDCMDEXEC(?,?)");
          String command = "CHGJOB CCSID(" + ccsid + ")";

          cmd.setString(1, command);
          cmd.setInt(2, command.length());
          cmd.execute();

          // Running a statement changes the CCSID back in V7R1 and earlier
          Statement s = connection_.createStatement(); 
          
          String tablename = JDConnectionTest.COLLECTION + ".JDCCCSID03"; 
          try {
            s.executeUpdate("DROP TABLE "+tablename);
            
          } catch (Exception e) {} 
         
          s.executeUpdate("CREATE TABLE "+tablename+" (C1 VARCHAR(80))");
          
          ResultSet rs = s.executeQuery("select CCSID from qsys2.syscolumns "+
              " where COLUMN_NAME='C1' AND TABLE_NAME='JDCCCSID03' "+
              " AND TABLE_SCHEMA='"+JDConnectionTest.COLLECTION+"' "); 
              
          rs.next(); 
          int outCcsid = rs.getInt(1); 
          
          s.executeUpdate("DROP TABLE "+tablename);
          s.close(); 
          
          int expectedCcsid = ccsid;   
          if (expectedCcsid == 65535) {
            expectedCcsid=37; 
          }
          
          assertCondition(outCcsid == expectedCcsid, "changeCcsidAndGetFromColumn(): input ccsid=" + ccsid
              + " output ccsid=" + outCcsid+" expected ccsid="+expectedCcsid);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
      
    }
    
    
    public void loginCcsidAndGetFromQusrjobi(int ccsid) {
      try {

        String profile = setupProfile(ccsid); 
        Connection profileConnection = testDriver_.getConnection (baseURL_, profile, encryptedPassword_);
        Statement s = profileConnection.createStatement();


        ResultSet rs = s.executeQuery("SELECT " + JDConnectionTest.COLLECTION
            + ".SRVJBCCSID() from sysibm.sysdummy1");
        rs.next();
        int outCcsid = rs.getInt(1);
        profileConnection.close(); 
        
        int expectedCcsid = ccsid;   
        if (expectedCcsid == 65535 && getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          expectedCcsid=37; 
        }
        
        assertCondition(outCcsid == expectedCcsid, "loginCcsidAndGetFromQusrjobi(): input ccsid=" + ccsid
            + " output ccsid=" + outCcsid+" expected ccsid="+expectedCcsid);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

    public void loginCcsidAndGetFromWcb(int ccsid) { 
      try {
	  
        if (checkNative()) {

	    if (getRelease() < JDTestDriver.RELEASE_V7R2M0) {
		notApplicable("Server WCB CCSID test fixed in V7R2");
		return; 
	    }

          String profile = setupProfile(ccsid); 
          Connection profileConnection = testDriver_.getConnection (baseURL_, profile, encryptedPassword_);


          // Running a statement changes the CCSID back in V7R1 and earlier
          Statement s = profileConnection.createStatement(); 
          
          ResultSet rs = s.executeQuery("select * from sysibm.sysdummy1"); 
          while(rs.next()) {
            
          }
          s.close(); 
          
          int outCcsid = getConnectionWcbCCSID(profileConnection);
	  String extraInfo=""; 

            int expectedCcsid = ccsid;   

	    boolean passed = (outCcsid == expectedCcsid);

	    if (interactive_ && (!passed)) {
		BufferedReader is = new BufferedReader (new InputStreamReader(System.in));
		System.out.println("Pausing for failure with test.  Id is "+profile); 
		is.readLine(); 
	    } 


	    profileConnection.close();

            assertCondition(passed, "loginCcsidAndGetFromWcb() input ccsid=" + ccsid
                + " output ccsid=" + outCcsid+" expected ccsid="+expectedCcsid+extraInfo);

        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
      
    }

    
    public void loginCcsidAndGetFromColumn(int ccsid) { 
      try {

          String profile = setupProfile(ccsid); 
          Connection profileConnection = testDriver_.getConnection (baseURL_, profile, encryptedPassword_);

          // Running a statement changes the CCSID back in V7R1 and earlier
          Statement s = profileConnection.createStatement(); 
          
          String tablename = JDConnectionTest.COLLECTION + ".JDCCCSID03"; 
          try {
            s.executeUpdate("DROP TABLE "+tablename);
            
          } catch (Exception e) {} 
         
          s.executeUpdate("CREATE TABLE "+tablename+" (C1 VARCHAR(80))");
          
          ResultSet rs = s.executeQuery("select CCSID from qsys2.syscolumns "+
              " where COLUMN_NAME='C1' AND TABLE_NAME='JDCCCSID03' "+
              " AND TABLE_SCHEMA='"+JDConnectionTest.COLLECTION+"' "); 
              
          rs.next(); 
          int outCcsid = rs.getInt(1); 
          
          s.executeUpdate("DROP TABLE "+tablename);
          s.close(); 
          
          
          profileConnection.close(); 
          
          int expectedCcsid = ccsid;   
          if (expectedCcsid == 65535) {
            expectedCcsid=37; 
          }
          
          assertCondition(outCcsid == expectedCcsid, "loginCcsidAndGetFromColumn: input ccsid=" + ccsid
              + " output ccsid=" + outCcsid+" expected ccsid="+expectedCcsid);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
      
    }
    

    

/**
Set the CCSID and get the returned value.
**/
  public void Var001() {   changeCcsidAndGetFromQusrjobi(500);   }


/**
Set the CCSID and get the returned value from the WCB 
**/
  public void Var002() {
       changeCcsidAndGetFromWcb(500);
  }


  /**
  Set the CCSID and check the CCSID of a created table 
  **/
    public void Var003() { changeCcsidAndGetFromColumn(500); }


  /**
   * Connect using profile with different CCSID and check QUSRJOBI
   */
 public void Var004() {   loginCcsidAndGetFromQusrjobi(500); }
 
    /**
     * Connect using profile with different CCSID and check WCB
     */
 public void Var005() {

      loginCcsidAndGetFromWcb(500);
 }
  
    /**
     * Connect using profile with different CCSID and check CCSID of created table
     */
 public void Var006() {
     loginCcsidAndGetFromColumn(500);
 }
    
 

 
 
 public void Var007() { changeCcsidAndGetFromQusrjobi(65535);   }
 public void Var008() {

 changeCcsidAndGetFromWcb(65535);
}
 public void Var009() { changeCcsidAndGetFromColumn(65535); }
 public void Var010() { loginCcsidAndGetFromQusrjobi(65535); }
 public void Var011() { loginCcsidAndGetFromWcb(65535); }
 public void Var012() { loginCcsidAndGetFromColumn(65535); }

 
 
  
}



