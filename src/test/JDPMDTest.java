///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPMDTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
//
//
//
//
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDPMDTest.java
//
// Classes:      JDPMDTest
//
////////////////////////////////////////////////////////////////////////
//
//
//
//
////////////////////////////////////////////////////////////////////////

package test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.Job;

import test.JD.JDSetupCollection;
import test.JD.PMD.JDPMDGetDB2ParameterName;
import test.JD.PMD.JDPMDGetDirection;
import test.JD.PMD.JDPMDGetParameterClassName;
import test.JD.PMD.JDPMDGetParameterCount;
import test.JD.PMD.JDPMDGetParameterType;
import test.JD.PMD.JDPMDGetParameterTypeName;
import test.JD.PMD.JDPMDGetPrecision;
import test.JD.PMD.JDPMDGetScale;
import test.JD.PMD.JDPMDIsNullable;
import test.JD.PMD.JDPMDIsSigned;
import test.JD.PMD.JDPMDWrapper;

/**
Test driver for the JDBC ParameterMetaData class.
**/
public class JDPMDTest
extends JDTestDriver {


    // Constants.
    public static String COLLECTION     = "JDTESTPMD";

    public static String PMDTEST_SET     = COLLECTION + ".PMDTEST_SET";


    public static final int ZEROPARMS_PREPSTMT  = 0;
    public static final int INSERT_PREPSTMT     = 1;
    public static final int SELECT_PREPSTMT     = 2;
    public static final int IOPARMS_CALLSTMT    = 3;
    public static final int DIRECTIONS_CALLSTMT = 4;
    public static final int RETURN_CALLSTMT     = 5;
    public static final int RETURNPARAMS_CALLSTMT = 6;

    // NOTICE:  If the SQL statements change to have more data types that 
    //          are tested (and therefore, more parameters), this total 
    //          must be changed.
    // NOTE:    I originally wanted a DISTINCT type in the prepared statement
    //          that does the select but the system complained about using one
    //          in this way.  Therefore, we only have a DISTINCT type in the 
    //          update statement now.
    // NOTE:    The system complains if I try to use a DATALINK with an = COMPARISON
    //          too.  I believe I could cast the value to a varchar and do the 
    //          comparison, but that wouldn't be a very useful test given that 
    //          we are already testing varchar.  I just pulled the DATALINK out of
    //          the select statement.
    public static int SELECT_PARM_COUNT = 18;
    public static int INSERT_PARM_COUNT = 19;
    public static int CALL_PARM_COUNT = 22;

    // Private data.
    private Connection  connection_;
    private Statement   statement_;



/**
Run the test as an application.  This should be called
from the test driver's main().

@param  args        The command line arguments.

@exception Exception If an exception occurs.
**/
    public static void main (String args[])
    throws Exception
    {
        runApplication (new JDPMDTest (args));
    }



/**
Constructs an object for applets.

@exception Exception If an exception occurs.
**/
    public JDPMDTest ()
    throws Exception
    {
        super();
    }



/**
Constructs an object for testing applications.

@param      args        The command line arguments.

@exception Exception If an exception occurs.
**/
    public JDPMDTest (String[] args)
    throws Exception
    {
        super (args);
    }



/**
Performs setup needed before running testcases.

@exception Exception If an exception occurs.
**/
    public void setup ()
    throws Exception
    {
        super.setup();

        connection_ = getConnection (getBaseURL (),
                                     systemObject_.getUserId (), encryptedPassword_);


        if (testLib_ != null) {
            COLLECTION = testLib_;
            PMDTEST_SET         = COLLECTION + ".PMDTEST_SET";
        }

/*
        try {
            Statement s = connection_.createStatement();
            s.executeUpdate("DROP COLLECTION " + COLLECTION);
            s.close();
        } catch (SQLException e) {
            // Don't worry if it doesn't drop - it probably doesn't exist.
        }
*/
        JDSetupCollection.create (systemObject_, 
                                  connection_, COLLECTION);


        statement_ = connection_.createStatement ();


        // Setup PMDTEST_SET table.
        boolean lobSupport = (getRelease () >= RELEASE_V7R1M0);
        StringBuffer buffer = new StringBuffer ();
        dropTable(statement_, PMDTEST_SET); 
        buffer.append ("CREATE TABLE ");
        buffer.append (PMDTEST_SET);
        buffer.append ("(C_SMALLINT        SMALLINT NOT NULL  ");                // 1
        buffer.append (",C_INTEGER         INTEGER       ");                     // 2
        buffer.append (",C_REAL            REAL     NOT NULL  ");                // 3
        buffer.append (",C_FLOAT           FLOAT         ");                     // 4
        buffer.append (",C_DOUBLE          DOUBLE   NOT NULL  ");                // 5
        buffer.append (",C_DECIMAL         DECIMAL(5,0)  ");                     // 6
        buffer.append (",C_NUMERIC         NUMERIC(10,5) NOT NULL ");            // 7
        buffer.append (",C_CHAR            CHAR(50)      ");                     // 8
        buffer.append (",C_VARCHAR         VARCHAR(50) NOT NULL  ");             // 9
        buffer.append (",C_BINARY          CHAR(20)  FOR BIT DATA   ");          // 10
        buffer.append (",C_VARBINARY       VARCHAR(20) FOR BIT DATA NOT NULL "); // 11
        buffer.append (",C_DATE            DATE          ");                     // 12
        buffer.append (",C_TIME            TIME NOT NULL         ");             // 13
        buffer.append (",C_TIMESTAMP       TIMESTAMP     ");                     // 14
        if (lobSupport) {                                                  
            buffer.append (",C_BLOB         BLOB(200) NOT NULL    ");            // 15
            buffer.append (",C_CLOB         CLOB(200)     ");                    // 16
            buffer.append (",C_DBCLOB       DBCLOB(200) NOT NULL  ");            // 17
            buffer.append (",C_DATALINK     DATALINK      ");                    // 18
        }
        if (areBigintsSupported())
            buffer.append(",C_BIGINT       BIGINT NOT NULL");                    // 19
        else
            buffer.append(",C_BIGINT       INTEGER NOT NULL");                   // 19
        buffer.append (")");
        statement_.executeUpdate (buffer.toString ());

        // Setup stored procedure.

            JDSupportedFeatures supportedFeatures_= new JDSupportedFeatures(this);
            // For testing the getDirection method.
            JDSetupProcedure.create (systemObject_,
                                     connection_, JDSetupProcedure.STP_CSPARMS,
                                     supportedFeatures_, COLLECTION);

            // For testing all the other methods against a stored procedure call.
            JDSetupProcedure.create (systemObject_,
                                     connection_, JDSetupProcedure.STP_CSTYPESINOUT,
                                     supportedFeatures_, COLLECTION);

            // For testing that ParameterMetaData objects handle return values right.
            JDSetupProcedure.create (systemObject_,
                                     connection_, JDSetupProcedure.STP_CSRV,
                                     supportedFeatures_, COLLECTION);

            JDSetupProcedure.create (systemObject_,
                                     connection_, JDSetupProcedure.STP_CSPARMSRV,
                                     supportedFeatures_, COLLECTION);


        connection_.commit(); // for xa
    }



/**
Performs setup needed after running testcases.

@exception Exception If an exception occurs.
**/
    public void cleanup ()
    throws Exception
    {
        statement_.executeUpdate ("DROP TABLE " + PMDTEST_SET);

        statement_.close ();
        connection_.commit(); // for xa
        connection_.close ();
    }


/**
Cleanup - - this does not run automatically - - it is called by JDCleanup.
**/
    public static void dropCollections(Connection c)
    {
        dropCollection(c, COLLECTION);
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
 	   
 	   
        addTestcase (new JDPMDGetParameterCount  (systemObject_, namesAndVars_, 
                                                  runMode_, fileOutputStream_, 
                                                  password_));
        
        addTestcase (new JDPMDGetParameterClassName  (systemObject_, namesAndVars_, 
                                                      runMode_, fileOutputStream_, 
                                                      password_));

        addTestcase (new JDPMDGetDB2ParameterName  (systemObject_, namesAndVars_, 
                                                      runMode_, fileOutputStream_, 
                                                      password_));


        addTestcase (new JDPMDGetParameterType (systemObject_, namesAndVars_, 
                                                runMode_, fileOutputStream_, 
                                                password_));

        addTestcase (new JDPMDGetParameterTypeName (systemObject_, namesAndVars_, 
                                                    runMode_, fileOutputStream_, 
                                                    password_));

        addTestcase (new JDPMDGetPrecision (systemObject_, namesAndVars_, 
                                            runMode_, fileOutputStream_, 
                                            password_));

        addTestcase (new JDPMDGetScale (systemObject_, namesAndVars_, runMode_, 
                                        fileOutputStream_,  password_));

        addTestcase (new JDPMDIsNullable (systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_,  password_));

        addTestcase (new JDPMDIsSigned (systemObject_, namesAndVars_, runMode_, 
                                        fileOutputStream_,  password_));

        addTestcase (new JDPMDGetDirection (systemObject_, namesAndVars_, runMode_, 
                                        fileOutputStream_,  password_));

        addTestcase (new JDPMDWrapper (systemObject_, namesAndVars_, runMode_, 
                                        fileOutputStream_,  password_));


    }



/**
This method returns a prepared statement or a callable statement that is the 
right format for the various tests of the ParameterMetaData test bucket.
**/
    public static PreparedStatement getStatement(int stmt, Connection connection_, JDSupportedFeatures supportedFeatures) 
    throws Exception
    {
        PreparedStatement ps = null;

        // TODO: This code will not run on a system before v4r5 right now because
        // we are not handling the fact that this statement could require fewer than
        // 24 parameters (if lobs or bigints were not supported yet).
        switch (stmt) {
        case INSERT_PREPSTMT:
            ps = connection_.prepareStatement ("INSERT INTO " +  JDPMDTest.PMDTEST_SET 
                                              + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " 
                                              + " ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            break;
        case SELECT_PREPSTMT:
            ps = connection_.prepareStatement("SELECT * FROM " +  JDPMDTest.PMDTEST_SET + " WHERE "
                                                     + " C_SMALLINT = ? AND C_INTEGER = ?   AND C_REAL = ?      AND C_FLOAT = ? AND "
                                                     + " C_DOUBLE = ?   AND C_DECIMAL = ?   AND C_NUMERIC = ?   AND C_CHAR = ? AND "
                                                     + " C_VARCHAR = ?  AND C_BINARY = ?    AND C_VARBINARY = ? AND C_DATE = ? AND "
                                                     + " C_TIME = ?     AND C_TIMESTAMP = ? AND C_BLOB = ?      AND C_CLOB = ? AND "
                                                     + " C_DBCLOB = ?   AND C_BIGINT = ?");
            break;
        case ZEROPARMS_PREPSTMT:
            ps = connection_.prepareStatement("SELECT * FROM QIWS.QCUSTCDT");
            break;
        case IOPARMS_CALLSTMT:
            ps = JDSetupProcedure.prepare (connection_,
                                           JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures);
	    if (supportedFeatures.decfloatSupport) {
		CALL_PARM_COUNT = 24; 
	    }
	    if (supportedFeatures.booleanSupport) {
		CALL_PARM_COUNT=25; 
	    }

            break;
        case DIRECTIONS_CALLSTMT:
            ps = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + "(?, ?, ?)");
            break;
        case RETURN_CALLSTMT:
            ps = connection_.prepareCall("?=CALL " + JDSetupProcedure.STP_CSRV);
            break;
        case RETURNPARAMS_CALLSTMT:
            ps = connection_.prepareCall("?=CALL " + JDSetupProcedure.STP_CSPARMSRV + "(?, ?, ?)");
            break;
        default:
            throw new Exception("User requested an invalid statement type");
        }

        return (PreparedStatement) ps;  
    }
}



