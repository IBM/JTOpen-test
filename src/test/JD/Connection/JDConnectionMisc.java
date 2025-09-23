///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionMisc.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDConnectionMisc.java
//
// Classes:      JDConnectionMisc
//
////////////////////////////////////////////////////////////////////////

package test.JD.Connection;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCConnection;

import test.JDConnectionTest;
import test.JDJSTPTestcase;
import test.JDJobName;
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;
import test.PasswordVault;
import test.JD.JDSetupCollection;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;



/**
Testcase JDConnectionMisc.  This tests the following methods
of the JDBC Connection class:

<ul>
<li>getCatalog()
<li>setCatalog()
<li>getMetaData()
<li>getTypeMap()
<li>setTypeMap()
<li>toString()
<li>isValid()
</ul>
**/
public class JDConnectionMisc
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDConnectionMisc";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDConnectionTest.main(newArgs); 
   }



    // Private data.
    private              Connection     connection_;
    private              Connection     closedConnection_;
    private              boolean        remoteConnection_ = false; 
    static boolean systemNamingLibrariesCreated = false;
    static String  baseCollection = "BASECOL";
    static Vector<String>  systemNamingLibraries = null ;
    static boolean cleanupLibraries = true;
    private Connection pwrConnection_;
    private Statement  pwrStatement_; 
    private StringBuffer sb = new StringBuffer(); 
/**
Constructor.
**/
    public JDConnectionMisc (AS400 systemObject,
                             Hashtable<String,Vector<String>> namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             
                             String password,
                             String pwrUID,     //@H2A
                             String pwrPwd) {   //@H2A
        super (systemObject, "JDConnectionMisc",
               namesAndVars, runMode, fileOutputStream,
               password);
        pwrSysUserID_ = pwrUID;   //@H2A
        pwrSysEncryptedPassword_ = PasswordVault.getEncryptedPassword(pwrPwd); 
    }



/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
    throws Exception
    {
        connection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);


	remoteConnection_ = isRemoteConnection(connection_);

	pwrConnection_ = testDriver_.getConnection (baseURL_, pwrSysUserID_, pwrSysEncryptedPassword_);
	pwrStatement_ = pwrConnection_.createStatement(); 


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
        cleanupSystemNamingLibraries(connection_ );
        connection_.close ();
	pwrConnection_.close(); 
    }



/**
Queries the relational database directory to get the catalog name for
*LOCAL.

@exception SQLException If a database access error occurs.
**/
    protected String getRealCatalogName()
    throws SQLException
    {
        Statement s = connection_.createStatement();
        ResultSet rs = s.executeQuery("SELECT VARCHAR(DBXRDBN, 128) FROM QSYS.QADBXRDBD WHERE DBXRMTN='*LOCAL'");
        rs.next();
        return rs.getString(1).trim();
    }



/**
getCatalog() - Returns the name of the AS/400 to which
we are connected.

SQL400 - The Native JDBC driver has changed so that it will
         always return the real catalog name, even when the user
         connects with the special values of *LOCAL or localhost.
         The value will be retrieved from the database if needed.
         Note that today this is only going to work when you
         are running the tests to the local database.
**/
    public void Var001() {
        try {
            boolean passed = true; 
            sb.setLength(0); 
            
            String catalog = connection_.getCatalog ();

            //@F1D if (getDriver () == JDTestDriver.DRIVER_NATIVE) {
            if ((system_.equalsIgnoreCase("localhost")) ||
                (system_.equalsIgnoreCase("*LOCAL"))) {
                assertCondition(catalog.equals(getRealCatalogName()));
                return;
            }

            passed = (catalog.equals (system_.toUpperCase ()))
                || (catalog.equalsIgnoreCase("localhost"))
                || (catalog.equalsIgnoreCase("*LOCAL"))
                || (catalog.equals(getRealCatalogName())); 
            if (!passed) 
              sb.append("catalog is "+catalog+" Did not match "+system_.toUpperCase ()+",localhost,*LOCAL,"+getRealCatalogName());   
            assertCondition (passed, sb); 
        }
        catch (Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
getCatalog() - Should throw an exception when the connection
is closed.
**/
    public void Var002() {
        try {
            String catalog = closedConnection_.getCatalog ();
            failed ("Did not throw exception. "+catalog);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setCatalog() - This should have no effect, since we don't
support catalogs.

SQL400 - The Native JDBC driver has changed so that it will
         always return the real catalog name, even when the user
         connects with the special values of *LOCAL or localhost.
         The value will be retrieved from the database if needed.
         Note that today this is only going to work when you
         are running the tests to the local database.
**/
    public void Var003() {
        try {
            boolean passed = true; 
            sb.setLength(0);
            connection_.setCatalog ("Sears");
            String catalog = connection_.getCatalog ();

            //@F1D if (getDriver () == JDTestDriver.DRIVER_NATIVE) {
            if ((system_.equalsIgnoreCase("localhost")) ||
                (system_.equalsIgnoreCase("*LOCAL"))) {
                assertCondition(catalog.equals(getRealCatalogName()));
                return;
            }
            passed = (catalog.equals (system_.toUpperCase ()))
                || (catalog.equalsIgnoreCase("localhost"))
                || (catalog.equalsIgnoreCase("*LOCAL"))
                || (catalog.equals(getRealCatalogName())); 
            if (!passed) { 
              sb.append("catalog is "+catalog+" Did not match "+system_.toUpperCase ()+",localhost,*LOCAL,"+getRealCatalogName());      
            }
            assertCondition (passed, sb); 
        }
        catch (Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
setCatalog() - Should throw an exception when the connection
is closed.
**/
    public void Var004() {
        try {
            closedConnection_.setCatalog ("JCPenney");
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getMetaData() - Returns the meta data when the connection
is open.
**/
    public void Var005() {
        try {
            DatabaseMetaData dmd = connection_.getMetaData ();
            assertCondition (dmd != null);
        }
        catch (Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
getMetaData() - Returns the meta data when the connection
is closed.
**/
    public void Var006() {
        try {
            DatabaseMetaData dmd = closedConnection_.getMetaData ();
            assertCondition (dmd != null);
        }
        catch (Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
getTypeMap() - Throws an exception, not supported.
**/
    public void Var007() {
        if (checkJdbc20 ()) {
	    if ((getDriver () == JDTestDriver.DRIVER_NATIVE) ) {
		try {
		    java.util.Map<?,?> map = connection_.getTypeMap ();
		    System.out.println("passed "+map);
		    assertCondition(true);
		}
		catch (Exception e) {
		    failed(e, "Unexpected Exception from getTypeMap");
		}

	    } else {
		try {
		    connection_.getTypeMap ();
		    failed ("Did not throw exception for getTypeMap.");
		}
		catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
        }
    }



/**
setTypeMap() - Throws an exception, not supported.
**/
    public void Var008() {
        /* We can not compile this without JDK 1.2, and
           we are currently compiling with JDK 1.1.6.
           No big deal, since we don't support this method
           anyway.

        try {
            connection_.setTypeMap (new Hashtable ());
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        */
        notApplicable ("Type map not supported.");
    }



/**
toString() - Returns the name of the AS/400 to which
we are connected.

SQL400 - The Native JDBC driver has changed so that it will
         always return the real catalog name, even when the user
         connects with the special values of *LOCAL or localhost.
         The value will be retrieved from the database if needed.
         Note that today this is only going to work when you
         are running the tests to the local database.
**/
    public void Var009() {
        try {
            String s = connection_.toString ();

            //@F1D if (getDriver () == JDTestDriver.DRIVER_NATIVE) {
            if ((system_.equalsIgnoreCase("localhost")) ||
                (system_.equalsIgnoreCase("*LOCAL"))) {
                assertCondition(s.equals(getRealCatalogName()),"s = "+s+ "system is "+system_+" getRealCatalogName="+getRealCatalogName());
                return;
            }
            assertCondition ((s.equals (system_.toUpperCase ()))
                             || (s.equalsIgnoreCase("localhost"))
                             || (s.equalsIgnoreCase("*LOCAL"))
                             || (s.equals(getRealCatalogName())), "s = "+s);   //can have local db/catalog name diff from host name
        }
        catch (Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
toString() - Returns the name of the AS/400 when the connection
is closed.

SQL400 - The Native JDBC driver has changed so that it will
         always return the real catalog name, even when the user
         connects with the special values of *LOCAL or localhost.
         The value will be retrieved from the database if needed.
         Note that today this is only going to work when you
         are running the tests to the local database.
**/
    public void Var010() {
        try {
            String s = closedConnection_.toString ();
            //@F1D if (getDriver () == JDTestDriver.DRIVER_NATIVE) {
            if ((system_.equalsIgnoreCase("localhost")) ||
                (system_.equalsIgnoreCase("*LOCAL"))) {
                assertCondition(s.equals(getRealCatalogName()),"s = "+s+ "system is "+system_+" getRealCatalogName="+getRealCatalogName());
                return;
            }
            assertCondition ((s.equals (system_.toUpperCase ()))
                             || (s.equalsIgnoreCase("localhost"))
                             || (s.equalsIgnoreCase("*LOCAL"))
                             || (s.equals(getRealCatalogName())), "s = "+s);   // can have local db/catalog name diff from host name
        }
        catch (Exception e) {
            failed(e, "Unexpected Exception");
        }
    }

/**
getServerJobName - Returns the name of the server job
**/
    public void Var011() {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE)) {

	    try {

		//
		// Call the method using reflection
		//

		String jobName = JDReflectionUtil.callMethod_S(connection_, "getServerJobName");

		assertCondition(jobName != null && jobName.indexOf("QSQSRVR") > 0,
				"job name ("+jobName+") not correct");

	    }
	    catch (Exception e) {
		failed(e, "Unexpected Exception");
	    }
	} else {
	    notApplicable("V5R3 Native driver variation");
	}
    }

/**
getConnection -- test with an invalid subprotocol, db2j should not allow a successful connection otherwise the
                 Cloudscape driver will not work.  This accounts for the change in DB2Driver.java which now
                 uses equal instead of startsWith to check the subprotocol
**/
    public void Var012() {
	{
	    try{
		String url = "jdbc:db2j:localhost;user=BOGUS;password=XXXX" ;
		Connection tempConn = DriverManager.getConnection(url);
		failed("Did not throw expected Exception for incorrect url -- native added 3/3/2004");
		tempConn.close();
	    }
	    catch(Exception e){
	        String expectedClassName = "SQLException";
	        String expectedDetail = "No suitable driver found for jdbc:db2j:localhost;user=BOGUS;password=XXXX"; 
          assertExceptionIs(e, expectedClassName, expectedDetail);
  		      
	    }
	} 
    }


/**
getConnection -- test the query optimize goal property
**/
    // Change var013 to not be dependent on i5 system performance @H2A
    // Rather than "time" the response of the two different "query optimize goal" @H2A
    // values, verify that the values were set correctly on the i5 system. @H2A
    // The response times are subject to i5 system code changes and other  @H2A
    // jobs which could affect system response... which is outside the   @H2A
    // control and sphere of the Java Toolbox.
    // Therefore, this variation will verify that the toolbox set the  @H2A
    // correct value by retrieving the value from the server  @H2A
    // via the output of the Database Performance Monitor output.  @H2A
    public void Var013() {
      String added="-- added 01/19/2005 by native driver";
       {
        Connection connQOpt2 = null;
        Connection connOpt = null;
        Statement stmt = null ;
        Statement stmtOpt = null ;
        String sqlQuery="";

        try{
/*
          String table2[] = {
              "qsys2.syscstcol",
              "qsys2.sysindexes",
              "qsys2.syskeycst",
              "qsys2.syskeys",
              "qsys2.systabledep",
              "qsys2.systriggers"
          } ;
*/
          // Change to use pwrUID_ because QCMDEXC needs it later.
          connQOpt2    = testDriver_.getConnection (baseURL_+";query optimize goal=2", pwrSysUserID_, pwrSysEncryptedPassword_); //@H2C
          connOpt = testDriver_.getConnection (baseURL_+";query optimize goal=1", pwrSysUserID_, pwrSysEncryptedPassword_); //@H2C
          stmt = connQOpt2.createStatement();
          stmtOpt = connOpt.createStatement();

          //
          // Create new tables for this test so that the
          // results are not in the SWSC
          // Note:  The tables are in QTEMP, so each connection
          // has it's own copy
          //
          String uniqueTable="QTEMP.QOTEMP1";
          sqlQuery="CREATE TABLE "+uniqueTable+" (c1 int)";
          stmt.executeUpdate(sqlQuery);
          stmtOpt.executeUpdate(sqlQuery);

          sqlQuery="INSERT INTO "+uniqueTable+" values(1)";
          stmt.executeUpdate(sqlQuery);
          stmtOpt.executeUpdate(sqlQuery);

          String uniqueTable2="QTEMP.QOTEMP2";
          sqlQuery="CREATE TABLE "+uniqueTable2+" (c1 int)";
          stmt.executeUpdate(sqlQuery);
          stmtOpt.executeUpdate(sqlQuery);

          sqlQuery="INSERT INTO "+uniqueTable2+" values(1)";
          stmt.executeUpdate(sqlQuery);
          stmtOpt.executeUpdate(sqlQuery);
/*
          message+="allio         firstio\n";
          for (int i = 0; i < table2.length; i++) {
            //
            // Do each query in different orders to make sure
            // the ordering isn't skewing the results
            //
            sqlQuery = "Select * from "+uniqueTable+",qsys2.syscolumns, "+table2[i];

            startTime = System.currentTimeMillis();
            stmt.executeQuery(sqlQuery);
            endTime = System.currentTimeMillis();
            queryTime = endTime - startTime;
            totalQueryTime += queryTime;

            startTime = System.currentTimeMillis();
            stmtOpt.executeQuery(sqlQuery);
            endTime = System.currentTimeMillis();
            queryOptTime = endTime - startTime;
            totalQueryOptTime += queryOptTime;
            message+=queryTime+"       "+queryOptTime+"         "+uniqueTable+" "+table2[i]+"\n";


            sqlQuery = "Select * from "+uniqueTable2+",qsys2.syscolumns, "+table2[i];

            startTime = System.currentTimeMillis();
            stmtOpt.executeQuery(sqlQuery);
            endTime = System.currentTimeMillis();
            queryOptTime = endTime - startTime;
            totalQueryOptTime += queryOptTime;

            startTime = System.currentTimeMillis();
            stmt.executeQuery(sqlQuery);
            endTime = System.currentTimeMillis();
            queryTime = endTime - startTime;
            totalQueryTime += queryTime;

            message+=queryTime+"       "+queryOptTime+"         "+uniqueTable2+" "+table2[i]+"\n";
          } // End of loop
*/
          // Start of new code @H2A
          String sql;
          // We run commands via the QCMDEXC stored procedure.  That procedure
          // requires the length of the command be included with the command
          // specified in precision 15, scale 5 format.  That is,
          // "CALL QSYS.QCMDEXC('command-to-run', 000000nnnn.00000)"
          sql = "CALL QSYS.QCMDEXC('STRDBMON OUTFILE(QTEMP/DBMONOUT)    ',0000000036.00000)";
          stmt.executeUpdate(sql);
          stmtOpt.executeUpdate(sql);

          String sqlQuery1 = "Select * from "+uniqueTable+",qsys2.syscolumns";
          stmt.executeQuery(sqlQuery1);
          stmtOpt.executeQuery(sqlQuery1);

          sql = "CALL QSYS.QCMDEXC('ENDDBMON         ',0000000010.00000)";
          stmt.executeUpdate(sql);
          stmtOpt.executeUpdate(sql);

          // Following select of the DBMON output file extracts the
          // Column QVC23 is a one character field where 'A' == ALLIO and 'F' == FIRSTIO
          // Column QQRID =
          sql = "SELECT SUBSTR(QVC23,1,1) FROM QTEMP.DBMONOUT WHERE QQRID=3014";
          ResultSet rs    = stmt.executeQuery(sql);
          ResultSet rsOpt = stmtOpt.executeQuery(sql);
          rs.next();
          rsOpt.next();
          // Extract optimization level from the Database Monitor results
          String value =""+rs.getString(1);
          String valueOpt =""+rsOpt.getString(1);
          System.out.println("value   ="+value);
          System.out.println("valueOpt="+valueOpt);
          String expected = "A"; //allio
          String expectedOpt = "F"; //firstio
          String failMsg = "(goal=2) Expected '"+expected+"' and received '"+value+"'"+"\n";
          failMsg += "(goal=1) Expected '"+expectedOpt+"' and received '"+valueOpt+"'" ;

          connQOpt2.close(); 
          connOpt.close(); 
          assertCondition((value.equalsIgnoreCase(expected) &&
              valueOpt.equalsIgnoreCase(expectedOpt)), failMsg);
          // End of new code @H2A
          //message+="Totals\n allio="+totalQueryTime+"       firstio"+totalQueryOptTime+"\n";
          /* System.out.println("Totals allio="+totalQueryTime+"       firstio"+totalQueryOptTime);  */

          //assertCondition(totalQueryTime > totalQueryOptTime, "Error time for connection with *firstio is not less than *allio Messages=\n"+message);



        } catch(Exception e){
          failed(e, "unexpectedException "+added);
        }
      } 
    }



/**
isValid -- Check that the connection is valid.
**/
    public void Var014() {
       String added="-- added 06/08/2006 by native driver to test isValid";

	if (checkJdbc40()) {
          try {
	    boolean answer = JDReflectionUtil.callMethod_B(connection_, "isValid", 60);
            assertCondition(answer, "isValid returned "+answer+" sb true ");
          } catch (Exception e) {
            failed(e, "unexpected exception calling isValid "+added);
          }
	}
    }

/**
isValid -- Check that the connection is valid with a timeout of 0.
**/
    public void Var015() {
       String added="-- added 06/08/2006 by native driver to test isValid";

	if (checkJdbc40()) {
          try {
	    boolean answer = JDReflectionUtil.callMethod_B(connection_, "isValid", 0);
            assertCondition(answer, "isValid returned "+answer+" sb true ");
          } catch (Exception e) {
            failed(e, "unexpected exception calling isValid "+added);
          }
	}
    }


/**
isValid -- Verify that a negative value throws an exception.
**/
    public void Var016() {
       String added="-- added 07/14/2011 to test isValid";

	if (checkJdbc40()) {
          try {
	    boolean answer = JDReflectionUtil.callMethod_B(connection_, "isValid", -1);
            assertCondition(false, "Should have thrown exception but got "+answer+ " when calling isValid(-1)");
          } catch (Exception e) {
	      // This is the expected exception for toolbox
	      String expectedException = "Attribute value not valid";
	      boolean passed = false;
	      passed = e.toString().indexOf(expectedException) > 0;
	      if (passed) {
		  assertCondition(true);
	      } else {
		  failed(e, "unexpected exception calling isValid "+added+" Looking for "+expectedException);
	      }
          }
	}
    }



    /**
   isValid -- Verify exception is thrown Check that the connection is not valid.  I do this by
   using a separate thread to whack the connection

  On 7/14/2011 Added as toolbox test also.  The following problem was reported by a customer.

  Submitted by	Daniel Stainhauser
  Category	JTOpen
  Date composed	06/29/2011 04:17 AM
  E-mail	dstainhauser@ionesoft.ch
  Version	7.4 jdbc4
  Title	Connection.isValid interrupts main thread on invalid connections

  Message content	Hi

  There is a bug in AS400JDBCConnection.isValid(int timeout).

  When checking a connection with isValid(timeout), and this connection is invalid, or has been invalidated on the AS/400 system, and therefore throws an exception, then the timer is not stopped and the main thread gets an InterruptedException after timeout. The problem is, that at line 4538, the method returns with false, but does not stop the CommTimer. This timer waits until the timeout is over, and then interrupts otherThread (which is the main thread) at line 4505.

  Before returning from isValid, the CommTimer should be stopped, or the CommTimer should check with a boolean, if it shall interrupt otherThread.

  Thanks for correction

   **/
  public void Var017() {
    String added = "-- added 06/08/2006 by native driver to test isValid";

    if (checkJdbc40()) {

        try {
          Connection hangConnection = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);

	  //
	  // Start a thread to kill the connection
	  //
          KillThread killThread  = new KillThread(hangConnection);
          killThread.start();

	  //
	  // Wait for the thread to finish killing
	  //
          killThread.join();

          boolean answer = JDReflectionUtil.callMethod_B(hangConnection, "isValid", 3);
	  Thread.sleep(5);
	  boolean expectedAnswer = false;
	  int subDriver = testDriver_.getSubDriver();
	  if (subDriver == JDTestDriver.SUBDRIVER_JTOPENCA ||
	      subDriver == JDTestDriver.SUBDRIVER_JTOPENSF) {
	      expectedAnswer = true; 
	  }

          assertCondition(answer == expectedAnswer , "isValid returned " + answer + " sb "+expectedAnswer+" "+killThread.getStatus());

        } catch (Exception e) {
          failed(e, "unexpected exception calling isValid " + added);
        }
    }
  }


  class KillThread extends Thread {
 
      Connection c = null;
      StringBuffer statusStringBuffer = new StringBuffer(); 
      public KillThread(Connection c) {
	  this .c = c;
      }

      public String getStatus() {
	  return statusStringBuffer.toString(); 
      }

      public void run() {
        String sql = null; 
        try {
         Statement stmt = c.createStatement();
          sql = "call QSYS.QCMDEXC('"+
          "ENDJOB JOB(*) OPTION(*IMMED)                                                                '"+
          ", 0000000080.00000) ";

          // System.out.println(sql);
          // System.out.flush();
	  statusStringBuffer.append("Executing "+sql+"\n"); 
          stmt.executeQuery(sql);
	  statusStringBuffer.append("Return from "+sql+"\n"); 
        } catch (Exception e) {
	    statusStringBuffer.append("Exception from "+sql+"\n");
	    printStackTraceToStringBuffer(e, statusStringBuffer);
        }
      }


  }

  /**
  isValid -- Check that the connection is not valid after it is closed.

  **/
 public void Var018() {
   String added = "-- added 06/08/2006 by native driver to test isValid";

   if (checkJdbc40()) {

       try {
         Connection closedConnection = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);

         closedConnection.close();

         boolean answer = JDReflectionUtil.callMethod_B(closedConnection, "isValid", 2);
	 // Sleep to make sure that the main thead is not killed
	 Thread.sleep(4000);

         assertCondition(answer == false , "isValid returned " + answer + " sb false ");

       } catch (Exception e) {
         failed(e, "unexpected exception calling isValid " + added);
       }
   }
 }



/**
getConnection -- test the servermode subsystem property
**/
    public void Var019() {

      	
      String added="-- added 01/09/2008 by native driver to test servermode subsystem";
      if ((getDriver() == JDTestDriver.DRIVER_NATIVE) ) {

	  if (remoteConnection_) {
	      notApplicable("servermode substytem tests do not work for remote systems");
	      return; 
	  } 
	  //
	  // Note: In order for this to work, prestart job entries must exist for
	  // each of these subsystems.  If they do not exist, then the BCI jobs will
          // be started in the current subsystem. This testcase will create the prestart job entries,
	  // but they may not be used until the next time the subsystem is started
	  //
	  Connection conn19a = null; 
        try {
	  conn19a =  testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
	  Statement stmt = conn19a.createStatement();
	  try {
	  stmt.executeUpdate("CALL QSYS.QCMDEXC('ADDPJE SBSD(QSYS/QBATCH) PGM(QSYS/QSQSRVR) STRJOBS(*YES) INLJOBS(2) THRESHOLD(2) ADLJOBS(2) MAXUSE(200)                           ',  000000115.00000   )");
	  } catch (Exception e) {
	      String message = e.toString();
	      if (message.indexOf("not changed") > 0) {
	        // Must already exist, just ignore
	      } else {
	        e.printStackTrace();
	      }
	  }

	  try {
	  stmt.executeUpdate("CALL QSYS.QCMDEXC('ADDPJE SBSD(QSYS/QUSRWRK) PGM(QSYS/QSQSRVR) STRJOBS(*YES) INLJOBS(2) THRESHOLD(2) ADLJOBS(2) MAXUSE(200) CLS(QSYSCLS20)                        ',  000000125.00000   )");
	  } catch (Exception e) {
            String message = e.toString();
            if (message.indexOf("not changed") > 0) {
              // Must already exist, just ignore
            } else {
              e.printStackTrace();
            }
	  }
        } catch (Exception e) {
          System.out.println("ERROR setting up subsystems");
          e.printStackTrace();
        }

        Connection connQusrwrk19 = null;
        Connection connQbatch19 = null;
        Connection connQsyswrk19 = null;
        Statement stmtQusrwrk = null ;
        Statement stmtQbatch = null ;
        Statement stmtQsyswrk = null ;

        try{
          connQusrwrk19    = testDriver_.getConnection (baseURL_+";servermode subsystem=QUSRWRK", userId_, encryptedPassword_);
          connQbatch19 = testDriver_.getConnection (baseURL_+";servermode subsystem=QBATCH", userId_, encryptedPassword_);
          connQsyswrk19 = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
          stmtQusrwrk = connQusrwrk19.createStatement();
          stmtQbatch = connQbatch19.createStatement();
          stmtQsyswrk = connQsyswrk19.createStatement();

	  /*
           * TODO Determine the subsystem of the job
	   */
	  JDJSTPTestcase.assureGETSUBSYSisAvailable(connQusrwrk19);



	  ResultSet rs = stmtQusrwrk.executeQuery("SELECT "+JDJSTPTestcase.envLibrary+".GETSUBSYS() FROM SYSIBM.SYSDUMMY1");
	  rs.next();
	  String sbsQusrwrk = rs.getString(1).trim();
	  

	  ResultSet rsQbatch = stmtQbatch.executeQuery("SELECT "+JDJSTPTestcase.envLibrary+".GETSUBSYS() FROM SYSIBM.SYSDUMMY1");
	  rsQbatch.next();
	  String sbsQbatch = rsQbatch.getString(1).trim();

          ResultSet rsQsyswrk = stmtQsyswrk.executeQuery("SELECT "+JDJSTPTestcase.envLibrary+".GETSUBSYS() FROM SYSIBM.SYSDUMMY1");
          rsQsyswrk.next();
          String sbsQsyswrk = rsQsyswrk.getString(1).trim();

          connQusrwrk19.close();
          connQbatch19.close();
          connQsyswrk19.close();

          
    if (conn19a != null) conn19a.close(); 
	  assertCondition("QUSRWRK".equals(sbsQusrwrk) && "QBATCH".equals(sbsQbatch) && "QSYSWRK".equals(sbsQsyswrk),
			  "\n expected QUSRWRK got "+sbsQusrwrk+
                          "\n expected QBATCH  got "+sbsQbatch+
                          "\n expected QSYSWRK got "+sbsQsyswrk+"\n"+added);


        } catch(Exception e){
          failed(e, "unexpectedException "+added);
        }
      } else {
        notApplicable("V7R1 servermode subsystem variation "+added);
      }
    }



/**
getConnection -- test the *same servermode subsystem property
**/
    public void Var020() {
      String added="-- added 01/09/2008 by native driver to test *SAME subsystem";
      if (checkNative()) { 
	  if ((getDriver() == JDTestDriver.DRIVER_NATIVE) ) {
	      Connection conn = null;
	      Statement stmt = null ;

	      if (remoteConnection_) {
		  notApplicable("servermode substytem tests do not work for remote systems");
		  return; 
	      } 

	      try{
		  conn    = testDriver_.getConnection (baseURL_+";servermode subsystem=*SAME", userId_, encryptedPassword_);
		  stmt = conn.createStatement();

		  String subsystemName = JDJobName.getSubsystemName();

		  JDJSTPTestcase.assureGETSUBSYSisAvailable(conn);

		  ResultSet rs = stmt.executeQuery("SELECT "+JDJSTPTestcase.envLibrary+".GETSUBSYS() FROM SYSIBM.SYSDUMMY1");
		  rs.next();
		  String sbs = rs.getString(1).trim();


		  assertCondition(subsystemName.equals(sbs) ,
				  "expected "+subsystemName+" got "+sbs+ " "+added);


	      } catch(Exception e){
		  failed(e, "unexpectedException "+added);
	      }
	  } else {
	      notApplicable("V7R1 servermode subsystem variation "+added);
	  }
      }
    }



/**
getConnection -- test a bogus subsystem property
**/
    public void Var021() {
	if (checkNative()) { 
	    String added="-- added 01/09/2008 by native driver to test *SAME subsystem";
	    if ((getDriver() == JDTestDriver.DRIVER_NATIVE) ) {
		Connection conn = null;
		Statement stmt = null ;

		if (remoteConnection_) {
		    notApplicable("servermode substytem tests do not work for remote systems");
		    return; 
		} 

		try{
		    conn    = testDriver_.getConnection (baseURL_+";servermode subsystem=BOGUSBOGUS", userId_, encryptedPassword_);
		    stmt = conn.createStatement();

		    String subsystemName = JDJobName.getSubsystemName();

		    JDJSTPTestcase.assureGETSUBSYSisAvailable(conn);

		    ResultSet rs = stmt.executeQuery("SELECT "+JDJSTPTestcase.envLibrary+".GETSUBSYS() FROM SYSIBM.SYSDUMMY1");
		    rs.next();
		    String sbs = rs.getString(1).trim();


		    assertCondition(subsystemName.equals(sbs) ,
				    "expected "+subsystemName+" got "+sbs+ " "+added);


		} catch(Exception e){
		    failed(e, "unexpectedException "+added);
		}
	    } else {
		notApplicable("V7R1 servermode subsystem variation "+added);
	    }
	}
    }



    /*
     * setup the libraries used by the system naming tests
     */

    public static void setupSystemNamingLibraries(Connection connection, String newBaseCollection ) throws Exception {
	if (!systemNamingLibrariesCreated) {
	  systemNamingLibraries = new Vector<String>();
	    String sql = "";
	    try {
		baseCollection = newBaseCollection;
		if (baseCollection.length() > 8) {
		    baseCollection=baseCollection.substring(0,8);
		}
		Statement s = connection.createStatement();

		for (int i = 0; i < 10; i++) {
		    String collection = baseCollection+i;
		    systemNamingLibraries.addElement(collection);
		
		    JDSetupCollection.create(connection,collection,false);

		    try {
			sql = "DROP TABLE "+collection+".VERSION";
			s.executeUpdate(sql);
		    } catch (Exception e) {
		    }
		    sql = "CREATE TABLE "+collection+".VERSION(VERSION_NUMBER INT)";
		    s.executeUpdate(sql);
		    sql = "INSERT INTO "+collection+".VERSION VALUES("+i+")";
		    s.executeUpdate(sql);

		    try {
			sql = "DROP PROCEDURE "+collection+".VERSIONPROC()";
			s.executeUpdate(sql);
		    } catch (Exception e) {
		    }
		    /* NOTE:  Do not create the procedures as RETURN TO CLIENT */
		    /*        That will cause UDFs to fail                     */

		    sql = "CREATE PROCEDURE "+collection+".VERSIONPROC() dynamic result set 1 LANGUAGE SQL BEGIN DECLARE C1 CURSOR WITH RETURN FOR SELECT "+i+" FROM SYSIBM.SYSDUMMY1; open c1; set result sets cursor c1; end";
		    s.executeUpdate(sql);

		    try {
			sql = "DROP PROCEDURE "+collection+".VERSIONPROCRC()";
			s.executeUpdate(sql);
		    } catch (Exception e) {
		    }

		    sql = "CREATE PROCEDURE "+collection+".VERSIONPROCRC() dynamic result set 1 LANGUAGE SQL BEGIN DECLARE C1 CURSOR WITH RETURN TO CLIENT FOR SELECT "+i+" FROM SYSIBM.SYSDUMMY1; open c1; set result sets WITH RETURN TO CLIENT cursor c1; end";
		    s.executeUpdate(sql);

		    s.close();
		    JDReflectionUtil.callMethod_V(connection, "setUseSystemNaming", true);
		    s = connection.createStatement();
		    try {
			sql = "DROP PROCEDURE "+collection+"/VERSIONQUERYPROC()";
			s.executeUpdate(sql);
		    } catch (Exception e) {
		    }
		    sql = "CREATE PROCEDURE "+collection+"/VERSIONQUERYPROC() dynamic result set 1 LANGUAGE SQL BEGIN DECLARE C1 CURSOR WITH RETURN FOR SELECT * FROM VERSION; open c1; set result sets cursor c1; end";
		    s.executeUpdate(sql);


		    try {
			sql = "DROP PROCEDURE "+collection+"/VERSIONQUERYPROCRC()";
			s.executeUpdate(sql);
		    } catch (Exception e) {
		    }


		    sql = "CREATE PROCEDURE "+collection+"/VERSIONQUERYPROCRC() dynamic result set 1 LANGUAGE SQL BEGIN DECLARE C1 CURSOR WITH RETURN TO CLIENT FOR SELECT * FROM VERSION; open c1; set result sets WITH RETURN TO CLIENT cursor c1; end";
		    s.executeUpdate(sql);


		    JDReflectionUtil.callMethod_V(connection, "setUseSystemNaming", false);

		}

		systemNamingLibrariesCreated = true;
		s.close();
	    } catch (Exception sqlex) {
		System.out.println("setupSystemNamingLibraries: Exception caught sql="+sql);
		throw sqlex;
	    }
	}
    }

    /**
     * get the list of libraries created for the system naming test
     * @return
     */
    public static Vector<String> getSystemNamingLibraries() {
      return systemNamingLibraries;
    }

  /*
   * cleanup the libaries used by the system naming tests
   */
  public static void cleanupSystemNamingLibraries(Connection connection) throws SQLException {
      if (cleanupLibraries ) {
	  if (systemNamingLibrariesCreated) {
	      Statement s = connection.createStatement();

	      for (int i = 0; i < 10; i++) {
		  String collection = baseCollection + i;
		  JDSetupCollection.dropCollection(s, collection);
	      }
	      s.close();
	      systemNamingLibrariesCreated = false;
	  }
      }
  }



  public void runNamingTest(String testQuery) {
      String added = "-- added 07/13/2012 by native driver to test setUseSystemNaming";
      if ((getDriver() == JDTestDriver.DRIVER_NATIVE)) {
	  String sql = "";
	  StringBuffer sb = new StringBuffer();

	  try {
	      boolean passed = true;
	      setupSystemNamingLibraries(connection_, JDConnectionTest.COLLECTION);
        connection_.commit();
        
	      Connection connNamingTest = testDriver_.getConnection(baseURL_, userId_,
							  encryptedPassword_);
	      Statement stmt = connNamingTest.createStatement();

	      for (int i = 0; i < 10; i++) {
		  String collection = baseCollection + i;

	  // Change the library list
		  sql = "call QSYS.QCMDEXC('ADDLIBLE LIB("
		    + collection
		    + ") POSITION(*FIRST)                       ' , 0000000050.00000)";
		  sb.append("Executing " + sql + "\n");
		  stmt.executeUpdate(sql);

	  // Make sure the file cannot be found
		  sql = testQuery;
		  try {
		      sb.append("Before setting systemNaming:  Executing "+sql+"\n");

		      stmt.executeQuery(sql);
		      passed = false;
		      sb.append("VERSIONPROC was found when it should not have been");
		  } catch (Exception e) {
	    // OK here.
		  }

	  // Set system naming
		  JDReflectionUtil.callMethod_V(connNamingTest, "setUseSystemNaming", true);
		  sb.append("Setting path using SET PATH *LIBL\n");
		  stmt.executeUpdate("SET PATH *LIBL");

	  // Verify that the file can be found.

		  try {
		      sb.append("After setting systemNaming:  Executing "+sql+"\n");
		      ResultSet rs = stmt.executeQuery(sql);
		      rs.next();
		      int value = rs.getInt(1);
		      if (value != i) {
			  passed=false;
			  sb.append("Error got "+value+" expected "+i+" from query:"+sql+"\n");
		      }
		  } catch (Exception e) {
		      passed = false;
		      sb.append("Exception.. execute SQL file in " + collection + "\n");
		      printStackTraceToStringBuffer(e, sb);

		  }

	  // Unset system naming
		  JDReflectionUtil.callMethod_V(connNamingTest, "setUseSystemNaming", false);

		  sb.append("Setting path using SET PATH USER,SYSTEMPATH\n");
		  stmt.executeUpdate("SET PATH USER,SYSTEMPATH");

	  // Verify that the file cannot be found
		  try {
		      sb.append("After setting sqlNaming:  Executing "+sql+"\n");
		      stmt.executeQuery(sql);
		      passed = false;
		      sb.append("Look 2 VERSION file was found unexpectedly");
		  } catch (Exception e) {
	    // OK here.
		  }

	      }
	      connNamingTest.close(); 
	      assertCondition(passed, sb.toString() + added);
	  } catch (Exception e) {
	      failed(e, "sb=" + sb.toString() + " Unexpected exception " + added);
	  }

      } else {
	  notApplicable("Native System naming test");
      }

  }


    /* setUseSystemNaming -- test system naming setting */

  public void Var022() {
      runNamingTest("select * from VERSION");
  }




    /* setUseSystemNaming -- test system naming setting using procedures */

  public void Var023() {
      runNamingTest("CALL VERSIONPROC()");
  }


  public void Var024() {
      runNamingTest("CALL VERSIONQUERYPROC()");
  }


  class SignonThread extends Thread {
  
    Connection c = null;
    boolean keepRunning = true;
    boolean checkUser = false;
    int id; 
    String profile;
    String password;
    StringBuffer statusStringBuffer = new StringBuffer();

    public SignonThread(int id, String profile, boolean checkUser) {
      this.id = id; 
      this.profile = profile;
      this.checkUser = checkUser; 
      password = getPassword(profile);
    }

    public String getStatus() {
      return statusStringBuffer.toString();
    }

    public void run() {
	Thread.currentThread().setName("SOT-"+id+"-"+profile); 
      int connectionCount = 0;
      while (keepRunning) {
        try {
          c = DriverManager.getConnection(baseURL_, profile, password);
          connectionCount++;
          if (checkUser) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt
                .executeQuery("SELECT CURRENT USER FROM SYSIBM.SYSDUMMY1");
            rs.next();
            String currentUser = rs.getString(1);  
            if (!currentUser.equals(profile)) {
              statusStringBuffer.append("ERROR:  For profile " + profile
                  + " signed on as " + currentUser + "\n");
              keepRunning = false;
            }
          }
          c.close();
          c = null; 
        } catch (Exception e) {
          statusStringBuffer.append("ERROR:  Exception for profile " + profile
              + "\n");
          printStackTraceToStringBuffer(e, statusStringBuffer);
          keepRunning = false;
        }
      }
      statusStringBuffer.append("Thread for " + profile + " completed for "
          + connectionCount + "connections.\n");
      try { 
        if (c != null) { 
          c.close(); 
          c = null; 
        }
      } catch (Exception e) { 
      
      }
    }

    public void markStop() {
      keepRunning = false;
    }

  }

  /*
   * test the connecting of several threads at a time, they they should not
   * error and
   */
  /*
   * not connect as a user. This test is to expose potential threading problems
   * * /* in jt400native.jar
   */

  public void Var025() {

    String added = " -- Testcase added 1/24/2017 to test jt400native.jar connections ";

   
    
    StringBuffer sb = new StringBuffer();
    try {
      boolean passed = true;
      int profileCount = 20;

      String[] profiles = new String[profileCount];

      createProfiles(profiles);

      SignonThread[] threads = createThreads(profiles);

      startThreads(threads);

      // Wait for test to complete
      long endTime = System.currentTimeMillis() + 60000;
      while (System.currentTimeMillis() < endTime) {
        Thread.sleep(250);
      }
      stopThreads(threads);
      passed = checkThreads(threads, sb);

      removeProfiles(profiles);

      assertCondition(passed, sb.toString() + added);
    } catch (Exception e) {
      failed(e, "sb=" + sb.toString() + " Unexpected exception " + added);
    }

  }

  boolean checkThreads(SignonThread[] threads, StringBuffer sb) {
    boolean passed = true;
    for (int i = 0; i < threads.length; i++) {
      String status = threads[i].getStatus();
      sb.append("-----------------------\n");
      sb.append("For thread " + i + "\n");
      sb.append(status);
      if (status.indexOf("ERROR") >= 0) {
        passed = false;
      }
    }
    return passed;
  }

  void startThreads(SignonThread[] threads) {
    for (int i = 0; i < threads.length; i++) {
      System.out.println("Starting thread " + i);
      threads[i].start();
    }
  }

  void stopThreads(SignonThread[] threads) throws InterruptedException {
    for (int i = 0; i < threads.length; i++) {
      System.out.println("Stopping thread " + i);
      threads[i].markStop();
    }
    for (int i = 0; i < threads.length; i++) {
      System.out.println("joining " + i);
      threads[i].join();
    }



  }

  SignonThread[] createThreads(String[] profiles) {
    SignonThread[] threads = new SignonThread[profiles.length];

    for (int i = 0; i < profiles.length; i++) {
      boolean checkUser = false; ;
      if (i%3==0) checkUser = true;
      threads[i] = new SignonThread(i, profiles[i], checkUser);
    }
    return threads;
  }

  String getPassword(String profile) {
    return profile + "X2";
  }

  void createProfiles(String[] profiles) throws Exception {
    for (int i = 0; i < profiles.length; i++) {
      profiles[i] = "JDCM" + i;
      try {
        String command = "QSYS/CRTUSRPRF USRPRF(" + profiles[i] + ") PASSWORD(DUMMY)"; 
        pwrStatement_.executeUpdate("CALL QSYS2.QCMDEXC('"
            + command+ "')");
      } catch (Exception e) {

      }
      String command = "QSYS/CHGUSRPRF USRPRF("+ profiles[i] + ") PASSWORD(" + getPassword(profiles[i])+")"; 
      pwrStatement_.executeUpdate("CALL QSYS2.QCMDEXC('"+command  + "') ");

    }
  }

  void removeProfiles(String[] profiles) throws Exception {
    for (int i = 0; i < profiles.length; i++) {
      profiles[i] = "JDCM" + i;
      try {
        pwrStatement_.executeUpdate("CALL QSYS2.QCMDEXC('"
            + "QSYS/DLTUSRPRF USRPRF(" + profiles[i] + ") ')");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }


/**
getConnection -- test debug connection properties
**/
    public void Var026() {
      String added="-- added 7/20/2018 to test toolbox connection properties"; 
      if ( (getDriver() == JDTestDriver.DRIVER_TOOLBOX) ) {
        Connection conn = null;
        
	boolean passed = true;

        try{
	    String[] properties = {
		"garbage",
		"none", 
		"datastream",
		"diagnostic",
		"error",
		"information",
		"warning",
		"conversion",
		"proxy",
		"pcml",
		"jdbc",
		"all",
		"thread",
		"none", 
	    };

	    for (int i = 0; i < properties.length; i++) { 
		conn    = testDriver_.getConnection (baseURL_+";toolbox trace="+properties[i], userId_, encryptedPassword_);
		conn.close();
	    }

	  assertCondition(passed,  " "+added);


        } catch(Exception e){
          failed(e, "unexpectedException "+added);
        }
      } else {
        notApplicable("toolbox trace variation "+added);
      }
    }


    
    /**
    getReconnectURLs -- 
    **/
        public void Var027() {
          sb.setLength(0); 
          sb.append("-- added 8/08/2018 to test getReconnectUrls"); 
         if (checkToolbox())  {
           try { 
             boolean passed = true; 
             String url = baseURL_; 
             int propertiesIndex = url.indexOf(';'); 
             if (propertiesIndex > 0) { 
               url = url.substring(0,propertiesIndex); 
             }
             url = url+";enableClientAffinitiesList=0";
             
             AS400JDBCConnection testConnection = (AS400JDBCConnection) 
                 testDriver_.getConnection (url, userId_, encryptedPassword_);

             String[] reconnectUrls = (String[]) JDReflectionUtil.callMethod_O(testConnection, "getReconnectURLs"); 
             if (reconnectUrls == null) {
               passed = false; 
               sb.append("Got null, should be empty array"); 
             } else if (reconnectUrls.length > 0) {
               passed = false; 
               sb.append("Got non empty array, should be empty array\n");
               for (int i = 0; i < reconnectUrls.length; i++) { 
                 sb.append(" reconnectUrls["+i+"] = "+reconnectUrls[i]+"\n"); 
               }
             }
             assertCondition(passed, sb); 
           } catch ( Exception e) { 
             failed(e, "unexpected exception "+sb.toString()); 
           }
         }
    
        }

        
  /**
   * getReconnectURLs --
   **/
  public void Var028() {
    sb.setLength(0);
    sb.append("-- added 8/08/2018 to test getReconnectUrls\n");
    if (checkToolbox()) {
      try {
        boolean passed = true;
        String url = baseURL_;
        int propertiesIndex = url.indexOf(';');
        if (propertiesIndex > 0) {
          url = url.substring(0, propertiesIndex);
        }
        url = url + ";enableClientAffinitiesList=1";
        sb.append("Connecting using URL "+url+"\n"); 
        AS400JDBCConnection testConnection = (AS400JDBCConnection) testDriver_
            .getConnection(url, userId_, encryptedPassword_);

        String[] reconnectUrls = (String[]) JDReflectionUtil.callMethod_O(testConnection, "getReconnectURLs"); 
        if (reconnectUrls == null) {
          passed = false;
          sb.append("Got null, should be array with 1 element ");
        } else if (reconnectUrls.length != 1) {
          passed = false;
          sb.append("Got array.length != 1, should be array of length 1 \n");
          for (int i = 0; i < reconnectUrls.length; i++) {
            sb.append(" reconnectUrls[" + i + "] = " + reconnectUrls[i] + "\n");
          }
	  sb.append(" URL was "+url+"\n");
	  sb.append("If not a mirror system, need to disable mirror using SQL: call terminate_mirror('DESTROY')\n"); 
        } else {
          if (!url.equals(reconnectUrls[0])) {
            passed = false; 
            sb.append("Got reconnectUrls[0] = "+reconnectUrls[0]+"\n"); 
            sb.append("Should be            = "+url+"\n"); 
          }
        }
        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "unexpected exception " + sb.toString());
      }
    }

  }

  
  /**
   * getReconnectURLs --
   **/
  public void Var029() {
    sb.setLength(0);
    sb.append("-- added 8/08/2018 to test getReconnectUrls");
    if (checkToolbox()) {
      try {
        boolean passed = true;
        String url = baseURL_;
        int propertiesIndex = url.indexOf(';');
        if (propertiesIndex > 0) {
          url = url.substring(0, propertiesIndex);
        }
        url = url + ";enableClientAffinitiesList=1;clientRerouteAlternateServerName=one,two,three";

        AS400JDBCConnection testConnection = (AS400JDBCConnection) testDriver_
            .getConnection(url, userId_, encryptedPassword_);

        String[] reconnectUrls = (String[]) JDReflectionUtil.callMethod_O(testConnection, "getReconnectURLs"); 
        if (reconnectUrls == null) {
          passed = false;
          sb.append("Got null, should be array with 1 element ");
        } else if (reconnectUrls.length != 4) {
          passed = false;
          sb.append("Got array.length != 4, should be array of length 4 \n");
          for (int i = 0; i < reconnectUrls.length; i++) {
            sb.append(" reconnectUrls[" + i + "] = " + reconnectUrls[i] + "\n");
          }
        } else {
          if (!url.equals(reconnectUrls[0])) {
            passed = false; 
            sb.append("Got reconnectUrls[0] = "+reconnectUrls[0]+"\n"); 
            sb.append("Should be            = "+url+"\n"); 
          }
          if (reconnectUrls[1].indexOf("one") < 0) {
            sb.append("Got reconnectUrls[1] = "+reconnectUrls[1]+"\n"); 
            sb.append("Should contain one \n"); 
          }
          if (reconnectUrls[2].indexOf("two") < 0) {
            sb.append("Got reconnectUrls[2] = "+reconnectUrls[2]+"\n"); 
            sb.append("Should contain two \n"); 
          }
          if (reconnectUrls[3].indexOf("three") < 0) {
            sb.append("Got reconnectUrls[3] = "+reconnectUrls[3]+"\n"); 
            sb.append("Should contain three \n"); 
          }
        }
        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "unexpected exception " + sb.toString());
      }
    }

  }


  public void Var030() {
    sb.setLength(0);
    sb.append("-- added 8/08/2018 to test getReconnectUrls");
    if (checkToolbox()) {
      try {
        boolean passed = true;
        String url = baseURL_;
        int propertiesIndex = url.indexOf(';');
        if (propertiesIndex > 0) {
          url = url.substring(0, propertiesIndex);
        }
        url = url + ";enableClientAffinitiesList=1;clientRerouteAlternateServerName=one,two,three;clientRerouteAlternatePortNumber=1,2,3";

        AS400JDBCConnection testConnection = (AS400JDBCConnection) testDriver_
            .getConnection(url, userId_, encryptedPassword_);

        String[] reconnectUrls = (String[]) JDReflectionUtil.callMethod_O(testConnection, "getReconnectURLs"); 
        if (reconnectUrls == null) {
          passed = false;
          sb.append("Got null, should be array with 1 element ");
        } else if (reconnectUrls.length != 4) {
          passed = false;
          sb.append("Got array.length != 4, should be array of length 4 \n");
          for (int i = 0; i < reconnectUrls.length; i++) {
            sb.append(" reconnectUrls[" + i + "] = " + reconnectUrls[i] + "\n");
          }
        } else {
          if (!url.equals(reconnectUrls[0])) {
            passed = false; 
            sb.append("Got reconnectUrls[0] = "+reconnectUrls[0]+"\n"); 
            sb.append("Should be            = "+url+"\n"); 
          }
          if (reconnectUrls[1].indexOf("one") < 0) {
            sb.append("Got reconnectUrls[1] = "+reconnectUrls[1]+"\n"); 
            sb.append("Should contain one \n"); 
          }
          if (reconnectUrls[2].indexOf("two") < 0) {
            sb.append("Got reconnectUrls[2] = "+reconnectUrls[2]+"\n"); 
            sb.append("Should contain two \n"); 
          }
          if (reconnectUrls[3].indexOf("three") < 0) {
            sb.append("Got reconnectUrls[3] = "+reconnectUrls[3]+"\n"); 
            sb.append("Should contain three \n"); 
          }
          if (reconnectUrls[1].indexOf("1") < 0) {
            sb.append("Got reconnectUrls[1] = "+reconnectUrls[1]+"\n"); 
            sb.append("Should contain 1 \n"); 
          }
          if (reconnectUrls[2].indexOf("2") < 0) {
            sb.append("Got reconnectUrls[2] = "+reconnectUrls[2]+"\n"); 
            sb.append("Should contain 2 \n"); 
          }
          if (reconnectUrls[3].indexOf("3") < 0) {
            sb.append("Got reconnectUrls[3] = "+reconnectUrls[3]+"\n"); 
            sb.append("Should contain 3 \n"); 
          }
        }
        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "unexpected exception " + sb.toString());
      }
    }

  }

  
  public void Var031() {
    sb.setLength(0);
    sb.append(
        "-- added 6/29/2022 to test socket timeout and leaking socket connections \n");
    if (checkToolbox()) {
      try {
        boolean passed = true;
        String url = baseURL_;
        int propertiesIndex = url.indexOf(';');
        if (propertiesIndex > 0) {
          url = url.substring(0, propertiesIndex);
        }
        url = url + ";thread used=false;socket timeout=5000";
        sb.append("Connecting with URL="+url+"\n"); 
        for (int i = 0; i < 10; i++) {

          AS400JDBCConnection testConnection = (AS400JDBCConnection) testDriver_
              .getConnection(url, userId_, encryptedPassword_);
          Statement stmt = testConnection.createStatement();
          String sql = "select TCP_STATE,CONNECTION_TYPE,REMOTE_ADDRESS,REMOTE_PORT,REMOTE_PORT_NAME,LOCAL_ADDRESS,LOCAL_PORT,LOCAL_PORT_NAME,PROTOCOL,IDLE_TIME from QSYS2.NETSTAT_INFO where REMOTE_ADDRESS=SYSIBM.CLIENT_IPADDR and LOCAL_NAME='as-database' ORDER BY TCP_STATE";
          sb.append("Running : " + sql+"\n");
          ResultSet rs = stmt.executeQuery(sql);
          int badCount = 0;
          while (rs.next()) {
            sb.append("Got: " + rs.getString(1) + "," + rs.getString(2) + ","
                + rs.getString(3) + "," + rs.getString(4) + ","
                + rs.getString(5) + "," + rs.getString(6) + ","
                + rs.getString(7) + "," + rs.getString(8) + ","
                + rs.getString(9) + "," + rs.getString(10) + "\n");
            if (!"ESTABLISHED".equals(rs.getString(1))) {
              if (!"TIME-WAIT".equals(rs.getString(1))) {
               badCount++;
              } 
            }
          }
          if (badCount > 2) {
            sb.append(
                "FAILED:  More than two connections is not ESTABLISHED or TIME-WAIT \n");
            passed = false;
          }

          try {
            sql = "CALL QSYS2.QCMDEXC('DLYJOB 6')";
            stmt.executeUpdate(sql);
            sb.append("FAILED: No error from :" + sql + "\n");
            passed = false;
          } catch (SQLException e) {
            String expected = "Read timed out";
            StringBuffer sbError = new  StringBuffer(); 
            printStackTraceToStringBuffer(e,sbError);
            String message = sbError.toString();
            if (message.indexOf(expected) < 0) {
              sb.append(
                  "FAILED: Expected " + expected + " Found: " + message + "\n");
              passed = false;
            }
          }

          testConnection.close();

        }

        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "unexpected exception " + sb.toString());
      }
    }

  }  

  
}

