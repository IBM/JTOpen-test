///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSMDGetColumnName.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////
//
//
//
//
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDRSMDGetColumnName.java
//
// Classes:      JDRSMDGetColumnName
//
////////////////////////////////////////////////////////////////////////

package test.JD.RSMD;

import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDRSMDTest;
import test.JDTestDriver;
import test.JDTestcase;
import test.JD.JDSetupPackage;



/**
Testcase JDRSMDGetColumnName.  This tests the following method
of the JDBC ResultSetMetaData class:

<ul>
<li>getColumnName()
</ul>
**/
public class JDRSMDGetColumnName
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSMDGetColumnName";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSMDTest.main(newArgs); 
   }



    private static final String PACKAGE             = "JDRSMDGCN";
    private static final String PACKAGE_CACHE_NO    = "extended dynamic=false";
    private static String PACKAGE_CACHE_YES   = "extended dynamic=true;package="
                                                    + PACKAGE + ";package library="
                                                    + JDRSMDTest.COLLECTION
                                                    + ";package cache=true";
    private static String TABLE               = JDRSMDTest.COLLECTION
                                                    + ".JDRSMDGCN";
    private static String TABLELCN            = JDRSMDTest.COLLECTION
                                                    + ".JDRSMDGCNLCN";  //@C1A



    // Private data.
    private String properties_      = "";
    private Statement statement_    = null;
    private ResultSet rs_           = null;
    private ResultSetMetaData rsmd_ = null;




/**
Constructor.
**/
    public JDRSMDGetColumnName (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSMDGetColumnName",
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
        PACKAGE_CACHE_YES   = "extended dynamic=true;package="
          + PACKAGE + ";package library="
          + JDRSMDTest.COLLECTION
          + ";package cache=true";
        TABLE               = JDRSMDTest.COLLECTION + ".JDRSMDGCN";
        TABLELCN            = JDRSMDTest.COLLECTION + ".JDRSMDGCNLCN";  //@C1A

        // SQL400 - driver neutral...
        String url = baseURL_
        // String url = "jdbc:as400://" + systemObject_.getSystemName()
            
            ;
        connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
        statement_ = connection_.createStatement ();
        initTable(statement_, TABLE, " (C INTEGER, COL2 INTEGER)");

        initTable(statement_,TABLELCN, " (THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST INTEGER)");

        if (isToolboxDriver())
        {
            JDSetupPackage.prime (systemObject_, PACKAGE,
                JDRSMDTest.COLLECTION, "SELECT * FROM " + TABLE);
        }
        else
        {
            JDSetupPackage.prime (systemObject_, encryptedPassword_, PACKAGE,
                JDRSMDTest.COLLECTION, "SELECT * FROM " + TABLE, "",
                getDriver());
        }

        reconnect (PACKAGE_CACHE_NO);
    }



/**
Reconnects with different properties, if needed.

@exception Exception If an exception occurs.
**/
    private void reconnect (String properties)
        throws Exception
    {
        if (! properties_.equals (properties)) {
            properties_ = properties;
            if (connection_ != null) {
                if (rs_ != null)
                    rs_.close ();
                statement_.close ();
                connection_.close ();
            }

            // SQL400 - driver neutral...
            String url = baseURL_
            // String url = "jdbc:as400://" + systemObject_.getSystemName()
                
                ;
            connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
            statement_ = connection_.createStatement ();
            rs_ = statement_.executeQuery ("SELECT * FROM " + TABLE);
            rsmd_ = rs_.getMetaData ();
        }
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
        rs_.close ();

        cleanupTable(statement_, TABLE);
        cleanupTable(statement_, TABLELCN);
        

        statement_.close ();
        connection_.close ();
    }



/**
getColumnName() - Check column -1.  Should throw an exception.
(Package cache = false)
**/
    public void Var001()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            rsmd_.getColumnName (-1);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getColumnName() - Check column 0.  Should throw an exception.
(Package cache = false)
**/
    public void Var002()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            rsmd_.getColumnName (0);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getColumnName() - Check a column greater than the max.
Should throw an exception. (Package cache = false)
**/
    public void Var003()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            rsmd_.getColumnName (3);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getColumnName() - Check a column with a 1 character name.
(Package cache = false)
**/
    public void Var004()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            String s = rsmd_.getColumnName (1);
            assertCondition (s.equals ("C"));
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnName() - Check a column with a multiple character name.
(Package cache = false)
**/
    public void Var005()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            String s = rsmd_.getColumnName (2);
            assertCondition (s.equals ("COL2"));
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnName() - Check column -1.  Should throw an exception.
(Package cache = true)
**/
    public void Var006()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            rsmd_.getColumnName (-1);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getColumnName() - Check column 0.  Should throw an exception.
(Package cache = true)
**/
    public void Var007()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            rsmd_.getColumnName (0);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getColumnName() - Check a column greater than the max.
Should throw an exception. (Package cache = true)
**/
    public void Var008()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            rsmd_.getColumnName (3);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getColumnName() - Check a column with a 1 character name.
(Package cache = true)
**/
    public void Var009()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            String s = rsmd_.getColumnName (1);
            assertCondition (s.equals ("C"));
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnName() - Check a column with a multiple character name.
(Package cache = true)
**/
    public void Var010()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            String s = rsmd_.getColumnName (2);
            assertCondition (s.equals ("COL2"));
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
getColumnName() - Check when the result set is closed.
**/
    public void Var011()
    {
        try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM " + TABLE);
            ResultSetMetaData rsmd = rs.getMetaData ();
            rs.close ();
            String v = rsmd.getColumnName (2);
            s.close();
            assertCondition (v.equals ("COL2"));
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
getColumnName() - Check when the meta data comes from prepared statement.
**/
    public void Var012()
    {
        if (checkJdbc20 ()) {
            try {

                PreparedStatement ps;
		try {
		    ps = connection_.prepareStatement ("SELECT * FROM " + TABLE,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
		} catch (SQLException sqlex) {
		    ps = connection_.prepareStatement ("SELECT * FROM " + TABLE);
		}
                ResultSetMetaData rsmd = ps.getMetaData ();
                String v = rsmd.getColumnName (2);
                ps.close ();
                assertCondition (v.equals ("COL2"));
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


//@C1A
/**
getColumnName() - Check to see if you can get a 128 byte column name.
**/
    public void Var013()
    {
        if(getRelease() >= JDTestDriver.RELEASE_V7R1M0){
            if(checkJdbc20()){
                try{
                    Statement s = connection_.createStatement();
                    ResultSet rs = s.executeQuery("SELECT * FROM " + TABLELCN);
                    ResultSetMetaData rsmd = rs.getMetaData();
                    String columnName = rsmd.getColumnName(1);
                    rs.close();
                    s.close();
                    assertCondition(columnName.equals("THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST"), "Added by Toolbox 8/12/2004 to test 128 byte column names."+
				    "\nGot      : "+columnName+
				    "\nExpected : THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST");
                }
                catch(Exception e){
                    failed(e, "Unexpected Exception.  Added by Toolbox 8/12/2004 to test 128 byte column names.");
                }
            }
        }
        else
            notApplicable("V5R4 or greater variation.");
    }
    
    
    
    /**
    getColumnName() - Test delimited names with leading whitespace.
    **/
        public void Var014()       {
          String[][] tests = {
              {
                "select 1 as \" leading space\" from sysibm.sysdummy1",
                " leading space",
              },
              {
                "select 1 as \" a    \" from sysibm.sysdummy1",
                " a",
              },
              {
                "select 1 as \" leading and trailing space      \" from sysibm.sysdummy1",
                " leading and trailing space",
              },
              {
                "select 1 as \" leading space and trailing tab \t      \" from sysibm.sysdummy1",
                " leading space and trailing tab \t",
              },
              {
                "select 1 as \" leading space and trailing tab \t      \" from sysibm.sysdummy1",
                " leading space and trailing tab \t",
              },
              {
                "select 1 as \" leading space and trailing linefeed \n      \" from sysibm.sysdummy1",
                " leading space and trailing linefeed \n",
              },
              {
                "select 1 as \" leading space and trailing vertical tab \u000b      \" from sysibm.sysdummy1",
                " leading space and trailing vertical tab \u000b",
              },
              {
                "select 1 as \" leading space and trailing formfeed \f      \" from sysibm.sysdummy1",
                " leading space and trailing formfeed \f",
              },
              {
                "select 1 as \" leading space and trailing CR \r      \" from sysibm.sysdummy1",
                " leading space and trailing CR \r",
              },
              {
                "select 1 as \"trailing space      \" from sysibm.sysdummy1",
                "trailing space",
              },
              {
                "select 1 as \"trailing tab \t      \" from sysibm.sysdummy1",
                "trailing tab \t",
              },
              {
                "select 1 as \"trailing tab \t      \" from sysibm.sysdummy1",
                "trailing tab \t",
              },
              {
                "select 1 as \"trailing linefeed \n      \" from sysibm.sysdummy1",
                "trailing linefeed \n",
              },
              {
                "select 1 as \"trailing vertical tab \u000b      \" from sysibm.sysdummy1",
                "trailing vertical tab \u000b",
              },
              {
                "select 1 as \"trailing formfeed \f      \" from sysibm.sysdummy1",
                "trailing formfeed \f",
              },
              {
                "select 1 as \"trailing CR \r      \" from sysibm.sysdummy1",
                "trailing CR \r",
              },
              
          }; 
          StringBuffer sb = new StringBuffer("Test added 6/26/2015 to test trimming of column names\n"); 
          try{
            Statement s = connection_.createStatement();
            boolean passed = true; 
            for (int i = 0; i < tests.length; i++) {
              String[] thisTest = tests[i]; 
              sb.append("executing "+thisTest[0]+"\n" ); 
              ResultSet rs = s.executeQuery(thisTest[0]);
              ResultSetMetaData rsmd = rs.getMetaData();
              for (int j = 1; j < thisTest.length; j++) { 
                  if (!(rsmd.getColumnName(j).equals(thisTest[j]))) {
                    sb.append("  ERROR got '"+rsmd.getColumnName(j)+"' expected '"+thisTest[j]+"'\n");
                    passed = false; 
                  }
              }
              rs.close();
              
            }
            s.close();
            assertCondition(passed,sb); 
          }
          catch(Exception e){
            failed(e, "Unexpected Exception. "+sb.toString()); 
          }
        }
        
        
    
    
    
    
    
}
