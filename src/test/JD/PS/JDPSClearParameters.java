///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSClearParameters.java
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
 // File Name:    JDPSClearParameters.java
 //
 // Classes:      JDPSClearParameters
 //
 ////////////////////////////////////////////////////////////////////////
 //
 //
 //
 //
 ////////////////////////////////////////////////////////////////////////

package test.JD.PS;

import com.ibm.as400.access.AS400;

import test.JDPSTest;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDPSClearParameters.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>clearParameters()
</ul>
**/
public class JDPSClearParameters
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDPSClearParameters";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDPSTest.main(newArgs); 
   }



    // Private data.
    private Connection          connection_;
    private Statement           statement_;



/**
Constructor.
**/
    public JDPSClearParameters (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDPSClearParameters",
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
        statement_ = connection_.createStatement ();
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
        statement_.close ();
        connection_.close ();
    }



/**
clearParameters() - Verify that setting a value holds through 
repeated executes.
**/
  public void Var001()
  {                 
      try {
          statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

          PreparedStatement ps = connection_.prepareStatement ("INSERT INTO " 
            + JDPSTest.PSTEST_SET + " (C_KEY) VALUES (?)");
          ps.setString (1, "Hola");
          ps.executeUpdate ();
          ps.executeUpdate ();
          ps.close ();
          ResultSet rs = statement_.executeQuery ("SELECT C_KEY FROM " 
                                                  + JDPSTest.PSTEST_SET
                                                  + " WHERE C_KEY='Hola'");
          int count = 0;
          while (rs.next ())
              ++count;
          rs.close ();
          assertCondition (count == 2);
      }
      catch (Exception e) {
          failed (e, "Unexpected Exception");
      }
  }



/**
clearParameters() - Verify that executing after a clear
throws an exception.
**/
  public void Var002()
  {                 
      try {
          statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

          PreparedStatement ps = connection_.prepareStatement ("INSERT INTO " 
            + JDPSTest.PSTEST_SET + " (C_KEY) VALUES (?)");
          ps.setString (1, "Hola");
          ps.executeUpdate ();
          ps.clearParameters ();
          ps.executeUpdate ();
          ps.close ();
          failed ("Didn't throw SQLException");
      }
      catch (Exception e) {
          assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
  }



/**
clearParameters() - Verify that this has no effect on a statement
with no parameters.
**/
  public void Var003()
  {                 
      try {
          statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

          PreparedStatement ps = connection_.prepareStatement ("INSERT INTO " 
            + JDPSTest.PSTEST_SET + " (C_KEY) VALUES ('Adios')");
          ps.clearParameters ();
          ps.executeUpdate ();
          ps.close ();

          ResultSet rs = statement_.executeQuery ("SELECT C_KEY FROM " 
                                                  + JDPSTest.PSTEST_SET
                                                  + " WHERE C_KEY='Adios'");
          int count = 0;
          while (rs.next ())
              ++count;
          rs.close ();
          assertCondition (count == 1);
      }
      catch (Exception e) {
          failed (e, "Unexpected Exception");
      }
  }



/**
clearParameters() - Should throw an exception when the statement
is closed.
**/
  public void Var004()
  {                 
      try {
          PreparedStatement ps = connection_.prepareStatement ("INSERT INTO " 
            + JDPSTest.PSTEST_SET + " (C_KEY) VALUES (?)");
          ps.setString (1, "Hola");
          ps.close ();
          ps.clearParameters ();
          failed ("Didn't throw SQLException");
      }
      catch (Exception e) {
          assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
  }



/**
clearParameters()  -- When batching, using clearPameters before executeBatch should not cause the execute
to fail.  Reported for toolbox via CPS 8KLGCZ Aug 2011
**/


  public void Var005()
  {
      String added = " -- added Aug 2011 for toolbox CPS 8KLGCZ"; 
      try {
	  String value = "JDPSClearP"; 
	  Statement stmt = connection_.createStatement(); 
          PreparedStatement ps = connection_.prepareStatement ("INSERT INTO " 
            + JDPSTest.PSTEST_SET + " (C_KEY) VALUES (?)");
          ps.setString (1, value);
	  ps.addBatch();
          ps.clearParameters ();
	  ps.executeBatch();
	  ps.close();
	  ps = connection_.prepareStatement("DELETE FROM "+JDPSTest.PSTEST_SET+" WHERE C_KEY=?");
          ps.setString (1, value);
	  ps.addBatch();
          ps.clearParameters ();
	  ps.executeBatch();
	  ps.close();

	  assertCondition(true); 

      }
      catch (Exception e) {
          failed (e, "Unexpected Exception"+added);
      }
  }


/**
clearParameters()  -- When batching, using clearPameters before executeBatch should not cause the execute
to fail.  Reported for toolbox via CPS 8KLGCZ Aug 2011.  This is the case where there are more than 32000
rows 
**/


  public void Var006()
  {
      String added = " -- added Aug 2011 for toolbox CPS 8KLGCZ"; 
      try {
	  String value = "JDPSClearP"; 
	  Statement stmt = connection_.createStatement(); 
          PreparedStatement ps = connection_.prepareStatement ("INSERT INTO " 
            + JDPSTest.PSTEST_SET + " (C_KEY) VALUES (?)");
	  for (int i = 0; i < 40000; i++) { 
	      ps.setString (1, value);
	      ps.addBatch();
	  }
          ps.clearParameters ();
	  ps.executeBatch();
	  ps.close();
	  ps = connection_.prepareStatement("DELETE FROM "+JDPSTest.PSTEST_SET+" WHERE C_KEY=?");

          ps.setString (1, value);
	  ps.addBatch();
          ps.clearParameters ();
	  ps.executeBatch();
	  ps.close();

	  assertCondition(true); 

      }
      catch (Exception e) {
          failed (e, "Unexpected Exception"+added);
      }
  }






}




