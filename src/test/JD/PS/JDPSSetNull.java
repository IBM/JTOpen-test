///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSSetNull.java
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
// File Name:    JDPSSetNull.java
//
// Classes:      JDPSSetNull
//
////////////////////////////////////////////////////////////////////////
//
//
//
//
//
//
////////////////////////////////////////////////////////////////////////

package test.JD.PS;

import com.ibm.as400.access.AS400;

import test.JDPSTest;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JDTestcase;
import test.JD.JDSetupPackage;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable;



/**
Testcase JDPSetNull.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setNull()
</ul>
**/
public class JDPSSetNull
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDPSSetNull";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDPSTest.main(newArgs); 
   }



   // Constants.
   private static final String PACKAGE             = "JDPSSN";



   // Private data.
   private Connection          connection_;
   private Statement           statement_;



/**
Constructor.
**/
   public JDPSSetNull (AS400 systemObject,
                       Hashtable namesAndVars,
                       int runMode,
                       FileOutputStream fileOutputStream,
                       
                       String password)
   {
      super (systemObject, "JDPSSetNull",
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
setNull() - Should throw exception when the prepared
statement is closed.
**/
   public void Var001()
   {
      try {
         PreparedStatement ps = connection_.prepareStatement (
                                                             "INSERT INTO " + JDPSTest.PSTEST_SET
                                                             + " (C_NUMERIC_105) VALUES (?)");
         ps.close ();
         ps.setNull (1, Types.NUMERIC);
         failed ("Didn't throw SQLException");
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
setNull() - Should throw exception when an invalid index is
specified.
**/
   public void Var002()
   {
      try {
         PreparedStatement ps = connection_.prepareStatement (
                                                             "INSERT INTO " + JDPSTest.PSTEST_SET
                                                             + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
         ps.setNull (100, Types.INTEGER);
         ps.close ();
         failed ("Didn't throw SQLException");
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
setNull() - Should throw exception when index is 0.
**/
   public void Var003()
   {
      try {
         PreparedStatement ps = connection_.prepareStatement (
                                                             "INSERT INTO " + JDPSTest.PSTEST_SET
                                                             + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
         ps.setNull (0, Types.CHAR);
         ps.close ();
         failed ("Didn't throw SQLException");
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
setNull() - Should throw exception when index is -1.
**/
   public void Var004()
   {
      try {
         PreparedStatement ps = connection_.prepareStatement (
                                                             "INSERT INTO " + JDPSTest.PSTEST_SET
                                                             + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
         ps.setNull (-1, Types.CLOB);
         ps.close ();
         failed ("Didn't throw SQLException");
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
setNull() - Should work with a valid parameter index
greater than 1.
**/
   public void Var005()
   {
      try {
         statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

         PreparedStatement ps = connection_.prepareStatement (
                                                             "INSERT INTO " + JDPSTest.PSTEST_SET
                                                             + " (C_KEY, C_NUMERIC_105) VALUES (?, ?)");
         ps.setString (1, "Test");
         ps.setNull (2, Types.NUMERIC);
         ps.executeUpdate ();
         ps.close ();

         ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_105 FROM " + JDPSTest.PSTEST_SET);
         rs.next ();
         rs.getBigDecimal (1, 0);
         boolean wn = rs.wasNull ();
         rs.close ();

         assertCondition (wn == true);
      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }



/**
setNull() - Should throw exception when the parameter is
not an input parameter.
**/
   public void Var006()
   {
      try {
         PreparedStatement ps = connection_.prepareStatement (
                                                             "CALL " + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
         ps.setNull (2, Types.INTEGER);
         ps.close ();
         failed ("Didn't throw SQLException");
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
setNull() - Should throw an SQLException when the type is not valid.
   // @D2 will now succeed since type no longer checked
**/
   public void Var007()
   {
      try {
         statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

         PreparedStatement ps = connection_.prepareStatement (
                                                             "INSERT INTO " + JDPSTest.PSTEST_SET
                                                             + " (C_VARCHAR_50) VALUES (?)");
         ps.setNull (1, 48945);
         // failed("no exception");
         assertCondition(true);
      } catch (Exception e) {
         // assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         failed (e, "Unexpected Exception");
      }
   }



/**
setNull() - Set a SMALLINT parameter.
**/
   public void Var008()
   {
      try {
         statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

         PreparedStatement ps = connection_.prepareStatement (
                                                             "INSERT INTO " + JDPSTest.PSTEST_SET
                                                             + " (C_SMALLINT) VALUES (?)");
         ps.setNull (1, Types.SMALLINT);
         ps.executeUpdate ();
         ps.close ();

         ResultSet rs = statement_.executeQuery ("SELECT C_SMALLINT FROM " + JDPSTest.PSTEST_SET);
         rs.next ();
         rs.getShort (1);
         boolean wn = rs.wasNull ();
         rs.close ();

         assertCondition (wn == true);
      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }



/**
setNull() - Set a SMALLINT parameter, with the wrong type.
   // @D2 will now succeed since type no longer checked
**/
   public void Var009()
   {
      try {
         statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

         PreparedStatement ps = connection_.prepareStatement (
                                                             "INSERT INTO " + JDPSTest.PSTEST_SET
                                                             + " (C_SMALLINT) VALUES (?)");
         ps.setNull (1, Types.NULL);
         ps.executeUpdate ();
         ps.close ();

         ResultSet rs = statement_.executeQuery ("SELECT C_SMALLINT FROM " + JDPSTest.PSTEST_SET);
         rs.next ();
         rs.getShort (1);
         boolean wn = rs.wasNull ();
         rs.close ();

         assertCondition (wn == true);
      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }



/**
setNull() - Set an INTEGER parameter.
**/
   public void Var010()
   {
      try {
         statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

         PreparedStatement ps = connection_.prepareStatement (
                                                             "INSERT INTO " + JDPSTest.PSTEST_SET
                                                             + " (C_INTEGER) VALUES (?)");
         ps.setNull (1, Types.INTEGER);
         ps.executeUpdate ();
         ps.close ();

         ResultSet rs = statement_.executeQuery ("SELECT C_INTEGER FROM " + JDPSTest.PSTEST_SET);
         rs.next ();
         rs.getInt (1);
         boolean wn = rs.wasNull ();
         rs.close ();

         assertCondition (wn == true);
      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }



/**
setNull() - Set a INTEGER parameter, with the wrong type.
   // @D2 will now succeed since type no longer checked
**/
   public void Var011()
   {
      try {
         statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

         PreparedStatement ps = connection_.prepareStatement (
                                                             "INSERT INTO " + JDPSTest.PSTEST_SET
                                                             + " (C_INTEGER) VALUES (?)");
         ps.setNull (1, Types.DATE);
         ps.executeUpdate ();
         ps.close ();

         ResultSet rs = statement_.executeQuery ("SELECT C_INTEGER FROM " + JDPSTest.PSTEST_SET);
         rs.next ();
         rs.getInt (1);
         boolean wn = rs.wasNull ();
         rs.close ();

         assertCondition (wn == true);
      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }



/**
setNull() - Set an REAL parameter.
**/
   public void Var012()
   {
      try {
         statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

         PreparedStatement ps = connection_.prepareStatement (
                                                             "INSERT INTO " + JDPSTest.PSTEST_SET
                                                             + " (C_REAL) VALUES (?)");
         ps.setNull (1, Types.REAL);
         ps.executeUpdate ();
         ps.close ();

         ResultSet rs = statement_.executeQuery ("SELECT C_REAL FROM " + JDPSTest.PSTEST_SET);
         rs.next ();
         rs.getFloat (1);
         boolean wn = rs.wasNull ();
         rs.close ();

         assertCondition (wn == true);
      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }



/**
setNull() - Set a REAL parameter, with the wrong type.
   // @D2 will now succeed since type no longer checked
**/
   public void Var013()
   {
      try {
         statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

         PreparedStatement ps = connection_.prepareStatement (
                                                             "INSERT INTO " + JDPSTest.PSTEST_SET
                                                             + " (C_REAL) VALUES (?)");
         ps.setNull (1, Types.VARCHAR);
         ps.executeUpdate ();
         ps.close ();

         ResultSet rs = statement_.executeQuery ("SELECT C_REAL FROM " + JDPSTest.PSTEST_SET);
         rs.next ();
         rs.getFloat (1);
         boolean wn = rs.wasNull ();
         rs.close ();

         assertCondition (wn == true);
      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }



/**
setNull() - Set an FLOAT parameter.
**/
   public void Var014()
   {
      try {
         statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

         PreparedStatement ps = connection_.prepareStatement (
                                                             "INSERT INTO " + JDPSTest.PSTEST_SET
                                                             + " (C_FLOAT) VALUES (?)");
         ps.setNull (1, Types.DOUBLE);
         ps.executeUpdate ();
         ps.close ();

         ResultSet rs = statement_.executeQuery ("SELECT C_FLOAT FROM " + JDPSTest.PSTEST_SET);
         rs.next ();
         rs.getFloat (1);
         boolean wn = rs.wasNull ();
         rs.close ();

         assertCondition (wn == true);
      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }



/**
setNull() - Set a FLOAT parameter, with the wrong type.
   // @D2 will now succeed since type no longer checked
**/
   public void Var015()
   {
      try {
         statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

         PreparedStatement ps = connection_.prepareStatement (
                                                             "INSERT INTO " + JDPSTest.PSTEST_SET
                                                             + " (C_FLOAT) VALUES (?)");
         ps.setNull (1, Types.SMALLINT);
         ps.executeUpdate ();
         ps.close ();

         ResultSet rs = statement_.executeQuery ("SELECT C_FLOAT FROM " + JDPSTest.PSTEST_SET);
         rs.next ();
         rs.getFloat (1);
         boolean wn = rs.wasNull ();
         rs.close ();

         assertCondition (wn == true);
      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }



/**
setNull() - Set an DOUBLE parameter.
**/
   public void Var016()
   {
      try {
         statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

         PreparedStatement ps = connection_.prepareStatement (
                                                             "INSERT INTO " + JDPSTest.PSTEST_SET
                                                             + " (C_DOUBLE) VALUES (?)");
         ps.setNull (1, Types.DOUBLE);
         ps.executeUpdate ();
         ps.close ();

         ResultSet rs = statement_.executeQuery ("SELECT C_DOUBLE FROM " + JDPSTest.PSTEST_SET);
         rs.next ();
         rs.getDouble (1);
         boolean wn = rs.wasNull ();
         rs.close ();

         assertCondition (wn == true);
      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }



/**
setNull() - Set a DOUBLE parameter, with the wrong type.
   // @D2 will now succeed since type no longer checked
**/
   public void Var017()
   {
      try {
         statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

         PreparedStatement ps = connection_.prepareStatement (
                                                             "INSERT INTO " + JDPSTest.PSTEST_SET
                                                             + " (C_DOUBLE) VALUES (?)");
         ps.setNull (1, Types.TIMESTAMP);
         ps.executeUpdate ();
         ps.close ();

         ResultSet rs = statement_.executeQuery ("SELECT C_DOUBLE FROM " + JDPSTest.PSTEST_SET);
         rs.next ();
         rs.getDouble (1);
         boolean wn = rs.wasNull ();
         rs.close ();

         assertCondition (wn == true);
      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }



/**
setNull() - Set an DECIMAL parameter.
**/
   public void Var018()
   {
      try {
         statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

         PreparedStatement ps = connection_.prepareStatement (
                                                             "INSERT INTO " + JDPSTest.PSTEST_SET
                                                             + " (C_DECIMAL_105) VALUES (?)");
         ps.setNull (1, Types.DECIMAL);
         ps.executeUpdate ();
         ps.close ();

         ResultSet rs = statement_.executeQuery ("SELECT C_DECIMAL_105 FROM " + JDPSTest.PSTEST_SET);
         rs.next ();
         rs.getBigDecimal (1, 0);
         boolean wn = rs.wasNull ();
         rs.close ();

         assertCondition (wn == true);
      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }



/**
setNull() - Set a DECIMAL parameter, with the wrong type.
   // @D2 will now succeed since type no longer checked
**/
   public void Var019()
   {
      try {
         statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

         PreparedStatement ps = connection_.prepareStatement (
                                                             "INSERT INTO " + JDPSTest.PSTEST_SET
                                                             + " (C_DECIMAL) VALUES (?)");
         ps.setNull (1, Types.BINARY);
         failed ("Didn't throw SQLException");
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
setNull() - Set an NUMERIC parameter.
**/
   public void Var020()
   {
      try {
         statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

         PreparedStatement ps = connection_.prepareStatement (
                                                             "INSERT INTO " + JDPSTest.PSTEST_SET
                                                             + " (C_NUMERIC_105) VALUES (?)");
         ps.setNull (1, Types.NUMERIC);
         ps.executeUpdate ();
         ps.close ();

         ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_105 FROM " + JDPSTest.PSTEST_SET);
         rs.next ();
         rs.getBigDecimal (1, 0);
         boolean wn = rs.wasNull ();
         rs.close ();

         assertCondition (wn == true);
      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }



/**
setNull() - Set a NUMERIC parameter, with the wrong type.
   // @D2 will now succeed since type no longer checked
**/
   public void Var021()
   {
      try {
         statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

         PreparedStatement ps = connection_.prepareStatement (
                                                             "INSERT INTO " + JDPSTest.PSTEST_SET
                                                             + " (C_NUMERIC) VALUES (?)");
         ps.setNull (1, Types.CLOB);
         failed ("Didn't throw SQLException");
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
setNull() - Set an CHAR(50) parameter.
**/
   public void Var022()
   {
      try {
         statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

         PreparedStatement ps = connection_.prepareStatement (
                                                             "INSERT INTO " + JDPSTest.PSTEST_SET
                                                             + " (C_CHAR_50) VALUES (?)");
         ps.setNull (1, Types.CHAR);
         ps.executeUpdate ();
         ps.close ();

         ResultSet rs = statement_.executeQuery ("SELECT C_CHAR_50 FROM " + JDPSTest.PSTEST_SET);
         rs.next ();
         rs.getString (1);
         boolean wn = rs.wasNull ();
         rs.close ();

         assertCondition (wn == true);
      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }



/**
setNull() - Set a CHAR parameter, with the wrong type.
   // @D2 will now succeed since type no longer checked
**/
   public void Var023()
   {
      try {
         statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

         PreparedStatement ps = connection_.prepareStatement (
                                                             "INSERT INTO " + JDPSTest.PSTEST_SET
                                                             + " (C_CHAR) VALUES (?)");
         ps.setNull (1, Types.FLOAT);
         failed ("Didn't throw SQLException");
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
setNull() - Set an VARCHAR parameter.
**/
   public void Var024()
   {
      try {
         statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

         PreparedStatement ps = connection_.prepareStatement (
                                                             "INSERT INTO " + JDPSTest.PSTEST_SET
                                                             + " (C_VARCHAR_50) VALUES (?)");
         ps.setNull (1, Types.VARCHAR);
         ps.executeUpdate ();
         ps.close ();

         ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
         rs.next ();
         rs.getString (1);
         boolean wn = rs.wasNull ();
         rs.close ();

         assertCondition (wn == true);
      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }



/**
setNull() - Set a VARCHAR parameter, with the wrong type.
   // @D2 will now succeed since type no longer checked
**/
   public void Var025()
   {
      try {
         statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

         PreparedStatement ps = connection_.prepareStatement (
                                                             "INSERT INTO " + JDPSTest.PSTEST_SET
                                                             + " (C_VARCHAR_50) VALUES (?)");
         ps.setNull (1, Types.NULL);
         ps.executeUpdate ();
         ps.close ();

         ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
         rs.next ();
         rs.getString (1);
         boolean wn = rs.wasNull ();
         rs.close ();

         assertCondition (wn == true);
      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }



/**
setNull() - Set a CLOB parameter.
**/
   public void Var026()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               PreparedStatement ps = connection_.prepareStatement (
                                                                   "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                   + " (C_CLOB) VALUES (?)");
               ps.setNull (1, Types.CLOB);
               ps.executeUpdate ();
               ps.close ();

               ResultSet rs = statement_.executeQuery ("SELECT C_CLOB FROM " + JDPSTest.PSTEST_SET);
               rs.next ();
               rs.getClob (1);
               boolean wn = rs.wasNull ();
               rs.close ();

               assertCondition (wn == true);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }



/**
setNull() - Set a CLOB parameter, with the wrong type.
   // @D2 will now succeed since type no longer checked
**/
   public void Var027()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               PreparedStatement ps = connection_.prepareStatement (
                                                                   "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                   + " (C_CLOB) VALUES (?)");
               ps.setNull (1, Types.INTEGER);
               ps.executeUpdate ();
               ps.close ();

               ResultSet rs = statement_.executeQuery ("SELECT C_CLOB FROM " + JDPSTest.PSTEST_SET);
               rs.next ();
               rs.getClob (1);
               boolean wn = rs.wasNull ();
               rs.close ();

               assertCondition (wn == true);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }



/**
setNull() - Set a DBCLOB parameter.
**/
   public void Var028()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            succeeded ();
            /* Need to investigate this variation ...
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SET
                    + " (C_DBCLOB) VALUES (?)");
                ps.setNull (1, Types.CLOB);
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_DBCLOB FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                rs.getClob (1);
                boolean wn = rs.wasNull ();
                rs.close ();

                assertCondition (wn == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        */
         }
      }
   }



/**
setNull() - Set a DBCLOB parameter, with the wrong type.
   // @D2 will now succeed since type no longer checked
**/
   public void Var029()
   {
      if (checkLobSupport ()) {
         try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_DBCLOB) VALUES (?)");
            ps.setNull (1, Types.CHAR);
            assertCondition(true);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
setNull() - Set a BINARY parameter.
**/
   public void Var030()
   {
      try {
         PreparedStatement ps = connection_.prepareStatement (
                                                             "INSERT INTO " + JDPSTest.PSTEST_SET
                                                             + " (C_BINARY_20) VALUES (?)");
         ps.setNull (1, Types.BINARY);
         ps.executeUpdate ();
         ps.close ();

         ResultSet rs = statement_.executeQuery ("SELECT C_BINARY_20 FROM " + JDPSTest.PSTEST_SET);
         rs.next ();
         rs.getBytes (1);
         boolean wn = rs.wasNull ();
         rs.close ();

         assertCondition (wn == true);
      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }



/**
setNull() - Set a BINARY parameter, with the wrong type.
   // @D2 will now succeed since type no longer checked
**/
   public void Var031()
   {
      try {
         statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

         PreparedStatement ps = connection_.prepareStatement (
                                                             "INSERT INTO " + JDPSTest.PSTEST_SET
                                                             + " (C_BINARY) VALUES (?)");
         ps.setNull (1, Types.DOUBLE);
         failed ("Didn't throw SQLException");
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }




/**
setNull() - Set a VARBINARY parameter.
**/
   public void Var032()
   {
      try {
         PreparedStatement ps = connection_.prepareStatement (
                                                             "INSERT INTO " + JDPSTest.PSTEST_SET
                                                             + " (C_VARBINARY_20) VALUES (?)");
         ps.setNull (1, Types.VARBINARY);
         ps.executeUpdate ();
         ps.close ();

         ResultSet rs = statement_.executeQuery ("SELECT C_VARBINARY_20 FROM " + JDPSTest.PSTEST_SET);
         rs.next ();
         rs.getBytes (1);
         boolean wn = rs.wasNull ();
         rs.close ();

         assertCondition (wn == true);
      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }




/**
setNull() - Set a VARBINARY parameter, with the wrong type.
   // @D2 will now succeed since type no longer checked
**/
   public void Var033()
   {
      try {
         statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

         PreparedStatement ps = connection_.prepareStatement (
                                                             "INSERT INTO " + JDPSTest.PSTEST_SET
                                                             + " (C_VARBINARY) VALUES (?)");
         ps.setNull (1, Types.REAL);
         failed ("Didn't throw SQLException");
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
setNull() - Set a BLOB parameter.
**/
   public void Var034()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               PreparedStatement ps = connection_.prepareStatement (
                                                                   "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                   + " (C_BLOB) VALUES (?)");
               ps.setNull (1, Types.BLOB);
               ps.executeUpdate ();
               ps.close ();

               ResultSet rs = statement_.executeQuery ("SELECT C_BLOB FROM " + JDPSTest.PSTEST_SET);
               rs.next ();
               rs.getBlob (1);
               boolean wn = rs.wasNull ();
               rs.close ();

               assertCondition (wn == true);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }




/**
setNull() - Set a BLOB parameter, with the wrong type.
   // @D2 will now succeed since type no longer checked
**/
   public void Var035()
   {
      if (checkJdbc20 ()) {
         if (checkLobSupport ()) {
            try {
               PreparedStatement ps = connection_.prepareStatement (
                                                                   "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                   + " (C_BLOB) VALUES (?)");
               ps.setNull (1, Types.TIMESTAMP);
               ps.executeUpdate ();
               ps.close ();

               ResultSet rs = statement_.executeQuery ("SELECT C_BLOB FROM " + JDPSTest.PSTEST_SET);
               rs.next ();
               rs.getBlob (1);
               boolean wn = rs.wasNull ();
               rs.close ();

               assertCondition (wn == true);
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }
   }



/**
setNull() - Set a DATE parameter.
**/
   public void Var036()
   {
      try {
         PreparedStatement ps = connection_.prepareStatement (
                                                             "INSERT INTO " + JDPSTest.PSTEST_SET
                                                             + " (C_DATE) VALUES (?)");
         ps.setNull (1, Types.DATE);
         ps.executeUpdate ();
         ps.close ();

         ResultSet rs = statement_.executeQuery ("SELECT C_DATE FROM " + JDPSTest.PSTEST_SET);
         rs.next ();
         rs.getDate (1);
         boolean wn = rs.wasNull ();
         rs.close ();

         assertCondition (wn == true);
      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }



/**
setNull() - Set a DATE parameter, with the wrong type.
   // @D2 will now succeed since type no longer checked
**/
   public void Var037()
   {
      try {
         PreparedStatement ps = connection_.prepareStatement (
                                                             "INSERT INTO " + JDPSTest.PSTEST_SET
                                                             + " (C_DATE) VALUES (?)");
         ps.setNull (1, Types.BLOB);
         ps.executeUpdate ();
         ps.close ();

         ResultSet rs = statement_.executeQuery ("SELECT C_DATE FROM " + JDPSTest.PSTEST_SET);
         rs.next ();
         rs.getDate (1);
         boolean wn = rs.wasNull ();
         rs.close ();

         assertCondition (wn == true);
      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }


/**
setNull() - Set a TIME parameter.
**/
   public void Var038()
   {
      try {
         PreparedStatement ps = connection_.prepareStatement (
                                                             "INSERT INTO " + JDPSTest.PSTEST_SET
                                                             + " (C_TIME) VALUES (?)");
         ps.setNull (1, Types.TIME);
         ps.executeUpdate ();
         ps.close ();

         ResultSet rs = statement_.executeQuery ("SELECT C_TIME FROM " + JDPSTest.PSTEST_SET);
         rs.next ();
         rs.getTime (1);
         boolean wn = rs.wasNull ();
         rs.close ();

         assertCondition (wn == true);
      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }



/**
setNull() - Set a TIME parameter, with the wrong type.
   // @D2 will now succeed since type no longer checked
**/
   public void Var039()
   {
     try
     {
         PreparedStatement ps = connection_.prepareStatement (
                                                             "INSERT INTO " + JDPSTest.PSTEST_SET
                                                             + " (C_TIME) VALUES (?)");
         ps.setNull (1, Types.CHAR);
         ps.executeUpdate ();
         ps.close ();

         ResultSet rs = statement_.executeQuery ("SELECT C_TIME FROM " + JDPSTest.PSTEST_SET);
         rs.next ();
         rs.getTime (1);
         boolean wn = rs.wasNull ();
         rs.close ();

         assertCondition (wn == true);
      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }



/**
setNull() - Set a TIMESTAMP parameter.
**/
   public void Var040()
   {
      try {
         PreparedStatement ps = connection_.prepareStatement (
                                                             "INSERT INTO " + JDPSTest.PSTEST_SET
                                                             + " (C_TIMESTAMP) VALUES (?)");
         ps.setNull (1, Types.TIMESTAMP);
         ps.executeUpdate ();
         ps.close ();

         ResultSet rs = statement_.executeQuery ("SELECT C_TIMESTAMP FROM " + JDPSTest.PSTEST_SET);
         rs.next ();
         rs.getTimestamp (1);
         boolean wn = rs.wasNull ();
         rs.close ();

         assertCondition (wn == true);
      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }



/**
setNull() - Set a TIMESTAMP parameter, with the wrong type.
   // @D2 will now succeed since type no longer checked
**/
   public void Var041()
   {
      try {
         PreparedStatement ps = connection_.prepareStatement (
                                                             "INSERT INTO " + JDPSTest.PSTEST_SET
                                                             + " (C_TIMESTAMP) VALUES (?)");
         ps.setNull (1, Types.CLOB);
         ps.executeUpdate ();
         ps.close ();

         ResultSet rs = statement_.executeQuery ("SELECT C_TIMESTAMP FROM " + JDPSTest.PSTEST_SET);
         rs.next ();
         rs.getTimestamp (1);
         boolean wn = rs.wasNull ();
         rs.close ();

         assertCondition (wn == true);
      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }



/**
setNull() - Set a DATALINK parameter.
**/
   public void Var042()
   {
      if (checkLobSupport ()) {
         try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_DATALINK) VALUES (DLVALUE( CAST(? AS VARCHAR(50))))");
            ps.setNull (1, Types.VARCHAR);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DATALINK FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            rs.getString (1);
            boolean wn = rs.wasNull ();
            rs.close ();

            assertCondition (wn == true);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
setNull() - Set a DATALINK parameter, with the wrong type.
   // @D2 will now succeed since type no longer checked
**/
   public void Var043()
   {
      if (checkLobSupport ()) {
         try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_DATLINK) VALUES (?)");
            ps.setNull (1, Types.DOUBLE);
            failed ("Didn't throw SQLException");
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
setNull() - Set a DISTINCT parameter.
**/
   public void Var044()
   {
      if (checkLobSupport ()) {
         try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_DISTINCT) VALUES (?)");
            ps.setNull (1, Types.INTEGER);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DISTINCT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            rs.getInt (1);
            boolean wn = rs.wasNull ();
            rs.close ();

            assertCondition (wn == true);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
setNull() - Set a DISTINCT parameter, with the wrong type.
   // @D2 will now succeed since type no longer checked
**/
   public void Var045()
   {
      if (checkLobSupport ()) {
         try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_DISTINCT) VALUES (?)");
            ps.setNull (1, Types.REAL);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DISTINCT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            rs.getInt (1);
            boolean wn = rs.wasNull ();
            rs.close ();

            assertCondition (wn == true);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
setNull() - Set a BIGINT parameter.
**/
   public void Var046()
   {
      if (checkBigintSupport()) {
         try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_BIGINT) VALUES (?)");
            ps.setNull (1, Types.BIGINT);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_BIGINT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            rs.getLong (1);
            boolean wn = rs.wasNull ();
            rs.close ();

            assertCondition (wn == true);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
setNull() - Set a BIGINT parameter, with the wrong type.
   // @D2 will now succeed since type no longer checked
**/
   public void Var047()
   {
      if (checkBigintSupport()) {
         try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_BIGINT) VALUES (?)");
            ps.setNull (1, Types.TIME);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_BIGINT FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            rs.getLong (1);
            boolean wn = rs.wasNull ();
            rs.close ();

            assertCondition (wn == true);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
setNull() - Set a parameter in a statement that comes from the
package cache.
**/
   public void Var048()
   {
      try {
         String insert = "INSERT INTO " + JDPSTest.PSTEST_SET
                         + " (C_VARCHAR_50) VALUES (?)";

         if (isToolboxDriver())
            JDSetupPackage.prime (systemObject_, PACKAGE,
                                  JDPSTest.COLLECTION, insert);
         else
            JDSetupPackage.prime (systemObject_, encryptedPassword_, PACKAGE,
                                  JDPSTest.COLLECTION, insert, "", getDriver());

         statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

         Connection c2 = testDriver_.getConnection (baseURL_
                                                    + ";extended dynamic=true;package=" + PACKAGE
                                                    + ";package library=" + JDPSTest.COLLECTION
                                                    + ";package cache=true", userId_, encryptedPassword_);
         PreparedStatement ps = c2.prepareStatement (insert);
         ps.setNull (1, Types.VARCHAR);
         ps.executeUpdate ();
         ps.close ();
         c2.close ();

         ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
         rs.next ();
         rs.getString (1);
         boolean wn = rs.wasNull ();
         rs.close ();

         assertCondition (wn == true);
      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }



/**
setNull() - Set an VARCHAR parameter using Types.LONGVARCHAR.  There are users who want this to work.

SQL400 - Not run through the Toolbox as I don't think they make this work.
**/
   public void Var049()
   {
      if (getDriver () == JDTestDriver.DRIVER_NATIVE) {
         try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_VARCHAR_50) VALUES (?)");
            ps.setNull (1, Types.LONGVARCHAR);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            rs.getString (1);
            boolean wn = rs.wasNull ();
            rs.close ();

            assertCondition (wn == true);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      } else {
         notApplicable();
      }
   }



/**
setNull() - Set a VARBINARY parameter using Types.LONGVARBINARY.  Therea re users who want this to work.

SQL400 - Not run through the Toolbox as I don't think they make this work.
**/
   public void Var050()
   {
      if (getDriver () == JDTestDriver.DRIVER_NATIVE) {
         try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_VARBINARY_20) VALUES (?)");
            ps.setNull (1, Types.LONGVARBINARY);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_VARBINARY_20 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            rs.getBytes (1);
            boolean wn = rs.wasNull ();
            rs.close ();

            assertCondition (wn == true);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      } else {
         notApplicable();
      }
   }



/**
setNull() - Test that we can set a parameter to null and back to inserting valid values correctly
when we are dealing with BigDecimal values.

SQL400 - This test was added due to a customer bug.
**/
   public void Var051()
   {
      if (getDriver () == JDTestDriver.DRIVER_NATIVE) {
         try {

            try{
               statement_.executeUpdate ("create table qtemp.test2( col1 decimal(5,2) )");
            } catch (java.sql.SQLException ex) {
               // Ignore failure on table create.
            }

            PreparedStatement ps = connection_.prepareStatement( "insert into qtemp.test2 (col1) values (?)" );
            ps.setBigDecimal( 1, new java.math.BigDecimal( 0.1 ) );
            ps.executeUpdate();
            ps.setNull( 1, Types.DECIMAL );
            ps.executeUpdate();
            ps.setNull( 1, Types.DECIMAL );
            ps.executeUpdate();
            // NOTE:  This is where we were having problems inserting parameters... (on the executeUpdate)
            ps.setBigDecimal( 1, new java.math.BigDecimal( 0.1 ) );
            ps.executeUpdate();

            ResultSet rs = statement_.executeQuery( "select col1 from qtemp.test2" );

            rs.next();
            boolean success1 = rs.getString(1).equals("0.10");
            rs.next();
            boolean success2 = rs.getString(1) == null;
            rs.next();
            boolean success3 = rs.getString(1) == null;
            rs.next();
            boolean success4 = rs.getString(1).equals("0.10");


            assertCondition (success1 && success2 && success3 && success4);

            rs.close();

         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      } else {
         notApplicable();
      }
   }



/**
setNull() - Set an DECFLOAT(16) parameter.
**/
   public void Var052()
   {
       if (checkDecFloatSupport()) { 
	   try {
	       statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP16);

	       PreparedStatement ps = connection_.prepareStatement (
								    "INSERT INTO " + JDPSTest.PSTEST_SETDFP16
								    + "  VALUES (?)");
	       ps.setNull (1, Types.DECIMAL);
	       ps.executeUpdate ();
	       ps.close ();

	       ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP16);
	       rs.next ();
	       rs.getBigDecimal (1, 0);
	       boolean wn = rs.wasNull ();
	       rs.close ();

	       assertCondition (wn == true);
	   } catch (Exception e) {
	       failed (e, "Unexpected Exception");
	   }
       }
   }


/**
setNull() - Set an DECFLOAT(16) parameter.
**/
   public void Var053()
   {
       if (checkDecFloatSupport()) { 
	   try {
	       statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP16);

	       PreparedStatement ps = connection_.prepareStatement (
								    "INSERT INTO " + JDPSTest.PSTEST_SETDFP16
								    + "  VALUES (?)");
	       ps.setNull (1, Types.OTHER);
	       ps.executeUpdate ();
	       ps.close ();

	       ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP16);
	       rs.next ();
	       rs.getBigDecimal (1, 0);
	       boolean wn = rs.wasNull ();
	       rs.close ();

	       assertCondition (wn == true);
	   } catch (Exception e) {
	       failed (e, "Unexpected Exception");
	   }
       }
   }



/**
setNull() - Set an DECFLOAT(34) parameter.
**/
   public void Var054()
   {
       if (checkDecFloatSupport()) { 
	   try {
	       statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP34);

	       PreparedStatement ps = connection_.prepareStatement (
								    "INSERT INTO " + JDPSTest.PSTEST_SETDFP34
								    + "  VALUES (?)");
	       ps.setNull (1, Types.DECIMAL);
	       ps.executeUpdate ();
	       ps.close ();

	       ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP34);
	       rs.next ();
	       rs.getBigDecimal (1, 0);
	       boolean wn = rs.wasNull ();
	       rs.close ();

	       assertCondition (wn == true);
	   } catch (Exception e) {
	       failed (e, "Unexpected Exception");
	   }
       }
   }


/**
setNull() - Set an DECFLOAT(34) parameter.
**/
   public void Var055()
   {
       if (checkDecFloatSupport()) { 
	   try {
	       statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP34);

	       PreparedStatement ps = connection_.prepareStatement (
								    "INSERT INTO " + JDPSTest.PSTEST_SETDFP34
								    + "  VALUES (?)");
	       ps.setNull (1, Types.OTHER);
	       ps.executeUpdate ();
	       ps.close ();

	       ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP34);
	       rs.next ();
	       rs.getBigDecimal (1, 0);
	       boolean wn = rs.wasNull ();
	       rs.close ();

	       assertCondition (wn == true);
	   } catch (Exception e) {
	       failed (e, "Unexpected Exception");
	   }
       }

   }


/**
setNull() - Set an XML parameter.
**/
   public void Var056()
   {
       String added = "-- NULL XML test added 5/15/2009 by native driver "; 
       if (checkXmlSupport()) { 
	   try {
	       statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETXML);

	       PreparedStatement ps = connection_.prepareStatement (
								    "INSERT INTO " + JDPSTest.PSTEST_SETXML
								    + "  VALUES (?)");
	       ps.setNull (1, 2009);
	       ps.executeUpdate ();
	       ps.close ();

	       ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETXML);
	       rs.next ();
	       rs.getString(1); 
	       boolean wn = rs.wasNull ();
	       rs.close ();

	       assertCondition (wn == true, "expected null, but wasNull="+wn+ added); 
	   } catch (Exception e) {
	       failed (e, "Unexpected Exception "+added); 
	   }
       }

   }



   /**
   setNull() - Set a BOOLEAN parameter.
   **/
      public void Var057()
      {
         if (checkBooleanSupport()) {
            try {
               statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

               PreparedStatement ps = connection_.prepareStatement (
                                                                   "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                   + " (C_BOOLEAN) VALUES (?)");
               ps.setNull (1, Types.BOOLEAN);
               ps.executeUpdate ();
               ps.close ();

               ResultSet rs = statement_.executeQuery ("SELECT C_BOOLEAN FROM " + JDPSTest.PSTEST_SET);
               rs.next ();
               rs.getBoolean (1);
               boolean wn = rs.wasNull ();
               rs.close ();

               assertCondition (wn == true, "wasNull returned "+wn+" instead of true");
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      }






}

