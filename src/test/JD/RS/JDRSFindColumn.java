///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSFindColumn.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.RS;

import com.ibm.as400.access.AS400;

import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDRSFindColumn.  This tests the following
methods of the JDBC ResultSet class:

<ul>
<li>findColumn()
</ul>
**/
public class JDRSFindColumn
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSFindColumn";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



    // Private data.
    private DatabaseMetaData    dmd_;
    private Statement           statement_;



/**
Constructor.
**/
    public JDRSFindColumn (AS400 systemObject,
                           Hashtable namesAndVars,
                           int runMode,
                           FileOutputStream fileOutputStream,
                           
                           String password)
    {
        super (systemObject, "JDRSFindColumn",
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
	if (connection_ != null) connection_.close();

        connection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_,"JDRSFindColumn");
        dmd_ = connection_.getMetaData ();
        statement_ = connection_.createStatement ();
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
        statement_.close ();
        connection_.close ();
    }



/**
findColumn() - Should thrown an exception on a closed result set.
**/
    public void Var001 ()
    {
        try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            rs.close ();
            rs.findColumn ("LSTNAM");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
findColumn() - Should thrown an exception when null is passed.
**/
    public void Var002 ()
    {
        try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            rs.findColumn (null);
            failed ("Didn't throw SQLException for null column");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
findColumn() - Should thrown an exception when an empty
string is passed.
**/
    public void Var003 ()
    {
        try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            rs.findColumn ("");
            failed ("Didn't throw SQLException for empty string");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
findColumn() - Should thrown an exception when an non-existent
column name is passed.
**/
    public void Var004 ()
    {
        try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            rs.findColumn ("JIM");
            failed ("Didn't throw SQLException for not existing column");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
findColumn() - Should return the correct column when it is
the first.
**/
    public void Var005()
    {
        try {
            ResultSet rs = statement_.executeQuery ("SELECT CUSNUM,LSTNAM,INIT FROM QIWS.QCUSTCDT");
            int i = rs.findColumn ("CUSNUM");
            rs.close ();
            assertCondition (i == 1);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
findColumn() - Should return the correct column when it is
the last.
**/
    public void Var006()
    {
        try {
            ResultSet rs = statement_.executeQuery ("SELECT CUSNUM,LSTNAM,INIT FROM QIWS.QCUSTCDT");
            int i = rs.findColumn ("INIT");
            rs.close ();
            assertCondition (i == 3);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
findColumn() - Should return the correct column when it is
neither the first nor last on a "simple" result set.
**/
    public void Var007()
    {
        try {
            ResultSet rs = statement_.executeQuery ("SELECT CUSNUM,LSTNAM,INIT FROM QIWS.QCUSTCDT");
            int i = rs.findColumn ("LSTNAM");
            rs.close ();
            assertCondition (i == 2);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
findColumn() - Should return the correct column when it is
in a different case.
**/
    public void Var008()
    {
        try {
            ResultSet rs = statement_.executeQuery ("SELECT CUSNUM,LSTNAM,INIT FROM QIWS.QCUSTCDT");
            int i = rs.findColumn ("LsTnAm");
            rs.close ();
            assertCondition (i == 2, "found column = "+i+" sb 2 ");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
findColumn() - Should thrown an exception when null is passed
on a "simple" result set.
**/
    public void Var009 ()
    {
        try {
            ResultSet rs = dmd_.getTableTypes ();
            rs.findColumn (null);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
findColumn() - Should thrown an exception when an empty
string is passed on a "simple" result set.
**/
    public void Var010 ()
    {
        try {
            ResultSet rs = dmd_.getTableTypes ();
            rs.findColumn ("");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
findColumn() - Should thrown an exception when an non-existent
column name is passed on a "simple" result set.
**/
    public void Var011 ()
    {
        try {
            ResultSet rs = dmd_.getTableTypes ();
            rs.findColumn ("JIM");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
findColumn() - Should return the correct column when it is
the only on a "simple" result set.
**/
    public void Var012()
    {
        try {
            ResultSet rs = dmd_.getTableTypes ();
            int i = rs.findColumn ("TABLE_TYPE");
            rs.close ();
            assertCondition (i == 1);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
findColumn() - Should return the correct column when it is
in a different case on a "simple" result set.
**/
    public void Var013()
    {
        try {
            ResultSet rs = dmd_.getTableTypes ();
            int i = rs.findColumn ("tAbLe_TyPe");
            rs.close ();
            assertCondition (i == 1);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
findColumn() - Should thrown an exception when null is passed
on a "mapped" result set.
**/
    public void Var014 ()
    {
        try {
            ResultSet rs = dmd_.getColumns (null, "QIWS",
                                            "QCUSTCDT", "%");
            rs.findColumn (null);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
findColumn() - Should thrown an exception when an empty
string is passed on a "mapped" result set.
**/
    public void Var015 ()
    {
        try {
            ResultSet rs = dmd_.getColumns (null, "QIWS",
                                            "QCUSTCDT", "%");
            rs.findColumn ("");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
findColumn() - Should thrown an exception when an non-existent
column name is passed on a "mapped" result set.
**/
    public void Var016 ()
    {
        try {
            ResultSet rs = dmd_.getColumns (null, "QIWS",
                                            "QCUSTCDT", "%");
            rs.findColumn ("JIM");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
findColumn() - Should return the correct column when it is
the first on a "mapped" result set.
**/
    public void Var017 ()
    {
        try {
            ResultSet rs = dmd_.getColumns (null, "QIWS",
                                            "QCUSTCDT", "%");
            int i = rs.findColumn ("TABLE_CAT");
            rs.close ();
            assertCondition (i == 1);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
findColumn() - Should return the correct column when it is
the last on a "mapped" result set.
**/
    public void Var018 ()
    {
        try {
            ResultSet rs = dmd_.getColumns (null, "QIWS",
                                            "QCUSTCDT", "%");
            int i = rs.findColumn ("IS_NULLABLE");
            rs.close ();
            assertCondition (i == 18);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
findColumn() - Should return the correct column when it is
neither the first nor last on a "mapped" result set.
**/
    public void Var019()
    {
        try {
            ResultSet rs = dmd_.getColumns (null, "QIWS",
                                            "QCUSTCDT", "%");
            int i = rs.findColumn ("BUFFER_LENGTH");
            rs.close ();
            assertCondition (i == 8);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
findColumn() - Should return the correct column when it is
in a different case on a "mapped" result set.
**/
    public void Var020()
    {
        try {
            ResultSet rs = dmd_.getColumns (null, "QIWS",
                                            "QCUSTCDT", "%");
            int i = rs.findColumn ("rEMaRKs");
            rs.close ();
            assertCondition (i == 12);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

/**
findColumn() - Should return the correct column when it is
the first and lowercase 
**/
    public void Var021()
    {
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT CUSNUM as \"col1\",LSTNAM as \"col2\", INIT as \"col3\" FROM QIWS.QCUSTCDT");
                int i = rs.findColumn ("col1");
                rs.close ();
                assertCondition (i == 1);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        } else {
            notApplicable("Need to add support for case insensitive column name search");
        }
    }



/**
findColumn() - Should return the correct column when it is
the last and lower case.
**/
    public void Var022()
    {
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT CUSNUM as \"col1\",LSTNAM as \"col2\", INIT as \"col3\" FROM QIWS.QCUSTCDT");
                int i = rs.findColumn ("col3");
                rs.close ();
                assertCondition (i == 3);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        } else {
            notApplicable("Need to add support for case insensitive column name search");
        }
    }



/**
findColumn() - Should return the correct column when it is
neither the first nor last on a "simple" result set 
**/
    public void Var023()
    {
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT CUSNUM as \"col1\",LSTNAM as \"col2\", INIT as \"col3\" FROM QIWS.QCUSTCDT");
                int i = rs.findColumn ("col2");
                rs.close ();
                assertCondition (i == 2);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        } else {
            notApplicable("Need to add support for case insensitive column name search");
        }
    }


/**
findColumn() - Should return the correct column when it is
the first and lowercase  and the second column queried 
**/
    public void Var024()
    {
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT CUSNUM as \"col1\",LSTNAM as \"col2\", INIT as \"col3\" FROM QIWS.QCUSTCDT");
                int j = rs.findColumn ("col3");
                int i = rs.findColumn ("col1");
                rs.close ();
                assertCondition (i == 1 && j == 3 );
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        } else {
            notApplicable("Need to add support for case insensitive column name search");
        }
    }



/**
findColumn() - Should return the correct column when it is
the last and lower case and the second column queried.
**/
    public void Var025()
    {
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT CUSNUM as \"col1\",LSTNAM as \"col2\", INIT as \"col3\" FROM QIWS.QCUSTCDT");
                int j = rs.findColumn ("col2");
                int i = rs.findColumn ("col3");
                rs.close ();
                assertCondition (j == 2 && i == 3);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        } else {
            notApplicable("Need to add support for case insensitive column name search");
        }
    }



/**
findColumn() - Should return the correct column when it is
neither the first nor last on a "simple" result set and the second column queried
**/
    public void Var026()
    {
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT CUSNUM as \"col1\",LSTNAM as \"col2\", INIT as \"col3\" FROM QIWS.QCUSTCDT");
                int j = rs.findColumn ("col1");
                int i = rs.findColumn ("col2");
                rs.close ();
                assertCondition (j == 1 && i == 2);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        } else {
            notApplicable("Need to add support for case insensitive column name search");
        }
    }



/**
findColumn() - Should return the first column on a case insensitive search
**/
    public void Var027()
    {
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT CUSNUM as \"One\",LSTNAM as \"ONe\", INIT as \"ONE\" FROM QIWS.QCUSTCDT");
                int j = rs.findColumn ("one");
                int i = rs.findColumn ("one");
                rs.close ();
                assertCondition (j == 1 && i == 1);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        } else {
            notApplicable("Need to add support for case insensitive column name search");
        }
    }


/**
findColumn() - Find the right columns using a case sensitive search -- the native driver does different stuff on the first time lookup
               so variations 28-30 are almost the same 
**/
    public void Var028()
    {
        try {
            ResultSet rs = statement_.executeQuery ("SELECT CUSNUM as \"One\",LSTNAM as \"ONe\", INIT as \"ONE\" FROM QIWS.QCUSTCDT");
            int i = rs.findColumn ("\"One\"");
            int j = rs.findColumn ("\"ONe\"");
            int k = rs.findColumn ("\"ONE\"");
            rs.close ();
            assertCondition (i == 1 && j == 2 && k == 3 );
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

/**
findColumn() - Find the right columns using a case sensitive search -- the native driver does different stuff on the first time lookup
               so variations 28-30 are almost the same 
**/
    public void Var029()
    {
        try {
            ResultSet rs = statement_.executeQuery ("SELECT CUSNUM as \"One\",LSTNAM as \"ONe\", INIT as \"ONE\" FROM QIWS.QCUSTCDT");
            int j = rs.findColumn ("\"ONe\"");
            int i = rs.findColumn ("\"One\"");
            int k = rs.findColumn ("\"ONE\"");
            rs.close ();
            assertCondition (i == 1 && j == 2 && k == 3 );
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
findColumn() - Find the right columns using a case sensitive search -- the native driver does different stuff on the first time lookup
               so variations 28-30 are almost the same 
**/
    public void Var030()
    {
        try {
            ResultSet rs = statement_.executeQuery ("SELECT CUSNUM as \"One\",LSTNAM as \"ONe\", INIT as \"ONE\" FROM QIWS.QCUSTCDT");
            int k = rs.findColumn ("\"ONE\"");
            int i = rs.findColumn ("\"One\"");
            int j = rs.findColumn ("\"ONe\"");
            rs.close ();
            assertCondition (i == 1 && j == 2 && k == 3 );
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

/**
 * findColumn -- Don't find a column using a case sensitive search
 */
    public void Var031()
    {
	ResultSet rs = null; 
        try {
            rs = statement_.executeQuery ("SELECT CUSNUM as \"One\",LSTNAM as \"ONe\", INIT as \"ONE\" FROM QIWS.QCUSTCDT");
            int i = rs.findColumn ("\"one\"");
            failed ("Didn't throw SQLException but got "+i);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
	try {
	    if (rs != null) rs.close(); 
	} catch (Exception e) {} 

    }

/**
 * findColumn -- Don't find a column using a case sensitive search (on second try) 
 */
    public void Var032()
    {
	ResultSet rs = null; 
	try {
	    rs = statement_.executeQuery ("SELECT CUSNUM as \"One\",LSTNAM as \"ONe\", INIT as \"ONE\" FROM QIWS.QCUSTCDT");
            int j = rs.findColumn ("\"ONe\"");
	    int i = rs.findColumn ("\"one\"");
	    failed ("Didn't throw SQLException but got "+i+" and "+j);
	}
	catch (Exception e) {
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
	try { 
	    if (rs != null) rs.close();
	} catch (Exception e) {} 

    }





}



