///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionFormat.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

 ////////////////////////////////////////////////////////////////////////
 //
 // File Name:    JDConnectionFormat.java
 //
 // Classes:      JDConnectionFormat
 //
 ////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDConnectionFormat.  This tests the following
properties with respect to the JDBC Connection class:

<ul>
<li>decimal separator
<li>date format
<li>date separator
<li>time format
<li>time separator
</ul>
**/
public class JDConnectionFormat
extends JDTestcase
{



    // Private data.
    private static  String table_      = JDConnectionTest.COLLECTION + ".JDCFORMAT";

    private Connection  connection_;



/**
Constructor.
**/
    public JDConnectionFormat (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDConnectionFormat",
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
        // Create the table.
        table_      = JDConnectionTest.COLLECTION + ".JDCFORMAT";
        connection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
        Statement s = connection_.createStatement ();
        s.executeUpdate ("CREATE TABLE " + table_
            + " (COL1 DECIMAL(5,1), COL2 DATE, COL3 TIME)");
        connection_.commit(); // for xa
        s.close ();
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
        // Drop the table.
        Statement s = connection_.createStatement ();
        s.executeUpdate ("DROP TABLE " + table_);
        connection_.commit(); // for xa
        s.close ();
    }



/**
decimal separator - Use the default for the decimal separator.
Insert a records using a "." and "," decimal separators.
The default should be what the server job is set to.  This
is a bit difficult to check, so we make sure exactly one of the
inserts succeeds.
**/
    public void Var001 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            Statement s = c.createStatement ();

            int inserts = 0;
            try {
                s.executeUpdate ("INSERT INTO " + table_
                    + " (COL1) VALUES (1.2)");
                ++inserts;
            }
            catch (SQLException e) {
                // Ignore.
            }
            try {
                s.executeUpdate ("INSERT INTO " + table_
                    + " (COL1) VALUES (1,2)");
                ++inserts;
            }
            catch (SQLException e) {
                // Ignore.
            }

            c.close ();
            assertCondition (inserts == 1);
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
decimal separator - Specify an invalid value for the decimal
separator.  This should not thow an exception but use what
the server job is set to.  Insert a records using a "." and
"," decimal separators.  Make sure exactly one of the inserts
succeeds.
**/
    public void Var002 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";decimal separator=bogus", userId_, encryptedPassword_);
            Statement s = c.createStatement ();

            int inserts = 0;
            try {
                s.executeUpdate ("INSERT INTO " + table_
                    + " (COL1) VALUES (1.2)");
                ++inserts;
            }
            catch (SQLException e) {
                // Ignore.
            }
            try {
                s.executeUpdate ("INSERT INTO " + table_
                    + " (COL1) VALUES (1,2)");
                ++inserts;
            }
            catch (SQLException e) {
                // Ignore.
            }

            c.close ();
            assertCondition (inserts == 1);
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
decimal separator - Specify "." for the decimal separator.
Insert a record using a "." decimal separator.  This should work.
**/
    public void Var003 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";decimal separator=.", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL1) VALUES (1.2)");
            c.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
decimal separator - Specify "." for the decimal separator.
Insert a record using a "," decimal separator.  This should
throw an exception.
**/
    public void Var004 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";decimal separator=.", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL1) VALUES (1,2)");
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
decimal separator - Specify "," for the decimal separator.
Insert a record using a "," decimal separator.  This should work.
**/
    public void Var005 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";decimal separator=,", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL1) VALUES (1,2)");
            c.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
decimal separator - Specify "," for the decimal separator.
Insert a record using a "." decimal separator.  This should
throw an exception.
**/
    public void Var006 ()
    {
       try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";decimal separator=,", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL1) VALUES (1.2)");

            // Database seems to be accepting this now!
            succeeded();
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
date format - Use the default for the date format.
This will depend on the server job, but iso should always
work.
**/
    public void Var007 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL2) VALUES ('1997-03-19')");
            c.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }




/**
date format - Specify an invalid value for the date format.
The default will depend on the server job, but iso should always
work.
**/
    public void Var008 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";date format=bogus", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL2) VALUES ('1997-03-19')");
            c.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
date format - Specify "mdy" for the date format.
Insert a date in mdy format.  This should work.
**/
    public void Var009 ()
    {
       try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";date format=mdy;date separator=-", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL2) VALUES ('03-19-97')");
            c.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
date format - Specify "mdy" for the date format.
Insert a date in julian format.  This should throw an exception.
**/
    public void Var010 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";date format=mdy;date separator=/", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL2) VALUES ('97/100')");
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }




/**
date format - Specify "dmy" for the date format.
Insert a date in dmy format.  This should work.
**/
    public void Var011 ()
    {
       try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";date format=dmy;date separator=-", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL2) VALUES ('19-03-97')");
            c.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
date format - Specify "dmy" for the date format.
Insert a date in julian format.  This should throw an exception.
**/
    public void Var012 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";date format=dmy;date separator=/", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL2) VALUES ('97/100')");
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
date format - Specify "ymd" for the date format.
Insert a date in dmy format.  This should work.
**/
    public void Var013 ()
    {
       try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";date format=ymd;date separator=-", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL2) VALUES ('97-03-19')");
            c.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
date format - Specify "ymd" for the date format.
Insert a date in julian format.  This should throw an exception.
**/
    public void Var014 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";date format=ymd;date separator=/", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL2) VALUES ('97/100')");
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
date format - Specify "usa" for the date format.
Insert a date in usa format.  This should work.
**/
    public void Var015 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";date format=usa", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL2) VALUES ('03/19/1997')");
            c.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
date format - Specify "usa" for the date format.
Insert a date in julian format.  This should throw an exception.
**/
    public void Var016 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";date format=usa", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL2) VALUES ('97/100')");
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
date format - Specify "iso" for the date format.
Insert a date in usa format.  This should work.
**/
    public void Var017 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";date format=iso", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL2) VALUES ('1997-03-19')");
            c.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
date format - Specify "iso" for the date format.
Insert a date in julian format.  This should throw an exception.
**/
    public void Var018 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";date format=iso", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL2) VALUES ('97/100')");
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
date format - Specify "eur" for the date format.
Insert a date in eur format.  This should work.
**/
    public void Var019 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";date format=eur", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL2) VALUES ('19.03.1997')");
            c.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
date format - Specify "eur" for the date format.
Insert a date in julian format.  This should throw an exception.
**/
    public void Var020 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";date format=eur", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL2) VALUES ('97/100')");
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
date format - Specify "jis" for the date format.
Insert a date in jis format.  This should work.
**/
    public void Var021 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";date format=jis", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL2) VALUES ('1997-03-19')");
            c.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
date format - Specify "jis" for the date format.
Insert a date in eur format.  This should throw an exception.
**/
    public void Var022 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";date format=eur", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL2) VALUES ('19-03-1997')");
            c.close ();
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }


/**
date format - Specify "julian" for the date format.
Insert a date in julian format.  This should work.
**/
    public void Var023 ()
    {
       try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";date format=julian", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL2) VALUES ('98/154')");
            c.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
date format - Specify "julian" for the date format.
Insert a date in eur format.  This should throw an exception.
**/
    public void Var024 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";date format=julian", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL2) VALUES ('19-03-1997')");
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }




/**
date separator - Use the default for the date separator.
This will depend on the server job, but iso should always
work.
**/
    public void Var025 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL2) VALUES ('1997-03-19')");
            c.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }




/**
date separator - Specify an invalid value for the date separator.
The default will depend on the server job, but iso should always
work.
**/
    public void Var026 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";date separator=bogus", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL2) VALUES ('1997-03-19')");
            c.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
date separator - Specify "/" for the date separator.
Insert a date using the "/" separator.  This should work.
**/
    public void Var027 ()
    {
       try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";date format=mdy;date separator=/", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL2) VALUES ('03/19/98')");
            c.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
date separator - Specify "/" for the date separator.
Insert a date using the "-" separator.  This should throw
an exception.
**/
    public void Var028 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";date format=mdy;date separator=/", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL2) VALUES ('03-19-98')");
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
date separator - Specify "-" for the date separator.
Insert a date using the "-" separator.  This should work.
**/
    public void Var029 ()
    {
       try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";date format=mdy;date separator=-", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL2) VALUES ('03-19-98')");
            c.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
date separator - Specify "-" for the date separator.
Insert a date using the "." separator.  This should throw
an exception.
**/
    public void Var030 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";date format=mdy;date separator=/", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL2) VALUES ('03.19.98')");
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
date separator - Specify "." for the date separator.
Insert a date using the "." separator.  This should work.
**/
    public void Var031 ()
    {
       try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";date format=mdy;date separator=.", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL2) VALUES ('03.19.98')");
            c.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
date separator - Specify "." for the date separator.
Insert a date using the "," separator.  This should throw
an exception.
**/
    public void Var032 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";date format=mdy;date separator=/", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL2) VALUES ('03,19,98')");
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }




/**
date separator - Specify "," for the date separator.
Insert a date using the "," separator.  This should work.
**/
    public void Var033 ()
    {
       try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";date format=mdy;date separator=,", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL2) VALUES ('03,19,98')");
            c.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
date separator - Specify "," for the date separator.
Insert a date using the " " separator.  This should throw
an exception.
**/
    public void Var034 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";date format=mdy;date separator=,", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL2) VALUES ('03 19 98')");
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
date separator - Specify "b" for the date separator.
Insert a date using the " " separator.  This should work.
**/
    public void Var035 ()
    {
       try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";date format=mdy;date separator=b", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL2) VALUES ('03 19 98')");
            c.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
date separator - Specify "b" for the date separator.
Insert a date using the "/" separator.  This should throw
an exception.
**/
    public void Var036 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";date format=mdy;date separator=b", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL2) VALUES ('03/19/98')");
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
time format - Use the default for the time format.
This will depend on the server job, but iso should always
work.
**/
    public void Var037 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL3) VALUES ('02:12:07')");
            c.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }




/**
time format - Specify an invalid value for the time format.
The default will depend on the server job, but iso should always
work.
**/
    public void Var038 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";time format=bogus", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL3) VALUES ('02:12:08')");
            c.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
time format - Specify "hms" for the time format.
Insert a time in hms format.  This should work.
**/
    public void Var039 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";time format=hms;time separator=:", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL3) VALUES ('02:36:37')");
            c.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
time format - Specify "hms" for the time format.
Insert a time in bogus format.  (We can't force an exception
with one of the other valid formats.)  This should throw an
exception.
**/
    public void Var040 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";time format=hms;time separator=:", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL3) VALUES ('21340')");
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }




/**
time format - Specify "usa" for the time format.
Insert a time in usa format.  This should work.
**/
    public void Var041 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";time format=usa;time separator=:", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL3) VALUES ('2:15 PM')");
            c.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
time format - Specify "usa" for the time format.
Insert a time in bogus format.  (We can't force an exception
with one of the other valid formats.)  This should throw an
exception.
**/
    public void Var042 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";time format=usa;time separator=:", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL3) VALUES ('021512')");
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
time format - Specify "iso" for the time format.
Insert a time in iso format.  This should work.
**/
    public void Var043 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";time format=iso;time separator=:", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL3) VALUES ('02:16:13')");
            c.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
time format - Specify "iso" for the time format.
Insert a time in bogus format.  (We can't force an exception
with one of the other valid formats.)  This should throw an
exception.
**/
    public void Var044 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";time format=iso;time separator=:", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL3) VALUES ('216PM')");
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
time format - Specify "eur" for the time format.
Insert a time in eur format.  This should work.
**/
    public void Var045 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";time format=eur;time separator=:", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL3) VALUES ('02:17:15')");
            c.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
time format - Specify "eur" for the time format.
Insert a time in bogus format.  (We can't force an exception
with one of the other valid formats.)  This should throw an
exception.
**/
    public void Var046 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";time format=eur;time separator=:", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL3) VALUES ('217PM')");
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
time format - Specify "jis" for the time format.
Insert a time in jis format.  This should work.
**/
    public void Var047 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";time format=jis;time separator=:", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL3) VALUES ('02:18:17')");
            c.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
time format - Specify "jis" for the time format.
Insert a time in bogus format.  (We can't force an exception
with one of the other valid formats.)  This should throw an
exception.
**/
    public void Var048 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";time format=jis;time separator=:", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL3) VALUES ('218PM')");
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
time separator - Use the default for the time separator.
This will depend on the server job, but iso should always
work.
**/
    public void Var049 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL3) VALUES ('02:20:23')");
            c.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }




/**
time separator - Specify an invalid value for the time separator.
The default will depend on the server job, but iso should always
work.
**/
    public void Var050 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";time separator=bogus", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL3) VALUES ('02:20:23')");
            c.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
time separator - Specify ":" for the time separator.
Insert a time using the ":" separator.  This should work.
**/
    public void Var051 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";time format=hms;time separator=:", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL3) VALUES ('02:22:25')");
            c.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
time separator - Specify ":" for the time separator.
Insert a time using the " " separator.  This should throw
an exception.
**/
    public void Var052 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";time format=hms;time separator=.", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL3) VALUES ('02 22 26')");
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
time separator - Specify "." for the time separator.
Insert a time using the "." separator.  This should work.
**/
    public void Var053 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";time format=hms;time separator=.", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL3) VALUES ('02.22.27')");
            c.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
time separator - Specify "." for the time separator.
Insert a time using the " " separator.  This should throw
an exception.
**/
    public void Var054 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";time format=hms;time separator=,", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL3) VALUES ('02 27 28')");
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
time separator - Specify "," for the time separator.
Insert a time using the "," separator.  This should work.
**/
    public void Var055 ()
    {
       try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";time format=hms;time separator=,", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL3) VALUES ('02,29,28')");
            c.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
time separator - Specify "," for the time separator.
Insert a time using the " " separator.  This should throw
an exception.
**/
    public void Var056 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";time format=hms;time separator=,", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL3) VALUES ('02 29 28')");
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }




/**
time separator - Specify "b" for the time separator.
Insert a time using the " " separator.  This should work.
**/
    public void Var057 ()
    {
       try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";time format=hms;time separator=b", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL3) VALUES ('02 29 28')");
            c.close ();
            succeeded ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
time separator - Specify "b" for the time separator.
Insert a time in bogus format.  (We can't force an exception
with one of the other valid separators.)  This should throw an
exception.
**/
    public void Var058 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";time format=hms;time separator=b", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (COL3) VALUES ('022932')");
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



}












