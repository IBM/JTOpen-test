///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetDate.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetDate.java
//
// Classes:      JDCSGetDate
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
import java.sql.Date;
import java.sql.Statement;
import java.sql.Types;
import java.util.Calendar;
import java.util.Hashtable; import java.util.Vector;



/**
Testcase JDCSGetDate.  This tests the following
method of the JDBC CallableStatement class:

<ul>
<li>getDate()
</ul>
**/
public class JDCSGetDate
extends JDCSGetTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSGetDate";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCSTest.main(newArgs); 
   }

/**
Constructor.
**/
   public JDCSGetDate (AS400 systemObject,
                       Hashtable<String,Vector<String>> namesAndVars,
                       int runMode,
                       FileOutputStream fileOutputStream,
                       
                       String password)
   {
      super (systemObject, "JDCSGetDate",
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
getDate() - Get parameter -1.
**/
   public void Var001()
   {
      try {
         csTypes_.execute ();
         Date p = csTypes_.getDate (-1);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }

/**
getDate() - Get parameter 0.
**/
   public void Var002()
   {
      try {
         Date p = csTypes_.getDate (0);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }

/**
getDate() - Use a parameter that is too big.
**/
   public void Var003()
   {
      try {
         Date p = csTypes_.getDate (35);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }

/**
getDate() - Get a parameter when there are no parameters.
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
         Date p = cs.getDate (1);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      } 
      if (c != null) try { c.close(); } catch (Exception e) {} 
   }

/**
getDate() - Get a parameter that was not registered.
**/
   public void Var005()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
         cs.execute ();
         Date p = cs.getDate (15);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }

/**
getDate() - Get a parameter when the statement has not been
executed.
**/
   public void Var006()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
         cs.registerOutParameter (15, Types.DATE);
         Date p = cs.getDate (15);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }

/**
getDate() - Get a parameter when the statement is closed.
**/
   public void Var007()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
         cs.registerOutParameter (15, Types.DATE);
         cs.execute ();
         cs.close ();
         Date p = cs.getDate (15);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }

/**
getDate() - Get an IN parameter that was correctly registered.
**/
   public void Var008()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESIN, supportedFeatures_);
         JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESIN,
                                              supportedFeatures_);
         cs.registerOutParameter (15, Types.DATE);
         cs.execute ();
         Date p = cs.getDate (15);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }

/**
getDate() - Get an INOUT parameter.
**/
   public void Var009()
   {
       if (getDriver() == JDTestDriver.DRIVER_JCC) {
	   notApplicable("JCC fails with the jdbcType 8 does match between the set method and the registerOutParameter method"); 
	   return; 
       }
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_);
         JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                              supportedFeatures_);
         JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                    supportedFeatures_, getDriver());
         cs.registerOutParameter (15, Types.DATE);
         cs.execute ();
         Date p = cs.getDate (15);
         assertCondition (p.toString().equals("1998-04-15"));
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }

/**
getDate() - Get a parameter when the calendar is null.
**/
   public void Var010()
   {
      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
         cs.registerOutParameter (15, Types.DATE);
         cs.execute ();
         Date p = cs.getDate (15, null);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }

/**
getDate() - Get a type that was registered as a SMALLINT.
**/
   public void Var011()
   {
      try {
         Date p = csTypes_.getDate (1);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getDate() - Get a type that was registered as an INTEGER.
**/
   public void Var012()
   {
      try {
         Date p = csTypes_.getDate (2);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getDate() - Get a type that was registered as an REAL.
**/
   public void Var013()
   {
      try {
         Date p = csTypes_.getDate (3);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getDate() - Get a type that was registered as an FLOAT.
**/
   public void Var014()
   {
      try {
         Date p = csTypes_.getDate (4);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getDate() - Get a type that was registered as an DOUBLE.
**/
   public void Var015()
   {
      try {
         Date p = csTypes_.getDate (5);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getDate() - Get a type that was registered as an DECIMAL.
**/
   public void Var016()
   {
      try {
         Date p = csTypes_.getDate (6);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getDate() - Get a type that was registered as an NUMERIC.
**/
   public void Var017()
   {
      try {
         Date p = csTypes_.getDate (8);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getDate() - Get a type that was registered as a CHAR.
**/
   public void Var018()
   {
      try {
         Date p = csTypes_.getDate (11);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getDate() - Get a type that was registered as a VARCHAR.
**/
   public void Var019()
   {
      try {
         Date p = csTypes_.getDate (12);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getDate() - Get a type that was registered as a BINARY.
**/
   public void Var020()
   {
      try {
         Date p = csTypes_.getDate (13);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getDate() - Get a type that was registered as a VARBINARY.
**/
   public void Var021()
   {
      try {
         Date p = csTypes_.getDate (14);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getDate() - Get a type that was registered as a DATE.
**/
   public void Var022()
   {
      try {
         Date p = csTypes_.getDate (15);
         assertCondition (p.toString().equals("1998-04-15"));
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }



/**
getDate() - Get a type that was registered as a DATE, with a
calendar specifed.
**/
   public void Var023()
   {
      if (checkJdbc20 ()) {
         try {

	    Calendar cal = Calendar.getInstance ();
	    System.out.println("Using "+cal); 
	    // Passing the calendar means to use this calendar to get the timezone offset.
            // Passing a buddhist calendar to jcc will get return the expected string.
	    // 
            Date p = csTypes_.getDate (15, cal);
	    System.out.println("Result is "+p.getClass().getName()+"="+p); 
	    String out = p.toString();
	    String expected = "1998-04-15"; 
            assertCondition (out.equals(expected), "Got "+out+" expected "+expected+" for "+cal+" class="+csTypes_.getClass());
         } catch (Exception e)
         {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
getDate() - Get a type that was registered as a TIME.
**/
   public void Var024()
   {
      try {
         Date p = csTypes_.getDate (16);
         failed ("Didn't throw SQLException "+p);
      } catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }



/**
getDate() - Get a type that was registered as a TIMESTAMP.
**/
   public void Var025()
   {
     // As of 10/3/2011 for the toolbox driver, it is now possible to get a date
     // from a parameter registered as timestamp
     // Note: JCC does not throw exception either.
     // Native JDBC will now need to change.
     //
     String info = " -- call getDate against TIMESTAMP -- Changed October 2011";

     try {
         Date p = csTypes_.getDate (17);

         String expected = "2001-11-18";
         assertCondition(p.toString().equals(expected), "got "+p+" expected "+expected+info); 
      } catch (Exception e) {
        failed (e, "Unexpected Exception "+info);
      }
   }



/**
getDate() - Get a type that was registered as an OTHER.
**/
   public void Var026()
   {
      if (checkLobSupport ()) {
         try {
            Date p = csTypes_.getDate (18);
            failed ("Didn't throw SQLException "+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getDate() - Get a type that was registered as a BLOB.
**/
   public void Var027()
   {
      if (checkLobSupport ()) {
         try {
            Date p = csTypes_.getDate (19);
            failed ("Didn't throw SQLException "+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getDate() - Get a type that was registered as a CLOB.
**/
   public void Var028()
   {
      if (checkLobSupport ()) {
         try {
            Date p = csTypes_.getDate (20);
            failed ("Didn't throw SQLException "+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getDate() - Get a type that was registered as a BIGINT.
**/
   
   public void Var029()
   {
      if (checkBigintSupport()) {
        getTestFailed(csTypes_,"getDate",22,"Data type mismatch",""); 
       
      }
   }



/**
getDate() - Get an INOUT parameter, when the output parameter is registered first.

SQL400 - We added this testcase because of a customer bug.
**/
   public void Var030()
   {

       if (getDriver() == JDTestDriver.DRIVER_JCC) {
	   notApplicable("JCC fails with the jdbcType 7 does match between the set method and the registerOutParameter method"); 
	   return; 
       }

      try {
         CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                          JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_);
         JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                    supportedFeatures_, getDriver());
         cs.registerOutParameter (15, Types.DATE);
         JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                              supportedFeatures_);
         cs.execute ();
         Date p = cs.getDate (15);
         assertCondition (p.toString().equals("1998-04-15"));
      } catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }


/**
getDate() -- Get an output parameter where the database output type isn't necessarily a date 
*/
   public void Var031()
   {
      try {
          if(isToolboxDriver())                                                    //@E3A
                connection_ = testDriver_.getConnection (baseURL_                                           //@E3A
                                               + ";errors=full;date format=ymd;", userId_, encryptedPassword_);      //@E3A
	  Statement stmt = connection_.createStatement();
	  
	  try {
	      stmt.executeUpdate("drop table "+JDCSTest.COLLECTION+".DATE_tab");
	  } catch (Exception e) {}
	  try {
	      stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".DATE_Proc"); 
	  } catch (Exception e) {}

	  stmt.executeUpdate("create table "+JDCSTest.COLLECTION+".DATE_Tab(MAX_VAL VARCHAR(20), MIN_VAL VARCHAR(20), NULL_VAL VARCHAR(20) )"); 
	  stmt.executeUpdate("create procedure "+JDCSTest.COLLECTION+".DATE_Proc (out MAX_PARAM VARCHAR(20), out MIN_PARAM VARCHAR(20), out NULL_PARAM VARCHAR(20)) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from "+JDCSTest.COLLECTION+".DATE_Tab; end");

	  String maxString = "2002-08-08";
	  String minString = "1980-01-01"; 

	  stmt.executeUpdate("insert into "+JDCSTest.COLLECTION+".DATE_Tab values('"+maxString+"','"+minString+"',null)");
	  CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".DATE_Proc(?,?,?)}");

		//register the output parameters
	  cstmt.registerOutParameter(1,java.sql.Types.DATE);
	  cstmt.registerOutParameter(2,java.sql.Types.DATE);
	  cstmt.registerOutParameter(3,java.sql.Types.DATE);

		//execute the procedure
	  cstmt.execute();

		//invoke getByte method
	  Date xRetVal = cstmt.getDate(1);
	  Date yRetVal = cstmt.getDate(2);
	  Date zRetVal = cstmt.getDate(3);
	  Date xCheck  = Date.valueOf(maxString);
	  Date yCheck  = Date.valueOf(minString);
	  boolean condition =  (xRetVal.equals(xCheck)) && (yRetVal.equals(yCheck)) && (zRetVal == null);
	  if (!condition) {
	      System.out.println("xRetVal ("+xRetVal+"="+xRetVal.getTime()+") != xCheck ("+xCheck+"="+xCheck.getTime()+")" ); 
	      System.out.println("yRetVal ("+yRetVal+"="+yRetVal.getTime()+") != yCheck ("+yCheck+"="+xCheck.getTime()+")");
	      System.out.println("zRetVal ("+zRetVal+") != null"); 
	  } 
	  assertCondition(condition); 

      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
      finally                                                                //@E3A
      {
          try{
              connection_ = testDriver_.getConnection (baseURL_
                                               + ";errors=full;", userId_, encryptedPassword_);
          }
          catch(Exception e)
          {
          }
      }
   }


/**
getDate() -- Get an output parameter where the database output type isn't necessarily a Date and the version is not valid 
*/
   public void Var032()
   {
      try {
	  Statement stmt = connection_.createStatement();
	  
	  try {
	      stmt.executeUpdate("drop table "+JDCSTest.COLLECTION+".DATE_tab");
	  } catch (Exception e) {}
	  try {
	      stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".DATE_Proc"); 
	  } catch (Exception e) {}

	  stmt.executeUpdate("create table "+JDCSTest.COLLECTION+".DATE_Tab(MAX_VAL VARCHAR(20), MIN_VAL VARCHAR(20), NULL_VAL VARCHAR(20) )"); 
	  stmt.executeUpdate("create procedure "+JDCSTest.COLLECTION+".DATE_Proc (out MAX_PARAM VARCHAR(20), out MIN_PARAM VARCHAR(20), out NULL_PARAM VARCHAR(20)) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from "+JDCSTest.COLLECTION+".DATE_Tab; end");

	  String maxString = "abcdefgh";
	  String minString = "00300400"; 
	  stmt.executeUpdate("insert into "+JDCSTest.COLLECTION+".DATE_Tab values('"+maxString+"','"+minString+"',null)");
	  CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".DATE_Proc(?,?,?)}");

		//register the output parameters
	  cstmt.registerOutParameter(1,java.sql.Types.DATE);
	  cstmt.registerOutParameter(2,java.sql.Types.DATE);
	  cstmt.registerOutParameter(3,java.sql.Types.DATE);

		//execute the procedure
	  cstmt.execute();

		//invoke getByte method
	  Date xRetVal = cstmt.getDate(1);
	  Date yRetVal = cstmt.getDate(2);
	  Date zRetVal = cstmt.getDate(3);
	  System.out.println("xRetVal ("+xRetVal+"="+xRetVal.getTime()+") "); 
	  System.out.println("yRetVal ("+yRetVal+"="+yRetVal.getTime()+")"); 
	  System.out.println("zRetVal ("+zRetVal+")"); 

	  failed("Didn't throw exception"); 

      } catch (Exception e) {
          //
          // For the native driver, this is a data type mismatch
          // 
	  assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
   }

// - named parameter
/**
getDate() - Get a type that was registered as a SMALLINT.
**/
   public void Var033()
   {
       if(checkNamedParametersSupport())
       {
	   try {
	       Date p = csTypes_.getDate ("P_SMALLINT");
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }
/**
getDate() - Get a type that was registered as an INTEGER.
**/
   public void Var034()
   {
       if(checkNamedParametersSupport())
       {
	   try {
	       Date p = csTypes_.getDate ("P_INTEGER");
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }
/**
getDate() - Get a type that was registered as an REAL.
**/
   public void Var035()
   {
       if(checkNamedParametersSupport())
       {
	   try {
	       Date p = csTypes_.getDate ("P_REAL");
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }
/**
getDate() - Get a type that was registered as an FLOAT.
**/
   public void Var036()
   {
       if(checkNamedParametersSupport())
       {
	   try {
	       Date p = csTypes_.getDate ("P_FLOAT");
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }
/**
getDate() - Get a type that was registered as an DOUBLE.
**/
   public void Var037()
   {
       if(checkNamedParametersSupport())
       {
	   try {
	       Date p = csTypes_.getDate ("P_DOUBLE");
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }	
   }
/**
getDate() - Get a type that was registered as an DECIMAL.
**/
   public void Var038()
   {
       if(checkNamedParametersSupport())
       {
	   try {
	       Date p = csTypes_.getDate ("P_DECIMAL_50");
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }
/**
getDate() - Get a type that was registered as an DECIMAL.
**/
   public void Var039()
   {
       if(checkNamedParametersSupport())
       {
	   try {
	       Date p = csTypes_.getDate ("P_DECIMAL_105");
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }
/**
getDate() - Get a type that was registered as an NUMERIC.
**/
   public void Var040()
   {
       if(checkNamedParametersSupport())
       {
	   try {
	       Date p = csTypes_.getDate ("P_NUMERIC_50");
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }
/**
getDate() - Get a type that was registered as an NUMERIC.
**/
   public void Var041()
   {
       if(checkNamedParametersSupport())
       {
	   try {
	       Date p = csTypes_.getDate ("P_NUMERIC_105");
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }
/**
getDate() - Get a type that was registered as a CHAR.
**/
   public void Var042()
   {
       if(checkNamedParametersSupport())
       {
	   try {
	       Date p = csTypes_.getDate ("P_CHAR_1");
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }
/**
getDate() - Get a type that was registered as a CHAR.
**/
   public void Var043()
   {
       if(checkNamedParametersSupport())
       {
	   try {
	       Date p = csTypes_.getDate ("P_CHAR_50");
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }
/**
getDate() - Get a type that was registered as a VARCHAR.
**/
   public void Var044()
   {
       if(checkNamedParametersSupport())
       {
	   try {
	       Date p = csTypes_.getDate ("P_VARCHAR_20");
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }
/**
getDate() - Get a type that was registered as a BINARY.
**/
   public void Var045()
   {
       if(checkNamedParametersSupport())
       {
	   try {
	       Date p = csTypes_.getDate ("P_BINARY_20");
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }
/**
getDate() - Get a type that was registered as a VARBINARY.
**/
   public void Var046()
   {
       if(checkNamedParametersSupport())
       {
	   try {
	       Date p = csTypes_.getDate ("P_VARBINARY_20");
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }
/**
getDate() - Get a type that was registered as a DATE.
**/
   public void Var047()
   {
       if (getDriver() == JDTestDriver.DRIVER_JCC) {
	   notApplicable("JCC fails with method not supported for target server"); 
	   return; 
       }

       if(checkNamedParametersSupport())
       {
	   try {
	       Date p = csTypes_.getDate ("P_DATE");
	       assertCondition (p.toString().equals("1998-04-15"));
	   } catch (Exception e)
	   {
	       failed (e, "Unexpected Exception");
	   }
       }
   }
/**
getDate() - Get a type that was registered as a DATE, with a
calendar specifed.
**/
   public void Var048()
   {
       if (getDriver() == JDTestDriver.DRIVER_JCC) {
	   notApplicable("JCC fails with method not supported for target server"); 
	   return; 
       }

       if(checkNamedParametersSupport())
       {
	   if (checkJdbc20 ()) {
	       try {
		   Calendar cal = Calendar.getInstance ();
		   String expected = "1998-04-15"; 
		   Date p = csTypes_.getDate ("P_DATE", cal);
		   String out = p.toString(); 
		   assertCondition (out.equals(expected), "Got "+out+" expected "+expected+" for "+cal);
	       } catch (Exception e)
	       {
		   failed (e, "Unexpected Exception");
	       }
	   }
       }
   }
/**
getDate() - Get a type that was registered as a TIME.
**/
   public void Var049()
   {
       if(checkNamedParametersSupport())
       {
	   try {
	       Date p = csTypes_.getDate ("P_TIME");
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }
/**
getDate() - Get a type that was registered as a TIMESTAMP.
**/
   public void Var050()
   {
       if(checkNamedParametersSupport())
       {
	     String info = " -- call getDate against TIMESTAMP -- Changed October 2011";

	     try {
	         Date p = csTypes_.getDate ("P_TIMESTAMP");

	         String expected = "2001-11-18";
	         assertCondition(p.toString().equals(expected), "got "+p+" expected "+expected+info); 
	      } catch (Exception e) {
	        failed (e, "Unexpected Exception "+info);
	      }

       }
   }
/**
getDate() - Get a type that was registered as an OTHER.
**/
   public void Var051()
   {
      if(checkNamedParametersSupport())
      {
         try {
            Date p = csTypes_.getDate ("P_DATALINK");
            failed ("Didn't throw SQLException "+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**
getDate() - Get a type that was registered as a BLOB.
**/
   public void Var052()
   {
      if(checkNamedParametersSupport())
      { 
         try {
            Date p = csTypes_.getDate ("P_BLOB");
            failed ("Didn't throw SQLException "+p);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }

/**
getDate() - Get a type that was registered as a CLOB.
**/
   public void Var053()
   {
       if(checkNamedParametersSupport())
       {
	   if (checkLobSupport()){
	       try {
		   Date p = csTypes_.getDate ("P_CLOB");
		   failed ("Didn't throw SQLException "+p);
	       } catch (Exception e) {
		   assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	       }
	   }
       }
   }
/**
getDate() - Get a type that was registered as a BIGINT.
**/
   public void Var054()
   {
       if(checkNamedParametersSupport())
       {
	   if (checkBigintSupport()) {
	       try {
		   Date p = csTypes_.getDate ("P_BIGINT");
		   failed ("Didn't throw SQLException "+p);
	       } catch (Exception e) {
		   assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	       }
	   }
       }
   }
   
   
   /**
   getDate() - Get a type that was registered as a BOOLEAN.
   **/
      
      public void Var055()
      {
         if (checkBooleanSupport()) {
           getTestFailed(csTypes_,"getDate",25,"Data type mismatch",""); 
          
         }
      }

      
      /**
      getDate() - Get a type that was registered as a BOOLEAN.
      **/
         
         public void Var056()
         {
            if (checkBooleanSupport()) {
              getTestFailed(csTypes_,"getDate","P_BOOLEAN","Data type mismatch",""); 
             
            }
         }

}
