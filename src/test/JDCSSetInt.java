///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSSetInt.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////



//////////////////////////////////////////////////////////////////////
//
//
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSSetInt.java
//
// Classes:      JDCSSetInt
//
////////////////////////////////////////////////////////////////////////

package test;

import java.awt.TextArea;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.DataTruncation;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;



/**
Testcase JDCSetInt.  This tests the following method
of the JDBC CallableStatement class:

<ul>
<li>setInt()
</ul>
**/
public class JDCSSetInt
extends JDCSSetTestcase {




/**
Constructor.
**/
    public JDCSSetInt (AS400 systemObject,
                       Hashtable namesAndVars,
                       int runMode,
                       FileOutputStream fileOutputStream,
                       
                       String password)
    {
        super (systemObject, "JDCSSetInt",
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
setInt() - Should throw an exception when the callable 
statement is closed.
- Using an ordinal parameter
**/
    public void Var001 ()
    {
        if (checkJdbc20()) {
            try {
                cs.close ();
                cs.setInt (2, 533);
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setInt() - Should throw an exception when an invalid index is specified.
- Using an ordinal parameter
**/
    public void Var002 ()
    {
        if (checkJdbc20()) {
            try {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);

                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setInt (100, 524);
                cs.execute();
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/** 
setInt() - Should throw an exception when the index is 0.
- Using an ordinal parameter
**/
    public void Var003 ()
    {
        if (checkJdbc20()) {
            try {

                cs.setInt (0, 454);
                cs.execute();
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setInt()- Should throw an exception when the index is -1.
- Using an ordinal parameter
**/
    public void Var004 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setInt (-1, 585);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/** 
setInt() - Should work with a valid parameter index.
- Using an ordinal parameter
**/
    public void Var005 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setInt (2, 585);
                cs.execute();
                int check=cs.getInt(2);
                assertCondition(check==585);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/** 
setInt() - Should throw an exception when the parameter is not an input parameter.
- Using an ordinal parameter
**/
    public void Var006 ()
    {
        if (checkJdbc20()) {
            try {
                CallableStatement cs1=connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + "( ?, ?, ?)");
                cs1.setInt(2, 525);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setInt() - Set a SMALLINT parameter.
- Using an ordinal parameter
**/
    public void Var007 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setInt (1, 2352);
                cs.execute();
                short check=cs.getShort(1);
                assertCondition(check==2352);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setInt() - Set a SMALLINT parameter when the index is too large.
- Using an ordinal parameter
**/
    public void Var008 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setInt (1, 6568556);
                failed("Didn't throw SQLException");
            } catch (Exception e) {

		boolean success = e.getMessage().indexOf("Data type mismatch") != -1;
		if (success) { 
		    assertCondition(true); 
		} else { 
		    failed(e, "Expected data type mismatch"); 
		} 

            }
        }
    }

/**
setInt() - Set an INTEGER parameter.
- Using an ordinal parameter
**/
    public void Var009 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setInt (2, 6585954);
                cs.execute();
                int check = cs.getInt(2);
                assertCondition(check==6585954);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setInt() - Set a REAL parameter.
- Using an ordinal parameter
**/
    public void Var010 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setInt (3, 487);
                cs.execute();
                float check=cs.getFloat(3);
                assertCondition(check==487f);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setInt() - Set a FLOAT parameter.
- Using an ordinal parameter
**/
    public void Var011 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setInt (4, 52646);
                cs.execute();
                double check=cs.getDouble(4);
                assertCondition(check==52646);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setInt() - Set a DOUBLE parameter.
- Using an ordinal parameter
**/
    public void Var012 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setInt (5, 4521);
                cs.execute();
                double check=cs.getDouble(5);
                assertCondition(check==4521);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setInt() - Set a DECIMAL parameter.
- Using an ordinal parameter
**/
    public void Var013 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setInt (7, -10012);
                cs.execute();
                BigDecimal check=cs.getBigDecimal(7);
                assertCondition(check.intValue()==-10012);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setInt() - Set a DECIMAL parameter, when the value is too big.
- Using an ordinal parameter
**/
    public void Var014 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setInt (6, -100122123);
                cs.execute();
                failed("Didn't throw SQLException");
            } catch (Exception e) {
		if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
		    assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation now in toolbox");

		} else { 

		    assertExceptionIsInstanceOf (e, "java.sql.DataTruncation");
		}
            }
        }
    }

/**
setInt() - Set a Numeric parameter.
- Using an ordinal parameter
**/
    public void Var015 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setInt (8, 12888);
                cs.execute();
                BigDecimal check=cs.getBigDecimal(8);
                assertCondition(check.intValue()==12888);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setInt() - Set a NUMERIC parameter, when the value is too big.
- Using an ordinal parameter
**/
    public void Var016 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setInt (8, 1134567892);
                cs.execute();
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
		if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
		    assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation now in toolbox");

		} else { 

		    DataTruncation dt=(DataTruncation)e;
		    assertCondition ((dt.getIndex() == 8)
				     && (dt.getParameter()==true)
				     && (dt.getRead() ==false)
				     && (dt.getTransferSize()==5));
		}
            }
        }
    }

/**
setInt() - Set a CHAR(50) parameter.
- Using an ordinal parameter
**/
    public void Var017 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setInt (11, 1547);
                cs.execute();
                String check=cs.getString(11);
                assertCondition(check.equals("1547                                              "));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setInt() - Set a CHAR(1) parameter.
- Using an ordinal parameter
**/
    public void Var018 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setInt (10, 9);
                cs.execute();
                String check=cs.getString(10);
                assertCondition(check.equals("9"));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setInt() - Set a CHAR(1) parameter, when the value is too big.
- Using an ordinal parameter
**/
    public void Var019 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setInt (10, 98);
                cs.execute();
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.DataTruncation");
            }
        }
    }

/**
setInt() - Set a VARCHAR parameter.
- Using an ordinal parameter
**/
    public void Var020 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setInt (12, 5867);
                cs.execute();
                String check=cs.getString(12);
                assertCondition(check.equals("5867"));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setInt() - Set a CLOB parameter.
- Using an ordinal parameter
**/
    public void Var021 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setInt (20, 5854);
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setInt() - Set a DBCLOB parameter.
- Using an ordinal parameter
**/
    public void Var022 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setInt (21, 587465);
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setInt() - Set a BINARY parameter.
- Using an ordinal parameter
**/
    public void Var023 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setInt (13, 121365);
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setInt() - Set a VARBINARY parameter.
- Using an ordinal parameter
**/
    public void Var024 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setInt (14, 56789);
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setInt() - Set a BLOB parameter.
- Using an ordinal parameter
**/
    public void Var025 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setInt (19, 9080);
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setInt() - Set a DATE parameter.
- Using an ordinal parameter
**/
    public void Var026 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setInt (15, 564);
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf ( e, "java.sql.SQLException");
            }
        }
    }

/**
setInt() - Set a TIME parameter.
- Using an ordinal parameter 
**/
    public void Var027 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setInt (16, 5858);
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/** 
setInt() - Set a TIMESTAMP parameter.
- Using an ordinal parameter
**/
    public void Var028 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setInt (17, 987946);
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/** 
setInt() - Set a DATALINK parameter.  Can't have a datalink parameter as an out parameter.
It is represented here by a varchar thus this test passes.
- Using an ordinal parameter
**/
    public void Var029 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setInt (18, 678);
                cs.execute();
                String check=cs.getString(18);
                assertCondition (check.equals("678"));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setInt() - Set a BIGINT parameter.
- Using an ordinal parameter
**/
    public void Var030 ()
    {
        if (checkJdbc20()) {
            if (checkBigintSupport()) {
                try {
                    cs.setInt (22, 104613);
                    cs.execute();
                    long check = cs.getLong(22);
                    assertCondition(check==104613);
                } catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }

/**
setInt() - Attempt to set the return value parameter.
- Using an ordinal parameter
**/
    public void Var031()
    {
        if (checkJdbc20()) {
            if (checkReturnValueSupport()) {
                try {
                    CallableStatement cs2 = connection_.prepareCall("?=CALL " + JDSetupProcedure.STP_CSRV);
                    cs2.setInt(1, 444);
                    cs2.close();
                    failed ("Didn't throw SQLException");
                } catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }

/**
setInt() - Should throw an exception when the callable 
statement is closed.
- Using a named parameter
**/
    public void Var032 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.close ();
                cs.setInt ("P_INTEGER", 533);
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setInt() - Should throw an exception when an invalid index is specified.
- Using a named parameter
**/
    public void Var033 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);

                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setInt ("PARAM_1", 524);
                cs.execute();
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/** 
setInt() - Should work with a valid parameter index.
- Using a named parameter
**/
    public void Var034 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setInt ("P_INTEGER", 585);
                cs.execute();
                int check=cs.getInt(2);
                assertCondition(check==585);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/** 
setInt() - Should throw an exception when the parameter is not an input parameter.
- Using a named parameter
**/
    public void Var035 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                CallableStatement cs1=connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + "( ?, ?, ?)");
                cs1.setInt("P2", 525);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setInt() - Set a SMALLINT parameter.
- Using a named parameter
**/
    public void Var036 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setInt ("P_SMALLINT", 2352);
                cs.execute();
                short check=cs.getShort(1);
                assertCondition(check==2352);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setInt() - Set a SMALLINT parameter when the index is too large.
- Using a named parameter
**/
    public void Var037 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setInt ("P_SMALLINT", 6568556);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
		boolean success = e.getMessage().indexOf("Data type mismatch") != -1;
		if (success) { 
		    assertCondition(true); 
		} else { 
		    failed(e, "Expected data type mismatch"); 
		} 


            }
        }
    }

/**
setInt() - Set an INTEGER parameter.
- Using a named parameter
**/
    public void Var038 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setInt ("P_INTEGER", 6585954);
                cs.execute();
                int check = cs.getInt(2);
                assertCondition(check==6585954);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setInt() - Set a REAL parameter.
- Using a named parameter
**/
    public void Var039 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setInt ("P_REAL", 487);
                cs.execute();
                float check=cs.getFloat(3);
                assertCondition(check==487f);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setInt() - Set a FLOAT parameter.
- Using a named parameter
**/
    public void Var040 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setInt ("P_FLOAT", 52646);
                cs.execute();
                double check=cs.getDouble(4);
                assertCondition(check==52646);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setInt() - Set a DOUBLE parameter.
- Using a named parameter
**/
    public void Var041 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setInt ("P_DOUBLE", 4521);
                cs.execute();
                double check=cs.getDouble(5);
                assertCondition(check==4521);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setInt() - Set a DECIMAL parameter.
- Using a named parameter
**/
    public void Var042 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setInt ("P_DECIMAL_105", -10012);
                cs.execute();
                BigDecimal check=cs.getBigDecimal(7);
                assertCondition(check.intValue()==-10012);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setInt() - Set a DECIMAL parameter, when the value is too big.
- Using a named parameter
**/
    public void Var043 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setInt ("P_DECIMAL_50", -100122123);
                cs.execute();
                failed("Didn't throw SQLException");
            } catch (Exception e) {
		if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
		    assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation now in toolbox");

		} else { 

		    assertExceptionIsInstanceOf (e, "java.sql.DataTruncation");
		}
            }
        }
    }

/**
setInt() - Set a Numeric parameter.
- Using a named parameter
**/
    public void Var044 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setInt ("P_NUMERIC_50", 12888);
                cs.execute();
                BigDecimal check=cs.getBigDecimal(8);
                assertCondition(check.intValue()==12888);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setInt() - Set a NUMERIC parameter, when the value is too big.
- Using a named parameter
**/
    public void Var045 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setInt ("P_NUMERIC_50", 1134567892);
                cs.execute();
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
		if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
		    assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation now in toolbox");

		} else { 

		    DataTruncation dt=(DataTruncation)e;
		    assertCondition ((dt.getIndex() == 8)
				     && (dt.getParameter()==true)
				     && (dt.getRead() ==false)
				     && (dt.getTransferSize()==5));
		}
            }
        }
    }

/**
setInt() - Set a CHAR(50) parameter.
- Using a named parameter
**/
    public void Var046 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setInt ("P_CHAR_50", 1547);
                cs.execute();
                String check=cs.getString(11);
                assertCondition(check.equals("1547                                              "));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setInt() - Set a CHAR(1) parameter.
- Using a named parameter
**/
    public void Var047 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setInt ("P_CHAR_1", 9);
                cs.execute();
                String check=cs.getString(10);
                assertCondition(check.equals("9"));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setInt() - Set a CHAR(1) parameter, when the value is too big.
- Using a named parameter
**/
    public void Var048 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setInt ("P_CHAR_1", 98);
                cs.execute();
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.DataTruncation");
            }
        }
    }

/**
setInt() - Set a VARCHAR parameter.
- Using a named parameter
**/
    public void Var049 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setInt ("P_VARCHAR_50", 5867);
                cs.execute();
                String check=cs.getString(12);
                assertCondition(check.equals("5867"));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setInt() - Set a CLOB parameter.
- Using a named parameter
**/
    public void Var050 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setInt ("P_CLOB", 5854);
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setInt() - Set a DBCLOB parameter.
- Using a named parameter
**/
    public void Var051 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setInt ("P_DBCLOB", 587465);
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setInt() - Set a BINARY parameter.
- Using a named parameter
**/
    public void Var052 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setInt ("P_BINARY_20", 121365);
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setInt() - Set a VARBINARY parameter.
- Using a named parameter
**/
    public void Var053 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setInt ("P_VARBINARY_20", 56789);
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setInt() - Set a BLOB parameter.
- Using a named parameter
**/
    public void Var054 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setInt ("P_BLOB", 9080);
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setInt() - Set a DATE parameter.
- Using a named parameter
**/
    public void Var055 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setInt ("P_DATE", 564);
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf ( e, "java.sql.SQLException");
            }
        }
    }

/**
setInt() - Set a TIME parameter.
- Using a named parameter 
**/
    public void Var056 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setInt ("P_TIME", 5858);
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/** 
setInt() - Set a TIMESTAMP parameter.
- Using a named parameter
**/
    public void Var057 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setInt ("P_TIMESTAMP", 987946);
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/** 
setInt() - Set a DATALINK parameter.  Can't have a datalink parameter as an out parameter.
It is represented here by a varchar thus this test passes.
- Using a named parameter
**/
    public void Var058 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setInt ("P_DATALINK", 678);
                cs.execute();
                String check=cs.getString(18);
                assertCondition (check.equals("678"));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setInt() - Set a BIGINT parameter.
- Using a named parameter
**/
    public void Var059 ()
    {
        if (checkNamedParametersSupport()) {
            if (checkBigintSupport()) {
                try {
                    cs.setInt ("P_BIGINT", 104613);
                    cs.execute();
                    long check = cs.getLong(22);
                    assertCondition(check==104613);
                } catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }

/**
setInt() - Set parameters for a callable statement using an ordinal and a named parameter
should throw an SQLException according to the specification, but we are not going to 
enforce it directly in the native JDBC driver.  How the Toolbox JDBC driver will handle the 
situation is not decided yet.

- Using an ordinal parameter
- Using a named parameter
**/
    public void Var060 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setInt (1, 5454);
                cs.setInt ("P_INTEGER", 54564);
                cs.execute();
                short check1 = cs.getShort(1);
                int check2 = cs.getInt(2);
                assertCondition ((check1 == 5454) && (check2==54564));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setInt() - Set the parameters and make sure the procedure runs correctly.
- Using an ordinal parameter
**/
    public void Var061 ()
    {
        if (checkJdbc20()) {
            try {
                CallableStatement cs1=connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + "( ?, ?, ?)");
                cs1.setInt(1, 43);
                cs1.registerOutParameter(2, Types.INTEGER);
                cs1.setInt(3, 54);
                cs1.registerOutParameter(3, Types.INTEGER);
                cs1.execute();
                int check1=cs1.getInt(2);       //P2 = P1 + 1
                int check2=cs1.getInt(3);       //P3 = P3 + 1
                assertCondition ( (check1==44) && (check2==55));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setInt() - Set the parameters and make sure the procedure runs correctly.
- Using a named parameter
**/
    public void Var062 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                CallableStatement cs1=connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + "( ?, ?, ?)");
                cs1.setInt("P1", 43);
                cs1.registerOutParameter(2, Types.INTEGER);
                cs1.setInt("P3", 54);
                cs1.registerOutParameter(3, Types.INTEGER);
                cs1.execute();
                int check1=cs1.getInt(2);       //P2 = P1 + 1
                int check2=cs1.getInt(3);       //P3 = P3 + 1
                assertCondition ( (check1==44) && (check2==55));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/** 
setInt() - Should work since the lower case name is not in quotes.
- Using a named parameter
**/
    public void Var063 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setInt ("p_integer", 585);
                cs.execute();
                int check=cs.getInt(2);
                assertCondition(check==585);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/** 
setInt() - Should work since the mixed case name is not in quotes.
- Using a named parameter
**/
    public void Var064 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setInt ("p_InTeGeR", 585);
                cs.execute();
                int check=cs.getInt(2);
                assertCondition(check==585);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/** 
setInt() - Should throw exception since the lower case name is in quotes.
- Using a named parameter
**/
    public void Var065 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setInt ("'p_integer'", 585);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/** 
setInt() - Should throw exception since the mixed case name is in quotes.
- Using a named parameter
**/
    public void Var066 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setInt ("'P_iNtEgEr'", 585);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }




    public void testNamedParameters(String procedureName,
					   String procedureDefinition,
					   String parameterName,
					   int parameterValue) {

	String added=" -- added by native driver 6/24/2015 to test named arguments";
	if (checkRelease710()) {

		try {
		    Statement stmt = connection_.createStatement();
		    try {
			stmt.executeUpdate("DROP PROCEDURE "+procedureName);
		    } catch (Exception e) {
		    // e.printStackTrace();
		    }
		    stmt.executeUpdate("CREATE PROCEDURE "+procedureName+ procedureDefinition);

		    CallableStatement ps;
		    ps = connection_.prepareCall( "CALL " + procedureName + "(?, "+parameterName+" => ?)");

		    ps.registerOutParameter(1,java.sql.Types.INTEGER);
		    ps.setInt (parameterName, parameterValue);

		    ps.execute();
		    String value=ps.getString(1);
		    String expectedValue = ""+parameterValue; 
		    stmt.executeUpdate("DROP PROCEDURE "+procedureName);
		    ps.close();
		    stmt.close(); 
		    assertCondition(expectedValue.equals(value), "value("+value+") != "+expectedValue+added);

		} catch (Exception e) {
		    failed (e, "Unexpected Exception "+added);
		}
	    }



    } 



    public void Var067() {
	testNamedParameters( JDCSTest.COLLECTION+".JDCSSI67",
			     "(OUT OUTARG INTEGER , IN INARG INTEGER ) BEGIN SET OUTARG=INARG; END ",
			     "INARG",
			     4); 

    } 

    
    
    public void Var068() {
  testNamedParameters( JDCSTest.COLLECTION+".JDCSSI68",
           "(OUT OUTARG INTEGER , IN INARG INTEGER, IN UNUSED1 INTEGER DEFAULT 0.0, IN UNUSED2 INTEGER DEFAULT 0.0, IN UNUSED3 INTEGER DEFAULT 0.0, IN UNUSED4 INTEGER DEFAULT 0.0, IN UNUSED5 INTEGER DEFAULT 0.0 ) BEGIN SET OUTARG=INARG; END ",
           "INARG",
           4); 

    } 

    
    public void Var069() {
  testNamedParameters( JDCSTest.COLLECTION+".JDCSSI69",
           "(OUT OUTARG INTEGER ,  IN UNUSED1 INTEGER DEFAULT 0.0, IN UNUSED2 INTEGER DEFAULT 0.0, IN INARG INTEGER, IN UNUSED3 INTEGER DEFAULT 0.0, IN UNUSED4 INTEGER DEFAULT 0.0, IN UNUSED5 INTEGER DEFAULT 0.0 ) BEGIN SET OUTARG=INARG; END ",
           "INARG",
		       7);


    } 

    
    public void Var070() {
  testNamedParameters( JDCSTest.COLLECTION+".JDCSSI70",
           "(OUT OUTARG INTEGER ,  IN UNUSED1 INTEGER DEFAULT 0.0, IN UNUSED2 INTEGER DEFAULT 0.0, IN UNUSED3 INTEGER DEFAULT 0.0, IN UNUSED4 INTEGER DEFAULT 0.0, IN UNUSED5 INTEGER DEFAULT 0.0, IN INARG INTEGER ) BEGIN SET OUTARG=INARG; END ",
           "INARG",
		       8);


    } 

   
    /**
     * setInt() - Set a BOOLEAN parameter. - Using a named parameter
     **/
    public void Var071() {

        if (checkBooleanSupport()) {
          setTestSuccessful("setInt", "P_BOOLEAN", "1", "1",
              " -- call setInt against BOOLEAN parameter -- added January 2021");
        }
    }

    
    /**
     * setInt() - Set a BOOLEAN parameter. - Using a named parameter
     **/
    public void Var072() {

        if (checkBooleanSupport()) {
          setTestSuccessful("setInt", "P_BOOLEAN", "0", "0",
              " -- call setInt against BOOLEAN parameter -- added January 2021");
        }
    }

    
    
}
