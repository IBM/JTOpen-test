///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSSetNCharacterStream.java
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
// File Name:    JDCSSetNCharacterStream.java
//
// Classes:      JDCSSetNCharacterStream
//
////////////////////////////////////////////////////////////////////////
//
//
//                 	        
// 
//
////////////////////////////////////////////////////////////////////////

package test;

import java.awt.TextArea;
import java.io.FileOutputStream;
import java.io.StringReader;

import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;


/**
Testcase JDCSSetNCharacterStream.  This tests the following
method of the JDBC CallableStatement class:


     SetNCharacterStream()

**/
public class JDCSSetNCharacterStream
extends JDCSSetTestcase
{
   private static int LARGE_CLOB_SIZE = 20000000; 


   



/**
Constructor.
**/
    public JDCSSetNCharacterStream (AS400 systemObject,
                          Hashtable namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDCSSetNCharacterStream",
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
      super.setup();     }

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
Compares a Clob with a String.
**/
    private boolean compare (Clob i, String b)
        throws SQLException
    {
        return i.getSubString (1, (int) i.length ()).equals (b);            
    }


/**
SetNCharacterStream() - Set parameter PARAM_1 which doesn't exist.
- Using an ordinal parameter
**/
    public void Var001()
    {
        if(checkJdbc40())
        {
            try {
		JDReflectionUtil.callMethod_V(cs, "setNCharacterStream", 0, new StringReader( "Test Clob"), (long) 9);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e){
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
SetNCharacterStream() - Set parameter PARAM_1 which doesn't exist.
- Using an ordinal parameter
**/
    public void Var002()
    {
        if(checkJdbc40())
        {
	    try{
		JDReflectionUtil.callMethod_V(cs, "setNCharacterStream", -1, new StringReader("Test Clob"), 9L );
                failed ("Didn't throw SQLException");
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
SetNCharacterStream() - Set parameter PARAM_1 which is too big.
- Using an ordinal parameter
**/
    public void Var003()
    {
        if(checkJdbc40())
        {
	    try {
                JDReflectionUtil.callMethod_V(cs, "setNCharacterStream", 35, new StringReader("Test Clob"),9L);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
SetNCharacterStream() - Should throw an exception when the callable statment is closed.
- Using an ordinal parameter
**/
    public void Var004()
    {
        if(checkJdbc40())
        {
            try
            {
		cs.close();
                JDReflectionUtil.callMethod_V(cs, "setNCharacterStream", 1, new StringReader("Test Clob"),9L);
                failed("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
SetNCharacterStream() - Should work with a valid parameter name.
- Using an ordinal parameter
**/
    public void Var005 ()
    {
        if(checkJdbc40()) {

            try {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		 
		JDReflectionUtil.callMethod_V(cs, "setNCharacterStream", 20, new StringReader("Test Clob"),9L);
                cs.execute();
                Clob check = (Clob) JDReflectionUtil.callMethod_O(cs, "getNClob", 20);
                cs.close();
                assertCondition(compare (check, "Test Clob"));
            }
            catch(Exception e){
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
SetNCharacterStream() - Should work with a valid parameter name.
- Using an ordinal parameter
**/
    public void Var006 ()
    {
        if(checkJdbc40())
        {

            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                Class[] argClasses = new Class[3]; 
                argClasses[0] = Integer.TYPE;
                argClasses[1] = Class.forName("java.io.Reader"); 
                argClasses[2] = Long.TYPE; 
                Object[] argValues = new Object[3];
                argValues[0] = new Integer(20); 
                argValues[1] = null; 
                argValues[2] = new Long(0); 
                JDReflectionUtil.callMethod_V(cs, "setNCharacterStream", argClasses, argValues);
                cs.execute();
                Clob check = (Clob) JDReflectionUtil.callMethod_O(cs, "getNClob", 20);
		boolean wn = cs.wasNull();
                assertCondition ((check == null) && (wn == true));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
SetNCharacterStream() - Should throw exception when the parameter is
not an input parameter.
- Using an ordinal parameter
**/
    public void Var007 ()
    {
        if(checkJdbc40())
        {
            try {
                CallableStatement cs1 = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + "( ?, ?, ?)");

		JDReflectionUtil.callMethod_V(cs1, "setNCharacterStream", 2, new StringReader("Test Clob"),9L);
                cs1.close();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }

    }

/**
SetNCharacterStream() - Set a SMALLINT parameter.
- Using an ordinal parameter
**/
    public void Var008()
    {
        if(checkJdbc40())
        {
            try {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());

		JDReflectionUtil.callMethod_V(cs, "setNCharacterStream", 1, new StringReader("Test Clob"),9L);
                cs.execute();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e){
                try{
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                    cs.close();
                }
                catch (SQLException s) {
                    failed (s, "Unexpected Exception");
                }
	    }
	}
    }

/**
SetNCharacterStream() - Set a INTEGER parameter.
- Using an ordinal parameter
**/
    public void Var009()
    {
        if(checkJdbc40())
        {
            try {

                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());

		JDReflectionUtil.callMethod_V(cs, "setNCharacterStream", 2, new StringReader("Test Clob"),9L);
                cs.execute();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e){
                try{
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                    cs.close();
                }
                catch (SQLException s) {
                    failed (s, "Unexpected Exception");
                }
	    }
	}
    }

/**
SetNCharacterStream() - Set a REAL parameter.
- Using an ordinal parameter
**/
    public void Var010()
    {
        if(checkJdbc40())
        {
            try {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		
		JDReflectionUtil.callMethod_V(cs, "setNCharacterStream", 3, new StringReader("Test Clob"),9L);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e) {
                try{
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                    cs.close();
                }
                catch (SQLException s) {
                    failed (s, "Unexpected Exception");
                }
            }
        }
    }

/**
SetNCharacterStream() - Set a FLOAT parameter.
- Using an ordinal parameter
**/
    public void Var011()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		
                JDReflectionUtil.callMethod_V(cs, "setNCharacterStream", 4, new StringReader("Test Clob"),9L);
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
SetNCharacterStream() - Set a DOUBLE parameter.
- Using an ordinal parameter
**/
    public void Var012 ()
    {
        if(checkJdbc40())
        {
            try {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		
                JDReflectionUtil.callMethod_V(cs, "setNCharacterStream", 5, new StringReader("Test Clob"),9L);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e) {
                try{
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                    cs.close();
                }
                catch (SQLException s) {
                    failed (s, "Unexpected Exception");
                }
            }
        }
    }

/**
SetNCharacterStream() - Set a DECIMAL parameter.
- Using an ordinal parameter
**/
    public void Var013 ()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		
                JDReflectionUtil.callMethod_V(cs, "setNCharacterStream", 6, new StringReader("Test Clob"),9L);
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
SetNCharacterStream() - Set a NUMERIC parameter
- Using an ordinal parameter
**/
    public void Var014 ()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		
                JDReflectionUtil.callMethod_V(cs, "setNCharacterStream", 8, new StringReader("Test Clob"),9L);
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
SetNCharacterStream() - Set a CHAR(1) parameter.
- Using an ordinal parameter
**/
    public void Var015 ()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs, "setNCharacterStream", 10, new StringReader("Test Clob"),9L);
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
                }            }
	}
    }
/**
SetNCharacterStream() - Set a CHAR(50) parameter.
- Using an ordinal parameter
**/
    public void Var016 ()
    {
        if(checkJdbc40())
        {
            try{
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());

		JDReflectionUtil.callMethod_V(cs, "setNCharacterStream", 11, new StringReader("Test Clob"),9L);
		cs.execute();
		Clob check = (Clob) JDReflectionUtil.callMethod_O(cs, "getNClob", 11);
		if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		    String expected = "Test Clob                                         ";
		    assertCondition(expected.equals(check.toString()), "expected = '"+expected+"' got '"+check+"'"); 
		} else { 
		    failed("Didn't Throw SQLException check="+check);
		}
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
/**
SetNCharacterStream() - Set a VARCHAR(50) parameter.
- Using an ordinal parameter
**/
    public void Var017 ()
    {
        if(checkJdbc40())
        {
            try{
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		
                JDReflectionUtil.callMethod_V(cs, "setNCharacterStream", 12, new StringReader("Test Clob"),9L);
		cs.execute();
		Clob check = (Clob) JDReflectionUtil.callMethod_O(cs, "getNClob", 12);
		if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		    String expected = "Test Clob"; 
		    assertCondition(expected.equals(check.toString()), "expected = '"+expected+"' got '"+check+"'"); 
		} else { 

		    failed("Didn't Throw SQLException check="+check);
		}
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
    
/**
SetNCharacterStream() - Set a CLOB parameter.
- Using an ordinal parameter
**/
    public void Var018 ()
    {
        if(checkJdbc40())
        {
            if(checkLobSupport ())
            {
                try{
		    cs=connection_.prepareCall(sql);
                    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_);
                    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		    
                    JDReflectionUtil.callMethod_V(cs, "setNCharacterStream", 20, new StringReader("Test Clob Test"), 14L);
		    cs.execute();
		    Clob check=(Clob) JDReflectionUtil.callMethod_O(cs, "getNClob", 20);
		    cs.close();
		    assertCondition (compare (check, "Test Clob Test"));
		}
		catch(Exception e){
		   failed (e, "Unexpected Exception");
		}               
	    }	
	}
    }
    

/**
SetNCharacterStream() - Set a Clob parameter.
- Using an ordinal parameter
**/
    public void Var019()
    {
        if(checkJdbc40())
        {
            try{
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		
                JDReflectionUtil.callMethod_V(cs, "setNCharacterStream", 19, new StringReader("Test Clob"),9L);
		failed("Didn't throw SQLException");
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
SetNCharacterStream() - Set a DATE parameter to a value that is not a valid date.  This should throw an exception. 
- Using an ordinal parameter
**/
    public void Var020()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());

		JDReflectionUtil.callMethod_V(cs, "setNCharacterStream", 15,  new StringReader( "Test Clob"),9L);
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
SetNCharacterStream() - Set a TIME parameter to a value that is not a valid date.  This should throw an exception. 
- Using an ordinal parameter
**/
    public void Var021()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		
                JDReflectionUtil.callMethod_V(cs, "setNCharacterStream", 16,  new StringReader( "Test Clob"),9L);
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
SetNCharacterStream() - Set a TIMESTAMP parameter to a value that is not valid.  This should throw an exception.
- Using an ordinal parameter
**/
    public void Var022()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		
                JDReflectionUtil.callMethod_V(cs, "setNCharacterStream", 17,  new StringReader( "Test Clob"),9L);
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
SetNCharacterStream() - Set a DATALINK parameter.
- Using an ordinal parameter
**/
    public void Var023()
    {
        if(checkJdbc40())
        {
            if(checkLobSupport ())
            {
                try {
                    cs=connection_.prepareCall(sql);
                    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                         supportedFeatures_);
                    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		    
		    JDReflectionUtil.callMethod_V(cs, "setNCharacterStream", 18, new StringReader( "Test Clob"),9L);
                    cs.execute ();
		    Clob check = (Clob) JDReflectionUtil.callMethod_O(cs, "getNClob", 18);
		    assertCondition (compare (check, "Test Clob"));
                }
                catch(Exception e){
		    try{
			assertExceptionIsInstanceOf(e, "java.sql.SQLException");
			cs.close();
		    }
		    catch(SQLException s){
			failed (s, "Unexpected Exception");

		    }
		}
	    }
	}
    }

/**
SetNCharacterStream() - Set a BIGINT parameter.
- Using an ordinal parameter
**/
    public void Var024()
    {
        if(checkJdbc40())
        {
            if(checkBigintSupport())
            {
                try
                {
                    cs=connection_.prepareCall(sql);
                    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                         supportedFeatures_);
                    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		    
                    JDReflectionUtil.callMethod_V(cs, "setNCharacterStream", 22,  new StringReader( "Test Clob"),9L);
                    failed ("Didn't throw SQLException");
                }
                catch(Exception e)
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
SetNCharacterStream() - Set a BINARY parameter, should throw error 
- Using an ordinal parameter
**/
    public void Var025()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());

		
		JDReflectionUtil.callMethod_V(cs, "setNCharacterStream", 13,  new StringReader( "Test Clob"),9L);
                cs.execute();
		Clob check = (Clob) JDReflectionUtil.callMethod_O(cs, "getNClob", 13);
		String expected = "Test Clob\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000"; 
		assertCondition (compare (check,expected), "got '"+check+"' expected '"+expected+"'");
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
SetNCharacterStream() - Set a VARBINARY parameter should throw error
- Using an ordinal parameter
**/
    public void Var026()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		
                JDReflectionUtil.callMethod_V(cs, "setNCharacterStream", 14,  new StringReader( "Test Clob"),9L);
                cs.execute();
		Clob check = (Clob) JDReflectionUtil.callMethod_O(cs, "getNClob", 14);
		assertCondition (compare (check,"Test Clob"));

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
SetNCharacterStream() - Set more than one parameter.
- Using an ordinal parameter
**/
    public void Var027()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		// String b = " clob test";
		// String c = "new clob test";
		JDReflectionUtil.callMethod_V(cs, "setNCharacterStream", 20,  new StringReader( " clob test"),10L);
                JDReflectionUtil.callMethod_V(cs, "setNCharacterStream", 20,  new StringReader( "new clob test"),13L);
                cs.execute();
                Clob check=(Clob) JDReflectionUtil.callMethod_O(cs, "getNClob", 20);
                cs.close();
                assertCondition (compare (check, "new clob test"));
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception");
            }
        }
    }

/**
SetNCharacterStream() - Should set to SQL NULL when the value is null.
- Using an ordinal parameter
**/
    public void Var028()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                
                Class[] argClasses = new Class[3]; 
                argClasses[0] = Integer.TYPE;
                argClasses[1] = Class.forName("java.io.Reader"); 
                argClasses[2] = Long.TYPE; 
                Object[] argValues = new Object[3];
                argValues[0] = new Integer(20); 
                argValues[1] = null; 
                argValues[2] = new Long(0); 
                
                JDReflectionUtil.callMethod_V(cs, "setNCharacterStream", argClasses, argValues);
                cs.execute ();
                Clob check = (Clob) JDReflectionUtil.callMethod_O(cs,"getNClob", 20);
                assertCondition ((check == null));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
	}
    }
    
    /**
   SetNCharacterStream() - Using a LARGE CLOB
   **/
  public void Var029() {
    String baseProcName = "JDCSSC029";
    String procName = JDCSTest.COLLECTION + "." + baseProcName;

    if (checkJdbc40()) {
      if (isToolboxDriver()) {
        notApplicable("TOOLBOX can't handle large blobs on 32-bit platforms");
      } else {

        String sql1 = "";
        try {
          Statement stmt = connection_.createStatement();

          try {
            stmt.executeUpdate("drop procedure " + procName);
          } catch (Exception e) {
          }

          StringBuffer sb = new StringBuffer();
          for (int i = 0; i < LARGE_CLOB_SIZE; i++) {
            sb.append('A');

          }

          sql1 = "CREATE PROCEDURE " + procName + "(IN B CLOB("
              + LARGE_CLOB_SIZE + "), "
              + " OUT I INT, OUT C CHAR(1)) LANGUAGE SQL " + "SPECIFIC "
              + baseProcName + " XXXX1: BEGIN " + "SET I = LENGTH(B); "
              + "SET C = SUBSTRING(B, 1,1); " + "END XXXX1";

          stmt.executeUpdate(sql1);
          sql1 = "{call " + procName + " (?,?,?)}";
          CallableStatement cstmt = connection_.prepareCall(sql1);
          JDReflectionUtil.callMethod_V(cstmt, "setNCharacterStream", 1, new StringReader(sb.toString()), (long) sb.length());
          cstmt.registerOutParameter(2, java.sql.Types.INTEGER);
          cstmt.registerOutParameter(3, java.sql.Types.CHAR);
          cstmt.execute();
          int answer = cstmt.getInt(2);
          String s = cstmt.getString(3);
          assertCondition((answer == LARGE_CLOB_SIZE) && (s.equals("A")),
              "Error answer is " + answer + " sb " + LARGE_CLOB_SIZE + " s="
                  + s + " sb A  -- Added by native driver 12/14/2005");
        } catch (Exception e) {
          failed(e, "Unexpected Exception -- added by native driver 12/14/2005");
        }
      }
    }
  }
  
  
  /**
   * setNCharacterStream() - Set a BOOLEAN parameter. - Using an ordinal parameter
   */
  public void Var030() {

    if(checkJdbc40())
      if (checkBooleanSupport()) {

      setTestFailure("setNCharacterStream", 27,   new StringReader( "Test Clob"),
          "Added 2021/01/07 to test boolean support");
    }

  }

  /**
   * setNCharacterStream() - Set a BOOLEAN parameter. - Using a named parameter
   */
  public void Var031() {

    if(checkJdbc40())
      if (checkBooleanSupport()) {

      setTestFailure("setNCharacterStream", "P_BOOLEAN",   new StringReader( "Test Clob"),
          "Added 2021/01/07 to test boolean support");
    }

  }

  
  /**
   * setNCharacterStream() - Set a BOOLEAN parameter. - Using an ordinal parameter
   */
  public void Var032() {

    if(checkJdbc40())
      if (checkBooleanSupport()) {

      setTestFailure("setNCharacterStream", 27,   new StringReader( "Test Clob"),  9,
          "Added 2021/01/07 to test boolean support");
    }

  }

  /**
   * setNCharacterStream() - Set a BOOLEAN parameter. - Using a named parameter
   */
  public void Var033() {

    if(checkJdbc40())
      if (checkBooleanSupport()) {

      setTestFailure("setNCharacterStream", "P_BOOLEAN",   new StringReader( "Test Clob"), 9,
          "Added 2021/01/07 to test boolean support");
    }

  }
}
