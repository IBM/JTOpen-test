///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSSetObject2.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.PS;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDLobTest;
import test.JDPSTest;
import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JDTestcase;
import test.JD.JDSerializeFile;



/**
Testcase JDPSSetObject2.  This tests the following method
of the JDBC PreparedStatement class (2 parameters):

<ul>
<li>setObject(int,Object)
</ul>
**/
public class JDPSSetObject2
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDPSSetObject2";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDPSTest.main(newArgs); 
   }



    // Private data.
    private Statement           statement_;
    private Connection          connectionDateTime_;
    private Statement           statementDateTime_;

    private Connection          connectionCommaSeparator_;


/**
Constructor.
**/
    public JDPSSetObject2 (AS400 systemObject,
                           Hashtable<String,Vector<String>> namesAndVars,
                           int runMode,
                           FileOutputStream fileOutputStream,
                           
                           String password)
    {
        super (systemObject, "JDPSSetObject2",
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
        connectionCommaSeparator_ = testDriver_.getConnection (url+";decimal separator=,");
	connectionDateTime_ = testDriver_.getConnection (url+";date format=jis;time format=jis",systemObject_.getUserId(), encryptedPassword_);
        statement_ = connection_.createStatement ();
	statementDateTime_ = connectionDateTime_.createStatement ();
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

        connectionCommaSeparator_.close ();
	connectionDateTime_.close(); 
    }



/**
setObject() - Should throw exception when the prepared
statement is closed.
**/
    public void Var001()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_NUMERIC_105) VALUES (?)");
            ps.close ();
            ps.setObject (1, new BigDecimal (2.3));
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
setObject() - Should throw exception when an invalid index is
specified.
**/
    public void Var002()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
            ps.setObject (100, Integer.valueOf(4));
            ps.close ();
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
setObject() - Should throw exception when index is 0.
**/
    public void Var003()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
            ps.setObject (0, "Hi Mom");
            ps.close ();
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
setObject() - Should throw exception when index is -1.
**/
    public void Var004()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
            ps.setObject (-1, "Yo Dad");
            ps.close ();
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
setObject() - Should work with a valid parameter index
greater than 1.
**/
    @SuppressWarnings("deprecation")
    public void Var005()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_KEY, C_NUMERIC_105) VALUES (?, ?)");
            ps.setString (1, "Test");
            ps.setObject (2, new BigDecimal ("4.3"));
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_105 FROM " + pstestSet.getName());
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 1);
            rs.close ();

            assertCondition (check.doubleValue() == 4.3);
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
setObject() - Should set to SQL NULL when the object is null.
**/
    @SuppressWarnings("deprecation")
    public void Var006()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_NUMERIC_105) VALUES (?)");
            ps.setObject (1, null);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_105 FROM " + pstestSet.getName());
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 0);
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
setObject() - Should throw exception when the parameter is
not an input parameter.
**/
    public void Var007()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                                                                "CALL " + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
            ps.setObject (2, Integer.valueOf(3));
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setObject() - Should throw exception when the parameter is
not anything close to being a JDBC-style type.
**/
    @SuppressWarnings("rawtypes")
    public void Var008() {
      JDSerializeFile pstestSet = null;
      PreparedStatement ps = null;
      try {
        pstestSet = JDPSTest.getPstestSet(connection_);
        ps = connection_.prepareStatement("INSERT INTO " + pstestSet.getName() + " (C_SMALLINT) VALUES (?)");
        ps.setObject(1, new Hashtable());
        // For some drivers, errors not reflected until executeUpdate
        ps.executeUpdate();

        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");

      } finally {

        try {
          if (pstestSet != null)
            pstestSet.close();
          if (ps != null)
            ps.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }

      }
    }


/**
setObject() - Set a SMALLINT parameter.
**/
    public void Var009()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_SMALLINT) VALUES (?)");
            ps.setObject (1, Short.valueOf((short) -33));
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_SMALLINT FROM " + pstestSet.getName());
            rs.next ();
            short check = rs.getShort (1);
            rs.close ();

            assertCondition (check == -33);
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
setObject() - Set a SMALLINT parameter, when the data gets truncated.
**/
    public void Var010()
    {
    JDSerializeFile pstestSet = null;
    PreparedStatement ps = null; 
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_SMALLINT) VALUES (?)");
            ps.setObject (1, new BigDecimal (-24433323.0));
	    ps.executeUpdate(); 
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {

		boolean success = e.getMessage().indexOf("Data type mismatch") != -1;
		if (success) { 
		    assertCondition(true); 
		} else { 
		    failed(e, "Expected data type mismatch"); 
		} 

        
    } finally {
        try {
          if (pstestSet != null) pstestSet.close();
          if (ps != null) ps.close(); 
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
  }



/**
setObject() - Set a SMALLINT parameter, when the data contains a fraction. 
This is ok.
**/
    public void Var011()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_SMALLINT) VALUES (?)");
            ps.setObject (1, Float.valueOf(-33.3f));
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_SMALLINT FROM " + pstestSet.getName());
            rs.next ();
            short check = rs.getShort (1);
            rs.close ();

            assertCondition (check == -33);
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
   * setObject() - Set a SMALLINT parameter, when the object is the wrong type.
   **/
  public void Var012() {
    JDSerializeFile pstestSet = null;
    PreparedStatement ps = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
      statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

      ps = connection_.prepareStatement("INSERT INTO " + pstestSet.getName() + " (C_SMALLINT) VALUES (?)");
      ps.setObject(1, Time.valueOf("04:15:33"));
      ps.executeUpdate();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");

    } finally {

      try {
        if (pstestSet != null)
          pstestSet.close();
        if (ps != null)
          ps.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

  }


/**
setObject() - Set an INTEGER parameter.
**/
    public void Var013()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_INTEGER) VALUES (?)");
            ps.setObject (1, Integer.valueOf(9595));
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_INTEGER FROM " + pstestSet.getName());
            rs.next ();
            int check = rs.getInt (1);
            rs.close ();

            assertCondition (check == 9595);
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
setObject() - Set a INTEGER parameter, when the data gets truncated.
**/
    public void Var014()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_INTEGER) VALUES (?)");
            ps.setObject (1, new BigDecimal (-24475577533323.0));
	    ps.executeUpdate(); 

            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
		boolean success = e.getMessage().indexOf("Data type mismatch") != -1;
		if (success) { 
		    assertCondition(true); 
		} else { 
		    failed(e, "Expected data type mismatch"); 
		} 

        
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
setObject() - Set a INTEGER parameter, when the data contains a fraction. 
This is ok.
**/
    public void Var015()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_INTEGER) VALUES (?)");
            ps.setObject (1, Float.valueOf(-33.3f));
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_INTEGER FROM " + pstestSet.getName());
            rs.next ();
            int check = rs.getInt (1);
            rs.close ();

            assertCondition (check == -33);
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
setObject() - Set an INTEGER parameter, when the object is the wrong type.
**/
    public void Var016()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_INTEGER) VALUES (?)");
            ps.setObject (1, new Date (1234));
	    ps.executeUpdate(); 

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
setObject() - Set an REAL parameter.
**/
    public void Var017()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_REAL) VALUES (?)");
            ps.setObject (1, Float.valueOf(4.325f));
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_REAL FROM " + pstestSet.getName());
            rs.next ();
            float check = rs.getFloat (1);
            rs.close ();

            assertCondition (check == 4.325f);
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
setObject() - Set a REAL parameter, when the object is the wrong type.
**/
    public void Var018()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_REAL) VALUES (?)");
            ps.setObject (1, "Peace");
	    ps.executeUpdate(); 

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
setObject() - Set an FLOAT parameter.
**/
    public void Var019()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_FLOAT) VALUES (?)");
            ps.setObject (1, Float.valueOf(-34.2f));
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_FLOAT FROM " + pstestSet.getName());
            rs.next ();
            float check = rs.getFloat (1);
            rs.close ();

            assertCondition (check == -34.2f);
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
setObject() - Set a FLOAT parameter, when the object is the wrong type.
**/
    public void Var020()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_FLOAT) VALUES (?)");
            ps.setObject (1, "This is not a test");
	    ps.executeUpdate(); 

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
setObject() - Set an DOUBLE parameter.
**/
    public void Var021()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_DOUBLE) VALUES (?)");
            ps.setObject (1, Double.valueOf(3.14159));
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DOUBLE FROM " + pstestSet.getName());
            rs.next ();
            double check = rs.getDouble (1);
            rs.close ();

            assertCondition (check == 3.14159);
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
setObject() - Set a DOUBLE parameter, when the object is the wrong type.
**/
    public void Var022()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_DOUBLE) VALUES (?)");
            ps.setObject (1, new Timestamp (34422343));
	    ps.executeUpdate(); 

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
setObject() - Set an DECIMAL parameter.
**/
    @SuppressWarnings("deprecation")
    public void Var023()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_DECIMAL_105) VALUES (?)");
            ps.setObject (1, new BigDecimal ("322.344"));
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DECIMAL_105 FROM " + pstestSet.getName());
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 3);
            rs.close ();

            assertCondition (check.doubleValue () == 322.344);
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
setObject() - Set an DECIMAL parameter, when the data gets truncated.
**/
    public void Var024()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_DECIMAL_50) VALUES (?)");
            ps.setObject (1, new BigDecimal (-332232.2344));
	    ps.executeUpdate(); 

            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
		if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
		    assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation now in toolbox");

		} else { 

		    DataTruncation dt = (DataTruncation)e;
		    assertCondition ((dt.getIndex() == 1)
				     && (dt.getParameter() == true)
				     && (dt.getRead() == false)
				     && (dt.getTransferSize() == 5));
		}
        
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
setObject() - Set an DECIMAL parameter, when the fraction gets truncated.  This
does not cause a data truncation exception.
**/
    @SuppressWarnings("deprecation")
    public void Var025()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_DECIMAL_50) VALUES (?)");
            ps.setObject (1, new BigDecimal ("322.344"));
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DECIMAL_50 FROM " + pstestSet.getName());
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 0);
            rs.close ();

            assertCondition (check.doubleValue () == 322);
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
setObject() - Set a DECIMAL parameter, when the object is the wrong type.
**/
    public void Var026()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_DECIMAL_105) VALUES (?)");
            ps.setObject (1, "Friends");
	    ps.executeUpdate(); 

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
setObject() - Set a NUMERIC parameter.
**/
    @SuppressWarnings("deprecation")
    public void Var027()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_NUMERIC_105) VALUES (?)");
            ps.setObject (1, new BigDecimal ("-9999.123"));
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_105 FROM " + pstestSet.getName());
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 3);
            rs.close ();

            assertCondition (check.doubleValue () == -9999.123);
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
setObject() - Set a NUMERIC parameter, when the data gets truncated.
**/
    public void Var028()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_NUMERIC_50) VALUES (?)");
            ps.setObject (1, new BigDecimal (-24433323.2344));
	    ps.executeUpdate(); 

            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
		if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
		    assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation now in toolbox");

		} else { 

		    DataTruncation dt = (DataTruncation)e;
		    assertCondition ((dt.getIndex() == 1)
				     && (dt.getParameter() == true)
				     && (dt.getRead() == false)
				     && (dt.getTransferSize() == 5));
		}
        
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
setObject() - Set an NUMERIC parameter, when the fraction gets truncated.  This
does not cause a data truncation exception.
**/
    @SuppressWarnings("deprecation")
    public void Var029()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_NUMERIC_50) VALUES (?)");
            ps.setObject (1, new BigDecimal ("322.3443846"));
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_50 FROM " + pstestSet.getName());
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 0);
            rs.close ();

            assertCondition (check.doubleValue () == 322);
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
setObject() - Set a NUMERIC parameter, when the object is the wrong type.
**/
    public void Var030()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_NUMERIC_105) VALUES (?)");
            ps.setObject (1, new Time (32354));
	    ps.executeUpdate(); 

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
setObject() - Set a CHAR(50) parameter.
**/
    public void Var031()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_CHAR_50) VALUES (?)");
            ps.setObject (1, "Nature");
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_CHAR_50 FROM " + pstestSet.getName());
            rs.next ();
            String check = rs.getString (1);
            rs.close ();

            assertCondition (check.equals ("Nature                                            "));
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
setObject() - Set a CHAR  parameter, when the data gets truncated.
**/
    public void Var032()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_CHAR_1) VALUES (?)");
            ps.setObject (1, "Sky");
	    ps.executeUpdate(); 

            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.DataTruncation");
        
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
setObject() - Set a CHAR parameter, when the object is the wrong type.
**/
    public void Var033()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_CHAR_50) VALUES (?)");
            ps.setObject (1, new byte[] { (byte) -12});
	    ps.executeUpdate(); 

            failed ("Didn't throw SQLException when setting char from byte array");
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
setObject() - Set an VARCHAR parameter.
**/
    public void Var034()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_VARCHAR_50) VALUES (?)");
            ps.setObject (1, "Mountains");
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + pstestSet.getName());
            rs.next ();
            String check = rs.getString (1);
            rs.close ();

            assertCondition (check.equals ("Mountains"));
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
setObject() - Set a VARCHAR  parameter, when the data gets truncated.
**/
    public void Var035()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_VARCHAR_50) VALUES (?)");
            ps.setObject (1, "JDBC testing is sure fun, especially when you get to test method after method after method, ...");
	    ps.executeUpdate(); 

            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.DataTruncation");
        
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
setObject() - Set a VARCHAR parameter, when the object is the wrong type.
**/
    public void Var036()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_VARCHAR_50) VALUES (?)");
            ps.setObject (1, new int[0]);
	    ps.executeUpdate(); 

            failed ("Didn't throw SQLException setting varchar from empy type array");
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
setObject() - Set a CLOB parameter.
**/
    public void Var037()
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
                    ps.setObject (1, new JDLobTest.JDTestClob ("Horizon"));
                    ps.executeUpdate ();
                    ps.close ();

                    ResultSet rs = statement_.executeQuery ("SELECT C_CLOB FROM " + pstestSet.getName());
                    rs.next ();
                    Clob check = rs.getClob (1);
                    // rs.close ();                                                                     // @F1D

                    assertCondition (check.getSubString (1, (int) check.length ()).equals ("Horizon")); // @D1C
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
setObject() - Set a CLOB parameter, when the object is the wrong type.
**/
    public void Var038()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkLobSupport ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + pstestSet.getName()
                                                                    + " (C_CLOB) VALUES (?)");
                ps.setObject (1, Short.valueOf((short) 3342));
	    ps.executeUpdate(); 

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
setObject() - Set a DBCLOB parameter.
**/
    public void Var039()
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
                    ps.setObject (1, new JDLobTest.JDTestClob ("Sunset"));
                    ps.executeUpdate ();
                    ps.close ();
        
                    ResultSet rs = statement_.executeQuery ("SELECT C_DBCLOB FROM " + pstestSet.getName());
                    rs.next ();
                    Clob check = rs.getClob (1);
                    rs.close ();
        
                    assertCondition (check.getSubString (1, (int) check.length ()).equals ("Sunset"));   // @D1C
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
setObject() - Set a DBCLOB parameter, when the object is the wrong type.
**/
    public void Var040()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkLobSupport ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + pstestSet.getName()
                                                                    + " (C_DBCLOB) VALUES (?)");
                ps.setObject (1, Long.valueOf(34242));
	    ps.executeUpdate(); 

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
setObject() - Set a BINARY parameter.
**/
    public void Var041()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_BINARY_20) VALUES (?)");
            byte[] b = { (byte) 32, (byte) 0, (byte) -1, (byte) -11};
            ps.setObject (1, b);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_BINARY_20 FROM " + pstestSet.getName());
            rs.next ();
            byte[] check = rs.getBytes (1);
            rs.close ();

            byte[] b2 = new byte[20];
            System.arraycopy (b, 0, b2, 0, b.length);
            assertCondition (areEqual (check, b2));
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
setObject() - Set a BINARY parameter, when data gets truncated.
**/
    public void Var042()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_BINARY_20) VALUES (?)");
            byte[] b = { (byte) 32, (byte) 0, (byte) -1, (byte) -11, (byte) 45,
                (byte) 32, (byte) 12, (byte) 123, (byte) 0,
                (byte) 32, (byte) 12, (byte) 123, (byte) 0,
                (byte) 32, (byte) 12, (byte) 123, (byte) 0,
                (byte) 32, (byte) 12, (byte) 123, (byte) 0,
                (byte) 32, (byte) 12, (byte) 123, (byte) 0
            };
            ps.setObject (1, b);
	    ps.executeUpdate(); 

            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.DataTruncation");
        
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
setObject() - Set a BINARY parameter, when the object is the wrong type.
**/
    public void Var043()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        try {
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_VARBINARY_20) VALUES (?)");
            ps.setObject (1, new BigDecimal (34.23));
	    ps.executeUpdate(); 

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
setObject() - Set a VARBINARY parameter.
**/
    public void Var044()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_VARBINARY_20) VALUES (?)");
            byte[] b = { (byte) -13, (byte) 32, (byte) 0, (byte) -1, (byte) -11};
            ps.setObject (1, b);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_VARBINARY_20 FROM " + pstestSet.getName());
            rs.next ();
            byte[] check = rs.getBytes (1);
            rs.close ();

            assertCondition (areEqual (check, b));
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
setObject() - Set a VARBINARY parameter, when data gets truncated.
**/
    public void Var045()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_VARBINARY_20) VALUES (?)");
            byte[] b = { (byte) 32, (byte) 0, (byte) -1, (byte) -11, (byte) 45,
                (byte) 32, (byte) 12, (byte) 123, (byte) 0,
                (byte) 32, (byte) 12, (byte) 123, (byte) 0,
                (byte) 32, (byte) 12, (byte) 123, (byte) 1,
                (byte) 32, (byte) 12, (byte) 123, (byte) 0,
                (byte) 32, (byte) 12, (byte) 123, (byte) 100
            };
            ps.setObject (1, b);
	    ps.executeUpdate(); 

            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.DataTruncation");
        
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
setObject() - Set a VARBINARY parameter, when the object is the wrong type.
**/
    public void Var046()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_VARBINARY_20) VALUES (?)");
            ps.setObject (1, Double.valueOf(34.23));
	    ps.executeUpdate(); 

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
setObject() - Set a BLOB parameter.
**/
    public void Var047()
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
                                                                        + " (C_BLOB) VALUES (?)");
                    byte[] b = new byte[] { (byte) 12, (byte) 94};
                    ps.setObject (1, new JDLobTest.JDTestBlob (b));
                    ps.executeUpdate ();
                    ps.close ();

                    ResultSet rs = statement_.executeQuery ("SELECT C_BLOB FROM " + pstestSet.getName());
                    rs.next ();
                    Blob check = rs.getBlob (1);
                    // rs.close ();                                                           // @F1D

                    assertCondition (areEqual (check.getBytes (1, (int) check.length ()), b)); // @D1C
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
setObject() - Set a BLOB parameter, when the object is the wrong type.
**/
    public void Var048()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkLobSupport ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + pstestSet.getName()
                                                                    + " (C_BLOB) VALUES (?)");
                ps.setObject (1, new Date (3424));
	    ps.executeUpdate(); 

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
setObject() - Set a DATE parameter.
**/
    public void Var049()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        try {
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_DATE) VALUES (?)");
            Date d = new Date (342349439);
            ps.setObject (1, d);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DATE FROM " + pstestSet.getName());
            rs.next ();
            Date check = rs.getDate (1);
            rs.close ();

            assertCondition (d.toString ().equals (check.toString ()));
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
setObject() - Set a DATE parameter, when the object is the wrong type.
**/
    public void Var050()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        try {
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_DATE) VALUES (?)");
            ps.setObject (1, "This is only a test.");
	    ps.executeUpdate(); 

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
setObject() - Set a TIME parameter.
**/
    public void Var051()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        try {
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_TIME) VALUES (?)");
            Time t = new Time (33333);
            ps.setObject (1, t);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_TIME FROM " + pstestSet.getName());
            rs.next ();
            Time check = rs.getTime (1);
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
setObject() - Set a TIME parameter, when the object is the wrong type.
**/
    public void Var052()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        try {
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_TIME) VALUES (?)");
            ps.setObject (1, Float.valueOf(4.332f));
	    ps.executeUpdate(); 

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
setObject() - Set a TIMESTAMP parameter.
**/
    public void Var053()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        try {
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_TIMESTAMP) VALUES (?)");              
            Timestamp ts = new Timestamp (343452230);
            ps.setObject (1, ts);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_TIMESTAMP FROM " + pstestSet.getName());
            rs.next ();
            Timestamp check = rs.getTimestamp (1);
            rs.close ();

            assertCondition (check.toString ().equals (ts.toString ()));
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
setObject() - Set a TIMESTAMP parameter, when the object is the wrong type.
**/
    public void Var054()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        try {
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_TIMESTAMP) VALUES (?)");
            ps.setObject (1, new BigDecimal (34.2));
	    ps.executeUpdate(); 

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
setObject() - Set a DATALINK parameter.
**/
    public void Var055()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        if (checkLobSupport ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + pstestSet.getName()
                                                                    + " (C_DATALINK) VALUES (DLVALUE( CAST(? AS VARCHAR(50))))");
                ps.setObject (1, "http://www.ibm.com/as400/toolbox.html");
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_DATALINK FROM " + pstestSet.getName());
                rs.next ();
                String check = rs.getString (1);
                rs.close ();

                assertCondition (check.equalsIgnoreCase("http://www.ibm.com/as400/toolbox.html"));
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
setObject() - Set a DATALINK parameter, when the object is the wrong type.
**/
    public void Var056()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        if ((getDriver () == JDTestDriver.DRIVER_NATIVE) && (getJdbcLevel() <= 2)) {
            notApplicable ("Native driver pre-JDBC 3.0");
            return;
        }
        if (checkLobSupport ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + pstestSet.getName()
                                                                    + " (C_DATALINK) VALUES (?)");
                ps.setObject (1, Time.valueOf("07:42:00")); 
                
	    ps.executeUpdate(); 

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
setObject() - Set a DISTINCT parameter.
**/
    public void Var057()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        if (checkLobSupport ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + pstestSet.getName()
                                                                    + " (C_DISTINCT) VALUES (?)");
                ps.setObject (1, Integer.valueOf(432));
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_DISTINCT FROM " + pstestSet.getName());
                rs.next ();
                int check = rs.getInt (1);
                rs.close ();

                assertCondition (check == 432);
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
setObject() - Set a DATALINK parameter, when the object is the wrong type.
**/
    public void Var058()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        if ((getDriver () == JDTestDriver.DRIVER_NATIVE) && (getJdbcLevel() <= 2)) {
            notApplicable ("Native driver pre-JDBC 3.0");
            return;
        }
        if (checkLobSupport ()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + pstestSet.getName()
                                                                    + " (C_DATALINK) VALUES (?)");
                ps.setObject (1, Integer.valueOf(-4));
	    ps.executeUpdate(); 

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
setObject() - Set an BIGINT parameter.
**/
    public void Var059()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        if (checkBigintSupport()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + pstestSet.getName()
                                                                    + " (C_BIGINT) VALUES (?)");
                ps.setObject (1, Long.valueOf(-322123495953l));
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_BIGINT FROM " + pstestSet.getName());
                rs.next ();
                long check = rs.getLong (1);
                rs.close ();

                assertCondition (check == -322123495953l);
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
setFloat() - Set an BIGINT parameter, when the value is too big.  This will
cause a data truncation exception.
**/
    public void Var060()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
	if (checkBigintSupport()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + pstestSet.getName()
								     + " (C_BIGINT) VALUES (?)");
		ps.setObject (1, Float.valueOf(-92233720368547758099.0f));
		ps.executeUpdate(); 

		failed ("Didn't throw SQLException");
	    }
	    catch (Exception e) {
		boolean success = e.getMessage().indexOf("Data type mismatch") != -1;
		if (success) { 
		    assertCondition(true); 
		} else { 
		    failed(e, "Expected data type mismatch -- updated 7/1/2014 to match other data types"); 
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
setObject() - Set a BIGINT parameter, when the data contains a fraction. 
This is ok.
**/
    public void Var061()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        try {
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_BIGINT) VALUES (?)");
            ps.setObject (1, Float.valueOf(-33.3f));
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_BIGINT FROM " + pstestSet.getName());
            rs.next ();
            int check = rs.getInt (1);
            rs.close ();

            assertCondition (check == -33);
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
setObject() - Set a BIGINT parameter, when the object is the wrong type.
**/
    public void Var062()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        if (checkBigintSupport()) {
            try {
                statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + pstestSet.getName()
                                                                    + " (C_BIGINT) VALUES (?)");
                ps.setObject (1, new Timestamp (1234));
	    ps.executeUpdate(); 

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
setObject() - Set an DECFLOAT(16) parameter.
**/
    public void Var063()
    {

	String added = " -- DECFLOAT(16) test added by native driver 05/09/2007"; 
	if (checkDecFloatSupport()) { 


	         JDSerializeFile pstestSetdfp = null;
	         try {
	           pstestSetdfp = JDPSTest.getPstestSetdfp16(connection_);
	           String tablename16=pstestSetdfp.getName();


		statement_.executeUpdate ("DELETE FROM " + tablename16);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + tablename16
								     + " VALUES (?)");
		ps.setObject (1, new BigDecimal ("322.344"));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT * FROM " + tablename16);
		rs.next ();
		BigDecimal check = rs.getBigDecimal (1);
		rs.close ();

		assertCondition (check.doubleValue () == 322.344, "Got "+check.doubleValue() +" expected 322.34 "+added);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception "+added);
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
setObject() - Set an DECFLOAT(16) parameter, when the data gets truncated.
**/
    public void Var064()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
String added = " -- DECFLOAT(16) test added by native driver 05/09/2007"; 
	if (checkDecFloatSupport()) { 

	         JDSerializeFile pstestSetdfp = null;
	         try {
	           pstestSetdfp = JDPSTest.getPstestSetdfp16(connection_);
	           String tablename16=pstestSetdfp.getName();
		statement_.executeUpdate ("DELETE FROM " + tablename16);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + tablename16
								     + " VALUES (?)");

		ps.setObject (1, new BigDecimal ("9.999999999999999E800"));
                ps.executeUpdate(); 
                ResultSet rs = statement_.executeQuery ("SELECT * FROM " + tablename16);
                rs.next ();
                BigDecimal check = rs.getBigDecimal (1);
                rs.close ();
                
		failed ("Didn't throw SQLException but got "+check+ added );
	    }
	    catch (DataTruncation dt) {
		assertCondition ((dt.getIndex() == 1)
				 && (dt.getParameter() == true)
				 && (dt.getRead() == false)
				 && (dt.getTransferSize() == 5), added);
	    } catch (SQLException e) {
		// The native driver will throw a Data Type mismatch since this cannot be expressed
		boolean success = e.getMessage().indexOf("Data type mismatch") != -1;
		if (success) { 
		    assertCondition(true); 
		} else { 
		    failed(e, "Expected data type mismatch (for native driver)"+added ); 
		} 

	    } catch (Exception e) {
		failed (e, "Unexpected Exception "+added);

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
setObject() - Set an DECFLOAT(16) parameter, when the fraction gets truncated.  This
does not cause a data truncation exception.
**/
public void Var065() {
  String added = " -- DECFLOAT(16) test added by native driver 05/09/2007";
  if (checkDecFloatSupport()) {
    JDSerializeFile pstestSetdfp = null;
    try {
      pstestSetdfp = JDPSTest.getPstestSetdfp16(connection_);
      String tablename16 = pstestSetdfp.getName();
      statement_.executeUpdate("DELETE FROM " + tablename16);

      PreparedStatement ps = connection_.prepareStatement("INSERT INTO " + tablename16 + " VALUES (?)");
      ps.setObject(1, new BigDecimal("322.34412345678901234567890"));
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_.executeQuery("SELECT * FROM " + tablename16);
      rs.next();
      BigDecimal check = rs.getBigDecimal(1);
      rs.close();

      assertCondition(check.toString().equals("322.3441234567890") || check.toString().equals("322.344123456789"),
          "Got " + check + " sb 322.3441234567890 or 322.344123456789 " + added);
    } catch (Exception e) {
      failed(e, "Unexpected Exception" + added);
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
setObject() - Set a DECFLOAT(16) parameter, when the object is the wrong type.
**/
public void Var066() {
  String added = " -- DECFLOAT(16) test added by native driver 05/09/2007";
  if (checkDecFloatSupport()) {
    PreparedStatement ps = null;
    JDSerializeFile pstestSetdfp = null;
    try {
      pstestSetdfp = JDPSTest.getPstestSetdfp16(connection_);
      String tablename16 = pstestSetdfp.getName();
      statement_.executeUpdate("DELETE FROM " + tablename16);

      ps = connection_.prepareStatement("INSERT INTO " + tablename16 + " VALUES (?)");
      ps.setObject(1, "Friends");
      ps.executeUpdate();

      failed("Didn't throw SQLException" + added);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    } finally {

      try {
        if (pstestSetdfp != null)
          pstestSetdfp.close();
        if (ps != null)
          ps.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
}

 
    /**
    setObject() - Set an DECFLOAT(34) parameter.
    **/
        public void Var067()
        {

        String added = " -- DECFLOAT(34) test added by native driver 05/09/2007"; 
        if (checkDecFloatSupport()) { 


          JDSerializeFile pstestSetdfp = null;
          try {
            pstestSetdfp = JDPSTest.getPstestSetdfp34(connection_);
            String tablename34=pstestSetdfp.getName();


                statement_.executeUpdate ("DELETE FROM " + tablename34);

                PreparedStatement ps = connection_.prepareStatement (
                                                                     "INSERT INTO " + tablename34
                                                                     + " VALUES (?)");
                ps.setObject (1, new BigDecimal ("322.344"));
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT * FROM " + tablename34);
                rs.next ();
                BigDecimal check = rs.getBigDecimal (1);
                rs.close ();

                assertCondition (check.doubleValue () == 322.344, "Got "+check.doubleValue() +" expected 322.34 "+added);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception "+added);
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
         * setObject() - Set an DECFLOAT(34) parameter, when the data gets truncated.
         **/
        public void Var068() {
          String added = " -- DECFLOAT(34) test added by native driver 05/09/2007";
          if (checkDecFloatSupport()) {
            PreparedStatement ps = null;
            JDSerializeFile pstestSetdfp = null;
            try {
              pstestSetdfp = JDPSTest.getPstestSetdfp34(connection_);
              String tablename34 = pstestSetdfp.getName();
              statement_.executeUpdate("DELETE FROM " + tablename34);

              ps = connection_.prepareStatement("INSERT INTO " + tablename34 + " VALUES (?)");

              ps.setObject(1, new BigDecimal("9.999999999999999E56642"));
              ps.executeUpdate();

              ResultSet rs = statement_.executeQuery("SELECT * FROM " + tablename34);
              rs.next();
              BigDecimal check = rs.getBigDecimal(1);
              rs.close();

              failed("Didn't throw SQLException got " + check + added);
            } catch (DataTruncation dt) {
              assertCondition((dt.getIndex() == 1) && (dt.getParameter() == true) && (dt.getRead() == false)
                  && (dt.getTransferSize() == 5), added);

            } catch (SQLException e) {
              // The native driver will throw a Data Type mismatch since this cannot be
              // expressed
              boolean success = e.getMessage().indexOf("Data type mismatch") != -1;
              if (success) {
                assertCondition(true);
              } else {
                failed(e, "Expected data type mismatch (for native driver)" + added);
              }

            } catch (Exception e) {
              failed(e, "Unexpected Exception " + added);
            } finally {
               
                try {
                  if (pstestSetdfp != null) pstestSetdfp.close();
                  if (ps != null) ps.close(); 
                } catch (SQLException e) {
                  e.printStackTrace();
                }
             

            }
          }
        }

    /**
    setObject() - Set an DECFLOAT(34) parameter, when the fraction gets truncated.  This
    does not cause a data truncation exception.
    **/
        public void Var069()
        {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
String added = " -- DECFLOAT(34) test added by native driver 05/09/2007"; 
        if (checkDecFloatSupport()) { 
          JDSerializeFile pstestSetdfp = null;
          try {
            pstestSetdfp = JDPSTest.getPstestSetdfp34(connection_);
            String tablename34=pstestSetdfp.getName();
                statement_.executeUpdate ("DELETE FROM " + tablename34);

                PreparedStatement ps = connection_.prepareStatement (
                                                                     "INSERT INTO " + tablename34
                                                                     + " VALUES (?)");
                ps.setObject (1, new BigDecimal ("322.344123456789012345678901234567890123456789088323"));
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT * FROM " + tablename34);
                rs.next ();
                BigDecimal check = rs.getBigDecimal (1);
                rs.close ();

		String expected = "322.3441234567890123456789012345679"; 
                assertCondition (check.toString().equals(expected), "Got "+check +" sb "+expected+added);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+added);
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
   * setObject() - Set a DECFLOAT(34) parameter, when the object is the wrong
   * type.
   **/
  public void Var070() {
    String added = " -- DECFLOAT(34) test added by native driver 05/09/2007";
    if (checkDecFloatSupport()) {
      JDSerializeFile pstestSetdfp = null;
      PreparedStatement ps = null;
      try {
        pstestSetdfp = JDPSTest.getPstestSetdfp34(connection_);
        String tablename34 = pstestSetdfp.getName();
        statement_.executeUpdate("DELETE FROM " + tablename34);

        ps = connection_.prepareStatement("INSERT INTO " + tablename34 + " VALUES (?)");
        ps.setObject(1, "Friends");
        ps.executeUpdate();

        failed("Didn't throw SQLException" + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      } finally {

        try {
          if (pstestSetdfp != null)
            pstestSetdfp.close();
          if (ps != null)
            ps.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }

      }
    }
  }


        public void setXmlOk(Object o, String expected) {
          String added = " -- XML test added by native driver 08/21/2009"; 
          if (checkXmlSupport()) { 
            JDSerializeFile pstestSetxml = null;
            try {
              pstestSetxml = JDPSTest.getSerializeFile(connection_, JDPSTest.SETXML);
              String tablename = pstestSetxml.getName(); 
                  statement_.executeUpdate ("DELETE FROM " + tablename);
                  PreparedStatement ps = connection_.prepareStatement (
                                                                       "INSERT INTO " + tablename
                                                                       + " VALUES (?)");
                  ps.setObject (1, o);
                  ps.executeUpdate ();
                  ps.close ();

                  ResultSet rs = statement_.executeQuery ("SELECT * FROM " + tablename);
                  rs.next ();
                  String  check = rs.getString (1);
                  rs.close ();

                  assertCondition (check.equals(expected), "Got "+check+" expected "+expected+added);
              }
              catch (Exception e) {
                  failed (e, "Unexpected Exception "+added);
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

        public void setXmlFail(Object o, String expectedException) {
          String added = " -- XML test added by native driver 08/21/2009";
          if (checkXmlSupport()) {
            JDSerializeFile pstestSetxml = null;
            PreparedStatement ps = null;
            try {
              pstestSetxml = JDPSTest.getSerializeFile(connection_, JDPSTest.SETXML);
              String tablename = pstestSetxml.getName();
              statement_.executeUpdate("DELETE FROM " + tablename);

              ps = connection_.prepareStatement("INSERT INTO " + tablename + " VALUES (?)");
              ps.setObject(1, "Friends");
              ps.executeUpdate();
              failed("Didn't throw SQLException" + added);
            } catch (Exception e) {
              String exceptionMessage = e.toString();
              if (exceptionMessage.indexOf(expectedException) >= 0) {
                assertCondition(true);

              } else {
                failed(e, "Expected exception with '" + expectedException + "'");
              }
            } finally {
              {
                try {
                  if (pstestSetxml != null)
                    pstestSetxml.close();
                  if (ps != null)
                    ps.close();
                } catch (SQLException e) {
                  e.printStackTrace();
                }
              }

            }
          }
        }



  /* Set XML column using various objects */ 
  public void Var071() { setXmlOk("<test>Var071</test>", "<test>Var071</test>");  }
  public void Var072() { setXmlFail(new BigDecimal("3.4"), "XML parsing failed"); } 
  public void Var073() { setXmlFail(Boolean.valueOf(true), "XML parsing failed"); } 
  public void Var074() { setXmlFail(Byte.valueOf((byte)1), "XML parsing failed"); } 
  public void Var075() { setXmlFail(Short.valueOf((short)1), "XML parsing failed"); } 
  public void Var076() { setXmlFail(Integer.valueOf(1), "XML parsing failed"); } 
  public void Var077() { setXmlFail(Long.valueOf(1), "XML parsing failed"); } 
  public void Var078() { setXmlFail(Float.valueOf(1), "XML parsing failed"); } 
  public void Var079() { setXmlFail(Double.valueOf(1), "XML parsing failed"); } 
  public void Var080() {
    try {
      String expected = "<bytes>Var080</bytes>";
      byte[] stuff = expected.getBytes("UTF-8");
      setXmlOk(stuff, expected);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }
  public void Var081() { setXmlFail(new java.sql.Date(103992), "XML parsing failed"); } 
  public void Var082() { setXmlFail(new java.sql.Time(103992), "XML parsing failed"); } 
  public void Var083() { setXmlFail(new java.sql.Timestamp(103992), "XML parsing failed"); } 
  public void Var084() {
    try {
      String expected = "<clob>Var080</clob>";
      Clob clob = new JDLobTest.JDTestClob (expected);
      setXmlOk(clob, expected);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }
      
  public void Var085() {
    try {
      String expected = "<blob>Var085</blob>";
      byte[] bytes = expected.getBytes("UTF-8");
      Blob blob = new JDLobTest.JDTestBlob(bytes); 
      setXmlOk(blob, expected);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }
  public void Var086() {
    if (checkJdbc40()) {
      try {
        String expected = "<sqlxml>Var086</sqlxml>";
        Object o = JDReflectionUtil.callMethod_O(connection_, "createNClob"); 
        JDReflectionUtil.callMethod_V(o, "setString", 1L, expected);
        setXmlOk(o, expected);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  public void Var087() {
    if (checkJdbc40()) {
      try {
        String expected = "<sqlxml>Var086</sqlxml>";
        Object sqlxml  =  JDReflectionUtil.callMethod_O(connection_, "createSQLXML");
        JDReflectionUtil.callMethod_V(sqlxml, "setString", expected);
        setXmlOk(sqlxml, expected);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }
  
  

/**
setObject() - Should work with a valid parameter index
greater than 1 and decimal separator is a comma.
**/
    @SuppressWarnings("deprecation")
    public void Var088()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 
        try {
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connectionCommaSeparator_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_KEY, C_NUMERIC_105) VALUES (?, ?)");
            ps.setString (1, "Test");
            ps.setObject (2, new BigDecimal ("4.3"));
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_105 FROM " + pstestSet.getName());
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 1);
            rs.close ();

            assertCondition (check.doubleValue() == 4.3, "got "+check+" sb 4.3 "+added );
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception"+added );
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
setObject() - Set an DECIMAL parameter when decimal separator is a comma.
**/
    @SuppressWarnings("deprecation")
    public void Var089()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 
        try {
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connectionCommaSeparator_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_DECIMAL_105) VALUES (?)");
            ps.setObject (1, new BigDecimal ("322.344"));
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DECIMAL_105 FROM " + pstestSet.getName());
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 3);
            rs.close ();

            assertCondition (check.doubleValue () == 322.344, added );
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception "+added);
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
setObject() - Set a NUMERIC parameter when decimal separator is a comma.
**/
    @SuppressWarnings("deprecation")
    public void Var090()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 
        try {
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connectionCommaSeparator_.prepareStatement (
                                                                "INSERT INTO " + pstestSet.getName()
                                                                + " (C_NUMERIC_105) VALUES (?)");
            ps.setObject (1, new BigDecimal ("-9999.123"));
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_105 FROM " + pstestSet.getName());
            rs.next ();
            BigDecimal check = rs.getBigDecimal (1, 3);
            rs.close ();

            assertCondition (check.doubleValue () == -9999.123, added );
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception "+added);
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
setObject() - Set an DECFLOAT(16) parameter  when decimal separator is a comma.
**/
    public void Var091()
    {


	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 
	if (checkDecFloatSupport()) { 


	         JDSerializeFile pstestSetdfp = null;
	         try {
	           pstestSetdfp = JDPSTest.getPstestSetdfp16(connection_);
	           String tablename16=pstestSetdfp.getName();


		statement_.executeUpdate ("DELETE FROM " + tablename16);

		PreparedStatement ps = connectionCommaSeparator_.prepareStatement (
								     "INSERT INTO " + tablename16
								     + " VALUES (?)");
		ps.setObject (1, new BigDecimal ("322.344"));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT * FROM " + tablename16);
		rs.next ();
		BigDecimal check = rs.getBigDecimal (1);
		rs.close ();

		assertCondition (check.doubleValue () == 322.344, "Got "+check.doubleValue() +" expected 322.34 "+added);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception "+added);
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
    setObject() - Set an DECFLOAT(34) parameter when decimal separator is a comma.
    **/
        public void Var092()
        {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);

	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 

        if (checkDecFloatSupport()) { 


          JDSerializeFile pstestSetdfp = null;
          try {
            pstestSetdfp = JDPSTest.getPstestSetdfp34(connection_);
            String tablename34=pstestSetdfp.getName();


                statement_.executeUpdate ("DELETE FROM " + tablename34);

                PreparedStatement ps = connectionCommaSeparator_.prepareStatement (
                                                                     "INSERT INTO " + tablename34
                                                                     + " VALUES (?)");
                ps.setObject (1, new BigDecimal ("322.344"));
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT * FROM " + tablename34);
                rs.next ();
                BigDecimal check = rs.getBigDecimal (1);
                rs.close ();

                assertCondition (check.doubleValue () == 322.344, "Got "+check.doubleValue() +" expected 322.34 "+added);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception "+added);
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
setParameterTest () - Set the specified parameter using an object. 
**/
    public void setParameterTest(String columnName, Object setObject,String expectedString)
    {
      JDSerializeFile pstestSet = null;
      try {
        pstestSet = JDPSTest.getPstestSet(connection_);
                statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + pstestSet.getName()
                                                                    + " ("+columnName+") VALUES (?)");
                ps.setObject (1, setObject);
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT "+columnName+" FROM " + pstestSet.getName());
                rs.next ();
                String check = rs.getString (1);
                rs.close ();

                assertCondition (expectedString.equals(check)," got "+check+" sb " + expectedString);
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
setDateTimeParameterTest () - Set the specified paramber using an object using an ISO connection. 
**/
    public void setDateTimeParameterTest(String columnName, Object setObject,String expectedString)
    {
      JDSerializeFile pstestSet = null;
      try {
        pstestSet = JDPSTest.getPstestSet(connection_);
                statementDateTime_.executeUpdate ("DELETE FROM " + pstestSet.getName());

                PreparedStatement ps = connectionDateTime_.prepareStatement (
                                                                    "INSERT INTO " + pstestSet.getName()
                                                                    + " ("+columnName+") VALUES (?)");
                ps.setObject (1, setObject);
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statementDateTime_.executeQuery ("SELECT "+columnName+" FROM " + pstestSet.getName());
                rs.next ();
                String check = rs.getString (1);
                rs.close ();

                assertCondition (expectedString.equals(check)," got "+check+" sb " + expectedString);
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

  public void Var093() {
    if (checkBooleanSupport()) {
      setParameterTest("C_BOOLEAN", Boolean.valueOf(true), "1");
    }
  }

  public void Var094() {
    if (checkBooleanSupport()) {
      setParameterTest("C_BOOLEAN", Boolean.valueOf(false), "0");
    }
  }

  public void Var095() {
    if (checkBooleanSupport()) {
      setParameterTest("C_BOOLEAN", "true", "1");
    }
  }

  public void Var096() {
    if (checkBooleanSupport()) {
      setParameterTest("C_BOOLEAN", "false", "0");
    }
  }

  public void Var097() {
    if (checkBooleanSupport()) {
      setParameterTest("C_BOOLEAN", Integer.valueOf(100), "1");
    }
  }

  public void Var098() {
    if (checkBooleanSupport()) {
      setParameterTest("C_BOOLEAN", Integer.valueOf(0), "0");
    }
  }


  /* Check valid local date mappings -- DATE / CHAR / VARCHAR / GRAPHIC / VARGRAPHIC / LONGVARGRAPHIC  */ 
  public void Var099() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDate",
            "of", 2021, 02, 04);
        setDateTimeParameterTest("C_DATE", o, "2021-02-04");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

 public void Var100() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDate",
            "of", 2021, 02, 04);
        setDateTimeParameterTest("C_CHAR_50", o, "2021-02-04                                        ");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

 public void Var101() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDate",
            "of", 2021, 02, 04);
        setDateTimeParameterTest("C_VARCHAR_50", o, "2021-02-04");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

 public void Var102() {
   notApplicable(); 
  }

 
 public void Var103() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDate",
            "of", 2021, 02, 04);

        setTypedParameterTest("GRAPHIC(10) CCSID 1200", o, "2021-02-04");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

 public void Var104() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDate",
            "of", 2021, 02, 04);
        setTypedParameterTest("VARGRAPHIC(10) CCSID 1200", o, "2021-02-04");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

 public void Var105() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDate",
            "of", 2021, 02, 04);
        setTypedParameterTest("VARGRAPHIC(257) CCSID 1200", o, "2021-02-04");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

 
 
 
 
  /* Check valid local time mappings -- TIME / CHAR / VARCHAR / GRAPHIC / VARGRAPHIC / LONGVARGRAPHIC  */ 
  public void Var106() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalTime",
            "of", 21, 02, 04);
        setDateTimeParameterTest("C_TIME", o, "21:02:04");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

 public void Var107() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalTime",
            "of", 21, 02, 04);
        setDateTimeParameterTest("C_CHAR_50", o, "21:02:04                                          ");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

 public void Var108() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalTime",
            "of", 21, 02, 04);
        setDateTimeParameterTest("C_VARCHAR_50", o, "21:02:04");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

 
  public void Var109() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalTime",
            "of", 21, 02, 04);
        setTypedParameterTest("GRAPHIC(10) CCSID 1200", o, "21:02:04  ");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

 public void Var110() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalTime",
            "of", 21, 02, 04);
        setTypedParameterTest("VARGRAPHIC(10) CCSID 1200", o, "21:02:04");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  public void Var111() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalTime",
            "of", 21, 02, 04);
        setTypedParameterTest("VARGRAPHIC(16000) CCSID 1200", o, "21:02:04");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

 
 
 
 
 
 
  /* Check valid LocalDateTime mappings -- DATE / TIME TIMESTAMP/ CHAR / VARCHAR / LONGVARGRAPHIC */ 
  public void Var112() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDateTime",
            "of", 2021,02,04, 21, 02, 04);
        setDateTimeParameterTest("C_DATE", o, "2021-02-04");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  public void Var113() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDateTime",
            "of", 2021,02,04, 21, 02, 04);
        setDateTimeParameterTest("C_TIME", o, "21:02:04");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  public void Var114() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDateTime",
            "of", 2021,02,04, 21, 02, 04,123456789);
         output_.println("localDatetime is "+o); 
        setDateTimeParameterTest("C_TIMESTAMP", o, "2021-02-04 21:02:04.123456");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }


 public void Var115() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDateTime",
            "of", 2021,02,04, 21, 02, 04 );
        setDateTimeParameterTest("C_CHAR_50", o, "2021-02-04 21:02:04.0                             ");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

 public void Var116() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDateTime",
            "of",  2021,02,04, 21, 02, 04); 
        setDateTimeParameterTest("C_VARCHAR_50", o, "2021-02-04 21:02:04.0");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

 
  public void Var117() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDateTime",
            "of",  2021,02,04, 21, 02, 04); 
        setTypedParameterTest("VARGRAPHIC(16000) CCSID 1200", o, "2021-02-04 21:02:04.0");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }
/**
setTypedParameterTest () - Set the specified paramete of the specified type.              
**/
    public void setTypedParameterTest(String parameterType, Object setObject,String expectedString)
    {
            try {
                

                PreparedStatement ps = connectionDateTime_.prepareStatement ( 
                    "SELECT CAST(? AS "+parameterType+") AS C1 FROM SYSIBM.SYSDUMMY1"); 
                ps.setObject (1, setObject);
                ResultSet rs = ps.executeQuery ();
                rs.next ();
                String check = rs.getString (1);
                rs.close ();
                ps.close ();

                assertCondition (expectedString.equals(check)," got "+check+" sb " + expectedString);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
    }

  /* Graphic */ 
  public void Var118() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O(
            "java.time.LocalDateTime", "of", 2021, 02, 04, 21, 02, 04);
        setTypedParameterTest("GRAPHIC(26) CCSID 1200", o,
            "2021-02-04 21:02:04.0     ");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /* varGraphic */ 
   public void Var119() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O(
            "java.time.LocalDateTime", "of", 2021, 02, 04, 21, 02, 04);
        setTypedParameterTest("VARGRAPHIC(26) CCSID 1200", o,
            "2021-02-04 21:02:04.0");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
      
    }
  }
 
 
 
/* LIMITS Testing */

  /* Check valid local date mappings -- DATE / CHAR / VARCHAR  */ 
  public void Var120() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDate",
            "of", 1999, 01, 01);
        setDateTimeParameterTest("C_DATE", o, "1999-01-01");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  public void Var121() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDate",
            "of", 1999, 12, 31);
        setDateTimeParameterTest("C_DATE", o, "1999-12-31");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }



 public void Var122() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDate",
            "of", 1999, 1, 1);
        setDateTimeParameterTest("C_CHAR_50", o, "1999-01-01                                        ");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }
 public void Var123() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDate",
            "of", 1999, 12, 31);
        setDateTimeParameterTest("C_CHAR_50", o, "1999-12-31                                        ");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }


 public void Var124() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDate",
            "of", 1999, 1, 1);
        setDateTimeParameterTest("C_VARCHAR_50", o, "1999-01-01");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

 public void Var125() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDate",
            "of", 1999, 12, 31);
        setDateTimeParameterTest("C_VARCHAR_50", o, "1999-12-31");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }



  



  /* Check valid local time mappings -- TIME / CHAR / VARCHAR  Limits */ 
  public void Var126() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalTime",
            "of", 0, 00, 0);
        setDateTimeParameterTest("C_TIME", o, "00:00:00");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  public void Var127() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalTime",
            "of", 23, 59, 59);
        setDateTimeParameterTest("C_TIME", o, "23:59:59");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

 public void Var128() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalTime",
            "of", 0, 0, 0);
        setDateTimeParameterTest("C_CHAR_50", o, "00:00:00                                          ");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

 public void Var129() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalTime",
            "of", 23, 59, 59);
        setDateTimeParameterTest("C_CHAR_50", o, "23:59:59                                          ");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }


 public void Var130() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalTime",
            "of", 0, 0, 0);
        setDateTimeParameterTest("C_VARCHAR_50", o, "00:00:00");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

 public void Var131() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalTime",
            "of", 23, 59, 59);
        setDateTimeParameterTest("C_VARCHAR_50", o, "23:59:59");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }




  /* Check valid LocalDateTime mappings -- DATE / TIME TIMESTAMP/ CHAR / VARCHAR -- Limit */ 
  public void Var132() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDateTime",
            "of", 2000,01,01, 0, 0, 0);
        setDateTimeParameterTest("C_DATE", o, "2000-01-01");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }
 public void Var133() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDateTime",
            "of", 1999,12,31, 23, 59, 59);
        setDateTimeParameterTest("C_DATE", o, "1999-12-31");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  public void Var134() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDateTime",
            "of", 2000,01,01, 0, 0, 0);
        setDateTimeParameterTest("C_TIME", o, "00:00:00");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }


 public void Var135() {
    if (checkJdbc42()) {
      try {
       Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDateTime",
            "of", 1999,12,31, 23, 59, 59);  
        setDateTimeParameterTest("C_TIME", o, "23:59:59");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }



  public void Var136() {
    if (checkJdbc42()) {
      try {
       Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDateTime",
            "of", 2000,01,01, 0, 0, 0);

       setDateTimeParameterTest("C_TIMESTAMP", o, "2000-01-01 00:00:00.000000");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }


  public void Var137() {
    if (checkJdbc42()) {
      try {
  Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDateTime",
            "of", 1999,12,31, 23, 59, 59,999999000); 
 
        setDateTimeParameterTest("C_TIMESTAMP", o, "1999-12-31 23:59:59.999999");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }


 public void Var138() {
    if (checkJdbc42()) {
      try {
      Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDateTime",
            "of", 2000,01,01, 0, 0, 0);


        setDateTimeParameterTest("C_CHAR_50", o, "2000-01-01 00:00:00.0                             ");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

 public void Var139() {
    if (checkJdbc42()) {
      try {
   Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDateTime",
            "of", 1999,12,31, 23, 59, 59,999999000); 
 

         setDateTimeParameterTest("C_CHAR_50", o, "1999-12-31 23:59:59.999999                        ");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }



public void Var140() {
    if (checkJdbc42()) {
      try {
      Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDateTime",
            "of", 2000,01,01, 0, 0, 0);


        setDateTimeParameterTest("C_VARCHAR_50", o, "2000-01-01 00:00:00.0");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  public void Var141() {
    JDSerializeFile pstestSet = null;
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDateTime", "of", 1999, 12, 31, 23, 59, 59,
            999999000);

        setDateTimeParameterTest("C_VARCHAR_50", o, "1999-12-31 23:59:59.999999");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

    public void testSetFailure(String columnName, Object o, String expectedMessage) {
      JDSerializeFile pstestSet = null;
      try {
        pstestSet = JDPSTest.getPstestSet(connection_);
	    PreparedStatement ps = connection_.prepareStatement (
								 "INSERT INTO " + pstestSet.getName()
								 + " ("+columnName+") VALUES (?)");
	    ps.setObject (1, o);
	    ps.execute(); 
	    ps.close ();
	    failed ("Didn't throw SQLException");
	}
	catch (Exception e) {
	    String message = e.toString();
	    assertCondition(  message.indexOf(expectedMessage) >= 0, "got "+message+" sb "+expectedMessage); 
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

   /* Test the setting of all other types using LocalTime */
    public void testLocalTimeFailure(String columnName) { 
      testLocalTimeFailure(columnName,"Data type mismatch"); 
    }

    public void testLocalTimeFailure(String columnName, String expectedMessage) {
      if (checkJdbc42()) {
        try {
          Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalTime", "of", 23, 59, 59);

          testSetFailure(columnName, o, expectedMessage);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }

    }

    public void Var142() {     notApplicable();  }
 
    public void Var143() {       testLocalTimeFailure("C_SMALLINT");   }
    public void Var144() {       testLocalTimeFailure("C_INTEGER");   }
    public void Var145() {       testLocalTimeFailure("C_REAL");   }
    public void Var146() {       testLocalTimeFailure("C_FLOAT");   }
    public void Var147() {       testLocalTimeFailure("C_DOUBLE");   }
    public void Var148() {       testLocalTimeFailure("C_DECIMAL_50");   }
    public void Var149() {       testLocalTimeFailure("C_DECIMAL_105");   }
    public void Var150() {       testLocalTimeFailure("C_NUMERIC_50");   }
    public void Var151() {       testLocalTimeFailure("C_NUMERIC_105");   }
    public void Var152() {       testLocalTimeFailure("C_BINARY_1");   }
    public void Var153() {       testLocalTimeFailure("C_BINARY_20");   }
    public void Var154() {       testLocalTimeFailure("C_VARBINARY_20");   }
    public void Var155() {       testLocalTimeFailure("C_DATE");   }
    public void Var156() {       testLocalTimeFailure("C_TIMESTAMP");   }
    public void Var157() {       testLocalTimeFailure("C_BLOB");   }
    public void Var158() {       testLocalTimeFailure("C_CLOB");   }
    public void Var159() {       testLocalTimeFailure("C_DBCLOB");   }
    public void Var160() {       testLocalTimeFailure("C_DATALINK");   }
    public void Var161() {       testLocalTimeFailure("C_DISTINCT");   }
    public void Var162() {       testLocalTimeFailure("C_BIGINT");   }
    public void Var163() {    if (checkBooleanSupport()) {    testLocalTimeFailure("C_BOOLEAN");   }}
    public void Var164() {       testLocalTimeFailure("C_CHAR_1","Data truncation");   }


    
        public void testLocalDateFailure(String columnName) { 
      testLocalDateFailure(columnName,"Data type mismatch"); 
    }

      public void testLocalDateFailure(String columnName, String expectedMessage) {
    if (checkJdbc42()) {
      try {
        
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDate",
            "of", 2021, 02, 04);

        testSetFailure(columnName, o, expectedMessage);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

  }



    public void Var165() {       testLocalDateFailure("C_SMALLINT");   }
    public void Var166() {       testLocalDateFailure("C_INTEGER");   }
    public void Var167() {       testLocalDateFailure("C_REAL");   }
    public void Var168() {       testLocalDateFailure("C_FLOAT");   }
    public void Var169() {       testLocalDateFailure("C_DOUBLE");   }
    public void Var170() {       testLocalDateFailure("C_DECIMAL_50");   }
    public void Var171() {       testLocalDateFailure("C_DECIMAL_105");   }
    public void Var172() {       testLocalDateFailure("C_NUMERIC_50");   }
    public void Var173() {       testLocalDateFailure("C_NUMERIC_105");   }
    public void Var174() {       testLocalDateFailure("C_BINARY_1");   }
    public void Var175() {       testLocalDateFailure("C_BINARY_20");   }
    public void Var176() {       testLocalDateFailure("C_VARBINARY_20");   }
    public void Var177() {       testLocalDateFailure("C_TIME");   }
    public void Var178() {       testLocalDateFailure("C_TIMESTAMP");   }
    public void Var179() {       testLocalDateFailure("C_BLOB");   }
    public void Var180() {       testLocalDateFailure("C_CLOB");   }
    public void Var181() {       testLocalDateFailure("C_DBCLOB");   }
    public void Var182() {       testLocalDateFailure("C_DATALINK");   }
    public void Var183() {       testLocalDateFailure("C_DISTINCT");   }
    public void Var184() {       testLocalDateFailure("C_BIGINT");   }
    public void Var185() {    if (checkBooleanSupport()) {    testLocalDateFailure("C_BOOLEAN");   }}
    public void Var186() {       testLocalDateFailure("C_CHAR_1","Data truncation");   }




        public void testLocalDateTimeFailure(String columnName) { 
      testLocalDateTimeFailure(columnName,"Data type mismatch"); 
    }

      public void testLocalDateTimeFailure(String columnName, String expectedMessage) {
    if (checkJdbc42()) {
      try {
        
            
                    Object o = JDReflectionUtil.callStaticMethod_O(
            "java.time.LocalDateTime", "of", 2021, 02, 04, 21, 02, 04);

        testSetFailure(columnName, o, expectedMessage);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

  }


    public void Var187() {       testLocalDateTimeFailure("C_SMALLINT");   }
    public void Var188() {       testLocalDateTimeFailure("C_INTEGER");   }
    public void Var189() {       testLocalDateTimeFailure("C_REAL");   }
    public void Var190() {       testLocalDateTimeFailure("C_FLOAT");   }
    public void Var191() {       testLocalDateTimeFailure("C_DOUBLE");   }
    public void Var192() {       testLocalDateTimeFailure("C_DECIMAL_50");   }
    public void Var193() {       testLocalDateTimeFailure("C_DECIMAL_105");   }
    public void Var194() {       testLocalDateTimeFailure("C_NUMERIC_50");   }
    public void Var195() {       testLocalDateTimeFailure("C_NUMERIC_105");   }
    public void Var196() {       testLocalDateTimeFailure("C_BINARY_1");   }
    public void Var197() {       testLocalDateTimeFailure("C_BINARY_20");   }
    public void Var198() {       testLocalDateTimeFailure("C_VARBINARY_20");   }
    public void Var199() {       testLocalDateTimeFailure("C_BLOB");   }
    public void Var200() {       testLocalDateTimeFailure("C_CLOB");   }
    public void Var201() {       testLocalDateTimeFailure("C_DBCLOB");   }
    public void Var202() {       testLocalDateTimeFailure("C_DATALINK");   }
    public void Var203() {       testLocalDateTimeFailure("C_DISTINCT");   }
    public void Var204() {       testLocalDateTimeFailure("C_BIGINT");   }
    public void Var205() {    if (checkBooleanSupport()) {    testLocalDateTimeFailure("C_BOOLEAN");   }}
    public void Var206() {       testLocalDateTimeFailure("C_CHAR_1","Data truncation");   }








}
