
///////////////////////////////////////////////////////////////////////////////
//
//JTOpen (IBM Toolbox for Java - OSS version)
//
//Filename:  JDASSeamlessFailover.java
//
//The source code contained herein is licensed under the IBM Public License
//Version 1.0, which has been approved by the Open Source Initiative.
//Copyright (C) 1997-2023 International Business Machines Corporation and
//others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////
//
//File Name: AS400JDBCSeamlessFailover.java
//Classes:   AS400JDBCSeamlessFailover
//
////////////////////////////////////////////////////////////////////////////
//
////////////////////////////////////////////////////////////////////////////

package test.JD.AS;
import test.*; 

import java.io.FileOutputStream;
import java.util.Hashtable;
import java.sql.*;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDataSource;

import test.JDASTest;
import test.JD.AS.*;
import test.socketProxy.*;

/**
* Testcase JDASSeamlessFailover
**/
public class JDASSeamlessFailover extends JDASTestcase {
int RUN_SECONDS = 20;

protected String systemName;

protected Connection transactionalConnection;
protected Connection autocommitConnection;
protected Connection failConnection;
protected SocketProxyPair transactionalSocketProxyPair;
protected SocketProxyPair autocommitSocketProxyPair;
protected SocketProxyPair failSocketProxyPair;
protected String collection;

protected String table;

/**
* Constructor.
**/
public JDASSeamlessFailover(AS400 systemObject, Hashtable namesAndVars,
int runMode, FileOutputStream fileOutputStream, 
String password, String pwrSysUserID, String pwrSysPassword) {
super(systemObject, "JDASSeamlessFailover", namesAndVars, runMode,
fileOutputStream, password, pwrSysUserID, pwrSysPassword);
// originalPrintWriter_ = Trace.getPrintWriter();
}

public JDASSeamlessFailover(AS400 systemObject, String testname, Hashtable namesAndVars,
int runMode, FileOutputStream fileOutputStream, 
String password, String pwrSysUserID, String pwrSysPassword) {
super(systemObject, testname, namesAndVars, runMode,
fileOutputStream, password, pwrSysUserID, pwrSysPassword);
// originalPrintWriter_ = Trace.getPrintWriter();
}



public void setup() {
String runSeconds = System.getProperty("RUN_SECONDS"); 
if (runSeconds != null) { 
RUN_SECONDS=Integer.parseInt(runSeconds); 
System.out.println("RUN_SECONDS set from property to "+RUN_SECONDS); 
}


String sql = "";
try {
systemName = systemObject_.getSystemName();

fixupSql(setupSql);
fixupSql(statementSql);
fixupSql(callableStatementSql);
fixupSql(cleanupSql);

if (isToolboxFixDate(TOOLBOX_FIX_DATE)) {
url_ = "jdbc:as400://" + systemObject_.getSystemName() + ";user="
+ systemObject_.getUserId() ;

Connection connection = testDriver_.getConnection(url_,
systemObject_.getUserId(), encryptedPassword_);

Statement s = connection.createStatement();
for (int i = 0; i < setupSql.length; i++) {
sql = setupSql[i];
s.execute(sql);
}
connection.close();

String url;

transactionalSocketProxyPair = SocketProxyPair.getSocketProxyPair(8001,
8010, systemName, JDASTest.AS_DATABASE);

url = "jdbc:as400:localhost:"
+ transactionalSocketProxyPair.getPortNumber1()
+ ";enableClientAffinitiesList=1;enableSeamlessFailover=1;"
+ "retryIntervalForClientReroute=1;"
+ "clientRerouteAlternateServerName=localhost;clientRerouteAlternatePortNumber="
+ transactionalSocketProxyPair.getPortNumber2();

transactionalConnection = testDriver_.getConnection(url,
systemObject_.getUserId(), encryptedPassword_);
transactionalConnection.setAutoCommit(false);
transactionalConnection
.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

autocommitSocketProxyPair = SocketProxyPair.getSocketProxyPair(8021,
8030, systemName, JDASTest.AS_DATABASE);

url = "jdbc:as400:localhost:"
+ autocommitSocketProxyPair.getPortNumber1()
+";date format=jis;time format=jis;"
+ "enableClientAffinitiesList=1;enableSeamlessFailover=1;"
+ "retryIntervalForClientReroute=1;"
+ "clientRerouteAlternateServerName=localhost;clientRerouteAlternatePortNumber="
+ autocommitSocketProxyPair.getPortNumber2();

autocommitConnection = testDriver_.getConnection(url,
systemObject_.getUserId(), encryptedPassword_);
autocommitConnection.setAutoCommit(true);

failSocketProxyPair = SocketProxyPair.getSocketProxyPair(8021, 8030,
systemName, JDASTest.AS_DATABASE);

url = "jdbc:as400:localhost:"
+ failSocketProxyPair.getPortNumber1()
+ ";enableClientAffinitiesList=1;enableSeamlessFailover=1;"
+ "retryIntervalForClientReroute=1;"
+ "clientRerouteAlternateServerName=localhost;clientRerouteAlternatePortNumber="
+ failSocketProxyPair.getPortNumber2();

failConnection = testDriver_.getConnection(url,
systemObject_.getUserId(), encryptedPassword_);

failConnection.setAutoCommit(true);

collection = JDASTest.COLLECTION;
table = collection + ".JDASEMLSS";
Statement stmt = transactionalConnection.createStatement();
try {
stmt.executeUpdate("DROP TABLE " + table);
} catch (Exception e) {

}
transactionalConnection.commit();

stmt.executeUpdate("CREATE TABLE " + table
+ "(C1 INT, GENID INT GENERATED ALWAYS AS IDENTITY)");
transactionalConnection.commit();

}

} catch (Exception e) {
System.out.println("Setup error.");
System.out.println("Last sql statement was the following");
System.out.println(sql);
e.printStackTrace(System.out);
}
}

/**
* @exception Exception
*              If an exception occurs.
**/
public void cleanup() throws Exception {
try {
if (isToolboxFixDate(TOOLBOX_FIX_DATE)) {
url_ = "jdbc:as400://" + systemObject_.getSystemName() + ";user="
+ systemObject_.getUserId() ;

Connection connection = testDriver_.getConnection(url_,
systemObject_.getUserId(), encryptedPassword_);

Statement s = connection.createStatement();
for (int i = 0; i < cleanupSql.length; i++) {
try {
s.execute(cleanupSql[i]);
} catch (Exception e) {
e.printStackTrace(System.out);
}
}
connection.close();

transactionalConnection.close();
autocommitConnection.close();
failConnection.close();
transactionalSocketProxyPair.endService();
autocommitSocketProxyPair.endService();
failSocketProxyPair.endService();

}
} catch (Throwable e) {
e.printStackTrace();
}
}

/**
* Test to make sure that we can recover to the same system without an
* exception
**/
public void Var001() { notApplicable("see JDASSeamlessFailover1");  }
public void Var002() { notApplicable("see JDASSeamlessFailover1");  }
public void Var003() { notApplicable("see JDASSeamlessFailover1");  }
public void Var004() { notApplicable("see JDASSeamlessFailover1");  }
public void Var005() { notApplicable("see JDASSeamlessFailover1");  }
public void Var006() { notApplicable("see JDASSeamlessFailover1");  }
public void Var007() { notApplicable("see JDASSeamlessFailover1");  }
public void Var008() { notApplicable("see JDASSeamlessFailover1");  }
public void Var009() { notApplicable("see JDASSeamlessFailover1");  }
public void Var010() { notApplicable("see JDASSeamlessFailover1");  }
public void Var011() { notApplicable("see JDASSeamlessFailover1");  }
public void Var012() { notApplicable("see JDASSeamlessFailover1");  }
public void Var013() { notApplicable("see JDASSeamlessFailover1");  }
public void Var014() { notApplicable("see JDASSeamlessFailover1");  }
public void Var015() { notApplicable("see JDASSeamlessFailover1");  }
public void Var016() { notApplicable("see JDASSeamlessFailover1");  }
public void Var017() { notApplicable("see JDASSeamlessFailover1");  }

public void Var018() { notApplicable("see JDASSeamlessFailover2");  }
public void Var019() { notApplicable("see JDASSeamlessFailover2");  }
public void Var020() { notApplicable("see JDASSeamlessFailover2");  }
public void Var021() { notApplicable("see JDASSeamlessFailover2");  }
public void Var022() { notApplicable("see JDASSeamlessFailover2");  }
public void Var023() { notApplicable("see JDASSeamlessFailover2");  }
public void Var024() { notApplicable("see JDASSeamlessFailover2");  }
public void Var025() { notApplicable("see JDASSeamlessFailover2");  }
public void Var026() { notApplicable("see JDASSeamlessFailover2");  }
public void Var027() { notApplicable("see JDASSeamlessFailover2");  }

public void Var028() { notApplicable("see JDASSeamlessFailover3");  }
public void Var029() { notApplicable("see JDASSeamlessFailover3");  }
public void Var030() { notApplicable("see JDASSeamlessFailover3");  }
public void Var031() { notApplicable("see JDASSeamlessFailover3");  }
public void Var032() { notApplicable("see JDASSeamlessFailover3");  }
public void Var033() { notApplicable("see JDASSeamlessFailover3");  }
public void Var034() { notApplicable("see JDASSeamlessFailover3");  }
public void Var035() { notApplicable("see JDASSeamlessFailover3");  }
public void Var036() { notApplicable("see JDASSeamlessFailover3");  }
public void Var037() { notApplicable("see JDASSeamlessFailover3");  }
public void Var038() { notApplicable("see JDASSeamlessFailover3");  }
public void Var039() { notApplicable("see JDASSeamlessFailover3");  }

public void Var040() { notApplicable("see JDASSeamlessFailover4");  }
public void Var041() { notApplicable("see JDASSeamlessFailover4");  }
public void Var042() { notApplicable("see JDASSeamlessFailover4");  }
public void Var043() { notApplicable("see JDASSeamlessFailover4");  }
public void Var044() { notApplicable("see JDASSeamlessFailover4");  }
public void Var045() { notApplicable("see JDASSeamlessFailover4");  }
public void Var046() { notApplicable("see JDASSeamlessFailover4");  }
public void Var047() { notApplicable("see JDASSeamlessFailover4");  }
public void Var048() { notApplicable("see JDASSeamlessFailover4");  }


public void Var049() { notApplicable("see JDASSeamlessFailover5");  }
public void Var050() { notApplicable("see JDASSeamlessFailover5");  }
public void Var051() { notApplicable("see JDASSeamlessFailover5");  }
public void Var052() { notApplicable("see JDASSeamlessFailover5");  }
public void Var053() { notApplicable("see JDASSeamlessFailover5");  }
public void Var054() { notApplicable("see JDASSeamlessFailover5");  }
public void Var055() { notApplicable("see JDASSeamlessFailover5");  }
public void Var056() { notApplicable("see JDASSeamlessFailover5");  }
public void Var057() { notApplicable("see JDASSeamlessFailover5");  }
public void Var058() { notApplicable("see JDASSeamlessFailover5");  }
public void Var059() { notApplicable("see JDASSeamlessFailover5");  }
public void Var060() { notApplicable("see JDASSeamlessFailover5");  }
public void Var061() { notApplicable("see JDASSeamlessFailover5");  }
public void Var062() { notApplicable("see JDASSeamlessFailover5");  }
public void Var063() { notApplicable("see JDASSeamlessFailover5");  }
public void Var064() { notApplicable("see JDASSeamlessFailover5");  }
public void Var065() { notApplicable("see JDASSeamlessFailover5");  }
public void Var066() { notApplicable("see JDASSeamlessFailover5");  }
public void Var067() { notApplicable("see JDASSeamlessFailover5");  }
public void Var068() { notApplicable("see JDASSeamlessFailover5");  }
public void Var069() { notApplicable("see JDASSeamlessFailover5");  }
public void Var070() { notApplicable("see JDASSeamlessFailover5");  }
public void Var071() { notApplicable("see JDASSeamlessFailover5");  }
public void Var072() { notApplicable("see JDASSeamlessFailover5");  }
public void Var073() { notApplicable("see JDASSeamlessFailover5");  }
public void Var074() { notApplicable("see JDASSeamlessFailover5");  }
public void Var075() { notApplicable("see JDASSeamlessFailover5");  }
public void Var076() { notApplicable("see JDASSeamlessFailover5");  }
public void Var077() { notApplicable("see JDASSeamlessFailover5");  }
public void Var078() { notApplicable("see JDASSeamlessFailover5");  }
public void Var079() { notApplicable("see JDASSeamlessFailover5");  }


/* These are the same test as in JDASClientReroute */
/* We run them again to make sure they work with seamless */

public void testPSTypeParametersSwitch(String[][] psTransactions,
String[][][][] psParms, int parmType) {
if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) { 
SocketProxy socketProxy1 = SocketProxy.getSocketProxy(7011, systemName,
JDASTest.AS_DATABASE);
int localPort1 = socketProxy1.getPortNumber();
Thread proxyThread1 = new Thread(socketProxy1);
proxyThread1.start();
socketProxy1.enable(true);

SocketProxy socketProxy2 = SocketProxy.getSocketProxy(7021, systemName,
JDASTest.AS_DATABASE);
int localPort2 = socketProxy2.getPortNumber();
Thread proxyThread2 = new Thread(socketProxy2);
proxyThread2.start();
socketProxy2.enable(false);

SocketProxy socketProxy3 = SocketProxy.getSocketProxy(7031, systemName,
JDASTest.AS_DATABASE);
int localPort3 = socketProxy3.getPortNumber();
Thread proxyThread3 = new Thread(socketProxy3);
proxyThread3.start();
socketProxy3.enable(false);

SocketProxy socketProxy4 = SocketProxy.getSocketProxy(7031, systemName,
JDASTest.AS_DATABASE);
int localPort4 = socketProxy4.getPortNumber();
Thread proxyThread4 = new Thread(socketProxy4);
proxyThread4.start();
socketProxy4.enable(false);

SocketProxy[] proxies = new SocketProxy[4];

proxies[0] = socketProxy1;
proxies[1] = socketProxy2;
proxies[2] = socketProxy3;
proxies[3] = socketProxy4;
StringBuffer sb;
SwitchKillThread switchKillThread;
Connection killerConnection = null; 
Connection connection = null; 
String url;
try {

String killerUrl = url_; 
int ecaIndex = killerUrl.indexOf("enableClientAffinitiesList"); 
if (ecaIndex > 0) {
int semiIndex = killerUrl.indexOf(";", ecaIndex); 
if (semiIndex < 0) {
killerUrl = killerUrl.substring(0,ecaIndex); 
} else {
killerUrl = killerUrl.substring(0,ecaIndex) + killerUrl.substring(semiIndex+1); 
}
}
try { 
killerConnection = testDriver_.getConnection(killerUrl,
systemObject_.getUserId(), encryptedPassword_);
} catch (Exception e) { 
System.out.println("******************************");
System.out.println("Exception connecting to "+killerUrl);
System.out.println("******************************");
throw e; 
}

sb = new StringBuffer();
switchKillThread = new SwitchKillThread(killerConnection, "",
MINIMUM_TRANSACTION_MILLISECONDS, sb, proxies);

url = "jdbc:as400:localhost:7;enableClientAffinitiesList=1;enableSeamlessFailover=1;"
+ "retryIntervalForClientReroute=1;"
+ "clientRerouteAlternateServerName=localhost,localhost,localhost,localhost;"
+ "clientRerouteAlternatePortNumber="
+ localPort1
+ ","
+ localPort2
+ "," + localPort3 + "," + localPort4;

sb.append("Connecting to " + url + "\n");
connection = testDriver_.getConnection(url,
systemObject_.getUserId(), encryptedPassword_);



} catch (Exception e) {
failed(e);
return;
}
testPSTypeParameters(psTransactions, psParms, parmType, connection, killerConnection,
switchKillThread, RUN_SECONDS, sb);
// Cleanup
for (int i = 0; i < proxies.length; i++) {
proxies[i].endService();
}

}
}



public void testDSPSTypeParametersSwitch(String[][] psTransactions,
String[][][][] psParms, int parmType) {
if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) { 
SocketProxy socketProxy1 = SocketProxy.getSocketProxy(7011, systemName,
JDASTest.AS_DATABASE);
int localPort1 = socketProxy1.getPortNumber();
Thread proxyThread1 = new Thread(socketProxy1);
proxyThread1.start();
socketProxy1.enable(true);

SocketProxy socketProxy2 = SocketProxy.getSocketProxy(7021, systemName,
JDASTest.AS_DATABASE);
int localPort2 = socketProxy2.getPortNumber();
Thread proxyThread2 = new Thread(socketProxy2);
proxyThread2.start();
socketProxy2.enable(false);

SocketProxy socketProxy3 = SocketProxy.getSocketProxy(7031, systemName,
JDASTest.AS_DATABASE);
int localPort3 = socketProxy3.getPortNumber();
Thread proxyThread3 = new Thread(socketProxy3);
proxyThread3.start();
socketProxy3.enable(false);

SocketProxy socketProxy4 = SocketProxy.getSocketProxy(7031, systemName,
JDASTest.AS_DATABASE);
int localPort4 = socketProxy4.getPortNumber();
Thread proxyThread4 = new Thread(socketProxy4);
proxyThread4.start();
socketProxy4.enable(false);

SocketProxy[] proxies = new SocketProxy[4];

proxies[0] = socketProxy1;
proxies[1] = socketProxy2;
proxies[2] = socketProxy3;
proxies[3] = socketProxy4;
StringBuffer sb;
SwitchKillThread switchKillThread;
Connection killerConnection = null; 
Connection connection = null; 
String url;
try {

String killerUrl = url_; 
int ecaIndex = killerUrl.indexOf("enableClientAffinitiesList"); 
if (ecaIndex > 0) {
int semiIndex = killerUrl.indexOf(";", ecaIndex); 
if (semiIndex < 0) {
killerUrl = killerUrl.substring(0,ecaIndex); 
} else {
killerUrl = killerUrl.substring(0,ecaIndex) + killerUrl.substring(semiIndex+1); 
}
}
try { 
killerConnection = testDriver_.getConnection(killerUrl,
systemObject_.getUserId(), encryptedPassword_);
} catch (Exception e) { 
System.out.println("******************************");
System.out.println("Exception connecting to "+killerUrl);
System.out.println("******************************");
throw e; 
}

sb = new StringBuffer();
switchKillThread = new SwitchKillThread(killerConnection, "",
MINIMUM_TRANSACTION_MILLISECONDS, sb, proxies);

url = "jdbc:as400:localhost:7;enableClientAffinitiesList=1;enableSeamlessFailover=1;"
+ "retryIntervalForClientReroute=1;"
+ "clientRerouteAlternateServerName=localhost,localhost,localhost,localhost;"
+ "clientRerouteAlternatePortNumber="
+ localPort1
+ ","
+ localPort2
+ "," + localPort3 + "," + localPort4;

sb.append("Connecting to " + url + " via DataSource\n");
AS400JDBCDataSource ds = new AS400JDBCDataSource("loclhost",
systemObject_.getUserId(), encryptedPassword_);
ds.setPortNumber(7);
ds.setEnableClientAffinitiesList(1);
ds.setEnableSeamlessFailover(1);
ds.setRetryIntervalForClientReroute(1);
ds.setClientRerouteAlternateServerName("localhost,localhost,localhost,localhost");
ds.setClientRerouteAlternatePortNumber(localPort1
+ ","
+ localPort2
+ "," + localPort3 + "," + localPort4);
connection = ds.getConnection(); 


} catch (Exception e) {
failed(e);
return;
}
testPSTypeParameters(psTransactions, psParms, parmType, connection, killerConnection,
switchKillThread, RUN_SECONDS, sb);
// Cleanup
for (int i = 0; i < proxies.length; i++) {
proxies[i].endService();
}

}
}

public void testCSTypeParametersSwitch(String[][] csTransactions,
String[][][][] csParms, int parmType) {
if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
SocketProxy socketProxy1 = SocketProxy.getSocketProxy(7011, systemName,
JDASTest.AS_DATABASE);
int localPort1 = socketProxy1.getPortNumber();
Thread proxyThread1 = new Thread(socketProxy1);
proxyThread1.start();
socketProxy1.enable(true);

SocketProxy socketProxy2 = SocketProxy.getSocketProxy(7021, systemName,
JDASTest.AS_DATABASE);
int localPort2 = socketProxy2.getPortNumber();
Thread proxyThread2 = new Thread(socketProxy2);
proxyThread2.start();
socketProxy2.enable(false);

SocketProxy socketProxy3 = SocketProxy.getSocketProxy(7031, systemName,
JDASTest.AS_DATABASE);
int localPort3 = socketProxy3.getPortNumber();
Thread proxyThread3 = new Thread(socketProxy3);
proxyThread3.start();
socketProxy3.enable(false);

SocketProxy socketProxy4 = SocketProxy.getSocketProxy(7031, systemName,
JDASTest.AS_DATABASE);
int localPort4 = socketProxy4.getPortNumber();
Thread proxyThread4 = new Thread(socketProxy4);
proxyThread4.start();
socketProxy4.enable(false);

SocketProxy[] proxies = new SocketProxy[4];

proxies[0] = socketProxy1;
proxies[1] = socketProxy2;
proxies[2] = socketProxy3;
proxies[3] = socketProxy4;
StringBuffer sb;
SwitchKillThread switchKillThread;
String url;
Connection killerConnection; 
Connection connection; 
try {
String killerUrl = url_; 
int ecaIndex = killerUrl.indexOf("enableClientAffinitiesList="); 
if (ecaIndex > 0) { 
int semiIndex = killerUrl.indexOf(";",ecaIndex);
if (semiIndex < 0) {
killerUrl = killerUrl.substring(0,ecaIndex); 
} else {
killerUrl = killerUrl.substring(0,ecaIndex) + killerUrl.substring(semiIndex+1); 
}
}
killerConnection = testDriver_.getConnection(killerUrl,
systemObject_.getUserId(), encryptedPassword_);

sb = new StringBuffer();
switchKillThread = new SwitchKillThread(killerConnection, "",
MINIMUM_TRANSACTION_MILLISECONDS, sb, proxies);

url = "jdbc:as400:localhost:7;enableClientAffinitiesList=1;enableSeamlessFailover=1;"
+ "retryIntervalForClientReroute=1;"
+ "clientRerouteAlternateServerName=localhost,localhost,localhost,localhost;"
+ "clientRerouteAlternatePortNumber="
+ localPort1
+ ","
+ localPort2
+ "," + localPort3 + "," + localPort4;
connection = testDriver_.getConnection(url,
systemObject_.getUserId(), encryptedPassword_);

} catch (Exception e) {
failed(e);
return;
}
testCSTypeParameters(csTransactions, csParms, parmType, connection, killerConnection,
switchKillThread, RUN_SECONDS, sb);
// Cleanup
for (int i = 0; i < proxies.length; i++) {
proxies[i].endService();
}
} 
}


public void testDSCSTypeParametersSwitch(String[][] csTransactions,
String[][][][] csParms, int parmType) {
if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
SocketProxy socketProxy1 = SocketProxy.getSocketProxy(7011, systemName,
JDASTest.AS_DATABASE);
int localPort1 = socketProxy1.getPortNumber();
Thread proxyThread1 = new Thread(socketProxy1);
proxyThread1.start();
socketProxy1.enable(true);

SocketProxy socketProxy2 = SocketProxy.getSocketProxy(7021, systemName,
JDASTest.AS_DATABASE);
int localPort2 = socketProxy2.getPortNumber();
Thread proxyThread2 = new Thread(socketProxy2);
proxyThread2.start();
socketProxy2.enable(false);

SocketProxy socketProxy3 = SocketProxy.getSocketProxy(7031, systemName,
JDASTest.AS_DATABASE);
int localPort3 = socketProxy3.getPortNumber();
Thread proxyThread3 = new Thread(socketProxy3);
proxyThread3.start();
socketProxy3.enable(false);

SocketProxy socketProxy4 = SocketProxy.getSocketProxy(7031, systemName,
JDASTest.AS_DATABASE);
int localPort4 = socketProxy4.getPortNumber();
Thread proxyThread4 = new Thread(socketProxy4);
proxyThread4.start();
socketProxy4.enable(false);

SocketProxy[] proxies = new SocketProxy[4];

proxies[0] = socketProxy1;
proxies[1] = socketProxy2;
proxies[2] = socketProxy3;
proxies[3] = socketProxy4;
StringBuffer sb;
SwitchKillThread switchKillThread;
Connection killerConnection; 
Connection connection; 
try {
String killerUrl = url_; 
int ecaIndex = killerUrl.indexOf("enableClientAffinitiesList="); 
if (ecaIndex > 0) { 
int semiIndex = killerUrl.indexOf(";",ecaIndex);
if (semiIndex < 0) {
killerUrl = killerUrl.substring(0,ecaIndex); 
} else {
killerUrl = killerUrl.substring(0,ecaIndex) + killerUrl.substring(semiIndex+1); 
}
}
killerConnection = testDriver_.getConnection(killerUrl,
systemObject_.getUserId(), encryptedPassword_);

sb = new StringBuffer();
switchKillThread = new SwitchKillThread(killerConnection, "",
MINIMUM_TRANSACTION_MILLISECONDS, sb, proxies);

AS400JDBCDataSource ds = new AS400JDBCDataSource("localhost", 
systemObject_.getUserId(), encryptedPassword_);
ds.setPortNumber(7);
ds.setEnableClientAffinitiesList(1);
ds.setEnableSeamlessFailover(1);
ds.setRetryIntervalForClientReroute(1);
ds.setClientRerouteAlternateServerName("localhost,localhost,localhost,localhost");
ds.setClientRerouteAlternatePortNumber(localPort1
+ ","
+ localPort2
+ "," + localPort3 + "," + localPort4 );

connection = ds.getConnection(); 

} catch (Exception e) {
failed(e);
return;
}
testCSTypeParameters(csTransactions, csParms, parmType, connection, killerConnection,
switchKillThread, RUN_SECONDS, sb);
// Cleanup
for (int i = 0; i < proxies.length; i++) {
proxies[i].endService();
}
} 
}

protected boolean checkResultSet(ResultSet rs, String expected, StringBuffer sb) throws SQLException {
rs.next(); 
ResultSetMetaData rsmd = rs.getMetaData(); 
StringBuffer resultSb = new StringBuffer(); 
for (int i = 1 ; i <= rsmd.getColumnCount(); i++) { 
if (i > 1) { resultSb.append(",");}
int columnType = rsmd.getColumnType(1); 
switch (columnType) {
case Types.BINARY:
case Types.VARBINARY:
// When calling getString, toolbox converts to hex representation
resultSb.append(rs.getString(i));
break; 

default:
resultSb.append(rs.getString(i));
break; 
}
}
String result = resultSb.toString(); 
if (! expected.equals(result)) {
sb.append("FAILED: got row :'"+result+"'\n");
sb.append("   Expected row :'"+expected+"'\n");
return false; 
}
return true; 
}





}
