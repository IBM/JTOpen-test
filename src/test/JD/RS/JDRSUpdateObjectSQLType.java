///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSUpdateObjectSQLType.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.RS;

import com.ibm.as400.access.AS400;
// import com.ibm.as400.access.AS400JDBCDataSource;

import test.JDLobTest;
import test.JDRSTest;
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;
import test.JVMInfo;
import test.JDLobTest.JDTestBlob;
import test.JDLobTest.JDTestClob;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Hashtable;



/**
Testcase JDRSUpdateObjectSQLType.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>updateObject(SQLType)
</ul>

Note:  The current implementation ignores SQLType. 
**/
public class JDRSUpdateObjectSQLType
extends JDTestcase {


    // Private data.
    private static final String key_            = "JDRSUpdateObjectSQLType";
    private static String select_         = "SELECT * FROM "
                                                  + JDRSTest.RSTEST_UPDATE;

    private Statement           statement_;
    private Statement           statement2_;
    private Statement           statement2f_;
    private ResultSet           rs_ = null;


    private Connection          connectionCommaSeparator_;
    private Statement           statementCommaSeparator_;
    private Statement           statementCommaSeparator2_;
    private Statement           statementCommaSeparator2f_; 
    private ResultSet           rsCommaSeparator_ = null ;

    private Connection          connection3_;
    private Statement           statement3_;
    private Statement           statement4_;
    private ResultSet           rs3_;

    private boolean tableUpdated = false; 


/**
Constructor.
**/
    public JDRSUpdateObjectSQLType (AS400 systemObject,
                             Hashtable namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             
                             String password)
    {
        super (systemObject, "JDRSUpdateObjectSQLType",
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

        if (isJdbc20 ()) {
            String url = baseURL_
            // String url = "jdbc:as400://" + systemObject_.getSystemName()
                         
                         
                         + ";data truncation=true";
            connection_ = testDriver_.getConnection (url + ";lob threshold=30000",systemObject_.getUserId(), encryptedPassword_);
            connection_.setAutoCommit(false); // @C1A



            // Force LOB locators.
            connection3_ = testDriver_.getConnection (url + ";lob threshold=0",systemObject_.getUserId(), encryptedPassword_);
            connection3_.setAutoCommit(false); // @C1A
            statement3_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                       ResultSet.CONCUR_UPDATABLE);
            statement4_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                       ResultSet.CONCUR_READ_ONLY);

            rs3_ = statement3_.executeQuery (select_ + " FOR UPDATE");




            connectionCommaSeparator_ = testDriver_.getConnection (url + ";lob threshold=30000;decimal separator=,");
            connectionCommaSeparator_.setAutoCommit(false); // @C1A


        }
    }




    protected void setupRs() {
	if (rsCommaSeparator_ != null) {
          try { 
	    rsCommaSeparator_.close ();
	    statementCommaSeparator_.close ();
	    statementCommaSeparator2_.close ();
	    statementCommaSeparator2f_.close ();
	    connectionCommaSeparator_.commit();
	    rsCommaSeparator_ = null;
	    statementCommaSeparator_= null;
	    statementCommaSeparator2_= null;
	    statementCommaSeparator2f_= null;
	  } catch (SQLException e) { 
	    e.printStackTrace(); 
	  }
	} 
	if (rs_ == null) {
          try { 

	      

            statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                      ResultSet.CONCUR_UPDATABLE);
            statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                       ResultSet.CONCUR_READ_ONLY);
            statement2f_ = connection_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY);

	    if (!tableUpdated) {

		statement_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_UPDATE
					  + " (C_KEY) VALUES ('DUMMY_ROW')");
		statement_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_UPDATE
					  + " (C_KEY) VALUES ('DUMMY_ROW2')");
		statement_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_UPDATE
					  + " (C_KEY) VALUES ('" + key_ + "')");
		tableUpdated=true; 
	    }
            rs_ = statement_.executeQuery (select_ + " FOR UPDATE");



          } catch (SQLException e) { 
            e.printStackTrace(); 
          }

	} 
    } 


    protected void setupRsCommaSeparator() {
	if (rs_ != null) {
	  try { 
	    rs_.close ();
	    statement_.close ();
	    statement2_.close ();
	    statement2f_.close ();
	    connection_.commit();
	    rs_ = null;
	    statement_= null;
	    statement2_= null;
	    statement2f_= null;
	     } catch (SQLException e) { 
	            e.printStackTrace(); 
	          }

	} 
	if (rsCommaSeparator_ == null) {
	  try { 
	    statementCommaSeparator_ = connectionCommaSeparator_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
						      ResultSet.CONCUR_UPDATABLE);
	    statementCommaSeparator2_ = connectionCommaSeparator_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
						       ResultSet.CONCUR_READ_ONLY);
	    statementCommaSeparator2f_ = connectionCommaSeparator_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
							ResultSet.CONCUR_READ_ONLY);

	    if (!tableUpdated) {
		tableUpdated = true; 
		statementCommaSeparator_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_UPDATE
							+ " (C_KEY) VALUES ('DUMMY_ROW')");
		statementCommaSeparator_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_UPDATE
							+ " (C_KEY) VALUES ('DUMMY_ROW1')");
		statementCommaSeparator_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_UPDATE
							+ " (C_KEY) VALUES ('" + key_ + "')");
	    }
	    rsCommaSeparator_ = statementCommaSeparator_.executeQuery (select_ + " FOR UPDATE");
          } catch (SQLException e) { 
            e.printStackTrace(); 
          }

	} 
    } 



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup() throws Exception {
    if (isJdbc20()) {
      rs3_.close();
      statement4_.close();
      statement3_.close();
      connection3_.commit(); // @C1A
      connection3_.close();

      if (rs_ != null) {
        rs_.close();
        statement2_.close();
        statement2f_.close();
        statement_.close();
      }
      connection_.commit(); // @C1A
      connection_.close();
      if (rsCommaSeparator_ != null) {
        rsCommaSeparator_.close();
        statementCommaSeparator2_.close();
        statementCommaSeparator2f_.close();
        statementCommaSeparator_.close();
      }
      connectionCommaSeparator_.commit(); // @C1A
      connectionCommaSeparator_.close();
    }
  }


    protected void cleanupConnections() throws Exception {
    if (isJdbc20()) {
      connection3_.commit(); // @C1A
      connection3_.close();

      connection_.commit(); // @C1A
      connection_.close();
      connectionCommaSeparator_.commit(); // @C1A
      connectionCommaSeparator_.close();
    }
  }



  /*
   * Create the SQLType corresponding to Type.
   */
  private Object getSQLType(int typesNumber) throws Exception {
    return JDReflectionUtil.callStaticMethod_O("java.sql.JDBCType", "valueOf",
        typesNumber);
  }

/**
updateObject() - Should throw exception when the result set is
closed.
**/
    public void Var001()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                           ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = s.executeQuery ("SELECT * FROM "
                                               + JDRSTest.RSTEST_UPDATE + " FOR UPDATE");
                rs.next ();
                rs.close ();
                JDReflectionUtil.callMethod_V(rs,"updateObject","C_VARCHAR_50", "Maine", getSQLType(Types.VARCHAR));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateObject() - Should throw exception when the result set is
not updatable.
**/
    public void Var002()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                           ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = s.executeQuery ("SELECT * FROM "
                                               + JDRSTest.RSTEST_UPDATE);
                rs.next ();
                JDReflectionUtil.callMethod_V(rs,"updateObject","C_VARCHAR_50", "New Hampshire", getSQLType(Types.VARCHAR));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateObject() - Should throw exception when cursor is not pointing
to a row.
**/
    public void Var003()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, null);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_VARCHAR_50", "Vermont", getSQLType(Types.VARCHAR));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateObject() - Should throw an exception when the column
is an invalid index.
**/
    public void Var004()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject",100, "New York", getSQLType(Types.VARCHAR));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateObject() - Should throw an exception when the column
is 0.
**/
    public void Var005()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject",0, "Connecticut", getSQLType(Types.VARCHAR));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateObject() - Should throw an exception when the column
is -1.
**/
    public void Var006()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject",-1, "Massachusetts", getSQLType(Types.VARCHAR));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateObject() - Should work when the column index is valid.
**/
    public void Var007()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject",14, "Rhode Island", getSQLType(Types.VARCHAR));
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_VARCHAR_50");
                rs2.close ();
                assertCondition (v.equals ("Rhode Island"));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateObject() - Should throw an exception when the column
name is null.
**/
    public void Var008()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject",null, "Pennsylvania", getSQLType(Types.VARCHAR));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateObject() - Should throw an exception when the column
name is an empty string.
**/
    public void Var009()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","", "Maryland", getSQLType(Types.VARCHAR));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateObject() - Should throw an exception when the column
name is invalid.
**/
    public void Var010()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","INVALID", "Delaware", getSQLType(Types.VARCHAR));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateObject() - Should work when the column name is valid.
**/
    public void Var011()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_VARCHAR_50", "District of Columbia", getSQLType(Types.VARCHAR));
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_VARCHAR_50");
                rs2.close ();
                assertCondition (v.equals ("District of Columbia"));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateObject() - Should update to SQL NULL when the column
value is null.
**/
    public void Var012()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                // JDReflectionUtil.callMethod_V(rs_,"updateObject","C_VARCHAR_50", null, getSQLType(Types.VARCHAR));
                
                Class[] argClasses = new Class[3]; 
                argClasses[0] = Class.forName("java.lang.String"); 
                argClasses[1] = Class.forName("java.lang.Object"); 
                argClasses[2] = Class.forName("java.sql.SQLType"); 
                Object[] args = new Object[3]; 
                args[0] = "C_VARCHAR_50";
                args[1] = null; 
                args[2] = getSQLType(Types.VARCHAR); 
                JDReflectionUtil.callMethod_V(rs_,"updateObject",argClasses, args );

                
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_VARCHAR_50");
                boolean wn = rs2.wasNull ();
                rs2.close ();
                assertCondition ((v == null) && (wn == true));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateObject() - Should throw an exception when the scale is invalid.
**/
    public void Var013()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_VARCHAR_50", "Virginia", getSQLType(Types.VARCHAR), -1);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateObject() - Should be reflected by get, even if update has
not yet been issued (i.e. update is still pending).
**/
    public void Var014()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_VARCHAR_50", "North Carolina", getSQLType(Types.VARCHAR));
                assertCondition (rs_.getString ("C_VARCHAR_50").equals ("North Carolina"));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
updateObject() - Should be reflected by get, after update has
been issued, but cursor has not been repositioned.
**/
    public void Var015()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_VARCHAR_50", "South Carolina", getSQLType(Types.VARCHAR));
                rs_.updateRow ();
                assertCondition (rs_.getString ("C_VARCHAR_50").equals ("South Carolina"));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
updateObject() - Should be reflected by get, after update has
been issued and cursor has been repositioned.
**/
    public void Var016()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_VARCHAR_50", "Georgia", getSQLType(Types.VARCHAR));
                rs_.updateRow ();
                rs_.beforeFirst ();
                JDRSTest.position (rs_, key_);
                String v = rs_.getString ("C_VARCHAR_50");
                assertCondition (v.equals ("Georgia"));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
updateObject() - Should work when the current row is the insert
row.
**/
    public void Var017()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                rs_.moveToInsertRow ();
                rs_.updateString ("C_KEY", "JDRSUpdateObjectSQLType 1");
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_VARCHAR_50", "Florida", getSQLType(Types.VARCHAR));
                rs_.insertRow ();
                JDRSTest.position (rs_, "JDRSUpdateObjectSQLType 1");
                assertCondition (rs_.getString ("C_VARCHAR_50").equals ("Florida"));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateObject() - Should be reflected by get on insert row, even if
insert has not yet been issued (i.e. insert is still pending).
**/
    public void Var018()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                rs_.moveToInsertRow ();
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_VARCHAR_50", "Ohio", getSQLType(Types.VARCHAR));
                assertCondition (rs_.getString ("C_VARCHAR_50").equals ("Ohio"));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateObject() - Should throw an exception on a deleted row.
**/
    public void Var019()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, "DUMMY_ROW");
                rs_.deleteRow ();
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_VARCHAR_50", "Kentucky", getSQLType(Types.VARCHAR));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


/**
updateObject() - Update a SMALLINT.
**/
    public void Var020 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_SMALLINT", new Short ((short) 243), getSQLType(Types.SMALLINT));
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                short v = rs2.getShort ("C_SMALLINT");
                rs2.close ();
                assertCondition (v == 243);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateObject() - Update a SMALLINT, when the integer is too big.
**/
    public void Var021 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
        int expectedColumn = -1;
        try {
            JDRSTest.position (rs_, key_);
            expectedColumn = rs_.findColumn ("C_SMALLINT");
            JDReflectionUtil.callMethod_V(rs_,"updateObject","C_SMALLINT", new Integer (4873423), getSQLType(Types.SMALLINT));
            rs_.updateRow ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
          if (e instanceof DataTruncation) { 
            DataTruncation dt = (DataTruncation)e;
            assertCondition ((dt.getIndex() == expectedColumn)
                && (dt.getParameter() == false)
                && (dt.getRead() == false)
                && (dt.getDataSize() == 8)
                && (dt.getTransferSize() == 2));
          } else {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
          }

        }
        }
    }



/**
updateObject() - Update a SMALLINT with an object that has a fraction.
This should work.
**/
    public void Var022 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_SMALLINT", new Float (243.6f), getSQLType(Types.SMALLINT));
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                short v = rs2.getShort ("C_SMALLINT");
                rs2.close ();
                assertCondition (v == 243);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateObject() - Update a SMALLINT, when the object is not a number.
**/
    public void Var023 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_SMALLINT", "West Virginia", getSQLType(Types.SMALLINT));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateObject() - Update an INTEGER.
**/
    public void Var024 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_INTEGER", new Integer (-122833), getSQLType(Types.INTEGER));
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                int v = rs2.getInt ("C_INTEGER");
                rs2.close ();
                assertCondition (v == -122833);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateObject() - Update an INTEGER, when the integer is too big.
**/
    public void Var025 ()
    {
	setupRs();
      if (checkJdbc42 ()) {
        int expectedColumn = -1;
        try {
          JDRSTest.position (rs_, key_);
          expectedColumn = rs_.findColumn ("C_INTEGER");
          JDReflectionUtil.callMethod_V(rs_,"updateObject","C_INTEGER", new Long (245545147483647L), getSQLType(Types.INTEGER));
          rs_.updateRow ();
          failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
          if (e instanceof DataTruncation) { 
            DataTruncation dt = (DataTruncation)e;
            assertCondition ((dt.getIndex() == expectedColumn)
                && (dt.getParameter() == false)
                && (dt.getRead() == false)
                && (dt.getDataSize() == 8)
                && (dt.getTransferSize() == 4));
          } else {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
          }
          
        }
      }
      
    }



/**
updateObject() - Update an INTEGER with an object that has a fraction.  This
should work.
**/
    public void Var026 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_INTEGER", new Float (-122833.6f), getSQLType(Types.INTEGER));
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                int v = rs2.getInt ("C_INTEGER");
                rs2.close ();
                assertCondition (v == -122833);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateObject() - Update an INTEGER, when the object is not a number.
**/
    public void Var027 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_INTEGER", "Tennessee", getSQLType(Types.INTEGER));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateObject() - Update a REAL.
**/
    public void Var028 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_REAL", new Float (31.53), getSQLType(Types.REAL));
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                float v = rs2.getFloat ("C_REAL");
                rs2.close ();
                assertCondition (v == 31.53f);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
updateObject() - Update a REAL, when the object is not a number.
**/
    public void Var029 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_INTEGER", "Alabama", getSQLType(Types.REAL));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }




/**
updateObject() - Update a FLOAT.
**/
    public void Var030 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_FLOAT", new Float (-9845.32), getSQLType(Types.FLOAT));
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                float v = rs2.getFloat ("C_FLOAT");
                rs2.close ();
                assertCondition (v == -9845.32f);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateObject() - Update a FLOAT, when the object is not a number.
**/
    public void Var031 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_FLOAT", "Mississippi", getSQLType(Types.FLOAT));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


/**
updateObject() - Update a DOUBLE.
**/
    public void Var032 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_DOUBLE", new Double (19845.987987), getSQLType(Types.DOUBLE));
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                double v = rs2.getDouble ("C_DOUBLE");
                rs2.close ();
                assertCondition (v == 19845.987987);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateObject() - Update a DOUBLE, when the object is not a number.
**/
    public void Var033 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_DOUBLE", "Louisiana", getSQLType(Types.DOUBLE));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


/**
updateObject() - Update a DECIMAL, with no scale specified.
**/
    public void Var034 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_DECIMAL_105", new BigDecimal (-4539.9), getSQLType(Types.DECIMAL));
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                BigDecimal v = rs2.getBigDecimal ("C_DECIMAL_105");
                rs2.close ();
                assertCondition (v.floatValue() == -4539.9f);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateObject() - Update a DECIMAL, when the fraction will truncate.  This is ok.
**/
    public void Var035 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_DECIMAL_105", new BigDecimal (-4539.987666666), getSQLType(Types.DECIMAL));
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                BigDecimal v = rs2.getBigDecimal ("C_DECIMAL_105");
                rs2.close ();
                assertCondition (v.equals(new BigDecimal("-4539.98766")));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateObject() - Update a DECIMAL, with scale -1 specified.
**/
    public void Var036 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_DECIMAL_105", new BigDecimal (-4532.9), getSQLType(Types.DECIMAL), -1);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


/**
updateObject() - Update a DECIMAL, with scale 0 specified.
**/
    public void Var037 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_DECIMAL_105", new BigDecimal (39455.58734), getSQLType(Types.DECIMAL), 0);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                BigDecimal v = rs2.getBigDecimal ("C_DECIMAL_105");
                rs2.close ();
                assertCondition (v.floatValue() == 39455);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
updateObject() - Update a DECIMAL, with scale less than the value's scale.
**/
    public void Var038 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_DECIMAL_105", new BigDecimal (34255.53434), getSQLType(Types.DECIMAL), 3);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                BigDecimal v = rs2.getBigDecimal ("C_DECIMAL_105");
                rs2.close ();
                assertCondition (v.floatValue() == 34255.534f);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
updateObject() - Update a DECIMAL, with scale equal to the value's scale.
**/
    public void Var039 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_DECIMAL_105", new BigDecimal (54321.12345), getSQLType(Types.DECIMAL), 5);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                BigDecimal v = rs2.getBigDecimal ("C_DECIMAL_105");
                rs2.close ();
                assertCondition (v.floatValue() == 54321.12345f);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
updateObject() - Update a DECIMAL, with scale greater the value's scale.
**/
    public void Var040 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_DECIMAL_105", new BigDecimal (54321.12345), getSQLType(Types.DECIMAL), 8);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                BigDecimal v = rs2.getBigDecimal ("C_DECIMAL_105");
                rs2.close ();
                assertCondition (v.floatValue() ==54321.12345f);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
updateObject() - Update a DECIMAL, when the value is too big.
**/
    public void Var041 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
        int expectedColumn = -1;
        try {
            JDRSTest.position (rs_, key_);
            expectedColumn = rs_.findColumn ("C_DECIMAL_105");
            JDReflectionUtil.callMethod_V(rs_,"updateObject","C_DECIMAL_105", new BigDecimal ("394585.432389445"), getSQLType(Types.DECIMAL));
            rs_.updateRow ();
            failed ("Didn't throw SQLException");
        }
	catch (Exception e) {
	    if (e instanceof DataTruncation) { 
		DataTruncation dt = (DataTruncation)e;
		assertCondition ((dt.getIndex() == expectedColumn)
				 && (dt.getParameter() == false)
				 && (dt.getRead() == false)
				 && (dt.getDataSize() == 15)
				 && (dt.getTransferSize() == 10));
	    } else {
		assertSqlException(e, -99999, "07006", "Data type mismatch",
				   "Mismatch instead of truncation in latest toolbox");

	    } 

        }
        }
    }


/**
updateObject() - Update a DECIMAL, when the object is not a number.
**/
    public void Var042 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_DECIMAL_105", "Arkansas", getSQLType(Types.DECIMAL));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateObject() - Update a NUMERIC, with no scale specified.
**/
    public void Var043 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_NUMERIC_105", new BigDecimal (-6639.95), getSQLType(Types.NUMERIC));
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                BigDecimal v = rs2.getBigDecimal ("C_NUMERIC_105");
                rs2.close ();
                assertCondition (v.floatValue() == -6639.95f);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateObject() - Update a NUMERIC, when the fraction will truncate.
**/
    public void Var044 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_NUMERIC_40", new BigDecimal (-4539.98), getSQLType(Types.NUMERIC));
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                BigDecimal v = rs2.getBigDecimal ("C_NUMERIC_40");
                rs2.close ();
                assertCondition (v.equals(new BigDecimal(-4539.0)));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateObject() - Update a NUMERIC, with scale -1 specified.
**/
    public void Var045 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_NUMERIC_105", new BigDecimal (-4432.9), getSQLType(Types.NUMERIC), -1);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


/**
updateObject() - Update a NUMERIC, with scale 0 specified.
**/
    public void Var046 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_NUMERIC_105", new BigDecimal (39458.88734), getSQLType(Types.NUMERIC), 0);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                BigDecimal v = rs2.getBigDecimal ("C_NUMERIC_105");
                rs2.close ();
                assertCondition (v.floatValue() == 39458);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
updateObject() - Update a NUMERIC, with scale less than the value's scale.
**/
    public void Var047 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_NUMERIC_105", new BigDecimal (34255.53434), getSQLType(Types.NUMERIC), 3);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                BigDecimal v = rs2.getBigDecimal ("C_NUMERIC_105");
                rs2.close ();
                assertCondition (v.floatValue() == 34255.534f);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
updateObject() - Update a NUMERIC, with scale equal to the value's scale.
**/
    public void Var048 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_NUMERIC_105", new BigDecimal (54321.12345), getSQLType(Types.NUMERIC), 5);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                BigDecimal v = rs2.getBigDecimal ("C_NUMERIC_105");
                rs2.close ();
                assertCondition (v.floatValue() == 54321.12345f);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
updateObject() - Update a NUMERIC, with scale greater the value's scale.
**/
    public void Var049 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_NUMERIC_105", new BigDecimal (54321.12345), getSQLType(Types.NUMERIC), 8);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                BigDecimal v = rs2.getBigDecimal ("C_NUMERIC_105");
                rs2.close ();
                assertCondition (v.floatValue() == 54321.12345f);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
updateObject() - Update a NUMERIC, when the value is too big.
**/
    public void Var050 ()
    {

	setupRs();
        if (checkJdbc42 ()) {
        int expectedColumn = -1;
        try {
            JDRSTest.position (rs_, key_);
            expectedColumn = rs_.findColumn ("C_NUMERIC_40");
            JDReflectionUtil.callMethod_V(rs_,"updateObject","C_NUMERIC_40", new BigDecimal (394585.0), getSQLType(Types.NUMERIC));
            rs_.updateRow ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
	    if (e instanceof DataTruncation) { 
            DataTruncation dt = (DataTruncation)e;
            assertCondition ((dt.getIndex() == expectedColumn)
                && (dt.getParameter() == false)
                && (dt.getRead() == false)
                && (dt.getDataSize() == 6)
                && (dt.getTransferSize() == 4));
	    } else {
		assertSqlException(e, -99999, "07006", "Data type mismatch",
				   "Mismatch instead of truncation in latest toolbox");

	    }

        }
        }
    }


/**
updateObject() - Update a NUMERIC, when the object is not a number.
**/
    public void Var051 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_NUMERIC_40", "Missouri", getSQLType(Types.NUMERIC));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateObject() - Update a CHAR.
**/
    public void Var052 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_CHAR_50", "Iowa", getSQLType(Types.CHAR));
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_CHAR_50");
                rs2.close ();
                assertCondition (v.equals ("Iowa                                              "));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateObject() - Update a CHAR, when the value is too big.
**/
    public void Var053 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
        int expectedColumn = -1;
        try {
            JDRSTest.position (rs_, key_);
            expectedColumn = rs_.findColumn ("C_CHAR_1");
            JDReflectionUtil.callMethod_V(rs_,"updateObject","C_CHAR_1", "Minnesota", getSQLType(Types.CHAR));
            rs_.updateRow ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            DataTruncation dt = (DataTruncation)e;
            assertCondition ((dt.getIndex() == expectedColumn)
                && (dt.getParameter() == false)
                && (dt.getRead() == false)
                && (dt.getDataSize() == 9)
                && (dt.getTransferSize() == 1));

        }
        }
    }



/**
updateObject() - Update a VARCHAR.
**/
    public void Var054 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_VARCHAR_50", "Wisconsin", getSQLType(Types.VARCHAR));
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_VARCHAR_50");
                rs2.close ();
                assertCondition (v.equals ("Wisconsin"));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateObject() - Update a VARCHAR, when the value is too big.
**/
    public void Var055 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
        int expectedColumn = -1;
        try {
            JDRSTest.position (rs_, key_);
            expectedColumn = rs_.findColumn ("C_VARCHAR_50");
            JDReflectionUtil.callMethod_V(rs_,"updateObject","C_VARCHAR_50", "Michigan, Illinois, Indiana, and whatever states around there that I have missed.", getSQLType(Types.VARCHAR));
            rs_.updateRow ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            DataTruncation dt = (DataTruncation)e;
            assertCondition ((dt.getIndex() == expectedColumn)
                && (dt.getParameter() == false)
                && (dt.getRead() == false)
                && (dt.getDataSize() == 81)
                && (dt.getTransferSize() == 50));

        }
        }
    }



/**
updateObject() - Update a BINARY.
**/
    public void Var056 ()
    {
	setupRs();
        if (checkJdbc42 ()) 
        {
            try 
            {
               String expected;
               if (isToolboxDriver())
                  expected = "F1F2";
               else
                  expected = "Ford";
               
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_BINARY_20", expected, getSQLType(Types.BINARY));
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_BINARY_20");
                rs2.close ();
                assertCondition (v.startsWith (expected));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateObject() - Update a BINARY, when the value is too big.
**/
    public void Var057 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
        int expectedColumn = -1;
        try {
            JDRSTest.position (rs_, key_);
            expectedColumn = rs_.findColumn ("C_BINARY_1");
            JDReflectionUtil.callMethod_V(rs_,"updateObject","C_BINARY_1", "F1F2F3F4", getSQLType(Types.BINARY));
            rs_.updateRow ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            DataTruncation dt = (DataTruncation)e;
            assertCondition ((dt.getIndex() == expectedColumn)
                && (dt.getParameter() == false)
                && (dt.getRead() == false)
                && (dt.getTransferSize() == 1));

        }
        }
    }



/**
updateObject() - Update a VARBINARY.
**/
    public void Var058 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try 
            {
                String expected;
                if (isToolboxDriver())
                   expected = "F1F2";
                else
                   expected = "Ford";

                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_VARBINARY_20", expected, getSQLType(Types.VARBINARY));
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_VARBINARY_20");
                rs2.close ();
                assertCondition (v.equals (expected));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateObject() - Update a VARBINARY, when the value is too big.
**/
    public void Var059 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
        int expectedColumn = -1;
        try {
            JDRSTest.position (rs_, key_);
            expectedColumn = rs_.findColumn ("C_VARBINARY_20");
            JDReflectionUtil.callMethod_V(rs_,"updateObject","C_VARBINARY_20", "E1E2E3E4E5F1F2F3F4F5C1C2C3C4C5D1D2D3D3D5E1E2E3E4E5F1F2F3F4F5C1C2C3C4C5D1D2D3D3D5", getSQLType(Types.VARBINARY));
            rs_.updateRow ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            DataTruncation dt = (DataTruncation)e;
            assertCondition ((dt.getIndex() == expectedColumn)
                && (dt.getParameter() == false)
                && (dt.getRead() == false)
                && (dt.getTransferSize() == 20));

        }
        }
    }



/**
updateObject() - Update a CLOB, when the data is stored in the result
set.
**/
    public void Var060 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            if (checkLobSupport ()) {
                try {
                    JDRSTest.position (rs_, key_);
                    String s = "New Mexico";
                    Clob c = new JDLobTest.JDTestClob (s);
                    JDReflectionUtil.callMethod_V(rs_,"updateObject","C_CLOB", c, getSQLType(Types.CLOB));
                    rs_.updateRow ();
                    ResultSet rs2 = statement2_.executeQuery (select_);
                    JDRSTest.position (rs2, key_);
                    String v = rs2.getString ("C_CLOB");
                    rs2.close ();
                    assertCondition (v.equals (s));
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }



/**
updateObject() - Update a CLOB, when the locator is stored in the result
set.
**/
    public void Var061 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            if (checkLobSupport ()) {
                try {
                    JDRSTest.position (rs3_, key_);
                    String s = "New Mexico";
                    Clob c = new JDLobTest.JDTestClob (s);
                    JDReflectionUtil.callMethod_V(rs3_,"updateObject","C_CLOB", c, getSQLType(Types.CLOB));
                    rs3_.updateRow ();
                    ResultSet rs2 = statement4_.executeQuery (select_);
                    JDRSTest.position (rs2, key_);
                    String v = rs2.getString ("C_CLOB");
                    rs2.close ();
                    assertCondition (v.equals (s));
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }



/**
updateObject() - Update a DBCLOB, when the data is stored in the result
set.
**/
    public void Var062 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            if (checkLobSupport ()) {
                try {
                    JDRSTest.position (rs_, key_);
                    String s = "Arizona";
                    Clob c = new JDLobTest.JDTestClob (s);
                    JDReflectionUtil.callMethod_V(rs_,"updateObject","C_DBCLOB", c, getSQLType(Types.CLOB));
                    rs_.updateRow ();
                    ResultSet rs2 = statement2_.executeQuery (select_);
                    JDRSTest.position (rs2, key_);
                    String v = rs2.getString ("C_DBCLOB");
                    rs2.close ();
                    assertCondition (v.equals (s));
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }



/**
updateObject() - Update a DBCLOB, when the locator is stored in the result
set.
**/
    public void Var063 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            if (checkLobSupport ()) {
                try {
                    JDRSTest.position (rs3_, key_);
                    String s = "Arizona";
                    Clob c = new JDLobTest.JDTestClob (s);
                    JDReflectionUtil.callMethod_V(rs3_,"updateObject","C_DBCLOB", c, getSQLType(Types.CLOB));
                    rs3_.updateRow ();
                    ResultSet rs2 = statement4_.executeQuery (select_);
                    JDRSTest.position (rs2, key_);
                    String v = rs2.getString ("C_DBCLOB");
                    rs2.close ();
                    assertCondition (v.equals (s));
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }



/**
updateObject() - Update a BLOB, when the data is stored in the result
set.
**/
    public void Var064 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            if (checkLobSupport ()) {
                try {
                    JDRSTest.position (rs_, key_);
                    byte[] ba = { 54, 65, -34, 65, 0, -2, -1, 0};
                    Blob b = new JDLobTest.JDTestBlob (ba);
                    JDReflectionUtil.callMethod_V(rs_,"updateObject","C_BLOB", b, getSQLType(Types.BLOB));
                    rs_.updateRow ();
                    ResultSet rs2 = statement2_.executeQuery (select_);
                    JDRSTest.position (rs2, key_);
                    byte[] v = rs2.getBytes ("C_BLOB");
                    rs2.close ();
                    assertCondition (isEqual (v, ba));
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }



/**
updateObject() - Update a BLOB, when the locator is stored in the result
set.
**/
    public void Var065 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            if (checkLobSupport ()) {
                try {
                    JDRSTest.position (rs3_, key_);
                    byte[] ba = { 54, 65, -34, 65, 0, -2, -1, 0};
                    Blob b = new JDLobTest.JDTestBlob (ba);
                    JDReflectionUtil.callMethod_V(rs3_,"updateObject","C_BLOB", b, getSQLType(Types.BLOB));
                    rs3_.updateRow ();
                    ResultSet rs2 = statement4_.executeQuery (select_);
                    JDRSTest.position (rs2, key_);
                    byte[] v = rs2.getBytes ("C_BLOB");
                    rs2.close ();
                    assertCondition (isEqual (v, ba));
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }



/**
updateObject() - Update a DATE.
**/
    public void Var066 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try 
            {
                JDRSTest.position (rs_, key_);
                Date d = new Date (1922400000);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_DATE", d, getSQLType(Types.DATE));
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                Date v = rs2.getDate ("C_DATE");
                rs2.close ();

                if (getDriver () == JDTestDriver.DRIVER_NATIVE ||
                    (JDTestDriver.OSName_.indexOf("OS/400") >= 0))  //@F1A
                  assertCondition (v.toString().equals(d.toString()));
                else{
                  //making more platform generic
                    assertCondition (v.toString().equals(d.toString()));
                }
            } 
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateObject() - Update a DATE, when the string is not a valid date.
**/
    public void Var067 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_DATE", "Nevada", getSQLType(Types.DATE));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateObject() - Update a TIME.
**/
    public void Var068 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                Time t = new Time (7, 36, 0);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_TIME", t, getSQLType(Types.TIME));
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                Time v = rs2.getTime ("C_TIME");
                rs2.close ();
                assertCondition (v.toString ().equals (t.toString ()));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateObject() - Update a TIME, when the string is not a valid time.
**/
    public void Var069 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_TIME", "Wyoming", getSQLType(Types.TIME));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateObject() - Update a TIMESTAMP.
**/
    public void Var070 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                Timestamp ts = new Timestamp (425252435);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_TIMESTAMP", ts, getSQLType(Types.TIMESTAMP));
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                Timestamp v = rs2.getTimestamp ("C_TIMESTAMP");
                rs2.close ();
                assertCondition (v.equals (ts));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateObject() - Update a TIMESTAMP, when the string is not a vliad timestamp.
**/
    public void Var071 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rs_, key_);
                JDReflectionUtil.callMethod_V(rs_,"updateObject","C_TIMESTAMP", "Montana", getSQLType(Types.TIMESTAMP));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateObject() - Update a DATALINK.
**/
    public void Var072 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            if (checkLobSupport ()) {
                // We do not test updating datalinks, since it is not 
                // possible to open a updatable cursor/result set with
                // a datalink column.
                notApplicable("DATALINK update not supported.");
                /*
                try {
                    JDRSTest.position (rs_, key_);
                    JDReflectionUtil.callMethod_V(rs_,"updateObject","C_DATALINK", "Idaho", getSQLType(Types.VARCHAR));
                    failed ("Didn't throw SQLException");
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
                */
            }
        }
    }



/**
updateObject() - Update a DISTINCT.
**/
    public void Var073 ()
    {
	setupRs();
        if (checkJdbc42 ()) {
            if (checkLobSupport ()) {
                try {
                    JDRSTest.position (rs_, key_);
                    JDReflectionUtil.callMethod_V(rs_,"updateObject","C_DISTINCT", "Washing", getSQLType(Types.VARCHAR));
                    rs_.updateRow ();
                    ResultSet rs2 = statement2_.executeQuery (select_);
                    JDRSTest.position (rs2, key_);
                    String v = rs2.getString ("C_DISTINCT");
                    rs2.close ();
                    assertCondition (v.equals ("Washing  "));
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



/**
updateObject() - Update a BIGINT.
**/
    public void Var074()
    {
	setupRs();
        if (checkJdbc42 ()) {
            if (checkBigintSupport()) {
                try {
                    JDRSTest.position (rs_, key_);
                    JDReflectionUtil.callMethod_V(rs_,"updateObject","C_BIGINT", new Integer (223833), getSQLType(Types.BIGINT));
                    rs_.updateRow ();
                    ResultSet rs2 = statement2_.executeQuery (select_);
                    JDRSTest.position (rs2, key_);
                    long v = rs2.getLong("C_BIGINT");
                    rs2.close ();
                    assertCondition (v == 223833);
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



/**
updateObject() - Update an BIGINT, when the object is not a number.
**/
    public void Var075()
    {
	setupRs();
        if (checkJdbc42 ()) {
            if (checkBigintSupport()) {
                try {
                    JDRSTest.position (rs_, key_);
                    JDReflectionUtil.callMethod_V(rs_,"updateObject","C_BIGINT", "Tennessee", getSQLType(Types.BIGINT));
                    failed ("Didn't throw SQLException");
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }

/** B1A
updateObject() - Update an BIGINT, when the integer is too big.
**/
    public void Var076()
    {
        if (checkNative()) {
	    setupRs();
           if (checkJdbc42 ()) {
              if (checkBigintSupport()) {
                 try {
                    JDRSTest.position (rs_, key_);
                    JDReflectionUtil.callMethod_V(rs_,"updateObject","C_BIGINT", new BigDecimal ("-994535874335432445653"), getSQLType(Types.BIGINT));
                    failed ("Didn't throw SQLException");
                 } catch (Exception e) {
		     
		     assertSqlException(e, -99999, "07006", "Data type mismatch",
					"Mismatch instead of truncation in V7R1");
                     /*
                     DataTruncation dt = (DataTruncation)e;
                     assertCondition ((dt.getIndex() == 24)
                         && (dt.getParameter() == false)
                         && (dt.getRead() == false)
                         && (dt.getDataSize() == 18)
                         && (dt.getTransferSize() == 8));
                         */

                 }
              }
           }
        }
    }


    /**
    setObject() - Set a CHAR parameter using Types.BOOLEAN.
    This is by customer request.  Property "translate boolean", true is default

    This is to test property to force toolbox to match native driver's behavior.
    It was decided that the native driver's behavior was correct, but we cannot
    break existing customers' apps, so added this property for customer.
    **/
    public void Var077() {
	if (checkJdbc42 ()) {
	    Connection conn = null;
	    Statement stmt = null;
	// AS400JDBCDataSource ds = null;

	    if (isToolboxDriver()) {
		try {  
		    for (int x = 0; x < 2; x++) {

		    //"boolean string" property setting "true" is default, and driver just used old boolean values in setOjbect
			String trueString = null;
			String falseString = null;
			String url = null;

			if (x == 0) {
			//first time in iteration, use "true" and "false" in setObject
			    trueString = "true"; //toolbox default
			    falseString = "false"; //toolbox default
			//don't set the "boolean string" property, true should be default
			    url = baseURL_;  //;translate boolean=true by default
			    conn = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
			} else if (x == 1) {
			//second time in iteration, use "0" and "1" in setObject
			    trueString = "1"; //toolbox matches Native here
			    falseString = "0"; //toolbox matches Native here
			//this time, explicity set property "boolean string" false
			    url = baseURL_ + ";translate boolean=false";
			    conn = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
			}
			if (conn == null )  {
			  assertCondition(false, "Connect is null "); 
			  return; 
			}
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
						    ResultSet.CONCUR_UPDATABLE);



			PreparedStatement ps = conn.prepareStatement("INSERT INTO " + JDRSTest.RSTEST_GET 
								     + " (C_KEY, C_CHAR_50, C_VARCHAR_50  ) " +
								     " VALUES (?, ?, ?)");
			ps.setString(1, "PROP_CHAR_BOOL1");
			ps.setObject(2, new Boolean(true), Types.BOOLEAN);
			ps.setObject(3, new Boolean(true), Types.BOOLEAN);
			ps.executeUpdate();
			ps.setString(1, "PROP_CHAR_BOOL2");
			ps.setObject(2, new Boolean(false), Types.BOOLEAN);
			ps.setObject(3, new Boolean(false), Types.BOOLEAN);
			ps.executeUpdate();

			ps.close();
			conn.setAutoCommit(false);
			ResultSet rs = stmt.executeQuery("SELECT C_CHAR_50, C_VARCHAR_50 FROM " 
							 + JDRSTest.RSTEST_GET 
							 + " WHERE C_KEY like 'PROP_CHAR_BOOL%' ");

			while (rs.next()) {

			    JDReflectionUtil.callMethod_V(rs,"updateObject","C_CHAR_50", new Boolean(true), getSQLType(Types.CHAR));
			    JDReflectionUtil.callMethod_V(rs,"updateObject","C_VARCHAR_50", new Boolean(true), getSQLType(Types.VARCHAR));
			    rs.updateRow();
			    conn.commit();
			    String retVal = rs.getString("C_CHAR_50").trim();
			    String retVal2 = rs.getString("C_VARCHAR_50");

			    if ( ((retVal.equals( falseString ) == false) && (retVal.equals( trueString ) == false))
				 || ((retVal2.equals( falseString ) == false) && (retVal2.equals( trueString ) == false)) ) {
				failed("SetObject(bool) should be stored as \"" + falseString + "\" or \"" + trueString 
				       + "\" char.  Values from select " + retVal + ", " + retVal2 );

				stmt.close();
				conn.close();
				return;
			    }
			}


			rs = stmt.executeQuery("SELECT C_CHAR_50, C_VARCHAR_50 FROM " 
					       + JDRSTest.RSTEST_GET
					       + " WHERE C_KEY like 'PROP_CHAR_BOOL%' ");

			while (rs.next()) {
			    String retVal = rs.getString("C_CHAR_50").trim();
			    String retVal2 = rs.getString("C_VARCHAR_50");
			    if (  (retVal.equals( trueString ) == false) 
				  || (retVal2.equals( trueString ) == false) )  {
				failed("SetObject(bool) should be stored as \"" + trueString 
				       + "\" char.  Values from select " + retVal + ", " + retVal2 );

				stmt.close();
				conn.close();
				return;
			    }
			}
			conn.setAutoCommit(true);
			stmt.executeUpdate("DELETE FROM " + JDRSTest.RSTEST_GET  + " WHERE C_KEY like 'PROP_CHAR_BOOL%' ");

		    }

		    assertCondition(true);

		} catch (Exception e) {
		    failed(e, "Unexpected Exception");
		}

	    } else
		notApplicable("Toolbox property");
	}
    }
    
  public void dfpTest(String table, Object value, String expected) {
      dfpTest(table, value, expected, expected); 
  }

  public void dfpTest(String table, Object value, String expected, String expected2) {
      if (checkJdbc42 ()) {
	  if (checkDecFloatSupport()) {
	      try {
		  Statement s = connection_.createStatement(
							    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		  ResultSet rs = s.executeQuery("SELECT * FROM " + table + " FOR UPDATE ");
		  rs.next();
	  // JDReflectionUtil.callMethod_V(rs,"updateObject",1, value, getSQLType(Types.OTHER));

		  Class[] argClasses = new Class[3]; 
		  argClasses[0] = Integer.TYPE;  
		  argClasses[1] = Class.forName("java.lang.Object"); 
		  argClasses[2] = Class.forName("java.sql.SQLType"); 
		  Object[] args = new Object[3]; 
		  args[0] = new Integer(1); 
		  args[1] = value; 
		  args[2] = getSQLType(Types.OTHER); 
		  JDReflectionUtil.callMethod_V(rs,"updateObject",argClasses, args );

		  rs.updateRow();

		  ResultSet rs2 = statement2f_.executeQuery("SELECT * FROM " + table);
		  rs2.next();
		  String v = rs2.getString(1);
		  rs2.close();
		  s.close();
		  try {
		      connection_.commit(); 
		  } catch (Exception e) {} 
		  assertCondition((v==null && expected==null) || 
				  (v!=null && v.equals(expected)) || 
				  (v!=null && v.equals(expected2)) , "Got " + v + " from "+ value +" sb " + expected);
	      } catch (Exception e) {
		  failed(e, "Unexpected Exception");
	      }
	  }
      }
  }


   /**
     * updateObject -- set DFP16 to different values and retrieve
     */
    public void Var078 () { dfpTest(JDRSTest.RSTEST_DFP16, "4533.43", "4533.43"); }
    public void Var079 () { dfpTest(JDRSTest.RSTEST_DFP16, "NaN", "NaN");} 
    public void Var080 () { dfpTest(JDRSTest.RSTEST_DFP16, "NAN", "NaN");} 
    public void Var081 () { dfpTest(JDRSTest.RSTEST_DFP16, "+NaN", "NaN");} 
    public void Var082 () { dfpTest(JDRSTest.RSTEST_DFP16, "-NaN", "-NaN");} 
    public void Var083 () { dfpTest(JDRSTest.RSTEST_DFP16, "QNaN", "NaN");} 
    public void Var084 () { dfpTest(JDRSTest.RSTEST_DFP16, "+QNaN", "NaN");} 
    public void Var085 () { dfpTest(JDRSTest.RSTEST_DFP16, "-QNaN", "-NaN");} 
    public void Var086 () { dfpTest(JDRSTest.RSTEST_DFP16, "SNaN", "SNaN");} 
    public void Var087 () { dfpTest(JDRSTest.RSTEST_DFP16, "+SNaN", "SNaN");} 
    public void Var088 () { dfpTest(JDRSTest.RSTEST_DFP16, "-SNaN", "-SNaN");} 
    public void Var089 () { dfpTest(JDRSTest.RSTEST_DFP16, "INF", "Infinity");}
    public void Var090 () { dfpTest(JDRSTest.RSTEST_DFP16, "+INF", "Infinity");}
    public void Var091 () { dfpTest(JDRSTest.RSTEST_DFP16, "-INF", "-Infinity");}
    public void Var092 () { dfpTest(JDRSTest.RSTEST_DFP16, "Infinity", "Infinity");}
    public void Var093 () { dfpTest(JDRSTest.RSTEST_DFP16, "+Infinity", "Infinity");}
    public void Var094 () { dfpTest(JDRSTest.RSTEST_DFP16, "-Infinity", "-Infinity");}
    public void Var095 () { dfpTest(JDRSTest.RSTEST_DFP16, "1234567890123456", "1234567890123456");} 
    public void Var096 () { dfpTest(JDRSTest.RSTEST_DFP16, "-1234567890123456", "-1234567890123456");}
    public void Var097 () { dfpTest(JDRSTest.RSTEST_DFP16, "+1234567890123456","1234567890123456");}
    public void Var098 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver()) && getJDK() <= JVMInfo.JDK_142) {
	    dfpTest(JDRSTest.RSTEST_DFP16, "+1234567890123456E28","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+1234567890123456E28","1.234567890123456E+43");
	}
    }
    public void Var099 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver()) && getJDK() <= JVMInfo.JDK_142) {
	    dfpTest(JDRSTest.RSTEST_DFP16, "+1234567890123456E+28","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+1234567890123456E+28","1.234567890123456E+43");
	}
    }
    public void Var100 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver()) && getJDK() <= JVMInfo.JDK_142) {
	    dfpTest(JDRSTest.RSTEST_DFP16, "+123456789012345.6E+29","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+123456789012345.6E+29","1.234567890123456E+43");
	}
    }
    public void Var101 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver()) && getJDK() <= JVMInfo.JDK_142) {
	    dfpTest(JDRSTest.RSTEST_DFP16, "+0.01234567890123456E+45","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+0.01234567890123456E+45","1.234567890123456E+43");
	}
    }
    public void Var102 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver()) && getJDK() <= JVMInfo.JDK_142) {
	    dfpTest(JDRSTest.RSTEST_DFP16, "-1234567890123456E28","-12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "-1234567890123456E28","-1.234567890123456E+43");
	}
    }
    
    public void Var103 () { dfpTest(JDRSTest.RSTEST_DFP16, "1E0", "1");}
    public void Var104 () { dfpTest(JDRSTest.RSTEST_DFP16, "1.1", "1.1");} 
    public void Var105 () { dfpTest(JDRSTest.RSTEST_DFP16, "1.1E0", "1.1");}
    public void Var106 () { dfpTest(JDRSTest.RSTEST_DFP16, null, null);}

    public void Var107 () { dfpTest(JDRSTest.RSTEST_DFP16, new BigDecimal("4533.43"), "4533.43"); }
    public void Var108 () { dfpTest(JDRSTest.RSTEST_DFP16, new BigDecimal("1234567890123456"), "1234567890123456");} 
    public void Var109 () { dfpTest(JDRSTest.RSTEST_DFP16, new BigDecimal("-1234567890123456"), "-1234567890123456");}
    public void Var110 () { dfpTest(JDRSTest.RSTEST_DFP16, new BigDecimal("+1234567890123456"),"1234567890123456");}
    public void Var111 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver()) && getJDK() <= JVMInfo.JDK_142) {
	    dfpTest(JDRSTest.RSTEST_DFP16, new BigDecimal("+1234567890123456E28"),"12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP16, new BigDecimal("+1234567890123456E28"),"1.234567890123456E+43");
	}
    }
    public void Var112 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver()) && getJDK() <= JVMInfo.JDK_142) {
	    dfpTest(JDRSTest.RSTEST_DFP16, new BigDecimal("+1234567890123456E+28"), "12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP16, new BigDecimal("+1234567890123456E+28"),"1.234567890123456E+43");
	}
    }
    public void Var113 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver()) && getJDK() <= JVMInfo.JDK_142) {
	    dfpTest(JDRSTest.RSTEST_DFP16, new BigDecimal("+123456789012345.6E+29"), "12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP16, new BigDecimal("+123456789012345.6E+29"),"1.234567890123456E+43");
	}
    }
    public void Var114 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver()) && getJDK() <= JVMInfo.JDK_142) {
	    dfpTest(JDRSTest.RSTEST_DFP16, new BigDecimal("+0.01234567890123456E+45"), "12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP16, new BigDecimal("+0.01234567890123456E+45"),"1.234567890123456E+43");
	}
    }
    public void Var115 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver()) && getJDK() <= JVMInfo.JDK_142) {
	    dfpTest(JDRSTest.RSTEST_DFP16, new BigDecimal("-1234567890123456E28"), "-12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP16, new BigDecimal("-1234567890123456E28"),"-1.234567890123456E+43");
	}
    }
    
    public void Var116 () { dfpTest(JDRSTest.RSTEST_DFP16, new BigDecimal("1E0"), "1");}
    public void Var117 () { dfpTest(JDRSTest.RSTEST_DFP16, new BigDecimal("1.1"), "1.1");} 
    public void Var118 () { dfpTest(JDRSTest.RSTEST_DFP16, new BigDecimal("1.1E0"), "1.1");}


    public void Var119 () { dfpTest(JDRSTest.RSTEST_DFP16, new Float(4533.43f), "4533.43"); }
    public void Var120 () { dfpTest(JDRSTest.RSTEST_DFP16, new Float(Float.NaN), "NaN");} 
    public void Var121 () { dfpTest(JDRSTest.RSTEST_DFP16, new Float(Float.POSITIVE_INFINITY), "Infinity");}
    public void Var122 () { dfpTest(JDRSTest.RSTEST_DFP16, new Float(Float.NEGATIVE_INFINITY), "-Infinity");}
    public void Var123 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver()) && getJDK() <= JVMInfo.JDK_142) {
	    dfpTest(JDRSTest.RSTEST_DFP16, new Float(-1234567890123456.0), "-1234567950000000");
	} else {
	    /* with the new native PASE support, this comes back with the zeros*/
	    dfpTest(JDRSTest.RSTEST_DFP16, new Float(-1234567890123456.0), "-1.23456795E+15", "-1234567950000000");
	}
    }
    public void Var124 () { dfpTest(JDRSTest.RSTEST_DFP16, new Double(4533.43f), "4533.43017578125"); }
    public void Var125 () { dfpTest(JDRSTest.RSTEST_DFP16, new Double(Double.NaN), "NaN");} 
    public void Var126 () { dfpTest(JDRSTest.RSTEST_DFP16, new Double(Double.POSITIVE_INFINITY), "Infinity");}
    public void Var127 () { dfpTest(JDRSTest.RSTEST_DFP16, new Double(Double.NEGATIVE_INFINITY), "-Infinity");}
    public void Var128 () { dfpTest(JDRSTest.RSTEST_DFP16, new Double(-1234567890123456.0), "-1234567890123456");}



/**
 New comma decimal separator tests
**/ 

/**
updateObject() - Update a DECIMAL, with no scale specified.
**/
    public void Var129 ()
    {
	
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X";
	setupRsCommaSeparator();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rsCommaSeparator_, key_);
                JDReflectionUtil.callMethod_V(rsCommaSeparator_,"updateObject", "C_DECIMAL_105", new BigDecimal (-4539.9), getSQLType(Types.DECIMAL));
                rsCommaSeparator_.updateRow ();
                ResultSet rs2 = statementCommaSeparator2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                BigDecimal v = rs2.getBigDecimal ("C_DECIMAL_105");
                rs2.close ();
                assertCondition (v.floatValue() == -4539.9f, added);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+added);
            }
        }
    }


/**
updateObject() - Update a DECIMAL, when the fraction will truncate.  This is ok.
**/
    public void Var130 ()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X";
	setupRsCommaSeparator();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rsCommaSeparator_, key_);
                JDReflectionUtil.callMethod_V(rsCommaSeparator_,"updateObject", "C_DECIMAL_105", new BigDecimal (-4539.987666666), getSQLType(Types.DECIMAL));
                rsCommaSeparator_.updateRow ();
                ResultSet rs2 = statementCommaSeparator2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                BigDecimal v = rs2.getBigDecimal ("C_DECIMAL_105");
                rs2.close ();
                assertCondition (v.equals(new BigDecimal("-4539.98766")), added);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+added);
            }
        }
    }



/**
updateObject() - Update a DECIMAL, with scale 0 specified.
**/
    public void Var131 ()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X";
	setupRsCommaSeparator();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rsCommaSeparator_, key_);
                JDReflectionUtil.callMethod_V(rsCommaSeparator_,"updateObject", "C_DECIMAL_105", new BigDecimal (39455.58734), getSQLType(Types.DECIMAL), 0);
                rsCommaSeparator_.updateRow ();
                ResultSet rs2 = statementCommaSeparator2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                BigDecimal v = rs2.getBigDecimal ("C_DECIMAL_105");
                rs2.close ();
                assertCondition (v.floatValue() == 39455, added);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+added);
            }
        }
    }


/**
updateObject() - Update a DECIMAL, with scale less than the value's scale.
**/
    public void Var132 ()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X";
	setupRsCommaSeparator();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rsCommaSeparator_, key_);
                JDReflectionUtil.callMethod_V(rsCommaSeparator_,"updateObject", "C_DECIMAL_105", new BigDecimal (34255.53434), getSQLType(Types.DECIMAL), 3);
                rsCommaSeparator_.updateRow ();
                ResultSet rs2 = statementCommaSeparator2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                BigDecimal v = rs2.getBigDecimal ("C_DECIMAL_105");
                rs2.close ();
                assertCondition (v.floatValue() == 34255.534f, added);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+added);
            }
        }
    }


/**
updateObject() - Update a DECIMAL, with scale equal to the value's scale.
**/
    public void Var133 ()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X";
	setupRsCommaSeparator();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rsCommaSeparator_, key_);
                JDReflectionUtil.callMethod_V(rsCommaSeparator_,"updateObject", "C_DECIMAL_105", new BigDecimal (54321.12345), getSQLType(Types.DECIMAL), 5);
                rsCommaSeparator_.updateRow ();
                ResultSet rs2 = statementCommaSeparator2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                BigDecimal v = rs2.getBigDecimal ("C_DECIMAL_105");
                rs2.close ();
                assertCondition (v.floatValue() == 54321.12345f, added);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+added);
            }
        }
    }


/**
updateObject() - Update a DECIMAL, with scale greater the value's scale.
**/
    public void Var134 ()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X";
	setupRsCommaSeparator();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rsCommaSeparator_, key_);
                JDReflectionUtil.callMethod_V(rsCommaSeparator_,"updateObject", "C_DECIMAL_105", new BigDecimal (54321.12345), getSQLType(Types.DECIMAL), 8);
                rsCommaSeparator_.updateRow ();
                ResultSet rs2 = statementCommaSeparator2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                BigDecimal v = rs2.getBigDecimal ("C_DECIMAL_105");
                rs2.close ();
                assertCondition (v.floatValue() ==54321.12345f, added);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+added);
            }
        }
    }


/**
updateObject() - Update a DECIMAL, when the value is too big.
**/
    public void Var135 ()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X";
	setupRsCommaSeparator();
        if (checkJdbc42 ()) {
        int expectedColumn = -1;
        try {
            JDRSTest.position (rsCommaSeparator_, key_);
            expectedColumn = rsCommaSeparator_.findColumn ("C_DECIMAL_105");
            JDReflectionUtil.callMethod_V(rsCommaSeparator_,"updateObject", "C_DECIMAL_105", new BigDecimal ("394585.432389445"), getSQLType(Types.DECIMAL));
            rsCommaSeparator_.updateRow ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
	    if (e instanceof DataTruncation) { 
            DataTruncation dt = (DataTruncation)e;
            assertCondition ((dt.getIndex() == expectedColumn)
                && (dt.getParameter() == false)
                && (dt.getRead() == false)
                && (dt.getDataSize() == 15)
                && (dt.getTransferSize() == 10), added);
	    } else {
		assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation in latest toolbox ");
	    }

        }
        }
    }




/**
updateObject() - Update a NUMERIC, with no scale specified.
**/
    public void Var136 ()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X";
	setupRsCommaSeparator();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rsCommaSeparator_, key_);
                JDReflectionUtil.callMethod_V(rsCommaSeparator_,"updateObject", "C_NUMERIC_105", new BigDecimal (-6639.95), getSQLType(Types.NUMERIC));
                rsCommaSeparator_.updateRow ();
                ResultSet rs2 = statementCommaSeparator2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                BigDecimal v = rs2.getBigDecimal ("C_NUMERIC_105");
                rs2.close ();
                assertCondition (v.floatValue() == -6639.95f, added);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+added);
            }
        }
    }



/**
updateObject() - Update a NUMERIC, when the fraction will truncate.
**/
    public void Var137 ()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X";
	setupRsCommaSeparator();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rsCommaSeparator_, key_);
                JDReflectionUtil.callMethod_V(rsCommaSeparator_,"updateObject", "C_NUMERIC_40", new BigDecimal (-4539.98), getSQLType(Types.NUMERIC));
                rsCommaSeparator_.updateRow ();
                ResultSet rs2 = statementCommaSeparator2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                BigDecimal v = rs2.getBigDecimal ("C_NUMERIC_40");
                rs2.close ();
                assertCondition (v.equals(new BigDecimal(-4539.0)), added);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+added);
            }
        }
    }


/**
updateObject() - Update a NUMERIC, with scale 0 specified.
**/
    public void Var138 ()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X";
	setupRsCommaSeparator();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rsCommaSeparator_, key_);
                JDReflectionUtil.callMethod_V(rsCommaSeparator_,"updateObject", "C_NUMERIC_105", new BigDecimal (39458.88734), getSQLType(Types.NUMERIC), 0);
                rsCommaSeparator_.updateRow ();
                ResultSet rs2 = statementCommaSeparator2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                BigDecimal v = rs2.getBigDecimal ("C_NUMERIC_105");
                rs2.close ();
                assertCondition (v.floatValue() == 39458, added);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+added);
            }
        }
    }


/**
updateObject() - Update a NUMERIC, with scale less than the value's scale.
**/
    public void Var139 ()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X";
	setupRsCommaSeparator();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rsCommaSeparator_, key_);
                JDReflectionUtil.callMethod_V(rsCommaSeparator_,"updateObject", "C_NUMERIC_105", new BigDecimal (34255.53434), getSQLType(Types.NUMERIC), 3);
                rsCommaSeparator_.updateRow ();
                ResultSet rs2 = statementCommaSeparator2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                BigDecimal v = rs2.getBigDecimal ("C_NUMERIC_105");
                rs2.close ();
                assertCondition (v.floatValue() == 34255.534f, added);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+added);
            }
        }
    }


/**
updateObject() - Update a NUMERIC, with scale equal to the value's scale.
**/
    public void Var140 ()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X";
	setupRsCommaSeparator();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rsCommaSeparator_, key_);
                JDReflectionUtil.callMethod_V(rsCommaSeparator_,"updateObject", "C_NUMERIC_105", new BigDecimal (54321.12345), getSQLType(Types.NUMERIC), 5);
                rsCommaSeparator_.updateRow ();
                ResultSet rs2 = statementCommaSeparator2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                BigDecimal v = rs2.getBigDecimal ("C_NUMERIC_105");
                rs2.close ();
                assertCondition (v.floatValue() == 54321.12345f, added);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+added);
            }
        }
    }


/**
updateObject() - Update a NUMERIC, with scale greater the value's scale.
**/
    public void Var141 ()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X";
	setupRsCommaSeparator();
        if (checkJdbc42 ()) {
            try {
                JDRSTest.position (rsCommaSeparator_, key_);
                JDReflectionUtil.callMethod_V(rsCommaSeparator_,"updateObject", "C_NUMERIC_105", new BigDecimal (54321.12345), getSQLType(Types.NUMERIC), 8);
                rsCommaSeparator_.updateRow ();
                ResultSet rs2 = statementCommaSeparator2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                BigDecimal v = rs2.getBigDecimal ("C_NUMERIC_105");
                rs2.close ();
                assertCondition (v.floatValue() == 54321.12345f, added);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception"+added);
            }
        }
    }


 public void dfpTestCommaSeparator(String table, Object value, String expected) {
      dfpTestCommaSeparator(table, value, expected, expected); 
  }

 public void dfpTestCommaSeparator(String table, Object value, String expected, String expected2) {
     String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X";
     if (checkJdbc42()) { 
	 if (checkDecFloatSupport()) {
	     try {

		 setupRsCommaSeparator(); 
		 Statement s = connectionCommaSeparator_.createStatement(
									 ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		 ResultSet rs = s.executeQuery("SELECT * FROM " + table + " FOR UPDATE ");
		 rs.next();
	  // JDReflectionUtil.callMethod_V(rs,"updateObject",1, value, getSQLType(Types.OTHER));
		 Class[] argClasses = new Class[3]; 
		 argClasses[0] = Integer.TYPE;  
		 argClasses[1] = Class.forName("java.lang.Object"); 
		 argClasses[2] = Class.forName("java.sql.SQLType"); 
		 Object[] args = new Object[3]; 
		 args[0] = new Integer(1); 
		 args[1] = value; 
		 args[2] = getSQLType(Types.OTHER); 
		 JDReflectionUtil.callMethod_V(rs,"updateObject",argClasses, args );

		 rs.updateRow();

		 ResultSet rs2 = statementCommaSeparator2f_.executeQuery("SELECT * FROM " + table);
		 rs2.next();
		 String v = rs2.getString(1);
		 rs2.close();
		 s.close();
		 try {
		     connection_.commit(); 
		 } catch (Exception e) {} 
		 assertCondition((v==null && expected==null) || 
				 (v!=null && v.equals(expected)) || 
				 (v!=null && v.equals(expected2)) , "Got " + v + " from "+ value +" sb " + expected+added);
	     } catch (Exception e) {
		 failed(e, "Unexpected Exception"+added);
	     }
	 }
     }
 }

   /**
     * updateObject -- set DFP16 to different values and retrieve
     */
    public void Var142 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "4533,43", "4533,43"); }
    public void Var143 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "1234567890123456", "1234567890123456");} 
    public void Var144 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "-1234567890123456", "-1234567890123456");}
    public void Var145 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "+1234567890123456","1234567890123456");}
    public void Var146 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver()) && getJDK() <= JVMInfo.JDK_142) {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "+1234567890123456E28","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "+1234567890123456E28","1,234567890123456E+43");
	}
    }
    public void Var147 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver()) && getJDK() <= JVMInfo.JDK_142) {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "+1234567890123456E+28","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "+1234567890123456E+28","1,234567890123456E+43");
	}
    }
    public void Var148 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver()) && getJDK() <= JVMInfo.JDK_142) {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "+123456789012345.6E+29","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "+123456789012345.6E+29","1,234567890123456E+43");
	}
    }
    public void Var149 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver()) && getJDK() <= JVMInfo.JDK_142) {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "+0.01234567890123456E+45","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "+0.01234567890123456E+45","1,234567890123456E+43");
	}
    }
    public void Var150 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver()) && getJDK() <= JVMInfo.JDK_142) {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "-1234567890123456E28","-12345678901234560000000000000000000000000000");
	} else { 
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "-1234567890123456E28","-1,234567890123456E+43");
	}
    }
    
    public void Var151 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "1E0", "1");}
    public void Var152 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "1,1", "1,1");} 
    public void Var153 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "1.1E0", "1,1");}
    public void Var154 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, null, null);}

    public void Var155 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, new BigDecimal("4533.43"), "4533,43"); }
    public void Var156 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, new BigDecimal("1234567890123456"), "1234567890123456");} 
    public void Var157 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, new BigDecimal("-1234567890123456"), "-1234567890123456");}
    public void Var158 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, new BigDecimal("+1234567890123456"),"1234567890123456");}
    public void Var159 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver()) && getJDK() <= JVMInfo.JDK_142) {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, new BigDecimal("+1234567890123456E28"),"12345678901234560000000000000000000000000000");
	} else { 
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, new BigDecimal("+1234567890123456E28"),"1,234567890123456E+43");
	}
    }
    public void Var160 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver()) && getJDK() <= JVMInfo.JDK_142) {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, new BigDecimal("+1234567890123456E+28"), "12345678901234560000000000000000000000000000");
	} else { 
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, new BigDecimal("+1234567890123456E+28"),"1,234567890123456E+43");
	}
    }
    public void Var161 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver()) && getJDK() <= JVMInfo.JDK_142) {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, new BigDecimal("+123456789012345.6E+29"), "12345678901234560000000000000000000000000000");
	} else { 
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, new BigDecimal("+123456789012345.6E+29"),"1,234567890123456E+43");
	}
    }
    public void Var162 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver()) && getJDK() <= JVMInfo.JDK_142) {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, new BigDecimal("+0.01234567890123456E+45"), "12345678901234560000000000000000000000000000");
	} else { 
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, new BigDecimal("+0.01234567890123456E+45"),"1,234567890123456E+43");
	}
    }
    public void Var163 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver()) && getJDK() <= JVMInfo.JDK_142) {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, new BigDecimal("-1234567890123456E28"), "-12345678901234560000000000000000000000000000");
	} else { 
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, new BigDecimal("-1234567890123456E28"),"-1,234567890123456E+43");
	}
    }
    
    public void Var164 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, new BigDecimal("1E0"), "1");}
    public void Var165 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, new BigDecimal("1.1"), "1,1");} 
    public void Var166 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, new BigDecimal("1.1E0"), "1,1");}


    public void Var167 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, new Float(4533.43f), "4533,43"); }
    public void Var168 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, new Float(Float.NaN), "NaN");} 
    public void Var169 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, new Float(Float.POSITIVE_INFINITY), "Infinity");}
    public void Var170 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, new Float(Float.NEGATIVE_INFINITY), "-Infinity");}
    public void Var171 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver()) && getJDK() <= JVMInfo.JDK_142) {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, new Float(-1234567890123456.0), "-1234567950000000");
	} else {
	    /* with the new native PASE support, this comes back with the zeros*/
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, new Float(-1234567890123456.0), "-1,23456795E+15", "-1234567950000000");
	}
    }
    public void Var172 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, new Double(4533.43f), "4533,43017578125"); }
    public void Var173 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, new Double(Double.NaN), "NaN");} 
    public void Var174 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, new Double(Double.POSITIVE_INFINITY), "Infinity");}
    public void Var175 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, new Double(Double.NEGATIVE_INFINITY), "-Infinity");}
    public void Var176 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, new Double(-1234567890123456.0), "-1234567890123456");}



    /**
updateBooleanObject() - Update a BOOLEAN.
**/
  public void updateBooleanObject(Object inObject, String outString, int SQLType) {
    setupRs();
    if (checkBooleanSupport()) {
      try {
        JDRSTest.position(rs_, key_);
         JDReflectionUtil.callMethod_V(rs_,"updateObject","C_BOOLEAN", inObject, getSQLType(Types.BIGINT));
        rs_.updateRow();
        ResultSet rs2 = statement2_.executeQuery(select_);
        JDRSTest.position(rs2, key_);
        String v = rs2.getString("C_BOOLEAN");
        rs2.close();
        assertCondition(outString.equals(v), "got " + v + " sb " + outString);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

      public void Var177() { updateBooleanObject(new Boolean(true),"1",Types.BOOLEAN); } 
      public void Var178() { updateBooleanObject(new Boolean(false),"0",Types.BOOLEAN); } 
      public void Var179() {updateBooleanObject("1", "1",Types.VARCHAR); }
      public void Var180() {updateBooleanObject("0", "0",Types.VARCHAR); }
      public void Var181() { updateBooleanObject("1000", "1",Types.VARCHAR);}
  
  public void Var182() {
    updateBooleanObject("-100", "1",Types.VARCHAR);
  }

  public void Var183() {
    updateBooleanObject("true", "1",Types.VARCHAR);
  }

  public void Var184() {
    updateBooleanObject("false", "0",Types.VARCHAR);
  }

  public void Var185() {
    updateBooleanObject("T", "1",Types.VARCHAR);
  }

  public void Var186() {
    updateBooleanObject("F", "0",Types.VARCHAR);
  }

  public void Var187() {
    updateBooleanObject("yes", "1",Types.VARCHAR);
  }

  public void Var188() {
    updateBooleanObject("no", "0",Types.VARCHAR);
  }
      public void Var189() { updateBooleanObject(new Integer(1),"1",Types.INTEGER); } 
      public void Var190() { updateBooleanObject(new Integer(0),"0",Types.INTEGER); } 
      public void Var191() { updateBooleanObject(new Long(1),"1",Types.BIGINT); } 
      public void Var192() { updateBooleanObject(new Long(0),"0",Types.BIGINT); } 
      public void Var193() { updateBooleanObject(new Float(3.0),"1",Types.FLOAT); } 
      public void Var194() { updateBooleanObject(new Float(0.0),"0",Types.FLOAT); } 
      public void Var195() { updateBooleanObject(new Double(2.0),"1",Types.DOUBLE); } 
      public void Var196() { updateBooleanObject(new Double(0.0),"0",Types.DOUBLE); } 
      public void Var197() { updateBooleanObject(new BigDecimal(3.14),"1",Types.DECIMAL); } 
      public void Var198() { updateBooleanObject(new BigDecimal(0.0),"0",Types.DECIMAL); } 






}



