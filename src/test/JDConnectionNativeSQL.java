///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionNativeSQL.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDConnectionNativeSQL.java
//
// Classes:      JDConnectionNativeSQL
//
////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.*;
import java.io.FileOutputStream;
import java.sql.*;
import java.util.Hashtable;


/**
Testcase JDConnectionNativeSQL.  This tests the following method
of the JDBC Connection class:

<ul>
<li>nativeSQL()
</ul>
**/
public class JDConnectionNativeSQL
extends JDTestcase
{



    // Private data.
    private              Connection     connection_;
    private              AS400          system_;
    private              int            vrm_;
    
    
    static       int            VRM_450 = AS400.generateVRM(4,5,0);
    private static       int            VRM_510 = AS400.generateVRM(5,1,0);
    private static       int            VRM_520 = AS400.generateVRM(5,2,0);
    private static       int            VRM_530 = AS400.generateVRM(5,3,0);
    private boolean    nativeDriver_ = false;
    private              boolean        v5r2nativeFunctions_ = false;


/**
Constructor.
**/
    public JDConnectionNativeSQL (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDConnectionNativeSQL",
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
        connection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);

	v5r2nativeFunctions_ = (getRelease() == JDTestDriver.RELEASE_V5R2M0) && (getDriver() == JDTestDriver.DRIVER_NATIVE);
        nativeDriver_ = getDriver() == JDTestDriver.DRIVER_NATIVE;
	if (nativeDriver_) {
            // @H1A  native driver uses different notion of VRM 
	    vrm_ = testDriver_.getRelease();
	    System.out.println("vrm = "+vrm_);

            // Set the VRM constants to match driver
	    VRM_450 = 450;
	    VRM_510 = 510;
	    VRM_520 = 520;
	    VRM_530 = 530;

	} else {
	    /* This cast doesn't work for native driver @H1A */ 
         
        try{
	    system_ = ((AS400JDBCConnection)connection_).getSystem();
        vrm_ = system_.getVRM();
        }catch(Exception e){
            vrm_ = testDriver_.getRelease();  //for proxy
            VRM_450 = 450;
            VRM_510 = 510;
            VRM_520 = 520;
            VRM_530 = 530;
        }
	    
	}
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
nativeSQL() - Pass null, should throw an exception.
**/
    public void Var001()
    {
        try {
            connection_.nativeSQL (null);
            failed("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
nativeSQL() - Pass "", should throw an exception.
**/
    public void Var002()
    {
        try {
            String translated = connection_.nativeSQL ("");
            failed("Didn't throw SQLException but got "+translated);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
nativeSQL() - Pass " ", should throw an exception.
**/
    public void Var003()
    {
        try {
            String translated = connection_.nativeSQL (" ");
            failed("Didn't throw SQLException but got "+translated);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
nativeSQL() - Pass a string with no escape clauses, should
return the same string.
**/
    public void Var004()
    {
        try {
            String sql = "SELECT * FROM SYSTABLES";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (sql), "actual("+translated+")!=expected("+sql+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() - Pass a string with \n and \t (not in quotes).
Should not convert them to whitespace.
**/
    public void Var005()
    {
        try {
            String sql = "SELECT COL1,\nCOL2 FROM\tMYTABLE1";
            String expected = "SELECT COL1,\nCOL2 FROM\tMYTABLE1";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() - Pass a string with \t at the begining.
Should trim it off.
**/
    public void Var006()
    {
        try {
            String sql = "\tSELECT COL1, COL2 FROM MYTABLE1";
            String expected = "SELECT COL1, COL2 FROM MYTABLE1";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() - Pass a string with \t at the end.
Should trim it off.
**/
    public void Var007()
    {
        try {
            String sql = "SELECT COL1, COL2 FROM MYTABLE1\t";
            String expected = "SELECT COL1, COL2 FROM MYTABLE1";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() - Pass a string with \n and \t (in single quotes).
Should not convert them to whitespace.
**/
    public void Var008()
    {
        try {
            String sql = "SELECT COL1, COL2 FROM MYTABLE1 "
                + "WHERE COL1='Charlie\tJin'";
            String expected = "SELECT COL1, COL2 FROM MYTABLE1 "
                + "WHERE COL1='Charlie\tJin'";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() - Pass a string with \n and \t (in double quotes).
Should not convert them to whitespace.
**/
    public void Var009()
    {
        try {
            String sql = "SELECT COL1, COL2 FROM MYTABLE1 "
                + "WHERE COL1=\"Charlie\tJin\"";
            String expected = "SELECT COL1, COL2 FROM MYTABLE1 "
                + "WHERE COL1=\"Charlie\tJin\"";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() - Pass a string with containing an escape clause
with a bad keyword, should throw an exception.
**/
    public void Var010()
    {
        try {
            String sql = "SELECT {badkeyword Hi Mom}) FROM MYTABLE";
            String translated = connection_.nativeSQL (sql);
            failed("Didn't throw SQLException but got "+translated);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
nativeSQL() - Pass a string with containing only an escape clause,
should return the correct SQL.
**/
    public void Var011()
    {
        try {
            String sql = "{call QIWS.QZDGTRGI}";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase ("CALL QIWS.QZDGTRGI"));
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }


/**
nativeSQL() - Pass a string with containing only an escape clause,
but with leading blanks, should return the correct SQL.
**/
    public void Var012()
    {
        try {
            String sql = "      {call QIWS.QZDGTRGI}";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase ("CALL QIWS.QZDGTRGI"));
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }


/**
nativeSQL() - Pass a string with containing only an escape clause,
but with trailing blanks, should return the correct SQL.
**/
    public void Var013()
    {
        try {
            String sql = "{call QIWS.QZDGTRGI}     ";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase ("CALL QIWS.QZDGTRGI"));
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() - Pass a string with containing an escape clause,
and more stuff after, should return the correct SQL.
**/
    public void Var014()
    {
        try {
            String sql = "{call QIWS.QZDGTRGI} MORE STUFF";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase ("CALL QIWS.QZDGTRGI MORE STUFF"));
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }


/**
nativeSQL() - Pass a string with containing an escape clause,
and more stuff before, should return the correct SQL.
**/
    public void Var015()
    {
        try {
            String sql = "MORE STUFF {call QIWS.QZDGTRGI}";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase ("MORE STUFF CALL QIWS.QZDGTRGI"));
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }


/**
nativeSQL() - Pass a string with containing an escape clause,
and more stuff before and after, should return the correct SQL.
**/
    public void Var016()
    {
        try {
            String sql = "MORE STUFF {call QIWS.QZDGTRGI} STILL MORE STUFF";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase ("MORE STUFF CALL QIWS.QZDGTRGI STILL MORE STUFF"));
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }


/**
nativeSQL() - Pass a string with containing an escape clause
with a valid keyword, should return the correct SQL.
**/
    public void Var017()
    {
        try {
            String sql = "{call QIWS.QZDGTRGI}";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase ("CALL QIWS.QZDGTRGI"));
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() - Pass a string with containing an escape clause
with a valid keyword but with odd case, should return the
correct SQL.
**/
    public void Var018()
    {
        try {
            String sql = "{cAlL QIWS.QZDGTRGI}";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase ("CALL QIWS.QZDGTRGI"));
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() - Pass a string with containing an escape clause
with a valid keyword but there are spaces after the opening
brace, should return the correct SQL.
**/
    public void Var019()
    {
        try {
            String sql = "{     call QIWS.QZDGTRGI}";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase ("CALL QIWS.QZDGTRGI"));
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() - Pass a string with containing an escape clause,
but no closing brace, should throw an exception.
**/
    public void Var020()
    {
        try {
            String sql = "{call QIWS.QZDGTRGI";
            String translated = connection_.nativeSQL (sql);
            failed("Didn't throw SQLException but got "+translated);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }


/**
nativeSQL() - Pass a string with containing an escape clause,
but no open brace, should throw an exception.
**/
    public void Var021()
    {
        try {
            String sql = "call QIWS.QZDGTRGI}";
            String translated = connection_.nativeSQL (sql);
            failed("Didn't throw SQLException but got "+translated);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
nativeSQL() - Pass a string with containing 3 escape clauses,
should return the correct SQL.
**/
    public void Var022()
    {
        try {
            String sql = "INSERT INTO MYTABLE "
                + "(TIMECOL, DATECOL, TIMESTAMPCOL) "
                + "VALUES ({t '12:12:12'}, {d '1997-01-01'}, "
                + "{ts '1997-01-01 12:12:12'})";
            String expected = "INSERT INTO MYTABLE "
                + "(TIMECOL, DATECOL, TIMESTAMPCOL) "
                + "VALUES ('12:12:12', '1997-01-01', "
                + "'1997-01-01-12.12.12')";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() - Pass a string with nested escape clauses,
should return the correct SQL.
// pi supported at V5R1 and higher
**/
    public void Var023()
    {
        try {
            String sql = "SELECT {fn abs({fn pi()})} FROM MYTABLE";
            String expected;
            if (vrm_ < VRM_510 )
                expected = "SELECT ABS(3.1415926E00) FROM MYTABLE"; 
            else
                expected = "SELECT ABS(PI()) FROM MYTABLE";

            if (vrm_ < VRM_530 && nativeDriver_) {
                expected = "SELECT ABSVAL(3.1415926E00) FROM MYTABLE"; 
	    } 
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }


/**
nativeSQL() - Pass a string with containing escape clauses
within a single quoted string.  The escape clause should not get
translated.
**/
    public void Var024()
    {
        try {
            String sql = "INSERT INTO MYTABLE "
                + "(PICOL) VALUES ('{fn pi }')";
            String expected = "INSERT INTO MYTABLE "
                + "(PICOL) VALUES ('{fn pi }')";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() - Pass a string with containing escape clauses
within a double quoted string.  The escape clause should not get
translated.
**/
    public void Var025()
    {
        try {
            String sql = "INSERT INTO MYTABLE "
                + "(PICOL) VALUES (\"{fn pi }\")";
            String expected = "INSERT INTO MYTABLE "
                + "(PICOL) VALUES (\"{fn pi }\")";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() - Pass a string with containing escape clauses
several quoted strings, should return the correct SQL.
**/
    public void Var026()
    {
        try {
            String sql = "INSERT INTO MYTABLE "
                + "(PICOL \"HiMom\") VALUES ('{fn pi }')";
            String expected = "INSERT INTO MYTABLE "
                + "(PICOL \"HiMom\") VALUES ('{fn pi }')";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() "escape" - Pass a string with containing an "escape"
escape clause, should return the correct SQL.
**/
    public void Var027()
    {
        try {
            String sql = "SELECT * FROM MYTABLE WHERE "
                + "CHARCOL LIKE '/%' {escape '/'}";
            String expected = "SELECT * FROM MYTABLE WHERE "
                + "CHARCOL LIKE '/%' ESCAPE '/'";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() "escape" - Pass a string with containing an "escape"
escape clause and no arguments, should throw an exception.
**/
    public void Var028()
    {
        try {
            String sql = "SELECT * FROM MYTABLE WHERE "
                + "CHARCOL LIKE '/%' {escape}";
            String translated = connection_.nativeSQL (sql);
            failed("Didn't throw SQLException but got "+translated);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
nativeSQL() "escape" - Pass a string with containing an "escape"
escape clause and 2 arguments, should return the correct SQL.
**/
    public void Var029()
    {
        try {
            String sql = "SELECT * FROM MYTABLE WHERE "
                + "CHARCOL LIKE '/%' {escape Hi Mom}";
            String expected = "SELECT * FROM MYTABLE WHERE "
                + "CHARCOL LIKE '/%' ESCAPE Hi Mom";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() "fn" - Pass a string with containing a scalar function,
should return the correct SQL.
**/
    public void Var030()
    {
        try {
            String sql = "INSERT INTO MYTABLE "
                + "(CHARCOL) VALUES ({fn user()})";
            String expected = "INSERT INTO MYTABLE "
                + "(CHARCOL) VALUES (USER)";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "fn" - Pass a string with containing a scalar function,
where the scalar function name has odd case, should return the
correct SQL.
**/
    public void Var031()
    {
        try {
            String sql = "INSERT INTO MYTABLE "
                + "(CHARCOL) VALUES ({fn uSeR()})";
            String expected = "INSERT INTO MYTABLE "
                + "(CHARCOL) VALUES (USER)";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "fn" - Pass a string with containing a scalar function
with no arguments, should return the correct SQL.
**/
    public void Var032()
    {
        try {
            String sql = "INSERT INTO MYTABLE "
                + "(CHARCOL) VALUES ({fn user()})";
            String expected = "INSERT INTO MYTABLE "
                + "(CHARCOL) VALUES (USER)";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "fn" - Pass a string with containing a scalar function
with 1 argument, should return the correct SQL.
Native Driver returns ABSVAL in V5R2.
**/
    public void Var033()
    {
        try {
            String sql = "SELECT {fn abs(INTEGERCOL)} FROM MYTABLE";
            String expected = "SELECT ABS(INTEGERCOL) FROM MYTABLE";
	    if (vrm_ < VRM_530 && nativeDriver_) {
		expected = "SELECT ABSVAL(INTEGERCOL) FROM MYTABLE";
	    } 
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "fn" - Pass a string with containing a scalar function
with 2 arguments, should return the correct SQL.

@H1C -- The native driver is the same
**/
    public void Var034()
    {
        try {
            String sql = "SELECT {fn concat(CHARCOL1, CHARCOL2)}) FROM MYTABLE";
            String expected;
	    if (vrm_ < VRM_530 && nativeDriver_) {
		expected = "SELECT CONCAT(CHARCOL1,  CHARCOL2)) FROM MYTABLE";
	    } else { 
		expected = "SELECT CONCAT(CHARCOL1, CHARCOL2)) FROM MYTABLE";
	    }
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() "fn" - Pass a string with containing a scalar function
with 2 arguments, and many spaces around the comma - should
return the correct SQL.

@H1C -- The native driver is the same.
**/
    public void Var035()
    {
        try {
            String sql = "SELECT {fn concat(CHARCOL1    ,     CHARCOL2)}) FROM MYTABLE";

            String expected;
	    if (vrm_ < VRM_530 && nativeDriver_) {
		expected = "SELECT CONCAT(CHARCOL1    ,      CHARCOL2)) FROM MYTABLE";
	    } else { 
		expected = "SELECT CONCAT(CHARCOL1    ,     CHARCOL2)) FROM MYTABLE";
	    }
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "fn" - Pass a string with containing a scalar function
with the wrong number of arguments, should throw an exception.
**/
    public void Var036()
    {
        // the native driver throws an Exception in Connection.nativeSQL, Toolbox lets the server throw it
        // @H1C -- native driver now lets the server throw it 
        try {
            String sql = "SELECT {fn concat(CHARCOL1, CHARCOL2, CHARCOL3)} FROM MYTABLE";
            String translated = connection_.nativeSQL (sql);
	    Statement s = connection_.createStatement();
	    ResultSet rs = s.executeQuery(sql);

            failed("Didn't throw SQLException  but got "+translated +" and "+rs );
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
nativeSQL() "fn" - Pass a string with containing a scalar function
that we do not support, should throw an exception.
**/
    public void Var037()
    {
        // the native driver throws an Exception in Connection.nativeSQL, Toolbox lets the server throw it
        try {
            String sql = "SELECT {fn ascii ('a')} FROM MYTABLE";
            String translated = connection_.nativeSQL (sql);
	    Statement s = connection_.createStatement();
	    ResultSet rs = s.executeQuery(sql);
            failed("Didn't throw SQLException  but got "+translated+" rs="+rs);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }




/**
nativeSQL() "fn" - Pass a string with containing a scalar function
that does not exist, should throw an exception.
**/
    public void Var038()
    {
        // the native driver throws an Exception in Connection.nativeSQL, Toolbox lets the server throw it
        try {
            String sql = "SELECT {fn badfn()} FROM MYTABLE";
            String translated = connection_.nativeSQL (sql);
	    Statement s = connection_.createStatement();
	    ResultSet rs = s.executeQuery(sql);

            failed("Didn't throw SQLException  "+translated +" and "+rs);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
nativeSQL() "fn" - Pass a string with containing a scalar function
and no parentheses, should throw an exception.
**/
    public void Var039()
    {
        try {
            String sql = "SELECT {fn abs INTEGERCOL} FROM MYTABLE";
            String translated = connection_.nativeSQL (sql);
            failed("Didn't throw SQLException but got "+translated);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
nativeSQL() "fn" - Pass a string with containing a scalar function
and no close parenthesis, should throw an exception.
**/
    public void Var040()
    {
        try {
            String sql = "SELECT {fn abs (INTEGERCOL}) FROM MYTABLE";
            String translated = connection_.nativeSQL (sql);
            failed("Didn't throw SQLException but got "+translated);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
nativeSQL() "fn" - Pass a string with containing a scalar function
and no open parenthesis, should throw an exception.
**/
    public void Var041()
    {
        try {
            String sql = "SELECT {fn abs INTEGERCOL)}) FROM MYTABLE";
            String translated = connection_.nativeSQL (sql);
            failed("Didn't throw SQLException but got "+translated);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
nativeSQL() "fn" - Test ABS scalar function.
**/
    public void Var042()
    {
        try {
            String sql = "SELECT {fn abs(INTEGERCOL)} FROM MYTABLE";
            String expected = "SELECT ABS(INTEGERCOL) FROM MYTABLE";
	    if (vrm_ < VRM_530 && nativeDriver_) {
		expected = "SELECT ABSVAL(INTEGERCOL) FROM MYTABLE";
	    } 
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() "fn" - Test ACOS scalar function.
**/
    public void Var043()
    {
        try {
            String sql = "SELECT {fn acos(NUMCOL)} FROM MYTABLE";
            String expected = "SELECT ACOS(NUMCOL) FROM MYTABLE";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() "fn" - Test ASIN scalar function.
**/
    public void Var044()
    {
        try {
            String sql = "SELECT {fn asin(NUMCOL)} FROM MYTABLE";
            String expected = "SELECT ASIN(NUMCOL) FROM MYTABLE";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() "fn" - Test ATAN scalar function.
**/
    public void Var045()
    {
        try {
            String sql = "SELECT {fn atan(NUMCOL)} FROM MYTABLE";
            String expected = "SELECT ATAN(NUMCOL) FROM MYTABLE";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() "fn" - Test ATAN2 scalar function.
**/
    public void Var046()
    {
	if ( getRelease() ==  JDTestDriver.RELEASE_V5R1M0 && getDriver() == JDTestDriver.DRIVER_NATIVE ) {
	    try {
		String sql = "SELECT {fn atan2(FLOATCOL1, FLOATCOL2)} FROM MYTABLE";
		String translated = connection_.nativeSQL (sql);
		failed("Didn't throw SQLException  but got "+translated);
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	} else { 
	    try {
		String sql = "SELECT {fn atan2(FLOATCOL1, FLOATCOL2)} FROM MYTABLE";
		String expected = "SELECT ATAN2(FLOATCOL1, FLOATCOL2) FROM MYTABLE";
		if (vrm_ < VRM_530 && nativeDriver_) {
		    expected = "SELECT ATAN2(FLOATCOL1,  FLOATCOL2) FROM MYTABLE";
		} 
		String translated = connection_.nativeSQL (sql);
		assertCondition(translated.equalsIgnoreCase(expected));
	    }
	    catch (Exception e) {
		failed(e , "Unexpected Exception");
	    }
	}
    }



/**
nativeSQL() "fn" - Test CEILING scalar function.
**/
    public void Var047()
    {
        try {
            String sql = "SELECT {fn ceiling(INTEGERCOL)} FROM MYTABLE";
            String translated = connection_.nativeSQL (sql);
            String expected = "SELECT CEILING(INTEGERCOL) FROM MYTABLE";
            assertCondition(translated.equalsIgnoreCase(expected), "translated("+translated+") != expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() "fn" - Test COS scalar function.
**/
    public void Var048()
    {
        try {
            String sql = "SELECT {fn cos(FLOATCOL)} FROM MYTABLE";
            String expected = "SELECT COS(FLOATCOL) FROM MYTABLE";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() "fn" - Test COT scalar function.
**/
    public void Var049()
    {
        try {
            String sql = "SELECT {fn cot(FLOATCOL)} FROM MYTABLE";
            String expected = "SELECT COT(FLOATCOL) FROM MYTABLE";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() "fn" - Test DEGREES scalar function.
**/
    public void Var050()
    {
        try {
            String sql = "SELECT {fn degrees(INTEGERCOL)} FROM MYTABLE";
            String expected = "SELECT DEGREES(INTEGERCOL) FROM MYTABLE";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() "fn" - Test EXP scalar function.
**/
    public void Var051()
    {
        try {
            String sql = "SELECT {fn exp(FLOATCOL)} FROM MYTABLE";
            String expected = "SELECT EXP(FLOATCOL) FROM MYTABLE";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() "fn" - Test FLOOR scalar function.
**/
    public void Var052()
    {
        try {
            String sql = "SELECT {fn floor(INTEGERCOL)} FROM MYTABLE";
            String expected = "SELECT FLOOR(INTEGERCOL) FROM MYTABLE";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() "fn" - Test LOG scalar function.

@H1C - the native driver is now the same. 
**/
    public void Var053()
    {
        try {
            String sql = "SELECT {fn log(FLOATCOL)} FROM MYTABLE";
            String expected;

	    expected = "SELECT LN(FLOATCOL) FROM MYTABLE";
	    if (vrm_ < VRM_530 && nativeDriver_) {
		expected = "SELECT LOG(FLOATCOL) FROM MYTABLE";
	    } 
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() "fn" - Test LOG10 scalar function.
**/
    public void Var054()
    {
        try {
            String sql = "SELECT {fn log10(FLOATCOL)} FROM MYTABLE";
            String expected = "SELECT LOG10(FLOATCOL) FROM MYTABLE";
            String translated = connection_.nativeSQL (sql);

            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() "fn" - Test MOD scalar function.
**/
    public void Var055()
    {
        try {
            String sql = "SELECT {fn mod(INTEGERCOL1, INTEGERCOL2)} FROM MYTABLE";
            String expected = "SELECT MOD(INTEGERCOL1, INTEGERCOL2) FROM MYTABLE";
	    if (vrm_ < VRM_530 && nativeDriver_) {
		   expected = "SELECT MOD(INTEGERCOL1,  INTEGERCOL2) FROM MYTABLE";
	    } 
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() "fn" - Test PI scalar function.
pi supported at V5R1 and higher
**/
    public void Var056()
    {
        try {
            String sql = "SELECT {fn pi()} FROM MYTABLE";
            String expected;
            if (vrm_ < VRM_510)
                expected = "SELECT 3.1415926E00 FROM MYTABLE";
            else
                expected = "SELECT PI() FROM MYTABLE";
	    if (vrm_ < VRM_530 && nativeDriver_) {
		expected = "SELECT 3.1415926E00 FROM MYTABLE"; 
	    }
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() "fn" - Test POWER scalar function.
**/
    public void Var057()
    {
        try {
            String sql = "SELECT {fn power(INTEGERCOL1, INTEGERCOL2)} FROM MYTABLE";
            String expected = "SELECT POWER(INTEGERCOL1, INTEGERCOL2) FROM MYTABLE";
	    if (vrm_ < VRM_530 && nativeDriver_) {
		expected = "SELECT POWER(INTEGERCOL1,  INTEGERCOL2) FROM MYTABLE";
	    }
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() "fn" - Test RADIANS scalar function.
radians supported at V5R1 and higher
-- In v5r1 the native driver will throw an exception.. 
**/
    public void Var058()
    {   if (vrm_ >= VRM_510) {
            try {
                String sql = "SELECT {fn radians(INTEGERCOL)} FROM MYTABLE";
                String expected = "SELECT RADIANS(INTEGERCOL) FROM MYTABLE";
                String translated = connection_.nativeSQL (sql);
                assertCondition(translated.equalsIgnoreCase(expected));
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        } else {
            notApplicable("RADIANS supported only at V5R1M0 and higher.");
        }
    }



/**
nativeSQL() "fn" - Test RAND scalar function.
rand supported at V5R1 and higher
**/
    public void Var059()
    {
        if (vrm_ >= VRM_510) {
            try {
                String sql = "SELECT {fn rand(INTEGERCOL)} FROM MYTABLE";
                String expected = "SELECT RAND(INTEGERCOL) FROM MYTABLE";
                String translated = connection_.nativeSQL (sql);
                assertCondition(translated.equalsIgnoreCase(expected));
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        } else {
            notApplicable("RAND supported only at V5R1M0 and higher.");
        }
    }



/**
nativeSQL() "fn" - Test ROUND scalar function.
**/
    public void Var060()
    {
        try {
            String sql = "SELECT {fn round(INTEGERCOL)} FROM MYTABLE";
            String expected = "SELECT ROUND(INTEGERCOL) FROM MYTABLE";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "fn" - Test SIGN scalar function.
**/
    public void Var061()
    {
        try {
            String sql = "SELECT {fn sign(INTEGERCOL)} FROM MYTABLE";
            String expected = "SELECT SIGN(INTEGERCOL) FROM MYTABLE";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "fn" - Test SIN scalar function.
**/
    public void Var062()
    {
        try {
            String sql = "SELECT {fn sin(FLOATCOL)} FROM MYTABLE";
            String expected = "SELECT SIN(FLOATCOL) FROM MYTABLE";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "fn" - Test SQRT scalar function.
**/
    public void Var063()
    {
        try {
            String sql = "SELECT {fn sqrt(FLOATCOL)} FROM MYTABLE";
            String expected = "SELECT SQRT(FLOATCOL) FROM MYTABLE";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "fn" - Test TAN scalar function.
**/
    public void Var064()
    {
        try {
            String sql = "SELECT {fn tan(FLOATCOL)} FROM MYTABLE";
            String expected = "SELECT TAN(FLOATCOL) FROM MYTABLE";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "fn" - Test TRUNCATE scalar function.
**/
    public void Var065()
    {
        try {
            String sql = "SELECT {fn truncate(FLOATCOL, 5)} FROM MYTABLE";
            String expected = "SELECT TRUNCATE(FLOATCOL, 5) FROM MYTABLE";
	    if (vrm_ < VRM_530 && nativeDriver_) {
		expected = "SELECT TRUNCATE(FLOATCOL,  5) FROM MYTABLE";
	    }
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "fn" - Test ASCII scalar function.  Not supported,
should throw an exception.
**/
    public void Var066()
    {
        try {
            String sql = "SELECT {fn ascii(CHARCOL)} FROM MYTABLE";
            String translated = connection_.nativeSQL (sql);
	    Statement s = connection_.createStatement();
	    ResultSet rs = s.executeQuery(sql);

            failed("Didn't throw SQLException  but got "+translated+" rs="+rs);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }




/**
nativeSQL() "fn" - Test CHAR scalar function.  Not supported,
should throw an exception.
**/
    public void Var067()
    {
        try {
            String sql = "SELECT {fn char(CHARCOL)} FROM MYTABLE";
            String translated = connection_.nativeSQL (sql);
	    Statement s = connection_.createStatement();
	    ResultSet rs = s.executeQuery(sql);

            failed("Didn't throw SQLException  but got "+translated+" rs="+rs);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }




/**
nativeSQL() "fn" - Test CONCAT scalar function.
**/
    public void Var068()
    {
        try {
            String sql = "SELECT {fn concat(CHARCOL1, CHARCOL2)} FROM MYTABLE";
            String expected = "SELECT CONCAT(CHARCOL1, CHARCOL2) FROM MYTABLE";
	    if (vrm_ < VRM_530 && nativeDriver_) {
		expected = "SELECT CONCAT(CHARCOL1,  CHARCOL2) FROM MYTABLE";
	    }
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "fn" - Test DIFFERENCE scalar function. 
difference supported at V5R1 and higher
**/
    public void Var069()
    {
	if (v5r2nativeFunctions_) {
	    try {
		String sql = "SELECT {fn difference(CHARCOL1,CHARCOL2)} FROM MYTABLE";
		String expected = "SELECT DIFFERENCE(CHARCOL1, CHARCOL2) FROM MYTABLE";
		String translated = connection_.nativeSQL (sql);

		boolean condition = translated.equals (expected);
		if (!condition) {
		    System.out.println("expected   = '"+expected+"'");
		    System.out.println("translated = '"+translated+"'");  
		} 
		assertCondition (condition); 

	    }
	    catch (Exception e) {
		failed(e , "Unexpected Exception");
	    }
	} else { 
	    if (vrm_ >= VRM_510) {
		try {
		    String sql = "SELECT {fn difference(INTEGERCOL)} FROM MYTABLE";
		    String expected = "SELECT DIFFERENCE(INTEGERCOL) FROM MYTABLE";
		    String translated = connection_.nativeSQL (sql);
		    assertCondition(translated.equalsIgnoreCase(expected));
		}
		catch (Exception e) {
		    failed(e, "Unexpected Exception");
		}
	    } else {
		notApplicable("DIFFERENCE supported only at V5R1M0 and higher.");
	    }
	}
    }




/**
nativeSQL() "fn" - Test INSERT scalar function.
**/
    public void Var070()
    {
        try {
            String sql = "SELECT {fn insert(CHARCOL1,5,7,CHARCOL2)} FROM MYTABLE";
	    String expected;
	    if (vrm_ < VRM_530) {
		expected = "SELECT SUBSTR(CHARCOL1, 1, 5 - 1) "
                + "|| CHARCOL2 || SUBSTR(CHARCOL1, 5 + 7) FROM MYTABLE";
	    } else {
		expected = "SELECT insert(CHARCOL1,5,7,CHARCOL2) FROM MYTABLE";
	    } 
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "fn" - Test LCASE scalar function.
lcase supported at V5R2 and higher
The native driver returns LOWER prior to V5R3.
**/
    public void Var071()
    {
        if (vrm_ >= VRM_520) {
            try {
                String sql = "SELECT {fn lcase(CHARCOL)} FROM MYTABLE";
                String expected = "SELECT LCASE(CHARCOL) FROM MYTABLE";
		if (vrm_ < VRM_530 && nativeDriver_) {
		    expected = "SELECT LOWER(CHARCOL) FROM MYTABLE";
		}

                String translated = connection_.nativeSQL (sql);
                assertCondition(translated.equalsIgnoreCase(expected));
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        } else {
            notApplicable("LCASE supported only at V5R2M0 and later.");
        }
    }




/**
nativeSQL() "fn" - Test LEFT scalar function.
Native driver returns SUBSTR prior to V5R3
**/
    public void Var072()
    {
        try {
            String sql = "SELECT {fn left(CHARCOL, 7)} FROM MYTABLE";
	    String expected = "SELECT LEFT(CHARCOL, 7) FROM MYTABLE";
	    if (vrm_ < VRM_530 && nativeDriver_) {
		expected = "SELECT SUBSTR(CHARCOL, 1,  7) FROM MYTABLE";
	    }

            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "fn" - Test LENGTH scalar function.
**/
    public void Var073()
    {
        try {
            String sql = "SELECT {fn length(CHARCOL)} FROM MYTABLE";
            //@I1D String expected = "SELECT LENGTH(STRIP(CHARCOL,T,' ')) FROM MYTABLE";
            String expected = "SELECT LENGTH(STRIP(CHARCOL,T)) FROM MYTABLE";           //@I1A
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "fn" - Test LOCATE scalar function.
**/
    public void Var074()
    {
        try {
            String sql = "SELECT {fn locate(CHARCOL1, CHARCOL2, 5)} FROM MYTABLE";
            String expected = "SELECT LOCATE(CHARCOL1, CHARCOL2, 5) FROM MYTABLE";
	    if (vrm_ < VRM_530 && nativeDriver_) {
		expected = "SELECT LOCATE(CHARCOL1,  CHARCOL2,  5) FROM MYTABLE";
	    }
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "fn" - Test LTRIM scalar function.
The native driver returns STRIP prior to v5r3.
**/
    public void Var075()
    {
        try {
            String sql = "SELECT {fn ltrim(CHARCOL)} FROM MYTABLE";
	    String expected = "SELECT LTRIM(CHARCOL) FROM MYTABLE";
	    if (vrm_ < VRM_530 && nativeDriver_) {
		expected = "SELECT STRIP(CHARCOL,L,' ') FROM MYTABLE"; 
	    }
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "fn" - Test REPEAT scalar function.
repeat supported at V5R3 and higher
**/
    public void Var076()
    {
	if (v5r2nativeFunctions_) {
	    try {
		String sql = "SELECT {fn repeat(CHARCOL,6)} FROM MYTABLE";
		String translated = connection_.nativeSQL (sql);
		failed("Didn't throw SQLException but got "+translated);
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	} else {
	    try {
		String sql = "SELECT {fn repeat(CHARCOL,6)} FROM MYTABLE";
		String expected = "SELECT REPEAT(CHARCOL,6) FROM MYTABLE";
		if (vrm_ < VRM_530 && nativeDriver_) {
		}
		String translated = connection_.nativeSQL(sql);
		assertCondition(translated.equalsIgnoreCase(expected));
	    }
	    catch (Exception e) {
		failed(e, "Unexpected Exception");
	    }
	}
    }




/**
nativeSQL() "fn" - Test REPLACE scalar function.
replace supported at V5R3 and higher
**/
    public void Var077()
    {
	if (v5r2nativeFunctions_) {
	    try {
		String sql = "SELECT {fn replace(CHARCOL,'HELLO','GOODBYE')} FROM MYTABLE";
		String translated = connection_.nativeSQL (sql);
		failed("Didn't throw SQLException but got "+translated);
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }

	} else { 
	    try {
		String sql = "SELECT {fn replace(CHARCOL,'HELLO','GOODBYE')} FROM MYTABLE";
		String expected = "SELECT REPLACE(CHARCOL,'HELLO','GOODBYE') FROM MYTABLE";
		if (vrm_ < VRM_530 && nativeDriver_) {
		}
		String translated = connection_.nativeSQL (sql);
		assertCondition(translated.equalsIgnoreCase(expected));
	    }
	    catch (Exception e) {
		failed(e, "Unexpected Exception");
	    }
	}
    }




/**
nativeSQL() "fn" - Test RIGHT scalar function.
**/
    public void Var078()
    {
        try {
            String sql = "SELECT {fn right(CHARCOL,1)} FROM MYTABLE";
	    String expected;
	    if (vrm_ < VRM_530) { 
               expected = "SELECT SUBSTR(CHARCOL, LENGTH(CHARCOL) - 1 + 1) FROM MYTABLE";
	    } else {
	       expected = "SELECT right(CHARCOL,1) FROM MYTABLE"; 
	    }

            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "fn" - Test RTRIM scalar function.
Native driver returns STRIP prior to V5R3
**/
    public void Var079()
    {
        try {
            String sql = "SELECT {fn rtrim(CHARCOL)} FROM MYTABLE";
            String expected = "SELECT RTRIM(CHARCOL) FROM MYTABLE";
	    if (vrm_ < VRM_530 && nativeDriver_) {
		expected = "SELECT STRIP(CHARCOL,T,' ') FROM MYTABLE";
	    }
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "fn" - Test SOUNDEX scalar function.
soundex supported at V5R1 and higher
**/
    public void Var080()
    {
        if (vrm_ >= VRM_510) {
            try {
                String sql = "SELECT {fn soundex(CHARCOL)} FROM MYTABLE";
                String expected = "SELECT SOUNDEX(CHARCOL) FROM MYTABLE";
                String translated = connection_.nativeSQL (sql);
                assertCondition(translated.equalsIgnoreCase(expected));
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        } else { 
            notApplicable("SOUNDEX supported only at V5R1M0 and higher.");
        }
    }




/**
nativeSQL() "fn" - Test SPACE scalar function.
space supported at V5R1 and higher
**/
    public void Var081()
    {
        if (vrm_ >= VRM_510) {
            try {
                String sql = "SELECT {fn space(80)} FROM MYTABLE";
                String expected = "SELECT SPACE(80) FROM MYTABLE";
                String translated = connection_.nativeSQL (sql);
                assertCondition(translated.equalsIgnoreCase(expected));
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        } else {
            notApplicable("SPACE supported only at V5R1M0 and higher.");
        }
    }




/**
nativeSQL() "fn" - Test SUBSTRING scalar function.
The native driver returns SUBSTR prior to V5R3
**/
    public void Var082()
    {
        try {
            String sql = "SELECT {fn substring(CHARCOL, 6, 3)} FROM MYTABLE";
            String expected = "SELECT SUBSTRING(CHARCOL, 6, 3) FROM MYTABLE";
	    if (vrm_ < VRM_530 && nativeDriver_) {
		expected = "SELECT SUBSTR(CHARCOL,  6,  3) FROM MYTABLE";
	    }
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "fn" - Test UCASE scalar function.
**/
    public void Var083()
    {
        try {
            String sql = "SELECT {fn ucase(CHARCOL)} FROM MYTABLE";
            String expected = "SELECT UCASE(CHARCOL) FROM MYTABLE";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "fn" - Test CURDATE scalar function.
The native driver returns CURRENT DATE prior to V5R3
**/
    public void Var084()
    {
        try {
            String sql = "SELECT {fn curdate()} FROM MYTABLE";
            String expected = "SELECT CURDATE() FROM MYTABLE";
	    if (vrm_ < VRM_530 && nativeDriver_) {
		expected = "SELECT CURRENT DATE FROM MYTABLE"; 
	    }
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "fn" - Test CURTIME scalar function.
The native driver returns CURRENT TIME prior to V5R3
**/
    public void Var085()
    {
        try {
            String sql = "SELECT {fn curtime()} FROM MYTABLE";
            String expected = "SELECT CURTIME() FROM MYTABLE";
	    if (vrm_ < VRM_530 && nativeDriver_) {
		expected = "SELECT CURRENT TIME FROM MYTABLE";
	    }
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "fn" - Test DAYNAME scalar function.
dayname supported at V5R3 and higher
**/
    public void Var086()
    {
	if (v5r2nativeFunctions_) {
	    try {
		String sql = "SELECT {fn dayname(DATECOL)} FROM MYTABLE";
		String translated = connection_.nativeSQL (sql);
		failed("Didn't throw SQLException but got "+translated);
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	} else { 
	    try {
		String sql = "SELECT {fn dayname(DATECOL)} FROM MYTABLE";
		String expected = "SELECT DAYNAME(DATECOL) FROM MYTABLE";
		if (vrm_ < VRM_530 && nativeDriver_) {
		}
		String translated = connection_.nativeSQL (sql);
		assertCondition(translated.equalsIgnoreCase(expected));
	    }
	    catch (Exception e) {
		failed(e, "Unexpected Exception");
	    }
	}
    }




/**
nativeSQL() "fn" - Test DAYOFMONTH scalar function.
The native driver returns DAY prior to V5R3
**/
    public void Var087()
    {
        try {
            String sql = "SELECT {fn dayofmonth(DATECOL)} FROM MYTABLE";
            String expected;
            if (vrm_ < VRM_510)
                expected = "SELECT DAY(DATECOL) FROM MYTABLE";
            else
                expected = "SELECT DAYOFMONTH(DATECOL) FROM MYTABLE";
	    if (vrm_ < VRM_530 && nativeDriver_) {
		expected = "SELECT DAY(DATECOL) FROM MYTABLE";
	    }

            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "fn" - Test DAYOFWEEK scalar function.
**/
    public void Var088()
    {
        try {
            String sql = "SELECT {fn dayofweek(DATECOL)} FROM MYTABLE";
            String expected = "SELECT DAYOFWEEK(DATECOL) FROM MYTABLE";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }        
    }




/**
nativeSQL() "fn" - Test DAYOFYEAR scalar function.
**/
    public void Var089()
    {
        try {
            String sql = "SELECT {fn dayofyear(DATECOL)} FROM MYTABLE";
            String expected = "SELECT DAYOFYEAR(DATECOL) FROM MYTABLE";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }        
    }




/**
nativeSQL() "fn" - Test HOUR scalar function.
**/
    public void Var090()
    {
        try {
            String sql = "SELECT {fn hour(TIMECOL)} FROM MYTABLE";
            String expected = "SELECT HOUR(TIMECOL) FROM MYTABLE";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "fn" - Test MINUTE scalar function.
**/
    public void Var091()
    {
        try {
            String sql = "SELECT {fn minute(TIMECOL)} FROM MYTABLE";
            String expected = "SELECT MINUTE(TIMECOL) FROM MYTABLE";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "fn" - Test MONTH scalar function.
**/
    public void Var092()
    {
        try {
            String sql = "SELECT {fn month(DATECOL)} FROM MYTABLE";
            String expected = "SELECT MONTH(DATECOL) FROM MYTABLE";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "fn" - Test MONTHNAME scalar function.
monthname supported at V5R3 and higher
**/
    public void Var093()
    {
	if (v5r2nativeFunctions_) {
	    try {
		String sql = "SELECT {fn monthname(DATECOL)} FROM MYTABLE";
		String translated = connection_.nativeSQL (sql);
		failed("Didn't throw SQLException but got "+translated);
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	} else { 
	    try {
		String sql = "SELECT {fn monthname(DATECOL)} FROM MYTABLE";
		String expected = "SELECT MONTHNAME(DATECOL) FROM MYTABLE";
		if (vrm_ < VRM_530 && nativeDriver_) {
		}
		String translated = connection_.nativeSQL (sql);
		assertCondition(translated.equalsIgnoreCase(expected));
	    }
	    catch (Exception e) {
		failed(e, "Unexpected Exception");
	    }
	}
    }




/**
nativeSQL() "fn" - Test NOW scalar function.
The native driver returns CURRENT TIMESTAMP prior to V5R3
**/
    public void Var094()
    {
        try {
            String sql = "SELECT {fn now()} FROM MYTABLE";
            String expected = "SELECT NOW() FROM MYTABLE";
	    if (vrm_ < VRM_530 && nativeDriver_) {
		expected = "SELECT CURRENT TIMESTAMP FROM MYTABLE";
	    }
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "fn" - Test QUARTER scalar function.
**/
    public void Var095()
    {
        try {
            String sql = "SELECT {fn quarter(INTEGERCOL)} FROM MYTABLE";
            String expected = "SELECT QUARTER(INTEGERCOL) FROM MYTABLE";
            String translated = connection_.nativeSQL (sql);
            assertCondition(translated.equalsIgnoreCase(expected));
        }
        catch (Exception e) {
            failed(e, "Unexpected Exception");
        }
    }




/**
nativeSQL() "fn" - Test SECOND scalar function.
**/
    public void Var096()
    {
        try {
            String sql = "SELECT {fn second(TIMECOL)} FROM MYTABLE";
            String expected = "SELECT SECOND(TIMECOL) FROM MYTABLE";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "fn" - Test TIMESTAMPADD scalar function. Not supported,
should throw an exception.
**/
    public void Var097()
    {
        // native throws an exception in .nativeSQL(), we let the server throw it
        try {
            String sql = "SELECT {fn timestampadd(SQL_TSI_HOUR,5,TSCOL)} FROM MYTABLE";
            String translated = connection_.nativeSQL (sql);
	    Statement s = connection_.createStatement();
	    ResultSet rs = s.executeQuery(sql);

            failed("Didn't throw SQLException  but got "+translated+" rs="+rs);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }




/**
nativeSQL() "fn" - Test TIMESTAMPDIFF scalar function.
timestampdiff supported at V5R1
**/
    public void Var098()
    {
        if (vrm_ >= VRM_510) {
            try {
                String sql = "SELECT {fn timestampdiff(SQL_TSI_MONTH,TSCOL1,TSCOL2)} FROM MYTABLE";
                String expected = "SELECT TIMESTAMPDIFF(SQL_TSI_MONTH,TSCOL1,TSCOL2) FROM MYTABLE";
                String translated = connection_.nativeSQL (sql);
                assertCondition(translated.equalsIgnoreCase(expected),"actual("+translated+")!=expected("+expected+")");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        } else {
            notApplicable("TIMESTAMPDIFF supported only at V5R1M0 and higher.");
        }
    }




/**
nativeSQL() "fn" - Test WEEK scalar function.
**/
    public void Var099()
    {
        try {
            String sql = "SELECT {fn week(DATECOL)} FROM MYTABLE";
	    String expected = "";
	    if (isToolboxDriver())
	    {
		expected = "SELECT WEEK(DATECOL) FROM MYTABLE";
	    }
	    else
	    {
		expected = "SELECT WEEK_ISO(DATECOL) FROM MYTABLE";
	    }
            String translated = connection_.nativeSQL (sql);
            assertCondition(translated.equalsIgnoreCase(expected),"actual("+translated+")!=expected("+expected+")" );
        }
        catch (Exception e) {
            failed(e, "Unexpected Exception");
        }
    }




/**
nativeSQL() "fn" - Test YEAR scalar function.
**/
    public void Var100()
    {
        try {
            String sql = "SELECT {fn year(DATECOL)} FROM MYTABLE";
            String expected = "SELECT YEAR(DATECOL) FROM MYTABLE";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "fn" - Test DATABASE scalar function.
**/
    public void Var101()
    {
        try {
            String sql = "SELECT {fn database()} FROM MYTABLE";
	    String expected;
	    if (vrm_ < VRM_530) { 
		expected = "SELECT CURRENT SERVER FROM MYTABLE";
	    } else {
		expected = "SELECT database() FROM MYTABLE"; 
	    }
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "fn" - Test IFNULL scalar function.
The native driver returns VALUE prior to V5R3
**/
    public void Var102()
    {
        try {
            String sql = "SELECT {fn ifnull(INTEGERCOL, -1)} FROM MYTABLE";
            String expected = "SELECT IFNULL(INTEGERCOL, -1) FROM MYTABLE";
	    if (vrm_ < VRM_530 && nativeDriver_) {
		expected = "SELECT VALUE(INTEGERCOL,  -1) FROM MYTABLE";
	    }
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "fn" - Test USER scalar function.
**/
    public void Var103()
    {
        try {
            String sql = "SELECT {fn user()} FROM MYTABLE";
            String expected = "SELECT USER FROM MYTABLE";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "d" - Test with a valid date.
**/
    public void Var104()
    {
        try {
            String sql = "SELECT * FROM MYTABLE WHERE DATECOL={d '1998-03-23'}";
            String expected = "SELECT * FROM MYTABLE WHERE DATECOL='1998-03-23'";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "d" - Test with no date. Should throw an exception.
**/
    public void Var105()
    {
        try {
            String sql = "SELECT * FROM MYTABLE WHERE DATECOL={d}";
            String translated = connection_.nativeSQL (sql);
            failed("Didn't throw SQLException but got "+translated);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }




/**
nativeSQL() "d" - Test with 2 dates. We just pass this onto
the server, so accept it.
**/
    public void Var106()
    {
        try {
            String sql = "SELECT * FROM MYTABLE WHERE DATECOL={d '1998-03-23' '1998-02-21'}";
            String expected = "SELECT * FROM MYTABLE WHERE DATECOL='1998-03-23' '1998-02-21'";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() "d" - Test with an invalid date.  We just pass this onto
the server, so accept it.
**/
    public void Var107()
    {
        try {
            String sql = "SELECT * FROM MYTABLE WHERE DATECOL={d '1998-02-31'}";
            String expected = "SELECT * FROM MYTABLE WHERE DATECOL='1998-02-31'";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "d" - Test with an invalid string.  We just pass this onto
the server, so accept it.
**/
    public void Var108()
    {
        try {
            String sql = "SELECT * FROM MYTABLE WHERE DATECOL={d 'Hello There'}";
            String expected = "SELECT * FROM MYTABLE WHERE DATECOL='Hello There'";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() "d" - Test with no quotes.  We just pass this onto
the server, so accept it.
**/
    public void Var109()
    {
        try {
            String sql = "SELECT * FROM MYTABLE WHERE DATECOL={d 1998-03-23}";
            String expected = "SELECT * FROM MYTABLE WHERE DATECOL=1998-03-23";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "t" - Test with a valid time.
**/
    public void Var110()
    {
        try {
            String sql = "SELECT * FROM MYTABLE WHERE TIMECOL={t '12:42:02'}";
            String expected = "SELECT * FROM MYTABLE WHERE TIMECOL='12:42:02'";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "t" - Test with no time. Should throw an exception.
**/
    public void Var111()
    {
        try {
            String sql = "SELECT * FROM MYTABLE WHERE TIMECOL={t}";
            String translated = connection_.nativeSQL (sql);
            failed("Didn't throw SQLException but got "+translated);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }




/**
nativeSQL() "t" - Test with 2 times. We just pass this onto
the server, so accept it.
**/
    public void Var112()
    {
        try {
            String sql = "SELECT * FROM MYTABLE WHERE TIMECOL={t '12:42:04' '04:04:04'}";
            String expected = "SELECT * FROM MYTABLE WHERE TIMECOL='12:42:04' '04:04:04'";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() "t" - Test with an invalid time. We just pass this onto
the server, so accept it.
**/
    public void Var113()
    {
        try {
            String sql = "SELECT * FROM MYTABLE WHERE TIMECOL={t '13:63:98'}";
            String expected = "SELECT * FROM MYTABLE WHERE TIMECOL='13:63:98'";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "t" - Test with an invalid string. We just pass this onto
the server, so accept it.
**/
    public void Var114()
    {
        try {
            String sql = "SELECT * FROM MYTABLE WHERE TIMECOL={t 'Hello There'}";
            String expected = "SELECT * FROM MYTABLE WHERE TIMECOL='Hello There'";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() "t" - Test with no quotes. We just pass this onto
the server, so accept it.
**/
    public void Var115()
    {
        try {
            String sql = "SELECT * FROM MYTABLE WHERE TIMECOL={t 12:39:07}";
            String expected = "SELECT * FROM MYTABLE WHERE TIMECOL=12:39:07";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "ts" - Test with a valid timestamp, no fraction.
**/
    public void Var116()
    {
        try {
            String sql = "SELECT * FROM MYTABLE WHERE TSCOL={ts '1998-03-23 12:42:02'}";
            String expected = "SELECT * FROM MYTABLE WHERE TSCOL='1998-03-23-12.42.02'";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "ts" - Test with a valid timestamp, just a decimal point.
**/
    public void Var117()
    {
        try {
            String sql = "SELECT * FROM MYTABLE WHERE TSCOL={ts '1998-03-23 12:42:02.'}";
            String expected = "SELECT * FROM MYTABLE WHERE TSCOL='1998-03-23-12.42.02.'";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "ts" - Test with a valid timestamp, 1 digit fraction.
**/
    public void Var118()
    {
        try {
            String sql = "SELECT * FROM MYTABLE WHERE TSCOL={ts '1998-03-23 12:42:02.5'}";
            String expected = "SELECT * FROM MYTABLE WHERE TSCOL='1998-03-23-12.42.02.5'";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "ts" - Test with a valid timestamp, 6 digit fraction.
**/
    public void Var119()
    {
        try {
            String sql = "SELECT * FROM MYTABLE WHERE TSCOL={ts '1998-03-23 12:42:02.123456'}";
            String expected = "SELECT * FROM MYTABLE WHERE TSCOL='1998-03-23-12.42.02.123456'";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "ts" - Test with a valid timestamp, 9 digit fraction.
**/
    public void Var120()
    {
        try {
            String sql = "SELECT * FROM MYTABLE WHERE TSCOL={ts '1998-03-23 12:42:02.123456789'}";
            String expected = "SELECT * FROM MYTABLE WHERE TSCOL='1998-03-23-12.42.02.123456789'";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "ts" - Test with no timestamp.  Should throw
an exception.
**/
    public void Var121()
    {
        try {
            String sql = "SELECT * FROM MYTABLE WHERE TSCOL={ts}";
            String translated = connection_.nativeSQL (sql);
            failed("Didn't throw SQLException but got "+translated);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }




/**
nativeSQL() "ts" - Test with 2 timestamps.  Should throw
an exception.
**/
    public void Var122()
    {
        try {
            String sql = "SELECT * FROM MYTABLE WHERE TIMECOL={ts '1998-03-23 12:42:02' '1998-03-23 12:42:04'}";
            String translated = connection_.nativeSQL (sql);
            failed("Didn't throw SQLException but got "+translated);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
nativeSQL() "ts" - Test with an invalid timestamp. We just pass this onto
the server, so accept it.
**/
    public void Var123()
    {
        try {
            String sql = "SELECT * FROM MYTABLE WHERE TIMECOL={ts '1998-03-32 12:42:62'}";
            String expected = "SELECT * FROM MYTABLE WHERE TIMECOL='1998-03-32-12.42.62'";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() "ts" - Test with an invalid string.  We just pass this onto
the server, so accept it.
**/
    public void Var124()
    {
        try {
            String sql = "SELECT * FROM MYTABLE WHERE TIMECOL={ts 'Hello There'}";
            String expected = "SELECT * FROM MYTABLE WHERE TIMECOL='Hello-There'";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() "ts" - Test with no quotes.  We just pass this onto
the server, so accept it.
**/
    public void Var125()
    {
        try {
            String sql = "SELECT * FROM MYTABLE WHERE TSCOL={ts 1998-03-21 12:42:22}";
            String expected = "SELECT * FROM MYTABLE WHERE TSCOL=1998-03-21-12.42.22"; 
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() "call" - Pass a string with containing 1 call escape
clause with no parameters, should return the correct SQL.
**/
    public void Var126()
    {
        try {
            String sql = "{CALL QIWS.QZDGTRGI}";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase ("CALL QIWS.QZDGTRGI"));
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() "call" - Pass a string with containing 1 call escape
clause with 3 parameters, should return the correct SQL.
**/
    public void Var127()
    {
        try {
            String sql = "{CALL QIWS.QZDGTRGI (?, ?, ?)}";
            String expected = "CALL QIWS.QZDGTRGI (?, ?, ?)";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() "?=call" - Pass a string with containing 1 call escape
clause with 3 parameters, should return the correct SQL.
**/
    public void Var128()
    {
        try {
            String sql = "{?=CALL QIWS.QZDGTRGI (?, ?, ?)}";
            String expected = "CALL QIWS.QZDGTRGI (?, ?, ?)"; // @C1C
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() "?=call" - Pass a string with containing 1 call escape
clause with 3 parameters, and spaces around the equals sign,
should return the correct SQL.
**/
    public void Var129()
    {
        try {
            String sql = "{?  =     CALL QIWS.QZDGTRGI (?, ?, ?)}";
            String expected = "CALL QIWS.QZDGTRGI (?, ?, ?)"; // @C1C
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() "oj" - Pass a string with containing an outer join
escape clause, should return the correct SQL.
**/
    public void Var130()
    {
        try {
            String sql = "SELECT COL1, COL2 FROM MYTABLE1 "
                + "{oj MYTABLE2 LEFT OUTER JOIN MYTABLE1 ON "
                + "MYTABLE1.COL1=MYTABLE2.COL2}";
            String expected = "SELECT COL1, COL2 FROM MYTABLE1 "
                + "MYTABLE2 LEFT OUTER JOIN MYTABLE1 ON "
                + "MYTABLE1.COL1=MYTABLE2.COL2";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() "oj" - Pass a string with containing an outer join
escape clause with no argument, should throw an exception.
**/
    public void Var131()
    {
        try {
            String sql = "SELECT COL1, COL2 FROM MYTABLE1 "
                + "{oj}";
            String translated = connection_.nativeSQL (sql);
            failed("Didn't throw SQLException but got "+translated);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }

 

/**
nativeSQL() - Pass a string with a comment.  
**/
    public void Var132()
    {
        try {
            String sql = "INSERT INTO MYTABLE "
                + "(PICOL) VALUES ('3.1415') -- Insert a value into the column.";
            String expected = "INSERT INTO MYTABLE "
                + "(PICOL) VALUES ('3.1415') -- Insert a value into the column.";
	    if (vrm_ < VRM_530 && nativeDriver_) {
		expected = "INSERT INTO MYTABLE (PICOL) VALUES ('3.1415')";
	    }
            //
            // Native driver will strip the comments if the length of the
            // statement if > 32 K. 
            // This is because the native driver is used by Jerry Miller's PTF
            // tools, and if we dont strip out the comments the line becomes
            // too long to execute
            // 
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }


/**
nativeSQL() - Pass a string with a comment at the beginning.  
**/
    public void Var133()
    {
        try {
            String sql      = "-- One major league comment.";
            String expected = "-- One major league comment.";
	    if (vrm_ < VRM_530 && nativeDriver_) {
		expected = ""; 
	    }
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }


/**
nativeSQL() - Pass a string with a comment at the end.  It will
be trimmed off.
**/
    public void Var134()
    {
        try {
            String sql = "INSERT INTO MYTABLE "
                + "(PICOL) VALUES ('3.1415') --";
            String expected = "INSERT INTO MYTABLE "
                + "(PICOL) VALUES ('3.1415') --";
	    if (vrm_ < VRM_530 && nativeDriver_) {
		expected = "INSERT INTO MYTABLE "
		  + "(PICOL) VALUES ('3.1415')";
	    }
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }


/**
nativeSQL() - Pass a string with a single dash.  
**/
    public void Var135()
    {
        try {
            String sql = "INSERT INTO MYTABLE "
                + "(PICOL) VALUES (-3.1415)";
            String expected = "INSERT INTO MYTABLE "
                + "(PICOL) VALUES (-3.1415)";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }


/**
nativeSQL() - Pass a string with a single dash at the end.  
**/
    public void Var136()
    {
        try {
            String sql = "INSERT INTO MYTABLE "
                + "(PICOL) VALUES (-3.1415) -";
            String expected = "INSERT INTO MYTABLE "
                + "(PICOL) VALUES (-3.1415) -";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }


/**
nativeSQL() - Pass a string with a single dash and a comment.  
**/
    public void Var137()
    {
        try {
            String sql = "INSERT INTO MYTABLE "
                + "(PICOL) VALUES (-3.1415) - This is not really a comment";
            String expected = "INSERT INTO MYTABLE "
                + "(PICOL) VALUES (-3.1415) - This is not really a comment";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() - Pass a string with containing escape clauses
in a comment.  The escape clause should not get
translated and the comment will be stripped off before the statement
is trimmed.
**/
    public void Var138()
    {
        try {
            String sql = "INSERT INTO MYTABLE "
                + "(PICOL) VALUES ('3.1415') -- Insert {fn pi()} into the column.";
            String expected = "INSERT INTO MYTABLE "
                + "(PICOL) VALUES ('3.1415') -- Insert {fn pi()} into the column.";
	    if (vrm_ < VRM_530 && nativeDriver_) {
		expected = "INSERT INTO MYTABLE "
		  + "(PICOL) VALUES ('3.1415')";
	    }
            String translated = connection_.nativeSQL (sql);

            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }


/**
nativeSQL() - Pass a string with containing escape clauses
after a single dash.  The escape clause should get translated.
pi supported at V5R1 and higher
**/
    public void Var139()
    {
        try {
            String sql = "INSERT INTO MYTABLE "
                + "(PICOL1, PICOL2) VALUES (-3.1415, {fn pi()})";
            String expected;
            if (vrm_ < VRM_510)
                expected = "INSERT INTO MYTABLE (PICOL1, PICOL2) VALUES (-3.1415, 3.1415926E00)";
            else
                expected = "INSERT INTO MYTABLE (PICOL1, PICOL2) VALUES (-3.1415, PI())";
	    if (vrm_ < VRM_530 && nativeDriver_) {
		expected = "INSERT INTO MYTABLE "
                + "(PICOL1, PICOL2) VALUES (-3.1415, 3.1415926E00)"; 
	    }

            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() - Pass an INSERT string with containing a ROWS clause
but no VALUES clause.
**/
    public void Var140()
    {
        try {
            String sql = "INSERT INTO MYTABLE "
                + "(COL) ROWS ('Yo')";
            String expected = "INSERT INTO MYTABLE "
                + "(COL) ROWS ('Yo')";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() - Pass an INSERT string with containing a ROWS VALUES clause.
**/
    public void Var141()
    {
        try {
            String sql = "INSERT INTO MYTABLE "
                + "(COL) ROWS VALUES ('Yo')";
            String expected = "INSERT INTO MYTABLE "
                + "(COL) ROWS VALUES ('Yo')";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() - Pass a INSERT string with a subselect.
**/
    public void Var142()
    {
        try {
            String sql = "INSERT INTO MYTABLE "
                + "(COL) ROWS ('Yo') SELECT * FROM QCUSTCDT";
            String expected = "INSERT INTO MYTABLE "
                + "(COL) ROWS ('Yo') SELECT * FROM QCUSTCDT";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }



/**
nativeSQL() - Pass a DECLARE string.
**/
    public void Var143()
    {
        try {
            String sql = "DECLARE PROCEDURE MYPOPS";
            String expected = "DECLARE PROCEDURE MYPOPS";
            String translated = connection_.nativeSQL (sql);
            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() - Ensure that we can correctly handle a single line comment 
when the comment line ends before the entire SQL string ends.
**/
    public void Var144()
    {
        try {
            String sql      = "select * from cujosql.comment -- This is a comment to the end of the line.\nwhere col1 = {ts '1998-03-23 12:42:02.123450'}";
            String expected = "select * from cujosql.comment -- This is a comment to the end of the line.\nwhere col1 = '1998-03-23-12.42.02.123450'";
	    if (vrm_ < VRM_530 && nativeDriver_) {
		expected = "select * from cujosql.comment  \nwhere col1 = '1998-03-23-12.42.02.123450'";
	    }
            String translated = connection_.nativeSQL (sql);

            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }




/**
nativeSQL() - Ensure that we can correctly handle a multi-line comment
when the comment contains some invalid JDBC escape syntax.
**/
    public void Var145()
    {
        try {
            String sql = "select * from cujosql.comment \n" + 
                " /* TODO:  We remove the special {ts} syntax here.  */ \n " + 
                " where col1 = {ts '1998-03-23 12:42:02.123450'}";
            String expected = "select * from cujosql.comment \n" + 
                " /* TODO:  We remove the special {ts} syntax here.  */ \n " + 
                " where col1 = '1998-03-23-12.42.02.123450'";
	    if (vrm_ < VRM_530 && nativeDriver_) {
		expected = "select * from cujosql.comment \n" + 
		  "   \n " + 
		  " where col1 = '1998-03-23-12.42.02.123450'";
	    }
            String translated = connection_.nativeSQL (sql);

            assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }

/**
nativeSQL() - Ensure that we can correctly handle a multi-line comment
when the comment contains some valid JDBC escape syntax.
**/
    public void Var146()
    {
        try {
            String sql = "select * from cujosql.comment \n" +
                " /* TODO:  Do not convert escape syntax here. {fn pi()} */" +
                "\n where col1 = {ts '1998-03-23 12:42:02.123450'}";
            String expected = "select * from cujosql.comment \n" +
                " /* TODO:  Do not convert escape syntax here. {fn pi()} */" +
                "\n where col1 = '1998-03-23-12.42.02.123450'";
	    if (vrm_ < VRM_530 && nativeDriver_) {
		expected="select * from cujosql.comment \n" +
                "  " +
                "\n where col1 = '1998-03-23-12.42.02.123450'";
	    }
            String translated = connection_.nativeSQL (sql);
	    assertCondition (translated.equals (expected)); 
        }
        catch (Exception e) {
            failed(e , "Unexpected Exception");
        }
    }


/**
nativeSQL() - Pass a string with a comment.  The comment will be > 32 K , and will thus be stripped 
**/
    public void Var147()
    {
	if (v5r2nativeFunctions_) {
	    notApplicable(); 
	}  else { 
	    try {
            if ((isToolboxDriver()) && ( vrm_ > VRM_530))
            {
                notApplicable();  //new limit is 2m in toolbox v5r4
                return;
            }
		String sql = "INSERT INTO MYTABLE "
		  + "(PICOL) VALUES ('3.1415') -- Insert a value into the column.";
		String moreComment = "THIS IS ADDITIONAL COMMENT THAT IS 060 CHARACTERS LONG......"; 
		for (int i = 0; i < 33000; i+= moreComment.length()) {
		    sql += moreComment; 
		} 
		String expected = "INSERT INTO MYTABLE "
		  + "(PICOL) VALUES ('3.1415')";
	    //
	    // Native driver will strip the comments if the length of the
	    // statement if > 32 K. 
	    // This is because the native driver is used by Jerry Miller's PTF
	    // tools, and if we dont strip out the comments the line becomes
	    // too long to execute
	    // 
		String translated = connection_.nativeSQL (sql);
		assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
	    }
	    catch (Exception e) {
		failed(e , "Unexpected Exception");
	    }
	}
    }

/**
nativeSQL() - Ensure that we can correctly handle a single line comment 
when the comment line ends before the entire SQL string ends.
**/
    public void Var148()
    {
	if (v5r2nativeFunctions_) {
	    notApplicable(); 
	}  else { 
        if ((isToolboxDriver()) && ( vrm_ > VRM_530))
        {
            notApplicable();  //new limit is 2m in toolbox v5r4
            return;
        }
	    try {
		String sql      = "select * from eberhard.comment -- This is a comment to the end of the line. ";
		String moreComment = "THIS IS ADDITIONAL COMMENT THAT IS 060 CHARACTERS LONG......"; 
		for (int i = 0; i < 33000; i+= moreComment.length()) {
		    sql += moreComment; 
		} 
		sql += "\nwhere col1 = {ts '1998-03-23 12:42:02.123450'}";
		String expected = "select * from eberhard.comment where col1 = '1998-03-23-12.42.02.123450'";
		if (vrm_ < VRM_530 && nativeDriver_) {
		}
		String translated = connection_.nativeSQL (sql);

		assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
	    }
	    catch (Exception e) {
		failed(e , "Unexpected Exception");
	    }
	}
    }

/**
nativeSQL() - Ensure that we can correctly handle a mulitple single line comment 
when the comment line ends before the entire SQL string ends.
**/
    public void Var149()
    {
	if (v5r2nativeFunctions_) {
	    notApplicable(); 
	}  else { 

        if ((isToolboxDriver()) && ( vrm_ > VRM_530))
        {
            notApplicable();  //new limit is 2m in toolbox v5r4
            return;
        }
	    try {
		String sql      = "select * from eberhard.comment -- This is a comment to the end of the line. ";
		String moreComment = "THIS IS ADDITIONAL COMMENT THAT IS 060 CHARACTERS LONG......"; 
		for (int i = 0; i < 17000; i+= moreComment.length()) {
		    sql += moreComment; 
		} 
		sql += "\nwhere col1 = -- THIS IS EVEN MORE COMMENT.. ";
		for (int i = 0; i < 17000; i+= moreComment.length()) {
		    sql += moreComment; 
		} 
		sql += "\n{ts '1998-03-23 12:42:02.123450'}";
		String expected = "select * from eberhard.comment where col1 = '1998-03-23-12.42.02.123450'";
		if (vrm_ < VRM_530 && nativeDriver_) {
		}
		String translated = connection_.nativeSQL (sql);

		assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
	    }
	    catch (Exception e) {
		failed(e , "Unexpected Exception");
	    }
	}
    }





/**
nativeSQL() - Ensure that we can correctly handle a bracketed 
when the comment contains some invalid JDBC escape syntax.
**/
    public void Var150()
    {
	if (v5r2nativeFunctions_) {
	    notApplicable(); 
	}  else { 
          if ((isToolboxDriver()) && ( vrm_ > VRM_530))
          {
              notApplicable();  //new limit is 2m in toolbox v5r4
              return;
          }
	    try {
		String sql = "select * from eberhard.comment \n" + 
		  " /* TODO:  We remove the special {ts} syntax here. ";
		String moreComment = "THIS IS ADDITIONAL COMMENT THAT IS 060 CHARACTERS LONG......"; 
		for (int i = 0; i < 33000; i+= moreComment.length()) {
		    sql += moreComment; 
		} 
		sql +=  "*/ \n " + 
		  " where col1 = {ts '1998-03-23 12:42:02.123450'}";

		String expected = "select * from eberhard.comment \n  \n  where col1 = '1998-03-23-12.42.02.123450'";
		if (vrm_ < VRM_530 && nativeDriver_) {
		}
		String translated = connection_.nativeSQL (sql);

		assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
	    }
	    catch (Exception e) {
		failed(e , "Unexpected Exception");
	    }
	}
    }


/**
nativeSQL() - Ensure that we can correctly handle a multiline bracketed 
when the comment contains some invalid JDBC escape syntax.
**/
    public void Var151()
    {
	if (v5r2nativeFunctions_) {
	    notApplicable(); 
	}  else { 
          if ((isToolboxDriver()) && ( vrm_ > VRM_530))
          {
              notApplicable();  //new limit is 2m in toolbox v5r4
              return;
          }
	    try {
		String sql = "select * from eberhard.comment \n" + 
		  " /* TODO:  We remove the special {ts} syntax here. ";
		String moreComment = "THIS IS ADDITIONAL COMMENT THAT IS 060 CHARACTERS LONG.....\n"; 
		for (int i = 0; i < 33000; i+= moreComment.length()) {
		    sql += moreComment; 
		} 
		sql +=  "*/ \n " + 
		  " where col1 = {ts '1998-03-23 12:42:02.123450'}";

		String expected = "select * from eberhard.comment \n  \n  where col1 = '1998-03-23-12.42.02.123450'";
		if (vrm_ < VRM_530 && nativeDriver_) {
		}
		String translated = connection_.nativeSQL (sql);

		assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
	    }
	    catch (Exception e) {
		failed(e , "Unexpected Exception");
	    }
	}
    }


/**
nativeSQL() - Ensure that we can correctly handle a nested bracketed 
when the comment contains some invalid JDBC escape syntax.
**/
    public void Var152()
    {
	if (v5r2nativeFunctions_) {
	    notApplicable(); 
	}  else { 
          if ((isToolboxDriver()) && ( vrm_ > VRM_530))
          {
              notApplicable();  //new limit is 2m in toolbox v5r4
              return;
          }

	    try {
		String sql = "select * from eberhard.comment \n" + 
		  " /* TODO:  We remove the special {ts} syntax here. ";
		String moreComment = " /* THIS IS ADDITIONAL COMMENT THAT IS 060 CHARACTERS LONG */ "; 
		for (int i = 0; i < 33000; i+= moreComment.length()) {
		    sql += moreComment; 
		} 
		sql +=  "*/ \n " + 
		  " where col1 = {ts '1998-03-23 12:42:02.123450'}";

		String expected = "select * from eberhard.comment \n  \n  where col1 = '1998-03-23-12.42.02.123450'";
		if (vrm_ < VRM_530 && nativeDriver_) {
		}
		String translated = connection_.nativeSQL (sql);

		assertCondition (translated.equalsIgnoreCase (expected), "actual("+translated+")!=expected("+expected+")");
	    }
	    catch (Exception e) {
		failed(e , "Unexpected Exception");
	    }
	}
    }





}



