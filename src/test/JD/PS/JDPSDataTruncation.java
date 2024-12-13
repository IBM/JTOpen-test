///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSDataTruncation.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

/*
   // Different types that should be tested:
   // 1) CHAR         1          -done
   // 2) VARCHAR      1          -done
   // 3) CHAR         10         -done
   // 4) VARCHAR      10         -done
   // 5) BINARY       1          -done
   // 6) VARBINARY    1          -done
   // 7) BINARY       10         -done
   // 8) VARBINARY    10         -done               - much is tested by other parts today
   // 9) NUMERIC      (5,0)                          - much is tested by other parts today 
   // 10 DECIMAL      (5,0)                          - much is tested by other parts today
   // 11 NUMERIC      (10,5)                         - much is tested by other parts today
   // 12 DECIMAL      (10,5)
   // 13) CLOB        100        -defer
   // 14) BLOB        100        -defer
   // 15) WCHAR       100                            - unicode data
   // 16) WVARCHAR    100                            - unicode data
   // 17) GRAPHIC                -defer until you understand it better
   // 18) VARCHAPHIC             -defer until you understand it better
   // 19) DATALINK               -defer
   

DT_CHAR1          CHAR(1)      ");
DT_VARCHAR1       VARCHAR(1)       ");
DT_CHAR10         CHAR(10)       ");
DT_VARCHAR10      VARCHAR(10)       ");
DT_BINARY1        CHAR(1)  FOR BIT DATA   ");
DT_VARBINARY1     VARCHAR(1) FOR BIT DATA ");
DT_BINARY10       CHAR(10)  FOR BIT DATA   ");
DT_VARBINARY10    VARCHAR(10) FOR BIT DATA ");
C_BLOB        BLOB(500)     ");
C_CLOB        CLOB(500)     ");


What is tested so far:
char 1.  dt off - Empty string works
char 1.  dt off - char 1 works
char 1.  dt off - char 2 works, only first character inserted
char 1.  dt on - empty string works
char 1.  dt on - char 1 works
char 1   dt on - char 2 fails, get data truncation error.

char 10.  dt off - Empty string works
char 10.  dt off - char 10 works
char 10.  dt off - char 20 works, only first character inserted
char 10.  dt on - empty string works
char 10.  dt on - char 10 works
char 10   dt on - char 20 fails, get data truncation error.

varchar 1.  dt off - Empty string works
varchar 1.  dt off - char 1 works
varchar 1.  dt off - char 2 works, only first character inserted
varchar 1.  dt on - empty string works
varchar 1.  dt on - char 1 works
varchar 1   dt on - char 2 fails, get data truncation error.

varchar 10.  dt off - Empty string works
varchar 10.  dt off - char 10 works
varchar 10.  dt off - char 20 works, only first character inserted
varchar 10.  dt on - empty string works
varchar 10.  dt on - char 10 works
varchar 10   dt on - char 20 fails, get data truncation error.


bainry 1.  dt off - Empty string works
binary 1.  dt off - binary 1 works
binary 1.  dt off - binary 2 works, only first binary inserted
binary 1.  dt on - empty string works
binary 1.  dt on - binary 1 works
binary 1   dt on - binary 2 fails, get data truncation error.

binary 10.  dt off - Empty string works
binary 10.  dt off - binary 10 works
binary 10.  dt off - binary 20 works, only first binaryacter inserted
binary 10.  dt on - empty string works
binary 10.  dt on - binary 10 works
binary 10   dt on - binary 20 fails, get data truncation error.

varbinary 1.  dt off - Empty string works
varbinary 1.  dt off - binary 1 works
varbinary 1.  dt off - binary 2 works, only first binary inserted
varbinary 1.  dt on - empty string works
varbinary 1.  dt on - binary 1 works
varbinary 1   dt on - binary 2 fails, get data truncation error.

varbinary 10.  dt off - Empty string works
varbinary 10.  dt off - binary 10 works
varbinary 10.  dt off - binary 20 works, only first binaryacter inserted
varbinary 10.  dt on - empty string works
varbinary 10.  dt on - binary 10 works
varbinary 10   dt on - binary 20 fails, get data truncation error.
*/

//////////////////////////////////////////////////////////////////////
//
//
//
//
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDPSDataTruncation.java
//
// Classes:      JDPSDataTruncation
//
////////////////////////////////////////////////////////////////////////
//
//
//
//
////////////////////////////////////////////////////////////////////////

package test.JD.PS;

import com.ibm.as400.access.AS400;

import test.JDPSTest;
import test.JDTestDriver;
import test.JDTestcase;
import test.JD.JDTestUtilities;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.SQLWarning;
import java.sql.Types;
import java.util.Hashtable;



/**
Testcase JDPSDataTruncation.  This tests data truncation issues to make sure that
various scenarios are handled correctly by the JDBC driver.
**/
public class JDPSDataTruncation
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDPSDataTruncation";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDPSTest.main(newArgs); 
   }


    // Private data.
    private static String JDPS_DT      = JDPSTest.COLLECTION + ".JDDT";

    private Connection      connection_;     // Default connection that does not expect data trunctions messages
    private Connection      connectionNoDT_;   // Connection that expects data truncation messages



/**
Constructor.
**/
    public JDPSDataTruncation (AS400 systemObject,
                               Hashtable namesAndVars,
                               int runMode,
                               FileOutputStream fileOutputStream,
                               
                               String password)
    {
        super (systemObject, "JDPSDataTruncation",
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
        JDPS_DT      = JDPSTest.COLLECTION + ".JDDT";

        // if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
        if (true) {
            connection_ = testDriver_.getConnection (baseURL_ + ";errors=full", 
                                                     userId_, encryptedPassword_);


            connectionNoDT_ = testDriver_.getConnection (baseURL_ + ";errors=full;data truncation=false", 
                                                       userId_, encryptedPassword_);

            Statement s = connection_.createStatement ();


            StringBuffer buffer = new StringBuffer ();
            buffer.append ("CREATE TABLE ");
            buffer.append (JDPS_DT);
            buffer.append ("(DT_CHAR1          CHAR(1)      ");
            buffer.append (",DT_VARCHAR1       VARCHAR(1)       ");
            buffer.append (",DT_CHAR10         CHAR(10)       ");
            buffer.append (",DT_VARCHAR10      VARCHAR(10)       ");
            buffer.append (",DT_BINARY1        CHAR(1)  FOR BIT DATA   ");
            buffer.append (",DT_VARBINARY1     VARCHAR(1) FOR BIT DATA ");
            buffer.append (",DT_BINARY10       CHAR(10)  FOR BIT DATA   ");
            buffer.append (",DT_VARBINARY10    VARCHAR(10) FOR BIT DATA ");
            if (areLobsSupported ()) {
                buffer.append (",C_BLOB        BLOB(500)     ");
                buffer.append (",C_CLOB        CLOB(500)     ");
            }
            // TODO:  Others to add WCHAR1(10), WVARCHAR1(10), GRAPHIC1(10), VARGRAPHIC1(10)

            buffer.append (")");
            s.executeUpdate (buffer.toString ());

            s.close ();
        }

    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
        // if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
        if (true) {
            Statement s = connection_.createStatement ();
            s.executeUpdate ("DROP TABLE " + JDPS_DT);
            s.close ();
            connection_.close ();
            connectionNoDT_.close();
        }
    }



/**
Checks to make sure that a String value from the input column is the
same as the value passed in for the column value.

@param  column       Column name to look up.
@param  value        The value that is expected to be at that column.
**/   
    protected boolean verifyStringColumn(String column, String value) 
    {
        boolean success = false;

        try {
            Statement stmt = connection_.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT " + column + " FROM " + JDPS_DT);

            if (rs.next()) {
                String compare = rs.getString(1).trim();
                if (compare.equals(value))
                    success = true;
            }

            rs.close();
            stmt.close();

        }
        catch (SQLException e) {
            success = false;
        }

        return success;
    }



/**
Checks to make sure that a Byte array value from the input column is the
same as the value passed in for the column value.

@param  column       Column name to look up.
@param  value        The value that is expected to be at that column.
**/   
    protected boolean verifyByteColumn(String column, byte[] value) 
    {
        boolean success = false;

        try {
            Statement stmt = connection_.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT " + column + " FROM " + JDPS_DT);

            if (rs.next()) {
                byte[] compare = rs.getBytes(1);
                if (isEqual (value, compare))
                    success = true;
            }

            rs.close();
            stmt.close();

        }
        catch (SQLException e) {
            success = false;
        }

        return success;
    }



/**
Performs cleanup after each variation so that the table 
is back to its starting state.
**/
    protected void purge() {
        try {
            Statement stmt = connection_.createStatement();
            stmt.executeUpdate("DELETE FROM " + JDPS_DT);
            stmt.close();
        }
        catch (SQLException e) {
            // Ignore.
        }
    }



/**
Insert an empty string into a column of type CHAR(1) when data truncation is 
turned off. This should work.
**/
    public void Var001()
    {
        String column = "DT_CHAR1";
        String value = "";

        // if (checkNative()) {  
        if (true) {
            try {
                PreparedStatement ps = connectionNoDT_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                     + " (" + column + ") VALUES (?)");

                ps.setString(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyStringColumn(column, value));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert an single char value into a column of type CHAR(1) when data truncation is 
turned off. This should work.
**/
    public void Var002()
    {
        String column = "DT_CHAR1";
        String value = "X";

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connectionNoDT_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                     + " (" + column + ") VALUES (?)");

                ps.setString(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyStringColumn(column, value));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert a value into a column of type CHAR(1) that is larger than the column when data
truncation is turned off. This should work.
**/
    public void Var003()
    {
        String column = "DT_CHAR1";
        String value = "CUJO";

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connectionNoDT_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                     + " (" + column + ") VALUES (?)");

                ps.setString(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyStringColumn(column, value.substring(0, 1)));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert an empty string into a column of type CHAR(1) when data truncation is 
turned on. This should work.
**/
    public void Var004()
    {
        String column = "DT_CHAR1";
        String value = "";

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connection_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                       + " (" + column + ") VALUES (?)");

                ps.setString(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyStringColumn(column, value));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert an single char value into a column of type CHAR(1) when data truncation is 
turned on. This should work.
**/
    public void Var005()
    {
        String column = "DT_CHAR1";
        String value = "X";

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connection_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                       + " (" + column + ") VALUES (?)");

                ps.setString(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyStringColumn(column, value));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert a value into a column of type CHAR(1) that is larger than the column when data
truncation is turned on. This should fail with a data truncation exception.
**/
    public void Var006()
    {
        String column = "DT_CHAR1";
        String value = "CUJO";

        PreparedStatement ps = null;

        // if (checkNative()) {
        if (true) {
            try {
                ps = connection_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                     + " (" + column + ") VALUES (?)");

                ps.setString(1, value);
                ps.execute();
                ps.close();

                failed("Data Truncation Exception was not thrown.");

                purge();

            }
            catch (DataTruncation dt) {
                assertCondition(true);
                // todo: get fancy later....
                //assertCondition ((dt.getIndex() == 1)
                //        && (dt.getParameter() == true)
                //        && (dt.getRead() == false)
                //        && (dt.getDataSize() == 57)
                //        && (dt.getTransferSize() == 50));
            }
            catch (Exception e) {
                failed (e, "Incorrect Exception");
            }
            finally {
                try {
                    if (ps != null)
                        ps.close();
                }
                catch (SQLException e) {
                    // Ignore it.
                }

            }
        }

    }



/**
Insert an empty string into a column of type VARCHAR(1) when data truncation is 
turned off. This should work.
**/
    public void Var007()
    {
        String column = "DT_VARCHAR1";
        String value = "";

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connectionNoDT_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                     + " (" + column + ") VALUES (?)");

                ps.setString(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyStringColumn(column, value));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert an single char value into a column of type VARCHAR(1) when data truncation is 
turned off. This should work.
**/
    public void Var008()
    {
        String column = "DT_VARCHAR1";
        String value = "X";

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connectionNoDT_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                     + " (" + column + ") VALUES (?)");

                ps.setString(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyStringColumn(column, value));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert a value into a column of type VARCHAR(1) that is larger than the column when data
truncation is turned off. This should work.
**/
    public void Var009()
    {
        String column = "DT_VARCHAR1";
        String value = "CUJO";

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connectionNoDT_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                     + " (" + column + ") VALUES (?)");

                ps.setString(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyStringColumn(column, value.substring(0, 1)));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert an empty string into a column of type VARCHAR(1) when data truncation is 
turned on. This should work.
**/
    public void Var010()
    {
        String column = "DT_VARCHAR1";
        String value = "";

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connection_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                       + " (" + column + ") VALUES (?)");

                ps.setString(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyStringColumn(column, value));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert an single char value into a column of type VARCHAR(1) when data truncation is 
turned on. This should work.
**/
    public void Var011()
    {
        String column = "DT_VARCHAR1";
        String value = "X";

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connection_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                       + " (" + column + ") VALUES (?)");

                ps.setString(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyStringColumn(column, value));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert a value into a column of type VARCHAR(1) that is larger than the column when data
truncation is turned on. This should fail with a data truncation exception.
**/
    public void Var012()
    {     
        // if (checkNative()) {
        if (true) {
            String column = "DT_VARCHAR1";
            String value = "CUJO";

            PreparedStatement ps = null;

            try {
                ps = connection_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                     + " (" + column + ") VALUES (?)");

                ps.setString(1, value);
                ps.execute();
                ps.close();

                failed("Data Truncation Exception was not thrown.");

                purge();

            }
            catch (DataTruncation dt) {
                assertCondition(true);
                // todo: get fancy later....
                //assertCondition ((dt.getIndex() == 1)
                //        && (dt.getParameter() == true)
                //        && (dt.getRead() == false)
                //        && (dt.getDataSize() == 57)
                //        && (dt.getTransferSize() == 50));
            }
            catch (Exception e) {
                failed (e, "Incorrect Exception");
            }
            finally {
                try {
                    if (ps != null)
                        ps.close();
                }
                catch (SQLException e) {
                    // Ignore it.
                }

            }
        }

    }



/**
Insert an empty string into a column of type CHAR(10) when data truncation is 
turned off. This should work.
**/
    public void Var013()
    {
        String column = "DT_CHAR10";
        String value = "";

        // if (checkNative()) {
        if (true) {
           try {
                PreparedStatement ps = connectionNoDT_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                     + " (" + column + ") VALUES (?)");

                ps.setString(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyStringColumn(column, value));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert a 10 char value into a column of type CHAR(10) when data truncation is 
turned off. This should work.
**/
    public void Var014()
    {
        String column = "DT_CHAR10";
        String value = "CUJOCUJOCU";

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connectionNoDT_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                     + " (" + column + ") VALUES (?)");

                ps.setString(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyStringColumn(column, value));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert a value into a column of type CHAR(10) that is larger than the column when data
truncation is turned off. This should work.
**/
    public void Var015()
    {
        String column = "DT_CHAR10";
        String value = "CUJOCUJOCUJOCUJO";

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connectionNoDT_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                     + " (" + column + ") VALUES (?)");

                ps.setString(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyStringColumn(column, value.substring(0, 10)));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");            }
        }
    }



/**
Insert an empty string into a column of type CHAR(10) when data truncation is 
turned on. This should work.
**/
    public void Var016()
    {
        String column = "DT_CHAR10";
        String value = "";

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connection_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                       + " (" + column + ") VALUES (?)");

                ps.setString(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyStringColumn(column, value));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert a 10 char value into a column of type CHAR(10) when data truncation is 
turned on. This should work.
**/
    public void Var017()
    {
        String column = "DT_CHAR10";
        String value = "CUJOCUJOCU";

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connection_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                       + " (" + column + ") VALUES (?)");

                ps.setString(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyStringColumn(column, value));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert a value into a column of type CHAR(10) that is larger than the column when data
truncation is turned on. This should fail with a data truncation exception.
**/
    public void Var018()
    {    
      // if (checkNative()) {
        if (true) {
            String column = "DT_CHAR10";
            String value = "CUJOCUJOCUJOCUJO";

            PreparedStatement ps = null;

            try {
                ps = connection_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                     + " (" + column + ") VALUES (?)");

                ps.setString(1, value);
                ps.execute();
                ps.close();

                failed("Data Truncation Exception was not thrown.");

                purge();

            }
            catch (DataTruncation dt) {
                assertCondition(true);
                // todo: get fancy later....
                //assertCondition ((dt.getIndex() == 1)
                //        && (dt.getParameter() == true)
                //        && (dt.getRead() == false)
                //        && (dt.getDataSize() == 57)
                //        && (dt.getTransferSize() == 50));
            }
            catch (Exception e) {
                failed (e, "Incorrect Exception");
            }
            finally {
                try {
                    if (ps != null)
                        ps.close();
                }
                catch (SQLException e) {
                    // Ignore it.
                }

            }
        }

    }



/**
Insert an empty string into a column of type VARCHAR(10) when data truncation is 
turned off. This should work.
**/
    public void Var019()
    {
        String column = "DT_VARCHAR10";
        String value = "";

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connectionNoDT_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                     + " (" + column + ") VALUES (?)");

                ps.setString(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyStringColumn(column, value));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert a 10 char value into a column of type VARCHAR(10) when data truncation is 
turned off. This should work.
**/
    public void Var020()
    {
        String column = "DT_VARCHAR10";
        String value = "CUJOCUJOCU";

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connectionNoDT_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                     + " (" + column + ") VALUES (?)");

                ps.setString(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyStringColumn(column, value));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert a value into a column of type VARCHAR(10) that is larger than the column when data
truncation is turned off. This should work.
**/
    public void Var021()
    {
        String column = "DT_VARCHAR10";
        String value = "CUJOCUJOCUJOCUJO";

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connectionNoDT_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                     + " (" + column + ") VALUES (?)");

                ps.setString(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyStringColumn(column, value.substring(0, 10)));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert an empty string into a column of type VARCHAR(10) when data truncation is 
turned on. This should work.
**/
    public void Var022()
    {
        String column = "DT_VARCHAR10";
        String value = "";

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connection_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                       + " (" + column + ") VALUES (?)");

                ps.setString(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyStringColumn(column, value));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert a 10 char value into a column of type VARCHAR(10) when data truncation is 
turned on. This should work.
**/
    public void Var023()
    {
        String column = "DT_VARCHAR10";
        String value = "CUJOCUJOCU";

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connection_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                       + " (" + column + ") VALUES (?)");

                ps.setString(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyStringColumn(column, value));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert a value into a column of type VARCHAR(10) that is larger than the column when data
truncation is turned on. This should fail with a data truncation exception.
**/
    public void Var024()
    {    // if (checkNative()) {
        if (true) {
            String column = "DT_VARCHAR10";
            String value = "CUJOCUJOCUJOCUJO";

            PreparedStatement ps = null;

            try {
                ps = connection_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                     + " (" + column + ") VALUES (?)");

                ps.setString(1, value);
                ps.execute();
                ps.close();

                failed("Data Truncation Exception was not thrown.");

                purge();

            }
            catch (DataTruncation dt) {
                assertCondition(true);
                // todo: get fancy later....
                //assertCondition ((dt.getIndex() == 1)
                //        && (dt.getParameter() == true)
                //        && (dt.getRead() == false)
                //        && (dt.getDataSize() == 57)
                //        && (dt.getTransferSize() == 50));
            }
            catch (Exception e) {
                failed (e, "Incorrect Exception");
            }
            finally {
                try {
                    if (ps != null)
                        ps.close();
                }
                catch (SQLException e) {
                    // Ignore it.
                }

            }
        }

    }



/**
Insert an empty byte array into a column of type BINARY(1) when data truncation is 
turned off. This should work.
**/
    public void Var025()
    {
        String column = "DT_BINARY1";
        byte[] value = new byte[] {};
        byte[] compareValue = new byte[] {(byte) 0};

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connectionNoDT_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                     + " (" + column + ") VALUES (?)");

                ps.setBytes(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyByteColumn(column, compareValue));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert an single byte value into a column of type BINARY(1) when data truncation is 
turned off. This should work.
**/
    public void Var026()
    {
        String column = "DT_BINARY1";
        byte[] value        = new byte[] {(byte) 27};
        byte[] compareValue = new byte[] {(byte) 27};

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connectionNoDT_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                     + " (" + column + ") VALUES (?)");

                ps.setBytes(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyByteColumn(column, compareValue));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert a value into a column of type BINARY(1) that is larger than the column when data
truncation is turned off. This should work.
**/
    public void Var027()
    {
        String column = "DT_BINARY1";
        byte[] value        = new byte[] {(byte) 27, (byte) 28, (byte) 29, (byte) 30};
        byte[] compareValue = new byte[] {(byte) 27};

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connectionNoDT_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                     + " (" + column + ") VALUES (?)");

                ps.setBytes(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyByteColumn(column, compareValue));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    } 



/**
Insert an empty byte array into a column of type BINARY(1) when data truncation is 
turned on. This should work.
**/
    public void Var028()
    {
        String column = "DT_BINARY1";
        byte[] value = new byte[] {};
        byte[] compareValue = new byte[] {(byte) 0};

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connection_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                       + " (" + column + ") VALUES (?)");

                ps.setBytes(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyByteColumn(column, compareValue));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert an single byte value into a column of type BINARY(1) when data truncation is 
turned on. This should work.
**/
    public void Var029()
    {
        String column = "DT_BINARY1";
        byte[] value        = new byte[] {(byte) 27};
        byte[] compareValue = new byte[] {(byte) 27};

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connection_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                       + " (" + column + ") VALUES (?)");

                ps.setBytes(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyByteColumn(column, compareValue));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert a value into a column of type BINARY(1) that is larger than the column when data
truncation is turned on. This should fail with a data truncation exception.
**/
    public void Var030()
    {  // if (checkNative()) {
        if (true) {
            String column = "DT_BINARY1";
            byte[] value        = new byte[] {(byte) 27, (byte) 28, (byte) 29, (byte) 30};

            PreparedStatement ps = null;


            try {
                ps = connection_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                     + " (" + column + ") VALUES (?)");

                ps.setBytes(1, value);
                ps.execute();
                ps.close();

                failed("Data Truncation Exception was not thrown.");

                purge();

            }
            catch (DataTruncation dt) {
                assertCondition(true);
                // todo: get fancy later....
                //assertCondition ((dt.getIndex() == 1)
                //        && (dt.getParameter() == true)
                //        && (dt.getRead() == false)
                //        && (dt.getDataSize() == 57)
                //        && (dt.getTransferSize() == 50));
            }
            catch (Exception e) {
                failed (e, "Incorrect Exception");
            }
            finally {
                try {
                    if (ps != null)
                        ps.close();
                }
                catch (SQLException e) {
                    // Ignore it.
                }

            }
        }

    }



/**
Insert an empty byte array into a column of type VARBINARY(1) when data truncation is 
turned off. This should work.
**/
    public void Var031()
    {
        String column = "DT_VARBINARY1";
        byte[] value = new byte[] {};
        byte[] compareValue = new byte[] {};

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connectionNoDT_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                     + " (" + column + ") VALUES (?)");

                ps.setBytes(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyByteColumn(column, compareValue));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert an single byte value into a column of type VARBINARY(1) when data truncation is 
turned off. This should work.
**/
    public void Var032()
    {
        String column = "DT_VARBINARY1";
        byte[] value        = new byte[] {(byte) 27};
        byte[] compareValue = new byte[] {(byte) 27};

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connectionNoDT_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                     + " (" + column + ") VALUES (?)");

                ps.setBytes(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyByteColumn(column, compareValue));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert a value into a column of type VARBINARY(1) that is larger than the column when data
truncation is turned off. This should work.
**/
    public void Var033()
    {
        String column = "DT_VARBINARY1";
        byte[] value        = new byte[] {(byte) 27, (byte) 28, (byte) 29, (byte) 30};
        byte[] compareValue = new byte[] {(byte) 27};

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connectionNoDT_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                     + " (" + column + ") VALUES (?)");

                ps.setBytes(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyByteColumn(column, compareValue));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert an empty byte array into a column of type VARBINARY(1) when data truncation is 
turned on. This should work.
**/
    public void Var034()
    {
        String column = "DT_VARBINARY1";
        byte[] value = new byte[] {};
        byte[] compareValue = new byte[] {};

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connection_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                       + " (" + column + ") VALUES (?)");

                ps.setBytes(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyByteColumn(column, compareValue));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert an single byte value into a column of type VARBINARY(1) when data truncation is 
turned on. This should work.
**/
    public void Var035()
    {
        String column = "DT_VARBINARY1";
        byte[] value        = new byte[] {(byte) 27};
        byte[] compareValue = new byte[] {(byte) 27};

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connection_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                       + " (" + column + ") VALUES (?)");

                ps.setBytes(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyByteColumn(column, compareValue));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert a value into a column of type VARBINARY(1) that is larger than the column when data
truncation is turned on. This should fail with a data truncation exception.
**/
    public void Var036()
    {
        String column = "DT_VARBINARY1";
        byte[] value        = new byte[] {(byte) 27, (byte) 28, (byte) 29, (byte) 30};

        PreparedStatement ps = null;

        // if (checkNative()) {
        if (true) {
            try {
                ps = connection_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                     + " (" + column + ") VALUES (?)");

                ps.setBytes(1, value);
                ps.execute();
                ps.close();

                failed("Data Truncation Exception was not thrown.");

                purge();

            }
            catch (DataTruncation dt) {
                assertCondition(true);
                // todo: get fancy later....
                //assertCondition ((dt.getIndex() == 1)
                //        && (dt.getParameter() == true)
                //        && (dt.getRead() == false)
                //        && (dt.getDataSize() == 57)
                //        && (dt.getTransferSize() == 50));
            }
            catch (Exception e) {
                failed (e, "Incorrect Exception");
            }
            finally {
                try {
                    if (ps != null)
                        ps.close();
                }
                catch (SQLException e) {
                    // Ignore it.
                }

            }
        }

    }



/**
Insert an empty byte array into a column of type BINARY(10) when data truncation is 
turned off. This should work.
**/
    public void Var037()
    {
        String column = "DT_BINARY10";
        byte[] value = new byte[] {};
        byte[] compareValue = new byte[] {(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, 
            (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0};

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connectionNoDT_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                     + " (" + column + ") VALUES (?)");

                ps.setBytes(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyByteColumn(column, compareValue));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert a byte array of length 10 into a column of type BINARY(10) when data truncation is 
turned off. This should work.
**/
    public void Var038()
    {
        String column = "DT_BINARY10";
        byte[] value        = new byte[] {(byte) 27, (byte) 28, (byte) 29, (byte) 30, (byte) 31, 
            (byte) 32, (byte) 33, (byte) 34, (byte) 35, (byte) 36};
        byte[] compareValue = new byte[] {(byte) 27, (byte) 28, (byte) 29, (byte) 30, (byte) 31, 
            (byte) 32, (byte) 33, (byte) 34, (byte) 35, (byte) 36};

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connectionNoDT_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                     + " (" + column + ") VALUES (?)");

                ps.setBytes(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyByteColumn(column, compareValue));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert a value into a column of type BINARY(10) that is larger than the column when data
truncation is turned off. This should work.
**/
    public void Var039()
    {
        String column = "DT_BINARY10";
        byte[] value        = new byte[] {(byte) 27, (byte) 28, (byte) 29, (byte) 30, (byte) 31, 
            (byte) 32, (byte) 33, (byte) 34, (byte) 35, (byte) 36,
            (byte) 37, (byte) 38, (byte) 39, (byte) 40, (byte) 41};
        byte[] compareValue = new byte[] {(byte) 27, (byte) 28, (byte) 29, (byte) 30, (byte) 31, 
            (byte) 32, (byte) 33, (byte) 34, (byte) 35, (byte) 36};

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connectionNoDT_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                     + " (" + column + ") VALUES (?)");

                ps.setBytes(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyByteColumn(column, compareValue));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert an empty byte array into a column of type BINARY(10) when data truncation is 
turned on. This should work.
**/
    public void Var040()
    {
        String column = "DT_BINARY10";
        byte[] value = new byte[] {};
        byte[] compareValue = new byte[] {(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, 
            (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0};

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connection_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                       + " (" + column + ") VALUES (?)");

                ps.setBytes(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyByteColumn(column, compareValue));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert a byte array of length 10 into a column of type BINARY(10) when data truncation is 
turned on. This should work.
**/
    public void Var041()
    {
        String column = "DT_BINARY10";
        byte[] value        = new byte[] {(byte) 27, (byte) 28, (byte) 29, (byte) 30, (byte) 31, 
            (byte) 32, (byte) 33, (byte) 34, (byte) 35, (byte) 36};
        byte[] compareValue = new byte[] {(byte) 27, (byte) 28, (byte) 29, (byte) 30, (byte) 31, 
            (byte) 32, (byte) 33, (byte) 34, (byte) 35, (byte) 36};

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connection_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                       + " (" + column + ") VALUES (?)");

                ps.setBytes(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyByteColumn(column, compareValue));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert a value into a column of type BINARY(10) that is larger than the column when data
truncation is turned on. This should fail with a data truncation exception.
**/
    public void Var042()
    {
        String column = "DT_BINARY10";
        byte[] value        = new byte[] {(byte) 27, (byte) 28, (byte) 29, (byte) 30, (byte) 31, 
            (byte) 32, (byte) 33, (byte) 34, (byte) 35, (byte) 36,
            (byte) 37, (byte) 38, (byte) 39, (byte) 40, (byte) 41};

        PreparedStatement ps = null;

        // if (checkNative()) {
        if (true) {
            try {
                ps = connection_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                     + " (" + column + ") VALUES (?)");

                ps.setBytes(1, value);
                ps.execute();
                ps.close();

                failed("Data Truncation Exception was not thrown.");

                purge();

            }
            catch (DataTruncation dt) {
                assertCondition(true);
                // todo: get fancy later....
                //assertCondition ((dt.getIndex() == 1)
                //        && (dt.getParameter() == true)
                //        && (dt.getRead() == false)
                //        && (dt.getDataSize() == 57)
                //        && (dt.getTransferSize() == 50));
            }
            catch (Exception e) {
                failed (e, "Incorrect Exception");
            }
            finally {
                try {
                    if (ps != null)
                        ps.close();
                }
                catch (SQLException e) {
                    // Ignore it.
                }

            }
        }

    }



/**
Insert an empty byte array into a column of type VARBINARY(10) when data truncation is 
turned off. This should work.
**/
    public void Var043()
    {
        String column = "DT_VARBINARY10";
        byte[] value = new byte[] {};
        byte[] compareValue = new byte[] {};

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connectionNoDT_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                     + " (" + column + ") VALUES (?)");

                ps.setBytes(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyByteColumn(column, compareValue));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert a byte array of length 10 into a column of type VARBINARY(10) when data truncation is 
turned off. This should work.
**/
    public void Var044()
    {
        String column = "DT_VARBINARY10";
        byte[] value        = new byte[] {(byte) 27, (byte) 28, (byte) 29, (byte) 30, (byte) 31, 
            (byte) 32, (byte) 33, (byte) 34, (byte) 35, (byte) 36};
        byte[] compareValue = new byte[] {(byte) 27, (byte) 28, (byte) 29, (byte) 30, (byte) 31, 
            (byte) 32, (byte) 33, (byte) 34, (byte) 35, (byte) 36};

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connectionNoDT_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                     + " (" + column + ") VALUES (?)");

                ps.setBytes(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyByteColumn(column, compareValue));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert a value into a column of type VARBINARY(10) that is larger than the column when data
truncation is turned off. This should work.
**/
    public void Var045()
    {
        String column = "DT_VARBINARY10";
        byte[] value        = new byte[] {(byte) 27, (byte) 28, (byte) 29, (byte) 30, (byte) 31, 
            (byte) 32, (byte) 33, (byte) 34, (byte) 35, (byte) 36,
            (byte) 37, (byte) 38, (byte) 39, (byte) 40, (byte) 41};
        byte[] compareValue = new byte[] {(byte) 27, (byte) 28, (byte) 29, (byte) 30, (byte) 31, 
            (byte) 32, (byte) 33, (byte) 34, (byte) 35, (byte) 36};

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connectionNoDT_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                     + " (" + column + ") VALUES (?)");

                ps.setBytes(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyByteColumn(column, compareValue));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert an empty byte array into a column of type VARBINARY(10) when data truncation is 
turned on. This should work.
**/
    public void Var046()
    {
        String column = "DT_VARBINARY10";
        byte[] value = new byte[] {};
        byte[] compareValue = new byte[] {};

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connection_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                       + " (" + column + ") VALUES (?)");

                ps.setBytes(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyByteColumn(column, compareValue));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert a byte array of length 10 into a column of type VARBINARY(10) when data truncation is 
turned on. This should work.
**/
    public void Var047()
    {
        String column = "DT_VARBINARY10";
        byte[] value        = new byte[] {(byte) 27, (byte) 28, (byte) 29, (byte) 30, (byte) 31, 
            (byte) 32, (byte) 33, (byte) 34, (byte) 35, (byte) 36};
        byte[] compareValue = new byte[] {(byte) 27, (byte) 28, (byte) 29, (byte) 30, (byte) 31, 
            (byte) 32, (byte) 33, (byte) 34, (byte) 35, (byte) 36};

        // if (checkNative()) {
        if (true) {
            try {
                PreparedStatement ps = connection_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                                       + " (" + column + ") VALUES (?)");

                ps.setBytes(1, value);
                ps.execute();
                ps.close();

                assertCondition(verifyByteColumn(column, compareValue));

                purge();

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
Insert a value into a column of type VARBINARY(10) that is larger than the column when data
truncation is turned on. This should fail with a data truncation exception.
**/
    public void Var048()
    {
        String column = "DT_VARBINARY10";
        byte[] value        = new byte[] {(byte) 27, (byte) 28, (byte) 29, (byte) 30, (byte) 31, 
            (byte) 32, (byte) 33, (byte) 34, (byte) 35, (byte) 36,
            (byte) 37, (byte) 38, (byte) 39, (byte) 40, (byte) 41};

        PreparedStatement ps = null;

        // if (checkNative()) {
        if (true) {
            try {
                ps = connection_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                     + " (" + column + ") VALUES (?)");

                ps.setBytes(1, value);
                ps.execute();
                ps.close();

                failed("Data Truncation Exception was not thrown.");

                purge();

            }
            catch (DataTruncation dt) {
                assertCondition(true);
                // todo: get fancy later....
                //assertCondition ((dt.getIndex() == 1)
                //        && (dt.getParameter() == true)
                //        && (dt.getRead() == false)
                //        && (dt.getDataSize() == 57)
                //        && (dt.getTransferSize() == 50));
            }
            catch (Exception e) {
                failed (e, "Incorrect Exception");
            }
            finally {
                try {
                    if (ps != null)
                        ps.close();
                }
                catch (SQLException e) {
                    // Ignore it.
                }

            }
        }

    }



/**
Ensure warnings are posted when parameter marker on set too long.
**/
    public void Var049()
    {
        String column = "DT_CHAR1";
        String value = "X";      
        PreparedStatement ps;

        if (proxy_ != null && (!proxy_.equals(""))) {
            notApplicable("Testcase doss not work for proxy driver");
            return; 
        }


        // if (checkNative()) {
        if (true) 
        {
            // First, add a row to the table 
            try 
            {
                ps = connection_.prepareStatement ("INSERT INTO " + JDPS_DT
                                              + " (" + column + ") VALUES (?)");
                ps.setString(1, value);
                ps.execute();
                ps.close();
            }
            catch (Exception e) 
            {
                failed (e, "Setup failed -- could not add row to table");
                return;
            }

                                            
            // Now query that row
            try 
            {
                ps = connection_.prepareStatement ("SELECT DT_CHAR1 FROM " + JDPS_DT
                                                 + " WHERE DT_CHAR1 = ? ");
                ps.setString(1, "XXXXX");  
                
                SQLWarning warnings = ps.getWarnings();

                if (warnings == null)
                {
                   failed("No warnings after string too long");
                   return;
                }     

		//
		// For JDK 1.6, the default java.sql.DataTrucation warning will return
                // a state of 22001 when read is set to false.  In this case, it isn't
		// a read so we will have the expected warning.
		//
		String expectedWarning = "01004"; 

        if (getJDK() >= 160) {// may be running under jdk16 and jdbc30 implementation
		    expectedWarning = "22001"; 
		} 
		
                if (! (warnings.getSQLState().startsWith(expectedWarning)))
                {
		   warnings.printStackTrace(); 
                   failed("SQLState wrong.  Expected "+expectedWarning+".  Received " + warnings.getSQLState()+ "-- "+warnings.toString()+" -- errorcode="+warnings.getErrorCode());
                   return;
                }                                     

                if (warnings.getNextWarning() != null)
                {
                   failed("Too many warnings in list");
                   return;
                }
                
                ResultSet rs = ps.executeQuery();

                warnings = ps.getWarnings();

                if (warnings != null)
                {
                   failed("Warnings not cleared after execute query");
                   return;
                }    
                
                if (! rs.next())
                {
                   failed("no result set returned");
                   return;
                } 

                if (! rs.getString(1).equals("X"))
                {
                   failed("data returned in results set is wrong.  Expected X.  Received " + rs.getString(1));
                } 
                 
                ps.close();

                assertCondition(true);
            }
            catch (Exception e) 
            {
                failed (e, "Setup failed -- could not add row to table");
                return;
            }
        }
    }

/**                   
Insert a value into a column of type VARCHAR(1) that is larger than the column when data
truncation is turned on, then do a setNull().  The first set should fail with a
data truncation error, the second should work.
**/
    public void Var050()
    {     
        if (true) 
        {
            String column = "DT_VARCHAR1";
            String value = "CUJO";

            PreparedStatement ps = null;
            boolean truncationThrown = false;

            try 
            {
                ps = connection_.prepareStatement ("INSERT INTO " + JDPS_DT
                                                     + " (" + column + ") VALUES (?)");
            
                // this one should throw a data truncation exception   
                try
                {
                   ps.setString(1, value);
                }
                catch (DataTruncation dt) {truncationThrown = true;}
                                      
                if (truncationThrown)           
                {
                   // this should work (before a fix this would throw an exception 
                   // because truncation was checked even when set-null called).
                   ps.setNull(1, Types.VARCHAR);
                   succeeded();                             
                }
                else
                   failed("variation setup failed -- no truncation exception thrown the first time");   
            }
            catch (Exception e) 
            {
                failed (e, "Unexpected Exception");
            }
            finally 
            {
                try {
                    if (ps != null)
                        ps.close();
                }
                catch (SQLException e) { }
            }
        }
    }



    /*
     * Test the truncation of an open CCSID.
   */ 
    public void testCCSID(String dataType, int size,  int CCSID, String data) {
	StringBuffer sb = new StringBuffer();
	String sql=""; 
	try {
	    sb.append("Testcase added 10/8/2012 to test mixed ccsid truncation\n");

	    boolean passed = true;

	    String tablename= JDPSTest.COLLECTION  + ".JDPSDT"+CCSID;
	    Statement stmt = connection_.createStatement();
	    try {
		sql = "DROP TABLE "+tablename;
		sb.append("Executing "+sql+"\n"); 
		stmt.executeUpdate(sql); 
	    } catch (Exception e) {
	    } 

	    sql = "CREATE TABLE "+tablename+" (C1 "+dataType+"("+size+") CCSID "+CCSID+" )";
	    sb.append("Executing "+sql+"\n"); 
	    stmt.executeUpdate(sql); 

	    sql = "INSERT INTO "+tablename+" VALUES (?)";
	    sb.append("Preparing "+sql+"\n"); 
	    PreparedStatement pstmt = connection_.prepareStatement(sql);

	    sb.append("Setting string to "+JDTestUtilities.getMixedString(data)+"\n"); 
	    try { 
		pstmt.setString(1, data);
		sb.append("Executing\n");
		pstmt.executeUpdate();
		sb.append("Failed, did not get exception\n");
		  passed = false;
		  sql = "Select * from "+tablename;
		  sb.append("looking at stuff using "+sql); 
		    ResultSet rs = stmt.executeQuery(sql);  
		  rs.next(); 
		    String added = rs.getString(1); 
		     sb.append("Erroneously added "+JDTestUtilities.getMixedString(added) +"\n"); 
		  
	    } catch (Exception e) {
		String expected = "Data truncation";
		String expected2 = "NOT AAPPLLICCABLE"; 
		String message = e.toString();
		if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		    // For some CCSIDs the database detects the truncation
		    expected2="Value for column or variable C1 too long"; 
		} 
		if ((message.indexOf(expected) < 0) && (message.indexOf(expected2) < 0) ) {
		    passed = false;
		    sb.append("Did not find '"+expected+"' in exception "+message+"\n");
		    printStackTraceToStringBuffer(e,  sb); 
		} 
	    } 

	    sql = "DROP TABLE "+tablename;
	    sb.append("Executing "+sql+"\n"); 
	    stmt.executeUpdate(sql); 
	    pstmt.close();
	    stmt.close(); 
	    assertCondition(passed,sb); 

	} catch (Exception e) {
	    failed (e, "Unexpected Exception \n"+sb.toString());
	}

    }

    

    public void Var051() { 	testCCSID("VARCHAR", 10, 930,"1\u6885345678");     }; /* ready */ 
    public void Var052() { 	testCCSID("CHAR", 10, 930,"1\u6885345678");     };/* ready */ 

    public void Var053() {  testCCSID("VARCHAR", 10, 933,"1\u6885345678");     };/* ready */
    public void Var054() {  testCCSID("CHAR", 10, 933,"1\u6885345678");     };/* ready */

    public void Var055() {  testCCSID("VARCHAR", 10, 935,"1\u6885345678");     };/* ready */
    public void Var056() {  testCCSID("CHAR", 10, 935,"1\u6885345678");     };/* ready */

    public void Var057() {  testCCSID("VARCHAR", 10, 937,"1\u6885345678");     };/* ready */
    public void Var058() {  testCCSID("CHAR", 10, 937,"1\u6885345678");     };/* ready */

    public void Var059() {  testCCSID("VARCHAR", 10, 939,"1\u6885345678");     }; /* ready */ 
    public void Var060() {  testCCSID("CHAR", 10, 939,"1\u6885345678");     };   /* ready */ 

    public void Var061() {  testCCSID("VARCHAR", 10, 1364,"1\u6885345678");     };/* ready */
    public void Var062() {  testCCSID("CHAR", 10, 1364,"1\u6885345678");     };/* ready */

    public void Var063() {  testCCSID("VARCHAR", 10, 1371,"1\u6885345678");     };/* ready */
    public void Var064() {  testCCSID("CHAR", 10, 1371,"1\u6885345678");     };/* ready */
    
public void Var065() {  testCCSID("VARCHAR", 10, 1388,"1\u6885345678");     };/* ready */
public void Var066() {  testCCSID("CHAR", 10, 1388,"1\u6885345678");     };/* ready */


public void Var067() {  testCCSID("VARCHAR", 10, 1399,"1\u6885345678");     };/* ready */ 
public void Var068() {  testCCSID("CHAR", 10, 1399,"1\u6885345678");     };/* ready */ 

public void Var069() {  testCCSID("VARCHAR", 10, 5026,"1\u6885345678");     };/* ready */ 
public void Var070() {  testCCSID("CHAR", 10, 5026,"1\u6885345678");     };/* ready */ 

public void Var071() {  testCCSID("VARCHAR", 10, 5035,"1\u6885345678");     };/* ready */ 
public void Var072() {  testCCSID("CHAR", 10, 5035,"1\u6885345678");     };/* ready */ 

public void Var073() {  testCCSID("VARCHAR", 10, 1208,"1\u68853456789");     }; /* ready */ 
public void Var074() {  testCCSID("CHAR", 10, 1208,"1\u68853456789");     }; /* ready */

public void Var075() {

    if (getRelease() == JDTestDriver.RELEASE_V7R1M0 && getDriver() == JDTestDriver.DRIVER_NATIVE) {
	notApplicable("Fails in V5R4 for native driver.  Not fixing");
	return; 
    }
    StringBuffer sb = new StringBuffer(" -- Added 11/19/2012 to Test toolbox data truncation\n"+
				       "   Reported by forum\n");

    try {
	String[][] tests =  {
	     /*   TYPE         SETTYPE,   VALUE                   RESULT */ 
     { "NUMERIC(13,2)", "String",  "123456789012.23",    "Data truncation sqlstate=01004 errorCode=0 datasize=14 transferSize=13"},
      { "NUMERIC(13,2)", "String",  "123456789012.2",     "Data truncation sqlstate=01004 errorCode=0 datasize=14 transferSize=13"},
      { "NUMERIC(13,2)", "String",  "123456789012",       "Data truncation sqlstate=01004 errorCode=0 datasize=14 transferSize=13"},
      { "NUMERIC(13,2)", "String",  "123456789010",       "Data truncation sqlstate=01004 errorCode=0 datasize=14 transferSize=13"},
      { "DECIMAL(13,2)", "String",  "123456789012.23",    "Data truncation sqlstate=01004 errorCode=0 datasize=14 transferSize=13"},
      { "DECIMAL(13,2)", "String",  "123456789012.2",     "Data truncation sqlstate=01004 errorCode=0 datasize=14 transferSize=13"},
      { "DECIMAL(13,2)", "String",  "123456789012",       "Data truncation sqlstate=01004 errorCode=0 datasize=14 transferSize=13"},
      { "DECIMAL(13,2)", "String",  "123456789010",       "Data truncation sqlstate=01004 errorCode=0 datasize=14 transferSize=13"},

	}; 

	String[][] tests16 =  {
	     /*   TYPE         SETTYPE,   VALUE                   RESULT */ 
	    { "NUMERIC(13,2)", "String",  "123456789012.23",    "Data truncation sqlstate=22001 errorCode=0 datasize=14 transferSize=13"},
      { "NUMERIC(13,2)", "String",  "123456789012.2",     "Data truncation sqlstate=22001 errorCode=0 datasize=14 transferSize=13"},
      { "NUMERIC(13,2)", "String",  "123456789012",       "Data truncation sqlstate=22001 errorCode=0 datasize=14 transferSize=13"},
      { "NUMERIC(13,2)", "String",  "123456789010",       "Data truncation sqlstate=22001 errorCode=0 datasize=14 transferSize=13"},
      { "DECIMAL(13,2)", "String",  "123456789012.23",    "Data truncation sqlstate=22001 errorCode=0 datasize=14 transferSize=13"},
      { "DECIMAL(13,2)", "String",  "123456789012.2",     "Data truncation sqlstate=22001 errorCode=0 datasize=14 transferSize=13"},
      { "DECIMAL(13,2)", "String",  "123456789012",       "Data truncation sqlstate=22001 errorCode=0 datasize=14 transferSize=13"},
      { "DECIMAL(13,2)", "String",  "123456789010",       "Data truncation sqlstate=22001 errorCode=0 datasize=14 transferSize=13"},

	}; 


		String[][] testsToolbox =  {
	     /*   TYPE         SETTYPE,   VALUE                   RESULT */ 
      { "NUMERIC(13,2)", "String",  "123456789012.23",    "Data type mismatch. (P#=1) sqlstate=07006 errorCode=-99999"},
      { "NUMERIC(13,2)", "String",  "123456789012.2",     "Data type mismatch. (P#=1) sqlstate=07006 errorCode=-99999"},
      { "NUMERIC(13,2)", "String",  "123456789012",       "Data type mismatch. (P#=1) sqlstate=07006 errorCode=-99999"},
      { "NUMERIC(13,2)", "String",  "123456789010",       "Data type mismatch. (P#=1) sqlstate=07006 errorCode=-99999"},
      { "DECIMAL(13,2)", "String",  "123456789012.23",    "Data type mismatch. (P#=1) sqlstate=07006 errorCode=-99999"},
      { "DECIMAL(13,2)", "String",  "123456789012.2",     "Data type mismatch. (P#=1) sqlstate=07006 errorCode=-99999"},
      { "DECIMAL(13,2)", "String",  "123456789012",       "Data type mismatch. (P#=1) sqlstate=07006 errorCode=-99999"},
      { "DECIMAL(13,2)", "String",  "123456789010",       "Data type mismatch. (P#=1) sqlstate=07006 errorCode=-99999"},

	}; 


	boolean passed  = true;
	PreparedStatement ps = null;
	String            psText = "";

	/* Primed on V7R1 */ 
	if (getJDK() >= 160) {
	    tests= tests16; 
	}
	if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
	    tests = testsToolbox; 
	} 

	for (int i =0; i < tests.length; i++) {
	    
	    try {
		sb.append("Running testcase "+tests[i][0]+" "+tests[i][1]+" "+tests[i][2]+" '"+tests[i][3]+"'\n");

		String sql = "SELECT CAST ( ? AS "+tests[i][0]+") from sysibm.sysdummy1";
		if (! sql.equals(psText)) {
		    if (ps != null) ps.close(); 
		    ps = connection_.prepareStatement(sql);
		    psText = sql; 
		}
		if (tests[i][1].equals("String")) {
		    ps.setString(1, tests[i][2]); 
		} else {
		    passed=false;
		    sb.append("******setType ("+tests[i][1]+")not recognized\n"); 
		}

		ResultSet rs = ps.executeQuery();
		rs.next();
		String outputValue = rs.getString(1);
		if (!outputValue.equals(tests[i][3])) {
		    passed = false;
		    sb.append("**** GOT got  "+outputValue+"\n"); 
		}

	    } catch (Exception e) {
		String formattedException = formatException(e);
		if (! matchingException(formattedException, tests[i][3], sb)) {
		    passed = false;
		    printStackTraceToStringBuffer(e, sb); 
		}
		
	    }   
	    
	} 

	assertCondition(passed,sb);

	
    } catch (Exception e) {
      failed (e, "Unexpected Exception \n"+sb.toString());
    }


}


  /**
   * Ensure warnings are posted when parameter marker on set too long and data
   * is UTF 8 that expands
   **/
  public void Var076() {
    String added = "-- added 10/20/2017 for Toolbox Bug #395"; 
    if (proxy_ != null && (!proxy_.equals(""))) {
      notApplicable("Testcase doss not work for proxy driver");
      return;
    }

    // Run a query where the truncation is detected at character conversion
    // time.
    try {
      PreparedStatement ps = connection_
          .prepareStatement("select * from sysibm.sysdummy1 where IBMREQD = CAST(? AS VARCHAR(8) CCSID 1208)");
      ps.setString(1, "\u00fccontent");

      SQLWarning warnings = ps.getWarnings();

      ResultSet rs = ps.executeQuery();
      
      if (warnings == null) { 
        warnings = ps.getWarnings();
      }

      ps.close();

      if (warnings == null ) { 
        failed("No warnings returned "+added); 
      }
      //
      // For JDK 1.6, the default java.sql.DataTrucation warning will return
      // a state of 22001 when read is set to false. In this case, it isn't
      // a read so we will have the expected warning.
      //
      String expectedWarning = "01004";

      if (getJDK() >= 160) {// may be running under jdk16 and jdbc30
                            // implementation
        expectedWarning = "22001";
      }

      if (!(warnings.getSQLState().startsWith(expectedWarning))) {
        warnings.printStackTrace();
        failed("SQLState wrong.  Expected " + expectedWarning + ".  Received "
            + warnings.getSQLState() + "-- " + warnings.toString()
            + " -- errorcode=" + warnings.getErrorCode()+added);
        return;
      }

      if (warnings.getNextWarning() != null) {
        failed("Too many warnings in list"+added);
        return;
      }

      assertCondition(true);
    } catch (Exception e) {
      failed(e, "Unexpected exception "+added);
      return;
    }
  } /* var076 */    

  /**
   * Ensure warnings are posted when parameter marker on set too long and data
   * is UTF 8 that expands
   **/
  public void Var077() {
    String added = "-- added 10/20/2017 for Toolbox Bug #395"; 
    if (proxy_ != null && (!proxy_.equals(""))) {
      notApplicable("Testcase doss not work for proxy driver");
      return;
    }

    // Run a query where the truncation is detected at character conversion
    // time.
    try {
      PreparedStatement ps = connection_
          .prepareStatement("select * from sysibm.sysdummy1 where IBMREQD = CAST(? AS VARCHAR(8) CCSID 1208)");
      ps.setString(1, "content\u00fc");

      SQLWarning warnings = ps.getWarnings();

      ResultSet rs = ps.executeQuery();
      
      if (warnings == null) { 
        warnings = ps.getWarnings();
      }

      ps.close();

      if (warnings == null ) { 
        failed("No warnings returned "+added); 
      }
      //
      // For JDK 1.6, the default java.sql.DataTrucation warning will return
      // a state of 22001 when read is set to false. In this case, it isn't
      // a read so we will have the expected warning.
      //
      String expectedWarning = "01004";

      if (getJDK() >= 160) {//  may be running under jdk16 and jdbc30
                            // implementation
        expectedWarning = "22001";
      }

      if (!(warnings.getSQLState().startsWith(expectedWarning))) {
        warnings.printStackTrace();
        failed("SQLState wrong.  Expected " + expectedWarning + ".  Received "
            + warnings.getSQLState() + "-- " + warnings.toString()
            + " -- errorcode=" + warnings.getErrorCode()+added);
        return;
      }

      if (warnings.getNextWarning() != null) {
        failed("Too many warnings in list"+added);
        return;
      }

      assertCondition(true);
    } catch (Exception e) {
      failed(e, "Unexpected exception "+added);
      return;
    }
  } /* var077 */    

  /**
  Insert a value into a column of type CHAR(8) CCSID 1208 that is larger than the column when data
  truncation is turned on. This should fail with a data truncation exception.
  **/
  public void Var078() {
    String added = " -- added 10/30/2017 for toolbox bug #395";
    PreparedStatement ps = null;
    String tablename = collection_ + ".JDPSDT78";
    Statement stmt = null;
    try {
      stmt = connection_.createStatement();
      try {
        stmt.executeUpdate("DROP TABLE " + tablename);
      } catch (Exception e) {

      }
      stmt.executeUpdate("CREATE TABLE " + tablename
          + " ( COL1 VARCHAR(8) CCSID 1208)");
      ps = connection_.prepareStatement("INSERT INTO " + tablename
          + " VALUES (?)");

      ps.setString(1, "\u00fccontent");
      ps.execute();
      ps.close();

      failed("Data Truncation Exception was not thrown." + added);

      purge();

    } catch (DataTruncation dt) {
      String message = dt.getMessage(); 
      assertCondition(message.equals("Data truncation"), "Got expection '"+message+"' sb 'Data truncation'");
      // todo: get fancy later....
      // assertCondition ((dt.getIndex() == 1)
      // && (dt.getParameter() == true)
      // && (dt.getRead() == false)
      // && (dt.getDataSize() == 57)
      // && (dt.getTransferSize() == 50));
    } catch (Exception e) {
      failed(e, "Incorrect Exception");
    } finally {
      try {
        if (stmt != null)
          stmt.close();
        if (ps != null)
          ps.close();
      } catch (SQLException e) {
        // Ignore it.
      }

    }

  }

  /**
  Insert a value into a column of type CHAR(8) CCSID 1208 that is larger than the column when data
  truncation is turned on. This should fail with a data truncation exception.
  **/
  public void Var079() {
    String added = " -- added 10/30/2017 for toolbox bug #395";
    PreparedStatement ps = null;
    String tablename = collection_ + ".JDPSDT79";
    Statement stmt = null;
    try {
      stmt = connection_.createStatement();
      try {
        stmt.executeUpdate("DROP TABLE " + tablename);
      } catch (Exception e) {

      }
      stmt.executeUpdate("CREATE TABLE " + tablename
          + " ( COL1 VARCHAR(8) CCSID 1208)");
      ps = connection_.prepareStatement("INSERT INTO " + tablename
          + " VALUES (?)");

      ps.setString(1, "content\u00fc");
      ps.execute();
      ps.close();

      failed("Data Truncation Exception was not thrown." + added);

      purge();

    } catch (DataTruncation dt) {
      String message = dt.getMessage(); 
      assertCondition(message.equals("Data truncation"), "Got expection '"+message+"' sb 'Data truncation'");
      // todo: get fancy later....
      // assertCondition ((dt.getIndex() == 1)
      // && (dt.getParameter() == true)
      // && (dt.getRead() == false)
      // && (dt.getDataSize() == 57)
      // && (dt.getTransferSize() == 50));
    } catch (Exception e) {
      failed(e, "Incorrect Exception");
    } finally {
      try {
        if (stmt != null)
          stmt.close();
        if (ps != null)
          ps.close();
      } catch (SQLException e) {
        // Ignore it.
      }

    }

  }


private boolean matchingException(String formattedException, String string,
    StringBuffer sb) {
  sb.append("looking for '"+string+"' in '"+formattedException+"'\n");
  if (formattedException.indexOf(string) >= 0) {
    return true; 
  } else {
    return false; 
  }
  
  
}



public static String formatException(Exception e) {
    StringBuffer sb = new StringBuffer(); 
    if (e instanceof DataTruncation) {
      DataTruncation dt = (DataTruncation) e;
      sb.append(e.getMessage()+" sqlstate="+dt.getSQLState()+ " errorCode="+dt.getErrorCode()+" datasize="+dt.getDataSize()+ " transferSize="+dt.getTransferSize());  
    } else if (e instanceof SQLException) {
          SQLException sqlex = (SQLException) e; 
          sb.append(e.getMessage()+" sqlstate="+sqlex.getSQLState()+ " errorCode="+sqlex.getErrorCode());  
          
    } else {
      sb.append(e.toString()); 
    }
    return sb.toString(); 
} 


}
