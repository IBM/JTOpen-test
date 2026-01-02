///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetTime.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////



package test.JD.CS;

import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Types;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDSetupProcedure;



/**
Testcase JDCSGetTime.  This tests the following
method of the JDBC CallableStatement class:

<ul>
<li>getTime()
</ul>
**/
public class JDCSGetTime
extends JDCSGetTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSGetTime";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCSTest.main(newArgs); 
   }



 

/**
Constructor.
**/
   public JDCSGetTime (AS400 systemObject,
                       Hashtable<String,Vector<String>> namesAndVars,
                       int runMode,
                       FileOutputStream fileOutputStream,
                       
                       String password)
   {
      super (systemObject, "JDCSGetTime",
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
getTime() - Get parameter -1.
**/
   public void Var001()
   {
      try {
         csTypes_.execute ();
         Time p = csTypes_.getTime (-1);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTime() - Get parameter 0.
**/
   public void Var002()
   {
      try {
         Time p = csTypes_.getTime (0);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTime() - Use a parameter that is too big.
**/
   public void Var003()
   {
      try {
         Time p = csTypes_.getTime (35);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTime() - Get a parameter when there are no parameters.
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
         Time p = cs.getTime (1);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      } 

      if (c != null) try { c.close(); } catch (Exception e) {} 
      
   }



/**
getTime() - Get a parameter that was not registered.
**/
   public void Var005()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
         cs.execute ();
         Time p = cs.getTime (15);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTime() - Get a parameter when the statement has not been
executed.
**/
   public void Var006()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
         cs.registerOutParameter (16, Types.TIME);
         Time p = cs.getTime (16);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTime() - Get a parameter when the statement is closed.
**/
   public void Var007()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
         cs.registerOutParameter (16, Types.TIME);
         cs.execute ();
         cs.close ();
         Time p = cs.getTime (16);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTime() - Get an IN parameter that was correctly registered.
**/
   public void Var008()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESIN, supportedFeatures_);
         JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESIN,
                                              supportedFeatures_);
         cs.registerOutParameter (16, Types.TIME);
         cs.execute ();
         Time p = cs.getTime (16);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }


/**
getTime() - Get an INOUT parameter.
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
         cs.registerOutParameter (16, Types.TIME);
         cs.execute ();
         Time p = cs.getTime (16);
         assertCondition (p.toString ().equals ("08:42:30"));
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }


/**
getTime() - Get a parameter when the calendar is null.
**/
   public void Var010()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
         cs.registerOutParameter (16, Types.TIME);
         cs.execute ();
         Time p = cs.getTime (16, null);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTime() - Get a type that was registered as a SMALLINT.
**/
   public void Var011()
   {
      try {
         Time p = csTypes_.getTime (1);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTime() - Get a type that was registered as an INTEGER.
**/
   public void Var012()
   {
      try {
         Time p = csTypes_.getTime (2);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTime() - Get a type that was registered as an REAL.
**/
   public void Var013()
   {
      try {
         Time p = csTypes_.getTime (3);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTime() - Get a type that was registered as an FLOAT.
**/
   public void Var014()
   {
      try {
         Time p = csTypes_.getTime (4);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTime() - Get a type that was registered as an DOUBLE.
**/
   public void Var015()
   {
      try {
         Time p = csTypes_.getTime (5);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTime() - Get a type that was registered as an DECIMAL.
**/
   public void Var016()
   {
      try {
         Time p = csTypes_.getTime (6);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTime() - Get a type that was registered as an NUMERIC.
**/
   public void Var017()
   {
      try {
         Time p = csTypes_.getTime (8);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTime() - Get a type that was registered as a CHAR.
**/
   public void Var018()
   {
      try {
         Time p = csTypes_.getTime (11);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTime() - Get a type that was registered as a VARCHAR.
**/
   public void Var019()
   {
      try {
         Time p = csTypes_.getTime (12);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTime() - Get a type that was registered as a BINARY.
**/
   public void Var020()
   {
      try {
         Time p = csTypes_.getTime (13);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTime() - Get a type that was registered as a VARBINARY.
**/
   public void Var021()
   {
      try {
         Time p = csTypes_.getTime (14);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTime() - Get a type that was registered as a DATE.
**/
   public void Var022()
   {
      try {
         Time p = csTypes_.getTime (15);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTime() - Get a type that was registered as a TIME.
**/
   public void Var023()
   {
      try {
         Time p = csTypes_.getTime (16);
         assertCondition (p.toString ().equals ("08:42:30"));
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
getTime() - Get a type that was registered as a TIME, with a 
calendar specified.
**/
   public void Var024()
   {
      if (checkJdbc20 ()) {
         try {
            Time p = csTypes_.getTime (16, Calendar.getInstance ());
            assertCondition (p.toString ().equals ("08:42:30"));
         } catch (Exception e)
         {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
getTime() - Get a type that was registered as a TIMESTAMP.
**/
   public void Var025()
   {
     // As of 10/3/2011 for the toolbox driver, it is now possible to get a Time
     // from a parameter registered as timestamp
     // Note: JCC does not throw exception either.
     // Native JDBC will now need to change.
     //
     String info = " -- call getTime against TIMESTAMP -- Changed October 2011";

     try {
         Time p = csTypes_.getTime(17);

         String expected = "13:42:22";
         assertCondition(p.toString().equals(expected), "got "+p+" expected "+expected+info); 
      } catch (Exception e) {
        failed (e, "Unexpected Exception "+info);
      }

   }



/**
getTime() - Get a type that was registered as an OTHER.
**/
   public void Var026()
   {
      if (checkLobSupport ()) {
         try {
            Time p = csTypes_.getTime (18);
            failed ("Didn't throw SQLException "+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getTime() - Get a type that was registered as a BLOB.
**/
   public void Var027()
   {
      if (checkLobSupport ()) {
         try {
            Time p = csTypes_.getTime (19);
            failed ("Didn't throw SQLException "+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getTime() - Get a type that was registered as a CLOB.
**/
   public void Var028()
   {
      if (checkLobSupport ()) {
         try {
            Time p = csTypes_.getTime (20);
            failed ("Didn't throw SQLException "+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getTime() - Get a type that was registered as a BIGINT.
**/
   public void Var029()
   {
      if (checkBigintSupport()) {
         try {
            Time p = csTypes_.getTime (22);
            failed ("Didn't throw SQLException "+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getTime() - Get an INOUT parameter, when the output parameter is registered first.

SQL400 - We added this testcase because of a customer bug.
**/
   public void Var030()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_);
         JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                    supportedFeatures_, getDriver());
         cs.registerOutParameter (16, Types.TIME);
         JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                              supportedFeatures_);
         cs.execute ();
         Time p = cs.getTime (16);
         assertCondition (p.toString ().equals ("08:42:30"));
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }


/**
getTime() -- Get an output parameter where the database output type isn't necessarily a time 
*/
   public void Var031()
   {
      try {
	  Statement stmt = connection_.createStatement();
	  
	  try {
	      stmt.executeUpdate("drop table "+JDCSTest.COLLECTION+".TIME_tab");
	  } catch (Exception e) {}
	  try {
	      stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".TIME_Proc"); 
	  } catch (Exception e) {}

	  stmt.executeUpdate("create table "+JDCSTest.COLLECTION+".TIME_Tab(MAX_VAL VARCHAR(20), MIN_VAL VARCHAR(20), NULL_VAL VARCHAR(20) )"); 
	  stmt.executeUpdate("create procedure "+JDCSTest.COLLECTION+".TIME_Proc (out MAX_PARAM VARCHAR(20), out MIN_PARAM VARCHAR(20), out NULL_PARAM VARCHAR(20)) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from "+JDCSTest.COLLECTION+".TIME_Tab; end");

	  String maxString = "12:59:59";
	  String minString = "00:00:00"; 
	  stmt.executeUpdate("insert into "+JDCSTest.COLLECTION+".TIME_Tab values('"+maxString+"','"+minString+"',null)");
	  CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".TIME_Proc(?,?,?)}");

		//register the output parameters
	  cstmt.registerOutParameter(1,java.sql.Types.TIME);
	  cstmt.registerOutParameter(2,java.sql.Types.TIME);
	  cstmt.registerOutParameter(3,java.sql.Types.TIME);

		//execute the procedure
	  cstmt.execute();

		//invoke getByte method
	  Time xRetVal = cstmt.getTime(1);
	  Time yRetVal = cstmt.getTime(2);
	  Time zRetVal = cstmt.getTime(3);
	  Time xCheck  = Time.valueOf(maxString);
	  Time yCheck  = Time.valueOf(minString);
	  boolean condition =  false;
          if(isToolboxDriver())
              condition = ((xRetVal.toString()).equals(xCheck.toString())) && ((yRetVal.toString()).equals(yCheck.toString())) && (zRetVal == null);
          else
              condition = (xRetVal.equals(xCheck)) && (yRetVal.equals(yCheck)) && (zRetVal == null);
	  if (!condition) {
	      output_.println("xRetVal ("+xRetVal+"="+xRetVal.getTime()+") != xCheck ("+xCheck+"="+xCheck.getTime()+")" ); 
	      output_.println("yRetVal ("+yRetVal+"="+yRetVal.getTime()+") != yCheck ("+yCheck+"="+xCheck.getTime()+")");
	      output_.println("zRetVal ("+zRetVal+") != null"); 
	  } 
	  assertCondition(condition); 

      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }


/**
getTime() -- Get an output parameter where the database output type isn't necessarily a time and the version is not valid 
*/
   public void Var032()
   {
      try {
	  Statement stmt = connection_.createStatement();
	  
	  try {
	      stmt.executeUpdate("drop table "+JDCSTest.COLLECTION+".TIME_tab");
	  } catch (Exception e) {}
	  try {
	      stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".TIME_Proc"); 
	  } catch (Exception e) {}

	  stmt.executeUpdate("create table "+JDCSTest.COLLECTION+".TIME_Tab(MAX_VAL VARCHAR(20), MIN_VAL VARCHAR(20), NULL_VAL VARCHAR(20) )"); 
	  stmt.executeUpdate("create procedure "+JDCSTest.COLLECTION+".TIME_Proc (out MAX_PARAM VARCHAR(20), out MIN_PARAM VARCHAR(20), out NULL_PARAM VARCHAR(20)) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from "+JDCSTest.COLLECTION+".TIME_Tab; end");

	  String maxString = "abcdefgh";
	  String minString = "00300400"; 
	  stmt.executeUpdate("insert into "+JDCSTest.COLLECTION+".TIME_Tab values('"+maxString+"','"+minString+"',null)");
	  CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".TIME_Proc(?,?,?)}");

		//register the output parameters
	  cstmt.registerOutParameter(1,java.sql.Types.TIME);
	  cstmt.registerOutParameter(2,java.sql.Types.TIME);
	  cstmt.registerOutParameter(3,java.sql.Types.TIME);

		//execute the procedure
	  cstmt.execute();

		//invoke getByte method
	  Time xRetVal = cstmt.getTime(1);
	  Time yRetVal = cstmt.getTime(2);
	  Time zRetVal = cstmt.getTime(3);
	  output_.println("xRetVal ("+xRetVal+"="+xRetVal.getTime()+") "); 
	  output_.println("yRetVal ("+yRetVal+"="+yRetVal.getTime()+")"); 
	  output_.println("zRetVal ("+zRetVal+")"); 

	  failed("Didn't throw exception"); 

      } catch (Exception e) {
          //
          // For the native driver, this is a data type mismatch
          // 
	  assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }




/**
getTime() - Get a type that was registered as a TIME. Make sure that the Time matches a constructed time
  Per JDK 1.4 java doc -- The date components should be set to the "zero epoch" value of January 1, 1970 and should not be accessed. 
**/
   public void Var033()
   {
      try {
	 Time checkTime = Time.valueOf("08:42:30"); 
         Time p = csTypes_.getTime (16);
         assertCondition (p.equals(checkTime));
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }


/**
getTime() - Get a type that was registered as a TIME using a calendare. Make sure that the Time matches a constructed time
  Per JDK 1.4 java doc -- The date components should be set to the "zero epoch" value of January 1, 1970 and should not be accessed. 
**/
   public void Var034()
   {
       if (checkJdbc20 ()) {
	   try {
	       Time checkTime = Time.valueOf("08:42:30"); 
	       Time p = csTypes_.getTime (16, Calendar.getInstance ());
	       boolean condition =  p.equals(checkTime);
	       if (! condition) {
		   output_.println("check time    = "+checkTime+" ms="+checkTime.getTime()); 
		   output_.println("returned time = "+p        +" ms="+p.getTime()); 
	       } 
	       assertCondition (condition);
	   } catch (Exception e)
	   {
	       failed (e, "Unexpected Exception");
	   }
       }
   }

// Named Parameter tests

/**
getTime() - Get a named parameter that doesn't exist
**/
   public void Var035()
   {
        if(checkNamedParametersSupport()) {
	    try {
		Time p = csTypes_.getTime ("P_FAKE");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }
/**
getTime() - Get a type that was registered as a SMALLINT.
**/
   public void Var036()
   {
        if(checkNamedParametersSupport()) {
	    try {
		Time p = csTypes_.getTime ("P_SMALLINT");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }
/**
getTime() - Get a type that was registered as an INTEGER.
**/
   public void Var037()
   {
        if(checkNamedParametersSupport()) {
	    try {
		Time p = csTypes_.getTime ("P_INTEGER");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }


/**
getTime() - Get a type that was registered as an REAL.
**/
   public void Var038()
   {
        if(checkNamedParametersSupport()) {
	    try {
		Time p = csTypes_.getTime ("P_REAL");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }


/**
getTime() - Get a type that was registered as an FLOAT.
**/
   public void Var039()
   {
        if(checkNamedParametersSupport()) {
	    try {
		Time p = csTypes_.getTime ("P_FLOAT");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }


/**
getTime() - Get a type that was registered as an DOUBLE.
**/
   public void Var040()
   {
        if(checkNamedParametersSupport()) {
	    try {
		Time p = csTypes_.getTime ("P_DOUBLE");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }


/**
getTime() - Get a type that was registered as an DECIMAL.
**/
   public void Var041()
   {
        if(checkNamedParametersSupport()) {
	    try {
		Time p = csTypes_.getTime ("P_DECIMAL_50");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }

/**
getTime() - Get a type that was registered as an NUMERIC.
**/
   public void Var042()
   {
        if(checkNamedParametersSupport()) {
	    try {
		Time p = csTypes_.getTime ("P_NUMERIC_50");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }


/**
getTime() - Get a type that was registered as a CHAR.
**/
   public void Var043()
   {
        if(checkNamedParametersSupport()) {
	    try {
		Time p = csTypes_.getTime ("P_CHAR_50");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }


/**
getTime() - Get a type that was registered as a VARCHAR.
**/
   public void Var044()
   {
        if(checkNamedParametersSupport()) {
	    try {
		Time p = csTypes_.getTime ("P_VARCHAR_50");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }


/**
getTime() - Get a type that was registered as a BINARY.
**/
   public void Var045()
   {
        if(checkNamedParametersSupport()) {
	    try {
		Time p = csTypes_.getTime ("P_BINARY_20");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }


/**
getTime() - Get a type that was registered as a VARBINARY.
**/
   public void Var046()
   {
        if(checkNamedParametersSupport()) {
	    try {
		Time p = csTypes_.getTime ("P_VARBINARY_20");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }


/**
getTime() - Get a type that was registered as a DATE.
**/
   public void Var047()
   {
        if(checkNamedParametersSupport()) {
	    try {
		Time p = csTypes_.getTime ("P_DATE");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}	
   }


/**
getTime() - Get a type that was registered as a TIME.
**/
   public void Var048()
   {
        if(checkNamedParametersSupport()) {
	    try {
		Time p = csTypes_.getTime ("P_TIME");
		assertCondition (p.toString ().equals ("08:42:30"));
	    } catch (Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
   }


/**
getTime() - Get a type that was registered as a TIME, with a 
calendar specified.
**/
   public void Var049()
   {
        if(checkNamedParametersSupport()) {
	    if (checkJdbc20 ()) {
		try {
		    Time p = csTypes_.getTime ("P_TIME", Calendar.getInstance ());
		    assertCondition (p.toString ().equals ("08:42:30"));
		} catch (Exception e)
		{
		    failed (e, "Unexpected Exception");
		}
	    }
	}
   }


/**
getTime() - Get a type that was registered as a TIMESTAMP.
**/
   public void Var050()
   {
        if(checkNamedParametersSupport()) {
	     String info = " -- call getTime against TIMESTAMP -- Changed October 2011";

	     try {
	         Time p = csTypes_.getTime("P_TIMESTAMP");

	         String expected = "13:42:22";
	         assertCondition(p.toString().equals(expected), "got "+p+" expected "+expected+info); 
	      } catch (Exception e) {
	        failed (e, "Unexpected Exception "+info);
	      }

	}
   }


/**
getTime() - Get a type that was registered as an OTHER.
**/
   public void Var051()
   {
        if(checkNamedParametersSupport()) {
	    if (checkLobSupport ()) {
		try {
		    Time p = csTypes_.getTime ("P_DATALINK");
		    failed ("Didn't throw SQLException "+p);
		} catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}	
   }


/**
getTime() - Get a type that was registered as a BLOB.
**/
   public void Var052()
   {
        if(checkNamedParametersSupport()) {
	    if (checkLobSupport ()) {
		try {
		    Time p = csTypes_.getTime ("P_BLOB");
		    failed ("Didn't throw SQLException "+p);
		} catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
   }


/**
getTime() - Get a type that was registered as a CLOB.
**/
   public void Var053()
   {
        if(checkNamedParametersSupport()) {
	    if (checkLobSupport ()) {
		try {
		    Time p = csTypes_.getTime ("P_CLOB");
		    failed ("Didn't throw SQLException "+p);
		} catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
   }


/**
getTime() - Get a type that was registered as a BIGINT.
**/
   public void Var054()
   {
        if(checkNamedParametersSupport()) {
	    if (checkBigintSupport()) {
		try {
		    Time p = csTypes_.getTime ("P_BIGINT");
		    failed ("Didn't throw SQLException "+p);
		} catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
   }
   
   
   
   /**
   getTime() - Get a type that was registered as a BOOLEAN.
   **/
      
      public void Var055()
      {
         if (checkBooleanSupport()) {
           getTestFailed(csTypes_,"getTime",25,"Data type mismatch",""); 
          
         }
      }

      
      /**
      getTime() - Get a type that was registered as a BOOLEAN.
      **/
         
         public void Var056()
         {
            if (checkBooleanSupport()) {
              getTestFailed(csTypes_,"getTime","P_BOOLEAN","Data type mismatch",""); 
             
            }
         }


   
}
