///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSWasNull.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSWasNull.java
//
// Classes:      JDCSWasNull
//
////////////////////////////////////////////////////////////////////////
//
//
////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;
import java.awt.TextArea;
import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Hashtable;



/**
Testcase JDCSWasNull.  This tests the following
method of the JDBC CallableStatement class:

<ul>
<li>wasNull()
</ul>
**/
public class JDCSWasNull
extends JDTestcase
{



   // Private data.
   private Connection          connection_;
   private CallableStatement   csTypes_;
   private CallableStatement   csTypes2_;



/**
Constructor.
**/
   public JDCSWasNull (AS400 systemObject,
                       Hashtable namesAndVars,
                       int runMode,
                       FileOutputStream fileOutputStream,
                       
                       String password)
   {
      super (systemObject, "JDCSWasNull",
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
      connection_ = testDriver_.getConnection (baseURL_
                                               + ";errors=full", userId_, encryptedPassword_);

      csTypes_ = JDSetupProcedure.prepare (connection_,
                                           JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
      JDSetupProcedure.register (csTypes_, JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_, getDriver());
      csTypes_.execute ();        

      csTypes2_ = JDSetupProcedure.prepare (connection_,
                                            JDSetupProcedure.STP_CSTYPESNULL, supportedFeatures_);
      JDSetupProcedure.register (csTypes2_, JDSetupProcedure.STP_CSTYPESNULL, supportedFeatures_, getDriver());
      csTypes2_.execute ();

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
wasNull() - Without getting a parameter .
**/
   public void Var001()
   {
      try {
         assertCondition (csTypes_.wasNull () == false);
      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }



/**
wasNull() - After getting parameter -1.
**/
   public void Var002()
   {
      try {
         try {
            csTypes_.getObject (-1);
         } catch (SQLException e) {
            // Ignore.
         }
         assertCondition (csTypes_.wasNull () == false);
      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }



/**
wasNull() - After getting parameter 0.
**/
   public void Var003()
   {
      try {
         try {
            csTypes_.getObject (0);
         } catch (SQLException e) {
            // Ignore.
         }
         assertCondition (csTypes_.wasNull () == false);
      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }



/**
wasNull() - Use a parameter that is too big.
**/
   public void Var004()
   {
      try {
         try {
            csTypes_.getObject (35);
         } catch (SQLException e) {
            // Ignore.
         }
         assertCondition (csTypes_.wasNull () == false);
      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }



/**
wasNull() - After getting a parameter when there are no parameters.
**/
   public void Var005()
   {        
      // I created a whole new Connection object here to work
      // around a server bug.
      Connection c = null;
      try {
         c = testDriver_.getConnection (baseURL_
                                        + ";errors=full", userId_, encryptedPassword_);
         CallableStatement cs = c.prepareCall ("CALL "
                                               + JDSetupProcedure.STP_CS0);
         cs.execute ();
         try {
            cs.getObject (0);
         } catch (SQLException e) {
            // Ignore.
         }
         assertCondition (cs.wasNull () == false);
      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      } finally {
         try {
            c.close ();
         } catch (SQLException e) {
            // Ignore.
         }
      }
   }



/**
wasNull() - After getting a parameter that was not registered.
**/
   public void Var006()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESNULL, supportedFeatures_);
         JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                    supportedFeatures_, getDriver());
         try {
            cs.execute ();
            cs.getObject (12);
         } catch (SQLException e) {
            // Ignore.
         }
         assertCondition (cs.wasNull () == true);
      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }



/**
wasNull() - After getting a parameter when the statement has not been
executed.
**/
   public void Var007()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESNULL, supportedFeatures_);
         cs.registerOutParameter (12, Types.VARCHAR);
         try {
            cs.getObject (12);
         } catch (SQLException e) {
            // Ignore.
         }
         assertCondition (cs.wasNull () == false);
      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }



/**
wasNull() - After getting a parameter when the statement is closed.
**/
   public void Var008()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
         cs.registerOutParameter (12, Types.VARCHAR);
         cs.execute ();
         cs.close ();
         try {
            cs.getObject (12);
         } catch (SQLException e) {
            // Ignore.
         }
         cs.wasNull ();
         failed ("Didn't throw SQLException");
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
wasNull() - After getting an IN parameter that was correctly registered.
**/
   public void Var009()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESIN, supportedFeatures_);
         JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESIN,
                                              supportedFeatures_);
         cs.registerOutParameter (12, Types.VARCHAR);
         cs.execute ();
         try {
            cs.getObject (12);
         } catch (SQLException e) {
            // Ignore.
         }
         cs.wasNull ();
         failed ("Didn't throw SQLException");
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }


/**
wasNull() - After getting an INOUT parameter, where the OUT parameter is
longer than the IN parameter.
**/
   public void Var010()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_);
         JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                              supportedFeatures_);
         JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                    supportedFeatures_, getDriver());
         cs.registerOutParameter (12, Types.VARCHAR);
         cs.execute ();
         try {
            cs.getObject (12);
         } catch (SQLException e) {
            // Ignore.
         }
         assertCondition (cs.wasNull () == false);
      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }


/**
wasNull() - After getting a type that was registered as a SMALLINT when
the value is not NULL.
**/
   public void Var011()
   {
      try {
         csTypes_.getShort (1);
         assertCondition (csTypes_.wasNull() == false);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
wasNull() - After getting a type that was registered as a SMALLINT when
the value is NULL.
**/
   public void Var012()
   {
      try {
         csTypes2_.getShort (1);
         assertCondition (csTypes2_.wasNull() == true);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
wasNull() - After getting a type that was registered as an INTEGER when
the value is not NULL.
**/
   public void Var013()
   {
      try {
         csTypes_.getInt (2);
         assertCondition (csTypes_.wasNull() == false);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
wasNull() - After getting a type that was registered as an INTEGER when
the value is NULL.
**/
   public void Var014()
   {
      try {
         csTypes2_.getInt (2);
         assertCondition (csTypes2_.wasNull() == true);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
wasNull() - After getting a type that was registered as an REAL when
the value is not NULL.
**/
   public void Var015()
   {
      try {
         csTypes_.getFloat (3);
         assertCondition (csTypes_.wasNull() == false);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
wasNull() - After getting a type that was registered as an REAL when
the value is NULL.
**/
   public void Var016()
   {
      try {
         csTypes2_.getFloat (3);
         assertCondition (csTypes2_.wasNull() == true);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
wasNull() - After getting a type that was registered as an FLOAT when
the value is not NULL.
**/
   public void Var017()
   {
      try {
         csTypes_.getDouble (4);
         assertCondition (csTypes_.wasNull () == false);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
wasNull() - After getting a type that was registered as an FLOAT when
the value is NULL.
**/
   public void Var018()
   {
      try {
         csTypes2_.getDouble (4);
         assertCondition (csTypes2_.wasNull () == true);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
wasNull() - After getting a type that was registered as an DOUBLE
when the value is not NULL.
**/
   public void Var019()
   {
      try {
         csTypes_.getDouble (5);
         assertCondition (csTypes_.wasNull() == false);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
wasNull() - After getting a type that was registered as an DOUBLE
when the value is NULL.
**/
   public void Var020()
   {
      try {
         csTypes2_.getDouble (5);
         assertCondition (csTypes2_.wasNull() == true);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
wasNull() - After getting a type that was registered as an DECIMAL
when the value is not NULL.
**/
   public void Var021()
   {
      try {
         csTypes_.getBigDecimal (7, 0);
         assertCondition (csTypes_.wasNull () == false);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
wasNull() - After getting a type that was registered as an DECIMAL
when the value is NULL.
**/
   public void Var022()
   {
      try {
         csTypes2_.getBigDecimal (7, 0);
         assertCondition (csTypes2_.wasNull () == true);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
wasNull() - After getting a type that was registered as an NUMERIC when
the value is not NULL.
**/
   public void Var023()
   {
      try {
         csTypes_.getBigDecimal (9, 0);
         assertCondition (csTypes_.wasNull () == false);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
wasNull() - After getting a type that was registered as an NUMERIC when
the value is NULL.
**/
   public void Var024()
   {
      try {
         csTypes2_.getBigDecimal (9, 0);
         assertCondition (csTypes2_.wasNull () == true);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
wasNull() - After getting a type that was registered as a CHAR
when the value is not NULL.
**/
   public void Var025()
   {
      try {
         csTypes_.getString (11);
         assertCondition (csTypes_.wasNull () == false);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
wasNull() - After getting a type that was registered as a CHAR
when the value is NULL.
**/
   public void Var026()
   {
      try {
         csTypes2_.getString (11);
         assertCondition (csTypes2_.wasNull () == true);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
wasNull() - After getting a type that was registered as a VARCHAR
when the value is not NULL.
**/
   public void Var027()
   {
      try {
         csTypes_.getString (12);
         assertCondition (csTypes_.wasNull() == false);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
wasNull() - After getting a type that was registered as a VARCHAR
when the value is NULL.
**/
   public void Var028()
   {
      try {
         csTypes2_.getString (12);
         assertCondition (csTypes2_.wasNull() == true);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
wasNull() - After getting a type that was registered as a BINARY
when the value is not NULL.
**/
   public void Var029()
   {
      try {
         csTypes_.getBytes (13);
         assertCondition (csTypes_.wasNull() == false);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
wasNull() - After getting a type that was registered as a BINARY
when the value is NULL.
**/
   public void Var030()
   {
      try {
         csTypes2_.getBytes (13);
         assertCondition (csTypes2_.wasNull() == true);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
wasNull() - After getting a type that was registered as a VARBINARY
when the value is not NULL.
**/
   public void Var031()
   {
      try {
         csTypes_.getBytes (14);
         assertCondition (csTypes_.wasNull() == false);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
wasNull() - After getting a type that was registered as a VARBINARY
when the value is  NULL.
**/
   public void Var032()
   {
      try {
         csTypes2_.getBytes (14);
         assertCondition (csTypes2_.wasNull() == true);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
wasNull() - After getting a type that was registered as a DATE
when the value is not NULL.
**/
   public void Var033()
   {
      try {
         csTypes_.getDate (15);
         assertCondition (csTypes_.wasNull() == false);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
wasNull() - After getting a type that was registered as a DATE
when the value is NULL.
**/
   public void Var034()
   {
      try {
         csTypes2_.getDate (15);
         assertCondition (csTypes2_.wasNull() == true);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
wasNull() - After getting a type that was registered as a TIME
when the value is not NULL.
**/
   public void Var035()
   {
      try {
         csTypes_.getTime (16);
         assertCondition (csTypes_.wasNull() == false);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
wasNull() - After getting a type that was registered as a TIME
when the value is NULL.
**/
   public void Var036()
   {
      try {
         csTypes2_.getTime (16);
         assertCondition (csTypes2_.wasNull() == true);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
wasNull() - After getting a type that was registered as a TIMESTAMP
when the value is not NULL.
**/
   public void Var037()
   {
      try {
         csTypes_.getTimestamp (17);
         assertCondition (csTypes_.wasNull() == false);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
wasNull() - After getting a type that was registered as a TIMESTAMP
when the value is NULL.
**/
   public void Var038()
   {
      try {
         csTypes2_.getTimestamp (17);
         assertCondition (csTypes2_.wasNull() == true);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
wasNull() - After getting a type that was registered as an OTHER
when the value is not NULL.
**/
   public void Var039()
   {
      if (checkLobSupport ()) {
         try {
            csTypes_.getObject (18);
            assertCondition (csTypes_.wasNull() == false);
         } catch (Exception e)
         {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
wasNull() - After getting a type that was registered as an OTHER
when the value is NULL.
**/
   public void Var040()
   {
      if (checkLobSupport ()) {
         try {
            csTypes2_.getObject (18);
            assertCondition (csTypes2_.wasNull() == true);
         } catch (Exception e)
         {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
wasNull() - After getting a type that was registered as a BLOB
when the value is not NULL.
**/
   public void Var041()
   {
      if (checkLobSupport ()) {
         try {
            if (isJdbc20 ())
               csTypes_.getBlob (19);
            else
               csTypes_.getBytes (19);
            assertCondition (csTypes_.wasNull() == false);
         } catch (Exception e)
         {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
wasNull() - After getting a type that was registered as a BLOB
when the value is NULL.
**/
   public void Var042()
   {
      if (checkLobSupport ()) {
         try {
            if (isJdbc20 ())
               csTypes2_.getBlob (19);
            else
               csTypes2_.getBytes (19);
            assertCondition (csTypes2_.wasNull() == true);
         } catch (Exception e)
         {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
wasNull() - After getting a type that was registered as a CLOB
when the value is not NULL.
**/
   public void Var043()
   {
      if (checkLobSupport ()) {
         try {
            if (isJdbc20 ())
               csTypes_.getClob (20);
            else
               csTypes_.getString (20);
            assertCondition (csTypes_.wasNull() == false);
         } catch (Exception e)
         {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
wasNull() - After getting a type that was registered as a CLOB
when the value is NULL.
**/
   public void Var044()
   {
      if (checkLobSupport ()) {
         try {
            if (isJdbc20 ())
               csTypes2_.getClob (20);
            else
               csTypes2_.getString (20);
            assertCondition (csTypes2_.wasNull() == true);
         } catch (Exception e)
         {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
wasNull() - After getting a type that was registered as a BIGINT when
the value is not NULL.
**/
   public void Var045()
   {
      if (checkBigintSupport()) {
         try {
            csTypes_.getLong (22);
            assertCondition (csTypes_.wasNull() == false);
         } catch (Exception e)
         {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
wasNull() - After getting a type that was registered as a BIGINT when
the value is NULL.
**/
   public void Var046()
   {
      if (checkBigintSupport()) {
         try {
            csTypes2_.getLong (22);
            assertCondition (csTypes2_.wasNull() == true);
         } catch (Exception e)
         {
            failed (e, "Unexpected Exception");
         }
      }
   }



   public void Var047()
   {                 
      // For PTR 9950856
      Connection c = null;
      ResultSet rs = null;
      CallableStatement cs = null;
      try 
      {
         c = testDriver_.getConnection (baseURL_
                                        + ";errors=full", userId_, encryptedPassword_);

         cs = c.prepareCall( "{CALL " + JDSetupProcedure.STP_CSNULLTEST + "(?, ?, ?, ?, ?) }" );
          
         cs.setInt(1, 123);
         cs.setNull(2, Types.INTEGER);
         cs.setNull(3, Types.INTEGER);
         cs.setNull(4, Types.INTEGER);
         cs.setNull(5, Types.VARCHAR);
         rs = cs.executeQuery();
         rs.close();   
            
         cs.setInt(1, 123);
         cs.setNull(2, Types.INTEGER);
         cs.setNull(3, Types.INTEGER);
         cs.setNull(4, Types.INTEGER);
         cs.setNull(5, Types.VARCHAR);    // without fix this line throws exception 
         rs = cs.executeQuery();
         succeeded();
      } 
      catch (Exception e) 
      {
         failed (e, "Unexpected Exception");
      } 

      if (rs != null)
         try { rs.close(); } catch (Exception e1) {}
      if (cs != null)
         try { cs.close(); } catch (Exception e1) {}
      if (c != null)
         try { c.close(); } catch (Exception e1) {}

   }




/**
wasNull() - After getting a type that was registered as a BOOLEAN when
the value is not NULL.
**/
   public void Var048()
   {
      if (checkBooleanSupport()) {
         try {
            csTypes_.getLong (22);
            assertCondition (csTypes_.wasNull() == false);
         } catch (Exception e)
         {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
wasNull() - After getting a type that was registered as a BOOLEAN when
the value is NULL.
**/
   public void Var049()
   {
      if (checkBooleanSupport()) {
         try {
            csTypes2_.getLong (22);
            assertCondition (csTypes2_.wasNull() == true);
         } catch (Exception e)
         {
            failed (e, "Unexpected Exception");
         }
      }
   }



}



