///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSMisc.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.RS;

import com.ibm.as400.access.AS400;

import test.JDRSTest;
import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;


/**
Testcase JDRSMisc.  This tests the following
methods of the JDBC ResultSet class:

<ul>
<li>close()
<li>getConcurrency()
<li>getCursorName()
<li>getFetchDirection()
<li>setFetchDirection()
<li>getMetaData()
<li>getStatement()
<li>getType()
<li>toString()
<li>"lazy close" property
</ul>
**/
public class JDRSMisc
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSMisc";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



    // Private data.
    private Connection          connection2_, connection3_;
    private Statement           statement_;
    private Statement           statement0_;



/**
Constructor.
**/
    public JDRSMisc (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSMisc",
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
	if (connection_ != null) connection_.close();
        connection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_, "JDRSMISC_CON1");
        connection2_ = testDriver_.getConnection (baseURL_+";cursor hold=true", userId_, encryptedPassword_, "JDRSMISC_CON2");
        connection3_ = testDriver_.getConnection (baseURL_+";cursor hold=false", userId_, encryptedPassword_, "JDRSMISC_CON3");
        statement0_ = connection_.createStatement ();

        if (isJdbc20 ()) {
	    try { 
		statement_ = connection_.createStatement
		  (
		   ResultSet.TYPE_SCROLL_INSENSITIVE,
		   ResultSet.CONCUR_READ_ONLY);
	    } catch (Exception e) {
		System.out.println("Warning:  creating nonScrollable cursor");
		statement_ = connection_.createStatement(); 
	    }
	    
            if(collection_ != null) {
                JDSetupProcedure.create (systemObject_,
                    connection_, JDSetupProcedure.STP_RS1, supportedFeatures_, collection_);
            }
            else {
                JDSetupProcedure.create (systemObject_,
                                         connection_, JDSetupProcedure.STP_RS1, supportedFeatures_, JDSetupProcedure.STP_RS1);
            }
        }
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
        if (isJdbc20 ())
            statement_.close ();

        statement0_.close ();
        connection_.close ();
    }


    public void cleanupConnections ()
        throws Exception
    {
        connection_.close ();
        connection2_.close ();
        connection3_.close ();
        
    }



/**
close() - Verify that the result set is indeed closed.
**/
    public void Var001 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            rs.close ();
            rs.next ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
close() - Verify closing a closed result set is ok.
**/
    public void Var002 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            rs.close ();
            rs.close ();
            succeeded ();
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
close() - Verify that closing the result set after the statement is closed is ok.
**/
    public void Var003 ()
    {
        try {
           Statement statement = connection_.createStatement ();
           ResultSet rs = statement.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
           rs.next();
           statement.close();
           rs.close ();

           succeeded ();
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getConcurrency() - Should thrown an exception on a closed result set.
**/
    public void Var004 ()
    {
        if (checkJdbc20 ()) {
        try {
            Statement statement = connection_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = statement.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            rs.close ();
            rs.getConcurrency ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getConcurrency() - Should return the concurrency when it is read only.
**/
    public void Var005 ()
    {
        if (checkJdbc20 ()) {
        try {
            Statement statement = connection_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = statement.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            int concurrency = rs.getConcurrency ();
            rs.close ();
            statement.close ();
            assertCondition (concurrency == ResultSet.CONCUR_READ_ONLY);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getConcurrency() - Should return the concurrency when it is updatable.
**/
    public void Var006 ()
    {
        if (checkJdbc20 ()) {
        try {
            Statement statement = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = statement.executeQuery ("SELECT * FROM QIWS.QCUSTCDT FOR UPDATE");
            int concurrency = rs.getConcurrency ();
            rs.close ();
            statement.close ();
            assertCondition (concurrency == ResultSet.CONCUR_UPDATABLE);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getConcurrency() - Verify that stored procedure calls are read only
regardless of what they are set to.
**/
    public void Var007 ()
    {
        if (checkJdbc20 ()) {
        try {
                JDSetupProcedure.create (systemObject_,
                    connection_, JDSetupProcedure.STP_RS1, supportedFeatures_, collection_);
            Statement statement = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = statement.executeQuery ("CALL " + JDSetupProcedure.STP_RS1);
            int concurrency = rs.getConcurrency ();
            rs.close ();
            statement.close ();
            assertCondition (concurrency == ResultSet.CONCUR_READ_ONLY);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getConcurrency() - Verify that DatabaseMetaData catalog methods
are read only.
**/
    public void Var008 ()
    { 
        if (checkJdbc20 ()) {
        try {
                JDSetupProcedure.create (systemObject_,
                    connection_, JDSetupProcedure.STP_RS1, supportedFeatures_, collection_);
            ResultSet rs = connection_.getMetaData ().getTables (null, null, null, null);
            int concurrency = rs.getConcurrency ();
            rs.close ();
            assertCondition (concurrency == ResultSet.CONCUR_READ_ONLY, "getTables concurrency is "+concurrency+" sb CONCUR_READ_ONLY="+ResultSet.CONCUR_READ_ONLY);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getConcurrency() - Verify that SELECTs that do not specify
FOR UPDATE are read only regardless of what they are set to.
**/
    public void Var009 ()
    {
        if (checkJdbc20 ()) {
        try {
                JDSetupProcedure.create (systemObject_,
                    connection_, JDSetupProcedure.STP_RS1, supportedFeatures_, collection_);
            Statement statement = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = statement.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            int concurrency = rs.getConcurrency ();
            rs.close ();
            statement.close ();
            assertCondition (concurrency == ResultSet.CONCUR_UPDATABLE);   //@C4C
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getCursorName() - Should thrown an exception on a closed result set.
**/
    public void Var010 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            rs.close ();
            rs.getCursorName ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getCursorName() - Should return a default name when none has been
set, and the result set is forward only.
**/
    public void Var011 ()
    {
        try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            String cursorName = rs.getCursorName ();
            rs.close ();
            s.close ();

            if (isToolboxDriver())
                assertCondition ((cursorName.startsWith ("CRSR")) && (cursorName.length () == 8));
            else
                assertCondition (cursorName.length () > 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getCursorName() - Should return a default name when none has been
set, and the result set is scrollable.
**/
    public void Var012 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            String cursorName = rs.getCursorName ();
            rs.close ();

            if (isToolboxDriver())
                assertCondition ((cursorName.startsWith ("SCRSR")) && (cursorName.length () == 9));
            else
                assertCondition (cursorName.length () > 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getCursorName() - Should return then cursor name when it has been
set.
**/
    public void Var013 ()
    {
        try {
            Statement s = connection_.createStatement ();
            s.setCursorName ("DYLAN");
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            String cursorName = rs.getCursorName ();
            rs.close ();
            s.close ();
            assertCondition (cursorName.equals ("DYLAN"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getFetchDirection() - Should throw an exception when the result
set is closed.
**/
    public void Var014 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            rs.close ();
            int fetchDirection = rs.getFetchDirection ();
            failed ("Didn't throw SQLException was"+fetchDirection);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getFetchDirection() - Should return the default fetch direction
when it has not been set.
**/
    public void Var015 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            int fetchDirection = rs.getFetchDirection ();
            rs.close ();
            assertCondition (fetchDirection == ResultSet.FETCH_FORWARD);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getFetchDirection() - Should return the fetch direction when it
has been set in the statement only.
**/
    public void Var016 ()
    {
        if (checkJdbc20 ()) {
        try {
            Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
            s.setFetchDirection (ResultSet.FETCH_REVERSE);
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            int fetchDirection = rs.getFetchDirection ();
            rs.close ();
            s.close ();
            assertCondition (fetchDirection == ResultSet.FETCH_REVERSE);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getFetchDirection() - Should return the fetch direction when it
has been set in the result set only.
**/
    public void Var017 ()
    {
        if (checkJdbc20 ()) {
        try {
            Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            rs.setFetchDirection (ResultSet.FETCH_UNKNOWN);
            int fetchDirection = rs.getFetchDirection ();
            rs.close ();
            assertCondition (fetchDirection == ResultSet.FETCH_UNKNOWN);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getFetchDirection() - Should return the fetch direction when it
has been set in the statement and result set.
**/
    public void Var018 ()
    {
        if (checkJdbc20 ()) {
        try {
            Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
            s.setFetchDirection (ResultSet.FETCH_UNKNOWN);
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            rs.setFetchDirection (ResultSet.FETCH_REVERSE);
            int fetchDirection = rs.getFetchDirection ();
            rs.close ();
            s.close ();
            assertCondition (fetchDirection == ResultSet.FETCH_REVERSE);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
setFetchDirection() - Should throw an exception when the result
set is closed.
**/
    public void Var019 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            rs.close ();
            rs.setFetchDirection (ResultSet.FETCH_REVERSE);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
setFetchDirection() - Pass a fetch direction other than FETCH_FORWARD
when the result set type is TYPE_FORWARD_ONLY.  Should throw
an exception.
**/
    public void Var020 ()
    {
        if (checkJdbc20 ()) {
        try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            rs.setFetchDirection (ResultSet.FETCH_REVERSE);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
setFetchDirection() - Pass an invalid fetch direction.  Should throw
an exception.
**/
    public void Var021 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            rs.setFetchDirection (8796);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
setFetchDirection() - Pass a valid fetch direction other than the
default.  Should set the fetch size.
**/
    public void Var022 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            rs.setFetchDirection (ResultSet.FETCH_UNKNOWN);
            int fetchDirection = rs.getFetchDirection ();
            rs.close ();
            assertCondition (fetchDirection == ResultSet.FETCH_UNKNOWN);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
setFetchDirection() - Pass a valid fetch direction that is the
default.  Should set the fetch size.
**/
    public void Var023 ()
    {
        if (checkJdbc20 ()) {
        try {
            Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
            s.setFetchDirection (ResultSet.FETCH_REVERSE);
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            rs.setFetchDirection (ResultSet.FETCH_FORWARD);
            int fetchDirection = rs.getFetchDirection ();
            rs.close ();
            s.close ();
            assertCondition (fetchDirection == ResultSet.FETCH_FORWARD);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getMetaData() - Should work on a closed result set.
**/
    public void Var024 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            rs.close ();
            ResultSetMetaData rsmd = rs.getMetaData ();
            assertCondition (rsmd != null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getMetaData() - Should work on an open result set.
**/
    public void Var025 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            ResultSetMetaData rsmd = rs.getMetaData ();
            rs.close ();
            assertCondition (rsmd != null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getStatement() - Returns the statement for this result set, when
created from a regular statement.
**/
    public void Var026()
    {
        if (checkJdbc20 ()) {
        try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            Statement s2 = rs.getStatement ();
            rs.close ();
            s.close ();
            assertCondition (s == s2, "Statement objects are not the same");
        }
        catch (SQLException e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getStatement() - Returns the statement for this result set, when
created from a prepared statement.
**/
    public void Var027()
    {
        if (checkJdbc20 ()) {
        try {
            PreparedStatement s = connection_.prepareStatement ("SELECT * FROM QIWS.QCUSTCDT");
            ResultSet rs = s.executeQuery ();
            Statement s2 = rs.getStatement ();
            rs.close ();
            s.close ();
            assertCondition (s == s2, "Statement objects are not the same");
        }
        catch (SQLException e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getStatement() - Returns the statement for this result set, when
created from a callable statement.
**/
    public void Var028()
    {
        if (checkJdbc20 ()) {
        try {
            PreparedStatement s = connection_.prepareCall ("SELECT * FROM QIWS.QCUSTCDT");
            ResultSet rs = s.executeQuery ();
            Statement s2 = rs.getStatement ();
            rs.close ();
            s.close ();
            assertCondition (s == s2, "Statement objects are not the same");
        }
        catch (SQLException e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getStatement() - Returns the statement for this result set, when
created from a DatabaseMetaData catalog method.
**/
    public void Var029()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = connection_.getMetaData ().getTables (null, null, null, null);
            Statement s2 = rs.getStatement ();
            rs.close ();
            assertCondition (s2 == null,
			     "Result set statement from database metadata method is not null but is "+s2);
        }
        catch (SQLException e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getStatement() - Returns the statement for this result set, when
the result set is closed.
**/
    public void Var030()
    {
        if (checkJdbc20 ()) {
        try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            rs.close ();
            Statement s2 = rs.getStatement ();
            s.close ();
            assertCondition (s == s2, "statementObjects are not the same s="+s+"("+s.getClass()+") s2="+s2+"("+s2.getClass()+")");
        }
        catch (SQLException e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getType() - Should thrown an exception on a closed result set.
**/
    public void Var031 ()
    {
        if (checkJdbc20 ()) {
        try {
            Statement statement = connection_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = statement.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            rs.close ();
            rs.getType ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getType() - Should return the type when it is forward only.
**/
    public void Var032 ()
    {
        if (checkJdbc20 ()) {
        try {
            Statement statement = connection_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = statement.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            int type = rs.getType ();
            rs.close ();
            statement.close ();
            assertCondition (type == ResultSet.TYPE_FORWARD_ONLY);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getType() - Should return the type when it is scroll sensitive.
**/
    public void Var033 ()
    {
        if (checkJdbc20 ()) {
        try {
            Statement statement = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = statement.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            int type = rs.getType ();
            rs.close ();
            statement.close ();
            assertCondition (type == ResultSet.TYPE_SCROLL_SENSITIVE);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getType() - Should return the type when it is scroll insensitive.
**/
    public void Var034 ()
    {
        if (checkJdbc20 ()) {
        try {
            Statement statement = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = statement.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            int type = rs.getType ();
            rs.close ();
            statement.close ();
            assertCondition (type == ResultSet.TYPE_SCROLL_INSENSITIVE);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getType() - Verify that stored procedure calls are forward only
regardless of what they are set to.
**/
    public void Var035 ()
    {
	if (checkJdbc20 ()) {
	    try {
		    JDSetupProcedure.create (systemObject_,
					     connection_, JDSetupProcedure.STP_RS1S, supportedFeatures_, collection_);
		Statement statement = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
								   ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = statement.executeQuery ("CALL " + JDSetupProcedure.STP_RS1S);
		int type = rs.getType ();
		int firstFetch = -1, secondFetch = -2;
		if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {                   /* @D2A */
		    // verify that the cursor is scrollable                          /* @D2A */
		    rs.first();
		    firstFetch = rs.getInt("CUSNUM");
		    rs.next();                                                       /* @D2A */
		    rs.previous();						     /* @D2A */
		    secondFetch = rs.getInt("CUSNUM");
		}                                                                    /* @D2A */
		rs.close ();
		statement.close ();
        if(getRelease() < JDTestDriver.RELEASE_V7R1M0) {                                                  //@D1A
            assertCondition (type == ResultSet.TYPE_FORWARD_ONLY);
        } else if(getRelease() <= JDTestDriver.RELEASE_V7R1M0) { //@pda                                                                                             //@D1A
            assertCondition (type == ResultSet.TYPE_SCROLL_INSENSITIVE && firstFetch == secondFetch, "Type is "+type+" instead of TYPE_SCROLL_INSENSITIVE or firstFetch("+firstFetch+") != secondFetch("+secondFetch+") Added by Toolbox 11/18/2003"); ///@D1A
        } else {                                                                                             
            assertCondition ( ((type == ResultSet.TYPE_SCROLL_SENSITIVE) || (type == ResultSet.TYPE_SCROLL_INSENSITIVE)) && firstFetch == secondFetch, "Type is "+type+" instead of TYPE_SCROLL_INSENSITIVE or firstFetch("+firstFetch+") != secondFetch("+secondFetch+") Added by Toolbox 05/31/2007"); //@pda //get from sp definition, which is assensitive by default
        }
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception -- changed by toolbox 11/18/2003 ");
	    }
	}
    }



/**
toString() - Should return the cursor name on a closed result set.
**/
    public void Var036 ()
    {
        try {
            Statement s = connection_.createStatement ();
            s.setCursorName ("LIAM");
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            rs.close ();
            String cursorName = rs.toString ();
            s.close ();
            assertCondition (cursorName.equals ("LIAM"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
toString() - Should return a default name when none has been
set.
**/
    public void Var037 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            String cursorName = rs.toString ();
            rs.close ();
            assertCondition (cursorName.length () > 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
toString() - Should return then cursor name when it has been
set.
**/
    public void Var038 ()
    {
        try {
            Statement s = connection_.createStatement ();
            s.setCursorName ("BERNARD");
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            String cursorName = rs.toString ();
            rs.close ();
            s.close ();
            assertCondition (cursorName.equals ("BERNARD"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
"lazy close" property - open and close a single cursor via Statement successively with 
lazy close on.
<P>
SQL400 - the native driver does not explicitly support the lazy close property, but the
         driver does support the functionality tested in this testcase.
**/
    public void Var039()
    {
        try {
             Connection c = testDriver_.getConnection(baseURL_ + ";lazy close=true;block criteria=0", userId_, encryptedPassword_);
             Statement s = c.createStatement();

             // Do various numbers of fetches.
             for(int i = 0; i < 17; ++i) {
                 ResultSet rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
                 boolean success = true;
                 for(int j = 0; (j < i) && (success); ++j)
                     success = rs.next();
             }

             s.close();
             c.close();
             succeeded();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
"lazy close" property - open and close a single cursor via Statement successively with 
lazy close off.
<P>
SQL400 - the native driver does not explicitly support the lazy close property, but the
         driver does support the functionality tested in this testcase.
**/
    public void Var040()
    {
        try {
             Connection c = testDriver_.getConnection(baseURL_ + ";lazy close=false;block criteria=0", userId_, encryptedPassword_);
             Statement s = c.createStatement();

             // Do various numbers of fetches.
             for(int i = 0; i < 17; ++i) {
                 ResultSet rs = s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
                 boolean success = true;
                 for(int j = 0; (j < i) && (success); ++j)
                     success = rs.next();
             }

             s.close();
             c.close();
             succeeded();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
"lazy close" property - open and close multiple cursors successively with 
lazy close on.
<P>
SQL400 - the native driver does not explicitly support the lazy close property, but the
         driver does support the functionality tested in this testcase.
**/
    public void Var041()
    {
        try {
             Connection c = testDriver_.getConnection(baseURL_ + ";lazy close=true;block criteria=0", userId_, encryptedPassword_);
             Statement[] s = new Statement[15];
             for(int i = 0; i < s.length; ++i) {
                 s[i] = c.createStatement();

                 // Do various numbers of fetches.
                 for(int j = 0; j < 17; ++j) {
                     ResultSet rs = s[i].executeQuery("SELECT * FROM QIWS.QCUSTCDT");
                     boolean success = true;
                     for(int k = 0; (k < j) && (success); ++k)
                         success = rs.next();
                 }
             }

             for(int i = 0; i < s.length; ++i) {
                 // Do various numbers of fetches.
                 for(int j = 0; j < 17; ++j) {
                     ResultSet rs = s[i].executeQuery("SELECT * FROM QIWS.QCUSTCDT");
                     boolean success = true;
                     for(int k = 0; (k < j) && (success); ++k)
                         success = rs.next();
                 }
             }

             c.close();
             succeeded();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
"lazy close" property - open and close multiple cursors successively with 
lazy close off.
<P>
SQL400 - the native driver does not explicitly support the lazy close property, but the
         driver does support the functionality tested in this testcase.
**/
    public void Var042()
    {
        try {
             Connection c = testDriver_.getConnection(baseURL_ + ";lazy close=false;block criteria=0", userId_, encryptedPassword_);
             Statement[] s = new Statement[15];
             for(int i = 0; i < s.length; ++i) {
                 s[i] = c.createStatement();

                 // Do various numbers of fetches.
                 for(int j = 0; j < 17; ++j) {
                     ResultSet rs = s[i].executeQuery("SELECT * FROM QIWS.QCUSTCDT");
                     boolean success = true;
                     for(int k = 0; (k < j) && (success); ++k)
                         success = rs.next();
                 }
             }

             for(int i = 0; i < s.length; ++i) {
                 // Do various numbers of fetches.
                 for(int j = 0; j < 17; ++j) {
                     ResultSet rs = s[i].executeQuery("SELECT * FROM QIWS.QCUSTCDT");
                     boolean success = true;
                     for(int k = 0; (k < j) && (success); ++k)
                         success = rs.next();
                 }
             }

             c.close();
             succeeded();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

/**
getType() - Verify that a result set returned from a stored procedure may not be scrollable,
even though it is declared to be. @D2A 
**/
    public void Var043 ()
    {
	String command=""; 
	if (checkJdbc20 ()) {
	    if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {                   /* @D2A */
		try {
		    String collection = collection_;
		    if (collection == null) {
			collection = "JDTESTSTP";
		    }
		    String procedure = collection+".JDRSMISC43";

	            //
                    // Create the array result sets procedure
	            //
		    Statement stmt = connection_.createStatement();

		    //
		    // Cleanup from last run 
		    // 
		    command = "CALL QSYS.QCMDEXC('DLTF "+collection+"/arrayrs                        ',0000000035.00000)";
		    try { 
			stmt.executeUpdate(command);
		    }catch(Exception e) { } 
		    command = "drop procedure "+procedure;
		    try { 
			stmt.executeUpdate(command);
		    } catch(Exception e) {
		    }


		    //
		    // create the source file
		    //
		    command = "CALL QSYS.QCMDEXC('CRTSRCPF "+collection+"/arrayrs                    ', 0000000030.00000)"; 
		    stmt.executeUpdate(command);
		    command = "CALL QSYS.QCMDEXC('ADDPFM   "+collection+"/arrayrs arrayrs                                   ',0000000060.00000)";
		    stmt.executeUpdate(command) ;


		    //
		    // Load the code into it 
		    // 


		      String[] sourceCode= {
			"     DRESULT           DS                  OCCURS(20)",
			"     D COL1                    1     16A",
			"     C     1             DO        20            X                 2 0",
			"     C     X             OCCUR     RESULT",
			"     C                   EVAL      COL1='array result set'",
			"     C                   ENDDO",
			"     C                   EVAL      X=X-1",
			"     C/EXEC SQL",
			"     C+ SET RESULT SETS FOR RETURN TO CLIENT ARRAY :RESULT FOR :X ROWS",
			"     C/END-EXEC",
			"     C                   SETON                                            LR",
			"     C                   RETURN",
			"",
			""
		    }; 
		    command = "insert into "+collection+".arrayrs values(?, 0, ?)";
		    PreparedStatement ps = connection_.prepareStatement(command);

		    for (int i = 0; i < sourceCode.length; i++) {
			ps.setInt(1, i+1); 
			ps.setString(2, sourceCode[i]);
			ps.executeUpdate();
		    }
		    //
		    // Compile it 
		    //
		    command = "crtsqlrpgi obj("+collection+"/arrayrs) srcfile("+collection+"/arrayrs) commit(*none) objtype(*module) option(*Xref *sql)";
		    command = "call QSYS.QCMDEXC('"+
		      command+"',0000000"+command.length()+".00000)";
		    stmt.executeUpdate(command);

		    command = "crtpgm "+collection+"/arrayrs";
		    command = "call QSYS.QCMDEXC('"+command+"', 00000000"+command.length()+".00000)";
		    stmt.executeUpdate(command);

		    //
		    // Declare the procedures 
		    // 
		      command = "create procedure "+procedure+" language rpg parameter style general external name '"+collection+"/ARRAYRS'" ;
		      stmt.executeUpdate(command); 

            Statement statement;
            if (isToolboxDriver()){ //@PDC 
                statement =
                    connection_.createStatement
                       (ResultSet.TYPE_FORWARD_ONLY,
                  ResultSet.CONCUR_READ_ONLY);   
            } else {
		        statement =
                        connection_.createStatement
                           (ResultSet.TYPE_SCROLL_INSENSITIVE,
			    ResultSet.CONCUR_READ_ONLY);
            }
		    command = "CALL " + procedure;
		    ResultSet rs = statement.executeQuery (command); 
		    int type = rs.getType ();

                    // System.out.println("Type is "+type +" Scroll insenstive = "+ResultSet.TYPE_SCROLL_INSENSITIVE );

		    // verify that the cursor is scrollable
		    // this should throw exception for an array result set 
		    rs.next();
		    rs.previous();
		    rs.close ();
		    statement.close ();

                    // assertCondition (type == ResultSet.TYPE_FORWARD_ONLY,
		    //		       "Type is "+type+" instead of TYPE_SCROLL_INSENSITIVE Added by Native  12/15/2003");

		    failed("Didn't throw expected exception -- Added by Native 12/15/2003 type was "+type);

		} catch (SQLException e) {
		    // HY009 or HY024 is received by the native driver when trying to fetch prior when using a
		    // cursor for an array result set
   		    // Toolbox will need to add the error they expect
		    String sqlState = e.getSQLState();
		    if (sqlState == null) sqlState = "SQLSTATE IS NULL"; 

		    if (  sqlState.equals("HY009")  || sqlState.equals("HY024") || sqlState.equals("24000") )
			succeeded();
		    else 
			failed (e, "Fetch prior failed with unexpected exception SQLState="+ sqlState +" -- Toolbox needs to add expected SQLState -- added by native driver 01/14/03");
		}
		catch(Exception e) {
		    failed(e, "Unexpected Exception probably on "+command +" -- changed native 12/15/2003 ");
		}
	    } else {
		notApplicable("V5R3 stored procedure scrollable result set variation"); 
	    } 
	}
    }


/**
getType() - Verify that a result set returned doesn't have a null when reused.

The recreate in JDSQL400 using var043 test was the following

call JDTESTSTP.JDRSMISC43
select cast(null as int) from qsys2.qsqptabl
call JDTESTSTP.JDRSMISC43


**/
    public void Var044 ()
    {
	String command=""; 
	if (checkJdbc20 ()) {
	    if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {                   /* @D2A */
		try {
		    String collection = collection_;
		    if (collection == null) {
			collection = "JDTESTSTP";
		    }
		    String procedure = collection+".JDRSMISC44";

	            //
                    // Create the array result sets procedure
	            //
		    Statement stmt = connection_.createStatement();

		    //
		    // Cleanup from last run 
		    // 
		    command = "CALL QSYS.QCMDEXC('DLTF "+collection+"/arrayrs                        ',0000000035.00000)";
		    try { 
			stmt.executeUpdate(command);
		    }catch(Exception e) { } 
		    command = "drop procedure "+procedure;
		    try { 
			stmt.executeUpdate(command);
		    } catch(Exception e) {
		    }


		    //
		    // create the source file
		    //
		    command = "CALL QSYS.QCMDEXC('CRTSRCPF "+collection+"/arrayrs                    ', 0000000030.00000)"; 
		    stmt.executeUpdate(command);
		    command = "CALL QSYS.QCMDEXC('ADDPFM   "+collection+"/arrayrs arrayrs                                   ',0000000060.00000)";
		    stmt.executeUpdate(command) ;


		    //
		    // Load the code into it 
		    // 


		      String[] sourceCode= {
			"     DRESULT           DS                  OCCURS(20)",
			"     D COL1                    1     16A",
			"     C     1             DO        20            X                 2 0",
			"     C     X             OCCUR     RESULT",
			"     C                   EVAL      COL1='array result set'",
			"     C                   ENDDO",
			"     C                   EVAL      X=X-1",
			"     C/EXEC SQL",
			"     C+ SET RESULT SETS FOR RETURN TO CLIENT ARRAY :RESULT FOR :X ROWS",
			"     C/END-EXEC",
			"     C                   SETON                                            LR",
			"     C                   RETURN",
			"",
			""
		    }; 
		    command = "insert into "+collection+".arrayrs values(?, 0, ?)";
		    PreparedStatement ps = connection_.prepareStatement(command);

		    for (int i = 0; i < sourceCode.length; i++) {
			ps.setInt(1, i+1); 
			ps.setString(2, sourceCode[i]);
			ps.executeUpdate();
		    }
		    //
		    // Compile it 
		    //
		    command = "crtsqlrpgi obj("+collection+"/arrayrs) srcfile("+collection+"/arrayrs) commit(*none) objtype(*module) option(*Xref *sql)";
		    command = "call QSYS.QCMDEXC('"+
		      command+"',0000000"+command.length()+".00000)";
		    stmt.executeUpdate(command);

		    command = "crtpgm "+collection+"/arrayrs";
		    command = "call QSYS.QCMDEXC('"+command+"', 00000000"+command.length()+".00000)";
		    stmt.executeUpdate(command);

		    //
		    // Declare the procedures 
		    // 
		      command = "create procedure "+procedure+" language rpg parameter style general external name '"+collection+"/ARRAYRS'" ;
		      stmt.executeUpdate(command); 


		    Statement statement =
                        connection_.createStatement(); 
		    command = "CALL " + procedure;
		    ResultSet rs = statement.executeQuery (command); 

		    rs.next();
		    rs.close ();
		    statement.close(); 

		    statement =
		      connection_.createStatement(); 

		    rs = statement.executeQuery("select cast(null as int) from qsys2.qsqptabl");

		    rs.next();
		    rs.close();
		    statement.close();

		    statement =
		      connection_.createStatement(); 
		    rs = statement.executeQuery (command); 

		    rs.next();
		    boolean isNull = (null == rs.getString(1)); 
		    rs.close ();
		    statement.close();

                    assertCondition (!isNull, "Error.. returned null for array result set .. added by native driver 06/21/2005"); 

		} catch (SQLException e) {
		    
		    failed (e, " -- added by native driver 06/20/05");
		}
		catch(Exception e) {
		    failed(e, "Unexpected Exception probably on "+command +" -- added native 06/20/2005 ");
		}
	    } else {
		notApplicable("V5R3 stored procedure scrollable result set variation"); 
	    } 
	}
    }


    /**
     * test isClosed when result set is open.  Should return false
     *
     * @since 1.6 (JDBC 4.0)
     */ 
    public void Var045()
    {
	String added = " -- Added 12/27/2006 for JDBC 4.0";

	if (checkJdbc40()) { 
	    try
	    {
		Statement s = connection_.createStatement ();
		ResultSet rs;
		if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
		rs = s.executeQuery ("SELECT  concat(CITY,STATE) FROM QIWS.QCUSTCDT");
		} else { 
		rs = s.executeQuery ("SELECT {fn concat(CITY,STATE)} FROM QIWS.QCUSTCDT");
		}
		boolean closed = JDReflectionUtil.callMethod_B(rs,"isClosed"); 
		s.close ();
		assertCondition (closed==false, "isClosed returned true sb false "+added);
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception"+added);
	    }
	}
    }


    /**
     * test isClosed when result set is closed.  Should return true
     *
     * @since 1.6 (JDBC 4.0)
     */ 
    public void Var046()
    {
	String added = " -- Added 12/27/2006 for JDBC 4.0";
	if (checkJdbc40()) { 
	    try
	    {
		Statement s = connection_.createStatement ();
		ResultSet rs; 
		if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
		rs = s.executeQuery ("SELECT  concat(CITY,STATE) FROM QIWS.QCUSTCDT");
		} else { 
		rs = s.executeQuery ("SELECT {fn concat(CITY,STATE)} FROM QIWS.QCUSTCDT");
		}
		rs.close(); 
		boolean closed = JDReflectionUtil.callMethod_B(rs,"isClosed"); 
		s.close ();
		assertCondition (closed==true, "isClosed returned false sb true"+added);
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception"+added);
	    }
	}
    }


    /**
     * test isClosed when result set is closed.  Should return true
     *
     * @since 1.6 (JDBC 4.0)
     */ 
    public void Var047()
    {
	String added = " -- Added 12/27/2006 for JDBC 4.0";
	if (checkJdbc40()) { 
	    try
	    {
		Statement s = connection_.createStatement ();
		ResultSet rs;
		if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
		    rs = s.executeQuery ("SELECT concat(CITY,STATE) FROM QIWS.QCUSTCDT");
		} else { 
		    rs = s.executeQuery ("SELECT {fn concat(CITY,STATE)} FROM QIWS.QCUSTCDT");
		}
		s.close(); 
		boolean closed = JDReflectionUtil.callMethod_B(rs,"isClosed"); 
		assertCondition (closed==true, "isClosed returned false sb true"+added);
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception"+added);
	    }
	}
    }


    /**
     * test isClosed when connection has been closed.  Should return false.
     */ 
    public void Var048()
    {
	String added = " -- Added 12/27/2006 for JDBC 4.0";
	if (checkJdbc40()) { 
	    try
	    {
		Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
		  Statement s = c.createStatement ();
		  ResultSet rs; 
		if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
		    rs  = s.executeQuery ("SELECT concat(CITY,STATE) FROM QIWS.QCUSTCDT");
		} else {
		    rs  = s.executeQuery ("SELECT {fn concat(CITY,STATE)} FROM QIWS.QCUSTCDT");
		}
		c.close();
		boolean closed = JDReflectionUtil.callMethod_B(rs,"isClosed"); 
		assertCondition (closed==true, "isClosed returned false sb true after connection is closed"+added);
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception"+added);
	    }
	}
    }



    /**
     *  getConcurrency() -- Make sure stored procedure returns correct attributes for READ_ONLY cursor
     */
    public void Var049() {
	String added = " -- Added 03/01/2007";
	if (getRelease() >=   JDTestDriver.RELEASE_V7R1M0) {
	    try { 
		String procedureName = collection_+".JDRSMISC49";
                if(isToolboxDriver())  // Use JDTESTRS for colleciton as collecion might not exist for user
                    procedureName = JDRSTest.COLLECTION + ".JDRSMISC49";
		int    expectedConcurrency = ResultSet.CONCUR_READ_ONLY;
		String createProcedureSql = "CREATE PROCEDURE "+procedureName+"() "
                  + " RESULT SET 1 LANGUAGE SQL READS SQL DATA " 
                  + " JDRS49: BEGIN"
                  + "   DECLARE C49 CURSOR WITH RETURN FOR SELECT * FROM SYSIBM.SYSDUMMY1;"
                  + "   OPEN C49;"
                  + "   SET RESULT SETS CURSOR C49;"
                  + " END JDRS49";

		Statement statement = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
								   ResultSet.CONCUR_UPDATABLE);

		try {
		    statement.executeUpdate("DROP PROCEDURE "+procedureName); 
		} catch (Exception e) {
		}

                statement.executeUpdate(createProcedureSql); 
		
		ResultSet rs = statement.executeQuery ("CALL " + procedureName+"()");

		int concurrency = rs.getConcurrency();

		statement.executeUpdate("DROP PROCEDURE "+procedureName); 

		assertCondition(concurrency == expectedConcurrency,
				"Expected "+expectedConcurrency+" but got "+concurrency+added); 

	    } catch (Exception e) {
		failed (e, "Unexpected Exception"+added);
	    } 
	} else {
	    notApplicable("V5R5 and later variation"); 
	}  
    }
      

    /**
     *  getConcurrency() -- Make sure stored procedure returns correct attributes for UPDATEABLE cursor
     *  Note:   As of V6R1 a stored procedure cannot return an updatable cursor.  Make sure the
     *  setting is correct.  See issue 35474 for more information. 
     */
    public void Var050() {
        String added = " -- Added 03/01/2007";
        if (getRelease() >=   JDTestDriver.RELEASE_V7R1M0) {
          String sql = ""; 
            try { 
                String procedureName = collection_+".JDRSMISC50";
                String tableName =     collection_+".JDRSMISC50";
                if(isToolboxDriver())  // Use JDTESTRS for colleciton as collecion might not exist for user
                {
                    procedureName = JDRSTest.COLLECTION + ".JDRSMISC50";
                    tableName = JDRSTest.COLLECTION + ".JDRSMISC50";
                }

                int    expectedConcurrency = ResultSet.CONCUR_READ_ONLY;
                String insertTableSql = " INSERT INTO "+tableName+" VALUES('TESTING')"; 
                String createProcedureSql = "CREATE PROCEDURE "+procedureName+"() "
                  + " RESULT SET 1 LANGUAGE SQL READS SQL DATA " 
                  + " JDRS50: BEGIN"
                  + "   DECLARE C50 CURSOR WITH RETURN FOR SELECT * FROM "+tableName+" FOR UPDATE;"
                  + "   OPEN C50;"
                  + "   SET RESULT SETS CURSOR C50;"
                  + " END JDRS50";

                Statement statement = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_UPDATABLE);

                try {
                    statement.executeUpdate("DROP PROCEDURE "+procedureName); 
                } catch (Exception e) {
                }

                initTable(statement, tableName," (C1 VARCHAR(80)) "); ;
                sql = insertTableSql;   statement.executeUpdate(sql);
                sql = createProcedureSql;   statement.executeUpdate(sql);
                sql = "CALL " + procedureName+"()"; 
                ResultSet rs = statement.executeQuery (sql);

                int concurrency = rs.getConcurrency();
                rs.next(); 
                
                //
                // Verify that it is updateable
                //
                String cursorName = rs.getCursorName(); 
                boolean updateWorked = false; 
                try { 
                  Statement statement2 = connection_.createStatement ();
                  sql = "DELETE FROM "+tableName+" WHERE CURRENT OF "+cursorName; 
                  statement2.executeUpdate(sql);
                  statement2.close(); 
                  updateWorked = true; 
                } catch (Exception e) {
                   // System.out.println("FAILED : "+sql);
                   // System.out.println(" This was created using rs.getCursorName"); 
                   // e.printStackTrace(); 
                   updateWorked = false; 
                }
                sql = "DROP PROCEDURE "+procedureName; statement.executeUpdate(sql);  
                cleanupTable(statement, tableName);

                assertCondition(concurrency == expectedConcurrency && (!updateWorked),
                                "UpdateWorked = "+updateWorked+" sb false : Expected "+expectedConcurrency+" but got "+concurrency+added); 

            } catch (Exception e) {
                failed (e, "Unexpected Exception SQL="+sql+" "+added);
            } 
        } else {
            notApplicable("V5R5 and later variation"); 
        }  
    }
      


    /**
     *  getHoldability() -- Make sure stored procedure returns correct attributes for HOLD_CURSORS_OVER_COMMIT cursor
     */
    public void Var051() {
      String added = " -- Added 03/01/2007";
      if (checkJdbc40()) {
        if (getRelease() >=   JDTestDriver.RELEASE_V7R1M0) {
          String sql = ""; 
          try { 
            String procedureName = collection_+".JDRSMISC51";
            String tableName =     collection_+".JDRSMISC51";
            if(isToolboxDriver())  // Use JDTESTRS for colleciton as collecion might not exist for user
            {
                procedureName = JDRSTest.COLLECTION + ".JDRSMISC51";
                tableName = JDRSTest.COLLECTION + ".JDRSMISC51";
            }
            
            int    expectedHoldability = ResultSet.HOLD_CURSORS_OVER_COMMIT;
            String insertTableSql = " INSERT INTO "+tableName+" VALUES('TESTING')"; 
            String createProcedureSql = "CREATE PROCEDURE "+procedureName+"() "
            + " RESULT SET 1 LANGUAGE SQL READS SQL DATA " 
            + " JDRS51: BEGIN"
            + "   DECLARE C51 CURSOR WITH HOLD WITH RETURN FOR SELECT * FROM "+tableName+" FOR UPDATE;"
            + "   OPEN C51;"
            + "   SET RESULT SETS CURSOR C51;"
            + " END JDRS51";
            
            Connection connection = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            
            
            Statement statement = connection.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE,
                ResultSet.CLOSE_CURSORS_AT_COMMIT);
            
            try {
              statement.executeUpdate("DROP PROCEDURE "+procedureName); 
            } catch (Exception e) {
            }
            
            connection.commit(); 
            initTable(statement, tableName, " (C1 VARCHAR(80)) "); 
            sql = insertTableSql;   statement.executeUpdate(sql);
            sql = insertTableSql;   statement.executeUpdate(sql);
            sql = insertTableSql;   statement.executeUpdate(sql);
            sql = createProcedureSql;   statement.executeUpdate(sql);
            connection.commit();
            
            sql = "CALL " + procedureName+"()"; 
            ResultSet rs = statement.executeQuery (sql);
            
            int holdability = JDReflectionUtil.callMethod_I(rs, "getHoldability"); 
            rs.next(); 
            
            //
            // Verify that it is holdable. 
            //
            boolean holdable; 
            try {
              connection.commit(); 
              rs.next(); 
              holdable = true; 
            } catch (Exception e) {
              System.out.println("Using cursor after commit failed\n"); 
              e.printStackTrace(); 
              holdable = false; 
            }
            sql = "DROP PROCEDURE "+procedureName; statement.executeUpdate(sql);  
            cleanupTable(statement, tableName);
            
            connection.close();
            
            assertCondition(holdability == expectedHoldability && holdable,
                "Holdable = "+holdable+" : Expected "+expectedHoldability+" but got "+holdability); 
            
          } catch (Exception e) {
            failed (e, "Unexpected Exception SQL="+sql+" "+added);
          } 
        } else {
          notApplicable("V5R5 and later variation"); 
        }
      }
    }
      
    /**
     *  getHoldability() -- Make sure stored procedure returns correct attributes for CLOSE_CURSORS_AT_COMMIT cursor
     */
    public void Var052() {
      String added = " -- Added 03/01/2007";
      if (checkJdbc40()) {
        if (getRelease() >=   JDTestDriver.RELEASE_V7R1M0) {
          String sql = ""; 
          try { 
            String procedureName = collection_+".JDRSMISC52";
            String tableName =     collection_+".JDRSMISC52";

            if(isToolboxDriver())  // Use JDTESTRS for colleciton as collecion might not exist for user
            {
                procedureName = JDRSTest.COLLECTION + ".JDRSMISC52";
                tableName = JDRSTest.COLLECTION + ".JDRSMISC52";
            }
            
            int    expectedHoldability = ResultSet.CLOSE_CURSORS_AT_COMMIT; 
            String createTableDefinition = " (C1 VARCHAR(80)) ";
            String insertTableSql = " INSERT INTO "+tableName+" VALUES('TESTING')"; 
            String createProcedureSql = "CREATE PROCEDURE "+procedureName+"() "
            + " RESULT SET 1 LANGUAGE SQL READS SQL DATA " 
            + " JDRS52: BEGIN"
            + "   DECLARE C52 CURSOR WITHOUT HOLD WITH RETURN FOR SELECT * FROM "+tableName+" FOR UPDATE WITH CS;"
            + "   OPEN C52;"
            + "   SET RESULT SETS CURSOR C52;"
            + " END JDRS52";
            
            Connection connection = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            
            
            Statement statement = connection.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE,
                ResultSet.HOLD_CURSORS_OVER_COMMIT);
            
            try {
              statement.executeUpdate("DROP PROCEDURE "+procedureName); 
            } catch (Exception e) {
            }
            
            connection.commit(); 
            initTable(statement, tableName, createTableDefinition); 
            sql = insertTableSql;   statement.executeUpdate(sql);
            sql = insertTableSql;   statement.executeUpdate(sql);
            sql = insertTableSql;   statement.executeUpdate(sql);
            sql = createProcedureSql;   statement.executeUpdate(sql);
            connection.commit();
            
            sql = "CALL " + procedureName+"()"; 
            ResultSet rs = statement.executeQuery (sql);
            
            int holdability = JDReflectionUtil.callMethod_I(rs, "getHoldability"); 
            rs.next(); 
            
            //
            // Verify that it is holdable. 
            //
            boolean holdable; 
            try {
              connection.commit(); 
              rs.next(); 
              holdable = true; 
            } catch (Exception e) {
              holdable = false; 
            }
            sql = "DROP PROCEDURE "+procedureName; statement.executeUpdate(sql);  
            cleanupTable(statement, tableName);
            
            connection.close();
            
            assertCondition(holdability == expectedHoldability && (!holdable),
                "Holdable = "+holdable+" : Expected "+expectedHoldability+" but got "+holdability +  " - V7R1 *none - (needs hostserver support)"); 
            
          } catch (Exception e) {
            failed (e, "Unexpected Exception SQL="+sql+" "+added);
          } 
        } else {
          notApplicable("V5R5 and later variation"); 
        }
      }
    }
      
    /**
     *  getType() -- Make sure stored procedure returns correct attributes for TYPE_FORWARD_ONLY cursor when
     *               requesting TYPE_FORWARD_ONLY
     */
    public void Var053() {
        String added = " -- Added 03/01/2007";
        if (getRelease() >=   JDTestDriver.RELEASE_V7R1M0) {
            try { 
                String procedureName = collection_+".JDRSMISC53";
                if(isToolboxDriver())  // Use JDTESTRS for colleciton as collecion might not exist for user
                    procedureName = JDRSTest.COLLECTION + ".JDRSMISC53";
                int    expectedType = ResultSet.TYPE_FORWARD_ONLY;
                String createProcedureSql = "CREATE PROCEDURE "+procedureName+"() "
                  + " RESULT SET 1 LANGUAGE SQL READS SQL DATA " 
                  + " JDRS53: BEGIN"
                  + "   DECLARE C53 NO SCROLL CURSOR WITH RETURN FOR "
                  + "   SELECT * FROM SYSIBM.SQLTABLES;"
                  + "   OPEN C53;"
                  + "   SET RESULT SETS CURSOR C53;"
                  + " END JDRS53";

                Statement statement = connection_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
                                                                   ResultSet.CONCUR_UPDATABLE);

                try {
                    statement.executeUpdate("DROP PROCEDURE "+procedureName); 
                } catch (Exception e) {
                }

                statement.executeUpdate(createProcedureSql); 
                
                ResultSet rs = statement.executeQuery ("CALL " + procedureName+"()");

                int rsType = rs.getType();

                rs.next(); 
                rs.next();
                boolean scrollable; 
                try { 
                  rs.previous(); 
                  scrollable = true; 
                } catch (Exception e) { 
                  scrollable = false; 
                }
                
                
                statement.executeUpdate("DROP PROCEDURE "+procedureName); 

                assertCondition((!scrollable) && (rsType  == expectedType),
                                "Scrollable = "+scrollable+" (sb false) : Expected Type "+expectedType+" but got "+rsType+added); 

            } catch (Exception e) {
                failed (e, "Unexpected Exception"+added);
            } 
        } else {
            notApplicable("V5R5 and later variation"); 
        }  
    }


    /**
     *  getType() -- Make sure stored procedure returns correct attributes for TYPE_FORWARD_ONLY cursor when
     *               requesting SCROLL_INSENSITIVE
     */
    public void Var054() {
        String added = " -- Added 03/01/2007";
        if (getRelease() >=   JDTestDriver.RELEASE_V7R1M0) {
            try { 
                String procedureName = collection_+".JDRSMISC53";
                if(isToolboxDriver())  // Use JDTESTRS for colleciton as collecion might not exist for user
                    procedureName = JDRSTest.COLLECTION + ".JDRSMISC53";
                int    expectedType = ResultSet.TYPE_FORWARD_ONLY;
                String createProcedureSql = "CREATE PROCEDURE "+procedureName+"() "
                  + " RESULT SET 1 LANGUAGE SQL READS SQL DATA " 
                  + " JDRS53: BEGIN"
                  + "   DECLARE C53 NO SCROLL CURSOR WITH RETURN FOR "
                  + "   SELECT * FROM SYSIBM.SQLTABLES;"
                  + "   OPEN C53;"
                  + "   SET RESULT SETS CURSOR C53;"
                  + " END JDRS53";

                Statement statement = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_UPDATABLE);

                try {
                    statement.executeUpdate("DROP PROCEDURE "+procedureName); 
                } catch (Exception e) {
                }

                statement.executeUpdate(createProcedureSql); 
                
                ResultSet rs = statement.executeQuery ("CALL " + procedureName+"()");

                int rsType = rs.getType();

                rs.next(); 
                rs.next();
                boolean scrollable; 
                try { 
                  rs.previous(); 
                  scrollable = true; 
                } catch (Exception e) { 
                  scrollable = false; 
                }
                
                
                statement.executeUpdate("DROP PROCEDURE "+procedureName); 

                assertCondition((!scrollable) && (rsType  == expectedType),
                                "Scrollable = "+scrollable+" (sb false) : Expected Type "+expectedType+" but got "+rsType+added); 

            } catch (Exception e) {
                failed (e, "Unexpected Exception"+added);
            } 
        } else {
            notApplicable("V5R5 and later variation"); 
        }  
    }
      

    /**
     *  getType() -- Make sure stored procedure returns correct attributes for TYPE_FORWARD_ONLY cursor when
     *               requesting SCROLL_SENSITIVE
     */
    public void Var055() {
        String added = " -- Added 03/01/2007";
        if (getRelease() >=   JDTestDriver.RELEASE_V7R1M0) {
            try { 
                String procedureName = collection_+".JDRSMISC53";
                if(isToolboxDriver())  // Use JDTESTRS for colleciton as collecion might not exist for user
                    procedureName = JDRSTest.COLLECTION + ".JDRSMISC53";
                int    expectedType = ResultSet.TYPE_FORWARD_ONLY;
                String createProcedureSql = "CREATE PROCEDURE "+procedureName+"() "
                  + " RESULT SET 1 LANGUAGE SQL READS SQL DATA " 
                  + " JDRS53: BEGIN"
                  + "   DECLARE C53 NO SCROLL CURSOR WITH RETURN FOR "
                  + "   SELECT * FROM SYSIBM.SQLTABLES;"
                  + "   OPEN C53;"
                  + "   SET RESULT SETS CURSOR C53;"
                  + " END JDRS53";

                Statement statement = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                                   ResultSet.CONCUR_UPDATABLE);

                try {
                    statement.executeUpdate("DROP PROCEDURE "+procedureName); 
                } catch (Exception e) {
                }

                statement.executeUpdate(createProcedureSql); 
                
                ResultSet rs = statement.executeQuery ("CALL " + procedureName+"()");

                int rsType = rs.getType();

                rs.next(); 
                rs.next();
                boolean scrollable; 
                try { 
                  rs.previous(); 
                  scrollable = true; 
                } catch (Exception e) { 
                  scrollable = false; 
                }
                
                
                statement.executeUpdate("DROP PROCEDURE "+procedureName); 

                assertCondition((!scrollable) && (rsType  == expectedType),
                                "Scrollable = "+scrollable+" (sb false) : Expected Type "+expectedType+" but got "+rsType+added); 

            } catch (Exception e) {
                failed (e, "Unexpected Exception"+added);
            } 
        } else {
            notApplicable("V5R5 and later variation"); 
        }  
    }
      

    /**
     *  getType() -- Make sure stored procedure returns correct attributes for TYPE_SCROLL_INSENSITIVE cursor when requesting FORWARD_ONLY.  In this case, the type should be forward only. 
     */
    public void Var056() {
        String added = " -- Added 03/01/2007";
        if (getRelease() >=   JDTestDriver.RELEASE_V7R1M0) {
            try { 
                String procedureName = collection_+".JDRSMISC54";
                int    expectedType = ResultSet.TYPE_FORWARD_ONLY;
                String createProcedureSql = "CREATE PROCEDURE "+procedureName+"() "
                  + " RESULT SET 1 LANGUAGE SQL READS SQL DATA " 
                  + " JDRS54: BEGIN"
                  + "   DECLARE C54 INSENSITIVE SCROLL CURSOR WITH RETURN FOR "
                  + "   SELECT * FROM SYSIBM.SQLTABLES;"
                  + "   OPEN C54;"
                  + "   SET RESULT SETS CURSOR C54;"
                  + " END JDRS54";

                Statement statement = connection_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
                                                                   ResultSet.CONCUR_READ_ONLY);

                try {
                    statement.executeUpdate("DROP PROCEDURE "+procedureName); 
                } catch (Exception e) {
                }

                statement.executeUpdate(createProcedureSql); 
                
                ResultSet rs = statement.executeQuery ("CALL " + procedureName+"()");

                int rsType = rs.getType();

                rs.next(); 
                rs.next();
                boolean scrollable; 
                try { 
                  rs.previous(); 
                  scrollable = true; 
                } catch (Exception e) { 
                  scrollable = false; 
                }
                
                
                statement.executeUpdate("DROP PROCEDURE "+procedureName); 

                assertCondition((!scrollable) && (rsType  == expectedType),
                                "Scrollable = "+scrollable+" (sb false) : Expected Type "+expectedType+" but got "+rsType+added); 

            } catch (Exception e) {
                failed (e, "Unexpected Exception"+added);
            } 
        } else {
            notApplicable("V5R5 and later variation"); 
        }  
    }
      

    /**
     *  getType() -- Make sure stored procedure returns correct attributes for TYPE_SCROLL_INSENSITIVE cursor when requesting TYPE_SCROLL_INSENSITIVE.
     */
    public void Var057() {
        String added = " -- Added 03/01/2007";
        if (getRelease() >=   JDTestDriver.RELEASE_V7R1M0) {
            try { 
                String procedureName = collection_+".JDRSMISC54";
                int    expectedType = ResultSet.TYPE_SCROLL_INSENSITIVE;
                String createProcedureSql = "CREATE PROCEDURE "+procedureName+"() "
                  + " RESULT SET 1 LANGUAGE SQL READS SQL DATA " 
                  + " JDRS54: BEGIN"
                  + "   DECLARE C54 INSENSITIVE SCROLL CURSOR WITH RETURN FOR "
                  + "   SELECT * FROM SYSIBM.SQLTABLES;"
                  + "   OPEN C54;"
                  + "   SET RESULT SETS CURSOR C54;"
                  + " END JDRS54";

                Statement statement = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);

                try {
                    statement.executeUpdate("DROP PROCEDURE "+procedureName); 
                } catch (Exception e) {
                }

                statement.executeUpdate(createProcedureSql); 
                
                ResultSet rs = statement.executeQuery ("CALL " + procedureName+"()");

                int rsType = rs.getType();

                rs.next(); 
                rs.next();
                boolean scrollable; 
                try { 
                  rs.previous(); 
                  scrollable = true; 
                } catch (Exception e) { 
                  scrollable = false; 
                }
                
                
                statement.executeUpdate("DROP PROCEDURE "+procedureName); 

                assertCondition((scrollable) && (rsType  == expectedType),
                                "Scrollable = "+scrollable+" (sb true) : Expected Type "+expectedType+" but got "+rsType+added); 

            } catch (Exception e) {
                failed (e, "Unexpected Exception"+added);
            } 
        } else {
            notApplicable("V5R5 and later variation"); 
        }  
    }
      
    /**
     *  getType() -- Make sure stored procedure returns correct attributes for TYPE_SCROLL_INSENSITIVE cursor when requesting TYPE_SCROLL_SENSITIVE.
     */
    public void Var058() {
        String added = " -- Added 03/01/2007";
        if (getRelease() >=   JDTestDriver.RELEASE_V7R1M0) {
            try { 
                String procedureName = collection_+".JDRSMISC54";
		            int    expectedType = ResultSet.TYPE_SCROLL_INSENSITIVE;
                String createProcedureSql = "CREATE PROCEDURE "+procedureName+"() "
                  + " RESULT SET 1 LANGUAGE SQL READS SQL DATA " 
                  + " JDRS54: BEGIN"
                  + "   DECLARE C54 INSENSITIVE SCROLL CURSOR WITH RETURN FOR "
                  + "   SELECT * FROM SYSIBM.SQLTABLES;"
                  + "   OPEN C54;"
                  + "   SET RESULT SETS CURSOR C54;"
                  + " END JDRS54";

                Statement statement = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);

                try {
                    statement.executeUpdate("DROP PROCEDURE "+procedureName); 
                } catch (Exception e) {
                }

                statement.executeUpdate(createProcedureSql); 
                
                ResultSet rs = statement.executeQuery ("CALL " + procedureName+"()");

                int rsType = rs.getType();

                rs.next(); 
                rs.next();
                boolean scrollable; 
                try { 
                  rs.previous(); 
                  scrollable = true; 
                } catch (Exception e) { 
                  scrollable = false; 
                }
                
                
                statement.executeUpdate("DROP PROCEDURE "+procedureName); 

                assertCondition((scrollable) && (rsType  == expectedType),
                                "Scrollable = "+scrollable+" (sb true) : Expected Type "+expectedType+" but got "+rsType+added); 

            } catch (Exception e) {
                failed (e, "Unexpected Exception"+added);
            } 
        } else {
            notApplicable("V5R5 and later variation"); 
        }  
    }
      


    /**
     *  getConcurrency() -- Make sure stored procedure returns correct attributes for 
     *  TYPE_SCROLL_SENSITIVE cursor when requeseting TYPE_FORWARD ONLY 
     */
    public void Var059() {
        String added = " -- Added 03/01/2007";
        if (getRelease() >=   JDTestDriver.RELEASE_V7R1M0) {
            try { 
                String procedureName = collection_+".JDRSMISC55";
                if(isToolboxDriver())  // Use JDTESTRS for colleciton as collecion might not exist for user
                    procedureName = JDRSTest.COLLECTION + ".JDRSMISC55";
                int    expectedType = ResultSet.TYPE_FORWARD_ONLY;
                String createProcedureSql = "CREATE PROCEDURE "+procedureName+"() "
                  + " RESULT SET 1 LANGUAGE SQL READS SQL DATA " 
                  + " JDRS55: BEGIN"
                  + "   DECLARE C55 SENSITIVE SCROLL CURSOR WITH RETURN FOR "
                  + "   SELECT * FROM SYSIBM.SQLTABLES;"
                  + "   OPEN C55;"
                  + "   SET RESULT SETS CURSOR C55;"
                  + " END JDRS55";

                Statement statement = connection_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
                                                                   ResultSet.CONCUR_READ_ONLY);

                try {
                    statement.executeUpdate("DROP PROCEDURE "+procedureName); 
                } catch (Exception e) {
                }

                statement.executeUpdate(createProcedureSql); 
                
                ResultSet rs = statement.executeQuery ("CALL " + procedureName+"()");

                int rsType = rs.getType();

                rs.next(); 
                rs.next();
                boolean scrollable; 
                try { 
                  rs.previous(); 
                  scrollable = true; 
                } catch (Exception e) { 
                  scrollable = false; 
                }
                
                
                statement.executeUpdate("DROP PROCEDURE "+procedureName); 

                assertCondition((!scrollable) && (rsType  == expectedType),
                                "Scrollable = "+scrollable+" (sb false) : Expected Type "+expectedType+" but got "+rsType+added); 

            } catch (Exception e) {
                failed (e, "Unexpected Exception"+added);
            } 
        } else {
            notApplicable("V5R5 and later variation"); 
        }  
    }
      


    /**
     *  getConcurrency() -- Make sure stored procedure returns correct attributes for 
     *  TYPE_SCROLL_SENSITIVE cursor when requeseting TYPE_SCROLL_SENSITIVE
     */
    public void Var060() {
        String added = " -- Added 03/01/2007";
        if (getRelease() >=   JDTestDriver.RELEASE_V7R1M0) {
            try { 
                String procedureName = collection_+".JDRSMISC55";
                if(isToolboxDriver())  // Use JDTESTRS for colleciton as collecion might not exist for user
                    procedureName = JDRSTest.COLLECTION + ".JDRSMISC55";
                int    expectedType = ResultSet.TYPE_SCROLL_SENSITIVE;
                String createProcedureSql = "CREATE PROCEDURE "+procedureName+"() "
                  + " RESULT SET 1 LANGUAGE SQL READS SQL DATA " 
                  + " JDRS55: BEGIN"
                  + "   DECLARE C55 SENSITIVE SCROLL CURSOR WITH RETURN FOR "
                  + "   SELECT * FROM SYSIBM.SQLTABLES;"
                  + "   OPEN C55;"
                  + "   SET RESULT SETS CURSOR C55;"
                  + " END JDRS55";

                Statement statement = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);

                try {
                    statement.executeUpdate("DROP PROCEDURE "+procedureName); 
                } catch (Exception e) {
                }

                statement.executeUpdate(createProcedureSql); 
                
                ResultSet rs = statement.executeQuery ("CALL " + procedureName+"()");

                int rsType = rs.getType();

                rs.next(); 
                rs.next();
                boolean scrollable; 
                try { 
                  rs.previous(); 
                  scrollable = true; 
                } catch (Exception e) { 
                  scrollable = false; 
                }
                
                
                statement.executeUpdate("DROP PROCEDURE "+procedureName); 

                assertCondition((scrollable) && (rsType  == expectedType),
                                "Scrollable = "+scrollable+" (sb true) : Expected Type "+expectedType+" but got "+rsType+added); 

            } catch (Exception e) {
                failed (e, "Unexpected Exception"+added);
            } 
        } else {
            notApplicable("V5R5 and later variation"); 
        }  
    }


    /**
     *  getConcurrency() -- Make sure stored procedure returns correct attributes for 
     *  TYPE_SCROLL_SENSITIVE cursor when requeseting TYPE_SCROLL_INSENSITIVE
     */
    public void Var061() {
        String added = " -- Added 03/01/2007";
        if (getRelease() >=   JDTestDriver.RELEASE_V7R1M0) {
            try { 
                String procedureName = collection_+"JDRSMISC55";
                // Use JDTESTRS for colleciton as collecion might not exist for user
                    procedureName = JDRSTest.COLLECTION + ".JDRSMISC55";
                int    expectedType = ResultSet.TYPE_SCROLL_SENSITIVE;
                String createProcedureSql = "CREATE PROCEDURE "+procedureName+"() "
                  + " RESULT SET 1 LANGUAGE SQL READS SQL DATA " 
                  + " JDRS55: BEGIN"
                  + "   DECLARE C55 SENSITIVE SCROLL CURSOR WITH RETURN FOR "
                  + "   SELECT * FROM SYSIBM.SQLTABLES;"
                  + "   OPEN C55;"
                  + "   SET RESULT SETS CURSOR C55;"
                  + " END JDRS55";

                Statement statement = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);

                try {
                    statement.executeUpdate("DROP PROCEDURE "+procedureName); 
                } catch (Exception e) {
                }

                statement.executeUpdate(createProcedureSql); 
                
                ResultSet rs = statement.executeQuery ("CALL " + procedureName+"()");

                int rsType = rs.getType();

                rs.next(); 
                rs.next();
                boolean scrollable; 
                try { 
                  rs.previous(); 
                  scrollable = true; 
                } catch (Exception e) { 
                  scrollable = false; 
                }
                
                
                statement.executeUpdate("DROP PROCEDURE "+procedureName); 

                assertCondition((scrollable) && (rsType  == expectedType),
                                "Scrollable = "+scrollable+" (sb true) : Expected Type "+expectedType+" but got "+rsType+added); 

            } catch (Exception e) {
                failed (e, "Unexpected Exception"+added);
            } 
        } else {
            notApplicable("V5R5 and later variation"); 
        }  
    }
      



    public void Var062() {
        if (isToolboxDriver())
        {
            notApplicable("Takes too long.");
            return;
        }
        if (getDriver() == JDTestDriver.DRIVER_NATIVE && ( getDriverFixLevel() < 27438)) {
            notApplicable("Native driver needs SI27438 or later.. Current "+getDriverFixLevel());
            return; 
        }
        String added = " -- Added 04/30/2007 Bug fixed by native driver SE28737 SI27438(V5R3) ???(V5R4) ";
        if (getRelease() >=   JDTestDriver.RELEASE_V7R1M0) {
            try {
                int runIterations = 20000; 
                int iteration = 0;
                boolean passed=true;
                StringBuffer sb = new StringBuffer(); 
                String tableName = collection_+".JDRSMISC56";
                
                String createTableDefinition = " ("+
                " C1 VARCHAR(80) CCSID 870, "+
                " C2 VARCHAR(80) CCSID 870, "+
                " C3 VARCHAR(80) CCSID 870, "+
                " C4 VARCHAR(80) CCSID 870, "+
                " C5 VARCHAR(80) CCSID 870, "+
                " C6 VARCHAR(80) CCSID 870, "+
                " C7 VARCHAR(80) CCSID 870, "+
                " C8 VARCHAR(80) CCSID 870, "+
                " C9 VARCHAR(80) CCSID 870, "+
                " C10 VARCHAR(80) CCSID 870, "+
                " C11 VARCHAR(80) CCSID 870, "+
                " C12 VARCHAR(80) CCSID 870, "+
                " C13 VARCHAR(80) CCSID 870, "+
                " C14 VARCHAR(80) CCSID 870, "+
                " C15 VARCHAR(80) CCSID 870, "+
                " C16 VARCHAR(80) CCSID 870, "+
                " C17 VARCHAR(80) CCSID 870, "+
                " C18 VARCHAR(80) CCSID 870, "+
                " C19 VARCHAR(80) CCSID 870, "+
                " C20 VARCHAR(80) CCSID 870, "+
                " C21 VARCHAR(80) CCSID 870, "+
                " C22 VARCHAR(80) CCSID 870, "+
                " C23 VARCHAR(80) CCSID 870, "+
                " C24 VARCHAR(80) CCSID 870, "+
                " C25 VARCHAR(80) CCSID 870, "+
                " C26 VARCHAR(80) CCSID 870, "+
                " C27 VARCHAR(80) CCSID 870, "+
                " C28 VARCHAR(80) CCSID 870, "+
                " C29 VARCHAR(80) CCSID 870, "+
                " C30 VARCHAR(80) CCSID 870, "+
                " C31 VARCHAR(80) CCSID 870, "+
                " C32 VARCHAR(80) CCSID 870, "+
                " C33 VARCHAR(80) CCSID 870, "+
                " C34 VARCHAR(80) CCSID 870, "+
                " C35 VARCHAR(80) CCSID 870, "+
                " C36 VARCHAR(80) CCSID 870, "+
                " C37 VARCHAR(80) CCSID 870, "+
                " C38 VARCHAR(80) CCSID 870, "+
                " C39 VARCHAR(80) CCSID 870, "+
                " C40 VARCHAR(80) CCSID 870) ";

                String insertSql = "INSERT INTO "+tableName+" VALUES("+
                " 'C1', "+
                " 'C2', "+
                " 'C3', "+
                " 'C4', "+
                " 'C5', "+
                " 'C6', "+
                " 'C7', "+
                " 'C8', "+
                " 'C9', "+
                " 'C10', "+
                " 'C11', "+
                " 'C12', "+
                " 'C13', "+
                " 'C14', "+
                " 'C15', "+
                " 'C16', "+
                " 'C17', "+
                " 'C18', "+
                " 'C19', "+
                " 'C20', "+
                " 'C21', "+
                " 'C22', "+
                " 'C23', "+
                " 'C24', "+
                " 'C25', "+
                " 'C26', "+
                " 'C27', "+
                " 'C28', "+
                " 'C29', "+
                " 'C30', "+
                " 'C31', "+
                " 'C32', "+
                " 'C33', "+
                " 'C34', "+
                " 'C35', "+
                " 'C36', "+
                " 'C37', "+
                " 'C38', "+
                " 'C39', "+
                " 'C40') ";



                Statement statement = connection_.createStatement (); 

                

                initTable(statement, tableName, createTableDefinition); 
               
                statement.executeUpdate(insertSql);

                while (passed && iteration < runIterations) {
                    ResultSet rs = statement.executeQuery("select * from "+tableName);
                    rs.next();
                    String s = rs.getString(1); 
                    if ("C1".equals(s)) {
                        // ok 
                    } else {
                        passed=false;
                        sb.append("Failed on iteration "+iteration+" got '"+s+"'"); 
                    } 
                    iteration++; 
                } 

                cleanupTable(statement, tableName); 		

                assertCondition(passed, sb.toString()+added);

            } catch (Exception e) {
                failed (e, "Unexpected Exception"+added);
            } 
        } else {
            notApplicable("V5R3 and later variation"); 
        }  
    }


    /**
    getHoldability() - Should return the Holdability ResultSet.HOLD_CURSORS_OVER_COMMIT.
    **/
        public void Var063 ()
        {
            if (checkJdbc40 ()) {
            try {
                Statement statement = connection2_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = statement.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
                int holdability = JDReflectionUtil.callMethod_I(rs, "getHoldability");
                //ResultSet.HOLD_CURSORS_OVER_COMMIT or ResultSet.CLOSE_CURSORS_AT_COMMIT
                rs.close ();
                statement.close ();
                assertCondition (holdability == ResultSet.HOLD_CURSORS_OVER_COMMIT);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
            }
        }
        

        /**
        getHoldability() - Should return the Holdability ResultSet.CLOSE_CURSORS_AT_COMMIT.
        **/
	public void Var064 ()
	{
	    if (checkJdbc40 ()) {
		try {
		    Statement statement = connection3_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
									ResultSet.CONCUR_READ_ONLY);
		    ResultSet rs = statement.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
		    int holdability = JDReflectionUtil.callMethod_I(rs, "getHoldability");
		    //ResultSet.HOLD_CURSORS_OVER_COMMIT or ResultSet.CLOSE_CURSORS_AT_COMMIT
		    rs.close ();
		    statement.close ();
		    if(getRelease() >=   JDTestDriver.RELEASE_V7R1M0)
                assertCondition (holdability == ResultSet.HOLD_CURSORS_OVER_COMMIT, "holdability="+holdability+" expected HOLD_CURSORS_OVER_COMMIT="+ResultSet.HOLD_CURSORS_OVER_COMMIT+" V7R1 *none - (needs hostserver support)");
		    else
		        assertCondition (holdability == ResultSet.CLOSE_CURSORS_AT_COMMIT);
		}
		catch (Exception e) {
		    failed (e, "Unexpected Exception");
		}
	    }
	}

     
            /**
            getType() - Verify that stored procedure calls resultset.getType is correct (sp is read-only and scrollable)
            **/
	public void Var065 ()
	{
	    if (checkJdbc20 ()) {
		   // Trace.setTraceJDBCOn(true);
		   // Trace.setTraceOn(true);
		    //Trace.setTraceDatastreamOn(true);
		try {
			JDSetupProcedure.create (systemObject_,
						 connection_, JDSetupProcedure.STP_RS1S, supportedFeatures_,collection_);
		    Statement statement = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
								       ResultSet.CONCUR_READ_ONLY);


		    ResultSet rs = statement.executeQuery ("CALL " + JDSetupProcedure.STP_RS1S);
		    int type = rs.getType ();

		    rs.close ();
		    statement.close ();
		    if (getRelease() <= JDTestDriver.RELEASE_V7R1M0)  {
                        // Last changed 2/23/2010 for java test.JDRunit 543CN JDRSMisc                        
			assertCondition (type == ResultSet.TYPE_SCROLL_SENSITIVE, "type is "+type+" Driver is V5R4 or less"); //same that creatStatement()
		    } else { 
			assertCondition (type == ResultSet.TYPE_SCROLL_SENSITIVE || type == ResultSet.TYPE_SCROLL_INSENSITIVE,
					 "Metadata RS Type = "+type+" sb TYPE_SCROLL_SENSITIVE/INSENSITIVIE="+ResultSet.TYPE_SCROLL_SENSITIVE+"/"+ResultSet.TYPE_SCROLL_INSENSITIVE
					 ); //diff that creatStatement() - doc says default is asensitive and could result in sens or insens
		    }
		}
		catch (Exception e) {
		    failed (e, "Unexpected Exception");
		}
	    }
	}


            /**
            getConcurrency() - Verify that stored procedure calls are read only.
            **/
	public void Var066 ()
	{

	    if (checkJdbc20 ()) {
		try {
			JDSetupProcedure.dropProcedure(connection_, JDSetupProcedure.STP_RS1);
			JDSetupProcedure.create (systemObject_,
						 connection_, JDSetupProcedure.STP_RS1, supportedFeatures_, collection_);
		    Statement statement = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
								       ResultSet.CONCUR_UPDATABLE);
		    ResultSet rs = statement.executeQuery ("CALL " + JDSetupProcedure.STP_RS1);
		    int concurrency = rs.getConcurrency ();
		    rs.close ();
		    statement.close ();
		    assertCondition (concurrency == ResultSet.CONCUR_READ_ONLY); //diff from createStatement() input
		}
		catch (Exception e) {
		    failed (e, "Unexpected Exception");
		}
	    }
	}


		/**
		getConcurrency() - Verify that stored procedure calls are updatable.
		**/
	public void Var067 ()
	{
	    if (checkJdbc20 ()) {
		try {
			JDSetupProcedure.create (systemObject_,
						 connection_, JDSetupProcedure.STP_RS1UPD, supportedFeatures_, collection_);
		    Statement statement = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
								       ResultSet.CONCUR_UPDATABLE);
		    ResultSet rs = statement.executeQuery ("CALL " + JDSetupProcedure.STP_RS1UPD);


		    int concurrency = rs.getConcurrency ();
		    rs.close ();
		    statement.close ();
		    assertCondition (concurrency == ResultSet.CONCUR_READ_ONLY); //doc says stored procedure cursors are always read-only
			    /*Any attempts to use an updateable cursor with a stored procedure is ignored. Stored procedure result sets are read-only.
			     * 
			     */

		}
		catch (Exception e) {
		    failed (e, "Unexpected Exception");
		}
	    }
	}

		/**
		getHoldability() - Verify that stored procedure calls are with hold
		**/
	public void Var068 ()
	{
	    if (checkJdbc40 ()) {
		try {
			JDSetupProcedure.create (systemObject_,
						 connection3_, JDSetupProcedure.STP_RS1WH, supportedFeatures_, collection_); //conn3
		    Statement statement = connection3_.createStatement (); //conn3
		    ResultSet rs = statement.executeQuery ("CALL " + JDSetupProcedure.STP_RS1WH);
		    int hold = JDReflectionUtil.callMethod_I(rs, "getHoldability");
		    rs.close ();
		    statement.close ();

		    if (getRelease() <= JDTestDriver.RELEASE_V7R1M0)       
			assertCondition (hold == ResultSet.CLOSE_CURSORS_AT_COMMIT); //conn3 is not holdable
		    else
			assertCondition (hold == ResultSet.HOLD_CURSORS_OVER_COMMIT); //from STP_RS1WH
		}
		catch (Exception e) {
		    failed (e, "Unexpected Exception");
		}
	    }
	}

                    /**
                    getHoldability() - Verify that stored procedure calls are with out hold
                    **/
	public void Var069 ()
	{



	    if (checkJdbc40 ()) {
		try {
			JDSetupProcedure.create (systemObject_,
						 connection2_, JDSetupProcedure.STP_RS1WOH, supportedFeatures_, collection_); //conn2
		    Statement statement = connection2_.createStatement (); //conn2
		    ResultSet rs = statement.executeQuery ("CALL " + JDSetupProcedure.STP_RS1WOH);
		    int hold = JDReflectionUtil.callMethod_I(rs, "getHoldability");
		    rs.close ();
		    statement.close ();
		    if (getRelease() <= JDTestDriver.RELEASE_V7R1M0)       
			    assertCondition (hold == ResultSet.HOLD_CURSORS_OVER_COMMIT); //conn2 is holdable
		    else if(getRelease() >=   JDTestDriver.RELEASE_V7R1M0)
			assertCondition (hold == ResultSet.HOLD_CURSORS_OVER_COMMIT, "hold="+hold+" expected HOLD_CURSORS_OVER_COMMIT -- V7R1 *none - (needs hostserver support)");
		    else	
			assertCondition (hold == ResultSet.CLOSE_CURSORS_AT_COMMIT); //STP_RS1WH
		}
		catch (Exception e) {
		    failed (e, "Unexpected Exception");
		}
	    }
	}
/*todo when support is added on hstserver

		       // 3 cursors - Verify diff attrs 

			    public void Var063 ()
			    {

				if (checkJdbc20 ()) {
				try {
				    if(collection_ != null) {
					JDSetupProcedure.create (systemObject_,
					    connection2_, JDSetupProcedure.STP_RS3CUR, false, false, collection_); //conn2
				    }
				    else {
					JDSetupProcedure.create (systemObject_,
								 connection2_, JDSetupProcedure.STP_RS3CUR);  //conn2
				    }
				    Statement statement = connection2_.createStatement (); //conn2
				    ResultSet rs = statement.executeQuery ("CALL " + JDSetupProcedure.STP_RS3CUR);
				    int hold = JDReflectionUtil.callMethod_I(rs, "getHoldability");
				    int concurrency = rs.getConcurrency ();
				    int type = rs.getType();
				    System.out.println(hold);
				    System.out.println(concurrency);
				    System.out.println(type);
				    while(statement.getMoreResults()){
					rs = statement.getResultSet();
					hold = JDReflectionUtil.callMethod_I(rs, "getHoldability");
					concurrency = rs.getConcurrency ();
					type = rs.getType();
					System.out.println(hold);
					System.out.println(concurrency);
					System.out.println(type);
				    }
				    rs.close ();
				    statement.close ();

				}
				catch (Exception e) {
				    failed (e, "Unexpected Exception");
				}
				}
			    }
*/



    /**
     *  Make sure that cursor row when row inserted.  Detects CLI bug for native driver fixed by
     *  PTF 9C25019.
     * 
     */	
	public void Var070() {
	    String added = " -- Added 04/29/2008 to detech CLI bug fixed by PTF 9C25019";
	    try {
		int rollbackCount  = 0;
		int commitCount = 0;

		Connection conn = testDriver_.getConnection (baseURL_+";cursor hold=false", userId_, encryptedPassword_);
		conn.setAutoCommit(false);
		conn.setTransactionIsolation(4);

		Statement stmt = conn.createStatement();
	
		String table = JDRSTest.COLLECTION+".JDRSMISC64";
		
		initTable(stmt, table, "(c1 int, c2 varchar(80))"); 
		conn.commit();

		PreparedStatement p3 = conn.prepareStatement("INSERT INTO "+table +" VALUES(?,?)");
		p3.setInt(1,1);
		p3.setString(2,"Rochester"); 
		p3.executeUpdate();
		conn.rollback();

		PreparedStatement p8 = conn.prepareStatement("SELECT * FROM "+table+" WHERE C1=?"); 
		p8.setInt(1,1);
		ResultSet rs8 = p8.executeQuery();
		int count = 0; 
		while (rs8.next()) {
		    count ++; 
		}
		rollbackCount = count; 
		conn.commit();

		p3.setInt(1,1);
		p3.setString(2,"Rochester"); 
		p3.executeUpdate();

		p8.setInt(1,1);
		rs8 = p8.executeQuery();
		count = 0; 
		while (rs8.next()) {
		    count ++; 
		}
		commitCount = count; 
		conn.commit();
		
		cleanupTable(stmt, table); 
		conn.commit(); 
		conn.close(); 

		assertCondition(rollbackCount == 0 &&
				commitCount == 1,
				"Failed rollbackCount="+rollbackCount+" sb 0 "+
				"commitCount="+commitCount+" sb 1 "+added); 

	    } catch (Exception e) {
		failed (e, "Unexpected Exception"+added);
	    } 
	}





}



