package test.JD.RS;

import com.ibm.as400.access.AS400;

import test.JDJobName;
import test.JDRSTest;
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Hashtable;



/**
Testcase JDRSUpdateNString.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>updateNString()
</ul>
**/
public class JDRSUpdateNString
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSUpdateNString";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



    // Private data.
    private static final String key_ = "JDRSUpdateNString";
    private static String select_    = "SELECT * FROM " + JDRSTest.RSTEST_UPDATE;

    private Statement           statement_;
    private Statement           statement2_;
    private ResultSet           rs_;



    /**
    Constructor.
    **/
    public JDRSUpdateNString (AS400 systemObject,
                             Hashtable namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             
                             String password)
    {
        super (systemObject, "JDRSUpdateNString",
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

        if(isJdbc40 ())
        {
            // SQL400 - driver neutral...
            String url = baseURL_
                         // String url = "jdbc:as400://" + systemObject_.getSystemName()
                         
                         
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
        if(isJdbc40 ())
        {
            rs_.close ();
            statement_.close ();
            connection_.commit(); // @C1A
            connection_.close ();
        }
    }



    /**
    updateNString() - Should throw exception when the result set is
    closed.
    **/
    public void Var001()
    {
        if(checkJdbc40 ())
        {
            try
            {
                Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                           ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = s.executeQuery ("SELECT * FROM "
                                               + JDRSTest.RSTEST_UPDATE + " FOR UPDATE");
                rs.next ();
                rs.close ();
                JDReflectionUtil.callMethod_V(rs, "updateNString", "C_VARCHAR_50", "JDBC");
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNString() - Should throw exception when the result set is
    not updatable.
    **/
    public void Var002()
    {
        if(checkJdbc40 ())
        {
            try
            {
                Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                           ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = s.executeQuery ("SELECT * FROM "
                                               + JDRSTest.RSTEST_UPDATE);
                rs.next ();
                JDReflectionUtil.callMethod_V(rs, "updateNString", "C_VARCHAR_50", "Database");
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNString() - Should throw exception when cursor is not pointing
    to a row.
    **/
    public void Var003()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, null);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_VARCHAR_50", "Tables");
                rs_.updateRow(); 
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNString() - Should throw an exception when the column
    is an invalid index.
    **/
    public void Var004()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", 100, "Schemas");
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNString() - Should throw an exception when the column
    is 0.
    **/
    public void Var005()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", 0, "Columns");
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNString() - Should throw an exception when the column
    is -1.
    **/
    public void Var006()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", -1, "Rows");
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNString() - Should work when the column index is valid.
    **/
    public void Var007()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", 14, "Queries");
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_VARCHAR_50");
                rs2.close ();
                assertCondition (v.equals ("Queries"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNString() - Should throw an exception when the column
    name is null.
    **/
    public void Var008()
    {
      if(checkJdbc40 ())
      {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC throws null pointer exception when column name is null "); 
        } else { 
          
          try
          {
            JDRSTest.position (rs_, key_);
            JDReflectionUtil.callMethod_V(rs_, "updateNString", null, "Updates");
            failed ("Didn't throw SQLException");
          }
          catch(Exception e)
          {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
          }
        }
      }
    }


    /**
    updateNString() - Should throw an exception when the column
    name is an empty string.
    **/
    public void Var009()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "", "Inserts");
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNString() - Should throw an exception when the column
    name is invalid.
    **/
    public void Var010()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "INVALID", "Joins");
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNString() - Should work when the column name is valid.
    **/
    public void Var011()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_VARCHAR_50", "Inner joins");
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_VARCHAR_50");
                rs2.close ();
                assertCondition (v.equals ("Inner joins"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNString() - Should update to SQL NULL when the column
    value is null.
    **/
    public void Var012()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_VARCHAR_50", null);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_VARCHAR_50");
                boolean wn = rs2.wasNull ();
                rs2.close ();
                assertCondition ((v == null) && (wn == true));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNString() - Should be reflected by get, even if update has
    not yet been issued (i.e. update is still pending).
    **/
    public void Var013()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_VARCHAR_50", "DB2/400");
                assertCondition (rs_.getString ("C_VARCHAR_50").equals ("DB2/400"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }




    /**
    updateNString() - Should be reflected by get, after update has
    been issued, but cursor has not been repositioned.
    **/
    public void Var014()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_VARCHAR_50", "SQL");
                rs_.updateRow ();
                assertCondition (rs_.getString ("C_VARCHAR_50").equals ("SQL"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }




    /**
    updateNString() - Should be reflected by get, after update has
    been issued and cursor has been repositioned.
    **/
    public void Var015()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_VARCHAR_50", "Collections");
                rs_.updateRow ();
                rs_.beforeFirst ();
                JDRSTest.position (rs_, key_);
                String v = rs_.getString ("C_VARCHAR_50");
                assertCondition (v.equals ("Collections"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }




    /**
    updateNString() - Should work when the current row is the insert
    row.
    **/
    public void Var016()
    {
        if(checkJdbc40 ())
        {
            try
            {
                rs_.moveToInsertRow ();
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_KEY", "JDRSUpdateNString 1");
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_VARCHAR_50", "Foreign keys");
                rs_.insertRow ();
                JDRSTest.position (rs_, "JDRSUpdateNString 1");
                assertCondition (rs_.getString ("C_VARCHAR_50").equals ("Foreign keys"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNString() - Should be reflected by get on insert row, even if
    insert has not yet been issued (i.e. insert is still pending).
    **/
    public void Var017()
    {
        if(checkJdbc40 ())
        {
            try
            {
                rs_.moveToInsertRow ();
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_VARCHAR_50", "Primary keys");
                assertCondition (rs_.getString ("C_VARCHAR_50").equals ("Primary keys"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNString() - Should throw an exception on a deleted row.
    **/
    public void Var018()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, "DUMMY_ROW");
                rs_.deleteRow ();
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_VARCHAR_50", "STRSQL");
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


    /**
    updateNString() - Update a SMALLINT.
    **/
    public void Var019 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_SMALLINT", "423");
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                short v = rs2.getShort ("C_SMALLINT");
                rs2.close ();
                assertCondition (v == 423);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNString() - Update a SMALLINT, when the integer is too big.
    **/
    public void Var020 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_SMALLINT", "487623");
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNString() - Update a SMALLINT, when the string is not a number.
    **/
    public void Var021 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_SMALLINT", "Record level access");
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNString() - Update an INTEGER.
    **/
    public void Var022 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_INTEGER", "-1228374");
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                int v = rs2.getInt ("C_INTEGER");
                rs2.close ();
                assertCondition (v == -1228374);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNString() - Update an INTEGER, when the integer is too big.
    **/
    public void Var023 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_INTEGER", "45353487623");
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNString() - Update an INTEGER, when the string is not a number.
    **/
    public void Var024 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_INTEGER", "Data queues");
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNString() - Update a REAL.
    **/
    public void Var025 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_REAL", "-1.5");
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                float v = rs2.getFloat ("C_REAL");
                rs2.close ();
                assertCondition (v == -1.5);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }


    /**
    updateNString() - Update a REAL, when the number is too big.
    
    Note:  Given the new data truncation rules for JDBC, this testcase
           is expected to work without throwing a DataTruncation exception.
    **/
    public void Var026 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_REAL", "4534335.443487623");
                rs_.updateRow ();

                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                float v = rs2.getFloat ("C_REAL");
                rs2.close ();
                assertCondition (v == 4534335.5);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }

        /*
        failed ("Didn't throw SQLException");
    }
    catch (Exception e) {
        DataTruncation dt = (DataTruncation)e;
        assertCondition ((dt.getIndex() == expectedColumn)
            && (dt.getParameter() == false)
            && (dt.getRead() == false)
            && (dt.getDataSize() == 16)
            && (dt.getTransferSize() == 8));

    }
    }
    */
    }



    /**
    updateNString() - Update a REAL, when the string is not a number.
    **/
    public void Var027 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_INTEGER", "Data areas");
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }




    /**
    updateNString() - Update a FLOAT.
    **/
    public void Var028 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_FLOAT", "9845.0");
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                float v = rs2.getFloat ("C_FLOAT");
                rs2.close ();
                assertCondition (v == 9845.0);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNString() - Update a FLOAT, when the number is too big.
    
    Note:  Given the new data truncation rules for JDBC, this testcase
           is expected to work without throwing a DataTruncation exception.
    **/
    public void Var029 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_FLOAT", "453435.4434556337633");
                rs_.updateRow ();

                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                float v = rs2.getFloat ("C_FLOAT");
                rs2.close ();
                assertCondition ((v > 453435) && (v < 453436));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
        /*
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            DataTruncation dt = (DataTruncation)e;
            assertCondition ((dt.getIndex() == expectedColumn)
                && (dt.getParameter() == false)
                && (dt.getRead() == false)
                && (dt.getDataSize() == 19)
                && (dt.getTransferSize() == 17));

        }
        }
        */
    }



    /**
    updateNString() - Update a FLOAT, when the string is not a number.
    **/
    public void Var030 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_FLOAT", "User spaces");
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


    /**
    updateNString() - Update a DOUBLE.
    **/
    public void Var031 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_DOUBLE", "-19845");
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                double v = rs2.getDouble ("C_DOUBLE");
                rs2.close ();
                assertCondition (v == -19845);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNString() - Update a DOUBLE, when the number is too big.
    
    Note:  Given the new data truncation rules for JDBC, this testcase
           is expected to work without throwing a DataTruncation exception.
    **/
    public void Var032 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_DOUBLE", "4533435.4433445532333");
                rs_.updateRow ();

                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                float v = rs2.getFloat ("C_DOUBLE");
                rs2.close ();
                assertCondition (v == 4533435.5);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
        /*
        failed ("Didn't throw SQLException");
    }
    catch (Exception e) {
        DataTruncation dt = (DataTruncation)e;
        assertCondition ((dt.getIndex() == expectedColumn)
            && (dt.getParameter() == false)
            && (dt.getRead() == false)
            && (dt.getDataSize() == 20)
            && (dt.getTransferSize() == 16));

    }
    }
    */
    }



    /**
    updateNString() - Update a DOUBLE, when the string is not a number.
    **/
    public void Var033 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_DOUBLE", "IFS");
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


    /**
    updateNString() - Update a DECIMAL.
    **/
    public void Var034 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_DECIMAL_105", "-4533.4");
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                BigDecimal v = rs2.getBigDecimal ("C_DECIMAL_105");
                rs2.close ();
                assertCondition (v.floatValue() == -4533.4f);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNString() - Update a DECIMAL, when the value is too big.
    **/
    public void Var035 ()
    {
        if(checkJdbc40 ())
        {
            int expectedColumn = -1;
            try
            {
                JDRSTest.position (rs_, key_);
                expectedColumn = rs_.findColumn ("C_DECIMAL_40");
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_DECIMAL_40", "543511");
                rs_.updateRow ();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
		if (e instanceof DataTruncation) { 
                DataTruncation dt = (DataTruncation)e;
                assertCondition ((dt.getIndex() == expectedColumn)
                                 && (dt.getParameter() == false)
                                 && (dt.getRead() == false)
                                 && (dt.getDataSize() == 6)
                                 && (dt.getTransferSize() == 4));
		} else {
		    assertSqlException(e, -99999, "07006", "Data type mismatch",
				       "Mismatch instead of truncation in latest toolbox");

		}

            }
        }
    }


    /**
    updateNString() - Update a DECIMAL, when the string is not a number.
    **/
    public void Var036 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_DECIMAL1050", "GUI components");
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNString() - Update a NUMERIC.
    **/
    public void Var037 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_NUMERIC_105", "-9933.455");
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                BigDecimal v = rs2.getBigDecimal ("C_NUMERIC_105");
                rs2.close ();
                assertCondition (v.doubleValue() == -9933.455);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNString() - Update a NUMERIC, when the value is too big.
    **/
    public void Var038 ()
    {
        if(checkJdbc40 ())
        {
            int expectedColumn = -1;
            try
            {
                JDRSTest.position (rs_, key_);
                expectedColumn = rs_.findColumn ("C_NUMERIC_40");
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_NUMERIC_40", "-331515");
                rs_.updateRow ();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
		if (e instanceof DataTruncation) { 
                DataTruncation dt = (DataTruncation)e;
                assertCondition ((dt.getIndex() == expectedColumn)
                                 && (dt.getParameter() == false)
                                 && (dt.getRead() == false)
                                 && (dt.getDataSize() == 6)
                                 && (dt.getTransferSize() == 4));
		} else {
		    assertSqlException(e, -99999, "07006", "Data type mismatch",
				       "Mismatch instead of truncation in latest toolbox");

		}

            }
        }
    }



    /**
    updateNString() - Update a NUMERIC, when the string is not a number.
    **/
    public void Var039 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_NUMERIC_105", "Jar maker");
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNString() - Update a CHAR.
    **/
    public void Var040 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_CHAR_50", "JDBC 2.0");
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_CHAR_50");
                rs2.close ();
                assertCondition (v.equals ("JDBC 2.0                                          "));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNString() - Update a CHAR, when the value is too big.
    **/
    public void Var041 ()
    {
        if(checkJdbc40 ())
        {
            int expectedColumn = -1;
            try
            {
                JDRSTest.position (rs_, key_);
                expectedColumn = rs_.findColumn ("C_CHAR_1");
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_CHAR_1", "Data conversion");
                rs_.updateRow ();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                DataTruncation dt = (DataTruncation)e;
                assertCondition ((dt.getIndex() == expectedColumn)
                                 && (dt.getParameter() == false)
                                 && (dt.getRead() == false)
                                 && (dt.getDataSize() == 15)
                                 && (dt.getTransferSize() == 1),
				 "dt.getIndex()="+dt.getIndex()+" sb "+expectedColumn+"\n"+
				 "dt.getParameter()="+dt.getParameter()+" sb false\n" +
				 "dt.getRead()="+dt.getRead()+" sb false\n"+
				 "dt.getDataSize()="+dt.getDataSize()+" sb 15\n"+
                                 "dt.getTransferSize()="+dt.getTransferSize()+"sb 1");

            }
        }
    }



    /**
    updateNString() - Update a VARCHAR.
    **/
    public void Var042 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_VARCHAR_50", "Message files");
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_VARCHAR_50");
                rs2.close ();
                assertCondition (v.equals ("Message files"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNString() - Update a VARCHAR, when the value is too big.
    **/
    public void Var043 ()
    {
        if(checkJdbc40 ())
        {
            int expectedColumn = -1;
            try
            {
                JDRSTest.position (rs_, key_);
                expectedColumn = rs_.findColumn ("C_VARCHAR_50");
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_VARCHAR_50", "This string is really long and is testing data truncation.");
                rs_.updateRow ();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                DataTruncation dt = (DataTruncation)e;
                assertCondition ((dt.getIndex() == expectedColumn)
                                 && (dt.getParameter() == false)
                                 && (dt.getRead() == false)
                                 && (dt.getDataSize() == 58)
                                 && (dt.getTransferSize() == 50),
				 "dt.getIndex()="+dt.getIndex()+" sb "+expectedColumn+"\n"+
				 "dt.getParameter()="+dt.getParameter()+" sb false\n" +
				 "dt.getRead()="+dt.getRead()+" sb false\n"+
				 "dt.getDataSize()="+dt.getDataSize()+" sb 58\n"+
                                 "dt.getTransferSize()="+dt.getTransferSize()+"sb 50");

            }
        }
    }



    /**
    updateNString() - Update a BINARY.
    **/
    public void Var044 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_BINARY_20", "F1F2F3F4F5");
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_BINARY_20");
                rs2.close ();
                assertCondition (v.startsWith ("F1F2F3F4F5"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNString() - Update a BINARY, when the value is too big.
    **/
    public void Var045 ()
    {
        if(checkJdbc40 ())
        {
            int expectedColumn = -1;
            try
            {
                JDRSTest.position (rs_, key_);
                expectedColumn = rs_.findColumn ("C_BINARY_1");
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_BINARY_1", "F1F2F3F4");
                rs_.updateRow ();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                DataTruncation dt = (DataTruncation)e;
                assertCondition ((dt.getIndex() == expectedColumn)
                                 && (dt.getParameter() == false)
                                 && (dt.getRead() == false)
                                 && (dt.getTransferSize() == 1));
            }
        }

    }



    /**
    updateNString() - Update a VARBINARY.
    **/
    public void Var046 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_VARBINARY_20", "F1");
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_VARBINARY_20");
                rs2.close ();
                assertCondition (v.equals ("F1"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNString() - Update a VARBINARY, when the value is too big.
    **/
    public void Var047 ()
    {
        if(checkJdbc40 ())
        {
            int expectedColumn = -1;
            try
            {
                JDRSTest.position (rs_, key_);
                expectedColumn = rs_.findColumn ("C_VARBINARY_20");
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_VARBINARY_20", "F1F2F3F4F5F1F2F3F4F5F1F2F3F4F5F1F2F3F4F5F1F2F3F4F5F1F2F3F4F5");
                rs_.updateRow ();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                DataTruncation dt = (DataTruncation)e;
                assertCondition ((dt.getIndex() == expectedColumn)
                                 && (dt.getParameter() == false)
                                 && (dt.getRead() == false)
                                 && (dt.getTransferSize() == 20));
            }
        }
    }



    /**
    updateNString() - Update a CLOB.
    
    SQL400 - the native driver expects this update to work correctly.
    **/
    public void Var048 ()
    {
        if(checkJdbc40 ())
        {
            if(checkLobSupport ())
            {
                try
                {
                    JDRSTest.position (rs_, key_);
                    JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_CLOB", "Toolbox");

                    rs_.updateRow ();
                    ResultSet rs2 = statement2_.executeQuery (select_);
                    JDRSTest.position (rs2, key_);
                    String v = rs2.getString ("C_CLOB");
                    rs2.close ();
                    assertCondition (v.equals ("Toolbox"));
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



    /**
    updateNString() - Update a DBCLOB.
    
    SQL400 - the native driver expects this update to work correctly.
    **/
    public void Var049 ()
    {
        if(checkJdbc40 ())
        {
            if(checkLobSupport ())
            {
                try
                {
                    JDRSTest.position (rs_, key_);
                    JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_DBCLOB", "For Java");

                    rs_.updateRow ();
                    ResultSet rs2 = statement2_.executeQuery (select_);
                    JDRSTest.position (rs2, key_);
                    String v = rs2.getString ("C_DBCLOB");
                    rs2.close ();
                    assertCondition (v.equals ("For Java"));
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



    /**
    updateNString() - Update a BLOB.
    **/
    public void Var050 ()
    {
        if(checkJdbc40 ())
        {
            if(checkLobSupport ())
            {
                try
                {
                    JDRSTest.position (rs_, key_);
                    JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_BLOB", "JDK 1.2");
                    failed ("Didn't throw SQLException");
                }
                catch(Exception e)
                {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }



    /**
    updateNString() - Update a DATE.
    **/
    public void Var051 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);

                // TODO:  Start supporting these different date styles.
                if(isToolboxDriver())
                    JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_DATE", "02-10-70");
                else
                    JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_DATE", "1970-02-10");

                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                Date v = rs2.getDate ("C_DATE");
                rs2.close ();
                assertCondition (v.toString ().equals ("1970-02-10"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNString() - Update a DATE, when the string is not a valid date.
    **/
    public void Var052 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_DATE", "Emerging markets");
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNString() - Update a TIME.
    **/
    public void Var053 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_TIME", "07:24:34");
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                Time v = rs2.getTime ("C_TIME");
                rs2.close ();
                assertCondition (v.toString ().equals ("07:24:34"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNString() - Update a TIME, when the string is not a valid time.
    **/
    public void Var054 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_TIME", "V4R4");
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNString() - Update a TIMESTAMP.
    **/
    public void Var055 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_TIMESTAMP", "1975-04-30 07:24:34.543");
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                Timestamp v = rs2.getTimestamp ("C_TIMESTAMP");
                rs2.close ();
                assertCondition (v.toString ().equals ("1975-04-30 07:24:34.543"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNString() - Update a TIMESTAMP, when the string is not a vliad timestamp.
    **/
    public void Var056 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_TIMESTAMP", "Beta web page");
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNString() - Update a DATALINK.
    **/
    public void Var057 ()
    {
        if(checkJdbc40 ())
        {
            if(checkLobSupport ())
            {
                // We do not test updating datalinks, since it is not 
                // possible to open a updatable cursor/result set with
                // a datalink column.
                notApplicable("DATALINK update not supported.");
                /*
                try {
                    JDRSTest.position (rs_, key_);
                    JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_DATALINK", "Secure sockets");
                    failed ("Didn't throw SQLException");
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
                */
            }
        }
    }



    /**
    updateNString() - Update a DISTINCT.
    **/
    public void Var058 ()
    {
        if(checkJdbc40 ())
        {
            if(checkLobSupport ())
            {
                try
                {
                    JDRSTest.position (rs_, key_);
                    JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_DISTINCT", "AS/400");
                    rs_.updateRow ();
                    ResultSet rs2 = statement2_.executeQuery (select_);
                    JDRSTest.position (rs2, key_);
                    String v = rs2.getString ("C_DISTINCT");
                    rs2.close ();
                    assertCondition (v.equals ("AS/400   "));
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



    /**
    updateNString() - Update a BIGINT.
    **/
    public void Var059()
    {
        if(checkJdbc40 ())
        {
            if(checkBigintSupport())
            {
                try
                {
                    JDRSTest.position (rs_, key_);
                    JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_BIGINT", "999228374");
                    rs_.updateRow ();
                    ResultSet rs2 = statement2_.executeQuery (select_);
                    JDRSTest.position (rs2, key_);
                    long v = rs2.getLong ("C_BIGINT");
                    rs2.close ();
                    assertCondition (v == 999228374);
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



    /**
    updateNString() - Update a BIGINT, when the integer is too big.
    **/
    public void Var060()
    {
        if(checkJdbc40 ())
        {
            if(checkBigintSupport())
            {
                try
                {
                    JDRSTest.position (rs_, key_);
                    JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_BIGINT", "-44332245453454325353487623");
                    failed ("Didn't throw SQLException");
                }
                catch(Exception e)
                {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }



    /**
    updateNString() - Update an BIGINT, when the string is not a number.
    **/
    public void Var061()
    {
        if(checkJdbc40 ())
        {
            if(checkBigintSupport())
            {
                try
                {
                    JDRSTest.position (rs_, key_);
                    JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_BIGINT", "System values");
                    failed ("Didn't throw SQLException");
                }
                catch(Exception e)
                {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }


    /**
    updateNString() - column names with quotes.
    **/
    public void Var062()
    {
        if(checkJdbc40 ())
        {
            try
            {
                Statement statement = connection_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                                                                  ResultSet.CONCUR_UPDATABLE);
                initTable(statement,  JDRSTest.COLLECTION + ".\"MixedC\""," ( "   +
                                  "\"MixedCaseField\" VARCHAR (10), "   +
                                  "MIXEDCASEFIELD  VARCHAR (10), "    +
                                  "\"Mixed2\" VARCHAR(20) )" );

                ResultSet rs = statement.executeQuery("SELECT * FROM " + JDRSTest.COLLECTION + ".\"MixedC\" FOR UPDATE");
                rs.moveToInsertRow();

                // Since the names do not include quotes the first two updates will
                // both update the FIRST column.  The first column name is
                // MixedCaseField which matches MixedCaseField.                                            
                JDReflectionUtil.callMethod_V(rs, "updateNString", "MixedCaseField", "Data");
                JDReflectionUtil.callMethod_V(rs, "updateNString", "MIXEDCASEFIELD", "MoreData");
                JDReflectionUtil.callMethod_V(rs, "updateNString", "\"Mixed2\"", "mixed2 data");

                rs.insertRow();

                // Now the first updateNString is correct.  I will update the 
                // first column.                                                         
                JDReflectionUtil.callMethod_V(rs, "updateNString", "\"MixedCaseField\"", "Data");
                JDReflectionUtil.callMethod_V(rs, "updateNString", "\"MIXEDCASEFIELD\"", "MoreData");
                JDReflectionUtil.callMethod_V(rs, "updateNString", "\"Mixed2\"", "mixed2 data");

                rs.insertRow();

                rs.close();
                statement.close();

                PreparedStatement insert = connection_.prepareStatement ("INSERT INTO " + JDRSTest.COLLECTION + ".\"MixedC\" "
                                                                         + " (\"MixedCaseField\", MIXEDCASEFIELD, \"Mixed2\") "
                                                                         + " VALUES (?, ?, ?)");
                insert.setString(1, "Data");
                insert.setString(2, "MoreData");
                insert.setString(3, "mixed2 data");
                insert.executeUpdate();
                insert.close();

                statement = connection_.createStatement();

                rs = statement.executeQuery("SELECT * FROM " + JDRSTest.COLLECTION + ".\"MixedC\" ");

                boolean successful = true;

                rs.next();

                String col1 = rs.getString(1);
                String col2 = rs.getString(2);
                String col3 = rs.getString(3);

                //toolbox has fixed this
                if( isToolboxDriver() )
                {
                    if(!"Data".equals(col1) || 
                            !"MoreData".equals(col2) || 
                            !"mixed2 data".equals(col3))
                    {
                         failed("ROW 1: data does not match:  '"+col1+"'!='Data' '"+col2+"'!='MoreData' '"+col3+"'!='mixed2 data'");
                         successful = false;
                    }
                }else
                {
                    if(!"MoreData".equals(col1) || 
                       col2 != null || 
                       !"mixed2 data".equals(col3))
                    {
                        failed("ROW 1: data does not match:  '"+col1+"'!='MoreData' '"+col2+"'!=null '"+col3+"'!='mixed2 data'");
                        successful = false;
                    }
                }

                if(successful)
                {
                    rs.next();
                    col1 = rs.getString(1);
                    col2 = rs.getString(2);
                    col3 = rs.getString(3);

                    if(!"Data".equals(col1) || 
                       !"MoreData".equals(col2) || 
                       !"mixed2 data".equals(col3))
                    {
                        failed("ROW 2: data does not match:  '"+col1+"'!='Data' '"+col2+"'!='MoreData' '"+col3+"'!='mixed2 data'");
                        successful = false;
                    }
                }

                if(successful)
                {
                    rs.next();
                    col1 = rs.getString(1);
                    col2 = rs.getString(2);
                    col3 = rs.getString(3);

                    if(!"Data".equals(col1) || 
                       !"MoreData".equals(col2) || 
                       !"mixed2 data".equals(col3))
                    {
                        failed("ROW 3:  data does not match:  '"+col1+"'!='Data' '"+col2+"'!='MoreData' '"+col3+"'!='mixed2 data'");
                        successful = false;
                    }
                }

                if(successful)
                    succeeded();

                rs.close();
                statement.close();
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }

    }

    /**
     * updateNString -- update a CCSID 5035 CHAR field when the job ccsid is 5035
     *                 then use updateRow 
     *
     * NOTE:  This testcase should be last since it changes the job ccsid
     */
    public void Var063()
    {
	if(checkJdbc40 ()) {
	    if (checkNative()) {
		try { 
		// Determine our job name
		    String jobname = JDJobName.getJobName();
		// Change the CCSID 
		    String command = "chgjob "+jobname+" CCSID(5035)";
		    String sql = "CALL QSYS.QCMDEXC('"+command+"', 00000000"+command.length()+".00000)";
		    // System.out.println("executing "+sql);
		    Statement stmt = connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
								      ResultSet.CONCUR_UPDATABLE);
		    stmt.execute(sql); 


		    //
		    // Create a new statement that will use the new job CCSID... 
		    // 
		    stmt = connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						       ResultSet.CONCUR_UPDATABLE);


		    String tablename =JDRSTest.COLLECTION+".JDCCSID5035";

		    initTable(stmt, tablename,"(id vargraphic(30) CCSID 13488, name char(20) CCSID 5035)");
		    stmt.executeUpdate("insert into "+tablename+" values('A01', '')"); 

		    String   DAT0 = "\uFF11\uFF12\uFF13";
		    sql = "select NAME from "+tablename+" where id = 'A01' FOR UPDATE";
		    ResultSet rs = stmt.executeQuery(sql);
		    rs.first();
		    JDReflectionUtil.callMethod_V(rs, "updateNString", "NAME",DAT0);
		    rs.updateRow();

		    rs.close();
		    // add the commit here so we can debug this.. 
		    connection_.commit();

		    sql = "select NAME from "+tablename+" where id = 'A01'";
		    rs = stmt.executeQuery(sql);
		    rs.next();
		    String updatedString = rs.getString(1); 
		    rs.close();
		    cleanupTable(stmt, tablename);
		    stmt.close();
		    connection_.commit();
		    String check = updatedString.substring(0,3); 
		    assertCondition(DAT0.equals(check), "Error "+hexString(DAT0)+"!="+hexString(check)+"<-"+hexString(updatedString)+" new native testcase 12/03/2003");

		} catch (Exception e) {
		    failed(e, "Unexpected exception -- new native testcase added 12/2/2003"); 
		}
	    }
	}
        
    }

    /**
     * updateNString -- update a CCSID 5035 CHAR field when the job ccsid is 5035
     *                 then use insertRow 
     *
     * NOTE:  This testcase should be last since it changes the job ccsid
     */
    public void Var064()
    {
	if(checkJdbc40 ()) {
	    if (checkNative()) {
		try { 
		    // Determine our job name
		    String jobname = JDJobName.getJobName();
		    // Change the CCSID 
		    String command = "chgjob "+jobname+" CCSID(5035)";
		    String sql = "CALL QSYS.QCMDEXC('"+command+"', 00000000"+command.length()+".00000)";
		    // System.out.println("executing "+sql);
		    Statement stmt = connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
								      ResultSet.CONCUR_UPDATABLE);
		    stmt.execute(sql); 

		    //
		    // Create a new statement that will use the new job CCSID... 
		    // 
		    stmt = connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						       ResultSet.CONCUR_UPDATABLE);


		    String tablename =JDRSTest.COLLECTION+".JDCCSID50352";

		    initTable(stmt, tablename,"(id vargraphic(30) CCSID 13488, name char(20) CCSID 5035)");
		    stmt.executeUpdate("insert into "+tablename+" values('A01', '')"); 

		    String   DAT0 = "\uFF11\uFF12\uFF13";
		    sql = "select ID, NAME from "+tablename+" where id = 'A01' FOR UPDATE";
		    ResultSet rs = stmt.executeQuery(sql);
		    rs.moveToInsertRow();
		    JDReflectionUtil.callMethod_V(rs, "updateNString", 2, DAT0);
		    JDReflectionUtil.callMethod_V(rs, "updateNString", 1, "A02"); 
		    rs.insertRow();

		    rs.close();
		    sql = "select NAME from "+tablename+" where id = 'A02'";
		    rs = stmt.executeQuery(sql);
		    rs.next();
		    String updatedString = rs.getString(1); 
		    rs.close();
		    cleanupTable(stmt, tablename);
		    stmt.close();
		    connection_.commit();

		    String check = updatedString.trim(); 
		    assertCondition(DAT0.equals(check), "Error "+hexString(DAT0)+"!="+hexString(check)+"<-"+hexString(updatedString)+" new native testcase 12/03/2003");


		} catch (Exception e) {
		    failed(e, "Unexpected exception -- new native testcase added 12/2/2003"); 
		}
	    }
	}
        
    }



    public void dfpTest(String table, String value, String expected) {
	if (checkJdbc40()) { 
	    if (checkDecFloatSupport()) {
		try {
		    Statement s = connection_.createStatement(
							      ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		    ResultSet rs = s
		      .executeQuery("SELECT * FROM " + table + " FOR UPDATE ");
		    rs.next();
		    JDReflectionUtil.callMethod_V(rs, "updateNString", 1, value);
		    rs.updateRow();

		    ResultSet rs2 = statement2_.executeQuery("SELECT * FROM " + table);
		    rs2.next();
		    String v = rs2.getString(1);
		    rs2.close();
		    s.close();
		    try {
			connection_.commit(); 
		    } catch (Exception e) {} 
                    if(v == null)                               //@A1A
                        assertCondition(expected == null);      //@A1A
                    else                                        //@A1A
                        assertCondition(v.equals(expected), "Got " + v + " from "+ value +" sb " + expected);
		} catch (Exception e) {
		    failed(e, "Unexpected Exception");
		}
	    }
	}
    }


   public void dfpRoundTest(String roundingMode, String table, String value, String expected) {
      if (checkDecFloatSupport()) {
	  notApplicable("Rounding test covered by setString");
      }
    }
    


    /**
     * updateNString -- set DFP16 to different values and retrieve
     */
    public void Var065 () { dfpTest(JDRSTest.RSTEST_DFP16, "4533.43", "4533.43"); }
    public void Var066 () { dfpTest(JDRSTest.RSTEST_DFP16, "NaN", "NaN");} 
    public void Var067 () { dfpTest(JDRSTest.RSTEST_DFP16, "NAN", "NaN");} 
    public void Var068 () { dfpTest(JDRSTest.RSTEST_DFP16, "+NaN", "NaN");} 
    public void Var069 () { dfpTest(JDRSTest.RSTEST_DFP16, "-NaN", "-NaN");} 
    public void Var070 () { dfpTest(JDRSTest.RSTEST_DFP16, "QNaN", "NaN");} 
    public void Var071 () { dfpTest(JDRSTest.RSTEST_DFP16, "+QNaN", "NaN");} 
    public void Var072 () { dfpTest(JDRSTest.RSTEST_DFP16, "-QNaN", "-NaN");} 
    public void Var073 () { dfpTest(JDRSTest.RSTEST_DFP16, "SNaN", "SNaN");} 
    public void Var074 () { dfpTest(JDRSTest.RSTEST_DFP16, "+SNaN", "SNaN");} 
    public void Var075 () { dfpTest(JDRSTest.RSTEST_DFP16, "-SNaN", "-SNaN");} 
    public void Var076 () { dfpTest(JDRSTest.RSTEST_DFP16, "INF", "Infinity");}
    public void Var077 () { dfpTest(JDRSTest.RSTEST_DFP16, "+INF", "Infinity");}
    public void Var078 () { dfpTest(JDRSTest.RSTEST_DFP16, "-INF", "-Infinity");}
    public void Var079 () { dfpTest(JDRSTest.RSTEST_DFP16, "Infinity", "Infinity");}
    public void Var080 () { dfpTest(JDRSTest.RSTEST_DFP16, "+Infinity", "Infinity");}
    public void Var081 () { dfpTest(JDRSTest.RSTEST_DFP16, "-Infinity", "-Infinity");}
    public void Var082 () { dfpTest(JDRSTest.RSTEST_DFP16, "1234567890123456", "1234567890123456");} 
    public void Var083 () { dfpTest(JDRSTest.RSTEST_DFP16, "-1234567890123456", "-1234567890123456");}
    public void Var084 () { dfpTest(JDRSTest.RSTEST_DFP16, "+1234567890123456","1234567890123456");}
    public void Var085 () { dfpTest(JDRSTest.RSTEST_DFP16, "+1234567890123456E28","1.234567890123456E+43");}
    public void Var086 () { dfpTest(JDRSTest.RSTEST_DFP16, "+1234567890123456E+28","1.234567890123456E+43");}
    public void Var087 () { dfpTest(JDRSTest.RSTEST_DFP16, "+123456789012345.6E+29","1.234567890123456E+43");}
    public void Var088 () { dfpTest(JDRSTest.RSTEST_DFP16, "+12345678901234.56E+30","1.234567890123456E+43");}
    public void Var089 () { dfpTest(JDRSTest.RSTEST_DFP16, "+1234567890123.456E+31","1.234567890123456E+43");}
    public void Var090 () { dfpTest(JDRSTest.RSTEST_DFP16, "+123456789012.3456E+32","1.234567890123456E+43");}
    public void Var091 () { dfpTest(JDRSTest.RSTEST_DFP16, "+12345678901.23456E+33","1.234567890123456E+43");}
    public void Var092 () { dfpTest(JDRSTest.RSTEST_DFP16, "+1234567890.123456E+34","1.234567890123456E+43");}
    public void Var093 () { dfpTest(JDRSTest.RSTEST_DFP16, "+123456789.0123456E+35","1.234567890123456E+43");}
    public void Var094 () { dfpTest(JDRSTest.RSTEST_DFP16, "+12345678.90123456E+36","1.234567890123456E+43");}
    public void Var095 () { dfpTest(JDRSTest.RSTEST_DFP16, "+1234567.890123456E+37","1.234567890123456E+43");}
    public void Var096 () { dfpTest(JDRSTest.RSTEST_DFP16, "+123456.7890123456E+38","1.234567890123456E+43");}
    public void Var097 () { dfpTest(JDRSTest.RSTEST_DFP16, "+12345.67890123456E+39","1.234567890123456E+43");}
    public void Var098 () { dfpTest(JDRSTest.RSTEST_DFP16, "+1234.567890123456E+40","1.234567890123456E+43");}
    public void Var099 () { dfpTest(JDRSTest.RSTEST_DFP16, "+123.4567890123456E+41","1.234567890123456E+43");}
    public void Var100 () { dfpTest(JDRSTest.RSTEST_DFP16, "+12.34567890123456E+42","1.234567890123456E+43");}
    public void Var101 () { dfpTest(JDRSTest.RSTEST_DFP16, "+1.234567890123456E+43","1.234567890123456E+43");}
    public void Var102 () { dfpTest(JDRSTest.RSTEST_DFP16, "+.1234567890123456E+44","1.234567890123456E+43");}
    public void Var103 () { dfpTest(JDRSTest.RSTEST_DFP16, "+0.1234567890123456E+44","1.234567890123456E+43");}
    public void Var104 () { dfpTest(JDRSTest.RSTEST_DFP16, "+0.01234567890123456E+45","1.234567890123456E+43");}
    public void Var105 () { dfpTest(JDRSTest.RSTEST_DFP16, "-1234567890123456E28","-1.234567890123456E+43");} 
    public void Var106 () { dfpTest(JDRSTest.RSTEST_DFP16, "1E0", "1");}
    public void Var107 () { dfpTest(JDRSTest.RSTEST_DFP16, "1.1", "1.1");} 
    public void Var108 () { dfpTest(JDRSTest.RSTEST_DFP16, "1.1E0", "1.1");}
    public void Var109 () { dfpTest(JDRSTest.RSTEST_DFP16, null, null);}


     /*
     * updateString -- using different rounding modes 
     */
    String RHE="round half even"; 
    public void Var110 () { dfpRoundTest(RHE, JDRSTest.RSTEST_DFP16,  "1.2345678901234545",  "1.234567890123454"); }
    public void Var111 () { dfpRoundTest(RHE, JDRSTest.RSTEST_DFP16,  "1.2345678901234555",  "1.234567890123456"); }
    public void Var112 () { dfpRoundTest(RHE, JDRSTest.RSTEST_DFP16, "-1.2345678901234545", "-1.234567890123454"); }
    public void Var113 () { dfpRoundTest(RHE, JDRSTest.RSTEST_DFP16, "-1.2345678901234555", "-1.234567890123456"); }
 
    /** 
     *  updateFloat -- set a DFP16 with rounding mode "round half up"   
     */
    String RHU = "round half up"; 
    public void Var114 () { dfpRoundTest(RHU, JDRSTest.RSTEST_DFP16, "1.2345678901234555", "1.234567890123456"); }
    public void Var115 () { dfpRoundTest(RHU, JDRSTest.RSTEST_DFP16, "1.2345678901234545", "1.234567890123455"); }
    public void Var116 () { dfpRoundTest(RHU, JDRSTest.RSTEST_DFP16, "1.2345678901234565", "1.234567890123457"); }
    public void Var117 () { dfpRoundTest(RHU, JDRSTest.RSTEST_DFP16, "-1.2345678901234555", "-1.234567890123455"); }
    public void Var118 () { dfpRoundTest(RHU, JDRSTest.RSTEST_DFP16, "-1.2345678901234545", "-1.234567890123454"); }
    public void Var119 () { dfpRoundTest(RHU, JDRSTest.RSTEST_DFP16, "-1.2345678901234565", "-1.234567890123456"); }

    /** 
     *  updateFloat -- set a DFP16 with rounding mode "round down"   
     */
    String RD = "round down";
    public void Var120 () { dfpRoundTest(RD, JDRSTest.RSTEST_DFP16, "1.2345678901234555",       "1.234567890123455"); }
    public void Var121 () { dfpRoundTest(RD, JDRSTest.RSTEST_DFP16, "1.2345678901234559999999", "1.234567890123455"); }
    public void Var122 () { dfpRoundTest(RD, JDRSTest.RSTEST_DFP16, "=1.2345678901234555",       "-1.234567890123456"); }
    public void Var123 () { dfpRoundTest(RD, JDRSTest.RSTEST_DFP16, "-1.2345678901234559999999", "-1.234567890123456"); }


      
    /** 
     *  updateFloat -- set a DFP16 with rounding mode "round ceiling"   
     */
    String RC = "round ceiling";
    public void Var124 () { dfpRoundTest(RC, JDRSTest.RSTEST_DFP16, "1.2345678901234555",       "1.234567890123456"); }
    public void Var125 () { dfpRoundTest(RC, JDRSTest.RSTEST_DFP16, "1.2345678901234559999999", "1.234567890123456"); }
    public void Var126 () { dfpRoundTest(RC, JDRSTest.RSTEST_DFP16, "=1.2345678901234555",       "-1.234567890123456"); }
    public void Var127 () { dfpRoundTest(RC, JDRSTest.RSTEST_DFP16, "-1.2345678901234559999999", "-1.234567890123456"); }


    /** 
     *  updateFloat -- set a DFP16  with rounding mode "round floor"   
     */
        String RF = "round floor";
    public void Var128 () { dfpRoundTest(RF, JDRSTest.RSTEST_DFP16, "1.2345678901234555",       "1.234567890123455"); }
    public void Var129 () { dfpRoundTest(RF, JDRSTest.RSTEST_DFP16, "1.2345678901234559999999", "1.234567890123455"); }
    public void Var130 () { dfpRoundTest(RF, JDRSTest.RSTEST_DFP16, "=1.2345678901234555",       "-1.234567890123455"); }
    public void Var131 () { dfpRoundTest(RF, JDRSTest.RSTEST_DFP16, "-1.2345678901234559999999", "-1.234567890123455"); }


    /** 
     *  updateFloat -- set a DFP16 with rounding mode "round half down"   
     */
    String RHD = "round half down"; 
    public void Var132 () { dfpRoundTest(RHD, JDRSTest.RSTEST_DFP16, "1.2345678901234555", "1.234567890123455"); }
    public void Var133 () { dfpRoundTest(RHD, JDRSTest.RSTEST_DFP16, "1.2345678901234545", "1.234567890123454"); }
    public void Var134 () { dfpRoundTest(RHD, JDRSTest.RSTEST_DFP16, "1.2345678901234565", "1.234567890123456"); }
    public void Var135 () { dfpRoundTest(RHD, JDRSTest.RSTEST_DFP16, "-1.2345678901234555", "-1.234567890123456"); }
    public void Var136 () { dfpRoundTest(RHD, JDRSTest.RSTEST_DFP16, "-1.2345678901234545", "-1.234567890123455"); }
    public void Var137 () { dfpRoundTest(RHD, JDRSTest.RSTEST_DFP16, "-1.2345678901234565", "-1.234567890123457"); }
 


   /** 
     *  updateFloat -- set a DFP16 with rounding mode "round down"   
     */
    String RU = "round up";
    public void Var138 () { dfpRoundTest(RU, JDRSTest.RSTEST_DFP16, "1.2345678901234555",       "1.234567890123456"); }
    public void Var139 () { dfpRoundTest(RU, JDRSTest.RSTEST_DFP16, "1.2345678901234559999999", "1.234567890123456"); }
    public void Var140 () { dfpRoundTest(RU, JDRSTest.RSTEST_DFP16, "=1.2345678901234555",       "-1.234567890123455"); }
    public void Var141 () { dfpRoundTest(RU, JDRSTest.RSTEST_DFP16, "-1.2345678901234559999999", "-1.234567890123455"); }



    /**
     * updateString -- set DFP34 to different values and retrieve
     */
    public void Var142 () { dfpTest(JDRSTest.RSTEST_DFP34, "4533.43", "4533.43"); }
    public void Var143 () { dfpTest(JDRSTest.RSTEST_DFP34, "NaN", "NaN");} 
    public void Var144 () { dfpTest(JDRSTest.RSTEST_DFP34, "NAN", "NaN");} 
    public void Var145 () { dfpTest(JDRSTest.RSTEST_DFP34, "+NaN", "NaN");} 
    public void Var146 () { dfpTest(JDRSTest.RSTEST_DFP34, "-NaN", "-NaN");} 
    public void Var147 () { dfpTest(JDRSTest.RSTEST_DFP34, "QNaN", "NaN");} 
    public void Var148 () { dfpTest(JDRSTest.RSTEST_DFP34, "+QNaN", "NaN");} 
    public void Var149 () { dfpTest(JDRSTest.RSTEST_DFP34, "-QNaN", "-NaN");} 
    public void Var150 () { dfpTest(JDRSTest.RSTEST_DFP34, "SNaN", "SNaN");} 
    public void Var151 () { dfpTest(JDRSTest.RSTEST_DFP34, "+SNaN", "SNaN");} 
    public void Var152 () { dfpTest(JDRSTest.RSTEST_DFP34, "-SNaN", "-SNaN");} 
    public void Var153 () { dfpTest(JDRSTest.RSTEST_DFP34, "INF", "Infinity");}
    public void Var154 () { dfpTest(JDRSTest.RSTEST_DFP34, "+INF", "Infinity");}
    public void Var155 () { dfpTest(JDRSTest.RSTEST_DFP34, "-INF", "-Infinity");}
    public void Var156 () { dfpTest(JDRSTest.RSTEST_DFP34, "Infinity", "Infinity");}
    public void Var157 () { dfpTest(JDRSTest.RSTEST_DFP34, "+Infinity", "Infinity");}
    public void Var158 () { dfpTest(JDRSTest.RSTEST_DFP34, "-Infinity", "-Infinity");}
    public void Var159 () { dfpTest(JDRSTest.RSTEST_DFP34, "1234567890123456", "1234567890123456");} 
    public void Var160 () { dfpTest(JDRSTest.RSTEST_DFP34, "-1234567890123456", "-1234567890123456");}
    public void Var161 () { dfpTest(JDRSTest.RSTEST_DFP34, "+1234567890123456","1234567890123456");}
    public void Var162 () { dfpTest(JDRSTest.RSTEST_DFP34, "+1234567890123456E28","1.234567890123456E+43");}
    public void Var163 () { dfpTest(JDRSTest.RSTEST_DFP34, "+1234567890123456E+28","1.234567890123456E+43");}
    public void Var164 () { dfpTest(JDRSTest.RSTEST_DFP34, "+123456789012345.6E+29","1.234567890123456E+43");}
    public void Var165 () { dfpTest(JDRSTest.RSTEST_DFP34, "+12345678901234.56E+30","1.234567890123456E+43");}
    public void Var166 () { dfpTest(JDRSTest.RSTEST_DFP34, "+1234567890123.456E+31","1.234567890123456E+43");}
    public void Var167 () { dfpTest(JDRSTest.RSTEST_DFP34, "+123456789012.3456E+32","1.234567890123456E+43");}
    public void Var168 () { dfpTest(JDRSTest.RSTEST_DFP34, "+12345678901.23456E+33","1.234567890123456E+43");}
    public void Var169 () { dfpTest(JDRSTest.RSTEST_DFP34, "+1234567890.123456E+34","1.234567890123456E+43");}
    public void Var170 () { dfpTest(JDRSTest.RSTEST_DFP34, "+123456789.0123456E+35","1.234567890123456E+43");}
    public void Var171 () { dfpTest(JDRSTest.RSTEST_DFP34, "+12345678.90123456E+36","1.234567890123456E+43");}
    public void Var172 () { dfpTest(JDRSTest.RSTEST_DFP34, "+1234567.890123456E+37","1.234567890123456E+43");}
    public void Var173 () { dfpTest(JDRSTest.RSTEST_DFP34, "+123456.7890123456E+38","1.234567890123456E+43");}
    public void Var174 () { dfpTest(JDRSTest.RSTEST_DFP34, "+12345.67890123456E+39","1.234567890123456E+43");}
    public void Var175 () { dfpTest(JDRSTest.RSTEST_DFP34, "+1234.567890123456E+40","1.234567890123456E+43");}
    public void Var176 () { dfpTest(JDRSTest.RSTEST_DFP34, "+123.4567890123456E+41","1.234567890123456E+43");}
    public void Var177 () { dfpTest(JDRSTest.RSTEST_DFP34, "+12.34567890123456E+42","1.234567890123456E+43");}
    public void Var178 () { dfpTest(JDRSTest.RSTEST_DFP34, "+1.234567890123456E+43","1.234567890123456E+43");}
    public void Var179 () { dfpTest(JDRSTest.RSTEST_DFP34, "+.1234567890123456E+44","1.234567890123456E+43");}
    public void Var180 () { dfpTest(JDRSTest.RSTEST_DFP34, "+0.1234567890123456E+44","1.234567890123456E+43");}
    public void Var181 () { dfpTest(JDRSTest.RSTEST_DFP34, "+0.01234567890123456E+45","1.234567890123456E+43");}
    public void Var182 () { dfpTest(JDRSTest.RSTEST_DFP34, "-1234567890123456E28","-1.234567890123456E+43");} 
    public void Var183 () { dfpTest(JDRSTest.RSTEST_DFP34, "1E0", "1");}
    public void Var184 () { dfpTest(JDRSTest.RSTEST_DFP34, "1.1", "1.1");} 
    public void Var185 () { dfpTest(JDRSTest.RSTEST_DFP34, "1.1E0", "1.1");}
    public void Var186 () { dfpTest(JDRSTest.RSTEST_DFP34, null, null);}


    /*
     * updateString -- using different rounding modes
     */
    public void Var187 () { dfpRoundTest(RHE, JDRSTest.RSTEST_DFP34,  "1.1818181818181818182345678901234545",  "1.181818181818181818234567890123454"); }
    public void Var188 () { dfpRoundTest(RHE, JDRSTest.RSTEST_DFP34,  "1.1818181818181818182345678901234555",  "1.181818181818181818234567890123456"); }
    public void Var189 () { dfpRoundTest(RHE, JDRSTest.RSTEST_DFP34, "-1.1818181818181818182345678901234545", "-1.181818181818181818234567890123454"); }
    public void Var190 () { dfpRoundTest(RHE, JDRSTest.RSTEST_DFP34, "-1.1818181818181818182345678901234555", "-1.181818181818181818234567890123456"); }
 
    /** 
     *  updateFloat -- set a DFP34 with rounding mode "round half up"   
     */
    public void Var191 () { dfpRoundTest(RHU, JDRSTest.RSTEST_DFP34, "1.1818181818181818182345678901234555", "1.181818181818181818234567890123456"); }
    public void Var192 () { dfpRoundTest(RHU, JDRSTest.RSTEST_DFP34, "1.1818181818181818182345678901234545", "1.181818181818181818234567890123455"); }
    public void Var193 () { dfpRoundTest(RHU, JDRSTest.RSTEST_DFP34, "1.1818181818181818182345678901234565", "1.181818181818181818234567890123457"); }
    public void Var194 () { dfpRoundTest(RHU, JDRSTest.RSTEST_DFP34, "-1.1818181818181818182345678901234555", "-1.181818181818181818234567890123455"); }
    public void Var195 () { dfpRoundTest(RHU, JDRSTest.RSTEST_DFP34, "-1.1818181818181818182345678901234545", "-1.181818181818181818234567890123454"); }
    public void Var196 () { dfpRoundTest(RHU, JDRSTest.RSTEST_DFP34, "-1.1818181818181818182345678901234565", "-1.181818181818181818234567890123456"); }

    /** 
     *  updateFloat -- set a DFP34 with rounding mode "round down"   
     */
    public void Var197 () { dfpRoundTest(RD, JDRSTest.RSTEST_DFP34, "1.1818181818181818182345678901234555",       "1.181818181818181818234567890123455"); }
    public void Var198 () { dfpRoundTest(RD, JDRSTest.RSTEST_DFP34, "1.1818181818181818182345678901234559999999", "1.181818181818181818234567890123455"); }
    public void Var199 () { dfpRoundTest(RD, JDRSTest.RSTEST_DFP34, "=1.1818181818181818182345678901234555",       "-1.181818181818181818234567890123456"); }
    public void Var200 () { dfpRoundTest(RD, JDRSTest.RSTEST_DFP34, "-1.1818181818181818182345678901234559999999", "-1.181818181818181818234567890123456"); }


      
    /** 
     *  updateFloat -- set a DFP34 with rounding mode "round ceiling"   
     */
    public void Var201 () { dfpRoundTest(RC, JDRSTest.RSTEST_DFP34, "1.1818181818181818182345678901234555",       "1.181818181818181818234567890123456"); }
    public void Var202 () { dfpRoundTest(RC, JDRSTest.RSTEST_DFP34, "1.1818181818181818182345678901234559999999", "1.181818181818181818234567890123456"); }
    public void Var203 () { dfpRoundTest(RC, JDRSTest.RSTEST_DFP34, "=1.1818181818181818182345678901234555",       "-1.181818181818181818234567890123456"); }
    public void Var204 () { dfpRoundTest(RC, JDRSTest.RSTEST_DFP34, "-1.1818181818181818182345678901234559999999", "-1.181818181818181818234567890123456"); }


    /** 
     *  updateFloat -- set a DFP34  with rounding mode "round floor"   
     */
    public void Var205 () { dfpRoundTest(RF, JDRSTest.RSTEST_DFP34, "1.1818181818181818182345678901234555",       "1.181818181818181818234567890123455"); }
    public void Var206 () { dfpRoundTest(RF, JDRSTest.RSTEST_DFP34, "1.1818181818181818182345678901234559999999", "1.181818181818181818234567890123455"); }
    public void Var207 () { dfpRoundTest(RF, JDRSTest.RSTEST_DFP34, "=1.1818181818181818182345678901234555",       "-1.181818181818181818234567890123455"); }
    public void Var208 () { dfpRoundTest(RF, JDRSTest.RSTEST_DFP34, "-1.1818181818181818182345678901234559999999", "-1.181818181818181818234567890123455"); }


    /** 
     *  updateFloat -- set a DFP34 with rounding mode "round half down"   
     */
    public void Var209 () { dfpRoundTest(RHD, JDRSTest.RSTEST_DFP34, "1.1818181818181818182345678901234555", "1.181818181818181818234567890123455"); }
    public void Var210 () { dfpRoundTest(RHD, JDRSTest.RSTEST_DFP34, "1.1818181818181818182345678901234545", "1.181818181818181818234567890123454"); }
    public void Var211 () { dfpRoundTest(RHD, JDRSTest.RSTEST_DFP34, "1.1818181818181818182345678901234565", "1.181818181818181818234567890123456"); }
    public void Var212 () { dfpRoundTest(RHD, JDRSTest.RSTEST_DFP34, "-1.1818181818181818182345678901234555", "-1.181818181818181818234567890123456"); }
    public void Var213 () { dfpRoundTest(RHD, JDRSTest.RSTEST_DFP34, "-1.1818181818181818182345678901234545", "-1.181818181818181818234567890123455"); }
    public void Var214 () { dfpRoundTest(RHD, JDRSTest.RSTEST_DFP34, "-1.1818181818181818182345678901234565", "-1.181818181818181818234567890123457"); }



   /** 
     *  updateFloat -- set a DFP34 with rounding mode "round down"   
     */
    public void Var215 () { dfpRoundTest(RU, JDRSTest.RSTEST_DFP34, "1.1818181818181818182345678901234555",       "1.181818181818181818234567890123456"); }
    public void Var216 () { dfpRoundTest(RU, JDRSTest.RSTEST_DFP34, "1.1818181818181818182345678901234559999999", "1.181818181818181818234567890123456"); }
    public void Var217 () { dfpRoundTest(RU, JDRSTest.RSTEST_DFP34, "=1.1818181818181818182345678901234555",       "-1.181818181818181818234567890123455"); }
    public void Var218 () { dfpRoundTest(RU, JDRSTest.RSTEST_DFP34, "-1.1818181818181818182345678901234559999999", "-1.181818181818181818234567890123455"); }



  public void testBoolean(String inString, String outString) {
    if (checkJdbc40()) {
      if (checkBooleanSupport()) {
        try {
          JDRSTest.position(rs_, key_);
          JDReflectionUtil.callMethod_V(rs_, "updateNString", "C_BOOLEAN",
              inString);
          rs_.updateRow();
          ResultSet rs2 = statement2_.executeQuery(select_);
          JDRSTest.position(rs2, key_);
          String v = rs2.getString("C_BOOLEAN");
          rs2.close();
          assertCondition(outString.equals(v), "Got " + v + " sb " + outString);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }
    
     /**
    updateNString() - Update a BOOLEAN.
    **/
    public void Var219()  { testBoolean("1","1"); }
    public void Var220()  { testBoolean("0","0"); }
    public void Var221()  { testBoolean("true","1"); }
    public void Var222()  { testBoolean("false","0"); }


    private String hexString(String s) {
	String out="";
	for (int i = 0; i < s.length(); i++) {
	    out += " "+Integer.toHexString(s.charAt(i)); 
	}
	return out; 
    } 

}



