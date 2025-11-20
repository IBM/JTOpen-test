///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSSetObject3SQLType.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.PS;

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
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDataSource;

import test.JDLobTest;
import test.JDPSTest;
import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JDTestcase;
import test.PasswordVault;



/**
Testcase JDPSSetObject3SQLType.  This tests the following method
of the JDBC PreparedStatement class (3 parameters) :

<ul>
<li>setObject(int,Object,SQLType)
</ul>
**/
public class JDPSSetObject3SQLType
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDPSSetObject3SQLType";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDPSTest.main(newArgs); 
   }



    private Statement           statement_;

    private Connection          connectionCommaSeparator_;

    private Connection          connectionDateTime_;
    private Statement           statementDateTime_;



/**
Constructor.
**/
    public JDPSSetObject3SQLType (AS400 systemObject,
				  Hashtable<String,Vector<String>> namesAndVars,
				  int runMode,
				  FileOutputStream fileOutputStream,
				  
				  String password)
    {
	super (systemObject, "JDPSSetObject3SQLType",
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
	String url = baseURL_
	  
	  
	  + ";data truncation=true";
	connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
	connectionCommaSeparator_ = testDriver_.getConnection (url+";decimal separator=,");
	connectionDateTime_ = testDriver_.getConnection (url+";date format=jis;time format=jis",systemObject_.getUserId(), encryptedPassword_);
	statement_ = connection_.createStatement ();
	statementDateTime_ = connectionDateTime_.createStatement ();
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
      throws Exception
    {
	statement_.close ();
	connection_.close ();
        connection_ = null; 

	connectionCommaSeparator_.close ();
	connectionDateTime_.close(); 
    }

   /*
    * Create the SQLType corresponding to Type. 
    */
    private Object getSQLType(int typesNumber) throws Exception {
	return JDReflectionUtil.callStaticMethod_O("java.sql.JDBCType", "valueOf",  typesNumber);
    }


/**
setObject() - Should throw exception when the prepared
statement is closed.
**/
    public void Var001()
    {
	if (checkJdbc42()) { 
	    try {
		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_NUMERIC_105) VALUES (?)");
		ps.close ();
		JDReflectionUtil.callMethod_V(ps,"setObject", 1 , new BigDecimal (2.3), getSQLType(Types.NUMERIC));
		failed ("Didn't throw SQLException");
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }





/**
setObject() - Should throw exception when an invalid index is
specified.
**/
    public void Var002()
    {
	if (checkJdbc42()) { 
	    try {
		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",100, Integer.valueOf(4), getSQLType(Types.INTEGER));
		ps.close ();
		failed ("Didn't throw SQLException");
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
setObject() - Should throw exception when index is 0.
**/
    public void Var003()
    {
	if (checkJdbc42()) { 

	    try {
		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",0, "Hi Mom", getSQLType(Types.VARCHAR));
		ps.close ();
		failed ("Didn't throw SQLException");
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
setObject() - Should throw exception when index is -1.
**/
    public void Var004()
    {
	if (checkJdbc42()) { 

	    try {
		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",-1, "Yo Dad", getSQLType(Types.VARCHAR));
		ps.close ();
		failed ("Didn't throw SQLException");
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



/**
setObject() - Should work with a valid parameter index
greater than 1.
**/
    @SuppressWarnings("deprecation")
    public void Var005()
    {
	if (checkJdbc42()) { 
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_KEY, C_NUMERIC_105) VALUES (?, ?)");
		ps.setString (1, "Test");
		Class<?>[] argClasses = new Class[3]; 
		argClasses[0] = Integer.TYPE;
		argClasses[1] = Class.forName("java.lang.Object"); 
		argClasses[2] = Class.forName("java.sql.SQLType"); 
		Object[] args = new Object[3]; 
		args[0] = Integer.valueOf(2);
		args[1] = new BigDecimal ("4.3");
		args[2] = getSQLType(Types.NUMERIC); 
		JDReflectionUtil.callMethod_V(ps,"setObject",argClasses, args );
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_105 FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		BigDecimal check = rs.getBigDecimal (1, 1);
		rs.close ();

		assertCondition (check.doubleValue() == 4.3);
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}

    }

/**
setObject() - Should set to SQL NULL when the object is null.
**/
    @SuppressWarnings("deprecation")
    public void Var006()
    {
	if (checkJdbc42()) { 
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_NUMERIC_105) VALUES (?)");
    Class<?>[] argClasses = new Class[3]; 
    argClasses[0] = Integer.TYPE;
    argClasses[1] = Class.forName("java.lang.Object"); 
    argClasses[2] = Class.forName("java.sql.SQLType"); 
    Object[] args = new Object[3]; 
    args[0] = Integer.valueOf(1);
    args[1] = null; 
    args[2] = getSQLType(Types.NUMERIC); 
    JDReflectionUtil.callMethod_V(ps,"setObject",argClasses, args );
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_105 FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		BigDecimal check = rs.getBigDecimal (1, 0);
		boolean wn = rs.wasNull ();
		rs.close ();

		assertCondition ((check == null) && (wn == true));
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
setObject() - Should throw exception when the type is invalid.
**/
    public void Var007()
    { 
	if (checkJdbc42()) { 
	    if (isToolboxDriver()) {
	   //toolbox     
		notApplicable("Toolbox does not use col types");
		return;
	    }
	    try {
		
		    notApplicable("Native does not use col types");
/* 
	      PreparedStatement ps = connection_.prepareStatement (
								   "INSERT INTO " + JDPSTest.PSTEST_SET
								   + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
	      JDReflectionUtil.callMethod_V(ps,"setObject",1, Integer.valueOf(4), 4848484);
	      failed ("Didn't throw SQLException for invalid type of "+4848484);
*/ 
		
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
setObject() - Should throw exception when the parameter is
not an input parameter.
**/
    public void Var008()
    {
	if (checkJdbc42()) { 
	    try {
		PreparedStatement ps = connection_.prepareStatement (
								     "CALL " + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",2, Integer.valueOf(3), getSQLType(Types.INTEGER));
		ps.close ();
		failed ("Didn't throw SQLException");
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
setObject() - Should throw exception when the parameter is
not anything close to being a JDBC-style type.
**/
    @SuppressWarnings("rawtypes")
    public void Var009()
    {
	if (checkJdbc42()) { 
	    try {
		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_SMALLINT) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, new Hashtable (), getSQLType(Types.SMALLINT));
		failed ("Didn't throw SQLException");
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
setObject() - Set a SMALLINT parameter.
**/
    public void Var010()
    {
	if (checkJdbc42()) { 
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_SMALLINT) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, Short.valueOf((short) -33), getSQLType(Types.SMALLINT));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_SMALLINT FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		short check = rs.getShort (1);
		rs.close ();

		assertCondition (check == -33, "returned "+check+" sb -33" );
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}

    }



/**
setObject() - Set a SMALLINT parameter, when the data gets truncated.
**/
    public void Var011()
    {
	if (checkJdbc42()) { 
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_SMALLINT) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, new BigDecimal (-24433323.0), getSQLType(Types.SMALLINT));
		failed ("Didn't throw SQLException");
	    }
	    catch (Exception e) {

		    assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation in V7R1");

	    }
	}
    }


/**
setObject() - Set a SMALLINT parameter, when the data contains a fraction.
This is ok.
**/
    public void Var012()
    {
	if (checkJdbc42()) { 
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_SMALLINT) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, Float.valueOf(-33.3f), getSQLType(Types.SMALLINT));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_SMALLINT FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		short check = rs.getShort (1);
		rs.close ();

		assertCondition (check == -33, "returned "+check+" sb -33" );
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}

    }

/**
setObject() - Set a SMALLINT parameter, when the object is the wrong type.
**/
    public void Var013()
    {
	if (checkJdbc42()) { 
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_SMALLINT) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, Time.valueOf("07:45:00"), getSQLType(Types.SMALLINT));
		failed ("Didn't throw SQLException");
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
setObject() - Set a SMALLINT parameter, when the type is invalid.
**/
    public void Var014()
    {
	if (checkJdbc42()) { 
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_SMALLINT) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, Short.valueOf((short) -33), getSQLType(Types.NULL));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_SMALLINT FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		short check = rs.getShort (1);
		rs.close ();

		assertCondition (check == -33, "returned "+check+" sb -33");
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}

    }

/**
setObject() - Set an INTEGER parameter.
**/
    public void Var015()
    {
	if (checkJdbc42()) { 
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_INTEGER) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, Integer.valueOf(9595), getSQLType(Types.INTEGER));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_INTEGER FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		int check = rs.getInt (1);
		rs.close ();

		assertCondition (check == 9595, "returned "+check+" sb 9595");
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }

/**
setObject() - Set a INTEGER parameter, when the data gets truncated.
**/
    public void Var016()
    {
	if (checkJdbc42()) { 
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_INTEGER) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, new BigDecimal (-24475577533323.0), getSQLType(Types.INTEGER));
		failed ("Didn't throw SQLException");
	    }
	    catch (Exception e) {

		    assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation in V7R1");

	    }
	}

    }

/**
setObject() - Set a INTEGER parameter, when the data contains a fraction.
This is ok.
**/
    public void Var017()
    {
	if (checkJdbc42()) { 
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_INTEGER) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, Float.valueOf(-33.3f), getSQLType(Types.INTEGER));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_INTEGER FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		int check = rs.getInt (1);
		rs.close ();

		assertCondition (check == -33, "returned "+check+" sb -33");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}

    }

/**
setObject() - Set an INTEGER parameter, when the object is the wrong type.
**/
    public void Var018()
    {
	if (checkJdbc42()) { 
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_INTEGER) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, new Date (1234), getSQLType(Types.INTEGER));
		failed ("Didn't throw SQLException");
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
setObject() - Set an INTEGER parameter, when the type is invalid.
**/
    public void Var019()
    {
	if (checkJdbc42()) { 
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_INTEGER) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, Integer.valueOf(9595), getSQLType(Types.DATE));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_INTEGER FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		int check = rs.getInt (1);
		rs.close ();

		assertCondition (check == 9595, "returned "+check+" sb 9595");
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
setObject() - Set an REAL parameter.
**/
    public void Var020()
    {
	if (checkJdbc42()) { 
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_REAL) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, Float.valueOf(4.325f), getSQLType(Types.REAL));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_REAL FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		float check = rs.getFloat (1);
		rs.close ();

		assertCondition (check == 4.325f);
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
setObject() - Set a REAL parameter, when the object is the wrong type.
**/
    public void Var021()
    {
	if (checkJdbc42()) { 
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_REAL) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, "Pierre", getSQLType(Types.REAL));
		failed ("Didn't throw SQLException");
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
setObject() - Set a REAL parameter, when the type is invalid.
**/
    public void Var022()
    {
	if (checkJdbc42()) { 
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_REAL) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, Float.valueOf(4.325f), getSQLType(Types.VARCHAR));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_REAL FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		float check = rs.getFloat (1);
		rs.close ();

		assertCondition (check == 4.325f);
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
setObject() - Set an FLOAT parameter.
**/
    public void Var023()
    {
	if (checkJdbc42()) { 
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_FLOAT) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, Float.valueOf(-34.2f), getSQLType(Types.DOUBLE));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_FLOAT FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		float check = rs.getFloat (1);
		rs.close ();

		assertCondition (check == -34.2f);
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }



/**
setObject() - Set a FLOAT parameter, when the object is the wrong type.
**/
    public void Var024()
    {
	if (checkJdbc42()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_FLOAT) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, "Sioux Falls", getSQLType(Types.DOUBLE));
		failed ("Didn't throw SQLException");
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
setObject() - Set a FLOAT parameter, when the object is the wrong type.
**/
    public void Var025()
    {
	if (checkJdbc42()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_FLOAT) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, Float.valueOf(-34.2f), getSQLType(Types.DATE));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_FLOAT FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		float check = rs.getFloat (1);
		rs.close ();

		assertCondition (check == -34.2f);
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
setObject() - Set an DOUBLE parameter.
**/
    public void Var026()
    {
	if (checkJdbc42()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_DOUBLE) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, Double.valueOf(3.14159), getSQLType(Types.DOUBLE));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_DOUBLE FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		double check = rs.getDouble (1);
		rs.close ();

		assertCondition (check == 3.14159);
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
setObject() - Set a DOUBLE parameter, when the object is the wrong type.
**/
    public void Var027()
    {
	if (checkJdbc42()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_DOUBLE) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, new Timestamp (34422343), getSQLType(Types.DOUBLE));
		failed ("Didn't throw SQLException");
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
setObject() - Set a DOUBLE parameter, when the type is invalid.
**/
    public void Var028()
    {
	if (checkJdbc42()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_DOUBLE) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, Double.valueOf(3.14159), getSQLType(Types.BINARY));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_DOUBLE FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		double check = rs.getDouble (1);
		rs.close ();

		assertCondition (check == 3.14159);
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
setObject() - Set an DECIMAL parameter.
**/
    @SuppressWarnings("deprecation")
    public void Var029()
    {
	if (checkJdbc42()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_DECIMAL_105) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, new BigDecimal ("322.344"), getSQLType(Types.DECIMAL));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_DECIMAL_105 FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		BigDecimal check = rs.getBigDecimal (1, 3);
		rs.close ();

		assertCondition (check.doubleValue () == 322.344);
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
setObject() - Set an DECIMAL parameter, when the data gets truncated.
**/
    public void Var030()
    {
	if (checkJdbc42()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_DECIMAL_50) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, new BigDecimal (-3332232.2344), getSQLType(Types.DECIMAL));
		failed ("Didn't throw SQLException");
	    } catch (Exception e) {
		if (e instanceof DataTruncation) { 
		    DataTruncation dt = (DataTruncation)e;
		    assertCondition ((dt.getIndex() == 1)
				     && (dt.getParameter() == true)
				     && (dt.getRead() == false)
				     && (dt.getTransferSize() == 5));
		} else {
		    assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation in latest toolbox ");
		} 
	    }
	}
    }


/**
setObject() - Set an DECIMAL parameter, when the fraction gets truncated.  This
does not cause a data truncation exception.
**/
    @SuppressWarnings("deprecation")
    public void Var031()
    {
	if (checkJdbc42()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_DECIMAL_50) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, new BigDecimal ("322.344"), getSQLType(Types.DECIMAL));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_DECIMAL_50 FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		BigDecimal check = rs.getBigDecimal (1, 0);
		rs.close ();

		assertCondition (check.doubleValue () == 322, "returned "+check.doubleValue()+" sb 322");
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
setObject() - Set a DECIMAL parameter, when the object is the wrong type.
**/
    public void Var032()
    {
	if (checkJdbc42()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_DECIMAL_105) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, "Friends", getSQLType(Types.DECIMAL));
		failed ("Didn't throw SQLException");
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
setObject() - Set a DECIMAL parameter, when the type is invalid.
**/
    @SuppressWarnings("deprecation")
    public void Var033()
    {
	if (checkJdbc42()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_DECIMAL_105) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, new BigDecimal ("322.344"), getSQLType(Types.BLOB));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_DECIMAL_105 FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		BigDecimal check = rs.getBigDecimal (1, 3);
		rs.close ();

		assertCondition (check.doubleValue () == 322.344);
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
setObject() - Set a NUMERIC parameter.
**/
    @SuppressWarnings("deprecation")
    public void Var034()
    {
	if (checkJdbc42()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_NUMERIC_105) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, new BigDecimal ("-9999.123"), getSQLType(Types.NUMERIC));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_105 FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		BigDecimal check = rs.getBigDecimal (1, 3);
		rs.close ();

		assertCondition (check.doubleValue () == -9999.123);
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
setObject() - Set a NUMERIC parameter, when the data gets truncated.
**/
    public void Var035()
    {
	if (checkJdbc42()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_NUMERIC_50) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, new BigDecimal (-24334423.2344), getSQLType(Types.NUMERIC));
		failed ("Didn't throw SQLException");
	    } catch (Exception e) {

		if (e instanceof DataTruncation) { 
		    DataTruncation dt = (DataTruncation)e;
		    assertCondition ((dt.getIndex() == 1)
				     && (dt.getParameter() == true)
				     && (dt.getRead() == false)
				     && (dt.getTransferSize() == 5));
		} else {
		    assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation in latest toolbox ");
		
		} 

	    }
	}
    }

/**
setObject() - Set an NUMERIC parameter, when the fraction gets truncated.  This
does not cause a data truncation exception.
**/
    @SuppressWarnings("deprecation")
    public void Var036()
    {
	if (checkJdbc42()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_NUMERIC_50) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, new BigDecimal ("322.3443846"), getSQLType(Types.NUMERIC));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_50 FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		BigDecimal check = rs.getBigDecimal (1, 0);
		rs.close ();

		assertCondition (check.doubleValue () == 322);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
setObject() - Set a NUMERIC parameter, when the object is the wrong type.
**/
    public void Var037()
    {
	if (checkJdbc42()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_NUMERIC_105) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, new Time (32354), getSQLType(Types.NUMERIC));
		failed ("Didn't throw SQLException");
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
setObject() - Set a NUMERIC parameter, when the type is invalid.
**/
    @SuppressWarnings("deprecation")
    public void Var038()
    {
	if (checkJdbc42()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_NUMERIC_105) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, new BigDecimal ("-9999.123"), getSQLType(Types.CHAR));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_105 FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		BigDecimal check = rs.getBigDecimal (1, 3);
		rs.close ();

		assertCondition (check.doubleValue () == -9999.123);
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
setObject() - Set a CHAR(50) parameter.
**/
    public void Var039()
    {
	if (checkJdbc42()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_CHAR_50) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, "Nature", getSQLType(Types.CHAR));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_CHAR_50 FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		String check = rs.getString (1);
		rs.close ();

		assertCondition (check.equals ("Nature                                            "));
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
setObject() - Set a CHAR  parameter, when the data gets truncated.
**/
    public void Var040()
    {
	if (checkJdbc42()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_CHAR_1) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, "Sky", getSQLType(Types.CHAR));
		failed ("Didn't throw SQLException");
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.DataTruncation");
	    }
	}
    }


/**
setObject() - Set a CHAR parameter, when the object is the wrong type.
**/
    public void Var041()
    {
	if (checkJdbc42()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_CHAR_50) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, new byte[] { (byte) -12}, getSQLType(Types.CHAR));
		failed ("Didn't throw SQLException");
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
setObject() - Set a CHAR parameter, when the object is the wrong type.
**/
    public void Var042()
    {
	if (checkJdbc42()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_CHAR_50) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, "Nature", getSQLType(Types.DOUBLE));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_CHAR_50 FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		String check = rs.getString (1);
		rs.close ();

		assertCondition (check.equals ("Nature                                            "));
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
setObject() - Set an VARCHAR parameter.
**/
    public void Var043()
    {
	if (checkJdbc42()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_VARCHAR_50) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, "Aberdeen", getSQLType(Types.VARCHAR));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		String check = rs.getString (1);
		rs.close ();

		assertCondition (check.equals ("Aberdeen"));
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
setObject() - Set a VARCHAR  parameter, when the data gets truncated.
**/
    public void Var044()
    {
	if (checkJdbc42()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_VARCHAR_50) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, "JDBC testing is sure fun, especially when you get to test method after method after method, ...", getSQLType(Types.VARCHAR));
		failed ("Didn't throw SQLException");
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.DataTruncation");
	    }
	}
    }


/**
setObject() - Set a VARCHAR parameter, when the object is the wrong type.
**/
    public void Var045()
    {
	if (checkJdbc42()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_VARCHAR_50) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, new int[0], getSQLType(Types.VARCHAR));
		failed ("Didn't throw SQLException");
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
setObject() - Set a VARCHAR parameter, when the type is invalid.
**/
    public void Var046()
    {
	if (checkJdbc42()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_VARCHAR_50) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, "Aberdeen", getSQLType(Types.SMALLINT));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		String check = rs.getString (1);
		rs.close ();

		assertCondition (check.equals ("Aberdeen"));
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
setObject() - Set a CLOB parameter.
**/
    public void Var047()
    {
	if (checkJdbc42 ()) {
	    if (checkLobSupport ()) {
		try {
		    statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		    PreparedStatement ps = connection_.prepareStatement (
									 "INSERT INTO " + JDPSTest.PSTEST_SET
									 + " (C_CLOB) VALUES (?)");
		    JDReflectionUtil.callMethod_V(ps,"setObject",1, new JDLobTest.JDTestClob ("Milbank"), getSQLType(Types.CLOB));
		    ps.executeUpdate ();
		    ps.close ();

		    ResultSet rs = statement_.executeQuery ("SELECT C_CLOB FROM " + JDPSTest.PSTEST_SET);
		    rs.next ();
		    Clob check = rs.getClob (1);
	       // rs.close ();                                                                     // @F1D

		    assertCondition (check.getSubString (1, (int) check.length ()).equals ("Milbank")); // @D1C
		} catch (Exception e) {
		    failed (e, "Unexpected Exception");
		}
	    }
	}
    }




/**
setObject() - Set a CLOB parameter, when the object is the wrong type.
**/
    public void Var048()
    {
	if (checkJdbc42 ()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_CLOB) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, Short.valueOf((short) 3342), getSQLType(Types.CLOB));
		failed ("Didn't throw SQLException");
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



/**
setObject() - Set a CLOB parameter, when the object is the wrong type.
**/
    public void Var049()
    {
	if (checkJdbc42 ()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_CLOB) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, Integer.valueOf(5), getSQLType(Types.NUMERIC));
		failed ("Didn't throw SQLException");
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
setObject() - Set a DBCLOB parameter.
**/
    public void Var050()
    {
	if (checkJdbc42 ()) {
	    succeeded ();
	    /* Need to investigate this variation ...
	    try {
		PreparedStatement ps = connection_.prepareStatement (
		    "INSERT INTO " + JDPSTest.PSTEST_SET
		    + " (C_DBCLOB) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, new JDLobTest.JDTestClob ("Brookings"), getSQLType(Types.CLOB));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_DBCLOB FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		Clob check = rs.getClob (1);
		rs.close ();

		assertCondition (check.getSubString (1, (int) check.length ()).equals ("Sunset")); // @D1C
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	    */
	}
    }



/**
setObject() - Set a DBCLOB parameter, when the object is the wrong type.
**/
    public void Var051()
    {
	if (checkJdbc42 ()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_DBCLOB) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, Long.valueOf(34242), getSQLType(Types.CLOB));
		failed ("Didn't throw SQLException");
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



/**
setObject() - Set a DBCLOB parameter, when the type is invalid.
**/
    public void Var052()
    {
	if (checkJdbc42 ()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_DBCLOB) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, Float.valueOf(4.33f), getSQLType(Types.VARCHAR));
		failed ("Didn't throw SQLException");
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



/**
setObject() - Set a BINARY parameter.
**/
    public void Var053()
    {
	if (checkJdbc42()) { 
	    try
	    {
		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_BINARY_20) VALUES (?)");
		byte[] b = { (byte) 32, (byte) 0, (byte) -1, (byte) -11};
		JDReflectionUtil.callMethod_V(ps,"setObject",1, b, getSQLType(Types.BINARY));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_BINARY_20 FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		byte[] check = rs.getBytes (1);
		rs.close ();

		byte[] b2 = new byte[20];
		System.arraycopy (b, 0, b2, 0, b.length);
		assertCondition (areEqual (check, b2));
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }	



/**
setObject() - Set a BINARY parameter, when data gets truncated.
**/
    public void Var054()
    {
	if (checkJdbc42()) {
	    try {
		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_BINARY_20) VALUES (?)");
		byte[] b = { (byte) 32, (byte) 0, (byte) -1, (byte) -11, (byte) 45,
		(byte) 32, (byte) 12, (byte) 123, (byte) 0,
		(byte) 32, (byte) 12, (byte) 123, (byte) 0,
		(byte) 32, (byte) 12, (byte) 123, (byte) 0,
		(byte) 32, (byte) 12, (byte) 123, (byte) 0,
		(byte) 32, (byte) 12, (byte) 123, (byte) 0
		};
		JDReflectionUtil.callMethod_V(ps,"setObject",1, b, getSQLType(Types.BINARY));
		failed ("Didn't throw SQLException");
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.DataTruncation");
	    }
	}
    }


/**
setObject() - Set a BINARY parameter, when the object is the wrong type.
**/
    public void Var055()
    {
	if (checkJdbc42()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_VARBINARY_20) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, new BigDecimal (34.23), getSQLType(Types.BINARY));
		failed ("Didn't throw SQLException");
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
setObject() - Set a BINARY parameter, when the type is invalid.
**/
    public void Var056()
    {
	if (checkJdbc42()) {
	    try {
		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_BINARY_20) VALUES (?)");
		byte[] b = { (byte) 32, (byte) 0, (byte) -1, (byte) -11};
		JDReflectionUtil.callMethod_V(ps,"setObject",1, b, getSQLType(Types.TIMESTAMP));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_BINARY_20 FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		byte[] check = rs.getBytes (1);
		rs.close ();

		byte[] b2 = new byte[20];
		System.arraycopy (b, 0, b2, 0, b.length);
		assertCondition (areEqual (check, b2));
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
setObject() - Set a VARBINARY parameter.
**/
    public void Var057()
    {
	if (checkJdbc42()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);
		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_VARBINARY_20) VALUES (?)");
		byte[] b = { (byte) -13, (byte) 32, (byte) 0, (byte) -1, (byte) -11};
		JDReflectionUtil.callMethod_V(ps,"setObject",1, b, getSQLType(Types.VARBINARY));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_VARBINARY_20 FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		byte[] check = rs.getBytes (1);
		rs.close ();

		assertCondition (areEqual (check, b));
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }



/**
setObject() - Set a VARBINARY parameter, when data gets truncated.
**/
    public void Var058()
    {
	if (checkJdbc42()) {
	    try {
		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_VARBINARY_20) VALUES (?)");
		byte[] b = { (byte) 32, (byte) 0, (byte) -1, (byte) -11, (byte) 45,
		(byte) 32, (byte) 12, (byte) 123, (byte) 0,
		(byte) 32, (byte) 12, (byte) 123, (byte) 0,
		(byte) 32, (byte) 12, (byte) 123, (byte) 1,
		(byte) 32, (byte) 12, (byte) 123, (byte) 0,
		(byte) 32, (byte) 12, (byte) 123, (byte) 100
		};
		JDReflectionUtil.callMethod_V(ps,"setObject",1, b, getSQLType(Types.VARBINARY));
		failed ("Didn't throw SQLException");
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.DataTruncation");
	    }
	}
    }


/**
setObject() - Set a VARBINARY parameter, when the object is the wrong type.
**/
    public void Var059()
    {
	if (checkJdbc42()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_VARBINARY_20) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, Double.valueOf(34.23), getSQLType(Types.VARBINARY));
		failed ("Didn't throw SQLException");
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
setObject() - Set a VARBINARY parameter, when the type is invalid.
**/
    public void Var060()
    {
	if (checkJdbc42()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_VARBINARY_20) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, new byte[43], getSQLType(Types.FLOAT));
		failed ("Didn't throw SQLException");
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
setObject() - Set a BLOB parameter.
**/
    public void Var061()
    {
	if (checkJdbc42 ()) {
	    try {
		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_BLOB) VALUES (?)");
		byte[] b = new byte[] { (byte) 12, (byte) 94};
		JDReflectionUtil.callMethod_V(ps,"setObject",1, new JDLobTest.JDTestBlob (b), getSQLType(Types.BLOB));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_BLOB FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		Blob check = rs.getBlob (1);
	       // rs.close ();                                                           // @F1D

		assertCondition (areEqual (check.getBytes (1, (int) check.length ()), b)); // @D1C
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }



/**
setObject() - Set a BLOB parameter, when the object is the wrong type.
**/
    public void Var062()
    {
	if (checkJdbc42 ()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_BLOB) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, new Date (3424), getSQLType(Types.BLOB));
		failed ("Didn't throw SQLException");
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



/**
setObject() - Set a BLOB parameter, when the type is invalid.
**/
    public void Var063()
    {
	if (checkJdbc42 ()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_BLOB) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, Time.valueOf("14:55:03"), getSQLType(Types.DATE));
		failed ("Didn't throw SQLException ");
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



/**
setObject() - Set a DATE parameter.
**/
    public void Var064()
    {
	if (checkJdbc42 ()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_DATE) VALUES (?)");
		Date d = new Date (342349439);
		JDReflectionUtil.callMethod_V(ps,"setObject",1, d, getSQLType(Types.DATE));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_DATE FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		Date check = rs.getDate (1);
		rs.close ();

		assertCondition (d.toString ().equals (check.toString ()));
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
setObject() - Set a DATE parameter, when the object is the wrong type.
**/
    public void Var065()
    {
	if (checkJdbc42 ()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_DATE) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, "This is only a test.", getSQLType(Types.DATE));
		failed ("Didn't throw SQLException");
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
setObject() - Set a DATE parameter, when the type is invalid.
**/
    public void Var066()
    {
	if (checkJdbc42 ()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_DATE) VALUES (?)");
		Date d = new Date (342349439);
		JDReflectionUtil.callMethod_V(ps,"setObject",1, d, getSQLType(Types.CHAR));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_DATE FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		Date check = rs.getDate (1);
		rs.close ();

		assertCondition (d.toString ().equals (check.toString ()));
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
setObject() - Set a TIME parameter.
**/
    public void Var067()
    {
	if (checkJdbc42 ()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);
		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_TIME) VALUES (?)");
		Time t = new Time (33333);
		JDReflectionUtil.callMethod_V(ps,"setObject",1, t, getSQLType(Types.TIME));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_TIME FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		Time check = rs.getTime (1);
		rs.close ();

		assertCondition (check.toString ().equals (t.toString ()));
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
setObject() - Set a TIME parameter, when the object is the wrong type.
**/
    public void Var068()
    {
	if (checkJdbc42 ()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_TIME) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, "Water", getSQLType(Types.TIME));
		failed ("Didn't throw SQLException");
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
setObject() - Set a TIME parameter, when the object is the wrong type.
**/
    public void Var069()
    {
	if (checkJdbc42 ()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);
		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_TIME) VALUES (?)");
		Time t = new Time (33333);
		JDReflectionUtil.callMethod_V(ps,"setObject",1, t, getSQLType(Types.NULL));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_TIME FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		Time check = rs.getTime (1);
		rs.close ();

		assertCondition (check.toString ().equals (t.toString ()));
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
setObject() - Set a TIMESTAMP parameter.
**/
    public void Var070()
    {
	if (checkJdbc42 ()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_TIMESTAMP) VALUES (?)");
		Timestamp ts = new Timestamp (343452230);
		JDReflectionUtil.callMethod_V(ps,"setObject",1, ts, getSQLType(Types.TIMESTAMP));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_TIMESTAMP FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		Timestamp check = rs.getTimestamp (1);
		rs.close ();

		assertCondition (check.toString ().equals (ts.toString ()));
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }

/**
setObject() - Set a TIMESTAMP parameter, when the object is the wrong type.
**/
    public void Var071()
    {
	if (checkJdbc42 ()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_TIMESTAMP) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, new BigDecimal (34.2), getSQLType(Types.TIMESTAMP));
		failed ("Didn't throw SQLException");
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
setObject() - Set a TIMESTAMP parameter, when the type is invalid.
**/
    public void Var072()
    {
	if (checkJdbc42 ()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_TIMESTAMP) VALUES (?)");
		Timestamp ts = new Timestamp (343452230);
		JDReflectionUtil.callMethod_V(ps,"setObject",1, ts, getSQLType(Types.INTEGER));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_TIMESTAMP FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		Timestamp check = rs.getTimestamp (1);
		rs.close ();

		assertCondition (check.toString ().equals (ts.toString ()));
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
setObject() - Set a DATALINK parameter.
**/
    public void Var073()
    {
	if (checkJdbc42 ()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);
		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_DATALINK) VALUES (DLVALUE( CAST(? AS VARCHAR(40))))");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, "http://www.yahoo.com/news.html", getSQLType(Types.VARCHAR));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_DATALINK FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		String check = rs.getString (1);
		rs.close ();

		assertCondition (check.equalsIgnoreCase("http://www.yahoo.com/news.html"));
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }



/**
setObject() - Set a DATALINK parameter, when the object is the wrong type.
**/
    public void Var074()
    {
	if (checkJdbc42 ()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_DATALINK) VALUES (DLVALUE( CAST(? AS CHAR(20))))");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, Integer.valueOf(8), getSQLType(Types.INTEGER));
		assertCondition(true);
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }



/**
setObject() - Set a DATALINK parameter, when the type is invalid.
**/
    public void Var075()
    {
	if (checkJdbc42 ()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_DATALINK) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, "http://www.ibm.com/", getSQLType(Types.INTEGER));
		assertCondition(true);
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }



/**
setObject() - Set a DISTINCT parameter.
**/
    public void Var076()
    {
	if (checkJdbc42 ()) {
	    try {
		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_DISTINCT) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, Integer.valueOf(-412), getSQLType(Types.INTEGER));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_DISTINCT FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		int check = rs.getInt (1);
		rs.close ();

		assertCondition (check == -412, "returned "+check+" sb -412");
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }



/**
setObject() - Set a DATALINK parameter, when the object is the wrong type.
**/
    public void Var077()
    {
	if (checkJdbc42 ()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_DATALINK) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, "We are done, soon.", getSQLType(Types.NUMERIC));
		assertCondition(true);
	    } catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }



/**
setObject() - Set a DATALINK parameter, when the type is invalid.
**/
    public void Var078()
    {
	if ((getDriver () == JDTestDriver.DRIVER_NATIVE) && (getJdbcLevel() <= 2)) {
	    notApplicable ("Native driver pre-JDBC 3.0");
	    return;
	}
	if (checkJdbc42 ()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_DATALINK) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, new BigDecimal (3.321), getSQLType(Types.TIMESTAMP));
		failed ("Didn't throw SQLException");
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



/**
setObject() - Set a BIGINT parameter.
**/
    public void Var079()
    {
	if (checkBigintSupport()) {
	    if (checkJdbc42 ()) {
		try {
		    statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		    PreparedStatement ps = connection_.prepareStatement (
									 "INSERT INTO " + JDPSTest.PSTEST_SET
									 + " (C_BIGINT) VALUES (?)");
		    JDReflectionUtil.callMethod_V(ps,"setObject",1, Long.valueOf(959543224556l), getSQLType(Types.BIGINT));
		    ps.executeUpdate ();
		    ps.close ();

		    ResultSet rs = statement_.executeQuery ("SELECT C_BIGINT FROM " + JDPSTest.PSTEST_SET);
		    rs.next ();
		    long check = rs.getLong (1);
		    rs.close ();

		    assertCondition (check == 959543224556l, "returned "+check+" sb 959543224556");
		} catch (Exception e) {
		    failed (e, "Unexpected Exception");
		}
	    }
	}
    }


/**
setFloat() - Set an BIGINT parameter, when the value is too big.  This will
cause a data truncation exception.
**/
    public void Var080()
    {
	if (checkBigintSupport()) {
	    if (checkJdbc42 ()) {
		try {
		    statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		    PreparedStatement ps = connection_.prepareStatement (
									 "INSERT INTO " + JDPSTest.PSTEST_SET
									 + " (C_BIGINT) VALUES (?)");
		    JDReflectionUtil.callMethod_V(ps,"setObject",1, Float.valueOf(-92233720368547758099.0f), getSQLType(Types.BIGINT));
		    failed ("Didn't throw SQLException");
		}
		catch (Exception e) {


			assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation in V7R1");


		}
	    }
	}
    }


/**
setObject() - Set a BIGINT parameter, when the data contains a fraction.
This is ok.
**/
    public void Var081()
    {
	if (checkJdbc42 ()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connection_.prepareStatement (
								     "INSERT INTO " + JDPSTest.PSTEST_SET
								     + " (C_BIGINT) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, Float.valueOf(-33.3f), getSQLType(Types.BIGINT));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_BIGINT FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		int check = rs.getInt (1);
		rs.close ();

		assertCondition (check == -33, "returned "+check+" sb -33"); 
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
setObject() - Set a BIGINT parameter, when the object is the wrong type.
**/
    public void Var082()
    {
	if (checkBigintSupport()) {
	    if (checkJdbc42 ()) {
		try {
		    statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		    PreparedStatement ps = connection_.prepareStatement (
									 "INSERT INTO " + JDPSTest.PSTEST_SET
									 + " (C_BIGINT) VALUES (?)");
		    JDReflectionUtil.callMethod_V(ps,"setObject",1, "This is a test", getSQLType(Types.BIGINT));
		    failed ("Didn't throw SQLException");
		} catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
    }


/**
setObject() - Set a BIGINT parameter, when the type is invalid.
**/
    public void Var083()
    {
	if (checkBigintSupport()) {
	    if (checkJdbc42 ()) {
		try {
		    statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		    PreparedStatement ps = connection_.prepareStatement (
									 "INSERT INTO " + JDPSTest.PSTEST_SET
									 + " (C_BIGINT) VALUES (?)");
		    JDReflectionUtil.callMethod_V(ps,"setObject",1, Long.valueOf(959543224556l), getSQLType(Types.VARCHAR));
		    ps.executeUpdate ();
		    ps.close ();

		    ResultSet rs = statement_.executeQuery ("SELECT C_BIGINT FROM " + JDPSTest.PSTEST_SET);
		    rs.next ();
		    long check = rs.getLong (1);
		    rs.close ();

		    assertCondition (check == 959543224556l, "returned "+check+" sb 959543224556l");
		} catch (Exception e) {
		    failed (e, "Unexpected Exception");
		}
	    }
	}
    }


/**
setObject() - Set an VARCHAR parameter using Types.LONGVARCHAR.
This is by customer request.

SQL400 - Not run through the Toolbox as I don't think they make this work.
**/
  public void Var084() {
    if (checkNative()) {

      if (checkJdbc42()) {
        try {
          statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

          PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
              + JDPSTest.PSTEST_SET + " (C_VARCHAR_50) VALUES (?)");
          JDReflectionUtil.callMethod_V(ps, "setObject", 1, "Aberdeen",
              getSQLType(Types.LONGVARCHAR));
          ps.executeUpdate();
          ps.close();

          ResultSet rs = statement_.executeQuery("SELECT C_VARCHAR_50 FROM "
              + JDPSTest.PSTEST_SET);
          rs.next();
          String check = rs.getString(1);
          rs.close();

          assertCondition(check.equals("Aberdeen"));
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }

  }

  /**
   * setObject() - Set a VARBINARY parameter using Types.LONGVARBINARY. This is
   * by customer request.
   * 
   * SQL400 - Not run through the Toolbox as I don't think they make this work.
   **/
  public void Var085() {
    if (checkNative()) {
      if (checkJdbc42()) {
        try {
          statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

          PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
              + JDPSTest.PSTEST_SET + " (C_VARBINARY_20) VALUES (?)");
          byte[] b = { (byte) -13, (byte) 32, (byte) 0, (byte) -1, (byte) -11 };
          JDReflectionUtil.callMethod_V(ps, "setObject", 1, b,
              getSQLType(Types.LONGVARBINARY));
          ps.executeUpdate();
          ps.close();

          ResultSet rs = statement_.executeQuery("SELECT C_VARBINARY_20 FROM "
              + JDPSTest.PSTEST_SET);
          rs.next();
          byte[] check = rs.getBytes(1);
          rs.close();

          assertCondition(areEqual(check, b));
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
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
    public void Var086() {
	if (checkJdbc42 ()) {
	if (proxy_ != null && (!proxy_.equals(""))) {
	    notApplicable("Testcase doss not work for proxy driver");
	    return; 
	}


	Connection conn = null;
	Statement stmt = null;
	AS400JDBCDataSource ds = null;

	if (isToolboxDriver()) {
	    try {
	       char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      //don't set the "boolean string" property, true should be default
		String url = null;
		url = baseURL_ ;
		conn = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
		for (int x = 0; x < 6; x++) {

		    //"boolean string" property setting "true" is default, and driver just used old boolean values in setOjbect
			String trueString = null;
			String falseString = null;

			if (x == 0) {
			//first time in iteration, use "true" and "false" in setObject
			    trueString = "true"; //toolbox default
			    falseString = "false"; //toolbox default
			//don't set the "boolean string" property, true should be default
			    url = baseURL_ ;
			    conn = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
			} else if (x == 1) {
			//second time in iteration, use "true" and "false" in setObject
			    trueString = "true"; //toolbox matches Native here
			    falseString = "false"; //toolbox matches Native here
			//this time, explicity set property "boolean string" true
			    url = baseURL_ + ";translate boolean=true";
			    conn = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
			}
			else if (x == 2) {
			//third time in iteration, use "0" and "1" in setObject
			    trueString = "1"; //toolbox matches Native here
			    falseString = "0"; //toolbox matches Native here
			//this time, explicity set property "boolean string" false
			    url = baseURL_ + ";translate boolean=false";
			    conn = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
			}
			else if (x == 3) {
			//forth time in iteration, use "true" and "false" in setObject with DS property setter 
			    trueString = "true"; //toolbox default
			    falseString = "false"; //toolbox default
			//don't set the "boolean string" property, true should be default
			    ds = new AS400JDBCDataSource(systemObject_.getSystemName(), systemObject_.getUserId (), charPassword);
			    conn = ds.getConnection();
			} else if (x == 4) {
			//fifth time in iteration, use "true" and "false" in setObject with DS property setter 
			    trueString = "true"; //toolbox matches Native here
			    falseString = "false"; //toolbox matches Native here
			//this time, explicity set property "boolean string" true
			    ds = new AS400JDBCDataSource(systemObject_.getSystemName(), systemObject_.getUserId (), charPassword);
			    JDReflectionUtil.callMethod_V(ds, "setTranslateBoolean", true); 
			/* ds.setTranslateBoolean(true); */ 
			    conn = ds.getConnection();
			}
			else if (x == 5) {
			//sixth time in iteration, use "0" and "1" in setObject with DS property setter 
			    trueString = "1"; //toolbox matches Native here
			    falseString = "0"; //toolbox matches Native here
			//this time, explicity set property "boolean string" false
			    ds = new AS400JDBCDataSource(systemObject_.getSystemName(), systemObject_.getUserId (), charPassword);
			    JDReflectionUtil.callMethod_V(ds, "setTranslateBoolean", false); 
			/* ds.setTranslateBoolean(false); */ 
			    conn = ds.getConnection();
			}

			stmt = conn.createStatement();

			stmt.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

			PreparedStatement ps = conn.prepareStatement("INSERT INTO " + JDPSTest.PSTEST_SET 
								     + " (C_CHAR_50, C_VARCHAR_50, C_LONGVARCHAR_257, C_GRAPHIC_10, C_VARGRAPHIC_10, C_LONGVARGRAPHIC_257  ) " +
								     " VALUES (?, ?, ?, ?, ?, ?)");
			ps.setObject(1, Boolean.valueOf(true), Types.BOOLEAN);
			ps.setObject(2, Boolean.valueOf(true), Types.BOOLEAN);
			ps.setObject(3, Boolean.valueOf(true), Types.BOOLEAN);                                     
			ps.setObject(4, Boolean.valueOf(true), Types.BOOLEAN);
			ps.setObject(5, Boolean.valueOf(true), Types.BOOLEAN);
			ps.setObject(6, Boolean.valueOf(true), Types.BOOLEAN);
			ps.executeUpdate();
			ps.setObject(1, Boolean.valueOf(false), Types.BOOLEAN);
			ps.setObject(2, Boolean.valueOf(false), Types.BOOLEAN);
			ps.setObject(3, Boolean.valueOf(false), Types.BOOLEAN);
			ps.setObject(4, Boolean.valueOf(false), Types.BOOLEAN);
			ps.setObject(5, Boolean.valueOf(false), Types.BOOLEAN);
			ps.setObject(6, Boolean.valueOf(false), Types.BOOLEAN);

			ps.executeUpdate();

			ps.close();

			ResultSet rs = stmt.executeQuery("SELECT C_CHAR_50, C_VARCHAR_50,  C_LONGVARCHAR_257, C_GRAPHIC_10, C_VARGRAPHIC_10 , C_LONGVARGRAPHIC_257 FROM " + JDPSTest.PSTEST_SET );

			while (rs.next()) {
			    String retVal = rs.getString("C_CHAR_50").trim();
			    String retVal2 = rs.getString("C_VARCHAR_50");
			    String retVal3 = rs.getString("C_LONGVARCHAR_257");
			    Boolean retValB4 = Boolean.valueOf(rs.getBoolean("C_GRAPHIC_10"));
			    Boolean retValB5 = Boolean.valueOf(rs.getBoolean("C_VARGRAPHIC_10"));
			    Boolean retValB6 = Boolean.valueOf(rs.getBoolean("C_LONGVARGRAPHIC_257"));
			    if ( ((retVal.equals( falseString ) == false) && (retVal.equals( trueString ) == false))
				 || ((retVal2.equals( falseString ) == false) && (retVal2.equals( trueString ) == false))
				 || ((retVal3.equals( falseString ) == false) && (retVal3.equals( trueString ) == false))) {
				failed("SetObject(bool) should be stored as \"" + falseString + "\" or \"" + trueString 
				       + "\" char.  Values from select " + retVal + ", " + retVal2 + ", " + retVal3 +
				       "boolean values are "+retValB4+" "+retValB5+" "+retValB6+" ");

				stmt.close();
				conn.close();
				return;
			    } /* if */ 
			} /* while rs.next*/ 
		} /* for x */ 

		  PasswordVault.clearPassword(charPassword);

		    assertCondition(true);

	    } catch (Exception e) {
		    failed(e, "Unexpected Exception");
		}

	} else { 
		notApplicable("Toolbox testcase");
	}
	} /* checkJBC42*/ 
    } /* var086 */ 



/**
setObject() - Set an DECFLOAT(16) parameter.
**/
    public void Var087()
    {

	String added = " -- DECFLOAT(16) test added by native driver 05/09/2007"; 
	if (checkDecFloatSupport()) { 


	    if (checkJdbc42 ()) {
		try {


		    statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP16);

		    PreparedStatement ps = connection_.prepareStatement (
									 "INSERT INTO " + JDPSTest.PSTEST_SETDFP16
									 + " VALUES (?)");
		    JDReflectionUtil.callMethod_V(ps,"setObject",1, new BigDecimal ("322.344"), getSQLType(Types.OTHER));
		    ps.executeUpdate ();
		    ps.close ();

		    ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP16);
		    rs.next ();
		    BigDecimal check = rs.getBigDecimal (1);
		    rs.close ();

		    assertCondition (check.doubleValue () == 322.344, "Got "+check.doubleValue() +" expected 322.34 "+added);
		}
		catch (Exception e) {
		    failed (e, "Unexpected Exception "+added);
		}
	    }
	}
    }


/**
setObject() - Set an DECFLOAT(16) parameter, when the data gets truncated.
**/
    public void Var088()
    {
	String added = " -- DECFLOAT(16) test added by native driver 05/09/2007"; 
	if (checkDecFloatSupport()) { 

	    if (checkJdbc42 ()) {
		try {
		    statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP16);

		    PreparedStatement ps = connection_.prepareStatement (
									 "INSERT INTO " + JDPSTest.PSTEST_SETDFP16
									 + " VALUES (?)");

		    JDReflectionUtil.callMethod_V(ps,"setObject",1, new BigDecimal ("9.999999999999999E800"), getSQLType(Types.OTHER));
		    ps.executeUpdate(); 
		    ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP16);
		    rs.next ();
		    BigDecimal check = rs.getBigDecimal (1);
		    rs.close ();

		    failed ("Didn't throw SQLException but got "+check+ added );
		}
		catch (DataTruncation dt) {
		    assertCondition ((dt.getIndex() == 1)
				     && (dt.getParameter() == true)
				     && (dt.getRead() == false)
				     && (dt.getTransferSize() == 5), added);
		} catch (SQLException e) {
		// The native driver will throw a Data Type mismatch since this cannot be expressed
		    boolean success = e.getMessage().indexOf("Data type mismatch") != -1;
		    if (success) { 
			assertCondition(true); 
		    } else { 
			failed(e, "Expected data type mismatch (for native driver)"+added ); 
		    } 


		} catch (Exception e) {
		    failed (e, "Unexpected Exception "+added);

		}
	    }

	}
    }
/**
setObject() - Set an DECFLOAT(16) parameter, when the fraction gets truncated.  This
does not cause a data truncation exception.
**/
    public void Var089()
    {
	String added = " -- DECFLOAT(16) test added by native driver 05/09/2007"; 
	if (checkDecFloatSupport()) { 
	    if (checkJdbc42 ()) {
		try {
		    statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP16);

		    PreparedStatement ps = connection_.prepareStatement (
									 "INSERT INTO " + JDPSTest.PSTEST_SETDFP16
									 + " VALUES (?)");
		    JDReflectionUtil.callMethod_V(ps,"setObject",1, new BigDecimal ("322.34412345678901234567890"), getSQLType(Types.OTHER));
		    ps.executeUpdate ();
		    ps.close ();

		    ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP16);
		    rs.next ();
		    BigDecimal check = rs.getBigDecimal (1);
		    rs.close ();

		    assertCondition (check.toString().equals("322.3441234567890") ||
				     check.toString().equals("322.344123456789"), "Got "+check +" sb 322.3441234567890 or 322.344123456789 "+added);
		}
		catch (Exception e) {
		    failed (e, "Unexpected Exception"+added);
		}

	    }
	}
    }

/**
setObject() - Set a DECFLOAT(16) parameter, when the object is the wrong type.
**/
    public void Var090()
    {
	String added = " -- DECFLOAT(16) test added by native driver 05/09/2007"; 
	if (checkDecFloatSupport()) { 
	    if (checkJdbc42 ()) {
		try {
		    statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP16);

		    PreparedStatement ps = connection_.prepareStatement (
									 "INSERT INTO " + JDPSTest.PSTEST_SETDFP16
									 + " VALUES (?)");
		    JDReflectionUtil.callMethod_V(ps,"setObject",1, "Friends", getSQLType(Types.OTHER));
		    failed ("Didn't throw SQLException"+added);
		}
		catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
    }

    /**
    setObject() - Set an DECFLOAT(34) parameter.
    **/
    public void Var091()
    {

	String added = " -- DECFLOAT(34) test added by native driver 05/09/2007"; 
	if (checkDecFloatSupport()) { 


	    if (checkJdbc42 ()) {
		try {


		    statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP34);

		    PreparedStatement ps = connection_.prepareStatement (
									 "INSERT INTO " + JDPSTest.PSTEST_SETDFP34
									 + " VALUES (?)");
		    JDReflectionUtil.callMethod_V(ps,"setObject",1, new BigDecimal ("322.344"), getSQLType(Types.OTHER));
		    ps.executeUpdate ();
		    ps.close ();

		    ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP34);
		    rs.next ();
		    BigDecimal check = rs.getBigDecimal (1);
		    rs.close ();

		    assertCondition (check.doubleValue () == 322.344, "Got "+check.doubleValue() +" expected 322.34 "+added);
		}
		catch (Exception e) {
		    failed (e, "Unexpected Exception "+added);
		}
	    }
	}
    }


    /**
    setObject() - Set an DECFLOAT(34) parameter, when the data gets truncated.
    **/
    public void Var092()
    {
	String added = " -- DECFLOAT(34) test added by native driver 05/09/2007"; 
	if (checkDecFloatSupport()) { 

	    if (checkJdbc42 ()) {
		try {
		    statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP34);

		    PreparedStatement ps = connection_.prepareStatement (
									 "INSERT INTO " + JDPSTest.PSTEST_SETDFP34
									 + " VALUES (?)");

		    JDReflectionUtil.callMethod_V(ps,"setObject",1, new BigDecimal ("9.999999999999999E56642"), getSQLType(Types.OTHER));
		    ps.executeUpdate(); 


		    ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP34);
		    rs.next ();
		    BigDecimal check = rs.getBigDecimal (1);
		    rs.close ();

		    failed ("Didn't throw SQLException got "+check+ added );
		}
		catch (DataTruncation dt) {
		    assertCondition ((dt.getIndex() == 1)
				     && (dt.getParameter() == true)
				     && (dt.getRead() == false)
				     && (dt.getTransferSize() == 5), added);

		} catch (SQLException e) {
		// The native driver will throw a Data Type mismatch since this cannot be expressed
		    boolean success = e.getMessage().indexOf("Data type mismatch") != -1;
		    if (success) { 
			assertCondition(true); 
		    } else { 
			failed(e, "Expected data type mismatch (for native driver)"+added ); 
		    } 


		} catch (Exception e) {
		    failed (e, "Unexpected Exception "+added);

		}
	    }

	}
    }
    /**
    setObject() - Set an DECFLOAT(34) parameter, when the fraction gets rounded.  This
    does not cause a data truncation exception.
    **/
    public void Var093()
    {
	String added = " -- DECFLOAT(34) test added by native driver 05/09/2007"; 
	if (checkDecFloatSupport()) { 
	    if (checkJdbc42 ()) {
		try {
		    statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP34);

		    PreparedStatement ps = connection_.prepareStatement (
									 "INSERT INTO " + JDPSTest.PSTEST_SETDFP34
									 + " VALUES (?)");
		    JDReflectionUtil.callMethod_V(ps,"setObject",1, new BigDecimal ("322.344123456789012345678901234567890123456789088323"), getSQLType(Types.OTHER));
		    ps.executeUpdate ();
		    ps.close ();

		    ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP34);
		    rs.next ();
		    BigDecimal check = rs.getBigDecimal (1);
		    rs.close ();

		    String expected = "322.3441234567890123456789012345679"; 
		    assertCondition (check.toString().equals(expected), "Got "+check +" sb "+expected+added);
		}
		catch (Exception e) {
		    failed (e, "Unexpected Exception"+added);
		}

	    }
	}
    }

    /**
    setObject() - Set a DECFLOAT(34) parameter, when the object is the wrong type.
    **/
    public void Var094()
    {
	String added = " -- DECFLOAT(34) test added by native driver 05/09/2007"; 
	if (checkDecFloatSupport()) { 
	    if (checkJdbc42 ()) {
		try {
		    statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP34);

		    PreparedStatement ps = connection_.prepareStatement (
									 "INSERT INTO " + JDPSTest.PSTEST_SETDFP34
									 + " VALUES (?)");
		    JDReflectionUtil.callMethod_V(ps,"setObject",1, "Friends", getSQLType(Types.OTHER));
		    failed ("Didn't throw SQLException"+added);
		}
		catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
    }



/**
setObject() - Should work with a valid parameter index
greater than 1 and with decimal separator set to comma.
**/
    @SuppressWarnings("deprecation")
    public void Var095()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 

	if (checkJdbc42 ()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connectionCommaSeparator_.prepareStatement (
										   "INSERT INTO " + JDPSTest.PSTEST_SET
										   + " (C_KEY, C_NUMERIC_105) VALUES (?, ?)");
		ps.setString (1, "Test");
		JDReflectionUtil.callMethod_V(ps,"setObject",2, new BigDecimal ("4.3"), getSQLType(Types.NUMERIC));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_105 FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		BigDecimal check = rs.getBigDecimal (1, 1);
		rs.close ();

		assertCondition (check.doubleValue() == 4.3, added);
	    } catch (Exception e) {
		failed (e, "Unexpected Exception "+added);
	    }
	}
    }



/**
setObject() - Set an DECIMAL parameter  and with decimal separator set to comma.
**/
    @SuppressWarnings("deprecation")
    public void Var096()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 

	if (checkJdbc42 ()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connectionCommaSeparator_.prepareStatement (
										   "INSERT INTO " + JDPSTest.PSTEST_SET
										   + " (C_DECIMAL_105) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, new BigDecimal ("322.344"), getSQLType(Types.DECIMAL));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_DECIMAL_105 FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		BigDecimal check = rs.getBigDecimal (1, 3);
		rs.close ();

		assertCondition (check.doubleValue () == 322.344, added);
	    } catch (Exception e) {
		failed (e, "Unexpected Exception"+added);
	    }
	}
    }


/**
setObject() - Set a NUMERIC parameter and with decimal separator set to comma.
**/
    @SuppressWarnings("deprecation")
    public void Var097()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 

	if (checkJdbc42 ()) {
	    try {
		statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SET);

		PreparedStatement ps = connectionCommaSeparator_.prepareStatement (
										   "INSERT INTO " + JDPSTest.PSTEST_SET
										   + " (C_NUMERIC_105) VALUES (?)");
		JDReflectionUtil.callMethod_V(ps,"setObject",1, new BigDecimal ("-9999.123"), getSQLType(Types.NUMERIC));
		ps.executeUpdate ();
		ps.close ();

		ResultSet rs = statement_.executeQuery ("SELECT C_NUMERIC_105 FROM " + JDPSTest.PSTEST_SET);
		rs.next ();
		BigDecimal check = rs.getBigDecimal (1, 3);
		rs.close ();

		assertCondition (check.doubleValue () == -9999.123, added);
	    } catch (Exception e) {
		failed (e, "Unexpected Exception"+added);
	    }
	}
    }


/**
setObject() - Set an DECFLOAT(16) parameter and with decimal separator set to comma.
**/
    public void Var098()
    {

	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 

	if (checkDecFloatSupport()) { 


	    if (checkJdbc42 ()) {
		try {


		    statement_.executeUpdate ("DELETE FROM " + JDPSTest.PSTEST_SETDFP16);

		    PreparedStatement ps = connectionCommaSeparator_.prepareStatement (
										       "INSERT INTO " + JDPSTest.PSTEST_SETDFP16
										       + " VALUES (?)");
		    JDReflectionUtil.callMethod_V(ps,"setObject",1, new BigDecimal ("322.344"), getSQLType(Types.OTHER));
		    ps.executeUpdate ();
		    ps.close ();

		    ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDPSTest.PSTEST_SETDFP16);
		    rs.next ();
		    BigDecimal check = rs.getBigDecimal (1);
		    rs.close ();

		    assertCondition (check.doubleValue () == 322.344, "Got "+check.doubleValue() +" expected 322.34 "+added);
		}
		catch (Exception e) {
		    failed (e, "Unexpected Exception "+added);
		}
	    }
	}
    }

  /**
   * setObject() - Set an DECFLOAT(34) parameter and with decimal separator set
   * to comma.
   **/
  public void Var099() {

    String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X";

    if (checkDecFloatSupport()) {

      if (checkJdbc42()) {
        try {

          statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SETDFP34);

          PreparedStatement ps = connectionCommaSeparator_
              .prepareStatement("INSERT INTO " + JDPSTest.PSTEST_SETDFP34
                  + " VALUES (?)");
          JDReflectionUtil.callMethod_V(ps, "setObject", 1, new BigDecimal(
              "322.344"), getSQLType(Types.OTHER));
          ps.executeUpdate();
          ps.close();

          ResultSet rs = statement_.executeQuery("SELECT * FROM "
              + JDPSTest.PSTEST_SETDFP34);
          rs.next();
          BigDecimal check = rs.getBigDecimal(1);
          rs.close();

          assertCondition(check.doubleValue() == 322.344,
              "Got " + check.doubleValue() + " expected 322.34 " + added);
        } catch (Exception e) {
          failed(e, "Unexpected Exception " + added);
        }
      }
    }

  }
  
  
          /**
setParameterTest () - Set the specified parameter using an object. 
         * @param setType 
**/
  public void setParameterTest(String columnName, Object setObject, int setType,
      String expectedString) {
    if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (" + columnName + ") VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, setObject,
            getSQLType(setType));
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery(
            "SELECT " + columnName + " FROM " + JDPSTest.PSTEST_SET);
        rs.next();
        String check = rs.getString(1);
        rs.close();

        assertCondition(expectedString.equals(check),
            " got " + check + " sb " + expectedString);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

          /**
setDateTimeParameterTest () - Set the specified parameter using an object. 
         * @param setType 
**/
  public void setDateTimeParameterTest(String columnName, Object setObject, int setType,
      String expectedString) {
    if (checkJdbc42()) {
      try {
        statementDateTime_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connectionDateTime_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (" + columnName + ") VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, setObject,
            getSQLType(setType));
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statementDateTime_.executeQuery(
            "SELECT " + columnName + " FROM " + JDPSTest.PSTEST_SET);
        rs.next();
        String check = rs.getString(1);
        rs.close();

        assertCondition(expectedString.equals(check),
            " got " + check + " sb " + expectedString);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  public void Var100() {
    if (checkBooleanSupport()) {
      setParameterTest("C_BOOLEAN", Boolean.valueOf(true), Types.BOOLEAN, "1");
    }
  }

  public void Var101() {
    if (checkBooleanSupport()) {
      setParameterTest("C_BOOLEAN", Boolean.valueOf(false),Types.BOOLEAN, "0");
    }
  }

  public void Var102() {
    if (checkBooleanSupport()) {
      setParameterTest("C_BOOLEAN", "true", Types.BOOLEAN,"1");
    }
  }

  public void Var103() {
    if (checkBooleanSupport()) {
      setParameterTest("C_BOOLEAN", "false", Types.BOOLEAN, "0");
    }
  }

  public void Var104() {
    if (checkBooleanSupport()) {
      setParameterTest("C_BOOLEAN", Integer.valueOf(100),Types.BOOLEAN, "1");
    }
  }

  public void Var105() {
    if (checkBooleanSupport()) {
      setParameterTest("C_BOOLEAN", Integer.valueOf(0),Types.BOOLEAN, "0");
    }
  }

   public void Var106() { notApplicable(); } 
   public void Var107() { notApplicable(); } 
   public void Var108() { notApplicable(); } 
   public void Var109() { notApplicable(); } 
   public void Var110() { notApplicable(); } 

     public void testSetFailure(String columnName, Object setObject, int setType,
      String expectedMessage) {
    try {
      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (" + columnName + ") VALUES (?)");
      
      JDReflectionUtil.callMethod_V(ps, "setObject", 1, setObject,
            getSQLType(setType));

      ps.execute();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      String message = e.toString();
      assertCondition(message.indexOf(expectedMessage) >= 0,
          "got " + message + " sb " + expectedMessage);
    }

  }

    public void testLocalTimeFailure(String columnName, int sqlType, String expectedMessage) {
    if (checkJdbc42()) {
      try {

        Object o = JDReflectionUtil.callStaticMethod_O(
            "java.time.LocalTime", "of",  21, 02, 04);

        testSetFailure(columnName,  o, sqlType, expectedMessage);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

  }

      public void testLocalDateFailure(String columnName, int sqlType, String expectedMessage) {
    if (checkJdbc42()) {
      try {

        Object o = JDReflectionUtil.callStaticMethod_O(
            "java.time.LocalDate", "of", 2021, 02, 04);

        testSetFailure(columnName,  o, sqlType, expectedMessage);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

  }

  
  public void testLocalDateTimeFailure(String columnName, int sqlType, String expectedMessage) {
    if (checkJdbc42()) {
      try {

        Object o = JDReflectionUtil.callStaticMethod_O(
            "java.time.LocalDateTime", "of", 2021, 02, 04, 21, 02, 04);

        testSetFailure(columnName,  o, sqlType, expectedMessage);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

  }

  public void setTypedParameterTest(String parameterType, Object setObject,
      int sqlType, String expectedString) {
    try {

      PreparedStatement ps = connectionDateTime_
          .prepareStatement("SELECT CAST(? AS " + parameterType
              + ") AS C1 FROM SYSIBM.SYSDUMMY1");
      JDReflectionUtil.callMethod_V(ps, "setObject", 1, setObject,
            getSQLType(sqlType));
      ResultSet rs = ps.executeQuery();
      rs.next();
      String check = rs.getString(1);
      rs.close();
      ps.close();

      assertCondition(expectedString.equals(check),
          " got " + check + " sb " + expectedString);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }  
  
  
  /* Test LocalTime */ 
  public void Var111() { testLocalTimeFailure("C_SMALLINT", Types.SMALLINT, "Data type mismatch");  }
  public void Var112() { testLocalTimeFailure("C_INTEGER", Types.INTEGER, "Data type mismatch");  }
  public void Var113() { testLocalTimeFailure("C_REAL",Types.REAL ,"Data type mismatch"); }
  public void Var114() { testLocalTimeFailure("C_FLOAT",Types.FLOAT ,"Data type mismatch"); }
  public void Var115() { testLocalTimeFailure("C_DOUBLE",Types.DOUBLE ,"Data type mismatch"); }
  public void Var116() { testLocalTimeFailure("C_DECIMAL_50",Types.DECIMAL ,"Data type mismatch"); }
  public void Var117() { testLocalTimeFailure("C_DECIMAL_105",Types.DECIMAL ,"Data type mismatch"); }
  public void Var118() { testLocalTimeFailure("C_NUMERIC_50",Types.NUMERIC ,"Data type mismatch"); }
  public void Var119() { testLocalTimeFailure("C_NUMERIC_105",Types.NUMERIC ,"Data type mismatch"); }
  public void Var120() { testLocalTimeFailure("C_CHAR_1",Types.CHAR ,"Data truncation"); }
  
  public void Var121() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalTime",
            "of", 21, 02, 04);
        setDateTimeParameterTest("C_CHAR_50", o, Types.CHAR,
            "21:02:04                                          ");
      } catch (Exception e) {
        failed(e);
      }
    }
  }
  public void Var122() { 

    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalTime",
            "of", 21, 02, 04);
        setDateTimeParameterTest("C_VARCHAR_50", o, Types.CHAR, "21:02:04");
      } catch (Exception e) {
        failed(e);
      }
    }
  
  }
  
  
  public void Var123() { testLocalTimeFailure("C_BINARY_1",Types.BINARY ,"Data type mismatch"); }
  public void Var124() { testLocalTimeFailure("C_BINARY_20",Types.BINARY ,"Data type mismatch"); }
  public void Var125() { testLocalTimeFailure("C_VARBINARY_20",Types.VARBINARY ,"Data type mismatch"); }
  public void Var126() { testLocalTimeFailure("C_DATE",Types.DATE ,"Data type mismatch"); }
  
  public void Var127() { 
    
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalTime",
            "of", 21, 02, 04);
        setDateTimeParameterTest("C_TIME", o, Types.CHAR, "21:02:04");
      } catch (Exception e) {
        failed(e);
      }
    }
  }    
  
  public void Var128() { testLocalTimeFailure("C_TIMESTAMP",Types.TIMESTAMP ,"Data type mismatch"); }
  public void Var129() { testLocalTimeFailure("C_BLOB",Types.BLOB ,"Data type mismatch"); }
  public void Var130() { testLocalTimeFailure("C_CLOB",Types.CLOB ,"Data type mismatch"); }
  public void Var131() { testLocalTimeFailure("C_DBCLOB",Types.CLOB ,"Data type mismatch"); }
  public void Var132() { testLocalTimeFailure("C_DATALINK",Types.DATALINK ,"Data type mismatch"); }
  public void Var133() { testLocalTimeFailure("C_DISTINCT",Types.DISTINCT ,"Data type mismatch"); }
  public void Var134() { testLocalTimeFailure("C_BIGINT",Types.BIGINT ,"Data type mismatch"); }
  
  public void Var135() {
      if (checkToolbox()) { 
	  if (checkJdbc42()) {
	      try {
		  Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalTime",
								 "of", 21, 02, 04);
		  setDateTimeParameterTest("C_LONGVARCHAR_257", o, Types.CHAR, "21:02:04");
	      } catch (Exception e) {
		  failed(e);
	      }
	  }
      }
  }
  
  public void Var136() { 
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalTime",
            "of", 21, 02, 04);
        setTypedParameterTest("GRAPHIC(10) CCSID 1200 ", o, Types.CHAR, "21:02:04  ");
      } catch (Exception e) {
        failed(e);
      }

    }
  }
  
  public void Var137() { 
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalTime",
            "of", 21, 02, 04);
        setTypedParameterTest("VARGRAPHIC(10) CCSID 1200 ", o, Types.VARCHAR, "21:02:04");
      } catch (Exception e) {
        failed(e);
      }

    }
  }
  public void Var138() {
      if (checkToolbox()) { 

	  if (checkJdbc42()) {
	      try {
		  Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalTime",
								 "of", 21, 02, 04);
		  setTypedParameterTest("VARGRAPHIC(257) CCSID 1200  ", o, Types.VARCHAR, "21:02:04");
	      } catch (Exception e) {
		  failed(e);
	      }

	  }
      }
  }  
  public void Var139() { if (checkBooleanSupport()) testLocalTimeFailure("C_BOOLEAN",Types.BOOLEAN ,"Data type mismatch"); }
  public void Var140() { notApplicable();}   

  
  
    
  /* Test LocalDate */ 
  public void Var141() { testLocalDateFailure("C_SMALLINT", Types.SMALLINT, "Data type mismatch");  }
  public void Var142() { testLocalDateFailure("C_INTEGER", Types.INTEGER, "Data type mismatch");  }
  public void Var143() { testLocalDateFailure("C_REAL",Types.REAL ,"Data type mismatch"); }
  public void Var144() { testLocalDateFailure("C_FLOAT",Types.FLOAT ,"Data type mismatch"); }
  public void Var145() { testLocalDateFailure("C_DOUBLE",Types.DOUBLE ,"Data type mismatch"); }
  public void Var146() { testLocalDateFailure("C_DECIMAL_50",Types.DECIMAL ,"Data type mismatch"); }
  public void Var147() { testLocalDateFailure("C_DECIMAL_105",Types.DECIMAL ,"Data type mismatch"); }
  public void Var148() { testLocalDateFailure("C_NUMERIC_50",Types.NUMERIC ,"Data type mismatch"); }
  public void Var149() { testLocalDateFailure("C_NUMERIC_105",Types.NUMERIC ,"Data type mismatch"); }
  public void Var150() { testLocalDateFailure("C_CHAR_1",Types.CHAR ,"Data truncation"); }
  
  public void Var151() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDate",
            "of", 2021, 02, 04);
        setDateTimeParameterTest("C_CHAR_50", o, Types.CHAR,
            "2021-02-04                                        ");
      } catch (Exception e) {
        failed(e);
      }
    }
  }
  public void Var152() { 

    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDate",
            "of", 2021, 02, 04);
        setDateTimeParameterTest("C_VARCHAR_50", o, Types.CHAR, "2021-02-04");
      } catch (Exception e) {
        failed(e);
      }
    }
  
  }
  
  
  public void Var153() { testLocalDateFailure("C_BINARY_1",Types.BINARY ,"Data type mismatch"); }
  public void Var154() { testLocalDateFailure("C_BINARY_20",Types.BINARY ,"Data type mismatch"); }
  public void Var155() { testLocalDateFailure("C_VARBINARY_20",Types.VARBINARY ,"Data type mismatch"); }
  public void Var156() { testLocalDateFailure("C_TIME",Types.TIME ,"Data type mismatch"); }
  
  public void Var157() { 
    
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDate",
            "of", 2021, 02, 04);
        setDateTimeParameterTest("C_DATE", o, Types.CHAR, "2021-02-04");
      } catch (Exception e) {
        failed(e);
      }
    }
  }    
  
  public void Var158() { testLocalDateFailure("C_TIMESTAMP",Types.TIMESTAMP ,"Data type mismatch"); }
  public void Var159() { testLocalDateFailure("C_BLOB",Types.BLOB ,"Data type mismatch"); }
  public void Var160() { testLocalDateFailure("C_CLOB",Types.CLOB ,"Data type mismatch"); }
  public void Var161() { testLocalDateFailure("C_DBCLOB",Types.CLOB ,"Data type mismatch"); }
  public void Var162() { testLocalDateFailure("C_DATALINK",Types.DATALINK ,"Data type mismatch"); }
  public void Var163() { testLocalDateFailure("C_DISTINCT",Types.DISTINCT ,"Data type mismatch"); }
  public void Var164() { testLocalDateFailure("C_BIGINT",Types.BIGINT ,"Data type mismatch"); }
  
  public void Var165() {
      if (checkToolbox()) { 

	  if (checkJdbc42()) {
	      try {
		  Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDate",
								 "of", 2021, 02, 04);
		  setDateTimeParameterTest("C_LONGVARCHAR_257", o, Types.CHAR, "2021-02-04");
	      } catch (Exception e) {
		  failed(e);
	      }
	  }
      }
  }
  
  public void Var166() { 
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDate",
            "of", 2021, 02, 04);
        setTypedParameterTest("GRAPHIC(10) CCSID 1200 ", o, Types.CHAR, "2021-02-04");
      } catch (Exception e) {
        failed(e);
      }

    }
  }
  
  public void Var167() { 
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDate",
            "of", 2021, 02, 04);
        setTypedParameterTest("VARGRAPHIC(10) CCSID 1200 ", o, Types.VARCHAR, "2021-02-04");
      } catch (Exception e) {
        failed(e);
      }

    }
  }
  public void Var168() {
      if (checkToolbox()) { 

	  if (checkJdbc42()) {
	      try {
		  Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDate",
								 "of", 2021, 02, 04);
		  setTypedParameterTest("VARGRAPHIC(257) CCSID 1200  ", o, Types.VARCHAR, "2021-02-04");
	      } catch (Exception e) {
		  failed(e);
	      }

	  }
      }
  }  
  public void Var169() { if (checkBooleanSupport()) testLocalDateFailure("C_BOOLEAN",Types.BOOLEAN ,"Data type mismatch"); }
  public void Var170() { notApplicable();}   

  
    
  /* Test LocalDateTime */ 
  public void Var171() { testLocalDateTimeFailure("C_SMALLINT", Types.SMALLINT, "Data type mismatch");  }
  public void Var172() { testLocalDateTimeFailure("C_INTEGER", Types.INTEGER, "Data type mismatch");  }
  public void Var173() { testLocalDateTimeFailure("C_REAL",Types.REAL ,"Data type mismatch"); }
  public void Var174() { testLocalDateTimeFailure("C_FLOAT",Types.FLOAT ,"Data type mismatch"); }
  public void Var175() { testLocalDateTimeFailure("C_DOUBLE",Types.DOUBLE ,"Data type mismatch"); }
  public void Var176() { testLocalDateTimeFailure("C_DECIMAL_50",Types.DECIMAL ,"Data type mismatch"); }
  public void Var177() { testLocalDateTimeFailure("C_DECIMAL_105",Types.DECIMAL ,"Data type mismatch"); }
  public void Var178() { testLocalDateTimeFailure("C_NUMERIC_50",Types.NUMERIC ,"Data type mismatch"); }
  public void Var179() { testLocalDateTimeFailure("C_NUMERIC_105",Types.NUMERIC ,"Data type mismatch"); }
  public void Var180() { testLocalDateTimeFailure("C_CHAR_1",Types.CHAR ,"Data truncation"); }
  
  public void Var181() {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDateTime",
            "of", 2021, 02, 04,21,2,4,123456789);
        setDateTimeParameterTest("C_CHAR_50", o, Types.CHAR,
            "2021-02-04 21:02:04.123456789                     ");
      } catch (Exception e) {
        failed(e);
      }
    }
  }
  public void Var182() { 

    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDateTime",
            "of", 2021, 02, 04,21,2,4,123456789);
        setDateTimeParameterTest("C_VARCHAR_50", o, Types.CHAR, "2021-02-04 21:02:04.123456789");
      } catch (Exception e) {
        failed(e);
      }
    }
  
  }
  
  
  public void Var183() { testLocalDateTimeFailure("C_BINARY_1",Types.BINARY ,"Data type mismatch"); }
  public void Var184() { testLocalDateTimeFailure("C_BINARY_20",Types.BINARY ,"Data type mismatch"); }
  public void Var185() { testLocalDateTimeFailure("C_VARBINARY_20",Types.VARBINARY ,"Data type mismatch"); }
  public void Var186() { 
    
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDateTime",
            "of", 2021, 02, 04,21,2,4,123456789);
        setDateTimeParameterTest("C_TIME", o, Types.TIME, "21:02:04");
      } catch (Exception e) {
        failed(e);
      }
    }
  }    

  
  public void Var187() { 
    
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDateTime",
            "of", 2021, 02, 04,21,2,4,123456789);
        setDateTimeParameterTest("C_DATE", o, Types.CHAR, "2021-02-04");
      } catch (Exception e) {
        failed(e);
      }
    }
  }    
  
  public void Var188() { 
    
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDateTime",
            "of", 2021, 02, 04,21,2,4,123456789);
        setDateTimeParameterTest("C_TIMESTAMP", o, Types.TIMESTAMP, "2021-02-04 21:02:04.123456");
      } catch (Exception e) {
        failed(e);
      }
    }
  }    
  public void Var189() { testLocalDateTimeFailure("C_BLOB",Types.BLOB ,"Data type mismatch"); }
  public void Var190() { testLocalDateTimeFailure("C_CLOB",Types.CLOB ,"Data type mismatch"); }
  public void Var191() { testLocalDateTimeFailure("C_DBCLOB",Types.CLOB ,"Data type mismatch"); }
  public void Var192() { testLocalDateTimeFailure("C_DATALINK",Types.DATALINK ,"Data type mismatch"); }
  public void Var193() { testLocalDateTimeFailure("C_DISTINCT",Types.DISTINCT ,"Data type mismatch"); }
  public void Var194() { testLocalDateTimeFailure("C_BIGINT",Types.BIGINT ,"Data type mismatch"); }
  
  public void Var195() {
      if (checkToolbox()) { 

	  if (checkJdbc42()) {
	      try {
		  Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDateTime",
								 "of", 2021, 02, 04,21,2,4,123456789);
		  setDateTimeParameterTest("C_LONGVARCHAR_257", o, Types.CHAR, "2021-02-04 21:02:04.123456789");
	      } catch (Exception e) {
		  failed(e);
	      }
	  }
      }
  }
  
  public void Var196() { 
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDateTime",
            "of", 2021, 02, 04,21,2,4,123456789);
        setTypedParameterTest("GRAPHIC(30) CCSID 1200 ", o, Types.CHAR, "2021-02-04 21:02:04.123456789 ");
      } catch (Exception e) {
        failed(e);
      }

    }
  }
  
  public void Var197() { 
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDateTime",
            "of", 2021, 02, 04,21,2,4,123456789);
        setTypedParameterTest("VARGRAPHIC(30) CCSID 1200 ", o, Types.VARCHAR, "2021-02-04 21:02:04.123456789");
      } catch (Exception e) {
        failed(e);
      }

    }
  }
  public void Var198() {
      if (checkToolbox()) {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDateTime",
            "of", 2021, 02, 04,21,2,4,123456789);
        setTypedParameterTest("VARGRAPHIC(257) CCSID 1200  ", o, Types.VARCHAR, "2021-02-04 21:02:04.123456789");
      } catch (Exception e) {
        failed(e);
      }

    }
  }
}	
  public void Var199() { if (checkBooleanSupport()) testLocalDateTimeFailure("C_BOOLEAN",Types.BOOLEAN ,"Data type mismatch"); }
  public void Var200() { notApplicable();}   

  
    


}
