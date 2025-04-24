///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSSetDouble.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSSetDouble.java
//
// Classes:      JDCSSetDouble
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.DataTruncation;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDSetupProcedure;
import test.JDTestDriver;



/**
Testcase JDCSSetDouble.  This tests the following method
of the JDBC CallableStatement class:

<ul>
<li>setDouble()
</ul>
**/
public class JDCSSetDouble
extends JDCSSetTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSSetDouble";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCSTest.main(newArgs); 
   }



   


/**
Constructor.
**/
    public JDCSSetDouble (AS400 systemObject,
                          Hashtable<String,Vector<String>> namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDCSSetDouble",
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
Compares 2 doubles, allowing for some rounding error.
**/
    private boolean compare (double d1, double d2)
    {
        return(Math.abs (d1 - d2) < 0.001);
    }


/**
setDouble() - Should throw exception when the prepared
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
                cs.setDouble (4, 33.4345);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setDouble() - Should throw exception when an invalid index is
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
                cs.setDouble (100, 334.432);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


/**
setDouble() - Should work with a valid parameter index
greater than 1.
- Using an ordinal parameter
**/
    public void Var003()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setDouble (4, 93423.459);
                cs.execute();
                double check = cs.getDouble (4);
                assertCondition (check == 93423.459);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setDouble() - Should throw exception when the parameter is
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
                cs1.setDouble(2, 2222.5);
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
setDouble() - Set a SMALLINT parameter.
- Using an ordinal parameter
**/
    public void Var005()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setDouble (1, -426);
                cs.execute();
                short check = cs.getShort (1);
                assertCondition (check == -426);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setDouble() - Set an SMALLINT parameter, when there is a decimal part.
- Using an ordinal parameter
**/
    public void Var006 ()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setDouble (1, -212.222);
                cs.execute();
                short check=cs.getShort(1);
                assertCondition(check==-212);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setDouble() - Set a SMALLINT parameter, when the value is too big.  This should 
cause a DataTruncation exception to be thrown.
- Using an ordinal parameter
**/
    public void Var007 ()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setDouble (1, -2122846575.1);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {

		  assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation in V7R1");

            }
        }
    }

/**
setDouble() - Set an INTEGER parameter.
- Using an ordinal parameter
**/
    public void Var008()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setDouble (2, -30679);
                cs.execute();
                int check = cs.getInt (2);
                assertCondition (check == -30679);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setDouble() - Set an INTEGER parameter, when there is a decimal part.
- Using an ordinal parameter
**/
    public void Var009 ()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setDouble (2, -306.8);
                cs.execute();
                int check=cs.getInt(2);
                assertCondition(check==-306);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setDouble() - Set an INTEGER parameter, when the value is too big.  This should
cause a DataTruncation exception to be thrown.
- Using an ordinal parameter
**/
    public void Var010 ()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setDouble (2, -448484842122846575.1);
                cs.execute();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
		  assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation in V7R1");

            }
        }
    }

/**
setDouble() - Set an REAL parameter.
- Using an ordinal parameter
**/
    public void Var011()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setDouble (3, 792.249);
                cs.execute();
                float check = cs.getFloat (3);
                assertCondition (check == 792.249f);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setDouble() - Set an FLOAT parameter.
- Using an ordinal parameter
**/
    public void Var012()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setDouble (4, -0.123);
                cs.execute();
                double check = cs.getDouble (4);
                assertCondition (check ==  -0.123);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setDouble() - Set an DOUBLE parameter.
- Using an ordinal parameter
**/
    public void Var013()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setDouble (5, -1.22290344);
                cs.execute();
                double check = cs.getDouble (5);
                assertCondition (check == -1.22290344);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setDouble() - Set an DECIMAL parameter.
- Using an ordinal parameter
**/
    public void Var014()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setDouble (7, -14001.23425);
                cs.execute();
                BigDecimal check = cs.getBigDecimal (7);
                assertCondition (check.doubleValue() == -14001.23425);

            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setDouble() - Set a DECIMAL paramter, when the value is too big.
- Using an ordinal parameter
**/
    public void Var015 ()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setDouble(6, -10132342.2);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {

	    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
		assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation now in toolbox");

	    } else { 

		DataTruncation dt= (DataTruncation)e;
		assertCondition ((dt.getIndex()==6)
				 && (dt.getParameter()==true)
				 && (dt.getRead()==false)
				 && (dt.getTransferSize() ==5));
	    }
            }
        }
    }

/**
setDouble() - Set a DECIMAL parameter, when only the fraction truncates.  This
is allowed.
- Using an ordinal parameter
**/
    public void Var016 ()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setDouble(6, -32342.2363);
                cs.execute();
                BigDecimal check=cs.getBigDecimal(6);
                assertCondition(compare(check.doubleValue(), -32342));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setDouble() - Set a DECIMAL parameter, when the value is very small.
- Using an ordinal parameter
**/
    public void Var017 ()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setDouble (7, -0.00008);
                cs.execute();
                BigDecimal check=cs.getBigDecimal(7);
                assertCondition(compare(check.doubleValue(), -0.00008));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setDouble() - Set an NUMERIC parameter.
- Using an ordinal parameter
**/
    public void Var018()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setDouble (9,-2774 );
                cs.execute();
                BigDecimal check = cs.getBigDecimal (9);
                assertCondition (check.doubleValue() == -2774);

            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setDouble() - Set a NUMERIC parameter, when the value is too big.  The Native JDBC driver and
the Toolbox JDBC driver differ slightly in the information contained in the data truncation
exception at this point.  The values for the transfer size and data size in the data truncation 
object differ some between the native JDBC driver and the Toolbox JDBC driver.  The native JDBC
driver bases the exception information off of the significant digits that are truncated.  The
data size is 14 significant digits and the transfer size is 5 digits.
- Using an ordinal parameter
**/
    public void Var019 ()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setDouble (9, -22143456437334.4);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
	    {
		if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
		    assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation now in toolbox");

		} else { 

		    DataTruncation dt = (DataTruncation)e;
		    if (getDriver() == JDTestDriver.DRIVER_NATIVE && getDriverFixLevel() < 49160) {
			notApplicable("Native driver fix in PTF V7R1 SI49160" );
			return; 
		    }

		    assertCondition ((dt.getIndex() == 9)
				     && (dt.getParameter()==true)
				     && (dt.getRead() ==false)
				     && (dt.getTransferSize()==10)
				     && (dt.getDataSize()==19),
				     "dt.getIndex()="+dt.getIndex()+" sb 9 "+
				     "dt.getParameter()="+dt.getParameter()+" sb true "+
				     "dt.getRead()="+dt.getRead()+" sb false "+
				     "dt.getDataSize()="+dt.getDataSize()+" sb 19 "+
				     "dt.getTransferSize()="+dt.getTransferSize()+" sb 10  Updated 1/18/2013 for V7R1 fix");
		}
	    }
        }
    }

/**
setDouble() - Set a NUMERIC parameter, when only the fraction truncates.  This
is allowed.
- Using an ordinal parameter
**/
    public void Var020 ()
    {
        if(checkJdbc20())
        {
            try
            {
            //needed to open new connection to get this to work properly.
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setDouble (8, -32342.2363);
                cs.execute();
                BigDecimal check=cs.getBigDecimal(8);
                assertCondition(check.doubleValue() == -32342);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setDouble() - Set a NUMERIC parameter, when the value is very small.
- Using an ordinal parameter
**/
    public void Var021 ()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setDouble (9, 0.00005);
                cs.execute();
                BigDecimal check=cs.getBigDecimal(9);
                assertCondition(check.doubleValue()==0.00005);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setDouble() - Set an CHAR(50) parameter.
- Using an ordinal parameter
**/
    public void Var022()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setDouble (11, 218242.799332);
                cs.execute();
                String check = cs.getString (11);
                assertCondition (check.equals("218242.799332                                     "));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setDouble() - Set an CHAR(1) parameter.  This throws an exception because
the double is stored with .0 at the end.
- Using an ordinal parameter
**/
    public void Var023()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setDouble (10, 5);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.DataTruncation");
            }
        }
    }

/**
setDouble() - Set a CHAR(1) parameter, when the value is too big.
- Using an ordinal parameter
**/
    public void Var024 ()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setDouble (10, 533.25);
                failed("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.DataTruncation");
            }
        }
    }



/**
setDouble() - Set an VARCHAR parameter.
- Using an ordinal parameter
**/
    public void Var025()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setDouble (12, -0.8766753);
                cs.execute();
                String check = cs.getString (12);
                assertCondition (check.equals("-0.8766753"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setDouble() - Set a CLOB parameter.
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
                    cs.setDouble (20, -6542.3);
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
setDouble() - Set a DBCLOB parameter.
- Using an ordinal parameter
**/
    public void Var027()
    {
        if(checkJdbc20())
        {
            if(checkLobSupport ())
            {
                try
                {
                    cs.setDouble (21, -45.432);
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
setDouble() - Set a BINARY parameter.
- Using an ordinal parameter
**/
    public void Var028()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setDouble (13, -545.44);
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
setDouble() - Set a VARBINARY parameter.
- Using an ordinal parameter
**/
    public void Var029()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setDouble (14, -1.9443);
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
setDouble() - Set a BLOB parameter.
- Using an ordinal parameter
**/
    public void Var030()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setDouble (19, 54.43);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }




/**
setDouble() - Set a DATE parameter.
- Using an ordinal parameter
**/
    public void Var031()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setDouble (15, 56.5);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setDouble() - Set a TIME parameter.
- Using an ordinal parameter
**/
    public void Var032()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setDouble (16, 0.13);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setDouble() - Set a TIMESTAMP parameter.
- Using an ordinal parameter
**/
    public void Var033()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setDouble (17, 454);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


/**
setDouble() - Set a DATALINK parameter. A datalink can't be an out parameter so it
is represented here as a VARCHAR.  That is why this test passes.
- Using an ordinal parameter
**/
    public void Var034()
    {
        if(checkJdbc20())
        {
            if(checkLobSupport ())
            {
                try
                {
                    cs.setDouble (18, -5.54);
                    cs.execute();
                    String check= cs.getString(18);
                    assertCondition(check.equals("-5.54"));
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



/**
setDouble() - Set a BIGINT parameter.
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
                    cs.setDouble (22, 3079);
                    cs.execute();
                    long check = cs.getLong (22);
                    assertCondition (check == 3079);
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }

/**
setDouble() - Set a BIGINT paramter, when there is a decimal part.
- Using an ordinal parameter
**/
    public void Var036 ()
    {
        if(checkJdbc20())
        {
            if(checkBigintSupport())
            {
                try
                {
                    cs.setDouble (22, 306.8);
                    cs.execute();
                    long check=cs.getLong(22);
                    assertCondition(check==306);
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }

/** 
setDouble() - Set a BIGINT parameter, when the value is too big.  This should cause a 
DataTruncation exception to be thrown.
- Using an ordinal parameter
**/
    public void Var037 ()
    {
        if(checkJdbc20())
        {
            if(checkBigintSupport())
            {
                try
                {
                    cs.setDouble (22, 306154175489237602375024750974097540.8);
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
setDouble() - Should throw exception when the prepared
statement is closed.
- Using a named parameter
**/
    public void Var038()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.close ();
                cs.setDouble ("P_FLOAT", 33.4345);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setDouble() - Should throw exception when an invalid name is
specified.
- Using a named parameter
**/
    public void Var039()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setDouble ("PARAM_1", 334.432);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setDouble() - Should work with a valid parameter name.
- Using a named parameter
**/
    public void Var040()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setDouble ("P_FLOAT", 93423.459);
                cs.execute();
                double check = cs.getDouble (4);
                assertCondition (check == 93423.459);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setDouble() - Should throw exception when the parameter is
not an input parameter.
- Using a named parameter
**/
    public void Var041()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                CallableStatement cs1 = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + "( ?, ?, ?)");cs.setDouble (2, (short) 222);
                cs1.setDouble("P2", 2222.5);
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
setDouble() - Set a SMALLINT parameter.
- Using a named parameter
**/
    public void Var042()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setDouble ("P_SMALLINT", -426);
                cs.execute();
                short check = cs.getShort (1);
                assertCondition (check == -426);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setDouble() - Set an SMALLINT parameter, when there is a decimal part.
- Using a named parameter
**/
    public void Var043 ()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setDouble ("P_SMALLINT", -212.222);
                cs.execute();
                short check=cs.getShort(1);
                assertCondition(check==-212);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setDouble() - Set a SMALLINT parameter, when the value is too big.  This should 
cause a DataTruncation exception to be thrown.
- Using a named parameter
**/
    public void Var044 ()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setDouble ("P_SMALLINT", -2122846575.1);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
		  assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation in V7R1");

            }
        }
    }

/**
setDouble() - Set an INTEGER parameter.
- Using a named parameter
**/
    public void Var045()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setDouble ("P_INTEGER", -30679);
                cs.execute();
                int check = cs.getInt (2);
                assertCondition (check == -30679);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setDouble() - Set an INTEGER parameter, when there is a decimal part.
- Using a named parameter
**/
    public void Var046 ()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setDouble ("P_INTEGER", -306.8);
                cs.execute();
                int check=cs.getInt(2);
                assertCondition(check==-306);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setDouble() - Set an INTEGER parameter, when the value is too big.  This should
cause a DataTruncation exception to be thrown.
- Using a named parameter
**/
    public void Var047 ()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setDouble ("P_INTEGER", -448484842122846575.1);
                cs.execute();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
		  assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation in V7R1");

            }
        }
    }

/**
setDouble() - Set an REAL parameter.
- Using a named parameter
**/
    public void Var048()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setDouble ("P_REAL", 792.249);
                cs.execute();
                float check = cs.getFloat (3);
                assertCondition (check == 792.249f);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setDouble() - Set an FLOAT parameter.
- Using a named parameter
**/
    public void Var049()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setDouble ("P_FLOAT", -0.123);
                cs.execute();
                double check = cs.getDouble (4);
                assertCondition (check ==  -0.123);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setDouble() - Set an DOUBLE parameter.
- Using a named parameter
**/
    public void Var050()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setDouble ("P_DOUBLE", -1.22290344);
                cs.execute();
                double check = cs.getDouble (5);
                assertCondition (check == -1.22290344);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setDouble() - Set an DECIMAL parameter.
- Using a named parameter
**/
    public void Var051()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setDouble ("P_DECIMAL_105", -14001.23425);
                cs.execute();
                BigDecimal check = cs.getBigDecimal (7);
                assertCondition (check.doubleValue() == -14001.23425);

            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setDouble() - Set a DECIMAL paramter, when the value is too big.
- Using a named parameter
**/
    public void Var052 ()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setDouble("P_DECIMAL_50", -10132342.2);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
		if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
		    assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation now in toolbox");

		} else { 

		    DataTruncation dt= (DataTruncation)e;
		    assertCondition ((dt.getIndex()==6)
				     && (dt.getParameter()==true)
				     && (dt.getRead()==false)
				     && (dt.getTransferSize() ==5));
		}
            }
        }
    }

/**
setDouble() - Set a DECIMAL parameter, when only the fraction truncates.  This
is allowed.
- Using a named parameter
**/
    public void Var053 ()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setDouble("P_DECIMAL_50", -32342.2363);
                cs.execute();
                BigDecimal check=cs.getBigDecimal(6);
                assertCondition(compare(check.doubleValue(), -32342));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setDouble() - Set a DECIMAL parameter, when the value is very small.
- Using a named parameter
**/
    public void Var054 ()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setDouble ("P_DECIMAL_105", -0.00008);
                cs.execute();
                BigDecimal check=cs.getBigDecimal(7);
                assertCondition(compare(check.doubleValue(), -0.00008));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setDouble() - Set an NUMERIC parameter.
- Using a named parameter
**/
    public void Var055()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setDouble ("P_NUMERIC_105",-2774 );
                cs.execute();
                BigDecimal check = cs.getBigDecimal (9);
                assertCondition (check.doubleValue() == -2774);

            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setDouble() - Set a NUMERIC parameter, when the value is too big.  The Native JDBC driver and
the Toolbox JDBC driver differ slightly in the information contained in the data truncation
exception at this point.  The values for the transfer size and data size in the data truncation 
object differ some between the native JDBC driver and the Toolbox JDBC driver.  The native JDBC
driver bases the exception information off of the significant digits that are truncated.  The
data size is 14 significant digits and the transfer size is 5 digits.
- Using a named parameter
**/
    public void Var056 ()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setDouble ("P_NUMERIC_105", -22143456437334.4);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
		if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
		    assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation now in toolbox");

		} else { 

		    DataTruncation dt = (DataTruncation)e;

			if (getDriver() == JDTestDriver.DRIVER_NATIVE && getDriverFixLevel() < 49160) {
			    notApplicable("Native driver fix in PTF V7R1 SI49160" );
			    return; 
			}

			assertCondition ((dt.getIndex() == 9)
					 && (dt.getParameter()==true)
					 && (dt.getRead() ==false)
					 && (dt.getTransferSize()==10)
					 && (dt.getDataSize()==19), "Updated 1/18/2013 for V7R1 fix");

		}
            }
        }
    }

/**
setDouble() - Set a NUMERIC parameter, when only the fraction truncates.  This
is allowed.
- Using a named parameter
**/
    public void Var057 ()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
            //needed to open new connection to get this to work properly.
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setDouble ("P_NUMERIC_50", -32342.2363);
                cs.execute();
                BigDecimal check=cs.getBigDecimal(8);
                assertCondition(check.doubleValue() == -32342);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setDouble() - Set a NUMERIC parameter, when the value is very small.
- Using a named parameter
**/
    public void Var058 ()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setDouble ("P_NUMERIC_105", 0.00005);
                cs.execute();
                BigDecimal check=cs.getBigDecimal(9);
                assertCondition(check.doubleValue()==0.00005);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setDouble() - Set an CHAR(50) parameter.
- Using a named parameter
**/
    public void Var059()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setDouble ("P_CHAR_50", 218242.799332);
                cs.execute();
                String check = cs.getString (11);
                assertCondition (check.equals("218242.799332                                     "));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setDouble() - Set an CHAR(1) parameter.  This throws an exception because
the double is stored with .0 at the end.
- Using a named parameter
**/
    public void Var060()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setDouble ("P_CHAR_1", 5);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.DataTruncation");
            }
        }
    }

/**
setDouble() - Set a CHAR(1) parameter, when the value is too big.
- Using a named parameter
**/
    public void Var061 ()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setDouble ("P_CHAR_1", 533.25);
                failed("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.DataTruncation");
            }
        }
    }

/**
setDouble() - Set an VARCHAR parameter.
- Using a named parameter
**/
    public void Var062()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setDouble ("P_VARCHAR_50", -0.8766753);
                cs.execute();
                String check = cs.getString (12);
                assertCondition (check.equals("-0.8766753"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setDouble() - Set a CLOB parameter.
- Using a named parameter
**/
    public void Var063()
    {
        if(checkNamedParametersSupport())
        {
            if(checkLobSupport ())
            {
                try
                {
                    cs.setDouble ("P_CLOB", -6542.3);
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
setDouble() - Set a DBCLOB parameter.
- Using a named parameter
**/
    public void Var064()
    {
        if(checkNamedParametersSupport())
        {
            if(checkLobSupport ())
            {
                try
                {
                    cs.setDouble ("P_DBCLOB", -45.432);
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
setDouble() - Set a BINARY parameter.
- Using a named parameter
**/
    public void Var065()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setDouble ("P_BINARY_20", -545.44);
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
setDouble() - Set a VARBINARY parameter.
- Using a named parameter
**/
    public void Var066()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setDouble ("P_VARBINARY_20", -1.9443);
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
setDouble() - Set a BLOB parameter.
- Using a named parameter
**/
    public void Var067()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setDouble ("P_BLOB", 54.43);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setDouble() - Set a DATE parameter.
- Using a named parameter
**/
    public void Var068()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setDouble ("P_DATE", 56.5);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setDouble() - Set a TIME parameter.
- Using a named parameter
**/
    public void Var069()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setDouble ("P_TIME", 0.13);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setDouble() - Set a TIMESTAMP parameter.
- Using a named parameter
**/
    public void Var070()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setDouble ("P_TIMESTAMP", 454);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setDouble() - Set a DATALINK parameter. A datalink can't be an out parameter so it
is represented here as a VARCHAR.  That is why this test passes.
- Using a named parameter
**/
    public void Var071()
    {
        if(checkNamedParametersSupport())
        {
            if(checkLobSupport ())
            {
                try
                {
                    cs.setDouble ("P_DATALINK", -5.54);
                    cs.execute();
                    String check= cs.getString(18);
                    assertCondition(check.equals("-5.54"));
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }

/**
setDouble() - Set a BIGINT parameter.
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
                    cs.setDouble ("P_BIGINT", 3079);
                    cs.execute();
                    long check = cs.getLong (22);
                    assertCondition (check == 3079);
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }

/**
setDouble() - Set a BIGINT paramter, when there is a decimal part.
- Using a named parameter
**/
    public void Var073 ()
    {
        if(checkNamedParametersSupport())
        {
            if(checkBigintSupport())
            {
                try
                {
                    cs.setDouble ("P_BIGINT", 306.8);
                    cs.execute();
                    long check=cs.getLong(22);
                    assertCondition(check==306);
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }

/** 
setDouble() - Set a BIGINT parameter, when the value is too big.  This should cause a 
DataTruncation exception to be thrown.
- Using a named parameter
**/
    public void Var074 ()
    {
        if(checkNamedParametersSupport())
        {
            if(checkBigintSupport())
            {
                try
                {
                    cs.setDouble ("P_BIGINT", 306154175489237602375024750974097540.8);
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
setDouble() - Setting a parameters with an ordinal and a named parameter for one 
callable statement should throw an exception.
- Using an ordinal parameter
- Using a named parameter
**/
    public void Var075 ()
    {
        if(checkNamedParametersSupport())
        {
            try {
                cs.setDouble (4, -568.98);
                cs.setDouble ("P_DOUBLE", -98569.52);
                cs.execute();
                double check1 = cs.getDouble (4);
                double check2 = cs.getDouble(5);
                assertCondition ((check1 == -568.98) && (check2==-98569.52));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setDouble() - Attempt to set the return value parameter.
- Using an ordinal parameter
**/
    public void Var076()
    {
        if(checkJdbc20())
        {
            if(checkReturnValueSupport())
            {
                try
                {
                    CallableStatement cs2 = connection_.prepareCall("?=CALL " + JDSetupProcedure.STP_CSRV);
                    cs2.setDouble(1, 52.232);
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
setDouble() - Should work since the lower case parameter name is not in quotes.
- Using a named parameter
**/
    public void Var077()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setDouble ("p_float", 93423.459);
                cs.execute();
                double check = cs.getDouble (4);
                assertCondition (check == 93423.459);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setDouble() - Should work since the mixed case name is not in quotes.
- Using a named parameter
**/
    public void Var078()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setDouble ("P_fLoAt", 93423.459);
                cs.execute();
                double check = cs.getDouble (4);
                assertCondition (check == 93423.459);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
setDouble() - Should throw an exception since the lower case name is in quotes.
- Using a named parameter
**/
    public void Var079()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setDouble ("'p_float'", 93423.459);
                failed("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }

    }

/**
setDouble() - Should throw an exception since the mixed case name is in quotes.
- Using a named parameter
**/
    public void Var080()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setDouble ("'P_fLoAt'", 93423.459);
                failed("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }
    
    
    
    /**
     * setDouble() - Set a BOOLEAN parameter. - Using an ordinal parameter
     **/
    public void Var081() {

        if (checkBooleanSupport()) {
          setTestSuccessful("setDouble", 27, "0", "0",
              " -- call setDouble against BOOLEAN parameter -- added January 2021");
        }
    }

    
    /**
     * setDouble() - Set a BOOLEAN parameter. - Using a named parameter
     **/
    public void Var082() {

        if (checkBooleanSupport()) {
          setTestSuccessful("setDouble", "P_BOOLEAN", "1", "1",
              " -- call setDouble against BOOLEAN parameter -- added January 2021");
        }
    }
}
