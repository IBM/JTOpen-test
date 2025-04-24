///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPMDGetParameterTypeName.java
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
// File Name:    JDPMDGetParameterTypeName.java
//
// Classes:      JDPMDGetParameterTypeName
//
////////////////////////////////////////////////////////////////////////
//
//
//
////////////////////////////////////////////////////////////////////////

package test.JD.PMD;

import com.ibm.as400.access.AS400;

import test.JDPMDTest;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;

import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;

import java.util.Hashtable; import java.util.Vector;

/**
Testcase JDPSetInt.  This tests the following method
of the JDBC ParameterMetaData class:

<ul>
<li>getParameterTypeName()
</ul>
**/
public class JDPMDGetParameterTypeName
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDPMDGetParameterTypeName";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDPMDTest.main(newArgs); 
   }

    // Private data.
    private Connection          connection_;
    private PreparedStatement   ps;
    private ParameterMetaData   pmd;


/**
Constructor.
**/
    public JDPMDGetParameterTypeName (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDPMDGetParameterTypeName",
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
getParameterTypeName() - Should throw exception if the statement gets closed.
**/
    public void Var001()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		ps.close();
		String name = pmd.getParameterTypeName(1);

		failed ("Didn't throw SQLException "+name);
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



/**
getParameterTypeName() - Should throw exception if the index is too small.
**/
    public void Var002()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(0);

		failed ("Didn't throw SQLException"+name);
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



/**
getParameterTypeName() - Should throw exception if the index is too large.
**/
    public void Var003()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(100);

		failed ("Didn't throw SQLException"+name);
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
getParameterTypeName() - Should work for a statement after the statement has been 
executed.
**/
    public void Var004()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		CallableStatement cs = (CallableStatement)JDPMDTest.getStatement(JDPMDTest.DIRECTIONS_CALLSTMT, connection_,supportedFeatures_);
		cs.setInt(1, 1);
		cs.setInt(3, 1);
		cs.registerOutParameter(2, java.sql.Types.INTEGER);
		cs.registerOutParameter(3, java.sql.Types.INTEGER);

		cs.execute();
		pmd = cs.getParameterMetaData();
		String name = pmd.getParameterTypeName(1);
		cs.close();
		assertCondition(name.equals("INTEGER"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work for a stored procedure call with
a return value parameter.
**/
    public void Var005()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.RETURN_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(1);
		ps.close();
		assertCondition(name.equals("INTEGER"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a prepared statement for 
an insert for data type of smallint.
**/
    public void Var006()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(1);
		ps.close();
		assertCondition(name.equals("SMALLINT"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a prepared statement for 
an insert for data type of integer.
**/
    public void Var007()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(2);
		ps.close();
		assertCondition(name.equals("INTEGER"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a prepared statement for 
an insert for data type of real.
**/
    public void Var008()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(3);
		ps.close();
		assertCondition(name.equals("REAL"), "got "+name+" expected REAL");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a prepared statement for 
an insert for data type of float.
**/
    public void Var009()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(4);
		ps.close();
		assertCondition(name.equals("DOUBLE"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a prepared statement for 
an insert for data type of double.
**/
    public void Var010()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(5);
		ps.close();
		assertCondition(name.equals("DOUBLE"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a prepared statement for 
an insert for data type of decimal.
**/
    public void Var011()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(6);
		ps.close();
		assertCondition(name.equals("DECIMAL"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a prepared statement for 
an insert for data type of numeric.
**/
    public void Var012()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(7);
		ps.close();
		assertCondition(name.equals("NUMERIC"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a prepared statement for 
an insert for data type of char.
**/
    public void Var013()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(8);
		ps.close();
		assertCondition(name.equals("CHAR"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a prepared statement for 
an insert for data type of varchar.
**/
    public void Var014()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(9);
		ps.close();
		assertCondition(name.equals("VARCHAR"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a prepared statement for 
an insert for data type of binary.
**/
    public void Var015()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(10);
		ps.close();
		assertCondition(name.equals("CHAR () FOR BIT DATA"), "got "+name+" expected CHAR () FOR BIT DATA");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a prepared statement for 
an insert for data type of varbinary.
**/
    public void Var016()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(11);
		ps.close();
		assertCondition(name.equals("VARCHAR () FOR BIT DATA"), "got "+name+" expected VARCHAR () FOR BIT DATA");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getParameterTypeName() - Should work with a prepared statement for 
an insert for data type of date.
**/
    public void Var017()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(12);
		ps.close();
		assertCondition(name.equals("DATE"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a prepared statement for 
an insert for data type of time.
**/
    public void Var018()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(13);
		ps.close();
		assertCondition(name.equals("TIME"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a prepared statement for 
an insert for data type of timestamp.
**/
    public void Var019()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(14);
		ps.close();
		assertCondition(name.equals("TIMESTAMP"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a prepared statement for 
an insert for data type of blob.
**/
    public void Var020()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(15);
		ps.close();
		assertCondition(name.equals("BLOB"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a prepared statement for 
an insert for data type of clob.
**/
    public void Var021()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(16);
		ps.close();
	    //if((getRelease() >=  JDTestDriver.RELEASE_V5R3M0) && (getDriver () == JDTestDriver.DRIVER_NATIVE)) //@B0
	    //	assertCondition(name.equals("DBCLOB"));
	    // else
		assertCondition(name.equals("CLOB"), "Name = "+name+" SB CLOB");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a prepared statement for 
an insert for data type of dbclob.
**/
    public void Var022()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(17);
		ps.close();
		assertCondition(name.equals("DBCLOB"), "got "+name+" expected DBCLOB");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a prepared statement for 
an insert for data type of datalink.
**/
    public void Var023()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(18);
		ps.close();

	    // The correct type for this is (I feel) java.net.URL per the 
	    // JDBC 3.0 specification.  Before that release, it should
	    // be treated as a String.
		if (getJdbcLevel() > 2)
		    assertCondition(name.equals("DATALINK"));
		else
		    assertCondition(name.equals("VARCHAR"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a prepared statement for 
an insert for data type of bigint.
**/
    public void Var024()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(19);
		ps.close();
		assertCondition(name.equals("BIGINT"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a prepared statement for 
a query for data type of smallint.
**/
    public void Var025()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(1);
		ps.close();
		assertCondition(name.equals("SMALLINT"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a prepared statement for 
a query for data type of integer.
**/
    public void Var026()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(2);
		ps.close();
		assertCondition(name.equals("INTEGER"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a prepared statement for 
a query for data type of real.
**/
    public void Var027()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(3);
		ps.close();
		assertCondition(name.equals("REAL"), "got "+name+" expected REAL");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a prepared statement for 
a query for data type of float.
**/
    public void Var028()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(4);
		ps.close();
		assertCondition(name.equals("DOUBLE"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a prepared statement for 
a query for data type of double.
**/
    public void Var029()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(5);
		ps.close();
		assertCondition(name.equals("DOUBLE"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a prepared statement for 
a query for data type of decimal.
**/
    public void Var030()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(6);
		ps.close();
		assertCondition(name.equals("DECIMAL"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a prepared statement for 
a query for data type of numeric.
**/
    public void Var031()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(7);
		ps.close();
		assertCondition(name.equals("NUMERIC"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a prepared statement for 
a query for data type of char.
**/
    public void Var032()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(8);
		ps.close();
		assertCondition(name.equals("CHAR"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a prepared statement for 
a query for data type of varchar.
**/
    public void Var033()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(9);
		ps.close();
		assertCondition(name.equals("VARCHAR"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a prepared statement for 
a query for data type of binary.
**/
    public void Var034()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(10);
		ps.close();
		assertCondition(name.equals("CHAR () FOR BIT DATA"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a prepared statement for 
a query for data type of varbinary.
**/
    public void Var035()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(11);
		ps.close();
		assertCondition(name.equals("VARCHAR () FOR BIT DATA"), "got "+name+" expected 'VARCHAR () FOR BIT DATA'");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getParameterTypeName() - Should work with a prepared statement for 
a query for data type of date.
**/
    public void Var036()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(12);
		ps.close();
		assertCondition(name.equals("DATE"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a prepared statement for 
a query for data type of time.
**/
    public void Var037()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(13);
		ps.close();
		assertCondition(name.equals("TIME"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a prepared statement for 
a query for data type of timestamp.
**/
    public void Var038()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(14);
		ps.close();
		assertCondition(name.equals("TIMESTAMP"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a prepared statement for 
a query for data type of blob.
**/
    public void Var039()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(15);
		ps.close();
		assertCondition(name.equals("BLOB"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a prepared statement for 
a query for data type of clob.
**/
    public void Var040()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(16);
		ps.close();
	    // if((getRelease() >=  JDTestDriver.RELEASE_V5R3M0) && (getDriver () == JDTestDriver.DRIVER_NATIVE)) //@B0
	    //	assertCondition(name.equals("DBCLOB"));
	    // else
		assertCondition(name.equals("CLOB"), "Name = "+name+" SB CLOB");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a prepared statement for 
a query for data type of dbclob.
**/
    public void Var041()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(17);
		ps.close();
		assertCondition(name.equals("DBCLOB"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a prepared statement for 
a query for data type of bigint.
**/
    public void Var042()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(18);
		ps.close();
		assertCondition(name.equals("BIGINT"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a callable statement for 
a procedure call for data type of smallint.
**/
    public void Var043()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(1);
		ps.close();
		assertCondition(name.equals("SMALLINT"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a callable statement for 
a procedure call for data type of integer.
**/
    public void Var044()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(2);
		ps.close();
		assertCondition(name.equals("INTEGER"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a callable statement for 
a procedure call for data type of real.
**/
    public void Var045()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(3);
		ps.close();
		assertCondition(name.equals("REAL"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a callable statement for 
a procedure call for data type of float.
**/
    public void Var046()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(4);
		ps.close();
		assertCondition(name.equals("DOUBLE"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a callable statement for 
a procedure call for data type of double.
**/
    public void Var047()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(5);
		ps.close();
		assertCondition(name.equals("DOUBLE"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a callable statement for 
a procedure call for data type of decimal.
**/
    public void Var048()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(6);
		ps.close();
		assertCondition(name.equals("DECIMAL"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a callable statement for 
a procedure call for data type of numeric.
**/
    public void Var049()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(8);
		ps.close();
		assertCondition(name.equals("NUMERIC"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a callable statement for 
a procedure call for data type of char.
**/
    public void Var050()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(10);
		ps.close();
		assertCondition(name.equals("CHAR"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a callable statement for 
a procedure call for data type of varchar.
**/
    public void Var051()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(12);
		ps.close();
		assertCondition(name.equals("VARCHAR"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a callable statement for 
a procedure call for data type of binary.
**/
    public void Var052()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(13);
		ps.close();
		assertCondition(name.equals("CHAR () FOR BIT DATA"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a callable statement for 
a procedure call for data type of varbinary.
**/
    public void Var053()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(14);
		ps.close();
		String expected = "VARCHAR () FOR BIT DATA";
		assertCondition(name.equals(expected), "got "+name+" expected "+expected);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getParameterTypeName() - Should work with a callable statement for 
a procedure call for data type of date.
**/
    public void Var054()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(15);
		ps.close();
		assertCondition(name.equals("DATE"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a callable statement for 
a procedure call for data type of time.
**/
    public void Var055()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(16);
		ps.close();
		assertCondition(name.equals("TIME"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a callable statement for 
a procedure call for data type of timestamp.
**/
    public void Var056()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(17);
		ps.close();
		assertCondition(name.equals("TIMESTAMP"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a callable statement for 
a procedure call for data type of datalink.
**/
    public void Var057()
    {
        // Right now the datalink in the stored procedure is actually a varchar
        // so this test can't work.
        notApplicable();
        /*
        try {
            ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
            pmd = ps.getParameterMetaData();
            String name = pmd.getParameterTypeName(18);
            ps.close();
            assertCondition(name.equals("DATALINK"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        */
    }


/**
getParameterTypeName() - Should work with a callable statement for 
a procedure call for data type of blob.
**/
    public void Var058()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(19);
		ps.close();
		assertCondition(name.equals("BLOB"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a callable statement for 
a procedure call for data type of clob.
**/
    public void Var059()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(20);
		ps.close();
	    // if((getRelease() >=  JDTestDriver.RELEASE_V5R3M0) && (getDriver () == JDTestDriver.DRIVER_NATIVE)) //@B0
	    //	assertCondition(name.equals("DBCLOB"));
	    // else
		assertCondition(name.equals("CLOB"), "Name = "+name+" SB CLOB");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a callable statement for 
a procedure call for data type of dbclob.
**/
    public void Var060()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(21);
		ps.close();
	    // if((getRelease() >=  JDTestDriver.RELEASE_V5R3M0) && (getDriver () == JDTestDriver.DRIVER_NATIVE)) //@B0
	    //	assertCondition(name.equals("DBCLOB"));
	    // else
		assertCondition(name.equals("CLOB"), "Name = "+name+" SB CLOB");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterTypeName() - Should work with a callable statement for 
a procedure call for data type of bigint.
**/
    public void Var061()
    {
	if(checkJdbc30()) /* $B2 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterTypeName(22);
		ps.close();
		assertCondition(name.equals("BIGINT"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    public void testQueryParameter(String castType, String expectedName) {
	try {
	   ps = connection_.prepareStatement("Select cast(? AS "+castType+") from sysibm.sysdummy1");
	   pmd = ps.getParameterMetaData();
	   String name = pmd.getParameterTypeName(1);
	   ps.close();
	   assertCondition(name.equals(expectedName), "From castType="+castType+" got "+name+" sb "+ expectedName);
	}
	catch (Exception e) {
	    failed (e, "Unexpected Exception");
	}

    } 

    public void Var062() {	testQueryParameter("XML", "XML");     }
    public void Var063() {  testQueryParameter("NCHAR(10)", "NCHAR");   }
    public void Var064() {  testQueryParameter("GRAPHIC(10) CCSID 1200 ", "NCHAR");     }
    public void Var065() {  testQueryParameter("GRAPHIC(10) CCSID 13488 ", "GRAPHIC");   }
    public void Var066() {  testQueryParameter("GRAPHIC(10) CCSID 835 ", "GRAPHIC");   }
    public void Var067() {  testQueryParameter("NVARCHAR(10)", "NVARCHAR");  }
    public void Var068() {  testQueryParameter("VARGRAPHIC(10) CCSID 1200 ", "NVARCHAR");     }
    public void Var069() {  testQueryParameter("VARGRAPHIC(10) CCSID 13488 ", "VARGRAPHIC");   }
    public void Var070() {  testQueryParameter("VARGRAPHIC(10) CCSID 835 ", "VARGRAPHIC");   }
    public void Var071() {  testQueryParameter("NCLOB(10)", "NCLOB");    }
    public void Var072() {  testQueryParameter("DBCLOB(10) CCSID 1200", "NCLOB");    }
    public void Var073() {  testQueryParameter("DBCLOB(10) CCSID 13488", "DBCLOB");    }
    public void Var074() {  testQueryParameter("DBCLOB(10) CCSID 835", "DBCLOB");    }
    public void Var075() {  testQueryParameter("DECFLOAT(16)", "DECFLOAT");    }
    public void Var076() {  testQueryParameter("DECFLOAT(34)", "DECFLOAT");    }

/**
getParameterTypeName() - Should work with a callable statement for 
a procedure call for data type of boolean.
**/
    public void Var077()
    {
  if(checkBooleanSupport()) 
  {
      try {
    ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
    pmd = ps.getParameterMetaData();
    String name = pmd.getParameterTypeName(25);
    ps.close();
    assertCondition(name.equals("BOOLEAN"));
      }
      catch (Exception e) {
    failed (e, "Unexpected Exception");
      }
  }
    }

    public void Var078() { if (checkBooleanSupport()) testQueryParameter("BOOLEAN", "BOOLEAN");    }
    
    
    
}

