 ///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDJSTPTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringBufferInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector; 

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.jdbcClient.Main;



/**
The JDJSTPTestcase class is a superclass for all STPJDBC testcase.
These testcase test java stored procedures and Java user defined functions on the server.
To do this, they must be able to load code on the server. This superclass contains static
functions that permit this to be done. 
**/
@SuppressWarnings("deprecation")
public class JDJSTPTestcase
extends JDTestcase
{


    public static final int IGNORE_ERROR = 1;
    public static final int THROW_ERROR = 2; 
    public static final int VISIBLE_ERROR = 3;
    public static final int IGNORE_REMOTE_ERROR = 4; 


    protected   boolean             nullBug;
    public static boolean           javaExecDebug = false; 
    public static  String           connectSTP;
    public static  String           disconnectSTP;
    public static  String           envLibrary="JDJSTPENV"; 
    public static  String           cmdChecked="NO";

    /* Homogenious N-1 tests */ 
    private static  String           GCLIConnect_ = null; 
    private static  String           HCLIConnect_ = null; 
    private static  String           ZCLIConnect_ = null; 
    private static  String           LCLIConnect_ = null; 

    public static String getGCLIConnect() { 
      if (GCLIConnect_ == null)  {
         GCLIConnect_ =  " SERVER=NOTSET ";
        //
        // Initialize the connection string2 for the CLI test
        //
        String gDSN = System.getProperty("CLIWGDSN");
        if (gDSN != null) {
          GCLIConnect_ = " SERVER=" + gDSN + " ";
        }
        String gUID = System.getProperty("CLIWGUID");
        if (gUID != null) {
          GCLIConnect_ = GCLIConnect_ + "USERID=" + gUID.toUpperCase() + " ";
        }
        String gPWD = System.getProperty("CLIWGPWD");
        if (gPWD != null) {
          char[] encryptedPassword = PasswordVault.getEncryptedPassword(gPWD);
          GCLIConnect_ = GCLIConnect_ + "PASSWORD=" + PasswordVault.decryptPasswordLeak(encryptedPassword) + " ";
        }
      }
      return GCLIConnect_; 
    }

    
    public static String getHCLIConnect() { 
      if (HCLIConnect_ == null)  {
         HCLIConnect_ =  " SERVER=NOTSET ";
        //
        // Initialize the connection string2 for the CLI test
        //
        String gDSN = System.getProperty("CLIWHDSN");
        if (gDSN != null) {
          HCLIConnect_ = " SERVER=" + gDSN + " ";
        }
        String gUID = System.getProperty("CLIWHUID");
        if (gUID != null) {
          HCLIConnect_ = HCLIConnect_ + "USERID=" + gUID.toUpperCase() + " ";
        }
        String gPWD = System.getProperty("CLIWHPWD");
        if (gPWD != null) {
          char[] encryptedPassword = PasswordVault.getEncryptedPassword(gPWD);
          HCLIConnect_ = HCLIConnect_ + "PASSWORD=" + PasswordVault.decryptPasswordLeak(encryptedPassword) + " ";
        }
      }
      return HCLIConnect_; 
    }
    
    public static String getZCLIConnect() { 
      if (ZCLIConnect_ == null)  {
         ZCLIConnect_ =  " SERVER=NOTSET ";
        //
        // Initialize the connection string2 for the CLI test
        //
        String gDSN = System.getProperty("CLIWZDSN");
        if (gDSN != null) {
          ZCLIConnect_ = " SERVER=" + gDSN + " ";
        }
        String gUID = System.getProperty("CLIWZUID");
        if (gUID != null) {
          ZCLIConnect_ = ZCLIConnect_ + "USERID=" + gUID.toUpperCase() + " ";
        }
        String gPWD = System.getProperty("CLIWZPWD");
        if (gPWD != null) {
          char[] encryptedPassword = PasswordVault.getEncryptedPassword(gPWD);
          ZCLIConnect_ = ZCLIConnect_ + "PASSWORD=" + PasswordVault.decryptPasswordLeak(encryptedPassword) + " ";
        }
      }
      return ZCLIConnect_; 
    }
    
    
    public static String getLCLIConnect() { 
      if (LCLIConnect_ == null)  {
         LCLIConnect_ =  " SERVER=NOTSET ";
        //
        // Initialize the connection string2 for the CLI test
        //
        String gDSN = System.getProperty("CLIWLDSN");
        if (gDSN != null) {
          LCLIConnect_ = " SERVER=" + gDSN + " ";
        }
        String gUID = System.getProperty("CLIWLUID");
        if (gUID != null) {
          LCLIConnect_ = LCLIConnect_ + "USERID=" + gUID.toUpperCase() + " ";
        }
        String gPWD = System.getProperty("CLIWLPWD");
        if (gPWD != null) {
          char[] encryptedPassword = PasswordVault.getEncryptedPassword(gPWD);
          LCLIConnect_ = LCLIConnect_ + "PASSWORD=" + PasswordVault.decryptPasswordLeak(encryptedPassword) + " ";
        }
      }
      return LCLIConnect_; 
    }
    
    
    public static boolean useCliJobRun = false; 
    protected String systemName = null ;


    public static int cliJobRunPort  = 0;
    public static int cliJobRunTimeout = 30000; 
    static Socket cliJobRunSocket = null;
    static DataInputStream cliJobRunIn = null; 
    static DataOutputStream cliJobRunOut = null;

    /* Server mode versions */ 
    public static int cliJobRunSMPort  = 0;
    static Socket cliJobRunSMSocket = null;
    static DataInputStream cliJobRunSMIn = null; 
    static DataOutputStream cliJobRunSMOut = null;



    static byte[] newLineBytes = { '\n' }; 
    static byte[] cliJobRunInBuffer = new byte[4096]; 
    

    static boolean usePase = false; 

/**
Constructor.
**/
    public JDJSTPTestcase (AS400 systemObject,
                       String testcaseName,
                       Hashtable <String, Vector<String>>namesAndVars,
                       int runMode,
                       FileOutputStream fileOutputStream,
                       
                       String password)
    {
       super(systemObject, testcaseName, namesAndVars, runMode, fileOutputStream,  password);
    }

    
    public JDJSTPTestcase (AS400 systemObject,
        String testcaseName,
        Hashtable <String, Vector<String>>namesAndVars,
        int runMode,
        FileOutputStream fileOutputStream,
        
        String password, 
        String pwrUserid,
        String pwrPassword)
{
super(systemObject, testcaseName, namesAndVars, runMode, fileOutputStream,  password, pwrUserid, pwrPassword);
}

    
    
    protected void setup() throws Exception {
      

	if (globalURL == null) { 
	    globalURL=baseURL_;
	    globalUserId=userId_;
	    globalEncryptedPassword=encryptedPassword_;
	    globalDriver = testDriver_;
	}
        if (systemName_ == null) {
           System.out.println("baseURL_="+baseURL_);
           int lastColon = baseURL_.lastIndexOf(':');
           lastColon++; 
           while (baseURL_.charAt(lastColon) == '/') {
             lastColon++; 
           }
           systemName=baseURL_.substring(lastColon); 
           System.out.println("systemname is null using "+systemName);
        } else {
           systemName=systemName_; 
        }
	if (useCliJobRun) {
	    System.out.println("Testcase using CLIJOBRUN"); 
	} 
	
	int dotIndex = systemName.indexOf('.'); 
	if (dotIndex > 0) {
	  systemName = systemName.substring(0,dotIndex); 
	}
        int length = systemName.length(); 
        int start = length - 4; 
        if (start < 0) start = 0; 
        envLibrary="JDJSTP"+systemName.substring(start, length); 
        envLibrary = envLibrary.toUpperCase(); 
	//
        // Determine the release of the operating system
	//
	{
	    vrm = testDriver_.getRelease(); 

	    // System.out.println("JDJSTPjavastp: The vrm is "+vrm);
	    if (vrm > 450) nullBug=false; 
	}
	globalDriver = testDriver_;  
	JDSQL400.setUrl(globalURL);
	JDSQL400.setUserId(userId_);
	JDSQL400.setPassword(PasswordVault.decryptPasswordLeak(encryptedPassword_,  "JDJSTPTestcase.setup"));

	if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
	    isToolbox = true;
	} 

	connectSTP = System.getProperty("JDJSTP.connectSTP");
	disconnectSTP = System.getProperty("JDJSTP.disconnectSTP");
    }

    static protected void cleanupCliJobRunSocket() throws Exception {
      if (cliJobRunSocket != null) {
	  try { 
	// Send a message to the port and wait for the reply.
	      String cliJobCommand = "KILLSERVERKILL\n"; 

	      if (debug) System.out.println("Sending message "+cliJobCommand); 

	      byte[] outBytes = cliJobCommand.getBytes("US-ASCII");


	      try {
		  if (cliJobRunOut != null)
		      cliJobRunOut.write( outBytes, 0, outBytes.length );
	      } catch (Exception e) {
	      }
	      try {
		  if (cliJobRunSMOut != null)
		      cliJobRunSMOut.write( outBytes, 0, outBytes.length );
	      } catch (Exception e) {
	      }

	      byte[] inBuffer = new byte[4095];

	      try {
		  if (cliJobRunIn != null)
		      cliJobRunIn.read(inBuffer);
	      } catch (Exception e) {
	      } 

	      try {
		  if (cliJobRunSMIn != null)
		  cliJobRunSMIn.read(inBuffer);
	      } catch (Exception e) {
	      } 

	      if (debug) System.out.println("Cli job socket set to null"); 
	      cliJobRunSocket = null;

	  } catch (Exception e) {
          e.printStackTrace(System.out); 
        }
      } else {
	  if (debug) System.out.println("Cli job socket does not exist"); 
      }


      if (cliJobRunSMSocket != null) {
	  try { 
	// Send a message to the port and wait for the reply.
	      String cliJobCommand = "KILLSERVERKILL\n"; 

	      if (debug) System.out.println("Sending message "+cliJobCommand); 

	      byte[] outBytes = cliJobCommand.getBytes("US-ASCII");


	      try {
		  if (cliJobRunSMOut != null)
		      cliJobRunSMOut.write( outBytes, 0, outBytes.length );
	      } catch (Exception e) {
	      }
	      try {
		  if (cliJobRunSMOut != null)
		      cliJobRunSMOut.write( outBytes, 0, outBytes.length );
	      } catch (Exception e) {
	      }

	      byte[] inBuffer = new byte[4095];

	      try {
		  if (cliJobRunSMIn != null)
		      cliJobRunSMIn.read(inBuffer);
	      } catch (Exception e) {
	      } 

	      try {
		  if (cliJobRunSMIn != null)
		  cliJobRunSMIn.read(inBuffer);
	      } catch (Exception e) {
	      } 

	      if (debug) System.out.println("Cli job sm socket set to null"); 
	      cliJobRunSMSocket = null;

	  } catch (Exception e) {
          e.printStackTrace(System.out); 
        }
      } else {
	  if (debug) System.out.println("Cli job sm socket does not exist"); 
      }



    } 
    protected void cleanup() throws Exception  {
      if (debug) System.out.println("Running JDJSTP.cleanup"); 

      cleanupCliJobRunSocket();  
    }

/**
 * Utility functions -- formerly in RTest
 */
    static String globalURL = null;
    static String globalUserId = null;
    static char[] globalEncryptedPassword = null; 
    static JDTestDriver globalDriver = null;
    static String hostname1 =null; 
    static boolean clientSameAsServer = false;

    protected static String nativeBaseDir = JTOpenTestEnvironment.testcaseHomeDirectory+"3.NOLIB"; 
    protected static String funcpath = "/QIBM/UserData/OS400/SQLLib/Function";
    protected static String library = "NOLIB";

    // Keep this static variable private
    // These should be set in the setup (not in the constructor) 
    private static String sourcepath="sourcepathNotSet";
    private static String javaRunPath="javaRunPathNotSet";
    private static String exppath="expPathNotSet";

    protected static int vrm = 0;
    public static boolean useNewActivationGroup = false; 
    protected static boolean      dumpJobLog = false;
    static boolean jdk13 = false;
    static boolean v7r1  = false;
    static boolean v7r2  = false;
    static boolean v7r3  = false;
    static boolean v7r4  = false;
    static boolean v7r5  = false;
    static boolean v7r6  = false; 
    static boolean v7r6plus = false; 
    static boolean nativeClient = false; 
    static int CONTEXT_LINE_COUNT = 5;
    static int DIFFERENCES_COUNT = 10;
    static Connection cmdConn = null;
    static Connection mirrorCmdConn = null;
    static boolean mirroringChecked = false; 
    static CallableStatement cmdCallableStatement = null;
    static CallableStatement mirrorCmdCallableStatement = null; 
    static Statement         cmdStatement = null;
    static Statement         mirrorCmdStatement = null;  
    private static char     sep='/';
    static boolean isToolbox = false; 
    static String pathSep; 


    /**
     * Static initializer sets up debug flag
     * also sets up VRM.
     **/
  static {


    pathSep = System.getProperty("path.separator").trim();
    String sepString = System.getProperty("file.separator");
    sep = sepString.charAt(0);

    String propertyString;
    debug = false;
    try {
      propertyString = System.getProperty("JDJSTP.debug");
      if (propertyString != null) {
        propertyString = propertyString.toUpperCase().trim();
        if (propertyString.equals("TRUE")) {
          debug = true;
        }
      }
    } catch (Exception dontCare) {
    }

    connectSTP = System.getProperty("JDJSTP.connectSTP");
    disconnectSTP = System.getProperty("JDJSTP.disconnectSTP");

    try {
      if (JTOpenTestEnvironment.isOS400) {
        String osVersion = System.getProperty("os.version");
        if (osVersion == null)
          osVersion = "null";
        v7r1 = osVersion.equals("V7R1M0");
        if (debug) {
          System.out.println("JDJSTP.debug:  setting v7r1 = " + v7r1);
        }
        if (!v7r1) {
          v7r2 = osVersion.equals("V7R2M0");

          if (debug) {
            System.out.println("JDJSTP.debug:  setting v7r2 = " + v7r2);
          }
          if (!v7r2) {

            v7r3 = osVersion.equals("V7R3M0");

            if (debug) {
              System.out.println("JDJSTP.debug:  setting v7r3 = " + v7r3);

            }
            if (!v7r3) {
              v7r4 = osVersion.equals("V7R4M0") || osVersion.equals("7.4");

              if (debug) {
                System.out.println("JDJSTP.debug:  setting v7r4 = " + v7r4);

              }
              if (!v7r5) {
                v7r5 = osVersion.equals("V7R5M0") || osVersion.equals("7.5");

                if (debug) {
                  System.out.println("JDJSTP.debug:  setting v7r5 = " + v7r5);
                }

                if (!v7r6) {
                  v7r6 = osVersion.equals("V7R6M0") || osVersion.equals("7.6");

                  if (debug) {
                    System.out.println("JDJSTP.debug:  setting v7r6 = " + v7r6);
                  }
                  if (!v7r6) {
                    v7r6plus = (osVersion.indexOf("V7R") >= 0) || (osVersion.indexOf("7.") >= 0);
                    if (debug) {
                      System.out.println("JDJSTP.debug:  setting v7r6plus = " + v7r6plus);
                    }
                    if (!v7r6plus) {
                      throw new Exception("Unable to select release");
                    }
                  } /* !v7r6 */
                } /* !V7r6 */
              } /* not v7r5 */
            } /* not v7r3 */
          } /* not v7r2 */
        } /* not v7r1 */
      }
    } catch (Exception dontCare) {
      dontCare.printStackTrace(System.out);
    }

    try {
      propertyString = System.getProperty("JDJSTP.dumpJobLog");
      if (propertyString != null) {
        propertyString = propertyString.toUpperCase().trim();
        if (propertyString.equals("TRUE")) {
          dumpJobLog = true;
        }
      }
    } catch (Exception dontCare) {
    }

    //
    // Determine if nativeClient
    //
    try {
      nativeClient = false;
      if (JTOpenTestEnvironment.isOS400) {
        nativeClient = true;
      }
    } catch (Exception dontCare) {
    }


 
  }

    /**
     * Do actions at connect time to set up code coverage tool (or other action)
     */ 

    protected static void connectionConnect(Connection conn, String name)  {
	connectionConnect(conn, name, System.getProperty("java.home"));
    } 


    protected static void connectionConnect(Connection conn, String name, String javaHome)  {
	try { 
	    if (connectSTP != null) { 
		CallableStatement cstmt = conn.prepareCall("call "+connectSTP+"(?)");
		cstmt.setString(1, name);
		cstmt.execute(); 
	    }
	    if (javaHome != null) {

		// Make sure the system is IBM i before calling this

		DatabaseMetaData dmd = conn.getMetaData();
		String productName = dmd.getDatabaseProductName();
		if (productName.equals("DB2")) {
		    // Not IBM i..
		    return; 
		} 
		Statement stmt = conn.createStatement(); 
		String[] sql = getSetJVMSQL(javaHome);
		try {
		    stmt.executeUpdate(sql[0]); 
		} catch (Exception e) {
		}
		try {
		    stmt.executeUpdate(sql[1]); 
		} catch (Exception e) {
		    System.out.println("For "+sql[1]); 
		    e.printStackTrace(System.out); 
		}
		stmt.close(); 

	    }
	} catch (Exception e) {
	    e.printStackTrace(System.out); 
	} 
    } 

    protected static void connectionDisconnect(Connection conn, String name)  {
	try { 
	    if (disconnectSTP != null) { 
		CallableStatement cstmt = conn.prepareCall("call "+disconnectSTP+"(?)");
		cstmt.setString(1, name);
		cstmt.execute(); 
	    }
	} catch (Exception e) {
	    e.printStackTrace(System.out); 
	} 
    } 


    /**
     * Make sure the envLibrary is setup
     */ 
    protected static void checkSetup() throws Exception {
	
        // Make sure the connection exists 
	if (cmdConn == null) {
	    if (globalDriver == null) {
		System.out.println("-----------------------------------------");
		System.out.println("Warning:  JDJSTPTestcase.checkSetup connecting without password"); 
		cmdConn = DriverManager.getConnection("jdbc:db2:*LOCAL");
		Exception e = new Exception("Current Stack"); 
		e.printStackTrace(System.out);
		System.out.println("-----------------------------------------"); 
	    } else {
		if (debug) System.out.println("JDJSTPTestcase.getSetup1: Getting connection for "+globalURL+","+ globalUserId); 
		cmdConn = globalDriver.getConnection(globalURL, globalUserId, globalEncryptedPassword);
	    }
	}


	if (hostname1 == null) {
	    hostname1 =  java.net.InetAddress.getLocalHost().getHostName().toLowerCase();
	    int dotIndex = hostname1.indexOf('.');
	    if (dotIndex > 0) {
		hostname1 = hostname1.substring(0, dotIndex); 
	    }
	    if (globalURL == null) globalURL="jdbc:db2:*LOCAL"; 
	    if (debug) System.out.println("JDJSTP.debug: checkSetup: hostname is "+hostname1+" globalURL is "+globalURL);
	    if (globalURL.indexOf(hostname1) > 0) clientSameAsServer=true;
	    if (globalURL.indexOf("*LOCAL") > 0) clientSameAsServer=true;
	    if (globalURL.indexOf("*local") > 0) clientSameAsServer=true;
	    if (globalURL.indexOf("localhost") > 0) clientSameAsServer=true;

	    if (debug) {
		System.out.println("JDJSTP.debug: checkSetup:  clientSameAsServer = "+clientSameAsServer); 
	    } 

	} 


	if (cmdStatement == null) { 
	    cmdStatement = cmdConn.createStatement();
	}

	//
	// Check to see if we are running on a mirrored system.
	// If so, create a connection to the mirror system so
        // we can execute commands. 
	{
	    if (!mirroringChecked) { 
	    if (true) {
	    	System.out.println("JDJSTP.debug: checkSetup: checking for mirror"); 
	    }
		ResultSet rs = cmdStatement.executeQuery("select  DBXRDBN from qsys.QADBXRDBD WHERE  DBXRDBN = 'DB2M_RMT'");
		if (rs.next()) {
		    if (true) System.out.println("JDJSTPTestcase.checkSetup: Getting connection for jdbc:db2:DB2M_RMT,"+ globalUserId);
		    if (mirrorCmdConn == null) {
			mirrorCmdConn = DriverManager.getConnection("jdbc:db2:DB2M_RMT", globalUserId, PasswordVault.decryptPasswordLeak(globalEncryptedPassword, "JDJSTPTestcase.cs"));
			mirrorCmdStatement = mirrorCmdConn.createStatement();
			try {
			    boolean oldDebug = debug;
			    debug=true; 
			    assureSTPJOBLOGXisAvailable(mirrorCmdConn);
			    debug = oldDebug; 
			} catch (Exception e) {
			    System.out.println("Warning:  assureSTPJOBLOGXisAvailable failed for mirror connection");
			    e.printStackTrace(System.out); 
			} 
		    } 
		}
		rs.close();
		mirroringChecked=true; 
	    }

	}


	if (! envLibrary.equals(cmdChecked)) {
	    cmdChecked=envLibrary; 
	// 
	// Make sure that the stored procedure exists
	// If not, make sure everything is created
	// 
	    DatabaseMetaData dmd = cmdConn.getMetaData();
	    try {
		if (debug) System.out.println("JDJSTP.debug:  Getting procedures for "+envLibrary+".CMD"); 
		ResultSet rs = dmd.getProcedures(null, envLibrary, "CMD");
		if ( ! rs.next()) {
		    execStatement(cmdStatement, "create schema "+envLibrary, true);

		    execStatement(cmdStatement, "CALL QSYS.QCMDEXC('"+
				  "QSYS/GRTOBJAUT OBJ("+envLibrary+") OBJTYPE(*LIB) USER(*PUBLIC) AUT(*ALL)                            ',0000000080.00000)", true); 

		    execStatement(cmdStatement, "create procedure "+
				  envLibrary+".CMD(IN CMDSTR VARCHAR(1024),IN CMDLEN DECIMAL(15,5)) "+
				  "External name QSYS.QCMDEXC LANGUAGE C GENERAL", true);
		    execStatement(cmdStatement, "create table "+envLibrary+".FILES(source varchar(80), sourcetimestamp timestamp, sourcepf varchar(11))",true);
		    execStatement(cmdStatement, "GRANT ALL ON "+envLibrary+".FILES TO PUBLIC", true);

		} 
		rs.close();


	    } catch (Exception e) {
		System.out.println("JDJSTP.debug: Ignoring error");
		System.out.println("JDJSTP.debug: -----------------------------"); 
		e.printStackTrace(System.out);
		System.out.println("JDJSTP.debug: -----------------------------"); 
	    } catch (java.lang.UnknownError jlu) {
		System.out.println("ERROR.. java.lang.Unknown"); 
		jlu.printStackTrace(System.out); 
	    } 
	}

    }

    /**
     * execute a statement and ignore errors if needed
     */
    protected static int execStatement(Statement stmt, String sql, boolean ignoreErrors) throws Exception {
	int count = 0; 
	try {
	    if (debug) System.out.println("JDJSTP.debug: execStatement: "+sql); 
	    count = stmt.executeUpdate(sql); 
	} catch (Exception ex) {
	    if (debug) ex.printStackTrace(System.out); 
	    if (!ignoreErrors) {
		System.out.println("JDJSTP.debug: Error executing -- "+sql); 
		throw ex; 
	    } else {
		if (debug) System.out.println("JDJSTP.debug: Error on "+sql+" ignored"); 
	    } 
	}
	return count; 
    }

    public static void setUsePase(boolean newUsePase) {
	usePase = newUsePase; 
    } 
    /**
     * Set the library used to compile C source files
     * @param expPath The path used to find the results files.
     **/
    public static void setLibrary(String setLibrary) {
       if (debug) System.out.println("JDJSTP.debug: Setting library = "+setLibrary); 
       library = setLibrary;
       nativeBaseDir = JTOpenTestEnvironment.testcaseHomeDirectory+"3."+library; 

       // 
       // Make sure that the directory if newer than the library, if not then delete the 
       // directory so it will be recreated.  
       //
       try {
	   checkSetup(); 
	   DatabaseMetaData dmd = cmdConn.getMetaData(); 
	   if (dmd == null) {
	       System.out.println("DMD for cmdConn="+cmdConn+" is null"); 
	   } 
	   if ( (dmd != null) && "DB2 UDB for AS/400".equals(dmd.getDatabaseProductName())) { 
	       String command; 


	       serverCommand("QSYS/GRTOBJAUT OBJ("+envLibrary+"/"+library+") OBJTYPE(*file) USER(*PUBLIC) AUT(*ALL)", true);


	        //
	       // Update the FILES in the env library if the library is newer 
	       // 
	       boolean retry = true;
	       while (retry) { 
	         retry = false; 
  	       command = " DSPOBJD OBJ("+library+") OBJTYPE(*LIB) OUTPUT(*OUTFILE) OUTFILE("+
  		 envLibrary+"/"+library+")"; 
  	       if (debug) System.out.println("JDJSTP.debug: Checking "+command);
  	       try { 
  		   serverCommand(command, false);
  	       } catch (Exception e) {
  	         String message = e.toString(); 
  	         if (message.indexOf("type *LIB not found") >= 0 ) {
  	           Statement s = cmdConn.createStatement(); 
  	           s.execute("CREATE COLLECTION "+library); 
  	           retry = true; 
  	           s.executeUpdate("GRANT ALL ON SCHEMA " + library  + " TO PUBLIC " );
  	              
  	         } else { 
  		   System.out.println("Warning:  Command = "+command);
  		   e.printStackTrace(); 
  	         }
  	       } 
	       }


	       command = "DELETE FROM "+envLibrary+".FILES WHERE SOURCE LIKE '%"+library+"%' AND "+
		 " SOURCETIMESTAMP < "+
		 " (SELECT TIMESTAMP('20' || SUBSTRING(ODCDAT,5,2) || SUBSTRING(ODCDAT,1,4) " +
		 " || ODCTIM) "+
		 " from "+envLibrary+"."+library+")"; 

	       if (debug) System.out.println("JDJSTP.debug: Checking "+command);
	       try { 
		   cmdStatement.executeUpdate(command); 
	       } catch (Exception e) {
		   System.out.println("ERROR on : "+command); 
		   e.printStackTrace(System.out);
		   
		   command = "DELETE FROM "+envLibrary+".FILES WHERE SOURCE LIKE '%"+library+"%'";
		   System.out.println("Deleting all "+command); 
		   cmdStatement.executeUpdate(command); 

	       }
	   }
       } catch (Exception e) { 
	   e.printStackTrace(System.out); 
       }

    }

    /**
     * Set the path used to find the source files.  This is the
     * path below the current directory on the client and below /home/jdbc on the server. 
     * @param sourcePath The path used to find the source files.
     **/

    public static void setSourcepath(String sourcePath) {
       sourcepath = sourcePath;
    }

    /**
     * Set the path used to find the expect results files.
     * @param expPath The path used to find the results files.
     **/
    public static void setExppath(String expPath) {
       exppath = expPath;
    }

     /**
      * getInheritedJavaOptions
      *
      * returns -Dxxx options inherited from parent
      */
    public static String getInheritedJavaOptions() {
	String options ="";

	String inheritedProperties[] = {
	    "jdbc.db2.native.library",
	    "os400.class.path.system",
            "jdbc.db2.j9.library", 
            "jdbc.db2.cli.trace",
            "jdbc.db2.trace",
            "com.ibm.as400.access.Trace.category",
            "com.ibm.as400.access.Trace.file",
	} ;

	for (int i = 0; i < inheritedProperties.length; i++) {
	    String value = System.getProperty(inheritedProperties[i]);
	    if (value != null) {
		options += " -D"+inheritedProperties[i]+"="+value;
	    } 
	}

	//
	// check out the sun.boot.class.path.  Only add if contains a
	// /home/ directory
	//

	String value = System.getProperty("sun.boot.class.path");
	if (value != null) {
	    int startIndex = value.indexOf("/home/");
	    if (startIndex >= 0) {
		// if running J9 use the -X option
		    int endIndex=value.indexOf(":",startIndex);
		    options += " -Xbootclasspath/a:"+value.substring(startIndex,endIndex)+" "; 
	    }

	} else {
	    if (JVMInfo.getJDK() < JVMInfo.JDK_V11) {  /* Java 11 does not have sun.boot.class.path */ 
		System.out.println("Warning.. sun.boot.class.path is not defined");
	    }
	}

	
	//
	// check out the java.class.path.  Only add if contains a
	// jdbcsrc directory
	//
	String userDir = System.getProperty("user.dir"); 
	value = System.getProperty("java.class.path");
	
	if (value != null) {
	    if (value.indexOf("jdbcsrc") > 0) {
		options += " -cp "+value+":"+userDir+"/stp/javastp/test:"+userDir+"/stp/jdbc/test:/qibm/proddata/http/public/jt400/lib/jt400.jar:"+userDir+"/jars/jt400.jar:.";
		value = JVMInfo.getJavaVersionString(); 
		if (value.equals("1.5.0")) value = "1.5"; 
		options += " -Djava.version="+value; 
	    }

	} 

	//
	// check out the java.ext.dirs.  Only add if contains a
	// jdbcsrc directory
	//
	String javaExtDirs = System.getProperty("java.ext.dirs"); 
	if (javaExtDirs != null) {
	    if (javaExtDirs.indexOf("jdbcsrc") > 0) {
		options += " -Djava.ext.dirs="+javaExtDirs+" ";
	    }
	} 


 


	//
	// add additional options to inherit
	// 

	options += " -DURL=" + globalURL +"  -Duserid="
                + globalUserId + " -Dpassword=" + PasswordVault.decryptPasswordLeak(globalEncryptedPassword, "JDJSTPTestcase.gio");


          if (library != null && (!library.equals("NOLIB"))) {
            options += " -DLIBRARY=" + library+" ";
          }

        return options; 
    } 

     /**
      * install installs a binary file in the function path directory.  The
      * install is only done if the source is newer than the installed file.<br>
      * @param javaSource The filename of the file to install.  The file should reside in the
      * the directory for RTest.  This directory is set using RTest.setSourcepath.
      * @exception java.lang.Exception If an error occurs, an exception is thrown describing the error.
      * @see #setSourcepath
      **/
		public static void install(String filename) throws Exception {

			//
			// Check to see if the file needs to be reinstalled
			//
			String sourceFile = sourcepath + "/" + filename;
			String destFile = funcpath + "/" + filename;
			boolean updated = updateBinaryServerFile(sourceFile, destFile);
			if (!updated) {
				return;
			}
			serverShellCommand("chmod +rx " + destFile, false);

	    }

     /**
      * installServer installs a binary file in the function path directory.  The
      * install is only done if the source is newer than the installed file.<br>
      * This copies a compiled object from the server directory (without path) 
      * @param javaSource The filename of the file to install.  The file should reside in the
      * the directory for RTest.  This directory is set using RTest.setSourcepath.
      * @exception java.lang.Exception If an error occurs, an exception is thrown describing the error.
      * @see #setSourcepath
      **/
		public static void installServer(String filename) throws Exception {

			//
			// Check to see if the file needs to be reinstalled
			// For now, don't check, just do it.
			//
			String sourceFile = nativeBaseDir + "/" + filename;
			String destFile = funcpath + "/" + filename;
			String command = "QSYS/copy OBJ('" + sourceFile + "') TODIR('" + funcpath + "') ";
			serverCommand("QSYS/rmvlnk OBJLNK('" + destFile + "')", true /* ignore error */);
			serverCommand(command, false);
			serverShellCommand("chmod +rx " + destFile, false);

		}


     /**
      * creates a jar file from class files
      * The source is the same as the destination of link 
      * The destination is the sourcepath
      * This is executed on the server 
      */
		public static void makeJar(String jarFile, String[] classFiles) throws Exception {

			String command = "rm " + jarFile + "; cd " + funcpath + "; jar cvf " + nativeBaseDir + "/" + sourcepath
					+ "/" + jarFile + " ";
			for (int i = 0; i < classFiles.length; i++) {
				command = command + " " + classFiles[i];
			}
			serverShellCommand(command, false);

			if (debug)
				System.out.println("JDJSTP.debug: Checking for "+ nativeBaseDir + "/" + sourcepath + "/" + jarFile);
			if (!serverFileExists(nativeBaseDir + "/" + sourcepath + "/" + jarFile)) {
				throw new Exception("Jar file not created :" + nativeBaseDir + "/" + sourcepath+"/"+jarFile + " server command was "+command);
			}

		}

                public static int getIntFromQuery(String query) throws Exception {
                  ResultSet rs = cmdStatement.executeQuery(query);
                  if (rs.next()) {
                    int value = rs.getInt(1);
                    rs.close();
                    return value;
                  } else {
                    rs.close();
                    throw new Exception("Query returned no rows");
                  }
                }
		
     /**
      * checks to see if a server file exists
      * If a full path is not specified, then /home/jdbc/<path> is checked 
      */ 
		public static boolean serverFileExists(String filename) throws Exception {
			checkSetup();
			assureFILEEXISTSisAvailable(cmdConn);
			if (filename.indexOf('/') == 0) {
				// Leave as is
			} else {
				filename = nativeBaseDir + "/" + filename;
			}
			if (clientSameAsServer) {
				File testFile = new java.io.File(filename);
				if (testFile.exists()) {
					return true;
				} else {
					return false;
				}
			} else {
				int found = getIntFromQuery(
						"select " + envLibrary + ".fileexists('" + filename + "') from qsys2.qsqptabl");
				if (found == 0) {
					return false;
				} else if (found == 1) {
					return true;
				} else {
					throw new Exception("Value returned from query, " + found + ", is not valid");
				}
			}
		}

		public static void cat(String filename) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(filename));
				String line = reader.readLine();
				while (line != null) {
					System.out.println(line);
					line = reader.readLine();
				}
				reader.close();
			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
		}

		public static boolean serverFileNewer(String serverFile, String localFile) throws Exception {
			checkSetup();
			assureFILETIMEisAvailable(cmdConn);
			assureCURTIMEisAvailable(cmdConn);

			if (serverFile.indexOf('/') == 0) {
				// Leave as is
			} else {
				serverFile = nativeBaseDir + "/" + serverFile;
			}
			int filetime;
			if (clientSameAsServer) {
				File checkFile = new File(serverFile);
				filetime = (int) (checkFile.lastModified() / 1000);
			} else {
				filetime = getIntFromQuery(
						"select " + envLibrary + ".FILETIME('" + serverFile + "') from qsys2.qsqptabl");
				if (filetime < 0) {
					throw new Exception("Unable to get time of server file = " + serverFile);
				}
			}
			int curtime;
			if (clientSameAsServer) {
				curtime = (int) (System.currentTimeMillis() / 1000);
			} else {
				curtime = getIntFromQuery("select " + envLibrary + ".CURTIME() from qsys2.qsqptabl");
			}

			int serverAgeInSeconds = curtime - filetime;

			long localTime = System.currentTimeMillis();
			File file = new File(localFile);
			long localFileTime = file.lastModified();
			if (localFileTime == 0L) {
				throw new Exception("Unable to get time of local file = " + localFile);
			}
			int localAgeInSeconds = (int) ((localTime - localFileTime) / 1000);

			if (debug) {
				System.out.println("JDJSTP.debug: serverFileNewer:  serverFile     = " + serverFile);
				System.out.println("JDJSTP.debug: serverFileNewer:  serverTime     = " + curtime);
				System.out.println("JDJSTP.debug: serverFileNewer:  serverFileTime = " + filetime);
				System.out.println("JDJSTP.debug: serverFileNewer:  serverAge      = " + serverAgeInSeconds);
				System.out.println("JDJSTP.debug: serverFileNewer:  localFile     = " + localFile);
				System.out.println("JDJSTP.debug: serverFileNewer:  localTime      = " + localTime / 1000);
				System.out.println("JDJSTP.debug: serverFileNewer:  localFileTime  = " + localFileTime / 1000);
				System.out.println("JDJSTP.debug: serverFileNewer:  localAge       = " + localAgeInSeconds);
			}

			return (serverAgeInSeconds < localAgeInSeconds);
		}

     public static void linkClient(String javaSource) throws Exception {
	     linkClient(javaSource, "");
     }
     /**
      * l compiles a java source program and installs in the function path directory.  The
      * compile is only done if the source is newer than the installe file.<br>
      * -- l means link  ( or compile and link).
      * @param javaSource The filename of the java source to compile.  The file should reside in
      * the sourcePath directory.  This directory is set using setSourcepath.
      * @exception java.lang.Exception If an error occurs, an exception is thrown describing the error.
      * @see #setSourcepath
      **/
     public static void link(String javaSource) throws Exception {
       
       link(javaSource,14); 
     }

		public static void link(String javaSource, int javaVersion1) throws Exception {
			if (vrm >= 510) {
				link(javaSource,
						"-classpath /QIBM/ProdData/OS400/Java400/ext/db2routines_classes.jar:/QIBM/ProdData/OS400/Java400/ext/runtime.zip:"
								+ sourcepath + " ",
						javaVersion1);
			} else {
				link(javaSource,
						"-classpath /QIBM/ProdData/Java400/ext/db2routines_classes.jar:/QIBM/ProdData/Java400/ext/runtime.zip:"
								+ sourcepath + " ",
						javaVersion1);
			}
		}

     /**
      * Generate a physical file name for use on the AS/400
      */ 
		public static String genPfName(String sourceName) {
			// Replace . with x
			sourceName = sourceName.replace('.', 'x');

			// Strip the leading path
			int slashIndex = sourceName.lastIndexOf('/');
			if (slashIndex > 0) {
				sourceName = sourceName.substring(slashIndex + 1);
			}
			slashIndex = sourceName.lastIndexOf('\\');
			if (slashIndex > 0) {
				sourceName = sourceName.substring(slashIndex + 1);
			}
			// Use the first 10 characters that are left
			if (sourceName.length() > 10) {
				sourceName = sourceName.substring(0, 10);
			}
			return sourceName;
		}


		public static void showFile(String file) throws Exception {
			serverCommand("QSYS/CRTSRCPF FILE(QGPL/JDJSTPSHOW) RCDLEN(300)", true);
			serverCommand("QSYS/GRTOBJAUT OBJ(QGPL/JDJSTPSHOW) OBJTYPE(*file) USER(*PUBLIC) AUT(*ALL)", true);

			serverCommand("QSYS/CPYFRMSTMF FROMSTMF('" + file + "') "
					+ "TOMBR('/QSYS.LIB/QGPL.LIB/JDJSTPSHOW.FILE/JDJSTPSHOW.MBR') MBROPT(*REPLACE)", true);
			ResultSet rs = cmdStatement.executeQuery("select SRCDTA from QGPL.JDJSTPSHOW");

			boolean found = rs.next();
			while (found) {
				String line = rs.getString(1);
				System.out.println(line);
				found = rs.next();
			}
			rs.close();
		}

		public static String getFile(String file) throws Exception {
			StringBuffer sb = new StringBuffer();
			serverCommand("QSYS/CRTSRCPF FILE(QGPL/JDJSTPSHOW) RCDLEN(300)", true);
			serverCommand("QSYS/GRTOBJAUT OBJ(QGPL/JDJSTPSHOW) OBJTYPE(*file) USER(*PUBLIC) AUT(*ALL)", true);

			serverCommand("QSYS/CPYFRMSTMF FROMSTMF('" + file + "') "
					+ "TOMBR('/QSYS.LIB/QGPL.LIB/JDJSTPSHOW.FILE/JDJSTPSHOW.MBR') MBROPT(*REPLACE)", true);
			ResultSet rs = cmdStatement.executeQuery("select SRCDTA from QGPL.JDJSTPSHOW");

			boolean found = rs.next();
			while (found) {
				String line = rs.getString(1);
				sb.append(line);
				if (!line.endsWith("\n")) {
					sb.append("\n");
				}
				found = rs.next();
			}
			rs.close();
			return sb.toString();
		}

     /**
      * l compiles a java source program and installs in the function path directory.  The
      * compile is only done if the source is newer than the installe file.<br>
      * 
      * NOTE:  These actions must take place on the host.
      * To accomplish this we define a series of stored procedures to do this work.
      * Step 1.  Check if compilation needs to be done.
      * If needed...
      * Step 2.  Move source from client to host
      * Step 3.  Compile code on host 
      * 
      * -- l means link  ( or compile and link).
      * @param javaSource The filename of the java source or c source to compile.  The file should reside in
      * the sourcePath directory.  This directory is set using setSourcepath.
      * @param javacOptions Options that are passed to javac
      * @exception java.lang.Exception If an error occurs, an exception is thrown describing the error.
      * @see #setSourcepath
      **/

		public static void link(String javaSource, String javacOptions) throws Exception {
			link(javaSource, javacOptions, 14);
		}

	     public static void link(String javaSource, String javacOptions, int javaVersion1) throws Exception {

	 if (vrm >= 720 && javaVersion1 <= 15) javaVersion1=16; 

	 if (vrm >= 740 && javaVersion1 <= 18) javaVersion1=18; 

        //
        // Check for java class
        //
	if ( javaSource.trim().endsWith(".java")) {
          String classFilename = javaSource.substring(0, javaSource.lastIndexOf(".java")) + ".class";

          //
          // Check to see if the file needs to be recompiled
          //
          String sourceFile = sourcepath+"/"+javaSource;
          String destFile   = nativeBaseDir+"/"+sourcepath+"/"+javaSource;
	  String destClasses  = nativeBaseDir+"/"+sourcepath+"/"+ javaSource.substring(0, javaSource.lastIndexOf(".java")) + "*.class";
	  String serverPf   = genPfName(javaSource);


	  
	  boolean updated = updateServerFile(sourceFile, serverPf, destFile, destClasses);


	  if (! updated) {
               if (debug) System.out.println("JDJSTP.debug: 1. Skipping compile of "+sourceFile+"-- destination file "+ destFile+" is newer");
               return;
	  }
		try {
			String compileCommand = "";
			serverShellCommand("rm " + funcpath + "/" + classFilename, true);
			serverShellCommand("rm " + destClasses, true);
			if (javacOptions.indexOf("java.version") > 0) {
				serverShellCommand("cd " + nativeBaseDir + "; javac " + javacOptions + " " + sourcepath + "/"
						+ javaSource + " > /tmp/" + javaSource + ".out  2>&1", false);
			} else {
				if (vrm == 720) {
					/* JDK14 will not be shipped with v7r2 */
					compileCommand = "export -s JAVA_HOME=/QOpenSys/QIBM/ProdData/JavaVM/jdk60/32bit; cd "
							+ nativeBaseDir + "; javac  " + javacOptions + " " + sourcepath + "/" + javaSource
							+ " > /tmp/" + javaSource + ".out 2>&1";
					serverShellCommand(compileCommand, false);
				} else {
					if (vrm == 730) {
						/* JDK60 will not be shipped with v7r3 */
						compileCommand = "export -s JAVA_HOME=/QOpenSys/QIBM/ProdData/JavaVM/jdk70/32bit; cd "
								+ nativeBaseDir + "; javac  " + javacOptions + " " + sourcepath + "/" + javaSource
								+ " > /tmp/" + javaSource + ".out 2>&1";
						serverShellCommand(compileCommand, false);
					} else {
						/* JDK80 is the lowest shipped with v7r4 */
						compileCommand = "export -s JAVA_HOME=/QOpenSys/QIBM/ProdData/JavaVM/jdk80/32bit; cd "
								+ nativeBaseDir + "; javac  " + javacOptions + " " + sourcepath + "/" + javaSource
								+ " > /tmp/" + javaSource + ".out 2>&1";
						serverShellCommand(compileCommand, false);
					}
				}
			}
		      //
	      // Check the results of the compile 
	      //
			String compileResult = getFile("/tmp/" + javaSource + ".out");
			if ((compileResult.indexOf("error:") >= 0) || (compileResult.indexOf("too many parameters") >= 0)
					|| (compileResult.indexOf("java.lang.StackOverflowError") >= 0)) {
				throw new Exception("Compile failure\n" + compileResult);
			}

			serverShellCommand("cd " + nativeBaseDir + ";chmod a+rx " + destClasses, false);
			String command = "QSYS/CPY OBJ('" + destClasses + "') TODIR('" + funcpath + "')  REPLACE(*YES) ";
			try {
				serverCommand(command, false);
			} catch (Exception e) {
				/* Get the output of the compilation */
				System.out.println("----------------------------------------------------------");
				System.out.println(
						"JDJSTP.debug: Error occured while building " + javaSource + " -- running command: " + command);
				System.out.println("----------------------------------------------------------");
				showFile("/tmp/" + javaSource + ".out");
				System.out.println("----------------------------------------------------------");
				e.printStackTrace(System.out);
				System.out.println("----------------------------------------------------------");
				throw e;

			}
			serverShellCommand("rm -f /tmp/" + javaSource + ".out", false);
		} catch (Exception e) {
			System.out.println("JDJSTP.debug: Error occured while building " + javaSource + " -- rethrowing exception");
			markOutdated(sourceFile);
			e.printStackTrace(System.out);
			System.out.println("----------------------------------------------------------");
			throw e;
		}

		 
	} else {
		if (javaSource.trim().endsWith(".h")) {
			String base = javaSource.substring(0, javaSource.lastIndexOf(".h"));
			String upperBase = base.toUpperCase();
			//
			// Check to see if the file needs to be recompiled
			//
			String sourceFile = sourcepath + "/" + javaSource;
			String destFile;
			if (usePase) {
				destFile = "/QOpenSys" + nativeBaseDir + "/" + sourcepath + "/" + javaSource;
			} else {
				destFile = nativeBaseDir + "/" + sourcepath + "/" + javaSource;
			}
			String serverPf = upperBase;
			try {
				boolean updated = updateServerFile(sourceFile, serverPf, destFile, destFile);
				if (!updated) {
					if (debug)
						System.out.println("JDJSTP.debug: 2. Skipping compile of " + sourceFile + "-- destination file"
								+ destFile + " is newer");
					return;
				}

	      //
	      // Make sure things exist
	      // 
		  serverCommand("QSYS/CRTLIB "+library, true);
		  serverCommand("QSYS/CRTSRCPF FILE("+library+"/H) RCDLEN(192)", true);
                  serverCommand("QSYS/GRTOBJAUT OBJ("+library+"/H) OBJTYPE(*file) USER(*PUBLIC) AUT(*ALL)  ", true);

		  String command; 
	     //
	     // Delete all the files that we are going to be using.
	     // -- Shouldn't need to touach anymore .. 
	     // touch the source file to assure it is in sync with the current system.  (At most, we have one false recompile).
	     //
		  command =  "rm /QSYS.LIB/"+library+".LIB/H.FILE/"+upperBase+".mbr; " +
		    "rm "+sourcepath+"/"+base+"; ";
			       /* "touch "+sourcepath+"/"+javaSource */ 
		  serverShellCommand(command, false);


	     //
	     // Put the source in QSYS.LIB where CRTSQLCI can get to it
	     //

		  command = "grep -v sh.do "+nativeBaseDir+"/"+sourcepath+"/"+base+".h > " +
		    "  /QSYS.LIB/"+library+".LIB/H.FILE/"+upperBase+".mbr";
		  serverShellCommand(command, false);




	      } catch (Exception e) {
		  System.out.println("JDJSTP.debug: Error occured while building "+javaSource+" -- rethrowing exception");
		  markOutdated(sourceFile);
		  e.printStackTrace(System.out);
		  System.out.println("----------------------------------------------------------"); 

		  throw e; 
	      } 
	      
	  } else if ( javaSource.trim().endsWith(".c")) {
	      
	      String base = javaSource.substring(0, javaSource.lastIndexOf(".c"));
              String upperBase = base.toUpperCase();

	      //
	      // Check to see if the file needs to be recompiled
	      //
	      String sourceFile = sourcepath+"/"+javaSource;
	      String destFile   = nativeBaseDir+"/"+sourcepath+"/"+javaSource;
	      if (usePase) {
		  destFile   = "/QOpenSys/"+nativeBaseDir+"/"+sourcepath+"/"+javaSource;
	      }
	      
	      // Check if the target program exists
	      boolean programExists = true;
              if (!usePase) {
                String targetProgram = "/QSYS.LIB/" + library + ".LIB/" + upperBase + ".PGM";
                String sql = "SELECT * FROM TABLE(QSYS2.IFS_OBJECT_STATISTICS('" + targetProgram + "'))";
                ResultSet rs = cmdStatement.executeQuery(sql);
                programExists = rs.next();
                if (debug) {
                  if (!programExists) {
                    System.out.println("Program " + targetProgram + " does not exist 1418");
                  } else {
                    System.out.println("Program " + targetProgram + " exists 1420");
                  }
                }
                rs.close();
              }
	      
              String serverPf   = upperBase;
	      String command = ""; 
	      String createModCommand = null;
	      String precompileCommand = null; 
	      try { 
				boolean updated = updateServerFile(sourceFile, serverPf, destFile, destFile);
				if (!updated && programExists) {
					boolean doReturn = true;
					if (usePase) {
						String compiledFilename = "/QOpenSys/" + nativeBaseDir + "/" + sourcepath + "/" + base;
						File compiledFile = new File(compiledFilename);
						if (!compiledFile.exists()) {
							if (debug)
								System.out.println("JDJSTP.debug: 3. Compiling " + sourceFile + "-- destination file"
										+ compiledFile + " does not exist");
							doReturn = false;
						}

					}

					if (doReturn) {
						if (debug)
							System.out.println("JDJSTP.debug: 3. Skipping compile of " + sourceFile
									+ "-- destination file" + destFile + " is newer");
						return;
					}
				}

			      if (!usePase) { 
	      //
	      // Make sure things exist and authority is granted
	      //
		  serverCommand("QSYS/CRTLIB "+library, true);
		  serverCommand("QSYS/GRTOBJAUT OBJ("+library+") OBJTYPE(*LIB) USER(*PUBLIC) AUT(*ALL)  ", true); 
		  serverCommand("QSYS/CRTSRCPF FILE("+library+"/QCSRC) RCDLEN(192)", true);
		  serverCommand("QSYS/GRTOBJAUT OBJ("+library+"/QCSRC) OBJTYPE(*file) USER(*PUBLIC) AUT(*ALL)  ", true); 
		  serverCommand("QSYS/CRTSRCPF FILE("+library+"/QSQLTEMP) RCDLEN(192)", true);
		  serverCommand("QSYS/GRTOBJAUT OBJ("+library+"/QSQLTEMP) OBJTYPE(*file) USER(*PUBLIC) AUT(*ALL)  ", true); 		  

	     //
	     // Delete all the files that we are going to be using.
	     // -- Shouldn't need to touach anymore .. 
	     // touch the source file to assure it is in sync with the current system.  (At most, we have one false recompile).
	     //
		  command =  "rm /QSYS.LIB/"+library+".LIB/QCSRC.FILE/"+upperBase+".mbr; " +
		    "rm /QSYS.LIB/"+library+".LIB/QSQLTEMP.FILE/"+upperBase+".mbr; " +
		    "rm /QSYS.LIB/"+library+".LIB/"+upperBase+".MODULE; " +
		    "rm /QSYS.LIB/"+library+".LIB/"+upperBase+".PGM; " +
		    "rm "+sourcepath+"/"+base+"; ";
			       /* "touch "+sourcepath+"/"+javaSource */ 
		  serverShellCommand(command, false);


	     //
	     // Put the source in QSYS.LIB where CRTSQLCI can get to it
	     //
		  command = "grep -v sh.do "+nativeBaseDir+"/"+sourcepath+"/"+base+".c > " +
		    "  /QSYS.LIB/"+library+".LIB/QCSRC.FILE/"+upperBase+".mbr";
		  serverShellCommand(command, false);

	     //
	     // Do the SQL procompile
	     //
		  command = " export -s QIBM_MULTI_THREADED=N; system 'CRTSQLCI "+library+"/"+upperBase+" SRCFILE("+library+"/QCSRC) COMMIT(*NONE) OUTPUT(*PRINT) DLYPRP(*YES) DBGVIEW(*SOURCE) TOSRCFILE("+library+"/QSQLTEMP)' 2>&1 ";
		  serverShellCommand(command,false);
		  precompileCommand = command;



		  command = " export -s QIBM_MULTI_THREADED=N; system 'CRTCMOD "+library+"/"+upperBase+
                            " SRCFILE("+library+"/QSQLTEMP)  DBGVIEW(*ALL) SYSIFCOPT(*IFSIO)'  2>&1 ";
		  createModCommand=command;
		  serverShellCommand(command, false);

		  command = "system 'GRTOBJAUT OBJ("+library+"/"+upperBase+" ) OBJTYPE(*module ) USER(*PUBLIC) AUT(*ALL)'";
		  serverShellCommand(command, false);


	     //
	     // Create the program
	     //
		  if (useNewActivationGroup) {
		      command = "QSYS/CRTPGM "+library+"/"+upperBase+" MODULE("+library+"/"+upperBase+") ACTGRP(*NEW)";
		  } else {
		      command = "QSYS/CRTPGM "+library+"/"+upperBase+" MODULE("+library+"/"+upperBase+") ACTGRP(*CALLER)";
		  }
		  serverCommand(command, false);

		  command = "QSYS/GRTOBJAUT OBJ("+library+"/"+upperBase+") OBJTYPE(*PGM) USER(*PUBLIC) AUT(*ALL)  ";
		  serverCommand(command, false);

	     //
	     // Make the symbolic link
	     //
		  command = "ln -f -s /QSYS.LIB/"+library+".LIB/"+upperBase+".PGM "+nativeBaseDir+"/"+sourcepath+"/"+base;
		  serverShellCommand(command, false);

	     //
	     // Create the srvpgm if CLI
	     //
		  if (upperBase.startsWith("CLI")) {
		      command = "QSYS/CRTSRVPGM "+library+"/"+upperBase+" MODULE("+library+"/"+upperBase+")  EXPORT(*ALL)";
		      serverCommand(command, false);
		  }

		  command = "QSYS/GRTOBJAUT OBJ("+library+"/"+upperBase+") OBJTYPE(*ALL) USER(*PUBLIC) AUT(*USE)";
		  serverCommand(command, false);

	      } else {
		  // compile using pase --  usePase
		  if (vrm >= 740) {
		      command = "/QOpenSys/bin/sh -c \"xlc -ldb400 -DPASE -DPASE_UTF8 -o /QOpenSys/"+nativeBaseDir+"/"+sourcepath+"/"+base+" /QOpenSys/"+nativeBaseDir+"/"+sourcepath+"/"+base+".c\"";

		  } else { 
		      command = "/QOpenSys/bin/sh -c \"xlc -ldb400 -DPASE -o /QOpenSys/"+nativeBaseDir+"/"+sourcepath+"/"+base+" /QOpenSys/"+nativeBaseDir+"/"+sourcepath+"/"+base+".c\"";
		  }
		  serverShellCommand(command, false);

	      } 
             //
             // Whew!!! We're finally done -- if we made it this far without throwing an exception
             //

			} catch (Exception e) {
				System.out.println(
						"JDJSTP.debug: Error occured while building " + javaSource + " -- rethrowing exception");
				System.out.println("JDJSTP.debug: Command was " + command);
				if (createModCommand != null) {
					System.out.println("JDJSTP>debug: CreateMOD command was " + createModCommand);
				}
				if (precompileCommand != null) {
					System.out.println("JDJSTP>debug: Precompile command was " + precompileCommand);
				}
				e.printStackTrace(System.out);
				System.out.println("----------------------------------------------------------");

				markOutdated(sourceFile);
				throw e;
			}
	      
	  } else if ( javaSource.trim().endsWith(".rpg")) {
	      
	      String base = javaSource.substring(0, javaSource.lastIndexOf(".rpg"));
              String upperBase = base.toUpperCase();

	      String sourceFile = sourcepath+"/"+javaSource;
	      String destFile   = nativeBaseDir+"/"+sourcepath+"/"+javaSource;
              String serverPf   = upperBase;
	      String command = ""; 
	      String createModCommand = null;
	      try { 

		  boolean updated = updateServerFile(sourceFile, serverPf, destFile, destFile);

	      //
	      // If destination program does not exist
	      // then we need to recompile
	      //
		  if (serverFileExists("/QSYS.LIB/"+library+".LIB/"+upperBase+".PGM")) {
	      //
	      // Check to see if the file needs to be recompiled -- only if it exists 
	      //
		      if (! updated) {
			  if (debug) System.out.println("JDJSTP.debug: 4. Skipping compile of "+sourceFile+"-- destination file"+ destFile+" is newer");
			  return;
		      }

		  }
	      //
              // Make sure things exist
              // 
	      serverCommand("QSYS/CRTLIB "+library, true);
	      serverCommand("QSYS/GRTOBJAUT OBJ("+library+") OBJTYPE(*LIB) USER(*PUBLIC) AUT(*ALL)  ", true); 


	      command =  " system \"QSYS/CRTBNDRPG PGM("+library+"/"+upperBase+") SRCSTMF('"+destFile+"')\"";
	      command =  " system \"QSYS/CRTSQLRPGI OBJ("+library+"/"+upperBase+") SRCSTMF('"+destFile+"')\"";
	      createModCommand=command;
	      serverShellCommand(command, false);

	      command = "QSYS/GRTOBJAUT OBJ("+library+"/"+upperBase+") OBJTYPE(*PGM) USER(*PUBLIC) AUT(*ALL)  ";
	      serverShellCommand(command, false);

             //
             // Make the symbolic link
             //
             command = "ln -f -s /QSYS.LIB/"+library+".LIB/"+upperBase+".PGM "+nativeBaseDir+"/"+sourcepath+"/"+base;
	     serverShellCommand(command, false);

             //
             // Whew!!! We're finally done -- if we made it this far without throwing an exception
             //

			} catch (Exception e) {
				System.out.println(
						"JDJSTP.debug: Error occured while building " + javaSource + " -- rethrowing exception");
				System.out.println("JDJSTP.debug: Command was " + command);
				if (createModCommand != null) {
					System.out.println("JDJSTP>debug: CreateMOD command was " + createModCommand);
				}
				e.printStackTrace(System.out);
				System.out.println("----------------------------------------------------------");

				markOutdated(sourceFile);
				throw e;
			}
	      
	  } else if (javaSource.trim().endsWith(".sh")) {

	      String base = javaSource.substring(0, javaSource.lastIndexOf(".sh"));
              String upperBase = base.toUpperCase();

	      //
	      // load the file on the system 
	      //
	      String sourceFile = sourcepath+"/"+javaSource;
	      String destFile   = nativeBaseDir+"/"+sourcepath+"/"+javaSource;
              String serverPf   = upperBase;
	      try { 
		  boolean updated = updateServerFile(sourceFile, serverPf, destFile, destFile);
		  if (! updated) {
		      if (debug) System.out.println("JDJSTP.debug: 5. Skipping compile of "+sourceFile+"-- destination file"+ destFile+" is newer");
		      return;
		  }
	      } catch (Exception e) {
		  System.out.println("JDJSTP.debug: Error occured while building "+javaSource+" -- rethrowing exception");
		  markOutdated(sourceFile);
		  e.printStackTrace(System.out);
		  System.out.println("----------------------------------------------------------"); 

		  throw e; 
	      } 


		} else {
			throw new Exception("file passed to link is not valid because it does not end with .c or .java or .sh ");
		}
	} /* not java */
} /* link */

     //
     // Create a service program on the system.
     // 
		public static void crtsrvpgm(String library1, String srvpgm, String source) throws Exception {

			// transfer the file up
			String sourceFile = sourcepath + "/" + source;
			String destFile = nativeBaseDir + "/" + sourcepath + "/" + source;
			String serverPf = genPfName(source);

			boolean updated = updateServerFile(sourceFile, serverPf, destFile, destFile);
			if (!updated) {
				if (debug)
					System.out.println("JDJSTP.debug: 6. Skipping compile of " + sourceFile + "-- destination file "
							+ destFile + " is newer");
				return;
			}

			// compile
			serverCommand("QSYS/CRTCMOD MODULE(" + library1 + "/" + srvpgm + ") SRCSTMF('" + destFile + "') ", false);
			serverCommand("QSYS/GRTOBJAUT OBJ(" + library1 + "/" + srvpgm + " ) OBJTYPE(*module ) USER(*PUBLIC) AUT(*ALL)",
					false);

			// build srvpgm
			serverCommand("QSYS/CRTSRVPGM SRVPGM(" + library1 + "/" + srvpgm + ") EXPORT(*ALL)", false);
			serverCommand("QSYS/GRTOBJAUT OBJ(" + library1 + "/" + srvpgm + ") OBJTYPE(*SRVPGM) USER(*PUBLIC) AUT(*ALL)  ",
					false);
		}

     //
     // Create a service program on the system.
     // 
		public static void crtsrvpgmCPP(String library1, String srvpgm, String source) throws Exception {

			// transfer the file up
			String sourceFile = sourcepath + "/" + source;
			String destFile = nativeBaseDir + "/" + sourcepath + "/" + source;
			String serverPf = genPfName(source);

			boolean updated = updateServerFile(sourceFile, serverPf, destFile, destFile);
			if (!updated) {
				if (debug)
					System.out.println("JDJSTP.debug: 7. Skipping compile of " + sourceFile + "-- destination file "
							+ destFile + " is newer");
				return;
			}

			// compile
			serverCommand("QSYS/CRTCPPMOD MODULE(" + library1 + "/" + srvpgm + ") SRCSTMF('" + destFile + "') ", false);
			serverCommand("QSYS/GRTOBJAUT OBJ(" + library1 + "/" + srvpgm + " ) OBJTYPE(*module ) USER(*PUBLIC) AUT(*ALL)",
					false);
			// build srvpgm
			serverCommand("QSYS/CRTSRVPGM SRVPGM(" + library1 + "/" + srvpgm + ") EXPORT(*ALL)", false);
			serverCommand("QSYS/GRTOBJAUT OBJ(" + library1 + "/" + srvpgm + ") OBJTYPE(*SRVPGM) USER(*PUBLIC) AUT(*ALL)  ",
					false);

		}

		public static void touchSource(String javaSource) throws Exception {
			File sourceFile = new File(sourcepath + "/" + javaSource);
			sourceFile.setLastModified(System.currentTimeMillis());
		}

     public static void linkClient(String javaSource, String javacOptions) throws Exception {

        Process process;

	if (debug) {
	    System.out.println("Calling linkClient("+javaSource+","+javacOptions+")"); 
	} 
	//
	// Check for java class
	//
	    if ( javaSource.trim().endsWith(".java") || javaSource.trim().endsWith(".sqlj") ) {
		String classFilename; 
		if ( javaSource.trim().endsWith(".sqlj")) {
		    classFilename = javaSource.substring(0, javaSource.lastIndexOf(".sqlj")) + ".class";
		} else {
		    classFilename = javaSource.substring(0, javaSource.lastIndexOf(".java")) + ".class";
		}

		javaRunPath = sourcepath+"/"+library;
		File destDir = new File(javaRunPath);
		if (!destDir.exists()) {
		    destDir.mkdir(); 
		} 
	        //
	        // Check to see if the file needs to be recompiled
	        //
		File sourceFile = new File( sourcepath+"/"+javaSource);
		File newSourceFile = new File (javaRunPath+"/"+javaSource); 
		File destFile   = new File( javaRunPath+"/"+classFilename);
		if (destFile.exists()) {
		    if (destFile.lastModified() > sourceFile.lastModified()) {
			if (debug) System.out.println("JDJSTP.debug: 8. Skipping compile of "+sourceFile+"-- destination file"+ destFile+" is newer");
			return;
		    }
		}

		// Copy the source file to the destination directory
		InputStream in = new FileInputStream(sourceFile);
		OutputStream out = new FileOutputStream(newSourceFile);
		byte []buffer = new byte[4096];
		int l;
		l = in.read(buffer); 
		while (l > 0) {
		    out.write(buffer, 0, l); 
		    l = in.read(buffer); 
		} 
		in.close();
		out.close(); 

		//
                // Make sure that the date of the source file is less than the current date. 
                //
		long currentTime = System.currentTimeMillis(); 

		if ( sourceFile.lastModified() > currentTime) {
		    sourceFile.setLastModified(currentTime); 
		} 

		String classpath = ".";
		    if ( javaSource.trim().endsWith(".sqlj")) {

			String command;
			if (JVMInfo.getJDK() >= JVMInfo.JDK_19) {
                          command = "export -s CLASSPATH='.:/QIBM/ProdData/OS400/Java400/ext/runtime.zip:/QIBM/ProdData/OS400/Java400/ext/translator.zip:/QIBM/ProdData/OS400/Java400/ext/db2routines_classes.jar:/QIBM/ProdData/OS400/Java400/ext/db2_classes11.jar';"+
                          "cd "+javaRunPath+"; sqlj "+javaSource; 
			  
			} else {
			  command = "export -s CLASSPATH='.:/QIBM/ProdData/OS400/Java400/ext/runtime.zip:/QIBM/ProdData/OS400/Java400/ext/translator.zip:/QIBM/ProdData/OS400/Java400/ext/db2routines_classes.jar';"+
			  "cd "+javaRunPath+"; sqlj "+javaSource; 
			}

			process = exec(command );
			showProcessOutput(process, null, JDJSTPOutputThread.ENCODING_UNKNOWN);
			process.waitFor();
			javaSource = javaSource.substring(0, javaSource.lastIndexOf(".sqlj")) + ".java"; 
			classpath=".:/QIBM/ProdData/OS400/Java400/ext/runtime.zip:/QIBM/ProdData/OS400/Java400/ext/translator.zip";
		    } else {
			if (javaSource.indexOf("ConnectionManager")> 0) {
			    classpath=".:/QIBM/ProdData/OS400/Java400/ext/runtime.zip:/QIBM/ProdData/OS400/Java400/ext/translator.zip";	    
			}
		    }

		    // Add native JDBC driver for compiles
		    if (JVMInfo.getJDK() >= JVMInfo.JDK_V11) {
			classpath=classpath+":/qibm/proddata/os400/java400/ext/db2routines_classes.jar:/qibm/proddata/os400/java400/ext/db2_classes11.jar:/qibm/proddata/os400/java400/ext/runtime.zip:/qibm/proddata/os400/java400/ext/translator.zip"; 
		    }
		    String userDir = System.getProperty("user.dir" );
		    String command;

		    command = "cd "+javaRunPath+"; javac -classpath '"+userDir+
		      "/"+javaRunPath+":"+classpath+
		      "' "+javacOptions+" "+javaSource;


		    process = exec(command );
		    showProcessOutput(process, "/tmp/JDJSTP.javac.out", JDJSTPOutputThread.ENCODING_UNKNOWN);
		    process.waitFor();

		    if (debug) System.out.println("JSTPTest.debug: Checking for "+ javaRunPath+"/"+classFilename);
		    checkExists(javaRunPath+"/"+classFilename);

	    } else {
			if (javaSource.trim().endsWith(".c") || javaSource.trim().endsWith(".h")) {

				// Check to see if running in CYGWIN

				//
				// Copy to location
				//
				if (JTOpenTestEnvironment.isWindows) {
					javaRunPath = JTOpenTestEnvironment.cygwinBase + sep + nativeBaseDir.replace('/', '\\') + sep + sourcepath;
				} else if (usePase) {
					javaRunPath = "/QOpenSys" + nativeBaseDir + "/" + sourcepath;
				} else {
					javaRunPath = nativeBaseDir + "/" + sourcepath;
				}
				String shellRunPath = nativeBaseDir + "/" + sourcepath;
				if (usePase) {
					shellRunPath = "/QOpenSys" + shellRunPath;
				}
				File destDir = new File(javaRunPath);
				if (!destDir.exists()) {

					boolean created = destDir.mkdirs();
					if (!created) {
						throw new Exception("Directories " + destDir + " " + javaRunPath + " not created ");
					}
				}
				String classFilename;
				if ( javaSource.trim().endsWith(".c")) {
                    classFilename = javaSource.substring(0, javaSource.lastIndexOf(".c")) ;
                } else {
                    classFilename = javaSource;
                }
                
                
                //
                // Check to see if the file needs to be recompiled
                // 
		String destinationPath; 
                String base ;
                String upperBase ;

		if (javaSource.trim().endsWith(".c")) {
		  base = javaSource.substring(0, javaSource.lastIndexOf(".c"));
	          upperBase = base.toUpperCase();
                  destinationPath =  "/QSYS.LIB/"+library+".LIB/"+upperBase+".PGM";
		} else {
		  base = javaSource.substring(0, javaSource.lastIndexOf(".h"));
		  upperBase = base.toUpperCase();
		  destinationPath =  "/QSYS.LIB/"+library+".LIB/H.FILE/"+upperBase+".mbr";
		}

                Timestamp destTimestamp = null ;
                String sql = "SELECT MAX(OBJECT_CHANGE_TIMESTAMP, CREATE_TIMESTAMP, DATA_CHANGE_TIMESTAMP)  FROM TABLE(QSYS2.IFS_OBJECT_STATISTICS('"
                    + destinationPath + "'))";
                ResultSet rs = cmdStatement.executeQuery(sql);
                boolean programExists = rs.next();
                if (programExists) {
                  destTimestamp = rs.getTimestamp(1);
                }
                if (destTimestamp == null)
                  destTimestamp = new Timestamp(0);
                if (debug) {
                  if (!programExists) {
                    System.out.println("Destination" + destinationPath + " does not exist");
                  } else {
                    System.out.println("Destination" + destinationPath + " exists");
                  }
                }
                rs.close();
		
                File sourceFile = new File( sourcepath+"/"+javaSource);
                File newSourceFile = new File (javaRunPath+"/"+javaSource); 
                File destFile   = new File( javaRunPath+"/"+classFilename);
                if (destFile.exists()) {
                    if ((destFile.lastModified() > sourceFile.lastModified()) &&
                        (destTimestamp.getTime() > sourceFile.lastModified())) {
                        if (debug) System.out.println("JDJSTP.debug: 9. Skipping compile of "+sourceFile+"-- destination file"+ destFile+" is newer");
                        return;
                    }
                }

                
                // Copy the source file to the destination directory
                InputStream in = new FileInputStream(sourceFile);
                OutputStream out = new FileOutputStream(newSourceFile);
                byte []buffer = new byte[4096];
                int l;
                l = in.read(buffer); 
                while (l > 0) {
                    out.write(buffer, 0, l); 
                    l = in.read(buffer); 
                } 
                in.close();
                out.close(); 
                if (debug) {
                  System.out.println("JDJSTP.debug:  Copied from " + sourceFile + " to " + newSourceFile);
                }

                if (javaSource.trim().endsWith(".c")) {
 
                  if (JTOpenTestEnvironment.isWindows) {
                    // Compile if CYGWIN
                    System.out.println("compiling on windows");
                    
                    String command = "cd " + shellRunPath + ";cc  -v -Wl,-trace -DWINDOWS -I. "
								+ "-I/c/Program\\ Files/IBM/SQLLIB/include  "
								+ "-ldb2cli -ldb2api -L/c/ProgramFiles/IBM/SQLLIB/lib " + "    " + javaSource + " -o "
								+ classFilename + " /c/ProgramFiles/IBM/SQLLIB/lib/db2cli.lib";
                    
                    process = exec(command);
                    showProcessOutput(process, "/tmp/JDJSTP.cc.out", JDJSTPOutputThread.ENCODING_UNKNOWN);
                    process.waitFor();

                  } else if (usePase) {
                    String command = "/QOpenSys/bin/sh -c \"xlc -ldb400 -DPASE -o /QOpenSys/" + nativeBaseDir + "/"
                        + sourcepath + "/" + base + " /QOpenSys/" + nativeBaseDir + "/" + sourcepath + "/"
                        + base + ".c\"";
                    process = exec(command);
                    showProcessOutput(process, "/tmp/JDJSTP.cc.out", JDJSTPOutputThread.ENCODING_UNKNOWN);
                    process.waitFor();

                  } else {

			// Setup / cleanup before the compile
			String command = "system CRTLIB "+library+";"+
			  "system 'GRTOBJAUT OBJ("+library+") OBJTYPE(*LIB) USER(*PUBLIC) AUT(*ALL)';"+

			  " system 'CRTSRCPF FILE("+library+"/QCSRC) RCDLEN(192)';"+
			  " system 'GRTOBJAUT OBJ("+library+"/QCSRC) OBJTYPE(*file) USER(*PUBLIC) AUT(*ALL)';"+ 
			  " system 'CRTSRCPF FILE("+library+"/QSQLTEMP) RCDLEN(192)';"+
			  " system 'GRTOBJAUT OBJ("+library+"/QSQLTEMP) OBJTYPE(*file) USER(*PUBLIC) AUT(*ALL)';"+ 
			  "rm /QSYS.LIB/"+library+".LIB/QCSRC.FILE/"+upperBase+".mbr; " +
			  "rm /QSYS.LIB/"+library+".LIB/QSQLTEMP.FILE/"+upperBase+".mbr; " +
			  "rm /QSYS.LIB/"+library+".LIB/"+upperBase+".MODULE; " +
			  "rm /QSYS.LIB/"+library+".LIB/"+upperBase+".PGM; " +
			  "rm "+sourcepath+"/"+base+"; ";
			       /* "touch "+sourcepath+"/"+javaSource */ 

			process = exec(command );
			showProcessOutput(process, "/tmp/JDJSTP.setup.out", JDJSTPOutputThread.ENCODING_UNKNOWN);
			process.waitFor();


			command = "grep -v sh.do "+nativeBaseDir+"/"+sourcepath+"/"+base+".c > " +
			  "  /QSYS.LIB/"+library+".LIB/QCSRC.FILE/"+upperBase+".mbr";


			process = exec(command );
			showProcessOutput(process, "/tmp/JDJSTP.intoqsys.out", JDJSTPOutputThread.ENCODING_UNKNOWN);
			process.waitFor();


			command = " export -s QIBM_MULTI_THREADED=N; system 'CRTSQLCI "+library+"/"+upperBase+" SRCFILE("+library+"/QCSRC) COMMIT(*NONE) OUTPUT(*PRINT) DLYPRP(*YES) DBGVIEW(*SOURCE) TOSRCFILE("+library+"/QSQLTEMP)' 2>&1 ";
			process = exec(command );
			showProcessOutput(process, "/tmp/JDJSTP.precompile.out", JDJSTPOutputThread.ENCODING_UNKNOWN);
			process.waitFor();


			command = " export -s QIBM_MULTI_THREADED=N; system 'CRTCMOD "+library+"/"+upperBase+
			  " SRCFILE("+library+"/QSQLTEMP)  DBGVIEW(*ALL) SYSIFCOPT(*IFSIO)'  2>&1 ";

			process = exec(command );
			showProcessOutput(process, "/tmp/JDJSTP.crtcmod.out", JDJSTPOutputThread.ENCODING_UNKNOWN);
			process.waitFor();


			command = "system 'GRTOBJAUT OBJ(" +library+"/"+upperBase+" ) OBJTYPE(*module ) USER(*PUBLIC) AUT(*ALL)'";

			process = exec(command );
			showProcessOutput(process, "/tmp/JDJSTP.crtcmod.out", JDJSTPOutputThread.ENCODING_UNKNOWN);
			process.waitFor();


			if (useNewActivationGroup) {
			    command = "system 'CRTPGM "+library+"/"+upperBase+" MODULE("+library+"/"+upperBase+") ACTGRP(*NEW)'";
			} else {
			    command = "system 'CRTPGM "+library+"/"+upperBase+" MODULE("+library+"/"+upperBase+") ACTGRP(*CALLER)'";
			}

			process = exec(command );
			showProcessOutput(process, "/tmp/JDJSTP.crtpgm.out", JDJSTPOutputThread.ENCODING_UNKNOWN);
			process.waitFor();

			command="system 'GRTOBJAUT OBJ("+   library+"/"+upperBase+") OBJTYPE(*PGM) USER(*PUBLIC) AUT(*ALL)' "; 
			process = exec(command );
			showProcessOutput(process, "/tmp/JDJSTP.crtpgm2.out", JDJSTPOutputThread.ENCODING_UNKNOWN);
			process.waitFor();


			command = "ln -f -s /QSYS.LIB/"+library+".LIB/"+upperBase+".PGM "+nativeBaseDir+"/"+sourcepath+"/"+base;
			process = exec(command );
			showProcessOutput(process, "/tmp/JDJSTP.crtpgm.out", JDJSTPOutputThread.ENCODING_UNKNOWN);
			process.waitFor();

			if (upperBase.startsWith("CLI")) {
				command = "system 'CRTSRVPGM " + library + "/" + upperBase + " MODULE(" + library + "/" + upperBase
						+ ")  EXPORT(*ALL)'";
				process = exec(command);
				showProcessOutput(process, "/tmp/JDJSTP.crtsrvpgm.out", JDJSTPOutputThread.ENCODING_UNKNOWN);
				process.waitFor();

				command = "system 'GRTOBJAUT OBJ(" + library + "/" + upperBase
						+ ") OBJTYPE(*SRVPGM) USER(*PUBLIC) AUT(*ALL)' ";
				process = exec(command);
				showProcessOutput(process, "/tmp/JDJSTP.crtsrvpgm.out", JDJSTPOutputThread.ENCODING_UNKNOWN);
				process.waitFor();

			}

		}
                  
		} else {
		    // Must end with .h
		    // Make sure the srcpf exst

		   String command = " system 'CRTLIB "+library+"'";
		   process = exec(command );
		   showProcessOutput(process, "/tmp/JDJSTP.crtlib.out", JDJSTPOutputThread.ENCODING_UNKNOWN);
		   process.waitFor();


		   command = "system 'GRTOBJAUT OBJ("+library+") OBJTYPE(*LIB) USER(*PUBLIC) AUT(*ALL)'"; 
		   process = exec(command );
		   showProcessOutput(process, "/tmp/JDJSTP.crtlib2.out", JDJSTPOutputThread.ENCODING_UNKNOWN);
		   process.waitFor();


		   command = " system 'CRTSRCPF FILE("+library+"/H) RCDLEN(192)'"; 
		   process = exec(command );
		   showProcessOutput(process, "/tmp/JDJSTP.crtsrcpf out", JDJSTPOutputThread.ENCODING_UNKNOWN);
		   process.waitFor();


		   command = "system 'GRTOBJAUT OBJ("+library+"/H) OBJTYPE(*file) USER(*PUBLIC) AUT(*ALL)'";
		   process = exec(command );
		   showProcessOutput(process, "/tmp/JDJSTP.crtsrcpf out", JDJSTPOutputThread.ENCODING_UNKNOWN);
		   process.waitFor();


		   command = "grep -v sh.do "+nativeBaseDir+"/"+sourcepath+"/"+base+".h > " +
		     "  /QSYS.LIB/"+library+".LIB/H.FILE/"+upperBase+".mbr";

		   process = exec(command );
		   showProcessOutput(process, "/tmp/JDJSTP.copyheadfile out", JDJSTPOutputThread.ENCODING_UNKNOWN);
		   process.waitFor();

		}

	} else {
		throw new Exception(
				"file passed to link (" + javaSource + ") is not valid because it does not end with .c or .java");
	}
} /* not java */

} /* link */

     /**
      * linkSqlj compiles sqlj to a java file name 
      *
      * @param sqljSource The filename of the java source to compile.  The file should reside in the
      * sourcepath.
      * @exception java.lang.Exception If an error occurs, an exception is thrown describing the error.
      * @see #setSourcepath
      **/
     public static void linkSqlj(String sqljSource) throws Exception {


	  //
	  // Check to see if the file needs to be recompiled
	  //
	 String base = sqljSource.substring(0, sqljSource.lastIndexOf(".sqlj"));
	 String sourceFile = sourcepath+"/"+sqljSource;
	 String serverFile = nativeBaseDir+"/"+base+".sqlj"; 
	 String destFile   = nativeBaseDir+"/"+base+".class"; 
	 String javaFile   = nativeBaseDir+"/"+base+".java"; 

	 String destClasses  = nativeBaseDir+"/"+ base + "_*.class";
	 String destSer      = nativeBaseDir+"/"+ base + "_*.ser";
	 String serverPf   = genPfName(sqljSource);

	 boolean updated = updateServerFile(sourceFile, serverPf, serverFile,destFile);
	 if (! updated) {
	     if (debug) System.out.println("JDJSTP.debug: 10. Skipping compile of "+sourceFile+"-- destination file"+ serverFile+" is newer");
	     return;
	 }
	 try { 
	     serverShellCommand("rm "+javaFile, true); 
	     serverShellCommand("rm "+destSer, true);

	     String command;
	     if ( vrm >= 510 ) {
		 command = "cd "+nativeBaseDir+"; export -s CLASSPATH=\"$CLASSPATH\"\":/QIBM/ProdData/OS400/Java400/ext/runtime.zip:/QIBM/ProdData/OS400/Java400/ext/translator.zip:/QIBM/ProdData/OS400/Java400/ext/db2routines_classes.jar\";  java  sqlj.tools.Sqlj  "  + sqljSource;
	     } else {
               if (JVMInfo.getJDK() >= JVMInfo.JDK_V11) {
                 command = "export -s CLASSPATH=\"$CLASSPATH\"\":/QIBM/ProdData/OS400/Java400/ext/db2routines_classes.jar:/QIBM/ProdData/OS400/Java400/ext/db2_classes11.jar:/QIBM/ProdData/Java400/ext/runtime.zip:/QIBM/ProdData/Java400/ext/translator.zip:/QIBM/ProdData/Java400/ext/db2routines_classes.jar\";  java  sqlj.tools.Sqlj  "  + sourcepath+"/"+sqljSource;
               } else { 
	       
		 command = "export -s CLASSPATH=\"$CLASSPATH\"\":/QIBM/ProdData/Java400/ext/runtime.zip:/QIBM/ProdData/Java400/ext/translator.zip:/QIBM/ProdData/Java400/ext/db2routines_classes.jar\";  java  sqlj.tools.Sqlj  "  + sourcepath+"/"+sqljSource;
               }
             }
	     if (debug) System.out.println("JDJSTP.debug: Excuting \""+command+"\"");
	     serverShellCommand(command, false);

	     serverShellCommand("rm "+destFile, true);
	     serverShellCommand("rm "+destClasses, true);
             //
             // Workaround
             // 
	     serverShellCommand("cd "+nativeBaseDir+"; mv "+javaFile+".tmp "+javaFile, true);
             //
             // End workaround
             // 
             if (JVMInfo.getJDK() >= JVMInfo.JDK_V11) {
               serverShellCommand("cd "+nativeBaseDir+"; export -s CLASSPATH=\"$CLASSPATH\"\":/QIBM/ProdData/OS400/Java400/ext/db2routines_classes.jar:/QIBM/ProdData/OS400/Java400/ext/db2_classes11.jar:/QIBM/ProdData/OS400/Java400/ext/runtime.zip:/QIBM/ProdData/OS400/Java400/ext/translator.zip:/QIBM/ProdData/OS400/Java400/ext/db2routines_classes.jar\";"+
                   "  javac  "+  javaFile+" 2>&1", false);
             } else { 	     
               serverShellCommand("cd "+nativeBaseDir+"; export -s CLASSPATH=\"$CLASSPATH\"\":/QIBM/ProdData/OS400/Java400/ext/runtime.zip:/QIBM/ProdData/OS400/Java400/ext/translator.zip:/QIBM/ProdData/OS400/Java400/ext/db2routines_classes.jar\";"+
                 "  javac  "+  javaFile+" 2>&1", false);
             }  
	     serverShellCommand("cd "+nativeBaseDir+"; chmod a+rx "+destFile, false);
	     serverShellCommand("cd "+nativeBaseDir+"; chmod a+rx "+destClasses, false);
	     serverCommand("QSYS/CPY OBJ('"+destFile+"') TODIR('"+funcpath+"')  REPLACE(*YES) ", false);
	     serverCommand("QSYS/CPY OBJ('"+destClasses+"') TODIR('"+funcpath+"')  REPLACE(*YES) ", false);
             System.out.println("Copying from "+destSer); 
	     serverCommand("QSYS/CPY OBJ('"+destSer+"') TODIR('"+funcpath+"')  REPLACE(*YES) ", false);

	     //
             // No exceptions taken, we're done
             // 

	 } catch (Exception e) {
	     System.out.println("JDJSTP.debug: Error occured while building "+sqljSource+" -- rethrowing exception");
	     markOutdated(sourceFile);
	     e.printStackTrace(System.out);
	     System.out.println("----------------------------------------------------------"); 

	     throw e; 
	 } 

  
     }




    /**
     * exec execute a shell command using runtime.exec
     * Note:  This command is run on the client!!! 
     *
     */
     public static Process exec(String command) throws java.io.IOException {
	 Runtime runtime;
	 Process process;
	 runtime = Runtime.getRuntime();


         // 
         // On V5R1 the following doesn't work
         // 
	 if ( !v7r1 && jdk13 && nativeClient ) {
             //
	     // On V7R1 using QSH causes translation problems with the
             // output data when running with java 
	     //
	     // On V7R4, the PASE runtime is now UTF-8.
             // This is supposed to be disabled bye setting
             // PASE_DEFAULT_UTF8=N.  I tried setting it several ways
             // and the only way it seems to work is if it set in the
             // outer most shell before calling test.JDRunit
	     // Could not figure out why that was the case.
	     // Had to change the PASE programs to run with UTF-8 instead of 819.
	     // When the release is 7.4. 
	     String[] commands = new String[4];
	     commands[0] = "/usr/bin/qsh";
	     commands[1] = "-t"; 
	     commands[2] = "-c";
	     commands[3] = command;
	     if (debug) {	System.out.println("JDJSTP.debug: c[0]="+commands[0]+
					    " c[1]="+commands[1]+
					    " c[2]="+commands[2]+
					    " c[3]="+commands[3]);
	     }
	     process = runtime.exec(commands); 
	 } else {



	     if (JTOpenTestEnvironment.isAIX ) {
		 String[] commands = new String[4];
		 commands[0] = "/bin/sh";
		 commands[1] = "-t"; 
		 commands[2] = "-c";
		 commands[3] = command; 
		 if (debug)  System.out.println("JDJSTP.debug: 2. c[0]="+commands[0]+
						" c[1]="+commands[1]+
						" c[2]="+commands[2]+
						" c[3]="+commands[3]);
		 process = runtime.exec(commands); 
             } else if (JTOpenTestEnvironment.isWindows ) {
                 if (debug) System.out.println("JDJSTP.debug: running on Windows");
		 // System.out.println("user.dir is " + System.getProperty("user.dir"));
		 // System.out.println("env PATH is "+System.getenv("PATH")); 
                 String[] commands = new String[4];

                 commands[0] = "sh";
                 commands[1] = "-t"; 
                 commands[2] = "-c";
                 commands[3] = command;

		 if (debug) {
		     System.out.println("commands[0]="+commands[0]);
		     System.out.println("commands[1]="+commands[1]);
		     System.out.println("commands[2]="+commands[2]);
		     System.out.println("commands[3]="+commands[3]);
		 }
                 process = runtime.exec(commands); 

             } else if (JTOpenTestEnvironment.isLinux ) {
                 if (debug) System.out.println("JDJSTP.debug: running on Linux");
		 // System.out.println("user.dir is " + System.getProperty("user.dir"));
		 // System.out.println("env PATH is "+System.getenv("PATH")); 
                 String[] commands = new String[3];

                 commands[0] = "sh";
                 commands[1] = "-c";
                 commands[2] = command;

		 if (debug) {
		     System.out.println("commands[0]="+commands[0]);
		     System.out.println("commands[1]="+commands[1]);
		     System.out.println("commands[2]="+commands[2]);
		 }
                 process = runtime.exec(commands); 

	     } else 	 if ( ( v7r1 || v7r2 || v7r3 || v7r4 || v7r5 || v7r6 || v7r6plus) && nativeClient ) {
		 String[] commands = new String[4];
		 if (command.indexOf("java ") >= 0 && (command.indexOf(';') < 0 )) {
		     /* If java, just run directly */ 
		     if (debug) {	System.out.println("JDJSTP.debug: v7r1 exec("+command+")"); }
		     process = runtime.exec(command); 
		 } else {
		     commands[0] = "/usr/bin/qsh";
		     commands[1] = "-t"; 
		     commands[2] = "-c";
		     commands[3] = command;
		     if (debug) {	System.out.println("JDJSTP.debug: 3. c[0]="+commands[0]+
							   " c[1]="+commands[1]+
							   " c[2]="+commands[2]+
							   " c[3]="+commands[3]);
		     }
		     process = runtime.exec(commands); 

		 } 

	     }  else {
		 if (debug)  System.out.println("JDJSTP.debug: exec("+command+")"); 

		 process = runtime.exec(command);
	     }
	 }
	 return process; 
     }



     public static char EtoChar[] = {
(char)0x00,(char)0x01,(char)0x02,(char)0x03,       ' ',      '\t',       ' ',(char)0x7f, /* 00-07 */
       ' ',       ' ',       ' ',(char)0x0b,      '\f',      '\r',(char)0x0e,(char)0x0f, /* 08-0f */
(char)0x10,(char)0x11,(char)0x12,(char)0x13,       ' ',      '\n',      '\b',       ' ', /* 10-17 */
(char)0x18,(char)0x19,       ' ',       ' ',       ' ',(char)0x1d,(char)0x1e,(char)0x1f, /* 18-1f */
       ' ',       ' ',(char)0x1c,       ' ',       ' ',      '\n',(char)0x17,(char)0x1b, /* 20-27 */
       ' ',       ' ',       ' ',       ' ',       ' ',(char)0x05,(char)0x06,(char)0x07, /* 28-2f */
       ' ',       ' ',(char)0x16,       ' ',       ' ',       ' ',       ' ',(char)0x04, /* 30-37 */
       ' ',       ' ',       ' ',       ' ',(char)0x14,(char)0x15,       ' ',(char)0x1a, /* 38-3f */
       ' ',       ' ',(char)0x83,(char)0x84,(char)0x85,(char)0xa0,(char)0xc6,(char)0x86, /* 40-47 */
(char)0x87,(char)0xa4,(char)0xbd,       '.',(char)0x3c,       '(',       '+',(char)0x7c, /* 48-4f */
       '&',(char)0x82,(char)0x88,(char)0x89,(char)0x8a,(char)0xa1,(char)0x8c,(char)0x8b, /* 50-57 */
(char)0x8d,(char)0xe1,       '!',       '$',       '*',       ')',       ';',(char)0xaa, /* 58-5f */
       '-',       '/',(char)0xb6,(char)0x8e,(char)0xb7,(char)0xb5,(char)0xc7,(char)0x8f, /* 60-67 */
(char)0x80,(char)0xa5,(char)0xdd,       ',',       '%',       '_',       '>',       '?', /* 68-6f */
(char)0x9b,(char)0x90,(char)0xd2,(char)0xd3,(char)0xd4,(char)0xd6,(char)0xd7,(char)0xd8, /* 70-77 */
(char)0xde,       '`',       ':',       '#',       '@',      '\'',       '=',       '"', /* 78-7f */
(char)0x9d,       'a',       'b',       'c',       'd',       'e',       'f',       'g', /* 80-87 */
       'h',       'i',(char)0xae,(char)0xaf,(char)0xd0,(char)0xec,(char)0xe7,(char)0xf1, /* 88-8f */
(char)0xf8,       'j',       'k',       'l',       'm',       'n',       'o',       'p', /* 90-97 */
       'q',       'r',(char)0xa6,(char)0xa7,(char)0x91,(char)0xf7,(char)0x92,(char)0xcf, /* 98-9f */
(char)0xe6,       '~',       's',       't',       'u',       'v',       'w',       'x', /* a8-a7 */
       'y',       'z',(char)0xad,(char)0xa8,(char)0xd1,(char)0xed,(char)0xe8,(char)0xa9, /* a8-af */
       '^',(char)0x9c,(char)0xbe,(char)0xfa,(char)0xb8,(char)0x15,(char)0x14,(char)0xac, /* b0-b7 */
(char)0xab,(char)0xf3,(char)0x5b,(char)0x5d,(char)0xee,(char)0xf9,(char)0xef,(char)0x9e, /* b8-bf */
(char)0x7b,       'A',       'B',       'C',       'D',       'E',       'F',       'G', /* c0-c7 */
       'H',       'I',(char)0xf0,(char)0x93,(char)0x94,(char)0x95,(char)0xa2,(char)0xe4, /* c8-cf */
(char)0x7d,       'J',       'K',       'L',       'M',       'N',       'O',       'P', /* d0-d7 */
       'Q',       'R',(char)0xfb,(char)0x96,(char)0x81,(char)0x97,(char)0xa3,(char)0x98, /* d8-df */
      '\\',(char)0xf6,       'S',       'T',       'U',       'V',       'W',       'X', /* e0-e7 */
       'Y',       'Z',(char)0xfc,(char)0xe2,(char)0x99,(char)0xe3,(char)0xe0,(char)0xe5, /* e8-ef */
       '0',       '1',       '2',       '3',       '4',       '5',       '6',       '7', /* f0-f7 */
       '8',       '9',(char)0xfd,(char)0xea,(char)0x9a,(char)0xeb,(char)0xe9,(char)0xff  /* f8-ff */
     };

    /**
     *  Save the output of process to a file 
     *  @param p The process from which to display the output
     *  @param outfile The name of the file to place the output 
     **/
     public static void showProcessOutput(Process p, String outfile, int encoding) throws Exception {
	 showProcessOutput(p,outfile, false, encoding); 
     }
     public static void showProcessOutput(Process p, String outfile, boolean toStdout, int encoding ) throws Exception {
         PrintWriter writer = null; 
	 try {
	     
	     if (outfile != null) {
		 if (outfile.equals("stdout") ) {
		     if (sep != '/') {
			 outfile = outfile.replace('/', sep);
			 if (outfile.charAt(0)==sep) {
			     outfile="C:"+outfile; 
			 }
		     } 
		     writer = new PrintWriter(System.out); 

		 } else { 
		     if (sep != '/') {
			 outfile = outfile.replace('/', sep);
			 if (outfile.charAt(0)==sep) {
			     outfile="C:"+outfile; 
			 }
		     } 
		     writer = new PrintWriter(new BufferedWriter(new FileWriter(outfile)));
		 }
	     }


	     InputStream iStream = p.getInputStream();
             StringBuffer outputBuffer = new StringBuffer(); 
	     if (debug) System.out.println("JDJSTP.debug: ");
             JDJSTPRunFlag runFlag = new JDJSTPRunFlag(); 
            JDJSTPOutputThread stdoutThread = new JDJSTPOutputThread(iStream, outputBuffer, writer, encoding, runFlag);
             JDJSTPOutputThread stderrThread = new JDJSTPOutputThread(p.getErrorStream(), outputBuffer, writer, encoding, runFlag);
	     if (toStdout) {
		 stdoutThread.setWriteToStdout();
		 stderrThread.setWriteToStdout();
	     } 
             stdoutThread.start(); 
             stderrThread.start(); 
		      
             stdoutThread.join(); 
             stderrThread.join(); 
	     if (debug) {
                 System.out.println("**************** JDJSTP.debug *********");
                 System.out.println(outputBuffer); 
                 System.out.println("**************** JDJSTP.debug *********");

			 }
	 } catch (Exception e) {
	     e.printStackTrace(System.out);
	 }
	 if (outfile != null) {
	     if (outfile.equals("stdout")) {
	     } else { 
		 if (writer != null )  writer.close();
	     }
	 }
     }

     /*
      * Like showProcessOutput, but does not wait for output threads
      */
     public static JDJSTPOutputThread startProcessOutput(Process p, String outfile, boolean toStdout, int encoding ) throws Exception {
	 return startProcessOutput(p, outfile, toStdout, null, null, null, encoding, new JDJSTPRunFlag() ); 
     }
     public static JDJSTPOutputThread startProcessOutput(Process p,
							 String outfile,
							 boolean toStdout,
							 String[] hangMessages,
							 String[] hangMessagesException,
							 Vector<String> hangMessagesFound, 
							 int encoding,
							 JDJSTPRunFlag runFlag) throws Exception {
	 if (debug) {
	     System.out.println("JDJSTPTestcase.startProcessOutput");

	 } 
         PrintWriter writer = null;
         JDJSTPOutputThread stdoutThread = null;
	 try {
	     
	     if (outfile != null) {
		 if (outfile.equals("stdout") ) {
		     if (sep != '/') {
			 outfile = outfile.replace('/', sep);
			 if (outfile.charAt(0)==sep) {
			     outfile="C:"+outfile; 
			 }
		     } 
		     writer = new PrintWriter(System.out); 

		 } else { 
		     if (sep != '/') {
			 outfile = outfile.replace('/', sep);
			 if (outfile.charAt(0)==sep) {
			     outfile="C:"+outfile; 
			 }
		     } 
		     writer = new PrintWriter(new BufferedWriter(new FileWriter(outfile)));
		 }
	     }


	     InputStream iStream = p.getInputStream();
             StringBuffer outputBuffer = new StringBuffer(); 
	     if (debug) System.out.println("JDJSTP.debug: ");
             stdoutThread = new JDJSTPOutputThread(iStream, outputBuffer, writer, hangMessages, hangMessagesException, hangMessagesFound, encoding, runFlag);
             JDJSTPOutputThread stderrThread = new JDJSTPOutputThread(p.getErrorStream(), outputBuffer, writer, hangMessages, hangMessagesException, hangMessagesFound, encoding, runFlag);
             stdoutThread.setSecondaryOutputThread(stderrThread); 
	     if (toStdout) {
		 stdoutThread.setWriteToStdout();
		 stderrThread.setWriteToStdout();
	     } 
             stdoutThread.start(); 
             stderrThread.start(); 
		      
	 } catch (Exception e) {
	     e.printStackTrace(System.out);
	 }
	 return stdoutThread; 
     }




     //////////////////////////////////////////////////////////////////////////////
     //
     // Server file / command routines
     //
     //////////////////////////////////////////////////////////////////////////////

     /** 
      * Mark the file on the server as outdated
      */ 
     public static void markOutdated(String localFile) throws Exception  {
	 checkSetup(); 
	 execStatement(cmdStatement, "delete from "+envLibrary+".FILES where source like '%"+library+"%"+localFile+"'", true);
     } 
     /**
      * Get a file from the server and place on same directory on client
      */

     public static void getFileFromServer(String localFile ) throws Exception {

	 checkSetup();

	 //
	 // If we are already on the server, don't need to do anything
         // For some reason, for some files the cat to the SRCPF is not working. 
    //
    {
      ResultSet rs = cmdStatement
          .executeQuery("SELECT CURRENT SERVER FROM SYSIBM.SYSDUMMY1");
      rs.next();
      String serverName = rs.getString(1).toUpperCase();

      hostname1 = java.net.InetAddress.getLocalHost().getHostName()
          .toUpperCase();
      int dotIndex = hostname1.indexOf('.');
      if (dotIndex > 0) {
        hostname1 = hostname1.substring(0, dotIndex);
      }

      if (hostname1.equals(serverName)) {
        // File is already here. Return
        return; 
      }
    }

	 String serverPf = genPfName(localFile); 

	 //
	 // Create the serverPf
	 //
	   createSourceFile(envLibrary,serverPf);

	 
	 //
	 // Copy into the physical file
         //
	 serverShellCommand("cat "+localFile+" > /QSYS.LIB/"+envLibrary+".LIB/"+serverPf+".FILE/"+serverPf+".MBR ",
                       false);

	 ResultSet rs = cmdStatement.executeQuery("select SRCDTA from "+envLibrary+"."+serverPf);

	 if (sep != '/') {
	     localFile = localFile.replace('/', sep);
	     if (localFile.charAt(0)==sep) {
		 localFile="C:"+localFile; 
	     }
	 } 

	 PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(localFile)));

	 boolean found = rs.next();
	 while (found) {
	     String line = rs.getString(1);
             /* Note:: the sourcefp always pads the end with spaces */
             /*        So we need to trim */ 
	     line = line.trim(); 
	     if (debug) System.out.println("JDJSTP.debug: getFileFromServer: "+line); 
	     writer.println(line);
	     found = rs.next(); 
	 } 
	 rs.close();
	 writer.close();

         //
         // Clean up
         // 
	 serverCommand("QSYS/DLTF "+envLibrary+"/"+serverPf, true);

     } /* getFileFromServer */ 

     /**
      * Assure that a binary file on the server is up to date.
      */
     public static boolean updateBinaryServerFile(String localFile, String serverFile) throws Exception {
	 checkSetup();

         //
         // Determine if server file needs to be updated
         //

	 File file = new File(localFile);
	 long currentTime = file.lastModified();
	 if (currentTime == 0L) {
	     throw new Exception("File "+localFile+" not found"); 
	 } 
	 long lastTime = 0 ; 

	 try {
	     ResultSet rs = cmdStatement.executeQuery ("select sourcetimestamp from "+envLibrary+".FILES where source='"+localFile+"'");
	     boolean found = rs.next();
	     if (found) { 
		 Timestamp lastTimestamp = rs.getTimestamp(1);
		 lastTime = lastTimestamp.getTime();
		 if (debug) System.out.println("JDJSTP.debug: updateBinaryServerFile:  Timestamp found"); 
	     } else {
		 if (debug) System.out.println("JDJSTP.debug: updateBinaryServerFile:  Timestamp not found");
		 lastTime = 0; 
	     } 
	     rs.close();

	 } catch (SQLException e)  {
	     e.printStackTrace(System.out);
	     lastTime = 0; 
	 } 
	 if (lastTime < currentTime) { 

	     //
	     // Copy into a blob 
	     //

	     JDJSTPBlobUtil.copyToBlob(cmdConn, localFile, localFile);

	     //
             // Copy from a blob
             //

	     serverShellCommand("cd "+nativeBaseDir+"; java test.JDJSTPBlobUtil CopyFromBlob localhost "+serverFile+" "+localFile, false);


  	     //
	     // Update our record that the file has been updated
	     //
             currentTime=System.currentTimeMillis(); 
	     try {

		 PreparedStatement ps = cmdConn.prepareStatement("update "+envLibrary+".FILES set sourcepf='BINARY', sourcetimestamp=CURRENT TIMESTAMP where source='"+localFile+"'");
		 int count; 
		 count = ps.executeUpdate(); 

		 if (count > 0) { 
		     if (debug) System.out.println("JDJSTP.debug: updateBinaryServerFile:  record updated");
		 } else {
		     ps = cmdConn.prepareStatement("insert into "+envLibrary+".FILES values('"+localFile+"', CURRENT TIMESTAMP, 'BINARY')");
		     count = ps.executeUpdate(); 
		     // System.out.println(count); 
		 } 

	     } catch (Exception e) {
		 e.printStackTrace(System.out);
		 throw e; 
	     }
	     return true; 
	 } else {
	     return false; 
	 } /* Has file changed */ 

     } 


     /**
      * Assures that a file on the server is up to date.
      * Returns true if the file was updated. 
      * Assumptions.
      * JDJSTPENV library created.
      * JDJSTPCMD procedure exists. 
      */ 
     public static boolean updateServerFile(String localFile, String serverPf, String serverFile, String serverGeneratedFile) throws Exception {
       
       checkSetup();

       //
       // Determine if server file needs to be updated
       //

       File file = new File(localFile);
       long currentTime = file.lastModified();
       if (currentTime == 0L) {
	   throw new Exception("File "+localFile+" not found"); 
       } 
       long lastTime = 0 ;
       if (debug) System.out.println("JDJSTP.debug: updateServerFile:  localFile="+localFile+" ts="+currentTime+" now="+System.currentTimeMillis()); 


       try {
             // Verify that the server file exists
             String sql = "select SYSTOOLS.IFS_Access('"+serverFile+"','YES') FROM SYSIBM.SYSDUMMY1 ";
             ResultSet rs = cmdStatement.executeQuery(sql); 
             boolean fileExists = false; 
             String message = "File does not exists on server: " + serverFile;
             if (rs.next()) {
               if (rs.getInt(1) == 0) {
                 fileExists = true; 
               }
             }
             rs.close(); 
             if (fileExists) { 
               fileExists = false; 
               message = "File does not exists on server: " + serverGeneratedFile;
               sql = "select SYSTOOLS.IFS_Access('"+serverGeneratedFile+"','YES') FROM SYSIBM.SYSDUMMY1 ";
               
               rs = cmdStatement.executeQuery(sql); 
               if (rs.next()) {
                 if (rs.getInt(1) == 0) {
                   fileExists = true; 
                 }
               }
               rs.close(); 
               
             }
             if (fileExists) {
               rs = cmdStatement.executeQuery(
                   "select sourcetimestamp from " + envLibrary + ".FILES where source='" + serverFile + "'");
               boolean found = rs.next();
               if (found) {
                 Timestamp lastTimestamp = rs.getTimestamp(1);
                 lastTime = lastTimestamp.getTime();
                 if (debug)
                   System.out.println("JDJSTP.debug: updateServerFile:  Timestamp found in " + envLibrary
                       + ".FILES  for serverFile=" + serverFile);
               } else {
                 if (debug)
                   System.out.println("JDJSTP.debug: updateServerFile:  Timestamp not found in " + envLibrary
                       + ".FILES  for serverFile=" + serverFile);
                 lastTime = 0;
               }
               rs.close();
             } else {
               if (debug)
                 System.out.println("JDJSTP.debug: updateServerFile:  " + message );
               lastTime = 0;

             }
	     
       } catch (SQLException e)  {
	     e.printStackTrace(System.out);
	     lastTime = 0; 
       }
       if (debug) {
	     System.out.println("JDJSTP.updateServerfile lastTime = "+lastTime+" currentTime = "+currentTime); 
       } 

       if (lastTime < currentTime) { 
	 //
	 // Create the serverPf
	 //
	   createSourceFile(envLibrary,serverPf);

	 PreparedStatement ps = cmdConn.prepareStatement("insert into "+envLibrary+"."+serverPf+" values(?, 0, ?)");

	 //
	 // Read the file using javaio and insert each file suing
	 // insert into ... values(lineNum, 0, lineString);
	 //

      BufferedReader reader = new BufferedReader(new FileReader(localFile));
      int count = 1;
      String line = reader.readLine();
      while (line != null) {
	  // Do not trim the line.. This causes problem with RPG programs
	  // line = line.trim();
        try {
	  // Note.  Line number is numeric 6.2 which gives a max of 9999 lines
	  // Devide this by /50 to get more lines 
          ps.setDouble(1, ((double) count) / 50 );

          ps.setString(2, line);
        } catch (Exception e) {
          System.out.println("Exception processing " + localFile + " on line "
              + count + "line='" + line + "'");
          reader.close(); 
          throw e;
        }
        ps.executeUpdate();

        line = reader.readLine();
        count++;
      }
	 reader.close();

         //
         // Use cat to copy the database file to an IFS file
         //
	 String directory = serverFile; 
	 int slashIndex = serverFile.lastIndexOf('/');
	 if (slashIndex > 0) {
	     directory = directory.substring(0, slashIndex);
	 } 
	 serverShellCommand("mkdir -p "+directory, false);
	 serverShellCommand("rm -f "+serverFile, false);
	 serverShellCommand("touch -C 819 "+serverFile, false);
	 //
	 // Note.  This command does not work if the file is an IASP
	 // However, this doesn't report an error either
	 // 
	 serverShellCommand("cat /QSYS.LIB/"+envLibrary+".LIB/"+serverPf+".FILE/"+serverPf+".MBR > "+serverFile,
                       false);
	 //
	 // Run an IASP variation and ignore the errors
	 //
	 serverShellCommand("cat /IASP*/QSYS.LIB/"+envLibrary+".LIB/"+serverPf+".FILE/"+serverPf+".MBR >> "+serverFile,
                       true);
	 serverShellCommand("cat /INTS*/QSYS.LIB/"+envLibrary+".LIB/"+serverPf+".FILE/"+serverPf+".MBR >> "+serverFile,
                       true);


	 //
         // Update our record that the file has been updated
         //

	 try {
             currentTime = System.currentTimeMillis(); 
	     ps = cmdConn.prepareStatement("update "+envLibrary+".FILES set sourcepf='"+serverPf+"', sourcetimestamp=?  where source='"+serverFile+"'");
	     ps.setTimestamp(1, new java.sql.Timestamp(currentTime)); 
	     count = ps.executeUpdate(); 

	     if (count > 0) { 
		 if (debug) System.out.println("JDJSTP.debug: updateServerFile:  record updated");
	     } else {
		 ps = cmdConn.prepareStatement("insert into "+envLibrary+".FILES values('"+serverFile+"', CURRENT TIMESTAMP, '"+serverPf+"')");
		 count = ps.executeUpdate(); 
		 // System.out.println(count); 
	     } 

	 } catch (Exception e) {
	     e.printStackTrace(System.out);
	     throw e; 
	 }
	 return true; 
       } else {
	 return false; 
       } /* Has file changed */ 
     }


     /**
      * Escape all the quotes in the string
      */
     public static String fixQuotes(String command) {
	 int quoteIndex = command.indexOf('\'');
	 if (quoteIndex < 0) {
	     return command; 
	 } else {
	     command = command.substring(0, quoteIndex) + "''" + fixQuotes(command.substring(quoteIndex+1));
	     return command; 
	 } 
     } 
     /**
      * execute a shell command on the server
      */
     public static void serverShellCommand(String command, boolean ignoreError) throws Exception {
	 command = fixQuotes(command); 
	 command = "QSYS/QSH CMD('"+command+"')";
	 if (debug) System.out.println("Running serverCommand "+command); 
	 serverCommand(command, ignoreError); 
     } 

     public static void serverShellCommandForUser(String command, boolean ignoreError, String userid, String password) throws Exception {
         command = fixQuotes(command); 
         command = "QSYS/QSH CMD('"+command+"')";
         if (debug) System.out.println("Running serverCommandForUser "+command); 
         serverCommandForUser(command, ignoreError, userid, password); 
   } 


     public static void serverCommand(String command, boolean ignoreError) throws Exception {
	 if (ignoreError) { 
	     serverCommand(command, IGNORE_ERROR);
	 } else {
	     serverCommand(command, THROW_ERROR);
	 } 
     } 

     public static void serverCommand(Connection c, String command, boolean ignoreError) throws Exception {
	 if (ignoreError) { 
	     serverCommand(c, command, IGNORE_ERROR);
	 } else {
	     serverCommand(c, command, THROW_ERROR);
	 } 
     } 


     public static void mirrorServerCommand(String command, boolean ignoreError) throws Exception {
	 if (ignoreError) { 
	     mirrorServerCommand(command, IGNORE_ERROR);
	 } else {
	     mirrorServerCommand(command, THROW_ERROR);
	 } 
     } 


  /**
   * server command executes a CL command on the server Assumes JDSTPCMD
   * procedure exists
   */
  public static void serverCommand(String command, int errorHandling)
      throws Exception {
    checkSetup();
    if (cmdCallableStatement == null) {
      cmdCallableStatement = cmdConn.prepareCall("call QSYS2.QCMDEXC(?)");
    }
    cmdCallableStatement.setString(1, command);
    if (debug)
      System.out.println("JDJSTP.debug: serverCommand:  Executing " + command);
    try {
      cmdCallableStatement.execute();
    } catch (Exception e) {
      if (errorHandling == THROW_ERROR) {
        System.out.println("Error calling " + command);
        throw e;
      } else {
        if (errorHandling == VISIBLE_ERROR) {
          System.out.println("Error calling " + command);
          e.printStackTrace(System.out);
        }
        if (debug) {
          if (debug)
            System.out.println("JDJSTP.debug: serverCommand:  Ignoring error");
        }
      }
    } catch (java.lang.UnknownError jlu) {
      if (errorHandling == THROW_ERROR) {
        Exception e2 = new Exception("java.lang.unknown found");
        jlu.printStackTrace(System.out);
        throw e2;

      } else if (errorHandling == VISIBLE_ERROR) {
        System.out.println("java.lang.UnknownError calling " + command);
        jlu.printStackTrace(System.out);

      }
    }
  }

  /**
   * server command executes a CL command on the server Assumes JDSTPCMD
   * procedure exists
   */
  public static void serverCommand(Connection c, String command, int errorHandling)
      throws Exception {
    
    
      CallableStatement cmdCallableStatement = c.prepareCall("call QSYS2.QCMDEXC(?)");
   
    cmdCallableStatement.setString(1, command);
    if (debug)
      System.out.println("JDJSTP.debug: serverCommand:  Executing " + command);
    try {
      cmdCallableStatement.execute();
    } catch (Exception e) {
      if (errorHandling == THROW_ERROR) {
        System.out.println("Error calling " + command);
        throw e;
      } else {
        if (errorHandling == VISIBLE_ERROR) {
          System.out.println("Error calling " + command);
          e.printStackTrace(System.out);
        }
        if (debug) {
          if (debug)
            System.out.println("JDJSTP.debug: serverCommand:  Ignoring error");
        }
      }
    } catch (java.lang.UnknownError jlu) {
      if (errorHandling == THROW_ERROR) {
        Exception e2 = new Exception("java.lang.unknown found");
        jlu.printStackTrace(System.out);
        throw e2;

      } else if (errorHandling == VISIBLE_ERROR) {
        System.out.println("java.lang.UnknownError calling " + command);
        jlu.printStackTrace(System.out);

      }
    } finally {
        if (cmdCallableStatement != null) {
        	cmdCallableStatement.close(); 
        }
    }
  }


  /**
   * server command executes a CL command on the server Assumes JDSTPCMD
   * procedure exists
   */
  public static void mirrorServerCommand(String command, int errorHandling)
    throws Exception {
      Exception throwThisException = null; 
      checkSetup();
      if (cmdCallableStatement == null) {
	  cmdCallableStatement = cmdConn.prepareCall("call QSYS2.QCMDEXC(?)");
      }
      if (mirrorCmdCallableStatement == null) {
	  if (mirrorCmdConn != null) { 
	      mirrorCmdCallableStatement = mirrorCmdConn.prepareCall("call QSYS2.QCMDEXC(?)");
	  }
      }

      cmdCallableStatement.setString(1, command);
      if (mirrorCmdCallableStatement != null) { 
	  mirrorCmdCallableStatement.setString(1, command);
      }

      if (debug)
	  System.out.println("JDJSTP.debug: serverCommand:  Executing " + command);
      try {
	  cmdCallableStatement.execute();
      } catch (Exception e) {
	  if (errorHandling == THROW_ERROR || errorHandling == IGNORE_REMOTE_ERROR) {
	      System.out.println("Error calling " + command);
	      throwThisException= e;
	  } else {
	      if (errorHandling == VISIBLE_ERROR) {
		  System.out.println("Error calling " + command);
		  e.printStackTrace(System.out);
	      }
	      if (debug) {
		  if (debug)
		      System.out.println("JDJSTP.debug: serverCommand:  Ignoring error");
	      }
	  }
      } catch (java.lang.UnknownError jlu) {
	  if (errorHandling == THROW_ERROR || errorHandling == IGNORE_REMOTE_ERROR) {
	      Exception e2 = new Exception("java.lang.unknown found");
	      jlu.printStackTrace(System.out);
	      throwThisException= e2;

	  } else if (errorHandling == VISIBLE_ERROR) {
	      System.out.println("java.lang.UnknownError calling " + command);
	      jlu.printStackTrace(System.out);

	  }
      }

      try {
	  if (mirrorCmdCallableStatement != null) {
	      if (debug)
		  System.out.println("JDJSTP.debug: serverCommand:  Executing mirror  " + command);

	      mirrorCmdCallableStatement.execute();
	  } else {
	      if (debug)
		  System.out.println("JDJSTP.debug: serverCommand:  WARNING ================ NOT =============== executing mirror  " + command);

	  } 
      } catch (Exception e) {
	  if (debug)
	      System.out.println("JDJST.debug: serverCommand : Mirror error "+e.toString());

	  if (errorHandling == THROW_ERROR) {
	      System.out.println("Error calling " + command);
	      throwThisException =  e;
	  } else {
	      if (errorHandling == VISIBLE_ERROR) {
		  System.out.println("Error calling " + command);
		  e.printStackTrace(System.out);
	      }
	      if (debug) {
	
		      System.out.println("JDJSTP.debug: serverCommand:  Ignoring error");
	      }
	  }
      } catch (java.lang.UnknownError jlu) {
	  if (errorHandling == THROW_ERROR) {
	      Exception e2 = new Exception("java.lang.unknown found");
	      jlu.printStackTrace(System.out);
	      throwThisException = e2;

	  } else if (errorHandling == VISIBLE_ERROR) {
	      System.out.println("java.lang.UnknownError calling " + command);
	      jlu.printStackTrace(System.out);

	  }
      }


      if (throwThisException != null) {
	  throw throwThisException; 
      }
  }



     /**
      * server command executes a CL command on the server
      * Assumes JDSTPCMD procedure exists
      */ 
     public static void serverCommandForUser(String command, boolean ignoreError, String userid, String password ) throws Exception {
         checkSetup();
	 if (debug) System.out.println("JDJSTPTestcase.serverCommandForUser: getting connection for "+globalURL+","+userid+","+password); 
         Connection c = DriverManager.getConnection(globalURL, userid, password);
         CallableStatement cs =  c.prepareCall("call QSYS2.QCMDEXC(?)"); 
         
         cs.setString(1, command);
              if (debug) System.out.println("JDJSTP.debug: serverCommandForUser:  Executing "+command);
         try {
             cs.execute();
         } catch (Exception e) {
             if (!ignoreError) {
                 throw e;
             } else {
                 if (debug) {
                     if (debug) System.out.println("JDJSTP.debug: serverCommandForUser:  Ignoring error");
                 }
             } 
         } catch (java.lang.UnknownError jlu) {
             if (!ignoreError) {
                 Exception e2 = new Exception("java.lang.unknown found");
                 jlu.printStackTrace(System.out);
                 throw e2; 

             } 
         } 
         cs.close(); 
         c.close(); 
     }


     //////////////////////////////////////////////////////////////////////////////////////////////////
     //
     // PROCEDURE FUNCTIONS
     //
     //////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Uses a connection to drop a procedure
     * @param connection Connection used to create the procedure
     * @param procedure  Name of the procedure
     * @exception java.lang.Exception Exception is thrown if procedure is not dropped.
     */

    public static void dropProcedure(Connection connection, String procedure) throws Exception {
        Statement  s = null;
        try {
	    s = connection.createStatement();
            if (debug) System.out.println("JDJSTP.debug: drop procedure " + procedure);
	    s.executeUpdate("drop procedure " + procedure);
        }
        catch(Exception e) {
           //
	   // Try more 60 times if something in use
	   //
	   int count = 0;
	   while ( count < 60 && e != null && e.toString().indexOf("in use") > 0) {
	       try {
		   if (debug) System.out.println("JDJSTP.debug:  Attempting to drop procedure again");
		   if (s != null) s.executeUpdate("drop procedure " + procedure);
		   e = null;
	       } catch (Exception e2) {
		   count++;
		   e = e2;
	       }
	   }
	   if (e != null) {
	       throw e;
	   }
	} finally {
	    try {
		if (s != null) s.close();
	    } catch (Exception ex) {
	    }
	}

    }



    /**
     * Uses a connection to create a procedure
     * @param connection Connection used to create the procedure
     * @param procedure  Name of the procedure
     * @param procedureParms Parameter for the procedure
     * @exception java.lang.Exception Exception is thrown if procedure is not created.
     */
    public static void createProcedure(Connection connection, String procedure, String procedureParms) throws Exception {
	StringBuffer sb = new StringBuffer();
	String sql = null; 
        Statement  s = null;
        try {

	    s = connection.createStatement();

            //
            // make sure it is gone
            //
	    try {
		sql = "drop procedure " + procedure;
		sb.append(sql+"\n"); 
   	       s.executeUpdate(sql);
	    } catch (Exception e) {
              //
              // The only exception we should silently tolerate is a not found -- the rest will print messages
              //
	      if (e.toString().indexOf("not found") < 0) {
                //
                // Try more 60 times if something in use
                //
                int count = 0;
    	        while ( count < 60 && e != null && e.toString().indexOf("in use") > 0) {
		   try {
                       if (debug) System.out.println("JDJSTP.debug:  Attempting to drop procedure again");
		       sql = "drop procedure " + procedure; 
		       sb.append(sql+"\n");
		       s.executeUpdate(sql);
                       e = null;
		   } catch (Exception e2) {
                      count ++;
                      e = e2;
		   }
	         }
		if (e != null) {
		    System.out.println("JDJSTP.debug:  warning:  unable to drop procedure");
		    e.printStackTrace(System.out);
		}
	      }
            }
            if (debug) System.out.println("JDJSTP.debug:  create procedure " + procedure + procedureParms);
	    sql = "create procedure " + procedure + procedureParms; 
	    sb.append(sql+"\n");
            s.executeUpdate(sql);
	    sql = "grant all on procedure "+procedure +" to public "; 
	    sb.append(sql+"\n");
	    try  {
		s.executeUpdate(sql);
	    } catch (Exception e) {
		// Ignore any errors on the grant.
		// The grant will fail if the class does not exists
	    }

        }
        catch(Exception e) {
	    System.out.println("Exception executing the following\n"+sb.toString()); 
            throw e;
	} finally {
	    try {
		if (s != null) s.close();
	    } catch (Exception ex) {
	    }
	}


    }

    /**
     * Uses a connection to create a procedure without deleting it first
     * @param connection Connection used to create the procedure
     * @param procedure  Name of the procedure
     * @param procedureParms Parameter for the procedure
     * @exception java.lang.Exception Exception is thrown if procedure is not created.
     */
    public static void createProcedureNoDrop(Connection connection, String procedure, String procedureParms) throws Exception {
        Statement  s = null;
        try {

	    s = connection.createStatement();

            if (debug) System.out.println("JDJSTP.debug:  create procedure " + procedure + procedureParms);
            s.executeUpdate("create procedure " + procedure + procedureParms);

        }  finally {
	    try {
		if (s != null) s.close();
	    } catch (Exception ex) {
	    }
	}


    }



    /**
     * Uses a connection to verify that a procedure exists
     *
     * @param  connection  Connection to use for verification
     * @param  schema      Schema of the stored procedure
     * @param  procedure   Name of the stored procedure
     * @return Returns true if and only if the procedure exists
     **/
    public static boolean verifyProcedureExists(Connection connection, String schema, String procedure) {
       boolean found;
       ResultSet rs = null;
       try {
         //
         // Convert procedure to uppercase
         //
	 String procedure_ = procedure.toUpperCase();

         //
         // Convert schema to upppercase
         //
	 String schema_ = schema.toUpperCase();

	 DatabaseMetaData dmd_ = connection.getMetaData ();

	 rs = dmd_.getProcedures(null, schema_, procedure_);
	 boolean more = rs.next();

	 String foundProcedure = "EMPTY RS";
	 if (more) { 
	     foundProcedure = rs.getString("PROCEDURE_NAME");
	 }
	 if ( ! foundProcedure.equals(procedure_)) {
	    System.out.println("JDJSTP.debug: more = "+more);
	    System.out.println("JDJSTP.debug: found Procedure '"+foundProcedure+"' != procedure '" + procedure_ +"' in schema: "+schema_);
	    rs.close();
	    rs = dmd_.getProcedures(null, schema_, "%");
	    //
	    // Debug info
	    //
	    dispResultSet(rs);

	    System.out.println("JDJSTP.debug: found Procedure '"+foundProcedure+"' != procedure '" + procedure_ +"'");

	    found = false;
	 } else {
	     found = true;
	 }
       } catch (Exception e) {
          e.printStackTrace(System.out);
          found = false;
       } finally {
	   try {
	       if (rs != null) rs.close();
	   } catch (Exception ignore) {}
       }
       return found;
    }


    /**
     * Displays all columns and rows in the given result set
     * @param rs result set to display to System.out
     **/

    public static void dispResultSet (ResultSet rs)
      throws SQLException
    {
	int i;

	// Get the ResultSetMetaData.  This will be used for
	    // the column headings

	    java.sql.ResultSetMetaData rsmd = rs.getMetaData ();

	// Get the number of columns in the result set

	  int numCols = rsmd.getColumnCount ();

	// Display column headings

	  for (i=1; i<=numCols; i++) {
	      if (i > 1) System.out.print(",");
	      System.out.print(rsmd.getColumnLabel(i));
	  }
	System.out.println("");

	// Display data, fetching until end of the result set

	boolean more = rs.next ();
	while (more) {

	    // Loop through each column, getting the
	    // column data and displaying

	    for (i=1; i<=numCols; i++) {
		if (i > 1) System.out.print(",");
		System.out.print(rs.getString(i));
	    }
	    System.out.println("");

	    // Fetch the next result set row

	      more = rs.next ();
	}
    }



    /**
     * Uses a connection to drop a function
     */
    public static void dropFunction(Connection connection, String function, String functionSpec) throws Exception {
        Statement  s = null;
        try {

	    s = connection.createStatement();
            //
            // make sure it is gone
            //
	    try {
               // replace any x y z arguments
               String noArgs = function;
               int argindex = noArgs.indexOf("x ");
	       while (argindex > 0) {
                  // if (debug)  System.out.println("RTest:  noArgs = " + noArgs + "argindex = "+argindex  );
                  noArgs = noArgs.substring(0,argindex)  + noArgs.substring(argindex+1);
                  argindex = noArgs.indexOf("x ");
	       }
               if (debug)  System.out.println("JDJSTP:  drop function " + noArgs  );
   	       s.executeUpdate("drop  function " + noArgs  );
	    } catch (Exception e) {
              //
              // The only exception we should silently tolerate is a not found -- the rest will print messages
              //
	      if (e.toString().indexOf("not found") < 0) {
                //
                // Try more 60 times if something in use
                //
                int count = 0;
    	        while ( count < 60 && e != null && e.toString().indexOf("in use") > 0) {
		   try {
                       if (debug) System.out.println("JDJSTP.debug:  Attempting to drop function again");
		       s.executeUpdate("drop function " + function );
                       e = null;
		   } catch (Exception e2) {
                      count ++;
                      e = e2;
		   }
	         }
		if (e != null) {
		    System.out.println("JDJSTP.debug:  warning:  unable to drop function");
		    e.printStackTrace(System.out);
		}
	      }
            }
        }
        catch(Exception e) {
            throw e;
	} finally {
	    try {
		if (s != null) s.close();
	    } catch (Exception ex) {
	    }
	}


    }




    /**
     * Uses a connection to create a function
     * @param connection Connection used to create the function
     * @param function  Name of the function (as passed to drop function)
     * @param procedureParms Remainder of create function statement
     * @exception java.lang.Exception Exception is thrown if functionis not created.
     */
    public static void createFunction(Connection connection, String function, String functionSpec) throws Exception {
        Statement  s = null;
        try {

            //
            // make sure it is gone
            //
            dropFunction(connection, function, functionSpec);

	    s = connection.createStatement();
            if (debug) System.out.println("JDJSTP.debug:  create function " + function + functionSpec);
            s.executeUpdate("create function " + function + functionSpec);

        }
        catch(Exception e) {
            throw e;
	} finally {
	    try {
		if (s != null) s.close();
	    } catch (Exception ex) {
	    }
	}


    }

    /**
     * Uses a connection to grant access to a function
     * @param connection Connection used to grant access the function
     * @param function  Name of the function (as passed to drop function)
     * @param permissions Permissions to grant
     * @param user        User to grant permissions to
     * @exception java.lang.Exception Exception is thrown if functionis not created.
     */


    public static void grantFunction(Connection connection, String function, String permissions, String user) throws Exception {
        Statement  s = null;
        try {

	    s = connection.createStatement();
            String command = "grant "+permissions+" on function  "+function+" to " +user;
            if (debug) System.out.println("JDJSTP.debug:  "+command);
            s.executeUpdate(command);

        }
        catch(Exception e) {
            throw e;
	} finally {
	    try {
		if (s != null) s.close();
	    } catch (Exception ex) {
	    }
	}


    } /* grantFunction */ 


    /**
     * Uses a connection to verify that a function exists
     *
     * @param  connection  Connection to use for verification
     * @param  schema      Schema of the function
     * @param  function   Name of the  function
     * @return Returns true if and only if the function exists
     **/
    static PreparedStatement functionsPrepared = null;
    static Connection        functionsConnection = null;
    public static boolean verifyFunctionExists(Connection connection, String schema, String function) {
       boolean found;
       ResultSet rs = null;
       try {
         //
         // Convert function to uppercase
         //
	 String function_ = function.toUpperCase();

         //
         // Convert schema to upppercase
         //
	 String schema_ = schema.toUpperCase();

         //
         // prepare the statement if not yet prepared
         //
	 if (connection != functionsConnection || functionsPrepared == null ) {
	     if (debug) {
		 System.out.println("JDJSTP.debug: function not prepared ");
	     }
             functionsPrepared = connection.prepareStatement("select ROUTINE_SCHEMA, ROUTINE_NAME  from QSYS2.SYSFUNCS where ROUTINE_SCHEMA=? and ROUTINE_NAME=?");
             functionsConnection = connection;
	 } else {
	     if (debug) {
                 // Don't trace this noise 
		 // System.out.println("JDJSTP.debug: select routine function already prepared ");
	     }
	 }

         functionsPrepared.setString(1,schema_);
         functionsPrepared.setString(2,function_);

         rs = functionsPrepared.executeQuery();

	 boolean more = rs.next();
	 if (more ) {
	     String foundFunction = rs.getString("ROUTINE_NAME");
	     if ( ! foundFunction.equals(function_)) {
		 System.out.println("JDJSTP.debug: more = "+more);
		 System.out.println("JDJSTP.debug: found function '"+foundFunction+"' != function '" + function_ +"' in schema: "+schema_);
		 rs.close();

		 functionsPrepared.setString(1,schema);
		 functionsPrepared.setString(2,"%");
		 rs = functionsPrepared.executeQuery();
	         //
	         // Debug info
	         //
		 dispResultSet(rs);

		 System.out.println("JDJSTP.debug: found function '"+foundFunction+"' != function '" + function_ +"'");

		 found = false;
	     } else {
		 found = true;
	     }
	 } else {
	    if (debug) {
                System.out.println("JDJSTP.debug:  rs.next() returned false for "+schema+" "+function);
	    }
            found = false;
	 }
       } catch (Exception e) {
          e.printStackTrace(System.out);
          found = false;
       } catch ( java.lang.UnknownError jlu) {
	   System.out.println("*** java.lang.UnknownError encountered ***");
	   jlu.printStackTrace(System.out);
	   found = false; 
       } finally {
	   try {
	       if (rs != null) rs.close();
	   } catch (Exception ignore) {}
       }
       return found;
    }


  public static String BLOBTOCLOBSOURCE[] = {
    "#include <qusrjobi.h> ",
    "#include <stdio.h>",
    "#include <sqludf.h>", 
"void BLOBTOCLOB(",
"		SQLUDF_BLOB *inBlob,",
"		SQLUDF_CLOB *outClob,",
"		SQLUDF_SMALLINT * inBlobNullInd,",
"		SQLUDF_SMALLINT * outClobNullInd,",
"	       SQLUDF_TRAIL_ARGS_ALL) { ",
"",
"    *outClobNullInd = *inBlobNullInd; ",
"    if (*inBlobNullInd >= 0) {",
"	outClob->length = inBlob->length;",
"	memcpy(outClob->data, inBlob->data, inBlob->length);",
"    } ",
"}",
"",
"void BLOBTODBCLOB(",
"		SQLUDF_BLOB *inBlob,",
"		SQLUDF_CLOB *outClob,",
"		SQLUDF_SMALLINT * inBlobNullInd,",
"		SQLUDF_SMALLINT * outClobNullInd,",
"	       SQLUDF_TRAIL_ARGS_ALL) { ",
"",
"    *outClobNullInd = *inBlobNullInd; ",
"    if (*inBlobNullInd >= 0) {",
"	outClob->length = inBlob->length / 2;",
"	memcpy(outClob->data, inBlob->data, inBlob->length);",
"    } ",
"}",
"",
"",
 
    }; 


    public static void assureBLOBTOCLOBisAvailable(Connection connection) throws Exception {

	/*
	 * check to see if the function 
	 * If it doesn't they we need to create it
	 */

	boolean functionExists = verifyFunctionExists(connection, ""+envLibrary+"", "BLOBTOCLOB");
	if ( ! functionExists) {
	    createSourceFile(envLibrary,"BLOBTOCLOB");

	    PreparedStatement ps = cmdConn.prepareStatement("insert into "+envLibrary+".BLOBTOCLOB values(?, 0, ?)");

	    for (int i = 0; i < BLOBTOCLOBSOURCE.length; i++) {
		ps.setInt(1, i+1);
		ps.setString(2, BLOBTOCLOBSOURCE[i]);
		ps.executeUpdate();
	    }
	    /* Compile the program */
	    serverCommand("QSYS/CRTCMOD MODULE("+envLibrary+"/BLOBTOCLOB) SRCFILE("+envLibrary+"/BLOBTOCLOB) OUTPUT(*print) DBGVIEW(*ALL) OPTION(*SYSINCPATH *LOGMSG) SYSIFCOPT(*IFSIO)", false);
	    serverCommand("QSYS/GRTOBJAUT OBJ("+       envLibrary+"/BLOBTOCLOB) OBJTYPE(*MODULE) USER(*PUBLIC) AUT(*ALL) ", false); 
	    serverCommand("QSYS/CRTSRVPGM  SRVPGM(   "+envLibrary+"/BLOBTOCLOB) MODULE( "+envLibrary+"/BLOBTOCLOB) EXPORT(*ALL)", false);
	    serverCommand("QSYS/GRTOBJAUT OBJ("+       envLibrary+"/BLOBTOCLOB) OBJTYPE(*SRVPGM) USER(*PUBLIC) AUT(*ALL) ", false); 

	  /* register the function  */
	    Statement s = null; 
	    try {
		String sqlString; 
		s = connection.createStatement();


		sqlString = " CREATE FUNCTION "+envLibrary+".BLOBTOCLOB (JSON    BLOB(1M)) RETURNS CLOB(1M) CCSID 1208 LANGUAGE C PARAMETER STYLE SQL EXTERNAL NAME '"+envLibrary+"/BLOBTOCLOB(BLOBTOCLOB)'"; 
		if (debug) System.out.println("JDJSTP.debug: " + sqlString);
		s.executeUpdate(sqlString);

		sqlString = " CREATE FUNCTION "+envLibrary+".BLOBTODBCLOB (JSON    BLOB(1M)) RETURNS DBCLOB(1M) CCSID 1200 LANGUAGE C PARAMETER STYLE SQL EXTERNAL NAME '"+envLibrary+"/BLOBTOCLOB(BLOBTODBCLOB)'"; 
		if (debug) System.out.println("JDJSTP.debug: " + sqlString);
		s.executeUpdate(sqlString);

	    }  finally {
		try {
		    if (s != null) s.close();
		} catch (Exception ex) {
		}
	    }

	} else { /* if procedure doesn't exist */  
	    if (debug) System.out.println("JDJSTP.debug: "+envLibrary+".BLOBTOCLOB exists"); 
	}
    } 


  public static String GETJOBMEMSOURCE[] = {
    "#include <qusrjobi.h> ",
    "#include <stdio.h>",
    "int GETJOBMEM(void)",
    "{",
    "    Qwc_JOBI0150_t jobi;",
    "    QUSRJOBI(&jobi,",
    "             sizeof(jobi),",
    "             \"JOBI0150\",",
    "             \"*                         \",",
    "             \"                \");",
    "    return jobi.Temp_Storage_Used; ",
    "}",
    "int getjobmem(void) {",
    " return GETJOBMEM(); ",
    "}",
    "int main(int argc, int ** argv)",
    "{",
    "   printf(\"The job temporary usage is is %d\\n\", getjobmem());",
    "}",
    "" 
    }; 


    public static void assureGETJOBMEMisAvailable(Connection connection) throws Exception {

	/*
	 * check to see if the function 
	 * If it doesn't they we need to create it
	 */

	boolean functionExists = verifyFunctionExists(connection, ""+envLibrary+"", "GETJOBMEM");
	if ( ! functionExists) {
	    createSourceFile(envLibrary,"GETJOBMEM");

	    PreparedStatement ps = cmdConn.prepareStatement("insert into "+envLibrary+".GETJOBMEM values(?, 0, ?)");

	    for (int i = 0; i < GETJOBMEMSOURCE.length; i++) {
		ps.setInt(1, i+1);
		ps.setString(2, GETJOBMEMSOURCE[i]);
		ps.executeUpdate();
	    }
	    /* Compile the program */
	    serverCommand("QSYS/CRTCMOD MODULE("+envLibrary+"/GETJOBMEM) SRCFILE("+envLibrary+"/GETJOBMEM) OUTPUT(*print) DBGVIEW(*ALL) OPTION(*SYSINCPATH *LOGMSG) SYSIFCOPT(*IFSIO)", false);
	    serverCommand("QSYS/GRTOBJAUT OBJ("+       envLibrary+"/GETJOBMEM) OBJTYPE(*MODULE) USER(*PUBLIC) AUT(*ALL) ", false); 
	    serverCommand("QSYS/CRTSRVPGM  SRVPGM(   "+envLibrary+"/GETJOBMEM) MODULE( "+envLibrary+"/GETJOBMEM) EXPORT(*ALL)", false);
	    serverCommand("QSYS/GRTOBJAUT OBJ("+       envLibrary+"/GETJOBMEM) OBJTYPE(*SRVPGM) USER(*PUBLIC) AUT(*ALL) ", false); 

	  /* register the function  */
	    Statement s = null; 
	    try {
		String sqlString; 
		s = connection.createStatement();



		sqlString = " CREATE FUNCTION "+envLibrary+".GETJOBMEM () RETURNS INT NO SQL LANGUAGE C EXTERNAL NAME '"+envLibrary+"/GETJOBMEM(GETJOBMEM)' PARAMETER STYLE GENERAL"; 
		if (debug) System.out.println("JDJSTP.debug: " + sqlString);
		s.executeUpdate(sqlString);

	    }  finally {
		try {
		    if (s != null) s.close();
		} catch (Exception ex) {
		}
	    }

	} else { /* if procedure doesn't exist */  
	    if (debug) System.out.println("JDJSTP.debug: "+envLibrary+".GETJOBMEM exists"); 
	}
    } 

  public static String GETJOBNAMESOURCE[] = {
    "#include <qusrjobi.h> ",
    "#include <stdio.h>",
    "#define NAMESIZE 30", 
    "int nameset=0;",
    "int i=0;", 
    "char jobname[NAMESIZE+1];",
    "void GETJOBNAME(char * out)",
    "{",
    "    if (nameset == 0)  { ",
    "      char * dest; ", 
    "      Qwc_JOBI0100_t jobi;",
    "      QUSRJOBI(&jobi,",
    "               sizeof(jobi),",
    "               \"JOBI0100\",",
    "               \"*                         \",",
    "               \"                \");",
    "       dest = jobname;",
    "       for (i = 0; i < 6; i++) {",
    "          *dest = jobi.Job_Number[i];",
    "          if (*dest != ' ') dest++; ",
    "       }",
    "       *dest = '/'; dest++; ",
    "       for (i = 0; i < 10; i++) {",
    "         *dest = jobi.User_Name[i];",
    "          if (*dest != ' ') dest++; ",
    "       }",
    "       *dest = '/'; dest++; ",
    "       for (i = 0; i < 10; i++) {",
    "         *dest = jobi.Job_Name[i];",
    "          if (*dest != ' ') dest++; ",
    "       } ",
    "       while ( dest < (jobname+NAMESIZE)){",
    "         *dest = ' '; dest++; ",
    "       }", 
    "       *dest = '\0'; ",
    "      nameset=1;",
    "    }",
    "    strncpy(out, jobname, 30);",
    "}",
    "int main(int argc, int ** argv)",
    "{",
    "   char name[30]; ",
    "   GETJOBNAME(name);", 
    "   printf(\"The job name is %s\\n\", name);",
    "}",
    "" 
    }; 


    public static void assureGETJOBNAMEisAvailable(Connection connection) throws Exception {

	/*
	 * check to see if the function 
	 * If it doesn't they we need to create it
	 */

	boolean functionExists = verifyFunctionExists(connection, ""+envLibrary+"", "GETJOBNAME");
	if ( ! functionExists) {
	    boolean cmdConnWasNull = false;
	    if (cmdConn == null) {
		cmdConnWasNull = true;
		cmdConn = connection;
                globalURL="UNKNOWN"; 
	    }
	    createSourceFile(envLibrary,"GETJOBNAME");

	    PreparedStatement ps = cmdConn.prepareStatement("insert into "+envLibrary+".GETJOBNAME values(?, 0, ?)");

	    for (int i = 0; i < GETJOBNAMESOURCE.length; i++) {
		ps.setInt(1, i+1);
		ps.setString(2, GETJOBNAMESOURCE[i]);
		ps.executeUpdate();
	    }
	    /* Compile the program */
	    serverCommand("QSYS/CRTCMOD MODULE("+envLibrary+"/GETJOBNAME) SRCFILE("+envLibrary+"/GETJOBNAME) OUTPUT(*print) DBGVIEW(*ALL) OPTION(*SYSINCPATH *LOGMSG) SYSIFCOPT(*IFSIO)", false);
	    serverCommand("QSYS/CRTSRVPGM  SRVPGM(   "+envLibrary+"/GETJOBNAME) MODULE( "+envLibrary+"/GETJOBNAME) EXPORT(*ALL)", false);
	    serverCommand("QSYS/GRTOBJAUT OBJ("+       envLibrary+"/GETJOBNAME) OBJTYPE(*ALL) USER(*PUBLIC) AUT(*ALL) ", false); 

	  /* register the function  */
	    Statement s = null; 
	    try {
		String sqlString; 
		s = connection.createStatement();


		sqlString = " CREATE FUNCTION "+envLibrary+".GETJOBNAME () RETURNS CHAR(30) NO SQL LANGUAGE C EXTERNAL NAME '"+envLibrary+"/GETJOBNAME(GETJOBNAME)' PARAMETER STYLE DB2SQL"; 
		if (debug) System.out.println("JDJSTP.debug: " + sqlString);
		s.executeUpdate(sqlString);

	    }  finally {
		try {
		    if (s != null) s.close();
		} catch (Exception ex) {
		}
	    }
	    if (cmdConnWasNull) cmdConn=null; 

	} else { /* if procedure doesn't exist */  
	    if (debug) System.out.println("JDJSTP.debug: "+envLibrary+".GETJOBNAME exists"); 
	}
    } 




  public static String RETCASOURCE[] = {
       "#include <stdio.h>",
       "#include <stdlib.h>",
       "#include <string.h> ",
       "#include <mih/retca.h> ",
       "",
       "void getMask(char * output, char input) {",
       "    int needComma=0; ",
       "    output[0]='\0';",
       "    if ( (input & 0x20) !=  0) {",
       "	strcat(output,\"OVERFLOW\");",
       "	needComma = 1; ",
       "    }",
       "",
       "    if ( (input & 0x10) !=  0) {",
       "	if (needComma) strcat(output,\",\"); ",
       "	strcat(output,\"UNDERFLOW\");",
       "	needComma = 1; ",
       "    }",
       "",
       "    if ( (input & 0x8) !=  0) {",
       "	if (needComma) strcat(output,\",\"); ",
       "	strcat(output,\"ZERO-DIVIDE\");",
       "	needComma = 1; ",
       "    }",
       "",
       "    if ( (input & 0x4) !=  0) {",
       "	if (needComma) strcat(output,\",\"); ",
       "	strcat(output,\"INEXACT-RESULT\");",
       "	needComma = 1; ",
       "    }",
       "",
       "    if ( (input & 0x2) !=  0) {",
       "	if (needComma) strcat(output,\",\"); ",
       "	strcat(output,\"INVALID-OPERATION\");",
       "	needComma = 1; ",
       "    }",
       "",
       "} ",
       "",
       "",
       "void getMode(char * output, char input) {",
       "    char binaryMask;",
       "    char decimalMask;",
       "",
       "    strcpy(output,\"BINARY_ROUND=\");",
       "    binaryMask = (input >> 5) & 0x3;",
       "    switch (binaryMask) {",
       "	case 0x00:  strcat(output, \"Round_toward_positive_infinity\"); break;",
       "	case 0x01:  strcat(output, \"Round_toward_negative_infinity\"); break;",
       "	case 0x2:  strcat(output, \"Round_toward_zero\"); break;",
       "	case 0x3:  strcat(output, \"Round_to_nearest\"); break;  ",
       "    }",
       "    strcat(output,\" DECIMAL_ROUND=\"); ",
       "    decimalMask = (input & 0x7);",
       "    switch (decimalMask) {",
       "	case 0x0: strcat(output, \"Round_to_nearest,_ties_to_even\"); break;",
       "	case 0x1: strcat(output, \"Round_toward_zero\"); break;",
       "	case 0x2: strcat(output, \"Round_toward_positive_infinity\"); break;",
       "	case 0x3: strcat(output, \"Round_toward_negative_infinity\"); break;",
       "	case 0x4: strcat(output, \"Round_to_nearest,_ties_away_from_zero\"); break;",
       "	case 0x5: strcat(output, \"Round_to_nearest,_ties_toward_zero\"); break;",
       "	case 0x6: strcat(output, \"Round_away_from_zero\"); break;",
       "	case 0x7: strcat(output, \"Round_to_prepare_for_shorter_precision \"); break;",
       "    }",
       "",
       "} ",
       "",
       "void RETCA(char * outputString)",
       "{",
       "",
       "    int setting;",
       "    int roundingMode = -1; ",
       "    char mask[256];",
       "    char occurrence[256];",
       "    char mode[256]; ",
       "",
       "    setting =  _RETCA( 0x0f ); ",
       "",
       "    getMask(mask, (char) (setting >> 24) );",
       "    getMask(occurrence, (char) ((setting >> 8) & 0xff));",
       "    getMode(mode, setting & 0xff);",
       "    ",
       "",
       "    sprintf(outputString,\"Setting=0x%08x MASK=%s OCCURENCE=%s MODE=%s\",",
       "	    setting,",
       "	    mask,",
       "	    occurrence,",
       "	    mode); ",
       "}",
       "",
       "",
    "int main(int argc, int ** argv)",
    "{",
    "   char info[256]; ",
    "   RETCA(info);", 
    "   printf(\"RETCA returned %s\\n\", info);",
    "}",
    "" 
    }; 


    public static void assureRETCAisAvailable(Connection connection) throws Exception {

	/*
	 * check to see if the function 
	 * If it doesn't they we need to create it
	 */

	boolean functionExists = verifyFunctionExists(connection, envLibrary, "RETCA");
	if ( ! functionExists) {
	    System.out.println("JDJSTPTestcase:  Building RETCA"); 
	    boolean cmdConnWasNull = false;
	    if (cmdConn == null) {
		cmdConnWasNull = true;
		cmdConn = connection;
                globalURL="UNKNOWN"; 
	    }
	    createSourceFile(envLibrary,"RETCA");

	    PreparedStatement ps = cmdConn.prepareStatement("insert into "+envLibrary+".RETCA values(?, 0, ?)");

	    for (int i = 0; i < RETCASOURCE.length; i++) {
		ps.setInt(1, i+1);
		ps.setString(2, RETCASOURCE[i]);
		ps.executeUpdate();
	    }
	    /* Compile the program */
	    String command =  "QSYS/CRTCMOD MODULE("+envLibrary+"/RETCA) SRCFILE("+envLibrary+"/RETCA) OUTPUT(*print) DBGVIEW(*ALL) OPTION(*SYSINCPATH *LOGMSG) SYSIFCOPT(*IFSIO)"; 
	    try {
		serverCommand(command, false);
	    } catch (Exception e) {
		System.out.println("Command failed "+command); 
		throw e; 
	    }
	    command = "QSYS/CRTSRVPGM  SRVPGM(   "+envLibrary+"/RETCA) MODULE( "+envLibrary+"/RETCA) EXPORT(*ALL)";
	    try { 
		serverCommand(command, false);
	    } catch (Exception e) {
		System.out.println("Command failed "+command); 
		throw e; 
	    }

	    serverCommand("QSYS/GRTOBJAUT OBJ("+       envLibrary+"/RETCA) OBJTYPE(*ALL) USER(*PUBLIC) AUT(*ALL) ", false); 

	  /* register the function  */
	    Statement s = null; 
	    try {
		String sqlString; 
		s = connection.createStatement();


		sqlString = " CREATE FUNCTION "+envLibrary+".RETCA () RETURNS CHAR(256) NO SQL LANGUAGE C EXTERNAL NAME '"+envLibrary+"/RETCA(RETCA)' PARAMETER STYLE DB2SQL NOT FENCED DISALLOW PARALLEL "; 
		if (debug) System.out.println("JDJSTP.debug: " + sqlString);
		s.executeUpdate(sqlString);

		/* Authorize all users to use it */
		sqlString = " GRANT EXECUTE ON FUNCTION "+envLibrary+".RETCA () TO PUBLIC"; 

		if (debug) System.out.println("JDJSTP.debug: " + sqlString);
		s.executeUpdate(sqlString);

		sqlString = "CALL QSYS.QCMDEXC('GRTOBJAUT OBJ("+envLibrary+") OBJTYPE(*LIB) USER(*PUBLIC) AUT(*USE)                                                               ', 0000000100.00000)";
		if (debug) System.out.println("JDJSTP.debug: " + sqlString);
		try { 
		    s.executeUpdate(sqlString);
		} catch (Exception e) {
		    System.out.println("Exception on "+sqlString);
		    throw e; 
		}



	    }  finally {
		try {
		    if (s != null) s.close();
		} catch (Exception ex) {
		}
	    }
	    if (cmdConnWasNull) cmdConn=null; 

	} else { /* if procedure doesn't exist */  
	    if (debug) System.out.println("JDJSTP.debug: "+envLibrary+".RETCA exists"); 
	}
    } 


  public static String GETSUBSYSSOURCE[] = {
    "#include <qusrjobi.h> ",
    "#include <stdio.h>",
    "#define NAMESIZE 10", 
    "int nameset=0;",
    "int i=0;", 
    "char subsys[NAMESIZE+1];",
    "void GETSUBSYS(char * out)",
    "{",
    "    if (nameset == 0)  { ",
    "      char * dest; ", 
    "      Qwc_JOBI0200_t jobi;",
    "      QUSRJOBI(&jobi,",
    "               sizeof(jobi),",
    "               \"JOBI0200\",",
    "               \"*                         \",",
    "               \"                \");",
    "       dest = subsys;",
    "       for (i = 0; i < 10; i++) {",
    "          *dest = jobi.Subsys_Name[i];",
    "          if (*dest != ' ') dest++; ",
    "       }",
    "       while ( dest < (subsys+NAMESIZE)){",
    "         *dest = ' '; dest++; ",
    "       }", 
    "       *dest = '\0'; ",
    "      nameset=1;",
    "    }",
    "    strncpy(out, subsys, 10);",
    "}",
    "int main(int argc, int ** argv)",
    "{",
    "   char name[30]; ",
    "   GETSUBSYS(name);", 
    "   printf(\"The job name is %s\\n\", name);",
    "}",
    "" 
    }; 


    public static void assureGETSUBSYSisAvailable(Connection connection) throws Exception {

	/*
	 * check to see if the function 
	 * If it doesn't they we need to create it
	 */

	boolean functionExists = verifyFunctionExists(connection, ""+envLibrary+"", "GETSUBSYS");
	if ( ! functionExists) {
	    boolean cmdConnWasNull = false;
	    if (cmdConn == null) {
		cmdConnWasNull = true;
		cmdConn = connection;
                globalURL="UNKNOWN"; 
	    }
	    createSourceFile(envLibrary,"GETSUBSYS");

	    PreparedStatement ps = cmdConn.prepareStatement("insert into "+envLibrary+".GETSUBSYS values(?, 0, ?)");

	    for (int i = 0; i < GETSUBSYSSOURCE.length; i++) {
		ps.setInt(1, i+1);
		ps.setString(2, GETSUBSYSSOURCE[i]);
		ps.executeUpdate();
	    }
	    /* Compile the program */
	    serverCommand("QSYS/CRTCMOD MODULE("+envLibrary+"/GETSUBSYS) SRCFILE("+envLibrary+"/GETSUBSYS) OUTPUT(*print) DBGVIEW(*ALL) OPTION(*SYSINCPATH *LOGMSG) SYSIFCOPT(*IFSIO)", false);
	    serverCommand("QSYS/CRTSRVPGM  SRVPGM(   "+envLibrary+"/GETSUBSYS) MODULE( "+envLibrary+"/GETSUBSYS) EXPORT(*ALL)", false);
	    serverCommand("QSYS/GRTOBJAUT OBJ("+       envLibrary+"/GETSUBSYS) OBJTYPE(*ALL) USER(*PUBLIC) AUT(*ALL) ", false); 
	  /* register the function  */
	    Statement s = null; 
	    try {
		String sqlString; 
		s = connection.createStatement();


		sqlString = " CREATE FUNCTION "+envLibrary+".GETSUBSYS () RETURNS CHAR(10) NO SQL LANGUAGE C EXTERNAL NAME '"+envLibrary+"/GETSUBSYS(GETSUBSYS)' PARAMETER STYLE DB2SQL"; 
		if (debug) System.out.println("JDJSTP.debug: " + sqlString);
		s.executeUpdate(sqlString);

	    }  finally {
		try {
		    if (s != null) s.close();
		} catch (Exception ex) {
		}
	    }
	    if (cmdConnWasNull) cmdConn=null; 

	} else { /* if procedure doesn't exist */  
	    if (debug) System.out.println("JDJSTP.debug: "+envLibrary+".GETSUBSYS exists"); 
	}
    } 


  public static String ALLCHARSSOURCE[] = {
    "#include <stdio.h>",
    "void ALLCHARS(char * out)",
    "{",
    "    int i;",
    "    for (i = 0; i < 256; i++ ) {",
    "      if (i >= 0x40) {",
    "	    out[i] = i; ",
    "	  } else {",
    "	    out[i] = 0x40; ",
    "	  } ",
    "    } ",
    "}",
    "int main(int argc, int ** argv)",
    "{",
    "   char name[257]; ",
    "   ALLCHARS(name);", 
    "   printf(\"The job name is %s\\n\", name);",
    "}",
    "" 
    }; 


    public static void assureALLCHARSisAvailable(Connection connection) throws Exception {

	/*
	 * check to see if the function 
	 * If it doesn't they we need to create it
	 */

	boolean functionExists = verifyFunctionExists(connection, ""+envLibrary+"", "ALLCHARS");
	if ( ! functionExists) {
	    boolean cmdConnWasNull = false;
	    if (cmdConn == null) {
		cmdConnWasNull = true;
		cmdConn = connection;
                globalURL="UNKNOWN"; 
	    }
	    createSourceFile(envLibrary,"ALLCHARS");

	    PreparedStatement ps = cmdConn.prepareStatement("insert into "+envLibrary+".ALLCHARS values(?, 0, ?)");

	    for (int i = 0; i < ALLCHARSSOURCE.length; i++) {
		ps.setInt(1, i+1);
		ps.setString(2, ALLCHARSSOURCE[i]);
		ps.executeUpdate();
	    }
	    /* Compile the program */
	    String command = "QSYS/CRTCMOD MODULE("+envLibrary+"/ALLCHARS) SRCFILE("+envLibrary+"/ALLCHARS) OUTPUT(*print) DBGVIEW(*ALL) OPTION(*SYSINCPATH *LOGMSG) SYSIFCOPT(*IFSIO)";
	    try { 
	    serverCommand(command, false);
	    } catch (Exception e) {
		System.out.println("Exception on "+command); 
		throw e; 
	    }
	    command = "QSYS/CRTSRVPGM  SRVPGM(   "+envLibrary+"/ALLCHARS) MODULE( "+envLibrary+"/ALLCHARS) EXPORT(*ALL)";
	    try { 
	    serverCommand(command, false);
	    } catch (Exception e) {
		System.out.println("Exception on "+command); 
		throw e; 
	    }
	    serverCommand("QSYS/GRTOBJAUT OBJ("+       envLibrary+"/ALLCHARS) OBJTYPE(*ALL) USER(*PUBLIC) AUT(*ALL) ", false); 

	  /* register the function  */
	    Statement s = null; 
	    try {
		String sqlString; 
		s = connection.createStatement();


		sqlString = " CREATE FUNCTION "+envLibrary+".ALLCHARS () RETURNS CHAR(256) NO SQL LANGUAGE C EXTERNAL NAME '"+envLibrary+"/ALLCHARS(ALLCHARS)' PARAMETER STYLE DB2SQL"; 
		if (debug) System.out.println("JDJSTP.debug: " + sqlString);
		s.executeUpdate(sqlString);

	    }  finally {
		try {
		    if (s != null) s.close();
		} catch (Exception ex) {
		}
	    }
	    if (cmdConnWasNull) cmdConn=null; 

	} else { /* if procedure doesn't exist */  
	    if (debug) System.out.println("JDJSTP.debug: "+envLibrary+".ALLCHARS exists"); 
	}
    } 




  public static String ALLCHARS2SOURCE[] = {
    "#include <stdio.h>",
    "void ALLCHARS2(char * out)",
    "{",
    "    int i;",
    "    for (i = 0; i < 256; i++ ) {",
    "      if (i >= 0x40) {",
    "	    out[2*i] = i; ",
    "	  } else {",
    "	    out[2*i] = 0x40; ",
    "	  } ",
    "	    out[2*i+1] = 0x40; ",
    "    } ",
    "}",
    "int main(int argc, int ** argv)",
    "{",
    "   char name[513]; ",
    "   ALLCHARS2(name);", 
    "   printf(\"The job name is %s\\n\", name);",
    "}",
    "" 
    }; 


    public static void assureALLCHARS2isAvailable(Connection connection) throws Exception {

	/*
	 * check to see if the function 
	 * If it doesn't they we need to create it
	 */

	boolean functionExists = verifyFunctionExists(connection, ""+envLibrary+"", "ALLCHARS2");
	if ( ! functionExists) {
	    boolean cmdConnWasNull = false;
	    if (cmdConn == null) {
		cmdConnWasNull = true;
		cmdConn = connection;
                globalURL="UNKNOWN"; 
	    }
	    createSourceFile(envLibrary,"ALLCHARS2");

	    PreparedStatement ps = cmdConn.prepareStatement("insert into "+envLibrary+".ALLCHARS2 values(?, 0, ?)");

	    for (int i = 0; i < ALLCHARS2SOURCE.length; i++) {
		ps.setInt(1, i+1);
		ps.setString(2, ALLCHARS2SOURCE[i]);
		ps.executeUpdate();
	    }
	    /* Compile the program */
	    String command = "QSYS/CRTCMOD MODULE("+envLibrary+"/ALLCHARS2) SRCFILE("+envLibrary+"/ALLCHARS2) OUTPUT(*print) DBGVIEW(*ALL) OPTION(*SYSINCPATH *LOGMSG) SYSIFCOPT(*IFSIO)";
	    try { 
	    serverCommand(command, false);
	    } catch (Exception e) {
		System.out.println("Exception on "+command); 
		throw e; 
	    }
	    command = "QSYS/CRTSRVPGM  SRVPGM(   "+envLibrary+"/ALLCHARS2) MODULE( "+envLibrary+"/ALLCHARS2) EXPORT(*ALL)";
	    try { 
	    serverCommand(command, false);
	    } catch (Exception e) {
		System.out.println("Exception on "+command); 
		throw e; 
	    }
	    serverCommand("QSYS/GRTOBJAUT OBJ("+       envLibrary+"/ALLCHARS2) OBJTYPE(*ALL) USER(*PUBLIC) AUT(*ALL) ", false); 

	  /* register the function  */
	    Statement s = null; 
	    try {
		String sqlString; 
		s = connection.createStatement();


		sqlString = " CREATE FUNCTION "+envLibrary+".ALLCHARS2 () RETURNS CHAR(256) NO SQL LANGUAGE C EXTERNAL NAME '"+envLibrary+"/ALLCHARS2(ALLCHARS2)' PARAMETER STYLE DB2SQL"; 
		if (debug) System.out.println("JDJSTP.debug: " + sqlString);
		s.executeUpdate(sqlString);

	    }  finally {
		try {
		    if (s != null) s.close();
		} catch (Exception ex) {
		}
	    }
	    if (cmdConnWasNull) cmdConn=null; 

	} else { /* if procedure doesn't exist */  
	    if (debug) System.out.println("JDJSTP.debug: "+envLibrary+".ALLCHARS2 exists"); 
	}
    } 




  public static String CHARRANGESOURCE[] = {
    "#include <stdio.h>",
    "void CHARRANGE(int *startPtr, int *finishPtr, char * out)",
    "{",
    "    int i;",
    "    int loop; ", 
    "    int start;",
    "    int finish;",
    "    start = *startPtr;",
    "    finish = *finishPtr;",
    "    i = 0;  ",
    "    for (loop = start; loop <= finish; loop++) {",
    "       out[i] = loop;",
    "       i++;", 
    "    } ",
    "    out[i] = 0; ", 
    "}",
    "int main(int argc, int ** argv)",
    "{",
    "   char name[257]; ",
    "   int start = 120; ",
    "   int end   = 121;", 
    "   CHARRANGE(&start,&end,name);", 
    "   printf(\"The job name is %s\\n\", name);",
    "}",
    "" 
    }; 


    public static void assureCHARRANGEisAvailable(Connection connection) throws Exception {

	/*
	 * check to see if the function 
	 * If it doesn't they we need to create it
	 */
	int version = 1;
	boolean functionExists = verifyFunctionExists(connection, ""+envLibrary+"", "CHARRANGE"+version);
	if ( ! functionExists) {
	    boolean cmdConnWasNull = false;
	    if (cmdConn == null) {
		cmdConnWasNull = true;
		cmdConn = connection;
                globalURL="UNKNOWN"; 
	    }
	    createSourceFile(envLibrary,"CHARRANGE");



	    PreparedStatement ps = cmdConn.prepareStatement("insert into "+envLibrary+".CHARRANGE values(?, 0, ?)");

	    for (int i = 0; i < CHARRANGESOURCE.length; i++) {
		ps.setInt(1, i+1);
		ps.setString(2, CHARRANGESOURCE[i]);
		ps.executeUpdate();
	    }
	    /* Compile the program */
	    String command = "QSYS/CRTCMOD MODULE("+envLibrary+"/CHARRANGE) SRCFILE("+envLibrary+"/CHARRANGE) OUTPUT(*print) DBGVIEW(*ALL) OPTION(*SYSINCPATH *LOGMSG) SYSIFCOPT(*IFSIO)";
	    try { 
	    serverCommand(command, false);
	    } catch (Exception e) {
		System.out.println("Exception on "+command); 
		throw e; 
	    }
	    command = "QSYS/CRTSRVPGM  SRVPGM(   "+envLibrary+"/CHARRANGE) MODULE( "+envLibrary+"/CHARRANGE) EXPORT(*ALL)";
	    try { 
	    serverCommand(command, false);
	    } catch (Exception e) {
		System.out.println("Exception on "+command); 
		throw e; 
	    }
	    try { 
	    serverCommand("QSYS/GRTOBJAUT OBJ("+       envLibrary+"/CHARRANGE) OBJTYPE(*ALL) USER(*PUBLIC) AUT(*ALL) ", false);
	    } catch (Exception e) {
		System.out.println("Exception on "+command); 
		System.out.println("Continuing "); 
	    }


	  /* register the function  */
	    Statement s = null; 
	    try {
		String sqlString; 
		s = connection.createStatement();

		try {
		    s.executeUpdate("DROP FUNCTION "+envLibrary+".CHARRANGE"); 
		} catch (Exception e) {
		} 
		sqlString = " CREATE FUNCTION "+envLibrary+".CHARRANGE (start int, end int) RETURNS VARCHAR(256) NO SQL LANGUAGE C EXTERNAL NAME '"+envLibrary+"/CHARRANGE(CHARRANGE)' PARAMETER STYLE DB2SQL"; 
		if (debug) System.out.println("JDJSTP.debug: " + sqlString);
		s.executeUpdate(sqlString);


		sqlString = " CREATE FUNCTION "+envLibrary+".CHARRANGE"+version+" (start int, end int) RETURNS VARCHAR(256) NO SQL LANGUAGE C EXTERNAL NAME '"+envLibrary+"/CHARRANGE(CHARRANGE)' PARAMETER STYLE DB2SQL"; 
		if (debug) System.out.println("JDJSTP.debug: " + sqlString);
		s.executeUpdate(sqlString);

	    }  finally {
		try {
		    if (s != null) s.close();
		} catch (Exception ex) {
		}
	    }
	    if (cmdConnWasNull) cmdConn=null; 

	} else { /* if procedure doesn't exist */  
	    if (debug) System.out.println("JDJSTP.debug: "+envLibrary+".CHARRANGE exists"); 
	}
    } 



  public static String CHARRANGEHSOURCE[] = {
    "#include <stdio.h>",
    "void CHARRANGEH(char *startPtr, char *finishPtr, char * out)",
    "{",
    "    int i;",
    "    int loop;  ", 
    "    int start;",
    "    int finish;",
    "    sscanf(startPtr, \"%x\", &start); ", 
    "    sscanf(finishPtr,\"%x\", &finish); ", 
    "    i = 0;  ", 
    "    for (loop = start; loop <= finish; loop++ ) {",
    "", 
    "       if (loop < 0x100) {", 
    "           out[i] = loop;", 
    "           i++;", 
    "       } else if ( loop < 0x10000 ) {", 
    "           out[i] = loop / 0x100;", 
    "           i++;", 
    "           out[i] = loop % 0x100;", 
    "           i++;", 
    "       } else if (loop < 0x1000000) {", 
    "           out[i] = loop / 0x10000;", 
    "           i++;", 
    "           out[i] = ( loop / 0x100) % 0x100;", 
    "           i++;", 
    "           out[i] = loop % 0x100;", 
    "           i++;", 
    "       } else {", 
    "           out[i] = loop / 0x1000000;", 
    "           i++;", 
    "           out[i] = ( loop / 0x10000) % 0x100;", 
    "           i++;", 
    "           out[i] = ( loop / 0x100) % 0x100;", 
    "           i++;", 
    "           out[i] = loop % 0x100;", 
    "           i++;", 
    "       } ", 
    "    } ",
    "    out[i] = 0; ", 
    "}",
    "int main(int argc, int ** argv)",
    "{",
    "   char name[257]; ",
    "   char * start = \"50\";",
    "   char * end   = \"60\";", 
    "   CHARRANGEH(start,end,name);", 
    "   printf(\"The job name is %s\\n\", name);",
    "}",
    "" 
    }; 


    public static void assureCHARRANGEHisAvailable(Connection connection) throws Exception {

	/*
	 * check to see if the function 
	 * If it doesn't they we need to create it
	 */

	boolean functionExists = verifyFunctionExists(connection, ""+envLibrary+"", "CHARRANGEH");
	if ( ! functionExists) {
	    boolean cmdConnWasNull = false;
	    if (cmdConn == null) {
		cmdConnWasNull = true;
		cmdConn = connection;
                globalURL="UNKNOWN"; 
	    }
	    createSourceFile(envLibrary,"CHARRANGEH");



	    PreparedStatement ps = cmdConn.prepareStatement("insert into "+envLibrary+".CHARRANGEH values(?, 0, ?)");

	    for (int i = 0; i < CHARRANGEHSOURCE.length; i++) {
		ps.setInt(1, i+1);
		ps.setString(2, CHARRANGEHSOURCE[i]);
		ps.executeUpdate();
	    }
	    /* Compile the program */
	    String command = "QSYS/CRTCMOD MODULE("+envLibrary+"/CHARRANGEH) SRCFILE("+envLibrary+"/CHARRANGEH) OUTPUT(*print) DBGVIEW(*ALL) OPTION(*SYSINCPATH *LOGMSG) SYSIFCOPT(*IFSIO)";
	    try { 
	    serverCommand(command, false);
	    } catch (Exception e) {
		System.out.println("Exception on "+command); 
		throw e; 
	    }
	    command = "QSYS/CRTSRVPGM  SRVPGM(   "+envLibrary+"/CHARRANGEH) MODULE( "+envLibrary+"/CHARRANGEH) EXPORT(*ALL)";
	    try { 
	    serverCommand(command, false);
	    } catch (Exception e) {
		System.out.println("Exception on "+command); 
		throw e; 
	    }
	    try { 
	    serverCommand("QSYS/GRTOBJAUT OBJ("+       envLibrary+"/CHARRANGEH) OBJTYPE(*ALL) USER(*PUBLIC) AUT(*ALL) ", false);
	    } catch (Exception e) {
		System.out.println("Exception on "+command); 
		System.out.println("Continuing "); 
	    }


	  /* register the function  */
	    Statement s = null; 
	    try {
		String sqlString; 
		s = connection.createStatement();


		sqlString = " CREATE FUNCTION "+envLibrary+".CHARRANGEH (start VARCHAR(10), end VARCHAR(10)) RETURNS VARCHAR(4096) NO SQL LANGUAGE C EXTERNAL NAME '"+envLibrary+"/CHARRANGEH(CHARRANGEH)' PARAMETER STYLE DB2SQL"; 
		if (debug) System.out.println("JDJSTP.debug: " + sqlString);
		s.executeUpdate(sqlString);

	    }  finally {
		try {
		    if (s != null) s.close();
		} catch (Exception ex) {
		}
	    }
	    if (cmdConnWasNull) cmdConn=null; 

	} else { /* if procedure doesn't exist */  
	    if (debug) System.out.println("JDJSTP.debug: "+envLibrary+".CHARRANGEH exists"); 
	}
    } 





   public static String CURTIMESOURCE[] = {
      "#include <time.h>",
      "#include <stdlib.h>", 
      "",
      "int CURTIME ()",
      "{",
      "   return time(NULL);",
      "}",
      "" 
    }; 


    public static void assureCURTIMEisAvailable(Connection connection) throws Exception {

	/*
	 * check to see if the function 
	 * If it doesn't they we need to create it
	 */

	boolean functionExists = verifyFunctionExists(connection, ""+envLibrary+"", "CURTIME");
	if ( ! functionExists) {
	    createSourceFile(envLibrary,"CURTIME");

	    PreparedStatement ps = cmdConn.prepareStatement("insert into "+envLibrary+".CURTIME values(?, 0, ?)");

	    for (int i = 0; i < CURTIMESOURCE.length; i++) {
		ps.setInt(1, i+1);
		ps.setString(2, CURTIMESOURCE[i]);
		ps.executeUpdate();
	    }
	    /* Compile the program */
	    serverCommand("QSYS/CRTCMOD MODULE("+envLibrary+"/CURTIME) SRCFILE("+envLibrary+"/CURTIME) OUTPUT(*print) DBGVIEW(*ALL) OPTION(*SYSINCPATH *LOGMSG) SYSIFCOPT(*IFSIO)", false);
	    serverCommand("QSYS/CRTSRVPGM  SRVPGM(   "+envLibrary+"/CURTIME) MODULE( "+envLibrary+"/CURTIME) EXPORT(*ALL)", false);
	    serverCommand("QSYS/GRTOBJAUT OBJ("+       envLibrary+"/CURTIME) OBJTYPE(*ALL) USER(*PUBLIC) AUT(*ALL) ", false); 

	  /* register the function  */
	    Statement s = null; 
	    try {
		String sqlString; 
		s = connection.createStatement();



		sqlString = " CREATE FUNCTION "+envLibrary+".CURTIME () RETURNS INT NO SQL LANGUAGE C EXTERNAL NAME '"+envLibrary+"/CURTIME(CURTIME)' PARAMETER STYLE GENERAL"; 
		if (debug) System.out.println("JDJSTP.debug: " + sqlString);
		s.executeUpdate(sqlString);

	    }  finally {
		try {
		    if (s != null) s.close();
		} catch (Exception ex) {
		}
	    }

	} /* if procedure doesn't exist */  

    } 



   public static String FILETIMESOURCE[] = {
      "#include <sys/stat.h>",
      "",
      "int FILETIME (char * filename)",
      "{",
      "   struct stat buf;",
      "   int rc;", 
      "   rc = stat(filename,&buf);",
      "   if (rc == 0) {",
      "       return buf.st_mtime;",
      "   } else {",
      "       return -1; ",
      "   } ",
      "}",
      "" 
    }; 

    public static void createSourceFile(String library1, String filename) throws Exception { 
	serverCommand("QSYS/DLTF     "+library1+"/"+filename, true);
	serverCommand("QSYS/CRTSRCPF "+library1+"/"+filename +" RCDLEN(192)", true);
	serverCommand("QSYS/GRTOBJAUT OBJ("+library1+"/"+filename+") OBJTYPE(*file) USER(*PUBLIC) AUT(*ALL)", true); 
	serverCommand("QSYS/ADDPFM   "+library1+"/"+filename+" "+filename, true);
    }

    public static void createSourceFile(Connection c, String library1, String filename) throws Exception { 
	serverCommand(c,"QSYS/DLTF     "+library1+"/"+filename, true);
	serverCommand(c,"QSYS/CRTSRCPF "+library1+"/"+filename +" RCDLEN(192)", true);
	serverCommand(c,"QSYS/GRTOBJAUT OBJ("+library1+"/"+filename+") OBJTYPE(*file) USER(*PUBLIC) AUT(*ALL)", true); 
	serverCommand(c,"QSYS/ADDPFM   "+library1+"/"+filename+" "+filename, true);
    }


    public static void assureFILETIMEisAvailable(Connection connection) throws Exception {

	/*
	 * check to see if the function 
	 * If it doesn't they we need to create it
	 */

	boolean functionExists = verifyFunctionExists(connection, ""+envLibrary+"", "FILETIME");
	if ( ! functionExists) {
	    createSourceFile(envLibrary, "FILETIME");

	    PreparedStatement ps = cmdConn.prepareStatement("insert into "+envLibrary+".FILETIME values(?, 0, ?)");

	    for (int i = 0; i < FILETIMESOURCE.length; i++) {
		ps.setInt(1, i+1);
		ps.setString(2, FILETIMESOURCE[i]);
		ps.executeUpdate();
	    }
	    /* Compile the program */
	    serverCommand("QSYS/CRTCMOD MODULE("+envLibrary+"/FILETIME) SRCFILE("+envLibrary+"/FILETIME) OUTPUT(*print) DBGVIEW(*ALL) OPTION(*SYSINCPATH *LOGMSG) SYSIFCOPT(*IFSIO)", false);
	    serverCommand("QSYS/CRTSRVPGM  SRVPGM(   "+envLibrary+"/FILETIME) MODULE( "+envLibrary+"/FILETIME) EXPORT(*ALL)", false);
	    serverCommand("QSYS/GRTOBJAUT OBJ("+       envLibrary+"/FILETIME) OBJTYPE(*ALL) USER(*PUBLIC) AUT(*ALL) ", false); 

	  /* register the function  */
	    Statement s = null; 
	    try {
		String sqlString; 
		s = connection.createStatement();



		sqlString = " CREATE FUNCTION "+envLibrary+".FILETIME (VARCHAR(200)) RETURNS INT NO SQL LANGUAGE C EXTERNAL NAME '"+envLibrary+"/FILETIME(FILETIME)' PARAMETER STYLE GENERAL"; 
		if (debug) System.out.println("JDJSTP.debug: " + sqlString);
		s.executeUpdate(sqlString);

	    }  finally {
		try {
		    if (s != null) s.close();
		} catch (Exception ex) {
		}
	    }

	} /* if procedure doesn't exist */  



    } 


    public static String FILEEXISTSSOURCE[] = {
      "#include <unistd.h>",
      "",
      "int fileexists (char * filename)",
      "{",
      "   int rc = access(filename,F_OK);",
      "   if (rc == 0) {",
      "       return 1;",
      "   } else {",
      "       return 0; ",
      "   } ",
      "}",
      "" 
    }; 


    public static void assureFILEEXISTSisAvailable(Connection connection) throws Exception {

	/*
	 * check to see if the function 
	 * If it doesn't they we need to create it
	 */

	boolean functionExists = verifyFunctionExists(connection, ""+envLibrary+"", "FILEEXISTS");
	if ( ! functionExists) {
	    createSourceFile(envLibrary,"FILEEXISTS");

	    PreparedStatement ps = cmdConn.prepareStatement("insert into "+envLibrary+".FILEEXISTS values(?, 0, ?)");

	    for (int i = 0; i < FILEEXISTSSOURCE.length; i++) {
		ps.setInt(1, i+1);
		ps.setString(2, FILEEXISTSSOURCE[i]);
		ps.executeUpdate();
	    }
	    /* Compile the program */
	    serverCommand("QSYS/CRTCMOD MODULE("+envLibrary+"/FILEEXISTS) SRCFILE("+envLibrary+"/FILEEXISTS) OUTPUT(*print) DBGVIEW(*ALL) OPTION(*SYSINCPATH *LOGMSG) SYSIFCOPT(*IFSIO)", false);
	    serverCommand("QSYS/CRTSRVPGM  SRVPGM(   "+envLibrary+"/FILEEXISTS) MODULE( "+envLibrary+"/FILEEXISTS) EXPORT(*ALL)", false);
	    serverCommand("QSYS/GRTOBJAUT OBJ("+       envLibrary+"/FILEEXISTS) OBJTYPE(*ALL) USER(*PUBLIC) AUT(*ALL) ", false); 

	  /* register the function  */
	    Statement s = null; 
	    try {
		String sqlString; 
		s = connection.createStatement();



		sqlString = " CREATE FUNCTION "+envLibrary+".FILEEXISTS (VARCHAR(200)) RETURNS INT NO SQL LANGUAGE C EXTERNAL NAME '"+envLibrary+"/FILEEXISTS(fileexists)' PARAMETER STYLE GENERAL"; 
		if (debug) System.out.println("JDJSTP.debug: " + sqlString);
		s.executeUpdate(sqlString);

	    }  finally {
		try {
		    if (s != null) s.close();
		} catch (Exception ex) {
		}
	    }

	} /* if procedure doesn't exist */  



    }




 
     //////////////////////////////////////////////////////////////////////////////////////////////////
     //
     // JOB LOG FUNCTIONS
     // 
     //////////////////////////////////////////////////////////////////////////////////////////////////

    public static String STPJOBLOGSOURCE[] = {
     "#include <stdio.h>                                                      ",
     "#include <stdlib.h>                                                     ",
     "#include <string.h>                                                     ",
     "#include <qusrjobi.h>                                                   ",
     "                                                                        ",
     "#include <quscrtus.h>         /* create user space */                   ",
     "#include <qusec.h>            /* error return code */                   ",
     "#include <qusptrus.h>         /* user space pointer */                  ",
     "#include <qusgen.h>           /* user space generic */                  ",
     "#include <qmhljobl.h>         /* list job log api */                    ",
     "#include <qp0z1170.h>         /* Qp0zSystem */                          ",
     "#include <errno.h>                                                      ",
     "#include <fcntl.h>                                                      ",
     "                                                                        ",
     "#define MAXFILESIZE 15*1024*1024                                        ",
     "                                                                        ",
     "int main (int argc, char *argv[])                                       ",
     "{                                                                       ",
     "   /* The only argument we care about is the first one, */              ",
     "   /* which is the file name */                                         ",
     "                                                                        ",
     "    char jobId[40];                                                     ",
     "    char jdbcTraceName[40];                                             ",
     "    char * filename;                                                    ",
     "    Qwc_JOBI0100_t jobi;                                                ",
     "    Qwc_JOBI0400_t jobi400; ",
     "    char * dest;                                                        ",
     "    int i;                                                              ",
     "    struct stdOut_s * stdOut;                                           ",
     "    Qus_EC_t errorCode;                                                 ",
     "    int interactive = 0;                                                ",
     "    int count = 0;                                                      ",
     "    char buffer[4096];                                                  ",
     "    char * fileBuffer;                                                  ",
     "    pid_t pid;                                                          ",
     "    int fd;                                                             ",
     "    int rc;                                                             ",
     "    int ccsid;                                                          ",
     "    int savedCcsid = 0;                                               ",
     "                                                                        ",
     "    if (argc > 1 ) {                                                    ",
     "                                                                        ",
     "	if (strncmp(argv[1], \"stdout\", 6) == 0) {                             ",
     "	    interactive = 1;                                                  ",
     "	} else {                                                              ",
     "	    filename = argv[1];                                               ",
     "	}                                                                     ",
     "    } else {                                                            ",
     "	interactive = 1;                                                      ",
     "    }                                                                   ",
     "    if (interactive) {                                                  ",
     "	filename=\"/tmp/stpjoblog.out\";                                        ",
     "    }                                                                   ",
     "                                                                        ",
     "    pid = getpid();                                                     ",
     "                                                                        ",
     "    errorCode.Bytes_Provided = 0;                                       ",
     "                                                                        ",
     "    QUSRJOBI(&jobi,                                                     ",
     "             sizeof(jobi),                                              ",
     "             \"JOBI0100\",                                                ",
     "            /*12345678901234567890123456*/                              ",
     "             \"*                         \",                              ",
     "            /*1234567890123456*/                                        ",
     "             \"                \");                                       ",
     "                                                                        ",
     "    /*       char Job_Name[10]; */                                      ",
     "    /*       char User_Name[10]; */                                     ",
     "    /*       char Job_Number[6]; */                                     ",
     "    /* format NUMBER/USER/JOB */                                        ",
     "                                                                        ", 
     "    dest = jobId;                                                       ", 
     "    for (i = 0; i < 6; i++) {                                           ", 
     "       *dest = jobi.Job_Number[i];                                      ", 
     "       if (*dest != ' ') dest++;                                        ", 
     "    }                                                                   ", 
     "    *dest = '/'; dest++;                                                ", 
     "    for (i = 0; i < 10; i++) {                                          ", 
     "      *dest = jobi.User_Name[i];                                        ", 
     "       if (*dest != ' ') dest++;                                        ", 
     "    }                                                                   ", 
     "    *dest = '/'; dest++;                                                ", 
     "    for (i = 0; i < 10; i++) {                                          ", 
     "      *dest = jobi.Job_Name[i];                                         ", 
     "       if (*dest != ' ') dest++;                                        ", 
     "    }                                                                   ", 
     "    *dest = '\0';                                                       ", 
     "                                                                        ", 
     "    dest = jdbcTraceName;                                               ", 
     "    strcpy(dest, \"trace.\");                                             ", 
     "    dest+= strlen(dest);                                                ", 
     "    for (i = 0; i < 6; i++) {                                           ", 
     "       *dest = jobi.Job_Number[i];                                      ", 
     "       if (*dest != ' ') dest++;                                        ", 
     "    }                                                                   ", 
     "    *dest = '-'; dest++;                                                ", 
     "    for (i = 0; i < 10; i++) {                                          ", 
     "      *dest = jobi.User_Name[i];                                        ", 
     "       if (*dest != ' ') dest++;                                        ", 
     "    }                                                                   ", 
     "    *dest = '-'; dest++;                                                ", 
     "    for (i = 0; i < 10; i++) {                                          ", 
     "      *dest = jobi.Job_Name[i];                                         ", 
     "       if (*dest != ' ') dest++;                                        ", 
     "    }                                                                   ", 
     "    *dest = '\0';                                                       ",
     "",
     "    errorCode.Bytes_Provided = 0;                                       ",
     "    QUSRJOBI(&jobi400,        ",
     "             sizeof(jobi400),",
     "             \"JOBI0400\",",
     "             \"*                         \",",
     "             \"                \");",
     "    ccsid = jobi400.Coded_Char_Set_ID; ",
     "    if (ccsid == 290 || ccsid == 5026 ) {                              ",
     "        savedCcsid=ccsid;                                             ",
     "    sprintf(buffer, \"QSYS/CHGJOB JOB(%s) CCSID(37)\",  jobId);           ", 
     "    if (interactive) {                                                  ", 
     "	printf(\"%s\\n\", buffer);                                               ", 
     "    }                                                                   ", 
     "    rc=Qp0zSystem(buffer);                                              ", 
     "    }                                                                ", 
     "                                                                        ", 
     "    sprintf(buffer, \"QSYS/QSH CMD('RM %s')\",  filename);                     ", 
     "    if (interactive) {                                                  ", 
     "	printf(\"%s\\n\", buffer);                                            ", 
     "    }                                                                   ", 
     "    rc = Qp0zSystem(buffer);                                            ", 
     "                                                                        ", 
     "    sprintf(buffer, \"QSYS/QSH CMD('TOUCH -C 819 %s')\",  filename);           ", 
     "    if (interactive) {                                                  ", 
     "	printf(\"%s\\n\", buffer);                                               ", 
     "    }                                                                   ", 
     "    rc=Qp0zSystem(buffer);                                              ", 
     "                                                                        ", 
     "                                                                        ", 
     "    sprintf(buffer, \"QSYS/QSH CMD('SYSTEM DSPJOBLOG %s >> %s')\",             ", 
     "                    jobId, filename);                                   ", 
     "    if (interactive) {                                                  ", 
     "	printf(\"%s\\n\", buffer);                                               ", 
     "    }                                                                   ", 
     "    rc=Qp0zSystem(buffer);                                              ", 
     "                                                                        ", 
     "    sprintf(buffer,                                                     ", 
     "            \" QSH CMD('if [ -e %s ] ;  then cat %s >> %s; fi')\",        ",        
     "            jdbcTraceName,                                              ",        
     "            jdbcTraceName,                                              ",        
     "            filename);                                                  ",        
     "    if (interactive) {                                                  ",        
     "	printf(\"%s\\n\", buffer);                                               ",        
     "    }                                                                   ",        
     "    rc=Qp0zSystem(buffer);                                              ",        
     "                                                                        ",        
     "                                                                        ",        
     "    if (interactive) {                                                  ",        
     "	sprintf(buffer, \"QSYS/QSH CMD('echo argc is %d >> %s')\", argc, filename);  ",        
     "	if (interactive) {                                                    ",        
     "	    printf(\"%s\\n\", buffer);                                           ",        
     "	}                                                                     ",        
     "	rc=Qp0zSystem(buffer);                                                ",        
     "                                                                        ",        
     "    }                                                                   ",        
     "                                                                        ",        
     "    if (interactive) {                                                  ",        
     "        /* */                                                           ",        
     "        /* Read in the file (at least 10 meg) */                        ",        
     "        /* */                                                           ",        
     "	fileBuffer = malloc(MAXFILESIZE);                                     ",        
     "	fd = open(filename, O_RDONLY);                                        ",        
     "	if (fd > 0) {                                                         ",        
     "	    size_t bytesRead;                                                 ",        
     "	    bytesRead = read(fd, fileBuffer, MAXFILESIZE);                    ",        
     "	    close(fd);                                                        ",        
     "	} else {                                                              ",        
     "	    printf(\"Unable to open %s, errno = %d\\n\", filename, errno);       ",        
     "	}                                                                     ",        
     "                                                                        ",        
     "	if (interactive) {                                                    ",        
     "	    printf(\"Here is the read file\");                                  ",        
     "	    printf(\"%s\", fileBuffer);                                         ",        
     "	}                                                                     ",        
     "                                                                        ",        
     "    }                                                                   ",        
     "                                                                        ",        
     "                                                                        ",
     "    if (savedCcsid > 0 ) {                                              ",
     "      sprintf(buffer, \"QSYS/CHGJOB JOB(%s) CCSID(%d)\",jobId,savedCcsid);   ",
     "      if (interactive) {                                                ", 
     "	      printf(\"%s\\n\", buffer);                                      ", 
     "      }                                                                 ", 
     "      rc = Qp0zSystem(buffer);                                          ",
     "    }                                                                   ",
       
     "    return 0;                                                           ",        
     "}                                                                       ",        
     ""
    };



   /**
    * Make sure the STPJOBLOG stored procedure is defined and usable
    */
    public static void assureSTPJOBLOGisAvailable(Connection connection) throws Exception  {

        /*
         * check to see if the stored procedure exists.
         * If it doesn't they we need to create it
         */

        boolean procedureExists = verifyProcedureExists(connection, "QGPL", "STPJOBLOG");
	if ( ! procedureExists) {
	    createSourceFile("QGPL","STPJOBLOG");

	  PreparedStatement ps = cmdConn.prepareStatement("insert into QGPL.STPJOBLOG values(?, 0, ?)");

	 //
	 // Read the file using javaio and insert each file suing
	 // insert into ... values(lineNum, 0, lineString);
	 //
	  for (int i = 0; i < STPJOBLOGSOURCE.length; i++) {
	      ps.setInt(1, i+1);
	      ps.setString(2, STPJOBLOGSOURCE[i]);
	      ps.executeUpdate();
	  }
            /* Compile the program */
	  serverCommand("QSYS/CRTCMOD MODULE(QGPL/stpjoblog) SRCFILE(QGPL/STPJOBLOG) OUTPUT(*print) OPTION(*SYSINCPATH *LOGMSG)  DBGVIEW(*ALL)  SYSIFCOPT(*IFSIO)", false);
          serverCommand("QSYS/CRTPGM  PGM(   QGPL/stpjoblog) MODULE( QGPL/stpjoblog)", false);
	  serverCommand("QSYS/GRTOBJAUT OBJ(QGPL/STPJOBLOG) OBJTYPE(*ALL) USER(*PUBLIC) AUT(*ALL) ", false); 
	  /* register the stored procedure */
	  Statement s = null; 
	  try {
	      String sqlString; 
	      s = connection.createStatement();


	      sqlString = " drop procedure qgpl.stpjoblog";
	      if (debug) System.out.println("JDJSTP.debug: " + sqlString);
	      try {
		  s.executeUpdate(sqlString);
	      } catch (Exception e) {
		  if (debug) System.out.println("JDJSTP.debug: ignoring exception :"+e);
	      } 
	      
	      sqlString = " create procedure qgpl.stpjoblog(in path varchar(256))  language c parameter style general external name 'QGPL/STPJOBLOG'"; 
	      if (debug) System.out.println("JDJSTP.debug: " + sqlString);
	      s.executeUpdate(sqlString);
	      
	  }  finally {
	      try {
		  if (s != null) s.close();
	      } catch (Exception ex) {
	      }
	  }

	} /* if procedure doesn't exist */  

    }



    //////////////////////////////////////////////////////////////////////////////////////////////////
     //
     // JOB LOG FUNCTIONS
     // 
     //////////////////////////////////////////////////////////////////////////////////////////////////

    public static String STPJOBLOGXSOURCE[] = {
     "#include <stdio.h>                                                      ",
     "#include <stdlib.h>                                                     ",
     "#include <string.h>                                                     ",
     "                                                                        ",
     "#include <qp0z1170.h>         /* Qp0zSystem */                          ",
     "#include <errno.h>                                                      ",
     "#include <fcntl.h>                                                      ",
     "                                                                        ",
     "#define MAXFILESIZE 15*1024*1024                                        ",
     "                                                                        ",
     "int main (int argc, char *argv[])                                       ",
     "{                                                                       ",
     "   /* Two arguments.                                    */              ",
     "   /* First is the filename */                                          ",
     "   /* Second is the job name CHAR(26)*/                                 ",
     "                                                                        ",
     "    char jobId[40];                                                     ",
     "    char jdbcTraceName[40];                                             ",
     "    char * filename;                                                    ",
     "    char * dest;                                                        ",
     "    int i;                                                              ",
     "    struct stdOut_s * stdOut;                                           ",
     "    int interactive = 0;                                                ",
     "    int count = 0;                                                      ",
     "    char buffer[4096];                                                  ",
     "    char * fileBuffer;                                                  ",
     "    int fd;                                                             ",
     "    int rc;                                                             ",
     "    int ccsid;                                                          ",
     "                                                                        ",
     "    if (argc > 2 ) {                                                    ",
     "                                                                        ",
     "	    if (strncmp(argv[1], \"stdout\", 6) == 0) {                       ",
     "	       interactive = 1;                                               ",
     "	    } else {                                                          ",
     "	       filename = argv[1];                                            ",
     "	    }                                                                 ",
     "      strcpy(jobId, argv[2]);                                           ",
     "      jobId[28]='\\0';                                                   ",
     "    } else {                                                            ",
     "	interactive = 1;                                                      ",
     "    }                                                                   ",
     "    if (interactive) {                                                  ",
     "	filename=\"/tmp/stpjoblogx.out\";                                        ",
     "      }                                                                   ",
     "                                                                        ",
     "                                                                        ", 
     "                                                                        ", 
     "    sprintf(buffer, \"QSYS/QSH CMD('RM %s')\",  filename);                     ", 
     "    if (interactive) {                                                  ", 
     "	printf(\"%s\\n\", buffer);                                            ", 
     "    }                                                                   ", 
     "    rc = Qp0zSystem(buffer);                                            ", 
     "                                                                        ", 
     "    sprintf(buffer, \"QSYS/QSH CMD('TOUCH -C 819 %s')\",  filename);           ", 
     "    if (interactive) {                                                  ", 
     "	printf(\"%s\\n\", buffer);                                               ", 
     "    }                                                                   ", 
     "    rc=Qp0zSystem(buffer);                                              ", 
     "                                                                        ", 
     "                                                                        ", 
     "    sprintf(buffer, \"QSYS/QSH CMD('SYSTEM DSPJOBLOG %s >> %s')\",             ", 
     "                    jobId, filename);                                   ", 
     "    if (interactive) {                                                  ", 
     "	    printf(\"%s\\n\", buffer);                                        ", 
     "    }                                                                   ", 
     "    rc=Qp0zSystem(buffer);                                              ", 
     "                                                                        ", 
/* No need to dump trace */ 
     "                                                                        ",        
     "                                                                        ",        
     "    if (interactive) {                                                  ",        
     "	sprintf(buffer, \"QSYS/QSH CMD('echo argc is %d >> %s')\", argc, filename);  ",        
     "	    printf(\"%s\\n\", buffer);                                           ",        
     "	rc=Qp0zSystem(buffer);                                                ",        
     "                                                                        ",        
     "    }                                                                   ",        
     "                                                                        ",        
     "    if (interactive) {                                                  ",        
     "        /* */                                                           ",        
     "        /* Read in the file (at least 10 meg) */                        ",        
     "        /* */                                                           ",        
     "	fileBuffer = malloc(MAXFILESIZE);                                     ",        
     "	fd = open(filename, O_RDONLY);                                        ",        
     "	if (fd > 0) {                                                         ",        
     "	    size_t bytesRead;                                                 ",        
     "	    bytesRead = read(fd, fileBuffer, MAXFILESIZE);                    ",        
     "	    close(fd);                                                        ",        
     "	} else {                                                              ",        
     "	    printf(\"Unable to open %s, errno = %d\\n\", filename, errno);       ",        
     "	}                                                                     ",        
     "                                                                        ",        
     "	if (interactive) {                                                    ",        
     "	    printf(\"Here is the read file\");                                  ",        
     "	    printf(\"%s\", fileBuffer);                                         ",        
     "	}                                                                     ",        
     "                                                                        ",        
     "    }                                                                   ",        
     "                                                                        ",        
     "                                                                        ",
     "    return 0;                                                           ",        
     "}                                                                       ",        
     ""
    };



   /**
    * Make sure the STPJOBLOGX stored procedure is defined and usable
    */
    public static void assureSTPJOBLOGXisAvailable(Connection connection) throws Exception  {
      String sql = "unset"; 

	try { 
        /*
         * check to see if the stored procedure exists.
         * If it doesn't they we need to create it
         */
	  sql="verifyProcedureExists for QGPL/STPJOBLOGX"; 
        boolean procedureExists = verifyProcedureExists(connection, "QGPL", "STPJOBLOGX");
	if ( ! procedureExists) {
	    sql="javaMethod:createSourceFile(\"QGPL\",\"STPJOBLOGX\")";
	    createSourceFile(connection, "QGPL","STPJOBLOGX");

	    sql = "insert into QGPL.STPJOBLOGX values(?, 0, ?)"; 
 	  PreparedStatement ps = connection.prepareStatement(sql);

	 //
	 // Read the file using javaio and insert each file suing
	 // insert into ... values(lineNum, 0, lineString);
	 //
	  for (int i = 0; i < STPJOBLOGXSOURCE.length; i++) {
	      ps.setInt(1, i+1);
	      ps.setString(2, STPJOBLOGXSOURCE[i]);
	      sql = "Execute update of insert into QGPL.STPJOBLOGX values(?, 0, ?)";
	      ps.executeUpdate();
	  }
            /* Compile the program */
	  sql="QSYS/CRTCMOD MODULE(QGPL/stpjoblogx) SRCFILE(QGPL/STPJOBLOGX) OUTPUT(*print) OPTION(*SYSINCPATH *LOGMSG)  DBGVIEW(*ALL)  SYSIFCOPT(*IFSIO)";
	  serverCommand(connection, sql, false);
	  sql="QSYS/CRTPGM  PGM(   QGPL/stpjoblogx) MODULE( QGPL/stpjoblogx)";
          serverCommand(connection, sql, false);
	  sql = "QSYS/GRTOBJAUT OBJ(QGPL/STPJOBLOGX) OBJTYPE(*ALL) USER(*PUBLIC) AUT(*ALL) ";
	  serverCommand(connection, sql, false); 
	  /* register the stored procedure */
	  Statement s = null; 
	  try {
	      s = connection.createStatement();


	      sql = " drop procedure qgpl.stpjoblogx";
	      if (debug) System.out.println("JDJSTP.debug: " + sql);
	      try {
		  s.executeUpdate(sql);
	      } catch (Exception e) {
		  if (debug) System.out.println("JDJSTP.debug: ignoring exception :"+e);
	      } 
	      
	      sql = " create procedure qgpl.stpjoblogx(in path varchar(256), in job varchar(30))  language c parameter style general external name 'QGPL/STPJOBLOGX'"; 
	      if (debug) System.out.println("JDJSTP.debug: " + sql);
	      s.executeUpdate(sql);
	      
	  }  finally {
	      try {
		  if (s != null) s.close();
	      } catch (Exception ex) {
	      }
	  }

	} /* if procedure doesn't exist */  

	} catch (Exception e) {
	    System.out.println(" Rethrowing exception "+e+" caught processing "+sql);
	    throw(e); 
	} 
    }




    public static String getJdkString(int jdk, int vrm1) throws Exception { 
      switch (jdk) {
      case 190:  
          return ".jdk9"; 
      case 180:  
          return ".jdk1.8.0"; 
      case 170:  
         if (vrm1 >= 740) return null;
          return ".jdk1.7.0"; 
      case 160:
          if (vrm1 >= 730) return null; 
          return ".jdk1.6.0"; 
      case 150:  
          if (vrm1 >= 720) return null;
          return ".jdk1.5.0"; 
      case 142:
          if (vrm1 >= 720) return null; 
          return ".jdk1.4.2"; 
      case 140:
          if (vrm1 >= 720) return null; 
          return ".jdk1.4.0";
      case 0 :  return "";
      default:
        throw new Exception("Unrecognized jdk"+jdk); 
      }
    }
    public static String getExpectedOutputFile(
					       String testbase,
					       String inVariation,
					       StringBuffer possibleOutputFiles) throws Exception  {


    String expectedOutput;
    File checkFile; 
    String variationString="."+inVariation;
    if (inVariation.length() == 0) variationString = "";

    expectedOutput = exppath + "/" + testbase + variationString + ".rxp" + vrm
        + ".jdkXX";
    checkFile = new File(expectedOutput);
    if (!checkFile.exists()) { 
      possibleOutputFiles.append(" " + expectedOutput);
    }

    
    int vrms[] = { 770, 760, 750, 740, 730, 720, 710, 0 };
    int jdks[] = { 180,170,160,150,142,140,0}; 
    int currentJdk = JVMInfo.getJDK(); 
    
    
    for (int i = 0; i < vrms.length && !checkFile.exists(); i++) {
      if (vrm >= vrms[i]) {  /* current vrm greater than loop vrm */ 
        String vrmString = ""+vrms[i]; 
        if (vrms[i] == 0) { 
          vrmString=""; 
        }
        for (int j = 0; j < jdks.length && !checkFile.exists(); j++) {
          if (currentJdk >= jdks[j]) {
            String jdkString = getJdkString(jdks[j], vrms[i]); /*
                                                                * return .jdk
                                                                * string to use
                                                                */
            if (jdkString != null) { 
              // check for J9
              expectedOutput = exppath + "/" + testbase + variationString
                  + ".rxp" + vrmString + jdkString + ".j9";
              checkFile = new File(expectedOutput);
              if (!checkFile.exists()) {
                possibleOutputFiles.append(" " + expectedOutput);
              }
            if (!checkFile.exists()) {
              if (isToolbox) {
                // check for toolbox
                expectedOutput = exppath + "/" + testbase + variationString
                    + ".rxpt" + vrmString + jdkString;
                checkFile = new File(expectedOutput);
                if (!checkFile.exists()) {
                  possibleOutputFiles.append(" " + expectedOutput);
                }
              }
              if (!checkFile.exists()) {

                expectedOutput = exppath + "/" + testbase + variationString
                    + ".rxp" + vrmString + jdkString;
                checkFile = new File(expectedOutput);
                if (!checkFile.exists()) {
                  /* Do not list old releases as possible output files */
                  if (vrmString.equals("610") || vrmString.equals("550")
                      || vrmString.equals("540")
                      || jdkString.equals(".jdk1.4.0")
                      || jdkString.equals(".jdk1.4.2")
                      || (jdkString.indexOf("j9") > 0)) {
                    // Don't output the names
                  } else {
                    possibleOutputFiles.append(" " + expectedOutput);
                  }

                }
              } /* toolbox string does not exist */
            } /* j9 string does not exist */
            } /* jdkString not null */ 
          } /* current JRK not less than loop jdk */
        } /* for j -- jdks */
      } /* current vrm not less than loop vrm */
    } /* for i -- vrms */

    if (!checkFile.exists()) { 
      System.out.println("Did not find file:  Tried :"+possibleOutputFiles.toString());
    }
    return expectedOutput;
    }



     /**
      * Run a test program on the server and compare with the expected output
      * TODO.. 
      */ 
     public static void RunOnServer(String testPgm, String variation) throws Exception  {

	 String testbase =  testPgm.substring(0, testPgm.lastIndexOf("."));
	 String testext  =  testPgm.substring(testPgm.lastIndexOf(".")+1);
	 String output = "/tmp/"+testbase+"."+variation+".rxp";
	 String command = null; 

  	 //
	 // Run the program...
	 //

	 if (testext.equals("c")) {
	     if (usePase) {
		command = "rm "+output+";  touch -C 819 "+output+"; cd /QOpenSys"+nativeBaseDir+"/"+sourcepath+";   ./"+ testbase+" "+variation +" > "+output +" 2>&1";
	     } else {  
		command = "rm "+output+";  touch -C 819 "+output+"; cd "+nativeBaseDir+"/"+sourcepath+";   ./"+ testbase+" "+variation +" > "+output +" 2>&1";
	     }
		if (debug) {
		    System.out.println("JDJSTP.debug:    .....................................");
		    System.out.println("JDJSTP.debug:    Running c using serverShellCommand #1 : "+command);
		    System.out.println("JDJSTP.debug:    .....................................");
		}
		serverShellCommand(command, false);

		if (debug) {
		    System.out.println("JDJSTP.debug:    .....................................");
		    System.out.println("JDJSTP.debug:    command complete");
		    System.out.println("JDJSTP.debug:    .....................................");
		}

		getFileFromServer(output);
	 } else if (testext.equals("sh")) {
		command = "rm "+output+";  touch -C 819 "+output+"; cd "+nativeBaseDir+"/"+sourcepath+";   ./"+ testPgm+" "+variation +" > "+output +" 2>&1";

		File f1 = new File(output);
		if (f1.exists()) f1.delete();
		
		if (debug) {
		    System.out.println("JDJSTP.debug:    .....................................");
		    System.out.println("JDJSTP.debug:    Running sh : "+command);
		    System.out.println("JDJSTP.debug:    .....................................");
		}
		serverShellCommand(command, false);

		if (debug) {
		    System.out.println("JDJSTP.debug:    .....................................");
		    System.out.println("JDJSTP.debug:    command complete");
		    System.out.println("JDJSTP.debug:    .....................................");
		}

		if (!f1.exists()) { 
		  getFileFromServer(output);
		}
		
    if (v7r5 || v7r6 || v7r6plus) {
        getFileFromServer(output+".2");
        File f2 = new File(output+".2");
        long lengthF1 = f1.length(); 
        long lengthF2 = f2.length(); 
            if (debug) { 
              System.out.println("JDJSTP.debug: length "+f1+" = "+lengthF1);
              System.out.println("JDJSTP.debug: length "+f2+" = "+lengthF2);
            }
        if (lengthF2 > lengthF1) {
            if (debug) { 
              System.out.println("JDJSTP.debug: renaming "+f2+" to "+f1); 
            }
            f2.renameTo(f1); 
        }
    }
		

	 } else {
	     throw new Exception("File type "+testext+" not supported"); 
	 } 

	 StringBuffer possibleOutputFiles = new StringBuffer(); 
	String expectedOutput = getExpectedOutputFile(
						      testbase,
						      variation,
						      
						      possibleOutputFiles);


        //
        // compare the outputs
        //

        diff(output, expectedOutput, possibleOutputFiles.toString());
        rmf(output);
	serverShellCommand("rm "+output, false); 
     } /* RunOnServer */ 


     /**
      * Run a test program on the server and compare with the expected output
      * TODO.. 
      */ 
     public static void RunOnServer(String testPgm, String variation, String arg2) throws Exception  {

	 String testbase =  testPgm.substring(0, testPgm.lastIndexOf("."));
	 String testext  =  testPgm.substring(testPgm.lastIndexOf(".")+1);
	 String output = "/tmp/"+testbase+"."+variation+".rxp";
	 String command; 

  	 //
	 // Run the program...
	 //

	 if (testext.equals("c")) {
		command = "rm "+output+";  touch -C 819 "+output+"; cd "+nativeBaseDir+"/"+sourcepath+";   ./"+ testbase+" "+variation +" "+arg2+" > "+output +" 2>&1";

		if (debug) {
		    System.out.println("JDJSTP.debug:    .....................................");
		    System.out.println("JDJSTP.debug:    Running c serverShellCommand #2: "+command);
		    System.out.println("JDJSTP.debug:    .....................................");
		}
		serverShellCommand(command, false);

		if (debug) {
		    System.out.println("JDJSTP.debug:    .....................................");
		    System.out.println("JDJSTP.debug:    command complete");
		    System.out.println("JDJSTP.debug:    .....................................");
		}

		getFileFromServer(output);
	 } else if (testext.equals("sh")) {
		command = "rm "+output+";  touch -C 819 "+output+"; cd "+nativeBaseDir+"/"+sourcepath+";   ./"+ testPgm+" "+variation +" "+arg2+" > "+output +" 2>&1";
		if (v7r5 || v7r6 || v7r6plus)  { 
		    command = "rm "+output+";  touch -C 819 "+output+"; cd "+nativeBaseDir+"/"+sourcepath+";   ./"+ testPgm+" "+variation +" "+arg2+" | tee  "+output +".2  > "+output+" 2>&1";
		    command = "rm "+output+";  touch -C 819 "+output+"; cd "+nativeBaseDir+"/"+sourcepath+";   ./"+ testPgm+" "+variation +" "+arg2+" | tee  "+output +"  > "+output+".2 2>&1";
		}
		if (debug) {
		    System.out.println("JDJSTP.debug:    .....................................");
		    System.out.println("JDJSTP.debug:    Running sh : "+command);
		    System.out.println("JDJSTP.debug:    .....................................");
		}
		serverShellCommand(command, false);

		if (debug) {
		    System.out.println("JDJSTP.debug:    .....................................");
		    System.out.println("JDJSTP.debug:    command complete");
		    System.out.println("JDJSTP.debug:    .....................................");
		}

		getFileFromServer(output);
		if (v7r5 || v7r6 || v7r6plus) {
		    getFileFromServer(output+".2");
		    File f1 = new File(output);
		    File f2 = new File(output+".2");
		    long lengthF1 = f1.length(); 
        long lengthF2 = f2.length(); 
            if (debug) { 
              System.out.println("JDJSTP.debug: length "+f1+" = "+lengthF1);
              System.out.println("JDJSTP.debug: length "+f2+" = "+lengthF2);
            }
		    if (lengthF2 > lengthF1) {
		        if (debug) { 
		          System.out.println("JDJSTP.debug: renaming "+f2+" to "+f1); 
		        }
		        f2.renameTo(f1); 
		    }
		}

	 } else {
	     throw new Exception("File type "+testext+" not supported"); 
	 } 

	 StringBuffer possibleOutputFiles = new StringBuffer(); 
	 String expectedOutput = getExpectedOutputFile(
						       testbase,
						       variation,
						       possibleOutputFiles);


        diff(output, expectedOutput, possibleOutputFiles.toString(), "RunOnServer: "+command);
        rmf(output);
	serverShellCommand("rm "+output, false); 
     } /* RunOnServer */ 


    /**
      * Run a test program and compares the output with the expected output.
      * @param testpgm Name of the test program to run.  The test program should be the name
      * is the source program.   Currently, the only support type is a java program.
      * The java program should have be compiled previously by using linkClient()
      * @exception java.lang.Exception If an error occurs, an exception is thrown describing the error.
      **/
     public static void Run(String testpgm) throws Exception {

	 int jdk = JVMInfo.getJDK(); 
        String testbase =  testpgm.substring(0, testpgm.lastIndexOf("."));
        String testext  =  testpgm.substring(testpgm.lastIndexOf(".")+1);
	String command = null; 

        //
        // Run the program...
        //
        String output = "/tmp/"+testbase+".rxp";

        //
        // Determine which type of test is being run...
        //
	javaRunPath = sourcepath+sep+library;

        //
        // .java  Java program...
        //
	if (testext.equals("java")) {
	    command = "java "+getInheritedJavaOptions()+" "+testbase;

      if (nativeClient) {
        command = "java "+getInheritedJavaOptions()+" ";
        // Add the library if needed 
        // Use the right Jar file
        if (jdk == JVMInfo.JDK_16 ||
		   jdk == JVMInfo.JDK_17 ) {
          command+= " -classpath /QIBM/proddata/OS400/jt400/lib/java6/jt400.jar:"
              + javaRunPath
              + pathSep
              + "jars/java6/jt400.jar:.  -DJSTPPath=1.1 "
              + getInheritedJavaOptions() + " " + testbase;
	} else if (jdk == JVMInfo.JDK_18 || jdk == JVMInfo.JDK_19) {
	      command+= " -classpath /QIBM/proddata/OS400/jt400/lib/java8/jt400.jar:"
		+ javaRunPath
		+ pathSep
		+ "jars/java8/jt400.jar:.  -DJSTPPath=1.2 "
		+ getInheritedJavaOptions() + " " + testbase;


	} else if (jdk == JVMInfo.JDK_V11 )   {
	      command+= " -classpath /QIBM/proddata/OS400/jt400/lib/java9/jt400.jar:"+
		"/QIBM/ProdData/OS400/Java400/ext/sqlj_classes.jar:/qibm/proddata/os400/java400/ext/runtime.zip:/qibm/proddata/os400/java400/ext/translator.zip:"
		+ javaRunPath
		+ pathSep
		+ "jars/java9/jt400.jar:.  -DJSTPPath=1.3 "
		+ getInheritedJavaOptions() + " " + testbase;

        } else if (jdk == JVMInfo.JDK_V17 )   {
          command+= " -classpath /QIBM/proddata/OS400/jt400/lib/java9/jt400.jar:"+
            "/QIBM/ProdData/OS400/Java400/ext/sqlj_classes.jar:/qibm/proddata/os400/java400/ext/runtime.zip:/qibm/proddata/os400/java400/ext/translator.zip:"
            + javaRunPath
            + pathSep
            + "jars/java9/jt400.jar:.  -DJSTPPath=1.3 "
            + getInheritedJavaOptions() + " " + testbase;

        } else if (jdk == JVMInfo.JDK_V21 )   {
          command+= " -classpath /QIBM/proddata/OS400/jt400/lib/java9/jt400.jar:"+
            "/QIBM/ProdData/OS400/Java400/ext/sqlj_classes.jar:/qibm/proddata/os400/java400/ext/runtime.zip:/qibm/proddata/os400/java400/ext/translator.zip:"
            + javaRunPath
            + pathSep
            + "jars/java9/jt400.jar:.  -DJSTPPath=1.3 "
            + getInheritedJavaOptions() + " " + testbase;

	} else {
	    System.out.println("WARNING:  JDJSTPTestcase1:jdk='"+jdk+"' not recognized");

          command+= " -classpath /QIBM/proddata/OS400/jt400/lib/java8/jt400.jar:"
              + javaRunPath
              + pathSep
              + "jars/java8/jt400.jar:.  -DJSTPPath=1.4 "
              + getInheritedJavaOptions() + " " + testbase;

	}
      } else {
		
        if (JTOpenTestEnvironment.isWindows) {
          // Must escape classpath on windows
          // on 3/14/2011 change from " to ' to surround classpath
          if (jdk == JVMInfo.JDK_16 ||
		     jdk == JVMInfo.JDK_17) { 
            command = "java " + getInheritedJavaOptions()
                + "  -DJSTPPath=2.2 "
                + " -classpath '" + sep + "qibm" + sep + "proddata" + sep
                + "http" + sep + "public" + sep + "jt400/lib/jt400.jar"
                + pathSep + javaRunPath + pathSep + "jars/java6/jt400.jar"
                + pathSep + ".' " + testbase;
	  } else if (jdk == JVMInfo.JDK_18 ||
		     jdk == JVMInfo.JDK_19){

            command = "java " + getInheritedJavaOptions()
                + "  -DJSTPPath=2.2 "
                + " -classpath '" + sep + "qibm" + sep + "proddata" + sep
                + "http" + sep + "public" + sep + "jt400/lib/jt400.jar"
                + pathSep + javaRunPath + pathSep + "jars/java8/jt400.jar"
                + pathSep + ".' " + testbase;

          } else {

	    System.out.println("WARNING:  JDJSTPTestcase2:jdk='"+jdk+"' not recognized");

            command = "java " + getInheritedJavaOptions()
                + "  -DJSTPPath=2.2 "
                + " -classpath '" + sep + "qibm" + sep + "proddata" + sep
                + "http" + sep + "public" + sep + "jt400/lib/jt400.jar"
                + pathSep + javaRunPath + pathSep + "jars/java8/jt400.jar"
                + pathSep + ".' " + testbase;

          }

        } else {
          command = "java " + getInheritedJavaOptions()
              + "  -DJSTPPath=3 ";


          if (jdk == JVMInfo.JDK_16
              || jdk == JVMInfo.JDK_17) {
            command += " -classpath " + sep + "qibm" + sep + "proddata" + sep
                + "http" + sep + "public" + sep + "jt400/lib/jt400.jar"
                + pathSep + javaRunPath + pathSep + "jars/java6/jt400.jar"
                + pathSep + ". " + testbase;

	  } else if ((jdk == JVMInfo.JDK_18) || (jdk == JVMInfo.JDK_19)) {

            command += " -classpath " + sep + "qibm" + sep + "proddata" + sep
                + "http" + sep + "public" + sep + "jt400/lib/jt400.jar"
                + pathSep + javaRunPath + pathSep + "jars/java8/jt400.jar"
                + pathSep + ". " + testbase;

          } else {
	    System.out.println("WARNING:  JDJSTPTestcase3:jdk='"+jdk+"' not recognized");

            command += " -classpath " + sep + "qibm" + sep + "proddata" + sep
                + "http" + sep + "public" + sep + "jt400/lib/jt400.jar"
                + pathSep + javaRunPath + pathSep + "jars/java8/jt400.jar"
                + pathSep + ". " + testbase;
          }
        }
      }

	    if (debug | javaExecDebug) {
		System.out.println("JDJSTP.debug:   .....................................");
		System.out.println("JDJSTP.debug:    Running java1 : "+command);
		System.out.println("JDJSTP.debug:    .....................................");
	    }
	    Process process = exec(command);
	    showProcessOutput(process, output, JDJSTPOutputThread.ENCODING_UNKNOWN);
	    process.waitFor();

	    if (debug | javaExecDebug) {
		System.out.println("JDJSTP.debug:    .....................................");
		System.out.println("JDJSTP.debug:    command complete");
		System.out.println("JDJSTP.debug:    .....................................");
	    }

	} else {
	    if (testext.equals("clp")) {
	      command = sourcepath+"/"+testpgm; 
                JDSQL400.run(command, output, globalUserId,  PasswordVault.decryptPasswordLeak(globalEncryptedPassword, "JDJSTPTestcase.rau.7") );
	    } else if (testext.equals("jdbc")) {
	      command = sourcepath+"/"+testpgm; 
	      runJdbcClient(command, output, globalUserId, globalEncryptedPassword );
	    } else if (testext.equals("c")) {
		throw new Exception("TODO"); 
	    } else {
		throw new Exception("Cannot run test "+testpgm+".  Extension "+testext+" not recognized");
	    }
	}

	StringBuffer possibleOutputFiles = new StringBuffer(); 
	String expectedOutput = getExpectedOutputFile(
						      testbase,
						      "",
						      possibleOutputFiles);
        //
        // compare the outputs
        // diff will throw an exception
	// It will throw a java.lang.UnsupportedClassVersionError if
	// java.lang.UnsupportedClassVersionError is seen in the output 
        //

        diff(output, expectedOutput, possibleOutputFiles.toString(), "Run: "+command);

        rmf(output);

     }

    /**
     * Removes a file
     * @param filename Name of a file to remove
     **/
    public static void rmf(String filename) {
	try {
	    File file = new File(filename);
            file.delete();
	} catch (Exception e) {
	}
    }

    /**
     * Check to see if a file exists.  Throws exception if file does not exist
     * @param filename Name of file to see if exists
     * @exception java.io.FileNotFoundException Thrown if the file does not exist
     **/

    public static void checkExists(String filename) throws java.io.FileNotFoundException {
        File file = new File ( filename);
	if (!file.exists()) {
	    throw new java.io.FileNotFoundException(filename);
	}
     }

    public static boolean isPortUp(int portNumber) {
      try { 
        Socket s = new Socket();
        s.setSoTimeout(10); 
        
        InetSocketAddress socketAddress = 
          new InetSocketAddress("localhost", portNumber); 
        s.connect(socketAddress, 10);
        s.close(); 
        return true; 
      } catch (Exception e) {
        return false; 
      }
    }
    /**
     * Run a program by calling cliJobRun
     */ 

    public static void cliJobRun(String output, String program, String args) throws Exception {

      boolean serverMode = (args.indexOf("SERVERMODE=ON") >= 0);
      int checkPort; 

      if (serverMode) {
	  try {
	      cliJobRunSMOut.write( newLineBytes, 0, 1 );
	      cliJobRunSMIn.read( cliJobRunInBuffer, 0, 1); 
	  } catch (Exception e) {
	      cliJobRunSMPort = 0; 
	  }
	  checkPort  = cliJobRunSMPort; 
      } else { 
	  try {
	      cliJobRunOut.write( newLineBytes, 0, 1 );
	      cliJobRunIn.read( cliJobRunInBuffer, 0, 1); 
	  } catch (Exception e) {
	      cliJobRunPort = 0; 
	  }
	  checkPort  = cliJobRunPort; 
      }



	if (checkPort == 0) {
	    int runPort = 7382; 
	    // Make sure the program is available
	    link("CLIJobRun.c");

	    // 
	    // Use different ports for server mode and non-server mode
	    //

	    Random random = new Random();

	    runPort = 7382 + random.nextInt(1000);
	    while(isPortUp(runPort)) {
		runPort++; 
                if (debug) System.out.println("Bumping port number to "+runPort); 
	    } 
            if (debug) System.out.println("Using port number "+runPort); 

	    String command;
	    if (usePase) {
		command =  "/QOpenSys/" + nativeBaseDir+"/"+sourcepath+"/CLIJobRun "+runPort;
	    } else { 
		command =  nativeBaseDir+"/"+sourcepath+"/CLIJobRun "+runPort;
	    }

	    if (debug) {
		System.out.println("JDJSTP.debug:    .....................................");
		System.out.println("JDJSTP.debug:   Starting server c : "+command);
		System.out.println("JDJSTP.debug:    .....................................");
	    }


	    if (serverMode) {
		cliJobRunSMPort = runPort ;
	    // Start the server in the background.
		exec(command);

		if (debug) System.out.println("Server started. Connect to it "); 

		cliJobRunSMSocket = new Socket();
		cliJobRunSMSocket.setSoTimeout(cliJobRunTimeout); 
		if (debug) System.out.println("Cli job sm socket set");

		InetSocketAddress socketAddress = 
		  new InetSocketAddress("localhost", cliJobRunSMPort);
		boolean connected = false; 
		int attempts = 0 ;
		int sleepTime=1000; 
		while (!connected && attempts < 10) {
		    try {
			cliJobRunSMSocket.connect(socketAddress, 6000);
			cliJobRunSMIn = new DataInputStream(cliJobRunSMSocket.getInputStream());
			cliJobRunSMOut = new DataOutputStream(cliJobRunSMSocket.getOutputStream());

			if (debug) System.out.println("Connected "); 
			connected = true; 
		    } catch (java.net.ConnectException cex) {
			cex.printStackTrace(System.out); 
			attempts++;
			System.out.println("Warning: Could not connect "+cex+" attempts = "+attempts+" at "+ (new java.util.Date(System.currentTimeMillis()))+" sleeping for "+sleepTime ); 
			cliJobRunSMSocket = new Socket();
			if (debug) System.out.println("Cli job sm socket set"); 
			Thread.sleep(sleepTime);
			// Use upgrading sleep time to allow time for debugging 
			sleepTime = sleepTime * 3  / 2;
			

			cliJobRunSMSocket.setSoTimeout(cliJobRunTimeout); 

		    }
		}


	    } else { 
		cliJobRunPort = runPort ;
	    // Start the server in the background.
		exec(command);

		if (debug) System.out.println("Server started. Connect to it "); 

		if (cliJobRunSocket != null); cleanupCliJobRunSocket(); 
		cliJobRunSocket = new Socket();
		if (debug) System.out.println("cli job socket created "); 
		cliJobRunSocket.setSoTimeout(cliJobRunTimeout); 

		InetSocketAddress socketAddress = 
		  new InetSocketAddress("localhost", cliJobRunPort);
		boolean connected = false; 
		int attempts = 0 ;
		int sleepTime = 1000; 
		boolean dumpInfo = false; 
		while (!connected && attempts < 20) {
		    try {
			cliJobRunSocket.connect(socketAddress, 6000);
			connected = true;

			cliJobRunIn = new DataInputStream(cliJobRunSocket.getInputStream());
			cliJobRunOut = new DataOutputStream(cliJobRunSocket.getOutputStream());

			if (debug) System.out.println("Connected "); 

		    } catch (java.net.ConnectException cex) {
			
			    attempts++;

			if (debug || dumpInfo )  {
			    cex.printStackTrace(System.out); 
			    System.out.println("Could not connect "+cex+" attempts = "+attempts+" sleepTime="+sleepTime+" at "+ (new java.util.Date(System.currentTimeMillis())) );
			}

			if ((dumpInfo == false) && (attempts == 10)) {
			    dumpInfo = true;
			    attempts--; 
			} 
			Thread.sleep(sleepTime);
			// Use upgrading sleep time to allow time for debugging 
			sleepTime = sleepTime * 3  / 2;

			if (debug) System.out.println("cli job socket created ");
			// Try again to connect using a new socket  
			cliJobRunSocket = new Socket();
			cliJobRunSocket.setSoTimeout(cliJobRunTimeout); 

		    }
		}

	    }

	}


	// Send a message to the port and wait for the reply.
	String cliJobCommand = output+"|"+program+"|"+args+"\n"; 

        if (debug) System.out.println("Sending message "+cliJobCommand); 

	byte[] outBytes = cliJobCommand.getBytes("US-ASCII");
	if (serverMode) {
	    cliJobRunSMOut.write( outBytes, 0, outBytes.length );
	    cliJobRunSMOut.flush();

	} else { 
	    cliJobRunOut.write( outBytes, 0, outBytes.length );
	    cliJobRunOut.flush();
	}
	byte[] inBuffer = new byte[4095];

	int readBytes = 0;
	boolean reading = true;
	while (reading) {
	    try {
		if (serverMode) {
		    readBytes = cliJobRunSMIn.read(inBuffer);
		} else { 
		    readBytes = cliJobRunIn.read(inBuffer);
		}
                // Check for EOF 
                if (readBytes < 0) {
                  readBytes=4; 
                  inBuffer[0]='E'; 
                  inBuffer[1]='O'; 
                  inBuffer[2]='F'; 
                  inBuffer[3]='\n'; 
                }
		reading = false; 
	    } catch (java.net.SocketTimeoutException ste) {
		if (debug) System.out.println("Timeout Exception at "+ 
                        (new java.util.Date(System.currentTimeMillis())));
	    } 
	}
	String inString = new String(inBuffer, 0,readBytes, "US-ASCII");
	if (debug) System.out.println("Read "+inString); 
	while ( inString.indexOf("\n") <0 ) {
	  reading = true; 
	  while (reading) {
	    try {
		if (serverMode) {
		    readBytes = cliJobRunSMIn.read(inBuffer);
		} else { 
		    readBytes = cliJobRunIn.read(inBuffer);
		}
	      reading = false; 
	    } catch (java.net.SocketTimeoutException ste) {
	      if (debug) System.out.println("Timeout Exception at "+ (new java.util.Date(System.currentTimeMillis()))); 
	    } 
	  }
	    String moreString = new String(inBuffer, 0,readBytes, "US-ASCII");
	    inString += moreString;
            if (debug) System.out.println("Read "+inString); 

	} 


        if (debug) System.out.println("Got message "+inString); 


	
    } 


     /**
      * Run a test program and compares the output with the expected output.
      * @param testpgm Name of the test program to run.  The test program should be the name.
      * is the source program.   Currently, the only support type is a java program.
      * The java program should have be compiled previously by using RTest.l()
      * @param variation The number of the variation to run.  This is only valid if the test program
      * accepts the first argument as the variation number.
      * @param javaOptions The options to pass to the java program
      * @param timeout     The time to wait in milliseconds.  If 0, then no timeout.
      * @exception java.lang.Exception If an error occurs, an exception is thrown describing the error.
      **/
     public static void Run(String testpgm, String variation) throws Exception {
        Run(testpgm, variation, "");
     }
     public static void Run(String testpgm, String variation, String javaOptions) throws Exception {
         Run(testpgm, variation, javaOptions, 0);
     }
     public static void Run(String testpgm, String variation, String javaOptions, long timeout) throws Exception {
	 Run(testpgm, variation, javaOptions, timeout, ""); 
     }
     public static void Run(String testpgm, String variation, String javaOptions, long timeout, String arg2) throws Exception {
	 Run(testpgm, variation, javaOptions, timeout, arg2, null); 
     }
     public static void Run(String testpgm, String variation, String javaOptions, long timeout, String arg2, String javaArgs) throws Exception {

	 int dotIndex =  testpgm.lastIndexOf(".");
	 if (dotIndex < 0) {
	     throw new Exception("Error "+testpgm+" does not contain a . (i.e. .java, .clp, ..."); 
	 }
        String testbase =  testpgm.substring(0,dotIndex);
        String testext  =  testpgm.substring(dotIndex+1);
	      String command; 

        //
        // Run the program...
        //
        String output = "/tmp/"+testbase+"."+variation+".rxp";

        //
        // Determine which type of test is being run...
        //

        //
        // .java  Java program...
        //
        String preCommands = null; 
	if (testext.equals("java")) {
	    // Fix the javaOptions
	    
	    int sysenvIndex = javaOptions.indexOf("-SYSENV=");
	    while (sysenvIndex >= 0) {
	       if (preCommands == null) preCommands = ""; 
	       int spaceIndex = javaOptions.indexOf(' ', sysenvIndex); 
	       if (spaceIndex > 0) { 
	           String sysenv = javaOptions.substring(sysenvIndex + 8, spaceIndex);
	           preCommands += "export -s "+sysenv+"; "; 
	           javaOptions = javaOptions.substring(0,sysenvIndex)+
	                 javaOptions.substring(spaceIndex); 
	       }
	       sysenvIndex = javaOptions.indexOf("-SYSENV="); 
	    }


	    int rmvsysenvIndex = javaOptions.indexOf("-RMVSYSENV=");
	    while (rmvsysenvIndex >= 0) {
	       if (preCommands == null) preCommands = ""; 
	       int spaceIndex = javaOptions.indexOf(' ', rmvsysenvIndex); 
	       if (spaceIndex > 0) { 
	           String rmvsysenv = javaOptions.substring(rmvsysenvIndex + 11, spaceIndex);
		   /* Note.. I am unable to get this to work, but I am leaving this here */ 
	           preCommands += "unset "+rmvsysenv+"; ";
		   preCommands += "export -s "+rmvsysenv+"=UNABLE_TO_UNSET; ";
	           javaOptions = javaOptions.substring(0,rmvsysenvIndex)+
	                 javaOptions.substring(spaceIndex); 
	       }
	       rmvsysenvIndex = javaOptions.indexOf("-RMVSYSENV="); 
	    }


	    javaRunPath = sourcepath+sep+library;


	    int jdk = JVMInfo.getJDK(); 
	    command = "java "+javaOptions+" "+getInheritedJavaOptions()+" "+ testbase+" "+variation;
	    if (nativeClient) {
		command = "cd "+funcpath+" && export -s QIBM_JAVA_STDIO_CONVERT=Y && export -s CLASSPATH=\"$CLASSPATH\"\":/QIBM/ProdData/OS400/Java400/ext/runtime.zip:/QIBM/ProdData/OS400/Java400/ext/translator.zip:/QIBM/ProdData/OS400/Java400/ext/db2routines_classes.jar:"+funcpath+"\" && " + command ;
		if (jdk == JVMInfo.JDK_16 ||jdk == JVMInfo.JDK_17 )  {
		   command = "java -classpath /QIBM/proddata/OS400/jt400/lib/java6/jt400.jar:"+javaRunPath+pathSep+"jars/java6/jt400.jar:. -DJSTPPath=4.1 "+javaOptions+" "+getInheritedJavaOptions()+" "+testbase+" "+variation;

		} else if ((jdk == JVMInfo.JDK_18 ) || (jdk == JVMInfo.JDK_19 ))  {
		    command = "java -classpath /QIBM/proddata/OS400/jt400/lib/java8/jt400.jar:"+javaRunPath+pathSep+"jars/java8/jt400.jar:. -DJSTPPath=4.1 "+javaOptions+" "+getInheritedJavaOptions()+" "+testbase+" "+variation;

		} else if ((jdk == JVMInfo.JDK_V11 ) )  {
		    command = "java -classpath /QIBM/proddata/OS400/jt400/lib/java9/jt400.jar:"+javaRunPath+pathSep+"jars/java9/jt400.jar:. -DJSTPPath=4.1 "+javaOptions+" "+getInheritedJavaOptions()+" "+testbase+" "+variation;

		} else if ((jdk == JVMInfo.JDK_V17 ) )  {
		    command = "java -classpath /QIBM/proddata/OS400/jt400/lib/java9/jt400.jar:"+javaRunPath+pathSep+"jars/java9/jt400.jar:. -DJSTPPath=4.1 "+javaOptions+" "+getInheritedJavaOptions()+" "+testbase+" "+variation;

                } else if ((jdk == JVMInfo.JDK_V21 ) )  {
                  command = "java -classpath /QIBM/proddata/OS400/jt400/lib/java9/jt400.jar:"+javaRunPath+pathSep+"jars/java9/jt400.jar:. -DJSTPPath=4.1 "+javaOptions+" "+getInheritedJavaOptions()+" "+testbase+" "+variation;

		} else {
		    System.out.println("WARNING:  JDJSTPTestcase4:jdk='"+jdk+"' not recognized");

		    command = "java -classpath /QIBM/proddata/OS400/jt400/lib/java8/jt400.jar:"+javaRunPath+pathSep+"jars/java8/jt400.jar:. -DJSTPPath=4.1 "+javaOptions+" "+getInheritedJavaOptions()+" "+testbase+" "+variation;

		} 

	    } else {
		if (JTOpenTestEnvironment.isWindows ) {
		    // Windows is run through a shell so we need to escape the pathSep (;)
		    // If JDBC 4.0 and later use the java6 version of the jar
		    // 
		    
		    if (jdk == JVMInfo.JDK_16 || jdk == JVMInfo.JDK_17) {
			command = "java "+javaOptions+" -DJSTPPath=5.1 "+getInheritedJavaOptions()+" -classpath '"+ sep+"qibm"+ sep+ "proddata"+ sep+ "http"+ sep+ "public"+ sep+ "jt400"+ sep+ "lib"+ sep+ "jt400.jar"+ pathSep+javaRunPath+pathSep+"jars"+sep+"java6"+sep+"jt400.jar"+pathSep+".' "+testbase+" "+variation;
		    } else if (( jdk == JVMInfo.JDK_18) || ( jdk == JVMInfo.JDK_19)) {
			command = "java "+javaOptions+" -DJSTPPath=5.1 "+getInheritedJavaOptions()+" -classpath '"+ sep+"qibm"+ sep+ "proddata"+ sep+ "http"+ sep+ "public"+ sep+ "jt400"+ sep+ "lib"+ sep+ "jt400.jar"+ pathSep+javaRunPath+pathSep+"jars"+sep+"java8"+sep+"jt400.jar"+pathSep+".' "+testbase+" "+variation;
		    } else {
			
			  System.out.println("WARNING:  JDJSTPTestcase5:jdk='"+jdk+"' not recognized");

			command = "java "+javaOptions+" -DJSTPPath=5.1 "+getInheritedJavaOptions()+" -classpath '"+ sep+"qibm"+ sep+ "proddata"+ sep+ "http"+ sep+ "public"+ sep+ "jt400"+ sep+ "lib"+ sep+ "jt400.jar"+ pathSep+javaRunPath+pathSep+"jars"+sep+"java8"+sep+"jt400.jar"+pathSep+".' "+testbase+" "+variation;

		    } 



		} else {
		    if  (jdk == JVMInfo.JDK_16 || jdk == JVMInfo.JDK_17  ) { 
			command = "java "+javaOptions+" -DJSTPPath=6 "+getInheritedJavaOptions()+" -classpath "+javaRunPath + pathSep+ sep+"qibm"+ sep+ "proddata"+ sep+ "http"+ sep+ "public"+ sep+ "jt400"+ sep+ "lib"+ sep+ "jt400.jar"+pathSep+"jars"+sep+"java6"+sep+"jt400.jar"+pathSep+". "+testbase+" "+variation;

		    } else if  ((jdk == JVMInfo.JDK_18)  || (jdk == JVMInfo.JDK_19) ){

			command = "java "+javaOptions+" -DJSTPPath=6 "+getInheritedJavaOptions()+" -classpath "+javaRunPath + pathSep+ sep+"qibm"+ sep+ "proddata"+ sep+ "http"+ sep+ "public"+ sep+ "jt400"+ sep+ "lib"+ sep+ "jt400.jar"+pathSep+"jars"+sep+"java8"+sep+"jt400.jar"+pathSep+". "+testbase+" "+variation;

		    } else {
			System.out.println("WARNING:  JDJSTPTestcase6:jdk='"+jdk+"' not recognized");
			command = "java "+javaOptions+" -DJSTPPath=6 "+getInheritedJavaOptions()+" -classpath "+javaRunPath + pathSep+ sep+"qibm"+ sep+ "proddata"+ sep+ "http"+ sep+ "public"+ sep+ "jt400"+ sep+ "lib"+ sep+ "jt400.jar"+pathSep+"jars"+sep+"java8"+sep+"jt400.jar"+pathSep+". "+testbase+" "+variation;

		    } 
		}



	    }
	    if (javaArgs != null) {
		command = command + " "+javaArgs; 
	    } 
	    if (preCommands != null ) {
	      command = preCommands + command; 
	    }
	    if (debug | javaExecDebug) {
		System.out.println("JDJSTP.debug:    .....................................");
		System.out.println("JDJSTP.debug:    Running java2 : "+command);
		System.out.println("JDJSTP.debug:    .....................................");
	    }
	    Process process = exec(command);
	    showProcessOutput(process, output, JDJSTPOutputThread.ENCODING_UNKNOWN ); process.waitFor();
	    if (debug | javaExecDebug) {
		System.out.println("JDJSTP.debug:    .....................................");
		System.out.println("JDJSTP.debug:    command complete");
		System.out.println("JDJSTP.debug:    .....................................");
	    }

	} else {
	    if (testext.equals("c")) {
		if (nativeClient) {

		    if (useCliJobRun) {
                        command="CLIJobRun "+nativeBaseDir+"/"+sourcepath+"/"+testbase+" "+variation + " "+arg2;

			cliJobRun(output, nativeBaseDir+"/"+sourcepath+"/"+testbase, variation + " "+arg2);

		    } else { 
			command = "rm "+output+";  touch -C 819 "+output+"; cd "+nativeBaseDir+"/"+sourcepath+";   ./"+ testbase+" "+variation +" "+arg2+" 2>&1"; 
			if (usePase) {
			    command =  "/QOpenSys"+nativeBaseDir+"/"+sourcepath+"/"+testbase+" "+variation + " "+arg2;
			} else { 
			    command =  nativeBaseDir+"/"+sourcepath+"/"+testbase+" "+variation + " "+arg2;
			}
			if (debug) {
			    System.out.println("JDJSTP.debug:    .....................................");
			    System.out.println("JDJSTP.debug:    Running c using exec1 : "+command);
			    System.out.println("JDJSTP.debug:    .....................................");
			}
			Process process = exec(command);
			// Save the process output 
			showProcessOutput(process, output, JDJSTPOutputThread.ENCODING_UNKNOWN ); process.waitFor();
			if (debug) {
			    System.out.println("JDJSTP.debug:    .....................................");
			    System.out.println("JDJSTP.debug:    command complete");
			    System.out.println("JDJSTP.debug:    .....................................");
			}
		    }

		} else { 
                  
                  if (JTOpenTestEnvironment.isWindows) {
                        command =  nativeBaseDir+"/"+sourcepath+"/"+testbase+" "+variation + " "+arg2;
                        if (debug) {
                            System.out.println("JDJSTP.debug:    .....................................");
                            System.out.println("JDJSTP.debug:    Running c using exec2: "+command);
                            System.out.println("JDJSTP.debug:    .....................................");
                        }
                        Process process = exec(command);
                        // Save the process output 
                        showProcessOutput(process, output, JDJSTPOutputThread.ENCODING_UNKNOWN ); 
                        process.waitFor();
                        if (debug) {
                            System.out.println("JDJSTP.debug:    .....................................");
                            System.out.println("JDJSTP.debug:    command complete");
                            System.out.println("JDJSTP.debug:    .....................................");
                        }
                    
                  } else { 
		    throw new Exception("Running c programs on client not permitted -- use serverCommand "); 
                  }

		}

	    } else {
		throw new Exception("Cannot run test "+testpgm+".  Extension "+testext+" not recognized");
	    }
	}

	StringBuffer possibleOutputFiles = new StringBuffer(); 
	String expectedOutput = getExpectedOutputFile(
						      testbase,
						      variation,
						      possibleOutputFiles);


        //
        // compare the outputs
        //

        diff(output, expectedOutput, possibleOutputFiles.toString(), "Run5: "+ command );
        rmf(output);

     }


     /**
      * Run a test program as a specific users and compares the output with the expected output.
      * @param testpgm Name of the test program to run.  The test program should be the name
      * is the source program.   Currently, the only support type is a clp program.
      * The java program should have be compiled previously by using linek
      * @param userid
      * @param password
      * @exception java.lang.Exception If an error occurs, an exception is thrown describing the error.
      **/
     public static void RasUser(String testpgm, String userId, String password) throws Exception {
       RasUser(testpgm, userId, PasswordVault.getEncryptedPassword(password)); 
     }
     public static void RasUser(String testpgm, String userId, char[] encryptedPassword) throws Exception {


        String testbase =  testpgm.substring(0, testpgm.lastIndexOf("."));
        String testext  =  testpgm.substring(testpgm.lastIndexOf(".")+1);
	String command; 

        //
        // Run the program...
        //
        String output = "/tmp/"+testbase+".rxp";
        
        if (JTOpenTestEnvironment.isWindows) {
          output = "C:"+output.replace("/","\\"); 
        }
        //
        // Determine which type of test is being run...
        //

        //
        // .java  Java program...
        //
	if (testext.equals("clp")) {
	    command = sourcepath+"/"+testpgm; 
	    JDSQL400.run(command, output, userId, PasswordVault.decryptPasswordLeak(encryptedPassword, "JDJSTPTestcase.rau.1") );
	} else if (testext.equals("jdbc")) {
    command = sourcepath+"/"+testpgm; 
    runJdbcClient(command, output, userId,  encryptedPassword); 
	} else {
	    throw new Exception("Cannot run test "+testpgm+".  Extension "+testext+" not recognized");
	}


        //
        // Check to see if a Release specific output exists (ends with .rxpVRM)
        //
	StringBuffer possibleOutputFiles = new StringBuffer(); 
	String expectedOutput = getExpectedOutputFile(
						      testbase,
						      "", 
						      possibleOutputFiles);



        //
        // compare the outputs
        // diff will throw an exception
        //

        diff(output, expectedOutput, possibleOutputFiles.toString(), "RasUser "+command );

        rmf(output);

     }


    static Hashtable<String, String> jdbcReplaceHashtable = new Hashtable<String,String>();  
    
    protected static void setJdbcReplace(String from, String to) {
      jdbcReplaceHashtable.put(from, to); 
    }
    
    static String jdbcReplace(String line) {
      Enumeration<String> keys = jdbcReplaceHashtable.keys(); 
      while (keys.hasMoreElements()) {
        String key = (String) keys.nextElement(); 
        String value = (String) jdbcReplaceHashtable.get(key);
        line = line.replace(key,value); 
      }
      return line; 
    }

     private static void runJdbcClient(String command, String output,
        String userId, char[] encryptedPassword) throws SQLException, IOException  {
      InputStream in; 
      PrintStream out;
      
      StringBuffer sb = new StringBuffer(); 

      BufferedReader br = new BufferedReader(JDRunit.loadReaderResource(command)); 
      String line = br.readLine();
      while (line != null) { 
        line = jdbcReplace(line); 
        sb.append(line); 
        sb.append("\n"); 
        line = br.readLine();
      }
      br.close(); 
      
      
      
      in = new StringBufferInputStream(sb.toString());
      out = new PrintStream(output); 
      
      Main jdbcClient = new Main(globalURL, userId, PasswordVault.decryptPasswordLeak(encryptedPassword, "JDJSTPTestcase")); 
         
          
      jdbcClient.go(in, out);
      
    }

    /**
     * Compares two string.
     * @param stringOne Base string
     * @param stringTwo String containing possible regular expresssion
     *
     * The second string can have ".*" characters -- meaning match anything..
     *
     * return true if they are the same
     * @exception java.lang.Exception Throwns an exception if the files are not identical
     **/
     public static boolean regexSame(String s, String regex)  {

	 //if (debug) {
	 //    System.out.println("Enter regexSame("+s+", "+regex+")");
	 //}
         //
         // Find the first occurence of .* in regex
         //

	 int dotStarIndex = regex.indexOf(".*");

	 if (dotStarIndex == -1) {
	     //
	     // no .*, just match exactly
  	     //
	     return s.equals(regex);
	 }

	 if (dotStarIndex > 0) {
	     //
	     // make sure everything matches up to the .*
	     //
	     String sSubstring = null;
	     // Input string too short -- return false
	     if (dotStarIndex > s.length()) {
	       return false; 
	     }
	     try {
		  sSubstring = s.substring(0,dotStarIndex);
	     } catch (StringIndexOutOfBoundsException ex1) {
                 //
                 // The input string could be too short, if so, return false
                 //
                 return false;
	     }
	     String rSubstring = regex.substring(0,dotStarIndex);
	     if ( !sSubstring.equals(rSubstring)) {
		 //
		 // it doesn't match, so return false
		 // otherwise keep going to match the start of the string
		 //
		 return false;
	     }
	
	 }

         //
         // At this point, the two strings are identical up to the .*
         // Now move up the rest of the string and compare to the rest of the regex string
         //

	 String newRegex = regex.substring(dotStarIndex+2);
         //
         // if there is nothing after the .* then the answer is true
         //
	 if (newRegex.length() == 0) {
	     return true;
	 }

	 boolean theSame = false;
	 for (int i = dotStarIndex; !theSame && i < s.length(); i++) {
	     try {
		 theSame = regexSame(s.substring(i), newRegex);
	     } catch (StringIndexOutOfBoundsException ex2) {
                 //
                 // If s is too small this will occur
                 //
                 theSame = false;
	     }
	 }

	 return theSame;


     }





    public static void addLine(String line, String[] contextLines) {
	for ( int i = 0; i < contextLines.length; i++) {
           String temp = contextLines[i];
	   contextLines[i] = line;
	   line = temp; 
	} 
    } 
    public static void reset(String[] contextLines) {
	for (int i = 0; i < contextLines.length; i++) {
	    contextLines[i] = null;
	}	
    } 

    public static void  showContextLines(java.io.PrintStream out, String prefix, String[] contextLines) {
	for (int i = contextLines.length -1; i >=0; i--) {
	    if (contextLines[i] != null) {
		out.println(prefix+"\""+contextLines[i]+"\""); 
	    } 
	} 
    } 



    /**
     * Compares two files.
     * @param filenameOne Name of a file to compare
     * @param filenameTwo Name of the file to compare against
     *
     * The second file can have lines which have ".*" characters -- meaning match anything..
     * @param possibleOutputFiles 
     *
     * @exception java.lang.Exception Throwns an exception if the files are not identical
     **/
    static String[] diffIgnores = {
	"CPD0170:  Program QJVACHKP in library QSYS not found.",
	"CPF0001:  Error found on CALL command."
    };
    
    static String diffIgnoresSource = "JDJSTPTestcase.java"; 
    
    public static void setDiffIgnores(String[] newIgnores) {
      StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace(); 
      if (stackTrace.length > 2) {
        setDiffIgnores(newIgnores, stackTrace[1].getClassName()+ " or "+stackTrace[2].getClassName());
      } else {
        setDiffIgnores(newIgnores,"unknown"); 
      }
    }
    
    public static void setDiffIgnores(String[] newIgnores, String newSource) {
      
	     diffIgnores = newIgnores; 
	     diffIgnoresSource = newSource; 
    } 
    public static void diff(String filenameOne, String filenameTwo, String possibleOutputFiles) throws Exception {
	diff(filenameOne, filenameTwo, possibleOutputFiles, null); 
    }

  public static void diff(String filenameOne, String filenameTwo,
      String possibleOutputFiles, String command) throws Exception {

    if (sep != '/') {
      filenameOne = filenameOne.replace('/', sep);
      filenameTwo = filenameTwo.replace('/', sep);
      if (filenameOne.charAt(0) == sep) {
        filenameOne = "C:" + filenameOne;
      }
      if (filenameTwo.charAt(0) == sep) {
        filenameTwo = "C:" + filenameTwo;
      }

    }

    boolean different = false;
    String unsupportedClassException = null;
    int differences = 0;

    String paddedFilenameOne;
    String paddedFilenameTwo;
    String contextLines[] = new String[CONTEXT_LINE_COUNT];
    String beginningLines[] = new String[CONTEXT_LINE_COUNT];
    int beginningLinesCount = 0;
    boolean beginningLinesPrinted = false;
    boolean diffLinesPrinted = false;
    //
    // Change the file names so that they are the same length.
    //
    if (filenameOne.length() > filenameTwo.length()) {
      paddedFilenameOne = filenameOne;
      char[] pad = new char[filenameOne.length() - filenameTwo.length()];
      for (int i = 0; i < pad.length; i++) {
        pad[i] = ' ';
      }
      paddedFilenameTwo = new String(pad) + filenameTwo;

    } else {
      if (filenameOne.length() < filenameTwo.length()) {
        paddedFilenameTwo = filenameTwo;
        char[] pad = new char[filenameTwo.length() - filenameOne.length()];
        for (int i = 0; i < pad.length; i++) {
          pad[i] = ' ';
        }
        paddedFilenameOne = new String(pad) + filenameOne;

      } else {
        paddedFilenameOne = filenameOne;
        paddedFilenameTwo = filenameTwo;
      }
    }

    //
    // Open the files and compare line by line... Show at most 10 differences
    //
    if (debug) {
       System.out.println("JDJSTP.debug: diffing " + filenameOne + " "
          + filenameTwo);
    }
    BufferedReader readerOne = null;
    try {
      readerOne = new BufferedReader(JDRunit.loadReaderResource(filenameOne));
    } catch (Exception e) {
      System.out.println("Exception opening " + filenameOne);
      System.out.println("Command was " + command);
      throw e;
    }
    BufferedReader readerTwo = null;
    try {
      readerTwo = new BufferedReader(JDRunit.loadReaderResource(filenameTwo));
    } catch (Exception e) {
      System.out.println("Exception opening " + filenameTwo);
      System.out.println("Command was " + command);
      readerOne.close();
      throw e;
    }

    StringBuffer sbOne = new StringBuffer();
    sbOne.append(filenameOne+"\n------------------------------------------------\n"); 
    StringBuffer sbTwo = new StringBuffer();
    sbTwo.append(filenameTwo+"\n------------------------------------------------\n"); 
    String sOneRaw = "";
    String sTwoRaw = "";
    String sOne = "dummy";
    String sTwo = "dummy";
    //
    // readline returning null indicates end of file
    //
    boolean showSep = true;
    while (sOne != null && sTwo != null ) {
      boolean retry = true;
      sOne = readerOne.readLine();
      sOneRaw = sOne;
      sTwo = readerTwo.readLine();
      sTwoRaw = sTwo;

      while (retry) {

        StringBuffer comparesStringBuffer = new StringBuffer("====\n");
        // Ignore the global ignore lines
        if (diffIgnores != null) {
          int i = 0;
          while (sOne != null && i < diffIgnores.length) {
            /*
             * comparesStringBuffer.append("Comparing "+sOne+" to ["+i+"]"+
             * diffIgnores[i]+"\n");
             */
            if (regexSame(sOne, diffIgnores[i])) {
              sbOne.append(sOneRaw + "\n");
              sbTwo.append("-->Found diffIgnore " + diffIgnores[i] + "\n");
              sOne = readerOne.readLine();
              sOneRaw = sOne;
              i = 0;
            } else {
              i++;
            }
          }
        }

        retry = false;
        if (sOne != null && sTwo != null) {
          if (sOne.indexOf("java.lang.UnsupportedClassVersionError") >= 0) {
            unsupportedClassException = sOne;
          }
          if (sOne.indexOf("@IGNORE_REST") >= 0
              || sTwo.indexOf("@IGNORE_REST") >= 0) {
            sOne = null;
            sTwo = null;
          } else {
            boolean ignoreOne = false;
            boolean ignoreTwo = false;
            if (sOne.indexOf("IGNORE_EXTRA:") == 0) {
              sOne = sOne.substring(13);
              ignoreOne = true;
            }
            if (sTwo.indexOf("IGNORE_EXTRA:") == 0) {
              sTwo = sTwo.substring(13);
              ignoreTwo = true;
            }

            if ((sOne != null) && (sOne.compareTo(sTwo) != 0)) {
              if (!regexSame(sOne, sTwo)) {
                if (ignoreOne) {
                  sbOne.append(sOneRaw + "\n");
                  sbTwo.append(sTwoRaw + "\n");
                  sOne = readerOne.readLine();
                  sOneRaw = sOne;
                  retry = true;
                } else if (ignoreTwo) {
                  sbOne.append(sOneRaw + "\n");
                  sbTwo.append(sTwoRaw + "\n");
                  sTwo = readerTwo.readLine();
                  sTwoRaw = sTwo;
                  retry = true;
                } else {
                  // Check that they do not differ just by spaces
                  String a = sOne.trim();
                  String b = null;
                  if (sTwo != null)
                    b = sTwo.trim();

                  if ((a.compareTo(b) != 0) && !regexSame(a, b)) {
                    differences++;
                    different = true;
                    if (differences < DIFFERENCES_COUNT) { 
                    if (showSep)
                      System.out
                          .println("JDJSTP.debug: -------------------------------------------");
                    if (!beginningLinesPrinted) {
                      System.out
                          .println("JDJSTP.debug: ----- beginning lines ---------------------");
                      beginningLinesPrinted = true;
                      showContextLines(System.out, "JDJSTP.debug: "
                          + paddedFilenameOne + ": ", beginningLines);
                      System.out.println("JDJSTP.debug: " + paddedFilenameOne
                          + ": ...");
                    }

                    if (!diffLinesPrinted) {
                      diffLinesPrinted = true;
                      if (diffIgnores != null) {
                        System.out
                            .println("JDJSTP.debug: ----- diffIgnore lines ------- source = "+diffIgnoresSource);
                        for (int i = 0; i < diffIgnores.length; i++) {
                          System.out
                              .println("JDJSTP.debug:  " + diffIgnores[i]);
                        }
                      }

                    }

                    if (showSep)
                      System.out
                          .println("JDJSTP.debug: -------------------------------------------");
                    showContextLines(System.out, "JDJSTP.debug: "
                        + paddedFilenameOne + ": ", contextLines);
                    // comparesStringBuffer cannot be null at this point.
                    if (/* comparesStringBuffer != null && */
                    (comparesStringBuffer.length() > 0)) {
                      System.out.println(comparesStringBuffer.toString());
                    }
                    System.out.println("JDJSTP.debug: " + paddedFilenameOne
                        + ": \"" + sOne + "\"");
                    System.out.println("JDJSTP.debug: " + paddedFilenameTwo
                        + ": \"" + sTwo + "\"");
                    }
                    // Lines were different
                    sbOne.append("</pre><p style=\"color:red;\">"+cleanHtml(sOneRaw) + "</p>\n<pre>");
                    sbTwo.append("</pre><p style=\"color:red;\">"+cleanHtml(sTwoRaw) + "</p>\n<pre>");
                    reset(contextLines);
                    showSep = false;
                  } else {
                    // Lines were the same
                    sbOne.append(sOneRaw+"\n"); 
                    sbTwo.append(sTwoRaw+"\n"); 
                  }
                }
              } else {
                // Lines were the same
                sbOne.append(sOneRaw+"\n"); 
                sbTwo.append(sTwoRaw+"\n"); 

                if (beginningLinesCount < CONTEXT_LINE_COUNT) {
                  addLine(sOne, beginningLines);
                  beginningLinesCount++;
                }
                addLine(sOne, contextLines);
                showSep = true;
              } // regexDifferent
            } else {
              // Lines were the same
              sbOne.append(sOneRaw+"\n"); 
              sbTwo.append(sTwoRaw+"\n"); 
              
              if (beginningLinesCount < CONTEXT_LINE_COUNT) {
                addLine(sOne, beginningLines);
                beginningLinesCount++;
              }
              addLine(sOne, contextLines);
              showSep = true;
            } // sOne
          } // ignore rest
        } // both not null
      }
    } // while

    if (sOne == null && sTwo != null) {
      System.out.println("JDJSTP.debug: "
          + "-------------------------------------------");
      System.out.println("JDJSTP.debug: " + "File 1 " + filenameOne
          + " is empty, yet File 2 " + filenameTwo + " still contains data");
      different = true;
    }

    if (sTwo == null && sOne != null) {
      System.out.println("JDJSTP.debug: "
          + "-------------------------------------------");
      System.out.println("JDJSTP.debug: " + "File 2 " + filenameTwo
          + " is empty, yet File 1 " + filenameOne + " still contains data");
      different = true;
    }

    readerOne.close();
    readerTwo.close();
    if (unsupportedClassException != null && different) {
      throw new UnsupportedClassVersionError(unsupportedClassException);
    }

    if (different) {
      System.out.println("JDJSTP.debug: "
          + "-------------------------------------------");
      if (command != null) {
        System.out.println("JDJSTP.debug: Running " + command);
        System.out.println("JDJSTP.debug: "
            + "-------------------------------------------");
      }
      // Make sure the line is flushed before throwing exception
      System.out.flush();

      //
      // Create a differences html file
      // 
      String filename="NOTSET";
      try { 
      String baseScriptName = getBaseScriptName(filenameOne); 
      String sep = File.separator; 
      filename = "ct"+sep+"out"+sep+"compare";
      File directory = new File(filename); 
      if (!directory.exists()) directory.mkdir();
      filename = filename+sep+baseScriptName+".html";
      PrintWriter pw = new PrintWriter(new FileWriter(filename)); 
      pw.println(""+
"          \n"+
"          <html>\n"+
"          <head>\n"+
"          <script>\n"+
"\n"+
"          function SyncScroll(divid) {\n"+
"            var div1 = document.getElementById(\"div1\");\n"+
"            var div2 = document.getElementById(\"div2\");\n"+
"            if (divid==\"div1\") {\n"+
"              div2.scrollTop = div1.scrollTop;\n"+
"            }\n"+
"            else {\n"+
"              div1.scrollTop = div2.scrollTop;\n"+
"            }\n"+
"          }\n"+
"          </script>\n"+
"\n"+
"          <style> \n"+
"          div {\n"+
"              border: 2px solid;\n"+
"              padding: 20px; \n"+
"              resize: both;\n"+
"              overflow: auto;\n"+
"          }\n"+
"\n"+
"          * {\n"+
"              box-sizing: border-box;\n"+
"          }\n"+
"\n"+
"          /* Create two equal columns that floats next to each other */\n"+
"          .column {\n"+
"              float: left;\n"+
"              width: 50%;\n"+
"              padding: 10px;\n"+
"              height: 100%;\n"+
"          }\n"+
"\n"+
"          .row {\n"+
"             width: 100%;\n"+
"             height: 80%;\n"+
"          }\n"+
"          /* Clear floats after the columns */\n"+
"          .row:after {\n"+
"              content: \"\";\n"+
"              display: table;\n"+
"              clear: both;\n"+
"          }\n"+
"          </style>\n"+
"          </style>\n"+
"          </head>\n"+
"          <body>\n"+
"          <h1> Comparision for "+ baseScriptName +"</h1>\n"+
"\n"+
"           <div class=\"row\">\n"+
"            <div class=\"column\" id=\"div1\" onscroll=\"SyncScroll('div1')\">\n"+
"          <pre>\n"); 
      pw.println(sbOne.toString());
      pw.println(
"          </pre>\n"+
"          </div>\n"+
"            <div class=\"column\"  id=\"div2\" onscroll=\"SyncScroll('div2')\"> \n" +
"          <pre>\n"); 
      pw.println(sbTwo.toString()); 
      pw.println(
          "          </pre>\n"+
          "          </div>\n"+
              "          </div> \n"+
              "\n"+
              "\n"+
              "\n"+
              "          </body>\n"+
          "          </html>\n"); 
      
      pw.close(); 
      
      } catch (Exception e) { 
         System.out.println("Error creating compare file "+filename); 
         e.printStackTrace(System.out) ;
      }
      
      
      throw new Exception("Files '" + filenameOne + "' '" + filenameTwo
          + "' are different.  Possible output files are "
          + possibleOutputFiles);
    }
  }

  private static String cleanHtml(String raw) {
    raw = raw.replace("<","&lt;"); 
    return raw;
  }

  private static String getBaseScriptName(String filename) {
    int lastSlashIndex = filename.lastIndexOf('/'); 
    if (lastSlashIndex < 0) {
      lastSlashIndex = filename.lastIndexOf('\\'); 
    }
    if (lastSlashIndex > 0) {
      filename = filename.substring(lastSlashIndex+1); 
    }
    int rxpIndex = filename.indexOf(".rxp"); 
    if (rxpIndex > 0) {
      filename = filename.substring(0,rxpIndex); 
      
    }
    return filename;
  }

    /**
      * Testing code
      */

     public static void main(String args[]) throws Exception  {
	 try {
	   globalURL = args[0]; 
	   globalUserId = args[1]; 
	
	   globalEncryptedPassword = PasswordVault.getEncryptedPassword(args[2]); 
	   try { 
	       Class.forName("com.ibm.as400.access.AS400JDBCDriver");
	   } catch (Exception e) {
	       if (debug) System.out.println("com.ibm.as400.access.AS400JDBCDriver not found "); 
	   }
	   try { 
	       Class.forName("com.ibm.db2.jdbc.app.DB2Driver");
	   } catch (Throwable e) {
	       if (debug) System.out.println("com.ibm.db2.jdbc.app.DB2Driver not found ");
	   }
	   if (debug) System.out.println("JDJSTPTestcase.main: getting connection for "+globalURL+","+globalUserId); 
	   cmdConn = DriverManager.getConnection(globalURL, globalUserId, 
	       PasswordVault.decryptPasswordLeak(globalEncryptedPassword, "JSTPTestcase.main"));
	 } catch (Exception e) {

           System.out.println("Exception caught connecting to ");
	   e.printStackTrace(System.out);
	   System.out.println("Usage:  java JDJSTPTestcase <URL> <userid> <password>");
           System.out.println("        <URL> like jdbc:as400://<systemname");
           System.out.println("        <URL> like jdbc:db2://<systemname");

	   System.exit(1); 
	 }
	 try {
	     System.out.println("RUNNING TEST"); 
	     checkSetup();
	     // System.out.println("checking updateServerFile"); 
	     // updateServerFile("test/JDJSTPTestcase.java", "PHYF", "/tmp/JDJSTPTestcase.java");
		 // serverShellCommand("mkdir /tmp/test", true); 
		 // updateBinaryServerFile("test/JDJSTPTestcase.class", "/tmp/test/JDJSTPTestcase.class");
		 System.out.println("checking l");

		 String sourcepath1 = "stp/t/test";
		 String exppath1 =    "stp/t/exp";

		 setSourcepath(sourcepath1);
		 setExppath(exppath1);
		 link("hi.java");

		 assureSTPJOBLOGisAvailable(cmdConn);
		 assureSTPJOBLOGXisAvailable(cmdConn);
		 assureFILEEXISTSisAvailable(cmdConn);

		 String filename; 
		 filename = "/tmp";
		 System.out.println("serverFileExists("+filename+") ="+serverFileExists(filename)); 
		 filename = "/tmpx";
		 System.out.println("serverFileExists("+filename+") ="+serverFileExists(filename)); 
		 filename = "/QIBM";
		 System.out.println("serverFileExists("+filename+") ="+serverFileExists(filename)); 
		 assureFILETIMEisAvailable(cmdConn);
		 assureCURTIMEisAvailable(cmdConn); 

	     String serverFile;
	     String localFile;
	     serverFile = "test/JDJSTPTestcase.class"; 
	     localFile  = "test/JDJSTPTestcase.java"; 
	     serverFileNewer(serverFile, localFile); 


	 } catch (Exception e) {
	    e.printStackTrace(System.out);
	 } 

     }


     /* returns the sql statements needed to set the JVM used */
     /* by java stored procedures */ 

     static String[] getSetJVMSQL() throws Exception  {
	 return getSetJVMSQL(System.getProperty("java.home")); 
     }

     static String[] getSetJVMSQL(String javaHome) throws Exception  {


	 // J9 JVMS
	 if (javaHome.indexOf("/QOpenSys/QIBM/ProdData/JavaVM/") >= 0) {
	     int jreIndex = javaHome.indexOf("/jre");
	     if (jreIndex > 0) {
		 javaHome = javaHome.substring(0,jreIndex); 
	     }
	     String[] answer = {
		 "call QSYS2.QCMDEXC('ADDENVVAR ENVVAR(JAVA_HOME) VALUE(''"+javaHome+"'')')",
		 "call QSYS2.QCMDEXC('CHGENVVAR ENVVAR(JAVA_HOME) VALUE(''"+javaHome+"'')')",
	     };
	     return answer; 
	 }



	 if ((javaHome.indexOf("C:") >= 0) ||
	     (javaHome.indexOf("/jvm") >= 0)) {
	     // On windows or linux... Pick a matching 32 bit version
	     int jdk = JVMInfo.getJDK(); 
	     if (jdk == JVMInfo.JDK_16) {

		     String[] answer = {
			 "call QSYS.QCMDEXC('ADDENVVAR ENVVAR(JAVA_HOME) VALUE(''/QOpenSys/QIBM/ProdData/JavaVM/jdk60/32bit'')                                            ', 000000090.00000)",
			 "call QSYS.QCMDEXC('CHGENVVAR ENVVAR(JAVA_HOME) VALUE(''/QOpenSys/QIBM/ProdData/JavaVM/jdk60/32bit'')                                            ', 000000090.00000)",
		     };
		     return answer; 
	     } else if (jdk == JVMInfo.JDK_17) {

		     String[] answer = {
			 "call QSYS.QCMDEXC('ADDENVVAR ENVVAR(JAVA_HOME) VALUE(''/QOpenSys/QIBM/ProdData/JavaVM/jdk70/32bit'')                                            ', 000000090.00000)",
			 "call QSYS.QCMDEXC('CHGENVVAR ENVVAR(JAVA_HOME) VALUE(''/QOpenSys/QIBM/ProdData/JavaVM/jdk70/32bit'')                                            ', 000000090.00000)",
		     };
		     return answer; 

	     } else if (jdk == JVMInfo.JDK_18) {

		     String[] answer = {
			 "call QSYS.QCMDEXC('ADDENVVAR ENVVAR(JAVA_HOME) VALUE(''/QOpenSys/QIBM/ProdData/JavaVM/jdk80/32bit'')                                            ', 000000090.00000)",
			 "call QSYS.QCMDEXC('CHGENVVAR ENVVAR(JAVA_HOME) VALUE(''/QOpenSys/QIBM/ProdData/JavaVM/jdk80/32bit'')                                            ', 000000090.00000)",
		     };
		     return answer; 
	     } else if (jdk == JVMInfo.JDK_19) {

		     String[] answer = {
			 "call QSYS.QCMDEXC('ADDENVVAR ENVVAR(JAVA_HOME) VALUE(''/QOpenSys/QIBM/ProdData/JavaVM/jdk90/64bit'')                                            ', 000000090.00000)",
			 "call QSYS.QCMDEXC('CHGENVVAR ENVVAR(JAVA_HOME) VALUE(''/QOpenSys/QIBM/ProdData/JavaVM/jdk90/64bit'')                                            ', 000000090.00000)",
		     };
		     return answer; 
	     } else if (jdk == JVMInfo.JDK_V11) {

		     String[] answer = {
			 "call QSYS.QCMDEXC('ADDENVVAR ENVVAR(JAVA_HOME) VALUE(''/QOpenSys/QIBM/ProdData/JavaVM/jdk11/64bit'')                                            ', 000000090.00000)",
			 "call QSYS.QCMDEXC('CHGENVVAR ENVVAR(JAVA_HOME) VALUE(''/QOpenSys/QIBM/ProdData/JavaVM/jdk11/64bit'')                                            ', 000000090.00000)",
		     };
		     return answer; 
             } else if (jdk == JVMInfo.JDK_V17) {

               String[] answer = {
                   "call QSYS.QCMDEXC('ADDENVVAR ENVVAR(JAVA_HOME) VALUE(''/QOpenSys/QIBM/ProdData/JavaVM/jdk17/64bit'')                                            ', 000000090.00000)",
                   "call QSYS.QCMDEXC('CHGENVVAR ENVVAR(JAVA_HOME) VALUE(''/QOpenSys/QIBM/ProdData/JavaVM/jdk17/64bit'')                                            ', 000000090.00000)",
               };
               return answer; 
             } else if (jdk == JVMInfo.JDK_V21) {

               String[] answer = {
                   "call QSYS.QCMDEXC('ADDENVVAR ENVVAR(JAVA_HOME) VALUE(''/QOpenSys/QIBM/ProdData/JavaVM/jdk21/64bit'')                                            ', 000000090.00000)",
                   "call QSYS.QCMDEXC('CHGENVVAR ENVVAR(JAVA_HOME) VALUE(''/QOpenSys/QIBM/ProdData/JavaVM/jdk21/64bit'')                                            ', 000000090.00000)",
               };
               return answer; 
	     }


	 }
	 
	throw new Exception("Error javaHome of '"+javaHome+" not valid"); 


     }

 
  
     public static void setupUserProfile(String testUser, char[] encryptedTestPass) throws Exception {
        String testPass = PasswordVault.decryptPasswordLeak(encryptedTestPass, "JDJSTPTestcase.setupUserProfile");
	 System.out.println("Setting up user profile "+testUser);
	 String command = ""; 

	 try {
	     command = "QSYS/CRTUSRPRF USRPRF("+testUser+") PASSWORD("+testPass+") TEXT('Java stored procedures test')  ACGCDE(514648897)  CCSID(37)"; 
	     mirrorServerCommand(command, IGNORE_ERROR);

	     command = "QSYS/GRTOBJAUT OBJ(QSYS/"+testUser+") OBJTYPE(*USRPRF) USER("+testUser+") AUT(*ALL)"; 
	     mirrorServerCommand(command, THROW_ERROR);
	     command = "QSYS/CHGUSRPRF USRPRF("+testUser+") PASSWORD(AB12DE34)"; 
	     mirrorServerCommand(command,IGNORE_REMOTE_ERROR );
	     command = "QSYS/CHGUSRPRF USRPRF("+testUser+")  STATUS(*ENABLED)  PASSWORD("+testPass+")"; 
	     mirrorServerCommand(command,THROW_ERROR);
	     command = "QSYS/GRTOBJAUT OBJ(QSYS/RSTLIB) OBJTYPE(*CMD) USER("+testUser+") AUT(*USE)"; 
	     mirrorServerCommand(command, THROW_ERROR);
	     command = "QSYS/GRTOBJAUT OBJ(QSYS/QDFTOWN) OBJTYPE(*USRPRF) USER("+testUser+") AUT(*ADD)";
	     mirrorServerCommand(command, THROW_ERROR);
	     command = "QSYS/GRTOBJAUT OBJ(QSYS/RSTLIB) OBJTYPE(*CMD) USER("+testUser+") AUT(*USE)"; 
	     mirrorServerCommand(command, THROW_ERROR);
	     command = "QSYS/GRTOBJAUT OBJ(QSYS/STRDBG) OBJTYPE(*CMD) USER("+testUser+") AUT(*USE)"; 
	     mirrorServerCommand(command, THROW_ERROR);

	     command = "QSYS/GRTOBJAUT OBJ(QSYS/QSQJSON2) OBJTYPE(*SRVPGM) USER("+testUser+") AUT(*ALL)"; 
	     mirrorServerCommand(command, IGNORE_ERROR);


	     command = "QSYS/GRTOBJAUT OBJ(QSYS/QSQJSON) OBJTYPE(*SRVPGM) USER("+testUser+") AUT(*ALL)"; 
	     mirrorServerCommand(command, IGNORE_ERROR);

	     command = "QSYS/GRTOBJAUT OBJ(QSYS/QSQSILE) OBJTYPE(*SRVPGM) USER("+testUser+") AUT(*ALL)"; 
	     mirrorServerCommand(command, IGNORE_ERROR);

	     command = "QSYS/CHGFCNUSG FCNID(QIBM_DB_SQLADM) USER("+testUser+") USAGE(*ALLOWED)"; 
	     mirrorServerCommand(command, IGNORE_ERROR);

	     // Make sure the user has access to /qibm/userdata/os400/sqllib/function/jar 
	     command = "QSYS/CHGAUT OBJ('/') USER("+testUser+") DTAAUT(*RWX)";
	     mirrorServerCommand(command, IGNORE_ERROR);
	     command = "QSYS/CHGAUT OBJ('/QIBM') USER("+testUser+") DTAAUT(*RWX)";
	     mirrorServerCommand(command, IGNORE_ERROR);
	     command = "QSYS/CHGAUT OBJ('/QIBM/UserData') USER("+testUser+") DTAAUT(*RWX)";
	     mirrorServerCommand(command, IGNORE_ERROR);
	     command = "QSYS/CHGAUT OBJ('/QIBM/UserData/OS400') USER("+testUser+") DTAAUT(*RWX)";
	     mirrorServerCommand(command, IGNORE_ERROR);
	     command = "QSYS/CHGAUT OBJ('/QIBM/UserData/OS400/SQLLib') USER("+testUser+") DTAAUT(*RWX)";
	     mirrorServerCommand(command, IGNORE_ERROR);
	     command = "QSYS/CHGAUT OBJ('/QIBM/UserData/OS400/SQLLib/Function') USER("+testUser+") DTAAUT(*RWX)";
	     mirrorServerCommand(command, IGNORE_ERROR);
	     command = "QSYS/CHGAUT OBJ('/QIBM/UserData/OS400/SQLLib/Function/jar') USER("+testUser+") DTAAUT(*RWX)";
	     mirrorServerCommand(command, IGNORE_ERROR);

	 } catch (Exception e) {
	     System.out.println("Setup exception on "+command); 
	     e.printStackTrace(System.out); 
	     System.out.println("Possible setup problem.. Make sure "+testUser+" has been created");
	     System.out.println(".... CRTUSRPRF USRPRF("+testUser+") PASSWORD("+testPass+")  ACGCDE(514648897)  TEXT('Java stored procedures test') CCSID(37)");
	     e.printStackTrace(System.out); 
	 } 

     } 


     public static void setupUserProfileWithCCSID(String testUser, String testPass, int ccsid) throws Exception {
	 serverCommand("QSYS/CRTUSRPRF USRPRF("+testUser+") PASSWORD("+testPass+") TEXT('Java stored procedures test')  ACGCDE(514648897)  CCSID("+ccsid+")", true);

	 try {
	     serverCommand("QSYS/GRTOBJAUT OBJ(QSYS/"+testUser+") OBJTYPE(*USRPRF) USER("+testUser+") AUT(*ALL)", false);
	     serverCommand("QSYS/CHGUSRPRF USRPRF("+testUser+") PASSWORD(AB12DE34)",false );
	     serverCommand("QSYS/CHGUSRPRF USRPRF("+testUser+")  STATUS(*ENABLED)  PASSWORD("+testPass+") CCSID("+ccsid+")", false);

	 } catch (Exception e) {
	     System.out.println("Possible setup problem.. Make sure "+testUser+" has been created");
	     System.out.println(".... CRTUSRPRF USRPRF("+testUser+") PASSWORD("+testPass+")  ACGCDE(514648897)  TEXT('Java stored procedures test') CCSID("+ccsid+")");
	     e.printStackTrace(System.out); 
	 } 

     } 



     public static void createTable(Statement s, String table, String columns) throws Exception {
	 execStatement(s, "drop table "+table, true);
	 execStatement(s, "create table "+table+" "+columns, false );
	 execStatement(s, "grant all on table "+table+" to public", false);
     } 

     public static void createProcedure(Statement s, String procedure, String definition) throws Exception { 
		execStatement(s, "drop   procedure "+procedure, true);
		execStatement(s, "create procedure "+procedure+" "+definition, false); 
		execStatement(s, "grant all on procedure "+procedure+" to public", false);

     }

}





