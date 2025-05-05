///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSMisc.java
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
 // File Name:    JDPSMisc.java
 //
 // Classes:      JDPSMisc
 //
 ////////////////////////////////////////////////////////////////////////
 //
 //
 //
 //
 // 
 // 
 //
 ////////////////////////////////////////////////////////////////////////

package test.JD.PS;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCPreparedStatement;

import test.JDPSTest;
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Hashtable; import java.util.Vector;



/**
Testcase JDPSMisc.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>getMetaData()
<li>counting of parameters
</ul>
**/
public class JDPSMisc
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDPSMisc";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDPSTest.main(newArgs); 
   }



    // Private data.
    private String              TABLE           = JDPSTest.COLLECTION + ".JDPSMISC";
    private String              TABLE2           = JDPSTest.COLLECTION + ".JDPSMISC2";
    private Connection          connection_;
    private Statement           statement_;



/**
Constructor.
**/
    public JDPSMisc (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDPSMisc",
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
            
            ;
        connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
        connection_.setAutoCommit(true); // for xa
        statement_ = connection_.createStatement ();
        TABLE           = JDPSTest.COLLECTION + ".JDPSMISC";
	TABLE2           = JDPSTest.COLLECTION + ".JDPSMISC2";

        statement_.executeUpdate ("CREATE TABLE " + TABLE + " (" 
            + "COL1 INTEGER, COL2 VARCHAR(50))");
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
        statement_.executeUpdate ("DROP TABLE " + TABLE);

        statement_.close ();
        connection_.commit(); // for xa
        connection_.close ();
    }


/**
getMetaData() - Should throw an exception on a closed prepared statement.
**/
    public void Var001 ()
    {
        if (checkJdbc20 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement ("SELECT * FROM " + TABLE);
                ps.close ();
                ResultSetMetaData rsmd = ps.getMetaData ();
                failed ("Didn't throw SQLException "+rsmd);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getMetaData() - Should work on an open prepared statement.
**/
    public void Var002 ()
    {
        if (checkJdbc20 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement ("SELECT * FROM " + TABLE);
                ResultSetMetaData rsmd = ps.getMetaData ();
                ps.close ();
                assertCondition (rsmd != null);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
counting of parameters - Attempt to perform a regular insert with 
no real or quoted parameters.
**/
    public void Var003()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + TABLE);
            PreparedStatement ps = connection_.prepareStatement ("INSERT INTO "
                + TABLE + " (COL1, COL2) VALUES (12, 'A string with no question marks.')");
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT COL1,COL2 FROM " + TABLE);
            rs.next ();
            int col1 = rs.getInt ("COL1");
            String col2 = rs.getString ("COL2");
            rs.close ();

            assertCondition ((col1 == 12) && (col2.equals ("A string with no question marks.")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

        

/**
counting of parameters - Attempt to insert with one real parameter and 
no quoted parameters or comment parameters.
**/
    public void Var004()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + TABLE);
            PreparedStatement ps = connection_.prepareStatement ("INSERT INTO "
                + TABLE + " (COL1, COL2) VALUES (?, 'Another string with no question marks.')");
            ps.setInt (1, -33789);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT COL1,COL2 FROM " + TABLE);
            rs.next ();
            int col1 = rs.getInt (1);
            String col2 = rs.getString (2);
            rs.close ();
            
            assertCondition ((col1 == -33789) && (col2.equals ("Another string with no question marks.")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
counting of parameters - Attempt to insert with one real parameter and 
one quoted parameter.
**/
    public void Var005()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + TABLE);
            PreparedStatement ps = connection_.prepareStatement ("INSERT INTO "
                + TABLE + " (COL1, COL2) VALUES (?, 'A string with a question mark?.')");
            ps.setInt (1, -4789);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT COL1,COL2 FROM " + TABLE);
            rs.next ();
            int col1 = rs.getInt ("COL1");
            String col2 = rs.getString (2);
            rs.close ();
            
            assertCondition ((col1 == -4789) && (col2.equals ("A string with a question mark?.")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
counting of parameters - Attempt to insert with one real parameter and 
one comment parameter.
**/
    public void Var006()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + TABLE);
            PreparedStatement ps = connection_.prepareStatement ("INSERT INTO "
                + TABLE + " (COL1, COL2) VALUES (?, 'A normal string.') -- This is a comment with a question mark?");
            ps.setInt (1, -4789);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT COL1,COL2 FROM " + TABLE);
            rs.next ();
            int col1 = rs.getInt ("COL1");
            String col2 = rs.getString (2);
            rs.close ();
            
            assertCondition ((col1 == -4789) && (col2.equals ("A normal string.")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
counting of parameters - Attempt to insert with one real parameter,
one quoted parameter, and one comment parameter.
**/
    public void Var007()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + TABLE);
            PreparedStatement ps = connection_.prepareStatement ("INSERT INTO "
                + TABLE + " (COL1, COL2) VALUES (?, 'A normal (?) string') -- This is a comment (?) with a question mark.");
            ps.setInt (1, -4789);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT COL1,COL2 FROM " + TABLE);
            rs.next ();
            int col1 = rs.getInt ("COL1");
            String col2 = rs.getString (2);
            rs.close ();
            
            assertCondition ((col1 == -4789) && (col2.equals ("A normal (?) string")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
counting of parameters - Attempt to set two parameters, one real parameter and 
one quoted parameter.
**/
    public void Var008()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + TABLE);
            PreparedStatement ps = connection_.prepareStatement ("INSERT INTO "
                + TABLE + " (COL1, COL2) VALUES (?, 'Another string with a question mark?.')");
            ps.setInt (1, 54);
            ps.setString (2, "Testing - should fail.");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
counting of parameters - Attempt to set two parameters, one real parameter and 
one comment parameter.
**/
    public void Var009()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + TABLE);
            PreparedStatement ps = connection_.prepareStatement ("INSERT INTO "
                + TABLE + " (COL1, COL2) VALUES (?, 'Another string without a question mark.') -- This question mark ? should have no affect.");
            ps.setInt (1, -58);
            ps.setString (2, "Testing - should fail.");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
counting of parameters - Attempt to insert with one real parameter and 
two quoted parameters.
**/
    public void Var010()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + TABLE);
            PreparedStatement ps = connection_.prepareStatement ("INSERT INTO "
                + TABLE + " (COL1, COL2) VALUES (?, 'A string with two question marks? ?')");
            ps.setInt (1, 4289);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT COL1,COL2 FROM " + TABLE);
            rs.next ();
            int col1 = rs.getInt ("COL1");
            String col2 = rs.getString (2);
            rs.close ();
            
            assertCondition ((col1 == 4289) && (col2.equals ("A string with two question marks? ?")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
counting of parameters - Attempt to insert with one real parameter and 
two comment parameters.
**/
    public void Var011()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + TABLE);
            PreparedStatement ps = connection_.prepareStatement ("INSERT INTO "
                + TABLE + " (COL1, COL2) VALUES (?, 'The last string for now') -- This comment ? has two question marks?");
            ps.setInt (1, 4289);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT COL1,COL2 FROM " + TABLE);
            rs.next ();
            int col1 = rs.getInt ("COL1");
            String col2 = rs.getString (2);
            rs.close ();
            
            assertCondition ((col1 == 4289) && (col2.equals ("The last string for now")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
counting of parameters - Attempt to insert with one real parameter when
there is a single dash in the SQL statement.  This is to verify that
there needs to be 2 dashes to detect a comment.
**/
    public void Var012()
    {
        try {
            statement_.executeUpdate ("DELETE FROM " + TABLE);
            PreparedStatement ps = connection_.prepareStatement ("INSERT INTO "
                + TABLE + " (COL1, COL2) VALUES (-56, ?)");
            ps.setString (1, "Comments in SQL statements are no fun");
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT COL1,COL2 FROM " + TABLE);
            rs.next ();
            int col1 = rs.getInt ("COL1");
            String col2 = rs.getString (2);
            rs.close ();
            
            assertCondition ((col1 == -56) && (col2.equals ("Comments in SQL statements are no fun")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
getMetaData() - Should return null when the statement does not produce a result set
**/
    public void Var013 ()
    {
	int vrm = testDriver_.getRelease(); 
	if (getDriver () == JDTestDriver.DRIVER_NATIVE  && vrm <= 520 ) {
	    notApplicable("Prior to V5R3 the native driver also returns non null for ps.getMetaData()"); 
	} else { 
	    if (checkJdbc20 ()) 
	    {
		try 
		{
		    PreparedStatement ps = connection_.prepareStatement ("INSERT INTO " + TABLE + " (COL1) VALUES (?)");
		    ResultSetMetaData rsmd = ps.getMetaData ();
		    if (rsmd == null)
			succeeded();
		    else
			failed("rsmd returned");
		    ps.close ();
		}
		catch (Exception e) {
		    failed (e, "Unexpected Exception");
		}
	    }
	}
    }


/**
 * Test a weird native memory allocation error that the native driver experiences on the prepareStatement.
 */

public void Var014() {
  String sqlString = null;
  if (checkNative()) {
    try {
      Statement stmt = connection_.createStatement();
      sqlString = "CREATE OR REPLACE TABLE " + collection_
          + ".QAYPSJCDT(SEQNO INT, MCKEY VARCHAR(1000) FOR BIT DATA, ACTOBJECT VARCHAR(30000) FOR BIT DATA) ON REPLACE DELETE ROWS";
      stmt.executeUpdate(sqlString);
      sqlString = "INSERT INTO  " + collection_ + ".QAYPSJCDT VALUES(999, X'0D11', X'0d11')";
      stmt.executeUpdate(sqlString);
      sqlString = "SELECT * FROM " + collection_
          + ".QAYPSJCDT WHERE MCKEY IN (? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? ,"
          + " ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , "
          + "? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? ,"
          + " ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , "
          + "? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? ,"
          + " ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? ,"
          + " ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? ,"
          + " ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? ,"
          + " ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? ,"
          + " ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? ,"
          + " ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? ,"
          + " ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? ,"
          + " ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? ,"
          + " ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? ,"
          + " ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? ,"
          + " ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?  ) ORDER BY MCKEY ";
      PreparedStatement ps = connection_.prepareStatement(sqlString);

      ps.close();

      sqlString = "DROP TABLE " + collection_ + ".QAYPSJCDT";
      stmt.executeUpdate(sqlString);
      stmt.close();

      assertCondition(true);
    } catch (Exception e) {
      System.out.println("SQL string is :" + sqlString);
      failed(e, "Unexpected Exception - native Added");
    }
  }
}

    /**
     * test isPoolable when not set.  Should return true for prepared statement 
     *
     * @since 1.6 (JDBC 4.0)
     */ 
    public void Var015()
    {
	if (checkJdbc40()) { 
	    try
	    {
		PreparedStatement s = connection_.prepareStatement ("select * from sysibm.sysdummy1");
		boolean poolable = JDReflectionUtil.callMethod_B(s,"isPoolable"); 
		s.close ();
        if (isToolboxDriver())
            assertCondition (poolable==false, "isPoolable returned true sb false");
        else
    		assertCondition (poolable==true, "isPoolable returned false sb true");
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    isPoolable() - isPoolable on a closed statement.  Should throw an exception.
    **/
    public void Var016()
    {
	if (checkJdbc40()) { 
	    try
	    {
		PreparedStatement s = connection_.prepareStatement ("select * from sysibm.sysdummy1");
		s.close ();
		boolean poolable = JDReflectionUtil.callMethod_B(s,"isPoolable"); 
		failed("Didn't throw SQLException" +poolable );
	    }
	    catch(Exception e)
	    {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}

    }

    /**
     * setPoolable() Set true and check 
     *
     * @since 1.6 (JDBC 4.0)
     */ 
    public void Var017()
    {
	if (checkJdbc40()) { 
	    try
	    {
		PreparedStatement s = connection_.prepareStatement ("select * from sysibm.sysdummy1");
		JDReflectionUtil.callMethod_V(s,"setPoolable", true); 
		boolean poolable = JDReflectionUtil.callMethod_B(s,"isPoolable"); 
		s.close ();
		assertCondition (poolable ==true, "isPoolable returned false sb true");
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
    }

    /**
     * setPoolable() Set false and check 
     *
     * @since 1.6 (JDBC 4.0)
     */ 
    public void Var018()
    {
	if (checkJdbc40()) { 
	    try
	    {
		PreparedStatement s = connection_.prepareStatement ("select * from sysibm.sysdummy1");
		JDReflectionUtil.callMethod_V(s,"setPoolable", false); 
		boolean poolable = JDReflectionUtil.callMethod_B(s,"isPoolable"); 
		s.close ();
		assertCondition (poolable==false, "isPoolable returned true sb false");
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    isPoolable() - isPoolable on a closed statement.  Should throw an exception.
    **/
    public void Var019()
    {
	if (checkJdbc40()) {
	    try
	    {
		PreparedStatement s = connection_.prepareStatement ("select * from sysibm.sysdummy1");
		s.close ();
		JDReflectionUtil.callMethod_V(s,"setPoolable", true); 
		failed("Didn't throw SQLException"  );
	    }
	    catch(Exception e)
	    {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



    /**
     * Toolbox test for cps 7AYQXL  //test fetch/close rc=700, class=2
     * If maxRows == count in table then we had a logic error.
     * Use smaller block size to force multiple fetches to server.
     */ 
    public void Var020()
    {//if(1==1){ notApplicable("future Toolbox code"); return; }
        if ((!isToolboxDriver()))
        {
            notApplicable("Toolbox only TC");
            return;
        }
        else if ( getRelease() <= JDTestDriver.RELEASE_V7R1M0 ){
            succeeded("V6R1 onward");
            return;
        }
        else
        {
            int resultCount = 0; 
            try {
                String url = baseURL_
                
                ;
                Connection connX = testDriver_.getConnection (url+ ";block criteria=1;block size=8;cursor hold=false",systemObject_.getUserId(), encryptedPassword_);
                
                 
                connX.setAutoCommit(false);
                connX.setTransactionIsolation(2);           
 
                
                    
                    Statement stmt = connX.createStatement(); 
                    try{
                        stmt.execute("drop table " + TABLE2);
                    }catch(Exception e){                   
                    }
                    try{
                        stmt.execute("create table " + TABLE2 + " ( col1 varchar(100))");
                    }catch(Exception e){ }
                     
                    stmt.execute("delete from " + TABLE2);
                    PreparedStatement ps = connX.prepareStatement("insert into " + TABLE2 + " values(  ?  )");
                    for(int x = 0; x< 3000; x++){
                        ps.setInt(1,x);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                    
                    connX.commit();
                    ps.close();

                    ps = connX.prepareStatement("select * FROM " + TABLE2);  

                    resultCount = 0;
                    String results = "";
                    
                    
                    //////////test max = number of rows
                    ps.setMaxRows(3000);//must be = to rowcount in table to test this functionality
                    ResultSet rs = ps.executeQuery();
                    while(rs.next()){
                        rs.getString(1);
                        resultCount++;
                    }
                    if(resultCount != 3000)
                        results += " ResultSet count=" + resultCount + " sb=3000";
                   
                    
                    ////////////test max diff than number of rows
                    ps.setMaxRows(1);//must be = to rowcount in table to test this functionality
                    resultCount = 0;
                    rs = ps.executeQuery();
                    while(rs.next()){
                        rs.getString(1);
                        resultCount++;
                    }
                    if(resultCount != 1)
                        results += " ResultSet count=" + resultCount + " sb=1";
                   
                    
                    ////////////test max diff than number of rows
                    resultCount = 0;
                    ps.setMaxRows(2999);//must be = to rowcount in table to test this functionality
                    rs = ps.executeQuery();
              
                    while(rs.next()){
                        rs.getString(1);
                        resultCount++;
                    }
                    if(resultCount != 2999)
                        results += " ResultSet count=" + resultCount + " sb=2999";
                    
                    
                    ////////////test max diff than number of rows
                    resultCount = 0;
                    ps.setMaxRows(3001);//must be = to rowcount in table to test this functionality
                    rs = ps.executeQuery();
              
                    while(rs.next()){
                        rs.getString(1);
                        resultCount++;
                    }
                    if(resultCount != 3000)
                        results += " ResultSet count=" + resultCount + " sb=3000";
  
                    ////////////test max diff than number of rows
                    resultCount = 0;
                    ps.setMaxRows(4000);//must be = to rowcount in table to test this functionality
                    rs = ps.executeQuery();
              
                    while(rs.next()){
                        rs.getString(1);
                        resultCount++;
                    }
                    if(resultCount != 3000)
                        results += " ResultSet count=" + resultCount + " sb=3000";
                    
                    
                    connX.commit();
                     
                    rs.close();
                    stmt.close();
                    connX.close();
                     

             


                assertCondition(results.equals(""), results);
            } catch (Exception e) {
                failed(e, "Unexpected Exception, new TC added 3/18/08 resultCount="+resultCount );
            }
        }
    }


    /**
     * Toolbox test for cps 7AYQXL  //test fetch/close rc=700, class=2
     * If maxRows == count in table then we had a logic error.
     * 
     *  Use larger block size to force only one fetch to server.
     */ 
    public void Var021()
    {/// if(1==1){ notApplicable("future Toolbox code"); return; }
        if ((!isToolboxDriver()))
        {
            notApplicable("Toolbox only TC");
            return;
        }
        else if ( getRelease() <= JDTestDriver.RELEASE_V7R1M0 ){
            succeeded("V6R1 onward");
            return;
        }
        else
        {
            try {
                String url = baseURL_
                
                ;
                Connection connX = testDriver_.getConnection (url+ ";cursor hold=false",systemObject_.getUserId(), encryptedPassword_);
                
                 
                connX.setAutoCommit(false);
                connX.setTransactionIsolation(2);           
 
                
                    
                    Statement stmt = connX.createStatement(); 
                    try{
                        stmt.execute("drop table " + TABLE2);
                    }catch(Exception e){                   
                    }
                    try{
                        stmt.execute("create table " + TABLE2 + " ( col1 varchar(100))");
                    }catch(Exception e){ }
                     
                    stmt.execute("delete from " + TABLE2);
                    PreparedStatement ps = connX.prepareStatement("insert into " + TABLE2 + " values(  ?  )");
                    for(int x = 0; x< 10; x++){
                        ps.setInt(1,x);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                    
                    connX.commit();
                    ps.close();

                    ps = connX.prepareStatement("select * FROM " + TABLE2);  

                    int resultCount = 0;
                    String results = "";
                    
                    
                    //////////test max = number of rows
                    resultCount = 0;
                    ps.setMaxRows(10);//must be = to rowcount in table to test this functionality
                    ResultSet rs = ps.executeQuery();
                    while(rs.next()){
                        rs.getString(1);
                        resultCount++;
                    }
                    if(resultCount != 10)
                        results += " ResultSet count=" + resultCount + " sb=10";
                    
                    
                    //////////test max diff than number of rows
                    resultCount = 0;
                    ps.setMaxRows(1);//must be = to rowcount in table to test this functionality
                    rs = ps.executeQuery();
                    while(rs.next()){
                        rs.getString(1);
                        resultCount++;
                    }
                    if(resultCount != 1)
                        results += " ResultSet count=" + resultCount + " sb=1";
                    
                    
                    //////////test max diff than number of rows
                    resultCount = 0;
                    ps.setMaxRows(9);//must be = to rowcount in table to test this functionality
                    rs = ps.executeQuery();
                    while(rs.next()){
                        rs.getString(1);
                        resultCount++;
                    }
                    if(resultCount != 9)
                        results += " ResultSet count=" + resultCount + " sb=9";
                    
                    
                    
                    //////////test max diff than number of rows
                    resultCount = 0;
                    ps.setMaxRows(11);//must be = to rowcount in table to test this functionality
                    rs = ps.executeQuery();
                    while(rs.next()){
                        rs.getString(1);
                        resultCount++;
                    }
                    if(resultCount != 10)
                        results += " ResultSet count=" + resultCount + " sb=10";
                    
                    
                    
                    
                    //////////test max diff than number of rows
                    resultCount = 0;
                    ps.setMaxRows(99);//must be = to rowcount in table to test this functionality
                    rs = ps.executeQuery();
                    while(rs.next()){
                        rs.getString(1);
                        resultCount++;
                    }
                    if(resultCount != 10)
                        results += " ResultSet count=" + resultCount + " sb=10";
                     
                    
                    
                    
                    
                    
                    connX.commit();
                     
                    rs.close();
                    stmt.close();
                    connX.close();
                     

             


                    assertCondition(results.equals(""), results);
            } catch (Exception e) {
                failed(e, "Unexpected Exception, new TC added 3/18/08");
            }
        }
    }



    /**
     Execute sql statement with lots of text 
    **/
    public void Var022()
    {
        if (getRelease() <= JDTestDriver.RELEASE_V7R1M0 && isToolboxDriver())
        {
            notApplicable("Too complex for v5r3/4");
            return;
        }

	if (getDriver() == JDTestDriver.DRIVER_NATIVE &&
	    getRelease() <= JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Native fails in V5R4, needs CLI header fix");
	    return; 
	} 

	int length = 0;
	String sql = ""; 
	try
	{
	    String procLong = JDPSTest.COLLECTION + ".JDSMLONG";
	    try {
		Statement s = connection_.createStatement ();
		
		sql = "DROP PROCEDURE " + procLong; 
		s.executeUpdate(sql) ;

	    } catch (Exception e) {
	    } 

	    String before = " CREATE PROCEDURE " + procLong + " (IN INDATE DATE, OUT OUTDATE DATE) LANGUAGE SQL BEGIN SET OUTDATE = DATE(DAYS(INDATE)+1) ;"; 
	    String after = " END ";
	    int[]  testLength = { 1000,10000,100000,1000000,1048576  };
	    String char100 = "                                                                                                    "; 
	    for (int i = 0; i < testLength.length; i++) {
		System.out.println("Testing "+testLength[i]); 
		StringBuffer sb = new StringBuffer(0);
		sb.append(before);
		length = before.length() + after.length();
		while ( length < testLength[i]) {
		    if (testLength[i] - length >= 100) {
			sb.append(char100);
			length += 100; 
		    } else {
			while ( length < testLength[i]) {
			    sb.append(" ");
			    length++; 
			}
		    } 
		}
		sb.append(after); 
		
		sql = sb.toString();

		PreparedStatement ps = connection_.prepareStatement(sql); 
		ps.executeUpdate();
		ps.close();
		Statement s = connection_.createStatement(); 

		sql = "DROP PROCEDURE " + procLong; 
		s.executeUpdate(sql) ;
		s.close(); 
		
	    } 

	    assertCondition (true); 

	}
	catch(Exception e)
	{
	    String printSql = sql;
	    if (printSql.length()> 100) {
		printSql=printSql.substring(0,100)+"..."; 
	    } 
	    failed(e, "Unexpected Exception. Length="+length+"/"+sql.length()+" for SQL'"+printSql+"'  Added by Native 6/23/2008");
	}


    }


    public void testGetDB2ParameterName(String sql, String[] parameters) {
	try {
	    StringBuffer sb = new StringBuffer(" -- added by Toolbox 1/21/2015\n");
	    boolean passed = true; 

	    PreparedStatement ps = connection_.prepareStatement(sql);
	    if (ps instanceof AS400JDBCPreparedStatement) { 
		for (int i = 0; i < parameters.length; i++) {
		    String parameterName = ((AS400JDBCPreparedStatement)ps).getDB2ParameterName(1+i); 
		    if (!parameters[i].equals(parameterName)) {
			sb.append("For parameter "+(i+1)+" got '"+parameterName+"' sb '"+parameters[i]+"'\n");
			passed = false; 
		    } 
		}
	    }	
	    ps.close();
	    ps = null; 
	    assertCondition(passed, sb); 
	} catch (Exception e) {
	    failed(e, "Unexpected Exception. Added by Toolbox 1/21/2015");
	} 
    } 

  public void Var023() {
    String sql = "select CAST(? AS VARCHAR(10)) from SYSIBM.SYSDUMMY1";
    String[] parameters = { "00001" };

    testGetDB2ParameterName(sql, parameters);
  }

  public void Var024() {
    String sql = "select CAST(? AS VARCHAR(10)) from SYSIBM.SYSDUMMY1 WHERE IBMREQD=?";
    String[] parameters = { "00001","00002" };

    testGetDB2ParameterName(sql, parameters);
  }

  public void Var025() {
    String sql = "select CAST(? AS VARCHAR(10)), CAST(? AS INTEGER) from SYSIBM.SYSDUMMY1 WHERE IBMREQD=?";
    String[] parameters = { "00001","00002","00003" };

    testGetDB2ParameterName(sql, parameters);
  }

  /**
  getDB2ParameterName() - Should throw an exception on a closed prepared statement.
  **/
      public void Var026 ()
      {
         if (checkJdbc20 ()) {
             StringBuffer sb = new StringBuffer(); 
              try {
                  String sql = "SELECT IBMREQD FROM SYSIBM.SYSDUMMY1 WHERE IBMREQD = ?";
                  sb.append("Preparing "+sql+"\n"); 
                  PreparedStatement ps = connection_.prepareStatement (sql);
                  sb.append("Closing statement\n"); 
                  ps.close ();
                  if (ps instanceof AS400JDBCPreparedStatement) { 
                    String name = ((AS400JDBCPreparedStatement)ps).getDB2ParameterName(1);
                    failed ("Didn't throw SQLException "+name);
                  } else {
                    notApplicable("Toolbox getDB2ParameterName test"); 
                  }
              }
              catch (Exception e) {
                  assertExceptionContains(e, "Function sequence error", sb); 
              }
          }
      }

  /**
   * getDB2ParameterName() - Should throw an exception on zero index
   **/
  public void Var027() {
    StringBuffer sb = new StringBuffer();
    try {
      String sql = "SELECT IBMREQD FROM SYSIBM.SYSDUMMY1 WHERE IBMREQD = ?";
      sb.append("Preparing " + sql + "\n");
      PreparedStatement ps = connection_.prepareStatement(sql);
      if (ps instanceof AS400JDBCPreparedStatement) {
        sb.append("Getting name with index 0");
        String name = ((AS400JDBCPreparedStatement) ps).getDB2ParameterName(0);
        failed("Didn't throw SQLException " + name);
      } else {
        notApplicable("Toolbox getDB2ParameterName test");
      }
    } catch (Exception e) {
      assertExceptionContains(e, "Descriptor index not valid", sb);
    }
  }

  
  /**
   * getDB2ParameterName() - Should throw an exception on out of range 
   **/
  public void Var028() {
    StringBuffer sb = new StringBuffer();
    try {
      String sql = "SELECT IBMREQD FROM SYSIBM.SYSDUMMY1 WHERE IBMREQD = ?";
      sb.append("Preparing " + sql + "\n");
      PreparedStatement ps = connection_.prepareStatement(sql);
      if (ps instanceof AS400JDBCPreparedStatement) {
        sb.append("Getting name with index 0");
        String name = ((AS400JDBCPreparedStatement) ps).getDB2ParameterName(2);
        failed("Didn't throw SQLException " + name);
      } else {
        notApplicable("Toolbox getDB2ParameterName test");
      }
    } catch (Exception e) {
      assertExceptionContains(e, "Descriptor index not valid", sb);
    }
  }

}


