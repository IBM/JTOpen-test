///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSTest.java
//
// Classes:      JDCSTest
//
////////////////////////////////////////////////////////////////////////

package test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.Job;

import test.JD.JDSetupCollection;
import test.JD.CS.JDCSArrays;
import test.JD.CS.JDCSExecute;
import test.JD.CS.JDCSGetArray;
import test.JD.CS.JDCSGetBigDecimal;
import test.JD.CS.JDCSGetBigDecimal2;
import test.JD.CS.JDCSGetBigDecimal3;
import test.JD.CS.JDCSGetBlob;
import test.JD.CS.JDCSGetBlob2;
import test.JD.CS.JDCSGetBoolean;
import test.JD.CS.JDCSGetBoolean2;
import test.JD.CS.JDCSGetByte;
import test.JD.CS.JDCSGetByte2;
import test.JD.CS.JDCSGetBytes;
import test.JD.CS.JDCSGetBytes2;
import test.JD.CS.JDCSGetCharacterStream;
import test.JD.CS.JDCSGetClob;
import test.JD.CS.JDCSGetClob2;
import test.JD.CS.JDCSGetDate;
import test.JD.CS.JDCSGetDate2;
import test.JD.CS.JDCSGetDouble;
import test.JD.CS.JDCSGetDouble2;
import test.JD.CS.JDCSGetFloat;
import test.JD.CS.JDCSGetFloat2;
import test.JD.CS.JDCSGetInt;
import test.JD.CS.JDCSGetInt2;
import test.JD.CS.JDCSGetLong;
import test.JD.CS.JDCSGetLong2;
import test.JD.CS.JDCSGetNCharacterStream;
import test.JD.CS.JDCSGetNClob;
import test.JD.CS.JDCSGetNString;
import test.JD.CS.JDCSGetObject;
import test.JD.CS.JDCSGetObject2;
import test.JD.CS.JDCSGetObject3;
import test.JD.CS.JDCSGetObject41;
import test.JD.CS.JDCSGetObject41N;
import test.JD.CS.JDCSGetRef;
import test.JD.CS.JDCSGetRowId;
import test.JD.CS.JDCSGetSQLXML;
import test.JD.CS.JDCSGetShort;
import test.JD.CS.JDCSGetShort2;
import test.JD.CS.JDCSGetString;
import test.JD.CS.JDCSGetString2;
import test.JD.CS.JDCSGetTime;
import test.JD.CS.JDCSGetTime2;
import test.JD.CS.JDCSGetTimestamp;
import test.JD.CS.JDCSGetTimestamp2;
import test.JD.CS.JDCSGetURL;
import test.JD.CS.JDCSGetURL2;
import test.JD.CS.JDCSMisc;
import test.JD.CS.JDCSNamed;
import test.JD.CS.JDCSNamed10;
import test.JD.CS.JDCSNamed11;
import test.JD.CS.JDCSNamed13;
import test.JD.CS.JDCSNamed9;
import test.JD.CS.JDCSRSDateTime;
import test.JD.CS.JDCSRegisterOutParameter;
import test.JD.CS.JDCSRegisterOutParameterN;
import test.JD.CS.JDCSRegisterOutParameterNSQLType;
import test.JD.CS.JDCSRegisterOutParameterSQLType;
import test.JD.CS.JDCSReturnValue;
import test.JD.CS.JDCSScrollableResultSet;
import test.JD.CS.JDCSSetArray;
import test.JD.CS.JDCSSetArrayN;
import test.JD.CS.JDCSSetBigDecimal;
import test.JD.CS.JDCSSetBlob;
import test.JD.CS.JDCSSetBoolean;
import test.JD.CS.JDCSSetByte;
import test.JD.CS.JDCSSetBytes;
import test.JD.CS.JDCSSetBytesBinary;
import test.JD.CS.JDCSSetClob;
import test.JD.CS.JDCSSetDate;
import test.JD.CS.JDCSSetDouble;
import test.JD.CS.JDCSSetFloat;
import test.JD.CS.JDCSSetInt;
import test.JD.CS.JDCSSetLong;
import test.JD.CS.JDCSSetNCharacterStream;
import test.JD.CS.JDCSSetNClob;
import test.JD.CS.JDCSSetNString;
import test.JD.CS.JDCSSetObject;
import test.JD.CS.JDCSSetObject3;
import test.JD.CS.JDCSSetObject3SQLType;
import test.JD.CS.JDCSSetObject4;
import test.JD.CS.JDCSSetObject4SQLType;
import test.JD.CS.JDCSSetRowId;
import test.JD.CS.JDCSSetShort;
import test.JD.CS.JDCSSetString;
import test.JD.CS.JDCSSetTime;
import test.JD.CS.JDCSSetTimestamp;
import test.JD.CS.JDCSSetURL;
import test.JD.CS.JDCSWasNull;



/**
Test driver for the JDBC CallableStatement class.
**/
public class JDCSTest
extends JDTestDriver {



    /**
   * 
   */
    // Private data.
    public static  String COLLECTION     = "JDTESTCS";



    /**
    Run the test as an application.  This should be called
    from the test driver's main().
    
    @param  args        The command line arguments.
    
    @exception Exception If an exception occurs.
    **/
    public static void main (String args[])
    throws Exception
    {
        runApplication (new JDCSTest (args));
    }



    /**
    Constructs an object for applets.
    
    @exception Exception If an exception occurs.
    **/
    public JDCSTest ()
    throws Exception
    {
        super();
    }



    /**
    Constructs an object for testing applications.
    
    @param      args        The command line arguments.
    
    @exception Exception If an exception occurs.
    **/
    public JDCSTest (String[] args)
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
        super.setup();                                                          // @D1A

        Connection c = getConnection (getBaseURL ()
                                      + ";errors=full",
                                      systemObject_.getUserId (), encryptedPassword_);
	System.out.println("JDCSTest.setup connectionClass="+c.getClass().getName()); 

        if (testLib_ != null) { // @E1A
            COLLECTION = testLib_;
        }
        JDSetupCollection.create (systemObject_, 
                                  c, COLLECTION);

 
	String collection = COLLECTION;

        if (testLib_ == null) { 
	    collection = JDSetupProcedure.COLLECTION;
	} 
        JDSupportedFeatures supportedFeatures_= new JDSupportedFeatures(this); 
            JDSetupProcedure.create (systemObject_,
                                     c, JDSetupProcedure.STP_RS0, supportedFeatures_, collection);
            JDSetupProcedure.create (systemObject_,
                                     c, JDSetupProcedure.STP_RS1, supportedFeatures_, collection);
            JDSetupProcedure.create (systemObject_,
                                     c, JDSetupProcedure.STP_RS3, supportedFeatures_, collection);

            JDSetupProcedure.create (systemObject_,
                                     c, JDSetupProcedure.STP_CS0, supportedFeatures_, collection);
            JDSetupProcedure.create (systemObject_,
                                     c, JDSetupProcedure.STP_CS1, supportedFeatures_, collection);       //@F2A
            JDSetupProcedure.create (systemObject_,
                                     c, JDSetupProcedure.STP_CSPARMS, supportedFeatures_, collection);
            JDSetupProcedure.create (systemObject_,
                                     c, JDSetupProcedure.STP_CSPARMSRS, supportedFeatures_, collection);
            JDSetupProcedure.create (systemObject_,
                                     c, JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_, collection);
            JDSetupProcedure.create (systemObject_,
                                     c, JDSetupProcedure.STP_CSTYPESOUTX, supportedFeatures_, collection);
            JDSetupProcedure.create (systemObject_,
                                     c, JDSetupProcedure.STP_CSTYPESOUTB, supportedFeatures_, collection);
            JDSetupProcedure.create (systemObject_,
                                     c, JDSetupProcedure.STP_CSTYPESIN, supportedFeatures_, collection);
            JDSetupProcedure.create (systemObject_,
                                     c, JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_, collection);

            JDSetupProcedure.create (systemObject_,
                                     c, JDSetupProcedure.STP_CSTYPESINOUTX, supportedFeatures_, collection);

            JDSetupProcedure.create (systemObject_,
                                     c, JDSetupProcedure.STP_CSTYPESNULL, supportedFeatures_, collection);
            JDSetupProcedure.create (systemObject_,                                                 //@F1A
                                     c, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, collection);  //@F1A
            JDSetupProcedure.create (systemObject_,                                                 //@F1A
                                     c, JDSetupProcedure.STP_CSNULLTEST, supportedFeatures_, collection);  //@F1A
            JDSetupProcedure.create (systemObject_,                                                  //@KBA
                                     c, JDSetupProcedure.STP_CSSRS, supportedFeatures_, collection);     //@KBA
            JDSetupProcedure.create (systemObject_,                                                  //@KBA
                                     c, JDSetupProcedure.STP_CSMSRS, supportedFeatures_, collection);    //@KBA

            if (areReturnValuesSupported()) {                                                       // @E2A
                JDSetupProcedure.create (systemObject_,                                  // @E2A
                                         c, JDSetupProcedure.STP_CSRV, supportedFeatures_, collection);           // @E2A
                JDSetupProcedure.create (systemObject_,                                  // @E2A
                                         c, JDSetupProcedure.STP_CSPARMSRV, supportedFeatures_, collection);      // @E2A
                JDSetupProcedure.create (systemObject_,                                  // @E2A
                                         c, JDSetupProcedure.STP_CSPARMSRSRV, supportedFeatures_, collection);    // @E2A
            }                                                                                       // @E2A

        c.close ();
    }



    /**
    Performs setup needed after running testcases.
    
    @exception Exception If an exception occurs.
    **/
    public void cleanup ()
    throws Exception
    {
	/* Call the garbage collector to free still open Java objects */ 
	System.gc(); 
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
    	
    	
      addTestcase (new JDCSExecute (systemObject_,
                                      namesAndVars_, runMode_, fileOutputStream_, 
                                      password_));

        addTestcase (new JDCSGetArray (systemObject_,
                                       namesAndVars_, runMode_, fileOutputStream_, 
                                       password_));

        addTestcase (new JDCSGetBigDecimal (systemObject_,
                                            namesAndVars_, runMode_, fileOutputStream_, 
                                            password_));

        addTestcase (new JDCSGetBigDecimal2 (systemObject_,
                                             namesAndVars_, runMode_, fileOutputStream_, 
                                             password_));

        addTestcase (new JDCSGetBlob (systemObject_,
                                      namesAndVars_, runMode_, fileOutputStream_, 
                                      password_));

        addTestcase (new JDCSGetBoolean (systemObject_,
                                         namesAndVars_, runMode_, fileOutputStream_, 
                                         password_));

        addTestcase (new JDCSGetByte(systemObject_,
                                     namesAndVars_, runMode_, fileOutputStream_, 
                                     password_));

        addTestcase (new JDCSGetBytes (systemObject_,
                                       namesAndVars_, runMode_, fileOutputStream_, 
                                       password_));

        addTestcase (new JDCSGetClob (systemObject_,
                                      namesAndVars_, runMode_, fileOutputStream_, 
                                      password_));

        addTestcase (new JDCSGetDate (systemObject_,
                                      namesAndVars_, runMode_, fileOutputStream_, 
                                      password_));

        addTestcase (new JDCSGetDouble (systemObject_,
                                        namesAndVars_, runMode_, fileOutputStream_, 
                                        password_));

        addTestcase (new JDCSGetFloat (systemObject_,
                                       namesAndVars_, runMode_, fileOutputStream_, 
                                       password_));

        addTestcase (new JDCSGetInt (systemObject_,
                                     namesAndVars_, runMode_, fileOutputStream_, 
                                     password_));

        addTestcase (new JDCSGetLong (systemObject_,
                                      namesAndVars_, runMode_, fileOutputStream_, 
                                      password_));

        addTestcase (new JDCSGetObject (systemObject_,
                                        namesAndVars_, runMode_, fileOutputStream_, 
                                        password_));

        addTestcase (new JDCSGetRef (systemObject_,
                                     namesAndVars_, runMode_, fileOutputStream_, 
                                     password_));

        addTestcase (new JDCSGetShort (systemObject_,
                                       namesAndVars_, runMode_, fileOutputStream_, 
                                       password_));

        addTestcase (new JDCSGetString (systemObject_,
                                        namesAndVars_, runMode_, fileOutputStream_, 
                                        password_));

        addTestcase (new JDCSGetTime (systemObject_,
                                      namesAndVars_, runMode_, fileOutputStream_, 
                                      password_));

        addTestcase (new JDCSGetTimestamp (systemObject_,
                                           namesAndVars_, runMode_, fileOutputStream_, 
                                           password_));

        JDCSMisc testCaseJDCSMisc = new JDCSMisc (systemObject_,
                                                  namesAndVars_, runMode_, fileOutputStream_, 
                                                  password_);
        testCaseJDCSMisc.setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, namesAndVars_, runMode_, fileOutputStream_,  pwrSysUserID_, pwrSysPassword_);

        addTestcase( testCaseJDCSMisc );

        addTestcase (new JDCSRegisterOutParameter (systemObject_,
                                                   namesAndVars_, runMode_, fileOutputStream_, 
                                                   password_));

        addTestcase (new JDCSRegisterOutParameterSQLType (systemObject_,
                                                   namesAndVars_, runMode_, fileOutputStream_, 
                                                   password_));

        addTestcase (new JDCSRegisterOutParameterN (systemObject_,
                                                   namesAndVars_, runMode_, fileOutputStream_, 
                                                   password_));
        addTestcase (new JDCSRegisterOutParameterNSQLType (systemObject_,
                                                   namesAndVars_, runMode_, fileOutputStream_, 
                                                   password_));

        addTestcase (new JDCSReturnValue (systemObject_,                        // @E2A
                                          namesAndVars_, runMode_, fileOutputStream_,                 // @E2A
                                          password_));                                                        // @E2A

        addTestcase (new JDCSSetBigDecimal (systemObject_,                                          //@F1A
                                            namesAndVars_, runMode_, fileOutputStream_,     //@F1A
                                            password_));                                            //@F1A

        addTestcase (new JDCSSetDate (systemObject_,                                                //@F1A
                                      namesAndVars_, runMode_, fileOutputStream_,           //@F1A
                                      password_));                                                  //@F1A


        addTestcase (new JDCSSetDouble (systemObject_,                                              //@F1A
                                        namesAndVars_, runMode_, fileOutputStream_,         //@F1A
                                        password_));                                                //@F1A

        addTestcase (new JDCSSetFloat (systemObject_,                                               //@F1A
                                       namesAndVars_, runMode_, fileOutputStream_,          //@F1A
                                       password_));                                                 //@F1A

        addTestcase (new JDCSSetInt (systemObject_,                                                 //@F1A
                                     namesAndVars_, runMode_, fileOutputStream_,            //@F1A
                                     password_));                                                   //@F1A

        addTestcase (new JDCSSetLong (systemObject_,                                                //@F1A
                                      namesAndVars_, runMode_, fileOutputStream_,           //@F1A
                                      password_));                                                  //@F1A
      
        addTestcase (new JDCSSetString (systemObject_,                                              //@F1A
                                        namesAndVars_, runMode_, fileOutputStream_,         //@F1A
                                        password_));                                                //@F1A

        addTestcase (new JDCSSetShort (systemObject_,                                               //@F1A
                                       namesAndVars_, runMode_, fileOutputStream_,          //@F1A
                                       password_));                                                 //@F1A

        addTestcase (new JDCSSetTime (systemObject_,                                                //@F1A
                                      namesAndVars_, runMode_, fileOutputStream_,          //@F1A
                                      password_));                                                 //@F1A

        addTestcase (new JDCSSetTimestamp (systemObject_,                                           //@F1A
                                           namesAndVars_, runMode_, fileOutputStream_,          //@F1A
                                           password_));                                                 //@F1A

        addTestcase (new JDCSWasNull (systemObject_,                                                 
                                      namesAndVars_, runMode_, fileOutputStream_,             
                                      password_));                                                    

        addTestcase (new JDCSGetURL (systemObject_,                                                 
                                      namesAndVars_, runMode_, fileOutputStream_,             
                                      password_));

        addTestcase (new JDCSSetBytes (systemObject_,                                                 
                                      namesAndVars_, runMode_, fileOutputStream_,             
                                      password_));

	addTestcase (new JDCSSetBytesBinary (systemObject_,                                                 
                                      namesAndVars_, runMode_, fileOutputStream_,                    password_));

	addTestcase (new JDCSSetBlob (systemObject_,
				      namesAndVars_, runMode_, fileOutputStream_, 
				      password_));

	addTestcase (new JDCSSetBoolean (systemObject_,
				      namesAndVars_, runMode_, fileOutputStream_, 
				      password_));

	addTestcase (new JDCSGetBoolean2 (systemObject_,
				      namesAndVars_, runMode_, fileOutputStream_, 
				      password_));

	addTestcase (new JDCSSetByte (systemObject_,
				      namesAndVars_, runMode_, fileOutputStream_, 
				      password_));

	addTestcase (new JDCSSetClob (systemObject_,
				      namesAndVars_, runMode_, fileOutputStream_, 
				      password_));

	addTestcase (new JDCSSetObject (systemObject_,
				      namesAndVars_, runMode_, fileOutputStream_, 
				      password_));

	addTestcase (new JDCSSetObject3 (systemObject_,
				      namesAndVars_, runMode_, fileOutputStream_, 
				      password_));

	addTestcase (new JDCSSetObject3SQLType (systemObject_,
				      namesAndVars_, runMode_, fileOutputStream_, 
				      password_));


	addTestcase (new JDCSSetObject4 (systemObject_,
				      namesAndVars_, runMode_, fileOutputStream_, 
				      password_));

	addTestcase (new JDCSSetObject4SQLType (systemObject_,
				      namesAndVars_, runMode_, fileOutputStream_, 
				      password_));

	addTestcase (new JDCSGetString2 (systemObject_,
				      namesAndVars_, runMode_, fileOutputStream_, 
				      password_));

	addTestcase (new JDCSGetURL2 (systemObject_,
				      namesAndVars_, runMode_, fileOutputStream_, 
				      password_));

	addTestcase (new JDCSGetByte2 (systemObject_,
				      namesAndVars_, runMode_, fileOutputStream_, 
				      password_));

	addTestcase (new JDCSGetShort2 (systemObject_,
				      namesAndVars_, runMode_, fileOutputStream_, 
				      password_));

	addTestcase (new JDCSGetInt2 (systemObject_,
				      namesAndVars_, runMode_, fileOutputStream_, 
				      password_));

	addTestcase (new JDCSGetLong2 (systemObject_,
				      namesAndVars_, runMode_, fileOutputStream_, 
				      password_));

	addTestcase (new JDCSGetFloat2 (systemObject_,
				      namesAndVars_, runMode_, fileOutputStream_, 
				      password_));

	addTestcase (new JDCSGetDouble2 (systemObject_,
				      namesAndVars_, runMode_, fileOutputStream_, 
				      password_));

	addTestcase (new JDCSGetBigDecimal3 (systemObject_,
				      namesAndVars_, runMode_, fileOutputStream_, 
				      password_));

	addTestcase (new JDCSGetBytes2 (systemObject_,
					namesAndVars_, runMode_, fileOutputStream_, 
					password_));

	addTestcase (new JDCSGetObject2 (systemObject_,
				      namesAndVars_, runMode_, fileOutputStream_, 
				      password_));

	addTestcase (new JDCSGetObject3 (systemObject_,
				      namesAndVars_, runMode_, fileOutputStream_, 
				      password_));

	addTestcase (new JDCSGetObject41 (systemObject_,
				      namesAndVars_, runMode_, fileOutputStream_, 
				      password_));

	addTestcase (new JDCSGetObject41N (systemObject_,
				      namesAndVars_, runMode_, fileOutputStream_, 
				      password_));

	addTestcase (new JDCSGetBlob2 (systemObject_,
				      namesAndVars_, runMode_, fileOutputStream_, 
				      password_));
	
	addTestcase (new JDCSGetClob2 (systemObject_,
				      namesAndVars_, runMode_, fileOutputStream_, 
				      password_));

	addTestcase (new JDCSGetDate2 (systemObject_,
				      namesAndVars_, runMode_, fileOutputStream_, 
				      password_));

	addTestcase (new JDCSGetTime2 (systemObject_,
				      namesAndVars_, runMode_, fileOutputStream_, 
				      password_));

	addTestcase (new JDCSGetTimestamp2 (systemObject_,
				      namesAndVars_, runMode_, fileOutputStream_, 
				      password_));

	addTestcase (new JDCSSetURL (systemObject_,
				    namesAndVars_, runMode_, fileOutputStream_, 
				    password_));

        addTestcase (new JDCSScrollableResultSet (systemObject_,
                                                  namesAndVars_, runMode_, fileOutputStream_, 
                                                  password_));
        

        addTestcase (new JDCSSetNString (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_));

        addTestcase (new JDCSSetNClob (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_));

        addTestcase (new JDCSSetNCharacterStream (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_));


        addTestcase (new JDCSGetNClob (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_));

        addTestcase (new JDCSGetNString (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_));

        addTestcase (new JDCSGetCharacterStream (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_));

        addTestcase (new JDCSGetNCharacterStream (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_));

        try{
             // 5/15/2008 For some release getRelease() is returning 0.
            // Still add testcase if this occurs.  The testcase itself
            // should be smart enough to mark variations as notApplicable
            // if run on a previous release
            {
                addTestcase (new JDCSRSDateTime(systemObject_,
        		namesAndVars_, runMode_, fileOutputStream_, 
        		password_));	// @G1A
            }
        }catch(Exception e){
            //Do nothing, test won't be run.
        }


        addTestcase (new JDCSGetRowId (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_));
        
        addTestcase (new JDCSSetRowId (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_));
        


        addTestcase (new JDCSGetSQLXML (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_));
        

        

	addTestcase (new JDCSSetArray (systemObject_,
				      namesAndVars_, runMode_, fileOutputStream_, 
				      password_));
 

	addTestcase (new JDCSSetArrayN (systemObject_,
				      namesAndVars_, runMode_, fileOutputStream_, 
				      password_));


	addTestcase (new JDCSNamed (systemObject_,
				      namesAndVars_, runMode_, fileOutputStream_, 
				      password_));

	addTestcase (new JDCSNamed9 (systemObject_,
				      namesAndVars_, runMode_, fileOutputStream_, 
				      password_));

	addTestcase (new JDCSNamed10 (systemObject_,
				      namesAndVars_, runMode_, fileOutputStream_, 
				      password_));

	addTestcase (new JDCSNamed11 (systemObject_,
				      namesAndVars_, runMode_, fileOutputStream_, 
				      password_));

	addTestcase (new JDCSNamed13 (systemObject_,
				      namesAndVars_, runMode_, fileOutputStream_, 
				      password_));


	 addTestcase (new JDCSArrays (systemObject_,
       namesAndVars_, runMode_, fileOutputStream_, 
       password_));


    }




    /**
     * There are procedures used by several testcase.  Creating the procedure takes a couple of seconds.
     * This will make sure it exists and only recreate it if needed
     *
     * The following testcases use the procedures.
     * JDCSGetTimestamp2
     *
     **/

    static String[][] procedures = {
	{"ADD10SMINT",
	    "(INOUT B SMALLINT) LANGUAGE SQL " +
	    "SPECIFIC COLLECTION.ADD10SMINT JDCSADD10SMINT: BEGIN DECLARE DUMMY SMALLINT; "+
 	    "SET DUMMY=B; SET B = DUMMY + 10; END JDCSADD10SMINT"
	},

	{"ADD10INT",
	    "(INOUT B INTEGER) LANGUAGE SQL " +
	    "SPECIFIC COLLECTION.ADD10INT JDCSADD10INT: BEGIN DECLARE DUMMY INTEGER; "+
 	    "SET DUMMY=B; SET B = DUMMY + 10; END JDCSADD10INT"
	},
	{ "ADD10REAL",
	  "(INOUT B REAL) LANGUAGE SQL " +
             "SPECIFIC COLLECTION.ADD10REAL JDCSADD10REAL: BEGIN DECLARE DUMMY REAL; "+
             "SET DUMMY=B; SET B = DUMMY + 10; END JDCSADD10REAL"
	},
	{ "ADD10FLOAT",
	  "(INOUT B FLOAT) LANGUAGE SQL " +
             "SPECIFIC COLLECTION.ADD10FLOAT JDCSADD10FLOAT: BEGIN DECLARE DUMMY FLOAT; "+
             "SET DUMMY=B; SET B = DUMMY + 10; END JDCSADD10FLOAT"
	},
	{  "ADD10DOUBLE",
	   "(INOUT B DOUBLE) LANGUAGE SQL " +
             "SPECIFIC COLLECTION.ADD10DOUBLE JDCSADD10DOUBLE: BEGIN DECLARE DUMMY DOUBLE; "+
             "SET DUMMY=B; SET B = DUMMY + 10; END JDCSADD10DOUBLE"
	},
	{   "ADD10DEC50",
	    "(INOUT B DECIMAL(5,0)) LANGUAGE SQL " +
             "SPECIFIC COLLECTION.ADD10DEC50 JDCSADD10DEC50: BEGIN DECLARE DUMMY DECIMAL(5,0); "+
	     "SET DUMMY=B; SET B = DUMMY + 10; END JDCSADD10DEC50"
	},
	{   "ADD10DEC105",
	    "(INOUT B DECIMAL(10,5)) LANGUAGE SQL " +
             "SPECIFIC COLLECTION.ADD10DEC105 JDCSADD10DEC105: BEGIN DECLARE DUMMY DECIMAL(10,5); "+
             "SET DUMMY=B; SET B = DUMMY + 10; END JDCSADD10DEC105"
	},
	{   "ADD10NUM50",
	    "(INOUT B NUMERIC(5,0)) LANGUAGE SQL " +
             "SPECIFIC COLLECTION.ADD10NUM50 JDCSADD10NUM50: BEGIN DECLARE DUMMY SMALLINT; "+
             "SET DUMMY=B; SET B = DUMMY + 10; END JDCSADD10NUM50"
	},
	{    "ADD10NUM105",
             "(INOUT B NUMERIC(10,5)) LANGUAGE SQL " +
             "SPECIFIC COLLECTION.ADD10NUM105 JDCSADD10NUM105: BEGIN DECLARE DUMMY NUMERIC(10,5); "+
             "SET DUMMY=B; SET B = DUMMY + 10; END JDCSADD10NUM105"
	},
	{    "RETURNCHAR1",
	     "(INOUT B CHAR(1)) LANGUAGE SQL " +
             "SPECIFIC COLLECTION.RETURNCHAR1 JDCSRETURNCHAR1: BEGIN DECLARE DUMMY CHAR(1); "+
             "SET DUMMY=B; SET B = DUMMY ; END JDCSRETURNCHAR1"
	},
	{    "RETURNCHAR",
	     "(INOUT B CHAR) LANGUAGE SQL " +
             "SPECIFIC COLLECTION.RETURNCHAR JDCSRETURNCHAR: BEGIN DECLARE DUMMY CHAR; "+
             "SET DUMMY=B; SET B = DUMMY ; END JDCSRETURNCHAR"
	},

	{    "RETURNCHAR50",
	     "(INOUT B CHAR(50)) LANGUAGE SQL " +
             "SPECIFIC COLLECTION.RETURNCHAR50 JDCSRETURNCHAR50: BEGIN DECLARE DUMMY CHAR(50); "+
             "SET DUMMY=B; SET B = DUMMY ; END JDCSRETURNCHAR50" 
	},

	{    "RETURNVARCHAR30",
	     "(INOUT B VARCHAR(30)) LANGUAGE SQL " +
             "SPECIFIC COLLECTION.RETURNVARCHAR30 JDCSRETURNVARCHAR30: BEGIN DECLARE DUMMY VARCHAR(30); "+
             "SET DUMMY=B; SET B = DUMMY ; END JDCSRETURNVARCHAR30"
	},
	{    "RETURNVARCHAR50",
	     "(INOUT B VARCHAR(50)) LANGUAGE SQL " +
             "SPECIFIC COLLECTION.RETURNVARCHAR50 JDCSRETURNVARCHAR50: BEGIN DECLARE DUMMY VARCHAR(50); "+
             "SET DUMMY=B; SET B = DUMMY ; END JDCSRETURNVARCHAR50"
	},
	{    "RETURNBIN20",
	     "(INOUT B BINARY(20)) LANGUAGE SQL " +
             "SPECIFIC COLLECTION.returnbin20 JDCSreturnbin20: BEGIN DECLARE DUMMY BINARY(20); "+
             "SET DUMMY=B; SET B = DUMMY ; END JDCSreturnbin20"
	},

	{      "RETURNVBIN20",
               "(INOUT B VARBINARY(20)) LANGUAGE SQL " +"SPECIFIC COLLECTION.RETURNVBIN20 JDCSRETURNVBIN20: BEGIN DECLARE DUMMY VARBINARY(20); "+
             "SET DUMMY=B; SET B = DUMMY ; END JDCSRETURNVBIN20"
	},
	{     "RETURNDATE",
	     "(INOUT B DATE) LANGUAGE SQL " +
             "SPECIFIC COLLECTION.RETURNDATE JDCSRETURNDATE: BEGIN DECLARE DUMMY DATE; "+
             "SET DUMMY=B; SET B = DUMMY ; END JDCSRETURNDATE"
	},

	{     "RETURNTIME",
	       "(INOUT B TIME) LANGUAGE SQL " +
		  "SPECIFIC COLLECTION.RETURNTIME JDCSRETURNTIME: BEGIN DECLARE DUMMY TIME; "+
		  "SET DUMMY=B; SET B = DUMMY ; END JDCSRETURNTIME"
	},
	{      "RETURNTS",
	       "(INOUT B TIMESTAMP) LANGUAGE SQL " +
             "SPECIFIC COLLECTION.RETURNTS JDCSRETURNTS: BEGIN DECLARE DUMMY TIMESTAMP; "+
             "SET DUMMY=B; SET B = DUMMY ; END JDCSRETURNTS"
	},

	{     "RETURNDL",
	      "(INOUT B DATALINK) LANGUAGE SQL " +
             "SPECIFIC COLLECTION.RETURNDL JDCSRETURNDL: BEGIN DECLARE DUMMY DATALINK; "+
             "SET DUMMY=B; SET B = DUMMY ; END JDCSRETURNDL"
	},
	{     "RETURNBLOB200",
	      "(INOUT B BLOB(200)) LANGUAGE SQL " +
		  "SPECIFIC COLLECTION.RETURNBLOB200 JDCSRETURNBLOB200: BEGIN DECLARE DUMMY BLOB(200); "+
		  "SET DUMMY=B; SET B = DUMMY ; END JDCSRETURNBLOB200"
	},
	{    "RETURNCLOB200",
	     "(INOUT B CLOB(200)) LANGUAGE SQL " +
		  "SPECIFIC COLLECTION.RETURNCLOB200 JDCSRETURNCLOB200: BEGIN DECLARE DUMMY CLOB(200); "+
		  "SET DUMMY=B; SET B = DUMMY ; END JDCSRETURNCLOB200"
	},
	{
	    "ADD10BINT",
	    "(INOUT B BIGINT) LANGUAGE SQL " +
             "SPECIFIC COLLECTION.ADD10BINT JDCSADD10BINT: BEGIN DECLARE DUMMY BIGINT; "+
             "SET DUMMY=B; SET B = DUMMY + 10; END JDCSADD10BINT"
	},
	{
	    "RETURNXML",
	    "(INOUT B XML) LANGUAGE SQL " +
                "SPECIFIC COLLECTION.RETURNXML JDCSRETURNXML: BEGIN DECLARE DUMMY XML; "+
                "SET DUMMY=B; SET B = DUMMY; END JDCSRETURNXML"
	}

    };

    public static void assureProcedureExists(Connection connection, String collection, String procName) throws SQLException {
	for (int i = 0; i < procedures.length; i++) {
	    if (procName.equals(procedures[i][0])) {
		
		String sql = "CREATE PROCEDURE "+collection+"."+procName+procedures[i][1].replaceAll("COLLECTION",collection)  ;
		Statement stmt = connection.createStatement ();
		try {
		    stmt.executeUpdate(sql);
		} catch (Exception e) {
		    String message = e.toString();
		    if (message.indexOf("already exists") >= 0) {
			// IGNORE
		    } else {
			System.out.println("Warning:  exception creating procedure in assureProcedureExists: SQL="+sql);
			e.printStackTrace(System.out); 
		    } 
		}    	    
		return; 
	    } 
	}
	StringBuffer validValue= new StringBuffer(); 
	for (int i = 0; i < procedures.length; i++) {
	    validValue.append("'");
	    validValue.append(procedures[i][0]); 
	    validValue.append("' ");
	}

	throw new SQLException("Procedure '"+procName+"' not valid -- valid values are "+validValue.toString()); 

    } 



}
