///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSSetBlob.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSSetBlob.java
//
// Classes:      JDCSSetBlob
//
////////////////////////////////////////////////////////////////////////
//
// 
//
////////////////////////////////////////////////////////////////////////

package test;

import java.awt.TextArea;
import java.io.FileOutputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;


/**
Testcase JDCSSetBlob.  This tests the following
method of the JDBC CallableStatement class:

setBlob()

**/
public class JDCSSetBlob
extends JDCSSetTestcase
{

	private static int LARGE_BLOB_SIZE=20000000; 




/**
Constructor.
**/
    public JDCSSetBlob (AS400 systemObject,
                          Hashtable namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDCSSetBlob",
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
      super.cleanup(); 
      
    }

    /**         //@K1A
Compares a Blob with a byte[].
**/
    private boolean compare (Blob i, byte[] b)
    throws SQLException
    {
        byte[] iBytes = i.getBytes (1, (int) b.length );      
        return areEqual (iBytes, b);
    }

/**
setBlob() - Set parameter PARAM_1 which doesn't exist.
- Using an ordinal parameter
**/
    public void Var001()
    {
        if(checkJdbc20())
        {
            try {
		byte[] b = new byte[] { (byte) 98, (byte) -111};
                cs.setBlob(0, new JDLobTest.JDTestBlob (b));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e){
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setBlob() - Set parameter PARAM_1 which doesn't exist.
- Using an ordinal parameter
**/
    public void Var002()
    {
        if(checkJdbc20())
        {
            try{
		byte[] b = new byte[] { (byte) 98, (byte) -111};
		cs.setBlob(-1, new JDLobTest.JDTestBlob(b));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setBlob() - Set parameter PARAM_1 which is too big.
- Using an ordinal parameter
**/
    public void Var003()
    {
        if(checkJdbc20())
        {
	    try {
		byte[] b = new byte[] { (byte) 98, (byte) -111};
                cs.setBlob(35, new JDLobTest.JDTestBlob(b));
                failed ("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setBlob() - Should throw an exception when the callable statment is closed.
- Using an ordinal parameter
**/
    public void Var004()
    {
        if(checkJdbc20())
        {
            try
            {
		byte[] b = new byte[] { (byte) 98, (byte) -111};
		cs.close();
                cs.setBlob(1, new JDLobTest.JDTestBlob(b));
                failed("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
setBlob() - Should work with a valid parameter name.
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
		  byte[] b = new byte[] { (byte) 1, (byte) -12, (byte) 57, (byte) 45,(byte) -33, (byte) 0};

		cs.setBlob(19, new JDLobTest.JDTestBlob(b));
                cs.execute();
                Blob check=cs.getBlob(19);
                cs.close();
                assertCondition (areEqual (b, check.getBytes (1, (int) check.length ())));
            }
            catch(Exception e){
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setBlob() - Should work with a valid parameter name.
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
                cs.setBlob(19, (java.sql.Blob) null);
                cs.execute();
                Blob check = cs.getBlob(19);
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
setBlob() - Should throw exception when the parameter is
not an input parameter.
- Using an ordinal parameter
**/
    public void Var007 ()
    {
        if(checkJdbc20())
        {
            try {
                CallableStatement cs1 = connection_.prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + "( ?, ?, ?)");

		byte[] b = new byte[] { (byte) 1, (byte) -12, (byte) 57};
		cs1.setBlob(2, new JDLobTest.JDTestBlob(b));
                cs1.close();
                failed ("Didn't throw SQLException");
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }

    }

/**
setBlob() - Set a SMALLINT parameter.
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

		byte[] b = new byte[] { (byte) 1, (byte) -12, (byte) 57};
		cs.setBlob(1, new JDLobTest.JDTestBlob(b));
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
setBlob() - Set a INTEGER parameter.
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
		byte[] b = new byte[] { (byte) 1, (byte) -12, (byte) 57};
		cs.setBlob(2, new JDLobTest.JDTestBlob(b));
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
setBlob() - Set a REAL parameter.
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
		byte[] b = new byte[] { (byte) 1, (byte) -12, (byte) 57};
		cs.setBlob(3, new JDLobTest.JDTestBlob(b));
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
setBlob() - Set a FLOAT parameter.
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
		byte[] b = new byte[] { (byte) 1, (byte) -12, (byte) 57};
                cs.setBlob(4, new JDLobTest.JDTestBlob(b));
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
setBlob() - Set a DOUBLE parameter.
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
		 byte[] b = new byte[] { (byte) 1, (byte) -12, (byte) 57};
                cs.setBlob(5, new JDLobTest.JDTestBlob(b));
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
setBlob() - Set a DECIMAL parameter.
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
		byte[] b = new byte[] { (byte) 1, (byte) -12, (byte) 57};
                cs.setBlob(6, new JDLobTest.JDTestBlob(b));
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
setBlob() - Set a NUMERIC parameter
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
		byte[] b = new byte[] { (byte) 1, (byte) -12, (byte) 57};
                cs.setBlob(8, new JDLobTest.JDTestBlob(b));
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
setBlob() - Set a CHAR(1) parameter.
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
		byte[] b = new byte[] { (byte) 1, (byte) -12, (byte) 57};
                cs.setBlob(10, new JDLobTest.JDTestBlob(b));
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
setBlob() - Set a CHAR(50) parameter.
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
		byte[] b = new byte[] { (byte) 1, (byte) -12, (byte) 57};
		cs.setBlob(11, new JDLobTest.JDTestBlob(b));
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
setBlob() - Set a VARCHAR(50) parameter.
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
		byte[] b = new byte[] { (byte) 1, (byte) -12, (byte) 57};
                cs.setBlob(12, new JDLobTest.JDTestBlob(b));
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
setBlob() - Set a CLOB parameter.
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
		    byte[] b = new byte[] { (byte) 1, (byte) -12, (byte) 57};
                    cs.setBlob(20, new JDLobTest.JDTestBlob(b));
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
    }
    

/**
setBlob() - Set a BLOB parameter.
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
		byte[] b = new byte[] { (byte) 1, (byte) -12, (byte) 57};
                cs.setBlob(19, new JDLobTest.JDTestBlob(b));
		cs.execute();
                Blob check=cs.getBlob(19);
                cs.close();
                assertCondition (areEqual (b, check.getBytes (1, (int) check.length ())));
	    }catch(Exception e)
            {
                    failed (e, "Unexpected Exception");  
            }
	}
    }

/**
setBlob() - Set a DATE parameter to a value that is not a valid date.  This should throw an exception. 
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
		byte[] b = new byte[] { (byte) 1, (byte) -12, (byte) 57};
                cs.setBlob(15,  new JDLobTest.JDTestBlob (b));
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
setBlob() - Set a TIME parameter to a value that is not a valid date.  This should throw an exception. 
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
		byte[] b = new byte[] { (byte) 1, (byte) -12, (byte) 57};
                cs.setBlob(16,  new JDLobTest.JDTestBlob (b));
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
setBlob() - Set a TIMESTAMP parameter to a value that is not valid.  This should throw an exception.
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
		  byte[] b = new byte[] { (byte) 1, (byte) -12, (byte) 57};
                cs.setBlob(17,  new JDLobTest.JDTestBlob (b));
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
setBlob() - Set a DATALINK parameter.
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
		    byte[] b = new byte[] { (byte) 1, (byte) -12, (byte) 57};
		    cs.setBlob(18, new JDLobTest.JDTestBlob (b));
                    cs.execute ();
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
setBlob() - Set a BIGINT parameter.
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
		    byte[] b = new byte[] { (byte) 1, (byte) -12, (byte) 57};
                    cs.setBlob(22,  new JDLobTest.JDTestBlob (b));
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
setBlob() - Set a BINARY parameter, should throw error 
- Using an ordinal parameter
**/
    public void Var025() {
	String info = " -- Update 01/03/2011 for native driver"; 
		if (checkJdbc20()) {
			try {
				cs = connection_.prepareCall(sql);
				JDSetupProcedure.setTypesParameters(cs,
						JDSetupProcedure.STP_CSINOUT, supportedFeatures_
						);
				JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
						supportedFeatures_, getDriver());
				byte[] b = new byte[] { (byte) 1, (byte) -12, (byte) 57,
						(byte) 44, (byte) 78 };
				cs.setBlob(13, new JDLobTest.JDTestBlob(b));
				cs.registerOutParameter(13, Types.BLOB); // @K2
				cs.execute();
					Blob v = cs.getBlob(13); // @K1A
					assertCondition(compare(v, b), info); // @K1A

			} catch (Exception e) {
					failed(e, "Unexpected Exception"+info); // @K1A
			}
		}
	}
    
     
/**
 * setBlob() - Set a VARBINARY parameter should throw error - Using an ordinal
 * parameter
 */
    public void Var026()
    {
	String info = " -- Update 01/03/2011 for native driver"; 

        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		byte[] b = new byte[] { (byte) 1, (byte) -12, (byte) 57, (byte) 44, (byte) 78}; 
                cs.setBlob (14,  new JDLobTest.JDTestBlob (b));
		cs.registerOutParameter(14, Types.BLOB);  //@K2
                cs.execute();
                    Blob v = cs.getBlob(14);                                //@K1A
                    assertCondition( compare (v, b), info );                  //@K1A

            }
            catch(Exception e)
            {
                    failed(e, "Unexpected Exception"+info );                   //@K1A
            }
        }
    }


/**
setBlob() - Set a BINARY parameter.
- Using an ordinal parameter
**/
    public void Var027()
    {
	String info = " -- Update 01/03/2011 for native driver"; 

	if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {
	    if(checkJdbc20())
	    {
		try
		{
		    cs=connection_.prepareCall(sql);
		    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
							 supportedFeatures_);
		    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		    byte[] b = new byte[] { (byte) 1, (byte) -12, (byte) 57, (byte) 44, (byte) 99};
		    cs.setBlob (13,  new JDLobTest.JDTestBlob (b));
                    cs.registerOutParameter(13, Types.BLOB);
		    cs.execute();
                        Blob v = cs.getBlob(13);                                //@K1A
                        assertCondition( compare (v, b), info );                      //@K1A
		}
		catch(Exception e)
		{
                        failed(e, "Unexpected Exception"+info);                      //@K1A
		}
	    }
	} else {
	    notApplicable(); 
	} 
    }

/**
setBlob() - Set a VARBINARY parameter.
- Using an ordinal parameter
**/
    public void Var028()
    {
	String info = " -- Update 01/03/2011 for native driver"; 

	if (getRelease() >=  JDTestDriver.RELEASE_V5R3M0) {
	    if(checkJdbc20())
	    {
		try{
		    cs=connection_.prepareCall(sql);
		    JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
							 supportedFeatures_);
		    JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		    byte[] b = new byte[] { (byte) 1, (byte) -12, (byte) 57, (byte) 44, (byte) 99};
		    cs.setBlob (14,  new JDLobTest.JDTestBlob (b));
                    cs.registerOutParameter(14, Types.BLOB);
		    cs.execute();
                        Blob v = cs.getBlob(14);                                //@K1A
                        assertCondition( compare (v, b), info );                      //@K1A
		}
		catch(Exception e){
                        failed(e, "Unexpected Exception"+info);                      //@K1A
		}
	    }
	} else {
	    notApplicable(); 
	} 
    }
    

/**
setBlob() - Set more than one parameter.
- Using an ordinal parameter
**/
    public void Var029()
    {
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
		byte[] b = new byte[] { (byte) 1, (byte) -12, (byte) 57};
		byte[] c = new byte[] { (byte) 23, (byte) -45, (byte) 98};
		cs.setBlob(19,  new JDLobTest.JDTestBlob (b));
                cs.setBlob(19,  new JDLobTest.JDTestBlob (c));
                cs.registerOutParameter(19, Types.BLOB);
                cs.execute();
                Blob check=cs.getBlob(19);
                cs.close();
                assertCondition (areEqual (c, check.getBytes (1, (int) check.length ())));
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception");
            }
        }
    }

/**
setBlob() - Should set to SQL NULL when the value is null.
- Using an ordinal parameter
**/
    public void Var030()
    {
        if(checkJdbc20())
        {
            try
            {
                cs=connection_.prepareCall(sql);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSINOUT,
                                                     supportedFeatures_);
                JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_, getDriver());
                cs.setBlob (19, (java.sql.Blob) null);
                cs.registerOutParameter(19, Types.BLOB);
                cs.execute ();
                Blob check = cs.getBlob (19);
                boolean wn = cs.wasNull ();
                assertCondition ((check == null) && (wn == true));
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
	}
    }
    
    /**
   setBlob() - With a very large blob
   **/
  public void Var031() {
    if (getRelease() < JDTestDriver.RELEASE_V5R3M0) {
      notApplicable("V5R3 or later test");
    } else {
      if(isToolboxDriver()) {
        notApplicable("TOOLBOX can't handle large blobs on 32-bit platforms"); 
      } else { 
      String baseProcName = "JDCSSB031";
      String procName = JDCSTest.COLLECTION + "." + baseProcName;
      if (checkJdbc20()) {
        String sql1 = "";
        try {
          Statement stmt = connection_.createStatement();

          try {
            stmt.executeUpdate("drop procedure " + procName);
          } catch (Exception e) {
          }

          byte[] inBlobBytes = new byte[LARGE_BLOB_SIZE];
          for (int i = 0; i < LARGE_BLOB_SIZE; i++) {
            inBlobBytes[i] = 0x01;
          }
          Blob inBlob = new JDLobTest.JDTestBlob(inBlobBytes);

          sql1 = "CREATE PROCEDURE " + procName + "(IN B BLOB("
              + LARGE_BLOB_SIZE + "), "
              + " OUT I INT, OUT S BINARY(1)) LANGUAGE SQL " + "SPECIFIC "
              + baseProcName + " XXXX1: BEGIN " + "SET I = LENGTH(B); "
              + "SET S = SUBSTRING(B,1,1);" + "END XXXX1";

          stmt.executeUpdate(sql1);
          sql1 = "{call " + procName + " (?,?,?)}";
          CallableStatement cstmt = connection_.prepareCall(sql1);
          cstmt.setBlob(1, inBlob);
          cstmt.registerOutParameter(2, java.sql.Types.INTEGER);
          cstmt.registerOutParameter(3, java.sql.Types.BINARY);
          cstmt.execute();
          int answer = cstmt.getInt(2);
          byte[] b = cstmt.getBytes(3);

          assertCondition((answer == LARGE_BLOB_SIZE) && (b[0] == 0x1),
              "Error answer is " + answer + " sb " + LARGE_BLOB_SIZE + " b[0]="
                  + b[0] + " sb" + 0x1 + "-- Added by native driver 12/14/2005");
        } catch (Exception e) {
          failed(e, "Unexpected Exception -- added by native driver 12/14/2005");
        }
      }
      }
  }
  }
  /**
   * SetBlob() - Set a BOOLEAN parameter. - Using an ordinal parameter
   */
  public void Var032()
  {

      if (checkBooleanSupport())
      {
        byte[] b = new byte[] { (byte) 1, (byte) -12, (byte) 57};
       
        setTestFailure("setBlob",27,  new JDLobTest.JDTestBlob (b), "Added 2021/01/07 to test boolean support");
      }

  }
  
  /**
   * SetBlob() - Set a BOOLEAN parameter. - Using a named parameter
   */
  public void Var033()
  {

      if (checkBooleanSupport())
      {
        byte[] b = new byte[] { (byte) 1, (byte) -12, (byte) 57};
       
        setTestFailure("setBlob","P_BOOLEAN",  new JDLobTest.JDTestBlob (b), "Added 2021/01/07 to test boolean support");
      }

  }

}
