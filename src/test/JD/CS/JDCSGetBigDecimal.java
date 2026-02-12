///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetBigDecimal.java
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

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDSetupProcedure;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable; import java.util.Vector;



/**
Testcase JDCSGetBigDecimal.  This tests the following
method of the JDBC CallableStatement class:

<ul>
<li>getBigDecimal()
</ul>
**/
public class JDCSGetBigDecimal
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSGetBigDecimal";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCSTest.main(newArgs); 
   }
    // table names
    private static String INTEGER_TABLE = JDCSTest.COLLECTION + ".int_tabgbd";
    private static String INTEGER_PROCEDURE = JDCSTest.COLLECTION + ".int_procgbd";
    private static String VARCHAR_TABLE = JDCSTest.COLLECTION + ".vc_tabgbd";
    private static String VARCHAR_PROCEDURE = JDCSTest.COLLECTION + ".vc_procgbd";
    private static String NUMERIC_TABLE = JDCSTest.COLLECTION + ".num_tabgbd";
    private static String NUMERIC_PROCEDURE = JDCSTest.COLLECTION + ".num_procgbd";


    // Private data.
    private CallableStatement   csTypes_;
    private CallableStatement   csTypesB_;



    /**
    Constructor.
    **/
    public JDCSGetBigDecimal (AS400 systemObject,
                              Hashtable<String,Vector<String>> namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password)
    {
        super (systemObject, "JDCSGetBigDecimal",
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

	// reset table names after -lib arg is processed.
	INTEGER_TABLE = JDCSTest.COLLECTION + ".int_tabgbd";
	INTEGER_PROCEDURE = JDCSTest.COLLECTION + ".int_procgbd";
	VARCHAR_TABLE = JDCSTest.COLLECTION + ".vc_tabgbd";
	VARCHAR_PROCEDURE = JDCSTest.COLLECTION + ".vc_procgbd";
	NUMERIC_TABLE = JDCSTest.COLLECTION + ".num_tabgbd";
	NUMERIC_PROCEDURE = JDCSTest.COLLECTION + ".num_procgbd";



        connection_ = testDriver_.getConnection (baseURL_
                                                 + ";errors=full", userId_, encryptedPassword_);

        
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
    getBigDecimal() - Get parameter -1.
    **/
    public void Var001()
    {
        if(checkJdbc20 ())
        {
            try
            {
                csTypes_.execute ();
                BigDecimal p = csTypes_.getBigDecimal (-1);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    getBigDecimal() - Get parameter 0.
    **/
    public void Var002()
    {
        if(checkJdbc20 ())
        {
            try
            {
                BigDecimal p = csTypes_.getBigDecimal (0);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    getBigDecimal() - Use a parameter that is too big.
    **/
    public void Var003()
    {
        if(checkJdbc20 ())
        {
            try
            {
                BigDecimal p = csTypes_.getBigDecimal (35);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    getBigDecimal() - Get a parameter when there are no parameters.
    **/
    public void Var004()
    {
        if(checkJdbc20 ())
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
                BigDecimal p = cs.getBigDecimal (1);
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
    }



    /**
    getBigDecimal() - Get a parameter that was not registered.
    **/
    public void Var005()
    {
        if(checkJdbc20 ())
        {
            try
            {
                CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                                 JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
                cs.execute ();
                BigDecimal p = cs.getBigDecimal (7);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    getBigDecimal() - Get a parameter when the statement has not been
    executed.
    **/
    public void Var006()
    {
        if(checkJdbc20 ())
        {
            try
            {
                CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                                 JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
                cs.registerOutParameter (7, Types.DECIMAL);
                BigDecimal p = cs.getBigDecimal (7);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    getBigDecimal() - Get a parameter when the statement is closed.
    **/
    public void Var007()
    {
        if(checkJdbc20 ())
        {
            try
            {
                CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                                 JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
                cs.registerOutParameter (7, Types.DECIMAL);
                cs.execute ();
                cs.close ();
                BigDecimal p = cs.getBigDecimal (7);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    getBigDecimal() - Get an IN parameter that was correctly registered.
    **/
    public void Var008()
    {
        if(checkJdbc20 ())
        {
            try
            {
                CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                                 JDSetupProcedure.STP_CSTYPESIN, supportedFeatures_);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESIN,
                                                     supportedFeatures_);
                cs.registerOutParameter (7, Types.DECIMAL);
                cs.execute ();
                BigDecimal p = cs.getBigDecimal (7);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


    /**
    getBigDecimal() - Get an INOUT parameter, where the OUT parameter is
    longer than the IN parameter.
    **/
    public void Var009()
    {
        if(checkJdbc20 ())
        {
            try
            {
                CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                                 JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_);
		// Do the register before setting the parameters
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                           supportedFeatures_, getDriver());

                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                                     supportedFeatures_);
                cs.registerOutParameter (7, Types.DECIMAL);
                cs.execute ();
                BigDecimal p = cs.getBigDecimal (7);
                assertCondition (p.toString ().equals ("30777.77703"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }


    /**
    getBigDecimal() - Get a type that was registered as a SMALLINT.
    **/
    public void Var010()
    {
	String info = " -- call getBigDecimal against registered SMALLINT -- Changed October 2011";

	// As of 10/3/2011 for the toolbox driver, it is now possible to get a big decimal
	// from a parameter registered as int.
	// Note:  JCC does not throw exception either. 
	// Native JDBC will now need to change. 
        if(checkJdbc20 ())
        {
            try
            {
                BigDecimal p = csTypes_.getBigDecimal (1);
		String expected = "123";
                assertCondition (p.toString ().equals (expected), "Got "+p+" sb "+expected+info);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception"+info);
            }
        }
    }



    /**
    getBigDecimal() - Get a type that was registered as an INTEGER.
    **/
    public void Var011()
    {
        if(checkJdbc20 ())
        {

		String info = " -- call getBigDecimal against registered INTEGER -- Changed October 2011";

		try
		{
		    BigDecimal p = csTypes_.getBigDecimal (2);
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
    getBigDecimal() - Get a type that was registered as an REAL.
    **/
    public void Var012()
    {
	if(checkJdbc20 ())
	{

	    String info = " -- call getBigDecimal against registered REAL -- Changed October 2011";

	    try
	    {
		BigDecimal p = csTypes_.getBigDecimal (3);
		String expected = "789.54";
		assertCondition (p.toString ().equals (expected), "Got "+p+" sb "+expected+info);
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception"+info);
	    }



	}
    }



    /**
    getBigDecimal() - Get a type that was registered as an FLOAT.
    **/
    public void Var013()
    {
        if(checkJdbc20 ())
        {
	
		String info = " -- call getBigDecimal against registered FLOAT -- Changed October 2011";

		try
		{
		    BigDecimal p = csTypes_.getBigDecimal (4);
		    String expected = "253.1027";
		    assertCondition (p.toString ().equals (expected), "Got "+p+" sb "+expected+info);
		}
		catch(Exception e)
		{
		    failed (e, "Unexpected Exception"+info);
		}

	     

        }
    }



    /**
    getBigDecimal() - Get a type that was registered as an DOUBLE.
    **/
    public void Var014()
    {
        if(checkJdbc20 ())
        {
	{
		String info = " -- call getBigDecimal against registered DOUBLE -- Changed October 2011";

		try
		{
		    BigDecimal p = csTypes_.getBigDecimal (5);
		    String expected = "-987.3434";
		    assertCondition (p.toString ().equals (expected), "Got "+p+" sb "+expected+info);
		}
		catch(Exception e)
		{
		    failed (e, "Unexpected Exception"+info);
		}

	    } 

        }
    }



    /**
    getBigDecimal() - Get a type that was registered as an DECIMAL, when it is a DECIMAL(5,0).
    **/
    public void Var015()
    {
        if(checkJdbc20 ())
        {
            try
            {
                BigDecimal p = csTypes_.getBigDecimal (6);
                assertCondition (p.toString ().equals ("54362"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getBigDecimal() - Get a type that was registered as an DECIMAL, when it is a DECIMAL(10,5).
    **/
    public void Var016()
    {
        if(checkJdbc20 ())
        {
            try
            {
                BigDecimal p = csTypes_.getBigDecimal (7);
                assertCondition (p.toString ().equals ("-94732.12345"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getBigDecimal() - Get a type that was registered as an NUMERIC, when it is a NUMERIC(5,0).
    **/
    public void Var017()
    {
        if(checkJdbc20 ())
        {
            try
            {
                BigDecimal p = csTypes_.getBigDecimal (8);
                assertCondition (p.toString ().equals ("-1112"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getBigDecimal() - Get a type that was registered as an NUMERIC, when it is a NUMERIC(10,5).
    **/
    public void Var018()
    {
        if(checkJdbc20 ())
        {
            try
            {
                BigDecimal p = csTypes_.getBigDecimal (9);
                assertCondition (p.toString ().equals ("19.98765"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getBigDecimal() - Get a type that was registered as a CHAR(1).
    **/
    public void Var019()
    {
        if(checkJdbc20 ())
        {
            try
            {
                BigDecimal p = csTypes_.getBigDecimal (10);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    getBigDecimal() - Get a type that was registered as a CHAR(50).
    **/
    public void Var020()
    {
        if(checkJdbc20 ())
        {
            try
            {
                BigDecimal p = csTypes_.getBigDecimal (11);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    getBigDecimal() - Get a type that was registered as a VARCHAR(50).
    **/
    public void Var021()
    {
        if(checkJdbc20 ())
        {
            try
            {
                BigDecimal p = csTypes_.getBigDecimal (12);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    getBigDecimal() - Get a type that was registered as a BINARY.
    **/
    public void Var022()
    {
        if(checkJdbc20 ())
        {
            try
            {
                BigDecimal p = csTypes_.getBigDecimal (13);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    getBigDecimal() - Get a type that was registered as a VARBINARY.
    **/
    public void Var023()
    {
        if(checkJdbc20 ())
        {
            try
            {
                BigDecimal p = csTypes_.getBigDecimal (14);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    getBigDecimal() - Get a type that was registered as a DATE.
    **/
    public void Var024()
    {
        if(checkJdbc20 ())
        {
            try
            {
                BigDecimal p = csTypes_.getBigDecimal (15);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    getBigDecimal() - Get a type that was registered as a TIME.
    **/
    public void Var025()
    {
        if(checkJdbc20 ())
        {
            try
            {
                BigDecimal p = csTypes_.getBigDecimal (16);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    getBigDecimal() - Get a type that was registered as a TIMESTAMP.
    **/
    public void Var026()
    {
        if(checkJdbc20 ())
        {
            try
            {
                BigDecimal p = csTypes_.getBigDecimal (17);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    getBigDecimal() - Get a type that was registered as an OTHER. - Datalink (varchar (200))
    **/
    public void Var027()
    {
        if(checkJdbc20 ())
        {
            try
            {
                BigDecimal p = csTypes_.getBigDecimal (18);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    getBigDecimal() - Get a type that was registered as a BLOB.
    **/
    public void Var028()
    {
        if(checkJdbc20 ())
        {
            try
            {
                BigDecimal p = csTypes_.getBigDecimal (19);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    getBigDecimal() - Get a type that was registered as a CLOB.
    **/
    public void Var029()
    {
        if(checkJdbc20 ())
        {
            try
            {
                BigDecimal p = csTypes_.getBigDecimal (20);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }    


  public void getTestSuccessful(CallableStatement cstmt, int column,
      String expectedValue, String info) {
    try {
      BigDecimal p = cstmt.getBigDecimal(column);

      assertCondition(p.toString().equals(expectedValue),
          "Got " + p + " sb " + expectedValue + info);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

    /**
   * getBigDecimal() - Get a type that was registered as a BIGINT.
   **/
  public void Var030() {
    if (checkJdbc20()) {
      if (checkBigintSupport()) {
        String info = " -- call getBigDecimal against registered BIGINT -- Changed October 2011";
        getTestSuccessful(csTypes_, 22, "987662234567", info);
      }
    }
  }


    /**
    getBigDecimal() -- Get an output parameter where the database output type isn't necessarily a numeric
    */
    public void Var031()
    {
        if(checkJdbc20 ())
        {
            try
            {
                Statement statement = connection_.createStatement();
                statement.executeUpdate("DELETE FROM " + INTEGER_TABLE);
                statement.executeUpdate("INSERT INTO " + INTEGER_TABLE + " VALUES(32000,0,null)");
                statement.close();

                CallableStatement cstmt = connection_.prepareCall("{CALL " + INTEGER_PROCEDURE + "(?,?,?)}");

                //register the output parameters
                cstmt.registerOutParameter(1,java.sql.Types.DECIMAL);
                cstmt.registerOutParameter(2,java.sql.Types.DECIMAL);
                cstmt.registerOutParameter(3,java.sql.Types.DECIMAL);

                //execute the procedure
                cstmt.execute();

                //invoke getByte method
                BigDecimal xRetVal = cstmt.getBigDecimal(1);
                BigDecimal yRetVal = cstmt.getBigDecimal(2);
                BigDecimal zRetVal = cstmt.getBigDecimal(3);
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
    }

    /**
    getBigDecimal() -- Get an output parameter where the database output type isn't necessarily a numerc/ etc 
    */ 
    public void Var032()
    {
        if(checkJdbc20 ())
        {
            try
            {
                Statement statement = connection_.createStatement();
                statement.executeUpdate("DELETE FROM " + VARCHAR_TABLE);
                statement.executeUpdate("INSERT INTO " + VARCHAR_TABLE + " VALUES('32000','0',null)");
                statement.close();

                CallableStatement cstmt = connection_.prepareCall("{CALL " + VARCHAR_PROCEDURE + "(?,?,?)}");

                //register the output parameters
                cstmt.registerOutParameter(1,java.sql.Types.DECIMAL);
                cstmt.registerOutParameter(2,java.sql.Types.DECIMAL);
                cstmt.registerOutParameter(3,java.sql.Types.DECIMAL);

                //execute the procedure
                cstmt.execute();

                //invoke getBigDecimal method
                BigDecimal xRetVal = cstmt.getBigDecimal(1);
                BigDecimal yRetVal = cstmt.getBigDecimal(2);
                BigDecimal zRetVal = cstmt.getBigDecimal(3);
                cstmt.close();
                assertCondition(xRetVal.intValue() == 32000 && yRetVal.intValue() == 0 && zRetVal == null); 

            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getBigDecimal() -- Get an output parameter where the database output type isn't necessarily a short, but value cannot be mapped 
    */
    public void Var033()
    {
        if(checkJdbc20 ())
        {
            try
            {
                Statement statement = connection_.createStatement();
                statement.executeUpdate("DELETE FROM " + VARCHAR_TABLE);
                statement.executeUpdate("INSERT INTO " + VARCHAR_TABLE + " VALUES('ISMALL','0',null)");
                statement.close();

                CallableStatement cstmt = connection_.prepareCall("{CALL " + VARCHAR_PROCEDURE + "(?,?,?)}");

                //register the output parameters
                cstmt.registerOutParameter(1,java.sql.Types.DECIMAL);
                cstmt.registerOutParameter(2,java.sql.Types.DECIMAL);
                cstmt.registerOutParameter(3,java.sql.Types.DECIMAL);

                //execute the procedure
                cstmt.execute();

                //invoke getBigDecimal method
                BigDecimal xRetVal = cstmt.getBigDecimal(1);
                BigDecimal yRetVal = cstmt.getBigDecimal(2);
                BigDecimal zRetVal = cstmt.getBigDecimal(3);
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
    }



    /**
    getBigDecimal() -- Get an output parameter where the database output type isn't necessarily a numeric
    */
    public void Var034()
    {
        if(checkJdbc20 ())
        {
            try
            {
                Statement statement = connection_.createStatement();
                statement.executeUpdate("DELETE FROM " + INTEGER_TABLE);
                statement.executeUpdate("INSERT INTO " + INTEGER_TABLE + " VALUES(32000,0,null)");
                statement.close();

                CallableStatement cstmt = connection_.prepareCall("{CALL " + INTEGER_PROCEDURE + "(?,?,?)}");

                //register the output parameters
                cstmt.registerOutParameter(1,java.sql.Types.NUMERIC);
                cstmt.registerOutParameter(2,java.sql.Types.NUMERIC);
                cstmt.registerOutParameter(3,java.sql.Types.NUMERIC);

                //execute the procedure
                cstmt.execute();

                //invoke getByte method
                BigDecimal xRetVal = cstmt.getBigDecimal(1);
                BigDecimal yRetVal = cstmt.getBigDecimal(2);
                BigDecimal zRetVal = cstmt.getBigDecimal(3);
                cstmt.close();

                assertCondition(xRetVal.intValue() == 32000 && yRetVal.intValue() == 0 && zRetVal == null); 

            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getBigDecimal() -- Get an output parameter where the database output type isn't necessarily a numerc/ etc 
    */ 
    public void Var035()
    {
        if(checkJdbc20 ())
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
                BigDecimal xRetVal = cstmt.getBigDecimal(1);
                BigDecimal yRetVal = cstmt.getBigDecimal(2);
                BigDecimal zRetVal = cstmt.getBigDecimal(3);
                cstmt.close();

                assertCondition(xRetVal.intValue() == 32000 && yRetVal.intValue() == 0 && zRetVal == null); 

            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getBigDecimal() -- Get an output parameter where the database output type isn't necessarily a short, but value cannot be mapped 
    */
    public void Var036()
    {
        if(checkJdbc20 ())
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
                BigDecimal xRetVal = cstmt.getBigDecimal(1);
                BigDecimal yRetVal = cstmt.getBigDecimal(2);
                BigDecimal zRetVal = cstmt.getBigDecimal(3);
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
    }


    /**
    getBigDecimal() -- Make sure null can be retrieved from typical non-null types
    */
    public void Var037()
    {
        if(checkJdbc20 ())
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
                BigDecimal aRetVal = cstmt.getBigDecimal(1);
                BigDecimal bRetVal = cstmt.getBigDecimal(2);
                BigDecimal cRetVal = cstmt.getBigDecimal(3);
                BigDecimal dRetVal = cstmt.getBigDecimal(4);
                BigDecimal eRetVal = cstmt.getBigDecimal(5);
                BigDecimal fRetVal = cstmt.getBigDecimal(6);
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
    }


    //NERYKAL

    /**
    getBigDecimal() - Get a type that was registered as a SMALLINT. 
    - Using a named parameter
    **/
    public void Var038()
    {
	if(checkNamedParametersSupport())
	{
	{
		String info = " -- call getBigDecimal against registered SMALLINT -- Changed October 2011";

		try
		{
		    BigDecimal p = csTypes_.getBigDecimal ("P_SMALLINT");
		    String expected = "123";
		    assertCondition (p.toString ().equals (expected), "Got "+p+" sb "+expected+info);
		}
		catch(Exception e)
		{
		    failed (e, "Unexpected Exception"+info);
		}
	    } 

        }
    }


    /**
    getBigDecimal() - Get a type that was registered as an INTEGER.
    - Using a named parameter
    **/
    public void Var039()
    {
        if(checkNamedParametersSupport())
        {
	{
		String info = " -- call getBigDecimal against registered INTEGER - Changed October 2011";

		try
		{
		    BigDecimal p = csTypes_.getBigDecimal ("P_INTEGER");
		    String expected = "-456";
		    assertCondition (p.toString ().equals (expected), "Got "+p+" sb "+expected+info);
		}
		catch(Exception e)
		{
		    failed (e, "Unexpected Exception"+info);
		}
	    } 

        }
    }



    /**
    getBigDecimal() - Get a type that was registered as an REAL.
    - Using a named parameter
    **/
    public void Var040()
    {
        if(checkNamedParametersSupport())
        {
	{
		String info = " -- call getBigDecimal against registered REAL -- Changed October 2011";

		try
		{
		    BigDecimal p = csTypes_.getBigDecimal ("P_REAL");
		    String expected = "789.54";
		    assertCondition (p.toString ().equals (expected), "Got "+p+" sb "+expected+info);
		}
		catch(Exception e)
		{
		    failed (e, "Unexpected Exception"+info);
		}
	    } 

        }
    }



    /**
    getBigDecimal() - Get a type that was registered as an FLOAT.
    - Using a named parameter
    **/
    public void Var041()
    {
        if(checkNamedParametersSupport())
        {
	{
		String info = " -- call getBigDecimal against registered FLOAT -- Changed October 2011";

		try
		{
		    BigDecimal p = csTypes_.getBigDecimal ("P_FLOAT");
		    String expected = "253.1027";
		    assertCondition (p.toString ().equals (expected), "Got "+p+" sb "+expected+info);
		}
		catch(Exception e)
		{
		    failed (e, "Unexpected Exception"+info);
		}
	    } 

        }
    }



    /**
    getBigDecimal() - Get a type that was registered as an DOUBLE.
    - Using a named parameter
    **/
    public void Var042()
    {
        if(checkNamedParametersSupport())
        {
 {
		String info = " -- call getBigDecimal against registered DOUBLE -- Changed October 2011";

		try
		{
		    BigDecimal p = csTypes_.getBigDecimal ("P_DOUBLE");
		    String expected = "-987.3434";
		    assertCondition (p.toString ().equals (expected), "Got "+p+" sb "+expected+info);
		}
		catch(Exception e)
		{
		    failed (e, "Unexpected Exception"+info);
		}
	    } 

        }
    }



    /**
    getBigDecimal() - Get a type that was registered as an DECIMAL, when it is a DECIMAL(5,0).
    - Using a named parameter
    **/
    public void Var043()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                BigDecimal p = csTypes_.getBigDecimal ("P_DECIMAL_50");
                assertCondition (p.toString ().equals ("54362"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getBigDecimal() - Get a type that was registered as an DECIMAL, when it is a DECIMAL(10,5).
    - Using a named parameter
    **/
    public void Var044()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                BigDecimal p = csTypes_.getBigDecimal ("P_DECIMAL_105");
                assertCondition (p.toString ().equals ("-94732.12345"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getBigDecimal() - Get a type that was registered as an NUMERIC, when it is a NUMERIC(5,0).
    - Using a named parameter
    **/
    public void Var045()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                BigDecimal p = csTypes_.getBigDecimal ("P_NUMERIC_50");
                assertCondition (p.toString ().equals ("-1112"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getBigDecimal() - Get a type that was registered as an NUMERIC, when it is a NUMERIC(10,5).
    - Using a named parameter
    **/
    public void Var046()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                BigDecimal p = csTypes_.getBigDecimal ("P_NUMERIC_105");
                assertCondition (p.toString ().equals ("19.98765"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getBigDecimal() - Get a type that was registered as a CHAR(1).
    - Using a named parameter
    **/
    public void Var047()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                BigDecimal p = csTypes_.getBigDecimal ("P_CHAR_1");
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    getBigDecimal() - Get a type that was registered as a CHAR(50).
    - Using a named parameter
    **/
    public void Var048()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                BigDecimal p = csTypes_.getBigDecimal ("P_CHAR_50");
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    getBigDecimal() - Get a type that was registered as a VARCHAR(50).
    - Using a named parameter
    **/
    public void Var049()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                BigDecimal p = csTypes_.getBigDecimal ("P_VARCHAR_50");
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    getBigDecimal() - Get a type that was registered as a BINARY.
    - Using a named parameter
    **/
    public void Var050()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                BigDecimal p = csTypes_.getBigDecimal ("P_BINARY_20");
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    getBigDecimal() - Get a type that was registered as a VARBINARY.
    - Using a named parameter
    **/
    public void Var051()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                BigDecimal p = csTypes_.getBigDecimal ("P_VARBINARY_20");
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    getBigDecimal() - Get a type that was registered as a DATE.
    - Using a named parameter
    **/
    public void Var052()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                BigDecimal p = csTypes_.getBigDecimal ("P_DATE");
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    getBigDecimal() - Get a type that was registered as a TIME.
    - Using a named parameter
    **/
    public void Var053()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                BigDecimal p = csTypes_.getBigDecimal ("P_TIME");
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    getBigDecimal() - Get a type that was registered as a TIMESTAMP.
    - Using a named parameter
    **/
    public void Var054()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                BigDecimal p = csTypes_.getBigDecimal ("P_TIMESTAMP");
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    getBigDecimal() - Get a type that was registered as an OTHER.
    - Using a named parameter
    **/
    public void Var055()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                BigDecimal p = csTypes_.getBigDecimal ("P_DATALINK");
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    getBigDecimal() - Get a type that was registered as a BLOB.
    - Using a named parameter
    **/
    public void Var056()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                BigDecimal p = csTypes_.getBigDecimal ("P_BLOB");
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    getBigDecimal() - Get a type that was registered as a CLOB.
    - Using a named parameter
    **/
    public void Var057()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                BigDecimal p = csTypes_.getBigDecimal ("P_CLOB");
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }    



    /**
    getBigDecimal() - Get a type that was registered as a BIGINT.
    - Using a named parameter
    **/
    public void Var058()
    {
        if(checkNamedParametersSupport())
        {
            if(checkBigintSupport())
            {
	    {
		    String info = " -- call getBigDecimal against registered BIGINT -- Changed October 2011";

		    try
		    {
			BigDecimal p = csTypes_.getBigDecimal ("P_BIGINT");
			String expected = "987662234567";
			assertCondition (p.toString ().equals (expected), "Got "+p+" sb "+expected+info);
		    }
		    catch(Exception e)
		    {
			failed (e, "Unexpected Exception"+info);
		    }
		} 

            }
        }
    }
    
    
    /**
    getBigDecimal() - Get a type that was registered as an DECFLOAT16
    - Using a named parameter
    **/
    public void Var059()
    {
        if(checkNamedParametersSupport())
	{

	    if(checkDecFloatSupport())
	    {
		try
		{
		    String expected = "1234567890123456"; 
		    BigDecimal p = null;
		    if (isToolboxDriver()){
		    //output registered as varchar now
			String pStr = csTypes_.getString ("P_DECFLOAT16");
			p = new BigDecimal(pStr);
		    }else{
			p = csTypes_.getBigDecimal ("P_DECFLOAT16");
		    }
		    assertCondition (p.toString ().equals (expected), "expected "+expected+" got "+p.toString());
		}
		catch(Exception e)
		{
		    failed (e, "Unexpected Exception");
		}
	    }
	}
    }

    /**
    getBigDecimal() - Get a type that was registered as an DECFLOAT34
    - Using a named parameter
    **/
    public void Var060()
    {
        if(checkNamedParametersSupport())
	{

	    if(checkDecFloatSupport())
	    {
		try
		{
		    String expected = "1234567890123456789012345678901234"; 
		    BigDecimal p = null;
		    if (isToolboxDriver()){
		    //output registered as varchar now
			String pStr = csTypes_.getString ("P_DECFLOAT34");
			p = new BigDecimal(pStr);
		    }else{
			p = csTypes_.getBigDecimal ("P_DECFLOAT34");
		    }

		    assertCondition (p.toString ().equals (expected), "expected "+expected+" got "+p.toString());
		}
		catch(Exception e)
		{
		    failed (e, "Unexpected Exception");
		}
	    }
	}
    }


    /**
     * getString() - Get a type that was registered as a BOOLEAN.
     **/
    public void Var061() {
      if (checkBooleanSupport()) {
        String info = " -- Tested added 2021/01/06 ";
        getTestSuccessful(csTypes_, 25, "1", info);
      }
    }


    /**
     * getString() - Get a type that was registered as a BOOLEAN that returns
     * false.
     **/
    public void Var062() {
      if (checkBooleanSupport()) {
        String info = " -- Tested added 2021/01/06 ";
        getTestSuccessful(csTypesB_, 25, "0", info);
      }
    }


    
}
