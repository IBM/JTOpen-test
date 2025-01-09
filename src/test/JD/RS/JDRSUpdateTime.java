///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSUpdateTime.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.JD.RS;

import java.io.FileOutputStream;
import java.sql.DataTruncation;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDRSTest;
import test.JDTestDriver;
import test.JDTestcase;



/**
Testcase JDRSUpdateTime.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>updateTime()
</ul>
**/
public class JDRSUpdateTime
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSUpdateTime";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



    // Private data.
    private static final String key_            = "JDRSUpdateTime";
    private static String select_         = "SELECT * FROM "
                                                    + JDRSTest.RSTEST_UPDATE;

    private Statement           statement_;
    private Statement           statement2_;
    private ResultSet           rs_;



/**
Constructor.
**/
    public JDRSUpdateTime (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSUpdateTime",
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
        select_         = "SELECT * FROM "  + JDRSTest.RSTEST_UPDATE;

        if (isJdbc20 ()) {
            // SQL400 - driver neutral...
            String url = baseURL_
            // String url = "jdbc:as400://" + systemObject_.getSystemName()
                
                
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
        if (isJdbc20 ()) {
            rs_.close ();
            statement_.close ();
            connection_.commit(); // @C1A
            connection_.close ();
        }
    }



/**
updateTime() - Should throw exception when the result set is
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
            rs.updateTime ("C_TIME", Time.valueOf("10:01:12"));
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateTime() - Should throw exception when the result set is
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
            rs.updateTime ("C_TIME", Time.valueOf("04:06:23"));
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateTime() - Should throw exception when cursor is not pointing
to a row.
**/
     public void Var003()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, null);
            rs_.updateTime ("C_TIME", Time.valueOf("08:07:23"));
            rs_.updateRow(); 
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateTime() - Should throw an exception when the column
is an invalid index.
**/
    public void Var004()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateTime (100, Time.valueOf("19:03:12"));
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateTime() - Should throw an exception when the column
is 0.
**/
    public void Var005()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateTime (0, Time.valueOf("23:34:23"));
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateTime() - Should throw an exception when the column
is -1.
**/
    public void Var006()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateTime (-1, Time.valueOf("14:24:09"));
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateTime() - Should work when the column index is valid.
**/
    public void Var007()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            Time d = Time.valueOf("19:22:40");
            rs_.updateTime (20, d);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            Time v = rs2.getTime ("C_TIME");
            rs2.close ();
            assertCondition (v.toString ().equals (d.toString ()));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateTime() - Should throw an exception when the column
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
            rs_.updateTime (null, Time.valueOf("19:49:59"));
            failed ("Didn't throw SQLException");
          }
          catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
          }
        }
      }
    }


/**
updateTime() - Should throw an exception when the column
name is an empty string.
**/
    public void Var009()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateTime ("", Time.valueOf("09:59:34"));
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateTime() - Should throw an exception when the column
name is invalid.
**/
    public void Var010()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateTime ("INVALID", Time.valueOf("15:09:34"));
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateTime() - Should work when the column name is valid.
**/
    public void Var011()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            Time d = Time.valueOf("10:08:00");
            rs_.updateTime ("C_TIME", d);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            Time v = rs2.getTime ("C_TIME");
            rs2.close ();
            assertCondition (v.toString ().equals (d.toString ()));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateTime() - Should update to SQL NULL when the column
value is null.
**/
    public void Var012()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateTime ("C_TIME", null);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            Time v = rs2.getTime ("C_TIME");
            boolean wn = rs2.wasNull ();
            rs2.close ();
            assertCondition ((v == null) && (wn == true));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateTime() - Should be reflected by get, even if update has
not yet been issued (i.e. update is still pending).
**/
    public void Var013()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            Time d = Time.valueOf("14:04:00");
            rs_.updateTime ("C_TIME", d);
            Time v = rs_.getTime ("C_TIME");
            assertCondition (v.toString ().equals (d.toString ()));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
updateTime() - Should be reflected by get, after update has
been issued, but cursor has not been repositioned.
**/
    public void Var014()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            Time d = Time.valueOf("06:04:08");
            rs_.updateTime ("C_TIME", d);
            rs_.updateRow ();
            Time v = rs_.getTime ("C_TIME");
            assertCondition (v.toString ().equals (d.toString ()));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
updateTime() - Should be reflected by get, after update has
been issued and cursor has been repositioned.
**/
    public void Var015()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            Time d = Time.valueOf("04:08:30");
            rs_.updateTime ("C_TIME", d);
            rs_.updateRow ();
            rs_.beforeFirst ();
            JDRSTest.position (rs_, key_);
            Time v = rs_.getTime ("C_TIME");
            assertCondition (v.toString ().equals (d.toString ()));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
updateTime() - Should work when the current row is the insert
row.
**/
    public void Var016()
    {
        if (checkJdbc20 ()) {
        try {
            rs_.moveToInsertRow ();
            rs_.updateString ("C_KEY", "JDRSUpdateTime 1");
            Time d = Time.valueOf("10:08:30");
            rs_.updateTime ("C_Time", d);
            rs_.insertRow ();
            JDRSTest.position (rs_, "JDRSUpdateTime 1");
            Time v = rs_.getTime ("C_TIME");
            assertCondition (v.toString ().equals (d.toString ()));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateTime() - Should be reflected by get on insert row, even if
insert has not yet been issued (i.e. insert is still pending).
**/
    public void Var017()
    {
        if (checkJdbc20 ()) {
        try {
            rs_.moveToInsertRow ();
            Time d = Time.valueOf("04:08:05");
            rs_.updateTime ("C_TIME", d);
            Time v = rs_.getTime ("C_TIME");
            assertCondition (v.toString ().equals (d.toString ()));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }

           

/**
updateTime() - Should throw an exception on a deleted row.
**/
    public void Var018()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "DUMMY_ROW");
            rs_.deleteRow ();
            rs_.updateTime ("C_TIME", Time.valueOf("14:34:05"));
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }


/**
updateTime() - Update a SMALLINT.
**/
    public void Var019 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateTime ("C_SMALLINT", Time.valueOf("12:34:19"));
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateTime() - Update a INTEGER.
**/
    public void Var020 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateTime ("C_INTEGER", Time.valueOf("12:51:49"));
            failed ("Didn't throw SQLException");
       }
       catch (Exception e) {
           assertExceptionIsInstanceOf (e, "java.sql.SQLException");
       }
        }
    }



/**
updateTime() - Update a REAL.
**/
    public void Var021 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateTime ("C_REAL", Time.valueOf("14:44:19"));
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateTime() - Update a FLOAT.
**/
    public void Var022 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateTime ("C_FLOAT", Time.valueOf("18:41:05"));
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateTime() - Update a DOUBLE.
**/
    public void Var023 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateTime ("C_DOUBLE", Time.valueOf("03:41:05"));
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateTime() - Update a DECIMAL.
**/
    public void Var024 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateTime ("C_DECIMAL_105", Time.valueOf("12:34:49"));
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateTime() - Update a NUMERIC.
**/
    public void Var025 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateTime ("C_NUMERIC_105", Time.valueOf("1:0,:,49"));
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateTime() - Update a CHAR.

SQL400 - The native JDBC driver, be default, will show times in ISO format.
**/
    public void Var026 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            Time d = Time.valueOf("10:08:00");
            rs_.updateTime ("C_CHAR_50", d);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            String v = rs2.getString ("C_CHAR_50");
            rs2.close ();
            if (isToolboxDriver())
                assertCondition (v.equals ("10:08:00                                          "));
            else 
                assertCondition (v.equals ("10.08.00                                          "));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateTime() - Update a CHAR, when the value is too big.
**/
    public void Var027 ()
    {
        if (checkJdbc20 ()) {
        int expectedColumn = -1;
        try {
            JDRSTest.position (rs_, key_);
            expectedColumn = rs_.findColumn ("C_CHAR_1");
            Time d = Time.valueOf("08:00:00");
            rs_.updateTime ("C_CHAR_1", d);
            rs_.updateRow ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            DataTruncation dt = (DataTruncation)e;
            assertCondition ((dt.getIndex() == expectedColumn)
                && (dt.getParameter() == false)
                && (dt.getRead() == false)
                && (dt.getDataSize() == 8)
                && (dt.getTransferSize() == 1));

        }
        }
    }



/**
updateTime() - Update a VARCHAR.

SQL400 - The native JDBC driver, be default, will show times in ISO format.
**/
    public void Var028 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            Time d = Time.valueOf("09:22:40");
            rs_.updateTime ("C_VARCHAR_50", d);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            String v = rs2.getString ("C_VARCHAR_50");
            rs2.close ();
            if (isToolboxDriver())
                assertCondition (v.equals ("09:22:40"));
            else
                assertCondition (v.equals ("09.22.40"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateTime() - Update a BINARY.
**/
    public void Var029 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateTime ("C_BINARY_20", Time.valueOf("21:32:34"));
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateTime() - Update a VARBINARY.
**/
    public void Var030 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateTime ("C_VARBINARY_20", Time.valueOf("21:33:34"));
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateTime() - Update a CLOB.
**/
    public void Var031 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateTime ("C_CLOB", Time.valueOf("21:33:34"));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }



/**
updateTime() - Update a DBCLOB.
**/
    public void Var032 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateTime ("C_DBCLOB", Time.valueOf("23:03:34"));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }



/**
updateTime() - Update a BLOB.
**/
    public void Var033 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateTime ("C_BLOB", Time.valueOf("22:32:34"));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }



/**
updateTime() - Update a DATE.
**/
    public void Var034 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            Time d = Time.valueOf("01:09:22");
            rs_.updateTime ("C_DATE", d);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            java.sql.Date v = rs2.getDate ("C_DATE");
            rs2.close ();
            assertCondition (v.toString ().equals ("1970-01-01"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateTime() - Update a TIME.
**/
    public void Var035 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            Time d = Time.valueOf("21:33:24");
            rs_.updateTime ("C_TIME", d);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            Time v = rs2.getTime ("C_TIME");
            rs2.close ();
            assertCondition (v.toString ().equals (d.toString ()));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateTime() - Update a TIMESTAMP.
**/
    public void Var036 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            Time d = Time.valueOf("19:22:40");
            rs_.updateTime ("C_TIMESTAMP", d);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            java.sql.Date v = rs2.getDate ("C_TIMESTAMP");
            rs2.close ();
            assertCondition (v.toString ().equals ("1970-01-01"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateTime() - Update a DATALINK.
**/
    public void Var037 ()
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
                rs_.updateTime ("C_DATALINK", Time.valueOf("21:32:34"));
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
updateTime() - Update a DISTINCT.

SQL400 - The native JDBC driver, be default, will show times in ISO format.
**/
    public void Var038 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateTime ("C_DISTINCT", Time.valueOf("13:23:04"));
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_DISTINCT");
                rs2.close ();
                if (isToolboxDriver())
                    assertCondition (v.equals ("13:23:04 "));
                else 
                    assertCondition (v.equals ("13.23.04 "));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }



/**
updateTime() - Update a BIGINT.
**/
    public void Var039()
    {
        if (checkJdbc20 ()) {
        if (checkBigintSupport()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateTime ("C_BIGINT", Time.valueOf("12:51:49"));
            failed ("Didn't throw SQLException");
       }
       catch (Exception e) {
           assertExceptionIsInstanceOf (e, "java.sql.SQLException");
       }
        }
        }
    }

    /**
    updateTime() - Update a DFP16.
    **/
    public void Var040 ()
    {
      if(checkDecFloatSupport())
        try {
          Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
              ResultSet.CONCUR_UPDATABLE);
          ResultSet rs = s.executeQuery ("SELECT * FROM "
              + JDRSTest.RSTEST_DFP16+" FOR UPDATE ");
          rs.next(); 
          rs.updateTime (1, new Time(1423449495)); 
          failed ("Didn't throw SQLException");
        }
      catch (Exception e) {
        assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
    }


    /**
    updateTime() - Update a DFP34.
    **/
        public void Var041 ()
        {
          if(checkDecFloatSupport())
            try {
              Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                  ResultSet.CONCUR_UPDATABLE);
              ResultSet rs = s.executeQuery ("SELECT * FROM "
                  + JDRSTest.RSTEST_DFP34+" FOR UPDATE ");
              rs.next(); 
              rs.updateTime (1, new Time(1423449495)); 
              failed ("Didn't throw SQLException");
            }
          catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
          }
        }


/**
updateTime() - Update a BOOLEAN.
**/
    public void Var042()
    {
        if (checkJdbc20 ()) {
        if (checkBooleanSupport()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateTime ("C_BOOLEAN", Time.valueOf("12:51:49"));
            failed ("Didn't throw SQLException");
       }
       catch (Exception e) {
           assertExceptionIsInstanceOf (e, "java.sql.SQLException");
       }
        }
        }
    }



}



