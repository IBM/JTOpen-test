///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSSetBoolean.java
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
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSSetBoolean.java
//
// Classes:      JDCSSetBoolean.java
//
////////////////////////////////////////////////////////////////////////
//
//
//                              
// 
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDSetupProcedure;
import test.JDTestDriver;



/**
Testcase JDCSSetBoolean.  This tests the following
method of the JDBC CallableStatement class:

     setBoolean()

**/
public class JDCSSetBoolean
extends JDCSSetTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSSetBoolean";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCSTest.main(newArgs); 
   }


/**
Constructor.
**/
    public JDCSSetBoolean (AS400 systemObject,
                          Hashtable<String,Vector<String>> namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDCSSetBoolean",
               namesAndVars, runMode, fileOutputStream,
               password);
    }



/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
    throws Exception
    {
      super.setup(); 

    }

/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
      super.cleanup(); 
    }


/**
 get the expected message based on the driver and situation
*/

    public String getExpectedException(String situation) throws SQLException {
	int driver = getDriver(); 
	if (situation.equals("SetRegisteredParameterMismatch")) {
	    switch (driver) {
		case JDTestDriver.DRIVER_JCC:
		    return "does  not match between the set method and the registerOutParameter method";
		default:
		    break; 
	    }
	} else {
	    throw new SQLException("Unrecognized situation "+situation); 
	}
	return "getExpectedException("+situation+") TBD"; 
    } 
/**
setBoolean() - Set parameter PARAM_1 which doesn't exist.
- Using an ordinal parameter
**/
    public void Var001()
    {
        if(checkJdbc30())
        {
            try {
                cs.setBoolean(0, true);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e){
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setBoolean() - Set parameter PARAM_1 which doesn't exist.
- Using an ordinal parameter
**/
    public void Var002()
    {
        if(checkJdbc30())
        {
            try{
                cs.setBoolean(-1, true);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setBoolean() - Set parameter PARAM_1 which is too big.
- Using an ordinal parameter
**/
    public void Var003()
    {
        if(checkJdbc30())
        {
            try {
                cs.setBoolean(35, true);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setBoolean() - Should throw an exception when the callable statment is closed.
- Using an ordinal parameter
**/
    public void Var004()
    {
        if(checkJdbc30())
        {
            try
            {
                cs.close();
                cs.setBoolean(1, true);
                failed("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setBoolean() - Should work with a valid parameter name.
- Using an ordinal parameter
**/
    public void Var005 ()
    {
        if(checkJdbc30()) {

            try {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                
                cs.setBoolean(1, true);
                cs.execute();
                boolean check = cs.getBoolean(1);
                cs.close();
                assertCondition (check == true);
            }
            catch(Exception e){
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setBoolean() - Should throw exception when the parameter is
not an input parameter.
- Using an ordinal parameter
**/
    public void Var006 ()
    {
        if(checkJdbc30())
        {
            try {
                CallableStatement cs1 = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + "( ?, ?, ?)");

                cs1.setBoolean(2, true);
		// For jcc, error not detected until execute time
		cs1.execute(); 
                cs1.close();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }

    }

/**
setBoolean() - Set a SMALLINT parameter - false
- Using an ordinal parameter
**/
    public void Var007()
    {
        if(checkJdbc30())
        {
            try {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());

                cs.setBoolean(1, false);
                cs.execute();
		boolean check = cs.getBoolean(1);
		cs.close();
		assertCondition(check == false);
            }
            catch(Exception e){
		failed (e, "Unexpected Exception");
	    }
	}    
    }

/**
setBoolean() - Set a SMALLINT parameter - true
- Using an ordinal parameter
**/
    public void Var008()
    {
        if(checkJdbc30())
        {
            try {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());

                cs.setBoolean(1, true);
                cs.execute();
		boolean check = cs.getBoolean(1);
		cs.close();
		assertCondition(check == true);
            }
            catch(Exception e){
		failed (e, "Unexpected Exception");
	    }
	}    
    }


/**
setBoolean() - Set a INTEGER parameter.
- Using an ordinal parameter
**/
    public void Var009()
    {
	//
	// In October 2011, toolbox was changed to allow getBoolean from an integer column.
        // After this change, this test began to pass.   Toolbox has always permitted
        // the setBoolean on a Prepared Statement parameter:  see JDPSSetBoolean variation 9.
        // 
	// The JCC driver does not allow this because the set parameter is different than
        // the registered parameter.  The toolbox driver does not check the bound parameter.
	//
	// Changing this testcase to pass.  This will also pass for native when
	// the getBoolean also works for the native driver. 
	// 
	StringBuffer info = new StringBuffer(" -- call setBoolean against INTEGER parameter -- Changed October 2011");

        if(checkJdbc30())
        {
            try {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBoolean(2, true);
                cs.execute();
		boolean check = cs.getBoolean(2);
		cs.close();
                assertCondition(check == true, "got "+check+" sb true "+info.toString()); 
	    }
            catch(Exception e){
              failed (e, "Unexpected Exception");
	    }
        }
    }

/**
setBoolean() - Set a REAL parameter.
- Using an ordinal parameter
**/
    public void Var010()
    {
      StringBuffer info = new StringBuffer(" -- call setBoolean against REAL parameter -- Changed October 2011");
        if(checkJdbc30())
        {
            try {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBoolean(3, false);
		cs.execute();
		boolean check = cs.getBoolean(3);
		cs.close();
                assertCondition(check == false, "got "+check+" sb false "+info.toString()); 

	    }
            catch(Exception e) {
                        failed (e, "Unexpected Exception");
	    }    
        }
    }

/**
setBoolean() - Set a FLOAT parameter.
- Using an ordinal parameter
**/
    public void Var011()
    {
      StringBuffer info = new StringBuffer(" -- call setBoolean against FLOAT parameter -- Changed October 2011");
        if(checkJdbc30())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBoolean(4, false);
		cs.execute();
		boolean check = cs.getBoolean(4);
		cs.close();
                assertCondition(check == false, "got "+check+" sb false "+info.toString()); 
               
            }
            catch(Exception e)
            {
                        failed (e, "Unexpected Exception");
            }
        }
    }

/**
setBoolean() - Set a DOUBLE parameter.
- Using an ordinal parameter
**/
    public void Var012 ()
    {
      StringBuffer info = new StringBuffer(" -- call setBoolean against DOUBLE parameter -- Changed October 2011");
        if(checkJdbc30())
        {
            try {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBoolean(5, true);
		cs.execute();
		boolean check = cs.getBoolean(5);
		cs.close();
                assertCondition(check == true, "got "+check+" sb true "+info.toString()); 
               
            }
            catch(Exception e) {
                        failed (e, "Unexpected Exception");
            }
        }
    }

/**
setBoolean() - Set a DECIMAL parameter.
- Using an ordinal parameter
**/
    public void Var013 ()
    {
      StringBuffer info = new StringBuffer(" -- call setBoolean against DECIMAL parameter -- Changed October 2011");

        if(checkJdbc30())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBoolean(6, true);
		cs.execute();
		boolean check = cs.getBoolean(6);
		cs.close();
                assertCondition(check == true, "got "+check+" sb true "+info.toString()); 
               
            }
            catch(Exception e)
            {
                        failed (e, "Unexpected Exception");
            }
        }
    }

/**
setBoolean() - Set a NUMERIC parameter
- Using an ordinal parameter
**/
    public void Var014 ()
    {
      StringBuffer info = new StringBuffer(" -- call setBoolean against NUMERIC parameter -- Changed October 2011");

        if(checkJdbc30())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBoolean(8, true);
		cs.execute();
		boolean check = cs.getBoolean(8);
		cs.close();
                assertCondition(check == true, "got "+check+" sb true "+info.toString()); 
              
            }
            catch(Exception e)
            {
                        failed (e, "Unexpected Exception");
            }
        }
    }

/**
setBoolean() - Set a CHAR(1) parameter.
- Using an ordinal parameter
**/
    public void Var015 ()
    {
      StringBuffer info = new StringBuffer(" -- call setBoolean against CHAR parameter -- Changed October 2011");

        if(checkJdbc30())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBoolean(10, false);
		cs.execute();
		boolean check = cs.getBoolean(10);
		cs.close();
                assertCondition(check == false, "got "+check+" sb false "+info.toString()); 
                
            }
            catch(Exception e)
            {
              failed (e, "Unexpected Exception");
                
            }
        }
    }
/**
setBoolean() - Set a CHAR(50) parameter.
- Using an ordinal parameter
**/
    public void Var016 ()
    {
      StringBuffer info = new StringBuffer(" -- call setBoolean against CHAR parameter -- Changed October 2011");

        if(checkJdbc30())
        {
            try{
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBoolean(11, false);
		cs.execute();
		boolean check = cs.getBoolean(11);
		cs.close();
                assertCondition(check == false, "got "+false+" sb true "+info.toString()); 
	
            }
            catch(Exception e){
              failed (e, "Unexpected Exception");
                    
            }
        }
    }
/**
setBoolean() - Set a VARCHAR(50) parameter.
- Using an ordinal parameter
**/
    public void Var017 ()
    {
      StringBuffer info = new StringBuffer(" -- call setBoolean against VARCHAR parameter -- Changed October 2011");

        if(checkJdbc30())
        {
            try{
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBoolean(12, true);
		cs.execute();
		boolean check = cs.getBoolean(12);
                assertCondition(check == true, "got "+check+" sb true "+info.toString()); 
            }
            catch(Exception e){               
                        failed (e, "Unexpected Exception");
	    }
	}
    }
    
/**
setBoolean() - Set a CLOB parameter.
- Using an ordinal parameter
**/
    public void Var018 ()
    {
        if(checkJdbc30())
        {
            if(checkLobSupport ())
            {
                try{
                    cs=connection_.prepareCall(sql);
                    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_);
                    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                    cs.setBoolean(20, true);
		    cs.execute();
		    failed ("Didn't throw SQLException");
                }
                catch(Exception e){
                    try{
                        assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                        cs.close();
                    }
                    catch (SQLException s){
                        failed (s, "Unexpected Exception");
                    }
                }               
            }   
        }
    }
    

/**
setBoolean() - Set a Blob parameter.
- Using an ordinal parameter
**/
    public void Var019()
    {
        if(checkJdbc30())
        {
            try{
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBoolean(19, true);
                cs.execute();
		failed ("Didn't throw SQLException");
            }catch(Exception e)
            {
		try{
                        assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                        cs.close();
                    }
                    catch (SQLException s){
                        failed (s, "Unexpected Exception");
                    }
	    }
	}
    }

/**
setBoolean() - Set a DATE parameter to a value that is not a valid date.  This should throw an exception. 
- Using an ordinal parameter
**/
    public void Var020()
    {
        if(checkJdbc30())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBoolean(15,  true);
		cs.execute();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                try{
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                    cs.close();
                }
                catch (SQLException s)
                {
                    failed (s, "Unexpected Exception");
                }
            }
        }
    }

/**
setBoolean() - Set a TIME parameter to a value that is not a valid date.  This should throw an exception. 
- Using an ordinal parameter
**/
    public void Var021()
    {
        if(checkJdbc30())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBoolean(16, false);
		cs.execute();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                try{
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                    cs.close();
                }
                catch (SQLException s)
                {
                    failed (s, "Unexpected Exception");
                }
            }
        }
    }

/**
setBoolean() - Set a TIMESTAMP parameter to a value that is not valid.  This should throw an exception.
- Using an ordinal parameter
**/
    public void Var022()
    {
        if(checkJdbc30())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBoolean(17,  true);
		cs.execute();
		failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                try{
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                    cs.close();
                }
                catch (SQLException s)
                {
                    failed (s, "Unexpected Exception");
                }
            }
        }
    }

/**
setBoolean() - Set a DATALINK parameter.
- Using an ordinal parameter
**/
    public void Var023()
    {
      StringBuffer info = new StringBuffer(" -- call setBoolean against DATALINK parameter -- Changed October 2011");

        if(checkJdbc30())
        {
            if(checkLobSupport ())
            {
                try {
                    cs=connection_.prepareCall(sql);
                    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                         supportedFeatures_);
                    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                    cs.setBoolean(18, true);
                    cs.execute ();
		    boolean check = cs.getBoolean(18);

		    assertCondition(check == true, "got "+check+" sb true "+info.toString()); 
                }
                catch(Exception e){
                        failed (e, "Unexpected Exception");
                }
            }
        }
    }

  /**
   * setBoolean() - Set a BIGINT parameter. - Using an ordinal parameter
   **/
  public void Var024() {

    if (checkJdbc30()) {
      if (checkBigintSupport()) {
        setTestSuccessful("setBoolean", 22, "false", "0",
            " -- call setBoolean against BIGINT parameter -- Changed October 2011");
      }
    }
  }
 
/**
setBoolean() - Set a BINARY parameter, should throw error 
- Using an ordinal parameter
**/
    public void Var025()
    {
        if(checkJdbc30())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());

		cs.setBoolean (13, false);
                cs.execute();
                failed("Didn't throw SQLException");

            }
            catch(Exception e)
            {
                try{
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                    cs.close();
                }
                catch (SQLException s)
                {
                    failed (s, "Unexpected Exception");
                }
            }
        }
    }

/**
setBoolean() - Set a VARBINARY parameter should throw error
- Using an ordinal parameter
**/
    public void Var026()
    {
        if(checkJdbc30())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
               
                cs.setBoolean (14, true);
                cs.execute();
                failed("Didn't throw SQLException");

            }
            catch(Exception e)
            {
                try{
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                    cs.close();
                }
                catch (SQLException s)
                {
                    failed (s, "Unexpected Exception");
                }
            }
        }
    }


/**
setBoolean() - Set more than one parameter.
- Using an ordinal parameter
**/
    public void Var027()
    {
        if(checkJdbc30())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                boolean first = true;
		boolean second= false;
                cs.setBoolean(1,  first);
                cs.setBoolean(1,  second);
                cs.execute();
                boolean check = cs.getBoolean(1);
                cs.close();
		assertCondition (check == false);
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception");
            }
        }
    }

// Named Parameters

/**
setBoolean() - Set a SMALLINT parameter - false
- Using named parameter
**/
    public void Var028()
    {
	if(checkNamedParametersSupport()) { 
	    if(checkJdbc30())
	    {
		try {
		    cs=connection_.prepareCall(sql);
		    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
							 supportedFeatures_);
		    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());

		    cs.setBoolean("P_SMALLINT", false);
		    cs.execute();
		    boolean check = cs.getBoolean(1);
		    assertCondition(check == false);
		}
		catch(Exception e){
		    failed (e, "Unexpected Exception");
		}
	    }    
	}
    }

/**
setBoolean() - Set a SMALLINT parameter - true
- Using NAMED parameter
**/
    public void Var029()
    {
	if(checkNamedParametersSupport()) { 
	    if(checkJdbc30())
	    {
		try {
		    cs=connection_.prepareCall(sql);
		    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
							 supportedFeatures_);
		    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());

		    cs.setBoolean("P_SMALLINT", true);
		    cs.execute();
		    boolean check = cs.getBoolean(1);
		    assertCondition(check == true);
		}
		catch(Exception e){
		    failed (e, "Unexpected Exception");
		}
	    }    
	}
    }

/**
setBoolean() - Set a INTEGER parameter.
- Using NAMED parameter
**/
    public void Var030()
    {
	if(checkNamedParametersSupport()) {
	    if(checkJdbc30())
	    {
	      StringBuffer info = new StringBuffer(" -- call setBoolean against INTEGER parameter -- Changed October 2011");
		try {

		    cs=connection_.prepareCall(sql);
		    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
							 supportedFeatures_);
		    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		    cs.setBoolean("P_INTEGER", true);
		    cs.execute();
		    boolean check = cs.getBoolean(2);
		    assertCondition(check == true, "got "+check+" sb true "+info.toString()); 
		}
		catch(Exception e){
			failed (e, "Unexpected Exception");
		}
	    }
	}
    }

/**
setBoolean() - Set a REAL parameter.
- Using NAMED parameter
**/
    public void Var031()
    {
	if(checkNamedParametersSupport()) {
	    if(checkJdbc30())
	    {
	      StringBuffer info = new StringBuffer(" -- call setBoolean against REAL parameter -- Changed October 2011");
		try {
		    cs=connection_.prepareCall(sql);
		    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
							 supportedFeatures_);
		    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		    cs.setBoolean("P_REAL", false);
		    cs.execute();
		    boolean check = cs.getBoolean(3);
	                assertCondition(check == false, "got "+check+" sb false "+info.toString()); 
		}
		    catch (Exception s) {
			failed (s, "Unexpected Exception");
		    }
	    }
	}
    }
/**
setBoolean() - Set a FLOAT parameter.
- Using NAMED parameter
**/
    public void Var032()
    {
	if(checkNamedParametersSupport()) {

	    if(checkJdbc30())
	    {
	      StringBuffer info = new StringBuffer(" -- call setBoolean against FLOAT parameter -- Changed October 2011");
		try
		{
		    cs=connection_.prepareCall(sql);
		    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
							 supportedFeatures_);
		    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		    cs.setBoolean("P_FLOAT", false);
		    cs.execute();
		    boolean check = cs.getBoolean(4);
	                assertCondition(check == false, "got "+check+" sb false "+info.toString()); 
		}
		catch(Exception e)
		{
			failed (e, "Unexpected Exception");
		}
	    }
	}
    }
/**
setBoolean() - Set a DOUBLE parameter.
- Using NAMED parameter
**/
    public void Var033 ()
    {
	if(checkNamedParametersSupport()) {

	    if(checkJdbc30())
	    {
	      StringBuffer info = new StringBuffer(" -- call setBoolean against DOUBLE parameter -- Changed October 2011");
		try {
		    cs=connection_.prepareCall(sql);
		    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
							 supportedFeatures_);
		    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		    cs.setBoolean("P_DOUBLE", true);
		    cs.execute();
		    boolean check = cs.getBoolean(5);
	                assertCondition(check == true, "got "+check+" sb true "+info.toString()); 
		}
		catch(Exception e) {
		  failed (e, "Unexpected Exception");
		}
	    }
	}
    }
/**
setBoolean() - Set a DECIMAL parameter.
- Using NAMED parameter
**/
    public void Var034 ()
    {
	if(checkNamedParametersSupport()) {
	    if(checkJdbc30())
	    {
	      StringBuffer info = new StringBuffer(" -- call setBoolean against DECIMAL parameter -- Changed October 2011");
		try
		{
		    cs=connection_.prepareCall(sql);
		    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
							 supportedFeatures_);
		    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		    cs.setBoolean("P_DECIMAL_50", true);
		    cs.execute();
		    boolean check = cs.getBoolean(6);
	                assertCondition(check == true, "got "+check+" sb true "+info.toString()); 
		}
		catch(Exception e)
		{
		  failed (e, "Unexpected Exception");
		}
	    }
	}
    }

/**
setBoolean() - Set a NUMERIC parameter
- Using NAMED parameter
**/
    public void Var035 ()
    {
	if(checkNamedParametersSupport()) {
	    if(checkJdbc30())
	    {
	      StringBuffer info = new StringBuffer(" -- call setBoolean against NUMERIC parameter -- Changed October 2011");
		try
		{
		    cs=connection_.prepareCall(sql);
		    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
							 supportedFeatures_);
		    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		    cs.setBoolean("P_NUMERIC_50", true);
		    cs.execute();
		    boolean check = cs.getBoolean(8);
	                assertCondition(check == true, "got "+check+" sb true "+info.toString()); 
		}
		catch(Exception e)
		{
			failed (e, "Unexpected Exception");
		}
	    }
	}
    }
/**
setBoolean() - Set a CHAR(1) parameter.
- Using NAMED parameter
**/
    public void Var036 ()
    {
	if(checkNamedParametersSupport()) {
	    if(checkJdbc30())
	    {
	      StringBuffer info = new StringBuffer(" -- call setBoolean against CHAR parameter -- Changed October 2011");
try
		{
		    cs=connection_.prepareCall(sql);
		    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
							 supportedFeatures_);
		    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		    cs.setBoolean("P_CHAR_1", false);
		    cs.execute();
		    boolean check = cs.getBoolean(10);
	                assertCondition(check == false, "got "+check+" sb false "+info.toString()); 
		}
		catch(Exception e)
		{
			failed (e, "Unexpected Exception");
		}
	    }
	}
    }
/**
setBoolean() - Set a CHAR(50) parameter.
- Using NAMED parameter
**/
    public void Var037 ()
    {
      StringBuffer info = new StringBuffer(" -- call setBoolean against CHAR parameter -- Changed October 2011");
	if(checkNamedParametersSupport()) {
	    if(checkJdbc30())
	    {
		try{
		    cs=connection_.prepareCall(sql);
		    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
							 supportedFeatures_);
		    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		    cs.setBoolean("P_CHAR_50", false);
		    cs.execute();
		    boolean check = cs.getBoolean(11);
	                assertCondition(check == false, "got "+check+" sb false "+info.toString()); 
		}
		catch(Exception e){

			failed (e, "Unexpected Exception");
		}
	    }
	}
    }
/**
setBoolean() - Set a VARCHAR(50) parameter.
- Using NAMED parameter
**/
// @B2 you need to register the outparm before you execute.  This was working before becuase JDSetupProcedure.register() does this but not to the type we want.
    public void Var038 ()
    {
	if(checkNamedParametersSupport()) {
	    if(checkJdbc30())
	    {
		try{
		    cs=connection_.prepareCall(sql);
		    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
							 supportedFeatures_);
		    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		    cs.setBoolean("P_VARCHAR_50", true);
		    cs.registerOutParameter("P_VARCHAR_50", Types.BOOLEAN);
		    cs.execute();
		//cs.registerOutParameter("P_VARCHAR_50", Types.BOOLEAN);
		    boolean check = cs.getBoolean(12);
		    assertCondition( check == true );           
		}
		catch(Exception e){
		    try{
			assertExceptionIsInstanceOf (e, "java.sql.SQLException");
			cs.close();
		    }
		    catch (SQLException s){
			failed (s, "Unexpected Exception");
		    }
		}
	    }
	}
    }
    
/**
setBoolean() - Set a CLOB parameter.
- Using NAMED parameter
**/
    public void Var039 ()
    {
	if(checkNamedParametersSupport()) {
	    if(checkJdbc30())
	    {
		if(checkLobSupport ())
		{
		    try{
			cs=connection_.prepareCall(sql);
			JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_);
			JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
			cs.setBoolean("P_CLOB", true);
			cs.execute();
			failed ("Didn't throw SQLException");
		    }
		    catch(Exception e){
			try{
			    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
			    cs.close();
			}
			catch (SQLException s){
			    failed (s, "Unexpected Exception");
			}
		    }               
		}   
	    }
	}
    }

/**
setBoolean() - Set a BlOB parameter.
- Using an ordinal parameter
**/
    public void Var040()
    {
	if(checkNamedParametersSupport()) {
	    if(checkJdbc30())
	    {
		try{
		    cs=connection_.prepareCall(sql);
		    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
							 supportedFeatures_);
		    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		    cs.setBoolean("P_BLOB", true);
		    cs.execute();
		    failed ("Didn't throw SQLException");
		}catch(Exception e)
		{
		    try{
			assertExceptionIsInstanceOf (e, "java.sql.SQLException");
			cs.close();
		    }
		    catch (SQLException s){
			failed (s, "Unexpected Exception");
		    }
		}
	    }
	}
    }

/**
setBoolean() - Set a DATE parameter to a value that is not a valid date.  This should throw an exception. 
- Using an ordinal parameter
**/
    public void Var041()
    {
	if(checkNamedParametersSupport()) {
	    if(checkJdbc30())
	    {
		try
		{
		    cs=connection_.prepareCall(sql);
		    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
							 supportedFeatures_);
		    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		    cs.setBoolean("P_DATE",  true);
		    cs.execute();
		    failed ("Didn't throw SQLException");
		}
		catch(Exception e)
		{
		    try{
			assertExceptionIsInstanceOf (e, "java.sql.SQLException");
			cs.close();
		    }
		    catch (SQLException s)
		    {
			failed (s, "Unexpected Exception");
		    }
		}
	    }
	}
    }
/**
setBoolean() - Set a TIME parameter to a value that is not a valid date.  This should throw an exception. 
- Using an ordinal parameter
**/
    public void Var042()
    {
	if(checkNamedParametersSupport()) {
	    if(checkJdbc30())
	    {
		try
		{
		    cs=connection_.prepareCall(sql);
		    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
							 supportedFeatures_);
		    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		    cs.setBoolean("P_TIME", false);
		    cs.execute();
		    failed ("Didn't throw SQLException");
		}
		catch(Exception e)
		{
		    try{
			assertExceptionIsInstanceOf (e, "java.sql.SQLException");
			cs.close();
		    }
		    catch (SQLException s)
		    {
			failed (s, "Unexpected Exception");
		    }
		}
	    }
	}
    }
/**
setBoolean() - Set a TIMESTAMP parameter to a value that is not valid.  This should throw an exception.
- Using an ordinal parameter
**/
    public void Var043()
    {
	if(checkNamedParametersSupport()) {
	    if(checkJdbc30())
	    {
		try
		{
		    cs=connection_.prepareCall(sql);
		    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
							 supportedFeatures_);
		    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		    cs.setBoolean("P_TIMESTAMP",  true);
		    cs.execute();
		    failed ("Didn't throw SQLException");
		}
		catch(Exception e)
		{
		    try{
			assertExceptionIsInstanceOf (e, "java.sql.SQLException");
			cs.close();
		    }
		    catch (SQLException s)
		    {
			failed (s, "Unexpected Exception");
		    }
		}
	    }
	}
    }
/**
setBoolean() - Set a DATALINK parameter.
- Using an ordinal parameter
**/
// @B2 it is illegal to not register the output parameter before you Execute() - made necessary changes 
    public void Var044()
    {
	if(checkNamedParametersSupport()) {
	    if(checkJdbc30())
	    {
		    if(checkLobSupport ())
		    {
			try {
			    cs=connection_.prepareCall(sql);
			    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
								 supportedFeatures_);
			    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
			    cs.setBoolean("P_DATALINK", true);
			    cs.registerOutParameter("P_DATALINK", Types.BOOLEAN);               //@B1A
			    cs.execute ();
			    if(isToolboxDriver())                          //@B1A
			    {                                                                       //@B1A
			   // cs.registerOutParameter("P_DATALINK", Types.BOOLEAN);               //@B1A
				boolean b = cs.getBoolean("P_DATALINK");                            //@B1A
				assertCondition( b == true );                                       //@B1A
			    }                                                                       //@B1A
			    else{
				boolean nativeb = cs.getBoolean("P_DATALINK");
				assertCondition( nativeb == true );

			    }
			}
			catch(Exception e){
			    failed(e, "Unexpected Exception");                                  //@B1A
			}

		    }


		
	    }

	}

    }
/**
setBoolean() - Set a BIGINT parameter.
- Using an ordinal parameter
**/
    public void Var045()
    {
	if(checkNamedParametersSupport()) {
	    if(checkJdbc30())
	    {
		if(checkBigintSupport())
		{
   	           StringBuffer info = new StringBuffer(" -- call setBoolean against BIGINT parameter -- Changed October 2011");
		    try
		    {
			cs=connection_.prepareCall(sql);
			JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
							     supportedFeatures_);
			JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
			cs.setBoolean("P_BIGINT", false);
			cs.execute();
			boolean check = cs.getBoolean(22);
	                assertCondition(check == false, "got "+check+" sb false "+info.toString()); 
		    }
		    catch(Exception e)
		    {
			    failed (e, "Unexpected Exception");
		    }
		}
	    }
	}
    }
/**
setBoolean() - Set a BINARY parameter, should throw error 
- Using an ordinal parameter
**/
    public void Var046()
    {
	if(checkNamedParametersSupport()) {
	    if(checkJdbc30())
	    {
		try
		{
		    cs=connection_.prepareCall(sql);
		    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
							 supportedFeatures_);
		    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());

		    cs.setBoolean ("P_BINARY_20", false);
		    cs.execute();
		    failed("Didn't throw SQLException");

		}
		catch(Exception e)
		{
		    try{
			assertExceptionIsInstanceOf (e, "java.sql.SQLException");
			cs.close();
		    }
		    catch (SQLException s)
		    {
			failed (s, "Unexpected Exception");
		    }
		}
	    }
	}
    }
/**
setBoolean() - Set a VARBINARY parameter should throw error
- Using an ordinal parameter
**/
    public void Var047()
    {
	if(checkNamedParametersSupport()) {
	    if(checkJdbc30())
	    {
		try
		{
		    cs=connection_.prepareCall(sql);
		    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
							 supportedFeatures_);
		    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());

		    cs.setBoolean ("P_VARBINARY_20", true);
		    cs.execute();
		    failed("Didn't throw SQLException");

		}
		catch(Exception e)
		{
		    try{
			assertExceptionIsInstanceOf (e, "java.sql.SQLException");
			cs.close();
		    }
		    catch (SQLException s)
		    {
			failed (s, "Unexpected Exception");
		    }
		}
	    }
	}
    }
    
    
    /**
     * setBoolean() - Set a BOOLEAN parameter. - Using an ordinal parameter
     **/
    public void Var048() {

        if (checkBooleanSupport()) {
          setTestSuccessful("setBoolean", 27, "false", "0",
              " -- call setBoolean against BOOLEAN parameter -- added January 2021");
        }
    }

    
    /**
     * setBoolean() - Set a BOOLEAN parameter. - Using a named parameter
     **/
    public void Var049() {

        if (checkBooleanSupport()) {
          setTestSuccessful("setBoolean", "P_BOOLEAN", "true", "1",
              " -- call setBoolean against BOOLEAN parameter -- added January 2021");
        }
    }

}
