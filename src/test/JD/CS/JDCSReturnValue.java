///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSReturnValue.java
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
// File Name:    JDCSReturnValue.java
//
// Classes:      JDCSReturnValue
//
////////////////////////////////////////////////////////////////////////
//
//
// 
//
////////////////////////////////////////////////////////////////////////
package test.JD.CS;

import java.awt.TextArea;
import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;

import test.JDSetupProcedure;
import test.JDTestcase;



/**
Testcase JDCSReturnValue.  This tests the use of return
value parameters in the JDBC CallableStatement class.
**/
public class JDCSReturnValue
extends JDTestcase
{


    // Private data.
    private Connection      connection_;



/**
Constructor.
**/
    public JDCSReturnValue(AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super(systemObject, "JDCSReturnValue",
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
        connection_ = testDriver_.getConnection (baseURL_ + ";errors=full", 
                                                   userId_, encryptedPassword_);
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
syntax - Use no spaces, positive values.
**/
    public void Var001()
    {
        if (checkReturnValueSupport()) {
        try {
            CallableStatement cs = connection_.prepareCall("?=CALL " + JDSetupProcedure.STP_CSPARMSRV + "(?,?,?)");
            cs.setInt(2, 332);
            cs.setInt(4, 126);
            cs.registerOutParameter(1, Types.INTEGER);
            cs.registerOutParameter(3, Types.INTEGER);
            cs.registerOutParameter(4, Types.INTEGER);
            cs.executeUpdate ();
            boolean success = ((cs.getInt(1) == 332)        // return value == P1
                               && (cs.getInt(3) == 333)     // P2 = P1 + 1
                               && (cs.getInt(4) == 127));   // P3 = P3 + 1
            cs.close ();
            assertCondition(success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }

 
/**
syntax - Use a space before =, negative values.
**/
    public void Var002()
    {
        if (checkReturnValueSupport()) {
        try {
            CallableStatement cs = connection_.prepareCall("? =CALL " + JDSetupProcedure.STP_CSPARMSRV + "(?,?,?)");
            cs.setInt(2, -332);
            cs.setInt(4, -126);
            cs.registerOutParameter(1, Types.INTEGER);
            cs.registerOutParameter(3, Types.INTEGER);
            cs.registerOutParameter(4, Types.INTEGER);
            cs.executeUpdate ();
            boolean success = ((cs.getInt(1) == -332)        // return value == P1
                               && (cs.getInt(3) == -331)     // P2 = P1 + 1
                               && (cs.getInt(4) == -125));   // P3 = P3 + 1
            cs.close ();
            assertCondition(success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }

 
/**
syntax - Use a space after =.
**/
    public void Var003()
    {
        if (checkReturnValueSupport()) {
        try {
            CallableStatement cs = connection_.prepareCall("? =CALL " + JDSetupProcedure.STP_CSPARMSRV + "(?,?,?)");
            cs.setInt(2, -332324234);
            cs.setInt(4, 12432932);
            cs.registerOutParameter(1, Types.INTEGER);
            cs.registerOutParameter(3, Types.INTEGER);
            cs.registerOutParameter(4, Types.INTEGER);
            cs.executeUpdate ();
            boolean success = ((cs.getInt(1) == -332324234)        // return value == P1
                               && (cs.getInt(3) == -332324233)     // P2 = P1 + 1
                               && (cs.getInt(4) == 12432933));   // P3 = P3 + 1
            cs.close ();
            assertCondition(success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }

 
/**
syntax - Use lots of extra spaces, and 0 values.
**/
    public void Var004()
    {
        if (checkReturnValueSupport()) {
        try {
            CallableStatement cs = connection_.prepareCall("   ?      =     CALL      " + JDSetupProcedure.STP_CSPARMSRV + "(?,?,?)");
            cs.setInt(2, 0);
            cs.setInt(4, 0);
            cs.registerOutParameter(1, Types.INTEGER);
            cs.registerOutParameter(3, Types.INTEGER);
            cs.registerOutParameter(4, Types.INTEGER);
            cs.executeUpdate ();
            boolean success = ((cs.getInt(1) == 0)        // return value == P1
                               && (cs.getInt(3) == 1)     // P2 = P1 + 1
                               && (cs.getInt(4) == 1));   // P3 = P3 + 1
            cs.close ();
            assertCondition(success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }

 
/**
syntax - Use escape syntax.
**/
    public void Var005()
    {
        if (checkReturnValueSupport()) {
        try {
            CallableStatement cs = connection_.prepareCall(" {   ?      =     CALL      " + JDSetupProcedure.STP_CSPARMSRV + "(?,?,?)     }  ");
            cs.setInt(2, 1);
            cs.setInt(4, -1);
            cs.registerOutParameter(1, Types.INTEGER);
            cs.registerOutParameter(3, Types.INTEGER);
            cs.registerOutParameter(4, Types.INTEGER);
            cs.executeUpdate ();
            boolean success = ((cs.getInt(1) == 1)        // return value == P1
                               && (cs.getInt(3) == 2)     // P2 = P1 + 1
                               && (cs.getInt(4) == 0));   // P3 = P3 + 1
            cs.close ();
            assertCondition(success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }

 
/**
Call a stroed procedure that has a return value, but do no register or get the return value.
**/
    public void Var006()
    {
        if (checkReturnValueSupport()) {
        try {
            CallableStatement cs = connection_.prepareCall("?=CALL " + JDSetupProcedure.STP_CSPARMSRV + "(?,?,?)");
            cs.setInt(2, 33244);
            cs.setInt(4, -4);
            cs.registerOutParameter(3, Types.INTEGER);
            cs.registerOutParameter(4, Types.INTEGER);
            cs.executeUpdate ();
            boolean success = ((cs.getInt(3) == 33245)     // P2 = P1 + 1
                               && (cs.getInt(4) == -3));   // P3 = P3 + 1
            cs.close ();
            assertCondition(success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }

 
/**
Call a stored procedure that has a return value, but do no deal with the return value at all.
**/
    public void Var007()
    {
        if (checkReturnValueSupport()) {
        try {
            CallableStatement cs = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSPARMSRV + "(?,?,?)");
            cs.setInt(1, 10);
            cs.setInt(3, 9);
            cs.registerOutParameter(2, Types.INTEGER);
            cs.registerOutParameter(3, Types.INTEGER);
            cs.executeUpdate ();
            boolean success = ((cs.getInt(2) == 11)     // P2 = P1 + 1
                               && (cs.getInt(3) == 10));   // P3 = P3 + 1
            cs.close ();
            assertCondition(success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }

 
/**
Call a stored procedure that does not have a return value, but deal with it
like it does.
**/
    public void Var008()
    {
        if (checkReturnValueSupport()) {
        try {
            CallableStatement cs = connection_.prepareCall("?=CALL " + JDSetupProcedure.STP_CSPARMS + "(?,?,?)");
            cs.setInt(2, 332);
            cs.setInt(4, 126);
            cs.registerOutParameter(1, Types.INTEGER);
            cs.registerOutParameter(3, Types.INTEGER);
            cs.registerOutParameter(4, Types.INTEGER);
            cs.executeUpdate ();
            boolean success = ((cs.getInt(1) == 0)          // return value == 0
                               && (cs.getInt(3) == 333)     // P2 = P1 + 1
                               && (cs.getInt(4) == 127));   // P3 = P3 + 1
            cs.close ();
            assertCondition(success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }

 
/**
Call a stored procedure that does not have any parameters, but returns a value.
**/
    public void Var009()
    {
        if (checkReturnValueSupport()) {
        try {
            CallableStatement cs = connection_.prepareCall("?=CALL " + JDSetupProcedure.STP_CSRV);
            cs.registerOutParameter(1, Types.INTEGER);
            cs.executeUpdate ();
            boolean success = (cs.getInt(1) == 1976);
            cs.close ();
            assertCondition(success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }

 
/**
Call a stored procedure that has a return value and result sets.
**/
    public void Var010()
    {
	
        if (checkReturnValueSupport()) {
	    StringBuffer sb = new StringBuffer();
	    sb.append(" -- added 11/14/2013 for issue 51847 "); 
        try {
            CallableStatement cs = connection_.prepareCall("?=CALL " + JDSetupProcedure.STP_CSPARMSRSRV + "(?,?,?)");
            cs.setInt(2, 1332);
            cs.setInt(4, -1126);
            cs.registerOutParameter(1, Types.INTEGER);
            cs.registerOutParameter(3, Types.INTEGER);
            cs.registerOutParameter(4, Types.INTEGER);
            
            boolean result = cs.execute ();
            ResultSet rs = cs.getResultSet ();
            rs.next ();
            rs.close ();
            boolean check1 = cs.getMoreResults ();
            rs = cs.getResultSet ();
            rs.next ();
            rs.close ();
            boolean check2 = cs.getMoreResults ();

            boolean success = ((cs.getInt(1) == 1332)        // return value == P1
                               && (cs.getInt(3) == 1333)     // P2 = P1 + 1
                               && (cs.getInt(4) == -1125));   // P3 = P3 + 1
	    if (!success) {
		sb.append("\ncs.getInt(1)="+cs.getInt(1)+" sb 1334 "+
			  "cs.getInt(3)="+cs.getInt(3)+" sb 1333 "+
			  "cs.getInt(4)="+cs.getInt(4)+" sb -1125"); 
	    }
	    if (!result) {
		sb.append("\nresult is "+result+" sb true"); 
	    }
	    if (!check1) {
		sb.append("\ncheck1 (more results) is "+check1+" sb true"); 
	    }
	    if (check2) {
		sb.append("\ncheck2 (even more results) is "+check2+" sb false"); 
	    } 

            cs.close ();
            assertCondition ((success) && (result == true) && (check1 == true) && (check2 == false),sb);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- added 11/14/2013 for issue 51847");
        }
        }
    }



/**
syntax - Use no spaces, positive values -- named parameters
**/
    public void Var011()
    {
        if (checkReturnValueSupport()) {
        try {
            CallableStatement cs = connection_.prepareCall("?=CALL " + JDSetupProcedure.STP_CSPARMSRV + "(?,?,?)");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.registerOutParameter("P2", Types.INTEGER);
            cs.registerOutParameter("P3", Types.INTEGER);
            cs.setInt("P1", 332);
            cs.setInt("P3", 126);
            cs.executeUpdate ();
            boolean success = ((cs.getInt(1) == 332)        // return value == P1
                               && (cs.getInt(3) == 333)     // P2 = P1 + 1
                               && (cs.getInt(4) == 127));   // P3 = P3 + 1
            cs.close ();
            assertCondition(success, " -- added 11/14/2013 for issue 51847");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- added 11/14/2013 for issue 51847");
        }
        }
    }

 
/**
syntax - Use a space before =, negative values.
**/
    public void Var012()
    {
        if (checkReturnValueSupport()) {
        try {
            CallableStatement cs = connection_.prepareCall("? =CALL " + JDSetupProcedure.STP_CSPARMSRV + "(?,?,?)");
            cs.setInt("P1", -332);
            cs.setInt("P3", -126);
            cs.registerOutParameter(1, Types.INTEGER);
            cs.registerOutParameter("P2", Types.INTEGER);
            cs.registerOutParameter("P3", Types.INTEGER);
            cs.executeUpdate ();
            boolean success = ((cs.getInt(1) == -332)        // return value == P1
                               && (cs.getInt(3) == -331)     // P2 = P1 + 1
                               && (cs.getInt(4) == -125));   // P3 = P3 + 1
            cs.close ();
            assertCondition(success, " -- added 11/14/2013 for issue 51847");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- added 11/14/2013 for issue 51847");
        }
        }
    }

 
/**
syntax - Use a space after =.
**/
    public void Var013()
    {
        if (checkReturnValueSupport()) {
        try {
            CallableStatement cs = connection_.prepareCall("? =CALL " + JDSetupProcedure.STP_CSPARMSRV + "(?,?,?)");
            cs.setInt("P1", -332324234);
            cs.setInt("P3", 12432932);
            cs.registerOutParameter(1, Types.INTEGER);
            cs.registerOutParameter("P2", Types.INTEGER);
            cs.registerOutParameter("P3", Types.INTEGER);
            cs.executeUpdate ();
            boolean success = ((cs.getInt(1) == -332324234)        // return value == P1
                               && (cs.getInt(3) == -332324233)     // P2 = P1 + 1
                               && (cs.getInt(4) == 12432933));   // P3 = P3 + 1
            cs.close ();
            assertCondition(success, " -- added 11/14/2013 for issue 51847");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- added 11/14/2013 for issue 51847");
        }
        }
    }

 
/**
syntax - Use lots of extra spaces, and 0 values.
**/
    public void Var014()
    {
        if (checkReturnValueSupport()) {
        try {
            CallableStatement cs = connection_.prepareCall("   ?      =     CALL      " + JDSetupProcedure.STP_CSPARMSRV + "(?,?,?)");
            cs.setInt("P1", 0);
            cs.setInt("P3", 0);
            cs.registerOutParameter(1, Types.INTEGER);
	    cs.registerOutParameter("P2", Types.INTEGER);
            cs.registerOutParameter("P3", Types.INTEGER);
            cs.executeUpdate ();
            boolean success = ((cs.getInt(1) == 0)        // return value == P1
                               && (cs.getInt(3) == 1)     // P2 = P1 + 1
                               && (cs.getInt(4) == 1));   // P3 = P3 + 1
            cs.close ();
            assertCondition(success, " -- added 11/14/2013 for issue 51847");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- added 11/14/2013 for issue 51847");
        }
        }
    }

 
/**
syntax - Use escape syntax.
**/
    public void Var015()
    {
        if (checkReturnValueSupport()) {
        try {
            CallableStatement cs = connection_.prepareCall(" {   ?      =     CALL      " + JDSetupProcedure.STP_CSPARMSRV + "(?,?,?)     }  ");
	    cs.setInt("P1", 1);
	    cs.setInt("P3", -1);
            cs.registerOutParameter(1, Types.INTEGER);
            cs.registerOutParameter("P2", Types.INTEGER);
            cs.registerOutParameter("P3", Types.INTEGER);
            cs.executeUpdate ();
            boolean success = ((cs.getInt(1) == 1)        // return value == P1
                               && (cs.getInt(3) == 2)     // P2 = P1 + 1
                               && (cs.getInt(4) == 0));   // P3 = P3 + 1
            cs.close ();
            assertCondition(success, " -- added 11/14/2013 for issue 51847");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- added 11/14/2013 for issue 51847");
        }
        }
    }

 
/**
Call a stroed procedure that has a return value, but do no register or get the return value.
**/
    public void Var016()
    {
        if (checkReturnValueSupport()) {
        try {
            CallableStatement cs = connection_.prepareCall("?=CALL " + JDSetupProcedure.STP_CSPARMSRV + "(?,?,?)");
            cs.setInt("P1", 33244);
	    cs.setInt("P3", -4);
	    cs.registerOutParameter("P2", Types.INTEGER);
	    cs.registerOutParameter("P3", Types.INTEGER);
            cs.executeUpdate ();
            boolean success = ((cs.getInt(3) == 33245)     // P2 = P1 + 1
                               && (cs.getInt(4) == -3));   // P3 = P3 + 1
            cs.close ();
            assertCondition(success, " -- added 11/14/2013 for issue 51847");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception  -- added 11/14/2013 for issue 51847");
        }
        }
    }

 
/**
Call a stored procedure that has a return value, but do no deal with the return value at all.
**/
    public void Var017()
    {
        if (checkReturnValueSupport()) {
        try {
            CallableStatement cs = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSPARMSRV + "(?,?,?)");
	    cs.setInt("P1", 10);
            cs.setInt("P3", 9);
	    cs.registerOutParameter("P2", Types.INTEGER);
            cs.registerOutParameter("P3", Types.INTEGER);
            cs.executeUpdate ();
            boolean success = ((cs.getInt(2) == 11)     // P2 = P1 + 1
                               && (cs.getInt(3) == 10));   // P3 = P3 + 1
            cs.close ();
            assertCondition(success, " -- added 11/14/2013 for issue 51847");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- added 11/14/2013 for issue 51847");
        }
        }
    }

 
/**
Call a stored procedure that does not have a return value, but deal with it
like it does.
**/
    public void Var018()
    {
        if (checkReturnValueSupport()) {
        try {
            CallableStatement cs = connection_.prepareCall("?=CALL " + JDSetupProcedure.STP_CSPARMS + "(?,?,?)");
            cs.setInt("P1", 332);
            cs.setInt("P3", 126);
            cs.registerOutParameter(1, Types.INTEGER);
	    cs.registerOutParameter("P2", Types.INTEGER);
            cs.registerOutParameter("P3", Types.INTEGER);
            cs.executeUpdate ();
            boolean success = ((cs.getInt(1) == 0)          // return value == 0
                               && (cs.getInt(3) == 333)     // P2 = P1 + 1
                               && (cs.getInt(4) == 127));   // P3 = P3 + 1
            cs.close ();
            assertCondition(success, " -- added 11/14/2013 for issue 51847");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception -- added 11/14/2013 for issue 51847");
        }
        }
    }

 
 
/**
Call a stored procedure that has a return value and result sets.
**/
    public void Var019()
    {
        if (checkReturnValueSupport()) {
	    StringBuffer sb = new StringBuffer();
	    sb.append(" -- added 11/14/2013 for issue 51847 "); 
        try {
            CallableStatement cs = connection_.prepareCall("?=CALL " + JDSetupProcedure.STP_CSPARMSRSRV + "(?,?,?)");
	    cs.setInt("P1", 1332);
            cs.setInt("P3", -1126);
            cs.registerOutParameter(1, Types.INTEGER);
            cs.registerOutParameter("P2", Types.INTEGER);
            cs.registerOutParameter("P3", Types.INTEGER);
            
            boolean result = cs.execute ();
            ResultSet rs = cs.getResultSet ();
            rs.next ();
            rs.close ();
            boolean check1 = cs.getMoreResults ();
            rs = cs.getResultSet ();
            rs.next ();
            rs.close ();
            boolean check2 = cs.getMoreResults ();

            boolean success = ((cs.getInt(1) == 1332)        // return value == P1
                               && (cs.getInt(3) == 1333)     // P2 = P1 + 1
                               && (cs.getInt(4) == -1125));   // P3 = P3 + 1
	    if (!success) {
		sb.append("\ncs.getInt(1)="+cs.getInt(1)+" sb 1334 "+
			  "cs.getInt(3)="+cs.getInt(3)+" sb 1333 "+
			  "cs.getInt(4)="+cs.getInt(4)+" sb -1125"); 
	    }
	    if (!result) {
		sb.append("\nresult is "+result+" sb true"); 
	    }
	    if (!check1) {
		sb.append("\ncheck1 (more results) is "+check1+" sb true"); 
	    }
	    if (check2) {
		sb.append("\ncheck2 (even more results) is "+check2+" sb false"); 
	    } 

            cs.close ();
            assertCondition ((success) && (result == true) && (check1 == true) && (check2 == false),sb);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception "+sb);
        }
        }
    }




 
}





