///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSSetByte.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSSetByte.java
//
// Classes:      JDCSSetByte.java
//
////////////////////////////////////////////////////////////////////////
//
//
//                              
// 
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import java.awt.TextArea;
import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;

import test.JDSetupProcedure;
import test.JDTestDriver;

/**
Testcase JDCSSetByte.  This tests the following
method of the JDBC CallableStatement class:

     setByte()

**/
public class JDCSSetByte
extends JDCSSetTestcase
{



/**
Constructor.
**/
    public JDCSSetByte(AS400 systemObject,
                          Hashtable namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDCSSetByte",
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
setByte() - Set parameter PARAM_1 which doesn't exist.
- Using an ordinal parameter
**/
    public void Var001()
    {
        if(checkJdbc20())
        {
            try {
                cs.setByte(0, (byte) 12);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e){
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setByte() - Set parameter PARAM_1 which doesn't exist.
- Using an ordinal parameter
**/
    public void Var002()
    {
        if(checkJdbc20())
        {
            try{
                cs.setByte(-1, (byte) 12);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setByte() - Set parameter PARAM_1 which is too big.
- Using an ordinal parameter
**/
    public void Var003()
    {
        if(checkJdbc20())
        {
            try {
                cs.setByte(35, (byte) 12);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setByte() - Should throw an exception when the callable statment is closed.
- Using an ordinal parameter
**/
    public void Var004()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.close();
                cs.setByte(1, (byte) 12);
                failed("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setByte() - Should work with a valid parameter name.
- Using an ordinal parameter
**/
    public void Var005 ()
    {
        if(checkJdbc20()) {

            try {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                
                cs.setByte(1, (byte) 12);
                cs.execute();
                byte check = cs.getByte(1);
                cs.close();
                assertCondition (check == (byte) 12);
            }
            catch(Exception e){
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setByte() - Should throw exception when the parameter is
not an input parameter.
- Using an ordinal parameter
**/
    public void Var006 ()
    {
        if(checkJdbc20())
        {
            try {
                CallableStatement cs1 = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + "( ?, ?, ?)");

                cs1.setByte(2, (byte) 12);
                cs1.close();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }

    }

/**
setByte() - Set a SMALLINT parameter 
- Using an ordinal parameter
**/
    public void Var007()
    {
        if(checkJdbc20())
        {
            try {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());

                cs.setByte(1, (byte) 12);
                cs.execute();
		byte check = cs.getByte(1);
		cs.close();
		assertCondition(check == 12);
            }
            catch(Exception e){
		failed (e, "Unexpected Exception");
	    }
	}    
    }

/**
setByte() - Set a SMALLINT parameter
Using an ordinal parameter
**/
    public void Var008()
    {
        if(checkJdbc20())
        {
            try {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());

                cs.setByte(1, (byte) -12);
                cs.execute();
		byte check = cs.getByte(1);
		cs.close();
		assertCondition(check == -12);
            }
            catch(Exception e){
		failed (e, "Unexpected Exception");
	    }
	}    
    }

/**
setByte() - Set a INTEGER parameter.
- Using an ordinal parameter
**/
    public void Var009()
    {

	//
	// In October 2011, toolbox was changed to allow getByte from an integer column.
        // After this change, this test began to pass.   Toolbox has always permitted
        // the setByte on a Prepared Statement parameter:  see JDPSSetByte variation 8.
        // 
	// The JCC driver does not allow this because the set parameter is different than
        // the registered parameter.  The toolbox driver does not check the bound parameter.
	//
	// Changing this testcase to pass.  This will also pass for native when
	// the getBoolean also works for the native driver. 
	// 

	StringBuffer info = new StringBuffer(" -- call setByte against INTEGER parameter -- Changed October 2011");

        if(checkJdbc20())
        {
            try {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setByte(2, (byte) 12);
                cs.execute();
		byte check = cs.getByte(2);
		cs.close();
		byte expected = 12;
		assertCondition(check == expected, "Got "+check+" expected "+expected+ info.toString()); 
	    }
            catch(Exception e){
              failed (e, "Unexpected Exception "+info.toString());
	    }
        }
    }

/**
setByte() - Set a REAL parameter.
- Using an ordinal parameter
**/
    public void Var010()
    {
      StringBuffer info = new StringBuffer(" -- call setByte against REAL parameter -- Changed October 2011");
        if(checkJdbc20())
        {
            try {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setByte(3, (byte) 12);
		cs.execute();
		byte check = cs.getByte(3);
		cs.close();
                byte expected = 12;
                assertCondition(check == expected, "Got "+check+" expected "+expected+ info.toString()); 

	    }
            catch(Exception e) {
              failed (e, "Unexpected Exception "+info.toString());
	    }    
        }
    }

/**
setByte() - Set a FLOAT parameter.
- Using an ordinal parameter
**/
    public void Var011()
    {
      StringBuffer info = new StringBuffer(" -- call setByte against FLOAT parameter -- Changed October 2011");
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setByte(4, (byte) 12);
		cs.execute();
		byte check = cs.getByte(4);
		cs.close();
                byte expected = 12;
                assertCondition(check == expected, "Got "+check+" expected "+expected+ info.toString()); 
               
            }
            catch(Exception e)
            {
              failed (e, "Unexpected Exception "+info.toString());
            }
        }
    }

/**
setByte() - Set a DOUBLE parameter.
- Using an ordinal parameter
**/
    public void Var012 ()
    {
      StringBuffer info = new StringBuffer(" -- call setByte against DOUBLE parameter -- Changed October 2011");
        if(checkJdbc20())
        {
            try {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setByte(5, (byte) 12);
		cs.execute();
		byte check = cs.getByte(5);
		cs.close();
                byte expected = 12;
                assertCondition(check == expected, "Got "+check+" expected "+expected+ info.toString()); 
               
            }
            catch(Exception e) {
              failed (e, "Unexpected Exception "+info.toString());
            }
        }
    }

/**
setByte() - Set a DECIMAL parameter.
- Using an ordinal parameter
**/
    public void Var013 ()
    {
      StringBuffer info = new StringBuffer(" -- call setByte against DECIMAL parameter -- Changed October 2011");
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setByte(6, (byte) 12);
		cs.execute();
		byte check = cs.getByte(6);
		cs.close();
                byte expected = 12;
                assertCondition(check == expected, "Got "+check+" expected "+expected+ info.toString()); 
               
            }
            catch(Exception e)
            {
              failed (e, "Unexpected Exception "+info.toString());
            }
        }
    }

/**
setByte() - Set a NUMERIC parameter
- Using an ordinal parameter
**/
    public void Var014 ()
    {
      StringBuffer info = new StringBuffer(" -- call setByte against NUMERIC parameter -- Changed October 2011");
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setByte(8, (byte) 12);
		cs.execute();
		byte check = cs.getByte(8);
		cs.close();
                byte expected = 12;
                assertCondition(check == expected, "Got "+check+" expected "+expected+ info.toString()); 
              
            }
            catch(Exception e)
            {
              failed (e, "Unexpected Exception "+info.toString());
            }
        }
    }

/**
setByte() - Set a CHAR(1) parameter.
- Using an ordinal parameter
**/
    public void Var015 ()
    {
      StringBuffer info = new StringBuffer(" -- call setByte against CHAR1 parameter -- Changed October 2011");
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setByte(10, (byte) 0);
		cs.execute();
		byte check = cs.getByte(10);
		cs.close();
                byte expected = 0;
                assertCondition(check == expected, "Got "+check+" expected "+expected+ info.toString()); 
                
            }
            catch(Exception e)
            {
              failed (e, "Unexpected Exception "+info.toString());
            }
        }
    }
/**
setByte() - Set a CHAR(50) parameter.
- Using an ordinal parameter
**/
    public void Var016 ()
    {
      StringBuffer info = new StringBuffer(" -- call setByte against CHAR parameter -- Changed October 2011");
        if(checkJdbc20())
        {
            try{
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setByte(11, (byte) 12);
		cs.execute();
		byte check = cs.getByte(11);
		cs.close();
                byte expected = 12;
                assertCondition(check == expected, "Got "+check+" expected "+expected+ info.toString()); 
	
            }
            catch(Exception e){
              failed (e, "Unexpected Exception "+info.toString());
                    
            }
        }
    }
/**
setByte() - Set a VARCHAR(50) parameter.
- Using an ordinal parameter
**/
    public void Var017 ()
    {
      StringBuffer info = new StringBuffer(" -- call setByte against VARCHAR parameter -- Changed October 2011");
        if(checkJdbc20())
        {
            try{
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setByte(12, (byte) 12);
		cs.execute();
		byte check = cs.getByte(12);
                byte expected = 12;
                assertCondition(check == expected, "Got "+check+" expected "+expected+ info.toString()); 
            }
            catch(Exception e){               
              failed (e, "Unexpected Exception "+info.toString());
	    }
	}
    }
    
/**
setByte() - Set a CLOB parameter.
- Using an ordinal parameter
**/
    public void Var018 ()
    {
        if(checkJdbc20())
        {
            if(checkLobSupport ())
            {
                try{
                    cs=connection_.prepareCall(sql);
                    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_);
                    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                    cs.setByte(20, (byte) 12);
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
setByte() - Set a Blob parameter.
- Using an ordinal parameter
**/
    public void Var019()
    {
        if(checkJdbc20())
        {
            try{
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setByte(19, (byte) 12);
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
setByte() - Set a DATE parameter to a value that is not a valid date.  This should throw an exception. 
- Using an ordinal parameter
**/
    public void Var020()
    {
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setByte(15, (byte) 12);
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
setByte() - Set a TIME parameter to a value that is not a valid date.  This should throw an exception. 
- Using an ordinal parameter
**/
    public void Var021()
    {
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setByte(16, (byte) 12);
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
setByte() - Set a TIMESTAMP parameter to a value that is not valid.  This should throw an exception.
- Using an ordinal parameter
**/
    public void Var022()
    {
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setByte(17, (byte) 12);
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
setByte() - Set a DATALINK parameter.
- Using an ordinal parameter
**/
    public void Var023()
    {
      StringBuffer info = new StringBuffer(" -- call setByte against DATALINK parameter -- Changed October 2011");
        if(checkJdbc20())
        {
            if(checkLobSupport ())
            {
                try {
                    cs=connection_.prepareCall(sql);
                    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                         supportedFeatures_);
                    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                    cs.setByte(18, (byte) 12);
                    cs.execute ();
		    byte check = cs.getByte(18);

	                byte expected = 12;
	                assertCondition(check == expected, "Got "+check+" expected "+expected+ info.toString()); 
                }
                catch(Exception e){
                  failed (e, "Unexpected Exception "+info.toString());
                }
            }
        }
    }

/**
setByte() - Set a BIGINT parameter.
- Using an ordinal parameter
**/
    public void Var024()
    {
      StringBuffer info = new StringBuffer(" -- call setByte against BIGINT parameter -- Changed October 2011");
        if(checkJdbc20())
        {
            if(checkBigintSupport())
            {
                try
                {
                    cs=connection_.prepareCall(sql);
                    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                         supportedFeatures_);
                    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                    cs.setByte(22, (byte) 12);
		    cs.execute();
		    byte check = cs.getByte(22);
		    cs.close();
	                byte expected = 12;
	                assertCondition(check == expected, "Got "+check+" expected "+expected+ info.toString()); 
                
                }
                catch(Exception e)
                {
                  failed (e, "Unexpected Exception "+info.toString());
                }
            }
        }
    }

/**
setByte() - Set a BINARY parameter, should throw error 
- Using an ordinal parameter
**/
    public void Var025()
    {
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());

		cs.setByte (13, (byte) 12);
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
setByte() - Set a VARBINARY parameter should throw error
- Using an ordinal parameter
**/
    public void Var026()
    {
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
               
                cs.setByte (14, (byte) 12);
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
setByte() - Set more than one parameter.
- Using an ordinal parameter
**/
    public void Var027()
    {
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                byte first = (byte) 12;
		byte second= (byte) 31;
                cs.setByte(1, (byte) first);
                cs.setByte(1, (byte) second);
                cs.execute();
                byte check = cs.getByte(1);
                cs.close();
		assertCondition (check == 31);
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception");
            }
        }
    }

// Named Parameters

/**
setByte() - Set a SMALLINT parameter - false
- Using named parameter
**/
    public void Var028()
    {
        if(checkJdbc30())
        {
            try {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());

                cs.setByte("P_SMALLINT", (byte) 32);
                cs.execute();
		byte check = cs.getByte(1);
		assertCondition(check == 32);
            }
            catch(Exception e){
		failed (e, "Unexpected Exception");
	    }
	}    
    }

/**
setByte() - Set a SMALLINT parameter - true
- Using NAMED parameter
**/
    public void Var029()
    {
        if(checkJdbc30())
        {
            try {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());

                cs.setByte("P_SMALLINT", (byte) -3);
                cs.execute();
		byte check = cs.getByte(1);
		assertCondition(check == -3);
            }
            catch(Exception e){
		failed (e, "Unexpected Exception");
	    }
	}    
    }


/**
setByte() - Set a INTEGER parameter.
- Using NAMED parameter
**/
    public void Var030()
    {
      StringBuffer info = new StringBuffer(" -- call setByte against INTEGER parameter -- Changed October 2011");
        if(checkJdbc30())
        {
            try {

                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setByte("P_INTEGER", (byte) 12);
                cs.execute();
		byte check = cs.getByte(2);
                byte expected = 12;
                assertCondition(check == expected, "Got "+check+" expected "+expected+ info.toString()); 
            }
            catch(Exception e){
                
              failed (e, "Unexpected Exception "+info.toString());
	    }
	}
    }

/**
setByte() - Set a REAL parameter.
- Using NAMED parameter
**/
    public void Var031()
    {
      StringBuffer info = new StringBuffer(" -- call setByte against REAL parameter -- Changed October 2011");
        if(checkJdbc30())
        {
            try {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setByte("P_REAL", (byte) 12);
		cs.execute();
		byte check = cs.getByte(3);
                byte expected = 12;
                assertCondition(check == expected, "Got "+check+" expected "+expected+ info.toString()); 
            }
            catch(Exception e) {
              failed (e, "Unexpected Exception "+info.toString());
            }
        }
    }

/**
setByte() - Set a FLOAT parameter.
- Using NAMED parameter
**/
    public void Var032()
    {
      StringBuffer info = new StringBuffer(" -- call setByte against FLOAT parameter -- Changed October 2011");
        if(checkJdbc30())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setByte("P_FLOAT", (byte) 12);
		cs.execute();
		byte check = cs.getByte(4);
                byte expected = 12;
                assertCondition(check == expected, "Got "+check+" expected "+expected+ info.toString()); 
            }
            catch(Exception e)
            {
              failed (e, "Unexpected Exception "+info.toString());
            }
        }
    }

/**
setByte() - Set a DOUBLE parameter.
- Using NAMED parameter
**/
    public void Var033 ()
    {
      StringBuffer info = new StringBuffer(" -- call setByte against DOUBLE parameter -- Changed October 2011");
        if(checkJdbc30())
        {
            try {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setByte("P_DOUBLE", (byte) 23);
		cs.execute();
		byte check = cs.getByte(5);
                byte expected = 23;
                assertCondition(check == expected, "Got "+check+" expected "+expected+ info.toString()); 
            }
            catch(Exception e) {
              failed (e, "Unexpected Exception "+info.toString());
            }
        }
    }

/**
setByte() - Set a DECIMAL parameter.
- Using NAMED parameter
**/
    public void Var034 ()
    {
      StringBuffer info = new StringBuffer(" -- call setByte against DECIMAL parameter -- Changed October 2011");
        if(checkJdbc30())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setByte("P_DECIMAL_50",(byte) 23);
		cs.execute();
		byte check = cs.getByte(6);
                byte expected = 23;
                assertCondition(check == expected, "Got "+check+" expected "+expected+ info.toString()); 
            }
            catch(Exception e)
            {
              failed (e, "Unexpected Exception "+info.toString());
            }
        }
    }

/**
setByte() - Set a NUMERIC parameter
- Using NAMED parameter
**/
    public void Var035 ()
    {
      StringBuffer info = new StringBuffer(" -- call setByte against NUMERIC parameter -- Changed October 2011");
        if(checkJdbc30())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setByte("P_NUMERIC_50", (byte) 23);
		cs.execute();
		byte check = cs.getByte(8);
                byte expected = 23;
                assertCondition(check == expected, "Got "+check+" expected "+expected+ info.toString()); 
            }
            catch(Exception e)
            {
              failed (e, "Unexpected Exception "+info.toString());
            }
        }
    }

/**
setByte() - Set a CHAR(1) parameter.
- Using NAMED parameter
**/
    public void Var036 ()
    {
      StringBuffer info = new StringBuffer(" -- call setByte against CHAR1 parameter -- Changed October 2011");
        if(checkJdbc30())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setByte("P_CHAR_1", (byte) 1);
		cs.execute();
		byte check = cs.getByte(10);
                byte expected = 1;
                assertCondition(check == expected, "Got "+check+" expected "+expected+ info.toString()); 
            }
            catch(Exception e)
            {
              failed (e, "Unexpected Exception "+info.toString());
            }
        }
    }
/**
setByte() - Set a CHAR(50) parameter.
- Using NAMED parameter
**/
    public void Var037 ()
    {
      StringBuffer info = new StringBuffer(" -- call setByte against CHAR50 parameter -- Changed October 2011");
        if(checkJdbc30())
        {
            try{
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setByte("P_CHAR_50", (byte) 23);
		cs.execute();
		byte check = cs.getByte(11);
                byte expected = 23;
                assertCondition(check == expected, "Got "+check+" expected "+expected+ info.toString()); 
            }
            catch(Exception e){
              failed (e, "Unexpected Exception "+info.toString());
            }
        }
    }
/**
setByte() - Set a VARCHAR(50) parameter.
- Using NAMED parameter
**/
    public void Var038 ()
    {
      StringBuffer info = new StringBuffer(" -- call setByte against VARCHAR parameter -- Changed October 2011");
        if(checkJdbc30())
        {
            try{
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setByte("P_VARCHAR_50", (byte) 23);
		cs.execute();
		byte check = cs.getByte(12);
                byte expected = 23;
                assertCondition(check == expected, "Got "+check+" expected "+expected+ info.toString()); 
            }
            catch(Exception e){
              failed (e, "Unexpected Exception "+info.toString());
            }
        }
    }
    
/**
setByte() - Set a CLOB parameter.
- Using NAMED parameter
**/
    public void Var039 ()
    {
        if(checkJdbc30())
        {
            if(checkLobSupport ())
            {
                try{
                    cs=connection_.prepareCall(sql);
                    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_);
                    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                    cs.setByte("P_CLOB", (byte) 23);
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
setByte() - Set a BlOB parameter.
- Using an named parameter
**/
    public void Var040()
    {
        if(checkJdbc30())
        {
            try{
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setByte("P_BLOB", (byte) 23);
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
setByte() - Set a DATE parameter to a value that is not a valid date.  This should throw an exception. 
- Using an named parameter
**/
    public void Var041()
    {
        if(checkJdbc30())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setByte("P_DATE", (byte) 23);
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
setByte() - Set a TIME parameter to a value that is not a valid date.  This should throw an exception. 
- Using an named parameter
**/
    public void Var042()
    {
        if(checkJdbc30())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setByte("P_TIME", (byte) 23);
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
setByte() - Set a TIMESTAMP parameter to a value that is not valid.  This should throw an exception.
- Using an named parameter
**/
    public void Var043()
    {
        if(checkJdbc30())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setByte("P_TIMESTAMP", (byte) 32);
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
setByte() - Set a DATALINK parameter.
- Using an named parameter
**/
// @B2 Changes made because it is illegal to Execute() before you register your output parm.  This may have worked fine before because JDSetupProcedure.register() registers all parms, just not to the correct type we want.
    public void Var044()
    {
	if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) { 
	    if(checkJdbc30())
	    {
		if(checkLobSupport ())
		{
		    try {
			cs=connection_.prepareCall(sql);
			JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
							     supportedFeatures_);
			JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
			cs.setByte("P_DATALINK", (byte) 23);
			cs.registerOutParameter("P_DATALINK", Types.SMALLINT);
			cs.execute ();
                        if(isToolboxDriver())                  //@B1A
                        {                                                               //@B1A
                     //       cs.registerOutParameter("P_DATALINK", Types.SMALLINT);          //@B1A
                            byte b = cs.getByte("P_DATALINK");                          //@B1A
                            assertCondition( b == (byte) 23);                           //@B1A
                        }                                                               //@B1A
			else{
			    byte nativeb = cs.getByte("P_DATALINK");                          //@B1A
                            assertCondition( nativeb == (byte) 23);
                            //  failed ("Didn't throw SQLException");
			}		    
		    }
		    catch(Exception e){
			failed(e, "Unexpected Exception");                          //@B1A
		    }
		}
	    }
	} else {
	    notApplicable(); 
	} 
    }

/**
setByte() - Set a BIGINT parameter.
- Using an named parameter
**/
    public void Var045()
    {
        if(checkJdbc30())
        {
            if(checkBigintSupport())
            {
              StringBuffer info = new StringBuffer(" -- call setByte against BIGINT parameter -- Changed October 2011");
                try
                {
                    cs=connection_.prepareCall(sql);
                    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                         supportedFeatures_);
                    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                    cs.setByte("P_BIGINT", (byte) 23);
		    cs.execute();
		    byte check = cs.getByte(22);
	                byte expected = 23;
	                assertCondition(check == expected, "Got "+check+" expected "+expected+ info.toString()); 
                }
                catch(Exception e)
                {
                  failed (e, "Unexpected Exception "+info.toString());
                }
            }
        }
    }

/**
setByte() - Set a BINARY parameter, should throw error 
- Using an named parameter
**/
    public void Var046()
    {
        if(checkJdbc30())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());

		cs.setByte ("P_BINARY_20", (byte) 23);
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
setByte() - Set a VARBINARY parameter should throw error
- Using an named parameter
**/
    public void Var047()
    {
        if(checkJdbc30())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
               
                cs.setByte ("P_VARBINARY_20", (byte) 32);
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
     * setByte() - Set a BOOLEAN parameter. - Using an ordinal parameter
     **/
    public void Var048() {

        if (checkBooleanSupport()) {
          setTestSuccessful("setByte", 27, "0", "0",
              " -- call setByte against BOOLEAN parameter -- added January 2021");
        }
    }

    
    /**
     * setByte() - Set a BOOLEAN parameter. - Using a named parameter
     **/
    public void Var049() {

        if (checkBooleanSupport()) {
          setTestSuccessful("setByte", "P_BOOLEAN", "1", "1",
              " -- call setByte against BOOLEAN parameter -- added January 2021");
        }
    }

    
    /**
     * setByte() - Set a BOOLEAN parameter. - Using a named parameter
     **/
    public void Var050() {

        if (checkBooleanSupport()) {
          setTestSuccessful("setByte", "P_BOOLEAN", "0", "0",
              " -- call setByte against BOOLEAN parameter -- added January 2021");
        }
    }

}
