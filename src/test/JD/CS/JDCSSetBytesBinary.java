///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSSetBytesBinary.java
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
// File Name:    JDCSSetBytesBinary.java
//
// Classes:      JDCSSetBytesBinary
//
////////////////////////////////////////////////////////////////////////
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import java.awt.TextArea;
import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDTestDriver;
import test.JDTestcase;


/**
Testcase JDCSSetBytesBinary.  This tests the following
method of the JDBC CallableStatement class:

<ul>
<li>setBytes() -> using BINARY and VARBINARY types 
</ul>
**/
public class JDCSSetBytesBinary
extends JDTestcase
{



    // Private data.
    private Connection          connection_;
    private CallableStatement   cs;
    
    byte[] test = new byte[] { (byte) 0x34, (byte) 0x45, (byte) 0x50,
                               (byte) 0x56, (byte) 0x67, (byte) 0x78,
                               (byte) 0x89, (byte) 0x9A, (byte) 0xAB,
                               (byte) 0xBC, (byte) 0xBB, (byte) 0xCB,
                               (byte) 0xDC, (byte) 0xCD, (byte) 0xCC,
                               (byte) 0xDD, (byte) 0xED, (byte) 0xDE,
                               (byte) 0xEE, (byte) 0xDD};

    String STP_BIN  = JDCSTest.COLLECTION+".STP_BINARY";
    String STP_VAR  = JDCSTest.COLLECTION+".STP_VARBINARY";
    String sql_bin  = "CALL " + STP_BIN + " (?, ?)";
    String sql_var  = "CALL " + STP_VAR + " (?, ?)";    

/**
Constructor.
**/
    public JDCSSetBytesBinary (AS400 systemObject,
                          Hashtable namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDCSSetBytesBinary",
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

	// reset vars after -lib parameter is processed.
	STP_BIN  = JDCSTest.COLLECTION+".STP_BINARY";
	STP_VAR  = JDCSTest.COLLECTION+".STP_VARBINARY";
	sql_bin  = "CALL " + STP_BIN + " (?, ?)";
	sql_var  = "CALL " + STP_VAR + " (?, ?)";    



        connection_ = testDriver_.getConnection (baseURL_
						 + ";errors=full", userId_, encryptedPassword_);

	if(getRelease() >= JDTestDriver.RELEASE_V5R3M0) { 
	    String sql =   "CREATE PROCEDURE " + STP_BIN
	      + " (IN P_BINARY_IN		BINARY(20),"
	      + "  OUT P_BINARY_OUT		BINARY(20))"
	      + "  LANGUAGE SQL SPECIFIC " + STP_BIN
	      + "  BEGIN"
	      + "    SET P_BINARY_OUT = P_BINARY_IN;"
	      + "  END";
	    Statement s = connection_.createStatement ();
	    s.executeUpdate (sql);
	    s.executeUpdate ("GRANT ALL PRIVILEGES ON PROCEDURE "
			     + STP_BIN + " TO PUBLIC");

	    sql =		"CREATE PROCEDURE " + STP_VAR
	      + " (IN P_VARBINARY_IN		VARBINARY(20),"
	      + "  OUT P_VARBINARY_OUT		VARBINARY(20))"
	      + "  LANGUAGE SQL SPECIFIC " + STP_VAR
	      + "  BEGIN"
	      + "    SET P_VARBINARY_OUT = P_VARBINARY_IN;"
	      + "  END";
	    s.executeUpdate (sql);
	    s.executeUpdate ("GRANT ALL PRIVILEGES ON PROCEDURE "
			     + STP_VAR + " TO PUBLIC");
	    s.close();
	}

    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
	Statement s = connection_.createStatement ();
	if(getRelease() >= JDTestDriver.RELEASE_V5R3M0) { 
	    s.executeUpdate("DROP PROCEDURE " + STP_BIN);
	    s.executeUpdate("DROP PROCEDURE " + STP_VAR);
	}
	s.close();	
	//cs.close();
        connection_.close ();
    }



/**
setBytes() - Should work with a valid parameter name.
- Using an ordinal parameter
**/
    public void Var001 ()
    {
        if(getRelease() >= JDTestDriver.RELEASE_V5R3M0)
        {

            try
            {
                cs=connection_.prepareCall(sql_bin);
		cs.setBytes(1, test);
		cs.registerOutParameter (2, Types.BINARY);
                cs.execute();
		byte[] v = cs.getBytes(2);
                cs.close();
                assertCondition(areEqual(v, test));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
	} else {
	    notApplicable(); 
	} 
    }

/**
setBytes() - Verify that a data truncation warning is
posted when data is truncated.
- Using an ordinal parameter
**/
    public void Var002()
    {
        if(getRelease() >= JDTestDriver.RELEASE_V5R3M0)
        {

            try
            {
                cs=connection_.prepareCall(sql_bin);
		byte[] long_arr = new byte[] {(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03,
		                          (byte) 0x04, (byte) 0x05, (byte) 0x05, (byte) 0x07,
		                          (byte) 0x08, (byte) 0x09, (byte) 0x0A, (byte) 0x0B,
		                          (byte) 0x0C, (byte) 0x0D, (byte) 0x0E, (byte) 0x0F,
		                          (byte) 0x10, (byte) 0x11, (byte) 0x12, (byte) 0x13,
		                          (byte) 0x14, (byte) 0x15, (byte) 0x16, (byte) 0x17};
                cs.setBytes(1,long_arr);
                failed ("Didn't throw SQLException");
            }
            catch(DataTruncation dt)
            {
                try{
                assertCondition ((dt.getIndex() == 1)
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
        } else {
	    notApplicable(); 
	} 

    }

/**
setBytes() - Should set to SQL NULL when the value is null.
- Using an ordinal parameter
**/
    public void Var003()
    {
        if(getRelease() >= JDTestDriver.RELEASE_V5R3M0)
        {
            try
            {
                cs=connection_.prepareCall(sql_bin);
                cs.setBytes (1, null);
		cs.registerOutParameter (2, Types.BINARY);
                cs.execute ();
                byte[] check = cs.getBytes (2);
                boolean wn = cs.wasNull ();
		cs.close();
                assertCondition ((check == null) && (wn == true));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        } else {
	    notApplicable(); 
	} 
    }

/**
setBytes() - Should work with a valid parameter name.
- Using a named parameter
**/
    public void Var004 ()
    {
        if(getRelease() >= JDTestDriver.RELEASE_V5R3M0)
	{

	    if(checkNamedParametersSupport())
	    {
		try
		{
		    cs=connection_.prepareCall(sql_bin);
		    cs.setBytes("P_BINARY_IN", test);
		    cs.registerOutParameter (2,Types.BINARY);
		    cs.execute();
		    byte[] v = cs.getBytes(2);
		    cs.close();
		    assertCondition(areEqual(v, test));
		}
		catch(Exception e)
		{
		    failed (e, "Unexpected Exception");
		}
	    }
	} else {
	    notApplicable(); 
	} 
    }

/**
setBytes() - Set a BINARY parameter.
- Using a named parameter
**/
    public void Var005 ()
    {
	if(checkJdbc30()) /* WILSONJO named parameters need jdbc 3.0 */
	{
	    if(getRelease() >= JDTestDriver.RELEASE_V5R3M0)
	    {

		try
		{
		    cs=connection_.prepareCall(sql_bin);
		    cs.setBytes("P_BINARY_IN", test);
		    cs.registerOutParameter (2, Types.BINARY);
		    cs.execute();
		    byte[] v = cs.getBytes(2);
		    cs.close();
		    assertCondition(areEqual(v, test));
		}
		catch(Exception e)
		{
		    failed (e, "Unexpected Exception");
		}
	    } else {
		notApplicable(); 
	    }
	} 
    }

/**
setBytes() - Verify that a data truncation warning is
posted when data is truncated.
- Using a named parameter
**/
    public void Var006()
    {
	if(checkJdbc30()) /* WILSONJO named parameters need jdbc 3.0 */
	{
	    if(getRelease() >= JDTestDriver.RELEASE_V5R3M0)
	    {

		try
		{
		    cs=connection_.prepareCall(sql_bin);
		    byte[] long_arr = new byte[] {(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03,
		    (byte) 0x04, (byte) 0x05, (byte) 0x05, (byte) 0x07,
		    (byte) 0x08, (byte) 0x09, (byte) 0x0A, (byte) 0x0B,
		    (byte) 0x0C, (byte) 0x0D, (byte) 0x0E, (byte) 0x0F,
		    (byte) 0x10, (byte) 0x11, (byte) 0x12, (byte) 0x13,
		    (byte) 0x14, (byte) 0x15, (byte) 0x16, (byte) 0x17};
		    cs.setBytes("P_BINARY_IN",long_arr);
		    failed ("Didn't throw SQLException");
		}
		catch(DataTruncation dt)
		{
		    try{
			assertCondition ((dt.getIndex() == 1)
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
	    } else {
		notApplicable(); 
	    }
	}
    }    

/**
setBytes() - Should set to SQL NULL when the value is null.
- Using a named parameter
**/
    public void Var007()
    {
	if(checkJdbc30()) /* WILSONJO named parameters need jdbc 3.0 */
	{
	    if(getRelease() >= JDTestDriver.RELEASE_V5R3M0)
	    {
		try
		{
		    cs=connection_.prepareCall(sql_bin);
		    cs.setBytes ("P_BINARY_IN", null);
		    cs.registerOutParameter (2, Types.BINARY);
		    cs.execute ();
		    byte[] check = cs.getBytes (2);
		    boolean wn = cs.wasNull ();
		    cs.close();
		    assertCondition ((check == null) && (wn == true));
		}
		catch(Exception e)
		{
		    failed (e, "Unexpected Exception");
		}
	    } else {
		notApplicable(); 
	    }
	}
    }

/**
setBytes() - Should work with a valid parameter name.
- Using an ordinal parameter
**/
    public void Var008 ()
    {
        if(getRelease() >= JDTestDriver.RELEASE_V5R3M0)
        {

            try
            {
                cs=connection_.prepareCall(sql_var);
		cs.setBytes(1, test);
		cs.registerOutParameter (2, Types.VARBINARY);
                cs.execute();
		byte[] v = cs.getBytes(2);
                cs.close();
                assertCondition(areEqual(v, test));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        } else {
	    notApplicable(); 
	} 
    }

/**
setBytes() - Verify that a data truncation warning is
posted when data is truncated.
- Using an ordinal parameter
**/
    public void Var009()
    {
        if(getRelease() >= JDTestDriver.RELEASE_V5R3M0)
        {

            try
            {
                cs=connection_.prepareCall(sql_var);
		byte[] long_arr = new byte[] {(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03,
		                          (byte) 0x04, (byte) 0x05, (byte) 0x05, (byte) 0x07,
		                          (byte) 0x08, (byte) 0x09, (byte) 0x0A, (byte) 0x0B,
		                          (byte) 0x0C, (byte) 0x0D, (byte) 0x0E, (byte) 0x0F,
		                          (byte) 0x10, (byte) 0x11, (byte) 0x12, (byte) 0x13,
		                          (byte) 0x14, (byte) 0x15, (byte) 0x16, (byte) 0x17};
                cs.setBytes(1,long_arr);
                failed ("Didn't throw SQLException");
            }
            catch(DataTruncation dt)
            {
                try{
                assertCondition ((dt.getIndex() == 1)
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
        } else {
	    notApplicable(); 
	} 
    }

/**
setBytes() - Should set to SQL NULL when the value is null.
- Using an ordinal parameter
**/
    public void Var010()
    {
        if(getRelease() >= JDTestDriver.RELEASE_V5R3M0)
        {
            try
            {
                cs=connection_.prepareCall(sql_var);
                cs.setBytes (1, null);
		cs.registerOutParameter (2, Types.VARBINARY);
                cs.execute ();
                byte[] check = cs.getBytes (2);
                boolean wn = cs.wasNull ();
		cs.close();
                assertCondition ((check == null) && (wn == true));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        } else {
	    notApplicable(); 
	} 
    }

/**
setBytes() - Should work with a valid parameter name.
- Using a named parameter
**/
    public void Var011 ()
    {
        if(getRelease() >= JDTestDriver.RELEASE_V5R3M0)
	{

	    if(checkNamedParametersSupport())
	    {
		try
		{
		    cs=connection_.prepareCall(sql_var);
		    cs.setBytes("P_VARBINARY_IN", test);
		    cs.registerOutParameter (2,Types.VARBINARY);
		    cs.execute();
		    byte[] v = cs.getBytes(2);
		    cs.close();
		    assertCondition(areEqual(v, test));
		}
		catch(Exception e)
		{
		    failed (e, "Unexpected Exception");
		}
	    }
	} else {
	    notApplicable(); 
	} 
    }

/**
setBytes() - Set a BINARY parameter.
- Using a named parameter
**/
    public void Var012 ()
    {
	if(checkJdbc30()) /* WILSONJO named parameters need jdbc 3.0 */
	{
	    if(getRelease() >= JDTestDriver.RELEASE_V5R3M0)
	    {

		try
		{
		    cs=connection_.prepareCall(sql_var);
		    cs.setBytes("P_VARBINARY_IN", test);
		    cs.registerOutParameter (2, Types.VARBINARY);
		    cs.execute();
		    byte[] v = cs.getBytes(2);
		    cs.close();
		    assertCondition(areEqual(v, test));
		}
		catch(Exception e)
		{
		    failed (e, "Unexpected Exception");
		}
	    } else {
		notApplicable(); 
	    }
	}
    }

/**
setBytes() - Verify that a data truncation warning is
posted when data is truncated.
- Using a named parameter
**/
    public void Var013 ()
    {
	if(checkJdbc30()) /* WILSONJO named parameters need jdbc 3.0 */
	{
	    if(getRelease() >= JDTestDriver.RELEASE_V5R3M0)
	    {

		try
		{
		    cs=connection_.prepareCall(sql_var);
		    byte[] long_arr = new byte[] {(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03,
		    (byte) 0x04, (byte) 0x05, (byte) 0x05, (byte) 0x07,
		    (byte) 0x08, (byte) 0x09, (byte) 0x0A, (byte) 0x0B,
		    (byte) 0x0C, (byte) 0x0D, (byte) 0x0E, (byte) 0x0F,
		    (byte) 0x10, (byte) 0x11, (byte) 0x12, (byte) 0x13,
		    (byte) 0x14, (byte) 0x15, (byte) 0x16, (byte) 0x17};
		    cs.setBytes("P_VARBINARY_IN",long_arr);
		    failed ("Didn't throw SQLException");
		}
		catch(DataTruncation dt)
		{
		    try{
			assertCondition ((dt.getIndex() == 1)
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
	    } else {
		notApplicable(); 
	    }
	}
    }    

/**
setBytes() - Should set to SQL NULL when the value is null.
- Using a named parameter
**/
    public void Var014 ()
    {
	if(checkJdbc30()) /* WILSONJO named parameters need jdbc 3.0 */
	{
	    if(getRelease() >= JDTestDriver.RELEASE_V5R3M0)
	    {
		try
		{
		    cs=connection_.prepareCall(sql_var);
		    cs.setBytes ("P_VARBINARY_IN", null);
		    cs.registerOutParameter (2, Types.VARBINARY);
		    cs.execute ();
		    byte[] check = cs.getBytes (2);
		    boolean wn = cs.wasNull ();
		    cs.close();
		    assertCondition ((check == null) && (wn == true));
		}
		catch(Exception e)
		{
		    failed (e, "Unexpected Exception");
		}
	    } else {
		notApplicable(); 

	    }
	}
    }
}
