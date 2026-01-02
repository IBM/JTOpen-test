///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetTimestamp.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetTimestamp.java
//
// Classes:      JDCSGetTimestamp
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDSetupProcedure;



/**
Testcase JDCSGetTimestamp.  This tests the following
method of the JDBC CallableStatement class:

<ul>
<li>getTimestamp()
</ul>
**/
public class JDCSGetTimestamp
extends JDCSGetTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSGetTimestamp";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCSTest.main(newArgs); 
   }






/**
Constructor.
**/
   public JDCSGetTimestamp (AS400 systemObject,
                            Hashtable<String,Vector<String>> namesAndVars,
                            int runMode,
                            FileOutputStream fileOutputStream,
                            
                            String password)
   {
      super (systemObject, "JDCSGetTimestamp",
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

  void setupCstypes() throws Exception {
    if (csTypes_ == null) {

      csTypes_ = JDSetupProcedure.prepare(connection_,
          JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
      JDSetupProcedure.register(csTypes_, JDSetupProcedure.STP_CSTYPESOUT,
          supportedFeatures_, getDriver());
      csTypes_.execute();
    }
  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    super.cleanup();
  }


/**
getTimestamp() - Get parameter -1.
**/
   public void Var001()
   {

      try {
	  setupCstypes(); 
         csTypes_.execute ();
         Timestamp p = csTypes_.getTimestamp (-1);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTimestamp() - Get parameter 0.
**/
   public void Var002()
   {
      try {
	  setupCstypes(); 

         Timestamp p = csTypes_.getTimestamp (0);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTimestamp() - Use a parameter that is too big.
**/
   public void Var003()
   {
      try {
	  setupCstypes(); 

         Timestamp p = csTypes_.getTimestamp (35);	
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTimestamp() - Get a parameter when there are no parameters.
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
         Timestamp p = cs.getTimestamp (1);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      } finally {
         try {
            if (c != null) c.close ();
         } catch (SQLException e) {
            // Ignore.
         }
      }
   }



/**
getTimestamp() - Get a parameter that was not registered.
**/
   public void Var005()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
         cs.execute ();
         Timestamp p = cs.getTimestamp (17);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTimestamp() - Get a parameter when the statement has not been
executed.
**/
   public void Var006()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
         cs.registerOutParameter (17, Types.TIMESTAMP);
         Timestamp p = cs.getTimestamp (17);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTimestamp() - Get a parameter when the statement is closed.
**/
   public void Var007()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
         cs.registerOutParameter (17, Types.TIMESTAMP);
         cs.execute ();
         cs.close ();
         Timestamp p = cs.getTimestamp (17);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTimestamp() - Get an IN parameter that was correctly registered.
**/
   public void Var008()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESIN, supportedFeatures_);
         JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESIN,
                                              supportedFeatures_);
         cs.registerOutParameter (17, Types.TIMESTAMP);
         cs.execute ();
         Timestamp p = cs.getTimestamp (17);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }


/**
getTimestamp() - Get an INOUT parameter.
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
         cs.registerOutParameter (17, Types.TIMESTAMP);
         cs.execute ();
         Timestamp p = cs.getTimestamp (17);
         assertCondition (p.toString().equals("2001-11-18 13:42:22.123456"), "Got "+p.toString()+" sb 2001-11-18 13:42:22.123456");
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }


/**
getTimestamp() - Get a parameter when the calendar is null.
**/
   public void Var010()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
         cs.registerOutParameter (17, Types.TIMESTAMP);
         cs.execute ();
         Timestamp p = cs.getTimestamp (17, null);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTimestamp() - Get a type that was registered as a SMALLINT.
**/
   public void Var011()
   {
      try {
	  setupCstypes(); 
         Timestamp p = csTypes_.getTimestamp (1);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTimestamp() - Get a type that was registered as an INTEGER.
**/
   public void Var012()
   {
      try {
	  setupCstypes(); 
         Timestamp p = csTypes_.getTimestamp (2);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTimestamp() - Get a type that was registered as an REAL.
**/
   public void Var013()
   {
      try {
	  setupCstypes(); 
         Timestamp p = csTypes_.getTimestamp (3);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTimestamp() - Get a type that was registered as an FLOAT.
**/
   public void Var014()
   {
      try {
	  setupCstypes(); 
         Timestamp p = csTypes_.getTimestamp (4);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTimestamp() - Get a type that was registered as an DOUBLE.
**/
   public void Var015()
   {
      try {
	  setupCstypes(); 
         Timestamp p = csTypes_.getTimestamp (5);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTimestamp() - Get a type that was registered as an DECIMAL.
**/
   public void Var016()
   {
      try {
	  setupCstypes(); 
         Timestamp p = csTypes_.getTimestamp (6);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTimestamp() - Get a type that was registered as an NUMERIC.
**/
   public void Var017()
   {
      try {
	  setupCstypes(); 
         Timestamp p = csTypes_.getTimestamp (8);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTimestamp() - Get a type that was registered as a CHAR.
**/
   public void Var018()
   {
      try {
	  setupCstypes(); 
         Timestamp p = csTypes_.getTimestamp (11);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTimestamp() - Get a type that was registered as a VARCHAR.
**/
   public void Var019()
   {
      try {
	  setupCstypes(); 
         Timestamp p = csTypes_.getTimestamp (12);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTimestamp() - Get a type that was registered as a BINARY.
**/
   public void Var020()
   {
      try {
	  setupCstypes(); 
         Timestamp p = csTypes_.getTimestamp (13);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTimestamp() - Get a type that was registered as a VARBINARY.
**/
   public void Var021()
   {
      try {
	  setupCstypes(); 
         Timestamp p = csTypes_.getTimestamp (14);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getTimestamp() - Get a type that was registered as a DATE.
**/
   public void Var022()
   {
     
     // As of 10/3/2011 for the toolbox driver, it is now possible to get a timestamp
     // from a parameter registered as date or time 
     // Note: JCC does not throw exception either.
     // Native JDBC will now need to change.
     //
     String info = " -- call getTimestamp against DATE -- Changed October 2011";

      try {
	  setupCstypes(); 
         Timestamp p = csTypes_.getTimestamp (15);
         String expected = "1998-04-15 00:00:00.0";
         assertCondition(p.toString().equals(expected), "got "+p+" expected "+expected+info); 
      } catch (Exception e) {
        failed (e, "Unexpected Exception "+info);
      }
   }



/**
getTimestamp() - Get a type that was registered as a TIME.
**/
   public void Var023()
   {
     String info = " -- call getTimestamp against TIME -- Changed October 2011";
      try {
	  setupCstypes(); 
         Timestamp p = csTypes_.getTimestamp (16);
         String expected = "1970-01-01 08:42:30.0";
         assertCondition(p.toString().equals(expected), "got "+p+" expected "+expected+info); 
      } catch (Exception e) {
        failed (e, "Unexpected Exception "+info);
      }
   }



/**
getTimestamp() - Get a type that was registered as a TIMESTAMP.
**/
   public void Var024()
   {
      try {
	  setupCstypes(); 
         Timestamp p = csTypes_.getTimestamp (17);
         assertCondition (p.toString().equals("2001-11-18 13:42:22.123456"), "got "+p.toString()+" sb 2001-11-18 13:42:22.123456");
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
getTimestamp() - Get a type that was registered as a TIMESTAMP,
with a calendar specified.
**/
   public void Var025()
   {
      if (checkJdbc20 ()) {
         try {
	  setupCstypes(); 
            Timestamp p = csTypes_.getTimestamp (17, Calendar.getInstance ());
            assertCondition (p.toString().equals("2001-11-18 13:42:22.123456"), "got "+p.toString()+" sb 2001-11-18 13:42:22.123456" );
         } catch (Exception e)
         {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
getTimestamp() - Get a type that was registered as an OTHER.
**/
   public void Var026()
   {
      if (checkLobSupport ()) {
         try {
	  setupCstypes(); 
            Timestamp p = csTypes_.getTimestamp (18);
            failed ("Didn't throw SQLException "+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getTimestamp() - Get a type that was registered as a BLOB.
**/
   public void Var027()
   {
      if (checkLobSupport ()) {
         try {
	  setupCstypes(); 
            Timestamp p = csTypes_.getTimestamp (19);
            failed ("Didn't throw SQLException "+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getTimestamp() - Get a type that was registered as a CLOB.
**/
   public void Var028()
   {
      if (checkLobSupport ()) {
         try {
	  setupCstypes(); 
            Timestamp p = csTypes_.getTimestamp (20);
            failed ("Didn't throw SQLException "+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getTimestamp() - Get an INOUT parameter, when the output parameter is registered first.

SQL400 - We added this testcase because of a customer bug.
**/
   public void Var029()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_);
         JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                    supportedFeatures_, getDriver());
         cs.registerOutParameter (17, Types.TIMESTAMP);
         JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                              supportedFeatures_);
         cs.execute ();
         Timestamp p = cs.getTimestamp (17);
         assertCondition (p.toString().equals("2001-11-18 13:42:22.123456"), "got "+p.toString()+" sb 2001-11-18 13:42:22.123456");
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
getTimestamp() -- Get an output parameter where the database output type isn't necessarily a time 
*/
   public void Var030()
   {
      try {
	  Statement stmt = connection_.createStatement();
	  
	  try {
	      stmt.executeUpdate("drop table "+JDCSTest.COLLECTION+".TIMESTAMP_TAB");
	  } catch (Exception e) {}
	  try {
	      stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".TIMESTAMP_PROC"); 
	  } catch (Exception e) {}

	  stmt.executeUpdate("create table "+JDCSTest.COLLECTION+".TIMESTAMP_TAB(MAX_VAL varchar(40), MIN_VAL varchar(40), NULL_VAL varchar(40) )"); 
	  stmt.executeUpdate("create procedure "+JDCSTest.COLLECTION+".TIMESTAMP_PROC (out MAX_PARAM varchar(40), out MIN_PARAM varchar(40), out NULL_PARAM varchar(40)) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from "+JDCSTest.COLLECTION+".TIMESTAMP_TAB; end");

	  String maxString = "2003-04-05 06:07:08.91234"; 
	  String minString = "1971-01-01 00:00:00.00000"; 
	  stmt.executeUpdate("insert into "+JDCSTest.COLLECTION+".TIMESTAMP_TAB values('"+maxString+"','"+minString+"',null)");
	  CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".TIMESTAMP_PROC(?,?,?)}");

		//register the output parameters
	  cstmt.registerOutParameter(1,java.sql.Types.TIMESTAMP);
	  cstmt.registerOutParameter(2,java.sql.Types.TIMESTAMP);
	  cstmt.registerOutParameter(3,java.sql.Types.TIMESTAMP);

		//execute the procedure
	  cstmt.execute();

		//invoke getByte method
	  Timestamp xRetVal = cstmt.getTimestamp(1);
	  Timestamp yRetVal = cstmt.getTimestamp(2);
	  Timestamp zRetVal = cstmt.getTimestamp(3);
	  Timestamp xCheck  = Timestamp.valueOf(maxString);
	  Timestamp yCheck  = Timestamp.valueOf(minString);
	  boolean condition =  (xRetVal.equals(xCheck)) && (yRetVal.equals(yCheck)) && (zRetVal == null);
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
getTimestamp() -- Get an output parameter where the database output type isn't necessarily a time and the version is not valid 
*/
   public void Var031()
   {
      try {
	  Statement stmt = connection_.createStatement();
	  
	  try {
	      stmt.executeUpdate("drop table "+JDCSTest.COLLECTION+".TIMESTAMP_TAB");
	  } catch (Exception e) {}
	  try {
	      stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".TIMESTAMP_PROC"); 
	  } catch (Exception e) {}

	  stmt.executeUpdate("create table "+JDCSTest.COLLECTION+".TIMESTAMP_TAB(MAX_VAL varchar(40), MIN_VAL varchar(40), NULL_VAL varchar(40) )"); 
	  stmt.executeUpdate("create procedure "+JDCSTest.COLLECTION+".TIMESTAMP_PROC (out MAX_PARAM varchar(40), out MIN_PARAM varchar(40), out NULL_PARAM varchar(40)) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from "+JDCSTest.COLLECTION+".TIMESTAMP_TAB; end");

	  String maxString = "abcdefgh";
	  String minString = "00300400"; 
	  stmt.executeUpdate("insert into "+JDCSTest.COLLECTION+".TIMESTAMP_TAB values('"+maxString+"','"+minString+"',null)");
	  CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".TIMESTAMP_PROC(?,?,?)}");

		//register the output parameters
	  cstmt.registerOutParameter(1,java.sql.Types.TIMESTAMP);
	  cstmt.registerOutParameter(2,java.sql.Types.TIMESTAMP);
	  cstmt.registerOutParameter(3,java.sql.Types.TIMESTAMP);

		//execute the procedure
	  cstmt.execute();

		//invoke getByte method
	  Timestamp xRetVal = cstmt.getTimestamp(1);
	  Timestamp yRetVal = cstmt.getTimestamp(2);
	  Timestamp zRetVal = cstmt.getTimestamp(3);
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

// - Named Parameter test
/**
getTimestamp() - Get a Named Parameter that Doesn't exists
**/
   public void Var032()
   {
        if(checkNamedParametersSupport()) {
	    try {
	  setupCstypes(); 
		Timestamp p = csTypes_.getTimestamp ("P_FAKE");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }
/**
getTimestamp() - Get a type that was registered as a SMALLINT.
**/
   public void Var033()
   {
        if(checkNamedParametersSupport()) {
	    try {
	  setupCstypes(); 
		Timestamp p = csTypes_.getTimestamp ("P_SMALLINT");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }


/**
getTimestamp() - Get a type that was registered as an INTEGER.
**/
   public void Var034()
   {
        if(checkNamedParametersSupport()) {
	    try {
	  setupCstypes(); 
		Timestamp p = csTypes_.getTimestamp ("P_INTEGER");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }	
	}
   }


/**	
getTimestamp() - Get a type that was registered as an REAL.
**/
   public void Var035()
   {
        if(checkNamedParametersSupport()) {
	    try {
	  setupCstypes(); 
		Timestamp p = csTypes_.getTimestamp ("P_REAL");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }


/**
getTimestamp() - Get a type that was registered as an FLOAT.
**/
   public void Var036()
   {
        if(checkNamedParametersSupport()) {
	    try {
	  setupCstypes(); 
		Timestamp p = csTypes_.getTimestamp ("P_FLOAT");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }


/**
getTimestamp() - Get a type that was registered as an DOUBLE.
**/
   public void Var037()
   {
        if(checkNamedParametersSupport()) {
	    try {
	  setupCstypes(); 
		Timestamp p = csTypes_.getTimestamp ("P_DOUBLE");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }


/**
getTimestamp() - Get a type that was registered as an DECIMAL.
**/
   public void Var038()
   {
        if(checkNamedParametersSupport()) {
	    try {
	  setupCstypes(); 
		Timestamp p = csTypes_.getTimestamp ("P_DECIMAL_50");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }


/**
getTimestamp() - Get a type that was registered as an NUMERIC.
**/
   public void Var039()
   {
        if(checkNamedParametersSupport()) {
	    try {
	  setupCstypes(); 
		Timestamp p = csTypes_.getTimestamp ("P_NUMERIC_50");
		failed ("Didn't throw SQLException "+p);
	    }catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }


/**
getTimestamp() - Get a type that was registered as a CHAR.
**/
   public void Var040()
   {
        if(checkNamedParametersSupport()) {
	    try {
	  setupCstypes(); 
		Timestamp p = csTypes_.getTimestamp ("P_CHAR_50");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }


/**
getTimestamp() - Get a type that was registered as a VARCHAR.
**/
   public void Var041()
   {
        if(checkNamedParametersSupport()) {
	    try {
	  setupCstypes(); 
		Timestamp p = csTypes_.getTimestamp ("P_VARCHAR_50");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }


/**
getTimestamp() - Get a type that was registered as a BINARY.
**/
   public void Var042()
   {
        if(checkNamedParametersSupport()) {
	    try {
	  setupCstypes(); 
		Timestamp p = csTypes_.getTimestamp ("P_BINARY_20");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }


/**
getTimestamp() - Get a type that was registered as a VARBINARY.
**/
   public void Var043()
   {
        if(checkNamedParametersSupport()) {
	    try {
	  setupCstypes(); 
		Timestamp p = csTypes_.getTimestamp ("P_VARBINARY_20");
		failed ("Didn't throw SQLException "+p);
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
   }


/**
getTimestamp() - Get a type that was registered as a DATE.
**/
   public void Var044()
   {
        if(checkNamedParametersSupport()) {
	     String info = " -- call getTimestamp against DATE -- Changed October 2011";

	      try {
	  setupCstypes(); 
	         Timestamp p = csTypes_.getTimestamp ("P_DATE");
	         String expected = "1998-04-15 00:00:00.0";
	         assertCondition(p.toString().equals(expected), "got "+p+" expected "+expected+info); 
	      } catch (Exception e) {
	        failed (e, "Unexpected Exception "+info);
	      }

	}	
   }


/**
getTimestamp() - Get a type that was registered as a TIME.
**/
   public void Var045()
   {
        if(checkNamedParametersSupport()) {
	     String info = " -- call getTimestamp against TIME -- Changed October 2011";
	      try {
	  setupCstypes(); 
	         Timestamp p = csTypes_.getTimestamp ("P_TIME");
	         String expected = "1970-01-01 08:42:30.0";
	         assertCondition(p.toString().equals(expected), "got "+p+" expected "+expected+info); 
	      } catch (Exception e) {
	        failed (e, "Unexpected Exception "+info);
	      }

	}
        
   }


/**
getTimestamp() - Get a type that was registered as a TIMESTAMP.
**/
   public void Var046()
   {
        if(checkNamedParametersSupport()) {
	    try {
	  setupCstypes(); 
		Timestamp p = csTypes_.getTimestamp ("P_TIMESTAMP");
		assertCondition (p.toString().equals("2001-11-18 13:42:22.123456"), "got "+p.toString()+" sb 2001-11-18 13:42:22.123456");
	    } catch (Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }	
	}
   }


/**
getTimestamp() - Get a type that was registered as a TIMESTAMP,
with a calendar specified.
**/
   public void Var047()
   {
        if(checkNamedParametersSupport()) {
	    if (checkJdbc20 ()) {
		try {
	  setupCstypes(); 
		    Timestamp p = csTypes_.getTimestamp ("P_TIMESTAMP", Calendar.getInstance ());
		    assertCondition (p.toString().equals("2001-11-18 13:42:22.123456"), "got "+p.toString()+" sb 2001-11-18 13:42:22.123456");
		} catch (Exception e)
		{
		    failed (e, "Unexpected Exception");
		}
	    }
	}
   }


/**
getTimestamp() - Get a type that was registered as an OTHER.
**/
   public void Var048()
   {
        if(checkNamedParametersSupport()) {
	    if (checkLobSupport ()) {
		try {
	  setupCstypes(); 
		    Timestamp p = csTypes_.getTimestamp ("P_DATALINK");
		    failed ("Didn't throw SQLException "+p);
		} catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
   }


/**
getTimestamp() - Get a type that was registered as a BLOB.
**/
   public void Var049()
   {
        if(checkNamedParametersSupport()) {
	    if (checkLobSupport ()) {
		try {
	  setupCstypes(); 
		    Timestamp p = csTypes_.getTimestamp ("P_BLOB");
		    failed ("Didn't throw SQLException "+p);
		} catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}	
	    }	
	}
   }


/**
getTimestamp() - Get a type that was registered as a CLOB.
**/
   public void Var050()
   {
        if(checkNamedParametersSupport()) {
	    if (checkLobSupport ()) {
		try {
	  setupCstypes(); 
		    Timestamp p = csTypes_.getTimestamp ("P_CLOB");
		    failed ("Didn't throw SQLException "+p);
		} catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
   }



    /**
     * Test get timestamp for different precision
     * The called procedure will have two parameters.
     * parameter1 is an integer an is an input parameter
     * parameter2 is the output timestamp
     */

  public void testTimestampPrecision(String procedureName,
      String procedureDefinition, String[] expectedValues) {
    if (checkTimestamp12Support()) {
      String sql; 
      StringBuffer sb = new StringBuffer();
      boolean passed = true;
      try {
        Statement s = connection_.createStatement();
        sql = "DROP PROCEDURE " + procedureName;
        try {
          sb.append("Executing " + sql + "\n");
          s.executeUpdate(sql);
        } catch (Exception e) {
          sb.append("Ignoring exception " + e.toString() + "\n");
        }
        sql = "CREATE PROCEDURE " + procedureName + procedureDefinition;
        sb.append("Executing " + sql + "\n");
        s.executeUpdate(sql);
        sql = "CALL " + procedureName + "(?,?)";
        sb.append("Executing " + sql + "\n");
        CallableStatement cstmt = connection_.prepareCall(sql);
        cstmt.registerOutParameter(2, java.sql.Types.TIMESTAMP);
        for (int i = 0; i < expectedValues.length; i++) {

	  String outputString = null;

	  try {
	      cstmt.setInt(1, i);
	      cstmt.execute();
	      Timestamp outputTimestamp = cstmt.getTimestamp(2);
	      if (outputTimestamp == null) {
		  outputString="null";
	      } else {
		  outputString = outputTimestamp.toString();
	      }
	  } catch (Exception e) {
	      e.printStackTrace(output_);
	      outputString=e.toString(); 
	  } 
          if (outputString.equals(expectedValues[i])) {
            sb.append("OK output=" + outputString + "\n");
          } else {
            passed = false;
            sb.append("FAILED output=" + outputString + " expected "
                + expectedValues[i] + "\n");
          }
        }

        s.close();
        assertCondition(passed,sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception " + sb);
      }
    }

  }

  public void Var051() {
    String[] expectedValues = {
    "2013-01-13 11:12:13.0",
    "2013-02-13 11:12:13.1",
    "2013-02-13 11:12:13.12",
    "2013-02-13 11:12:13.123",
    "2013-02-13 11:12:13.1234",
    "2013-02-13 11:12:13.12345",
    "2013-02-13 11:12:13.123456",
    "2013-02-13 11:12:13.1234567",
    "2013-02-13 11:12:13.12345678",
    "2013-02-13 11:12:13.123456789",
    "2013-02-13 11:12:13.1234567891",
    "2013-02-13 11:12:13.12345678901",
    "2013-02-13 11:12:13.123456789012",
    "2013-02-13 11:12:13.1",
    "2013-02-13 11:12:13.12",
    "2013-02-13 11:12:13.123",
    "2013-02-13 11:12:13.1234",
    "2013-02-13 11:12:13.12345",
    "2013-02-13 11:12:13.123456",
    "2013-02-13 11:12:13.1234567",
    "2013-02-13 11:12:13.12345678",
    "2013-02-13 11:12:13.123456789",
    "2013-02-13 11:12:13.1234567891",
    "2013-02-13 11:12:13.12345678901",
    "2013-02-13 11:12:13.123456789",
    "null"
    };
    
    testTimestampPrecision(collection_ + ".JDCSGT51", 
          "( in c1 int, out c2 timestamp(12)) begin  " +
          "if c1=0 then set c2='2013-01-13 11:12:13'; " +
          "elseif c1=1 then set c2='2013-02-13 11:12:13.1'; " +
          "elseif  c1=2 then set c2='2013-02-13 11:12:13.12';  " +
          "elseif  c1=3 then set c2='2013-02-13 11:12:13.123';  " +
          "elseif  c1=4 then set c2='2013-02-13 11:12:13.1234';  " +
          "elseif  c1=5 then set c2='2013-02-13 11:12:13.12345';  " +
          "elseif  c1=6 then set c2='2013-02-13 11:12:13.123456';  " +
          "elseif  c1=7 then set c2='2013-02-13 11:12:13.1234567';  " +
          "elseif  c1=8 then set c2='2013-02-13 11:12:13.12345678';  " +
          "elseif  c1=9 then set c2='2013-02-13 11:12:13.123456789';  " +
          "elseif  c1=10 then set c2='2013-02-13 11:12:13.1234567891';  " +
          "elseif  c1=11 then set c2='2013-02-13 11:12:13.12345678901';  " +
          "elseif  c1=12 then set c2='2013-02-13 11:12:13.123456789012'; " +
          "elseif  c1=13 then set c2='2013-02-13 11:12:13.100'; " +
          "elseif  c1=14 then set c2='2013-02-13 11:12:13.1200';  " +
          "elseif  c1=15 then set c2='2013-02-13 11:12:13.12300';  " +
          "elseif  c1=16 then set c2='2013-02-13 11:12:13.12340000';  " +
          "elseif  c1=17 then set c2='2013-02-13 11:12:13.1234500';  " +
          "elseif  c1=18 then set c2='2013-02-13 11:12:13.1234560';  " +
          "elseif  c1=19 then set c2='2013-02-13 11:12:13.12345670';  " +
          "elseif  c1=20 then set c2='2013-02-13 11:12:13.123456780';  " +
          "elseif  c1=21 then set c2='2013-02-13 11:12:13.1234567890';  " +
          "elseif  c1=22 then set c2='2013-02-13 11:12:13.12345678910';  " +
          "elseif  c1=23 then set c2='2013-02-13 11:12:13.123456789010';  " +
          "elseif  c1=24 then set c2='2013-02-13 11:12:13.12345678900'; " +
          "elseif  c1=25 then set c2=NULL; " +

          " end if ;  end", 
          expectedValues);

  }


  public void Var052() {
    String[] expectedValues = {
    "2013-01-13 11:12:13.0",
    "2013-02-13 11:12:13.1",
    "2013-02-13 11:12:13.12",
    "2013-02-13 11:12:13.123",
    "2013-02-13 11:12:13.1234",
    "2013-02-13 11:12:13.12345",
    "2013-02-13 11:12:13.123456",
    "2013-02-13 11:12:13.1234567",
    "2013-02-13 11:12:13.12345678",
    "2013-02-13 11:12:13.123456789",
    "2013-02-13 11:12:13.1234567891",
    "2013-02-13 11:12:13.12345678901",
    "2013-02-13 11:12:13.12345678901",
    "2013-02-13 11:12:13.1",
    "2013-02-13 11:12:13.12",
    "2013-02-13 11:12:13.123",
    "2013-02-13 11:12:13.1234",
    "2013-02-13 11:12:13.12345",
    "2013-02-13 11:12:13.123456",
    "2013-02-13 11:12:13.1234567",
    "2013-02-13 11:12:13.12345678",
    "2013-02-13 11:12:13.123456789",
    "2013-02-13 11:12:13.1234567891",
    "2013-02-13 11:12:13.12345678901",
    "2013-02-13 11:12:13.123456789",
    "null",
    };
    
    testTimestampPrecision(collection_ + ".JDCSGT52", 
          "( in c1 int, out c2 timestamp(11)) begin  " +
          "if c1=0 then set c2='2013-01-13 11:12:13'; " +
          "elseif c1=1 then set c2='2013-02-13 11:12:13.1'; " +
          "elseif  c1=2 then set c2='2013-02-13 11:12:13.12';  " +
          "elseif  c1=3 then set c2='2013-02-13 11:12:13.123';  " +
          "elseif  c1=4 then set c2='2013-02-13 11:12:13.1234';  " +
          "elseif  c1=5 then set c2='2013-02-13 11:12:13.12345';  " +
          "elseif  c1=6 then set c2='2013-02-13 11:12:13.123456';  " +
          "elseif  c1=7 then set c2='2013-02-13 11:12:13.1234567';  " +
          "elseif  c1=8 then set c2='2013-02-13 11:12:13.12345678';  " +
          "elseif  c1=9 then set c2='2013-02-13 11:12:13.123456789';  " +
          "elseif  c1=10 then set c2='2013-02-13 11:12:13.1234567891';  " +
          "elseif  c1=11 then set c2='2013-02-13 11:12:13.12345678901';  " +
          "elseif  c1=12 then set c2='2013-02-13 11:12:13.123456789012'; " +
          "elseif  c1=13 then set c2='2013-02-13 11:12:13.100'; " +
          "elseif  c1=14 then set c2='2013-02-13 11:12:13.1200';  " +
          "elseif  c1=15 then set c2='2013-02-13 11:12:13.12300';  " +
          "elseif  c1=16 then set c2='2013-02-13 11:12:13.12340000';  " +
          "elseif  c1=17 then set c2='2013-02-13 11:12:13.1234500';  " +
          "elseif  c1=18 then set c2='2013-02-13 11:12:13.1234560';  " +
          "elseif  c1=19 then set c2='2013-02-13 11:12:13.12345670';  " +
          "elseif  c1=20 then set c2='2013-02-13 11:12:13.123456780';  " +
          "elseif  c1=21 then set c2='2013-02-13 11:12:13.1234567890';  " +
          "elseif  c1=22 then set c2='2013-02-13 11:12:13.12345678910';  " +
          "elseif  c1=23 then set c2='2013-02-13 11:12:13.123456789010';  " +
          "elseif  c1=24 then set c2='2013-02-13 11:12:13.12345678900'; " +
          "elseif  c1=25 then set c2=NULL; " +

          " end if ;  end", 
          expectedValues);

  }


  public void Var053() {
    String[] expectedValues = {
    "2013-01-13 11:12:13.0",
    "2013-02-13 11:12:13.1",
    "2013-02-13 11:12:13.12",
    "2013-02-13 11:12:13.123",
    "2013-02-13 11:12:13.1234",
    "2013-02-13 11:12:13.12345",
    "2013-02-13 11:12:13.123456",
    "2013-02-13 11:12:13.1234567",
    "2013-02-13 11:12:13.12345678",
    "2013-02-13 11:12:13.123456789",
    "2013-02-13 11:12:13.1234567891",
    "2013-02-13 11:12:13.1234567892",
    "2013-02-13 11:12:13.1234567893",
    "2013-02-13 11:12:13.1",
    "2013-02-13 11:12:13.12",
    "2013-02-13 11:12:13.123",
    "2013-02-13 11:12:13.1234",
    "2013-02-13 11:12:13.12345",
    "2013-02-13 11:12:13.123456",
    "2013-02-13 11:12:13.1234567",
    "2013-02-13 11:12:13.12345678",
    "2013-02-13 11:12:13.123456789",
    "2013-02-13 11:12:13.1234567891",
    "2013-02-13 11:12:13.1234567891",
    "2013-02-13 11:12:13.123456789",
    "null",
    };
    
    testTimestampPrecision(collection_ + ".JDCSGT53", 
          "( in c1 int, out c2 timestamp(10)) begin  " +
          "if c1=0 then set c2='2013-01-13 11:12:13'; " +
          "elseif c1=1 then set c2='2013-02-13 11:12:13.1'; " +
          "elseif  c1=2 then set c2='2013-02-13 11:12:13.12';  " +
          "elseif  c1=3 then set c2='2013-02-13 11:12:13.123';  " +
          "elseif  c1=4 then set c2='2013-02-13 11:12:13.1234';  " +
          "elseif  c1=5 then set c2='2013-02-13 11:12:13.12345';  " +
          "elseif  c1=6 then set c2='2013-02-13 11:12:13.123456';  " +
          "elseif  c1=7 then set c2='2013-02-13 11:12:13.1234567';  " +
          "elseif  c1=8 then set c2='2013-02-13 11:12:13.12345678';  " +
          "elseif  c1=9 then set c2='2013-02-13 11:12:13.123456789';  " +
          "elseif  c1=10 then set c2='2013-02-13 11:12:13.1234567891';  " +
          "elseif  c1=11 then set c2='2013-02-13 11:12:13.1234567892';  " +
          "elseif  c1=12 then set c2='2013-02-13 11:12:13.1234567893'; " +
          "elseif  c1=13 then set c2='2013-02-13 11:12:13.100'; " +
          "elseif  c1=14 then set c2='2013-02-13 11:12:13.1200';  " +
          "elseif  c1=15 then set c2='2013-02-13 11:12:13.12300';  " +
          "elseif  c1=16 then set c2='2013-02-13 11:12:13.12340000';  " +
          "elseif  c1=17 then set c2='2013-02-13 11:12:13.1234500';  " +
          "elseif  c1=18 then set c2='2013-02-13 11:12:13.1234560';  " +
          "elseif  c1=19 then set c2='2013-02-13 11:12:13.12345670';  " +
          "elseif  c1=20 then set c2='2013-02-13 11:12:13.123456780';  " +
          "elseif  c1=21 then set c2='2013-02-13 11:12:13.1234567890';  " +
          "elseif  c1=22 then set c2='2013-02-13 11:12:13.12345678910';  " +
          "elseif  c1=23 then set c2='2013-02-13 11:12:13.123456789100';  " +
          "elseif  c1=24 then set c2='2013-02-13 11:12:13.12345678900'; " +
          "elseif  c1=25 then set c2=NULL; " +

          " end if ;  end", 
          expectedValues);

  }


  public void Var054() {
    String[] expectedValues = {
    /* 0 */ "2013-01-13 11:12:13.0",
    /* 1 */ "2013-02-13 11:12:13.1",
    /* 2 */ "2013-02-13 11:12:13.12",
    /* 3 */ "2013-02-13 11:12:13.123",
    /* 4 */ "2013-02-13 11:12:13.1234",
    /* 5 */ "2013-02-13 11:12:13.12345",
    /* 6 */ "2013-02-13 11:12:13.123456",
    /* 7 */ "2013-02-13 11:12:13.1234567",
    /* 8 */ "2013-02-13 11:12:13.12345678",
    /* 9 */ "2013-02-13 11:12:13.123456789",
    /*10 */ "2013-02-13 11:12:13.1",
    /*11 */ "2013-02-13 11:12:13.12",
    /*12 */ "2013-02-13 11:12:13.123",
    /*13 */ "2013-02-13 11:12:13.1234",
    /*14 */ "2013-02-13 11:12:13.12345",
    /*15 */ "2013-02-13 11:12:13.123456",
    /*16 */ "2013-02-13 11:12:13.1234567",
    /*17 */ "2013-02-13 11:12:13.12345678",
    /*18 */ "2013-02-13 11:12:13.123456789",
    /*19 */ "2013-02-13 11:12:13.123456789",
    /*20 */ "null",
    };
    
    testTimestampPrecision(collection_ + ".JDCSGT54", 
          "( in c1 int, out c2 timestamp(9)) begin  " +
          "if c1=0 then set c2='2013-01-13 11:12:13'; " +
          "elseif c1=1 then set c2='2013-02-13 11:12:13.1'; " +
          "elseif  c1=2 then set c2='2013-02-13 11:12:13.12';  " +
          "elseif  c1=3 then set c2='2013-02-13 11:12:13.123';  " +
          "elseif  c1=4 then set c2='2013-02-13 11:12:13.1234';  " +
          "elseif  c1=5 then set c2='2013-02-13 11:12:13.12345';  " +
          "elseif  c1=6 then set c2='2013-02-13 11:12:13.123456';  " +
          "elseif  c1=7 then set c2='2013-02-13 11:12:13.1234567';  " +
          "elseif  c1=8 then set c2='2013-02-13 11:12:13.12345678';  " +
          "elseif  c1=9 then set c2='2013-02-13 11:12:13.123456789';  " +
          "elseif  c1=10 then set c2='2013-02-13 11:12:13.100'; " +
          "elseif  c1=11 then set c2='2013-02-13 11:12:13.1200';  " +
          "elseif  c1=12 then set c2='2013-02-13 11:12:13.12300';  " +
          "elseif  c1=13 then set c2='2013-02-13 11:12:13.12340000';  " +
          "elseif  c1=14 then set c2='2013-02-13 11:12:13.1234500';  " +
          "elseif  c1=15 then set c2='2013-02-13 11:12:13.1234560';  " +
          "elseif  c1=16 then set c2='2013-02-13 11:12:13.12345670';  " +
          "elseif  c1=17 then set c2='2013-02-13 11:12:13.123456780';  " +
          "elseif  c1=18 then set c2='2013-02-13 11:12:13.1234567890';  " +
          "elseif  c1=19 then set c2='2013-02-13 11:12:13.12345678900'; " +
          "elseif  c1=20 then set c2=NULL; " +

          " end if ;  end", 
          expectedValues);

  }






  public void Var055() {
    String[] expectedValues = {
    /* 0 */ "2013-01-13 11:12:13.0",
    /* 1 */ "2013-02-13 11:12:13.1",
    /* 2 */ "2013-02-13 11:12:13.12",
    /* 3 */ "2013-02-13 11:12:13.123",
    /* 4 */ "2013-02-13 11:12:13.1234",
    /* 5 */ "2013-02-13 11:12:13.12345",
    /* 6 */ "2013-02-13 11:12:13.123456",
    /* 7 */ "2013-02-13 11:12:13.1234567",
    /* 8 */ "2013-02-13 11:12:13.12345678",
    /* 9 */ "2013-02-13 11:12:13.1",
    /*10 */ "2013-02-13 11:12:13.12",
    /*11 */ "2013-02-13 11:12:13.123",
    /*12 */ "2013-02-13 11:12:13.1234",
    /*13 */ "2013-02-13 11:12:13.12345",
    /*14 */ "2013-02-13 11:12:13.123456",
    /*15 */ "2013-02-13 11:12:13.1234567",
    /*16 */ "2013-02-13 11:12:13.12345678",
    /*17 */ "2013-02-13 11:12:13.12345678",
    /*18 */ "2013-02-13 11:12:13.12345678",
    /*19 */ "null",
    };
    
    testTimestampPrecision(collection_ + ".JDCSGT55", 
          "( in c1 int, out c2 timestamp(8)) begin  " +
          "if c1=0 then set c2='2013-01-13 11:12:13'; " +
          "elseif c1=1 then set c2='2013-02-13 11:12:13.1'; " +
          "elseif  c1=2 then set c2='2013-02-13 11:12:13.12';  " +
          "elseif  c1=3 then set c2='2013-02-13 11:12:13.123';  " +
          "elseif  c1=4 then set c2='2013-02-13 11:12:13.1234';  " +
          "elseif  c1=5 then set c2='2013-02-13 11:12:13.12345';  " +
          "elseif  c1=6 then set c2='2013-02-13 11:12:13.123456';  " +
          "elseif  c1=7 then set c2='2013-02-13 11:12:13.1234567';  " +
          "elseif  c1=8 then set c2='2013-02-13 11:12:13.12345678';  " +
          "elseif  c1=9 then set c2='2013-02-13 11:12:13.100'; " +
          "elseif  c1=10 then set c2='2013-02-13 11:12:13.1200';  " +
          "elseif  c1=11 then set c2='2013-02-13 11:12:13.12300';  " +
          "elseif  c1=12 then set c2='2013-02-13 11:12:13.12340000';  " +
          "elseif  c1=13 then set c2='2013-02-13 11:12:13.1234500';  " +
          "elseif  c1=14 then set c2='2013-02-13 11:12:13.1234560';  " +
          "elseif  c1=15 then set c2='2013-02-13 11:12:13.12345670';  " +
          "elseif  c1=16 then set c2='2013-02-13 11:12:13.123456780';  " +
          "elseif  c1=17 then set c2='2013-02-13 11:12:13.1234567800';  " +
          "elseif  c1=18 then set c2='2013-02-13 11:12:13.12345678000'; " +
          "elseif  c1=19 then set c2=NULL; " +

          " end if ;  end", 
          expectedValues);

  }



  public void Var056() {
    String[] expectedValues = {
    /* 0 */ "2013-01-13 11:12:13.0",
    /* 1 */ "2013-02-13 11:12:13.1",
    /* 2 */ "2013-02-13 11:12:13.12",
    /* 3 */ "2013-02-13 11:12:13.123",
    /* 4 */ "2013-02-13 11:12:13.1234",
    /* 5 */ "2013-02-13 11:12:13.12345",
    /* 6 */ "2013-02-13 11:12:13.123456",
    /* 7 */ "2013-02-13 11:12:13.1234567",
    /* 8 */ "2013-02-13 11:12:13.1234567",
    /* 9 */ "2013-02-13 11:12:13.1",
    /*10 */ "2013-02-13 11:12:13.12",
    /*11 */ "2013-02-13 11:12:13.123",
    /*12 */ "2013-02-13 11:12:13.1234",
    /*13 */ "2013-02-13 11:12:13.12345",
    /*14 */ "2013-02-13 11:12:13.123456",
    /*15 */ "2013-02-13 11:12:13.1234567",
    /*16 */ "2013-02-13 11:12:13.1234567",
    /*17 */ "2013-02-13 11:12:13.1234567",
    /*18 */ "2013-02-13 11:12:13.1234567",
    /*19 */ "null",
    };
    
    testTimestampPrecision(collection_ + ".JDCSGT56", 
          "( in c1 int, out c2 timestamp(7)) begin  " +
          "if c1=0 then set c2='2013-01-13 11:12:13'; " +
          "elseif c1=1 then set c2='2013-02-13 11:12:13.1'; " +
          "elseif  c1=2 then set c2='2013-02-13 11:12:13.12';  " +
          "elseif  c1=3 then set c2='2013-02-13 11:12:13.123';  " +
          "elseif  c1=4 then set c2='2013-02-13 11:12:13.1234';  " +
          "elseif  c1=5 then set c2='2013-02-13 11:12:13.12345';  " +
          "elseif  c1=6 then set c2='2013-02-13 11:12:13.123456';  " +
          "elseif  c1=7 then set c2='2013-02-13 11:12:13.1234567';  " +
          "elseif  c1=8 then set c2='2013-02-13 11:12:13.12345678';  " +
          "elseif  c1=9 then set c2='2013-02-13 11:12:13.100'; " +
          "elseif  c1=10 then set c2='2013-02-13 11:12:13.1200';  " +
          "elseif  c1=11 then set c2='2013-02-13 11:12:13.12300';  " +
          "elseif  c1=12 then set c2='2013-02-13 11:12:13.12340000';  " +
          "elseif  c1=13 then set c2='2013-02-13 11:12:13.1234500';  " +
          "elseif  c1=14 then set c2='2013-02-13 11:12:13.1234560';  " +
          "elseif  c1=15 then set c2='2013-02-13 11:12:13.12345670';  " +
          "elseif  c1=16 then set c2='2013-02-13 11:12:13.12345670';  " +
          "elseif  c1=17 then set c2='2013-02-13 11:12:13.123456700';  " +
          "elseif  c1=18 then set c2='2013-02-13 11:12:13.1234567000'; " +
          "elseif  c1=19 then set c2=NULL; " +

          " end if ;  end", 
          expectedValues);

  }



  public void Var057() {
    String[] expectedValues = {
    /* 0 */ "2013-01-13 11:12:13.0",
    /* 1 */ "2013-02-13 11:12:13.1",
    /* 2 */ "2013-02-13 11:12:13.12",
    /* 3 */ "2013-02-13 11:12:13.123",
    /* 4 */ "2013-02-13 11:12:13.1234",
    /* 5 */ "2013-02-13 11:12:13.12345",
    /* 6 */ "2013-02-13 11:12:13.123456",
    /* 7 */ "2013-02-13 11:12:13.123456",
    /* 8 */ "2013-02-13 11:12:13.123456",
    /* 9 */ "2013-02-13 11:12:13.1",
    /*10 */ "2013-02-13 11:12:13.12",
    /*11 */ "2013-02-13 11:12:13.123",
    /*12 */ "2013-02-13 11:12:13.1234",
    /*13 */ "2013-02-13 11:12:13.12345",
    /*14 */ "2013-02-13 11:12:13.123456",
    /*15 */ "2013-02-13 11:12:13.123456",
    /*16 */ "2013-02-13 11:12:13.123456",
    /*17 */ "2013-02-13 11:12:13.123456",
    /*18 */ "2013-02-13 11:12:13.123456",
    /*19 */ "null",
    };
    
    testTimestampPrecision(collection_ + ".JDCSGT57", 
          "( in c1 int, out c2 timestamp(6)) begin  " +
          "if c1=0 then set c2='2013-01-13 11:12:13'; " +
          "elseif c1=1 then set c2='2013-02-13 11:12:13.1'; " +
          "elseif  c1=2 then set c2='2013-02-13 11:12:13.12';  " +
          "elseif  c1=3 then set c2='2013-02-13 11:12:13.123';  " +
          "elseif  c1=4 then set c2='2013-02-13 11:12:13.1234';  " +
          "elseif  c1=5 then set c2='2013-02-13 11:12:13.12345';  " +
          "elseif  c1=6 then set c2='2013-02-13 11:12:13.123456';  " +
          "elseif  c1=7 then set c2='2013-02-13 11:12:13.1234567';  " +
          "elseif  c1=8 then set c2='2013-02-13 11:12:13.12345678';  " +
          "elseif  c1=9 then set c2='2013-02-13 11:12:13.100'; " +
          "elseif  c1=10 then set c2='2013-02-13 11:12:13.1200';  " +
          "elseif  c1=11 then set c2='2013-02-13 11:12:13.12300';  " +
          "elseif  c1=12 then set c2='2013-02-13 11:12:13.12340000';  " +
          "elseif  c1=13 then set c2='2013-02-13 11:12:13.1234500';  " +
          "elseif  c1=14 then set c2='2013-02-13 11:12:13.1234560';  " +
          "elseif  c1=15 then set c2='2013-02-13 11:12:13.1234560';  " +
          "elseif  c1=16 then set c2='2013-02-13 11:12:13.1234560';  " +
          "elseif  c1=17 then set c2='2013-02-13 11:12:13.12345600';  " +
          "elseif  c1=18 then set c2='2013-02-13 11:12:13.123456000'; " +
          "elseif  c1=19 then set c2=NULL; " +

          " end if ;  end", 
          expectedValues);

  }








  public void Var058() {
    String[] expectedValues = {
    /* 0 */ "2013-01-13 11:12:13.0",
    /* 1 */ "2013-02-13 11:12:13.1",
    /* 2 */ "2013-02-13 11:12:13.12",
    /* 3 */ "2013-02-13 11:12:13.123",
    /* 4 */ "2013-02-13 11:12:13.1234",
    /* 5 */ "2013-02-13 11:12:13.12345",
    /* 6 */ "2013-02-13 11:12:13.12345",
    /* 7 */ "2013-02-13 11:12:13.12345",
    /* 8 */ "2013-02-13 11:12:13.12345",
    /* 9 */ "2013-02-13 11:12:13.1",
    /*10 */ "2013-02-13 11:12:13.12",
    /*11 */ "2013-02-13 11:12:13.123",
    /*12 */ "2013-02-13 11:12:13.1234",
    /*13 */ "2013-02-13 11:12:13.12345",
    /*14 */ "2013-02-13 11:12:13.12345",
    /*15 */ "2013-02-13 11:12:13.12345",
    /*16 */ "2013-02-13 11:12:13.12345",
    /*17 */ "2013-02-13 11:12:13.12345",
    /*18 */ "2013-02-13 11:12:13.12345",
    /*19 */ "null",
    };
    
    testTimestampPrecision(collection_ + ".JDCSGT58", 
          "( in c1 int, out c2 timestamp(5)) begin  " +
          "if c1=0 then set c2='2013-01-13 11:12:13'; " +
          "elseif c1=1 then set c2='2013-02-13 11:12:13.1'; " +
          "elseif  c1=2 then set c2='2013-02-13 11:12:13.12';  " +
          "elseif  c1=3 then set c2='2013-02-13 11:12:13.123';  " +
          "elseif  c1=4 then set c2='2013-02-13 11:12:13.1234';  " +
          "elseif  c1=5 then set c2='2013-02-13 11:12:13.12345';  " +
          "elseif  c1=6 then set c2='2013-02-13 11:12:13.123456';  " +
          "elseif  c1=7 then set c2='2013-02-13 11:12:13.1234567';  " +
          "elseif  c1=8 then set c2='2013-02-13 11:12:13.12345678';  " +
          "elseif  c1=9 then set c2='2013-02-13 11:12:13.100'; " +
          "elseif  c1=10 then set c2='2013-02-13 11:12:13.1200';  " +
          "elseif  c1=11 then set c2='2013-02-13 11:12:13.12300';  " +
          "elseif  c1=12 then set c2='2013-02-13 11:12:13.12340000';  " +
          "elseif  c1=13 then set c2='2013-02-13 11:12:13.1234500';  " +
          "elseif  c1=14 then set c2='2013-02-13 11:12:13.1234560';  " +
          "elseif  c1=15 then set c2='2013-02-13 11:12:13.12345670';  " +
          "elseif  c1=16 then set c2='2013-02-13 11:12:13.123450';  " +
          "elseif  c1=17 then set c2='2013-02-13 11:12:13.1234500';  " +
          "elseif  c1=18 then set c2='2013-02-13 11:12:13.12345000'; " +
          "elseif  c1=19 then set c2=NULL; " +

          " end if ;  end", 
          expectedValues);

  }


  public void Var059() {
    String[] expectedValues = {
    /* 0 */ "2013-01-13 11:12:13.0",
    /* 1 */ "2013-02-13 11:12:13.1",
    /* 2 */ "2013-02-13 11:12:13.12",
    /* 3 */ "2013-02-13 11:12:13.123",
    /* 4 */ "2013-02-13 11:12:13.1234",
    /* 5 */ "2013-02-13 11:12:13.1234",
    /* 6 */ "2013-02-13 11:12:13.1234",
    /* 7 */ "2013-02-13 11:12:13.1234",
    /* 8 */ "2013-02-13 11:12:13.1234",
    /* 9 */ "2013-02-13 11:12:13.1",
    /*10 */ "2013-02-13 11:12:13.12",
    /*11 */ "2013-02-13 11:12:13.123",
    /*12 */ "2013-02-13 11:12:13.1234",
    /*13 */ "2013-02-13 11:12:13.1234",
    /*14 */ "2013-02-13 11:12:13.1234",
    /*15 */ "2013-02-13 11:12:13.1234",
    /*16 */ "2013-02-13 11:12:13.1234",
    /*17 */ "2013-02-13 11:12:13.1234",
    /*18 */ "2013-02-13 11:12:13.1234",
    /*19 */ "null",
    };
    
    testTimestampPrecision(collection_ + ".JDCSGT59", 
          "( in c1 int, out c2 timestamp(4)) begin  " +
          "if c1=0 then set c2='2013-01-13 11:12:13'; " +
          "elseif c1=1 then set c2='2013-02-13 11:12:13.1'; " +
          "elseif  c1=2 then set c2='2013-02-13 11:12:13.12';  " +
          "elseif  c1=3 then set c2='2013-02-13 11:12:13.123';  " +
          "elseif  c1=4 then set c2='2013-02-13 11:12:13.1234';  " +
          "elseif  c1=5 then set c2='2013-02-13 11:12:13.12345';  " +
          "elseif  c1=6 then set c2='2013-02-13 11:12:13.123456';  " +
          "elseif  c1=7 then set c2='2013-02-13 11:12:13.1234567';  " +
          "elseif  c1=8 then set c2='2013-02-13 11:12:13.12345678';  " +
          "elseif  c1=9 then set c2='2013-02-13 11:12:13.100'; " +
          "elseif  c1=10 then set c2='2013-02-13 11:12:13.1200';  " +
          "elseif  c1=11 then set c2='2013-02-13 11:12:13.12300';  " +
          "elseif  c1=12 then set c2='2013-02-13 11:12:13.12340000';  " +
          "elseif  c1=13 then set c2='2013-02-13 11:12:13.123400';  " +
          "elseif  c1=14 then set c2='2013-02-13 11:12:13.12340';  " +
          "elseif  c1=15 then set c2='2013-02-13 11:12:13.123470';  " +
          "elseif  c1=16 then set c2='2013-02-13 11:12:13.12340';  " +
          "elseif  c1=17 then set c2='2013-02-13 11:12:13.123400';  " +
          "elseif  c1=18 then set c2='2013-02-13 11:12:13.1234000'; " +
          "elseif  c1=19 then set c2=NULL; " +

          " end if ;  end", 
          expectedValues);

  }




  public void Var060() {
    String[] expectedValues = {
    /* 0 */ "2013-01-13 11:12:13.0",
    /* 1 */ "2013-02-13 11:12:13.1",
    /* 2 */ "2013-02-13 11:12:13.12",
    /* 3 */ "2013-02-13 11:12:13.123",
    /* 4 */ "2013-02-13 11:12:13.123",
    /* 5 */ "2013-02-13 11:12:13.123",
    /* 6 */ "2013-02-13 11:12:13.123",
    /* 7 */ "2013-02-13 11:12:13.123",
    /* 8 */ "2013-02-13 11:12:13.123",
    /* 9 */ "2013-02-13 11:12:13.1",
    /*10 */ "2013-02-13 11:12:13.12",
    /*11 */ "2013-02-13 11:12:13.123",
    /*12 */ "2013-02-13 11:12:13.123",
    /*13 */ "2013-02-13 11:12:13.123",
    /*14 */ "2013-02-13 11:12:13.123",
    /*15 */ "2013-02-13 11:12:13.123",
    /*16 */ "2013-02-13 11:12:13.123",
    /*17 */ "2013-02-13 11:12:13.123",
    /*18 */ "2013-02-13 11:12:13.123",
    /*19 */ "null",
    };
    
    testTimestampPrecision(collection_ + ".JDCSGT60", 
          "( in c1 int, out c2 timestamp(3)) begin  " +
          "if c1=0 then set c2='2013-01-13 11:12:13'; " +
          "elseif c1=1 then set c2='2013-02-13 11:12:13.1'; " +
          "elseif  c1=2 then set c2='2013-02-13 11:12:13.12';  " +
          "elseif  c1=3 then set c2='2013-02-13 11:12:13.123';  " +
          "elseif  c1=4 then set c2='2013-02-13 11:12:13.1234';  " +
          "elseif  c1=5 then set c2='2013-02-13 11:12:13.12345';  " +
          "elseif  c1=6 then set c2='2013-02-13 11:12:13.123456';  " +
          "elseif  c1=7 then set c2='2013-02-13 11:12:13.1234567';  " +
          "elseif  c1=8 then set c2='2013-02-13 11:12:13.12345678';  " +
          "elseif  c1=9 then set c2='2013-02-13 11:12:13.100'; " +
          "elseif  c1=10 then set c2='2013-02-13 11:12:13.1200';  " +
          "elseif  c1=11 then set c2='2013-02-13 11:12:13.12300';  " +
          "elseif  c1=12 then set c2='2013-02-13 11:12:13.12340000';  " +
          "elseif  c1=13 then set c2='2013-02-13 11:12:13.123400';  " +
          "elseif  c1=14 then set c2='2013-02-13 11:12:13.12340';  " +
          "elseif  c1=15 then set c2='2013-02-13 11:12:13.123470';  " +
          "elseif  c1=16 then set c2='2013-02-13 11:12:13.1230';  " +
          "elseif  c1=17 then set c2='2013-02-13 11:12:13.12300';  " +
          "elseif  c1=18 then set c2='2013-02-13 11:12:13.123000'; " +
          "elseif  c1=19 then set c2=NULL; " +

          " end if ;  end", 
          expectedValues);

  }




  public void Var061() {
    String[] expectedValues = {
    /* 0 */ "2013-01-13 11:12:13.0",
    /* 1 */ "2013-02-13 11:12:13.1",
    /* 2 */ "2013-02-13 11:12:13.12",
    /* 3 */ "2013-02-13 11:12:13.12",
    /* 4 */ "2013-02-13 11:12:13.12",
    /* 5 */ "2013-02-13 11:12:13.12",
    /* 6 */ "2013-02-13 11:12:13.12",
    /* 7 */ "2013-02-13 11:12:13.12",
    /* 8 */ "2013-02-13 11:12:13.12",
    /* 9 */ "2013-02-13 11:12:13.1",
    /*10 */ "2013-02-13 11:12:13.12",
    /*11 */ "2013-02-13 11:12:13.12",
    /*12 */ "2013-02-13 11:12:13.12",
    /*13 */ "2013-02-13 11:12:13.12",
    /*14 */ "2013-02-13 11:12:13.12",
    /*15 */ "2013-02-13 11:12:13.12",
    /*16 */ "2013-02-13 11:12:13.12",
    /*17 */ "2013-02-13 11:12:13.12",
    /*18 */ "2013-02-13 11:12:13.12",
    /*19 */ "null",
    };
    
    testTimestampPrecision(collection_ + ".JDCSGT61", 
          "( in c1 int, out c2 timestamp(2)) begin  " +
          "if c1=0 then set c2='2013-01-13 11:12:13'; " +
          "elseif c1=1 then set c2='2013-02-13 11:12:13.1'; " +
          "elseif  c1=2 then set c2='2013-02-13 11:12:13.12';  " +
          "elseif  c1=3 then set c2='2013-02-13 11:12:13.123';  " +
          "elseif  c1=4 then set c2='2013-02-13 11:12:13.1234';  " +
          "elseif  c1=5 then set c2='2013-02-13 11:12:13.12345';  " +
          "elseif  c1=6 then set c2='2013-02-13 11:12:13.123456';  " +
          "elseif  c1=7 then set c2='2013-02-13 11:12:13.1234567';  " +
          "elseif  c1=8 then set c2='2013-02-13 11:12:13.12345678';  " +
          "elseif  c1=9 then set c2='2013-02-13 11:12:13.100'; " +
          "elseif  c1=10 then set c2='2013-02-13 11:12:13.1200';  " +
          "elseif  c1=11 then set c2='2013-02-13 11:12:13.12300';  " +
          "elseif  c1=12 then set c2='2013-02-13 11:12:13.12340000';  " +
          "elseif  c1=13 then set c2='2013-02-13 11:12:13.123400';  " +
          "elseif  c1=14 then set c2='2013-02-13 11:12:13.12340';  " +
          "elseif  c1=15 then set c2='2013-02-13 11:12:13.123470';  " +
          "elseif  c1=16 then set c2='2013-02-13 11:12:13.120';  " +
          "elseif  c1=17 then set c2='2013-02-13 11:12:13.1200';  " +
          "elseif  c1=18 then set c2='2013-02-13 11:12:13.12000'; " +
          "elseif  c1=19 then set c2=NULL; " +

          " end if ;  end", 
          expectedValues);

  }


  public void Var062() {
    String[] expectedValues = {
    /* 0 */ "2013-01-13 11:12:13.0",
    /* 1 */ "2013-02-13 11:12:13.1",
    /* 2 */ "2013-02-13 11:12:13.1",
    /* 3 */ "2013-02-13 11:12:13.1",
    /* 4 */ "2013-02-13 11:12:13.1",
    /* 5 */ "2013-02-13 11:12:13.1",
    /* 6 */ "2013-02-13 11:12:13.1",
    /* 7 */ "2013-02-13 11:12:13.1",
    /* 8 */ "2013-02-13 11:12:13.1",
    /* 9 */ "2013-02-13 11:12:13.1",
    /*10 */ "2013-02-13 11:12:13.1",
    /*11 */ "2013-02-13 11:12:13.1",
    /*12 */ "2013-02-13 11:12:13.1",
    /*13 */ "2013-02-13 11:12:13.1",
    /*14 */ "2013-02-13 11:12:13.1",
    /*15 */ "2013-02-13 11:12:13.1",
    /*16 */ "2013-02-13 11:12:13.1",
    /*17 */ "2013-02-13 11:12:13.1",
    /*18 */ "2013-02-13 11:12:13.1",
    /*19 */ "null",
    };
    
    testTimestampPrecision(collection_ + ".JDCSGT62", 
          "( in c1 int, out c2 timestamp(1)) begin  " +
          "if c1=0 then set c2='2013-01-13 11:12:13'; " +
          "elseif c1=1 then set c2='2013-02-13 11:12:13.1'; " +
          "elseif  c1=2 then set c2='2013-02-13 11:12:13.12';  " +
          "elseif  c1=3 then set c2='2013-02-13 11:12:13.123';  " +
          "elseif  c1=4 then set c2='2013-02-13 11:12:13.1234';  " +
          "elseif  c1=5 then set c2='2013-02-13 11:12:13.12345';  " +
          "elseif  c1=6 then set c2='2013-02-13 11:12:13.123456';  " +
          "elseif  c1=7 then set c2='2013-02-13 11:12:13.1234567';  " +
          "elseif  c1=8 then set c2='2013-02-13 11:12:13.12345678';  " +
          "elseif  c1=9 then set c2='2013-02-13 11:12:13.100'; " +
          "elseif  c1=10 then set c2='2013-02-13 11:12:13.1200';  " +
          "elseif  c1=11 then set c2='2013-02-13 11:12:13.12300';  " +
          "elseif  c1=12 then set c2='2013-02-13 11:12:13.12340000';  " +
          "elseif  c1=13 then set c2='2013-02-13 11:12:13.123400';  " +
          "elseif  c1=14 then set c2='2013-02-13 11:12:13.12340';  " +
          "elseif  c1=15 then set c2='2013-02-13 11:12:13.123470';  " +
          "elseif  c1=16 then set c2='2013-02-13 11:12:13.10';  " +
          "elseif  c1=17 then set c2='2013-02-13 11:12:13.100';  " +
          "elseif  c1=18 then set c2='2013-02-13 11:12:13.1000'; " +
          "elseif  c1=19 then set c2=NULL; " +

          " end if ;  end", 
          expectedValues);

  }


  public void Var063() {
    String[] expectedValues = {
    /* 0 */ "2013-01-13 11:12:13.0",
    /* 1 */ "2013-02-13 11:12:13.0",
    /* 2 */ "2013-02-13 11:12:13.0",
    /* 3 */ "2013-02-13 11:12:13.0",
    /* 4 */ "2013-02-13 11:12:13.0",
    /* 5 */ "2013-02-13 11:12:13.0",
    /* 6 */ "2013-02-13 11:12:13.0",
    /* 7 */ "2013-02-13 11:12:13.0",
    /* 8 */ "2013-02-13 11:12:13.0",
    /* 9 */ "2013-02-13 11:12:13.0",
    /*10 */ "2013-02-13 11:12:13.0",
    /*11 */ "2013-02-13 11:12:13.0",
    /*12 */ "2013-02-13 11:12:13.0",
    /*13 */ "2013-02-13 11:12:13.0",
    /*14 */ "2013-02-13 11:12:13.0",
    /*15 */ "2013-02-13 11:12:13.0",
    /*16 */ "2013-02-13 11:12:13.0",
    /*17 */ "2013-02-13 11:12:13.0",
    /*18 */ "2013-02-13 11:12:13.0",
    /*19 */ "null",
    };
    
    testTimestampPrecision(collection_ + ".JDCSGT63", 
          "( in c1 int, out c2 timestamp(0)) begin  " +
          "if c1=0 then set c2='2013-01-13 11:12:13'; " +
          "elseif c1=1 then set c2='2013-02-13 11:12:13.1'; " +
          "elseif  c1=2 then set c2='2013-02-13 11:12:13.12';  " +
          "elseif  c1=3 then set c2='2013-02-13 11:12:13.123';  " +
          "elseif  c1=4 then set c2='2013-02-13 11:12:13.1234';  " +
          "elseif  c1=5 then set c2='2013-02-13 11:12:13.12345';  " +
          "elseif  c1=6 then set c2='2013-02-13 11:12:13.123456';  " +
          "elseif  c1=7 then set c2='2013-02-13 11:12:13.1234567';  " +
          "elseif  c1=8 then set c2='2013-02-13 11:12:13.12345678';  " +
          "elseif  c1=9 then set c2='2013-02-13 11:12:13.100'; " +
          "elseif  c1=10 then set c2='2013-02-13 11:12:13.1200';  " +
          "elseif  c1=11 then set c2='2013-02-13 11:12:13.12300';  " +
          "elseif  c1=12 then set c2='2013-02-13 11:12:13.12340000';  " +
          "elseif  c1=13 then set c2='2013-02-13 11:12:13.123400';  " +
          "elseif  c1=14 then set c2='2013-02-13 11:12:13.12340';  " +
          "elseif  c1=15 then set c2='2013-02-13 11:12:13.123470';  " +
          "elseif  c1=16 then set c2='2013-02-13 11:12:13.0';  " +
          "elseif  c1=17 then set c2='2013-02-13 11:12:13.00';  " +
          "elseif  c1=18 then set c2='2013-02-13 11:12:13.000'; " +
          "elseif  c1=19 then set c2=NULL; " +

          " end if ;  end", 
          expectedValues);

  }


  /**
  getTimeStamp() - Get a type that was registered as a BOOLEAN.
  **/
     
     public void Var064()
     {
        if (checkBooleanSupport()) {
          getTestFailed(csTypes_,"getTimestamp",25,"Data type mismatch",""); 
         
        }
     }

     
     /**
     getTimeStamp() - Get a type that was registered as a BOOLEAN.
     **/
        
        public void Var065()
        {
           if (checkBooleanSupport()) {
             getTestFailed(csTypes_,"getTimestamp","P_BOOLEAN","Data type mismatch",""); 
            
           }
        }




}
