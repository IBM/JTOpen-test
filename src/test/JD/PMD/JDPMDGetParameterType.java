///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPMDGetParameterType.java
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
// File Name:    JDPMDGetParameterType.java
//
// Classes:      JDPMDGetParameterType
//
////////////////////////////////////////////////////////////////////////
//
//

//
////////////////////////////////////////////////////////////////////////

package test.JD.PMD;


import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDPMDTest;
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;


/**
Testcase JDPSetInt.  This tests the following method
of the JDBC ParameterMetaData class:

<ul>
<li>getParameterType()
</ul>
**/
public class JDPMDGetParameterType
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDPMDGetParameterType";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDPMDTest.main(newArgs); 
   }

    // Private data.
    // Change when we get a JDK 1.4
    private PreparedStatement   ps;
    // Change when we get a JDK 1.4
    private ParameterMetaData   pmd;


/**
Constructor.
**/
    public JDPMDGetParameterType (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDPMDGetParameterType",
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
        connection_ = null; 

    }



/**
getParameterType() - Should throw exception if the statement gets closed.
**/
    public void Var001()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		ps.close();
		int type = pmd.getParameterType(1);

		failed ("Didn't throw SQLException "+type);
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



/**
getParameterType() - Should throw exception if the index is too small.
**/
    public void Var002()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(0);

		failed ("Didn't throw SQLException"+type);
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



/**
getParameterType() - Should throw exception if the index is too large.
**/
    public void Var003()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(100);

		failed ("Didn't throw SQLException"+type);
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
getParameterType() - Should work for a statement after the statement has been 
executed.
**/
    public void Var004()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		CallableStatement cs = (CallableStatement)JDPMDTest.getStatement(JDPMDTest.DIRECTIONS_CALLSTMT, connection_,supportedFeatures_);
		cs.setInt(1, 1);
		cs.setInt(3, 1);
		cs.registerOutParameter(2, java.sql.Types.INTEGER);
		cs.registerOutParameter(3, java.sql.Types.INTEGER);

		cs.executeUpdate();
		pmd = cs.getParameterMetaData();
		int type = pmd.getParameterType(1);
		cs.close();
		assertCondition(type == java.sql.Types.INTEGER);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work for a stored procedure call with
a return value parameter.
**/
    public void Var005()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.RETURN_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(1);
		ps.close();
		assertCondition(type == java.sql.Types.INTEGER);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a prepared statement for 
an insert for data type of smallint.
**/
    public void Var006()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(1);
		ps.close();
		assertCondition(type == java.sql.Types.SMALLINT);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a prepared statement for 
an insert for data type of integer.
**/
    public void Var007()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(2);
		ps.close();
		assertCondition(type == java.sql.Types.INTEGER);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a prepared statement for 
an insert for data type of real.
**/
    public void Var008()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(3);
		ps.close();
		assertCondition(type == java.sql.Types.REAL, "type is "+type+" expected REAL="+java.sql.Types.REAL);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a prepared statement for 
an insert for data type of float.
**/
    public void Var009()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(4);
		ps.close();
		assertCondition(type == java.sql.Types.DOUBLE);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a prepared statement for 
an insert for data type of double.
**/
    public void Var010()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(5);
		ps.close();
		assertCondition(type == java.sql.Types.DOUBLE);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a prepared statement for 
an insert for data type of decimal.
**/
    public void Var011()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(6);
		ps.close();
		assertCondition(type == java.sql.Types.DECIMAL);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a prepared statement for 
an insert for data type of numeric.
**/
    public void Var012()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(7);
		ps.close();
		assertCondition(type == java.sql.Types.NUMERIC);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a prepared statement for 
an insert for data type of char.
**/
    public void Var013()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(8);
		ps.close();
		assertCondition(type == java.sql.Types.CHAR);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a prepared statement for 
an insert for data type of varchar.
**/
    public void Var014()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(9);
		ps.close();
		assertCondition(type == java.sql.Types.VARCHAR);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a prepared statement for 
an insert for data type of binary.
**/
    public void Var015()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(10);
		ps.close();
		assertCondition(type == java.sql.Types.BINARY, "Got type="+type+" expected BINARY="+java.sql.Types.BINARY);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a prepared statement for 
an insert for data type of varbinary.
**/
    public void Var016()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(11);
		ps.close();
		assertCondition(type == java.sql.Types.VARBINARY, "Got type="+type+" expected VARBINARY="+java.sql.Types.VARBINARY);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getParameterType() - Should work with a prepared statement for 
an insert for data type of date.
**/
    public void Var017()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(12);
		ps.close();
		assertCondition(type == java.sql.Types.DATE);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a prepared statement for 
an insert for data type of time.
**/
    public void Var018()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(13);
		ps.close();
		assertCondition(type == java.sql.Types.TIME);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a prepared statement for 
an insert for data type of timestamp.
**/
    public void Var019()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(14);
		ps.close();
		assertCondition(type == java.sql.Types.TIMESTAMP);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a prepared statement for 
an insert for data type of blob.
**/
    public void Var020()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(15);
		ps.close();
		assertCondition(type == java.sql.Types.BLOB);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a prepared statement for 
an insert for data type of clob.
**/
    public void Var021()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(16);
		ps.close();
		assertCondition(type == java.sql.Types.CLOB);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a prepared statement for 
an insert for data type of dbclob.
**/
    public void Var022()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(17);
		ps.close();
		assertCondition(type == java.sql.Types.CLOB);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a prepared statement for 
an insert for data type of datalink.
**/
    public void Var023()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(18);
		ps.close();

	    // The correct type for this is (I feel) java.net.URL per the 
	    // JDBC 3.0 specification.  Before that release, it should
	    // be treated as a String.
		if (getJdbcLevel() > 2)
		    assertCondition(type == 70); // 70 is the constant from java.sql.Types for DATALINK
		else
		    assertCondition(type == java.sql.Types.VARCHAR);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a prepared statement for 
an insert for data type of bigint.
**/
    public void Var024()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(19);
		ps.close();
		assertCondition(type == java.sql.Types.BIGINT);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a prepared statement for 
a query for data type of smallint.
**/
    public void Var025()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(1);
		ps.close();
		assertCondition(type == java.sql.Types.SMALLINT);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a prepared statement for 
a query for data type of integer.
**/
    public void Var026()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(2);
		ps.close();
		assertCondition(type == java.sql.Types.INTEGER);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a prepared statement for 
a query for data type of real.
**/
    public void Var027()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(3);
		ps.close();
		assertCondition(type == java.sql.Types.REAL, "Got type="+type+" expected REAL="+java.sql.Types.REAL);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a prepared statement for 
a query for data type of float.
**/
    public void Var028()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(4);
		ps.close();
		assertCondition(type == java.sql.Types.DOUBLE);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a prepared statement for 
a query for data type of double.
**/
    public void Var029()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(5);
		ps.close();
		assertCondition(type == java.sql.Types.DOUBLE);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a prepared statement for 
a query for data type of decimal.
**/
    public void Var030()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(6);
		ps.close();
		assertCondition(type == java.sql.Types.DECIMAL);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a prepared statement for 
a query for data type of numeric.
**/
    public void Var031()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(7);
		ps.close();
		assertCondition(type == java.sql.Types.NUMERIC);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a prepared statement for 
a query for data type of char.
**/
    public void Var032()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(8);
		ps.close();
		assertCondition(type == java.sql.Types.CHAR);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a prepared statement for 
a query for data type of varchar.
**/
    public void Var033()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(9);
		ps.close();
		assertCondition(type == java.sql.Types.VARCHAR);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a prepared statement for 
a query for data type of binary.
**/
    public void Var034()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(10);
		ps.close();
		assertCondition(type == java.sql.Types.BINARY, "Got type="+type+" expected BINARY="+java.sql.Types.BINARY);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a prepared statement for 
a query for data type of varbinary.
**/
    public void Var035()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(11);
		ps.close();
		assertCondition(type == java.sql.Types.VARBINARY, "Got type="+type+" expected VARBINARY="+java.sql.Types.VARBINARY);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getParameterType() - Should work with a prepared statement for 
a query for data type of date.
**/
    public void Var036()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(12);
		ps.close();
		assertCondition(type == java.sql.Types.DATE);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a prepared statement for 
a query for data type of time.
**/
    public void Var037()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(13);
		ps.close();
		assertCondition(type == java.sql.Types.TIME);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a prepared statement for 
a query for data type of timestamp.
**/
    public void Var038()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(14);
		ps.close();
		assertCondition(type == java.sql.Types.TIMESTAMP);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a prepared statement for 
a query for data type of blob.
**/
    public void Var039()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(15);
		ps.close();
		assertCondition(type == java.sql.Types.BLOB);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a prepared statement for 
a query for data type of clob.
**/
    public void Var040()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(16);
		ps.close();
		assertCondition(type == java.sql.Types.CLOB);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a prepared statement for 
a query for data type of dbclob.
**/
    public void Var041()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(17);
		ps.close();
		assertCondition(type == java.sql.Types.CLOB);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a prepared statement for 
a query for data type of bigint.
**/
    public void Var042()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(18);
		ps.close();
		assertCondition(type == java.sql.Types.BIGINT);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a callable statement for 
a procedure call for data type of smallint.
**/
    public void Var043()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(1);
		ps.close();
		assertCondition(type == java.sql.Types.SMALLINT);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a callable statement for 
a procedure call for data type of integer.
**/
    public void Var044()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(2);
		ps.close();
		assertCondition(type == java.sql.Types.INTEGER);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a callable statement for 
a procedure call for data type of real.
**/
    public void Var045()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(3);
		ps.close();
		assertCondition(type == java.sql.Types.REAL);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a callable statement for 
a procedure call for data type of float.
**/
    public void Var046()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(4);
		ps.close();
		assertCondition(type == java.sql.Types.DOUBLE);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a callable statement for 
a procedure call for data type of double.
**/
    public void Var047()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(5);
		ps.close();
		assertCondition(type == java.sql.Types.DOUBLE);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a callable statement for 
a procedure call for data type of decimal.
**/
    public void Var048()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(6);
		ps.close();
		assertCondition(type == java.sql.Types.DECIMAL);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a callable statement for 
a procedure call for data type of numeric.
**/
    public void Var049()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(8);
		ps.close();
		assertCondition(type == java.sql.Types.NUMERIC);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a callable statement for 
a procedure call for data type of char.
**/
    public void Var050()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(10);
		ps.close();
		assertCondition(type == java.sql.Types.CHAR);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a callable statement for 
a procedure call for data type of varchar.
**/
    public void Var051()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(12);
		ps.close();
		assertCondition(type == java.sql.Types.VARCHAR);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a callable statement for 
a procedure call for data type of binary.
**/
    public void Var052()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(13);
		ps.close();
		assertCondition(type == java.sql.Types.BINARY);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a callable statement for 
a procedure call for data type of varbinary.
**/
    public void Var053()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(14);
		ps.close();
		assertCondition(type == java.sql.Types.VARBINARY);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getParameterType() - Should work with a callable statement for 
a procedure call for data type of date.
**/
    public void Var054()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(15);
		ps.close();
		assertCondition(type == java.sql.Types.DATE);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a callable statement for 
a procedure call for data type of time.
**/
    public void Var055()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(16);
		ps.close();
		assertCondition(type == java.sql.Types.TIME);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a callable statement for 
a procedure call for data type of timestamp.
**/
    public void Var056()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(17);
		ps.close();
		assertCondition(type == java.sql.Types.TIMESTAMP);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a callable statement for 
a procedure call for data type of datalink.
**/
    public void Var057()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(18);
		ps.close();
		assertCondition(type == java.sql.Types.VARCHAR); // with datalink supported... what should this value be?
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a callable statement for 
a procedure call for data type of blob.
**/
    public void Var058()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(19);
		ps.close();
		assertCondition(type == java.sql.Types.BLOB);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a callable statement for 
a procedure call for data type of clob.
**/
    public void Var059()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(20);
		ps.close();
		assertCondition(type == java.sql.Types.CLOB);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a callable statement for 
a procedure call for data type of dbclob.
**/
    public void Var060()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(21);
		ps.close();
		assertCondition(type == java.sql.Types.CLOB);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getParameterType() - Should work with a callable statement for 
a procedure call for data type of bigint.
**/
    public void Var061()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int type = pmd.getParameterType(22);
		ps.close();
		assertCondition(type == java.sql.Types.BIGINT);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }
    
    
    
    

    public void testQueryParameter(String castType, int expectedType) {
  try {
     ps = connection_.prepareStatement("Select cast(? AS "+castType+") from sysibm.sysdummy1");
     pmd = ps.getParameterMetaData();
     int parmType = pmd.getParameterType(1);
     ps.close();
     assertCondition(parmType == expectedType, "From castType="+castType+" got "+parmType+" sb "+ expectedType);
  }
  catch (Exception e) {
      failed (e, "Unexpected Exception");
  }

    } 

    int getXMLType() { 
      try { 
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
       if (JDReflectionUtil.getStaticField_I("com.ibm.as400.access.AS400JDBCDriver", "JDBC_MAJOR_VERSION_") >= 4) { 
          return 2009; 
        } 
      } else if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
	  // Note -- native returns bad answer to JDK 1.4 (12/05/2016 -- not bothering to fix)
          return 2009;
	  
      }
      } catch (Exception e) {
        System.out.println("Warning:  TESTCASE error "); 
        e.printStackTrace(System.out); 

      }
      return Types.CLOB; 
    }
    
    int getNCharType() { 
      try { 
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
       if (JDReflectionUtil.getStaticField_I("com.ibm.as400.access.AS400JDBCDriver", "JDBC_MAJOR_VERSION_") >= 4) { 
          return -15; /* NCHAR */  
        } 
      } else if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
        if (isJdbc40()) {
          return -15; /* NCHAR */  
        } 

      }
      } catch (Exception e) {
        System.out.println("Warning:  TESTCASE error "); 
        e.printStackTrace(System.out); 

      }
      return Types.CHAR; 
    }
    
    int getNVarcharType() { 
      try { 
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
       if (JDReflectionUtil.getStaticField_I("com.ibm.as400.access.AS400JDBCDriver", "JDBC_MAJOR_VERSION_") >= 4) { 
         return -9; /* NVARCHAR */  
        } 
      } else if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
        if (isJdbc40()) {
          return -9; /* NVARCHAR */  
        } 

      }
      } catch (Exception e) {
        System.out.println("Warning:  TESTCASE error "); 
        e.printStackTrace(System.out); 

      }
      return Types.VARCHAR; 
    }
    

  int getNClobType() {
    try {
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
        if (JDReflectionUtil.getStaticField_I(
            "com.ibm.as400.access.AS400JDBCDriver", "JDBC_MAJOR_VERSION_") >= 4) {
          return 2011; /* NCHAR */
        }
      } else if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
        if (isJdbc40()) {
          return 2011; /* NCHAR */
        } 
      }
    } catch (Exception e) {
      System.out.println("Warning:  TESTCASE error ");
      e.printStackTrace(System.out);

    }
    return Types.CLOB;
  }
    
    public void Var062() {  testQueryParameter("XML", getXMLType());     }
    public void Var063() {  testQueryParameter("NCHAR(10)", getNCharType());   }
    public void Var064() {  testQueryParameter("GRAPHIC(10) CCSID 1200 ", getNCharType());     }
    public void Var065() {  testQueryParameter("GRAPHIC(10) CCSID 13488 ", Types.CHAR);   }
    public void Var066() {  testQueryParameter("GRAPHIC(10) CCSID 835 ", Types.CHAR);   }
    public void Var067() {  testQueryParameter("NVARCHAR(10)", getNVarcharType());  }
    public void Var068() {  testQueryParameter("VARGRAPHIC(10) CCSID 1200 ", getNVarcharType());     }
    public void Var069() {  testQueryParameter("VARGRAPHIC(10) CCSID 13488 ", Types.VARCHAR);   }
    public void Var070() {  testQueryParameter("VARGRAPHIC(10) CCSID 835 ", Types.VARCHAR);   }
    public void Var071() {  testQueryParameter("NCLOB(10)", getNClobType());    }
    public void Var072() {  testQueryParameter("DBCLOB(10) CCSID 1200", getNClobType());    }
    public void Var073() {  testQueryParameter("DBCLOB(10) CCSID 13488", Types.CLOB);    }
    public void Var074() {  testQueryParameter("DBCLOB(10) CCSID 835", Types.CLOB);    }
    public void Var075() {  testQueryParameter("DECFLOAT(16)", Types.OTHER);    }
    public void Var076() {  testQueryParameter("DECFLOAT(34)", Types.OTHER);    }

/**
getParameterType() - Should work with a callable statement for 
a procedure call for data type of bigint.
**/
    public void Var077()
    {
  if(checkBooleanSupport()) 
  {
      try {
    ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
    pmd = ps.getParameterMetaData();
    int type = pmd.getParameterType(25);
    ps.close();
    assertCondition(type == java.sql.Types.BOOLEAN, "Got type="+type+" sb java.sql.Types.BOOLEAN="+java.sql.Types.BOOLEAN);
      }
      catch (Exception e) {
    failed (e, "Unexpected Exception");
      }
  }
    }

    public void Var078() { if (checkBooleanSupport()) testQueryParameter("BOOLEAN", Types.BOOLEAN);    }

    
}

