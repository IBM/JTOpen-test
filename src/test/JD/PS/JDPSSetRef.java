///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSSetRef.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.PS;


import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDPSTest;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JDTestcase;
import test.JD.JDSerializeFile;



/**
Testcase JDPSSetRef.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setRef()
</ul>
**/
public class JDPSSetRef
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDPSSetRef";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDPSTest.main(newArgs); 
   }






    /**
    Constructor.
    **/
    public JDPSSetRef (AS400 systemObject,
                       Hashtable<String,Vector<String>> namesAndVars,
                       int runMode,
                       FileOutputStream fileOutputStream,
                       
                       String password)
    {
        super (systemObject, "JDPSSetRef",
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
    }



    /**
    Performs cleanup needed after running variations.
    
    @exception Exception If an exception occurs.
    **/
    protected void cleanup ()
    throws Exception
    {
        connection_.close ();
        connection_ = null; 

    }



    /**
    Test implementation of Ref for use in testing.
    **/
    private static class TestRef
    implements Ref {
        public String getBaseTypeName() throws SQLException     { return null;}
        @SuppressWarnings("rawtypes")
        public java.lang.Object getObject(java.util.Map map) throws SQLException { return null;} //@C1A
        public java.lang.Object getObject() throws SQLException { return null;} //@C1A
        public void setObject(java.lang.Object value) throws SQLException {}    //@C1A
    }


    /**
    setRef() - Should throw exception when the prepared
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
                                                                    + " (C_INTEGER) VALUES (?)");
                ps.close ();
                ps.setRef (1, new TestRef ());
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
    setRef() - Should throw exception when an invalid index is
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
                                                                    + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
                ps.setRef (100, new TestRef ());
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
    setRef() - Should throw exception when index is 0.
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
                                                                    + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
                ps.setRef (0, new TestRef ());
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
    setRef() - Should throw exception when index is -1.
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
                                                                    + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
                ps.setRef (0, new TestRef ());
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
    setRef() - Should throw exception when the value is null.
    **/
    public void Var005()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        if (checkJdbc20 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + pstestSet.getName()
                                                                    + " (C_INTEGER) VALUES (?)");
                ps.setRef (1, null);
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
    setRef() - Should throw an exception with a valid parameter index
    greater than 1.
    **/
    public void Var006()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc20 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + pstestSet.getName()
                                                                    + " (C_KEY, C_VARCHAR_50) VALUES (?, ?)");
                ps.setRef (2, new TestRef ());
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
    setRef() - Should throw exception when the parameter is
    not an input parameter.
    **/
    public void Var007()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        if (checkJdbc20 ()) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            notApplicable("JCC permits setting of an output parameter");
            return; 
          } 
            try {
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "CALL " + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
                ps.setRef (2, new TestRef ());
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


    public void testSetFailed(String column, Ref ref) {
	if (checkJdbc20 ()) {
	      JDSerializeFile pstestSet = null;
	      try {
	        pstestSet = JDPSTest.getPstestSet(connection_);
		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + pstestSet.getName()
								     + " ("+column+") VALUES (?)");
		ps.setRef (1, ref);
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
    }
    /**
    setRef() - Set a SMALLINT parameter.
    **/
    public void Var008()
    {
	testSetFailed("C_SMALLINT", new TestRef());

     
    }



    /**
    setRef() - Set a INTEGER parameter.
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
                                                                    + " (C_INTEGER) VALUES (?)");
                ps.setRef (1, new TestRef ());
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
    setRef() - Set a REAL parameter.
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
                                                                    + " (C_REAL) VALUES (?)");
                ps.setRef (1, new TestRef ());
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
    setRef() - Set a FLOAT parameter.
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
                                                                    + " (C_FLOAT) VALUES (?)");
                ps.setRef (1, new TestRef ());
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
    setRef() - Set a DOUBLE parameter.
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
                                                                    + " (C_DOUBLE) VALUES (?)");
                ps.setRef (1, new TestRef ());
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
    setRef() - Set a DECIMAL parameter.
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
                                                                    + " (C_DECIMAL_105) VALUES (?)");
                ps.setRef (1, new TestRef ());
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
    setRef() - Set a NUMERIC parameter.
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
                                                                    + " (C_NUMERIC_50) VALUES (?)");
                ps.setRef (1, new TestRef ());
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
    setRef() - Set a CHAR parameter.
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
                                                                    + " (C_CHAR_50) VALUES (?)");
                ps.setRef (1, new TestRef ());
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
    setRef() - Set a VARCHAR parameter.
    **/
    public void Var016()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc20 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + pstestSet.getName()
                                                                    + " (C_VARCHAR_50) VALUES (?)");
                ps.setRef (1, new TestRef ());
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
    setRef() - Set a CLOB parameter.
    **/
    public void Var017()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    PreparedStatement ps = connection_.prepareStatement (
                                                                        "INSERT INTO " + pstestSet.getName()
                                                                        + " (C_CLOB) VALUES (?)");
                    ps.setRef (1, new TestRef ());
                    ps.executeUpdate(); 
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
    setRef() - Set a DBCLOB parameter.
    **/
    public void Var018()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    PreparedStatement ps = connection_.prepareStatement (
                                                                        "INSERT INTO " + pstestSet.getName()
                                                                        + " (C_DBCLOB) VALUES (?)");
                    ps.setRef (1, new TestRef ());
                    ps.executeUpdate(); 
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
    setRef() - Set a BINARY parameter.
    **/
    public void Var019()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc20 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + pstestSet.getName()
                                                                    + " (C_BINARY_20) VALUES (?)");
                ps.setRef (1, new TestRef ());
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
    setRef() - Set a VARBINARY parameter.
    **/
    public void Var020()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc20 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + pstestSet.getName()
                                                                    + " (C_VARBINARY_20) VALUES (?)");
                ps.setRef (1, new TestRef ());
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
    setRef() - Set a BLOB parameter.
    **/
    public void Var021()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        if (checkJdbc20 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + pstestSet.getName()
                                                                    + " (C_BLOB) VALUES (?)");
                ps.setRef (1, new TestRef ());
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
    setRef() - Set a DATE parameter.
    **/
    public void Var022()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        if (checkJdbc20 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + pstestSet.getName()
                                                                    + " (C_DATE) VALUES (?)");
                ps.setRef (1, new TestRef ());
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
    setRef() - Set a TIME parameter.
    **/
    public void Var023()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        if (checkJdbc20 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + pstestSet.getName()
                                                                    + " (C_TIME) VALUES (?)");
                ps.setRef (1, new TestRef ());
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
    setRef() - Set a TIMESTAMP parameter.
    **/
    public void Var024()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        if (checkJdbc20 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement (
                                                                    "INSERT INTO " + pstestSet.getName()
                                                                    + " (C_TIMESTAMP) VALUES (?)");
                ps.setRef (1, new TestRef ());
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
    setRef() - Set a DATALINK parameter.
    **/
    public void Var025()
    {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
        if (checkJdbc20 ()) {
            if (checkDatalinkSupport ()) {
                try {
                    PreparedStatement ps = connection_.prepareStatement (
                                                                        "INSERT INTO " + pstestSet.getName()
                                                                        + " (C_DATALINK) VALUES (?)");
                    ps.setRef (1, new TestRef ());
                    ps.executeUpdate(); 
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
    setRef() - Set a DISTINCT parameter.
    **/
    public void Var026()
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
                    ps.setRef (1, new TestRef ());
                    ps.executeUpdate(); 
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
    setRef() - Set a BIGINT parameter.
    **/
    public void Var027()
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
                    ps.setRef (1, new TestRef ());
                    ps.executeUpdate(); 
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
    setRef() - Set a DEFLOAT parameter.
    **/
    public void Var028()
    {
        if (checkJdbc20 ()) {
            if (checkDecFloatSupport()) {
              JDSerializeFile pstestSetdfp = null;
              try {
                pstestSetdfp = JDPSTest.getPstestSetdfp16(connection_);
                String tablename16=pstestSetdfp.getName();
                    PreparedStatement ps = connection_.prepareStatement (
                                                                        "INSERT INTO " + tablename16
                                                                        + " VALUES (?)");
                    ps.setRef (1, new TestRef ());
                    ps.executeUpdate(); 
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
    }

    /**
    setRef() - Set a DEFLOAT parameter.
    **/
    public void Var029()
    {
        if (checkJdbc20 ()) {
            if (checkDecFloatSupport()) {
              JDSerializeFile pstestSetdfp = null;
              try {
                pstestSetdfp = JDPSTest.getPstestSetdfp34(connection_);
                String tablename34=pstestSetdfp.getName();
                    PreparedStatement ps = connection_.prepareStatement (
                                                                        "INSERT INTO " + tablename34
                                                                        + " VALUES (?)");
                    ps.setRef (1, new TestRef ());
                    ps.executeUpdate(); 
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
    }


    /**
    setRef() - Set a SQLXML parameter.
    **/
    public void Var030()
    {
	if (checkXmlSupport ()) {
	      JDSerializeFile pstestSetxml = null;
	      try {
	        pstestSetxml = JDPSTest.getSerializeFile(connection_, JDPSTest.SETXML);
	        String tablename = pstestSetxml.getName(); 
		PreparedStatement ps =
		  connection_.prepareStatement ("INSERT INTO " +
						tablename
						+ " VALUES (?)");

		try { 
		    ps.setRef (1, new TestRef ());
		    ps.executeUpdate(); 
		    failed ("Didn't throw SQLException");
		}
		catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }catch (Exception e) {
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
    setRef() - Set a BOOLEAN parameter.
    **/
    public void Var031()
    {
	if (checkBooleanSupport()) { 
	    testSetFailed("C_BOOLEAN", new TestRef());
	}
     
    }




}



