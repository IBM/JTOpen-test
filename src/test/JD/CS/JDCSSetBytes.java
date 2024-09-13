///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSSetBytes.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
//
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSSetBytes.java
//
// Classes:      JDCSSetBytes
//
////////////////////////////////////////////////////////////////////////
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import java.awt.TextArea;
import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.DataTruncation;
import java.sql.SQLException;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDSetupProcedure;

/**
Testcase JDCSSetBytes.  This tests the following
method of the JDBC CallableStatement class:

<ul>
<li>setBytes()
</ul>
**/
public class JDCSSetBytes
extends JDCSSetTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSSetBytes";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     JDCSTest.main(newArgs); 
   }



  

    byte[] test = new byte[] { (byte) 0x34, (byte) 0x45, (byte) 0x50,
                               (byte) 0x56, (byte) 0x67, (byte) 0x78,
                               (byte) 0x89, (byte) 0x9A, (byte) 0xAB,
                               (byte) 0xBC, (byte) 0xBB, (byte) 0xCB,
                               (byte) 0xDC, (byte) 0xCD, (byte) 0xCC,
                               (byte) 0xDD, (byte) 0xED, (byte) 0xDE,
                               (byte) 0xEE, (byte) 0xDD};

/**
Constructor.
**/
    public JDCSSetBytes (AS400 systemObject,
                          Hashtable namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDCSSetBytes",
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
Utility Function

returns the hex representation of an array of bytes
**/
    public static String dumpBytes(byte[] temp)
    {
       int length = temp.length;
       String s = "";
       for(int i = 0 ; i < length ; i++)
       {
	  String ns = Integer.toHexString(((int) temp[i])& 0xFF);
	  if(ns.length() == 1)
	  {
	      s += "0"+ ns;
	  }
 	  else
	  {
	      s += ns;
	  }
       }
       return s;
    }



/**
setBytes() - Set parameter PARAM_1 which doesn't exist.
- Using an ordinal parameter
**/
    public void Var001()
    {
        if(checkJdbc20())
        {

            try
            {
                cs.setBytes(0, test);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


/**
setBytes() - Should throw an exception when the callable statment is closed.
- Using an ordinal parameter
**/
    public void Var002()
    {
        if(checkJdbc20())
        {

            try
            {
                cs.close();
                cs.setBytes(1, test);
                failed("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setBytes() - Should work with a valid parameter name.
- Using an ordinal parameter
**/
    public void Var003 ()
    {
        if(checkJdbc20())
        {

            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBytes(14, test);
                cs.execute();
		byte[] v = cs.getBytes(14);
                cs.close();
                assertCondition(areEqual(v, test));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setBytes() - Should throw exception when the parameter is
not an input parameter.
- Using an ordinal parameter
**/
    public void Var004 ()
    {
        if(checkJdbc20())
        {

            try
            {

                CallableStatement cs1 = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + "( ?, ?, ?)");
                cs1.setBytes(2, test);
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
setBytes() - Verify that a data truncation warning is
posted when data is truncated.
- Using an ordinal parameter
**/
    public void Var005()
    {
        if(checkJdbc20())
        {

            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		byte[] long_arr = new byte[] {(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03,
		                          (byte) 0x04, (byte) 0x05, (byte) 0x05, (byte) 0x07,
		                          (byte) 0x08, (byte) 0x09, (byte) 0x0A, (byte) 0x0B,
		                          (byte) 0x0C, (byte) 0x0D, (byte) 0x0E, (byte) 0x0F,
		                          (byte) 0x10, (byte) 0x11, (byte) 0x12, (byte) 0x13,
		                          (byte) 0x14, (byte) 0x15, (byte) 0x16, (byte) 0x17};
                cs.setBytes(13,long_arr);
                failed ("Didn't throw SQLException");
            }
            catch(DataTruncation dt)
            {
                try{
                assertCondition ((dt.getIndex() == 13)
                        && (dt.getParameter() == true)
                        && (dt.getRead() == false)
                        && (dt.getDataSize() == 24)
                        && (dt.getTransferSize() == 20));

                cs.close();
                }
                catch (SQLException s)
                {
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
setBytes() - Set a SMALLINT parameter.
- Using an ordinal parameter
**/
    public void Var006()
    {
        if(checkJdbc20())
        {

            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBytes(1, test);
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
setBytes() - Set a INTEGER parameter.
- Using an ordinal parameter
**/
    public void Var007()
    {
        if(checkJdbc20())
        {
            try
            {

                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBytes(2, test);
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
setBytes() - Set a REAL parameter.
- Using an ordinal parameter
**/
    public void Var008()
    {
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBytes(3, test);
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
setBytes() - Set a FLOAT parameter.
- Using an ordinal parameter
**/
    public void Var009()
    {
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBytes(4, test);
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
setBytes() - Set a DOUBLE parameter.
- Using an ordinal parameter
**/
    public void Var010 ()
    {
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBytes(5, test);
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
setBytes() - Set a DECIMAL parameter.
- Using an ordinal parameter
**/
    public void Var011 ()
    {
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBytes(6, test);
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
setBytes() - Set a NUMERIC parameter
- Using an ordinal parameter
**/
    public void Var012 ()
    {
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBytes(8, test);
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
setBytes() - Set a CHAR(1) parameter.
- Using an ordinal parameter
**/
    public void Var013 ()
    {
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBytes(10, test);
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
setBytes() - Set a CHAR(50) parameter.
- Using an ordinal parameter
**/
    public void Var014 ()
    {
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBytes(11, test);
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
setBytes() - Set a VARCHAR(50) parameter.
- Using an ordinal parameter
**/
    public void Var015 ()
    {
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBytes(12, test);
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
setBytes() - Set a CLOB parameter.
- Using an ordinal parameter
**/
    public void Var016 ()
    {
        if(checkJdbc20())
        {
            if(checkLobSupport ())
            {
                try
                {
                    cs=connection_.prepareCall(sql);
                    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                         supportedFeatures_);
                    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                    cs.setBytes(20, test);
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
setBytes() - Set a BLOB parameter.
- Using an ordinal parameter
**/
    public void Var017()
    {
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBytes(19, test);
		cs.execute();
		byte[] v = cs.getBytes(19);
                assertCondition(areEqual(v, test));
            }
            catch(Exception e)
            {
		failed (e, "Unexpected Exception");
            }
        }
    }

/**
setBytes() - Set a DATE parameter to a value that is not a valid date.  This should throw an exception. 
- Using an ordinal parameter
**/
    public void Var018()
    {
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBytes(15, test);
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
setBytes() - Set a TIME parameter to a value that is not a valid date.  This should throw an exception. 
- Using an ordinal parameter
**/
    public void Var019()
    {
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBytes(16, test);
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
setBytes() - Set a TIMESTAMP parameter to a value that is not valid.  This should throw an exception.
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
                cs.setBytes(17, test);
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
setBytes() - Set a DATALINK parameter.
- Using an ordinal parameter
**/
    public void Var021()
    {
        if(checkJdbc20())
        {
            if(checkLobSupport ())
            {
                try
                {
                    cs=connection_.prepareCall(sql);
                    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                         supportedFeatures_);
                    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                    cs.setBytes(18, test);
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
setBytes() - Set a BIGINT parameter.
- Using an ordinal parameter
**/
    public void Var022()
    {
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
                    cs.setBytes(22, test);
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
setBytes() - Set a BINARY parameter.
- Using an ordinal parameter
**/
    public void Var023()
    {
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBytes (13, test);
		cs.execute();
		byte[] v = cs.getBytes(13);
                assertCondition(areEqual(v, test));
            }
            catch(Exception e)
            {
		failed (e, "Unexpected Exception");
            }
        }
    }

/**
setBytes() - Set a VARBINARY parameter.
- Using an ordinal parameter
**/
    public void Var024()
    {
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBytes (14, test);
                cs.execute();
		byte[] v = cs.getBytes(14);
                assertCondition(areEqual(v, test));
            }
            catch(Exception e)
            {
		failed (e, "Unexpected Exception");
            }
        }
    }
/**
setBytes() - Should set to SQL NULL when the value is null.
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
                cs.setBytes (13, null);
                cs.execute ();
                byte[] check = cs.getBytes (13);
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
setBytes() - Set parameter PARAM_1 which doesn't exist.
- Using a named parameter
**/
    public void Var026()
    {
        if(checkNamedParametersSupport())
        {
            try
            {

                cs.setBytes("PARAM_1", test);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


/**
setBytes() - Should throw an exception when the callable statment is closed.
- Using a named parameter
**/
    public void Var027()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.close();
                cs.setBytes("P_BINARY_20", test);
                failed("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setBytes() - Should work with a valid parameter name.
- Using a named parameter
**/
    public void Var028 ()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBytes("P_BINARY_20", test);
                cs.execute();
                byte[] v = cs.getBytes(13);
                cs.close();
                assertCondition(areEqual(v, test));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setBytes() - Should throw exception when the parameter is
not an input parameter.
- Using a named parameter
**/
    public void Var029 ()
    {
        if(checkNamedParametersSupport())
        {
            try
            {

                CallableStatement cs1 = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + "( ?, ?, ?)");
                cs1.setBytes("P2", test);
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
setBytes() - Verify that a data truncation warning is
posted when data is truncated.
- Using a named parameter
**/
    public void Var030 ()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		byte[] long_arr = new byte[] {(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03,
		                          (byte) 0x04, (byte) 0x05, (byte) 0x05, (byte) 0x07,
		                          (byte) 0x08, (byte) 0x09, (byte) 0x0A, (byte) 0x0B,
		                          (byte) 0x0C, (byte) 0x0D, (byte) 0x0E, (byte) 0x0F,
		                          (byte) 0x10, (byte) 0x11, (byte) 0x12, (byte) 0x13,
		                          (byte) 0x14, (byte) 0x15, (byte) 0x16, (byte) 0x17};
                cs.setBytes("P_BINARY_20",long_arr);

                failed ("Didn't throw SQLException");
            }
            catch(DataTruncation dt)
            {
                try{
                assertCondition ((dt.getIndex() == 13)
                        && (dt.getParameter() == true)
                        && (dt.getRead() == false)
                        && (dt.getDataSize() == 24)
                        && (dt.getTransferSize() == 20));
                cs.close();
                }
                catch(SQLException s)
                {
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
setBytes() - Set a SMALLINT parameter.
- Using a named parameter
**/
    public void Var031()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBytes ("P_SMALLINT", test);
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
setBytes() - Set a INTEGER parameter.
- Using a named parameter
**/
    public void Var032()
    {
        if(checkNamedParametersSupport())
        {
            try
            {

                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBytes ("P_INTEGER", test); 
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
setBytes() - Set a REAL parameter.
- Using a named parameter
**/
    public void Var033 ()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBytes ("P_REAL", test);
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
setBytes() - Set a FLOAT parameter.
- Using a named parameter
**/
    public void Var034()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBytes ("P_FLOAT", test);
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
setBytes() - Set a DOUBLE parameter.
- Using a named parameter
**/
    public void Var035 ()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBytes("P_DOUBLE", test);
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
setBytes() - Set a DECIMAL parameter.
- Using a named parameter
**/
    public void Var036 ()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBytes("P_DECIMAL_50", test);
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
setBytes() - Set a NUMERIC parameter
- Using a named parameter
**/
    public void Var037 ()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBytes("P_NUMERIC_50", test);
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
setBytes() - Set a CHAR(1) parameter.
- Using a named parameter
**/
    public void Var038 ()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBytes("P_CHAR_1", test);
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
setBytes() - Set a CHAR(50) parameter.
- Using a named parameter
**/
    public void Var039 ()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBytes("P_CHAR_50", test);
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
setBytes() - Set a VARCHAR(50) parameter.
- Using a named parameter
**/
    public void Var040 ()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBytes("P_VARCHAR_50",test);
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
setBytes() - Set a CLOB parameter.
- Using a named parameter
**/
    public void Var041 ()
    {
        if(checkNamedParametersSupport())
        {
            if(checkLobSupport ())
            {
                try
                {
                    cs=connection_.prepareCall(sql);
                    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                         supportedFeatures_);
                    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                    cs.setBytes("P_CLOB", test);
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
setBytes() - Set a BLOB parameter.
- Using a named parameter
**/
    public void Var042()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBytes ("P_BLOB", test);
		cs.execute();
		byte[] v = cs.getBytes(19);
                assertCondition(areEqual(v, test));
            }
            catch(Exception e)
            {
		failed (e, "Unexpected Exception");
            }
        }
    }

/**
setBytes() - Set a DATE parameter to a value that is not a valid date.  This should throw an exception. 
- Using a named parameter
**/
    public void Var043()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBytes ("P_DATE", test);
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
setBytes() - Set a TIME parameter to a value that is not a valid date.  This should throw an exception. 
- Using a named parameter
**/
    public void Var044()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBytes ("P_TIME", test);
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
setBytes() - Set a TIMESTAMP parameter to a value that is not valid.  This should throw an exception.
- Using a named parameter
**/
    public void Var045()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBytes ("P_TIMESTAMP", test);
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
setBytes() - Set a DATALINK parameter.
- Using a named parameter
**/
    public void Var046()
    {
        if(checkNamedParametersSupport())
        {
            if(checkLobSupport ())
            {
                try
                {
                    cs=connection_.prepareCall(sql);
                    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                         supportedFeatures_);
                    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                    cs.setBytes ("P_DATALINK", test);
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
setBytes() - Set a BIGINT parameter.
- Using a named parameter
**/
    public void Var047()
    {
        if(checkNamedParametersSupport())
        {
            if(checkBigintSupport())
            {
                try
                {
                    cs=connection_.prepareCall(sql);
                    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                         supportedFeatures_);
                    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                    cs.setBytes ("P_BIGINT", test);
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
setBytes() - Set a BINARY parameter.
- Using a named parameter
**/
    public void Var048()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBytes ("P_BINARY_20", test);
		cs.execute();
		byte[] v = cs.getBytes(13);
                assertCondition(areEqual(v, test));
            }
            catch(Exception e)
            {
		failed (e, "Unexpected Exception");
            }
        }
    }

/**
setBytes() - Set a VARBINARY parameter.
- Using a named parameter
**/
    public void Var049()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBytes ("P_VARBINARY_20", test);
		cs.execute();
		byte[] v = cs.getBytes(14);
                assertCondition(areEqual(v, test));
            }
            catch(Exception e)
            {
		failed (e, "Unexpected Exception");
            }
        }
    }

/**
setBytes() - Should set to SQL NULL when the value is null.
- Using a named parameter
**/
    public void Var050()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBytes("P_VARBINARY_20", null);
                cs.execute ();
                byte[] check = cs.getBytes (14);
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
setBytes() - Attempt to set a parameter twice.
- Using an ordinal parameter
**/
    public void Var051 ()
    {
        if(checkJdbc20())
        {
            try
            {
		cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());

                cs.setBytes (13, test);
		byte[] test2 = new byte[] { (byte) 0xFF, (byte) 0x45, (byte) 0x50,
		                           (byte) 0xFF, (byte) 0x67, (byte) 0x78,
		                           (byte) 0xFF, (byte) 0x9A, (byte) 0xAB,                           (byte) 0xFF};
		byte[] test2exp = new byte[] { (byte) 0xFF, (byte) 0x45, (byte) 0x50,
		                           (byte) 0xFF, (byte) 0x67, (byte) 0x78,
		                           (byte) 0xFF, (byte) 0x9A, (byte) 0xAB,
		                           (byte) 0xFF, (byte) 0x00, (byte) 0x00,
		                           (byte) 0x00, (byte) 0x00, (byte) 0x00,
		                           (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,};
                cs.setBytes (13, test2);
                cs.execute();
                byte[] v = cs.getBytes(13);
		cs.close();
                assertCondition(areEqual(v, test2exp),"v = " + dumpBytes(v) + " and test2 = "+ dumpBytes(test2exp));
	    }  
            catch (Exception e)
            {
                failed (e, "Unexpected Exception");
            }
	}
    }
/**
setBytes() - Attempt to set a parameter twice.
- Using a named parameter
**/
    public void Var052 ()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
		cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());

		cs.setBytes ("P_BINARY_20", test);
		byte[] test2 = new byte[] { (byte) 0xFF, (byte) 0x45, (byte) 0x50,
		                           (byte) 0xFF, (byte) 0x67, (byte) 0x78,
		                           (byte) 0xFF, (byte) 0x9A, (byte) 0xAB,
		                           (byte) 0xFF};
		byte[] test2exp = new byte[] { (byte) 0xFF, (byte) 0x45, (byte) 0x50,
		                           (byte) 0xFF, (byte) 0x67, (byte) 0x78,
		                           (byte) 0xFF, (byte) 0x9A, (byte) 0xAB,
		                           (byte) 0xFF, (byte) 0x00, (byte) 0x00,
		                           (byte) 0x00, (byte) 0x00, (byte) 0x00,
		                           (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,};
		cs.setBytes ("P_BINARY_20", test2);
                cs.execute();
                byte[] v = cs.getBytes(13);
		cs.close();
                assertCondition(areEqual(v, test2exp),"v = " + dumpBytes(v) + " and test2 = "+ dumpBytes(test2exp));
           }
            catch (Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setBytes() - Attempt to set a parameter twice.
- Using an ordinal parameter
**/
    public void Var053 ()
    {
        if(checkJdbc20())
        {
            try
            {
		cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());

                cs.setBytes (14, test);
		byte[] test2 = new byte[] { (byte) 0xFF, (byte) 0x45, (byte) 0x50,
		                           (byte) 0xFF, (byte) 0x67, (byte) 0x78,
		                           (byte) 0xFF, (byte) 0x9A, (byte) 0xAB,
		                           (byte) 0xFF};
                cs.setBytes (14, test2);
                cs.execute();
                byte[] v = cs.getBytes(14);
		cs.close();
                assertCondition(areEqual(v, test2),"v = " + dumpBytes(v) + " and test2 = "+ dumpBytes(test2));
	    }  
            catch (Exception e)
            {
                failed (e, "Unexpected Exception");
            }
	}
    }
/**
setBytes() - Attempt to set a parameter twice.
- Using a named parameter
**/
    public void Var054 ()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
		cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());

		cs.setBytes ("P_VARBINARY_20", test);
		byte[] test2 = new byte[] { (byte) 0xFF, (byte) 0x45, (byte) 0x50,
		                           (byte) 0xFF, (byte) 0x67, (byte) 0x78,
		                           (byte) 0xFF, (byte) 0x9A, (byte) 0xAB,(byte) 0xFF};
		cs.setBytes ("P_VARBINARY_20", test2);
                cs.execute();
                byte[] v = cs.getBytes(14);
		cs.close();
                assertCondition(areEqual(v, test2),"v = " + dumpBytes(v) + " and test2 = "+ dumpBytes(test2));
           }
            catch (Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }
    
    
    
    /**
     * setBytes() - Set a BOOLEAN parameter. - Using an ordinal parameter
     */
    public void Var055()
    {

        if (checkBooleanSupport())
        {
          
          setTestFailure("setBytes",27,  test, "Added 2021/01/07 to test boolean support");
        }

    }
    
    /**
     * setBytes() - Set a BOOLEAN parameter. - Using a named parameter
     */
    public void Var056()
    {

        if (checkBooleanSupport())
        {
          
          setTestFailure("setBytes","P_BOOLEAN",  test, "Added 2021/01/07 to test boolean support");
        }

    }

}
