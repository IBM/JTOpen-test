///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDStatementDelimiters.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDStatementDelimiters.java
//
// Classes:      JDStatementDelimiters
//
////////////////////////////////////////////////////////////////////////

package test.JD.Statement;

import java.sql.*;

import java.io.FileOutputStream;
import java.util.Hashtable;
import com.ibm.as400.access.AS400;

import test.JDStatementTest;
import test.JDTestDriver;
import test.JDTestcase;
import test.JD.JDSetupCollection;

/**
 Testcase JDDelimiters.  This tests the use of delimiters in statements.
 **/
public class JDStatementDelimiters extends JDTestcase {

  String collection1_ = "\"JDDelim\""; //collection with a space in the name
  String collection1NoDelim_ = "JDDelim";

  String collection2_ = "JDDELIM"; //normal characters in collection name

  String collection3_ = "\"JDDELIM%\""; //collection with non-normal character
  String collection3NoDelim_ = "JDDELIM%";

  String collection4_ = "\"JDDe.lim\""; //test with '.'          @A1A

  String table1_ = "MYTABLE";

  String table2_ = "\"My Table\"";

  String table3_ = "\"My%$Table\"";

  String table4_ = "\"My.Table\""; //test with '.'          @A1A

  String proc1_ = "MYPROC";

  String proc2_ = "\"My Proc\"";

  String proc3_ = "\"My%Proc\"";

  String proc4_ = "\"My'Proc\"";

  String proc5_ = "\"My.Proc\""; //test with '.'          @A1A

  Connection c;

  Connection c1;

  DatabaseMetaData dmd;

  /**
   Constructor.
   **/
  public JDStatementDelimiters(AS400 systemObject, Hashtable namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDStatementDelimiters", namesAndVars, runMode,
        fileOutputStream, password);
  }

  /**
   Setup.

   @exception Exception If an exception occurs.
   **/
  protected void setup() throws Exception {
    c = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      // jcc doesn't have extended metadata property 
      c1 = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
     
    } else { 
      c1 = testDriver_.getConnection(baseURL_ + ";extended metadata=true",
        userId_, encryptedPassword_);
    }
    dmd = c.getMetaData();
    //
    // Rename collections to be specific to this testcase
    //
    String suffix = JDStatementTest.COLLECTION.substring(JDStatementTest.COLLECTION.length() - 3);


  collection1_ = "\"JDDe"+suffix+"\""; //collection with a space in the name
  collection1NoDelim_ = "JDDe"+suffix; 
  collection2_ = "JDDE"+suffix; //normal characters in collection name

  collection3_ = "\"JDDE"+suffix+"%\""; //collection with non-normal character
  collection3NoDelim_ = "JDDE"+suffix+"%"; //collection with non-normal character

  collection4_ = "\"JDDe."+suffix+"\""; //test with '.'          @A1A

    //Create the collections
    JDSetupCollection.create(systemObject_,  c, collection1_);
    JDSetupCollection.create(systemObject_,  c, collection2_);
    JDSetupCollection.create(systemObject_,  c, collection3_);
    //Test '.'     @A1
    JDSetupCollection.create(systemObject_,  c, collection4_);

    // Create the tables.
    createTable(collection1_, table1_);
    createTable(collection1_, table2_);
    createTable(collection1_, table3_);
    createTable(collection2_, table1_);
    createTable(collection2_, table2_);
    createTable(collection2_, table3_);
    createTable(collection3_, table1_);
    createTable(collection3_, table2_);
    createTable(collection3_, table3_);

    // Create the procedures
    createProcedure(collection1_, proc1_);
    createProcedure(collection1_, proc2_);
    createProcedure(collection1_, proc3_);
    createProcedure(collection1_, proc4_);
    createProcedure(collection2_, proc1_);
    createProcedure(collection2_, proc2_);
    createProcedure(collection2_, proc3_);
    createProcedure(collection2_, proc4_);
    createProcedure(collection3_, proc1_);
    createProcedure(collection3_, proc2_);
    createProcedure(collection3_, proc3_);
    createProcedure(collection3_, proc4_);
    createProcedure(collection4_, proc5_); //test '.'     @A1

    /*if (collection_ != null) {   // @C1A
     JDSetupProcedure.create (systemObject_, c, JDSetupProcedure.STP_CSPARMS,
     false, false, collection_);
     }
     else {
     JDSetupProcedure.create (systemObject_, c, JDSetupProcedure.STP_CSPARMS);
     }
     */

  }

  /**
   Cleanup.

   @exception Exception If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    // Drop the tables.
    dropTable(collection1_, table1_);
    dropTable(collection1_, table2_);
    dropTable(collection1_, table3_);
    dropTable(collection2_, table1_);
    dropTable(collection2_, table2_);
    dropTable(collection2_, table3_);
    dropTable(collection3_, table1_);
    dropTable(collection3_, table2_);
    dropTable(collection3_, table3_);

    // Drop the procedures
    dropProcedure(collection1_, proc1_);
    dropProcedure(collection1_, proc2_);
    dropProcedure(collection1_, proc3_);
    dropProcedure(collection1_, proc4_);
    dropProcedure(collection2_, proc1_);
    dropProcedure(collection2_, proc2_);
    dropProcedure(collection2_, proc3_);
    dropProcedure(collection2_, proc4_);
    dropProcedure(collection3_, proc1_);
    dropProcedure(collection3_, proc2_);
    dropProcedure(collection3_, proc3_);
    dropProcedure(collection3_, proc4_);
    // Test '.' @A1A
    dropProcedure(collection4_, proc5_);

    c.close();
    c1.close();
  }

  void createTable(String collection, String table) throws SQLException {
    DatabaseMetaData dmd = c.getMetaData();
    Statement s = c.createStatement();
    s
        .execute("CREATE TABLE "
            + collection
            + dmd.getCatalogSeparator()
            + table
            + " (MYCOLUMN INTEGER, \"My Column\" INTEGER, \"MYCO%LUMN\" INTEGER, \"Yes, \"\"My Column\"\"\" INTEGER)");
    s.executeUpdate("INSERT INTO " + collection + dmd.getCatalogSeparator()
        + table + " VALUES(1, 2, 3, 4)");
    s.executeUpdate("INSERT INTO " + collection + dmd.getCatalogSeparator()
        + table + " VALUES(5, 6, 7, 8)");
    s.close();
  }

  void dropTable(String collection, String table) throws SQLException {
    DatabaseMetaData dmd = c.getMetaData();
    Statement s = c.createStatement();
    s.execute("DROP TABLE " + collection + dmd.getCatalogSeparator() + table);
    s.close();
  }

  void createProcedure(String collection, String proc) throws SQLException {
    Statement s = c.createStatement();
    String stp = collection + dmd.getCatalogSeparator() + proc;
    //Create a Procedure with a space in its name
    String sql = "CREATE PROCEDURE "
        + stp
        + " (INOUT PARAM1 INTEGER, INOUT \"Param 1\" INTEGER, INOUT \"PARAM%1\" INTEGER, INOUT \"My \"\"Param\"\"\" INTEGER)"
        + " LANGUAGE SQL SPECIFIC " + stp + " SP: BEGIN"
        + "   SET PARAM1 = PARAM1 + 1;"
        + "   SET \"Param 1\"= \"Param 1\" + 1;"
        + "   SET \"PARAM%1\" = \"PARAM%1\" +1;"
        + "   SET \"My \"\"Param\"\"\" = \"My \"\"Param\"\"\" + 1;" + " END SP";
    s.execute(sql);
    s.close();
  }

  void dropProcedure(String collection, String proc) throws SQLException {
    DatabaseMetaData dmd = c.getMetaData();
    Statement s = c.createStatement();
    s
        .execute("DROP PROCEDURE " + collection + dmd.getCatalogSeparator()
            + proc);
    s.close();
  }

  //Test data format, nothing should be returned delimited.
  public void Var001() {
    try {
      Statement s = c.createStatement();
      ResultSet rs = s.executeQuery("SELECT * FROM " + collection1_
          + dmd.getCatalogSeparator() + table1_);
      ResultSetMetaData rsmd = rs.getMetaData();
      String schema = rsmd.getSchemaName(1);
      String table = rsmd.getTableName(1);
      String column1 = rsmd.getColumnName(1);
      String column2 = rsmd.getColumnName(2);
      String column3 = rsmd.getColumnName(3);
      String column4 = rsmd.getColumnName(4);
      rs.close();
      s.close();
      assertCondition(schema.equals("") && table.equals("")
          && column1.equals("MYCOLUMN") && column2.equals("My Column")
          && column3.equals("MYCO%LUMN")
          && column4.equals("Yes, \"My Column\""), schema + " != \"\", or "
          + table + " != \"\", or " + column1 + " != MYCOLUMN, or " + column2
          + " != My Column, or " + column3 + " != MYCO%LUMN, or " + column4
          + " != Yes, \"My Column\"");
    } catch (SQLException e) {
      failed(e, "Unexpected Exception");
    }
  }

  //Test extended column descriptors, if running to V5R3 or earlier, names should be delimited, except column names
  public void Var002() {
    try {
      Statement s = c1.createStatement();
      ResultSet rs = s.executeQuery("SELECT * FROM " + collection1_
          + dmd.getCatalogSeparator() + table1_+" A ORDER BY RRN(A)");
      ResultSetMetaData rsmd = rs.getMetaData();
      String schema = rsmd.getSchemaName(1);
      String table = rsmd.getTableName(1);
      String column1 = rsmd.getColumnName(1);
      String column2 = rsmd.getColumnName(2);
      String column3 = rsmd.getColumnName(3);
      String column4 = rsmd.getColumnName(4);
      rs.close();
      s.close();
        assertCondition(schema.equals(collection1NoDelim_) && table.equals("MYTABLE")
            && column1.equals("MYCOLUMN") && column2.equals("My Column")
            && column3.equals("MYCO%LUMN")
            && column4.equals("Yes, \"My Column\""), schema + " != JDDelim, or "
            + table + " != MYTABLE, or " + column1 + " != MYCOLUMN, or "
            + column2 + " != My Column, or " + column3 + " != MYCO%LUMN, or "
            + column4 + " != Yes, \"My Column\"");

    } catch (SQLException e) {
      failed(e, "Unexpected Exception");
    }
  }

  //Test that you can insert a row into a result set
  public void Var003() {
      if (checkJdbc30()) { 
	  Statement s = null;
	  ResultSet rs = null;
	  try {
	      s = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				    ResultSet.CONCUR_UPDATABLE);
	      rs = s.executeQuery("SELECT * FROM " + collection1_
				  + dmd.getCatalogSeparator() + table1_);
	      rs.moveToInsertRow();
	      rs.updateInt(1, 9);
	      rs.updateInt(2, 10);
	      rs.updateInt(3, 11);
	      rs.updateInt(4, 12);
	      rs.insertRow();
	      rs.beforeFirst();
	      boolean success = true;
	      rs.first();
	      if (rs.getInt(1) != 1 && rs.getInt(2) != 2 && rs.getInt(3) != 3
		  && rs.getInt(4) != 4)
		  success = false;
	      if (success) {
		  rs.next();
		  if (rs.getInt(1) != 5 && rs.getInt(2) != 6 && rs.getInt(3) != 7
		      && rs.getInt(4) != 8)
		      success = false;
		  if (success) {
		      rs.next();
		      if (rs.getInt(1) != 9 && rs.getInt(2) != 10 && rs.getInt(3) != 11
			  && rs.getInt(4) != 12)
			  success = false;
		      rs.deleteRow();
		  }
	      }
	      assertCondition(success);
	  } catch (Exception e) {
	      failed(e, "Unexpected Exception");
	  } finally {
	      try {
		  rs.close();
		  s.close();
	      } catch (Exception e) {

	      }
	  }
      }
  }

  //Test that you can insert a row into a result set, using descriptors
  public void Var004() {
      if (checkJdbc30()) { 
	  Statement s = null;
	  ResultSet rs = null;
	  try {
	      s = c1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				     ResultSet.CONCUR_UPDATABLE);
	      rs = s.executeQuery("SELECT * FROM " + collection1_
				  + dmd.getCatalogSeparator() + table1_);
	      rs.moveToInsertRow();
	      rs.updateInt(1, 9);
	      rs.updateInt(2, 10);
	      rs.updateInt(3, 11);
	      rs.updateInt(4, 12);
	      rs.insertRow();
	      rs.beforeFirst();
	      boolean success = true;
	      rs.first();
	      if (rs.getInt(1) != 1 && rs.getInt(2) != 2 && rs.getInt(3) != 3
		  && rs.getInt(4) != 4)
		  success = false;
	      if (success) {
		  rs.next();
		  if (rs.getInt(1) != 5 && rs.getInt(2) != 6 && rs.getInt(3) != 7
		      && rs.getInt(4) != 8)
		      success = false;
		  if (success) {
		      rs.next();
		      if (rs.getInt(1) != 9 && rs.getInt(2) != 10 && rs.getInt(3) != 11
			  && rs.getInt(4) != 12)
			  success = false;
		      rs.deleteRow();
		  }
	      }
	      assertCondition(success);
	  } catch (Exception e) {
	      failed(e, "Unexpected Exception");
	  } finally {
	      try {
		  rs.close();
		  s.close();
	      } catch (Exception e) {

	      }
	  }
      }

  }
  //Test that you can update a row into a result set
  public void Var005() {
      if (checkJdbc30()) {
	  Statement s = null;
	  ResultSet rs = null;
	  try {
	      s = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				    ResultSet.CONCUR_UPDATABLE);
	      rs = s.executeQuery("SELECT * FROM " + collection1_
				  + dmd.getCatalogSeparator() + table1_);
	      rs.last();
	      rs.updateInt(1, 9);
	      rs.updateInt(2, 10);
	      rs.updateInt(3, 11);
	      rs.updateInt(4, 12);
	      rs.updateRow();
	      rs.beforeFirst();
	      boolean success = true;
	      rs.first();
	      if (rs.getInt(1) != 1 && rs.getInt(2) != 2 && rs.getInt(3) != 3
		  && rs.getInt(4) != 4)
		  success = false;
	      if (success) {
		  rs.next();
		  if (rs.getInt(1) != 9 && rs.getInt(2) != 10 && rs.getInt(3) != 11
		      && rs.getInt(4) != 12)
		      success = false;
		  rs.updateInt(1, 5);
		  rs.updateInt(2, 6);
		  rs.updateInt(3, 7);
		  rs.updateInt(4, 8);
		  rs.updateRow();
	      }
	      assertCondition(success);
	  } catch (Exception e) {
	      failed(e, "Unexpected Exception");
	  } finally {
	      try {
		  rs.close();
		  s.close();
	      } catch (Exception e) {

	      }
	  }
      }
  }

  //Test that you can update a row into a result set, using descriptors
  public void Var006() {
      if (checkJdbc30()) {
	  Statement s = null;
	  ResultSet rs = null;
	  try {
	      s = c1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				     ResultSet.CONCUR_UPDATABLE);
	      rs = s.executeQuery("SELECT * FROM " + collection1_
				  + dmd.getCatalogSeparator() + table1_);
	      rs.last();
	      rs.updateInt(1, 9);
	      rs.updateInt(2, 10);
	      rs.updateInt(3, 11);
	      rs.updateInt(4, 12);
	      rs.updateRow();
	      rs.beforeFirst();
	      boolean success = true;
	      rs.first();
	      if (rs.getInt(1) != 1 && rs.getInt(2) != 2 && rs.getInt(3) != 3
		  && rs.getInt(4) != 4)
		  success = false;
	      if (success) {
		  rs.next();
		  if (rs.getInt(1) != 9 && rs.getInt(2) != 10 && rs.getInt(3) != 11
		      && rs.getInt(4) != 12)
		      success = false;
		  rs.updateInt(1, 5);
		  rs.updateInt(2, 6);
		  rs.updateInt(3, 7);
		  rs.updateInt(4, 8);
		  rs.updateRow();
	      }
	      assertCondition(success);
	  } catch (Exception e) {
	      failed(e, "Unexpected Exception");
	  } finally {
	      try {
		  rs.close();
		  s.close();
	      } catch (Exception e) {

	      }
	  }
      }

  }
  //\"Delim\", \"My Table\"
  //Test data format, nothing should be returned delimited.
  public void Var007() {
    try {
      Statement s = c.createStatement();
      ResultSet rs = s.executeQuery("SELECT * FROM " + collection1_
          + dmd.getCatalogSeparator() + table2_);
      ResultSetMetaData rsmd = rs.getMetaData();
      String schema = rsmd.getSchemaName(1);
      String table = rsmd.getTableName(1);
      String column1 = rsmd.getColumnName(1);
      String column2 = rsmd.getColumnName(2);
      String column3 = rsmd.getColumnName(3);
      String column4 = rsmd.getColumnName(4);
      rs.close();
      s.close();
      assertCondition(schema.equals("") && table.equals("")
          && column1.equals("MYCOLUMN") && column2.equals("My Column")
          && column3.equals("MYCO%LUMN")
          && column4.equals("Yes, \"My Column\""), schema + " != \"\", "
          + table + " != \"\", " + column1 + " != MYCOLUMN, " + column2
          + " != My Column, " + column3 + " != MYCO%LUMN, " + column4
          + " != Yes, \"My Column\"");
    } catch (SQLException e) {
      failed(e, "Unexpected Exception");
    }
  }

  //Test extended column descriptors, if running to V5R3 or earlier, names should be delimited, except column names
  public void Var008() {
    try {
      Statement s = c1.createStatement();
      ResultSet rs = s.executeQuery("SELECT * FROM " + collection1_
          + dmd.getCatalogSeparator() + table2_);
      ResultSetMetaData rsmd = rs.getMetaData();
      String schema = rsmd.getSchemaName(1);
      String table = rsmd.getTableName(1);
      String column1 = rsmd.getColumnName(1);
      String column2 = rsmd.getColumnName(2);
      String column3 = rsmd.getColumnName(3);
      String column4 = rsmd.getColumnName(4);
      rs.close();
      s.close();
        assertCondition(schema.equals(collection1NoDelim_) && table.equals("My Table")
            && column1.equals("MYCOLUMN") && column2.equals("My Column")
            && column3.equals("MYCO%LUMN")
            && column4.equals("Yes, \"My Column\""), schema + " != JDDelim, "
            + table + " != My Table, " + column1 + " != MYCOLUMN, " + column2
            + " != My Column, " + column3 + " != MYCO%LUMN, " + column4
            + " != Yes, \"My Column\"");

    } catch (SQLException e) {
      failed(e, "Unexpected Exception");
    }
  }

  //Test that you can insert a row into a result set
  public void Var009() {
      if (checkJdbc30()) {

	  Statement s = null;
	  ResultSet rs = null;
	  try {
	      s = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				    ResultSet.CONCUR_UPDATABLE);
	      rs = s.executeQuery("SELECT * FROM " + collection1_
				  + dmd.getCatalogSeparator() + table2_);
	      rs.moveToInsertRow();
	      rs.updateInt(1, 9);
	      rs.updateInt(2, 10);
	      rs.updateInt(3, 11);
	      rs.updateInt(4, 12);
	      rs.insertRow();
	      rs.beforeFirst();
	      boolean success = true;
	      rs.first();
	      if (rs.getInt(1) != 1 && rs.getInt(2) != 2 && rs.getInt(3) != 3
		  && rs.getInt(4) != 4)
		  success = false;
	      if (success) {
		  rs.next();
		  if (rs.getInt(1) != 5 && rs.getInt(2) != 6 && rs.getInt(3) != 7
		      && rs.getInt(4) != 8)
		      success = false;
		  if (success) {
		      rs.next();
		      if (rs.getInt(1) != 9 && rs.getInt(2) != 10 && rs.getInt(3) != 11
			  && rs.getInt(4) != 12)
			  success = false;
		      rs.deleteRow();
		  }
	      }
	      assertCondition(success);
	  } catch (Exception e) {
	      failed(e, "Unexpected Exception");
	  } finally {
	      try {
		  rs.close();
		  s.close();
	      } catch (Exception e) {

	      }
	  }
      }
  }
  //Test that you can insert a row into a result set, using descriptors
  public void Var010() {
      if (checkJdbc30()) {
	  Statement s = null;
	  ResultSet rs = null;
	  try {
	      s = c1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				     ResultSet.CONCUR_UPDATABLE);
	      rs = s.executeQuery("SELECT * FROM " + collection1_
				  + dmd.getCatalogSeparator() + table2_);
	      rs.moveToInsertRow();
	      rs.updateInt(1, 9);
	      rs.updateInt(2, 10);
	      rs.updateInt(3, 11);
	      rs.updateInt(4, 12);
	      rs.insertRow();
	      rs.beforeFirst();
	      boolean success = true;
	      rs.first();
	      if (rs.getInt(1) != 1 && rs.getInt(2) != 2 && rs.getInt(3) != 3
		  && rs.getInt(4) != 4)
		  success = false;
	      if (success) {
		  rs.next();
		  if (rs.getInt(1) != 5 && rs.getInt(2) != 6 && rs.getInt(3) != 7
		      && rs.getInt(4) != 8)
		      success = false;
		  if (success) {
		      rs.next();
		      if (rs.getInt(1) != 9 && rs.getInt(2) != 10 && rs.getInt(3) != 11
			  && rs.getInt(4) != 12)
			  success = false;
		      rs.deleteRow();
		  }
	      }
	      assertCondition(success);
	  } catch (Exception e) {
	      failed(e, "Unexpected Exception");
	  } finally {
	      try {
		  rs.close();
		  s.close();
	      } catch (Exception e) {

	      }
	  }
      }

  }
  //Test that you can update a row into a result set
  public void Var011() {
      if (checkJdbc30()) {

	  Statement s = null;
	  ResultSet rs = null;
	  try {
	      s = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				    ResultSet.CONCUR_UPDATABLE);
	      rs = s.executeQuery("SELECT * FROM " + collection1_
				  + dmd.getCatalogSeparator() + table2_);
	      rs.last();
	      rs.updateInt(1, 9);
	      rs.updateInt(2, 10);
	      rs.updateInt(3, 11);
	      rs.updateInt(4, 12);
	      rs.updateRow();
	      rs.beforeFirst();
	      boolean success = true;
	      rs.first();
	      if (rs.getInt(1) != 1 && rs.getInt(2) != 2 && rs.getInt(3) != 3
		  && rs.getInt(4) != 4)
		  success = false;
	      if (success) {
		  rs.next();
		  if (rs.getInt(1) != 9 && rs.getInt(2) != 10 && rs.getInt(3) != 11
		      && rs.getInt(4) != 12)
		      success = false;
		  rs.updateInt(1, 5);
		  rs.updateInt(2, 6);
		  rs.updateInt(3, 7);
		  rs.updateInt(4, 8);
		  rs.updateRow();
	      }
	      assertCondition(success);
	  } catch (Exception e) {
	      failed(e, "Unexpected Exception");
	  } finally {
	      try {
		  rs.close();
		  s.close();
	      } catch (Exception e) {

	      }
	  }
      }

  }
  //Test that you can update a row into a result set, using descriptors
  public void Var012() {
      if (checkJdbc30()) {

	  Statement s = null;
	  ResultSet rs = null;
	  try {
	      s = c1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				     ResultSet.CONCUR_UPDATABLE);
	      rs = s.executeQuery("SELECT * FROM " + collection1_
				  + dmd.getCatalogSeparator() + table2_);
	      rs.last();
	      rs.updateInt(1, 9);
	      rs.updateInt(2, 10);
	      rs.updateInt(3, 11);
	      rs.updateInt(4, 12);
	      rs.updateRow();
	      rs.beforeFirst();
	      boolean success = true;
	      rs.first();
	      if (rs.getInt(1) != 1 && rs.getInt(2) != 2 && rs.getInt(3) != 3
		  && rs.getInt(4) != 4)
		  success = false;
	      if (success) {
		  rs.next();
		  if (rs.getInt(1) != 9 && rs.getInt(2) != 10 && rs.getInt(3) != 11
		      && rs.getInt(4) != 12)
		      success = false;
		  rs.updateInt(1, 5);
		  rs.updateInt(2, 6);
		  rs.updateInt(3, 7);
		  rs.updateInt(4, 8);
		  rs.updateRow();
	      }
	      assertCondition(success);
	  } catch (Exception e) {
	      failed(e, "Unexpected Exception");
	  } finally {
	      try {
		  rs.close();
		  s.close();
	      } catch (Exception e) {

	      }
	  }
      }
  }

  //Test data format, nothing should be returned delimited.
  public void Var013() {
    try {
      Statement s = c.createStatement();
      ResultSet rs = s.executeQuery("SELECT * FROM " + collection1_
          + dmd.getCatalogSeparator() + table3_);
      ResultSetMetaData rsmd = rs.getMetaData();
      String schema = rsmd.getSchemaName(1);
      String table = rsmd.getTableName(1);
      String column1 = rsmd.getColumnName(1);
      String column2 = rsmd.getColumnName(2);
      String column3 = rsmd.getColumnName(3);
      String column4 = rsmd.getColumnName(4);
      rs.close();
      s.close();
      assertCondition(schema.equals("") && table.equals("")
          && column1.equals("MYCOLUMN") && column2.equals("My Column")
          && column3.equals("MYCO%LUMN")
          && column4.equals("Yes, \"My Column\""), schema + " != \"\", "
          + table + " != \"\", " + column1 + " != MYCOLUMN, " + column2
          + " != My Column, " + column3 + " != MYCO%LUMN, " + column4
          + " != Yes, \"My Column\"");
    } catch (SQLException e) {
      failed(e, "Unexpected Exception");
    }
  }

  //Test extended column descriptors, if running to V5R3 or earlier, names should be delimited, except column names
  public void Var014() {
    try {
      Statement s = c1.createStatement();
      ResultSet rs = s.executeQuery("SELECT * FROM " + collection1_
          + dmd.getCatalogSeparator() + table3_);
      ResultSetMetaData rsmd = rs.getMetaData();
      String schema = rsmd.getSchemaName(1);
      String table = rsmd.getTableName(1);
      String column1 = rsmd.getColumnName(1);
      String column2 = rsmd.getColumnName(2);
      String column3 = rsmd.getColumnName(3);
      String column4 = rsmd.getColumnName(4);
      rs.close();
      s.close();
        assertCondition(schema.equals(collection1NoDelim_) && table.equals("My%$Table")
            && column1.equals("MYCOLUMN") && column2.equals("My Column")
            && column3.equals("MYCO%LUMN")
            && column4.equals("Yes, \"My Column\""), schema + " != JDDelim, "
            + table + " != My%$Table, " + column1 + " != MYCOLUMN, " + column2
            + " != My Column, " + column3 + " != MYCO%LUMN, " + column4
            + " != Yes, \"My Column\"");

    } catch (SQLException e) {
      failed(e, "Unexpected Exception");
    }
  }

  //Test that you can insert a row into a result set
  public void Var015() {
      if (checkJdbc30()) {

	  Statement s = null;
	  ResultSet rs = null;
	  try {
	      s = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				    ResultSet.CONCUR_UPDATABLE);
	      rs = s.executeQuery("SELECT * FROM " + collection1_
				  + dmd.getCatalogSeparator() + table3_);
	      rs.moveToInsertRow();
	      rs.updateInt(1, 9);
	      rs.updateInt(2, 10);
	      rs.updateInt(3, 11);
	      rs.updateInt(4, 12);
	      rs.insertRow();
	      rs.beforeFirst();
	      boolean success = true;
	      rs.first();
	      if (rs.getInt(1) != 1 && rs.getInt(2) != 2 && rs.getInt(3) != 3
		  && rs.getInt(4) != 4)
		  success = false;
	      if (success) {
		  rs.next();
		  if (rs.getInt(1) != 5 && rs.getInt(2) != 6 && rs.getInt(3) != 7
		      && rs.getInt(4) != 8)
		      success = false;
		  if (success) {
		      rs.next();
		      if (rs.getInt(1) != 9 && rs.getInt(2) != 10 && rs.getInt(3) != 11
			  && rs.getInt(4) != 12)
			  success = false;
		      rs.deleteRow();
		  }
	      }
	      assertCondition(success);
	  } catch (Exception e) {
	      failed(e, "Unexpected Exception");
	  } finally {
	      try {
		  rs.close();
		  s.close();
	      } catch (Exception e) {

	      }
	  }
      }
  }

  //Test that you can insert a row into a result set, using descriptors
  public void Var016() {
      if (checkJdbc30()) {

	  Statement s = null;
	  ResultSet rs = null;
	  try {
	      s = c1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				     ResultSet.CONCUR_UPDATABLE);
	      rs = s.executeQuery("SELECT * FROM " + collection1_
				  + dmd.getCatalogSeparator() + table3_);
	      rs.moveToInsertRow();
	      rs.updateInt(1, 9);
	      rs.updateInt(2, 10);
	      rs.updateInt(3, 11);
	      rs.updateInt(4, 12);
	      rs.insertRow();
	      rs.beforeFirst();
	      boolean success = true;
	      rs.first();
	      if (rs.getInt(1) != 1 && rs.getInt(2) != 2 && rs.getInt(3) != 3
		  && rs.getInt(4) != 4)
		  success = false;
	      if (success) {
		  rs.next();
		  if (rs.getInt(1) != 5 && rs.getInt(2) != 6 && rs.getInt(3) != 7
		      && rs.getInt(4) != 8)
		      success = false;
		  if (success) {
		      rs.next();
		      if (rs.getInt(1) != 9 && rs.getInt(2) != 10 && rs.getInt(3) != 11
			  && rs.getInt(4) != 12)
			  success = false;
		      rs.deleteRow();
		  }
	      }
	      assertCondition(success);
	  } catch (Exception e) {
	      failed(e, "Unexpected Exception");
	  } finally {
	      try {
		  rs.close();
		  s.close();
	      } catch (Exception e) {

	      }
	  }
      }
  }
  //Test that you can update a row into a result set
  public void Var017() {
      if (checkJdbc30()) {

	  Statement s = null;
	  ResultSet rs = null;
	  try {
	      s = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				    ResultSet.CONCUR_UPDATABLE);
	      rs = s.executeQuery("SELECT * FROM " + collection1_
				  + dmd.getCatalogSeparator() + table3_);
	      rs.last();
	      rs.updateInt(1, 9);
	      rs.updateInt(2, 10);
	      rs.updateInt(3, 11);
	      rs.updateInt(4, 12);
	      rs.updateRow();
	      rs.beforeFirst();
	      boolean success = true;
	      rs.first();
	      if (rs.getInt(1) != 1 && rs.getInt(2) != 2 && rs.getInt(3) != 3
		  && rs.getInt(4) != 4)
		  success = false;
	      if (success) {
		  rs.next();
		  if (rs.getInt(1) != 9 && rs.getInt(2) != 10 && rs.getInt(3) != 11
		      && rs.getInt(4) != 12)
		      success = false;
		  rs.updateInt(1, 5);
		  rs.updateInt(2, 6);
		  rs.updateInt(3, 7);
		  rs.updateInt(4, 8);
		  rs.updateRow();
	      }
	      assertCondition(success);
	  } catch (Exception e) {
	      failed(e, "Unexpected Exception");
	  } finally {
	      try {
		  rs.close();
		  s.close();
	      } catch (Exception e) {

	      }
	  }
      }
  }

  //Test that you can update a row into a result set, using descriptors
  public void Var018() {
      if (checkJdbc30()) {

	  Statement s = null;
	  ResultSet rs = null;
	  try {
	      s = c1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				     ResultSet.CONCUR_UPDATABLE);
	      rs = s.executeQuery("SELECT * FROM " + collection1_
				  + dmd.getCatalogSeparator() + table3_);
	      rs.last();
	      rs.updateInt(1, 9);
	      rs.updateInt(2, 10);
	      rs.updateInt(3, 11);
	      rs.updateInt(4, 12);
	      rs.updateRow();
	      rs.beforeFirst();
	      boolean success = true;
	      rs.first();
	      if (rs.getInt(1) != 1 && rs.getInt(2) != 2 && rs.getInt(3) != 3
		  && rs.getInt(4) != 4)
		  success = false;
	      if (success) {
		  rs.next();
		  if (rs.getInt(1) != 9 && rs.getInt(2) != 10 && rs.getInt(3) != 11
		      && rs.getInt(4) != 12)
		      success = false;
		  rs.updateInt(1, 5);
		  rs.updateInt(2, 6);
		  rs.updateInt(3, 7);
		  rs.updateInt(4, 8);
		  rs.updateRow();
	      }
	      assertCondition(success);
	  } catch (Exception e) {
	      failed(e, "Unexpected Exception");
	  } finally {
	      try {
		  rs.close();
		  s.close();
	      } catch (Exception e) {

	      }
	  }
      }
  }

  //Test data format, nothing should be returned delimited.
  public void Var019() {
    try {
      Statement s = c.createStatement();
      ResultSet rs = s.executeQuery("SELECT * FROM " + collection2_
          + dmd.getCatalogSeparator() + table1_);
      ResultSetMetaData rsmd = rs.getMetaData();
      String schema = rsmd.getSchemaName(1);
      String table = rsmd.getTableName(1);
      String column1 = rsmd.getColumnName(1);
      String column2 = rsmd.getColumnName(2);
      String column3 = rsmd.getColumnName(3);
      String column4 = rsmd.getColumnName(4);
      rs.close();
      s.close();
      assertCondition(schema.equals("") && table.equals("")
          && column1.equals("MYCOLUMN") && column2.equals("My Column")
          && column3.equals("MYCO%LUMN")
          && column4.equals("Yes, \"My Column\""), schema + " != \"\", "
          + table + " != \"\", " + column1 + " != MYCOLUMN, " + column2
          + " != My Column, " + column3 + " != MYCO%LUMN, " + column4
          + " != Yes, \"My Column\"");
    } catch (SQLException e) {
      failed(e, "Unexpected Exception");
    }
  }

  //Test extended column descriptors, if running to V5R3 or earlier, names should be delimited, except column names
  public void Var020() {
    try {
      Statement s = c1.createStatement();
      ResultSet rs = s.executeQuery("SELECT * FROM " + collection2_
          + dmd.getCatalogSeparator() + table1_);
      ResultSetMetaData rsmd = rs.getMetaData();
      String schema = rsmd.getSchemaName(1);
      String table = rsmd.getTableName(1);
      String column1 = rsmd.getColumnName(1);
      String column2 = rsmd.getColumnName(2);
      String column3 = rsmd.getColumnName(3);
      String column4 = rsmd.getColumnName(4);
      rs.close();
      s.close();
      if (getRelease() <= JDTestDriver.RELEASE_V7R1M0) //no extended column descriptions returned prior to V5R2, therefore no schema name or table name
        assertCondition(schema.equals("") && table.equals("")
            && column1.equals("MYCOLUMN") && column2.equals("My Column")
            && column3.equals("MYCO%LUMN")
            && column4.equals("Yes, \"My Column\""), schema + " != \"\", "
            + table + " != \"\", " + column1 + " != MYCOLUMN, " + column2
            + " != My Column, " + column3 + " != MYCO%LUMN, " + column4
            + " != Yes, \"My Column\"");
      else if (getRelease() <= JDTestDriver.RELEASE_V7R1M0) //names should come back delimited
        assertCondition(schema.equals(collection2_) && table.equals("MYTABLE")
            && column1.equals("MYCOLUMN") && column2.equals("My Column")
            && column3.equals("MYCO%LUMN")
            && column4.equals("Yes, \"My Column\""), schema + " != JDDELIM, "
            + table + " != MYTABLE, " + column1 + " != MYCOLUMN, " + column2
            + " != My Column, " + column3 + " != MYCO%LUMN, " + column4
            + " != Yes, \"My Column\"");
      else
        assertCondition(schema.equals(collection2_) && table.equals("MYTABLE")
            && column1.equals("MYCOLUMN") && column2.equals("My Column")
            && column3.equals("MYCO%LUMN")
            && column4.equals("Yes, \"My Column\""), schema + " != JDDELIM, "
            + table + " != MYTABLE, " + column1 + " != MYCOLUMN, " + column2
            + " != My Column, " + column3 + " != MYCO%LUMN, " + column4
            + " != Yes, \"My Column\"");

    } catch (SQLException e) {
      failed(e, "Unexpected Exception");
    }
  }

  //Test that you can insert a row into a result set
  public void Var021() {
      if (checkJdbc30()) {

	  Statement s = null;
	  ResultSet rs = null;
	  try {
	      s = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				    ResultSet.CONCUR_UPDATABLE);
	      rs = s.executeQuery("SELECT * FROM " + collection2_
				  + dmd.getCatalogSeparator() + table1_);
	      rs.moveToInsertRow();
	      rs.updateInt(1, 9);
	      rs.updateInt(2, 10);
	      rs.updateInt(3, 11);
	      rs.updateInt(4, 12);
	      rs.insertRow();
	      rs.beforeFirst();
	      boolean success = true;
	      rs.first();
	      if (rs.getInt(1) != 1 && rs.getInt(2) != 2 && rs.getInt(3) != 3
		  && rs.getInt(4) != 4)
		  success = false;
	      if (success) {
		  rs.next();
		  if (rs.getInt(1) != 5 && rs.getInt(2) != 6 && rs.getInt(3) != 7
		      && rs.getInt(4) != 8)
		      success = false;
		  if (success) {
		      rs.next();
		      if (rs.getInt(1) != 9 && rs.getInt(2) != 10 && rs.getInt(3) != 11
			  && rs.getInt(4) != 12)
			  success = false;
		      rs.deleteRow();
		  }
	      }
	      assertCondition(success);
	  } catch (Exception e) {
	      failed(e, "Unexpected Exception");
	  } finally {
	      try {
		  rs.close();
		  s.close();
	      } catch (Exception e) {

	      }
	  }
      }
  }

  //Test that you can insert a row into a result set, using descriptors
  public void Var022() {
      if (checkJdbc30()) {
	  Statement s = null;
	  ResultSet rs = null;
	  try {
	      s = c1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				     ResultSet.CONCUR_UPDATABLE);
	      rs = s.executeQuery("SELECT * FROM " + collection2_
				  + dmd.getCatalogSeparator() + table1_);
	      rs.moveToInsertRow();
	      rs.updateInt(1, 9);
	      rs.updateInt(2, 10);
	      rs.updateInt(3, 11);
	      rs.updateInt(4, 12);
	      rs.insertRow();
	      rs.beforeFirst();
	      boolean success = true;
	      rs.first();
	      if (rs.getInt(1) != 1 && rs.getInt(2) != 2 && rs.getInt(3) != 3
		  && rs.getInt(4) != 4)
		  success = false;
	      if (success) {
		  rs.next();
		  if (rs.getInt(1) != 5 && rs.getInt(2) != 6 && rs.getInt(3) != 7
		      && rs.getInt(4) != 8)
		      success = false;
		  if (success) {
		      rs.next();
		      if (rs.getInt(1) != 9 && rs.getInt(2) != 10 && rs.getInt(3) != 11
			  && rs.getInt(4) != 12)
			  success = false;
		      rs.deleteRow();
		  }
	      }
	      assertCondition(success);
	  } catch (Exception e) {
	      failed(e, "Unexpected Exception");
	  } finally {
	      try {
		  rs.close();
		  s.close();
	      } catch (Exception e) {

	      }
	  }
      }
  }

  //Test that you can update a row into a result set
  public void Var023() {
      if (checkJdbc30()) {
	  Statement s = null;
	  ResultSet rs = null;
	  try {
	      s = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				    ResultSet.CONCUR_UPDATABLE);
	      rs = s.executeQuery("SELECT * FROM " + collection2_
				  + dmd.getCatalogSeparator() + table1_);
	      rs.last();
	      rs.updateInt(1, 9);
	      rs.updateInt(2, 10);
	      rs.updateInt(3, 11);
	      rs.updateInt(4, 12);
	      rs.updateRow();
	      rs.beforeFirst();
	      boolean success = true;
	      rs.first();
	      if (rs.getInt(1) != 1 && rs.getInt(2) != 2 && rs.getInt(3) != 3
		  && rs.getInt(4) != 4)
		  success = false;
	      if (success) {
		  rs.next();
		  if (rs.getInt(1) != 9 && rs.getInt(2) != 10 && rs.getInt(3) != 11
		      && rs.getInt(4) != 12)
		      success = false;
		  rs.updateInt(1, 5);
		  rs.updateInt(2, 6);
		  rs.updateInt(3, 7);
		  rs.updateInt(4, 8);
		  rs.updateRow();
	      }
	      assertCondition(success);
	  } catch (Exception e) {
	      failed(e, "Unexpected Exception");
	  } finally {
	      try {
		  rs.close();
		  s.close();
	      } catch (Exception e) {

	      }
	  }
      }
  }

  //Test that you can update a row into a result set, using descriptors
  public void Var024() {
      if (checkJdbc30()) {
	  Statement s = null;
	  ResultSet rs = null;
	  try {
	      s = c1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				     ResultSet.CONCUR_UPDATABLE);
	      rs = s.executeQuery("SELECT * FROM " + collection2_
				  + dmd.getCatalogSeparator() + table1_);
	      rs.last();
	      rs.updateInt(1, 9);
	      rs.updateInt(2, 10);
	      rs.updateInt(3, 11);
	      rs.updateInt(4, 12);
	      rs.updateRow();
	      rs.beforeFirst();
	      boolean success = true;
	      rs.first();
	      if (rs.getInt(1) != 1 && rs.getInt(2) != 2 && rs.getInt(3) != 3
		  && rs.getInt(4) != 4)
		  success = false;
	      if (success) {
		  rs.next();
		  if (rs.getInt(1) != 9 && rs.getInt(2) != 10 && rs.getInt(3) != 11
		      && rs.getInt(4) != 12)
		      success = false;
		  rs.updateInt(1, 5);
		  rs.updateInt(2, 6);
		  rs.updateInt(3, 7);
		  rs.updateInt(4, 8);
		  rs.updateRow();
	      }
	      assertCondition(success);
	  } catch (Exception e) {
	      failed(e, "Unexpected Exception");
	  } finally {
	      try {
		  rs.close();
		  s.close();
	      } catch (Exception e) {

	      }
	  }
      }
  }

  //Test data format, nothing should be returned delimited.
  public void Var025() {
    try {
      Statement s = c.createStatement();
      ResultSet rs = s.executeQuery("SELECT * FROM " + collection2_
          + dmd.getCatalogSeparator() + table2_);
      ResultSetMetaData rsmd = rs.getMetaData();
      String schema = rsmd.getSchemaName(1);
      String table = rsmd.getTableName(1);
      String column1 = rsmd.getColumnName(1);
      String column2 = rsmd.getColumnName(2);
      String column3 = rsmd.getColumnName(3);
      String column4 = rsmd.getColumnName(4);
      rs.close();
      s.close();
      assertCondition(schema.equals("") && table.equals("")
          && column1.equals("MYCOLUMN") && column2.equals("My Column")
          && column3.equals("MYCO%LUMN")
          && column4.equals("Yes, \"My Column\""), schema + " != \"\", "
          + table + " != \"\", " + column1 + " != MYCOLUMN, " + column2
          + " != My Column, " + column3 + " != MYCO%LUMN, " + column4
          + " != Yes, \"My Column\"");
    } catch (SQLException e) {
      failed(e, "Unexpected Exception");
    }
  }

  //Test extended column descriptors, if running to V5R3 or earlier, names should be delimited, except column names
  public void Var026() {
    try {
      Statement s = c1.createStatement();
      ResultSet rs = s.executeQuery("SELECT * FROM " + collection2_
          + dmd.getCatalogSeparator() + table2_);
      ResultSetMetaData rsmd = rs.getMetaData();
      String schema = rsmd.getSchemaName(1);
      String table = rsmd.getTableName(1);
      String column1 = rsmd.getColumnName(1);
      String column2 = rsmd.getColumnName(2);
      String column3 = rsmd.getColumnName(3);
      String column4 = rsmd.getColumnName(4);
      rs.close();
      s.close();
      if (getRelease() <= JDTestDriver.RELEASE_V7R1M0) //no extended column descriptions returned prior to V5R2, therefore no schema name or table name
        assertCondition(schema.equals("") && table.equals("")
            && column1.equals("MYCOLUMN") && column2.equals("My Column")
            && column3.equals("MYCO%LUMN")
            && column4.equals("Yes, \"My Column\""), schema + " != \"\", "
            + table + " != \"\", " + column1 + " != MYCOLUMN, " + column2
            + " != My Column, " + column3 + " != MYCO%LUMN, " + column4
            + " != Yes, \"My Column\"");
      else if (getRelease() <= JDTestDriver.RELEASE_V7R1M0)
        assertCondition(schema.equals(collection2_) && table.equals("\"My Table\"")
            && column1.equals("MYCOLUMN") && column2.equals("My Column")
            && column3.equals("MYCO%LUMN")
            && column4.equals("Yes, \"My Column\""), schema + " != JDDELIM, "
            + table + " != \"My Table\", " + column1 + " != MYCOLUMN, "
            + column2 + " != My Column, " + column3 + " != MYCO%LUMN, "
            + column4 + " != Yes, \"My Column\"");
      else
        assertCondition(schema.equals(collection2_) && table.equals("My Table")
            && column1.equals("MYCOLUMN") && column2.equals("My Column")
            && column3.equals("MYCO%LUMN")
            && column4.equals("Yes, \"My Column\""), schema + " != JDDELIM, "
            + table + " != My Table, " + column1 + " != MYCOLUMN, " + column2
            + " != My Column, " + column3 + " != MYCO%LUMN, " + column4
            + " != Yes, \"My Column\"");

    } catch (SQLException e) {
      failed(e, "Unexpected Exception");
    }
  }

  //Test that you can insert a row into a result set
  public void Var027() {
      if (checkJdbc30()) {
	  Statement s = null;
	  ResultSet rs = null;
	  try {
	      s = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				    ResultSet.CONCUR_UPDATABLE);
	      rs = s.executeQuery("SELECT * FROM " + collection2_
				  + dmd.getCatalogSeparator() + table2_);
	      rs.moveToInsertRow();
	      rs.updateInt(1, 9);
	      rs.updateInt(2, 10);
	      rs.updateInt(3, 11);
	      rs.updateInt(4, 12);
	      rs.insertRow();
	      rs.beforeFirst();
	      boolean success = true;
	      rs.first();
	      if (rs.getInt(1) != 1 && rs.getInt(2) != 2 && rs.getInt(3) != 3
		  && rs.getInt(4) != 4)
		  success = false;
	      if (success) {
		  rs.next();
		  if (rs.getInt(1) != 5 && rs.getInt(2) != 6 && rs.getInt(3) != 7
		      && rs.getInt(4) != 8)
		      success = false;
		  if (success) {
		      rs.next();
		      if (rs.getInt(1) != 9 && rs.getInt(2) != 10 && rs.getInt(3) != 11
			  && rs.getInt(4) != 12)
			  success = false;
		      rs.deleteRow();
		  }
	      }
	      assertCondition(success);
	  } catch (Exception e) {
	      failed(e, "Unexpected Exception");
	  } finally {
	      try {
		  rs.close();
		  s.close();
	      } catch (Exception e) {

	      }
	  }
      }
  }

  //Test that you can insert a row into a result set, using descriptors
  public void Var028() {
      if (checkJdbc30()) {
	  Statement s = null;
	  ResultSet rs = null;
	  try {
	      s = c1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				     ResultSet.CONCUR_UPDATABLE);
	      rs = s.executeQuery("SELECT * FROM " + collection2_
				  + dmd.getCatalogSeparator() + table2_);
	      rs.moveToInsertRow();
	      rs.updateInt(1, 9);
	      rs.updateInt(2, 10);
	      rs.updateInt(3, 11);
	      rs.updateInt(4, 12);
	      rs.insertRow();
	      rs.beforeFirst();
	      boolean success = true;
	      rs.first();
	      if (rs.getInt(1) != 1 && rs.getInt(2) != 2 && rs.getInt(3) != 3
		  && rs.getInt(4) != 4)
		  success = false;
	      if (success) {
		  rs.next();
		  if (rs.getInt(1) != 5 && rs.getInt(2) != 6 && rs.getInt(3) != 7
		      && rs.getInt(4) != 8)
		      success = false;
		  if (success) {
		      rs.next();
		      if (rs.getInt(1) != 9 && rs.getInt(2) != 10 && rs.getInt(3) != 11
			  && rs.getInt(4) != 12)
			  success = false;
		      rs.deleteRow();
		  }
	      }
	      assertCondition(success);
	  } catch (Exception e) {
	      failed(e, "Unexpected Exception");
	  } finally {
	      try {
		  rs.close();
		  s.close();
	      } catch (Exception e) {

	      }
	  }
      }
  }
  //Test that you can update a row into a result set
  public void Var029() {
      if (checkJdbc30()) {
	  Statement s = null;
	  ResultSet rs = null;
	  try {
	      s = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				    ResultSet.CONCUR_UPDATABLE);
	      rs = s.executeQuery("SELECT * FROM " + collection2_
				  + dmd.getCatalogSeparator() + table2_);
	      rs.last();
	      rs.updateInt(1, 9);
	      rs.updateInt(2, 10);
	      rs.updateInt(3, 11);
	      rs.updateInt(4, 12);
	      rs.updateRow();
	      rs.beforeFirst();
	      boolean success = true;
	      rs.first();
	      if (rs.getInt(1) != 1 && rs.getInt(2) != 2 && rs.getInt(3) != 3
		  && rs.getInt(4) != 4)
		  success = false;
	      if (success) {
		  rs.next();
		  if (rs.getInt(1) != 9 && rs.getInt(2) != 10 && rs.getInt(3) != 11
		      && rs.getInt(4) != 12)
		      success = false;
		  rs.updateInt(1, 5);
		  rs.updateInt(2, 6);
		  rs.updateInt(3, 7);
		  rs.updateInt(4, 8);
		  rs.updateRow();
	      }
	      assertCondition(success);
	  } catch (Exception e) {
	      failed(e, "Unexpected Exception");
	  } finally {
	      try {
		  rs.close();
		  s.close();
	      } catch (Exception e) {

	      }
	  }
      }
  }
  //Test that you can update a row into a result set, using descriptors
  public void Var030() {
      if (getDriver()==JDTestDriver.DRIVER_NATIVE && getRelease() == JDTestDriver.RELEASE_V7R1M0) {
	  notApplicable("Delimited tests not working for native JDBC driver on V5R3");  return;
      } 
      if (checkJdbc30()) {
	  Statement s = null;
	  ResultSet rs = null;
	  try {
	      s = c1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				     ResultSet.CONCUR_UPDATABLE);
	      rs = s.executeQuery("SELECT * FROM " + collection2_
				  + dmd.getCatalogSeparator() + table2_);
	      rs.last();
	      rs.updateInt(1, 9);
	      rs.updateInt(2, 10);
	      rs.updateInt(3, 11);
	      rs.updateInt(4, 12);
	      rs.updateRow();
	      rs.beforeFirst();
	      boolean success = true;
	      rs.first();
	      if (rs.getInt(1) != 1 && rs.getInt(2) != 2 && rs.getInt(3) != 3
		  && rs.getInt(4) != 4)
		  success = false;
	      if (success) {
		  rs.next();
		  if (rs.getInt(1) != 9 && rs.getInt(2) != 10 && rs.getInt(3) != 11
		      && rs.getInt(4) != 12)
		      success = false;
		  rs.updateInt(1, 5);
		  rs.updateInt(2, 6);
		  rs.updateInt(3, 7);
		  rs.updateInt(4, 8);
		  rs.updateRow();
	      }
	      assertCondition(success);
	  } catch (Exception e) {
	      failed(e, "Unexpected Exception");
	  } finally {
	      try {
		  rs.close();
		  s.close();
	      } catch (Exception e) {

	      }
	  }
      }
  }

  //Test data format, nothing should be returned delimited.
  public void Var031() {
    try {
      Statement s = c.createStatement();
      ResultSet rs = s.executeQuery("SELECT * FROM " + collection2_
          + dmd.getCatalogSeparator() + table3_);
      ResultSetMetaData rsmd = rs.getMetaData();
      String schema = rsmd.getSchemaName(1);
      String table = rsmd.getTableName(1);
      String column1 = rsmd.getColumnName(1);
      String column2 = rsmd.getColumnName(2);
      String column3 = rsmd.getColumnName(3);
      String column4 = rsmd.getColumnName(4);
      rs.close();
      s.close();
      assertCondition(schema.equals("") && table.equals("")
          && column1.equals("MYCOLUMN") && column2.equals("My Column")
          && column3.equals("MYCO%LUMN")
          && column4.equals("Yes, \"My Column\""), schema + " != \"\", "
          + table + " != \"\", " + column1 + " != MYCOLUMN, " + column2
          + " != My Column, " + column3 + " != MYCO%LUMN, " + column4
          + " != Yes, \"My Column\"");
    } catch (SQLException e) {
      failed(e, "Unexpected Exception");
    }
  }

  //Test extended column descriptors, if running to V5R3 or earlier, names should be delimited, except column names
  public void Var032() {
    try {
      Statement s = c1.createStatement();
      ResultSet rs = s.executeQuery("SELECT * FROM " + collection2_
          + dmd.getCatalogSeparator() + table3_);
      ResultSetMetaData rsmd = rs.getMetaData();
      String schema = rsmd.getSchemaName(1);
      String table = rsmd.getTableName(1);
      String column1 = rsmd.getColumnName(1);
      String column2 = rsmd.getColumnName(2);
      String column3 = rsmd.getColumnName(3);
      String column4 = rsmd.getColumnName(4);
      rs.close();
      s.close();
      if (getRelease() <= JDTestDriver.RELEASE_V7R1M0) //no extended column descriptions returned prior to V5R2, therefore no schema name or table name
        assertCondition(schema.equals("") && table.equals("")
            && column1.equals("MYCOLUMN") && column2.equals("My Column")
            && column3.equals("MYCO%LUMN")
            && column4.equals("Yes, \"My Column\""), schema + " != \"\", "
            + table + " != \"\", " + column1 + " != MYCOLUMN, " + column2
            + " != My Column, " + column3 + " != MYCO%LUMN, " + column4
            + " != Yes, \"My Column\"");
      else if (getRelease() <= JDTestDriver.RELEASE_V7R1M0)
        assertCondition(schema.equals(collection2_) && table.equals("\"My%$Table\"")
            && column1.equals("MYCOLUMN") && column2.equals("My Column")
            && column3.equals("MYCO%LUMN")
            && column4.equals("Yes, \"My Column\""), schema + " != JDDELIM, "
            + table + " != \"My%$Table\", " + column1 + " != MYCOLUMN, "
            + column2 + " != My Column, " + column3 + " != MYCO%LUMN, "
            + column4 + " != Yes, \"My Column\"");
      else
        assertCondition(schema.equals(collection2_) && table.equals("My%$Table")
            && column1.equals("MYCOLUMN") && column2.equals("My Column")
            && column3.equals("MYCO%LUMN")
            && column4.equals("Yes, \"My Column\""), schema + " != JDDELIM, "
            + table + " != My%$Table, " + column1 + " != MYCOLUMN, " + column2
            + " != My Column, " + column3 + " != MYCO%LUMN, " + column4
            + " != Yes, \"My Column\"");

    } catch (SQLException e) {
      failed(e, "Unexpected Exception");
    }
  }

  //Test that you can insert a row into a result set
  public void Var033() {
      if (checkJdbc30()) {
	  Statement s = null;
	  ResultSet rs = null;
	  try {
	      s = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				    ResultSet.CONCUR_UPDATABLE);
	      rs = s.executeQuery("SELECT * FROM " + collection2_
				  + dmd.getCatalogSeparator() + table3_);
	      rs.moveToInsertRow();
	      rs.updateInt(1, 9);
	      rs.updateInt(2, 10);
	      rs.updateInt(3, 11);
	      rs.updateInt(4, 12);
	      rs.insertRow();
	      rs.beforeFirst();
	      boolean success = true;
	      rs.first();
	      if (rs.getInt(1) != 1 && rs.getInt(2) != 2 && rs.getInt(3) != 3
		  && rs.getInt(4) != 4)
		  success = false;
	      if (success) {
		  rs.next();
		  if (rs.getInt(1) != 5 && rs.getInt(2) != 6 && rs.getInt(3) != 7
		      && rs.getInt(4) != 8)
		      success = false;
		  if (success) {
		      rs.next();
		      if (rs.getInt(1) != 9 && rs.getInt(2) != 10 && rs.getInt(3) != 11
			  && rs.getInt(4) != 12)
			  success = false;
		      rs.deleteRow();
		  }
	      }
	      assertCondition(success);
	  } catch (Exception e) {
	      failed(e, "Unexpected Exception");
	  } finally {
	      try {
		  rs.close();
		  s.close();
	      } catch (Exception e) {

	      }
	  }
      }
  }
  //Test that you can insert a row into a result set, using descriptors
  public void Var034() {
      if (checkJdbc30()) {
	  Statement s = null;
	  ResultSet rs = null;
	  try {
	      s = c1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				     ResultSet.CONCUR_UPDATABLE);
	      rs = s.executeQuery("SELECT * FROM " + collection2_
				  + dmd.getCatalogSeparator() + table3_);
	      rs.moveToInsertRow();
	      rs.updateInt(1, 9);
	      rs.updateInt(2, 10);
	      rs.updateInt(3, 11);
	      rs.updateInt(4, 12);
	      rs.insertRow();
	      rs.beforeFirst();
	      boolean success = true;
	      rs.first();
	      if (rs.getInt(1) != 1 && rs.getInt(2) != 2 && rs.getInt(3) != 3
		  && rs.getInt(4) != 4)
		  success = false;
	      if (success) {
		  rs.next();
		  if (rs.getInt(1) != 5 && rs.getInt(2) != 6 && rs.getInt(3) != 7
		      && rs.getInt(4) != 8)
		      success = false;
		  if (success) {
		      rs.next();
		      if (rs.getInt(1) != 9 && rs.getInt(2) != 10 && rs.getInt(3) != 11
			  && rs.getInt(4) != 12)
			  success = false;
		      rs.deleteRow();
		  }
	      }
	      assertCondition(success);
	  } catch (Exception e) {
	      failed(e, "Unexpected Exception");
	  } finally {
	      try {
		  rs.close();
		  s.close();
	      } catch (Exception e) {

	      }
	  }
      }
  }
  //Test that you can update a row into a result set
  public void Var035() {
      if (checkJdbc30()) {
	  Statement s = null;
	  ResultSet rs = null;
	  try {
	      s = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				    ResultSet.CONCUR_UPDATABLE);
	      rs = s.executeQuery("SELECT * FROM " + collection2_
				  + dmd.getCatalogSeparator() + table3_);
	      rs.last();
	      rs.updateInt(1, 9);
	      rs.updateInt(2, 10);
	      rs.updateInt(3, 11);
	      rs.updateInt(4, 12);
	      rs.updateRow();
	      rs.beforeFirst();
	      boolean success = true;
	      rs.first();
	      if (rs.getInt(1) != 1 && rs.getInt(2) != 2 && rs.getInt(3) != 3
		  && rs.getInt(4) != 4)
		  success = false;
	      if (success) {
		  rs.next();
		  if (rs.getInt(1) != 9 && rs.getInt(2) != 10 && rs.getInt(3) != 11
		      && rs.getInt(4) != 12)
		      success = false;
		  rs.updateInt(1, 5);
		  rs.updateInt(2, 6);
		  rs.updateInt(3, 7);
		  rs.updateInt(4, 8);
		  rs.updateRow();
	      }
	      assertCondition(success);
	  } catch (Exception e) {
	      failed(e, "Unexpected Exception");
	  } finally {
	      try {
		  rs.close();
		  s.close();
	      } catch (Exception e) {

	      }
	  }
      }
  }

  //Test that you can update a row into a result set, using descriptors
  public void Var036() {
      if (checkJdbc30()) {
	  Statement s = null;
	  ResultSet rs = null;
	  try {
	      s = c1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				     ResultSet.CONCUR_UPDATABLE);
	      rs = s.executeQuery("SELECT * FROM " + collection2_
				  + dmd.getCatalogSeparator() + table3_);
	      rs.last();
	      rs.updateInt(1, 9);
	      rs.updateInt(2, 10);
	      rs.updateInt(3, 11);
	      rs.updateInt(4, 12);
	      rs.updateRow();
	      rs.beforeFirst();
	      boolean success = true;
	      rs.first();
	      if (rs.getInt(1) != 1 && rs.getInt(2) != 2 && rs.getInt(3) != 3
		  && rs.getInt(4) != 4)
		  success = false;
	      if (success) {
		  rs.next();
		  if (rs.getInt(1) != 9 && rs.getInt(2) != 10 && rs.getInt(3) != 11
		      && rs.getInt(4) != 12)
		      success = false;
		  rs.updateInt(1, 5);
		  rs.updateInt(2, 6);
		  rs.updateInt(3, 7);
		  rs.updateInt(4, 8);
		  rs.updateRow();
	      }
	      assertCondition(success);
	  } catch (Exception e) {
	      failed(e, "Unexpected Exception");
	  } finally {
	      try {
		  rs.close();
		  s.close();
	      } catch (Exception e) {

	      }
	  }
      }
  }

  //Test data format, nothing should be returned delimited.
  public void Var037() {
    try {
      Statement s = c.createStatement();
      ResultSet rs = s.executeQuery("SELECT * FROM " + collection3_
          + dmd.getCatalogSeparator() + table1_);
      ResultSetMetaData rsmd = rs.getMetaData();
      String schema = rsmd.getSchemaName(1);
      String table = rsmd.getTableName(1);
      String column1 = rsmd.getColumnName(1);
      String column2 = rsmd.getColumnName(2);
      String column3 = rsmd.getColumnName(3);
      String column4 = rsmd.getColumnName(4);
      rs.close();
      s.close();
      assertCondition(schema.equals("") && table.equals("")
          && column1.equals("MYCOLUMN") && column2.equals("My Column")
          && column3.equals("MYCO%LUMN")
          && column4.equals("Yes, \"My Column\""), schema + " != \"\", "
          + table + " != \"\", " + column1 + " != MYCOLUMN, " + column2
          + " != My Column, " + column3 + " != MYCO%LUMN, " + column4
          + " != Yes, \"My Column\"");
    } catch (SQLException e) {
      failed(e, "Unexpected Exception");
    }
  }

  //Test extended column descriptors, if running to V5R3 or earlier, names should be delimited, except column names
  public void Var038() {
    try {
      Statement s = c1.createStatement();
      ResultSet rs = s.executeQuery("SELECT * FROM " + collection3_
          + dmd.getCatalogSeparator() + table1_);
      ResultSetMetaData rsmd = rs.getMetaData();
      String schema = rsmd.getSchemaName(1);
      String table = rsmd.getTableName(1);
      String column1 = rsmd.getColumnName(1);
      String column2 = rsmd.getColumnName(2);
      String column3 = rsmd.getColumnName(3);
      String column4 = rsmd.getColumnName(4);
      rs.close();
      s.close();
      if (getRelease() <= JDTestDriver.RELEASE_V7R1M0) //no extended column descriptions returned prior to V5R2, therefore no schema name or table name
        assertCondition(schema.equals("") && table.equals("")
            && column1.equals("MYCOLUMN") && column2.equals("My Column")
            && column3.equals("MYCO%LUMN")
            && column4.equals("Yes, \"My Column\""), schema + " != \"\", "
            + table + " != \"\", " + column1 + " != MYCOLUMN, " + column2
            + " != My Column, " + column3 + " != MYCO%LUMN, " + column4
            + " != Yes, \"My Column\"");
      else if (getRelease() <= JDTestDriver.RELEASE_V7R1M0) //names should come back delimited
        assertCondition(schema.equals(collection3_) && table.equals("MYTABLE")
            && column1.equals("MYCOLUMN") && column2.equals("My Column")
            && column3.equals("MYCO%LUMN")
            && column4.equals("Yes, \"My Column\""), schema
            + " != \"JDDELIM%\", " + table + " != MYTABLE, " + column1
            + " != MYCOLUMN, " + column2 + " != My Column, " + column3
            + " != MYCO%LUMN, " + column4 + " != Yes, \"My Column\"");
      else
        assertCondition(schema.equals(collection3NoDelim_) && table.equals("MYTABLE")
            && column1.equals("MYCOLUMN") && column2.equals("My Column")
            && column3.equals("MYCO%LUMN")
            && column4.equals("Yes, \"My Column\""), schema + " != "+collection3NoDelim_+", "
            + table + " != MYTABLE, " + column1 + " != MYCOLUMN, " + column2
            + " != My Column, " + column3 + " != MYCO%LUMN, " + column4
            + " != Yes, \"My Column\"");

    } catch (SQLException e) {
      failed(e, "Unexpected Exception");
    }
  }

  //Test that you can insert a row into a result set
  public void Var039() {
      if (checkJdbc30()) {
	  Statement s = null;
	  ResultSet rs = null;
	  try {
	      s = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				    ResultSet.CONCUR_UPDATABLE);
	      rs = s.executeQuery("SELECT * FROM " + collection3_
				  + dmd.getCatalogSeparator() + table1_);
	      rs.moveToInsertRow();
	      rs.updateInt(1, 9);
	      rs.updateInt(2, 10);
	      rs.updateInt(3, 11);
	      rs.updateInt(4, 12);
	      rs.insertRow();
	      rs.beforeFirst();
	      boolean success = true;
	      rs.first();
	      if (rs.getInt(1) != 1 && rs.getInt(2) != 2 && rs.getInt(3) != 3
		  && rs.getInt(4) != 4)
		  success = false;
	      if (success) {
		  rs.next();
		  if (rs.getInt(1) != 5 && rs.getInt(2) != 6 && rs.getInt(3) != 7
		      && rs.getInt(4) != 8)
		      success = false;
		  if (success) {
		      rs.next();
		      if (rs.getInt(1) != 9 && rs.getInt(2) != 10 && rs.getInt(3) != 11
			  && rs.getInt(4) != 12)
			  success = false;
		      rs.deleteRow();
		  }
	      }
	      assertCondition(success);
	  } catch (Exception e) {
	      failed(e, "Unexpected Exception");
	  } finally {
	      try {
		  rs.close();
		  s.close();
	      } catch (Exception e) {

	      }
	  }
      }
  }

  //Test that you can insert a row into a result set, using descriptors
  public void Var040() {
      if (checkJdbc30()) {
	  Statement s = null;
	  ResultSet rs = null;
	  try {
	      s = c1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				     ResultSet.CONCUR_UPDATABLE);
	      rs = s.executeQuery("SELECT * FROM " + collection3_
				  + dmd.getCatalogSeparator() + table1_);
	      rs.moveToInsertRow();
	      rs.updateInt(1, 9);
	      rs.updateInt(2, 10);
	      rs.updateInt(3, 11);
	      rs.updateInt(4, 12);
	      rs.insertRow();
	      rs.beforeFirst();
	      boolean success = true;
	      rs.first();
	      if (rs.getInt(1) != 1 && rs.getInt(2) != 2 && rs.getInt(3) != 3
		  && rs.getInt(4) != 4)
		  success = false;
	      if (success) {
		  rs.next();
		  if (rs.getInt(1) != 5 && rs.getInt(2) != 6 && rs.getInt(3) != 7
		      && rs.getInt(4) != 8)
		      success = false;
		  if (success) {
		      rs.next();
		      if (rs.getInt(1) != 9 && rs.getInt(2) != 10 && rs.getInt(3) != 11
			  && rs.getInt(4) != 12)
			  success = false;
		      rs.deleteRow();
		  }
	      }
	      assertCondition(success);
	  } catch (Exception e) {
	      failed(e, "Unexpected Exception");
	  } finally {
	      try {
		  rs.close();
		  s.close();
	      } catch (Exception e) {

	      }
	  }
      }
  }

  //Test that you can update a row into a result set
  public void Var041() {
      if (checkJdbc30()) {
	  Statement s = null;
	  ResultSet rs = null;
	  try {
	      s = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				    ResultSet.CONCUR_UPDATABLE);
	      rs = s.executeQuery("SELECT * FROM " + collection3_
				  + dmd.getCatalogSeparator() + table1_);
	      rs.last();
	      rs.updateInt(1, 9);
	      rs.updateInt(2, 10);
	      rs.updateInt(3, 11);
	      rs.updateInt(4, 12);
	      rs.updateRow();
	      rs.beforeFirst();
	      boolean success = true;
	      rs.first();
	      if (rs.getInt(1) != 1 && rs.getInt(2) != 2 && rs.getInt(3) != 3
		  && rs.getInt(4) != 4)
		  success = false;
	      if (success) {
		  rs.next();
		  if (rs.getInt(1) != 9 && rs.getInt(2) != 10 && rs.getInt(3) != 11
		      && rs.getInt(4) != 12)
		      success = false;
		  rs.updateInt(1, 5);
		  rs.updateInt(2, 6);
		  rs.updateInt(3, 7);
		  rs.updateInt(4, 8);
		  rs.updateRow();
	      }
	      assertCondition(success);
	  } catch (Exception e) {
	      failed(e, "Unexpected Exception");
	  } finally {
	      try {
		  rs.close();
		  s.close();
	      } catch (Exception e) {

	      }
	  }
      }
  }
  //Test that you can update a row into a result set, using descriptors
  public void Var042() {
      if (checkJdbc30()) {
	  Statement s = null;
	  ResultSet rs = null;
	  try {
	      s = c1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				     ResultSet.CONCUR_UPDATABLE);
	      rs = s.executeQuery("SELECT * FROM " + collection3_
				  + dmd.getCatalogSeparator() + table1_);
	      rs.last();
	      rs.updateInt(1, 9);
	      rs.updateInt(2, 10);
	      rs.updateInt(3, 11);
	      rs.updateInt(4, 12);
	      rs.updateRow();
	      rs.beforeFirst();
	      boolean success = true;
	      rs.first();
	      if (rs.getInt(1) != 1 && rs.getInt(2) != 2 && rs.getInt(3) != 3
		  && rs.getInt(4) != 4)
		  success = false;
	      if (success) {
		  rs.next();
		  if (rs.getInt(1) != 9 && rs.getInt(2) != 10 && rs.getInt(3) != 11
		      && rs.getInt(4) != 12)
		      success = false;
		  rs.updateInt(1, 5);
		  rs.updateInt(2, 6);
		  rs.updateInt(3, 7);
		  rs.updateInt(4, 8);
		  rs.updateRow();
	      }
	      assertCondition(success);
	  } catch (Exception e) {
	      failed(e, "Unexpected Exception");
	  } finally {
	      try {
		  rs.close();
		  s.close();
	      } catch (Exception e) {

	      }
	  }
      }
  }
  //Test data format, nothing should be returned delimited.
  public void Var043() {
    try {
      Statement s = c.createStatement();
      ResultSet rs = s.executeQuery("SELECT * FROM " + collection3_
          + dmd.getCatalogSeparator() + table2_);
      ResultSetMetaData rsmd = rs.getMetaData();
      String schema = rsmd.getSchemaName(1);
      String table = rsmd.getTableName(1);
      String column1 = rsmd.getColumnName(1);
      String column2 = rsmd.getColumnName(2);
      String column3 = rsmd.getColumnName(3);
      String column4 = rsmd.getColumnName(4);
      rs.close();
      s.close();
      assertCondition(schema.equals("") && table.equals("")
          && column1.equals("MYCOLUMN") && column2.equals("My Column")
          && column3.equals("MYCO%LUMN")
          && column4.equals("Yes, \"My Column\""), schema + " != \"\", "
          + table + " != \"\", " + column1 + " != MYCOLUMN, " + column2
          + " != My Column, " + column3 + " != MYCO%LUMN, " + column4
          + " != Yes, \"My Column\"");
    } catch (SQLException e) {
      failed(e, "Unexpected Exception");
    }
  }

  //Test extended column descriptors, if running to V5R3 or earlier, names should be delimited, except column names
  public void Var044() {
    try {
      Statement s = c1.createStatement();
      ResultSet rs = s.executeQuery("SELECT * FROM " + collection3_
          + dmd.getCatalogSeparator() + table2_);
      ResultSetMetaData rsmd = rs.getMetaData();
      String schema = rsmd.getSchemaName(1);
      String table = rsmd.getTableName(1);
      String column1 = rsmd.getColumnName(1);
      String column2 = rsmd.getColumnName(2);
      String column3 = rsmd.getColumnName(3);
      String column4 = rsmd.getColumnName(4);
      rs.close();
      s.close();
      if (getRelease() <= JDTestDriver.RELEASE_V7R1M0) //no extended column descriptions returned prior to V5R2, therefore no schema name or table name
        assertCondition(schema.equals("") && table.equals("")
            && column1.equals("MYCOLUMN") && column2.equals("My Column")
            && column3.equals("MYCO%LUMN")
            && column4.equals("Yes, \"My Column\""), schema + " != \"\", "
            + table + " != \"\", " + column1 + " != MYCOLUMN, " + column2
            + " != My Column, " + column3 + " != MYCO%LUMN, " + column4
            + " != Yes, \"My Column\"");
      else if (getRelease() <= JDTestDriver.RELEASE_V7R1M0)
        assertCondition(schema.equals(collection3_)
            && table.equals("\"My Table\"") && column1.equals("MYCOLUMN")
            && column2.equals("My Column") && column3.equals("MYCO%LUMN")
            && column4.equals("Yes, \"My Column\""), schema
            + " != \"JDDELIM%\", " + table + " != \"My Table\", " + column1
            + " != MYCOLUMN, " + column2 + " != My Column, " + column3
            + " != MYCO%LUMN, " + column4 + " != Yes, \"My Column\"");
      else
        assertCondition(schema.equals(collection3NoDelim_) && table.equals("My Table")
            && column1.equals("MYCOLUMN") && column2.equals("My Column")
            && column3.equals("MYCO%LUMN")
            && column4.equals("Yes, \"My Column\""), schema + " != JDDELIM%, "
            + table + " != My Table, " + column1 + " != MYCOLUMN, " + column2
            + " != My Column, " + column3 + " != MYCO%LUMN, " + column4
            + " != Yes, \"My Column\"");

    } catch (SQLException e) {
      failed(e, "Unexpected Exception");
    }
  }

  //Test that you can insert a row into a result set
  public void Var045() {
      if (checkJdbc30()) {
	  Statement s = null;
	  ResultSet rs = null;
	  try {
	      s = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				    ResultSet.CONCUR_UPDATABLE);
	      rs = s.executeQuery("SELECT * FROM " + collection3_
				  + dmd.getCatalogSeparator() + table2_);
	      rs.moveToInsertRow();
	      rs.updateInt(1, 9);
	      rs.updateInt(2, 10);
	      rs.updateInt(3, 11);
	      rs.updateInt(4, 12);
	      rs.insertRow();
	      rs.beforeFirst();
	      boolean success = true;
	      rs.first();
	      if (rs.getInt(1) != 1 && rs.getInt(2) != 2 && rs.getInt(3) != 3
		  && rs.getInt(4) != 4)
		  success = false;
	      if (success) {
		  rs.next();
		  if (rs.getInt(1) != 5 && rs.getInt(2) != 6 && rs.getInt(3) != 7
		      && rs.getInt(4) != 8)
		      success = false;
		  if (success) {
		      rs.next();
		      if (rs.getInt(1) != 9 && rs.getInt(2) != 10 && rs.getInt(3) != 11
			  && rs.getInt(4) != 12)
			  success = false;
		      rs.deleteRow();
		  }
	      }
	      assertCondition(success);
	  } catch (Exception e) {
	      failed(e, "Unexpected Exception");
	  } finally {
	      try {
		  rs.close();
		  s.close();
	      } catch (Exception e) {

	      }
	  }
      }
  }
  //Test that you can insert a row into a result set, using descriptors
  public void Var046() {
      if (checkJdbc30()) {
	  Statement s = null;
	  ResultSet rs = null;
	  try {
	      s = c1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				     ResultSet.CONCUR_UPDATABLE);
	      rs = s.executeQuery("SELECT * FROM " + collection3_
				  + dmd.getCatalogSeparator() + table2_);
	      rs.moveToInsertRow();
	      rs.updateInt(1, 9);
	      rs.updateInt(2, 10);
	      rs.updateInt(3, 11);
	      rs.updateInt(4, 12);
	      rs.insertRow();
	      rs.beforeFirst();
	      boolean success = true;
	      rs.first();
	      if (rs.getInt(1) != 1 && rs.getInt(2) != 2 && rs.getInt(3) != 3
		  && rs.getInt(4) != 4)
		  success = false;
	      if (success) {
		  rs.next();
		  if (rs.getInt(1) != 5 && rs.getInt(2) != 6 && rs.getInt(3) != 7
		      && rs.getInt(4) != 8)
		      success = false;
		  if (success) {
		      rs.next();
		      if (rs.getInt(1) != 9 && rs.getInt(2) != 10 && rs.getInt(3) != 11
			  && rs.getInt(4) != 12)
			  success = false;
		      rs.deleteRow();
		  }
	      }
	      assertCondition(success);
	  } catch (Exception e) {
	      failed(e, "Unexpected Exception");
	  } finally {
	      try {
		  rs.close();
		  s.close();
	      } catch (Exception e) {

	      }
	  }
      }
  }

  //Test that you can update a row into a result set
  public void Var047() {
      if (checkJdbc30()) {
	  Statement s = null;
	  ResultSet rs = null;
	  try {
	      s = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				    ResultSet.CONCUR_UPDATABLE);
	      rs = s.executeQuery("SELECT * FROM " + collection3_
				  + dmd.getCatalogSeparator() + table2_);
	      rs.last();
	      rs.updateInt(1, 9);
	      rs.updateInt(2, 10);
	      rs.updateInt(3, 11);
	      rs.updateInt(4, 12);
	      rs.updateRow();
	      rs.beforeFirst();
	      boolean success = true;
	      rs.first();
	      if (rs.getInt(1) != 1 && rs.getInt(2) != 2 && rs.getInt(3) != 3
		  && rs.getInt(4) != 4)
		  success = false;
	      if (success) {
		  rs.next();
		  if (rs.getInt(1) != 9 && rs.getInt(2) != 10 && rs.getInt(3) != 11
		      && rs.getInt(4) != 12)
		      success = false;
		  rs.updateInt(1, 5);
		  rs.updateInt(2, 6);
		  rs.updateInt(3, 7);
		  rs.updateInt(4, 8);
		  rs.updateRow();
	      }
	      assertCondition(success);
	  } catch (Exception e) {
	      failed(e, "Unexpected Exception");
	  } finally {
	      try {
		  rs.close();
		  s.close();
	      } catch (Exception e) {

	      }
	  }
      }
  }
  //Test that you can update a row into a result set, using descriptors
  public void Var048() {
      if (checkJdbc30()) {
	  Statement s = null;
	  ResultSet rs = null;
	  try {
	      s = c1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				     ResultSet.CONCUR_UPDATABLE);
	      rs = s.executeQuery("SELECT * FROM " + collection3_
				  + dmd.getCatalogSeparator() + table2_);
	      rs.last();
	      rs.updateInt(1, 9);
	      rs.updateInt(2, 10);
	      rs.updateInt(3, 11);
	      rs.updateInt(4, 12);
	      rs.updateRow();
	      rs.beforeFirst();
	      boolean success = true;
	      rs.first();
	      if (rs.getInt(1) != 1 && rs.getInt(2) != 2 && rs.getInt(3) != 3
		  && rs.getInt(4) != 4)
		  success = false;
	      if (success) {
		  rs.next();
		  if (rs.getInt(1) != 9 && rs.getInt(2) != 10 && rs.getInt(3) != 11
		      && rs.getInt(4) != 12)
		      success = false;
		  rs.updateInt(1, 5);
		  rs.updateInt(2, 6);
		  rs.updateInt(3, 7);
		  rs.updateInt(4, 8);
		  rs.updateRow();
	      }
	      assertCondition(success);
	  } catch (Exception e) {
	      failed(e, "Unexpected Exception");
	  } finally {
	      try {
		  rs.close();
		  s.close();
	      } catch (Exception e) {

	      }
	  }
      }
  }

  //Test data format, nothing should be returned delimited.
  public void Var049() {
    try {
      Statement s = c.createStatement();
      ResultSet rs = s.executeQuery("SELECT * FROM " + collection3_
          + dmd.getCatalogSeparator() + table3_);
      ResultSetMetaData rsmd = rs.getMetaData();
      String schema = rsmd.getSchemaName(1);
      String table = rsmd.getTableName(1);
      String column1 = rsmd.getColumnName(1);
      String column2 = rsmd.getColumnName(2);
      String column3 = rsmd.getColumnName(3);
      String column4 = rsmd.getColumnName(4);
      rs.close();
      s.close();
      assertCondition(schema.equals("") && table.equals("")
          && column1.equals("MYCOLUMN") && column2.equals("My Column")
          && column3.equals("MYCO%LUMN")
          && column4.equals("Yes, \"My Column\""), schema + " != \"\", "
          + table + " != \"\", " + column1 + " != MYCOLUMN, " + column2
          + " != My Column, " + column3 + " != MYCO%LUMN, " + column4
          + " != Yes, \"My Column\"");
    } catch (SQLException e) {
      failed(e, "Unexpected Exception");
    }
  }

  //Test extended column descriptors, if running to V5R3 or earlier, names should be delimited, except column names
  public void Var050() {
    try {
      Statement s = c1.createStatement();
      ResultSet rs = s.executeQuery("SELECT * FROM " + collection3_
          + dmd.getCatalogSeparator() + table3_);
      ResultSetMetaData rsmd = rs.getMetaData();
      String schema = rsmd.getSchemaName(1);
      String table = rsmd.getTableName(1);
      String column1 = rsmd.getColumnName(1);
      String column2 = rsmd.getColumnName(2);
      String column3 = rsmd.getColumnName(3);
      String column4 = rsmd.getColumnName(4);
      rs.close();
      s.close();
        assertCondition(schema.equals(collection3NoDelim_) && table.equals("My%$Table")
            && column1.equals("MYCOLUMN") && column2.equals("My Column")
            && column3.equals("MYCO%LUMN")
            && column4.equals("Yes, \"My Column\""), schema + " != JDDELIM5, "
            + table + " != My%$Table, " + column1 + " != MYCOLUMN, " + column2
            + " != My Column, " + column3 + " != MYCO%LUMN, " + column4
            + " != Yes, \"My Column\"");

    } catch (SQLException e) {
      failed(e, "Unexpected Exception");
    }
  }

  //Test that you can insert a row into a result set
  public void Var051() {
      if (checkJdbc30()) {
	  Statement s = null;
	  ResultSet rs = null;
	  try {
	      s = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				    ResultSet.CONCUR_UPDATABLE);
	      rs = s.executeQuery("SELECT * FROM " + collection3_
				  + dmd.getCatalogSeparator() + table3_);
	      rs.moveToInsertRow();
	      rs.updateInt(1, 9);
	      rs.updateInt(2, 10);
	      rs.updateInt(3, 11);
	      rs.updateInt(4, 12);
	      rs.insertRow();
	      rs.beforeFirst();
	      boolean success = true;
	      rs.first();
	      if (rs.getInt(1) != 1 && rs.getInt(2) != 2 && rs.getInt(3) != 3
		  && rs.getInt(4) != 4)
		  success = false;
	      if (success) {
		  rs.next();
		  if (rs.getInt(1) != 5 && rs.getInt(2) != 6 && rs.getInt(3) != 7
		      && rs.getInt(4) != 8)
		      success = false;
		  if (success) {
		      rs.next();
		      if (rs.getInt(1) != 9 && rs.getInt(2) != 10 && rs.getInt(3) != 11
			  && rs.getInt(4) != 12)
			  success = false;
		      rs.deleteRow();
		  }
	      }
	      assertCondition(success);
	  } catch (Exception e) {
	      failed(e, "Unexpected Exception");
	  } finally {
	      try {
		  rs.close();
		  s.close();
	      } catch (Exception e) {

	      }
	  }
      }
  }
  //Test that you can insert a row into a result set, using descriptors
  public void Var052() {
      if (checkJdbc30()) {
	  Statement s = null;
	  ResultSet rs = null;
	  try {
	      s = c1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				     ResultSet.CONCUR_UPDATABLE);
	      rs = s.executeQuery("SELECT * FROM " + collection3_
				  + dmd.getCatalogSeparator() + table3_);
	      rs.moveToInsertRow();
	      rs.updateInt(1, 9);
	      rs.updateInt(2, 10);
	      rs.updateInt(3, 11);
	      rs.updateInt(4, 12);
	      rs.insertRow();
	      rs.beforeFirst();
	      boolean success = true;
	      rs.first();
	      if (rs.getInt(1) != 1 && rs.getInt(2) != 2 && rs.getInt(3) != 3
		  && rs.getInt(4) != 4)
		  success = false;
	      if (success) {
		  rs.next();
		  if (rs.getInt(1) != 5 && rs.getInt(2) != 6 && rs.getInt(3) != 7
		      && rs.getInt(4) != 8)
		      success = false;
		  if (success) {
		      rs.next();
		      if (rs.getInt(1) != 9 && rs.getInt(2) != 10 && rs.getInt(3) != 11
			  && rs.getInt(4) != 12)
			  success = false;
		      rs.deleteRow();
		  }
	      }
	      assertCondition(success);
	  } catch (Exception e) {
	      failed(e, "Unexpected Exception");
	  } finally {
	      try {
		  rs.close();
		  s.close();
	      } catch (Exception e) {

	      }
	  }
      }
  }

  //Test that you can update a row into a result set
  public void Var053() {
      if (checkJdbc30()) {
	  Statement s = null;
	  ResultSet rs = null;
	  try {
	      s = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				    ResultSet.CONCUR_UPDATABLE);
	      rs = s.executeQuery("SELECT * FROM " + collection3_
				  + dmd.getCatalogSeparator() + table3_);
	      rs.last();
	      rs.updateInt(1, 9);
	      rs.updateInt(2, 10);
	      rs.updateInt(3, 11);
	      rs.updateInt(4, 12);
	      rs.updateRow();
	      rs.beforeFirst();
	      boolean success = true;
	      rs.first();
	      if (rs.getInt(1) != 1 && rs.getInt(2) != 2 && rs.getInt(3) != 3
		  && rs.getInt(4) != 4)
		  success = false;
	      if (success) {
		  rs.next();
		  if (rs.getInt(1) != 9 && rs.getInt(2) != 10 && rs.getInt(3) != 11
		      && rs.getInt(4) != 12)
		      success = false;
		  rs.updateInt(1, 5);
		  rs.updateInt(2, 6);
		  rs.updateInt(3, 7);
		  rs.updateInt(4, 8);
		  rs.updateRow();
	      }
	      assertCondition(success);
	  } catch (Exception e) {
	      failed(e, "Unexpected Exception");
	  } finally {
	      try {
		  rs.close();
		  s.close();
	      } catch (Exception e) {

	      }
	  }
      }
  }

  //Test that you can update a row into a result set, using descriptors
  public void Var054() {
      if (checkJdbc30()) {
	  Statement s = null;
	  ResultSet rs = null;
	  try {
	      s = c1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				     ResultSet.CONCUR_UPDATABLE);
	      rs = s.executeQuery("SELECT * FROM " + collection3_
				  + dmd.getCatalogSeparator() + table3_);
	      rs.last();
	      rs.updateInt(1, 9);
	      rs.updateInt(2, 10);
	      rs.updateInt(3, 11);
	      rs.updateInt(4, 12);
	      rs.updateRow();
	      rs.beforeFirst();
	      boolean success = true;
	      rs.first();
	      if (rs.getInt(1) != 1 && rs.getInt(2) != 2 && rs.getInt(3) != 3
		  && rs.getInt(4) != 4)
		  success = false;
	      if (success) {
		  rs.next();
		  if (rs.getInt(1) != 9 && rs.getInt(2) != 10 && rs.getInt(3) != 11
		      && rs.getInt(4) != 12)
		      success = false;
		  rs.updateInt(1, 5);
		  rs.updateInt(2, 6);
		  rs.updateInt(3, 7);
		  rs.updateInt(4, 8);
		  rs.updateRow();
	      }
	      assertCondition(success);
	  } catch (Exception e) {
	      failed(e, "Unexpected Exception");
	  } finally {
	      try {
		  rs.close();
		  s.close();
	      } catch (Exception e) {

	      }
	  }
      }
  }

  //verify you can get the column by column name
  public void Var055() {
    Statement s = null;
    ResultSet rs = null;
    try {
      s = c.createStatement();
      rs = s.executeQuery("SELECT * FROM " + collection1_
          + dmd.getCatalogSeparator() + table1_);
      rs.next();
      assertCondition(rs.getInt("MYCOLUMN") == 1);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      try {
        rs.close();
        s.close();
      } catch (Exception e) {
      }
    }
  }

  //verify you can get the column by column name
  public void Var056() {
    Statement s = null;
    ResultSet rs = null;
    try {
      s = c.createStatement();
      rs = s.executeQuery("SELECT * FROM " + collection1_
          + dmd.getCatalogSeparator() + table1_);
      rs.next();
      assertCondition(rs.getInt("My Column") == 2);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      try {
        rs.close();
        s.close();
      } catch (Exception e) {
      }
    }
  }

  //verify you can get the column by column name
  public void Var057() {
    Statement s = null;
    ResultSet rs = null;
    try {
      s = c.createStatement();
      rs = s.executeQuery("SELECT * FROM " + collection1_
          + dmd.getCatalogSeparator() + table1_);
      rs.next();
      assertCondition(rs.getInt("MYCO%LUMN") == 3);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      try {
        rs.close();
        s.close();
      } catch (Exception e) {
      }
    }
  }

  //verify you can get the column by column name
  public void Var058() {
    Statement s = null;
    ResultSet rs = null;
    try {
      s = c.createStatement();
      rs = s.executeQuery("SELECT * FROM " + collection1_
          + dmd.getCatalogSeparator() + table1_);
      rs.next();
      assertCondition(rs.getInt("Yes, \"My Column\"") == 4);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      try {
        rs.close();
        s.close();
      } catch (Exception e) {
      }
    }
  }

  public void Var059() {
    CallableStatement cs = null;
    try {
      cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
          + proc1_ + "(?,?,?,?)");
      cs.registerOutParameter(1, Types.INTEGER);
      cs.registerOutParameter(2, Types.INTEGER);
      cs.registerOutParameter(3, Types.INTEGER);
      cs.registerOutParameter(4, Types.INTEGER);
      cs.setInt(1, 1);
      cs.setInt(2, 2);
      cs.setInt(3, 3);
      cs.setInt(4, 4);
      cs.execute();
      assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
          && cs.getInt(3) == 4 && cs.getInt(4) == 5);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      try {
        cs.close();
      } catch (Exception e) {
      }
    }
  }

  public void Var060() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
            + proc1_ + "(?,?,?,?)");
        cs.registerOutParameter("PARAM1", Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var061() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
            + proc1_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter("Param 1", Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var062() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
            + proc1_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter("PARAM%1", Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var063() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
            + proc1_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter("My \"Param\"", Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var064() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
            + proc1_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt("PARAM1", 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var065() {
    if (checkJdbc30()) {

      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
            + proc1_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt("Param 1", 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var066() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
            + proc1_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt("PARAM%1", 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var067() {
    if (checkJdbc30()) {

      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
            + proc1_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt("My \"Param\"", 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var068() {
    CallableStatement cs = null;
    try {
      cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
          + proc2_ + "(?,?,?,?)");
      cs.registerOutParameter(1, Types.INTEGER);
      cs.registerOutParameter(2, Types.INTEGER);
      cs.registerOutParameter(3, Types.INTEGER);
      cs.registerOutParameter(4, Types.INTEGER);
      cs.setInt(1, 1);
      cs.setInt(2, 2);
      cs.setInt(3, 3);
      cs.setInt(4, 4);
      cs.execute();
      assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
          && cs.getInt(3) == 4 && cs.getInt(4) == 5);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      try {
        cs.close();
      } catch (Exception e) {
      }
    }
  }

  public void Var069() {
    if (checkJdbc30()) {

      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
            + proc2_ + "(?,?,?,?)");
        cs.registerOutParameter("PARAM1", Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var070() {
    if (checkJdbc30()) {

      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
            + proc2_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter("Param 1", Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var071() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
            + proc2_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter("PARAM%1", Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var072() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
            + proc2_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter("My \"Param\"", Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var073() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
            + proc2_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt("PARAM1", 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var074() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
            + proc2_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt("Param 1", 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var075() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
            + proc2_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt("PARAM%1", 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var076() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
            + proc2_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt("My \"Param\"", 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var077() {
    CallableStatement cs = null;
    try {
      cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
          + proc3_ + "(?,?,?,?)");
      cs.registerOutParameter(1, Types.INTEGER);
      cs.registerOutParameter(2, Types.INTEGER);
      cs.registerOutParameter(3, Types.INTEGER);
      cs.registerOutParameter(4, Types.INTEGER);
      cs.setInt(1, 1);
      cs.setInt(2, 2);
      cs.setInt(3, 3);
      cs.setInt(4, 4);
      cs.execute();
      assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
          && cs.getInt(3) == 4 && cs.getInt(4) == 5);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      try {
        cs.close();
      } catch (Exception e) {
      }
    }
  }

  public void Var078() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
            + proc3_ + "(?,?,?,?)");
        cs.registerOutParameter("PARAM1", Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var079() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
            + proc3_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter("Param 1", Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var080() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
            + proc3_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter("PARAM%1", Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var081() {
    CallableStatement cs = null;
    if (checkJdbc30()) {
      try {
        cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
            + proc3_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter("My \"Param\"", Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var082() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
            + proc3_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt("PARAM1", 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var083() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
            + proc3_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt("Param 1", 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var084() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
            + proc3_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt("PARAM%1", 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var085() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
            + proc3_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt("My \"Param\"", 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var086() {
    CallableStatement cs = null;
    try {
      cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
          + proc1_ + "(?,?,?,?)");
      cs.registerOutParameter(1, Types.INTEGER);
      cs.registerOutParameter(2, Types.INTEGER);
      cs.registerOutParameter(3, Types.INTEGER);
      cs.registerOutParameter(4, Types.INTEGER);
      cs.setInt(1, 1);
      cs.setInt(2, 2);
      cs.setInt(3, 3);
      cs.setInt(4, 4);
      cs.execute();
      assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
          && cs.getInt(3) == 4 && cs.getInt(4) == 5);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      try {
        cs.close();
      } catch (Exception e) {
      }
    }
  }

  public void Var087() {
    if (checkJdbc30()) {

      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc1_ + "(?,?,?,?)");
        cs.registerOutParameter("PARAM1", Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var088() {
    if (checkJdbc30()) {

      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc1_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter("Param 1", Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var089() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc1_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter("PARAM%1", Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var090() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc1_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter("My \"Param\"", Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var091() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc1_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt("PARAM1", 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var092() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc1_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt("Param 1", 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var093() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc1_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt("PARAM%1", 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var094() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc1_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt("My \"Param\"", 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var095() {
    CallableStatement cs = null;
    try {
      cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
          + proc2_ + "(?,?,?,?)");
      cs.registerOutParameter(1, Types.INTEGER);
      cs.registerOutParameter(2, Types.INTEGER);
      cs.registerOutParameter(3, Types.INTEGER);
      cs.registerOutParameter(4, Types.INTEGER);
      cs.setInt(1, 1);
      cs.setInt(2, 2);
      cs.setInt(3, 3);
      cs.setInt(4, 4);
      cs.execute();
      assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
          && cs.getInt(3) == 4 && cs.getInt(4) == 5);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      try {
        cs.close();
      } catch (Exception e) {
      }
    }
  }

  public void Var096() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc2_ + "(?,?,?,?)");
        cs.registerOutParameter("PARAM1", Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var097() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc2_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter("Param 1", Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var098() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc2_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter("PARAM%1", Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var099() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc2_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter("My \"Param\"", Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var100() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc2_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt("PARAM1", 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var101() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc2_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt("Param 1", 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var102() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc2_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt("PARAM%1", 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var103() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc2_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt("My \"Param\"", 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var104() {
    CallableStatement cs = null;
    try {
      cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
          + proc3_ + "(?,?,?,?)");
      cs.registerOutParameter(1, Types.INTEGER);
      cs.registerOutParameter(2, Types.INTEGER);
      cs.registerOutParameter(3, Types.INTEGER);
      cs.registerOutParameter(4, Types.INTEGER);
      cs.setInt(1, 1);
      cs.setInt(2, 2);
      cs.setInt(3, 3);
      cs.setInt(4, 4);
      cs.execute();
      assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
          && cs.getInt(3) == 4 && cs.getInt(4) == 5);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      try {
        cs.close();
      } catch (Exception e) {
      }
    }
  }

  public void Var105() {
    if (checkJdbc30()) {

      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc3_ + "(?,?,?,?)");
        cs.registerOutParameter("PARAM1", Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var106() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc3_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter("Param 1", Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var107() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc3_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter("PARAM%1", Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var108() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc3_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter("My \"Param\"", Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var109() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc3_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt("PARAM1", 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var110() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc3_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt("Param 1", 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var111() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc3_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt("PARAM%1", 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var112() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc3_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt("My \"Param\"", 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var113() {
    CallableStatement cs = null;
    try {
      cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
          + proc1_ + "(?,?,?,?)");
      cs.registerOutParameter(1, Types.INTEGER);
      cs.registerOutParameter(2, Types.INTEGER);
      cs.registerOutParameter(3, Types.INTEGER);
      cs.registerOutParameter(4, Types.INTEGER);
      cs.setInt(1, 1);
      cs.setInt(2, 2);
      cs.setInt(3, 3);
      cs.setInt(4, 4);
      cs.execute();
      assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
          && cs.getInt(3) == 4 && cs.getInt(4) == 5);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      try {
        cs.close();
      } catch (Exception e) {
      }
    }
  }

  public void Var114() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
            + proc1_ + "(?,?,?,?)");
        cs.registerOutParameter("PARAM1", Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var115() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
            + proc1_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter("Param 1", Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var116() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
            + proc1_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter("PARAM%1", Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var117() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
            + proc1_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter("My \"Param\"", Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var118() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
            + proc1_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt("PARAM1", 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var119() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
            + proc1_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt("Param 1", 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var120() {
    CallableStatement cs = null;
    if (checkJdbc30()) {
      try {
        cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
            + proc1_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt("PARAM%1", 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var121() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
            + proc1_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt("My \"Param\"", 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var122() {
    CallableStatement cs = null;
    try {
      cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
          + proc2_ + "(?,?,?,?)");
      cs.registerOutParameter(1, Types.INTEGER);
      cs.registerOutParameter(2, Types.INTEGER);
      cs.registerOutParameter(3, Types.INTEGER);
      cs.registerOutParameter(4, Types.INTEGER);
      cs.setInt(1, 1);
      cs.setInt(2, 2);
      cs.setInt(3, 3);
      cs.setInt(4, 4);
      cs.execute();
      assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
          && cs.getInt(3) == 4 && cs.getInt(4) == 5);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      try {
        cs.close();
      } catch (Exception e) {
      }
    }
  }

  public void Var123() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
            + proc2_ + "(?,?,?,?)");
        cs.registerOutParameter("PARAM1", Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var124() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
            + proc2_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter("Param 1", Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var125() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
            + proc2_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter("PARAM%1", Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var126() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
            + proc2_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter("My \"Param\"", Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var127() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
            + proc2_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt("PARAM1", 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var128() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
            + proc2_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt("Param 1", 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var129() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
            + proc2_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt("PARAM%1", 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }

  }

  public void Var130() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
            + proc2_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt("My \"Param\"", 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var131() {
    CallableStatement cs = null;
    try {
      cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
          + proc3_ + "(?,?,?,?)");
      cs.registerOutParameter(1, Types.INTEGER);
      cs.registerOutParameter(2, Types.INTEGER);
      cs.registerOutParameter(3, Types.INTEGER);
      cs.registerOutParameter(4, Types.INTEGER);
      cs.setInt(1, 1);
      cs.setInt(2, 2);
      cs.setInt(3, 3);
      cs.setInt(4, 4);
      cs.execute();
      assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
          && cs.getInt(3) == 4 && cs.getInt(4) == 5);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      try {
        cs.close();
      } catch (Exception e) {
      }
    }
  }

  public void Var132() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
            + proc3_ + "(?,?,?,?)");
        cs.registerOutParameter("PARAM1", Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var133() {
    CallableStatement cs = null;
    if (checkJdbc30()) {
      try {
        cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
            + proc3_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter("Param 1", Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }

  }

  public void Var134() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
            + proc3_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter("PARAM%1", Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var135() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
            + proc3_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter("My \"Param\"", Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var136() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
            + proc3_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt("PARAM1", 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var137() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
            + proc3_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt("Param 1", 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var138() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
            + proc3_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt("PARAM%1", 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var139() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
            + proc3_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt("My \"Param\"", 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var140() {
    CallableStatement cs = null;
    if (checkJdbc30()) {
      try {
        cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
            + proc1_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt("PARAM1") == 2 && cs.getInt("Param 1") == 3
            && cs.getInt("PARAM%1") == 4 && cs.getInt("My \"Param\"") == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var141() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
            + proc2_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt("PARAM1") == 2 && cs.getInt("Param 1") == 3
            && cs.getInt("PARAM%1") == 4 && cs.getInt("My \"Param\"") == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var142() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
            + proc3_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt("PARAM1") == 2 && cs.getInt("Param 1") == 3
            && cs.getInt("PARAM%1") == 4 && cs.getInt("My \"Param\"") == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var143() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc1_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt("PARAM1") == 2 && cs.getInt("Param 1") == 3
            && cs.getInt("PARAM%1") == 4 && cs.getInt("My \"Param\"") == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var144() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc2_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt("PARAM1") == 2 && cs.getInt("Param 1") == 3
            && cs.getInt("PARAM%1") == 4 && cs.getInt("My \"Param\"") == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var145() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc3_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt("PARAM1") == 2 && cs.getInt("Param 1") == 3
            && cs.getInt("PARAM%1") == 4 && cs.getInt("My \"Param\"") == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var146() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
            + proc1_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt("PARAM1") == 2 && cs.getInt("Param 1") == 3
            && cs.getInt("PARAM%1") == 4 && cs.getInt("My \"Param\"") == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var147() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
            + proc2_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt("PARAM1") == 2 && cs.getInt("Param 1") == 3
            && cs.getInt("PARAM%1") == 4 && cs.getInt("My \"Param\"") == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var148() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
            + proc3_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt("PARAM1") == 2 && cs.getInt("Param 1") == 3
            && cs.getInt("PARAM%1") == 4 && cs.getInt("My \"Param\"") == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var149() {
    CallableStatement cs = null;
    try {
      cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
          + proc4_ + "(?,?,?,?)");
      cs.registerOutParameter(1, Types.INTEGER);
      cs.registerOutParameter(2, Types.INTEGER);
      cs.registerOutParameter(3, Types.INTEGER);
      cs.registerOutParameter(4, Types.INTEGER);
      cs.setInt(1, 1);
      cs.setInt(2, 2);
      cs.setInt(3, 3);
      cs.setInt(4, 4);
      cs.execute();
      assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
          && cs.getInt(3) == 4 && cs.getInt(4) == 5);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      try {
        cs.close();
      } catch (Exception e) {
      }
    }
  }

  public void Var150() {
    CallableStatement cs = null;
    if (checkJdbc30()) {
      try {
        cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
            + proc4_ + "(?,?,?,?)");
        cs.registerOutParameter("PARAM1", Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var151() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
            + proc4_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter("Param 1", Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var152() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
            + proc4_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter("PARAM%1", Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var153() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
            + proc4_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter("My \"Param\"", Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var154() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
            + proc4_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt("PARAM1", 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var155() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
            + proc4_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt("Param 1", 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var156() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
            + proc4_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt("PARAM%1", 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var157() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
            + proc4_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt("My \"Param\"", 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var158() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc4_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var159() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc4_ + "(?,?,?,?)");
        cs.registerOutParameter("PARAM1", Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var160() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc4_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter("Param 1", Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var161() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc4_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter("PARAM%1", Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var162() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc4_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter("My \"Param\"", Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var163() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc4_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt("PARAM1", 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var164() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc4_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt("Param 1", 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var165() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc4_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt("PARAM%1", 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var166() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc4_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt("My \"Param\"", 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var167() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
            + proc4_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }

  }

  public void Var168() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
            + proc4_ + "(?,?,?,?)");
        cs.registerOutParameter("PARAM1", Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var169() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
            + proc4_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter("Param 1", Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var170() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
            + proc4_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter("PARAM%1", Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var171() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
            + proc4_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter("My \"Param\"", Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var172() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
            + proc4_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt("PARAM1", 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var173() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
            + proc4_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt("Param 1", 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var174() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
            + proc4_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt("PARAM%1", 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var175() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
            + proc4_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt("My \"Param\"", 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var176() {
    if (checkJdbc30()) {

      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection1_ + dmd.getCatalogSeparator()
            + proc4_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt("PARAM1") == 2 && cs.getInt("Param 1") == 3
            && cs.getInt("PARAM%1") == 4 && cs.getInt("My \"Param\"") == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var177() {
    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection2_ + dmd.getCatalogSeparator()
            + proc4_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt("PARAM1") == 2 && cs.getInt("Param 1") == 3
            && cs.getInt("PARAM%1") == 4 && cs.getInt("My \"Param\"") == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public void Var178() {
   if (checkJdbc30()) {
    CallableStatement cs = null;
    try {
      cs = c.prepareCall("CALL " + collection3_ + dmd.getCatalogSeparator()
          + proc4_ + "(?,?,?,?)");
      cs.registerOutParameter(1, Types.INTEGER);
      cs.registerOutParameter(2, Types.INTEGER);
      cs.registerOutParameter(3, Types.INTEGER);
      cs.registerOutParameter(4, Types.INTEGER);
      cs.setInt(1, 1);
      cs.setInt(2, 2);
      cs.setInt(3, 3);
      cs.setInt(4, 4);
      cs.execute();
      assertCondition(cs.getInt("PARAM1") == 2 && cs.getInt("Param 1") == 3
          && cs.getInt("PARAM%1") == 4 && cs.getInt("My \"Param\"") == 5);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      try {
        cs.close();
      } catch (Exception e) {
      }
    }
  }
  }

  public void Var179() {
    CallableStatement cs = null;
    try {
      cs = c.prepareCall("CALL " + proc3_ + "(?,?,?,?)"); //Should fail since proc3_ is not in USERNAME library
      failed("Didn't throw expected exception.");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    } finally {
      try {
        cs.close();
      } catch (Exception e) {
      }
    }
  }

  public void Var180() {
    Connection c = null;
    CallableStatement cs = null;
    try {
      c = testDriver_.getConnection(baseURL_ + ";libraries=" + collection2_,
          userId_, encryptedPassword_);
      cs = c.prepareCall("CALL " + proc3_ + "(?,?,?,?)");
      //should be able to prepare the call, default schema is collection2_
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception ");
    } finally {
      try {
        cs.close();
        c.close();
      } catch (Exception e) {
      }
    }
  }

  public void Var181() {
    Connection c = null;
    CallableStatement cs = null;
    try {
      c = testDriver_.getConnection(baseURL_ + ";libraries=" + collection1_,
          userId_, encryptedPassword_);
      cs = c.prepareCall("CALL " + proc3_ + "(?,?,?,?)");
      //should be able to prepare the call, default schema is collection1_
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception ");
    } finally {
      try {
        cs.close();
        c.close();
      } catch (Exception e) {
      }
    }
  }

  public void Var182() {
    Connection c = null;
    CallableStatement cs = null;
    try {
      c = testDriver_.getConnection(baseURL_ + ";libraries=" + collection3_,
          userId_, encryptedPassword_);
      cs = c.prepareCall("CALL " + proc3_ + "(?,?,?,?)");
      //should be able to prepare the call, default schema is collection3_
      succeeded(); // TBD - Failing due to issue 22733, as of 2005-10-26
    } catch (Exception e) {
      failed(e, "Unexpected Exception ");
    } finally {
      try {
        cs.close();
        c.close();
      } catch (Exception e) {
      }
    }
  }

  public void Var183() {
    Connection c = null;
    CallableStatement cs = null;
    try {
      c = testDriver_.getConnection(baseURL_ + ";naming=system;libraries="
          + collection2_, userId_, encryptedPassword_);
      cs = c.prepareCall("CALL " + proc3_ + "(?,?,?,?)");
      //should be able to prepare the call, no default schema, should be found in collection2_
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception ");
    } finally {
      try {
        cs.close();
        c.close();
      } catch (Exception e) {
      }
    }
  }

  public void Var184() {
    Connection c = null;
    CallableStatement cs = null;
    try {
      c = testDriver_.getConnection(baseURL_ + ";naming=system;libraries="
          + collection1_, userId_, encryptedPassword_);
      cs = c.prepareCall("CALL " + proc3_ + "(?,?,?,?)");
      //should be able to prepare the call, no default schema, should be found in collection1_
      succeeded(); // TBD - Failing due to issue 22733, as of 2005-10-26
    } catch (Exception e) {
      failed(e, "Unexpected Exception ");
    } finally {
      try {
        cs.close();
        c.close();
      } catch (Exception e) {
      }
    }
  }

  public void Var185() {
    Connection c = null;
    CallableStatement cs = null;
    try {
      c = testDriver_.getConnection(baseURL_ + ";naming=system;libraries="
          + collection3_, userId_, encryptedPassword_);
      cs = c.prepareCall("CALL " + proc3_ + "(?,?,?,?)");
      //should be able to prepare the call, no default schema, should be found in collection3_
      succeeded(); // TBD - Failing due to issue 22733, as of 2005-10-26
    } catch (Exception e) {
      failed(e, "Unexpected Exception ");
    } finally {
      try {
        cs.close();
        c.close();
      } catch (Exception e) {
      }
    }
  }

  public void Var186() {
    Connection c = null;
    CallableStatement cs = null;
    try {
      c = testDriver_.getConnection(baseURL_ + ";naming=system;libraries=QGPL,"
          + collection2_, userId_, encryptedPassword_);
      cs = c.prepareCall("CALL " + proc3_ + "(?,?,?,?)");
      //should be able to prepare the call, no default schema, should be found in collection2_
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception ");
    } finally {
      try {
        cs.close();
        c.close();
      } catch (Exception e) {
      }
    }
  }

  public void Var187() {

    if (checkJdbc30()) {
      Connection c = null;
      CallableStatement cs = null;
      try {
        c = testDriver_.getConnection(baseURL_ + ";libraries=" + collection2_,
            userId_, encryptedPassword_);
        cs = c.prepareCall("CALL " + proc3_ + "(?,?,?,?)");
        //should be able to prepare the call, no default schema, should be found in collection1_
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt("PARAM1", 1);
        cs.setInt(2, 2);
        cs.setInt(3, 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception ");
      } finally {
        try {
          cs.close();
          c.close();
        } catch (Exception e) {
        }
      }
    }
  }

  // Test the effect's of '.' in a delimited collection and procedure name.
  // This test was written specifically for native JDBC's DB2SQLTokenizer coverage. @A1A
  public void Var188() {
    if (getDriver() == JDTestDriver.DRIVER_NATIVE &&
	getRelease() <= JDTestDriver.RELEASE_V7R1M0) {
	notApplicable("Native driver gets internal error in find parameter ");
	return; 
    } 

    if (checkJdbc30()) {
      CallableStatement cs = null;
      try {
        cs = c.prepareCall("CALL " + collection4_ + dmd.getCatalogSeparator()
            + proc5_ + "(?,?,?,?)");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.INTEGER);
        cs.registerOutParameter(4, Types.INTEGER);
        cs.setInt(1, 1);
        cs.setInt(2, 2);
        cs.setInt("PARAM%1", 3);
        cs.setInt(4, 4);
        cs.execute();
        assertCondition(cs.getInt(1) == 2 && cs.getInt(2) == 3
            && cs.getInt(3) == 4 && cs.getInt(4) == 5);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        try {
          cs.close();
        } catch (Exception e) {
        }
      }
    }
  }
}


