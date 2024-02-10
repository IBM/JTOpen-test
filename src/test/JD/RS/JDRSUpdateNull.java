
package test.JD.RS;

import com.ibm.as400.access.AS400;

import test.JDRSTest;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Hashtable;



/**
Testcase JDRSUpdateNull.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>updateNull()
</ul>
**/
public class JDRSUpdateNull
extends JDTestcase
{



    // Private data.
    private static final String key_            = "JDRSUpdateNull";
    private static String select_         = "SELECT * FROM "
                                                    + JDRSTest.RSTEST_UPDATE;

    private Statement           statement_;
    private Statement           statement2_;
    private ResultSet           rs_;



/**
Constructor.
**/
    public JDRSUpdateNull (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSUpdateNull",
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
        select_         = "SELECT * FROM " + JDRSTest.RSTEST_UPDATE;

        if (isJdbc20 ()) {
            // SQL400 - driver neutral...
            String url = baseURL_
            //String url = "jdbc:as400://" + systemObject_.getSystemName()
                
                
                + ";data truncation=true";
            connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
            connection_.setAutoCommit(false); // @C1A
            statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
            statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
    
            statement_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_UPDATE
                + " (C_KEY) VALUES ('DUMMY_ROW')");
            statement_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_UPDATE
                + " (C_KEY) VALUES ('SECOND_ROW')");
            statement_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_UPDATE
                + " (C_KEY) VALUES ('" + key_ + "')");
    
            rs_ = statement_.executeQuery (select_ + " FOR UPDATE");
        }
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
        if (isJdbc20 ()) {
            rs_.close ();
            statement_.close ();
            connection_.commit(); // @C1A
            connection_.close ();
        }
    }



/**
updateNull() - Should throw exception when the result set is
closed.
**/
    public void Var001()
    {
        if (checkJdbc20 ()) {
        try {
            Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_UPDATE + " FOR UPDATE");
            rs.next ();
            rs.close ();
            rs.updateNull ("C_VARCHAR_50");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateNull() - Should throw exception when the result set is
not updatable.
**/
    public void Var002()
    {
        if (checkJdbc20 ()) {
        try {
            Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_UPDATE);
            rs.next ();
            rs.updateNull ("C_VARCHAR_50");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateNull() - Should throw exception when cursor is not pointing
to a row.
**/
    public void Var003()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, null);
            rs_.updateNull ("C_VARCHAR_50");
            rs_.updateRow(); 
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateNull() - Should throw an exception when the column
is an invalid index.
**/
    public void Var004()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateNull (100);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateNull() - Should throw an exception when the column
is 0.
**/
    public void Var005()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateNull (0);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateNull() - Should throw an exception when the column
is -1.
**/
    public void Var006()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateNull (-1);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateNull() - Should work when the column index is valid.
**/
    public void Var007()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateNull (13);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            String v = rs2.getString ("C_VARCHAR_50");
            boolean wn = rs2.wasNull ();
            rs2.close ();
            assertCondition (wn == true, ""+v);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateNull() - Should throw an exception when the column
name is null.
**/
    public void Var008()
    {
      if (checkJdbc20 ()) {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC throws null pointer exception when column name is null "); 
        } else { 
          try {
            JDRSTest.position (rs_, key_);
            rs_.updateNull (null);
            failed ("Didn't throw SQLException");
          }
          catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
          }
        }
      }
    }


/**
updateNull() - Should throw an exception when the column
name is an empty string.
**/
    public void Var009()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateNull ("");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateNull() - Should throw an exception when the column
name is invalid.
**/
    public void Var010()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateNull ("INVALID");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateNull() - Should work when the column name is valid.
**/
    public void Var011()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateNull ("C_VARCHAR_50");
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            String v = rs2.getString ("C_VARCHAR_50");
            boolean wn = rs2.wasNull ();
            rs2.close ();
            assertCondition (wn == true, ""+v);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateNull() - Should be reflected by get, even if update has
not yet been issued (i.e. update is still pending).
**/
    public void Var012()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateNull ("C_VARCHAR_50");
            String v = rs_.getString ("C_VARCHAR_50");
            boolean wn = rs_.wasNull ();
            assertCondition (wn == true, ""+v);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
updateNull() - Should be reflected by get, after update has
been issued, but cursor has not been repositioned.
**/
    public void Var013()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateNull ("C_VARCHAR_50");
            rs_.updateRow ();
            String v = rs_.getString ("C_VARCHAR_50");
            boolean wn = rs_.wasNull ();
            assertCondition (wn == true, ""+v);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
updateNull() - Should be reflected by get, after update has
been issued and cursor has been repositioned.
**/
    public void Var014()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateNull ("C_VARCHAR_50");
            rs_.updateRow ();
            rs_.beforeFirst ();
            JDRSTest.position (rs_, key_);
            String v = rs_.getString ("C_VARCHAR_50");
            boolean wn = rs_.wasNull ();
            assertCondition (wn == true, ""+v);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
updateNull() - Should work when the current row is the insert
row.
**/
    public void Var015()
    {
        if (checkJdbc20 ()) {
        try {
            rs_.moveToInsertRow ();
            rs_.updateString ("C_KEY", "JDRSUpdateNull 1");
            rs_.updateNull ("C_VARCHAR_50");
            rs_.insertRow ();
            JDRSTest.position (rs_, "JDRSUpdateNull 1");
            String v = rs_.getString ("C_VARCHAR_50");
            boolean wn = rs_.wasNull ();
            assertCondition (wn == true, ""+v);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateNull() - Should be reflected by get on insert row, even if
insert has not yet been issued (i.e. insert is still pending).
**/
    public void Var016()
    {
        if (checkJdbc20 ()) {
        try {
            rs_.moveToInsertRow ();
            rs_.updateNull ("C_VARCHAR_50");
            String v = rs_.getString ("C_VARCHAR_50");
            boolean wn = rs_.wasNull ();
            assertCondition (wn == true, ""+v);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }

           

/**
updateNull() - Should throw an exception on a deleted row.
**/
    public void Var017()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "DUMMY_ROW");
            rs_.deleteRow ();
            rs_.updateNull ("C_VARCHAR_50");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }


/**
updateNull() - Update a SMALLINT.
**/
    public void Var018 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateNull ("C_SMALLINT");
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            short v = rs2.getShort ("C_SMALLINT");
            boolean wn = rs2.wasNull ();
            rs2.close ();
            assertCondition (wn == true, ""+v);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateNull() - Update an INTEGER.
**/
    public void Var019 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateNull ("C_INTEGER");
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            int v = rs2.getInt ("C_INTEGER");
            boolean wn = rs2.wasNull ();
            rs2.close ();
            assertCondition (wn == true, ""+v);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateNull() - Update a REAL.
**/
    public void Var020 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateNull ("C_REAL");
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            float v = rs2.getFloat ("C_REAL");
            boolean wn = rs2.wasNull ();
            rs2.close ();
            assertCondition (wn == true, ""+v);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }


/**
updateNull() - Update a FLOAT.
**/
    public void Var021 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateNull ("C_FLOAT");
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            float v = rs2.getFloat ("C_FLOAT");
            boolean wn = rs2.wasNull ();
            rs2.close ();
            assertCondition (wn == true, ""+v);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateNull() - Update a DOUBLE.
**/
    public void Var022 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateNull ("C_DOUBLE");
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            double v = rs2.getDouble ("C_DOUBLE");
            boolean wn = rs2.wasNull ();
            rs2.close ();
            assertCondition (wn == true, ""+v);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateNull() - Update a DECIMAL.
**/
    public void Var023 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateNull ("C_DECIMAL_105");
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            BigDecimal v = rs2.getBigDecimal ("C_DECIMAL_105");
            boolean wn = rs2.wasNull ();
            rs2.close ();
            assertCondition (wn == true, ""+v);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateNull() - Update a NUMERIC.
**/
    public void Var024 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateNull ("C_NUMERIC_105");
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            BigDecimal v = rs2.getBigDecimal ("C_NUMERIC_105");
            boolean wn = rs2.wasNull ();
            rs2.close ();
            assertCondition (wn == true, ""+v);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateNull() - Update a CHAR.
**/
    public void Var025 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateNull ("C_CHAR_50");
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            String v = rs2.getString ("C_CHAR_50");
            boolean wn = rs2.wasNull ();
            rs2.close ();
            assertCondition (wn == true, ""+v);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateNull() - Update a VARCHAR.
**/
    public void Var026 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateNull ("C_VARCHAR_50");
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            String v = rs2.getString ("C_VARCHAR_50");
            boolean wn = rs2.wasNull ();
            rs2.close ();
            assertCondition (wn == true, ""+v);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateNull() - Update a BINARY.
**/
    public void Var027 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateNull ("C_BINARY_20");
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            String v = rs2.getString ("C_BINARY_20");
            boolean wn = rs2.wasNull ();
            rs2.close ();
            assertCondition (wn == true, ""+v);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateNull() - Update a VARBINARY.
**/
    public void Var028 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateNull ("C_VARBINARY_20");
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            String v = rs2.getString ("C_VARBINARY_20");
            boolean wn = rs2.wasNull ();
            rs2.close ();
            assertCondition (wn == true, ""+v);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateNull() - Update a CLOB.
**/
    public void Var029 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateNull ("C_CLOB");
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                Clob v = rs2.getClob ("C_CLOB");
                boolean wn = rs2.wasNull ();
                rs2.close ();
                assertCondition (wn == true, ""+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }



/**
updateNull() - Update a DBCLOB.
**/
    public void Var030 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateNull ("C_DBCLOB");
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                Clob v = rs2.getClob ("C_DBCLOB");
                boolean wn = rs2.wasNull ();
                rs2.close ();
                assertCondition (wn == true, ""+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }



/**
updateNull() - Update a BLOB.
**/
    public void Var031 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateNull ("C_BLOB");
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                Blob v = rs2.getBlob ("C_BLOB");
                boolean wn = rs2.wasNull ();
                rs2.close ();
                assertCondition (wn == true, ""+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }



/**
updateNull() - Update a DATE.
**/
    public void Var032 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateNull ("C_DATE");
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            Date v = rs2.getDate ("C_DATE");
            boolean wn = rs2.wasNull ();
            rs2.close ();
            assertCondition (wn == true, ""+v);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateNull() - Update a TIME.
**/
    public void Var033 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateNull ("C_TIME");
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            Time v = rs2.getTime ("C_TIME");
            boolean wn = rs2.wasNull ();
            rs2.close ();
            assertCondition (wn == true, ""+v);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateNull() - Update a TIMESTAMP.
**/
    public void Var034 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateNull ("C_TIMESTAMP");
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            Timestamp v = rs2.getTimestamp ("C_TIMESTAMP");
            boolean wn = rs2.wasNull ();
            rs2.close ();
            assertCondition (wn == true, ""+v);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateNull() - Update a DATALINK.
**/
    public void Var035 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            // We do not test updating datalinks, since it is not 
            // possible to open a updatable cursor/result set with
            // a datalink column.
            notApplicable("DATALINK update not supported.");
            /*
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateNull ("C_DATALINK");
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_DATLINK");
                boolean wn = rs2.wasNull ();
                rs2.close ();
                assertCondition (wn == true);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
            */
        }
        }
    }



/**
updateNull() - Update a DISTINCT.
**/
    public void Var036 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateNull ("C_DISTINCT");
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_DISTINCT");
                boolean wn = rs2.wasNull ();
                rs2.close ();
                assertCondition (wn == true, ""+v);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }



/**
updateNull() - Update a BIGINT.
**/
    public void Var037()
    {
        if (checkJdbc20 ()) {
        if (checkBigintSupport()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateNull ("C_BIGINT");
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            long v = rs2.getLong ("C_BIGINT");
            boolean wn = rs2.wasNull ();
            rs2.close ();
            assertCondition (wn == true, ""+v);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
        }
    }

    
    public void dfpTest(String table) {
      if (checkDecFloatSupport()) {
        try {
          Statement s = connection_.createStatement(
              ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
          ResultSet rs = s
          .executeQuery("SELECT * FROM " + table + " FOR UPDATE ");
          rs.next();
          rs.updateNull(1);
          rs.updateRow();
          
          ResultSet rs2 = statement2_.executeQuery("SELECT * FROM " + table);
          rs2.next();
          String v = rs2.getString(1);
          rs2.close();
          s.close();
          try {
           connection_.commit(); 
          } catch (Exception e) {} 
          assertCondition(v == null, "Got " + v + " sb null ");
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }

    
    /**
     * updateInt -- set a DFP16 value 
     */
    public void Var038 () { dfpTest(JDRSTest.RSTEST_DFP16); }
  
    /**
     * updateInt -- set a DFP16 value 
     */
    public void Var039 () { dfpTest(JDRSTest.RSTEST_DFP34); }

/**
updateNull() - Update a BOOLEAN.
**/
    public void Var040()
    {
        if (checkBooleanSupport()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateNull ("C_BOOLEAN");
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            long v = rs2.getLong ("C_BOOLEAN");
            boolean wn = rs2.wasNull ();
            rs2.close ();
            assertCondition (wn == true, ""+v);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }

    


}



