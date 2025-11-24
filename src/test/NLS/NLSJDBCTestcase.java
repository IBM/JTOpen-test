///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NLSJDBCTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.NLS;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDLobTest;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JDTestcase;
import test.NLSTest;
import test.PasswordVault;
import test.JD.JDSetupCollection;



/**
Testcase NLSJDBCTestcase.  This test class verifies the use of DBCS Strings
in selected JDBC testcase variations.
**/
public class NLSJDBCTestcase
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NLSJDBCTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NLSTest.main(newArgs); 
   }


    // Private data.
    private static final String COLLECTION          = "JDTESTNL";
    private static final String COLLECTION2         = "JDTESTNL2";
    private static final String COLLECTIONXX        = "JDTESTNLXX";

    private static String jdbc_dbcs_string20a;
    private static String jdbc_dbcs_string20b;
    private static String[] jdbc_dbcs_words;

    private Connection          localConnection_;

    StringBuffer sb = new StringBuffer(); 

/**
Constructor.
**/
    public NLSJDBCTestcase (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "NLSJDBCTestcase",
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
        // DriverManager.registerDriver (new AS400JDBCDriver ());
        Class.forName ("com.ibm.as400.access.AS400JDBCDriver");

        String url = baseURL_
            
            ;
        localConnection_ = DriverManager.getConnection (url);

        jdbc_dbcs_string20a = getResource ("JDBC_DBCS_STRING20A");
        jdbc_dbcs_string20b = getResource ("JDBC_DBCS_STRING20B");
        jdbc_dbcs_words     = new String[20];
        for (int i=1; i<=20; i++)
            jdbc_dbcs_words[i-1] = getResource("JDBC_DBCS_WORDS"+i);

        JDSetupCollection.create (systemObject_, 
            localConnection_, COLLECTION);
        JDSetupCollection.create (systemObject_, 
            localConnection_, COLLECTION2);
        JDSetupCollection.create (systemObject_, 
            localConnection_, COLLECTIONXX);
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
        localConnection_.close ();
    }



/**
DatabaseMetaData.getBestRowIdentifier() - Passes in a null for catalog.
One row should be returned. No Exceptions should be thrown.
**/
    public void Var001()
    {
        try {
            // Variation setup.
            Statement s = localConnection_.createStatement ();
            s.executeUpdate ("CREATE TABLE " + COLLECTION
                + ".BR1 (CUSTID INT, NAME CHAR(10) NOT NULL, "
                + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DECIMAL(6,2), "
                + " CONSTRAINT " + COLLECTION + ".BRKEY1 UNIQUE (CUSTID))");
            s.executeUpdate ("CREATE TABLE " + COLLECTION
                + ".BR2 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DECIMAL(6,2), "
                + " CONSTRAINT " + COLLECTION + ".BRKEY2 PRIMARY KEY (NAME))");
            s.executeUpdate ("CREATE TABLE " + COLLECTION2
                + ".BR1 (CUSTID INT, NAME CHAR(10) NOT NULL, "
                + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DECIMAL(6,2), "
                + " CONSTRAINT " + COLLECTION2 + ".BRKEY3 UNIQUE (ACCTNBR))");
            s.executeUpdate ("CREATE TABLE " + COLLECTION2
                + ".BR2 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DECIMAL(6,2), "
                + " CONSTRAINT " + COLLECTION2 + ".BRKEY4 UNIQUE (BALANCE))");
            s.close ();

            DatabaseMetaData dmd = localConnection_.getMetaData ();
            ResultSet rs = dmd.getBestRowIdentifier (null, COLLECTION,
                "BR1", DatabaseMetaData.bestRowTemporary, true);
            boolean success = true;

            int rows = 0;
            while (rs.next ()) {
                ++rows;

                short scope             = rs.getShort ("SCOPE");
                String columnName       = rs.getString ("COLUMN_NAME");
                short dataType          = rs.getShort ("DATA_TYPE");
                String typeName         = rs.getString ("TYPE_NAME");
                int columnSize          = rs.getInt ("COLUMN_SIZE");
                int bufferLength        = rs.getInt ("BUFFER_LENGTH");
                short decimalDigits     = rs.getShort ("DECIMAL_DIGITS");
                short pseudoColumn      = rs.getShort ("PSEUDO_COLUMN");

                success = success && (scope == DatabaseMetaData.bestRowTemporary);
                success = success && (columnName.equals ("CUSTID"));
                success = success && (dataType == Types.INTEGER);
                success = success && (typeName.equals ("INTEGER"));
                success = success && (columnSize == 4);
                success = success && (bufferLength == 0);
                success = success && (decimalDigits == 0);
                success = success && (pseudoColumn == DatabaseMetaData.bestRowNotPseudo);
            }

            rs.close ();
            assertCondition ((rows == 1) && success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        finally {
            // Variation cleanup.
            try {
                Statement s = localConnection_.createStatement ();

                s.executeUpdate ("ALTER TABLE " + COLLECTION
                    + ".BR1 DROP UNIQUE " + COLLECTION + ".BRKEY1");
                s.executeUpdate ("ALTER TABLE " + COLLECTION
                    + ".BR2 DROP PRIMARY KEY CASCADE");
                s.executeUpdate ("ALTER TABLE " + COLLECTION2
                    + ".BR1 DROP UNIQUE " + COLLECTION2 + ".BRKEY3");
                s.executeUpdate ("ALTER TABLE " + COLLECTION2
                    + ".BR2 DROP UNIQUE " + COLLECTION2 + ".BRKEY4");

                s.executeUpdate ("DROP TABLE " + COLLECTION
                    + ".BR1");
                s.executeUpdate ("DROP TABLE " + COLLECTION
                    + ".BR2");
                s.executeUpdate ("DROP TABLE " + COLLECTION2
                    + ".BR1");
                s.executeUpdate ("DROP TABLE " + COLLECTION2
                    + ".BR2");

                s.close ();
            }
            catch (SQLException e) {
                e.printStackTrace (System.out);
            }
        }
    }


/**
DatabaseMetaData.getColumns() - Passes in a valid value containing
no patterns for all arguments.  No Exceptions should be thrown.
**/
    public void Var002()
    {
        try {
            // Variation setup.
            Statement s = localConnection_.createStatement ();
            s.executeUpdate ("CREATE TABLE " + COLLECTION
                + ".COLUMNS (COLUMN INT NOT NULL, COLUMN1 TIMESTAMP, "
                + "COLUMN2 CHAR(10) NOT NULL, "
                + "COLUMNXX DECIMAL(6,2))");
            s.executeUpdate ("CREATE TABLE " + COLLECTION
                + ".COLUMNS1 (COLUMN1 INT, COLUMN2 CHAR(10))");
            s.executeUpdate ("CREATE TABLE " + COLLECTION
                + ".COLUMNS2 (COLUMN1 INT, COLUMNXX CHAR(10))");
            s.executeUpdate ("CREATE TABLE " + COLLECTION2
                + ".COLUMNS (COLUMN2 DECIMAL, COLUMNXX CHAR(10))");

            s.executeUpdate ("LABEL ON COLUMN " + COLLECTION
                    + ".COLUMNS (COLUMN1 TEXT IS '8')");
            s.close ();

            DatabaseMetaData dmd = localConnection_.getMetaData ();
            ResultSet rs = dmd.getColumns (null, COLLECTION,
                "COLUMNS", "COLUMN1");
            boolean success = true;

            int rows = 0;
            while (rs.next ()) {
                ++rows;
                success = success && rs.getString ("TABLE_CAT").equals (localConnection_.getCatalog ());
                success = success && rs.getString ("TABLE_SCHEM").equals (COLLECTION);
                success = success && rs.getString ("TABLE_NAME").equals ("COLUMNS");

                String column           = rs.getString ("COLUMN_NAME");
                short dataType          = rs.getShort ("DATA_TYPE");
                String typeName         = rs.getString ("TYPE_NAME");
                int columnSize          = rs.getInt ("COLUMN_SIZE");
                int bufferLength        = rs.getInt ("BUFFER_LENGTH");
                int decimalDigits       = rs.getInt ("DECIMAL_DIGITS");
                int numPrecRadix        = rs.getInt ("NUM_PREC_RADIX");
                int nullable            = rs.getInt ("NULLABLE");
                String remarks          = rs.getString ("REMARKS");
                String columnDef        = rs.getString ("COLUMN_DEF");
                int sqlDataType         = rs.getInt ("SQL_DATA_TYPE");
                int sqlDateTimeSub      = rs.getInt ("SQL_DATETIME_SUB");
                int charOctetLength     = rs.getInt ("CHAR_OCTET_LENGTH");
                int ordinalPosition     = rs.getInt ("ORDINAL_POSITION");
                String isNullable       = rs.getString ("IS_NULLABLE");


                success = success && (bufferLength == 0);
                success = success && (columnDef == null);
                success = success && (sqlDataType == 0);
                success = success && (sqlDateTimeSub == 0);

                success = success && (column.equals ("COLUMN1"));
                if (column.equals ("COLUMN1")) {
                    success = success && ((ordinalPosition == -1) || (ordinalPosition == 2)); // @E1C
                    success = success && (charOctetLength == -1);
                    success = success && remarks.equals ("8");
                    success = success && (dataType == Types.TIMESTAMP);
                    success = success && typeName.equals ("TIMESTAMP");
                    success = success && (columnSize == 26);
                    success = success && (decimalDigits == 6);
                    success = success && (numPrecRadix == 0);
                    success = success && (nullable == DatabaseMetaData.columnNullable);
                    success = success && isNullable.equals ("YES");
                }

            }

            rs.close ();
            assertCondition ((rows == 1) && success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        finally {
            try {
                // Variation cleanup.
                Statement s = localConnection_.createStatement ();

                s.executeUpdate ("DROP TABLE " + COLLECTION
                    + ".COLUMNS");
                s.executeUpdate ("DROP TABLE " + COLLECTION
                    + ".COLUMNS1");
                s.executeUpdate ("DROP TABLE " + COLLECTION
                    + ".COLUMNS2");
                s.executeUpdate ("DROP TABLE " + COLLECTION2
                    + ".COLUMNS");

                s.close ();
            }
            catch (SQLException e) {
                e.printStackTrace (System.out);
            }
        }
    }

/**
DatabaseMetaData.getColumnPrivileges() - Passes in valid values for
all parameters.  One row should be returned.  No Exceptions should be thrown.
**/
    public void Var003()
    {
        try {
            // Variation setup.
            Statement s = localConnection_.createStatement ();
            s.executeUpdate ("CREATE TABLE " + COLLECTION
                + ".CPRIVS (COLUMN INT NOT NULL, COLUMN1 TIMESTAMP, "
                + "COLUMN2 CHAR(10) NOT NULL, "
                + "COLUMNXX DECIMAL(6,2))");
            s.executeUpdate ("CREATE TABLE " + COLLECTION
                + ".CPRIVS1 (COLUMN1 INT, COLUMN2 CHAR(10))");
            s.executeUpdate ("CREATE TABLE " + COLLECTION
                + ".CPRIVS2 (COLUMN1 INT, COLUMNXX CHAR(10))");
            s.executeUpdate ("CREATE TABLE " + COLLECTION2
                + ".CPRIVS (COLUMN2 DECIMAL, COLUMNXX CHAR(10))");
            s.close ();

            DatabaseMetaData dmd = localConnection_.getMetaData ();
            String userName = dmd.getUserName ();
            ResultSet rs = dmd.getColumnPrivileges (null, COLLECTION,
                                                     "CPRIVS", null);
            boolean success = true;

            int rows = 0;
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            boolean check4 = false;
            while (rs.next ()) {
                ++rows;
                success = success && rs.getString ("TABLE_CAT").equals (localConnection_.getCatalog ());

                String schemaName   = rs.getString ("TABLE_SCHEM");
                String tableName    = rs.getString ("TABLE_NAME");
                String columnName   = rs.getString ("COLUMN_NAME");

                // System.out.println (schemaName + ":" + tableName + ":" + columnName + ":");

                success = success && schemaName.equals (COLLECTION);
                success = success && tableName.equals ("CPRIVS");

                if (columnName.equals ("COLUMN"))
                    check1 = true;
                if (columnName.equals ("COLUMN1"))
                    check2 = true;
                if (columnName.equals ("COLUMN2"))
                    check3 = true;
                if (columnName.equals ("COLUMNXX"))
                    check4 = true;

                String grantor              = rs.getString ("GRANTOR");
                boolean grantorWasNull      = rs.wasNull ();
                String grantee              = rs.getString ("GRANTEE");
                String privilege            = rs.getString ("PRIVILEGE");
                String isGrantable          = rs.getString ("IS_GRANTABLE");
                boolean isGrantableWasNull  = rs.wasNull ();

                //  System.out.println (grantor + ":" + grantorWasNull + ":" + grantee + ":"
                //                    + privilege + ":" + isGrantable + ":" + isGrantableWasNull + ":");

                success = success && (grantor == null) && grantorWasNull;
                success = success && grantee.equals (userName);

                // It would be nice to try to force this to be
                // different values, but nobody seems to understand how
                // to do it!
                success = success && privilege.equals ("");

                success = success && (isGrantable == null) && isGrantableWasNull;
            }

            rs.close ();

            // System.out.println ("Rows = " + rows);

            assertCondition ((rows == 4) && check1 && check2 && check3 && check4 && success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        finally {
            try {
                // Variation cleanup.
                Statement s = localConnection_.createStatement ();

                s.executeUpdate ("DROP TABLE " + COLLECTION
                    + ".CPRIVS");
                s.executeUpdate ("DROP TABLE " + COLLECTION
                    + ".CPRIVS1");
                s.executeUpdate ("DROP TABLE " + COLLECTION
                    + ".CPRIVS2");
                s.executeUpdate ("DROP TABLE " + COLLECTION2
                    + ".CPRIVS");

                s.close ();
            }
            catch (SQLException e) {
                e.printStackTrace (System.out);
            }
        }
    }



/**
DatabaseMetaData.getColumns() - Passes in a column pattern
containing "%" and "_" wildcards.  No Exceptions should be thrown.
**/
    public void Var004()
    {
        try {
            // Variation setup.
            Statement s = localConnection_.createStatement ();
            s.executeUpdate ("CREATE TABLE " + COLLECTION
                + ".COLUMNS (COLUMN INT NOT NULL, COLUMN1 TIMESTAMP, "
                + "COLUMN2 CHAR(10) NOT NULL, "
                + "COLUMNXX DECIMAL(6,2))");
            s.executeUpdate ("CREATE TABLE " + COLLECTION
                + ".COLUMNS1 (COLUMN1 INT, COLUMN2 CHAR(10))");
            s.executeUpdate ("CREATE TABLE " + COLLECTION
                + ".COLUMNS2 (COLUMN1 INT, COLUMNXX CHAR(10))");
            s.executeUpdate ("CREATE TABLE " + COLLECTION2
                + ".COLUMNS (COLUMN2 DECIMAL, COLUMNXX CHAR(10))");

            // Label the columns.  No label on COLUMN, please.
            s = localConnection_.createStatement ();
            s.executeUpdate ("LABEL ON COLUMN " + COLLECTION
                + ".COLUMNS (COLUMN1 TEXT IS '8')");
            s.executeUpdate ("LABEL ON COLUMN " + COLLECTION
                + ".COLUMNS (COLUMN2 TEXT IS 'Nine')");
            s.executeUpdate ("LABEL ON COLUMN " + COLLECTION
                + ".COLUMNS (COLUMNXX TEXT IS 'SQL/400')");
            s.close ();

            DatabaseMetaData dmd = localConnection_.getMetaData ();
            ResultSet rs = dmd.getColumns (null, COLLECTION,
                "COLUMNS", "COL_MN%");
            boolean success = true;

            int rows = 0;
            while (rs.next ()) {
                ++rows;
                success = success && rs.getString ("TABLE_CAT").equals (localConnection_.getCatalog ());
                success = success && rs.getString ("TABLE_SCHEM").equals (COLLECTION);
                success = success && rs.getString ("TABLE_NAME").equals ("COLUMNS");

                String column           = rs.getString ("COLUMN_NAME");
                short dataType          = rs.getShort ("DATA_TYPE");
                String typeName         = rs.getString ("TYPE_NAME");
                int columnSize          = rs.getInt ("COLUMN_SIZE");
                int bufferLength        = rs.getInt ("BUFFER_LENGTH");
                int decimalDigits       = rs.getInt ("DECIMAL_DIGITS");
                int numPrecRadix        = rs.getInt ("NUM_PREC_RADIX");
                int nullable            = rs.getInt ("NULLABLE");
                String remarks          = rs.getString ("REMARKS");
                String columnDef        = rs.getString ("COLUMN_DEF");
                int sqlDataType         = rs.getInt ("SQL_DATA_TYPE");
                int sqlDateTimeSub      = rs.getInt ("SQL_DATETIME_SUB");
                int charOctetLength     = rs.getInt ("CHAR_OCTET_LENGTH");
                int ordinalPosition     = rs.getInt ("ORDINAL_POSITION");
                String isNullable       = rs.getString ("IS_NULLABLE");

                success = success && (bufferLength == 0);
                success = success && (columnDef == null);
                success = success && (sqlDataType == 0);
                success = success && (sqlDateTimeSub == 0);

                if (column.equals ("COLUMN"))
                {
                    if (getDriver () == JDTestDriver.DRIVER_NATIVE)
                    {
                        success = success && (remarks == null);
                        success = success && (ordinalPosition == 1);
                        success = success && (charOctetLength == 0);
                    }
                    else
                    {
                        success = success && remarks.equals ("");
                        success = success && ((ordinalPosition == -1) || (ordinalPosition == 1));  // @E1C
                        success = success && (charOctetLength == -1);
                    }

                    success = success && (dataType == Types.INTEGER);
                    success = success && typeName.equals ("INTEGER");
                    success = success && (columnSize == 10);
                    success = success && (decimalDigits == 0);
                    success = success && (numPrecRadix == 10);
                    success = success && (nullable == DatabaseMetaData.columnNoNulls);
                    success = success && isNullable.equals ("NO");
                }
                else if (column.equals ("COLUMN1"))
                {
                    if (getDriver () == JDTestDriver.DRIVER_NATIVE)
                    {
                        success = success && (ordinalPosition == 2);
                        success = success && (charOctetLength == 0);
                        success = success && remarks.equals ("8");
                        success = success && (dataType == 9 /* Types.TIMESTAMP */ );
                        success = success && typeName.equals ("TIMESTMP" /*"TIMESTAMP"*/ );
                        success = success && (columnSize == 0 /* 26 */ );
                        success = success && (decimalDigits == 0 /* 6  */ );
                        success = success && (numPrecRadix == 0);
                        success = success && (nullable == DatabaseMetaData.columnNullable);
                        success = success && isNullable.equals ("YES");
                    }
                    else
                    {
                        success = success && ((ordinalPosition == -1) || (ordinalPosition == 2));  // @E1C
                        success = success && (charOctetLength == -1);
                        success = success && remarks.equals ("8");
                        success = success && (dataType == Types.TIMESTAMP);
                        success = success && typeName.equals ("TIMESTAMP");
                        success = success && (columnSize == 26);
                        success = success && (decimalDigits == 6);
                        success = success && (numPrecRadix == 0);
                        success = success && (nullable == DatabaseMetaData.columnNullable);
                        success = success && isNullable.equals ("YES");
                    }

                }
                else if (column.equals ("COLUMN2"))
                {
                    if (getDriver () == JDTestDriver.DRIVER_NATIVE)
                    {
                        success = success && (ordinalPosition == 3);
                    }
                    else
                    {
                        success = success && ((ordinalPosition == -1) || (ordinalPosition == 3));  // @E1C
                    }

                    success = success && remarks.equals ("Nine");
                    success = success && (dataType == Types.CHAR);
                    success = success && typeName.equals ("CHAR");
                    success = success && (columnSize == 10);
                    success = success && (decimalDigits == 0);
                    success = success && (numPrecRadix == 0);
                    success = success && (nullable == DatabaseMetaData.columnNoNulls);
                    success = success && (charOctetLength == 10);
                    success = success && isNullable.equals ("NO");
                }
                else if (column.equals ("COLUMNXX"))
                {
                    if (getDriver () == JDTestDriver.DRIVER_NATIVE)
                    {
                        success = success && (ordinalPosition == 4);
                        success = success && (charOctetLength == 0);
                    }
                    else
                    {
                        success = success && ((ordinalPosition == -1) || (ordinalPosition == 4));  // @E1C
                        success = success && (charOctetLength == -1);
                    }

                    success = success && remarks.equals ("SQL/400");
                    success = success && (dataType == Types.DECIMAL);
                    success = success && typeName.equals ("DECIMAL");
                    success = success && (columnSize == 6);
                    success = success && (decimalDigits == 2);
                    success = success && (numPrecRadix == 10);
                    success = success && (nullable == DatabaseMetaData.columnNullable);
                    success = success && isNullable.equals ("YES");
                }
            }

            rs.close ();
            assertCondition ((rows == 4) && success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        finally {
            try {
                // Variation cleanup.
                Statement s = localConnection_.createStatement ();

                s.executeUpdate ("DROP TABLE " + COLLECTION
                    + ".COLUMNS");
                s.executeUpdate ("DROP TABLE " + COLLECTION
                    + ".COLUMNS1");
                s.executeUpdate ("DROP TABLE " + COLLECTION
                    + ".COLUMNS2");
                s.executeUpdate ("DROP TABLE " + COLLECTION2
                    + ".COLUMNS");

                s.close ();
            }
            catch (SQLException e) {
                e.printStackTrace (System.out);
            }
        }
    }



/**
DatabaseMetaData.getProcedures() - Passes all valid arguments.
One procedure should be returned. No Exceptions should be thrown.
**/
    public void Var005()
    {
        try {
            // Variation setup.
            Statement s = localConnection_.createStatement ();
            s.executeUpdate ("CREATE PROCEDURE " + COLLECTION
                + ".PROCS LANGUAGE SQL READS SQL DATA P1: BEGIN DECLARE DUMMY INTEGER;"
                + " SET DUMMY = 5; END P1");
            s.executeUpdate ("CREATE PROCEDURE " + COLLECTION
                + ".PROCS1 RESULT SET 1 LANGUAGE SQL READS SQL DATA P1: BEGIN "
                + " DECLARE C1 CURSOR FOR SELECT * FROM QIWS.QCUSTCDT; "
                + " OPEN C1; SET RESULT SETS CURSOR C1; END P1");
            s.executeUpdate ("CREATE PROCEDURE " + COLLECTION
                + ".PROCS2 LANGUAGE SQL READS SQL DATA P1: BEGIN DECLARE DUMMY INTEGER;"
                + " SET DUMMY = 5; END P1");
            s.executeUpdate ("CREATE PROCEDURE " + COLLECTION
                + ".PROCSXX LANGUAGE SQL READS SQL DATA P1: BEGIN DECLARE DUMMY INTEGER;"
                + " SET DUMMY = 5; END P1");
            s.executeUpdate ("CREATE PROCEDURE " + COLLECTION2
                + ".PROCS LANGUAGE SQL READS SQL DATA P1: BEGIN DECLARE DUMMY INTEGER;"
                + " SET DUMMY = 5; END P1");
            s.executeUpdate ("CREATE PROCEDURE " + COLLECTION2
                + ".PROCS3 RESULT SET 1 LANGUAGE SQL READS SQL DATA P1: BEGIN "
                + " DECLARE C1 CURSOR FOR SELECT * FROM QIWS.QCUSTCDT; "
                + " OPEN C1; SET RESULT SETS CURSOR C1; END P1");
            s.executeUpdate ("CREATE PROCEDURE " + COLLECTION2
                + ".PROCS4 LANGUAGE SQL READS SQL DATA P1: BEGIN DECLARE DUMMY INTEGER;"
                + " SET DUMMY = 5; END P1");
            s.executeUpdate ("CREATE PROCEDURE " + COLLECTIONXX
                + ".PROCSXX LANGUAGE SQL READS SQL DATA P1: BEGIN DECLARE DUMMY INTEGER;"
                + " SET DUMMY = 5; END P1");
            s.close ();

            DatabaseMetaData dmd = localConnection_.getMetaData ();
            ResultSet rs = dmd.getProcedures (null, COLLECTION,
                "PROCS%");
            boolean success = true;

            int rows = 0;
            while (rs.next ()) {
                ++rows;
                success = success && rs.getString ("PROCEDURE_CAT").equals (localConnection_.getCatalog ());
                success = success && rs.getString ("PROCEDURE_SCHEM").equals (COLLECTION);
                success = success && rs.getString ("REMARKS").equals ("");

                String procedureName = rs.getString ("PROCEDURE_NAME");
                short procedureType = rs.getShort ("PROCEDURE_TYPE");

                if (procedureName.equals ("PROCS")) {
                    success = success && (procedureType == DatabaseMetaData.procedureNoResult);
                }
                else if (procedureName.equals ("PROCS1")) {
                    success = success && (procedureType == DatabaseMetaData.procedureReturnsResult);
                }
                else if (procedureName.equals ("PROCS2")) {
                    success = success && (procedureType == DatabaseMetaData.procedureNoResult);
                }
                else if (procedureName.equals ("PROCSXX")) {
                    success = success && (procedureType == DatabaseMetaData.procedureNoResult);
                }
            }

            rs.close ();
            assertCondition ((rows == 4) && success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        finally {
            try {
                // Variation cleanup.
                Statement s = localConnection_.createStatement ();

                s.executeUpdate ("DROP PROCEDURE " + COLLECTION
                    + ".PROCS");
                s.executeUpdate ("DROP PROCEDURE " + COLLECTION
                    + ".PROCS1");
                s.executeUpdate ("DROP PROCEDURE " + COLLECTION
                    + ".PROCS2");
                s.executeUpdate ("DROP PROCEDURE " + COLLECTION
                    + ".PROCSXX");
                s.executeUpdate ("DROP PROCEDURE " + COLLECTION2
                    + ".PROCS");
                s.executeUpdate ("DROP PROCEDURE " + COLLECTION2
                    + ".PROCS3");
                s.executeUpdate ("DROP PROCEDURE " + COLLECTION2
                    + ".PROCS4");
                s.executeUpdate ("DROP PROCEDURE " + COLLECTIONXX
                    + ".PROCSXX");

                s.close ();
            }
            catch (SQLException e) {
                e.printStackTrace (System.out);
            }
        }
    }



/**
DatabaseMetaData.getPrimaryKeys() - Passes in a valid catalog name.
One row should be returned. No Exceptions should be thrown.
**/
    public void Var006()
    {
        try {
            // Variation setup.
            Statement s = localConnection_.createStatement ();

            s.executeUpdate ("CREATE TABLE " + COLLECTION
                             + ".PK1 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                             + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DEC (6,2), "
                             + "CONSTRAINT " + COLLECTION
                             + ".PKKEY1 PRIMARY KEY (CUSTID))");

            s.executeUpdate ("CREATE TABLE " + COLLECTION
                             + ".PK2 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                             + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DEC (6,2), "
                             + "CONSTRAINT " + COLLECTION
                             + ".PKKEY2 PRIMARY KEY (NAME))");

            s.executeUpdate ("CREATE TABLE " + COLLECTION2
                             + ".PK1 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                             + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DEC (6,2), "
                             + "CONSTRAINT " + COLLECTION2
                             + ".PKKEY3 PRIMARY KEY (ACCTNBR))");

            s.close ();

            DatabaseMetaData dmd = localConnection_.getMetaData ();
            ResultSet rs = dmd.getPrimaryKeys (null, COLLECTION,
                "PK1");
            boolean success = true;

            int rows = 0;
            while (rs.next ()) {
                ++rows;

                String tableCat       = rs.getString ("TABLE_CAT");
                String tableSchem     = rs.getString ("TABLE_SCHEM");
                String tableName      = rs.getString ("TABLE_NAME");
                String columnName     = rs.getString ("COLUMN_NAME");
                short keySeq          = rs.getShort ("KEY_SEQ");
                String pkName         = rs.getString ("PK_NAME");

                success = success && (tableCat.equals (localConnection_.getCatalog ()));
                success = success && (tableSchem.equals (COLLECTION));
                success = success && (tableName.equals ("PK1"));
                success = success && (columnName.equals ("CUSTID"));
                success = success && (keySeq == 1);
                success = success && (pkName == null);
            }

            rs.close ();
            assertCondition ((rows == 1) && success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        finally {
            try {
                // Variation cleanup.
                Statement s = localConnection_.createStatement ();

                s.executeUpdate ("ALTER TABLE " + COLLECTION
                    + ".PK1 DROP PRIMARY KEY CASCADE");
                s.executeUpdate ("ALTER TABLE " + COLLECTION
                    + ".PK2 DROP PRIMARY KEY CASCADE");
                s.executeUpdate ("ALTER TABLE " + COLLECTION2
                    + ".PK1 DROP PRIMARY KEY CASCADE");

                s.executeUpdate ("DROP TABLE " + COLLECTION
                    + ".PK1");
                s.executeUpdate ("DROP TABLE " + COLLECTION
                    + ".PK2");
                s.executeUpdate ("DROP TABLE " + COLLECTION2
                    + ".PK1");

                s.close ();
            }
            catch (SQLException e) {
                e.printStackTrace (System.out);
            }
        }
    }


/**
DatabaseMetaData.getTables() - Passes in the system name for the
catalog argument. No Exceptions should be thrown.
**/
    public void Var007()
    {
        try {
            // Variation setup.
            Statement s = localConnection_.createStatement ();
            s.executeUpdate ("CREATE TABLE " + COLLECTION
                + ".TABLES (NAME INTEGER)");
            s.executeUpdate ("CREATE TABLE " + COLLECTION
                + ".TABLES1 (NAME INTEGER)");
            s.executeUpdate ("CREATE TABLE " + COLLECTION
                + ".TABLES2 (NAME INTEGER)");
            s.executeUpdate ("CREATE TABLE " + COLLECTION
                + ".TABLESXX (NAME INTEGER)");
            s.executeUpdate ("CREATE TABLE " + COLLECTION2
                + ".TABLES (NAME INTEGER)");
            s.executeUpdate ("CREATE TABLE " + COLLECTION2
                + ".TABLES3 (NAME INTEGER)");
            s.executeUpdate ("CREATE TABLE " + COLLECTION2
                + ".TABLES4 (NAME INTEGER)");
            s.executeUpdate ("CREATE TABLE " + COLLECTIONXX
                + ".TABLESXX (NAME INTEGER)");

            s.executeUpdate ("CREATE VIEW " + COLLECTION
                + ".TABLESV AS SELECT * FROM " + COLLECTION
                + ".TABLES");
            s.executeUpdate ("CREATE VIEW " + COLLECTION2
                + ".TABLESV2 AS SELECT * FROM " + COLLECTION2
                + ".TABLES3");

            // Label the tables.  No label on TABLES, please.
            s.executeUpdate ("LABEL ON TABLE " + COLLECTION
                + ".TABLES1 IS '9'");
            s.executeUpdate ("LABEL ON TABLE " + COLLECTION
                + ".TABLES2 IS 'Ten'");
            s.executeUpdate ("LABEL ON TABLE " + COLLECTION
                + ".TABLESXX IS 'Client Access/400'");
            s.close ();

            DatabaseMetaData dmd = localConnection_.getMetaData ();
            final String[] justTables = { "TABLE" };
            ResultSet rs = dmd.getTables (null, COLLECTION,
                "TABLES%", justTables);
            boolean success = true;

            int rows = 0;
            while (rs.next ()) {
                ++rows;
                success = success && rs.getString ("TABLE_CAT").equals (localConnection_.getCatalog ());
                success = success && rs.getString ("TABLE_SCHEM").equals (COLLECTION);
                success = success && rs.getString ("TABLE_TYPE").equals ("TABLE");

                String table = rs.getString ("TABLE_NAME");
                String remarks = rs.getString ("REMARKS");
                // System.out.println ("Table = " + table + ":");
                // System.out.println ("Remarks = " + remarks + ":");
                if (table.equals ("TABLES"))
                    success = success && remarks.equals ("");
                else if (table.equals ("TABLES1"))
                    success = success && remarks.equals ("9");
                else if (table.equals ("TABLES2"))
                    success = success && remarks.equals ("Ten");
                else if (table.equals ("TABLESXX"))
                    success = success && remarks.equals ("Client Access/400");
            }

            // System.out.println ("Rows = " + rows);

            rs.close ();
            assertCondition ((rows == 4) && success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        finally {
            try {
                // Variation cleanup.
                Statement s = localConnection_.createStatement ();

                s.executeUpdate ("DROP VIEW " + COLLECTION
                    + ".TABLESV");
                s.executeUpdate ("DROP VIEW " + COLLECTION2
                    + ".TABLESV2");

                s.executeUpdate ("DROP TABLE " + COLLECTION
                    + ".TABLES");
                s.executeUpdate ("DROP TABLE " + COLLECTION
                    + ".TABLES1");
                s.executeUpdate ("DROP TABLE " + COLLECTION
                    + ".TABLES2");
                s.executeUpdate ("DROP TABLE " + COLLECTION
                    + ".TABLESXX");
                s.executeUpdate ("DROP TABLE " + COLLECTION2
                    + ".TABLES");
                s.executeUpdate ("DROP TABLE " + COLLECTION2
                    + ".TABLES3");
                s.executeUpdate ("DROP TABLE " + COLLECTION2
                    + ".TABLES4");
                s.executeUpdate ("DROP TABLE " + COLLECTIONXX
                    + ".TABLESXX");

                s.close ();
            }
            catch (SQLException e) {
                e.printStackTrace (System.out);
            }
        }
    }



/**
DatabaseMetaData.getTablePrivileges() - Passes in a valid catalog name.
One row should be returned. No Exceptions should be thrown.
**/
    public void Var008()
    {
        try {
            // Variation setup.
            Statement s = localConnection_.createStatement ();
            s.executeUpdate ("CREATE TABLE " + COLLECTION
                + ".TPRIVS (NAME INTEGER)");
            s.executeUpdate ("CREATE TABLE " + COLLECTION
                + ".TPRIVS1 (NAME INTEGER)");
            s.executeUpdate ("CREATE TABLE " + COLLECTION
                + ".TPRIVS2 (NAME INTEGER)");
            s.executeUpdate ("CREATE TABLE " + COLLECTION
                + ".TPRIVSXX (NAME INTEGER)");
            s.executeUpdate ("CREATE TABLE " + COLLECTION2
                + ".TPRIVS (NAME INTEGER)");
            s.executeUpdate ("CREATE TABLE " + COLLECTION2
                + ".TPRIVS3 (NAME INTEGER)");
            s.executeUpdate ("CREATE TABLE " + COLLECTION2
                + ".TPRIVS4 (NAME INTEGER)");
            s.executeUpdate ("CREATE TABLE " + COLLECTIONXX
                + ".TPRIVSXX (NAME INTEGER)");

            s.close ();

            DatabaseMetaData dmd = localConnection_.getMetaData ();
            String userName = dmd.getUserName ();
            ResultSet rs = dmd.getTablePrivileges (null,
                COLLECTION, "TPRIVS%");
            boolean success = true;

            int rows = 0;
            while (rs.next ()) {
                ++rows;
                success = success && rs.getString ("TABLE_CAT").equals (localConnection_.getCatalog ());
                success = success && rs.getString ("TABLE_SCHEM").equals (COLLECTION);

                success = success && (rs.getString ("TABLE_NAME").length () > 0);
                success = success && (rs.getString ("GRANTOR") == null) && rs.wasNull ();
                success = success && rs.getString ("GRANTEE").equals (userName);

                // It would be nice to try to force this to be
                // different values, but nobody seems to understand how
                // to do it!
                success = success && rs.getString ("PRIVILEGE").equals ("READ ADD DELETE UPDATE");

                success = success && (rs.getString ("IS_GRANTABLE") == null) && rs.wasNull ();
            }

            rs.close ();
            assertCondition ((rows == 4) && success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        finally {
            try {
                // Variation cleanup.
                Statement s = localConnection_.createStatement ();

                s.executeUpdate ("DROP TABLE " + COLLECTION
                    + ".TPRIVS");
                s.executeUpdate ("DROP TABLE " + COLLECTION
                    + ".TPRIVS1");
                s.executeUpdate ("DROP TABLE " + COLLECTION
                    + ".TPRIVS2");
                s.executeUpdate ("DROP TABLE " + COLLECTION
                    + ".TPRIVSXX");
                s.executeUpdate ("DROP TABLE " + COLLECTION2
                    + ".TPRIVS");
                s.executeUpdate ("DROP TABLE " + COLLECTION2
                    + ".TPRIVS3");
                s.executeUpdate ("DROP TABLE " + COLLECTION2
                    + ".TPRIVS4");
                s.executeUpdate ("DROP TABLE " + COLLECTIONXX
                    + ".TPRIVSXX");

                s.close ();
            }
            catch (SQLException e) {
                e.printStackTrace (System.out);
            }
        }
    }


/**
DatabaseMetaData.getVersionColumns() - Passes in a valid catalog name.
One row should be returned. No Exceptions should be thrown.
**/
    public void Var009()
    {
        try {
            // Variation setup.
            Statement s = localConnection_.createStatement ();

            s.executeUpdate ("CREATE TABLE " + COLLECTION
                             + ".VC1 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                             + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DEC (6,2), "
                             + "CONSTRAINT " + COLLECTION
                             + ".VCKEY1 PRIMARY KEY (CUSTID))");

            s.executeUpdate ("CREATE TABLE " + COLLECTION
                             + ".VC2 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                             + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DEC (6,2), "
                             + "CONSTRAINT " + COLLECTION
                             + ".VCKEY2 PRIMARY KEY (NAME))");

            s.executeUpdate ("CREATE TABLE " + COLLECTION2
                             + ".VC1 (CUSTID INT NOT NULL, NAME CHAR(10) NOT NULL, "
                             + "ACCTNBR INT NOT NULL WITH DEFAULT, BALANCE DEC (6,2), "
                             + "CONSTRAINT " + COLLECTION2
                             + ".VCKEY3 PRIMARY KEY (ACCTNBR))");

            s.close ();

            DatabaseMetaData dmd = localConnection_.getMetaData ();
            ResultSet rs = dmd.getVersionColumns (localConnection_.getCatalog (),
                                                  COLLECTION, "VC1");
            boolean success = true;

            int rows = 0;
            while (rs.next ()) {
                ++rows;

                short scope           = rs.getShort ("SCOPE");
                String columnName     = rs.getString ("COLUMN_NAME");
                short dataType        = rs.getShort ("DATA_TYPE");
                String typeName       = rs.getString ("TYPE_NAME");
                int columnSize        = rs.getInt ("COLUMN_SIZE");
                int bufferLength      = rs.getInt ("BUFFER_LENGTH");
                short decimalDigits   = rs.getShort ("DECIMAL_DIGITS");
                short pseudoColumn    = rs.getShort ("PSEUDO_COLUMN");

                // System.out.println (scope + ":" + columnName + ":" + dataType + ":" + typeName + ":");
                // System.out.println (columnSize + ":" + bufferLength + ":" + decimalDigits + ":" + pseudoColumn + ":");

                success = success && (scope == 0);
                success = success && (pseudoColumn == DatabaseMetaData.versionColumnNotPseudo);

                if (columnName.equals ("CUSTID")) {
                    success = success && (dataType == Types.INTEGER);
                    success = success && (typeName.equals ("INTEGER"));
                    success = success && (columnSize == 10);
                    success = success && (bufferLength == 4);
                    success = success && (decimalDigits == 0);
                }
            }

            rs.close ();
            assertCondition ((rows == 1) && success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        finally {
            try {
                // Variation cleanup.
                Statement s = localConnection_.createStatement ();

                s.executeUpdate ("ALTER TABLE " + COLLECTION
                    + ".VC1 DROP PRIMARY KEY CASCADE");
                s.executeUpdate ("ALTER TABLE " + COLLECTION
                    + ".VC2 DROP PRIMARY KEY CASCADE");
                s.executeUpdate ("ALTER TABLE " + COLLECTION2
                    + ".VC1 DROP PRIMARY KEY CASCADE");

                s.executeUpdate ("DROP TABLE " + COLLECTION
                    + ".VC1");
                s.executeUpdate ("DROP TABLE " + COLLECTION
                    + ".VC2");
                s.executeUpdate ("DROP TABLE " + COLLECTION2
                    + ".VC1");

                s.close ();
            }
            catch (SQLException e) {
                e.printStackTrace (System.out);
            }
        }
    }



/**
Statement.executeUpdate() and executeQuery() - Passes in a
SELECT statement when we create the PreparedStatement and then tries
to execute it using executeUpdate().  No SQLException should be thrown.
**/
    public void Var010()
    {
        try {
            // Variation setup.
            Statement s = localConnection_.createStatement ();
            final String table = COLLECTION + ".NLSEX";
            s.executeUpdate ("CREATE TABLE " + table
                             + " (NAME VARCHAR(50), ID INT, SCORE INT)");
            s.executeUpdate ("INSERT INTO " + table
                + " (NAME, ID) VALUES ('" + jdbc_dbcs_string20b + "', 15)");
            ResultSet rs = s.executeQuery ("SELECT * "
                + " FROM " + table + " WHERE NAME = '" + jdbc_dbcs_string20b + "'");
            rs.next ();
            String str = rs.getString (1);
            rs.close ();

            // Variation cleanup.
            s.executeUpdate ("DROP TABLE " + table);
            s.close ();

            assertCondition (str.equals (jdbc_dbcs_string20b));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
PreparedStatement.setAsciiString() and ResultSet.getAsciiStream() - Passes in a
SELECT statement when we create the PreparedStatement and then tries
to execute it using executeUpdate().  No SQLException should be thrown.
**/
    public void Var011()
    {
        try {
            // Variation setup.
            Statement s = localConnection_.createStatement ();
            final String table = COLLECTION + ".NLSEX";
            s.executeUpdate ("CREATE TABLE " + table
                + " (NAME VARCHAR(50), ID INT, SCORE INT)");
            PreparedStatement ps = localConnection_.prepareStatement ("INSERT INTO " + table
                + " (NAME, ID) VALUES (?, 15)");
            InputStream w = new ByteArrayInputStream (jdbc_dbcs_string20a.getBytes ("ISO8859_1"));
            ps.setAsciiStream (1, w, w.available ());
            ps.executeUpdate ();
            ps.close ();

            PreparedStatement ps2 = localConnection_.prepareStatement ("SELECT * "
                + " FROM " + table + " WHERE NAME = ?");
            w = new ByteArrayInputStream (jdbc_dbcs_string20a.getBytes ("ISO8859_1"));
            ps2.setAsciiStream (1, w, w.available ());
            ResultSet rs = ps2.executeQuery ();
            rs.next ();
            InputStream v = rs.getAsciiStream (1);
            rs.close ();
            ps2.close ();

            // Variation cleanup.
            s.executeUpdate ("DROP TABLE " + table);
            s.close ();
	    sb.setLength(0); 
            assertCondition (compare (v, jdbc_dbcs_string20a, "8859_1",sb),sb);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
PreparedStatement.setCharacterStream() and ResultSet.getCharacterStream() - Passes in a
SELECT statement when we create the PreparedStatement and then tries
to execute it using executeUpdate().  No SQLException should be thrown.
**/
    public void Var012()
    {
        try {
            // Variation setup.
            Statement s = localConnection_.createStatement ();
            final String table = COLLECTION + ".NLSEX";
            s.executeUpdate ("CREATE TABLE " + table
                + " (NAME VARCHAR(50), ID INT, SCORE INT)");
            PreparedStatement ps = localConnection_.prepareStatement ("INSERT INTO " + table
                + " (NAME, ID) VALUES (?, 15)");
            Reader w = new StringReader (jdbc_dbcs_string20b);
            ps.setCharacterStream (1, w, jdbc_dbcs_string20b.length ());
            ps.executeUpdate ();
            ps.close ();

            PreparedStatement ps2 = localConnection_.prepareStatement ("SELECT * "
                + " FROM " + table + " WHERE NAME = ?");
            w = new StringReader (jdbc_dbcs_string20b);
            ps2.setCharacterStream (1, w, jdbc_dbcs_string20b.length ());
            ResultSet rs = ps2.executeQuery ();
            rs.next ();
            Reader v = rs.getCharacterStream (1);
            rs.close ();
            ps2.close ();

            // Variation cleanup.
            s.executeUpdate ("DROP TABLE " + table);
            s.close ();
	    sb.setLength(0); 

            assertCondition (compare (v, jdbc_dbcs_string20b,sb),sb);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
PreparedStatement.setClob() and ResultSet.getClob() - Passes in a
SELECT statement when we create the PreparedStatement and then tries
to execute it using executeUpdate().  No SQLException should be thrown.
**/
    public void Var013()
    {
        try {
            // Variation setup.
            Statement s = localConnection_.createStatement ();
            final String table = COLLECTION + ".NLSEX";
            s.executeUpdate ("CREATE TABLE " + table
                + " (NAME VARCHAR(50), ID INT, SCORE INT)");
            PreparedStatement ps = localConnection_.prepareStatement ("INSERT INTO " + table
                + " (NAME, ID) VALUES (?, 15)");
            Clob w = new JDLobTest.JDTestClob (jdbc_dbcs_string20a);
            ps.setClob (1, w);
            ps.executeUpdate ();
            ps.close ();

            PreparedStatement ps2 = localConnection_.prepareStatement ("SELECT * "
                + " FROM " + table + " WHERE NAME = ?");
            ps2.setClob (1, w);
            ResultSet rs = ps2.executeQuery ();
            rs.next ();
            Clob v = rs.getClob (1);
            rs.close ();
            ps2.close ();

            // Variation cleanup.
            s.executeUpdate ("DROP TABLE " + table);
            s.close ();

            assertCondition (v.getSubString (1, (int) v.length ()).equals (jdbc_dbcs_string20a));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
PreparedStatement.setObject() and ResultSet.getObject() - Passes in a
SELECT statement when we create the PreparedStatement and then tries
to execute it using executeUpdate().  No SQLException should be thrown.
**/
    public void Var014()
    {
        try {
            // Variation setup.
            Statement s = localConnection_.createStatement ();
            final String table = COLLECTION + ".NLSEX";
            s.executeUpdate ("CREATE TABLE " + table
                + " (NAME VARCHAR(50), ID INT, SCORE INT)");
            PreparedStatement ps = localConnection_.prepareStatement ("INSERT INTO " + table
                + " (NAME, ID) VALUES (?, 15)");
            ps.setObject (1, jdbc_dbcs_string20b);
            ps.executeUpdate ();
            ps.close ();

            PreparedStatement ps2 = localConnection_.prepareStatement ("SELECT * "
                + " FROM " + table + " WHERE NAME = ?");
            ps2.setObject (1, jdbc_dbcs_string20b);
            ResultSet rs = ps2.executeQuery ();
            rs.next ();
            String v = (String) rs.getObject (1);
            rs.close ();
            ps2.close ();

            // Variation cleanup.
            s.executeUpdate ("DROP TABLE " + table);
            s.close ();

            assertCondition (v.equals (jdbc_dbcs_string20b));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
PreparedStatement.setString() and ResultSet.getString() - Passes in a
SELECT statement when we create the PreparedStatement and then tries
to execute it using executeUpdate().  No SQLException should be thrown.
**/
    public void Var015()
    {
        try {
            // Variation setup.
            Statement s = localConnection_.createStatement ();
            final String table = COLLECTION + ".NLSEX";
            s.executeUpdate ("CREATE TABLE " + table
                + " (NAME VARCHAR(50), ID INT, SCORE INT)");
            PreparedStatement ps = localConnection_.prepareStatement ("INSERT INTO " + table
                + " (NAME, ID) VALUES (?, 15)");
            ps.setString (1, jdbc_dbcs_string20b);
            ps.executeUpdate ();
            ps.close ();

            PreparedStatement ps2 = localConnection_.prepareStatement ("SELECT * "
                + " FROM " + table + " WHERE NAME = ?");
            ps2.setString (1, jdbc_dbcs_string20b);
            ResultSet rs = ps2.executeQuery ();
            rs.next ();
            String v = rs.getString (1);
            rs.close ();
            ps2.close ();

            // Variation cleanup.
            s.executeUpdate ("DROP TABLE " + table);

            assertCondition (v.equals (jdbc_dbcs_string20b));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
PrepardStatement.setString() and ResultSet.getString() -
Set values in a table that has columns with different CCSIDs.
**/
    public void Var016()
    {
        try {
            // Variation setup.
            Statement s = localConnection_.createStatement ();
            final String table = COLLECTION + ".CCSIDFUN";
            s.executeUpdate ("CREATE TABLE " + table
                + "(COL1 VARCHAR(20) CCSID 37, "
                + " COL2 VARCHAR(20) CCSID 5026, "
                + " COL3 VARCHAR(20) CCSID 937)");
            PreparedStatement ps = localConnection_.prepareStatement ("INSERT INTO " + table
                + " (COL1, COL2, COL3) VALUES (?, ?, ?)");
            ps.setString (1, jdbc_dbcs_string20a);
            ps.setString (2, jdbc_dbcs_string20a);
            ps.setString (3, jdbc_dbcs_string20a);
            ps.executeUpdate ();
            ps.setString (1, jdbc_dbcs_string20b);
            ps.setString (2, jdbc_dbcs_string20b);
            ps.setString (3, jdbc_dbcs_string20b);
            ps.executeUpdate ();
            ps.close ();

            PreparedStatement ps2 = localConnection_.prepareStatement ("SELECT * "
                + " FROM " + table + " WHERE COL1 = ?");

            ps2.setString (1, jdbc_dbcs_string20a);
            ResultSet rs = ps2.executeQuery ();
            rs.next ();
            String v11 = rs.getString (1);
            String v12 = rs.getString (2);
            String v13 = rs.getString (3);
            rs.close ();

            ps2.setString (1, jdbc_dbcs_string20b);
            rs = ps2.executeQuery ();
            rs.next ();
            String v21 = rs.getString (1);
            String v22 = rs.getString (2);
            String v23 = rs.getString (3);
            rs.close ();

            ps2.close ();

            // Variation cleanup.
            s.executeUpdate ("DROP TABLE " + table);
            s.close ();

            assertCondition (v11.equals (jdbc_dbcs_string20a)
                    && v12.equals (jdbc_dbcs_string20a)
                    && v13.equals (jdbc_dbcs_string20a)
                    && v21.equals (jdbc_dbcs_string20b)
                    && v22.equals (jdbc_dbcs_string20b)
                    && v23.equals (jdbc_dbcs_string20b));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
PreparedStatement.setUnicodeString() and ResultSet.getUnicodeString() - Passes in a
SELECT statement when we create the PreparedStatement and then tries
to execute it using executeUpdate().  No SQLException should be thrown.
**/
    @SuppressWarnings("deprecation")
    public void Var017()
    {
        try {
            // Variation setup.
            Statement s = localConnection_.createStatement ();
            final String table = COLLECTION + ".NLSEX";
            s.executeUpdate ("CREATE TABLE " + table
                + " (NAME VARCHAR(50), ID INT, SCORE INT)");
            PreparedStatement ps = localConnection_.prepareStatement ("INSERT INTO " + table
                + " (NAME, ID) VALUES (?, 15)");
            InputStream w = new ByteArrayInputStream (jdbc_dbcs_string20a.getBytes ("Unicode"));
            ps.setUnicodeStream (1, w, w.available ());
            ps.executeUpdate ();
            ps.close ();

            PreparedStatement ps2 = localConnection_.prepareStatement ("SELECT * "
                + " FROM " + table + " WHERE NAME = ?");
            w = new ByteArrayInputStream (jdbc_dbcs_string20a.getBytes ("Unicode"));
            ps2.setUnicodeStream (1, w, w.available ());
            ResultSet rs = ps2.executeQuery ();
            rs.next ();
            InputStream v = rs.getUnicodeStream (1);
            rs.close ();
            ps2.close ();

            // Variation cleanup.
            s.executeUpdate ("DROP TABLE " + table);
            s.close ();
	    sb.setLength(0); 

            assertCondition (compare (v, jdbc_dbcs_string20a, "UTF-16BE",sb),sb);  //@F1C Change from "Unicode" to "UTF-16BE"
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
CallableStatement.setObject() and getObject() - Set and get an
INOUT parameter.
**/
    public void Var018()
    {
        try {
            // Variation cleanup.
            JDSetupProcedure.create (systemObject_, localConnection_, 
                JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_); // @D0C

            CallableStatement csTypes = JDSetupProcedure.prepare (localConnection_,
                JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_); // @D0C
            JDSetupProcedure.setTypesParameters (csTypes, JDSetupProcedure.STP_CSTYPESINOUT,
                                                 supportedFeatures_); // @D0C
            JDSetupProcedure.register (csTypes, JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_, getDriver());
            csTypes.setObject (12, jdbc_dbcs_string20a);
            csTypes.execute ();
            String p = (String) csTypes.getObject (12);
            csTypes.close ();
            assertCondition (p.equals (jdbc_dbcs_string20a + "JDBC"));
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
ResultSet.updateAsciiStream() and ResultSet.getAsciiStream() - Passes in a
SELECT statement when we create the PreparedStatement and then tries
to execute it using executeUpdate().  No SQLException should be thrown.
**/
    public void Var019()
    {
        try {
            // Variation setup.
            Statement s = localConnection_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
                                                       ResultSet.CONCUR_UPDATABLE);
            final String table = COLLECTION + ".NLSEX";
            s.executeUpdate ("CREATE TABLE " + table
                + " (NAME VARCHAR(50), ID INT, SCORE INT)");
            s.executeUpdate ("INSERT INTO " + table
                + " (NAME, ID) VALUES (NULL, 15)");
            ResultSet rs = s.executeQuery ("SELECT * FROM " + table + " FOR UPDATE");
            rs.next ();
            InputStream w = new ByteArrayInputStream (jdbc_dbcs_string20b.getBytes ("ISO8859_1"));
            rs.updateAsciiStream (1, w, w.available ());
            rs.updateRow ();
            rs.close ();

            rs = s.executeQuery ("SELECT * FROM " + table);
            rs.next ();
            InputStream v = rs.getAsciiStream (1);
            rs.close ();

            // Variation cleanup.
            s.executeUpdate ("DROP TABLE " + table);
            s.close ();
	    sb.setLength(0); 

            assertCondition (compare (v, jdbc_dbcs_string20b, "8859_1",sb),sb);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
ResultSet.updateCharacterStream() and ResultSet.getCharacterStream() - Passes in a
SELECT statement when we create the PreparedStatement and then tries
to execute it using executeUpdate().  No SQLException should be thrown.
**/
    public void Var020()
    {
        try {
            // Variation setup.
            Statement s = localConnection_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
                                                       ResultSet.CONCUR_UPDATABLE);
            final String table = COLLECTION + ".NLSEX";
            s.executeUpdate ("CREATE TABLE " + table
                + " (NAME VARCHAR(50), ID INT, SCORE INT)");
            s.executeUpdate ("INSERT INTO " + table
                + " (NAME, ID) VALUES (NULL, 15)");
            ResultSet rs = s.executeQuery ("SELECT * FROM " + table + " FOR UPDATE");
            rs.next ();
            Reader w = new StringReader (jdbc_dbcs_string20a);
            rs.updateCharacterStream (1, w, jdbc_dbcs_string20a.length ());
            rs.updateRow ();
            rs.close ();

            rs = s.executeQuery ("SELECT * FROM " + table);
            rs.next ();
            Reader v = rs.getCharacterStream (1);
            rs.close ();

            // Variation cleanup.
            s.executeUpdate ("DROP TABLE " + table);
            s.close ();
	    sb.setLength(0); 

            assertCondition (compare (v, jdbc_dbcs_string20a,sb),sb);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
ResultSet.updateObject() and ResultSet.getObject() - Passes in a
SELECT statement when we create the PreparedStatement and then tries
to execute it using executeUpdate().  No SQLException should be thrown.
**/
    public void Var021()
    {
        try {
            // Variation setup.
            Statement s = localConnection_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
                                                       ResultSet.CONCUR_UPDATABLE);
            final String table = COLLECTION + ".NLSEX";
            s.executeUpdate ("CREATE TABLE " + table
                + " (NAME VARCHAR(50), ID INT, SCORE INT)");
            s.executeUpdate ("INSERT INTO " + table
                + " (NAME, ID) VALUES (NULL, 15)");
            ResultSet rs = s.executeQuery ("SELECT * FROM " + table + " FOR UPDATE");
            rs.next ();
            rs.updateObject (1, jdbc_dbcs_string20a);
            rs.updateRow ();
            rs.close ();

            rs = s.executeQuery ("SELECT * FROM " + table);
            rs.next ();
            String v = (String) rs.getObject (1);
            rs.close ();

            // Variation cleanup.
            s.executeUpdate ("DROP TABLE " + table);
            s.close ();

            assertCondition (v.equals (jdbc_dbcs_string20a));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
ResultSet.updateString() and ResultSet.getString() - Passes in a
SELECT statement when we create the PreparedStatement and then tries
to execute it using executeUpdate().  No SQLException should be thrown.
**/
    public void Var022()
    {
        try {
            // Variation setup.
            Statement s = localConnection_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
                                                       ResultSet.CONCUR_UPDATABLE);
            final String table = COLLECTION + ".NLSEX";
            s.executeUpdate ("CREATE TABLE " + table
                + " (NAME VARCHAR(50), ID INT, SCORE INT)");
            s.executeUpdate ("INSERT INTO " + table
                + " (NAME, ID) VALUES (NULL, 15)");
            ResultSet rs = s.executeQuery ("SELECT * FROM " + table + " FOR UPDATE");
            rs.next ();
            rs.updateString (1, jdbc_dbcs_string20b);
            rs.updateRow ();
            rs.close ();

            rs = s.executeQuery ("SELECT * FROM " + table);
            rs.next ();
            String v = rs.getString (1);
            rs.close ();

            // Variation cleanup.
            s.executeUpdate ("DROP TABLE " + table);
            s.close ();

            assertCondition (v.equals (jdbc_dbcs_string20b));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
ResultSetMetaData.getColumnName() - Check column 2, with more
than 1 character in the name.
**/
    public void Var023()
    {
        try {
            // Variation setup.
            Statement s = localConnection_.createStatement ();
            final String table = COLLECTION + ".NLSRSMD";
            s.executeUpdate ("CREATE TABLE " + table
                + " (C INTEGER, COL2 INTEGER)");

            ResultSet rs = s.executeQuery ("SELECT * FROM " + table);
            ResultSetMetaData rsmd = rs.getMetaData ();
            String columnName = rsmd.getColumnName (2);
            rs.close ();

            // Variation cleanup.
            s.executeUpdate ("DROP TABLE " + table);
            s.close ();

            assertCondition (columnName.equals ("COL2"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    Access DBCS data in a DDM file.  Test read, write, update, and delete with
    that DBCS data.  See also NLSDDMTestcase, variation 34.
    **/
    public void Var024()
    {
        Connection connection = null;
        StringBuffer failMsg = new StringBuffer();
        // The sign-on names look like JAVA??? where ??? is JAP, KOR, TCH, or SCH
        // for the double byte languages.
        String specificName = systemObject_.getUserId().substring(4);
        if (!specificName.equals("JAP") &&
        !specificName.equals("KOR") &&
        !specificName.equals("TCH") &&
        !specificName.equals("SCH")) {
            notApplicable();
            output_.println("  Var40 was not attempted: not using DBCS signon: "+systemObject_.getUserId());
            return;
        }

        try {
            // initialize private data
            String url = "jdbc:as400://" + systemObject_.getSystemName();
            url += "/JAVANLS";

            Properties properties = new Properties();
            properties.put("user", systemObject_.getUserId());
            properties.put("password", PasswordVault.decryptPasswordLeak(encryptedPassword_));

            properties.put("libraries", COLLECTION + " JAVANLS *LIBL");
            properties.put("errors","full");
            connection = DriverManager.getConnection(url, properties);
            //DatabaseMetaData dbMeta = connection.getMetaData();

            Statement stmt = connection.createStatement();

            // String catalog = null;
            String fieldList = "";
            // String schema = "JAVANLS";
            String table = "SMPDBCS"+specificName;

            // Get the "real" strings from the property file.
            String[][] propfile_recs = new String[2][4];
            propfile_recs [0][0] = getResource(specificName+"OPEN_1");
            propfile_recs [0][1] = getResource(specificName+"GRAPH_1");
            propfile_recs [0][2] = getResource(specificName+"ONLY_1");
            propfile_recs [0][3] = getResource(specificName+"EITHER_1");
            propfile_recs [1][0] = getResource(specificName+"OPEN_2");
            propfile_recs [1][1] = getResource(specificName+"GRAPH_2");
            propfile_recs [1][2] = getResource(specificName+"ONLY_2");
            propfile_recs [1][3] = getResource(specificName+"EITHER_2");

            // Assign correct field names, depending on DBCS language being tested.
            if (specificName.equals("JAP"))
                fieldList = "DBCSOPENJ,DBCSGRAPHJ,DBCSONLYJ,DBCSEITHEJ";
            else
                fieldList = specificName+"OPEN,"+specificName+"GRAPH,"+specificName+"ONLY,"+specificName+"EITHER";

//      ResultSet rsNLS = dbMeta.getColumns(catalog, schema, table, null);
// Can't use getColumns on a DDM file since it wasn't created with SQL (and not catalogued).

            // Test reading DBCS records from a DDM file using JDBC getString
            System.out.println("Reading records (w/ getString) from SMPDBCSxxx in JAVANLS ...");
            ResultSet rsNLS = stmt.executeQuery("SELECT "+fieldList+" FROM "+table);
            String[][] recs = new String[2][4];
            int count = 0;
            while (rsNLS.next()) {
                for (int i=0; i<4; i++) {
                    recs[count][i] = rsNLS.getString(i+1);

                    //System.out.println("string got = ." + recs[count][i] + ".");
                    //System.out.println("profile string = ." + propfile_recs[count][i] + ".");

                    if (!recs[count][i].equals(propfile_recs[count][i])) {
                        failMsg.append("  Mismatched strings from getString: \n");
                        failMsg.append("    Original: "+propfile_recs[count][i]+".  Unicode: "+NLSTest.stringToUnicode(propfile_recs[count][i])+"\n");
                        failMsg.append("    Returned: "+recs[count][i]+".  Unicode: "+NLSTest.stringToUnicode(recs[count][i])+"\n");
                    }
                }
                output_.print("\n");
                count++;
            }
            rsNLS.close();

            // Test reading DBCS records from a DDM file using JDBC getUnicodeStream
/* getUnicodeStream() has been deprecated in JDK 1.2.   -JPL
      System.out.println("Reading records (w/ getUnicodeStream) from SMPDBCSxxx in JAVANLS ...");
      rsNLS = stmt.executeQuery("SELECT "+fieldList+" FROM "+table);
      InputStream[][] recUS = new InputStream[2][4];
      byte[] dataread = new byte[40];
      byte[] result;
      count = 0;
      while (rsNLS.next())
      {
        for (int i=0; i<4; i++)
        {
          recUS[count][i] = rsNLS.getUnicodeStream(i+1);
          int numOfBytes = 0;
          for (int j=0; (recUS[count][i].read(dataread, j, 1)) != -1; j++, numOfBytes++);
          result = new byte[numOfBytes];
          System.arraycopy(dataread, 0, result, 0, numOfBytes);
          String temp = new String(result, "Unicode");
          //output_.println("\ntemp.length() = " + temp.length());
          //System.out.println("temp = ."+temp+".");
          if (!temp.equals(propfile_recs[count][i]))
          {
            failMsg.append("  Mismatched strings from getUnicodeStream: \n");
            failMsg.append("    Original: "+propfile_recs[count][i]+".  Unicode: "+NLSTest.stringToUnicode(propfile_recs[count][i])+"\n");
            failMsg.append("    Returned: "+temp+".  Unicode: "+NLSTest.stringToUnicode(temp)+"\n");
          }
        }
        output_.print("\n");
        count++;
      }
      rsNLS.close();
*/

            // Test writing DBCS records to the DDM file using JDBC
            System.out.println("Writing records just read back to SMPDBCSxxx in JAVANLS ...");
            for (int i=0; i<2; i++) {
                String ins = "INSERT INTO "+table+"("+fieldList+") VALUES(";
                ins += "'"+recs[i][0]+"',";
                ins += "G'"+recs[i][1]+"',";
                ins += "'"+recs[i][2]+"',";
                ins += "'"+recs[i][3].substring(0, recs[i][3].length()-7)+"',";
                ins = ins.substring(0,ins.length()-1);
                ins += ")";
                //System.out.println("Insert statement = " + ins);
                stmt.executeUpdate(ins);
            }

            // Verify written records by reading them back using getString
            System.out.println("Verifying records written to SMPDBCSxxx in JAVANLS ...");
            rsNLS = stmt.executeQuery("SELECT "+fieldList+" FROM "+table);
            String[][] recs2 = new String[4][4];
            count = 0;
            recs[0][3] = (recs[0][3].substring(0, recs[0][3].length()-7))+"       ";
            recs[1][3] = (recs[1][3].substring(0, recs[1][3].length()-7))+"       ";
            while (rsNLS.next()) {
                for (int i=0; i<4; i++) {
                    recs2[count][i] = rsNLS.getString(i+1);
                    if (count > 1) {
                        if (!recs2[count][i].equals(recs[count-2][i])) {
                            failMsg.append("Verify write found mismatched records: "+count+","+i+": "+recs[count-2][i]+" != "+recs2[count][i]+".\n");
                        }
                    }
                }
                count++;
            }
            rsNLS.close();

            // Update each field in the last record written.
            System.out.println("Updating the last record ...");
            {
                // Assign correct field names, depending on DBCS language being tested.
                String upd = new String();
                if (specificName.equals("JAP")) {
                    upd = "UPDATE "+table+" SET ";
                    upd += "DBCSOPENJ='"+recs[0][0]+"',";
                    upd += "DBCSGRAPHJ=G'"+recs[0][1]+"',";
                    upd += "DBCSONLYJ='"+recs[0][2]+"',";
                    upd += "DBCSEITHEJ='"+recs[0][3]+"' ";
                    upd += "WHERE (DBCSEITHEJ='"+recs[0][3]+"' ";
                    upd += "AND DBCSOPENJ='"+recs[1][0]+"')";
                } else {
                    upd = "UPDATE "+table+" SET ";
                    upd += specificName+"OPEN='"+recs[0][0]+"',";
                    upd += specificName+"GRAPH=G'"+recs[0][1]+"',";
                    upd += specificName+"ONLY='"+recs[0][2]+"',";
                    upd += specificName+"EITHER='"+recs[0][3]+"' ";
                    upd += "WHERE ("+specificName+"EITHER='"+recs[0][3]+"' ";
                    upd += "AND "+specificName+"OPEN='"+recs[1][0]+"')";
                }
                //System.out.println("Update statement = " + upd);
                stmt.executeUpdate(upd);
            }

            // Verify updated record by reading it back using getString
            System.out.println("Verifying last record was updated in SMPDBCSxxx in JAVANLS ...");
            rsNLS = stmt.executeQuery("SELECT "+fieldList+" FROM "+table);
            count = 0;
            while (rsNLS.next()) {
                for (int i=0; i<4; i++) {
                    recs2[count][i] = rsNLS.getString(i+1);
                    if (count > 1) {
                        if (!recs2[count][i].equals(recs[0][i])) {
                            failMsg.append("Verify update found mismatched records: "+count+","+i+": "+recs[0][i]+" != "+recs2[count][i]+".\n");
                        }
                    }
                }
                count++;
            }
            rsNLS.close();

            // Delete the last 2 records written to put file back to original state.
            System.out.println("Deleting 2 records to restore file to original state ...");
            try {
                if (specificName.equals("JAP"))
                    stmt.executeUpdate("DELETE FROM "+table+" WHERE DBCSEITHEJ='"+recs[0][3]+"'");
                else
                    stmt.executeUpdate("DELETE FROM "+table+" WHERE "+specificName+"EITHER='"+recs[0][3]+"'");
            } catch (Exception e) {
                System.out.println(e+"\n\nCleanup failed.  Setup will put file back to original state on next run"+
                " as long as 'noclean' option is NOT used.");
            }

            if (failMsg.length() != 0)
                failed(failMsg.toString());
            else
                succeeded();
        } catch (Exception e) {
            failed(e, "Unexpected Exception");
        }
        try {
            connection.close();
        } catch (Exception e) {
            output_.println("Connection close failed.");
        }
    }

    /**
     Set values in a table whose DBCS field uses 13488 (Unicode) for the CCSID.
     This should perform better than taking the default language CCSID because
     we don't have to do any conversions along the way.
     **/
    public void Var025()
    {
        Connection connection = null;
        String qualifiedTable = null;
        StringBuffer failMsg = new StringBuffer();
        // The sign-on names look like JAVA??? where ??? is JAP, KOR, TCH, or SCH
        // for the double byte languages.
        String specificName = systemObject_.getUserId().substring(4);
        if (!specificName.equals("JAP") &&
        !specificName.equals("KOR") &&
        !specificName.equals("TCH") &&
        !specificName.equals("SCH")) {
            notApplicable();
            output_.println("  Var41 was not attempted: not using DBCS signon: "+systemObject_.getUserId());
            return;
        }

        try {
            // initialize private data
            String url = "jdbc:as400://" + systemObject_.getSystemName();
            qualifiedTable = COLLECTION + ".UCSTEST";
            Properties properties = new Properties();
            properties.put("user", systemObject_.getUserId());
            properties.put("password", PasswordVault.decryptPasswordLeak(encryptedPassword_));

            connection = DriverManager.getConnection(url, properties);

            // begin createTables()

            Statement stmt = connection.createStatement();

            try {
                stmt.executeUpdate( "DROP TABLE " + qualifiedTable );
            } catch ( Exception e ) {

            }


            stmt.executeUpdate( "CREATE TABLE " + qualifiedTable + " (" +
            "UNICODECOL GRAPHIC (16) CCSID 13488)"  );

            // end createTables()

        } catch (Exception e) {
            output_.println("Setup failed.");
            e.printStackTrace(output_);
        }

        try {
            Statement statement = connection.createStatement ();
            String ins = "INSERT INTO "+qualifiedTable+" (UNICODECOL) VALUES (G'"+jdbc_dbcs_words[17]+"')";
            //System.out.println("first stmt = " + ins + " .\n");
            statement.executeUpdate (ins);

            String ins2 = "INSERT INTO "+qualifiedTable+" (UNICODECOL) VALUES (G'"+jdbc_dbcs_words[18]+"')";
            //System.out.println("second stmt = " + ins2 + " .\n");
            statement.executeUpdate (ins2);

            //System.out.println("Look at file JDBCTEST/UCSTEST; press ENTER to proceed.\n");
            //System.in.read();

            ResultSet rs = statement.executeQuery ("SELECT * FROM " + COLLECTION + ".UCSTEST");

            rs.next ();
            String ucsread = rs.getString(1);
            //System.out.println("jdbc_dbcs_words[17] = ." + jdbc_dbcs_words[17] + ".");
            //System.out.println("ucsread = ."+ucsread+".\n");
            //System.in.read();
            if (!ucsread.equals(jdbc_dbcs_words[17] + "        ")) {
                failMsg.append("Read found mismatched records: "+ucsread+" != "+jdbc_dbcs_words[17]+".\n");
            }

            rs.next ();
            ucsread = rs.getString(1);
            //System.out.println("jdbc_dbcs_words[18] = ." + jdbc_dbcs_words[18] + ".");
            //System.out.println("ucsread = ."+ucsread+".\n");
            //System.in.read();
            if (!ucsread.equals(jdbc_dbcs_words[18] + "        ")) {
                failMsg.append("Read found mismatched records: "+ucsread+" != "+jdbc_dbcs_words[18]+".\n");
            }
            statement.close ();
            if (failMsg.length() != 0)
                failed(failMsg.toString());
            else
                succeeded();
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }

        // Clean up
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate( "DROP TABLE " + qualifiedTable );
        } catch (Exception e) {
            output_.println("Cleanup failed.");
            e.printStackTrace(output_);
        }
    }


    /**
     Set values in a table whose DBCS field uses 13488 (Unicode) for the CCSID.
     This should perform better than taking the default language CCSID because
     we don't have to do any conversions along the way.
     **/
    public void Var026()
    {
        Connection connection = null;
        String qualifiedTable = null;
        StringBuffer failMsg = new StringBuffer();
        // The sign-on names look like JAVA??? where ??? is JAP, KOR, TCH, or SCH
        // for the double byte languages.
        String specificName = systemObject_.getUserId().substring(4);
        if (!specificName.equals("JAP") &&
        !specificName.equals("KOR") &&
        !specificName.equals("TCH") &&
        !specificName.equals("SCH")) {
            notApplicable();
            output_.println("  Var41 was not attempted: not using DBCS signon: "+systemObject_.getUserId());
            return;
        }

        try {
            // initialize private data
            String url = "jdbc:as400://" + systemObject_.getSystemName();
            qualifiedTable = COLLECTION + ".UCSTEST";
            Properties properties = new Properties();
            properties.put("user", systemObject_.getUserId());
            properties.put("password", PasswordVault.decryptPasswordLeak(encryptedPassword_));

            connection = DriverManager.getConnection(url, properties);

            // begin createTables()

            Statement stmt = connection.createStatement();

            try {
                stmt.executeUpdate( "DROP TABLE " + qualifiedTable );
            } catch ( Exception e ) {

            }


            stmt.executeUpdate( "CREATE TABLE " + qualifiedTable + " (" +
            "UNICODECOL VARGRAPHIC (16) CCSID 13488)"  );

            // end createTables()

        }

        catch (Exception e) {
            output_.println("Setup failed.");
            e.printStackTrace(output_);
        }

        try {
            Statement statement = connection.createStatement ();
            String ins = "INSERT INTO "+qualifiedTable+" (UNICODECOL) VALUES (G'"+jdbc_dbcs_words[17]+"')";
            //System.out.println("first stmt = " + ins + " .\n");
            statement.executeUpdate (ins);

            String ins2 = "INSERT INTO "+qualifiedTable+" (UNICODECOL) VALUES (G'"+jdbc_dbcs_words[18]+"')";
            //System.out.println("second stmt = " + ins2 + " .\n");
            statement.executeUpdate (ins2);

            //System.out.println("Look at file JDBCTEST/UCSTEST; press ENTER to proceed.\n");
            //System.in.read();

            ResultSet rs = statement.executeQuery ("SELECT * FROM " + COLLECTION + ".UCSTEST");

            rs.next ();
            String ucsread = rs.getString(1);
            //System.out.println("jdbc_dbcs_words[17] = ." + jdbc_dbcs_words[17] + ".");
            //System.out.println("ucsread = ."+ucsread+".\n");
            //System.in.read();
            if (!ucsread.equals(jdbc_dbcs_words[17])) {
                failMsg.append("Read found mismatched records: "+ucsread+" != "+jdbc_dbcs_words[17]+".\n");
            }

            rs.next ();
            ucsread = rs.getString(1);
            //System.out.println("jdbc_dbcs_words[18] = ." + jdbc_dbcs_words[18] + ".");
            //System.out.println("ucsread = ."+ucsread+".\n");
            //System.in.read();
            if (!ucsread.equals(jdbc_dbcs_words[18])) {
                failMsg.append("Read found mismatched records: "+ucsread+" != "+jdbc_dbcs_words[18]+".\n");
            }
            statement.close ();
            if (failMsg.length() != 0)
                failed(failMsg.toString());
            else
                succeeded();
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }


        // Clean up
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate( "DROP TABLE " + qualifiedTable );
        } catch (Exception e) {
            output_.println("Cleanup failed.");
            e.printStackTrace(output_);
        }
    }


    /**
     Set values in a table whose DBCS field is VARGRAPHIC and uses 5026 for the CCSID.
    **/
    public void Var027()
    {
        Connection connection = null;
        String qualifiedTable = null;
        StringBuffer failMsg = new StringBuffer();
        // The sign-on names look like JAVA??? where ??? is JAP, KOR, TCH, or SCH
        // for the double byte languages.
        String specificName = systemObject_.getUserId().substring(4);
        if (!specificName.equals("JAP") &&
        !specificName.equals("KOR") &&
        !specificName.equals("TCH") &&
        !specificName.equals("SCH")) {
            notApplicable();
            output_.println("  Var41 was not attempted: not using DBCS signon: "+systemObject_.getUserId());
            return;
        }

        try {
            // initialize private data
            String url = "jdbc:as400://" + systemObject_.getSystemName();
            qualifiedTable = COLLECTION + ".VARGTEST";
            Properties properties = new Properties();
            properties.put("user", systemObject_.getUserId());
            properties.put("password", PasswordVault.decryptPasswordLeak(encryptedPassword_));

            connection = DriverManager.getConnection(url, properties);

            // begin createTables()

            Statement stmt = connection.createStatement();

            try {
                stmt.executeUpdate( "DROP TABLE " + qualifiedTable );
            } catch ( Exception e ) {

            }


            stmt.executeUpdate( "CREATE TABLE " + qualifiedTable + " (" +
            "VARGCOL VARGRAPHIC (16) CCSID 835)"  );

            // end createTables()

        } catch (Exception e) {
            output_.println("Setup failed.");
            e.printStackTrace(output_);
        }

        try {
            Statement statement = connection.createStatement ();
            String ins = "INSERT INTO "+qualifiedTable+" (VARGCOL) VALUES (G'"+jdbc_dbcs_words[17]+"')";
            //System.out.println("first stmt = " + ins + " .\n");
            statement.executeUpdate (ins);

            String ins2 = "INSERT INTO "+qualifiedTable+" (VARGCOL) VALUES (G'"+jdbc_dbcs_words[18]+"')";
            //System.out.println("second stmt = " + ins2 + " .\n");
            statement.executeUpdate (ins2);

            //System.out.println("Look at file JDBCTEST/VARGTEST; press ENTER to proceed.\n");
            //System.in.read();

            ResultSet rs = statement.executeQuery ("SELECT * FROM " + COLLECTION + ".VARGTEST");

            rs.next ();
            String vargread = rs.getString(1);
            //System.out.println("jdbc_dbcs_words[17] = ." + jdbc_dbcs_words[17] + ".");
            //System.out.println("vargread = ."+vargread+".\n");
            //System.in.read();
            if (!vargread.equals(jdbc_dbcs_words[17])) {
                failMsg.append("Read found mismatched records: "+vargread+" != "+jdbc_dbcs_words[17]+".\n");
            }

            rs.next ();
            vargread = rs.getString(1);
            //System.out.println("jdbc_dbcs_words[18] = ." + jdbc_dbcs_words[18] + ".");
            //System.out.println("vargread = ."+vargread+".\n");
            //System.in.read();
            if (!vargread.equals(jdbc_dbcs_words[18])) {
                failMsg.append("Read found mismatched records: "+vargread+" != "+jdbc_dbcs_words[18]+".\n");
            }
            statement.close ();
            if (failMsg.length() != 0)
                failed(failMsg.toString());
            else
                succeeded();
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }


        // Clean up
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate( "DROP TABLE " + qualifiedTable );
        } catch (Exception e) {
            output_.println("Cleanup failed.");
            e.printStackTrace(output_);
        }
    }


}
