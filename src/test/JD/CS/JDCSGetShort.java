///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetShort.java
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
// File Name:    JDCSGetShort.java
//
// Classes:      JDCSGetShort
//
////////////////////////////////////////////////////////////////////////
//
//
//
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDSetupProcedure;

import java.awt.TextArea;
import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable;



/**
Testcase JDCSGetShort.  This tests the following
method of the JDBC CallableStatement class:

<ul>
<li>getShort()
</ul>
**/
public class JDCSGetShort
extends JDCSGetTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSGetShort";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCSTest.main(newArgs); 
   }



 
/**
Constructor.
**/
   public JDCSGetShort (AS400 systemObject,
                        Hashtable namesAndVars,
                        int runMode,
                        FileOutputStream fileOutputStream,
                        
                        String password)
   {
      super (systemObject, "JDCSGetShort",
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
getShort() - Get parameter -1.
**/
   public void Var001()
   {
      try {
         csTypes_.execute ();
         short p = csTypes_.getShort (-1);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getShort() - Get parameter 0.
**/
   public void Var002()
   {
      try {
         short p = csTypes_.getShort (0);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getShort() - Use a parameter that is too big.
**/
   public void Var003()
   {
      try {
         short p = csTypes_.getShort (35);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getShort() - Get a parameter when there are no parameters.
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
         short p = cs.getShort (1);
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
getShort() - Get a parameter that was not registered.
**/
   public void Var005()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
         cs.execute ();
         short p = cs.getShort (1);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getShort() - Get a parameter when the statement has not been
executed.
**/
   public void Var006()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
         cs.registerOutParameter (1, Types.SMALLINT);
         short p = cs.getShort (1);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getShort() - Get a parameter when the statement is closed.
**/
   public void Var007()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
         cs.registerOutParameter (1, Types.SMALLINT);
         cs.execute ();
         cs.close ();
         short p = cs.getShort (1);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getShort() - Get an IN parameter that was correctly registered.
**/
   public void Var008()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESIN, supportedFeatures_);
         JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESIN,
                                              supportedFeatures_);
         cs.registerOutParameter (1, Types.SMALLINT);
         cs.execute ();
         short p = cs.getShort (1);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }


/**
getShort() - Get an INOUT parameter, where the OUT parameter is
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
         cs.registerOutParameter (1, Types.SMALLINT);
         cs.execute ();
         short p = cs.getShort (1);
         assertCondition (p == 24);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }


/**
getShort() - Get a type that was registered as a SMALLINT.
**/
   public void Var010()
   {
      try {
         short p = csTypes_.getShort (1);
         assertCondition (p == 123);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
getShort() - Get a type that was registered as an INTEGER.
**/
   public void Var011()
   {
     String info = " -- call getShort against INTEGER -- Changed October 2011";

     // As of 10/3/2011 for the toolbox driver, it is now possible to get a short
     // from a parameter registered as smallint.
     // Note: JCC does not throw exception either.
     // Native JDBC will now need to change.
     //

      try {
         short p = csTypes_.getShort (2);
         short expected = -456; 
         assertCondition(p == expected, " got "+p+" expected "+expected+info); 
      } catch (Exception e) {
        failed (e, "Unexpected Exception"+info);
      }
   }



/**
getShort() - Get a type that was registered as an REAL.
**/
   public void Var012()
   {
     String info = " -- call getShort against REAL -- Changed October 2011";
      try {
         short p = csTypes_.getShort (3);
         short expected = 789; 
         assertCondition(p == expected, " got "+p+" expected "+expected+info); 
      } catch (Exception e) {
        failed (e, "Unexpected Exception"+info);
      }
   }



/**
getShort() - Get a type that was registered as an FLOAT.
**/
   public void Var013()
   {
     String info = " -- call getShort against FLOAT -- Changed October 2011";
      try {
         short p = csTypes_.getShort (4);
         short expected = 253; 
         assertCondition(p == expected, " got "+p+" expected "+expected+info); 
      } catch (Exception e) {
        failed (e, "Unexpected Exception"+info);
      }
   }



/**
getShort() - Get a type that was registered as an DOUBLE.
**/
   public void Var014()
   {
     String info = " -- call getShort against DOUBLE -- Changed October 2011";
      try {
         short p = csTypes_.getShort (5);
         short expected = -987; 
         assertCondition(p == expected, " got "+p+" expected "+expected+info); 
      } catch (Exception e) {
        failed (e, "Unexpected Exception"+info);
      }
   }



/**
getShort() - Get a type that was registered as an DECIMAL.
**/
   public void Var015()
   {
     String info = " -- call getShort against DECIMAL -- Changed October 2011";
      try {
         short p = csTypesB_.getShort (7);
         short expected = 7; 
         assertCondition(p == expected, " got "+p+" expected "+expected+info); 
      } catch (Exception e) {
        failed (e, "Unexpected Exception"+info);
      }
   }



/**
getShort() - Get a type that was registered as an NUMERIC.
**/
   public void Var016()
   {
     String info = " -- call getShort against NUMERIC -- Changed October 2011";
      try {
         short p = csTypes_.getShort (9);
         short expected = 19; 
         assertCondition(p == expected, " got "+p+" expected "+expected+info); 
      } catch (Exception e) {
        failed (e, "Unexpected Exception"+info);
      }
   }



/**
getShort() - Get a type that was registered as a CHAR.
**/
   public void Var017()
   {
      try {
         short p = csTypes_.getShort (11);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getShort() - Get a type that was registered as a VARCHAR.
**/
   public void Var018()
   {
      try {
         short p = csTypes_.getShort (12);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getShort() - Get a type that was registered as a BINARY.
**/
   public void Var019()
   {
      try {
         short p = csTypes_.getShort (13);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getShort() - Get a type that was registered as a VARBINARY.
**/
   public void Var020()
   {
      try {
         short p = csTypes_.getShort (14);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getShort() - Get a type that was registered as a DATE.
**/
   public void Var021()
   {
      try {
         short p = csTypes_.getShort (15);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getShort() - Get a type that was registered as a TIME.
**/
   public void Var022()
   {
      try {
         short p = csTypes_.getShort (16);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getShort() - Get a type that was registered as a TIMESTAMP.
**/
   public void Var023()
   {
      try {
         short p = csTypes_.getShort (17);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getShort() - Get a type that was registered as an OTHER.
**/
   public void Var024()
   {
      if (checkLobSupport ()) {
         try {
            short p = csTypes_.getShort (18);
            failed ("Didn't throw SQLException "+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getShort() - Get a type that was registered as a BLOB.
**/
   public void Var025()
   {
      if (checkLobSupport ()) {
         try {
            short p = csTypes_.getShort (19);
            failed ("Didn't throw SQLException "+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getShort() - Get a type that was registered as a CLOB.
**/
   public void Var026()
   {
      if (checkLobSupport ()) {
         try {
            short p = csTypes_.getShort (20);
            failed ("Didn't throw SQLException "+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getShort() - Get a type that was registered as a BIGINT.
**/
   public void Var027()
   {
      if (checkBigintSupport()) {
         String info = " -- call getShort against BIGINT -- Changed October 2011";
         try {
            short p = csTypesB_.getShort (22);
            short expected = 21; 
            assertCondition(p == expected, " got "+p+" expected "+expected+info); 
         } catch (Exception e) {
           failed (e, "Unexpected Exception"+info);
         }

      }
   }




/**
getShort() - Get an INOUT parameter, where the OUT parameter is
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
         cs.registerOutParameter (1, Types.SMALLINT);
         JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                              supportedFeatures_);
         cs.execute ();
         short p = cs.getShort (1);
         assertCondition (p == 24);
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }

/**
getShort() -- Get an output parameter where the database output type isn't necessarily a short
*/
   public void Var029()
   {
      try {
	  Statement stmt = connection_.createStatement();
	  
	  try {
	      stmt.executeUpdate("drop table "+JDCSTest.COLLECTION+".smallint_tab");
	  } catch (Exception e) {}
	  try {
	      stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".smallint_Proc"); 
	  } catch (Exception e) {}

	  stmt.executeUpdate("create table "+JDCSTest.COLLECTION+".smallint_Tab(MAX_VAL NUMERIC, MIN_VAL NUMERIC, NULL_VAL NUMERIC )"); 

	  stmt.executeUpdate("create procedure "+JDCSTest.COLLECTION+".smallint_Proc (out MAX_PARAM NUMERIC, out MIN_PARAM NUMERIC, out NULL_PARAM NUMERIC) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from "+JDCSTest.COLLECTION+".smallint_Tab; end");


	  stmt.executeUpdate("insert into "+JDCSTest.COLLECTION+".smallint_Tab values(32000,0,null)");
	  CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".smallint_Proc(?,?,?)}");

		//register the output parameters
	  cstmt.registerOutParameter(1,java.sql.Types.SMALLINT);
	  cstmt.registerOutParameter(2,java.sql.Types.SMALLINT);
	  cstmt.registerOutParameter(3,java.sql.Types.SMALLINT);

		//execute the procedure
	  cstmt.execute();

		//invoke getByte method
	  short xRetVal = cstmt.getShort(1);
	  short yRetVal = cstmt.getShort(2);
	  short zRetVal = cstmt.getShort(3);
	  
	  assertCondition(xRetVal == 32000 && yRetVal == 0 && zRetVal == 0); 

      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }

/**
getShort() -- Get an output parameter where the database output type isn't necessarily a short
*/ 
   public void Var030()
   {
      try {
	  Statement stmt = connection_.createStatement();
	  
	  try {
	      stmt.executeUpdate("drop table "+JDCSTest.COLLECTION+".smallint_tab");
	  } catch (Exception e) {}
	  try {
	      stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".smallint_Proc"); 
	  } catch (Exception e) {}

	  stmt.executeUpdate("create table "+JDCSTest.COLLECTION+".smallint_Tab(MAX_VAL VARCHAR(30), MIN_VAL VARCHAR(30), NULL_VAL VARCHAR(30) )"); 

	  stmt.executeUpdate("create procedure "+JDCSTest.COLLECTION+".smallint_Proc (out MAX_PARAM VARCHAR(30), out MIN_PARAM VARCHAR(30), out NULL_PARAM VARCHAR(30)) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from "+JDCSTest.COLLECTION+".smallint_Tab; end");


	  stmt.executeUpdate("insert into "+JDCSTest.COLLECTION+".smallint_Tab values('32000','0',null)");
	  CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".smallint_Proc(?,?,?)}");

		//register the output parameters
	  cstmt.registerOutParameter(1,java.sql.Types.SMALLINT);
	  cstmt.registerOutParameter(2,java.sql.Types.SMALLINT);
	  cstmt.registerOutParameter(3,java.sql.Types.SMALLINT);

		//execute the procedure
	  cstmt.execute();

		//invoke getShort method
	  short xRetVal = cstmt.getShort(1);
	  short yRetVal = cstmt.getShort(2);
	  short zRetVal = cstmt.getShort(3);
	  
	  assertCondition(xRetVal == 32000 && yRetVal == 0 && zRetVal == 0); 

      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }

/**
getShort() -- Get an output parameter where the database output type isn't necessarily a short, but value cannot be mapped 
*/
   public void Var031()
   {
      try {
	  Statement stmt = connection_.createStatement();
	  
	  try {
	      stmt.executeUpdate("drop table "+JDCSTest.COLLECTION+".smallint_tab");
	  } catch (Exception e) {}
	  try {
	      stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".smallint_Proc"); 
	  } catch (Exception e) {}

	  stmt.executeUpdate("create table "+JDCSTest.COLLECTION+".smallint_Tab(MAX_VAL VARCHAR(30), MIN_VAL VARCHAR(30), NULL_VAL VARCHAR(30) )"); 

	  stmt.executeUpdate("create procedure "+JDCSTest.COLLECTION+".smallint_Proc (out MAX_PARAM VARCHAR(30), out MIN_PARAM VARCHAR(30), out NULL_PARAM VARCHAR(30)) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from "+JDCSTest.COLLECTION+".smallint_Tab; end");


	  stmt.executeUpdate("insert into "+JDCSTest.COLLECTION+".smallint_Tab values('ISMALL','0',null)");
	  CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".smallint_Proc(?,?,?)}");

		//register the output parameters
	  cstmt.registerOutParameter(1,java.sql.Types.SMALLINT);
	  cstmt.registerOutParameter(2,java.sql.Types.SMALLINT);
	  cstmt.registerOutParameter(3,java.sql.Types.SMALLINT);

		//execute the procedure
	  cstmt.execute();

		//invoke getShort method
	  short xRetVal = cstmt.getShort(1);
	  short yRetVal = cstmt.getShort(2);
	  short zRetVal = cstmt.getShort(3);


	  failed("Didn't throw exception"+xRetVal+","+yRetVal+","+zRetVal); 

      } catch (Exception e) {
          //
          // For the native driver, this is a data type mismatch
          // 
	  assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }

   }

// Named Parameter 

/**
getShort() - Get a named parameter that doesn't exist
**/
   public void Var032()
   {
       if(checkNamedParametersSupport())  {
	   try {
	       short p = csTypes_.getShort ("P_FAKE");
	        failed("Didn't throw exception"+p);
	   } catch (Exception e)
	   {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }
/**
getShort() - Get a type that was registered as a SMALLINT.
**/
   public void Var033()
   {
       if(checkNamedParametersSupport()) {
	   try {
	       short p = csTypes_.getShort ("P_SMALLINT");
	       assertCondition (p == 123);
	   } catch (Exception e)
	   {
	       failed (e, "Unexpected Exception");
	   }
       }
   }

/**
getShort() - Get a type that was registered as an INTEGER.
**/
   public void Var034() {
    if (checkNamedParametersSupport()) {

      String info = " -- call getShort against INTEGER -- Changed October 2011";

      try {
        short p = csTypes_.getShort("P_INTEGER");
        short expected = -456;
        assertCondition(p == expected, " got " + p + " expected " + expected
            + info);
      } catch (Exception e) {
        failed(e, "Unexpected Exception" + info);
      }
    }
  }
       

/**
getShort() - Get a type that was registered as an REAL.
**/
   public void Var035()
   {
       if(checkNamedParametersSupport()) {
         String info = " -- call getShort against REAL -- Changed October 2011";
	   try {
	       short p = csTypes_.getShort ("P_REAL");
	        short expected = 789;
	        assertCondition(p == expected, " got " + p + " expected " + expected
	            + info);
	      } catch (Exception e) {
	        failed(e, "Unexpected Exception" + info);
	      }
       }
   }


/**
getShort() - Get a type that was registered as an FLOAT.
**/
   public void Var036()
   {
       if(checkNamedParametersSupport()) {
         String info = " -- call getShort against FLOAT -- Changed October 2011";

	   try {
	       short p = csTypes_.getShort ("P_FLOAT");
	        short expected = 253;
	        assertCondition(p == expected, " got " + p + " expected " + expected
	            + info);
	      } catch (Exception e) {
	        failed(e, "Unexpected Exception" + info);
	      }
       }
   }

/**
getShort() - Get a type that was registered as an DOUBLE.
**/
   public void Var037()
   {
       if(checkNamedParametersSupport()) {
         String info = " -- call getShort against DOUBLE -- Changed October 2011";
	   try {
	       short p = csTypes_.getShort ("P_DOUBLE");
	        short expected = -987;
	        assertCondition(p == expected, " got " + p + " expected " + expected
	            + info);
	      } catch (Exception e) {
	        failed(e, "Unexpected Exception" + info);
	      }
       }	
   }


/**
getShort() - Get a type that was registered as an DECIMAL.
**/
   public void Var038()
   {
       if(checkNamedParametersSupport()) {
         String info = " -- call getShort against DECIMAL -- Changed October 2011";
	   try {
	       short p = csTypesB_.getShort ("P_DECIMAL_105");
	        short expected = 7;
	        assertCondition(p == expected, " got " + p + " expected " + expected
	            + info);
	      } catch (Exception e) {
	        failed(e, "Unexpected Exception" + info);
	      }
       }
   }


/**
getShort() - Get a type that was registered as an NUMERIC.
**/
   public void Var039()
   {
       if(checkNamedParametersSupport()) {
         String info = " -- call getShort against NUMERIC -- Changed October 2011";

	   try {
	       short p = csTypes_.getShort ("P_NUMERIC_105");
	        short expected = 19;
	        assertCondition(p == expected, " got " + p + " expected " + expected
	            + info);
	      } catch (Exception e) {
	        failed(e, "Unexpected Exception" + info);
	      }
       }
   }


/**
getShort() - Get a type that was registered as a CHAR.
**/
   public void Var040()
   {
       if(checkNamedParametersSupport()) {
	   try {
	       short p = csTypes_.getShort ("P_CHAR_50");
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }


/**
getShort() - Get a type that was registered as a VARCHAR.
**/
   public void Var041()
   {
       if(checkNamedParametersSupport()) {
	   try {
	       short p = csTypes_.getShort ("P_VARCHAR_50");
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	  assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }


/**
getShort() - Get a type that was registered as a BINARY.
**/
   public void Var042()
   {
       if(checkNamedParametersSupport()) {
	   try {
	       short p = csTypes_.getShort ("P_BINARY_20");
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }


/**
getShort() - Get a type that was registered as a VARBINARY.
**/
   public void Var043()
   {
       if(checkNamedParametersSupport()) {
	   try {
	       short p = csTypes_.getShort ("P_VARBINARY");
	       failed ("Didn't throw SQLException "+p);
	   }catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }


/**
getShort() - Get a type that was registered as a DATE.
**/
   public void Var044()
   {
       if(checkNamedParametersSupport()) {
	   try {
	       short p = csTypes_.getShort ("P_DATE");
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }


/**
getShort() - Get a type that was registered as a TIME.
**/
   public void Var045()
   {
       if(checkNamedParametersSupport()) {
	   try {
	       short p = csTypes_.getShort ("P_DATE");
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }


/**
getShort() - Get a type that was registered as a TIMESTAMP.
**/
   public void Var046()
   {
       if(checkNamedParametersSupport()) {
	   try {
	       short p = csTypes_.getShort ("P_TIMESTAMP");
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }


/**
getShort() - Get a type that was registered as an OTHER.
**/
   public void Var047()
   {
       if(checkNamedParametersSupport()) {
	   if (checkLobSupport ()) {
	       try {
		   short p = csTypes_.getShort ("P_DATALINK");
            failed ("Didn't throw SQLException "+p);
	       }catch (Exception e) {
		   assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	       }
	   }
       }
   }


/**
getShort() - Get a type that was registered as a BLOB.
**/
   public void Var048()
   {
       if(checkNamedParametersSupport()) {
	   if (checkLobSupport ()) {
	       try {
		   short p = csTypes_.getShort ("P_BLOB");
		   failed ("Didn't throw SQLException "+p);
	       } catch (Exception e) {
		   assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	       }
	   }
       }
   }


/**
getShort() - Get a type that was registered as a CLOB.
**/
   public void Var049()
   {
       if(checkNamedParametersSupport()) {
	   if (checkLobSupport ()) {
	       try {
		   short p = csTypes_.getShort ("P_CLOB");
		   failed ("Didn't throw SQLException "+p);
	       } catch (Exception e) {
		   assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	       }
	   }
       }
   }


/**
getShort() - Get a type that was registered as a BIGINT.
**/
   public void Var050()
   {
       if(checkNamedParametersSupport()) {
	   if (checkBigintSupport()) {
	     String info = " -- call getShort against BIGINT -- Changed October 2011";

	       try {
		   short p = csTypesB_.getShort ("P_BIGINT");
		        short expected = 21;
		        assertCondition(p == expected, " got " + p + " expected " + expected
		            + info);
		      } catch (Exception e) {
		        failed(e, "Unexpected Exception" + info);
		      }
	   }
       }
   }
   
   
   /**
    * getShort () - Get a type that was registered as a BOOLEAN.
    **/
   public void Var051() {
     if (checkBooleanSupport()) {
       String info = " -- Test added 2021/01/06 for boolean";
       getTestSuccessful(csTypes_, "getShort", 25, "1",info);
     }
   }


   /**
    * getShort() - Get a type that was registered as a BOOLEAN that returns
    * false.
    **/
   public void Var052() {
     if (checkBooleanSupport()) { 
       String info = " -- Test added 2021/01/06 for boolean";
       getTestSuccessful(csTypesB_, "getShort", 25, "0", info);
     }
   }
   
   /**
    * getShort() - Get a type that was registered as a BOOLEAN that returns
    * null.
    **/
   public void Var053() {
     if (checkBooleanSupport()) {
       String info = " -- Test added 2021/01/06 for boolean";
            getTestSuccessful(csTypesNull_, "getShort", 25, "0",info);
     }
   }

   

}
