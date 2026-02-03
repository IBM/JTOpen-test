///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSSetBoolean.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

 ////////////////////////////////////////////////////////////////////////
 //
 // File Name:    JDPSSetBoolean.java
 //
 // Classes:      JDPSSetBoolean
 //
 ////////////////////////////////////////////////////////////////////////

package test.JD.PS;


import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDPSTest;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JDTestcase;
import test.JD.JDSerializeFile;



/**
Testcase JDPSetBoolean.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setBoolean()
</ul>
**/
public class JDPSSetBoolean
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDPSSetBoolean";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDPSTest.main(newArgs); 
   }



    // Private data.
    private Statement           statement_;



/**
Constructor.
**/
    public JDPSSetBoolean (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDPSSetBoolean",
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
        
        ;
        connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
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
setBoolean() - Should throw exception when the prepared
statement is closed.
**/
    public void Var001()
    {
      JDSerializeFile pstestSet = null;
      try {
        pstestSet = JDPSTest.getPstestSet(connection_);
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_INTEGER) VALUES (?)");
            ps.close ();
            ps.setBoolean (1, true);
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
setBoolean() - Should throw exception when an invalid index is
specified.
**/
    public void Var002()
    {
      JDSerializeFile pstestSet = null;
      try {
        pstestSet = JDPSTest.getPstestSet(connection_);
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_INTEGER, C_SMALLINT) VALUES (?, ?)");
            ps.setBoolean (100, false);
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
setBoolean() - Should throw exception when index is 0.
**/
    public void Var003()
    {
      JDSerializeFile pstestSet = null;
      try {
        pstestSet = JDPSTest.getPstestSet(connection_);
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_INTEGER, C_SMALLINT) VALUES (?, ?)");
            ps.setBoolean (0, true);
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
setBoolean() - Should throw exception when index is -1.
**/
    public void Var004()
    {
      JDSerializeFile pstestSet = null;
      try {
        pstestSet = JDPSTest.getPstestSet(connection_);
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_INTEGER, C_SMALLINT) VALUES (?, ?)");
            ps.setBoolean (-1, false);
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
setBoolean() - Should work with a valid parameter index
greater than 1.
**/
    public void Var005()
    {
      JDSerializeFile pstestSet = null;
      try {
        pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_KEY, C_SMALLINT) VALUES (?, ?)");
            ps.setString (1, "Test");
            ps.setBoolean (2, false);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_SMALLINT FROM " + pstestSet.getName());
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == false);
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
setBoolean() - Should throw exception when the parameter is
not an input parameter.
**/
    public void Var006()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC permits setting of an output parameter");
        return; 
      } 

        try {
            PreparedStatement ps = connection_.prepareStatement (
                "CALL " + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
            
            ps.setBoolean (2, false);
            ps.setByte    (1, (byte)1); 
            ps.setByte    (3, (byte) 2); 
            // Note: jcc doesn't check until the execute. 
            ps.execute(); 
            ps.executeUpdate(); ps.close ();

            failed ("Didn't throw SQLException when setting an output only parameter");
        }
        catch (Exception e) {
            // e.printStackTrace(output_); 
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setBoolean() - Set a SMALLINT parameter, specifying true.
**/
    public void Var007()
    {
      JDSerializeFile pstestSet = null;
      try {
        pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_SMALLINT) VALUES (?)");
            ps.setBoolean (1, true);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_SMALLINT FROM " + pstestSet.getName());
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == true);
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
setBoolean() - Set a SMALLINT parameter, specifying false.
**/
    public void Var008()
    {
      JDSerializeFile pstestSet = null;
      try {
        pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_SMALLINT) VALUES (?)");
            ps.setBoolean (1, false);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_SMALLINT FROM " + pstestSet.getName());
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == false);
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
setBoolean() - Set an INTEGER parameter, specifying true.
**/
    public void Var009()
    {
      JDSerializeFile pstestSet = null;
      try {
        pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_INTEGER) VALUES (?)");
            ps.setBoolean (1, true);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_INTEGER FROM " + pstestSet.getName());
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == true);
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
setBoolean() - Set an INTEGER parameter, specifying false.
**/
    public void Var010()
    {
      JDSerializeFile pstestSet = null;
      try {
        pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_INTEGER) VALUES (?)");
            ps.setBoolean (1, false);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_INTEGER FROM " + pstestSet.getName());
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == false);
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
setBoolean() - Set an REAL parameter, specifying true.
**/
    public void Var011()
    {
      JDSerializeFile pstestSet = null;
      try {
        pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_REAL) VALUES (?)");
            ps.setBoolean (1, true);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_REAL FROM " + pstestSet.getName());
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == true);
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
setBoolean() - Set an REAL parameter, specifying false.
**/
    public void Var012()
    {
      JDSerializeFile pstestSet = null;
      try {
        pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_REAL) VALUES (?)");
            ps.setBoolean (1, false);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_REAL FROM " + pstestSet.getName());
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == false);
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
setBoolean() - Set an FLOAT parameter, specifying true.
**/
    public void Var013()
    {
      JDSerializeFile pstestSet = null;
      try {
        pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_FLOAT) VALUES (?)");
            ps.setBoolean (1, true);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_FLOAT FROM " + pstestSet.getName());
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == true);
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
setBoolean() - Set an FLOAT parameter, specifying false.
**/
    public void Var014()
    {
      JDSerializeFile pstestSet = null;
      try {
        pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_FLOAT) VALUES (?)");
            ps.setBoolean (1, false);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_FLOAT FROM " + pstestSet.getName());
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == false);
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
setBoolean() - Set an DOUBLE parameter, specifying true.
**/
    public void Var015()
    {
      JDSerializeFile pstestSet = null;
      try {
        pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_DOUBLE) VALUES (?)");
            ps.setBoolean (1, true);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DOUBLE FROM " + pstestSet.getName());
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == true);
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
setBoolean() - Set an DOUBLE parameter, specifying false.
**/
    public void Var016()
    {
      JDSerializeFile pstestSet = null;
      try {
        pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_DOUBLE) VALUES (?)");
            ps.setBoolean (1, false);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DOUBLE FROM " + pstestSet.getName());
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == false);
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
setBoolean() - Set an DECIMAL parameter, specifying true.
**/
    public void Var017()
    {
      JDSerializeFile pstestSet = null;
      try {
        pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_DECIMAL_105) VALUES (?)");
            ps.setBoolean (1, true);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DECIMAL_105 FROM " + pstestSet.getName());
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == true);
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
setBoolean() - Set an DECIMAL parameter, specifying false.
**/
    public void Var018()
    {
      JDSerializeFile pstestSet = null;
      try {
        pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_DECIMAL_50) VALUES (?)");
            ps.setBoolean (1, false);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DECIMAL_50 FROM " + pstestSet.getName());
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == false);
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
setBoolean() - Set an NUMERIC parameter, specifying true.
**/
    public void Var019()
    {
      JDSerializeFile pstestSet = null;
      try {
        pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_NUMERIC_50) VALUES (?)");
            ps.setBoolean (1, true);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_50 FROM " + pstestSet.getName());
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == true);
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
setBoolean() - Set an NUMERIC parameter, specifying false.
**/
    public void Var020()
    {
      JDSerializeFile pstestSet = null;
      try {
        pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_NUMERIC_105) VALUES (?)");
            ps.setBoolean (1, false);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_105 FROM " + pstestSet.getName());
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == false);
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
setBoolean() - Set an CHAR parameter, specifying true.
**/
    public void Var021()
    {
      JDSerializeFile pstestSet = null;
      try {
        pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_CHAR_1) VALUES (?)");
            ps.setBoolean (1, true);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_CHAR_1 FROM " + pstestSet.getName());
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == true);
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
setBoolean() - Set an CHAR parameter, specifying false.
**/
    public void Var022()
    {
      JDSerializeFile pstestSet = null;
      try {
        pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_CHAR_1) VALUES (?)");
            ps.setBoolean (1, false);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_CHAR_1 FROM " + pstestSet.getName());
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == false);
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
setBoolean() - Set an VARCHAR parameter, specifying true.
**/
    public void Var023()
    {
      JDSerializeFile pstestSet = null;
      try {
        pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_VARCHAR_50) VALUES (?)");
            ps.setBoolean (1, true);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + pstestSet.getName());
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == true);
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
setBoolean() - Set an VARCHAR parameter, specifying false.
**/
    public void Var024()
    {
      JDSerializeFile pstestSet = null;
      try {
        pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_VARCHAR_50) VALUES (?)");
            ps.setBoolean (1, false);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + pstestSet.getName());
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == false);
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
setBoolean() - Set a CLOB parameter.
**/
    public void Var025()
    {
        if (checkLobSupport ()) {
          JDSerializeFile pstestSet = null;
          try {
            pstestSet = JDPSTest.getPstestSet(connection_);
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + pstestSet.getName()
                    + " (C_CLOB) VALUES (?)");
                ps.setBoolean (1, true);
                ps.execute(); 
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
    }




/**
setBoolean() - Set a DBCLOB parameter.
**/
    public void Var026()
    {
        if (checkLobSupport ()) {
          JDSerializeFile pstestSet = null;
          try {
            pstestSet = JDPSTest.getPstestSet(connection_);
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + pstestSet.getName()
                    + " (C_DBCLOB) VALUES (?)");
                ps.setBoolean (1, true);
                ps.execute(); 
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
    }



/**
setBoolean() - Set a BINARY parameter.
**/
    public void Var027()
    {
      JDSerializeFile pstestSet = null;
      try {
        pstestSet = JDPSTest.getPstestSet(connection_);
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_BINARY_20) VALUES (?)");
            ps.setBoolean (1, false);
            ps.executeUpdate ();
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
setBoolean() - Set a VARBINARY parameter.
**/
    public void Var028()
    {
      JDSerializeFile pstestSet = null;
      try {
        pstestSet = JDPSTest.getPstestSet(connection_);
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_VARBINARY_20) VALUES (?)");
            ps.setBoolean (1, true);
            ps.executeUpdate ();
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
setBoolean() - Set a BLOB parameter.
**/
    public void Var029()
    {
      JDSerializeFile pstestSet = null;
      try {
        pstestSet = JDPSTest.getPstestSet(connection_);
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_BLOB) VALUES (?)");
            ps.setBoolean (1, true);
            ps.execute(); 
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
setBoolean() - Set a DATE parameter.
**/
    public void Var030()
    {
      JDSerializeFile pstestSet = null;
      try {
        pstestSet = JDPSTest.getPstestSet(connection_);
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_DATE) VALUES (?)");
            ps.setBoolean (1, true);
            ps.execute(); 
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
setBoolean() - Set a TIME parameter.
**/
    public void Var031()
    {
      JDSerializeFile pstestSet = null;
      try {
        pstestSet = JDPSTest.getPstestSet(connection_);
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_TIME) VALUES (?)");
            ps.setBoolean (1, true);
            ps.execute(); 
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
setBoolean() - Set a TIMESTAMP parameter.
**/
    public void Var032()
    {
      JDSerializeFile pstestSet = null;
      try {
        pstestSet = JDPSTest.getPstestSet(connection_);
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_TIMESTAMP) VALUES (?)");
            ps.setBoolean (1, true);
            ps.execute(); 
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
setBoolean() - Set a DATALINK parameter.
**/
    public void Var033()
    {
        if ((getDriver () == JDTestDriver.DRIVER_NATIVE) && (getJdbcLevel() <= 2)) {
            notApplicable ("Native driver pre-JDBC 3.0");
            return;
        }
        if (checkDatalinkSupport ()) {
          JDSerializeFile pstestSet = null;
          try {
            pstestSet = JDPSTest.getPstestSet(connection_);
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + pstestSet.getName()
                    + " (C_DATALINK) VALUES (?)");
                ps.setBoolean (1, true);
                ps.execute(); 
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
    }



/**
setBoolean() - Set a DISTINCT parameter.
**/
    // @C0C
    public void Var034()
    {
        if (checkLobSupport ()) {         
          JDSerializeFile pstestSet = null;
          try {
            pstestSet = JDPSTest.getPstestSet(connection_);
                statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + pstestSet.getName()
                    + " (C_DISTINCT) VALUES (?)");
                ps.setBoolean (1, true);
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_DISTINCT FROM " + pstestSet.getName());
                rs.next ();
                boolean check = rs.getBoolean (1);
                rs.close ();

                assertCondition (check == true);
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
    }



/**
setBoolean() - Set a BIGINT parameter, specifying true.
**/
    public void Var035()
    {
        if (checkBigintSupport()) {
          JDSerializeFile pstestSet = null;

          try {
            pstestSet = JDPSTest.getPstestSet(connection_);
          
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_BIGINT) VALUES (?)");
            ps.setBoolean (1, true);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_BIGINT FROM " + pstestSet.getName());
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == true);
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
    }



/**
setBoolean() - Set a BIGINT parameter, specifying false.
**/
    public void Var036()
    {
        if (checkBigintSupport()) {
          JDSerializeFile pstestSet = null;
          try {
            pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_BIGINT) VALUES (?)");
            ps.setBoolean (1, false);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_BIGINT FROM " + pstestSet.getName());
            rs.next ();
            boolean check = rs.getBoolean (1);
            rs.close ();

            assertCondition (check == false);
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
    }


    /**
    setBoolean() - Set a DECFLOAT parameter, specifying true.
    **/
        public void Var037()
        {
          if (checkDecFloatSupport()) { 
            if (getDriver() == JDTestDriver.DRIVER_JCC) {
              notApplicable("JCC does not support setting DECFLOAT from boolean");
              return; 
            } 

            JDSerializeFile pstestSet = null;
            try {
              pstestSet = JDPSTest.getSerializeFile(connection_, JDPSTest.SETDFP16);
              String tablename=pstestSet.getName();
              statement_.executeUpdate ("DELETE FROM " + tablename);
              
              PreparedStatement ps = connection_.prepareStatement (
                  "INSERT INTO " + tablename
                  + "  VALUES (?)");
              ps.setBoolean (1, true);
              ps.executeUpdate ();
              ps.close ();
              
              ResultSet rs = statement_.executeQuery ("SELECT * FROM " + tablename);
              rs.next ();
              boolean check = rs.getBoolean (1);
              rs.close ();
              
              assertCondition (check == true);
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
        }



    /**
    setBoolean() - Set an DECFLOAT parameter, specifying false.
    **/
        public void Var038()
        {
          if (checkDecFloatSupport()) { 
            if (getDriver() == JDTestDriver.DRIVER_JCC) {
              notApplicable("JCC does not support setting DECFLOAT from boolean");
              return; 
            } 
            JDSerializeFile pstestSet = null;
            try {
              pstestSet = JDPSTest.getSerializeFile(connection_, JDPSTest.SETDFP16);
              String tablename=pstestSet.getName();
              statement_.executeUpdate ("DELETE FROM " + tablename);
              
              PreparedStatement ps = connection_.prepareStatement (
                  "INSERT INTO " + tablename
                  + "  VALUES (?)");
              ps.setBoolean (1, false);
              ps.executeUpdate ();
              ps.close ();
              
              ResultSet rs = statement_.executeQuery ("SELECT * FROM " + tablename);
              rs.next ();
              boolean check = rs.getBoolean (1);
              rs.close ();
              
              assertCondition (check == false);
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
          
        }

        /**
        setBoolean() - Set a DECFLOAT parameter, specifying true.
        **/
            public void Var039()
            {
              if (checkDecFloatSupport()) { 
                if (getDriver() == JDTestDriver.DRIVER_JCC) {
                  notApplicable("JCC does not support setting DECFLOAT from boolean");
                  return; 
                } 
                JDSerializeFile pstestSet = null;
                try {
                  pstestSet = JDPSTest.getSerializeFile(connection_, JDPSTest.SETDFP34);
                  String filename = pstestSet.getName();
                  statement_.executeUpdate ("DELETE FROM " + filename);
                  
                  PreparedStatement ps = connection_.prepareStatement (
                      "INSERT INTO " + filename
                      + "  VALUES (?)");
                  ps.setBoolean (1, true);
                  ps.executeUpdate ();
                  ps.close ();
                  
                  ResultSet rs = statement_.executeQuery ("SELECT * FROM " + filename);
                  rs.next ();
                  boolean check = rs.getBoolean (1);
                  rs.close ();
                  
                  assertCondition (check == true);
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
            }



        /**
        setBoolean() - Set an DECFLOAT parameter, specifying false.
        **/
            public void Var040() 
            {
              if (checkDecFloatSupport()) { 
                if (getDriver() == JDTestDriver.DRIVER_JCC) {
                  notApplicable("JCC does not support setting DECFLOAT from boolean");
                  return; 
                } 
                JDSerializeFile pstestSet = null;
                try {
                  pstestSet = JDPSTest.getSerializeFile(connection_, JDPSTest.SETDFP34);
                  String tablename=pstestSet.getName();
                  statement_.executeUpdate ("DELETE FROM " + tablename);
                  
                  PreparedStatement ps = connection_.prepareStatement (
                      "INSERT INTO " + tablename
                      + "  VALUES (?)");
                  ps.setBoolean (1, false);
                  ps.executeUpdate ();
                  ps.close ();
                  
                  ResultSet rs = statement_.executeQuery ("SELECT * FROM " + tablename);
                  rs.next ();
                  boolean check = rs.getBoolean (1);
                  rs.close ();
                  
                  assertCondition (check == false);
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
              
            }


/**
setBoolean() - Set an XML  parameter.
**/
    public void Var041()
    {
        if (checkXmlSupport ()) {
          JDSerializeFile pstestSetxml = null;
          try {
            pstestSetxml = JDPSTest.getPstestSetxml(connection_);
            String tablename = pstestSetxml.getName(); 
		PreparedStatement ps =
		  connection_.prepareStatement( 
					       "INSERT INTO " +
						tablename
						+ " VALUES (?)");
		try {
		    ps.setBoolean (1, true);
		    ps.execute(); 
		    ps.close ();
		    failed ("Didn't throw SQLException");
		}
		catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    } catch (Exception e) {
		failed(e, "unexpected exception"); 
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
setBoolean() - Should work on boolean column.
**/
    public void Var042()
    {
	if (checkBooleanSupport()) { 
	      JDSerializeFile pstestSet = null;
	      try {
	        pstestSet = JDPSTest.getPstestSet(connection_);
		statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + pstestSet.getName()
								     + " (C_KEY, C_BOOLEAN) VALUES (?, ?)");
		ps.setString (1, "Test");
		ps.setBoolean (2, false);
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_BOOLEAN FROM " + pstestSet.getName());
		rs.next ();
		boolean check = rs.getBoolean (1);
		rs.close ();

		assertCondition (check == false);
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


    }


    public void Var043()
    {
	if (checkBooleanSupport()) { 
	      JDSerializeFile pstestSet = null;
	      try {
	        pstestSet = JDPSTest.getPstestSet(connection_);
		statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + pstestSet.getName()
								     + " (C_KEY, C_BOOLEAN) VALUES (?, ?)");
		ps.setString (1, "Test");
		ps.setBoolean (2, true);
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_BOOLEAN FROM " + pstestSet.getName());
		rs.next ();
		boolean check = rs.getBoolean (1);
		rs.close ();

		assertCondition (check == true);
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


    }
}


