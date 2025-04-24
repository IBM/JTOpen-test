///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDMDMisc.java
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
 //
 //
 ////////////////////////////////////////////////////////////////////////
 //
 // File Name:    JDDMDMisc.java
 //
 // Classes:      JDDMDMisc
 //
 ////////////////////////////////////////////////////////////////////////
 //
 //
 //
 //
 //
 //
 //
 ////////////////////////////////////////////////////////////////////////

package test.JD.DMD;

import com.ibm.as400.access.*;

import test.JDDMDTest;
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet; 
import java.sql.DatabaseMetaData;
import java.util.Hashtable; import java.util.Vector;

/**
Testcase JDDMDMisc.  This tests the following methods
of the JDBC DatabaseMetaData class:

<ul>
<li>toString()
</ul>
**/
public class JDDMDMisc
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDDMDMisc";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDDMDTest.main(newArgs); 
   }


    // Private data.
    private Connection          connection_;
    private Connection          closedConnection_;
    private DatabaseMetaData    dmd_;
    private DatabaseMetaData    dmd2_;
    private String url_;
    // AS400 systemVar005;
    private Statement stmt_;


/**
Constructor.
**/
    public JDDMDMisc (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDDMDMisc",
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
     

        if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
            url_ = "jdbc:as400://" + systemObject_.getSystemName()
                ;
	else  if (getDriver () == JDTestDriver.DRIVER_JTOPENLITE)
            url_ = "jdbc:jtopenlite://" + systemObject_.getSystemName()
                ;

        else
            url_ = "jdbc:db2://" + systemObject_.getSystemName()
                ;

        reconnect(); 
        closedConnection_ = testDriver_.getConnection (url_,systemObject_.getUserId(), encryptedPassword_);
        dmd2_ = closedConnection_.getMetaData ();
        closedConnection_.close ();
    }


   void reconnect() throws Exception {
     if (connection_ != null) connection_.close(); 
        connection_ = testDriver_.getConnection (url_,systemObject_.getUserId(), encryptedPassword_);
        dmd_ = connection_.getMetaData ();
        stmt_ = connection_.createStatement(); 
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
toString() - Should return the catalog name on an
open connection.
**/
    public void Var001()
    {
        try {
            assertCondition (dmd_.toString().equals (connection_.getCatalog ()));
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }

/**
toString() - Should return the default toString() output on a
closed connection.
**/
    public void Var002()
    {
        try {
            assertCondition (dmd2_.toString().length () > 0);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }

/**
test the getNumericFunctions method of DatabaseMetaData
**/
    public void Var003()
    {
        try {
	    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
		String expected="abs,acos,asin,atan,atan2,ceiling,character_length,cos,cot,degrees,exp,floor,log,log10,mod,octet_length,pi,power,position,radians,rand,round,sign,sin,sqrt,tan,truncate";
		    assertCondition(dmd_.getNumericFunctions().equals(expected),  "(toolbox) dmd_.getNumericFunctions() = "+ dmd_.getNumericFunctions() + " AND SHOULD BE "+expected);

	    } else if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {

		    assertCondition(dmd_.getNumericFunctions() == "abs,acos,asin,atan,atan2,ceiling,cos,cot,degrees,exp,floor,log,log10,mod,pi,power,radians,rand,round,sin,sign,sqrt,tan,truncate",
				    "\n(nested else) dmd_.getNumericFunctions() = "+ dmd_.getNumericFunctions() +
				    " AND SHOULD BE                             = abs,acos,asin,atan,atan2,ceiling,cos,cot,degrees,exp,floor,log,log10,mod,pi,power,radians,rand,round,sin,sign,sqrt,tan,truncate");
	    }  else {
                   /* NATIVE VERSION */
		    String expected = "abs,acos,asin,atan,atan2,ceiling,cos,cot,degrees,exp,floor,log,log10,mod,pi,power,radians,rand,round,sign,sin,sqrt,tan,truncate";
		    assertCondition(dmd_.getNumericFunctions().equals(expected),  "(outer else) dmd_.getNumericFunctions() = "+ dmd_.getNumericFunctions() + " AND SHOULD BE "+expected);

	    }
	} catch (Exception e)  {
            failed(e, "Unexpected Exception");
	}
    }

/**
test the getStringFunctions method of DatabaseMetaData
**/
    public void Var004()
    {
        try {
	    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX ) {

	      String expected="ascii,char,char_length,character_length,concat,difference,insert,lcase,left,length,locate,ltrim,octet_length,position,repeat,replace,right,rtrim,soundex,space,substring,ucase";
		
		assertCondition(dmd_.getStringFunctions().equals(expected),
			    "dmd_.getStringFunctions() = " + dmd_.getStringFunctions() + " AND SHOULD BE  "+expected);

	    } else if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
		    assertCondition(dmd_.getStringFunctions() == "char,concat,difference,insert,lcase,left,length,locate,ltrim,repeat,replace,right,rtrim,soundex,space,substring,ucase",
"\n(nested else) dmd_.getStringFunctions() = "+ dmd_.getStringFunctions() +
"\n AND SHOULD BE                            "+"concat,difference,insert,lcase,left,length,locate,ltrim,repeat,replace,right,rtrim,soundex,space,substring,ucase");
	    } else {
               // NATIVE VERSION
		String expected;
		if (getRelease() >=  JDTestDriver.RELEASE_V7R1M0) {
		    expected= "concat,difference,insert,lcase,left,length,locate,ltrim,repeat,replace,right,rtrim,soundex,space,substring,ucase";
		} else {
		    expected="concat,difference,insert,lcase,left,length,locate,ltrim,right,rtrim,soundex,space,substring,ucase";
		}
		assertCondition(dmd_.getStringFunctions().equals(expected),
				"dmd_.getStringFunctions() = " + dmd_.getStringFunctions() + " AND SHOULD BE  "+expected);

	    }
        } catch (Exception e)  {
            failed(e, "Unexpected Exception");
        }
    }

/**
test the getSystemFunctions method of DatabaseMetaData
**/
    public void Var005()
    {
        try {
	    // if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
		// @C1, the cast on the next line is only valid for the toolbox driver

	 //	AS400 system = ((AS400JDBCConnection)connection_).getSystem();
           //     systemVar005 = system;
	    // }
	    String expected = "database,ifnull,user";

            assertCondition(dmd_.getSystemFunctions().equals(expected), "dmd_.getSystemFunctions() ="+dmd_.getSystemFunctions() +" AND SHOULD BE "+expected);
        } catch (Exception e)  {
            failed(e, "Unexpected Exception");
        }
    }

/**
test the getTimeDateFunctions method of DatabaseMetaData
**/
    public void Var006()
    {
        try {
	    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
		String expected = "current_date,current_time,current_timestamp,curdate,curtime,dayname,dayofmonth,dayofweek,dayofyear,extract,hour,minute,month,monthname,now,quarter,second,timestampdiff,week,year";


		    assertCondition(dmd_.getTimeDateFunctions().equals(expected), "dmd_.getTimeDateFunctions() = " + dmd_.getTimeDateFunctions() + " AND SHOULD BE "+expected);


	    } else {
                //
                // native driver
                //
		String expected;
		if (getRelease() >=  JDTestDriver.RELEASE_V7R1M0) {
		    expected = "curdate,curtime,dayname,dayofmonth,dayofweek,dayofyear,hour,minute,month,monthname,now,quarter,second,timestampdiff,week,year";
		} else {
		    expected = "curdate,curtime,dayofmonth,dayofweek,dayofyear,hour,minute,month,now,quarter,second,week,year";
		}

		assertCondition(dmd_.getTimeDateFunctions().equals(expected), "dmd_.getTimeDateFunctions() = " + dmd_.getTimeDateFunctions() + " AND SHOULD BE "+expected);

	    }
        } catch (Exception e)  {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     * misc jdbc40 methods
     * supportsStoredFunctionsUsingCallSyntax
     * providesQueryObjectGenerator
     * providesQueryObjectGenerator
     * autoCommitFailureClosesAllResultSets
     */
    public void Var007()
    {
        String added = "-- added 07/26/2006 by toolbox driver";

        if (checkJdbc40())
        {
            StringBuffer sb = new StringBuffer();
            try
            {
                //expect false
                boolean test1 =  !JDReflectionUtil.callMethod_B(dmd_, "supportsStoredFunctionsUsingCallSyntax");
                if(!test1)
                    sb.append("  dmd.supportsStoredFunctionsUsingCallSyntax() returned " + !test1);

                boolean test2 = true;// !JDReflectionUtil.callMethod_B(dmd_, "providesQueryObjectGenerator");
                //if(!test2)
                   // sb.append("  dmd.providesQueryObjectGenerator() returned " + !test2);
                /* test3 needs to go into JDBC40-ONLY testcase.  It contains enums.

                boolean test3 = false;
                java.sql.RowIdLifetime testLifeTime3 =  (java.sql.RowIdLifetime)JDReflectionUtil.callMethod_O(dmd_, "getRowIdLifetime");
                if(testLifeTime3.compareTo(RowIdLifetime.ROWID_VALID_FOREVER) == 0)
                    test3 = true;
                if(!test3)
                    sb.append("  dmd.getRowIdLifetime() returned incorrect value");
                */


                boolean test4 =  !JDReflectionUtil.callMethod_B(dmd_, "autoCommitFailureClosesAllResultSets");
                if(!test4)
                    sb.append("  dmd.autoCommitFailureClosesAllResultSets() returned " + !test4);

                assertCondition(test1 && test2 && test4 , sb + added);

            } catch (Exception e)
            {
                failed(e, "Unexpected Exception" + added);
            }
        }
    }



    int[] CCSIDs = {
	37,
	500,
	256,
	273,
	277,
	278,
	280,
	284,
	285,
	290,
	297,
	/* 423 Greece  -- does not have backslash */ 
	833,
	836,
	838,
	870,
	871,
	875,
	880,
	905,
	918,
	924,
	930,
	933,
	935,
	937,
	939,
	1025,
	1026,
	1027,
	1097,
	1112,
	1122,
	1123,
	1130,
	1132,
	1137,
	1140,
	1141,
	1142,
	1143,
	1144,
	1145,
	1146,
	1147,
	1148,
	1149,
	1153,
	1154,
	1155,
	1156,
	1157,
	1158,
	1160,
	1164,
	1364,
	1388,
	1399,
	5026,
	5035,
	5123,
	9030,
	 /* 65535, Tested in own variation */ 

    };


    /**
     * Test SQLColumns with different CCSIDs.
     * Prior to the fix for SE62366 the SYSIBM procedures did
     * not handle escape characters in different CCSIDs.
     *
     */
    public void Var008()
    {
	String added = "-- added 07/14/2015 to getColumns with different CCSIDS Fixed by SE62366 (SYSIBM) -- updated 02/16/2022 to check column 23 ";
	if (checkRelease710(added)) { 
	    StringBuffer sb = new StringBuffer();
	    String sql; 
	    try
	    {
		boolean passed = true; 
		try {
		    sql ="Create Collection JDSCHEM_01"; 
		    sb.append("Executing "+sql+"\n"); 
		    stmt_.executeUpdate(sql);
		} catch (Exception e) {
		    sb.append(".. failed with "+e.toString()+"\n"); 
		} 

		try {
		    sql ="Create table JDSCHEM_01.JDTAB_01(COL_01 INT)"; 
		    sb.append("Executing "+sql+"\n"); 
		    stmt_.executeUpdate(sql);
		} catch (Exception e) {
		    sb.append(".. failed with "+e.toString()+"\n"); 
		} 


		for (int i = 0; i < CCSIDs.length; i++) {
		    reconnect(); 
		    sql = "CALL QSYS.QCMDEXC('CHGJOB CCSID("+CCSIDs[i]+")              ', 0000000021.00000)"; 
		    sb.append("Changing CCSID using "+sql+"\n");
		    stmt_.executeUpdate(sql);
		    sb.append("Getting JDSCHEM\\_01,JDTAB\\_01,COL\\_01\n"); 
		    ResultSet rs = dmd_.getColumns(null,"JDSCHEM\\_01","JDTAB\\_01","COL\\_01");
		    if (!rs.next()) {
			passed= false;
			sb.append("..FAILED -- no values returned\n"); 
		    } else {
			String expected = "NO";
			String col23 = rs.getString(23);
			if (!expected.equals(col23)) {
			    passed = false;
			    sb.append("For col23 expected '"+expected+"' got '"+col23+"'\n"); 
			}
		    }
		    rs.close();

		    sb.append("Getting JDSCHEM\\_01,JDTAB\\_01,%\n"); 
		    rs = dmd_.getColumns(null,"JDSCHEM\\_01","JDTAB\\_01","%");
		    if (!rs.next()) {
			passed= false;
			sb.append("..FAILED -- no values returned\n"); 
		    }
		    rs.close();

		    sb.append("Getting JDSCHEM\\_01,%,%\n"); 
		    rs = dmd_.getColumns(null,"JDSCHEM\\_01","%","%");
		    if (!rs.next()) {
			passed= false;
			sb.append("..FAILED -- no values returned\n"); 
		    }
		    rs.close();

		} 

		assertCondition(passed, sb + added);

	    } catch (Exception e)
	    {
		failed(e, "Unexpected Exception" + sb.toString() + added);
	    }
	}

    }


    /**
     * Test SQLFunctionColumns with different CCSIDs.
     * Prior to the fix for SE62366 the SYSIBM procedures did
     * not handle escape characters in different CCSIDs.
     *
     */
    public void Var009()
    {
	String added = "-- added 07/14/2015 to getFunctionColumns with different CCSIDS Fixed by SE62366 (SYSIBM)";

	if (checkRelease710(added)) {
	    StringBuffer sb = new StringBuffer();
	    String sql; 
	    try
	    {
		boolean passed = true; 
		try {
		    sql ="Create Collection JDSCHEM_01"; 
		    sb.append("Executing "+sql+"\n"); 
		    stmt_.executeUpdate(sql);
		} catch (Exception e) {
		    sb.append(".. failed with "+e.toString()+"\n"); 
		} 

		try {
		    sql ="Create function JDSCHEM_01.JDFUN_01(COL_01 INT) returns int language java parameter style java external name 'totally.bogus'"; 
		    sb.append("Executing "+sql+"\n"); 
		    stmt_.executeUpdate(sql);
		} catch (Exception e) {
		    sb.append(".. failed with "+e.toString()+"\n"); 
		} 


		for (int i = 0; i < CCSIDs.length; i++) {
		    reconnect(); 
		    sql = "CALL QSYS.QCMDEXC('CHGJOB CCSID("+CCSIDs[i]+")              ', 0000000021.00000)"; 
		    sb.append("Changing CCSID using "+sql+"\n");
		    stmt_.executeUpdate(sql);
		    sb.append("Getting JDSCHEM\\_01,JDFUN\\_01,COL\\_01\n"); 
		    ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_,"getFunctionColumns", (String) null,"JDSCHEM\\_01","JDFUN\\_01","COL\\_01");
		    if (!rs.next()) {
			passed= false;
			sb.append("..FAILED -- no values returned\n"); 
		    }
		    rs.close();

		    sb.append("Getting JDSCHEM\\_01,JDFUN\\_01,%\n"); 
		    rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_,"getFunctionColumns", (String) null,"JDSCHEM\\_01","JDFUN\\_01","%");
		    if (!rs.next()) {
			passed= false;
			sb.append("..FAILED -- no values returned\n"); 
		    }
		    rs.close();

		    sb.append("Getting JDSCHEM\\_01,%,%\n"); 
		    rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_,"getFunctionColumns", (String) null,"JDSCHEM\\_01","%","%");
		    if (!rs.next()) {
			passed= false;
			sb.append("..FAILED -- no values returned\n"); 
		    }
		    rs.close();

		} 

		assertCondition(passed, sb + added);

	    } catch (Exception e)
	    {
		failed(e, "Unexpected Exception" + sb.toString() + added);
	    }
	}

    }

    /**
     * Test SQLFunction with different CCSIDs.
     * Prior to the fix for SE62366 the SYSIBM procedures did
     * not handle escape characters in different CCSIDs.
     *
     */
    public void Var010()
    {
	String added = "-- added 07/14/2015 to getFunctions with different CCSIDS Fixed by SE62366 (SYSIBM)";

	if (checkRelease710(added)) {
	    StringBuffer sb = new StringBuffer();
	    String sql; 
	    try
	    {
		boolean passed = true; 
		try {
		    sql ="Create Collection JDSCHEM_01"; 
		    sb.append("Executing "+sql+"\n"); 
		    stmt_.executeUpdate(sql);
		} catch (Exception e) {
		    sb.append(".. failed with "+e.toString()+"\n"); 
		} 

		try {
		    sql ="Create function JDSCHEM_01.JDFUN_01(COL_01 INT) returns int language java parameter style java external name 'totally.bogus'"; 
		    sb.append("Executing "+sql+"\n"); 
		    stmt_.executeUpdate(sql);
		} catch (Exception e) {
		    sb.append(".. failed with "+e.toString()+"\n"); 
		} 


		for (int i = 0; i < CCSIDs.length; i++) {
		    reconnect(); 
		    sql = "CALL QSYS.QCMDEXC('CHGJOB CCSID("+CCSIDs[i]+")              ', 0000000021.00000)"; 
		    sb.append("Changing CCSID using "+sql+"\n");
		    stmt_.executeUpdate(sql);
		    sb.append("Getting JDSCHEM\\_01,JDFUN\\_01\n"); 
		    ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSS(dmd_,"getFunctions",(String)null,"JDSCHEM\\_01","JDFUN\\_01");
		    if (!rs.next()) {
			passed= false;
			sb.append("..FAILED -- no values returned\n"); 
		    }
		    rs.close();

		    sb.append("Getting JDSCHEM\\_01,%,%\n"); 
		    rs = (ResultSet) JDReflectionUtil.callMethod_OSSS(dmd_,"getFunctions", null,"JDSCHEM\\_01","%");
		    if (!rs.next()) {
			passed= false;
			sb.append("..FAILED -- no values returned\n"); 
		    }
		    rs.close();

		} 

		assertCondition(passed, sb + added);

	    } catch (Exception e)
	    {
		failed(e, "Unexpected Exception" + sb.toString() + added);
	    }
	}

    }

    /**
     * Test SQLProcedureColumns with different CCSIDs.
     * Prior to the fix for SE62366 the SYSIBM procedures did
     * not handle escape characters in different CCSIDs.
     *
     */
    public void Var011()
    {
        String added = "-- added 07/14/2015 to getProcedureColumns with different CCSIDS Fixed by SE62366 (SYSIBM)";

            StringBuffer sb = new StringBuffer();
	    String sql; 
            try
            {
		boolean passed = true; 
		try {  
		    sql ="Create Collection JDSCHEM_01"; 
		    sb.append("Executing "+sql+"\n"); 
		    stmt_.executeUpdate(sql);
		} catch (Exception e) {
		    sb.append(".. failed with "+e.toString()+"\n"); 
		} 

		try {
		    sql ="Create Procedure JDSCHEM_01.JDPRO_01(COL_01 INT) language java parameter style java external name 'totally.bogus'"; 
		    sb.append("Executing "+sql+"\n"); 
		    stmt_.executeUpdate(sql);
		} catch (Exception e) {
		    sb.append(".. failed with "+e.toString()+"\n"); 
		} 

		
		for (int i = 0; i < CCSIDs.length; i++) {
		    reconnect(); 
		    sql = "CALL QSYS.QCMDEXC('CHGJOB CCSID("+CCSIDs[i]+")              ', 0000000021.00000)"; 
		    sb.append("Changing CCSID using "+sql+"\n");
		    stmt_.executeUpdate(sql);
		    sb.append("Getting JDSCHEM\\_01,JDPRO\\_01,COL\\_01\n"); 
		    ResultSet rs = dmd_.getProcedureColumns(null,"JDSCHEM\\_01","JDPRO\\_01","COL\\_01");
		    if (!rs.next()) {
			passed= false;
			sb.append("..FAILED -- no values returned\n"); 
		    }
		    rs.close();

		    sb.append("Getting JDSCHEM\\_01,JDPRO\\_01,%\n"); 
		    rs = dmd_.getProcedureColumns(null,"JDSCHEM\\_01","JDPRO\\_01","%");
		    if (!rs.next()) {
			passed= false;
			sb.append("..FAILED -- no values returned\n"); 
		    }
		    rs.close();

		    sb.append("Getting JDSCHEM\\_01,%,%\n"); 
		    rs = dmd_.getProcedureColumns(null,"JDSCHEM\\_01","%","%");
		    if (!rs.next()) {
			passed= false;
			sb.append("..FAILED -- no values returned\n"); 
		    }
		    rs.close();

		} 
		
                assertCondition(passed, sb + added);

            } catch (Exception e)
            {
                failed(e, "Unexpected Exception" + sb.toString() + added);
            }
    }



    /**
     * Test SQLProcedures with different CCSIDs.
     * Prior to the fix for SE62366 the SYSIBM procedures did
     * not handle escape characters in different CCSIDs.
     *
     */
    public void Var012()
    {
        String added = "-- added 07/14/2015 to getProcedures with different CCSIDS Fixed by SE62366 (SYSIBM)";

            StringBuffer sb = new StringBuffer();
	    String sql; 
            try
            {
		boolean passed = true; 
		try {
		    sql ="Create Collection JDSCHEM_01"; 
		    sb.append("Executing "+sql+"\n"); 
		    stmt_.executeUpdate(sql);
		} catch (Exception e) {
		    sb.append(".. failed with "+e.toString()+"\n"); 
		} 

		try {
		    sql ="Create Procedure JDSCHEM_01.JDPRO_01(COL_01 INT) returns int language java parameter style java external name 'totally.bogus'"; 
		    sb.append("Executing "+sql+"\n"); 
		    stmt_.executeUpdate(sql);
		} catch (Exception e) {
		    sb.append(".. failed with "+e.toString()+"\n"); 
		} 

		
		for (int i = 0; i < CCSIDs.length; i++) {
		    reconnect();
		    sql = "CALL QSYS.QCMDEXC('CHGJOB CCSID("+CCSIDs[i]+")              ', 0000000021.00000)"; 
		    sb.append("Changing CCSID using "+sql+"\n");
		    stmt_.executeUpdate(sql);
		    sb.append("Getting JDSCHEM\\_01,JDPRO\\_01\n"); 
		    ResultSet rs = dmd_.getProcedures(null,"JDSCHEM\\_01","JDPRO\\_01");
		    if (!rs.next()) {
			passed= false;
			sb.append("..FAILED -- no values returned\n"); 
		    }
		    rs.close();

		    sb.append("Getting JDSCHEM\\_01,%\n"); 
		    rs = dmd_.getProcedures(null,"JDSCHEM\\_01","%");
		    if (!rs.next()) {
			passed= false;
			sb.append("..FAILED -- no values returned\n"); 
		    }
		    rs.close();

		} 
		
                assertCondition(passed, sb + added);

            } catch (Exception e)
            {
                failed(e, "Unexpected Exception" + sb.toString() + added);
            }
    }


    /**
     * Test SQLTables with different CCSIDs.
     * Prior to the fix for SE62366 the SYSIBM procedures did
     * not handle escape characters in different CCSIDs.
     *
     */
    public void Var013()
    {
	String added = "-- added 07/14/2015 to getTables with different CCSIDS Fixed by SE62366 (SYSIBM) -- updated 2/16/2022 to check column 4";
	if (checkRelease710(added)) {
	    StringBuffer sb = new StringBuffer();
	    String sql; 
	    try
	    {
		boolean passed = true; 
		try {
		    sql ="Create Collection JDSCHEM_01"; 
		    sb.append("Executing "+sql+"\n"); 
		    stmt_.executeUpdate(sql);
		} catch (Exception e) {
		    sb.append(".. failed with "+e.toString()+"\n"); 
		} 

		try {
		    sql ="Create table JDSCHEM_01.JDTAB_01(COL_01 INT)"; 
		    sb.append("Executing "+sql+"\n"); 
		    stmt_.executeUpdate(sql);
		} catch (Exception e) {
		    sb.append(".. failed with "+e.toString()+"\n"); 
		} 


		for (int i = 0; i < CCSIDs.length; i++) {
		    reconnect(); 
		    sql = "CALL QSYS.QCMDEXC('CHGJOB CCSID("+CCSIDs[i]+")              ', 0000000021.00000)"; 
		    sb.append("Changing CCSID using "+sql+"\n");
		    stmt_.executeUpdate(sql);
		    sb.append("Getting JDSCHEM\\_01,JDTAB\\_01"); 
		    ResultSet rs = dmd_.getTables(null,"JDSCHEM\\_01","JDTAB\\_01",null);
		    if (!rs.next()) {
			passed= false;
			sb.append("..FAILED -- no values returned\n"); 
		    } else {
			String expectedTableType="TABLE"; 
			String column4 = rs.getString(4);
			if (!expectedTableType.equals(column4)) {
			    passed = false;
			    sb.append("Expected table type of "+expectedTableType+" got "+column4+"\n"); 
			} 
		    }
		    rs.close();

		    sb.append("Getting JDSCHEM\\_01, %\n"); 
		    rs = dmd_.getTables(null,"JDSCHEM\\_01","%",null);
		    if (!rs.next()) {
			passed= false;
			sb.append("..FAILED -- no values returned\n"); 
		    }
		    rs.close();


		} 

		assertCondition(passed, sb + added);

	    } catch (Exception e)
	    {
		failed(e, "Unexpected Exception" + sb.toString() + added);
	    }
	}



    }

   /**
     * Test SQLColumnPrivileges with different CCSIDs.
     * Prior to the fix for SE62366 the SYSIBM procedures did
     * not handle escape characters in different CCSIDs.
     *
     */
    public void Var014()
    {
	String added = "-- added 07/14/2015 to getColumnPrivileges with different CCSIDS Fixed by SE62366 (SYSIBM)";
	if (checkRelease710(added)) {
	    StringBuffer sb = new StringBuffer();
	    String sql; 
	    try
	    {
		boolean passed = true; 
		try {
		    sql ="Create Collection JDSCHEM_01"; 
		    sb.append("Executing "+sql+"\n"); 
		    stmt_.executeUpdate(sql);
		} catch (Exception e) {
		    sb.append(".. failed with "+e.toString()+"\n"); 
		} 

		try {
		    sql ="Create table JDSCHEM_01.JDTPRIV_01(COL_01 INT)"; 
		    sb.append("Executing "+sql+"\n"); 
		    stmt_.executeUpdate(sql);
		} catch (Exception e) {
		    sb.append(".. failed with "+e.toString()+"\n"); 
		} 


		try {
		    sql = "GRANT UPDATE(COL_01) ON JDSCHEM_01.JDTPRIV_01 TO QUSER"; 
		    sb.append("Executing "+sql+"\n"); 
		    stmt_.executeUpdate(sql);
		} catch (Exception e) {
		    sb.append(".. failed with "+e.toString()+"\n"); 
		} 


		for (int i = 0; i < CCSIDs.length; i++) {
		    reconnect();

		    sql = "CALL QSYS.QCMDEXC('CHGJOB CCSID("+CCSIDs[i]+")              ', 0000000021.00000)"; 
		    sb.append("Changing CCSID using "+sql+"\n");
		    stmt_.executeUpdate(sql);
		    sb.append("Getting JDSCHEM_01,JDTPRIV_01,COL\\_01\n"); 
		    ResultSet rs = dmd_.getColumnPrivileges(null,"JDSCHEM_01","JDTPRIV_01","COL\\_01");
		    if (!rs.next()) {
			passed= false;
			sb.append("..FAILED -- no values returned\n"); 
		    }
		    rs.close();


		} 

		assertCondition(passed, sb + added);

	    } catch (Exception e)
	    {
		failed(e, "Unexpected Exception" + sb.toString() + added);
	    }
	}

    }


   /**
     * Test SQLTablePrivileges with different CCSIDs.
     * Prior to the fix for SE62366 the SYSIBM procedures did
     * not handle escape characters in different CCSIDs.
     *
     */
    public void Var015()
    {
	String added = "-- added 07/14/2015 to getTablePrivileges with different CCSIDS Fixed by SE62366 (SYSIBM)";
	if (checkRelease710(added)) {
	    StringBuffer sb = new StringBuffer();
	    String sql; 
	    try
	    {
		boolean passed = true; 
		try {
		    sql ="Create Collection JDSCHEM_01"; 
		    sb.append("Executing "+sql+"\n"); 
		    stmt_.executeUpdate(sql);
		} catch (Exception e) {
		    sb.append(".. failed with "+e.toString()+"\n"); 
		} 

		try {
		    sql ="Create table JDSCHEM_01.JDTTPRIV_01(COL_01 INT)"; 
		    sb.append("Executing "+sql+"\n"); 
		    stmt_.executeUpdate(sql);
		} catch (Exception e) {
		    sb.append(".. failed with "+e.toString()+"\n"); 
		} 


		try {
		    sql = "GRANT UPDATE ON JDSCHEM_01.JDTTPRIV_01 TO QUSER"; 
		    sb.append("Executing "+sql+"\n"); 
		    stmt_.executeUpdate(sql);
		} catch (Exception e) {
		    sb.append(".. failed with "+e.toString()+"\n"); 
		} 


		for (int i = 0; i < CCSIDs.length; i++) {
		    reconnect(); 
		    sql = "CALL QSYS.QCMDEXC('CHGJOB CCSID("+CCSIDs[i]+")              ', 0000000021.00000)"; 
		    sb.append("Changing CCSID using "+sql+"\n");
		    stmt_.executeUpdate(sql);
		    sb.append("Getting JDSCHEM_01,JDTTPRIV\\_01\n"); 
		    ResultSet rs = dmd_.getTablePrivileges(null,"JDSCHEM_01","JDTTPRIV\\_01");
		    if (!rs.next()) {
			passed= false;
			sb.append("..FAILED -- no values returned\n"); 
		    }
		    rs.close();

		} 

		assertCondition(passed, sb + added);

	    } catch (Exception e)
	    {
		failed(e, "Unexpected Exception" + sb.toString() + added);
	    }
	}


    }


    /**
     * Test SQLPseudoColumns with different CCSIDs.
     * Prior to the fix for SE62366 the SYSIBM procedures did
     * not handle escape characters in different CCSIDs.
     *
     */
    public void Var016()
    {
	String added = "-- added 07/14/2015 to getPseudoColumns with different CCSIDS Fixed by SE62366 (SYSIBM)";

	if (checkRelease710(added)) {
	    StringBuffer sb = new StringBuffer();
	    String sql; 
	    try
	    {
		boolean passed = true; 
		try {
		    sql ="Create Collection JDSCHEM_01"; 
		    sb.append("Executing "+sql+"\n"); 
		    stmt_.executeUpdate(sql);
		} catch (Exception e) {
		    sb.append(".. failed with "+e.toString()+"\n"); 
		} 

		try {
		    sql ="Create table JDSCHEM_01.JDPSE_01(COL_01 INT IMPLICITLY HIDDEN, COL_02 INT )"; 
		    sb.append("Executing "+sql+"\n"); 
		    stmt_.executeUpdate(sql);
		} catch (Exception e) {
		    sb.append(".. failed with "+e.toString()+"\n"); 
		} 


		for (int i = 0; i < CCSIDs.length; i++) {
		    reconnect(); 
		    sql = "CALL QSYS.QCMDEXC('CHGJOB CCSID("+CCSIDs[i]+")              ', 0000000021.00000)"; 
		    sb.append("Changing CCSID using "+sql+"\n");
		    stmt_.executeUpdate(sql);
		    sb.append("Getting JDSCHEM\\_01,JDPSE\\_01,COL\\_01\n");
		    ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_, "getPseudoColumns",null, "JDSCHEM\\_01","JDPSE\\_01","COL\\_01");
		    if (!rs.next()) {
			passed= false;
			sb.append("..FAILED -- no values returned\n"); 
		    }
		    rs.close();

		    sb.append("Getting JDSCHEM\\_01,JDPSE\\_01,%\n"); 
		    rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_, "getPseudoColumns",null,"JDSCHEM\\_01","JDPSE\\_01","%");
		    if (!rs.next()) {
			passed= false;
			sb.append("..FAILED -- no values returned\n"); 
		    }
		    rs.close();

		    sb.append("Getting JDSCHEM\\_01,%,%\n"); 
		    rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_, "getPseudoColumns",null,"JDSCHEM\\_01","%","%");
		    if (!rs.next()) {
			passed= false;
			sb.append("..FAILED -- no values returned\n"); 
		    }
		    rs.close();

		} 

		assertCondition(passed, sb + added);

	    } catch (Exception e)
	    {
		failed(e, "Unexpected Exception" + sb.toString() + added);
	    }
	}


    }


   /**
     * Test SYSIBM.SQLTYPEINFO
     * Make sure JDBC 4.0 types are correct.
     */
    public void Var017()
    {
	String added = "-- added 12/16/2015 to testSYSIBM.SQLTYPEINFO"; 

	    StringBuffer sb = new StringBuffer();
	    String expectedRows[][] = {
		{"NCHAR","-15"},
		{"NCLOB","2011"},
		{"NUMERIC","2"},
		{"NVARCHAR","-9"},
	    }; 
	    try
	    {
		boolean passed = true; 
		Statement stmt = connection_.createStatement();

		ResultSet rs = stmt.executeQuery("select TYPE_NAME,JDBC_DATA_TYPE from sysibm.sqltypeinfo where TYPE_NAME like 'N%' ORDER BY TYPE_NAME");

		passed = checkExpectedRows(rs, expectedRows, sb); 
		stmt.close(); 
		
		String [][]expectedMetadataRows4  = {
		  /* 0 row is columns to check */ 
		  { "TYPE_NAME", "DATA_TYPE" },
      { "NCHAR", "-15" },
      { "NVARCHAR", "-9"},
      { "NCLOB",  "2011"},
      { "XML", "2009"},
      { "ROWID", "-8"}, 
		}; 
		
    String [][]expectedMetadataRows3  = {
        /* 0 row is columns to check */ 
        { "TYPE_NAME", "DATA_TYPE" },
        { "NCHAR", "1" },
        { "NVARCHAR", "12"},
        { "NCLOB",  "2005"},
        { "XML", "1111"},
        { "ROWID", "1111"}, 
      }; 
		
		// Check metadata calls for JDBC 3.0 and JDBC 4.0 
		sb.append("checking SQLGetTypeInfo JDBC 4.0\n"); 
		CallableStatement cstmt = connection_.prepareCall("CALL sysibm.SQLGetTypeInfo(0,?)"); 
		cstmt.setString(1, "DATATYPE='JDBC';JDBCVER='4.0';DYNAMIC=0;REPORTPUBLICPRIVILEGES=1;CURSORHOLD=1");
		cstmt.execute(); 
		rs = cstmt.getResultSet(); 
		if ( ! checkExpectedMetadataRows(rs, expectedMetadataRows4, sb)) {
		  passed = false; 
		}
		
    sb.append("checking SQLGetTypeInfo JDBC 3.0\n"); 
    cstmt.setString(1, "DATATYPE='JDBC';DYNAMIC=0;REPORTPUBLICPRIVILEGES=1;CURSORHOLD=1");
    cstmt.execute(); 
    rs = cstmt.getResultSet(); 
    if ( ! checkExpectedMetadataRows(rs, expectedMetadataRows3, sb)) {
      passed = false; 
    }
		
    cstmt.close(); 
		
		
		assertCondition(passed, sb + added);

	    } catch (Exception e)
	    {
		failed(e, "Unexpected Exception" + sb.toString() + added);
	    }



    }

    public boolean checkExpectedRows(ResultSet rs, String[][] expectedRows, StringBuffer sb) throws SQLException {
	boolean passed = true; 
	int i = 0;
	int rowCount = 0; 
	while (rs.next()) {
	  rowCount++; 
	    String[] expectedRow = expectedRows[i];
	    for (int j = 0; j < expectedRow.length; j++) {
		String rowValue = rs.getString(j+1);
		if (rowValue == null) rowValue="null"; 
		if (!expectedRow[j].equals(rowValue)) {
		    sb.append("Row "+i+":"+expectedRow[0]+" Column "+j+" got "+rowValue+" sb "+expectedRow[j]+"\n");
		    passed = false; 
		} 
	    } 
	    i++; 
	} 
	if (rowCount < expectedRows.length) {
	  sb.append("Only fetched "+rowCount+ " rows expected "+expectedRows.length); 
	  passed = false; 
	}
	return passed; 
	
    } 


    public boolean checkExpectedMetadataRows(ResultSet rs, String[][] expectedMetadataRows, StringBuffer sb) throws SQLException {
      boolean[] found = new boolean[expectedMetadataRows.length];
  boolean passed = true; 
  while (rs.next()) {
    String keyValue = rs.getString(expectedMetadataRows[0][0]);
    if (keyValue == null) keyValue="null"; 
    sb.append("Found row with key="+keyValue+"\n"); 
    for (int i = 1; i < expectedMetadataRows.length; i++) {
      String[] expectedRow = expectedMetadataRows[i]; 
      if (keyValue.equals(expectedRow[0])) {
        found[i] = true; 
        for (int j = 1; j < expectedMetadataRows[0].length; j++) {
          String value = rs.getString(expectedMetadataRows[0][j]);
          if (value == null) value = "null"; 
          if (! value.equals(expectedRow[j])) {
            passed = false; 
            sb.append("For row with keyValue="+keyValue+" getString("+expectedMetadataRows[0][j]+")" +
            		"returned "+value+" sb "+expectedRow[j]+"\n");
          }
        }
      }
    }
    
  } 
  
  for (int i = 1; i < expectedMetadataRows.length; i++) { 
    if (!found[i]) {
      String[] expectedRow = expectedMetadataRows[i]; 
      sb.append("Did not find row with key="+expectedRow[0]+"\n");
      passed = false; 
    }
    
    
  }
  
  return passed; 
  
    } 



    
    /**
     * Test SYSIBM.SQLCOLUMNS
     * Make sure JDBC 4.0 types are correct.
     */
  public void Var018() {
    String added = "-- added 12/16/2015 to test SYSIBM.SQLCOLUMNS";

    String schema = JDDMDTest.COLLECTION;
    String table = "JDDMDMSC18";
    StringBuffer sb = new StringBuffer();
    String sql;
    String expectedRows[][] = { 
        { "NCHAR", "C1", "-15" },
        { "NVARCHAR", "C2", "-9" }, 
        { "NCLOB", "C3", "2011" }, 
        { "XML", "C4", "2009" }, 
        { "ROWID", "C5", "-8" }, 
        };
    try {
      boolean passed = true;
      sql = "DROP TABLE " + schema + "." + table;
      try {
        stmt_.executeUpdate(sql);
      } catch (Exception e) {
      }

      sql = " CREATE TABLE " + schema + "." + table
          + "(c1 NCHAR(10), c2 NVARCHAR(10), c3 NCLOB, c4 XML, c5 ROWID)";
      sb.append("SQL=" + sql + "\n");
      stmt_.executeUpdate(sql);
      sql = "select TYPE_NAME, COLUMN_NAME, JDBC_DATA_TYPE  from sysibm.sqlcolumns "
          + "where TABLE_NAME='"
          + table
          + "' AND TABLE_SCHEM='"
          + schema
          + "'"
          + "ORDER BY COLUMN_NAME";
      sb.append("SQL=" + sql + "\n");
      ResultSet rs = stmt_.executeQuery(sql);

      passed = checkExpectedRows(rs, expectedRows, sb);
      
      String [][]expectedMetadataRows4  = {
          /* 0 row is columns to check */ 
          { "COLUMN_NAME", "TYPE_NAME", "DATA_TYPE" },
          { "C1", "NCHAR", "-15" },
          { "C2", "NVARCHAR", "-9"},
          { "C3", "NCLOB",  "2011"},
          { "C4", "XML", "2009"},
          { "C5", "ROWID", "-8"}, 
        }; 
        
        String [][]expectedMetadataRows3  = {
            /* 0 row is columns to check */ 
            { "COLUMN_NAME", "TYPE_NAME", "DATA_TYPE" },
            { "C1", "NCHAR", "1" },
            { "C2", "NVARCHAR", "12"},
            { "C3", "NCLOB",  "2005"},
            { "C4", "XML", "1111"},
            { "C5", "ROWID", "1111"}, 
          }; 
        
        // Check metadata calls for JDBC 3.0 and JDBC 4.0 
        sb.append("checking SQLGetTypeInfo JDBC 4.0\n"); 
        CallableStatement cstmt = connection_.prepareCall("CALL sysibm.SQLColumns(null,'"+schema+"','"+table+"',null,?)");
        
        cstmt.setString(1, "DATATYPE='JDBC';JDBCVER='4.0';DYNAMIC=0;REPORTPUBLICPRIVILEGES=1;CURSORHOLD=1");
        cstmt.execute(); 
        rs = cstmt.getResultSet(); 
        if ( ! checkExpectedMetadataRows(rs, expectedMetadataRows4, sb)) {
          passed = false; 
        }
        
        sb.append("checking SQLGetTypeInfo JDBC 3.0\n"); 
        cstmt.setString(1, "DATATYPE='JDBC';DYNAMIC=0;REPORTPUBLICPRIVILEGES=1;CURSORHOLD=1");
        cstmt.execute(); 
        rs = cstmt.getResultSet(); 
        if ( ! checkExpectedMetadataRows(rs, expectedMetadataRows3, sb)) {
          passed = false; 
        }
        
        cstmt.close(); 
        sql = "DROP TABLE " + schema + "." + table;
        sb.append("SQL=" + sql + "\n");
        stmt_.executeUpdate(sql);
        

      
      
      
      
      assertCondition(passed, sb + added);

    } catch (Exception e) {
      failed(e, "Unexpected Exception" + sb.toString() + added);
    }

  }    

  /**
   * Test SYSIBM.SQLFUNCTIONCOLS
   * Make sure JDBC 4.0 types are correct.
   */
public void Var019() {
  String added = "-- added 12/16/2015 to test SYSIBM.SQLFUNCTIONCOLS";

  String schema = JDDMDTest.COLLECTION;
  String function = "JDDMDMSC19";
  StringBuffer sb = new StringBuffer();
  String sql;
  String expectedRows[][] = { 
      { "NCHAR", "P1", "-15" },
      { "NVARCHAR", "P2", "-9" }, 
      { "NCLOB", "P3", "2011" }, 
      { "XML",   "P4", "2009" },
      { "ROWID",  "P5", "-8" }, 
      { "INTEGER", "null", "4" }, 
      };
  try {
    boolean passed = true;
    sql = "DROP FUNCTION " + schema + "." + function;
    try {
      stmt_.executeUpdate(sql);
    } catch (Exception e) {
    }

    sql = " CREATE FUNCTION " + schema + "." + function
        + "(p1 NCHAR(10), p2 NVARCHAR(10), p3 NCLOB, p4 XML AS NCLOB, p5 ROWID) RETURNS INT language JAVA parameter style java external name 'hi.there'";
    sb.append("SQL=" + sql + "\n");
    stmt_.executeUpdate(sql);
    sql = "select TYPE_NAME, COLUMN_NAME, JDBC_DATA_TYPE  from sysibm.sqlfunctioncols "
        + "where FUNCTION_NAME='"
        + function
        + "' AND FUNCTION_SCHEM='"
        + schema
        + "'"
        + "ORDER BY COLUMN_NAME";
    sb.append("SQL=" + sql + "\n");
    ResultSet rs = stmt_.executeQuery(sql);

    passed = checkExpectedRows(rs, expectedRows, sb);
    
    
    String [][]expectedMetadataRows4  = {
        /* 0 row is columns to check */ 
        { "COLUMN_NAME", "TYPE_NAME", "DATA_TYPE" },
        { "P1", "NCHAR", "-15" },
        { "P2", "NVARCHAR", "-9"},
        { "P3", "NCLOB",  "2011"},
        { "P4", "XML", "2009"},
        { "P5", "ROWID", "-8"}, 
      }; 
      
      String [][]expectedMetadataRows3  = {
          /* 0 row is columns to check */ 
          { "COLUMN_NAME", "TYPE_NAME", "DATA_TYPE" },
          { "P1", "NCHAR", "1" },
          { "P2", "NVARCHAR", "12"},
          { "P3", "NCLOB",  "2005"},
          { "P4", "XML", "1111"},
          { "P5", "ROWID", "1111"}, 
        }; 
      
      // Check metadata calls for JDBC 3.0 and JDBC 4.0 
      sb.append("checking SQLGetTypeInfo JDBC 4.0\n"); 
      CallableStatement cstmt = connection_.prepareCall("CALL sysibm.SQLFunctionCols(null,'"+schema+"','"+function+"',null,?)");
      
      cstmt.setString(1, "DATATYPE='JDBC';JDBCVER='4.0';DYNAMIC=0;REPORTPUBLICPRIVILEGES=1;CURSORHOLD=1");
      cstmt.execute(); 
      rs = cstmt.getResultSet(); 
      if ( ! checkExpectedMetadataRows(rs, expectedMetadataRows4, sb)) {
        passed = false; 
      }
      
      sb.append("checking SQLGetTypeInfo JDBC 3.0\n"); 
      cstmt.setString(1, "DATATYPE='JDBC';DYNAMIC=0;REPORTPUBLICPRIVILEGES=1;CURSORHOLD=1");
      cstmt.execute(); 
      rs = cstmt.getResultSet(); 
      if ( ! checkExpectedMetadataRows(rs, expectedMetadataRows3, sb)) {
        passed = false; 
      }
      
      cstmt.close(); 
    
    
    
    
    
    
    
    sql = "DROP FUNCTION " + schema + "." + function;
    sb.append("SQL=" + sql + "\n");
    stmt_.executeUpdate(sql);
    
    assertCondition(passed, sb + added);

  } catch (Exception e) {
    failed(e, "Unexpected Exception" + sb.toString() + added);
  }

}    


/**
 * Test SYSIBM.SQLPROCEDURECOLS
 * Make sure JDBC 4.0 types are correct.
 */
public void Var020() {
String added = "-- added 12/16/2015 to test SYSIBM.SQLPROCEDURECOLS";

String schema = JDDMDTest.COLLECTION;
String procedure = "JDDMDMSC20";
StringBuffer sb = new StringBuffer();
String sql;
String expectedRows[][] = { 
    { "NCHAR", "P1", "-15" },
    { "NVARCHAR", "P2", "-9" }, 
    { "NCLOB", "P3", "2011" }, 
    { "XML",   "P4", "2009" },
    { "ROWID",  "P5", "-8" }, 
    };
try {
  boolean passed = true;
  sql = "DROP PROCEDURE " + schema + "." + procedure;
  try {
    stmt_.executeUpdate(sql);
  } catch (Exception e) {
  }

  sql = " CREATE PROCEDURE " + schema + "." + procedure
      + "(p1 NCHAR(10), p2 NVARCHAR(10), p3 NCLOB, P4 XML AS NCLOB, P5 ROWID)  language JAVA parameter style java external name 'hi.there'";
  sb.append("SQL=" + sql + "\n");
  stmt_.executeUpdate(sql);
  sql = "select TYPE_NAME, COLUMN_NAME, JDBC_DATA_TYPE  from sysibm.sqlprocedurecols "
      + "where PROCEDURE_NAME='"
      + procedure
      + "' AND PROCEDURE_SCHEM='"
      + schema
      + "'"
      + "ORDER BY COLUMN_NAME";
  sb.append("SQL=" + sql + "\n");
  ResultSet rs = stmt_.executeQuery(sql);

  passed = checkExpectedRows(rs, expectedRows, sb);

  
  String [][]expectedMetadataRows4  = {
      /* 0 row is columns to check */ 
      { "COLUMN_NAME", "TYPE_NAME", "DATA_TYPE" },
      { "P1", "NCHAR", "-15" },
      { "P2", "NVARCHAR", "-9"},
      { "P3", "NCLOB",  "2011"},
      { "P4", "XML", "2009"},
      { "P5", "ROWID", "-8"}, 
    }; 
    
    String [][]expectedMetadataRows3  = {
        /* 0 row is columns to check */ 
        { "COLUMN_NAME", "TYPE_NAME", "DATA_TYPE" },
        { "P1", "NCHAR", "1" },
        { "P2", "NVARCHAR", "12"},
        { "P3", "NCLOB",  "2005"},
        { "P4", "XML", "1111"},
        { "P5", "ROWID", "1111"}, 
      }; 
    
    // Check metadata calls for JDBC 3.0 and JDBC 4.0 
    sb.append("checking SQLGetTypeInfo JDBC 4.0\n"); 
    CallableStatement cstmt = connection_.prepareCall("CALL sysibm.SQLProcedureCols(null,'"+schema+"','"+procedure+"',null,?)");
    
    cstmt.setString(1, "DATATYPE='JDBC';JDBCVER='4.0';DYNAMIC=0;REPORTPUBLICPRIVILEGES=1;CURSORHOLD=1");
    cstmt.execute(); 
    rs = cstmt.getResultSet(); 
    if ( ! checkExpectedMetadataRows(rs, expectedMetadataRows4, sb)) {
      passed = false; 
    }
    
    sb.append("checking SQLGetTypeInfo JDBC 3.0\n"); 
    cstmt.setString(1, "DATATYPE='JDBC';DYNAMIC=0;REPORTPUBLICPRIVILEGES=1;CURSORHOLD=1");
    cstmt.execute(); 
    rs = cstmt.getResultSet(); 
    if ( ! checkExpectedMetadataRows(rs, expectedMetadataRows3, sb)) {
      passed = false; 
    }
    
    cstmt.close(); 
  

  
  
  
  
  sql = "DROP PROCEDURE " + schema + "." + procedure;
  sb.append("SQL=" + sql + "\n");
  stmt_.executeUpdate(sql);
  
  assertCondition(passed, sb + added);

} catch (Exception e) {
  failed(e, "Unexpected Exception" + sb.toString() + added);
}

}    

/**
 * Test SYSIBM.SQLSPECIALCOLUMNS
 * Make sure JDBC 4.0 types are correct.
 */
public void Var021() {
String added = "-- added 12/16/2015 to test SYSIBM.SQLSPECIALCOLUMNS";

String schema = JDDMDTest.COLLECTION;
String tableA = "JDDMDMS21A";
String tableB = "JDDMDMS21B";
/* String tableC = "JDDMDMS21C"; */ 
StringBuffer sb = new StringBuffer();
String sql;
String expectedRowsA[][] = { { "NCHAR", "C1", "-15" },}; 
String expectedRowsB[][] = { { "NVARCHAR", "C1", "-9" }, };
/* String expectedRowsC[][] = { { "NCLOB", "C1", "2011" }, }; */ 
try {
  boolean passed = true;
  sql = "DROP TABLE " + schema + "." + tableA;
  try {
    stmt_.executeUpdate(sql);
  } catch (Exception e) {
  }
  sql = "DROP TABLE " + schema + "." + tableB;
  try {
    stmt_.executeUpdate(sql);
  } catch (Exception e) {
  }
  /*
  sql = "DROP TABLE " + schema + "." + tableC;
  try {
    stmt.executeUpdate(sql);
  } catch (Exception e) {
  }
*/ 
  
  sql = " CREATE TABLE " + schema + "." + tableA
      + "(c1 NCHAR(10) primary key)";
  sb.append("SQL=" + sql + "\n");
  stmt_.executeUpdate(sql);
  
  sql = " CREATE TABLE " + schema + "." + tableB
      + "(c1  NVARCHAR(10) primary key)";
  sb.append("SQL=" + sql + "\n");
  stmt_.executeUpdate(sql);

  /* currently not possible to have lobs as constraints of any type
  sql = " CREATE TABLE " + schema + "." + tableC
      + "(c1  NCLOB primary key)";
  sb.append("SQL=" + sql + "\n");
  stmt.executeUpdate(sql);
*/ 
  
  
  sql = "select TYPE_NAME, COLUMN_NAME, JDBC_DATA_TYPE  from sysibm.sqlspecialcolumns "
      + "where TABLE_NAME='"
      + tableA
      + "' AND TABLE_SCHEM='"
      + schema
      + "'"
      + "ORDER BY COLUMN_NAME";
  sb.append("SQL=" + sql + "\n");
  ResultSet rs = stmt_.executeQuery(sql);
  if (!checkExpectedRows(rs, expectedRowsA, sb)) {
    passed = false; 
  }

  sql = "select TYPE_NAME, COLUMN_NAME, JDBC_DATA_TYPE  from sysibm.sqlspecialcolumns "
      + "where TABLE_NAME='"
      + tableB
      + "' AND TABLE_SCHEM='"
      + schema
      + "'"
      + "ORDER BY COLUMN_NAME";
  sb.append("SQL=" + sql + "\n");
  rs = stmt_.executeQuery(sql);
  if (!checkExpectedRows(rs, expectedRowsB, sb)) {
    passed = false; 
  }

  /*
  sql = "select TYPE_NAME, COLUMN_NAME, JDBC_DATA_TYPE  from sysibm.sqlcolumns "
      + "where TABLE_NAME='"
      + tableC
      + "' AND TABLE_SCHEM='"
      + schema
      + "'"
      + "ORDER BY COLUMN_NAME";
  sb.append("SQL=" + sql + "\n");
  rs = stmt.executeQuery(sql);
  if (!checkExpectedRows(rs, expectedRowsC, sb)) {
    passed = false; 
  }
*/ 
  
  
  
    String [][]expectedMetadataRowsA4  = {
      /* 0 row is columns to check */ 
      { "COLUMN_NAME", "TYPE_NAME", "DATA_TYPE" },
      { "C1", "NCHAR", "-15" },
    }; 

  String [][]expectedMetadataRowsB4  = {
      /* 0 row is columns to check */ 
      { "COLUMN_NAME", "TYPE_NAME", "DATA_TYPE" },
      { "C1", "NVARCHAR", "-9"},
    }; 

    String [][]expectedMetadataRowsA3  = {
        /* 0 row is columns to check */ 
        { "COLUMN_NAME", "TYPE_NAME", "DATA_TYPE" },
        { "C1", "NCHAR", "1" },
      }; 
    String [][]expectedMetadataRowsB3  = {
        /* 0 row is columns to check */ 
        { "COLUMN_NAME", "TYPE_NAME", "DATA_TYPE" },
        { "C1", "NVARCHAR", "12"},
      }; 


    // Check metadata calls for JDBC 3.0 and JDBC 4.0 
    {
      sb.append("checking SQLSpecialColumns JDBC 4.0\n"); 
    CallableStatement cstmt = connection_.prepareCall(
        "CALL sysibm.SQLSpecialColumns(1, null,'"+schema+"','"+tableA+"',null,null,?)");
    
    cstmt.setString(1, "DATATYPE='JDBC';JDBCVER='4.0';DYNAMIC=0;REPORTPUBLICPRIVILEGES=1;CURSORHOLD=1");
    cstmt.execute(); 
    rs = cstmt.getResultSet(); 
    if ( ! checkExpectedMetadataRows(rs, expectedMetadataRowsA4, sb)) {
      passed = false; 
    }
    
    sb.append("checking SQLGetTypeInfo JDBC 3.0\n"); 
    cstmt.setString(1, "DATATYPE='JDBC';DYNAMIC=0;REPORTPUBLICPRIVILEGES=1;CURSORHOLD=1");
    cstmt.execute(); 
    rs = cstmt.getResultSet(); 
    if ( ! checkExpectedMetadataRows(rs, expectedMetadataRowsA3, sb)) {
      passed = false; 
    }
    
    cstmt.close(); 
    }
  
    {
      sb.append("checking SQLSpecialColumns JDBC 4.0\n"); 
    CallableStatement cstmt = connection_.prepareCall(
        "CALL sysibm.SQLSpecialColumns(1, null,'"+schema+"','"+tableB+"',null,null,?)");
    
    cstmt.setString(1, "DATATYPE='JDBC';JDBCVER='4.0';DYNAMIC=0;REPORTPUBLICPRIVILEGES=1;CURSORHOLD=1");
    cstmt.execute(); 
    rs = cstmt.getResultSet(); 
    if ( ! checkExpectedMetadataRows(rs, expectedMetadataRowsB4, sb)) {
      passed = false; 
    }
    
    sb.append("checking SQLGetTypeInfo JDBC 3.0\n"); 
    cstmt.setString(1, "DATATYPE='JDBC';DYNAMIC=0;REPORTPUBLICPRIVILEGES=1;CURSORHOLD=1");
    cstmt.execute(); 
    rs = cstmt.getResultSet(); 
    if ( ! checkExpectedMetadataRows(rs, expectedMetadataRowsB3, sb)) {
      passed = false; 
    }
    
    cstmt.close(); 
    }
  
  
  sql = "DROP TABLE " + schema + "." + tableA;
  sb.append("SQL=" + sql + "\n");
  stmt_.executeUpdate(sql);
  sql = "DROP TABLE " + schema + "." + tableB;
  sb.append("SQL=" + sql + "\n");
  stmt_.executeUpdate(sql);
  /* 
  sql = "DROP TABLE " + schema + "." + tableC;
  sb.append("SQL=" + sql + "\n");
  stmt.executeUpdate(sql);
  */ 
  
  
  assertCondition(passed, sb + added);

} catch (Exception e) {
  failed(e, "Unexpected Exception" + sb.toString() + added);
}

}    

/**
 * Test SYSIBM.SQLUDTS
 * Make sure JDBC 4.0 types are correct.
 */
public void Var022() {
String added = "-- added 12/16/2015 to test SYSIBM.SQLSPECIALCOLUMNS";

String schema = JDDMDTest.COLLECTION;
String typePrefix = "JDDMDMS22%";
String typeA =      "JDDMDMS22NCHAR";
String typeB =      "JDDMDMS22NVARCHAR";
String typeC =        "JDDMDMS22NCLOB"; 
String typeD =        "JDDMDMS22ROWID"; 
String typeE =        "JDDMDMS22XML"; 
StringBuffer sb = new StringBuffer();
String sql;
String expectedRows[][] = { 
    { "JDDMDMS22NCHAR", "2001", "-15" },
    { "JDDMDMS22NCLOB", "2001", "2011" }, 
    { "JDDMDMS22NVARCHAR","2001",  "-9" },
    /* ROWID shows up as binary in V7R1 in the base tables */ 
    { "JDDMDMS22ROWID", "2001", "-3" }, 
    { "JDDMDMS22XML", "2001",  "2009" }, 
    }; 
try {
  boolean passed = true;
  sql = "DROP TYPE " + schema + "." + typeA;
  try {
    stmt_.executeUpdate(sql);
  } catch (Exception e) {
  }
  sql = "DROP TYPE " + schema + "." + typeB;
  try {
    stmt_.executeUpdate(sql);
  } catch (Exception e) {
  }
  
  sql = "DROP TYPE " + schema + "." + typeC;
  try {
    stmt_.executeUpdate(sql);
  } catch (Exception e) {
  }
 
  sql = "DROP TYPE " + schema + "." + typeD;
  try {
    stmt_.executeUpdate(sql);
  } catch (Exception e) {
  }

  sql = "DROP TYPE " + schema + "." + typeE;
  try {
    stmt_.executeUpdate(sql);
  } catch (Exception e) {
  }

  sql = " CREATE TYPE " + schema + "." + typeA
      + " AS NCHAR(10)";
  sb.append("SQL=" + sql + "\n");
  stmt_.executeUpdate(sql);
  
  sql = " CREATE TYPE " + schema + "." + typeB
      + " AS  NVARCHAR(10)";
  sb.append("SQL=" + sql + "\n");
  stmt_.executeUpdate(sql);

  sql = " CREATE TYPE " + schema + "." + typeC
      + " AS NCLOB";
  sb.append("SQL=" + sql + "\n");
  stmt_.executeUpdate(sql);

  sql = " CREATE TYPE " + schema + "." + typeD
      + " AS ROWID";
  sb.append("SQL=" + sql + "\n");
  stmt_.executeUpdate(sql);

  sql = " CREATE TYPE " + schema + "." + typeE
      + " AS XML";
  sb.append("SQL=" + sql + "\n");
  stmt_.executeUpdate(sql);

  
  sql = "select TYPE_NAME, DATA_TYPE, BASE_TYPE  from sysibm.sqludts "
      + "where TYPE_NAME like '"
      + typePrefix
      + "' AND TYPE_SCHEM='"
      + schema
      + "'"
      + " ORDER BY TYPE_NAME";
  sb.append("SQL=" + sql + "\n");
  ResultSet rs = stmt_.executeQuery(sql);
  if (!checkExpectedRows(rs, expectedRows, sb)) {
    passed = false; 
  }

  
  
  
  String [][]expectedMetadataRows4  = {
    /* 0 row is columns to check */ 
    { "TYPE_NAME", "DATA_TYPE", "BASE_TYPE" },
    { "JDDMDMS22NCHAR", "2001", "-15" },
    { "JDDMDMS22NCLOB", "2001",  "2011" }, 
    { "JDDMDMS22NVARCHAR", "2001",  "-9" }, 
    { "JDDMDMS22ROWID", "2001",  "-3" }, 
    { "JDDMDMS22XML", "2001",  "2009" }, 
  }; 

  String [][]expectedMetadataRows3  = {
      /* 0 row is columns to check */ 
      { "TYPE_NAME", "DATA_TYPE", "BASE_TYPE" },
      { "JDDMDMS22NCHAR", "2001", "1" },
      { "JDDMDMS22NCLOB", "2001",  "2005" }, 
      { "JDDMDMS22NVARCHAR","2001",   "12" }, 
      { "JDDMDMS22ROWID", "2001",  "-3" }, 
      { "JDDMDMS22XML", "2001",  "1111" }, 
    }; 


  // Check metadata calls for JDBC 3.0 and JDBC 4.0 
  {
    
        
    sb.append("checking SQLUDTS  JDBC 4.0\n"); 
  CallableStatement cstmt = connection_.prepareCall(
      "CALL sysibm.SQLUDTS( null,'"+schema+"','"+typePrefix+"',null,?)");
  
  cstmt.setString(1, "DATATYPE='JDBC';JDBCVER='4.0';DYNAMIC=0;REPORTPUBLICPRIVILEGES=1;CURSORHOLD=1");
  cstmt.execute(); 
  rs = cstmt.getResultSet(); 
  if ( ! checkExpectedMetadataRows(rs, expectedMetadataRows4, sb)) {
    passed = false; 
  }
  
  sb.append("checking SQLGetTypeInfo JDBC 3.0\n"); 
  cstmt.setString(1, "DATATYPE='JDBC';DYNAMIC=0;REPORTPUBLICPRIVILEGES=1;CURSORHOLD=1");
  cstmt.execute(); 
  rs = cstmt.getResultSet(); 
  if ( ! checkExpectedMetadataRows(rs, expectedMetadataRows3, sb)) {
    passed = false; 
  }
  
  cstmt.close(); 
  }
  
  
  
  
  
  
  
  sql = "DROP TYPE " + schema + "." + typeA;
  sb.append("SQL=" + sql + "\n");
  stmt_.executeUpdate(sql);
  sql = "DROP TYPE " + schema + "." + typeB;
  sb.append("SQL=" + sql + "\n");
  stmt_.executeUpdate(sql);
  
  sql = "DROP TYPE " + schema + "." + typeC;
  sb.append("SQL=" + sql + "\n");
  stmt_.executeUpdate(sql);
  
  
  
  assertCondition(passed, sb + added);

} catch (Exception e) {
  failed(e, "Unexpected Exception" + sb.toString() + added);
}

}    



}



