///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.RS;

import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDRSTest;
import test.JDTestDriver;
import test.JDTestcase;



/**
Testcase JDRSGetInt.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>getInt
</ul>
**/
public class JDRSTestcase
extends JDTestcase
{


    // Private data.
    Statement           statement_;
    Statement           statement0_;
    ResultSet           rs_;


/**
Constructor.
**/
    public JDRSTestcase (AS400 systemObject,
		       String testname, 
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, testname,
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
        // SQL400 - driver neutral...
        String url = baseURL_
        // String url = "jdbc:as400://" + systemObject_.getSystemName()
            
            ;
        connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
    try {
      connection_.setAutoCommit(false); // @E1A
    } catch (Exception e) {
      output_.println("Warning:  setAutCommit(false) failed");
      output_.flush();
      System.err.flush();
      e.printStackTrace();
      output_.flush();
      System.err.flush();
    }
        statement0_ = connection_.createStatement ();
        if (isJdbc20 ()) {
	    if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
	    	statement_ = connection_.createStatement ();
	    } else { 
	    	statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
							  ResultSet.CONCUR_UPDATABLE);
	    }
            statement_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_GET
                + " (C_KEY) VALUES ('DUMMY_ROW')");
	    // Note:  LUW fails on this query because...
	    // 53 A column with a LONG VARCHAR, LONG VARGRAPHIC, DATALINK, LOB, XML type, distinct type on any of these types, or structured type cannot be specified in the select-list of a scrollable cursor. 

            rs_ = statement_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET + " FOR UPDATE");
        }
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
      try {
        if (isJdbc20 ()) {
          if (rs_ != null) { 
            rs_.close ();
          }
          statement_.close ();
        }
        statement0_.close ();
        
        if (connection_ != null) connection_.commit(); // @E1A
        
        
      } catch (Exception e) { 
        output_.println("Warning:  Exception in JDRSTestcase.cleanup"); 
        e.printStackTrace(output_) ;
      }
        if (connection_ != null) connection_.close ();
	connection_ = null; 
    }




    /** 
     *  Run a Dfp test
     *  
     * @param table               -- name of table to query
     * @param expectedArray       -- array of expected results 
     * @param statement0          -- statement to use to run test 
     * @param methodName          -- name of method to call 
     */
    public void testDfp(String table, String[] expectedArray, Statement statement0, String methodName) {
      if (checkDecFloatSupport ()) {
        
        boolean success = true; 
        StringBuffer sb = new StringBuffer();
        
        try {
          ResultSet rs = statement0.executeQuery ("SELECT * FROM "
              + table);
          int i = 0; 
          while (rs.next()) {
            String retrieved = "" ;
            try { 
              if (methodName.equals("getInt")) { 
                retrieved=""+rs.getInt (1);
              } else if (methodName.equals("getShort")) {
                retrieved=""+rs.getShort (1);
              } else if (methodName.equals("getLong")) {
                retrieved=""+rs.getLong (1);
              } else if (methodName.equals("getDouble")) {
                retrieved=""+rs.getDouble (1);
              } else {
                retrieved="Error in testDfp: Method "+methodName+" not supported"; 
              }
            } catch (Exception e) { 
              retrieved=e.toString(); 
            }
            String expected = expectedArray[i];
            if (expected == null) {
              if ("null".equals(retrieved)) {
                success = false;
                sb.append("\nexpected "+expected+" but got "+retrieved+" for "+rs.getString(1)); 
              } 
            } else {
              if (expected.startsWith("CONTAINS:")) {
                if (retrieved == null) { 
                  sb.append("\nexpected "+expected+" but got "+retrieved+" for "+rs.getString(1));
                  success = false;
                } else {
                  String rest = expected.substring(9);
                  String check = rest;
                  int barIndex = rest.indexOf('|');
                  if (barIndex > 0) {
                    check = rest.substring(0,barIndex); 
                    rest = rest.substring(barIndex+1); 
                  } else {
                    rest = null; 
                  }
                  boolean found = false; 
                  while (check !=null  && !found) {
                    if (retrieved.indexOf(check) >= 0) {
                      found = true; 
                    } else {
                      sb.append("\nexpected '"+check+"' but got "+retrieved+" for "+rs.getString(1));
                    } 
                      if (rest != null) {
                      barIndex = rest.indexOf('|');
                      if (barIndex > 0) {
                        check = rest.substring(0,barIndex); 
                        rest = rest.substring(barIndex+1); 
                      } else {
                        check = rest;
                        rest = null; 
                      }
                    } else {
                      check =null; 
                    }

                  }
                  if (!found) {
                    success = false; 
                  }
                }       
                
              } else { 
                if (expected.equals(""+retrieved)) {
                } else {
                  sb.append("\nexpected "+expected+" but got "+retrieved+" for "+rs.getString(1));
                  success = false;
                } 
              }
            } 
            i++; 
          }
          assertCondition (success, "Error -- "+ sb.toString() );
        }
        catch (Exception e) {
          failed (e, "Unexpected Exception");
        }
      }
    }


    /*
     *  Testing for truncation using get methods
     */    
    public void baseTruncationTest(String x, String method ) {

            try {
                Statement stmt = connection_.createStatement(); 
                ResultSet rs = stmt.executeQuery ("SELECT "+x+" FROM SYSIBM.SYSDUMMY1");    
                rs.next(); 
              String retrieved= null; 
              if (method.equals("getInt")) { 
                int v = rs.getInt (1);
                retrieved=""+v; 
              } else if (method.equals("getByte")) { 
                retrieved=""+rs.getByte(1); 
              } else {
                retrieved = "ERROR in baseTruncationTest: method "+method+" not suppported ";
              }
                rs.close(); 
                failed ("Didn't throw SQLException but got "+retrieved+ " for "+x);
            }
            catch (SQLException e) {
                // output_.println("Expected exception occurred"); 
                // output_.println("Message : "+ e.getMessage()); 
                // output_.println("SQLState: "+ e.getSQLState());
                // output_.println("SQLCode:  "+ e.getErrorCode()); 
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
            catch (Exception e2) { 
                failed(e2, "Unexpected Exception"); 
            }
  }
    
    
    //
    // Check that the limit threw an exception.
    // This was added for V5R5
    // 
    public void checkLimitBAD(String query, String exceptionType, String method) {
        try {
             String values = ""; 
             ResultSet rs = statement0_.executeQuery (query); 
             while (rs.next()) { 
               if (method.equals("getByte")) { 
                 byte v = rs.getByte (1);
                 values += v+", "; 
               } else {
                 values += "Error in checkLimitBAD: unsupported method "+method; 
               }
             }
             failed ("Didn't throw SQLException values were:" +values+" for "+query );
         }
         catch (Exception e) {
             assertExceptionIsInstanceOf (e, exceptionType);
         } 
    }

    public void checkLimitOK(String query, String expectedValue, String method) { 
      try { 
        ResultSet rs = statement0_.executeQuery (query); 
        rs.next();
        String v; 
        if (method.equals("getByte")) { 
            v = ""+ rs.getByte (1);
        } else {
          v = "Error in checkLimitOK:  unsupported method "+method; 
        }
        assertCondition (v.equals(expectedValue), "Got "+v+" sb "+expectedValue);
      } catch (Exception e) {
        e.printStackTrace(); 
        failed("Unexpected exception for "+query+" method"+method); 
      }
    }


}



