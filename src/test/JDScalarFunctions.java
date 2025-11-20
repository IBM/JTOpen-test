///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDScalarFunctions.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

 ////////////////////////////////////////////////////////////////////////
 //
 // File Name:    JDScalarFunctions.java
 //
 // Classes:      JDScalarFunctions
 //
 ////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;


/**
Testcase JDScalarFunctions.  This tests the following scalar
functions:
Numeric:
abs,acos,asin,atan,atan2,ceiling,cos,cot,degrees,exp,floor,log,log10,mod,pi,power,radians,rand,round,sin,sign,sqrt,tan,truncate
String:
concat,difference,insert,lcase,left,length,locate,ltrim,right,rtrim,soundex,space,substring,ucase
System:
database,ifnull,user
Time/Date:
curdate,curtime,dayofmonth,dayofweek,dayofyear,hour,minute,month,now,quarter,second,timestampdiff,week,year

Ability to handle nested scalar functions is also tested.
**/
public class JDScalarFunctions extends JDTestcase {

    // Private data.
    private Statement statement_;
    private ResultSet rs_;

    private Connection decsepconn1_;
    private Statement decsepstmnt1_;
    private Connection decsepconn2_;
    private Statement decsepstmnt2_;

    /**
    Constructor.
    **/
    public JDScalarFunctions(AS400 systemObject,
                             Hashtable <String,Vector<String>>namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             
                             String password)
    {
        super(systemObject, "JDScalarFunctions",
              namesAndVars, runMode, fileOutputStream,
              password);
    }



    /**
    Performs setup needed before running variations.

    @exception Exception If an exception occurs.
    **/
    protected void setup() throws Exception {
        connection_ = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
        statement_ = connection_.createStatement();

        decsepconn1_ = testDriver_.getConnection(baseURL_ + ";decimal separator=,", userId_, encryptedPassword_);
        decsepstmnt1_ = decsepconn1_.createStatement();

        decsepconn2_ = testDriver_.getConnection(baseURL_ + ";decimal separator=.", userId_, encryptedPassword_);
        decsepstmnt2_ = decsepconn2_.createStatement();

        statement_.executeUpdate("CREATE TABLE " + JDStatementTest.COLLECTION + ".SCLRTST (PIFIELD DOUBLE)");
        statement_.executeUpdate("INSERT INTO " + JDStatementTest.COLLECTION + ".SCLRTST (PIFIELD) VALUES(PI())");
    }



    /**
    Performs cleanup needed after running variations.

    @exception Exception If an exception occurs.
    **/
    protected void cleanup() throws Exception {
        statement_.executeUpdate("DROP TABLE " + JDStatementTest.COLLECTION + ".SCLRTST");
        statement_.close();
        connection_.close();
        connection_ = null; 

        decsepconn1_.close();
        decsepstmnt1_.close();
        decsepconn2_.close();
        decsepstmnt2_.close();
    }


    /**
    test simple scalar function
    concat
    **/
    public void Var001()
    {
        try {
            rs_ = statement_.executeQuery("SELECT {fn concat(CITY,  STATE)} FROM QIWS.QCUSTCDT");
            rs_.close();
            assertCondition(true);
        } catch (SQLException e) {
            failed(e, "Unexpected Exception");
        }
    }

    /**
    test single nested scalar function
    concat
    **/
    public void Var002()
    {
        try {
            rs_ = statement_.executeQuery("SELECT {fn concat(CITY,{fn concat(STATE,STREET)})} FROM QIWS.QCUSTCDT");
            rs_.close();
            assertCondition(true);
        } catch (SQLException e) {
            failed(e, "Unexpected Exception");
        }
    }

    /**
    test single nested scalar function
    concat
    **/
    public void Var003()
    {
        try {
            rs_ = statement_.executeQuery("SELECT {fn concat({fn concat(CITY, STATE)},STREET)} FROM QIWS.QCUSTCDT");
            rs_.close();
            assertCondition(true);
        } catch (SQLException e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    test two single nested scalar functions
    concat
    **/
    public void Var004()
    {
        try {
            rs_ = statement_.executeQuery("SELECT {fn concat({fn concat(CITY, STATE)},{fn concat(STREET, STATE)})} FROM QIWS.QCUSTCDT");
            rs_.close();
            assertCondition(true);
        } catch (SQLException e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    test triple nested scalar function
    abs,log10,sin,cos
    **/
    public void Var005()
    {
        try {
            rs_ = statement_.executeQuery("SELECT {fn abs({fn log10({fn sin({fn cos(ZIPCOD)})})})} FROM QIWS.QCUSTCDT");
            rs_.close();
            assertCondition(true);
        } catch (SQLException e) {
            failed(e, "Unexpected Exception");
        }
    }

    /**
    test single nested scalar function
    mod,atan
    **/
    public void Var006()
    {
        try {
            rs_ = statement_.executeQuery("SELECT {fn mod(ZIPCOD, {fn atan(CUSNUM)})} FROM QIWS.QCUSTCDT");
            rs_.close();
            assertCondition(true);
        } catch (SQLException e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    test scalar functions we do not map
    power
    **/
    public void Var007()
    {
        try {
            rs_ = statement_.executeQuery("SELECT {fn power(CDTLMT,CHGCOD)} FROM QIWS.QCUSTCDT");
            rs_.close();
            assertCondition(true);
        } catch (SQLException e) {
            failed(e, "Unexpected Exception");
        }
    }

    /**
    test simple scalar function we map
    pi
    **/
    public void Var008()
    {
        try {
            rs_ = statement_.executeQuery("SELECT CITY FROM QIWS.QCUSTCDT WHERE BALDUE > {fn pi()}");
            rs_.close();
            assertCondition(true);
        } catch (SQLException e) {
            failed(e, "Unexpected Exception");
        }
    }

    /**
    test simple scalar function we map
    log
    **/
    public void Var009()
    {
        try {
            rs_ = statement_.executeQuery("SELECT CITY FROM QIWS.QCUSTCDT WHERE CHGCOD > {fn log(1)}");
            rs_.close();
            assertCondition(true);
        } catch (SQLException e) {
            failed(e, "Unexpected Exception");
        }
    }

    /**
    test simple scalar function we map
    insert
    **/
    public void Var010()
    {
        try {
            rs_ = statement_.executeQuery("SELECT {fn insert(STATE, 2, 0, CITY)} FROM QIWS.QCUSTCDT");
            rs_.close();
            assertCondition(true);
        } catch (SQLException e) {
            failed(e, "Unexpected Exception");
        }
    }

    /**
    test simple scalar function we map
    right
    **/
    public void Var011()
    {
        try {
            rs_ = statement_.executeQuery("SELECT {fn right(CITY, 3)} FROM QIWS.QCUSTCDT");
            rs_.close();
            assertCondition(true);
        } catch (SQLException e) {
            failed(e, "Unexpected Exception");
        }
    }

    /**
    test simple scalar function we map
    length
    **/
    public void Var012()
    {
        try {
            rs_ = statement_.executeQuery("SELECT {fn length(STATE)} FROM QIWS.QCUSTCDT");
            rs_.close();
            assertCondition(true);
        } catch (SQLException e) {
            failed(e, "Unexpected Exception");
        }
    }

    /**
    test nested mapped scalar function
    length, right
    **/
    public void Var013()
    {
        try {
            rs_ = statement_.executeQuery("SELECT {fn length({fn right(CITY,3)})} FROM QIWS.QCUSTCDT");
            rs_.close();
            assertCondition(true);
        } catch(SQLException e) {
            failed(e, "Unexpected Exception");
        }
    }

    /**
    test nested scalar functions
    right, right
    **/
    public void Var014()
    {
        try {
            rs_ = statement_.executeQuery("SELECT {fn right({fn right(CITY,4)}, 3)} FROM QIWS.QCUSTCDT");
            rs_.close();
            assertCondition(true);
        } catch(SQLException e) {
            failed(e, "Unexpected Exception");
        }
    }

    /**
    test simple scalar function we do not map
    count
    **/
    public void Var015()
    {
        try {
            rs_ = statement_.executeQuery("SELECT {fn cos(ZIPCOD)} FROM QIWS.QCUSTCDT");
            rs_.close();
            assertCondition(true);
        } catch(SQLException e) {
            failed(e, "Unexpected Exception");
        }
    }

    /**
    test nested scalar functions we do not map
    power, ceiling, sin
    **/
    public void Var016()
    {
        try {
            rs_ = statement_.executeQuery("SELECT {fn power(ZIPCOD,{fn ceiling({fn sin(ZIPCOD)})})} FROM QIWS.QCUSTCDT");
            rs_.close();
            assertCondition(true);
        } catch(SQLException e) {
            failed(e, "Unexpected Exception");
        }
    }

    /**
    test decimal separator "," in PI
    **/
    public void Var017()
    {
        try {
            rs_ = decsepstmnt1_.executeQuery("SELECT * FROM " + JDStatementTest.COLLECTION + ".SCLRTST WHERE PIFIELD = {fn pi()}");
            rs_.next();
            rs_.close();
            assertCondition(true);
        } catch(SQLException e) {
            failed(e, "Unexpected Exception");
        }
    }

    /**
    test decimal separator "." in PI
    **/
    public void Var018()
    {
        try {
            rs_ = decsepstmnt2_.executeQuery("SELECT * FROM " + JDStatementTest.COLLECTION + ".SCLRTST WHERE PIFIELD = {fn pi()}");
            rs_.next();
            rs_.close();
            assertCondition(true);
        } catch(SQLException e) {
            failed(e, "Unexpected Exception");
        }
    }




    /**
    test simple scalar function we map
    length -- test with a vargraphic 
    **/
    public void Var019()
    {
        try {
            rs_ = statement_.executeQuery("SELECT {fn LENGTH( CAST( GX'BB88' AS VARGRAPHIC( 10000 ) ) )} AS GLEN  FROM QIWS.QCUSTCDT");
	    rs_.next();
	    int answer = rs_.getInt(1); 
            rs_.close();
            assertCondition(answer == 1, "answer = "+answer+" sb 1  added by native driver 8/03/05");
        } catch (SQLException e) {
            failed(e, "Unexpected Exception == added by native driver 8/03/05");
        }
    }

    /**
    test simple scalar function we map
    length -- test with a graphic 
    **/
    public void Var020()
    {
        try {
            rs_ = statement_.executeQuery("SELECT {fn LENGTH( CAST( GX'BB88' AS GRAPHIC( 10000 ) ) )} AS GLEN  FROM QIWS.QCUSTCDT");
	    rs_.next();
	    int answer = rs_.getInt(1); 
            rs_.close();
            assertCondition(answer == 1, "answer = "+answer+" sb 1  added by native driver 8/03/05");
        } catch (SQLException e) {
            failed(e, "Unexpected Exception == added by native driver 8/03/05");
        }
    }

    /**
    test simple scalar function we map
    length -- test with a vargraphic ccsid 13488 
    **/
    public void Var021()
    {
        try {
            rs_ = statement_.executeQuery("SELECT {fn LENGTH( CAST( UX'00a0' AS VARGRAPHIC( 10000 ) ccsid 13488 ) )} AS GLEN  FROM QIWS.QCUSTCDT");
	    rs_.next();
	    int answer = rs_.getInt(1); 
            rs_.close();
            assertCondition(answer == 1, "answer = "+answer+" sb 1  added by native driver 8/03/05");
        } catch (SQLException e) {
            failed(e, "Unexpected Exception == added by native driver 8/03/05");
        }
    }

    /**
    test simple scalar function we map
    length -- test with a graphic ccsid 13488
    **/
    public void Var022()
    {
        try {
            rs_ = statement_.executeQuery("SELECT {fn LENGTH( CAST( UX'00A0' AS GRAPHIC( 10000 ) ccsid 13488 ) )} AS GLEN  FROM QIWS.QCUSTCDT");
	    rs_.next();
	    int answer = rs_.getInt(1); 
            rs_.close();
            assertCondition(answer == 1, "answer = "+answer+" sb 1  added by native driver 8/03/05");
        } catch (SQLException e) {
            failed(e, "Unexpected Exception == added by native driver 8/03/05");
        }
    }

}


