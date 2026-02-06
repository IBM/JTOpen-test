///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSBatch.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDPSBatch.java
//
// Classes:      JDPSBatch
//
////////////////////////////////////////////////////////////////////////

package test.JD.PS;

import java.io.FileOutputStream;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDLobTest;
import test.JDPSTest;
import test.JD.JDSerializeFile;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JDTestcase;
import test.JTOpenTestEnvironment;
import test.JD.JDTestUtilities;



/**
Testcase JDPSBatch.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>addBatch()</li>
<li>clearBatch()</li>
<li>executeBatch()</li>
</ul>
**/
public class JDPSBatch
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDPSBatch";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDPSTest.main(newArgs); 
   }



    // Private data.
    private static String table_      = JDPSTest.COLLECTION + ".JDPSB";
    private static String table2_     = JDPSTest.COLLECTION + ".JDSB2";
    private static String insert0_    = "INSERT INTO " + table_ + "(NAME) VALUES ('NORMAN')";
    private static String insert1_    = "INSERT INTO " + table_ + "(NAME) VALUES (?)";
    private static String insert2_    = "INSERT INTO " + table_ + "(NAME, ID) VALUES (?, ?)";
    private static String insert3_    = "INSERT INTO " + table_ + "(NAME, ID) VALUES ('Vanilla Coke', ?)";  //@D1A
    private static String insert4_    = "INSERT INTO " + table_ + "(NAME, ID) VALUES (NULL, ?)";  //@D1A
    private static String insert5_    = "INSERT INTO " + table_ + "(NAME, ID) VALUES (?, 5)";  //@D1A
    private static String insert6_    = "INSERT INTO " + table_ + "(NAME, ID) VALUES (?, NULL)";  //@D1A
    private static String insert7_    = "INSERT INTO " + table_ + " ? ROWS VALUES (?, ?)";  //@D1A
    private static String insert8_    = "INSERT INTO " + table_ + " ? ROWS (NAME, ID) VALUES ('Mountain Dew', ?)";  //@D1A
    public static final int     WIDTH_          = 30000;                    //@K2A
    public static String tableLob1_  = JDPSTest.COLLECTION + ".BLOBLOC";      //@K2A
    public static String tableLob2_  = JDPSTest.COLLECTION + ".CLOBLOC";      //@K2A
    public static String tableLob3_  = JDPSTest.COLLECTION + ".CLOBLOC2";     //@K2A
    public static  String LARGECLOB_           = null;                      //@K2A

    public static String table4_  = JDPSTest.COLLECTION + ".GRAPHIC";	// @L2
    public static String table5_ = JDPSTest.COLLECTION + ".JDPSTEMP37";
    private static String insertT41_, insertT42_;		// @L2

    public static String tableBoolean_ = JDPSTest.COLLECTION + ".JDPSBBOOL"; 
    public static String insertBoolean_; 
    
    private static final int    forceErrorId_ = 555;

    protected Connection      connection2_;

    protected boolean  useBlockInsert = false;			// @L2
    
    int longRunTest = 0; 

    //
    // Record times for tests where blocked update is not used
    //

    long var032Time = 0;
    long var034Time = 0;
    long var037Time = 0;
    long var038Time = 0;
    long var041Time = 0;
    long var042Time = 0;
    long totalLongvarTime = 0; 

    static
    {
        StringBuffer buffer = new StringBuffer (WIDTH_);
        int actualLength = WIDTH_ - 2;
        for (int i = 1; i <= actualLength; ++i)
            buffer.append ("&");
        LARGECLOB_ = buffer.toString ();
    }

/**
Constructor.
**/
    public JDPSBatch (AS400 systemObject,
                      Hashtable<String,Vector<String>> namesAndVars,
                      int runMode,
                      FileOutputStream fileOutputStream,
                      
                      String password)
    {
        super (systemObject, "JDPSBatch",
               namesAndVars, runMode, fileOutputStream,
               password);
    }


    public JDPSBatch (AS400 systemObject,
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
     * Performs setup needed before running variations.
     * 
     * @exception Exception If an exception occurs.
     **/
    protected void setup() throws Exception {
      setup("");
    }

    protected void setup(String suffix) throws Exception {
      String url = baseURL_;
      connection_ = testDriver_.getConnection(url, userId_, encryptedPassword_);
      connection_.setAutoCommit(true); // for xa

      JDSerializeFile setupLock = null;
      try {
        setupLock = new JDSerializeFile(connection_, "JDPSSETUP");

        connection2_ = testDriver_.getConnection(url + ";lob threshold=1", userId_, encryptedPassword_); // @K2A

        Statement s = connection_.createStatement();

        table_ = JDPSTest.COLLECTION + ".JDPSB" + suffix;
        insert0_ = "INSERT INTO " + table_ + "(NAME) VALUES ('NORMAN')";
        insert1_ = "INSERT INTO " + table_ + "(NAME) VALUES (?)";
        insert2_ = "INSERT INTO " + table_ + "(NAME, ID) VALUES (?, ?)";
        insert3_ = "INSERT INTO " + table_ + "(NAME, ID) VALUES ('Vanilla Coke', ?)"; // @D1A
        insert4_ = "INSERT INTO " + table_ + "(NAME, ID) VALUES (NULL, ?)"; // @D1A
        insert5_ = "INSERT INTO " + table_ + "(NAME, ID) VALUES (?, 5)"; // @D1A
        insert6_ = "INSERT INTO " + table_ + "(NAME, ID) VALUES (?, NULL)"; // @D1A
        insert7_ = "INSERT INTO " + table_ + " ? ROWS VALUES (?, ?)"; // @D1A
        insert8_ = "INSERT INTO " + table_ + " ? ROWS (NAME, ID) VALUES ('Mountain Dew', ?)"; // @D1A

        table2_ = JDPSTest.COLLECTION + ".JDSB2" + suffix;

        table4_ = JDPSTest.COLLECTION + ".GRAPHIC" + suffix; // @L2
        table5_ = JDPSTest.COLLECTION + ".JDPST37" + suffix;

        insertT41_ = "INSERT INTO " + table4_ + " (COL1) VALUES(?) "; // @L2
        insertT42_ = "INSERT INTO " + table4_ + " (COL2) VALUES(?) "; // @L2

        tableLob1_ = JDPSTest.COLLECTION + ".BLOBLOC" + suffix; // @K2A
        tableLob2_ = JDPSTest.COLLECTION + ".CLOBLOC" + suffix; // @K2A
        tableLob3_ = JDPSTest.COLLECTION + ".CLOBLOC2" + suffix; // @K2A
        table4_ = JDPSTest.COLLECTION + ".GRAPHIC" + suffix; // @L2

        tableBoolean_ = JDPSTest.COLLECTION + ".JDPSBBOOL" + suffix;
        insertBoolean_ = "INSERT INTO " + tableBoolean_ + "(COL1,COL2) VALUES(?,?)";

        //
        // Note: no need to serialize files because suffix causes different files to be
        // used

        try {
          s.executeUpdate(" DROP TABLE " + table_);
        } catch (Exception e0) {
        }
        // We create a unique key so it is easy to force errors.
        s.executeUpdate("CREATE TABLE " + table_ + " (NAME VARCHAR(25), ID INTEGER, UNIQUE(ID))");

        s.executeUpdate("INSERT INTO " + table_ + " (ID) VALUES (" + forceErrorId_ + ")");

        try {
          s.executeUpdate(" DROP TABLE " + table2_);
        } catch (Exception e0) {
        }

        try {
          s.executeUpdate("CREATE TABLE " + table2_ + " (NAME VARCHAR(10), ID INTEGER)");
        } catch (Exception e) {
          e.printStackTrace();
        }

        try {
          s.executeUpdate(" DROP TABLE " + table4_);
        } catch (Exception e4) {
        }
        s.executeUpdate(
            "CREATE TABLE " + table4_ + " (COL1 GRAPHIC(200) ccsid 13488, COL2 VARGRAPHIC(200) ccsid 13488)"); // @L2

        try {
          s.executeUpdate(" DROP TABLE " + tableLob1_);
        } catch (Exception e1) {
        }
        s.executeUpdate("CREATE TABLE " + tableLob1_ + "(C_BLOB BLOB(" + WIDTH_ + "))"); // @K2A

        try {
          s.executeUpdate(" DROP TABLE " + tableLob2_);
        } catch (Exception e2) {
        }
        s.executeUpdate("CREATE TABLE " + tableLob2_ + "(C_CLOB CLOB(" + WIDTH_ + "))"); // @K2A

        try {
          s.executeUpdate(" DROP TABLE " + tableLob3_);
        } catch (Exception e3) {
          String message = e3.toString();
          if (message.indexOf("not found") < 0) {
            output_.println("Warning TABLE3 not dropped");
            e3.printStackTrace();
          }
        }
        s.executeUpdate("CREATE TABLE " + tableLob3_ + "(C_CLOB CLOB(" + WIDTH_ + "))"); // @K2A

        try {
          s.executeUpdate("DROP TABLE " + table5_); // @L1
        } catch (Exception e) {

        }
        s.executeUpdate("CREATE TABLE " + table5_ + " (X INT)"); // @ L1

        if (areBooleansSupported()) {
          try {
            s.executeUpdate(" DROP TABLE " + tableBoolean_);
          } catch (Exception e4) {
          }
          s.executeUpdate("CREATE TABLE " + tableBoolean_ + " (COL1 VARGRAPHIC(200) ccsid 13488, COL2 BOOLEAN)");

        }

        connection_.commit(); // for xa
        s.close();

        //
        // Randomly pick long running tests
        //

        Random random = new Random();
        longRunTest = random.nextInt(4);

      } finally {

        if (setupLock != null) {
          setupLock.close();
        }

      }
    }

/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
        Statement s = connection_.createStatement ();
        s.executeUpdate ("DROP TABLE " + table_);
        s.executeUpdate ("DROP TABLE " + table2_);
        s.executeUpdate ("DROP TABLE " + tableLob1_);          //@K2A
        s.executeUpdate ("DROP TABLE " + tableLob2_);          //@K2A
        s.executeUpdate ("DROP TABLE " + tableLob3_);          //@K2A
	s.executeUpdate("DROP TABLE "+table4_);			// @L2
        s.executeUpdate("DROP TABLE " +table5_); // @L1
	if (areBooleansSupported()) {
	  s.executeUpdate("DROP TABLE "+tableBoolean_);     
	  
	}
        s.close ();
        connection_.commit(); // for xa
        connection_.close ();
        connection_ = null; 

    }



/**
Counts the rows that match a pattern.
**/
    private int countRows (String pattern)
    throws SQLException
    {
        int count = 0;
	Statement s = connection_.createStatement ();
        ResultSet rs = s.executeQuery ("SELECT * FROM " + table_
			       + " WHERE NAME LIKE '" + pattern + "'");
        while (rs.next ())
            ++count;
        rs.close ();
	s.close();
        return count;
    }

    private int countRows (String pattern, int expectedCount) throws SQLException {
	int count = countRows(pattern);
	if (count != expectedCount) {
	    output_.println("Did not find "+expectedCount +" rows for "+pattern);
	    output_.println("File contains the following names");
	    Statement s = connection_.createStatement ();
	    ResultSet rs = s.executeQuery ("SELECT NAME FROM " + table_);
	    while (rs.next ()) {
		output_.println(rs.getString(1)); 
	    }
	    rs.close ();
	    s.close();

	}
	return count; 
    }

/**
Determines if the batch is clear.
**/
    private boolean isBatchCleared (Statement s)
    throws SQLException
    {
        // Verify that the batch was clear by counting the rows
        // and executing the batch again.
        int before = countRows ("%");
        s.executeBatch ();
        int after = countRows ("%");
        return(before == after);
    }



/**
addBatch() - Add a statement to the batch after the statement
has been closed.  Should throw an exception.
**/
    public void Var001 ()
    {



        if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement (insert0_);
                s.close ();
                s.addBatch ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
addBatch() - Add a statement that contains parameters to the batch,
but one has not been set.  Should throw an exception.
**/
    public void Var002 ()
    {

        if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement (insert2_);
                s.setString (1, "Dr. Pepper");
                s.addBatch ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
addBatch() - Add a statement that contains an inout parameters
to the batch.  Should throw an exception.
**/
    public void Var003 ()
    {
        if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement ("CALL "
                                                                    + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
                s.setInt (1, 55);
                s.setInt (3, 66);
                s.addBatch ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
addBatch() - Add a statement to the batch.  Verify that it does
not get executed at that time.
**/
    public void Var004 ()
    {
        if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement (insert1_);
                s.setString (1, "Diet Coke");
                s.addBatch ();
                int rows = countRows ("Diet Coke");
                s.close ();
                assertCondition (rows == 0);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
executeBatch() - Execute the batch when the statement is closed.
Should throw an exception.
**/
    public void Var005 ()
    {
        if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement (insert1_);
                s.setString (1, "Mt. Dew");
                s.addBatch ();
                s.close ();
                s.executeBatch ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }




/**
executeBatch() - Execute the batch when no statements have been
added to the batch.
**/
    public void Var006 ()
    {
        if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement (insert2_);
                s.setString (1, "Diet Mt. Dew");
                s.setInt (2, 543);
                int before = countRows ("%");
                int[] updateCounts = s.executeBatch ();
                int after = countRows ("%");
                s.close ();
                assertCondition ((updateCounts.length == 0) && (before == after));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
addBatch()/executeBatch() - Execute the batch when 1 statement
has been added to the batch.
**/
    public void Var007 ()
    {
	String message = " -- Updated 4/16/2010 for new toolbox insert behavior " ; 
        if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement (insert1_);
                s.setString (1, "Mello Yello");
                s.addBatch ();
                int[] updateCounts = s.executeBatch ();

                int rows = countRows ("Mello Yello%");
                s.close ();

               // if(false /* isToolboxDriver() */  )
               // {
               //     // -2 is the value of SUCCESS_NO_INFO
               //     if(updateCounts.length != 1 || updateCounts[0] != -2 || rows != 1)
               //     {
               //         failed("Incorrect return information from executeBatch() "+"updateCounts.length: " + updateCounts.length + " updateCounts[0]: " + updateCounts[0] + " rows: " + rows);
               //     }
               //     else
               //         succeeded();
               // }
               // else
                {
                    if(updateCounts.length != 1 || updateCounts[0] != 1 || rows != 1)
                    {
                        failed("Incorrect return information from executeBatch() updateCounts.length: " + updateCounts.length + " updateCounts[0]: " + updateCounts[0] + " rows: " + rows + message );
                    }
                    else
                        succeeded();
                }
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
addBatch()/executeBatch() - Execute the batch when 2 statements
have been added to the batch.
**/
    public void Var008 ()
    {
	String message = " -- Updated 4/16/2010 for new toolbox insert behavior " ; 

        if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement (insert2_);
                s.setString (1, "Shasta,Grape");
                s.setInt (2, 123);
                s.addBatch ();
                s.setString (1, "Shasta,Lemon Lime");
                s.setInt (2, 125);
                s.addBatch ();
                int[] updateCounts = s.executeBatch ();
                int rows = countRows ("Shasta%");
                s.close ();

                //if(false /* isToolboxDriver() */ )
                // {
                //   // -2 is the value of SUCCESS_NO_INFO
                //    if(updateCounts.length != 2 || updateCounts[0] != -2 || updateCounts[1] != -2 || rows != 2)
                //    {
                //        failed("Incorrect return information from executeBatch() updateCounts.length: " + updateCounts.length + " updateCounts[0]: " + updateCounts[0] + " updateCounts[1]: " + updateCounts[1] + " rows: " + rows);
                //    }
                //    else
                //        succeeded();
                //}
                //else
                {
                    if(updateCounts.length != 2 || updateCounts[0] != 1 || updateCounts[1] != 1 || rows != 2)
                    {
                        failed("Incorrect return information from executeBatch() updateCounts.length: " + updateCounts.length + " updateCounts[0]: " + updateCounts[0] + " updateCounts[1]: " + updateCounts[1] + " rows: " + rows + message );
                    }
                    else
                        succeeded();
                }
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
addBatch()/executeBatch() - Execute the batch when 5 statements
have been added to the batch.
**/
    public void Var009 ()
    {
	String message = " -- Updated 4/16/2010 for new toolbox insert behavior " ; 

        if (checkJdbc20 ()) {
            try {
		String str[] = {"Surge", "SurgeWithCaffiene","SurgeWithSugar", "SurgeWithCarbos","SurgeWithEverything" };

                PreparedStatement s = connection_.prepareStatement (insert1_);

		for(int i=0; i<str.length; i++){
		    s.setString(1, str[i]);
		    s.addBatch();
		}

                int[] updateCounts = s.executeBatch ();
                int rows = countRows ("Surge%");
		s.close();

		boolean success = true;
		Statement st = connection_.createStatement ();
		ResultSet rs = st.executeQuery ("SELECT * FROM " + table_ + " WHERE NAME LIKE 'Surge%'");
		for(int i=0; rs.next(); i++)
		    success = success && rs.getString(1).equals(str[i]);
		rs.close();
                st.close ();		

                // if(false /* isToolboxDriver()*/ ) 
                //{
                //    // -2 is the value of SUCCESS_NO_INFO
                //    if(updateCounts.length != 5
                //       || updateCounts[0] != -2
                //       || updateCounts[1] != -2
                //       || updateCounts[2] != -2
                //       || updateCounts[3] != -2
                //       || updateCounts[4] != -2
                //       || rows != 5)
                //    {
                //        String message2 = "updateCounts.length: " + updateCounts.length + " updateCounts[0]: " + updateCounts[0] +
                //                           " updateCounts[1]: " + updateCounts[1] + " updateCounts[2]: " + updateCounts[2] +
                //                           " updateCounts[3]: " + updateCounts[3] + " updateCounts[4]: " + updateCounts[4] + " rows: " + rows;
                //        failed("Incorrect return information from executeBatch() "+message2);
                //    }
                //   else
                //        succeeded();
               // }
                // else
                {
                    if(updateCounts.length != 5
                       || updateCounts[0] != 1
                       || updateCounts[1] != 1
                       || updateCounts[2] != 1
                       || updateCounts[3] != 1
                       || updateCounts[4] != 1
                       || rows != 5)
                    {
                        String message2 = "updateCounts.length: " + updateCounts.length + " updateCounts[0]: " + updateCounts[0] +
                                           " updateCounts[1]: " + updateCounts[1] + " updateCounts[2]: " + updateCounts[2] +
                                           " updateCounts[3]: " + updateCounts[3] + " updateCounts[4]: " + updateCounts[4] + " rows: " + rows;
                        failed("Incorrect return information from executeBatch() "+message2+" "+message);
                    }
                    else
                        succeeded();
                }
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
addBatch()/executeBatch() - Execute the batch when the statements
have update counts other than 1.
**/
    public void Var010 ()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s2 = connection_.createStatement ();
                for (int i = 1; i <= 8; ++i)
                    s2.executeUpdate ("INSERT INTO " + table_ + "(NAME) VALUES ('Diet Rite " + i + "')");
                for (int i = 1; i <= 4; ++i)
                    s2.executeUpdate ("INSERT INTO " + table_ + "(NAME) VALUES ('RC Cola " + i + "')");

                PreparedStatement s = connection_.prepareStatement ("UPDATE "
                                                                    + table_ + " SET ID=NULL WHERE NAME LIKE ?");
                s.setString (1, "Diet Rite%");
                s.addBatch ();
                s.setString (1, "RC Cola%");
                s.addBatch ();
                int[] updateCounts = s.executeBatch ();
                s2.close ();
                s.close ();

                //in v7r1, we do true batch of updates (not pseudo-batch), and so we cannot get updatecounts
		// Unless configured in V7R1, we do not do true batching of updates
		// unless the "use block update" property is set
    //            if(false /* isToolboxDriver() && true */ )
    //                assertCondition ((updateCounts.length == 2)
    //                        && (updateCounts[0] == -2)
    //                        && (updateCounts[1] == -2),
		//		     " updateCounts.length="+updateCounts.length+" sb 2 "+
		//		     " updateCounts[0]="+updateCounts[0]+" sb  -2 "+
		//		     " updateCounts[1]="+updateCounts[1]+" sb  -2 ");
    //            else
                    assertCondition ((updateCounts.length == 2)
                                 && (updateCounts[0] == 8)
                                 && (updateCounts[1] == 4));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
addBatch()/executeBatch() - Execute the batch SQL NULL parameters
are involved.
**/
    public void Var011 ()
    {
	String baseMessage = " -- Updated 4/16/2010 for new toolbox insert behavior " ; 
        if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement (insert2_);
                s.setNull (1, Types.VARCHAR);
                s.setInt (2, 767);
                s.addBatch ();
                s.setString (1, "Sprite");
                s.setNull (2, Types.INTEGER);
                s.addBatch ();
                int[] updateCounts = s.executeBatch ();

                Statement s2 = connection_.createStatement ();
                ResultSet rs = s2.executeQuery ("SELECT NAME FROM " + table_
                                                + " WHERE ID = 767");
                rs.next ();
                rs.getString (1);
                boolean success1 = rs.wasNull ();
                rs.close ();

                rs = s2.executeQuery ("SELECT ID FROM " + table_
                                      + " WHERE NAME='Sprite'");
                rs.next ();
                rs.getInt (1);
                boolean success2 = rs.wasNull ();
                rs.close ();
                s2.close ();

                s.close ();

                // if(false /* isToolboxDriver() */ )
                //{
                //    // -2 is the value of SUCCESS_NO_INFO
                //    if(updateCounts.length != 2
                //       || updateCounts[0] != -2
                //       || updateCounts[1] != -2
                //       || !success1 || !success2)
                //    {
                //        String message = "updateCounts.length: " + updateCounts.length + " updateCounts[0]: " + updateCounts[0] +
                //                           " updateCounts[1]: " + updateCounts[1] + " success1: " + success1 + " success2: " + success2;
                //        failed("Incorrect return information from executeBatch() "+message );
                //    }
                //    else
                //        succeeded();
                //}
                //else
                {
                    if(updateCounts.length != 2
                       || updateCounts[0] != 1
                       || updateCounts[1] != 1
                       || !success1 || !success2)
                    {
                        String message = "updateCounts.length: " + updateCounts.length + " updateCounts[0]: " + updateCounts[0] +
                                           " updateCounts[1]: " + updateCounts[1] + " success1: " + success1 + " success2: " + success2;
                        failed("Incorrect return information from executeBatch() "+message+baseMessage );
                    }
                    else
                        succeeded();


                }
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
addBatch() - Try to use the Statement form of addBatch().
**/
    public void Var012 ()
    {
        if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement (insert1_);
                s.addBatch ("INSERT INTO " + table_ + " (NAME) VALUES ('Squirt, Watermelon')");
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
addBatch()/executeBatch() - Execute the batch when the first of 5
statements results in an error.

SQL400 - the native JDBC driver will no longer allow statements to only
set the parameters that are changed for each execution.  Therefore, in order
for this test to work, I had to add setInt() calls for each of the rows that
is not the one with the failure.  This is done for compliance with the way
UDB JDBC on other platforms works.
**/
    public void Var013 ()
    {
        if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement (insert2_);

                s.setString (1, "Gatorade, Grape");
                s.setInt (2, forceErrorId_);
                s.addBatch ();
                s.setString (1, "Gatorade, Strawberry");
                s.setInt(2, 1302);
                s.addBatch ();
                s.setString (1, "Gatorade, Orange");
                s.setInt(2, 1303);
                s.addBatch ();
                s.setString (1, "Gatorade, Pickle-flavored");
                s.setInt(2, 1304);
                s.addBatch ();

                int[] updateCounts = null;
                boolean exceptionCaught = false;
                try {
                    updateCounts = s.executeBatch ();
                }
                catch (BatchUpdateException bue) {
                    updateCounts = bue.getUpdateCounts ();
                    exceptionCaught = true;
                }

                int rows = countRows ("Gatorade%");
                s.close ();


                //if(isToolboxDriver())
                //{
                    // -2 is the value of SUCCESS_NO_INFO
                //    if(exceptionCaught != true
                //       || updateCounts.length != 1
                //       || updateCounts[0] != 0
                //       || rows != 0)
                //   {
                //        String message = "exceptionCaught: " + exceptionCaught + " updateCounts.length: " + updateCounts.length + " rows: " + rows;
                //        failed("Incorrect return information from BatchUpdateException.getUpdateCounts() "+message);
                //    }
                //    else
                //        succeeded();
                //}
                //else
                //{
                    assertCondition ((exceptionCaught == true)
                                     && (updateCounts.length == 0)
                                     && (rows == 0));
                //}
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
addBatch()/executeBatch() - Execute the batch when the third of 5
statements results in an error.
**/
    public void Var014 ()
    {
        if (checkJdbc20 ()) {
	    int[] updateCounts = null; 
            try {
                PreparedStatement s = connection_.prepareStatement (insert2_);
                s.setString (1, "Welchs, Plain");
                s.setInt (2, 1401);
                s.addBatch ();
                s.setString (1, "Welchs, Grape");
                s.setInt (2, 1402);
                s.addBatch ();
                s.setString (1, "Welchs, Green Grape");
                s.setInt (2, forceErrorId_);
                s.addBatch ();            
                s.setString (1, "Welchs, Concord Grape");
                s.setInt (2, 1404);
                s.addBatch ();
                s.setString (1, "Welchs, Brown Grape");
                s.setInt (2, 1405);
                s.addBatch ();

                boolean exceptionCaught = false;
                try {
                    updateCounts = s.executeBatch ();
                }
                catch (BatchUpdateException bue) {
                    updateCounts = bue.getUpdateCounts ();
                    exceptionCaught = true;
                }

		int updateCountsLength = updateCounts.length;
		if (updateCountsLength < 2) {
		    int[] newUpdateCounts = new int[2];
		    for (int i = 0; i < 2; i++) {
			if (i < updateCountsLength) {
			    newUpdateCounts[i] = updateCounts[i];
			} else {
			    newUpdateCounts[i] = -9999; 
			}
		    }
		    updateCounts = newUpdateCounts; 
		} 

                int rows = countRows ("Welchs%");
                s.close ();

                if(isToolboxDriver())
                {
                    // -2 is the value of SUCCESS_NO_INFO
                    if(exceptionCaught != true
                       || updateCountsLength != 2
                       || updateCounts[0] != -2
                       || updateCounts[1] != -2
                       || rows != 2)
                    {
                        String message = "exceptionCaught: " + exceptionCaught + " updateCountsLength: " + updateCountsLength + 
                                           " updateCounts[0]: " + updateCounts[0] + "updateCounts[1]: " + updateCounts[1] +
                                           " rows: " + rows;
                        failed("Incorrect return information from BatchUpdateException.getUpdateCounts() "+message);
                    }
                    else
                        succeeded();
                }
                else
                {
		    String message = "exceptionCaught = "+exceptionCaught+
				       " updateCountsLength = "+updateCountsLength;
		    if(useBlockInsert)								// @L2
			assertCondition( exceptionCaught && updateCountsLength == 0, message);		// @L2
		    else									// @L2
			assertCondition ((exceptionCaught == true)
					 && (updateCountsLength == 2)
					 && (updateCounts[0] == 1)
					 && (updateCounts[1] == 1)
					 && (rows == 2),
					 "exceptionCaught is "+exceptionCaught+" sb true "+
					 "updateCounts.length is "+updateCountsLength+" sb 2 "+
					 "updateCounts[0] is "+updateCounts[0]+" sb 1 " +
					 "updateCounts[1] is "+updateCounts[1]+" sb 1 " +
					 "rows is "+rows+" sb 2");
		}
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
addBatch()/executeBatch() - Execute the batch when the last of 5
statements results in an error.
**/
    public void Var015 ()
    {
        if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement (insert2_);
                s.setString (1, "Store brand, Plain");
                s.setInt (2, 1501);
                s.addBatch ();
                s.setString (1, "Store brand, Chocolate");
                s.setInt (2, 1502);
                s.addBatch ();
                s.setString (1, "Store brand, Vanilla");
                s.setInt (2, 1503);
                s.addBatch ();
                s.setString (1, "Store brand, Bubble gum");
                s.setInt (2, 1504);
                s.addBatch ();
                s.setString (1, "Store brand, Cardboard");
                s.setInt (2, forceErrorId_);
                s.addBatch ();

                int[] updateCounts = null;
                boolean exceptionCaught = false;
                try {
                    updateCounts = s.executeBatch ();
                }
                catch (BatchUpdateException bue) {
                    updateCounts = bue.getUpdateCounts ();
                    exceptionCaught = true;
                }
		int updateCountsLength = updateCounts.length; 
		if (updateCountsLength < 4) {
		    int[] newUpdateCounts = new int[4];
		    for (int i = 0; i < 4; i++) {
			if (i < updateCountsLength) {
			    newUpdateCounts[i] = updateCounts[i];
			} else {
			    newUpdateCounts[i] = -9999; 
			}
		    }
		    updateCounts = newUpdateCounts; 
		} 

                int rows = countRows ("Store brand%");
                s.close ();

                if(isToolboxDriver())
                {
                    // -2 is the value of SUCCESS_NO_INFO
                    if(exceptionCaught != true
                       || updateCountsLength != 4
                       || updateCounts[0] != -2
                       || updateCounts[1] != -2
                       || updateCounts[2] != -2
                       || updateCounts[3] != -2
                       || rows != 4)
                    {
			String message = "exceptionCaught: " + exceptionCaught + " updateCountsLength: " + updateCountsLength + 
                                           " updateCounts[0]: " + updateCounts[0] + " updateCounts[1]: " + updateCounts[1] + 
                                           " updateCounts[2]: " + updateCounts[2] + " updateCounts[3]: " + updateCounts[3] +
                                           " rows: " + rows;
                        failed("Incorrect return information from BatchUpdateException.getUpdateCounts() "+message);
                    }
                    else
                        succeeded();
                }
                else
                {
		    if(useBlockInsert)								// @L2
			assertCondition( exceptionCaught && updateCountsLength == 0);		// @L2
		    else									// @L2
			assertCondition ((exceptionCaught == true)
					 && (updateCountsLength == 4)
					 && (updateCounts[0] == 1)
					 && (updateCounts[1] == 1)
					 && (updateCounts[2] == 1)
					 && (updateCounts[3] == 1)
					 && (rows == 4),
					 "exceptionCaught is "+exceptionCaught+" sb true "+
					 "updateCounts.length is "+updateCountsLength+" sb 4 "+
					 "updateCounts[0] is "+updateCounts[0]+" sb 1 " +
					 "updateCounts[1] is "+updateCounts[1]+" sb 1 " +
					 "updateCounts[2] is "+updateCounts[1]+" sb 1 " +
					 "updateCounts[3] is "+updateCounts[1]+" sb 1 " +
					 "rows is "+rows+" sb 4");
                }
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
addBatch()/executeBatch() - Execute the batch when the statement
contains a query.  Should throw an exception.  The spec also says that
if an exception is thrown the updateCounts should reflect the number
of statements successfully executed.
<P>
SQL400 - the native driver changed in v4r5 to not throw an exception when
the user passes a query through executeUpdate.  That means that queries can
be passed through a batch as well.  The update count for a query is always
-1, but the rest of the batch is expected to complete successfully.
**/
    public void Var016 ()
    {
        if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement ("SELECT * FROM QIWS.QCUSTCDT");
                s.addBatch ();

                int[] updateCounts = null;
                boolean exceptionCaught = false;
                try {
                    updateCounts = s.executeBatch ();
                }
                catch (BatchUpdateException bue) {
                    updateCounts = bue.getUpdateCounts ();
                    exceptionCaught = true;
                }

                s.close ();
                assertCondition ((exceptionCaught == true)
                                  && (updateCounts.length == 0));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
executeBatch() - Verify that executeBatch() clears the batch
after executing it (when no errors occurred).
**/
    public void Var017 ()
    {
        if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement (insert1_);
                s.setString (1, "Tab");
                s.addBatch ();
                int[] updateCounts = s.executeBatch ();
                boolean cleared = isBatchCleared (s);
                s.close ();
                assertCondition (cleared == true, "cleared = "+cleared+" updateCounts.length="+updateCounts.length);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
executeBatch() - Verify that executeBatch() clears the batch
after executing it (when errors occurred).
**/
    public void Var018 ()
    {
        if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement (insert2_);
                s.setString (1, "Juice, Plain");
                s.setInt (2, 444);
                s.addBatch ();
                s.setString (1, "Juice, Celery");
                s.setInt (2, forceErrorId_);
                s.addBatch ();

                try {
                    s.executeBatch ();
                }
                catch (BatchUpdateException bue) {
                    // Ignore.
                }

                boolean cleared = isBatchCleared (s);
                s.close ();
                assertCondition (cleared == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
clearBatch() - Clear the batch after the statement
has been closed.  Should throw an exception.
**/
    public void Var019 ()
    {
        if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement (insert0_);
                s.close ();
                s.clearBatch ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
clearBatch() - Verify that this has no effect when the batch
is already empty.
**/
    public void Var020 ()
    {
        if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement (insert2_);
                s.clearBatch ();
                boolean cleared = isBatchCleared (s);
                s.close ();
                assertCondition (cleared == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
clearBatch() - Verify that this works when the batch
is not empty.
**/
    public void Var021 ()
    {
        if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement (insert1_);
                s.setString (1, "7-Up");
                s.addBatch ();
                s.setString (1, "Diet 7-Up");
                s.addBatch ();
                s.clearBatch ();
                boolean cleared = isBatchCleared (s);
                s.close ();
                assertCondition (cleared == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


    /**
executeBatch() - Verify that Toolbox doesn't batch if a constant is in the statement.
**/
    public void Var022 ()
    {

        if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement (insert3_);
                s.setInt (1, 25);
                s.addBatch ();
                s.setInt (1, 7);
                s.addBatch ();
                s.executeBatch ();
                int rows = countRows ("Vanilla Coke%");
                s.close ();
                assertCondition (rows == 2);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception -> statement is "+insert3_);
            }
        }
    }


    /**
executeBatch() - Verify that Toolbox doesn't batch if a constant is in the statement.
**/
    public void Var023 ()
    {

        if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement (insert4_);
                s.setInt (1, 235);
                s.addBatch ();
                s.setInt (1, 245);
                s.addBatch ();
                s.executeBatch ();
                s.close ();
                succeeded();
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception -> statement is "+insert4_);
            }
        }
    }


/**
executeBatch() - Verify that Toolbox doesn't batch if a constant is in the statement.
**/
    public void Var024 ()
    {

        if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement (insert5_);
                s.setString (1, "Bob");
                s.addBatch ();
                s.executeBatch ();
                int rows = countRows ("Bob%");
                s.close ();
                assertCondition (rows == 1);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception -> statement is "+insert5_);
            }
        }
    }


    /**
executeBatch() - Verify that Toolbox doesn't batch if a constant is in the statement.
**/
    public void Var025 ()
    {

        if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement (insert6_);
                s.setString (1, "Sally");
                s.addBatch ();
                s.setString (1, "Sally");
                s.addBatch ();
                s.executeBatch ();
                int rows = countRows ("Sally%");
                s.close ();
                assertCondition (rows == 2);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception -> statement is "+insert6_);
            }
        }
    }


/**
executeBatch() - Verify that Toolbox supports statements of type INSERT INTO TABLE ? ROWS VALUES (?,?).
**/
// @ L1 - Native drive is not able to support INSERT INTO ... ROWS...
    public void Var026 ()
    {

	if (getDriver () == JDTestDriver.DRIVER_NATIVE){
	    notApplicable();
	}
	else{
	    if (checkJdbc20 ()) {
		try {
		    if(isToolboxDriver())
			notApplicable("work TODO statement is "+insert7_);
		    else
		    {
			PreparedStatement s = connection_.prepareStatement (insert7_);
			s.setInt (1, 2);
			s.setInt (2, 1024);
			s.setString(3, "Classic Coke");
			s.addBatch ();
			s.setInt (1, 2);
			s.setInt (2, 512);
			s.setString(3, "Classic Coke");
			s.addBatch ();
			s.executeBatch ();
			int rows = countRows ("Classic Coke%");
			s.close ();
			assertCondition (rows == 2);
		    }
		}
		catch (Exception e) {
		    failed (e, "Unexpected Exception: work TODO statement is "+insert7_);
		}
	    }
	}
    }

    /**
executeBatch() - Verify that Toolbox supports statement of type INSERT INTO TABLE ? ROWS VALUES (String,?).
**/
//  @ L1 - The native drive doesn't support INSERT INTO .... ROWS
    public void Var027 ()
    {

	if (getDriver () == JDTestDriver.DRIVER_NATIVE){
	    notApplicable();
	}
	else{
	    if (checkJdbc20 ()) {
		try {
		    if(isToolboxDriver() )
			notApplicable("work TODO -> statement is "+insert8_);
		    else
		    {
			PreparedStatement s = connection_.prepareStatement (insert8_);
			s.setInt (1, 130);
			s.addBatch ();
			s.setInt (1, 140);
			s.addBatch ();
			s.executeBatch ();
			int rows = countRows ("Mountain Dew%");
			s.close ();
			assertCondition (rows == 2);
		    }
		}
		catch (Exception e) {
		    failed (e, "Unexpected Exception: work TODO -> statement is "+insert8_);
		}
	    }
	}
    }
/**
@F1A
addBatch() - Verify that JTOpen Toolbox allows 32,767 statements to be added to a batch and verify they aren't executed.
**/
    public void Var028 ()
    {

	
        if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement (insert1_);
                for (int i = 0; i<32767; i++)
                {
                    s.setString(1, "Golf" + Integer.toString(i));
                    s.addBatch ();
                }
                int rows = countRows ("Golf%");
                if(rows != 0)
                    failed("Added by Toolbox 2/26/03:  Executed the Batch before calling executeBatch()");
                s.close();
                succeeded();
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception:  Added by Toolbox 2/26/03 -> JTOpen testcase only for more than 32,767 statements in a batch");
            }
        }
    }

/**
@F1A
addBatch() - Verify that JTOpen Toolbox allows more than 32,767 statements to be added to a batch.
**/
    public void Var029 ()
    {

        if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement (insert1_);
                for (int i = 0; i<40000; i++)
                {
                    s.setString(1, "Bunker" + Integer.toString(i));
                    s.addBatch ();
                }
                int rows = countRows("Bunker%");
                if(rows != 0)
                    failed("Added by Toolbox 2/26/03 -> Executed the Batch before calling executeBatch()");
                s.close();
                succeeded();
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception:  Added by Toolbox 2/26/03 -> JTOpen testcase only for more than 32,767 statements in a batch");
            }
        }
    }

/**
@F1A
addBatch() - Verify that Toolbox allows 32,767 statements to be added to a batch.
executeBatch() - Verify that the Toolbox can execute a batch with 32,767 statements.
**/
    public void Var030 ()
    {



	if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
	    if (true ) {
		    /* "Longing running test only used on JDK 1.5 for release >= V5R4M0" */
		assertCondition(true); 
		    return; 
	    } 
	} 

      if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement (insert1_);
                String jdbc = null;
                for (int i = 0; i<32767; i++)
                {
                    jdbc = "JDBC" + Integer.toString(i);
                    s.setString(1, jdbc);
                    s.addBatch ();
                }
                int updateCounts[]  = s.executeBatch();
                int rows = countRows("JDBC%");
                s.close();
                assertCondition(updateCounts.length == 32767 && rows == 32767);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception:  Added by Toolbox 2/26/03 -> JTOpen testcase only for more than 32,767 statements in a batch");
            }
        }
    }

/**
@F1A
addBatch() - Verify that Toolbox allows more than 32,767 statements to be added to a batch.
executeBatch() - Verify that the Toolbox can execute a batch with more than 32,767 statements.

@L2
Native doesn't allow this though when useBlockInsert option is set to true.
**/
    public void Var031 ()
    {


	//
	// To reduce runtime for group test.. Do not run for native driver for certain release/JDK combinations
	//
	if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
	    if (true &&  
                true) {
		    /* "Longing running test only used on JDK 1.5 for release >= V5R4M0" */
		  assertCondition(true); 
		    return; 
	    } 
	} 

            if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement (insert1_);
                for (int i = 0; i<37000; i++)
                {
                    s.setString(1, "Eagle"+ Integer.toString(i));
                    s.addBatch ();
                }
                int updateCounts[]  = s.executeBatch();
                int rows = countRows("Eagle%");
                s.close();

		assertCondition(updateCounts.length == 37000 && rows == 37000, "updateCount.length="+updateCounts.length+" sb 37000  rows="+rows+" sb 37000");
            }
            catch (Exception e) {
		failed (e, "Unexpected Exception:  Added by Toolbox 2/26/03 -> JTOpen testcase only for more than 32,767 statements in a batch");
            }
        }

    }

/**
@F1A
addBatch() - Verify that Toolbox allows more than 65,534 statements to be added to a batch.
executeBatch() - Verify that the Toolbox can execute a batch with more than 65.534 statements.

@L2
Native doesn't allow more than 32767 statements to be added to a batch with useBlockInsert option.
**/
    public void Var032 ()
    {

	long startTime = System.currentTimeMillis(); 
        //
        // To reduce runtime for group test.. Do not run for native driver for certain release/JDK combinations
        //
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) { 
        } 
        if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement (insert1_);
                for (int i = 0; i<65600; i++)
                {
                    s.setString(1, "Birdie"+ Integer.toString(i));
                    s.addBatch ();
                }
                int updateCounts[]  = s.executeBatch();
                int rows = countRows("Birdie%");
                s.close();

		assertCondition(updateCounts.length == 65600 && rows == 65600);
            }
            catch (Exception e) {
		failed (e, "Unexpected Exception:  Added by Toolbox 2/26/03 -> JTOpen testcase only for more than 32,767 statements in a batch");
            }
        }
	var032Time = System.currentTimeMillis() - startTime;
	totalLongvarTime += var032Time; 
    }

/**
@F1A
addBatch() - Verify that JTOpen Toolbox allows more than 65,534 statements to be added to a batch.
**/
    public void Var033 ()
    {

        //
        // To reduce runtime for group test.. Do not run for native driver for certain release/JDK combinations
        //
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) { 
        } 

        if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement (insert1_);
                for (int i = 0; i<65600; i++)
                {
                    s.setString(1, "SandTrap" + Integer.toString(i));
                    s.addBatch ();
                }
                int rows = countRows("SandTrap%");
                if(rows != 0)
                    failed("Added by Toolbox 2/26/03 -> Executed the Batch before calling executeBatch()");
                s.close();
                succeeded();
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception:  Added by Toolbox 2/26/03 -> JTOpen testcase only for more than 32,767 statements in a batch");
            }
        }
    }

/**
@F1A
addBatch() - Verify that Toolbox allows exactly 32000 statements to be added to a batch.
executeBatch() - Verify that the Toolbox can execute a batch with exactly 32000 statements.
**/
    public void Var034 ()
    {


	if (totalLongvarTime > 1200000) {
	    assertCondition(false, "Did not attempt, testcases running too long");
	    return; 
	}

	long startTime = System.currentTimeMillis(); 
        //
        // To reduce runtime for group test.. Do not run for native driver for certain release/JDK combinations
        //
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) { 
        } 
        if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement (insert1_);
                for (int i = 0; i<32000; i++)
                {
                    s.setString(1, "Bogie"+ Integer.toString(i));
                    s.addBatch ();
                }
                int updateCounts[]  = s.executeBatch();
                int rows = countRows("Bogie%");
                s.close();
                assertCondition(updateCounts.length == 32000 && rows == 32000);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception:  Added by Toolox 2/26/03 -> JTOpen testcase only for more than 32,767 statements in a batch");
            }
        }
	var034Time = System.currentTimeMillis() - startTime;
	totalLongvarTime += var034Time; 

    }
/**
@F1A
addBatch() - Verify that JTOpen Toolbox allows 32,766 statements to be added to a batch and verify they aren't executed.
**/
    public void Var035 ()
    {

        //
        // To reduce runtime for group test.. Do not run for native driver for certain release/JDK combinations
        //
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
                     notApplicable("JTOpen test");
		    return;
            
        } 
        if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement (insert1_);
                for (int i = 0; i<32766; i++)
                {
                    s.setString(1, "Putter" + Integer.toString(i));
                    s.addBatch ();
                }
                int rows = countRows ("Putter%");
                if(rows != 0)
                    failed("Added by Toolbox 2/26/03 -> Executed the Batch before calling executeBatch()");
                s.close();
                succeeded();
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception:  Added by Toolbox 2/26/03 -> JTOpen testcase only for 32,766 statements in a batch");
            }
        }
    }

/**
@F1A
addBatch() - Verify that JTOpen Toolbox allows 32,768 statements to be added to a batch and verify they aren't executed.
**/
    public void Var036 ()
    {

        if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement (insert1_);
                for (int i = 0; i<32768; i++)
                {
                    s.setString(1, "Woods" + Integer.toString(i));
                    s.addBatch ();
                }
                int rows = countRows ("Woods%");
                if(rows != 0)
                    failed("Added by Toolbox 2/26/03 -> Executed the Batch before calling executeBatch()");
                s.close();
                succeeded();
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception:  Added by Toolbox 2/26/03 -> JTOpen testcase only for more than 32,767 statements in a batch");
            }
        }
    }

/**
@F1A
addBatch() - Verify that Toolbox allows 32,766 statements to be added to a batch.
executeBatch() - Verify that the Toolbox can execute a batch with 32,766 statements.
**/
    public void Var037 ()
    {
	if (totalLongvarTime > 1200000) {
	    assertCondition(false, "Did not attempt, testcases running too long");
	    return; 
	}
	
long startTime = System.currentTimeMillis(); 
         if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement (insert1_);
                for (int i = 0; i<32766; i++)
                {
                    s.setString(1, "Irons"+ Integer.toString(i));
                    s.addBatch ();
                }
                int updateCounts[]  = s.executeBatch();
                int rows = countRows("Irons%");
                s.close();
                assertCondition(updateCounts.length == 32766 && rows == 32766);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception:  Added by Toolbox 2/26/03 -> JTOpen testcase only for 32,766 statements in a batch");
            }
        }
	var037Time = System.currentTimeMillis() - startTime;
	totalLongvarTime += var037Time; 
    }

/**
@F1A
addBatch() - Verify that Toolbox allows more than 32,767 statements to be added to a batch.
executeBatch() - Verify that the Toolbox can execute a batch with more than 32,767 statements.

@L2
Native doesn't allow more than 32767 statements to be added to a batch with useBlockInsert.
**/
    public void Var038 ()
    {

	if (totalLongvarTime > 1200000) {
	    assertCondition(false, "Did not attempt, testcases running too long");
	    return; 
	}

long startTime = System.currentTimeMillis(); 
       
        
      

        if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement (insert1_);
                for (int i = 0; i<32768; i++)
                {
                    s.setString(1, "Par"+ Integer.toString(i));
                    s.addBatch ();
                }
                int updateCounts[]  = s.executeBatch();
                int rows = countRows("Par%");
                s.close();

		assertCondition(updateCounts.length == 32768 && rows == 32768);
            }
            catch (Exception e) {

		failed (e, "Unexpected Exception:  Added by Toolbox 2/26/03 -> JTOpen testcase only for more than 32,767 statements in a batch");
            }
        }
	var038Time = System.currentTimeMillis() - startTime;
	totalLongvarTime += var038Time; 
    }

/**
@F1A
addBatch() - Verify that JTOpen Toolbox allows 31,999 statements to be added to a batch and verify they aren't executed.
**/
    public void Var039 ()
    {

       
        if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement (insert1_);
                for (int i = 0; i<31999; i++)
                {
                    s.setString(1, "Four" + Integer.toString(i));
                    s.addBatch ();
                }
                int rows = countRows ("Four%");
                if(rows != 0)
                    failed("Added by Toolbox 2/26/03 -> Executed the Batch before calling executeBatch()");
                s.close();
                succeeded();
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception:  Added by Toolbox 2/26/03 -> JTOpen testcase only for 32,766 statements in a batch");
            }
        }
    }

/**
@F1A
addBatch() - Verify that JTOpen Toolbox allows 32,001 statements to be added to a batch and verify they aren't executed.
**/
    public void Var040 ()
    {

        
        
        if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement (insert1_);
                for (int i = 0; i<32001; i++)
                {
                    s.setString(1, "Green" + Integer.toString(i));
                    s.addBatch ();
                }
                int rows = countRows ("Green%");
                if(rows != 0)
                    failed("Added by Toolbox 2/26/03 -> Executed the Batch before calling executeBatch()");
                s.close();
                succeeded();
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception:  Added by Toolbox 2/26/03 -> JTOpen testcase only for more than 32,767 statements in a batch");
            }
        }
    }

/**
@F1A
addBatch() - Verify that Toolbox allows 31999 statements to be added to a batch.
executeBatch() - Verify that the Toolbox can execute a batch with 31999 statements.
**/
    public void Var041 ()
    {

	if (totalLongvarTime > 1200000) {
	    assertCondition(false, "Did not attempt, testcases running too long");
	    return; 
	}

long startTime = System.currentTimeMillis(); 
        //
        

        if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement (insert1_);
                for (int i = 0; i<31999; i++)
                {
                    s.setString(1, "Fairway"+ Integer.toString(i));
                    s.addBatch ();
                }
                int updateCounts[]  = s.executeBatch();
                int rows = countRows("Fairway%");
                s.close();
                assertCondition(updateCounts.length == 31999 && rows == 31999);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception:  Added by Toolbox 2/26/03 -> JTOpen testcase only for 32,766 statements in a batch");
            }
        }
	var041Time = System.currentTimeMillis() - startTime;
	totalLongvarTime += var041Time;  
    }


/**
@F1A
addBatch() - Verify that Toolbox allows 32,001 statements to be added to a batch.
executeBatch() - Verify that the Toolbox can execute a batch with 32,001 statements.
**/
    public void Var042 ()
    {

	if (totalLongvarTime > 1200000) {
	    assertCondition(false, "Did not attempt, testcases running too long");
	    return; 
	}

long startTime = System.currentTimeMillis(); 
       

        if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement (insert1_);
                for (int i = 0; i<32001; i++)
                {
                    s.setString(1, "Tee"+ Integer.toString(i));
                    s.addBatch ();
                }
                int updateCounts[]  = s.executeBatch();
                int rows = countRows("Tee%");
                s.close();
                assertCondition(updateCounts.length == 32001 && rows == 32001);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception:  Added by Toolbox 2/26/03 -> JTOpen testcase only for more than 32,767 statements in a batch");
            }
        }
	var042Time = System.currentTimeMillis() - startTime;
	totalLongvarTime += var042Time; 
    }

/**
@F1A
addBatch() - Verify that Toolbox allows exactly 64000 statements to be added to a batch.
executeBatch() - Verify that the Toolbox can execute a batch with exactly 64000 statements.

@L2
Native doesn't allow more than 32767 statements to be added to a batch with useBlockInsert option.
**/
    public void Var043 ()
    {

	if (totalLongvarTime > 1200000) {
	    assertCondition(false, "Did not attempt, testcases running too long");
	    return; 
	}

      

        if (checkJdbc20 ()) {
            try {
                PreparedStatement s = connection_.prepareStatement (insert1_);
                for (int i = 0; i<64000; i++)
                {
                    s.setString(1, "Divot"+ Integer.toString(i));
                    s.addBatch ();
                }
                int updateCounts[]  = s.executeBatch();
                int rows = countRows("Divot%");
                s.close();

		assertCondition(updateCounts.length == 64000 && rows == 64000);
            }
            catch (Exception e) {
		failed (e, "Unexpected Exception:  Added by Toolbox 2/26/03 -> JTOpen testcase only for more than 32,767 statements in a batch");
            }
        }
    }

/**
@K1A
addBatch()
executeBatch()
execute()
Verify that we can set the parameters on a PreparedStatement, add the statements to a batch
and execute them and then set the parameters on a the same PreparedStatement and execute the 
PreparedStatement by itself
**/
    public void Var044 ()
    {

        if (checkJdbc20 ())
        {
            try
            {
                PreparedStatement s = connection_.prepareStatement (insert1_);
                for (int i = 0; i<5; i++)
                {
                    s.setString(1, "Telephone"+ Integer.toString(i));
                    s.addBatch ();
                }
                int updateCounts[]  = s.executeBatch();
                s.setString(1, "Telephone" + 6);
                s.execute();
                int rows = countRows("Telephone%");
                s.close();
                assertCondition(updateCounts.length == 5 && rows == 6);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception:  Added by Toolbox 4/9/03");
            }
        }
    }

/**
@K1A
addBatch()
executeBatch()
execute()
Verify that we can set the parameters on a PreparedStatement, add the statements to a batch
and execute them close the statement and reopen it and then set the parameters on a the same 
PreparedStatement and execute the PreparedStatement by itself
**/
    public void Var045 ()
    {

        if (checkJdbc20 ())
        {
            try
            {
                PreparedStatement s = connection_.prepareStatement (insert1_);
                for (int i = 0; i<5; i++)
                {
                    s.setString(1, "Televisions"+ Integer.toString(i));
                    s.addBatch ();
                }
                int updateCounts[]  = s.executeBatch();
                s.close();
                s = connection_.prepareStatement (insert1_);
                s.setString(1, "Televisions" + 6);
                s.execute();
                int rows = countRows("Televisions%");
                s.close();
                assertCondition(updateCounts.length == 5 && rows == 6);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception:  Added by Toolbox 4/9/03");
            }
        }
    }

// This variation tests how executeBatch() handles warning.  A warning should be handled as a warning not an Exception
     public void Var046 ()
     {

	 if(getDriver() == JDTestDriver.DRIVER_NATIVE){
	     if (checkJdbc20 ())
	     {
		 Connection conn = null ;
		 try
		 {
		     String url46 = baseURL_;
		     conn = testDriver_.getConnection (url46, userId_, encryptedPassword_);
		     conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		     conn.setAutoCommit(true); // for xa
		     
		   //  stmt.executeUpdate("DROP TABLE "+JDPSTest.COLLECTION + ".JDPSTEMP37");
		   //  stmt.executeUpdate("CREATE TABLE "+JDPSTest.COLLECTION + ".JDPSTEMP37 (X INT)");
		     PreparedStatement ps = conn.prepareStatement ("INSERT INTO " +table5_+" (X) VALUES (?)");
		     for (int i = 0; i<5; i++)
		     {
			 ps.setInt(1, (i%2));
			 ps.addBatch ();
		     }
		     int updateCounts[]  = ps.executeBatch();
		     PreparedStatement ps1 = conn.prepareStatement ("Select x from " +table5_+" where x = 1");
		     ps1.execute();
		     PreparedStatement ps2 = conn.prepareStatement ("Delete from " +table5_+" X where X = 0");
		     ps2.addBatch();
		     int updateCounts2[] =  ps2.executeBatch();
		     ps2 = conn.prepareStatement ("INSERT INTO " +table5_+" (X) VALUES (?)");
		     for (int i = 0; i<5; i++)
		     {
			 ps2.setInt(1, (i%2));
			 ps2.addBatch ();
		     }	
		     ps2.executeBatch();
		     assertCondition(updateCounts.length == 5 && updateCounts2.length == 1, updateCounts.length+" "+updateCounts2.length);
		 }
		 catch (Exception e){
		     failed(e, " Unexpected Exception - Native Driver Added Test");
		 } finally {
		     try {
			 if (conn != null) { conn.close(); }
			 conn = null; 
		     } catch (Exception e) {
			 output_.println("Warning:  Exception on testcase cleanup "+e);
			 e.printStackTrace(output_); 
		     } 
		 } 
	     }
	 }
	 else{
	     notApplicable();
	 }
     }

/**
@K1A
execute()
addBatch()
executeBatch()
Verify that we can set the parameters on a PreparedStatement and execute it, then set the parameters
and add the statements to a batch and execute them
**/
    public void Var047 ()
    {

        if (checkJdbc20 ())
        {
            try
            {
                PreparedStatement s = connection_.prepareStatement (insert1_);
                s.setString(1, "Satellite" + 6);
                s.execute();
                for (int i = 0; i<5; i++)
                {
                    s.setString(1, "Satellite"+ Integer.toString(i));
                    s.addBatch ();
                }
                int updateCounts[]  = s.executeBatch();
                int rows = countRows("Satellite%");
                s.close();
                assertCondition(updateCounts.length == 5 && rows == 6);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception:  Added by Toolbox 4/9/03");
            }
        }
    }

/**
@K1A
execute()
addBatch()
executeBatch()
Verify that we can set the parameters on a PreparedStatement and execute it and close it.
Then reopen the same statement and set the parameters and add the statements to a batch
and execute them.
**/
    public void Var048 ()
    {

        if (checkJdbc20 ())
        {
            try
            {
                PreparedStatement s = connection_.prepareStatement (insert1_);
                s.setString(1, "Laser" + 6);
                s.execute();
                s.close();
                s = connection_.prepareStatement (insert1_);
                for (int i = 0; i<5; i++)
                {
                    s.setString(1, "Laser"+ Integer.toString(i));
                    s.addBatch ();
                }
                int updateCounts[]  = s.executeBatch();
                int rows = countRows("Laser%");
                s.close();
                assertCondition(updateCounts.length == 5 && rows == 6);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception:  Added by Toolbox 4/9/03");
            }
        }
    }

//@K2A
/**
Adds a statement using a blob to a batch
**/
    public void Var049()
    {

 test.JD.JDSerializeFile pstestSet = null;
 StringBuffer messageBuffer = new StringBuffer();
        if (checkJdbc20 ())
        {
     /* WILSONJO @L3 make sure the table is empty before the test is ran */
     try{
      pstestSet = JDPSTest.getPstestSet(connection_);
      Statement s = connection_.createStatement();
      s.executeUpdate("DELETE from "+pstestSet.getName());
      s.close();
     }catch(Exception ex){}
 
            try
            {
                PreparedStatement ps = connection_.prepareStatement (
                                                                        "INSERT INTO " + pstestSet.getName()
                                                                        + " (C_BLOB) VALUES (?)");
                byte[] b = new byte[] { (byte) 1, (byte) -12, (byte) 57, (byte) 45,
                    (byte) -33, (byte) 5};
                byte[] b1 = new byte[] { (byte) 1, (byte) -12, (byte) 57, (byte) 45,
                    (byte) -33, (byte) 3};
                byte[] b2 = new byte[] { (byte) 1, (byte) -12, (byte) 57, (byte) 45,
                    (byte) -33, (byte) 1};

                ps.setBlob (1, new JDLobTest.JDTestBlob (b));
                ps.addBatch ();
                ps.setBlob (1, new JDLobTest.JDTestBlob (b1));
                ps.addBatch ();
                ps.setBlob (1, new JDLobTest.JDTestBlob (b2));
                ps.addBatch ();
                int updateCounts[]  = ps.executeBatch ();
                ps.close ();

                Statement s = connection_.createStatement();
                ResultSet rs = s.executeQuery ("SELECT C_BLOB FROM " + pstestSet.getName());
                boolean success = true;
                int count = 0;
                
                byte[] inspect = new byte[b.length];

  byte[] r1 = new byte[b.length];
  byte[] r2 = new byte[b.length];
  byte[] r3 = new byte[b.length];

  byte[] recvd = new byte[b.length];

                while(rs.next())
                {

                    java.sql.Blob check = rs.getBlob(1);

      if(count == 0){
                        inspect = b;
   r1 = check.getBytes (1, (int) check.length ());
   recvd = r1;
      }
      else if(count == 1){
                        inspect = b1;
   r2 = check.getBytes (1, (int) check.length ());
   recvd = r2;
      }
      else{
                        inspect = b2;
   r3 = check.getBytes (1, (int) check.length ());
   recvd = r3;
      }
                    
                    if(!areEqual (inspect, recvd))
                        success = false;

                     count++;
                }
                rs.close();
                s.close();

  messageBuffer.append("\nRow1: ");
  for(int i=0; i< r1.length; i++)
      messageBuffer.append(r1[i]+" ");

  messageBuffer.append("\nRow2: ");
  for(int i=0; i< r2.length; i++)
      messageBuffer.append(r2[i]+" ");

  messageBuffer.append("\nRow3: ");
  for(int i=0; i< r3.length; i++)
      messageBuffer.append(r3[i]+" ");

  messageBuffer.append("\n");

  assertCondition(updateCounts.length == 3 && success,"len = "+updateCounts.length+
    " SB 3 success = "+success+" SB true "+messageBuffer.toString() ); // @L2
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception:  Added by Toolbox 5/5/03 "+messageBuffer.toString());
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

//@K2A
/**
Adds a statement using a blob locator to a batch
**/
    public void Var050()
    {

        if (checkJdbc20 ())
        {
            try
            {
                PreparedStatement ps = connection2_.prepareStatement (
                                                                        "INSERT INTO " + tableLob1_
                                                                        + " (C_BLOB) VALUES (?)");

                byte[] b = new byte[] { (byte) 1, (byte) -12, (byte) 57, (byte) 45,
                    (byte) -33, (byte) 0};
                byte[] b1 = new byte[] { (byte) 1, (byte) -12, (byte) 57, (byte) 45,
                    (byte) -33, (byte) 1};
                byte[] b2 = new byte[] { (byte) 1, (byte) -12, (byte) 57, (byte) 45,
                    (byte) -33, (byte) 0};

                ps.setBlob (1, new JDLobTest.JDTestBlob (b));
                ps.addBatch ();
                ps.setBlob (1, new JDLobTest.JDTestBlob (b1));
                ps.addBatch ();
                ps.setBlob (1, new JDLobTest.JDTestBlob (b2));
                ps.addBatch ();
                int updateCounts[]  = ps.executeBatch ();
                ps.close ();

                Statement s = connection_.createStatement();
                ResultSet rs = s.executeQuery ("SELECT C_BLOB FROM " + tableLob1_);//pstestSet.getName());		// @L2
                boolean success = true;
                int count = 0;
                
                byte[] inspect = new byte[b.length];
                while(rs.next())
                {
                    if(count == 0)
                        inspect = b;
                    else if(count == 1)
                        inspect = b1;
                    else
                        inspect = b2;

                    java.sql.Blob check = rs.getBlob(1);
                    
                    if(!areEqual (inspect, check.getBytes (1, (int) check.length ())))
                    {
                        success = false;
                    }
                    count++;
                }
                rs.close();
                s.close();

                assertCondition(updateCounts.length == 3 && success);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception:  Added by Toolbox 5/5/03");
            }
        }
    }

//@K2A
/**
Adds a statement using a clob to a batch
**/
    public void Var051()
    {
	if (getDriver() != JDTestDriver.DRIVER_NATIVE && useBlockInsert) {  notApplicable("Native block insert test");  return;	} 
	StringBuffer messageBuffer = new StringBuffer(); 
        if (checkJdbc20 ())
        {
            try
            {
                PreparedStatement ps = connection_.prepareStatement (
                                                                        "INSERT INTO " + tableLob2_
                                                                        + " (C_CLOB) VALUES (?)");
                for (int i = 0; i<5; i++)
                {
                    ps.setClob (1, new JDLobTest.JDTestClob("Los Angeles" + Integer.toString(i)));
                    ps.addBatch ();
                }
                int updateCounts[]  = ps.executeBatch ();
                ps.close ();

                Statement s = connection_.createStatement();

                ResultSet rs = s.executeQuery("SELECT C_CLOB FROM " + tableLob2_);
                boolean success = true;
                int count = 0;
                while(rs.next())
                {
		    String tmp = rs.getString(1);						// @L2
		    String res = "Los Angeles" + Integer.toString(count);			// @L2
		    messageBuffer.append(""+(count+1)+". "+tmp+" sb "+res+"\n");			// @L2
                    if(!tmp.equals(res))							// @L2
//                  if(!rs.getString(1).equals("Los Angeles" + Integer.toString(count)))	// @L2

                    {
                        success = false;
                    }
                    count++;
                }
                rs.close();
                s.close();
                assertCondition(updateCounts.length == 5 && success, "updateCounts.length = "	// @L2
				+updateCounts.length+" SB 5; success = "+success+" SB true "+messageBuffer.toString());	// @L2
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception:  Added by Toolbox 5/5/03 "+messageBuffer.toString());
            }
        }
    }

//@K2A
/**
Adds a statement using a clob locator to a batch
**/
    public void Var052()
    {
	if (getDriver() == JDTestDriver.DRIVER_TOOLBOX & useBlockInsert) {  notApplicable("Don't run for toolbox and userBlockInsert");  return;	} 
	StringBuffer messageBuffer = new StringBuffer(); 
        if (checkJdbc20 ())
        {
            try
            {
                PreparedStatement ps = connection_.prepareStatement (
                                                                        "INSERT INTO " + tableLob3_
                                                                        + " (C_CLOB) VALUES (?)");
                for (int i = 0; i<5; i++)
                {
		    String str = LARGECLOB_+i;
		    messageBuffer.append("inserting ... "+i+".\n"+str+"\n");
		    messageBuffer.append("length = "+str.length()+"\n");
                    ps.setClob (1, new JDLobTest.JDTestClob (str));
                    ps.addBatch ();
                }
                int updateCounts[]  = ps.executeBatch ();
                ps.close ();

                Statement s = connection_.createStatement();

                ResultSet rs = s.executeQuery("Select * FROM " + tableLob3_);
                boolean success = true;
                int count = 0;
                while(rs.next())
                {
		    String recvd = rs.getString(1);
		    String expected = LARGECLOB_+count;
                    if(isToolboxDriver())
                    {
                        if(!recvd.equals(expected))
                           success = false;
                    }
                    else
                    {
                        if(!recvd.equals(expected) || updateCounts[count] != 1)
		        {
			    messageBuffer.append(count+". failed\n");
			    messageBuffer.append("updateCounts["+count+"] = "+updateCounts[count]+" sb 1\n");
			messageBuffer.append("\n"+(count+1)+". "+recvd+"\nsb " + expected +"\n");
			messageBuffer.append("\n length of (recvd, expected) =  ("+
					    recvd.length()+", "+expected.length()+")\n");
			    success = false;
	                }
                    }
                    count++;
                }
                rs.close();
                s.close();
                assertCondition(updateCounts.length == 5 && success, "updateCounts.length = "+
				updateCounts.length+" sb 5 and success = "+success+" sb true "+messageBuffer.toString());

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception:  Added by Toolbox 5/5/03 "+messageBuffer.toString());
            }
        }
    }

/**
Adds a statement using a graphic to a batch
**/
    public void Var053()		// @L2
    {
	StringBuffer messageBuffer = new StringBuffer(); 
        if (checkJdbc20 ())
        {
            try
	    {
                PreparedStatement ps = connection_.prepareStatement ( insertT41_ );

		int len = 5;
                for (int i = 0; i<len; i++)
                {
                    ps.setString (1, "Los Angeles" + Integer.toString(i));
                    ps.addBatch ();
                }
                int updateCounts[]  = ps.executeBatch ();
                ps.close ();

                Statement s = connection_.createStatement();

                ResultSet rs = s.executeQuery("SELECT COL1 FROM " + table4_ );
                boolean success = true;
                int count = 0;
                while(rs.next())
                {
		    String tmp = rs.getString(1);						
		    String res = "Los Angeles" + Integer.toString(count);			
		    messageBuffer.append(""+(count+1)+". "+tmp+" sb "+res+"\n");			
		    if(tmp.indexOf(res)!=0)	// if(!tmp.equals(res)) ... coz native pads extra characters in end				
                        success = false;
                    count++;
                }
                rs.close();
                s.close();
                assertCondition(updateCounts.length == len && success, "updateCounts.length = "	    // @L2
				+updateCounts.length+" SB "+len+" ; success = "+success+" SB true "+messageBuffer.toString());// @L2
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception:  Added by Native 8/20/03 "+messageBuffer.toString());
            }
        }
    }
/**
Adds a statement using a vargraphic to a batch
**/
    public void Var054()		// @L2
    {
	StringBuffer messageBuffer = new StringBuffer(); 
        if (checkJdbc20 ())
        {
            try
	    {

                PreparedStatement ps = connection_.prepareStatement ( insertT42_ );

		int len = 5;
                for (int i = 0; i<len; i++)
                {
                    ps.setString (1,"Los Angeles" + Integer.toString(i));
                    ps.addBatch ();
                }
                int updateCounts[]  = ps.executeBatch ();
                ps.close ();

                Statement s = connection_.createStatement();

                ResultSet rs = s.executeQuery("SELECT COL2 FROM " + table4_ +" where col2 like 'Los Angeles%'");
                boolean success = true;
                int count = 0;
                while(rs.next())
                {
		    String tmp = rs.getString(1);						
		    String res = "Los Angeles" + Integer.toString(count);			
		    messageBuffer.append(""+(count+1)+". "+tmp+" sb "+res+"\n");			
		    if(!tmp.equals(res))
                        success = false;
                    count++;
                }
                rs.close();
                s.close();
                assertCondition(updateCounts.length == len && success, "updateCounts.length = "	     // @L2
				+updateCounts.length+" SB "+len+" ; success = "+success+" SB true "+messageBuffer.toString()); // @L2
	    }
            catch (Exception e) {
                failed (e, "Unexpected Exception:  Added by Native 8/20/03 "+messageBuffer.toString());
            }
        }
    }

/**
Adds a statement using a vargraphic to a batch
**/
    public void Var055()		// @L2
    {
	StringBuffer messageBuffer = new StringBuffer(); 
        if (checkJdbc20 ())
        {
            try
	    {

		try{
		    Statement st = connection_.createStatement();
		    st.executeUpdate(" DELETE FROM "+table4_);
		    st.close();
		}catch(Exception se){
		}
		
                PreparedStatement ps = connection_.prepareStatement ("insert into "+table4_+" values (?,?)");
		for(int i=0; i<2; i++){
		    ps.setString(1, null);
		    ps.setString(2, null);
                    ps.addBatch ();
                }
                int updateCounts[]  = ps.executeBatch ();
                ps.close ();

                Statement s = connection_.createStatement();

                ResultSet rs = s.executeQuery("SELECT * FROM " + table4_);// +" where col2 not like 'Los Angeles%'");
                boolean success = true;
                int count = 0;
                while(rs.next())
                {
		    for(int i=0; i<2; i++){
			String tmp = rs.getString(i+1);
			messageBuffer.append(""+(count+1)+". col # "+i+": "+tmp+" sb null\n");			
			if(tmp != null)
			    success = false;
			count++;
		    }
		}
                rs.close();
                s.close();
                assertCondition(updateCounts.length == 2 && success, "updateCounts.length = "	     // @L2
				+updateCounts.length+" SB "+2+" ; success = "+success+" SB true "+messageBuffer.toString()); // @L2
	    }
            catch (Exception e) {
                failed (e, "Unexpected Exception:  Added by Native 8/20/03 "+messageBuffer.toString());
            }
        }
    }


//@K2A
/**
Adds a statement using a blob of varying lengths to a batch
**/
    public void Var056()
    {
 JDSerializeFile pstestSet = null;
 StringBuffer messageBuffer = new StringBuffer();
        if (checkJdbc20 ())
        {
            try
            {

  try{
      pstestSet = JDPSTest.getPstestSet(connection_);
      Statement s = connection_.createStatement();
      s.executeUpdate("DELETE from "+pstestSet.getName());
      s.close();
  }catch(Exception ex){
  }


                PreparedStatement ps = connection_.prepareStatement (
                                                                        "INSERT INTO " + pstestSet.getName()
                                                                        + " (C_BLOB) VALUES (?)");
  byte[] b = new byte[] { (byte) 1, (byte) -12, (byte) 57, (byte) 45, (byte) -33, (byte) 5};
//                byte[] b1 = new byte[] { (byte) 1, (byte) -12, (byte) 57, (byte) 45,
//                    (byte) -33, (byte) 3};
  byte[] b1 = new byte[200];
  for(int i=0; i<b1.length; i++)
      b1[i] = (byte)i;
                byte[] b2 = new byte[] { (byte) 1, (byte) -12, (byte) 57, (byte) 45, (byte) -33, (byte) 1};

  messageBuffer.append("Initialization done!\n");
                ps.setBlob (1, new JDLobTest.JDTestBlob (b));
  messageBuffer.append("b set\n");
                ps.addBatch ();
  messageBuffer.append("b added to batch\n");
                ps.setBlob (1, new JDLobTest.JDTestBlob (b1));
  messageBuffer.append("b1 set\n");
                ps.addBatch ();
  messageBuffer.append("b1 added to batch\n");
                ps.setBlob (1, new JDLobTest.JDTestBlob (b2));
  messageBuffer.append("b2 set\n");
                ps.addBatch ();
  messageBuffer.append("b2 added to batch\n");
                int updateCounts[]  = ps.executeBatch ();
  messageBuffer.append("executed batch\n");
                ps.close ();

                Statement s = connection_.createStatement();
                ResultSet rs = s.executeQuery ("SELECT C_BLOB FROM " + pstestSet.getName()+ " ORDER BY HASH(C_BLOB)");
                boolean success = true;
                int count = 0;
                
                byte[] inspect = null;

  byte[] r1 = new byte[b.length];
  byte[] r2 = new byte[b1.length];
  byte[] r3 = new byte[b2.length];

  byte[] recvd = null;

                while(rs.next())
                {

                    java.sql.Blob check = rs.getBlob(1);

      if(count == 0){
                        inspect = b2;
   r1 = check.getBytes (1, (int) check.length ());
   recvd = r1;
      }
      else if(count == 1){
                        inspect = b;
   r2 = check.getBytes (1, (int) check.length ());
   recvd = r2;
      }
      else{
                        inspect = b1;
   r3 = check.getBytes (1, (int) check.length ());
   recvd = r3;
      }
                    
      if(!areEqual (inspect, recvd)) {
   messageBuffer.append("\nFor Row "+(count+1));
   messageBuffer.append("\nGot: ");
   for(int i=0; i< recvd.length; i++)
       messageBuffer.append(recvd[i]+" ");
   messageBuffer.append("\nSB:  ");
   for(int i=0; i< inspect.length; i++)
       messageBuffer.append(inspect[i]+" ");
   messageBuffer.append("\n");
                        success = false;
      }

                    count++;
                }
                rs.close();
                s.close();

  messageBuffer.append("\nRow1: ");
  for(int i=0; i< r1.length; i++)
      messageBuffer.append(r1[i]+" ");

		messageBuffer.append("\nRow2: ");
		for(int i=0; i< r2.length; i++)
		    messageBuffer.append(r2[i]+" ");

		messageBuffer.append("\nRow3: ");
		for(int i=0; i< r3.length; i++)
		    messageBuffer.append(r3[i]+" ");

		messageBuffer.append("\n");

		assertCondition(updateCounts.length == 3 && success,"len = "+updateCounts.length+
				" SB 3 success = "+success+" SB true "+messageBuffer.toString());
		          }
		          catch (Exception e) {
		              failed (e, "Unexpected Exception: "+messageBuffer.toString()+"  Added by Native 9/9/03 ");
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
Adds a statement using a blob of varying lengths to a batch
**/
    public void Var057()
    {

	StringBuffer messageBuffer = new StringBuffer(); 

	if (toolboxNative) {
	    notApplicable("Toolbox native fails with Out of Memory");
	    return; 
	} 


	String tablename = JDPSTest.COLLECTION+ ".JDPSBAT56"; 
  try {
Statement stmt = connection_.createStatement();
try {
    stmt.executeUpdate("DROP TABLE "+tablename);
} catch (Exception e) {
} 

stmt.executeUpdate("CREATE TABLE "+tablename+" ( c1 varchar(16384))");

//
// Now create a batch with 1500 rows. This should exeed the
// 16 Meg limit
//

PreparedStatement pStmt = connection_.prepareStatement("INSERT INTO "+tablename+" VALUES(?)");

for (int i = 0; i < 1500; i++) { 
    pStmt.setString(1, "ROW "+i); 
    pStmt.addBatch(); 
}

int updateCounts[]  = pStmt.executeBatch ();


messageBuffer.append("Length of updateCounts is "+updateCounts.length+"\n");
output_.println(messageBuffer.toString());

assertCondition(true, messageBuffer.toString());

  } catch (Exception e) {
failed (e, "Unexpected Exception:  Added by Native 10/26/2005 "+messageBuffer.toString() );
  } finally {
/* Clean up the table */

try {
    Statement stmt = connection_.createStatement();

    stmt.executeUpdate("DROP TABLE "+tablename);
} catch (Exception e) {
    output_.println("Warning:  Exception during test cleanup "+e); 
} 

  } 

    }



/**
Adds a statement using several ints.  The goal is to overflow the 16 M boundary
for indicator variables.

**/
    public void Var058()
    {

	if (toolboxNative) {
	    notApplicable("Toolbox native fails with Out of Memory");
	    return; 
	} 

	StringBuffer messageBuffer = new StringBuffer(); 

	//
	// This test only matter for native JDBC if we are using a block insert
	//
	if (getDriver() == JDTestDriver.DRIVER_NATIVE && (!useBlockInsert )) {
	    notApplicable("Don't run 16M block test for NATIVE and not block insert");
	    return; 
	} 

	try {
String tablename = JDPSTest.COLLECTION+ ".JDPSBAT58";
Statement stmt = connection_.createStatement();
try {
    stmt.executeUpdate("DROP TABLE "+tablename);
} catch (Exception e) {
} 

stmt.executeUpdate("CREATE TABLE "+tablename+" ( c1 int, "+
                                                         "   c2 int, "+
                                                         "   c3 int, "+
                                                         "   c4 int, "+
                                                         "   c5 int, "+
                                                         "   c6 int, "+
                                                         "   c7 int, "+
                                                         "   c8 int, "+
                                                         "   c9 int, "+
                                                         "   c10 int, "+
                                                         "   c11 int, "+
                                                         "   c12 int, "+
                                                         "   c13 int, "+
                                                         "   c14 int, "+
                                                         "   c15 int, "+
                                                         "   c16 int)");

//
// Now create a batch with 270,000 rows. This should exceed the
// 16 Meg limit
// 270,000 * 16 columns / row  * 4 bytes / column = 17,280,000 
            //

PreparedStatement pStmt = connection_.prepareStatement("INSERT INTO "+tablename+
                " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            //
            // Note:  The native block insert fails because CLI only permits
            // 32K rows.  Opened issue 31000 to see if limit can be increased
//
// This was fixed internally by the native driver by breaking the execute into
// 32K row chunks. 
            // 
for (int i = 1; i <= 270000; i++) {
              // if (i % 1000 == 0) { output_.print(i+"."); }
  pStmt.setInt(1, i);   
              for (int j = 2; j <= 16; j++) { 
    pStmt.setNull(j,Types.INTEGER);
              }
              pStmt.addBatch(); 
}
// output_.println(); 
int updateCounts[]  = pStmt.executeBatch ();

ResultSet rs = stmt.executeQuery("Select sum( cast (c1 as bigint)) from "+tablename);
rs.next();
long answer = rs.getLong(1);
output_.println("The answer is "+answer); 

messageBuffer.append("Length of updateCounts is "+updateCounts.length+"\n");
output_.println(messageBuffer.toString());


stmt.executeUpdate("DROP TABLE "+tablename);

stmt.close();
pStmt.close();

// Check the answer sb 2700000 * (2700000 + 1 ) / 2

assertCondition(answer==36450135000L && updateCounts.length == 270000, "answer="+answer+" sb 36450135000  updateCounts.length="+updateCounts.length+" sb 270000 "+
                  messageBuffer.toString());

  } catch (Exception e) {
failed (e, "Unexpected Exception:  Added by Native 7/19/2006 "+messageBuffer.toString()+" Note:  Currently fails for native when native insert support used -- see issue 31000" );
  } 

    }


/**
Adds a statement using several ints.  The goal is to overflow the 16 M boundary
for indicator variables.  Then reuse the statement to assure it is still usable.


**/
    public void Var059()
    {

	if (toolboxNative) {
	    notApplicable("Toolbox native fails with Out of Memory");
	    return; 
	} 

	if (getDriver() == JDTestDriver.DRIVER_NATIVE && (!useBlockInsert )) {
	    notApplicable("Don't run 16M block test for NATIVE and not block insert");
	    return; 
	} 

	StringBuffer messageBuffer = new StringBuffer(); 

	try {
String tablename = JDPSTest.COLLECTION+ ".JDPSBAT59";
Statement stmt = connection_.createStatement();
try {
    stmt.executeUpdate("DROP TABLE "+tablename);
} catch (Exception e) {
} 

stmt.executeUpdate("CREATE TABLE "+tablename+" ( c1 int, "+
                                                         "   c2 int, "+
                                                         "   c3 int, "+
                                                         "   c4 int, "+
                                                         "   c5 int, "+
                                                         "   c6 int, "+
                                                         "   c7 int, "+
                                                         "   c8 int, "+
                                                         "   c9 int, "+
                                                         "   c10 int, "+
                                                         "   c11 int, "+
                                                         "   c12 int, "+
                                                         "   c13 int, "+
                                                         "   c14 int, "+
                                                         "   c15 int, "+
                                                         "   c16 int)");

//
// Now create a batch with 270,000 rows. This should exeed the
// 16 Meg limit
// 270,000 * 16 columns / row  * 4 bytes / column = 17,280,000 
            //

PreparedStatement pStmt = connection_.prepareStatement("INSERT INTO "+tablename+
                " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            //
            // Note:  The native block instert fails because CLI only permits
            // 32K rows.  Opened issue 31000 to see if limit can be increased
            // 
for (int i = 0; i < 270000; i++) {
              // if (i % 1000 == 0) { output_.print(i+"."); }
              for (int j = 1; j <= 16; j++) { 
    pStmt.setNull(j,Types.INTEGER);
              }
              pStmt.addBatch(); 
}
// output_.println(); 
int updateCounts[]  = pStmt.executeBatch ();


for (int i = 0; i < 270000; i++) {
              // if (i % 1000 == 0) { output_.print(i+"."); }
              for (int j = 1; j <= 16; j++) { 
    pStmt.setNull(j,Types.INTEGER);
              }
              pStmt.addBatch(); 
}
// output_.println(); 
int updateCounts2[]  = pStmt.executeBatch ();


stmt.executeUpdate("DROP TABLE "+tablename);
stmt.close();
pStmt.close(); 

messageBuffer.append("Length of updateCounts is "+updateCounts.length+"\n");
messageBuffer.append("Length of updateCounts2 is "+updateCounts2.length+"\n");
output_.println(messageBuffer.toString()); 
assertCondition(updateCounts.length == 270000 && updateCounts2.length == 270000, "updateCounts.length="+updateCounts.length+" sb 270000 updateCounts2.length="+updateCounts2.length+ " "+
                  messageBuffer.toString());

  } catch (Exception e) {
failed (e, "Unexpected Exception:  Added by Native 07/19/2006 "+messageBuffer.toString()+" Note:  Currently fails for native when native insert support used -- see issue 31000" );
  } 

    }

    //UPDATE STATEMENT with no literals
    public void Var060 ()
    {


        int expectedUpdateCount = 1;
	//if(false /* isToolboxDriver() */ ) {
	    // -2 is the value of SUCCESS_NO_INFO
	    //expectedUpdateCount = -2; 
	//}
        
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.execute("DELETE FROM " + table_);

                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI1')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI2')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI3')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI4')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI5')");
                s.executeBatch();

                PreparedStatement ps = connection_.prepareStatement("UPDATE " + table_ + " SET NAME = ? where NAME = ?");
                ps.setString(1, "SAFARI1a");
                ps.setString(2, "SAFARI1");
                ps.addBatch();
                ps.setString(1, "SAFARI2a");
                ps.setString(2, "SAFARI2");
                ps.addBatch();
                ps.setString(1, "SAFARI3a");
                ps.setString(2, "SAFARI3");
                ps.addBatch();
                ps.setString(1, "SAFARI4a");
                ps.setString(2, "SAFARI4");
                ps.addBatch();
                ps.setString(1, "SAFARI5a");
                ps.setString(2, "SAFARI5");
                ps.addBatch();

                int[] updateCounts = ps.executeBatch ();
                int rows = countRows ("SAFARI%a");
                s.close ();
                assertCondition ((updateCounts.length == 5)
                        && (updateCounts[0] == expectedUpdateCount)
                        && (updateCounts[1] == expectedUpdateCount)
                        && (updateCounts[2] == expectedUpdateCount)
                        && (updateCounts[3] == expectedUpdateCount)
                        && (updateCounts[4] == expectedUpdateCount)
                        && (rows == 5),
				 "FAILED -- \n"+
				 "updateCounts.length="+updateCounts.length+" sb 5\n"+
				 "updateCounts[0]="+updateCounts[0]+" sb "+expectedUpdateCount+"\n"+
				 "updateCounts[1]="+updateCounts[1]+" sb "+expectedUpdateCount+"\n"+
				 "updateCounts[2]="+updateCounts[2]+" sb "+expectedUpdateCount+"\n"+
				 "updateCounts[3]="+updateCounts[3]+" sb "+expectedUpdateCount+"\n"+
				 "updateCounts[4]="+updateCounts[4]+" sb "+expectedUpdateCount+"\n"+
				 "rows="+rows+" sb 5\n"+
				 " -updated 4/16/2010 for toolbox insert fix");
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception -added 4/16/2010 for toolbox insert fix");
            }
        }
    }

    /**
    addBatch()/executeBatch() - Execute the batch of DELETE STATEMENTS v7r1 no literals
     **/
    public void Var061 ()
    {
	if (getDriver() != JDTestDriver.DRIVER_NATIVE && useBlockInsert) {  notApplicable("Native block insert test");  return;	} 
        int expectedUpdateCount = 1;

	//if(false /* isToolboxDriver() */ ) {
	    // -2 is the value of SUCCESS_NO_INFO
	//    expectedUpdateCount = -2; 
	//}

        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.execute("DELETE FROM " + table_);

                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI1')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI2')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI3')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI4')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI5')");
                s.executeBatch();

                PreparedStatement ps = connection_.prepareStatement("DELETE from " + table_ + " where NAME = ? ");
                ps.setString(1, "SAFARI1");
                ps.addBatch();
                ps.setString(1, "SAFARI2");
                ps.addBatch();
                ps.setString(1, "SAFARI3");
                ps.addBatch();
                ps.setString(1, "SAFARI4");
                ps.addBatch();
                ps.setString(1, "SAFARI5");
                ps.addBatch();
                
                int[] updateCounts = ps.executeBatch ();
                int rows = countRows ("SAFARI%");
                s.close ();
                assertCondition ((updateCounts.length == 5)
                        && (updateCounts[0] == expectedUpdateCount)
                        && (updateCounts[1] == expectedUpdateCount)
                        && (updateCounts[2] == expectedUpdateCount)
                        && (updateCounts[3] == expectedUpdateCount)
                        && (updateCounts[4] == expectedUpdateCount)
                        && (rows == 0),
				 "FAILED -- \n"+
				 "updateCounts.length="+updateCounts.length+" sb 5\n"+
				 "updateCounts[0]="+updateCounts[0]+" sb "+expectedUpdateCount+"\n"+
				 "updateCounts[1]="+updateCounts[1]+" sb "+expectedUpdateCount+"\n"+
				 "updateCounts[2]="+updateCounts[2]+" sb "+expectedUpdateCount+"\n"+
				 "updateCounts[3]="+updateCounts[3]+" sb "+expectedUpdateCount+"\n"+
				 "updateCounts[4]="+updateCounts[4]+" sb "+expectedUpdateCount+"\n"+
				 "rows="+rows+" sb 0\n"+
				 " -added by TB 6/22/09 for multirow update");
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception -added by TB 6/22/09 for multirow update");
            }
        }
    }

    /**
        addBatch()/executeBatch() - Execute the batch of MERGE STATEMENTS v7r1 no literals
     **/
    public void Var062 ()
    {
	if (getDriver() != JDTestDriver.DRIVER_NATIVE && useBlockInsert) {  notApplicable("Native block insert test");  return;	} 
        int[] expectedUpdateCounts = {2,0,0 }; 

	if(isToolboxDriver()) {
	    // -2 is the value of SUCCESS_NO_INFO
            expectedUpdateCounts = new int[3];

	    expectedUpdateCounts[0] = -2; 
	    expectedUpdateCounts[1] = -2; 
	    expectedUpdateCounts[2] = -2; 
	}


        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.execute("DELETE FROM " + table_);
                s.execute("DELETE FROM " + table2_);

                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI1')");
                s.addBatch ("INSERT INTO " + table_ + "(NAME) VALUES ('SAFARI2')");
                s.addBatch ("INSERT INTO " + table2_ + "(NAME) VALUES ('SAFARI1')");
                s.addBatch ("INSERT INTO " + table2_ + "(NAME) VALUES ('SAFARI2')");
                s.addBatch ("INSERT INTO " + table2_ + "(NAME) VALUES ('SAFARI3')");
                s.executeBatch();

                PreparedStatement ps = connection_.prepareStatement("MERGE INTO " + table_ + " ar USING " + table2_ + " ac " +
                                 " ON ( ar.name = ac.name )  WHEN MATCHED THEN  UPDATE SET ar.name = ?"); 
                ps.setString(1, "saf a");
                ps.addBatch();
                ps.setString(1, "saf b");
                ps.addBatch();
                ps.setString(1, "saf c");
                ps.addBatch();

             
                int[] updateCounts = ps.executeBatch ();
                int rows = countRows ("saf a%"); //sb 2 from first merge 
             
                s.close ();
                assertCondition ((updateCounts.length == 3)
                        && (updateCounts[0] == expectedUpdateCounts[0])
                        && (updateCounts[1] == expectedUpdateCounts[1])
                        && (updateCounts[2] == expectedUpdateCounts[2])
                        && (rows == 2),
			"got\n updateCounts.length="+updateCounts.length +" sb 3\n"+
				 "updateCounts[0]="+updateCounts[0]+" sb "+expectedUpdateCounts[0]+"\n"+
				 "updateCounts[1]="+updateCounts[1]+" sb "+expectedUpdateCounts[1]+"\n"+
				 "updateCounts[2]="+updateCounts[2]+" sb "+expectedUpdateCounts[2]+"\n"+
				 "rows="+rows+"sb 2       "+" -added by TB 6/22/09 for multirow update");
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception -added by TB 6/22/09 for multirow update");
            }
        }
    }


   //UPDATE STATEMENT with literal in where clause (TODO:  do in two batches ) 
    public void Var063 ()
    {
	if (getDriver() != JDTestDriver.DRIVER_NATIVE && useBlockInsert) {  notApplicable("Native block insert test");  return;	} 

	// Batching should work for all releases.. 
        // if(false) {
        //    notApplicable("Batched update/merge/delete vor v7r1 only" ); 
        //    return;
        //}

	int expectedUpdateCount = 1;
	//if(false /* isToolboxDriver() */ ) {
	    // -2 is the value of SUCCESS_NO_INFO
	  //  expectedUpdateCount = -2; 
	//}
        
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.execute("DELETE FROM " + table_);

                s.addBatch ("INSERT INTO " + table_ + "(NAME, ID) VALUES ('SAFARI1',1)");
                s.addBatch ("INSERT INTO " + table_ + "(NAME, ID) VALUES ('SAFARI2',2)");
                s.addBatch ("INSERT INTO " + table_ + "(NAME, ID) VALUES ('SAFARI3',3)");
                s.addBatch ("INSERT INTO " + table_ + "(NAME, ID) VALUES ('SAFARI4',4)");
                s.addBatch ("INSERT INTO " + table_ + "(NAME, ID) VALUES ('SAFARI5',5)");
                s.executeBatch();

                PreparedStatement ps = connection_.prepareStatement("UPDATE " + table_ + " SET NAME = ? where NAME = ? AND ID<100");
                ps.setString(1, "SAFARI1a");
                ps.setString(2, "SAFARI1");
                ps.addBatch();
                ps.setString(1, "SAFARI2a");
                ps.setString(2, "SAFARI2");
                ps.addBatch();
                ps.setString(1, "SAFARI3a");
                ps.setString(2, "SAFARI3");
                ps.addBatch();
                ps.setString(1, "SAFARI4a");
                ps.setString(2, "SAFARI4");
                ps.addBatch();
                ps.setString(1, "SAFARI5a");
                ps.setString(2, "SAFARI5");
                ps.addBatch();

                int[] updateCounts = ps.executeBatch ();
                int rows = countRows ("SAFARI%a");
                s.close ();
                assertCondition ((updateCounts.length == 5)
                        && (updateCounts[0] == expectedUpdateCount)
                        && (updateCounts[1] == expectedUpdateCount)
                        && (updateCounts[2] == expectedUpdateCount)
                        && (updateCounts[3] == expectedUpdateCount)
                        && (updateCounts[4] == expectedUpdateCount)
                        && (rows == 5),
				 "FAILED -- \n"+
				 "updateCounts.length="+updateCounts.length+" sb 5\n"+
				 "updateCounts[0]="+updateCounts[0]+" sb "+expectedUpdateCount+"\n"+
				 "updateCounts[1]="+updateCounts[1]+" sb "+expectedUpdateCount+"\n"+
				 "updateCounts[2]="+updateCounts[2]+" sb "+expectedUpdateCount+"\n"+
				 "updateCounts[3]="+updateCounts[3]+" sb "+expectedUpdateCount+"\n"+
				 "updateCounts[4]="+updateCounts[4]+" sb "+expectedUpdateCount+"\n"+
				 "rows="+rows+" sb 5\n"+
				 " -added by TB 6/22/09 for multirow update");
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception -added by TB 6/22/09 for multirow update");
            }
        }
    }

   //UPDATE STATEMENT with only 1 set column with literal and multiple batches   
    public void Var064 ()
    {
	if (getDriver() != JDTestDriver.DRIVER_NATIVE && useBlockInsert) {  notApplicable("Native block insert test");  return;	} 

	// Batching should work for all releases.. 
        // if(false) {
        //    notApplicable("Batched update/merge/delete vor v7r1 only" ); 
        //    return;
        //}

	int expectedUpdateCount = 1;
	//if(false /* isToolboxDriver() */ ) {
	    // -2 is the value of SUCCESS_NO_INFO
	//    expectedUpdateCount = -2; 
//	}
        
        if (checkJdbc20 ()) {
            try {
		/* 
		output_.println("Testcase pausing for 45 seconds\n");
		try {
		    Thread.sleep(45000); 
		} catch (Exception e) {
		    e.printStackTrace(); 
		} 
		output_.println("Testcase continuing\n");
		*/

                Statement s = connection_.createStatement ();
                s.execute("DELETE FROM " + table_);

                s.addBatch ("INSERT INTO " + table_ + "(NAME, ID) VALUES ('SAFARI1',1)");
                s.addBatch ("INSERT INTO " + table_ + "(NAME, ID) VALUES ('SAFARI2',2)");
                s.addBatch ("INSERT INTO " + table_ + "(NAME, ID) VALUES ('SAFARI3',3)");
                s.addBatch ("INSERT INTO " + table_ + "(NAME, ID) VALUES ('SAFARI4',4)");
                s.addBatch ("INSERT INTO " + table_ + "(NAME, ID) VALUES ('SAFARI5',5)");
                s.executeBatch();

                countRows ("SAFARI%", 5);

                PreparedStatement ps = connection_.prepareStatement("UPDATE " + table_ + " SET NAME = 'SAFARIX' where NAME = ? AND ID<100");
                ps.setString(1, "SAFARI1");
                ps.addBatch();
                ps.setString(1, "SAFARI2");
                ps.addBatch();
                ps.setString(1, "SAFARI5");
                ps.addBatch();
                ps.setString(1, "SAFARI3");
                ps.addBatch();
                ps.setString(1, "SAFARI4");
                ps.addBatch();

                int[] updateCounts = ps.executeBatch ();
                int rows = countRows ("SAFARIX", 5);
                s.close ();
                assertCondition ((updateCounts.length == 5)
                        && (updateCounts[0] == expectedUpdateCount)
                        && (updateCounts[1] == expectedUpdateCount)
                        && (updateCounts[2] == expectedUpdateCount)
                        && (updateCounts[3] == expectedUpdateCount)
                        && (updateCounts[4] == expectedUpdateCount)
                        && (rows == 5),
				 "FAILED -- \n"+
				 "updateCounts.length="+updateCounts.length+" sb 5\n"+
				 "updateCounts[0]="+updateCounts[0]+" sb "+expectedUpdateCount+"\n"+
				 "updateCounts[1]="+updateCounts[1]+" sb "+expectedUpdateCount+"\n"+
				 "updateCounts[2]="+updateCounts[2]+" sb "+expectedUpdateCount+"\n"+
				 "updateCounts[3]="+updateCounts[3]+" sb "+expectedUpdateCount+"\n"+
				 "updateCounts[4]="+updateCounts[4]+" sb "+expectedUpdateCount+"\n"+
				 "rows="+rows+" sb 5\n"+
				 " -added by TB 6/22/09 for multirow update");
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception -added by TB 6/22/09 for multirow update");
            }
        }
    }


   //UPDATE STATEMENT with literal in where clause
    public void Var065 ()
    {
	if (getDriver() != JDTestDriver.DRIVER_NATIVE && useBlockInsert) {  notApplicable("Native block insert test");  return;	} 

	// Batching should work for all releases.. 
        // if(false) {
        //    notApplicable("Batched update/merge/delete vor v7r1 only" ); 
        //    return;
        //}

	int expectedUpdateCount = 1;
	int expectedNoUpdate = 0; 
	//if(false /* isToolboxDriver() */ ) {
	    // -2 is the value of SUCCESS_NO_INFO
	//    expectedUpdateCount = -2;
	//    expectedNoUpdate = -2; 
	//}
        
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement ();
                s.execute("DELETE FROM " + table_);

                s.addBatch ("INSERT INTO " + table_ + "(NAME, ID) VALUES ('SAFARI1',1)");
                s.addBatch ("INSERT INTO " + table_ + "(NAME, ID) VALUES ('SAFARI2',2)");
                s.addBatch ("INSERT INTO " + table_ + "(NAME, ID) VALUES ('SAFARI3',3)");
                s.addBatch ("INSERT INTO " + table_ + "(NAME, ID) VALUES ('SAFARI4',4)");
                s.addBatch ("INSERT INTO " + table_ + "(NAME, ID) VALUES ('SAFARI5',5)");
                s.executeBatch();

                countRows ("SAFARI%", 5);

                PreparedStatement ps = connection_.prepareStatement("UPDATE " + table_ + " SET NAME = ? where ID = ? AND NAME='SAFARI1'");
                ps.setString(1, "SAFARIX1X");
		ps.setInt(2,1); 
                ps.addBatch();

                ps.setString(1, "SAFARIX2X");
		ps.setInt(2,2); 
                ps.addBatch();

                ps.setString(1, "SAFARIX3X");
		ps.setInt(2,3); 
                ps.addBatch();

                ps.setString(1, "SAFARIX4X");
		ps.setInt(2,4); 
                ps.addBatch();

                ps.setString(1, "SAFARIX5X");
		ps.setInt(2,5); 
                ps.addBatch();


                int[] updateCounts = ps.executeBatch ();
		int expectedRowCount = 1; 
                int rows = countRows ("SAFARIX1X", expectedRowCount);
                s.close ();
                assertCondition ((updateCounts.length == 5)
                        && (updateCounts[0] == expectedUpdateCount)
                        && (updateCounts[1] == expectedNoUpdate)
                        && (updateCounts[2] == expectedNoUpdate)
                        && (updateCounts[3] == expectedNoUpdate)
                        && (updateCounts[4] == expectedNoUpdate)
                        && (rows == expectedRowCount),
				 "FAILED -- \n"+
				 "updateCounts.length="+updateCounts.length+" sb 5\n"+
				 "updateCounts[0]="+updateCounts[0]+" sb "+expectedUpdateCount+"\n"+
				 "updateCounts[1]="+updateCounts[1]+" sb "+expectedNoUpdate+"\n"+
				 "updateCounts[2]="+updateCounts[2]+" sb "+expectedNoUpdate+"\n"+
				 "updateCounts[3]="+updateCounts[3]+" sb "+expectedNoUpdate+"\n"+
				 "updateCounts[4]="+updateCounts[4]+" sb "+expectedNoUpdate+"\n"+
				 "rows="+rows+" sb "+expectedRowCount+"\n"+
				 " -added by TB 6/22/09 for multirow update");
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception -added by TB 6/22/09 for multirow update");
            }
        }
    }





    //UPDATE STATEMENT with literal in where clause with multiple batches 
    // Recreate Defect FW513306
    // This has a literal in the where clause 
    public void Var066 ()
    {
	if (getDriver() != JDTestDriver.DRIVER_NATIVE && useBlockInsert) {  notApplicable("Native block insert test");  return;	} 
	StringBuffer executeBatchInfo = new StringBuffer();

	int executeBatchCount = 0; 
	// Batching should work for all releases.. 
        // if(false) {
        //    notApplicable("Batched update/merge/delete vor v7r1 only" ); 
        //    return;
        //}

	boolean passed = true; 
	//int expectedUpdateCount = 1;
	//if(isToolboxDriver()) {
	    // -2 is the value of SUCCESS_NO_INFO
	//    expectedUpdateCount = -2; 
	//}


        if (checkJdbc20 ()) {
	    String sql = ""; 
            try {
                Statement s = connection_.createStatement ();

		String tableName = JDPSTest.COLLECTION + ".JDPSBAT66";

		sql = "DROP TABLE "+tableName;
		try {
		    s.executeUpdate(sql); 
		} catch (Exception e) {
		} 

		sql = "CREATE TABLE "+tableName+"("+
		  "OFFICER_ID INTEGER,"+
		  "COURTHOUSE_ID CHAR(6)," +
		  "TITLE  VARCHAR(20)," +
		  "NAME VARCHAR(36),"+
		  "BADGE_NUMBER INTEGER,"+
		  "DATE_OF_SERVICE DATE,"+
		  "PATROL_TYPE VARCHAR(20),"+
		  "HIRED_BY INTEGER)";

		s.executeUpdate(sql); 

		sql = "INSERT INTO "+tableName+" VALUES(?,?,?,?,?,?,?,?)"; 
		PreparedStatement ps = connection_.prepareStatement(sql);

	for (int i = 1; i <= 100; i++) {
          ps.setString(1, "" + i);
          ps.setString(2, "CH0001");
          ps.setString(3, "TITLE" + i);
          ps.setString(4, "NAME" + i);
          ps.setString(5, "" + i);
          ps.setDate(6, new java.sql.Date(i));
          ps.setString(7, "PATROL" + i);
          ps.setString(8, "" + i);
          ps.addBatch();
	  if (i%20==0) {
	      executeBatchCount++;
	      executeBatchInfo.append("calling batch "+executeBatchCount+" insert into table\n"); 
	      ps.executeBatch();

	  }
        }


        sql = "UPDATE "
            + tableName
            + " SET TITLE = ?, NAME = ?, BADGE_NUMBER = ?, DATE_OF_SERVICE = ?, PATROL_TYPE = ?, HIRED_BY = ? WHERE OFFICER_ID = ? AND COURTHOUSE_ID = 'CH0001'";
        ps = connection_.prepareStatement(sql);

        for (int i = 1; i <= 100; i++) {
          ps.setString(1, "TITLE" + i + "X");
          ps.setString(2, "NAME" + i + "X");
          ps.setString(3, "" + i );
          ps.setDate(4, new java.sql.Date(i));
          ps.setString(5, "PATROL" + i + "X");
          ps.setString(6,  ""+i);
          ps.setString(7,  ""+i);
          ps.addBatch();
	  if (i%20==0) {
	      executeBatchCount++; 
	      executeBatchInfo.append("calling batch "+executeBatchCount+" update entries\n"); 
	      ps.executeBatch();
	  }

        }



	// Verify updates were made
	sql = "select TITLE, NAME, BADGE_NUMBER, DATE_OF_SERVICE, PATROL_TYPE, HIRED_BY, OFFICER_ID FROM "+tableName+" ORDER BY OFFICER_ID";

	executeBatchInfo.append("Verify using query:  "+sql); 
	ResultSet rs = s.executeQuery(sql);
	int i = 1; 
	while (rs.next()) {
	    String string;
	    String expected;

	    string = rs.getString(1);
	    expected ="TITLE" + i + "X";
	    if (! string.equals(expected)) {
		executeBatchInfo.append("Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(2);
	    expected ="NAME" + i + "X";
	    if (! string.equals(expected)) {
		executeBatchInfo.append("Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(3);
	    expected = "" + i ;
	    if (! string.equals(expected)) {
		executeBatchInfo.append("Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getDate(4).toString();
	    java.sql.Date ed = new java.sql.Date(i); 
	    expected = ed.toString(); 

	    if (! string.equals(expected)) {
		executeBatchInfo.append("Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(5);
	    expected =  "PATROL" + i + "X";
	    if (! string.equals(expected)) {
		executeBatchInfo.append("Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(6);
	    expected = ""+i;
	    if (! string.equals(expected)) {
		executeBatchInfo.append("Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(7);
	    expected =  ""+i;

	    if (! string.equals(expected)) {
		executeBatchInfo.append("Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }


	    i++; 
	} 



		// Now delete the batch
		sql = "DELETE FROM " + tableName + " WHERE OFFICER_ID = ? AND COURTHOUSE_ID = 'CH0001'";
		ps = connection_.prepareStatement(sql);

	        for ( i = 1; i < 100; i++) {
	          ps.setString(1, "" + i);
	          ps.addBatch();
	        }


		executeBatchCount++; 

		executeBatchInfo.append("calling batch "+executeBatchCount+" delete entries\n");
		int[] count = ps.executeBatch();
		

		sql = "DROP TABLE "+tableName;
		executeBatchInfo.append(sql); 
		s.executeUpdate(sql); 
		s.close();
		ps.close(); 

		assertCondition(passed, "Test failed\nInfo=\n"+executeBatchInfo.toString()+count); 

            }
            catch (Exception e) {

                failed (e, "Unexpected Exception -added by TB 6/22/09 for multirow update sql = "+sql+"\nInfo=\n"+executeBatchInfo.toString());

            }
        }
    }



    //UPDATE STATEMENT with literal in first set position with multiple batches 
    public void Var067 ()
    {
	if (getDriver() != JDTestDriver.DRIVER_NATIVE && useBlockInsert) {  notApplicable("Native block insert test");  return;	} 
	StringBuffer executeBatchInfo = new StringBuffer();

	executeBatchInfo.append("Testing literal in first set position with multiple batches");

	int executeBatchCount = 0; 
	// Batching should work for all releases.. 
        // if(false) {
        //    notApplicable("Batched update/merge/delete vor v7r1 only" ); 
        //    return;
        //}

	boolean passed = true; 
	//int expectedUpdateCount = 1;
	//if(isToolboxDriver()) {
	    // -2 is the value of SUCCESS_NO_INFO
	  //  expectedUpdateCount = -2; 
	//}


        if (checkJdbc20 ()) {
	    String sql = ""; 
            try {
                Statement s = connection_.createStatement ();

		String tableName = JDPSTest.COLLECTION + ".JDPSBAT67";

		sql = "DROP TABLE "+tableName;
		try {
		    s.executeUpdate(sql); 
		} catch (Exception e) {
		} 

		sql = "CREATE TABLE "+tableName+"("+
		  "OFFICER_ID INTEGER,"+
		  "COURTHOUSE_ID CHAR(6)," +
		  "TITLE  VARCHAR(20)," +
		  "NAME VARCHAR(36),"+
		  "BADGE_NUMBER INTEGER,"+
		  "DATE_OF_SERVICE DATE,"+
		  "PATROL_TYPE VARCHAR(20),"+
		  "HIRED_BY INTEGER)";

		s.executeUpdate(sql); 

		sql = "INSERT INTO "+tableName+" VALUES(?,?,?,?,?,?,?,?)"; 
		PreparedStatement ps = connection_.prepareStatement(sql);

	for (int i = 1; i <= 100; i++) {
          ps.setString(1, "" + i);
          ps.setString(2, "CH0001");
          ps.setString(3, "TITLE" + i);
          ps.setString(4, "NAME" + i);
          ps.setString(5, "" + i);
          ps.setDate(6, new java.sql.Date(i));
          ps.setString(7, "PATROL" + i);
	  if (i == 1) {
	      ps.setString(8, "1"); 
	  } else { 
	      ps.setString(8, "" + (i-1));
	  }
          ps.addBatch();
	  if (i%20==0) {
	      executeBatchCount++;
	      executeBatchInfo.append("calling batch "+executeBatchCount+" insert into table\n"); 
	      ps.executeBatch();

	  }
        }


        sql = "UPDATE "
            + tableName
            + " SET COURTHOUSE_ID = 'CH0002', TITLE = ?, NAME = ?  WHERE OFFICER_ID = ? AND COURTHOUSE_ID = ? and NAME = ? ";
        ps = connection_.prepareStatement(sql);

        for (int i = 1; i <= 100; i++) {
	  // Set every 10th title to null
	  if (i%10 == 0) {
	      ps.setString(1, null); 
	  } else { 
	      ps.setString(1, "TITLE" + i + "X");
	  }
          ps.setString(2, "NAME" + i + "X");
          ps.setString(3, "" + i );
	  ps.setString(4, "CH0001"); 
          ps.setString(5, "NAME"+i); 
          ps.addBatch();
	  if (i%20==0) {
	      executeBatchCount++; 
	      executeBatchInfo.append("calling batch "+executeBatchCount+" update entries\n"); 
	      ps.executeBatch();
	  }

        }


	// Verify updates were made
	sql = "select TITLE, NAME, BADGE_NUMBER, DATE_OF_SERVICE, PATROL_TYPE, HIRED_BY, OFFICER_ID, COURTHOUSE_ID FROM "+tableName+" ORDER BY OFFICER_ID";

	executeBatchInfo.append("Verify using query:  "+sql); 
	ResultSet rs = s.executeQuery(sql);
	int i = 1; 
	while (rs.next()) {
	    String string;
	    String expected;

	    string = rs.getString(1);
	    expected ="TITLE" + i + "X";

	    if (i%10 == 0) {
		if (string != null) {
		    executeBatchInfo.append("Row "+i+" TITLE:"+"Got '"+string+"' expected 'null'\n");
		    passed = false; 
		}
	    } else { 
		if ((string == null) || ! string.equals(expected)) {
		    executeBatchInfo.append("Row "+i+" TITLE:"+"Got '"+string+"' expected '"+expected+"'\n");
		    passed = false; 
		}
	    }

	    string = rs.getString(2);
	    expected ="NAME" + i + "X";
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" NAME:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(3);
	    expected = "" + i ;
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" BADGE NUMBER:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getDate(4).toString();
	    java.sql.Date ed = new java.sql.Date(i); 
	    expected = ed.toString(); 

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" DATE_OF_SERVICE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(5);
	    expected =  "PATROL" + i ; 
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" PATROL TYPE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(6);
	    if (i == 1) {
		expected = "1";
	    } else { 
		expected = ""+(i-1);
	    }
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" HIRED BY:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(7);
	    expected =  ""+i;

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" OFFICER ID:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }


	    string = rs.getString(8);
	    expected =  "CH0002";

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" COURTHOUSE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }



	    i++; 
	} 

		// Now delete the batch
		sql = "DELETE FROM " + tableName + " WHERE OFFICER_ID = ? AND COURTHOUSE_ID = 'CH0001'";
		ps = connection_.prepareStatement(sql);

	        for ( i = 1; i < 100; i++) {
	          ps.setString(1, "" + i);
	          ps.addBatch();
	        }


		executeBatchCount++; 

		executeBatchInfo.append("calling batch "+executeBatchCount+" delete entries\n");
		int[] count = ps.executeBatch();
		

		
		sql = "DROP TABLE "+tableName;
		executeBatchInfo.append(sql); 
		s.executeUpdate(sql); 
		s.close();
		ps.close();

		assertCondition(passed, "Test failed\nInfo=\n"+executeBatchInfo.toString()+count); 

            }
            catch (Exception e) {

                failed (e, "Unexpected Exception -added by TB 6/22/09 for multirow update sql = "+sql+"\nInfo=\n"+executeBatchInfo.toString());

            }
        }
    }




    //UPDATE STATEMENT with literal in second set position with multiple batches 
    public void Var068 ()
    {
	if (getDriver() != JDTestDriver.DRIVER_NATIVE && useBlockInsert) {  notApplicable("Native block insert test");  return;	} 
	StringBuffer executeBatchInfo = new StringBuffer();

	executeBatchInfo.append("Testing literal in second set position with multiple batches");

	int executeBatchCount = 0; 
	// Batching should work for all releases.. 
        // if(false) {
        //    notApplicable("Batched update/merge/delete vor v7r1 only" ); 
        //    return;
        //}

	boolean passed = true; 


        if (checkJdbc20 ()) {
	    String sql = ""; 
            try {
                Statement s = connection_.createStatement ();

		String tableName = JDPSTest.COLLECTION + ".JDPSBAT68";

		sql = "DROP TABLE "+tableName;
		try {
		    s.executeUpdate(sql); 
		} catch (Exception e) {
		} 

		sql = "CREATE TABLE "+tableName+"("+
		  "OFFICER_ID INTEGER,"+
		  "COURTHOUSE_ID CHAR(6)," +
		  "TITLE  VARCHAR(20)," +
		  "NAME VARCHAR(36),"+
		  "BADGE_NUMBER INTEGER,"+
		  "DATE_OF_SERVICE DATE,"+
		  "PATROL_TYPE VARCHAR(20),"+
		  "HIRED_BY INTEGER)";

		s.executeUpdate(sql); 

		sql = "INSERT INTO "+tableName+" VALUES(?,?,?,?,?,?,?,?)"; 
		PreparedStatement ps = connection_.prepareStatement(sql);

	for (int i = 1; i <= 100; i++) {
          ps.setString(1, "" + i);
          ps.setString(2, "CH0001");
          ps.setString(3, "TITLE" + i);
          ps.setString(4, "NAME" + i);
          ps.setString(5, "" + i);
          ps.setDate(6, new java.sql.Date(i));
          ps.setString(7, "PATROL" + i);
	  if (i == 1) {
	      ps.setString(8, "1"); 
	  } else { 
	      ps.setString(8, "" + (i-1));
	  }
          ps.addBatch();
	  if (i%20==0) {
	      executeBatchCount++;
	      executeBatchInfo.append("calling batch "+executeBatchCount+" insert into table\n"); 
	      ps.executeBatch();

	  }
        }


        sql = "UPDATE "
            + tableName
            + " SET  TITLE = ?, COURTHOUSE_ID = 'CH0002', NAME = ?  WHERE OFFICER_ID = ? AND COURTHOUSE_ID = ? and NAME = ? ";
        ps = connection_.prepareStatement(sql);

        for (int i = 1; i <= 100; i++) {
	  // Set every 10th title to null
	  if (i%10 == 0) {
	      ps.setString(1, null); 
	  } else { 
	      ps.setString(1, "TITLE" + i + "X");
	  }
          ps.setString(2, "NAME" + i + "X");
          ps.setString(3, "" + i );
	  ps.setString(4, "CH0001"); 
          ps.setString(5, "NAME"+i); 
          ps.addBatch();
	  if (i%20==0) {
	      executeBatchCount++; 
	      executeBatchInfo.append("calling batch "+executeBatchCount+" update entries\n"); 
	      ps.executeBatch();
	  }

        }


	// Verify updates were made
	sql = "select TITLE, NAME, BADGE_NUMBER, DATE_OF_SERVICE, PATROL_TYPE, HIRED_BY, OFFICER_ID, COURTHOUSE_ID FROM "+tableName+" ORDER BY OFFICER_ID";

	executeBatchInfo.append("Verify using query:  "+sql); 
	ResultSet rs = s.executeQuery(sql);
	int i = 1; 
	while (rs.next()) {
	    String string;
	    String expected;

	    string = rs.getString(1);
	    expected ="TITLE" + i + "X";

	    if (i%10 == 0) {
		if (string != null) {
		    executeBatchInfo.append("Row "+i+" TITLE:"+"Got '"+string+"' expected 'null'\n");
		    passed = false; 
		}
	    } else { 
		if ((string == null) || ! string.equals(expected)) {
		    executeBatchInfo.append("Row "+i+" TITLE:"+"Got '"+string+"' expected '"+expected+"'\n");
		    passed = false; 
		}
	    }

	    string = rs.getString(2);
	    expected ="NAME" + i + "X";
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" NAME:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(3);
	    expected = "" + i ;
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" BADGE NUMBER:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getDate(4).toString();
	    java.sql.Date ed = new java.sql.Date(i); 
	    expected = ed.toString(); 

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" DATE_OF_SERVICE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(5);
	    expected =  "PATROL" + i ; 
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" PATROL TYPE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(6);
	    if (i == 1) {
		expected = "1";
	    } else { 
		expected = ""+(i-1);
	    }
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" HIRED BY:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(7);
	    expected =  ""+i;

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" OFFICER ID:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }


	    string = rs.getString(8);
	    expected =  "CH0002";

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" COURTHOUSE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }



	    i++; 
	} 

		// Now delete the batch
		sql = "DELETE FROM " + tableName + " WHERE OFFICER_ID = ? AND COURTHOUSE_ID = 'CH0001'";
		ps = connection_.prepareStatement(sql);

	        for ( i = 1; i < 100; i++) {
	          ps.setString(1, "" + i);
	          ps.addBatch();
	        }


		executeBatchCount++; 

		executeBatchInfo.append("calling batch "+executeBatchCount+" delete entries\n");
		int[] count = ps.executeBatch();

		sql = "DROP TABLE "+tableName;
		s.executeUpdate(sql); 
		s.close();
		ps.close(); 

		assertCondition(passed, "Test failed\nInfo=\n"+executeBatchInfo.toString()+count); 

            }
            catch (Exception e) {

                failed (e, "Unexpected Exception -added by TB 6/22/09 for multirow update sql = "+sql+"\nInfo=\n"+executeBatchInfo.toString());

            }
        }
    }



   //UPDATE STATEMENT with literal in third set position with multiple batches 
    public void Var069 ()
    {
	if (getDriver() != JDTestDriver.DRIVER_NATIVE && useBlockInsert) {  notApplicable("Native block insert test");  return;	} 
	StringBuffer executeBatchInfo = new StringBuffer();

	executeBatchInfo.append("Testing literal in second set position with multiple batches");

	int executeBatchCount = 0; 
	// Batching should work for all releases.. 
        // if(false) {
        //    notApplicable("Batched update/merge/delete vor v7r1 only" ); 
        //    return;
        //}

	boolean passed = true; 


        if (checkJdbc20 ()) {
	    String sql = ""; 
            try {
                Statement s = connection_.createStatement ();

		String tableName = JDPSTest.COLLECTION + ".JDPSBAT69";

		sql = "DROP TABLE "+tableName;
		try {
		    s.executeUpdate(sql); 
		} catch (Exception e) {
		} 

		sql = "CREATE TABLE "+tableName+"("+
		  "OFFICER_ID INTEGER,"+
		  "COURTHOUSE_ID CHAR(6)," +
		  "TITLE  VARCHAR(20)," +
		  "NAME VARCHAR(36),"+
		  "BADGE_NUMBER INTEGER,"+
		  "DATE_OF_SERVICE DATE,"+
		  "PATROL_TYPE VARCHAR(20),"+
		  "HIRED_BY INTEGER)";

		s.executeUpdate(sql); 

		sql = "INSERT INTO "+tableName+" VALUES(?,?,?,?,?,?,?,?)"; 
		PreparedStatement ps = connection_.prepareStatement(sql);

	for (int i = 1; i <= 100; i++) {
          ps.setString(1, "" + i);
          ps.setString(2, "CH0001");
          ps.setString(3, "TITLE" + i);
          ps.setString(4, "NAME" + i);
          ps.setString(5, "" + i);
          ps.setDate(6, new java.sql.Date(i));
          ps.setString(7, "PATROL" + i);
	  if (i == 1) {
	      ps.setString(8, "1"); 
	  } else { 
	      ps.setString(8, "" + (i-1));
	  }
          ps.addBatch();
	  if (i%20==0) {
	      executeBatchCount++;
	      executeBatchInfo.append("calling batch "+executeBatchCount+" insert into table\n"); 
	      ps.executeBatch();

	  }
        }


        sql = "UPDATE "
            + tableName
            + " SET  TITLE = ?, NAME = ?, COURTHOUSE_ID = 'CH0002'  WHERE OFFICER_ID = ? AND COURTHOUSE_ID = ? and NAME = ? ";
        ps = connection_.prepareStatement(sql);

        for (int i = 1; i <= 100; i++) {
	  // Set every 10th title to null
	  if (i%10 == 0) {
	      ps.setString(1, null); 
	  } else { 
	      ps.setString(1, "TITLE" + i + "X");
	  }
          ps.setString(2, "NAME" + i + "X");
          ps.setString(3, "" + i );
	  ps.setString(4, "CH0001"); 
          ps.setString(5, "NAME"+i); 
          ps.addBatch();
	  if (i%20==0) {
	      executeBatchCount++; 
	      executeBatchInfo.append("calling batch "+executeBatchCount+" update entries\n"); 
	      ps.executeBatch();
	  }

        }


	// Verify updates were made
	sql = "select TITLE, NAME, BADGE_NUMBER, DATE_OF_SERVICE, PATROL_TYPE, HIRED_BY, OFFICER_ID, COURTHOUSE_ID FROM "+tableName+" ORDER BY OFFICER_ID";

	executeBatchInfo.append("Verify using query:  "+sql); 
	ResultSet rs = s.executeQuery(sql);
	int i = 1; 
	while (rs.next()) {
	    String string;
	    String expected;

	    string = rs.getString(1);
	    expected ="TITLE" + i + "X";

	    if (i%10 == 0) {
		if (string != null) {
		    executeBatchInfo.append("Row "+i+" TITLE:"+"Got '"+string+"' expected 'null'\n");
		    passed = false; 
		}
	    } else { 
		if ((string == null) || ! string.equals(expected)) {
		    executeBatchInfo.append("Row "+i+" TITLE:"+"Got '"+string+"' expected '"+expected+"'\n");
		    passed = false; 
		}
	    }

	    string = rs.getString(2);
	    expected ="NAME" + i + "X";
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" NAME:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(3);
	    expected = "" + i ;
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" BADGE NUMBER:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getDate(4).toString();
	    java.sql.Date ed = new java.sql.Date(i); 
	    expected = ed.toString(); 

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" DATE_OF_SERVICE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(5);
	    expected =  "PATROL" + i ; 
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" PATROL TYPE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(6);
	    if (i == 1) {
		expected = "1";
	    } else { 
		expected = ""+(i-1);
	    }
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" HIRED BY:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(7);
	    expected =  ""+i;

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" OFFICER ID:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }


	    string = rs.getString(8);
	    expected =  "CH0002";

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" COURTHOUSE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }



	    i++; 
	} 

		// Now delete the batch
		sql = "DELETE FROM " + tableName + " WHERE OFFICER_ID = ? AND COURTHOUSE_ID = 'CH0001'";
		ps = connection_.prepareStatement(sql);

	        for ( i = 1; i < 100; i++) {
	          ps.setString(1, "" + i);
	          ps.addBatch();
	        }


		executeBatchCount++; 

		executeBatchInfo.append("calling batch "+executeBatchCount+" delete entries\n");
		 ps.executeBatch();
		

		sql = "DROP TABLE "+tableName;
		executeBatchInfo.append(sql); 
		s.executeUpdate(sql);
		s.close();
		ps.close(); 

		assertCondition(passed, "Test failed\nInfo=\n"+executeBatchInfo.toString()); 

            }
            catch (Exception e) {

                failed (e, "Unexpected Exception -added by TB 6/22/09 for multirow update sql = "+sql+"\nInfo=\n"+executeBatchInfo.toString());

            }
        }
    }



   //UPDATE STATEMENT with literal in first where  position with multiple batches 
    public void Var070 ()
    {
	if (getDriver() != JDTestDriver.DRIVER_NATIVE && useBlockInsert) {  notApplicable("Native block insert test");  return;	} 
	StringBuffer executeBatchInfo = new StringBuffer();

	executeBatchInfo.append("Testing literal in first where position with multiple batches");

	int executeBatchCount = 0; 
	// Batching should work for all releases.. 
        // if(false) {
        //    notApplicable("Batched update/merge/delete vor v7r1 only" ); 
        //    return;
        //}

	boolean passed = true; 


        if (checkJdbc20 ()) {
	    String sql = ""; 
            try {
                Statement s = connection_.createStatement ();

		String tableName = JDPSTest.COLLECTION + ".JDPSBAT70";

		sql = "DROP TABLE "+tableName;
		try {
		    s.executeUpdate(sql); 
		} catch (Exception e) {
		} 

		sql = "CREATE TABLE "+tableName+"("+
		  "OFFICER_ID INTEGER,"+
		  "COURTHOUSE_ID CHAR(6)," +
		  "TITLE  VARCHAR(20)," +
		  "NAME VARCHAR(36),"+
		  "BADGE_NUMBER INTEGER,"+
		  "DATE_OF_SERVICE DATE,"+
		  "PATROL_TYPE VARCHAR(20),"+
		  "HIRED_BY INTEGER)";

		s.executeUpdate(sql); 

		sql = "INSERT INTO "+tableName+" VALUES(?,?,?,?,?,?,?,?)"; 
		PreparedStatement ps = connection_.prepareStatement(sql);

	for (int i = 1; i <= 100; i++) {
          ps.setString(1, "" + i);
          ps.setString(2, "CH0001");
          ps.setString(3, "TITLE" + i);
          ps.setString(4, "NAME" + i);
          ps.setString(5, "" + i);
          ps.setDate(6, new java.sql.Date(i));
          ps.setString(7, "PATROL" + i);
	  if (i == 1) {
	      ps.setString(8, "1"); 
	  } else { 
	      ps.setString(8, "" + (i-1));
	  }
          ps.addBatch();
	  if (i%20==0) {
	      executeBatchCount++;
	      executeBatchInfo.append("calling batch "+executeBatchCount+" insert into table\n"); 
	      ps.executeBatch();

	  }
        }


        sql = "UPDATE "
            + tableName
            + " SET  TITLE = ?, NAME = ?, COURTHOUSE_ID = ?  WHERE COURTHOUSE_ID = 'CH0001' and OFFICER_ID = ? AND NAME = ? ";
        ps = connection_.prepareStatement(sql);

        for (int i = 1; i <= 100; i++) {
	  // Set every 10th title to null
	  if (i%10 == 0) {
	      ps.setString(1, null); 
	  } else { 
	      ps.setString(1, "TITLE" + i + "X");
	  }
          ps.setString(2, "NAME" + i + "X");
	  ps.setString(3, "CH0002"); 
          ps.setString(4, "" + i );
          ps.setString(5, "NAME"+i); 
          ps.addBatch();
	  if (i%20==0) {
	      executeBatchCount++; 
	      executeBatchInfo.append("calling batch "+executeBatchCount+" update entries\n"); 
	      ps.executeBatch();
	  }

        }


	// Verify updates were made
	sql = "select TITLE, NAME, BADGE_NUMBER, DATE_OF_SERVICE, PATROL_TYPE, HIRED_BY, OFFICER_ID, COURTHOUSE_ID FROM "+tableName+" ORDER BY OFFICER_ID";

	executeBatchInfo.append("Verify using query:  "+sql); 
	ResultSet rs = s.executeQuery(sql);
	int i = 1; 
	while (rs.next()) {
	    String string;
	    String expected;

	    string = rs.getString(1);
	    expected ="TITLE" + i + "X";

	    if (i%10 == 0) {
		if (string != null) {
		    executeBatchInfo.append("Row "+i+" TITLE:"+"Got '"+string+"' expected 'null'\n");
		    passed = false; 
		}
	    } else { 
		if ((string == null) || ! string.equals(expected)) {
		    executeBatchInfo.append("Row "+i+" TITLE:"+"Got '"+string+"' expected '"+expected+"'\n");
		    passed = false; 
		}
	    }

	    string = rs.getString(2);
	    expected ="NAME" + i + "X";
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" NAME:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(3);
	    expected = "" + i ;
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" BADGE NUMBER:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getDate(4).toString();
	    java.sql.Date ed = new java.sql.Date(i); 
	    expected = ed.toString(); 

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" DATE_OF_SERVICE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(5);
	    expected =  "PATROL" + i ; 
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" PATROL TYPE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(6);
	    if (i == 1) {
		expected = "1";
	    } else { 
		expected = ""+(i-1);
	    }
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" HIRED BY:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(7);
	    expected =  ""+i;

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" OFFICER ID:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }


	    string = rs.getString(8);
	    expected =  "CH0002";

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" COURTHOUSE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }



	    i++; 
	} 

		// Now delete the batch
		sql = "DELETE FROM " + tableName + " WHERE OFFICER_ID = ? AND COURTHOUSE_ID = 'CH0001'";
		ps = connection_.prepareStatement(sql);

	        for ( i = 1; i < 100; i++) {
	          ps.setString(1, "" + i);
	          ps.addBatch();
	        }


		executeBatchCount++; 

		executeBatchInfo.append("calling batch "+executeBatchCount+" delete entries\n");
	 ps.executeBatch();

		sql = "DROP TABLE "+tableName;
		executeBatchInfo.append(sql); 
		s.executeUpdate(sql);
		s.close();
		ps.close(); 


		assertCondition(passed, "Test failed\nInfo=\n"+executeBatchInfo.toString()); 

            }
            catch (Exception e) {

                failed (e, "Unexpected Exception -added by TB 6/22/09 for multirow update sql = "+sql+"\nInfo=\n"+executeBatchInfo.toString());

            }
        }
    }

   //UPDATE STATEMENT with literal in second where  position with multiple batches 
    public void Var071 ()
    {
	if (getDriver() != JDTestDriver.DRIVER_NATIVE && useBlockInsert) {  notApplicable("Native block insert test");  return;	} 
	StringBuffer executeBatchInfo = new StringBuffer();

	executeBatchInfo.append("Testing literal in second where position with multiple batches");

	int executeBatchCount = 0; 
	// Batching should work for all releases.. 
        // if(false) {
        //    notApplicable("Batched update/merge/delete vor v7r1 only" ); 
        //    return;
        //}

	boolean passed = true; 


        if (checkJdbc20 ()) {
	    String sql = ""; 
            try {
                Statement s = connection_.createStatement ();

		String tableName = JDPSTest.COLLECTION + ".JDPSBAT71";

		sql = "DROP TABLE "+tableName;
		try {
		    s.executeUpdate(sql); 
		} catch (Exception e) {
		} 

		sql = "CREATE TABLE "+tableName+"("+
		  "OFFICER_ID INTEGER,"+
		  "COURTHOUSE_ID CHAR(6)," +
		  "TITLE  VARCHAR(20)," +
		  "NAME VARCHAR(36),"+
		  "BADGE_NUMBER INTEGER,"+
		  "DATE_OF_SERVICE DATE,"+
		  "PATROL_TYPE VARCHAR(20),"+
		  "HIRED_BY INTEGER)";

		s.executeUpdate(sql); 

		sql = "INSERT INTO "+tableName+" VALUES(?,?,?,?,?,?,?,?)"; 
		PreparedStatement ps = connection_.prepareStatement(sql);

	for (int i = 1; i <= 100; i++) {
          ps.setString(1, "" + i);
          ps.setString(2, "CH0001");
          ps.setString(3, "TITLE" + i);
          ps.setString(4, "NAME" + i);
          ps.setString(5, "" + i);
          ps.setDate(6, new java.sql.Date(i));
          ps.setString(7, "PATROL" + i);
	  if (i == 1) {
	      ps.setString(8, "1"); 
	  } else { 
	      ps.setString(8, "" + (i-1));
	  }
          ps.addBatch();
	  if (i%20==0) {
	      executeBatchCount++;
	      executeBatchInfo.append("calling batch "+executeBatchCount+" insert into table\n"); 
	      ps.executeBatch();

	  }
        }


        sql = "UPDATE "
            + tableName
            + " SET  TITLE = ?, NAME = ?, COURTHOUSE_ID = ?  WHERE OFFICER_ID = ? AND COURTHOUSE_ID = 'CH0001' and NAME = ? ";
        ps = connection_.prepareStatement(sql);

        for (int i = 1; i <= 100; i++) {
	  // Set every 10th title to null
	  if (i%10 == 0) {
	      ps.setString(1, null); 
	  } else { 
	      ps.setString(1, "TITLE" + i + "X");
	  }

          ps.setString(2, "NAME" + i + "X");

	  ps.setString(3, "CH0002");

          ps.setString(4, "" + i );

          ps.setString(5, "NAME"+i);

          ps.addBatch();
	  if (i%20==0) {
	      executeBatchCount++; 
	      executeBatchInfo.append("calling batch "+executeBatchCount+" update entries\n"); 
	      ps.executeBatch();
	  }

        }


	// Verify updates were made
	sql = "select TITLE, NAME, BADGE_NUMBER, DATE_OF_SERVICE, PATROL_TYPE, HIRED_BY, OFFICER_ID, COURTHOUSE_ID FROM "+tableName+" ORDER BY OFFICER_ID";

	executeBatchInfo.append("Verify using query:  "+sql); 
	ResultSet rs = s.executeQuery(sql);
	int i = 1; 
	while (rs.next()) {
	    String string;
	    String expected;

	    string = rs.getString(1);
	    expected ="TITLE" + i + "X";

	    if (i%10 == 0) {
		if (string != null) {
		    executeBatchInfo.append("Row "+i+" TITLE:"+"Got '"+string+"' expected 'null'\n");
		    passed = false; 
		}
	    } else { 
		if ((string == null) || ! string.equals(expected)) {
		    executeBatchInfo.append("Row "+i+" TITLE:"+"Got '"+string+"' expected '"+expected+"'\n");
		    passed = false; 
		}
	    }

	    string = rs.getString(2);
	    expected ="NAME" + i + "X";
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" NAME:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(3);
	    expected = "" + i ;
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" BADGE NUMBER:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getDate(4).toString();
	    java.sql.Date ed = new java.sql.Date(i); 
	    expected = ed.toString(); 

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" DATE_OF_SERVICE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(5);
	    expected =  "PATROL" + i ; 
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" PATROL TYPE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(6);
	    if (i == 1) {
		expected = "1";
	    } else { 
		expected = ""+(i-1);
	    }
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" HIRED BY:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(7);
	    expected =  ""+i;

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" OFFICER ID:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }


	    string = rs.getString(8);
	    expected =  "CH0002";

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" COURTHOUSE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }



	    i++; 
	} 

		// Now delete the batch
		sql = "DELETE FROM " + tableName + " WHERE OFFICER_ID = ? AND COURTHOUSE_ID = 'CH0001'";
		ps = connection_.prepareStatement(sql);

	        for ( i = 1; i < 100; i++) {
	          ps.setString(1, "" + i);
	          ps.addBatch();
	        }


		executeBatchCount++; 

		executeBatchInfo.append("calling batch "+executeBatchCount+" delete entries\n");
		 ps.executeBatch();

		 sql = "DROP TABLE "+tableName;
		 executeBatchInfo.append(sql); 
		 s.executeUpdate(sql);
		 s.close();
		 ps.close(); 


		assertCondition(passed, "Test failed\nInfo=\n"+executeBatchInfo.toString()); 

            }
            catch (Exception e) {

                failed (e, "Unexpected Exception -added by TB 6/22/09 for multirow update sql = "+sql+"\nInfo=\n"+executeBatchInfo.toString());

            }
        }
    }


   //UPDATE STATEMENT with literal in third where  position with multiple batches 
    public void Var072 ()
    {
	if (getDriver() != JDTestDriver.DRIVER_NATIVE && useBlockInsert) {  notApplicable("Native block insert test");  return;	} 
	StringBuffer executeBatchInfo = new StringBuffer();

	executeBatchInfo.append("Testing literal in third where position with multiple batches");

	int executeBatchCount = 0; 
	// Batching should work for all releases.. 
        // if(false) {
        //    notApplicable("Batched update/merge/delete vor v7r1 only" ); 
        //    return;
        //}

	boolean passed = true; 


        if (checkJdbc20 ()) {
	    String sql = ""; 
            try {
                Statement s = connection_.createStatement ();

		String tableName = JDPSTest.COLLECTION + ".JDPSBAT72";

		sql = "DROP TABLE "+tableName;
		try {
		    s.executeUpdate(sql); 
		} catch (Exception e) {
		} 

		sql = "CREATE TABLE "+tableName+"("+
		  "OFFICER_ID INTEGER,"+
		  "COURTHOUSE_ID CHAR(6)," +
		  "TITLE  VARCHAR(20)," +
		  "NAME VARCHAR(36),"+
		  "BADGE_NUMBER INTEGER,"+
		  "DATE_OF_SERVICE DATE,"+
		  "PATROL_TYPE VARCHAR(20),"+
		  "HIRED_BY INTEGER)";

		s.executeUpdate(sql); 

		sql = "INSERT INTO "+tableName+" VALUES(?,?,?,?,?,?,?,?)"; 
		PreparedStatement ps = connection_.prepareStatement(sql);

	for (int i = 1; i <= 100; i++) {
          ps.setString(1, "" + i);
          ps.setString(2, "CH0001");
          ps.setString(3, "TITLE" + i);
          ps.setString(4, "NAME" + i);
          ps.setString(5, "" + i);
          ps.setDate(6, new java.sql.Date(i));
          ps.setString(7, "PATROL" + i);
	  if (i == 1) {
	      ps.setString(8, "1"); 
	  } else { 
	      ps.setString(8, "" + (i-1));
	  }
          ps.addBatch();
	  if (i%20==0) {
	      executeBatchCount++;
	      executeBatchInfo.append("calling batch "+executeBatchCount+" insert into table\n"); 
	      ps.executeBatch();

	  }
        }


        sql = "UPDATE "
            + tableName
            + " SET  TITLE = ?, NAME = ?, COURTHOUSE_ID = ?  WHERE  OFFICER_ID = ? AND NAME = ? AND COURTHOUSE_ID = 'CH0001' ";
        ps = connection_.prepareStatement(sql);

        for (int i = 1; i <= 100; i++) {
	  // Set every 10th title to null
	  if (i%10 == 0) {
	      ps.setString(1, null); 
	  } else { 
	      ps.setString(1, "TITLE" + i + "X");
	  }
          ps.setString(2, "NAME" + i + "X");
	  ps.setString(3, "CH0002"); 
          ps.setString(4, "" + i );
          ps.setString(5, "NAME"+i); 
          ps.addBatch();
	  if (i%20==0) {
	      executeBatchCount++; 
	      executeBatchInfo.append("calling batch "+executeBatchCount+" update entries\n"); 
	      ps.executeBatch();
	  }

        }


	// Verify updates were made
	sql = "select TITLE, NAME, BADGE_NUMBER, DATE_OF_SERVICE, PATROL_TYPE, HIRED_BY, OFFICER_ID, COURTHOUSE_ID FROM "+tableName+" ORDER BY OFFICER_ID";

	executeBatchInfo.append("Verify using query:  "+sql); 
	ResultSet rs = s.executeQuery(sql);
	int i = 1; 
	while (rs.next()) {
	    String string;
	    String expected;

	    string = rs.getString(1);
	    expected ="TITLE" + i + "X";

	    if (i%10 == 0) {
		if (string != null) {
		    executeBatchInfo.append("Row "+i+" TITLE:"+"Got '"+string+"' expected 'null'\n");
		    passed = false; 
		}
	    } else { 
		if ((string == null) || ! string.equals(expected)) {
		    executeBatchInfo.append("Row "+i+" TITLE:"+"Got '"+string+"' expected '"+expected+"'\n");
		    passed = false; 
		}
	    }

	    string = rs.getString(2);
	    expected ="NAME" + i + "X";
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" NAME:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(3);
	    expected = "" + i ;
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" BADGE NUMBER:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getDate(4).toString();
	    java.sql.Date ed = new java.sql.Date(i); 
	    expected = ed.toString(); 

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" DATE_OF_SERVICE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(5);
	    expected =  "PATROL" + i ; 
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" PATROL TYPE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(6);
	    if (i == 1) {
		expected = "1";
	    } else { 
		expected = ""+(i-1);
	    }
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" HIRED BY:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(7);
	    expected =  ""+i;

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" OFFICER ID:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }


	    string = rs.getString(8);
	    expected =  "CH0002";

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" COURTHOUSE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }



	    i++; 
	} 

		// Now delete the batch
		sql = "DELETE FROM " + tableName + " WHERE OFFICER_ID = ? AND COURTHOUSE_ID = 'CH0001'";
		ps = connection_.prepareStatement(sql);

	        for ( i = 1; i < 100; i++) {
	          ps.setString(1, "" + i);
	          ps.addBatch();
	        }


		executeBatchCount++; 

		executeBatchInfo.append("calling batch "+executeBatchCount+" delete entries\n");
		 ps.executeBatch();

		sql = "DROP TABLE "+tableName;
		executeBatchInfo.append(sql); 
		s.executeUpdate(sql);
		s.close();
		ps.close(); 


		assertCondition(passed, "Test failed\nInfo=\n"+executeBatchInfo.toString()); 

            }
            catch (Exception e) {

                failed (e, "Unexpected Exception -added by TB 6/22/09 for multirow update sql = "+sql+"\nInfo=\n"+executeBatchInfo.toString());

            }
        }
    }




    //UPDATE STATEMENT with null in first set position with multiple batches 
    public void Var073 ()
    {
	if (getDriver() != JDTestDriver.DRIVER_NATIVE && useBlockInsert) {  notApplicable("Native block insert test");  return;	} 
	StringBuffer executeBatchInfo = new StringBuffer();

	executeBatchInfo.append("Testing literal null in first set position with multiple batches");

	int executeBatchCount = 0; 
	// Batching should work for all releases.. 
        // if(false) {
        //    notApplicable("Batched update/merge/delete vor v7r1 only" ); 
        //    return;
        //}

	boolean passed = true; 


        if (checkJdbc20 ()) {
	    String sql = ""; 
            try {
                Statement s = connection_.createStatement ();

		String tableName = JDPSTest.COLLECTION + ".JDPSBAT73";

		sql = "DROP TABLE "+tableName;
		try {
		    s.executeUpdate(sql); 
		} catch (Exception e) {
		} 

		sql = "CREATE TABLE "+tableName+"("+
		  "OFFICER_ID INTEGER,"+
		  "COURTHOUSE_ID CHAR(6)," +
		  "TITLE  VARCHAR(20)," +
		  "NAME VARCHAR(36),"+
		  "BADGE_NUMBER INTEGER,"+
		  "DATE_OF_SERVICE DATE,"+
		  "PATROL_TYPE VARCHAR(20),"+
		  "HIRED_BY INTEGER)";

		s.executeUpdate(sql); 

		sql = "INSERT INTO "+tableName+" VALUES(?,?,?,?,?,?,?,?)"; 
		PreparedStatement ps = connection_.prepareStatement(sql);

	for (int i = 1; i <= 100; i++) {
          ps.setString(1, "" + i);
          ps.setString(2, "CH0001");
          ps.setString(3, "TITLE" + i);
          ps.setString(4, "NAME" + i);
          ps.setString(5, "" + i);
          ps.setDate(6, new java.sql.Date(i));
          ps.setString(7, "PATROL" + i);
	  if (i == 1) {
	      ps.setString(8, "1"); 
	  } else { 
	      ps.setString(8, "" + (i-1));
	  }
          ps.addBatch();
	  if (i%20==0) {
	      executeBatchCount++;
	      executeBatchInfo.append("calling batch "+executeBatchCount+" insert into table\n"); 
	      ps.executeBatch();

	  }
        }


        sql = "UPDATE "
            + tableName
            + " SET COURTHOUSE_ID = NULL, TITLE = ?, NAME = ?  WHERE OFFICER_ID = ? AND COURTHOUSE_ID = ? and NAME = ? ";
        ps = connection_.prepareStatement(sql);

        for (int i = 1; i <= 100; i++) {
	  // Set every 10th title to null
	  if (i%10 == 0) {
	      ps.setString(1, null); 
	  } else { 
	      ps.setString(1, "TITLE" + i + "X");
	  }
          ps.setString(2, "NAME" + i + "X");
          ps.setString(3, "" + i );
	  ps.setString(4, "CH0001"); 
          ps.setString(5, "NAME"+i); 
          ps.addBatch();
	  if (i%20==0) {
	      executeBatchCount++; 
	      executeBatchInfo.append("calling batch "+executeBatchCount+" update entries\n"); 
	      ps.executeBatch();
	  }

        }


	// Verify updates were made
	sql = "select TITLE, NAME, BADGE_NUMBER, DATE_OF_SERVICE, PATROL_TYPE, HIRED_BY, OFFICER_ID, COURTHOUSE_ID FROM "+tableName+" ORDER BY OFFICER_ID";

	executeBatchInfo.append("Verify using query:  "+sql); 
	ResultSet rs = s.executeQuery(sql);
	int i = 1; 
	while (rs.next()) {
	    String string;
	    String expected;

	    string = rs.getString(1);
	    expected ="TITLE" + i + "X";

	    if (i%10 == 0) {
		if (string != null) {
		    executeBatchInfo.append("Row "+i+" TITLE:"+"Got '"+string+"' expected 'null'\n");
		    passed = false; 
		}
	    } else { 
		if ((string == null) || ! string.equals(expected)) {
		    executeBatchInfo.append("Row "+i+" TITLE:"+"Got '"+string+"' expected '"+expected+"'\n");
		    passed = false; 
		}
	    }

	    string = rs.getString(2);
	    expected ="NAME" + i + "X";
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" NAME:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(3);
	    expected = "" + i ;
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" BADGE NUMBER:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getDate(4).toString();
	    java.sql.Date ed = new java.sql.Date(i); 
	    expected = ed.toString(); 

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" DATE_OF_SERVICE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(5);
	    expected =  "PATROL" + i ; 
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" PATROL TYPE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(6);
	    if (i == 1) {
		expected = "1";
	    } else { 
		expected = ""+(i-1);
	    }
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" HIRED BY:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(7);
	    expected =  ""+i;

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" OFFICER ID:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }


	    string = rs.getString(8);
	    expected =  null;

	    if ((string != null) ) {
		executeBatchInfo.append("Row "+i+" COURTHOUSE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }



	    i++; 
	} 

		// Now delete the batch
		sql = "DELETE FROM " + tableName + " WHERE OFFICER_ID = ? AND COURTHOUSE_ID = 'CH0001'";
		ps = connection_.prepareStatement(sql);

	        for ( i = 1; i < 100; i++) {
	          ps.setString(1, "" + i);
	          ps.addBatch();
	        }


		executeBatchCount++; 

		executeBatchInfo.append("calling batch "+executeBatchCount+" delete entries\n");
	 ps.executeBatch();

		sql = "DROP TABLE "+tableName;
		executeBatchInfo.append(sql); 
		s.executeUpdate(sql);
		s.close();
		ps.close(); 


		assertCondition(passed, "Test failed\nInfo=\n"+executeBatchInfo.toString()); 

            }
            catch (Exception e) {

                failed (e, "Unexpected Exception -added by TB 6/22/09 for multirow update sql = "+sql+"\nInfo=\n"+executeBatchInfo.toString());

            }
        }
    }




    //UPDATE STATEMENT with null literal in second set position with multiple batches 
    public void Var074 ()
    {
	if (getDriver() != JDTestDriver.DRIVER_NATIVE && useBlockInsert) {  notApplicable("Native block insert test");  return;	} 
	StringBuffer executeBatchInfo = new StringBuffer();

	executeBatchInfo.append("Testing literal null in second set position with multiple batches");

	int executeBatchCount = 0; 
	// Batching should work for all releases.. 
        // if(false) {
        //    notApplicable("Batched update/merge/delete vor v7r1 only" ); 
        //    return;
        //}

	boolean passed = true; 


        if (checkJdbc20 ()) {
	    String sql = ""; 
            try {
                Statement s = connection_.createStatement ();

		String tableName = JDPSTest.COLLECTION + ".JDPSBAT74";

		sql = "DROP TABLE "+tableName;
		try {
		    s.executeUpdate(sql); 
		} catch (Exception e) {
		} 

		sql = "CREATE TABLE "+tableName+"("+
		  "OFFICER_ID INTEGER,"+
		  "COURTHOUSE_ID CHAR(6)," +
		  "TITLE  VARCHAR(20)," +
		  "NAME VARCHAR(36),"+
		  "BADGE_NUMBER INTEGER,"+
		  "DATE_OF_SERVICE DATE,"+
		  "PATROL_TYPE VARCHAR(20),"+
		  "HIRED_BY INTEGER)";

		s.executeUpdate(sql); 

		sql = "INSERT INTO "+tableName+" VALUES(?,?,?,?,?,?,?,?)"; 
		PreparedStatement ps = connection_.prepareStatement(sql);

	for (int i = 1; i <= 100; i++) {
          ps.setString(1, "" + i);
          ps.setString(2, "CH0001");
          ps.setString(3, "TITLE" + i);
          ps.setString(4, "NAME" + i);
          ps.setString(5, "" + i);
          ps.setDate(6, new java.sql.Date(i));
          ps.setString(7, "PATROL" + i);
	  if (i == 1) {
	      ps.setString(8, "1"); 
	  } else { 
	      ps.setString(8, "" + (i-1));
	  }
          ps.addBatch();
	  if (i%20==0) {
	      executeBatchCount++;
	      executeBatchInfo.append("calling batch "+executeBatchCount+" insert into table\n"); 
	      ps.executeBatch();

	  }
        }


        sql = "UPDATE "
            + tableName
            + " SET  TITLE = ?, COURTHOUSE_ID = NULL, NAME = ?  WHERE OFFICER_ID = ? AND COURTHOUSE_ID = ? and NAME = ? ";
        ps = connection_.prepareStatement(sql);

        for (int i = 1; i <= 100; i++) {
	  // Set every 10th title to null
	  if (i%10 == 0) {
	      ps.setString(1, null); 
	  } else { 
	      ps.setString(1, "TITLE" + i + "X");
	  }
          ps.setString(2, "NAME" + i + "X");
          ps.setString(3, "" + i );
	  ps.setString(4, "CH0001"); 
          ps.setString(5, "NAME"+i); 
          ps.addBatch();
	  if (i%20==0) {
	      executeBatchCount++; 
	      executeBatchInfo.append("calling batch "+executeBatchCount+" update entries\n"); 
	      ps.executeBatch();
	  }

        }


	// Verify updates were made
	sql = "select TITLE, NAME, BADGE_NUMBER, DATE_OF_SERVICE, PATROL_TYPE, HIRED_BY, OFFICER_ID, COURTHOUSE_ID FROM "+tableName+" ORDER BY OFFICER_ID";

	executeBatchInfo.append("Verify using query:  "+sql); 
	ResultSet rs = s.executeQuery(sql);
	int i = 1; 
	while (rs.next()) {
	    String string;
	    String expected;

	    string = rs.getString(1);
	    expected ="TITLE" + i + "X";

	    if (i%10 == 0) {
		if (string != null) {
		    executeBatchInfo.append("Row "+i+" TITLE:"+"Got '"+string+"' expected 'null'\n");
		    passed = false; 
		}
	    } else { 
		if ((string == null) || ! string.equals(expected)) {
		    executeBatchInfo.append("Row "+i+" TITLE:"+"Got '"+string+"' expected '"+expected+"'\n");
		    passed = false; 
		}
	    }

	    string = rs.getString(2);
	    expected ="NAME" + i + "X";
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" NAME:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(3);
	    expected = "" + i ;
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" BADGE NUMBER:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getDate(4).toString();
	    java.sql.Date ed = new java.sql.Date(i); 
	    expected = ed.toString(); 

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" DATE_OF_SERVICE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(5);
	    expected =  "PATROL" + i ; 
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" PATROL TYPE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(6);
	    if (i == 1) {
		expected = "1";
	    } else { 
		expected = ""+(i-1);
	    }
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" HIRED BY:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(7);
	    expected =  ""+i;

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" OFFICER ID:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }


	    string = rs.getString(8);
	    expected =  null;

	    if ((string != null)) {
		executeBatchInfo.append("Row "+i+" COURTHOUSE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }



	    i++; 
	} 

		// Now delete the batch
		sql = "DELETE FROM " + tableName + " WHERE OFFICER_ID = ? AND COURTHOUSE_ID = 'CH0001'";
		ps = connection_.prepareStatement(sql);

	        for ( i = 1; i < 100; i++) {
	          ps.setString(1, "" + i);
	          ps.addBatch();
	        }


		executeBatchCount++; 

		executeBatchInfo.append("calling batch "+executeBatchCount+" delete entries\n");
		 ps.executeBatch();

		sql = "DROP TABLE "+tableName;
		executeBatchInfo.append(sql); 
		s.executeUpdate(sql);
		s.close();
		ps.close(); 


		assertCondition(passed, "Test failed\nInfo=\n"+executeBatchInfo.toString()); 

            }
            catch (Exception e) {

                failed (e, "Unexpected Exception -added by TB 6/22/09 for multirow update sql = "+sql+"\nInfo=\n"+executeBatchInfo.toString());

            }
        }
    }



   //UPDATE STATEMENT with null  literal in third set position with multiple batches 
    public void Var075 ()
    {
	if (getDriver() != JDTestDriver.DRIVER_NATIVE && useBlockInsert) {  notApplicable("Native block insert test");  return;	} 
	StringBuffer executeBatchInfo = new StringBuffer();

	executeBatchInfo.append("Testing literal null in third set position with multiple batches");

	int executeBatchCount = 0; 
	// Batching should work for all releases.. 
        // if(false) {
        //    notApplicable("Batched update/merge/delete vor v7r1 only" ); 
        //    return;
        //}

	boolean passed = true; 


        if (checkJdbc20 ()) {
	    String sql = ""; 
            try {
                Statement s = connection_.createStatement ();

		String tableName = JDPSTest.COLLECTION + ".JDPSBAT75";

		sql = "DROP TABLE "+tableName;
		try {
		    s.executeUpdate(sql); 
		} catch (Exception e) {
		} 

		sql = "CREATE TABLE "+tableName+"("+
		  "OFFICER_ID INTEGER,"+
		  "COURTHOUSE_ID CHAR(6)," +
		  "TITLE  VARCHAR(20)," +
		  "NAME VARCHAR(36),"+
		  "BADGE_NUMBER INTEGER,"+
		  "DATE_OF_SERVICE DATE,"+
		  "PATROL_TYPE VARCHAR(20),"+
		  "HIRED_BY INTEGER)";

		s.executeUpdate(sql); 

		sql = "INSERT INTO "+tableName+" VALUES(?,?,?,?,?,?,?,?)"; 
		PreparedStatement ps = connection_.prepareStatement(sql);

	for (int i = 1; i <= 100; i++) {
          ps.setString(1, "" + i);
          ps.setString(2, "CH0001");
          ps.setString(3, "TITLE" + i);
          ps.setString(4, "NAME" + i);
          ps.setString(5, "" + i);
          ps.setDate(6, new java.sql.Date(i));
          ps.setString(7, "PATROL" + i);
	  if (i == 1) {
	      ps.setString(8, "1"); 
	  } else { 
	      ps.setString(8, "" + (i-1));
	  }
          ps.addBatch();
	  if (i%20==0) {
	      executeBatchCount++;
	      executeBatchInfo.append("calling batch "+executeBatchCount+" insert into table\n"); 
	      ps.executeBatch();

	  }
        }


        sql = "UPDATE "
            + tableName
            + " SET  TITLE = ?, NAME = ?, COURTHOUSE_ID = NULL  WHERE OFFICER_ID = ? AND COURTHOUSE_ID = ? and NAME = ? ";
        ps = connection_.prepareStatement(sql);

        for (int i = 1; i <= 100; i++) {
	  // Set every 10th title to null
	  if (i%10 == 0) {
	      ps.setString(1, null); 
	  } else { 
	      ps.setString(1, "TITLE" + i + "X");
	  }
          ps.setString(2, "NAME" + i + "X");
          ps.setString(3, "" + i );
	  ps.setString(4, "CH0001"); 
          ps.setString(5, "NAME"+i); 
          ps.addBatch();
	  if (i%20==0) {
	      executeBatchCount++; 
	      executeBatchInfo.append("calling batch "+executeBatchCount+" update entries\n"); 
	      ps.executeBatch();
	  }

        }


	// Verify updates were made
	sql = "select TITLE, NAME, BADGE_NUMBER, DATE_OF_SERVICE, PATROL_TYPE, HIRED_BY, OFFICER_ID, COURTHOUSE_ID FROM "+tableName+" ORDER BY OFFICER_ID";

	executeBatchInfo.append("Verify using query:  "+sql); 
	ResultSet rs = s.executeQuery(sql);
	int i = 1; 
	while (rs.next()) {
	    String string;
	    String expected;

	    string = rs.getString(1);
	    expected ="TITLE" + i + "X";

	    if (i%10 == 0) {
		if (string != null) {
		    executeBatchInfo.append("Row "+i+" TITLE:"+"Got '"+string+"' expected 'null'\n");
		    passed = false; 
		}
	    } else { 
		if ((string == null) || ! string.equals(expected)) {
		    executeBatchInfo.append("Row "+i+" TITLE:"+"Got '"+string+"' expected '"+expected+"'\n");
		    passed = false; 
		}
	    }

	    string = rs.getString(2);
	    expected ="NAME" + i + "X";
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" NAME:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(3);
	    expected = "" + i ;
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" BADGE NUMBER:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getDate(4).toString();
	    java.sql.Date ed = new java.sql.Date(i); 
	    expected = ed.toString(); 

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" DATE_OF_SERVICE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(5);
	    expected =  "PATROL" + i ; 
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" PATROL TYPE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(6);
	    if (i == 1) {
		expected = "1";
	    } else { 
		expected = ""+(i-1);
	    }
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" HIRED BY:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(7);
	    expected =  ""+i;

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" OFFICER ID:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }


	    string = rs.getString(8);
	    expected =  null;

	    if ((string != null)) {
		executeBatchInfo.append("Row "+i+" COURTHOUSE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }



	    i++; 
	} 

		// Now delete the batch
		sql = "DELETE FROM " + tableName + " WHERE OFFICER_ID = ? AND COURTHOUSE_ID = 'CH0001'";
		ps = connection_.prepareStatement(sql);

	        for ( i = 1; i < 100; i++) {
	          ps.setString(1, "" + i);
	          ps.addBatch();
	        }


		executeBatchCount++; 

		executeBatchInfo.append("calling batch "+executeBatchCount+" delete entries\n");
		ps.executeBatch();
		

		sql = "DROP TABLE "+tableName;
		executeBatchInfo.append(sql); 
		s.executeUpdate(sql);
		s.close();
		ps.close(); 

		assertCondition(passed, "Test failed\nInfo=\n"+executeBatchInfo.toString()); 

            }
            catch (Exception e) {

                failed (e, "Unexpected Exception -added by TB 6/22/09 for multirow update sql = "+sql+"\nInfo=\n"+executeBatchInfo.toString());

            }
        }
    }



   //UPDATE STATEMENT with null literal in first where  position with multiple batches 
    public void Var076 ()
    {
	if (getDriver() != JDTestDriver.DRIVER_NATIVE && useBlockInsert) {  notApplicable("Native block insert test");  return;	} 
	StringBuffer executeBatchInfo = new StringBuffer();

	executeBatchInfo.append("Testing literal null in first where position with multiple batches");

	int executeBatchCount = 0; 
	// Batching should work for all releases.. 
        // if(false) {
        //    notApplicable("Batched update/merge/delete vor v7r1 only" ); 
        //    return;
        //}

	boolean passed = true; 


        if (checkJdbc20 ()) {
	    String sql = ""; 
            try {
                Statement s = connection_.createStatement ();

		String tableName = JDPSTest.COLLECTION + ".JDPSBAT76";

		sql = "DROP TABLE "+tableName;
		try {
		    s.executeUpdate(sql); 
		} catch (Exception e) {
		} 

		sql = "CREATE TABLE "+tableName+"("+
		  "OFFICER_ID INTEGER,"+
		  "COURTHOUSE_ID CHAR(6)," +
		  "TITLE  VARCHAR(20)," +
		  "NAME VARCHAR(36),"+
		  "BADGE_NUMBER INTEGER,"+
		  "DATE_OF_SERVICE DATE,"+
		  "PATROL_TYPE VARCHAR(20),"+
		  "HIRED_BY INTEGER)";

		s.executeUpdate(sql); 

		sql = "INSERT INTO "+tableName+" VALUES(?,?,?,?,?,?,?,?)"; 
		PreparedStatement ps = connection_.prepareStatement(sql);

	for (int i = 1; i <= 100; i++) {
          ps.setString(1, "" + i);
          ps.setString(2, "CH0001");
          ps.setString(3, "TITLE" + i);
          ps.setString(4, "NAME" + i);
          ps.setString(5, "" + i);
          ps.setDate(6, new java.sql.Date(i));
          ps.setString(7, "PATROL" + i);
	  if (i == 1) {
	      ps.setString(8, "1"); 
	  } else { 
	      ps.setString(8, "" + (i-1));
	  }
          ps.addBatch();
	  if (i%20==0) {
	      executeBatchCount++;
	      executeBatchInfo.append("calling batch "+executeBatchCount+" insert into table\n"); 
	      ps.executeBatch();

	  }
        }


        sql = "UPDATE "
            + tableName
            + " SET  TITLE = ?, NAME = ?, COURTHOUSE_ID = ?  WHERE COURTHOUSE_ID IS NOT NULL  and OFFICER_ID = ? AND NAME = ? ";
        ps = connection_.prepareStatement(sql);

        for (int i = 1; i <= 100; i++) {
	  // Set every 10th title to null
	  if (i%10 == 0) {
	      ps.setString(1, null); 
	  } else { 
	      ps.setString(1, "TITLE" + i + "X");
	  }
          ps.setString(2, "NAME" + i + "X");
	  ps.setString(3, "CH0002"); 
          ps.setString(4, "" + i );
          ps.setString(5, "NAME"+i); 
          ps.addBatch();
	  if (i%20==0) {
	      executeBatchCount++; 
	      executeBatchInfo.append("calling batch "+executeBatchCount+" update entries\n"); 
	      ps.executeBatch();
	  }

        }


	// Verify updates were made
	sql = "select TITLE, NAME, BADGE_NUMBER, DATE_OF_SERVICE, PATROL_TYPE, HIRED_BY, OFFICER_ID, COURTHOUSE_ID FROM "+tableName+" ORDER BY OFFICER_ID";

	executeBatchInfo.append("Verify using query:  "+sql); 
	ResultSet rs = s.executeQuery(sql);
	int i = 1; 
	while (rs.next()) {
	    String string;
	    String expected;

	    string = rs.getString(1);
	    expected ="TITLE" + i + "X";

	    if (i%10 == 0) {
		if (string != null) {
		    executeBatchInfo.append("Row "+i+" TITLE:"+"Got '"+string+"' expected 'null'\n");
		    passed = false; 
		}
	    } else { 
		if ((string == null) || ! string.equals(expected)) {
		    executeBatchInfo.append("Row "+i+" TITLE:"+"Got '"+string+"' expected '"+expected+"'\n");
		    passed = false; 
		}
	    }

	    string = rs.getString(2);
	    expected ="NAME" + i + "X";
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" NAME:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(3);
	    expected = "" + i ;
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" BADGE NUMBER:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getDate(4).toString();
	    java.sql.Date ed = new java.sql.Date(i); 
	    expected = ed.toString(); 

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" DATE_OF_SERVICE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(5);
	    expected =  "PATROL" + i ; 
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" PATROL TYPE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(6);
	    if (i == 1) {
		expected = "1";
	    } else { 
		expected = ""+(i-1);
	    }
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" HIRED BY:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(7);
	    expected =  ""+i;

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" OFFICER ID:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }


	    string = rs.getString(8);
	    expected =  "CH0002";

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" COURTHOUSE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }



	    i++; 
	} 

		// Now delete the batch
		sql = "DELETE FROM " + tableName + " WHERE OFFICER_ID = ? AND COURTHOUSE_ID = 'CH0001'";
		ps = connection_.prepareStatement(sql);

	        for ( i = 1; i < 100; i++) {
	          ps.setString(1, "" + i);
	          ps.addBatch();
	        }


		executeBatchCount++; 

		executeBatchInfo.append("calling batch "+executeBatchCount+" delete entries\n");
	 ps.executeBatch();

	 sql = "DROP TABLE "+tableName;
	 executeBatchInfo.append(sql); 
	 s.executeUpdate(sql);
	 s.close();
	 ps.close(); 


		assertCondition(passed, "Test failed\nInfo=\n"+executeBatchInfo.toString()); 

            }
            catch (Exception e) {

                failed (e, "Unexpected Exception -added by TB 6/22/09 for multirow update sql = "+sql+"\nInfo=\n"+executeBatchInfo.toString());

            }
        }
    }

   //UPDATE STATEMENT with null literal in second where  position with multiple batches 
    public void Var077 ()
    {
	if (getDriver() != JDTestDriver.DRIVER_NATIVE && useBlockInsert) {  notApplicable("Native block insert test");  return;	} 
	StringBuffer executeBatchInfo = new StringBuffer();

	executeBatchInfo.append("Testing literal null in second where position with multiple batches");

	int executeBatchCount = 0; 
	// Batching should work for all releases.. 
        // if(false) {
        //    notApplicable("Batched update/merge/delete vor v7r1 only" ); 
        //    return;
        //}

	boolean passed = true; 


        if (checkJdbc20 ()) {
	    String sql = ""; 
            try {
                Statement s = connection_.createStatement ();

		String tableName = JDPSTest.COLLECTION + ".JDPSBAT77";

		sql = "DROP TABLE "+tableName;
		try {
		    s.executeUpdate(sql); 
		} catch (Exception e) {
		} 

		sql = "CREATE TABLE "+tableName+"("+
		  "OFFICER_ID INTEGER,"+
		  "COURTHOUSE_ID CHAR(6)," +
		  "TITLE  VARCHAR(20)," +
		  "NAME VARCHAR(36),"+
		  "BADGE_NUMBER INTEGER,"+
		  "DATE_OF_SERVICE DATE,"+
		  "PATROL_TYPE VARCHAR(20),"+
		  "HIRED_BY INTEGER)";

		s.executeUpdate(sql); 

		sql = "INSERT INTO "+tableName+" VALUES(?,?,?,?,?,?,?,?)"; 
		PreparedStatement ps = connection_.prepareStatement(sql);

	for (int i = 1; i <= 100; i++) {
          ps.setString(1, "" + i);
          ps.setString(2, "CH0001");
          ps.setString(3, "TITLE" + i);
          ps.setString(4, "NAME" + i);
          ps.setString(5, "" + i);
          ps.setDate(6, new java.sql.Date(i));
          ps.setString(7, "PATROL" + i);
	  if (i == 1) {
	      ps.setString(8, "1"); 
	  } else { 
	      ps.setString(8, "" + (i-1));
	  }
          ps.addBatch();
	  if (i%20==0) {
	      executeBatchCount++;
	      executeBatchInfo.append("calling batch "+executeBatchCount+" insert into table\n"); 
	      ps.executeBatch();

	  }
        }


        sql = "UPDATE "
            + tableName
            + " SET  TITLE = ?, NAME = ?, COURTHOUSE_ID = ?  WHERE OFFICER_ID = ? AND COURTHOUSE_ID IS NOT NULL and NAME = ? ";
        ps = connection_.prepareStatement(sql);

        for (int i = 1; i <= 100; i++) {
	  // Set every 10th title to null
	  if (i%10 == 0) {
	      ps.setString(1, null); 
	  } else { 
	      ps.setString(1, "TITLE" + i + "X");
	  }
          ps.setString(2, "NAME" + i + "X");
	  ps.setString(3, "CH0002"); 
          ps.setString(4, "" + i );
          ps.setString(5, "NAME"+i); 
          ps.addBatch();
	  if (i%20==0) {
	      executeBatchCount++; 
	      executeBatchInfo.append("calling batch "+executeBatchCount+" update entries\n"); 
	      ps.executeBatch();
	  }

        }


	// Verify updates were made
	sql = "select TITLE, NAME, BADGE_NUMBER, DATE_OF_SERVICE, PATROL_TYPE, HIRED_BY, OFFICER_ID, COURTHOUSE_ID FROM "+tableName+" ORDER BY OFFICER_ID";

	executeBatchInfo.append("Verify using query:  "+sql); 
	ResultSet rs = s.executeQuery(sql);
	int i = 1; 
	while (rs.next()) {
	    String string;
	    String expected;

	    string = rs.getString(1);
	    expected ="TITLE" + i + "X";

	    if (i%10 == 0) {
		if (string != null) {
		    executeBatchInfo.append("Row "+i+" TITLE:"+"Got '"+string+"' expected 'null'\n");
		    passed = false; 
		}
	    } else { 
		if ((string == null) || ! string.equals(expected)) {
		    executeBatchInfo.append("Row "+i+" TITLE:"+"Got '"+string+"' expected '"+expected+"'\n");
		    passed = false; 
		}
	    }

	    string = rs.getString(2);
	    expected ="NAME" + i + "X";
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" NAME:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(3);
	    expected = "" + i ;
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" BADGE NUMBER:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getDate(4).toString();
	    java.sql.Date ed = new java.sql.Date(i); 
	    expected = ed.toString(); 

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" DATE_OF_SERVICE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(5);
	    expected =  "PATROL" + i ; 
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" PATROL TYPE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(6);
	    if (i == 1) {
		expected = "1";
	    } else { 
		expected = ""+(i-1);
	    }
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" HIRED BY:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(7);
	    expected =  ""+i;

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" OFFICER ID:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }


	    string = rs.getString(8);
	    expected =  "CH0002";

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" COURTHOUSE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }



	    i++; 
	} 

		// Now delete the batch
		sql = "DELETE FROM " + tableName + " WHERE OFFICER_ID = ? AND COURTHOUSE_ID = 'CH0001'";
		ps = connection_.prepareStatement(sql);

	        for ( i = 1; i < 100; i++) {
	          ps.setString(1, "" + i);
	          ps.addBatch();
	        }


		executeBatchCount++; 

		executeBatchInfo.append("calling batch "+executeBatchCount+" delete entries\n");
	 ps.executeBatch();
		

		assertCondition(passed, "Test failed\nInfo=\n"+executeBatchInfo.toString()); 

            }
            catch (Exception e) {

                failed (e, "Unexpected Exception -added by TB 6/22/09 for multirow update sql = "+sql+"\nInfo=\n"+executeBatchInfo.toString());

            }
        }
    }


   //UPDATE STATEMENT with null literal in third where  position with multiple batches 
    public void Var078 ()
    {
	if (getDriver() != JDTestDriver.DRIVER_NATIVE && useBlockInsert) {  notApplicable("Native block insert test");  return;	} 
	StringBuffer executeBatchInfo = new StringBuffer();

	executeBatchInfo.append("Testing literal null in third set position with multiple batches");

	int executeBatchCount = 0; 
	// Batching should work for all releases.. 
        // if(false) {
        //    notApplicable("Batched update/merge/delete vor v7r1 only" ); 
        //    return;
        //}

	boolean passed = true; 


        if (checkJdbc20 ()) {
	    String sql = ""; 
            try {
                Statement s = connection_.createStatement ();

		String tableName = JDPSTest.COLLECTION + ".JDPSBAT78";

		sql = "DROP TABLE "+tableName;
		try {
		    s.executeUpdate(sql); 
		} catch (Exception e) {
		} 

		sql = "CREATE TABLE "+tableName+"("+
		  "OFFICER_ID INTEGER,"+
		  "COURTHOUSE_ID CHAR(6)," +
		  "TITLE  VARCHAR(20)," +
		  "NAME VARCHAR(36),"+
		  "BADGE_NUMBER INTEGER,"+
		  "DATE_OF_SERVICE DATE,"+
		  "PATROL_TYPE VARCHAR(20),"+
		  "HIRED_BY INTEGER)";

		s.executeUpdate(sql); 

		sql = "INSERT INTO "+tableName+" VALUES(?,?,?,?,?,?,?,?)"; 
		PreparedStatement ps = connection_.prepareStatement(sql);

	for (int i = 1; i <= 100; i++) {
          ps.setString(1, "" + i);
          ps.setString(2, "CH0001");
          ps.setString(3, "TITLE" + i);
          ps.setString(4, "NAME" + i);
          ps.setString(5, "" + i);
          ps.setDate(6, new java.sql.Date(i));
          ps.setString(7, "PATROL" + i);
	  if (i == 1) {
	      ps.setString(8, "1"); 
	  } else { 
	      ps.setString(8, "" + (i-1));
	  }
          ps.addBatch();
	  if (i%20==0) {
	      executeBatchCount++;
	      executeBatchInfo.append("calling batch "+executeBatchCount+" insert into table\n"); 
	      ps.executeBatch();

	  }
        }


        sql = "UPDATE "
            + tableName
            + " SET  TITLE = ?, NAME = ?, COURTHOUSE_ID = ?  WHERE  OFFICER_ID = ? AND NAME = ? AND COURTHOUSE_ID IS NOT NULL ";
        ps = connection_.prepareStatement(sql);

        for (int i = 1; i <= 100; i++) {
	  // Set every 10th title to null
	  if (i%10 == 0) {
	      ps.setString(1, null); 
	  } else { 
	      ps.setString(1, "TITLE" + i + "X");
	  }
          ps.setString(2, "NAME" + i + "X");
	  ps.setString(3, "CH0002"); 
          ps.setString(4, "" + i );
          ps.setString(5, "NAME"+i); 
          ps.addBatch();
	  if (i%20==0) {
	      executeBatchCount++; 
	      executeBatchInfo.append("calling batch "+executeBatchCount+" update entries\n"); 
	      ps.executeBatch();
	  }

        }


	// Verify updates were made
	sql = "select TITLE, NAME, BADGE_NUMBER, DATE_OF_SERVICE, PATROL_TYPE, HIRED_BY, OFFICER_ID, COURTHOUSE_ID FROM "+tableName+" ORDER BY OFFICER_ID";

	executeBatchInfo.append("Verify using query:  "+sql); 
	ResultSet rs = s.executeQuery(sql);
	int i = 1; 
	while (rs.next()) {
	    String string;
	    String expected;

	    string = rs.getString(1);
	    expected ="TITLE" + i + "X";

	    if (i%10 == 0) {
		if (string != null) {
		    executeBatchInfo.append("Row "+i+" TITLE:"+"Got '"+string+"' expected 'null'\n");
		    passed = false; 
		}
	    } else { 
		if ((string == null) || ! string.equals(expected)) {
		    executeBatchInfo.append("Row "+i+" TITLE:"+"Got '"+string+"' expected '"+expected+"'\n");
		    passed = false; 
		}
	    }

	    string = rs.getString(2);
	    expected ="NAME" + i + "X";
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" NAME:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(3);
	    expected = "" + i ;
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" BADGE NUMBER:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getDate(4).toString();
	    java.sql.Date ed = new java.sql.Date(i); 
	    expected = ed.toString(); 

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" DATE_OF_SERVICE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(5);
	    expected =  "PATROL" + i ; 
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" PATROL TYPE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(6);
	    if (i == 1) {
		expected = "1";
	    } else { 
		expected = ""+(i-1);
	    }
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" HIRED BY:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(7);
	    expected =  ""+i;

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" OFFICER ID:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }


	    string = rs.getString(8);
	    expected =  "CH0002";

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" COURTHOUSE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }



	    i++; 
	} 

		// Now delete the batch
		sql = "DELETE FROM " + tableName + " WHERE OFFICER_ID = ? AND COURTHOUSE_ID = 'CH0001'";
		ps = connection_.prepareStatement(sql);

	        for ( i = 1; i < 100; i++) {
	          ps.setString(1, "" + i);
	          ps.addBatch();
	        }


		executeBatchCount++; 

		executeBatchInfo.append("calling batch "+executeBatchCount+" delete entries\n");
	 ps.executeBatch();

		sql = "DROP TABLE "+tableName;
		executeBatchInfo.append(sql); 
		s.executeUpdate(sql);
		s.close();
		ps.close(); 


		assertCondition(passed, "Test failed\nInfo=\n"+executeBatchInfo.toString()); 

            }
            catch (Exception e) {

                failed (e, "Unexpected Exception -added by TB 6/22/09 for multirow update sql = "+sql+"\nInfo=\n"+executeBatchInfo.toString());

            }
        }
    }





    public void testNoLiteralInSetPosition(int position, String comment, String filename) {

	if (getDriver() != JDTestDriver.DRIVER_NATIVE && useBlockInsert) {  notApplicable("Native block insert test");  return;	} 

	StringBuffer executeBatchInfo = new StringBuffer();

	executeBatchInfo.append(comment);

	int executeBatchCount = 0; 
	// Batching should work for all releases.. 
        // if(false) {
        //    notApplicable("Batched update/merge/delete vor v7r1 only" ); 
        //    return;
        //}

	boolean passed = true; 


        if (checkJdbc20 ()) {
	    String sql = ""; 
            try {
                Statement s = connection_.createStatement ();

		String tableName = JDPSTest.COLLECTION + "."+filename; 

		sql = "DROP TABLE "+tableName;
		try {
		    s.executeUpdate(sql); 
		} catch (Exception e) {
		} 

		sql = "CREATE TABLE "+tableName+"("+
		  "OFFICER_ID INTEGER,"+
		  "COURTHOUSE_ID CHAR(6)," +
		  "TITLE  VARCHAR(20)," +
		  "NAME VARCHAR(36),"+
		  "BADGE_NUMBER INTEGER,"+
		  "DATE_OF_SERVICE DATE,"+
		  "PATROL_TYPE VARCHAR(20),"+
		  "HIRED_BY INTEGER)";

		s.executeUpdate(sql); 

		sql = "INSERT INTO "+tableName+" VALUES(?,?,?,?,?,?,?,?)"; 
		PreparedStatement ps = connection_.prepareStatement(sql);

	for (int i = 1; i <= 100; i++) {
          ps.setString(1, "" + i);
          ps.setString(2, "CH0001");
          ps.setString(3, "TITLE" + i);
          ps.setString(4, "NAME" + i);
          ps.setString(5, "" + i);
          ps.setDate(6, new java.sql.Date(i));
          ps.setString(7, "PATROL" + i);
	  if (i == 1) {
	      ps.setString(8, "1"); 
	  } else { 
	      ps.setString(8, "" + (i-1));
	  }
          ps.addBatch();
	  if (i%20==0) {
	      executeBatchCount++;
	      executeBatchInfo.append("calling batch "+executeBatchCount+" insert into table\n"); 
	      ps.executeBatch();

	  }
        }

	switch(position) {
	    case 1:
		sql = "UPDATE "
		  + tableName
		  + " SET TITLE = ?, COURTHOUSE_ID = 'CH0002', PATROL_TYPE = 'PTXXX'   WHERE OFFICER_ID = ? AND COURTHOUSE_ID = ? and NAME = ? ";
		break;
	    case 2:
		sql = "UPDATE "
		  + tableName
		  + " SET COURTHOUSE_ID = 'CH0002', TITLE = ?, PATROL_TYPE = 'PTXXX'  WHERE OFFICER_ID = ? AND COURTHOUSE_ID = ? and NAME = ? ";
		break;
	    case 3:
		sql = "UPDATE "
		  + tableName
		  + " SET COURTHOUSE_ID = 'CH0002', PATROL_TYPE = 'PTXXX', TITLE = ?  WHERE OFFICER_ID = ? AND COURTHOUSE_ID = ? and NAME = ? ";
		break;
	    default:
		sql = "INVALID POSITION"; 
	}

        ps = connection_.prepareStatement(sql);

        for (int i = 1; i <= 100; i++) {
	  // Set every 10th title to null
	  if (i%10 == 0) {
	      ps.setString(1, null); 
	  } else { 
	      ps.setString(1, "TITLE" + i + "X");
	  }
          ps.setString(2, "" + i );
	  ps.setString(3, "CH0001"); 
          ps.setString(4, "NAME"+i); 
          ps.addBatch();
	  if (i%20==0) {
	      executeBatchCount++; 
	      executeBatchInfo.append("calling batch "+executeBatchCount+" update entries\n"); 
	      ps.executeBatch();
	  }

        }


	// Verify updates were made
	sql = "select TITLE, NAME, BADGE_NUMBER, DATE_OF_SERVICE, PATROL_TYPE, HIRED_BY, OFFICER_ID, COURTHOUSE_ID FROM "+tableName+" ORDER BY OFFICER_ID";

	executeBatchInfo.append("Verify using query:  "+sql); 
	ResultSet rs = s.executeQuery(sql);
	int i = 1; 
	while (rs.next()) {
	    String string;
	    String expected;

	    string = rs.getString(1);
	    expected ="TITLE" + i + "X";

	    if (i%10 == 0) {
		if (string != null) {
		    executeBatchInfo.append("Row "+i+" TITLE:"+"Got '"+string+"' expected 'null'\n");
		    passed = false; 
		}
	    } else { 
		if ((string == null) || ! string.equals(expected)) {
		    executeBatchInfo.append("Row "+i+" TITLE:"+"Got '"+string+"' expected '"+expected+"'\n");
		    passed = false; 
		}
	    }

	    string = rs.getString(2);
	    expected ="NAME" + i ;
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" NAME:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(3);
	    expected = "" + i ;
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" BADGE NUMBER:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getDate(4).toString();
	    java.sql.Date ed = new java.sql.Date(i); 
	    expected = ed.toString(); 

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" DATE_OF_SERVICE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(5);
	    expected =  "PTXXX";
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" PATROL TYPE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(6);
	    if (i == 1) {
		expected = "1";
	    } else { 
		expected = ""+(i-1);
	    }
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" HIRED BY:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(7);
	    expected =  ""+i;

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" OFFICER ID:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }


	    string = rs.getString(8);
	    expected =  "CH0002";

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" COURTHOUSE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }



	    i++; 
	}
	rs.close();


		// Now delete the batch
		sql = "DELETE FROM " + tableName + " WHERE OFFICER_ID = ? AND COURTHOUSE_ID = 'CH0001'";
		ps = connection_.prepareStatement(sql);

	        for ( i = 1; i < 100; i++) {
	          ps.setString(1, "" + i);
	          ps.addBatch();
	        }


		executeBatchCount++; 

		executeBatchInfo.append("calling batch "+executeBatchCount+" delete entries\n");
	 ps.executeBatch();
		
		ps.close();

		sql = "DROP TABLE "+tableName;
		executeBatchInfo.append(sql); 
		s.executeUpdate(sql);
		s.close();
	
		assertCondition(passed, "Test failed\nInfo=\n"+executeBatchInfo.toString()); 

            }
            catch (Exception e) {

                failed (e, "Unexpected Exception -added by TB 6/22/09 for multirow update sql = "+sql+"\nInfo=\n"+executeBatchInfo.toString());

            }
        }
 
    } 

    //UPDATE STATEMENT with literal in first and second set position with multiple batches 
    public void Var079 ()
    {
	testNoLiteralInSetPosition(3, "Testing literal in first and second set position with multiple batches", "JDPSBAT79");
    }
    public void Var080 ()
    {
	if (getDriver() != JDTestDriver.DRIVER_NATIVE && useBlockInsert) {  notApplicable("Native block insert test");  return;	} 
	testNoLiteralInSetPosition(2, "Testing literal in first and third set position with multiple batches", "JDPSBAT80");
    }
    public void Var081 ()
    {
	testNoLiteralInSetPosition(1, "Testing literal in second and third set position with multiple batches", "JDPSBAT81");
    }



    public void testNoLiteralInWherePosition(int position, String comment, int variation) {
	if (getDriver() != JDTestDriver.DRIVER_NATIVE && useBlockInsert) {  notApplicable("Native block insert test");  return;	} 

	StringBuffer executeBatchInfo = new StringBuffer();

	executeBatchInfo.append(comment);

	int executeBatchCount = 0; 
	// Batching should work for all releases.. 
        // if(false) {
        //    notApplicable("Batched update/merge/delete vor v7r1 only" ); 
        //    return;
        //}

	boolean passed = true; 


        if (checkJdbc20 ()) {
	    String sql = ""; 
            try {
                Statement s = connection_.createStatement ();

		String tableName = JDPSTest.COLLECTION + ".JDPSBAT"+variation; 

		sql = "DROP TABLE "+tableName;
		try {
		    s.executeUpdate(sql); 
		} catch (Exception e) {
		} 

		sql = "CREATE TABLE "+tableName+"("+
		  "OFFICER_ID INTEGER,"+
		  "COURTHOUSE_ID CHAR(6)," +
		  "TITLE  VARCHAR(20)," +
		  "NAME VARCHAR(36),"+
		  "BADGE_NUMBER INTEGER,"+
		  "DATE_OF_SERVICE DATE,"+
		  "PATROL_TYPE VARCHAR(20),"+
		  "HIRED_BY INTEGER)";

		s.executeUpdate(sql); 

		sql = "INSERT INTO "+tableName+" VALUES(?,?,?,?,?,?,?,?)"; 
		PreparedStatement ps = connection_.prepareStatement(sql);

	for (int i = 1; i <= 100; i++) {
          ps.setString(1, "" + i);
          ps.setString(2, "CH0001");
          ps.setString(3, "TITLE" + i);
          ps.setString(4, "NAME" + i);
          ps.setString(5, "" + i);
          ps.setDate(6, new java.sql.Date(i));
          ps.setString(7, "PATROL" + i);
	  if (i == 1) {
	      ps.setString(8, "1"); 
	  } else { 
	      ps.setString(8, "" + (i-1));
	  }
          ps.addBatch();
	  if (i%20==0) {
	      executeBatchCount++;
	      executeBatchInfo.append("calling batch "+executeBatchCount+" insert into table\n"); 
	      ps.executeBatch();

	  }
        }

	switch(position) {
	    case 1:
		sql = "UPDATE "
		  + tableName
		  + " SET TITLE = ?, COURTHOUSE_ID = ?, PATROL_TYPE = ?   WHERE NAME = ? AND PATROL_TYPE like 'PATROL%' AND COURTHOUSE_ID = 'CH0001'";
		break;
	    case 2:
		sql = "UPDATE "
		  + tableName
		  + " SET TITLE = ?, COURTHOUSE_ID = ?, PATROL_TYPE = ?   WHERE  PATROL_TYPE like 'PATROL%' AND NAME = ? AND COURTHOUSE_ID = 'CH0001'";
		break;
	    case 3:
		sql = "UPDATE "
		  + tableName
		  + " SET TITLE = ?, COURTHOUSE_ID = ?, PATROL_TYPE = ?   WHERE  PATROL_TYPE like 'PATROL%' AND COURTHOUSE_ID = 'CH0001' AND NAME = ?";
		break;

	    default:
		sql = "INVALID POSITION"; 
	}

        ps = connection_.prepareStatement(sql);

        for (int i = 1; i <= 100; i++) {
	  // Set every 10th title to null
	  if (i%10 == 0) {
	      ps.setString(1, null); 
	  } else { 
	      ps.setString(1, "TITLE" + i + "X");
	  }
	  ps.setString(2, "CH0002");
	  ps.setString(3, "PTXXX");
          ps.setString(4, "NAME"+i); 
          ps.addBatch();
	  if (i%20==0) {
	      executeBatchCount++; 
	      executeBatchInfo.append("calling batch "+executeBatchCount+" update entries\n"); 
	      ps.executeBatch();
	  }

        }


	// Verify updates were made
	sql = "select TITLE, NAME, BADGE_NUMBER, DATE_OF_SERVICE, PATROL_TYPE, HIRED_BY, OFFICER_ID, COURTHOUSE_ID FROM "+tableName+" ORDER BY OFFICER_ID";

	executeBatchInfo.append("Verify using query:  "+sql); 
	ResultSet rs = s.executeQuery(sql);
	int i = 1; 
	while (rs.next()) {
	    String string;
	    String expected;

	    string = rs.getString(1);
	    expected ="TITLE" + i + "X";

	    if (i%10 == 0) {
		if (string != null) {
		    executeBatchInfo.append("Row "+i+" TITLE:"+"Got '"+string+"' expected 'null'\n");
		    passed = false; 
		}
	    } else { 
		if ((string == null) || ! string.equals(expected)) {
		    executeBatchInfo.append("Row "+i+" TITLE:"+"Got '"+string+"' expected '"+expected+"'\n");
		    passed = false; 
		}
	    }

	    string = rs.getString(2);
	    expected ="NAME" + i ;
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" NAME:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(3);
	    expected = "" + i ;
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" BADGE NUMBER:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getDate(4).toString();
	    java.sql.Date ed = new java.sql.Date(i); 
	    expected = ed.toString(); 

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" DATE_OF_SERVICE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(5);
	    expected =  "PTXXX";
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" PATROL TYPE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(6);
	    if (i == 1) {
		expected = "1";
	    } else { 
		expected = ""+(i-1);
	    }
	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" HIRED BY:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }

	    string = rs.getString(7);
	    expected =  ""+i;

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" OFFICER ID:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }


	    string = rs.getString(8);
	    expected =  "CH0002";

	    if ((string == null) ||! string.equals(expected)) {
		executeBatchInfo.append("Row "+i+" COURTHOUSE:"+"Got '"+string+"' expected '"+expected+"'\n");
		passed = false; 
	    }



	    i++; 
	} 

		// Now delete the batch
		sql = "DELETE FROM " + tableName + " WHERE OFFICER_ID = ? AND COURTHOUSE_ID = 'CH0001'";
		ps = connection_.prepareStatement(sql);

	        for ( i = 1; i < 100; i++) {
	          ps.setString(1, "" + i);
	          ps.addBatch();
	        }


		executeBatchCount++; 

		executeBatchInfo.append("calling batch "+executeBatchCount+" delete entries\n");
	 ps.executeBatch();


	 sql = "DROP TABLE "+tableName;
	 executeBatchInfo.append(sql); 
	 s.executeUpdate(sql); 
	 s.close(); 




		assertCondition(passed, "Test failed\nInfo=\n"+executeBatchInfo.toString()); 

            }
            catch (Exception e) {

                failed (e, "Unexpected Exception -added by TB 6/22/09 for multirow update sql = "+sql+"\nInfo=\n"+executeBatchInfo.toString());

            }
	    try {
		connection_.commit(); 
	    } catch (Exception e) {
	    } 
        }
 
    } 



    public void Var082 ()
    {
	testNoLiteralInWherePosition(3, "Testing literal in first and second where position with multiple batches", 82);
    }
    public void Var083 ()
    {
	testNoLiteralInWherePosition(2, "Testing literal in first and third where position with multiple batches", 83);
    }
    public void Var084 ()
    {
	testNoLiteralInWherePosition(1, "Testing literal in second and third where position with multiple batches",84);
    }







    public String arrayToString(int [] a) { 
      StringBuffer sb = new StringBuffer(); 
      for (int i = 0; i < a.length; i++ ) {
        if (i > 0) sb.append(","); 
        sb.append(""+a[i]); 
      }
      return sb.toString(); 
    }






    /**
     * test what happens when a batch error occurs
     */ 

    public void testBatchError(String[][] testArray, String info) {
	if (getDriver() != JDTestDriver.DRIVER_NATIVE && useBlockInsert) {  notApplicable("Native block insert test");  return;	} 
        boolean passed = true; 
	String sql = "NONE";
	StringBuffer executeBatchInfo = new StringBuffer();
	executeBatchInfo.append(info);
	executeBatchInfo.append("\n"); 
	String tableName = "TSTBATERR";
	PreparedStatement pstmt = null;
	String [][] queryRow = null;
	boolean []  foundRow = null; 

	Connection c= null;
	Statement stmt = null; 
	try { 
	    c = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
	    stmt = c.createStatement(); 
	    for (int i = 0 ; i < testArray.length; i++) {

	         if (testArray[i][0].equalsIgnoreCase("DESCRIPTION"))  {
	           executeBatchInfo.append(testArray[i][1]+"\n"); 
	         } else if (testArray[i][0].equalsIgnoreCase("SETAUTOCOMMIT"))  {
	           if (testArray[i][1].equalsIgnoreCase("false")) {
	             c.setAutoCommit(false );
	           } else if (testArray[i][1].equalsIgnoreCase("true")) {
                     c.setAutoCommit(true );
	           } else {
	             String info2 = "ERROR:  did not recognize SETAUTOCOMMIT setting "+testArray[i][1]+"\n";
	             executeBatchInfo.append(info2); 
	             output_.print(info2); 
	           }

	           
	         }  else if (testArray[i][0].equalsIgnoreCase("SHOWSETTINGS"))  {
	            executeBatchInfo.append("isolation level(1=RU/2=RC/4=RR/8=S)="+c.getTransactionIsolation()+"\n"); 
	            executeBatchInfo.append("auotcommit="+c.getAutoCommit()+"\n"); 
	         } else if (testArray[i][0].equalsIgnoreCase("TABLENAME"))  {
		    sql = "Setting TABLENAME"; 
		    tableName =  JDPSTest.COLLECTION  +"." + testArray[i][1];
		    executeBatchInfo.append("Using tablename "+tableName+"\n");
		    sql = "DROP TABLE "+tableName;
		    try {
			stmt.executeUpdate(sql); 
		    } catch (Exception e) {
		    } 

		    sql = "CREATE TABLE "+tableName+" ( K INT PRIMARY KEY NOT NULL, V VARCHAR(80)) ";
		    stmt.executeUpdate(sql); 
		}  else	if (testArray[i][0].equalsIgnoreCase("INSERT"))  {
		    sql = "INSERT INTO "+tableName+" VALUES("+testArray[i][1]+",'"+testArray[i][2]+"')";
		    executeBatchInfo.append(sql+"\n"); 
 		    stmt.executeUpdate(sql);
		}  else	if (testArray[i][0].equalsIgnoreCase("PREPAREBATCH"))  {
		    sql = "INSERT INTO "+tableName+" VALUES(?,?)";
		    pstmt = c.prepareStatement(sql); 
		    executeBatchInfo.append(sql+"\n"); 
		}  else	if (testArray[i][0].equalsIgnoreCase("ADDBATCH"))  {
		    if (pstmt == null) { 
		       passed = false;
		       executeBatchInfo.append("FAILED:  testcase error, pstmt is False\n");
		    } else { 
            executeBatchInfo.append(sql + "\n");
            sql = "setString(1,...";
            pstmt.setString(1, testArray[i][1]);
            sql = "setString(2,...";
            pstmt.setString(2, testArray[i][2]);
            sql = "addBatch";
            pstmt.addBatch();
            executeBatchInfo.append("Add batch (" + testArray[i][1] + ","
                + testArray[i][2] + ")\n");
          }
		}  else	if (testArray[i][0].equalsIgnoreCase("EXECUTEBATCH"))  {
		    sql = "executeBatch";
		    executeBatchInfo.append(sql+"\n");
          if (pstmt == null) {
            passed = false;
            executeBatchInfo
                .append("FAILED:  testcase error, pstmt is False\n");
          } else {
            try {
              pstmt.executeBatch();
              if (!testArray[i][1].equals("NONE")) {
                passed = false;
                executeBatchInfo
                    .append("FAILED:  Did not get expected exception '"
                        + testArray[i][1] + "'");
              }
            } catch (Exception e) {
              String exMessage = e.toString();
              if (testArray[i][1].equals("NONE")) {
                passed = false;
                executeBatchInfo.append("FAILED: Unexpected exception '"
                    + exMessage + "'\n");
              } else {
                if (exMessage.indexOf(testArray[i][1]) >= 0) {
                  // Passed = true;
                } else {
                  executeBatchInfo.append("FAILED: Expected exception '"
                      + testArray[i][1] + "' got '" + exMessage + "'\n");
                  passed = false;
                }
                if (e instanceof BatchUpdateException) {
                  BatchUpdateException buEx = (BatchUpdateException) e;
                  int[] updateCounts = buEx.getUpdateCounts();
                  String updateCountString = arrayToString(updateCounts);
                  if (testArray[i].length < 3) {
                    passed = false;
                    executeBatchInfo
                        .append("FAILED: Got batch update exception but expected counts missing\n");
                  } else {
                    if (testArray[i][2].equals(updateCountString)) {
                      // passed
                    } else {
                      passed = false;
                      executeBatchInfo
                          .append("FAILED: Got exception updates of '"
                              + updateCountString + "' but expected '"
                              + testArray[i][2] + "'\n");
                    }
                  }
                }

              }
            }
          }
        } else	if (testArray[i][0].equalsIgnoreCase("QUERY"))  {
		    int rowCount =0 ; 
		    int expectedRowCount = Integer.parseInt(testArray[i][1]);
		    queryRow = new String[expectedRowCount][];
		    foundRow = new boolean[expectedRowCount]; 
		    sql = "select * from "+tableName+" order by K";
		    ResultSet rs = stmt.executeQuery(sql);
		    while (rs.next()) {
			if (rowCount < expectedRowCount) { 
			    queryRow[rowCount] = new String[2];
			    queryRow[rowCount][0] = rs.getString(1);
			    queryRow[rowCount][1] = rs.getString(2);
			    executeBatchInfo.append("Read row "+queryRow[rowCount][0]+","+queryRow[rowCount][1]+"\n"); 
			    rowCount++;
			} else {
			    passed = false;
			    executeBatchInfo.append("FAILED: Unexpected row "+rs.getString(1)+","+rs.getString(2)+"\n"); 
			} 
		    }
		    executeBatchInfo.append("QUERY read "+rowCount+" rows\n"); 
		    rs.close();

		}  else	if (testArray[i][0].equalsIgnoreCase("CHECK"))  {
		    boolean found = false;

		    if (queryRow == null ) {
			passed = false;
			executeBatchInfo.append("FAILED: For CHECK queryRow is null -- should be set by QUERY -- Does QUERY preceed CHECK? \n"); 
		    } else { 
          for (int j = 0; !found && j < queryRow.length; j++) {

              if (queryRow[j] != null && queryRow[j][0] != null
                  && queryRow[j][1] != null
                  && queryRow[j][0].equals(testArray[i][1])
                  && queryRow[j][1].equals(testArray[i][2])) {
                if (foundRow != null) {
                  foundRow[j] = true;
                }
                found = true;
              }
            }
			if (!found) {
			    passed = false;
			    executeBatchInfo.append("FAILED:Did not find row "+testArray[i][1]+","+testArray[i][2]+"\n");

			}
		    }
                }  else if (testArray[i][0].equalsIgnoreCase("EXTRA"))  {
                  // Just ignore 
		} else {
		   output_.println("FAILED:  did not recognize "+testArray[i][0]);
		   passed = false; 
		}

	    }

	    try {
	      c.commit(); 
	    } catch (Exception e) {
	      
	    }

	    sql = "DROP TABLE "+tableName;
	    executeBatchInfo.append(sql);
	    Statement s = c.createStatement(); 
	    s.executeUpdate(sql); 
	    s.close(); 

	    c.close();

	    assertCondition(passed, executeBatchInfo.toString());

	} catch (Exception e) {
	    failed (e, "Unexpected Exception -added 4/05/10 for update error case:  sql = "+sql+"\nInfo=\n"+executeBatchInfo.toString());

	} 

    } 


    public void Var085 ()
    {
	String[][] testString = {
	    /* for native JDBC setting duplicate key thrown.  First two updates made, but not subsequent */  
 /* 0 */     { "DESCRIPTION", "DEFAULT SETTINGS" }, 
 /* 1 */ 	    { "SHOWSETTINGS" }, 
 /* 2 */ 	    { "TABLENAME", "JDPSBAT85" },
 /* 3 */ 	    { "INSERT", "1", "V1"},
 /* 4 */ 	    { "PREPAREBATCH" },
 /* 5 */ 	    { "ADDBATCH", "2", "V2"},
 /* 6 */      { "ADDBATCH", "3", "V3"},
 /* 7 */ 	    { "ADDBATCH", "1", "X1"},
 /* 8 */ 	    { "ADDBATCH", "4", "V4"},
 /* 9 */ 	    { "EXECUTEBATCH", "Duplicate key value specified", "1,1"}, 
 /*10 */ 	    { "QUERY", "3" },
 /*11 */ 	    { "CHECK", "1", "V1" },
 /*12 */ 	    { "CHECK", "2", "V2" },
 /*13 */      { "CHECK", "3", "V3" },
 /*14 */      { "EXTRA", "X", "X" },
	};


	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	  // jcc executes as much as possible and throw non-atomic batch failure 
	  testString[9][1] = "Non-atomic batch failure";
	  testString[9][2] = "1,1,-3,1"; 
	  testString[10][1] = "4"; 
	  testString[14][1] = "4"; 
	  testString[14][2] = "V4"; 
	}
        if (isToolboxDriver()) {
          // toolbox returns SUCCESS_NO_INFO in update array */ 
          testString[9][2] = "-2,-2"; 
        }
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&
	    (getRelease() <= JDTestDriver.RELEASE_V7R2M0) &&
	    useBlockInsert) {
	    // Block insert does not correctly update counts in V5R4 and V6R1 and V7R1 and V7R2
            // and currently V7R2 (9/25/2013) 
	    testString[9][2] = ""; 
	} 
        
	testBatchError(testString, " -- native V7R2 needs to be enhanced/fixed");

    }


    /* Run with autocommit off */ 
    public void Var086 ()
    {
        String[][] testString = {
            /* for native JDBC setting duplicate key thrown.  First two update made, but not subsequent */  
 /* 0 */     { "DESCRIPTION", "AUTOCOMMIT FALSE" },
 /* 1 */     { "SETAUTOCOMMIT", "FALSE" },
 /* 2 */            { "SHOWSETTINGS" }, 
 /* 3 */            { "TABLENAME", "JDPSBAT85" },
 /* 4 */            { "INSERT", "1", "V1"},
 /* 5 */            { "PREPAREBATCH" },
 /* 6 */            { "ADDBATCH", "2", "V2"},
 /* 7 */            { "ADDBATCH", "3", "V3"},
 /* 8 */            { "ADDBATCH", "1", "X1"},
 /* 9 */            { "ADDBATCH", "4", "V4"},
 /*10 */            { "EXECUTEBATCH", "Duplicate key value specified", "1,1"}, 
 /*11 */            { "QUERY", "3" },
 /*12 */            { "CHECK", "1", "V1" },
 /*13 */            { "CHECK", "2", "V2" },
 /*14 */            { "CHECK", "3", "V3" },
 /*15 */            { "EXTRA", "X", "X" },
        };


        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          // jcc executes as much as possible and throw non-atomic batch failure 
          testString[10][1] = "Non-atomic batch failure";
          testString[10][2] = "1,1,-3,1"; 
          testString[11][1] = "4"; 
          testString[15][1] = "4"; 
          testString[15][2] = "V4"; 
        }
        if (isToolboxDriver()) {
	  // toolbox throws an exception and doesn't update anything 
          // toolbox returns SUCCESS_NO_INFO in update array */ 
          testString[10][2] = "";
	  testString[13][0] = "EXTRA";
	  testString[14][0] = "EXTRA"; 
        }


	if ((getDriver() == JDTestDriver.DRIVER_NATIVE) &&
	    (getRelease() <= JDTestDriver.RELEASE_V7R2M0) &&
	    useBlockInsert) {
	    // Block insert does not correctly update counts in V5R4 and V6R1 and V7R1
            // and currently V7R2 (9/23/2013) 
	    // Block insert also does not do any of the updateds 
	    testString[10][2] = "";
	    testString[13][0] = "EXTRA";
	    testString[14][0] = "EXTRA"; 
	} 


        testBatchError(testString, " -- native V7R2 needs to be enhanced/fixed");

    }










/**
addBatch()/executeBatch() - Execute the batch when the insert statement can insert more than 1 row

**/
    public void Var087 ()
    {

	if (getDriver() != JDTestDriver.DRIVER_NATIVE && useBlockInsert) {  notApplicable("Native block insert test");  return;	}

	String message = " -- New test 4/16/2010 to verify bach when insert statment inserts more than 1 row " ; 
	StringBuffer sb = new StringBuffer();
	if (checkJdbc20 ()) {
            try {
		String tablename=JDPSTest.COLLECTION + ".JDPSBAT87"; 
		Statement s = connection_.createStatement();

		try {
		    s.executeUpdate("drop table "+tablename); 
		} catch (Exception e) {
		} 
		s.executeUpdate("Create table "+tablename+" (c1 int)");

		PreparedStatement ps = connection_.prepareStatement ("insert into "+tablename+"  values ?,?,?");

		ps.setInt(1, 11);	
		ps.setInt(2, 12);
		ps.setInt(3, 13);
		ps.addBatch();


		ps.setInt(1, 21);	
		ps.setInt(2, 22);
		ps.setInt(3, 23);
		ps.addBatch();




                int[] updateCounts = ps.executeBatch ();
		ps.close();

		boolean success = true;
		Statement st = connection_.createStatement ();
		ResultSet rs = st.executeQuery ("SELECT * FROM " + tablename + " ORDER BY C1");
		int rows = 0;
		int expectedValues[] = { 11, 12, 13, 21, 22, 23 } ; 
		while (rs.next()) {
		    int value = rs.getInt(1);
		    
		    if (rows < expectedValues.length && value != expectedValues[rows]) {
			success = false;
			sb.append("Expected "+expectedValues[rows]+" but got "+value+"\n"); 
		    } 
		    rows++;
		} 
		rs.close();
                st.close ();		

		s.executeUpdate("drop table "+tablename); 
		s.close(); 
		if(updateCounts.length != 2
                       || updateCounts[0] != 3
                       || updateCounts[1] != 3
                       || rows != 6
		       || ! success	   )
                    {
                        String message2 = "updateCounts.length: " + updateCounts.length +
			  " updateCounts[0]: " + updateCounts[0] +
			  " updateCounts[1]: " + updateCounts[1] +
			  " rows: " + rows +
			  " success: "+success+message;
                        failed("Incorrect return information from executeBatch() "+message2);
                    }
                    else
                        succeeded();
                
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
addBatch()/executeBatch() - Execute the batch when the insert statement can insert more than 1 row

**/
    public void Var088 ()
    {
	if (getDriver() != JDTestDriver.DRIVER_NATIVE && useBlockInsert) {  notApplicable("Native block insert test");  return;	} 
	String message = " -- New test 4/16/2010 to verify bach when insert statment inserts more than 1 row " ; 
	StringBuffer sb = new StringBuffer();

        if (checkJdbc20 ()) {
            try {
		String tablename=JDPSTest.COLLECTION + ".JDPSBAT88"; 
		Statement s = connection_.createStatement();

		try {
		    s.executeUpdate("drop table "+tablename); 
		} catch (Exception e) {
		} 
		s.executeUpdate("Create table "+tablename+" (c1 int)");

		PreparedStatement ps = connection_.prepareStatement ("insert into "+tablename+"  values (?),(?),(?)");

		ps.setInt(1, 11);	
		ps.setInt(2, 12);
		ps.setInt(3, 13);
		ps.addBatch();


		ps.setInt(1, 21);	
		ps.setInt(2, 22);
		ps.setInt(3, 23);
		ps.addBatch();




                int[] updateCounts = ps.executeBatch ();
		ps.close();

		boolean success = true;
		Statement st = connection_.createStatement ();
		ResultSet rs = st.executeQuery ("SELECT * FROM " + tablename + " ORDER BY C1");
		int rows = 0;
		int expectedValues[] = { 11, 12, 13, 21, 22, 23 } ; 
		while (rs.next()) {
		    int value = rs.getInt(1);
		    
		    if (rows < expectedValues.length && value != expectedValues[rows]) {
			success = false;
			sb.append("Expected "+expectedValues[rows]+" but got "+value+"\n"); 
		    } 
		    rows++;
		} 
		rs.close();
                st.close ();		

		s.executeUpdate("drop table "+tablename); 
		s.close(); 

		if(updateCounts.length != 2
                       || updateCounts[0] != 3
                       || updateCounts[1] != 3
                       || rows != 6
		       || ! success	   )
                    {
                        String message2 = "updateCounts.length: " + updateCounts.length +
			  " updateCounts[0]: " + updateCounts[0] +
			  " updateCounts[1]: " + updateCounts[1] +
			  " rows: " + rows +
			  " success: "+success+
			  message ;
                        failed("Incorrect return information from executeBatch() "+message2);
                    }
                    else
                        succeeded();
                
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
addBatch()/executeBatch() - Execute the batch when the insert statement can insert more than 1 row

**/
    public void Var089 ()
    {
	if (getDriver() != JDTestDriver.DRIVER_NATIVE && useBlockInsert) {  notApplicable("Native block insert test");  return;	} 
	boolean success = true; 
	String message = " -- New test 4/16/2010 to verify bach when insert statment inserts more than 1 row " ; 
	StringBuffer sb = new StringBuffer();

        if (checkJdbc20 ()) {
            try {
		String tablename1=JDPSTest.COLLECTION + ".JDPSBAT88A";
		String tablename2=JDPSTest.COLLECTION + ".JDPSBAT88B"; 
		Statement s = connection_.createStatement();
		PreparedStatement ps; 
		try {
		    s.executeUpdate("drop table "+tablename1); 
		} catch (Exception e) {
		}
		try {
		    s.executeUpdate("drop table "+tablename2); 
		} catch (Exception e) {
		} 

		s.executeUpdate("Create table "+tablename1+" (c1 int)");
		s.executeUpdate("Create table "+tablename2+" (c1 int)");

		ps = connection_.prepareStatement ("insert into "+tablename1+"  values (?)");
		for (int i = 0; i < 100; i++) {
		    ps.setInt(1, i);
		    ps.addBatch(); 
		} 
                int[] updateCounts = ps.executeBatch ();
		if (updateCounts.length != 100) {
		    success = false;
		    sb.append("initial update failed because updateCount.length = "+updateCounts.length+" sb 100\n"); 
		} else { 
		    for (int i = 0; i < 100; i++) {
			if (updateCounts[i] != 1) {
			    success =false; 
			    sb.append("initial update failed because updateCount["+i+"] = "+updateCounts[i]+" sb 10\n"); 
			} 
		    }
		}
		ps.close();

		ps = connection_.prepareStatement ("insert into "+tablename2+" (select c1 from "+tablename1+" where c1>= ?  AND c1<=?) ");

		ps.setInt(1,11);
		ps.setInt(2,11);
		ps.addBatch(); 

		ps.setInt(1,21);
		ps.setInt(2,22);
		ps.addBatch(); 

		ps.setInt(1,31);
		ps.setInt(2,33);
		ps.addBatch(); 


		ps.setInt(1,41);
		ps.setInt(2,44);
		ps.addBatch(); 


                updateCounts = ps.executeBatch ();

		if (updateCounts.length != 4) {
		    success = false;
		    sb.append("second update failed because updateCount.length = "+updateCounts.length+" sb 100\n"); 
		} else { 
		    int expectedUpdateCounts[] = {1,2,3,4};
		    for (int i = 0; i < expectedUpdateCounts.length; i++) {
			if (expectedUpdateCounts[i] != updateCounts[i]) {
			    success = false;
			    sb.append("second updateCount["+i+"] = "+updateCounts[i]+" sb "+expectedUpdateCounts[i]+"\n"); 
			} 
		    } 

		}

		Statement st = connection_.createStatement ();
		ResultSet rs = st.executeQuery ("SELECT * FROM " + tablename2 + " ORDER BY C1");
		int rows = 0;
		int expectedValues[] = { 11, 21, 22, 31, 32, 33, 41, 42, 43, 44 } ; 
		while (rs.next()) {
		    int value = rs.getInt(1);
		    
		    if (rows < expectedValues.length && value != expectedValues[rows]) {
			success = false;
			sb.append("Expected "+expectedValues[rows]+" but got "+value+"\n"); 
		    } 
		    rows++;
		} 
		rs.close();
                st.close ();		

		s.executeUpdate("drop table "+tablename1); 
                s.executeUpdate("drop table "+tablename2); 
		s.close(); 

		if( ! success	   )
                    {
                        failed(sb.toString()+message);
                    }
                    else
                        succeeded();
                
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


    
    /**
    Adds a statement using several characters.  The goal is to overflow the 16 M boundary 
    for data and use the maximum block input rows property to avoid the problem. 
    **/
        public void Var090()
        {


	if (getDriver() != JDTestDriver.DRIVER_NATIVE && useBlockInsert) {  notApplicable("Native block insert test");  return;	}

	if (getDriver() == JDTestDriver.DRIVER_NATIVE && !useBlockInsert) {  notApplicable("Native block insert test");  return;	}


	    String jvmName = System.getProperty("java.vm.name");
	    if (jvmName.indexOf("Classic") >=  0) { 
		String classpath = System.getProperty("java.class.path");
		if (classpath.indexOf("jt400native.jar") >= 0) {
		    notApplicable("Fails with OOM error for Classic JVM and jt400native.jar");
		    return; 
		} 
	    }


          String url = baseURL_; 
	  String varInfo = " -- Testing maximum blocked input rows"; 
            
            StringBuffer messageBuffer = new StringBuffer(); 
	    output_.println("Driver fix level is "+getDriverFixLevel()); 
	    try {
        Connection connection =  testDriver_.getConnection (url+";maximum blocked input rows=1000", userId_, encryptedPassword_);
          String tablename = JDPSTest.COLLECTION+ ".JDPSBAT90";
          Statement stmt = connection.createStatement();
          try {
              stmt.executeUpdate("DROP TABLE "+tablename);
          } catch (Exception e) {
          } 

          stmt.executeUpdate("CREATE TABLE "+tablename+" ( c1 char(1000), "+
                                                       "   c2 char(1000), "+
                                                       "   c3 char(1000), "+
                                                       "   c4 char(1000), "+
                                                       "   c5 char(1000), "+
                                                       "   c6 char(1000), "+
                                                       "   c7 char(1000), "+
                                                       "   c8 char(1000), "+
                                                       "   c9 char(1000), "+
                                                       "   c10 char(1000), "+
                                                       "   c11 char(1000), "+
                                                       "   c12 char(1000), "+
                                                       "   c13 char(1000), "+
                                                       "   c14 char(1000), "+
                                                       "   c15 char(1000), "+
                                                       "   c16 char(1000))");

          //
          // Now create a batch with 10000 rows. This should exceed the
          // 16 Meg limit
          //  10000 * 16 columns / row  * 1000 bytes / column = 160,0000,000
          //
          // When running on toolbox without changing the batch size, this fails
          // with an out of memory error during execute... when reallocating a buffer 
          // Setting the maximum blocked input rows to 1000 prevents this problem. 
          //
  // Note:  When running using J9 on native, there is a 200 meg limit for
  // allocating parm space.
  //

  int rowCount = 10000; 

  // If running on windows, 10000 rows is too big..
  if (JTOpenTestEnvironment.isWindows) {
rowCount=2000;
  } else if (JTOpenTestEnvironment.isLinux) {
// rowCount is good 
  } else if (JTOpenTestEnvironment.isOS400) {
    // Check if 32-bit os.. If so,
             // Then use smaller row count to avoid overflowing
             // the 200 meg limit for parameter space. 
             String bitmode = System.getProperty("com.ibm.vm.bitmode");
    if (bitmode != null && bitmode.equals("32")) {
  rowCount=5000;
    }

  } else {
output_.println("OS is "+JTOpenTestEnvironment.osVersion); 
  } 

          PreparedStatement pStmt = connection.prepareStatement("INSERT INTO "+tablename+
              " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

          for (int i = 1; i <= rowCount; i++) {
            for (int j = 1;  j <= 16; j++) { 
              pStmt.setString(j, "row "+i+" column "+j); 
            }
            pStmt.addBatch(); 
          }
          // output_.println(); 
          int updateCounts[]  = pStmt.executeBatch ();

          ResultSet rs = stmt.executeQuery("Select count (*) from "+tablename);
          rs.next();
          long answer = rs.getLong(1);
          output_.println("The answer is "+answer); 
          rs.close(); 
          
          messageBuffer.append("Length of updateCounts is "+updateCounts.length+"\n");
          output_.println(messageBuffer.toString());

          pStmt.close();

  stmt.executeUpdate("DROP TABLE "+tablename);
          stmt.close();

          connection.close(); 
          
          assertCondition(answer==rowCount && updateCounts.length == rowCount, 
              "answer="+answer+" sb "+rowCount+"  updateCounts.length="+updateCounts.length+" sb "+rowCount+" "+
                messageBuffer.toString()+varInfo);

      } catch (Exception e) {
          failed (e, "Unexpected Exception:  Added 3/05/2010 "+messageBuffer.toString() +varInfo);
      } 

        }



    
    /**
    Adds a statement using several characters.  The goal is to overflow the 16 M boundary 
    for data by calculating a value lower than the maximum block input rows property to avoid the problem. 
    **/
        public void Var091()
        {
	if (getDriver() != JDTestDriver.DRIVER_NATIVE && useBlockInsert) {  notApplicable("Native block insert test");  return;	} 
	if (getDriver() == JDTestDriver.DRIVER_NATIVE && !useBlockInsert) {  notApplicable("Native block insert test");  return;	}

	    String jvmName = System.getProperty("java.vm.name");
	    if (jvmName.indexOf("Classic") >=  0) { 
		String classpath = System.getProperty("java.class.path");
		if (classpath.indexOf("jt400native.jar") >= 0) {
		    notApplicable("Fails with OOM error for Classic JVM and jt400native.jar");
		    return; 
		} 
	    }


          String url = baseURL_; 
	  String varInfo = " -- Testing maximum blocked input rows"; 
            
            StringBuffer messageBuffer = new StringBuffer(); 
	    output_.println("Driver fix level is "+getDriverFixLevel()); 
	    String tablename = JDPSTest.COLLECTION+ ".JDPSBAT91";
      Connection connection = null ;
      Statement stmt = null; 
                  try {
          connection = testDriver_.getConnection (url+";maximum blocked input rows=4000", userId_, encryptedPassword_);

                      stmt = connection.createStatement();
                      try {
                          stmt.executeUpdate("DROP TABLE "+tablename);
                      } catch (Exception e) {
                      } 

                      stmt.executeUpdate("CREATE TABLE "+tablename+" ( c1 char(1000), "+
                                                                   "   c2 char(1000), "+
                                                                   "   c3 char(1000), "+
                                                                   "   c4 char(1000), "+
                                                                   "   c5 char(1000), "+
                                                                   "   c6 char(1000), "+
                                                                   "   c7 char(1000), "+
                                                                   "   c8 char(1000), "+
                                                                   "   c9 char(1000), "+
                                                                   "   c10 char(1000), "+
                                                                   "   c11 char(1000), "+
                                                                   "   c12 char(1000), "+
                                                                   "   c13 char(1000), "+
                                                                   "   c14 char(1000), "+
                                                                   "   c15 char(1000), "+
                                                                   "   c16 char(1000), "+
                                                                   "   c17 char(1000), "+
                                                                   "   c18 char(1000), "+
                                                                   "   c19 char(1000), "+
                                                                   "   c20 char(1000), "+
                                                                   "   c21 char(1000), "+
                                                                   "   c22 char(1000), "+
                                                                   "   c23 char(1000), "+
                                                                   "   c24 char(1000), "+
                                                                   "   c25 char(1000), "+
                                                                   "   c26 char(1000), "+
                                                                   "   c27 char(1000), "+
                                                                   "   c28 char(1000), "+
                                                                   "   c29 char(1000), "+
                                                                   "   c30 char(1000))");

          int rowCount = 6000; 
                      //
                      // Now create a batch with 6000 rows. This should exceed the
                      // 16 Meg limit
                      //  6000 * 30 columns / row  * 1000 bytes / column = 300,0000,000
                      //
                      // When running on toolbox without changing the batch size, this fails
                      // with an out of memory error during execute... when reallocating a buffer 
                      // Setting the maximum blocked input rows to 1000 prevents this problem. 
                      //
          // Note:  When running using J9 on native, there is a 200 meg limit for
          // allocating parm space.
          //


          // If running on windows, 10000 rows is too big..
          if (JTOpenTestEnvironment.isWindows ||
              JTOpenTestEnvironment.isLinux) {
      	rowCount=1000;
          } else if (JTOpenTestEnvironment.isOS400) {
             // Check if 32-bit os.. If so,
                         // Then use smaller row count to avoid overflowing
                         // the 200 meg limit for parameter space. 
                         String bitmode = System.getProperty("com.ibm.vm.bitmode");
             if (bitmode != null && bitmode.equals("32")) {
      	   rowCount=1000;
             }
          } else {
      	output_.println("OS is "+JTOpenTestEnvironment.osVersion); 
          } 


                      PreparedStatement pStmt = connection.prepareStatement("INSERT INTO "+tablename+
                          " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

                      for (int i = 1; i <= rowCount; i++) {
                        for (int j = 1;  j <= 30; j++) { 
                          pStmt.setString(j, "row "+i+" column "+j); 
                        }
                        pStmt.addBatch(); 
                      }
                      // output_.println(); 
                      int updateCounts[]  = pStmt.executeBatch ();

                      ResultSet rs = stmt.executeQuery("Select count (*) from "+tablename);
                      rs.next();
                      long answer = rs.getLong(1);
                      output_.println("The answer is "+answer); 
                      rs.close(); 
                      
                      messageBuffer.append("Length of updateCounts is "+updateCounts.length+"\n");
                      output_.println(messageBuffer.toString());

          try {
      	stmt.executeUpdate("DROP TABLE "+tablename);
          } catch (Exception e) {
          } 


                      stmt.close();
          stmt=null; 
                      pStmt.close();
          pStmt = null; 

                      connection.close();
          connection = null; 
                      
                      assertCondition(answer==rowCount && updateCounts.length == rowCount, 
                          "answer="+answer+" sb "+rowCount+"  updateCounts.length="+updateCounts.length+" sb "+rowCount+" "+
                            messageBuffer.toString()+varInfo);

                  } catch (Exception e) {
                      failed (e, "Unexpected Exception:  Added 7/27/2010 "+messageBuffer.toString() +varInfo);
      } finally {
          try {
      	if (stmt != null ) { 
      	    stmt.executeUpdate("DROP TABLE "+tablename);
      	    stmt.close();
      	    stmt = null; 
      	}
      	if (connection != null) {
      	    connection.close();
      	    connection = null; 
      	} 

          } catch (Exception e) {
      	output_.println("Warning:  Exception on testcase cleanup "+e);
      	e.printStackTrace(output_); 
          } 
      } 

        }


  /**
   * Use a timezone not valid in the current timezone with execute batch.
   **/
  public void Var092() {
    StringBuffer sb = new StringBuffer(
        " New Testcases for timestamp now valid in current timezone\n");
    Connection connection = null;
    Statement stmt = null;
    String tablename = JDPSTest.COLLECTION + ".JDPSBAT92";
    try {
      boolean passed = true;
      String sql;

      connection = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);

      stmt = connection.createStatement();
      try {
        sql = "DROP TABLE " + tablename;
        sb.append("Executing " + sql + "\n");
        stmt.executeUpdate(sql);
      } catch (Exception e) {
        printStackTraceToStringBuffer(e, sb);

      }
      sql = "CREATE TABLE " + tablename + " (c1 timestamp)";
      sb.append("Executing " + sql + "\n");
      stmt.executeUpdate(sql);

      sb.append("Preparing " + sql + "\n");
      sql = "insert into " + tablename + " values(?)";

      String[] values = { 
          "2016-03-13 02:15:00.000000", 
          "2016-03-13 03:15:00.000000", 
          "2016-03-13 01:15:00.000000",
          "2016-03-13 07:12:00.000000",
          "2016-11-06 02:12:00.000000",
          "2015-03-08 02:15:00.000000", 
          "2015-11-01 02:15:00.000000", 
          "2014-03-09 02:15:00.000000",  
          "2014-11-02 02:15:00.000000",  
          "2013-03-10 02:15:00.000000",  
          "2013-11-03 02:15:00.000000",  
          
          
          };
      PreparedStatement pStmt = connection.prepareStatement(sql);

      for (int i = 0; i < values.length; i++) {
        sb.append("Adding batch with " + values[i] + "\n");
        pStmt.setString(1, values[i]);
        pStmt.addBatch();
      }
      // output_.println();
      int updateCounts[] = pStmt.executeBatch();

      ResultSet rs = stmt.executeQuery("Select *  from " + tablename);
      int rowNumber = 0;
      while (rs.next()) {
        String answer = rs.getString(1);
        if (!values[rowNumber].equals(answer)) {
          sb.append("FAILED  Expected " + values[rowNumber] + " got " + answer
              + " updateCounts.length="+updateCounts.length+"\n");
          passed = false;
        }
        rowNumber++;
      }

      stmt.executeUpdate("DROP TABLE " + tablename);
      stmt.close();
      stmt = null;
      pStmt.close();
      pStmt = null;

      connection.close();
      connection = null;

      assertCondition(passed,sb);

    } catch (Exception e) {
      failed(e, "Unexpected Exception:  " + sb.toString());

    } finally {
      try {
        if (stmt != null) {
          stmt.executeUpdate("DROP TABLE " + tablename);
          stmt.close();
          stmt = null;
        }
        if (connection != null) {
          connection.close();
          connection = null;
        }

      } catch (Exception e) {
        output_.println("Warning:  Exception on testcase cleanup " + e);
        e.printStackTrace(output_);
      }
    }

  }




  /**
   * Use a GRAPHIC 835 with execute batch.
   **/
  public void Var093() {
    StringBuffer sb = new StringBuffer(
        " New Testcases for gaphic 835 in execute batch\n"); 
    Connection connection = null;
    Statement stmt = null;
    String tablename = JDPSTest.COLLECTION + ".JDPSBAT93";
    try {
      boolean passed = true;
      String sql;

      connection = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);

      stmt = connection.createStatement();
      try {
        sql = "DROP TABLE " + tablename;
        sb.append("Executing " + sql + "\n");
        stmt.executeUpdate(sql);
      } catch (Exception e) {
        printStackTraceToStringBuffer(e, sb);

      }
      sql = "CREATE TABLE " + tablename + " (c1 graphic(10) ccsid 835)";
      sb.append("Executing " + sql + "\n");
      stmt.executeUpdate(sql);

      sb.append("Preparing " + sql + "\n");
      sql = "insert into " + tablename + " values(?)";

      String[][] values = {
	  { "\uFF10", "\uFF10\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000"},
	  { "\uFF10", "\uFF10\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000"},
	  { "\uFF10", "\uFF10\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000"},
	  { "\uFF10", "\uFF10\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000"},
      };
      PreparedStatement pStmt = connection.prepareStatement(sql);

      for (int i = 0; i < values.length; i++) {
        sb.append("Adding batch with " + values[i][0] + "\n");
        pStmt.setString(1, values[i][0]);
        pStmt.addBatch();
      }
      // output_.println();
      int updateCounts[] = pStmt.executeBatch();

      ResultSet rs = stmt.executeQuery("Select *  from " + tablename);
      int rowNumber = 0;
      while (rs.next()) {
        String answer = rs.getString(1);
        if (!values[rowNumber][1].equals(answer)) {
          sb.append("FAILED  Expected " + JDTestUtilities.getMixedString(values[rowNumber][1]) + " got " + JDTestUtilities.getMixedString(answer)
              + "updateCounts.length="+updateCounts.length+"\n");
          passed = false;
        }
        rowNumber++;
      }

      stmt.executeUpdate("DROP TABLE " + tablename);
      stmt.close();
      stmt = null;
      pStmt.close();
      pStmt = null;

      connection.close();
      connection = null;

      assertCondition(passed,sb);

    } catch (Exception e) {
      failed(e, "Unexpected Exception:  " + sb.toString());

    } finally {
      try {
        if (stmt != null) {
          stmt.executeUpdate("DROP TABLE " + tablename);
          stmt.close();
          stmt = null;
        }
        if (connection != null) {
          connection.close();
          connection = null;
        }

      } catch (Exception e) {
        output_.println("Warning:  Exception on testcase cleanup " + e);
        e.printStackTrace(output_);
      }
    }

  }


  /**
   * Correct detect errors from batch
   **/
  public void Var094() {
    StringBuffer sb = new StringBuffer(
        " 2018/08/01 Correct detect errors from batch\n");
    Connection connection = null;
    Statement stmt = null;
    try {
      boolean passed = true;

      connection = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
      stmt = connection.createStatement();
      connection.setAutoCommit(false);
      stmt.executeUpdate("CREATE TABLE QTEMP.JSONDATA(C1 VARBINARY(20), C2 BLOB)");
      connection.commit();
      PreparedStatement pstmt = connection
          .prepareStatement("insert into QTEMP.JSONDATA VALUES(?,?)");
      byte[] bytes1 = { 0x11 };
      pstmt.setBytes(1, bytes1);
      pstmt.setBytes(2, bytes1);
      pstmt.addBatch();
      byte[] bytes2 = { 0x22 };
      pstmt.setBytes(1, bytes2);
      pstmt.setBytes(2, bytes2);
      pstmt.addBatch();

      try {
        pstmt.executeBatch();
        passed = false;
        sb.append("Did not get exception from executeBatch\n");
      } catch (Exception e) {
        String message = e.toString();
        String expected = "not valid for operation";
        if (message.indexOf(expected) < 0) {
          passed = false;
          sb.append("Got exception " + message + " expected " + expected + "\n");
          printStackTraceToStringBuffer(e, sb);
        }
      }

      assertCondition(passed,sb);

    } catch (Exception e) {
      failed(e, "Unexpected Exception:  " + sb.toString());

    } finally { 
      if (connection != null) {  
        try {
          connection.close();
        } catch (SQLException e) {
          output_.println("Warning -- exception on cleanup "); 
          e.printStackTrace(output_);
        } 
      }
        
    }

  }


  public void Var095() {
    StringBuffer sb = new StringBuffer(
        " 2018/08/01 Correctly detect errors from batch\n");
    Connection connection = null;
    Statement stmt = null;
    try {
      boolean passed = true;

      connection = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
      stmt = connection.createStatement();
      connection.setAutoCommit(false);
      String tableName = JDPSTest.COLLECTION + ".JDPSBAT95";
      try {
        stmt.executeUpdate("DROP TABLE "+tableName); 
      } catch (Exception e) {} 
      
      stmt.executeUpdate("CREATE TABLE "+tableName+"(C1 SMALLINT)");
      connection.commit();
      PreparedStatement pstmt = connection
          .prepareStatement("insert into "+tableName+" VALUES(CAST ( ? AS INTEGER))");
      
      
      sb.append("Testing bad insert at end\n"); 
      pstmt.setInt(1, 1);
      pstmt.addBatch();
      pstmt.setInt(1, 2);
      pstmt.addBatch();
      pstmt.setInt(1, 1000000);
      pstmt.addBatch();
      
      try {
        pstmt.executeBatch();
        passed = false;
        sb.append("Did not get exception from executeBatch\n");
      } catch (Exception e) {
        String message = e.toString();
        String expected = "Conversion error";
        if (message.indexOf(expected) < 0) {
          passed = false;
          sb.append("Got exception " + message + " expected " + expected + "\n");
          printStackTraceToStringBuffer(e, sb);
        }
      }


      sb.append("Testing bad insert in middle\n"); 
      
      pstmt.setInt(1, 11);
      pstmt.addBatch();
      pstmt.setInt(1, 12);
      pstmt.addBatch();
      pstmt.setInt(1, 1000000);
      pstmt.addBatch();
      pstmt.setInt(1, 13);
      pstmt.addBatch();
      pstmt.setInt(1, 14);
      pstmt.addBatch();
      pstmt.setInt(1, 15);
      pstmt.addBatch();
      pstmt.setInt(1, 16);
      pstmt.addBatch();
      pstmt.setInt(1, 17);
      pstmt.addBatch();
      
      try {
        pstmt.executeBatch();
        passed = false;
        sb.append("Did not get exception from executeBatch\n");
      } catch (Exception e) {
        String message = e.toString();
        String expected = "Conversion error";
        if (message.indexOf(expected) < 0) {
          passed = false;
          sb.append("Got exception " + message + " expected " + expected + "\n");
          printStackTraceToStringBuffer(e, sb);
        }
      }


      sb.append("Testing bad insert at beginning\n"); 

      pstmt.setInt(1, 1000000);
      pstmt.addBatch();
      
      for (int i = 1000; i < 2000; i++) { 
        pstmt.setInt(1, i);
        pstmt.addBatch();
      }
      
      try {
        pstmt.executeBatch();
        passed = false;
        sb.append("Did not get exception from executeBatch\n");
      } catch (Exception e) {
        String message = e.toString();
        String expected = "Conversion error";
        if (message.indexOf(expected) < 0) {
          passed = false;
          sb.append("Got exception " + message + " expected " + expected + "\n");
          printStackTraceToStringBuffer(e, sb);
        }
      }


      sb.append("Testing bad insert at end of long seqence\n"); 

      
      for (int i = 2000; i < 40000; i++) { 
        pstmt.setInt(1, i % 30000);
        pstmt.addBatch();
      }
      pstmt.setInt(1, 1000000);
      pstmt.addBatch();
      
      try {
        pstmt.executeBatch();
        passed = false;
        sb.append("Did not get exception from executeBatch\n");
      } catch (Exception e) {
        String message = e.toString();
        String expected = "Conversion error";
        if (message.indexOf(expected) < 0) {
          passed = false;
          sb.append("Got exception " + message + " expected " + expected + "\n");
          printStackTraceToStringBuffer(e, sb);
        }
      }


      

      
      
      
      
      stmt.executeUpdate("DROP TABLE "+tableName); 
      
      
      assertCondition(passed,sb);

    } catch (Exception e) {
      failed(e, "Unexpected Exception:  " + sb.toString());

    } finally { 
      if (connection != null) {  
        try {
          connection.close();
        } catch (SQLException e) {
          output_.println("Warning -- exception on cleanup "); 
          e.printStackTrace(output_);
        } 
      }
        
    }

  }

  
  /**
Adds a statement using a boolean to a batch
**/
    public void Var096()    // @L2
    {
  StringBuffer messageBuffer = new StringBuffer(); 
        if (checkBooleanSupport ())
        {
            try
      {
                PreparedStatement ps = connection_.prepareStatement ( insertBoolean_ );

    int len = 5;
                for (int i = 0; i<len; i++)
        {
          ps.setString(1, "Los Angeles" + Integer.toString(i));
          ps.setBoolean(2, i % 2 == 1);
          ps.addBatch();
        }
        int updateCounts[] = ps.executeBatch();
        ps.close();

        Statement s = connection_.createStatement();

        ResultSet rs = s
            .executeQuery("SELECT COL1, COL2  FROM " + tableBoolean_);
        boolean success = true;
        int count = 0;
        while (rs.next()) {
          String tmp = rs.getString(1);
          String res = "Los Angeles" + Integer.toString(count);
          boolean outBoolean = rs.getBoolean(2);
          boolean expBoolean = (count % 2 == 1);
          messageBuffer.append("" + (count + 1) + ". " + tmp + " sb " + res
              + " boolean=" + outBoolean + "(sb " + expBoolean + "\n");
          if (tmp.indexOf(res) != 0)
            success = false;
          if (outBoolean != expBoolean) 
              success = false; 
          count++;

        }
        rs.close();
        s.close();
        assertCondition(updateCounts.length == len && success,
            "updateCounts.length = " // @L2
                + updateCounts.length + " SB " + len + " ; success = " + success
                + " SB true " + messageBuffer.toString());// @L2
      } catch (Exception e) {
        failed(e, "Unexpected Exception:  Added by Native 8/20/03 "
            + messageBuffer.toString());
      }
    }
  }
  
  
  
}
