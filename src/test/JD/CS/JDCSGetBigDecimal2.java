///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetBigDecimal2.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetBigDecimal.java
//
// Classes:      JDCSGetBigDecimal
//
////////////////////////////////////////////////////////////////////////
package test.JD.CS;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JDTestcase;



/**
Testcase JDCSGetBigDecimal2.  This tests the following
method of the JDBC CallableStatement class:

<ul>
<li>getBigDecimal() with 2 args
</ul>
**/
@SuppressWarnings("deprecation")
public class JDCSGetBigDecimal2
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSGetBigDecimal2";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCSTest.main(newArgs); 
   }
    // table names
    private static String INTEGER_TABLE = JDCSTest.COLLECTION + ".int_tab";
    private static String INTEGER_PROCEDURE = JDCSTest.COLLECTION + ".int_proc";
    private static String VARCHAR_TABLE = JDCSTest.COLLECTION + ".vc_tab";
    private static String VARCHAR_PROCEDURE = JDCSTest.COLLECTION + ".vc_proc";
    private static String NUMERIC_TABLE = JDCSTest.COLLECTION + ".num_tab";
    private static String NUMERIC_PROCEDURE = JDCSTest.COLLECTION + ".num_proc";


    // Private data.
    private CallableStatement   csTypes_;
    private CallableStatement   csTypesB_;



    /**
    Constructor.
    **/
    public JDCSGetBigDecimal2 (AS400 systemObject,
                               Hashtable<String,Vector<String>> namesAndVars,
                               int runMode,
                               FileOutputStream fileOutputStream,
                               
                               String password)
    {
        super (systemObject, "JDCSGetBigDecimal2",
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

	//
	// Reset names to use collection names set by -lib parameter 
	// 
	INTEGER_TABLE = JDCSTest.COLLECTION + ".int_tab";
	INTEGER_PROCEDURE = JDCSTest.COLLECTION + ".int_proc";
	VARCHAR_TABLE = JDCSTest.COLLECTION + ".vc_tab";
	VARCHAR_PROCEDURE = JDCSTest.COLLECTION + ".vc_proc";
	NUMERIC_TABLE = JDCSTest.COLLECTION + ".num_tab";
	NUMERIC_PROCEDURE = JDCSTest.COLLECTION + ".num_proc";



        csTypes_ = JDSetupProcedure.prepare (connection_,
                                             JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
        JDSetupProcedure.register (csTypes_, JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_, getDriver());
        csTypes_.execute ();


        csTypesB_ = JDSetupProcedure.prepare (connection_,
                                             JDSetupProcedure.STP_CSTYPESOUTB, supportedFeatures_);
        JDSetupProcedure.register (csTypesB_, JDSetupProcedure.STP_CSTYPESOUTB, supportedFeatures_, getDriver());
        csTypesB_.execute ();


        Statement stmt = connection_.createStatement();

        stmt.executeUpdate("CREATE TABLE " + INTEGER_TABLE + "(MAX_VAL INTEGER, MIN_VAL INTEGER, NULL_VAL INTEGER )"); 
        stmt.executeUpdate("CREATE PROCEDURE " + INTEGER_PROCEDURE + "(out MAX_PARAM INTEGER, out MIN_PARAM INTEGER, out NULL_PARAM INTEGER) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from " + INTEGER_TABLE + "; end");

        stmt.executeUpdate("CREATE TABLE " + VARCHAR_TABLE + "(MAX_VAL VARCHAR(30), MIN_VAL VARCHAR(30), NULL_VAL VARCHAR(30) )"); 
        stmt.executeUpdate("CREATE PROCEDURE " + VARCHAR_PROCEDURE + "(out MAX_PARAM VARCHAR(30), out MIN_PARAM VARCHAR(30), out NULL_PARAM VARCHAR(30)) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from " + VARCHAR_TABLE + "; end");

        stmt.executeUpdate("CREATE TABLE " + NUMERIC_TABLE + "(XSMALLINT SMALLINT, XINTEGER INTEGER, XBIGINT BIGINT, XREAL REAL, XFLOAT FLOAT, XDOUBLE DOUBLE )"); 
        stmt.executeUpdate("CREATE PROCEDURE " + NUMERIC_PROCEDURE + "(out XSMALLINT SMALLINT, out XINTEGER INTEGER, out XBIGINT BIGINT, out XREAL REAL, out XFLOAT FLOAT, out XDOUBLE DOUBLE) LANGUAGE SQL begin select XSMALLINT, XINTEGER, XBIGINT, XREAL, XFLOAT, XDOUBLE into  XSMALLINT, XINTEGER, XBIGINT, XREAL, XFLOAT, XDOUBLE from " + NUMERIC_TABLE +"; end");

        stmt.close();
    }



    /**
    Performs cleanup needed after running variations.
    
    @exception Exception If an exception occurs.
    **/
    protected void cleanup ()
    throws Exception
    {
        Statement stmt = connection_.createStatement();
        try
        {
            stmt.execute("DROP TABLE " + INTEGER_TABLE);
        }
        catch(SQLException e)
        {
        }

        try
        {
            stmt.execute("DROP TABLE " + VARCHAR_TABLE);
        }
        catch(SQLException e)
        {
        }

        try
        {
            stmt.execute("DROP TABLE " + NUMERIC_TABLE);
        }
        catch(SQLException e)
        {
        }

        try
        {
            stmt.execute("DROP PROCEDURE " + INTEGER_PROCEDURE); 
        }
        catch(SQLException e)
        {
        }

        try
        {
            stmt.execute("DROP PROCEDURE " + VARCHAR_PROCEDURE); 
        }
        catch(SQLException e)
        {
        }

        try
        {
            stmt.execute("DROP PROCEDURE " + NUMERIC_PROCEDURE); 
        }
        catch(SQLException e)
        {
        }

        stmt.close();

        connection_.close ();
        connection_ = null; 

    }



    /**
    getBigDecimal() with 2 args - Get parameter -1.
    **/
    public void Var001()
    {
        try
        {
            csTypes_.execute ();
            BigDecimal p = csTypes_.getBigDecimal (-1, 0);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getBigDecimal() with 2 args - Get parameter 0.
    **/
    public void Var002()
    {
        try
        {
            BigDecimal p = csTypes_.getBigDecimal (0, 0);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getBigDecimal() with 2 args - Use a parameter that is too big.
    **/
    public void Var003()
    {
        try
        {
            BigDecimal p = csTypes_.getBigDecimal (35, 0);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getBigDecimal() with 2 args - Get a parameter when there are no parameters.
    **/
    public void Var004()
    {
        // I created a whole new Connection object here to work
        // around a server bug.
        Connection c = null;
        try
        {
            c = testDriver_.getConnection (baseURL_
                                           + ";errors=full", userId_, encryptedPassword_);
            CallableStatement cs = c.prepareCall ("CALL "
                                                  + JDSetupProcedure.STP_CS0);
            cs.execute ();
            BigDecimal p = cs.getBigDecimal (1, 0);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        finally
        {
            try
            {
                c.close ();
            }
            catch(SQLException e)
            {
                // Ignore.
            }
        }
    }



    /**
    getBigDecimal() with 2 args - Get a parameter that was not registered.
    **/
    public void Var005()
    {
        try
        {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
            cs.execute ();
            BigDecimal p = cs.getBigDecimal (7, 0);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getBigDecimal() with 2 args - Get a parameter when the statement has not been
    executed.
    **/
    public void Var006()
    {
        try
        {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
            cs.registerOutParameter (7, Types.DECIMAL);
            BigDecimal p = cs.getBigDecimal (7, 0);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getBigDecimal() with 2 args - Get a parameter when the statement is closed.
    **/
    public void Var007()
    {
        try
        {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
            cs.registerOutParameter (7, Types.DECIMAL);
            cs.execute ();
            cs.close ();
            BigDecimal p = cs.getBigDecimal (7, 0);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getBigDecimal() with 2 args - Get an IN parameter that was correctly registered.
    **/
    public void Var008()
    {
        try
        {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESIN, supportedFeatures_);
            JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESIN,
                                                 supportedFeatures_);
            cs.registerOutParameter (7, Types.DECIMAL);
            cs.execute ();
            BigDecimal p = cs.getBigDecimal (7, 0);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }


    /**
    getBigDecimal() with 2 args - Get an INOUT parameter, where the OUT parameter is
    longer than the IN parameter.
    **/
    public void Var009()
    {
        try
        {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_);
            JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                       supportedFeatures_,
				       getDriver());
            JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                                 supportedFeatures_);
            cs.registerOutParameter (7, Types.DECIMAL);
            cs.execute ();
            BigDecimal p = cs.getBigDecimal (7, 5);
            assertCondition (p.toString ().equals ("30777.77703"));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getBigDecimal() with 2 args - Get with an invalid sclae.
    **/
    public void Var010()
    {
        try
        {
            BigDecimal p = csTypes_.getBigDecimal (7, -1);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getBigDecimal() with 2 args - Get a type that was registered as a SMALLINT.
    **/
    public void Var011()
    {


	String info = " -- call getBigDecimal against registered SMALLINT -- Changed October 2011";

	// As of 10/3/2011 for the toolbox driver, it is now possible to get a big decimal
	// from a parameter registered as int.
	// Note:  JCC does not throw exception either. 
	// Native JDBC will now need to change. 

        try
        {
	    BigDecimal p = csTypes_.getBigDecimal (1, 0);
	    String expected = "123";
	    assertCondition (p.toString ().equals (expected), "Got "+p+" sb "+expected+info);
	}
	catch(Exception e)
	{
	    failed (e, "Unexpected Exception"+info);
	}

    }



    /**
    getBigDecimal() with 2 args - Get a type that was registered as an INTEGER.
    **/
    public void Var012()
    {
	if(checkJdbc20 ())
	{

		String info = " -- call getBigDecimal against registered INTEGER -- Changed October 2011";



		try
		{
		    BigDecimal p = csTypes_.getBigDecimal (2,0);
		    String expected = "-456";
		    assertCondition (p.toString ().equals (expected), "Got "+p+" sb "+expected+info);
		}
		catch(Exception e)
		{
		    failed (e, "Unexpected Exception"+info);
		}

	}
    }


    /**
    getBigDecimal() with 2 args - Get a type that was registered as an REAL.
    **/
    public void Var013()
    {
{
	    String info = " -- call getBigDecimal against registered REAL -- Changed October 2011";

	    try
	    {
		BigDecimal p = csTypes_.getBigDecimal (3,0);
		String expected = "790";
		if (getDriver() == JDTestDriver.DRIVER_JCC) {
		/* Jcc truncates instead of rounds */ 
		    expected = "789"; 
		} 

		assertCondition (p.toString ().equals (expected), "Got "+p+" sb "+expected+info);
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception"+info);
	    }

	} 

    }



    /**
    getBigDecimal() with 2 args - Get a type that was registered as an FLOAT.
    **/
    public void Var014()
    {
{
	    String info = " -- call getBigDecimal against registered FLOAT -- Changed October 2011";

	    try
	    {
		BigDecimal p = csTypes_.getBigDecimal (4,0);
		String expected = "253";
		assertCondition (p.toString ().equals (expected), "Got "+p+" sb "+expected+info);
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception"+info);
	    }

	} 

    }




    /**
    getBigDecimal() with 2 args - Get a type that was registered as an DOUBLE.
    **/
    public void Var015()
    {
	{
	    String info = " -- call getBigDecimal against registered DOUBLE -- Changed October 2011";

	    try
	    {
		BigDecimal p = csTypes_.getBigDecimal (5, 0);
		String expected = "-987";
		assertCondition (p.toString ().equals (expected), "Got "+p+" sb "+expected+info);
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception"+info);
	    }

	} 

    }



    /**
    getBigDecimal() with 2 args - Get a type that was registered as an DECIMAL, when it is a DECIMAL(5,0)
    with a zero scale.
    **/
    public void Var016()
    {
        try
        {
            BigDecimal p = csTypes_.getBigDecimal (6, 0);
            assertCondition (p.toString ().equals ("54362"));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getBigDecimal() with 2 args - Get a type that was registered as an DECIMAL, when it is a DECIMAL(5,0)
    with a nonzero scale.
    **/
    public void Var017()
    {
        try
        {
            BigDecimal p = csTypes_.getBigDecimal (6, 1);
            assertCondition (p.toString ().equals ("54362.0"));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getBigDecimal() with 2 args - Get a type that was registered as an DECIMAL, when it is a DECIMAL(10,5)
    with a zero scale.
    **/
    public void Var018()
    {
        try
        {
            BigDecimal p = csTypes_.getBigDecimal (7, 0);
            assertCondition (p.toString ().equals ("-94732"));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getBigDecimal() with 2 args - Get a type that was registered as an DECIMAL, when it is a DECIMAL(10,5)
    with a nonzero scale less than the actual scale.
    **/
    public void Var019()
    {
        try
        {
            BigDecimal p = csTypes_.getBigDecimal (7, 1);
            assertCondition (p.toString ().equals ("-94732.1"));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getBigDecimal() with 2 args - Get a type that was registered as an DECIMAL, when it is a DECIMAL(10,5)
    with a nonzero scale equal to the actual scale.
    **/
    public void Var020()
    {
        try
        {
            BigDecimal p = csTypes_.getBigDecimal (7, 5);
            assertCondition (p.toString ().equals ("-94732.12345"));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getBigDecimal() with 2 args - Get a type that was registered as an DECIMAL, when it is a DECIMAL(10,5)
    with a nonzero scale greater than the actual scale.
    **/
    public void Var021()
    {
        try
        {
            BigDecimal p = csTypes_.getBigDecimal (7, 6);
            assertCondition (p.toString ().equals ("-94732.123450"));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getBigDecimal() with 2 args - Get a type that was registered as an NUMERIC, when it is a NUMERIC(5,0)
    with a zero scale.
    **/
    public void Var022()
    {
        try
        {
            BigDecimal p = csTypes_.getBigDecimal (8, 0);
            assertCondition (p.toString ().equals ("-1112"));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getBigDecimal() with 2 args - Get a type that was registered as an NUMERIC, when it is a NUMERIC(5,0)
    with a nonzero scale.
    **/
    public void Var023()
    {
        try
        {
            BigDecimal p = csTypes_.getBigDecimal (8, 2);
            assertCondition (p.toString ().equals ("-1112.00"));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getBigDecimal() with 2 args - Get a type that was registered as an NUMERIC, when it is a NUMERIC(10,5)
    with a zero scale.
    **/
    public void Var024()
    {
        try
        {
            BigDecimal p = csTypes_.getBigDecimal (9, 0);
	    String expected = "20";
	    if (getDriver() == JDTestDriver.DRIVER_JCC) {
		/* Jcc truncates instead of rounds */ 
		expected = "19"; 
	    } 
            assertCondition (p.toString ().equals (expected), "got "+p+" expected "+expected);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getBigDecimal() with 2 args - Get a type that was registered as an NUMERIC, when it is a NUMERIC(10,5)
    with a nonzero scale less than the actual scale.
    **/
    public void Var025()
    {
        try
        {
            BigDecimal p = csTypes_.getBigDecimal (9, 2);
	    String expected = "19.99"; 
	    if (getDriver() == JDTestDriver.DRIVER_JCC) {
		/* JCC truncated instead of rounds */
		expected = "19.98"; 
	    }
            assertCondition (p.toString ().equals (expected), "got "+p+" expected "+expected);
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getBigDecimal() with 2 args - Get a type that was registered as an NUMERIC, when it is a NUMERIC(10,5)
    with a nonzero scale equals to the actual scale.
    **/
    public void Var026()
    {
        try
        {
            BigDecimal p = csTypes_.getBigDecimal (9, 5);
            assertCondition (p.toString ().equals ("19.98765"));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getBigDecimal() with 2 args - Get a type that was registered as an NUMERIC, when it is a NUMERIC(10,5)
    with a nonzero scale greater than the actual scale.
    **/
    public void Var027()
    {
        try
        {
            BigDecimal p = csTypes_.getBigDecimal (9, 9);
            assertCondition (p.toString ().equals ("19.987650000"));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getBigDecimal() with 2 args - Get a type that was registered as a CHAR(1).
    **/
    public void Var028()
    {
        try
        {
            BigDecimal p = csTypes_.getBigDecimal (10, 0);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getBigDecimal() with 2 args - Get a type that was registered as a CHAR(50).
    **/
    public void Var029()
    {
        try
        {
            BigDecimal p = csTypes_.getBigDecimal (11, 0);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getBigDecimal() with 2 args - Get a type that was registered as a VARCHAR(50).
    **/
    public void Var030()
    {
        try
        {
            BigDecimal p = csTypes_.getBigDecimal (12, 0);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getBigDecimal() with 2 args - Get a type that was registered as a BINARY.
    **/
    public void Var031()
    {
        try
        {
            BigDecimal p = csTypes_.getBigDecimal (13, 0);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getBigDecimal() with 2 args - Get a type that was registered as a VARBINARY.
    **/
    public void Var032()
    {
        try
        {
            BigDecimal p = csTypes_.getBigDecimal (14, 0);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getBigDecimal() with 2 args - Get a type that was registered as a DATE.
    **/
    public void Var033()
    {
        try
        {
            BigDecimal p = csTypes_.getBigDecimal (15, 0);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getBigDecimal() with 2 args - Get a type that was registered as a TIME.
    **/
    public void Var034()
    {
        try
        {
            BigDecimal p = csTypes_.getBigDecimal (16, 0);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getBigDecimal() with 2 args - Get a type that was registered as a TIMESTAMP.
    **/
    public void Var035()
    {
        try
        {
            BigDecimal p = csTypes_.getBigDecimal (17, 0);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getBigDecimal() with 2 args - Get a type that was registered as an OTHER.
    **/
    public void Var036()
    {
        if(checkLobSupport ())
        {
            try
            {
                BigDecimal p = csTypes_.getBigDecimal (18, 0);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    getBigDecimal() with 2 args - Get a type that was registered as a BLOB.
    **/
    public void Var037()
    {
        if(checkLobSupport ())
        {
            try
            {
                BigDecimal p = csTypes_.getBigDecimal (19, 0);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    getBigDecimal() with 2 args - Get a type that was registered as a CLOB.
    **/
    public void Var038()
    {
        if(checkLobSupport ())
        {
            try
            {
                BigDecimal p = csTypes_.getBigDecimal (20, 0);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    getBigDecimal() with 2 args - Get a type that was registered as a BIGINT.
    **/
    public void Var039()
    {

	if(checkBigintSupport()) {
		    String info = " -- call getBigDecimal against registered BIGINT -- Changed October 2011";
		    getTestSuccessful(csTypes_,22,0,"987662234567",info);
        }
    }



    /**
    getBigDecimal() with 2 args - Get an INOUT parameter, where the OUT parameter is
    longer than the IN parameter, when the output parameter is registered first.
    
    SQL400 - We added this testcase because of a customer bug.
    **/
    public void Var040()
    {
        try
        {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESINOUT,supportedFeatures_);
            JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                       supportedFeatures_, getDriver());
            cs.registerOutParameter (7, Types.DECIMAL);
            JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                                 supportedFeatures_);
            cs.execute ();
            BigDecimal p = cs.getBigDecimal (7, 5);
            assertCondition (p.toString ().equals ("30777.77703"));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    public void getTestSuccessful(CallableStatement cstmt, int column, int scale,
				  String expectedValue, String info) {
	try {
	    BigDecimal p = cstmt.getBigDecimal(column, scale);

	    assertCondition(p.toString().equals(expectedValue),
			    "Got " + p + " sb " + expectedValue + info);
	} catch (Exception e) {
	    failed(e, "Unexpected Exception");
	}
    }

    /**
    getBigDecimal() - Get a type that was registered as a BIGINT.
    **/
    public void Var041()
    {
	if(checkJdbc20 ())
	{
	    if(checkBigintSupport())
	    {
		String info = " -- call getBigDecimal against registered BIGINT -- Changed October 2011";
		getTestSuccessful(csTypes_,22,0,"987662234567",info);

	    }
	}
    }



    /**
    getBigDecimal() -- Get an output parameter where the database output type isn't necessarily a numeric
    */
    public void Var042()
    {
        try
        {
            Statement statement = connection_.createStatement();
            statement.executeUpdate("DELETE FROM " + INTEGER_TABLE);
            statement.executeUpdate("INSERT INTO " + INTEGER_TABLE + " VALUES(32000, 0, null)");
            statement.close();

            CallableStatement cstmt = connection_.prepareCall("{CALL " + INTEGER_PROCEDURE + "(?,?,?)}");

            //register the output parameters
            cstmt.registerOutParameter(1,java.sql.Types.DECIMAL);
            cstmt.registerOutParameter(2,java.sql.Types.DECIMAL);
            cstmt.registerOutParameter(3,java.sql.Types.DECIMAL);

            //execute the procedure
            cstmt.execute();

            //invoke getByte method
            BigDecimal xRetVal = cstmt.getBigDecimal(1,0);
            BigDecimal yRetVal = cstmt.getBigDecimal(2,0);
            BigDecimal zRetVal = cstmt.getBigDecimal(3,0);
            cstmt.close();

            boolean condition =  xRetVal.intValue() == 32000 && yRetVal.intValue() == 0 && zRetVal == null; 
            if(!condition)
            {
                output_.println("xRetVal ("+xRetVal+") != 32000"); 
                output_.println("yRetVal ("+yRetVal+") != 0");
                output_.println("zRetVal ("+zRetVal+") != null"); 
            }

            assertCondition(condition); 

        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    getBigDecimal() -- Get an output parameter where the database output type isn't necessarily a numerc/ etc 
    */ 
    public void Var043()
    {
        try
        {
            Statement statement = connection_.createStatement();
            statement.executeUpdate("DELETE FROM " + VARCHAR_TABLE);
            statement.executeUpdate("INSERT INTO " + VARCHAR_TABLE + " VALUES('32000', '0', null)");
            statement.close();

            CallableStatement cstmt = connection_.prepareCall("{CALL " + VARCHAR_PROCEDURE + "(?,?,?)}");

            //register the output parameters
            cstmt.registerOutParameter(1,java.sql.Types.DECIMAL);
            cstmt.registerOutParameter(2,java.sql.Types.DECIMAL);
            cstmt.registerOutParameter(3,java.sql.Types.DECIMAL);

            //execute the procedure
            cstmt.execute();

            //invoke getBigDecimal method
            BigDecimal xRetVal = cstmt.getBigDecimal(1,0);
            BigDecimal yRetVal = cstmt.getBigDecimal(2,0);
            BigDecimal zRetVal = cstmt.getBigDecimal(3,0);
            cstmt.close();

            assertCondition(xRetVal.intValue() == 32000 && yRetVal.intValue() == 0 && zRetVal == null); 

        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    getBigDecimal() -- Get an output parameter where the database output type isn't necessarily a short, but value cannot be mapped 
    */
    public void Var044()
    {
        try
        {
            Statement statement = connection_.createStatement();
            statement.executeUpdate("DELETE FROM " + VARCHAR_TABLE);
            statement.executeUpdate("INSERT INTO " + VARCHAR_TABLE + " VALUES('ISMALL', '0', null)");
            statement.close();
            
            CallableStatement cstmt = connection_.prepareCall("{CALL " + VARCHAR_PROCEDURE + "(?,?,?)}");

            //register the output parameters
            cstmt.registerOutParameter(1,java.sql.Types.DECIMAL);
            cstmt.registerOutParameter(2,java.sql.Types.DECIMAL);
            cstmt.registerOutParameter(3,java.sql.Types.DECIMAL);

            //execute the procedure
            cstmt.execute();

            //invoke getBigDecimal method
            BigDecimal xRetVal = cstmt.getBigDecimal(1,0);
            BigDecimal yRetVal = cstmt.getBigDecimal(2,0);
            BigDecimal zRetVal = cstmt.getBigDecimal(3,0);
            cstmt.close();

            failed("Didn't throw exception "+xRetVal+","+yRetVal+","+zRetVal); 

        }
        catch(Exception e)
        {
            //
            // For the native driver, this is a data type mismatch
            // 
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }

    }



    /**
    getBigDecimal() -- Get an output parameter where the database output type isn't necessarily a numeric
    */
    public void Var045()
    {
        try
        {
            Statement statement = connection_.createStatement();
            statement.executeUpdate("DELETE FROM " + INTEGER_TABLE);
            statement.executeUpdate("INSERT INTO " + INTEGER_TABLE + " VALUES(32000, 0, null)");
            statement.close();

            CallableStatement cstmt = connection_.prepareCall("{CALL " + INTEGER_PROCEDURE + "(?,?,?)}");

            //register the output parameters
            cstmt.registerOutParameter(1,java.sql.Types.NUMERIC);
            cstmt.registerOutParameter(2,java.sql.Types.NUMERIC);
            cstmt.registerOutParameter(3,java.sql.Types.NUMERIC);

            //execute the procedure
            cstmt.execute();

            //invoke getByte method
            BigDecimal xRetVal = cstmt.getBigDecimal(1,0);
            BigDecimal yRetVal = cstmt.getBigDecimal(2,0);
            BigDecimal zRetVal = cstmt.getBigDecimal(3,0);
            cstmt.close();

            assertCondition(xRetVal.intValue() == 32000 && yRetVal.intValue() == 0 && zRetVal == null); 

        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    getBigDecimal() -- Get an output parameter where the database output type isn't necessarily a numerc/ etc 
    */ 
    public void Var046()
    {
        try
        {
            Statement statement = connection_.createStatement();
            statement.executeUpdate("DELETE FROM " + VARCHAR_TABLE);
            statement.executeUpdate("INSERT INTO " + VARCHAR_TABLE + " VALUES('32000', '0', null)");
            statement.close();

            CallableStatement cstmt = connection_.prepareCall("{CALL " + VARCHAR_PROCEDURE + "(?,?,?)}");

            //register the output parameters
            cstmt.registerOutParameter(1,java.sql.Types.NUMERIC);
            cstmt.registerOutParameter(2,java.sql.Types.NUMERIC);
            cstmt.registerOutParameter(3,java.sql.Types.NUMERIC);

            //execute the procedure
            cstmt.execute();

            //invoke getBigDecimal method
            BigDecimal xRetVal = cstmt.getBigDecimal(1,0);
            BigDecimal yRetVal = cstmt.getBigDecimal(2,0);
            BigDecimal zRetVal = cstmt.getBigDecimal(3,0);
            cstmt.close();

            assertCondition(xRetVal.intValue() == 32000 && yRetVal.intValue() == 0 && zRetVal == null); 

        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    getBigDecimal() -- Get an output parameter where the database output type isn't necessarily a short, but value cannot be mapped 
    */
    public void Var047()
    {
        try
        {
            Statement statement = connection_.createStatement();
            statement.executeUpdate("DELETE FROM " + VARCHAR_TABLE);
            statement.executeUpdate("INSERT INTO " + VARCHAR_TABLE + " VALUES('ISMALL', '0', null)");
            statement.close();
            
            CallableStatement cstmt = connection_.prepareCall("{CALL " + VARCHAR_PROCEDURE + "(?,?,?)}");

            //register the output parameters
            cstmt.registerOutParameter(1,java.sql.Types.NUMERIC);
            cstmt.registerOutParameter(2,java.sql.Types.NUMERIC);
            cstmt.registerOutParameter(3,java.sql.Types.NUMERIC);

            //execute the procedure
            cstmt.execute();

            //invoke getBigDecimal method
            BigDecimal xRetVal = cstmt.getBigDecimal(1,0);
            BigDecimal yRetVal = cstmt.getBigDecimal(2,0);
            BigDecimal zRetVal = cstmt.getBigDecimal(3,0);
            cstmt.close();

            failed("Didn't throw exception "+xRetVal +","+yRetVal+","+zRetVal); 

        }
        catch(Exception e)
        {
            //
            // For the native driver, this is a data type mismatch
            // 
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }

    }


    /**
    getBigDecimal() -- Make sure null can be retrieved from typical non-null types
    */
    public void Var048()
    {
        try
        {
            Statement statement = connection_.createStatement();
            statement.executeUpdate("DELETE FROM " + NUMERIC_TABLE);
            statement.executeUpdate("INSERT INTO " + NUMERIC_TABLE + " VALUES(null, null, null, null, null, null)");
            statement.close();

            CallableStatement cstmt = connection_.prepareCall("{CALL " + NUMERIC_PROCEDURE + "(?,?,?,?,?,?)}");

            //register the output parameters
            cstmt.registerOutParameter(1,java.sql.Types.NUMERIC);
            cstmt.registerOutParameter(2,java.sql.Types.NUMERIC);
            cstmt.registerOutParameter(3,java.sql.Types.NUMERIC);
            cstmt.registerOutParameter(4,java.sql.Types.NUMERIC);
            cstmt.registerOutParameter(5,java.sql.Types.NUMERIC);
            cstmt.registerOutParameter(6,java.sql.Types.NUMERIC);

            //execute the procedure
            cstmt.execute();

            //invoke getBigDecimal method
            BigDecimal aRetVal = cstmt.getBigDecimal(1,0);
            BigDecimal bRetVal = cstmt.getBigDecimal(2,0);
            BigDecimal cRetVal = cstmt.getBigDecimal(3,0);
            BigDecimal dRetVal = cstmt.getBigDecimal(4,0);
            BigDecimal eRetVal = cstmt.getBigDecimal(5,0);
            BigDecimal fRetVal = cstmt.getBigDecimal(6,0);
            cstmt.close();

            boolean condition = (aRetVal == null &&
                                 bRetVal == null &&
                                 cRetVal == null &&
                                 dRetVal == null &&
                                 eRetVal == null &&
                                 fRetVal == null);
            if(!condition)
            {
                output_.println("aRetVal("+aRetVal+") != null");
                output_.println("bRetVal("+aRetVal+") != null");
                output_.println("cRetVal("+aRetVal+") != null");
                output_.println("dRetVal("+aRetVal+") != null");
                output_.println("eRetVal("+aRetVal+") != null");
                output_.println("fRetVal("+aRetVal+") != null");

            }
            assertCondition(condition); 
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }

    }



 /**
   * getBigDecimal() - Get a type that was registered as a BOOLEAN.
   **/
  public void Var049() {
    if (checkBooleanSupport()) {
      String info = " -- Test added 2021/01/06 for boolean";
      getTestSuccessful(csTypes_, 25, 0,"1",info);
    }
  }


  /**
   * getBigDecimal() - Get a type that was registered as a BOOLEAN that returns
   * false.
   **/
  public void Var050() {
    if (checkBooleanSupport()) {
      String info = " -- Test added 2021/01/06 for boolean";
           getTestSuccessful(csTypesB_, 25,0, "0",info);
    }
  }



}



