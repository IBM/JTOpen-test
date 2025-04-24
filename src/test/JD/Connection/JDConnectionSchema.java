///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionSchema.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDConnectionSchema.java
//
// Classes:      JDConnectionSchema
//
////////////////////////////////////////////////////////////////////////

package test.JD.Connection;

import com.ibm.as400.access.AS400;

import test.JDConnectionTest;
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;
import test.JD.JDSetupCollection;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable; import java.util.Vector;



/**
Testcase JDConnectionSchema.  his tests the following methods
of the JDBC Connection class:

<ul>
<li>setSchema()</li>
<li>getSchema()</li>
</ul>
**/
public class JDConnectionSchema extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDConnectionSchema";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDConnectionTest.main(newArgs); 
   }



    // Private data.
  
    private Connection conn_ = null;
    private Connection systemNamingConn_ = null; 
    private String baseCollection_ = ""; 
    StringBuffer sb = new StringBuffer(); 


/**
Constructor.
**/
    public JDConnectionSchema (AS400 systemObject,
                              Hashtable<String,Vector<String>> namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password)
    {
        super (systemObject, "JDConnectionSchema",
               namesAndVars, runMode, fileOutputStream,
               password);
    }


    protected void setupSchema(String collection) throws SQLException {
 
	Statement s = conn_.createStatement();
	JDSetupCollection.create(conn_, collection, false);


	try {
	    s.executeUpdate("DROP TABLE "+collection+".INFO"); 
	} catch (Exception e) {
	}

	try {
	    s.executeUpdate("CREATE TABLE "+collection+".INFO (INFO VARCHAR(80))");
	    s.executeUpdate("INSERT INTO "+collection+".INFO VALUES('"+collection+"')"); 
	} catch (Exception e) {
	    System.out.println("WARNING: setupSchema errors");
	    e.printStackTrace(); 
	}
    }

/**
Setup.

@exception Exception If an exception occurs.
**/
    protected void setup () throws Exception  {

        conn_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);

	// Create schemas A B C D
	baseCollection_ = JDConnectionTest.COLLECTION;
	if (baseCollection_.length() > 9) {
	    throw new Exception("collection '"+baseCollection_+"' is too long"); 
	}
	for (char a = 'A'; a <= 'F'; a++) {
	    setupSchema(baseCollection_+a); 
	}
        conn_.commit();
        
        systemNamingConn_ = testDriver_.getConnection (baseURL_+";naming=system", userId_, encryptedPassword_);
 
        
    }



/**
Cleanup.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
	conn_.close();
	if (systemNamingConn_ != null) systemNamingConn_.close(); 
    }


    public boolean checkSchema(Statement s, String schema, StringBuffer sb1 ) throws Exception {
        boolean passed = true; 
	ResultSet rs = s.executeQuery("Select * from INFO");
	rs.next();
	String outSchema = rs.getString(1);
	rs.close();
	s.close();
	passed = schema.equals(outSchema); 
	if (!passed) {
	  sb1.append("Got "+outSchema+" Expected "+schema+"\n");
	}
	return passed; 
    }

    public boolean checkSchema(PreparedStatement ps, String schema, StringBuffer sb1 ) throws Exception {
      boolean passed = true; 
      ResultSet rs = ps.executeQuery(); 
      rs.next();
      String outSchema = rs.getString(1);
      rs.close();
      passed = schema.equals(outSchema); 
      if (!passed) {
        sb1.append("Got "+outSchema+" Expected "+schema+"\n");
      }
      return passed; 
  }



/**
setSchema -- Make current correct schema is used. 
**/
    public void Var001 ()
    {
        try {
            sb.setLength(0); 
	    Statement s = conn_.createStatement();
	    s.executeUpdate("set schema "+  baseCollection_+"F");
	    String schema =  baseCollection_+"A"; 
	    JDReflectionUtil.callMethod_V(conn_, "setSchema", schema);
	    s.close(); 
	    s = conn_.createStatement(); 
	    boolean passed = checkSchema(s, schema, sb);
	    s.close(); 
	    assertCondition (passed,sb); 

        } catch (Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
setSchema() -- has no effect on previous prepared statements 
**/
    public void Var002 ()
    {
        try {
          boolean passed = true; 
          sb.setLength(0); 
          Statement s = conn_.createStatement();
          String ps1Schema = baseCollection_+"A"; 
          s.executeUpdate("set schema "+  ps1Schema);
          PreparedStatement ps1 = conn_.prepareStatement("SELECT * FROM INFO");
          String ps2Schema =  baseCollection_+"B"; 
          s.executeUpdate("set schema "+  ps2Schema);
          PreparedStatement ps2 = conn_.prepareStatement("SELECT * FROM INFO");
          String ps3Schema =  baseCollection_+"C"; 
          s.executeUpdate("set schema "+  ps3Schema);
          PreparedStatement ps3 = conn_.prepareStatement("SELECT * FROM INFO");
          String schema =  baseCollection_+"A"; 
          JDReflectionUtil.callMethod_V(conn_, "setSchema", schema);
          if (! checkSchema(ps1, ps1Schema, sb)) passed=false; 
          if (! checkSchema(ps2, ps2Schema, sb)) passed=false; 
          if (! checkSchema(ps3, ps3Schema, sb)) passed=false;
          s.close(); 
          ps1.close(); 
          ps2.close(); 
          ps3.close(); 
          assertCondition (passed,sb); 
          
        } catch (Exception e) {
            failed(e, "Unexpected Exception");
        }
    }



/**
setSchema() -- throw exception on a closed connetion 
**/
    public void Var003 ()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            c.close ();
            String schema =  baseCollection_+"A"; 
            JDReflectionUtil.callMethod_V(c, "setSchema", schema);
            assertCondition (false, "Expected exception on close connection");
        }
        catch (Exception e) {
	  // Toolbox expected message 
	  String expected = "The connection does not exist.";
	  String expectedClass = "SQLNonTransientConnectionException";
	  if (! isJdbc40()) {
	      expectedClass = "SQLException"; 
	  }
	  if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
	       expected = "Function sequence error."; 
	       expectedClass = "DB2JDBCException"; 
	  } 
          assertExceptionIs(e, expectedClass, expected);

        }
        
    }


    
    
    /**
    setSchema -- Make current correct schema is used with system naming 
    **/
        public void Var004 ()
        {
            try {
                sb.setLength(0); 
                Statement s = systemNamingConn_.createStatement();
                s.executeUpdate("set schema "+  baseCollection_+"F");
                String schema =  baseCollection_+"A"; 
                JDReflectionUtil.callMethod_V(systemNamingConn_, "setSchema", schema);
                s.close(); 
                s = systemNamingConn_.createStatement(); 
                boolean passed = checkSchema(s, schema, sb);
                s.close(); 
                assertCondition (passed,sb); 

            } catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }



    /**
    setSchema() -- has no effect on previous prepared statements with system naming 
    **/
        public void Var005 ()
        {
            try {
              boolean passed = true; 
              sb.setLength(0); 
              Statement s = systemNamingConn_.createStatement();
              String ps1Schema = baseCollection_+"A"; 
              s.executeUpdate("set schema "+  ps1Schema);
              PreparedStatement ps1 = systemNamingConn_.prepareStatement("SELECT * FROM INFO");
              String ps2Schema =  baseCollection_+"B"; 
              s.executeUpdate("set schema "+  ps2Schema);
              PreparedStatement ps2 = systemNamingConn_.prepareStatement("SELECT * FROM INFO");
              String ps3Schema =  baseCollection_+"C"; 
              s.executeUpdate("set schema "+  ps3Schema);
              PreparedStatement ps3 = systemNamingConn_.prepareStatement("SELECT * FROM INFO");
              String schema =  baseCollection_+"A"; 
              JDReflectionUtil.callMethod_V(systemNamingConn_, "setSchema", schema);
              if (! checkSchema(ps1, ps1Schema, sb)) passed=false; 
              if (! checkSchema(ps2, ps2Schema, sb)) passed=false; 
              if (! checkSchema(ps3, ps3Schema, sb)) passed=false;
              s.close(); 
              ps1.close(); 
              ps2.close(); 
              ps3.close(); 
              assertCondition (passed,sb); 
              
            } catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }



    
    
/**
setSchema() -- TODO:  throws exception on database error. 
**/
    public void Var006 ()
    {
        try {
           // TODO:  Find a way to get a DB error on set schema
           assertCondition(true); 
        }
        catch (Exception e) {
            failed(e, "Unexpected Exception");
        }
    }


    



/**
getSchema() -- should return value set by setSchema()
**/
    public void Var007 ()
    {
        try {
          sb.setLength(0); 
          Statement s = conn_.createStatement();
          s.executeUpdate("set schema "+  baseCollection_+"F");
          String schema =  baseCollection_+"A"; 
          JDReflectionUtil.callMethod_V(conn_, "setSchema", schema);
          String outSchema = JDReflectionUtil.callMethod_S(conn_, "getSchema"); 
          
          assertCondition(outSchema.equals(schema), "Got "+outSchema+" expected "+schema); 
        }
        catch (Exception e) {
            failed(e, "Unexpected Exception");
        }
    }

    /**
    getSchema() -- should return value set by set schema sql statement ()
    **/
        public void Var008 ()
        {
            try {
              sb.setLength(0); 
              Statement s = conn_.createStatement();
              JDReflectionUtil.callMethod_V(conn_, "setSchema", baseCollection_+"F");
              String schema =  baseCollection_+"A"; 
              s.executeUpdate("set schema "+  schema);
              String outSchema = JDReflectionUtil.callMethod_S(conn_, "getSchema"); 
              
              assertCondition(outSchema.equals(schema), "Got "+outSchema+" expected "+schema); 
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }

        /**
        getSchema() -- should return value set by setSchema() for system naming 
        **/
            public void Var009 ()
            {
                try {
                  sb.setLength(0); 
                  Statement s = systemNamingConn_.createStatement();
                  s.executeUpdate("set schema "+  baseCollection_+"F");
                  String schema =  baseCollection_+"A"; 
                  JDReflectionUtil.callMethod_V(systemNamingConn_, "setSchema", schema);
                  String outSchema = JDReflectionUtil.callMethod_S(systemNamingConn_, "getSchema"); 
                  
                  assertCondition(outSchema.equals(schema), "Got "+outSchema+" expected "+schema); 
                }
                catch (Exception e) {
                    failed(e, "Unexpected Exception");
                }
            }

            /**
            getSchema() -- should return value set by set schema sql statement () for system naming
            **/
                public void Var010 ()
                {
                    try {
                      sb.setLength(0); 
                      Statement s = systemNamingConn_.createStatement();
                      JDReflectionUtil.callMethod_V(systemNamingConn_, "setSchema", baseCollection_+"F");
                      String schema =  baseCollection_+"A"; 
                      s.executeUpdate("set schema "+  schema);
                      String outSchema = JDReflectionUtil.callMethod_S(systemNamingConn_, "getSchema"); 
                      
                      assertCondition(outSchema.equals(schema), "Got "+outSchema+" expected "+schema); 
                    }
                    catch (Exception e) {
                        failed(e, "Unexpected Exception");
                    }
                }


/**
getSchema() should return error on close connection
**/ 
    public void Var011 ()
    {
      try {
        Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
        c.close ();
        String schema =  JDReflectionUtil.callMethod_S(c, "getSchema");
        assertCondition (false, "Expected exception on close connection but got "+schema);
    }
    catch (Exception e) {
	  // Toolbox expected message 
	  String expected = "The connection does not exist.";
	  String expectedClass = "SQLNonTransientConnectionException"; 
	  if (! isJdbc40()) {
	      expectedClass = "SQLException"; 
	  }
	  if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
	       expected = "Function sequence error."; 
	       expectedClass = "DB2JDBCException"; 
	  } 

          assertExceptionIs(e, expectedClass, expected);


    }


    }


/**
 getSchema() should return *LIBI when called against SYSTEM naming
**/
    /**
    getSchema() -- should return value set by set schema sql statement () for system naming
    **/
        public void Var012 ()
        {
            try {
		if (systemNamingConn_ != null) systemNamingConn_.close(); 
              systemNamingConn_ = testDriver_.getConnection (baseURL_+";naming=system", userId_, encryptedPassword_);
              
              String schema =  "*LIBL"; 
              String outSchema = JDReflectionUtil.callMethod_S(systemNamingConn_, "getSchema"); 
              
              assertCondition(outSchema.equals(schema), "Got "+outSchema+" expected "+schema); 
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }

	/**
	 get/setSchema should allow changing back to *LIBL
         **/


        public void Var013 ()
        {
          sb.setLength(0); 
          sb.append(" -- added by toolbox ");
	    boolean passed = true; 
            try {
		if (systemNamingConn_ != null) systemNamingConn_.close();
		sb.append("\nopened new system naming convention"); 
		systemNamingConn_ = testDriver_.getConnection (baseURL_+";naming=system", userId_, encryptedPassword_);

		sb.append("\ngetting schema");               
		String schema = JDReflectionUtil.callMethod_S(systemNamingConn_, "getSchema"); 
		if (! "*LIBL".equals(schema)) {
		    passed = false;
		    sb.append("\nGot "+schema+" sb *LIBL"); 
		}

		sb.append("\nsetting QGPL");               
		JDReflectionUtil.callMethod_V(systemNamingConn_, "setSchema", "QGPL");
		schema = JDReflectionUtil.callMethod_S(systemNamingConn_, "getSchema"); 
		if (! "QGPL".equals(schema)) {
		    passed = false;
		    sb.append("\nGot "+schema+" sb *QGPL"); 
		}

		sb.append("\nsetting *LIBL");               
		JDReflectionUtil.callMethod_V(systemNamingConn_, "setSchema", "*LIBL");
		schema = JDReflectionUtil.callMethod_S(systemNamingConn_, "getSchema"); 
		if (! "*LIBL".equals(schema)) {
		    passed = false;
		    sb.append("\nGot "+schema+" sb **LIBL"); 
		}

		sb.append("\nsetting QGPL");               
		JDReflectionUtil.callMethod_V(systemNamingConn_, "setSchema", "QGPL");
		schema = JDReflectionUtil.callMethod_S(systemNamingConn_, "getSchema"); 
		if (! "QGPL".equals(schema)) {
		    passed = false;
		    sb.append("\nGot "+schema+" sb *QGPL"); 
		}

		sb.append("\nsetting DEFAULT");               
		JDReflectionUtil.callMethod_V(systemNamingConn_, "setSchema", "DEFAULT");
		schema = JDReflectionUtil.callMethod_S(systemNamingConn_, "getSchema"); 
		if (! "*LIBL".equals(schema)) {
		    passed = false;
		    sb.append("\nGot "+schema+" sb **LIBL"); 
		}


              assertCondition(passed,sb); 
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception"+sb.toString());
            }
        }



}


