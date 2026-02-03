///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSSetDate.java
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
 // File Name:    JDPSSetDate.java
 //
 // Classes:      JDPSSetDate
 //
 ////////////////////////////////////////////////////////////////////////
 //
 //
 //                                                  Toolbox.
 //
 //
 ////////////////////////////////////////////////////////////////////////

package test.JD.PS;

import com.ibm.as400.access.AS400;

import test.JDPSTest;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JDTestcase;
import test.JD.JDSetupPackage;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.Date;
 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
 
import java.sql.Statement;
import java.util.Calendar;
import java.util.Hashtable; import java.util.Vector;
import test.JD.JDSerializeFile;
import java.sql.SQLException;



/**
Testcase JDPSSetDate.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setDate()
</ul>
**/
public class JDPSSetDate
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDPSSetDate";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDPSTest.main(newArgs); 
   }



    // Constants.
    private static final String PACKAGE             = "JDPSST";



    // Private data.
    private Statement           statement_;



/**
Constructor.
**/
    public JDPSSetDate (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDPSSetDate",
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
            
            ;
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
setDate() - Should throw exception when the prepared
statement is closed.
**/
    public void Var001()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_DATE) VALUES (?)");
            ps.close ();
            ps.setDate (1, new Date (235445454));
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        
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
setDate() - Should throw exception when an invalid index is
specified.
**/
    public void Var002()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
            ps.setDate (100, new Date (3454644));
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        
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
setDate() - Should throw exception when index is 0.
**/
    public void Var003()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
            ps.setDate (0, new Date (25475744));
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        
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
setDate() - Should throw exception when index is -1.
**/
    public void Var004()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
            ps.setDate (0, new Date (26565665));
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        
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
setDate() - Should throw exception when the value is null.
**/
    public void Var005()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_DATE) VALUES (?)");
            ps.setDate (1, null);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DATE FROM " + pstestSet.getName());
            rs.next ();
            Date check = rs.getDate (1);
            boolean wn = rs.wasNull ();
            rs.close ();

            assertCondition ((check == null) && (wn == true));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        
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
setDate() - Should work with a valid parameter index
greater than 1.
**/
    public void Var006()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        try {
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_KEY, C_DATE) VALUES (?, ?)");
            ps.setString (1, "Hola");
            Date t = new Date (-2243544);
            ps.setDate (2, t);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DATE FROM " + pstestSet.getName());
            rs.next ();
            Date check = rs.getDate (1);
            rs.close ();

            assertCondition (check.toString ().equals (t.toString ()));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
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
setDate() - Should throw exception when the calendar is null.
**/
    public void Var007()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc20 ()) {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_DATE) VALUES (?)");
            ps.setDate (1, new Date (System.currentTimeMillis()), null);
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
setDate() - Should throw exception when the parameter is
not an input parameter.
**/
    public void Var008()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC permits setting of an output parameter");
        return; 
      } 
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "CALL " + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
            ps.setDate (2, new Date (System.currentTimeMillis()));
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }


    public void testSetFailed(String columnName, Date inDate) {
    JDSerializeFile pstestSet = null;
	try {
      pstestSet = JDPSTest.getPstestSet(connection_);
	    PreparedStatement ps = connection_.prepareStatement (
								 "INSERT INTO " + pstestSet.getName()
								 + " ("+columnName+") VALUES (?)");
	    ps.setDate (1, inDate);
	    ps.executeUpdate(); ps.close ();
	    failed ("Didn't throw SQLException");
	}
	catch (Exception e) {
	    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	
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
setDate() - Set a SMALLINT parameter.
**/
    public void Var009()
    {
	testSetFailed("C_SMALLINT", new Date (System.currentTimeMillis()));
    }



/**
setDate() - Set a INTEGER parameter.
**/
    public void Var010()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_INTEGER) VALUES (?)");
            ps.setDate (1, new Date (System.currentTimeMillis()));
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        
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
setDate() - Set a REAL parameter.
**/
    public void Var011()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_REAL) VALUES (?)");
            ps.setDate (1, new Date (System.currentTimeMillis()));
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        
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
setDate() - Set a FLOAT parameter.
**/
    public void Var012()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_FLOAT) VALUES (?)");
            ps.setDate (1, new Date (System.currentTimeMillis()));
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        
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
setDate() - Set a DOUBLE parameter.
**/
    public void Var013()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_DOUBLE) VALUES (?)");
            ps.setDate (1, new Date (System.currentTimeMillis()));
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        
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
setDate() - Set a DECIMAL parameter.
**/
    public void Var014()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_DECIMAL_105) VALUES (?)");
            ps.setDate (1, new Date (System.currentTimeMillis()));
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        
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
setDate() - Set a NUMERIC parameter.
**/
    public void Var015()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_NUMERIC_50) VALUES (?)");
            ps.setDate (1, new Date (System.currentTimeMillis()));
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        
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
setDate() - Set a CHAR(50) parameter.
**/
    public void Var016()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_CHAR_50) VALUES (?)");
            Date t = new Date (12324352);
            ps.setDate (1, t);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_CHAR_50 FROM " + pstestSet.getName());
            rs.next ();
            Date check = rs.getDate (1);
            rs.close ();

            assertCondition (check.toString ().equals (t.toString ()));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        
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
setDate() - Set a VARCHAR(50) parameter.
**/
    public void Var017()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_VARCHAR_50) VALUES (?)");
            Date t = new Date (325234532);
            ps.setDate (1, t);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + pstestSet.getName());
            rs.next ();
            Date check = rs.getDate (1);
            rs.close ();

            assertCondition (check.toString ().equals (t.toString ()));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        
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
setDate() - Set a CLOB parameter.
**/
    public void Var018()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkLobSupport ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + pstestSet.getName()
                    + " (C_CLOB) VALUES (?)");
                ps.setDate (1, new Date (System.currentTimeMillis()));
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
setDate() - Set a DBCLOB parameter.
**/
    public void Var019()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkLobSupport ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + pstestSet.getName()
                    + " (C_DBCLOB) VALUES (?)");
                ps.setDate (1, new Date (System.currentTimeMillis()));
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
setDate() - Set a BINARY parameter.
**/
    public void Var020()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_BINARY_20) VALUES (?)");
            ps.setDate (1, new Date (System.currentTimeMillis()));
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        
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
setDate() - Set a VARBINARY parameter.
**/
    public void Var021()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_VARBINARY_20) VALUES (?)");
            ps.setDate (1, new Date (System.currentTimeMillis()));
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        
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
setDate() - Set a BLOB parameter.
**/
    public void Var022()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_BLOB) VALUES (?)");
            ps.setDate (1, new Date (System.currentTimeMillis()));
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        
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
setDate() - Set a DATE parameter.
**/
    public void Var023()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc20 ()) {
        try {
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_DATE) VALUES (?)");
            Date t = new Date (34894589);
            ps.setDate (1, t, Calendar.getInstance ());
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DATE FROM " + pstestSet.getName());
            rs.next ();
            Date check = rs.getDate (1);
            rs.close ();

            assertCondition (check.toString ().equals (t.toString ()));
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
setDate() - Set a DATE parameter, with a calendar specified.
**/
    public void Var024()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        if (checkJdbc20 ()) {
        try {
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_DATE) VALUES (?)");
            Date t = new Date (1035645632);
            ps.setDate (1, t, Calendar.getInstance ());
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DATE FROM " + pstestSet.getName());
            rs.next ();
            Date check = rs.getDate (1);
            rs.close ();

            assertCondition (check.toString ().equals (t.toString ()));
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
setDate() - Set a TIME parameter.
**/
    public void Var025()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_TIME) VALUES (?)");
            ps.setDate (1, new Date (2223454));
            ps.executeUpdate(); ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
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
setDate() - Set a TIMESTAMP parameter.
**/
    public void Var026()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        if (checkJdbc20 ()) {
        try {
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_TIMESTAMP) VALUES (?)");
            Date t = new Date (348345589);
            ps.setDate (1, t, Calendar.getInstance ());
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_TIMESTAMP FROM " + pstestSet.getName());
            rs.next ();
            Date check = rs.getDate (1);
            rs.close ();

            assertCondition (check.toString ().equals (t.toString ()));
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
setDate() - Set a DATALINK parameter.
**/
    public void Var027()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        if ((getDriver () == JDTestDriver.DRIVER_NATIVE) && (getJdbcLevel() <= 2)) {
            notApplicable ("Native driver pre-JDBC 3.0");
            return;
        }
        if (checkDatalinkSupport ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + pstestSet.getName()
                    + " (C_DATALINK) VALUES (?)");
                ps.setDate (1, new Date (System.currentTimeMillis()));
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
setDate() - Set a DISTINCT parameter.
**/
    public void Var028()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        if (checkLobSupport ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + pstestSet.getName()
                    + " (C_DISTINCT) VALUES (?)");
                ps.setDate (1, new Date (System.currentTimeMillis()));
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
setDate() - Set a BIGINT parameter.
**/
    public void Var029()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        if (checkBigintSupport()) {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_BIGINT) VALUES (?)");
            ps.setDate (1, new Date (System.currentTimeMillis()));
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
setDate() - Set a parameter in a statement that comes from the
package cache.

SQL400 - the test below was changed for the native driver to test the
         dates the same way all the other tests do.  By string comparing
         instead of using the equals method. 
**/
    public void Var030()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        try {
            String insert = "INSERT INTO " + pstestSet.getName()
                + " (C_DATE) VALUES (?)";
            
            if (isToolboxDriver())
               JDSetupPackage.prime (systemObject_, PACKAGE, 
                   JDPSTest.COLLECTION, insert);
            else
               JDSetupPackage.prime (systemObject_, encryptedPassword_, PACKAGE, 
                   JDPSTest.COLLECTION, insert, "", getDriver());

            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            Connection c2 = testDriver_.getConnection (baseURL_
                + ";extended dynamic=true;package=" + PACKAGE
                + ";package library=" + JDPSTest.COLLECTION
                + ";package cache=true", userId_, encryptedPassword_);
            PreparedStatement ps = c2.prepareStatement (insert);
            Date t = new Date (1231200000);

            ps.setDate (1, t);
            ps.executeUpdate ();
            ps.close ();
            c2.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DATE FROM " + pstestSet.getName());
            rs.next ();
            Date check = rs.getDate (1);
            rs.close ();            

            //if (isToolboxDriver() &&
            //    (JDTestDriver.OSName_.indexOf("OS/400") < 0))  //@F1A
            //   assertCondition (check.equals (t));
           // else //if native or Toolbox on 400
            //simplify to tostring for all
               assertCondition (check.toString ().equals (t.toString ()));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
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
    setDate() - Set a DECFLOAT16 parameter.
    **/
        public void Var031()
        {
            if (checkDecFloatSupport()) {
              JDSerializeFile pstestSetdfp = null;
              try {
                pstestSetdfp = JDPSTest.getPstestSetdfp16(connection_);
                String tablename16 = pstestSetdfp.getName();
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + tablename16
                    + "   VALUES (?)");
                ps.setDate (1, new Date (System.currentTimeMillis()));
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

        /**
        setDate() - Set a DECFLOAT34 parameter.
        **/
            public void Var032()
            {
                if (checkDecFloatSupport()) {
                  JDSerializeFile pstestSetdfp = null;
                  try {
                    pstestSetdfp = JDPSTest.getPstestSetdfp34(connection_);
                    String tablename34 = pstestSetdfp.getName();
                    PreparedStatement ps = connection_.prepareStatement (
                        "INSERT INTO " + tablename34 
                        + "   VALUES (?)");
                    ps.setDate (1, new Date (System.currentTimeMillis()));
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


    /**
    setDate() - Set a SQLXML parameter.
    **/
   public void Var033() {
    if (checkXmlSupport()) {
      JDSerializeFile pstestSetxml = null;
      try {
        pstestSetxml = JDPSTest.getSerializeFile(connection_, JDPSTest.SETXML);
        String tablename = pstestSetxml.getName(); 
        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + tablename + " VALUES (?)");
        try {
          ps.setDate(1, new Date(System.currentTimeMillis()));
          ps.executeUpdate();
          ps.close();
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      } catch (Exception e) {
        failed(e, "Unexpected exception");
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
setDate() - Set a BOOLEAN parameter.
**/
    public void Var034()
    {
	if (checkBooleanSupport()) { 
	testSetFailed("C_BOOLEAN", new Date ( System.currentTimeMillis()));
	}
    }




}



