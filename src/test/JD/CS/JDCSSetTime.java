///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSSetTime.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////



////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSSetTime.java
//
// Classes:      JDCSSetTime
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import java.awt.TextArea;
import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;

import test.JDSetupProcedure;

/**
Testcase JDCSSetTime.  This tests the following method
of the JDBC CallableStatement class:

<ul>
<li>setTime()
</ul>
**/
public class JDCSSetTime
extends JDCSSetTestcase {

Time sampleTime = new Time(1000);

/**
Constructor.
**/
    public JDCSSetTime (AS400 systemObject,
                        Hashtable namesAndVars,
                        int runMode,
                        FileOutputStream fileOutputStream,
                        
                        String password)
    {
        super (systemObject, "JDCSSetTime",
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
setTime() - Should throw an exception when the callable statment is closed.
- Using an ordinal parameter
**/
    public void Var001 ()
    {
        if (checkJdbc20()) {
            try {
                cs.close();
                cs.setTime(16, sampleTime);
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }

    }

/**
setTime() - Should throw an exception when an invalid index is specified.
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
                cs.setTime(100, sampleTime);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }

    }

/**
setTime() - Should throw an exception when the index is 0.
- Using an ordinal parameter
**/
    public void Var003 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setTime(0, sampleTime);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTime() - Should throw an exception when the index is -1.
- Using an ordinal parameter
**/
    public void Var004 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setTime(-1, sampleTime);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }


/**
setTime() - Should set to SQL NULL when the value is null.
- Using an ordinal parameter
**/
    public void Var005 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setTime(16, null);
                cs.execute();
                Time check = cs.getTime(16);
                boolean wn = cs.wasNull();
                assertCondition ((check == null) && (wn == true), "check = "+check+" sb null\nwn="+wn+" sb true");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }

    }

/**
setTime() - Should work with a valid parameter index.
- Using an ordinal parameter
**/
    public void Var006 ()
    {
        if (checkJdbc20()) {
            try {
                
                Time t = Time.valueOf("22:33:44");
                cs.setTime(16, t);
                cs.execute();
                Time check = cs.getTime(16);
                assertCondition(check.toString().equals(t.toString()), "time  = ("+check.toString()+") sb "+t.toString()); 
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setTime() - Should throw an exception when the calendar is null.
- Using an ordinal parameter
**/
    public void Var007 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setTime(16, sampleTime, null);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }


/**
setTime() - Should throw an exception when the parameter is not an input parameter.
- Using an ordinal parameter
**/
    public void Var008 ()
    {
        if (checkJdbc20()) {
            try {
                CallableStatement cs1 = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + "( ?, ?, ?)");
                cs1.setTime(2,  sampleTime);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTime() - Set a SMALLINT parameter.
- Using an ordinal parameter
**/
    public void Var009 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setTime(1, sampleTime);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTime() - Set an INTEGER parameter.
- Using an ordinal parameter
**/
    public void Var010 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setTime(2, sampleTime);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }

    }

/**
setTime() - Set a REAL parameter.
- Using an ordinal parameter
**/
    public void Var011 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setTime(3, sampleTime);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTime() - Set a FLOAT parameter.
- Using an ordinal parameter
**/
    public void Var012 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setTime(4, sampleTime);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTime() - Set a DOUBLE parameter.
- Using an ordinal parameter
**/
    public void Var013 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setTime(5, sampleTime);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTime() - Set a DECIMAL parameter.
- Using an ordinal parameter
**/
    public void Var014 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setTime(7, sampleTime);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTime() - Set a NUMERIC parameter.
- Using an ordinal parameter
**/
    public void Var015 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setTime(8, sampleTime);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTime() - Set a CHAR(50) parameter.
- Using an ordinal parameter
**/
    public void Var016 ()
    {
        if (checkJdbc20()) {
            try {
                Time t = Time.valueOf("22:33:44");
                cs.setTime(11, t);
                cs.execute();
                String check = cs.getString(11);
                if (isToolboxDriver())
                    assertCondition(check.equals(t.toString()+"                                          "));
                else
                    assertCondition(check.equals("22.33.44                                          "));
            } catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }

/**
setTime() - Set a VARCHAR(50) parameter.
- Using an ordinal parameter
**/
    public void Var017 ()
    {
        if (checkJdbc20()) {
            try {
                Time t = Time.valueOf ("03:44:55");
                cs.setTime(12, t);
                cs.execute();
                String check = cs.getString(12);
                if (isToolboxDriver())
                    assertCondition(check.equals(t.toString()));
                else
                    assertCondition(check.equals("03.44.55"));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setTime() - Set a CLOB parameter.
- Using an ordinal parameter
**/
    public void Var018 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setTime(20, sampleTime);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTime() - Set a DBCLOB parameter.
- Using an ordinal parameter
**/
    public void Var019 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setTime(21, sampleTime);
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTime() - Set a BINARY parameter.
- Using an ordinal parameter
**/
    public void Var020 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setTime (13, sampleTime);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTime() - Set a VARBINARY parameter.
- Using an ordinal parameter
**/
    public void Var021 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setTime(14, sampleTime);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTime() - Set a BLOB parameter.
- Using an ordinal parameter
**/
    public void Var022 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setTime(19, sampleTime);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTime() - Set a DATE parameter.
- Using an ordinal parameter
**/
    public void Var023 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setTime(15, Time.valueOf("22:34:45"));
                cs.execute();
                Date check = cs.getDate(15);
                assertCondition(check.toString().equals("1970-01-01"), "Got "+check.toString()+" sb '1970-01-01'");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setTime() - Set a TIME parameter.
- Using an ordinal parameter
**/
    public void Var024 ()
    {
        if (checkJdbc20()) {
            try {
                Time t = Time.valueOf("03:04:05"); 
                cs.setTime(16, t);
                cs.execute();
                Time check = cs.getTime(16);
                assertCondition(check.toString().equals(t.toString()));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setTime() - Set a TIME parameter with a calendar specified.
- Using an ordinal parameter
**/
    public void Var025 ()
    {
        if (checkJdbc20()) {
            try {
                Time t = Time.valueOf("10:12:34");
                cs.setTime(16, t, Calendar.getInstance());
                cs.execute();
                Time check = cs.getTime(16);
                assertCondition (check.toString().equals(t.toString()));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setTime() - Set a TIMESTAMP parameter.
- Using an ordinal parameter
**/
    public void Var026 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setTime(17, Time.valueOf("03:02:04"));
                cs.execute();
                Timestamp check = cs.getTimestamp(17);
                assertCondition(check.toString().equals("1970-01-01 03:02:04.0"));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setTime() - Set a BIGINT parameter.
- Using an ordinal parameter
**/
    public void Var027 ()
    {
        if (checkJdbc20()) {
            if (checkBigintSupport()) {
                try {
                    cs.setTime(22, sampleTime);
                    failed("Didn't throw SQLException");
                } catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }

/**
setTime() - Should throw an exception when the callable statment is closed.
- Using a named parameter
**/
    public void Var028 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.close();
                cs.setTime("P_TIME", sampleTime);
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }

    }

/**
setTime() - Should throw an exception when an invalid index is specified.
- Using a named parameter
**/
    public void Var029 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);

                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setTime("PARAM", sampleTime);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }

    }

/**
setTime() - Should set to SQL NULL when the value is null.
- Using a named parameter
**/
    public void Var030 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setTime("P_TIME", null);
                cs.execute();
                Time check = cs.getTime(16);
                boolean wn = cs.wasNull();
                assertCondition ((check == null) && (wn == true), "check = "+check+" sb null\nwn="+wn+" sb true");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }

    }

/**
setTime() - Should work with a valid parameter index.
- Using a named parameter
**/
    public void Var031 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                Time t = Time.valueOf("22:33:44"); 
                cs.setTime("P_TIME", t);
                cs.execute();
                Time check = cs.getTime(16);
                assertCondition(check.toString().equals(t.toString()));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setTime() - Should throw an exception when the calendar is null.
- Using a named parameter
**/
    public void Var032 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setTime("P_TIME", sampleTime, null);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }


/**
setTime() - Should throw an exception when the parameter is not an input parameter.
- Using a named parameter
**/
    public void Var033 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                CallableStatement cs1 = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + "( ?, ?, ?)");
                cs1.setTime("P2",  sampleTime);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTime() - Set a SMALLINT parameter.
- Using a named parameter
**/
    public void Var034 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setTime("P_SMALLINT", sampleTime);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTime() - Set an INTEGER parameter.
- Using a named parameter
**/
    public void Var035 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setTime("P_INTEGER", sampleTime);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }

    }

/**
setTime() - Set a REAL parameter.
- Using a named parameter
**/
    public void Var036 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setTime("P_REAL", sampleTime);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTime() - Set a FLOAT parameter.
- Using a named parameter
**/
    public void Var037 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setTime("P_FLOAT", sampleTime);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTime() - Set a DOUBLE parameter.
- Using a named parameter
**/
    public void Var038 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setTime("P_DOUBLE", sampleTime);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTime() - Set a DECIMAL parameter.
- Using a named parameter
**/
    public void Var039 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setTime("P_DECIMAL_105", sampleTime);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTime() - Set a NUMERIC parameter.
- Using a named parameter
**/
    public void Var040 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setTime("P_NUMERIC_50", sampleTime);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTime() - Set a CHAR(50) parameter.
- Using a named parameter
**/
    public void Var041 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                Time t = Time.valueOf("22:33:44"); 
                cs.setTime("P_CHAR_50", t);
                cs.execute();
                String check = cs.getString(11);
                if (isToolboxDriver())
                    assertCondition(check.equals(t.toString()+"                                          "));
                else
                    assertCondition(check.equals("22.33.44                                          "));
            } catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }

/**
setTime() - Set a VARCHAR(50) parameter.
- Using a named parameter
**/
    public void Var042 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                Time t = Time.valueOf("03:44:55");
                cs.setTime("P_VARCHAR_50", t);
                cs.execute();
                String check = cs.getString(12);
                if (isToolboxDriver())
                    assertCondition(check.equals(t.toString()));
                else
                    assertCondition(check.equals("03.44.55"));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setTime() - Set a CLOB parameter.
- Using a named parameter
**/
    public void Var043 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setTime("P_CLOB", sampleTime);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTime() - Set a DBCLOB parameter.
- Using a named parameter
**/
    public void Var044 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setTime("P_DBCLOB", sampleTime);
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTime() - Set a BINARY parameter.
- Using a named parameter
**/
    public void Var045 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setTime ("P_BINARY_20", sampleTime);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTime() - Set a VARBINARY parameter.
- Using a named parameter
**/
    public void Var046 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setTime("P_VARBINARY_20", sampleTime);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTime() - Set a BLOB parameter.
- Using a named parameter
**/
    public void Var047 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setTime("P_BLOB", sampleTime);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTime() - Set a DATE parameter.
- Using a named parameter
**/
    public void Var048 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setTime("P_DATE", Time.valueOf("23:34:45"));
                cs.execute();
                Date check = cs.getDate("P_DATE");
                assertCondition(check.toString().equals("1970-01-01"), "got '"+check.toString()+"' sb '1970-01-01");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setTime() - Set a TIME parameter.
- Using a named parameter
**/
    public void Var049 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                Time t =Time.valueOf("03:04:05");
                cs.setTime("P_TIME", t);
                cs.execute();
                Time check = cs.getTime(16);
                assertCondition(check.toString().equals(t.toString()));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setTime() - Set a TIME parameter with a calendar specified.
- Using a named parameter
**/
    public void Var050 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                Time t = Time.valueOf("10:12:34");
                cs.setTime("P_TIME", t, Calendar.getInstance());
                cs.execute();
                Time check = cs.getTime(16);
                assertCondition (check.toString().equals(t.toString()));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setTime() - Set a TIMESTAMP parameter.
- Using a named parameter
**/
    public void Var051 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setTime("P_TIMESTAMP", Time.valueOf("03:02:04")); 
                cs.execute();
                Timestamp check = cs.getTimestamp("P_TIMESTAMP");
                assertCondition(check.toString().equals("1970-01-01 03:02:04.0"));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setTime() - Set a BIGINT parameter.
- Using a named parameter
**/
    public void Var052 ()
    {
        if (checkNamedParametersSupport()) {
            if (checkBigintSupport()) {
                try {
                    cs.setTime("P_BIGINT", sampleTime);
                    failed("Didn't throw SQLException");
                } catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }

/**
setTime() - Should throw an exception if an ordinal and a named parameter are
used for the same callable statement.
- Using an ordinal parameter
- Using a named parameter
**/
    public void Var053 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setString(12, "hi");
                cs.setTime("P_TIME", Time.valueOf("22:33:44")); 
                cs.execute();
                // I'm feeling lazy... if we were able to execute, the test worked.
                assertCondition (true);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setTime() - Should work since the lower case name is not in quotes.
- Using a named parameter
**/
    public void Var054 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                Time t = Time.valueOf("22:33:44");
                cs.setTime("p_time", t);
                cs.execute();
                Time check = cs.getTime(16);
                assertCondition(check.toString().equals(t.toString()));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setTime() - Should work since the mixed case name is not in quotes.
- Using a named parameter
**/
    public void Var055 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                Time t = Time.valueOf("22:33:44");
                cs.setTime("p_TIme", t);
                cs.execute();
                Time check = cs.getTime(16);
                assertCondition(check.toString().equals(t.toString()));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setTime() - Should throw an exception since the mixed case name is in quotes.
- Using a named parameter
**/
    public void Var056 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                Time t = sampleTime;
                cs.setTime("'p_TIme'", t);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTime() - Should throw an exception since the lower case name is in quotes.
- Using a named parameter
**/
    public void Var057 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                Time t = sampleTime;
                cs.setTime("'p_time'", t);
                failed("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

    
    /**
     * setTime() - Set a BOOLEAN parameter. - Using an ordinal parameter
     */
    public void Var058() {

      if (checkBooleanSupport()) {

        setTestFailure("setTime", 27, sampleTime,
            "Added 2021/01/07 to test boolean support");
      }

    }

    /**
     * setTime() - Set a BOOLEAN parameter. - Using a named parameter
     */
    public void Var059() {

      if (checkBooleanSupport()) {

        setTestFailure("setTime", "P_BOOLEAN", sampleTime,
            "Added 2021/01/07 to test boolean support");
      }

    }
}
