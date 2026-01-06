///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JTAResource2.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.JTA;

import com.ibm.as400.access.AS400;

import test.JDTestDriver;
import test.JTATest;
import test.JD.JDTestUtilities;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable; import java.util.Vector;
import javax.sql.XAConnection;
import javax.sql.XADataSource;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;



/**
Testcase JTAResource2.  This tests miscellaneous aspects
of the JDBC XA* classes.

<ul>
<li>XAConnection.getResource()
<li>Connection.getAutoCommit()
<li>Connection.setAutoCommit()
<li>Connection.commit()
<li>Connection.rollback()
</ul>
**/
public class JTAResource2
extends JTATestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JTAResource2";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JTAStdTest.main(newArgs); 
   }


   // Private data.
    private static String table_ = JTATest.COLLECTION + ".JTARESOURCE2";



/**
Constructor.
**/
    public JTAResource2(AS400 systemObject,
			Hashtable<String,Vector<String>> namesAndVars,
			int runMode,
			FileOutputStream fileOutputStream,
			
			String password)
    {
	super (systemObject, "JTAResource2",
	       namesAndVars, runMode, fileOutputStream,
	       password);
    }


    public JTAResource2(AS400 systemObject,
			String testname, 
			Hashtable<String,Vector<String>> namesAndVars,
			int runMode,
			FileOutputStream fileOutputStream,
			
			String password)
    {
	super (systemObject, testname, 
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
	lockSystem("JTATEST",600);
	Connection c = testDriver_.getConnection (baseURL_ + ";errors=full",
						  userId_, encryptedPassword_);
	isIasp = JDTestUtilities.isIasp(c); 
	Statement s = c.createStatement();
	table_ = JTATest.COLLECTION + ".JTARESOURCE2"; 
	s.executeUpdate("CREATE TABLE " + table_ + " (COL1 INTEGER)");
	s.executeUpdate("GRANT ALL ON "+ table_ + " TO PUBLIC"); 
	s.close();
	c.close();
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
      throws Exception
    {
	/* Run the garbage collector to clean up loose ends */ 
	System.gc();

	Connection c = testDriver_.getConnection (baseURL_ + ";errors=full",
						  userId_, encryptedPassword_);
	Statement s = c.createStatement();
	s.executeUpdate("DROP TABLE " + table_);
	s.close();
	c.close();
	unlockSystem(); 
    }



    private int countRows(int value)
      throws Exception
    {
	Connection c = testDriver_.getConnection (baseURL_ + ";errors=full",
						  userId_, encryptedPassword_);
	Statement s = c.createStatement();
	ResultSet rs = s.executeQuery("SELECT * FROM " + table_ + " WHERE COL1 = " + value);
	int count = 0;
	while (rs.next())
	    ++count;
	rs.close();
	s.close();
	c.close();
      // output_.println("Row count = " + count);
	return count;
    }



/**
XAConnection.getResource() - Verify that it always returns the same resource.
**/
    public void Var001()
    {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
	if (checkJdbc20StdExt()) {
	    try {
		XADataSource xads = newXADataSource();
		XAConnection xac = xads.getXAConnection();
		XAResource xar = xac.getXAResource();
		XAResource xar2 = xac.getXAResource();
		assertCondition(xar == xar2);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }



/**
Connection.getAutoCommit() - When auto commit has been set to false, this should return
false while in the distributed transaction and false when out of it.
**/
    public void Var002()
    {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
	if (checkJdbc20StdExt()) {
	    try {
		XADataSource xads = newXADataSource();
		XAConnection xac = xads.getXAConnection();
		XAResource xar = xac.getXAResource();
		Connection c = xac.getConnection();
		c.setAutoCommit(false);

		Xid xid = new JTATestXid();
		xar.start(xid, XAResource.TMNOFLAGS);
		boolean autoCommit1 = c.getAutoCommit();
		xar.end(xid, XAResource.TMSUCCESS);
		xar.commit(xid, true);
		boolean autoCommit2 = c.getAutoCommit();
		xac.close();

		assertCondition((autoCommit1 == false) && (autoCommit2 == false));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }



/**
Connection.getAutoCommit() - When auto commit has been set to true, this should be return
false while in the distributed transaction and true when out of it.
**/
    public void Var003()
    {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
	if (checkJdbc20StdExt()) {
	    try {
		XADataSource xads = newXADataSource();
		XAConnection xac = xads.getXAConnection();
		XAResource xar = xac.getXAResource();
		Connection c = xac.getConnection();
		c.setAutoCommit(true);

		Xid xid = new JTATestXid();
		xar.start(xid, XAResource.TMNOFLAGS);
		boolean autoCommit1 = c.getAutoCommit();
		xar.end(xid, XAResource.TMSUCCESS);
		xar.commit(xid, true);
		boolean autoCommit2 = c.getAutoCommit();
		xac.close();
		boolean condition = (autoCommit1 == false) && (autoCommit2 == true);
		if (!condition) {
		    output_.println("autoCommit inside transaction should be false but is "+autoCommit1); 
		    output_.println("autoCommit outside transaction should be true but is "+autoCommit2); 
		}
		assertCondition(condition); 
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }



/**
Connection.setAutoCommit() - Verify that this fails when in a global transaction.
**/
    public void Var004()
    {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
	if (checkJdbc20StdExt()) {
	    try {
		XADataSource xads = newXADataSource();
		XAConnection xac = xads.getXAConnection();
		XAResource xar = xac.getXAResource();
		Connection c = xac.getConnection();

		Xid xid = new JTATestXid();
		xar.start(xid, XAResource.TMNOFLAGS);
		boolean error = false;
		try {
		    c.setAutoCommit(true);
		}
		catch (SQLException e) {
		    error = true;
		}
		xar.end(xid, XAResource.TMSUCCESS);
		xar.rollback(xid);
		xac.close();
		if (!error) {
		    output_.println("Setting autocommit in global transaction did not fail as expected"); 
		}
		assertCondition(error == true);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }



/**
Connection.setAutoCommit() - Verify that does not work again when a global transaction is done,
but not committed.
**/
    public void Var005()
    {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
	if (checkJdbc20StdExt()) {
	    try {
		XADataSource xads = newXADataSource();
		XAConnection xac = xads.getXAConnection();
		XAResource xar = xac.getXAResource();
		Connection c = xac.getConnection();

		Xid xid = new JTATestXid();
		xar.start(xid, XAResource.TMNOFLAGS);
		Statement s = c.createStatement();
		s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(7754)");
		boolean error = false;
		try {
		    c.setAutoCommit(true);
		}
		catch(Exception e) {
		    if (JTATest.verbose) { 
			output_.println("Exception is "+e);
			e.printStackTrace();
		    }
		    error = true;
		}

		  xar.end(xid, XAResource.TMSUCCESS);


		xar.commit(xid, true);
		xac.close();
		if (!error) {
		    output_.println("Setting autocommit in global transaction did not fail as expected"); 
		} 

		assertCondition(error == true);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }



/**
Connection.setAutoCommit() - Verify that this works again when a global transaction is done,
and committed.
**/
    public void Var006()
    {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
	if (checkJdbc20StdExt()) {
	    try {
		XADataSource xads = newXADataSource();
		XAConnection xac = xads.getXAConnection();
		XAResource xar = xac.getXAResource();
		Connection c = xac.getConnection();

		Xid xid = new JTATestXid();
		xar.start(xid, XAResource.TMNOFLAGS);
		Statement s = c.createStatement();
		s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(77546)");
		xar.end(xid, XAResource.TMSUCCESS);
		xar.commit(xid, true);
		c.setAutoCommit(true);
		xac.close();

	    // Normally, this would be a perfectly reasonable thing
	    // to do. But due to As/400 architecture problems, we
	    // cannot support it. Because of that, we return something
	    // other than a normal XA return code.
		assertCondition(countRows(77546) == 1);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }



/**
Connection.setAutoCommit() - Verify that this works again when a global transaction is done,
and rolled back.
**/
    public void Var007()
    {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
	if (checkJdbc20StdExt()) {
	    try {
		XADataSource xads = newXADataSource();
		XAConnection xac = xads.getXAConnection();
		XAResource xar = xac.getXAResource();
		Connection c = xac.getConnection();

		Xid xid = new JTATestXid();
		xar.start(xid, XAResource.TMNOFLAGS);
		Statement s = c.createStatement();
		s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(-17546)");
		int errorCode = 0;
		try {
		    xar.end(xid, XAResource.TMFAIL);
		}
		catch (XAException e) {
		    errorCode = e.errorCode;
		}
		xar.rollback(xid);
		c.setAutoCommit(true);
		xac.close();

        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
              //@PDA toolbox
            assertCondition((countRows(-17546) == 0));
        } else {
		    assertCondition((errorCode == XAException.XA_RBROLLBACK) && (countRows(-17546) == 0));
        }
        
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }




/**
Connection.commit() - Verify that this fails when in a global transaction.
**/
    public void Var008()
    {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
	if (checkJdbc20StdExt()) {
	    try {
		XADataSource xads = newXADataSource();
		XAConnection xac = xads.getXAConnection();
		XAResource xar = xac.getXAResource();
		Connection c = xac.getConnection();
		c.setAutoCommit(false);

		Xid xid = new JTATestXid();
		xar.start(xid, XAResource.TMNOFLAGS);
		Statement s = c.createStatement();
		s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(34865)");
		boolean error = false;
		try {
		    c.commit();
		}
		catch (SQLException e) {
		    error = true;
		}
		xar.end(xid, XAResource.TMSUCCESS);
		xar.rollback(xid);
		xac.close();

		assertCondition((error == true) && (countRows(34865) == 0));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }



/**
Connection.commit() - Verify that does not work again when a global transaction is done,
but not committed.
**/
    public void Var009()
    {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
	if (checkJdbc20StdExt()) {
	    try {
		XADataSource xads = newXADataSource();
		XAConnection xac = xads.getXAConnection();
		XAResource xar = xac.getXAResource();
		Connection c = xac.getConnection();
		c.setAutoCommit(false);

		Xid xid = new JTATestXid();
		xar.start(xid, XAResource.TMNOFLAGS);
		Statement s = c.createStatement();
		s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(77549)");
		boolean error1 = false;
		try {
		    c.commit();
		}
		catch (Exception e) {
		    if (JTATest.verbose) e.printStackTrace(); 
		    error1 = true;
		}
		xar.end(xid, XAResource.TMSUCCESS);
		xar.commit(xid, true);
		boolean error2 = false;
		try {
		    c.commit();
		}
		catch (Exception e) {
		    if (JTATest.verbose) e.printStackTrace(); 
		    error2 = true;
		}
		xac.close();
		boolean condition = error1 == true && error2 == false;
		if (! condition) {
		    output_.println("error1 should be true but is "+error1); 
		    output_.println("error2 should be false but is "+error2); 
		}
		assertCondition(condition); 
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }



/**
Connection.commit() - Verify that this works again when a global transaction is done,
and committed.
**/
    public void Var010()
    {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
	if (checkJdbc20StdExt()) {
	    try {
		XADataSource xads = newXADataSource();
		XAConnection xac = xads.getXAConnection();
		XAResource xar = xac.getXAResource();
		Connection c = xac.getConnection();
		c.setAutoCommit(false);

		Xid xid = new JTATestXid();
		xar.start(xid, XAResource.TMNOFLAGS);
		Statement s = c.createStatement();
		s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(7754699)");
		xar.end(xid, XAResource.TMSUCCESS);
		xar.commit(xid, true);
		s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(7754698)");
		c.commit();
		xac.close();

		assertCondition((countRows(7754699) == 1) && (countRows(7754698) == 1));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }



/**
Connection.commit() - Verify that this works again when a global transaction is done,
and rolled back.
**/
    public void Var011()
    {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
	if (checkJdbc20StdExt()) {
	    try {
		XADataSource xads = newXADataSource();
		XAConnection xac = xads.getXAConnection();
		XAResource xar = xac.getXAResource();
		Connection c = xac.getConnection();
		c.setAutoCommit(false);

		Xid xid = new JTATestXid();
		xar.start(xid, XAResource.TMNOFLAGS);
		Statement s = c.createStatement();
		s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(-175469)");
		int errorCode = 0;
		try {
		    xar.end(xid, XAResource.TMFAIL);
		}
		catch (XAException e) {
		    errorCode = e.errorCode;
		}
		xar.rollback(xid);
	    // Normally, this would be a perfectly reasonable thing
	    // to do. But due to As/400 architecture problems, we
	    // cannot support accessing the local transaction after
	    // the global one has been ended but not committed.
	    // s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(-175468)");
	    // c.commit();
		xac.close();

		int rowCount = countRows(-175469); 
        boolean condition = false;
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
            //@PDA toolbox
            condition = (rowCount == 0);
        }else {
		    condition = (errorCode == XAException.XA_RBROLLBACK) &&
		                (rowCount == 0);
        }
		if (!condition) {
		    output_.println("errorCode should be XA_RBROLLBACK but is "+errorCode);
		    output_.println("rowCount should b 0, but is "+rowCount); 
		}
		assertCondition(condition);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }




/**
Connection.rollback() - Verify that this fails when in a global transaction.
**/
    public void Var012()
    {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
	if (checkJdbc20StdExt()) {
	    try {
		XADataSource xads = newXADataSource();
		XAConnection xac = xads.getXAConnection();
		XAResource xar = xac.getXAResource();
		Connection c = xac.getConnection();
		c.setAutoCommit(false);

		Xid xid = new JTATestXid();
		xar.start(xid, XAResource.TMNOFLAGS);
		Statement s = c.createStatement();
		s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(7465)");
		boolean error = false;
		try {
		    c.rollback();
		}
		catch (SQLException e) {
		    error = true;
		}
		xar.end(xid, XAResource.TMSUCCESS);
		xar.rollback(xid);
		xac.close();

		assertCondition((error == true) && (countRows(7465) == 0));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }



/**
Connection.rollback() - Verify that does not work again when a global transaction is done,
but not committed.
**/
    public void Var013()
    {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
	if (checkJdbc20StdExt()) {
	    try {
		XADataSource xads = newXADataSource();
		XAConnection xac = xads.getXAConnection();
		XAResource xar = xac.getXAResource();
		Connection c = xac.getConnection();
		c.setAutoCommit(false);

		Xid xid = new JTATestXid();
		xar.start(xid, XAResource.TMNOFLAGS);
		Statement s = c.createStatement();
		s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(77533)");
		boolean error1 = false;
		try {
		    c.rollback();
		}
		catch (Exception e) {
		    if (JTATest.verbose) e.printStackTrace(); 
		    error1 = true;
		}
		xar.end(xid, XAResource.TMSUCCESS);
		xar.commit(xid, true);
		boolean error2 = false;
		try {
		    c.rollback();
		}
		catch(Exception e) {
		    if (JTATest.verbose) e.printStackTrace(); 
		    error2 = true;
		}
		xac.close();
		if (JTATest.verbose) {
		    output_.println("error1 should be true but is "+error1);
		    output_.println("error2 should be false but is "+error2); 
		} 

		assertCondition(error1 == true && error2 == false);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }



/**
Connection.rollback() - Verify that this works again when a global transaction is done,
and committed.
**/
    public void Var014()
    {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
	if (checkJdbc20StdExt()) {
	    try {
		XADataSource xads = newXADataSource();
		XAConnection xac = xads.getXAConnection();
		XAResource xar = xac.getXAResource();
		Connection c = xac.getConnection();
		c.setAutoCommit(false);

		Xid xid = new JTATestXid();
		xar.start(xid, XAResource.TMNOFLAGS);
		Statement s = c.createStatement();
		s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(7754633)");
		xar.end(xid, XAResource.TMSUCCESS);
		xar.commit(xid, true);
		s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(7754634)");
		c.rollback();
		xac.close();

		assertCondition((countRows(7754633) == 1) && (countRows(7754634) == 0));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }



/**
Connection.rollback() - Verify that this works again when a global transaction is done,
and rolled back.
**/
    public void Var015()
    {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
	if (checkJdbc20StdExt()) {
	    try {
		XADataSource xads = newXADataSource();
		XAConnection xac = xads.getXAConnection();
		XAResource xar = xac.getXAResource();
		Connection c = xac.getConnection();
		c.setAutoCommit(false);

		Xid xid = new JTATestXid();
		xar.start(xid, XAResource.TMNOFLAGS);
		Statement s = c.createStatement();
		s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(-175433)");
		int errorCode1 = 0;
		try {
		    xar.end(xid, XAResource.TMFAIL);
		}
		catch (XAException e) {
		    errorCode1 = e.errorCode;
		}

	    // Normally, this would be a perfectly reasonable thing
	    // to do. But due to As/400 architecture problems, we
	    // cannot support accessing the local transaction after
	    // the global one has been ended but not committed.
	    //
	    // xar.rollback(xid);
	    // s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(-175434)");
	    // c.rollback();
		xar.rollback(xid);
		xac.close();

		assertCondition((errorCode1 == XAException.XA_RBROLLBACK) &&
				(countRows(-175433) == 0) && (countRows(-175434) == 0));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
Connection.commit() - Verify that this fails when in a global transaction with
no work done.
**/
    public void Var016()
    {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
	if (checkJdbc20StdExt()) {
	    try {
		XADataSource xads = newXADataSource();
		XAConnection xac = xads.getXAConnection();
		XAResource xar = xac.getXAResource();
		Connection c = xac.getConnection();
		c.setAutoCommit(false);

		Xid xid = new JTATestXid();
		xar.start(xid, XAResource.TMNOFLAGS);
		boolean error = false;
		try {
		    c.commit();
		}
		catch (SQLException e) {
		    error = true;
		}
		xar.end(xid, XAResource.TMSUCCESS);
		xar.rollback(xid);
		xac.close();
		boolean condition;
		condition = (error == true) && (countRows(34865) == 0);
if (! condition) {
    output_.println("Expect error to be true, but is "+error);
    output_.println("Expected out to be 0, but is "+countRows(34865)); 
}
assertCondition(condition); 
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
Connection.rollback() - Verify that this fails when in a global transaction
with no work done.
**/
    public void Var017()
    {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
	if (checkJdbc20StdExt()) {
	    try {
		XADataSource xads = newXADataSource();
		XAConnection xac = xads.getXAConnection();
		XAResource xar = xac.getXAResource();
		Connection c = xac.getConnection();
		c.setAutoCommit(false);

		Xid xid = new JTATestXid();
		xar.start(xid, XAResource.TMNOFLAGS);
		boolean error = false;
		try {
		    c.rollback();
		}
		catch (SQLException e) {
		    error = true;
		}
		xar.end(xid, XAResource.TMSUCCESS);
		xar.rollback(xid);
		xac.close();
		//
// Native driver permits a commit if no work has been done
    //
    boolean condition; 
condition = (error == true);

if (!condition) {
    output_.println("Expect error to be true, but is "+error);
}

assertCondition(condition);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }

    /**
     Connection.setSavepoint() - Verify that this fails when in a global transaction.
     **/
    public void Var018()
    {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
	if (checkJdbc30()) {
	    try {
		XADataSource xads = newXADataSource();
		XAConnection xac = xads.getXAConnection();
		XAResource xar = xac.getXAResource();
		Connection c = xac.getConnection();
		c.setAutoCommit(false);

		Xid xid = new JTATestXid();
		xar.start(xid, XAResource.TMNOFLAGS);
		Statement s = c.createStatement();
		s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(34865)");
		boolean error = false;
		try {
		    c.setSavepoint();
		}
		catch (SQLException e) {
		    error = true;
		}
		xar.end(xid, XAResource.TMSUCCESS);
		xar.rollback(xid);
		xac.close();
		int rowCount = countRows(34865) ; 
		boolean condition = (error == true) && (rowCount == 0);
		if (!condition) {
		    output_.println("Error is "+error+" should be true");
		    output_.println("countRows(34865) is "+rowCount+" but should be 0"); 
		} 
		assertCondition(condition);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
     Connection.setSavepoint() - Verify that this fails when in a global transaction.
     **/
    public void Var019()
    {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
	if (checkJdbc30()) {
	    try {
		XADataSource xads = newXADataSource();
		XAConnection xac = xads.getXAConnection();
		XAResource xar = xac.getXAResource();
		Connection c = xac.getConnection();
		c.setAutoCommit(false);

		Xid xid = new JTATestXid();
		xar.start(xid, XAResource.TMNOFLAGS);
		Statement s = c.createStatement();
		s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(34865)");
		boolean error = false;
		try {
		    c.setSavepoint("MySAVEPOINT");
		}
		catch (SQLException e) {
		    error = true;
		}
		xar.end(xid, XAResource.TMSUCCESS);
		xar.rollback(xid);
		xac.close();

		int rowCount = countRows(34865) ; 
		boolean condition = (error == true) && (rowCount == 0);
		if (!condition) {
		    output_.println("Error is "+error+" should be true");
		    output_.println("countRows(34865) is "+rowCount+" but should be 0"); 
		} 
		assertCondition(condition);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }
}



