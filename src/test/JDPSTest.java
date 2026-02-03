///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDPSTest.java
//
// Classes:      JDPSTest
//
////////////////////////////////////////////////////////////////////////

package test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.Job;

import test.JD.JDSerializeFile;
import test.JD.JDSetupCollection;
import test.JD.PS.JDPSBatch;
import test.JD.PS.JDPSBatchCompress;
import test.JD.PS.JDPSBatchCompressPerformance;
import test.JD.PS.JDPSBatchNative;
import test.JD.PS.JDPSBatchStress;
import test.JD.PS.JDPSBatchTruncation;
import test.JD.PS.JDPSClearParameters;
import test.JD.PS.JDPSClose;
import test.JD.PS.JDPSDataTruncation;
import test.JD.PS.JDPSExecute;
import test.JD.PS.JDPSMisc;
import test.JD.PS.JDPSResults;
import test.JD.PS.JDPSSetArray;
import test.JD.PS.JDPSSetAsciiStream;
import test.JD.PS.JDPSSetBigDecimal;
import test.JD.PS.JDPSSetBinaryStream;
import test.JD.PS.JDPSSetBinaryStream40;
import test.JD.PS.JDPSSetBlob;
import test.JD.PS.JDPSSetBlob40;
import test.JD.PS.JDPSSetBoolean;
import test.JD.PS.JDPSSetByte;
import test.JD.PS.JDPSSetBytes;
import test.JD.PS.JDPSSetCharacterStream;
import test.JD.PS.JDPSSetClob;
import test.JD.PS.JDPSSetClob40;
import test.JD.PS.JDPSSetDB2Default;
import test.JD.PS.JDPSSetDB2Unassigned;
import test.JD.PS.JDPSSetDBDefault;
import test.JD.PS.JDPSSetDBUnassigned;
import test.JD.PS.JDPSSetDate;
import test.JD.PS.JDPSSetDouble;
import test.JD.PS.JDPSSetFloat;
import test.JD.PS.JDPSSetInt;
import test.JD.PS.JDPSSetLong;
import test.JD.PS.JDPSSetNCharacterStream;
import test.JD.PS.JDPSSetNClob;
import test.JD.PS.JDPSSetNString;
import test.JD.PS.JDPSSetNull;
import test.JD.PS.JDPSSetObject2;
import test.JD.PS.JDPSSetObject3;
import test.JD.PS.JDPSSetObject3SQLType;
import test.JD.PS.JDPSSetObject4;
import test.JD.PS.JDPSSetObject4SQLType;
import test.JD.PS.JDPSSetRef;
import test.JD.PS.JDPSSetRowId;
import test.JD.PS.JDPSSetSQLXML;
import test.JD.PS.JDPSSetShort;
import test.JD.PS.JDPSSetString;
import test.JD.PS.JDPSSetTime;
import test.JD.PS.JDPSSetTimestamp;
import test.JD.PS.JDPSSetUnicodeStream;
import test.JD.PS.JDRowSetPSTestcase;



/**
 * Test driver for the JDBC PreparedStatement class.
 */
public class JDPSTest extends JDTestDriver {

  /**
   * 
   */
 
  // Constants.
  public static String COLLECTION = "JDTESTPS";

  private static String PSTEST_SET = COLLECTION + ".PSTEST_SET";
  private static String PSTEST_SETDFP16 = COLLECTION + ".PSTSDFP16";
  private static String PSTEST_SETDFP34 = COLLECTION + ".PSTSDFP34";
  private static String PSTEST_SETXML = COLLECTION + ".PSTSXML";
  private static String PSTEST_SETXML13488 = COLLECTION + ".PSTSX13488";
  private static String PSTEST_SETXML1200 = COLLECTION + ".PSTSX1200";
  private static String PSTEST_SETXML37 = COLLECTION + ".PSTSX37";
  private static String PSTEST_SETXML937 = COLLECTION + ".PSTSX937";
  private static String PSTEST_SETXML290 = COLLECTION + ".PSTSXML290";
  public static String PSTEST_SETDB2DEF = COLLECTION + ".PS_SETDB2";
  public static String PSTEST_CONCUR_BASE = "PS_CONCUR"; 
  public static String PSTEST_CONCUR = COLLECTION + "."+PSTEST_CONCUR_BASE;
  

  
  public static final int SET = 0;
  public static final int SETDFP16 = 1;
  public static final int SETDFP34 = 2;
  public static final int SETXML = 3;
  public static final int SETXML13488 = 4;
  public static final int SETXML1200 = 5;
  public static final int SETXML37 = 6;
  public static final int SETXML937 = 7;
  public static final int SETXML290 = 8;
  public static final int SETDB2DEF = 9;
  public static final int CONCUR = 10;
  
  
  // Private data.
  private Connection connection_;

  private Statement statement_;

  private int extraConnections = 1;

  private int extraConnectionsDefault = 1;

  private int handleStressReportInterval = 1;

  private Connection[] handleConnections;

  private Vector<PreparedStatement> handles = null;

  private Timestamp startTimestamp; 



/**
 * Run the test as an application. This should be called from the test driver's
 * main().
 * 
 * @param args
 *          The command line arguments.
 * 
 * @exception Exception
 *              If an exception occurs.
 */
   public static void main (String args[])
   throws Exception
   {
      runApplication (new JDPSTest (args));
   }



  /**
   * Constructs an object for applets.
   * 
   * @exception Exception
   *              If an exception occurs.
   */
  public JDPSTest() throws Exception {
    super();
  }

  /**
   * Constructs an object for testing applications.
   * 
   * @param args
   *          The command line arguments.
   * 
   * @exception Exception
   *              If an exception occurs.
   */
  public JDPSTest(String[] args) throws Exception {
    super(args);
  }



/**
Performs setup needed before running testcases.

@exception Exception If an exception occurs.
**/
   public void setup() throws Exception {
       boolean is400 = true; 
    super.setup(); // @D1A

    connection_ = getConnection(getBaseURL(), systemObject_.getUserId(),
        encryptedPassword_);

    String handleStress = System.getProperty("handleStress");
    if (handleStress != null) {

      String handleStressConnectionString = System
          .getProperty("handleStressConnections");
      if (handleStressConnectionString != null) {
        extraConnections = Integer.parseInt(handleStressConnectionString);
        if (extraConnections == 0)
          extraConnections = extraConnectionsDefault;
      }
      String cursorName = null;
      String sql = null;
      try {
        int handleCount = Integer.parseInt(handleStress);
        if (handleCount == 0) {
          handleCount = 100000;
        }

        // 
        // Build up a set of prepared statements to reach the
        // CLI handle limit.
        // 
        handleConnections = new Connection[extraConnections];
        handleConnections[0] = connection_;
        for (int i = 1; i < extraConnections; i++) {
          handleConnections[i] = getConnection(getBaseURL(), systemObject_
              .getUserId(), encryptedPassword_);
        }
        handles = new Vector<PreparedStatement>();

        int currentHandle = 0;
        long nextPrint = 0;
        int count = 0;
        int lastHandle = 0;
        while (currentHandle < handleCount) {

          PreparedStatement pstmt;

          if ((count % 500) == 0) {
            sql = "Select * from qsys2.qsqptabl";
            pstmt = handleConnections[count % extraConnections]
                .prepareStatement(sql);
            handles.addElement(pstmt);
            ResultSet rs = pstmt.executeQuery();

            cursorName = rs.getCursorName();
            rs.close();
          } else {
            sql = "Create table t" + count + " ( c1 int)";
            pstmt = handleConnections[count % extraConnections]
                .prepareStatement(sql);

            handles.addElement(pstmt);

          }
          // ((DB2Statement) pstmt).getStatementHandle();
          currentHandle = JDReflectionUtil.callMethod_I(pstmt, "getStatementHandle"); 
        		  
          long currentTime = System.currentTimeMillis();
          if (currentTime > nextPrint) {

            System.out
                .println("Current handle = "
                    + currentHandle
                    + " count="
                    + count
                    + " handles/sec="
                    + ((float) (currentHandle - lastHandle) / (float) handleStressReportInterval));
            nextPrint = currentTime + (handleStressReportInterval * 1000);
            lastHandle = currentHandle;
          }

          count++;
        }

      } catch (Exception e) {
        System.out
            .println("Warning:  problems with handleStress setup last cursor name "
                + cursorName + " sql = " + sql);
        e.printStackTrace();
      }

    }

    if (testLib_ != null) { // @E2A
      COLLECTION = testLib_;
      PSTEST_SET = COLLECTION + ".PSTEST_SET";
      PSTEST_SETDFP16 = COLLECTION + ".PSTSDFP16";
      PSTEST_SETDFP34 = COLLECTION + ".PSTSDFP34";
      PSTEST_SETXML   = COLLECTION + ".PSTSXML";

      PSTEST_SETXML13488 = COLLECTION + ".PSTSX13488";
      PSTEST_SETXML1200 = COLLECTION + ".PSTSX1200";
      PSTEST_SETXML37 = COLLECTION + ".PSTSX37";
      PSTEST_SETXML937 = COLLECTION + ".PSTSX937";
      PSTEST_SETXML290 = COLLECTION + ".PSTSXML290";
      PSTEST_SETDB2DEF = COLLECTION + ".PS_SETDB2";
      PSTEST_CONCUR  =   COLLECTION + ".PS_CONCUR"; 

    }
    JDSetupCollection.create(systemObject_,  connection_, COLLECTION, out_);
    

    statement_ = connection_.createStatement();

    //
    // Do not drop tables. 
    // dropTable(statement_, PSTEST_SET);
    // dropTable(statement_, PSTEST_SETDB2DEF);
    // dropDistinctType(statement_,  COLLECTION + ".PS_AGE");



      // If on LUW, must also have comparision

     
      // Determine if LUW 
      try {
	  ResultSet rs = statement_.executeQuery("select * from qsys2.qsqptabl");
	  rs.close(); 
	  is400 =true; 
      } catch (Exception e) {
	  is400 =false; 
      } 

      /* Make sure PSTEST_CONCUR is handled transactionally */ 
      boolean autocommit = connection_.getAutoCommit(); 
      if (autocommit) {
        connection_.setAutoCommit(false);
      }
      /* Wait for a long time if there is much contention */ 
      statement_.executeUpdate("CALL QSYS2.QCMDEXC('CHGJOB DFTWAIT(3600)')"); 
      
      String sql = "SELECT * FROM QSYS2.SYSTABLES where TABLE_NAME='"+PSTEST_CONCUR_BASE+"' and TABLE_SCHEMA='"+COLLECTION+"' " ;
      ResultSet rs = statement_.executeQuery(sql); 
      if (!rs.next()) { 
        rs.close(); 
        
        sql =  "CREATE OR REPLACE TABLE "+PSTEST_CONCUR+" (JOBNAME VARCHAR(80), STARTTIME TIMESTAMP) ON REPLACE PRESERVE ROWS"; 
        System.out.println("Running "+sql); 
        statement_.executeUpdate(sql); 
        connection_.commit(); 
      } else {
        rs.close(); 
        sql = "VALUES JOB_NAME"; 
        rs = statement_.executeQuery(sql); 
        rs.next(); 
        System.out.println("job name is "+rs.getString(1)); 
        rs.close(); 
      }
      startTimestamp = new Timestamp(System.currentTimeMillis()); 
      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "+PSTEST_CONCUR+" VALUES(JOB_NAME,?)");
      ps.setTimestamp(1,startTimestamp); 
      ps.executeUpdate(); 
      ps.close(); 
      connection_.commit(); 
      if (autocommit) {
        connection_.setAutoCommit(true);
      }
      statement_.executeUpdate("CALL QSYS2.QCMDEXC('CHGJOB DFTWAIT(30)')"); 

      // Added With comparisons 12/28/2006 so would work on LUW also
      try { 
	  safeExecuteUpdate(statement_, "CREATE DISTINCT TYPE " + COLLECTION
			    + ".PS_AGE AS INTEGER WITH COMPARISONS ");
      } catch (Exception e) {
	  System.out.println("Warning:  Unable to create distinct type PS_AGE"); 
      } 

    // Setup PSTEST_SET table.
    boolean lobSupport = true;
    StringBuffer buffer = new StringBuffer();
    buffer.append(" (C_KEY VARCHAR(20)");
    buffer.append(",C_SMALLINT        SMALLINT      ");
    buffer.append(",C_INTEGER         INTEGER       ");
    buffer.append(",C_REAL            REAL          ");
    buffer.append(",C_FLOAT           FLOAT         ");
    buffer.append(",C_DOUBLE          DOUBLE        ");
    buffer.append(",C_DECIMAL_50      DECIMAL(5,0)  ");
    buffer.append(",C_DECIMAL_105     DECIMAL(10,5) ");
    buffer.append(",C_NUMERIC_50      NUMERIC(5,0)  ");
    buffer.append(",C_NUMERIC_105     NUMERIC(10,5) ");
    buffer.append(",C_CHAR_1          CHAR          ");
    buffer.append(",C_CHAR_50         CHAR(50)      ");
    buffer.append(",C_VARCHAR_50      VARCHAR(50)   ");
    buffer.append(",C_BINARY_1        CHAR FOR BIT DATA        ");
    buffer.append(",C_BINARY_20       CHAR(20)  FOR BIT DATA   ");
    buffer.append(",C_VARBINARY_20    VARCHAR(20) FOR BIT DATA ");
    buffer.append(",C_DATE            DATE          ");
    buffer.append(",C_TIME            TIME          ");
    buffer.append(",C_TIMESTAMP       TIMESTAMP     ");
    if (lobSupport) {
      buffer.append(",C_BLOB         BLOB(200)     ");
      buffer.append(",C_CLOB         CLOB(200)     ");
      if (is400)  { 
	  buffer.append(",C_DBCLOB       DBCLOB(200) CCSID 13488");
      } else {
	  buffer.append(",C_DBCLOB       CLOB(200) ");
      }
      if (is400) { 
        buffer.append(",C_DATALINK     DATALINK      ");
        buffer.append(",C_DISTINCT " + COLLECTION + ".PS_AGE ");
      } else {
        buffer.append(",C_DATALINK     VARCHAR(80)     ");
        buffer.append(",C_DISTINCT " + COLLECTION + ".PS_AGE ");
       
      }
    }
    if (areBigintsSupported() && is400) // @D0A
      buffer.append(",C_BIGINT       BIGINT"); // @D0A
    else
      // @D0A
      buffer.append(",C_BIGINT       INTEGER"); // @D0A
    // @PDA  - new columns for testing toolbox property to match native driver setObject(boolean) 
    if (isToolboxDriver() ) { 
        buffer.append(",C_LONGVARCHAR_257     VARCHAR(257)      ");  
        buffer.append(",C_GRAPHIC_10          GRAPHIC(10)       "); 
        buffer.append(",C_VARGRAPHIC_10       VARGRAPHIC(10)    "); 
        buffer.append(",C_LONGVARGRAPHIC_257  VARGRAPHIC(257)   "); 
    }
    if (areBooleansSupported()) {
	buffer.append(",C_BOOLEAN    BOOLEAN"); 
    }
    buffer.append(")");
    initTable(statement_,   PSTEST_SET, buffer.toString()); 

    JDSupportedFeatures supportedFeatures_= new JDSupportedFeatures(this); 
    // Setup stored procedure.
      JDSetupProcedure.resetCollection(COLLECTION); 
      JDSetupProcedure.create(systemObject_,  connection_,
          JDSetupProcedure.STP_CSPARMS, supportedFeatures_, COLLECTION, out_);
    

 
     initTable(statement_, PSTEST_SETDFP16, "( C1 DECFLOAT(16))");
 
    initTable(statement_, PSTEST_SETDFP34, "( C1 DECFLOAT(34))");
    try {

      connection_.commit(); // for xa
    } catch (Exception e) {
      // Ignore any error
    }

    if (true) {
      
      initTable(statement_, PSTEST_SETXML , "( C1 XML )");

     
      if (is400) {
        initTable(statement_,  PSTEST_SETXML13488, "( C1 XML CCSID 13488)");
      } else {
        initTable(statement_,  PSTEST_SETXML13488, "( C1 XML )");
      }

      
      if (is400) {
        initTable(statement_,  PSTEST_SETXML1200, "( C1 XML CCSID 1200)");
      } else {
        initTable(statement_,  PSTEST_SETXML1200, "( C1 XML )");
      }

      
      if (is400) {
        initTable(statement_,  PSTEST_SETXML37, "( C1 XML CCSID 37)");
      } else {
        initTable(statement_,  PSTEST_SETXML37, "( C1 XML )");
      }

      
      if (is400) {
        initTable(statement_,  PSTEST_SETXML937,  "( C1 XML CCSID 937)");
      } else {
        initTable(statement_,  PSTEST_SETXML937,  "( C1 XML )");
      }

      
      if (is400) {
        initTable(statement_,  PSTEST_SETXML290, "( C1 XML CCSID 290)");
      } else {
        initTable(statement_,  PSTEST_SETXML290, "( C1 XML )");
      }

    }
    try { 
	connection_.commit(); // for xa
    } catch (Exception e) { 
      // Ignore any error 
    }




  }


  public static JDSerializeFile getPstestSet(Connection c) throws Exception {
    JDSerializeFile file = new JDSerializeFile(c, PSTEST_SET); 
    return file; 
  }

  public static JDSerializeFile getPstestSetxml(Connection c) throws Exception {
    JDSerializeFile file = new JDSerializeFile(c, PSTEST_SETXML); 
    return file; 
  }
  public static JDSerializeFile getPstestSetxml13488(Connection c) throws Exception {
    JDSerializeFile file = new JDSerializeFile(c, PSTEST_SETXML13488); 
    return file; 
  }
  public static JDSerializeFile getPstestSetxml1200(Connection c) throws Exception {
    JDSerializeFile file = new JDSerializeFile(c, PSTEST_SETXML1200); 
    return file; 
  }
  public static JDSerializeFile getPstestSetxml37(Connection c) throws Exception {
    JDSerializeFile file = new JDSerializeFile(c, PSTEST_SETXML37); 
    return file; 
  }
  public static JDSerializeFile getPstestSetxml937(Connection c) throws Exception {
    JDSerializeFile file = new JDSerializeFile(c, PSTEST_SETXML937); 
    return file; 
  }
  public static JDSerializeFile getPstestSetxml290(Connection c) throws Exception {
    JDSerializeFile file = new JDSerializeFile(c, PSTEST_SETXML290); 
    return file; 
  }

  public static JDSerializeFile getPstestSetdfp16(Connection c) throws Exception {
    JDSerializeFile file = new JDSerializeFile(c, PSTEST_SETDFP16); 
    return file; 
  }
  public static JDSerializeFile getPstestSetdfp34(Connection c) throws Exception {
    JDSerializeFile file = new JDSerializeFile(c, PSTEST_SETDFP34); 
    return file; 
  }

  public static JDSerializeFile getSerializeFile(Connection c, int file) throws Exception { 
    switch (file) {
      case SET: return getPstestSet(c); 
      case SETXML: return getPstestSetxml(c); 
      case SETXML13488 : return getPstestSetxml13488(c); 
      case SETXML1200 : return getPstestSetxml1200(c); 
      case SETXML37 : return getPstestSetxml37(c); 
      case SETXML937 : return getPstestSetxml937(c); 
      case SETXML290 : return getPstestSetxml290(c); 
      case SETDFP16  : return getPstestSetdfp16(c);
      case SETDFP34  : return getPstestSetdfp34(c);
      default:
        throw new Exception("File "+file+" not supported"); 
    }
  }

  
/**
 * Performs setup needed after running testcases.
 * 
 * @exception Exception
 *                If an exception occurs.
 */
   public void cleanup ()
   throws Exception
   {
     boolean dropUDTfailed = false;
     boolean autocommit = connection_.getAutoCommit(); 
     if (autocommit) {
       connection_.setAutoCommit(false);
     }
     
     PreparedStatement ps = connection_.prepareStatement("DELETE FROM " + PSTEST_CONCUR + " WHERE STARTTIME=?");
     ps.setTimestamp(1, startTimestamp);
     ps.executeUpdate();

     statement_.executeUpdate("DELETE FROM " + PSTEST_CONCUR + " WHERE STARTTIME < CURRENT TIMESTAMP - 2 HOURS");

     ResultSet rs = statement_.executeQuery("SELECT COUNT(*) FROM " + PSTEST_CONCUR);
     int count;
     rs.next();
     count = rs.getInt(1);
     rs.close();
     if (count == 0) {

       cleanupTable(statement_, PSTEST_SET);

       if (areLobsSupported()) {
         try {
           statement_.executeUpdate("DROP DISTINCT TYPE " + COLLECTION + ".PS_AGE");
         } catch (Exception e) {
           System.out.println("Error on DROP DISTINCT TYPE " + COLLECTION + ".PS_AGE");
           dropUDTfailed=true; 
           e.printStackTrace();

         }
       }
       cleanupTable(statement_, PSTEST_SETDFP16);
       cleanupTable(statement_, PSTEST_SETDFP34);

       cleanupTable(statement_, PSTEST_SETXML);

     } else {
       System.out.println("JDPSTest.cleanup not cleanning because "+count+" jobs are running"); 
     }

     try {
       connection_.commit(); // for xa
       if (autocommit) {
         connection_.setAutoCommit(true);
       }
     } catch (Exception e) {
       System.out.println("Warning on commit"); 
       e.printStackTrace(System.out); 
     }
     

      statement_.close ();
      try { 
        connection_.commit(); // for xa
      } catch (Exception e) { 
      }

      if (dropUDTfailed) {
	  System.out.println("Drop of UDT failed.  Dropping collection so that next run will work"); 
	  dropCollections(connection_); 
      } 
      connection_.close ();

      
   }


/**
Cleanup - - this does not run automatically - - it is called by JDCleanup.
**/
public static void dropCollections(Connection c) {
  try {
    Statement stmt = c.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + PSTEST_CONCUR);
    int count;
    rs.next();
    count = rs.getInt(1);
    rs.close();
    if (count == 0) {
      // Make sure the system reply list is set so that reply
      // message will be sent.
      stmt.executeUpdate("CALL QSYS2.QCMDEXC('CHGJOB INQMSGRPY(*SYSRPYL)       ')");
      dropCollection(c, COLLECTION);
    }
  } catch (Exception e) {
    System.out.println("Error dropping "+COLLECTION); 
    e.printStackTrace();
  }

}


/**
Creates the testcases.
**/
   public void createTestcases2 ()
   {
	   
	   if(TestDriverStatic.pause_)
	   { 
		  	try 
		  	{						
		  		systemObject_.connectService(AS400.DATABASE);
			}
	     	catch (AS400SecurityException e) 
	     	{
				e.printStackTrace();
			} 
	     	catch (IOException e) 
	     	{
	     	    e.printStackTrace();
			}
				 	 	   
	     	try
	     	{
	     	    Job[] jobs = systemObject_.getJobs(AS400.DATABASE);
	     	    System.out.println("Host Server job(s): ");

	     	    	for(int i = 0 ; i< jobs.length; i++)
	     	    	{   	    	
	     	    		System.out.println(jobs[i]);
	     	    	}    	    
	     	 }
	     	 catch(Exception exc){}
	     	    
	     	 try 
	     	 {
	     	    	System.out.println ("Toolbox is paused. Press ENTER to continue.");
	     	    	System.in.read ();
	     	 } 
	     	 catch (Exception exc) {};   	   
	   } 
	   
	   
      addTestcase (new JDPSBatch (systemObject_,
                                  namesAndVars_, runMode_, fileOutputStream_, 
                                  password_));



      addTestcase (new JDPSBatchNative (systemObject_,
					    namesAndVars_, runMode_, fileOutputStream_, 
					    password_));


      addTestcase (new JDPSBatchStress (systemObject_,
                                  namesAndVars_, runMode_, fileOutputStream_, 
                                  password_));

      
      
      addTestcase (new JDPSBatchTruncation (systemObject_,
          namesAndVars_, runMode_, fileOutputStream_, 
          password_, pwrSysUserID_, pwrSysPassword_));



      addTestcase (new JDPSBatchCompress (systemObject_,
                                  namesAndVars_, runMode_, fileOutputStream_, 
                                  password_, pwrSysUserID_, pwrSysPassword_));


      addTestcase (new JDPSBatchCompressPerformance (systemObject_,
                                  namesAndVars_, runMode_, fileOutputStream_, 
                                  password_, pwrSysUserID_, pwrSysPassword_));


      addTestcase (new JDPSClearParameters (systemObject_,
                                            namesAndVars_, runMode_, fileOutputStream_, 
                                            password_));

      addTestcase (new JDPSClose (systemObject_,
                                  namesAndVars_, runMode_, fileOutputStream_, 
                                  password_));

      addTestcase (new JDPSDataTruncation (systemObject_,
                                           namesAndVars_, runMode_, fileOutputStream_, 
                                           password_));

      addTestcase (new JDPSExecute (systemObject_,
                                    namesAndVars_, runMode_, fileOutputStream_, 
                                    password_));

      addTestcase (new JDPSMisc (systemObject_,
                                 namesAndVars_, runMode_, fileOutputStream_, 
                                 password_));

      addTestcase (new JDPSResults (systemObject_,
                                    namesAndVars_, runMode_, fileOutputStream_, 
                                    password_));

      addTestcase (new JDPSSetArray (systemObject_,
                                     namesAndVars_, runMode_, fileOutputStream_, 
                                     password_));

      addTestcase (new JDPSSetAsciiStream (systemObject_,
                                           namesAndVars_, runMode_, fileOutputStream_, 
                                           password_));

      addTestcase (new JDPSSetBigDecimal (systemObject_,
                                          namesAndVars_, runMode_, fileOutputStream_, 
                                          password_));

      addTestcase (new JDPSSetBinaryStream (systemObject_,
                                            namesAndVars_, runMode_, fileOutputStream_, 
                                            password_));

      addTestcase (new JDPSSetBinaryStream40 (systemObject_,
                                            namesAndVars_, runMode_, fileOutputStream_, 
                                            password_));

      addTestcase (new JDPSSetBlob (systemObject_,
                                    namesAndVars_, runMode_, fileOutputStream_, 
                                    password_));

      addTestcase (new JDPSSetBlob40 (systemObject_,
                                    namesAndVars_, runMode_, fileOutputStream_, 
                                    password_));

      addTestcase (new JDPSSetBoolean (systemObject_,
                                       namesAndVars_, runMode_, fileOutputStream_, 
                                       password_));

      addTestcase (new JDPSSetByte (systemObject_,
                                    namesAndVars_, runMode_, fileOutputStream_, 
                                    password_));

      addTestcase (new JDPSSetBytes (systemObject_,
                                     namesAndVars_, runMode_, fileOutputStream_, 
                                     password_));

      addTestcase (new JDPSSetCharacterStream (systemObject_,
                                               namesAndVars_, runMode_, fileOutputStream_, 
                                               password_));

      addTestcase (new JDPSSetClob (systemObject_,
                                    namesAndVars_, runMode_, fileOutputStream_, 
                                    password_));

      addTestcase (new JDPSSetClob40 (systemObject_,
                                    namesAndVars_, runMode_, fileOutputStream_, 
                                    password_));

      addTestcase (new JDPSSetDate (systemObject_,
                                    namesAndVars_, runMode_, fileOutputStream_, 
                                    password_));

      addTestcase (new JDPSSetDouble (systemObject_,
                                      namesAndVars_, runMode_, fileOutputStream_, 
                                      password_));

      addTestcase (new JDPSSetFloat (systemObject_,
                                     namesAndVars_, runMode_, fileOutputStream_, 
                                     password_));

      addTestcase (new JDPSSetInt (systemObject_,
                                   namesAndVars_, runMode_, fileOutputStream_, 
                                   password_));

      addTestcase (new JDPSSetLong (systemObject_,
                                    namesAndVars_, runMode_, fileOutputStream_, 
                                    password_));

      addTestcase (new JDPSSetNull (systemObject_,
                                    namesAndVars_, runMode_, fileOutputStream_, 
                                    password_));

      addTestcase (new JDPSSetObject2 (systemObject_,
                                       namesAndVars_, runMode_, fileOutputStream_, 
                                       password_));

      addTestcase (new JDPSSetObject3 (systemObject_,
                                       namesAndVars_, runMode_, fileOutputStream_, 
                                       password_));

      addTestcase (new JDPSSetObject4 (systemObject_,
                                       namesAndVars_, runMode_, fileOutputStream_, 
                                       password_));
      addTestcase (new JDPSSetObject3SQLType (systemObject_,
          namesAndVars_, runMode_, fileOutputStream_, 
          password_));

      addTestcase (new JDPSSetObject4SQLType (systemObject_,
          namesAndVars_, runMode_, fileOutputStream_, 
          password_));

      addTestcase (new JDPSSetRef (systemObject_,
                                   namesAndVars_, runMode_, fileOutputStream_, 
                                   password_));

      addTestcase (new JDPSSetShort (systemObject_,
                                     namesAndVars_, runMode_, fileOutputStream_, 
                                     password_));

      addTestcase (new JDPSSetString (systemObject_,
                                      namesAndVars_, runMode_, fileOutputStream_, 
                                      password_));

      addTestcase (new JDPSSetTime (systemObject_,
                                    namesAndVars_, runMode_, fileOutputStream_, 
                                    password_));

      addTestcase (new JDPSSetTimestamp (systemObject_,
                                         namesAndVars_, runMode_, fileOutputStream_, 
                                         password_));

      addTestcase (new JDPSSetUnicodeStream (systemObject_,
                                             namesAndVars_, runMode_, fileOutputStream_, 
                                             password_));


         addTestcase (new JDRowSetPSTestcase (systemObject_,    // @E1
                                              namesAndVars_, runMode_, fileOutputStream_, 
                                              password_, pwrSysUserID_, pwrSysPassword_));



      addTestcase (new JDPSSetNString  (systemObject_,
                                             namesAndVars_, runMode_, fileOutputStream_, 
                                             password_));

      addTestcase (new JDPSSetNClob (systemObject_,
                                    namesAndVars_, runMode_, fileOutputStream_, 
                                    password_));

      addTestcase (new JDPSSetNCharacterStream (systemObject_,
                                    namesAndVars_, runMode_, fileOutputStream_, 
                                    password_));


      addTestcase (new JDPSSetRowId (systemObject_,
                                    namesAndVars_, runMode_, fileOutputStream_, 
                                    password_));



      addTestcase (new JDPSSetSQLXML (systemObject_,
                                    namesAndVars_, runMode_, fileOutputStream_, 
                                    password_));

      addTestcase (new JDPSSetDB2Default (systemObject_,
                                    namesAndVars_, runMode_, fileOutputStream_, 
                                    password_));

      addTestcase (new JDPSSetDB2Unassigned (systemObject_,
                                    namesAndVars_, runMode_, fileOutputStream_, 
                                    password_));


      addTestcase (new JDPSSetDBDefault (systemObject_,
                                    namesAndVars_, runMode_, fileOutputStream_, 
                                    password_));

      addTestcase (new JDPSSetDBUnassigned (systemObject_,
                                    namesAndVars_, runMode_, fileOutputStream_, 
                                    password_));


   }
}



