///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSSetClob.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.PS;

import java.io.FileOutputStream;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDLobTest;
import test.JDPSTest;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JDTestcase;
import test.JD.JDSetupPackage;
import test.JD.JDTestUtilities;
import test.JD.JDSerializeFile;



/**
Testcase JDPSSetClob.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setClob()
</ul>
**/
public class JDPSSetClob
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDPSSetClob";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDPSTest.main(newArgs); 
   }



    // Constants.
    private static final String PACKAGE             = "JDPSSCLOB";



    // Private data.
    private Statement           statement_;


/**
Constructor.
**/
    public JDPSSetClob (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDPSSetClob",
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
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        String url = baseURL_; 
        connection_ = testDriver_.getConnection (url, userId_, encryptedPassword_);
        
      } else { 
      
        String url = baseURL_
            
             + ";lob threshold=30000"
            + ";data truncation=true";
        connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
      }
        statement_ = connection_.createStatement ();
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
        connection_ = null; 

    }



/**
Compares a Clob with a String.
**/
    private boolean compare (Clob i, String b)
        throws SQLException
    {
        return i.getSubString (1, (int) i.length ()).equals (b);            // @B1C
    }



/**
setClob() - Should throw exception when the prepared
statement is closed.
**/
    public void Var001()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc20 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + pstestSet.getName()
                    + " (C_VARCHAR_50) VALUES (?)");
                ps.close ();
                ps.setClob (1, new JDLobTest.JDTestClob ("Los Angeles"));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }



/**
setClob() - Should throw exception when an invalid index is
specified.
**/
    public void Var002()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc20 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + pstestSet.getName()
                    + " (C_INTEGER, C_SMALLINT) VALUES (?, ?)");
                ps.setClob (100, new JDLobTest.JDTestClob ("San Bernadino"));
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }



/**
setClob() - Should throw exception when index is 0.
**/
    public void Var003()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc20 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + pstestSet.getName()
                    + " (C_INTEGER, C_SMALLINT) VALUES (?, ?)");
                ps.setClob (0, new JDLobTest.JDTestClob ("Venice"));
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }



/**
setClob() - Should throw exception when index is -1.
**/
    public void Var004()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc20 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + pstestSet.getName()
                    + " (C_INTEGER, C_SMALLINT) VALUES (?, ?)");
                ps.setClob (-1, new JDLobTest.JDTestClob ("Beverly Hills"));
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }



/**
setClob() - Should throw exception when the value is null.
**/
    public void Var005()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc20 ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + pstestSet.getName()
                    + " (C_VARCHAR_50) VALUES (?)");
                ps.setClob (1,(java.sql.Clob) null);
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + pstestSet.getName());
                rs.next ();
                Clob check = rs.getClob (1);
                boolean wn = rs.wasNull ();
                rs.close ();

                assertCondition ((check == null) && (wn == true));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }



/**
setClob() - Should work with a valid parameter index
greater than 1.
**/
    public void Var006()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc20 ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + pstestSet.getName()
                    + " (C_KEY, C_VARCHAR_50) VALUES (?, ?)");
                ps.setString (1, "Muchas");
                ps.setClob (2, new JDLobTest.JDTestClob ("Hollywood"));
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + pstestSet.getName());
                rs.next ();
                Clob check = rs.getClob (1);
                rs.close ();

                assertCondition (compare (check, "Hollywood"));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }



/**
setClob() - Should throw exception when the parameter is
not an input parameter.
**/
    public void Var007()
    {
        if (checkJdbc20 ()) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            notApplicable("JCC permits setting of an output parameter");
            return; 
          } 
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "CALL " + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
                ps.setClob (2, new JDLobTest.JDTestClob ("Burbank"));
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setClob() - Verify that a data truncation warning is
posted when data is truncated.
**/
    public void Var008()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc20 ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + pstestSet.getName()
                    + " (C_VARCHAR_50) VALUES (?)");
                ps.setClob (1, new JDLobTest.JDTestClob ("The example strings in this test case are places in the Los Angeles area."));
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (DataTruncation dt) {
                assertCondition ((dt.getIndex() == 1)
                    && (dt.getParameter() == true)
                    && (dt.getRead() == false)
                    && (dt.getDataSize() == 73)
                    && (dt.getTransferSize() == 50));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }





/**
setClob() - Set a SMALLINT parameter.
**/
    public void Var009()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc20 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + pstestSet.getName()
                    + " (C_SMALLINT) VALUES (?)");
                ps.setClob (1, new JDLobTest.JDTestClob ("Anahiem"));
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }



/**
setClob() - Set a INTEGER parameter.
**/
    public void Var010()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc20 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + pstestSet.getName()
                    + " (C_INTEGER) VALUES (?)");
                ps.setClob (1, new JDLobTest.JDTestClob ("Pasadena"));
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }



/**
setClob() - Set a REAL parameter.
**/
    public void Var011()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc20 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + pstestSet.getName()
                    + " (C_REAL) VALUES (?)");
                ps.setClob (1, new JDLobTest.JDTestClob ("West Hollywood"));
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }



/**
setClob() - Set a FLOAT parameter.
**/
    public void Var012()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc20 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + pstestSet.getName()
                    + " (C_FLOAT) VALUES (?)");
                ps.setClob (1, new JDLobTest.JDTestClob ("Sierra Madre"));
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }



/**
setClob() - Set a DOUBLE parameter.
**/
    public void Var013()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc20 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + pstestSet.getName()
                    + " (C_DOUBLE) VALUES (?)");
                ps.setClob (1, new JDLobTest.JDTestClob ("Malibu"));
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }



/**
setClob() - Set a DECIMAL parameter.
**/
    public void Var014()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc20 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + pstestSet.getName()
                    + " (C_DECIMAL_105) VALUES (?)");
                ps.setClob (1, new JDLobTest.JDTestClob ("Long Beach"));
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }



/**
setClob() - Set a NUMERIC parameter.
**/
    public void Var015()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc20 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + pstestSet.getName()
                    + " (C_NUMERIC_50) VALUES (?)");
                ps.setClob (1, new JDLobTest.JDTestClob ("Compton"));
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }



/**
setClob() - Set a CHAR(1) parameter, where the string is
longer.
**/
    public void Var016()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc20 ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + pstestSet.getName()
                    + " (C_CHAR_1) VALUES (?)");
                ps.setClob (1, new JDLobTest.JDTestClob ("S"));
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_CHAR_1 FROM " + pstestSet.getName());
                rs.next ();
                Clob check = rs.getClob (1);
                rs.close ();

                assertCondition (compare (check, "S"));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }




/**
setClob() - Set a CHAR(50) parameter.
**/
    public void Var017()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc20 ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + pstestSet.getName()
                    + " (C_CHAR_50) VALUES (?)");
                ps.setClob (1, new JDLobTest.JDTestClob ("Orange"));
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_CHAR_50 FROM " + pstestSet.getName());
                rs.next ();
                Clob check = rs.getClob (1);
                rs.close ();

                assertCondition (compare (check, "Orange                                            "));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }



/**
setClob() - Set a VARCHAR(50) parameter.
**/
    public void Var018()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc20 ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + pstestSet.getName()
                    + " (C_VARCHAR_50) VALUES (?)");
                ps.setClob (1, new JDLobTest.JDTestClob ("La Brea"));
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + pstestSet.getName());
                rs.next ();
                Clob check = rs.getClob (1);
                rs.close ();

                assertCondition (compare (check, "La Brea"));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }



/**
setClob() - Set a CLOB parameter, when the data is passed directly.
**/
    public void Var019()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

                    PreparedStatement ps = connection_.prepareStatement (
                        "INSERT INTO " + pstestSet.getName()
                        + " (C_CLOB) VALUES (?)");
                    ps.setClob (1, new JDLobTest.JDTestClob ("Disneyland"));
                    ps.executeUpdate ();
                    ps.close ();

                    ResultSet rs = statement_.executeQuery ("SELECT C_CLOB FROM " + pstestSet.getName());
                    rs.next ();
                    Clob check = rs.getClob (1);
                    rs.close ();

                    assertCondition (compare (check, "Disneyland"),check.getSubString(1,(int)check.length())+" sb Disneyland");
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }




/**
setClob() - Set a CLOB parameter, when the data is passed in
a locator.
**/
    public void Var020()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

                    String url = baseURL_
                        
                         + ";lob threshold=1";
                    Connection c = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
                    PreparedStatement ps = c.prepareStatement (
                        "INSERT INTO " + pstestSet.getName()
                        + " (C_CLOB) VALUES (?)");
                    ps.setClob (1, new JDLobTest.JDTestClob ("Disneyland Hotel"));
                    ps.executeUpdate ();
                    ps.close ();
                    c.close ();

                    ResultSet rs = statement_.executeQuery ("SELECT C_CLOB FROM " + pstestSet.getName());
                    rs.next ();
                    Clob check = rs.getClob (1);
                    rs.close ();

                    assertCondition (compare (check, "Disneyland Hotel"),check.getSubString(1,(int)check.length())+" sb Disneyland Hotel");
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }




/**
setClob() - Set a DBCLOB parameter, when the data is passed
directly.
**/
    public void Var021()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                succeeded ();
                /* Need to investigate this variation ...
                try {
                    statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

                    PreparedStatement ps = connection_.prepareStatement (
                        "INSERT INTO " + pstestSet.getName()
                        + " (C_DBCLOB) VALUES (?)");
                    ps.setClob (1, new JDLobTest.JDTestClob ("Rodeo Drive"));
                    ps.executeUpdate ();
                    ps.close ();

                    ResultSet rs = statement_.executeQuery ("SELECT C_DBCLOB FROM " + pstestSet.getName());
                    rs.next ();
                    Clob check = rs.getClob (1);
                    rs.close ();

                    assertCondition (compare (check, "Rodeo Drive"));
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
                */
            }
        }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }



/**
setClob() - Set a DBCLOB parameter, when the data is passed as a locator.
**/
    public void Var022()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                succeeded ();
                /* Need to investigate this variation ...
                try {
                    statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

                    String url = baseURL_
                        
                         + ";lob threshold=1";
                    Connection c = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
                    PreparedStatement ps = c.prepareStatement (
                        "INSERT INTO " + pstestSet.getName()
                        + " (C_DBCLOB) VALUES (?)");
                    ps.setClob (1, new JDLobTest.JDTestClob ("Disneyland Motel"));
                    ps.executeUpdate ();
                    ps.close ();
                    c.close ();

                    ResultSet rs = statement_.executeQuery ("SELECT C_DBCLOB FROM " + pstestSet.getName());
                    rs.next ();
                    Clob check = rs.getClob (1);
                    rs.close ();

                    assertCondition (compare (check, "Disneyland Hotel"));
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
                */
            }
        }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }



/**
setClob() - Set a BINARY parameter.
**/
    public void Var023()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc20 ()) {
            try {
               String expected = null;

               if (isToolboxDriver())
                  expected = "436F6C6F6D616960";
               else
                  expected = "Colombia Heights";

                statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + pstestSet.getName()
                    + " (C_BINARY_20) VALUES (?)");

                ps.setClob (1, new JDLobTest.JDTestClob (expected));
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_BINARY_20 FROM " + pstestSet.getName());
                rs.next ();
                Clob check = rs.getClob (1);
                rs.close ();

                // Spaces get translated different, so we kluge this
                // comparison.
                assertCondition (check.getSubString (1, 12).equals (expected.substring(0,12)));         // @B1C
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }




/**
setClob() - Set a VARBINARY parameter.
**/
    public void Var024()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc20 ()) {
            try 
            {
                String expected = null;

                if (isToolboxDriver())
                   expected = "507565";
                else
                   expected = "Puerto";

                statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + pstestSet.getName()
                    + " (C_VARBINARY_20) VALUES (?)");
 
                ps.setClob (1, new JDLobTest.JDTestClob (expected));
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_VARBINARY_20 FROM " + pstestSet.getName());
                rs.next ();
                Clob check = rs.getClob (1);
                rs.close ();

                assertCondition (compare (check, expected));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }




/**
setClob() - Set a BLOB parameter.
**/
    public void Var025()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        if (checkJdbc20 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + pstestSet.getName()
                    + " (C_BLOB) VALUES (?)");
                ps.setClob (1, new JDLobTest.JDTestClob ("California"));
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    }




/**
setClob() - Set a DATE parameter.
**/
    public void Var026()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        if (checkJdbc20 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + pstestSet.getName()
                    + " (C_DATE) VALUES (?)");
                ps.setClob (1, new JDLobTest.JDTestClob ("West Coast"));
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    }



/**
setClob() - Set a TIME parameter.
**/
    public void Var027()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        if (checkJdbc20 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + pstestSet.getName()
                    + " (C_TIME) VALUES (?)");
                ps.setClob (1, new JDLobTest.JDTestClob ("Pacific Ocean"));
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    }



/**
setClob() - Set a TIMESTAMP parameter.
**/
    public void Var028()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        if (checkJdbc20 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + pstestSet.getName()
                    + " (C_TIMESTAMP) VALUES (?)");
                ps.setClob (1, new JDLobTest.JDTestClob ("Airport"));
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    }


/**
setClob() - Set a DATALINK parameter.
**/
    public void Var029()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
         if ((getDriver () == JDTestDriver.DRIVER_NATIVE) && (getJdbcLevel() <= 2)) {
            notApplicable ("Native driver pre-JDBC 3.0");
            return;
        }
        if (checkJdbc20 ()) {
            if (checkDatalinkSupport ()) {
                try {
                    PreparedStatement ps = connection_.prepareStatement (
                        "INSERT INTO " + pstestSet.getName()
                        + " (C_DATALINK) VALUES (?)");
                    ps.setClob (1, new JDLobTest.JDTestClob ("Dodger Stadium"));
                    ps.executeUpdate(); ps.close ();
                    failed ("Didn't throw SQLException");
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    }



/**
setClob() - Set a DISTINCT parameter.

SQL400 - Native JDBC has built the setClob support on top of the setCharacterStream.
setCharacterStream is built on top of setString.  With setString, this call works.
I could have put a call into setClob to check the data type and cause this path to
fail, but I have been so mean to that block of code lately that I decided that I would
just leave it working and change this testcase to expect it to work for now.  Sorry to
anyone that this annoys. :)
**/
    public void Var030()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    PreparedStatement ps = connection_.prepareStatement (
                        "INSERT INTO " + pstestSet.getName()
                        + " (C_DISTINCT) VALUES (?)");
                    ps.setClob (1, new JDLobTest.JDTestClob ("12345678"));
                    ps.executeUpdate(); ps.close ();
                    if (isToolboxDriver())
                        failed ("Didn't throw SQLException");
                    else
                        assertCondition(true);
                }
                catch (Exception e) {
                    if (isToolboxDriver())
                        assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                    else
                        failed (e, "Unexpected Exception");
                }
            }
        }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    }



/**
setClob() - Set a BIGINT parameter.
**/
    public void Var031()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        if (checkJdbc20 ()) {
        if (checkBigintSupport()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + pstestSet.getName()
                    + " (C_BIGINT) VALUES (?)");
                ps.setClob (1, new JDLobTest.JDTestClob ("Orange County"));
                ps.executeUpdate(); ps.close ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    }



/**
setClob() - Set a VARCHAR(50) parameter.

SQL400 - JDSetupPackage cache doesn't have enough information to be
         driver neutral today.
**/
    public void Var032()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        if (checkJdbc20 ()) {
            try {
                String insert = "INSERT INTO " + pstestSet.getName()
                    + " (C_VARCHAR_50) VALUES (?)";

                if (isToolboxDriver())
                   JDSetupPackage.prime (systemObject_,
                       PACKAGE, JDPSTest.COLLECTION, insert);
                else
                   JDSetupPackage.prime (systemObject_, encryptedPassword_,
                       PACKAGE, JDPSTest.COLLECTION, insert, "", getDriver());

                statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

                Connection c2 = testDriver_.getConnection (baseURL_
                    + ";extended dynamic=true;package=" + PACKAGE
                    + ";package library=" + JDPSTest.COLLECTION
                    + ";package cache=true", userId_, encryptedPassword_);
                PreparedStatement ps = c2.prepareStatement (insert);
                ps.setClob (1, new JDLobTest.JDTestClob ("Rose Bowl"));
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + pstestSet.getName());
                rs.next ();
                Clob check = rs.getClob (1);
                rs.close ();

                assertCondition (compare (check, "Rose Bowl"));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    }


/**
setClob() - Set a CLOB parameter, when the number of parameters is
> 20 and the only clob parameter is the first parameter.
This detects a bug in the native JDBC driver due to the
CLI. 
**/
    public void Var033()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
		    String sql;
		    int columnCount = 31;
		    String table = JDPSTest.COLLECTION + ".JDPSSC"+columnCount;

		    try {
			statement_.executeUpdate("drop table "+table); 
		    } catch (Exception e) {
		    } 


		    sql = "create table "+table+" ( c1 clob(1M) ";
		    for (int i = 2; i <= columnCount; i++) {
			sql+=", c"+i+" int "; 
		    }
		    sql += ")";
		    statement_.executeUpdate(sql);

		    sql = "UPDATE "+table+" set C1=?";
		    for (int i = 2; i <= columnCount; i++) {
			sql+=", c"+i+" = ? "; 
		    }


		    PreparedStatement pstmt = connection_.prepareStatement(sql);
		    pstmt.setString(1,"X");
		    for (int i = 2; i <= columnCount; i++) {
			pstmt.setInt(i,i); 
		    }

		    pstmt.execute();

		    pstmt.close();


                    assertCondition (true); 
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception -- testcase added by native driver 3/1/05 to detect CLI bug");
                }
            }
        }
    }


    /**
    setClob() - Set a DECFLOAT parameter.
    **/
        public void Var034()
        {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSetdfp16(connection_);
if (checkDecFloatSupport()) {
                try {
                    PreparedStatement ps = connection_.prepareStatement (
                        "INSERT INTO " + pstestSet.getName()
                        + "  VALUES (?)");
                    ps.setClob (1, new JDLobTest.JDTestClob ("Orange County"));
                    ps.executeUpdate(); ps.close ();
                    failed ("Didn't throw SQLException");
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }
        /**
        setClob() - Set a DECFLOAT parameter.
        **/
            public void Var035()
            {
                
                if (checkDecFloatSupport()) {
                  JDSerializeFile pstestSetdfp = null;
                  try {
                    pstestSetdfp = JDPSTest.getPstestSetdfp34(connection_);
                    String tablename34=pstestSetdfp.getName();
                        PreparedStatement ps = connection_.prepareStatement (
                            "INSERT INTO " + tablename34
                            + "  VALUES (?)");
                        ps.setClob (1, new JDLobTest.JDTestClob ("Orange County"));
                        ps.executeUpdate(); ps.close ();
                        failed ("Didn't throw SQLException");
                    }
                    catch (Exception e) {
                        assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                    } finally {
                      if (pstestSetdfp != null) {
                        try {
                          pstestSetdfp.close();
                        } catch (SQLException e) {
                          e.printStackTrace();
                        }
                      }
                    }
                }
               
            }

	    public void Var036() { notApplicable(); } 
	    public void Var037() { notApplicable(); } 
	    public void Var038() { notApplicable(); } 
	    public void Var039() { notApplicable(); } 
	    public void Var040() { notApplicable(); } 
	    public void Var041() { notApplicable(); } 
	    public void Var042() { notApplicable(); } 
	    public void Var043() { notApplicable(); } 


/**
setClob() - Set an XML  parameter.
**/
	   public void setXML(int table, String data, String expected) {
	       String added = " -- added by native driver 08/21/2009";
	       if (checkXmlSupport ()) {
                 JDSerializeFile pstestSetxml = null;
                 try {
                   pstestSetxml = JDPSTest.getSerializeFile(connection_, table);
                   String tablename = pstestSetxml.getName(); 
		       statement_.executeUpdate ("DELETE FROM " + tablename);

		       PreparedStatement ps = connection_.prepareStatement (
									    "INSERT INTO " + tablename
									    + "  VALUES (?)");


		       ps.setClob (1, new JDLobTest.JDTestClob (data));

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
					added );
		   }
		   catch (Exception e) {
		       failed (e, "Unexpected Exception"+added);
                   } finally {
                     if (pstestSetxml != null) {
                       try {
                         pstestSetxml.close();
                       } catch (SQLException e) {
                         e.printStackTrace();
                       }
                     }
		   }
	       }
	   }



/**
setClob() - Set an XML  parameter using invalid data.
**/
	   public void setInvalidXML(int table, String data, String expectedException) {
	            String added = " -- added by native driver 08/21/2009";
	       if (checkXmlSupport ()) {
                 JDSerializeFile pstestSetxml = null;
                 try {
                   pstestSetxml = JDPSTest.getSerializeFile(connection_, table);
                   String tablename = pstestSetxml.getName(); 
		       statement_.executeUpdate ("DELETE FROM " + tablename);

		       PreparedStatement ps = connection_.prepareStatement (
									    "INSERT INTO " + tablename
									    + "  VALUES (?)");

		      		       ps.setClob (1, new JDLobTest.JDTestClob (data));

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
                   } finally {
                     if (pstestSetxml != null) {
                       try {
                         pstestSetxml.close();
                       } catch (SQLException e) {
                         e.printStackTrace();
                       }
                     }
		   }
	       }
	   }

	   public void Var044() { setXML(JDPSTest.SETXML,  "<Test>VAR044\u00a2</Test>",  "<Test>VAR044\u00a2</Test>"); }
	   public void Var045() { setXML(JDPSTest.SETXML,  "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>VAR045\u00a2</Test>",  "<Test>VAR045\u00a2</Test>"); }
	   public void Var046() { setXML(JDPSTest.SETXML,  "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Test>VAR046\u0130\u3041\ud800\udf30</Test>",  "<Test>VAR046\u0130\u3041\ud800\udf30</Test>"); }

	   public void Var047() { setXML(JDPSTest.SETXML13488, "<Test>VAR047</Test>",  "<Test>VAR047</Test>"); }
	   public void Var048() { setXML(JDPSTest.SETXML13488, "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>VAR048\u00a2</Test>",  "<Test>VAR048\u00a2</Test>"); }
	   public void Var049() { setXML(JDPSTest.SETXML13488, "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Test>VAR049\u0130\u3041</Test>",  "<Test>VAR049\u0130\u3041</Test>"); }

	   public void Var050() { setXML(JDPSTest.SETXML1200, "<Test>VAR050</Test>",  "<Test>VAR050</Test>"); }
	   public void Var051() { setXML(JDPSTest.SETXML1200, "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>VAR051\u00a2</Test>",  "<Test>VAR051\u00a2</Test>"); }
	   public void Var052() { setXML(JDPSTest.SETXML1200, "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Test>VAR052\u0130\u3041\ud800\udf30</Test>",  "<Test>VAR052\u0130\u3041\ud800\udf30</Test>"); }

	   public void Var053() { setXML(JDPSTest.SETXML37, "<Test>VAR053\u00a2</Test>",  "<Test>VAR053\u00a2</Test>"); }
	   public void Var054() { setXML(JDPSTest.SETXML37, "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>VAR054\u00a2</Test>",  "<Test>VAR054\u00a2</Test>"); }
	   public void Var055() { setXML(JDPSTest.SETXML37, "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Test>VAR055\u00a2</Test>",  "<Test>VAR055\u00a2</Test>"); }

	   public void Var056() { setXML(JDPSTest.SETXML937, "<Test>VAR056\u672b</Test>",  "<Test>VAR056\u672b</Test>"); }
	   public void Var057() { setXML(JDPSTest.SETXML937, "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>VAR057\u672b</Test>",  "<Test>VAR057\u672b</Test>"); }
	   public void Var058() { setXML(JDPSTest.SETXML937, "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Test>VAR058\u672b</Test>",  "<Test>VAR058\u672b</Test>"); }

	   public void Var059() { setXML(JDPSTest.SETXML290, "<Test>VAR059</Test>",  "<Test>VAR059</Test>"); }
	   public void Var060() { setXML(JDPSTest.SETXML290, "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>VAR060\uff7a</Test>",  "<Test>VAR060\uff7a</Test>"); }
	   public void Var061() { setXML(JDPSTest.SETXML290, "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Test>VAR061\uff98</Test>",  "<Test>VAR061\uff98</Test>"); }



	   /* Encoding is stripped for character data since we know is is UTF-16 */ 
	   public void Var062() { setXML(JDPSTest.SETXML, "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Test>VAR062</Test>",  "<Test>VAR062</Test>"); }
	   public void Var063() { setInvalidXML(JDPSTest.SETXML, "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Test>VAR063</Tes>",  "XML parsing failed"); }

	   public void Var064() { setXML(JDPSTest.SETXML13488, "<?xml version=\"1.0\" encoding=\"IBM-037\"?><Test>VAR064</Test>",  "<Test>VAR064</Test>" ); }
	   public void Var065() { setInvalidXML(JDPSTest.SETXML13488, "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Tes>VAR065</Test>",  "XML parsing failed"); }

	   public void Var066() { setXML(JDPSTest.SETXML1200, "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Test>VAR066</Test>",  "<Test>VAR066</Test>"); }
	   public void Var067() { setInvalidXML(JDPSTest.SETXML1200, "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Tes>VAR067</Test>",  "XML parsing failed"); }

	   public void Var068() { setXML(JDPSTest.SETXML37, "<?xml version=\"1.0\" encoding=\"IBM-037\"?><Test>VAR068\u00a2</Test>",  "<Test>VAR068\u00a2</Test>"); }
	   public void Var069() { setInvalidXML(JDPSTest.SETXML37, "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Tet>VAR069</Test>",  "XML parsing failed"); }

	    public void setInvalid(String column, String inputValue, String exceptionInfo)  {
	      StringBuffer sb = new StringBuffer(); 
	      JDSerializeFile pstestSet = null;
	      try {
	        pstestSet = JDPSTest.getPstestSet(connection_);
	        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

	        PreparedStatement ps = connection_.prepareStatement(
	            "INSERT INTO " + pstestSet.getName() + " ("+column+") VALUES (?)");
          ps.setClob (1, new JDLobTest.JDTestClob (inputValue));
	        ps.close();
	        failed("Didn't throw SQLException for column("+column+") inputValue("+inputValue+")");
	      } catch (Exception e) {
	        assertExceptionContains(e, exceptionInfo, sb);
	      } finally {
	        if (pstestSet != null) {
	          try {
	            pstestSet.close();
	          } catch (SQLException e) {
	            e.printStackTrace();
	          }
	        }
	      }
	    }

	    public void Var070() {
		if (checkBooleanSupport()) { 
		    setInvalid("C_BOOLEAN","true","Data type mismatch" );
		}
	    }

	     public void Var071() {
		 if (checkBooleanSupport()) { 
		     setInvalid("C_BOOLEAN","false","Data type mismatch" );
		 }
	     }

	   
}



