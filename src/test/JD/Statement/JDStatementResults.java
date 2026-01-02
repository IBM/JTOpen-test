///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDStatementResults.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
 ////////////////////////////////////////////////////////////////////////
 //
 // File Name:    JDStatementResults.java
 //
 // Classes:      JDStatementResults
 //
 ////////////////////////////////////////////////////////////////////////

package test.JD.Statement;

import com.ibm.as400.access.AS400;

import test.JDJobName;
import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDStatementTest;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable; import java.util.Vector;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;



/**
Testcase JDStatementResults.  This tests the following methods
of the JDBC Statement class:

<ul>
<li>getResultSet()</li>
<li>getUpdateCount()</li>
<li>getMoreResults()</li>
<li>getMoreResults(int)</li>
<li>getLargeUpdateCount()</li>
</ul>
**/
public class JDStatementResults
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDStatementResults";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDStatementTest.main(newArgs); 
   }


    // Private data.
    private static  String table_  = JDStatementTest.COLLECTION + ".JDSR";

    private Statement       stmt_; 
    private static PreparedStatement  ps_;
    private static CallableStatement  cs_;


/**
Constructor.
**/
    public JDStatementResults (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDStatementResults",
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


	if (getDriver () == JDTestDriver.DRIVER_NATIVE ) {
           connection_ = testDriver_.getConnection (baseURL_+";reuse objects=false;", userId_, encryptedPassword_);
	} else {
        connection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
	} 

        Statement s1 = connection_.createStatement ();

        table_  = JDStatementTest.COLLECTION + ".JDSR";
	initTable(s1, table_ ,  " (NAME VARCHAR(10), ID INT, SCORE INT)");
        s1.executeUpdate ("INSERT INTO " + table_
            + " (NAME, ID) VALUES ('cnock', 1)");
        s1.executeUpdate ("INSERT INTO " + table_
            + " (NAME, ID) VALUES ('murch', 2)");
        s1.executeUpdate ("INSERT INTO " + table_
            + " (NAME, ID) VALUES ('joshvt', 3)");
        s1.close ();

            JDSetupProcedure.create (systemObject_,
                connection_, JDSetupProcedure.STP_RS0, supportedFeatures_, collection_, output_);
            JDSetupProcedure.create (systemObject_,
                connection_, JDSetupProcedure.STP_RS1, supportedFeatures_, collection_, output_);
            JDSetupProcedure.create (systemObject_,
                connection_, JDSetupProcedure.STP_RS3, supportedFeatures_, collection_, output_);
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
        Statement s1 = connection_.createStatement ();
        s1.executeUpdate ("DROP TABLE " + table_);
        s1.close ();
        connection_.close ();
    }






/**
Indicates if a result set is closed.
**/
    private boolean isResultSetClosed (ResultSet rs)
    {
        // Try to get the value of the first column of the
        // next row.  An exception will be thrown if the
        // result set is closed.
        try {
            rs.next ();
            rs.getString (1);
            return false;
        }
        catch (SQLException e) {
            return true;
        }
    }


/**
getResultSet() - Call on a closed statement.
Should throw an exception.
**/
    public void Var001()
    {
        try {
            Statement s1 = connection_.createStatement ();
            s1.close ();
            ResultSet rs = s1.getResultSet ();
            failed ("Didn't throw SQLException "+rs);
        }
        catch (Exception e) {
            assertClosedException(e, ""); 
        }
    }



/**
getResultSet() - Should return null when nothing has been done
with the statement.
**/
    public void Var002()
    {
        try {
            Statement s1 = connection_.createStatement ();
            ResultSet rs = s1.getResultSet ();
            s1.close ();
            assertCondition (rs == null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getResultSet() - Should return null when an update was run
using executeUpdate().
**/
    public void Var003()
    {
        try {
            Statement s1 = connection_.createStatement ();
            s1.executeUpdate ("UPDATE " + table_
                + " SET SCORE=2 WHERE ID > 1");
            ResultSet rs = s1.getResultSet ();
            s1.close ();
            assertCondition (rs == null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getResultSet() - Should return null when an update was run
using execute().
**/
    public void Var004()
    {
        try {
            Statement s1 = connection_.createStatement ();
            s1.execute ("UPDATE " + table_
                + " SET SCORE=3 WHERE ID > 1");
            ResultSet rs = s1.getResultSet ();
            s1.close ();
            assertCondition (rs == null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getResultSet() - Should return null when a stored procedure
with no result sets was run using executeUpdate().
**/
    public void Var005()
    {
        try {
            Statement s1 = connection_.createStatement ();
            s1.executeUpdate ("CALL "
                + JDSetupProcedure.STP_RS0);
            ResultSet rs = s1.getResultSet ();
            s1.close ();
            assertCondition (rs == null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getResultSet() - Should return null when a stored procedure
with no result sets was run using execute().
**/
    public void Var006()
    {
        try {
            Statement s = connection_.createStatement ();
            s.execute ("CALL " + JDSetupProcedure.STP_RS0);
            ResultSet rs = s.getResultSet ();
            s.close ();
            assertCondition (rs == null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getResultSet() - Should return the result set when a query
is run using executeQuery().
**/
    public void Var007()
    {
        try {
            Statement s = connection_.createStatement ();
            s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            ResultSet rs = s.getResultSet ();
            boolean success = rs.next ();
            rs.close ();
            s.close ();
            assertCondition (success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getResultSet() - Should return the result set when a query
is run using execute().
**/
    public void Var008()
    {
        try {
            Statement s = connection_.createStatement ();
            s.execute ("SELECT * FROM QIWS.QCUSTCDT");
            ResultSet rs = s.getResultSet ();
	    if (rs == null) throw new Exception("s.getResultSet() is null"); 
            boolean success = rs.next ();
            rs.close ();
            s.close ();
            assertCondition (success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getResultSet() - Should return the result set when a stored
procedure with 1 result set was run using executeQuery().
**/
    public void Var009()
    {
        try {
            Statement s = connection_.createStatement ();
            s.executeQuery ("CALL "
                + JDSetupProcedure.STP_RS1);
            ResultSet rs = s.getResultSet ();
            boolean success = rs.next ();
            rs.close ();
            s.close ();
            assertCondition (success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getResultSet() - Should return the result set when a stored
procedure with 1 result set was run using execute().
**/
    public void Var010()
    {
        try {
            Statement s = connection_.createStatement ();
            s.execute ("CALL "
                + JDSetupProcedure.STP_RS1);
            ResultSet rs = s.getResultSet ();
	    if (rs == null) throw new Exception("s.getResultSet() is null"); 
            boolean success = rs.next ();
            rs.close ();
            s.close ();
            assertCondition (success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getResultSet() - Should return the result set when a stored
procedure with 3 result sets was run using executeQuery().
**/
    public void Var011()
    {
        try {
            Statement s = connection_.createStatement ();
            s.executeQuery ("CALL "
                + JDSetupProcedure.STP_RS3);
            ResultSet rs = s.getResultSet ();
            boolean success = rs.next ();
            rs.close ();
            s.close ();
            assertCondition (success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getResultSet() - Should return the result set when a stored
procedure with 3 result sets was run using execute().
**/
    public void Var012()
    {
        try {
            Statement s = connection_.createStatement ();
            s.execute ("CALL "
                + JDSetupProcedure.STP_RS3);
            ResultSet rs = s.getResultSet ();
            boolean success = rs.next ();
            rs.close ();
            s.close ();
            assertCondition (success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getResultSet() - Should return the result set when a stored
procedure with 3 result sets was run using executeQuery()
and getMoreResults() is used to get the 2nd and 3rd result
sets.
**/
    public void Var013()
    {
        try {
            Statement s = connection_.createStatement ();
            s.executeQuery ("CALL "
                + JDSetupProcedure.STP_RS3);

            ResultSet rs1 = s.getResultSet ();
            boolean success1 = rs1.next ();
            rs1.close ();

            s.getMoreResults ();
            ResultSet rs2 = s.getResultSet ();
            boolean success2 = rs2.next ();
            rs2.close ();

            s.getMoreResults ();
            ResultSet rs3 = s.getResultSet ();
            boolean success3; 
            if (rs3 == null) {
              output_.println("rs3 not available ");
              success3 = false; 
            } else { 
              success3 = rs3.next ();
              rs3.close ();
            }

            s.getMoreResults ();
            ResultSet rs4 = s.getResultSet ();
            boolean success4 = (rs4 == null);

            s.close ();
            assertCondition (success1 && success2 && success3 && success4);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getResultSet() - Should return the result set when a stored
procedure with 3 result sets was run using execute()
and getMoreResults() is used to get the 2nd and 3rd result
sets.
**/
    public void Var014()
    {
        try {
            Statement s = connection_.createStatement ();
            s.execute ("CALL "
                + JDSetupProcedure.STP_RS3);

            ResultSet rs1 = s.getResultSet ();
            boolean success1 = rs1.next ();
            rs1.close ();

            s.getMoreResults ();
            ResultSet rs2 = s.getResultSet ();
            boolean success2 = rs2.next ();
            rs2.close ();

            s.getMoreResults ();
            ResultSet rs3 = s.getResultSet ();
            boolean success3; 
            if (rs3 == null) {
              output_.println("rs3 not available ");
              success3 = false; 
            } else { 
              success3 = rs3.next ();
              rs3.close ();
            }

            s.getMoreResults ();
            ResultSet rs4 = s.getResultSet ();
            boolean success4 = (rs4 == null);

            s.close ();
            assertCondition (success1 && success2 && success3 && success4);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getUpdateCount() - Call on a closed statement.
Should throw an exception.
**/
    public void Var015()
    {
        try {
            Statement s = connection_.createStatement ();
            s.close ();
            int updateCount = s.getUpdateCount ();
            failed ("Didn't throw SQLException updateCount="+updateCount);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getUpdateCount() - Should return -1 when nothing has been done
with the statement.
**/
    public void Var016()
    {
        try {
            Statement s = connection_.createStatement ();
            int updateCount = s.getUpdateCount ();
            s.close ();
            assertCondition (updateCount == -1);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getUpdateCount() - Should return 0 when an update was run that
does not update any rows using executeUpdate().
**/
    public void Var017()
    {
        try {
            Statement s = connection_.createStatement ();
            s.executeUpdate ("UPDATE " + table_
                + " SET SCORE=7 WHERE ID > 10");
            int updateCount = s.getUpdateCount ();
            s.close ();
            assertCondition (updateCount == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getUpdateCount() - Should return 0 when an update was run that
does not update any rows using execute().
**/
    public void Var018()
    {
        try {
            Statement s = connection_.createStatement ();
            s.execute ("UPDATE " + table_
                + " SET SCORE=7 WHERE ID > 10");
            int updateCount = s.getUpdateCount ();
            s.close ();
            assertCondition (updateCount == 0, "update count is "+updateCount+" sb 0");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getUpdateCount() - Should return the correct count when an
update was run that does update rows using executeUpdate().
**/
    public void Var019()
    {
        try {
            Statement s = connection_.createStatement ();
            s.executeUpdate ("UPDATE " + table_
                + " SET SCORE=7 WHERE ID > 1");
            int updateCount = s.getUpdateCount ();
            s.close ();
            assertCondition (updateCount == 2, "update count = "+updateCount+" should be 2 ");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getUpdateCount() - Should return the correct count when an
update was run that does update rows using execute().
**/
    public void Var020()
    {
        try {
            Statement s = connection_.createStatement ();
            s.execute ("UPDATE " + table_
                + " SET SCORE=7 WHERE ID > 1");
            int updateCount = s.getUpdateCount ();
            s.close ();
            assertCondition (updateCount == 2, "updateCount="+updateCount+" sb 2");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getUpdateCount() - Should return 0 when a stored procedure
with no result sets was run using executeUpdate().
**/
    public void Var021()
    {
        try {
            Statement s = connection_.createStatement ();
            s.executeUpdate ("CALL "
                + JDSetupProcedure.STP_RS0);
            int updateCount = s.getUpdateCount ();
            s.close ();
            assertCondition (updateCount == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getUpdateCount() - Should return 0 when a stored procedure
with no result sets was run using execute().
**/
    public void Var022()
    {
        try {
            Statement s = connection_.createStatement ();
            s.execute ("CALL "
                + JDSetupProcedure.STP_RS0);
            int updateCount = s.getUpdateCount ();
            s.close ();
            assertCondition (updateCount == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getUpdateCount() - Should return -1 when a query is run
using executeQuery().
**/
    public void Var023()
    {
        try {
            Statement s = connection_.createStatement ();
            s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            int updateCount = s.getUpdateCount ();
            s.close ();
            assertCondition (updateCount == -1, "update count from select is "+updateCount+" sb -1");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getUpdateCount() - Should return -1 when a query is run
using execute().
**/
    public void Var024()
    {
        try {
            Statement s = connection_.createStatement ();
            s.execute ("SELECT * FROM QIWS.QCUSTCDT");
            int updateCount = s.getUpdateCount ();
            s.close ();
            assertCondition (updateCount == -1, "update count from select is "+updateCount+" sb -1");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getUpdateCount() - Should return -1 when a stored
procedure with 1 result set was run using executeQuery().
**/
    public void Var025()
    {
        try {
            Statement s = connection_.createStatement ();
            s.executeQuery ("CALL "
                + JDSetupProcedure.STP_RS1);
            int updateCount = s.getUpdateCount ();
            s.close ();
            assertCondition (updateCount == -1, "Stored procedure "+JDSetupProcedure.STP_RS1+" update count is "+updateCount);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getUpdateCount() - Should return -1 when a stored
procedure with 1 result set was run using execute().
**/
    public void Var026()
    {
        try {
            Statement s = connection_.createStatement ();
            s.execute ("CALL "
                + JDSetupProcedure.STP_RS1);
            int updateCount = s.getUpdateCount ();
            s.close ();
            assertCondition (updateCount == -1, "Stored procedure "+JDSetupProcedure.STP_RS1+" update count is "+updateCount);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getUpdateCount() - Should return -1 when a stored
procedure with 3 result sets was run usgin executeQuery().
**/
    public void Var027()
    {
        try {
            Statement s = connection_.createStatement ();
            s.executeQuery ("CALL "
                + JDSetupProcedure.STP_RS3);
            int updateCount = s.getUpdateCount ();
            s.close ();
            assertCondition (updateCount == -1, "Stored procedure "+JDSetupProcedure.STP_RS1+" update count is "+updateCount);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getUpdateCount() - Should return -1 when a stored
procedure with 3 result sets was run usgin execute().
**/
    public void Var028()
    {
        try {
            Statement s = connection_.createStatement ();
            s.execute ("CALL "
                + JDSetupProcedure.STP_RS3);
            int updateCount = s.getUpdateCount ();
            s.close ();
            assertCondition (updateCount == -1, "Stored procedure "+JDSetupProcedure.STP_RS1+" update count is "+updateCount);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getUpdateCount() - Should return -1 when an
update was run that does update rows using executeUpdate()
and getMoreResults() is called.
**/
    public void Var029()
    {
        try {
            Statement s = connection_.createStatement ();
            s.executeUpdate ("UPDATE " + table_
                + " SET SCORE=7 WHERE ID > 1");
            s.getMoreResults ();
            int updateCount = s.getUpdateCount ();
            s.close ();
                assertCondition (updateCount == -1);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getUpdateCount() - Should return -1 when an
update was run that does update rows using execute() and
getMoreResults() is called.
**/
    public void Var030()
    {
        try {
            Statement s = connection_.createStatement ();
            s.execute ("UPDATE " + table_
                + " SET SCORE=7 WHERE ID > 1");
            s.getMoreResults ();
            int updateCount = s.getUpdateCount();
            s.close ();
             assertCondition (updateCount == -1);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getMoreResults() - Call on a closed statement.
Should throw an exception.
**/
    public void Var031()
    {
        try {
            Statement s = connection_.createStatement ();
            s.close ();
            boolean moreResults = s.getMoreResults ();
            failed ("Didn't throw SQLException moreResults="+moreResults);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getMoreResults() - Should return false when nothing has been done
with the statement.
**/
    public void Var032()
    {
        try {
            Statement s = connection_.createStatement ();
            boolean moreResults = s.getMoreResults ();
            s.close ();
            assertCondition (moreResults == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getMoreResults() - Should return false when an update was run
using executeUpdate().
**/
    public void Var033()
    {
        try {
            Statement s = connection_.createStatement ();
            s.executeUpdate ("UPDATE " + table_
                + " SET SCORE=2 WHERE ID > 1");
            boolean moreResults = s.getMoreResults ();
            s.close ();
            assertCondition (moreResults == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getMoreResults() - Should return false when an update was run
using execute().
**/
    public void Var034()
    {
        try {
            Statement s = connection_.createStatement ();
            s.execute ("UPDATE " + table_
                + " SET SCORE=3 WHERE ID > 1");
            boolean moreResults = s.getMoreResults ();
            s.close ();
            assertCondition (moreResults == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getMoreResults() - Should return false when a stored procedure
with no result sets was run using executeUpdate().
**/
    public void Var035()
    {
        try {
            Statement s = connection_.createStatement ();
            s.executeUpdate ("CALL "
                + JDSetupProcedure.STP_RS0);
            boolean moreResults = s.getMoreResults ();
            s.close ();
            assertCondition (moreResults == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getMoreResults() - Should return false when a stored procedure
with no result sets was run using execute().
**/
    public void Var036()
    {
        try {
            Statement s = connection_.createStatement ();
            s.execute ("CALL " + JDSetupProcedure.STP_RS0);
            boolean moreResults = s.getMoreResults ();
            s.close ();
            assertCondition (moreResults == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getMoreResults() - Should return false when a query
is run using executeQuery().
**/
    public void Var037()
    {
        try {
            Statement s = connection_.createStatement ();
            s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            boolean moreResults = s.getMoreResults ();
            s.close ();
            assertCondition (moreResults == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getMoreResults() - Should return false when a query
is run using execute().
**/
    public void Var038()
    {
        try {
            Statement s = connection_.createStatement ();
            s.execute ("SELECT * FROM QIWS.QCUSTCDT");
            ResultSet rs = s.getResultSet ();
            boolean moreResults = s.getMoreResults ();
            s.close ();
            assertCondition (moreResults == false, "moreResults is not false rs="+rs);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getMoreResults() - Should return false when a stored
procedure with 1 result set was run using executeQuery().
**/
    public void Var039()
    {
        try {
            Statement s = connection_.createStatement ();
            s.executeQuery ("CALL "
                + JDSetupProcedure.STP_RS1);
            boolean moreResults = s.getMoreResults ();
            s.close ();
            assertCondition (moreResults == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getMoreResults() - Should return false when a stored
procedure with 1 result set was run using execute().
**/
    public void Var040()
    {
        try {
            Statement s = connection_.createStatement ();
            s.execute ("CALL "
                + JDSetupProcedure.STP_RS1);
            boolean moreResults = s.getMoreResults ();
            s.close ();
            assertCondition (moreResults == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getMoreResults() - Should return true when a stored
procedure with 3 result sets was run using executeQuery(),
until the last result set has been retrieved.
**/
    public void Var041()
    {
        try {
            Statement s = connection_.createStatement ();
            s.executeQuery ("CALL "
                + JDSetupProcedure.STP_RS3);
            int count = 0;
            while (s.getMoreResults () == true)
                ++count;
            s.close ();
            assertCondition (count == 2);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getMoreResults() - Should return true when a stored
procedure with 3 result sets was run using execute(),
until the last result set has been retrieved.
**/
    public void Var042()
    {
        try {
            Statement s = connection_.createStatement ();
            s.execute ("CALL "
                + JDSetupProcedure.STP_RS3);
            int count = 0;
            while (s.getMoreResults () == true)
                ++count;
            s.close ();
            assertCondition (count == 2);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
getMoreResults(int)
getResultSet()
- If Statement.KEEP_CURRENT_RESULT is set, you should be able to have
access to three result sets at the same time.
- Using a Statement
**/
    public void Var043()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;
		Statement s=connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		rs1 = s.executeQuery("Call " + JDSetupProcedure.STP_RS3);
		boolean result = s.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs2 = s.getResultSet();
		boolean result1 = s.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs3 = s.getResultSet();
                /*
		ResultSetMetaData rsmd = rs1.getMetaData();
		output_.println(rsmd.getColumnCount());
		rsmd = rs2.getMetaData();
		output_.println(rsmd.getColumnCount());
                
		rsmd = rs3.getMetaData();
		output_.println(rsmd.getColumnCount());
                */
                boolean rs1next = rs1.next(); 
                boolean rs2next = rs2.next(); 
                boolean rs3next = rs3.next(); 
		assertCondition( rs1next && rs2next && rs3next && 
                                 result == true && result1 == true,
                               "Failed all should be true rs1next="+rs1next+
                               " rs2next="+rs2next+
                               " rs3next="+rs3next+
                               " result="+result+
                               " result1="+result1);
		rs1.close();
		rs2.close();
		rs3.close();
		s.close();
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
	            succeeded();
	        else
    		    failed(e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults(int)
getResultSet()
- After closing one of the result sets, you should no longer be able to perform commands
on it.
- Using a Statement
**/
    public void Var044()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;
		stmt_=connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		rs1 = stmt_.executeQuery("Call " + JDSetupProcedure.STP_RS3);
		stmt_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs2=stmt_.getResultSet();
		stmt_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs3=stmt_.getResultSet();
		rs1.close();
		rs3.next();
		//rs3.close();
		//rs2.close();
		try
		{
		    rs1.next();
		}
		catch (SQLException e)
		{
		    succeeded();
		    stmt_.close();
		    return;
		}
		stmt_.close();
		failed("Didn't throw SQLException rs2="+rs2);
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed ( e, "Unexpected Exception");
	    }
	}
    }

/**
getResultSet()
getMoreResults()
- After closing a ResultSet you should still be able to access the other result sets.
- Using a Statement
**/
    public void Var045()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;
		stmt_=connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		rs1 = stmt_.executeQuery("Call " + JDSetupProcedure.STP_RS3);
		stmt_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs2 = stmt_.getResultSet();
		stmt_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs3 = stmt_.getResultSet();
		rs1.next();
		rs2.next();
		rs3.next();
		rs2.close();
                boolean rs1next=rs1.next(); 
                boolean rs3next=rs3.next(); 
		assertCondition( rs1next && rs3next, 
                    "rs1next="+rs1next+" sb true "+
                    "rs3next="+rs3next+" sb true ");
		rs1.close();
		rs3.close();
		stmt_.close();
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed ( e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- After calling getMoreResults(Statement.CLOSE_CURRENT_RESULT) you shouldn't be able to access
the ResultSet obtained before the current one.
- Using a Statement
**/
    public void Var046()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;

		stmt_=connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		rs1=stmt_.executeQuery("Call " + JDSetupProcedure.STP_RS3);
		stmt_.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
		rs2=stmt_.getResultSet();
	        rs2.next();
		try
		{
		    rs1.next();
		}
		catch (SQLException e)
		{
		    succeeded();
		    rs2.close();
		    stmt_.close();
		    return;
		}
		rs1.close();
		rs2.close();
		stmt_.close();
		failed("Didn't throw SQLException");
	    }
	    catch( Exception e )
	    {
		failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- After getting two result sets and then calling getMoreResults(Statement.CLOSE_CURRENT_RESULT), you should
not be able to access the second result set.
- Using a Statement
**/
    public void Var047()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;

		stmt_=connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		rs1 = stmt_.executeQuery("Call " + JDSetupProcedure.STP_RS3);
		stmt_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs2 = stmt_.getResultSet();
		stmt_.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
		rs3 = stmt_.getResultSet();
	        rs1.next();
	        rs3.next();
		try
		{
		    rs2.next();
		}
		catch (SQLException e)
		{
		    succeeded();
		    rs1.close();
                    rs2.close();
		    rs3.close();
		    stmt_.close();
		    return;
		}
		rs1.close();
		rs2.close();
		rs3.close();
		stmt_.close();
		failed("Didn't throw SQLException");
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- After getting two result sets and then calling getMoreResults(Statement.CLOSE_CURRENT_RESULT), you should
be able to access the first and third result sets.
- Using a Statement
**/
    public void Var048()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;

		stmt_=connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		rs1 = stmt_.executeQuery("Call " + JDSetupProcedure.STP_RS3);
		stmt_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs2 = stmt_.getResultSet();
		stmt_.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
		rs3 = stmt_.getResultSet();
                boolean rs1next = rs1.next(); 
                boolean rs3next = rs3.next(); 
		assertCondition( rs1next && rs3next, 
                                 "rs1next="+rs1next+" sb true "+
                                 "rs3next="+rs3next+" sb true  rs2="+rs2);
		rs1.close();
		rs3.close();
		stmt_.close();
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- After getting two result sets and then calling getMoreResults(Statement.CLOSE_CURRENT_RESULT), you should
be able to access the first and third result sets, but not the second.
- Using a Statement
**/
    public void Var049()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;

		stmt_=connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		rs1 = stmt_.executeQuery("Call " + JDSetupProcedure.STP_RS3);
		stmt_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs2 = stmt_.getResultSet();
		stmt_.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
		rs3 = stmt_.getResultSet();
		rs1.next();
		rs3.next();
		rs1.close();
		rs3.close();
		try
		{
		    rs2.next();
		}
		catch (SQLException e)
		{
		    succeeded();
		    stmt_.close();
		    return;
		}
		rs2.close();
		stmt_.close();
		failed("Didn't throw SQLException");
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- After getting two ResultSets and then calling getMoreResults(Statement.CLOSE_ALL_RESULTS),
you should not be able to access the first ResultSet.
- Using a Statement
**/
    public void Var050()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;

		stmt_=connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		rs1 = stmt_.executeQuery("Call " + JDSetupProcedure.STP_RS3);
		rs1.next();
		stmt_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs2 = stmt_.getResultSet();
		rs2.next();
		stmt_.getMoreResults(Statement.CLOSE_ALL_RESULTS);
		rs3 = stmt_.getResultSet();
		try
		{
		    rs1.next();
		}
		catch (SQLException e)
		{
		    succeeded();
		    stmt_.close();
		    return;
		}
		rs1.close();
		stmt_.close();
		failed("Didn't throw SQLException rs3="+rs3);
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- After getting one ResultSet and then calling getMoreResults(Statement.CLOSE_ALL_RESULTS), you should
not be able to access the first ResultSet.
- Using a Statement
**/
    public void Var051()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;

		stmt_=connection_.createStatement();
		rs1 = stmt_.executeQuery("Call " + JDSetupProcedure.STP_RS3);
		stmt_.getMoreResults(Statement.CLOSE_ALL_RESULTS);
		rs2 = stmt_.getResultSet();
		try
		{
		    rs1.next();
		}
		catch (SQLException e)
		{
		    succeeded();
		    stmt_.close();
		    return;
		}
		stmt_.close();
		failed("Didn't throw SQLException rs2="+rs2);
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- After getting one ResultSet and then calling getMoreResults(Statement.CLOSE_ALL_RESULTS), you should
be able to access the second ResultSet.
- Using a Statement
**/
    public void Var052()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;

		stmt_=connection_.createStatement();
		rs1 = stmt_.executeQuery("Call " + JDSetupProcedure.STP_RS3);
		stmt_.getMoreResults(Statement.CLOSE_ALL_RESULTS);
		rs2 = stmt_.getResultSet();
		assertCondition( rs2.next() , "rs2.next returned false rs1="+rs1);
		rs2.close();
		stmt_.close();
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }
/**
getMoreResults()
getResultSet()
- After getting two ResultSets and then calling getMoreResults(Statement.CLOSE_ALL_RESULTS), you should
be able to access the third ResultSet.
- Using a Statement
**/
    public void Var053()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;

		stmt_=connection_.createStatement();
		rs1 = stmt_.executeQuery("Call " + JDSetupProcedure.STP_RS3);
		stmt_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs2 = stmt_.getResultSet();
		stmt_.getMoreResults(Statement.CLOSE_ALL_RESULTS);
		rs3 = stmt_.getResultSet();
                assertCondition( rs3.next(), "rs3.next returned false rs1="+rs1+" rs2="+rs2 );
		rs3.close();
		stmt_.close();

	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- After getting two ResultSets and then calling getMoreResults(Statement.CLOSE_ALL_RESULTS),
you should not be able to access the second ResultSet.
- Using a Statement
**/
    public void Var054()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;

		stmt_=connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		rs1 = stmt_.executeQuery("Call " + JDSetupProcedure.STP_RS3);
		rs1.next();
		stmt_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs2 = stmt_.getResultSet();
		rs2.next();
		stmt_.getMoreResults(Statement.CLOSE_ALL_RESULTS);
		rs3 = stmt_.getResultSet();
		try
		{
		    rs2.next();
		}
		catch (SQLException e)
		{
		    succeeded();
		    stmt_.close();
		    return;
		}
		stmt_.close();
		failed("Didn't throw SQLException rs3="+rs3);
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- After getting two ResultSets and then calling getMoreResults(Statement.CLOSE_ALL_RESULTS),
you should not be able to access the first ResultSet.
- Using a Statement
**/
    public void Var055()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;

		stmt_=connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		rs1 = stmt_.executeQuery("Call " + JDSetupProcedure.STP_RS3);
		rs1.next();
		stmt_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs2 = stmt_.getResultSet();
		rs2.next();
		rs1.next();      //should still be able to access first result set.
		rs1.getRow();
		stmt_.getMoreResults(Statement.CLOSE_ALL_RESULTS);
		rs3 = stmt_.getResultSet();
		try
		{
		    rs1.next();
		}
		catch (SQLException e)
		{
		    succeeded();
		    stmt_.close();
		    return;
		}
		stmt_.close();
		failed("Didn't throw SQLException rs3="+rs3);
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- Should be able to access a ResultSet that was kept open.
- Using a Statement
**/
    public void Var056()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;

		stmt_=connection_.createStatement();
		rs1 = stmt_.executeQuery("Call " + JDSetupProcedure.STP_RS3);
		stmt_.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
		rs2 = stmt_.getResultSet();
		stmt_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs3 = stmt_.getResultSet();
		assertCondition( rs2.next() && rs3.next(), "rs2.next or rs3.next returned false rs1="+rs1);
		rs2.close();
		rs3.close();
		stmt_.close();
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed ( e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- Should close all ResultSets when Statement.CLOSE_CURRENT_RESULT is used after all ResultSets
have been obtained.
- Using a Statement
**/
    public void Var057()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if(checkMultipleOpenResultSetSupport())
	{
	    try
	    {
		//This procedure only returns 3 ResultSets
		stmt_=connection_.createStatement();
		ResultSet rs1 = stmt_.executeQuery("Call " + JDSetupProcedure.STP_RS3);
		stmt_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		ResultSet rs2 = stmt_.getResultSet();
		stmt_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		ResultSet rs3 = stmt_.getResultSet();
		stmt_.getMoreResults(Statement.CLOSE_ALL_RESULTS);
		try
		{
		    rs3.next();
		}
		catch (SQLException e)
		{
		    succeeded();
		    stmt_.close();
		    return;
		}
		stmt_.close();
		failed("Didn't throw SQLException rs1="+rs1+" rs2="+rs2);
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- Should close all ResultSets when Statement.CLOSE_CURRENT_RESULT is used after all ResultSets
have been obtained.
- Using a Statement
**/
    public void Var058()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if(checkMultipleOpenResultSetSupport())
	{
	    try
	    {
		//This procedure only returns 3 ResultSets
		stmt_=connection_.createStatement();
		ResultSet rs1 = stmt_.executeQuery("Call " + JDSetupProcedure.STP_RS3);
		stmt_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		ResultSet rs2 = stmt_.getResultSet();
		stmt_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		ResultSet rs3 = stmt_.getResultSet();
		stmt_.getMoreResults(Statement.CLOSE_ALL_RESULTS);
		try
		{
		    rs2.next();
		}
		catch (SQLException e)
		{
		    succeeded();
		    stmt_.close();
		    return;
		}
		stmt_.close();
		failed("Didn't throw SQLException rs1="+rs1+" rs3="+rs3);
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- Should close all ResultSets when Statement.CLOSE_CURRENT_RESULT is used after all ResultSets
have been obtained.
- Using a Statement
**/
    public void Var059()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if(checkMultipleOpenResultSetSupport())
	{
	    try
	    {
		//This procedure only returns 3 ResultSets
		stmt_=connection_.createStatement();
		ResultSet rs1 = stmt_.executeQuery("Call " + JDSetupProcedure.STP_RS3);
		stmt_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		ResultSet rs2 = stmt_.getResultSet();
		stmt_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		ResultSet rs3 = stmt_.getResultSet();
		stmt_.getMoreResults(Statement.CLOSE_ALL_RESULTS);
		try
		{
		    rs1.next();
		}
		catch (SQLException e)
		{
		    succeeded();
		    stmt_.close();
		    return;
		}
		stmt_.close();
		failed("Didn't throw SQLException rs2="+rs2+" rs3="+rs3);
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- If Statement.KEEP_CURRENT_RESULT is set, you should be able to have
access to three result sets at the same time.
- Using a PreparedStatement
**/
    public void Var060()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;
		ps_=connection_.prepareStatement("Call " + JDSetupProcedure.STP_RS3, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		rs1 = ps_.executeQuery();
		boolean result = ps_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs2 = ps_.getResultSet();
		boolean result1 = ps_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs3 = ps_.getResultSet();
                boolean rs1next = rs1.next(); 
                boolean rs2next = rs2.next(); 
                boolean rs3next = rs3.next(); 
		assertCondition( rs1next && rs2next && rs3next && 
                    result == true && result1 == true, 
                    "All should be true but are "+
                    " rs1next="+rs1next+
                    " rs2next="+rs2next+
                    " rs3next="+rs3next+
                    " result="+result+
                    " result1="+result1    );
		rs1.close();
		rs2.close();
		rs3.close();
		ps_.close();
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed(e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- After closing one of the result sets, you should no longer be able to perform commands
on it.
- Using a PreparedStatement
**/
    public void Var061()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;
		ps_=connection_.prepareStatement("Call " + JDSetupProcedure.STP_RS3, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		rs1 = ps_.executeQuery();
		ps_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs2=ps_.getResultSet();
		ps_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs3=ps_.getResultSet();
		rs1.close();
		rs3.next();
		try
		{
		    rs1.next();
		}
		catch (SQLException e)
		{
		    succeeded();
		    ps_.close();
		    return;
		}
		ps_.close();
		failed("Didn't throw SQLException "+rs2);
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getResultSet()
getMoreResults()
- After closing a ResultSet you should still be able to access the other result sets.
- Using a PreparedStatement
**/
    public void Var062()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;
		ps_=connection_.prepareStatement("Call " + JDSetupProcedure.STP_RS3, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		rs1 = ps_.executeQuery();
		ps_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs2 = ps_.getResultSet();
		ps_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs3 = ps_.getResultSet();
		rs1.next();
		rs2.next();
		rs3.next();
		rs2.close();
                boolean rs1next = rs1.next(); 
                boolean rs3next = rs3.next(); 
		assertCondition( rs1next && rs3next , 
                    "rs1next="+rs1next+" rs3next = "+rs3next );
		rs1.close();
		rs3.close();
		ps_.close();
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed ( e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- After calling getMoreResults(Statement.CLOSE_CURRENT_RESULT) you shouldn't be able to access
the ResultSet obtained before the current one.
- Using a PreparedStatement
**/
    public void Var063()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;

		ps_=connection_.prepareStatement("Call " + JDSetupProcedure.STP_RS3, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		rs1=ps_.executeQuery();
		ps_.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
		rs2=ps_.getResultSet();
		rs2.next();
		try
		{
		    rs1.next();
		}
		catch (SQLException e)
		{
		    succeeded();
		    ps_.close();
		    return;
		}
		ps_.close();
		failed("Didn't throw SQLException");
	    }
	    catch( Exception e )
	    {
		failed (e, "Unexpected Exception");
	    }

	}
    }

/**
getMoreResults()
getResultSet()
- After getting two result sets and then calling getMoreResults(Statement.CLOSE_CURRENT_RESULT), you should
not be able to access the second result set.
- Using a PreparedStatement
**/
    public void Var064()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;

		ps_=connection_.prepareStatement("Call " + JDSetupProcedure.STP_RS3, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		rs1 = ps_.executeQuery();
		ps_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs2 = ps_.getResultSet();
		ps_.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
		rs3 = ps_.getResultSet();
		try
		{
		    rs2.next();
		}
		catch (SQLException e)
		{
		    succeeded();
		    ps_.close();
		    return;
		}
		ps_.close();
		failed("Didn't throw SQLException"+rs1+rs2+rs3);
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- After getting two result sets and then calling getMoreResults(Statement.CLOSE_CURRENT_RESULT), you should
be able to access the first and third result sets.
- Using a PreparedStatement
**/
    public void Var065()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;

		ps_=connection_.prepareStatement("Call " + JDSetupProcedure.STP_RS3, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		rs1 = ps_.executeQuery();
		ps_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs2 = ps_.getResultSet();
		ps_.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
		rs3 = ps_.getResultSet();
                boolean rs1next = rs1.next();
                boolean rs3next = rs3.next(); 
		assertCondition( rs1next && rs3next, "rs1next="+rs1next+" rs3next="+rs3next+"  rs2="+rs2);
		rs1.close();
		rs3.close();
		ps_.close();
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- After getting two result sets and then calling getMoreResults(Statement.CLOSE_CURRENT_RESULT), you should
be able to access the first and third result sets, but not the second.
- Using a PreparedStatement
**/
    public void Var066()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;

		ps_=connection_.prepareStatement("Call " + JDSetupProcedure.STP_RS3, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		rs1 = ps_.executeQuery();
		ps_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs2 = ps_.getResultSet();
		ps_.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
		rs3 = ps_.getResultSet();
		rs1.next();
		rs3.next();
		try
		{
		    rs2.next();
		}
		catch (SQLException e)
		{
		    succeeded();
		    ps_.close();
		    return;
		}
		ps_.close();
		failed("Didn't throw SQLException");
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- After getting two ResultSets and then calling getMoreResults(Statement.CLOSE_ALL_RESULTS),
you should not be able to access the first ResultSet.
- Using a PreparedStatement
**/
    public void Var067()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;

		ps_=connection_.prepareStatement("Call " + JDSetupProcedure.STP_RS3, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		rs1 = ps_.executeQuery();
		rs1.next();
		ps_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs2 = ps_.getResultSet();
		rs2.next();
		ps_.getMoreResults(Statement.CLOSE_ALL_RESULTS);
		rs3 = ps_.getResultSet();
		try
		{
		    rs1.next();
		}
		catch (SQLException e)
		{
		    succeeded();
                    rs3.close(); 
		    ps_.close();
		    return;
		}
		ps_.close();
		failed("Didn't throw SQLException");
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- After getting one ResultSet and then calling getMoreResults(Statement.CLOSE_ALL_RESULTS), you should
not be able to access the first ResultSet.
- Using a PreparedStatement
**/
    public void Var068()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;

		ps_=connection_.prepareStatement("Call " + JDSetupProcedure.STP_RS3);
		rs1 = ps_.executeQuery();
		ps_.getMoreResults(Statement.CLOSE_ALL_RESULTS);
		rs2 = ps_.getResultSet();
		try
		{
		    rs1.next();
		}
		catch (SQLException e)
		{
		    succeeded();
		    ps_.close();
		    return;
		}
                  rs2.close();
		ps_.close();
		failed("Didn't throw SQLException");
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- After getting one ResultSet and then calling getMoreResults(Statement.CLOSE_ALL_RESULTS), you should
be able to access the second ResultSet.
- Using a PreparedStatement
**/
    public void Var069()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;

		ps_=connection_.prepareStatement("Call " + JDSetupProcedure.STP_RS3);
		rs1 = ps_.executeQuery();
		ps_.getMoreResults(Statement.CLOSE_ALL_RESULTS);
		rs2 = ps_.getResultSet();
		assertCondition( rs2.next(), "rs2.next failed rs1="+rs1 );
		rs2.close();
		ps_.close();
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }
/**
getMoreResults()
getResultSet()
- After getting two ResultSets and then calling getMoreResults(Statement.CLOSE_ALL_RESULTS), you should
be able to access the third ResultSet.
- Using PreparedStatement
**/
    public void Var070()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;

		ps_=connection_.prepareStatement("Call " + JDSetupProcedure.STP_RS3);
		rs1 = ps_.executeQuery();
		ps_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs2 = ps_.getResultSet();
		ps_.getMoreResults(Statement.CLOSE_ALL_RESULTS);
		rs3 = ps_.getResultSet();
		assertCondition( rs3.next(), "rs3.next failed rs1="+rs1+" rs2="+rs2 );
		rs3.close();
		ps_.close();

	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- After getting two ResultSets and then calling getMoreResults(Statement.CLOSE_ALL_RESULTS),
you should not be able to access the second ResultSet.
- Using a PreparedStatement
**/
    public void Var071()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;

		ps_=connection_.prepareStatement("Call " + JDSetupProcedure.STP_RS3, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		rs1 = ps_.executeQuery();
		rs1.next();
		ps_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs2 = ps_.getResultSet();
		rs2.next();
		ps_.getMoreResults(Statement.CLOSE_ALL_RESULTS);
		rs3 = ps_.getResultSet();
		try
		{
		    rs2.next();
		}
		catch (SQLException e)
		{
		    succeeded();
		    ps_.close();
		    return;
		}
		ps_.close();
		failed("Didn't throw SQLException "+rs3);
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- After getting two ResultSets and then calling getMoreResults(Statement.CLOSE_ALL_RESULTS),
you should not be able to access the first ResultSet.
- Using a PreparedStatement
**/
    public void Var072()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;

		ps_=connection_.prepareStatement("Call " + JDSetupProcedure.STP_RS3, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		rs1 = ps_.executeQuery();
		rs1.next();
		ps_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs2 = ps_.getResultSet();
		rs2.next();
		rs1.next();      //should still be able to access first result set.
		rs1.getRow();
		ps_.getMoreResults(Statement.CLOSE_ALL_RESULTS);
		rs3 = ps_.getResultSet();
		try
		{
		    rs1.next();
		}
		catch (SQLException e)
		{
		    succeeded();
		    ps_.close();
		    return;
		}
		ps_.close();
		failed("Didn't throw SQLException "+rs3);
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- Should be able to access a ResultSet that was kept open.
- Using a PreparedStatement
**/
    public void Var073()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;

		ps_=connection_.prepareStatement("Call " + JDSetupProcedure.STP_RS3);
		rs1 = ps_.executeQuery();
		ps_.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
		rs2 = ps_.getResultSet();
		ps_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs3 = ps_.getResultSet();
		assertCondition( rs2.next() && rs3.next() , "rs2.next or rs3.next returned false rs1="+rs1);
		rs2.close();
		rs3.close();
		ps_.close();
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed ( e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- Should close all ResultSets when Statement.CLOSE_ALL_RESULTS is used after all ResultSets
have been obtained.
- Using a PreparedStatement
**/
    public void Var074()
    {

	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if(checkMultipleOpenResultSetSupport())
	{
	    try
	    {
		//This procedure only returns 3 ResultSets
		ps_=connection_.prepareStatement("Call " + JDSetupProcedure.STP_RS3);
		ResultSet rs1 = ps_.executeQuery();
		ps_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		ResultSet rs2 = ps_.getResultSet();
		ps_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		ResultSet rs3 = ps_.getResultSet();
		ps_.getMoreResults(Statement.CLOSE_ALL_RESULTS);
		try
		{
		    rs3.next();
		}
		catch (SQLException e)
		{
		    succeeded();
		    ps_.close();
		    return;
		}
		ps_.close();
		failed("Didn't throw SQLException "+rs1+" "+rs2);
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- Should close all ResultSets when Statement.CLOSE_CURRENT_RESULT is used after all ResultSets
have been obtained.
- Using a PreparedStatement
**/
    public void Var075()
    {

	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if(checkMultipleOpenResultSetSupport())
	{
	    try
	    {
		//This procedure only returns 3 ResultSets
		ps_=connection_.prepareStatement("Call " + JDSetupProcedure.STP_RS3);
		ResultSet rs1 = ps_.executeQuery();
		ps_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		ResultSet rs2 = ps_.getResultSet();
		ps_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		ResultSet rs3 = ps_.getResultSet();
		ps_.getMoreResults(Statement.CLOSE_ALL_RESULTS);
		try
		{
		    rs2.next();
		}
		catch (SQLException e)
		{
		    succeeded();
		    ps_.close();
		    return;
		}
		ps_.close();
		failed("Didn't throw SQLException "+rs1+" "+rs3);
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- Should close all ResultSets when Statement.CLOSE_CURRENT_RESULT is used after all ResultSets
have been obtained.
- Using a PreparedStatement
**/
    public void Var076()
    {

	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if(checkMultipleOpenResultSetSupport())
	{
	    try
	    {
		//This procedure only returns 3 ResultSets
		ps_=connection_.prepareStatement("Call " + JDSetupProcedure.STP_RS3);
		ResultSet rs1 = ps_.executeQuery();
		ps_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		ResultSet rs2 = ps_.getResultSet();
		ps_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		ResultSet rs3 = ps_.getResultSet();
		ps_.getMoreResults(Statement.CLOSE_ALL_RESULTS);
		try
		{
		    rs1.next();
		}
		catch (SQLException e)
		{
		    succeeded();
		    ps_.close();
		    return;
		}
		ps_.close();
		failed("Didn't throw SQLException "+rs2+" "+rs3);
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- If Statement.KEEP_CURRENT_RESULT is set, you should be able to have
access to three result sets at the same time.
- Using a CallableStatement
**/
    public void Var077()
    {

	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;
		cs_=connection_.prepareCall("Call " + JDSetupProcedure.STP_RS3, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		boolean retval = cs_.execute();
		rs1 = cs_.getResultSet();
		boolean result = cs_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs2 = cs_.getResultSet();
		boolean result1 = cs_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs3 = cs_.getResultSet();
                boolean rs1next = rs1.next(); 
                boolean rs2next = rs2.next(); 
                boolean rs3next = rs3.next(); 
                assertCondition( rs1next && rs2next && rs3next && 
                    result == true && result1 == true && retval == true, 
                    "All should be true but are "+
                    " rs1next="+rs1next+
                    " rs2next="+rs2next+
                    " rs3next="+rs3next+
                    " result="+result+
                    " result1="+result1+
                    " retval="+retval);
		rs1.close();
		rs2.close();
		rs3.close();
		cs_.close();
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed(e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- After closing one of the result sets, you should no longer be able to perform commands
on it.
- Using a CallableStatement
**/
    public void Var078()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;
		cs_=connection_.prepareCall("Call " + JDSetupProcedure.STP_RS3, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		cs_.execute();
		rs1 = cs_.getResultSet();
		cs_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs2=cs_.getResultSet();
		cs_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs3=cs_.getResultSet();
		rs1.close();
		rs3.next();
		try
		{
		    rs1.next();
		}
		catch (SQLException e)
		{
		    succeeded();
		    cs_.close();
		    return;
		}
		cs_.close();
		failed("Didn't throw SQLException "+rs2);
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getResultSet()
getMoreResults()
- After closing a ResultSet you should still be able to access the other result sets.
- Using a CallableStatement
**/
    public void Var079()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;
		cs_=connection_.prepareCall("Call " + JDSetupProcedure.STP_RS3, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		boolean retval = cs_.execute();
		rs1 = cs_.getResultSet();
		cs_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs2 = cs_.getResultSet();
		cs_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs3 = cs_.getResultSet();
		rs1.next();
		rs2.next();
		rs3.next();
		rs2.close();
                boolean rs1next = rs1.next(); 
                boolean rs3next = rs3.next(); 

		assertCondition( rs1next && rs3next && retval == true,
                    "rs1next="+rs1next+" rs3next="+rs3next+" retval="+retval);
		rs1.close();
		rs3.close();
		cs_.close();
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed ( e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- After calling getMoreResults(Statement.CLOSE_CURRENT_RESULT) you shouldn't be able to access
the ResultSet obtained before the current one.
- Using a CallableStatement
**/
    public void Var080()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;

		cs_=connection_.prepareCall("Call " + JDSetupProcedure.STP_RS3, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		cs_.execute();
		rs1 = cs_.getResultSet();
		cs_.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
		rs2=cs_.getResultSet();
		rs2.next();
		try
		{
		    rs1.next();
		}
		catch (SQLException e)
		{
		    succeeded();
		    cs_.close();
		    return;
		}
		cs_.close();
		failed("Didn't throw SQLException");
	    }
	    catch( Exception e )
	    {
		failed (e, "Unexpected Exception");
	    }

	}
    }

/**
getMoreResults()
getResultSet()
- After getting two result sets and then calling getMoreResults(Statement.CLOSE_CURRENT_RESULT), you should
not be able to access the second result set.
- Using a CallableStatement
**/
    public void Var081()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;

		cs_=connection_.prepareCall("Call " + JDSetupProcedure.STP_RS3, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		cs_.execute();
		rs1 = cs_.getResultSet();
		cs_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs2 = cs_.getResultSet();
		cs_.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
		rs3 = cs_.getResultSet();
		try
		{
		    rs2.next();
		}
		catch (SQLException e)
		{
		    succeeded();
		    cs_.close();
		    return;
		}
		cs_.close();
		failed("Didn't throw SQLException "+rs1+" "+rs3);
                
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- After getting two result sets and then calling getMoreResults(Statement.CLOSE_CURRENT_RESULT), you should
be able to access the first and third result sets.
- Using a CallableStatement
**/
    public void Var082()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;

		cs_=connection_.prepareCall("Call " + JDSetupProcedure.STP_RS3, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		boolean retval = cs_.execute();
		rs1 = cs_.getResultSet();
		cs_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs2 = cs_.getResultSet();
		cs_.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
		rs3 = cs_.getResultSet();
                boolean rs1next=rs1.next(); 
                boolean rs3next=rs3.next(); 
		assertCondition( rs1next && rs3next && retval == true, 
                    "rs1next="+rs1next+" rs3next="+rs3next+" retval="+retval+" rs2="+rs2);
		rs1.close();
		rs3.close();
		cs_.close();
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- After getting two result sets and then calling getMoreResults(Statement.CLOSE_CURRENT_RESULT), you should
be able to access the first and third result sets, but not the second.
- Using a CallableStatement
**/
    public void Var083()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;

		cs_=connection_.prepareCall("Call " + JDSetupProcedure.STP_RS3, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		cs_.execute();
		rs1 = cs_.getResultSet();
		cs_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs2 = cs_.getResultSet();
		cs_.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
		rs3 = cs_.getResultSet();
		rs1.next();
		rs3.next();
		try
		{
		    rs2.next();
		}
		catch (SQLException e)
		{
		    succeeded();
		    cs_.close();
		    return;
		}
		cs_.close();
		failed("Didn't throw SQLException");
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- After getting two ResultSets and then calling getMoreResults(Statement.CLOSE_ALL_RESULTS),
you should not be able to access the first ResultSet.
- Using a CallableStatement
**/
    public void Var084()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;

		cs_=connection_.prepareCall("Call " + JDSetupProcedure.STP_RS3, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		cs_.execute();
		rs1 = cs_.getResultSet();
		rs1.next();
		cs_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs2 = cs_.getResultSet();
		rs2.next();
		cs_.getMoreResults(Statement.CLOSE_ALL_RESULTS);
		rs3 = cs_.getResultSet();
		try
		{
		    rs1.next();
		}
		catch (SQLException e)
		{
		    succeeded();
		    cs_.close();
		    return;
		}
		cs_.close();
		failed("Didn't throw SQLException rs3="+rs3);
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- After getting one ResultSet and then calling getMoreResults(Statement.CLOSE_ALL_RESULTS), you should
not be able to access the first ResultSet.
- Using a CallableStatement
**/
    public void Var085()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;

		cs_=connection_.prepareCall("Call " + JDSetupProcedure.STP_RS3);
		cs_.execute();
		rs1 = cs_.getResultSet();
		cs_.getMoreResults(Statement.CLOSE_ALL_RESULTS);
		rs2 = cs_.getResultSet();
		try
		{
		    rs1.next();
		}
		catch (SQLException e)
		{
		    succeeded();
		    cs_.close();
		    return;
		}
		cs_.close();
		failed("Didn't throw SQLException rs2="+rs2);
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- After getting one ResultSet and then calling getMoreResults(Statement.CLOSE_ALL_RESULTS), you should
be able to access the second ResultSet.
- Using a CallableStatement
**/
    public void Var086()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;

		cs_=connection_.prepareCall("Call " + JDSetupProcedure.STP_RS3);
		boolean retval = cs_.execute();
		rs1 = cs_.getResultSet();
		cs_.getMoreResults(Statement.CLOSE_ALL_RESULTS);
		rs2 = cs_.getResultSet();
		assertCondition( rs2.next() && retval == true, "rs.next=false or retvalue=false rs1="+rs1 );
		rs2.close();
		cs_.close();
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }
/**
getMoreResults()
getResultSet()
- After getting two ResultSets and then calling getMoreResults(Statement.CLOSE_ALL_RESULTS), you should
be able to access the third ResultSet.
- Using a CallableStatement
**/
    public void Var087()
    {
	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;

		cs_=connection_.prepareCall("Call " + JDSetupProcedure.STP_RS3);
		boolean retval = cs_.execute();
		rs1 = cs_.getResultSet();
		cs_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs2 = cs_.getResultSet();
		cs_.getMoreResults(Statement.CLOSE_ALL_RESULTS);
		rs3 = cs_.getResultSet();
		assertCondition( rs3.next() && retval == true, "rs3.next=false or retvalue=false rs1="+rs1+" rs2="+rs2 );
		rs3.close();
		cs_.close();

	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- After getting two ResultSets and then calling getMoreResults(Statement.CLOSE_ALL_RESULTS),
you should not be able to access the second ResultSet.
- Using a CallableStatement
**/
    public void Var088()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;

		cs_=connection_.prepareCall("Call " + JDSetupProcedure.STP_RS3, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		cs_.execute();
		rs1 = cs_.getResultSet();
		rs1.next();
		cs_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs2 = cs_.getResultSet();
		rs2.next();
		cs_.getMoreResults(Statement.CLOSE_ALL_RESULTS);
		rs3 = cs_.getResultSet();
		try
		{
		    rs2.next();
		}
		catch (SQLException e)
		{
		    succeeded();
		    cs_.close();
		    return;
		}
		cs_.close();
		failed("Didn't throw SQLException "+rs3);
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- After getting two ResultSets and then calling getMoreResults(Statement.CLOSE_ALL_RESULTS),
you should not be able to access the first ResultSet.
- Using a CallableStatement
**/
    public void Var089()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;

		cs_=connection_.prepareCall("Call " + JDSetupProcedure.STP_RS3, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		cs_.execute();
		rs1 = cs_.getResultSet();
		rs1.next();
		cs_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs2 = cs_.getResultSet();
		rs2.next();
		rs1.next();      //should still be able to access first result set.
		rs1.getRow();
		cs_.getMoreResults(Statement.CLOSE_ALL_RESULTS);
		rs3 = cs_.getResultSet();
		try
		{
		    rs1.next();
		}
		catch (SQLException e)
		{
		    succeeded();
		    cs_.close();
		    return;
		}
		cs_.close();
		failed("Didn't throw SQLException "+rs3);
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- Should be able to access a ResultSet that was kept open.
- Using a CallableStatement
**/
    public void Var090()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;

		cs_=connection_.prepareCall("Call " + JDSetupProcedure.STP_RS3);
		boolean retval = cs_.execute();
		rs1 = cs_.getResultSet();
		cs_.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
		rs2 = cs_.getResultSet();
		cs_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs3 = cs_.getResultSet();
		assertCondition( rs2.next() && rs3.next() && retval == true,
                    "rs2.next=false or rs3.next=false or retval=false rs1="+rs1);
		rs2.close();
		rs3.close();
		cs_.close();
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed ( e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- Should close all ResultSets when Statement.CLOSE_ALL_RESULTS is used after all ResultSets
have been obtained.
- Using a CallableStatement
**/
    public void Var091()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if(checkMultipleOpenResultSetSupport())
	{
	    try
	    {
		//This procedure only returns 3 ResultSets
		cs_=connection_.prepareCall("Call " + JDSetupProcedure.STP_RS3);
		cs_.execute();
		ResultSet rs1 = cs_.getResultSet();
		cs_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		ResultSet rs2 = cs_.getResultSet();
		cs_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		ResultSet rs3 = cs_.getResultSet();
		cs_.getMoreResults(Statement.CLOSE_ALL_RESULTS);
		try
		{
		    rs3.next();
		}
		catch (SQLException e)
		{
		    succeeded();
		    cs_.close();
		    return;
		}
		cs_.close();
		failed("Didn't throw SQLException "+rs1+" "+rs2);
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- Should close all ResultSets when Statement.CLOSE_CURRENT_RESULT is used after all ResultSets
have been obtained.
- Using a CallableStatement
**/
    public void Var092()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if(checkMultipleOpenResultSetSupport())
	{
	    try
	    {
		//This procedure only returns 3 ResultSets
		cs_=connection_.prepareCall("Call " + JDSetupProcedure.STP_RS3);
		cs_.execute();
		ResultSet rs1 = cs_.getResultSet();
		cs_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		ResultSet rs2 = cs_.getResultSet();
		cs_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		ResultSet rs3 = cs_.getResultSet();
		cs_.getMoreResults(Statement.CLOSE_ALL_RESULTS);
		try
		{
		    rs2.next();
		}
		catch (SQLException e)
		{
		    succeeded();
		    cs_.close();
		    return;
		}
		cs_.close();
		failed("Didn't throw SQLException "+rs1+" "+rs3);
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getMoreResults()
getResultSet()
- Should close all ResultSets when Statement.CLOSE_CURRENT_RESULT is used after all ResultSets
have been obtained.
- Using a CallableStatement
**/
    public void Var093()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if(checkMultipleOpenResultSetSupport())
	{
	    try
	    {
		//This procedure only returns 3 ResultSets
		cs_=connection_.prepareCall("Call " + JDSetupProcedure.STP_RS3);
		cs_.execute();
		ResultSet rs1 = cs_.getResultSet();
		cs_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		ResultSet rs2 = cs_.getResultSet();
		cs_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		ResultSet rs3 = cs_.getResultSet();
		cs_.getMoreResults(Statement.CLOSE_ALL_RESULTS);
		try
		{
		    rs1.next();
		}
		catch (SQLException e)
		{
		    succeeded();
		    cs_.close();
		    return;
		}
		cs_.close();
		failed("Didn't throw SQLException "+rs2+" "+rs3);
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }



/**
 getMoreResults(CLOSE_CURRENT_RESULT) -  copy of var 13 
 **/
    public void Var094() {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if (checkMultipleOpenResultSetSupport()) {
	    try {
		stmt_ = connection_.createStatement ();
		stmt_.executeQuery ("CALL "
				+ JDSetupProcedure.STP_RS3);

		ResultSet rs1 = stmt_.getResultSet ();
		boolean success1 = rs1.next ();

		stmt_.getMoreResults (Statement.CLOSE_CURRENT_RESULT);
                boolean closed1 = isResultSetClosed(rs1); 
		ResultSet rs2 = stmt_.getResultSet ();
		boolean success2 = rs2.next ();

		stmt_.getMoreResults (Statement.CLOSE_CURRENT_RESULT);
	        boolean closed2 = isResultSetClosed(rs2); 
		ResultSet rs3 = stmt_.getResultSet ();
		boolean success3 = rs3.next ();

		stmt_.getMoreResults (Statement.CLOSE_CURRENT_RESULT);
		boolean closed3 = isResultSetClosed(rs3); 
		ResultSet rs4 = stmt_.getResultSet ();
		boolean success4 = (rs4 == null);



		stmt_.close ();
		boolean condition = success1 && success2 && success3 && success4 && closed1 && closed2 && closed3 ;
		if (!condition) {
		    output_.println("The following conditions should be true");
		    output_.println("success1 = "+success1);
		    output_.println("success2 = "+success2);
		    output_.println("success3 = "+success3);
		    output_.println("success4 = "+success4); 
		    output_.println("closed1 = "+closed1);
		    output_.println("closed2 = "+closed2);
		    output_.println("closed3 = "+closed3);
		}
		assertCondition (condition);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	} 
    } 
/**
getMoreResults(int)
getResultSet()
- all resultsets should be closed when executeQuery is called.
- test to make sure the first one is.
**/
    public void Var095()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;
		Statement s=connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		rs1 = s.executeQuery("Call " + JDSetupProcedure.STP_RS3);
		s.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs2 = s.getResultSet();
		s.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs3 = s.getResultSet();
		s.executeQuery("Call " + JDSetupProcedure.STP_RS3);
		try
		{
		    rs1.next();
		}
		catch (SQLException e)
		{
		    succeeded();
		    s.close();
		    return;
		}
		s.close();
		failed("Didn't throw SQLException "+rs2+" "+rs3);
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }
/**
getMoreResults(int)
getResultSet()
- all resultsets should be closed when executeQuery is called.
- test to make sure the second one is.
**/
    public void Var096()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;
		Statement s=connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		rs1 = s.executeQuery("Call " + JDSetupProcedure.STP_RS3);
		s.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs2 = s.getResultSet();
		s.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs3 = s.getResultSet();
		s.executeQuery("Call " + JDSetupProcedure.STP_RS3);
		try
		{
		    rs2.next();
		}
		catch (SQLException e)
		{
		    succeeded();
		    s.close();
		    return;
		}
		s.close();
		failed("Didn't throw SQLException "+rs1+" "+rs3);
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }
/**
getMoreResults(int)
getResultSet()
- all resultsets should be closed when executeQuery is called.
- test to make sure the third one is.
**/
    public void Var097()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
	    try
	    {
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;
		Statement s=connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		rs1 = s.executeQuery("Call " + JDSetupProcedure.STP_RS3);
		s.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs2 = s.getResultSet();
		s.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs3 = s.getResultSet();
		s.executeQuery("Call " + JDSetupProcedure.STP_RS3);
		try
		{
		    rs3.next();
		}
		catch (SQLException e)
		{
		    succeeded();
		    s.close();
		    return;
		}
		s.close();
		failed("Didn't throw SQLException "+rs1+" "+rs2);
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception");
	    }
	}
    }
/**
getMoreResults(int)
getResultSet()
- all resultsets should be closed when executeQuery is called.
- after s.close() is called all result sets should be closed.,
- there should not be any cursor already open exceptions.
**/
    public void Var098()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() )
	{
            ResultSet rs1 = null ;
            ResultSet rs2 = null ;
            ResultSet rs3 = null;
	    try
	    {
		Statement s=connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		rs1 = s.executeQuery("Call " + JDSetupProcedure.STP_RS3);
		s.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs2 = s.getResultSet();
		s.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs3 = s.getResultSet();
		s.close();
		s=connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		rs1 = s.executeQuery("Call " + JDSetupProcedure.STP_RS3);
		s.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs2 = s.getResultSet();
		s.getMoreResults(Statement.KEEP_CURRENT_RESULT);
		rs3 = s.getResultSet();
		succeeded();
		s.close();
	    }
	    catch( Exception e )
	    {
	        if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                succeeded();
            else
                failed (e, "Unexpected Exception rs1="+rs1+" rs2="+rs2+" rs3="+rs3);
	    }
	}
    }

/**
getMoreResults(int)
getResultSet()
- memory should not be leaked after calling get more results.
- The native driver is leaking 128 bytes per column
- Use 10 columns and repeat 10000 times.  This should point to a
- leak of at least 10 meg. 
- Running all resultsets should be closed when executeQuery is called.
- after s.close() is called all result sets should be closed.,
- there should not be any cursor already open exceptions.
-
- This was found by content manager in V5R3.
-
**/
    public void Var099()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if( checkMultipleOpenResultSetSupport() ) {
	    if (getDriver () == JDTestDriver.DRIVER_NATIVE ) {

		try {
		    System.gc(); 
		    int startMemKilobytes = JDJobName.getJobMem();


		    for (int i = 0 ; i < 10000; i++) {
			Statement stmt = connection_.createStatement();
			stmt.executeQuery("Select '1', '2', '3', '4', '5', '6', '7', '8', '9', '10' from qsys2.qsqptabl");
			stmt.getMoreResults();
			stmt.close();
			if (i % 300 == 0)
			    System.gc(); 
		    }



		    System.gc(); 
		    int endMemKilobytes = JDJobName.getJobMem();
		    assertCondition(endMemKilobytes - startMemKilobytes <= 10240,
				    "Memory leak occurred start K="+startMemKilobytes+" end K="+endMemKilobytes); 
		} catch( Exception e ) {
		    failed (e, "Unexpected Exception");
		}
	    } else {
		notApplicable("Native driver memory leak"); 
	    }

	} /* multiple rs */ 
    } /* var99 */ 
    
    
    
    /**
    getMoreResults()
    getResultSet()
    - After getting three ResultSets and then calling getMoreResults(Statement.CLOSE_ALL_RESULTS),
    you should not be able to access the result sets ResultSet.
    - Using a Statement
    **/
        public void Var100()
        {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

            String message=""; 
        if( checkMultipleOpenResultSetSupport() )
        {
            try
            {
                ResultSet rs1;
                ResultSet rs2;
                ResultSet rs3;

                stmt_=connection_.createStatement();
                rs1 = stmt_.executeQuery("Call " + JDSetupProcedure.STP_RS3);
                rs1.next();
                stmt_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
                rs2 = stmt_.getResultSet();
                stmt_.getMoreResults(Statement.KEEP_CURRENT_RESULT);
                rs2.next();
                rs3 = stmt_.getResultSet();
                //
                // Access all three 
                // 
                rs1.next(); 
                rs2.next(); 
                rs3.next(); 
                
                boolean rs1closed = false; 
                boolean rs2closed = false; 
                boolean rs3closed = false; 
                
                //
                // Make sure all are closed
                // 
                boolean rc  = stmt_.getMoreResults(Statement.CLOSE_ALL_RESULTS);
                if (rc) {
                  message+=" Error :getMoreResults resturned true"; 
                }
                try {
                  rs1.next();
                  message+=" Error rs1 is not closed ";
                } catch (SQLException e) {
                  rs1closed=true;
                }
                try {
                  rs2.next();
                  message+=" Error rs2 is not closed ";
                } catch (SQLException e) {
                  rs2closed=true;
                }
                try {
                  rs3.next();
                  message+=" Error rs3 is not closed ";
                } catch (SQLException e) {
                  rs3closed=true;
                }
                 
                assertCondition((rc==false) && rs1closed && rs2closed && rs3closed, message);     
                stmt_.close();
            }
            catch( Exception e )
            {
                if (isToolboxDriver() && e.getMessage().indexOf("not support") != -1)
                    succeeded();
                else
                    failed (e, "Unexpected Exception");
            }
        }
        }



/**
Is there a leak if getMoreResults is NOT called. 
getMoreResults(int)
getResultSet()
- memory should not be leaked after calling get more results.
- The native driver is leaking 128 bytes per column
- Use 10 columns and repeat 10000 times.  This should point to a
- leak of at least 10 meg. 
- Running all resultsets should be closed when executeQuery is called.
- after s.close() is called all result sets should be closed.,
- there should not be any cursor already open exceptions.
-
-
**/
    public void Var101()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	String added = " -- added by native 8/16/2006"; 
	if( checkMultipleOpenResultSetSupport() ) {
	    if (getDriver () == JDTestDriver.DRIVER_NATIVE ) {

		try {
		    System.gc(); 
		    int startMemKilobytes = JDJobName.getJobMem();

		    Statement stmt = connection_.createStatement();
		    for (int i = 0 ; i < 10000; i++) {
			ResultSet rs = stmt.executeQuery("Select '1', '2', '3', '4', '5', '6', '7', '8', '9', '10' from qsys2.qsqptabl");
			rs.next();
			for (int j = 1; j <= 10; j++) { 
			    rs.getString(j); 
			}
			if (i % 300 == 0)
			    System.gc(); 

		    }
		    stmt.close(); 
		  
		    System.gc(); 
		    int endMemKilobytes = JDJobName.getJobMem();
		    assertCondition(endMemKilobytes - startMemKilobytes <= 10240,
				    "Memory leak occurred start K="+startMemKilobytes+" end K="+endMemKilobytes+added); 
		} catch( Throwable e ) {
		    failed (e, "Unexpected Exception "+added);
		}
	    } else {
		notApplicable("Native driver memory leak"); 
	    }

	} /* multiple rs */ 
    } /* var99 */ 
    



/**
Is there a leak if getMoreResults is NOT called. Test when the number of columns increases.. 
getMoreResults(int)
getResultSet()
- memory should not be leaked after calling get more results.
- The native driver is leaking 128 bytes per column
- Use 10 columns and repeat 10000 times.  This should point to a
- leak of at least 10 meg. 
- Running all resultsets should be closed when executeQuery is called.
- after s.close() is called all result sets should be closed.,
- there should not be any cursor already open exceptions.
-
**/
    public void Var102()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	String added=" -- added by native driver 8/16/2006"; 
	String query = ""; 
	if( checkMultipleOpenResultSetSupport() ) {
	    if (getDriver () == JDTestDriver.DRIVER_NATIVE ) {

		try {
		    System.gc(); 
		    int startMemKilobytes = JDJobName.getJobMem();

		    for (int k = 0; k < 1; k++) { 
			Statement stmt = connection_.createStatement();
			for (int i = 0 ; i < 500; i++) {
			    StringBuffer sb = new StringBuffer();
			    sb.append("select "); 
			    int j = 0;
			    for (j = 0; j < i - 1; j++) {
				sb.append("'");
				sb.append(j);
				sb.append("',"); 
			    }
			    sb.append("'");
			    sb.append(j);
			    sb.append("' from qsys2.qsqptabl");

			    query = sb.toString(); 
			    ResultSet rs = stmt.executeQuery(query);
			    rs.next();
			    for (j = 1; j <= i-1; j++) {
				rs.getString(j); 
			    }

			    if (i % 10 == 0)
				System.gc(); 

			}
			stmt.close(); 

		    }
		    System.gc(); 
		    int endMemKilobytes = JDJobName.getJobMem();
		    assertCondition(endMemKilobytes - startMemKilobytes < 5000,
				    "Memory leak occurred start K="+startMemKilobytes+" end K="+endMemKilobytes+added); 
		} catch( Throwable e ) {
		    failed (e, "Unexpected Exception query = "+query+added );
		}
	    } else {
		notApplicable("Native driver memory leak"); 
	    }

	} /* multiple rs */ 
    } /* var99 */ 
    


/**
getResultSet() - Make sure we can call it many times after it is closed.
Websphere found a Null pointer exception coming from the native JDBC driver
with this test scenario.
*/
    public void Var103()
    {
	if (isProxy()) {
	    notApplicable("proxy doesn't support getMoreResults(int)");
	    return; 
	}

	if (isToolboxDriver()){
	    notApplicable("Make sure get more reuslts can be called many times after it is closed.  TODO.");
	    return;
	}
	if( checkMultipleOpenResultSetSupport() ) {

	    try {
		CallableStatement cs = connection_.prepareCall("CALL "
							       + JDSetupProcedure.STP_RS3);

		cs.execute();
		cs.getMoreResults(2);
		cs.execute(); 
		cs.getMoreResults(3);
		cs.getMoreResults(3);
		cs.getMoreResults(3);
		cs.getMoreResults(3);
		assertCondition (true);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception -- added 10/15/2007 by native driver to detect NPE found by Websphere.");
	    }
	}


    }


/**
getUpdateCount() - Should return the correct count when create table as select
is run.  See issue 46717.  Depends on the following SQ PTFs.
7.1  SI44489 SI44490
6.1  SI44487 SI44488
**/
    public void Var104()
    {
	if (checkRelease610()) {
	    String added = " -- getUpdateCount from create table from select added 11/7/2011\nSee issue 46717.  Depends on the following SQ PTFs.\n"+
	      "7.1  SI44489 SI44490\n"+
	      "6.1  SI44487 SI44488\n"; 
	    try {
		Statement s = connection_.createStatement ();
		String tableFrom = JDStatementTest.COLLECTION + ".JDSRFROM";
		String tableTo   = JDStatementTest.COLLECTION + ".JDSRTO";
		try {
		    s.executeUpdate("DROP TABLE "+tableFrom); 
		} catch (Exception e) {
		    if (e.toString().indexOf("not found") < 0) {
			e.printStackTrace(); 
		    }
		}
		try {
		    s.executeUpdate("DROP TABLE "+tableTo); 
		} catch (Exception e) {
		    if (e.toString().indexOf("not found") < 0) {
			e.printStackTrace();
		    }
		}

		s.executeUpdate("Create TABLE "+tableFrom+"(c1 int)");
		s.executeUpdate("insert into "+tableFrom+" values(1)");
		s.executeUpdate("insert into "+tableFrom+" values(2)");


		s.execute ("create table "+tableTo+" as (select * from "+tableFrom+") with data"); 
		int updateCount = s.getUpdateCount ();

		s.executeUpdate("DROP TABLE "+tableFrom);
		int dropUpdateCount = s.getUpdateCount(); 
		s.executeUpdate("DROP TABLE "+tableTo); 

		s.close ();


		assertCondition (updateCount == 2 && dropUpdateCount == 0 , "updateCount="+updateCount+" sb 2  dropUpdateCaount="+dropUpdateCount+" sb 0 "+added);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception "+added);
	    }
	}

    }




  /**
   * getLargeUpdateCount() - Call on a closed statement. Should throw an
   * exception.
   **/
  public void Var105() {
    if (checkJdbc42()) {
      try {
        Statement s = connection_.createStatement();
        s.close();
        long updateCount = JDReflectionUtil.callMethod_L(s,
            "getLargeUpdateCount");
        failed("Didn't throw SQLException updateCount=" + updateCount);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getLargeUpdateCount() - Should return -1 when nothing has been done with
   * the statement.
   **/
  public void Var106() {
    if (checkJdbc42()) {
      try {
        Statement s = connection_.createStatement();
        long updateCount = JDReflectionUtil.callMethod_L(s,
            "getLargeUpdateCount");
        s.close();
        assertCondition(updateCount == -1);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getLargeUpdateCount() - Should return 0 when an update was run that does
   * not update any rows using executeUpdate().
   **/
  public void Var107() {
    if (checkJdbc42()) {
      try {
        Statement s = connection_.createStatement();
        s.executeUpdate("UPDATE " + table_ + " SET SCORE=7 WHERE ID > 10");
        long updateCount = JDReflectionUtil.callMethod_L(s,
            "getLargeUpdateCount");
        s.close();
        assertCondition(updateCount == 0);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getLargeUpdateCount() - Should return 0 when an update was run that does
   * not update any rows using execute().
   **/
  public void Var108() {
    if (checkJdbc42()) {
      try {
        Statement s = connection_.createStatement();
        s.execute("UPDATE " + table_ + " SET SCORE=7 WHERE ID > 10");
        long updateCount = JDReflectionUtil.callMethod_L(s,
            "getLargeUpdateCount");
        s.close();
        assertCondition(updateCount == 0, "update count is " + updateCount
            + " sb 0");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getLargeUpdateCount() - Should return the correct count when an update was
   * run that does update rows using executeUpdate().
   **/
  public void Var109() {
    if (checkJdbc42()) {
      try {
        Statement s = connection_.createStatement();
        s.executeUpdate("UPDATE " + table_ + " SET SCORE=7 WHERE ID > 1");
        long updateCount = JDReflectionUtil.callMethod_L(s,
            "getLargeUpdateCount");
        s.close();
        assertCondition(updateCount == 2, "update count = " + updateCount
            + " should be 2 ");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getLargeUpdateCount() - Should return the correct count when an update was
   * run that does update rows using execute().
   **/
  public void Var110() {
    if (checkJdbc42()) {
      try {
        Statement s = connection_.createStatement();
        s.execute("UPDATE " + table_ + " SET SCORE=7 WHERE ID > 1");
        long updateCount = JDReflectionUtil.callMethod_L(s,
            "getLargeUpdateCount");
        s.close();
        assertCondition(updateCount == 2, "updateCount=" + updateCount
            + " sb 2");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getLargeUpdateCount() - Should return 0 when a stored procedure with no
   * result sets was run using executeUpdate().
   **/
  public void Var111() {
    if (checkJdbc42()) {
      try {
        Statement s = connection_.createStatement();
        s.executeUpdate("CALL " + JDSetupProcedure.STP_RS0);
        long updateCount = JDReflectionUtil.callMethod_L(s,
            "getLargeUpdateCount");
        s.close();
        assertCondition(updateCount == 0);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getLargeUpdateCount() - Should return 0 when a stored procedure with no
   * result sets was run using execute().
   **/
  public void Var112() {
    if (checkJdbc42()) {
      try {
        Statement s = connection_.createStatement();
        s.execute("CALL " + JDSetupProcedure.STP_RS0);
        long updateCount = JDReflectionUtil.callMethod_L(s,
            "getLargeUpdateCount");
        s.close();
        assertCondition(updateCount == 0);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getLargeUpdateCount() - Should return -1 when a query is run using
   * executeQuery().
   **/
  public void Var113() {
    if (checkJdbc42()) {
      try {
        Statement s = connection_.createStatement();
        s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
        long updateCount = JDReflectionUtil.callMethod_L(s,
            "getLargeUpdateCount");
        s.close();
        assertCondition(updateCount == -1, "update count from select is "
            + updateCount + " sb -1");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getLargeUpdateCount() - Should return -1 when a query is run using
   * execute().
   **/
  public void Var114() {
    if (checkJdbc42()) {
      try {
        Statement s = connection_.createStatement();
        s.execute("SELECT * FROM QIWS.QCUSTCDT");
        long updateCount = JDReflectionUtil.callMethod_L(s,
            "getLargeUpdateCount");
        s.close();
        assertCondition(updateCount == -1, "update count from select is "
            + updateCount + " sb -1");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getLargeUpdateCount() - Should return -1 when a stored procedure with 1
   * result set was run using executeQuery().
   **/
  public void Var115() {
    if (checkJdbc42()) {
      try {
        Statement s = connection_.createStatement();
        s.executeQuery("CALL " + JDSetupProcedure.STP_RS1);
        long updateCount = JDReflectionUtil.callMethod_L(s,
            "getLargeUpdateCount");
        s.close();
        assertCondition(updateCount == -1, "Stored procedure "
            + JDSetupProcedure.STP_RS1 + " update count is " + updateCount);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getLargeUpdateCount() - Should return -1 when a stored procedure with 1
   * result set was run using execute().
   **/
  public void Var116() {
    if (checkJdbc42()) {
      try {
        Statement s = connection_.createStatement();
        s.execute("CALL " + JDSetupProcedure.STP_RS1);
        long updateCount = JDReflectionUtil.callMethod_L(s,
            "getLargeUpdateCount");
        s.close();
        assertCondition(updateCount == -1, "Stored procedure "
            + JDSetupProcedure.STP_RS1 + " update count is " + updateCount);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getLargeUpdateCount() - Should return -1 when a stored procedure with 3
   * result sets was run usgin executeQuery().
   **/
  public void Var117() {
    if (checkJdbc42()) {
      try {
        Statement s = connection_.createStatement();
        s.executeQuery("CALL " + JDSetupProcedure.STP_RS3);
        long updateCount = JDReflectionUtil.callMethod_L(s,
            "getLargeUpdateCount");
        s.close();
        assertCondition(updateCount == -1, "Stored procedure "
            + JDSetupProcedure.STP_RS1 + " update count is " + updateCount);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

  }

  /**
   * getLargeUpdateCount() - Should return -1 when a stored procedure with 3
   * result sets was run usgin execute().
   **/
  public void Var118() {
    if (checkJdbc42()) {
      try {
        Statement s = connection_.createStatement();
        s.execute("CALL " + JDSetupProcedure.STP_RS3);
        long updateCount = JDReflectionUtil.callMethod_L(s,
            "getLargeUpdateCount");
        s.close();
        assertCondition(updateCount == -1, "Stored procedure "
            + JDSetupProcedure.STP_RS1 + " update count is " + updateCount);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getLargeUpdateCount() - Should return -1 when an update was run that does
   * update rows using executeUpdate() and getMoreResults() is called.
   **/
  public void Var119() {
    if (checkJdbc42()) {
      try {
        Statement s = connection_.createStatement();
        s.executeUpdate("UPDATE " + table_ + " SET SCORE=7 WHERE ID > 1");
        s.getMoreResults();
        long updateCount = JDReflectionUtil.callMethod_L(s,
            "getLargeUpdateCount");
        s.close();
        assertCondition(updateCount == -1);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

  }

  /**
   * getLargeUpdateCount() - Should return -1 when an update was run that does
   * update rows using execute() and getMoreResults() is called.
   **/
  public void Var120() {
    if (checkJdbc42()) {
      try {
        Statement s = connection_.createStatement();
        s.execute("UPDATE " + table_ + " SET SCORE=7 WHERE ID > 1");
        s.getMoreResults();
        long updateCount = JDReflectionUtil.callMethod_L(s,
            "getLargeUpdateCount");
        s.close();
        assertCondition(updateCount == -1);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getLargeUpdateCount() - Should return the correct count when create table
   * as select is run. See issue 46717. Depends on the following SQ PTFs. 7.1
   * SI44489 SI44490 6.1 SI44487 SI44488
   **/
  public void Var121() {
    if (checkJdbc42()) {
      if (checkRelease610()) {
        String added = " -- getLargeUpdateCount from create table from select added 11/7/2011\nSee issue 46717.  Depends on the following SQ PTFs.\n"
            + "7.1  SI44489 SI44490\n" + "6.1  SI44487 SI44488\n";
        try {
          Statement s = connection_.createStatement();
          String tableFrom = JDStatementTest.COLLECTION + ".JDSRFROM";
          String tableTo = JDStatementTest.COLLECTION + ".JDSRTO";
          try {
            s.executeUpdate("DROP TABLE " + tableFrom);
          } catch (Exception e) {
            if (e.toString().indexOf("not found") < 0) {
              e.printStackTrace();
            }
          }
          try {
            s.executeUpdate("DROP TABLE " + tableTo);
          } catch (Exception e) {
            if (e.toString().indexOf("not found") < 0) {
              e.printStackTrace();
            }
          }

          s.executeUpdate("Create TABLE " + tableFrom + "(c1 int)");
          s.executeUpdate("insert into " + tableFrom + " values(1)");
          s.executeUpdate("insert into " + tableFrom + " values(2)");

          s.execute("create table " + tableTo + " as (select * from "
              + tableFrom + ") with data");
          long updateCount = JDReflectionUtil.callMethod_L(s,
              "getLargeUpdateCount");

          s.executeUpdate("DROP TABLE " + tableFrom);
          long dropUpdateCount = JDReflectionUtil.callMethod_L(s,
              "getLargeUpdateCount");
          s.executeUpdate("DROP TABLE " + tableTo);

          s.close();

          assertCondition(updateCount == 2 && dropUpdateCount == 0,
              "updateCount=" + updateCount + " sb 2  dropUpdateCaount="
                  + dropUpdateCount + " sb 0 " + added);
        } catch (Exception e) {
          failed(e, "Unexpected Exception " + added);
        }
      }

    }

  }

}



