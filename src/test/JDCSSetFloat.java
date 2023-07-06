///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSSetFloat.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSSetFloat.java
//
// Classes:      JDCSSetFloat
//
////////////////////////////////////////////////////////////////////////

package test;

import java.awt.TextArea;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.DataTruncation;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;



/**
Testcase JDCSSetFloat.  This tests the following method
of the JDBC CallableStatement class:

<ul>
<li>setFloat()
</ul>
**/
public class JDCSSetFloat
extends JDCSSetTestcase
{



 


/**
Constructor.
**/
    public JDCSSetFloat (AS400 systemObject,
                         Hashtable namesAndVars,
                         int runMode,
                         FileOutputStream fileOutputStream,
                         
                         String password)
    {
        super (systemObject, "JDCSSetFloat",
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
setFloat() - Should throw exception when the prepared
statement is closed.
- Using an ordinal parameter
**/
    public void Var001()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.close ();
                cs.setFloat (3, 533.45f);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setFloat() - Should throw exception when an invalid index is
specified.
- Using an ordinal parameter
**/
    public void Var002()
    {
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setFloat (100, 334.45f);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


/**
setFloat() - Should work with a valid parameter index
greater than 1.
- Using an ordinal parameter
**/
    public void Var003()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setFloat (3, -43423.456f);
                cs.execute();
                float check = cs.getFloat (3);
                assertCondition (check == -43423.456f);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setFloat() - Should throw exception when the parameter is
not an input parameter.
- Using an ordinal parameter
**/
    public void Var004()
    {
        if(checkJdbc20())
        {
            try
            {
                CallableStatement cs1 = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + "( ?, ?, ?)");
                cs1.setFloat (2, -43423.456f);            
                cs1.close ();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setFloat() - Set a SMALLINT parameter.
- Using an ordinal parameter
**/
    public void Var005()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setFloat (1, 4269f);
                cs.execute();
                short check = cs.getShort (1);
                assertCondition (check == 4269);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setFloat() - Set a SMALLINT parameter, when the value is too big.  This will
cause a data truncation exception.
-Using an ordinal parameter
**/
    public void Var006()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setFloat (1, -70000.0f);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
               
              assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation in V7R1");
              

            }
        }
    }

    
    /**
    setFloat() - Set a SMALLINT parameter, when there is a decimal part.
    -Using an ordinal parameter
    **/
        public void Var007()
        {
            if(checkJdbc20())
            {
                try
                {
                    cs.setFloat (1, 212.243f);
                    cs.execute ();
                    short check = cs.getShort (1);
                    assertCondition (check == 212);
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }


/**
setFloat() - Set an INTEGER parameter.
- Using an ordinal parameter
**/
    public void Var008()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setFloat (2, 30679f);
                cs.execute();
                int check = cs.getInt (2);
                assertCondition (check == 30679);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setFloat() - Set an INTEGER parameter, when there is a decimal part.
- Using an ordinal parameter
**/
    public void Var009()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setFloat (2, 306.8976f);
                cs.execute ();
                int check = cs.getInt (2);
                assertCondition (check == 306);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setFloat() - Set an INTEGER parameter, when the value is too big.  This will
cause a data truncation exception.
- Using an ordinal parameter
**/
    public void Var010()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setFloat (2, 21474836448.0f);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
		  assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation in V7R1");

            }
        }
    }

/**
setFloat() - Set an REAL parameter.
- Using an ordinal parameter
**/
    public void Var011()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setFloat (3, -1792.249f);
                cs.execute();
                float check = cs.getFloat (3);
                assertCondition (check == -1792.249f);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setFloat() - Set an FLOAT parameter.
- Using an ordinal parameter
**/
    public void Var012()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setFloat (4, 0.1243f);
            //inserted in with more digits 
                cs.execute();
                double check = cs.getDouble (4);
                assertCondition (check ==  0.12430000305175781);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setFloat() - Set an DOUBLE parameter.
- Using an ordinal parameter
**/
    public void Var013()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setFloat (5, 122.290f);
            //inserted in with more digits
                cs.execute();
                double check = cs.getDouble (5);
                assertCondition (check == 122.29000091552734);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setFloat() - Set an DECIMAL parameter.
- Using an ordinal parameter
**/
    public void Var014()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setFloat (7, 4.0012f);
                cs.execute();
                BigDecimal check = cs.getBigDecimal (7);
                assertCondition (check.floatValue() == 4.0012f);

            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setFloat() - Set an DECIMAL parameter, when the value is too big.
- Using an ordinal parameter
**/
    public void Var015()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setFloat (6, 103212.2134f);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
	    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
		assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation now in toolbox");

	    } else { 

		assertExceptionIsInstanceOf (e, "java.sql.DataTruncation");
	    }
            }
        }
    }

/**
setFloat() - Set a DECIMAL parameter, where the float's fraction truncates.
This does not cause a DataTruncation object to be created.
- Using an ordinal parameter
**/
    public void Var016()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setFloat (6, -99999.2134f);
            //value is inserted as a whole number (-99999)
                cs.execute();
                BigDecimal check = cs.getBigDecimal (6);
                assertCondition (check.floatValue() == -99999f);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setFloat() - Set an DECIMAL parameter, when the value is very small.
- Using an ordinal parameter
**/
    public void Var017()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setFloat (7, 0.00007f);
                cs.execute();
                BigDecimal check = cs.getBigDecimal (7);
                assertCondition (check.floatValue() == 0.00007f);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setFloat() - Set an NUMERIC parameter.
- Using an ordinal parameter
**/
    public void Var018()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setFloat (9, -277.7919f);
                cs.execute();
                BigDecimal check = cs.getBigDecimal (9);
                assertCondition (check.floatValue() == -277.7919f);

            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setFloat() - Set an NUMERIC parameter, when the value is too big
- Using an ordinal parameter
**/
    public void Var019()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setFloat (8, -24345542334.4f);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
	    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
		assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation now in toolbox");

	    } else { 

		DataTruncation dt = (DataTruncation)e;
		assertCondition ((dt.getIndex() == 8)                                         
				 && (dt.getParameter() == true)
				 && (dt.getRead() == false)
				 && (dt.getTransferSize() == 5));
	    }
            }
        }
    }

/**
setFloat() - Set a NUMERIC parameter, where the float's fraction truncates.
This does not cause a DataTruncation object to be created.
- Using an ordinal parameter
**/
    public void Var020()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setFloat (8, 99999.2134f);
            //value is inserted as a whole number (99999)
                cs.execute();
                BigDecimal check = cs.getBigDecimal (8);
                assertCondition (check.intValue() == 99999);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setFloat() - Set an NUMERIC parameter, when the value is very small.
- Using an ordinal parameter
**/
    public void Var021()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setFloat (9, -0.00003f);
                cs.execute();
                BigDecimal check = cs.getBigDecimal (9);
                assertCondition (check.floatValue() == -0.00003f);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
setFloat() - Set an CHAR(50) parameter.
- Using an ordinal parameter
**/
    public void Var022()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setFloat (11, -1842.799f);
                cs.execute();
                String check = cs.getString (11);
                assertCondition (check.equals("-1842.799                                         "));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setFloat() - Set an CHAR(1) parameter.
- Using an ordinal parameter
**/
    public void Var023()
    {
        if(checkJdbc20())
        {
            try
            {
            // It seems that any float translates into bigger than
            // 1 character, so for a CHAR(1), we will always get a 
            // DataTruncation.
                cs.setFloat (10, 9f);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.DataTruncation");
            }
        }
    }



/**
setFloat() - Set an VARCHAR parameter.
- Using an ordinal parameter
**/
    public void Var024()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setFloat (12, 0.87667f);
                cs.execute();
                String check = cs.getString (12);
                assertCondition (check.equals("0.87667"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setFloat() - Set a CLOB parameter.
- Using an ordinal parameter
**/
    public void Var025()
    {
        if(checkJdbc20())
        {
            if(checkLobSupport ())
            {
                try
                {
                    cs.setFloat (20, 542.3f);
                    failed ("Didn't throw SQLException");
                }
                catch(Exception e)
                {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }




/**
setFloat() - Set a DBCLOB parameter.
- Using an ordinal parameter
**/
    public void Var026()
    {
        if(checkJdbc20())
        {
            if(checkLobSupport ())
            {
                try
                {
                    cs.setFloat (21, 5.432f);
                    failed ("Didn't throw SQLException");
                }
                catch(Exception e)
                {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }



/**
setFloat() - Set a BINARY parameter.
- Using an ordinal parameter
**/
    public void Var027()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setFloat (13, 545.4f);
                cs.execute();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }




/**
setFloat() - Set a VARBINARY parameter.
- Using an ordinal parameter
**/
    public void Var028()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setFloat (14, 1.944f);
                cs.execute();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }




/**
setFloat() - Set a BLOB parameter.
- Using an ordinal parameter
**/
    public void Var029()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setFloat (19, -54.4f);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }




/**
setFloat() - Set a DATE parameter.
- Using an ordinal parameter
**/
    public void Var030()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setFloat (15, -756.5f);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setFloat() - Set a TIME parameter.
- Using an ordinal parameter
**/
    public void Var031()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setFloat (16, 0.1f);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setFloat() - Set a TIMESTAMP parameter.
- Using an ordinal parameter
**/
    public void Var032()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setFloat (17, 454543f);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


/**
setFloat() - Set a DATALINK parameter.
- Using an ordinal parameter
**/
    public void Var033()
    {
        if(checkJdbc20())
        {
            if(checkLobSupport ())
            {
                try
                {
                    cs.setFloat (18, -75.5f);
                    cs.execute();
                    String check= cs.getString(18);
                    assertCondition(check.equals("-75.5"));
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



/**
setFloat() - Set a BIGINT parameter.
- Using an ordinal parameter
**/
    public void Var034()
    {
        if(checkJdbc20())
        {
            if(checkBigintSupport())
            {
                try
                {
                    cs.setFloat (22, -20679f);
                    cs.execute();
                    long check = cs.getLong (22);
                    assertCondition (check == -20679);
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }

/**
setFloat() - Set a BIGINT parameter, when there is a decimal part.
- Using an ordinal parameter
**/
    public void Var035()
    {
        if(checkJdbc20())
        {
            if(checkBigintSupport())
            {
                try
                {
                    cs.setFloat (22, -306.8976f);
                    cs.execute ();
                    long check = cs.getLong (22);
                    assertCondition (check == -306);
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }

/**
setFloat() - Set an BIGINT parameter, when the value is too big.  This will
cause a data truncation exception.
- Using an ordinal parameter
**/
    public void Var036()
    {
        if(checkJdbc20())
        {
            if(checkBigintSupport())
            {
                try
                {
                    cs.setFloat (1, -92233720368547758099.0f);
                    failed ("Didn't throw SQLException");
                }
                catch(Exception e)
                {

		  assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation in V7R1");

                }
            }
        }
    }

/**
setFloat() - Should throw exception when the prepared
statement is closed.
- Using a named parameter
**/
    public void Var037()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.close ();
                cs.setFloat ("P_REAL", 533.45f);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setFloat() - Should throw exception when an invalid index is
specified.
- Using a named parameter
**/
    public void Var038()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setFloat ("P_", 334.45f);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


/**
setFloat() - Should work with a valid parameter index
greater than 1.
- Using a named parameter
**/
    public void Var039()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setFloat ("P_REAL", -43423.456f);
                cs.execute();
                float check = cs.getFloat (3);
                assertCondition (check == -43423.456f);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setFloat() - Should throw exception when the parameter is
not an input parameter.
- Using a named parameter
**/
    public void Var040()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                CallableStatement cs1 = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + "( ?, ?, ?)");cs.setFloat ("P_INTEGER", (short) 222);
                cs1.setFloat ("P2", -43423.456f);            
                cs1.close ();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setFloat() - Set a SMALLINT parameter.
- Using a named parameter
**/
    public void Var041()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setFloat ("P_SMALLINT", 4269f);
                cs.execute();
                short check = cs.getShort (1);
                assertCondition (check == 4269);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setFloat() - Set a SMALLINT parameter, when there is a decimal part.
-Using a named parameter
**/
    public void Var042()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setFloat ("P_SMALLINT", 212.243f);
                cs.execute ();
                short check = cs.getShort (1);
                assertCondition (check == 212);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setFloat() - Set a SMALLINT parameter, when the value is too big.  This will
cause a data truncation exception.
-Using a named parameter
**/
    public void Var043()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setFloat ("P_SMALLINT", -70000.0f);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
		  assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation in V7R1");

            }
        }
    }

/**
setFloat() - Set an INTEGER parameter.
- Using a named parameter
**/
    public void Var044()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setFloat ("P_INTEGER", 30679f);
                cs.execute();
                int check = cs.getInt (2);
                assertCondition (check == 30679);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setFloat() - Set an INTEGER parameter, when there is a decimal part.
- Using a named parameter
**/
    public void Var045()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setFloat ("P_INTEGER", 306.8976f);
                cs.execute ();
                int check = cs.getInt (2);
                assertCondition (check == 306);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setFloat() - Set an INTEGER parameter, when the value is too big.  This will
cause a data truncation exception.
- Using a named parameter
**/
    public void Var046()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setFloat ("P_INTEGER", 21474836448.0f);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
		  assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation in V7R1");

            }
        }
    }

/**
setFloat() - Set an REAL parameter.
- Using a named parameter
**/
    public void Var047()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setFloat ("P_REAL", -1792.249f);
                cs.execute();
                float check = cs.getFloat (3);
                assertCondition (check == -1792.249f);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setFloat() - Set an FLOAT parameter.
- Using a named parameter
**/
    public void Var048()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setFloat ("P_FLOAT", 0.1243f);
            //inserted in with more digits 
                cs.execute();
                double check = cs.getDouble (4);
                assertCondition (check ==  0.12430000305175781);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setFloat() - Set an DOUBLE parameter.
- Using a named parameter
**/
    public void Var049()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setFloat ("P_DOUBLE", 122.290f);
            //inserted in with more digits
                cs.execute();
                double check = cs.getDouble (5);
                assertCondition (check == 122.29000091552734);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setFloat() - Set an DECIMAL parameter.
- Using a named parameter
**/
    public void Var050()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setFloat ("P_DECIMAL_105", 4.0012f);
                cs.execute();
                BigDecimal check = cs.getBigDecimal (7);
                assertCondition (check.floatValue() == 4.0012f);

            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setFloat() - Set an DECIMAL parameter, when the value is too big.
- Using a named parameter
**/
    public void Var051()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setFloat ("P_DECIMAL_50", 103212.2134f);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
		if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
		    assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation now in toolbox");

		} else { 

                assertExceptionIsInstanceOf (e, "java.sql.DataTruncation");
		}
            }
        }
    }

/**
setFloat() - Set a DECIMAL parameter, where the float's fraction truncates.
This does not cause a DataTruncation object to be created.
- Using a named parameter
**/
    public void Var052()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setFloat ("P_DECIMAL_50", -99999.2134f);
            //value is inserted as a whole number (-99999)
                cs.execute();
                BigDecimal check = cs.getBigDecimal (6);
                assertCondition (check.floatValue() == -99999f);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setFloat() - Set an DECIMAL parameter, when the value is very small.
- Using a named parameter
**/
    public void Var053()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setFloat ("P_DECIMAL_105", 0.00007f);
                cs.execute();
                BigDecimal check = cs.getBigDecimal (7);
                assertCondition (check.floatValue() == 0.00007f);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setFloat() - Set an NUMERIC parameter.
- Using a named parameter
**/
    public void Var054()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setFloat ("P_NUMERIC_105", -277.7919f);
                cs.execute();
                BigDecimal check = cs.getBigDecimal (9);
                assertCondition (check.floatValue() == -277.7919f);

            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setFloat() - Set an NUMERIC parameter, when the value is too big
- Using a named parameter
**/
    public void Var055()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setFloat ("P_NUMERIC_50", -24345542334.4f);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
	    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
		assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation now in toolbox");

	    } else { 

		DataTruncation dt = (DataTruncation)e;
		assertCondition ((dt.getIndex() == 8)                                         
				 && (dt.getParameter() == true)
				 && (dt.getRead() == false)
				 && (dt.getTransferSize() == 5));
	    }
            }
        }
    }

/**
setFloat() - Set a NUMERIC parameter, where the float's fraction truncates.
This does not cause a DataTruncation object to be created.
- Using a named parameter
**/
    public void Var056()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setFloat ("P_NUMERIC_50", 99999.2134f);
            //value is inserted as a whole number (99999)
                cs.execute();
                BigDecimal check = cs.getBigDecimal (8);
                assertCondition (check.intValue() == 99999);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setFloat() - Set an NUMERIC parameter, when the value is very small.
- Using a named parameter
**/
    public void Var057()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setFloat ("P_NUMERIC_105", -0.00003f);
                cs.execute();
                BigDecimal check = cs.getBigDecimal (9);
                assertCondition (check.floatValue() == -0.00003f);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
setFloat() - Set an CHAR(50) parameter.
- Using a named parameter
**/
    public void Var058()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setFloat ("P_CHAR_50", -1842.799f);
                cs.execute();
                String check = cs.getString (11);
                assertCondition (check.equals("-1842.799                                         "));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setFloat() - Set an CHAR(1) parameter.
- Using a named parameter
**/
    public void Var059()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
            // It seems that any float translates into bigger than
            // 1 character, so for a CHAR(1), we will always get a 
            // DataTruncation.
                cs.setFloat ("P_CHAR_1", 9f);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.DataTruncation");
            }
        }
    }



/**
setFloat() - Set an VARCHAR parameter.
- Using a named parameter
**/
    public void Var060()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setFloat ("P_VARCHAR_50", 0.87667f);
                cs.execute();
                String check = cs.getString (12);
                assertCondition (check.equals("0.87667"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setFloat() - Set a CLOB parameter.
- Using a named parameter
**/
    public void Var061()
    {
        if(checkNamedParametersSupport())
        {
            if(checkLobSupport ())
            {
                try
                {
                    cs.setFloat ("P_CLOB", 542.3f);
                    failed ("Didn't throw SQLException");
                }
                catch(Exception e)
                {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }




/**
setFloat() - Set a DBCLOB parameter.
- Using a named parameter
**/
    public void Var062()
    {
        if(checkNamedParametersSupport())
        {
            if(checkLobSupport ())
            {
                try
                {
                    cs.setFloat ("P_DBCLOB", 5.432f);
                    failed ("Didn't throw SQLException");
                }
                catch(Exception e)
                {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }



/**
setFloat() - Set a BINARY parameter.
- Using a named parameter
**/
    public void Var063()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setFloat ("P_BINARY_20", 545.4f);
                cs.execute();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }




/**
setFloat() - Set a VARBINARY parameter.
- Using a named parameter
**/
    public void Var064()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setFloat ("P_VARBINARY_20", 1.944f);
                cs.execute();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }




/**
setFloat() - Set a BLOB parameter.
- Using a named parameter
**/
    public void Var065()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setFloat ("P_BLOB", -54.4f);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }




/**
setFloat() - Set a DATE parameter.
- Using a named parameter
**/
    public void Var066()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setFloat ("P_DATE", -756.5f);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setFloat() - Set a TIME parameter.
- Using a named parameter
**/
    public void Var067()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setFloat ("P_TIME", 0.1f);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setFloat() - Set a TIMESTAMP parameter.
- Using a named parameter
**/
    public void Var068()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setFloat ("P_TIMESTAMP", 454543f);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


/**
setFloat() - Set a DATALINK parameter.
- Using a named parameter
**/
    public void Var069()
    {
        if(checkNamedParametersSupport())
        {
            if(checkLobSupport ())
            {
                try
                {
                    cs.setFloat ("P_DATALINK", -75.5f);
                    cs.execute();
                    String check= cs.getString(18);
                    assertCondition(check.equals("-75.5"));
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



/**
setFloat() - Set a BIGINT parameter.
- Using a named parameter
**/
    public void Var070()
    {
        if(checkNamedParametersSupport())
        {
            if(checkBigintSupport())
            {
                try
                {
                    cs.setFloat ("P_BIGINT", -20679f);
                    cs.execute();
                    long check = cs.getLong (22);
                    assertCondition (check == -20679);
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }

/**
setFloat() - Set a BIGINT parameter, when there is a decimal part.
- Using a named parameter
**/
    public void Var071()
    {
        if(checkNamedParametersSupport())
        {
            if(checkBigintSupport())
            {
                try
                {
                    cs.setFloat ("P_BIGINT", -306.8976f);
                    cs.execute ();
                    long check = cs.getLong (22);
                    assertCondition (check == -306);
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }

/**
setFloat() - Set an BIGINT parameter, when the value is too big.  This will
cause a data truncation exception.
- Using a named parameter
**/
    public void Var072()
    {
        if(checkNamedParametersSupport())
        {
            if(checkBigintSupport())
            {
                try
                {
                    cs.setFloat ("P_BIGINT", -92233720368547758099.0f);
                    failed ("Didn't throw SQLException");
                }
                catch(Exception e)
                {
		  assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation in V7R1");

                }
            }
        }
    }

/**
setFloat() - Should throw an exception if both an ordinal and named parameter 
are used.
- Using an ordinal parameter
- Using a named parameter
**/
    public void Var073 ()
    {
        if(checkNamedParametersSupport())
        {
            try {
                cs.setFloat("P_REAL", 45.6f);
                cs.setFloat(4, 65.5f);
                cs.execute();
                float check1 = cs.getFloat (3);
                double check2 = cs.getDouble(4);
                assertCondition ((check1 == 45.6f) && (check2==65.5d));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setFloat() - Attempt to set the return value parameter.
- Using an ordinal parameter
**/
    public void Var074()
    {
        if(checkJdbc20())
        {
            if(checkReturnValueSupport())
            {
                try
                {
                    CallableStatement cs2 = connection_.prepareCall("?=CALL " + JDSetupProcedure.STP_CSRV);
                    cs2.setFloat(1, 444f);
                    cs2.close();
                    failed ("Didn't throw SQLException");
                }
                catch(Exception e)
                {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }

/**
setFloat() - Should work since the lower case name is not in quotes.
- Using a named parameter
**/
    public void Var075()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setFloat ("p_real", -43423.456f);
                cs.execute();
                float check = cs.getFloat (3);
                assertCondition (check == -43423.456f);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setFloat() - Should work since the mixed case name is not in quotes.
- Using a named parameter
**/
    public void Var076()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setFloat ("P_rEaL", -43423.456f);
                cs.execute();
                float check = cs.getFloat (3);
                assertCondition (check == -43423.456f);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setFloat() - Should throw an exception since the mixed case name is in quotes.
- Using a named parameter
**/
    public void Var077()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setFloat ("'P_rEaL'", -43423.456f);
                failed("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setFloat() - Should throw an exception since the lower case name is in quotes.
- Using a named parameter
**/
    public void Var078()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setFloat ("'p_real'", -43423.456f);
                failed("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }
    
    
    /**
     * setFloat() - Set a BOOLEAN parameter. - Using an ordinal parameter
     **/
    public void Var079() {

        if (checkBooleanSupport()) {
          setTestSuccessful("setFloat", 27, "0", "0",
              " -- call setFloat against BOOLEAN parameter -- added January 2021");
        }
    }

    
    /**
     * setFloat() - Set a BOOLEAN parameter. - Using a named parameter
     **/
    public void Var080() {

        if (checkBooleanSupport()) {
          setTestSuccessful("setFloat", "P_BOOLEAN", "1", "1",
              " -- call setFloat against BOOLEAN parameter -- added January 2021");
        }
    }
}
