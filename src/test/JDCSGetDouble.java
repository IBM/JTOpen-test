///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetDouble.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetDouble.java
//
// Classes:      JDCSGetDouble
//
////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;
import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable;



/**
Testcase JDCSGetDouble.  This tests the following
method of the JDBC CallableStatement class:

<ul>
<li>getDouble()
</ul>
**/
public class JDCSGetDouble
extends JDCSGetTestcase
{



/**
Constructor.
**/
   public JDCSGetDouble (AS400 systemObject,
                         Hashtable namesAndVars,
                         int runMode,
                         FileOutputStream fileOutputStream,
                         
                         String password)
   {
      super (systemObject, "JDCSGetDouble",
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
getDouble() - Get parameter -1.
**/
   public void Var001()
   {
      try {
         csTypes_.execute ();
         double p = csTypes_.getDouble (-1);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getDouble() - Get parameter 0.
**/
   public void Var002()
   {
      try {
         double p = csTypes_.getDouble (0);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getDouble() - Use a parameter that is too big.
**/
   public void Var003()
   {
      try {
         double p = csTypes_.getDouble (35);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getDouble() - Get a parameter when there are no parameters.
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
         double p = cs.getDouble (1);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      } 
      if (c != null) try { c.close(); } catch (Exception e) {} 
   }



/**
getDouble() - Get a parameter that was not registered.
**/
   public void Var005()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
         cs.execute ();
         double p = cs.getDouble (5);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getDouble() - Get a parameter when the statement has not been
executed.
**/
   public void Var006()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
         cs.registerOutParameter (5, Types.DOUBLE);
         double p = cs.getDouble (5);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getDouble() - Get a parameter when the statement is closed.
**/
   public void Var007()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
         cs.registerOutParameter (5, Types.DOUBLE);
         cs.execute ();
         cs.close ();
         double p = cs.getDouble (5);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getDouble() - Get an IN parameter that was correctly registered.
**/
   public void Var008()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESIN, supportedFeatures_);
         JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESIN,
                                              supportedFeatures_);
         cs.registerOutParameter (5, Types.DOUBLE);
         cs.execute ();
         double p = cs.getDouble (5);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }


/**
getDouble() - Get an INOUT parameter, where the OUT parameter is
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
         cs.registerOutParameter (5, Types.DOUBLE);
         cs.execute ();
         double p = cs.getDouble (5);
         assertCondition (p == -49.04);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }


/**
getDouble() - Get a type that was registered as a SMALLINT.
**/
   public void Var010()
   {
     String info = " -- call getDouble against SMALL -- Changed October 2011";

     // As of 10/3/2011 for the toolbox driver, it is now possible to get a double
     // from a parameter registered as int.
     // Note: JCC does not throw exception either.
     // Native JDBC will now need to change.
     //

      try {
         double p = csTypes_.getDouble (1);
         double expected = 123.0;
         assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
      } catch (Exception e) {
        failed (e, "Unexpected Exception "+info);
      }
   }



/**
getDouble() - Get a type that was registered as an INTEGER.
**/
   public void Var011()
   {
     String info = " -- call getDouble against INTEGER -- Changed October 2011";
     
      try {
         double p = csTypes_.getDouble (2);                  
         double expected = -456.0;
         assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
      } catch (Exception e) {
        failed (e, "Unexpected Exception "+info);
      }
   }



/**
getDouble() - Get a type that was registered as an REAL.
**/
   public void Var012()
   {
     String info = " -- call getDouble against REAL -- Changed October 2011";
     try {
         double p = csTypes_.getDouble (3);
         double expected = 789.5399780273438;
         assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
      } catch (Exception e) {
        failed (e, "Unexpected Exception "+info);
      }
   }



/**
getDouble() - Get a type that was registered as an FLOAT.
**/
   public void Var013()
   {
      try {
         double p = csTypes_.getDouble (4);
         assertCondition (p == 253.1027);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
getDouble() - Get a type that was registered as an DOUBLE.
**/
   public void Var014()
   {
      try {
         double p = csTypes_.getDouble (5);
         assertCondition (p == -987.3434);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
getDouble() - Get a type that was registered as an DECIMAL.
**/
   public void Var015()
   {
     String info = " -- call getDouble against DECIMAL -- Changed October 2011";
      try {
         double p = csTypes_.getDouble (7);
         double expected = -94732.12345;
         assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
      } catch (Exception e) {
        failed (e, "Unexpected Exception "+info);
      }
   }



/**
getDouble() - Get a type that was registered as an NUMERIC.
**/
   public void Var016()
   {
     String info = " -- call getDouble against NUMERIC -- Changed October 2011";
      try {
         double p = csTypes_.getDouble (9);
         double expected = 19.98765;
         assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
      } catch (Exception e) {
        failed (e, "Unexpected Exception "+info);
      }
   }



/**
getDouble() - Get a type that was registered as a CHAR.
**/
   public void Var017()
   {
      try {
         double p = csTypes_.getDouble (11);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getDouble() - Get a type that was registered as a VARCHAR.
**/
   public void Var018()
   {
      try {
         double p = csTypes_.getDouble (12);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getDouble() - Get a type that was registered as a BINARY.
**/
   public void Var019()
   {
      try {
         double p = csTypes_.getDouble (13);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getDouble() - Get a type that was registered as a VARBINARY.
**/
   public void Var020()
   {
      try {
         double p = csTypes_.getDouble (14);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getDouble() - Get a type that was registered as a DATE.
**/
   public void Var021()
   {
      try {
         double p = csTypes_.getDouble (15);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getDouble() - Get a type that was registered as a TIME.
**/
   public void Var022()
   {
      try {
         double p = csTypes_.getDouble (16);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getDouble() - Get a type that was registered as a TIMESTAMP.
**/
   public void Var023()
   {
      try {
         double p = csTypes_.getDouble (17);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getDouble() - Get a type that was registered as an OTHER.
**/
   public void Var024()
   {
      if (checkLobSupport ()) {
         try {
            double p = csTypes_.getDouble (18);
            failed ("Didn't throw SQLException "+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getDouble() - Get a type that was registered as a BLOB.
**/
   public void Var025()
   {
      if (checkLobSupport ()) {
         try {
            double p = csTypes_.getDouble (19);
            failed ("Didn't throw SQLException "+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getDouble() - Get a type that was registered as a CLOB.
**/
   public void Var026()
   {
      if (checkLobSupport ()) {
         try {
            double p = csTypes_.getDouble (20);
            failed ("Didn't throw SQLException "+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getDouble() - Get a type that was registered as a BIGINT.
**/
   public void Var027()
   {
      if (checkBigintSupport()) {
        String info = " -- call getDouble against BIGINT -- Changed October 2011";
         try {
            double p = csTypes_.getDouble (22);                  
            double expected = 9.87662234567E11;
            assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
         } catch (Exception e) {
           failed (e, "Unexpected Exception "+info);
         }
      }
   }



/**
getDouble() - Get an INOUT parameter, where the OUT parameter is
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
         cs.registerOutParameter (5, Types.DOUBLE);
         JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                              supportedFeatures_);
         cs.execute ();
         double p = cs.getDouble (5);
         assertCondition (p == -49.04);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }

/**
getDouble() -- Get an output parameter where the database output type isn't necessarily a double 
*/
   public void Var029()
   {
      try {
	  Statement stmt = connection_.createStatement();
	  
	  try {
	      stmt.executeUpdate("drop table "+JDCSTest.COLLECTION+".double_tab");
	  } catch (Exception e) {}
	  try {
	      stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".double_proc"); 
	  } catch (Exception e) {}

	  stmt.executeUpdate("create table "+JDCSTest.COLLECTION+".double_tab(MAX_VAL NUMERIC, MIN_VAL NUMERIC, NULL_VAL NUMERIC )"); 

	  stmt.executeUpdate("create procedure "+JDCSTest.COLLECTION+".double_proc (out MAX_PARAM NUMERIC, out MIN_PARAM NUMERIC, out NULL_PARAM NUMERIC) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from "+JDCSTest.COLLECTION+".double_tab; end");

	  double maxDouble = 99999.0;
	  stmt.executeUpdate("insert into "+JDCSTest.COLLECTION+".double_tab values("+maxDouble+",0,null)");
	  CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".double_proc(?,?,?)}");

		//register the output parameters
	  cstmt.registerOutParameter(1,java.sql.Types.DOUBLE);
	  cstmt.registerOutParameter(2,java.sql.Types.DOUBLE);
	  cstmt.registerOutParameter(3,java.sql.Types.DOUBLE);

		//execute the procedure
	  cstmt.execute();

		//invoke getDouble method
	  double xRetVal = cstmt.getDouble(1);
	  double yRetVal = cstmt.getDouble(2);
	  double zRetVal = cstmt.getDouble(3);
	  
	  assertCondition(xRetVal == maxDouble && yRetVal == 0 && zRetVal == 0); 

      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }

/**
getDouble() -- Get an output parameter where the database output type isn't necessarily a double
*/
   public void Var030()
   {
      try {
	  Statement stmt = connection_.createStatement();
	  
	  try {
	      stmt.executeUpdate("drop table "+JDCSTest.COLLECTION+".double_tab");
	  } catch (Exception e) {}
	  try {
	      stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".double_proc"); 
	  } catch (Exception e) {}

	  stmt.executeUpdate("create table "+JDCSTest.COLLECTION+".double_tab(MAX_VAL VARCHAR(30), MIN_VAL VARCHAR(30), NULL_VAL VARCHAR(30) )"); 

	  stmt.executeUpdate("create procedure "+JDCSTest.COLLECTION+".double_proc (out MAX_PARAM VARCHAR(30), out MIN_PARAM VARCHAR(30), out NULL_PARAM VARCHAR(30)) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from "+JDCSTest.COLLECTION+".double_tab; end");

	  double maxDouble = 1.38972E+30;
	  stmt.executeUpdate("insert into "+JDCSTest.COLLECTION+".double_tab values('"+maxDouble+"','0',null)");
	  CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".double_proc(?,?,?)}");

		//register the output parameters
	  cstmt.registerOutParameter(1,java.sql.Types.DOUBLE);
	  cstmt.registerOutParameter(2,java.sql.Types.DOUBLE);
	  cstmt.registerOutParameter(3,java.sql.Types.DOUBLE);

		//execute the procedure
	  cstmt.execute();

		//invoke getDouble method
	  double xRetVal = cstmt.getDouble(1);
	  double yRetVal = cstmt.getDouble(2);
	  double zRetVal = cstmt.getDouble(3);
	  
	  assertCondition(xRetVal == maxDouble && yRetVal == 0 && zRetVal == 0); 

      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }

/**
getDouble() -- Get an output parameter where the database output type isn't necessarily a double, but the value can't be converted 
*/
   public void Var031()
   {
      try {
	  Statement stmt = connection_.createStatement();
	  
	  try {
	      stmt.executeUpdate("drop table "+JDCSTest.COLLECTION+".double_tab");
	  } catch (Exception e) {}
	  try {
	      stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".double_proc"); 
	  } catch (Exception e) {}

	  stmt.executeUpdate("create table "+JDCSTest.COLLECTION+".double_tab(MAX_VAL VARCHAR(30), MIN_VAL VARCHAR(30), NULL_VAL VARCHAR(30) )"); 

	  stmt.executeUpdate("create procedure "+JDCSTest.COLLECTION+".double_proc (out MAX_PARAM VARCHAR(30), out MIN_PARAM VARCHAR(30), out NULL_PARAM VARCHAR(30)) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from "+JDCSTest.COLLECTION+".double_tab; end");

	  double maxDouble = 1.38972E+30;
	  stmt.executeUpdate("insert into "+JDCSTest.COLLECTION+".double_tab values('BIGNUMBER','0',null)");
	  CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".double_proc(?,?,?)}");

		//register the output parameters
	  cstmt.registerOutParameter(1,java.sql.Types.DOUBLE);
	  cstmt.registerOutParameter(2,java.sql.Types.DOUBLE);
	  cstmt.registerOutParameter(3,java.sql.Types.DOUBLE);

		//execute the procedure
	  cstmt.execute();

		//invoke getDouble method
	  double xRetVal = cstmt.getDouble(1);
	  double yRetVal = cstmt.getDouble(2);
	  double zRetVal = cstmt.getDouble(3);
	  failed("Didn't throw exception "+xRetVal+","+yRetVal+","+zRetVal+" "+maxDouble);


      } catch (Exception e) {
          //
          // For the native driver, this is a data type mismatch
          // 
	  assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }

   }

// --- NAMED PARAMETERS
/**
getDouble() - Get a type that was registered as a SMALLINT.
**/
   public void Var032()
   {
        if(checkNamedParametersSupport())
	{
          String info = " -- call getDouble against SMALLINT -- Changed October 2011";
	    try {
		double p = csTypes_.getDouble ("P_SMALLINT");
	            double expected = 123.0;
	            assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
	    } catch (Exception e) {
	        failed (e, "Unexpected Exception "+info);
	    }
	}
   }

/**
getDouble() - Get a type that was registered as an INTEGER.
**/
   public void Var033()
   {
        if(checkNamedParametersSupport())
        {
          String info = " -- call getDouble against INTEGER -- Changed October 2011";
	    try {
		double p = csTypes_.getDouble ("P_INTEGER");                  
                double expected = -456.0; 
                assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
	    } catch (Exception e) {
	        failed (e, "Unexpected Exception "+info);
	    }
	}
   }

/**
getDouble() - Get a type that was registered as an REAL.
**/
   public void Var034()
   {
        if(checkNamedParametersSupport())
        {
          String info = " -- call getDouble against REAL -- Changed October 2011";
	    try {
		double p = csTypes_.getDouble ("P_REAL");
                double expected = 789.5399780273438;
                assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
	    } catch (Exception e) {
	        failed (e, "Unexpected Exception "+info);
	    }
	}
   }

/**
getDouble() - Get a type that was registered as an FLOAT.
**/
   public void Var035()
   {
        if(checkNamedParametersSupport())
        {
	    try {
		double p = csTypes_.getDouble ("P_FLOAT");
		assertCondition (p == 253.1027);
	    } catch (Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
   }

/**
getDouble() - Get a type that was registered as an DOUBLE.
**/
   public void Var036()
   {
        if(checkNamedParametersSupport())
        {
	    try {
		double p = csTypes_.getDouble ("P_DOUBLE");
		assertCondition (p == -987.3434);
	    } catch (Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
   }

/**
getDouble() - Get a type that was registered as an DECIMAL.
**/
   public void Var037()
   {
        if(checkNamedParametersSupport())
        {	
          String info = " -- call getDouble against DECIMAL -- Changed October 2011";
	    try {
		double p = csTypes_.getDouble ("P_DECIMAL_50");
                double expected = 54362.0 ;
                assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
	    } catch (Exception e) {
	        failed (e, "Unexpected Exception "+info);
	    }
	}
   }
/**
getDouble() - Get a type that was registered as an DECIMAL.
**/
   public void Var038()
   {
        if(checkNamedParametersSupport())
        {
          String info = " -- call getDouble against DECIMAL -- Changed October 2011";
	    try {
		double p = csTypes_.getDouble ("P_DECIMAL_105");
                double expected = -94732.12345 ;
                assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
	    } catch (Exception e) {
	        failed (e, "Unexpected Exception "+info);
	    }
	}
   }
/**
getDouble() - Get a type that was registered as an NUMERIC.
**/
   public void Var039()
   {
        if(checkNamedParametersSupport())
        {
          String info = " -- call getDouble against NUMERIC -- Changed October 2011";
	    try {
		double p = csTypes_.getDouble ("P_NUMERIC_50");
                double expected = -1112.0;
                assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
	    } catch (Exception e) {
	        failed (e, "Unexpected Exception "+info);
	    }
	}
   }
/**
getDouble() - Get a type that was registered as an NUMERIC.
**/
   public void Var040()
   {
        if(checkNamedParametersSupport())
        {
          String info = " -- call getDouble against NUMERIC -- Changed October 2011";
	    try {
		double p = csTypes_.getDouble ("P_NUMERIC_105");
                double expected = 19.98765;
                assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
	    } catch (Exception e) {
	        failed (e, "Unexpected Exception "+info);
	    }
	}
   }
/**
getDouble() - Get a type that was registered as a CHAR.
**/
   public void Var041()
   {	
        if(checkNamedParametersSupport())
        {
	    try {
		double p = csTypes_.getDouble ("P_CHAR_1");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }		
	}
   }
/**
getDouble() - Get a type that was registered as a CHAR.
**/
   public void Var042()
   {
        if(checkNamedParametersSupport())
        {
	    try {
		double p = csTypes_.getDouble ("P_CHAR_50");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }
/**
getDouble() - Get a type that was registered as a VARCHAR.
**/
   public void Var043()
   {
        if(checkNamedParametersSupport())
        {
	    try {
		double p = csTypes_.getDouble ("P_VARCHAR_50")	;
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }

/**
getDouble() - Get a type that was registered as a BINARY.
**/
   public void Var044()
   {
        if(checkNamedParametersSupport())
        {
	    try {
		double p = csTypes_.getDouble ("P_BINARY_20");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}	
   }

/**
getDouble() - Get a type that was registered as a VARBINARY.
**/
   public void Var045()
   {
        if(checkNamedParametersSupport())
        {
	    try {
		double p = csTypes_.getDouble ("P_VARBINARY_20");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }	
	}	
   }

/**
getDouble() - Get a type that was registered as a DATE.
**/
   public void Var046()
   {
        if(checkNamedParametersSupport())
        {
	    try {
		double p = csTypes_.getDouble ("P_DATE");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }

/**
getDouble() - Get a type that was registered as a TIME.
**/
   public void Var047()
   {
        if(checkNamedParametersSupport())
        {
	    try {
		double p = csTypes_.getDouble ("P_TIME");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }

/**
getDouble() - Get a type that was registered as a TIMESTAMP.
**/
   public void Var048()
   {
        if(checkNamedParametersSupport())
        {
	    try {
		double p = csTypes_.getDouble ("P_TIMESTAMP");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }

/**
getDouble() - Get a type that was registered as an OTHER.
**/
   public void Var049()
   {
        if(checkNamedParametersSupport())
        {
	    if (checkLobSupport ()) {
		try {
		    double p = csTypes_.getDouble ("P_DATALINK");
		    failed ("Didn't throw SQLException "+p);
		}catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
   }

/**
getDouble() - Get a type that was registered as a BLOB.
**/
   public void Var050()
   {
        if(checkNamedParametersSupport())
        {
	    if (checkLobSupport ()) {
		try {
		    double p = csTypes_.getDouble ("P_BLOB");
		    failed ("Didn't throw SQLException "+p);
		} catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
   }

/**
getDouble() - Get a type that was registered as a CLOB.
**/
   public void Var051()
   {
        if(checkNamedParametersSupport())
        {
	    if (checkLobSupport ()) {
		try {
		    double p = csTypes_.getDouble ("P_CLOB");
		    failed ("Didn't throw SQLException "+p);
		} catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
   }
/**
getDouble() - Get a type that was registered as a BIGINT.
**/
   public void Var052()
   {
        if(checkNamedParametersSupport())
        {
	    if (checkBigintSupport()) {
	      String info = " -- call getDouble against BIGINT -- Changed October 2011";
		try {
		    double p = csTypes_.getDouble ("P_BIGINT");                  
	                double expected = 9.87662234567E11;
	                assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
		} catch (Exception e) {
		        failed (e, "Unexpected Exception "+info);
		}
	    }
	}
   }
   
   
   /**
    * getDouble () - Get a type that was registered as a BOOLEAN.
    **/
   public void Var053() {
     if (checkBooleanSupport()) {
       String info = " -- Test added 2021/01/06 for boolean";
       getTestSuccessful(csTypes_, "getDouble", 25, "1.0",info);
     }
   }


   /**
    * getDouble() - Get a type that was registered as a BOOLEAN that returns
    * false.
    **/
   public void Var054() {
     if (checkBooleanSupport()) { 
       String info = " -- Test added 2021/01/06 for boolean";
       getTestSuccessful(csTypesB_, "getDouble", 25, "0.0", info);
     }
   }
   
   /**
    * getBoolean() - Get a type that was registered as a BOOLEAN that returns
    * null.
    **/
   public void Var055() {
     if (checkBooleanSupport()) {
       String info = " -- Test added 2021/01/06 for boolean";
            getTestSuccessful(csTypesNull_, "getDouble", 25, "0.0",info);
     }
   }

   
}
