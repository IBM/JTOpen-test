///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPMDGetParameterCount.java
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
// File Name:    JDPMDGetParameterCount.java
//
// Classes:      JDPMDGetParameterCount
//
////////////////////////////////////////////////////////////////////////
//
//
//
//
////////////////////////////////////////////////////////////////////////

package test.JD.PMD;

import com.ibm.as400.access.AS400;

import test.JDPMDTest;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;

import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.Hashtable;


/**
Testcase JDPSetInt.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setInt()
</ul>
**/
public class JDPMDGetParameterCount
extends JDTestcase
{

    // Private data.
    private Connection          connection_;
    private PreparedStatement   ps;
    private ParameterMetaData   pmd;


/**
Constructor.
**/
    public JDPMDGetParameterCount (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDPMDGetParameterCount",
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
        String url = baseURL_
            
            
            + ";data truncation=true";
        connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);

    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
        connection_.close ();
    }



/**
getParameterCount() - Should throw exception if the statement gets closed.
**/
    public void Var001()
    {
	if(checkJdbc30()) /* WILSONJO parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		ps.close();
		int count = pmd.getParameterCount();

		failed ("Didn't throw SQLException "+count);
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



/**
getParameterCount() - Should work for a statement with no parameters.
**/
    public void Var002()
    {
	if(checkJdbc30()) /* WILSONJO parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.ZEROPARMS_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int count = pmd.getParameterCount();
		ps.close();
		assertCondition(count == 0);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterCount() - Should work for a statement after the statement has been 
executed.
**/
    public void Var003()
    {
	if(checkJdbc30()) /* WILSONJO parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.ZEROPARMS_PREPSTMT, connection_,supportedFeatures_);
		ResultSet rs = ps.executeQuery();
		pmd = ps.getParameterMetaData();
		int count = pmd.getParameterCount();
		ps.close();
		assertCondition(count == 0, "count is "+count+" sb 0 rs="+rs);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }



/**
getParameterCount() - Should work for an insert statement that has parameters.
**/
    public void Var004()
    {
	if(checkJdbc30()) /* WILSONJO parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int count = pmd.getParameterCount();
		ps.close();
		assertCondition(count == JDPMDTest.INSERT_PARM_COUNT);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }



/**
getParameterCount() - Should work for a query statement that has parameters.
**/
    public void Var005()
    {
	if(checkJdbc30()) /* WILSONJO parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int count = pmd.getParameterCount();
		ps.close();
		assertCondition(count == JDPMDTest.SELECT_PARM_COUNT);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }



/**
getParameterCount() - Should work for a stored procedure call that has parameters.
**/
    public void Var006()
    {
	if(checkJdbc30()) 
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int count = pmd.getParameterCount();
		ps.close();
		assertCondition(count == JDPMDTest.CALL_PARM_COUNT, "Parameter count is "+count+" sb "+JDPMDTest.CALL_PARM_COUNT);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }



/**
getParameterCount() - Should work for a stored procedure call that has a return
value.
**/
    public void Var007()
    {
	if(checkJdbc30()) /* WILSONJO parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.RETURN_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int count = pmd.getParameterCount();
		ps.close();
		assertCondition(count == 1);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }

    /**
getParameterCount() - Should work for a stored procedure call that has a return
value and parameters.
**/
    public void Var008()
    {
	if(checkJdbc30()) 
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.RETURNPARAMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int count = pmd.getParameterCount();
		ps.close();
		assertCondition(count == 4);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }

}

