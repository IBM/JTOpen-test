///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSSetBigDecimal.java
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
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSSetBigDecimal.java
//
// Classes:      JDCSSetBigDecimal
//
////////////////////////////////////////////////////////////////////////
//
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import java.awt.TextArea;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.Statement;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JVMInfo;



/**
Testcase JDCSSetBigDecimal.  This tests the following method
of the JDBC CallableStatement class:

<ul>
<li>setBigDecimal()
</ul>
**/
public class JDCSSetBigDecimal
extends JDCSSetTestcase
{



    
    int jdk_; 



/**
Constructor.
**/
    public JDCSSetBigDecimal (AS400 systemObject,
                              Hashtable namesAndVacs,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password)
    {
        super (systemObject, "JDCSSetBigDecimal",
               namesAndVacs, runMode, fileOutputStream,
               password);
    }



/**
Performs setup needed before running variations.

@exception Exception If an exception occucs.
**/
    protected void setup ()
    throws Exception
    {


	jdk_ = JVMInfo.getJDK(); 
	  super.setup(); 
	      }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occucs.
**/
    protected void cleanup ()
    throws Exception
    {
      super.cleanup(); 
    }



/**
Compares a BigDecimal with a double, and allows a little rounding error.
**/
    private boolean compare (BigDecimal bd, double d)
    {
        return(Math.abs (bd.doubleValue () - d) < 0.001);
    }



/**
setBigDecimal() - Should throw exception when the prepared
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
                cs.setBigDecimal (6, new BigDecimal (0.43453));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setBigDecimal() - Should throw exception when an invalid index is
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
                cs.setBigDecimal (100, new BigDecimal (4.43233));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setBigDecimal() - Should throw exception when index is 0.
- Using an ordinal parameter
**/
    public void Var003()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setBigDecimal (0, new BigDecimal (-22.4538));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setBigDecimal() - Should throw exception when index is -1.
- Using an ordinal parameter
**/
    public void Var004()
    {
        if(checkJdbc20())
        {

            try
            {
                cs.setBigDecimal (-1, new BigDecimal (22.594));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setBigDecimal() - Should work with a valid parameter index
greater than 1.
- Using an ordinal parameter
**/
    public void Var005()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setBigDecimal (7, new BigDecimal ("-423.43"));
                cs.execute ();
                BigDecimal check = cs.getBigDecimal (7);
                assertCondition (compare (check, -423.43));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setBigDecimal() - Should set to SQL NULL when null is passed.
- Using an ordinal parameter
**/
    public void Var006()
    {

        if(checkJdbc20())
        {
            try
            {
                cs.setBigDecimal (6, null);
                cs.execute ();
                BigDecimal check = cs.getBigDecimal (6);
                boolean wn = cs.wasNull ();
                assertCondition ((check == null) && (wn == true));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setBigDecimal() - Should throw exception when the parameter is
not an input parameter.
- Using an ordinal parameter
**/
    public void Var007()
    {
        if(checkJdbc20())
        {
            try
            {
                CallableStatement cs1 = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + "( ?, ?, ?)");
                cs1.setBigDecimal (2, new BigDecimal (22.522));
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
setBigDecimal() - Set a SMALLINT parameter.
- Using an ordinal parameter
**/
    public void Var008()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setBigDecimal (1, new BigDecimal ("264"));
                cs.execute ();
                short check = cs.getShort (1);
                assertCondition (check == 264);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setBigDecimal() - Set a SMALLINT parameter, when there is a decimal part.  This will work
by silently truncating the data.
- Using an ordinal parameter
**/
    public void Var009()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setBigDecimal (1, new BigDecimal (12.2222));
                cs.execute ();
                short check = cs.getShort (1);
                assertCondition (check == 12);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setBigDecimal() - Set a SMALLINT parameter, when the value is too large.  This
will result in a DataTruncation exception.
- Using an ordinal parameter
**/
    public void Var010()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setBigDecimal (1, new BigDecimal ("12020282"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
		  assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation in V7R1");
            }
        }
    }



/**
setBigDecimal() - Set an INTEGER parameter.
- Using an ordinal parameter
**/
    public void Var011()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setBigDecimal (2, new BigDecimal ("6793"));
                cs.execute ();
                int check = cs.getInt (2);
                assertCondition (check == 6793);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setBigDecimal() - Set an INTEGER parameter, when there is a decimal part.  This will work
by silently truncating the data.
- Using an ordinal parameter
**/
    public void Var012()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setBigDecimal (2, new BigDecimal (6.83));
                cs.execute ();
                int check = cs.getInt (2);
                assertCondition (check == 6);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setBigDecimal() - Set an INTEGER parameter, when the value is too large.  This
will result in a DataTruncation exception.
- Using an ordinal parameter
**/
    public void Var013()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setBigDecimal (2, new BigDecimal ("1484573742020282"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {

		  assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation for all JDBC drivers -- update 07/14/2014");

            }
        }
    }



/**
setBigDecimal() - Set a REAL parameter.
- Using an ordinal parameter
**/
    public void Var014()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setBigDecimal (3, new BigDecimal ("-92.2497"));
                cs.execute ();
                float check = cs.getFloat (3);
                assertCondition (check == -92.2497f);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setBigDecimal() - Set a FLOAT parameter.
- Using an ordinal parameter
**/
    public void Var015()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setBigDecimal (4, new BigDecimal (0.123));
                cs.execute ();
                double check = cs.getDouble (4);
                assertCondition (check == 0.123);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setBigDecimal() - Set an DOUBLE parameter.
- Using an ordinal parameter
**/
    public void Var016()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setBigDecimal (5, new BigDecimal (0.222903441));
                cs.execute ();
                double check = cs.getDouble (5);
                assertCondition (check == 0.222903441);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setBigDecimal() - Set an DECIMAL parameter.
- Using an ordinal parameter
**/
    public void Var017()
    {

        if(checkJdbc20())
        {
            try
            {
                cs.setBigDecimal (7, new BigDecimal ("4001.23421"));
                cs.execute ();
                BigDecimal check = cs.getBigDecimal (7);
                assertCondition (compare (check, 4001.23421));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setBigDecimal() - Set an DECIMAL parameter, when the value is too big.
- Using an ordinal parameter
**/
    public void Var018()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setBigDecimal (6, new BigDecimal ("324512.21"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
		if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
		    assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation now in toolbox");

		} else { 

                DataTruncation dt = (DataTruncation)e;
                assertCondition ((dt.getIndex() == 6)
                        && (dt.getParameter() == true)
                        && (dt.getRead() == false)
                        && (dt.getTransferSize() == 5));
		}
            }
        }
    }



/**
setBigDecimal() - Set an DECIMAL parameter, where only the BigDecimal's
fraction truncates.  This should work.
- Using an ordinal parameter
**/
    public void Var019()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setBigDecimal (6, new BigDecimal ("40013.23421"));
                cs.execute ();
                BigDecimal check = cs.getBigDecimal (6);
                assertCondition (compare (check, 40013));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setBigDecimal() - Set an NUMERIC parameter.
- Using an ordinal parameter
**/
    public void Var020()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setBigDecimal (9, new BigDecimal ("7742"));
                cs.execute ();
                BigDecimal check = cs.getBigDecimal (9);
                assertCondition (compare (check, 7742));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setBigDecimal() - Set an NUMERIC parameter, when the value is too big
SQL400 - right now the Native JDBC driver data truncation exceptions report the
data size and transfer size for the precision - the scale.  This is because the
the scale portion can be of any size and will be ignored.  Also, otherwise you
can get into situations where the needed size is actually smaller than than
the available size which could be confusing.
- Using an ordinal parameter
**/
    public void Var021()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setBigDecimal (9, new BigDecimal ("2173345334.42"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
	    {
		if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
		    assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation now in toolbox");

		} else { 

		    DataTruncation dt = (DataTruncation)e;
		    if(isToolboxDriver())
			assertCondition ((dt.getIndex() == 9)
					 && (dt.getParameter() == true)
					 && (dt.getRead() == false)
					 && (dt.getTransferSize() == 10));
		    else {
			if (getDriver() == JDTestDriver.DRIVER_NATIVE && getDriverFixLevel() < 49160) {
			    notApplicable("Native driver fix in PTF V7R1 SI49160" );
			    return; 
			}

			assertCondition ((dt.getIndex() == 9)
					 && (dt.getParameter() == true)
					 && (dt.getRead() == false)
					 && (dt.getDataSize() == 15)
					 && (dt.getTransferSize() == 10),
					 "dt.getIndex()="+dt.getIndex()+" sb 9 "+
					 "dt.getParameter()="+dt.getParameter()+" sb true "+
					 "dt.getRead()="+dt.getRead()+" sb false "+
					 "dt.getDataSize()="+dt.getDataSize()+" sb 15 "+
					 "dt.getTransferSize()="+dt.getTransferSize()+" sb 10  Updated 1/18/2013 for V7R1 fix"  );
		    }
		}
	    }
        }
    }



/**
setBigDecimal() - Set a NUMERIC parameter, where only the BigDecimal's
fraction truncates.  This should work.
- Using an ordinal parameter
**/
    public void Var022()
    {
        if(checkJdbc20())
        {
            try
            {
            //had to open new callable statement to get this to work properly
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBigDecimal (8, new BigDecimal (-40013.23421));
                cs.execute ();
                BigDecimal check = cs.getBigDecimal (8);
                assertCondition (compare (check, -40013));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setBigDecimal() - Set an CHAR(50) parameter.
- Using an ordinal parameter
**/
    public void Var023()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setBigDecimal (11, new BigDecimal ("-1842.7993322"));
                cs.execute ();
                String check = cs.getString (11);
                assertCondition (check.equals("-1842.7993322                                     "));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setBigDecimal() - Set an CHAR(1) parameter.
- Using an ordinal parameter
**/
    public void Var024()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setBigDecimal (10, new BigDecimal ("8"));
                cs.execute ();
                String check = cs.getString (10);
                assertCondition (check.equals("8"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setBigDecimal() - Set an CHAR(1) parameter, when the value is too big.
- Using an ordinal parameter
**/
    public void Var025()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setBigDecimal (10, new BigDecimal (533.25));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.DataTruncation");
            }
        }
    }



/**
setBigDecimal() - Set an VARCHAR parameter.
- Using an ordinal parameter
**/
    public void Var026()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setBigDecimal (12, new BigDecimal ("0.8766753"));
                cs.execute ();
                String check = cs.getString (12);
                assertCondition (check.equals("0.8766753"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setBigDecimal() - Set a CLOB parameter.
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
                    cs.setBigDecimal (20, new BigDecimal (542.36));
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
setBigDecimal() - Set a DBCLOB parameter.
- Using an ordinal parameter
**/
    public void Var028()
    {
        if(checkJdbc20())
        {
            if(checkLobSupport ())
            {
                try
                {
                    cs.setBigDecimal (21, new BigDecimal (5.4324));
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
setBigDecimal() - Set a BINARY parameter.
- Using an ordinal parameter
**/
    public void Var029()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setBigDecimal (13, new BigDecimal (45.445));
                cs.execute ();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }




/**
setBigDecimal() - Set a VARBINARY parameter.
- Using an ordinal parameter
**/
    public void Var030()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setBigDecimal (14, new BigDecimal (0.94431));
                cs.execute ();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }




/**
setBigDecimal() - Set a BLOB parameter.
- Using an ordinal parameter
**/
    public void Var031()
    {
        if(checkJdbc20())
        {
            try
            {
		//@B0
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBigDecimal (19, new BigDecimal (-54.433));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }




/**
setBigDecimal() - Set a DATE parameter.
- Using an ordinal parameter
**/
    public void Var032()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setBigDecimal (15, new BigDecimal (-6.54));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setBigDecimal() - Set a TIME parameter.
- Using an ordinal parameter
**/
    public void Var033()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setBigDecimal (16, new BigDecimal (-0.31));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setBigDecimal() - Set a TIMESTAMP parameter.
- Using an ordinal parameter
**/
    public void Var034()
    {
        if(checkJdbc20())
        {
            try
            {
                cs.setBigDecimal (17, new BigDecimal ("-544"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


/**
setBigDecimal() - Set a DATALINK parameter.
It is illegal to have a out DATALINK parameter, so it is represented in the database
as a VARCHAR(200). That is why the test passes.
- Using an ordinal parameter
**/
    public void Var035()
    {
        if(checkJdbc20())
        {
            if(checkLobSupport ())
            {
                try
                {
                    cs.setBigDecimal (18, new BigDecimal ("5.543"));
                    cs.execute();
                    String check=cs.getString(18);
                    assertCondition(check.equals("5.543"));
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



/**
setBigDecimal() - Set a BIGINT parameter.
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
                    cs.setBigDecimal (22, new BigDecimal ("-67393"));
                    cs.execute ();
                    long check = cs.getLong (22);
                    assertCondition (check == -67393);
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



/**
setBigDecimal() - Set a BIGINT parameter, when there is a decimal part.  This will work
by silently truncating the data.
- Using an ordinal parameter
**/
    public void Var037()
    {
        if(checkJdbc20())
        {
            if(checkBigintSupport())
            {
                try
                {
                    cs.setBigDecimal (22, new BigDecimal (-6.834));
                    cs.execute ();
                    long check = cs.getLong (22);
                    assertCondition (check == -6);
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



/**
setBigDecimal() - Set a BIGINT parameter, when the value is too large.  This
will result in a DataTruncation exception.
- Using an ordinal parameter
**/
    public void Var038()
    {
        if(checkJdbc20())
        {
            if(checkBigintSupport())
            {
                try
                {
                    cs.setBigDecimal (22, new BigDecimal ("9999999999999999999494"));
                    failed ("Didn't throw SQLException");
                }
                catch(Exception e)
                {
			assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation in for all JDBC drivers ");

                }
            }
        }
    }

/**
setBigDecimal() - Should throw exception when the prepared
statement is closed.
- Using a named parameter
**/
    public void Var039()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.close ();
                cs.setBigDecimal ("P_DECIMAL_50", new BigDecimal (0.43453));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setBigDecimal() - Should throw exception when an invalid index is
specified.
- Using a named parameter
**/


    public void Var040()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBigDecimal ("PARAM_1", new BigDecimal (4.43233));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setBigDecimal() - Should work with a valid parameter index
greater than 1.
- Using a named parameter
**/
    public void Var041()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setBigDecimal ("P_DECIMAL_105", new BigDecimal ("-423.43"));
                cs.execute ();
                BigDecimal check = cs.getBigDecimal (7);
                assertCondition (compare (check, -423.43));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setBigDecimal() - Should set to SQL NULL when null is passed.
- Using a named parameter
**/
    public void Var042()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setBigDecimal ("P_DECIMAL_50", null);
                cs.execute ();
                BigDecimal check = cs.getBigDecimal (6);
                boolean wn = cs.wasNull ();
                assertCondition ((check == null) && (wn == true));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setBigDecimal() - Should throw exception when the parameter is
not an input parameter.
- Using a named parameter
**/
    public void Var043()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                CallableStatement cs1 = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + "( ?, ?, ?)");    //cs.setFloat (2, (short) 222);
                cs1.setBigDecimal ("P2", new BigDecimal (22.522));
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
setBigDecimal() - Set a SMALLINT parameter.
- Using a named parameter
**/
    public void Var044()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setBigDecimal ("P_SMALLINT", new BigDecimal ("264"));
                cs.execute ();
                short check = cs.getShort (1);
                assertCondition (check == 264);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setBigDecimal() - Set a SMALLINT parameter, when there is a decimal part.  This will work
by silently truncating the data.
- Using a named parameter
**/
    public void Var045()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setBigDecimal ("P_SMALLINT", new BigDecimal (12.2222));
                cs.execute ();
                short check = cs.getShort (1);
                assertCondition (check == 12);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setBigDecimal() - Set a SMALLINT parameter, when the value is too large.  This
will result in a DataTruncation exception.
- Using a named parameter
**/
    public void Var046()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setBigDecimal ("P_SMALLINT", new BigDecimal ("12020282"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
		    assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation for all JDBC drivers 07/14/2014");

            }
        }
    }



/**
setBigDecimal() - Set an INTEGER parameter.
- Using a named parameter
**/
    public void Var047()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setBigDecimal ("P_INTEGER", new BigDecimal ("6793"));
                cs.execute ();
                int check = cs.getInt (2);
                assertCondition (check == 6793);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setBigDecimal() - Set an INTEGER parameter, when there is a decimal part.  This will work
by silently truncating the data.
- Using a named parameter
**/
    public void Var048()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setBigDecimal ("P_INTEGER", new BigDecimal (6.83));
                cs.execute ();
                int check = cs.getInt (2);
                assertCondition (check == 6);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setBigDecimal() - Set an INTEGER parameter, when the value is too large.  This
will result in a DataTruncation exception.
- Using a named parameter
**/
    public void Var049()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setBigDecimal ("P_INTEGER", new BigDecimal ("1484573742020282"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {

		    assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation for all JDBC drivers");

            }
        }
    }



/**
setBigDecimal() - Set a REAL parameter.
- Using a named parameter
**/
    public void Var050()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setBigDecimal ("P_REAL", new BigDecimal ("-92.2497"));
                cs.execute ();
                float check = cs.getFloat (3);
                assertCondition (check == -92.2497f);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setBigDecimal() - Set a FLOAT parameter.
- Using a named parameter
**/
    public void Var051()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setBigDecimal ("P_FLOAT", new BigDecimal (0.123));
                cs.execute ();
                double check = cs.getDouble (4);
                assertCondition (check == 0.123);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setBigDecimal() - Set an DOUBLE parameter.
- Using a named parameter
**/
    public void Var052()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setBigDecimal ("P_DOUBLE", new BigDecimal (0.222903441));
                cs.execute ();
                double check = cs.getDouble (5);
                assertCondition (check == 0.222903441);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setBigDecimal() - Set an DECIMAL parameter.
- Using a named parameter
**/
    public void Var053()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setBigDecimal ("P_DECIMAL_105", new BigDecimal ("4001.23421"));
                cs.execute ();
                BigDecimal check = cs.getBigDecimal (7);
                assertCondition (compare (check, 4001.23421));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setBigDecimal() - Set an DECIMAL parameter, when the value is too big.
- Using a named parameter
**/
    public void Var054()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setBigDecimal ("P_DECIMAL_50", new BigDecimal ("324512.21"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
		if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
		    assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation now in toolbox");

		} else { 
		    DataTruncation dt = (DataTruncation)e;
		    assertCondition ((dt.getIndex() == 6)
				     && (dt.getParameter() == true)
				     && (dt.getRead() == false)
				     && (dt.getTransferSize() == 5));
		}
            }
        }
    }



/**
setBigDecimal() - Set an DECIMAL parameter, where only the BigDecimal's
fraction truncates.  This should work.
- Using a named parameter
**/
    public void Var055()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setBigDecimal ("P_DECIMAL_50", new BigDecimal ("40013.23421"));
                cs.execute ();
                BigDecimal check = cs.getBigDecimal (6);
                assertCondition (compare (check, 40013));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setBigDecimal() - Set an NUMERIC parameter.
- Using a named parameter
**/
    public void Var056()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setBigDecimal ("P_NUMERIC_105", new BigDecimal ("7742"));
                cs.execute ();
                BigDecimal check = cs.getBigDecimal (9);
                assertCondition (compare (check, 7742));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setBigDecimal() - Set an NUMERIC parameter, when the value is too big
SQL400 - right now the Native JDBC driver data truncation exceptions report the
data size and transfer size for the precision - the scale.  This is because the
the scale portion can be of any size and will be ignored.  Also, otherwise you
can get into situations where the needed size is actually smaller than than
the available size which could be confusing.
- Using a named parameter
**/
    public void Var057()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setBigDecimal ("P_NUMERIC_105", new BigDecimal ("2173345334.42"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
	    {
		if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
		    assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation now in toolbox");

		} else { 

		    DataTruncation dt = (DataTruncation)e;
		    if(isToolboxDriver())
			assertCondition ((dt.getIndex() == 9)
					 && (dt.getParameter() == true)
					 && (dt.getRead() == false)
					 && (dt.getTransferSize() == 10));
		    else {

			if (getDriver() == JDTestDriver.DRIVER_NATIVE && getDriverFixLevel() < 49160) {
			    notApplicable("Native driver fix in PTF V7R1 SI49160" );
			    return; 
			}

			assertCondition ((dt.getIndex() == 9)
					 && (dt.getParameter() == true)
					 && (dt.getRead() == false)
					 && (dt.getDataSize() == 15)
					 && (dt.getTransferSize() == 10),
					 "\ndt.getIndex()="+dt.getIndex()+" sb 9 "+
					 "\ndt.getParameter()="+dt.getParameter()+" sb true "+
					 "\ndt.getRead()="+dt.getRead()+" sb false "+
					 "\ndt.getDataSize()="+dt.getDataSize()+" sb 15 "+
					 "\ndt.getTransferSize()="+dt.getTransferSize()+" sb 10 " +
					 "\nUpdated 1/18/2013 for V7R1 fix" );
		    }

		}
	    }
        }
    }



/**
setBigDecimal() - Set a NUMERIC parameter, where only the BigDecimal's
fraction truncates.  This should work.
- Using a named parameter
**/
    public void Var058()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
            //had to open new callable statement to get this to work properly
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBigDecimal ("P_NUMERIC_50", new BigDecimal (-40013.23421));
                cs.execute ();
                BigDecimal check = cs.getBigDecimal (8);
                assertCondition (compare (check, -40013));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setBigDecimal() - Set an CHAR(50) parameter.
- Using a named parameter
**/
    public void Var059()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setBigDecimal ("P_CHAR_50", new BigDecimal ("-1842.7993322"));
                cs.execute ();
                String check = cs.getString (11);
                assertCondition (check.equals("-1842.7993322                                     "));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setBigDecimal() - Set an CHAR(1) parameter.
- Using a named parameter
**/
    public void Var060()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setBigDecimal ("P_CHAR_1", new BigDecimal ("8"));
                cs.execute ();
                String check = cs.getString (10);
                assertCondition (check.equals("8"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setBigDecimal() - Set an CHAR(1) parameter, when the value is too big.
- Using a named parameter
**/
    public void Var061()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setBigDecimal ("P_CHAR_1", new BigDecimal (533.25));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.DataTruncation");
            }
        }
    }



/**
setBigDecimal() - Set an VARCHAR parameter.
- Using a named parameter
**/
    public void Var062()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setBigDecimal ("P_VARCHAR_50", new BigDecimal ("0.8766753"));
                cs.execute ();
                String check = cs.getString (12);
                assertCondition (check.equals("0.8766753"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setBigDecimal() - Set a CLOB parameter.
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
                    cs.setBigDecimal ("P_CLOB", new BigDecimal (542.36));
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
setBigDecimal() - Set a DBCLOB parameter.
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
                    cs.setBigDecimal ("P_DBCLOB", new BigDecimal (5.4324));
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
setBigDecimal() - Set a BINARY parameter.
- Using a named parameter
**/
    public void Var065()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setBigDecimal ("P_BINARY_20", new BigDecimal (45.445));
                cs.execute ();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }




/**
setBigDecimal() - Set a VARBINARY parameter.
- Using a named parameter
**/
    public void Var066()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setBigDecimal ("P_VARBINARY_20", new BigDecimal (0.94431));
                cs.execute ();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }




/**
setBigDecimal() - Set a BLOB parameter.
- Using a named parameter
**/
    public void Var067()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
		//@B0
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBigDecimal ("P_BLOB", new BigDecimal (-54.433));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }




/**
setBigDecimal() - Set a DATE parameter.
- Using a named parameter
**/
    public void Var068()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setBigDecimal ("P_DATE", new BigDecimal (-6.54));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setBigDecimal() - Set a TIME parameter.
- Using a named parameter
**/
    public void Var069()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setBigDecimal ("P_TIME", new BigDecimal (-0.31));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setBigDecimal() - Set a TIMESTAMP parameter.
- Using a named parameter
**/
    public void Var070()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setBigDecimal ("P_TIMESTAMP", new BigDecimal ("-544"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


/**
setBigDecimal() - Set a DATALINK parameter.
It is illegal to have a out DATALINK parameter, so it is represented in the database
as a VARCHAR(200).  That is why the test passes.
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
                    cs.setBigDecimal ("P_DATALINK", new BigDecimal ("5.543"));
                    cs.execute();
                    String check=cs.getString(18);
                    assertCondition(check.equals("5.543"));
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



/**
setBigDecimal() - Set a BIGINT parameter.
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
                    cs.setBigDecimal ("P_BIGINT", new BigDecimal ("-67393"));
                    cs.execute ();
                    long check = cs.getLong (22);
                    assertCondition (check == -67393);
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



/**
setBigDecimal() - Set a BIGINT parameter, when there is a decimal part.  This will work
by silently truncating the data.
- Using a named parameter
**/
    public void Var073()
    {
        if(checkNamedParametersSupport())
        {
            if(checkBigintSupport())
            {
                try
                {
                    cs.setBigDecimal ("P_BIGINT", new BigDecimal (-6.834));
                    cs.execute ();
                    long check = cs.getLong (22);
                    assertCondition (check == -6);
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



/**
setBigDecimal() - Set a BIGINT parameter, when the value is too large.  This
will result in a DataTruncation exception.
- Using a named parameter
**/
    public void Var074()
    {
        if(checkNamedParametersSupport())
        {
            if(checkBigintSupport())
            {
                try
                {
                    cs.setBigDecimal ("P_BIGINT", new BigDecimal ("9999999999999999999494"));
                    failed ("Didn't throw SQLException");
                }
                catch(Exception e)
                {

			assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation fpr a;; driver 7/14/2014");
                }
            }
        }
    }

/**
setBigDecimal() - Should throw an exception if an ordinal and a named parameter are
used for the same statement.
- Using an ordinal parameter
- Using a named parameter
**/
    public void Var075 ()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setBigDecimal(6, new BigDecimal ("40013.23421"));
                cs.setBigDecimal("P_DECIMAL_105", new BigDecimal ("4001.23421"));
                cs.execute();
                succeeded();
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setBigDecimal() - Attempt to set the return value parameter.
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
                    cs2.setBigDecimal(1, new BigDecimal ("4001.23421"));
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
setBigDecimal() - Should be recognized since the lower case name is not in quotes.
- Using a named parameter
**/
    public void Var077()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setBigDecimal ("p_decimal_105", new BigDecimal ("-423.43"));
                cs.execute ();
                BigDecimal check = cs.getBigDecimal (7);
                assertCondition (compare (check, -423.43));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setBigDecimal() - Should be recognized since the mixed case name is not in quotes.
- Using a named parameter
**/
    public void Var078()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setBigDecimal ("p_DeCiMaL_105", new BigDecimal ("-423.43"));
                cs.execute ();
                BigDecimal check = cs.getBigDecimal (7);
                assertCondition (compare (check, -423.43));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setBigDecimal() - Should not be recognized since the lower case name is in quotes.
- Using a named parameter
**/
    public void Var079()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setBigDecimal ("'p_decimal_105'", new BigDecimal ("-423.43"));
                failed("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setBigDecimal() - Should not be recognized since the mixed case name is in quotes.
- Using a named parameter
**/
    public void Var080()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                cs.setBigDecimal ("'p_DeCiMaL_105'", new BigDecimal ("-423.43"));
                failed("Didn't throw SQLException");

            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

    /**
     * setBigDecimal
     *
     * set a DECIMAL field using a BigDecimal which in JDK 1.5 will be formatted
     * with an Exponent.  The toString method was changed in JDK 1.5 to
     * format a BigDecimal with an exponent.  Here is a snippet from the
     * java doc.
     *
     * Otherwise (that is, if the scale is negative, or the adjusted exponent
     * is less than -6), the number will be converted to a character form using
     *
     * The native PTR for this problem is 9B20698
     */

    public void Var081()
    {
      String added=" -- added by native driver 2/8/2006";

        if (checkBigintSupport()) {
            try {
                String procedureName = JDCSTest.COLLECTION +".JDCSBD081";
                Statement statement_ = connection_.createStatement();
                try {
                    statement_.executeUpdate("DROP PROCEDURE "+procedureName);
                } catch (Exception e) {
                    // e.printStackTrace();
                }
                statement_.executeUpdate("CREATE PROCEDURE "+procedureName+
                          " (in c1 DECIMAL(12,10), out c2 DECIMAL(12,10)) language sql  begin set c2=c1; end");

                CallableStatement ps;
                ps = connection_.prepareCall( "CALL " + procedureName + "(?,?)");
                ps.setBigDecimal (1, new BigDecimal ("0.00000016"));
                ps.registerOutParameter(2,java.sql.Types.VARCHAR);
                ps.execute();
                String value=ps.getString(2);
                String expectedValue = "1.600E-7";
                if(isToolboxDriver())
                    expectedValue = "0.0000001600";
                else if ((jdk_ == JVMInfo.JDK_13) || (jdk_ <= JVMInfo.JDK_142) || (getDriver() == JDTestDriver.DRIVER_NATIVE) ) {
		    /* In V5R3 JDK 1.5 native will return this */
                  expectedValue = "0.0000001600";

                }
                statement_.executeUpdate("DROP PROCEDURE "+procedureName);
                assertCondition(expectedValue.equals(value), "value("+value+") != "+expectedValue+added);

            } catch (Exception e) {
                failed (e, "Unexpected Exception "+added);
            }
        }
    }

    /**
     * set a DECIMAL field using a BigDecimal which in JDK 1.5 will be formatted
     * with a posite Exponent.  The toString method was changed in JDK 1.5 to
     * format a BigDecimal with an exponent.  Here is a snippet from the
     * java doc.
     *
     * Otherwise (that is, if the scale is negative, or the adjusted exponent
     * is less than -6), the number will be converted to a character form using
     *
     * The native PTR for this problem is 9B20698
     */

    public void Var082()
    {
      String added=" -- added by native driver 2/8/2006";
      if (checkJdbc20 ()) {
	  if (checkBigintSupport()) {
	      try {


		  String procedureName = JDCSTest.COLLECTION +".JDCSBD082";
		  Statement statement_ = connection_.createStatement();
		  try {
		      statement_.executeUpdate("DROP PROCEDURE "+procedureName);
		  } catch (Exception e) {
		    // e.printStackTrace();
		  }
		  statement_.executeUpdate("CREATE PROCEDURE "+procedureName+
					   " (in c1 DECIMAL(14,2), out c2 DECIMAL(14,2)) "+
					   " language sql begin set c2=c1; end");

		  CallableStatement ps;
		  ps = connection_.prepareCall( "CALL " + procedureName + "(?,?)");
		  BigDecimal bd;
		  if (jdk_ <= JVMInfo.JDK_142) {
		      bd = new BigDecimal("1600000000");
		  } else {
		      bd = new BigDecimal (new BigInteger("16"),-8 );
		  }

		  ps.setBigDecimal (1, bd);
		  ps.registerOutParameter(2,java.sql.Types.VARCHAR);
		  ps.execute();
		  String value=ps.getString(2);

		  String expectedValue = "1600000000.00";
		  if (jdk_ <= JVMInfo.JDK_142) {
		      expectedValue = "1600000000.00";
		  }
		  statement_.executeUpdate("DROP PROCEDURE "+procedureName);
		  assertCondition(expectedValue.equals(value), "value("+value+") != "+expectedValue+added);

	      } catch (Exception e) {
		  failed (e, "Unexpected Exception "+added);
	      }
	  }
      }
    }

    /**
     * set a NUMERIC field using a BigDecimal which in JDK 1.5 will be formatted
     * with an Exponent.  The toString method was changed in JDK 1.5 to
     * format a BigDecimal with an exponent.  Here is a snippet from the
     * java doc.
     *
     * Otherwise (that is, if the scale is negative, or the adjusted exponent
     * is less than -6), the number will be converted to a character form using
     *
     * The native PTR for this problem is 9B20698
     */

    public void Var083()
    {
      String added=" -- added by native driver 2/8/2006";

        if (checkBigintSupport()) {
            try {
                String procedureName = JDCSTest.COLLECTION +".JDCSBD083";
                Statement statement_ = connection_.createStatement();
                try {
                    statement_.executeUpdate("DROP PROCEDURE "+procedureName);
                } catch (Exception e) {
                    // e.printStackTrace();
                }
                statement_.executeUpdate("CREATE PROCEDURE "+procedureName+
                          " (in c1 DECIMAL(12,10), out c2 DECIMAL(12,10)) "+
                          "language SQL begin set c2=c1; end");

                CallableStatement ps;
                ps = connection_.prepareCall( "CALL " + procedureName + "(?,?)");
                ps.setBigDecimal (1, new BigDecimal ("0.00000016"));
                ps.registerOutParameter(2,java.sql.Types.VARCHAR);
                ps.execute();
                String value=ps.getString(2);

                String expectedValue = "1.600E-7";

                if(isToolboxDriver())
                    expectedValue = "0.0000001600";
                else if ((jdk_ <= JVMInfo.JDK_142) || (getDriver() == JDTestDriver.DRIVER_NATIVE) ) {
		  /* In V5R3 JDK 1.5 native will return this */
                  expectedValue = "0.0000001600";
                }
                statement_.executeUpdate("DROP PROCEDURE "+procedureName);
                assertCondition(expectedValue.equals(value), "value("+value+") != "+expectedValue+added);

            } catch (Exception e) {
                failed (e, "Unexpected Exception "+added);
            }
        }
    }

    /**
     * set a NUMERIC field using a BigDecimal which in JDK 1.5 will be formatted
     * with a positive Exponent.  The toString method was changed in JDK 1.5 to
     * format a BigDecimal with an exponent.  Here is a snippet from the
     * java doc.
     *
     * Otherwise (that is, if the scale is negative, or the adjusted exponent
     * is less than -6), the number will be converted to a character form using
     *
     * The native PTR for this problem is 9B20698
     */

    public void Var084()
    {
      String added=" -- added by native driver 2/8/2006";
      if (checkJdbc20 ()) {

	  if (checkBigintSupport()) {
	      try {

		  String procedureName = JDCSTest.COLLECTION +".JDCSBD084";
		  Statement statement_ = connection_.createStatement();
		  try {
		      statement_.executeUpdate("DROP PROCEDURE "+procedureName);
		  } catch (Exception e) {
		    // e.printStackTrace();
		  }
		  statement_.executeUpdate("CREATE PROCEDURE "+procedureName+
					   " (in c1 DECIMAL(14,2), out c2 DECIMAL(14,2)) "+
					   " language SQL begin set c2=c1; end");

		  CallableStatement ps;
		  ps = connection_.prepareCall( "CALL " + procedureName + "(?,?)");
		  BigDecimal bd;
		  if (jdk_ <= JVMInfo.JDK_142) {
		      bd = new BigDecimal("1600000000");
		  } else {
		      bd = new BigDecimal (new BigInteger("16"),-8 );
		  }

		  ps.setBigDecimal (1, bd);

		  ps.registerOutParameter(2,java.sql.Types.VARCHAR);
		  ps.execute();
		  String value=ps.getString(2);

		  String expectedValue = "1600000000.00";
		  if (jdk_ <= JVMInfo.JDK_142) {
		      expectedValue = "1600000000.00";
		  }
		  statement_.executeUpdate("DROP PROCEDURE "+procedureName);
		  assertCondition(expectedValue.equals(value), "value("+value+") != "+expectedValue+added);

	      } catch (Exception e) {
		  failed (e, "Unexpected Exception "+added);
	      }
	  }
      }
    }





    public void testNamedParameters(String procedureName,
					   String procedureDefinition,
					   String parameterName,
					   BigDecimal parameterValue) {

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

		    ps.registerOutParameter(1,java.sql.Types.DECIMAL);
		    ps.setBigDecimal (parameterName, parameterValue);

		    ps.execute();
		    String value=ps.getString(1);
		    String expectedValue = parameterValue.toString();
		    stmt.executeUpdate("DROP PROCEDURE "+procedureName);
		    ps.close();
		    stmt.close(); 
		    assertCondition(expectedValue.equals(value), "value("+value+") != "+expectedValue+added);

		} catch (Exception e) {
		    failed (e, "Unexpected Exception "+added);
		}
	    }



    } 



    public void Var085() {
	testNamedParameters( JDCSTest.COLLECTION+".JDCSSBD85",
			     "(OUT OUTARG DECIMAL(10,2) , IN INARG DECIMAL(10,2) ) BEGIN SET OUTARG=INARG; END ",
			     "INARG",
			     new BigDecimal("123.45"));

    } 

    
    
    public void Var086() {
  testNamedParameters( JDCSTest.COLLECTION+".JDCSSBD86",
           "(OUT OUTARG DECIMAL(10,2) , IN INARG DECIMAL(10,2), IN UNUSED1 DECIMAL(10,2) DEFAULT 0.0, IN UNUSED2 DECIMAL(10,2) DEFAULT 0.0, IN UNUSED3 DECIMAL(10,2) DEFAULT 0.0, IN UNUSED4 DECIMAL(10,2) DEFAULT 0.0, IN UNUSED5 DECIMAL(10,2) DEFAULT 0.0 ) BEGIN SET OUTARG=INARG; END ",
           "INARG",
           new BigDecimal("123.45"));

    } 

    
    public void Var087() {
  testNamedParameters( JDCSTest.COLLECTION+".JDCSSBD87",
           "(OUT OUTARG DECIMAL(10,2) ,  IN UNUSED1 DECIMAL(10,2) DEFAULT 0.0, IN UNUSED2 DECIMAL(10,2) DEFAULT 0.0, IN INARG DECIMAL(10,2), IN UNUSED3 DECIMAL(10,2) DEFAULT 0.0, IN UNUSED4 DECIMAL(10,2) DEFAULT 0.0, IN UNUSED5 DECIMAL(10,2) DEFAULT 0.0 ) BEGIN SET OUTARG=INARG; END ",
           "INARG",
           new BigDecimal("123.45"));

    } 

    
    public void Var088() {
  testNamedParameters( JDCSTest.COLLECTION+".JDCSSBD88",
           "(OUT OUTARG DECIMAL(10,2) ,  IN UNUSED1 DECIMAL(10,2) DEFAULT 0.0, IN UNUSED2 DECIMAL(10,2) DEFAULT 0.0, IN UNUSED3 DECIMAL(10,2) DEFAULT 0.0, IN UNUSED4 DECIMAL(10,2) DEFAULT 0.0, IN UNUSED5 DECIMAL(10,2) DEFAULT 0.0, IN INARG DECIMAL(10,2) ) BEGIN SET OUTARG=INARG; END ",
           "INARG",
           new BigDecimal("123.45"));

    } 

    /**
     * setBigDecimal() - Set a BOOLEAN parameter. - Using a named parameter
     **/
    public void Var089() {

        if (checkBooleanSupport()) {
          setTestSuccessful("setBigDecimal", "P_BOOLEAN", "1", "1",
              " -- call setBigDecimal against BOOLEAN parameter -- added January 2021");
        }
    }

    
    /**
     * setBigDecimal() - Set a BOOLEAN parameter. - Using a named parameter
     **/
    public void Var090() {

        if (checkBooleanSupport()) {
          setTestSuccessful("setBigDecimal", "P_BOOLEAN", "0", "0",
              " -- call setBigDecimal against BOOLEAN parameter -- added January 2021");
        }
    }

    
    
}
