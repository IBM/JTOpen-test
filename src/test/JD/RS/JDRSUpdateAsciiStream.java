///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSUpdateAsciiStream.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.RS;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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
import test.JDTestDriver;
import test.JDTestcase;
import test.JD.JDWeirdInputStream;



/**
Testcase JDRSUpdateAsciiStream.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>updateAsciiStream()
</ul>
**/
public class JDRSUpdateAsciiStream
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSUpdateAsciiStream";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



    // Private data.
    private static final String key_            = "JDRSUpdateAsciiStream";
    private static String select_         = "SELECT * FROM "
                                            + JDRSTest.RSTEST_UPDATE;

    private Statement           statement_;
    private Statement           statement2_;
    private ResultSet           rs_;



    /**
    Constructor.
    **/
    public JDRSUpdateAsciiStream (AS400 systemObject,
                                  Hashtable<String,Vector<String>> namesAndVars,
                                  int runMode,
                                  FileOutputStream fileOutputStream,
                                  
                                  String password)
    {
        super (systemObject, "JDRSUpdateAsciiStream",
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

        if(isJdbc20 ())
        {
            String url = baseURL_
                         
                          + ";data truncation=true"
                         + ";date format=iso";
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
        if(isJdbc20 ())
        {
            rs_.close ();
            statement_.close ();
            connection_.commit(); // @C1A
            connection_.close ();
        }
    }


    /**
    Returns an input stream containing the ASCII bytes for a string.
    **/
    private InputStream stringToAsciiStream (String s)
    throws UnsupportedEncodingException
    {
        return new ByteArrayInputStream (s.getBytes ("ISO8859_1"));
    }


    /**
    updateAsciiStream() - Should throw exception when the result set is
    closed.
    **/
    public void Var001()
    {
        if(checkJdbc20 ())
        {
            try
            {
                Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                           ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = s.executeQuery ("SELECT * FROM "
                                               + JDRSTest.RSTEST_UPDATE + " FOR UPDATE");
                rs.next ();
                rs.close ();
                rs.updateAsciiStream ("C_VARCHAR_50", stringToAsciiStream ("Mexico City"), 11);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateAsciiStream() - Should throw exception when the result set is
    not updatable.
    **/
    public void Var002()
    {
        if(checkJdbc20 ())
        {
            try
            {
                Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                           ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = s.executeQuery ("SELECT * FROM "
                                               + JDRSTest.RSTEST_UPDATE);
                rs.next ();
                rs.updateAsciiStream ("C_VARCHAR_50", stringToAsciiStream ("San Salvador"), 12);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateAsciiStream() - Should throw exception when cursor is not pointing
    to a row.
    **/
    public void Var003()
    {
      
      if(checkJdbc20 ())
      {
          try
          {
            JDRSTest.position (rs_, null);
            rs_.updateAsciiStream ("C_VARCHAR_50", stringToAsciiStream ("Buenos Aires"), 12);
            rs_.updateRow(); 
            failed ("Didn't throw SQLException when cursor not pointing to a row");
          }
          catch(Exception e)
          {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
          }
      }
    }


    /**
    updateAsciiStream() - Should throw an exception when the column
    is an invalid index.
    **/
    public void Var004()
    {
        if(checkJdbc20 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateAsciiStream (100, stringToAsciiStream ("Santiago"), 8);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateAsciiStream() - Should throw an exception when the column
    is 0.
    **/
    public void Var005()
    {
        if(checkJdbc20 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateAsciiStream (0, stringToAsciiStream ("Bogota"), 6);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateAsciiStream() - Should throw an exception when the column
    is -1.
    **/
    public void Var006()
    {
        if(checkJdbc20 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateAsciiStream (-1, stringToAsciiStream ("London"), 6);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateAsciiStream() - Should work when the column index is valid.
    **/
    public void Var007()
    {
        if(checkJdbc20 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateAsciiStream (14, stringToAsciiStream ("Dublin"), 6);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_VARCHAR_50");
                rs2.close ();
                assertCondition (v.equals ("Dublin"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateAsciiStream() - Should throw an exception when the column
    name is null.
    **/
    public void Var008()
    {
      if(checkJdbc20 ())
      {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC throws null pointer exception when column name is null "); 
        } else { 
          try
          {
            JDRSTest.position (rs_, key_);
            rs_.updateAsciiStream (null, stringToAsciiStream ("Glasgow"), 7);
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
    updateAsciiStream() - Should throw an exception when the column
    name is an empty string.
    **/
    public void Var009()
    {
        if(checkJdbc20 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateAsciiStream ("", stringToAsciiStream ("Barcelona"), 9);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateAsciiStream() - Should throw an exception when the column
    name is invalid.
    **/
    public void Var010()
    {
        if(checkJdbc20 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateAsciiStream ("INVALID", stringToAsciiStream ("Madrid"), 6);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateAsciiStream() - Should work when the column name is valid.
    **/
    public void Var011()
    {
        if(checkJdbc20 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateAsciiStream ("C_VARCHAR_50", stringToAsciiStream ("Ibiza"), 5);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_VARCHAR_50");
                rs2.close ();
                assertCondition (v.equals ("Ibiza"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateAsciiStream() - Should update to SQL NULL when the column
    value is null.
    **/
    public void Var012()
    {
      if(checkJdbc20 ())
      {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC throws null pointer exception when updating NULL ascii stream "); 
        } else { 
          try
          {
            JDRSTest.position (rs_, key_);
            rs_.updateAsciiStream ("C_VARCHAR_50", null, 0);
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
    }


    /**
    updateAsciiStream() - Should throw an exception when the length is invalid.
    **/
    public void Var013()
    {
        if(checkJdbc20 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateAsciiStream ("C_VARCHAR_50", stringToAsciiStream ("Paris"), -1);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateAsciiStream() - Should be reflected by get, even if update has
    not yet been issued (i.e. update is still pending).
    **/
    public void Var014()
    {
        if(checkJdbc20 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateAsciiStream ("C_VARCHAR_50", stringToAsciiStream ("Rotterdam"), 9);
                assertCondition (rs_.getString ("C_VARCHAR_50").equals ("Rotterdam"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }




    /**
    updateAsciiStream() - Should be reflected by get, after update has
    been issued, but cursor has not been repositioned.
    **/
    public void Var015()
    {
        if(checkJdbc20 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateAsciiStream ("C_VARCHAR_50", stringToAsciiStream ("Amsterdam"), 9);
                rs_.updateRow ();
                assertCondition (rs_.getString ("C_VARCHAR_50").equals ("Amsterdam"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }




    /**
    updateAsciiStream() - Should be reflected by get, after update has
    been issued and cursor has been repositioned.
    **/
    public void Var016()
    {
        if(checkJdbc20 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateAsciiStream ("C_VARCHAR_50", stringToAsciiStream ("Copenhagen"), 10);
                rs_.updateRow ();
                rs_.beforeFirst ();
                JDRSTest.position (rs_, key_);
                String v = rs_.getString ("C_VARCHAR_50");
                assertCondition (v.equals ("Copenhagen"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }




    /**
    updateAsciiStream() - Should work when the current row is the insert
    row.
    **/
    public void Var017()
    {
      if(checkJdbc20 ())
      {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC does not support move to insert row"); 
        } else { 
          try
          {
            rs_.moveToInsertRow ();
            rs_.updateString ("C_KEY", "JDRSUpdateAsciiStream 1");
            rs_.updateAsciiStream ("C_VARCHAR_50", stringToAsciiStream ("Brussels"), 8);
            rs_.insertRow ();
            JDRSTest.position (rs_, "JDRSUpdateAsciiStream 1");
            assertCondition (rs_.getString ("C_VARCHAR_50").equals ("Brussels"));
          }
          catch(Exception e)
          {
            failed (e, "Unexpected Exception");
          }
        }
      }
    }


    /**
    updateAsciiStream() - Should be reflected by get on insert row, even if
    insert has not yet been issued (i.e. insert is still pending).
    **/
    public void Var018()
    {
      if(checkJdbc20 ())
      {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC does not support move to insert row"); 
        } else { 
          try
          {
            rs_.moveToInsertRow ();
            rs_.updateAsciiStream ("C_VARCHAR_50", stringToAsciiStream ("Oslo"), 4);
            assertCondition (rs_.getString ("C_VARCHAR_50").equals ("Oslo"));
          }
          catch(Exception e)
          {
            failed (e, "Unexpected Exception");
          }
        }
      }
    }


    /**
    updateAsciiStream() - Should throw an exception on a deleted row.
    **/
    public void Var019()
    {
        if(checkJdbc20 ())
        {
            try
            {
                JDRSTest.position (rs_, "DUMMY_ROW");
                rs_.deleteRow ();
                rs_.updateAsciiStream ("C_VARCHAR_50", stringToAsciiStream ("Venice"), 6);
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
    updateAsciiStream() - Update a SMALLINT.
    **/
    public void Var020 ()
    {
      if(checkJdbc20 ())
      {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC does not conversion from asciiStream to smallint on update"); 
        } else { 
          
          try
          {
            JDRSTest.position (rs_, key_);
            rs_.updateAsciiStream ("C_SMALLINT", stringToAsciiStream ("110"), 3);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            short v = rs2.getShort ("C_SMALLINT");
            rs2.close ();
            assertCondition (v == 110);
          }
          catch(Exception e)
          {
            failed (e, "Unexpected Exception");
          }
        }
      }
    }



    /**
    updateAsciiStream() - Update a SMALLINT, when the integer is too big.
    **/
    public void Var021 ()
    {
        if(checkJdbc20 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateAsciiStream ("C_SMALLINT", stringToAsciiStream ("234399"), 6);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateAsciiStream() - Update a SMALLINT, when the string is not a number.
    **/
    public void Var022 ()
    {
        if(checkJdbc20 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateAsciiStream ("C_SMALLINT", stringToAsciiStream ("Milan"), 5);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateAsciiStream() - Update an INTEGER.
    **/
    public void Var023 ()
    {
      if(checkJdbc20 ())
      {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC does not conversion from asciiStream to integer on update"); 
        } else { 
          try
          {
            JDRSTest.position (rs_, key_);
            rs_.updateAsciiStream ("C_INTEGER", stringToAsciiStream ("-2377"), 5);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            int v = rs2.getInt ("C_INTEGER");
            rs2.close ();
            assertCondition (v == -2377);
          }
          catch(Exception e)
          {
            failed (e, "Unexpected Exception");
          }
        }
      }
    }


    /**
    updateAsciiStream() - Update an INTEGER, when the integer is too big.
    **/
    public void Var024 ()
    {
        if(checkJdbc20 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateAsciiStream ("C_INTEGER", stringToAsciiStream ("43533487623"), 11);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateAsciiStream() - Update an INTEGER, when the string is not a number.
    **/
    public void Var025 ()
    {
        if(checkJdbc20 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateAsciiStream ("C_INTEGER", stringToAsciiStream ("Moscow"), 6);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateAsciiStream() - Update a REAL.
    **/
    public void Var026 ()
    {
      if(checkJdbc20 ())
      {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC does not conversion from asciiStream to real on update"); 
        } else { 
          try
          {
            JDRSTest.position (rs_, key_);
            rs_.updateAsciiStream ("C_REAL", stringToAsciiStream ("213.355"), 7);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            float v = rs2.getFloat ("C_REAL");
            rs2.close ();
            assertCondition (v == 213.355f);
          }
          catch(Exception e)
          {
            failed (e, "Unexpected Exception");
          }
        }
      }
    }

    /**
    updateAsciiStream() - Update a REAL, when the number is too big.
    
    Note:  Given the new data truncation rules for JDBC, this testcase
           is expected to work without throwing a DataTruncation exception.
    **/
    public void Var027 ()
    {
      if(checkJdbc20 ())
      {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC does not conversion from asciiStream to real on update"); 
        } else { 
          
          try
          {
            JDRSTest.position (rs_, key_);
            rs_.findColumn ("C_REAL");
            rs_.updateAsciiStream ("C_REAL", stringToAsciiStream ("4533335.44323"), 13);
            rs_.updateRow ();
            
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            float v = rs2.getFloat ("C_REAL");
            rs2.close ();
            assertCondition (v == 4533335.5);
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
         && (dt.getDataSize() == 12)
         && (dt.getTransferSize() == 8));
         
         }
         }
         */
      }
    }


    /**
    updateAsciiStream() - Update a REAL, when the string is not a number.
    **/
    public void Var028 ()
    {
        if(checkJdbc20 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateAsciiStream ("C_INTEGER", stringToAsciiStream ("Tokyo"), 5);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }




    /**
    updateAsciiStream() - Update a FLOAT.
    **/
    public void Var029 ()
    {
      if(checkJdbc20 ())
      {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC does not conversion from asciiStream to float on update"); 
        } else { 
          try
          {
            JDRSTest.position (rs_, key_);
            rs_.updateAsciiStream ("C_FLOAT", stringToAsciiStream ("9.8352"), 6);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            float v = rs2.getFloat ("C_FLOAT");
            rs2.close ();
            assertCondition (v == 9.8352f);
          }
          catch(Exception e)
          {
            failed (e, "Unexpected Exception");
          }
        }
      }
    }


    /**
     updateAsciiStream() - Update a FLOAT, when the number is too big.
     
     Note:  Given the new data truncation rules for JDBC, this testcase
     is expected to work without throwing a DataTruncation exception.
     **/
    public void Var030 ()
    {
      if(checkJdbc20 ())
      {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC does not conversion from asciiStream to float on update"); 
        } else { 
          
          try
          {
            JDRSTest.position (rs_, key_);
            rs_.findColumn ("C_FLOAT");
            rs_.updateAsciiStream ("C_FLOAT", stringToAsciiStream ("43435.94434556337633"), 20);
            rs_.updateRow ();
            
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            float v = rs2.getFloat ("C_FLOAT");
            rs2.close ();
            // Can't seem to test the rounding error out of this just right.
            assertCondition ((v > 43435) && (v < 43436));
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
    }


    /**
    updateAsciiStream() - Update a FLOAT, when the string is not a number.
    **/
    public void Var031 ()
    {
        if(checkJdbc20 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateAsciiStream ("C_FLOAT", stringToAsciiStream ("Hong Kong"), 9);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


    /**
    updateAsciiStream() - Update a DOUBLE.
    **/
    public void Var032 ()
    {
      if(checkJdbc20 ())
      {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC does not conversion from asciiStream to double on update"); 
        } else { 
          
          try
          {
            JDRSTest.position (rs_, key_);
            rs_.updateAsciiStream ("C_DOUBLE", stringToAsciiStream ("-19845"), 6);
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
    }


    /**
    updateAsciiStream() - Update a DOUBLE, when the number is too big.
    
    Note:  Given the new data truncation rules for JDBC, this testcase
           is expected to work without throwing a DataTruncation exception.
    **/
    public void Var033 ()
    {
      if(checkJdbc20 ())
      {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC does not conversion from asciiStream to double on update"); 
        } else { 
          
          try
          {
            JDRSTest.position (rs_, key_);
            rs_.findColumn ("C_DOUBLE");
            rs_.updateAsciiStream ("C_DOUBLE", stringToAsciiStream ("4342035.4434445532333"), 21);
            rs_.updateRow ();
            
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            float v = rs2.getFloat ("C_DOUBLE");
            rs2.close ();
            assertCondition (v == 4342035.5);
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
    }


    /**
    updateAsciiStream() - Update a DOUBLE, when the string is not a number.
    **/
    public void Var034 ()
    {
        if(checkJdbc20 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateAsciiStream ("C_DOUBLE", stringToAsciiStream ("Sydney"), 6);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


    /**
    updateAsciiStream() - Update a DECIMAL.
    **/
    public void Var035 ()
    {
      if(checkJdbc20 ())
      {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC does not conversion from asciiStream to decimal on update"); 
        } else { 
          try
          {
            JDRSTest.position (rs_, key_);
            rs_.updateAsciiStream ("C_DECIMAL_105", stringToAsciiStream ("453.956"), 7);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            BigDecimal v = rs2.getBigDecimal ("C_DECIMAL_105");
            rs2.close ();
            assertCondition (v.floatValue() == 453.956f);
          }
          catch(Exception e)
          {
            failed (e, "Unexpected Exception");
          }
        }
      }
    }


    /**
    updateAsciiStream() - Update a DECIMAL, when the value is too big.
    **/
    public void Var036 ()
    {
      if(checkJdbc20 ())
      {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC does not conversion from asciiStream to decimal on update"); 
        } else { 
          int expectedColumn = -1;
          try
          {
            JDRSTest.position (rs_, key_);
            expectedColumn = rs_.findColumn ("C_DECIMAL_40");
            rs_.updateAsciiStream ("C_DECIMAL_40", stringToAsciiStream ("534323644531"), 12);
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
                  && (dt.getDataSize() == 12)
                  && (dt.getTransferSize() == 4));
            } else {
	      assertSqlException(e, -99999, "07006", "Data type mismatch",
				 "Mismatch instead of truncation in latest toolbox");
            }
            
          }
        }
      }
    }
    /**
    updateAsciiStream() - Update a DECIMAL, when the string is not a number.
    **/
    public void Var037 ()
    {
        if(checkJdbc20 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateAsciiStream ("C_DECIMAL_105", stringToAsciiStream ("Auckland"), 8);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateAsciiStream() - Update a NUMERIC.
    **/
    public void Var038 ()
    {
      if(checkJdbc20 ())
      {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC does not conversion from asciiStream to numeric on update"); 
        } else { 
          try
          {
            JDRSTest.position (rs_, key_);
            rs_.updateAsciiStream ("C_NUMERIC_105", stringToAsciiStream ("-93.4553"), 8);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            BigDecimal v = rs2.getBigDecimal ("C_NUMERIC_105");
            rs2.close ();
            assertCondition (v.doubleValue() == -93.4553);
          }
          catch(Exception e)
          {
            failed (e, "Unexpected Exception");
          }
        }
      }
    }


    /**
    updateAsciiStream() - Update a NUMERIC, when the value is too big.
    **/
    public void Var039 ()
    {
      if(checkJdbc20 ())
      {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC does not conversion from asciiStream to numeric on update"); 
        } else { 
          int expectedColumn = -1;
          try
          {
            JDRSTest.position (rs_, key_);
            expectedColumn = rs_.findColumn ("C_NUMERIC_40");
            rs_.updateAsciiStream ("C_NUMERIC_40", stringToAsciiStream ("-151453234"), 10);
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
                && (dt.getTransferSize() == 4));
	      } else {
		  assertSqlException(e, -99999, "07006", "Data type mismatch",
				     "Mismatch instead of truncation in latest toolbox");

	      }
            
          }
        }
      }
    }


    /**
    updateAsciiStream() - Update a NUMERIC, when the string is not a number.
    **/
    public void Var040 ()
    {
        if(checkJdbc20 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateAsciiStream ("C_NUMERIC_105", stringToAsciiStream ("Lima"), 4);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateAsciiStream() - Update a CHAR.
    **/
    public void Var041 ()
    {
        if(checkJdbc20 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateAsciiStream ("C_CHAR_50", stringToAsciiStream ("Manchester"), 10);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_CHAR_50");
                rs2.close ();
                assertCondition (v.equals ("Manchester                                        "));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateAsciiStream() - Update a CHAR, when the value is too big.
    **/
    public void Var042 ()
    {
        if(checkJdbc20 ())
        {
            int expectedColumn = -1;
            try
            {
                JDRSTest.position (rs_, key_);
                expectedColumn = rs_.findColumn ("C_CHAR_1");
                rs_.updateAsciiStream ("C_CHAR_1", stringToAsciiStream ("Edinburgh"), 9);
                rs_.updateRow ();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
              if (e  instanceof DataTruncation) {
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
                if ( getDriver() == JDTestDriver.DRIVER_JCC) {
                  String message = e.toString(); 
                  if (message.indexOf("302") >= 0) { 
                    assertCondition(true, "Conversion error"); 
                  } else { 
                    failed(e, "Unexpected exception");
                 
                  }
                } else { 
                    failed(e, "Unexpected exception");
                }       
              }

            }
        }
    }


    /**
    updateAsciiStream() - Update a VARCHAR, with the length equal to the
    full stream.
    **/
    public void Var043 ()
    {
        if(checkJdbc20 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateAsciiStream ("C_VARCHAR_50", stringToAsciiStream ("Cornwall"), 8);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_VARCHAR_50");
                rs2.close ();
                assertCondition (v.equals ("Cornwall"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateAsciiStream() - Update a VARCHAR, with the length less than the
    full stream.

     @D2 - Native driver doesn't throw an exception if stream contains extra characters then there needs to be there.
    **/
    public void Var044 ()
    {
        if(checkJdbc20 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateAsciiStream ("C_VARCHAR_50", stringToAsciiStream ("Liverpool"), 6);
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
    updateAsciiStream() - Update a VARCHAR, with the length greater than the
    full stream.
    **/
    public void Var045 ()
    {
        if(checkJdbc20 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateAsciiStream ("C_VARCHAR_50", stringToAsciiStream ("Leeds"), 19);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateAsciiStream() - Update a VARCHAR, with the length set to 1.

     @D2 - Native driver doesn't throw an exception if stream contains extra characters then there needs to be there.
    **/
    public void Var046 ()
    {
        if(checkJdbc20 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateAsciiStream ("C_VARCHAR_50", stringToAsciiStream ("Nice"), 1);
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
    updateAsciiStream() - Update a VARCHAR, with the length set to 0.

     @D2 - Native driver doesn't throw an exception if stream contains extra characters then there needs to be there.
    **/
    public void Var047 ()
    {
        if(checkJdbc20 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateAsciiStream ("C_VARCHAR_50", stringToAsciiStream ("Lyon"), 0);
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
    updateAsciiStream() - Update a VARCHAR, with an empty string.
    **/
    public void Var048 ()
    {
        if(checkJdbc20 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateAsciiStream ("C_VARCHAR_50", stringToAsciiStream (""), 0);
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
    updateAsciiStream() - Update a VARCHAR, when the value is too big.
    **/
    public void Var049 ()
    {
        if(checkJdbc20 ())
        {
            int expectedColumn = -1;
            try
            {
                JDRSTest.position (rs_, key_);
                expectedColumn = rs_.findColumn ("C_VARCHAR_50");
                rs_.updateAsciiStream ("C_VARCHAR_50", stringToAsciiStream ("The Year 2000 problem will surely be the end of the computing industry as we know it."), 85);
                rs_.updateRow ();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
              if (e  instanceof DataTruncation) {

                DataTruncation dt = (DataTruncation)e;
                assertCondition ((dt.getIndex() == expectedColumn)
                                 && (dt.getParameter() == false)
                                 && (dt.getRead() == false)
                                 && (dt.getDataSize() == 85)
                                 && (dt.getTransferSize() == 50),
				 "dt.getIndex()="+dt.getIndex()+" sb "+expectedColumn+"\n"+
				 "dt.getParameter()="+dt.getParameter()+" sb false\n" +
				 "dt.getRead()="+dt.getRead()+" sb false\n"+
				 "dt.getDataSize()="+dt.getDataSize()+" sb 85\n"+
                                 "dt.getTransferSize()="+dt.getTransferSize()+"sb 50");
              } else {
                if ( getDriver() == JDTestDriver.DRIVER_JCC) {
                  String message = e.toString(); 
                  if (message.indexOf("302") >= 0) { 
                    assertCondition(true, "Conversion error"); 
                  } else { 
                    failed(e, "Unexpected exception");
                 
                  }
                } else { 
                  failed(e, "Unexpected exception");
                }
              }

            }
        }
    }



    /**
    updateAsciiStream() - Update a VARCHAR parameter to a bad input stream.
    **/
    public void Var050()
    {
        if(checkJdbc20 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);

                class BadInputStream extends InputStream
                {
 
                    public BadInputStream ()
                    {
                        super ();
                    }                
                    public int available () throws IOException {
                        throw new IOException (); 
                    };
                    public int read () throws IOException {
                        throw new IOException (); 
                    };
                    public int read (byte[] buffer) throws IOException {
                        throw new IOException (); 
                    };
                }

                InputStream r = new BadInputStream ();
                rs_.updateAsciiStream ("C_VARCHAR_50", r, 2);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }




    /**
    updateAsciiStream() - Update a BINARY.
    **/
    public void Var051 ()
    {
      if(checkJdbc20 ())
      {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC does not conversion from asciiStream to binary on update"); 
        } else { 
          try
          {
            String expected;
            if(isToolboxDriver())
              expected = "F1F2";
            else
              expected = "Ford";
            
            JDRSTest.position (rs_, key_);
            rs_.updateAsciiStream ("C_BINARY_20", stringToAsciiStream (expected), 4);
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
    }


    /**
    updateAsciiStream() - Update a BINARY, when the value is too big.
    **/
    public void Var052 ()
    {
      if(checkJdbc20 ())
      {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          
          notApplicable("JCC does not conversion from asciiStream to binary on update"); 
        } else { 
          
          int expectedColumn = -1;
          try
          {
            JDRSTest.position (rs_, key_);
            expectedColumn = rs_.findColumn ("C_BINARY_1");
            rs_.updateAsciiStream ("C_BINARY_1", stringToAsciiStream ("F1F2F3"), 6);
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
              failed(e, "Unexpected exception"); 
            }
            
          }
        }
        
      }
    }


    /**
    updateAsciiStream() - Update a VARBINARY.
    **/
    public void Var053 ()
    {
      if(checkJdbc20 ())
      {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC does not conversion from asciiStream to varbinary on update"); 
        } else { 
          try
          {
            String expected;
            if(isToolboxDriver())
              expected = "F1F2";
            else
              expected = "Ford";
            
            JDRSTest.position (rs_, key_);
            rs_.updateAsciiStream ("C_VARBINARY_20", stringToAsciiStream (expected), 4);
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
    }


    /**
    updateAsciiStream() - Update a VARBINARY, when the value is too big.
    **/
    public void Var054 ()
    {
        if(checkJdbc20 ())
        {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            notApplicable("JCC does not conversion from asciiStream to varbinary on update"); 
          } else { 
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
              rs_.updateAsciiStream ("C_VARBINARY_20", stringToAsciiStream (expected), 60);
              rs_.updateRow ();
              failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
              DataTruncation dt = (DataTruncation)e;
              assertCondition ((dt.getIndex() == expectedColumn)
                  && (dt.getParameter() == false)
                  && (dt.getRead() == false)
                  && (dt.getTransferSize() == 20),
				 "dt.getIndex()="+dt.getIndex()+" sb "+expectedColumn+"\n"+
				 "dt.getParameter()="+dt.getParameter()+" sb false\n" +
				 "dt.getRead()="+dt.getRead()+" sb false\n"+
				 
                                 "dt.getTransferSize()="+dt.getTransferSize()+"sb 20");
              
            }
          }
        }
        
    }

    /**
    updateAsciiStream() - Update a CLOB.
    
    SQL400 - the native driver expects this update to work correctly.
    **/
    public void Var055 ()
    {
        if(checkJdbc20 ())
        {
            if(checkLobSupport ())
            {
                try
                {
                    JDRSTest.position (rs_, key_);
                    rs_.updateAsciiStream ("C_CLOB", stringToAsciiStream ("Havana"), 6);

                    rs_.updateRow ();
                    ResultSet rs2 = statement2_.executeQuery (select_);
                    JDRSTest.position (rs2, key_);
                    String v = rs2.getString ("C_CLOB");
                    rs2.close ();
                    assertCondition (v.equals ("Havana"));
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



    /**
    updateAsciiStream() - Update a DBCLOB.
    
    SQL400 - the native driver expects this update to work correctly.
    **/
    public void Var056 ()
    {
        if(checkJdbc20 ())
        {
            if(checkLobSupport ())
            {
                try
                {
                    JDRSTest.position (rs_, key_);
                    rs_.updateAsciiStream ("C_DBCLOB", stringToAsciiStream ("San Jose"), 8);

                    rs_.updateRow ();
                    ResultSet rs2 = statement2_.executeQuery (select_);
                    JDRSTest.position (rs2, key_);
                    String v = rs2.getString ("C_DBCLOB");
                    rs2.close ();
                    assertCondition (v.equals ("San Jose"));
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



    /**
    updateAsciiStream() - Update a BLOB.
    Toolbox will fail because the string is not valid Hex -- see Var069
    **/
    public void Var057 ()
    {
        if(checkJdbc20 ())
        {
            if(checkLobSupport ())
            {
                try
                {
                    JDRSTest.position (rs_, key_);
                    rs_.updateAsciiStream ("C_BLOB", stringToAsciiStream ("Guadalajara"), 11);
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
    updateAsciiStream() - Update a DATE.
    **/
    public void Var058 ()
    {
      if(checkJdbc20 ())
      {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC does not conversion from asciiStream to date on update"); 
        } else { 
          try
          {
            JDRSTest.position (rs_, key_);
            rs_.updateAsciiStream ("C_DATE", stringToAsciiStream ("1972-01-12"), 10);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            Date v = rs2.getDate ("C_DATE");
            rs2.close ();
            assertCondition (v.toString ().equals ("1972-01-12"));
          }
          catch(Exception e)
          {
            failed (e, "Unexpected Exception");
          }
        }
      }
    }


    /**
    updateAsciiStream() - Update a DATE, when the string is not a valid date.
    **/
    public void Var059 ()
    {
        if(checkJdbc20 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateAsciiStream ("C_DATE", stringToAsciiStream ("Bombay"), 6);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateAsciiStream() - Update a TIME.
    **/
    public void Var060 ()
    {
      if(checkJdbc20 ())
      {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC does not conversion from asciiStream to time on update"); 
        } else { 
          try
          {
            JDRSTest.position (rs_, key_);
            rs_.updateAsciiStream ("C_TIME", stringToAsciiStream ("07:51:12"), 8);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            Time v = rs2.getTime ("C_TIME");
            rs2.close ();
            assertCondition (v.toString ().equals ("07:51:12"));
          }
          catch(Exception e)
          {
            failed (e, "Unexpected Exception");
          }
        }
      }
    }


    /**
    updateAsciiStream() - Update a TIME, when the string is not a valid time.
    **/
    public void Var061 ()
    {
        if(checkJdbc20 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateAsciiStream ("C_TIME", stringToAsciiStream ("Delhi"), 5);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
     updateAsciiStream() - Update a TIMESTAMP.
     **/
    public void Var062 ()
    {
      if(checkJdbc20 ())
      {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC does not conversion from asciiStream to timestamp on update"); 
        } else { 
          try
          {
            JDRSTest.position (rs_, key_);
            rs_.updateAsciiStream ("C_TIMESTAMP", stringToAsciiStream ("2002-10-31 00:00:00.000002"), 26);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            Timestamp v = rs2.getTimestamp ("C_TIMESTAMP");
            rs2.close ();
            assertCondition (v.toString ().equals ("2002-10-31 00:00:00.000002"));
          }
          catch(Exception e)
          {
            failed (e, "Unexpected Exception");
          }
        }
      }
    }


    /**
    updateAsciiStream() - Update a TIMESTAMP, when the string is not a valid timestamp.
    **/
    public void Var063 ()
    {
        if(checkJdbc20 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                rs_.updateAsciiStream ("C_TIMESTAMP", stringToAsciiStream ("Melbourne"), 9);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateAsciiStream() - Update a DATALINK.
    **/
    public void Var064 ()
    {
        if(checkJdbc20 ())
        {
            if(checkDatalinkSupport ())
            {
                // We do not test updating datalinks, since it is not 
                // possible to open a updatable cursor/result set with
                // a datalink column.
                notApplicable("DATALINK update not supported.");
                /*
                try {
                    JDRSTest.position (rs_, key_);
                    rs_.updateAsciiStream ("C_DATALINK", stringToAsciiStream ("Frankfurt"), 9);
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
    updateAsciiStream() - Update a DISTINCT.
    **/
    public void Var065 ()
    {
        if(checkJdbc20 ())
        {
            if(checkLobSupport ())
            {
                try
                {
                    JDRSTest.position (rs_, key_);
                    rs_.updateAsciiStream ("C_DISTINCT", stringToAsciiStream ("Vienna"), 6);
                    rs_.updateRow ();
                    ResultSet rs2 = statement2_.executeQuery (select_);
                    JDRSTest.position (rs2, key_);
                    String v = rs2.getString ("C_DISTINCT");
                    rs2.close ();
                    assertCondition (v.equals ("Vienna   "));
                }
                catch(Exception e)
                {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



    /**
    updateAsciiStream() - Update a BIGINT.
    **/
    public void Var066 ()
    {
      if(checkJdbc20 ())
      {
        if(checkBigintSupport())
        {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            notApplicable("JCC does not conversion from asciiStream to bigint on update"); 
          } else { 
            try
            {
              JDRSTest.position (rs_, key_);
              rs_.updateAsciiStream ("C_BIGINT", stringToAsciiStream ("-223776221432"), 13);
              rs_.updateRow ();
              ResultSet rs2 = statement2_.executeQuery (select_);
              JDRSTest.position (rs2, key_);
              long v = rs2.getLong ("C_BIGINT");
              rs2.close ();
              assertCondition (v == -223776221432l);
            }
            catch(Exception e)
            {
              failed (e, "Unexpected Exception");
            }
          }
        }
      }
    }


    /**
    updateAsciiStream() - Update an BIGINT, when the integer is too big.
    **/
    public void Var067()
    {
        if(checkJdbc20 ())
        {
            if(checkBigintSupport())
            {
                try
                {
                    JDRSTest.position (rs_, key_);
                    rs_.updateAsciiStream ("C_BIGINT", stringToAsciiStream ("43533487623321345678990754"), 26);
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
    updateAsciiStream() - Update an BIGINT, when the string is not a number.
    **/
    public void Var068()
    {
        if(checkJdbc20 ())
        {
            if(checkBigintSupport())
            {
                try
                {
                    JDRSTest.position (rs_, key_);
                    rs_.updateAsciiStream ("C_BIGINT", stringToAsciiStream ("Hannover"), 8);
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
    updateAsciiStream() - Update a BLOB.
    Native will fail because they do not support  -- see Var057
    **/
    public void Var069 ()
    {
        if(checkJdbc20 ())
        {
            if(checkLobSupport ())
            {
                try
                {
                    JDRSTest.position (rs_, key_);
                    rs_.updateAsciiStream ("C_BLOB", stringToAsciiStream("1023AAFFB10"), 11);
                    if(isToolboxDriver())
                    {
                        succeeded();
                    }
                    else
                    {
                        failed ("Didn't throw SQLException");
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
                        assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                    }
                }
            }
        }
    }


    /**
    updateAsciiStream() - Should work when the column index is valid.
    **/
    public void Var070()
    { 
        if(checkJdbc20 ())
        {
            String added = "Added by native driver 10/11/2006 to test input stream that sometimes returns 0 bytes "; 

            try
            {
                JDRSTest.position (rs_, key_);
		InputStream is = new JDWeirdInputStream("0102030");
        //toolbox expects correct size
        try{
                rs_.updateAsciiStream (14, is, 6);
        }catch(SQLException e){
            if(isToolboxDriver())
            {
                assertCondition(e.getMessage().endsWith("buffer length not valid."));
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
                assertCondition (check != null && check.equals (expected), "\nExpected :'"+expected+"'" +
                                                          "\nGot      :'"+check+"'" + added );
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }


    /**
     updateAsciiStream() - Should work when the column index is valid and only part of stream is read
     **/
    public void Var071()
    {
      if(checkJdbc20 ())
      {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC does not handle input stream that does not return fall bytes at one time"); 
        } else { 
          
          String added = "Added by native driver 10/11/2006 to test input stream that sometimes returns 0 bytes "; 
          //toolbox expects correct size
          try
          {
            JDRSTest.position (rs_, key_);
            InputStream is = new JDWeirdInputStream("0102030");
            try{
                rs_.updateAsciiStream (14, is, 5);
            }catch(SQLException e){
                if(isToolboxDriver())
                {
                    assertCondition(e.getMessage().endsWith("buffer length not valid."));
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
            String expected = " !\"#$";
            assertCondition (check != null && check.equals (expected), "\nExpected :'"+expected+"'" +
                "\nGot      :'"+check+"'" + added );
          }
          catch(Exception e)
          {
            failed (e, "Unexpected Exception");
          }
        }
      }
    }

    /**
    updateAsciiStream() - Update DFP16:
    **/
   public void Var072 ()
   {
     if (checkDecFloatSupport()) {
       try {
         Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
             ResultSet.CONCUR_UPDATABLE);
         ResultSet rs = s.executeQuery ("SELECT * FROM "
             + JDRSTest.RSTEST_DFP16+" FOR UPDATE ");
         rs.next(); 
         rs.updateAsciiStream(1, stringToAsciiStream ("Hannover"), 8 );
	 rs.updateRow(); 
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
   updateAsciiStream() - Update DFP34:
   **/

  public void Var073 ()
  {
    if (checkDecFloatSupport()) {
      try {
        Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = s.executeQuery ("SELECT * FROM "
            + JDRSTest.RSTEST_DFP34+" FOR UPDATE ");
        rs.next(); 
        rs.updateAsciiStream(1, stringToAsciiStream ("Hannover"), 8 ); 
	rs.updateRow(); 
        failed ("Didn't throw SQLException for 'Hannover' ");
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
   * updateAsciiStream() - Update a Boolean
   **/
  public void Var074() {
    if (checkBooleanSupport()) {
      try {
        JDRSTest.position(rs_, key_);
        rs_.updateAsciiStream("C_BOOLEAN", stringToAsciiStream("1"), 1);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  

}



