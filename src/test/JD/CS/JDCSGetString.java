///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetString.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetString.java
//
// Classes:      JDCSGetString
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JD.JDTestUtilities;



/**
Testcase JDCSGetString.  This tests the following
method of the JDBC CallableStatement class:

<ul>
<li>getString()
</ul>
**/

public class JDCSGetString
extends JDCSGetTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSGetString";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCSTest.main(newArgs); 
   }

 
    boolean runningJ9 = false;
    
    
    /**
    Constructor.
    **/
    public JDCSGetString (AS400 systemObject,
                          Hashtable<String,Vector<String>> namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDCSGetString",
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
 
	if (getDriver() == JDTestDriver.DRIVER_NATIVE) { /* determine if running J9 */ 
	    String vmName = System.getProperty("java.vm.name");
	    if (vmName ==  null) {
		runningJ9 = false; 
	    } else { 
		if (vmName.indexOf("Classic VM") >= 0) {
		    runningJ9 = false;
		} else {
		    runningJ9 = true;
		}
	    }
	}

    }



    /**
    Performs cleanup needed after running variations.
    
    @exception Exception If an exception occurs.
    **/
    protected void cleanup ()
    throws Exception
    {
        connection_.close ();
    }



    /**
    getString() - Get parameter -1.
    **/
    public void Var001()
    {
        try
        {
            csTypes_.execute ();
	    try { 
		String p = csTypes_.getString (-1);
		failed ("Didn't throw SQLException "+p);
	    }
	    catch(Exception e)
	    {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
	catch(Exception e)
	{
	    failed(e, "Unexpected exception"); 
	}

    }



    /**
    getString() - Get parameter 0.
    **/
    public void Var002()
    {
        try
        {
            String p = csTypes_.getString (0);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getString() - Use a parameter that is too big.
    **/
    public void Var003()
    {
        try
        {
            String p = csTypes_.getString (35);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getString() - Get a parameter when there are no parameters.
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
	    try { 
		String p = cs.getString (1);
		failed ("Didn't throw SQLException "+p);
	    }
	    catch(Exception e)
	    {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
	catch(Exception e)
	{
	    failed(e, "Unexpected exception"); 
	}


        if(c != null) try
            {
                c.close();
            }
            catch(Exception e)
            {
            }
    }



    /**
    getString() - Get a parameter that was not registered. (fails on execute) 
    **/
    public void Var005()
    {
        try
        {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
							     JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
	    try { 
		cs.execute ();
		String p = cs.getString (12);
		failed ("Didn't throw SQLException "+p);
	    }
	    catch(Exception e)
	    {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
	catch(Exception e)
	{
	    failed(e, "Unexpected exception"); 
	}

    }



  /**
   * getString() - Get a parameter when the statement has not been executed.
   **/
  public void Var006() {
    try {
      CallableStatement cs = JDSetupProcedure.prepare(connection_,
          JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_
          );
      JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSTYPESOUT,
          supportedFeatures_, getDriver());
      cs.registerOutParameter(12, Types.VARCHAR);
      try {
        String p = cs.getString(12);
        failed("Didn't throw SQLException " + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }

  }

  /**
   * getString() - Get a parameter when the statement is closed.
   **/
  public void Var007() {
    try {
      CallableStatement cs = JDSetupProcedure.prepare(connection_,
          JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_
          );
      JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSTYPESOUT,
          supportedFeatures_, getDriver());

      cs.registerOutParameter(12, Types.VARCHAR);
      cs.execute();
      cs.close();
      try {
        String p = cs.getString(12);
        failed("Didn't throw SQLException " + p);
      } catch (Exception e) {
	  assertClosedException(e, "");
      }

    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }


    /**
    getString() - Get an IN parameter that was correctly registered.
    **/
    public void Var008()
    {
	if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
	    assertCondition(true, "Native driver allows get from input only parameter.  Updated 0214/08/06");
	    return ; 
	} 
        try
        {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESIN, supportedFeatures_);
            JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESIN,
						 supportedFeatures_);

	    // cs.registerOutParameter (12, Types.VARCHAR); // This failed because is input parm
	    cs.execute ();
	    try { 
		String p = cs.getString (12);
		failed ("Didn't throw SQLException "+p);
	    }
	    catch(Exception e)
	    {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
	catch(Exception e)
	{
	    failed(e, "Unexpected exception"); 
	}

    }


    /**
    getString() - Get an INOUT parameter, where the OUT parameter is
    longer than the IN parameter.
    **/
    public void Var009()
    {
        try
        {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_);
            JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                                 supportedFeatures_);
            JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                       supportedFeatures_, getDriver());
            cs.registerOutParameter (12, Types.VARCHAR);
            cs.execute ();
            String p = cs.getString (12);
            assertCondition (p.equals ("MiJDBC"));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    getString() - Get a type that was registered as a SMALLINT.
    **/
    public void Var010()
    {
	try
	{
	    String p = csTypes_.getString(1);
	    if(p.equals("123"))
		succeeded();
	    else
		failed(p + " != 123");
	}
	catch(Exception e)
	{
	    failed(e, "Unexpected Exception");
	}
    }



    /**
    getString() - Get a type that was registered as an INTEGER.
    **/
    public void Var011()
    {
	try
	{
	    String p = csTypes_.getString(2);
	    if(p.equals("-456"))
		succeeded();
	    else
		failed(p + " != -456");
	}
	catch(Exception e)
	{
	    failed(e, "Unexpected Exception");
	}
    }



    /**
    getString() - Get a type that was registered as an REAL.
    **/
    public void Var012()
    {
        try
        {
            String p = csTypes_.getString(3); // 789.54
                if(p.equals("789.54"))
                    succeeded();
                else
                    failed(p + " != 789.54");
        }
        catch(Exception e)
        {
                failed(e, "Unexpected Exception");
        }
    }



    /**
    getString() - Get a type that was registered as an FLOAT.
    **/
    public void Var013()
    {
        try
        {
            String p = csTypes_.getString(4); // 253.1027
                if(p.equals("253.1027"))
                    succeeded();
                else
                    failed(p + " != 253.1027");
        }
        catch(Exception e)
        {
                failed(e, "Unexpected Exception");
        }
    }



    /**
    getString() - Get a type that was registered as an DOUBLE.
    **/
    public void Var014()
    {
        try
        {
            String p = csTypes_.getString(5); // -987.3434
                if(p.equals("-987.3434"))
                    succeeded();
                else
                    failed(p + " != -987.3434");
        }
        catch(Exception e)
        {
                failed(e, "Unexpected Exception");
        }
    }



    /**
    getString() - Get a type that was registered as an DECIMAL.
    **/
    public void Var015()
    {
        try
        {
            String p = csTypes_.getString(6); // 54362
                if(p.equals("54362"))
                    succeeded();
                else
                    failed(p + " != 54362");
        }
        catch(Exception e)
        {
                failed(e, "Unexpected Exception");
        }
    }



    /**
    getString() - Get a type that was registered as an NUMERIC.
    **/
    public void Var016()
    {
        try
	{
	    String p = csTypes_.getString(8); // -1112
	    if(p.equals("-1112"))
		succeeded();
	    else
		failed(p + " != -1112");
	}
	catch(Exception e)
	{
	    failed(e, "Unexpected Exception");
	}
    }



    /**
    getString() - Get a type that was registered as a CHAR(1).
    **/
    public void Var017()
    {
        try
        {
            String p = csTypes_.getString (10);
            assertCondition (p.equals ("C"));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getString() - Get a type that was registered as a CHAR(50).
    **/
    public void Var018()
    {
        try
        {
            String p = csTypes_.getString (11);
            assertCondition (p.equals ("Jim                                               "));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getString() - Get a type that was registered as a CHAR, with maxFieldSize
    set to 0.  Check that no DataTruncation warning is posted
    and the string is not trucated.
    **/
    public void Var019()
    {
        try
        {
            csTypes_.setMaxFieldSize (0);
            csTypes_.clearWarnings();
            String p = csTypes_.getString (11);
            assertCondition ((csTypes_.getWarnings () == null)
                             && (p.equals ("Jim                                               ")));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getString() - Get a type that was registered as a CHAR, with maxFieldSize
    set to 5.  Check that no DataTruncation warning is posted
    and the string is truncated.
    **/
    public void Var020()
    {
        try
        {
            csTypes_.setMaxFieldSize (5);
            csTypes_.clearWarnings();
            String p = csTypes_.getString (11);
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
    getString() - Get a type that was registered as a VARCHAR(50).
    **/
    public void Var021()
    {
        try
        {
            String p = csTypes_.getString(12);
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
    getString() - Get a type that was registered as a VARCHAR, with maxFieldSize
    set to 0.  Check that no DataTruncation warning is posted
    and the string is not trucated.
    **/
    public void Var022()
    {
        try
        {
            csTypes_.setMaxFieldSize(0);
            csTypes_.clearWarnings();
            String p = csTypes_.getString(12);
            if(csTypes_.getWarnings() == null && p.equals("Charlie"))
                succeeded();
            else
            {
                SQLWarning warning = csTypes_.getWarnings();
                String message = new String();
                if(warning != null)
                    failed("Warning was posted: " + warning.getMessage()+" message="+message);
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
    getString() - Get a type that was registered as a VARCHAR, with maxFieldSize
    set to 5.  Check that no DataTruncation warning is posted
    and the string is truncated.
    **/
    public void Var023()
    {
        try
        {
            csTypes_.setMaxFieldSize (5);
            csTypes_.clearWarnings();
            String p = csTypes_.getString (12);
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
    getString() - Get a type that was registered as a BINARY.
    **/
    public void Var024()
    {
        try
        {
	    
	    String expected = "4D75726368202020202020202020202020202020";
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) { /* getString on binary */ 
		expected = "Murch               "; 
	    } 
            String p = csTypes_.getString (13);
	    if(p.equals(expected))
		succeeded();
	    else
		failed(p + " != "+expected );
        }
        catch(Exception e)
        {
                failed(e, "Unexpected Exception");
        }
    }



    /**
    getString() - Get a type that was registered as a VARBINARY.
    **/
    public void Var025()
    {
        try
        {
	    String expected = "446176652057616C6C";
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) { /* getString on var binary */ 
		expected = "\u0044\u0061\u0076\u0065\u0020\u0057\u0061\u006c\u006c"; 
	    } 

            String p = csTypes_.getString (14);
                if(p.equals(expected))
                    succeeded();
                else
                    failed(p + " != "+expected);
        }
        catch(Exception e)
        {
                failed(e, "Unexpected Exception");
        }
    }



    /**
    getString() - Get a type that was registered as a DATE.
    **/
    public void Var026()
    {
        try
        {
	    String expected = "04/15/98";
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) { /* native defaults to iso format */ 
		expected = "1998-04-15"; 
	    } 

            String p = csTypes_.getString (15);
                if(p.equals(expected))
                    succeeded();
                else
                    failed(p + " != "+expected);
        }
        catch(Exception e)
        {
	    failed(e, "Unexpected Exception");
        }
    }



    /**
    getString() - Get a type that was registered as a TIME.
    **/
    public void Var027()
    {
        try
        {
	    String expected = "08:42:30";
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) { /* native defaults to iso (not jis) format */ 
		expected = "08.42.30"; 
	    } 

            String p = csTypes_.getString (16);
	    if(p.equals(expected))
		succeeded();
	    else
		failed(p + " != "+expected );
        }
        catch(Exception e)
        {
                failed(e, "Unexpected Exception");
        }
    }



    /**
    getString() - Get a type that was registered as a TIMESTAMP.
    **/
    public void Var028()
    {
        try
        {
	    String expected = "2001-11-18 13:42:22.123456";
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) { /* native defaults IBM SQL TS  format */ 
		expected = "2001-11-18-13.42.22.123456"; 
	    } 

            String p = csTypes_.getString (17);
                if(p.equals(expected))
                    succeeded();
                else
                    failed(p + " != "+expected); 
        }
        catch(Exception e)
        {
                failed(e, "Unexpected Exception");
        }
    }



    /**
    getString() - Get a type that was registered as an OTHER.
    **/
    public void Var029()
    {
        if(checkLobSupport ())
        {
            try
            {
                String p = csTypes_.getString (18);
                assertCondition (p.equals ("https://github.com/IBM/JTOpen-test/blob/main/README.md"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getString() - Get a type that was registered as a BLOB.
    **/
    public void Var030()
    {
        if(checkLobSupport ())
        {
            try
            {
                String p = csTypes_.getString (19);

		String expected = "446176652045676765";
		if(getDriver() == JDTestDriver.DRIVER_NATIVE)  {
		    expected = "Dave Egge"; 
		} 
		if(p.equals(expected))
		    succeeded();
		else
		    failed(p + " != "+expected+" Updated 11/11/2011 for JDBC 4.1");
	    
            }
            catch(Exception e)
            {
		failed(e, "Unexpected Exception "+" Updated 11/11/2011 for JDBC 4.1");
            }
        }
    }



    /**
    getString() - Get a type that was registered as a CLOB.
    
    SQL400 - The native driver allows this test to work.
    **/
    public void Var031()
    {
        if(checkLobSupport ())
        {
            try
            {
                String p = csTypes_.getString (20);
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
    getString() - Get a type that was registered as a BIGINT.
    **/
    public void Var032()
    {
        if(checkBigintSupport())
        {
            try
            {
                String p = csTypes_.getString (22);
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
    getString() - Get an INOUT parameter, where the OUT parameter is
    longer than the IN parameter, when the output parameter is registered first.
    
    SQL400 - We added this testcase because of a customer bug.
    **/
    public void Var033()
    {
        try
        {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_);
            JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                       supportedFeatures_, getDriver());
            cs.registerOutParameter (12, Types.VARCHAR);
            JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                                 supportedFeatures_);
            cs.execute ();
            String p = cs.getString (12);
            assertCondition (p.equals ("MiJDBC"));
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    // @E1A
    /**
    getString() - Get the return value parameter when it is registered.
    **/
    public void Var034()
    {
        try
        {
            CallableStatement cs = connection_.prepareCall("?=CALL " + JDSetupProcedure.STP_CSRV);
            cs.registerOutParameter(1, Types.INTEGER);
            cs.execute ();
            String p = cs.getString(1);
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
            String xRetVal = cstmt.getString(1);
            String yRetVal = cstmt.getString(2);
            String zRetVal = cstmt.getString(3);
            boolean condition = (xRetVal.equals(maxString)) && (yRetVal.equals(minString)) && (zRetVal == null);
            if(!condition)
            {
                output_.println("xRetVal (" + xRetVal + ") != xCheck (" + maxString + ")" ); 
                output_.println("yRetVal (" + yRetVal + ") != yCheck (" + minString + ")");
                output_.println("zRetVal (" + zRetVal + ") != null"); 
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

    } /* mixedTest */ 

    /**
    getString() -- Get an output parameter where the database output type doesnt match bind type
                 DATABASE TYPE = SMALLINT  BOUND TYPE = Types.CHAR 
    */
    public void Var035()
    {
        mixedTest(35,  "SMALLINT" , Types.CHAR, "25", "0"); 
    }


    /**
    getString() -- Get an output parameter where the database output type doesnt match bind type
                 DATABASE TYPE = INTEGER  BOUND TYPE = Types.VARCHAR
    */
    public void Var036()
    {
        mixedTest(36,  "INTEGER" , Types.VARCHAR, "25342", "0"); 
    }


    /**
    getString() -- Get an output parameter where the database output type doesnt match bind type
                 DATABASE TYPE = BIGINT  BOUND TYPE = Types.LONGVARCHAR
    */
    public void Var037()
    {
        mixedTest(37,  "BIGINT" , Types.LONGVARCHAR, "25342", "0"); 
    }

    /**
    getString() -- Get an output parameter where the database output type doesnt match bind type
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
    getString() -- Get an output parameter where the database output type doesnt match bind type
                 DATABASE TYPE = FLOAT  BOUND TYPE = Types.CHAR 
    */
    public void Var039()
    {
        mixedTest(39,  "FLOAT" , Types.CHAR, "25.1", "0.0"); 
    }


    /**
    getString() -- Get an output parameter where the database output type doesnt match bind type
                 DATABASE TYPE = DOUBLE  BOUND TYPE = Types.CHAR 
    */
    public void Var040()
    {
        mixedTest(40,  "DOUBLE" , Types.CHAR, "25.1", "0.0"); 
    }


    /**
    getString() -- Get an output parameter where the database output type doesnt match bind type
                 DATABASE TYPE = DECIMAL  BOUND TYPE = Types.CHAR 
    */
    public void Var041()
    {
        mixedTest(41,  "DECIMAL" , Types.CHAR, "25", "0"); 
    }

    /**
    getString() -- Get an output parameter where the database output type doesnt match bind type
                 DATABASE TYPE = NUMERIC  BOUND TYPE = Types.CHAR 
    */
    public void Var042()
    {
        mixedTest(42,  "NUMERIC" , Types.CHAR, "25", "0"); 
    }

    // - Named Parameter tests
    /**
    getString() - Get A NAMED PARAMETER THAT DOESN'T EXIST
    **/
    public void Var043()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                String p = csTypes_.getString ("P_FAKE");
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }
    /**
    getString() - Get a type that was registered as a SMALLINT.
    **/
    public void Var044()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                String p = csTypes_.getString ("P_SMALLINT");
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
    getString() - Get a type that was registered as an INTEGER.
    **/
    public void Var045()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                String p = csTypes_.getString ("P_INTEGER");
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
    getString() - Get a type that was registered as an REAL.
    **/
    public void Var046()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                String p = csTypes_.getString ("P_REAL");
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
    getString() - Get a type that was registered as an FLOAT.
    **/
    public void Var047()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                String p = csTypes_.getString ("P_FLOAT");
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
    getString() - Get a type that was registered as an DOUBLE.
    **/
    public void Var048()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                String p = csTypes_.getString ("P_DOUBLE");
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
    getString() - Get a type that was registered as an DECIMAL.
    **/
    public void Var049()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                String p = csTypes_.getString ("P_DECIMAL_50");
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
    getString() - Get a type that was registered as an NUMERIC.
    **/
    public void Var050()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                String p = csTypes_.getString ("P_NUMERIC_50");
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
    getString() - Get a type that was registered as a CHAR(1).
    **/
    public void Var051()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                String p = csTypes_.getString ("P_CHAR_1");
                assertCondition (p.equals ("C"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }


    /**
    getString() - Get a type that was registered as a CHAR(50).
    **/
    public void Var052()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                String p = csTypes_.getString ("P_CHAR_50");
                assertCondition (p.equals ("Jim                                               "));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }


    /**
    getString() - Get a type that was registered as a CHAR, with maxFieldSize
    set to 0.  Check that no DataTruncation warning is posted
    and the string is not trucated.
    **/
    public void Var053()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                csTypes_.setMaxFieldSize (0);
                csTypes_.clearWarnings();
                String p = csTypes_.getString ("P_CHAR_50");
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
    getString() - Get a type that was registered as a CHAR, with maxFieldSize
    set to 5.  Check that no DataTruncation warning is posted
    and the string is truncated.
    **/
    public void Var054()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                csTypes_.setMaxFieldSize (5);
                csTypes_.clearWarnings();
                String p = csTypes_.getString ("P_CHAR_50");
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
    getString() - Get a type that was registered as a VARCHAR(50).
    **/
    public void Var055()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                String p = csTypes_.getString ("P_VARCHAR_50");
                assertCondition (p.equals ("Charlie"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }


    /**
    getString() - Get a type that was registered as a VARCHAR, with maxFieldSize
    set to 0.  Check that no DataTruncation warning is posted
    and the string is not trucated.
    **/
    public void Var056()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                csTypes_.setMaxFieldSize (0);
                csTypes_.clearWarnings();
                String p = csTypes_.getString ("P_VARCHAR_50");
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
    getString() - Get a type that was registered as a VARCHAR, with maxFieldSize
    set to 5.  Check that no DataTruncation warning is posted
    and the string is truncated.
    **/
    public void Var057()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                csTypes_.setMaxFieldSize (5);
                csTypes_.clearWarnings();
                String p = csTypes_.getString ("P_VARCHAR_50");
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
    getString() - Get a type that was registered as a BINARY.
    **/
    public void Var058()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                String p = csTypes_.getString ("P_BINARY_30");
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


    /**
    getString() - Get a type that was registered as a VARBINARY.
    **/
    public void Var059()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                String p = csTypes_.getString ("P_VARBINARY_20");
                {
		    String expected = "446176652057616C6C"; 
		      if (getDriver() == JDTestDriver.DRIVER_NATIVE) { /* getString on var binary */ 
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
    getString() - Get a type that was registered as a DATE.
    **/
    public void Var060()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                String p = csTypes_.getString ("P_DATE");
                {
		    String expected = "04/15/98";
		    if (getDriver() == JDTestDriver.DRIVER_NATIVE) { /* native defaults to iso format */ 
			expected = "1998-04-15"; 
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
    getString() - Get a type that was registered as a TIME.
    **/
    public void Var061()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                String p = csTypes_.getString ("P_TIME");
                {
		    String expected = "08:42:30";
		    if (getDriver() == JDTestDriver.DRIVER_NATIVE) { /* native defaults to iso (not jis) format */ 
			expected = "08.42.30"; 
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
    getString() - Get a type that was registered as a TIMESTAMP.
    **/
    public void Var062()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                String p = csTypes_.getString ("P_TIMESTAMP");
		{
		    String expected = "2001-11-18 13:42:22.123456";
		    if (getDriver() == JDTestDriver.DRIVER_NATIVE) { /* native defaults IBM SQL TS  format */ 
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
    getString() - Get a type that was registered as an OTHER.
    **/
    public void Var063()
    {
        if(checkNamedParametersSupport())
        {
            if(checkLobSupport ())
            {
                try
                {
                    String p = csTypes_.getString ("P_DATALINK");
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
    getString() - Get a type that was registered as a BLOB.
    **/
    public void Var064()
    {
        if(checkNamedParametersSupport())
        {
            if(checkLobSupport ())
            {
                try
                {
		    String p = csTypes_.getString ("P_BLOB");

		    String expected = "446176652045676765";

		    if(getDriver() == JDTestDriver.DRIVER_NATIVE)  {
			expected = "Dave Egge"; 
		    } 
		    if(p.equals(expected))
			succeeded();
		    else
			failed(p + " != "+expected+" Updated 11/11/2011 for JDBC 4.1");

                }
                catch(Exception e)
                {
		    failed(e, "Unexpected Exception"+" Updated 11/11/2011 for JDBC 4.1");
                }
            }
        }
    }


    /**
    getString() - Get a type that was registered as a CLOB.
    
    SQL400 - The native driver allows this test to work.
    **/
    public void Var065()
    {
        if(checkNamedParametersSupport())
        {
            if(checkLobSupport ())
            {
                try
                {
                    String p = csTypes_.getString ("P_CLOB");
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
    getString() - Get a type that was registered as a BIGINT.
    **/
    public void Var066()
    {
        if(checkNamedParametersSupport())
        {
            if(checkBigintSupport())
            {
                try
                {
                    String p = csTypes_.getString ("P_BIGINT");
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
	if(checkJdbc30()) /* $E5 named parameters need jdbc 3.0 */
	{
	    if(true)
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

		    cstmt.setString("cOl1", "one two three four");
		    cstmt.execute();

		    String s = cstmt.getString(1);
		    assertCondition(s.equalsIgnoreCase("one two three four  "),"s = "+s);

		}
		catch(SQLException e)
		{
		    failed (e, "Unexpected Exception");
		}
	    }
	    
	}
    }

    /*
    This variation test the last named parameter with a case insensitive search
    */
    public void Var068()
    {
	if(checkJdbc30()) /* $E5 named parameters need jdbc 3.0 */
	{
	    if(true)
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

		    cstmt.setString("cOl3", "abc");
		    cstmt.execute();

		    String s = cstmt.getString(3);
		    assertCondition(s.equalsIgnoreCase("abc                 "),"s = "+s);
		}
		catch(SQLException e)
		{
		    failed (e, "Unexpected Exception");
		}
	    }
	    
	}
    }

    /*
    This variation test the second named parameter with a case insensitive search
    */
    public void Var069()
    {
	if(checkJdbc30()) /* $E5 named parameters need jdbc 3.0 */
	{
	    if(true)
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

		    cstmt.setString("cOl2", "lmnop");
		    cstmt.execute();

		    String s = cstmt.getString(2);
		    assertCondition(s.equalsIgnoreCase("lmnop               "));
		}
		catch(SQLException e)
		{
		    failed (e, "Unexpected Exception");
		}
	    }
	    
	}
    }

    /*
    This variation test the last and first named parameters with a case insensitive search
    */
    public void Var070()
    {
	if(checkJdbc30()) /* $E5 named parameters need jdbc 3.0 */
	{
	    if(true)
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

		    cstmt.setString("cOl3", "xyz");
		    cstmt.setString("cOl1", "XYZ");
		    cstmt.execute();

		    String s = cstmt.getString(1);
		    String t = cstmt.getString(3);
		    assertCondition(s.equalsIgnoreCase("xyz  ") && (t.equalsIgnoreCase("XYZ  ")));
		}
		catch(SQLException e)
		{
		    failed (e, "Unexpected Exception");
		}
	    }
	    
	}
    }
    /*
    This variation test the sencond and last named parameters with a case insensitive search
    */
    public void Var071()
    {
	if(checkJdbc30()) /* $E5 named parameters need jdbc 3.0 */
	{
	    if(true)
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

		    cstmt.setString("cOl2", "KJH");
		    cstmt.setString("cOl3", "kbj");
		    cstmt.execute();

		    String s = cstmt.getString(2);
		    String t = cstmt.getString(3);
		    assertCondition(s.equalsIgnoreCase("KJH  ") && (t.equalsIgnoreCase("kbj  ")));

		}
		catch(SQLException e)
		{
		    failed (e, "Unexpected Exception");
		}
	    }
	    
	}
    }
    /*
    This variation test the first and second named parameters with a case insensitive search
    */
    public void Var072()
    {
	if(checkJdbc30()) /* $E5 named parameters need jdbc 3.0 */
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

          cstmt.setString("col1", "ABC");
          cstmt.setString("COL2", "xyz");
          cstmt.execute();

          String s = cstmt.getString(1);
          String t = cstmt.getString(2);
          assertCondition(s.equalsIgnoreCase("ABC  ") && (t.equalsIgnoreCase("xyz  ")));
      }
      catch(SQLException e)
      {
          failed (e, "Unexpected Exception");
      }
	}
    }
    /*
    This variation tests the first named parameter basically with a case insensitive search
    should set the first string twice 
    */
    public void Var073()
    {
	if(checkJdbc30()) /* $E5 named parameters need jdbc 3.0 */
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

          cstmt.setString("\"One\"", "test One");
          cstmt.setString("\"ONe\"", "test ONe");
          cstmt.setString("\"ONE\"", "test ONE");
          cstmt.setString("one", "tester");
          cstmt.execute();

          String r = cstmt.getString("\"One\"");
          String s = cstmt.getString("\"ONe\"");
          String t = cstmt.getString("\"ONE\"");
          assertCondition((r.equalsIgnoreCase("tester  ")) && (s.equalsIgnoreCase("test ONe")) && (t.equalsIgnoreCase("test ONE")));
      }
      catch(SQLException e)
      {
          failed (e, "Unexpected Exception");
      }
	}
    }

    /*
    This variation test all named parameter with a case sensitive search
    */
    public void Var074()
    {
	if(checkJdbc30()) /* $E5 named parameters need jdbc 3.0 */
	{
	    if(true)
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

		    cstmt.setString("\"One\"", "test abc");
		    cstmt.setString("\"ONe\"", "test 123");
		    cstmt.setString("\"ONE\"", "test xyz");
		    cstmt.execute();

		    String s = cstmt.getString("\"One\"");
		    String t = cstmt.getString("\"ONe\"");
		    String r = cstmt.getString("\"ONE\"");
		    assertCondition((s.equalsIgnoreCase("test abc  ")) && (t.equalsIgnoreCase("test 123  ")) && (r.equalsIgnoreCase("test xyz  ")));
		}
		catch(SQLException e)
		{
		    failed (e, "Unexpected Exception");
		}
	    }
	    
	}
    }

    /*
    This variation tests all named parameter with a case sensitive search
    */
    public void Var075()
    {
	if(checkJdbc30()) /* $E5 named parameters need jdbc 3.0 */
	{
	    if(true)
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

		    cstmt.setString("\"ONE\"", "test xyz");
		    cstmt.setString("\"One\"", "test abc");
		    cstmt.setString("\"ONe\"", "test 123");
		    cstmt.execute();

		    String s = cstmt.getString("\"One\"");
		    String t = cstmt.getString("\"ONe\"");
		    String r = cstmt.getString("\"ONE\"");
		    assertCondition((s.equalsIgnoreCase("test abc  ")) && (t.equalsIgnoreCase("test 123  ")) && (r.equalsIgnoreCase("test xyz  ")));
		}
		catch(SQLException e)
		{
		    failed (e, "Unexpected Exception");
		}
	    }
	    
	}
    }

    /*
    This variation test all named parameter with a case sensitive search
    */
    public void Var076()
    {
	if(checkJdbc30()) /* $E5 named parameters need jdbc 3.0 */
	{
	    if(true)
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

		    cstmt.setString("\"ONe\"", "test 123");
		    cstmt.setString("\"One\"", "test abc");
		    cstmt.setString("\"ONE\"", "test xyz");
		    cstmt.execute();

		    String s = cstmt.getString("\"One\"");
		    String t = cstmt.getString("\"ONe\"");
		    String r = cstmt.getString("\"ONE\"");
		    assertCondition((s.equalsIgnoreCase("test abc  ")) && (t.equalsIgnoreCase("test 123  ")) && (r.equalsIgnoreCase("test xyz  ")));
		}
		catch(SQLException e)
		{
		    failed (e, "Unexpected Exception");
		}
	    }
	     
	}
    }
    /*
    This variation test a named parameter with a case sensitive search, should fail
    */
    public void Var077()
    {
	if(checkJdbc30()) /* $E5 named parameters need jdbc 3.0 */
	{
	    if(true)
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

		    cstmt.setString("\"one\"", "test abc");
		    cstmt.execute();

		    failed("Didn't throw SQLException ");
		}
		catch(SQLException e)
		{
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	     
	}
    }

    /*
    This variation tests a good named parameter, and then tries to find an invalid named parameter
     with a case sensitive search
    */
    public void Var078()
    {
	if(checkJdbc30()) /* $E5 named parameters need jdbc 3.0 */
	{
	    if(true)
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

		    cstmt.setString("\"ONe\"", "test abc");
		    cstmt.setString("\"one\"", "test 123");
		    cstmt.execute();

		    failed("Didn't throw SQLException ");
		}
		catch(SQLException e)
		{
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException"); 
		}
	    }
	     
	}
    }


    /**
    getString() - Get a type that was registered as a very large DBCLOB.

    SQL400 - The native driver allows this test to work.
    **/
    public void Var079()
    {
	String added = " -- added by native driver 5/12/2009 to detect native bug in J9 native method when working with large allocated lobs -- Fixed in V7R1";
	String sql = "";
	
	if (checkNative()) { 
	    if(checkLobSupport ())	    {
		try { 
                  Statement stmt =  connection_.createStatement(); 
		  try { 
                  sql = "DROP PROCEDURE "+JDCSTest.COLLECTION+".CSGSV79 ";
                  stmt.executeUpdate(sql); 
                  } catch (Exception e) { } 
                  
                  sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+".CSGSV79 (OUT STUFF CLOB(100M) CCSID 37) LANGUAGE SQL SPECIFIC CSGSV79 JCSGSV79: SET STUFF=CLOB('HELLO');  END JCSGSV79";  


		    stmt.executeUpdate(sql); 

		    sql = "call "+JDCSTest.COLLECTION+".CSGSV79 (?)"; 

		    CallableStatement cstmt = connection_.prepareCall(sql);
		    cstmt.registerOutParameter(1,Types.CLOB);
		    cstmt.execute();
		    String output = cstmt.getString(1);

                    sql = "DROP PROCEDURE "+JDCSTest.COLLECTION+".CSGSV79 ";
                    stmt.executeUpdate(sql); 

		    cstmt.close();
		    stmt.close();
		    assertCondition("HELLO".equals(output), "Expected HELLO but got '"+output+"' "+added); 




		} catch (Exception e) {
		    failed(e, "Unexpected exception sql="+sql+added); 
		} 

	    }
	}
    }


    /**
    getString() - Get a type that was registered as a very large DBCLOB.

    SQL400 - The native driver allows this test to work.
    **/
    public void Var080()
    {
        String added = " -- added by native driver 5/12/2009 to detect native bug in J9 native method when working with large allocated lobs";
        String sql = ""; 
        if (checkNative()) { 
            if(checkLobSupport ())          {
                try { 
                  Statement stmt =  connection_.createStatement(); 
                  try { 
                  sql = "DROP PROCEDURE "+JDCSTest.COLLECTION+".CSGSV80 ";
                  stmt.executeUpdate(sql); 
                  } catch (Exception e) { } 
                    sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+".CSGSV80 (OUT STUFF DBCLOB(100M) CCSID 1200) LANGUAGE SQL SPECIFIC CSGSV80 JCSGSV80: SET STUFF=CAST('HELLO' AS DBCLOB CCSID 1200);  END JCSGSV80";  


                    stmt.executeUpdate(sql); 

                    sql = "call "+JDCSTest.COLLECTION+".CSGSV80 (?)"; 

                    CallableStatement cstmt = connection_.prepareCall(sql);
                    cstmt.registerOutParameter(1,Types.CLOB);
                    cstmt.execute();
                    String output = cstmt.getString(1);

                    sql = "DROP PROCEDURE "+JDCSTest.COLLECTION+".CSGSV80 ";
                    stmt.executeUpdate(sql); 
                    
                    cstmt.close();
                    stmt.close();
                    assertCondition("HELLO".equals(output), "Expected HELLO but got '"+output+"' "+added); 




                } catch (Exception e) {
                    failed(e, "Unexpected exception sql="+sql+added); 
                } 

            }
        }
    }



    /* Get the various timestamp types.  Adopted from JDCSGetTimestamp */


    /**
     * Test get timestamp for different precision
     * The called procedure will have two parameters.
     * parameter1 is an integer an is an input parameter
     * parameter2 is the output timestamp
     */

  public void testTimestampPrecision(String procedureName,
      String procedureDefinition, String[] expectedValues) {
    if (checkTimestamp12Support()) {


	if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
	    // For native:  The timestamps are like "2001-11-18-13.42.22.123456";
            //                           instead of  2001-11-18 13:42:22.123456
            //                                       01234567890123
	    // Fix the format

	    for (int i = 0; i < expectedValues.length; i++) {
		char[] chars = expectedValues[i].toCharArray();
		if (chars.length > 10) {
		    if (chars[10]==' ') chars[10]='-';
		    if (chars.length > 13) { 
			if (chars[13] == ':') chars[13]='.';
			if (chars.length > 16) { 
			    if (chars[16] == ':') chars[16]='.';
			}
		    }
		}
		expectedValues[i] = new String(chars); 
	    } 
	} 
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
	      outputString = cstmt.getString(2);
	      if (outputString == null) {
		  outputString="null";
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

  public void Var081() {
    String[] expectedValues = {
    "2013-01-13 11:12:13.000000000000",
    "2013-02-13 11:12:13.100000000000",
    "2013-02-13 11:12:13.120000000000",
    "2013-02-13 11:12:13.123000000000",
    "2013-02-13 11:12:13.123400000000",
    "2013-02-13 11:12:13.123450000000",
    "2013-02-13 11:12:13.123456000000",
    "2013-02-13 11:12:13.123456700000",
    "2013-02-13 11:12:13.123456780000",
    "2013-02-13 11:12:13.123456789000",
    "2013-02-13 11:12:13.123456789100",
    "2013-02-13 11:12:13.123456789010",
    "2013-02-13 11:12:13.123456789012",
    "2013-02-13 11:12:13.100000000000",
    "2013-02-13 11:12:13.120000000000",
    "2013-02-13 11:12:13.123000000000",
    "2013-02-13 11:12:13.123400000000",
    "2013-02-13 11:12:13.123450000000",
    "2013-02-13 11:12:13.123456000000",
    "2013-02-13 11:12:13.123456700000",
    "2013-02-13 11:12:13.123456780000",
    "2013-02-13 11:12:13.123456789000",
    "2013-02-13 11:12:13.123456789100",
    "2013-02-13 11:12:13.123456789010",
    "2013-02-13 11:12:13.123456789000",
    "null"
    };
    
    testTimestampPrecision(collection_ + ".JDCSGS51", 
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


  public void Var082() {
    String[] expectedValues = {
    "2013-01-13 11:12:13.00000000000",
    "2013-02-13 11:12:13.10000000000",
    "2013-02-13 11:12:13.12000000000",
    "2013-02-13 11:12:13.12300000000",
    "2013-02-13 11:12:13.12340000000",
    "2013-02-13 11:12:13.12345000000",
    "2013-02-13 11:12:13.12345600000",
    "2013-02-13 11:12:13.12345670000",
    "2013-02-13 11:12:13.12345678000",
    "2013-02-13 11:12:13.12345678900",
    "2013-02-13 11:12:13.12345678910",
    "2013-02-13 11:12:13.12345678901",
    "2013-02-13 11:12:13.12345678901",
    "2013-02-13 11:12:13.10000000000",
    "2013-02-13 11:12:13.12000000000",
    "2013-02-13 11:12:13.12300000000",
    "2013-02-13 11:12:13.12340000000",
    "2013-02-13 11:12:13.12345000000",
    "2013-02-13 11:12:13.12345600000",
    "2013-02-13 11:12:13.12345670000",
    "2013-02-13 11:12:13.12345678000",
    "2013-02-13 11:12:13.12345678900",
    "2013-02-13 11:12:13.12345678910",
    "2013-02-13 11:12:13.12345678901",
    "2013-02-13 11:12:13.12345678900",
    "null",
    };
    
    testTimestampPrecision(collection_ + ".JDCSGS52", 
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


  public void Var083() {
    String[] expectedValues = {
    "2013-01-13 11:12:13.0000000000",
    "2013-02-13 11:12:13.1000000000",
    "2013-02-13 11:12:13.1200000000",
    "2013-02-13 11:12:13.1230000000",
    "2013-02-13 11:12:13.1234000000",
    "2013-02-13 11:12:13.1234500000",
    "2013-02-13 11:12:13.1234560000",
    "2013-02-13 11:12:13.1234567000",
    "2013-02-13 11:12:13.1234567800",
    "2013-02-13 11:12:13.1234567890",
    "2013-02-13 11:12:13.1234567891",
    "2013-02-13 11:12:13.1234567892",
    "2013-02-13 11:12:13.1234567893",
    "2013-02-13 11:12:13.1000000000",
    "2013-02-13 11:12:13.1200000000",
    "2013-02-13 11:12:13.1230000000",
    "2013-02-13 11:12:13.1234000000",
    "2013-02-13 11:12:13.1234500000",
    "2013-02-13 11:12:13.1234560000",
    "2013-02-13 11:12:13.1234567000",
    "2013-02-13 11:12:13.1234567800",
    "2013-02-13 11:12:13.1234567890",
    "2013-02-13 11:12:13.1234567891",
    "2013-02-13 11:12:13.1234567891",
    "2013-02-13 11:12:13.1234567890",
    "null",
    };
    
    testTimestampPrecision(collection_ + ".JDCSGS53", 
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


  public void Var084() {
    String[] expectedValues = {
    /* 0 */ "2013-01-13 11:12:13.000000000",
    /* 1 */ "2013-02-13 11:12:13.100000000",
    /* 2 */ "2013-02-13 11:12:13.120000000",
    /* 3 */ "2013-02-13 11:12:13.123000000",
    /* 4 */ "2013-02-13 11:12:13.123400000",
    /* 5 */ "2013-02-13 11:12:13.123450000",
    /* 6 */ "2013-02-13 11:12:13.123456000",
    /* 7 */ "2013-02-13 11:12:13.123456700",
    /* 8 */ "2013-02-13 11:12:13.123456780",
    /* 9 */ "2013-02-13 11:12:13.123456789",
    /*10 */ "2013-02-13 11:12:13.100000000",
    /*11 */ "2013-02-13 11:12:13.120000000",
    /*12 */ "2013-02-13 11:12:13.123000000",
    /*13 */ "2013-02-13 11:12:13.123400000",
    /*14 */ "2013-02-13 11:12:13.123450000",
    /*15 */ "2013-02-13 11:12:13.123456000",
    /*16 */ "2013-02-13 11:12:13.123456700",
    /*17 */ "2013-02-13 11:12:13.123456780",
    /*18 */ "2013-02-13 11:12:13.123456789",
    /*19 */ "2013-02-13 11:12:13.123456789",
    /*20 */ "null",
    };
    
    testTimestampPrecision(collection_ + ".JDCSGS54", 
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






  public void Var085() {
    String[] expectedValues = {
    /* 0 */ "2013-01-13 11:12:13.00000000",
    /* 1 */ "2013-02-13 11:12:13.10000000",
    /* 2 */ "2013-02-13 11:12:13.12000000",
    /* 3 */ "2013-02-13 11:12:13.12300000",
    /* 4 */ "2013-02-13 11:12:13.12340000",
    /* 5 */ "2013-02-13 11:12:13.12345000",
    /* 6 */ "2013-02-13 11:12:13.12345600",
    /* 7 */ "2013-02-13 11:12:13.12345670",
    /* 8 */ "2013-02-13 11:12:13.12345678",
    /* 9 */ "2013-02-13 11:12:13.10000000",
    /*10 */ "2013-02-13 11:12:13.12000000",
    /*11 */ "2013-02-13 11:12:13.12300000",
    /*12 */ "2013-02-13 11:12:13.12340000",
    /*13 */ "2013-02-13 11:12:13.12345000",
    /*14 */ "2013-02-13 11:12:13.12345600",
    /*15 */ "2013-02-13 11:12:13.12345670",
    /*16 */ "2013-02-13 11:12:13.12345678",
    /*17 */ "2013-02-13 11:12:13.12345678",
    /*18 */ "2013-02-13 11:12:13.12345678",
    /*19 */ "null",
    };
    
    testTimestampPrecision(collection_ + ".JDCSGS55", 
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



  public void Var086() {
    String[] expectedValues = {
    /* 0 */ "2013-01-13 11:12:13.0000000",
    /* 1 */ "2013-02-13 11:12:13.1000000",
    /* 2 */ "2013-02-13 11:12:13.1200000",
    /* 3 */ "2013-02-13 11:12:13.1230000",
    /* 4 */ "2013-02-13 11:12:13.1234000",
    /* 5 */ "2013-02-13 11:12:13.1234500",
    /* 6 */ "2013-02-13 11:12:13.1234560",
    /* 7 */ "2013-02-13 11:12:13.1234567",
    /* 8 */ "2013-02-13 11:12:13.1234567",
    /* 9 */ "2013-02-13 11:12:13.1000000",
    /*10 */ "2013-02-13 11:12:13.1200000",
    /*11 */ "2013-02-13 11:12:13.1230000",
    /*12 */ "2013-02-13 11:12:13.1234000",
    /*13 */ "2013-02-13 11:12:13.1234500",
    /*14 */ "2013-02-13 11:12:13.1234560",
    /*15 */ "2013-02-13 11:12:13.1234567",
    /*16 */ "2013-02-13 11:12:13.1234567",
    /*17 */ "2013-02-13 11:12:13.1234567",
    /*18 */ "2013-02-13 11:12:13.1234567",
    /*19 */ "null",
    };
    
    testTimestampPrecision(collection_ + ".JDCSGS56", 
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



  public void Var087() {
    String[] expectedValues = {
    /* 0 */ "2013-01-13 11:12:13.000000",
    /* 1 */ "2013-02-13 11:12:13.100000",
    /* 2 */ "2013-02-13 11:12:13.120000",
    /* 3 */ "2013-02-13 11:12:13.123000",
    /* 4 */ "2013-02-13 11:12:13.123400",
    /* 5 */ "2013-02-13 11:12:13.123450",
    /* 6 */ "2013-02-13 11:12:13.123456",
    /* 7 */ "2013-02-13 11:12:13.123456",
    /* 8 */ "2013-02-13 11:12:13.123456",
    /* 9 */ "2013-02-13 11:12:13.100000",
    /*10 */ "2013-02-13 11:12:13.120000",
    /*11 */ "2013-02-13 11:12:13.123000",
    /*12 */ "2013-02-13 11:12:13.123400",
    /*13 */ "2013-02-13 11:12:13.123450",
    /*14 */ "2013-02-13 11:12:13.123456",
    /*15 */ "2013-02-13 11:12:13.123456",
    /*16 */ "2013-02-13 11:12:13.123456",
    /*17 */ "2013-02-13 11:12:13.123456",
    /*18 */ "2013-02-13 11:12:13.123456",
    /*19 */ "null",
    };
    
    testTimestampPrecision(collection_ + ".JDCSGS57", 
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








  public void Var088() {
    String[] expectedValues = {
    /* 0 */ "2013-01-13 11:12:13.00000",
    /* 1 */ "2013-02-13 11:12:13.10000",
    /* 2 */ "2013-02-13 11:12:13.12000",
    /* 3 */ "2013-02-13 11:12:13.12300",
    /* 4 */ "2013-02-13 11:12:13.12340",
    /* 5 */ "2013-02-13 11:12:13.12345",
    /* 6 */ "2013-02-13 11:12:13.12345",
    /* 7 */ "2013-02-13 11:12:13.12345",
    /* 8 */ "2013-02-13 11:12:13.12345",
    /* 9 */ "2013-02-13 11:12:13.10000",
    /*10 */ "2013-02-13 11:12:13.12000",
    /*11 */ "2013-02-13 11:12:13.12300",
    /*12 */ "2013-02-13 11:12:13.12340",
    /*13 */ "2013-02-13 11:12:13.12345",
    /*14 */ "2013-02-13 11:12:13.12345",
    /*15 */ "2013-02-13 11:12:13.12345",
    /*16 */ "2013-02-13 11:12:13.12345",
    /*17 */ "2013-02-13 11:12:13.12345",
    /*18 */ "2013-02-13 11:12:13.12345",
    /*19 */ "null",
    };
    
    testTimestampPrecision(collection_ + ".JDCSGS58", 
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


  public void Var089() {
    String[] expectedValues = {
    /* 0 */ "2013-01-13 11:12:13.0000",
    /* 1 */ "2013-02-13 11:12:13.1000",
    /* 2 */ "2013-02-13 11:12:13.1200",
    /* 3 */ "2013-02-13 11:12:13.1230",
    /* 4 */ "2013-02-13 11:12:13.1234",
    /* 5 */ "2013-02-13 11:12:13.1234",
    /* 6 */ "2013-02-13 11:12:13.1234",
    /* 7 */ "2013-02-13 11:12:13.1234",
    /* 8 */ "2013-02-13 11:12:13.1234",
    /* 9 */ "2013-02-13 11:12:13.1000",
    /*10 */ "2013-02-13 11:12:13.1200",
    /*11 */ "2013-02-13 11:12:13.1230",
    /*12 */ "2013-02-13 11:12:13.1234",
    /*13 */ "2013-02-13 11:12:13.1234",
    /*14 */ "2013-02-13 11:12:13.1234",
    /*15 */ "2013-02-13 11:12:13.1234",
    /*16 */ "2013-02-13 11:12:13.1234",
    /*17 */ "2013-02-13 11:12:13.1234",
    /*18 */ "2013-02-13 11:12:13.1234",
    /*19 */ "null",
    };
    
    testTimestampPrecision(collection_ + ".JDCSGS59", 
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




  public void Var090() {
    String[] expectedValues = {
    /* 0 */ "2013-01-13 11:12:13.000",
    /* 1 */ "2013-02-13 11:12:13.100",
    /* 2 */ "2013-02-13 11:12:13.120",
    /* 3 */ "2013-02-13 11:12:13.123",
    /* 4 */ "2013-02-13 11:12:13.123",
    /* 5 */ "2013-02-13 11:12:13.123",
    /* 6 */ "2013-02-13 11:12:13.123",
    /* 7 */ "2013-02-13 11:12:13.123",
    /* 8 */ "2013-02-13 11:12:13.123",
    /* 9 */ "2013-02-13 11:12:13.100",
    /*10 */ "2013-02-13 11:12:13.120",
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
    
    testTimestampPrecision(collection_ + ".JDCSGS60", 
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




  public void Var091() {
    String[] expectedValues = {
    /* 0 */ "2013-01-13 11:12:13.00",
    /* 1 */ "2013-02-13 11:12:13.10",
    /* 2 */ "2013-02-13 11:12:13.12",
    /* 3 */ "2013-02-13 11:12:13.12",
    /* 4 */ "2013-02-13 11:12:13.12",
    /* 5 */ "2013-02-13 11:12:13.12",
    /* 6 */ "2013-02-13 11:12:13.12",
    /* 7 */ "2013-02-13 11:12:13.12",
    /* 8 */ "2013-02-13 11:12:13.12",
    /* 9 */ "2013-02-13 11:12:13.10",
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
    
    testTimestampPrecision(collection_ + ".JDCSGS61", 
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


  public void Var092() {
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
    
    testTimestampPrecision(collection_ + ".JDCSGS62", 
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


  public void Var093() {
    String[] expectedValues = {
    /* 0 */ "2013-01-13 11:12:13",
    /* 1 */ "2013-02-13 11:12:13",
    /* 2 */ "2013-02-13 11:12:13",
    /* 3 */ "2013-02-13 11:12:13",
    /* 4 */ "2013-02-13 11:12:13",
    /* 5 */ "2013-02-13 11:12:13",
    /* 6 */ "2013-02-13 11:12:13",
    /* 7 */ "2013-02-13 11:12:13",
    /* 8 */ "2013-02-13 11:12:13",
    /* 9 */ "2013-02-13 11:12:13",
    /*10 */ "2013-02-13 11:12:13",
    /*11 */ "2013-02-13 11:12:13",
    /*12 */ "2013-02-13 11:12:13",
    /*13 */ "2013-02-13 11:12:13",
    /*14 */ "2013-02-13 11:12:13",
    /*15 */ "2013-02-13 11:12:13",
    /*16 */ "2013-02-13 11:12:13",
    /*17 */ "2013-02-13 11:12:13",
    /*18 */ "2013-02-13 11:12:13",
    /*19 */ "null",
    };
    
    testTimestampPrecision(collection_ + ".JDCSGS63", 
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
   * getString() - Get a type that was registered as a BOOLEAN.
   **/
  public void Var094() {
    if (checkBooleanSupport()) {
      String info = " -- Test added 2021/01/06 for boolean";
      getTestSuccessful(csTypes_, "getString", 25, "1",info);
    }
  }


  /**
   * getString() - Get a type that was registered as a BOOLEAN that returns
   * false.
   **/
  public void Var095() {
    if (checkBooleanSupport()) { 
      String info = " -- Test added 2021/01/06 for boolean";
      getTestSuccessful(csTypesB_, "getString", 25, "0", info);
    }
  }
  /**
   * getString() - Get a type that was registered as a BOOLEAN that returns
   * false.
   **/
  public void Var096() {
    if (checkBooleanSupport()) { 
      String info = " -- Test added 2021/01/06 for boolean";
      getTestSuccessful(csTypesNull_, "getString", 25, "null", info);
    }
  }



    /**
    getString() - Get a clob CCSID 1208 value
    **/
  public void Var097()   {
      String added = " -- added 8/20/2021 to test ZDA back with LOB CCSID 1208 parameters";
	  String sql = ""; 
	  if(checkLobSupport ())          {
	      StringBuffer sb = new StringBuffer(added); 
	      try {
		  boolean passed = true; 
		  Statement stmt =  connection_.createStatement(); 
		  try { 
		      sql = "DROP PROCEDURE "+JDCSTest.COLLECTION+".CSGSV97 ";
		      stmt.executeUpdate(sql); 
		  } catch (Exception e) { } 
		  sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+".CSGSV97 ("
		      + "in datain varchar(32000) CCSID 1208, "
		      + "out dataout CLOB(1M) CCSID 1208) "
		      + "LANGUAGE SQL SPECIFIC CSGSV97 JCSGSV97: "
		      + "BEGIN SET dataout=datain;  END JCSGSV97";  

		  stmt.executeUpdate(sql);


		  sql = "call "+JDCSTest.COLLECTION+".CSGSV97 (?,?)"; 

		  CallableStatement cstmt = connection_.prepareCall(sql);
		  cstmt.registerOutParameter(2,Types.CLOB);

		  String[] testStrings =  {
		      "HELLO",
		      "HELLO WITH UX'20AC' ( \u201c )",
		      "HELLO WITH UX'01BB' ( \u01bb )",

		  };
		  for (int i = 0; i < testStrings.length; i++) { 
		      String input = testStrings[i]; 
		      cstmt.setString(1, input);
		      cstmt.execute();
		      String output = cstmt.getString(2);
		      if (!input.equals(output)) {
			  passed= false;
			  sb.append(" Round trip failed for "+JDTestUtilities.getMixedString(input)+" >= "+JDTestUtilities.getMixedString(output));
		      }

		  }
		  sql = "DROP PROCEDURE "+JDCSTest.COLLECTION+".CSGSV97 ";
		  stmt.executeUpdate(sql); 

		  cstmt.close();
		  stmt.close();

		  assertCondition(passed, sb); 




	      } catch (Exception e) {
		  failed(e, "Unexpected exception sql="+sql+sb); 
	      } 

	  }
		}

	    }


