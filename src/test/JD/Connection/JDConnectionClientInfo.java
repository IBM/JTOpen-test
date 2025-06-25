///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionClientInfo.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDConnectionClientInfo.java
//
// Classes:      JDConnectionClientInfo
//
////////////////////////////////////////////////////////////////////////
package test.JD.Connection;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.PrintObject;
import com.ibm.as400.access.SpooledFile;
import com.ibm.as400.access.SpooledFileList;

import test.JDConnectionTest;
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;
import test.PasswordVault;

import java.io.FileOutputStream;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;

import java.sql.Statement;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable; import java.util.Vector;
import java.sql.SQLWarning;
import java.util.Properties;



public class JDConnectionClientInfo
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDConnectionClientInfo";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDConnectionTest.main(newArgs); 
   }



    // Private data.
    private              Connection     closedConnection_;
    private              String         pwrUID_ = null;     //@A1A
    private              char[]         encryptedPwrPwd_ = null; 


/**
Constructor.
**/
    public JDConnectionClientInfo (AS400 systemObject,
                             Hashtable<String,Vector<String>> namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             
                             String password,
                             String pwrUID,     //@A1C
                             String pwrPwd) {   //@A1C
        super (systemObject, "JDConnectionClientInfo",
               namesAndVars, runMode, fileOutputStream,
               password);
        pwrUID_ = pwrUID;   //@A1A
        encryptedPwrPwd_ = PasswordVault.getEncryptedPassword(pwrPwd);
    }



/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
    throws Exception
    {
        connection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
        closedConnection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
        closedConnection_.close ();
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
        connection_.close ();
    }


 
 


    /**
     * JDBC40 test of connection.getClientInfo(String, String)
     */
    public void Var001()
    {
        String added = "-- added 07/10/2006 by jdbc driver to test setClientInfo";
	StringBuffer sb = new StringBuffer(); 
        if ( isJdbc40()  )
        {
            try
            {
                Connection conn = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);

                // try with invalid clientinfo name
                JDReflectionUtil.callMethod_V(conn, "setClientInfo", "none", "abcd");
                SQLWarning warning = conn.getWarnings();
                boolean check1 = (warning == null) ? false : true;
		if (!check1) sb.append("setting invalid client name did not result in warning\n"); 

                // try with invalid clientinfo name
                conn.clearWarnings();
                String retVal = (String) JDReflectionUtil.callMethod_OS(conn, "getClientInfo", "none");
                warning = conn.getWarnings();
                boolean check10 = (warning == null) ? false : true;
		if (!check10) sb.append("getting invalid client info (of none) did not result in warning but got "+retVal+"\n"); 

                // ApplicationName try with null
                String applicationName = "ApplicationName";
                JDReflectionUtil.callMethod_V(conn, "setClientInfo", applicationName, null);
                retVal = (String) JDReflectionUtil.callMethod_OS(conn, "getClientInfo", applicationName);
                boolean check2 = false;
                if (retVal == null || retVal.equals(""))
                    check2 = true;
		if (!check2) sb.append("calling getClientInfo with null did not return null but returned "+retVal +"\n"); 


                // ApplicationName
                JDReflectionUtil.callMethod_V(conn, "setClientInfo", applicationName, "my app");
                retVal = (String) JDReflectionUtil.callMethod_OS(conn, "getClientInfo", applicationName);
                boolean check3 = retVal.equals("my app") ? true : false;
		if (!check3) sb.append("setting "+applicationName+" returned "+retVal+" instead of 'my app'\n"); 

                // ClientUser try with null
                String clientUser = "ClientUser";
                JDReflectionUtil.callMethod_V(conn, "setClientInfo", clientUser, null);
                retVal = (String) JDReflectionUtil.callMethod_OS(conn, "getClientInfo", clientUser);
                boolean check4 = false;
                if (retVal == null || retVal.equals(""))
                    check4 = true;
		if (!check4) sb.append("calling set/getClientInfo for clientUser with null did not return null but returned "+retVal +"\n");

                // ClientUser
                JDReflectionUtil.callMethod_V(conn, "setClientInfo", clientUser, "my user");
                retVal = (String) JDReflectionUtil.callMethod_OS(conn, "getClientInfo", clientUser);
                boolean check5 = retVal.equals("my user") ? true : false;
		if (!check5) sb.append("setting "+clientUser+" returned "+retVal+" instead of 'my user'\n"); 


                // ClientHostname try with null
                String clientHostname = "ClientHostname";
                JDReflectionUtil.callMethod_V(conn, "setClientInfo", clientHostname, null);
                retVal = (String) JDReflectionUtil.callMethod_OS(conn, "getClientInfo", clientHostname);
                boolean check6 = false;
                if (retVal == null || retVal.equals(""))
                    check6 = true;
		if (!check6) sb.append("calling set/getClientInfo for clientHostname with null did not return null but returned "+retVal +"\n");


                // ClientHostname
                JDReflectionUtil.callMethod_V(conn, "setClientInfo", clientHostname, "my host");
                retVal = (String) JDReflectionUtil.callMethod_OS(conn, "getClientInfo", clientHostname);
                boolean check7 = retVal.equals("my host") ? true : false;
		if (!check7) sb.append("setting "+clientHostname+" returned "+retVal+" instead of 'my host'\n"); 

                // ClientAccounting try with null
                String clientAccounting = "ClientAccounting";
                JDReflectionUtil.callMethod_V(conn, "setClientInfo", clientAccounting, null);
                retVal = (String) JDReflectionUtil.callMethod_OS(conn, "getClientInfo", clientAccounting);
                boolean check8 = false;
                if (retVal == null || retVal.equals(""))
                    check8 = true;
	if (!check8) sb.append("calling set/getClientInfo for "+clientAccounting+" with null did not return null but returned "+retVal +"\n");


                // ClientAccounting
                JDReflectionUtil.callMethod_V(conn, "setClientInfo", clientAccounting, "my acc");
                retVal = (String) JDReflectionUtil.callMethod_OS(conn, "getClientInfo", clientAccounting);
                boolean check9 = retVal.equals("my acc") ? true : false;
		if (!check9) sb.append("setting "+clientAccounting+" returned "+retVal+" instead of 'my acc'\n"); 
                
                // ProgramID
                String clientProgramID = "ClientProgramID";
                JDReflectionUtil.callMethod_V(conn, "setClientInfo", clientProgramID, null);
                retVal = (String) JDReflectionUtil.callMethod_OS(conn, "getClientInfo", clientProgramID);
                boolean check11 = false;
                if (retVal == null || retVal.equals(""))
                    check11 = true;
		if (!check11) sb.append("calling set/getClientInfo for "+clientProgramID+" with null did not return null but returned "+retVal +"\n");

                // ProgramID
                JDReflectionUtil.callMethod_V(conn, "setClientInfo", clientProgramID, "my prog id");
                retVal = (String) JDReflectionUtil.callMethod_OS(conn, "getClientInfo", clientProgramID);
		
                boolean check12 = "my prog id".equals(retVal);
		if (!check12) sb.append("setting "+clientProgramID+" returned "+retVal+" instead of 'my prog id'\n"); 
              

                conn.close();

                assertCondition(check1 && check2 && check3 && check4 && check5 && check6 && check7 && check8 && check9 && check10 && check11 && check12, sb.toString()+added);

            } catch (Exception e)
            {
                failed(e, "unexpected exception calling setClientInfo " + added);
            }

	} else {
	    notApplicable("V5R5 or later variation"); 
	}
    }



    /**
     * JDBC40 test of connection.getClientInfo(String, String)
     */
    public void Var002()
    {
        String added = "-- added 07/10/2006 by jdbc driver to test setClientInfo";

        if ( isJdbc40())
        {
            try
            {
                Connection conn = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);

                String applicationNamePropertyName_ = "ApplicationName";
                String clientUserPropertyName_ = "ClientUser";
                String clientHostnamePropertyName_ = "ClientHostname";
                String clientAccountingPropertyName_ = "ClientAccounting";
                String clientProgramIDPropertyName_ = "ClientProgramID";

                String applicationName_ = "app name";
                String clientUser_ = "user name";
                String clientHostname_ = "host name";
                String clientAccounting_ = "accounting data";
                String clientProgramID_ = "program id data";

                //try with invalid clientinfo name
                Properties props = new Properties();

                props.setProperty(applicationNamePropertyName_, applicationName_);
                props.setProperty(clientAccountingPropertyName_, clientAccounting_);
                props.setProperty(clientHostnamePropertyName_, clientHostname_);
                props.setProperty(clientUserPropertyName_, clientUser_);
                props.setProperty(clientProgramIDPropertyName_, clientProgramID_);

                JDReflectionUtil.callMethod_V(conn, "setClientInfo", props);
                Properties props2 = (Properties) JDReflectionUtil.callMethod_O(conn, "getClientInfo");
                boolean check1 = applicationName_.equals(props2.getProperty(applicationNamePropertyName_)) ? true : false;
                boolean check2 = clientAccounting_.equals(props2.getProperty(clientAccountingPropertyName_)) ? true : false;
                boolean check3 = clientHostname_.equals(props2.getProperty(clientHostnamePropertyName_)) ? true : false;
                boolean check4 = clientUser_.equals(props2.getProperty(clientUserPropertyName_)) ? true : false;
                boolean check9 = clientProgramID_.equals(props2.getProperty(clientProgramIDPropertyName_)) ? true : false;

                //next remove two clientInfos from properties, and set (according to javadoc, the two missing should be re-set also) 
                props.remove(applicationNamePropertyName_);
                props.remove(clientAccountingPropertyName_);
                props.remove(clientProgramIDPropertyName_);

                JDReflectionUtil.callMethod_V(conn, "setClientInfo", props);
                props2 = (Properties) JDReflectionUtil.callMethod_O(conn, "getClientInfo");
                boolean check5 = false;
                if ((props2.getProperty(applicationNamePropertyName_) == null) || (props2.getProperty(applicationNamePropertyName_).equals("")))
                    check5 = true;
                boolean check6 = false;
                if ((props2.getProperty(clientAccountingPropertyName_) == null) || (props2.getProperty(clientAccountingPropertyName_).equals("")))
                    check6 = true;
                boolean check10 = false;
                if ((props2.getProperty(clientProgramIDPropertyName_) == null) || (props2.getProperty(clientProgramIDPropertyName_).equals("")))
                    check10 = true;

                boolean check7 = clientHostname_.equals(props2.getProperty(clientHostnamePropertyName_)) ? true : false;
		if (!check7) System.out.println("Got "+props2.getProperty(clientHostnamePropertyName_)+
						" for "+clientHostnamePropertyName_+" sb "+clientHostname_);

                boolean check8 = clientUser_.equals(props2.getProperty(clientUserPropertyName_)) ? true : false;

		if (!check8) System.out.println("Got "+props2.getProperty(clientUserPropertyName_)+
						" for "+clientUserPropertyName_+" sb "+clientUser_);


                conn.close();

                assertCondition(check1 && check2 && check3 && check4 && check5 &&
				check6 && check7 && check8 && check9 && check10,
				" check1="+check1+" check2="+check2+" check3="+check3+" check4="+check4+" \ncheck5="+check5+" check6="+check6+" check7="+check7+" check8="+check8+" check9="+check9+" check10="+check10+" \ncalling properties = getClientInfo() "+ added);

            } catch (Exception e)
            {
                failed(e, "unexpected exception calling setClientInfo " + added);
            }

	} else {
	    notApplicable("V5R5 or later version"); 
	} 
    }
    

    /**
     * JDBC40 test of databasemetadata.getClientInfoProperties()
     */
    public void Var003()
    {
        String added = "-- added 07/10/2006 by jdbc driver to test getClientInfoProperties";

        if (isJdbc40() || (isToolboxDriver() && getRelease()>=JDTestDriver.RELEASE_V7R1M0) || (getDriver() == JDTestDriver.DRIVER_NATIVE && getRelease()>=JDTestDriver.RELEASE_V7R1M0) )        {
            try
            {
                Connection conn = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);

                DatabaseMetaData md = conn.getMetaData();
                int clientInfoCount = 5;
                ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_O(md, "getClientInfoProperties");
                Object toolboxValues[][] =  { { "ApplicationName", Integer.valueOf(255), "", "The name of the application currently utilizing the connection." }, 
                        { "ClientUser", Integer.valueOf(255), "", "The name of the user that the application using the connection is performing work for.  This may not be the same as the user name that was used in establishing the connection."},
                        { "ClientHostname", Integer.valueOf(255), "", "The hostname of the system the application using the connection is running on."}, 
                        { "ClientAccounting", Integer.valueOf(255), "", "Accounting information."},
                        { "ClientProgramID", Integer.valueOf(255), "", "Program identification information."}};

                Object nativeValues55[][] =  { { "ApplicationName", Integer.valueOf(255), "", "The name of the application currently utilizing the connection.  This value is stored in the CLIENT_APPLNAME register." }, 
                        { "ClientUser", Integer.valueOf(255), "", "The name of the user that the application using the connection is performing work for.  This may not be the same as the user name that was used in establishing the connection.  This value is stored in the CLIENT_USERID register."},
                        { "ClientHostname", Integer.valueOf(255), "", "The hostname of the computer the application using the connection is running on.  This value is stored in the CLIENT_WRKSTNNAME register."},
                        { "ClientAccounting", Integer.valueOf(255), "", "The accounting string for the client.  This value is stored in the CLIENT_ACCTNG register."},
                        { "ClientProgramID", Integer.valueOf(255), "", "Program identification information."}};

                Object clientValues[][] = null;
                if (isToolboxDriver()) 
                {
                    clientValues = toolboxValues;
                } else
                {
			clientValues = nativeValues55;
                }
              
                boolean check1 = true;
                String sb = "";
                while(rs.next())
                {
                    boolean foundRow = false;
                    String clientInfoName = rs.getString(1);
                    for(int x = 0; x < clientInfoCount; x++)
                    {
                        
                        if(clientInfoName.equals(clientValues[x][0]))
                        {
                            foundRow = true;
                            int size = rs.getInt(2);
                            String defaultVal = rs.getString(3);
                            String description = rs.getString(4);
                            if( (size == ((Integer)clientValues[x][1]).intValue())
                                    && (defaultVal.equals( clientValues[x][2]))
                                    && (description.equals( clientValues[x][3])))
                            {
				// leave as true 
                                // check1 = true;
                                break;
                            }
                            else 
                            {
                                sb += " Value(s) for " + rs.getString(1) + " incorrect.  \n";
                                sb += " Expected size    : "+clientValues[x][1]+"\n";
				sb += "   Output size    : "+size+"\n";
				sb += " Expected default : "+clientValues[x][2]+"\n";
				sb += "   Output default : "+defaultVal+"\n";
				sb += " Expected desc    : "+clientValues[x][3]+"\n";
				sb += "   Output desc    : "+description+"\n"; 
				    
                                check1 = false;
				break; 
                            }
                                                              
                        }
                    }
                    if(foundRow == false)
                    {
                        sb += " Row for clientInfo " + rs.getString(1) + " not expected.  ";
                        check1 = false;
                        break;
                    }
                }
                
                 
                conn.close();

                assertCondition(check1, sb + added );

            } catch (Exception e)
            {
                failed(e, "unexpected exception calling getClientInfoProperties " + added);
            }

	    } else {
		notApplicable("V5R5 or later variation");
	    } 
    }



    /*
     * For V5R5 check default settings in SQL registers for externalized values 
     */ 

    public void Var004()
    {
        String added = "-- added 03/06/2007 by native to test client register default values"; 

        if (getRelease() <  JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("V5R5 or later release -- queries built in properties "); 
	} else { 

            String sql = "NONE"; 
            try   {
                String expected[][] = { 
                    {"APPLNAME", ""}, 
                    {"USERID", ""}, 
                    {"WRKSTNNAME", ""}, 
                    {"ACCTNG", ""}, 
                    {"PROGRAMID", ""} 
                }; 
                boolean success = true; 
		StringBuffer sb = new StringBuffer(); 
		Connection conn = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
		Statement stmt = conn.createStatement();
                sql = "SELECT CURRENT CLIENT_APPLNAME AS APPLNAME, "+
                      "CURRENT CLIENT_USERID AS USERID, " +
                      "CURRENT CLIENT_WRKSTNNAME AS WRKSTNNAME, " +
                      "CURRENT CLIENT_ACCTNG AS ACCTNG, " +
                      "CURRENT CLIENT_PROGRAMID AS PROGRAMID FROM SYSIBM.SYSDUMMY1";  
                ResultSet rs = stmt.executeQuery(sql);
                rs.next(); 
                for (int i = 0; i < expected.length; i++) {
                  String register = expected[i][0]; 
                  sql = "GET "+register; 
                  String val = rs.getString(register); 
                  if ( ! expected[i][1].equals(val)) { 
                    sb.append("For "+register+" expected '"+expected[i][1]+"' but got '"+val+"'\n"); 
                    success = false; 
                  }
                }
                
                assertCondition(success, sb.toString() + added );

            } catch (Exception e)
            {
                failed(e, "unexpected exception SQL='"+sql+"' " + added);
            }
        }
    }

    /*
     * For V5R5 check settings in SQL registers for externalized values 
     */ 

    public void Var005()
    {
        String added = "-- added 03/06/2007 by native to test client register set values"; 

        if (getRelease() <  JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("V5R5 or later release"); 
	} else { 

            String sql = "NONE"; 
            try   {
                String expected[][] = { 
                    {"ApplicationName", "JDConnectionClientInfo"}, 
                    {"ClientUser", "JDBCUSER"}, 
                    {"ClientHostname", "localhost"}, 
                    {"ClientAccounting", "5072537016"}, 
                    {"ClientProgramID", "5722JV1"} 
                }; 
                
                boolean success = true; 
                StringBuffer sb = new StringBuffer(); 
                Connection conn = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);

                for (int i = 0; i < expected.length; i++) {
                  String register = expected[i][0]; 
                  sql = "SET "+register;
                  JDReflectionUtil.callMethod_V(conn, "setClientInfo", register, expected[i][1]); 
                }
                
                Statement stmt = conn.createStatement();
                
                
                sql = "SELECT CURRENT CLIENT_APPLNAME AS ApplicationName, "+
                      "CURRENT CLIENT_USERID AS ClientUser, " +
                      "CURRENT CLIENT_WRKSTNNAME AS ClientHostname, " +
                      "CURRENT CLIENT_ACCTNG AS ClientAccounting, " +
                      "CURRENT CLIENT_PROGRAMID AS ClientProgramID FROM SYSIBM.SYSDUMMY1";  
                ResultSet rs = stmt.executeQuery(sql);
                rs.next(); 
                for (int i = 0; i < expected.length; i++) {
                  String register = expected[i][0]; 
                  sql = "GET "+register; 
                  String val = rs.getString(register); 
                  if ( ! expected[i][1].equals(val)) { 
                    sb.append("For "+register+" expected '"+expected[i][1]+"' but got '"+val+"'"); 
                    success = false; 
                  }
                }
                
                assertCondition(success, sb.toString() + added );

            } catch (Exception e)
            {
                failed(e, "unexpected exception SQL='"+sql+"' " + added);
            }
        }
    }

   
    
    /*
     * For V5R5 check default settings for interface information
     * The I0 states... 
     *   4.     The values of the Interface Information will be output on 1000 
     *          Database monitor records for CONNECT, DISCONNECT, RELEASE, and 
     *          SET CONNECTION operations. This information will also be captured 
     *          on the first 1000 record for any operation when the database monitor 
     *          is started against active jobs.
     *          
     *          The Interface Information will be concatenated and delimited by colons. 
     *          It will be displayed in the QVC5001 (varchar(500)) field. 
     *          Remote server information, if applicable, will also be concatenated onto the 
     *          Interface Information.  If so, the end of the Interface Information will 
     *          be delimited by a semi-colon and the remote server information will follow. 
     *          The string will be truncated at character 500.  
     */
    public void Var006()
    {
        String added = "-- added 03/06/2007 by native to test client register default values"; 

        if (getRelease() <  JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("V5R5 or later release"); 
	} else { 
            String sql = "NONE"; 
            try   {
                String expected = ""; 
                if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		    if        (getRelease() == JDTestDriver.RELEASE_V7R6M0) {
			expected = "IBM Developer Kit for Java JDBC Driver:JDBC:07060";
		    } else if        (getRelease() == JDTestDriver.RELEASE_V7R5M0) {
			expected = "IBM Developer Kit for Java JDBC Driver:JDBC:07050";
		    } else if (getRelease() == JDTestDriver.RELEASE_V7R4M0) {
			expected = "IBM Developer Kit for Java JDBC Driver:JDBC:07040";
		    } else if (getRelease() == JDTestDriver.RELEASE_V7R3M0) {
			expected = "IBM Developer Kit for Java JDBC Driver:JDBC:07030";
		    } else if (getRelease() == JDTestDriver.RELEASE_V7R2M0) {
			expected = "IBM Developer Kit for Java JDBC Driver:JDBC:07020";
		    } else if (getRelease() == JDTestDriver.RELEASE_V7R1M0) {
			expected = "IBM Developer Kit for Java JDBC Driver:JDBC:07010";
		    } else { 
			expected = "IBM Developer Kit for Java JDBC Driver:JDBC:06010";
		    }
                }
                else if (isToolboxDriver()) {    
                  expected = "IBM Toolbox for Java:JDBC:070600";        
                }                                                        
                Connection conn = testDriver_.getConnection(baseURL_, ((isToolboxDriver()) ? pwrUID_ : userId_), ((isToolboxDriver()) ? encryptedPwrPwd_ : encryptedPassword_));  //@A1C Run with user id and password specified by -pwrSys param for toolbox
                Statement stmt = conn.createStatement();
                sql = "CALL QSYS.QCMDEXC('STRDBMON OUTFILE("+JDConnectionTest.COLLECTION+"/DBMONOUT)             ',0000000040.00000)";
                stmt.executeUpdate(sql); 
                sql = "SELECT * FROM SYSIBM.SYSDUMMY1"; 
                stmt.executeQuery(sql); 
                sql = "CALL QSYS.QCMDEXC('ENDDBMON         ',0000000010.00000)";
                stmt.executeUpdate(sql); 
                sql = "SELECT QVC5001 FROM "+JDConnectionTest.COLLECTION+".DBMONOUT WHERE QQRID=1000"; 
                ResultSet rs = stmt.executeQuery(sql);
                rs.next();
                sql = "Fetching first column"; 
                String value =""+rs.getString(1); 
                                
                assertCondition(value.indexOf(expected) >= 0, "Expected '"+expected+"' but got '"+value+"' from SELECT QVC5001 FROM "+JDConnectionTest.COLLECTION+".DBMONOUT WHERE QQRID=1000"  );

            } catch (Exception e)
            {
                failed(e, "unexpected exception SQL='"+sql+"' " + added);
            }
        }
    }


    /**
     * Set and check various values, include max length of 255
     */


    public void Var007()
    {
	String added = "-- added 09/06/2007 by native to test client register set values -- Note: as of 9/6/2007 this will fail because of issue 34082 "; 

	if (getRelease() <  JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("V5R5 or later release"); 
	} else { 

	    String sql = "NONE"; 
	    try   {
		String expected[][] = { 
		    {"1  SET", "ApplicationName", "JDConnectionClientInfo"}, 
		    {"2  SET", "ClientUser", "JDBCUSER"}, 
		    {"3  SET", "ClientHostname", "localhost"}, 
		    {"4  SET", "ClientAccounting", "5072537016"}, 
		    {"5  SET", "ClientProgramID", "5722JV1"},
		    {"6  GET", "ApplicationName", "JDConnectionClientInfo"}, 
		    {"7  GET", "ClientUser", "JDBCUSER"}, 
		    {"8  GET", "ClientHostname", "localhost"}, 
		    {"9  GET", "ClientAccounting", "5072537016"}, 
		    {"10 GET", "ClientProgramID", "5722JV1"},

		    {"6  QUERY", "ApplicationName", "JDConnectionClientInfo"}, 
		    {"7  QUERY", "ClientUser", "JDBCUSER"}, 
		    {"8  QUERY", "ClientHostname", "localhost"}, 
		    {"9  QUERY", "ClientAccounting", "5072537016"}, 
		    {"10 QUERY", "ClientProgramID", "5722JV1"},


		    {"11 SET", "ApplicationName", "A2345678911234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"},
		    {"12 GET", "ApplicationName", "A2345678911234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"},
		    {"13 GET", "ClientUser", "JDBCUSER"}, 
		    {"14 GET", "ClientHostname", "localhost"}, 
		    {"15 GET", "ClientAccounting", "5072537016"}, 
		    {"16 GET", "ClientProgramID", "5722JV1"},

		    {"12 QUERY", "ApplicationName", "A2345678911234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"},
		    {"13 QUERY", "ClientUser", "JDBCUSER"}, 
		    {"14 QUERY", "ClientHostname", "localhost"}, 
		    {"15 QUERY", "ClientAccounting", "5072537016"}, 
		    {"16 QUERY", "ClientProgramID", "5722JV1"},

		    {"17 SET", "ClientUser", "U2345678911234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"},
		    {"18 GET", "ApplicationName", "A2345678911234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"},
		    {"19 GET", "ClientUser", "U2345678911234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"},
		    {"20 GET", "ClientHostname", "localhost"}, 
		    {"21 GET", "ClientAccounting", "5072537016"}, 
		    {"22 GET", "ClientProgramID", "5722JV1"},

		    {"18 QUERY", "ApplicationName", "A2345678911234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"},
		    {"19 QUERY", "ClientUser", "U2345678911234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"},
		    {"20 QUERY", "ClientHostname", "localhost"}, 
		    {"21 QUERY", "ClientAccounting", "5072537016"}, 
		    {"22 QUERY", "ClientProgramID", "5722JV1"},

		    {"23 SET", "ClientHostname", "H2345678911234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"},
		    {"24 GET", "ApplicationName", "A2345678911234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"},
		    {"25 GET", "ClientUser", "U2345678911234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"},
		    {"26 GET", "ClientHostname", "H2345678911234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"},
		    {"27 GET", "ClientAccounting", "5072537016"}, 
		    {"28 GET", "ClientProgramID", "5722JV1"},
		    {"24 QUERY", "ApplicationName", "A2345678911234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"},
		    {"25 QUERY", "ClientUser", "U2345678911234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"},
		    {"26 QUERY", "ClientHostname", "H2345678911234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"},
		    {"27 QUERY", "ClientAccounting", "5072537016"}, 
		    {"28 QUERY", "ClientProgramID", "5722JV1"},


		    {"29 SET", "ClientAccounting", "G2345678911234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"},
		    {"30 GET", "ApplicationName", "A2345678911234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"},
		    {"31 GET", "ClientUser", "U2345678911234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"},
		    {"32 GET", "ClientHostname", "H2345678911234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"},
		    {"33 GET", "ClientAccounting", "G2345678911234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"},
		    {"34 GET", "ClientProgramID", "5722JV1"}, 
		    {"30 QUERY", "ApplicationName", "A2345678911234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"},
		    {"31 QUERY", "ClientUser", "U2345678911234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"},
		    {"32 QUERY", "ClientHostname", "H2345678911234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"},
		    {"33 QUERY", "ClientAccounting", "G2345678911234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"},
		    {"34 QUERY", "ClientProgramID", "5722JV1"}, 


		    {"35 SET", "ClientProgramID", "P2345678911234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"},
		    {"36 GET", "ApplicationName", "A2345678911234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"},
		    {"37 GET", "ClientUser", "U2345678911234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"},
		    {"38 GET", "ClientHostname", "H2345678911234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"},
		    {"39 GET", "ClientAccounting", "G2345678911234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"},
		    {"40 GET", "ClientProgramID", "P2345678911234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"}, 
		    {"36 QUERY", "ApplicationName", "A2345678911234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"},
		    {"37 QUERY", "ClientUser", "U2345678911234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"},
		    {"38 QUERY", "ClientHostname", "H2345678911234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"},
		    {"39 QUERY", "ClientAccounting", "G2345678911234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"},
		    {"40 QUERY", "ClientProgramID", "P2345678911234567892123456789312345678941234567895123456789612345678971234567898123456789912345678901234567891123456789212345678"}, 


		    
		}; 

		boolean success = true; 
		StringBuffer sb = new StringBuffer(); 
		Connection conn = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);


		Statement stmt = conn.createStatement(); 
		for (int i = 0; i < expected.length; i++) {

		    String operation = expected[i][0];
		    String register = expected[i][1];
		    String value    = expected[i][2];

		    /* System.out.println("Processing "+i+" "+operation+" "+register+" "+value);  */ 
		    if (operation.indexOf("SET") >= 0) {
			sql = "SET "+register;
			JDReflectionUtil.callMethod_V(conn, "setClientInfo", register, value); 
		    } else if (operation.indexOf("GET") >= 0) {
			String retVal = ""+(String) JDReflectionUtil.callMethod_OS(conn, "getClientInfo", register);
			if (!retVal.equals(value)) {
			    sb.append("\nFor "+operation+" "+register+" got "+retVal+" sb "+value);
			    success = false; 
			} 
		    } else if (operation.indexOf("QUERY") >= 0) {
			sql = "SELECT CURRENT CLIENT_APPLNAME AS ApplicationName, "+
			  "CURRENT CLIENT_USERID AS ClientUser, " +
			  "CURRENT CLIENT_WRKSTNNAME AS ClientHostname, " +
			  "CURRENT CLIENT_ACCTNG AS ClientAccounting, " +
			  "CURRENT CLIENT_PROGRAMID AS ClientProgramID FROM SYSIBM.SYSDUMMY1";  

			ResultSet rs = stmt.executeQuery(sql);
			rs.next(); 
			String retVal = rs.getString(register);
			//toolbox does not update sysdummy1
			if((!isToolboxDriver()) && !retVal.equals(value)) {
			    sb.append("\nFor "+operation+" "+register+" got "+retVal+" sb "+value);
			    success = false; 
			} 

		    } else {
			sql="UNKNOWN operation at "+i+" = "+operation;
			throw new Exception(sql); 
		    } 
		}


		assertCondition(success, sb.toString() + added );

	    } catch (Exception e)
	    {
		failed(e, "unexpected exception SQL='"+sql+"' " + added);
	    }
	}
    }



    /*
     * call getClientInfo()
     */ 
    public void Var008()
    {
        String added = "-- added 06/18/2008 by native"; 
        StringBuffer sb = new StringBuffer(); 
        boolean passed = true; 
        {
	    } 
            try   {
                Connection conn = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);

		Properties  properties =
		  (Properties) JDReflectionUtil.callMethod_O(conn, "getClientInfo");
		String expected; 
		Enumeration<Object> enumeration = properties.keys();
                while (enumeration.hasMoreElements()) {
                  String key = (String) enumeration.nextElement();
                  String value =  properties.getProperty(key ); 
                  if (key.equals("ClientUser")) {
		    // 
		    // Client user default is "" until it is set
		    // 
                    if (! "".equals(value)) {
                      sb.append("\n For ClientUser got '"+value+"' sb ''"); 
                      passed = false; 
                    }
                  } else if (key.equals("ClientHostName")) { 
                    String hostname = "localhost"; 
                    try { 
                      hostname = InetAddress.getLocalHost().getHostName(); 
                    } catch (Exception e) {
                    }
                    if (! hostname.equals(value)) {
                      sb.append("\n For ClientHostName got "+value+" sb "+hostname); 
                      passed = false; 
                    }
                    
                  } else if (key.equals("ApplicationName")) {
                    expected =  "";
                    if (! expected.equals(value)) {
                      sb.append("\n For "+key+" got "+value+" sb "+expected); 
                      passed = false; 
                    }
                    
                  } else if (key.equals("ClientAccounting")) { 
                     expected =  "";
                     if (! expected.equals(value)) {
                       sb.append("\n For "+key+" got "+value+" sb "+expected); 
                       passed = false; 
                     }
                  } else if (key.equals("ClientProgramID")) {
                    expected =  "";
                    if (! expected.equals(value)) {
                      sb.append("\n For "+key+" got "+value+" sb "+expected); 
                      passed = false; 
                    }
                  }
                } /* while */ 
                conn.close(); 

                assertCondition(passed, sb.toString() + "\n"+added); 

            } catch (Exception e)
            {
                failed(e, "unexpected exception " + added);
            }
        }
    
    /*
     * Verify that PRTSQLINF is not done when CLIENT ACCOUNTING setting
     */ 
  public void Var009() {
    if (checkNative()) {
      String testProfile = "ISSUE67424";
      String testPassword = "J8VAPASS";
      StringBuffer sb = new StringBuffer();
      sb.append("-- Added 10/19/2021 for issue 67424/CPS C7GCHE");
      try {
        boolean passed = true;
        char[] pwrPwd = PasswordVault.decryptPassword(encryptedPwrPwd_); 
        AS400 pwrAS400 = new AS400("localhost", pwrUID_, pwrPwd);
        Arrays.fill(pwrPwd,'\0'); 
        CommandCall pwrCommand = new CommandCall(pwrAS400);

        sb.append("\nCreating profile "+testProfile);
        boolean results = pwrCommand
            .run("QSYS/CRTUSRPRF "+testProfile+" PASSWORD("+testPassword+")");
        sb.append("\n CRTUSRPRF returned " + results);

        sb.append("\nGetting connection"); 
        Connection c = DriverManager.getConnection("jdbc:db2:localhost",testProfile,testPassword); 
        String clientAccounting = "ClientAccounting";
        sb.append("\nCalling setClientInfo"); 
        JDReflectionUtil.callMethod_V(c, "setClientInfo", clientAccounting, "ISSUE67424");
        Statement stmt = c.createStatement(); 
        
        sb.append("\nRunning query "); 
        ResultSet rs = stmt.executeQuery("Select * from sysibm.sysdummy1"); 
        rs.next(); 
        rs.close(); 
        sb.append("\nClosing connection"); 
        c.close(); 

        sb.append("\nChecking for PRTSQLINF spool file"); 
        SpooledFileList spooledFileList = new SpooledFileList(pwrAS400); 
        spooledFileList.setUserFilter(testProfile);
        spooledFileList.openSynchronously();
        Enumeration<SpooledFile> enumeration = spooledFileList.getObjects(); 
        while (enumeration.hasMoreElements()) {
          SpooledFile spooledFile = (SpooledFile) enumeration.nextElement();
          String userData = spooledFile.getStringAttribute(PrintObject.ATTR_USERDATA);
          sb.append("\n Found SPLF with userdata = "+userData); 
          if ("PRTSQLINF".equals(userData)) {
            sb.append("\n ** FAILED *** "+userData+" FOUND"); 
            passed = false; 
          }
        }
        spooledFileList.close(); 
        sb.append("\nDeleting spool files"); 
        results = pwrCommand
            .run("QSYS/DLTSPLF FILE(*SELECT) SELECT("+testProfile+")");

        sb.append("\nDropping profile  "+testProfile);
        results = pwrCommand.run("QSYS/DLTUSRPRF USRPRF("+testProfile+") OWNOBJOPT(*DLT)");
        sb.append("\n DLTUSRPRF returned " + results);
        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "unexpected exception " + sb);
      }
    } /* check native */
  } /* Var009 */


    /*
     * Verify that PRTSQLINF is not done when properties object used
     * 
     */ 
  public void Var010() {
    if (checkNative() ) {
      String testProfile = "ISSUE67424";
      String testPassword = "J8VAPASS";
      StringBuffer sb = new StringBuffer();
      sb.append("-- Added 10/19/2021 for issue 67424/CPS C7GCHE");
      try {
        boolean passed = true;
        char[] pwrPwd = PasswordVault.decryptPassword(encryptedPwrPwd_); 
        AS400 pwrAS400 = new AS400("localhost", pwrUID_, pwrPwd);
        Arrays.fill(pwrPwd,'\0'); 

        CommandCall pwrCommand = new CommandCall(pwrAS400);

        sb.append("\nCreating profile "+testProfile);
        boolean results = pwrCommand
            .run("QSYS/CRTUSRPRF "+testProfile+" PASSWORD("+testPassword+")");
        sb.append("\n CRTUSRPRF returned " + results);

        sb.append("\nGetting connection"); 
        Connection c = DriverManager.getConnection("jdbc:db2:localhost",testProfile,testPassword);
        Properties properties = new Properties(); 
        properties.put("ApplicationName","JDConnectionClientInfo");
        sb.append("\nCalling setClientInfo with properties"); 
        JDReflectionUtil.callMethod_V(c, "setClientInfo",properties);
        Statement stmt = c.createStatement(); 
        
        sb.append("\nRunning query "); 
        ResultSet rs = stmt.executeQuery("Select * from sysibm.sysdummy1"); 
        rs.next(); 
        rs.close(); 
        sb.append("\nClosing connection"); 
        c.close(); 

        sb.append("\nChecking for PRTSQLINF spool file"); 
        SpooledFileList spooledFileList = new SpooledFileList(pwrAS400); 
        spooledFileList.setUserFilter(testProfile);
        spooledFileList.openSynchronously();
        Enumeration<SpooledFile> enumeration = spooledFileList.getObjects(); 
        while (enumeration.hasMoreElements()) {
          SpooledFile spooledFile = (SpooledFile) enumeration.nextElement();
          String userData = spooledFile.getStringAttribute(PrintObject.ATTR_USERDATA);
          sb.append("\n Found SPLF with userdata = "+userData); 
          if ("PRTSQLINF".equals(userData)) {
            sb.append("\n ** FAILED *** "+userData+" FOUND"); 
            passed = false; 
          }
        }
        spooledFileList.close();
        
        sb.append("\nDeleting spool files"); 
        results = pwrCommand
            .run("QSYS/DLTSPLF FILE(*SELECT) SELECT("+testProfile+")");

        sb.append("\nDropping profile  "+testProfile);
        results = pwrCommand.run("QSYS/DLTUSRPRF USRPRF("+testProfile+") OWNOBJOPT(*DLT)");
        sb.append("\n DLTUSRPRF returned " + results);
        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "unexpected exception " + sb);
      }
    } /* check native */
  } /* Var010 */






}



