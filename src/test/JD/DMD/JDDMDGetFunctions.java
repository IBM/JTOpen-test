///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDMDGetFunctions.java
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
 // File Name:    JDDMDGetFunctions.java
 //
 // Classes:      JDDMDGetFunctions
 //
 ////////////////////////////////////////////////////////////////////////
 //
 //
 // 
 //
 ////////////////////////////////////////////////////////////////////////

package test.JD.DMD;

import com.ibm.as400.access.AS400;

import test.JDDMDTest;
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable; import java.util.Vector;



/**
Testcase JDDMDGetFunctions.  This tests the following methods
of the JDBC DatabaseMetaData class:

<ul>
<li>GetFunctions()
</ul>
**/
public class JDDMDGetFunctions
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDDMDGetFunctions";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDDMDTest.main(newArgs); 
   }



    // Private data.
    private Connection          connection_;
    private String              connectionCatalog_; 
    private Connection          closedConnection_;
    private DatabaseMetaData    dmd_;
    // private DatabaseMetaData    dmd2_;
    StringBuffer  message=new StringBuffer();  
    String underscore = ""; 
    

/**
Constructor.
**/
    public JDDMDGetFunctions (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDDMDGetFunctions",
            namesAndVars, runMode, fileOutputStream,
            password);
    }


    protected String getCatalogFromURL(String url) { 
      // System.out.println("BaseURL is "+baseURL_); 
      // must be running JCC, set to a valid value.
      // jdbc:db2://y0551p2:446/*LOCAL

      int lastColon = url.indexOf(":446");
      if (lastColon > 0) {
        String part1 = url.substring(0,lastColon);
        int lastSlash = part1.lastIndexOf('/',lastColon);
        if (lastSlash > 0) {
          return part1.substring(lastSlash+1).toUpperCase(); 
          
        }
      }
      return null; 
      
    }

/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
        throws Exception
    {
        connection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
        
        
        connectionCatalog_ = connection_.getCatalog(); 
        if (connectionCatalog_ == null) {
           connectionCatalog_ = getCatalogFromURL(baseURL_); 
           System.out.println("Warning:  connection.getCatalog() returned null setting to "+connectionCatalog_); 
        }
        dmd_ = connection_.getMetaData ();
       
        //create functions
        createFunction(connection_,  JDDMDTest.COLLECTION + ".JDDMDGFSI(smallint)", "returns smallint language java parameter style db2general  deterministic not fenced null call no sql external action  external name 'quickudf!q1short'");
        
        //createFunction(globaldbConnection_, "globaldb.q1NFsmallint(x smallint )", "returns smallint language sql deterministic not fenced returns null on null input begin return x; end  ");
    
     

        closedConnection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
        // dmd2_ = closedConnection_.getMetaData ();
        closedConnection_.close ();

	underscore = JDDMDTest.COLLECTION.substring(0,JDDMDTest.COLLECTION.length() -1 )+"_"; 
        

    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
        //Statement s = connection_.createStatement ();

        
        //s.close ();
        connection_.close ();
    }



    /**
     * Uses a connection to create a function
     * 
     * @param connection
     *            Connection used to create the function
     * @param function
     *            Name of the function (as passed to drop function)
     * @param procedureParms
     *            Remainder of create function statement
     * @exception java.lang.Exception
     *                Exception is thrown if functionis not created.
     */
    public static void createFunction(Connection connection, String function, String functionSpec) throws Exception
    {
        Statement s = null;
        try
        {

            //
            // make sure it is gone
            //
            dropFunction(connection, function, functionSpec);

            s = connection.createStatement();

            s.executeUpdate("create function " + function + functionSpec);

        } catch (Exception e)
        {
            throw e;
        } finally
        {
            try
            {
                if (s != null)
                    s.close();
            } catch (Exception ex)
            {}
        }

    }

    /**
     * Uses a connection to drop a function
     */
    public static void dropFunction(Connection connection, String function, String functionSpec) throws Exception
    {
        Statement s = null;
        try
        {

            s = connection.createStatement();
            //
            // make sure it is gone
            //
            try
            {
                // replace any x y z arguments
                String noArgs = function;
                int argindex = noArgs.indexOf("x ");
                while (argindex > 0)
                {
                    // if (debug) System.out.println("RTest: noArgs = " + noArgs
                    // + "argindex = "+argindex );
                    noArgs = noArgs.substring(0, argindex) + noArgs.substring(argindex + 1);
                    argindex = noArgs.indexOf("x ");
                }

                s.executeUpdate("drop  function " + noArgs);
            } catch (Exception e)
            {
                //
                // The only exception we should silently tolerate is a not found
                // -- the rest will print messages
                //
                if (e.toString().indexOf("not found") < 0)
                {
                    //
                    // Try more 60 times if something in use
                    //
                    int count = 0;
                    while (count < 60 && e != null && e.toString().indexOf("in use") > 0)
                    {
                        try
                        {

                            s.executeUpdate("drop function " + function);
                            e = null;
                        } catch (Exception e2)
                        {
                            count++;
                            e = e2;
                        }
                    }
                    if (e != null)
                    {
                        System.out.println("JDDMDGetFunctions.debug:  warning:  unable to drop function");
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e)
        {
            throw e;
        } finally
        {
            try
            {
                if (s != null)
                    s.close();
            } catch (Exception ex)
            {}
        }

    }


/**
GetFunctions() -
                Check the result set format.
**/
    public void Var001()
    {
        if ((getDriver() == JDTestDriver.DRIVER_TOOLBOX) ||  checkJdbc40()) {
            
        if(getRelease() < JDTestDriver.RELEASE_V7R1M0)  {
            notApplicable("V5R5 variation");
            return;
        }
        
        try {
            message.setLength(0); 
            /*  <LI><B>FUNCTION_CAT</B> String => function catalog (may be <code>null</code>)
            *   <LI><B>FUNCTION_SCHEM</B> String => function schema (may be <code>null</code>)
            *   <LI><B>FUNCTION_NAME</B> String => function name
            *   <LI><B>REMARKS</B> String => explanatory comment on the function
            *   <LI><B>FUNCTION_TYPE</B> short => kind of function:
                       * functionResultUnknown - Cannot determine if a return value or table will be returned
                       * functionNoTable- Does not return a table
                       * functionReturnsTable - Returns a table
            *   <LI><B>SPECIFIC_NAME</B> String  => the name which uniquely identifies 
            *  this function within its schema */
            
            //ResultSet rs = dmd_.getFunctions (null, null, null);
            ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSS(dmd_, 
                "getFunctions", null, null, null); 
            
            
            ResultSetMetaData rsmd = rs.getMetaData ();

            String[] expectedNames = { "FUNCTION_CAT", "FUNCTION_SCHEM",
                "FUNCTION_NAME", "REMARKS", "FUNCTION_TYPE", "SPECIFIC_NAME" };
            int[] expectedTypes = {
		Types.VARCHAR,
		Types.VARCHAR,
		Types.VARCHAR,
		Types.VARCHAR,
		Types.SMALLINT,
		Types.VARCHAR  };

            int[] expectedTypes40 = {
		Types.VARCHAR,
		Types.VARCHAR,
		Types.VARCHAR,
		-9,
		Types.SMALLINT,
		Types.VARCHAR  };


/*
 10/6/2015 -- back to VARCHAR 
		if (getDriver() == JDTestDriver.DRIVER_TOOLBOX && getRelease() >=  JDTestDriver.RELEASE_V7R1M0) {
		    // Changed for running 15T 4/16/2010 
		    expectedTypes[3] = Types.LONGVARCHAR; 
		} 
*/             
 
            
            int count = rsmd.getColumnCount ();
	    if (getJdbcLevel() >= 4) {
		expectedTypes = expectedTypes40; 
	    } 
            boolean namesCheck = JDDMDTest.checkColumnNames (rsmd, expectedNames, message);
            boolean typesCheck = JDDMDTest.checkColumnTypes (rsmd, expectedTypes, message);

            rs.close ();
            assertCondition ((count == 6) && (namesCheck) && (typesCheck), 
                "count is "+count+"sb 6\n"+
                "namesCheck = "+namesCheck+"\n"+
                "typesCheck = "+typesCheck+"\n"+
                message);
        }
        catch (Exception e)  {
            failed (e, "08/31/06 New jdbc 40 TC.  Unexpected Exception");
        }
        }
    }



/**
GetFunctions() - Get a list of those created in this testcase and
verify all columns with system remarks (the default).
**/
    public void Var002() {
        if ((getDriver() == JDTestDriver.DRIVER_TOOLBOX) || checkJdbc40()) {
            
        
        if(getRelease() < JDTestDriver.RELEASE_V7R1M0)  {
            notApplicable("V5R5 variation");
            return;
        }
        
        message.setLength(0); 
        try {
            ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSS(dmd_, "getFunctions", 
                null, JDDMDTest.COLLECTION, "JDDMDGFSI"); 
            boolean success = true;
            
            int rows = 0;
            while (rs.next()) {
                ++rows;
                success = JDDMDTest.check(rs.getString("FUNCTION_CAT"), connectionCatalog_, "FUNCTION_CAT", "NULL", message, success) ;
                success = JDDMDTest.check(rs.getString("FUNCTION_SCHEM"), JDDMDTest.COLLECTION, "FUNCTION_SCHEMA", JDDMDTest.COLLECTION, message, success); 
                success = JDDMDTest.check(rs.getString("FUNCTION_NAME"), "JDDMDGFSI", "FUNCTION_NAME", "NULL", message, success); 
            }
            
            rs.close();
            if (rows != 1)
                message.append("rows = " + rows + " sb 1 ");
            assertCondition((rows == 1) && success, "\n"+message.toString() );
        } catch (Exception e) {
            failed(e, "Unexpected Exception");
        }
    }
  }

/**
 * getFunctions.. Verify catalog parameter when matches catalog name as stored in the database
 */
    public void Var003() {
      if ((getDriver() == JDTestDriver.DRIVER_TOOLBOX) || checkJdbc40()) {
        
        
        if(getRelease() < JDTestDriver.RELEASE_V7R1M0)  {
          notApplicable("V5R5 variation");
          return;
        }
        
        message.setLength(0); 
        try {
          ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSS(dmd_, "getFunctions", 
              connectionCatalog_, null, "JDDMDGFSI"); 
          boolean success = true;
          
          int rows = 0;
          while (rs.next()) {
            ++rows;
            
            success = JDDMDTest.check(rs.getString("FUNCTION_CAT"), connectionCatalog_, "FUNCTION_CAT", "NULL", message, success) ;
	    /*
	     // The query could have produced other schemas also
            success = JDDMDTest.check(rs.getString("FUNCTION_SCHEM"), JDDMDTest.COLLECTION, "FUNCTION_SCHEMA", JDDMDTest.COLLECTION, message, success);
	     */ 
            success = JDDMDTest.check(rs.getString("FUNCTION_NAME"), "JDDMDGFSI", "FUNCTION_NAME", "NULL", message, success); 
          }
          
	  rs.close();
	  if (rows < 1)
	      message.append("rows = " + rows + " sb >= 1 ");
	  assertCondition((rows >= 1) && success, "\n"+message.toString() );
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }

  
    
/*
 *  getFunctions .. Verify that catalog parameter of  "" retrieves those without a catalog, 
 *
 * On iSeries, this still means to retrieve everything. 
 *
 */

    public void Var004() {
      if ((getDriver() == JDTestDriver.DRIVER_TOOLBOX) || checkJdbc40()) {
        
        
        if(getRelease() < JDTestDriver.RELEASE_V7R1M0)  {
          notApplicable("V5R5 variation");
          return;
        }
        
        message.setLength(0); 
        message.append("Testing catalog parameter of \"\"\n"); 
        try {
          ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSS(dmd_, "getFunctions", 
              "", null, "%"); 
          boolean success = true;
          
          int rows = 0;
          while (rs.next()) {
            ++rows;
          }
          
          rs.close();
          if (rows == 0)
            message.append("rows = " + rows + " sb > 0 ");
          assertCondition((rows > 0) && success, "\n"+message.toString() );
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }

        
/*
 * getFunctions .. Verify that catalog parameter of null is not used to narrow the search (tested by Var002)   
 */
    
/*
 * getFunctions .. Verify that schemaPattern with % correctly matches schema name
 */
    public void Var005() {
      if ((getDriver() == JDTestDriver.DRIVER_TOOLBOX) || checkJdbc40()) {
        
        
        if(getRelease() < JDTestDriver.RELEASE_V7R1M0)  {
          notApplicable("V5R5 variation");
          return;
        }
        
        message.setLength(0); 
        
        try {
          ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSS(dmd_, "getFunctions", 
              null, JDDMDTest.SCHEMAS_PERCENT, "JDDMDGFSI"); 
          boolean success = true;
	  boolean schemaFound = false; 
          int rows = 0;
          while (rs.next()) {
            ++rows;
            success = JDDMDTest.check(rs.getString("FUNCTION_CAT"), connectionCatalog_, "FUNCTION_CAT", "NULL", message, success) ;
	    if (!schemaFound) {
		schemaFound =  JDDMDTest.check(rs.getString("FUNCTION_SCHEM"), JDDMDTest.COLLECTION, "FUNCTION_SCHEMA", JDDMDTest.COLLECTION, message, true);
	    }
            success = JDDMDTest.check(rs.getString("FUNCTION_NAME"), "JDDMDGFSI", "FUNCTION_NAME", "NULL", message, success); 
          }
          
          rs.close();
          if (rows < 1)
            message.append("rows = " + rows + " sb > 1  ");
          assertCondition((rows >= 1) && success && schemaFound, "\n"+message.toString() );
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }

    
/*
 * getFunctions .. Verify that schemaPattern with _ correctly matches schema name
 */
    public void Var006() {
      if ((getDriver() == JDTestDriver.DRIVER_TOOLBOX) || checkJdbc40()) {
        
        
        if(getRelease() < JDTestDriver.RELEASE_V7R1M0)  {
          notApplicable("V5R5 variation");
          return;
        }

        message.setLength(0); 
        try {
          ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSS(dmd_, "getFunctions", 
              null, underscore, "JDDMDGFSI"); 
          boolean success = true;
          
          int rows = 0;
          while (rs.next()) {
            ++rows;
            success = JDDMDTest.check(rs.getString("FUNCTION_CAT"), connectionCatalog_, "FUNCTION_CAT", "NULL", message, success) ;
            success = JDDMDTest.check(rs.getString("FUNCTION_SCHEM"), underscore, "FUNCTION_SCHEMA", underscore, message, success); 
            success = JDDMDTest.check(rs.getString("FUNCTION_NAME"), "JDDMDGFSI", "FUNCTION_NAME", "NULL", message, success); 
          }
          
          rs.close();
          if (rows == 0)
            message.append("rows = " + rows + " sb 1 ");
          assertCondition((rows >= 1) && success, "rows="+rows+" success="+success+"\n"+message.toString() );
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }

    
/*
 * getFunctions .. Verify that schemaPattern with % does not  matches schema name
 */
    public void Var007() {
      if ((getDriver() == JDTestDriver.DRIVER_TOOLBOX) || checkJdbc40()) {
        
        
        if(getRelease() < JDTestDriver.RELEASE_V7R1M0)  {
          notApplicable("V5R5 variation");
          return;
        }
        
        message.setLength(0); 
        try {
          ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSS(dmd_, "getFunctions", 
              null, "DOESNOTEXIST%", "%"); 
          boolean success = true;
          
          int rows = 0;
          while (rs.next()) {
            ++rows;
          }
          
          rs.close();
          if (rows != 0)
            message.append("rows = " + rows + " sb 0 ");
          assertCondition((rows == 0) && success, "\n"+message.toString() );
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }

    
/*
 * getFunctions .. Verify that schemaPattern of "" retrieves those without a schema, which
 * for iSeries will be nothing.  
 */
    public void Var008() {
      if ((getDriver() == JDTestDriver.DRIVER_TOOLBOX) || checkJdbc40()) {
        
        
        if(getRelease() < JDTestDriver.RELEASE_V7R1M0)  {
          notApplicable("V5R5 variation");
          return;
        }
        
        message.setLength(0); 
        try {
          ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSS(dmd_, "getFunctions", 
              null, "", "%"); 
          boolean success = true;
          
          int rows = 0;
          while (rs.next()) {
            ++rows;
          }
          
          rs.close();
          if (rows != 0)
            message.append("rows = " + rows + " sb 0 ");
          assertCondition((rows == 0) && success, "\n"+message.toString() );
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }

    
    
/*
 * getFunctions .. Verify that schemaPattern of null is not used to narrow the search 
 */
    /*
     * getFunctions .. Verify that schemaPattern with _ correctly matches schema name
     */
        public void Var009() {
          if ((getDriver() == JDTestDriver.DRIVER_TOOLBOX) || checkJdbc40()) {
            
            
            if(getRelease() < JDTestDriver.RELEASE_V7R1M0)  {
              notApplicable("V5R5 variation");
              return;
            }
            
            message.setLength(0); 
            try {
              ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSS(dmd_, "getFunctions", 
                  null, null, "JDDMDGFSI"); 
              boolean success = true;
              
              int rows = 0;
              while (rs.next()) {
                ++rows;
                success = JDDMDTest.check(rs.getString("FUNCTION_CAT"), connectionCatalog_, "FUNCTION_CAT", "NULL", message, success) ;
                success = JDDMDTest.check(rs.getString("FUNCTION_NAME"), "JDDMDGFSI", "FUNCTION_NAME", "NULL", message, success); 
              }
              
              rs.close();
              if (rows < 1)
                message.append("rows = " + rows + " sb >= 1 ");
              assertCondition((rows >= 1) && success, "\n"+message.toString() );
            } catch (Exception e) {
              failed(e, "Unexpected Exception");
            }
          }
        }
    
/*
 * getFunctions .. Verify that fuctionNamePattern with % correctly matches function name  
 */
            public void Var010() {
              if ((getDriver() == JDTestDriver.DRIVER_TOOLBOX) || checkJdbc40()) {
                
                
                if(getRelease() < JDTestDriver.RELEASE_V7R1M0)  {
                  notApplicable("V5R5 variation");
                  return;
                }
                
                message.setLength(0); 
                try {
                  ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSS(dmd_, "getFunctions", 
                      null, JDDMDTest.COLLECTION, "JDDMDGFS%"); 
                  boolean success = true;
                  
                  int rows = 0;
                  while (rs.next()) {
                    ++rows;
                    success = JDDMDTest.check(rs.getString("FUNCTION_CAT"), connectionCatalog_, "FUNCTION_CAT", "NULL", message, success) ;
                    success = JDDMDTest.check(rs.getString("FUNCTION_NAME"), "JDDMDGFSI", "FUNCTION_NAME", "NULL", message, success); 
                  }
                  
                  rs.close();
                  if (rows < 1)
                    message.append("rows = " + rows + " sb >= 1 ");
                  assertCondition((rows >= 1) && success, "\n"+message.toString() );
                } catch (Exception e) {
                  failed(e, "Unexpected Exception");
                }
              }
            }
        
/*
 * getFunctions .. Verify that fuctionNamePattern with _ correctly matches function name  
 */
            public void Var011() {
              if ((getDriver() == JDTestDriver.DRIVER_TOOLBOX) || checkJdbc40()) {
                
                
                if(getRelease() < JDTestDriver.RELEASE_V7R1M0)  {
                  notApplicable("V5R5 variation");
                  return;
                }
                
                message.setLength(0); 
                try {
                  ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSS(dmd_, "getFunctions", 
                      null, JDDMDTest.COLLECTION, "JDDMDGFS_"); 
                  boolean success = true;
                  
                  int rows = 0;
                  while (rs.next()) {
                    ++rows;
                    success = JDDMDTest.check(rs.getString("FUNCTION_CAT"), connectionCatalog_, "FUNCTION_CAT", "NULL", message, success) ;
                    success = JDDMDTest.check(rs.getString("FUNCTION_NAME"), "JDDMDGFSI", "FUNCTION_NAME", "NULL", message, success); 
                  }
                  
                  rs.close();
                  if (rows < 1)
                    message.append("rows = " + rows + " sb >= 1 ");
                  assertCondition((rows >= 1) && success, "\n"+message.toString() );
                } catch (Exception e) {
                  failed(e, "Unexpected Exception");
                }
              }
            }
        

            
            
 /*
 * getFunctions .. Verify that fuctionNamePattern with % and not match does not return anything
 */

            public void Var012() {
              if ((getDriver() == JDTestDriver.DRIVER_TOOLBOX) || checkJdbc40()) {
                
                
                if(getRelease() < JDTestDriver.RELEASE_V7R1M0)  {
                  notApplicable("V5R5 variation");
                  return;
                }
                
                message.setLength(0); 
                try {
                  ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSS(dmd_, "getFunctions", 
                      null, JDDMDTest.COLLECTION, "BOGUSFUNCTION%"); 
                  boolean success = true;
                  
                  int rows = 0;
                  while (rs.next()) {
                    ++rows;
                  }
                  
                  rs.close();
                  if (rows != 0)
                    message.append("rows = " + rows + " sb 0 ");
                  assertCondition((rows == 0) && success, "\n"+message.toString() );
                } catch (Exception e) {
                  failed(e, "Unexpected Exception");
                }
              }
            }

            

/*
 *  getFunctions .. Verify that functionName Pattern "" does not find results
 */
            public void Var013() {
              if ((getDriver() == JDTestDriver.DRIVER_TOOLBOX) || checkJdbc40()) {
                
                
                if(getRelease() < JDTestDriver.RELEASE_V7R1M0)  {
                  notApplicable("V5R5 variation");
                  return;
                }
                
                message.setLength(0); 
                try {
                  ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSS(dmd_, "getFunctions", 
                      null, JDDMDTest.COLLECTION, ""); 
                  boolean success = true;
                  
                  int rows = 0;
                  while (rs.next()) {
                    ++rows;
                  }
                  
                  rs.close();
                  if (rows != 0)
                    message.append("rows = " + rows + " sb 0 ");
                  assertCondition((rows == 0) && success, "\n"+message.toString() );
                } catch (Exception e) {
                  failed(e, "Unexpected Exception");
                }
              }
            }


/*
 * getFunctions() - Check the RSMD results and extended metadata is not available
 */
public void Var014() {
  checkRSMD(false);
}
/*
 * getFunctions() - Check the RSMD results and extended metadata is not available
 */
public void Var015() {
	  checkRSMD(true);
}

    public void checkRSMD(boolean extendedMetadata)
    {

	Connection connection = connection_;
	DatabaseMetaData dmd = dmd_; 
	int TESTSIZE=40; 

	int j=0;
	int col=0; 
	String added=" -- Added 03/01/07 by native JDBC driver"; 
	boolean passed=true;
  message.setLength(0); 
	String [][] methodTests = {

	    {"isAutoIncrement",		"1","false"},
	    {"isCaseSensitive",		"1","true"},
	    {"isSearchable",		"1","true"},
	    {"isCurrency",		"1","false"},
	    {"isNullable",		"1","0"},
	    {"isSigned",		"1","false"},
	    {"getColumnDisplaySize",	"1","128"},
	    {"getColumnLabel",		"1","FUNCTION_CAT"},
	    {"getColumnName",		"1","FUNCTION_CAT"},
	    {"getPrecision",		"1","128"},
	    {"getScale",		"1","0"},
	    {"getCatalogName",		"1","RCHASJLB"},
	    {"getColumnType",		"1","12"},
	    {"getColumnTypeName",	"1","VARCHAR"},
	    {"isReadOnly",		"1","true"},
	    {"isWritable",		"1","false"},
	    {"isDefinitelyWritable",	"1","false"},
	    {"getColumnClassName",	"1","java.lang.String"},

	    {"isAutoIncrement",		"2","false"},
	    {"isCaseSensitive",		"2","true"},
	    {"isSearchable",		"2","true"},
	    {"isCurrency",		"2","false"},
	    {"isNullable",		"2","0"},
	    {"isSigned",		"2","false"},
	    {"getColumnDisplaySize",	"2","128"},
	    {"getColumnLabel",		"2","FUNCTION_SCHEM"},
	    {"getColumnName",		"2","FUNCTION_SCHEM"},
	    {"getPrecision",		"2","128"},
	    {"getScale",		"2","0"},
	    {"getCatalogName",		"2","RCHASJLB"},
	    {"getColumnType",		"2","12"},
	    {"getColumnTypeName",	"2","VARCHAR"},
	    {"isReadOnly",		"2","true"},
	    {"isWritable",		"2","false"},
	    {"isDefinitelyWritable",	"2","false"},
	    {"getColumnClassName",	"2","java.lang.String"},

	    {"isAutoIncrement",		"3","false"},
	    {"isCaseSensitive",		"3","true"},
	    {"isSearchable",		"3","true"},
	    {"isCurrency",		"3","false"},
	    {"isNullable",		"3","0"},
	    {"isSigned",		"3","false"},
	    {"getColumnDisplaySize",	"3","128"},
	    {"getColumnLabel",		"3","FUNCTION_NAME"},
	    {"getColumnName",		"3","FUNCTION_NAME"},
	    {"getPrecision",		"3","128"},
	    {"getScale",		"3","0"},
	    {"getCatalogName",		"3","RCHASJLB"},
	    {"getColumnType",		"3","12"},
	    {"getColumnTypeName",	"3","VARCHAR"},
	    {"isReadOnly",		"3","true"},
	    {"isWritable",		"3","false"},
	    {"isDefinitelyWritable",	"3","false"},
	    {"getColumnClassName",	"3","java.lang.String"},

	    {"isAutoIncrement",		"4","false"},
	    {"isCaseSensitive",		"4","true"},
	    {"isSearchable",		"4","true"},
	    {"isCurrency",		"4","false"},
	    {"isNullable",		"4","1"},
	    {"isSigned",		"4","false"},
	    {"getColumnDisplaySize",	"4","2000"},
	    {"getColumnLabel",		"4","REMARKS"},
	    {"getColumnName",		"4","REMARKS"},
	    {"getPrecision",		"4","2000"},
	    {"getScale",		"4","0"},
	    {"getCatalogName",		"4","RCHASJLB"},
	    {"getColumnType",		"4","12"},
	    {"getColumnTypeName",	"4","VARCHAR"},
	    {"isReadOnly",		"4","true"},
	    {"isWritable",		"4","false"},
	    {"isDefinitelyWritable",	"4","false"},
	    {"getColumnClassName",	"4","java.lang.String"},

	    {"isAutoIncrement",		"5","false"},
	    {"isCaseSensitive",		"5","false"},
	    {"isSearchable",		"5","true"},
	    {"isCurrency",		"5","false"},
	    {"isNullable",		"5","0"},
	    {"isSigned",		"5","true"},
	    {"getColumnDisplaySize",	"5","6"},
	    {"getColumnLabel",		"5","FUNCTION_TYPE"},
	    {"getColumnName",		"5","FUNCTION_TYPE"},
	    {"getPrecision",		"5","5"},
	    {"getScale",		"5","0"},
	    {"getCatalogName",		"5","RCHASJLB"},
	    {"getColumnType",		"5","5"},
	    {"getColumnTypeName",	"5","SMALLINT"},
	    {"isReadOnly",		"5","true"},
	    {"isWritable",		"5","false"},
	    {"isDefinitelyWritable",	"5","false"},
	    {"getColumnClassName",	"5","java.lang.Integer"},

	    {"isAutoIncrement",		"6","false"},
	    {"isCaseSensitive",		"6","true"},
	    {"isSearchable",		"6","true"},
	    {"isCurrency",		"6","false"},
	    {"isNullable",		"6","0"},
	    {"isSigned",		"6","false"},
	    {"getColumnDisplaySize",	"6","128"},
	    {"getColumnLabel",		"6","SPECIFIC_NAME"},
	    {"getColumnName",		"6","SPECIFIC_NAME"},
	    {"getPrecision",		"6","128"},
	    {"getScale",		"6","0"},
	    {"getCatalogName",		"6","RCHASJLB"},
	    {"getColumnType",		"6","12"},
	    {"getColumnTypeName",	"6","VARCHAR"},
	    {"isReadOnly",		"6","true"},
	    {"isWritable",		"6","false"},
	    {"isDefinitelyWritable",	"6","false"},
	    {"getColumnClassName",	"6","java.lang.String"},


	};


        String[][] fixup = {};
	String usingFixup = "fixup";

	String [][] nativeExtendedDifferences = {
	    {"isSearchable","1","false"},
	    {"isSearchable","2","false"},
	    {"isSearchable","3","false"},
	    {"isSearchable","4","false"},
	    {"isSearchable","5","false"},
	    {"isSearchable","6","false"},
	}; 


	String [][] nativeExtendedDifferences72 = {
	    {"isSearchable","1","false"},
	    {"isSearchable","2","false"},
	    {"isSearchable","3","false"},
	    {"isSearchable","4","false"},
	    {"isSearchable","5","false"},
	    {"isSearchable","6","false"},
	    {"getColumnTypeName","4","NVARCHAR"},
	}; 

	String [][] nativeExtendedDifferences7240 = {
	    {"isSearchable","1","false"},
	    {"isSearchable","2","false"},
	    {"isSearchable","3","false"},
	    {"isSearchable","4","false"},
	    {"isSearchable","5","false"},
	    {"isSearchable","6","false"},
	    {"getColumnTypeName","4","NVARCHAR"},
	    { "getColumnType","4","-9"},



	}; 

	String [][] nativeExtendedDifferences7340 = {
	    {"isSearchable","1","false"},
	    {"isSearchable","2","false"},
	    {"isSearchable","3","false"},
	    {"isSearchable","4","false"},
	    {"isSearchable","5","false"},
	    {"isSearchable","6","false"},
	    {"getColumnTypeName","4","NVARCHAR"},
	    { "getColumnType","4","-9"},

	    {"getColumnLabel","1","Function            Cat"},
	    {"getColumnLabel","2","Function            Schem"},
     {"getColumnLabel","3","Function            Name"},
     {"getColumnLabel","4","Remarks"},
     {"getColumnLabel","5","Function            Type"},
     {"getColumnLabel","6","Specific            Name"},


	}; 

	String [][] v7r2fixup = {
	    {"getColumnTypeName","4","NVARCHAR"},
	};

	String [][] v7r2fixup40 = {
	    {"getColumnTypeName","4","NVARCHAR"},
	    {"getColumnType","4","-9", },
	};

	String [][] v7r3fixup40 = {
	    {"getColumnTypeName","4","NVARCHAR"},
	    {"getColumnType","4","-9", },
	    {"getColumnLabel","1","Function Cat" },
	    {"getColumnLabel","2","Function Schem" },
	    {"getColumnLabel","3","Function Name" },
	    {"getColumnLabel","4","Remarks" },
	    {"getColumnLabel","5","Function Type" },
	    {"getColumnLabel","6","Specific Name" },
	};

	String[][] v7r1fixupTB = {
	    {"getPrecision","4","2000"},
	    {"getColumnTypeName","4","VARGRAPHIC"},

	};

	String[][] fixupJtopenlite = {
	    { "getColumnTypeName","4","VARGRAPHIC"}, 
	} ; 

	if (checkJdbc40()) { 
	    try {

		if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {

			    fixup = fixupJtopenlite; 
			    usingFixup = "fixupJtopenlite"; 

		} else { 
		    if (extendedMetadata) {
			String url = baseURL_
			  + ";extended metadata=true"; 
			if (getDriver() == JDTestDriver.DRIVER_JCC) {
			    url = baseURL_; 
			}
			connection = testDriver_.getConnection (url, 
								userId_, encryptedPassword_);
			dmd= connection.getMetaData ();

			if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
			    if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
				if (getJdbcLevel()>= 4) {
				    if (getRelease() >= JDTestDriver.RELEASE_V7R3M0) {
					fixup = nativeExtendedDifferences7340;
					usingFixup = "nativeExtendedDifferences7340";
				    } else { 
					fixup = nativeExtendedDifferences7240;
					usingFixup = "nativeExtendedDifferences7240";
				    }
				} else { 
				    fixup = nativeExtendedDifferences72;
				    usingFixup = "nativeExtendedDifferences72";
				}
			    } else { 
				fixup = nativeExtendedDifferences;
				usingFixup = "nativeExtendedDifferences"; 
			    }
			} else if (getDriver() == JDTestDriver.DRIVER_TOOLBOX &&
				   getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
			    if (getJdbcLevel()>= 4) {
				if (getRelease() >= JDTestDriver.RELEASE_V7R3M0) {
				    fixup = v7r3fixup40;
				    usingFixup = "v7r3fixup40";
				} else { 
				    fixup = v7r2fixup40;
				    usingFixup = "v7r2fixup40";
				}

			    } else { 

				fixup = v7r1fixupTB; 
				usingFixup = "v7r1fixupTB"; 
			    }
			}

		    } else {
			if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
			    if (getJdbcLevel()>= 4) {
				fixup = v7r2fixup40;
				usingFixup = "v7r2fixup40";

			    } else { 
				fixup = v7r2fixup;
				usingFixup = "v7r2fixup";
			    }
			}
			if (getDriver() == JDTestDriver.DRIVER_TOOLBOX &&
			    getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
			    if (getJdbcLevel()>= 4) {
				fixup = v7r2fixup40;
				usingFixup = "v7r2fixup40";

			    } else { 

				fixup = v7r1fixupTB; 
				usingFixup = "v7r1fixupTB";
			    }

			}
		    }
		}
                
		for (j = 0; j < fixup.length; j++) {
			boolean found = false; 
			for (int k = 0; !found &&  k < methodTests.length; k++) {
			    if (fixup[j][0].equals(methodTests[k][0]) &&
                                fixup[j][1].equals(methodTests[k][1])) {
				methodTests[k] = fixup[j]; 
				found = true; 
			    } 
			} 
		     
		}
		String catalog = connection.getCatalog();
		if (catalog == null) {
		    catalog = system_.toUpperCase();
		}

		ResultSet[] rsA =  new ResultSet[TESTSIZE];
		ResultSetMetaData[] rsmdA = new ResultSetMetaData[TESTSIZE];
		for (j = 0; j < TESTSIZE; j++) {
			try{
				rsA[j] = (ResultSet) JDReflectionUtil.callMethod_OSSS(dmd, 
						"getFunctions", connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT, "FUNCS_");
			}catch(Exception e){
				if(getDriver() == JDTestDriver.DRIVER_TOOLBOX && getRelease() < JDTestDriver.RELEASE_V7R1M0){
					assertCondition(e.getMessage().indexOf("not support") != -1);
					return;
				}
			}
			if (rsA[j] == null) {
			    failed("No result set returned for getFunctions");
			    return; 
			}
				
		    rsmdA[j] = rsA[j].getMetaData ();

		    ResultSetMetaData rsmd = rsmdA[j]; 

		    for (int i = 0; i < methodTests.length; i++) {
			String answer = "NOT SET"; 
			String method = methodTests[i][0];
			col = Integer.parseInt(methodTests[i][1]);
			String expected = methodTests[i][2];
			if (expected.equals("RCHASJLB")) {
			    expected=catalog; 
			}

			answer = callRsmdMethod(rsmd, method, col);


			if (answer==null) answer="null";
			if (answer.equals(expected)) {
			// ok
			} else {
			    passed=false;
			    if (j == 0) { 
				message.append("Expected: \""+method+"\",\""+col+"\",\""+expected+"\"\n"+
					       "Got:      \""+method+"\",\""+col+"\",\""+answer+"\"\n");
			    }
			} 
		    } /* for i */
		} /* for j */ 
		assertCondition(passed, message.toString()+" usingFixup="+usingFixup+" "+added);

	    } catch (Exception e) {
		failed (e, "Unexpected Error on loop "+j+" col = "+col+" "+added);
	    } catch (Error e ) {
		failed (e, "Unexpected Error on loop "+j+" col = "+col+" "+added);
	    } 
	}
    }


    /**
     * Test combinations of functions to make sure we get expected results
     */


    String expectRows="EXPECT ROWS";
    String expectZeroRows="EXPECT ZERO ROWS";

    void testPattern(String catalog, String schemaPattern, String functionNamePattern, String expectedResults ) {
      message.setLength(0); 
	message.append("Testing '"+catalog+"', '"+schemaPattern+"', '"+functionNamePattern+"', '"+expectedResults+"\n"); 
	if (checkJdbc40()) {
	    try {

    		ResultSet rs = null;
	    	try{
	    		rs = (ResultSet) JDReflectionUtil.callMethod_OSSS(dmd_,
	    				"getFunctions",
	    				catalog,
	    				schemaPattern,
	    				functionNamePattern); 
	    	}catch(Exception ee) {
	    		if(getDriver() == JDTestDriver.DRIVER_TOOLBOX && getRelease() < JDTestDriver.RELEASE_V7R1M0){
	    			assertCondition(ee.getMessage().indexOf("not support") != -1);
	    			return;
	    		}
	    	}

		if (rs == null) {
		    failed("No result set returned for getFunctions catalog="+catalog+
			   "schemaPattern="+schemaPattern+
			   "functionNamePattern="+functionNamePattern);
		    return; 
		}

		boolean foundRows = rs.next();

		if (expectedResults == expectRows) {
	 	    // Dont forget to close the rs -- otherwise they will pile up and
		    // cause a SQL7049
		    rs.close();
		    assertCondition(foundRows, "No rows found for "+message.toString()); 
		} else if ( expectedResults == expectZeroRows) {
		    if (foundRows) {
			message.append("Found a row when not expected \n");
                        int columns = rs.getMetaData().getColumnCount(); 
			for (int i  = 1; i <= columns; i++) {
			    message.append(rs.getString(i));
			    if (i < columns) message.append(","); 
			}
			message.append("\n"); 
		    }
		    rs.close(); 
		    assertCondition(!foundRows, "Rows found for "+message.toString()); 
		} else {
		    failed("expectedResults '"+expectedResults+"' not valid"); 
		}
		


	    } catch (Exception e ) {
		System.out.println("******* Exception Caught ******\n"); 
		failed(e, message.toString()); 
	    }
	} 

    }

    /**
     * Valid ENTRY is  connectionCatalog_, JDDMDTest.COLLECTION, "JDMDSMALLINT", "P1"
     * Testing all combinations of catalog, schemaPattern, functionNamePattern, columnNamePattern
       catalog -- NULL                -- return rows 
               -- ""                  -- return 0 rows
               -- connectionCatalog_  -- return rows ,
               -- "%"                 -- return 0 rows
     * schemaPattern -- NULL                         -- return rows
     *               -- ""                           -- return 0 rows
     *               -- JDDMDTest.COLLECTION         -- return rows
     *               -- JDDMDTest.SCHEMAS_PERCENT    -- return rows
     *               -- JDDMDTest.SCHEAMS_UNDERSCORE -- return rows
     *               -- "%X"                         -- return 0 rows 
     * functionNamePattern -- NULL                   -- return rows
     *                     -- ""                     -- return 0 rows
     *                     -- "JDDMDGFSI"         -- return 1 rows
     *                     -- "JDDMD_FSI"         -- return 1 rows
     *                     -- "JDDMD%FSI"         -- return 1 rows
     *                     -- "%X23531%"             -- return 0 rows
     *
     */

    /* Testing functionNamePattern only */ 
    public void Var016() { testPattern(null, null, null,         expectRows); }
    public void Var017() { testPattern(null, null, "",           expectZeroRows); }
    public void Var018() { testPattern(null, null, "JDDMDGFSI",  expectRows); }
    public void Var019() { testPattern(null, null, "JDDMD%FSI",  expectRows); }
    public void Var020() { testPattern(null, null, "JDDMD_FSI",  expectRows); }
    public void Var021() { testPattern(null, null, "JDDMDXFSI",  expectZeroRows); }

    /* schemaPattern variations */

    public void Var022() { testPattern(null, "", null,         expectZeroRows); }
    public void Var023() { testPattern(null, "", "",           expectZeroRows); }
    public void Var024() { testPattern(null, "", "JDDMDGFSI",  expectZeroRows); }
    public void Var025() { testPattern(null, "", "JDDMD%FSI",  expectZeroRows); }
    public void Var026() { testPattern(null, "", "JDDMD_FSI",  expectZeroRows); }
    public void Var027() { testPattern(null, "", "JDDMDXFSI",  expectZeroRows); }

    public void Var028() { testPattern(null, JDDMDTest.COLLECTION, null,         expectRows); }
    public void Var029() { testPattern(null, JDDMDTest.COLLECTION, "",           expectZeroRows); }
    public void Var030() { testPattern(null, JDDMDTest.COLLECTION, "JDDMDGFSI",  expectRows); }
    public void Var031() { testPattern(null, JDDMDTest.COLLECTION, "JDDMD%FSI",  expectRows); }
    public void Var032() { testPattern(null, JDDMDTest.COLLECTION, "JDDMD_FSI",  expectRows); }
    public void Var033() { testPattern(null, JDDMDTest.COLLECTION, "JDDMDXFSI",  expectZeroRows); }

    public void Var034() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT, null,         expectRows); }
    public void Var035() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT, "",           expectZeroRows); }
    public void Var036() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT, "JDDMDGFSI",  expectRows); }
    public void Var037() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT, "JDDMD%FSI",  expectRows); }
    public void Var038() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT, "JDDMD_FSI",  expectRows); }
    public void Var039() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT, "JDDMDXFSI",  expectZeroRows); }

    public void Var040() { testPattern(null, underscore, null,         expectRows); }
    public void Var041() { testPattern(null, underscore, "",           expectZeroRows); }
    public void Var042() { testPattern(null, underscore, "JDDMDGFSI",  expectRows); }
    public void Var043() { testPattern(null, underscore, "JDDMD%FSI",  expectRows); }
    public void Var044() { testPattern(null, underscore, "JDDMD_FSI",  expectRows); }
    public void Var045() { testPattern(null, underscore, "JDDMDXFSI",  expectZeroRows); }


    public void Var046() { testPattern(null, "%YXZ", null,         expectZeroRows); }
    public void Var047() { testPattern(null, "%YXZ", "",           expectZeroRows); }
    public void Var048() { testPattern(null, "%Z", "JDDMDGFSI",  expectZeroRows); }
    public void Var049() { testPattern(null, "%Z", "JDDMD%FSI",  expectZeroRows); }
    public void Var050() { testPattern(null, "%Z", "JDDMD_FSI",  expectZeroRows); }
    public void Var051() { testPattern(null, "%Z", "JDDMDXFSI",  expectZeroRows); }


    /* catalog variations */


    public void Var052() { testPattern(connectionCatalog_, null, null,         expectRows); }
    public void Var053() { testPattern(connectionCatalog_, null, "",           expectZeroRows); }
    public void Var054() { testPattern(connectionCatalog_, null, "JDDMDGFSI",  expectRows); }
    public void Var055() { testPattern(connectionCatalog_, null, "JDDMD%FSI",  expectRows); }
    public void Var056() { testPattern(connectionCatalog_, null, "JDDMD_FSI",  expectRows); }
    public void Var057() { testPattern(connectionCatalog_, null, "JDDMDXFSI",  expectZeroRows); }
    public void Var058() { testPattern(connectionCatalog_, "", null,         expectZeroRows); }
    public void Var059() { testPattern(connectionCatalog_, "", "",           expectZeroRows); }
    public void Var060() { testPattern(connectionCatalog_, "", "JDDMDGFSI",  expectZeroRows); }
    public void Var061() { testPattern(connectionCatalog_, "", "JDDMD%FSI",  expectZeroRows); }
    public void Var062() { testPattern(connectionCatalog_, "", "JDDMD_FSI",  expectZeroRows); }
    public void Var063() { testPattern(connectionCatalog_, "", "JDDMDXFSI",  expectZeroRows); }
    public void Var064() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, null,         expectRows); }
    public void Var065() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, "",           expectZeroRows); }
    public void Var066() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, "JDDMDGFSI",  expectRows); }
    public void Var067() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, "JDDMD%FSI",  expectRows); }
    public void Var068() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, "JDDMD_FSI",  expectRows); }
    public void Var069() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, "JDDMDXFSI",  expectZeroRows); }
    public void Var070() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT, null,         expectRows); }
    public void Var071() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT, "",           expectZeroRows); }
    public void Var072() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT, "JDDMDGFSI",  expectRows); }
    public void Var073() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT, "JDDMD%FSI",  expectRows); }
    public void Var074() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT, "JDDMD_FSI",  expectRows); }
    public void Var075() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT, "JDDMDXFSI",  expectZeroRows); }
    public void Var076() { testPattern(connectionCatalog_, underscore, null,         expectRows); }
    public void Var077() { testPattern(connectionCatalog_, underscore, "",           expectZeroRows); }
    public void Var078() { testPattern(connectionCatalog_, underscore, "JDDMDGFSI",  expectRows); }
    public void Var079() { testPattern(connectionCatalog_, underscore, "JDDMD%FSI",  expectRows); }
    public void Var080() { testPattern(connectionCatalog_, underscore, "JDDMD_FSI",  expectRows); }
    public void Var081() { testPattern(connectionCatalog_, underscore, "JDDMDXFSI",  expectZeroRows); }
    public void Var082() { testPattern(connectionCatalog_, "%ZZX", null,         expectZeroRows); }
    public void Var083() { testPattern(connectionCatalog_, "%ZZX", "",           expectZeroRows); }
    public void Var084() { testPattern(connectionCatalog_, "%Z", "JDDMDGFSI",  expectZeroRows); }
    public void Var085() { testPattern(connectionCatalog_, "%Z", "JDDMD%FSI",  expectZeroRows); }
    public void Var086() { testPattern(connectionCatalog_, "%Z", "JDDMD_FSI",  expectZeroRows); }
    public void Var087() { testPattern(connectionCatalog_, "%Z", "JDDMDXFSI",  expectZeroRows); }


    //
    // On LUW, SQLProcedures treats catalog of "" as null
    // 
    public void Var088() { testPattern("", null, null,         expectRows); }
    public void Var089() { testPattern("", null, "",           expectZeroRows); }
    public void Var090() { testPattern("", null, "JDDMDGFSI",  expectRows); }
    public void Var091() { testPattern("", null, "JDDMD%FSI",  expectRows); }
    public void Var092() { testPattern("", null, "JDDMD_FSI",  expectRows); }
    public void Var093() { testPattern("", null, "JDDMDXFSI",  expectZeroRows); }
    public void Var094() { testPattern("", "", null,         expectZeroRows); }
    public void Var095() { testPattern("", "", "",           expectZeroRows); }
    public void Var096() { testPattern("", "", "JDDMDGFSI",  expectZeroRows); }
    public void Var097() { testPattern("", "", "JDDMD%FSI",  expectZeroRows); }
    public void Var098() { testPattern("", "", "JDDMD_FSI",  expectZeroRows); }
    public void Var099() { testPattern("", "", "JDDMDXFSI",  expectZeroRows); }
    public void Var100() { testPattern("", JDDMDTest.COLLECTION, null,         expectRows); }
    public void Var101() { testPattern("", JDDMDTest.COLLECTION, "",           expectZeroRows); }
    public void Var102() { testPattern("", JDDMDTest.COLLECTION, "JDDMDGFSI",  expectRows); }
    public void Var103() { testPattern("", JDDMDTest.COLLECTION, "JDDMD%FSI",  expectRows); }
    public void Var104() { testPattern("", JDDMDTest.COLLECTION, "JDDMD_FSI",  expectRows); }
    public void Var105() { testPattern("", JDDMDTest.COLLECTION, "JDDMDXFSI",  expectZeroRows); }
    public void Var106() { testPattern("", JDDMDTest.SCHEMAS_PERCENT, null,         expectRows); }
    public void Var107() { testPattern("", JDDMDTest.SCHEMAS_PERCENT, "",           expectZeroRows); }
    public void Var108() { testPattern("", JDDMDTest.SCHEMAS_PERCENT, "JDDMDGFSI",  expectRows); }
    public void Var109() { testPattern("", JDDMDTest.SCHEMAS_PERCENT, "JDDMD%FSI",  expectRows); }
    public void Var110() { testPattern("", JDDMDTest.SCHEMAS_PERCENT, "JDDMD_FSI",  expectRows); }
    public void Var111() { testPattern("", JDDMDTest.SCHEMAS_PERCENT, "JDDMDXFSI",  expectZeroRows); }
    public void Var112() { testPattern("", underscore, null,         expectRows); }
    public void Var113() { testPattern("", underscore, "",           expectZeroRows); }
    public void Var114() { testPattern("", underscore, "JDDMDGFSI",  expectRows); }
    public void Var115() { testPattern("", underscore, "JDDMD%FSI",  expectRows); }
    public void Var116() { testPattern("", underscore, "JDDMD_FSI",  expectRows); }
    public void Var117() { testPattern("", underscore, "JDDMDXFSI",  expectZeroRows); }
    public void Var118() { testPattern("", "%ZZX", null,         expectZeroRows); }
    public void Var119() { testPattern("", "%X", "",           expectZeroRows); }
    public void Var120() { testPattern("", "%Y", "JDDMDGFSI",  expectZeroRows); }
    public void Var121() { testPattern("", "%Y", "JDDMD%FSI",  expectZeroRows); }
    public void Var122() { testPattern("", "%Y", "JDDMD_FSI",  expectZeroRows); }
    public void Var123() { testPattern("", "%Y", "JDDMDXFSI",  expectZeroRows); }



 
    public void Var124() { testPattern("%", null, null,         expectRows); }
    public void Var125() { testPattern("%", null, "",           expectZeroRows); }
    public void Var126() { testPattern("%", null, "JDDMDGFSI",  expectRows); }
    public void Var127() { testPattern("%", null, "JDDMD%FSI",  expectRows); }
    public void Var128() { testPattern("%", null, "JDDMD_FSI",  expectRows); }
    public void Var129() { testPattern("%", null, "JDDMDXFSI",  expectZeroRows); }
    public void Var130() { testPattern("%", "", null,         expectZeroRows); }
    public void Var131() { testPattern("%", "", "",           expectZeroRows); }
    public void Var132() { testPattern("%", "", "JDDMDGFSI",  expectZeroRows); }
    public void Var133() { testPattern("%", "", "JDDMD%FSI",  expectZeroRows); }
    public void Var134() { testPattern("%", "", "JDDMD_FSI",  expectZeroRows); }
    public void Var135() { testPattern("%", "", "JDDMDXFSI",  expectZeroRows); }
    public void Var136() { testPattern("%", JDDMDTest.COLLECTION, null,         expectRows); }
    public void Var137() { testPattern("%", JDDMDTest.COLLECTION, "",           expectZeroRows); }
    public void Var138() { testPattern("%", JDDMDTest.COLLECTION, "JDDMDGFSI",  expectRows); }
    public void Var139() { testPattern("%", JDDMDTest.COLLECTION, "JDDMD%FSI",  expectRows); }
    public void Var140() { testPattern("%", JDDMDTest.COLLECTION, "JDDMD_FSI",  expectRows); }
    public void Var141() { testPattern("%", JDDMDTest.COLLECTION, "JDDMDXFSI",  expectZeroRows); }
    public void Var142() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT, null,         expectRows); }
    public void Var143() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT, "",           expectZeroRows); }
    public void Var144() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT, "JDDMDGFSI",  expectRows); }
    public void Var145() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT, "JDDMD%FSI",  expectRows); }
    public void Var146() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT, "JDDMD_FSI",  expectRows); }
    public void Var147() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT, "JDDMDXFSI",  expectZeroRows); }
    public void Var148() { testPattern("%", underscore, null,         expectRows); }
    public void Var149() { testPattern("%", underscore, "",           expectZeroRows); }
    public void Var150() { testPattern("%", underscore, "JDDMDGFSI",  expectRows); }
    public void Var151() { testPattern("%", underscore, "JDDMD%FSI",  expectRows); }
    public void Var152() { testPattern("%", underscore, "JDDMD_FSI",  expectRows); }
    public void Var153() { testPattern("%", underscore, "JDDMDXFSI",  expectZeroRows); }
    public void Var154() { testPattern("%", "%ZZX", null,         expectZeroRows); }
    public void Var155() { testPattern("%", "%X", "",           expectZeroRows); }
    public void Var156() { testPattern("%", "%Z", "JDDMDGFSI",  expectZeroRows); }
    public void Var157() { testPattern("%", "%Z", "JDDMD%FSI",  expectZeroRows); }
    public void Var158() { testPattern("%", "%Z", "JDDMD_FSI",  expectZeroRows); }
    public void Var159() { testPattern("%", "%Z", "JDDMDXFSI",  expectZeroRows); }


    /* Test READONLY connection */

    public void Var160() {

        if ((getDriver() == JDTestDriver.DRIVER_TOOLBOX) || checkJdbc40()) {
            
        
        if(getRelease() < JDTestDriver.RELEASE_V7R1M0)  {
            notApplicable("V5R5 variation");
            return;
        }


        message.setLength(0); 
        try {
	    Connection c = testDriver_.getConnection (baseURL_
					       + ";access=read only", userId_, encryptedPassword_);
	    DatabaseMetaData dmd = c.getMetaData(); 


            ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSS(dmd, "getFunctions", 
                null, JDDMDTest.COLLECTION, "JDDMDGFSI"); 
            boolean success = true;
            
            int rows = 0;
            while (rs.next()) {
                ++rows;
                success = JDDMDTest.check(rs.getString("FUNCTION_CAT"), connectionCatalog_, "FUNCTION_CAT", "NULL", message, success) ;
                success = JDDMDTest.check(rs.getString("FUNCTION_SCHEM"), JDDMDTest.COLLECTION, "FUNCTION_SCHEMA", JDDMDTest.COLLECTION, message, success); 
                success = JDDMDTest.check(rs.getString("FUNCTION_NAME"), "JDDMDGFSI", "FUNCTION_NAME", "NULL", message, success); 
            }
            
            rs.close();
	    c.close(); 
            if (rows != 1)
                message.append("rows = " + rows + " sb 1 ");
            assertCondition((rows == 1) && success, "\n"+message.toString() );
        } catch (Exception e) {
            failed(e, "Unexpected Exception");
        }
    }
  }


    



}



