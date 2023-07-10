///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPMDGetDirection.java
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
// File Name:    JDPMDGetDirection.java
//
// Classes:      JDPMDGetDirection
//
////////////////////////////////////////////////////////////////////////
//
//
//
//
////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;

import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;

import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;

import java.util.Hashtable;
// remove when we get JDK 1.4
//import com.ibm.db2.jdbc.app.*;


/**
Testcase JDPSetInt.  This tests the following method
of the JDBC ParameterMetaData class:

<ul>
<li>getDirection()
</ul>
**/
public class JDPMDGetDirection
extends JDTestcase
{

    // Private data.
    private Connection          connection_;
    private PreparedStatement   ps;
    private ParameterMetaData   pmd;


/**
Constructor.
**/
    public JDPMDGetDirection (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDPMDGetDirection",
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
getDirection() - Should throw exception if the statement gets closed.
**/
    public void Var001()
    {
	if(checkJdbc30()) /* WILSONJO parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_, supportedFeatures_);
		pmd = ps.getParameterMetaData();
		ps.close();
		int direction = pmd.getParameterMode(1);

		failed ("Didn't throw SQLException"+direction);
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



/**
getDirection() - Should throw exception if the index is too small.
**/
    public void Var002()
    {
	if(checkJdbc30()) /* WILSONJO parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_, supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int direction = pmd.getParameterMode(0);

		failed ("Didn't throw SQLException "+direction);
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



/**
getDirection() - Should throw exception if the index is too large.
**/
    public void Var003()
    {
	if(checkJdbc30()) /* WILSONJO parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_, supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int direction = pmd.getParameterMode(100);

		failed ("Didn't throw SQLException "+direction);
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
getDirection() - Should work for a statement after the statement has been 
executed.
**/
    public void Var004()
    {
	if(checkJdbc30()) /* WILSONJO parameter metadata need jdbc 3.0 */
	{
	    try {
		CallableStatement cs = (CallableStatement)JDPMDTest.getStatement(JDPMDTest.DIRECTIONS_CALLSTMT, connection_, supportedFeatures_);
		cs.setInt(1, 1);
		cs.setInt(3, 1);
		cs.registerOutParameter(2, java.sql.Types.INTEGER);
		cs.registerOutParameter(3, java.sql.Types.INTEGER);

		cs.executeUpdate();
		pmd = cs.getParameterMetaData();
		int direction = pmd.getParameterMode(1);
		cs.close();
		assertCondition(direction == 1);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getDirection() - Should work for a stored procedure call with
an output parameter.
**/
    public void Var005()
    {
	if(checkJdbc30()) /* WILSONJO parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.RETURN_CALLSTMT, connection_, supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int direction = pmd.getParameterMode(1);
		ps.close();
		assertCondition(direction == 4);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getDirection() - Should work with a prepared statement for 
an insert.
**/
    public void Var006()
    {
	if(checkJdbc30()) /* WILSONJO parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_, supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int direction = pmd.getParameterMode(1);
		ps.close();
		assertCondition(direction == 1);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }



/**
getDirection() - Should work with a prepared statement for 
a query.
**/
    public void Var007()
    {
	if(checkJdbc30()) /* WILSONJO parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_, supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int direction = pmd.getParameterMode(1);
		ps.close();
		assertCondition(direction == 1);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    
/**
getDirection() - Should work with a callable statement for 
a procedure call for an input parameter.
**/
    public void Var008()
    {
	if(checkJdbc30()) /* WILSONJO parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.DIRECTIONS_CALLSTMT, connection_, supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int direction = pmd.getParameterMode(1);
		ps.close();
		assertCondition(direction == 1);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getDirection() - Should work with a callable statement for 
a procedure call for an output parameter.
**/
    public void Var009()
    {
	if(checkJdbc30()) /* WILSONJO parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.DIRECTIONS_CALLSTMT, connection_, supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int direction = pmd.getParameterMode(2);
		ps.close();
		assertCondition(direction == 4);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }
    

/**
getDirection() - Should work with a callable statement for 
a procedure call for an input/output parameter.
**/
    public void Var010()
    {
	if(checkJdbc30()) /* WILSONJO parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.DIRECTIONS_CALLSTMT, connection_, supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int direction = pmd.getParameterMode(3);
		ps.close();
		assertCondition(direction == 2);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }

  /**
   * getDirection() - Should work with a callable statement for a procedure call
   * for an boolean parameter.
   **/
  public void Var011() {
    if (checkBooleanSupport()) {
      try {
        ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,
            supportedFeatures_);
        pmd = ps.getParameterMetaData();
        int direction = pmd.getParameterMode(25);
        ps.close();
        assertCondition(direction == 2);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }
    
    
    
}

