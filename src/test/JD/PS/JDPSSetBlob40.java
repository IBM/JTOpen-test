///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSSetBlob40.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
//
//
//
//
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDPSSetBlob40.java
//
// Classes:      JDPSSetBlob40
//
////////////////////////////////////////////////////////////////////////
//
//
//
////////////////////////////////////////////////////////////////////////

package test.JD.PS;

import com.ibm.as400.access.AS400;

import test.JDPSTest;
import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDTestcase;
import test.JD.JDSetupPackage;
import test.JD.JDTestUtilities;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDPSSetBlob40.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>SetBlob40()
</ul>
**/
public class JDPSSetBlob40
extends JDTestcase {



    // Constants.
    private static final String PACKAGE             = "JDPSSBS";



    // Private data.
    private Connection          connection_;
    private Connection          connectionNoDT_;
    private Statement           statement_;



/**
Constructor.
**/
    public JDPSSetBlob40 (AS400 systemObject,
                                Hashtable namesAndVars,
                                int runMode,
                                FileOutputStream fileOutputStream,
                                
                                String password)
    {
        super (systemObject, "JDPSSetBlob40",
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

        String url = baseURL_
                     
                      
                     + ";data truncation=true";
        connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
        statement_ = connection_.createStatement ();

        url = url + ";data truncation=false";
        connectionNoDT_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
        statement_.close ();
        connection_.close ();
        connectionNoDT_.close();
    }



/**
setBlob() - Should throw exception when the prepared
statement is closed.
**/
    public void Var001()
    {
	if (checkJdbc40()) {
try {
	    PreparedStatement ps = connection_.prepareStatement (
								 "INSERT INTO " + JDPSTest.PSTEST_SET
								 + " (C_VARBINARY_20) VALUES (?)");
	    ps.close ();
	    InputStream is = new ByteArrayInputStream (new byte[] { (byte) 22, (byte) 4, (byte) 98, (byte) -2});
	    JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 4);
	    failed ("Didn't throw SQLException");
	}
	catch (Exception e) {
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
	}
    }



/**
setBlob() - Should throw exception when an invalid index is
specified.
**/
    public void Var002()
    {
	if (checkJdbc40()) { try {
	    PreparedStatement ps = connection_.prepareStatement (
								 "INSERT INTO " + JDPSTest.PSTEST_SET
								 + " (C_INTEGER, C_SMALLINT) VALUES (?, ?)");
	    InputStream is = new ByteArrayInputStream (new byte[] { (byte) 22, (byte) 4, (byte) 98, (byte) -2});
	    JDReflectionUtil.callMethod_V(ps, "setBlob", 100, is, (long) 4);
	    ps.executeUpdate(); ps.close ();
	    failed ("Didn't throw SQLException");
	}
	catch (Exception e) {
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
	}

    }

/**
setBlob() - Should throw exception when index is 0.
**/
    public void Var003()
    {
	if (checkJdbc40()) { try {
	    PreparedStatement ps = connection_.prepareStatement (
								 "INSERT INTO " + JDPSTest.PSTEST_SET
								 + " (C_INTEGER, C_SMALLINT) VALUES (?, ?)");
	    InputStream is = new ByteArrayInputStream (new byte[] { (byte) 22, (byte) 4, (byte) 98, (byte) -2});
	    JDReflectionUtil.callMethod_V(ps, "setBlob", 0, is, (long) 4);
	    ps.executeUpdate(); ps.close ();
	    failed ("Didn't throw SQLException");
	}
	catch (Exception e) {
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
	}

    }

/**
setBlob() - Should throw exception when index is -1.
**/
    public void Var004()
    {
	if (checkJdbc40()) { try {
	    PreparedStatement ps = connection_.prepareStatement (
								 "INSERT INTO " + JDPSTest.PSTEST_SET
								 + " (C_INTEGER, C_SMALLINT) VALUES (?, ?)");
	    InputStream is = new ByteArrayInputStream (new byte[] { (byte) 22, (byte) 4, (byte) 98, (byte) -2});
	    JDReflectionUtil.callMethod_V(ps, "setBlob", -1, is, (long) 4);
	    ps.executeUpdate(); ps.close ();
	    failed ("Didn't throw SQLException");
	}
	catch (Exception e) {
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
	}
    }


/**
setBlob() - Should set to SQL NULL when the value is null.
**/
    public void Var005()
    {
        if (checkJdbc40()) { try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_VARBINARY_20) VALUES (?)");
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, (InputStream) null, 0L);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_VARBINARY_20 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            byte[] check = rs.getBytes (1);
            boolean wn = rs.wasNull ();
            rs.close ();

            assertCondition ((check == null) && (wn == true));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
setBlob() - Should work with a valid parameter index
greater than 1.
**/
    public void Var006()
    {
        if (checkJdbc40()) { try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_KEY, C_VARBINARY_20) VALUES (?, ?)");
            ps.setString (1, "Muchas");
            byte[] b = new byte[] { (byte) -22, (byte) 4, (byte) 98, (byte) -2};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 2, is, (long) 4);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_VARBINARY_20 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            byte[] check = rs.getBytes (1);

            rs.close ();

            assertCondition (areEqual (b, check));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    }

/**
setBlob() - Should throw exception when the length is not valid.
**/
    public void Var007()
    {
        if (checkJdbc40()) { try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_VARBINARY_20) VALUES (?)");
            byte[] b = new byte[] { (byte) -22, (byte) 98, (byte) -2};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) -1);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }

    }

/**
setBlob() - Should throw exception when the parameter is
not an input parameter.
**/
    public void Var008()
    {
        if (checkJdbc40()) { try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "CALL " + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
            byte[] b = new byte[] { (byte) 98, (byte) -2};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 2, is, (long) 2);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }

    }

/**
setBlob() - Verify that a data truncation warning is
posted when data is truncated.
**/
    public void Var009()
    {
        int length = 0;
        if (checkJdbc40()) { try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_VARBINARY_20) VALUES (?)");
            byte[] b = new byte[] { (byte) -22, (byte) 4, (byte) 98, (byte) -2,
                (byte) 0, (byte) -111, (byte) 50, (byte) 2,
                (byte) 0, (byte) -111, (byte) 50, (byte) 2,
                (byte) 0, (byte) -111, (byte) 50, (byte) 2,
                (byte) 0, (byte) -111, (byte) 50, (byte) 2,
                (byte) 0, (byte) -111, (byte) 50, (byte) 2,
                (byte) 0, (byte) -111, (byte) 50, (byte) 2,
                (byte) 0, (byte) -111, (byte) 50, (byte) 2,
                (byte) 0, (byte) -111, (byte) 50, (byte) 2,
                (byte) 0, (byte) -111, (byte) 50, (byte) 2,
                (byte) 0, (byte) -111, (byte) 50, (byte) 2,
                (byte) 0, (byte) -111, (byte) 50, (byte) 2,
                (byte) 0, (byte) -111, (byte) 50, (byte) 2,
                (byte) 0, (byte) -111, (byte) 50, (byte) 2,
                (byte) 0, (byte) -111, (byte) 50, (byte) 2,
                (byte) 22};
            InputStream is = new ByteArrayInputStream (b);
            length = b.length;
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) length);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (DataTruncation dt) {
            assertCondition ((dt.getIndex() == 1)
                    && (dt.getParameter() == true)
                    && (dt.getRead() == false)
                    && (dt.getDataSize() == length)
                    && (dt.getTransferSize() == 20));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    }


    public void testSetFailed(String columnName, byte[] inArray) {
        if (checkJdbc40()) { try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " ("+columnName+") VALUES (?)");
            InputStream is = new ByteArrayInputStream (inArray);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 2);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }

    }
/**
setBlob() - Set a SMALLINT parameter.
**/
    public void Var010()
    {
	byte[] b = new byte[] { (byte) 98, (byte) 123};

	testSetFailed("C_SMALLINT",b); 
    }


/**
setBlob() - Set a INTEGER parameter.
**/
    public void Var011()
    {
        if (checkJdbc40()) { try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_INTEGER) VALUES (?)");
            byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 3);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }
    }


/**
setBlob() - Set a REAL parameter.
**/
    public void Var012()
    {
	if (checkJdbc40()) { try {
	    PreparedStatement ps = connection_.prepareStatement (
								 "INSERT INTO " + JDPSTest.PSTEST_SET
								 + " (C_REAL) VALUES (?)");
	    byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2};
	    InputStream is = new ByteArrayInputStream (b);
	    JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 3);
	    ps.executeUpdate(); ps.close ();
	    failed ("Didn't throw SQLException");
	}
	catch (Exception e) {
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
	}
    }


/**
setBlob() - Set a FLOAT parameter.
**/
    public void Var013()
    {
        if (checkJdbc40()) { try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_FLOAT) VALUES (?)");
            byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2, (byte) 45};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 4);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }
    }


/**
setBlob() - Set a DOUBLE parameter.
**/
    public void Var014()
    {
        if (checkJdbc40()) { try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_DOUBLE) VALUES (?)");
            byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2, (byte) 45,
                (byte) 12};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 5);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }
    }


/**
setBlob() - Set a DECIMAL parameter.
**/
    public void Var015()
    {
        if (checkJdbc40()) { try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_DECIMAL_105) VALUES (?)");
            byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2, (byte) 45,
                (byte) 12, (byte) -33};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 6);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }
    }


/**
setBlob() - Set a NUMERIC parameter.
**/
    public void Var016()
    {
        if (checkJdbc40()) { try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_NUMERIC_50) VALUES (?)");
            byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2, (byte) 45,
                (byte) 12, (byte) -33, (byte) 0};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 7);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }
    }


/**
setBlob() - Set a CHAR(1) parameter.
**/
    public void Var017()
    {
        if (checkJdbc40()) { try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_CHAR_1) VALUES (?)");
            byte[] b = new byte[] { (byte) 98};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 1);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }
    }



/**
setBlob() - Set a CHAR(50) parameter.
**/
    public void Var018()
    {
        if (checkJdbc40()) { try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_CHAR_50) VALUES (?)");
            byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -12, (byte) 45,
                (byte) 12, (byte) -33};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 6);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }
    }


/**
setBlob() - Set a VARCHAR(50) parameter.
**/
    public void Var019()
    {
        if (checkJdbc40()) { try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_VARCHAR_50) VALUES (?)");
            byte[] b = new byte[] { (byte) 7, (byte) 108, (byte) -12, 
                (byte) 12, (byte) -33};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 5);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }

    }

/**
setBlob() - Set a CLOB parameter.
**/
    public void Var020()
    {
        if (checkLobSupport ()) {
            if (checkJdbc40()) { try {
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_CLOB) VALUES (?)");
                byte[] b = new byte[] { (byte) 0, (byte) -12, 
                    (byte) 12, (byte) -33};
                InputStream is = new ByteArrayInputStream (b);
                JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 4);
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

    }


/**
setBlob() - Set a DBCLOB parameter.
**/
    public void Var021()
    {
        if (checkLobSupport ()) {
            if (checkJdbc40()) { try {
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_DBCLOB) VALUES (?)");
                byte[] b = new byte[] { (byte) 0, (byte) -12, (byte) 66,
                    (byte) 12, (byte) -33};
                InputStream is = new ByteArrayInputStream (b);
                JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 5);
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

    }

/**
setBlob() - Set a BINARY parameter.
**/
    public void Var022()
    {
	if (checkJdbc40()) { try {
	    statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

	    PreparedStatement ps = connection_.prepareStatement (
								 "INSERT INTO " + JDPSTest.PSTEST_SET
								 + " (C_BINARY_20) VALUES (?)");
	    byte[] b = new byte[] { (byte) 0, (byte) -12, (byte) 1, (byte) 0,
	    (byte) 12, (byte) -33, (byte) 57, (byte) 9};
	    InputStream is = new ByteArrayInputStream (b);
	    JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 8);
	    ps.executeUpdate ();
	    ps.close ();

	    ResultSet rs = statement_.executeQuery ("SELECT C_BINARY_20 FROM " + JDPSTest.PSTEST_SET);
	    rs.next ();
	    byte[] check = rs.getBytes (1);
	    rs.close ();

	    byte[] check2 = new byte[20];
	    System.arraycopy (b, 0, check2, 0, b.length);

	    assertCondition (areEqual (check, check2));
	}
	catch (Exception e) {
	    failed (e, "Unexpected Exception");
	}
	}

    }


/**
 * setBlob() - Set a VARBINARY parameter.
 */
  public void Var023() {
    if (checkJdbc40()) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (C_VARBINARY_20) VALUES (?)");
        byte[] b = new byte[] { (byte) 0, (byte) -12, (byte) 1, (byte) 0,
            (byte) -33, (byte) 57, (byte) 9 };
        InputStream is = new ByteArrayInputStream(b);
        JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 7);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_VARBINARY_20 FROM "
            + JDPSTest.PSTEST_SET);
        rs.next();
        byte[] check = rs.getBytes(1);
        rs.close();

        assertCondition(areEqual(b, check));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

  }


/**
 * setBlob() - Set a VARBINARY parameter, with the length less than the full
 * stream.
 */
    public void Var024()
    {
        if (checkJdbc40()) { try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_VARBINARY_20) VALUES (?)");
            byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -12, (byte) 45,
                (byte) -33};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 3);
	    /*                                                             @E1 
            if(isToolboxDriver())
            {
	    */
	    succeeded();
	    /*                                                             @E1
            }
            else
            {
                failed ("Didn't throw SQLException");
            }
	    */
        }
        catch (Exception e) {
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

/**
setBlob() - Set a VARBINARY parameter, with the length greater than
the full stream.
**/
    public void Var025()
    {
        if (checkJdbc40()) { try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_VARBINARY_20) VALUES (?)");
            byte[] b = new byte[] { (byte) 7, (byte) 28, (byte) -12, (byte) 45,
                (byte) -33};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 10);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }

    }

/**
setBlob() - Set a VARBINARY parameter, with the length set to 1 character.
**/
    public void Var026()
    {
        if (checkJdbc40()) { try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_VARBINARY_20) VALUES (?)");
            byte[] b = new byte[] { (byte) -18, (byte) -12, (byte) 25,
                (byte) -33};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 1);
	    
	    /*                                                      @E1
            if(isToolboxDriver())
            {
	    */
	    succeeded();
	    /*                                                      @E1
            }
            else
            {
                failed ("Didn't throw SQLException");
            }
	    */
        }
        catch (Exception e) {
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

/**
setBlob() - Set a VARBINARY parameter, with the length set to 0.
**/
    public void Var027()
    {
        if (checkJdbc40()) { try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_VARBINARY_20) VALUES (?)");
            byte[] b = new byte[] { (byte) -12, (byte) 45,
                (byte) -33};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 0);
            /*                                                    @E1
            if(isToolboxDriver())
            {
	    */
	    succeeded();
	    /*                                                    @E1
            }
            else
            {
                failed ("Didn't throw SQLException");
            }
	    */
        }
        catch (Exception e) {
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


/**
setBlob() - Set a VARBINARY parameter to the empty string.
**/
    public void Var028()
    {
        if (checkJdbc40()) { try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_VARBINARY_20) VALUES (?)");
            byte[] b = new byte[0];
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 0);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_VARBINARY_20 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            byte[] check = rs.getBytes (1);
            rs.close ();

            assertCondition (check.length == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    }
/**
setBlob() - Set a VARBINARY parameter to a bad input stream.
**/
    public void Var029()
    {
        if (checkJdbc40()) { try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_VARBINARY_20) VALUES (?)");

            class BadInputStream extends InputStream {
                public BadInputStream () {
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
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, r, 2L);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    }
/**
setBlob() - Set a BLOB parameter.
**/
    public void Var030()
    {
        if (checkLobSupport ()) {
            if (checkJdbc40()) { try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_BLOB) VALUES (?)");
                byte[] b = new byte[] { (byte) -12, (byte) 45,
                    (byte) -33, (byte) 0};
                InputStream is = new ByteArrayInputStream (b);
                JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 4);
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_BLOB FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                InputStream is2 = rs.getBinaryStream(1);
                byte[] check = new byte[b.length];
                is2.read(check);
                is2.close();
                rs.close ();

                assertCondition (areEqual (b, check));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

    }


/**
setBlob() - Set a DATE parameter.
**/
    public void Var031()
    {
        if (checkJdbc40()) { try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_DATE) VALUES (?)");
            byte[] b = new byte[] { (byte) -12, (byte) 45, (byte) 1,
                (byte) -33, (byte) 0};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 5);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }
    }


/**
setBlob() - Set a TIME parameter.
**/
    public void Var032()
    {
        if (checkJdbc40()) { try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_TIME) VALUES (?)");
            byte[] b = new byte[] { (byte) -12, (byte) 45, (byte) 1,
                (byte) -33, (byte) 0, (byte) 5};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 6);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }
    }


/**
setBlob() - Set a TIMESTAMP parameter.
**/
    public void Var033()
    {
        if (checkJdbc40()) { try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_TIMESTAMP) VALUES (?)");
            byte[] b = new byte[] { (byte) -12, (byte) 45, (byte) 1, (byte) 11,
                (byte) -33, (byte) 0, (byte) 5};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 7);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }
    }

/**
setBlob() - Set a DATALINK parameter.
**/
    public void Var034()
    {
        if (checkDatalinkSupport ()) {
            if (checkJdbc40()) { try {
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_DATALINK) VALUES (?)");
                byte[] b = new byte[] { (byte) -12, (byte) 45, (byte) 1, (byte) 11,
                    (byte) -33, (byte) 0, (byte) 5, (byte) 100};
                InputStream is = new ByteArrayInputStream (b);
                JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 8);
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

    }

/**
setBlob() - Set a DISTINCT parameter.
**/
    public void Var035()
    {
        if (checkLobSupport ()) {
            if (checkJdbc40()) { try {
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_DISTINCT) VALUES (?)");
                byte[] b = new byte[] { (byte) -12, (byte) 1, (byte) 11,
                    (byte) -33, (byte) 0, (byte) 5, (byte) 100};
                InputStream is = new ByteArrayInputStream (b);
                JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 7);
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }
    }


/**
setBlob() - Set a BIGINT parameter.
**/
    public void Var036()
    {
        if (checkBigintSupport()) {
            if (checkJdbc40()) { try {
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_BIGINT) VALUES (?)");
                byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2};
                InputStream is = new ByteArrayInputStream (b);
                JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 3);
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }
    }



/**
setBlob() - Set a VARBINARY parameter with package caching.
**/
    public void Var037()
    {
        if (checkJdbc40()) { try {
            String insert = "INSERT INTO " + JDPSTest.PSTEST_SET
                            + " (C_VARBINARY_20) VALUES (?)";

            if (isToolboxDriver())
                JDSetupPackage.prime (systemObject_, PACKAGE, 
                                      JDPSTest.COLLECTION, insert);
            else
                JDSetupPackage.prime (systemObject_, encryptedPassword_, PACKAGE, 
                                      JDPSTest.COLLECTION, insert, "", getDriver());

            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            Connection c2 = testDriver_.getConnection (baseURL_
                                                       + ";extended dynamic=true;package=" + PACKAGE
                                                       + ";package library=" + JDPSTest.COLLECTION
                                                       + ";package cache=true", userId_, encryptedPassword_);
            PreparedStatement ps = c2.prepareStatement (insert);
            byte[] b = new byte[] { (byte) -12, (byte) 45, (byte) 11,
                (byte) -33, (byte) 0, (byte) 5, (byte) 100};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 7);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_VARBINARY_20 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            byte[] check = rs.getBytes (1);
            rs.close ();

            assertCondition (areEqual (check, b));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }
/**   D1A
SQL400 - testcase added.
setBlob() - Verify that no data truncation warning is
posted when data is truncated but the data truncation flag is turned off.
**/
    public void Var038()
    {
        int length = 0;
        if (checkNative()) {
            if (checkJdbc40()) { try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connectionNoDT_.prepareStatement (
                                                                        "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                        + " (C_VARBINARY_20) VALUES (?)");
                byte[] b = new byte[] { (byte) -22, (byte) 4, (byte) 98, (byte) -2,
                    (byte) 0, (byte) -111, (byte) 50, (byte) 2,
                    (byte) 0, (byte) -111, (byte) 50, (byte) 2,
                    (byte) 0, (byte) -111, (byte) 50, (byte) 2,
                    (byte) 0, (byte) -111, (byte) 50, (byte) 2,
                    (byte) 0, (byte) -111, (byte) 50, (byte) 2,
                    (byte) 0, (byte) -111, (byte) 50, (byte) 2,
                    (byte) 0, (byte) -111, (byte) 50, (byte) 2,
                    (byte) 0, (byte) -111, (byte) 50, (byte) 2,
                    (byte) 0, (byte) -111, (byte) 50, (byte) 2,
                    (byte) 0, (byte) -111, (byte) 50, (byte) 2,
                    (byte) 0, (byte) -111, (byte) 50, (byte) 2,
                    (byte) 0, (byte) -111, (byte) 50, (byte) 2,
                    (byte) 0, (byte) -111, (byte) 50, (byte) 2,
                    (byte) 0, (byte) -111, (byte) 50, (byte) 2,
                    (byte) 22};
                InputStream is = new ByteArrayInputStream (b);
                length = b.length;
                JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) length);
                ps.executeUpdate(); ps.close ();
                assertCondition (true);
            }
            catch (DataTruncation dt) {
                failed (dt, "Unexpected Data Truncation Exception");
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }
    }

/**
setBlob() - Set a SMALLINT parameter. streamLength>lengthSpecified case
**/
    public void Var039()    // @E1: added this Var
    {
        if (checkJdbc40()) { try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_SMALLINT) VALUES (?)");
            byte[] b = new byte[] { (byte) 98, (byte) 123};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 1);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }

    }

/**
setBlob() - Set a INTEGER parameter. streamLength>lengthSpecified case
**/
    public void Var040()    // @E1: added this Var
    {
        if (checkJdbc40()) { try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_INTEGER) VALUES (?)");
            byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 2);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }

    }

/**
setBlob() - Set a REAL parameter. streamLength>lengthSpecified case
**/
    public void Var041()    // @E1: added this Var
    {
        if (checkJdbc40()) { try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_REAL) VALUES (?)");
            byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 2);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }

    }

/**
setBlob() - Set a FLOAT parameter. streamLength>lengthSpecified case
**/
    public void Var042()    // @E1: added this Var
    {
        if (checkJdbc40()) { try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_FLOAT) VALUES (?)");
            byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2, (byte) 45};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 3);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }

    }

/**
setBlob() - Set a DOUBLE parameter. streamLength>lengthSpecified case
**/
    public void Var043()       // @E1: added this Var
    {
        if (checkJdbc40()) { try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_DOUBLE) VALUES (?)");
            byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2, (byte) 45,
                (byte) 12};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 4);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }
    }


/**
setBlob() - Set a DECIMAL parameter. streamLength>lengthSpecified case
**/
    public void Var044()       // @E1: added this Var
    {
        if (checkJdbc40()) { try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_DECIMAL_105) VALUES (?)");
            byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2, (byte) 45,
                (byte) 12, (byte) -33};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 2);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }

    }

/**
setBlob() - Set a NUMERIC parameter. streamLength>lengthSpecified case
**/
    public void Var045()      // @E1: added this Var
    {
        if (checkJdbc40()) { try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_NUMERIC_50) VALUES (?)");
            byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2, (byte) 45,
                (byte) 12, (byte) -33, (byte) 0};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 1);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }

    }

/**
setBlob() - Set a CHAR(1) parameter. streamLength>lengthSpecified case
**/
    public void Var046()         // @E1: added this Var
    {
        if (checkJdbc40()) { try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_CHAR_1) VALUES (?)");
            byte[] b = new byte[] { (byte) 98, (byte) 98};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 1);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }
    }



/**
setBlob() - Set a CHAR(50) parameter. streamLength>lengthSpecified case
**/
    public void Var047()    // @E1: added this Var
    {
        if (checkJdbc40()) { try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_CHAR_50) VALUES (?)");
            byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -12, (byte) 45,
                (byte) 12, (byte) -33};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 3);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }
    }


/**
setBlob() - Set a VARCHAR(50) parameter. streamLength>lengthSpecified case
**/
    public void Var048()     // @E1: added this Var
    {
        if (checkJdbc40()) { try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_VARCHAR_50) VALUES (?)");
            byte[] b = new byte[] { (byte) 7, (byte) 108, (byte) -12, 
                (byte) 12, (byte) -33};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 1);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }
    }


/**
setBlob() - Set a CLOB parameter. streamLength>lengthSpecified case
**/
    public void Var049()      // @E1: added this Var
    {
        if (checkLobSupport ()) {
            if (checkJdbc40()) { try {
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_CLOB) VALUES (?)");
                byte[] b = new byte[] { (byte) 0, (byte) -12, 
                    (byte) 12, (byte) -33};
                InputStream is = new ByteArrayInputStream (b);
                JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 2);
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }
    }



/**
setBlob() - Set a DBCLOB parameter. streamLength>lengthSpecified case
**/
    public void Var050()        // @E1: added this Var
    {
        if (checkLobSupport ()) {
            if (checkJdbc40()) { try {
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_DBCLOB) VALUES (?)");
                byte[] b = new byte[] { (byte) 0, (byte) -12, (byte) 66,
                    (byte) 12, (byte) -33};
                InputStream is = new ByteArrayInputStream (b);
                JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 1);
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }
    }


/**
setBlob() - Set a BINARY parameter. streamLength>lengthSpecified case
**/
    public void Var051()         // @E1: added this Var
    {
        if (checkJdbc40()) { try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_BINARY_20) VALUES (?)");
            byte[] b = new byte[] { (byte) 0, (byte) -12, (byte) 1, (byte) 0,
                (byte) 12, (byte) -33, (byte) 57, (byte) 9};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 3);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_BINARY_20 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            byte[] check = rs.getBytes (1);
            rs.close ();

            byte[] check2 = new byte[20];
            System.arraycopy (b, 0, check2, 0, 3); // we are supplying 3 & not b.length as we are writing only 3 bytes 

            assertCondition (areEqual (check, check2));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }

/**
setBlob() - Set a BLOB parameter.  streamLength>lengthSpecified case
**/
    public void Var052()       // @E1: added this Var
    {
        if (checkLobSupport ()) {
            if (checkJdbc40()) { try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_BLOB) VALUES (?)");
                byte[] b = new byte[] { (byte) -12, (byte) 45,
                    (byte) -33, (byte) 0};
                InputStream is = new ByteArrayInputStream (b);
                JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 3);
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_BLOB FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                InputStream is2 = rs.getBinaryStream(1);
                byte[] check = new byte[b.length];
                is2.read(check);
                is2.close();
                rs.close ();

                assertCondition (areEqual (b, check));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }
    }



/**
setBlob() - Set a DATE parameter.  streamLength>lengthSpecified case
**/
    public void Var053()         // @E1: added this Var
    {
        if (checkJdbc40()) { try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_DATE) VALUES (?)");
            byte[] b = new byte[] { (byte) -12, (byte) 45, (byte) 1,
                (byte) -33, (byte) 0};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 3);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }
    }


/**
setBlob() - Set a TIME parameter.  streamLength>lengthSpecified case
**/
    public void Var054()            // @E1: added this Var
    {
        if (checkJdbc40()) { try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_TIME) VALUES (?)");
            byte[] b = new byte[] { (byte) -12, (byte) 45, (byte) 1,
                (byte) -33, (byte) 0, (byte) 5};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 4);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }

    }

/**
setBlob() - Set a TIMESTAMP parameter.  streamLength>lengthSpecified case
**/
    public void Var055()         // @E1: added this Var
    {
        if (checkJdbc40()) { try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_TIMESTAMP) VALUES (?)");
            byte[] b = new byte[] { (byte) -12, (byte) 45, (byte) 1, (byte) 11,
                (byte) -33, (byte) 0, (byte) 5};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 4);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }
    }

/**
setBlob() - Set a DATALINK parameter.  streamLength>lengthSpecified case
**/
    public void Var056()             // @E1: added this Var
    {
        if (checkLobSupport ()) {
            if (checkJdbc40()) { try {
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_DATALINK) VALUES (?)");
                byte[] b = new byte[] { (byte) -12, (byte) 45, (byte) 1, (byte) 11,
                    (byte) -33, (byte) 0, (byte) 5, (byte) 100};
                InputStream is = new ByteArrayInputStream (b);
                JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 4);
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

    }

/**
setBlob() - Set a DISTINCT parameter.  streamLength>lengthSpecified case
**/
    public void Var057()              // @E1: added this Var
    {
        if (checkLobSupport ()) {
            if (checkJdbc40()) { try {
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_DISTINCT) VALUES (?)");
                byte[] b = new byte[] { (byte) -12, (byte) 1, (byte) 11,
                    (byte) -33, (byte) 0, (byte) 5, (byte) 100};
                InputStream is = new ByteArrayInputStream (b);
                JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 4);
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

    }

/**
setBlob() - Set a BIGINT parameter.  streamLength>lengthSpecified case
**/
    public void Var058()             // @E1: added this Var
    {
        if (checkBigintSupport()) {
            if (checkJdbc40()) { try {
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_BIGINT) VALUES (?)");
                byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2};
                InputStream is = new ByteArrayInputStream (b);
                JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 2);
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }
    }
/**
setBlob() - Set a SMALLINT parameter. streamLength<lengthSpecified case
**/
    public void Var059()    // @E1: added this Var
    {
        if (checkJdbc40()) { try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_SMALLINT) VALUES (?)");
            byte[] b = new byte[] { (byte) 98, (byte) 123};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 1);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }

    }

/**
setBlob() - Set a INTEGER parameter. streamLength<lengthSpecified case
**/
    public void Var060()    // @E1: added this Var
    {
        if (checkJdbc40()) { try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_INTEGER) VALUES (?)");
            byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 2);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }

    }

/**
setBlob() - Set a REAL parameter. streamLength<lengthSpecified case
**/
    public void Var061()    // @E1: added this Var
    {
        if (checkJdbc40()) { try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_REAL) VALUES (?)");
            byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 2);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }

    }

/**
setBlob() - Set a FLOAT parameter. streamLength<lengthSpecified case
**/
    public void Var062()    // @E1: added this Var
    {
        if (checkJdbc40()) { try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_FLOAT) VALUES (?)");
            byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2, (byte) 45};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 3);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }

    }

/**
setBlob() - Set a DOUBLE parameter. streamLength<lengthSpecified case
**/
    public void Var063()       // @E1: added this Var
    {
        if (checkJdbc40()) { try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_DOUBLE) VALUES (?)");
            byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2, (byte) 45,
                (byte) 12};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 4);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }
    }


/**
setBlob() - Set a DECIMAL parameter. streamLength<lengthSpecified case
**/
    public void Var064()       // @E1: added this Var
    {
	if (checkJdbc40()) { try {
	    PreparedStatement ps = connection_.prepareStatement (
								 "INSERT INTO " + JDPSTest.PSTEST_SET
								 + " (C_DECIMAL_105) VALUES (?)");
	    byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2, (byte) 45,
	    (byte) 12, (byte) -33};
	    InputStream is = new ByteArrayInputStream (b);
	    JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 2);
	    ps.executeUpdate(); ps.close ();
	    failed ("Didn't throw SQLException");
	}
	catch (Exception e) {
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
	}

    }

/**
setBlob() - Set a NUMERIC parameter. streamLength<lengthSpecified case
**/
    public void Var065()      // @E1: added this Var
    {
        if (checkJdbc40()) { try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_NUMERIC_50) VALUES (?)");
            byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2, (byte) 45,
                (byte) 12, (byte) -33, (byte) 0};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 1);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }

    }


/**
setBlob() - Set a CHAR(1) parameter. streamLength<lengthSpecified case
**/
    public void Var066()         // @E1: added this Var
    {
        if (checkJdbc40()) { try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_CHAR_1) VALUES (?)");
            byte[] b = new byte[] { (byte) 98, (byte) 98};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 1);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }


    }

/**
setBlob() - Set a CHAR(50) parameter. streamLength<lengthSpecified case
**/
    public void Var067()    // @E1: added this Var
    {
        if (checkJdbc40()) { try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_CHAR_50) VALUES (?)");
            byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -12, (byte) 45,
                (byte) 12, (byte) -33};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 3);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }
    }


/**
setBlob() - Set a VARCHAR(50) parameter. streamLength<lengthSpecified case
**/
    public void Var068()     // @E1: added this Var
    {
        if (checkJdbc40()) { try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_VARCHAR_50) VALUES (?)");
            byte[] b = new byte[] { (byte) 7, (byte) 108, (byte) -12, 
                (byte) 12, (byte) -33};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 1);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }
    }


/**
setBlob() - Set a CLOB parameter. streamLength<lengthSpecified case
**/
    public void Var069()      // @E1: added this Var
    {
        if (checkLobSupport ()) {
            if (checkJdbc40()) { try {
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_CLOB) VALUES (?)");
                byte[] b = new byte[] { (byte) 0, (byte) -12, 
                    (byte) 12, (byte) -33};
                InputStream is = new ByteArrayInputStream (b);
                JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 2);
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

    }


/**
setBlob() - Set a DBCLOB parameter. streamLength<lengthSpecified case
**/
    public void Var070()        // @E1: added this Var
    {
        if (checkLobSupport ()) {
            if (checkJdbc40()) { try {
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_DBCLOB) VALUES (?)");
                byte[] b = new byte[] { (byte) 0, (byte) -12, (byte) 66,
                    (byte) 12, (byte) -33};
                InputStream is = new ByteArrayInputStream (b);
                JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 1);
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

    }

/**
setBlob() - Set a BINARY parameter. streamLength<lengthSpecified case
**/
    public void Var071()         // @E1: added this Var
    {
        if (checkJdbc40()) { try {
            statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_BINARY_20) VALUES (?)");
            byte[] b = new byte[] { (byte) 0, (byte) -12, (byte) 1, (byte) 0,
                (byte) 12, (byte) -33, (byte) 57, (byte) 9};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 3);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_BINARY_20 FROM " + JDPSTest.PSTEST_SET);
            rs.next ();
            byte[] check = rs.getBytes (1);
            rs.close ();

            byte[] check2 = new byte[20];
            System.arraycopy (b, 0, check2, 0, 3); //We are supplying 3 & not b.length as we are writing only 3 bytes

            assertCondition (areEqual (check, check2));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }
/**
setBlob() - Set a BLOB parameter.  streamLength<lengthSpecified case
**/
    public void Var072()       // @E1: added this Var
    {
        if (checkLobSupport ()) {
            if (checkJdbc40()) { try {
                statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_BLOB) VALUES (?)");
                byte[] b = new byte[] { (byte) -12, (byte) 45,
                    (byte) -33, (byte) 0};
                InputStream is = new ByteArrayInputStream (b);
                JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 3);
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_BLOB FROM " + JDPSTest.PSTEST_SET);
                rs.next ();
                InputStream is2 = rs.getBinaryStream(1);
                byte[] check = new byte[b.length];
                is2.read(check);
                is2.close();
                rs.close ();

                assertCondition (areEqual (b, check));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }
    }



/**
setBlob() - Set a DATE parameter.  streamLength<lengthSpecified case
**/
    public void Var073()         // @E1: added this Var
    {
	if (checkJdbc40()) { try {
	    PreparedStatement ps = connection_.prepareStatement (
								 "INSERT INTO " + JDPSTest.PSTEST_SET
								 + " (C_DATE) VALUES (?)");
	    byte[] b = new byte[] { (byte) -12, (byte) 45, (byte) 1,
	    (byte) -33, (byte) 0};
	    InputStream is = new ByteArrayInputStream (b);
	    JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 3);
	    ps.executeUpdate(); ps.close ();
	    failed ("Didn't throw SQLException");
	}
	catch (Exception e) {
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	}
	}
    }


/**
setBlob() - Set a TIME parameter.  streamLength<lengthSpecified case
**/
    public void Var074()            // @E1: added this Var
    {
        if (checkJdbc40()) { try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_TIME) VALUES (?)");
            byte[] b = new byte[] { (byte) -12, (byte) 45, (byte) 1,
                (byte) -33, (byte) 0, (byte) 5};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 4);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }
    }


/**
setBlob() - Set a TIMESTAMP parameter.  streamLength<lengthSpecified case
**/
    public void Var075()         // @E1: added this Var
    {
        if (checkJdbc40()) { try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                + " (C_TIMESTAMP) VALUES (?)");
            byte[] b = new byte[] { (byte) -12, (byte) 45, (byte) 1, (byte) 11,
                (byte) -33, (byte) 0, (byte) 5};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 4);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }
    }

/**
setBlob() - Set a DATALINK parameter.  streamLength<lengthSpecified case
**/
    public void Var076()             // @E1: added this Var
    {
        if (checkLobSupport ()) {
            if (checkJdbc40()) { try {
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_DATALINK) VALUES (?)");
                byte[] b = new byte[] { (byte) -12, (byte) 45, (byte) 1, (byte) 11,
                    (byte) -33, (byte) 0, (byte) 5, (byte) 100};
                InputStream is = new ByteArrayInputStream (b);
                JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 4);
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }
    }



/**
setBlob() - Set a DISTINCT parameter.  streamLength<lengthSpecified case
**/
    public void Var077()              // @E1: added this Var
    {
        if (checkLobSupport ()) {
            if (checkJdbc40()) { try {
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_DISTINCT) VALUES (?)");
                byte[] b = new byte[] { (byte) -12, (byte) 1, (byte) 11,
                    (byte) -33, (byte) 0, (byte) 5, (byte) 100};
                InputStream is = new ByteArrayInputStream (b);
                JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 4);
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }
    }


/**
setBlob() - Set a BIGINT parameter.  streamLength<lengthSpecified case
**/
    public void Var078()             // @E1: added this Var
    {
        if (checkBigintSupport()) {
            if (checkJdbc40()) { try {
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_BIGINT) VALUES (?)");
                byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2};
                InputStream is = new ByteArrayInputStream (b);
                JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 2);
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }
    }
    
    
    /**
    setBlob() - Should throw exception when the length is too large.
    **/
        public void Var079()
        {
            if (checkJdbc40()) { try {
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + JDPSTest.PSTEST_SET
                                                                    + " (C_VARBINARY_20) VALUES (?)");
                byte[] b = new byte[] { (byte) -22, (byte) 98, (byte) -2};
                InputStream is = new ByteArrayInputStream (b);
                JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is,  3000000000L);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }

        }



        /**
         setBlob() - Set a DECFLOAT16 parameter.
         **/
        public void Var080()
        {
          if (checkJdbc40()) {
            if (checkDecFloatSupport()) {
              try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + JDPSTest.PSTEST_SETDFP16
                    + "  VALUES (?)");
                byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2};
                InputStream is = new ByteArrayInputStream (b);
                JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 3);
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
              }
              catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
              }
            }
          }
        }
    /**
     setBlob() - Set a DECFLOAT34 parameter.
     **/
    public void Var081()
    {
      if (checkJdbc40()) {
        if (checkDecFloatSupport()) {
          
          try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + JDPSTest.PSTEST_SETDFP34
                + "  VALUES (?)");
            byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2};
            InputStream is = new ByteArrayInputStream (b);
            JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is, (long) 3);
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
          }
          catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
          }
        }
      }
      
    }





/**
setBlob() - Set an XML  parameter.
**/
	   public void setXML(String tablename, String byteEncoding, String data, String expected) {
	       String added = " -- added by native driver 08/21/2009";

	       if (checkJdbc40()) {

		   String jvm = System.getProperty("java.vm.name");

		   if ((byteEncoding.equals("UCS-2") && jvm.indexOf("Eclipse OpenJ9") >= 0)) {
		       /* Eclipse OpenJ9 does not UCS-2, change to UTF-16BE */
		       byteEncoding = "UTF-16BE"; 
		   }

		   if (checkXmlSupport ()) {
		       try {
			   statement_.executeUpdate ("DELETE FROM " + tablename);

			   PreparedStatement ps = connection_.prepareStatement (
										"INSERT INTO " + tablename
										+ "  VALUES (?)");
			   byte[] b = data.getBytes (byteEncoding); 

			   InputStream is = new ByteArrayInputStream (b);
			   long length = b.length;
			   JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is,  length);


			   ps.executeUpdate ();
			   ps.close ();

			   ResultSet rs = statement_.executeQuery ("SELECT * FROM " + tablename);
			   rs.next ();
			   String check = rs.getString (1);
			   rs.close ();

			   assertCondition (check.equals (expected),
					    "check = "+
					    JDTestUtilities.getMixedString(check)+
					    " And SB "+
					    JDTestUtilities.getMixedString(expected)+
					    added);
		       }
		       catch (Exception e) {
			   failed (e, "Unexpected Exception"+added);
		       }
		   }
	       }
	   }


/**
setBlob() - Set an XML  parameter using invalid data.
**/
	   public void setInvalidXML(String tablename, String byteEncoding, String data, String expectedException) {
	            String added = " -- added by native driver 08/21/2009";
	       if (checkJdbc40()) { 
		   if (checkXmlSupport ()) {
		       try {
			   statement_.executeUpdate ("DELETE FROM " + tablename);

			   PreparedStatement ps = connection_.prepareStatement (
										"INSERT INTO " + tablename
										+ "  VALUES (?)");
			   byte[] b = data.getBytes (byteEncoding); 

			   InputStream is = new ByteArrayInputStream (b);
			   long length = b.length;
			   JDReflectionUtil.callMethod_V(ps, "setBlob", 1, is,  length);

			   ps.executeUpdate ();
			   ps.close ();

			   ResultSet rs = statement_.executeQuery ("SELECT * FROM " + tablename);
			   rs.next ();
			   String check = rs.getString (1);
			   rs.close ();
			   failed("Didn't throw exception but got "+
				  JDTestUtilities.getMixedString(check)+added); 

		       }
		       catch (Exception e) {

			   String message = e.toString();
			   if (message.indexOf(expectedException) >= 0) {
			       assertCondition(true); 
			   } else { 
			       failed (e, "Unexpected Exception.  Expected "+expectedException+added);
			   }
		       }
		   }
	       }
	   }


	   /* Insert various types against a UTF-8 table */
	   public void Var082() { notApplicable();} 
	   public void Var083() { notApplicable();} 
	   public void Var084() { notApplicable();}
	   public void Var085() { notApplicable();} 
	   public void Var086() { notApplicable();} 
	   public void Var087() { notApplicable();} 


	   public void Var088() { setXML(JDPSTest.PSTEST_SETXML, "ISO8859_1", "<Test>Var088</Test>",  "<Test>Var088</Test>"); }
	   public void Var089() { setXML(JDPSTest.PSTEST_SETXML, "ISO8859_1", "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><Test>Var089\u00fb</Test>",  "<Test>Var089\u00fb</Test>"); }
	   public void Var090() { setXML(JDPSTest.PSTEST_SETXML, "UTF-8", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>  <Test>Var090\u00fb</Test>",  "<Test>Var090\u00fb</Test>"); }
	   public void Var091() { setXML(JDPSTest.PSTEST_SETXML, "IBM-037", "<?xml version=\"1.0\" encoding=\"IBM-037\"?>  <Test>Var091\u00fb</Test>",  "<Test>Var091\u00fb</Test>"); }
	   public void Var092() { setXML(JDPSTest.PSTEST_SETXML, "IBM-937", "<?xml version=\"1.0\" encoding=\"IBM-937\"?>  <Test>Var092\u672b</Test>",  "<Test>Var092\u672b</Test>"); }
	   public void Var093() { 
	       if ( isToolboxDriver() )  
           {
               notApplicable("non-supported"); //windows does not like
           }
           else
               setXML(JDPSTest.PSTEST_SETXML, "IBM-290", "<?xml version=\"1.0\" encoding=\"IBM-290\"?>  <Test>Var093\uff7a</Test>",  "<Test>Var093\uff7a</Test>"); }
	   public void Var094() { 
	       if ( isToolboxDriver() )  
           {
               notApplicable("non-supported"); //windows does not like
           }
           else
               setXML(JDPSTest.PSTEST_SETXML, "UCS-2", "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>Var094\u00fb\uff7a</Test>",  "<Test>Var094\u00fb\uff7a</Test>"); }
	   public void Var095() { setXML(JDPSTest.PSTEST_SETXML, "UTF-16BE", "<?xml version=\"1.0\" encoding=\"UTF-16BE\"?>  <Test>Var095\u00fb\uff7a\ud800\udf30</Test>",  "<Test>Var095\u00fb\uff7a\ud800\udf30</Test>"); }
	   public void Var096() { setXML(JDPSTest.PSTEST_SETXML, "UTF-16LE", "<?xml version=\"1.0\" encoding=\"UTF-16LE\"?>  <Test>Var096\u00fb\uff7a\ud800\udf30</Test>",  "<Test>Var096\u00fb\uff7a\ud800\udf30</Test>"); }

           /* Insert various types against a 13488 table */

           public void Var097() {
               
               setXML(JDPSTest.PSTEST_SETXML13488, "ISO8859_1", "<Test>Var097</Test>",  "<Test>Var097</Test>"); }
           public void Var098() { 
               
               setXML(JDPSTest.PSTEST_SETXML13488, "ISO8859_1", "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><Test>Var098\u00fb</Test>",  "<Test>Var098\u00fb</Test>"); }
           public void Var099() { 
             
               setXML(JDPSTest.PSTEST_SETXML13488, "UTF-8", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>  <Test>Var099\u00fb</Test>",  "<Test>Var099\u00fb</Test>"); }
           public void Var100() { 
              
               setXML(JDPSTest.PSTEST_SETXML13488, "IBM-037", "<?xml version=\"1.0\" encoding=\"IBM-037\"?>  <Test>Var100\u00fb</Test>",  "<Test>Var100\u00fb</Test>"); }
           public void Var101() { 
              
               setXML(JDPSTest.PSTEST_SETXML13488, "IBM-937", "<?xml version=\"1.0\" encoding=\"IBM-937\"?>  <Test>Var101\u672b</Test>",  "<Test>Var101\u672b</Test>"); }
           public void Var102() { 
               if( isToolboxDriver()){
                   //ascii data sent to 1208 xml (cent sign conversion from ascii to 1208)
                   notApplicable("NA - XML Toolbox future iteration");
                   return;
               }
               setXML(JDPSTest.PSTEST_SETXML13488, "IBM-290", "<?xml version=\"1.0\" encoding=\"IBM-290\"?>  <Test>Var102\uff7a</Test>",  "<Test>Var102\uff7a</Test>"); }
           public void Var103() { 
               if( isToolboxDriver()){
                   //ascii data sent to 1208 xml (cent sign conversion from ascii to 1208)
                   notApplicable("NA - XML Toolbox future iteration");
                   return;
               }
               setXML(JDPSTest.PSTEST_SETXML13488, "UCS-2", "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>Var103\u00fb\uff7a</Test>",  "<Test>Var103\u00fb\uff7a</Test>"); }
           public void Var104() { 
                
               setXML(JDPSTest.PSTEST_SETXML13488, "UTF-16BE", "<?xml version=\"1.0\" encoding=\"UTF-16BE\"?>  <Test>Var104\u00fb\uff7a</Test>",  "<Test>Var104\u00fb\uff7a</Test>"); }
           public void Var105() { 
               
               setXML(JDPSTest.PSTEST_SETXML13488, "UTF-16LE", "<?xml version=\"1.0\" encoding=\"UTF-16LE\"?>  <Test>Var105\u00fb\uff7a</Test>",  "<Test>Var105\u00fb\uff7a</Test>"); }

           /* Insert various types against a UTF-16 table */

           public void Var106() { 
              
               setXML(JDPSTest.PSTEST_SETXML1200, "ISO8859_1", "<Test>Var106</Test>",  "<Test>Var106</Test>"); }
           public void Var107() { 
                
               setXML(JDPSTest.PSTEST_SETXML1200, "ISO8859_1", "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><Test>Var089\u00fb</Test>",  "<Test>Var089\u00fb</Test>"); }
           public void Var108() { 
              
               setXML(JDPSTest.PSTEST_SETXML1200, "UTF-8", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>  <Test>Var090\u00fb</Test>",  "<Test>Var090\u00fb</Test>"); }
           public void Var109() { 
              
               setXML(JDPSTest.PSTEST_SETXML1200, "IBM-037", "<?xml version=\"1.0\" encoding=\"IBM-037\"?>  <Test>Var091\u00fb</Test>",  "<Test>Var091\u00fb</Test>"); }
           public void Var110() { 
             
               setXML(JDPSTest.PSTEST_SETXML1200, "IBM-937", "<?xml version=\"1.0\" encoding=\"IBM-937\"?>  <Test>Var092\u672b</Test>",  "<Test>Var092\u672b</Test>"); }
           public void Var111() { 
               if ( isToolboxDriver() )  
               {
                   notApplicable("non-supported"); //windows does not like
                   return;
               }
               setXML(JDPSTest.PSTEST_SETXML1200, "IBM-290", "<?xml version=\"1.0\" encoding=\"IBM-290\"?>  <Test>Var093\uff7a</Test>",  "<Test>Var093\uff7a</Test>"); }
           public void Var112() { 
               if ( isToolboxDriver() )  
               {
                   notApplicable("non-supported"); //windows does not like
                   return;
               }
               setXML(JDPSTest.PSTEST_SETXML1200, "UCS-2", "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>Var094\u00fb\uff7a</Test>",  "<Test>Var094\u00fb\uff7a</Test>"); }
           public void Var113() {
               
               setXML(JDPSTest.PSTEST_SETXML1200, "UTF-16BE", "<?xml version=\"1.0\" encoding=\"UTF-16BE\"?>  <Test>Var095\u00fb\uff7a\ud800\udf30</Test>",  "<Test>Var095\u00fb\uff7a\ud800\udf30</Test>"); }
           public void Var114() { 
              
               setXML(JDPSTest.PSTEST_SETXML1200, "UTF-16LE", "<?xml version=\"1.0\" encoding=\"UTF-16LE\"?>  <Test>Var096\u00fb\uff7a\ud800\udf30</Test>",  "<Test>Var096\u00fb\uff7a\ud800\udf30</Test>"); }


           /* Insert various types against a EBCDIC-37 table */ 

           public void Var115() { setXML(JDPSTest.PSTEST_SETXML37, "ISO8859_1", "<Test>Var115</Test>",  "<Test>Var115</Test>"); }
           public void Var116() { setXML(JDPSTest.PSTEST_SETXML37, "ISO8859_1", "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><Test>Var089\u00fb</Test>",  "<Test>Var089\u00fb</Test>"); }
           public void Var117() { setXML(JDPSTest.PSTEST_SETXML37, "UTF-8", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>  <Test>Var090\u00fb</Test>",  "<Test>Var090\u00fb</Test>"); }
           public void Var118() { setXML(JDPSTest.PSTEST_SETXML37, "IBM-037", "<?xml version=\"1.0\" encoding=\"IBM-037\"?>  <Test>Var091\u00fb</Test>",  "<Test>Var091\u00fb</Test>"); }
           public void Var119() { setXML(JDPSTest.PSTEST_SETXML37, "IBM-937", "<?xml version=\"1.0\" encoding=\"IBM-937\"?>  <Test>Var092</Test>",  "<Test>Var092</Test>"); }
           public void Var120() { 
               if ( isToolboxDriver() )  
               {
                   notApplicable("non-supported"); //windows does not like
                   return;
               }
               setXML(JDPSTest.PSTEST_SETXML37, "IBM-290", "<?xml version=\"1.0\" encoding=\"IBM-290\"?>  <Test>Var093</Test>",  "<Test>Var093</Test>"); }
           public void Var121() {
               if ( isToolboxDriver() )  
               {
                   notApplicable("non-supported"); //windows does not like
                   return;
               }
               setXML(JDPSTest.PSTEST_SETXML37, "UCS-2", "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>Var094\u00fb</Test>",  "<Test>Var094\u00fb</Test>"); }
           public void Var122() { setXML(JDPSTest.PSTEST_SETXML37, "UTF-16BE", "<?xml version=\"1.0\" encoding=\"UTF-16BE\"?>  <Test>Var095\u00fb</Test>",  "<Test>Var095\u00fb</Test>"); }
           public void Var123() { setXML(JDPSTest.PSTEST_SETXML37, "UTF-16LE", "<?xml version=\"1.0\" encoding=\"UTF-16LE\"?>  <Test>Var096\u00fb</Test>",  "<Test>Var096\u00fb</Test>"); }

           
           /* Insert various types against a EBCDIC-937 table */

           public void Var124() { setXML(JDPSTest.PSTEST_SETXML937, "ISO8859_1", "<Test>Var088</Test>",  "<Test>Var088</Test>"); }
           public void Var125() { setXML(JDPSTest.PSTEST_SETXML937, "ISO8859_1", "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><Test>Var089</Test>",  "<Test>Var089</Test>"); }
           public void Var126() { setXML(JDPSTest.PSTEST_SETXML937, "UTF-8", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>  <Test>Var090\u672b</Test>",  "<Test>Var090\u672b</Test>"); }
           public void Var127() { setXML(JDPSTest.PSTEST_SETXML937, "IBM-037", "<?xml version=\"1.0\" encoding=\"IBM-037\"?>  <Test>Var091</Test>",  "<Test>Var091</Test>"); }
           public void Var128() { setXML(JDPSTest.PSTEST_SETXML937, "IBM-937", "<?xml version=\"1.0\" encoding=\"IBM-937\"?>  <Test>Var092\u672b</Test>",  "<Test>Var092\u672b</Test>"); }
           public void Var129() { 
               if ( isToolboxDriver() )  
               {
                   notApplicable("non-supported"); //windows does not like
                   return;
               }
               setXML(JDPSTest.PSTEST_SETXML937, "IBM-290", "<?xml version=\"1.0\" encoding=\"IBM-290\"?>  <Test>Var093</Test>",  "<Test>Var093</Test>"); }
           public void Var130() { 
               if ( isToolboxDriver() )  
               {
                   notApplicable("non-supported"); //windows does not like
                   return;
               }
               setXML(JDPSTest.PSTEST_SETXML937, "UCS-2", "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>Var094\u672b</Test>",  "<Test>Var094\u672b</Test>"); }
           public void Var131() { setXML(JDPSTest.PSTEST_SETXML937, "UTF-16BE", "<?xml version=\"1.0\" encoding=\"UTF-16BE\"?>  <Test>Var095\u672b</Test>",  "<Test>Var095\u672b</Test>"); }
           public void Var132() { setXML(JDPSTest.PSTEST_SETXML937, "UTF-16LE", "<?xml version=\"1.0\" encoding=\"UTF-16LE\"?>  <Test>Var096\u672b</Test>",  "<Test>Var096\u672b</Test>"); }


           /* Insert various types against a EBCDIC 290 table */ 

           public void Var133() { setXML(JDPSTest.PSTEST_SETXML290, "ISO8859_1", "<Test>Var133</Test>",  "<Test>Var133</Test>"); }
           public void Var134() { setXML(JDPSTest.PSTEST_SETXML290, "ISO8859_1", "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><Test>Var139</Test>",  "<Test>Var139</Test>"); }
           public void Var135() { setXML(JDPSTest.PSTEST_SETXML290, "UTF-8", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>  <Test>Var090\uff7a</Test>",  "<Test>Var090\uff7a</Test>"); }
           public void Var136() { setXML(JDPSTest.PSTEST_SETXML290, "IBM-037", "<?xml version=\"1.0\" encoding=\"IBM-037\"?>  <Test>Var091</Test>",  "<Test>Var091</Test>"); }
           public void Var137() { setXML(JDPSTest.PSTEST_SETXML290, "IBM-937", "<?xml version=\"1.0\" encoding=\"IBM-937\"?>  <Test>Var092</Test>",  "<Test>Var092</Test>"); }
           public void Var138() {
               if ( isToolboxDriver() )  
               {
                   notApplicable("non-supported"); //windows does not like
                   return;
               }
               setXML(JDPSTest.PSTEST_SETXML290, "IBM-290", "<?xml version=\"1.0\" encoding=\"IBM-290\"?>  <Test>Var093\uff7a</Test>",  "<Test>Var093\uff7a</Test>"); }
           public void Var139() {
               if ( isToolboxDriver() )  
               {
                   notApplicable("non-supported"); //windows does not like
                   return;
               }
               setXML(JDPSTest.PSTEST_SETXML290, "UCS-2", "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>Var094\uff7a</Test>",  "<Test>Var094\uff7a</Test>"); }
           public void Var140() { setXML(JDPSTest.PSTEST_SETXML290, "UTF-16BE", "<?xml version=\"1.0\" encoding=\"UTF-16BE\"?>  <Test>Var145\uff7a</Test>",  "<Test>Var145\uff7a</Test>"); }
           public void Var141() { setXML(JDPSTest.PSTEST_SETXML290, "UTF-16LE", "<?xml version=\"1.0\" encoding=\"UTF-16LE\"?>  <Test>Var146\uff7a</Test>",  "<Test>Var146\uff7a</Test>"); }


           
           /**
           setBlob() - Set a BOOLEAN parameter.
           **/
               public void Var142()
	       {
		   if (checkBooleanSupport()) { 
		       byte[] b = new byte[] { (byte) 98, (byte) 123};

		       testSetFailed("C_BOOLEAN",b); 
		   }
	       }




}





