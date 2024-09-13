///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSSetNString.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////
//
//
//
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSSetNString.java
//
// Classes:      JDCSSetNString
//
////////////////////////////////////////////////////////////////////////
//
//
// 
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDTestDriver;

import java.awt.TextArea;
import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.sql.DataTruncation;
import java.util.Hashtable;


/**
Testcase JDCSSetNString.  This tests the following
method of the JDBC CallableStatement class:

<ul>
<li>setNString()
</ul>
**/
public class JDCSSetNString
extends JDCSSetTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSSetNString";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCSTest.main(newArgs); 
   }






/**
Constructor.
**/
    public JDCSSetNString (AS400 systemObject,
                          Hashtable namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDCSSetNString",
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
setNString() - Set parameter PARAM_1 which doesn't exist.
- Using an ordinal parameter
**/
    public void Var001()
    {
        if(checkJdbc40())
        {

            try
            {

                JDReflectionUtil.callMethod_V(cs, "setNString", 0, "price");
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


/**
setNString() - Should throw an exception when the callable statment is closed.
- Using an ordinal parameter
**/
    public void Var002()
    {
        if(checkJdbc40())
        {

            try
            {
                cs.close();
                JDReflectionUtil.callMethod_V(cs, "setNString", 1, "test");
                failed("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setNString() - Should work with a valid parameter name.
- Using an ordinal parameter
**/
    public void Var003 ()
    {
        if(checkJdbc40())
        {

            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs, "setNString", 11, "Kim");
                cs.execute();
                String check=(String) JDReflectionUtil.callMethod_O(cs,"getNString",11);
                cs.close();
                assertCondition(check.equals("Kim                                               "));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setNString() - Should throw exception when the parameter is
not an input parameter.
- Using an ordinal parameter
**/
    public void Var004 ()
    {
        if(checkJdbc40())
        {

            try
            {

                CallableStatement cs1 = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + "( ?, ?, ?)");
                JDReflectionUtil.callMethod_V(cs,"setNString",2, "Charlie");
                cs1.close();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }

    }

/**
setNString() - Verify that a data truncation warning is
posted when data is truncated.
- Using an ordinal parameter
**/
    public void Var005()
    {
        if(checkJdbc40())
        {

            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs, "setNString", 11, "This string is a little bit longer than fifty characters.");
                failed ("Didn't throw SQLException");
            }
            catch(DataTruncation dt)
            {
		try{
		    int dataSize = 57;
		    assertCondition ((dt.getIndex() == 11)
				     && (dt.getParameter() == true)
				     && (dt.getRead() == false)
				     && (dt.getDataSize() == dataSize)
				     && (dt.getTransferSize() == 50),dt.getIndex()+" "+
				     dt.getParameter()+" "+dt.getRead()+" "+dt.getDataSize()+
				     " "+dt.getTransferSize());

		    cs.close();
		}
		catch (SQLException s){
		    failed (s, "Unexpected Exception");
		}
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
            }	
        }	
    }

/**
setNString() - Set a SMALLINT parameter.
- Using an ordinal parameter
**/
    public void Var006()
    {
        if(checkJdbc40())
        {

            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs, "setNString", 1, "Test");
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
setNString() - Set a INTEGER parameter.
- Using an ordinal parameter
**/
    public void Var007()
    {
        if(checkJdbc40())
        {
            try
            {

                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs, "setNString", 2, "Test");
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
setNString() - Set a REAL parameter.
- Using an ordinal parameter
**/
    public void Var008()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs, "setNString", 3, "Test");
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
setNString() - Set a FLOAT parameter.
- Using an ordinal parameter
**/
    public void Var009()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs, "setNString", 4, "test");
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
setNString() - Set a DOUBLE parameter.
- Using an ordinal parameter
**/
    public void Var010 ()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs, "setNString", 5, "Test");
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
setNString() - Set a DECIMAL parameter.
- Using an ordinal parameter
**/
    public void Var011 ()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs, "setNString", 6, "Test");
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
setNString() - Set a NUMERIC parameter
- Using an ordinal parameter
**/
    public void Var012 ()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs, "setNString", 8, "test");
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
setNString() - Set a CHAR(1) parameter.
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
                JDReflectionUtil.callMethod_V(cs, "setNString", 10, "K");
                cs.execute ();
                String check=(String) JDReflectionUtil.callMethod_O(cs,"getNString",10);
                cs.close();
                assertCondition(check.equals("K"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setNString() - Set a CHAR(50) parameter.
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
                JDReflectionUtil.callMethod_V(cs, "setNString", 11, "Kim");
                cs.execute ();
                String check=(String) JDReflectionUtil.callMethod_O(cs,"getNString",11);
                cs.close();
                assertCondition(check.equals("Kim                                               "));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setNString() - Set a VARCHAR(50) parameter.
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
                JDReflectionUtil.callMethod_V(cs, "setNString", 12, "Kimberly");
                cs.execute ();
                String check=(String) JDReflectionUtil.callMethod_O(cs,"getNString",12);
                cs.close();
                assertCondition(check.equals("Kimberly"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setNString() - Set a CLOB parameter.
- Using an ordinal parameter
**/
    public void Var016 ()
    {
        if(checkJdbc40())
        {
            if(checkLobSupport ())
            {
                try
                {
                    cs=connection_.prepareCall(sql);
                    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_);
                    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                    JDReflectionUtil.callMethod_V(cs, "setNString", 20, "Kim Roomer");
                    cs.execute();

                    String p = (String) JDReflectionUtil.callMethod_O(cs,"getNString",20);
                    assertCondition (p.equals("Kim Roomer"), "p = " + p + ", AND SHOULD equal Kim Roomer");
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }

/**
setNString() - Set a BLOB parameter.
- Using an ordinal parameter
**/
    public void Var017()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs, "setNString", 19, "Test");
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
setNString() - Set a DATE parameter to a value that is not a valid date.  This should throw an exception. 
- Using an ordinal parameter
**/
    public void Var018()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs, "setNString", 15, "Test");
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
setNString() - Set a TIME parameter to a value that is not a valid date.  This should throw an exception. 
- Using an ordinal parameter
**/
    public void Var019()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs, "setNString", 16, "test");
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
setNString() - Set a TIMESTAMP parameter to a value that is not valid.  This should throw an exception.
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
                JDReflectionUtil.callMethod_V(cs, "setNString", 17, "test");
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
setNString() - Set a DATALINK parameter.
- Using an ordinal parameter
**/
    public void Var021()
    {
        if(checkJdbc40())
        {
            if(checkLobSupport ())
            {
                try
                {
                    cs=connection_.prepareCall(sql);
                    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                         supportedFeatures_);
                    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                    JDReflectionUtil.callMethod_V(cs, "setNString", 18, "https://github.com");
                    cs.execute ();
                    String check=(String) JDReflectionUtil.callMethod_O(cs,"getNString",18);
                    cs.close();
                    assertCondition (check.equalsIgnoreCase("https://github.com"));
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }


/**
setNString() - Set a BIGINT parameter.
- Using an ordinal parameter
**/
    public void Var022()
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
                    JDReflectionUtil.callMethod_V(cs, "setNString", 22, "Test");
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
setNString() - Set a BINARY parameter.
- Using an ordinal parameter
**/
    public void Var023()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs,"setNString",13, "Symantec Cafe1234567");
                cs.execute();
                String check = (String) JDReflectionUtil.callMethod_O(cs,"getNString",13);
		if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		    String expected = "Symantec Cafe1234567";
		    assertCondition(expected.equals(check), "got "+check+" sb "+expected); 
		} else { 
		    failed("Didn't throw SQLException "+check);
		}

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
setNString() - Set a VARBINARY parameter.
- Using an ordinal parameter
**/
    public void Var024()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs,"setNString",14, "Symantec Cafe");
                cs.execute();
                String check = (String) JDReflectionUtil.callMethod_O(cs,"getNString",14);

		if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		    String expected = "Symantec Cafe";
		    assertCondition(expected.equals(check), "got "+check+" sb "+expected); 
		} else { 
		    failed("Didn't throw SQLException "+check);
		}

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
setNString() - Set more than one parameter.
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
                JDReflectionUtil.callMethod_V(cs, "setNString", 10, "K");
                JDReflectionUtil.callMethod_V(cs, "setNString", 12, "Roomer");
                cs.execute();
                String s1=(String) JDReflectionUtil.callMethod_O(cs,"getNString",10);
                String s2=(String) JDReflectionUtil.callMethod_O(cs,"getNString",12);
                cs.close();
                assertCondition((s1.equals("K")) && (s2.equals("Roomer")));
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception");
            }
        }
    }

/**
setNString() - Should set to SQL NULL when the value is null.
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
                JDReflectionUtil.callMethod_V(cs,"setNString",12, null);
                cs.execute ();
                String check = (String) JDReflectionUtil.callMethod_O(cs,"getNString",12);
                boolean wn = cs.wasNull ();
                assertCondition ((check == null) && (wn == true));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setNString() - Setting parameters with ordinals and with names in the same statement,
should throw an SQLException.
- Using an ordinal parameter
- Using a named parameter
**/
    public void Var027()
    {
        if(checkJdbc40())
        {
            try {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs, "setNString", "P_CHAR_1", "K");
                JDReflectionUtil.callMethod_V(cs,"setNString",12, null);
                cs.execute ();
                String check1 = (String) JDReflectionUtil.callMethod_O(cs,"getNString",10);
                String check2 = (String) JDReflectionUtil.callMethod_O(cs,"getNString",12);
                assertCondition ((check1.equals("K")) && (check2 == null));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setNString() - Set parameter PARAM_1 which doesn't exist.
- Using a named parameter
**/
    public void Var028()
    {
        if(checkJdbc40())
        {
            try
            {

                JDReflectionUtil.callMethod_V(cs, "setNString", "PARAM_1", "price");
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


/**
setNString() - Should throw an exception when the callable statment is closed.
- Using a named parameter
**/
    public void Var029()
    {
        if(checkJdbc40())
        {
            try
            {
                cs.close();
                JDReflectionUtil.callMethod_V(cs, "setNString", "P_SMALLINT", "test");
                failed("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setNString() - Should work with a valid parameter name.
- Using a named parameter
**/
    public void Var030 ()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs, "setNString", "P_CHAR_50", "Kim");
                cs.execute();
                String check= (String) JDReflectionUtil.callMethod_O(cs,"getNString",11);
                cs.close();
                assertCondition(check.equals("Kim                                               "));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setNString() - Should throw exception when the parameter is
not an input parameter.
- Using a named parameter
**/
    public void Var031 ()
    {
        if(checkJdbc40())
        {
            try
            {

                CallableStatement cs1 = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + "( ?, ?, ?)");
                JDReflectionUtil.callMethod_V(cs,"setNString","P2", "Charlie");
                cs1.close();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }

    }

/**
setNString() - Verify that a data truncation warning is
posted when data is truncated.
- Using a named parameter
**/
    public void Var032()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs,"setNString","P_CHAR_50", "This string is a little bit longer than fifty characters.");
                failed ("Didn't throw SQLException");
            }
            catch(DataTruncation dt)
            {
		try{
		    assertCondition ((dt.getIndex() == 11)
				     && (dt.getParameter() == true)
				     && (dt.getRead() == false)
				     && (dt.getDataSize() == 57)
				     && (dt.getTransferSize() == 50),dt.getIndex()+" "+
				     dt.getParameter()+" "+dt.getRead()+" "+dt.getDataSize()
				     +" "+dt.getTransferSize());
		    cs.close();
		}
		catch(SQLException s){
		    failed(s, "Unexpected Exception");
		}
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}	
    }

/**
setNString() - Set a SMALLINT parameter.
- Using a named parameter
**/
    public void Var033()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs,"setNString","P_SMALLINT", "Test");
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
setNString() - Set a INTEGER parameter.
- Using a named parameter
**/
    public void Var034()
    {
        if(checkJdbc40())
        {
            try
            {

                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs,"setNString","P_INTEGER", "Test"); 
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
setNString() - Set a REAL parameter.
- Using a named parameter
**/
    public void Var035 ()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs,"setNString","P_REAL", "Test");
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
setNString() - Set a FLOAT parameter.
- Using a named parameter
**/
    public void Var036()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs,"setNString","P_FLOAT", "Test");
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
setNString() - Set a DOUBLE parameter.
- Using a named parameter
**/
    public void Var037 ()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs, "setNString", "P_DOUBLE", "Test");
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
setNString() - Set a DECIMAL parameter.
- Using a named parameter
**/
    public void Var038 ()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs, "setNString", "P_DECIMAL_50", "Test");
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
setNString() - Set a NUMERIC parameter
- Using a named parameter
**/
    public void Var039 ()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs, "setNString", "P_NUMERIC_50", "Test");
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
setNString() - Set a CHAR(1) parameter.
- Using a named parameter
**/
    public void Var040 ()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs, "setNString", "P_CHAR_1", "K");
                cs.execute ();
                String check=(String) JDReflectionUtil.callMethod_O(cs,"getNString",10);
                cs.close();
                assertCondition(check.equals("K"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setNString() - Set a CHAR(50) parameter.
- Using a named parameter
**/
    public void Var041 ()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs, "setNString", "P_CHAR_50", "Kim");
                cs.execute ();
                String check=(String) JDReflectionUtil.callMethod_O(cs,"getNString",11);
                cs.close();
                assertCondition(check.equals("Kim                                               "));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setNString() - Set a VARCHAR(50) parameter.
- Using a named parameter
**/
    public void Var042 ()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs, "setNString", "P_VARCHAR_50", "Kimberly");
                cs.execute ();
                String check=(String) JDReflectionUtil.callMethod_O(cs,"getNString",12);
                cs.close();
                assertCondition(check.equals("Kimberly"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setNString() - Set a CLOB parameter.
- Using a named parameter
**/
    public void Var043 ()
    {
        if(checkJdbc40())
        {
            if(checkLobSupport ())
            {
                try
                {
                    cs=connection_.prepareCall(sql);
                    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                         supportedFeatures_);
                    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                    JDReflectionUtil.callMethod_V(cs, "setNString", "P_CLOB", "Kim Roomer");
                    cs.execute();
                    String p = (String) JDReflectionUtil.callMethod_O(cs,"getNString",20);
                    assertCondition (p.equals("Kim Roomer"), "p = " + p + ", AND SHOULD be Kim Roomer");
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }

/**
setNString() - Set a BLOB parameter.
- Using a named parameter
**/
    public void Var044()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs,"setNString","P_BLOB", "Test");
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
setNString() - Set a DATE parameter to a value that is not a valid date.  This should throw an exception. 
- Using a named parameter
**/
    public void Var045()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs,"setNString","P_DATE", "Test");
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
setNString() - Set a TIME parameter to a value that is not a valid date.  This should throw an exception. 
- Using a named parameter
**/
    public void Var046()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs,"setNString","P_TIME", "Test");
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
setNString() - Set a TIMESTAMP parameter to a value that is not valid.  This should throw an exception.
- Using a named parameter
**/
    public void Var047()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs,"setNString","P_TIMESTAMP", "Test");
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
setNString() - Set a DATALINK parameter.
- Using a named parameter
**/
    public void Var048()
    {
        if(checkJdbc40())
        {
            if(checkLobSupport ())
            {
                try
                {
                    cs=connection_.prepareCall(sql);
                    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                         supportedFeatures_);
                    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                    JDReflectionUtil.callMethod_V(cs,"setNString","P_DATALINK", "https://github.com");
                    cs.execute ();
                    String check=(String) JDReflectionUtil.callMethod_O(cs,"getNString",18);
                    cs.close();
                    assertCondition (check.equalsIgnoreCase("https://github.com"));
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }


/**
setNString() - Set a BIGINT parameter.
- Using a named parameter
**/
    public void Var049()
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
                    JDReflectionUtil.callMethod_V(cs,"setNString","P_BIGINT", "Test");
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
setNString() - Set a BINARY parameter.
- Using a named parameter
**/
    public void Var050()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs, "setNString", "P_BINARY_20", "Symantec Cafe1234567");
                cs.execute();
                String check = (String) JDReflectionUtil.callMethod_O(cs,"getNString",13);
		if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		    String expected = "Symantec Cafe1234567";
		    assertCondition(expected.equals(check), "got "+check+" sb "+expected); 
		} else { 

		    failed("Didn't throw SQLException "+check);
		}

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
setNString() - Set a VARBINARY parameter.
- Using a named parameter
**/
    public void Var051()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs, "setNString", "P_VARBINARY_20", "Symantec Cafe");
                cs.execute();
                String check = (String) JDReflectionUtil.callMethod_O(cs,"getNString",14);

		if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		    String expected = "Symantec Cafe";
		    assertCondition(expected.equals(check), "got "+check+" sb "+expected); 
		} else { 

		    failed("Didn't throw SQLException "+check);
		}

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
setNString() - Set more than one parameter.
- Using a named parameter
**/
    public void Var052()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs, "setNString", "P_CHAR_1", "K");
                JDReflectionUtil.callMethod_V(cs, "setNString", "P_VARCHAR_50", "Roomer");
                cs.execute();
                String s1=(String) JDReflectionUtil.callMethod_O(cs,"getNString",10);
                String s2=(String) JDReflectionUtil.callMethod_O(cs,"getNString",12);
                cs.close();
                assertCondition((s1.equals("K")) && (s2.equals("Roomer")));
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception");
            }
        }
    }

/**
setNString() - Should set to SQL NULL when the value is null.
- Using a named parameter
**/
    public void Var053()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs, "setNString", "P_VARCHAR_50", null);
                cs.execute ();
                String check = (String) JDReflectionUtil.callMethod_O(cs,"getNString",12);
                boolean wn = cs.wasNull ();
                cs.close();
                assertCondition ((check == null) && (wn == true));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
setNString() - Attempt to set the return value parameter.
- Using an ordinal parameter
**/
    public void Var054()
    {
        if(checkJdbc40())
        {
            if(checkReturnValueSupport())
            {
                try
                {
                    CallableStatement cs2 = connection_.prepareCall("?=CALL " + JDSetupProcedure.STP_CSRV);
                    JDReflectionUtil.callMethod_V(cs2, "setNString", 1, "444f");
                    cs2.close();
                    failed ("Didn't throw SQLException");
                }
                catch(Exception e)
                {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }

/**
setNString() - Attempt to set a mixed case name not in quotes.
- Using a named parameter
**/
    public void Var055 ()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs,"setNString","P_mIxEd", "smile");
                cs.execute();
                String check = (String) JDReflectionUtil.callMethod_O(cs,"getNString",24);
                cs.close();
                assertCondition (check.equals("smile"));
	    }
	    catch(Exception e){
                failed (e, "Unexpected Exception");
	    }
	}
    }

/**
setNString() - Attempt to set a mixed case name when it is in quotes.
- Using a named parameter
**/
    public void Var056 ()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs,"setNString","\"P_mIxEd\"", "smile");
                cs.execute();
                String check = (String) JDReflectionUtil.callMethod_O(cs,"getNString",24);
                cs.close();
                assertCondition (check.equals("smile"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setNString() - Attempt to set a lower case name when it is not in quotes.
- Using a named parameter
**/
    public void Var057 ()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs,"setNString","p_lower", "cool");
                cs.execute();
                String check = (String) JDReflectionUtil.callMethod_O(cs,"getNString",23);
                cs.close();
                assertCondition (check.equals("cool"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/** 
setNString() - Attempt to set a lower case name when it is in quotes.
- Using a named parameter
**/
    public void Var058 ()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs,"setNString","\"p_lower\"", "hello!");
                cs.execute();
                String check = (String) JDReflectionUtil.callMethod_O(cs,"getNString",23);
                cs.close();
                assertCondition (check.equals("hello!"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setNString() - Attempt to set an upper case name when it is in quotes.
- Using a named parameter
**/
    public void Var059 ()
    {
        if(checkJdbc40())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cs,"setNString","\"P_VARCHAR_50\"", "awesome");
                cs.execute();
                String check =(String) JDReflectionUtil.callMethod_O(cs,"getNString",12);
                assertCondition (check.equals("awesome"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setNString() - Attempt to set a parameter twice.
- Using an ordinal parameter
**/
    public void Var060 ()
    {
        if(checkJdbc40())
        {
            try
            {
                JDReflectionUtil.callMethod_V(cs,"setNString",10, "C");
                JDReflectionUtil.callMethod_V(cs,"setNString",10, "K");
                cs.execute();
                String check = (String) JDReflectionUtil.callMethod_O(cs,"getNString",10);
                assertCondition (check.equals("K"));
            }
            catch (Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }
/**
setNString() - Attempt to set a parameter twice.
- Using a named parameter
**/
    public void Var061 ()
    {
        if(checkJdbc40())
        {
            try
            {
                JDReflectionUtil.callMethod_V(cs,"setNString","P_CHAR_1", "C");
                JDReflectionUtil.callMethod_V(cs,"setNString","P_CHAR_1", "K");
                cs.execute();
                String check = (String) JDReflectionUtil.callMethod_O(cs,"getNString",10);
                assertCondition (check.equals("K"));
            }
            catch (Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setNString() -- Use various decimal formats.  This detects a bug in the native JDBC driver in the
SQLDataFactory.convertScientificNotation method
**/

    public void Var062 ()
    {
	if (checkJdbc40()) { 
	    String added = " -- Added by native driver 3/21/2006";
	    String message = "";
	    boolean passed = true;
	    int i = 0;

	    String[] inputExpected = {
	/* 0*/     "1.0",                    "1.000000000000000",
	/* 2*/    "1.0E1",                   "10.000000000000000",
	/* 4*/    "100.0E-1",                "10.000000000000000",
	/* 6*/    "1.0E-15",                  "0.000000000000001",
	/* 8*/    "1.0E15",    "1000000000000000.000000000000000",
	/*10*/    "1E1",                     "10.000000000000000",
	/*12*/    "100E-1",                  "10.000000000000000",
	/*14*/    "1E-15",                    "0.000000000000001",
	/*16*/    "1E15",      "1000000000000000.000000000000000",
	/*18*/    "0.035E-1",                 "0.003500000000000",
	/*20*/    "0.035E+1",                 "0.350000000000000",
	/*22*/    "0.035E+2",                 "3.500000000000000",
	/*24*/    "-1.0",                    "-1.000000000000000",
	/*26*/    "-1.0E1",                 "-10.000000000000000",
	/*28*/    "-100.0E-1",              "-10.000000000000000",
	/*30*/    "-1.0E-15",                "-0.000000000000001",
	/*32*/    "-1.0E15",  "-1000000000000000.000000000000000",
	/*34*/    "-1E1",                   "-10.000000000000000",
	/*36*/    "-100E-1",                "-10.000000000000000",
	/*38*/    "-1E-15",                  "-0.000000000000001",
	/*40*/    "-1E15",    "-1000000000000000.000000000000000",
	/*42*/    "-0.035E-1",               "-0.003500000000000",
	/*44*/    "-0.035E+1",               "-0.350000000000000",
	/*46*/    "-0.035E+2",               "-3.500000000000000",
	    };


        
        //@pdc just changed entries that are failing on jvm1.6 
        String[] inputExpectedScientific = {
                /* 0*/     "1.0",                    "1.000000000000000",
                /* 2*/    "1.0E1",                   "10.000000000000000",
                /* 4*/    "100.0E-1",                "10.000000000000000",
                /* 6*/    "1.0E-15",                  "1E-15",
                /* 8*/    "1.0E15",    "1000000000000000.000000000000000",
                /*10*/    "1E1",                     "10.000000000000000",
                /*12*/    "100E-1",                  "10.000000000000000",
                /*14*/    "1E-15",                    "1E-15",
                /*16*/    "1E15",      "1000000000000000.000000000000000",
                /*18*/    "0.035E-1",                 "0.003500000000000",
                /*20*/    "0.035E+1",                 "0.350000000000000",
                /*22*/    "0.035E+2",                 "3.500000000000000",
                /*24*/    "-1.0",                    "-1.000000000000000",
                /*26*/    "-1.0E1",                 "-10.000000000000000",
                /*28*/    "-100.0E-1",              "-10.000000000000000",
                /*30*/    "-1.0E-15",                "-1E-15",
                /*32*/    "-1.0E15",  "-1000000000000000.000000000000000",
                /*34*/    "-1E1",                   "-10.000000000000000",
                /*36*/    "-100E-1",                "-10.000000000000000",
                /*38*/    "-1E-15",                 "-1E-15",
                /*40*/    "-1E15",    "-1000000000000000.000000000000000",
                /*42*/    "-0.035E-1",               "-0.003500000000000",
                /*44*/    "-0.035E+1",               "-0.350000000000000",
                /*46*/    "-0.035E+2",               "-3.500000000000000",
                };
	    try { 

	//
	// Create the procedure
	//
		String procedureName = JDCSTest.COLLECTION+".DECIO"; 
		Statement stmt = connection_.createStatement();
		try {
		    stmt.executeUpdate("Drop procedure "+procedureName); 
		} catch (Exception e) {
		}
		stmt.executeUpdate("CREATE PROCEDURE "+procedureName+" ( IN INDEC DECIMAL(31,15), OUT OUTDEC DECIMAL(31,15)) LANGUAGE SQL BEGIN SET OUTDEC=INDEC; END");

		stmt.close();
		CallableStatement cs1 = connection_.prepareCall("CALL "+procedureName+"(?,?)");

	//
	// Loop and test the combinations
	//
		for (i = 0; i < inputExpected.length; i+=2) {
		    String input = inputExpected[i];
		    String expected = inputExpected[i+1];
		    if(isJdbc40()) {
			// Note: On V5R4 the native driver puts out all the decimal places 
			if (isToolboxDriver()) {
			    expected = inputExpectedScientific[i+1];
			}
		    }
		    cs1.registerOutParameter(2, Types.VARCHAR);
		    JDReflectionUtil.callMethod_V(cs1, "setNString", 1, input);
		    cs1.execute();
		    String output = (String) JDReflectionUtil.callMethod_O(cs1,"getNString",2);
		    if (! output.equals(expected)) {
			message+="i = "+i+
			  " input="+input+
			  " output="+output+
			  " expected="+expected+"\n";
			passed=false; 
		    }

		}

		assertCondition(passed, message+added); 
	    } catch (Exception e) {
		failed (e, "Unexpected Exception i="+i+" input[i]="+inputExpected[i]+added);
	    }

	}

    }

    public void Var063() { 
      notApplicable("Placeholder"); 
    }
    /**
     * setString() - Set a BOOLEAN parameter. - Using a named parameter
     **/
    public void Var064() {

      if (checkBooleanSupport()) {
        setTestSuccessful("setString", "P_BOOLEAN", "1", "1",
            " -- call setString against BOOLEAN parameter -- added January 2021");
      }
    }

    /**
     * setString() - Set a BOOLEAN parameter. - Using a named parameter
     **/
    public void Var065() {

      if (checkBooleanSupport()) {
        setTestSuccessful("setString", "P_BOOLEAN", "0", "0",
            " -- call setString against BOOLEAN parameter -- added January 2021");
      }
    }

    /**
     * setString() - Set a BOOLEAN parameter. - Using a named parameter
     **/
    public void Var066() {

      if (checkBooleanSupport()) {
        setTestSuccessful("setString", "P_BOOLEAN", "true", "1",
            " -- call setString against BOOLEAN parameter -- added January 2021");
      }
    }

    /**
     * setString() - Set a BOOLEAN parameter. - Using a named parameter
     **/
    public void Var067() {

      if (checkBooleanSupport()) {
        setTestSuccessful("setString", "P_BOOLEAN", "false", "0",
            " -- call setString against BOOLEAN parameter -- added January 2021");
      }
    }

    /**
     * setString() - Set a BOOLEAN parameter. - Using a named parameter
     **/
    public void Var068() {

      if (checkBooleanSupport()) {
        setTestSuccessful("setString", "P_BOOLEAN", "T", "1",
            " -- call setString against BOOLEAN parameter -- added January 2021");
      }
    }

    /**
     * setString() - Set a BOOLEAN parameter. - Using a named parameter
     **/
    public void Var069() {

      if (checkBooleanSupport()) {
        setTestSuccessful("setString", "P_BOOLEAN", "F", "0",
            " -- call setString against BOOLEAN parameter -- added January 2021");
      }
    }

    /**
     * setString() - Set a BOOLEAN parameter. - Using a named parameter
     **/
    public void Var070() {

      if (checkBooleanSupport()) {
        setTestSuccessful("setString", "P_BOOLEAN", "yes", "1",
            " -- call setString against BOOLEAN parameter -- added January 2021");
      }
    }

    /**
     * setString() - Set a BOOLEAN parameter. - Using a named parameter
     **/
    public void Var071() {

      if (checkBooleanSupport()) {
        setTestSuccessful("setString", "P_BOOLEAN", "no", "0",
            " -- call setString against BOOLEAN parameter -- added January 2021");
      }
    }

   
    /**
     * setString() - Set a BOOLEAN parameter. - Using a parameter number
     **/
    public void Var072() {

      if (checkBooleanSupport()) {
        setTestSuccessful("setString", 27, "1", "1",
            " -- call setString against BOOLEAN parameter -- added January 2021");
      }
    }

    /**
     * setString() - Set a BOOLEAN parameter. - Using a parameter number
     **/
    public void Var073() {

      if (checkBooleanSupport()) {
        setTestSuccessful("setString", 27, "0", "0",
            " -- call setString against BOOLEAN parameter -- added January 2021");
      }
    }

    /**
     * setString() - Set a BOOLEAN parameter. - Using a parameter number
     **/
    public void Var074() {

      if (checkBooleanSupport()) {
        setTestSuccessful("setString", 27, "true", "1",
            " -- call setString against BOOLEAN parameter -- added January 2021");
      }
    }

    /**
     * setString() - Set a BOOLEAN parameter. - Using a parameter number
     **/
    public void Var075() {

      if (checkBooleanSupport()) {
        setTestSuccessful("setString", 27, "false", "0",
            " -- call setString against BOOLEAN parameter -- added January 2021");
      }
    }

    /**
     * setString() - Set a BOOLEAN parameter. - Using a parameter number
     **/
    public void Var076() {

      if (checkBooleanSupport()) {
        setTestSuccessful("setString", 27, "T", "1",
            " -- call setString against BOOLEAN parameter -- added January 2021");
      }
    }

    /**
     * setString() - Set a BOOLEAN parameter. - Using a parameter number
     **/
    public void Var077() {

      if (checkBooleanSupport()) {
        setTestSuccessful("setString", 27, "F", "0",
            " -- call setString against BOOLEAN parameter -- added January 2021");
      }
    }

    /**
     * setString() - Set a BOOLEAN parameter. - Using a parameter number
     **/
    public void Var078() {

      if (checkBooleanSupport()) {
        setTestSuccessful("setString", 27, "yes", "1",
            " -- call setString against BOOLEAN parameter -- added January 2021");
      }
    }

    /**
     * setString() - Set a BOOLEAN parameter. - Using a parameter number
     **/
    public void Var079() {

      if (checkBooleanSupport()) {
        setTestSuccessful("setString", 27, "no", "0",
            " -- call setString against BOOLEAN parameter -- added January 2021");
      }
    }



}
