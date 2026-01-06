///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionCursorHoldability.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDConnectionCursorHoldability.java
//
// Classes:      JDConnectionCursorHoldability
//
////////////////////////////////////////////////////////////////////////

package test.JD.Connection;

import com.ibm.as400.access.AS400;

import test.JDConnectionTest;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JDTestcase;
import test.PasswordVault;

import java.io.FileOutputStream;
import java.sql.*;
import java.util.*;



/**
Testcase JDConnectionCursorHoldability. This tests the following
methods of the JDBC class:

<ul>
<li>setHoldability()
<li>getHoldability()
<li>getResultSetHoldability()
<li>createStatement()
<li>prepareCall()
<li>prepareStatement()
</ul>
**/
public class JDConnectionCursorHoldability
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDConnectionCursorHoldability";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDConnectionTest.main(newArgs); 
   }



    // Private data.

    private static  String table_      = JDConnectionTest.COLLECTION + ".HOLDCURSOR";
    private Connection          c;
            DatabaseMetaData    dmd_;
    private Statement           stmt;
    private Properties          properties;
    private String              procedureName = JDConnectionTest.COLLECTION + ".JDRSONE";

/**
Constructor.
**/
    public JDConnectionCursorHoldability (AS400 systemObject,
                                          Hashtable<String,Vector<String>> namesAndVars,
                                          int runMode,
                                          FileOutputStream fileOutputStream,
                                          
                                          String password)
    {
        super (systemObject, "JDConnectionCursorHoldability",
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
	output_.println("Setup:  collection is "+JDConnectionTest.COLLECTION); 
        if ( areCursorHoldabilitySupported() )
        {
            table_ = JDConnectionTest.COLLECTION + ".HOLDCURSOR";
            procedureName = JDConnectionTest.COLLECTION + ".JDRSONE";

            properties = new Properties();

            properties.put("block size", "0");
            properties.put("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDConnectionCursorHoldability.setup"));
            properties.put("user", userId_);

            if(isToolboxDriver())
                c = testDriver_.getConnection (baseURL_ + ";data truncation=true;transaction isolation=repeatable read;true autocommit=true", properties);
            else
                c = testDriver_.getConnection (baseURL_ + ";data truncation=true;transaction isolation=repeatable read", properties);
            //@A4A Add setup of collection
		JDSetupProcedure.resetCollection(JDConnectionTest.COLLECTION); 
                JDSetupProcedure.create (systemObject_, c,
                                         JDSetupProcedure.STP_RS1,
                                         supportedFeatures_, collection_, output_);

            dmd_ = c.getMetaData ();

            Statement s = c.createStatement();
	    try {
		s.executeUpdate ("DROP TABLE " + table_); 
	    } catch (Exception e) {
		String exMessage =e.toString();
		if (exMessage.indexOf("not found") < 0) {
		    output_.println("Warning.. unable to drop table"); 
		    e.printStackTrace();
		}
	    } 
            s.executeUpdate ("CREATE TABLE " + table_
                             + " (NAME CHAR(10), IDNUM INT)");

            //insert a few values into the table so that they can be used later
            s.executeUpdate("INSERT INTO " + table_ + " VALUES('first', 1)");
            s.executeUpdate("INSERT INTO " + table_ + " VALUES('second', 2)");

            s.close();
	    c.commit(); 
        }
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
        if ( areCursorHoldabilitySupported() )
        {

	    c.close();

            Connection c1 = testDriver_.getConnection( baseURL_, userId_, encryptedPassword_);
            Statement s = c1.createStatement();
            s.executeUpdate("DROP TABLE " + table_);
            s.close();
            c1.close(); 
        }
    }


    /**
Checks that a ResultSet object is still usable.

@param  rs      The ResultSet.
@return         true if the result set can still be used.
                false otherwise.
**/
    private boolean cursorOpen (ResultSet rs)
    throws Exception
    {
        boolean success = false;
        try {
            // If a fetch returns true, the cursor is still open.
            if (rs.next())
                success = true;
        }
        catch (SQLException e) {
	    if (getDriver () == JDTestDriver.DRIVER_NATIVE)  //@B2
	    {
		String exceptionMessage = "Cursor state not valid.";
		success = !exceptionMessage.equals(e.getMessage());

		if (success) {                                                            
		/* IF WE GET HERE, WE DIDN'T GET WHAT WE EXPECTED */
		/* DUMP THE EXCEPTION SO WE CAN FIX IT  */
		/* ALL EXCEPTIONS THAT WE EXPECT AS WELL AS THE */
		/* EXPECTED RESULT SHOULD BE EXPLICILY SPECIFIED */
		    output_.println("cursorOpen encountered unexpected exception "+e);
		    e.printStackTrace();
		}		
	    }
	    else  //toolbox
	    {
            success = !exceptionIs(e, "SQLException", "Cursor state not valid.");
        }
          }
        return success;
    }


/**
getHoldability() - This method determines if the default holdability of HOLD_CURSORS_OVER_COMMIT
is set for this connection object.
**/
    public void Var001 ()
    {
        if ( checkCursorHoldabilitySupport() )
        {
            try
            {
                assertCondition(c.getHoldability() == ResultSet.HOLD_CURSORS_OVER_COMMIT);
            }
            catch ( Exception e )
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setHoldability()
getHoldability()
- This method checks to see if a cursor holdability for this connection is set.
**/
    public void Var002 ()
    {
        if ( checkCursorHoldabilitySupport() )
        {
            try
            {
                c.setHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);
                assertCondition(c.getHoldability() == ResultSet.CLOSE_CURSORS_AT_COMMIT);
            }
            catch ( Exception e )
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setHoldability()
getHoldability()
- This method checks to see if a cursor holdability for this connection is set.
**/
    public void Var003 ()
    {
        if ( checkCursorHoldabilitySupport() )
        {
            try
            {
                c.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                assertCondition(c.getHoldability() == ResultSet.HOLD_CURSORS_OVER_COMMIT);
            }
            catch ( Exception e )
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
getHoldability()
- This method gets the current holdability for this connection.
**/
    public void Var004 ()
    {
        if ( checkCursorHoldabilitySupport() )
        {
            try
            {
                c.getHoldability();
                succeeded();
            }
            catch ( Exception e )
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setHoldability()
- An exception should be thrown if you try to set the holdability to something
other than one of the ResultSet constants.
**/
    public void Var005 ()
    {
        if ( checkCursorHoldabilitySupport() )
        {
            try
            {
                c.setHoldability(6);
                failed("Didn't throw SQLException");
            }
            catch ( Exception e )
            {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setHoldability()
- If the connections holdibility is set to CLOSE_CURSORS_AT_COMMIT,
the ResultSet objects should be closed when Connection.commit() is called.
**/
    public void Var006 ()
    {
        if ( checkCursorHoldabilitySupport() )
        {
            try
            {
		
                c.setHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);
                stmt=c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery("SELECT * FROM " + table_);
                c.commit();

                //isol 3
                assertCondition( !cursorOpen(rs) , "V7R1- *none keeps cursor open. (requires host support)" );  
                rs.close();
                stmt.close();
            }
            catch ( Exception e )
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setHoldability()
- If the connections holdability is set to HOLD_CURSORS_OVER_COMMIT,
the ResultSet objects should not be closed when Connection.commit() is called.
**/
    public void Var007 ()
    {
        if ( checkCursorHoldabilitySupport() )
        {
            try
            {
                c.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                stmt=c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery("SELECT * FROM " + table_);
                c.commit();
                assertCondition( cursorOpen(rs) );  //@A2C
                rs.close();
                stmt.close();
            }
            catch ( Exception e )
            {
                failed (e, "Unexpected Exceptions");
            }
        }
    }

/**
setHoldability()
getHoldability()
- Sets a connections holdibility to one thing then another.
**/
    public void Var008 ()
    {
        if ( checkCursorHoldabilitySupport() )
        {
            try
            {
                c.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                c.setHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);
                assertCondition( c.getHoldability() == ResultSet.CLOSE_CURSORS_AT_COMMIT );
            }
            catch ( Exception e )
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setHoldability()
createStatement()
- Sets a connections holdability and sets a statements holdability.
**/
    public void Var009 ()
    {
        if ( checkCursorHoldabilitySupport() )
        {
            try
            {
                c.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                stmt=c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
                ResultSet rs = stmt.executeQuery("SELECT * FROM " + table_);
                c.commit();
                //isolation 3
                assertCondition( !cursorOpen(rs) , "V7R1- *none keeps cursor open. (requires host support)" );    //@A2c
                rs.close();
                stmt.close();
            }
            catch ( Exception e )
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setHoldability()
getHoldability()
getResultSetHoldability()
- This method sets the connections cursor Holdability and then checks to see
if a statement created with this connection has the same holdability.
**/
    public void Var010 ()
    {
        if ( checkCursorHoldabilitySupport() )
        {
            try
            {
                c.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                stmt = c.createStatement();
                assertCondition( c.getHoldability() == stmt.getResultSetHoldability() );
                stmt.close();
            }
            catch ( Exception e )
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setHoldability()
getHoldability()
getResultSetHoldability()
- This method sets the connections cursor holdability and then checks to see 
if a statement created with this connection has the same holdability.
**/
    public void Var011 ()
    {
        if ( checkCursorHoldabilitySupport() )
        {
            try
            {
                c.setHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);
                stmt = c.createStatement();
                assertCondition( c.getHoldability() == stmt.getResultSetHoldability() );
                stmt.close();
            }
            catch ( Exception e )
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setHoldability()
getHoldability()
getResultSetHoldability()
- This method sets the connections cursor holdability and then checks to see 
if several statements created with this connection have the same holdability.
**/
    public void Var012 ()
    {
        if ( checkCursorHoldabilitySupport() )
        {
            try
            {
                c.setHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);
                stmt = c.createStatement();
                Statement s = c.createStatement();
                assertCondition( (c.getHoldability() == stmt.getResultSetHoldability()) && (c.getHoldability() == s.getResultSetHoldability()) );
                stmt.close();
                s.close();
            }
            catch ( Exception e )
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setHoldability()
getHoldability()
getResultSetHoldability()
- This method sets the connections cursor holdability and then checks to see 
if several statements created with this connection have the same holdability.
**/
    public void Var013 ()
    {
        if ( checkCursorHoldabilitySupport() )
        {
            try
            {
                c.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                stmt = c.createStatement();
                Statement s = c.createStatement();
                assertCondition( (c.getHoldability() == stmt.getResultSetHoldability()) && (c.getHoldability() == s.getResultSetHoldability()) );
                stmt.close();
                s.close();
            }
            catch ( Exception e )
            {
                failed (e, "Unexpected Exception");
            }
        }
    } 

/**
setHoldablility()
getHoldabililty()
getResultSetHoldability()
createStatement();
- This method sets the connections cursor holdability and then checks to see if
a statment created with the connection has the same holdability.  Then the statements
holdability is overrided.
**/
    public void Var014 ()
    {
        if ( checkCursorHoldabilitySupport() )
        {
            try
            {
                c.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                stmt=c.createStatement();
                int holdability = stmt.getResultSetHoldability();
                stmt=c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
                assertCondition( (stmt.getResultSetHoldability() != c.getHoldability()) && (holdability == c.getHoldability()) ); 
                stmt.close();
            }
            catch ( Exception e )
            {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setHoldabiltiy()
createStatement()
- This method sets the connections holdability and then sets several statements holdabilities
and makes sure the correct thing happens on commit.
**/
    public void Var015 ()
    {
        if ( checkCursorHoldabilitySupport() )
        {
            try
            {
                c.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                stmt=c.createStatement();
                Statement s = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
                Statement s1 = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
                Statement s2 = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
                Statement s3 = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
                ResultSet rs = stmt.executeQuery("SELECT * FROM " + table_);//hold isol3
                ResultSet rs1 = s.executeQuery("SELECT * FROM " + table_); //nonhold isol3
                ResultSet rs2 = s1.executeQuery("SELECT * FROM " + table_);//hold isol3
                ResultSet rs3 = s2.executeQuery("SELECT * FROM " + table_);//hold isol3
                ResultSet rs4 = s3.executeQuery("SELECT * FROM " + table_);//nonhold isol3
                c.commit();

                assertCondition( cursorOpen(rs) && !cursorOpen(rs1) && cursorOpen(rs2) && cursorOpen(rs3) && !cursorOpen(rs4), "V7R1- *none keeps cursor open. (requires host support)" );     //@A2C
                rs.close();
                rs1.close();
                rs2.close();
                rs3.close();
                rs4.close();
                stmt.close();
                s.close();
                s1.close();
                s2.close();
                s3.close();
            }
            catch ( Exception e )
            {
                failed(e, "Unexpected Exception");
            }
        }
    }

/**
createStatement()
- An exception should be thrown when you try to set a statements holdability to
something other than on of the ResultSet constants.
**/
    public void Var016 ()
    {
        if ( checkCursorHoldabilitySupport() )
        {

            try
            {
                stmt=c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, 7);
                failed("Didn't throw SQLException");
            }
            catch ( Exception e )
            {
                try
                {
                    assertExceptionIsInstanceOf(e, "java.sql.SQLException");
                    stmt.close();
                }
                catch ( Exception s )
                {
                    failed(s, "Unexpected Exception");
                }
            }
        }
    }

/**
setHoldability()
- If the connections holdibility is set to CLOSE_CURSORS_AT_COMMIT,
the ResultSet objects should be closed when Connection.commit() is called.
**/
    public void Var017 ()
    {
        if ( checkCursorHoldabilitySupport() )
        {
            try
            {
                c.setHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);
                PreparedStatement ps=c.prepareStatement("SELECT * FROM " + table_, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = ps.executeQuery();                 
                c.commit();

                assertCondition( !cursorOpen(rs) , "V7R1- *none keeps cursor open. (requires host support)");
                rs.close();
                ps.close();
            }
            catch ( Exception e )
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setHoldability()
- If the connections holdability is set to HOLD_CURSORS_OVER_COMMIT,
the ResultSet objects should not be closed when Connection.commit() is called.
**/
    public void Var018 ()
    {
        if ( checkCursorHoldabilitySupport() )
        {
            try
            {
                c.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                PreparedStatement ps=c.prepareStatement("SELECT * FROM " + table_, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = ps.executeQuery();
                c.commit();
                assertCondition( cursorOpen(rs) );  //@A2C
                rs.close();
                ps.close();
            }
            catch ( Exception e )
            {
                failed (e, "Unexpected Exceptions");
            }
        }
    }

/**
setHoldability()
prepareStatement()
- Sets a connections holdability and sets a prepared statements holdability.
**/
    public void Var019 ()
    {
        if ( checkCursorHoldabilitySupport() )
        {
            try
            {
                c.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                PreparedStatement ps=c.prepareStatement("SELECT * FROM " + table_, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
                ResultSet rs = ps.executeQuery();
                c.commit();

                assertCondition( !cursorOpen(rs) , "V7R1- *none keeps cursor open. (requires host support)");
                rs.close();
                ps.close();
            }
            catch ( Exception e )
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setHoldability()
getHoldability()
getResultSetHoldability()
- This method sets the connections cursor Holdability and then checks to see
if a prepared statement created with this connection has the same holdability.
**/
    public void Var020 ()
    {
        if ( checkCursorHoldabilitySupport() )
        {
            try
            {
                c.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                PreparedStatement ps = c.prepareStatement("SELECT * FROM " + table_ , ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                assertCondition( c.getHoldability() == ps.getResultSetHoldability() );
                ps.close();
            }
            catch ( Exception e )
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setHoldability()
getHoldability()
getResultSetHoldability()
- This method sets the connections cursor holdability and then checks to see 
if a prepared statement created with this connection has the same holdability.
**/
    public void Var021 ()
    {
        if ( checkCursorHoldabilitySupport() )
        {
            try
            {
                c.setHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);
                PreparedStatement ps = c.prepareStatement("SELECT * FROM " + table_ , ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                assertCondition( c.getHoldability() == ps.getResultSetHoldability() );
                ps.close();
            }
            catch ( Exception e )
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setHoldability()
getHoldability()
getResultSetHoldability()
- This method sets the connections cursor holdability and then checks to see 
if several prepared statements created with this connection have the same holdability.
**/
    public void Var022 ()
    {
        if ( checkCursorHoldabilitySupport() )
        {
            try
            {
                c.setHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);
                PreparedStatement ps = c.prepareStatement("SELECT * FROM " + table_ , ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                PreparedStatement ps1 = c.prepareStatement("SELECT * FROM " + table_ , ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                assertCondition( (c.getHoldability() == ps.getResultSetHoldability()) && (c.getHoldability() == ps1.getResultSetHoldability()) );
                ps.close();
                ps1.close();
            }
            catch ( Exception e )
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setHoldability()
getHoldability()
getResultSetHoldability()
- This method sets the connections cursor holdability and then checks to see 
if several prepared statements created with this connection have the same holdability.
**/
    public void Var023 ()
    {
        if ( checkCursorHoldabilitySupport() )
        {
            try
            {
                c.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                PreparedStatement ps = c.prepareStatement("SELECT * FROM " + table_ , ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                PreparedStatement ps1 = c.prepareStatement("SELECT * FROM " + table_ , ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                assertCondition( (c.getHoldability() == ps.getResultSetHoldability()) && (c.getHoldability() == ps1.getResultSetHoldability()) );
                ps.close();
                ps1.close();
            }
            catch ( Exception e )
            {
                failed (e, "Unexpected Exception");
            }
        }
    } 

/**
setHoldablility()
getHoldabililty()
getResultSetHoldability()
prepareStatement()
- This method sets the connections cursor holdability and then checks to see if
a prepared statment created with the connection has the same holdability.  Then the prepared statements
holdability is overrided.
**/
    public void Var024 ()
    {
        if ( checkCursorHoldabilitySupport() )
        {
            try
            {
                c.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                PreparedStatement ps = c.prepareStatement("SELECT * FROM " + table_ , ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                int holdability = ps.getResultSetHoldability();
                ps=c.prepareStatement("SELECT * FROM " + table_, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
                assertCondition( (ps.getResultSetHoldability() != c.getHoldability()) && (holdability == c.getHoldability()) ); 
                ps.close();
            }
            catch ( Exception e )
            {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setHoldabiltiy()
prepareStatement()
- This method sets the connections holdability and then sets several prepared statements holdabilities
and makes sure the correct thing happens on commit.
**/
    public void Var025 ()
    {
        if ( checkCursorHoldabilitySupport() )
        {
            try
            {
                c.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                PreparedStatement ps = c.prepareStatement("SELECT * FROM " + table_ , ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                PreparedStatement ps1 = c.prepareStatement("SELECT * FROM " + table_ , ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
                PreparedStatement ps2 = c.prepareStatement("SELECT * FROM " + table_ , ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
                PreparedStatement ps3 = c.prepareStatement("SELECT * FROM " + table_ , ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
                PreparedStatement ps4 = c.prepareStatement("SELECT * FROM " + table_ , ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
                ResultSet rs = ps.executeQuery();
                ResultSet rs1 = ps1.executeQuery();
                ResultSet rs2 = ps2.executeQuery();
                ResultSet rs3 = ps3.executeQuery();
                ResultSet rs4 = ps4.executeQuery();
                c.commit();
                
                assertCondition( cursorOpen(rs) && !cursorOpen(rs1) && cursorOpen(rs2) && !cursorOpen(rs3) && cursorOpen(rs4) , "V7R1- *none keeps cursor open. (requires host support)" );  
                rs.close();
                rs1.close();
                rs2.close();
                rs3.close();
                rs4.close();
                ps.close();
                ps1.close();
                ps2.close();
                ps3.close();
                ps4.close();
            }
            catch ( Exception e )
            {
                failed(e, "Unexpected Exception");
            }
        }
    }

/**
prepareStatement()
- An exception should be thrown when you try to set a prepared statements holdability to
something other than on of the ResultSet constants.
**/
    public void Var026 ()
    {
        if ( checkCursorHoldabilitySupport() )
        {

            try
            {
                PreparedStatement ps = c.prepareStatement("SELECT * FROM " + table_ , ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, 8);
                failed("Didn't throw SQLException  "+ps);
            }
            catch ( Exception e )
            {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setHoldability()
- If the connections holdibility is set to CLOSE_CURSORS_AT_COMMIT,
the ResultSet objects should be closed when Connection.commit() is called.
**/
    public void Var027 ()
    {
        if ( checkCursorHoldabilitySupport() )
        {
	    String sql = "? = CALL " + procedureName + "()"; 
            try
            {      
                c.setHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);
                CallableStatement cs=c.prepareCall(sql);
                cs.execute();                 
                ResultSet rs = cs.getResultSet();
                c.commit();
                //isol 2
               
                assertCondition( !cursorOpen(rs), "V7R1- *none keeps cursor open. (requires host support)" );  
                rs.close();
                cs.close();
            }
            catch ( Exception e )
            {
                failed (e, "Unexpected Exception SQL="+sql);
            }
        }
    }

/**
setHoldability()
- If the connections holdability is set to HOLD_CURSORS_OVER_COMMIT,
the ResultSet objects should not be closed when Connection.commit() is called.
**/
    public void Var028 ()
    {
        if ( checkCursorHoldabilitySupport() )
        {
            try
            {
                c.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                CallableStatement cs=c.prepareCall(" ? = CALL " + procedureName + "()");
                cs.execute();
                ResultSet rs = cs.getResultSet();
                c.commit();
                //TB v5r4 no cursor info returned from hostserver, v6r1 has cursor hold support returned from hostserver,  v7r1 has isolation support returned from hostserver)
                 if( getRelease() > JDTestDriver.RELEASE_V7R5M0) {
                    //isol 2
                    assertCondition( !cursorOpen(rs) , "> V7R5 *none should close cursor open. (requires host support)" );  
                }else{
                    assertCondition( cursorOpen(rs) , "<= V7R5 *none keeps cursor open. (requires host support)" );  
                }
                rs.close();
                cs.close();
            }
            catch ( Exception e )
            {
                failed (e, "Unexpected Exceptions");
            }
        }
    }

/**
setHoldability()
prepareCall()
- Sets a connections holdability and sets a callable statements holdability.
**/
    public void Var029 ()
    {
        if ( checkCursorHoldabilitySupport() )
        {
            try
            {
                c.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                CallableStatement cs=c.prepareCall(" ? = CALL " + procedureName + "()", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
                cs.execute();
                ResultSet rs = cs.getResultSet();
                c.commit();
                //isol 2
                assertCondition( !cursorOpen(rs),  "V7R1- *none keeps cursor open. (requires host support)" ); 
                rs.close();
                cs.close();
            }
            catch ( Exception e )
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setHoldability()
getHoldability()
getResultSetHoldability()
- This method sets the connections cursor Holdability and then checks to see
if a callable statement created with this connection has the same holdability.
**/
    public void Var030 ()
    {
        if ( checkCursorHoldabilitySupport() )
        {
            try
            {
                c.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                CallableStatement cs=c.prepareCall(" ? = CALL " + procedureName + "()");
                assertCondition( c.getHoldability() == cs.getResultSetHoldability() );
                cs.close();
            }
            catch ( Exception e )
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setHoldability()
getHoldability()
getResultSetHoldability()
- This method sets the connections cursor holdability and then checks to see 
if a callable statement created with this connection has the same holdability.
**/
    public void Var031 ()
    {
        if ( checkCursorHoldabilitySupport() )
        {
            try
            {
                c.setHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);
                CallableStatement cs=c.prepareCall(" ? = CALL " + procedureName + "()");
                assertCondition( c.getHoldability() == cs.getResultSetHoldability() );
                cs.close();
            }
            catch ( Exception e )
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setHoldability()
getHoldability()
getResultSetHoldability()
- This method sets the connections cursor holdability and then checks to see 
if several callable statements created with this connection have the same holdability.
**/
    public void Var032 ()
    {
        if ( checkCursorHoldabilitySupport() )
        {
            try
            {
                c.setHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);
                CallableStatement cs=c.prepareCall(" ? = CALL " + procedureName + "()");
                CallableStatement cs1=c.prepareCall(" ? = CALL " + procedureName + "()");
                assertCondition( (c.getHoldability() == cs.getResultSetHoldability()) && (c.getHoldability() == cs1.getResultSetHoldability()) );
                cs.close();
                cs1.close();
            }
            catch ( Exception e )
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setHoldability()
getHoldability()
getResultSetHoldability()
- This method sets the connections cursor holdability and then checks to see 
if several callable statements created with this connection have the same holdability.
**/
    public void Var033 ()
    {
        if ( checkCursorHoldabilitySupport() )
        {
            try
            {
                c.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                CallableStatement cs=c.prepareCall(" ? = CALL " + procedureName + "()");
                CallableStatement cs1=c.prepareCall(" ? = CALL " + procedureName + "()");
                assertCondition( (c.getHoldability() == cs.getResultSetHoldability()) && (c.getHoldability() == cs1.getResultSetHoldability()) );
                cs.close();
                cs1.close();
            }
            catch ( Exception e )
            {
                failed (e, "Unexpected Exception");
            }
        }
    } 

/**
setHoldablility()
getHoldabililty()
getResultSetHoldability()
prepareCall()
- This method sets the connection's cursor holdability and then checks to see if
a callable statment created with the connection has the same holdability.  Then the callable statements
holdability is overrided.
**/
    public void Var034 ()
    {
        if ( checkCursorHoldabilitySupport() )
        {
            try
            {
                c.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                CallableStatement cs=c.prepareCall(" ? = CALL " + procedureName + "()");
                int holdability = cs.getResultSetHoldability();
                cs=c.prepareCall(" ? = CALL " + procedureName + "()", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
                assertCondition( (cs.getResultSetHoldability() != c.getHoldability()) && (holdability == c.getHoldability()) ); 
                cs.close();
            }
            catch ( Exception e )
            {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setHoldabiltiy()
prepareCall()
- This method sets the connection's holdability and then sets two callable statements' holdability
and makes sure the correct thing happens on commit.

//@A3C Had to change because the iSeries gives an error when more than one statement is executed
on the same stored procedure.  It's valid JDBC, but a server limitation.
**/
    public void Var035 ()
    {
	if ((getRelease() <= JDTestDriver.RELEASE_V7R2M0) &&
	    (isToolboxDriver())) {
	    notApplicable("Not working in V7R1/V7R2 for toolbox driver");
	    return; 
	} 

        if ( checkCursorHoldabilitySupport() )
        {
            try
            {
                c.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                CallableStatement cs=c.prepareCall(" ? = CALL " + procedureName + "()", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
                cs.execute();
                ResultSet rs = cs.getResultSet();
                c.commit();
                boolean result = cursorOpen(rs);//positive logic - close due to isol 2
                rs.close();
                cs.close();
                CallableStatement cs1=c.prepareCall(" ? = CALL " + procedureName + "()", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                cs1.execute();
                ResultSet rs1 = cs1.getResultSet();
                c.commit();
                boolean result1 = cursorOpen(rs1);
                rs1.close();
                cs1.close();
                CallableStatement cs2=c.prepareCall(" ? = CALL " + procedureName + "()", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
                cs2.execute();
                ResultSet rs2 = cs2.getResultSet();
                c.commit();
                boolean result2 = cursorOpen(rs2);
                rs2.close();
                cs2.close();
                //TB v5r4 no cursor info returned from hostserver, v6r1 has cursor hold support returned from hostserver,  v7r1 has isolation support returned from hostserver)
                if( (getRelease() > JDTestDriver.RELEASE_V7R5M0)){
                    assertCondition( !result && !result1 && !result2, "> V7R5  *none keeps cursor open. (requires host support)" );  
                }else{
                    assertCondition( !result && result1 && result2, "<= V7R5 *none keeps cursor open. (requires host support)" );  
                }
            }
            catch ( Exception e )
            {
                failed(e, "Unexpected Exception");
            }
        }
    }

/**
prepareCall()
- An exception should be thrown when you try to set a callable statements holdability to
something other than on of the ResultSet constants.
**/
    public void Var036 ()
    {
        if ( checkCursorHoldabilitySupport() )
        {

            try
            {
                CallableStatement cs1=c.prepareCall(" ? = CALL " + procedureName + "()", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, 9);
                failed("Didn't throw SQLException "+cs1);
            }
            catch ( Exception e )
            {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }

/**
setHoldability()
getHoldability()
getResultSetHoldability()
- This method sets the connections cursor holdability and then checks to see 
if a statement, prepared statement and a callable statement created with this 
connection have the same holdability.
**/
    public void Var037 ()
    {
        if ( checkCursorHoldabilitySupport() )
        {
            try
            {
                c.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                CallableStatement cs=c.prepareCall(" ? = CALL " + procedureName + "()");
                stmt=c.createStatement();
                PreparedStatement ps = c.prepareStatement("SELECT * FROM " + table_ , ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                assertCondition( (c.getHoldability() == cs.getResultSetHoldability()) && (c.getHoldability() == stmt.getResultSetHoldability()) && (c.getHoldability() == ps.getResultSetHoldability()) );
                cs.close();
                stmt.close();
                ps.close();
            }
            catch ( Exception e )
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setHoldability()
- If the connections holdibility is set to CLOSE_CURSORS_AT_COMMIT,
the ResultSet objects should be closed when Connection.commit() is called.  With prefetch off.
**/
    public void Var038 ()  //@B1
    {
	if (getDriver () == JDTestDriver.DRIVER_NATIVE ) {
	    if  (checkCursorHoldabilitySupport()) {


		try {
		    Properties p = new Properties();

		    p.put("block size", "0");
		    p.put("password", PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDConnectionCursorHoldability.38"));
		    p.put("user", userId_);
		    p.put("prefetch","false");

		    Connection c1 = testDriver_.getConnection (baseURL_ + ";data truncation=true;transaction isolation=repeatable read", p);

		    c1.setHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);
		    stmt=c1.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		    ResultSet rs = stmt.executeQuery("SELECT * FROM " + table_);
		    c1.commit();
		    assertCondition( !cursorOpen(rs) );   //@A2c
		    rs.close();
		    stmt.close();

		    c1.close();
		}
		catch ( Exception e )
		{
		    failed (e, "Unexpected Exception");
		}
	    }
	} else {
	    notApplicable("Native driver variation"); 
	} 
    }

/**
setHoldability()
- If the connections holdability is set to HOLD_CURSORS_OVER_COMMIT,
the ResultSet objects should not be closed when Connection.commit() is called.  With prefetch off.
**/
    public void Var039 ()  //@B1
    {
	if (getDriver () == JDTestDriver.DRIVER_NATIVE)
	    
	{
	    if (checkCursorHoldabilitySupport() )  {
		try
		{
		    Properties p = new Properties();

		    p.put("block size", "0");
		    p.put("password",  PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDConnectionCursorHoldability.39"));
		    p.put("user", userId_);
		    p.put("prefetch","false");

		    Connection c1 = testDriver_.getConnection (baseURL_ + ";data truncation=true", p);

		    c1.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
		    stmt=c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		    ResultSet rs = stmt.executeQuery("SELECT * FROM " + table_);
		    c1.commit();
		    assertCondition( cursorOpen(rs) );  //@A2C
		    rs.close();
		    stmt.close();

		    c1.close();
		}
		catch ( Exception e )
		{
		    failed (e, "Unexpected Exceptions");
		}
	    }
	} else {
	    notApplicable("Native driver variation"); 
        }
    }
}
