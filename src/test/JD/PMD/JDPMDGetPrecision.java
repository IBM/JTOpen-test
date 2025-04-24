///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPMDGetPrecision.java
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
// File Name:    JDPMDGetPrecision.java
//
// Classes:      JDPMDGetPrecision
//
////////////////////////////////////////////////////////////////////////
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
<li>getPrecision()
</ul>
**/
public class JDPMDGetPrecision
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDPMDGetPrecision";
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
    public JDPMDGetPrecision (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDPMDGetPrecision",
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
getPrecision() - Should throw exception if the statement gets closed.
**/
    public void Var001()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		ps.close();
		int precision = pmd.getPrecision(1);

		failed ("Didn't throw SQLException "+precision);
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



/**
getPrecision() - Should throw exception if the index is too small.
**/
    public void Var002()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(0);

		failed ("Didn't throw SQLException"+precision);
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



/**
getPrecision() - Should throw exception if the index is too large.
**/
    public void Var003()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(100);

		failed ("Didn't throw SQLException"+precision);
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
getPrecision() - Should work for a statement after the statement has been
executed.
**/
    public void Var004()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		CallableStatement cs = (CallableStatement)JDPMDTest.getStatement(JDPMDTest.DIRECTIONS_CALLSTMT, connection_,supportedFeatures_);
		cs.setInt(1, 1);
		cs.setInt(3, 1);
		cs.registerOutParameter(2, java.sql.Types.INTEGER);
		cs.registerOutParameter(3, java.sql.Types.INTEGER);

		cs.execute();
		pmd = cs.getParameterMetaData();
		int precision = pmd.getPrecision(1);
		cs.close();
		assertCondition(precision == 10);   //@B1C
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work for a stored procedure call with
a return value parameter.
**/
    public void Var005()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.RETURN_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(1);
		ps.close();
		assertCondition(precision == 10);  //@B1C
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a prepared statement for
an insert for data type of smallint.
**/
    public void Var006()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(1);
		ps.close();
		assertCondition(precision == 5, "got "+precision+" sb 5");   //@A1C
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a prepared statement for
an insert for data type of integer.
**/
    public void Var007()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(2);
		ps.close();
		assertCondition(precision == 10, "got "+precision+" sb 10");   //@A1C
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a prepared statement for
an insert for data type of real.
**/
    public void Var008()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(3);
		ps.close();
		assertCondition(precision == 24,"precision = "+precision+" SB 24");   //@A1C  //@C1C
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a prepared statement for
an insert for data type of float.
**/
    public void Var009()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(4);
		ps.close();
		assertCondition(precision == 53,"precision = "+precision+" SB 53");   //@A1C  //@C1C
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a prepared statement for
an insert for data type of double.
**/
    public void Var010()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(5);
		ps.close();
		assertCondition(precision == 53,"precision = "+precision+" SB 53");   //@A1C  //@C1C
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a prepared statement for
an insert for data type of decimal.
**/
    public void Var011()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(6);
		ps.close();
		assertCondition(precision == 5, "got "+precision+" sb 5");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a prepared statement for
an insert for data type of numeric.
**/
    public void Var012()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(7);
		ps.close();
		assertCondition(precision == 10, "got "+precision+" sb 10");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a prepared statement for
an insert for data type of char.
**/
    public void Var013()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(8);
		ps.close();
		assertCondition(precision == 50, "got "+precision+" sb 50");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a prepared statement for
an insert for data type of varchar.
**/
    public void Var014()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(9);
		ps.close();
		assertCondition(precision == 50, "got "+precision+" sb 50");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a prepared statement for
an insert for data type of binary.
**/
    public void Var015()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(10);
		ps.close();
		assertCondition(precision == 20, "got "+precision+" sb 20");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a prepared statement for
an insert for data type of varbinary.
**/
    public void Var016()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(11);
		ps.close();
		assertCondition(precision == 20, "got "+precision+" sb 20");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getPrecision() - Should work with a prepared statement for
an insert for data type of date.
**/
    public void Var017()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(12);
		ps.close();
		assertCondition(precision == 10, "got "+precision+" sb 10");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a prepared statement for
an insert for data type of time.
**/
    public void Var018()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(13);
		ps.close();
		assertCondition(precision == 8, "got "+precision+" sb 8");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a prepared statement for
an insert for data type of timestamp.
**/
    public void Var019()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(14);
		ps.close();
		assertCondition(precision == 26, "got "+precision+" sb 26");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a prepared statement for
an insert for data type of blob.
**/
    public void Var020()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(15);
		ps.close();
		assertCondition(precision == 200, "got "+precision+" sb 200");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a prepared statement for
an insert for data type of clob.
**/
    public void Var021()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(16);
		ps.close();
		assertCondition(precision == 200,"precision = "+precision+" SB 200");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a prepared statement for
an insert for data type of dbclob.
**/
    public void Var022()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(17);
		ps.close();

		// Fixed to return length in characters 10/28/2016
		assertCondition(precision == 200, "got "+precision+" sb 200");   //@A1C
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a prepared statement for
an insert for data type of datalink.
**/
    public void Var023()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(18);
		ps.close();
		assertCondition(precision == 200, "got "+precision+" sb 200");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a prepared statement for
an insert for data type of bigint.
**/
    public void Var024()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(19);
		ps.close();
		assertCondition(precision == 19, "got "+precision+" sb 19");   //@A1C
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a prepared statement for
a query for data type of smallint.
**/
    public void Var025()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(1);
		ps.close();
		assertCondition(precision == 5, "got "+precision+" sb 5");   //@A1C
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a prepared statement for
a query for data type of integer.
**/
    public void Var026()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(2);
		ps.close();
		assertCondition(precision == 10, "got "+precision+" sb 10");   //@A1C
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a prepared statement for
a query for data type of real.
**/
    public void Var027()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(3);
		ps.close();
		assertCondition(precision == 24,"precision = "+precision+" SB 24");   //@A1C  //@C1C
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a prepared statement for
a query for data type of float.
**/
    public void Var028()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(4);
		ps.close();
		assertCondition(precision == 53,"precision = "+precision+" SB 53");   //@A1C      //@C1C
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a prepared statement for
a query for data type of double.
**/
    public void Var029()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(5);
		ps.close();
		assertCondition(precision == 53,"precision = "+precision+" SB 53");   //@A1C      //@K1C
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a prepared statement for
a query for data type of decimal.
**/
    public void Var030()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(6);
		ps.close();
		assertCondition(precision == 5, "got "+precision+" sb 5");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a prepared statement for
a query for data type of numeric.
**/
    public void Var031()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(7);
		ps.close();
		assertCondition(precision == 10, "got "+precision+" sb 10");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a prepared statement for
a query for data type of char.
**/
    public void Var032()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(8);
		ps.close();
		assertCondition(precision == 50, "got "+precision+" sb 50");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a prepared statement for
a query for data type of varchar.
**/
    public void Var033()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(9);
		ps.close();
		assertCondition(precision == 50, "got "+precision+" sb 50");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a prepared statement for
a query for data type of binary.
**/
    public void Var034()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(10);
		ps.close();
		assertCondition(precision == 20, "got "+precision+" sb 20");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a prepared statement for
a query for data type of varbinary.
**/
    public void Var035()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(11);
		ps.close();
		assertCondition(precision == 20, "got "+precision+" sb 20");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getPrecision() - Should work with a prepared statement for
a query for data type of date.
**/
    public void Var036()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(12);
		ps.close();
		assertCondition(precision == 10, "got "+precision+" sb 10");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a prepared statement for
a query for data type of time.
**/
    public void Var037()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(13);
		ps.close();
		assertCondition(precision == 8, "got "+precision+" sb 8");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a prepared statement for
a query for data type of timestamp.
**/
    public void Var038()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(14);
		ps.close();
		assertCondition(precision == 26, "got "+precision+" sb 26");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a prepared statement for
a query for data type of blob.
**/
    public void Var039()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(15);
		ps.close();
		assertCondition(precision == 200, "got "+precision+" sb 200");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a prepared statement for
a query for data type of clob.
**/
    public void Var040()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(16);
		ps.close();
		assertCondition(precision == 200,"precision = "+precision+" SB 200");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a prepared statement for
a query for data type of dbclob.
**/
    public void Var041()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(17);
		ps.close();
		assertCondition(precision == 200, "got "+precision+" sb 200 for DBCLOB ");   //@A1C
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a prepared statement for
a query for data type of bigint.
**/
    public void Var042()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(18);
		ps.close();
		assertCondition(precision == 19, "got "+precision+" sb 19");   //@A1C
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a callable statement for
a procedure call for data type of smallint.
**/
    public void Var043()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(1);
		ps.close();
		assertCondition(precision == 5, "got "+precision+" sb 5");   //@A1C
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a callable statement for
a procedure call for data type of integer.
**/
    public void Var044()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(2);
		ps.close();
		assertCondition(precision == 10);   //@A1C
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a callable statement for
a procedure call for data type of real.
**/
    public void Var045()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(3);
		ps.close();
		assertCondition(precision == 24,"precision = "+precision+" SB 24 for REAL ");   //@A1C //@C1C
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a callable statement for
a procedure call for data type of float.
**/
    public void Var046()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(4);
		ps.close();
		assertCondition(precision == 53,"precision = "+precision+" SB 53 FOR FLOAT");   //@A1C     @C1C
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a callable statement for
a procedure call for data type of double.
**/
    public void Var047()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(5);
		ps.close();
		assertCondition(precision == 53,"precision = "+precision+" SB 53 FOR DOUBLE ");   //@A1C  @C1C
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a callable statement for
a procedure call for data type of decimal.
**/
    public void Var048()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(6);
		ps.close();
		assertCondition(precision == 5);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a callable statement for
a procedure call for data type of decimal.
**/
    public void Var049()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(7);
		ps.close();
		assertCondition(precision == 10);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a callable statement for
a procedure call for data type of numeric.
**/
    public void Var050()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(8);
		ps.close();
		assertCondition(precision == 5);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }



/**
getPrecision() - Should work with a callable statement for
a procedure call for data type of numeric.
**/
    public void Var051()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(9);
		ps.close();
		assertCondition(precision == 10);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a callable statement for
a procedure call for data type of char.
**/
    public void Var052()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(10);
		ps.close();
		assertCondition(precision == 1);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }



/**
getPrecision() - Should work with a callable statement for
a procedure call for data type of char.
**/
    public void Var053()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(11);
		ps.close();
		assertCondition(precision == 50);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a callable statement for
a procedure call for data type of varchar.
**/
    public void Var054()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(12);
		ps.close();
		assertCondition(precision == 50);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a callable statement for
a procedure call for data type of binary.
**/
    public void Var055()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(13);
		ps.close();
		assertCondition(precision == 20);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a callable statement for
a procedure call for data type of varbinary.
**/
    public void Var056()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(14);
		ps.close();
		assertCondition(precision == 20);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getPrecision() - Should work with a callable statement for
a procedure call for data type of date.
**/
    public void Var057()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(15);
		ps.close();
		assertCondition(precision == 10);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a callable statement for
a procedure call for data type of time.
**/
    public void Var058()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(16);
		ps.close();
		assertCondition(precision == 8);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a callable statement for
a procedure call for data type of timestamp.
**/
    public void Var059()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(17);
		ps.close();
		assertCondition(precision == 26);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a callable statement for
a procedure call for data type of datalink.
**/
    public void Var060()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(18);
		ps.close();
		assertCondition(precision == 200);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a callable statement for
a procedure call for data type of blob.
**/
    public void Var061()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(19);
		ps.close();
		assertCondition(precision == 200);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a callable statement for
a procedure call for data type of clob.
**/
    public void Var062()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(20);
		ps.close();
		assertCondition(precision == 200,"precision = "+precision+" SB 200");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a callable statement for
a procedure call for data type of dbclob.
**/
    public void Var063()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(21);
		ps.close();
		assertCondition(precision == 200,"precision = "+precision+" SB 200");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getPrecision() - Should work with a callable statement for
a procedure call for data type of bigint.
**/
    public void Var064()
    {
	if(checkJdbc30()) /* $C3 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		int precision = pmd.getPrecision(22);
		ps.close();
		assertCondition(precision == 19);   //@A1C
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }

    
    public void testQueryParameter(String castType, int expectedPrecision) {
  try {
     ps = connection_.prepareStatement("Select cast(? AS "+castType+") from sysibm.sysdummy1");
     pmd = ps.getParameterMetaData();
     int precision = pmd.getPrecision(1);
     // Toolbox returns max -1 for lob types.
     // Fix by bumping by one 
     if (precision == 2147483646) {
	 precision ++; 
     } 
     ps.close();
     assertCondition(precision == expectedPrecision, "From castType="+castType+" got "+precision+" sb "+ expectedPrecision);
  }
  catch (Exception e) {
      failed (e, "Unexpected Exception");
  }

    } 

    
    public void Var065() {  testQueryParameter("XML", 2147483647 );    }
    public void Var066() {  testQueryParameter("NCHAR(10)", 10 );  }
    public void Var067() {  testQueryParameter("GRAPHIC(10) CCSID 1200 ", 10);     }
    public void Var068() {  testQueryParameter("GRAPHIC(10) CCSID 13488 ", 10);   }
    public void Var069() {  testQueryParameter("GRAPHIC(10) CCSID 835 ", 10);   }
    public void Var070() {  testQueryParameter("NVARCHAR(10)", 10);  }
    public void Var071() {  testQueryParameter("VARGRAPHIC(10) CCSID 1200 ", 10  );   }
    public void Var072() {  testQueryParameter("VARGRAPHIC(10) CCSID 13488 ", 10   ); }
    public void Var073() {  testQueryParameter("VARGRAPHIC(10) CCSID 835 ", 10);   }
    public void Var074() {  testQueryParameter("NCLOB(10)", 10);    }
    public void Var075() {  testQueryParameter("DBCLOB(10) CCSID 1200", 10);    }
    public void Var076() {  testQueryParameter("DBCLOB(10) CCSID 13488", 10);    }
    public void Var077() {  testQueryParameter("DBCLOB(10) CCSID 835", 10);    }
    public void Var078() {  testQueryParameter("DECFLOAT(16)", 16);    }
    public void Var079() {  testQueryParameter("DECFLOAT(34)", 34);    }

/**
getPrecision() - Should work with a callable statement for
a procedure call for data type of bigint.
**/
    public void Var080()
    {
  if(checkBooleanSupport()) 
  {
      try {
    ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
    pmd = ps.getParameterMetaData();
    int precision = pmd.getPrecision(25);
    ps.close();
    assertCondition(precision == 1);   //@A1C
      }
      catch (Exception e) {
    failed (e, "Unexpected Exception");
      }
  }
    }

    
    public void Var081() { if (checkBooleanSupport()) testQueryParameter("BOOLEAN", 1);    }
    
    
    
}

