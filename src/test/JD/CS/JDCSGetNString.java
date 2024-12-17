///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetNString.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetNString.java
//
// Classes:      JDCSGetNString
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDTestDriver;

import java.awt.TextArea;
import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable;



/**
Testcase JDCSGetNString.  This tests the following
method of the JDBC CallableStatement class:

<ul>
<li>getNString()
</ul>
**/

public class JDCSGetNString
extends JDCSGetTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSGetNString";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCSTest.main(newArgs); 
   }


    /**
    Constructor.
    **/
    public JDCSGetNString (AS400 systemObject,
                          Hashtable namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDCSGetNString",
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

      super.setup("date format=iso;time format=jis");

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
    getNString() - Get parameter -1.
    **/
    public void Var001()
    {
	
        if (checkJdbc40()) try
        {
            csTypes_.execute ();
            String p = (String) JDReflectionUtil.callMethod_O(csTypes_, "getNString", -1);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getNString() - Get parameter 0.
    **/
    public void Var002()
    {
        if (checkJdbc40()) try
        {
            String p = (String) JDReflectionUtil.callMethod_O(csTypes_, "getNString", 0);
            failed ("Didn't throw SQLException"+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getNString() - Use a parameter that is too big.
    **/
    public void Var003()
    {
        if (checkJdbc40()) try
        {
            String p = (String) JDReflectionUtil.callMethod_O(csTypes_, "getNString", 35);
            failed ("Didn't throw SQLException"+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getNString() - Get a parameter when there are no parameters.
    **/
    public void Var004()
    {
	if (checkJdbc40()) { 
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
		String p = (String) JDReflectionUtil.callMethod_O(cs, "getNString", 1);
		failed ("Didn't throw SQLException "+p);
	    }
	    catch(Exception e)
	    {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }

	    if(c != null) try
	    {
		c.close();
	    }
	    catch(Exception e)
	    {
	    }
	}
    }


    /**
    getNString() - Get a parameter that was not registered.
    **/
    public void Var005()
    {
        if (checkJdbc40()) try
        {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
            cs.execute ();
            String p = (String) JDReflectionUtil.callMethod_O(cs, "getNString", 12);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getNString() - Get a parameter when the statement has not been
    executed.
    **/
    public void Var006()
    {
        if (checkJdbc40()) try
        {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
            cs.registerOutParameter (12, Types.VARCHAR);
            String p = (String) JDReflectionUtil.callMethod_O(cs, "getNString", 12);
            failed ("Didn't throw SQLException"+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getNString() - Get a parameter when the statement is closed.
    **/
    public void Var007()
    {
        if (checkJdbc40()) try
        {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
            cs.registerOutParameter (12, Types.VARCHAR);
            cs.execute ();
            cs.close ();
            String p = (String) JDReflectionUtil.callMethod_O(cs, "getNString", 12);
            failed ("Didn't throw SQLException"+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getNString() - Get an IN parameter that was correctly registered.
    **/
    public void Var008()
    {
        if (checkJdbc40()) try
        {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESIN, supportedFeatures_);
            JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESIN,
                                                 supportedFeatures_);
            cs.registerOutParameter (12, Types.VARCHAR);
            cs.execute ();
            String p = (String) JDReflectionUtil.callMethod_O(cs, "getNString", 12);
            failed ("Didn't throw SQLException"+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }


    /**
    getNString() - Get an INOUT parameter, where the OUT parameter is
    longer than the IN parameter.
    **/
    public void Var009()
    {
        if (checkJdbc40()) try
        {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_);
            JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                                 supportedFeatures_);
            JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                       supportedFeatures_, getDriver());
            cs.registerOutParameter (12, Types.VARCHAR);
            cs.execute ();
            String p = (String) JDReflectionUtil.callMethod_O(cs, "getNString", 12);
            assertCondition (p.equals ("MiJDBC"));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    getNString() - Get a type that was registered as a SMALLINT.
    **/
    public void Var010()
    {
        if (checkJdbc40()) try
        {
            String p = (String) JDReflectionUtil.callMethod_O(csTypes_, "getNString", 1);
            {
                if(p.equals("123"))
                    succeeded();
                else
                    failed(p + " != 123");
            }
        }
        catch(Exception e)
        {
                failed(e, "Unexpected Exception");
        }
    }



    /**
    getNString() - Get a type that was registered as an INTEGER.
    **/
    public void Var011()
    {
        if (checkJdbc40()) try
        {
            String p = (String) JDReflectionUtil.callMethod_O(csTypes_, "getNString", 2);
            {
                if(p.equals("-456"))
                    succeeded();
                else
                    failed(p + " != -456");
            }
        }
        catch(Exception e)
        {
                failed(e, "Unexpected Exception");
        }
    }



    /**
    getNString() - Get a type that was registered as an REAL.
    **/
    public void Var012()
    {
        if (checkJdbc40()) try
        {
            String p = (String) JDReflectionUtil.callMethod_O(csTypes_, "getNString", 3); // 789.54
            {
                if(p.equals("789.54"))
                    succeeded();
                else
                    failed(p + " != 789.54");
            }
        }
        catch(Exception e)
        {
                failed(e, "Unexpected Exception");
        }
    }



    /**
    getNString() - Get a type that was registered as an FLOAT.
    **/
    public void Var013()
    {
        if (checkJdbc40()) try
        {
            String p = (String) JDReflectionUtil.callMethod_O(csTypes_, "getNString", 4); // 253.1027
            {
                if(p.equals("253.1027"))
                    succeeded();
                else
                    failed(p + " != 253.1027");
            }
        }
        catch(Exception e)
        {
                failed(e, "Unexpected Exception");
        }
    }



    /**
    getNString() - Get a type that was registered as an DOUBLE.
    **/
    public void Var014()
    {
        if (checkJdbc40()) try
        {
            String p = (String) JDReflectionUtil.callMethod_O(csTypes_, "getNString", 5); // -987.3434
            {
                if(p.equals("-987.3434"))
                    succeeded();
                else
                    failed(p + " != -987.3434");
            }
        }
        catch(Exception e)
        {
                failed(e, "Unexpected Exception");
        }
    }



    /**
    getNString() - Get a type that was registered as an DECIMAL.
    **/
    public void Var015()
    {
        if (checkJdbc40()) try
        {
            String p = (String) JDReflectionUtil.callMethod_O(csTypes_, "getNString", 6); // 54362
            {
                if(p.equals("54362"))
                    succeeded();
                else
                    failed(p + " != 54362");
            }
        }
        catch(Exception e)
        {
                failed(e, "Unexpected Exception");
        }
    }



    /**
    getNString() - Get a type that was registered as an NUMERIC.
    **/
    public void Var016()
    {
        if (checkJdbc40()) try
        {
            String p = (String) JDReflectionUtil.callMethod_O(csTypes_, "getNString", 8); // -1112
            {
                if(p.equals("-1112"))
                    succeeded();
                else
                    failed(p + " != -1112");
            }
        }
        catch(Exception e)
        {
                failed(e, "Unexpected Exception");
        }
    }



    /**
    getNString() - Get a type that was registered as a CHAR(1).
    **/
    public void Var017()
    {
        if (checkJdbc40()) try
        {
            String p = (String) JDReflectionUtil.callMethod_O(csTypes_, "getNString", 10);
            assertCondition (p.equals ("C"));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getNString() - Get a type that was registered as a CHAR(50).
    **/
    public void Var018()
    {
        if (checkJdbc40()) try
        {
            String p = (String) JDReflectionUtil.callMethod_O(csTypes_, "getNString", 11);
            assertCondition (p.equals ("Jim                                               "));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getNString() - Get a type that was registered as a CHAR, with maxFieldSize
    set to 0.  Check that no DataTruncation warning is posted
    and the string is not trucated.
    **/
    public void Var019()
    {
        if (checkJdbc40()) try
        {
            csTypes_.setMaxFieldSize (0);
            csTypes_.clearWarnings();
            String p = (String) JDReflectionUtil.callMethod_O(csTypes_, "getNString", 11);
            assertCondition ((csTypes_.getWarnings () == null)
                             && (p.equals ("Jim                                               ")));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getNString() - Get a type that was registered as a CHAR, with maxFieldSize
    set to 5.  Check that no DataTruncation warning is posted
    and the string is truncated.
    **/
    public void Var020()
    {
        if (checkJdbc40()) try
        {
            csTypes_.setMaxFieldSize (5);
            csTypes_.clearWarnings();
            String p = (String) JDReflectionUtil.callMethod_O(csTypes_, "getNString", 11);
            DataTruncation dt = (DataTruncation) csTypes_.getWarnings();
            csTypes_.setMaxFieldSize (0);
            assertCondition ((dt == null) && (p.equals ("Jim  ")));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getNString() - Get a type that was registered as a VARCHAR(50).
    **/
    public void Var021()
    {
        if (checkJdbc40()) try
        {
            String p = (String) JDReflectionUtil.callMethod_O(csTypes_, "getNString", 12);
            if(p.equals("Charlie"))
                succeeded();
            else
                failed(p + " != Charlie");
        }
        catch(Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
    getNString() - Get a type that was registered as a VARCHAR, with maxFieldSize
    set to 0.  Check that no DataTruncation warning is posted
    and the string is not trucated.
    **/
    public void Var022()
    {
        if (checkJdbc40()) try
        {
            csTypes_.setMaxFieldSize(0);
            csTypes_.clearWarnings();
            String p = (String) JDReflectionUtil.callMethod_O(csTypes_, "getNString", 12);
            if(csTypes_.getWarnings() == null && p.equals("Charlie"))
                succeeded();
            else
            {
                SQLWarning warning = csTypes_.getWarnings();
                if(warning != null)
                    failed("Warning was posted: " + warning.getMessage());
                else if(!p.equals("Charlie"))
                    failed(p + " != Charlie");
                else
                    failed();
            }
        }
        catch(Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
    getNString() - Get a type that was registered as a VARCHAR, with maxFieldSize
    set to 5.  Check that no DataTruncation warning is posted
    and the string is truncated.
    **/
    public void Var023()
    {
        if (checkJdbc40()) try
        {
            csTypes_.setMaxFieldSize (5);
            csTypes_.clearWarnings();
            String p = (String) JDReflectionUtil.callMethod_O(csTypes_, "getNString", 12);
            DataTruncation dt = (DataTruncation) csTypes_.getWarnings();
            csTypes_.setMaxFieldSize (0);
            assertCondition ((dt == null) && (p.equals ("Charl")));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getNString() - Get a type that was registered as a BINARY.
    **/
    public void Var024()
    {
        if (checkJdbc40()) try
        {
            String p = (String) JDReflectionUtil.callMethod_O(csTypes_, "getNString", 13);
            {
		String expected = "4D75726368202020202020202020202020202020";
		if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		    expected = "\u004D\u0075\u0072\u0063\u0068\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020";
		} 
                if(p.equals(expected))
                    succeeded();
                else
                    failed(p + " != "+expected);
            }
        }
        catch(Exception e)
        {
                failed(e, "Unexpected Exception");
        }
    }



    /**
    getNString() - Get a type that was registered as a VARBINARY.
    **/
    public void Var025()
    {
        if (checkJdbc40()) try
        {
            String p = (String) JDReflectionUtil.callMethod_O(csTypes_, "getNString", 14);
            {

		String expected = "446176652057616C6C";
		if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		    expected = "Dave Wall";
		}
                if(p.equals(expected))
                    succeeded();
                else
                    failed(p + " != "+expected);
            }
        }
        catch(Exception e)
        {
                failed(e, "Unexpected Exception");
        }
    }



    /**
    getNString() - Get a type that was registered as a DATE.
    **/
    public void Var026()
    {
        if (checkJdbc40()) try
        {
            String p = (String) JDReflectionUtil.callMethod_O(csTypes_, "getNString", 15);
            {
                if(p.equals("1998-04-15"))
                    succeeded();
                else
                    failed(p + " != 1998-04-15");
            }
        }
        catch(Exception e)
        {
                failed(e, "Unexpected Exception");
        }
    }



    /**
    getNString() - Get a type that was registered as a TIME.
    **/
    public void Var027()
    {
        if (checkJdbc40()) try
        {
            String p = (String) JDReflectionUtil.callMethod_O(csTypes_, "getNString", 16);
            {
                if(p.equals("08:42:30"))
                    succeeded();
                else
                    failed(p + " != 08:42:30");
            }
        }
        catch(Exception e)
        {
                failed(e, "Unexpected Exception");
        }
    }



    /**
    getNString() - Get a type that was registered as a TIMESTAMP.
    **/
    public void Var028()
    {
        if (checkJdbc40()) try
        {
            String p = (String) JDReflectionUtil.callMethod_O(csTypes_, "getNString", 17);
            {
		String expected = "2001-11-18 13:42:22.123456";
		if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		    expected = "2001-11-18-13.42.22.123456";
		}
                if(p.equals(expected))
                    succeeded();
                else
                    failed(p + " != "+expected);
            }
        }
        catch(Exception e)
        {
                failed(e, "Unexpected Exception");
        }
    }



    /**
    getNString() - Get a type that was registered as an OTHER.
    **/
    public void Var029()
    {
        if(checkJdbc40 ())
        {
             try
            {
                String p = (String) JDReflectionUtil.callMethod_O(csTypes_, "getNString", 18);
                assertCondition (p.equals ("https://github.com/IBM/JTOpen-test/blob/main/README.md"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getNString() - Get a type that was registered as a BLOB.
    **/
    public void Var030()
    {
        if(checkJdbc40 ())
        {
            if (checkJdbc40()) try
            {
                String p = (String) JDReflectionUtil.callMethod_O(csTypes_, "getNString", 19);
                {
		    String expected = "446176652045676765"; 
		    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
			expected = "Dave Egge"; 
		    }
		    if(p.equals(expected))
			succeeded();
		    else
			failed(p + " != "+expected );
                }
            }
            catch(Exception e)
            {
                    failed(e, "Unexpected Exception");
            }
        }
    }



    /**
    getNString() - Get a type that was registered as a CLOB.
    
    SQL400 - The native driver allows this test to work.
    **/
    public void Var031()
    {
        if(checkJdbc40 ())
        {
            try
            {
                String p = (String) JDReflectionUtil.callMethod_O(csTypes_, "getNString", 20);
                if(p.equals("Chris Smyth"))
                    succeeded();
                else
                    failed(p + " != Chris Smyth");
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getNString() - Get a type that was registered as a BIGINT.
    **/
    public void Var032()
    {
        if(checkJdbc40())
        {
            try
            {
                String p = (String) JDReflectionUtil.callMethod_O(csTypes_, "getNString", 22);
                {
                    if(p.equals("987662234567"))
                        succeeded();
                    else
                        failed(p + " != 987662234567");
                }
            }
            catch(Exception e)
            {
                    failed(e, "Unexpected Exception");
            }
        }
    }



    /**
    getNString() - Get an INOUT parameter, where the OUT parameter is
    longer than the IN parameter, when the output parameter is registered first.
    
    SQL400 - We added this testcase because of a customer bug.
    **/
    public void Var033()
    {
        if (checkJdbc40()) try
        {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_);
            JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                       supportedFeatures_, getDriver());
            cs.registerOutParameter (12, Types.VARCHAR);
            JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                                 supportedFeatures_);
            cs.execute ();
            String p = (String) JDReflectionUtil.callMethod_O(cs, "getNString", 12);
            assertCondition (p.equals ("MiJDBC"));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    // @E1A
    /**
    getNString() - Get the return value parameter when it is registered.
    **/
    public void Var034()
    {
        if (checkJdbc40()) try
        {
            CallableStatement cs = connection_.prepareCall("?=CALL " + JDSetupProcedure.STP_CSRV);
            cs.registerOutParameter(1, Types.INTEGER);
            cs.execute ();
            String p = (String) JDReflectionUtil.callMethod_O(cs, "getNString", 1);
            assertCondition (p.equals("1976"), "p = "+ p + " and should 1976");
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
     * common code for variation 35 - 42
     */ 
    public void mixedTest(int variation,  String databaseType, int registerType, String maxString, String minString)
    {
	if (checkJdbc40())  { 
	    try
	    {
		Statement stmt = connection_.createStatement();

		try
		{
		    stmt.executeUpdate("drop table " + JDCSTest.COLLECTION + ".JDCSGSV" + variation + "_tab");
		}
		catch(Exception e)
		{
		}
		try
		{
		    stmt.executeUpdate("drop procedure " + JDCSTest.COLLECTION + ".JDCSGSV" + variation + "_Proc"); 
		}
		catch(Exception e)
		{
		}

		stmt.executeUpdate("create table " + JDCSTest.COLLECTION + ".JDCSGSV" + variation + "_Tab(MAX_VAL " + databaseType + ", MIN_VAL " + databaseType + ", NULL_VAL " + databaseType + " )"); 
		stmt.executeUpdate("create procedure " + JDCSTest.COLLECTION + ".JDCSGSV" + variation + "_Proc (out MAX_PARAM " + databaseType + ", out MIN_PARAM " + databaseType + ", out NULL_PARAM " + databaseType + ") LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from " + JDCSTest.COLLECTION + ".JDCSGSV" + variation + "_Tab; end");

		stmt.executeUpdate("insert into " + JDCSTest.COLLECTION + ".JDCSGSV" + variation + "_Tab values(" + maxString + "," + minString + ", null)");
		CallableStatement cstmt = connection_.prepareCall("{call " + JDCSTest.COLLECTION + ".JDCSGSV" + variation + "_Proc(?,?,?)}");

	    //register the output parameters
		cstmt.registerOutParameter(1, registerType);
		cstmt.registerOutParameter(2, registerType);
		cstmt.registerOutParameter(3, registerType);

	    //execute the procedure
		cstmt.execute();

	    //invoke getByte method
		String xRetVal = (String) JDReflectionUtil.callMethod_O(cstmt, "getNString", 1);
		String yRetVal = (String) JDReflectionUtil.callMethod_O(cstmt, "getNString", 2);
		String zRetVal = (String) JDReflectionUtil.callMethod_O(cstmt, "getNString", 3);
		boolean condition = (xRetVal.equals(maxString)) && (yRetVal.equals(minString)) && (zRetVal == null);
		if(!condition)
		{
		    System.out.println("xRetVal (" + xRetVal + ") != xCheck (" + maxString + ")" ); 
		    System.out.println("yRetVal (" + yRetVal + ") != yCheck (" + minString + ")");
		    System.out.println("zRetVal (" + zRetVal + ") != null"); 
		}

		assertCondition(condition); 

		try
		{
		    stmt.executeUpdate("drop table " + JDCSTest.COLLECTION + ".JDCSGSV" + variation + "_tab");
		}
		catch(Exception e)
		{
		}
		try
		{
		    stmt.executeUpdate("drop procedure " + JDCSTest.COLLECTION + ".JDCSGSV" + variation + "_Proc"); 
		}
		catch(Exception e)
		{
		}

	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }

	}
    } /* mixedTest */ 

    /**
    getNString() -- Get an output parameter where the database output type doesnt match bind type
                 DATABASE TYPE = SMALLINT  BOUND TYPE = Types.CHAR 
    */
    public void Var035()
    {
        mixedTest(35,  "SMALLINT" , Types.CHAR, "25", "0"); 
    }


    /**
    getNString() -- Get an output parameter where the database output type doesnt match bind type
                 DATABASE TYPE = INTEGER  BOUND TYPE = Types.VARCHAR
    */
    public void Var036()
    {
        mixedTest(36,  "INTEGER" , Types.VARCHAR, "25342", "0"); 
    }


    /**
    getNString() -- Get an output parameter where the database output type doesnt match bind type
                 DATABASE TYPE = BIGINT  BOUND TYPE = Types.LONGVARCHAR
    */
    public void Var037()
    {
        mixedTest(37,  "BIGINT" , Types.LONGVARCHAR, "25342", "0"); 
    }

    /**
    getNString() -- Get an output parameter where the database output type doesnt match bind type
                 DATABASE TYPE = REAL  BOUND TYPE = Types.CLOB
    */
    public void Var038()
    {
        if(checkJdbc20())
        {
            mixedTest(38,  "REAL" , Types.CLOB, "25342.1", "0.0");
        }
    }


    /**
    getNString() -- Get an output parameter where the database output type doesnt match bind type
                 DATABASE TYPE = FLOAT  BOUND TYPE = Types.CHAR 
    */
    public void Var039()
    {
        mixedTest(39,  "FLOAT" , Types.CHAR, "25.1", "0.0"); 
    }


    /**
    getNString() -- Get an output parameter where the database output type doesnt match bind type
                 DATABASE TYPE = DOUBLE  BOUND TYPE = Types.CHAR 
    */
    public void Var040()
    {
        mixedTest(40,  "DOUBLE" , Types.CHAR, "25.1", "0.0"); 
    }


    /**
    getNString() -- Get an output parameter where the database output type doesnt match bind type
                 DATABASE TYPE = DECIMAL  BOUND TYPE = Types.CHAR 
    */
    public void Var041()
    {
        mixedTest(41,  "DECIMAL" , Types.CHAR, "25", "0"); 
    }

    /**
    getNString() -- Get an output parameter where the database output type doesnt match bind type
                 DATABASE TYPE = NUMERIC  BOUND TYPE = Types.CHAR 
    */
    public void Var042()
    {
        mixedTest(42,  "NUMERIC" , Types.CHAR, "25", "0"); 
    }

    // - Named Parameter tests
    /**
    getNString() - Get A NAMED PARAMETER THAT DOESN'T EXIST
    **/
    public void Var043()
    {
        if(checkJdbc40())
        {
            try
            {
                String p = (String) JDReflectionUtil.callMethod_OS(csTypes_, "getNString", "P_FAKE");
                failed ("Didn't throw SQLException"+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }
    /**
    getNString() - Get a type that was registered as a SMALLINT.
    **/
    public void Var044()
    {
        if(checkJdbc40())
        {
            try
            {
                String p = (String) JDReflectionUtil.callMethod_OS(csTypes_, "getNString", "P_SMALLINT");
                {
                    if(p.equals("123"))
                        succeeded();
                    else
                        failed(p + " != 123");
                }
            }
            catch(Exception e)
            {
                    failed(e, "Unexpected Exception");
            }
        }
    }
    /**
    getNString() - Get a type that was registered as an INTEGER.
    **/
    public void Var045()
    {
        if(checkJdbc40())
        {
            try
            {
                String p = (String) JDReflectionUtil.callMethod_OS(csTypes_, "getNString", "P_INTEGER");
                {
                    if(p.equals("-456"))
                        succeeded();
                    else
                        failed(p + " != -456");
                }
            }
            catch(Exception e)
            {
                    failed(e, "Unexpected Exception");
            }
        }
    }


    /**
    getNString() - Get a type that was registered as an REAL.
    **/
    public void Var046()
    {
        if(checkJdbc40())
        {
            try
            {
                String p = (String) JDReflectionUtil.callMethod_OS(csTypes_, "getNString", "P_REAL");
                {
                    if(p.equals("789.54"))
                        succeeded();
                    else
                        failed(p + " != 789.54");
                }
            }
            catch(Exception e)
            {
                    failed(e, "Unexpected Exception");
            }
        }
    }


    /**
    getNString() - Get a type that was registered as an FLOAT.
    **/
    public void Var047()
    {
        if(checkJdbc40())
        {
            try
            {
                String p = (String) JDReflectionUtil.callMethod_OS(csTypes_, "getNString", "P_FLOAT");
                {
                    if(p.equals("253.1027"))
                        succeeded();
                    else
                        failed(p + " != 253.1027");
                }
            }
            catch(Exception e)
            {
                    failed(e, "Unexpected Exception");
            }
        }
    }


    /**
    getNString() - Get a type that was registered as an DOUBLE.
    **/
    public void Var048()
    {
        if(checkJdbc40())
        {
            try
            {
                String p = (String) JDReflectionUtil.callMethod_OS(csTypes_, "getNString", "P_DOUBLE");
                {
                    if(p.equals("-987.3434"))
                        succeeded();
                    else
                        failed(p + " != -987.3434");
                }
            }
            catch(Exception e)
            {
                    failed(e, "Unexpected Exception");
            }
        }
    }


    /**
    getNString() - Get a type that was registered as an DECIMAL.
    **/
    public void Var049()
    {
        if(checkJdbc40())
        {
            try
            {
                String p = (String) JDReflectionUtil.callMethod_OS(csTypes_, "getNString", "P_DECIMAL_50");
                {
                    if(p.equals("54362"))
                        succeeded();
                    else
                        failed(p + " != 54362");
                }
            }
            catch(Exception e)
            {
                    failed(e, "Unexpected Exception");
            }
        }
    }


    /**
    getNString() - Get a type that was registered as an NUMERIC.
    **/
    public void Var050()
    {
        if(checkJdbc40())
        {
            try
            {
                String p = (String) JDReflectionUtil.callMethod_OS(csTypes_, "getNString", "P_NUMERIC_50");
                {
                    if(p.equals("-1112"))
                        succeeded();
                    else
                        failed(p + " != -1112");
                }
            }
            catch(Exception e)
            {
                    failed(e, "Unexpected Exception");
            }
        }
    }


    /**
    getNString() - Get a type that was registered as a CHAR(1).
    **/
    public void Var051()
    {
        if(checkJdbc40())
        {
            try
            {
                String p = (String) JDReflectionUtil.callMethod_OS(csTypes_, "getNString", "P_CHAR_1");
                assertCondition (p.equals ("C"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }


    /**
    getNString() - Get a type that was registered as a CHAR(50).
    **/
    public void Var052()
    {
        if(checkJdbc40())
        {
            try
            {
                String p = (String) JDReflectionUtil.callMethod_OS(csTypes_, "getNString", "P_CHAR_50");
                assertCondition (p.equals ("Jim                                               "));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }


    /**
    getNString() - Get a type that was registered as a CHAR, with maxFieldSize
    set to 0.  Check that no DataTruncation warning is posted
    and the string is not trucated.
    **/
    public void Var053()
    {
        if(checkJdbc40())
        {
            try
            {
                csTypes_.setMaxFieldSize (0);
                csTypes_.clearWarnings();
                String p = (String) JDReflectionUtil.callMethod_OS(csTypes_, "getNString", "P_CHAR_50");
                assertCondition ((csTypes_.getWarnings () == null)
                                 && (p.equals ("Jim                                               ")));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }


    /**
    getNString() - Get a type that was registered as a CHAR, with maxFieldSize
    set to 5.  Check that no DataTruncation warning is posted
    and the string is truncated.
    **/
    public void Var054()
    {
        if(checkJdbc40())
        {
            try
            {
                csTypes_.setMaxFieldSize (5);
                csTypes_.clearWarnings();
                String p = (String) JDReflectionUtil.callMethod_OS(csTypes_, "getNString", "P_CHAR_50");
                DataTruncation dt = (DataTruncation) csTypes_.getWarnings();
                csTypes_.setMaxFieldSize (0);
                assertCondition ((dt == null) && (p.equals ("Jim  ")));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }


    /**
    getNString() - Get a type that was registered as a VARCHAR(50).
    **/
    public void Var055()
    {
        if(checkJdbc40())
        {
            try
            {
                String p = (String) JDReflectionUtil.callMethod_OS(csTypes_, "getNString", "P_VARCHAR_50");
                assertCondition (p.equals ("Charlie"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }


    /**
    getNString() - Get a type that was registered as a VARCHAR, with maxFieldSize
    set to 0.  Check that no DataTruncation warning is posted
    and the string is not trucated.
    **/
    public void Var056()
    {
        if(checkJdbc40())
        {
            try
            {
                csTypes_.setMaxFieldSize (0);
                csTypes_.clearWarnings();
                String p = (String) JDReflectionUtil.callMethod_OS(csTypes_, "getNString", "P_VARCHAR_50");
                assertCondition ((csTypes_.getWarnings () == null)
                                 && (p.equals ("Charlie")));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }


    /**
    getNString() - Get a type that was registered as a VARCHAR, with maxFieldSize
    set to 5.  Check that no DataTruncation warning is posted
    and the string is truncated.
    **/
    public void Var057()
    {
        if(checkJdbc40())
        {
            try
            {
                csTypes_.setMaxFieldSize (5);
                csTypes_.clearWarnings();
                String p = (String) JDReflectionUtil.callMethod_OS(csTypes_, "getNString", "P_VARCHAR_50");
                DataTruncation dt = (DataTruncation) csTypes_.getWarnings();
                csTypes_.setMaxFieldSize (0);
                assertCondition ((dt == null) && (p.equals ("Charl")));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }


    /**
    getNString() - Get a type that was registered as a BINARY.
    **/
    public void Var058()
    {
        if(checkJdbc40())
        {
            try
            {
                String p = (String) JDReflectionUtil.callMethod_OS(csTypes_, "getNString", "P_BINARY_30");
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


    /**
    getNString() - Get a type that was registered as a VARBINARY.
    **/
    public void Var059()
    {
        if(checkJdbc40())
        {
            try
            {
                String p = (String) JDReflectionUtil.callMethod_OS(csTypes_, "getNString", "P_VARBINARY_20");
                {
		    String expected = "446176652057616C6C";
		    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
			expected = "Dave Wall"; 
		    }
		    if(p.equals(expected))
			succeeded();
		    else
			failed(p + " != "+expected);

		}
            }
            catch(Exception e)
            {
                    failed(e, "Unexpected Exception");
            }
        }
    }


    /**
    getNString() - Get a type that was registered as a DATE.
    **/
    public void Var060()
    {
        if(checkJdbc40())
        {
            try
            {
                String p = (String) JDReflectionUtil.callMethod_OS(csTypes_, "getNString", "P_DATE");
                {
		    String expected = "1998-04-15"; 
                    if(p.equals(expected))
                        succeeded();
                    else
                        failed(p + " != "+expected);
                }
            }
            catch(Exception e)
            {
                    failed(e, "Unexpected Exception");
            }
        }
    }


    /**
    getNString() - Get a type that was registered as a TIME.
    **/
    public void Var061()
    {
        if(checkJdbc40())
        {
            try
            {
                String p = (String) JDReflectionUtil.callMethod_OS(csTypes_, "getNString", "P_TIME");
                {
                    if(p.equals("08:42:30"))
                        succeeded();
                    else
                        failed(p + " != 08:42:30");
                }
            }
            catch(Exception e)
            {
                    failed(e, "Unexpected Exception");
            }
        }
    }


    /**
    getNString() - Get a type that was registered as a TIMESTAMP.
    **/
    public void Var062()
    {
        if(checkJdbc40())
        {
            try
            {
                String p = (String) JDReflectionUtil.callMethod_OS(csTypes_, "getNString", "P_TIMESTAMP");
		{
		    String expected = "2001-11-18 13:42:22.123456";
		    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
			expected = "2001-11-18-13.42.22.123456";
		    }
		    if(p.equals(expected))
			succeeded();
		    else
			failed(p + " != "+expected);

                }
            }
            catch(Exception e)
            {
                    failed(e, "Unexpected Exception");
            }
        }
    }


    /**
    getNString() - Get a type that was registered as an OTHER.
    **/
    public void Var063()
    {
        if(checkJdbc40())
        {
            if(checkJdbc40 ())
            {
                try
                {
                    String p = (String) JDReflectionUtil.callMethod_OS(csTypes_, "getNString", "P_DATALINK");
                    assertCondition (p.equals ("https://github.com/IBM/JTOpen-test/blob/main/README.md"));
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }


    /**
    getNString() - Get a type that was registered as a BLOB.
    **/
    public void Var064()
    {
        if(checkJdbc40())
        {
            if(checkJdbc40 ())
            {
                try
                {
                    String p = (String) JDReflectionUtil.callMethod_OS(csTypes_, "getNString", "P_BLOB");
                    {
		    String expected = "446176652045676765"; 
		    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
			expected = "Dave Egge"; 
		    }
		    if(p.equals(expected))
			succeeded();
		    else
			failed(p + " != "+expected );

                    }
                }
                catch(Exception e)
                {
                        failed(e, "Unexpected Exception");
                }
            }
        }
    }


    /**
    getNString() - Get a type that was registered as a CLOB.
    
    SQL400 - The native driver allows this test to work.
    **/
    public void Var065()
    {
        if(checkJdbc40())
        {
            if(checkJdbc40 ())
            {
                try
                {
                    String p = (String) JDReflectionUtil.callMethod_OS(csTypes_, "getNString", "P_CLOB");
                    if(p.equals("Chris Smyth"))
                        succeeded();
                    else
                        failed(p + " != Chris Smyth");

                }
                catch(Exception e)
                {
                    failed(e, "Unexpected Exception");
                }
            }
        }
    }


    /**
    getNString() - Get a type that was registered as a BIGINT.
    **/
    public void Var066()
    {
        if(checkJdbc40())
        {
            if(checkJdbc40())
            {
                try
                {
                    String p = (String) JDReflectionUtil.callMethod_OS(csTypes_, "getNString", "P_BIGINT");
                    {
                        if(p.equals("987662234567"))
                            succeeded();
                        else
                            failed(p + " != 987662234567");
                    }
                }
                catch(Exception e)
                {
                        failed(e, "Unexpected Exception");
                }
            }
        }
    }

    /*
     Tests created to test lower case named parameters, the "real" test lie in the set and get methods
     which use the findParameter() method, to determine the if a case Sensitive search is used on the named
     parmeters.
    
     This problem is fixed in V5R3 for the native driver. 
    */

    /*
    This variation test the first named parameter with a case insensitive search
    */
    public void Var067()
    {
	if(checkJdbc40()) /* $E5 named parameters need jdbc 3.0 */
	{
	    if(getRelease() >=  JDTestDriver.RELEASE_V7R1M0)
	    {
		try
		{
		    String sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+".ADD10 (INOUT \"Col1\" CHAR(20))"+
		      " LANGUAGE SQL SPECIFIC ADD10 JDCSADD10: BEGIN DECLARE DUMMY"+
		      " CHAR(20); SET DUMMY = \"Col1\"; SET \"Col1\" = DUMMY ; END JDCSADD10";  

		    Statement stmt = connection_.createStatement ();

		    try
		    {
			stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".ADD10");
		    }
		    catch(Exception e)
		    {
		    }

		    stmt.executeUpdate(sql);
		    CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10 (?)}");
		    cstmt.registerOutParameter(1, java.sql.Types.CHAR);

		    JDReflectionUtil.callMethod_V(cstmt, "setNString", "cOl1", "one two three four");
		    cstmt.execute();

		    String s = (String) JDReflectionUtil.callMethod_O(cstmt, "getNString", 1);
		    assertCondition(s.equalsIgnoreCase("one two three four  "),"s = "+s);

		}
		catch(Exception e)
		{
		    failed (e, "Unexpected Exception");
		}
	    }
	    else
	    {
		notApplicable(); 
	    }
	}
    }

    /*
    This variation test the last named parameter with a case insensitive search
    */
    public void Var068()
    {
	if(checkJdbc40()) /* $E5 named parameters need jdbc 3.0 */
	{
	    if(getRelease() >=  JDTestDriver.RELEASE_V7R1M0)
	    {
		try
		{
		    String sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+".ADD10 (OUT \"Col1\" CHAR(20), OUT \"Col2\" CHAR(20),"+
		      " INOUT \"Col3\" CHAR(20)) LANGUAGE SQL SPECIFIC ADD10 JDCSADD10: BEGIN DECLARE DUMMY"+
		      " CHAR(20); SET DUMMY = \"Col3\"; SET \"Col3\" = DUMMY ; END JDCSADD10";  

		    Statement stmt = connection_.createStatement ();

		    try
		    {
			stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".ADD10");
		    }
		    catch(Exception e)
		    {
		    }

		    stmt.executeUpdate(sql);
		    CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10 (?,?,?)}");
		    cstmt.registerOutParameter(1, java.sql.Types.CHAR);
		    cstmt.registerOutParameter(2, java.sql.Types.CHAR);
		    cstmt.registerOutParameter(3, java.sql.Types.CHAR);

		    JDReflectionUtil.callMethod_V(cstmt, "setNString", "cOl3", "abc");
		    cstmt.execute();

		    String s = (String) JDReflectionUtil.callMethod_O(cstmt, "getNString", 3);
		    assertCondition(s.equalsIgnoreCase("abc                 "),"s = "+s);
		}
		catch(Exception e)
		{
		    failed (e, "Unexpected Exception");
		}
	    }
	    else
	    {
		notApplicable();
	    }
	}
    }

    /*
    This variation test the second named parameter with a case insensitive search
    */
    public void Var069()
    {
	if(checkJdbc40()) /* $E5 named parameters need jdbc 3.0 */
	{
	    if(getRelease() >=  JDTestDriver.RELEASE_V7R1M0)
	    {
		try
		{
		    String sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+".ADD10 (OUT \"Col1\" CHAR(20), INOUT \"Col2\" CHAR(20),"+
		      " OUT \"Col3\" CHAR(20)) LANGUAGE SQL SPECIFIC ADD10 JDCSADD10: BEGIN DECLARE DUMMY"+
		      " CHAR(20); SET DUMMY = \"Col2\"; SET \"Col2\" = DUMMY ; END JDCSADD10";  

		    Statement stmt = connection_.createStatement ();

		    try
		    {
			stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".ADD10");
		    }
		    catch(Exception e)
		    {
		    }

		    stmt.executeUpdate(sql);
		    CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10 (?,?,?)}");
		    cstmt.registerOutParameter(1, java.sql.Types.CHAR);
		    cstmt.registerOutParameter(2, java.sql.Types.CHAR);
		    cstmt.registerOutParameter(3, java.sql.Types.CHAR);

		    JDReflectionUtil.callMethod_V(cstmt, "setNString", "cOl2", "lmnop");
		    cstmt.execute();

		    String s = (String) JDReflectionUtil.callMethod_O(cstmt, "getNString", 2);
		    assertCondition(s.equalsIgnoreCase("lmnop               "));
		}
		catch(Exception e)
		{
		    failed (e, "Unexpected Exception");
		}
	    }
	    else
	    {
		notApplicable();
	    }
	}
    }

    /*
    This variation test the last and first named parameters with a case insensitive search
    */
    public void Var070()
    {
	if(checkJdbc40()) /* $E5 named parameters need jdbc 3.0 */
	{
	    if(getRelease() >=  JDTestDriver.RELEASE_V7R1M0)
	    {
		try
		{
		    String sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+".ADD10 (INOUT \"Col1\" CHAR(5), OUT \"Col2\" CHAR(5),"+
		      " INOUT \"Col3\" CHAR(5)) LANGUAGE SQL SPECIFIC ADD10 JDCSADD10: BEGIN DECLARE DUMMY"+
		      " CHAR(5); SET DUMMY = \"Col1\"; SET \"Col1\" = DUMMY ; END JDCSADD10";  

		    Statement stmt = connection_.createStatement ();

		    try
		    {
			stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".ADD10");
		    }
		    catch(Exception e)
		    {
		    }

		    stmt.executeUpdate(sql);
		    CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10 (?,?,?)}");
		    cstmt.registerOutParameter(1, java.sql.Types.CHAR);
		    cstmt.registerOutParameter(2, java.sql.Types.CHAR);
		    cstmt.registerOutParameter(3, java.sql.Types.CHAR);

		    JDReflectionUtil.callMethod_V(cstmt, "setNString", "cOl3", "xyz");
		    JDReflectionUtil.callMethod_V(cstmt, "setNString", "cOl1", "XYZ");
		    cstmt.execute();

		    String s = (String) JDReflectionUtil.callMethod_O(cstmt, "getNString", 1);
		    String t = (String) JDReflectionUtil.callMethod_O(cstmt, "getNString", 3);
		    assertCondition(s.equalsIgnoreCase("xyz  ") && (t.equalsIgnoreCase("XYZ  ")));
		}
		catch(Exception e)
		{
		    failed (e, "Unexpected Exception");
		}
	    }
	    else
	    {
		notApplicable();
	    }
	}
    }
    /*
    This variation test the sencond and last named parameters with a case insensitive search
    */
    public void Var071()
    {
	if(checkJdbc40()) /* $E5 named parameters need jdbc 3.0 */
	{
	    if(getRelease() >=  JDTestDriver.RELEASE_V7R1M0)
	    {
		try
		{
		    String sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+".ADD10 (OUT \"Col1\" CHAR(5), INOUT \"Col2\" CHAR(5),"+
		      " INOUT \"Col3\" CHAR(5)) LANGUAGE SQL SPECIFIC ADD10 JDCSADD10: BEGIN DECLARE DUMMY"+
		      " CHAR(5); SET DUMMY = \"Col1\"; SET \"Col1\" = DUMMY ; END JDCSADD10";  

		    Statement stmt = connection_.createStatement ();

		    try
		    {
			stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".ADD10");
		    }
		    catch(Exception e)
		    {
		    }

		    stmt.executeUpdate(sql);
		    CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10 (?,?,?)}");
		    cstmt.registerOutParameter(1, java.sql.Types.CHAR);
		    cstmt.registerOutParameter(2, java.sql.Types.CHAR);
		    cstmt.registerOutParameter(3, java.sql.Types.CHAR);

		    JDReflectionUtil.callMethod_V(cstmt, "setNString", "cOl2", "KJH");
		    JDReflectionUtil.callMethod_V(cstmt, "setNString", "cOl3", "kbj");
		    cstmt.execute();

		    String s = (String) JDReflectionUtil.callMethod_O(cstmt, "getNString", 2);
		    String t = (String) JDReflectionUtil.callMethod_O(cstmt, "getNString", 3);
		    assertCondition(s.equalsIgnoreCase("KJH  ") && (t.equalsIgnoreCase("kbj  ")));

		}
		catch(Exception e)
		{
		    failed (e, "Unexpected Exception");
		}
	    }
	    else
	    {
		notApplicable();
	    }
	}
    }
    /*
    This variation test the first and second named parameters with a case insensitive search
    */
    public void Var072()
    {
	if(checkJdbc40()) /* $E5 named parameters need jdbc 3.0 */
	{
	    if(getRelease() >=  JDTestDriver.RELEASE_V7R1M0)
	    {
		try
		{
		    String sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+".ADD10 (INOUT \"Col1\" CHAR(5), INOUT \"Col2\" CHAR(5),"+
		      " OUT \"Col3\" CHAR(5)) LANGUAGE SQL SPECIFIC ADD10 JDCSADD10: BEGIN DECLARE DUMMY"+
		      " CHAR(5); SET DUMMY = \"Col1\"; SET \"Col1\" = DUMMY ; END JDCSADD10";  

		    Statement stmt = connection_.createStatement ();

		    try
		    {
			stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".ADD10");
		    }
		    catch(Exception e)
		    {
		    }

		    stmt.executeUpdate(sql);
		    CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10 (?,?,?)}");
		    cstmt.registerOutParameter(1, java.sql.Types.CHAR);
		    cstmt.registerOutParameter(2, java.sql.Types.CHAR);
		    cstmt.registerOutParameter(3, java.sql.Types.CHAR);

		    JDReflectionUtil.callMethod_V(cstmt, "setNString", "col1", "ABC");
		    JDReflectionUtil.callMethod_V(cstmt, "setNString", "COL2", "xyz");
		    cstmt.execute();

		    String s = (String) JDReflectionUtil.callMethod_O(cstmt, "getNString", 1);
		    String t = (String) JDReflectionUtil.callMethod_O(cstmt, "getNString", 2);
		    assertCondition(s.equalsIgnoreCase("ABC  ") && (t.equalsIgnoreCase("xyz  ")));
		}
		catch(Exception e)
		{
		    failed (e, "Unexpected Exception");
		}
	    }
	    else
	    {
		notApplicable();
	    }
	}
    }
    /*
    This variation tests the first named parameter basically with a case insensitive search
    should set the first string twice 
    */
    public void Var073()
    {
	if(checkJdbc40()) /* $E5 named parameters need jdbc 3.0 */
	{
	    if(getRelease() >=  JDTestDriver.RELEASE_V7R1M0)
	    {
		try
		{
		    String sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+".ADD10 (INOUT \"One\" CHAR(8), INOUT \"ONe\" CHAR(8),"+
		      " INOUT \"ONE\" CHAR(8)) LANGUAGE SQL SPECIFIC ADD10 JDCSADD10: BEGIN DECLARE DUMMY"+
		      " CHAR(8); SET DUMMY = \"One\"; SET \"One\" = DUMMY ; "+
		      " SET DUMMY = \"ONe\"; SET \"ONe\" = DUMMY ; "+
		      " SET DUMMY = \"ONE\"; SET \"ONE\" = DUMMY ; END JDCSADD10";  

		    Statement stmt = connection_.createStatement ();

		    try
		    {
			stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".ADD10");
		    }
		    catch(Exception e)
		    {
		    }

		    stmt.executeUpdate(sql);
		    CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10 (?,?,?)}");
		    cstmt.registerOutParameter(1, java.sql.Types.CHAR);
		    cstmt.registerOutParameter(2, java.sql.Types.CHAR);
		    cstmt.registerOutParameter(3, java.sql.Types.CHAR);

		    JDReflectionUtil.callMethod_V(cstmt, "setNString", "\"One\"", "test One");
		    JDReflectionUtil.callMethod_V(cstmt, "setNString", "\"ONe\"", "test ONe");
		    JDReflectionUtil.callMethod_V(cstmt, "setNString", "\"ONE\"", "test ONE");
		    JDReflectionUtil.callMethod_V(cstmt, "setNString", "one", "tester");
		    cstmt.execute();

		    String r = (String) JDReflectionUtil.callMethod_OS(cstmt, "getNString", "\"One\"");
		    String s = (String) JDReflectionUtil.callMethod_OS(cstmt, "getNString", "\"ONe\"");
		    String t = (String) JDReflectionUtil.callMethod_OS(cstmt, "getNString", "\"ONE\"");
		    assertCondition((r.equalsIgnoreCase("tester  ")) && (s.equalsIgnoreCase("test ONe")) && (t.equalsIgnoreCase("test ONE")));
		}
		catch(Exception e)
		{
		    failed (e, "Unexpected Exception");
		}
	    }
	    else
	    {
		notApplicable();
	    }
	}
    }

    /*
    This variation test all named parameter with a case sensitive search
    */
    public void Var074()
    {
	if(checkJdbc40()) /* $E5 named parameters need jdbc 3.0 */
	{
	    if(getRelease() >=  JDTestDriver.RELEASE_V7R1M0)
	    {

		try
		{
		    String sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+".ADD10 (INOUT \"One\" CHAR(10), INOUT \"ONe\" CHAR(10),"+
		      " INOUT \"ONE\" CHAR(10)) LANGUAGE SQL SPECIFIC ADD10 JDCSADD10: BEGIN DECLARE DUMMY"+
		      " CHAR(10); SET DUMMY = \"One\"; SET \"One\" = DUMMY ; END JDCSADD10";  

		    Statement stmt = connection_.createStatement ();

		    try
		    {
			stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".ADD10");
		    }
		    catch(Exception e)
		    {
		    }

		    stmt.executeUpdate(sql);
		    CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10 (?,?,?)}");
		    cstmt.registerOutParameter(1, java.sql.Types.CHAR);
		    cstmt.registerOutParameter(2, java.sql.Types.CHAR);
		    cstmt.registerOutParameter(3, java.sql.Types.CHAR);

		    JDReflectionUtil.callMethod_V(cstmt, "setNString", "\"One\"", "test abc");
		    JDReflectionUtil.callMethod_V(cstmt, "setNString", "\"ONe\"", "test 123");
		    JDReflectionUtil.callMethod_V(cstmt, "setNString", "\"ONE\"", "test xyz");
		    cstmt.execute();

		    String s = (String) JDReflectionUtil.callMethod_OS(cstmt, "getNString", "\"One\"");
		    String t = (String) JDReflectionUtil.callMethod_OS(cstmt, "getNString", "\"ONe\"");
		    String r = (String) JDReflectionUtil.callMethod_OS(cstmt, "getNString", "\"ONE\"");
		    assertCondition((s.equalsIgnoreCase("test abc  ")) && (t.equalsIgnoreCase("test 123  ")) && (r.equalsIgnoreCase("test xyz  ")));
		}
		catch(Exception e)
		{
		    failed (e, "Unexpected Exception");
		}
	    }
	    else
	    {
		notApplicable();
	    }
	}
    }

    /*
    This variation tests all named parameter with a case sensitive search
    */
    public void Var075()
    {
	if(checkJdbc40()) /* $E5 named parameters need jdbc 3.0 */
	{
	    if(getRelease() >=  JDTestDriver.RELEASE_V7R1M0)
	    {
		try
		{
		    String sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+".ADD10 (INOUT \"One\" CHAR(10), INOUT \"ONe\" CHAR(10),"+
		      " INOUT \"ONE\" CHAR(10)) LANGUAGE SQL SPECIFIC ADD10 JDCSADD10: BEGIN DECLARE DUMMY"+
		      " CHAR(10); SET DUMMY = \"One\"; SET \"One\" = DUMMY ; END JDCSADD10";  

		    Statement stmt = connection_.createStatement ();

		    try
		    {
			stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".ADD10");
		    }
		    catch(Exception e)
		    {
		    }

		    stmt.executeUpdate(sql);
		    CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10 (?,?,?)}");
		    cstmt.registerOutParameter(1, java.sql.Types.CHAR);
		    cstmt.registerOutParameter(2, java.sql.Types.CHAR);
		    cstmt.registerOutParameter(3, java.sql.Types.CHAR);

		    JDReflectionUtil.callMethod_V(cstmt, "setNString", "\"ONE\"", "test xyz");
		    JDReflectionUtil.callMethod_V(cstmt, "setNString", "\"One\"", "test abc");
		    JDReflectionUtil.callMethod_V(cstmt, "setNString", "\"ONe\"", "test 123");
		    cstmt.execute();

		    String s = (String) JDReflectionUtil.callMethod_OS(cstmt, "getNString", "\"One\"");
		    String t = (String) JDReflectionUtil.callMethod_OS(cstmt, "getNString", "\"ONe\"");
		    String r = (String) JDReflectionUtil.callMethod_OS(cstmt, "getNString", "\"ONE\"");
		    assertCondition((s.equalsIgnoreCase("test abc  ")) && (t.equalsIgnoreCase("test 123  ")) && (r.equalsIgnoreCase("test xyz  ")));
		}
		catch(Exception e)
		{
		    failed (e, "Unexpected Exception");
		}
	    }
	    else
	    {
		notApplicable();
	    }
	}
    }

    /*
    This variation test all named parameter with a case sensitive search
    */
    public void Var076()
    {
	if(checkJdbc40()) /* $E5 named parameters need jdbc 3.0 */
	{
	    if(getRelease() >=  JDTestDriver.RELEASE_V7R1M0)
	    {
		try
		{
		    String sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+".ADD10 (INOUT \"One\" CHAR(10), INOUT \"ONe\" CHAR(10),"+
		      " INOUT \"ONE\" CHAR(10)) LANGUAGE SQL SPECIFIC ADD10 JDCSADD10: BEGIN DECLARE DUMMY"+
		      " CHAR(10); SET DUMMY = \"One\"; SET \"One\" = DUMMY ; END JDCSADD10";  

		    Statement stmt = connection_.createStatement ();

		    try
		    {
			stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".ADD10");
		    }
		    catch(Exception e)
		    {
		    }

		    stmt.executeUpdate(sql);
		    CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10 (?,?,?)}");
		    cstmt.registerOutParameter(1, java.sql.Types.CHAR);
		    cstmt.registerOutParameter(2, java.sql.Types.CHAR);
		    cstmt.registerOutParameter(3, java.sql.Types.CHAR);

		    JDReflectionUtil.callMethod_V(cstmt, "setNString", "\"ONe\"", "test 123");
		    JDReflectionUtil.callMethod_V(cstmt, "setNString", "\"One\"", "test abc");
		    JDReflectionUtil.callMethod_V(cstmt, "setNString", "\"ONE\"", "test xyz");
		    cstmt.execute();

		    String s = (String) JDReflectionUtil.callMethod_OS(cstmt, "getNString", "\"One\"");
		    String t = (String) JDReflectionUtil.callMethod_OS(cstmt, "getNString", "\"ONe\"");
		    String r = (String) JDReflectionUtil.callMethod_OS(cstmt, "getNString", "\"ONE\"");
		    assertCondition((s.equalsIgnoreCase("test abc  ")) && (t.equalsIgnoreCase("test 123  ")) && (r.equalsIgnoreCase("test xyz  ")));
		}
		catch(Exception e)
		{
		    failed (e, "Unexpected Exception");
		}
	    }
	    else
	    {
		notApplicable();
	    }
	}
    }
    /*
    This variation test a named parameter with a case sensitive search, should fail
    */
    public void Var077()
    {
	if(checkJdbc40()) /* $E5 named parameters need jdbc 3.0 */
	{
	    if(getRelease() >=  JDTestDriver.RELEASE_V7R1M0)
	    {
		try
		{
		    String sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+".ADD10 (INOUT \"One\" CHAR(10), INOUT \"ONe\" CHAR(10),"+
		      " INOUT \"ONE\" CHAR(10)) LANGUAGE SQL SPECIFIC ADD10 JDCSADD10: BEGIN DECLARE DUMMY"+
		      " CHAR(10); SET DUMMY = \"One\"; SET \"One\" = DUMMY ; END JDCSADD10";  

		    Statement stmt = connection_.createStatement ();

		    try
		    {
			stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".ADD10");
		    }
		    catch(Exception e)
		    {
		    }

		    stmt.executeUpdate(sql);
		    CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10 (?,?,?)}");
		    cstmt.registerOutParameter(1, java.sql.Types.CHAR);
		    cstmt.registerOutParameter(2, java.sql.Types.CHAR);
		    cstmt.registerOutParameter(3, java.sql.Types.CHAR);

		    JDReflectionUtil.callMethod_V(cstmt, "setNString", "\"one\"", "test abc");
		    cstmt.execute();

		    failed("Didn't throw SQLException");
		}
		catch(Exception e)
		{
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	    else
	    {
		notApplicable();
	    }
	}
    }

    /*
    This variation tests a good named parameter, and then tries to find an invalid named parameter
     with a case sensitive search
    */
    public void Var078()
    {
	if(checkJdbc40()) /* $E5 named parameters need jdbc 3.0 */
	{
	    if(getRelease() >=  JDTestDriver.RELEASE_V7R1M0)
	    {
		try
		{
		    String sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+".ADD10 (INOUT \"One\" CHAR(10), INOUT \"ONe\" CHAR(10),"+
		      " INOUT \"ONE\" CHAR(10)) LANGUAGE SQL SPECIFIC ADD10 JDCSADD10: BEGIN DECLARE DUMMY"+
		      " CHAR(10); SET DUMMY = \"One\"; SET \"One\" = DUMMY ; END JDCSADD10";  

		    Statement stmt = connection_.createStatement ();

		    try
		    {
			stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".ADD10");
		    }
		    catch(Exception e)
		    {
		    }

		    stmt.executeUpdate(sql);
		    CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".ADD10 (?,?,?)}");
		    cstmt.registerOutParameter(1, java.sql.Types.CHAR);
		    cstmt.registerOutParameter(2, java.sql.Types.CHAR);
		    cstmt.registerOutParameter(3, java.sql.Types.CHAR);

		    JDReflectionUtil.callMethod_V(cstmt, "setNString", "\"ONe\"", "test abc");
		    JDReflectionUtil.callMethod_V(cstmt, "setNString", "\"one\"", "test 123");
		    cstmt.execute();

		    failed("Didn't throw SQLException");
		}
		catch(Exception e)
		{
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException"); 
		}
	    }
	    else
	    {
		notApplicable();
	    }
	}
    }

    /**
     * getString() - Get a type that was registered as a BOOLEAN.
     **/
    public void Var079() {
      if (checkBooleanSupport()) {
        String info = " -- Test added 2021/01/06 for boolean";
        getTestSuccessful(csTypes_, "getNString", 25, "1",info);
      }
    }


    /**
     * getString() - Get a type that was registered as a BOOLEAN that returns
     * false.
     **/
    public void Var080() {
      if (checkBooleanSupport()) { 
        String info = " -- Test added 2021/01/06 for boolean";
        getTestSuccessful(csTypesB_, "getNString", 25, "0", info);
      }
    }
    /**
     * getString() - Get a type that was registered as a BOOLEAN that returns
     * false.
     **/
    public void Var081() {
      if (checkBooleanSupport()) { 
        String info = " -- Test added 2021/01/06 for boolean";
        getTestSuccessful(csTypesNull_, "getNString", 25, "null", info);
      }
    }
    
    
    
}

