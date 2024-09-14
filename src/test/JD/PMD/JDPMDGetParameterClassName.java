///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPMDGetParameterClassName.java
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
// File Name:    JDPMDGetParameterClassName.java
//
// Classes:      JDPMDGetParameterClassName
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
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.util.Hashtable;


/**
Testcase JDPSetInt.  This tests the following method
of the JDBC ParameterMetaData class:

<ul>
<li>getParameterClassName()
</ul>
**/
public class JDPMDGetParameterClassName
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDPMDGetParameterClassName";
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
    public JDPMDGetParameterClassName (AS400 systemObject,
                                       Hashtable namesAndVars,
                                       int runMode,
                                       FileOutputStream fileOutputStream,
                                       
                                       String password)
    {
        super (systemObject, "JDPMDGetParameterClassName",
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
    getParameterClassName() - Should throw exception if the statement gets closed.
    **/
    public void Var001()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		ps.close();
		String name = pmd.getParameterClassName(1);

		failed ("Didn't throw SQLException "+name);
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



    /**
    getParameterClassName() - Should throw exception if the index is too small.
    **/
    public void Var002()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(0);

		failed ("Didn't throw SQLException "+name);
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



    /**
    getParameterClassName() - Should throw exception if the index is too large.
    **/
    public void Var003()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(100);

		failed ("Didn't throw SQLException"+name);
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


    /**
    getParameterClassName() - Should work for a statement after the statement has been 
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
		String name = pmd.getParameterClassName(1);
		cs.close();
		assertCondition(name.equals("java.lang.Integer"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work for a stored procedure call with
    an output parameter.
    **/
    public void Var005()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.RETURN_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(1);
		ps.close();
		assertCondition(name.equals("java.lang.Integer"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a prepared statement for 
    an insert for data type of smallint.
    **/
    public void Var006()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(1);
		ps.close();
		if (getDriver() == JDTestDriver.DRIVER_TOOLBOX)
		    assertCondition(name.equals("java.lang.Integer"));
		else
            assertCondition(name.equals("java.lang.Short"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a prepared statement for 
    an insert for data type of integer.
    **/
    public void Var007()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(2);
		ps.close();
		assertCondition(name.equals("java.lang.Integer"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a prepared statement for 
    an insert for data type of real.
    **/
    public void Var008()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(3);
		ps.close();
		assertCondition(name.equals("java.lang.Float"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a prepared statement for 
    an insert for data type of float.
    **/
    public void Var009()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(4);
		ps.close();
		assertCondition(name.equals("java.lang.Double"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a prepared statement for 
    an insert for data type of double.
    **/
    public void Var010()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(5);
		ps.close();
		assertCondition(name.equals("java.lang.Double"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a prepared statement for 
    an insert for data type of decimal.
    **/
    public void Var011()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(6);
		ps.close();
		assertCondition(name.equals("java.math.BigDecimal"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a prepared statement for 
    an insert for data type of numeric.
    **/
    public void Var012()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(7);
		ps.close();
		assertCondition(name.equals("java.math.BigDecimal"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a prepared statement for 
    an insert for data type of char.
    **/
    public void Var013()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(8);
		ps.close();
		assertCondition(name.equals("java.lang.String"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a prepared statement for 
    an insert for data type of varchar.
    **/
    public void Var014()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(9);
		ps.close();
		assertCondition(name.equals("java.lang.String"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a prepared statement for 
    an insert for data type of binary.
    **/
    public void Var015()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(10);
		ps.close();
		assertCondition(name.equals("[B"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a prepared statement for 
    an insert for data type of varbinary.
    **/
    public void Var016()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(11);
		ps.close();
		assertCondition(name.equals("[B"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }

    /**
    getParameterClassName() - Should work with a prepared statement for 
    an insert for data type of date.
    **/
    public void Var017()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(12);
		ps.close();
		assertCondition(name.equals("java.sql.Date"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a prepared statement for 
    an insert for data type of time.
    **/
    public void Var018()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(13);
		ps.close();
		assertCondition(name.equals("java.sql.Time"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a prepared statement for 
    an insert for data type of timestamp.
    **/
    public void Var019()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(14);
		ps.close();
		assertCondition(name.equals("java.sql.Timestamp"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a prepared statement for 
    an insert for data type of blob.
    **/
    public void Var020()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(15);
		ps.close();
		if (getDriver() == JDTestDriver.DRIVER_TOOLBOX)
		    assertCondition(name.equals("com.ibm.as400.access.AS400JDBCBlob"));
		else
		    assertCondition(name.equals("com.ibm.db2.jdbc.app.DB2Blob"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a prepared statement for 
    an insert for data type of clob.
    **/
    public void Var021()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(16);
		ps.close();
		if (getDriver() == JDTestDriver.DRIVER_TOOLBOX)
		    assertCondition(name.equals("com.ibm.as400.access.AS400JDBCClob"));
		else
		    assertCondition(name.equals("com.ibm.db2.jdbc.app.DB2Clob"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a prepared statement for 
    an insert for data type of dbclob.
    **/
    public void Var022()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(17);
		ps.close();
		if (getDriver() == JDTestDriver.DRIVER_TOOLBOX)
		{
		    assertCondition(name.equals("com.ibm.as400.access.AS400JDBCClob"));
		}
		else
		    assertCondition(name.equals("com.ibm.db2.jdbc.app.DB2Clob"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a prepared statement for 
    an insert for data type of datalink.
    **/
    public void Var023()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(18);
		ps.close();

	    // The correct type for this is (I feel) java.net.URL per the 
	    // JDBC 3.0 specification.  Before that release, it should
	    // be treated as a String.
		if (getJdbcLevel() > 2)
		    assertCondition(name.equals("java.net.URL"));
		else
		    assertCondition(name.equals("java.lang.String"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a prepared statement for 
    an insert for data type of bigint.
    **/
    public void Var024()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(19);
		ps.close();
		assertCondition(name.equals("java.lang.Long"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a prepared statement for 
    a query for data type of smallint.
    **/
    public void Var025()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(1);
		ps.close();
		if (getDriver() == JDTestDriver.DRIVER_TOOLBOX)
            assertCondition(name.equals("java.lang.Integer"));
        else
    		assertCondition(name.equals("java.lang.Short"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a prepared statement for 
    a query for data type of integer.
    **/
    public void Var026()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(2);
		ps.close();
		assertCondition(name.equals("java.lang.Integer"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a prepared statement for 
    a query for data type of real.
    **/
    public void Var027()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(3);
		ps.close();
		assertCondition(name.equals("java.lang.Float"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a prepared statement for 
    a query for data type of float.
    **/
    public void Var028()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(4);
		ps.close();
		assertCondition(name.equals("java.lang.Double"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a prepared statement for 
    a query for data type of double.
    **/
    public void Var029()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(5);
		ps.close();
		assertCondition(name.equals("java.lang.Double"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a prepared statement for 
    a query for data type of decimal.
    **/
    public void Var030()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(6);
		ps.close();
		assertCondition(name.equals("java.math.BigDecimal"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a prepared statement for 
    a query for data type of numeric.
    **/
    public void Var031()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(7);
		ps.close();
		assertCondition(name.equals("java.math.BigDecimal"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a prepared statement for 
    a query for data type of char.
    **/
    public void Var032()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(8);
		ps.close();
		assertCondition(name.equals("java.lang.String"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a prepared statement for 
    a query for data type of varchar.
    **/
    public void Var033()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(9);
		ps.close();
		assertCondition(name.equals("java.lang.String"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a prepared statement for 
    a query for data type of binary.
    **/
    public void Var034()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(10);
		ps.close();
		assertCondition(name.equals("[B"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a prepared statement for 
    a query for data type of varbinary.
    **/
    public void Var035()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(11);
		ps.close();
		assertCondition(name.equals("[B"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }

    /**
    getParameterClassName() - Should work with a prepared statement for 
    a query for data type of date.
    **/
    public void Var036()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(12);
		ps.close();
		assertCondition(name.equals("java.sql.Date"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a prepared statement for 
    a query for data type of time.
    **/
    public void Var037()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(13);
		ps.close();
		assertCondition(name.equals("java.sql.Time"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a prepared statement for 
    a query for data type of timestamp.
    **/
    public void Var038()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(14);
		ps.close();
		assertCondition(name.equals("java.sql.Timestamp"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a prepared statement for 
    a query for data type of blob.
    **/
    public void Var039()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(15);
		ps.close();
		if (getDriver() == JDTestDriver.DRIVER_TOOLBOX)
		    assertCondition(name.equals("com.ibm.as400.access.AS400JDBCBlob"));
		else
		    assertCondition(name.equals("com.ibm.db2.jdbc.app.DB2Blob"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a prepared statement for 
    a query for data type of clob.
    **/
    public void Var040()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(16);
		ps.close();
		if (getDriver() == JDTestDriver.DRIVER_TOOLBOX)
		    assertCondition(name.equals("com.ibm.as400.access.AS400JDBCClob"));
		else
		    assertCondition(name.equals("com.ibm.db2.jdbc.app.DB2Clob"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a prepared statement for 
    a query for data type of dbclob.
    **/
    public void Var041()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(17);
		ps.close();
		if (getDriver() == JDTestDriver.DRIVER_TOOLBOX)
		    assertCondition(name.equals("com.ibm.as400.access.AS400JDBCClob"));
		else
		    assertCondition(name.equals("com.ibm.db2.jdbc.app.DB2Clob"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a prepared statement for 
    a query for data type of bigint.
    **/
    public void Var042()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(18);
		ps.close();
		assertCondition(name.equals("java.lang.Long"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a callable statement for 
    a procedure call for data type of smallint.
    **/
    public void Var043()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(1);
		ps.close();
		if (getDriver() == JDTestDriver.DRIVER_TOOLBOX)
            assertCondition(name.equals("java.lang.Integer"));
        else
    		assertCondition(name.equals("java.lang.Short"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a callable statement for 
    a procedure call for data type of integer.
    **/
    public void Var044()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(2);
		ps.close();
		assertCondition(name.equals("java.lang.Integer"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a callable statement for 
    a procedure call for data type of real.
    **/
    public void Var045()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(3);
		ps.close();
		assertCondition(name.equals("java.lang.Float"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a callable statement for 
    a procedure call for data type of float.
    **/
    public void Var046()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(4);
		ps.close();
		assertCondition(name.equals("java.lang.Double"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a callable statement for 
    a procedure call for data type of double.
    **/
    public void Var047()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(5);
		ps.close();
		assertCondition(name.equals("java.lang.Double"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a callable statement for 
    a procedure call for data type of decimal.
    **/
    public void Var048()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(6);
		ps.close();
		assertCondition(name.equals("java.math.BigDecimal"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a callable statement for 
    a procedure call for data type of numeric.
    **/
    public void Var049()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(8);
		ps.close();
		assertCondition(name.equals("java.math.BigDecimal"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a callable statement for 
    a procedure call for data type of char.
    **/
    public void Var050()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(10);
		ps.close();
		assertCondition(name.equals("java.lang.String"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a callable statement for 
    a procedure call for data type of varchar.
    **/
    public void Var051()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(12);
		ps.close();
		assertCondition(name.equals("java.lang.String"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a callable statement for 
    a procedure call for data type of binary.
    **/
    public void Var052()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(13);
		ps.close();
		assertCondition(name.equals("[B"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a callable statement for 
    a procedure call for data type of varbinary.
    **/
    public void Var053()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(14);
		ps.close();
		assertCondition(name.equals("[B"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }

    /**
    getParameterClassName() - Should work with a callable statement for 
    a procedure call for data type of date.
    **/
    public void Var054()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(15);
		ps.close();
		assertCondition(name.equals("java.sql.Date"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a callable statement for 
    a procedure call for data type of time.
    **/
    public void Var055()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(16);
		ps.close();
		assertCondition(name.equals("java.sql.Time"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a callable statement for 
    a procedure call for data type of timestamp.
    **/
    public void Var056()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(17);
		ps.close();
		assertCondition(name.equals("java.sql.Timestamp"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a callable statement for 
    a procedure call for data type of datalink.
    **/
    public void Var057()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(18);
		ps.close();
	    // Handle datalinks...
		assertCondition(name.equals("java.lang.String"));   //@A1C
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a callable statement for 
    a procedure call for data type of blob.
    **/
    public void Var058()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(19);
		ps.close();
		if (getDriver() == JDTestDriver.DRIVER_TOOLBOX)
		    assertCondition(name.equals("com.ibm.as400.access.AS400JDBCBlob"));
		else
		    assertCondition(name.equals("com.ibm.db2.jdbc.app.DB2Blob"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a callable statement for 
    a procedure call for data type of clob.
    **/
    public void Var059()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(20);
		ps.close();
		if (getDriver() == JDTestDriver.DRIVER_TOOLBOX)
		    assertCondition(name.equals("com.ibm.as400.access.AS400JDBCClob"));
		else
		    assertCondition(name.equals("com.ibm.db2.jdbc.app.DB2Clob"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a callable statement for 
    a procedure call for data type of dbclob.
    **/
    public void Var060()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(21);
		ps.close();
		if (getDriver() == JDTestDriver.DRIVER_TOOLBOX)
		    assertCondition(name.equals("com.ibm.as400.access.AS400JDBCClob"));
		else
		    assertCondition(name.equals("com.ibm.db2.jdbc.app.DB2Clob"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    getParameterClassName() - Should work with a callable statement for 
    a procedure call for data type of bigint.
    **/
    public void Var061()
    {
	if(checkJdbc30()) /* $A3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		String name = pmd.getParameterClassName(22);
		ps.close();
		assertCondition(name.equals("java.lang.Long"));
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
     String name = pmd.getParameterClassName(1);
     ps.close();
     assertCondition(name.equals(expectedName), "From castType="+castType+" got "+name+" sb "+ expectedName);
  }
  catch (Exception e) {
      failed (e, "Unexpected Exception");
  }

    } 

    String getXmlExpectedClass() { 
      String expectedClass = "java.sql.SQLXML";
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          expectedClass = "com.ibm.as400.access.AS400JDBCSQLXML"; 
      } else if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
          expectedClass = "com.ibm.db2.jdbc.app.DB2SQLXML";
      } else {
        
      }
      return expectedClass;
    }

    
    public void Var062() {  testQueryParameter("XML", getXmlExpectedClass());     }
    public void Var063() {  testQueryParameter("NCHAR(10)", "java.lang.String");   }
    public void Var064() {  testQueryParameter("GRAPHIC(10) CCSID 1200 ", "java.lang.String");     }
    public void Var065() {  testQueryParameter("GRAPHIC(10) CCSID 13488 ", "java.lang.String");   }
    public void Var066() {  testQueryParameter("GRAPHIC(10) CCSID 835 ", "java.lang.String");   }
    public void Var067() {  testQueryParameter("NVARCHAR(10)", "java.lang.String");  }
    public void Var068() {  testQueryParameter("VARGRAPHIC(10) CCSID 1200 ", "java.lang.String");     }
    public void Var069() {  testQueryParameter("VARGRAPHIC(10) CCSID 13488 ", "java.lang.String");   }
    public void Var070() {  testQueryParameter("VARGRAPHIC(10) CCSID 835 ", "java.lang.String");   }

    String getNclobExpectedClass() { 
      String expectedClass = "java.sql.NClob";
      try { 
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
	  /* For toolbox we always use the NClob class */
          /* Prior to JDK 1.6, the class does not implement NCLOB */
	  expectedClass = "com.ibm.as400.access.AS400JDBCNClob";
	  String javaVersion = System.getProperty("java.version");
	  if ((javaVersion.indexOf("1.4") >= 0)  ||
	      (javaVersion.indexOf("1.5") >= 0) ) {
	      expectedClass = "com.ibm.as400.access.AS400JDBCClob";
	  } 
      } else if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
          expectedClass = "com.ibm.db2.jdbc.app.DB2Clob";
      } else {
        
      }
      } catch (Exception e) {  
        System.out.println("Warning:  TESTCASE error "); 
        e.printStackTrace(System.out); 
      }
      return expectedClass;
    }
    
    String getClobExpectedClass() { 
      String expectedClass = "java.sql.Clob";
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          expectedClass = "com.ibm.as400.access.AS400JDBCClob"; 
      } else if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
          expectedClass = "com.ibm.db2.jdbc.app.DB2Clob"; 
          
      } else {
        
      }
      return expectedClass;
    }
    
    
    
  public void Var071() {
    testQueryParameter("NCLOB(10)", getNclobExpectedClass());
  }

  public void Var072() {
    testQueryParameter("DBCLOB(10) CCSID 1200", getNclobExpectedClass());
  }

  public void Var073() {  
      testQueryParameter("DBCLOB(10) CCSID 13488", getClobExpectedClass());  
  }
  public void Var074() {  
    testQueryParameter("DBCLOB(10) CCSID 835", getClobExpectedClass());  
  }
    public void Var075() {  testQueryParameter("DECFLOAT(16)", "java.math.BigDecimal");    }
    public void Var076() {  testQueryParameter("DECFLOAT(34)", "java.math.BigDecimal");    }


    
        /**
    getParameterClassName() - Should work with a callable statement for 
    a procedure call for data type of boolean
    **/
    public void Var077()
    {
  if(checkBooleanSupport()) 
  {
      try {
    ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
    pmd = ps.getParameterMetaData();
    String name = pmd.getParameterClassName(25);
    ps.close();
    assertCondition(name.equals("java.lang.Boolean"), "got "+name+" sb java.lang.Boolean");
      }
      catch (Exception e) {
    failed (e, "Unexpected Exception");
      }
  }
    }

    public void Var078() {
      if (checkBooleanSupport())
      testQueryParameter("BOOLEAN", "java.lang.Boolean");    }

    
    
    
    
}

