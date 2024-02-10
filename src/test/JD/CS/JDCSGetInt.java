///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetInt.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetInt.java
//
// Classes:      JDCSGetInt
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDSetupProcedure;
import test.JDTestDriver;

import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable;



/**
Testcase JDCSGetInt.  This tests the following
method of the JDBC CallableStatement class:

<ul>
<li>getInt()
</ul>
**/
public class JDCSGetInt
extends JDCSGetTestcase
{



/**
Constructor.
**/
   public JDCSGetInt (AS400 systemObject,
                      Hashtable namesAndVars,
                      int runMode,
                      FileOutputStream fileOutputStream,
                      
                      String password)
   {
      super (systemObject, "JDCSGetInt",
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
getInt() - Get parameter -1.
**/
   public void Var001()
   {
      try {
         csTypes_.execute ();
         int p = csTypes_.getInt (-1);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getInt() - Get parameter 0.
**/
   public void Var002()
   {
      try {
         int p = csTypes_.getInt (0);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getInt() - Use a parameter that is too big.
**/
   public void Var003()
   {
      try {
         int p = csTypes_.getInt (35);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getInt() - Get a parameter when there are no parameters.
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
         int p = cs.getInt (1);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      } 

      if (c != null) try { c.close(); } catch (Exception e) {} 
   }



/**
getInt() - Get a parameter that was not registered.
**/
   public void Var005()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
         cs.execute ();
         int p = cs.getInt (2);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getInt() - Get a parameter when the statement has not been
executed.
**/
   public void Var006()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
         cs.registerOutParameter (2, Types.INTEGER);
         int p = cs.getInt (2);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getInt() - Get a parameter when the statement is closed.
**/
   public void Var007()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
         cs.registerOutParameter (2, Types.INTEGER);
         cs.execute ();
         cs.close ();
         int p = cs.getInt (2);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getInt() - Get an IN parameter that was correctly registered.
**/
   public void Var008()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESIN, supportedFeatures_);
         JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESIN,
                                              supportedFeatures_);
         cs.registerOutParameter (2, Types.INTEGER);
         cs.execute ();
         int p = cs.getInt (2);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }


/**
getInt() - Get an INOUT parameter, where the OUT parameter is
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
         cs.registerOutParameter (2, Types.INTEGER);
         cs.execute ();
         int p = cs.getInt (2);
         assertCondition (p == -102);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }


/**
getInt() - Get a type that was registered as a SMALLINT.
**/
   public void Var010()
   {
     String info = " -- call getInt against SMALLINT -- Changed October 2011";

     // As of 10/3/2011 for the toolbox driver, it is now possible to get an integer
     // from a parameter registered as smallint.
     // Note: JCC does not throw exception either.
     // Native JDBC will now need to change.
     //

      try {
         int p = csTypes_.getInt (1);
         int expected = 123; 
         assertCondition(p == expected, "got "+p+" sb "+expected + info); 
      } catch (Exception e) {
        failed (e, "Unexpected Exception"+info);
      }
   }



/**
getInt() - Get a type that was registered as an INTEGER.
**/
   public void Var011()
   {
      try {
         int p = csTypes_.getInt (2);                  
         assertCondition (p == -456);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }

   }



/**
getInt() - Get a type that was registered as an REAL.
**/
   public void Var012()
   {
     String info = " -- call getInt against REAL -- Changed October 2011";
      try {
         int p = csTypes_.getInt (3);
         int expected = 789; 
         assertCondition(p == expected, "got "+p+" sb "+expected + info); 
      } catch (Exception e) {
        failed (e, "Unexpected Exception"+info);
      }
   }



/**
getInt() - Get a type that was registered as an FLOAT.
**/
   public void Var013()
   {
     String info = " -- call getInt against FLOAT -- Changed October 2011";
      try {
         int p = csTypes_.getInt (4);
         int expected = 253; 
         assertCondition(p == expected, "got "+p+" sb "+expected + info); 
      } catch (Exception e) {
        failed (e, "Unexpected Exception"+info);
      }
   }



/**
getInt() - Get a type that was registered as an DOUBLE.
**/
   public void Var014()
   {
     String info = " -- call getInt against DOUBLE -- Changed October 2011";
      try {
         int p = csTypes_.getInt (5);
         int expected = -987; 
         assertCondition(p == expected, "got "+p+" sb "+expected + info); 
      } catch (Exception e) {
        failed (e, "Unexpected Exception"+info);
      }
   }



/**
getInt() - Get a type that was registered as an DECIMAL.
**/
   public void Var015()
   {
     String info = " -- call getInt against DECIMAL -- Changed October 2011";
      try {
         int p = csTypes_.getInt (7);
         int expected = -94732; 
         assertCondition(p == expected, "got "+p+" sb "+expected + info); 
      } catch (Exception e) {
        failed (e, "Unexpected Exception"+info);
      }
   }



/**
getInt() - Get a type that was registered as an NUMERIC.
**/
   public void Var016()
   {
     String info = " -- call getInt against NUMERIC -- Changed October 2011";
      try {
         int p = csTypes_.getInt (9);
         int expected = 19; 
         assertCondition(p == expected, "got "+p+" sb "+expected + info); 
      } catch (Exception e) {
        failed (e, "Unexpected Exception"+info);
      }
   }



/**
getInt() - Get a type that was registered as a CHAR.
**/
   public void Var017()
   {
      try {
         int p = csTypes_.getInt (11);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getInt() - Get a type that was registered as a VARCHAR.
**/
   public void Var018()
   {
      try {
         int p = csTypes_.getInt (12);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getInt() - Get a type that was registered as a BINARY.
**/
   public void Var019()
   {
      try {
         int p = csTypes_.getInt (13);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getInt() - Get a type that was registered as a VARBINARY.
**/
   public void Var020()
   {
      try {
         int p = csTypes_.getInt (14);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getInt() - Get a type that was registered as a DATE.
**/
   public void Var021()
   {
      try {
         int p = csTypes_.getInt (15);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getInt() - Get a type that was registered as a TIME.
**/
   public void Var022()
   {
      try {
         int p = csTypes_.getInt (16);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getInt() - Get a type that was registered as a TIMESTAMP.
**/
   public void Var023()
   {
      try {
         int p = csTypes_.getInt (17);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getInt() - Get a type that was registered as an OTHER.
**/
   public void Var024()
   {
      if (checkLobSupport ()) {
         try {
            int p = csTypes_.getInt (18);
            failed ("Didn't throw SQLException "+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getInt() - Get a type that was registered as a BLOB.
**/
   public void Var025()
   {
      if (checkLobSupport ()) {
         try {
            int p = csTypes_.getInt (19);
            failed ("Didn't throw SQLException "+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getInt() - Get a type that was registered as a CLOB.
**/
   public void Var026()
   {
      if (checkLobSupport ()) {
         try {
            int p = csTypes_.getInt (20);
            failed ("Didn't throw SQLException "+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getInt() - Get a type that was registered as a BIGINT.
**/
   public void Var027()
   {
      if (checkBigintSupport()) {
        String info = " -- call getInt against BIGINT -- Changed October 2011";
         try {
            int p = csTypesB_.getInt (22);                  
            int expected = 21; 
            assertCondition(p == expected, "got "+p+" sb "+expected + info); 
         } catch (Exception e) {
           failed (e, "Unexpected Exception"+info);
         }
      }
   }



/**
getInt() - Get an INOUT parameter, where the OUT parameter is
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
         cs.registerOutParameter (2, Types.INTEGER);
         JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                              supportedFeatures_);
         cs.execute ();
         int p = cs.getInt (2);
         assertCondition (p == -102);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



// @E1A
/**
getInt() - Get the return value parameter when it is not registered.
**/
   public void Var029()
   {
       if (checkReturnValueSupport()) {
      try {
         CallableStatement cs = connection_.prepareCall("?=CALL " + JDSetupProcedure.STP_CSRV);
         cs.execute ();
         int p = cs.getInt (1);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
       }
   }



// @E1A
/**
getInt() - Get the return value parameter when it is registered.
**/
   public void Var030()
   {
     if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC doesn't handle return value parameter when not escaped"); 
        return; 
     }
       if (checkReturnValueSupport()) {
      try {
         CallableStatement cs = connection_.prepareCall("?=CALL " + JDSetupProcedure.STP_CSRV);
         cs.registerOutParameter(1, Types.INTEGER);
         cs.execute ();
         int p = cs.getInt (1);
         assertCondition (p == 1976, "p = "+ p + " and should equal 1976");
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
       }
   }

 
/**
getInt() -- Get an output parameter where the database output type isn't necessarily a integer
*/
   public void Var031()
   {
      try {
	  Statement stmt = connection_.createStatement();
	  
	  try {
	      stmt.executeUpdate("drop table "+JDCSTest.COLLECTION+".integer_tab");
	  } catch (Exception e) {}
	  try {
	      stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".integer_Proc"); 
	  } catch (Exception e) {}

	  stmt.executeUpdate("create table "+JDCSTest.COLLECTION+".integer_Tab(MAX_VAL NUMERIC, MIN_VAL NUMERIC, NULL_VAL NUMERIC )"); 

	  stmt.executeUpdate("create procedure "+JDCSTest.COLLECTION+".integer_Proc (out MAX_PARAM NUMERIC, out MIN_PARAM NUMERIC, out NULL_PARAM NUMERIC) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from "+JDCSTest.COLLECTION+".integer_Tab; end");


	  stmt.executeUpdate("insert into "+JDCSTest.COLLECTION+".integer_Tab values(32000,0,null)");
	  CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".integer_Proc(?,?,?)}");

		//register the output parameters
	  cstmt.registerOutParameter(1,java.sql.Types.INTEGER);
	  cstmt.registerOutParameter(2,java.sql.Types.INTEGER);
	  cstmt.registerOutParameter(3,java.sql.Types.INTEGER);

		//execute the procedure
	  cstmt.execute();

		//invoke getByte method
	  int xRetVal = cstmt.getInt(1);
	  int yRetVal = cstmt.getInt(2);
	  int zRetVal = cstmt.getInt(3);
	  
	  assertCondition(xRetVal == 32000 && yRetVal == 0 && zRetVal == 0); 

      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }

/**
getInt() -- Get an output parameter where the database output type isn't necessarily a integer
*/ 
   public void Var032()
   {
      try {
	  Statement stmt = connection_.createStatement();
	  
	  try {
	      stmt.executeUpdate("drop table "+JDCSTest.COLLECTION+".integer_tab");
	  } catch (Exception e) {}
	  try {
	      stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".integer_Proc"); 
	  } catch (Exception e) {}

	  stmt.executeUpdate("create table "+JDCSTest.COLLECTION+".integer_Tab(MAX_VAL VARCHAR(30), MIN_VAL VARCHAR(30), NULL_VAL VARCHAR(30) )"); 

	  stmt.executeUpdate("create procedure "+JDCSTest.COLLECTION+".integer_Proc (out MAX_PARAM VARCHAR(30), out MIN_PARAM VARCHAR(30), out NULL_PARAM VARCHAR(30)) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from "+JDCSTest.COLLECTION+".integer_Tab; end");


	  stmt.executeUpdate("insert into "+JDCSTest.COLLECTION+".integer_Tab values('32000','0',null)");
	  CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".integer_Proc(?,?,?)}");

		//register the output parameters
	  cstmt.registerOutParameter(1,java.sql.Types.INTEGER);
	  cstmt.registerOutParameter(2,java.sql.Types.INTEGER);
	  cstmt.registerOutParameter(3,java.sql.Types.INTEGER);

		//execute the procedure
	  cstmt.execute();

		//invoke getInt method
	  int xRetVal = cstmt.getInt(1);
	  int yRetVal = cstmt.getInt(2);
	  int zRetVal = cstmt.getInt(3);
	  
	  assertCondition(xRetVal == 32000 && yRetVal == 0 && zRetVal == 0); 

      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }

/**
getInt() -- Get an output parameter where the database output type isn't necessarily a integer, but value cannot be mapped 
*/
   public void Var033()
   {
      try {
	  Statement stmt = connection_.createStatement();
	  
	  try {
	      stmt.executeUpdate("drop table "+JDCSTest.COLLECTION+".integer_tab");
	  } catch (Exception e) {}
	  try {
	      stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".integer_Proc"); 
	  } catch (Exception e) {}

	  stmt.executeUpdate("create table "+JDCSTest.COLLECTION+".integer_Tab(MAX_VAL VARCHAR(30), MIN_VAL VARCHAR(30), NULL_VAL VARCHAR(30) )"); 

	  stmt.executeUpdate("create procedure "+JDCSTest.COLLECTION+".integer_Proc (out MAX_PARAM VARCHAR(30), out MIN_PARAM VARCHAR(30), out NULL_PARAM VARCHAR(30)) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from "+JDCSTest.COLLECTION+".integer_Tab; end");


	  stmt.executeUpdate("insert into "+JDCSTest.COLLECTION+".integer_Tab values('ISMALL','0',null)");
	  CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".integer_Proc(?,?,?)}");

		//register the output parameters
	  cstmt.registerOutParameter(1,java.sql.Types.INTEGER);
	  cstmt.registerOutParameter(2,java.sql.Types.INTEGER);
	  cstmt.registerOutParameter(3,java.sql.Types.INTEGER);

		//execute the procedure
	  cstmt.execute();

		//invoke getInt method
	  int xRetVal = cstmt.getInt(1);
	  int yRetVal = cstmt.getInt(2);
	  int zRetVal = cstmt.getInt(3);


	  failed("Didn't throw exception "+xRetVal+","+yRetVal+","+zRetVal); 

      } catch (Exception e) {
          //
          // For the native driver, this is a data type mismatch
          // 
	  assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }

   }
//  NAMED PARAMETER
/**
getInt() - TRY TO GET A NAMED PARAMETER THAT DOESN'T EXIST
**/
   public void Var034()
   {
       if(checkNamedParametersSupport()) {
	   try {
	       int p = csTypes_.getInt ("P_FAKE");
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }
/**
getInt() - Get a type that was registered as a SMALLINT.
**/
   public void Var035()
   {
        if(checkNamedParametersSupport()) {
          String info = " -- call getInt against SMALLINT -- Changed October 2011";
	    try {
		int p = csTypes_.getInt ("P_SMALLINT");
	         int expected = 123; 
	         assertCondition(p == expected, "got "+p+" sb "+expected + info); 
	    } catch (Exception e) {
	        failed (e, "Unexpected Exception"+info);
	    }
	}
   }

/**
getInt() - Get a type that was registered as an INTEGER.
**/
   public void Var036()
   {
        if(checkNamedParametersSupport()) {
	    try {
		int p = csTypes_.getInt ("P_INTEGER");                  
		assertCondition (p == -456);
	    } catch (Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }

	}
   }

/**
getInt() - Get a type that was registered as an REAL.
**/
   public void Var037()
   {
        if(checkNamedParametersSupport()) {
          String info = " -- call getInt against REAL -- Changed October 2011";
	    try {
		int p = csTypes_.getInt ("P_REAL");
	         int expected = 789; 
	         assertCondition(p == expected, "got "+p+" sb "+expected + info); 
	    } catch (Exception e) {
	        failed (e, "Unexpected Exception"+info);
	    }	
	}
   }

/**
getInt() - Get a type that was registered as an FLOAT.
**/
   public void Var038()
   {
        if(checkNamedParametersSupport()) {
          String info = " -- call getInt against FLOAT -- Changed October 2011";
	    try {
		int p = csTypes_.getInt ("P_FLOAT");
	         int expected = 253; 
	         assertCondition(p == expected, "got "+p+" sb "+expected + info); 
	    } catch (Exception e) {
	        failed (e, "Unexpected Exception"+info);
	    }
	}
   }

/**
getInt() - Get a type that was registered as an DOUBLE.
**/
   public void Var039()
   {
        if(checkNamedParametersSupport()) {
          String info = " -- call getInt against DOUBLE -- Changed October 2011";
	    try {
		int p = csTypes_.getInt ("P_DOUBLE");
	         int expected = -987; 
	         assertCondition(p == expected, "got "+p+" sb "+expected + info); 
	    } catch (Exception e) {
	        failed (e, "Unexpected Exception"+info);
	    }
	}
   }

/**
getInt() - Get a type that was registered as an DECIMAL.
**/
   public void Var040()
   {
       if(checkNamedParametersSupport()) {
         String info = " -- call getInt against DECIMAL -- Changed October 2011";
	   try {
	       int p = csTypes_.getInt ("P_DECIMAL_50");
	         int expected = 54362; 
	         assertCondition(p == expected, "got "+p+" sb "+expected + info); 
	   } catch (Exception e) {
	        failed (e, "Unexpected Exception"+info);
	   }
       }
   }

/**
getInt() - Get a type that was registered as an NUMERIC.
**/
   public void Var041()
   {
        if(checkNamedParametersSupport()) {
          String info = " -- call getInt against NUMERIC -- Changed October 2011";
	    try {
		int p = csTypes_.getInt ("P_NUMERIC_50");
	         int expected = -1112; 
	         assertCondition(p == expected, "got "+p+" sb "+expected + info); 
	    } catch (Exception e) {
	        failed (e, "Unexpected Exception"+info);
	    }
	}
   }

/**
getInt() - Get a type that was registered as a CHAR.
**/
   public void Var042()
   {
        if(checkNamedParametersSupport()) {
	    try {
		int p = csTypes_.getInt ("P_CHAR_1");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }

/**
getInt() - Get a type that was registered as a VARCHAR.
**/
   public void Var043()
   {
        if(checkNamedParametersSupport()) {
	    try {
		int p = csTypes_.getInt ("P_VARCHAR_50");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }

/**
getInt() - Get a type that was registered as a BINARY.
**/
   public void Var044()
   {
        if(checkNamedParametersSupport()) {
	    try {
		int p = csTypes_.getInt ("P_BINARY_20");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }

/**
getInt() - Get a type that was registered as a VARBINARY.
**/
   public void Var045()
   {
        if(checkNamedParametersSupport()) {
	    try {
		int p = csTypes_.getInt ("P_VARBINARY_20");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }

/**
getInt() - Get a type that was registered as a DATE.
**/
   public void Var046()
   {
        if(checkNamedParametersSupport()) {
	    try {
		int p = csTypes_.getInt ("P_DATE");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }

/**
getInt() - Get a type that was registered as a TIME.
**/
   public void Var047()
   {
        if(checkNamedParametersSupport()) {
	    try {
		int p = csTypes_.getInt ("P_TIME");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }

/**
getInt() - Get a type that was registered as a TIMESTAMP.
**/
   public void Var048()
   {
        if(checkNamedParametersSupport()) {
	    try {
		int p = csTypes_.getInt ("P_TIMESTAMP");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }

/**
getInt() - Get a type that was registered as an OTHER.
**/
   public void Var049()
   {
        if(checkNamedParametersSupport()) {
	    if (checkLobSupport ()) {	
		try {
		    int p = csTypes_.getInt ("P_DATALINK");
		    failed ("Didn't throw SQLException "+p);
		} catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
   }

/**
getInt() - Get a type that was registered as a BLOB.
**/
   public void Var050()
   {
        if(checkNamedParametersSupport()) {
	    if (checkLobSupport ()) {
		try {
		    int p = csTypes_.getInt ("P_BLOB");
		    failed ("Didn't throw SQLException "+p);
		}catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}	
	    }
	}
   }

/**
getInt() - Get a type that was registered as a CLOB.
**/
   public void Var051()
   {
        if(checkNamedParametersSupport()) {
	    if (checkLobSupport ()) {
		try {
		    int p = csTypes_.getInt ("P_CLOB");
		    failed ("Didn't throw SQLException "+p);
		} catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
   }

/**
getInt() - Get a type that was registered as a BIGINT.
**/
   public void Var052()
   {
        if(checkNamedParametersSupport()) {
	    if (checkBigintSupport()) {
	      String info = " -- call getInt against BIGINT -- Changed October 2011";
		try {
		    int p = csTypesB_.getInt ("P_BIGINT");                  
		    int expected = 21; 
		    assertCondition(p == expected, "got "+p+" sb "+expected + info); 
		} catch (Exception e) {
		        failed (e, "Unexpected Exception"+info);
		}
	    }
	}
   }
   
   /**
    * getInt () - Get a type that was registered as a BOOLEAN.
    **/
   public void Var053() {
     if (checkBooleanSupport()) {
       String info = " -- Test added 2021/01/06 for boolean";
       getTestSuccessful(csTypes_, "getInt", 25, "1",info);
     }
   }


   /**
    * getInt() - Get a type that was registered as a BOOLEAN that returns
    * false.
    **/
   public void Var054() {
     if (checkBooleanSupport()) { 
       String info = " -- Test added 2021/01/06 for boolean";
       getTestSuccessful(csTypesB_, "getInt", 25, "0", info);
     }
   }
   
   /**
    * getInt() - Get a type that was registered as a BOOLEAN that returns
    * null.
    **/
   public void Var055() {
     if (checkBooleanSupport()) {
       String info = " -- Test added 2021/01/06 for boolean";
            getTestSuccessful(csTypesNull_, "getInt", 25, "0",info);
     }
   }

   

}
