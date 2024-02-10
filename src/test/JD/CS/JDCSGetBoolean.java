///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetBoolean.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetBoolean.java
//
// Classes:      JDCSGetBoolean
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import com.ibm.as400.access.AS400;

import test.JDSetupProcedure;

import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Hashtable;



/**
Testcase JDCSGetBoolean.  This tests the following
method of the JDBC CallableStatement class:

<ul>
<li>getBoolean()
</ul>
**/
public class JDCSGetBoolean
extends JDCSGetTestcase
{





/**
Constructor.
**/
    public JDCSGetBoolean (AS400 systemObject,
			   Hashtable namesAndVars,
			   int runMode,
			   FileOutputStream fileOutputStream,
			   
			   String password)
    {
	super (systemObject, "JDCSGetBoolean",
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
getBoolean() - Get parameter -1.
**/
    public void Var001()
    {
	try {
	    csTypes_.execute ();
	    boolean p = csTypes_.getBoolean (-1);
	    failed ("Didn't throw SQLException "+p );
	} catch (Exception e) {
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }



/**
getBoolean() - Get parameter 0.
**/
    public void Var002()
    {
	try {
	    boolean p = csTypes_.getBoolean (0);
	    failed ("Didn't throw SQLException "+p );
	} catch (Exception e) {
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }



/**
getBoolean() - Use a parameter that is too big.
**/
    public void Var003()
    {
	try {
	    boolean p = csTypes_.getBoolean (35);
	    failed ("Didn't throw SQLException "+p );
	} catch (Exception e) {
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }



/**
getBoolean() - Get a parameter when there are no parameters.
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
	    boolean p = cs.getBoolean (1);
	    failed ("Didn't throw SQLException "+p );
	} catch (Exception e) {
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	} 
	if (c != null) try { c.close(); } catch (Exception e) {} 
    }



/**
getBoolean() - Get a parameter that was not registered.
**/
    public void Var005()
    {
	try {
	    CallableStatement cs = JDSetupProcedure.prepare (connection_,
							     JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
	    cs.execute ();
	    boolean p = cs.getBoolean (1);
	    failed ("Didn't throw SQLException "+p );
	} catch (Exception e) {
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }



/**
getBoolean() - Get a parameter when the statement has not been
executed.
**/
    public void Var006()
    {
	try {
	    CallableStatement cs = JDSetupProcedure.prepare (connection_,
							     JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
	    cs.registerOutParameter (1, Types.SMALLINT);
	    boolean p = cs.getBoolean (1);
	    failed ("Didn't throw SQLException "+p );
	} catch (Exception e) {
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }



/**
getBoolean() - Get a parameter when the statement is closed.
**/
    public void Var007()
    {
	try {
	    CallableStatement cs = JDSetupProcedure.prepare (connection_,
							     JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
	    cs.registerOutParameter (1, Types.SMALLINT);
	    cs.execute ();
	    cs.close ();
	    boolean p = cs.getBoolean (1);
	    failed ("Didn't throw SQLException "+p );
	} catch (Exception e) {
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }



/**
getBoolean() - Get an IN parameter that was correctly registered.
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
	    boolean p = cs.getBoolean (1);
	    failed ("Didn't throw SQLException "+p );
	} catch (Exception e) {
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }


/**
getBoolean() - Get an INOUT parameter, where the OUT parameter is
longer than the IN parameter.
**/
    public void Var009()
    {
	try {
	    CallableStatement cs = JDSetupProcedure.prepare (connection_,
							     JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_);
	    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSTYPESINOUT,
				       supportedFeatures_, getDriver());
	    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESINOUT,
						 supportedFeatures_);
	    cs.registerOutParameter (1, Types.SMALLINT);
	    cs.execute ();
	    boolean p = cs.getBoolean (1);
	    assertCondition (p == true);
	} catch (Exception e)
	{
	    failed (e, "Unexpected Exception");
	}
    }


/**
getBoolean() - Get a type that was registered as a SMALLINT.
**/
    public void Var010()
    {
	try {
	    boolean p = csTypes_.getBoolean (1);
	    assertCondition (p == true);
	} catch (Exception e)
	{
	    failed (e, "Unexpected Exception");
	}
    }



/**
getBoolean() - Get a type that was registered as an INTEGER.
**/
    public void Var011()
    {

	String info = " -- call getBoolean against registered INTEGER -- Changed October 2011";

	// As of 10/3/2011 for the toolbox driver, it is now possible to get a big decimal
	// from a parameter registered as int.
	// Note:  JCC does not throw exception either. 
	// Native JDBC will now need to change. 

	try {
	    boolean p = csTypes_.getBoolean (2);
	    boolean expected = true;
	    assertCondition (p == expected, "Got "+p+" sb "+expected+info);

	} catch (Exception e) {

	    failed (e, "Unexpected Exception"+info);
	}
    }



/**
getBoolean() - Get a type that was registered as an REAL.
**/
    public void Var012()
    {
	    String info = " -- call getBoolean against registered REAL -- Changed October 2011";

	    try {
		boolean p = csTypes_.getBoolean (3);
		boolean expected = true;
		assertCondition (p == expected, "Got "+p+" sb "+expected+info);

	    } catch (Exception e) {

		failed (e, "Unexpected Exception"+info);
	    }

    }



/**
getBoolean() - Get a type that was registered as an FLOAT.
**/
    public void Var013()
    {
	    String info = " -- call getBoolean against registered FLOAT -- Changed October 2011";

	    try {
		boolean p = csTypes_.getBoolean (4);
		boolean expected = true;
		assertCondition (p == expected, "Got "+p+" sb "+expected+info);

	    } catch (Exception e) {

		failed (e, "Unexpected Exception"+info);
	    }

    }



/**
getBoolean() - Get a type that was registered as an DOUBLE.
**/
    public void Var014()
    {

	    String info = " -- call getBoolean against registered DOUBLE -- Changed October 2011";

	    try {
		boolean p = csTypes_.getBoolean (5);
		boolean expected = true;
		assertCondition (p == expected, "Got "+p+" sb "+expected+info);

	    } catch (Exception e) {

		failed (e, "Unexpected Exception"+info);
	    }

    }



/**
getBoolean() - Get a type that was registered as an DECIMAL.
**/
    public void Var015()
    {
	    String info = " -- call getBoolean against registered DECIMAL -- Changed October 2011";

	    try {
		boolean p = csTypes_.getBoolean (7);
		boolean expected = true;
		assertCondition (p == expected, "Got "+p+" sb "+expected+info);

	    } catch (Exception e) {

		failed (e, "Unexpected Exception"+info);
	    }
    }



/**
getBoolean() - Get a type that was registered as an NUMERIC.
**/
    public void Var016()
    {
	    String info = " -- call getBoolean against registered NUMERICL -- Changed October 2011";

	    try {
		boolean p = csTypes_.getBoolean (9);
		boolean expected = true;
		assertCondition (p == expected, "Got "+p+" sb "+expected+info);

	    } catch (Exception e) {

		failed (e, "Unexpected Exception"+info);
	    }

    }



/**
getBoolean() - Get a type that was registered as a CHAR.
**/
    public void Var017()
    {

	    String info = " -- call getBoolean against registered CHAR -- Changed October 2011";
	    String value = null; 
	    try {
		value = csTypes_.getString(11); 
		boolean p = csTypes_.getBoolean (11);
		boolean expected = true;
		assertCondition (p == expected, "Got "+p+" sb "+expected+info);

	    } catch (Exception e) {

		failed (e, "Unexpected Exception String is "+value+" "+info);
	    }




    }



/**
getBoolean() - Get a type that was registered as a VARCHAR.
**/
    public void Var018()
    {
	String value = null; 
	    String info = " -- call getBoolean against registered VARCHAR -- Changed October 2011";

	    try {
		value = csTypes_.getString(12); 
		boolean p = csTypes_.getBoolean (12);
		boolean expected = true;
		assertCondition (p == expected, "Got "+p+" sb "+expected+info);

	    } catch (Exception e) {

		failed (e, "Unexpected Exception value="+value+" "+info);
	    }

    }



/**
getBoolean() - Get a type that was registered as a BINARY.
**/
    public void Var019()
    {
	try {
	    boolean p = csTypes_.getBoolean (13);
	    failed ("Didn't throw SQLException "+p );
	} catch (Exception e) {
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }



/**
getBoolean() - Get a type that was registered as a VARBINARY.
**/
    public void Var020()
    {
	try {
	    boolean p = csTypes_.getBoolean (14);
	    failed ("Didn't throw SQLException "+p );
	} catch (Exception e) {
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }



/**
getBoolean() - Get a type that was registered as a DATE.
**/
    public void Var021()
    {
	try {
	    boolean p = csTypes_.getBoolean (15);
	    failed ("Didn't throw SQLException "+p );
	} catch (Exception e) {
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }



/**
getBoolean() - Get a type that was registered as a TIME.
**/
    public void Var022()
    {
	try {
	    boolean p = csTypes_.getBoolean (16);
	    failed ("Didn't throw SQLException "+p );
	} catch (Exception e) {
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }



/**
getBoolean() - Get a type that was registered as a TIMESTAMP.
**/
    public void Var023()
    {
	try {
	    boolean p = csTypes_.getBoolean (17);
	    failed ("Didn't throw SQLException "+p );
	} catch (Exception e) {
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
    }



/**
getBoolean() - Get a type that was registered as an OTHER -- Datalink varchar 200 .
**/
    public void Var024()
    {

	if (checkLobSupport ()) {
		String info = " -- call getBoolean against registered OTHER -- Changed October 2011";

		try {
		    boolean p = csTypes_.getBoolean (18);
		    boolean expected = true;
		    assertCondition (p == expected, "Got "+p+" sb "+expected+info);

		} catch (Exception e) {

		    failed (e, "Unexpected Exception"+info);
		}

	}

    }



/**
getBoolean() - Get a type that was registered as a BLOB.
**/
    public void Var025()
    {
	if (checkLobSupport ()) {
	    try {
		boolean p = csTypes_.getBoolean (19);
		failed ("Didn't throw SQLException "+p );
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



/**
getBoolean() - Get a type that was registered as a CLOB.
**/
    public void Var026()
    {
	if (checkLobSupport ()) {
	    try {
		boolean p = csTypes_.getBoolean (20);
		failed ("Didn't throw SQLException "+p );
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



/**
getBoolean() - Get a type that was registered as a BIGINT.
**/
    public void Var027()
    {

	if (checkBigintSupport()) {
		String info = " -- call getBoolean against registered BIGINT -- Changed October 2011";
		getTestSuccessful(csTypes_,"getBoolean", 22,"true",info);
	}
    }



/**
getBoolean() - Get an INOUT parameter, where the OUT parameter is
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
	    boolean p = cs.getBoolean (1);
	    assertCondition (p == true);
	} catch (Exception e)
	{
	    failed (e, "Unexpected Exception");
	}
    }
// NERYKAL named parameters
/**
getBoolean() - Get a type that was registered as a SMALLINT.
- Using a named parameter
**/
    public void Var029()
    {
	if(checkNamedParametersSupport())
	{
	    try {
		boolean p = csTypes_.getBoolean ("P_SMALLINT");
		assertCondition (p == true);
	    }
	    catch (Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}	

    }

/**
getBoolean() - Get a type that was registered as an INTEGER.
- Using a named parameter
**/
    public void Var030()
    {
	if(checkNamedParametersSupport())
	{
	    String info = " -- call getBoolean against registered INTEGET-- Changed October 2011";

	    try {
		boolean p = csTypes_.getBoolean ("P_INTEGER");
		boolean expected = true;
		assertCondition (p == expected, "Got "+p+" sb "+expected+info);

	    } catch (Exception e) {

		failed (e, "Unexpected Exception"+info);
	    }

	}
    }


/**
getBoolean() - Get a type that was registered as an REAL.
- Using a named parameter
**/
    public void Var031()
    {
	if(checkNamedParametersSupport())
	{
		String info = " -- call getBoolean against registered REAL -- Changed October 2011";

		try {
		    boolean p = csTypes_.getBoolean ("P_REAL");
		    boolean expected = true;
		    assertCondition (p == expected, "Got "+p+" sb "+expected+info);

		} catch (Exception e) {

		    failed (e, "Unexpected Exception"+info);
		}



	}
    }


/**
getBoolean() - Get a type that was registered as an FLOAT.
- Using a named parameter
**/
    public void Var032()
    {
	if(checkNamedParametersSupport())
	{
		String info = " -- call getBoolean against registered float -- Changed October 2011";

		try {
		    boolean p = csTypes_.getBoolean ("P_FLOAT");
		    boolean expected = true;
		    assertCondition (p == expected, "Got "+p+" sb "+expected+info);

		} catch (Exception e) {

		    failed (e, "Unexpected Exception"+info);
		}

	}
    }


/**
getBoolean() - Get a type that was registered as an DOUBLE.
- Using a named parameter
**/
    public void Var033()
    {
	if(checkNamedParametersSupport())
	{
		String info = " -- call getBoolean against registered DOUBLE -- Changed October 2011";

		try {
		    boolean p = csTypes_.getBoolean ("P_DOUBLE");
		    boolean expected = true;
		    assertCondition (p == expected, "Got "+p+" sb "+expected+info);

		} catch (Exception e) {

		    failed (e, "Unexpected Exception"+info);
		}


	}
    }


/**
getBoolean() - Get a type that was registered as an DECIMAL.
- Using a named parameter
**/
    public void Var034()
    {
	if(checkNamedParametersSupport())
	{
	    String info = " -- call getBoolean against registered DECIMAL -- Changed October 2011";
	    String value = null; 

	    try {
		value = csTypes_.getString("P_DECIMAL_50"); 
		boolean p = csTypes_.getBoolean ("P_DECIMAL_50");
		boolean expected = true;
		assertCondition (p == expected, "Got "+p+" sb "+expected+info);

	    } catch (Exception e) {

		failed (e, "Unexpected Exception value="+value+" "+info);
	    }


	}	
    }
/**
getBoolean() - Get a type that was registered as an DECIMAL.
- Using a named parameter
**/
    public void Var035()
    {
	if(checkNamedParametersSupport())
	{
		String info = " -- call getBoolean against registered DECIMAL -- Changed October 2011";
		String value = null; 
		try {
		    value = csTypes_.getString ("P_DECIMAL_105");
		    boolean p = csTypes_.getBoolean ("P_DECIMAL_105");
		    boolean expected = true;
		    assertCondition (p == expected, "Got "+p+" sb "+expected+" stringValue="+value+info);

		} catch (Exception e) {

		    failed (e, "Unexpected Exception"+info);
		}
		
	}

    }
/**
getBoolean() - Get a type that was registered as an NUMERIC.
- Using a named parameter
**/
    public void Var036()
    {
	if(checkNamedParametersSupport())
	{
		String info = " -- call getBoolean against registered NUMERIC -- Changed October 2011";

		try {
		    boolean p = csTypes_.getBoolean ("P_NUMERIC_50");
		    boolean expected = true;
		    assertCondition (p == expected, "Got "+p+" sb "+expected+info);

		} catch (Exception e) {

		    failed (e, "Unexpected Exception"+info);
		}

	}
    }
/**
getBoolean() - Get a type that was registered as an NUMERIC.
- Using a named parameter
**/
    public void Var037()
    {
	if(checkNamedParametersSupport())
	{
		String info = " -- call getBoolean against registered NUMERIC -- Changed October 2011";

		try {
		    boolean p = csTypes_.getBoolean ("P_NUMERIC_105");
		    boolean expected = true;
		    assertCondition (p == expected, "Got "+p+" sb "+expected+info);

		} catch (Exception e) {

		    failed (e, "Unexpected Exception"+info);
		}

	}
    }

/**
getBoolean() - Get a type that was registered as a CHAR.
- Using a named parameter
**/
    public void Var038()
    {
	if(checkNamedParametersSupport())
	{
		String info = " -- call getBoolean against registered CHAR -- Changed October 2011";
		String value = null; 
		try {
		    value = csTypes_.getString ("P_CHAR_1");
		    boolean p = csTypes_.getBoolean ("P_CHAR_1");
		    boolean expected = true;
		    assertCondition (p == expected, "Got "+p+" sb "+expected+info);

		} catch (Exception e) {

		    failed (e, "Unexpected Exception value="+value+" "+info);
		}
	    

	}
    }
/**
getBoolean() - Get a type that was registered as a CHAR.
- Using a named parameter
**/
    public void Var039()
    {
	if(checkNamedParametersSupport())
	{
		String info = " -- call getBoolean against registered CHAR -- Changed October 2011";

		try {
		    boolean p = csTypes_.getBoolean ("P_CHAR_50");
		    boolean expected = true;
		    assertCondition (p == expected, "Got "+p+" sb "+expected+info);

		} catch (Exception e) {

		    failed (e, "Unexpected Exception"+info);
		}

	}
    }

/**
getBoolean() - Get a type that was registered as a VARCHAR.
- Using a named parameter
**/
    public void Var040()
    {
	if(checkNamedParametersSupport())
	{
		String info = " -- call getBoolean against registered VARCHAR -- Changed October 2011";

		try {
		    boolean p = csTypes_.getBoolean ("P_VARCHAR_50");
		    boolean expected = true;
		    assertCondition (p == expected, "Got "+p+" sb "+expected+info);

		} catch (Exception e) {

		    failed (e, "Unexpected Exception"+info);
		}

	}
    }


/**
getBoolean() - Get a type that was registered as a BINARY.
- Using a named parameter
**/
    public void Var041()
    {
	if(checkNamedParametersSupport())
	{
	    try {
		boolean p = csTypes_.getBoolean ("P_BINARY_20");
		failed ("Didn't throw SQLException "+p );
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
getBoolean() - Get a type that was registered as a VARBINARY.
- Using a named parameter
**/
    public void Var042()
    {
	if(checkNamedParametersSupport())
	{
	    try {
		boolean p = csTypes_.getBoolean ("P_VARBINARY_20");
		failed ("Didn't throw SQLException "+p );
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }

/**
getBoolean() - Get a type that was registered as a DATE.
- Using a named parameter
**/
    public void Var043()
    {
	if(checkNamedParametersSupport())
	{
	    try {
		boolean p = csTypes_.getBoolean ("P_DATE");
		failed ("Didn't throw SQLException "+p );
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
getBoolean() - Get a type that was registered as a TIME.
- Using a named parameter
**/
    public void Var044()
    {
	if(checkNamedParametersSupport())
	{
	    try {
		boolean p = csTypes_.getBoolean ("P_TIME");
		failed ("Didn't throw SQLException "+p );
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
getBoolean() - Get a type that was registered as a TIMESTAMP.
- Using a named parameter
**/
    public void Var045()
    {
	if(checkNamedParametersSupport())
	{
	    try {
		boolean p = csTypes_.getBoolean ("P_TIMESTAMP");
		failed ("Didn't throw SQLException "+p );
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
getBoolean() - Get a type that was registered as an OTHER. DataLink
- Using a named parameter
**/
    public void Var046()
    {
	if(checkNamedParametersSupport())
	{
		String info = " -- call getBoolean against registered DATALINK -- Changed October 2011";

		try {
		    boolean p = csTypes_.getBoolean ("P_DATALINK");
		    boolean expected = true;
		    assertCondition (p == expected, "Got "+p+" sb "+expected+info);

		} catch (Exception e) {

		    failed (e, "Unexpected Exception"+info);
		}

	}
    }



/**
getBoolean() - Get a type that was registered as a BLOB.
- Using a named parameter
**/
    public void Var047()
    {
	if(checkNamedParametersSupport())
	{
	    try {
		boolean p = csTypes_.getBoolean ("P_BLOB");
		failed ("Didn't throw SQLException "+p );
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



/**
getBoolean() - Get a type that was registered as a CLOB.
- Using a named parameter
**/
    public void Var048()
    {
	if(checkNamedParametersSupport())
	{
	    try {
		boolean p = csTypes_.getBoolean ("P_CLOB");
		failed ("Didn't throw SQLException "+p );
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }

/**
getBoolean() - Get a type that was registered as a CLOB.
- Using a named parameter
**/
    public void Var049()
    {
	if(checkNamedParametersSupport())
	{
	    try {
		boolean p = csTypes_.getBoolean ("P_DBCLOB");
		failed ("Didn't throw SQLException "+p );
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



/**
getBoolean() - Get a type that was registered as a BIGINT.
- Using a named parameter
**/
    public void Var050()
    {
	if(checkNamedParametersSupport())
	{
	    String info = " -- call getBoolean against registered BIGINT -- Changed October 2011";
	    
	    try {
		boolean p = csTypes_.getBoolean ("P_BIGINT");
		boolean expected = true;
		assertCondition (p == expected, "Got "+p+" sb "+expected+info);

	    } catch (Exception e) {
		
		failed (e, "Unexpected Exception"+info);
	    }

	}
    }


   /**
   getBoolean() - Get a type that was registered as a DECFLOAT(16)
   **/
    public void Var051()
    {
	if(checkNamedParametersSupport()) { 

	    if (checkDecFloatSupport()) {
		    String info = " -- call getBoolean against registered DECFLOAT(16) -- Changed October 2011";

		    try {
			boolean p = csTypes_.getBoolean ("P_DECFLOAT16");
			boolean expected = true;
			assertCondition (p == expected, "Got "+p+" sb "+expected+info);

		    } catch (Exception e) {

			failed (e, "Unexpected Exception"+info);
		    }
	    }
	}
    }
      /**
      getBoolean() - Get a type that was registered as a DECFLOAT(34)
      **/
    public void Var052()
    {
	if(checkNamedParametersSupport()) {

	    if (checkDecFloatSupport()) {
		    String info = " -- call getBoolean against registered DECFLOAT -- Changed October 2011";

		    try {
			boolean p = csTypes_.getBoolean ("P_DECFLOAT34");
			boolean expected = true;
			assertCondition (p == expected, "Got "+p+" sb "+expected+info);

		    } catch (Exception e) {

			failed (e, "Unexpected Exception"+info);
		    }

	    }

	}
    }

 /**
   * getBoolean() - Get a type that was registered as a BOOLEAN.
   **/
  public void Var053() {
    if (checkBooleanSupport()) {
      String info = " -- Test added 2021/01/06 for boolean";
      getTestSuccessful(csTypes_, "getBoolean", 25, "true",info);
    }
  }


  /**
   * getBoolean() - Get a type that was registered as a BOOLEAN that returns
   * false.
   **/
  public void Var054() {
    if (checkBooleanSupport()) {
      String info = " -- Test added 2021/01/06 for boolean";
           getTestSuccessful(csTypesB_, "getBoolean", 25, "false",info);
    }
  }

  /**
   * getBoolean() - Get a type that was registered as a BOOLEAN that returns
   * null.
   **/
  public void Var055() {
    if (checkBooleanSupport()) {
      String info = " -- Test added 2021/01/06 for boolean";
           getTestSuccessful(csTypesNull_, "getBoolean", 25, "false",info);
    }
  }


}
