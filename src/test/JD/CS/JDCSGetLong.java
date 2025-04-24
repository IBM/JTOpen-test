///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetLong.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetLong.java
//
// Classes:      JDCSGetLong
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDSetupProcedure;


/**
Testcase JDCSGetLong.  This tests the following
method of the JDBC CallableStatement class:

<ul>
<li>getLong()
</ul>
**/
public class JDCSGetLong
extends JDCSGetTestcase 
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSGetLong";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCSTest.main(newArgs); 
   }



  


/**
Constructor.
**/
    public JDCSGetLong (AS400 systemObject,
                        Hashtable<String,Vector<String>> namesAndVars,
                        int runMode,
                        FileOutputStream fileOutputStream,
                        
                        String password)
    {
        super (systemObject, "JDCSGetLong",
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
getLong() - Get parameter -1.
**/
    public void Var001()
    {
        try {
            csTypes_.execute ();
            long p = csTypes_.getLong (-1);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getLong() - Get parameter 0.
**/
    public void Var002()
    {
        try {
            long p = csTypes_.getLong (0);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getLong() - Use a parameter that is too big.
**/
    public void Var003()
    {
        try {
            long p = csTypes_.getLong (35);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getLong() - Get a parameter when there are no parameters.
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
            long p = cs.getLong (1);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        if (c != null) try { c.close(); } catch (Exception e) {} 
    }



/**
getLong() - Get a parameter that was not registered.
**/
    public void Var005()
    {
        try {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
            cs.execute ();
            long p = cs.getLong (2);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getLong() - Get a parameter when the statement has not been
executed.
**/
    public void Var006()
    {
        try {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
            long p; 
            if (areBigintsSupported()) {
                cs.registerOutParameter (22, Types.BIGINT);
                p = cs.getLong (22);
            }
            else {
                cs.registerOutParameter (2, Types.INTEGER);
                p = cs.getLong (2);
            }
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getLong() - Get a parameter when the statement is closed.
**/
    public void Var007()
    {
        if (areBigintsSupported()) {
            try {
                CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                                 JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
                cs.registerOutParameter (22, Types.BIGINT);
                cs.execute ();
                cs.close ();
                long p = cs.getLong (22);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        else {
            try {
                CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                                 JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
                cs.registerOutParameter (2, Types.INTEGER);
                cs.execute ();
                cs.close ();
                long p = cs.getLong (2);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getLong() - Get an IN parameter that was correctly registered.
**/
    public void Var008()
    {
        if (areBigintsSupported()) {
            try {
                CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                                 JDSetupProcedure.STP_CSTYPESIN, supportedFeatures_);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESIN,
                                                     supportedFeatures_);
                cs.registerOutParameter (22, Types.BIGINT);
                cs.execute ();
                long p = cs.getLong (22);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        else {
            try {
                CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                                 JDSetupProcedure.STP_CSTYPESIN, supportedFeatures_);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESIN,
                                                     supportedFeatures_);
                cs.registerOutParameter (2, Types.INTEGER);
                cs.execute ();
                long p = cs.getLong (2);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


/**
getLong() - Get an INOUT parameter, where the OUT parameter is
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
            if (areBigintsSupported()) {
                cs.registerOutParameter (22, Types.BIGINT);
                cs.execute ();
                long p = cs.getLong (22);
                assertCondition (p == 987662234567l);
            }
            else {
                cs.registerOutParameter (2, Types.INTEGER);
                cs.execute ();
                long p = cs.getLong (2);
                assertCondition (p == -102);
            }
        }
        catch(Exception e) {
          failed (e, "Unexpected Exception");
        }
    }


/**
getLong() - Get a type that was registered as a SMALLINT.
**/
    public void Var010()
    {
      
      String info = " -- call getLong against SMALLINT -- Changed October 2011";

      // As of 10/3/2011 for the toolbox driver, it is now possible to get an integer
      // from a parameter registered as smallint.
      // Note: JCC does not throw exception either.
      // Native JDBC will now need to change.
      //
        try {
            long p = csTypes_.getLong (1);
            long expected  = 123; 
            assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
        }
        catch(Exception e) {
          failed (e, "Unexpected Exception"+info);
        }
    }



/**
getLong() - Get a type that was registered as an INTEGER.
**/
    public void Var011()
    {
        if (areBigintsSupported()) {
          String info = " -- call getLong against INTEGER -- Changed October 2011";
            try {
                long p = csTypes_.getLong (2);                  
                long expected  = -456; 
                assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
            }
            catch(Exception e) {
              failed (e, "Unexpected Exception"+info);
            }
        }
        else {
            try {
                long p = csTypes_.getLong (2);                  
                assertCondition (p == -456);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getLong() - Get a type that was registered as an REAL.
**/
    public void Var012()
    {
      String info = " -- call getLong against REAL -- Changed October 2011";
        try {
            long p = csTypes_.getLong (3);
            long expected  = 789; 
            assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
        }
        catch(Exception e) {
          failed (e, "Unexpected Exception"+info);
        }
    }



/**
getLong() - Get a type that was registered as an FLOAT.
**/
    public void Var013()
    {
      String info = " -- call getLong against FLOAT -- Changed October 2011";
        try {
            long p = csTypes_.getLong (4);
            long expected  = 253; 
            assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
        }
        catch(Exception e) {
          failed (e, "Unexpected Exception"+info);
        }
    }



/**
getLong() - Get a type that was registered as an DOUBLE.
**/
    public void Var014()
    {
      String info = " -- call getLong against DOUBLE -- Changed October 2011";
        try {
            long p = csTypes_.getLong (5);
            long expected  = -987; 
            assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
        }
        catch(Exception e) {
          failed (e, "Unexpected Exception"+info);
        }
    }



/**
getLong() - Get a type that was registered as an DECIMAL.
**/
    public void Var015()
    {
      String info = " -- call getLong against DECIMAL -- Changed October 2011";
        try {
            long p = csTypes_.getLong (7);
            long expected  = -94732; 
            assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
        }
        catch(Exception e) {
          failed (e, "Unexpected Exception"+info);
        }
    }



/**
getLong() - Get a type that was registered as an NUMERIC.
**/
    public void Var016()
    {
      String info = " -- call getLong against NUMERIC -- Changed October 2011";
        try {
            long p = csTypes_.getLong (9);
            long expected  = 19; 
            assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
        }
        catch(Exception e) {
          failed (e, "Unexpected Exception"+info);
        }
    }



/**
getLong() - Get a type that was registered as a CHAR.
**/
    public void Var017()
    {
        try {
            long p = csTypes_.getLong (11);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getLong() - Get a type that was registered as a VARCHAR.
**/
    public void Var018()
    {
        try {
            long p = csTypes_.getLong (12);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getLong() - Get a type that was registered as a BINARY.
**/
    public void Var019()
    {
        try {
            long p = csTypes_.getLong (13);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getLong() - Get a type that was registered as a VARBINARY.
**/
    public void Var020()
    {
        try {
            long p = csTypes_.getLong (14);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getLong() - Get a type that was registered as a DATE.
**/
    public void Var021()
    {
        try {
            long p = csTypes_.getLong (15);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getLong() - Get a type that was registered as a TIME.
**/
    public void Var022()
    {
        try {
            long p = csTypes_.getLong (16);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getLong() - Get a type that was registered as a TIMESTAMP.
**/
    public void Var023()
    {
        try {
            long p = csTypes_.getLong (17);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getLong() - Get a type that was registered as an OTHER.
**/
    public void Var024()
    {
        if(checkLobSupport ()) {
            try {
                long p = csTypes_.getLong (18);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getLong() - Get a type that was registered as a BLOB.
**/
    public void Var025()
    {
        if(checkLobSupport ()) {
            try {
                long p = csTypes_.getLong (19);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getLong() - Get a type that was registered as a CLOB.
**/
    public void Var026()
    {
        if(checkLobSupport ()) {
            try {
                long p = csTypes_.getLong (20);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getLong() - Get a type that was registered as a BIGINT.
**/
    public void Var027()
    {
        if(checkBigintSupport()) {
            try {
                long p = csTypes_.getLong (22);                  
                assertCondition (p == 987662234567l);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getLong() - Get an INOUT parameter, where the OUT parameter is
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
            if (areBigintsSupported()) {
                cs.registerOutParameter (22, Types.BIGINT);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                                     supportedFeatures_);
                cs.execute ();
                long p = cs.getLong (22);
                assertCondition (p == 987662234567l);
            }
            else {
                cs.registerOutParameter (2, Types.INTEGER);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                                     supportedFeatures_);
                cs.execute ();
                long p = cs.getLong (2);
                assertCondition (p == -102);
            }
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
getLong() -- Get an output parameter where the database output type isn't necessarily a short
*/
   public void Var029()
   {
      try {
	  Statement stmt = connection_.createStatement();
	  
	  try {
	      stmt.executeUpdate("drop table "+JDCSTest.COLLECTION+".long_tab");
	  } catch (Exception e) {}
	  try {
	      stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".long_Proc"); 
	  } catch (Exception e) {}

	  stmt.executeUpdate("create table "+JDCSTest.COLLECTION+".long_Tab(MAX_VAL NUMERIC, MIN_VAL NUMERIC, NULL_VAL NUMERIC )"); 

	  stmt.executeUpdate("create procedure "+JDCSTest.COLLECTION+".long_Proc (out MAX_PARAM NUMERIC, out MIN_PARAM NUMERIC, out NULL_PARAM NUMERIC) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from "+JDCSTest.COLLECTION+".long_Tab; end");


	  stmt.executeUpdate("insert into "+JDCSTest.COLLECTION+".long_Tab values(32000,0,null)");
	  CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".long_Proc(?,?,?)}");

		//register the output parameters
	  cstmt.registerOutParameter(1,java.sql.Types.BIGINT);
	  cstmt.registerOutParameter(2,java.sql.Types.BIGINT);
	  cstmt.registerOutParameter(3,java.sql.Types.BIGINT);

		//execute the procedure
	  cstmt.execute();

		//invoke getByte method
	  long xRetVal = cstmt.getLong(1);
	  long yRetVal = cstmt.getLong(2);
	  long zRetVal = cstmt.getLong(3);
	  
	  assertCondition(xRetVal == 32000 && yRetVal == 0 && zRetVal == 0); 

      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }

/**
getLong() -- Get an output parameter where the database output type isn't necessarily a short
*/ 
   public void Var030()
   {
      try {
	  Statement stmt = connection_.createStatement();
	  
	  try {
	      stmt.executeUpdate("drop table "+JDCSTest.COLLECTION+".long_tab");
	  } catch (Exception e) {}
	  try {
	      stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".long_Proc"); 
	  } catch (Exception e) {}

	  stmt.executeUpdate("create table "+JDCSTest.COLLECTION+".long_Tab(MAX_VAL VARCHAR(30), MIN_VAL VARCHAR(30), NULL_VAL VARCHAR(30) )"); 

	  stmt.executeUpdate("create procedure "+JDCSTest.COLLECTION+".long_Proc (out MAX_PARAM VARCHAR(30), out MIN_PARAM VARCHAR(30), out NULL_PARAM VARCHAR(30)) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from "+JDCSTest.COLLECTION+".long_Tab; end");


	  stmt.executeUpdate("insert into "+JDCSTest.COLLECTION+".long_Tab values('32000','0',null)");
	  CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".long_Proc(?,?,?)}");

		//register the output parameters
	  cstmt.registerOutParameter(1,java.sql.Types.BIGINT);
	  cstmt.registerOutParameter(2,java.sql.Types.BIGINT);
	  cstmt.registerOutParameter(3,java.sql.Types.BIGINT);

		//execute the procedure
	  cstmt.execute();

		//invoke getLong method
	  long xRetVal = cstmt.getLong(1);
	  long yRetVal = cstmt.getLong(2);
	  long zRetVal = cstmt.getLong(3);
	  
	  assertCondition(xRetVal == 32000 && yRetVal == 0 && zRetVal == 0); 

      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
   }

/**
getLong() -- Get an output parameter where the database output type isn't necessarily a short, but value cannot be mapped 
*/
   public void Var031()
   {
      try {
	  Statement stmt = connection_.createStatement();
	  
	  try {
	      stmt.executeUpdate("drop table "+JDCSTest.COLLECTION+".long_tab");
	  } catch (Exception e) {}
	  try {
	      stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".long_Proc"); 
	  } catch (Exception e) {}

	  stmt.executeUpdate("create table "+JDCSTest.COLLECTION+".long_Tab(MAX_VAL VARCHAR(30), MIN_VAL VARCHAR(30), NULL_VAL VARCHAR(30) )"); 

	  stmt.executeUpdate("create procedure "+JDCSTest.COLLECTION+".long_Proc (out MAX_PARAM VARCHAR(30), out MIN_PARAM VARCHAR(30), out NULL_PARAM VARCHAR(30)) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from "+JDCSTest.COLLECTION+".long_Tab; end");


	  stmt.executeUpdate("insert into "+JDCSTest.COLLECTION+".long_Tab values('ISMALL','0',null)");
	  CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".long_Proc(?,?,?)}");

		//register the output parameters
	  cstmt.registerOutParameter(1,java.sql.Types.BIGINT);
	  cstmt.registerOutParameter(2,java.sql.Types.BIGINT);
	  cstmt.registerOutParameter(3,java.sql.Types.BIGINT);

		//execute the procedure
	  cstmt.execute();

		//invoke getLong method
	  long xRetVal = cstmt.getLong(1);
	  long yRetVal = cstmt.getLong(2);
	  long zRetVal = cstmt.getLong(3);


	  failed("Didn't throw exception "+xRetVal+","+yRetVal+","+zRetVal); 

      } catch (Exception e) {
          //
          // For the native driver, this is a data type mismatch
          // 
	  assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }

   }
// -- NAMED PARAMETER
/**
getLong() - GeT A NAMED PARAMETER THAT DOESN'T EXIST
**/
    public void Var032()
    {
	if(checkNamedParametersSupport()) {
	    try {
		long p = csTypes_.getLong ("P_FAKE");
		failed ("Didn't throw SQLException "+p);
	    }
	    catch(Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }
/**
getLong() - Get a type that was registered as a SMALLINT.
**/
    public void Var033()
    {
	if(checkNamedParametersSupport()){	
	      String info = " -- call getLong against SMALLINT -- Changed October 2011";
	    try {
		long p = csTypes_.getLong ("P_SMALLINT");
	            long expected  = 123; 
	            assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
	    }
	    catch(Exception e) {
	          failed (e, "Unexpected Exception"+info);
	    }
	}
    }

/**
getLong() - Get a type that was registered as an INTEGER.
**/
    public void Var034()
    {
	if(checkNamedParametersSupport()) {
	    if (areBigintsSupported()) {
	      String info = " -- call getLong against INTEGER -- Changed October 2011";
		try {
		    long p = csTypes_.getLong ("P_INTEGER");                  
	            long expected  = -456; 
	            assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
		}
		catch(Exception e) {
	          failed (e, "Unexpected Exception"+info);
		}
	    }
	    else {
		try {
		    long p = csTypes_.getLong (2);                  
		    assertCondition (p == -456);
		}
		catch(Exception e) {
		    failed (e, "Unexpected Exception");
		}
	    }
	}
    }

/**
getLong() - Get a type that was registered as an REAL.
**/
    public void Var035()
    {
	if(checkNamedParametersSupport()) {	
	      String info = " -- call getLong against REAL -- Changed October 2011";
	    try {
		long p = csTypes_.getLong ("P_REAL");
	            long expected  = 789; 
	            assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
	    }
	    catch(Exception e) {
	          failed (e, "Unexpected Exception"+info);
	    }
	}

    }
/**
getLong() - Get a type that was registered as an FLOAT.
**/
    public void Var036()
    {
	if(checkNamedParametersSupport()) {
	      String info = " -- call getLong against FLOAT -- Changed October 2011";
	    try {
		long p = csTypes_.getLong ("P_FLOAT");
	            long expected  = 253; 
	            assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
	    }
	    catch(Exception e) {
	          failed (e, "Unexpected Exception"+info);
	    }
	}
    }

/**
getLong() - Get a type that was registered as an DOUBLE.
**/
    public void Var037()
    {
	if(checkNamedParametersSupport()) {	
	      String info = " -- call getLong against DOUBLE -- Changed October 2011";
	    try {
		long p = csTypes_.getLong ("P_DOUBLE");
	            long expected  = -987; 
	            assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
	    }
	    catch(Exception e) {
	          failed (e, "Unexpected Exception"+info);
	    }
	}
    }

/**
getLong() - Get a type that was registered as an DECIMAL.
**/
    public void Var038()
    {
	if(checkNamedParametersSupport()) {	
	      String info = " -- call getLong against DECIMAL -- Changed October 2011";
	    try {
		long p = csTypes_.getLong ("P_DECIMAL_50");
	            long expected  = 54362; 
	            assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
	    }
	    catch(Exception e) {
	          failed (e, "Unexpected Exception"+info);
	    }
	}
    }

/**
getLong() - Get a type that was registered as an NUMERIC.
**/
    public void Var039()
    {
	if(checkNamedParametersSupport()) {	
	      String info = " -- call getLong against NUMERIC -- Changed October 2011";
	    try {
		long p = csTypes_.getLong ("P_NUMERIC_50");
	            long expected  = -1112; 
	            assertCondition(p == expected, "Got "+p+" expected "+expected+info); 
	    }	
	    catch(Exception e) {
	          failed (e, "Unexpected Exception"+info);
	    }
	}
    }

/**
getLong() - Get a type that was registered as a CHAR.
**/
    public void Var040()
    {
	if(checkNamedParametersSupport()) {	
	    try {
		long p = csTypes_.getLong ("P_CHAR_1");
		failed ("Didn't throw SQLException "+p);
	    }	
	    catch(Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }

/**
getLong() - Get a type that was registered as a VARCHAR.
**/
    public void Var041()
    {
	if(checkNamedParametersSupport()) {
	    try {
		long p = csTypes_.getLong ("P_VARCHAR_50");
		failed ("Didn't throw SQLException "+p);
	    }
	    catch(Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }

/**
getLong() - Get a type that was registered as a BINARY.
**/
    public void Var042()
    {
	if(checkNamedParametersSupport()) {
	    try {
		long p = csTypes_.getLong ("P_BINARY_20");
		failed ("Didn't throw SQLException "+p);
	    }
	    catch(Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }

/**
getLong() - Get a type that was registered as a VARBINARY.
**/
    public void Var043()
    {
	if(checkNamedParametersSupport()) {
	    try {
		long p = csTypes_.getLong ("P_VARBINARY_20");
		failed ("Didn't throw SQLException "+p);
	    }
	    catch(Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }

/**
getLong() - Get a type that was registered as a DATE.
**/
    public void Var044()
    {
	if(checkNamedParametersSupport()) {
	    try {
		long p = csTypes_.getLong ("P_DATE");
		failed ("Didn't throw SQLException "+p);
	    }
	    catch(Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }

/**
getLong() - Get a type that was registered as a TIME.
**/
    public void Var045()
    {
	if(checkNamedParametersSupport()) {
	    try {
		long p = csTypes_.getLong ("P_TIME");
		failed ("Didn't throw SQLException "+p);
	    }
	    catch(Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }

/**
getLong() - Get a type that was registered as a TIMESTAMP.
**/
    public void Var046()
    {
	if(checkNamedParametersSupport()) {
	    try {
		long p = csTypes_.getLong ("P_TIMESTAMP");
		failed ("Didn't throw SQLException "+p);
	    }
	    catch(Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }

/**
getLong() - Get a type that was registered as an OTHER.
**/
    public void Var047()
    {
	if(checkNamedParametersSupport()) {
	    if(checkLobSupport ()) {
		try {
		    long p = csTypes_.getLong ("P_DATALINK");
		    failed ("Didn't throw SQLException "+p);
		}
		catch(Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
    }

/**
getLong() - Get a type that was registered as a BLOB.
**/
    public void Var048()
    {
	if(checkNamedParametersSupport()) {
	    if(checkLobSupport ()) {
		try {
		    long p = csTypes_.getLong ("P_BLOB");
		    failed ("Didn't throw SQLException "+p);
		}
		catch(Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
    }

/**
getLong() - Get a type that was registered as a CLOB.
**/
    public void Var049()
    {
	if(checkNamedParametersSupport()) {
	    if(checkLobSupport ()) {
		try {
		    long p = csTypes_.getLong ("P_CLOB");
		    failed ("Didn't throw SQLException "+p);
		}
		catch(Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
    }

/**
getLong() - Get a type that was registered as a BIGINT.
**/
    public void Var050()
    {
	if(checkNamedParametersSupport()) {
	    if(checkBigintSupport()) {
		try {
		    long p = csTypes_.getLong ("P_BIGINT");                  
		    assertCondition (p == 987662234567l);
		}
		catch(Exception e) {
		    failed (e, "Unexpected Exception");
		}
	    }
	}
    }
    
    
    
    
    /**
     * getLong () - Get a type that was registered as a BOOLEAN.
     **/
    public void Var051() {
      if (checkBooleanSupport()) {
        String info = " -- Test added 2021/01/06 for boolean";
        getTestSuccessful(csTypes_, "getLong", 25, "1",info);
      }
    }


    /**
     * getLong() - Get a type that was registered as a BOOLEAN that returns
     * false.
     **/
    public void Var052() {
      if (checkBooleanSupport()) { 
        String info = " -- Test added 2021/01/06 for boolean";
        getTestSuccessful(csTypesB_, "getLong", 25, "0", info);
      }
    }
    
    /**
     * getLong() - Get a type that was registered as a BOOLEAN that returns
     * null.
     **/
    public void Var053() {
      if (checkBooleanSupport()) {
        String info = " -- Test added 2021/01/06 for boolean";
             getTestSuccessful(csTypesNull_, "getLong", 25, "0",info);
      }
    }

    
}
