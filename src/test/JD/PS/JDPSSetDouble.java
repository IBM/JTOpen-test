///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSSetDouble.java
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
// File Name:    JDPSSetDouble.java
//
// Classes:      JDPSSetDouble
//
////////////////////////////////////////////////////////////////////////
//
//
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
import java.sql.DataTruncation;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable; import java.util.Vector;
import test.JD.JDSerializeFile;
import java.sql.SQLException;



/**
Testcase JDPSetDouble.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setDouble()
</ul>
**/
public class JDPSSetDouble
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDPSSetDouble";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDPSTest.main(newArgs); 
   }



    // Constants.
    private static final String PACKAGE             = "JDPSSD";



    // Private data.
    private Statement           statement_;
    private Connection          connection1_;           // @E1A
    private Statement           statement1_;            // @E1A



/**
Constructor.
**/
    public JDPSSetDouble (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDPSSetDouble",
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
        connection1_ = testDriver_.getConnection (url+";big decimal=false",systemObject_.getUserId(), encryptedPassword_);    // @E1A
        statement1_ = connection_.createStatement ();                           // @E1A
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
        statement1_.close ();       // @E1A
        connection1_.close ();      // @E1A
        statement_.close ();
        connection_.close ();
        connection_ = null; 

    }



/**
Compares 2 doubles, allowing for some rounding error.
**/
    private boolean compare (double d1, double d2)
    {
        return (Math.abs (d1 - d2) < 0.001);
    }



/**
setDouble() - Should throw exception when the prepared
statement is closed.
**/
    public void Var001()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_DOUBLE) VALUES (?)");
            ps.close ();
            ps.setDouble (1, 33.4345);
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
setDouble() - Should throw exception when an invalid index is
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
            ps.setDouble (100, 334.432);
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
setDouble() - Should throw exception when index is 0.
**/
    public void Var003()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
            ps.setDouble (0, -3822.45);
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
setDouble() - Should throw exception when index is -1.
**/
    public void Var004()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
            ps.setDouble (-1, 9422.5);
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
setDouble() - Should work with a valid parameter index
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
                + " (C_KEY, C_DOUBLE) VALUES (?, ?)");
            ps.setString (1, "Test");
            ps.setDouble (2, 93423.459);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DOUBLE FROM " + pstestSet.getName());
            rs.next ();
            double check = rs.getDouble (1);
            rs.close ();

            assertCondition (check == 93423.459);
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
setDouble() - Should throw exception when the parameter is
not an input parameter.
**/
    public void Var006()
    {
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "CALL " + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
            ps.setDouble (2, 2222.5);
            ps.close ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setDouble() - Set a SMALLINT parameter.
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
            ps.setDouble (1, -426);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_SMALLINT FROM " + pstestSet.getName());
            rs.next ();
            double check = rs.getDouble (1);
            rs.close ();

            assertCondition (check == -426);
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
setDouble() - Set a SMALLINT parameter, when there is a decimal part.
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
            ps.setDouble (1, -212.222);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_SMALLINT FROM " + pstestSet.getName());
            rs.next ();
            double check = rs.getDouble (1);
            rs.close ();

            assertCondition (check == -212);
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
setDouble() - Set a SMALLINT parameter, when the value is too big.  This should
cause a DataTruncation exception to be thrown.
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
            ps.setDouble (1, -2122846575.1);
	    ps.executeUpdate(); 
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
		  assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation in V7R1");

        
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
setDouble() - Set an INTEGER parameter.
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
            ps.setDouble (1, -30679);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_INTEGER FROM " + pstestSet.getName());
            rs.next ();
            double check = rs.getDouble (1);
            rs.close ();

            assertCondition (check == -30679);
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
setDouble() - Set an INTEGER parameter, when there is a decimal part.
**/
    public void Var011()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_INTEGER) VALUES (?)");
            ps.setDouble (1, -306.8);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_INTEGER FROM " + pstestSet.getName());
            rs.next ();
            double check = rs.getDouble (1);
            rs.close ();

            assertCondition (check == -306);
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
setDouble() - Set an INTEGER parameter, when the value is too big.  This should
cause a DataTruncation exception to be thrown.
**/
    public void Var012()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_INTEGER) VALUES (?)");
            ps.setDouble (1, -448484842122846575.1);
	    ps.executeUpdate(); 
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
		  assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation in V7R1");

        
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
setDouble() - Set an REAL parameter.
**/
    public void Var013()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_REAL) VALUES (?)");
            ps.setDouble (1, 792.249);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_REAL FROM " + pstestSet.getName());
            rs.next ();
            double check = rs.getDouble (1);
            rs.close ();

            assertCondition (compare (check, 792.249));
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
setDouble() - Set an FLOAT parameter.
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
            ps.setDouble (1, -0.123);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_FLOAT FROM " + pstestSet.getName());
            rs.next ();
            double check = rs.getDouble (1);
            rs.close ();

            assertCondition (check == -0.123);
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
setDouble() - Set an DOUBLE parameter.
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
            ps.setDouble (1, -1.22290344);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DOUBLE FROM " + pstestSet.getName());
            rs.next ();
            double check = rs.getDouble (1);
            rs.close ();

            assertCondition (check == -1.22290344);
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
setDouble() - Set an DECIMAL parameter.
**/
    public void Var016()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_DECIMAL_105) VALUES (?)");
            ps.setDouble (1, -14001.23425);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DECIMAL_105 FROM " + pstestSet.getName());
            rs.next ();
            double check = rs.getDouble (1);
            rs.close ();

            assertCondition (check == -14001.23425);
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
setDouble() - Set an DECIMAL parameter, when the value is too big.
**/
    public void Var017()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_DECIMAL_50) VALUES (?)");
            ps.setDouble (1, -10132342.2);
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
setDouble() - Set an DECIMAL parameter, when only the fraction truncates.  This 
is allowed.
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
            ps.setDouble (1, -32342.2363);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DECIMAL_50 FROM " + pstestSet.getName());
            rs.next ();
            double check = rs.getDouble (1);
            rs.close ();

            assertCondition (check == -32342);
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
setDouble() - Set an DECIMAL parameter, when the value is very small.
**/
    public void Var019()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_DECIMAL_105) VALUES (?)");
            ps.setDouble (1, -0.00008);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DECIMAL_105 FROM " + pstestSet.getName());
            rs.next ();
            double check = rs.getDouble (1);
            rs.close ();
	    double difference = -0.00008 - check;
	    if (difference < 0) difference = -difference; 
            assertCondition (difference < 0.00000000000001, "Got "+check+" sb -0.00008");
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
setDouble() - Set an NUMERIC parameter.
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
            ps.setDouble (1, -2774);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_105 FROM " + pstestSet.getName());
            rs.next ();
            double check = rs.getDouble (1);
            rs.close ();

            assertCondition (check == -2774);
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
setDouble() - Set an NUMERIC parameter, when the value is too big.  The Native JDBC driver and 
the Toolbox JDBC driver differ slightly in the information contained in the data truncation
exception at this point.  The values for the transfer size and data size in the data truncation
object differ some between the native JDBC driver and the Toolbox JDBC driver.  The native JDBC 
driver bases the exception information off of the significant digits that are truncated.  The
data size is 14 significant digits and the transfer size is 5 digits.
**/
    public void Var021()
    {
    JDSerializeFile pstestSet = null;

	try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_NUMERIC_105) VALUES (?)");
            ps.setDouble (1, -22143456437334.4);
	    ps.executeUpdate(); 
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
		if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
		    assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation now in toolbox");

		} else { 

		    DataTruncation dt = (DataTruncation)e;

	    // See what we have here...
	    //output_.println("dt.getIndex() is " + dt.getIndex());
	    //output_.println("dt.getParameter() is " + dt.getParameter());
	    //output_.println("dt.getRead() is " + dt.getRead());
	    //output_.println("dt.getTransferSize() is " + dt.getTransferSize());
	    //output_.println("dt.getDataSize() is " + dt.getDataSize());
		    if ((isToolboxDriver()) || (getDriver () == JDTestDriver.DRIVER_JTOPENLITE))
			assertCondition ((dt.getIndex() == 1)
					 && (dt.getParameter() == true)
					 && (dt.getRead() == false)
					 && (dt.getTransferSize() == 10));
		    else 
			assertCondition ((dt.getIndex() == 1)
					 && (dt.getParameter() == true)
					 && (dt.getRead() == false)
					 && (dt.getTransferSize() == 10)
					 && (dt.getDataSize() == 19), "Updated 1/18/2013 for V7R1 fix");
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
setDouble() - Set an DECIMAL parameter, when only the fraction truncates.  This 
is allowed.
**/
    public void Var022()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_NUMERIC_50) VALUES (?)");
            ps.setDouble (1, -32342.2363);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_50 FROM " + pstestSet.getName());
            rs.next ();
            double check = rs.getDouble (1);
            rs.close ();

            assertCondition (check == -32342);
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
setDouble() - Set an NUMERIC parameter, when the value is very small.
**/
    public void Var023()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_NUMERIC_105) VALUES (?)");
            ps.setDouble (1, 0.00005);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_105 FROM " + pstestSet.getName());
            rs.next ();
            double check = rs.getDouble (1);
            rs.close ();

            assertCondition (check == 0.00005);
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
setDouble() - Set an CHAR(50) parameter.
**/
    public void Var024()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_CHAR_50) VALUES (?)");
            ps.setDouble (1, 21842.799332);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_CHAR_50 FROM " + pstestSet.getName());
            rs.next ();
            double check = rs.getDouble (1);
            rs.close ();

            assertCondition (check == 21842.799332);
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
setDouble() - Set an CHAR(1) parameter.   This throws an exception
because the double is stored with  .0 at the end.
**/
    public void Var025()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_CHAR_1) VALUES (?)");
            ps.setDouble (1, 5);
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
setDouble() - Set an CHAR(1) parameter, when the value is too big.
**/
    public void Var026()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_CHAR_1) VALUES (?)");
            ps.setDouble (1, 533.25);
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
setDouble() - Set an VARCHAR parameter.
**/
    public void Var027()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_VARCHAR_50) VALUES (?)");
            ps.setDouble (1, -0.8766753);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + pstestSet.getName());
            rs.next ();
            double check = rs.getDouble (1);
            rs.close ();

            assertCondition (check == -0.8766753);
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
setDouble() - Set a CLOB parameter.
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
                    + " (C_CLOB) VALUES (?)");
                ps.setDouble (1, -6542.3);
		ps.executeUpdate(); 
                ps.close ();
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
setDouble() - Set a DBCLOB parameter.
**/
    public void Var029()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkLobSupport ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + pstestSet.getName()
                    + " (C_DBCLOB) VALUES (?)");
                ps.setDouble (1, -45.432);
		ps.executeUpdate(); 
                ps.close ();
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
setDouble() - Set a BINARY parameter.
**/
    public void Var030()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_BINARY_20) VALUES (?)");
            ps.setDouble (1, -545.44);
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
setDouble() - Set a VARBINARY parameter.
**/
    public void Var031()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_VARBINARY_20) VALUES (?)");
            ps.setDouble (1, -1.9443);
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
setDouble() - Set a BLOB parameter.
**/
    public void Var032()
    {
    JDSerializeFile pstestSet = null;
        try {
      pstestSet = JDPSTest.getPstestSet(connection_);
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_BLOB) VALUES (?)");
            ps.setDouble (1, 54.43);
	    ps.executeUpdate(); 
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
setDouble() - Set a DATE parameter.
**/
    public void Var033()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_DATE) VALUES (?)");
            ps.setDouble (1, 56.5);
	    ps.executeUpdate(); 
            ps.close ();
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
setDouble() - Set a TIME parameter.
**/
    public void Var034()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_TIME) VALUES (?)");
            ps.setDouble (1, 0.13);
	    ps.executeUpdate(); 
            ps.close ();
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
setDouble() - Set a TIMESTAMP parameter.
**/
    public void Var035()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        try {
            PreparedStatement ps = connection_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_TIMESTAMP) VALUES (?)");
            ps.setDouble (1, 454);
	    ps.executeUpdate(); 
            ps.close ();
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
setDouble() - Set a DATALINK parameter.
**/
    public void Var036()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        if (checkLobSupport ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                    "INSERT INTO " + pstestSet.getName()
                    + " (C_DATALINK) VALUES (?)");
                ps.setDouble (1, -5.54);
	    ps.executeUpdate(); 
                ps.close ();
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
setDouble() - Set a DISTINCT parameter.
**/
    public void Var037()
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
                ps.setDouble (1, 39.81);
                ps.executeUpdate ();
                ps.close ();

                ResultSet rs = statement_.executeQuery ("SELECT C_DISTINCT FROM " + pstestSet.getName());
                rs.next ();
                double check = rs.getDouble (1);
                rs.close ();

                assertCondition (check == 39);
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
setDouble() - Set a BIGINT parameter.
**/
    public void Var038()
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
            ps.setDouble (1, 3079);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_BIGINT FROM " + pstestSet.getName());
            rs.next ();
            double check = rs.getDouble (1);
            rs.close ();

            assertCondition (check == 3079);
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
setDouble() - Set a BIGINT parameter, when there is a decimal part.
**/
    public void Var039()
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
            ps.setDouble (1, 306.8);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_BIGINT FROM " + pstestSet.getName());
            rs.next ();
            double check = rs.getDouble (1);
            rs.close ();

            assertCondition (check == 306);
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
setDouble() - Set a BIGINT parameter, when the value is too big.  This should
cause a DataTruncation exception to be thrown.
**/
    public void Var040()
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
            ps.setDouble (1, 306154175489237602375024750974097540.8);
	    ps.executeUpdate(); 
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
		  assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation in V7R1");

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
setDouble() - Set a parameter in a statement that comes from the
package cache.
**/
    public void Var041()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        try {
            String insert = "INSERT INTO " + pstestSet.getName()
                + " (C_DOUBLE) VALUES (?)";
            
            if (isToolboxDriver())
                JDSetupPackage.prime (systemObject_, PACKAGE, 
                    JDPSTest.COLLECTION, insert);
            else
                JDSetupPackage.prime (systemObject_,encryptedPassword_,  PACKAGE, 
                    JDPSTest.COLLECTION, insert, "", getDriver());

            statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            Connection c2 = testDriver_.getConnection (baseURL_
                + ";extended dynamic=true;package=" + PACKAGE
                + ";package library=" + JDPSTest.COLLECTION
                + ";package cache=true", userId_, encryptedPassword_);
            PreparedStatement ps = c2.prepareStatement (insert);
            ps.setDouble (1, 982.135);
            ps.executeUpdate ();
            ps.close ();
            c2.close ();

            ResultSet rs = statement_.executeQuery ("SELECT C_DOUBLE FROM " + pstestSet.getName());
            rs.next ();
            double check = rs.getDouble (1);
            rs.close ();            

            assertCondition (check == 982.135);
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



// @E1A
/**
setDouble() - Set an DECIMAL parameter, when the "big decimal" property
is set to "false".
**/
    public void Var042()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        try {
            statement1_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection1_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_DECIMAL_105) VALUES (?)");
            ps.setDouble (1, -14001.23425);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement1_.executeQuery ("SELECT C_DECIMAL_105 FROM " + pstestSet.getName());
            rs.next ();
            double check = rs.getDouble (1);
            rs.close ();

            assertCondition (check == -14001.23425);
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



// @E1A
/**
setDouble() - Set an NUMERIC parameter, when the "big decimal" property
is set to "false".
**/
    public void Var043()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        try {
            statement1_.executeUpdate ("DELETE FROM " + pstestSet.getName());

            PreparedStatement ps = connection1_.prepareStatement (
                "INSERT INTO " + pstestSet.getName()
                + " (C_NUMERIC_105) VALUES (?)");
            ps.setDouble (1, -2774);
            ps.executeUpdate ();
            ps.close ();

            ResultSet rs = statement1_.executeQuery ("SELECT C_NUMERIC_105 FROM " + pstestSet.getName());
            rs.next ();
            double check = rs.getDouble (1);
            rs.close ();

            assertCondition (check == -2774);
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
    setInt() - Set an DECFLOAT16 parameter.
    **/
    public void Var044()
    {
      if (checkDecFloatSupport()) {
        JDSerializeFile pstestSetdfp = null;
        try {
          pstestSetdfp = JDPSTest.getPstestSetdfp16(connection_);
          String tablename16 = pstestSetdfp.getName();
          statement_.executeUpdate ("DELETE FROM " + tablename16);
          
          PreparedStatement ps = connection_.prepareStatement (
              "INSERT INTO " + tablename16
              + "  VALUES (?)");
          ps.setDouble (1, -10012);
          ps.executeUpdate ();
          ps.close ();
          
          ResultSet rs = statement_.executeQuery ("SELECT * FROM " + tablename16);
          rs.next ();
          double check = rs.getDouble (1);
          rs.close ();
          
          assertCondition (check == -10012);
        }
        catch (Exception e) {
          failed (e, "Unexpected Exception");
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
        setInt() - Set an DECFLOAT34 parameter.
        **/
    public void Var045()
    {
      if (checkDecFloatSupport()) { 
        JDSerializeFile pstestSetdfp = null;
        try {
          pstestSetdfp = JDPSTest.getPstestSetdfp34(connection_);
          String filename34 = pstestSetdfp.getName();
          statement_.executeUpdate ("DELETE FROM " + filename34);
          
          PreparedStatement ps = connection_.prepareStatement (
              "INSERT INTO " + filename34
              + "  VALUES (?)");
          ps.setDouble (1, -10012);
          ps.executeUpdate ();
          ps.close ();
          
          ResultSet rs = statement_.executeQuery ("SELECT * FROM " + filename34);
          rs.next ();
          double check = rs.getDouble (1);
          rs.close ();
          
          assertCondition (check == -10012);
        }
        catch (Exception e) {
          failed (e, "Unexpected Exception");
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
   * setDouble() - Set a SQLXML parameter.
   **/
  public void Var046() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSetxml(connection_);
    if (checkXmlSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " VALUES (?)");
        try {
          ps.setDouble(1, -5.54);
	  ps.execute();
          ps.close();
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      } catch (Exception e) {
        failed(e, "Unexpected exception");
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
   * setDouble() - Set an parameter for a column of a specified type.
   **/
  public void testSetDouble(String columnName, double value, String outputValue) {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
      statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + pstestSet.getName() + " (" + columnName + ") VALUES (?)");
      ps.setDouble(1, value);
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_.executeQuery(
          "SELECT " + columnName + " FROM " + pstestSet.getName());
      rs.next();
      String check = "" + rs.getString(1);
      rs.close();

      assertCondition(outputValue.equals(check),
          " got " + check + " sb " + outputValue+" from "+value);
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

  public void Var047() {
    if (checkBooleanSupport()) {
      testSetDouble("C_BOOLEAN", 1, "1");
    }
  }

  public void Var048() {
    if (checkBooleanSupport()) {
      testSetDouble("C_BOOLEAN", 0, "0");
    }
  }

  public void Var049() {
    if (checkBooleanSupport()) {
      testSetDouble("C_BOOLEAN", 101, "1");
    }
  }

  public void Var050() {
    if (checkBooleanSupport()) {
      testSetDouble("C_BOOLEAN", -1, "1");
    }
  }

  public void Var051() {
    if (checkBooleanSupport()) {
      testSetDouble("C_BOOLEAN", Double.MAX_VALUE, "1");
    }
  }

  public void Var052() {
    if (checkBooleanSupport()) {
      testSetDouble("C_BOOLEAN", Double.MIN_VALUE, "1");
    }
  }
  

  public void Var053() {
    if (checkBooleanSupport()) {
      testSetDouble("C_BOOLEAN", Double.NEGATIVE_INFINITY, "1");
    }
  }

  public void Var054() {
    if (checkBooleanSupport()) {
      testSetDouble("C_BOOLEAN", Double.POSITIVE_INFINITY, "1");
    }
  }






}
