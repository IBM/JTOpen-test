///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSCursorScroll.java
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
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.*;
import java.util.*;



/**
Testcase JDRSCursorScroll.  This tests the following
methods of the JDBC ResultSet class:

<ul>
<li>relative(), absolute(), previous(), next(), isLast(), ast(), isFirst(), first(), isBeforeFirst(), isBeforeLast(), getRow()
</ul>
**/
public class JDRSCursorScroll
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSCursorScroll";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



    // Private data.
    private Statement           statement_;
    private Statement           statementMaxRows_;
    CallableStatement           callableStatementSensitive_;
    CallableStatement           callableStatementInsensitive_;

    String table49 = null; 
    boolean table49ready = false; 
    boolean doTable49cleanup = false;
    int TABLESIZE = 100000;

    boolean cursorFromCall = false;
    boolean cursorSensitive = false; 

    String suffix=""; 
/**
Constructor.
**/
    public JDRSCursorScroll (AS400 systemObject,
                         Hashtable<String,Vector<String>> namesAndVars,
                         int runMode,
                         FileOutputStream fileOutputStream,
                         
                         String password)
    {
        super (systemObject, "JDRSCursorScroll",
               namesAndVars, runMode, fileOutputStream,
               password);
    }


    public JDRSCursorScroll (AS400 systemObject,
			     String testName, 
                         Hashtable<String,Vector<String>> namesAndVars,
                         int runMode,
                         FileOutputStream fileOutputStream,
                         
                         String password)
    {
        super (systemObject, testName,
               namesAndVars, runMode, fileOutputStream,
               password);
    }


    void createScrollProcedure( String procedure, String table, String scrollType) { 
	try {
	    statement_.executeUpdate("DROP PROCEDURE "+procedure); 
	} catch (Exception e) {}

	String create = "CREATE PROCEDURE "+procedure+" () "+
	  " RESULT SETS 1   LANGUAGE SQL BEGIN   DECLARE C1 "+scrollType+" SCROLL CURSOR FOR   SELECT  * from "+table+" ORDER BY ROWNUMBER; OPEN C1; END";
	try { 
	    statement_.executeUpdate(create);
	} catch (Exception e) {
	    output_.println("Error on "+create); 
	    e.printStackTrace(); 
	} 

    }


/**
Performs setup needed before running variations.
@exception Exception If an exception occurs. */ 
  protected void setup() throws Exception {
    if (connection_ != null)
      connection_.close();

    if (isJdbc20()) {
      connection_ = testDriver_.getConnection(baseURL_
          + ";data truncation=true", userId_, encryptedPassword_, "JDRSCursorScroll");

      statement_ = connection_.createStatement(
          ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

      // This statement is used for variations that
      // need to test with the max rows set.
      statementMaxRows_ = connection_.createStatement(
          ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

      statementMaxRows_.setMaxRows(50);

      if (cursorFromCall) {
        if (cursorSensitive) {
          createScrollProcedure(JDRSTest.RSTEST_SCROLL + "SPROC",
              JDRSTest.RSTEST_SCROLL, "SENSITIVE");
          callableStatementSensitive_ = connection_.prepareCall("CALL "
              + JDRSTest.RSTEST_SCROLL + "SPROC",
              ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } else {
          createScrollProcedure(JDRSTest.RSTEST_SCROLL + "IPROC",
              JDRSTest.RSTEST_SCROLL, "INSENSITIVE");
          callableStatementInsensitive_ = connection_.prepareCall("CALL "
              + JDRSTest.RSTEST_SCROLL + "IPROC",
              ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        }
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
        {
	    if (doTable49cleanup) {
		cleanupTable(statement_,table49);
	    }
            statement_.close ();
            statementMaxRows_.close ();
            connection_.close ();
        }
    }


    ResultSet  getScrollableResultSet() throws SQLException {
	ResultSet rs;
	if (!cursorFromCall) { 
	    rs = statement_.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_SCROLL );
	} else {
	    if (cursorSensitive) {
		callableStatementSensitive_.execute();
		rs = callableStatementSensitive_.getResultSet(); 
	    } else {
		callableStatementInsensitive_.execute();
		rs = callableStatementInsensitive_.getResultSet(); 
	    } 
	} 
	return rs; 
    }

/**
absolute()-Should return true if it is in the table and false if it is not in the table
**/
    public void Var001 ()
    {
        if (checkJdbc20 ()) 
        {
            try 
            {
                ResultSet rs;
                rs = getScrollableResultSet();
                
                boolean success1 = rs.absolute(1);
                boolean success2 = rs.absolute(400);
                rs.close ();
                assertCondition ((success1 == true) && (success2 == false));
            }
            catch (Exception e) 
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
absolute() 
beforeFirst()
- After positioning the cursor before the first row and then positioning it on a row in the
table should return true.
**/
    public void Var002 ()
    {
        if (checkJdbc20 ()) 
        {
            try 
            {
                ResultSet rs = getScrollableResultSet();
                
                boolean success1=rs.absolute(1);
                rs.beforeFirst();
                boolean success2=rs.absolute(1);
                rs.close ();
                assertCondition((success1==true)&&(success2==true));
            }
            catch(Exception e) 
            {
          		failed(e, "Unexpected Exception");
            }
        }
    }


/**
last()
isLast()
- After positioning the cursor on the last row and then checking if it is on the last row should
return true
**/
	public void Var003 ()
	{
		if(checkJdbc20 () )
		{
			try
			{
				ResultSet rs = getScrollableResultSet();

				boolean success1=rs.last();
				boolean success2=rs.isLast();
				rs.close();
				assertCondition((success1==true)&&(success2==true));
			}
			catch(Exception e)
			{
				failed(e, "Unexpected Exception");
			}
		}
	}



/**
last()
isLast()
afterLast()
isAfterLast()
-After positioning the cursor on the last row and then checking if it is on the last row should return true.
Then after positioning the cursor after the last row and checking if it is after the last row should return true.
**/

	public void Var004()
	{
		if(checkJdbc20 () )
		{
			try
			{
				ResultSet rs = getScrollableResultSet();

				boolean success1=rs.last();
				boolean success2=rs.isLast();
				rs.afterLast();
				boolean success3=rs.isAfterLast();
				rs.close();
				assertCondition((success1==true)&&(success2==true)&&(success3==true));
			}
			catch(Exception e)
			{
				failed(e, "Unexpected Exception");
			}
		}
	}

/**
absolute()
isLast()
-After postioning the cursor on the last row of the table using absolute and then calling  isLast should return true
**/
	public void Var005()
	{
		if(checkJdbc20 () )
		{
			try
			{
				ResultSet rs = getScrollableResultSet();

				boolean success1=rs.absolute(97);
				boolean success2=rs.isLast();
				rs.close();
				assertCondition((success1==true)&&(success2==true), 
                                    "absolute returned "+success1+" sb  true "+
                                    "isLast   returned "+success2+" sb  true" );
			}
			catch(Exception e)
			{
				failed(e, "Unexpected Exception");
			}
		}
	}

/**
absolute()
relative()
-After positioning the cursor on the first row of the table by using absolute(1) and then calling relative(-2) should 
return false since you are no longer on a valid row in the table.
**/
	public void Var006()
	{
		if(checkJdbc20 () )
		{
			try
			{
				ResultSet rs = getScrollableResultSet();

				boolean success1=rs.absolute(1);
				boolean success2=rs.relative(-2);
				rs.close();
				assertCondition((success1==true)&&(success2==false));
			}
			catch(Exception e)
			{
				failed(e, "Unexpected Exception");
			}
		}
	}

/**
absolute()
relative()
isBeforeFirst()
-After positioning the cursor on the first row in the table and then moving two rows before it with relative, isBeforeFirst should return true
**/
	public void Var007()
	{
          
		if(checkJdbc20 () )
		{
			try
			{
				ResultSet rs = getScrollableResultSet();

				boolean success1=rs.absolute(1);
				boolean success2=rs.relative(-2);
				boolean success3=rs.isBeforeFirst();
				rs.close();
				assertCondition((success3==true), "success3="+success3+
                                    " success2="+success2+
                                    " success1="+success1);
			}
			catch(Exception e)
			{
				failed(e, "Unexpected Exception");
			}
		}
	}

/**
absolute() 
beforeFirst()
- After positioning the cursor somewhere in the table other than the first row, then 
positioning the cursor before the first row and then positioning it on the first row in the
table should return true.
**/
    public void Var008 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                
		boolean success1=rs.absolute(7);
		rs.beforeFirst();
		boolean success2=rs.absolute(1);
		rs.close ();
		assertCondition((success1==true)&&(success2==true));
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }

/**
absolute() 
beforeFirst()
- After positioning the cursor on the last row of the table using absolute, then 
positioning the cursor before the first row and then positioning it on the first row in the
table should return true.
**/
    public void Var009 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                
		boolean success1=rs.absolute(12);
		rs.beforeFirst();
		boolean success2=rs.absolute(1);
		rs.close ();
		assertCondition((success1==true)&&(success2==true));
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }

/**
absolute() 
beforeFirst()
- After positioning the cursor somewhere outside of the table, and then
position the cursor somewhere in the table other than the first row, then 
positioning the cursor before the first row and then positioning it on the first row in the
table should return true.
**/
    public void Var010 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                boolean success=rs.absolute(500);
		boolean success1=rs.absolute(7);
		rs.beforeFirst();
		boolean success2=rs.absolute(1);
		rs.close ();
		assertCondition((success==false)&&(success1==true)&&(success2==true));
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }

/**
absolute() 
beforeFirst()
- After positioning the cursor somewhere outside of the table, then 
positioning the cursor before the first row and then positioning it on the first row in the
table should return true.
**/
    public void Var011 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                
		boolean success1=rs.absolute(102);
		rs.beforeFirst();
		boolean success2=rs.absolute(1);
		rs.close ();
		assertCondition((success1==false)&&(success2==true));
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }
/**
absolute() 
isFirst()
- After positioning the cursor on the first row of the table using absolute and then calling
isLast should return true.
**/
    public void Var012 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                
		boolean success1=rs.absolute(1);
		boolean success2=rs.isFirst();
		rs.close ();
		assertCondition((success1==true)&&(success2==true));
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }

               
        }
    }

/**
absolute() 
previous()
- After positioning the cursor on the first row of the table using absolute, then by calling previous
false should be returned.
**/
    public void Var013 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                
		boolean success1=rs.absolute(1);
		boolean success2=rs.previous();
		rs.close ();
		assertCondition((success1==true)&&(success2==false));
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }

/**
first() 
previous()
- After positioning the cursor on the first row of the table, then by calling previous
false should be returned.
**/
    public void Var014 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                
		boolean success1=rs.first();
		boolean success2=rs.previous();
		rs.close ();
		assertCondition((success1==true)&&(success2==false));
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }

/**
absolute() 
next()
- After positioning the cursor on the last row of the table using absolute, then by calling next
false should be returned.
**/
    public void Var015 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                
		boolean success1=rs.absolute(97);
		boolean success2=rs.next();
		rs.close ();
		   assertCondition((success1==true)&&(success2==false), 
                      "absolute(97) to last row of "+JDRSTest.RSTEST_SCROLL+" returned "+success1+ "sb true "+
                      "rs.next() then returned "+success2+" sb false ");
                
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }

/**
last() 
next()
- After positioning the cursor on the last row of the table using last, then by calling next
false should be returned.
**/
    public void Var016 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                
		boolean success1=rs.last();
		boolean success2=rs.next();
		rs.close ();
		assertCondition((success1==true)&&(success2==false));
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }

/**
first() 
getRow()
- After positioning the cursor on the first row of the table using first, the result from getRow should be 1.
**/
    public void Var017 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                
		boolean success1=rs.first();
		int row=rs.getRow();
		rs.close ();
		assertCondition((success1==true)&&(row==1));
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }

/**
last() 
getString()
- After positioning the cursor on the last row of the table using last, the result from getString should be Abraham.
**/
    public void Var018 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                
		boolean success1=rs.last();
		int i = rs.getInt(1);
		rs.close ();
		assertCondition((success1==true)&&(i == 97));
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }

/**
absolute() 
afterLast()
- After positioning the cursor somewhere outside of the table using absolute and then calling afterLast followed
by absolute(12) to position it on the last row of the table should return true.
**/
    public void Var019 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                
		boolean success1=rs.absolute(1000);
		rs.afterLast();
		boolean success3=rs.absolute(12);

		rs.close ();
		assertCondition((success1==false)&&(success3==true));
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }

/**
absolute() 
afterLast()
- After positioning the cursor somewhere outside of the table using absolute and then calling afterLast followed
by absolute(1) to position it on the first row of the table should return true.
**/
    public void Var020 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                
		boolean success1=rs.absolute(333);
		rs.afterLast();
		boolean success3=rs.absolute(1);
		rs.close ();
		assertCondition((success1==false)&&(success3==true));
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }

/**
absolute() 
afterLast()
- After positioning the cursor before the table using absolute and then calling afterLast followed
by absolute(12) to position it on the last row of the table should return true.
**/
    public void Var021 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                
		boolean success1=rs.absolute(-500);
		rs.afterLast();
		boolean success3=rs.absolute(12);
		rs.close ();
		assertCondition((success1==false)&&(success3==true));
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }

/**
absolute()
isBeforeFirst()
-After positioning the cursor before the table using absolute and then calling isBeforeFirst should return true.
**/
    public void Var022 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                
		boolean success1=rs.absolute(-100);
		boolean success2=rs.isBeforeFirst();
		rs.close ();
		assertCondition((success1==false)&&(success2==true));
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }

/**
afterLast()
previous()
- After positioning the cursur after the table, calling previous should return true.  
**/
    public void Var023 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                
		rs.afterLast();
		boolean success1=rs.previous();
		rs.close ();
		assertCondition((success1==true));
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }

/**
afterLast()
previous()
isLast()
- After positioning the cursur after the table, calling previous should return true. The cursor should be on the
last row of the table, so isLast should return true. 
**/
    public void Var024 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                
		rs.afterLast();
		boolean success1=rs.previous();
		boolean success2=rs.isLast();
		rs.close ();
		assertCondition((success1==true)&&(success2==true));
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }

/**
last()
relative()
-After positioning the cursor on the last row of the table and then moving one low below it should return false
**/
    public void Var025 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                
		boolean success1=rs.last();
		boolean success2=rs.relative(2);
		rs.close ();
		assertCondition((success1==true)&&(success2==false));
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }


/**
last()
relative()
isAfterLast()
-After positioning the cursor on the last row of the table and then moving one low below it should return false.
The cursor should be after the last entry in the table so isAfterLast should return true.
**/
    public void Var026 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                
		boolean success1=rs.last();
		boolean success2=rs.relative(2);
		boolean success3=rs.isAfterLast();
		rs.close ();
		assertCondition((success1==true)&&(success2==false)&&(success3==true));
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }

/**
afterLast()
relative()
isAfterLast()
-After positioning the cursor after the last row of the table and then calling relative should return
false and the cursor should stay positioned after the last row of the table.
**/
    public void Var027 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                
		rs.afterLast();
		boolean success1=rs.relative(2);
		boolean success2=rs.isAfterLast();
		rs.close ();
		assertCondition((success1==false)&&(success2==true));
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }

/**
beforeFirst()
relative()
isBeforeFirst
-After positioning the cursor before the first row of the table and 
 then calling relative should return false 
 and the cursor should remain before the first row.
 
 For jcc this testcase fails with the following exception.. 
 JDRSCursorScroll  28   FAILED Unexpected Exception
com.ibm.db2.jcc.am.SqlException: [ibm][db2][jcc][1043][10867] Cursor is not on a valid row.
        at com.ibm.db2.jcc.am.ResultSet.relativeX(ResultSet.java:1727)
        at com.ibm.db2.jcc.am.ResultSet.relative(ResultSet.java:1701)
        at test.JDRSCursorScroll.Var028(JDRSCursorScroll.java:835)
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:64)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
        at java.lang.reflect.Method.invoke(Method.java:615)
        at test.Testcase.invokeVariation(Testcase.java:884)
        at test.Testcase.run(Testcase.java:1240)
        at test.JDTestcase.run(JDTestcase.java:871)
        at test.TestDriver.run(TestDriver.java:835)
        at test.TestDriver.start(TestDriver.java:993)
        at test.TestDriver.runApplication(TestDriver.java:940)
        at test.JDRSTest.main(JDRSTest.java:152)
        
 I disagree with this behavior since the javadoc says the relative(1) should work like next()
 and next should work when 'beforeFirst'.   
 Ditto: toolbox      
**/
    public void Var028 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                
		rs.beforeFirst();
		boolean success1=rs.relative(2);
		boolean success2=rs.isBeforeFirst();
		rs.close ();
                if ((getDriver() == JDTestDriver.DRIVER_NATIVE && 
                    true) ||
                    isToolboxDriver())
                {

		    // Also fixed in V5R4 
                    // In v5r5 native permits relative to be used when 
                    // cursor position is before first or after last
                assertCondition((success1==true)&&(success2==false),
                    "Querying from "+JDRSTest.RSTEST_SCROLL+" "+
                    "rs.relative(2) returned "+success1+" sb true "+
                    "rs.isBeforeFirst() returned "+success2+" sb false Fixed in V5R4 with SI34211");
                  
                } else { 

		assertCondition((success1==false)&&(success2==true),
                    "Querying from "+JDRSTest.RSTEST_SCROLL+" "+
                    "rs.relative(2) returned "+success1+" sb false "+
                    "rs.isBeforeFirst() returned "+success2+" sb true ");
                }
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }

/**
first()
relative()
isBeforeFirst()
-After positioning the cursor on the first row of the table and then calling relative(-1) should return false.
A call then to isBeforeFirst should return true.
**/
    public void Var029 ()
    {
        if (checkJdbc20 ()) 
        {
            try 
            {
                ResultSet rs = getScrollableResultSet();
                
		boolean success1=rs.first();
		boolean success2=rs.relative(-1);
		boolean success3=rs.isBeforeFirst();  
		rs.close ();
		assertCondition((success1==true)&&(success2==false)&&(success3==true));
		
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }

/**
first()
relative()
-After positioning the cursor on the first row of the table and then calling relative(-1) should return false.
**/
    public void Var030 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                
		boolean success1=rs.first();
		boolean success2=rs.relative(-1);
		rs.close ();
		assertCondition((success1==true)&&(success2==false));
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }

/**
absolute() 
beforeFirst()
- After positioning the cursor somewhere outside of the table, and then
position the cursor on the first row using absolute, then 
positioning the cursor before the first row and then positioning it on the first row in the
table should return true.
**/
    public void Var031 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                boolean success=rs.absolute(98);
		boolean success1=rs.absolute(1);
		rs.beforeFirst();
		boolean success2=rs.absolute(1);
		rs.close ();
		assertCondition((success==false)&&(success1==true)&&(success2==true));
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }

/**
absolute()
beforeFirst()
-After positioning the cursor before the table using absolute, then calling beforeFirst, by calling absolute(1) true should
be returned.
**/
    public void Var032 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                boolean success=rs.absolute(-250);
		rs.beforeFirst();
		boolean success1=rs.absolute(1);
		rs.close ();
		assertCondition((success==false)&&(success1==true));
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }

/**
absolute()
previous()
beforeFirst()
-After positioning the cursor on the first row of the table by using absolute and then calling previous folled by beforeFirst,
a call to absolute(1) should return true.
**/
    public void Var033 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                boolean success=rs.absolute(1);
		boolean success1=rs.previous();
		rs.beforeFirst();
		boolean success2=rs.absolute(1);
		rs.close ();
		assertCondition((success==true)&&(success1==false)&&(success2==true));
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }

/**
absolute()
isLast()
-After positioning the cursor on the last row of the table by calling absolute(-1), isLast should return true.
**/
    public void Var034 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                boolean success=rs.absolute(-1);
		boolean success1=rs.isLast();
		rs.close ();
		assertCondition((success==true)&&(success1==true));
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }

/**
absolute()
previous()
relative()
isBeforeFirst()
-After positioning the cursor on the first row of the table by using absolute and then calling previous,
a call to relative(1) should return false and the cursor should remain positioned before the table.
**/
    public void Var035 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                boolean success=rs.absolute(1);
		boolean success1=rs.previous();
		boolean success2=rs.relative(1);
		boolean success3=rs.isBeforeFirst();
		rs.close ();
		if ((getDriver() == JDTestDriver.DRIVER_NATIVE && 
		    true) ||
		    isToolboxDriver()){
		  // In v5r5 native permits relative to be used when 
		  // cursor position is before first or after last
		  assertCondition((success==true)&&
		      (success1==false)&&
		      (success2==true)&&
		      (success3==false), 
		      "Querying "+JDRSTest.RSTEST_SCROLL+" "+
		      "absolute(1)="+success+" sb true "+
		      "previous() ="+success1+" sb false "+
		      "relative(1)="+success2+" sb true "+
		      "isBeforeFirst="+success3+" sb false Fixed in V5R4 using SI34211");
		} else {
		assertCondition((success==true)&&
                                (success1==false)&&
                                (success2==false)&&
                                (success3==true), 
                    "Querying "+JDRSTest.RSTEST_SCROLL+" "+
                    "absolute(1)="+success+" sb true "+
                    "previous() ="+success1+" sb false "+
                    "relative(1)="+success2+" sb false "+
                    "isBeforeFirst="+success3+" sb true ");
                }
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }

/**
first()
previous()
relative()
isBeforeFirst()
-After positioning the cursor on the first row of the table by using first and then calling previous,
a call to relative(1) should return false and leave the cursor positioned before the table.
**/
    public void Var036 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                
                boolean success=rs.first();
		boolean success1=rs.previous();
		boolean success2=rs.relative(1);
		boolean success3=rs.isBeforeFirst();
		rs.close ();
                if ((getDriver() == JDTestDriver.DRIVER_NATIVE && 
                    true) ||
                    isToolboxDriver()){
                  // In v5r5 native permits relative to be used when 
                  // cursor position is before first or after last
                assertCondition((success==true)&&(success1==false)&&
                    ( success2==true)&&(success3==false),
                    "Querying "+JDRSTest.RSTEST_SCROLL+" "+
                    "first="+success+" sb true "+
                    "previous() ="+success1+" sb false "+
                    "relative(1)="+success2+" sb true "+
                    "isBeforeFirst="+success3+" sb false Fixed in V5R4 using SI34211");
                } else { 
		assertCondition((success==true)&&(success1==false)&&
                    ( success2==false)&&(success3==true),
                    "Querying "+JDRSTest.RSTEST_SCROLL+" "+
                    "first="+success+" sb true "+
                    "previous() ="+success1+" sb false "+
                    "relative(1)="+success2+" sb false "+
                    "isBeforeFirst="+success3+" sb true ");
                }
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }

/**
absolute()
next()
relative()
isAfterLast()
-After positioning the cursor on the last row of the table by using absolute and then calling next,
a call to relative(1) should return false and the cursor should stay positioned after the last row of the table.
**/
    public void Var037 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                boolean success=rs.absolute(97);
		boolean success1=rs.next();
		boolean success2=rs.relative(1);
		boolean success3=rs.isAfterLast();
		rs.close ();
		assertCondition((success==true)&&(success1==false)&&
                    (success2==false)&&(success3==true), 
                    "absolute(97)="+success+" sb true "+
                    "next() ="+success1+" sb false "+
                    "relative(1)="+success2+" sb false "+
                    "isAfterlast="+success3+" sb true ");
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }

/**
last()
next()
relative()
isAfterLast()
-After positioning the cursor on the last row of the table by using last and then calling next,
a call to relative(1) should return false and leave the cursor positioned after the table.
**/
    public void Var038 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                boolean success=rs.last();
		boolean success1=rs.next();
		boolean success2=rs.relative(1);
		boolean success3=rs.isAfterLast();
		rs.close ();
		assertCondition((success==true)&&(success1==false)&&(success2==false)&&(success3==true));
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }

/**
last()
relative()
isLast()
-After positioning the cursor on the last row of the table by using last and then calling relative(0),
a call to isLast should return true.
**/
    public void Var039 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                boolean success=rs.last();
		boolean success1=rs.relative(0);
		boolean success2=rs.isLast();
		rs.close ();
		assertCondition((success==true)&&(success1==true)&&(success2==true));
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }

/**
first()
relative()
isFirst()
-After positioning the cursor on the first row of the table by using first and then calling relative(0),
a call to isFirst should return true.
**/
    public void Var040 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                boolean success=rs.first();
		boolean success1=rs.relative(0);
		boolean success2=rs.isFirst();
		rs.close ();
		assertCondition((success==true)&&(success1==true)&&(success2==true));
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }

/**
beforeFirst()
next()
isFirst()
-After placing the cursor before the table, a call to next should place it on the first row of the table.
**/
    public void Var041 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                rs.beforeFirst();
		boolean success1=rs.next();
		boolean success2=rs.isFirst();
		rs.close ();
		assertCondition((success1==true)&&(success2==true));
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }

/**
first()
previous()
next()
isFirst()
-After placing the cursor on the first row in the table, calling previous followed by next, isFirst should return true.
**/
    public void Var042 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                boolean success1=rs.first();
		boolean success2=rs.previous();
		boolean success3=rs.next();
		boolean success4=rs.isFirst();
		rs.close ();
		assertCondition((success1==true)&&(success2==false)&&(success3==true)&&(success4==true));
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }

/**
absolute() 
beforeFirst()
next()
- After positioning the cursor somewhere outside of the table, and then
position the cursor somewhere in the table other than the first row, then 
positioning the cursor before the first row and calling next and then positioning it on the first row in the
table should return true.
**/
    public void Var043 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                boolean success=rs.absolute(119);
		boolean success1=rs.absolute(7);
		rs.beforeFirst();
		boolean success3=rs.next();
		boolean success2=rs.absolute(1);
		rs.close ();
		assertCondition((success==false)&&(success1==true)&&(success2==true)&&(success3==true));
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }

/**
absolute() 
beforeFirst()
next()
- After positioning the cursor somewhere outside of the table, and then 
positioning the cursor before the first row and calling next and then positioning it on the first row in the
table should return true.
**/
    public void Var044 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                boolean success=rs.absolute(205);
		rs.beforeFirst();
		boolean success3=rs.next();
		boolean success2=rs.absolute(1);
		rs.close ();
		assertCondition((success==false)&&(success3==true)&&(success2==true));
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }

/**
absolute() 
beforeFirst()
next()
- After positioning the cursor somewhere outside of the table, and then 
positioning the cursor before the first row and calling next and then positioning it on the first row in the
table should return true.
**/
    public void Var045 ()
    {
        if (checkJdbc20 ()) 
        {
            try 
            {
                ResultSet rs = getScrollableResultSet();
                boolean success=rs.absolute(-999);
                rs.beforeFirst();
                boolean success3=rs.next();
                boolean success2=rs.absolute(1);
                rs.close ();
                assertCondition((success==false)&&(success3==true)&&(success2==true));
	          }
             catch(Exception e) 
             {
                failed(e, "Unexpected Exception");
             }
        }
    }

/**
beforeFirst()
isBeforeFirst()
afterLast()
isAfterLast()
-After positioning the cursor before the table, isBeforeFirst should return true.  Then positioning the cursor
after the table, isAfterLast should return true.
**/
    public void Var046 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = getScrollableResultSet();
                rs.beforeFirst();
		boolean success1=rs.isBeforeFirst();
		rs.afterLast();
		boolean success2=rs.isAfterLast();
		rs.close ();
		assertCondition((success1==true)&&(success2==true));
	    }
	    catch(Exception e) {
		failed(e, "Unexpected Exception");
	    }
               
        }
    }

/**
absolute()
getRow()
-After positioning the cursor to row seven, getRow should return 7.
**/
    public void Var047()
    {
        if(checkJdbc20())
        {
            try{
                ResultSet rs= getScrollableResultSet(); 
                boolean success1=rs.absolute(7);
                int row=rs.getRow();
                rs.close();
                assertCondition((success1==true)&&(row==7));
            }
            catch(Exception e){
                failed(e, "Unexpected Exception");
            }
        }
    }

/**
first()
getRow()
-After positioning the cursor to the first row, getRow should return 1.
**/
    public void Var048()
    {
        if(checkJdbc20())
        {
            try{
                ResultSet rs= getScrollableResultSet(); 
                boolean success1=rs.first();
                int row=rs.getRow();
                rs.close();
                assertCondition((success1==true)&&(row==1));
            }
            catch(Exception e){
                failed(e, "Unexpected Exception");
            }
        }
    }

/**
 * Test all scroll methods and validate that they end up on the right column
 * Run the test for 60 seconds
 */

  public boolean checkRow49(ResultSet rs, int expectedRow, int lastRow,
      StringBuffer sb) throws SQLException {
    if (expectedRow < 1) {
       if (rs.isBeforeFirst()) {
         return true; 
       } else {
         sb.append("Expected isBeforeFirst to return true - expectedRow = "+expectedRow);
         return false; 
       }
    } else if (expectedRow > lastRow) {
      if (rs.isAfterLast()) {
        return true; 
      } else {
        sb.append("Expected isAfterLast to return true - expectedRow = "+expectedRow);
        return false; 
      }

    } else {
	int row = rs.getInt(1);
	if (row == expectedRow) {
	    return true;
	} else {
	    sb.append("On row " + row + " expected " + expectedRow);
	    return false;
	}
    }

  }

  void setupTable49 () throws Exception {
      if (!table49ready) {
	  Connection c = null; 
	  table49 = JDRSTest.COLLECTION+".JDRSCRS49"+suffix; 
	  initTable(statement_, table49, "( ROWNUMBER INT, INFO VARCHAR(91))");
	  PreparedStatement ps; 
	  if ( getDriver() == JDTestDriver.DRIVER_NATIVE) {
	      c = testDriver_.getConnection(baseURL_ +";use block insert=true", userId_, encryptedPassword_);
	      ps = c.prepareStatement("INSERT INTO "+table49+" VALUES(?,?)");
	  } else { 
	      ps = connection_.prepareStatement("INSERT INTO "+table49+" VALUES(?,?)");
	  }
	  int batchCount = 0; 
	  for (int i = 1; i <= TABLESIZE; i++) {
	      ps.setInt(1, i);
	      ps.setString(2,""+i);
	      ps.addBatch();
	      batchCount++;
	      if (batchCount == 1000) {
		  output_.println("Inserting batch at "+i); 
		  ps.executeBatch();
		  batchCount = 0; 
	      } 
	  }
	  if (batchCount > 0) ps.executeBatch();
	  if (c != null) { 
	      c.commit();
	      c.close();
	  }
	  doTable49cleanup = true; 
	  table49ready=true;
      }
  }

  int newRowPosition(int row, int tableSize) {
    if (row < 0) return 0; 
    if (row > tableSize) return tableSize + 1; 
    return row; 
  }

/**
 * Test all scroll methods and validate that they end up on the right column
 * Run the test for 10 seconds
 */

  public void Var049() {
      // Divide by 7200000 to use the same seed within two hours
      stressScroll(System.currentTimeMillis() / 7200000, 10000, "Original test");
  } 

  public void stressScroll(long seed, int RUNMILLIS, String info) {

    boolean success = true;
    Vector<String> log = new Vector<String>();
    StringBuffer sb = new StringBuffer();
    String firstMethodString = null;

    sb.append(info + "\n");
    if (checkJdbc20()) {

      int firstOp = (int) (seed % 20);
      try {
        setupTable49();

        ResultSet rs;
        CallableStatement cs = null;
        rs = statement_.executeQuery("SELECT count(*) FROM " + table49);
        rs.next();
        int rowCount = rs.getInt(1);
        rs.close();
        output_.println("Table has " + rowCount + " rows ");
        if (rowCount != TABLESIZE) {
          failed("Created table only has " + rowCount + " rows sb " + TABLESIZE);
        } else {
          sb.append("Running with seed " + seed + "\n");
          Random random = new Random(seed);

          int rowPosition = 0;
          int newRow;
          if (cursorFromCall) {
            if (cursorSensitive) {
              createScrollProcedure(table49 + "SPROC", table49, "SENSITIVE");
              cs = connection_
                  .prepareCall("CALL " + table49 + "SPROC",
                      ResultSet.TYPE_SCROLL_INSENSITIVE,
                      ResultSet.CONCUR_READ_ONLY);
            } else {
              createScrollProcedure(table49 + "IPROC", table49, "INSENSITIVE");
              cs = connection_
                  .prepareCall("CALL " + table49 + "IPROC",
                      ResultSet.TYPE_SCROLL_INSENSITIVE,
                      ResultSet.CONCUR_READ_ONLY);

            }
            cs.execute();
            rs = cs.getResultSet();

          } else {
            rs = statement_.executeQuery("SELECT * FROM " + table49+" ORDER BY ROWNUMBER");
          }

          long endTime = System.currentTimeMillis() + RUNMILLIS;
          while (success && System.currentTimeMillis() < endTime) {
            try {
              int op;
              if (firstOp == 0) {
                op = random.nextInt(12);
              } else {
                op = firstOp - 1;
                firstOp = 0;
              }

              switch (op) {
              case 0:
                log.add("first()");
                if (firstMethodString == null)
                  firstMethodString = "first()";
                rs.first();
                rowPosition = 1;
                success = checkRow49(rs, rowPosition, TABLESIZE, sb);
                break;
              case 1:
                log.add("last()");
                if (firstMethodString == null)
                  firstMethodString = "last()";
                rs.last();
                rowPosition = TABLESIZE;
                success = checkRow49(rs, rowPosition, TABLESIZE, sb);
                break;
              case 2:
                log.add("next()");
                if (firstMethodString == null)
                  firstMethodString = "next()";
                rs.next();
                rowPosition = newRowPosition(rowPosition + 1, TABLESIZE);
                success = checkRow49(rs, rowPosition, TABLESIZE, sb);
                break;
              case 3:
                log.add("previous()");
                if (firstMethodString == null)
                  firstMethodString = "previous()";
                rs.previous();
                rowPosition = newRowPosition(rowPosition - 1, TABLESIZE);
                success = checkRow49(rs, rowPosition, TABLESIZE, sb);
                break;
              case 4:
                log.add("absolute(-1)"); // same as last
                if (firstMethodString == null)
                  firstMethodString = "absolute(-1)";
                rs.absolute(-1);
                rowPosition = TABLESIZE;
                success = checkRow49(rs, rowPosition, TABLESIZE, sb);
                break;
              case 5:
                log.add("absolute(1)"); // same as first
                if (firstMethodString == null)
                  firstMethodString = "absolute(1)";
                rs.absolute(1);
                rowPosition = 1;

                success = checkRow49(rs, rowPosition, TABLESIZE, sb);
                break;
              case 6:
                newRow = -random.nextInt(TABLESIZE) - 1;
                log.add("absolute(" + newRow + ")");
                if (firstMethodString == null)
                  firstMethodString = "absolute(" + newRow + ")";
                rs.absolute(newRow);
                rowPosition = TABLESIZE + 1 + newRow;
                success = checkRow49(rs, rowPosition, TABLESIZE, sb);
                break;
              case 7:
                newRow = random.nextInt(TABLESIZE) + 1;
                log.add("absolute(" + newRow + ")");
                if (firstMethodString == null)
                  firstMethodString = "absolute(" + newRow + ")";
                rs.absolute(newRow);
                rowPosition = newRow;
                success = checkRow49(rs, rowPosition, TABLESIZE, sb);
                break;
              case 8:
                log.add("afterLast()");
                if (firstMethodString == null)
                  firstMethodString = "afterLast()";
                rs.afterLast();
                rowPosition = TABLESIZE + 1;
                success = checkRow49(rs, rowPosition, TABLESIZE, sb);
                break;
              case 9:
                log.add("beforeFirst()");
                if (firstMethodString == null)
                  firstMethodString = "beforeFirst()";
                rs.beforeFirst();
                rowPosition = 0;
                success = checkRow49(rs, rowPosition, TABLESIZE, sb);
                break;
              case 10:
                newRow = -random.nextInt(TABLESIZE / 4) - 1;
                log.add("relative(" + newRow + ")");
                if (firstMethodString == null)
                  firstMethodString = "relative(" + newRow + ")";
                rs.relative(newRow);
                rowPosition = rowPosition + newRow;
                if (rowPosition < 0)
                  rowPosition = 0;
                success = checkRow49(rs, rowPosition, TABLESIZE, sb);
                break;
              case 11:
                newRow = random.nextInt(TABLESIZE) + 1;
                log.add("relative(" + newRow + ")");
                if (firstMethodString == null)
                  firstMethodString = "relative(" + newRow + ")";
                rs.relative(newRow);
                rowPosition = rowPosition + newRow;
                if (rowPosition > TABLESIZE)
                  rowPosition = TABLESIZE + 1;
                success = checkRow49(rs, rowPosition, TABLESIZE, sb);
                break;

              }

            } catch (Exception e) {
              e.printStackTrace();
              sb.append("**** EXCEPTION OCCURED " + e);
              success = false;
            }

          }

          output_.println(log.size() + " methods executed beginning with "
              + firstMethodString);

          if (!success) {
            doTable49cleanup = false;
          }
          String message = "";
          if (!success) {
            sb.append(" -- Table is " + table49);
            sb.append(" -- Log is \n");
            Enumeration<String> enumeration = log.elements();
            while (enumeration.hasMoreElements()) {
              sb.append((String) enumeration.nextElement());
              sb.append("\n");
            }
            message = "FAILED... " + sb.toString();
          }
          rs.close();
          if (cs != null)
            cs.close();
          assertCondition(success, "test failed " + message);
        }

      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

    /*
     * test with a scrollsensitive cursor
     */

    public void Var050()
    {
       if (cursorFromCall) {
	   notApplicable("Tested by var 49");
	   return; 
       } 
	    boolean success=true; 
        Vector<String> log = new Vector<String>(); 
        StringBuffer sb = new StringBuffer(); 

	CallableStatement cs = null; 
	String firstMethodString = null; 
        if(checkJdbc20())
        {
	    int TABLESIZE = 100000; 
            int RUNMILLIS = 10000; 
            try{
		setupTable49();
		Statement stmt = connection_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                                                      ResultSet.CONCUR_READ_ONLY);
                ResultSet rs=stmt.executeQuery("SELECT count(*) FROM " + table49 );
                rs.next(); 
		int rowCount = rs.getInt(1); 
		rs.close();
		output_.println("Table has "+rowCount+" rows "); 
		if (rowCount != TABLESIZE ) {
                  failed("Created table only has "+rowCount+" rows sb "+TABLESIZE); 
                } else {
                  Random random = new Random(); 
                  
                  int rowPosition = 0;
                  int newRow;
                  if (cursorFromCall) {
                    if (cursorSensitive) {
                        createScrollProcedure(table49+"SPROC", table49, "SENSITIVE"); 
                        cs = connection_.prepareCall("CALL "+table49+"SPROC",
                                                          ResultSet.TYPE_SCROLL_SENSITIVE,
                                                          ResultSet.CONCUR_READ_ONLY
                                                          );
                    } else {
                        createScrollProcedure(table49+"IPROC", table49, "INSENSITIVE"); 
                        cs = connection_.prepareCall("CALL "+table49+"IPROC",
                                                          ResultSet.TYPE_SCROLL_SENSITIVE,
                                                          ResultSet.CONCUR_READ_ONLY
                                                          );

                    }
                    cs.execute();
                    rs = cs.getResultSet(); 

                } else { 

                  rs=stmt.executeQuery("SELECT * FROM " + table49 );
                }                  
                  
                  
                  long endTime = System.currentTimeMillis() + RUNMILLIS; 
                  while (success && System.currentTimeMillis() < endTime) {
		      try { 
			  int op = random.nextInt(12); 
			  switch (op) {
			      case 0:
				  log.add("first()");
				  if (firstMethodString == null) firstMethodString="first()"; 
				  rs.first(); 
				  rowPosition=1;
				  success = checkRow49(rs, rowPosition, TABLESIZE, sb); 
				  break; 
			      case 1:
				  log.add("last()"); 
				  if (firstMethodString == null) firstMethodString="last()"; 
				  rs.last(); 
				  rowPosition=TABLESIZE;
				  success = checkRow49(rs, rowPosition, TABLESIZE, sb); 
				  break; 
			      case 2:
				  log.add("next()"); 
				  if (firstMethodString == null) firstMethodString="next()"; 
				  rs.next(); 
				  rowPosition = newRowPosition(rowPosition+1, TABLESIZE); 
				  success = checkRow49(rs, rowPosition, TABLESIZE, sb); 
				  break; 
			      case 3:
				  log.add("previous()"); 
				  if (firstMethodString == null) firstMethodString="previous()"; 
				  rs.previous(); 
				  rowPosition = newRowPosition(rowPosition-1, TABLESIZE); 
				  success = checkRow49(rs, rowPosition, TABLESIZE, sb); 
				  break; 
			      case 4:
				  log.add("absolute(-1)"); // same as last
				  if (firstMethodString == null) firstMethodString="absolute(-1)"; 
				  rs.absolute(-1); 
				  rowPosition=TABLESIZE;
				  success = checkRow49(rs, rowPosition, TABLESIZE, sb); 
				  break; 
			      case 5:
				  log.add("absolute(1)"); // same as first
				  if (firstMethodString == null) firstMethodString="absolute(1)"; 
				  rs.absolute(1); 
				  rowPosition=1;

				  success = checkRow49(rs, rowPosition, TABLESIZE, sb); 
				  break; 
			      case 6:
				  newRow = - random.nextInt(TABLESIZE) - 1;
				  log.add("absolute("+newRow+")"); 
				  if (firstMethodString == null) firstMethodString="absolute("+newRow+")"; 
				  rs.absolute(newRow); 
				  rowPosition = TABLESIZE+1+newRow;
				  success = checkRow49(rs, rowPosition, TABLESIZE, sb);
				  break;
			      case 7:
				  newRow = random.nextInt(TABLESIZE) + 1;
				  log.add("absolute("+newRow+")"); 
				  if (firstMethodString == null) firstMethodString="absolute("+newRow+")"; 
				  rs.absolute(newRow); 
				  rowPosition = newRow;
				  success = checkRow49(rs, rowPosition, TABLESIZE, sb);
				  break;
			      case 8:
				  log.add("afterLast()");
				  if (firstMethodString == null) firstMethodString="afterLast()"; 
				  rs.afterLast();
				  rowPosition = TABLESIZE+1;
				  success = checkRow49(rs, rowPosition, TABLESIZE, sb);
				  break;
			      case 9:
				  log.add("beforeFirst()");
				  if (firstMethodString == null) firstMethodString="beforeFirst()"; 
				  rs.beforeFirst();
				  rowPosition = 0 ;
				  success = checkRow49(rs, rowPosition, TABLESIZE, sb);
				  break;
			      case 10:
				  newRow = - random.nextInt(TABLESIZE/4) - 1;
				  log.add("relative("+newRow+")"); 
				  if (firstMethodString == null) firstMethodString="relative("+newRow+")"; 
				  rs.relative(newRow); 
				  rowPosition = rowPosition + newRow;
				  if (rowPosition < 0) rowPosition = 0; 
				  success = checkRow49(rs, rowPosition, TABLESIZE, sb);
				  break;
			      case 11:
				  newRow = random.nextInt(TABLESIZE) + 1;
				  log.add("relative("+newRow+")"); 
				  if (firstMethodString == null) firstMethodString="relative("+newRow+")"; 
				  rs.relative(newRow); 
				  rowPosition = rowPosition + newRow;
				  if (rowPosition > TABLESIZE) rowPosition=TABLESIZE+1;
				  success = checkRow49(rs, rowPosition, TABLESIZE, sb);
				  break;

			  }

		      } catch (Exception e) {
			  e.printStackTrace();
			  sb.append("**** EXCEPTION OCCURED "+e); 
			  success=false; 
		      } 
                  
                  } 
                  
                  output_.println(log.size()+" methods executed first method="+firstMethodString); 
                  stmt.close(); 
		  if (!success) {
		      doTable49cleanup = false; 
		  }
                  String message = ""; 
                  if (!success) {
		    sb.append(" -- Table is "+table49); 
                    sb.append(" -- Log is \n"); 
                    Enumeration<String> enumeration = log.elements(); 
                    while (enumeration.hasMoreElements()) {
                      sb.append((String) enumeration.nextElement()); 
                      sb.append("\n"); 
                    }
                    message= "FAILED... "+sb.toString();
                  }
		  rs.close();
		  if (cs != null) cs.close(); 

                  assertCondition(success, "test failed "+message);
                }

            }
            catch(Exception e){
                failed(e, "Unexpected Exception");
            }
        }
    }



  public void Var051() {
      // Run test to start with first() 
      stressScroll(1, 500 , "Test added 8/4/2011");
  } 

  public void Var052() {
      // Run test to start with last()
      stressScroll(2, 500 , "Test added 8/4/2011");
  } 

  public void Var053() {
      // Run test to start with next()
      stressScroll(3, 500 , "Test added 8/4/2011");
  } 

  public void Var054() {
      // Run test to start with previous()
      stressScroll(4, 500 , "Test added 8/4/2011");
  } 

  public void Var055() {
      // Run test to start with absolute(-1)
      stressScroll(5, 500, "Test added 8/4/2011" );
  } 

  public void Var056() {
      // Run test to start with absolute(1)
      stressScroll(6, 500, "Test added 8/4/2011" );
  } 

  public void Var057() {
      // Run test to start with absolute(-xxxx)
      stressScroll(7, 500, "Test added 8/4/2011" );
  } 

  public void Var058() {
      // Run test to start with absolute(xxxx)
      stressScroll(8, 500, "Test added 8/4/2011" );
  } 

  public void Var059() {
      // Run test to start with afterLast()
      stressScroll(9, 500, "Test added 8/4/2011" );
  } 

  public void Var060() {
      // Run test to start with beforeFirst()
      stressScroll(10, 500, "Test added 8/4/2011" );
  } 

  public void Var061() {
      // Run test to start with relative(-xxxx)
      stressScroll(11, 500, "Test added 8/4/2011" );
  } 


  public void Var062() {
      // Run test to start with relative(+xxxx) 
      stressScroll(12, 500, "Test added 8/4/2011" );
  } 

  // Run test with see 200557 which failed 10/4/2015
  public void Var063() {
      // Run test to start with relative(+xxxx) 
      stressScroll(200557, 10000, "Test added 10/4/2015" );
  } 

  // Run with a more random seed
  public void Var064() {
      // Run test to start with relative(+xxxx) 
      stressScroll(System.currentTimeMillis(), 10000, "Test added 10/4/2015" );
  } 


}



