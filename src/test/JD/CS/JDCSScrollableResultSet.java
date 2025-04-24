///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSScrollableResultSet.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSScrollableResultSet.java
//
// Classes:      JDCSScrollableResultSet
//
////////////////////////////////////////////////////////////////////////
package test.JD.CS;

import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDSetupProcedure;
import test.JDTestcase;



/**
Testcase JDCSScrollableResultSet.  This tests the use of a scrollable result set returned by
a stored procedure in the JDBC CallableStatement class.
Scrollable Stored Procedure Result Sets are support in release V5R3 and higher.
**/
public class JDCSScrollableResultSet
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSScrollableResultSet";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCSTest.main(newArgs); 
   }


    // Private data.
    private Connection      connection_;



/**
Constructor.
**/
    public JDCSScrollableResultSet(AS400 systemObject,
				   Hashtable<String,Vector<String>> namesAndVars,
				   int runMode,
				   FileOutputStream fileOutputStream,
				   
				   String password)
    {
	super(systemObject, "JDCSScrollableResultSet",
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
	connection_ = testDriver_.getConnection (baseURL_ + ";errors=full", 
						 userId_, encryptedPassword_);
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
Check that you can scroll forward and backwards by using absolute positioning;
Should throw an exception for <= V5R2
**/
    public void Var001()
    {
	if (checkJdbc20 ())  {	
	    try {
		CallableStatement cs = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSSRS, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = cs.executeQuery();
		rs.beforeFirst();
		rs.absolute(3);
		    String name3 = rs.getString("LSTNAM").trim();
		    rs.absolute(1);
		    String name1 = rs.getString("LSTNAM").trim();
		    assertCondition(name3.equals("Doe") && name1.equals("Abraham"), "name3 should be Doe but is " + name3 + ", name1 should be Abraham but is " + name1 + " Added by Toolbox 10/20/2003");
	    }
	    catch (Exception e) {
		    failed (e, "Unexpected Exception - Added by Toolbox 10/20/2003");
	    }
	}
    }

/**
Check that you can scroll forward and backwards by using relative positioning;
Should throw an exception if server is less than V5R3
**/
    public void Var002()
    {
	if (checkJdbc20 ())  {	
	    try {
		CallableStatement cs = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSSRS, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = cs.executeQuery();
		rs.next();          //position to first row
		rs.relative(3);
		    String name3 = rs.getString("LSTNAM").trim();
		    rs.relative(1);
		    String name4 = rs.getString("LSTNAM").trim();
		    assertCondition(name3.equals("Henning") && name4.equals("Johnson"), "name3 should be Henning but is " + name3 + ", name4 should be Johnson but is " + name4 + " Added by Toolbox 10/20/2003");
	    }
	    catch (Exception e) {
		    failed (e, "Unexpected Exception - Added by Toolbox 10/20/2003");
	    }
	} 

    }

/**
Check that you can scroll forward and backwards
rs.next()
rs.previous()
Should throw an exception if server is less than V5R3
**/
    public void Var003()
    {
	if (checkJdbc20 ())  {	
	    try {
		CallableStatement cs = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSSRS, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = cs.executeQuery();
		rs.next();
		String name1 = rs.getString("LSTNAM").trim();
		rs.next();
		String name2 = rs.getString("LSTNAM").trim();
		rs.previous();
		    String name3 = rs.getString("LSTNAM").trim();
		    assertCondition(name3.trim().equals("Abraham") && name1.trim().equals("Abraham") && name2.trim().equals("Alison"), "name3 should be Abraham but is " + name3 + ", name1 should be Abraham but is " + name1 + ", name2 should be Alison but is " + name2 + " Added by Toolbox 10/20/2003");
	    }
	    catch (Exception e) {
		    failed (e, "Unexpected Exception - Added by Toolbox 10/20/2003");
	    }
	} 

    }


/**
Scroll to the end, check you can scroll back up;
Should throw an exception if server is less than V5R3
**/
    public void Var004()
    {
	if (checkJdbc20 ())  {	
	    try {
		CallableStatement cs = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSSRS, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = cs.executeQuery();
		while(rs.next())
		{
		}
		rs.previous();

		    String last = rs.getString("LSTNAM").trim();
		    assertCondition(last.equals("Williams"), "last should be Williams but is " + last + " Added by Toolbox 10/20/2003");
		
	    }
	    catch (Exception e) {
		    failed (e, "Unexpected Exception - Added by Toolbox 10/20/2003");
	    }
	} 

    }

/**
Scroll to the end, check you can scroll back up;
Should throw an exception if server is less than V5R3
**/
    public void Var005()
    {
	if (checkJdbc20 ())  {	
	    try {
		CallableStatement cs = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSSRS, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = cs.executeQuery();
		rs.afterLast();
		    rs.previous();
		    String last = rs.getString("LSTNAM").trim();
		    assertCondition(last.equals("Williams"), "last should be Williams but is " + last + " Added by Toolbox 10/20/2003");
	    }
	    catch (Exception e) {
		    failed (e, "Unexpected Exception - Added by Toolbox 10/20/2003");
	    }
	} 
    }

/**
Scroll to the end, check you can scroll back up;
Should throw an exception if server is less than V5R3
**/
    public void Var006()
    {
	if (checkJdbc20 ())  {	
	    try {
		CallableStatement cs = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSSRS, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = cs.executeQuery();
		rs.absolute(12);
		    rs.next();
		    rs.previous();
		    String last = rs.getString("LSTNAM").trim();
		    assertCondition(last.equals("Williams"), "last should be Williams but is " + last + " Added by Toolbox 10/20/2003");
	    }
	    catch (Exception e) {
		    failed (e, "Unexpected Exception - Added by Toolbox 10/20/2003");
	    }
	} 

    }

/** 
Scroll to the end and then all the way back up.
Should throw an exception if server is less than V5R3
**/
    public void Var007()
    {
	if (checkJdbc20 ())  {	
	    try {
		CallableStatement cs = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSSRS, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = cs.executeQuery();
		while(rs.next())
		{
		}
		while(rs.previous()){
		}
		    rs.next();
		    String first = rs.getString("LSTNAM").trim();
		    assertCondition(first.equals("Abraham"), "first should be Abraham but is " + first + " Added by Toolbox 10/20/2003");
	    }
	    catch (Exception e) {
		    failed (e, "Unexpected Exception - Added by Toolbox 10/20/2003");
	    }
	} 
    }

/**
Check that you can scroll forward and backwards by using absolute positioning;
Multiple result sets are returned
Should throw an exception for <= V5R2
**/
    public void Var008()
    {
	if (checkJdbc20 ())  {	
	    try {
		CallableStatement cs = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSMSRS, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		boolean results = cs.execute();
		String name3 = null;
		String name1 = null;
		boolean moreResults = false;
		boolean failDone=false; 
		if(results)
		{
		    ResultSet rs = cs.getResultSet();
		    moreResults = cs.getMoreResults();
		    if(moreResults)
		    {
			ResultSet rs1 = cs.getResultSet();
			rs1.absolute(3);
			    name3 = rs1.getString("LSTNAM").trim();
			    rs1.absolute(1);
			    name1 = rs1.getString("LSTNAM").trim();
			rs1.close();
		    }
		    rs.close();
		}
		if (!failDone) { 
		    assertCondition(results && moreResults && (name3 != null) && name3.equals("Doe") && (name1 != null) && name1.equals("Abraham"), "name3 should be Doe but is " + name3 + ", name1 should be Abraham but is " + name1 + " Added by Toolbox 10/20/2003");
		}
	    }
	    catch (Exception e) {
		    failed (e, "Unexpected Exception - Added by Toolbox 10/20/2003");
	    }
	} 
    }

/**
Check that you can scroll forward and backwards by using relative positioning;
Multiple Result sets are returned
Should throw an exception if server is less than V5R3
**/
    public void Var009()
    {
        ResultSet rs =null; 
	if (checkJdbc20 ())  {	
	    try {
		CallableStatement cs = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSMSRS, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		boolean results = cs.execute();
		String name3 = null;
		String name4 = null;
		boolean moreResults = false;
		boolean failDone=false; 
		if(results)
		{
		    rs = cs.getResultSet();
		    moreResults = cs.getMoreResults();
		    if(moreResults)
		    {
			ResultSet rs1 = cs.getResultSet();
			rs1.next();          //position to first row
			rs1.relative(3);
			    name3 = rs1.getString("LSTNAM").trim();
			    rs1.relative(1);
			    name4 = rs1.getString("LSTNAM").trim();
		    }
		}
		if (!failDone) { 
		    assertCondition(results && moreResults && (name3 != null) && name3.equals("Henning") && (name4 != null) && name4.equals("Johnson"), "name3 should be Henning but is " + name3 + ", name4 should be Johnson but is " + name4 + " Added by Toolbox 10/20/2003"+ rs );
		}
	    }
	    catch (Exception e) {
		    failed (e, "Unexpected Exception - Added by Toolbox 10/20/2003");
	    }
	} 
    }
/**
Check that you can scroll forward and backwards
multiple result sets returned
rs.next()
rs.previous()
Should throw an exception if server is less than V5R3
**/
    public void Var010()
    {
	if (checkJdbc20 ())  {	
	    try {
		CallableStatement cs = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSMSRS, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		String name1 = null;
		String name2 = null;
		String name3 = null;
		ResultSet rs = null; 
		boolean results = cs.execute();
		boolean moreResults = false;
		boolean failDone =false; 
		if(results)
		{
		    rs = cs.getResultSet();
		    moreResults = cs.getMoreResults();
		    if(moreResults)
		    {
			ResultSet rs1 = cs.getResultSet();
			rs1.next();
			name1 = rs1.getString("LSTNAM").trim();
			rs1.next();
			name2 = rs1.getString("LSTNAM").trim();
			rs1.previous();
			    name3 = rs1.getString("LSTNAM").trim();
		    }
		}
		if (!failDone) { 
		    assertCondition((rs != null) && results && moreResults && (name3 != null) && name3.trim().equals("Abraham") && (name1 != null) && name1.trim().equals("Abraham") && (name2 != null) && name2.trim().equals("Alison"), "name3 should be Abraham but is " + name3 + ", name1 should be Abraham but is " + name1 + ", name2 should be Alison but is " + name2 + " Added by Toolbox 10/20/2003");
		}
	    }
	    catch (Exception e) {
		    failed (e, "Unexpected Exception - Added by Toolbox 10/20/2003");
	    }
	} 
    }

/**
Scroll to the end, check you can scroll back up;
Multiple Result Sets returned
Should throw an exception if server is less than V5R3
**/
    public void Var011()
    {
	if (checkJdbc20 ())  {	
	    try {
		CallableStatement cs = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSMSRS, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		String last = null;
		boolean results = cs.execute();
		boolean moreResults = false;
		boolean failDone=false; 
		ResultSet rs1 = null; 
		if(results)
		{
		    rs1 = cs.getResultSet();
		    moreResults = cs.getMoreResults();
		    if(moreResults)
		    {
			ResultSet rs = cs.getResultSet();
			while(rs.next())
			{
			}
			rs.previous();
			    last = rs.getString("LSTNAM").trim();
		    }
		}
		if (!failDone) { 
		    assertCondition((rs1 != null) && results && moreResults && (last != null) && last.equals("Williams"), "last should be Williams but is " + last + " Added by Toolbox 10/20/2003");
		}
	    }
	    catch (Exception e) {
		    failed (e, "Unexpected Exception - Added by Toolbox 10/20/2003");
	    }
	} 
    }

/**
Scroll to the end, check you can scroll back up;
Multiple Result sets returned
Should throw an exception if server is less than V5R3
**/
    public void Var012()
    {
	if (checkJdbc20 ())  {	
	    try {
		CallableStatement cs = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSMSRS, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		String last = null;
		boolean moreResults = false;
		boolean results = cs.execute();
		boolean failDone = false; 
		ResultSet rs1 = null; 
		
		if(results)
		{
		    rs1 = cs.getResultSet();
		    moreResults = cs.getMoreResults();
		    if(moreResults)
		    {
			ResultSet rs = cs.getResultSet();
			rs.afterLast();
			    rs.previous();
			    last = rs.getString("LSTNAM").trim();
		    }
		}
		if (!failDone) { 
		    assertCondition((rs1 != null) &&results && moreResults && (last != null) && last.equals("Williams"), "last should be Williams but is " + last + " Added by Toolbox 10/20/2003");
		}
	    }
	    catch (Exception e) {
		    failed (e, "Unexpected Exception - Added by Toolbox 10/20/2003");
	    }
	} 
    }

/**
Scroll to the end, check you can scroll back up;
Multiple ResultSets returned
Should throw an exception if server is less than V5R3
**/
    public void Var013()
    {
	if (checkJdbc20 ())  {	
	    try {
		CallableStatement cs = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSMSRS, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		String last = null;
		boolean moreResults = false;
		boolean results = cs.execute();
                ResultSet rs1 = null; 
		if(results)
		{
		    rs1 = cs.getResultSet();
		    moreResults = cs.getMoreResults();
		    if (moreResults)
		    {
			ResultSet rs = cs.getResultSet();
			rs.absolute(12);
			    rs.next();
			    rs.previous();
			    last = rs.getString("LSTNAM").trim();
		    }
		}
		    assertCondition((rs1 != null) &&results && moreResults && (last != null) && last.equals("Williams"), "last should be Williams but is " + last + " Added by Toolbox 10/20/2003");
	    }
	    catch (Exception e) {
		    failed (e, "Unexpected Exception - Added by Toolbox 10/20/2003");
	    }
	} 
    }

/** 
Scroll to the end and then all the way back up.
Multiple Result Sets returned
Should throw an exception if server is less than V5R3
**/
    public void Var014()
    {
	if (checkJdbc20 ())  {	
	    try {
		CallableStatement cs = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSMSRS, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		String first = null;
		boolean moreResults = false;
		boolean results = cs.execute();
		boolean failDone=false; 
                ResultSet rs1 = null; 
		if(results)
		{
		    rs1 = cs.getResultSet();
		    moreResults = cs.getMoreResults();
		    if(moreResults)
		    {
			ResultSet rs = cs.getResultSet();
			while(rs.next())
			{
			}
			while(rs.previous()){
			}
			    rs.next();
			    first = rs.getString("LSTNAM").trim();
		    }
		}
		if (!failDone) { 
		    assertCondition((rs1 != null) &&results && moreResults && (first != null) && first.equals("Abraham"), "first should be Abraham but is " + first + " Added by Toolbox 10/20/2003");
		}
	    }
	    catch (Exception e) {
		    failed (e, "Unexpected Exception - Added by Toolbox 10/20/2003");
	    }
	} 

    }

    /**
Check that you cannot scroll forward and backwards when the result set is TYPE_FORWARD_ONLY
In V5R5 and later, both drivers correctly detect this is really a scrollable cursor.

In Feb 2011 this was changed.  If a forward only was requested, we will only allow forward only operations. 
**/
	public void Var015()
	{
	    if (checkJdbc20 ())  {	
		try {
		    CallableStatement cs = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSSRS, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		    ResultSet rs = cs.executeQuery();
		    rs.next();
		    rs.next();
		    rs.previous();
		    failed("Didn't throw SQLException Should throw exception for declared FORWARD_ONLY cursor and attempting to scroll backwards - Updated 2/18/2011.");
		}
		catch (Exception e) {
		    assertExceptionIsInstanceOf(e, "java.sql.SQLException");
		}

	    } 
	}


/*
 * Test suggested by John Broich Decemeber 2010 to test problem fixed for DST
 */


    public void Var016()
    {
	boolean passed =true;
	StringBuffer sb = new StringBuffer(); 
	String added = " -- Added 12/23/201 to test problem fixed for DST CPS 8BGR67";
	String sql = "";

	if (checkJdbc20 ())  {	
	    try {
		/* Do the setup for the procedures first */

		String [] drops = {
		    "DROP PROCEDURE "+collection_+".CSSRS",
		    "DROP PROCEDURE "+collection_+".CSSRS2",
		    "DROP PROCEDURE "+collection_+".CSSRS3",
		};
		String [] creates = {  "CREATE PROCEDURE "+collection_+".CSSRS"                                                         
		  + " RESULT SET 1 LANGUAGE SQL READS SQL DATA SPECIFIC "+collection_+".CSSRS"                     
		  + " BEGIN "                                                                       
		  + "   DECLARE C1 SCROLL CURSOR FOR SELECT * FROM QIWS.QCUSTCDT ORDER BY LSTNAM;"                  
		  + "   OPEN C1;"                                                                   
		  + "   SET RESULT SETS CURSOR C1;"                                                 
		  + " END  ", 
		"CREATE PROCEDURE "+collection_+".CSSRS2"                                                         
		  + " RESULT SET 1 LANGUAGE SQL READS SQL DATA SPECIFIC "+collection_+".CSSRS2"                     
		  + " BEGIN "                                                                       
		  + "   DECLARE C1 SCROLL CURSOR FOR SELECT * FROM QIWS.QCUSTCDT ORDER BY LSTNAM;"                  
		  + "   OPEN C1;"                                                                   
		  + "   SET RESULT SETS CURSOR C1;"                                                 
		  + " END  ",
		"CREATE PROCEDURE "+collection_+".CSSRS3"                                                         
		  + " RESULT SET 1 LANGUAGE SQL READS SQL DATA SPECIFIC "+collection_+".CSSRS3"                     
		  + " BEGIN "                                                                       
		  + "   DECLARE C1 SCROLL CURSOR FOR SELECT * FROM QIWS.QCUSTCDT ORDER BY LSTNAM;"                  
		  + "   OPEN C1;"                                                                   
		  + "   SET RESULT SETS CURSOR C1;"                                                 
		+ " END  " };
		

		Statement s = connection_.createStatement();
		for (int i = 0; i < drops.length; i++) {
		    try {
			sql = drops[i]; 
			s.executeUpdate(sql); 
		    } catch (Exception e) {
		    } 
		}

		for (int i = 0; i < creates.length; i++) {
		    sql = creates[i]; 
		    s.executeUpdate(sql);
		}
		s.close();
		connection_.commit(); 



		Connection c = testDriver_.getConnection (baseURL_ + ";errors=full", 
						 userId_, encryptedPassword_);
		Connection c2 = testDriver_.getConnection (baseURL_ + ";errors=full", 
						 userId_, encryptedPassword_);
		Connection c3 = testDriver_.getConnection (baseURL_ + ";errors=full", 
						 userId_, encryptedPassword_);
		sql = "CALL "+collection_+".CSSRS";
		CallableStatement cs = c.prepareCall(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		sql = "CALL "+collection_+".CSSRS2"; 
		CallableStatement cs2 = c2.prepareCall(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		sql = "CALL "+collection_+".CSSRS3"; 
		CallableStatement cs3 = c3.prepareCall(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

		// Open the result set cursors in an order different that the order in which they are closed, so that the last connection 
		// that is referenced is not the same as the connection being referenced for the current statement.
		    for (int i=0;i<500;i++)
		    {
			sb.append("Loop # " + i+"\n");
			sql="executeQuery on CSSRS"; 
			ResultSet rs = cs.executeQuery();
                        sql="executeQuery on CSSRS2"; 
			ResultSet rs2 = cs2.executeQuery();
                        sql="executeQuery on CSSRS3"; 
			ResultSet rs3 = cs3.executeQuery();
			// Close the cursor for the last result set opened. This leaves the
			// pointer to the result set array addressing 
			// the wrong connections result set array, resulting in the
                        // rs.close() not working properly. 
			// 
			rs3.close(); 
			rs.close();
			rs2.close();
		    }



		/* Cleanup the test */

		s = connection_.createStatement();
		for (int i = 0; i < drops.length; i++) {
		    sql = drops[i]; 
		}
		assertCondition(passed, sb.toString()+added);



	    }
	    catch (Exception e) {
		failed (connection_, e, "Unexpected Exception sql='"+sql+"' "+sb.toString()+added); 
	    }
	} 

    }

	/**
Check that you can scroll forward and backwards by using absolute positioning;
Should throw an exception for <= V5R2
**/
    public void Var017()
    {
	String added = " -- Added 2/17/2011 to test scrollable cursor returned where original cursor type is scroll insensitive "; 
	if (checkJdbc20 ())  {	
	    try {

		CallableStatement cs = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSSRS, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = cs.executeQuery();
		rs.beforeFirst();
		rs.absolute(3);
		    String name3 = rs.getString("LSTNAM").trim();
		    rs.absolute(1);
		    String name1 = rs.getString("LSTNAM").trim();
		    assertCondition(name3.equals("Doe") && name1.equals("Abraham"), "name3 should be Doe but is " + name3 + ", name1 should be Abraham but is " + name1 + added); 
	    }
	    catch (Exception e) {
		    failed (e, "Unexpected Exception "+added);
	    }
	}
    }

/**
Check that you can scroll forward and backwards by using relative positioning;
Should throw an exception if server is less than V5R3
**/
    public void Var018()
    {
	String added = " -- Added 2/17/2011 to test scrollable cursor returned where original cursor type is scroll insensitive "; 
	if (checkJdbc20 ())  {	
	    try {
		CallableStatement cs = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSSRS, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = cs.executeQuery();
		rs.next();          //position to first row
		rs.relative(3);
		    String name3 = rs.getString("LSTNAM").trim();
		    rs.relative(1);
		    String name4 = rs.getString("LSTNAM").trim();
		    assertCondition(name3.equals("Henning") && name4.equals("Johnson"), "name3 should be Henning but is " + name3 + ", name4 should be Johnson but is " + name4 + added); 
	    }
	    catch (Exception e) {
		    failed (e, "Unexpected Exception - "+added); 
	    }
	} 

    }

/**
Check that you can scroll forward and backwards
rs.next()
rs.previous()
Should throw an exception if server is less than V5R3
**/
    public void Var019()
    {
	String added = " -- Added 2/17/2011 to test scrollable cursor returned where original cursor type is scroll insensitive "; 

	if (checkJdbc20 ())  {	
	    try {
		CallableStatement cs = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSSRS, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = cs.executeQuery();
		rs.next();
		String name1 = rs.getString("LSTNAM").trim();
		rs.next();
		String name2 = rs.getString("LSTNAM").trim();
		rs.previous();
		    String name3 = rs.getString("LSTNAM").trim();
		    assertCondition(name3.trim().equals("Abraham") && name1.trim().equals("Abraham") && name2.trim().equals("Alison"), "name3 should be Abraham but is " + name3 + ", name1 should be Abraham but is " + name1 + ", name2 should be Alison but is " + name2 + added); 
	    }
	    catch (Exception e) {
		    failed (e, "Unexpected Exception - Added by Toolbox 10/20/2003");
	    }
	} 

    }


/**
Scroll to the end, check you can scroll back up;
Should throw an exception if server is less than V5R3
**/
    public void Var020()
    {
	String added = " -- Added 2/17/2011 to test scrollable cursor returned where original cursor type is scroll insensitive ";
	if (checkJdbc20 ())  {	
	    try {
		CallableStatement cs = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSSRS, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = cs.executeQuery();
		while(rs.next())
		{
		}
		rs.previous();
		    String last = rs.getString("LSTNAM").trim();
		    assertCondition(last.equals("Williams"), "last should be Williams but is " + last + added); 
	    }
	    catch (Exception e) {
		    failed (e, "Unexpected Exception - "+added); 
	    }
	} 

    }

/**
Scroll to the end, check you can scroll back up;
Should throw an exception if server is less than V5R3
**/
    public void Var021()
    {
	String added = " -- Added 2/17/2011 to test scrollable cursor returned where original cursor type is scroll insensitive ";
	if (checkJdbc20 ())  {	
	    try {
		CallableStatement cs = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSSRS, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = cs.executeQuery();
		rs.afterLast();
		    rs.previous();
		    String last = rs.getString("LSTNAM").trim();
		    assertCondition(last.equals("Williams"), "last should be Williams but is " + last + added); 
	    }
	    catch (Exception e) {
		    failed (e, "Unexpected Exception - "+added); 
	    }
	} 
    }

/**
Scroll to the end, check you can scroll back up;
Should throw an exception if server is less than V5R3
**/
    public void Var022()
    {
	String added = " -- Added 2/17/2011 to test scrollable cursor returned where original cursor type is scroll insensitive "; 	if (checkJdbc20 ())  {	
	    try {
		CallableStatement cs = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSSRS, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = cs.executeQuery();
		rs.absolute(12);
		    rs.next();
		    rs.previous();
		    String last = rs.getString("LSTNAM").trim();
		    assertCondition(last.equals("Williams"), "last should be Williams but is " + last + added); 
	    }
	    catch (Exception e) {
		    failed (e, "Unexpected Exception - "+added); 
	    }
	} 

    }

/** 
Scroll to the end and then all the way back up.
Should throw an exception if server is less than V5R3
**/
    public void Var023()
    {
	String added = " -- Added 2/17/2011 to test scrollable cursor returned where original cursor type is scroll insensitive "; 	if (checkJdbc20 ())  {	
	    try {
		CallableStatement cs = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSSRS, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = cs.executeQuery();
		while(rs.next())
		{
		}
		while(rs.previous()){
		}
		    rs.next();
		    String first = rs.getString("LSTNAM").trim();
		    assertCondition(first.equals("Abraham"), "first should be Abraham but is " + first + added); 
	    }
	    catch (Exception e) {
		    failed (e, "Unexpected Exception - "+added); 
	    }
	} 
    }

/**
Check that you can scroll forward and backwards by using absolute positioning;
Multiple result sets are returned
Should throw an exception for <= V5R2
**/
    public void Var024()
    {
	String added = " -- Added 2/17/2011 to test scrollable cursor returned where original cursor type is scroll insensitive ";
	if (checkJdbc20 ())  {	
	    try {
		CallableStatement cs = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSMSRS, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		boolean results = cs.execute();
		String name3 = "UNSET";
		String name1 = "UNSET";
		boolean moreResults = false;
		boolean failDone=false; 
		if(results)
		{
		    ResultSet rs = cs.getResultSet();
		    moreResults = cs.getMoreResults();
		    if(moreResults)
		    {
			ResultSet rs1 = cs.getResultSet();
			rs1.absolute(3);
			    name3 = rs1.getString("LSTNAM").trim();
			    rs1.absolute(1);
			    name1 = rs1.getString("LSTNAM").trim();
			rs1.close();
		    }
		    rs.close();
		}
		if (!failDone) { 
		    assertCondition(results && moreResults && name3.equals("Doe") && name1.equals("Abraham"), "name3 should be Doe but is " + name3 + ", name1 should be Abraham but is " + name1 + added); 
		}
	    }
	    catch (Exception e) {
		    failed (e, "Unexpected Exception - "+added); 
	    }
	} 
    }

/**
Check that you can scroll forward and backwards by using relative positioning;
Multiple Result sets are returned
Should throw an exception if server is less than V5R3
**/
    public void Var025()
    {
	String added = " -- Added 2/17/2011 to test scrollable cursor returned where original cursor type is scroll insensitive ";
	ResultSet rs =null; 
	if (checkJdbc20 ())  {	
	    try {
		CallableStatement cs = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSMSRS, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		boolean results = cs.execute();
		String name3 = null;
		String name4 = null;
		boolean moreResults = false;
		boolean failDone=false; 
		if(results)
		{
		    rs = cs.getResultSet();
		    moreResults = cs.getMoreResults();
		    if(moreResults)
		    {
			ResultSet rs1 = cs.getResultSet();
			rs1.next();          //position to first row
			rs1.relative(3);
			    name3 = rs1.getString("LSTNAM").trim();
			    rs1.relative(1);
			    name4 = rs1.getString("LSTNAM").trim();
		    }
		}
		if (!failDone) { 
		    assertCondition(results && moreResults && (name3 != null) && name3.equals("Henning") && (name4 != null) && name4.equals("Johnson"), "name3 should be Henning but is " + name3 + ", name4 should be Johnson but is " + name4 + added+ rs );
		}
	    }
	    catch (Exception e) {
		    failed (e, "Unexpected Exception - "+added); 
	    }
	} 
    }
/**
Check that you can scroll forward and backwards
multiple result sets returned
rs.next()
rs.previous()
Should throw an exception if server is less than V5R3
**/
    public void Var026()
    {
	String added = " -- Added 2/17/2011 to test scrollable cursor returned where original cursor type is scroll insensitive ";
	if (checkJdbc20 ())  {	
	    try {
		CallableStatement cs = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSMSRS, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		String name1 = null;
		String name2 = null;
		String name3 = null;
		boolean results = cs.execute();
		boolean moreResults = false;
		boolean failDone =false; 
		ResultSet rs  = null; 
		if(results)
		{
		    rs = cs.getResultSet();
		    moreResults = cs.getMoreResults();
		    if(moreResults)
		    {
			ResultSet rs1 = cs.getResultSet();
			rs1.next();
			name1 = rs1.getString("LSTNAM").trim();
			rs1.next();
			name2 = rs1.getString("LSTNAM").trim();
			rs1.previous();
			    name3 = rs1.getString("LSTNAM").trim();
		    }
		}
		if (!failDone) { 
		    assertCondition((rs != null) &&results && moreResults && (name3 != null) && name3.trim().equals("Abraham") && (name1 != null) && name1.trim().equals("Abraham") && (name2 != null) && name2.trim().equals("Alison"), "name3 should be Abraham but is " + name3 + ", name1 should be Abraham but is " + name1 + ", name2 should be Alison but is " + name2 + added); 
		}
	    }
	    catch (Exception e) {
		    failed (e, "Unexpected Exception - "+added); 
	    }
	} 
    }

/**
Scroll to the end, check you can scroll back up;
Multiple Result Sets returned
Should throw an exception if server is less than V5R3
**/
    public void Var027()
    {
	String added = " -- Added 2/17/2011 to test scrollable cursor returned where original cursor type is scroll insensitive ";
	if (checkJdbc20 ())  {	
	    try {
		CallableStatement cs = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSMSRS, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		String last = null;
		boolean results = cs.execute();
		boolean moreResults = false;
		boolean failDone=false; 
	              ResultSet rs1 = null; 

		if(results)
		{
		    rs1 = cs.getResultSet();
		    moreResults = cs.getMoreResults();
		    if(moreResults)
		    {
			ResultSet rs = cs.getResultSet();
			while(rs.next())
			{
			}
			rs.previous();
			    last = rs.getString("LSTNAM").trim();
		    }
		}
		if (!failDone) { 
		    assertCondition((rs1 != null) &&results && moreResults && (last != null) && last.equals("Williams"), "last should be Williams but is " + last + added); 
		}
	    }
	    catch (Exception e) {
		    failed (e, "Unexpected Exception "+added); 
	    }
	} 
    }

/**
Scroll to the end, check you can scroll back up;
Multiple Result sets returned
Should throw an exception if server is less than V5R3
**/
    public void Var028()
    {
	String added = " -- Added 2/17/2011 to test scrollable cursor returned where original cursor type is scroll insensitive ";
	if (checkJdbc20 ())  {	
	    try {
		CallableStatement cs = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSMSRS, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		String last = null;
		boolean moreResults = false;
		boolean results = cs.execute();
		boolean failDone = false;    
		ResultSet rs1 = null; 

		if(results)
		{
		     rs1 = cs.getResultSet();
		    moreResults = cs.getMoreResults();
		    if(moreResults)
		    {
			ResultSet rs = cs.getResultSet();
			rs.afterLast();
			    rs.previous();
			    last = rs.getString("LSTNAM").trim();
		    }
		}
		if (!failDone) { 
		    assertCondition((rs1 != null) &&results && moreResults && (last != null) && last.equals("Williams"), "last should be Williams but is " + last + added); 
		}
	    }
	    catch (Exception e) {
		    failed (e, "Unexpected Exception - Added by Toolbox 10/20/2003");
	    }
	} 
    }

/**
Scroll to the end, check you can scroll back up;
Multiple ResultSets returned
Should throw an exception if server is less than V5R3
**/
    public void Var029()
    {
	String added = " -- Added 2/17/2011 to test scrollable cursor returned where original cursor type is scroll insensitive ";
	if (checkJdbc20 ())  {	
	    try {
		CallableStatement cs = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSMSRS, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		String last = null;
		boolean moreResults = false;
		boolean results = cs.execute();
		boolean failDone = false; 
	              ResultSet rs1 = null; 

		if(results)
		{
		     rs1 = cs.getResultSet();
		    moreResults = cs.getMoreResults();
		    if (moreResults)
		    {
			ResultSet rs = cs.getResultSet();
			rs.absolute(12);
			    rs.next();
			    rs.previous();
			    last = rs.getString("LSTNAM").trim();
		    }
		}
		if (!failDone) {
		    assertCondition((rs1 != null) &&results && moreResults && (last != null) && last.equals("Williams"), "last should be Williams but is " + last + added); 
		}
	    }
	    catch (Exception e) {
		    failed (e, "Unexpected Exception - "+added); 
	    }
	} 
    }

/** 
Scroll to the end and then all the way back up.
Multiple Result Sets returned
Should throw an exception if server is less than V5R3
**/
    public void Var030()
    {
	String added = " -- Added 2/17/2011 to test scrollable cursor returned where original cursor type is scroll insensitive ";
	if (checkJdbc20 ())  {	
	    try {
		CallableStatement cs = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSMSRS, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		String first = null;
		boolean moreResults = false;
		boolean results = cs.execute();
		boolean failDone=false; 
	              ResultSet rs1 = null; 

		if(results)
		{
		    rs1 = cs.getResultSet();
		    moreResults = cs.getMoreResults();
		    if(moreResults)
		    {
			ResultSet rs = cs.getResultSet();
			while(rs.next())
			{
			}
			while(rs.previous()){
			}
			    rs.next();
			    first = rs.getString("LSTNAM").trim();
		    }
		}
		if (!failDone) { 
		    assertCondition((rs1 != null) &&results && moreResults && (first != null) && first.equals("Abraham"), "first should be Abraham but is " + first + added); 
		}
	    }
	    catch (Exception e) {
		    failed (e, "Unexpected Exception - "+added); 
	    }
	} 

    }

    /**
Check that you cannot scroll forward and backwards when the result set is TYPE_FORWARD_ONLY
In V5R5 and later, both drivers correctly detect this is really a scrollable cursor. 
**/
    public void Var031()
    {
	String added = " -- Added 2/17/2011 to test scrollable cursor returned where original cursor type is scroll insensitive ";

	if (checkJdbc20 ())  {	
	    try {
		CallableStatement cs = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSSRS, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = cs.executeQuery();
		rs.next();
		rs.next();
		rs.previous();
		succeeded();
	    }
	    catch (Exception e) {
		failed("V6R1 toolbox should follow what is returned by hostserver on PS definition"+added);
	    }
	} 
    }



    public void Var032() {
	assertCondition(true); 
    }

}





