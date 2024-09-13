///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSExecute.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

 ////////////////////////////////////////////////////////////////////////
 //
 // File Name:    JDCSExecute.java
 //
 // Classes:      JDCSExecute
 //
 ////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDSetupProcedure;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable;



/**
Testcase JDCSExecute.  This tests the following method
of the JDBC CallableStatement class:

<ul>
<li>executeUpdate()</li>
<li>executeQuery()</li>
<li>execute()</li>
</ul>
**/
public class JDCSExecute
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSExecute";
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
    public JDCSExecute (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDCSExecute",
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
executeUpdate() - Execute when no parameters are registered, and the statement
does not contain any parameter markers.  Should work.
**/
    public void Var001()
    {
        try {
            CallableStatement cs = connection_.prepareCall ("CALL "
                  + JDSetupProcedure.STP_RS0);
            cs.executeUpdate ();
            cs.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
executeUpdate() - Execute when no parameters are registered, and the statement
does contain parameter markers.
Should throw an exception.
**/
    public void Var002()
    {
        try {
            CallableStatement cs = connection_.prepareCall ("CALL "
                  + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
            cs.executeUpdate ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
executeUpdate() - Execute when all but one parameters (an OUT) is registered, 
and the statement
does contain parameter markers.
Should throw an exception.
**/
    public void Var003()
    {
        try {
            CallableStatement cs = connection_.prepareCall ("CALL "
                  + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
            cs.setInt (1, 1);
            cs.setInt (3, 3);
            cs.registerOutParameter (3, Types.INTEGER);
            cs.executeUpdate ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
executeUpdate() - Execute when all but one parameters (an INOUT) is registered, 
and the statement
does contain parameter markers.
Should throw an exception.
**/
    public void Var004()
    {
        try {
            CallableStatement cs = connection_.prepareCall ("CALL "
                  + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
            cs.setInt (1, 1);
            cs.setInt (3, 3);
            cs.registerOutParameter (2, Types.INTEGER);
            cs.executeUpdate ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
executeQuery() - Execute when no parameters are registered, and the statement
does not contain any parameter markers.  Should work.
**/
    public void Var005()
    {
        try {
            CallableStatement cs = connection_.prepareCall ("CALL "
                  + JDSetupProcedure.STP_RS1);
            cs.executeQuery ();
            cs.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
executeQuery() - Execute when no parameters are registered, and the statement
does contain parameter markers.
Should throw an exception.
**/
    public void Var006()
    {
        try {
            CallableStatement cs = connection_.prepareCall ("CALL "
                  + JDSetupProcedure.STP_CSPARMSRS + " (?, ?, ?)");
            cs.executeQuery ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
executeQuery() - Execute when all but one parameters (an OUT) is registered, 
and the statement
does contain parameter markers.
Should throw an exception.
**/
    public void Var007()
    {
        try {
            CallableStatement cs = connection_.prepareCall ("CALL "
                  + JDSetupProcedure.STP_CSPARMSRS + " (?, ?, ?)");
            cs.setInt (1, 1);
            cs.setInt (3, 3);
            cs.registerOutParameter (3, Types.INTEGER);
            cs.executeQuery ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
executeQuery() - Execute when all but one parameters (an INOUT) is registered, 
and the statement
does contain parameter markers.
Should throw an exception.
**/
    public void Var008()
    {
        try {
            CallableStatement cs = connection_.prepareCall ("CALL "
                  + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
            cs.setInt (1, 1);
            cs.setInt (3, 3);
            cs.registerOutParameter (2, Types.INTEGER);
            cs.executeQuery ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
execute() - Execute when no parameters are registered, and the statement
does not contain any parameter markers.  Should work.
**/
    public void Var009()
    {
        try {
            CallableStatement cs = connection_.prepareCall ("CALL "
                  + JDSetupProcedure.STP_RS0);
            cs.execute ();
            cs.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
execute() - Execute when no parameters are registered, and the statement
does contain parameter markers.
Should throw an exception.
**/
    public void Var010()
    {
        try {
            CallableStatement cs = connection_.prepareCall ("CALL "
                  + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
            cs.execute ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
execute() - Execute when all but one parameters (an OUT) is registered, 
and the statement
does contain parameter markers.
Should throw an exception.
**/
    public void Var011()
    {
        try {
            CallableStatement cs = connection_.prepareCall ("CALL "
                  + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
            cs.setInt (1, 1);
            cs.setInt (3, 3);
            cs.registerOutParameter (3, Types.INTEGER);
            cs.execute ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
execute() - Execute when all but one parameters (an INOUT) is registered, 
and the statement
does contain parameter markers.
Should throw an exception.
**/
    public void Var012()
    {
        try {
            CallableStatement cs = connection_.prepareCall ("CALL "
                  + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
            cs.setInt (1, 1);
            cs.setInt (3, 3);
            cs.registerOutParameter (2, Types.INTEGER);
            cs.execute ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) 
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }


/**
executeQuery() - Test new property, property is off so validate existing behavior 
                 (should get an exception if CS.executeQuery does not return a rs)
**/
    public void Var013()
    {
       Statement statement = null;        
       CallableStatement clblStmt = null;
       ResultSet rs = null;              
       Connection connection2 = null;
       
       String procedureName = JDSetupProcedure.COLLECTION + ".EXECUTEQUERY";
       String  proc = "CREATE PROCEDURE " + procedureName + "( "
                      + " IN P1 INTEGER, "
                      + " IN P2 INTEGER) "
                      + "LANGUAGE SQL "
                      + "BEGIN  "
                      + "   RETURN P1 + P2 ; "
                      + "END  ";
             
       
       if (isToolboxDriver())
       {  
          try
          {
             statement = connection_.createStatement();
             try { statement.executeUpdate("drop procedure " + procedureName); } catch (Exception e) {}
             try { statement.executeUpdate(proc); } catch (Exception e) {}
             connection2 = testDriver_.getConnection(baseURL_ + ";errors=full;behavior override=0;", 
                                                   userId_, encryptedPassword_);
             clblStmt = connection2.prepareCall( "{?=CALL " + procedureName + " (?, ?) }" );
          
             clblStmt.setInt(2, 10);
             clblStmt.setInt(3, 20);
             clblStmt.registerOutParameter(1, Types.INTEGER);     
                                
             rs = clblStmt.executeQuery(); 
             failed("did not throw exception");
          }
          catch (SQLException sql) 
          {
             succeeded();
          }
          catch (Exception e) 
          {
             failed (e, "Unexpected Exception");
          }

          if (rs != null) try { rs.close(); } catch (Exception e) {}
          if (clblStmt != null) try { clblStmt.close(); } catch (Exception e) {}
          if (statement != null) 
          {   
             try { statement.executeUpdate("drop procedure " + procedureName); } catch (Exception e) {}
             try { statement.close(); } catch (Exception e) {}
          }
          if (connection2 != null) try { connection2.close(); } catch (Exception e) {}
       }  
       else
          succeeded();
    }



/**
executeQuery() - Test new property, property set so exception suppressed
**/
    public void Var014()
    {
       Statement statement = null;        
       CallableStatement clblStmt = null;
       ResultSet rs = null;              
       Connection connection2 = null;
       
       String procedureName = JDSetupProcedure.COLLECTION + ".EXECUTEQUERY";
       String  proc = "CREATE PROCEDURE " + procedureName + "( "
                      + " IN P1 INTEGER, "
                      + " IN P2 INTEGER) "
                      + "LANGUAGE SQL "
                      + "BEGIN  "
                      + "   RETURN P1 + P2 ; "
                      + "END  ";
             
       
       if (isToolboxDriver())
       {  
          try
          {
             statement = connection_.createStatement();
             try { statement.executeUpdate("drop procedure " + procedureName); } catch (Exception e) {}
             try { statement.executeUpdate(proc); } catch (Exception e) {}
             connection2 = testDriver_.getConnection(baseURL_ + ";errors=full;behavior override=1;", 
                                                   userId_, encryptedPassword_);
             clblStmt = connection2.prepareCall( "{?=CALL " + procedureName + " (?, ?) }" );
          
             clblStmt.setInt(2, 10);
             clblStmt.setInt(3, 20);
             clblStmt.registerOutParameter(1, Types.INTEGER);     
                                
             // with the extra property, executeQuery will no longer throw an exception
             rs = clblStmt.executeQuery(); 
             succeeded();
          }
          catch (SQLException sql) 
          {
             failed(sql, "still threw an exception");
          }
          catch (Exception e) 
          {
             failed (e, "Unexpected Exception");
          }

          if (rs != null) try { rs.close(); } catch (Exception e) {}
          if (clblStmt != null) try { clblStmt.close(); } catch (Exception e) {}
          if (statement != null) 
          {   
             try { statement.executeUpdate("drop procedure " + procedureName); } catch (Exception e) {}
             try { statement.close(); } catch (Exception e) {}
          }
          if (connection2 != null) try { connection2.close(); } catch (Exception e) {}
       }  
       else
          succeeded();
    }



/**
execute() - Test procedure that sometimes returns a result set.
Written for CPS 9UAE6C  Native JDBC SPC gets SQ99999 HY010	
**/
    public void Var015()
    {
	String info = " -- testing procedure that sometimes returns a result set (CPS 9UAE6C) added 3/12/2015 --  "; 
	Statement statement = null;        
	CallableStatement cStmt = null;
	ResultSet rs = null;
	StringBuffer sb = new StringBuffer(); 

	String procedureName = JDSetupProcedure.COLLECTION + ".JDCSEXCOLS";
	String  proc = "CREATE PROCEDURE " + procedureName + "( "
	  + " IN COLS INTEGER) "
	  + " LANGUAGE SQL "
	  + " DYNAMIC RESULT SETS 1                                                   "
	  + " BEGIN                                                          "
	  + "   DECLARE W_SQLSTMT VARCHAR(512); "
	  + "   DECLARE C1 CURSOR FOR S1; "
	  + " "
	  + "   IF COLS = 1 THEN "
	  + "      SET W_SQLSTMT = 'SELECT 1 FROM SYSIBM.SYSDUMMY1'; "
	  + "      PREPARE S1 FROM W_SQLSTMT; "
	  + "      OPEN C1; "
	  + "      SET RESULT SETS CURSOR C1; "
	  + "   END IF; "
    + "   IF COLS = 2 THEN "
    + "      SET W_SQLSTMT = 'SELECT 2,2 FROM SYSIBM.SYSDUMMY1'; "
    + "      PREPARE S1 FROM W_SQLSTMT; "
    + "      OPEN C1; "
    + "      SET RESULT SETS CURSOR C1; "
    + "   END IF; "
    + "   IF COLS = 3 THEN "
    + "      SET W_SQLSTMT = 'SELECT 3,3,3 FROM SYSIBM.SYSDUMMY1'; "
    + "      PREPARE S1 FROM W_SQLSTMT; "
    + "      OPEN C1; "
    + "      SET RESULT SETS CURSOR C1; "
    + "   END IF; "
    + "   IF COLS = 4 THEN "
    + "      SET W_SQLSTMT = 'SELECT 4,4,4,4 FROM SYSIBM.SYSDUMMY1'; "
    + "      PREPARE S1 FROM W_SQLSTMT; "
    + "      OPEN C1; "
    + "      SET RESULT SETS CURSOR C1; "
    + "   END IF; "
    + "   IF COLS = 5 THEN "
    + "      SET W_SQLSTMT = 'SELECT 5,5,5,5,5 FROM SYSIBM.SYSDUMMY1'; "
    + "      PREPARE S1 FROM W_SQLSTMT; "
    + "      OPEN C1; "
    + "      SET RESULT SETS CURSOR C1; "
    + "   END IF; "
	  + " END "; 


    try {
      boolean passed = true;
      statement = connection_.createStatement();
      try {
        statement.executeUpdate("drop procedure " + procedureName);
      } catch (Exception e) {
      }
      sb.append("Creating: " + proc + "\n");
      try {
        statement.executeUpdate(proc);
      } catch (Exception e) {
      }

      int sequence[] = { 0, 1, 0, 1, 2, 1, 0, 3, 4, 5, 3, 4, 0, 3, 4 };
      sb.append("prepare: CALL " + procedureName + "(?)\n");
      cStmt = connection_.prepareCall("CALL " + procedureName + "(?)");

      for (int i = 0; i < sequence.length; i++) {

        int cols = sequence[i];
        sb.append("Calling with cols=" + cols + "\n");
        cStmt.setInt(1, cols);
        cStmt.execute();
        sb.append(" .. getting resultSet\n");
        rs = cStmt.getResultSet();
        if (rs == null) {
          if (cols > 0) {
            passed = false;
            sb.append("------------------ failed -------------------\n");
            sb.append("Failed, not rs returned but number of cols > 0");
            sb.append("------------------ failed -------------------\n");
          }
        } else {
          if (rs.next()) {
          for (int col = 1; col <= cols; col++) {
            sb.append(" .. getting column"+col+"\n"); 
            int val = rs.getInt(col);
            if (val != cols) {
              passed = false;
              sb.append("------------------ failed -------------------\n");
              sb.append("Failed, for column=" + col + " got " + val + " sb "
                  + cols + "\n");
            } /* if not equal */
          } /* loop col */
          } else {
            passed = false; 
            sb.append("------------------ failed -------------------\n");
            sb.append("Failed, no rows returned\n"); 
            
          }
        } /* rs returned */

      } /* for i */

      assertCondition(passed, info + "\n" + sb.toString());

    } catch (Exception e) {
      failed(e, "Unexpected Exception " + info +"\n"+sb.toString());
    }

	if (statement != null) 	{   
	    try { statement.close(); } catch (Exception e) {}
	}
  if (cStmt != null)  {   
    try { cStmt.close(); } catch (Exception e) {}
}

    }




}





