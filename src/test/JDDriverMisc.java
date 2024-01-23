///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDriverMisc.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDDriverMisc.java
//
// Classes:      JDDriverMisc
//
////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDriver;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Driver;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.util.Enumeration;
import java.util.Hashtable;
import java.sql.Connection;


/**
Testcase JDDriverMisc.  This tests the following methods
of the JDBC Driver class:

<ul>
<li>getMajorVersion()
<li>getMinorVersion()
<li>jdbcCompliant()
<li>toString()
<li>getParentLogger()
</ul>
**/
public class JDDriverMisc
extends JDTestcase
{



    // Private data.
    private Driver      driver_;

    private int         vrm_;

    static      int            VRM_450 = AS400.generateVRM(4,5,0);
    static      int            VRM_510 = AS400.generateVRM(5,1,0);
    static      int            VRM_520 = AS400.generateVRM(5,2,0);
    static      int            VRM_530 = AS400.generateVRM(5,3,0);



    /**
    Constructor.
    **/
    public JDDriverMisc (AS400 systemObject,
                         Hashtable namesAndVars,
                         int runMode,
                         FileOutputStream fileOutputStream,
                        
                         String password,
                         String powerUserID,
                         String powerPassword)
    {
        super (systemObject, "JDDriverMisc",
               namesAndVars, runMode, fileOutputStream, 
               password, powerUserID, powerPassword);

        systemObject_ = systemObject;

    }



    /**
    Performs setup needed before running variations.

    @exception Exception If an exception occurs.
    **/
    protected void setup ()
    throws Exception
    {
        try {
        driver_ = DriverManager.getDriver(baseURL_);
	if(getDriver () == JDTestDriver.DRIVER_TOOLBOX) {
	    vrm_ = systemObject_.getVRM();
	} else {
	    vrm_ = testDriver_.getRelease();
	}
        } catch (Exception e) {
          System.out.println("ERROR:  Exception in setup with URL="+baseURL_);
          throw e;
        }
    }



    /**
    getMajorVersion() - The major version for the Toolbox JDBC Driver should
    be 8.  The major version for the Native JDBC Driver switched to 5 with
    the release of V5R1.  No exceptions should be thrown.
    **/
    public void Var001 ()
    {
        try
        {
            if(getDriver () == JDTestDriver.DRIVER_TOOLBOX) {
		int expectedMajorVersion = 13;
		// Major version is 13 for v7r5!!


                assertCondition (driver_.getMajorVersion () == expectedMajorVersion,
				 "Driver major version is "+driver_.getMajorVersion ()+
				 " sb "+expectedMajorVersion ); // @E1C @F1c @G1C @H1C
	    } else if (getDriver () == JDTestDriver.DRIVER_JTOPENLITE) {
		int expectedMajorVersion = 1;

                assertCondition (driver_.getMajorVersion () == expectedMajorVersion,
				 "Driver major version is "+driver_.getMajorVersion ()+
				 " sb "+expectedMajorVersion );


            } else if (getDriver() == JDTestDriver.DRIVER_JCC) {
              int expected=1;
              assertCondition (driver_.getMajorVersion () ==expected,
                    "expected version "+expected+" but got version "+driver_.getMajorVersion ());

            } else {
                assertCondition (driver_.getMajorVersion () == 5);
            }
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    Driver.getMinorVersion() - The minor version should be correct.  No exceptions
    should be thrown.
    **/
    public void Var002 ()
    {
        try
        {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            int expected = 5;
            assertCondition (driver_.getMinorVersion () == expected,
                             "expected minor version of "+expected+" but got "+driver_.getMinorVersion());

          }
          else if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {                        //@K1A added changes for detecting toolbox's minor version
              // minor version is associated with toolbox ptf levels
              //This fails if minor version in code does not match minor version in jar manifest
            int expected = getToolboxDriverMinorVersion();
            assertCondition(driver_.getMinorVersion() == expected, "Toolbox driver_.getMinorVersion="+driver_.getMinorVersion()+", expected "+expected + " --jar manifest version must match code version");
          }
          else {
            assertCondition (driver_.getMinorVersion () == 0, "Minor version of "+driver_.getMinorVersion ()+" did not match 0 ");
          }
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    jdbcCompliant() - Should return true.  No exceptions
    should be thrown.
    **/
    public void Var003 ()
    {
        try
        {
	    boolean compliant = driver_.jdbcCompliant ();
	    boolean expected = true;
	    if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
		expected = false;
	    }
            assertCondition (compliant == expected, "driver.jdbcClient() returned "+compliant);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
     * toString() - Returns the name of the driver.
     *
     * @D1C
     */
  public void Var004() {
    try {
      String s = driver_.toString();

      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
        assertCondition(s.equals("AS/400 Toolbox for Java JDBC Driver"));
      } else if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
    	  String expected = "jtopenlite JDBC Driver";
        assertCondition(s.equals(expected), "Got "+s+" expected "+expected);
      } else if (getDriver() == JDTestDriver.DRIVER_JCC) {
        String expected = "com.ibm.db2.jcc.DB2Driver";
        assertCondition(s.indexOf(expected)>=0 ,
                        "expected to find name '"+expected+"' but got '"+s+"'");
      } else {
        assertCondition(s.equals("DB2 for OS/400 JDBC Driver"));
      }

    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }




    /**
     * Driver internals - Attempt to cause an error during startup due to
     * improper use of the SQL CLI Environment internally. This support changed
     * in v4r5 and this test is to make sure that the JDBC driver is handling
     * the change correctly.
     * <P>
     * This test tries to reload the DB2Driver class.
     */
    public void Var005()
    {
        if(getDriver () == JDTestDriver.DRIVER_TOOLBOX)
            notApplicable();
        else
        {
            try
            {
                Class.forName("com.ibm.db2.jdbc.app.DB2Driver");
                assertCondition(true);
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception");
            }
        }
    }



    /**
    Driver internals - Attempt to cause an error during startup due to
    improper use of the SQL CLI Environment internally.  This support
    changed in v4r5 and this test is to make sure that the JDBC driver
    is handling the change correctly.
    <P>
    This test creates a new DB2Driver object directly.
    **/
    public void Var006()
    {
        if(getDriver () == JDTestDriver.DRIVER_TOOLBOX ||
            getDriver() == JDTestDriver.DRIVER_JCC)
            notApplicable();
        else
        {
            try
            {
                Object drvr1 = JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2Driver");
                Object drvr2 = JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2Driver");
                assertCondition(true, "Allocated Drivers "+drvr1+" "+drvr2 );
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception");
            }
        }
    }



    /**
    Driver internals - Attempt to cause an error during startup due to
    improper use of the SQL CLI Environment internally.  This support
    changed in v4r5 and this test is to make sure that the JDBC driver
    is handling the change correctly.
    <P>
    This test tries to register the driver again.
    **/
    public void Var007()
    {
        if(getDriver () == JDTestDriver.DRIVER_TOOLBOX ||
            getDriver() == JDTestDriver.DRIVER_JCC)
            notApplicable();
        else
        {
            try
            {
                Object drvr1 = JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2Driver");
                Object drvr2 = JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2Driver");
                DriverManager.registerDriver((Driver) drvr1);
                DriverManager.registerDriver((Driver) drvr2);
                assertCondition(true);
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception");
            }
        }
    }



    /**
    Driver internals - Attempt to cause an error during startup due to
    improper use of the SQL CLI Environment internally.  This support
    changed in v4r5 and this test is to make sure that the JDBC driver
    is handling the change correctly.
    <P>
    This test gets a new connection after registering the driver again.
    **/
    public void Var008()
    {
        if(getDriver () == JDTestDriver.DRIVER_TOOLBOX ||
            getDriver() == JDTestDriver.DRIVER_JCC)
            notApplicable();
        else
        {
            try
            {
                Driver drvr1 = (Driver) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2Driver");
                Driver drvr2 = (Driver) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2Driver");
                DriverManager.registerDriver(drvr1);
                DriverManager.registerDriver(drvr2);
                Connection c1;
                Connection c2;
		    c1 = DriverManager.getConnection("jdbc:db2:*local;errors=full", null, null);
		    c2 = DriverManager.getConnection("jdbc:db2:*local;errors=full", null, null);
                c1.close();
                c2.close();
                assertCondition(true);
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception");
            }
        }
    }



    /**
    Driver internals - Attempt to cause an error during a run by instantiating
    a new DB2Driver while there is work currently pending.  There was a problem
    that Servlets could run into with the reinitialization of the underlying
    data structures while a thread was working on a Servlet.
    **/
    public void Var009()
    {
        if(getDriver () == JDTestDriver.DRIVER_TOOLBOX ||
            getDriver() == JDTestDriver.DRIVER_JCC)
            notApplicable();
        else
        {
            try
            {

                Connection c ;
                c = DriverManager.getConnection("jdbc:db2:*local;errors=full", null, null);
                Statement s = c.createStatement();
                ResultSet rs = s.executeQuery("SELECT * FROM QSYS2.SYSPROCS");

                Driver drvr1 = (Driver) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2Driver");

                DriverManager.registerDriver(drvr1);
                
                rs.next();
                rs.getString(2);  // This will cause an exception if the user space
                                                 // was re-initialized.
                s.close();
                c.close();

                // Just getting here is passing. :)
                assertCondition(true);

            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception");
            }
        }
    }


    /**
     * Turn on server trace
     */
    public void Var010() {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        if (getDriver() == JDTestDriver.DRIVER_JCC) {
            notApplicable("JCC doesn't have server trace");
        } else {
            if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
                if ((pwrSysUserID_ == null) || (pwrSysEncryptedPassword_ == null)) {
                    failed("-pwrSys uid,pwd not specified.  Need to be qsecofr to run server trace");
                    return;
                }
                try {
                    String url = baseURL_ + ";server trace=63" + ";user=" + pwrSysUserID_
                    + ";password=" + PasswordVault.decryptPasswordLeak(pwrSysEncryptedPassword_);

                    System.out.println();
                    System.out.println();
                    System.out.println("Starting server trace testcase   ");
                    System.out.println();
                    System.out.println();

                    DriverManager.registerDriver(new com.ibm.as400.access.AS400JDBCDriver());
                    connection = DriverManager.getConnection(url + ";login timeout=1000;errors=full"); //up socket timeout since v7r1 takes a long time to return


                    if( vrm_ >= VRM_V7R1M0)
                    {
                        // on v7r1, we reach limit of number of records and job gets msgw.
                        try {
                            CommandCall cmd = new CommandCall(systemObject_, "ADDRPYLE SEQNBR(4072) MSGID(CPA4072) RPY(R)");
                            cmd.run();
                            AS400Message[] messageList = cmd.getMessageList();
                            for(int i = 0; i < messageList.length; ++i)
                                System.out.println(messageList[i]);
                        } catch (Exception e) {
                            System.out.println("Warning:  ADDRPYLE failed with exception: "+e);

                        }
                        // We need to change the database server job to use
                        // the system reply list.
                        Statement s = connection.createStatement();
                        s.executeUpdate("CALL QSYS.QCMDEXC('CHGJOB INQMSGRPY(*SYSRPYL)', 0000000026.00000)");
                        s.close();

                    }


                    statement = connection.createStatement();
                    rs = statement.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
                    rs.close();
                    rs = null;
                    statement.close();
                    statement = null;
                    connection.close();
                    connection = null;

                    System.out.println();
                    System.out.println();
                    System.out
                    .println("Server trace testcase complete.  Read http://daw/jdbcServerTrace.html to make sure the files are on the server.  The server job id is part of the stuff dumped to standard out. ");
                    System.out.println();
                    System.out.println();

                    succeeded();
                } catch (Exception e) {
                    failed(e, "Unexpected Exception");
                }

                try {
                    if (rs != null) rs.close();
                } catch (Exception e) {
                }
                try {
                    if (statement != null) statement.close();
                } catch (Exception e) {
                }
                try {
                  if (connection != null)    connection.close();
                } catch (Exception e) {
                }
            } else {
                notApplicable();
            }
        }
    }


    /**
     * Turn on server trace
     */
    public void Var011()
    {
        Connection connection = null;
        Statement  statement  = null;
        ResultSet  rs         = null;

        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC doesn't have server trace");
        } else {

        if(getDriver () == JDTestDriver.DRIVER_TOOLBOX)
        {
            if((pwrSysUserID_ == null) || (pwrSysEncryptedPassword_ == null))
            {
                failed("-pwrSys uid,pwd not specified.  Need to be qsecofr to run server trace");
                return;
            }
            try
            {
                String url = baseURL_
                             + ";user=" + pwrSysUserID_
                             + ";password=" + PasswordVault.decryptPasswordLeak(pwrSysEncryptedPassword_);

                System.out.println();
                System.out.println();
                System.out.println("Starting server trace testcase 2 ");
                System.out.println("To make sure trace can be started from the command line, rerun this testcase as ");
                System.out.println();
                System.out.println("java -Dcom.ibm.as400.access.ServerTrace.JDBC=63 test.JDDriverTest -uid java -pwd jteam1 -system rchasdm3 -tc JDDriverMisc -var 11");
                System.out.println();
                System.out.println();

                DriverManager.registerDriver(new com.ibm.as400.access.AS400JDBCDriver());
                connection = DriverManager.getConnection(url);
                statement  = connection.createStatement();
                rs         = statement.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
                rs.close();
                rs = null;
                statement.close();
                statement = null;
                connection.close();
                connection = null;

                System.out.println();
                System.out.println();
                System.out.println("Server trace testcase complete 2.  Read http://daw/jdbcServerTrace.html to make sure the files are on the server. ");
                System.out.println();
                System.out.println();

                succeeded();
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }

            try
            {
                if (rs != null) rs.close();
            }
            catch(Exception e)
            {
            }
            try
            {
                if (statement != null) statement.close();
            }
            catch(Exception e)
            {
            }
            try
            {
                if (connection != null) connection.close();
            }
            catch(Exception e)
            {
            }
        }
        else
        {
            notApplicable();
        }
        }
    }

    // set the package CCSID property to send statements to the server
    // in UTF-16
    // @G0A - Toolbox only
    public void Var012()
    {
        if(getDriver() == JDTestDriver.DRIVER_NATIVE ||
            getDriver() == JDTestDriver.DRIVER_JCC )
            notApplicable("Toolbox package CCSID test ");
        else
        {
            if(vrm_ < VRM_530)
                notApplicable("v5r3 and later only");
            else
            {
                try
                {
                    boolean success = true;
                    Connection connection = testDriver_.getConnection(baseURL_ + ";package ccsid=1200;errors=full", userId_, encryptedPassword_);
                    Statement statement = connection.createStatement();

                    // insert a literal containing surrogates into table
                    statement.execute("CREATE TABLE " + JDDriverTest.COLLECTION + ".testtbl (id INTEGER, name VARGRAPHIC(50) CCSID 1200)");

                    // put stuff into the table
                    PreparedStatement ps = connection.prepareStatement("INSERT INTO " + JDDriverTest.COLLECTION + ".testtbl (id, name) VALUES (?, ?)");

                    ps.setInt(1, 1);
                    ps.setString(2, "my \uD8FA\uDFFA\uD8AA\uDFEA\uD8BA\uDFCA name");
                    ps.addBatch();

                    ps.setInt(1, 2);
                    ps.setString(2, "my \uD8FB\uDFFA\uD8AA\uDFEA\uD8BA\uDFCA name");
                    ps.addBatch();

                    ps.setInt(1, 3);
                    ps.setString(2, "my \uD8FC\uDFFA\uD8AA\uDFEA\uD8BA\uDFCA name");
                    ps.addBatch();

                    ps.setInt(1, 4);
                    ps.setString(2, "my \uD8FD\uDFFA\uD8AA\uDFEA\uD8BA\uDFCA name");
                    ps.addBatch();

                    ps.setInt(1, 5);
                    ps.setString(2, "my \uD8FE\uDFFA\uD8AA\uDFEA\uD8BA\uDFCA name");
                    ps.addBatch();

                    ps.executeBatch();
                    ps.close();

                    // get stuff out of the table
                    ResultSet rs = statement.executeQuery("SELECT name FROM " + JDDriverTest.COLLECTION + ".testtbl WHERE name='my \uD8FA\uDFFA\uD8AA\uDFEA\uD8BA\uDFCA name'");
                    int count = 0;
                    while(rs.next())
                    {
                        count++;
                    }

                    if(count != 1)
                        success = false;

                    rs.close();

                    statement.execute("DROP TABLE " + JDDriverTest.COLLECTION + ".testtbl");
                    statement.close();
                    connection.close();

                    if(!success)
                        failed("Number of rows found " + count + " != 1");
                    else
                        succeeded();
                }
                catch(Exception e)
                {
                    failed(e, "Unexpected Exception");
                }
            }
        }
    }

    // set the package CCSID property to send statements to the server
    // in UCS-2 - this should still work for us because we send the
    // SQL as UCS-2 even though it is UTF-16, the database doesn't
    // make sure there are no surrogates...
    // @G0A - Toolbox only
    public void Var013()
    {
        if(getDriver() == JDTestDriver.DRIVER_NATIVE ||
            getDriver() == JDTestDriver.DRIVER_JCC )
            notApplicable();
        else
        {
            if(vrm_ < VRM_530)
                notApplicable("v5r3 and later only");
            else
            {
                try
                {
                    boolean success = true;
                    Connection connection = testDriver_.getConnection(baseURL_ + ";package ccsid=13488;errors=full", userId_, encryptedPassword_);
                    Statement statement = connection.createStatement();

                    // insert a literal containing surrogates into table
                    statement.execute("CREATE TABLE " + JDDriverTest.COLLECTION + ".testtbl (id INTEGER, name VARGRAPHIC(50) CCSID 13488)");

                    // put stuff into the table
                    PreparedStatement ps = connection.prepareStatement("INSERT INTO " + JDDriverTest.COLLECTION + ".testtbl (id, name) VALUES (?, ?)");

                    ps.setInt(1, 1);
                    ps.setString(2, "my \uD8FA\uDFFA\uD8AA\uDFEA\uD8BA\uDFCA name");
                    ps.addBatch();

                    ps.setInt(1, 2);
                    ps.setString(2, "my \uD8FB\uDFFA\uD8AA\uDFEA\uD8BA\uDFCA name");
                    ps.addBatch();

                    ps.setInt(1, 3);
                    ps.setString(2, "my \uD8FC\uDFFA\uD8AA\uDFEA\uD8BA\uDFCA name");
                    ps.addBatch();

                    ps.setInt(1, 4);
                    ps.setString(2, "my \uD8FD\uDFFA\uD8AA\uDFEA\uD8BA\uDFCA name");
                    ps.addBatch();

                    ps.setInt(1, 5);
                    ps.setString(2, "my \uD8FE\uDFFA\uD8AA\uDFEA\uD8BA\uDFCA name");
                    ps.addBatch();

                    ps.executeBatch();
                    ps.close();

                    // get stuff out of the table
                    ResultSet rs = statement.executeQuery("SELECT name FROM " + JDDriverTest.COLLECTION + ".testtbl WHERE name='my \uD8FA\uDFFA\uD8AA\uDFEA\uD8BA\uDFCA name'");
                    int count = 0;
                    while(rs.next())
                    {
                        count++;
                    }

                    if(count != 1)
                        success = false;

                    rs.close();

                    statement.execute("DROP TABLE " + JDDriverTest.COLLECTION + ".testtbl");
                    statement.close();
                    connection.close();

                    if(!success)
                        failed("Number of rows found " + count + " != 1");
                    else
                        succeeded();
                }
                catch(Exception e)
                {
                    failed(e, "Unexpected Exception");
                }
            }
        }
    }

    // insert data with surrogates into a column tagged as UTF-8
    // @G0A - Toolbox only
    public void Var014()
    {
        if(getDriver() == JDTestDriver.DRIVER_NATIVE ||
            getDriver() == JDTestDriver.DRIVER_JCC )
            notApplicable();
        else
        {
            if(vrm_ < VRM_530)
                notApplicable("v5r3 and later only");
            else
            {
                try
                {
                    boolean success = true;
                    Connection connection = testDriver_.getConnection(baseURL_ + ";package ccsid=1200;errors=full", userId_, encryptedPassword_);
                    Statement statement = connection.createStatement();

                    // insert a literal containing surrogates into table
                    statement.execute("CREATE TABLE " + JDDriverTest.COLLECTION + ".testtbl (id INTEGER, name VARCHAR(50) CCSID 1208)");

                    // put stuff into the table
                    PreparedStatement ps = connection.prepareStatement("INSERT INTO " + JDDriverTest.COLLECTION + ".testtbl (id, name) VALUES (?, ?)");

                    ps.setInt(1, 1);
                    ps.setString(2, "my \uD8FA\uDFFA\uD8AA\uDFEA\uD8BA\uDFCA name");
                    ps.addBatch();

                    ps.setInt(1, 2);
                    ps.setString(2, "my \uD8FB\uDFFA\uD8AA\uDFEA\uD8BA\uDFCA name");
                    ps.addBatch();

                    ps.setInt(1, 3);
                    ps.setString(2, "my \uD8FC\uDFFA\uD8AA\uDFEA\uD8BA\uDFCA name");
                    ps.addBatch();

                    ps.setInt(1, 4);
                    ps.setString(2, "my \uD8FD\uDFFA\uD8AA\uDFEA\uD8BA\uDFCA name");
                    ps.addBatch();

                    ps.setInt(1, 5);
                    ps.setString(2, "my \uD8FE\uDFFA\uD8AA\uDFEA\uD8BA\uDFCA name");
                    ps.addBatch();

                    ps.executeBatch();
                    ps.close();

                    // get stuff out of the table
                    ResultSet rs = statement.executeQuery("SELECT name FROM " + JDDriverTest.COLLECTION + ".testtbl WHERE name='my \uD8FA\uDFFA\uD8AA\uDFEA\uD8BA\uDFCA name'");
                    int count = 0;
                    while(rs.next())
                    {
                        count++;
                    }

                    if(count != 1)
                        success = false;

                    rs.close();

                    statement.execute("DROP TABLE " + JDDriverTest.COLLECTION + ".testtbl");
                    statement.close();
                    connection.close();

                    if(!success)
                        failed("Number of rows found " + count + " != 1");
                    else
                        succeeded();
                }
                catch(Exception e)
                {
                    failed(e, "Unexpected Exception");
                }
            }
        }
    }

    /*
        Driver double registration.  (make sure driver is only tried once for connection)
        Since driver is registered twice in DriverManager via DriverManager.registerDriver(new AS400JDBCDriver()),
        remove extra driver references now so we don't waste resources by continuing to try, and also so we
        don't lock out id if pwd is not correct.
     */
    public void Var015()
    {
        if ( getDriver() != JDTestDriver.DRIVER_TOOLBOX)
        {
            notApplicable();
            return;
        }

        if (proxy_ != null && (!proxy_.equals(""))) {
            notApplicable("Double registration test does not work for proxy driver");
            return;
        }


        try
        {
            // boolean success = true;

            //add drivers many times to registration
            if (getDriver() == JDTestDriver.DRIVER_TOOLBOX)
            {
                DriverManager.registerDriver(new com.ibm.as400.access.AS400JDBCDriver());
                DriverManager.registerDriver(new com.ibm.as400.access.AS400JDBCDriver());
                DriverManager.registerDriver(new com.ibm.as400.access.AS400JDBCDriver());
                DriverManager.registerDriver(new com.ibm.as400.access.AS400JDBCDriver());
                DriverManager.registerDriver(new com.ibm.as400.access.AS400JDBCDriver());
            }else if (getDriver() == JDTestDriver.DRIVER_NATIVE)
            {
                Driver drvr1 = (Driver) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2Driver");
                Driver drvr2 = (Driver) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2Driver");
                Driver drvr3 = (Driver) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2Driver");
                Driver drvr4 = (Driver) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2Driver");
                Driver drvr5 = (Driver) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2Driver");

                DriverManager.registerDriver(drvr1); 
                DriverManager.registerDriver(drvr2); 
                DriverManager.registerDriver(drvr3); 
                DriverManager.registerDriver(drvr4); 
                DriverManager.registerDriver(drvr5); 
            }

            //try to connect, but get expected error
            //TB unregisters extra AS400JDBCDrivers when a connection error occurs
            try{
                DriverManager.getConnection(baseURL_ + ";prompt=false;errors=full", "noname", "nopass");
                //should not have to internally loop through all drivers
		System.out.println("Warning.  getConnection did not get an error");

            }catch(Exception e){
                //ignore
            }
            Enumeration en = DriverManager.getDrivers();   //here, we should only have one AS400JDBCDriver registered after getting error

	    StringBuffer driverList = new StringBuffer();
            Driver nextDriver = null;
            int drivercount = 0;
            while (en.hasMoreElements())
            {
                nextDriver = (Driver) en.nextElement();
                if (getDriver() == JDTestDriver.DRIVER_TOOLBOX)
                {
		    if(nextDriver instanceof AS400JDBCDriver) {
			driverList.append("Found driver "+nextDriver.toString()+" class:"+nextDriver.getClass().getName()+"\n");
                        drivercount++;
		    }

                }
                else if (getDriver() == JDTestDriver.DRIVER_NATIVE)
                {
                    if(JDReflectionUtil.instanceOf(nextDriver,"com.ibm.db2.jdbc.app.DB2Driver"))
                        drivercount++;
                }

            }


            if(drivercount != 1)
                failed("Number drivers registered = " + drivercount + " sb 1 Driver List is\n"+driverList.toString());
            else
                succeeded();
        }
        catch(Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }


  // Check for SQLConversionSettings objects. There should not be many of them
  public void Var016() {

       if (proxy_ != null && (!proxy_.equals(""))) {
            notApplicable("SQLConversionSettings test not needed for proxy driver");
            return;
        }

    try {
      // Tools APIs to checkout are only in JDK 1.6.
      if (checkJdbc40()) {
        boolean passed = true;
        StringBuffer sb = new StringBuffer();
        JVMRunUtility runUtility = new JVMRunUtility("test.JVMRunJDBC",
            baseURL_ + " " + userId_ + " " + PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverMisc.16") + " 200", 30);

        runUtility.startJVM();
        runUtility.waitForExit();
        String[] topList = runUtility.getTopList();
        System.out.println("topList is ");
        if (topList == null) {
          System.out.println("Error:  topList is null.  was exit called?");
          passed = false;
        } else {
          for (int i = 0; i < topList.length; i++) {
            if (topList[i].indexOf("SQLConversionSetting") > 0) {
              passed = false;
              sb.append("Error:  " + topList[i] + " found\n");
            }
            System.out.println((i + 1) + ": " + topList[i]);
          }
        }
        sb.append("OUTPUT\n");
        sb.append(runUtility.getOutput() + "\n");
        sb.append("ERROR\n");
        sb.append(runUtility.getError() + "\n");
        assertCondition(passed, sb);

      }

    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }

  }

  
  
  // Make user login timeout works. 
  public void Var017() {
    
    StringBuffer sb = new StringBuffer(); 
    sb.append(" -- testcase added 08/26/2019 to test login timeout\n"); 
    try {
      if (checkToolbox()) {
        if (true) { 
        notApplicable("Attempt to test login timeout"); 
        return; 
        } 
        boolean passed = true;
        int port = 1287; 
        ServerSocket serverSocket = null ;
        Socket socket = null; 
        boolean retry = true; 
        while (retry) { 
          retry = false; 
          try { 
            // 
            // What this is trying to do is to create a server socket that will 
            // timeout.   With this code, a timeout was seen on the IBM i, but
            // the timeout does not occur on the PC. 
            serverSocket = new ServerSocket(port,1 );
            serverSocket.setReceiveBufferSize(1);
            sb.append("ReceiveBufferSize is " + serverSocket.getReceiveBufferSize()+"\n"); 
          } catch (IOException ioe ) { 
            port ++; 
            System.out.println("Caught "+ioe+" moving to port "+port+"\n");
            retry = true; 
          }
        }
        // socket = new Socket("localhost",port);  
        Connection c = null;
        String url = "jdbc:as400:localhost:"+port+";login timeout=5";
        long startTime = System.currentTimeMillis(); 
        try { 
          
          c = DriverManager.getConnection(url,"user","password"); 
          passed = false; 
          sb.append("Got connection with "+url+"\n"); 
          
        } catch (Exception ex ) { 
          long finishTime = System.currentTimeMillis(); 
          if (finishTime - startTime < 5000) {
            passed = false; 
            sb.append("Error:  elapsed time was only "+(finishTime-startTime)+" ms\n"); 
          }
          passed = false; 
          sb.append("Unexpected exception "+ex+"\n"); 
          
        }
        
        if (serverSocket != null) { serverSocket.close(); }
        if (socket != null)  { socket.close(); } 
        assertCondition(passed, sb);

      }

    } catch (Exception e) {
      failed(e, "Unexpected Exception "+sb.toString());
    }

  }


  
  
}



