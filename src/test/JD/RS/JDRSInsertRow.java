///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSInsertRow.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.RS;

import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDRSTest;
import test.JDTestcase;



/**
Testcase JDRSInsertRow.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>insertRow()
<li>rowInserted()
</ul>
**/
public class JDRSInsertRow
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSInsertRow";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



    // Private data.
    private static  String key_            = "JDRSInsertRow";
    private static  String select_         = "SELECT * FROM "
                                                    + JDRSTest.RSTEST_UPDATE;
    private static  String noNullsTable_   = JDRSTest.COLLECTION + ".INSNONULL";

    private Statement           statement_;
    private Statement           statement2_;
    private ResultSet           rs_;



/**
Constructor.
**/
    public JDRSInsertRow (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSInsertRow",
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
        select_         = "SELECT * FROM " + JDRSTest.RSTEST_UPDATE;
	if (connection_ != null) connection_.close();
        if (isJdbc20 ()) {
            // SQL400 - driver neutral...
            String url = baseURL_
            // String url = "jdbc:as400://" + systemObject_.getSystemName()
                
                ;
            connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
            connection_.setAutoCommit(false); // @C1A
            statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
            statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);

            noNullsTable_   = JDRSTest.COLLECTION + ".INSNONULL";

            // Clear any existing rows.
            statement_.executeUpdate ("DELETE FROM " + JDRSTest.RSTEST_UPDATE);
    
            statement_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_UPDATE
                + " (C_KEY) VALUES ('" + key_ + "')");
    
            initTable(statement_, noNullsTable_, " (A INTEGER, B INTEGER NOT NULL, C INTEGER)");
    
            rs_ = statement_.executeQuery (select_ + " FOR UPDATE");
        }
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
        if (isJdbc20 ()) {
            rs_.close ();
    
            cleanupTable(statement_, noNullsTable_);
    
            statement_.close ();
            connection_.commit(); // @C1A
            connection_.close ();
        }
    }



/**
insertRow() - Should throw exception when the result set is
closed.
**/
    public void Var001()
    {
        if (checkJdbc20 ()) {
        try {
            Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_UPDATE + " FOR UPDATE");
            rs.moveToInsertRow ();
            rs.close ();
            rs.insertRow ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
insertRow() - Should throw exception when the result set is
not updatable.
**/
    public void Var002()
    {
        if (checkJdbc20 ()) {
        try {
            Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_UPDATE);
            rs.moveToInsertRow ();
            rs.insertRow ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
insertRow() - Should throw exception when cursor is not pointing
to a row.
**/
    public void Var003()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, null);
            rs_.insertRow ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
insertRow() - Should throw exception when cursor is not pointing
to the insert row.
**/
    public void Var004()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, null);
            rs_.next ();
            rs_.insertRow ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
insertRow() - Should throw an exception when inserting a row 
with a non-nullable column NOT specified. 
**/
    public void Var005()
    {
        if (checkJdbc20 ()) {
        try {
          /*
            Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSTest.COLLECTION + ".ISNONULL");
 */
            rs_.moveToInsertRow ();
            rs_.updateInt ("A", 747);
            rs_.updateInt ("C", -222);
            rs_.insertRow ();            
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }


    
/**
insertRow() - Should insert a row with all nulls when no updates
are pending.
**/
    public void Var006()
    {
	StringBuffer message = new StringBuffer(); 
        if (checkJdbc20 ()) {
        try {
            int columnCount = rs_.getMetaData ().getColumnCount ();
            int rowCountBefore = 0;
            rs_.beforeFirst ();
            while (rs_.next ())
                ++rowCountBefore;

            rs_.moveToInsertRow ();
            rs_.insertRow ();

            ResultSet rs2 = statement2_.executeQuery (select_);
            int rowCountAfter = 0;
            rs2.beforeFirst ();
            while (rs2.next ())
                ++rowCountAfter;

            rs2.last ();
            Object[] after = new Object[columnCount];
            boolean[] afterNulls = new boolean[columnCount];
            for (int i = 0; i < columnCount; ++i) {
                after[i] = rs2.getObject (i+1);
                afterNulls[i] = rs2.wasNull ();
            }
            rs2.close ();

            boolean success = true;
            for (int i = 0; i < columnCount; ++i) {
		// Note:  Column 27 is a row id, it will not be null because it is generated
		// C_ROWID       ROWID generated by default
		// this was added to JDRSTest in Sept 2006
		if (i != 27) { 
		    if (after[i] != null) {
			message.append("\n after["+i+"] != null "+after[i]); 
			success = false;
		    } 
		    if (afterNulls[i] != true) {
			message.append("\n afterNulls["+afterNulls[i]+"] != true"); 
			success = false;
		    }
		}
            }
	    
            assertCondition ((success) && (rowCountBefore + 1 == rowCountAfter),
			     message.toString()+"\n rowCounterBefore = "+rowCountBefore+" rowCountAfter = "+rowCountAfter+" before+1==after"); ;
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
insertRow() - Should insert a row with all but 1 null when 1 column
update is pending.
**/
    public void Var007()
    {
	StringBuffer sb = new StringBuffer(); 
        if (checkJdbc20 ()) {
        try {
            int columnCount = rs_.getMetaData ().getColumnCount ();
            int rowCountBefore = 0;
            rs_.beforeFirst ();
            while (rs_.next ())
                ++rowCountBefore;

            rs_.moveToInsertRow ();
            rs_.updateString ("C_VARCHAR_50", "I have JDBC fever!");
            rs_.insertRow ();            

            ResultSet rs2 = statement2_.executeQuery (select_);
            int rowCountAfter = 0;
            rs2.beforeFirst ();
            while (rs2.next ())
                ++rowCountAfter;

            rs2.last ();
            Object[] after = new Object[columnCount];
            boolean[] afterNulls = new boolean[columnCount];
            for (int i = 0; i < columnCount; ++i) {
                after[i] = rs2.getObject (i+1);
                afterNulls[i] = rs2.wasNull ();
            }
            int insertedColumn = rs2.findColumn ("C_VARCHAR_50") - 1;
            rs2.close ();

            boolean success = true;
            for (int i = 0; i < columnCount; ++i) {
		if (i != 27) {  /* skip the rowid row */ 

		    if (i == insertedColumn) {
			if (! after[i].equals ("I have JDBC fever!")) {
			    sb.append("\nfor after["+i+"] = "+after[i]+" sb 'I have JDBC fever!'"); 
			    success = false;
			}
			if (afterNulls[i] != false) {
			    sb.append("\nfor afterNull["+i+"] = "+afterNulls[i]+" sb false"); 

			    success = false;
			}
		    }
		    else {
			if (after[i] != null) {
			    sb.append("\nfor after["+i+"] = "+after[i]+" sb null"); 

			    success = false;
			}
			if (afterNulls[i] != true) {
			    sb.append("\nfor afterNull["+i+"] = "+afterNulls[i]+" sb true"); 
			    success = false;
			}
		    }
		}
            }
            assertCondition ((success) && (rowCountBefore + 1 == rowCountAfter), "success="+success+" rowcountBefore="+rowCountBefore+" rowCountAfter="+rowCountAfter+" query="+select_+" " +sb.toString());
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
insertRow() - Should insert a row with all but 3 nulls when exactly
3 column updates are pending.
**/
    public void Var008()
    {
        if (checkJdbc20 ()) {
        try {
            int columnCount = rs_.getMetaData ().getColumnCount ();
            int rowCountBefore = 0;
            rs_.beforeFirst ();
            while (rs_.next ())
                ++rowCountBefore;

            rs_.moveToInsertRow ();
            rs_.updateInt ("C_INTEGER", 365);
            rs_.updateNull ("C_SMALLINT");
            rs_.updateString ("C_CHAR_1", "%");
            rs_.insertRow ();            

            ResultSet rs2 = statement2_.executeQuery (select_);
            int rowCountAfter = 0;
            rs2.beforeFirst ();
            while (rs2.next ())
                ++rowCountAfter;

            rs2.last ();
            Object[] after = new Object[columnCount];
            boolean[] afterNulls = new boolean[columnCount];
            for (int i = 0; i < columnCount; ++i) {
                after[i] = rs2.getObject (i+1);
                afterNulls[i] = rs2.wasNull ();
            }
            int insertedColumn1 = rs2.findColumn ("C_INTEGER") - 1;
            rs2.findColumn ("C_SMALLINT") ;
            int insertedColumn3 = rs2.findColumn ("C_CHAR_1") - 1;
            rs2.close ();

            boolean success = true;
            for (int i = 0; i < columnCount; ++i) {
		if (i != 27) {  /* skip the rowid row */ 
		    if (i == insertedColumn1) {
			if (! after[i].equals (Integer.valueOf(365)))
			    success = false;
			if (afterNulls[i] != false)
			    success = false;
		    }
		    else if (i == insertedColumn3) {
			if (! after[i].equals ("%"))
			    success = false;
			if (afterNulls[i] != false)
			    success = false;
		    }
		    else {
			if (after[i] != null)
			    success = false;
			if (afterNulls[i] != true)
			    success = false;
		    }
		}
            }
            assertCondition ((success) &&
			     (rowCountBefore + 1 == rowCountAfter),
			     "success="+success+
			     " rowCountBefore="+rowCountBefore+
			     " rowCountAfters(sb+1)="+rowCountAfter+
			     " query="+select_);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
insertRow() - Inserted row should be reflected in the result set
being updated when not repositioned.
**/
    public void Var009()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.getMetaData ().getColumnCount ();

            rs_.moveToInsertRow ();
            rs_.updateString ("C_VARCHAR_50", "Space people");
            rs_.insertRow ();

            assertCondition (rs_.getString ("C_VARCHAR_50").equals ("Space people"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
insertRow() - Inserted row should be reflected in the result set
being updated when repositioned.
**/
    public void Var010()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.getMetaData ().getColumnCount ();

            rs_.moveToInsertRow ();
            rs_.updateString ("C_VARCHAR_50", "Martians");
            rs_.insertRow ();

            rs_.moveToCurrentRow ();
            rs_.last ();
            assertCondition (rs_.getString ("C_VARCHAR_50").equals ("Martians"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
insertRow() - Should insert appropriately when the result set
is generated by a SELECT with correlation names.
<P>
<B>Note:</B> The JDBC specification states that getting a value from a SMALLINT
database column with getObject() will return an java.lang.Integer object, not a 
java.lang.Short object.  This testcase has been changed to match that expectation.
**/
    public void Var011()
    {
        if (checkJdbc20 ()) {
        try {
            Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
            ResultSet rs1 = s.executeQuery (select_
                + " AS TESTCORR FOR UPDATE");
            int columnCount = rs1.getMetaData ().getColumnCount ();
            rs1.moveToInsertRow ();
            rs1.updateShort ("C_SMALLINT", (short) 77);
            rs1.insertRow ();
            rs1.close ();
            s.close ();

            ResultSet rs2 = statement2_.executeQuery (select_);
            rs2.last ();
            Object[] after = new Object[columnCount];
            boolean[] afterNulls = new boolean[columnCount];
            for (int i = 0; i < columnCount; ++i) {
                after[i] = rs2.getObject (i+1);
                afterNulls[i] = rs2.wasNull ();
            }
            int updatedColumn = rs2.findColumn ("C_SMALLINT") - 1;
            rs2.close ();

            boolean success = true;
            for (int i = 0; i < columnCount; ++i) {
		if (i != 27) {  /* skip the rowid row */ 
		    if (i == updatedColumn) {
			if ((! after[i].equals (Integer.valueOf(77)))
|| (afterNulls[i] != false))
success = false;
		    }
		    else {
			if ((after[i] != null) || (afterNulls[i] != true))
			    success = false;
		    }
		}
            }

            assertCondition (success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
insertRow() - Columns specified for a previous insert should not
be carried over to the next.
**/
    public void Var012()
    {
        if (checkJdbc20 ()) {
        try {
            int columnCount = rs_.getMetaData ().getColumnCount ();

            rs_.moveToInsertRow ();
            rs_.updateString ("C_VARCHAR_50", "Our friends in space");
            rs_.insertRow ();

            rs_.updateInt ("C_INTEGER", 253);
            rs_.insertRow ();

            ResultSet rs2 = statement2_.executeQuery (select_);
            rs2.last ();
            Object[] after = new Object[columnCount];
            boolean[] afterNulls = new boolean[columnCount];
            for (int i = 0; i < columnCount; ++i) {
                after[i] = rs2.getObject (i+1);
                afterNulls[i] = rs2.wasNull ();
            }
            int updatedColumn = rs2.findColumn ("C_INTEGER") - 1;
            rs2.close ();

            boolean success = true;
            for (int i = 0; i < columnCount; ++i) {
		if (i != 27) {  /* skip the rowid row */ 
		    if (i == updatedColumn) {
			if ((! after[i].equals (Integer.valueOf(253)))
			    || (afterNulls[i] != false))
			    success = false;
		    }
		    else {
			if ((after[i] != null) || (afterNulls[i] != true))
			    success = false;
		    }
		}
            }

            assertCondition (success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }


/**
rowInserted() - Should return false on all rows in another result set
after repositioning.
**/
    public void Var013()
    {
        if (checkJdbc20 ()) {
        try {
            rs_.moveToInsertRow ();
            rs_.updateInt ("C_INTEGER", 99999);
            rs_.updateNull ("C_SMALLINT");
            rs_.updateString ("C_CHAR_1", "(");
            rs_.insertRow ();            

            ResultSet rs2 = statement2_.executeQuery (select_);
            boolean success = true;
            while (rs2.next ())
                if (rs2.rowInserted () != false)
                    success = false;
            rs2.close ();
            
            assertCondition (success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
rowInserted() - Should return false on all rows in the updated result set
after repositioning.
**/
    public void Var014()
    {
        if (checkJdbc20 ()) {
        try {
            rs_.moveToInsertRow ();
            rs_.updateInt ("C_INTEGER", 68290);
            rs_.updateNull ("C_SMALLINT");
            rs_.updateString ("C_CHAR_1", ")");
            rs_.insertRow ();            

            rs_.beforeFirst ();
            boolean success = true;
            while (rs_.next ())
                if (rs_.rowInserted () != false)
                    success = false;
            
            assertCondition (success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



}



