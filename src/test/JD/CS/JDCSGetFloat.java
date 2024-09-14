///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetFloat.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetFloat.java
//
// Classes:      JDCSGetFloat
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDSetupProcedure;

import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable;



/**
Testcase JDCSGetFloat.  This tests the following
method of the JDBC CallableStatement class:

<ul>
<li>getFloat()
</ul>
**/
public class JDCSGetFloat
extends JDCSGetTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSGetFloat";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCSTest.main(newArgs); 
   }



 


/**
Constructor.
**/
   public JDCSGetFloat (AS400 systemObject,
                        Hashtable namesAndVars,
                        int runMode,
                        FileOutputStream fileOutputStream,
                        
                        String password)
   {
      super (systemObject, "JDCSGetFloat",
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
     super.setup();
     
   }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
   protected void cleanup ()
   throws Exception
   {
     super.cleanup(); 
   }



/**
getFloat() - Get parameter -1.
**/
   public void Var001()
   {
      try {
         csTypes_.execute ();
         float p = csTypes_.getFloat (-1);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getFloat() - Get parameter 0.
**/
   public void Var002()
   {
      try {
         float p = csTypes_.getFloat (0);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getFloat() - Use a parameter that is too big.
**/
   public void Var003()
   {
      try {
         float p = csTypes_.getFloat (35);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getFloat() - Get a parameter when there are no parameters.
**/
   public void Var004()
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
         float p = cs.getFloat (1);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      } finally {
         try {
            c.close ();
         } catch (SQLException e) {
            // Ignore.
         }
      }
   }



/**
getFloat() - Get a parameter that was not registered.
**/
   public void Var005()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
         cs.execute ();
         float p = cs.getFloat (3);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getFloat() - Get a parameter when the statement has not been
executed.
**/
   public void Var006()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
         cs.registerOutParameter (3, Types.REAL);
         float p = cs.getFloat (3);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getFloat() - Get a parameter when the statement is closed.
**/
   public void Var007()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
         cs.registerOutParameter (3, Types.REAL);
         cs.execute ();
         cs.close ();
         float p = cs.getFloat (3);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getFloat() - Get an IN parameter that was correctly registered.
**/
   public void Var008()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESIN, supportedFeatures_);
         JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESIN,
                                              supportedFeatures_);
         cs.registerOutParameter (3, Types.REAL);
         cs.execute ();
         float p = cs.getFloat (3);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }


/**
getFloat() - Get an INOUT parameter, where the OUT parameter is
longer than the IN parameter.
**/
   public void Var009()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_);
         JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                              supportedFeatures_);
         JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                    supportedFeatures_, getDriver());
         cs.registerOutParameter (3, Types.REAL);
         cs.execute ();
         float p = cs.getFloat (3);
         assertCondition (p == -3.3f);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }


/**
getFloat() - Get a type that was registered as a SMALLINT.
**/
   public void Var010()
   {
     String info = " -- call getFloat against SMALLINT -- Changed October 2011";

     // As of 10/3/2011 for the toolbox driver, it is now possible to get a double
     // from a parameter registered as smallint.
     // Note: JCC does not throw exception either.
     // Native JDBC will now need to change.
     //

      try {
         float p = csTypes_.getFloat (1);

         float expected = (float) 123.0;
         assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
      
      } catch (Exception e) {
        failed (e, "Unexpected Exception"+info);

      }
   }



/**
getFloat() - Get a type that was registered as an INTEGER.
**/
   public void Var011()
   {
     String info = " -- call getFloat against INTEGER -- Changed October 2011";
      try {
         float p = csTypes_.getFloat (2);                  
         float expected = (float)-456.0 ;
         assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
      } catch (Exception e) {
        failed (e, "Unexpected Exception"+info);
      }
   }



/**
getFloat() - Get a type that was registered as an REAL.
**/
   public void Var012()
   {
      try {
         float p = csTypes_.getFloat (3);
         assertCondition (p == 789.54f);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
getFloat() - Get a type that was registered as an FLOAT.
**/
   public void Var013()
   {
     String info = " -- call getFloat against FLOAT -- Changed October 2011";
      try {
         float p = csTypes_.getFloat (4);
         float expected = (float)253.1027 ;
         assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
      } catch (Exception e) {
        failed (e, "Unexpected Exception"+info);
      }
   }



/**
getFloat() - Get a type that was registered as an DOUBLE.
**/
   public void Var014()
   {
     String info = " -- call getFloat against DOUBLE -- Changed October 2011";
      try {
         float p = csTypes_.getFloat (5);
         float expected = (float)-987.3434 ;
         assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
      } catch (Exception e) {
        failed (e, "Unexpected Exception"+info);
      }
   }



/**
getFloat() - Get a type that was registered as an DECIMAL.
**/
   public void Var015()
   {
     String info = " -- call getFloat against DECIMAL -- Changed October 2011";
      try {
         float p = csTypes_.getFloat (7);
         float expected = (float)  -94732.125;
         assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
      } catch (Exception e) {
        failed (e, "Unexpected Exception"+info);
      }
   }



/**
getFloat() - Get a type that was registered as an NUMERIC.
**/
   public void Var016()
   {
     String info = " -- call getFloat against NUMERIC -- Changed October 2011";
      try {
         float p = csTypes_.getFloat (9);
         float expected = (float) 19.98765 ;
         assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
      } catch (Exception e) {
        failed (e, "Unexpected Exception"+info);
      }
   }



/**
getFloat() - Get a type that was registered as a CHAR.
**/
   public void Var017()
   {
      try {
         float p = csTypes_.getFloat (11);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getFloat() - Get a type that was registered as a VARCHAR.
**/
   public void Var018()
   {
      try {
         float p = csTypes_.getFloat (12);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getFloat() - Get a type that was registered as a BINARY.
**/
   public void Var019()
   {
      try {
         float p = csTypes_.getFloat (13);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getFloat() - Get a type that was registered as a VARBINARY.
**/
   public void Var020()
   {
      try {
         float p = csTypes_.getFloat (14);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getFloat() - Get a type that was registered as a DATE.
**/
   public void Var021()
   {
      try {
         float p = csTypes_.getFloat (15);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getFloat() - Get a type that was registered as a TIME.
**/
   public void Var022()
   {
      try {
         float p = csTypes_.getFloat (16);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getFloat() - Get a type that was registered as a TIMESTAMP.
**/
   public void Var023()
   {
      try {
         float p = csTypes_.getFloat (17);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getFloat() - Get a type that was registered as an OTHER.
**/
   public void Var024()
   {
      if (checkLobSupport ()) {
         try {
            float p = csTypes_.getFloat (18);
            failed ("Didn't throw SQLException "+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getFloat() - Get a type that was registered as a BLOB.
**/
   public void Var025()
   {
      if (checkLobSupport ()) {
         try {
            float p = csTypes_.getFloat (19);
            failed ("Didn't throw SQLException "+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getFloat() - Get a type that was registered as a CLOB.
**/
   public void Var026()
   {
      if (checkLobSupport ()) {
         try {
            float p = csTypes_.getFloat (20);
            failed ("Didn't throw SQLException "+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getFloat() - Get a type that was registered as a BIGINT.
**/
   public void Var027()
   {
      if (checkBigintSupport()) {
        String info = " -- call getFloat against BIGINT -- Changed October 2011";
         try {
            float p = csTypes_.getFloat (22);                  
            float expected = (float) 9.8766225E11;
            assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
         } catch (Exception e) {
           failed (e, "Unexpected Exception"+info);
         }
      }
   }



/**
getFloat() - Get an INOUT parameter, where the OUT parameter is
longer than the IN parameter, when the output parameter is registered first.

SQL400 - We added this testcase because of a customer bug.
**/
   public void Var028()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_);
         JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                    supportedFeatures_, getDriver());
         cs.registerOutParameter (3, Types.REAL);
         JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                              supportedFeatures_);
         cs.execute ();
         float p = cs.getFloat (3);
         assertCondition (p == -3.3f);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
getFloat() - Attempt to get the return value parameter when it is registered.
**/
   public void Var029()
   {
      if (checkReturnValueSupport()) {
      try {
         CallableStatement cs = connection_.prepareCall("?=CALL " + JDSetupProcedure.STP_CSRV);
         cs.registerOutParameter(1, Types.INTEGER);
         cs.execute ();
         float f = cs.getFloat(1);
         assertCondition (f == 1976.0);
      } catch (Exception e)
      {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
      }
   }



/**
getFloat() -- Get an output parameter where the database output type isn't necessarily a float 
*/
   public void Var030()
   {
      try {
	  Statement stmt = connection_.createStatement();
	  
	  try {
	      stmt.executeUpdate("drop table "+JDCSTest.COLLECTION+".float_tab");
	  } catch (Exception e) {}
	  try {
	      stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".float_proc"); 
	  } catch (Exception e) {}

	  stmt.executeUpdate("create table "+JDCSTest.COLLECTION+".float_tab(MAX_VAL NUMERIC, MIN_VAL NUMERIC, NULL_VAL NUMERIC )"); 

	  stmt.executeUpdate("create procedure "+JDCSTest.COLLECTION+".float_proc (out MAX_PARAM NUMERIC, out MIN_PARAM NUMERIC, out NULL_PARAM NUMERIC) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from "+JDCSTest.COLLECTION+".float_tab; end");

	  float maxFloat = (float) 99999.0;
	  stmt.executeUpdate("insert into "+JDCSTest.COLLECTION+".float_tab values("+maxFloat+",0,null)");
	  CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".float_proc(?,?,?)}");

		//register the output parameters
	  cstmt.registerOutParameter(1,java.sql.Types.FLOAT);
	  cstmt.registerOutParameter(2,java.sql.Types.FLOAT);
	  cstmt.registerOutParameter(3,java.sql.Types.FLOAT);

		//execute the procedure
	  cstmt.execute();

		//invoke getFloat method
	  float xRetVal = cstmt.getFloat(1);
	  float yRetVal = cstmt.getFloat(2);
	  float zRetVal = cstmt.getFloat(3);
	  
	  assertCondition(xRetVal == maxFloat && yRetVal == 0 && zRetVal == 0); 

      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }

/**
getFloat() -- Get an output parameter where the database output type isn't necessarily a float
*/
   public void Var031()
   {
      try {
	  Statement stmt = connection_.createStatement();
	  
	  try {
	      stmt.executeUpdate("drop table "+JDCSTest.COLLECTION+".float_tab");
	  } catch (Exception e) {}
	  try {
	      stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".float_proc"); 
	  } catch (Exception e) {}

	  stmt.executeUpdate("create table "+JDCSTest.COLLECTION+".float_tab(MAX_VAL VARCHAR(30), MIN_VAL VARCHAR(30), NULL_VAL VARCHAR(30) )"); 

	  stmt.executeUpdate("create procedure "+JDCSTest.COLLECTION+".float_proc (out MAX_PARAM VARCHAR(30), out MIN_PARAM VARCHAR(30), out NULL_PARAM VARCHAR(30)) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from "+JDCSTest.COLLECTION+".float_tab; end");

	  float maxFloat = (float) 1.38972E+15;
	  stmt.executeUpdate("insert into "+JDCSTest.COLLECTION+".float_tab values('"+maxFloat+"','0',null)");
	  CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".float_proc(?,?,?)}");

		//register the output parameters
	  cstmt.registerOutParameter(1,java.sql.Types.FLOAT);
	  cstmt.registerOutParameter(2,java.sql.Types.FLOAT);
	  cstmt.registerOutParameter(3,java.sql.Types.FLOAT);

		//execute the procedure
	  cstmt.execute();

		//invoke getFloat method
	  float xRetVal = cstmt.getFloat(1);
	  float yRetVal = cstmt.getFloat(2);
	  float zRetVal = cstmt.getFloat(3);
	  
	  assertCondition(xRetVal == maxFloat && yRetVal == 0 && zRetVal == 0); 

      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }

/**
getFloat() -- Get an output parameter where the database output type isn't necessarily a float, but the value can't be converted 
*/
   public void Var032()
   {
      try {
	  Statement stmt = connection_.createStatement();
	  
	  try {
	      stmt.executeUpdate("drop table "+JDCSTest.COLLECTION+".float_tab");
	  } catch (Exception e) {}
	  try {
	      stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".float_proc"); 
	  } catch (Exception e) {}

	  stmt.executeUpdate("create table "+JDCSTest.COLLECTION+".float_tab(MAX_VAL VARCHAR(30), MIN_VAL VARCHAR(30), NULL_VAL VARCHAR(30) )"); 

	  stmt.executeUpdate("create procedure "+JDCSTest.COLLECTION+".float_proc (out MAX_PARAM VARCHAR(30), out MIN_PARAM VARCHAR(30), out NULL_PARAM VARCHAR(30)) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from "+JDCSTest.COLLECTION+".float_tab; end");

	  stmt.executeUpdate("insert into "+JDCSTest.COLLECTION+".float_tab values('BIGNUMBER','0',null)");
	  CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".float_proc(?,?,?)}");

		//register the output parameters
	  cstmt.registerOutParameter(1,java.sql.Types.FLOAT);
	  cstmt.registerOutParameter(2,java.sql.Types.FLOAT);
	  cstmt.registerOutParameter(3,java.sql.Types.FLOAT);

		//execute the procedure
	  cstmt.execute();

		//invoke getFloat method
	  float xRetVal = cstmt.getFloat(1);
	  float yRetVal = cstmt.getFloat(2);
	  float zRetVal = cstmt.getFloat(3);
	  failed("Didn't throw exception "+xRetVal+","+yRetVal+","+zRetVal);


      } catch (Exception e) {
          //
          // For the native driver, this is a data type mismatch
          // 
	  assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }

   }

/**
getFloat() -- Get an output parameter where the database output type isn't necessarily a float 
*/
   public void Var033()
   {
      try {
	  Statement stmt = connection_.createStatement();
	  
	  try {
	      stmt.executeUpdate("drop table "+JDCSTest.COLLECTION+".float_tab");
	  } catch (Exception e) {}
	  try {
	      stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".float2_proc"); 
	  } catch (Exception e) {}

	  stmt.executeUpdate("create table "+JDCSTest.COLLECTION+".float_tab(MAX_VAL NUMERIC, MIN_VAL NUMERIC, NULL_VAL NUMERIC )"); 

	  stmt.executeUpdate("create procedure "+JDCSTest.COLLECTION+".float2_proc (out MAX_PARAM NUMERIC, out MIN_PARAM NUMERIC, out NULL_PARAM NUMERIC) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from "+JDCSTest.COLLECTION+".float_tab; end");

	  float maxFloat = (float) 99999.0;
	  stmt.executeUpdate("insert into "+JDCSTest.COLLECTION+".float_tab values("+maxFloat+",0,null)");
	  CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".float2_proc(?,?,?)}");

		//register the output parameters
	  cstmt.registerOutParameter(1,java.sql.Types.REAL);
	  cstmt.registerOutParameter(2,java.sql.Types.REAL);
	  cstmt.registerOutParameter(3,java.sql.Types.REAL);

		//execute the procedure
	  cstmt.execute();

		//invoke getFloat method
	  float xRetVal = cstmt.getFloat(1);
	  float yRetVal = cstmt.getFloat(2);
	  float zRetVal = cstmt.getFloat(3);
	  
	  assertCondition(xRetVal == maxFloat && yRetVal == 0 && zRetVal == 0); 

      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }

/**
getFloat() -- Get an output parameter where the database output type isn't necessarily a float
*/
   public void Var034()
   {
      try {
	  Statement stmt = connection_.createStatement();
	  
	  try {
	      stmt.executeUpdate("drop table "+JDCSTest.COLLECTION+".float_tab");
	  } catch (Exception e) {}
	  try {
	      stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".float2_proc"); 
	  } catch (Exception e) {}

	  stmt.executeUpdate("create table "+JDCSTest.COLLECTION+".float_tab(MAX_VAL VARCHAR(30), MIN_VAL VARCHAR(30), NULL_VAL VARCHAR(30) )"); 

	  stmt.executeUpdate("create procedure "+JDCSTest.COLLECTION+".float2_proc (out MAX_PARAM VARCHAR(30), out MIN_PARAM VARCHAR(30), out NULL_PARAM VARCHAR(30)) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from "+JDCSTest.COLLECTION+".float_tab; end");

	  float maxFloat = (float) 1.38972E+15;
	  stmt.executeUpdate("insert into "+JDCSTest.COLLECTION+".float_tab values('"+maxFloat+"','0',null)");
	  CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".float2_proc(?,?,?)}");

		//register the output parameters
	  cstmt.registerOutParameter(1,java.sql.Types.REAL);
	  cstmt.registerOutParameter(2,java.sql.Types.REAL);
	  cstmt.registerOutParameter(3,java.sql.Types.REAL);

		//execute the procedure
	  cstmt.execute();

		//invoke getFloat method
	  float xRetVal = cstmt.getFloat(1);
	  float yRetVal = cstmt.getFloat(2);
	  float zRetVal = cstmt.getFloat(3);
	  
	  assertCondition(xRetVal == maxFloat && yRetVal == 0 && zRetVal == 0); 

      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }

/**
getFloat() -- Get an output parameter where the database output type isn't necessarily a float, but the value can't be converted 
*/
   public void Var035()
   {
      try {
	  Statement stmt = connection_.createStatement();
	  
	  try {
	      stmt.executeUpdate("drop table "+JDCSTest.COLLECTION+".float_tab");
	  } catch (Exception e) {}
	  try {
	      stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".float2_proc"); 
	  } catch (Exception e) {}

	  stmt.executeUpdate("create table "+JDCSTest.COLLECTION+".float_tab(MAX_VAL VARCHAR(30), MIN_VAL VARCHAR(30), NULL_VAL VARCHAR(30) )"); 

	  stmt.executeUpdate("create procedure "+JDCSTest.COLLECTION+".float2_proc (out MAX_PARAM VARCHAR(30), out MIN_PARAM VARCHAR(30), out NULL_PARAM VARCHAR(30)) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from "+JDCSTest.COLLECTION+".float_tab; end");

	  stmt.executeUpdate("insert into "+JDCSTest.COLLECTION+".float_tab values('BIGNUMBER','0',null)");
	  CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".float2_proc(?,?,?)}");

		//register the output parameters
	  cstmt.registerOutParameter(1,java.sql.Types.REAL);
	  cstmt.registerOutParameter(2,java.sql.Types.REAL);
	  cstmt.registerOutParameter(3,java.sql.Types.REAL);

		//execute the procedure
	  cstmt.execute();

		//invoke getFloat method
	  float xRetVal = cstmt.getFloat(1);
	  float yRetVal = cstmt.getFloat(2);
	  float zRetVal = cstmt.getFloat(3);
	  failed("Didn't throw exception "+xRetVal+","+yRetVal+","+zRetVal);


      } catch (Exception e) {
          //
          // For the native driver, this is a data type mismatch
          // 
	  assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }

// NAMED PARAMETERS

/**
getFloat() - Get TYPE FROM NAMED PARAMETER THAT DOESN'T EXIST
**/
   public void Var036()
   {
       if(checkNamedParametersSupport()){
	   try {
	       float p = csTypes_.getFloat ("P_FAKE");
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }
/**
getFloat() - Get a type that was registered as a SMALLINT.
**/
   public void Var037()
   {
       if(checkNamedParametersSupport()){
         String info = " -- call getFloat against SMALLINT -- Changed October 2011";
	   try {
	       float p = csTypes_.getFloat ("P_SMALLINT");
	         float expected = (float) 123.0 ;
	         assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
	   } catch (Exception e) {
	        failed (e, "Unexpected Exception"+info);
	   }
       }
   }


/**
getFloat() - Get a type that was registered as an INTEGER.
**/
   public void Var038()
   {
     String info = " -- call getFloat against INTEGER -- Changed October 2011";
        if(checkNamedParametersSupport()){
	    try {
		float p = csTypes_.getFloat ("P_INTEGER");                  
	         float expected = (float) -456.0 ;
	         assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
	    } catch (Exception e) {
	        failed (e, "Unexpected Exception"+info);
	    }
	}
   }


/**
getFloat() - Get a type that was registered as an REAL.
**/
   public void Var039()
   {
        if(checkNamedParametersSupport()){
	    try {
		float p = csTypes_.getFloat ("P_REAL");
		assertCondition (p == 789.54f);
	    } catch (Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
   }


/**
getFloat() - Get a type that was registered as an FLOAT.
**/
   public void Var040()
   {
     String info = " -- call getFloat against FLOAT -- Changed October 2011";
        if(checkNamedParametersSupport()){
	    try {
		float p = csTypes_.getFloat ("P_FLOAT");
	         float expected = (float) 253.1027;
	         assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
	    } catch (Exception e) {
	        failed (e, "Unexpected Exception"+info);
	    }
	}	
   }


/**
getFloat() - Get a type that was registered as an DOUBLE.
**/
   public void Var041()
   {
     String info = " -- call getFloat against DOUBLE -- Changed October 2011";
        if(checkNamedParametersSupport()){
	    try {
		float p = csTypes_.getFloat ("P_DOUBLE");
	         float expected = (float) -987.3434 ;
	         assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
	    } catch (Exception e) {
	        failed (e, "Unexpected Exception"+info);
	    }
	}	
   }


/**
getFloat() - Get a type that was registered as an DECIMAL.
**/
   public void Var042()
   {
     String info = " -- call getFloat against DECIMAL -- Changed October 2011";
        if(checkNamedParametersSupport()){
	    try {
		float p = csTypes_.getFloat ("P_DECIMAL_50");
	         float expected = (float) 54362.0;
	         assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
	    } catch (Exception e) {
	        failed (e, "Unexpected Exception"+info);
	    }
	}
   }


/**
getFloat() - Get a type that was registered as an NUMERIC.
**/
   public void Var043()
   {
     String info = " -- call getFloat against NUMERIC -- Changed October 2011";
        if(checkNamedParametersSupport()){
	    try {
		float p = csTypes_.getFloat ("P_NUMERIC_50");
	         float expected = (float) -1112.0;
	         assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
	    } catch (Exception e) {
	        failed (e, "Unexpected Exception"+info);
	    }
	}
   }


/**
getFloat() - Get a type that was registered as a CHAR.
**/
   public void Var044()
   {
        if(checkNamedParametersSupport()){
	    try {
		float p = csTypes_.getFloat ("P_CHAR_1");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }
/**
getFloat() - Get a type that was registered as a CHAR.
**/
   public void Var045()
   {
        if(checkNamedParametersSupport()){
	    try {
		float p = csTypes_.getFloat ("P_CHAR_50");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }
/**
getFloat() - Get a type that was registered as a VARCHAR.
**/
   public void Var046()
   {
        if(checkNamedParametersSupport()){
	    try {
		float p = csTypes_.getFloat ("P_VARCHAR_50");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }


/**
getFloat() - Get a type that was registered as a BINARY.
**/
   public void Var047()
   {
        if(checkNamedParametersSupport()){
	    try {
		float p = csTypes_.getFloat ("P_BINARY_20");
		failed ("Didn't throw SQLException "+p);
	    }catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }


/**
getFloat() - Get a type that was registered as a VARBINARY.
**/
   public void Var048()
   {
        if(checkNamedParametersSupport()){
	    try {
		float p = csTypes_.getFloat ("P_VARBINARY_20");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }


/**
getFloat() - Get a type that was registered as a DATE.
**/
   public void Var049()
   {
        if(checkNamedParametersSupport()){
	    try {
		float p = csTypes_.getFloat ("P_DATE");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }


/**
getFloat() - Get a type that was registered as a TIME.
**/
   public void Var050()
   {
        if(checkNamedParametersSupport()){
	    try {
		float p = csTypes_.getFloat ("P_TIME");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }


/**
getFloat() - Get a type that was registered as a TIMESTAMP.
**/
   public void Var051()
   {
        if(checkNamedParametersSupport()){
	    try {
		float p = csTypes_.getFloat ("P_TIMESTAMP");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }


/**
getFloat() - Get a type that was registered as an OTHER.
**/
   public void Var052()
   {
        if(checkNamedParametersSupport()){
	    if (checkLobSupport ()) {
		try {
		    float p = csTypes_.getFloat ("P_DATALINK");
		    failed ("Didn't throw SQLException "+p);
		}	catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
   }


/**
getFloat() - Get a type that was registered as a BLOB.
**/
   public void Var053()
   {
        if(checkNamedParametersSupport()){
	    if (checkLobSupport ()) {
		try {
		    float p = csTypes_.getFloat ("P_BLOB");
		    failed ("Didn't throw SQLException "+p);
		} catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
   }


/**
getFloat() - Get a type that was registered as a CLOB.
**/
   public void Var054()
   {
        if(checkNamedParametersSupport()){
	    if (checkLobSupport ()) {
		try {
		    float p = csTypes_.getFloat ("P_CLOB");
		    failed ("Didn't throw SQLException "+p);
		} catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
   }

/**
getFloat() - Get a type that was registered as a BIGINT.
**/
   public void Var055()
   {
        if(checkNamedParametersSupport()){
	    if (checkBigintSupport()) {
	      String info = " -- call getFloat against BIGINT -- Changed October 2011";
		try {
		    float p = csTypes_.getFloat ("P_BIGINT");                  
		         float expected = (float) 9.8766225E11;
		         assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
		} catch (Exception e) {
		        failed (e, "Unexpected Exception"+info);
		}
	    }
	}	
   }
   
   
   
   /**
    * getFloat () - Get a type that was registered as a BOOLEAN.
    **/
   public void Var056() {
     if (checkBooleanSupport()) {
       String info = " -- Test added 2021/01/06 for boolean";
       getTestSuccessful(csTypes_, "getFloat", 25, "1.0",info);
     }
   }


   /**
    * getFloat() - Get a type that was registered as a BOOLEAN that returns
    * false.
    **/
   public void Var057() {
     if (checkBooleanSupport()) { 
       String info = " -- Test added 2021/01/06 for boolean";
       getTestSuccessful(csTypesB_, "getFloat", 25, "0.0", info);
     }
   }
   
   /**
    * getFloat() - Get a type that was registered as a BOOLEAN that returns
    * null.
    **/
   public void Var058() {
     if (checkBooleanSupport()) {
       String info = " -- Test added 2021/01/06 for boolean";
            getTestSuccessful(csTypesNull_, "getFloat", 25, "0.0",info);
     }
   }

}
