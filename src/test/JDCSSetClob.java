///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSSetClob.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSSetClob.java
//
// Classes:      JDCSSetClob
//
////////////////////////////////////////////////////////////////////////

package test;

import java.io.FileOutputStream;

import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;


/**
Testcase JDCSSetClob.  This tests the following
method of the JDBC CallableStatement class:


     setClob()

**/
public class JDCSSetClob
extends JDCSSetTestcase
{
   private static int LARGE_CLOB_SIZE = 20000000; 


  


/**
Constructor.
**/
    public JDCSSetClob (AS400 systemObject,
                          Hashtable namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDCSSetClob",
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
      super.setup(); 
          }

/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
      super.setup(); 
    }

/**
Compares a Clob with a String.
**/
    private boolean compare (Clob i, String b)
        throws SQLException
    {
        return i.getSubString (1, (int) i.length ()).equals (b);            
    }


/**
setClob() - Set parameter PARAM_1 which doesn't exist.
- Using an ordinal parameter
**/
    public void Var001()
    {
        if(checkJdbc20())
        {
            try {
		cs.setClob(0, new JDLobTest.JDTestClob ("Test Clob"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e){
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setClob() - Set parameter PARAM_1 which doesn't exist.
- Using an ordinal parameter
**/
    public void Var002()
    {
        if(checkJdbc20())
        {
	    try{
		cs.setClob(-1, new JDLobTest.JDTestClob("Test Clob"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setClob() - Set parameter PARAM_1 which is too big.
- Using an ordinal parameter
**/
    public void Var003()
    {
        if(checkJdbc20())
        {
	    try {
                cs.setClob(35, new JDLobTest.JDTestClob("Test Clob"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setClob() - Should throw an exception when the callable statment is closed.
- Using an ordinal parameter
**/
    public void Var004()
    {
        if(checkJdbc20())
        {
            try
            {
		cs.close();
                cs.setClob(1, new JDLobTest.JDTestClob("Test Clob"));
                failed("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setClob() - Should work with a valid parameter name.
- Using an ordinal parameter
**/
    public void Var005 ()
    {
        if(checkJdbc20()) {

            try {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		 
		cs.setClob(20, new JDLobTest.JDTestClob("Test Clob"));
                cs.execute();
                Clob check = cs.getClob(20);
                cs.close();
                assertCondition(compare (check, "Test Clob"));
            }
            catch(Exception e){
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setClob() - Should work with a valid parameter name.
- Using an ordinal parameter
**/
    public void Var006 ()
    {
        if(checkJdbc20())
        {

            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setClob(20, (java.sql.Clob) null);
                cs.execute();
                Clob check = cs.getClob(20);
		boolean wn = cs.wasNull();
                assertCondition ((check == null) && (wn == true));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
setClob() - Should throw exception when the parameter is
not an input parameter.
- Using an ordinal parameter
**/
    public void Var007 ()
    {
        if(checkJdbc20())
        {
            try {
                CallableStatement cs1 = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + "( ?, ?, ?)");

		cs1.setClob(2, new JDLobTest.JDTestClob("Test Clob"));
                cs1.close();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }

    }

/**
setClob() - Set a SMALLINT parameter.
- Using an ordinal parameter
**/
    public void Var008()
    {
        if(checkJdbc20())
        {
            try {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());

		cs.setClob(1, new JDLobTest.JDTestClob("Test Clob"));
                cs.execute();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e){
                try{
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                    cs.close();
                }
                catch (SQLException s) {
                    failed (s, "Unexpected Exception");
                }
	    }
	}
    }

/**
setClob() - Set a INTEGER parameter.
- Using an ordinal parameter
**/
    public void Var009()
    {
        if(checkJdbc20())
        {
            try {

                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());

		cs.setClob(2, new JDLobTest.JDTestClob("Test Clob"));
                cs.execute();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e){
                try{
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                    cs.close();
                }
                catch (SQLException s) {
                    failed (s, "Unexpected Exception");
                }
	    }
	}
    }

/**
setClob() - Set a REAL parameter.
- Using an ordinal parameter
**/
    public void Var010()
    {
        if(checkJdbc20())
        {
            try {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		
		cs.setClob(3, new JDLobTest.JDTestClob("Test Clob"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e) {
                try{
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                    cs.close();
                }
                catch (SQLException s) {
                    failed (s, "Unexpected Exception");
                }
            }
        }
    }

/**
setClob() - Set a FLOAT parameter.
- Using an ordinal parameter
**/
    public void Var011()
    {
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		
                cs.setClob(4, new JDLobTest.JDTestClob("Test Clob"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                try{
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                    cs.close();
                }
                catch (SQLException s)
                {
                    failed (s, "Unexpected Exception");
                }
            }
        }
    }

/**
setClob() - Set a DOUBLE parameter.
- Using an ordinal parameter
**/
    public void Var012 ()
    {
        if(checkJdbc20())
        {
            try {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		
                cs.setClob(5, new JDLobTest.JDTestClob("Test Clob"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e) {
                try{
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                    cs.close();
                }
                catch (SQLException s) {
                    failed (s, "Unexpected Exception");
                }
            }
        }
    }

/**
setClob() - Set a DECIMAL parameter.
- Using an ordinal parameter
**/
    public void Var013 ()
    {
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		
                cs.setClob(6, new JDLobTest.JDTestClob("Test Clob"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                try{
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                    cs.close();
                }
                catch (SQLException s)
                {
                    failed (s, "Unexpected Exception");
                }
            }
        }
    }

/**
setClob() - Set a NUMERIC parameter
- Using an ordinal parameter
**/
    public void Var014 ()
    {
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		
                cs.setClob(8, new JDLobTest.JDTestClob("Test Clob"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                try{
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                    cs.close();
                }
                catch (SQLException s)
                {
                    failed (s, "Unexpected Exception");
                }
            }
        }
    }

/**
setClob() - Set a CHAR(1) parameter.
- Using an ordinal parameter
**/
    public void Var015 ()
    {
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setClob(10, new JDLobTest.JDTestClob("Test Clob"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                try{
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                    cs.close();
                }
                catch (SQLException s)
                {
                    failed (s, "Unexpected Exception");
                }            }
	}
    }
/**
setClob() - Set a CHAR(50) parameter.
- Using an ordinal parameter
**/
    public void Var016 ()
    {
        if(checkJdbc20())
        {
            try{
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());

		cs.setClob(11, new JDLobTest.JDTestClob("Test Clob"));
		cs.execute();
		Clob check = cs.getClob(11);
		failed("Didn't Throw SQLException check="+check);
	    }
	    catch(Exception e){ 

		try{
		   assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		    cs.close();
		    }
		catch (SQLException s){
		    failed (s, "Unexpected Exception");
		    }
	    }
	}
    }
/**
setClob() - Set a VARCHAR(50) parameter.
- Using an ordinal parameter
**/
    public void Var017 ()
    {
        if(checkJdbc20())
        {
            try{
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		
                cs.setClob(12, new JDLobTest.JDTestClob("Test Clob"));
		cs.execute();
		Clob check = cs.getClob(12);
		failed("Didn't Throw SQLException check="+check);
	    }
	    catch(Exception e){
		try{
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		    cs.close();
		    }
		catch (SQLException s){
		    failed (s, "Unexpected Exception");
		    }
	    }
	}
    }
    
/**
setClob() - Set a CLOB parameter.
- Using an ordinal parameter
**/
    public void Var018 ()
    {
        if(checkJdbc20())
        {
            if(checkLobSupport ())
            {
                try{
		    cs=connection_.prepareCall(sql);
                    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_);
                    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		    
                    cs.setClob(20, new JDLobTest.JDTestClob("Test Clob Test"));
		    cs.execute();
		    Clob check=cs.getClob(20);
		    cs.close();
		    assertCondition (compare (check, "Test Clob Test"));
		}
		catch(Exception e){
		   failed (e, "Unexpected Exception");
		}               
	    }	
	}
    }
    

/**
setClob() - Set a Clob parameter.
- Using an ordinal parameter
**/
    public void Var019()
    {
        if(checkJdbc20())
        {
            try{
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		
                cs.setClob(19, new JDLobTest.JDTestClob("Test Clob"));
		failed("Didn't throw SQLException");
	    }catch(Exception e)
            {
		try{
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		    cs.close();
		    }
		catch (SQLException s){
		    failed (s, "Unexpected Exception");
		    }
  
            }
	}
    }

/**
setClob() - Set a DATE parameter to a value that is not a valid date.  This should throw an exception. 
- Using an ordinal parameter
**/
    public void Var020()
    {
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());

		cs.setClob(15,  new JDLobTest.JDTestClob ("Test Clob"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                try{
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                    cs.close();
                }
                catch (SQLException s)
                {
                    failed (s, "Unexpected Exception");
                }
            }
        }
    }

/**
setClob() - Set a TIME parameter to a value that is not a valid date.  This should throw an exception. 
- Using an ordinal parameter
**/
    public void Var021()
    {
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		
                cs.setClob(16,  new JDLobTest.JDTestClob ("Test Clob"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                try{
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                    cs.close();
                }
                catch (SQLException s)
                {
                    failed (s, "Unexpected Exception");
                }
            }
        }
    }

/**
setClob() - Set a TIMESTAMP parameter to a value that is not valid.  This should throw an exception.
- Using an ordinal parameter
**/
    public void Var022()
    {
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		
                cs.setClob(17,  new JDLobTest.JDTestClob ("Test Clob"));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                try{
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                    cs.close();
                }
                catch (SQLException s)
                {
                    failed (s, "Unexpected Exception");
                }
            }
        }
    }

/**
setClob() - Set a DATALINK parameter.
- Using an ordinal parameter
**/
    public void Var023()
    {
        if(checkJdbc20())
        {
            if(checkLobSupport ())
            {
                try {
                    cs=connection_.prepareCall(sql);
                    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                         supportedFeatures_);
                    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		    
		    cs.setClob(18, new JDLobTest.JDTestClob ("Test Clob"));
                    cs.execute ();
		    Clob check = cs.getClob(18);
		    assertCondition (compare (check, "Test Clob"));
                }
                catch(Exception e){
		    try{
			assertExceptionIsInstanceOf(e, "java.sql.SQLException");
			cs.close();
		    }
		    catch(SQLException s){
			failed (s, "Unexpected Exception");

		    }
		}
	    }
	}
    }

/**
setClob() - Set a BIGINT parameter.
- Using an ordinal parameter
**/
    public void Var024()
    {
        if(checkJdbc20())
        {
            if(checkBigintSupport())
            {
                try
                {
                    cs=connection_.prepareCall(sql);
                    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                         supportedFeatures_);
                    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		    
                    cs.setClob(22,  new JDLobTest.JDTestClob ("Test Clob"));
                    failed ("Didn't throw SQLException");
                }
                catch(Exception e)
                {
                    try{
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                    cs.close();
		    }
		    catch (SQLException s){
                    failed (s, "Unexpected Exception");
		    }
		}
	    }
	}
    }

/**
setClob() - Set a BINARY parameter, should throw error 
- Using an ordinal parameter
**/
    public void Var025()
    {
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());

		
		cs.setClob (13,  new JDLobTest.JDTestClob ("Test Clob"));
                cs.execute();
		Clob check = cs.getClob(13);
		assertCondition (compare (check,"Test Clob           "));
            }
            catch(Exception e)
            {
                try{
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                    cs.close();
                }
                catch (SQLException s)
                {
                    failed (s, "Unexpected Exception");
                }
            }
        }
    }

/**
setClob() - Set a VARBINARY parameter should throw error
- Using an ordinal parameter
**/
    public void Var026()
    {
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		
                cs.setClob (14,  new JDLobTest.JDTestClob ("Test Clob"));
                cs.execute();
		Clob check = cs.getClob(14);
		assertCondition (compare (check,"Test Clob"));

            }
            catch(Exception e)
            {
                try{
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                    cs.close();
                }
                catch (SQLException s)
                {
                    failed (s, "Unexpected Exception");
                }
            }
        }
    }
/**
setClob() - Set more than one parameter.
- Using an ordinal parameter
**/
    public void Var027()
    {
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		// String b = " clob test";
		// String c = "new clob test";
		cs.setClob(20,  new JDLobTest.JDTestClob (" clob test"));
                cs.setClob(20,  new JDLobTest.JDTestClob ("new clob test"));
                cs.execute();
                Clob check=cs.getClob(20);
                cs.close();
                assertCondition (compare (check, "new clob test"));
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception");
            }
        }
    }

/**
setClob() - Should set to SQL NULL when the value is null.
- Using an ordinal parameter
**/
    public void Var028()
    {
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setClob (20, (java.sql.Clob) null);
                cs.execute ();
                Clob check = cs.getClob (20);
                assertCondition ((check == null));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
	}
    }
    
    /**
   setClob() - Using a LARGE CLOB
   **/
  public void Var029() {
    String baseProcName = "JDCSSC029";
    String procName = JDCSTest.COLLECTION + "." + baseProcName;

    if (checkJdbc20()) {
      if (isToolboxDriver()) {
        notApplicable("TOOLBOX can't handle large blobs on 32-bit platforms");
      } else {

        String sql1 = "";
        try {
          Statement stmt = connection_.createStatement();

          try {
            stmt.executeUpdate("drop procedure " + procName);
          } catch (Exception e) {
          }

          StringBuffer sb = new StringBuffer();
          for (int i = 0; i < LARGE_CLOB_SIZE; i++) {
            sb.append('A');

          }
          Clob inClob = new JDLobTest.JDTestClob(sb.toString());

          sql1 = "CREATE PROCEDURE " + procName + "(IN B CLOB("
              + LARGE_CLOB_SIZE + "), "
              + " OUT I INT, OUT C CHAR(1)) LANGUAGE SQL " + "SPECIFIC "
              + baseProcName + " XXXX1: BEGIN " + "SET I = LENGTH(B); "
              + "SET C = SUBSTRING(B, 1,1); " + "END XXXX1";

          stmt.executeUpdate(sql1);
          sql1 = "{call " + procName + " (?,?,?)}";
          CallableStatement cstmt = connection_.prepareCall(sql1);
          cstmt.setClob(1, inClob);
          cstmt.registerOutParameter(2, java.sql.Types.INTEGER);
          cstmt.registerOutParameter(3, java.sql.Types.CHAR);
          cstmt.execute();
          int answer = cstmt.getInt(2);
          String s = cstmt.getString(3);
          assertCondition((answer == LARGE_CLOB_SIZE) && (s.equals("A")),
              "Error answer is " + answer + " sb " + LARGE_CLOB_SIZE + " s="
                  + s + " sb A  -- Added by native driver 12/14/2005");
        } catch (Exception e) {
          failed(e, "Unexpected Exception -- added by native driver 12/14/2005");
        }
      }
    }
  }


    public void testNamedParameters(String procedureName,
					   String procedureDefinition,
					   String parameterName,
					   String parameterValue) {

	String added=" -- added by native driver 6/24/2015 to test named arguments";
	if (checkJdbc40()) { 
	if (checkRelease710()) {

		try {
		    Statement stmt = connection_.createStatement();
		    try {
			stmt.executeUpdate("DROP PROCEDURE "+procedureName);
		    } catch (Exception e) {
		    // e.printStackTrace();
		    }
		    stmt.executeUpdate("CREATE PROCEDURE "+procedureName+ procedureDefinition);

		    CallableStatement cs;
		    cs = connection_.prepareCall( "CALL " + procedureName + "(?, "+parameterName+" => ?)");

		    cs.registerOutParameter(1,java.sql.Types.CLOB);

		  
		    JDReflectionUtil.callMethod_V(cs, "setClob", parameterName, new JDLobTest.JDTestClob(parameterValue) );

		    cs.execute();
		    String value=cs.getString(1);
		    String expectedValue = parameterValue.toString();
		    stmt.executeUpdate("DROP PROCEDURE "+procedureName);
		    cs.close();
		    stmt.close(); 
		    assertCondition(expectedValue.equals(value), "value("+value+") != "+expectedValue+added);

		} catch (Exception e) {
		    failed (e, "Unexpected Exception "+added);
		}
	    }



    } 
    }


    public void Var030() {
	testNamedParameters( JDCSTest.COLLECTION+".JDCSSCL30",
			     "(OUT OUTARG CLOB(10M) , IN INARG CLOB(10M) ) BEGIN SET OUTARG=INARG; END ",
			     "INARG",
			     "Test 30");


    } 

    
    
    public void Var031() {
  testNamedParameters( JDCSTest.COLLECTION+".JDCSSCL31",
           "(OUT OUTARG CLOB(10M) , IN INARG CLOB(10M), IN UNUSED1 CLOB(10M) default '1234', IN UNUSED2 CLOB(10M) default '1234', IN UNUSED3 CLOB(10M) default '1234', IN UNUSED4 CLOB(10M) default '1234', IN UNUSED5 CLOB(10M) default '1234' ) BEGIN SET OUTARG=INARG; END ",
           "INARG",
           "Test 31");

    } 

    
    public void Var032() {
  testNamedParameters( JDCSTest.COLLECTION+".JDCSSCL32",
           "(OUT OUTARG CLOB(10M) ,  IN UNUSED1 CLOB(10M) default '1234', IN UNUSED2 CLOB(10M) default '1234', IN INARG CLOB(10M), IN UNUSED3 CLOB(10M) default '1234', IN UNUSED4 CLOB(10M) default '1234', IN UNUSED5 CLOB(10M) default '1234' ) BEGIN SET OUTARG=INARG; END ",
           "INARG",
		       "test 32");

    } 

    
    public void Var033() {
  testNamedParameters( JDCSTest.COLLECTION+".JDCSSCL33",
           "(OUT OUTARG CLOB(10M) ,  IN UNUSED1 CLOB(10M) default '1234', IN UNUSED2 CLOB(10M) default '1234', IN UNUSED3 CLOB(10M) default '1234', IN UNUSED4 CLOB(10M) default '1234', IN UNUSED5 CLOB(10M) default '1234', IN INARG CLOB(10M) ) BEGIN SET OUTARG=INARG; END ",
           "INARG",
		       "Test 33");

    } 

    /**
     * setClob() - Set a BOOLEAN parameter. - Using an ordinal parameter
     */
    public void Var034()
    {

        if (checkBooleanSupport())
        {
          
          setTestFailure("setClob",27,   new JDLobTest.JDTestClob("TEST") , "Added 2021/01/07 to test boolean support");
        }

    }
    
    /**
     * setClob() - Set a BOOLEAN parameter. - Using a named parameter
     */
    public void Var035()
    {

        if (checkBooleanSupport())
        {
          
          setTestFailure("setClob","P_BOOLEAN",   new JDLobTest.JDTestClob("TEST") , "Added 2021/01/07 to test boolean support");
        }

    }

   


}
