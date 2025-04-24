///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPMDIsSigned.java
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
// File Name:    JDPMDIsSigned.java
//
// Classes:      JDPMDIsSigned
//
////////////////////////////////////////////////////////////////////////
//
//
//
//
////////////////////////////////////////////////////////////////////////

package test.JD.PMD;


import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.util.Hashtable; import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDPMDTest;
import test.JDTestcase;

/**
Testcase JDPSetInt.  This tests the following method
of the JDBC ParameterMetaData class:

<ul>
<li>isSigned()
</ul>
**/
public class JDPMDIsSigned
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDPMDIsSigned";
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
    public JDPMDIsSigned (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDPMDIsSigned",
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
isSigned() - Should throw exception if the statement gets closed.
**/
    public void Var001()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		ps.close();
		boolean signed = pmd.isSigned(1);

		failed ("Didn't throw SQLException"+signed);
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



/**
isSigned() - Should throw exception if the index is too small.
**/
    public void Var002()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(0);

		failed ("Didn't throw SQLException"+signed);
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



/**
isSigned() - Should throw exception if the index is too large.
**/
    public void Var003()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(100);

		failed ("Didn't throw SQLException"+signed);
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
isSigned() - Should work for a statement after the statement has been 
executed.
**/
    public void Var004()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		CallableStatement cs = (CallableStatement)JDPMDTest.getStatement(JDPMDTest.DIRECTIONS_CALLSTMT, connection_,supportedFeatures_);
		cs.setInt(1, 1);
		cs.setInt(3, 1);
		cs.registerOutParameter(2, java.sql.Types.INTEGER);
		cs.registerOutParameter(3, java.sql.Types.INTEGER);

		cs.execute();
		pmd = cs.getParameterMetaData();
		boolean signed = pmd.isSigned(1);
		cs.close();
		assertCondition(signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work for a stored procedure call with
an output parameter.
**/
    public void Var005()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.RETURN_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(1);
		ps.close();
		assertCondition(signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a prepared statement for 
an insert for data type of smallint.
**/
    public void Var006()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(1);
		ps.close();
		assertCondition(signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a prepared statement for 
an insert for data type of integer.
**/
    public void Var007()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(2);
		ps.close();
		assertCondition(signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a prepared statement for 
an insert for data type of real.
**/
    public void Var008()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(3);
		ps.close();
		assertCondition(signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a prepared statement for 
an insert for data type of float.
**/
    public void Var009()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(4);
		ps.close();
		assertCondition(signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a prepared statement for 
an insert for data type of double.
**/
    public void Var010()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(5);
		ps.close();
		assertCondition(signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a prepared statement for 
an insert for data type of decimal.
**/
    public void Var011()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(6);
		ps.close();
		assertCondition(signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a prepared statement for 
an insert for data type of numeric.
**/
    public void Var012()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(7);
		ps.close();
		assertCondition(signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a prepared statement for 
an insert for data type of char.
**/
    public void Var013()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(8);
		ps.close();
		assertCondition(!signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a prepared statement for 
an insert for data type of varchar.
**/
    public void Var014()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(9);
		ps.close();
		assertCondition(!signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a prepared statement for 
an insert for data type of binary.
**/
    public void Var015()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(10);
		ps.close();
		assertCondition(!signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a prepared statement for 
an insert for data type of varbinary.
**/
    public void Var016()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(11);
		ps.close();
		assertCondition(!signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }

/**
isSigned() - Should work with a prepared statement for 
an insert for data type of date.
**/
    public void Var017()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(12);
		ps.close();
		assertCondition(!signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a prepared statement for 
an insert for data type of time.
**/
    public void Var018()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(13);
		ps.close();
		assertCondition(!signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a prepared statement for 
an insert for data type of timestamp.
**/
    public void Var019()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(14);
		ps.close();
		assertCondition(!signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a prepared statement for 
an insert for data type of blob.
**/
    public void Var020()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(15);
		ps.close();
		assertCondition(!signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a prepared statement for 
an insert for data type of clob.
**/
    public void Var021()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(16);
		ps.close();
		assertCondition(!signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a prepared statement for 
an insert for data type of dbclob.
**/
    public void Var022()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(17);
		ps.close();
		assertCondition(!signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a prepared statement for 
an insert for data type of datalink.
**/
    public void Var023()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(18);
		ps.close();
		assertCondition(!signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a prepared statement for 
an insert for data type of bigint.
**/
    public void Var024()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.INSERT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(19);
		ps.close();
		assertCondition(signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a prepared statement for 
a query for data type of smallint.
**/
    public void Var025()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(1);
		ps.close();
		assertCondition(signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a prepared statement for 
a query for data type of integer.
**/
    public void Var026()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(2);
		ps.close();
		assertCondition(signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a prepared statement for 
a query for data type of real.
**/
    public void Var027()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(3);
		ps.close();
		assertCondition(signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a prepared statement for 
a query for data type of float.
**/
    public void Var028()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(4);
		ps.close();
		assertCondition(signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a prepared statement for 
a query for data type of double.
**/
    public void Var029()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(5);
		ps.close();
		assertCondition(signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a prepared statement for 
a query for data type of decimal.
**/
    public void Var030()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(6);
		ps.close();
		assertCondition(signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a prepared statement for 
a query for data type of numeric.
**/
    public void Var031()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(7);
		ps.close();
		assertCondition(signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a prepared statement for 
a query for data type of char.
**/
    public void Var032()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(8);
		ps.close();
		assertCondition(!signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a prepared statement for 
a query for data type of varchar.
**/
    public void Var033()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(9);
		ps.close();
		assertCondition(!signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a prepared statement for 
a query for data type of binary.
**/
    public void Var034()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(10);
		ps.close();
		assertCondition(!signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a prepared statement for 
a query for data type of varbinary.
**/
    public void Var035()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(11);
		ps.close();
		assertCondition(!signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }

/**
isSigned() - Should work with a prepared statement for 
a query for data type of date.
**/
    public void Var036()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(12);
		ps.close();
		assertCondition(!signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a prepared statement for 
a query for data type of time.
**/
    public void Var037()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(13);
		ps.close();
		assertCondition(!signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a prepared statement for 
a query for data type of timestamp.
**/
    public void Var038()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(14);
		ps.close();
		assertCondition(!signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a prepared statement for 
a query for data type of blob.
**/
    public void Var039()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(15);
		ps.close();
		assertCondition(!signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a prepared statement for 
a query for data type of clob.
**/
    public void Var040()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(16);
		ps.close();
		assertCondition(!signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a prepared statement for 
a query for data type of dbclob.
**/
    public void Var041()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(17);
		ps.close();
		assertCondition(!signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a prepared statement for 
a query for data type of bigint.
**/
    public void Var042()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.SELECT_PREPSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(18);
		ps.close();
		assertCondition(signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }

    
/**
isSigned() - Should work with a callable statement for 
a procedure call for data type of smallint.
**/
    public void Var043()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(1);
		ps.close();
		assertCondition(signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a callable statement for 
a procedure call for data type of integer.
**/
    public void Var044()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(2);
		ps.close();
		assertCondition(signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a callable statement for 
a procedure call for data type of real.
**/
    public void Var045()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(3);
		ps.close();
		assertCondition(signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a callable statement for 
a procedure call for data type of float.
**/
    public void Var046()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(4);
		ps.close();
		assertCondition(signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a callable statement for 
a procedure call for data type of double.
**/
    public void Var047()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(5);
		ps.close();
		assertCondition(signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a callable statement for 
a procedure call for data type of decimal.
**/
    public void Var048()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(6);
		ps.close();
		assertCondition(signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a callable statement for 
a procedure call for data type of decimal.
**/
    public void Var049()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(7);
		ps.close();
		assertCondition(signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a callable statement for 
a procedure call for data type of numeric.
**/
    public void Var050()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(8);
		ps.close();
		assertCondition(signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }



/**
isSigned() - Should work with a callable statement for 
a procedure call for data type of numeric.
**/
    public void Var051()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(9);
		ps.close();
		assertCondition(signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a callable statement for 
a procedure call for data type of char.
**/
    public void Var052()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(10);
		ps.close();
		assertCondition(!signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }



/**
isSigned() - Should work with a callable statement for 
a procedure call for data type of char.
**/
    public void Var053()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(11);
		ps.close();
		assertCondition(!signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a callable statement for 
a procedure call for data type of varchar.
**/
    public void Var054()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(12);
		ps.close();
		assertCondition(!signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a callable statement for 
a procedure call for data type of binary.
**/
    public void Var055()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(13);
		ps.close();
		assertCondition(!signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a callable statement for 
a procedure call for data type of varbinary.
**/
    public void Var056()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(14);
		ps.close();
		assertCondition(!signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }

/**
isSigned() - Should work with a callable statement for 
a procedure call for data type of date.
**/
    public void Var057()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(15);
		ps.close();
		assertCondition(!signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a callable statement for 
a procedure call for data type of time.
**/
    public void Var058()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(16);
		ps.close();
		assertCondition(!signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a callable statement for 
a procedure call for data type of timestamp.
**/
    public void Var059()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(17);
		ps.close();
		assertCondition(!signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a callable statement for 
a procedure call for data type of blob.
**/
    public void Var060()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(18);
		ps.close();
		assertCondition(!signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a callable statement for 
a procedure call for data type of clob.
**/
    public void Var061()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(19);
		ps.close();
		assertCondition(!signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a callable statement for 
a procedure call for data type of dbclob.
**/
    public void Var062()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(20);
		ps.close();
		assertCondition(!signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a callable statement for 
a procedure call for data type of datalink.
**/
    public void Var063()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(21);
		ps.close();
		assertCondition(!signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
isSigned() - Should work with a callable statement for 
a procedure call for data type of bigint.
**/
    public void Var064()
    {
	if(checkJdbc30()) /* $B0 parameter metadata need jdbc 3.0 */
	{
	    try {
		ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
		pmd = ps.getParameterMetaData();
		boolean signed = pmd.isSigned(22);
		ps.close();
		assertCondition(signed);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }

    /**
isSigned() - Should work with a callable statement for 
a procedure call for data type of boolean.
**/
    public void Var065()
    {
  if(checkBooleanSupport())
  {
      try {
    ps = JDPMDTest.getStatement(JDPMDTest.IOPARMS_CALLSTMT, connection_,supportedFeatures_);
    pmd = ps.getParameterMetaData();
    boolean signed = pmd.isSigned(25);
    ps.close();
    assertCondition(signed == false);
      }
      catch (Exception e) {
    failed (e, "Unexpected Exception");
      }
  }
    }

    
    
}

