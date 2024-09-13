///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDMDGetFunctionParameters.java
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
 // File Name:    JDDMDGetFunctionParameters.java
 //
 // Classes:      JDDMDGetFunctionParameters
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
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
// import java.sql.DatabaseMetaData;

import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDDMDGetFunctionParameters.  This tests the following methods
of the JDBC DatabaseMetaData class:

<ul>
<li>GetFunctionParameters()
</ul>
**/
public class JDDMDGetFunctionParameters extends JDTestcase
{



    // Private data.
    private Connection          connection_;
    private String              connectionCatalog_; 
    private Connection          closedConnection_;
    // private DatabaseMetaData    dmd_;
    // private DatabaseMetaData    dmd2_;
    String message=""; 

/**
Constructor.
**/
    public JDDMDGetFunctionParameters (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDDMDGetFunctionParameters",
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
        // dmd_ = connection_.getMetaData ();
       
        //create functions
        createFunction(connection_,  JDDMDTest.COLLECTION + ".MDSMALLINT(smallint)", "returns smallint language java parameter style db2general  deterministic not fenced null call no sql external action  external name 'quickudf!q1short'");
          
        //createFunction(globaldbConnection_, "globaldb.q1NFsmallint(x smallint )", "returns smallint language sql deterministic not fenced returns null on null input begin return x; end  ");
    
     

        closedConnection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
        // dmd2_ = closedConnection_.getMetaData ();
        closedConnection_.close ();
        
        

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
                        System.out.println("JDDMDGetFunctionParameters.debug:  warning:  unable to drop function");
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
GetFunctions() - Check the result set format.
**/
    public void Var001()
    {}



}



