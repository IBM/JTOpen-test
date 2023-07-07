package test;

import java.sql.*;

/**
This is the Build Verification Test (BVT) for the Native
JDBC driver, modified 1/2001 so it can also be used by
the Toolbox driver.
This test is designed to be completely stand-alone
and used by the build team to verify a minimum level
of functionality in the
JDBC driver is working before they release it to the lab.
<P>
To test the <B>Native driver</B>, this test is run in the following way.
<OL>
<LI>Place the .class file for this test in any IFS directory you
choose.  For example, '/home/cujo'
<LI>Either add IFS directory to your existing CLASSPATH environment variable
or create an environment variable called CLASSPATH with that directory in it.
<P>For example,  ADDENVVAR ENVVAR(CLASSPATH) VALUE('/home/cujo')
<LI>Run the Java program with the following command for the CL command line
(note that the names of classfiles are case sensitive so JDBVTTest must be
exactly as written):
<P>JAVA JDBVTTest
</OL>
<B>NOTE: </B>It is possible to get stack traces with an exception that is
thrown by this test by specifying the command line parameter "TRACE".
<P>For example, JAVA JDBVTTest TRACE
<P>This information might be needed to track where within the JDBC driver
a failure happened.
<P>You have two choices to test the <B>Toolbox driver</B>.
Option 1 -- specify "toolbox" as the first command line
parameter.  The Toolbox driver will be loaded and called
to carry out requests.  To see full error info specify
"TRACE" as the second parm.  Option 2 -- Testcase test.JDBCBVT
follows the Toolbox test framework and will call this
testcase.

**/
public class JDBVTTest {
    // Allows stack traces to be output
    static boolean    showExceptionTraces;
    static Connection connection_;

    static boolean    nativeDriver = true;     // use the native driver by default
    static String     URL_ = null;              // set url properties

/**
Mainline for the JDBC BVT.
**/
    public static void main(String args[]) throws Exception
    {
        // Allow the user to specify that stack traces should be output
        // when exceptions are thrown.  Be default, don't display them
        // because the clutter up the results.
        if ((args.length > 0) && (args[0].equalsIgnoreCase("TRACE")))
            showExceptionTraces = true;
        else
            showExceptionTraces = false;

        if ((args.length > 1) && (args[1].equalsIgnoreCase("TRACE")))
            showExceptionTraces = true;

        // Allow the user to switch to the Toolbox driver
        if ((args.length > 0) && (args[0].equalsIgnoreCase("TOOLBOX")))
            nativeDriver = false;

        // Setup to use the native JBBC driver.  If this fails, we
        // will not be able to run the tests.
        if (!setup())
            System.exit(1);

        // Test the connection class.  We need the connection
        // test to be successful or we will not be able to run
        // the rest of the tests.
        if (!testConnection())
            System.exit(1);

        // Test the Statement interface
        testStatement();

        // Test the PreparedStatement interface
        testPreparedStatement();

        // Test the CallableStatement interface
        testCallableStatement();

        // Test the ResultSet interface
        testResultSet();

        // Test the ResultSetMetaData interface
        testResultSetMetaData();

        // Test the DatabaseMetaData interface
        testDatabaseMetaData();

        System.exit(0);
    }



/**
Handles all testing setup that is global to all the tests.  Today this just
registers the native JDBC driver.  In the future it might create some DataSources
and build larger tables that we use in verification.
**/
    public static boolean setup()
    {
        try
        {
           if (nativeDriver)
              Class.forName("com.ibm.db2.jdbc.app.DB2Driver");
           else
              Class.forName("com.ibm.as400.access.AS400JDBCDriver");
        }
        catch (Exception ex)
        {
           System.out.println("BVT failed setup - Exception");
           System.out.println("Details: " );
           System.out.println("    Message:....." + ex.getMessage());

           ex.printStackTrace();

           System.out.println("Exiting...");
           return false;
        }

        return true;
    }



/**
Handles ensuring the Connection interface is working correctly.
**/
    public static boolean testConnection()
    {
        boolean success = true;
        System.out.print("Connection testing.... ");
        try {

            if (URL_ == null)
            {
               if (nativeDriver)
                  URL_ = "jdbc:db2:*local";
               else
                  URL_ = "jdbc:as400";
            }

            connection_ = DriverManager.getConnection(URL_);

            System.out.println("successful");

        } catch (SQLException e) {
            System.out.println("failed - SQLException");
            System.out.println("Details: " );
            System.out.println("    Message:....." + e.getMessage());
            System.out.println("    SQLState:...." + e.getSQLState());
            System.out.println("    Vendor Code:." + e.getErrorCode());

            if (showExceptionTraces)
                e.printStackTrace();

            success = false;
        } catch (Exception ex) {
            System.out.println("failed - Exception");
            System.out.println("Details: " );
            System.out.println("    Message:....." + ex.getMessage());

            if (showExceptionTraces)
                ex.printStackTrace();

            success = false;
        }
        return success;
    }



/**
Handles ensuring the Statement interface is working correctly.
**/
    public static boolean testStatement()
    {
        Statement s = null;
        boolean success = true;
        System.out.print("Statement testing.... ");

        try {
            setupTestTable();

            s = connection_.createStatement();

            ResultSet rs = s.executeQuery("SELECT * FROM QGPL.JDBVTTEST");
            int i = 0;
            while (rs.next())
                i++;

            rs.close();

            if (i == 5) {
                System.out.println("successful");
                success = true;
            } else {
                System.out.println("failed - incorrect results");
                success = false;
            }


        } catch (SQLException e) {
            System.out.println("failed - SQLException");
            System.out.println("Details: " );
            System.out.println("    Message:....." + e.getMessage());
            System.out.println("    SQLState:...." + e.getSQLState());
            System.out.println("    Vendor Code:." + e.getErrorCode());

            if (showExceptionTraces)
                e.printStackTrace();

            success = false;
        } catch (Exception ex) {
            System.out.println("failed - Exception");
            System.out.println("Details: " );
            System.out.println("    Message:....." + ex.getMessage());

            if (showExceptionTraces)
                ex.printStackTrace();

            success = false;
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (SQLException cleanup) {
                    System.out.println("    Warning - Exception on testcase cleanup.");
                    System.out.println("    Details: " );
                    System.out.println("        Message:....." + cleanup.getMessage());
                    System.out.println("        SQLState:...." + cleanup.getSQLState());
                    System.out.println("        Vendor Code:." + cleanup.getErrorCode());
                }
            }
        }
        return success;
    }



/**
Handles ensuring the PreparedStatement interface is working correctly.
**/
    public static boolean testPreparedStatement()
    {
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        boolean success = true;
        System.out.print("PreparedStatement testing.... ");

        try {
            setupTestTable();

            // Use a prepared statement to insert data into the table.
            ps = connection_.prepareStatement("INSERT INTO QGPL.JDBVTTEST VALUES(?, ?)");
            ps.setInt(1, -1);
            ps.setString(2, "MARK O");
            ps.executeUpdate();

            // Use a prepared statement to get a result set from the table.
            ps2 = connection_.prepareStatement("SELECT * FROM QGPL.JDBVTTEST WHERE COL1 < ?");
            ps2.setInt(1, 0);
            ResultSet rs = ps2.executeQuery();
            int i = 0;
            while (rs.next())
                i++;

            if (i == 1) {
                System.out.println("successful");
            } else {
                System.out.println("failed - incorrect results");
                success = false;
            }


        } catch (SQLException e) {
            System.out.println("failed - SQLException");
            System.out.println("Details: " );
            System.out.println("    Message:....." + e.getMessage());
            System.out.println("    SQLState:...." + e.getSQLState());
            System.out.println("    Vendor Code:." + e.getErrorCode());

            if (showExceptionTraces)
                e.printStackTrace();

            success = false;
        } catch (Exception ex) {
            System.out.println("failed - Exception");
            System.out.println("Details: " );
            System.out.println("    Message:....." + ex.getMessage());

            if (showExceptionTraces)
                ex.printStackTrace();

            success = false;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException cleanup) {
                    System.out.println("    Warning - Exception on testcase cleanup.");
                    System.out.println("    Details: " );
                    System.out.println("        Message:....." + cleanup.getMessage());
                    System.out.println("        SQLState:...." + cleanup.getSQLState());
                    System.out.println("        Vendor Code:." + cleanup.getErrorCode());
                }
            }
            if (ps2 != null) {
                try {
                    ps2.close();
                } catch (SQLException cleanup) {
                    System.out.println("    Warning - Exception on testcase cleanup.");
                    System.out.println("    Details: " );
                    System.out.println("        Message:....." + cleanup.getMessage());
                    System.out.println("        SQLState:...." + cleanup.getSQLState());
                    System.out.println("        Vendor Code:." + cleanup.getErrorCode());
                }
            }
        }
        return success;
    }



/**
Handles ensuring the CallableStatement interface is working correctly.
**/
    public static boolean testCallableStatement()
    {
        CallableStatement cs = null;
        CallableStatement cs2 = null;
        boolean success = true;
        System.out.print("CallableStatement testing.... ");

        try {
            setupTestTable();
            setupStoredProcedures();

            // Use the stored procedure to update the table.
            cs = connection_.prepareCall("CALL QGPL.INSERTSP(?, ?)");
            cs.setInt(1, 0);
            cs.setString(2, "MARK O");
            cs.executeUpdate();
            cs.close();

            // TODO: replace this with a stored procedure that uses result sets.
            Statement s = connection_.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM QGPL.JDBVTTEST");
            int i = 0;
            while (rs.next())
                i++;
            if (i != 6)
                success = false;
            s.close();


            // Returns a result set, the total number of rows in the table and the
            // name found at the index passed in.
            cs2 = connection_.prepareCall("CALL QGPL.OUTPUTSP(?, ?)");
            cs2.setInt(1, 1);
            cs2.registerOutParameter(2, Types.VARCHAR);
            cs2.execute();
            if (!cs2.getString(2).equals("CUJO"))
                success = false;
            cs2.close();

            // TODO:  Test stored procedure with return value

            if (success) {
                System.out.println("successful");
            } else {
                System.out.println("failed - incorrect results");
                success = false;
            }

        } catch (SQLException e) {
            System.out.println("failed - SQLException");
            System.out.println("Details: " );
            System.out.println("    Message:....." + e.getMessage());
            System.out.println("    SQLState:...." + e.getSQLState());
            System.out.println("    Vendor Code:." + e.getErrorCode());

            if (showExceptionTraces)
                e.printStackTrace();

            success = false;
        } catch (Exception ex) {
            System.out.println("failed - Exception");
            System.out.println("Details: " );
            System.out.println("    Message:....." + ex.getMessage());

            if (showExceptionTraces)
                ex.printStackTrace();

            success = false;
        } finally {
            if (cs != null) {
                try {
                    cs.close();
                } catch (SQLException cleanup) {
                    System.out.println("    Warning - Exception on testcase cleanup.");
                    System.out.println("    Details: " );
                    System.out.println("        Message:....." + cleanup.getMessage());
                    System.out.println("        SQLState:...." + cleanup.getSQLState());
                    System.out.println("        Vendor Code:." + cleanup.getErrorCode());
                }
            }
            if (cs2 != null) {
                try {
                    cs2.close();
                } catch (SQLException cleanup) {
                    System.out.println("    Warning - Exception on testcase cleanup.");
                    System.out.println("    Details: " );
                    System.out.println("        Message:....." + cleanup.getMessage());
                    System.out.println("        SQLState:...." + cleanup.getSQLState());
                    System.out.println("        Vendor Code:." + cleanup.getErrorCode());
                }
            }
        }
        return success;
    }



/**
Handles ensuring the ResultSet interface is working correctly.
**/
    public static boolean testResultSet()
    {
        Statement s = null;
        boolean success = true;
        System.out.print("ResultSet testing.... ");

        try {
            setupTestTable();

            s = connection_.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM QGPL.JDBVTTEST");

            for (int i = 1; i < 6; i++) {
                rs.next();
                if (rs.getInt(1) != i) {
                    success = false;
                    break;
                }
            }

            s.close();

            if (success) {
                System.out.println("successful");
            } else {
                System.out.println("failed - incorrect results");
            }

        } catch (SQLException e) {
            System.out.println("failed - SQLException");
            System.out.println("Details: " );
            System.out.println("    Message:....." + e.getMessage());
            System.out.println("    SQLState:...." + e.getSQLState());
            System.out.println("    Vendor Code:." + e.getErrorCode());

            if (showExceptionTraces)
                e.printStackTrace();

            success = false;
        } catch (Exception ex) {
            System.out.println("failed - Exception");
            System.out.println("Details: " );
            System.out.println("    Message:....." + ex.getMessage());

            if (showExceptionTraces)
                ex.printStackTrace();

            success = false;
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (SQLException cleanup) {
                    System.out.println("    Warning - Exception on testcase cleanup.");
                    System.out.println("    Details: " );
                    System.out.println("        Message:....." + cleanup.getMessage());
                    System.out.println("        SQLState:...." + cleanup.getSQLState());
                    System.out.println("        Vendor Code:." + cleanup.getErrorCode());
                }
            }
        }
        return success;
    }



/**
Handles ensuring the ResultSetMetaData interface is working correctly.
**/
    public static boolean testResultSetMetaData()
    {
        Statement s = null;
        boolean success = true;
        System.out.print("ResultSetMetaData testing.... ");

        try {
            setupTestTable();

            s = connection_.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM QGPL.JDBVTTEST");
            ResultSetMetaData rsmd = rs.getMetaData();

            if (rsmd.getColumnCount() != 2)
                success = false;

            if (!rsmd.getColumnName(1).equals("COL1"))
                success = false;

            if (rsmd.getPrecision(1) != 10)
                success = false;

            if (rsmd.getScale(1) != 0)
                success = false;

            if (rsmd.getColumnType(1) != Types.INTEGER)
                success = false;

            if (success)
                System.out.println("successful");
            else
                System.out.println("failed - incorrect results");

        } catch (SQLException e) {
            System.out.println("failed - SQLException");
            System.out.println("Details: " );
            System.out.println("    Message:....." + e.getMessage());
            System.out.println("    SQLState:...." + e.getSQLState());
            System.out.println("    Vendor Code:." + e.getErrorCode());

            if (showExceptionTraces)
                e.printStackTrace();

            success = false;
        } catch (Exception ex) {
            System.out.println("failed - Exception");
            System.out.println("Details: " );
            System.out.println("    Message:....." + ex.getMessage());

            if (showExceptionTraces)
                ex.printStackTrace();

            success = false;
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (SQLException cleanup) {
                    System.out.println("    Warning - Exception on testcase cleanup.");
                    System.out.println("    Details: " );
                    System.out.println("        Message:....." + cleanup.getMessage());
                    System.out.println("        SQLState:...." + cleanup.getSQLState());
                    System.out.println("        Vendor Code:." + cleanup.getErrorCode());
                }
            }
        }
        return success;
    }



/**
Handles ensuring the DatabaseMetaData interface is working correctly.
**/
    public static boolean testDatabaseMetaData()
    {
        boolean success = true;
        System.out.print("DatabaseMetaData testing.... ");

        try {
            DatabaseMetaData dmd = connection_.getMetaData();

            // this result could be huge.  Only fetch the first 1000 rows
            ResultSet rs = dmd.getTables(null, null, null, null);

            int count = 0;
            while (rs.next() && count < 1000)
                count++;

            rs.close();

            if (count > 0) {
                System.out.println("successful");
            } else {
                System.out.println("failed - incorrect results");
                success = false;
            }

        } catch (SQLException e) {
            System.out.println("failed - SQLException");
            System.out.println("Details: " );
            System.out.println("    Message:....." + e.getMessage());
            System.out.println("    SQLState:...." + e.getSQLState());
            System.out.println("    Vendor Code:." + e.getErrorCode());

            if (showExceptionTraces)
                e.printStackTrace();

            success = false;
        } catch (Exception ex) {
            System.out.println("failed - Exception");
            System.out.println("Details: " );
            System.out.println("    Message:....." + ex.getMessage());

            if (showExceptionTraces)
                ex.printStackTrace();

            success = false;
        }
        return success;
    }



/**
Removes the baseline test table (and stored procedures) that we use in our BVT test.
**/
    public static boolean cleanupTestTable()
    throws SQLException
    {
        Statement s = null;
        try
        {
          if (connection_ == null) testConnection();
          s = connection_.createStatement();

          try {
            s.executeUpdate("DROP PROCEDURE QGPL.INSERTSP");
          } catch (SQLException ignore) {
            // This is an OK failure.  The procedure might not exist.
          }

          try {
            s.executeUpdate("DROP PROCEDURE QGPL.OUTPUTSP");
          } catch (SQLException ignore) {
            // This is an OK failure.  The procedure might not exist.
          }

          try {
            s.executeUpdate("DROP TABLE QGPL.JDBVTTEST");
          } catch (SQLException ignore) {
            // This is an OK failure.  The table might not exist.
          }
        }
        catch (Throwable t) {
          if (showExceptionTraces) {
            t.printStackTrace();
          }
          System.out.println("\nCleanup failed.  Message is " + t.getMessage());
          return false;
        }
        finally {
          if (s != null) try { s.close(); } catch (Throwable t) {}
        }
        return true;
    }



/**
Creates the baseline test table that we use in our BVT test.
**/
    public static void setupTestTable()
    throws SQLException
    {
        Statement s = connection_.createStatement();
        try {
            s.executeUpdate("DROP TABLE QGPL.JDBVTTEST");
        } catch (SQLException ignore) {
            // This is an OK failure.  The table might not exist.
        }

        // Build a table
        s.executeUpdate("CREATE TABLE QGPL.JDBVTTEST(COL1 INT, COL2 VARCHAR(10))");

        s.executeUpdate("INSERT INTO QGPL.JDBVTTEST VALUES(1, 'CUJO')");
        s.executeUpdate("INSERT INTO QGPL.JDBVTTEST VALUES(2, 'FRED')");
        s.executeUpdate("INSERT INTO QGPL.JDBVTTEST VALUES(3, 'MARK')");
        s.executeUpdate("INSERT INTO QGPL.JDBVTTEST VALUES(4, 'SCOTT')");
        s.executeUpdate("INSERT INTO QGPL.JDBVTTEST VALUES(5, 'JASON')");

        s.close();
    }



/**
Creates all the stored procedures that are used in our BVT testing.
**/
    public static void setupStoredProcedures()
    throws SQLException
    {
        String insertSP = "CREATE PROCEDURE QGPL.INSERTSP "
                          + " (IN P1 INTEGER, IN P2 VARCHAR(10)) "
                          + " RESULT SET 0 LANGUAGE SQL MODIFIES SQL DATA SPECIFIC QGPL.INSERTSP "
                          + " INSSTART: BEGIN"
                          + "   INSERT INTO QGPL.JDBVTTEST VALUES(P1, P2);"
                          + " END INSSTART";


        String outputSP = "CREATE PROCEDURE QGPL.OUTPUTSP "
                          + " (IN P1 INTEGER, OUT P2 VARCHAR(10)) "
                          + " RESULT SET 1 LANGUAGE SQL SPECIFIC QGPL.OUTPUTSP "
                          + " OUTSTART: BEGIN"
                          + "   DECLARE C1 CURSOR FOR SELECT COL2 FROM QGPL.JDBVTTEST "
                          + "        WHERE COL1 = P1; "
                          + "   OPEN C1; "
                          + "   FETCH C1 INTO P2; "
                          + "   CLOSE C1; "
                          + " END OUTSTART";

        // TODO: SP with return value
        // TODO: SP with ResultSet

        Statement s = connection_.createStatement();

        try {
            s.executeUpdate("DROP PROCEDURE QGPL.INSERTSP");
        } catch (SQLException e) {
        }

        try {
            s.executeUpdate("DROP PROCEDURE QGPL.OUTPUTSP");
        } catch (SQLException e) {
        }

        s.executeUpdate(insertSP);
        s.executeUpdate(outputSP);
        s.close();

    }

    // create a programmable way to set the url
    public static void setURL(String newValue)
    {
       URL_ = newValue;
    }

    // create a programmable way to set trace
    public static void setShowException(boolean newValue)
    {
       showExceptionTraces = true;
    }

    // create a programmable way to set driver used
    public static void setUseNativeDriver(boolean newValue)
    {
        nativeDriver = newValue;
    }

}


