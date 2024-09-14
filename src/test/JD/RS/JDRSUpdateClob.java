///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSUpdateClob.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.RS;

import com.ibm.as400.access.AS400;

import test.JDLobTest;
import test.JDRSTest;
import test.JDTestDriver;
import test.JDTestcase;
import test.JDLobTest.JDTestClob;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Hashtable;



/**
Testcase JDRSUpdateClob.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>updateClob()
</ul>
**/
public class JDRSUpdateClob
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSUpdateClob";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



    // Private data.
    private static final String key_            = "JDRSUpdateClob";
    private static String select_         = "SELECT * FROM "
                                            + JDRSTest.RSTEST_UPDATE;

    private Statement           statement_;
    private Statement           statement2_;
    private ResultSet           rs_;



    /**
    Constructor.
    **/
    public JDRSUpdateClob (AS400 systemObject,
                                      Hashtable namesAndVars,
                                      int runMode,
                                      FileOutputStream fileOutputStream,
                                      
                                      String password)
    {
        super (systemObject, "JDRSUpdateClob",
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
	    System.out.println("Using table "+JDRSTest.RSTEST_UPDATE); 
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
    updateClob() - Should throw exception when the result set is
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


                rs.updateClob ("C_VARCHAR_50", new JDLobTest.JDTestClob ("Juneau"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateClob() - Should throw exception when the result set is
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
                rs.updateClob ("C_VARCHAR_50", new JDLobTest.JDTestClob ("Tacoma"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateClob() - Should throw exception when cursor is not pointing
    to a row.
    **/
    public void Var003()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, null);
                rs_.updateClob ("C_VARCHAR_50", new JDLobTest.JDTestClob ("Portland"));
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
    updateClob() - Should throw an exception when the column
    is an invalid index.
    **/
    public void Var004()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob (100, new JDLobTest.JDTestClob ("San Francisco"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateClob() - Should throw an exception when the column
    is 0.
    **/
    public void Var005()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob (0, new JDLobTest.JDTestClob ("Boise"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateClob() - Should throw an exception when the column
    is -1.
    **/
    public void Var006()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob (-1, new JDLobTest.JDTestClob ("Helena"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateClob() - Should work when the column index is valid.
    **/
    public void Var007()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob (14, new JDLobTest.JDTestClob ("Fargo"));
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
    updateClob() - Should throw an exception when the column
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
            rs_.updateClob (null, new JDLobTest.JDTestClob ("Sioux Falls"));
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
    updateClob() - Should throw an exception when the column
    name is an empty string.
    **/
    public void Var009()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob ("", new JDLobTest.JDTestClob ("Lincoln"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**    updateClob() - Should throw an exception when the column
    name is invalid.
    **/
    public void Var010()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob ("INVALID", new JDLobTest.JDTestClob ("Colorado Springs"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateClob() - Should work when the column name is valid.
    **/
    public void Var011()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob ("C_VARCHAR_50", new JDLobTest.JDTestClob ("Gillette"));
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
    updateClob() - Should update to SQL NULL when the column
    value is null.
    **/
    public void Var012()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob ("C_VARCHAR_50", (Clob)null);
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
    updateClob() - Should throw an exception when the length is invalid.
    **/
    public void Var013()
    {
        notApplicable(); 
    }



    /**
    updateClob() - Should be reflected by get, even if update has
    not yet been issued (i.e. update is still pending).
    **/
    public void Var014()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob ("C_VARCHAR_50", new JDLobTest.JDTestClob ("Santa Fe"));
                assertCondition (rs_.getString ("C_VARCHAR_50").equals ("Santa Fe"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }




    /**
    updateClob() - Should be reflected by get, after update has
    been issued, but cursor has not been repositioned.
    **/
    public void Var015()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob ("C_VARCHAR_50", new JDLobTest.JDTestClob ("Phoenix"));
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
    updateClob() - Should be reflected by get, after update has
    been issued and cursor has been repositioned.
    **/
    public void Var016()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob ("C_VARCHAR_50", new JDLobTest.JDTestClob ("Austin"));
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
    updateClob() - Should work when the current row is the insert
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
                rs_.updateClob ("C_VARCHAR_50", new JDLobTest.JDTestClob ("Tulsa"));
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
    updateClob() - Should be reflected by get on insert row, even if
    insert has not yet been issued (i.e. insert is still pending).
    **/
    public void Var018()
    {
        if(checkJdbc40 ())
        {
            try
            {
                rs_.moveToInsertRow ();
                rs_.updateClob ("C_VARCHAR_50", new JDLobTest.JDTestClob ("Lawrence"));
                assertCondition (rs_.getString ("C_VARCHAR_50").equals ("Lawrence"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateClob() - Should throw an exception on a deleted row.
    **/
    public void Var019()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, "DUMMY_ROW");
                rs_.deleteRow ();
                rs_.updateClob ("C_VARCHAR_50", new JDLobTest.JDTestClob ("Ames"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


    /**
    updateClob() - Update a SMALLINT.
    **/
    public void Var020 ()
    {
        if(checkJdbc40 ())
        {  
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob ("C_SMALLINT", new JDLobTest.JDTestClob ("31"));
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                short v = rs2.getShort ("C_SMALLINT");
                rs2.close ();
                assertCondition (v == 31);
            }
            catch(Exception e)
            {
                //toolbox does not support updateClob on this type
                if(isToolboxDriver())
                {
                    assertCondition((""+e.getMessage()).indexOf("Data type mismatch") != -1);
                    return;
                }
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateClob() - Update a SMALLINT, when the integer is too big.
    **/
    public void Var021 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob ("C_SMALLINT", new JDLobTest.JDTestClob ("224323"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateClob() - Update a SMALLINT, when the string is not a number.
    **/
    public void Var022 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob ("C_SMALLINT", new JDLobTest.JDTestClob ("Van Buren"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateClob() - Update an INTEGER.
    **/
    public void Var023 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob ("C_INTEGER", new JDLobTest.JDTestClob ("-12374"));
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                int v = rs2.getInt ("C_INTEGER");
                rs2.close ();
                assertCondition (v == -12374);
            }
            catch(Exception e)
            {
                //toolbox does not support updateClob on this type
                if(isToolboxDriver())
                {
                    assertCondition((""+e.getMessage()).indexOf("Data type mismatch") != -1);
                    return;
                }
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateClob() - Update an INTEGER, when the integer is too big.
    **/
    public void Var024 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob ("C_INTEGER", new JDLobTest.JDTestClob ("435353487623"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateClob() - Update an INTEGER, when the string is not a number.
    **/
    public void Var025 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob ("C_INTEGER", new JDLobTest.JDTestClob ("Baton Rouge"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateClob() - Update a REAL.
    **/
    public void Var026 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob ("C_REAL", new JDLobTest.JDTestClob ("13.35"));
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                float v = rs2.getFloat ("C_REAL");
                rs2.close ();
                assertCondition (v == 13.35f);
            }
            catch(Exception e)
            {
                //toolbox does not support updateClob on this type
                if(isToolboxDriver())
                {
                    assertCondition((""+e.getMessage()).indexOf("Data type mismatch") != -1);
                    return;
                }
                failed (e, "Unexpected Exception");
            }
        }
    }


    /**
    updateClob() - Update a REAL, when the number is too big.
    
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
                rs_.updateClob ("C_REAL", new JDLobTest.JDTestClob ("453433335.4437623"));
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
                //toolbox does not support updateClob on this type
                if(isToolboxDriver())
                {
                    assertCondition((""+e.getMessage()).indexOf("Data type mismatch") != -1);
                    return;
                }
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
    updateClob() - Update a REAL, when the string is not a number.
    **/    public void Var028 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob ("C_INTEGER", new JDLobTest.JDTestClob ("Edina"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }




    /**
    updateClob() - Update a FLOAT.
    **/
    public void Var029 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob ("C_FLOAT", new JDLobTest.JDTestClob ("9835.2"));
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                float v = rs2.getFloat ("C_FLOAT");
                rs2.close ();
                assertCondition (v == 9835.2f);
            }
            catch(Exception e)
            {
                //toolbox does not support updateClob on this type
                if(isToolboxDriver())
                {
                    assertCondition((""+e.getMessage()).indexOf("Data type mismatch") != -1);
                    return;
                }
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateClob() - Update a FLOAT, when the number is too big.
    
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
                rs_.updateClob ("C_FLOAT", new JDLobTest.JDTestClob ("453435.4434556337633"));
                rs_.updateRow ();

                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                float v = rs2.getFloat ("C_FLOAT");
                rs2.close ();
                assertCondition ((v > 453435) && (v < 453436));
            }
            catch(Exception e)
            {
                //toolbox does not support updateClob on this type
                if(isToolboxDriver())
                {
                    assertCondition((""+e.getMessage()).indexOf("Data type mismatch") != -1);
                    return;
                }
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
    updateClob() - Update a FLOAT, when the string is not a number.
    **/
    public void Var031 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob ("C_FLOAT", new JDLobTest.JDTestClob ("Madison"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


    /**
    updateClob() - Update a DOUBLE.
    **/
    public void Var032 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob ("C_DOUBLE", new JDLobTest.JDTestClob ("-9845"));
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                double v = rs2.getDouble ("C_DOUBLE");
                rs2.close ();
                assertCondition (v == -9845);
            }
            catch(Exception e)
            {
                //toolbox does not support updateClob on this type
                if(isToolboxDriver())
                {
                    assertCondition((""+e.getMessage()).indexOf("Data type mismatch") != -1);
                    return;
                }
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateClob() - Update a DOUBLE, when the number is too big.
    
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
                rs_.updateClob ("C_DOUBLE", new JDLobTest.JDTestClob ("453342035.44445532333"));
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
                //toolbox does not support updateClob on this type
                if(isToolboxDriver())
                {
                    assertCondition((""+e.getMessage()).indexOf("Data type mismatch") != -1);
                    return;
                }
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
    updateClob() - Update a DOUBLE, when the string is not a number.
    **/
    public void Var034 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob ("C_DOUBLE", new JDLobTest.JDTestClob ("Chicago"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


    /**
    updateClob() - Update a DECIMAL.
    **/
    public void Var035 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob ("C_DECIMAL_105", new JDLobTest.JDTestClob ("4533.43"));
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                BigDecimal v = rs2.getBigDecimal ("C_DECIMAL_105");
                rs2.close ();
                assertCondition (v.floatValue() == 4533.43f);
            }
            catch(Exception e)
            {
                //toolbox does not support updateClob on this type
                if(isToolboxDriver())
                {
                    assertCondition((""+e.getMessage()).indexOf("Data type mismatch") != -1);
                    return;
                }
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateClob() - Update a DECIMAL, when the value is too big.
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
                rs_.updateClob ("C_DECIMAL_40", new JDLobTest.JDTestClob ("5333644511"));
                rs_.updateRow ();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                //toolbox does not support updateClob on this type
                if(isToolboxDriver())
                {
                    assertCondition((""+e.getMessage()).indexOf("Data type mismatch") != -1);
                    return;
                }
                
                DataTruncation dt = (DataTruncation)e;
                assertCondition ((dt.getIndex() == expectedColumn)
                                 && (dt.getParameter() == false)
                                 && (dt.getRead() == false)
                                 && (dt.getDataSize() == 10)
                                 && (dt.getTransferSize() == 4));

            }
        }
    }


    /**
    updateClob() - Update a DECIMAL, when the string is not a number.
    **/
    public void Var037 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob ("C_DECIMAL_105", new JDLobTest.JDTestClob ("Terre Haute"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateClob() - Update a NUMERIC.
    **/
    public void Var038 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob ("C_NUMERIC_105", new JDLobTest.JDTestClob ("-9333.455"));
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                BigDecimal v = rs2.getBigDecimal ("C_NUMERIC_105");
                rs2.close ();
                assertCondition (v.doubleValue() == -9333.455);
            }
            catch(Exception e)
            {
                //toolbox does not support updateClob on this type
                if(isToolboxDriver())
                {
                    assertCondition((""+e.getMessage()).indexOf("Data type mismatch") != -1);
                    return;
                }
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateClob() - Update a NUMERIC, when the value is too big.
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
                rs_.updateClob ("C_NUMERIC_40", new JDLobTest.JDTestClob ("-3315145"));
                rs_.updateRow ();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                //toolbox does not support updateClob on this type
                if(isToolboxDriver())
                {
                    assertCondition((""+e.getMessage()).indexOf("Data type mismatch") != -1);
                    return;
                }
                DataTruncation dt = (DataTruncation)e;
                assertCondition ((dt.getIndex() == expectedColumn)
                                 && (dt.getParameter() == false)
                                 && (dt.getRead() == false)
                                 && (dt.getDataSize() == 7)
                                 && (dt.getTransferSize() == 4));

            }
        }
    }



    /**
    updateClob() - Update a NUMERIC, when the string is not a number.
    **/
    public void Var040 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob ("C_NUMERIC_105", new JDLobTest.JDTestClob ("Detroit"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateClob() - Update a CHAR.
    **/
    public void Var041 ()
    {

        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob ("C_CHAR_50", new JDLobTest.JDTestClob ("Upper Darby"));
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
    updateClob() - Update a CHAR, when the value is too big.
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
                rs_.updateClob ("C_CHAR_1", new JDLobTest.JDTestClob ("Manhattan"));
                rs_.updateRow ();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                DataTruncation dt = (DataTruncation)e;
                assertCondition ((dt.getIndex() == expectedColumn)
                                 && (dt.getParameter() == false)
                                 && (dt.getRead() == false)
                                 && (dt.getDataSize() == 9)
                                 && (dt.getTransferSize() == 1));

            }
        }
    }



    /**
    updateClob() - Update a VARCHAR, with the length equal to the
    full stream.
    **/
    public void Var043 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob ("C_VARCHAR_50", new JDLobTest.JDTestClob ("Rehoboth Beach"));
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
    updateClob() - Update a VARCHAR, with the length less than the
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
                rs_.updateClob ("C_VARCHAR_50", new JDLobTest.JDTestClob ("Toronto"));
                //toolbox works now
                //if( getDriver() == JDTestDriver.DRIVER_NATIVE &&	// @D2
                //  getRelease() >= JDTestDriver.RELEASE_V5R3M0 )	// @D2
                succeeded();					// @D2
                //else							// @D2
                //  failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                //if( getDriver() == JDTestDriver.DRIVER_NATIVE &&	// @D2
                //  getRelease() >= JDTestDriver.RELEASE_V5R3M0 )	// @D2
               // succeeded();					// @D2
                failed (e, "Unexpected Exception");
                //	else							// @D2
                //    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateClob() - Update a VARCHAR, with the length greater than the
    full stream.
    **/
    public void Var045 ()
    {
      notApplicable(); 
    }



    /**
    updateClob() - Update a VARCHAR, with the length set to 1.

     @D2 - Native driver doesn't throw an exception if stream contains extra characters then there needs to be there.
    **/
    public void Var046 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob ("C_VARCHAR_50", new JDLobTest.JDTestClob ("Winnipeg"));
                //toolbox works now
                //if( getDriver() == JDTestDriver.DRIVER_NATIVE &&	// @D2
                //    getRelease() >= JDTestDriver.RELEASE_V5R3M0 )	// @D2
                succeeded();					// @D2
                //else							// @D2
                //    failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                //if( getDriver() == JDTestDriver.DRIVER_NATIVE &&	// @D2
                //    getRelease() >= JDTestDriver.RELEASE_V5R3M0 )	// @D2
                // succeeded();					// @D2
                failed (e, "Unexpected Exception");
                //else							// @D2
                //  assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateClob() - Update a VARCHAR, with the length set to 0.
    **/
    public void Var047 ()
    {
        notApplicable(); 
    }



    /**
    updateClob() - Update a VARCHAR, with an empty string.
    **/
    public void Var048 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob ("C_VARCHAR_50", new JDLobTest.JDTestClob (""));
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
    updateClob() - Update a VARCHAR, when the value is too big.
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
                rs_.updateClob ("C_VARCHAR_50", new JDLobTest.JDTestClob ("The greater Washington, D.C. Metropolitan Area and surrounding communities"));
                rs_.updateRow ();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                DataTruncation dt = (DataTruncation)e;
                assertCondition ((dt.getIndex() == expectedColumn)
                                 && (dt.getParameter() == false)
                                 && (dt.getRead() == false)
                                 && (dt.getDataSize() == 74)
                                 && (dt.getTransferSize() == 50));

            }
        }
    }


    /**
    updateClob() - Update a VARCHAR parameter to a bad reader.
    **/
    public void Var050()
    {
        notApplicable(); 
    }





    /**
    updateClob() - Update a BINARY.
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
                rs_.updateClob ("C_BINARY_20", new JDLobTest.JDTestClob (expected));
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
    updateClob() - Update a BINARY, when the value is too big.
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
                rs_.updateClob ("C_BINARY_1", new JDLobTest.JDTestClob ("F1F2F3"));
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
    updateClob() - Update a VARBINARY.
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
                rs_.updateClob ("C_VARBINARY_20", new JDLobTest.JDTestClob (expected));
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
    updateClob() - Update a VARBINARY, when the value is too big.
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
                rs_.updateClob ("C_VARBINARY_20", new JDLobTest.JDTestClob (expected));
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
    updateClob() - Update a CLOB.
    
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
                    rs_.updateClob ("C_CLOB", new JDLobTest.JDTestClob ("Knoxville"));

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
    updateClob() - Update a DBCLOB.
    
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
                    rs_.updateClob ("C_DBCLOB", new JDLobTest.JDTestClob ("Memphis"));

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
    updateClob() - Update a BLOB.
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
                    rs_.updateClob ("C_BLOB", new JDLobTest.JDTestClob ("Honolulu"));
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
    updateClob() - Update a DATE.
    **/
    public void Var058 ()
    {
        if(checkJdbc40 ())
        { 
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob ("C_DATE", new JDLobTest.JDTestClob ("1970-02-10"));
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                Date v = rs2.getDate ("C_DATE");
                rs2.close ();
                assertCondition (v.toString ().equals ("1970-02-10"));
            }
            catch(Exception e)
            {
                //toolbox does not support updateClob on this type
                if(isToolboxDriver())
                {
                    assertCondition((""+e.getMessage()).indexOf("Data type mismatch") != -1);
                    return;
                }
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateClob() - Update a DATE, when the string is not a valid date.
    **/
    public void Var059 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob ("C_DATE", new JDLobTest.JDTestClob ("Augusta"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateClob() - Update a TIME.
    **/
    public void Var060 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob ("C_TIME", new JDLobTest.JDTestClob ("13:45:32"));
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                Time v = rs2.getTime ("C_TIME");
                rs2.close ();
                assertCondition (v.toString ().equals ("13:45:32"));
            }
            catch(Exception e)
            {
                //toolbox does not support updateClob on this type
                if(isToolboxDriver())
                {
                    assertCondition((""+e.getMessage()).indexOf("Data type mismatch") != -1);
                    return;
                }
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateClob() - Update a TIME, when the string is not a valid time.
    **/
    public void Var061 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob ("C_TIME", new JDLobTest.JDTestClob ("Birmingham"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateClob() - Update a TIMESTAMP.
    **/
    public void Var062 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob ("C_TIMESTAMP", new JDLobTest.JDTestClob ("1969-11-18 15:22:23.4329"));
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                Timestamp v = rs2.getTimestamp ("C_TIMESTAMP");
                rs2.close ();
                assertCondition (v.toString ().equals ("1969-11-18 15:22:23.4329"));
            }
            catch(Exception e)
            {
                //toolbox does not support updateClob on this type
                if(isToolboxDriver())
                {
                    assertCondition((""+e.getMessage()).indexOf("Data type mismatch") != -1);
                    return;
                }
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateClob() - Update a TIMESTAMP, when the string is not a valid timestamp.
    **/
    public void Var063 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateClob ("C_TIMESTAMP", new JDLobTest.JDTestClob ("Boston"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateClob() - Update a DATALINK.
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
                    rs_.updateClob ("C_DATALINK", new JDLobTest.JDTestClob ("Providence"));
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
    updateClob() - Update a DISTINCT.
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
                    rs_.updateClob ("C_DISTINCT", new JDLobTest.JDTestClob ("Concord"));
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
    updateClob() - Update a BIGINT.
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
                    rs_.updateClob ("C_BIGINT", new JDLobTest.JDTestClob ("-1237324"));
                    rs_.updateRow ();
                    ResultSet rs2 = statement2_.executeQuery (select_);
                    JDRSTest.position (rs2, key_);
                    long v = rs2.getLong ("C_BIGINT");
                    rs2.close ();
                    assertCondition (v == -1237324);
                }
                catch(Exception e)
                {
                    //toolbox does not support updateClob on this type
                    if(isToolboxDriver())
                    {
                        assertCondition((""+e.getMessage()).indexOf("Data type mismatch") != -1);
                        return;
                    }
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



    /**
    updateClob() - Update a BIGINT, when the integer is too big.
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
                    rs_.updateClob ("C_BIGINT", new JDLobTest.JDTestClob ("4353534432176864587623"));
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
    updateClob() - Update a BIGINTEGER, when the string is not a number.
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
                    rs_.updateClob ("C_BIGINT", new JDLobTest.JDTestClob ("Baton Rouge"));
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
    updateClob() - Update a BLOB.
    Native will fail because they do not support  -- see Var057
    Toolbox also fails
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
                    rs_.updateClob("C_BLOB", new JDLobTest.JDTestClob("1023AAFFB10"));
                
                        failed("Didn't throw SQLException");
                     
                }
                catch(Exception e)
                {
                    
                        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
                 
                }
            }
        }
    }
    
    
    
    
    /**
    updateClob() - Update a DFP16.
    **/
    public void Var070 ()
    {
      if(checkJdbc40 ())
        if(checkDecFloatSupport())
        {
            String value = "4533.43"; 
            try
            {
              Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                  ResultSet.CONCUR_UPDATABLE);
              ResultSet rs = s.executeQuery ("SELECT * FROM "
                  + JDRSTest.RSTEST_DFP16+" FOR UPDATE ");
              rs.next(); 
              rs.updateClob (1, new JDLobTest.JDTestClob (value));
              rs.updateRow ();

              ResultSet rs2 = statement2_.executeQuery ("SELECT * FROM "
                  + JDRSTest.RSTEST_DFP16);
              rs2.next(); 
              String v = rs2.getString (1);
              rs2.close ();
              assertCondition (v.equals(value), "Got "+v+" sb "+value); 
            }
            catch(Exception e)
            {
                //toolbox does not support updateClob on this type
                if(isToolboxDriver())
                {
                    assertCondition((""+e.getMessage()).indexOf("Data type mismatch") != -1);
                    return;
                }
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateClob() - Update a DFP16, when the value is too big.
    **/
    public void Var071 ()
    {
      if(checkJdbc40 ())
      if(checkDecFloatSupport())
      {
          String value = "123456E700"; 
          try
          {
            Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_DFP16+" FOR UPDATE ");
            rs.next(); 
            rs.updateClob (1, new JDLobTest.JDTestClob (value));
            rs.updateRow ();

            ResultSet rs2 = statement2_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_DFP16);
            rs2.next(); 
            String v = rs2.getString (1);
            rs2.close ();
            failed("Expected exception but got "+v+" for "+value); 
          }
          catch(Exception e)
          {
             // e.printStackTrace(); 
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          }
      }
    }


    /**
    updateClob() - Update a DFP16, when the string is not a number.
    **/
    public void Var072 ()
    {
      if(checkJdbc40 ())
      if(checkDecFloatSupport())
      {
          String value = "Terre Haute"; 
          try
          {
            Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_DFP16+" FOR UPDATE ");
            rs.next(); 
            rs.updateClob (1, new JDLobTest.JDTestClob (value));
            rs.updateRow ();

            ResultSet rs2 = statement2_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_DFP16);
            rs2.next(); 
            String v = rs2.getString (1);
            rs2.close ();
            failed("Expected exception but got "+v+" for "+value); 
          }
          catch(Exception e)
          {
             // e.printStackTrace(); 
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          }
      }
    }


    /**
    updateClob() - Update a DFP34.
    **/
    public void Var073 ()
    {
      if(checkJdbc40 ())
        if(checkDecFloatSupport())
        {
            String value = "4533.43"; 
            try
            {
              Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                  ResultSet.CONCUR_UPDATABLE);
              ResultSet rs = s.executeQuery ("SELECT * FROM "
                  + JDRSTest.RSTEST_DFP34+" FOR UPDATE ");
              rs.next(); 
              rs.updateClob (1, new JDLobTest.JDTestClob (value));
              rs.updateRow ();

              ResultSet rs2 = statement2_.executeQuery ("SELECT * FROM "
                  + JDRSTest.RSTEST_DFP34);
              rs2.next(); 
              String v = rs2.getString (1);
              rs2.close ();
              assertCondition (v.equals(value), "Got "+v+" sb "+value); 
            }
            catch(Exception e)
            {
                //toolbox does not support updateClob on this type
                if(isToolboxDriver())
                {
                    assertCondition((""+e.getMessage()).indexOf("Data type mismatch") != -1);
                    return;
                }
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateClob() - Update a DFP34, when the value is too big.
    **/
    public void Var074 ()
    {
      if(checkJdbc40 ())
      if(checkDecFloatSupport())
      {
          String value = "12345E7000 " ;
                        
          try
          {
            Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_DFP34+" FOR UPDATE ");
            rs.next(); 
            rs.updateClob (1, new JDLobTest.JDTestClob (value));
            rs.updateRow ();

            ResultSet rs2 = statement2_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_DFP34);
            rs2.next(); 
            String v = rs2.getString (1);
            rs2.close ();
            failed("Expected exception but got "+v+" for "+value); 
          }
          catch(Exception e)
          {
             // e.printStackTrace(); 
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          }
      }
    }


    /**
    updateClob() - Update a DFP34, when the string is not a number.
    **/
    public void Var075 ()
    {
      if(checkJdbc40 ())
      if(checkDecFloatSupport())
      {
          String value = "Terre Haute"; 
          try
          {
            Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_DFP34+" FOR UPDATE ");
            rs.next(); 
            rs.updateClob (1, new JDLobTest.JDTestClob (value));
            rs.updateRow ();

            ResultSet rs2 = statement2_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_DFP34);
            rs2.next(); 
            String v = rs2.getString (1);
            rs2.close ();
            failed("Expected exception but got "+v+" for "+value); 
          }
          catch(Exception e)
          {
              //e.printStackTrace(); 
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          }
      }
    }


      /**
    updateClob() - Update a BOOLEAN
    **/
    public void Var076()
    {
        if(checkJdbc20 ())
        {
            if(checkBooleanSupport())
            {
                try
                {
                    JDRSTest.position (rs_, key_);
		    rs_.updateClob ("C_BOOLEAN", new JDLobTest.JDTestClob ("1"));
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



