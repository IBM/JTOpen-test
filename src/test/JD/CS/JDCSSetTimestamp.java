///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSSetTimestamp.java
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
// File Name:    JDCSSetTimestamp.java
//
// Classes:      JDCSSetTimestamp
//
////////////////////////////////////////////////////////////////////////
//
//
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCTimestamp;

import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDTestDriver;

import java.awt.TextArea;
import java.io.FileOutputStream;
import java.util.Hashtable;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.Statement;
import java.util.Calendar;
import java.sql.Timestamp;
import java.sql.Time;

/**
Testcase JDCSSetTimestamp.  This tests the following method
of the JDBC CallableStatement class:

<ul>
<li>setTimestamp()
</ul>
**/
public class JDCSSetTimestamp
extends JDCSSetTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSSetTimestamp";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCSTest.main(newArgs); 
   }




private Timestamp sampleTimestamp;

/**
Constructor.
**/
    public JDCSSetTimestamp (AS400 systemObject,
                             Hashtable namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             
                             String password)
    {
        super (systemObject, "JDCSSetTimestamp",
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
      sampleTimestamp = new Timestamp (234); 
          }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
      super.setup(); 
      ;
    }


/**
setTimestamp() - Should throw an exception if the callable statement is closed.
- Using an ordinal parameter
**/
    public void Var001 ()
    {
        if (checkJdbc20()) {
            try {
                cs.close();
                cs.setTimestamp(17, sampleTimestamp);
                failed("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }

    }

/**
setTimestamp() - Should throw an exception when an invalid index is specified.
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
                cs.setTimestamp(100, sampleTimestamp);
                failed("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTimestamp() - Should throw an exception when the index is 0.
- Using an ordinal parameter
**/
    public void Var003 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setTimestamp(0, sampleTimestamp);
                failed("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTimestamp() - Should throw an exception when the index is -1.
- Using an ordinal parameter
**/
    public void Var004 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setTimestamp(-1, sampleTimestamp);
                failed("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTimestamp() - Should set to SQL NULL when the value is null.
- Using an ordinal parameter
**/
    public void Var005 ()
   {
        if (checkJdbc20()) {
            try {
                cs.setTimestamp(17, null);
                cs.execute();
                Timestamp check = cs.getTimestamp(17);
                boolean wn = cs.wasNull();
                assertCondition((check == null) && (wn == true),"check is "+check+" sb null wn="+wn+" sb true");
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }

/**
setTimestamp() - Should work with a valid parameter index.
- Using an ordinal parameter
**/
    public void Var006 ()
    {
        if (checkJdbc20()) {
            try {
                Timestamp t = new Timestamp (444);
                cs.setTimestamp(17, t);
                cs.execute();
                Timestamp check = cs.getTimestamp(17);
                assertCondition (check.toString().equals(t.toString()));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setTimestamp() - Should throw an exception when the calendar is null.
- Using an ordinal parameter
**/
    public void Var007 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setTimestamp(17, sampleTimestamp, null);
                failed("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTimestamp() - Should throw an exception when the parameter is not an input parameter.
- Using an ordinal parameter
**/
    public void Var008 ()
    {
        if (checkJdbc20()) {
            try {
                CallableStatement cs1 = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + "( ?, ?, ?)");
                cs1.setTimestamp(2, sampleTimestamp);
                failed("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTimestamp() - Set a SMALLINT parameter.
- Using an ordinal parameter
**/
    public void Var009 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setTimestamp(1, sampleTimestamp);
                failed("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTimestamp() - Set an INTEGER parameter.
- Using an ordinal parameter
**/
    public void Var010 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setTimestamp (2, sampleTimestamp);
                failed("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTimestamp() - Set a REAL parameter.
- Using an ordinal parameter
**/
    public void Var011 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setTimestamp(3, sampleTimestamp);
                failed("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTimestamp() - Set a FLOAT parameter.
- Using an ordinal parameter
**/
    public void Var012 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setTimestamp(4, sampleTimestamp);
                failed("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTimestamp() - Set a DOUBLE parameter.
- Using an ordinal parameter
**/
    public void Var013()
    {
        if (checkJdbc20()) {
            try {
                cs.setTimestamp(5, sampleTimestamp);
                failed("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTimestamp() - Set a DECIMAL parameter.
- Using an ordinal parameter
**/
    public void Var014()
    {
        if (checkJdbc20()) {
            try {
                cs.setTimestamp(7, sampleTimestamp);
                failed("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTimestamp() - Set a NUMERIC parameter.
- Using an ordinal parameter
**/
    public void Var015 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setTimestamp(8, sampleTimestamp);
                failed("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTimestamp() - Set a CHAR(50) parameter.
- Using an ordinal parameter
**/
    public void Var016 ()
    {
        if (checkJdbc20()) {
	    String check = "";
	    String expected= "";
	    String tz=""; 
            try {
                Timestamp t = new Timestamp(4342343);
                cs.setTimestamp(11, t);
                cs.execute();
                check = cs.getString(11);
		// For group test timezone = UTC
		tz = System.getProperty("user.timezone");
		// 04/08/2013 -- toolbox driver now trims extra zeros on the end
                if ((isToolboxDriver()
		    && !("UTC".equals(tz))) || (System.getProperty("java.home").indexOf("openjdk")>=0)){ 
		    
//&&
                    //(JDTestDriver.OSName_.indexOf("OS/400") < 0))  //@F1A //hostserver updated
		    expected="1969-12-31 19:12:22.343                           ";
                    assertCondition(check.equals(expected),
						 "\ncheck ='"+check+"'"+
				                 "\nsb    ='"+expected+"'  tz="+tz); 
               // else if (isToolboxDriver()) //@F3A  //if Toolbox on the 400
                   // assertCondition(check.equals("1970-01-01 01:12:22.343000                        ")); //@F3A
		} else if ("America/Chicago".equals(tz) || "GMT-6".equals(tz)) {
		    expected= "1969-12-31 19:12:22.343";
		    check=check.trim(); 
		    Timestamp eTs = Timestamp.valueOf(expected);
		    Timestamp cTs = Timestamp.valueOf(check); 
		    assertCondition(cTs.equals(eTs), "check is "+check+" sb "+expected+" tz="+tz); 

		} else { // if native
		    expected= "1970-01-01 01:12:22.343000                        ";

		    // In JDK 1.6 for J9, we cannot have extra space on the end 
		    expected=expected.trim();
		    check=check.trim(); 
		    Timestamp eTs = Timestamp.valueOf(expected);
		    Timestamp cTs = Timestamp.valueOf(check); 
		    boolean condition = cTs.equals(eTs);
		    if (!condition) {
			System.out.println("check is  '"+check+"'");
			System.out.println("should be '"+expected+"'");
			System.out.println("tz = "+tz); 
            }
		    
                    assertCondition(condition); // @F4C
		}
            } catch (Exception e) {
                failed(e, "Unexpected Exception :  check is  '"+check+"'  expected is '"+expected+"'   tz = "+tz); 

            }
        }
    }

/**
setTimestamp() - Set a VARCHAR(50) parameter.
- Using an ordinal parameter
**/
    public void Var017 ()
    {
        if (checkJdbc20()) {
            try {
                Timestamp t = new Timestamp (4342343);
                cs.setTimestamp(12, t);
                cs.execute();
                String check = cs.getString(12);
		// V5R4 group test runs with tz="UTC"; 
		String tz = System.getProperty("user.timezone"); 

                if ((isToolboxDriver()
		    && !("UTC".equals(tz))) || (System.getProperty("java.home").indexOf("openjdk")>=0)){
		    // 04/08/2013 -- toolbox now trims trailing 000 when convert to string 
                    assertCondition(check.equals("1969-12-31 19:12:22.343"));  //@F2C
               // else if (isToolboxDriver()) //@F3A //if Toolbox on the 400
                //    assertCondition(check.equals("1970-01-01 01:12:22.343000"));  //@F3A

		} else if ("America/Chicago".equals(tz) || "GMT-6".equals(tz)) {
		    String expected= "1969-12-31 19:12:22.343";
		    check=check.trim(); 
		    Timestamp eTs = Timestamp.valueOf(expected);
		    Timestamp cTs = Timestamp.valueOf(check); 
		    assertCondition(cTs.equals(eTs), "check is "+check+" sb "+expected+" tz="+tz); 

		} else { // if native
		    Timestamp cTs = Timestamp.valueOf(check);
		    Timestamp eTs = Timestamp.valueOf("1970-01-01 01:12:22.343000");
                    assertCondition(cTs.equals(eTs));  //@F4C
		}
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }

/**
setTimestamp() - Set a CLOB parameter.
- Using an ordinal parameter
**/
    public void Var018 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setTimestamp(20, sampleTimestamp);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTimestamp() - Set a DBCLOB parameter.
- Using an ordinal parameter
**/
    public void Var019 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setTimestamp(21, sampleTimestamp);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTimestamp() - Set a BINARY parameter.
- Using an ordinal parameter
**/
    public void Var020 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setTimestamp(13, sampleTimestamp);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTimestamp() - Set a VARBINARY parameter.
- Using an ordinal parameter
**/
    public void Var021 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setTimestamp(14, sampleTimestamp);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTimestamp() - Set a BLOB parameter.
- Using an ordinal parameter
**/
    public void Var022 ()
    {
        if (checkJdbc20()) {
            try {
                cs.setTimestamp(19, sampleTimestamp);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTimestamp() - Set a DATE parameter.

SQL$00 - for some reason, the toolbox JDBC driver has a time of 06:00:00.0 for the timestamp
they retrieve.  I have 00:00:00.0 which is what I think I would want.

- Using an ordinal parameter
**/
    public void Var023 ()
    {
        if (checkJdbc20()) {
            try {
                Timestamp t = new Timestamp (8640000000L);
                cs.setTimestamp(15, t);
                cs.execute();
                Date check = cs.getDate(15);
                assertCondition (check.toString().substring(0, 10).equals(t.toString().substring(0, 10)), "got "+check.toString().substring(0, 10)+" sb "+t.toString().substring(0, 10));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setTimestamp() - Set a TIME parameter.
- Using an ordinal parameter
**/
    public void Var024 ()
    {
        if (checkJdbc20()) {
            try {
                Timestamp t = new Timestamp (1198733);
                cs.setTimestamp(16, t);
                cs.execute();
                Time check = cs.getTime(16);
                assertCondition (t.toString().indexOf(check.toString()) >= 0);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setTimestamp() - Set a TIMESTAMP parameter.
- Using an ordinal parameter
**/
    public void Var025 ()
    {
        if (checkJdbc20()) {
            try {
                Timestamp t = new Timestamp (3232);
                cs.setTimestamp (17, t);
                cs.execute();
                Timestamp check = cs.getTimestamp (17);
                assertCondition (check.toString().equals(t.toString()));
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }

/**
setTimestamp() - Set a TIMESTAMP parameter with a calendar specified.
- Using an ordinal parameter
**/
    public void Var026 ()
    {
        if (checkJdbc20()) {
            try {
                Timestamp t = new Timestamp (34234);
                cs.setTimestamp (17, t, Calendar.getInstance());
                cs.execute();
                Timestamp check = cs.getTimestamp (17);
                assertCondition (check.toString().equals(t.toString()));
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }

/**
setTimestamp() - Set a BIGINT parameter.
- Using an ordinal parameter
**/
    public void Var027 ()
    {
        if (checkJdbc20()) {
            if (checkBigintSupport()) {
                try {
                    cs.setTimestamp(22, sampleTimestamp);
                    failed ("Didn't throw SQLException");
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf(e, "java.sql.SQLException");
                }
            }
        }
    }

/**
setTimestamp() - Should throw an exception if the callable statement is closed.
- Using a named parameter
**/
    public void Var028 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.close();
                cs.setTimestamp("P_TIMESTAMP", sampleTimestamp);
                failed("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }

    }

/**
setTimestamp() - Should throw an exception when an invalid index is specified.
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
                cs.setTimestamp("PARAM", sampleTimestamp);
                failed("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTimestamp() - Should set to SQL NULL when the value is null.
- Using a named parameter
**/
    public void Var030 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setTimestamp("P_TIMESTAMP", null);
                cs.execute();
                Timestamp check = cs.getTimestamp(17);
                boolean wn = cs.wasNull();
                assertCondition((check == null) && (wn == true), "check="+check+" sb null  wn="+wn+" sb true");
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }

/**
setTimestamp() - Should work with a valid parameter index.
- Using a named parameter
**/
    public void Var031 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                Timestamp t = new Timestamp (444);
                cs.setTimestamp("P_TIMESTAMP", t);
                cs.execute();
                Timestamp check = cs.getTimestamp(17);
                assertCondition (check.toString().equals(t.toString()));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setTimestamp() - Should throw an exception when the calendar is null.
- Using a named parameter
**/
    public void Var032 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setTimestamp("P_TIMESTAMP", sampleTimestamp, null);
                failed("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTimestamp() - Should throw an exception when the parameter is not an input parameter.
- Using a named parameter
**/
    public void Var033 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                CallableStatement cs1 = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + "( ?, ?, ?)");
                cs1.setTimestamp("P2", sampleTimestamp);
                failed("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTimestamp() - Set a SMALLINT parameter.
- Using a named parameter
**/
    public void Var034 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setTimestamp("P_SMALLINT", sampleTimestamp);
                failed("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTimestamp() - Set an INTEGER parameter.
- Using a named parameter
**/
    public void Var035 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setTimestamp ("P_INTEGER", sampleTimestamp);
                failed("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTimestamp() - Set a REAL parameter.
- Using a named parameter
**/
    public void Var036 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setTimestamp("P_REAL", sampleTimestamp);
                failed("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTimestamp() - Set a FLOAT parameter.
- Using a named parameter
**/
    public void Var037 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setTimestamp("P_FLOAT", sampleTimestamp);
                failed("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTimestamp() - Set a DOUBLE parameter.
- Using a named parameter
**/
    public void Var038()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setTimestamp("P_DOUBLE", sampleTimestamp);
                failed("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTimestamp() - Set a DECIMAL parameter.
- Using a named parameter
**/
    public void Var039()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setTimestamp("P_DECIMAL_105", sampleTimestamp);
                failed("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTimestamp() - Set a NUMERIC parameter.
- Using a named parameter
**/
    public void Var040 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setTimestamp("P_NUMERIC_50", sampleTimestamp);
                failed("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTimestamp() - Set a CHAR(50) parameter.
- Using a named parameter
**/
    public void Var041 ()
    {
        if (checkNamedParametersSupport()) {
	    String check="";
	    String tz="";
	    String expected=""; 
            try {
                Timestamp t = new Timestamp(4342343);
                cs.setTimestamp("P_CHAR_50", t);
                cs.execute();
                check = cs.getString(11);

		// V5R4 group test runs with tz="UTC"; 
		tz = System.getProperty("user.timezone"); 

                if ((isToolboxDriver()
		    && !("UTC".equals(tz)))|| (System.getProperty("java.home").indexOf("openjdk")>=0)) { // &&    //if Toolbox not on the 400             
                  //  (JDTestDriver.OSName_.indexOf("OS/400") < 0))  //@F3C
		    // 04/08/2013 Toolbox now trims trailing 0 when convert to string
                    assertCondition(check.startsWith("1969-12-31 19:12:22.343"), "(nested if)check.startsWith(1969-12-31 19:12:22.343) is false and should be true check='"+check+"'");
             //   else if (isToolboxDriver()) //@F3A  //if Toolbox on the 400
               //     assertCondition(check.startsWith("1970-01-01 01:12:22.343000"), "got '"+check+"' expected 1970-01-01 01:12:22.343000"); //@F3A
		} else if ("America/Chicago".equals(tz) || "GMT-6".equals(tz)) {
		    expected= "1969-12-31 19:12:22.343";
		    check=check.trim(); 
		    Timestamp eTs = Timestamp.valueOf(expected);
		    Timestamp cTs = Timestamp.valueOf(check); 
		    assertCondition(cTs.equals(eTs), "check is "+check+" sb "+expected+" tz="+tz); 

		} else {

		    expected= "1970-01-01 01:12:22.343000                        ";

		    // J9 JDK 1.6 doesn't like the extra spaces
		    expected = expected.trim();
		    check = check.trim(); 

		    Timestamp eTs = Timestamp.valueOf(expected);
		    Timestamp cTs = Timestamp.valueOf(check); 
		    boolean condition = cTs.equals(eTs);
		    if (!condition) {
			System.out.println("check is  '"+check+"'");
			System.out.println("should be '"+expected+"'"); 
            }
		    
                    assertCondition(condition); // @F4C

		}
            } catch (Exception e) {
                failed(e, "Unexpected Exception :  check is  '"+check+"'  expected is '"+expected+"'   tz = "+tz); 
            }
        }
    }

/**
setTimestamp() - Set a VARCHAR(50) parameter.
- Using a named parameter
**/
    public void Var042 ()
    {
        if (checkNamedParametersSupport()) {
	    String check="";
	    String tz="";
	    String expected=""; 
            try {
                Timestamp t = new Timestamp (4342343);
                cs.setTimestamp("P_VARCHAR_50", t);
                cs.execute();
                check = cs.getString(12);

		// V5R4 group test runs with tz="UTC"; 
		tz = System.getProperty("user.timezone"); 

                if ((isToolboxDriver()
		    && !("UTC".equals(tz))) || (System.getProperty("java.home").indexOf("openjdk")>=0)) {
		    // 04/08/2013 Toolbox now trims trailing 0 when convert to string
                    assertCondition(check.startsWith("1969-12-31 19:12:22.343"), "Got "+check+" sb 1969-12-31 19:12:22.343");
		} else if ("America/Chicago".equals(tz) || "GMT-6".equals(tz)) {
		    expected= "1969-12-31 19:12:22.343";
		    check=check.trim(); 
		    Timestamp eTs = Timestamp.valueOf(expected);
		    Timestamp cTs = Timestamp.valueOf(check); 
		    assertCondition(cTs.equals(eTs), "check is "+check+" sb "+expected+" tz="+tz); 
		} else {
		    expected= "1970-01-01 01:12:22.343000                        ";


		    // J9 JDK 1.6 doesn't like the extra spaces
		    expected = expected.trim(); 

		    Timestamp eTs = Timestamp.valueOf(expected);
		    Timestamp cTs = Timestamp.valueOf(check); 
		    boolean condition = cTs.equals(eTs);
		    if (!condition) {
			System.out.println("check is  '"+check+"'");
			System.out.println("should be '"+expected+"'"); 
            }

		    assertCondition(condition); // @F4C
		    }

            } catch (Exception e) {
                failed(e, "Unexpected Exception :  check is  '"+check+"'  expected is '"+expected+"'   tz = "+tz); 
            }
        }
    }

/**
setTimestamp() - Set a CLOB parameter.
- Using a named parameter
**/
    public void Var043 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setTimestamp("P_CLOB", sampleTimestamp);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTimestamp() - Set a DBCLOB parameter.
- Using a named parameter
**/
    public void Var044 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setTimestamp("P_DBCLOB", sampleTimestamp);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTimestamp() - Set a BINARY parameter.
- Using a named parameter
**/
    public void Var045 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setTimestamp("P_BINARY_20", sampleTimestamp);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTimestamp() - Set a VARBINARY parameter.
- Using a named parameter
**/
    public void Var046 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setTimestamp("P_VARBINARY_20", sampleTimestamp);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTimestamp() - Set a BLOB parameter.
- Using a named parameter
**/
    public void Var047 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setTimestamp("P_BLOB", sampleTimestamp);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTimestamp() - Set a DATE parameter.

SQL$00 - for some reason, the toolbox JDBC driver has a time of 06:00:00.0 for the timestamp
they retrieve.  I have 00:00:00.0 which is what I think I would want.

- Using a named parameter
**/
    public void Var048 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                Timestamp t = new Timestamp (8640000000L);
                cs.setTimestamp("P_DATE", t);
                cs.execute();
                Date check = cs.getDate(15);
                assertCondition (check.toString().substring(0, 10).equals(t.toString().substring(0, 10)),
				 "got "+check.toString().substring(0, 10)+ " sb "+t.toString().substring(0, 10));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setTimestamp() - Set a TIME parameter.
- Using a named parameter
**/
    public void Var049 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                Timestamp t = new Timestamp (1198733);
                cs.setTimestamp("P_TIME", t);
                cs.execute();
                Time check = cs.getTime(16);
                assertCondition (t.toString().indexOf(check.toString()) >= 0);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setTimestamp() - Set a TIMESTAMP parameter.
- Using a named parameter
**/
    public void Var050 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                Timestamp t = new Timestamp (3232);
                cs.setTimestamp ("P_TIMESTAMP", t);
                cs.execute();
                Timestamp check = cs.getTimestamp (17);
                assertCondition (check.toString().equals(t.toString()));
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }

/**
setTimestamp() - Set a TIMESTAMP parameter with a calendar specified.
- Using a named parameter
**/
    public void Var051 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                Timestamp t = new Timestamp (34234);
                cs.setTimestamp ("P_TIMESTAMP", t, Calendar.getInstance());
                cs.execute();
                Timestamp check = cs.getTimestamp (17);
                assertCondition (check.toString().equals(t.toString()));
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }

/**
setTimestamp() - Set a BIGINT parameter.
- Using a named parameter
**/
    public void Var052 ()
    {
        if (checkNamedParametersSupport()) {
            if (checkBigintSupport()) {
                try {
                    cs.setTimestamp("P_BIGINT", sampleTimestamp);
                    failed ("Didn't throw SQLException");
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf(e, "java.sql.SQLException");
                }
            }
        }
    }

/**
setTimestamp() - Should throw an exception if an ordinal and a named parameter
are used for the same callable statement.
- Using an ordinal parameter
- Using a named parameter
**/
    public void Var053 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                cs.setString(11, "Hi");
                cs.setTimestamp ("P_TIMESTAMP", new Timestamp (4545));
                cs.execute();
                // I'm feeling lazy... if we were able to execute, the test worked.
                assertCondition (true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
setTimestamp() - Should work since the lower case name is not in quotes.
- Using a named parameter
**/
    public void Var054 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                Timestamp t = new Timestamp (444);
                cs.setTimestamp("p_timestamp", t);
                cs.execute();
                Timestamp check = cs.getTimestamp(17);
                assertCondition (check.toString().equals(t.toString()));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setTimestamp() - Should work since the mixed case name is not in quotes.
- Using a named parameter
**/
    public void Var055 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                Timestamp t = new Timestamp (444);
                cs.setTimestamp("p_TIMEstamp", t);
                cs.execute();
                Timestamp check = cs.getTimestamp(17);
                assertCondition (check.toString().equals(t.toString()));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setTimestamp() - Should throw an exception since the mixed case name is in quotes.
- Using a named parameter
**/
    public void Var056 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                Timestamp t = sampleTimestamp;
                cs.setTimestamp("'p_TIMEstamp'", t);
                failed("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setTimestamp() - Should throw an exception since the lower case name is in quotes.
- Using a named parameter
**/
    public void Var057 ()
    {
        if (checkNamedParametersSupport()) {
            try {
                Timestamp t = sampleTimestamp;
                cs.setTimestamp("'p_timestamp'", t);
                failed("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

    public void Var058() { notApplicable("filler");}
    public void Var059() { notApplicable("filler");}
    public void Var060() { notApplicable("filler");}

    /**
     * Test set timestamp for different precision
     * The called procedure will have two parameters.
     * parameter1 is the input timestamp 
     * parameter2 is the output timestamp
     * the testValues parameters is array of {inputString, outputString} pairs 
     */

  public void testTimestampPrecision(String procedureName,
      String procedureDefinition, String[][] testValues) {
    if (checkTimestamp12Support()) {
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
        for (int i = 0; i < testValues.length; i++) {

	  String outputString = null;
	  String inputString =  testValues[i][0];
	  try {
	      Timestamp inputTimestamp = null;
	      if (!"null".equals(inputString)) {
		  if (inputString.length() < 29) {
		      inputTimestamp = Timestamp.valueOf(inputString);
		  } else {
		      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
 			     inputTimestamp = (Timestamp) JDReflectionUtil.callStaticMethod_O("com.ibm.db2.jdbc.app.DB2JDBCTimestamp", "valueOf", inputString);
			  
		      } else if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
			  inputTimestamp = AS400JDBCTimestamp.valueOf(inputString); 
		      } else {
			  inputTimestamp = Timestamp.valueOf(inputString);
		      } 
		  }
		      
	      } 
	      cstmt.setTimestamp(1, inputTimestamp);
	      cstmt.execute();
	      Timestamp outputTimestamp = cstmt.getTimestamp(2);
	      if (outputTimestamp == null) {
		  outputString="null";
	      } else {
		  outputString = outputTimestamp.toString();
	      }
	  } catch (Exception e) {
	      e.printStackTrace(System.out);
	      outputString=e.toString(); 
	  } 
          if (outputString.equals(testValues[i][1])) {
            sb.append("OK output=" + outputString + "\n");
          } else {
            passed = false;
            sb.append("FAILED input="+inputString +" output=" + outputString + " expected "
                + testValues[i][1] + "\n");
          }
        }

        s.close();
        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception " + sb);
      }
    }

  }

  public void Var061() {
    String[][] testValues = {
	{"2013-01-13 11:12:13.0","2013-01-13 11:12:13.0",},
	{"2013-02-13 11:12:13.1","2013-02-13 11:12:13.1",},
	{"2013-02-13 11:12:13.12","2013-02-13 11:12:13.12",},
	{"2013-02-13 11:12:13.123","2013-02-13 11:12:13.123",},
	{"2013-02-13 11:12:13.1234","2013-02-13 11:12:13.1234",},
	{"2013-02-13 11:12:13.12345","2013-02-13 11:12:13.12345",},
	{"2013-02-13 11:12:13.123456","2013-02-13 11:12:13.123456",},
	{"2013-02-13 11:12:13.1234567","2013-02-13 11:12:13.1234567",},
	{"2013-02-13 11:12:13.12345678","2013-02-13 11:12:13.12345678",},
	{"2013-02-13 11:12:13.123456789","2013-02-13 11:12:13.123456789",},
	{"2013-02-13 11:12:13.1234567891","2013-02-13 11:12:13.1234567891",},
	{"2013-02-13 11:12:13.12345678901","2013-02-13 11:12:13.12345678901",},
	{"2013-02-13 11:12:13.123456789012","2013-02-13 11:12:13.123456789012",},
	{"2013-02-13 11:12:13.1","2013-02-13 11:12:13.1",},
	{"2013-02-13 11:12:13.12","2013-02-13 11:12:13.12",},
	{"2013-02-13 11:12:13.123","2013-02-13 11:12:13.123",},
	{"2013-02-13 11:12:13.1234","2013-02-13 11:12:13.1234",},
	{"2013-02-13 11:12:13.12345","2013-02-13 11:12:13.12345",},
	{"2013-02-13 11:12:13.123456","2013-02-13 11:12:13.123456",},
	{"2013-02-13 11:12:13.1234567","2013-02-13 11:12:13.1234567",},
	{"2013-02-13 11:12:13.12345678","2013-02-13 11:12:13.12345678",},
	{"2013-02-13 11:12:13.123456789","2013-02-13 11:12:13.123456789",},
	{"2013-02-13 11:12:13.1234567891","2013-02-13 11:12:13.1234567891",},
	{"2013-02-13 11:12:13.12345678901","2013-02-13 11:12:13.12345678901",},
	{"2013-02-13 11:12:13.123456789","2013-02-13 11:12:13.123456789",},
	{"null","null",},
    };
    
    testTimestampPrecision(collection_ + ".JDCSST61", 
          "( in c1 timestamp(12), out c2 timestamp(12)) begin  " +
          "set c2=c1; " +
          "  end", 
          testValues);

  }


  public void Var062() {
    String[][] testValues = {
	{"2013-01-13 11:12:13.0", "2013-01-13 11:12:13.0",},
	{"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
	{"2013-02-13 11:12:13.12", "2013-02-13 11:12:13.12",},
	{"2013-02-13 11:12:13.123", "2013-02-13 11:12:13.123",},
	{"2013-02-13 11:12:13.1234", "2013-02-13 11:12:13.1234",},
	{"2013-02-13 11:12:13.12345", "2013-02-13 11:12:13.12345",},
	{"2013-02-13 11:12:13.123456", "2013-02-13 11:12:13.123456",},
	{"2013-02-13 11:12:13.1234567", "2013-02-13 11:12:13.1234567",},
	{"2013-02-13 11:12:13.12345678", "2013-02-13 11:12:13.12345678",},
	{"2013-02-13 11:12:13.123456789", "2013-02-13 11:12:13.123456789",},
	{"2013-02-13 11:12:13.1234567891", "2013-02-13 11:12:13.1234567891",},
	{"2013-02-13 11:12:13.12345678901", "2013-02-13 11:12:13.12345678901",},
	{"2013-02-13 11:12:13.12345678901", "2013-02-13 11:12:13.12345678901",},
	{"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
	{"2013-02-13 11:12:13.12", "2013-02-13 11:12:13.12",},
	{"2013-02-13 11:12:13.123", "2013-02-13 11:12:13.123",},
	{"2013-02-13 11:12:13.1234", "2013-02-13 11:12:13.1234",},
	{"2013-02-13 11:12:13.12345", "2013-02-13 11:12:13.12345",},
	{"2013-02-13 11:12:13.123456", "2013-02-13 11:12:13.123456",},
	{"2013-02-13 11:12:13.1234567", "2013-02-13 11:12:13.1234567",},
	{"2013-02-13 11:12:13.12345678", "2013-02-13 11:12:13.12345678",},
	{"2013-02-13 11:12:13.123456789", "2013-02-13 11:12:13.123456789",},
	{"2013-02-13 11:12:13.1234567891", "2013-02-13 11:12:13.1234567891",},
	{"2013-02-13 11:12:13.12345678901", "2013-02-13 11:12:13.12345678901",},
	{"2013-02-13 11:12:13.123456789", "2013-02-13 11:12:13.123456789",},
	{"null", "null",},
    };

    testTimestampPrecision(collection_ + ".JDCSST62", 
          "( in c1 timestamp(11), out c2 timestamp(11)) begin  " +
          "set c2=c1; " +
          "  end", 
          testValues);


  }


  public void Var063() {
    String[][] testValues = {
	{"2013-01-13 11:12:13.0", "2013-01-13 11:12:13.0",},
	{"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
	{"2013-02-13 11:12:13.12", "2013-02-13 11:12:13.12",},
	{"2013-02-13 11:12:13.123", "2013-02-13 11:12:13.123",},
	{"2013-02-13 11:12:13.1234", "2013-02-13 11:12:13.1234",},
	{"2013-02-13 11:12:13.12345", "2013-02-13 11:12:13.12345",},
	{"2013-02-13 11:12:13.123456", "2013-02-13 11:12:13.123456",},
	{"2013-02-13 11:12:13.1234567", "2013-02-13 11:12:13.1234567",},
	{"2013-02-13 11:12:13.12345678", "2013-02-13 11:12:13.12345678",},
	{"2013-02-13 11:12:13.123456789", "2013-02-13 11:12:13.123456789",},
	{"2013-02-13 11:12:13.1234567891", "2013-02-13 11:12:13.1234567891",},
	{"2013-02-13 11:12:13.1234567892", "2013-02-13 11:12:13.1234567892",},
	{"2013-02-13 11:12:13.1234567893", "2013-02-13 11:12:13.1234567893",},
	{"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
	{"2013-02-13 11:12:13.12", "2013-02-13 11:12:13.12",},
	{"2013-02-13 11:12:13.123", "2013-02-13 11:12:13.123",},
	{"2013-02-13 11:12:13.1234", "2013-02-13 11:12:13.1234",},
	{"2013-02-13 11:12:13.12345", "2013-02-13 11:12:13.12345",},
	{"2013-02-13 11:12:13.123456", "2013-02-13 11:12:13.123456",},
	{"2013-02-13 11:12:13.1234567", "2013-02-13 11:12:13.1234567",},
	{"2013-02-13 11:12:13.12345678", "2013-02-13 11:12:13.12345678",},
	{"2013-02-13 11:12:13.123456789", "2013-02-13 11:12:13.123456789",},
	{"2013-02-13 11:12:13.1234567891", "2013-02-13 11:12:13.1234567891",},
	{"2013-02-13 11:12:13.1234567891", "2013-02-13 11:12:13.1234567891",},
	{"2013-02-13 11:12:13.123456789", "2013-02-13 11:12:13.123456789",},
	{"null", "null",},
    };

    testTimestampPrecision(collection_ + ".JDCSST63", 
          "( in c1 timestamp(10), out c2 timestamp(10)) begin  " +
          "set c2=c1; " +
          "  end", 
          testValues);

  }


  public void Var064() {
    String[][] testValues = {
    /* 0 */ {"2013-01-13 11:12:13.0", "2013-01-13 11:12:13.0",},
    /* 1 */ {"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
    /* 2 */ {"2013-02-13 11:12:13.12", "2013-02-13 11:12:13.12",},
    /* 3 */ {"2013-02-13 11:12:13.123", "2013-02-13 11:12:13.123",},
    /* 4 */ {"2013-02-13 11:12:13.1234", "2013-02-13 11:12:13.1234",},
    /* 5 */ {"2013-02-13 11:12:13.12345", "2013-02-13 11:12:13.12345",},
    /* 6 */ {"2013-02-13 11:12:13.123456", "2013-02-13 11:12:13.123456",},
    /* 7 */ {"2013-02-13 11:12:13.1234567", "2013-02-13 11:12:13.1234567",},
    /* 8 */ {"2013-02-13 11:12:13.12345678", "2013-02-13 11:12:13.12345678",},
    /* 9 */ {"2013-02-13 11:12:13.123456789", "2013-02-13 11:12:13.123456789",},
    /*10 */ {"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
    /*11 */ {"2013-02-13 11:12:13.12", "2013-02-13 11:12:13.12",},
    /*12 */ {"2013-02-13 11:12:13.123", "2013-02-13 11:12:13.123",},
    /*13 */ {"2013-02-13 11:12:13.1234", "2013-02-13 11:12:13.1234",},
    /*14 */ {"2013-02-13 11:12:13.12345", "2013-02-13 11:12:13.12345",},
    /*15 */ {"2013-02-13 11:12:13.123456", "2013-02-13 11:12:13.123456",},
    /*16 */ {"2013-02-13 11:12:13.1234567", "2013-02-13 11:12:13.1234567",},
    /*17 */ {"2013-02-13 11:12:13.12345678", "2013-02-13 11:12:13.12345678",},
    /*18 */ {"2013-02-13 11:12:13.123456789", "2013-02-13 11:12:13.123456789",},
    /*19 */ {"2013-02-13 11:12:13.123456789", "2013-02-13 11:12:13.123456789",},
    /*20 */ {"null", "null",},
    };

    testTimestampPrecision(collection_ + ".JDCSST64", 
          "( in c1 timestamp(9), out c2 timestamp(9)) begin  " +
          "set c2=c1; " +
          "  end", 
          testValues);


  }






  public void Var065() {
    String[][] testValues = {
    /* 0 */ {"2013-01-13 11:12:13.0", "2013-01-13 11:12:13.0",},
    /* 1 */ {"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
    /* 2 */ {"2013-02-13 11:12:13.12", "2013-02-13 11:12:13.12",},
    /* 3 */ {"2013-02-13 11:12:13.123", "2013-02-13 11:12:13.123",},
    /* 4 */ {"2013-02-13 11:12:13.1234", "2013-02-13 11:12:13.1234",},
    /* 5 */ {"2013-02-13 11:12:13.12345", "2013-02-13 11:12:13.12345",},
    /* 6 */ {"2013-02-13 11:12:13.123456", "2013-02-13 11:12:13.123456",},
    /* 7 */ {"2013-02-13 11:12:13.1234567", "2013-02-13 11:12:13.1234567",},
    /* 8 */ {"2013-02-13 11:12:13.12345678", "2013-02-13 11:12:13.12345678",},
    /* 9 */ {"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
    /*10 */ {"2013-02-13 11:12:13.12", "2013-02-13 11:12:13.12",},
    /*11 */ {"2013-02-13 11:12:13.123", "2013-02-13 11:12:13.123",},
    /*12 */ {"2013-02-13 11:12:13.1234", "2013-02-13 11:12:13.1234",},
    /*13 */ {"2013-02-13 11:12:13.12345", "2013-02-13 11:12:13.12345",},
    /*14 */ {"2013-02-13 11:12:13.123456", "2013-02-13 11:12:13.123456",},
    /*15 */ {"2013-02-13 11:12:13.1234567", "2013-02-13 11:12:13.1234567",},
    /*16 */ {"2013-02-13 11:12:13.12345678", "2013-02-13 11:12:13.12345678",},
    /*17 */ {"2013-02-13 11:12:13.12345678", "2013-02-13 11:12:13.12345678",},
    /*18 */ {"2013-02-13 11:12:13.12345678", "2013-02-13 11:12:13.12345678",},
    /*19 */ {"null", "null",},
    };

    testTimestampPrecision(collection_ + ".JDCSST65", 
          "( in c1 timestamp(8), out c2 timestamp(8)) begin  " +
          "set c2=c1; " +
          "  end", 
          testValues);

  }



  public void Var066() {
    String[][] testValues = {
    /* 0 */ {"2013-01-13 11:12:13.0", "2013-01-13 11:12:13.0",},
    /* 1 */ {"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
    /* 2 */ {"2013-02-13 11:12:13.12", "2013-02-13 11:12:13.12",},
    /* 3 */ {"2013-02-13 11:12:13.123", "2013-02-13 11:12:13.123",},
    /* 4 */ {"2013-02-13 11:12:13.1234", "2013-02-13 11:12:13.1234",},
    /* 5 */ {"2013-02-13 11:12:13.12345", "2013-02-13 11:12:13.12345",},
    /* 6 */ {"2013-02-13 11:12:13.123456", "2013-02-13 11:12:13.123456",},
    /* 7 */ {"2013-02-13 11:12:13.1234567", "2013-02-13 11:12:13.1234567",},
    /* 8 */ {"2013-02-13 11:12:13.1234567", "2013-02-13 11:12:13.1234567",},
    /* 9 */ {"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
    /*10 */ {"2013-02-13 11:12:13.12", "2013-02-13 11:12:13.12",},
    /*11 */ {"2013-02-13 11:12:13.123", "2013-02-13 11:12:13.123",},
    /*12 */ {"2013-02-13 11:12:13.1234", "2013-02-13 11:12:13.1234",},
    /*13 */ {"2013-02-13 11:12:13.12345", "2013-02-13 11:12:13.12345",},
    /*14 */ {"2013-02-13 11:12:13.123456", "2013-02-13 11:12:13.123456",},
    /*15 */ {"2013-02-13 11:12:13.1234567", "2013-02-13 11:12:13.1234567",},
    /*16 */ {"2013-02-13 11:12:13.1234567", "2013-02-13 11:12:13.1234567",},
    /*17 */ {"2013-02-13 11:12:13.1234567", "2013-02-13 11:12:13.1234567",},
    /*18 */ {"2013-02-13 11:12:13.1234567", "2013-02-13 11:12:13.1234567",},
    /*19 */ {"null", "null",},
    };

    testTimestampPrecision(collection_ + ".JDCSST66", 
          "( in c1 timestamp(7), out c2 timestamp(7)) begin  " +
          "set c2=c1; " +
          "  end", 
          testValues);
  }



  public void Var067() {
    String[][] testValues = {
    /* 0 */ {"2013-01-13 11:12:13.0", "2013-01-13 11:12:13.0",},
    /* 1 */ {"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
    /* 2 */ {"2013-02-13 11:12:13.12", "2013-02-13 11:12:13.12",},
    /* 3 */ {"2013-02-13 11:12:13.123", "2013-02-13 11:12:13.123",},
    /* 4 */ {"2013-02-13 11:12:13.1234", "2013-02-13 11:12:13.1234",},
    /* 5 */ {"2013-02-13 11:12:13.12345", "2013-02-13 11:12:13.12345",},
    /* 6 */ {"2013-02-13 11:12:13.123456", "2013-02-13 11:12:13.123456",},
    /* 7 */ {"2013-02-13 11:12:13.123456", "2013-02-13 11:12:13.123456",},
    /* 8 */ {"2013-02-13 11:12:13.123456", "2013-02-13 11:12:13.123456",},
    /* 9 */ {"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
    /*10 */ {"2013-02-13 11:12:13.12", "2013-02-13 11:12:13.12",},
    /*11 */ {"2013-02-13 11:12:13.123", "2013-02-13 11:12:13.123",},
    /*12 */ {"2013-02-13 11:12:13.1234", "2013-02-13 11:12:13.1234",},
    /*13 */ {"2013-02-13 11:12:13.12345", "2013-02-13 11:12:13.12345",},
    /*14 */ {"2013-02-13 11:12:13.123456", "2013-02-13 11:12:13.123456",},
    /*15 */ {"2013-02-13 11:12:13.123456", "2013-02-13 11:12:13.123456",},
    /*16 */ {"2013-02-13 11:12:13.123456", "2013-02-13 11:12:13.123456",},
    /*17 */ {"2013-02-13 11:12:13.123456", "2013-02-13 11:12:13.123456",},
    /*18 */ {"2013-02-13 11:12:13.123456", "2013-02-13 11:12:13.123456",},
    /*19 */ {"null", "null",},
    };

    testTimestampPrecision(collection_ + ".JDCSST67", 
          "( in c1 timestamp(6), out c2 timestamp(6)) begin  " +
          "set c2=c1; " +
          "  end", 
          testValues);

  }








  public void Var068() {
    String[][] testValues = {
    /* 0 */ {"2013-01-13 11:12:13.0", "2013-01-13 11:12:13.0",},
    /* 1 */ {"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
    /* 2 */ {"2013-02-13 11:12:13.12", "2013-02-13 11:12:13.12",},
    /* 3 */ {"2013-02-13 11:12:13.123", "2013-02-13 11:12:13.123",},
    /* 4 */ {"2013-02-13 11:12:13.1234", "2013-02-13 11:12:13.1234",},
    /* 5 */ {"2013-02-13 11:12:13.12345", "2013-02-13 11:12:13.12345",},
    /* 6 */ {"2013-02-13 11:12:13.12345", "2013-02-13 11:12:13.12345",},
    /* 7 */ {"2013-02-13 11:12:13.12345", "2013-02-13 11:12:13.12345",},
    /* 8 */ {"2013-02-13 11:12:13.12345", "2013-02-13 11:12:13.12345",},
    /* 9 */ {"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
    /*10 */ {"2013-02-13 11:12:13.12", "2013-02-13 11:12:13.12",},
    /*11 */ {"2013-02-13 11:12:13.123", "2013-02-13 11:12:13.123",},
    /*12 */ {"2013-02-13 11:12:13.1234", "2013-02-13 11:12:13.1234",},
    /*13 */ {"2013-02-13 11:12:13.12345", "2013-02-13 11:12:13.12345",},
    /*14 */ {"2013-02-13 11:12:13.12345", "2013-02-13 11:12:13.12345",},
    /*15 */ {"2013-02-13 11:12:13.12345", "2013-02-13 11:12:13.12345",},
    /*16 */ {"2013-02-13 11:12:13.12345", "2013-02-13 11:12:13.12345",},
    /*17 */ {"2013-02-13 11:12:13.12345", "2013-02-13 11:12:13.12345",},
    /*18 */ {"2013-02-13 11:12:13.12345", "2013-02-13 11:12:13.12345",},
    /*19 */ {"null", "null",},
    };

    testTimestampPrecision(collection_ + ".JDCSST68", 
          "( in c1 timestamp(5), out c2 timestamp(5)) begin  " +
          "set c2=c1; " +
          "  end", 
          testValues);

  }


  public void Var069() {
    String[][] testValues = {
    /* 0 */ {"2013-01-13 11:12:13.0", "2013-01-13 11:12:13.0",},
    /* 1 */ {"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
    /* 2 */ {"2013-02-13 11:12:13.12", "2013-02-13 11:12:13.12",},
    /* 3 */ {"2013-02-13 11:12:13.123", "2013-02-13 11:12:13.123",},
    /* 4 */ {"2013-02-13 11:12:13.1234", "2013-02-13 11:12:13.1234",},
    /* 5 */ {"2013-02-13 11:12:13.1234", "2013-02-13 11:12:13.1234",},
    /* 6 */ {"2013-02-13 11:12:13.1234", "2013-02-13 11:12:13.1234",},
    /* 7 */ {"2013-02-13 11:12:13.1234", "2013-02-13 11:12:13.1234",},
    /* 8 */ {"2013-02-13 11:12:13.1234", "2013-02-13 11:12:13.1234",},
    /* 9 */ {"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
    /*10 */ {"2013-02-13 11:12:13.12", "2013-02-13 11:12:13.12",},
    /*11 */ {"2013-02-13 11:12:13.123", "2013-02-13 11:12:13.123",},
    /*12 */ {"2013-02-13 11:12:13.1234", "2013-02-13 11:12:13.1234",},
    /*13 */ {"2013-02-13 11:12:13.1234", "2013-02-13 11:12:13.1234",},
    /*14 */ {"2013-02-13 11:12:13.1234", "2013-02-13 11:12:13.1234",},
    /*15 */ {"2013-02-13 11:12:13.1234", "2013-02-13 11:12:13.1234",},
    /*16 */ {"2013-02-13 11:12:13.1234", "2013-02-13 11:12:13.1234",},
    /*17 */ {"2013-02-13 11:12:13.1234", "2013-02-13 11:12:13.1234",},
    /*18 */ {"2013-02-13 11:12:13.1234", "2013-02-13 11:12:13.1234",},
    /*19 */ {"null", "null",},
    };

    testTimestampPrecision(collection_ + ".JDCSST69", 
          "( in c1 timestamp(4), out c2 timestamp(4)) begin  " +
          "set c2=c1; " +
          "  end", 
          testValues);

  }




  public void Var070() {
    String[][] testValues = {
    /* 0 */ {"2013-01-13 11:12:13.0", "2013-01-13 11:12:13.0",},
    /* 1 */ {"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
    /* 2 */ {"2013-02-13 11:12:13.12", "2013-02-13 11:12:13.12",},
    /* 3 */ {"2013-02-13 11:12:13.123", "2013-02-13 11:12:13.123",},
    /* 4 */ {"2013-02-13 11:12:13.123", "2013-02-13 11:12:13.123",},
    /* 5 */ {"2013-02-13 11:12:13.123", "2013-02-13 11:12:13.123",},
    /* 6 */ {"2013-02-13 11:12:13.123", "2013-02-13 11:12:13.123",},
    /* 7 */ {"2013-02-13 11:12:13.123", "2013-02-13 11:12:13.123",},
    /* 8 */ {"2013-02-13 11:12:13.123", "2013-02-13 11:12:13.123",},
    /* 9 */ {"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
    /*10 */ {"2013-02-13 11:12:13.12", "2013-02-13 11:12:13.12",},
    /*11 */ {"2013-02-13 11:12:13.123", "2013-02-13 11:12:13.123",},
    /*12 */ {"2013-02-13 11:12:13.123", "2013-02-13 11:12:13.123",},
    /*13 */ {"2013-02-13 11:12:13.123", "2013-02-13 11:12:13.123",},
    /*14 */ {"2013-02-13 11:12:13.123", "2013-02-13 11:12:13.123",},
    /*15 */ {"2013-02-13 11:12:13.123", "2013-02-13 11:12:13.123",},
    /*16 */ {"2013-02-13 11:12:13.123", "2013-02-13 11:12:13.123",},
    /*17 */ {"2013-02-13 11:12:13.123", "2013-02-13 11:12:13.123",},
    /*18 */ {"2013-02-13 11:12:13.123", "2013-02-13 11:12:13.123",},
    /*19 */ {"null", "null",},
    };

    testTimestampPrecision(collection_ + ".JDCSST70", 
          "( in c1 timestamp(3), out c2 timestamp(3)) begin  " +
          "set c2=c1; " +
          "  end", 
          testValues);

  }




  public void Var071() {
    String[][] testValues = {
    /* 0 */ {"2013-01-13 11:12:13.0", "2013-01-13 11:12:13.0",},
    /* 1 */ {"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
    /* 2 */ {"2013-02-13 11:12:13.12", "2013-02-13 11:12:13.12",},
    /* 3 */ {"2013-02-13 11:12:13.12", "2013-02-13 11:12:13.12",},
    /* 4 */ {"2013-02-13 11:12:13.12", "2013-02-13 11:12:13.12",},
    /* 5 */ {"2013-02-13 11:12:13.12", "2013-02-13 11:12:13.12",},
    /* 6 */ {"2013-02-13 11:12:13.12", "2013-02-13 11:12:13.12",},
    /* 7 */ {"2013-02-13 11:12:13.12", "2013-02-13 11:12:13.12",},
    /* 8 */ {"2013-02-13 11:12:13.12", "2013-02-13 11:12:13.12",},
    /* 9 */ {"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
    /*10 */ {"2013-02-13 11:12:13.12", "2013-02-13 11:12:13.12",},
    /*11 */ {"2013-02-13 11:12:13.12", "2013-02-13 11:12:13.12",},
    /*12 */ {"2013-02-13 11:12:13.12", "2013-02-13 11:12:13.12",},
    /*13 */ {"2013-02-13 11:12:13.12", "2013-02-13 11:12:13.12",},
    /*14 */ {"2013-02-13 11:12:13.12", "2013-02-13 11:12:13.12",},
    /*15 */ {"2013-02-13 11:12:13.12", "2013-02-13 11:12:13.12",},
    /*16 */ {"2013-02-13 11:12:13.12", "2013-02-13 11:12:13.12",},
    /*17 */ {"2013-02-13 11:12:13.12", "2013-02-13 11:12:13.12",},
    /*18 */ {"2013-02-13 11:12:13.12", "2013-02-13 11:12:13.12",},
    /*19 */ {"null", "null",},
    };

    testTimestampPrecision(collection_ + ".JDCSST71", 
          "( in c1 timestamp(2), out c2 timestamp(2)) begin  " +
          "set c2=c1; " +
          "  end", 
          testValues);
  }

  public void Var072() {
    String[][] testValues = {
    /* 0 */ {"2013-01-13 11:12:13.0", "2013-01-13 11:12:13.0",},
    /* 1 */ {"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
    /* 2 */ {"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
    /* 3 */ {"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
    /* 4 */ {"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
    /* 5 */ {"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
    /* 6 */ {"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
    /* 7 */ {"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
    /* 8 */ {"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
    /* 9 */ {"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
    /*10 */ {"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
    /*11 */ {"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
    /*12 */ {"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
    /*13 */ {"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
    /*14 */ {"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
    /*15 */ {"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
    /*16 */ {"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
    /*17 */ {"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
    /*18 */ {"2013-02-13 11:12:13.1", "2013-02-13 11:12:13.1",},
    /*19 */ {"null", "null",},
    };

    testTimestampPrecision(collection_ + ".JDCSST72",
          "( in c1 timestamp(1), out c2 timestamp(1)) begin  " +
          "set c2=c1; " +
          "  end", 
          testValues);


  }


  public void Var073() {
    String[][] testValues = {
    /* 0 */ {"2013-01-13 11:12:13.0", "2013-01-13 11:12:13.0",},
    /* 1 */ {"2013-02-13 11:12:13.0", "2013-02-13 11:12:13.0",},
    /* 2 */ {"2013-02-13 11:12:13.0", "2013-02-13 11:12:13.0",},
    /* 3 */ {"2013-02-13 11:12:13.0", "2013-02-13 11:12:13.0",},
    /* 4 */ {"2013-02-13 11:12:13.0", "2013-02-13 11:12:13.0",},
    /* 5 */ {"2013-02-13 11:12:13.0", "2013-02-13 11:12:13.0",},
    /* 6 */ {"2013-02-13 11:12:13.0", "2013-02-13 11:12:13.0",},
    /* 7 */ {"2013-02-13 11:12:13.0", "2013-02-13 11:12:13.0",},
    /* 8 */ {"2013-02-13 11:12:13.0", "2013-02-13 11:12:13.0",},
    /* 9 */ {"2013-02-13 11:12:13.0", "2013-02-13 11:12:13.0",},
    /*10 */ {"2013-02-13 11:12:13.0", "2013-02-13 11:12:13.0",},
    /*11 */ {"2013-02-13 11:12:13.0", "2013-02-13 11:12:13.0",},
    /*12 */ {"2013-02-13 11:12:13.0", "2013-02-13 11:12:13.0",},
    /*13 */ {"2013-02-13 11:12:13.0", "2013-02-13 11:12:13.0",},
    /*14 */ {"2013-02-13 11:12:13.0", "2013-02-13 11:12:13.0",},
    /*15 */ {"2013-02-13 11:12:13.0", "2013-02-13 11:12:13.0",},
    /*16 */ {"2013-02-13 11:12:13.0", "2013-02-13 11:12:13.0",},
    /*17 */ {"2013-02-13 11:12:13.0", "2013-02-13 11:12:13.0",},
    /*18 */ {"2013-02-13 11:12:13.0", "2013-02-13 11:12:13.0",},
    /*19 */ {"null", "null",},
    };

    testTimestampPrecision(collection_ + ".JDCSST73", 
          "( in c1 timestamp(0), out c2 timestamp(0)) begin  " +
          "set c2=c1; " +
          "  end", 
          testValues);


  }





  /**
   * setTimestamp() - Set a BOOLEAN parameter. - Using an ordinal parameter
   */
  public void Var074() {

    if (checkBooleanSupport()) {

      setTestFailure("setTimestamp", 27, sampleTimestamp,
          "Added 2021/01/07 to test boolean support");
    }

  }

  /**
   * setTimestamp() - Set a BOOLEAN parameter. - Using a named parameter
   */
  public void Var075() {

    if (checkBooleanSupport()) {

      setTestFailure("setTimestamp", "P_BOOLEAN", sampleTimestamp,
          "Added 2021/01/07 to test boolean support");
    }

  }




}

