///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSUpdateNCharacterStream.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.RS;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.DataTruncation;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDRSTest;
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;
import test.JD.JDWeirdReader;



/**
Testcase JDRSUpdateNCharacterStream.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>updateNCharacterStream()
</ul>
**/
public class JDRSUpdateNCharacterStream
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSUpdateNCharacterStream";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



    // Private data.
    private static final String key_            = "JDRSUpdateNCharacterStream";
    private static String select_         = "SELECT * FROM "
                                            + JDRSTest.RSTEST_UPDATE;

    private Statement           statement_;
    private Statement           statement2_;
    private ResultSet           rs_;



    /**
    Constructor.
    **/
    public JDRSUpdateNCharacterStream (AS400 systemObject,
                                      Hashtable<String,Vector<String>> namesAndVars,
                                      int runMode,
                                      FileOutputStream fileOutputStream,
                                      
                                      String password)
    {
        super (systemObject, "JDRSUpdateNCharacterStream",
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
            String url = baseURL_
                         
                         
                         + ";date format=iso"
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
    updateNCharacterStream() - Should throw exception when the result set is
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
                JDReflectionUtil.callMethod_V(rs, "updateNCharacterStream", "C_VARCHAR_50", new StringReader ("Juneau"), 6L);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNCharacterStream() - Should throw exception when the result set is
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
                JDReflectionUtil.callMethod_V(rs, "updateNCharacterStream", "C_VARCHAR_50", new StringReader ("Tacoma"), 6L);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNCharacterStream() - Should throw exception when cursor is not pointing
    to a row.
    **/
    public void Var003()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, null);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_VARCHAR_50", new StringReader ("Portland"), 8L);
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
    updateNCharacterStream() - Should throw an exception when the column
    is an invalid index.
    **/
    public void Var004()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", 100, new StringReader ("San Francisco"), 13L);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNCharacterStream() - Should throw an exception when the column
    is 0.
    **/
    public void Var005()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", 0, new StringReader ("Boise"), 5L);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNCharacterStream() - Should throw an exception when the column
    is -1.
    **/
    public void Var006()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", -1, new StringReader ("Helena"), 6L);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNCharacterStream() - Should work when the column index is valid.
    **/
    public void Var007()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", 14, new StringReader ("Fargo"), 5L);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_VARCHAR_50");
                rs2.close ();
                assertCondition (v.equals ("Fargo"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNCharacterStream() - Should throw an exception when the column
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
            JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", null, new StringReader ("Sioux Falls"), 11L);
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
    updateNCharacterStream() - Should throw an exception when the column
    name is an empty string.
    **/
    public void Var009()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "", new StringReader ("Lincoln"), 7L);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNCharacterStream() - Should throw an exception when the column
    name is invalid.
    **/
    public void Var010()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "INVALID", new StringReader ("Colorado Springs"), 16L);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNCharacterStream() - Should work when the column name is valid.
    **/
    public void Var011()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_VARCHAR_50", new StringReader ("Gillette"),8L);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_VARCHAR_50");
                rs2.close ();
                assertCondition (v.equals ("Gillette"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNCharacterStream() - Should update to SQL NULL when the column
    value is null.
    **/
    public void Var012()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_VARCHAR_50", null, 0L);
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
    updateNCharacterStream() - Should throw an exception when the length is invalid.
    **/
    public void Var013()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_VARCHAR_50", new StringReader ("Reno"), -1L);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNCharacterStream() - Should be reflected by get, even if update has
    not yet been issued (i.e. update is still pending).
    **/
    public void Var014()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_VARCHAR_50", new StringReader ("Santa Fe"),8L);
                assertCondition (rs_.getString ("C_VARCHAR_50").equals ("Santa Fe"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }




    /**
    updateNCharacterStream() - Should be reflected by get, after update has
    been issued, but cursor has not been repositioned.
    **/
    public void Var015()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_VARCHAR_50", new StringReader ("Phoenix"),7L);
                rs_.updateRow ();
                assertCondition (rs_.getString ("C_VARCHAR_50").equals ("Phoenix"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }




    /**
    updateNCharacterStream() - Should be reflected by get, after update has
    been issued and cursor has been repositioned.
    **/
    public void Var016()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_VARCHAR_50", new StringReader ("Austin"),6L);
                rs_.updateRow ();
                rs_.beforeFirst ();
                JDRSTest.position (rs_, key_);
                String v = rs_.getString ("C_VARCHAR_50");
                assertCondition (v.equals ("Austin"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }




    /**
    updateNCharacterStream() - Should work when the current row is the insert
    row.
    **/
    public void Var017()
    {
        if(checkJdbc40 ())
        {
            try
            {
                rs_.moveToInsertRow ();
                rs_.updateString ("C_KEY", "JDRSCharacterStream 1");
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_VARCHAR_50", new StringReader ("Tulsa"),5L);
                rs_.insertRow ();
                JDRSTest.position (rs_, "JDRSCharacterStream 1");
                assertCondition (rs_.getString ("C_VARCHAR_50").equals ("Tulsa"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNCharacterStream() - Should be reflected by get on insert row, even if
    insert has not yet been issued (i.e. insert is still pending).
    **/
    public void Var018()
    {
        if(checkJdbc40 ())
        {
            try
            {
                rs_.moveToInsertRow ();
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_VARCHAR_50", new StringReader ("Lawrence"),8L);
                assertCondition (rs_.getString ("C_VARCHAR_50").equals ("Lawrence"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNCharacterStream() - Should throw an exception on a deleted row.
    **/
    public void Var019()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, "DUMMY_ROW");
                rs_.deleteRow ();
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_VARCHAR_50", new StringReader ("Ames"),4L);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


    /**
    updateNCharacterStream() - Update a SMALLINT.
    **/
    public void Var020 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_SMALLINT", new StringReader ("31"),2L);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                short v = rs2.getShort ("C_SMALLINT");
                rs2.close ();
                assertCondition (v == 31);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNCharacterStream() - Update a SMALLINT, when the integer is too big.
    **/
    public void Var021 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_SMALLINT", new StringReader ("224323"),6L);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNCharacterStream() - Update a SMALLINT, when the string is not a number.
    **/
    public void Var022 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_SMALLINT", new StringReader ("Van Buren"),9L);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNCharacterStream() - Update an INTEGER.
    **/
    public void Var023 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_INTEGER", new StringReader ("-12374"),6L);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                int v = rs2.getInt ("C_INTEGER");
                rs2.close ();
                assertCondition (v == -12374);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNCharacterStream() - Update an INTEGER, when the integer is too big.
    **/
    public void Var024 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_INTEGER", new StringReader ("435353487623"),12L);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNCharacterStream() - Update an INTEGER, when the string is not a number.
    **/
    public void Var025 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_INTEGER", new StringReader ("Baton Rouge"), 11L);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNCharacterStream() - Update a REAL.
    **/
    public void Var026 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_REAL", new StringReader ("13.35"),5L);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                float v = rs2.getFloat ("C_REAL");
                rs2.close ();
                assertCondition (v == 13.35f);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }


    /**
    updateNCharacterStream() - Update a REAL, when the number is too big.
    
    Note:  Given the new data truncation rules for JDBC, this testcase
           is expected to work without throwing a DataTruncation exception.
    **/
    public void Var027 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_REAL", new StringReader ("453433335.4437623"),17L);
                rs_.updateRow ();

                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                float v = rs2.getFloat ("C_REAL");
                rs2.close ();
                // Does anyone have a good suggestion for what should be done here?
                assertCondition (true, ""+v);
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
                && (dt.getDataSize() == 15)
                && (dt.getTransferSize() == 10));

        }
        }
        */
    }



    /**
    updateNCharacterStream() - Update a REAL, when the string is not a number.
    **/
    public void Var028 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_INTEGER", new StringReader ("Edina"),5L);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }




    /**
    updateNCharacterStream() - Update a FLOAT.
    **/
    public void Var029 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_FLOAT", new StringReader ("9835.2"),6L);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                float v = rs2.getFloat ("C_FLOAT");
                rs2.close ();
                assertCondition (v == 9835.2f);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNCharacterStream() - Update a FLOAT, when the number is too big.
    
    Note:  Given the new data truncation rules for JDBC, this testcase
           is expected to work without throwing a DataTruncation exception.
    **/
    public void Var030 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_FLOAT", new StringReader ("453435.4434556337633"),20L);
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
    updateNCharacterStream() - Update a FLOAT, when the string is not a number.
    **/
    public void Var031 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_FLOAT", new StringReader ("Madison"),7L);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


    /**
    updateNCharacterStream() - Update a DOUBLE.
    **/
    public void Var032 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_DOUBLE", new StringReader ("-9845"),5L);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                double v = rs2.getDouble ("C_DOUBLE");
                rs2.close ();
                assertCondition (v == -9845);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNCharacterStream() - Update a DOUBLE, when the number is too big.
    
    Note:  Given the new data truncation rules for JDBC, this testcase
           is expected to work without throwing a DataTruncation exception.
    **/
    public void Var033 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_DOUBLE", new StringReader ("453342035.44445532333"), 21L);
                rs_.updateRow ();

                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                float v = rs2.getFloat ("C_DOUBLE");
                rs2.close ();
                // Does anyone have a good suggestion for what should be done here?
                assertCondition (true, ""+v);
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
    updateNCharacterStream() - Update a DOUBLE, when the string is not a number.
    **/
    public void Var034 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_DOUBLE", new StringReader ("Chicago"),7L);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


    /**
    updateNCharacterStream() - Update a DECIMAL.
    **/
    public void Var035 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_DECIMAL_105", new StringReader ("4533.43"), 7L);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                BigDecimal v = rs2.getBigDecimal ("C_DECIMAL_105");
                rs2.close ();
                assertCondition (v.floatValue() == 4533.43f);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNCharacterStream() - Update a DECIMAL, when the value is too big.
    **/
    public void Var036 ()
    {
        if(checkJdbc40 ())
        {
            int expectedColumn = -1;
            try
            {
                JDRSTest.position (rs_, key_);
                expectedColumn = rs_.findColumn ("C_DECIMAL_40");
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_DECIMAL_40", new StringReader ("5333644511"), 10L);
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
				     && (dt.getDataSize() == 10)
				     && (dt.getTransferSize() == 4));
		} else {
		    assertSqlException(e, -99999, "07006", "Data type mismatch",
				       "Mismatch instead of truncation in latest toolbox");

		}

            }
        }
    }


    /**
    updateNCharacterStream() - Update a DECIMAL, when the string is not a number.
    **/
    public void Var037 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_DECIMAL_105", new StringReader ("Terre Haute"), 11L);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNCharacterStream() - Update a NUMERIC.
    **/
    public void Var038 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_NUMERIC_105", new StringReader ("-9333.455"),9L);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                BigDecimal v = rs2.getBigDecimal ("C_NUMERIC_105");
                rs2.close ();
                assertCondition (v.doubleValue() == -9333.455);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNCharacterStream() - Update a NUMERIC, when the value is too big.
    **/
    public void Var039 ()
    {
        if(checkJdbc40 ())
        {
            int expectedColumn = -1;
            try
            {
                JDRSTest.position (rs_, key_);
                expectedColumn = rs_.findColumn ("C_NUMERIC_40");
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_NUMERIC_40", new StringReader ("-3315145"),8L);
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
                                 && (dt.getDataSize() == 7)
                                 && (dt.getTransferSize() == 4));
		} else { 
		    assertSqlException(e, -99999, "07006", "Data type mismatch",
				       "Mismatch instead of truncation in latest toolbox");
		}


            }
        }
    }



    /**
    updateNCharacterStream() - Update a NUMERIC, when the string is not a number.
    **/
    public void Var040 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_NUMERIC_105", new StringReader ("Detroit"), 7L);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNCharacterStream() - Update a CHAR.
    **/
    public void Var041 ()
    {

        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_CHAR_50", new StringReader ("Upper Darby"), 11L);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_CHAR_50");
                rs2.close ();
                assertCondition (v.equals ("Upper Darby                                       "));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNCharacterStream() - Update a CHAR, when the value is too big.
    **/
    public void Var042 ()
    {
        if(checkJdbc40 ())
        {
            int expectedColumn = -1;
            try
            {
                JDRSTest.position (rs_, key_);
                expectedColumn = rs_.findColumn ("C_CHAR_1");
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_CHAR_1", new StringReader ("Manhattan"),9L);
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
                                 && (dt.getDataSize() == 9)
                                 && (dt.getTransferSize() == 1),

				 "dt.getIndex()="+dt.getIndex()+" sb "+expectedColumn+"\n"+
				 "dt.getParameter()="+dt.getParameter()+" sb false\n" +
				 "dt.getRead()="+dt.getRead()+" sb false\n"+
				 "dt.getDataSize()="+dt.getDataSize()+" sb 9\n"+
                                 "dt.getTransferSize()="+dt.getTransferSize()+"sb 1");
		} else {
		    failed(e, "Unexpected Exception");
		}


            }
        }
    }



    /**
    updateNCharacterStream() - Update a VARCHAR, with the length equal to the
    full stream.
    **/
    public void Var043 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_VARCHAR_50", new StringReader ("Rehoboth Beach"), 14L);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_VARCHAR_50");
                rs2.close ();
                assertCondition (v.equals ("Rehoboth Beach"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNCharacterStream() - Update a VARCHAR, with the length less than the
    full stream.

     @D2 - Native driver doesn't throw an exception if stream contains extra characters then there needs to be there.
    **/
    public void Var044 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_VARCHAR_50", new StringReader ("Toronto"),4L);
		if( getDriver() == JDTestDriver.DRIVER_NATIVE &&	// @D2
		    true )	// @D2
		    succeeded();					// @D2
		else							// @D2
		    failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
		if( getDriver() == JDTestDriver.DRIVER_NATIVE &&	// @D2
		    true )	// @D2
		    succeeded();					// @D2
		else							// @D2
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNCharacterStream() - Update a VARCHAR, with the length greater than the
    full stream.
    **/
    public void Var045 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_VARCHAR_50", new StringReader ("Montreal"), 13L);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNCharacterStream() - Update a VARCHAR, with the length set to 1.

     @D2 - Native driver doesn't throw an exception if stream contains extra characters then there needs to be there.
    **/
    public void Var046 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_VARCHAR_50", new StringReader ("Winnipeg"), 1L);
		if( getDriver() == JDTestDriver.DRIVER_NATIVE &&	// @D2
		    true )	// @D2
		    succeeded();					// @D2
		else							// @D2
		    failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
		if( getDriver() == JDTestDriver.DRIVER_NATIVE &&	// @D2
		    true )	// @D2
		    succeeded();					// @D2
		else							// @D2
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNCharacterStream() - Update a VARCHAR, with the length set to 0.

     @D2 - Native driver doesn't throw an exception if stream contains extra characters then there needs to be there.
    **/
    public void Var047 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_VARCHAR_50", new StringReader ("Vancouver"), 0L);
		if( getDriver() == JDTestDriver.DRIVER_NATIVE &&	// @D2
		    true )	// @D2
		    succeeded();					// @D2
		else							// @D2
		    failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
		if( getDriver() == JDTestDriver.DRIVER_NATIVE &&	// @D2
		    true )	// @D2
		    succeeded();					// @D2
		else							// @D2
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNCharacterStream() - Update a VARCHAR, with an empty string.
    **/
    public void Var048 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_VARCHAR_50", new StringReader (""), 0L);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_VARCHAR_50");
                rs2.close ();
                assertCondition (v.equals (""));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNCharacterStream() - Update a VARCHAR, when the value is too big.
    **/
    public void Var049 ()
    {
        if(checkJdbc40 ())
        {
            int expectedColumn = -1;
            try
            {
                JDRSTest.position (rs_, key_);
                expectedColumn = rs_.findColumn ("C_VARCHAR_50");
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_VARCHAR_50", new StringReader ("The greater Washington, D.C. Metropolitan Area and surrounding communities"), 74L);
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
                                 && (dt.getDataSize() == 74)
                                 && (dt.getTransferSize() == 50),
				 "dt.getIndex()="+dt.getIndex()+" sb "+expectedColumn+"\n"+
				 "dt.getParameter()="+dt.getParameter()+" sb false\n" +
				 "dt.getRead()="+dt.getRead()+" sb false\n"+
				 "dt.getDataSize()="+dt.getDataSize()+" sb 74\n"+
                                 "dt.getTransferSize()="+dt.getTransferSize()+"sb 50");
		} else {
		    failed(e, "Unexpected Exception");
		}


            }
        }
    }


    /**
    updateNCharacterStream() - Update a VARCHAR parameter to a bad reader.
    **/
    public void Var050()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);

                class BadReader extends StringReader
                {
 
                    public BadReader ()
                    {
                        super ("Hi Mom!");
                    }
                    public int read (char[] buffer) throws IOException {
                        throw new IOException (); 
                    };
                }

                Reader r = new BadReader ();
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_VARCHAR_50", r,2L);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }





    /**
    updateNCharacterStream() - Update a BINARY.
    **/
    public void Var051 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                String expected;

                if(isToolboxDriver())
                    expected = "F1F2";
                else
                    expected = "Ford";

                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_BINARY_20", new StringReader (expected),4L);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_BINARY_20");
                rs2.close ();
                assertCondition (v.startsWith (expected));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNCharacterStream() - Update a BINARY, when the value is too big.
    **/
    public void Var052 ()
    {
        if(checkJdbc40 ())
        {
            int expectedColumn = -1;
            try
            {
                JDRSTest.position (rs_, key_);
                expectedColumn = rs_.findColumn ("C_BINARY_1");
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_BINARY_1", new StringReader ("F1F2F3"),6L);
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
                                 && (dt.getTransferSize() == 1),
				 "dt.getIndex()="+dt.getIndex()+" sb "+expectedColumn+"\n"+
				 "dt.getParameter()="+dt.getParameter()+" sb false\n" +
				 "dt.getRead()="+dt.getRead()+" sb false\n"+
				 
                                 "dt.getTransferSize()="+dt.getTransferSize()+"sb 1");
		} else {
		    failed(e, "Unexpected Exception");
		}


            }
        }
    }



    /**
    updateNCharacterStream() - Update a VARBINARY.
    **/
    public void Var053 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                String expected;
                if(isToolboxDriver())
                    expected = "F1F2";
                else
                    expected = "Ford";

                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_VARBINARY_20", new StringReader (expected),4L);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_VARBINARY_20");
                rs2.close ();
                assertCondition (v.equals (expected));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNCharacterStream() - Update a VARBINARY, when the value is too big.
    **/
    public void Var054 ()
    {
        if(checkJdbc40 ())
        {
            int expectedColumn = -1;
            try
            {
                String expected;
                if(isToolboxDriver())
                    expected = "F1F1F1F1F1F1F1F1F1F1F1F1F1F1F1F1F1F1F1F1F1F1F1F1F1F1F1F1F1F1";
                else
                    expected = "Thirty characters, thirty characters, thirty characters xxxx";

                JDRSTest.position (rs_, key_);
                expectedColumn = rs_.findColumn ("C_VARBINARY_20");
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_VARBINARY_20", new StringReader (expected), 60L);
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
                                 && (dt.getTransferSize() == 20),
				 "dt.getIndex()="+dt.getIndex()+" sb "+expectedColumn+"\n"+
				 "dt.getParameter()="+dt.getParameter()+" sb false\n" +
				 "dt.getRead()="+dt.getRead()+" sb false\n"+
				 
                                 "dt.getTransferSize()="+dt.getTransferSize()+"sb 20");
		} else {
		    failed(e, "Unexpected Exception");
		}

            }
        }
    }



    /**
    updateNCharacterStream() - Update a CLOB.
    
    SQL400 - the native driver expects this update to work correctly.
    **/
    public void Var055 ()
    {
        if(checkJdbc40 ())
        {
            if(checkLobSupport ())
            {
                try
                {
                    JDRSTest.position (rs_, key_);
                    JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_CLOB", new StringReader ("Knoxville"),9L);

                    rs_.updateRow ();
                    ResultSet rs2 = statement2_.executeQuery (select_);
                    JDRSTest.position (rs2, key_);
                    String v = rs2.getString ("C_CLOB");
                    rs2.close ();
                    assertCondition (v.equals ("Knoxville"));
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



    /**
    updateNCharacterStream() - Update a DBCLOB.
    
    SQL400 - the native driver expects this update to work correctly.
    **/
    public void Var056 ()
    {
        if(checkJdbc40 ())
        {
            if(checkLobSupport ())
            {
                try
                {
                    JDRSTest.position (rs_, key_);
                    JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_DBCLOB", new StringReader ("Memphis"), 7L);

                    rs_.updateRow ();
                    ResultSet rs2 = statement2_.executeQuery (select_);
                    JDRSTest.position (rs2, key_);
                    String v = rs2.getString ("C_DBCLOB");
                    rs2.close ();
                    assertCondition (v.equals ("Memphis"));
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



    /**
    updateNCharacterStream() - Update a BLOB.
    **/
    public void Var057 ()
    {
        if(checkJdbc40 ())
        {
            if(checkLobSupport ())
            {
                try
                {
                    JDRSTest.position (rs_, key_);
                    JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_BLOB", new StringReader ("Honolulu"),8L);
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
    updateNCharacterStream() - Update a DATE.
    **/
    public void Var058 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_DATE", new StringReader ("1970-02-10"), 10L);
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
    updateNCharacterStream() - Update a DATE, when the string is not a valid date.
    **/
    public void Var059 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_DATE", new StringReader ("Augusta"), 7L);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNCharacterStream() - Update a TIME.
    **/
    public void Var060 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_TIME", new StringReader ("13:45:32"),8L);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                Time v = rs2.getTime ("C_TIME");
                rs2.close ();
                assertCondition (v.toString ().equals ("13:45:32"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNCharacterStream() - Update a TIME, when the string is not a valid time.
    **/
    public void Var061 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_TIME", new StringReader ("Birmingham"), 10L);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNCharacterStream() - Update a TIMESTAMP.
    **/
    public void Var062 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_TIMESTAMP", new StringReader ("1969-11-18 15:22:23.4329"), 24L);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                Timestamp v = rs2.getTimestamp ("C_TIMESTAMP");
                rs2.close ();
                assertCondition (v.toString ().equals ("1969-11-18 15:22:23.4329"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNCharacterStream() - Update a TIMESTAMP, when the string is not a valid timestamp.
    **/
    public void Var063 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_TIMESTAMP", new StringReader ("Boston"),6L);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNCharacterStream() - Update a DATALINK.
    **/
    public void Var064 ()
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
                    JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_DATALINK", new StringReader ("Providence"),9L);
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
    updateNCharacterStream() - Update a DISTINCT.
    **/
    public void Var065 ()
    {
        if(checkJdbc40 ())
        {
            if(checkLobSupport ())
            {
                try
                {
                    JDRSTest.position (rs_, key_);
                    JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_DISTINCT", new StringReader ("Concord"), 7L);
                    rs_.updateRow ();
                    ResultSet rs2 = statement2_.executeQuery (select_);
                    JDRSTest.position (rs2, key_);
                    String v = rs2.getString ("C_DISTINCT");
                    rs2.close ();
                    assertCondition (v.equals ("Concord  "));
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



    /**
    updateNCharacterStream() - Update a BIGINT.
    **/
    public void Var066()
    {
        if(checkJdbc40 ())
        {
            if(checkBigintSupport())
            {
                try
                {
                    JDRSTest.position (rs_, key_);
                    JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_BIGINT", new StringReader ("-1237324"),8L);
                    rs_.updateRow ();
                    ResultSet rs2 = statement2_.executeQuery (select_);
                    JDRSTest.position (rs2, key_);
                    long v = rs2.getLong ("C_BIGINT");
                    rs2.close ();
                    assertCondition (v == -1237324);
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



    /**
    updateNCharacterStream() - Update a BIGINT, when the integer is too big.
    **/
    public void Var067()
    {
        if(checkJdbc40 ())
        {
            if(checkBigintSupport())
            {
                try
                {
                    JDRSTest.position (rs_, key_);
                    JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_BIGINT", new StringReader ("4353534432176864587623"), 22L);
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
    updateNCharacterStream() - Update a BIGINTEGER, when the string is not a number.
    **/
    public void Var068()
    {
        if(checkJdbc40 ())
        {
            if(checkBigintSupport())
            {
                try
                {
                    JDRSTest.position (rs_, key_);
                    JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_BIGINT", new StringReader ("Baton Rouge"), 11L);
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
    updateNCharacterStream() - Update a BLOB.
    Native will fail because they do not support  -- see Var057
    **/
    public void Var069 ()
    {
        if(checkJdbc40 ())
        {
            if(checkLobSupport ())
            {
                try
                {
                    JDRSTest.position(rs_, key_);
                    JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_BLOB", new StringReader("1023AAFFB10"), 11L);
                    if(isToolboxDriver())
                    {
                        succeeded();
                    }
                    else
                    {
                        failed("Didn't throw SQLException");
                    }
                }
                catch(Exception e)
                {
                    if(isToolboxDriver())
                    {
                        failed(e, "Unexpected Exception");
                    }
                    else
                    {
                        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
                    }
                }
            }
        }
    }


    /**
    updateCharacterStream() - Should work with weird reader
    **/

   public void Var070()
    {

	String added = "Added by native driver 10/11/2006 to test input stream that sometimes returns 0 bytes "; 
        if(checkJdbc40 ())

        {
            try
            {
                JDRSTest.position (rs_, key_);
                Reader r = new JDWeirdReader("0102030"); 
                //toolbox expects correct size
                try{
		    
                    JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", 14, r,  (long) 6);
                }catch(SQLException e){
                    if(isToolboxDriver())
                    {
                        assertCondition((""+e.getMessage()).endsWith("buffer length not valid."), "Exception was "+e);
                        return;
                    }else{
                        throw e;
                    }
                }
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String check = rs2.getString ("C_VARCHAR_50");
                rs2.close ();
		String expected = " !\"#$%";
		assertCondition (check.equals (expected), "\nExpected :'"+expected+"'" +
				     "\nGot      :'"+check+"'" + added );
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception "+added);
            }
        }
    }

    /**
    updateNCharacterStream() - Should work with weird reader and not all read
    **/
    public void Var071()
    {
	String added = "Added by native driver 10/11/2006 to test input stream that sometimes returns 0 bytes "; 
        if(checkJdbc40 ())

        {
            try
            {
                JDRSTest.position (rs_, key_);
                Reader r = new JDWeirdReader("0102030"); 
                //toolbox expects correct size
                try{
                  JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", 14, r,  (long) 6);
                }catch(SQLException e){
                    if(isToolboxDriver())
                    {
                        assertCondition((""+e.getMessage()).endsWith("buffer length not valid."), "Exception was "+e);
                        return;
                    }else{
                        throw e;
                    }
                }
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String check = rs2.getString ("C_VARCHAR_50");
                rs2.close ();
		String expected = " !\"#$%";
		assertCondition (check.equals (expected), "\nExpected :'"+expected+"'" +
				     "\nGot      :'"+check+"'" + added );
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception "+added);
            }
        }
    }

    
    /**
    updateNCharacterStream() - Update DFP16:
    **/
   public void Var072 ()
   {
     if(checkJdbc40 ())
     if (checkDecFloatSupport()) {
       try {
         Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
             ResultSet.CONCUR_UPDATABLE);
         ResultSet rs = s.executeQuery ("SELECT * FROM "
             + JDRSTest.RSTEST_DFP16+" FOR UPDATE ");
         rs.next(); 
         JDReflectionUtil.callMethod_V(rs, "updateNCharacterStream", 1, 
             new StringReader ("Hannover"), 8L ); 
         failed ("Didn't throw SQLException ");
       }
       catch (Exception e) {
         if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            String message = e.getMessage(); 
            boolean success = (message.indexOf("Data type mismatch") >= 0);
            if (!success) { 
              e.printStackTrace();
            }
            assertCondition(success, "Expected Data type mismatch exception but got "+message);
         } else { 
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }      
       }
     }
   }
   
   /**
   updateNCharacterStream() - Update DFP34:
   **/

  public void Var073 ()
  {
    if(checkJdbc40 ())
    if (checkDecFloatSupport()) {
      try {
        Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = s.executeQuery ("SELECT * FROM "
            + JDRSTest.RSTEST_DFP34+" FOR UPDATE ");
        rs.next(); 
        JDReflectionUtil.callMethod_V(rs, "updateNCharacterStream", 1, 
            new StringReader ("Hannover"), 8L ); 
        failed ("Didn't throw SQLException ");
      }
      catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
           String message = e.getMessage(); 
           boolean success = (message.indexOf("Data type mismatch") >= 0);
           if (!success) { 
             e.printStackTrace();
           }
           assertCondition(success, "Expected Data type mismatch exception but got "+message);
        } else { 
           assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }      
      }
    }
  }
  

  
      /**
    updateNCharacterStream() - Update a BOOLEAN
    **/
    public void Var074()
    {
        if(checkJdbc40 ())
        {
            if(checkBooleanSupport())
            {
                try
                {
                    JDRSTest.position (rs_, key_);
                    JDReflectionUtil.callMethod_V(rs_, "updateNCharacterStream", "C_BOOLEAN", new StringReader ("1"), 1L);
                    failed ("Didn't throw SQLException");
                }
                catch(Exception e)
                {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }




}



