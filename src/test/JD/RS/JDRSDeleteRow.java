///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSDeleteRow.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.RS;

import com.ibm.as400.access.AS400;

import test.JDRSTest;
import test.JDTestcase;
import test.JD.JDSerializeFile;

import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;



/**
Testcase JDRSDeleteRow.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>deleteRow()
<li>rowDeleted()
</ul>
**/
public class JDRSDeleteRow
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSDeleteRow";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



    // Private data.
    private static final String key_            = "JDRSDeleteRow";
    private static String select_         = "SELECT * FROM "
                                                    + JDRSTest.RSTEST_UPDATE+" A ORDER BY RRN(A)";

    private Statement           statement_;
    private Statement           statement2_;
    private ResultSet           rs_;



/**
Constructor.
**/
    public JDRSDeleteRow (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSDeleteRow",
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
	if (connection_ != null) connection_.close();

        select_         = "SELECT * FROM " + JDRSTest.RSTEST_UPDATE;
        if (isJdbc20 ()) {
            // SQL400 - driver neutral...
            String url = baseURL_;
            connection_ = testDriver_.getConnection (url,systemObject_.getUserId(),encryptedPassword_,"JDRSDeleteRow");
            connection_.setAutoCommit(false);   // @C1A
            statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
            statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
    
            // Clear any existing rows.
            statement_.executeUpdate ("DELETE FROM " + JDRSTest.RSTEST_UPDATE);
    
            // Create a few different rows.  The number of loops is arbitrary.
            for (int i = 1; i < 198; ++i)
                statement_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_UPDATE
                    + " (C_KEY) VALUES ('" + key_ + i + "')");
            connection_.commit();               // @C1A
    
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
            statement_.close ();
            connection_.close ();
        }
    }



/**
deleteRow() - Should throw exception when the result set is
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
                rs.next ();
                rs.close ();
                rs.deleteRow ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
deleteRow() - Should throw exception when the result set is
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
                rs.next ();
                rs.deleteRow ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
deleteRow() - Should throw exception when cursor is not pointing
to a row.
**/
    public void Var003()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.beforeFirst ();
                rs_.deleteRow ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
deleteRow() - Should throw exception when cursor is pointing
to the insert row.
**/
    public void Var004()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.moveToInsertRow ();
                rs_.deleteRow ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
deleteRow() - Should delete the first row when positioned there.
**/
    public void Var005()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.beforeFirst ();
                int rowCountBefore = 0;
                Vector<String> keysBefore = new Vector<String> ();
                while (rs_.next ()) {
                    ++rowCountBefore;
                    keysBefore.addElement (rs_.getString ("C_KEY"));
                }
    
                rs_.first ();
                rs_.deleteRow ();
    
                ResultSet rs2 = statement2_.executeQuery (select_);
                int rowCountAfter = 0;
                Vector<String> keysAfter = new Vector<String> ();
                while (rs2.next ()) {
                    ++rowCountAfter;
                    keysAfter.addElement (rs2.getString ("C_KEY"));
                }
                rs2.close ();
                 
                boolean success = true;
                int deletedRow = 0;
                for (int i = 0; i < rowCountBefore; ++i) {
                    if (i < deletedRow) {
                        if (! keysBefore.elementAt (i).equals (keysAfter.elementAt (i)))
                            success = false;
                    }               
                    else if (i > deletedRow) {
                        if (! keysBefore.elementAt (i).equals (keysAfter.elementAt (i-1)))
                            success = false;
                    }
                }
    
                assertCondition (success && (rowCountAfter == (rowCountBefore - 1)));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
deleteRow() - Should delete a middle row when positioned there.
**/
    public void Var006()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.beforeFirst ();
                int rowCountBefore = 0;
                Vector<String> keysBefore = new Vector<String> ();
                while (rs_.next ()) {
                    ++rowCountBefore;
                    keysBefore.addElement (rs_.getString ("C_KEY"));
                }
    
                rs_.absolute (4);
                rs_.deleteRow ();
    
                ResultSet rs2 = statement2_.executeQuery (select_);
                int rowCountAfter = 0;
                Vector<String> keysAfter = new Vector<String> ();
                while (rs2.next ()) {
                    ++rowCountAfter;
                    keysAfter.addElement (rs2.getString ("C_KEY"));
                }
                rs2.close ();
                 
                boolean success = true;
                int deletedRow = 3;

		String s1, s2;

                for (int i = 0; i < rowCountBefore; ++i) {

		    s1 = keysBefore.elementAt(i).toString();

                    if (i < deletedRow) {
			s2 = keysAfter.elementAt(i).toString();

//                        if (! keysBefore.elementAt (i).equals (keysAfter.elementAt (i)))
			if(! s1.equals(s2)){
			    output_.println(" "+i+". "+s2+" sb "+s1);
                            success = false;
			}
                    }               
                    else if (i > deletedRow) {
//                        if (! keysBefore.elementAt (i).equals (keysAfter.elementAt (i-1)))
			s2 = keysAfter.elementAt(i-1).toString();

			if(! s1.equals(s2)){
			    output_.println(" "+i+". "+s2+" sb "+s1);
                            success = false;
			}
                    }
                }
    
                assertCondition (success && (rowCountAfter == (rowCountBefore - 1)));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
deleteRow() - Should delete the last row when positioned there.
**/
    public void Var007()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.beforeFirst ();
                int rowCountBefore = 0;
                Vector<String> keysBefore = new Vector<String> ();
                while (rs_.next ()) {
                    ++rowCountBefore;
                    keysBefore.addElement (rs_.getString ("C_KEY"));
                }
    
                rs_.last ();
                rs_.deleteRow ();
    
                ResultSet rs2 = statement2_.executeQuery (select_);
                int rowCountAfter = 0;
                Vector<String> keysAfter = new Vector<String> ();
                while (rs2.next ()) {
                    ++rowCountAfter;
                    keysAfter.addElement (rs2.getString ("C_KEY"));
                }
                rs2.close ();
                 
                boolean success = true;
                int deletedRow = rowCountBefore - 1;
                for (int i = 0; i < rowCountBefore; ++i) {
                    if (i < deletedRow) {
                        if (! keysBefore.elementAt (i).equals (keysAfter.elementAt (i)))
                            success = false;
                    }               
                    else if (i > deletedRow) {
                        if (! keysBefore.elementAt (i).equals (keysAfter.elementAt (i-1)))
                            success = false;
                    }
                }
    
                assertCondition (success && (rowCountAfter == (rowCountBefore - 1)));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
deleteRow() - Should delete appropriately when the result set
is generated by a SELECT with correlation names.
**/
    public void Var008()
    {
        if (checkJdbc20 ()) {
          
          
          JDSerializeFile serializeFile = null;
          try {
           serializeFile = new JDSerializeFile(connection_, JDRSTest.RSTEST_UPDATE);
                Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
                ResultSet rs1 = s.executeQuery (select_
                    + " AS TESTCORR FOR UPDATE");
    
                 rs1.beforeFirst ();
                 int rowCountBefore = 0;
                 Vector<String> keysBefore = new Vector<String> ();
                 while (rs1.next ()) {
                     ++rowCountBefore;
                     keysBefore.addElement (rs1.getString ("C_KEY"));
                 }
     
                 rs1.absolute (81);
                 rs1.deleteRow ();
                 rs1.close ();
     
                 ResultSet rs2 = statement2_.executeQuery (select_);
                 int rowCountAfter = 0;
                 Vector<String> keysAfter = new Vector<String> ();
                 while (rs2.next ()) {
                     ++rowCountAfter;
                     keysAfter.addElement (rs2.getString ("C_KEY"));
                 }
                 rs2.close ();
                  
                 boolean success = true;
                 int deletedRow = 80;
                 for (int i = 0; i < rowCountBefore; ++i) {
                     if (i < deletedRow) {
                         if (! keysBefore.elementAt (i).equals (keysAfter.elementAt (i)))
                             success = false;
                     }               
                     else if (i > deletedRow) {
                         if (! keysBefore.elementAt (i).equals (keysAfter.elementAt (i-1)))
                             success = false;
                     }
                 }
     
                 assertCondition (success && (rowCountAfter == (rowCountBefore - 1)));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            } finally {
              if (serializeFile != null) {
                try {
                  serializeFile.close();
                } catch (SQLException e) {
                  e.printStackTrace();
                }
              }
            }
        }
    }        



// @C2A
/**
deleteRow() - Should delete a row when the cursor has a mixed case name.
**/
    public void Var009()
    {
        if (checkJdbc20 ()) {
          JDSerializeFile serializeFile = null;
          try {
           serializeFile = new JDSerializeFile(connection_, JDRSTest.RSTEST_UPDATE);
                Statement statement = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
		if (isToolboxDriver())
		   statement.setCursorName("MiXeD CaSe");
		else
		    statement.setCursorName("MiXeDCaSe");
                ResultSet rs = statement.executeQuery (select_ + " FOR UPDATE");

                rs.beforeFirst ();
                int rowCountBefore = 0;
                Vector<String> keysBefore = new Vector<String> ();
                while (rs.next ()) {
                    ++rowCountBefore;
                    keysBefore.addElement (rs.getString ("C_KEY"));
                }
    
                rs.absolute (56);
                rs.deleteRow ();
    
                ResultSet rs2 = statement2_.executeQuery (select_);
                int rowCountAfter = 0;
                Vector<String> keysAfter = new Vector<String> ();
                while (rs2.next ()) {
                    ++rowCountAfter;
                    keysAfter.addElement (rs2.getString ("C_KEY"));
                }
                rs2.close ();
                 
                boolean success = true;
                int deletedRow = 55;
                for (int i = 0; i < rowCountBefore; ++i) {
                    if (i < deletedRow) {
                        if (! keysBefore.elementAt (i).equals (keysAfter.elementAt (i)))
                            success = false;
                    }               
                    else if (i > deletedRow) {
                        if (! keysBefore.elementAt (i).equals (keysAfter.elementAt (i-1)))
                            success = false;
                    }
                }
                rs.close(); 
                statement.close(); 
                assertCondition (success && (rowCountAfter == (rowCountBefore - 1)));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            } finally {
              if (serializeFile != null) {
                try {
                  serializeFile.close();
                } catch (SQLException e) {
                  e.printStackTrace();
                }
              }
            }
        }
    }



/**
rowDeleted() - Should return false on all rows in another result set
after repositioning.
**/
    public void Var010()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (21);
                rs_.deleteRow ();
    
                ResultSet rs2 = statement2_.executeQuery (select_);
                boolean success = true;
                while (rs2.next ())
                    if (rs2.rowDeleted () != false)
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
rowDeleted() - Should return false on all rows in the updated result set
after repositioning.
**/
    public void Var011()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (65);
                rs_.deleteRow ();
    
                rs_.beforeFirst ();
                boolean success = true;
                while (rs_.next ())
                    if (rs_.rowDeleted () != false)
                        success = false;
                
                assertCondition (success);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }        


/**
rowDeleted() - Should return true on the deleted row 
when called immediately without repositioning.
**/
    public void Var012()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.absolute (42);
                rs_.deleteRow ();
                assertCondition (rs_.rowDeleted () == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }        



}



