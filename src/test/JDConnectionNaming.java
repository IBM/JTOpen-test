///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionNaming.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
 ////////////////////////////////////////////////////////////////////////
 //
 // File Name:    JDConnectionNaming.java
 //
 // Classes:      JDConnectionNaming
 //
 ////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Hashtable;
import java.sql.CallableStatement;


/**
Testcase JDConnectionNaming.  This tests the following
property with respect to the JDBC Connection class:

<ul>
<li>naming
</ul>
**/
public class JDConnectionNaming
extends JDTestcase
{



/**
Constructor.
**/
    public JDConnectionNaming (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDConnectionNaming",
            namesAndVars, runMode, fileOutputStream,
            password);
    }



/**
Set up.

@exception Exception If an error occurs.
**/
    protected void setup()
    throws Exception
    {
        Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            JDSetupProcedure.create (systemObject_, c, JDSetupProcedure.STP_RS1,
                                     supportedFeatures_, collection_);
        

        c.close();
    }


/**
naming - Does not specify naming but uses system naming.
An exception should be thrown.
**/
    public void Var001()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeQuery ("SELECT * FROM QIWS/QCUSTCDT");
            failed ("System naming accepted with sql naming set.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
naming - Does not specify naming and uses sql naming.
This should work.
**/
    public void Var002()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            succeeded ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
naming - Specifies sql naming but uses system naming.
An exception should be thrown.
**/
    public void Var003()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";naming=sql", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeQuery ("SELECT * FROM QIWS/QCUSTCDT");
            failed ("System naming accepted with sql naming set.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
naming - Specifies sql naming and uses sql naming.
This should work.
**/
    public void Var004()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";naming=sql", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            succeeded ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
naming - Specifies system naming but uses sql naming.
An exception should be thrown.
**/
    public void Var005()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";naming=system", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            /* 09/09/2012 -- V7R1 now accepts system naming */
	    if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
		assertCondition(true); 
	    } else { 
		failed ("SQL naming accepted with system naming set.");
	    }
        }
        catch (Exception e) {
	    if (getRelease() >= JDTestDriver.RELEASE_V7R2M0) {
		failed(e, "unexpected exception.  In V7R2, Sue Romano says Yes, I now accept the period as the delimiter when using *SYS naming.  It was part of the default parameter I0."); 
	    } else { 
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
        }
    }



/**
naming - Specifies system naming and uses system naming.
This should work.
**/
    public void Var006()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";naming=system", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeQuery ("SELECT * FROM QIWS/QCUSTCDT");
            succeeded ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
naming - Specifies bogus naming but uses system naming.
An exception should be thrown.
**/
    public void Var007()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";naming=bogus", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeQuery ("SELECT * FROM QIWS/QCUSTCDT");
            failed ("System naming accepted with bogus naming set.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
naming - Specifies bogus naming and uses sql naming.
This should work.
**/
    public void Var008()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";naming=bogus", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            succeeded ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
naming - Specifies system naming and then calls a stored procedure.  This 
was a customer reported bug with the Native JDBC driver that was fixed in
the 9/99 e-pack release.
**/
    public void Var009()    
    {
       try {
          Connection c = testDriver_.getConnection (baseURL_
              + ";naming=system", userId_, encryptedPassword_);
          CallableStatement cs;

          if (collection_ != null) {   // @C1A
              cs = c.prepareCall ("CALL " + collection_ + "/JDRSONE");
          }
          else {
              cs = c.prepareCall ("CALL " + JDSetupProcedure.COLLECTION + "/JDRSONE");
          }

           cs.execute ();
           cs.close ();
           c.close();
           succeeded ();
       }
       catch (Exception e) {
           failed (e, "Unexpected Exception");
       }
    }

/**
naming - Specifies system naming and sets of parameters of stored procedure by name
**/

    public void Var010() {
	if (checkNamedParametersSupport()) {
	    try { 
		Connection c = testDriver_.getConnection (baseURL_
							  + ";naming=system",
							  userId_, encryptedPassword_);

		Statement stmt = c.createStatement();
		String col;
		if (collection_ != null) {   // @C1A
		    col =  collection_; 
		}
		else {
		    col =  JDSetupProcedure.COLLECTION;
		}

		String procedureName = col+"/JDCNV10";

		try {
		    stmt.executeUpdate("Drop procedure "+procedureName); 
		} catch (Exception e) {
		} 

		stmt.executeUpdate("Create Procedure "+procedureName+"(IN INDATE DATE, OUT OUTDATE DATE) LANGUAGE SQL BEGIN SET OUTDATE = DATE(DAYS(INDATE)+1); END");
		CallableStatement cs;

		cs = c. prepareCall("CALL "+procedureName+" (?,?)");
		cs.setDate("INDATE", new java.sql.Date(System.currentTimeMillis()));

		cs.registerOutParameter("OUTDATE", java.sql.Types.DATE); 

		cs.executeUpdate(); 

		cs.close();

		stmt.executeUpdate("Drop procedure "+procedureName); 

		succeeded();

	    } catch (Exception e) {
		failed (e, "Unexpected Exception -- added by native driver 03/31/2004 ");
	    }


	}
    } 

/**
naming - Specifies system naming and sets of parameters of stored procedure by name
When not all parameters are parameter markers.

Fixed in V5R5 using issue 33770.

**/

    public void Var011() {
        //@PDC na on toolbox driver for now
	if (isToolboxDriver()) {
	    notApplicable("toolbox driver possible todo");
	    return;
	}
	if (getRelease() <= JDTestDriver.RELEASE_V5R4M0) {
	    notApplicable("Find named parameter problem when not all parms are parameter markers not working in V5R4");
	    return; 
	} 
	if (checkNamedParametersSupport()) {
	    try { 
		Connection c = testDriver_.getConnection (baseURL_
							  + ";naming=system",
							  userId_, encryptedPassword_);

		Statement stmt = c.createStatement();
		String col;
		if (collection_ != null) {   // @C1A
		    col =  collection_; 
		}
		else {
		    col =  JDSetupProcedure.COLLECTION;
		}

		String procedureName = col+"/JDCNV10";

		try {
		    stmt.executeUpdate("Drop procedure "+procedureName); 
		} catch (Exception e) {
		} 

		stmt.executeUpdate("Create Procedure "+procedureName+"(IN ININT INT, OUT OUTINT INT) LANGUAGE SQL BEGIN SET OUTINT=ININT; END");
		CallableStatement cs;

		cs = c. prepareCall("CALL "+procedureName+" (22,?)");

		cs.registerOutParameter("OUTINT", java.sql.Types.DATE); 

		cs.executeUpdate();
		int outInt =  cs.getInt("OUTINT"); 

		cs.close();

		stmt.executeUpdate("Drop procedure "+procedureName); 

		assertCondition(outInt == 22, "Output is "+outInt+" instead of 22 -- added by native driver 03/30/2004 ");

	    } catch (Exception e) {
		failed (e, "Unexpected Exception -- Find named parameter problem when not all parms are parameter markers not working in V5R4 -- Issue 33770 --  this needs to be fixed in V5R4 by the native driver 03/30/2004 ");
	    }


	}
    } 

}












