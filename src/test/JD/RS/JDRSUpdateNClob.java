///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSUpdateNClob.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.JD.RS;

import java.io.FileOutputStream;
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



/**
Testcase JDRSUpdateNClob.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>updateNClob()
</ul>
**/
public class JDRSUpdateNClob
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSUpdateNClob";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



    // Private data.
    private static final String key_            = "JDRSUpdateNClob";
    private static String select_         = "SELECT * FROM "
                                            + JDRSTest.RSTEST_UPDATE;

    private Statement           statement_;
    private Statement           statement2_;
    private ResultSet           rs_;



    /**
    Constructor.
    **/
    public JDRSUpdateNClob (AS400 systemObject,
                                      Hashtable<String,Vector<String>> namesAndVars,
                                      int runMode,
                                      FileOutputStream fileOutputStream,
                                      
                                      String password)
    {
        super (systemObject, "JDRSUpdateNClob",
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
    updateNClob() - Should throw exception when the result set is
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

                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Juneau"); 
                JDReflectionUtil.callMethod_V(rs, "updateNClob", "C_VARCHAR_50", nClob);// "Juneau"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNClob() - Should throw exception when the result set is
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
                
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Tacoma"); 
                
                JDReflectionUtil.callMethod_V(rs, "updateNClob", "C_VARCHAR_50", nClob);//"Tacoma"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNClob() - Should throw exception when cursor is not pointing
    to a row.
    **/
    public void Var003()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, null);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Portland"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_VARCHAR_50", nClob);//"Portland"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNClob() - Should throw an exception when the column
    is an invalid index.
    **/
    public void Var004()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "San Francisco"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", 100, nClob);//"San Francisco"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNClob() - Should throw an exception when the column
    is 0.
    **/
    public void Var005()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Boise"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", 0, nClob);// "Boise"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNClob() - Should throw an exception when the column
    is -1.
    **/
    public void Var006()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Helena"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", -1, nClob);// "Helena"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNClob() - Should work when the column index is valid.
    **/
    public void Var007()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Fargo"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", 14, nClob);// "Fargo"));
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
    updateNClob() - Should throw an exception when the column
    name is null.
    **/
    public void Var008()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Sioux Falls"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", null, nClob);//"Sioux Falls"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNClob() - Should throw an exception when the column
    name is an empty string.
    **/
    public void Var009()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Lincoln"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "", nClob);//"Lincoln"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**    updateNClob() - Should throw an exception when the column
    name is invalid.
    **/
    public void Var010()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Colorado Springs"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "INVALID", nClob);// "Colorado Springs"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNClob() - Should work when the column name is valid.
    **/
    public void Var011()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Gillette"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_VARCHAR_50", nClob);// "Gillette"));
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
    updateNClob() - Should update to SQL NULL when the column
    value is null.
    **/
    public void Var012()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Class<?>[] argClasses = new Class<?>[2]; 
                argClasses[0] = String.class;
                try { 
                    argClasses[1] = Class.forName("java.sql.NClob"); 
                } catch (Exception e) {
                    System.out.println("Warning :"+e); 
                    argClasses[1] = Class.forName("com.ibm.db2.jdbc.app.NClob"); 
                }
                Object[] args = new Object[2]; 
                args[0] = "C_VARCHAR_50"; 
                args[1] = null; 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", argClasses, args);
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
    updateNClob() - Should throw an exception when the length is invalid.
    **/
    public void Var013()
    {
      notApplicable(); 
    }



    /**
    updateNClob() - Should be reflected by get, even if update has
    not yet been issued (i.e. update is still pending).
    **/
    public void Var014()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Santa Fe"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_VARCHAR_50", nClob);//, "Santa Fe"));
                assertCondition (rs_.getString ("C_VARCHAR_50").equals ("Santa Fe"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }




    /**
    updateNClob() - Should be reflected by get, after update has
    been issued, but cursor has not been repositioned.
    **/
    public void Var015()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Phoenix"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_VARCHAR_50", nClob);//, "Phoenix"));
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
    updateNClob() - Should be reflected by get, after update has
    been issued and cursor has been repositioned.
    **/
    public void Var016()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Austin"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_VARCHAR_50", nClob);//"Austin"));
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
    updateNClob() - Should work when the current row is the insert
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
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Tulsa"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_VARCHAR_50", nClob);// "Tulsa"));
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
    updateNClob() - Should be reflected by get on insert row, even if
    insert has not yet been issued (i.e. insert is still pending).
    **/
    public void Var018()
    {
        if(checkJdbc40 ())
        {
            try
            {
                rs_.moveToInsertRow ();
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Lawrence"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_VARCHAR_50", nClob);// "Lawrence"));
                assertCondition (rs_.getString ("C_VARCHAR_50").equals ("Lawrence"));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNClob() - Should throw an exception on a deleted row.
    **/
    public void Var019()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, "DUMMY_ROW");
                rs_.deleteRow ();
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Ames"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_VARCHAR_50", nClob);//"Ames"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


    /**
    updateNClob() - Update a SMALLINT.
    **/
    public void Var020 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "31"); 
                try
                {
                    JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_SMALLINT", nClob);// "31"));
                }catch(SQLException e)
                {
                    if(isToolboxDriver())
                    {
                        assertCondition(e.getMessage().equals("Data type mismatch."));
                        return;
                    }                    
                    else
                        throw e;
                }
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
    updateNClob() - Update a SMALLINT, when the integer is too big.
    **/
    public void Var021 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "224323"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_SMALLINT", nClob);// "224323"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNClob() - Update a SMALLINT, when the string is not a number.
    **/
    public void Var022 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Van Buren"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_SMALLINT", nClob);// "Van Buren"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNClob() - Update an INTEGER.
    **/
    public void Var023 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "-12374"); 
                try
                {
                    JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_INTEGER", nClob);// "-12374"));
                }catch(SQLException e)
                {
                    if(isToolboxDriver())
                    {
                        assertCondition(e.getMessage().equals("Data type mismatch."));
                        return;
                    }                    
                    else
                        throw e;
                }
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
    updateNClob() - Update an INTEGER, when the integer is too big.
    **/
    public void Var024 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "435353487623"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_INTEGER", nClob);// "435353487623"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNClob() - Update an INTEGER, when the string is not a number.
    **/
    public void Var025 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Baton Rouge"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_INTEGER", nClob);// "Baton Rouge"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNClob() - Update a REAL.
    **/
    public void Var026 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "13.35"); 
                try
                {
                    JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_REAL", nClob);// "13.35"));
                }catch(SQLException e)
                {
                    if(isToolboxDriver())
                    {
                        assertCondition(e.getMessage().equals("Data type mismatch."));
                        return;
                    }                    
                    else
                        throw e;
                }
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
    updateNClob() - Update a REAL, when the number is too big.
    
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
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "453433335.4437623"); 
                try
                {
                    JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_REAL", nClob);// "453433335.4437623"));
                }catch(SQLException e)
                {
                    if(isToolboxDriver())
                    {
                        assertCondition(e.getMessage().equals("Data type mismatch."));
                        return;
                    }                    
                    else
                        throw e;
                }
                rs_.updateRow ();

                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                float v = rs2.getFloat ("C_REAL");
                rs2.close ();
                // Does anyone have a good suggestion for what should be done here?
                assertCondition (v == 4.53433344E8, "v = "+v+"\n"+"" +
                                                         " sb 4.53433344E8");
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
    updateNClob() - Update a REAL, when the string is not a number.
    **/    public void Var028 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Edina"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_INTEGER", nClob); //"Edina"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }




    /**
    updateNClob() - Update a FLOAT.
    **/
    public void Var029 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "9835.2"); 
                try
                {
                    JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_FLOAT", nClob);// "9835.2"));
                }catch(SQLException e)
                {
                    if(isToolboxDriver())
                    {
                        assertCondition(e.getMessage().equals("Data type mismatch."));
                        return;
                    }                    
                    else
                        throw e;
                }
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
    updateNClob() - Update a FLOAT, when the number is too big.
    
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
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "453435.4434556337633"); 
                try
                {
                    JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_FLOAT", nClob);// "453435.4434556337633"));
                }catch(SQLException e)
                {
                    if(isToolboxDriver())
                    {
                        assertCondition(e.getMessage().equals("Data type mismatch."));
                        return;
                    }                    
                    else
                        throw e;
                }
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
    updateNClob() - Update a FLOAT, when the string is not a number.
    **/
    public void Var031 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Madison"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_FLOAT", nClob);//, "Madison"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


    /**
    updateNClob() - Update a DOUBLE.
    **/
    public void Var032 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "-9845"); 
                try
                {
                    JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_DOUBLE", nClob);// "-9845"));
                }catch(SQLException e)
                {
                    if(isToolboxDriver())
                    {
                        assertCondition(e.getMessage().equals("Data type mismatch."));
                        return;
                    }                    
                    else
                        throw e;
                }
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
    updateNClob() - Update a DOUBLE, when the number is too big.
    
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
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "453342035.44445532333"); 
                try
                {
                    JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_DOUBLE", nClob);// "453342035.44445532333"));
                }catch(SQLException e)
                {
                    if(isToolboxDriver())
                    {
                        assertCondition(e.getMessage().equals("Data type mismatch."));
                        return;
                    }                    
                    else
                        throw e;
                }
                rs_.updateRow ();

                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                float v = rs2.getFloat ("C_DOUBLE");
                rs2.close ();
                // Does anyone have a good suggestion for what should be done here?
                assertCondition (true, "v="+v);
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
    updateNClob() - Update a DOUBLE, when the string is not a number.
    **/
    public void Var034 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Chicago"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_DOUBLE", nClob);// "Chicago"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


    /**
    updateNClob() - Update a DECIMAL.
    **/
    public void Var035 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "4533.43"); 
                try
                {
                    JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_DECIMAL_105", nClob);//"4533.43"));
                }catch(SQLException e)
                {
                    if(isToolboxDriver())
                    {
                        assertCondition(e.getMessage().equals("Data type mismatch."));
                        return;
                    }                    
                    else
                        throw e;
                }
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
    updateNClob() - Update a DECIMAL, when the value is too big.
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
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "5333644511"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_DECIMAL_40", nClob);// "5333644511"));
                rs_.updateRow ();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                if(isToolboxDriver())
                {
                    assertCondition(e.getMessage().equals("Data type mismatch."));
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
    updateNClob() - Update a DECIMAL, when the string is not a number.
    **/
    public void Var037 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Terre Haute"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_DECIMAL_105", nClob);// "Terre Haute"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNClob() - Update a NUMERIC.
    **/
    public void Var038 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "-9333.455"); 
                try
                {
                    JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_NUMERIC_105", nClob);// "-9333.455"));
                }catch(SQLException e)
                {
                    if(isToolboxDriver())
                    {
                        assertCondition(e.getMessage().equals("Data type mismatch."));
                        return;
                    }                    
                    else
                        throw e;
                }
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
    updateNClob() - Update a NUMERIC, when the value is too big.
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
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "-3315145"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_NUMERIC_40", nClob);// "-3315145"));
                rs_.updateRow ();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                if(isToolboxDriver())
                {
                    assertCondition(e.getMessage().equals("Data type mismatch."));
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
    updateNClob() - Update a NUMERIC, when the string is not a number.
    **/
    public void Var040 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Detroit"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_NUMERIC_105", nClob);// "Detroit"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNClob() - Update a CHAR.
    **/
    public void Var041 ()
    {

        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Upper Darby"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_CHAR_50", nClob);// "Upper Darby"));
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
    updateNClob() - Update a CHAR, when the value is too big.
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
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Manhattan"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_CHAR_1", nClob);// "Manhattan"));
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
    updateNClob() - Update a VARCHAR, with the length equal to the
    full stream.
    **/
    public void Var043 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Rehoboth Beach"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_VARCHAR_50", nClob);//"Rehoboth Beach"));
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
    updateNClob() - Update a VARCHAR, with the length less than the
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
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Toronto"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_VARCHAR_50", nClob);// "Toronto"));
		if( (getDriver() == JDTestDriver.DRIVER_NATIVE &&	// @D2
		    true) || isToolboxDriver() )	// @D2
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
    updateNClob() - Update a VARCHAR, with the length greater than the
    full stream.
    **/
    public void Var045 ()
    {
      notApplicable(); 
    }



    /**
    updateNClob() - Update a VARCHAR, with the length set to 1.

     @D2 - Native driver doesn't throw an exception if stream contains extra characters then there needs to be there.
    **/
    public void Var046 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Winnipeg"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_VARCHAR_50", nClob);// "Winnipeg"));
		if( (getDriver() == JDTestDriver.DRIVER_NATIVE &&	// @D2
		    true) || isToolboxDriver() )	// @D2
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
    updateNClob() - Update a VARCHAR, with the length set to 0.
    **/
    public void Var047 ()
    {
        notApplicable(); 
    }



    /**
    updateNClob() - Update a VARCHAR, with an empty string.
    **/
    public void Var048 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, ""); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_VARCHAR_50", nClob);// ""));
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
    updateNClob() - Update a VARCHAR, when the value is too big.
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
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "The greater Washington, D.C. Metropolitan Area and surrounding communities"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_VARCHAR_50", nClob);//, "The greater Washington, D.C. Metropolitan Area and surrounding communities"));
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
    updateNClob() - Update a VARCHAR parameter to a bad reader.
    **/
    public void Var050()
    {
        notApplicable(); 
    }





    /**
    updateNClob() - Update a BINARY.
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
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, expected); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_BINARY_20", nClob);// expected));
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
    updateNClob() - Update a BINARY, when the value is too big.
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
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "F1F2F3"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_BINARY_1", nClob);//"F1F2F3"));
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
    updateNClob() - Update a VARBINARY.
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
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, expected); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_VARBINARY_20", nClob);//, expected));
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
    updateNClob() - Update a VARBINARY, when the value is too big.
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
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, expected); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_VARBINARY_20", nClob);// expected));
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
    updateNClob() - Update a CLOB.
    
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
                    Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                    JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Knoxville"); 
                    JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_CLOB", nClob);// "Knoxville"));

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
    updateNClob() - Update a DBCLOB.
    
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
                    Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                    JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Memphis"); 
                    JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_DBCLOB", nClob);//"Memphis"));

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
    updateNClob() - Update a BLOB.
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
                    Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                    JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Honolulu"); 
                    JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_BLOB", nClob);// "Honolulu"));
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
    updateNClob() - Update a DATE.
    **/
    public void Var058 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "1970-02-10"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_DATE", nClob);// "1970-02-10"));
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                Date v = rs2.getDate ("C_DATE");
                rs2.close ();
                assertCondition (v.toString ().equals ("1970-02-10"), "got "+v.toString()+" sb 1970-02-10");
            }
            catch(Exception e)
            {
                if(isToolboxDriver())
                {
                    assertCondition(e.getMessage().indexOf("Data type mismatch.") >= 0, "got error "+e.getMessage()+" should have Data type mismatch");
                    return;
                }      
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNClob() - Update a DATE, when the string is not a valid date.
    **/
    public void Var059 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Augusta"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_DATE", nClob);// "Augusta"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNClob() - Update a TIME.
    **/
    public void Var060 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "13:45:32"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_TIME", nClob);//"13:45:32"));
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                Time v = rs2.getTime ("C_TIME");
                rs2.close ();
                assertCondition (v.toString ().equals ("13:45:32"));
            }
            catch(Exception e)
            {
                if(isToolboxDriver())
                {
                    assertCondition(e.getMessage().equals("Data type mismatch."));
                    return;
                }      
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNClob() - Update a TIME, when the string is not a valid time.
    **/
    public void Var061 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Birmingham"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_TIME", nClob);// "Birmingham"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNClob() - Update a TIMESTAMP.
    **/
    public void Var062 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "1969-11-18 15:22:23.4329"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_TIMESTAMP", nClob);// "1969-11-18 15:22:23.4329"));
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                Timestamp v = rs2.getTimestamp ("C_TIMESTAMP");
                rs2.close ();
                assertCondition (v.toString ().equals ("1969-11-18 15:22:23.4329"));
            }
            catch(Exception e)
            {
                if(isToolboxDriver())
                {
                    assertCondition(e.getMessage().equals("Data type mismatch."));
                    return;
                }    
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNClob() - Update a TIMESTAMP, when the string is not a valid timestamp.
    **/
    public void Var063 ()
    {
        if(checkJdbc40 ())
        {
            try
            {
                JDRSTest.position (rs_, key_);
                Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Boston"); 
                JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_TIMESTAMP", nClob);// "Boston"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    updateNClob() - Update a DATALINK.
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
                    JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_DATALINK", JDRelectionUtil.callMethod_OS(connection_, "createNClob", "Providence"));
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
    updateNClob() - Update a DISTINCT.
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
                    Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                    JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Concord"); 
                    JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_DISTINCT", nClob);// "Concord"));
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
    updateNClob() - Update a BIGINT.
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
                    Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                    JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "-1237324"); 
                    JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_BIGINT", nClob);// "-1237324"));
                    rs_.updateRow ();
                    ResultSet rs2 = statement2_.executeQuery (select_);
                    JDRSTest.position (rs2, key_);
                    long v = rs2.getLong ("C_BIGINT");
                    rs2.close ();
                    assertCondition (v == -1237324);
                }
                catch(Exception e)
                {
                    if(isToolboxDriver())
                    {
                        assertCondition(e.getMessage().equals("Data type mismatch."));
                        return;
                    }    
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



    /**
    updateNClob() - Update a BIGINT, when the integer is too big.
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
                    Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                    JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "4353534432176864587623"); 
                    JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_BIGINT", nClob);// "4353534432176864587623"));
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
    updateNClob() - Update a BIGINTEGER, when the string is not a number.
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
                    Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                    JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Baton Rouge"); 
                    JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_BIGINT", nClob);// "Baton Rouge"));
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
    updateNClob() - Update a BLOB.
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
                    Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
                    JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "1023AAFFB10"); 
                    JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_BLOB", nClob);// "1023AAFFB10"));
                    
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
    updateNClob() - Update a DFP16.
    **/
    public void Var070 ()
    {
      if (checkJdbc40())
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
              Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
              JDReflectionUtil.callMethod_V(nClob, "setString", 1L, value);
              JDReflectionUtil.callMethod_V(rs, "updateNClob", 1, nClob);
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
                //toolbox does not support updateNClob on this type
                if(isToolboxDriver())
                {
                    assertCondition(e.getMessage().indexOf("Data type mismatch") != -1, "Expected Data type mismatch received " + e.getMessage());
                    return;
                }
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNClob() - Update a DFP16, when the value is too big.
    **/
    public void Var071 ()
    {
      if (checkJdbc40())
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
            Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
            JDReflectionUtil.callMethod_V(nClob, "setString", 1L, value);
            JDReflectionUtil.callMethod_V(rs, "updateNClob", 1, nClob);
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
    updateNClob() - Update a DFP16, when the string is not a number.
    **/
    public void Var072 ()
    {
      if (checkJdbc40())
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
            Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
            JDReflectionUtil.callMethod_V(nClob, "setString", 1L, value);
            JDReflectionUtil.callMethod_V(rs, "updateNClob", 1, nClob);
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
    updateNClob() - Update a DFP34.
    **/
    public void Var073 ()
    {
      if (checkJdbc40())
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
              Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
              JDReflectionUtil.callMethod_V(nClob, "setString", 1L, value);
              JDReflectionUtil.callMethod_V(rs, "updateNClob", 1, nClob);
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
                //toolbox does not support updateNClob on this type
                if(isToolboxDriver())
                {
                    assertCondition(e.getMessage().indexOf("Data type mismatch") != -1);
                    return;
                }
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    updateNClob() - Update a DFP34, when the value is too big.
    **/
    public void Var074 ()
    {
      if (checkJdbc40())
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
            Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
            JDReflectionUtil.callMethod_V(nClob, "setString", 1L, value);
            JDReflectionUtil.callMethod_V(rs, "updateNClob", 1, nClob);
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
    updateNClob() - Update a DFP34, when the string is not a number.
    **/
    public void Var075 ()
    {
      if (checkJdbc40())
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
            Object nClob = JDReflectionUtil.callMethod_O(connection_, "createNClob");
            JDReflectionUtil.callMethod_V(nClob, "setString", 1L, value);
            JDReflectionUtil.callMethod_V(rs, "updateNClob", 1, nClob);
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
   * updateNClob() - Update a BOOLEAN
   **/
  public void Var076() {
    if (checkJdbc40()) {
      if (checkBooleanSupport()) {
        try {
          JDRSTest.position(rs_, key_);
          Object nClob = JDReflectionUtil.callMethod_O(connection_,
              "createNClob");
          JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "1");
          JDReflectionUtil.callMethod_V(rs_, "updateNClob", "C_BOOLEAN", nClob);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }



}



