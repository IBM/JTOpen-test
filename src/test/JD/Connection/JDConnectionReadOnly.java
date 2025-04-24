///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionReadOnly.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

 ////////////////////////////////////////////////////////////////////////
 //
 // File Name:    JDConnectionReadOnly.java
 //
 // Classes:      JDConnectionReadOnly
 //
 ////////////////////////////////////////////////////////////////////////

package test.JD.Connection;

import com.ibm.as400.access.AS400;

import test.JDConnectionTest;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable; import java.util.Vector;



/**
Testcase JDConnectionReadOnly.  This tests the following
methods of the JDBC Connection class:

<ul>
<li>access property
<li>isReadOnly()
<li>setReadOnly()
</ul>
**/
public class JDConnectionReadOnly
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDConnectionReadOnly";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDConnectionTest.main(newArgs); 
   }



    // Private data.
    private static  String table_      = JDConnectionTest.COLLECTION + ".JDCACCESS";
    private static final String dummyTbl    = "QTEMP.CANCREATE";


/**
Constructor.
**/
    public JDConnectionReadOnly (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDConnectionReadOnly",
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
        // Create the table.
        table_      = JDConnectionTest.COLLECTION + ".JDCACCESS";
        Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
        Statement s = c.createStatement ();
        s.executeUpdate ("CREATE TABLE " + table_
            + " (NAME CHAR(10), ID INT)");
        s.close ();
        c.commit(); // for xa
        c.close ();
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
        // Drop the table.
        Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
        Statement s = c.createStatement ();
        s.executeUpdate ("DROP TABLE " + table_);
        s.close ();
        c.commit(); // for xa
        c.close ();
    }



/**
Checks that a connection can query a table.

@param  c       The connection.
@return         true if the connection can query a table,
                false otherwise.
**/
    private boolean canRead (Connection c)
        throws Exception
    {
        boolean success = false;
        Statement s = c.createStatement ();
        try {
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            success = rs.next ();
            rs.close ();
            success = true;
        }
        catch (SQLException e) {
            // Ignore.
        }

        s.close ();
        return success;
    }



/**
Checks that a connection can update a table.

@param  c       The connection.
@return         true if the connection can update or create a table,
                false otherwise.
**/
    private boolean canWrite (Connection c)
        throws Exception
    {
        boolean success = false;
        Statement s = c.createStatement ();
        try {
            s.executeUpdate ("INSERT INTO " + table_
                + " (NAME, ID) VALUES ('Josh', 123)");
            success = true;
        }
        catch (SQLException e) {
            // Ignore.
        }
        try {
            s.execute ("CREATE TABLE " + dummyTbl + " (COL1 INT)");
            success = true;
        }
        catch (SQLException e) {
            // Ignore.
        }

        s.close ();
        return success;
    }



/**
Checks that a connection can call a program.

@param  c       The connection.
@return         true if the connection can call a program,
                false otherwise.
**/
    private boolean canCall (Connection c)
        throws Exception
    {
        boolean success = false;
        Statement s = c.createStatement ();
        try {
	    s.execute ("CALL QSYS.QCMDEXC('DSPLIBL *PRINT', 0000000014.00000)");
            success = true;

        }
        catch (SQLException e) {
            // Ignore.
        }

        s.close ();
        return success;
    }



/**
access property - Uses default access.  Connection should be able to
read, write, and call.
**/
    public void Var001()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            boolean read = canRead (c);
            boolean write = canWrite (c);
            boolean call = canCall (c);
            c.close ();
            assertCondition (read && write && call, "read="+read+" write="+write+" call="+call+" all should be true");
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
access property - Specifies invalid access.  Connection should be
able to read, write, and call.
**/
    public void Var002()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";access=bogus", userId_, encryptedPassword_);
            boolean read = canRead (c);
            boolean write = canWrite (c);
            boolean call = canCall (c);
            c.close ();
            assertCondition (read && write && call);
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
access property - Specifies "all" access.  Connection should be
able to read, write, and call.
**/
    public void Var003()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";access=all", userId_, encryptedPassword_);
            boolean read = canRead (c);
            boolean write = canWrite (c);
            boolean call = canCall (c);
            c.close ();
            assertCondition (read && write && call);
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
access property - Specifies "read call" access.  Connection should be
able to read and call, but not write.
**/
    public void Var004()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";access=read call", userId_, encryptedPassword_);
            boolean read = canRead (c);
            boolean write = canWrite (c);
            boolean call = canCall (c);
            c.close ();
            assertCondition (read && !write && call);
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
access property - Specifies "read only" access.  Connection should be
able to read, but not write or call.
**/
    public void Var005()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";access=read only", userId_, encryptedPassword_);
            boolean read = canRead (c);
            boolean write = canWrite (c);
            boolean call = canCall (c);
            c.close ();
            assertCondition (read && !write && !call,
			     "\nread="+read+" sb true"+
                             "\nwrite="+write+" sb false" +
                             "\ncall="+call+" sb false");
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
access property - Specifies weird case.  The property should
still be recognized.
**/
    public void Var006()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";access=ReaD OnlY", userId_, encryptedPassword_);
            boolean read = canRead (c);
            boolean write = canWrite (c);
            boolean call = canCall (c);
            c.close ();
            assertCondition (read && !write && !call,
			     "\nread="+read+" sb true"+
                             "\nwrite="+write+" sb false" +
                             "\ncall="+call+" sb false");
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
isReadOnly() - Verify that the default is false when
no access property is set.
**/
    public void Var007()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            boolean ro = c.isReadOnly ();
            c.close ();
            assertCondition (ro == false);
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
isReadOnly() - Verify that the default is false when
the access property is set to "all".
**/
    public void Var008()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";access=all", userId_, encryptedPassword_);
            boolean ro = c.isReadOnly ();
            c.close ();
            assertCondition (ro == false);
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
isReadOnly() - Verify that the default is true when
the access property is set to "read call".
**/
    public void Var009()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";access=read call", userId_, encryptedPassword_);
            boolean ro = c.isReadOnly ();
            c.close ();
            assertCondition (ro == true);
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
isReadOnly() - Verify that the default is true when
the access property is set to "read only".
**/
    public void Var010()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";access=read only", userId_, encryptedPassword_);
            boolean ro = c.isReadOnly ();
            c.close ();
            assertCondition (ro == true);
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
isReadOnly() - Verify that an exception is thrown when
the connection is closed.
**/
    public void Var011()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            c.close ();
            c.isReadOnly ();
            failed("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setReadOnly()/isReadOnly() - Verify that the value that was
set is then returned.
**/
    public void Var012()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            c.setReadOnly (true);
            boolean ro = c.isReadOnly ();
            c.close ();
            assertCondition (ro == true);
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
setReadOnly()/isReadOnly() - Verify that the value that was
set is then returned.
**/
    public void Var013()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            c.setReadOnly (true);
            boolean ro = c.isReadOnly ();
            c.close ();
            assertCondition (ro == true);
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
setReadOnly() - Verify that when set to false, we have all access.
**/
    public void Var014()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            c.setReadOnly (false);
            boolean read = canRead (c);
            boolean write = canWrite (c);
            boolean call = canCall (c);
            c.close ();
            assertCondition (read && write && call);
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
setReadOnly() - Verify that when set to true, we only have
read and call access.
**/
    public void Var015()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            c.setReadOnly (true);
            boolean read = canRead (c);
            boolean write = canWrite (c);
            boolean call = canCall (c);
            c.close ();
            assertCondition (read && ! write && call);
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
setReadOnly() - Verify that an exception is thrown when
the connection is closed.
**/
    public void Var016()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            c.close ();
            c.setReadOnly (true);
            failed("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
setReadOnly() - Verify that an exception is thrown when
a transaction is active.
**/
    public void Var017()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            c.setAutoCommit (false);
            c.setTransactionIsolation (Connection.TRANSACTION_READ_UNCOMMITTED);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (NAME, ID) VALUES ('Jim', -754)");

            boolean success = false;
            try {
                c.setReadOnly (true);
            }
            catch (SQLException e) {
                success = true;
            }

            s.close ();
            c.close ();
            assertCondition (success);
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
setReadOnly() - Verify that an exception is thrown when
a setting read only to false, but the access property is
set to "read only".
**/
    public void Var018()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";access=read only", userId_, encryptedPassword_);

            boolean success = false;
            try {
                c.setReadOnly (false);
            }
            catch (SQLException e) {
                success = true;
            }

            c.close ();
            assertCondition (success);
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
setReadOnly() - Verify that an exception is thrown when
a setting read only to false, but the access property is
set to "read call".
**/
    public void Var019()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";access=read call", userId_, encryptedPassword_);

            boolean success = false;
            try {
                c.setReadOnly (false);
            }
            catch (SQLException e) {
                success = true;
            }

            c.close ();
            assertCondition (success);
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }



/**
setReadOnly() - Verify that setReadOnly can be called serveral times,
alternating back and forth between being readonly and not readonly.
**/
    public void Var020()
    {
        try {

           Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);

           c.setReadOnly (true);
           boolean success1 = (canRead (c) && !canWrite (c) && canCall (c));

           c.setReadOnly (false);
           boolean success2 = (canRead (c) && canWrite (c) && canCall (c));

           c.setReadOnly (true);
           boolean success3 = (canRead (c) && !canWrite (c) && canCall (c));

           assertCondition (success1 && success2 && success3);

        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }


/**
setReadOnly() - Verify that an exception is not thrown when
a setting read only to true and the access property is already 
set to "read only".
**/
    public void Var021()
    {
        try {
            Connection c = testDriver_.getConnection (baseURL_
                + ";access=read only", userId_, encryptedPassword_);

            try {
                c.setReadOnly (true);
		assertCondition (true);
            }
            catch (SQLException e) {
		failed(e, "calling setReadOnly(true) on readonly connection failed -- native driver will fix in V5R3");
            }

            c.close ();
        }
        catch (Exception e) {
            failed(e,"Unexpected Exception");
        }
    }




}












